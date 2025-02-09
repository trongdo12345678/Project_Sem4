package com.customer.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mapper.Brand_mapper;
import com.mapper.Category_mapper;
import com.mapper.Product_mapper;
import com.models.Brand;
import com.models.Category_Product;
import com.models.Comment;
import com.models.PageView;
import com.models.Product;
import com.models.Product_img;
import com.models.Product_spe;
import com.utils.Views;

@Repository
public class ShoppingpageRepository {

	@Autowired
	JdbcTemplate db;
	
	@Autowired
	DetailproductRepository reppd;
	@Autowired
	CommentRepository repcom;
	public List<Product> findAllnopaging(PageView ItemPage, String search, int[] idcate, int[] idbrands,
			String[] status) {
		try {
			// Start with the basic query
			StringBuilder str_query = new StringBuilder(String.format(
					"SELECT p.*, b.%s as brand_name, u.%s as unit_name, c.%s as category_name " + "FROM %s p "
							+ "INNER JOIN %s b ON p.%s = b.%s " + "INNER JOIN %s u ON p.%s = u.%s "
							+ "INNER JOIN %s c ON p.%s = c.%s " + "WHERE 1=1 ",
					Views.COL_BRAND_NAME, Views.COL_UNIT_NAME, Views.COL_CATEGORY_NAME, Views.TBL_PRODUCT,
					Views.TBL_BRAND, Views.COL_PRODUCT_BRAND_ID, Views.COL_BRAND_ID, Views.TBL_UNIT,
					Views.COL_PRODUCT_UNIT_ID, Views.COL_UNIT_ID, Views.TBL_CATEGORY, Views.COL_PRODUCT_CATE_ID,
					Views.COL_CATEGORY_ID));

			List<Object> params = new ArrayList<>(); // To hold query parameters

			// Add search condition if exists
			if (search != null && !search.isEmpty()) {
				str_query.append(" AND (p.Product_name LIKE ? OR p.Description LIKE ?) ");
				params.add("%" + search + "%");
				params.add("%" + search + "%");
			}

			// Add condition for category ids if idcate array is provided
			if (idcate != null && idcate.length > 0) {
				str_query.append(" AND p.").append(Views.COL_PRODUCT_CATE_ID).append(" IN (");
				for (int i = 0; i < idcate.length; i++) {
					str_query.append("?");
					if (i < idcate.length - 1) {
						str_query.append(", ");
					}
					params.add(idcate[i]);
				}
				str_query.append(") ");
			}

			// Add condition for brand ids if idbrands array is provided
			if (idbrands != null && idbrands.length > 0) {
				str_query.append(" AND b.").append(Views.COL_BRAND_ID).append(" IN (");
				for (int i = 0; i < idbrands.length; i++) {
					str_query.append("?");
					if (i < idbrands.length - 1) {
						str_query.append(", ");
					}
					params.add(idbrands[i]);
				}
				str_query.append(") ");
			}

			// Add condition for status if status array is provided
			if (status != null && status.length > 0) {
				str_query.append(" AND p.Status IN (");
				for (int i = 0; i < status.length; i++) {
					str_query.append("?");
					if (i < status.length - 1) {
						str_query.append(", ");
					}
					params.add(status[i]);
				}
				str_query.append(") ");
			}

			// Add ORDER BY clause
			str_query.append(" ORDER BY p.").append(Views.COL_PRODUCT_ID).append(" DESC");

			// Execute the query and return the results
			return db.query(str_query.toString(), new Product_mapper(), params.toArray());

		} catch (DataAccessException e) {
			System.err.println("Error fetching products: " + e.getMessage());
			return Collections.emptyList();
		}
	}

	public List<Product> findAllpaging(PageView ItemPage, String search, int[] idcate, int[] idbrands,
			String[] status) {
		try {
			// Start with the basic query for products
			StringBuilder str_query = new StringBuilder(String.format(
					"SELECT p.*, b.%s as brand_name, u.%s as unit_name, c.%s as category_name " + "FROM %s p "
							+ "INNER JOIN %s b ON p.%s = b.%s " + "INNER JOIN %s u ON p.%s = u.%s "
							+ "INNER JOIN %s c ON p.%s = c.%s " + "WHERE 1=1 ",
					Views.COL_BRAND_NAME, Views.COL_UNIT_NAME, Views.COL_CATEGORY_NAME, Views.TBL_PRODUCT,
					Views.TBL_BRAND, Views.COL_PRODUCT_BRAND_ID, Views.COL_BRAND_ID, Views.TBL_UNIT,
					Views.COL_PRODUCT_UNIT_ID, Views.COL_UNIT_ID, Views.TBL_CATEGORY, Views.COL_PRODUCT_CATE_ID,
					Views.COL_CATEGORY_ID));

			List<Object> params = new ArrayList<>(); // To hold query parameters

			// StringBuilder to hold the common conditions
			StringBuilder commonConditions = new StringBuilder();

			// Add search condition if exists
			if (search != null && !search.isEmpty()) {
				commonConditions.append(" AND (p.Product_name LIKE ? OR p.Description LIKE ?) ");
				params.add("%" + search + "%");
				params.add("%" + search + "%");
			}

			if (idcate != null && idcate.length > 0) {
				commonConditions.append(" AND p.").append(Views.COL_PRODUCT_CATE_ID).append(" IN (");
				for (int i = 0; i < idcate.length; i++) {
					commonConditions.append("?");
					params.add(idcate[i]); // Add the current category ID to the params
					if (i < idcate.length - 1) {
						commonConditions.append(", "); // Add comma for the next ID
					}
				}
				commonConditions.append(") ");
			}

			// Add condition for brand ids if idbrands array is provided
			if (idbrands != null && idbrands.length > 0) {
				commonConditions.append(" AND b.").append(Views.COL_BRAND_ID).append(" IN (");
				for (int i = 0; i < idbrands.length; i++) {
					commonConditions.append("?");
					params.add(idbrands[i]); // Add the current brand ID to the params
					if (i < idbrands.length - 1) {
						commonConditions.append(", "); // Add comma for the next ID
					}
				}
				commonConditions.append(") ");
			}

			// Add condition for status if status array is provided
			if (status != null && status.length > 0) {
				commonConditions.append(" AND p.Status IN (");
				for (int i = 0; i < status.length; i++) {
					commonConditions.append("?");
					params.add(status[i]); // Add the current status to the params
					if (i < status.length - 1) {
						commonConditions.append(", "); // Add comma for the next status
					}
				}
				commonConditions.append(") ");
			}

			// Append common conditions to the main query
			str_query.append(commonConditions);

			// Append the ORDER BY clause (important for OFFSET and FETCH)
			str_query.append(" ORDER BY p.").append(Views.COL_PRODUCT_ID).append(" DESC");

			// Count query should reflect the same filters
			String countQuery = String.format(
					"SELECT COUNT(*) FROM %s p " + "INNER JOIN %s b ON p.%s = b.%s " + "INNER JOIN %s u ON p.%s = u.%s "
							+ "INNER JOIN %s c ON p.%s = c.%s " + "WHERE 1=1 %s",
					Views.TBL_PRODUCT, Views.TBL_BRAND, Views.COL_PRODUCT_BRAND_ID, Views.COL_BRAND_ID, Views.TBL_UNIT,
					Views.COL_PRODUCT_UNIT_ID, Views.COL_UNIT_ID, Views.TBL_CATEGORY, Views.COL_PRODUCT_CATE_ID,
					Views.COL_CATEGORY_ID, commonConditions.toString());

			// Execute count query
			int count = db.queryForObject(countQuery, Integer.class, params.toArray());
			int total_page = (int) Math.ceil((double) count / ItemPage.getPage_size());
			ItemPage.setTotal_page(total_page);

			// Handle pagination
			if (ItemPage.isPaginationEnabled()) {
				// Add OFFSET and FETCH NEXT for pagination
				str_query.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
				params.add((ItemPage.getPage_current() - 1) * ItemPage.getPage_size());
				params.add(ItemPage.getPage_size());
			}

			// Execute the query and return the results
			return db.query(str_query.toString(), new Product_mapper(), params.toArray());

		} catch (DataAccessException e) {
			System.err.println("Error fetching products: " + e.getMessage());
			return Collections.emptyList();
		}
	}

	public List<Brand> findAllBrand() {
		try {
			String sql = "SELECT * FROM Brand";
			return db.query(sql, new Brand_mapper());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Category_Product> findAllCate() {
		try {
			String sql = "SELECT * FROM Product_category";
			return db.query(sql, new Category_mapper());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Product> findAllpagingapi(PageView ItemPage, String search, int[] idcate, int[] idbrands,
			String[] status) {
		try {
			// Start with the basic query for products
			StringBuilder str_query = new StringBuilder(String.format(
					"SELECT p.*, b.%s as brand_name, u.%s as unit_name, c.%s as category_name " + "FROM %s p "
							+ "INNER JOIN %s b ON p.%s = b.%s " + "INNER JOIN %s u ON p.%s = u.%s "
							+ "INNER JOIN %s c ON p.%s = c.%s " + "WHERE 1=1 ",
					Views.COL_BRAND_NAME, Views.COL_UNIT_NAME, Views.COL_CATEGORY_NAME, Views.TBL_PRODUCT,
					Views.TBL_BRAND, Views.COL_PRODUCT_BRAND_ID, Views.COL_BRAND_ID, Views.TBL_UNIT,
					Views.COL_PRODUCT_UNIT_ID, Views.COL_UNIT_ID, Views.TBL_CATEGORY, Views.COL_PRODUCT_CATE_ID,
					Views.COL_CATEGORY_ID));

			List<Object> params = new ArrayList<>(); // To hold query parameters

			// StringBuilder to hold the common conditions
			StringBuilder commonConditions = new StringBuilder();

			// Add search condition if exists
			if (search != null && !search.isEmpty()) {
				commonConditions.append(" AND (p.Product_name LIKE ? OR p.Description LIKE ?) ");
				params.add("%" + search + "%");
				params.add("%" + search + "%");
			}

			if (idcate != null && idcate.length > 0) {
				commonConditions.append(" AND p.").append(Views.COL_PRODUCT_CATE_ID).append(" IN (");
				for (int i = 0; i < idcate.length; i++) {
					commonConditions.append("?");
					params.add(idcate[i]); // Add the current category ID to the params
					if (i < idcate.length - 1) {
						commonConditions.append(", "); // Add comma for the next ID
					}
				}
				commonConditions.append(") ");
			}

			// Add condition for brand ids if idbrands array is provided
			if (idbrands != null && idbrands.length > 0) {
				commonConditions.append(" AND b.").append(Views.COL_BRAND_ID).append(" IN (");
				for (int i = 0; i < idbrands.length; i++) {
					commonConditions.append("?");
					params.add(idbrands[i]); // Add the current brand ID to the params
					if (i < idbrands.length - 1) {
						commonConditions.append(", "); // Add comma for the next ID
					}
				}
				commonConditions.append(") ");
			}

			// Add condition for status if status array is provided
			if (status != null && status.length > 0) {
				commonConditions.append(" AND p.Status IN (");
				for (int i = 0; i < status.length; i++) {
					commonConditions.append("?");
					params.add(status[i]); // Add the current status to the params
					if (i < status.length - 1) {
						commonConditions.append(", "); // Add comma for the next status
					}
				}
				commonConditions.append(") ");
			}

			// Append common conditions to the main query
			str_query.append(commonConditions);

			// Append the ORDER BY clause (important for OFFSET and FETCH)
			str_query.append(" ORDER BY p.").append(Views.COL_PRODUCT_ID).append(" DESC");

			// Count query should reflect the same filters
			String countQuery = String.format(
					"SELECT COUNT(*) FROM %s p " + "INNER JOIN %s b ON p.%s = b.%s " + "INNER JOIN %s u ON p.%s = u.%s "
							+ "INNER JOIN %s c ON p.%s = c.%s " + "WHERE 1=1 %s",
					Views.TBL_PRODUCT, Views.TBL_BRAND, Views.COL_PRODUCT_BRAND_ID, Views.COL_BRAND_ID, Views.TBL_UNIT,
					Views.COL_PRODUCT_UNIT_ID, Views.COL_UNIT_ID, Views.TBL_CATEGORY, Views.COL_PRODUCT_CATE_ID,
					Views.COL_CATEGORY_ID, commonConditions.toString());

			// Execute count query
			int count = db.queryForObject(countQuery, Integer.class, params.toArray());
			int total_page = (int) Math.ceil((double) count / ItemPage.getPage_size());
			ItemPage.setTotal_page(total_page);

			// Handle pagination
			if (ItemPage.isPaginationEnabled()) {
				// Add OFFSET and FETCH NEXT for pagination
				str_query.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
				params.add((ItemPage.getPage_current() - 1) * ItemPage.getPage_size());
				params.add(ItemPage.getPage_size());
			}

			List<Product> products = db.query(str_query.toString(), new Product_mapper(), params.toArray());

	       
	        for (Product product : products) {
	           
	            List<Product_spe> specifications = reppd.findprospeId(product.getId());
	            product.setListspe(specifications);

	           
	            List<Product_img> images = reppd.findProductImagesByProductId(product.getId());
	            product.setListimg(images);

	            
	            List<Comment> comments = repcom.getCommentsByProductId(product.getId());
	            product.setListcomment(comments);
	        }

	        return products;

		} catch (DataAccessException e) {
			System.err.println("Error fetching products: " + e.getMessage());
			return Collections.emptyList();
		}
	}
}
