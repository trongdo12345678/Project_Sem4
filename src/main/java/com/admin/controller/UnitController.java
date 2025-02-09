package com.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.admin.repository.UnitRepository;
import com.models.Unit;
import com.utils.Views;

@Controller
@RequestMapping("admin")
public class UnitController {

    @Autowired
    private UnitRepository unitRepository;
    
    @GetMapping("/units")
    public String getAllUnits(Model model) {
        List<Unit> units = unitRepository.getAllUnits();
        
        for (Unit unit : units) {
            unit.setRelatedCount(unitRepository.countByUnitId(unit.getId()));
        }
        
        model.addAttribute("units", units);
        return Views.SHOW_UNIT; 
    }

	@GetMapping("showAddUnit")
	public String showAddUnit(Model model) {
		Unit un  = new Unit();
		model.addAttribute("new_item",un);
		return Views.ADD_UNIT;
	}
    
    @PostMapping("/addUnit")
    public String addUnit(@RequestParam("name") String name) {
        Unit unit = new Unit();
        unit.setName(name);
        unitRepository.addUnit(unit);
        return "redirect:/admin/units";
    }
    
  
    @GetMapping("/deleteUnit")
    public String deleteUnit(@RequestParam("id") int id) {
        unitRepository.deleteUnit(id);
        return "redirect:/admin/units";
    }
    
	@GetMapping("showUpdateUnit")
	public String showUpdateUnit(Model model , @RequestParam String id) {
		int idp = Integer.parseInt(id);
		model.addAttribute("up_item",unitRepository.findUnitById(idp));
		return Views.UPDATE_UNIT;
	}
	
    @PostMapping("/updateUnit")
    public String updateUnit(@RequestParam("id") int id, @RequestParam("name") String name) {
        Unit unit = new Unit();
        unit.setId(id);
        unit.setName(name);
        unitRepository.updateUnit(unit);
        return "redirect:/admin/units";
    }
}
