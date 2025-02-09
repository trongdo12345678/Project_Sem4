package com.admin.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.admin.repository.ConversionRepository;
import com.admin.repository.ProductRepository;
import com.models.Conversion;
import com.models.PageView;
import com.models.Product;
import com.models.Product_img;
import com.models.Product_price_change;
import com.models.Product_specifications;
import com.utils.FileUtils;
import com.utils.Views;

@Controller
@RequestMapping("admin/product")
public class ProductController {
	@Autowired
	private ProductRepository reppro;
	@Autowired
	private ConversionRepository con;
	
	// show sản phẩm
	@GetMapping("/showProduct")
	public String showProduct(Model model, @RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	    try {
	        PageView pv = new PageView();
	        pv.setPage_current(cp);
	        pv.setPage_size(10);
	        List<Product> products = reppro.findAll(pv);

	        // Kiểm tra từng sản phẩm có được tham chiếu hay không
	        Map<Integer, Boolean> productReferences = new HashMap<>();
	        for (Product product : products) {
	            boolean isReferenced = reppro.isProductReferenced(product.getId());
	            productReferences.put(product.getId(), isReferenced);
	        }

	        model.addAttribute("products", products);
	        model.addAttribute("productReferences", productReferences);
	        model.addAttribute("pv", pv);
	        return Views.PRODUCT_SHOWPRODUCT;
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("errorMessage", "An error occurred while loading products. Please try again.");
	        return "admin/product/errorPage";
	    }
	}
	
	//phân trang spe
	@GetMapping("/findAllspe")
	@ResponseBody
	public List<Map<String, Object>> findAllspe(@RequestParam("productId") int productId) {
	    try {
	        List<Map<String, Object>> specifications = reppro.findListPss(productId);
	        return specifications;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	
	@GetMapping("/findPrice")
	@ResponseBody
	public List<Map<String, Object>> findPrice(@RequestParam("productId") int productId) {
	    try {
	        List<Map<String, Object>> priceChanges = reppro.findListProductPriceChanges(productId);
	        return priceChanges;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}

	// show deatils sản phẩm
	@GetMapping("/showProductDetail")
	public String showProductDetail(@RequestParam("id") String productId, @RequestParam("id") String psId,
			Model model) {
		try {
			int idpro = Integer.parseInt(productId);
			Product pro = reppro.findId(idpro);
			List<Product_img> images = reppro.findListImages(idpro);
			NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
			String formattedPrice = formatter.format(pro.getPrice());
			model.addAttribute("product", pro);
			model.addAttribute("formattedPrice", formattedPrice);
			model.addAttribute("images", images);
			model.addAttribute("units",con.findAllUnit());
	        model.addAttribute("conversion", new Conversion());

			return Views.PRODUCT_SHOWPRODUCTDETAIL;
		} catch (NumberFormatException e) {
			System.err.println("Product ID or Specification ID not valid: " + productId + ", " + psId);
			return "admin/product/errorPage";
		}
	}

	// add sản phẩm,images chi tiết,change price, và thuộc tính sản phẩm
	@GetMapping("showAddProduct")
	public String showAddProduct(Model model) {
		try {
			Product prod = new Product();
			model.addAttribute("units", reppro.findAllUnit());
			model.addAttribute("brands", reppro.findAllBrand());
			model.addAttribute("categorys", reppro.findAllCategory());
			model.addAttribute("new_item", prod);
			return Views.PRODUCT_SHOWADDPRODUCT;
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "An error occurred while loading the page.");
			return "admin/product/errorPage";
		}
	}

	@PostMapping("addProductWithDetails")
	public String addProductWithDetails(@RequestParam String proName, @RequestParam int cateId,
			@RequestParam int unitId, @RequestParam int brandId, @RequestParam double price,
			@RequestParam String description, @RequestParam int wpe, @RequestParam String status,
			@RequestParam int weight, @RequestParam int width, @RequestParam int height, @RequestParam int length,
			@RequestParam("productImage") MultipartFile productImage,
			@RequestParam("additionalImages") List<MultipartFile> additionalImages,
			@RequestParam("specNames") List<String> specNames,
			@RequestParam("specDescriptions") List<String> specDescriptions, @RequestParam double priceChanges,
			@RequestParam String dateStart, @RequestParam String dateEnd, RedirectAttributes redirectAttributes) {
		try {
			Product prod = new Product();
			prod.setProduct_name(proName);
			prod.setCate_id(cateId);
			prod.setUnit_id(unitId);
			prod.setBrand_id(brandId);
			prod.setPrice(price);
			prod.setDescription(description);
			prod.setWarranty_period(wpe);
			prod.setStatus(status);
			prod.setWeight(weight);
			prod.setWidth(width);
			prod.setHeight(height);
			prod.setLength(length);

			String productImgUrl = FileUtils.uploadFileImage(productImage, "uploads", "product");
			prod.setImg(productImgUrl);
			// lưu nhiều images
			List<Product_img> productImages = new ArrayList<>();
			for (MultipartFile image : additionalImages) {
				if (!image.isEmpty()) {
					String imgUrl = FileUtils.uploadFileImage(image, "uploads", "imgDetail");
					Product_img productImageDetail = new Product_img();
					productImageDetail.setImg_url(imgUrl);
					productImages.add(productImageDetail);
				}
			}

			// Lưu nhiều Product_specifications
			List<Product_specifications> specifications = new ArrayList<>();
			for (int i = 0; i < specNames.size(); i++) {
				Product_specifications spec = new Product_specifications();
				spec.setName_spe(specNames.get(i));
				spec.setDes_spe(specDescriptions.get(i));
				specifications.add(spec);
			}

			// Lưu thông tin thay đổi giá
			Product_price_change priceChangeDetail = new Product_price_change();
			priceChangeDetail.setPrice(priceChanges);
			priceChangeDetail.setDate_start(LocalDateTime.now());
			priceChangeDetail.setDate_end(null);

			reppro.saveProductWithDetails(prod, specifications, productImages, priceChangeDetail);

			redirectAttributes.addFlashAttribute("newProductId", prod.getId());
			redirectAttributes.addFlashAttribute("message", "✔ Product added successfully!");

			return "redirect:/admin/product/showProduct";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Error adding product: " + e.getMessage());
			return "redirect:/admin/product/showProduct";
		}

	}

	// xóa sản phẩm,hình sản phẩm
	@GetMapping("/deleteProduct")
	public String deleteProduct(@RequestParam("id") String id, @RequestParam("fileName") String fileName, @RequestParam int cp, RedirectAttributes redirectAttributes) {
		try {
			int idp = Integer.parseInt(id);
			String folderName = "uploads";
			reppro.deleteProduct(idp, folderName, fileName);
			PageView pv = new PageView();
	        int totalCount = reppro.countPro();
	        if ((cp - 1) * pv.getPage_size() >= totalCount) {
	            cp = cp > 1 ? cp - 1 : 1;
	        }
	        redirectAttributes.addFlashAttribute("message", "✔ Product deleted successfully!");
			return "redirect:showProduct?cp=" + cp;

		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "admin/product/errorPage";
		}
	}
	// sửa lại sản phẩm , change price lại
	@GetMapping("/showUpdateProduct")
	public String showUpdateProduct(Model model, @RequestParam String id) {
		try {
			int idPro = Integer.parseInt(id);
			Product product = reppro.findId(idPro);
			if (product == null) {
				model.addAttribute("error", "Product not found.");
				return "redirect:showProduct";
			}
			System.out.println("Image: " + product.getImg());
			model.addAttribute("product", product);
			model.addAttribute("units", reppro.findAllUnit());
			model.addAttribute("brands", reppro.findAllBrand());
			model.addAttribute("categorys", reppro.findAllCategory());

			return Views.PRODUCT_SHOWUPDATEPRODUCT;

		} catch (NumberFormatException e) {
			e.printStackTrace();
			model.addAttribute("error", "Invalid Product ID format.");
			return "redirect:showProduct";
		}
	}
	
	@PostMapping("/updateStatus")
	@ResponseBody
	public ResponseEntity<?> updateProductStatus(@RequestBody Map<String, Object> payload) {
	    String idStr = (String) payload.get("id");
	    String status = (String) payload.get("status");

	    if (idStr == null || status == null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product ID or Status is missing!");
	    }

	    try {
	        int productId = Integer.parseInt(idStr);
	        boolean updated = reppro.updateStatus(productId, status);
	        
	        if (updated) {
	            return ResponseEntity.ok("Status updated successfully!");
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update status in the database!");
	        }
	    } catch (NumberFormatException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product ID format!");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating status: " + e.getMessage());
	    }
	}

	@PostMapping("/updateProductAndPrice")
	public String updateProductAndPrice(@RequestParam int id, @RequestParam String productName,
			@RequestParam("cate_id") int categoryId, @RequestParam("brand_id") int brandId, @RequestParam("unit_id") int unitId,
			@RequestParam double price, @RequestParam String description, @RequestParam String status,
			@RequestParam("warranty_period") int warrantyPeriod, @RequestParam int weight, @RequestParam int width,
			@RequestParam int height, @RequestParam int length, @RequestParam("img") MultipartFile productImage,
			@RequestParam double newPrice, RedirectAttributes redirectAttributes) {
		Product product = new Product();
		product.setId(id);
		product.setProduct_name(productName);
		product.setCate_id(categoryId);
		product.setBrand_id(brandId);
		product.setUnit_id(unitId);
		product.setPrice(price);
		product.setDescription(description);
		product.setStatus(status);
		product.setWarranty_period(warrantyPeriod);
		product.setWeight(weight);
		product.setWidth(width);
		product.setHeight(height);
		product.setLength(length);

		if (!productImage.isEmpty()) {
			String imageUrl = FileUtils.uploadFileImage(productImage, "uploads", "product");
			product.setImg(imageUrl);
		}
		reppro.updateProductAndPrice(product, newPrice);
		redirectAttributes.addFlashAttribute("message", "✔ Product updated successfully!");
		return "redirect:showProduct";
	}

	// product_img
	// =========================================================================

	@GetMapping("showAddPImg")
	public String showAddPImg(Model model, @RequestParam("id") int id) {
		try {
			Product_img pi = new Product_img();
			Product product = reppro.findIdProT(id);
			if (product != null) {
				model.addAttribute("new_item", pi);
				model.addAttribute("product", product);
			} else {
				model.addAttribute("errorMessage", "Product_img not found!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "An error occurred while loading the product_IMG.");
			return "redirect:/erro";
		}
		return Views.PRODUCT_IMAGES_SHOWADDPI;
	}

	@PostMapping("addpi")
	public String addpi(@RequestParam("additionalImages") MultipartFile[] additionalImages,
			@RequestParam("product_id") int productId, RedirectAttributes redirectAttributes) {
		try {
			List<Product_img> productImages = new ArrayList<>();

			for (MultipartFile file : additionalImages) {
				if (!file.isEmpty()) {
					String fileName = file.getOriginalFilename();
					Path path = Paths.get("uploads/imgDetail/" + fileName);
					Files.createDirectories(path.getParent());
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

					Product_img pi = new Product_img();
					pi.setProduct_id(productId);
					pi.setImg_url("imgDetail/" + fileName);
					productImages.add(pi);
				}
			}
			reppro.addProDetails(productImages);
			redirectAttributes.addFlashAttribute("message", "✔ Images details added successfully !");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/admin/product/showProductDetail?id=" + productId;
	}

	// xóa 1 ảnh
	@GetMapping("deleteProductImage")
	public String deleteProductImage(@RequestParam("id") String id, @RequestParam("fileName") String fileName
			, RedirectAttributes redirectAttributes) {
		try {
			int imageId = Integer.parseInt(id);
			Integer productId = reppro.getProductIdByimgId(imageId);
			reppro.deletePi(imageId, "uploads", fileName);
			redirectAttributes.addFlashAttribute("message", "✔ Images details deleted successfully!");
			if (productId != null) {
				return "redirect:/admin/product/showProductDetail?id=" + productId;
			} else {
				return "admin/product/errorPage";
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "admin/product/errorPage";
		}
	}

	// xóa nhiều ảnh
	@PostMapping("/deleteSelected")
	@ResponseBody
	public ResponseEntity<String> deleteSelectedProducts(@RequestBody List<Integer> ids, RedirectAttributes redirectAttributes) {
		if (ids == null || ids.isEmpty()) {
			return ResponseEntity.badRequest().body("No product_img IDs provided.");
		}

		ids.removeIf(Objects::isNull);
		try {
			for (Integer id : ids) {
				String fileName = reppro.getProImageById(id);
				if (fileName != null && !fileName.isEmpty()) {
					reppro.deletePi(id, "uploads", fileName);
					redirectAttributes.addFlashAttribute("message", "✔ Images details deleted successfully!");
				} else {
					System.out.println("File name is invalid for product image ID: " + id);
				}
			}
			return ResponseEntity.ok("Product_img and corresponding images deleted successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to delete products: " + e.getMessage());
		}
	}

	// product_specifications
	// ============================================================

	// add thêm thuộc tính nếu cần
	@GetMapping("showAddPs")
	public String showAddPs(Model model, @RequestParam("id") int id) {
		try {
			Product_specifications ps = new Product_specifications();
			Product product = reppro.findIdProT(id);
			if (product != null) {
				model.addAttribute("new_item", ps);
				model.addAttribute("product", product);
			} else {
				model.addAttribute("errorMessage", "Product not found!");
			}

			return Views.PRODUCT_SPE_SHOWADDPS;

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "An error occurred while loading the product.");
			return "redirect:/showProduct";
		}
	}

	@PostMapping("/addPs")
	@ResponseBody
	public ResponseEntity<?> addPs(@RequestParam("name_spe") String nameSpe,
	                                @RequestParam("des_spe") String desSpe,
	                                @RequestParam("product_id") int productId) {
	    try {
	        Product_specifications ps = new Product_specifications();
	        ps.setName_spe(nameSpe);
	        ps.setDes_spe(desSpe);
	        ps.setProduct_id(productId);

	        reppro.addProPs(ps);
	        Map<String, Object> response = new HashMap<>();
	        response.put("name_spe", ps.getName_spe());
	        response.put("des_spe", ps.getDes_spe());
	        response.put("product_id", ps.getProduct_id());

	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("error", "Failed to add specification", "details", e.getMessage()));
	    }
	}

	// xóa thuộc tính
	@DeleteMapping("/deletePs")
	@ResponseBody
	public ResponseEntity<String> deleteSpecification(@RequestBody Map<String, Object> requestData) {
	    try {
	        int specificationId = Integer.parseInt(requestData.get("id").toString());
	        int productId = Integer.parseInt(requestData.get("product_id").toString());

	        int rowsAffected = reppro.deletePs(specificationId, productId);
	        if (rowsAffected == 0) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Specification not found or does not belong to the specified product.");
	        }
	        return ResponseEntity.ok("Specification deleted successfully.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error deleting specification: " + e.getMessage());
	    }
	}






}
