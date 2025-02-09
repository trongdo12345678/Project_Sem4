package com.admin.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.mapper.Category_mapper;
import com.models.Category_Product;
import com.models.PageView;
import com.models.Product;
import com.utils.Views;

@Repository
public class CategoryRepository {
	@Autowired
	private final JdbcTemplate dbcate;
	
	public CategoryRepository(JdbcTemplate jdbc) {
		this.dbcate = jdbc;
	}
	public List<Category_Product> findAll(PageView itemPage) {
	    try {
	        String sql = "SELECT * FROM Product_category ORDER BY id DESC";

	        if (itemPage != null && itemPage.isPaginationEnabled()) {
	            int count = dbcate.queryForObject("SELECT COUNT(*) FROM Product_category", Integer.class);
	            int total_page = (int) Math.ceil((double) count / itemPage.getPage_size());
	            itemPage.setTotal_page(total_page);
	            sql += " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
	            return dbcate.query(sql, new Category_mapper(), 
	                (itemPage.getPage_current() - 1) * itemPage.getPage_size(), 
	                itemPage.getPage_size());
	        } else {
	            return dbcate.query(sql, new Category_mapper());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}
	@SuppressWarnings("deprecation")
	public List<Product> findByCategoryId(int categoryId, PageView itemPage) {
	    try {
	        String sql = String.format("SELECT p.id, p.Product_name, p.Price, p.Img, p.Cate_Id, c.Cate_name " +
	                "FROM Product p " +
	                "JOIN Product_category c ON p.Cate_Id = c.id " +
	                "WHERE p.Cate_Id = ? " +
	                "ORDER BY p.Product_name DESC");

	        if (itemPage != null && itemPage.isPaginationEnabled()) {
	            int count = dbcate.queryForObject("SELECT COUNT(*) FROM Product WHERE Cate_Id = ?", Integer.class, categoryId);
	            int totalPage = (int) Math.ceil((double) count / itemPage.getPage_size());
	            itemPage.setTotal_page(totalPage);

	            return dbcate.query(sql + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY", new Object[]{categoryId,
	                    (itemPage.getPage_current() - 1) * itemPage.getPage_size(), itemPage.getPage_size()},
	                    (rs, rowNum) -> {
	                        Product product = new Product();
	                        product.setId(rs.getInt("id"));
	                        product.setProduct_name(rs.getString("Product_name"));
	                        product.setPrice(rs.getDouble("Price"));
	                        product.setImg(rs.getString("Img"));
	                        product.setCate_id(rs.getInt("Cate_Id"));
	                        product.setCategoryName(rs.getString("Cate_name"));
	                        return product;
	                    });
	        } else {
	            return dbcate.query(sql, new Object[]{categoryId}, (rs, rowNum) -> {
	                Product product = new Product();
	                product.setId(rs.getInt("Id"));
	                product.setProduct_name(rs.getString("Product_name"));
	                product.setPrice(rs.getDouble("Price"));
	                product.setImg(rs.getString("Img"));
	                product.setCate_id(rs.getInt("Cate_Id"));
	                product.setCategoryName(rs.getString("Cate_name"));
	                return product;
	            });
	        }
	    } catch (Exception e) {
	        System.err.println("Error fetching products: " + e.getMessage());
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}



	public Category_Product findId(int id) {
		try {
			String sql = "SELECT * FROM Product_category WHERE Id=?";
			return dbcate.queryForObject(sql ,(rs,rowNum) -> {
				Category_Product ca = new Category_Product();
				ca.setId(rs.getInt(Views.COL_CATEGORY_ID));
				ca.setName(rs.getString(Views.COL_CATEGORY_NAME));
				return ca;
			},id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public int countByCategoryId(int categoryId) {
	    try {
	        String sql = "SELECT COUNT(*) FROM Product WHERE cate_id = ?";
	        return dbcate.queryForObject(sql, Integer.class, categoryId);
	    } catch (Exception e) {
	        System.err.println("Error while counting products by category id: " + e.getMessage());
	        return 0;
	    }
	}

	public boolean saveCate(Category_Product ca) {
	    try {
	        String sql = "INSERT INTO Product_category (Cate_name) VALUES (?)";
	        KeyHolder keyHolder = new GeneratedKeyHolder();
	        int row = dbcate.update(connection -> {
	            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            ps.setString(1, ca.getName());
	            return ps;
	        }, keyHolder);
	        if (row > 0) {
	            ca.setId(keyHolder.getKey().intValue());
	            return true;
	        } else {
	            return false;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public boolean deleteCa(int id) {
		try {
			String sql = "DELETE FROM Product_category WHERE Id=?";
			Object[] params = {id};
			int[] types = {Types.INTEGER};
			int row = dbcate.update(sql,params,types);
			if(row > 0) {
				System.out.println("delete suess");
				return true;
			}else {
				System.out.println("delete faller");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public int countCategories() {
	    String sql = "SELECT COUNT(*) FROM Product_category";
	    return dbcate.queryForObject(sql, Integer.class);
	}

	public boolean updateCa(Category_Product ca) {
		try {
			String sql = "UPDATE Product_category SET Cate_name = ? WHERE Id = ?";
			int row = dbcate.update(sql, ca.getName(),ca.getId());
			return row > 0 ;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
