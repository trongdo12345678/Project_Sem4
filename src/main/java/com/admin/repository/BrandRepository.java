package com.admin.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.models.Brand;
import com.models.PageView;
import com.models.Product;
import com.utils.Views;
import com.mapper.Brand_mapper;

@Repository
public class BrandRepository {

	@Autowired
	private final JdbcTemplate dbbr;
	
	public BrandRepository(JdbcTemplate jdbc) {
		this.dbbr = jdbc;
	}
	public List<Brand> findAll(PageView itemPage) {
	    try {
	        String sql = "SELECT * FROM Brand ORDER BY Id DESC";
	        if (itemPage != null && itemPage.isPaginationEnabled()) {
	            int count = dbbr.queryForObject("SELECT COUNT(*) FROM Brand", Integer.class);
	            int totalPage = (int) Math.ceil((double) count / itemPage.getPage_size());
	            itemPage.setTotal_page(totalPage);

	            return dbbr.query(sql + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
	                    new Brand_mapper(),
	                    (itemPage.getPage_current() - 1) * itemPage.getPage_size(),
	                    itemPage.getPage_size());
	        } else {
	            return dbbr.query(sql, new Brand_mapper());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}
	@SuppressWarnings("deprecation")
	public List<Product> findByBrandId(int brandId, PageView itemPage) {
	    try {
	        String sql = String.format("SELECT p.id, p.Product_name, p.Price, p.Img, p.Brand_Id, b.Name AS Brand_name " +
	                "FROM Product p " +
	                "JOIN Brand b ON p.Brand_Id = b.Id " +
	                "WHERE p.Brand_Id = ? " +
	                "ORDER BY p.Product_name DESC");

	        if (itemPage != null && itemPage.isPaginationEnabled()) {
	            int count = dbbr.queryForObject("SELECT COUNT(*) FROM Product WHERE Brand_Id = ?", Integer.class, brandId);
	            int totalPage = (int) Math.ceil((double) count / itemPage.getPage_size());
	            itemPage.setTotal_page(totalPage);

	            return dbbr.query(sql + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY", new Object[]{brandId,
	                    (itemPage.getPage_current() - 1) * itemPage.getPage_size(), itemPage.getPage_size()},
	                    (rs, rowNum) -> {
	                        Product product = new Product();
	                        product.setId(rs.getInt("id"));  
	                        product.setProduct_name(rs.getString("Product_name"));
	                        product.setPrice(rs.getDouble("Price"));
	                        product.setImg(rs.getString("Img"));
	                        product.setBrand_id(rs.getInt("Brand_Id"));
	                        product.setBrandName(rs.getString("Brand_name"));
	                        return product;
	                    });
	        } else {
	            return dbbr.query(sql, new Object[]{brandId}, (rs, rowNum) -> {
	                Product product = new Product();
	                product.setId(rs.getInt("Id"));
	                product.setProduct_name(rs.getString("Product_name"));
	                product.setPrice(rs.getDouble("Price"));
	                product.setImg(rs.getString("Img"));
	                product.setBrand_id(rs.getInt("Brand_Id"));
	                product.setBrandName(rs.getString("Brand_name"));
	                return product;
	            });
	        }
	    } catch (Exception e) {
	        System.err.println("Error fetching products by brand: " + e.getMessage());
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}

	public int countByBrandId(int brandId) {
		try {
			String sql = "SELECT COUNT(*) FROM Product WHERE brand_id = ?";
		    return dbbr.queryForObject(sql, Integer.class, brandId);
		} catch (Exception e) {
			return 0;
		}
	    
	}
	public Brand findId(int id) {
		try {
			String sql = "SELECT * FROM Brand WHERE Id=?";
			return dbbr.queryForObject(sql ,(rs,rowNum) -> {
				Brand br = new Brand();
				br.setId(rs.getInt(Views.COL_BRAND_ID));
				br.setName(rs.getString(Views.COL_BRAND_NAME));
				return br;
			},id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public Brand saveBr(Brand brand) {
	    String sql = "INSERT INTO Brand (Name) VALUES (?)";
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    dbbr.update(connection -> {
	        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        ps.setString(1, brand.getName());
	        return ps;
	    }, keyHolder);
	    brand.setId(keyHolder.getKey().intValue());
	    return brand;
	}


	public boolean deleteBr(int id) {
		try {
			String sql = "DELETE FROM Brand WHERE Id=?";
			Object[] params = {id};
			int[] types = {Types.INTEGER};
			int row = dbbr.update(sql,params,types);
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
	public int countBrand() {
		try {
			String sql = "SELECT COUNT(*) FROM Brand";
		    return dbbr.queryForObject(sql, Integer.class);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	    
	}
	public boolean updateBr(Brand br) {
		try {
			String sql = "UPDATE Brand SET Name = ? WHERE Id = ?";
			int row = dbbr.update(sql, br.getName(),br.getId());
			return row > 0 ;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
