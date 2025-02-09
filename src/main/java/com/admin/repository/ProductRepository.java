package com.admin.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.mapper.Product_mapper;
import com.mapper.Product_img_mapper;
import com.mapper.Product_price_change_mapper;
import com.mapper.Unit_mapper;
import com.mapper.Brand_mapper;
import com.mapper.Category_mapper;
import com.models.Brand;
import com.models.Category_Product;
import com.models.PageView;
import com.models.Product;
import com.models.Product_img;
import com.models.Product_price_change;
import com.models.Product_specifications;
import com.mapper.Product_specifications_mapper;
import com.models.Unit;
import com.utils.FileUtils;
import com.utils.Views;

@Repository
public class ProductRepository {

	private final JdbcTemplate dbpro;

	public ProductRepository(JdbcTemplate jdbc) {
		this.dbpro = jdbc;
	}

	// show product
	public List<Product> findAll(PageView itemPage) {
		try {
			String str_query = String.format("SELECT p.*, " + "b.%s AS brand_name, " + "c.%s AS category_name, "
					+ "u.%s AS unit_name, " + "p.%s AS status " + "FROM %s p " + "INNER JOIN %s b ON p.%s = b.%s "
					+ "INNER JOIN %s c ON p.%s = c.%s " + "LEFT JOIN %s u ON p.%s = u.%s " + "ORDER BY p.%s DESC",
					Views.COL_BRAND_NAME, Views.COL_CATEGORY_NAME, Views.COL_UNIT_NAME, Views.COL_PRODUCT_STATUS,
					Views.TBL_PRODUCT, Views.TBL_BRAND, Views.COL_PRODUCT_BRAND_ID, Views.COL_BRAND_ID,
					Views.TBL_CATEGORY, Views.COL_PRODUCT_CATE_ID, Views.COL_CATEGORY_ID, Views.TBL_UNIT,
					Views.COL_PRODUCT_UNIT_ID, Views.COL_UNIT_ID, Views.COL_PRODUCT_ID);

			if (itemPage != null && itemPage.isPaginationEnabled()) {
				int count = dbpro.queryForObject("SELECT COUNT(*) FROM " + Views.TBL_PRODUCT, Integer.class);
				int totalPage = (int) Math.ceil((double) count / itemPage.getPage_size());
				itemPage.setTotal_page(totalPage);

				return dbpro.query(str_query + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY", new Product_mapper(),
						(itemPage.getPage_current() - 1) * itemPage.getPage_size(), itemPage.getPage_size());
			} else {
				return dbpro.query(str_query, new Product_mapper());
			}
		} catch (DataAccessException e) {
			System.err.println("Error fetching products: " + e.getMessage());
			return Collections.emptyList();
		}
	}

	public boolean isProductReferenced(int productId) {
		String query = "SELECT " + "    (SELECT COUNT(*) FROM Product_img WHERE product_id = ?) + "
				+ "    (SELECT COUNT(*) FROM Product_specifications WHERE product_id = ?) + "
				+ "    (SELECT COUNT(*) FROM Product_price_change WHERE product_id = ?) AS total";
		try {
			int count = dbpro.queryForObject(query, Integer.class, productId, productId, productId);
			return count > 0;
		} catch (Exception e) {
			System.err.println("Error while checking product reference: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// show đơn vị
	public List<Unit> findAllUnit() {
    	try {
    		String sql = "SELECT * FROM Unit";
            return dbpro.query(sql, new Unit_mapper());
		} catch (Exception e) {
			System.err.println("error:" + e.getMessage());
			return Collections.emptyList();
		}      
    }
	// show thương hiệu
    public List<Brand> findAllBrand() {
    	try {
    		String sql = "SELECT * FROM Brand";
            return dbpro.query(sql, new Brand_mapper());
		} catch (Exception e) {
			System.err.println("error:" + e.getMessage());
			return Collections.emptyList();
		}      
    }

	// lấy theo id
	public Product findIdProT(int id) {
		try {
			String sql = "SELECT * FROM Product WHERE Id = ?";

			return dbpro.queryForObject(sql, (rs, rowNum) -> {
				Product pro = new Product();
				pro.setId(rs.getInt("Id"));
				return pro;
			}, id);
		} catch (DataAccessException e) {
			System.err.println("Lỗi khi lấy sản phẩm với ID: " + id + " - " + e.getMessage());
			return null;
		}
	}

	// lấy list theo id
	public List<Product> findAllProductIds() {
		try {
			String sql = "SELECT Id FROM Product";
			return dbpro.query(sql, (rs, rowNum) -> {
				Product product = new Product();
				product.setId(rs.getInt(Views.COL_PRODUCT_ID));
				return product;
			});
		} catch (Exception e) {
			System.err.println("error:" + e.getMessage());
			return Collections.emptyList();
		}
	}

	// lấy danh mục
	public List<Category_Product> findAllCategory() {
		try {
			String sql = "SELECT * FROM Product_category";
			return dbpro.query(sql, new Category_mapper());
		} catch (Exception e) {
			System.err.println("error:" + e.getMessage());
			return Collections.emptyList();
		}
	}

	// lấy theo id để show chi tiết sản phẩm
	public Product findId(int id) {
		try {
			String sql = "SELECT p.*, b.Name AS brand_name, c.Cate_name AS category_name, "
					+ "u.Name AS unit_name, p.Status AS status, " + "p.Length, p.Width, p.Height, p.Weight "
					+ "FROM Product p " + "LEFT JOIN Brand b ON p.Brand_id = b.Id "
					+ "LEFT JOIN Product_category c ON p.Cate_id = c.Id " + "LEFT JOIN Unit u ON p.Unit_Id = u.Id "
					+ "WHERE p.Id = ?";

			return dbpro.queryForObject(sql, (rs, rowNum) -> {
				Product pro = new Product();
				pro.setId(rs.getInt("Id"));
				pro.setBrand_id(rs.getInt("Brand_id"));
				pro.setCate_id(rs.getInt("Cate_id"));
				pro.setUnit_id(rs.getInt("Unit_Id"));
				pro.setProduct_name(rs.getString("Product_name"));
				pro.setDescription(rs.getString(Views.COL_PRODUCT_DESCIPTION));
				pro.setImg(rs.getString(Views.COL_PRODUCT_IMG));
				pro.setPrice(rs.getDouble("Price"));
				pro.setWarranty_period(rs.getInt(Views.COL_PRODUCT_WARRANTY_PERIOD));
				pro.setBrandName(rs.getString("brand_name"));
				pro.setCategoryName(rs.getString("category_name"));
				pro.setStatus(rs.getString("Status"));
				pro.setUnit_name(rs.getString("unit_name"));
				pro.setLength(rs.getInt("Length"));
				pro.setWidth(rs.getInt("Width"));
				pro.setHeight(rs.getInt("Height"));
				pro.setWeight(rs.getInt("Weight"));
				return pro;
			}, id);
		} catch (DataAccessException e) {
			System.err.println("Lỗi khi lấy sản phẩm với ID: " + id + " - " + e.getMessage());
			return null;
		}
	}

	// add sản phẩm
	public boolean saveProduct(Product pro) {
		try {
			String sql = "INSERT INTO Product (Product_name, Cate_Id, Brand_Id, Unit_Id, Price, Img, Status, Description, Warranty_period) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			int rowsAffected = dbpro.update(sql, pro.getProduct_name(), pro.getCate_id(), pro.getBrand_id(),
					pro.getUnit_id(), pro.getPrice(), pro.getImg(), pro.getStatus(), pro.getDescription(),
					pro.getWarranty_period());
			return rowsAffected > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// kiểm tra product
	@SuppressWarnings("deprecation")
	public String getProductImageById(int idpro) {
		try {
			String sql = "SELECT img FROM Product WHERE Id = ?";
			Object[] params = { idpro };
			return dbpro.queryForObject(sql, params, String.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// xóa product
	public String deleteProduct(int idpro, String folderName, String fileName) {
		try {
			String sql = "DELETE FROM Product WHERE Id = ?";
			Object[] params = { idpro };
			int[] types = { Types.INTEGER };
			int rowsAffected = dbpro.update(sql, params, types);
			if (rowsAffected > 0) {
				String deleteFileResult = FileUtils.deleteFile(folderName, fileName);
				if (deleteFileResult.equals("file deleted")) {
					return "Product and image file deleted successfully!";
				} else {
					return "Product deleted, but failed to delete image file.";
				}
			} else {
				return "Failed to delete product: No rows affected";
			}
		} catch (Exception e) {
			return "Failed to delete product: " + e.getMessage();
		}
	}

	public int countPro() {
		try {
			String sql = "SELECT COUNT(*) FROM Product";
			return dbpro.queryForObject(sql, Integer.class);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	// thêm sản phẩm và các hàm ....
	@Transactional
	public boolean saveProductWithDetails(Product pro, List<Product_specifications> specifications,
			List<Product_img> images, Product_price_change priceChange) {
		try {
			// Lưu Product
			String sql1 = "INSERT INTO Product (Product_name, Cate_Id, Brand_Id, Unit_Id, "
					+ "Price, Img, Status, Description, Warranty_period, Weight, Width, Height, Length) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			int result1 = dbpro.update(connection -> {
				var ps = connection.prepareStatement(sql1, new String[] { "Id" });
				ps.setString(1, pro.getProduct_name());
				ps.setInt(2, pro.getCate_id());
				ps.setInt(3, pro.getBrand_id());
				ps.setInt(4, pro.getUnit_id());
				ps.setDouble(5, pro.getPrice());
				ps.setString(6, pro.getImg());
				ps.setString(7, pro.getStatus());
				ps.setString(8, pro.getDescription());
				ps.setInt(9, pro.getWarranty_period());
				ps.setInt(10, pro.getWeight());
				ps.setInt(11, pro.getWidth());
				ps.setInt(12, pro.getHeight());
				ps.setInt(13, pro.getLength());
				return ps;
			}, keyHolder);

			int generatedProductId = keyHolder.getKey().intValue();
			pro.setId(generatedProductId);

			// Lưu nhiều Product_specifications
			if (specifications != null && !specifications.isEmpty()) {
				String sql2 = "INSERT INTO product_specifications (Name_spe, Des_spe, Product_id) VALUES (?, ?, ?)";
				for (Product_specifications spec : specifications) {
					dbpro.update(sql2, spec.getName_spe(), spec.getDes_spe(), generatedProductId);
				}
			}

			// Lưu nhiều Product_img
			if (images != null && !images.isEmpty()) {
				String sql3 = "INSERT INTO product_img (Img_url, Product_id) VALUES (?, ?)";
				for (Product_img img : images) {
					img.setProduct_id(generatedProductId);
					dbpro.update(sql3, img.getImg_url(), img.getProduct_id());
				}
			}

			// Lưu Product_price_change
			if (priceChange != null) {
				String sql4 = "INSERT INTO Product_price_change (Product_Id, Price, Date_start, Date_end) VALUES (?, ?, ?, ?)";
				priceChange.setProduct_id(generatedProductId);
				dbpro.update(sql4, priceChange.getProduct_id(), priceChange.getPrice(), priceChange.getDate_start(),
						null);
			}

			return result1 > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// sửa product với price_change
	@Transactional
	public boolean updateProductAndPrice(Product pro, double newPrice) {
		try {
			String sqlGetCurrentPrice = "SELECT Price FROM Product WHERE Id = ?";
			Double currentPrice = dbpro.queryForObject(sqlGetCurrentPrice, Double.class, pro.getId());
			String sqlUpdateProduct = """
					    UPDATE Product
					    SET Product_name = ?, Cate_Id = ?, Brand_Id = ?, Unit_Id = ?,
					        Price = ?, Img = ?, Status = ?, Description = ?, Warranty_period = ?,
					        Length = ?, Width = ?, Height = ?, Weight = ?
					    WHERE Id = ?
					""";
			Object[] productParams = { pro.getProduct_name(), pro.getCate_id(), pro.getBrand_id(), pro.getUnit_id(),
					pro.getPrice(), pro.getImg(), pro.getStatus(), pro.getDescription(), pro.getWarranty_period(),
					pro.getLength(), pro.getWidth(), pro.getHeight(), pro.getWeight(), pro.getId() };
			int rowsProduct = dbpro.update(sqlUpdateProduct, productParams);
			if (rowsProduct == 0) {
				throw new RuntimeException("Unable to update product information.");
			}
			if (pro.getPrice() != currentPrice) {
				String sqlUpdatePreviousPrice = """
						    UPDATE Product_price_change
						    SET Date_end = ?
						    WHERE Product_Id = ? AND Date_end IS NULL
						""";
				int rowsUpdatedPrice = dbpro.update(sqlUpdatePreviousPrice, LocalDateTime.now(), pro.getId());
				if (rowsUpdatedPrice == 0) {
					System.err.println("No previous price record found to update Date_end.");
					throw new RuntimeException("Unable to update the end date for the previous price.");
				}
				String sqlInsertNewPrice = """
						    INSERT INTO Product_price_change (Product_Id, Price, Date_start, Date_end)
						    VALUES (?, ?, ?, NULL)
						""";
				int rowsPrice = dbpro.update(sqlInsertNewPrice, pro.getId(), newPrice, LocalDateTime.now());

				if (rowsPrice == 0) {
					System.err.println("Unable to add price change record.");
					throw new RuntimeException("Unable to add price change record.");
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Update failed, transaction has been rolled back.");
		}
	}
	public boolean updateStatus(int id, String status) {
	    try {
	        String sql = "UPDATE Product SET Status = ? WHERE Id = ?";
	        int row = dbpro.update(sql, status, id);
	        return row > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}


	// product_specifications
	// =========================================================

	// show list theo id

	public List<Map<String, Object>> findListPss(int psId) {
	    try {
	        String sql = "SELECT ps.*, p.Product_name AS product_name " + 
	                     "FROM product_specifications ps " + 
	                     "LEFT JOIN Product p ON ps.Product_id = p.Id " + 
	                     "WHERE ps.Product_id = ?";

	        return dbpro.queryForList(sql, psId);

	    } catch (DataAccessException e) {
	        System.err.println("Error Product ID: " + psId);
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}


//	public int countProductSpecifications(int productId) {
//		String sql = "SELECT COUNT(*) FROM " + Views.TBL_PRODUCT_SPE + " WHERE " + Views.COL_PRODUCT_SPE_PRODUCTID + " = ?";
//		try {
//			return dbpro.queryForObject(sql, Integer.class, productId);
//		} catch (DataAccessException e) {
//			System.err.println("Error counting product specifications for product ID: " + productId);
//			e.printStackTrace();
//			return 0;
//		}
//	}
//
//	public List<Product_specifications> findListPss(int productId, int page, int pageSize) {
//	    String sql = "SELECT ps.*, p.Product_name AS product_name " +
//	                 "FROM product_specifications ps " +
//	                 "LEFT JOIN Product p ON ps.Product_Id = p.Id " +
//	                 "WHERE ps.Product_Id = ? " +
//	                 "ORDER BY ps.Id " +
//	                 "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
//	    try {
//	        System.out.println("Executing SQL: " + sql);
//	        System.out.println("Product ID: " + productId);
//	        System.out.println("Offset: " + (page * pageSize));
//	        System.out.println("Page Size: " + pageSize);
//
//	        return dbpro.query(sql, new Product_specifications_mapper(), productId, page * pageSize, pageSize);
//	    } catch (DataAccessException e) {
//	        System.err.println("Error Product ID: " + productId);
//	        e.printStackTrace();
//	        return Collections.emptyList();
//	    }
//	}

	// lấy id show update
	public Product_specifications findPsById(int id) {
		try {
			String sql = "SELECT ps.*, p.Product_name AS product_name, ps.des_spe " + "FROM product_specifications ps "
					+ "LEFT JOIN Product p ON ps.Product_id = p.Id " + "WHERE ps.Id = ?";
			return dbpro.queryForObject(sql, new Product_specifications_mapper(), id);
		} catch (DataAccessException e) {
			System.err.println("Error when getting product specifications with ID: " + id + " - " + e.getMessage());
			return null;
		}
	}

	// return về detail product
	public Integer getProductIdBySpecificationId(int id) {
		try {
			String sql = "SELECT Product_id FROM product_specifications WHERE Id = ?";
			return dbpro.queryForObject(sql, Integer.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	// update bản product_spe
	public boolean updatePs(Product_specifications ps) {
		try {
			String sql = "UPDATE product_specifications SET Name_spe = ?, Des_spe = ?, Product_id = ? WHERE Id = ?";
			int row = dbpro.update(sql, ps.getName_spe(), ps.getDes_spe(), ps.getProduct_id(), ps.getId());
			return row > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// xóa product_spe
	public int deletePs(int idps, int productId) {
	    try {
	        String sql = "DELETE FROM product_specifications WHERE Id = ? AND Product_Id = ?";
	        Object[] params = { idps, productId };
	        int[] types = { Types.INTEGER, Types.INTEGER };
	        return dbpro.update(sql, params, types);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}


	// thêm product_spe
	public boolean addProPs(Product_specifications ps) {
	    try {
	        String sql = "INSERT INTO product_specifications (Product_id, Name_spe, Des_spe) VALUES (?, ?, ?)";
	        KeyHolder keyHolder = new GeneratedKeyHolder();

	        dbpro.update(connection -> {
	            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            pstmt.setInt(1, ps.getProduct_id());
	            pstmt.setString(2, ps.getName_spe());
	            pstmt.setString(3, ps.getDes_spe());
	            return pstmt;
	        }, keyHolder);
	        
	        if (keyHolder.getKey() != null) {
	            ps.setId(keyHolder.getKey().intValue());
	        }

	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}



	// Product_img
	// ==========================================================================

	// show Product_img theo id
	public List<Product_img> findListImages(int productId) {
		try {
			String sql = "SELECT pi.*, p.Product_name AS product_name " + "FROM product_img pi "
					+ "LEFT JOIN Product p ON pi.Product_id = p.Id " + "WHERE pi.Product_id = ?";
			return dbpro.query(sql, new Product_img_mapper(), productId);

		} catch (DataAccessException e) {
			System.err.println("Error fetching images for Product ID: " + productId);
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	// thêm nhiều ảnh product
	public boolean addProDetails(List<Product_img> imageList) {
		String sql = "INSERT INTO product_img (Product_Id, Img_url) VALUES (?, ?)";
		List<Object[]> batchArgs = new ArrayList<>();

		for (Product_img pi : imageList) {
			batchArgs.add(new Object[] { pi.getProduct_id(), pi.getImg_url() });
		}
		try {
			int[] rowsAffected = dbpro.batchUpdate(sql, batchArgs);
			return Arrays.stream(rowsAffected).allMatch(rows -> rows > 0);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Integer getProductIdByimgId(int id) {
		try {
			String sql = "SELECT Product_id FROM product_img WHERE Id = ?";
			return dbpro.queryForObject(sql, Integer.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	public void deleteImageById(Integer imageId) {
		String sql = "DELETE FROM product_img WHERE Id = ?";
		dbpro.update(sql, imageId);
	}

	// xóa product_imgaes
	public String deletePi(int idpi, String folderName, String fileName) {
		try {
			// Xóa bản ghi trong cơ sở dữ liệu
			String sql = "DELETE FROM product_img WHERE Id = ?";
			Object[] params = { idpi };
			int[] types = { Types.INTEGER };
			int rowsAffected = dbpro.update(sql, params, types);
			if (rowsAffected > 0) {
				// Kiểm tra fileName có hợp lệ không
				if (fileName != null && !fileName.isEmpty()) {
					// Xóa file từ thư mục uploads/imgDetail
					String deleteFileResult = FileUtils.deleteFile(folderName, fileName);
					System.out.println(deleteFileResult); // In ra kết quả xóa tệp
					if ("file deleted".equals(deleteFileResult)) {
						return "Product image file deleted successfully!";
					} else {
						return "Product deleted, but failed to delete image file: " + deleteFileResult;
					}
				} else {
					return "Failed to delete image: Invalid file name.";
				}
			} else {
				return "Failed to delete product: No rows affected";
			}
		} catch (Exception e) {
			return "Failed to delete product: " + e.getMessage();
		}
	}

	@SuppressWarnings("deprecation")
	public String getProImageById(int imageId) {
		try {
			String sql = "SELECT Img_url FROM product_img WHERE Id = ?";
			Object[] params = { imageId };
			return dbpro.queryForObject(sql, params, String.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Product_price_change
	// ===========================================================

	// show Product_price_change theo id
	public List<Map<String, Object>> findListProductPriceChanges(int productId) {
	    try {
	        String sql = "SELECT ppc.Price AS price, ppc.Date_start AS date_start, " +
	                     "ppc.Date_end AS date_end, p.Product_name AS product_name " +
	                     "FROM Product_price_change ppc " +
	                     "LEFT JOIN Product p ON ppc.Product_id = p.Id " +
	                     "WHERE ppc.Product_id = ?";
	        return dbpro.queryForList(sql, productId);
	    } catch (DataAccessException e) {
	        System.err.println("Error fetching price changes for Product ID: " + productId);
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}



}
