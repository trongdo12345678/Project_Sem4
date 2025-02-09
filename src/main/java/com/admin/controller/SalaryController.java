package com.admin.controller;

import com.models.*;
import com.admin.repository.*;
import com.utils.Views;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/salary")
public class SalaryController {

    @Autowired
    private SalaryRepository salaryRepo;

    @GetMapping("/types")
    public String showSalaryTypes(Model model) {
        model.addAttribute("salaryType", new SalaryType());  // for modal form
        model.addAttribute("salaryTypes", salaryRepo.findAllTypes());
        return Views.SALARY_TYPES;
    }

    @PostMapping("/add-type")
    public String addSalaryType(@ModelAttribute SalaryType salaryType) {
        try {
            salaryRepo.saveType(salaryType);
            return "redirect:/admin/salary/types?success=Type added successfully";
        } catch (Exception e) {
            return "redirect:/admin/salary/types?error=" + e.getMessage();
        }
    }

    @GetMapping("/type/{id}")
    @ResponseBody
    public SalaryType getTypeById(@PathVariable int id) {
        return salaryRepo.findTypeById(id);
    }

    @PostMapping("/update-type")
    public String updateSalaryType(@ModelAttribute SalaryType salaryType) {
        try {
            salaryRepo.updateType(salaryType);
            return "redirect:/admin/salary/types?success=Type updated successfully";
        } catch (Exception e) {
            return "redirect:/admin/salary/types?error=" + e.getMessage();
        }
    }

    @PostMapping("/type/toggle/{id}")
    public String toggleTypeStatus(@PathVariable int id) {
        try {
            SalaryType type = salaryRepo.findTypeById(id);
            if (type != null) {
                // Toggle is_active status
                type.setIsActive(type.getIsActive().equals("true") ? "false" : "true");
                salaryRepo.updateType(type);
                return "redirect:/admin/salary/types?success=Status updated successfully";
            }
            return "redirect:/admin/salary/types?error=Type not found";
        } catch (Exception e) {
            return "redirect:/admin/salary/types?error=" + e.getMessage();
        }
    }
    @PostMapping("/update-salary")
    @ResponseBody
    public Map<String, Object> updateSalary(
        @RequestParam int employeeId,
        @RequestParam int salaryTypeId,
        @RequestParam double newAmount,
        HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
	    
	    Employee emp = (Employee) request.getSession().getAttribute("loggedInEmployee");
        try {
            boolean success = salaryRepo.updateSalary(employeeId, salaryTypeId, newAmount, emp.getId());  // getCurrentUserId() là method của bạn
            
            if (success) {
                response.put("status", "success");
                response.put("message", "Salary updated successfully");
                List<EmployeeSalaryHistory> currentSalaries = salaryRepo.getCurrentSalaries(employeeId);
                response.put("data", currentSalaries);
            } else {
                response.put("status", "error");
                response.put("message", "No changes were made");
            }
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error updating salary: " + e.getMessage());
        }
        
        return response;
    }
}