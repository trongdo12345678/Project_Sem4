package com.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.admin.repository.WarehouseRepository;
import com.customer.repository.GHNService;
import com.models.Employee;
import com.models.Employee_warehouse;
import com.models.PageView;
import com.models.Warehouse;
import com.models.ghn.District;
import com.models.ghn.Province;
import com.models.ghn.Ward;
import com.utils.Views;


@Controller
@RequestMapping("admin/warehouse")
public class WarehouseController {
	@Autowired
	private WarehouseRepository repwh;
	@Autowired
	private GHNService ghn;
	
	//Show kho 
	@GetMapping("showWarehouse")
	public String showWarehouse(Model model, @RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	    try {
	        PageView pv = new PageView();
	        pv.setPage_current(cp);
	        pv.setPage_size(12);
	        List<Warehouse> warehouses = repwh.findAll(pv);
	        model.addAttribute("warehouses", warehouses);
	        model.addAttribute("pv", pv);
	        return Views.WAREHOUSE_SHOWWAREHOUSE;
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("error", "Có lỗi xảy ra khi hiển thị dữ liệu warehouse.");
	        return Views.WAREHOUSE_SHOWWAREHOUSE;
	    }
	}
	
	// show chi tiết của kho bao gồm xem người nào quản lý kho này
	@GetMapping("showWarehouseDetails")
	public String showWarehouseDetails(Model model, @RequestParam("id") String id) {
	    try {
	        int idwh = Integer.parseInt(id); 
	        Warehouse wh = repwh.findId(idwh);
	        if (wh != null) {
	            model.addAttribute("warehouse", wh);
	        } else {
	            model.addAttribute("error", "Warehouse not found");
	        }
	    } catch (NumberFormatException e) {
	        model.addAttribute("error", "Invalid Warehouse ID");
	    }
	    return Views.WAREHOUSE_SHOWWAREHOUSEDETAILS;
	}
	
	// Lấy API của tên huyện
	@GetMapping("districts")
    public ResponseEntity<List<District>> getDistricts(@RequestParam("provinceId") int provinceId) {
        try {
            List<District> districts = ghn.getDistricts(provinceId);
            if (districts.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(districts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
	//lấy api của xã
	@GetMapping("wards")
	@ResponseBody
	public ResponseEntity<List<Ward>> getWards(@RequestParam("districtId") int districtId) {
	    try {
	        List<Ward> wards = ghn.getWards(districtId);
	        if (wards.isEmpty()) { 
	            return ResponseEntity.noContent().build();
	        }
	        return ResponseEntity.ok(wards);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	// thêm kho
	@GetMapping("showAddWarehouse")
	public String showAddWarehouse(Model model) {
	    try {
	        Warehouse wh = new Warehouse();
	        List<Province> province = ghn.getProvinces();
	        model.addAttribute("provinces", province);
	        model.addAttribute("new_item", wh);
	        model.addAttribute("types", repwh.findAllType());
	        List<Employee> availableEmployees = repwh.showEmpAll();
	        model.addAttribute("employees", availableEmployees);
	        return Views.WAREHOUSE_SHOWADDWAREHOUSE;
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("error", "Có lỗi xảy ra khi hiển thị trang thêm warehouse.");
	        return Views.WAREHOUSE_SHOWADDWAREHOUSE;
	    }
	}
	@PostMapping("addWh")
	public String addWh(
	        @RequestParam("name") String name,
	        @RequestParam("address") String address,
	        @RequestParam("wh_type_id") int wh_type_id,
	        @RequestParam("wardId") String wardId,
	        @RequestParam("provinceId") int provinceId,
	        @RequestParam("districtId") int districtId,
	        @RequestParam("Lat") double Lat,
	        @RequestParam("Lng") double Lng,
	        @RequestParam("employeeId") int employeeId,
	        RedirectAttributes redirectAttributes) {
	    try {
	        // Tạo đối tượng Warehouse
	        Warehouse wh = new Warehouse();
	        wh.setName(name);
	        wh.setAddress(address);
	        wh.setWh_type_id(wh_type_id);
	        wh.setWard_Id(wardId);
	        wh.setProvince_Id(provinceId);
	        wh.setDistrict_Id(districtId);
	        wh.setLat(Lat);
	        wh.setLng(Lng);

	        boolean isSaved = repwh.saveWh(wh, employeeId);

	        if (isSaved) {
	            redirectAttributes.addFlashAttribute("newWarehouseId", wh.getId());
	            redirectAttributes.addFlashAttribute("message", "✔ Warehouse added successfully!");
	        } else {
	            redirectAttributes.addFlashAttribute("error", "❌ Failed to add warehouse. Please try again.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("error", "❌ An error occurred while adding the warehouse.");
	    }

	    return "redirect:showWarehouse";
	}

	
	
	// xóa kho đó 
	@GetMapping("deleteWh")
	public String deleteWh(@RequestParam String id, @RequestParam int cp, RedirectAttributes redirectAttributes) {
	    try {
	        int idwh = Integer.parseInt(id);
	        repwh.deleteWh(idwh);
			PageView pv = new PageView();
	        int totalCount = repwh.countWh();
	        if ((cp - 1) * pv.getPage_size() >= totalCount) {
	            cp = cp > 1 ? cp - 1 : 1;
	        }
			redirectAttributes.addFlashAttribute("message", "✔ Warehouse deleted successfully!");
	        return "redirect:showWarehouse?cp=" + cp;
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        return "redirect:showWarehouse?error=InvalidID";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:showWarehouse?error=DeleteFailed";
	    }
	}
	
	//update kho 
	@GetMapping("showUpdateWarehouse")
	public String showUpdateWarehouse(Model model, @RequestParam("id") String id) {
	    try {
	        int idwh = Integer.parseInt(id);
	        List<Province> province = ghn.getProvinces();
	        model.addAttribute("provinces", province);
	        model.addAttribute("up_item", repwh.findId(idwh));
	        model.addAttribute("types", repwh.findAllType());
	        return Views.WAREHOUSE_SHOWUPDATEWAREHOUSE;
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        return "redirect:showWarehouse?error=InvalidID";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:showWarehouse?error=UpdateFailed";
	    }
	}
	@PostMapping("updateWh")
	public String updateWh(@RequestParam("name") String name,
							@RequestParam("address") String address,
							@RequestParam("wh_type_id") int wh_type_id,
							@RequestParam("ward_Id") String ward_id,
							@RequestParam("province_Id") int province_id,
							@RequestParam("district_Id") int district_Id,
					        @RequestParam("Lat") double Lat,
					        @RequestParam("Lng") double Lng,
							@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
		Warehouse wh = new Warehouse();
		wh.setName(name);
		wh.setAddress(address);
		wh.setWh_type_id(wh_type_id);
		wh.setWard_Id(ward_id);
		wh.setProvince_Id(province_id);
		wh.setDistrict_Id(district_Id);
		wh.setLat(Lat);
		wh.setLng(Lng);
		wh.setId(id);
		repwh.updatewh(wh);
		redirectAttributes.addFlashAttribute("message", "✔ Warehouse updated successfully!");
		return "redirect:showWarehouse";
	}
	
	
	
	//Employee Warehouse ==============================================================
	@GetMapping("showEmpWare")
	public String showEmpWare(Model model) {
	    try {
	        model.addAttribute("ews", repwh.findAllEw());
	        return Views.EMPLOYEE_WAREHOUSE_SHOWEMPWARE;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    }
	}
	
	// Thêm quản lý cho kho 
	@GetMapping("showWarehouseManagementLevel")
	public String showWarehouseManagementLevel(Model model, @RequestParam String iddtwh) {
	    try {
	        int idwh = Integer.parseInt(iddtwh); 
	        Warehouse wh = repwh.findId(idwh);
	        List<Employee> availableEmployees = repwh.showEmpAll();
	        model.addAttribute("employees", availableEmployees);
	        model.addAttribute("warehouse", wh);
	        return Views.EMPLOYEE_WAREHOUSE_SHOWWAREHOUSEMANAGEMENTLEVEL;
	    } catch (NumberFormatException e) {
	        model.addAttribute("error", "Invalid warehouse ID format.");
	        return "error-page";
	    } catch (Exception e) {
	        model.addAttribute("error", "An error occurred while loading warehouse management level.");
	        return "error-page";
	    }
	}
	@PostMapping("warehouseManagementLevel")
	public String warehouseManagementLevel(@RequestParam int warehouse_id,
	                                       @RequestParam int employee_id,
	                                       RedirectAttributes redirectAttributes) {
	    Employee_warehouse ew = new Employee_warehouse();
	    ew.setWarehouse_Id(warehouse_id);
	    ew.setEmployee_Id(employee_id);
	    
	    repwh.addEw(ew);
	    
	    redirectAttributes.addFlashAttribute("message", "Employee assigned successfully!");
	    
	    return "redirect:/admin/warehouse/showWarehouseDetails?id=" + warehouse_id;
	}
	
	// sửa lại quản lý kho đã thêm
	@GetMapping("deleteManager")
	public String deleteManager(@RequestParam("idew") String idew, RedirectAttributes redirectAttributes) {
	    try {
	        int ideww = Integer.parseInt(idew);
	        boolean isDeleted = repwh.deleteEw(ideww);
	        if (isDeleted) {
	            redirectAttributes.addFlashAttribute("message", "✔ Manager deleted successfully!");
	        } else {
	            redirectAttributes.addFlashAttribute("error", "Failed to delete manager");
	        }
	        return "redirect:/admin/warehouse/showEmpWare";

	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("error", "Invalid Manager or Warehouse ID");
	        return "redirect:/admin/warehouse/showWarehouse";
	    }
	}



}
