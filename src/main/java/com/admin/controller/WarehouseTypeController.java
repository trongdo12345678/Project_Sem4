package com.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.admin.repository.WarehouseTypeRepository;
import com.models.PageView;
import com.models.Warehouse;
import com.models.Warehouse_type;
import com.utils.Views;

@Controller
@RequestMapping("/admin/warehouseType")
public class WarehouseTypeController {
	@Autowired
	private WarehouseTypeRepository reptype;
	
	//show loại kho
	@GetMapping("showWhType")
	public String showWhType(Model model, @RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	    try {
	        PageView pv = new PageView();
	        pv.setPage_current(cp);
	        pv.setPage_size(5);
	        List<Warehouse_type> whtypes = reptype.findAll(pv);
	        for (Warehouse_type type : whtypes) {
	            int relatedCount = reptype.countByWarehouseTypeId(type.getId());
	            type.setRelatedCount(relatedCount);
	        }
	        model.addAttribute("whtypes", whtypes);
	        model.addAttribute("pv", pv);
	        return Views.WAREHOUSETYPE_SHOWWHTYPE;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    }
	}
	// show ra kho
	@GetMapping("/getWarehousesByType")
	@ResponseBody
	public List<Warehouse> getWarehousesByType(@RequestParam("typeId") String typeId) {
	    try {
	        int idl = Integer.parseInt(typeId);
	        List<Warehouse> warehouses = reptype.findByTypeId(idl);
	        return warehouses;
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}

	// thêm loại kho 
	@GetMapping("showAddWhType")
	public String showAddWhType(Model model) {
	    try {
	        Warehouse_type wt = new Warehouse_type();
	        model.addAttribute("new_item", wt);
	        return Views.WAREHOUSETYPE_SHOWADDWHTYPE;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    }
	}
	@PostMapping("addWhType")
	public String addWhType(@RequestParam String name, RedirectAttributes redirectAttributes) {
		Warehouse_type wt = new Warehouse_type();
		wt.setName(name);
		reptype.saveWhType(wt);
		redirectAttributes.addFlashAttribute("newWtId", wt.getId());
		redirectAttributes.addFlashAttribute("message", "✔ Warehouse type added successfully!");
		return"redirect:showWhType";
	}
	
	// xóa loại kho 
	@GetMapping("deleteWt")
	public String deleteWt(@RequestParam String id,@RequestParam int cp, RedirectAttributes redirectAttributes) {
	    try {
	        int idwt = Integer.parseInt(id);
	        reptype.deleteWt(idwt);
	        PageView pv = new PageView();
	        int totalCount = reptype.countWhT();
	        if ((cp - 1) * pv.getPage_size() >= totalCount) {
	            cp = cp > 1 ? cp - 1 : 1;
	        }
	        redirectAttributes.addFlashAttribute("message", "✔ Warehouse type deleted successfully!");
	        return "redirect:showWhType?cp=" + cp;
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    }
	}
	
	// sửa loại kho
	@GetMapping("showUpdateWhType")
	public String showUpdateWhType(Model model, @RequestParam String id) {
	    try {
	        int idw = Integer.parseInt(id);
	        model.addAttribute("up_item", reptype.findById(idw));
	        return Views.WAREHOUSETYPE_SHOWUPDATEWHTYPE;
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    }
	}
	@PostMapping("updateWt")
	public String updateWt(@RequestParam("id") int id,
							@RequestParam("name") String Name, RedirectAttributes redirectAttributes) {
		Warehouse_type wt = new Warehouse_type();
		wt.setId(id);
		wt.setName(Name);
		reptype.updateWt(wt);
		redirectAttributes.addFlashAttribute("message", "✔ Warehouse type updated successfully!");
		return "redirect:showWhType";
	}
}
