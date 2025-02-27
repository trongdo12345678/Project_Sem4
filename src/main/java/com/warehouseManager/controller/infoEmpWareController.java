package com.warehouseManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.mindrot.jbcrypt.BCrypt;
import com.models.Employee;
import com.utils.Views;
import com.warehouseManager.repository.inforEmpWarehouse;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("warehouseManager")
public class infoEmpWareController {

    @Autowired
    private inforEmpWarehouse employ;

    @GetMapping("showEmployee")
    public String showEmployee(Model model, HttpSession session) {
        try {
            Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");

            if (loggedInEmployee == null) {
                return "redirect:/employee/login";  
            }

            Integer employeeId = loggedInEmployee.getId();


            Employee employee = employ.findId(employeeId);

            if (employee == null) {
                return "redirect:/employee/login"; 
            }

            model.addAttribute("employee", employee);


            return Views.EMPLOYEE_WAREHOUSE_MANAGER;
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/employee/login";  
        }
    }
    
    @PostMapping("/updateEmployee")
    public String updateEmployee(
            RedirectAttributes redirectAttributes,
            @RequestParam("id") int id,
            @RequestParam("first_name") String firstName,
            @RequestParam("last_name") String lastName) {
        try {
            Employee employee = new Employee();
            employee.setId(id);
            employee.setFirst_name(firstName);
            employee.setLast_name(lastName);

            employ.updateEmployee(employee);

            redirectAttributes.addFlashAttribute("message", "Employee updated successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
            return "redirect:/warehouseManager/showEmployee";
        } catch (Exception e) {
            e.printStackTrace();

            redirectAttributes.addFlashAttribute("message", "Error updating employee. Please try again!");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/warehouseManager/showEmployee";
        }
    }


    @PostMapping("/changePassword")
    public String changePassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
            if (loggedInEmployee == null) {
                return "redirect:/employee/login";  
            }

            if (!BCrypt.checkpw(oldPassword, loggedInEmployee.getPassword())) {
                redirectAttributes.addFlashAttribute("message1", "Old password is incorrect!");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/warehouseManager/showEmployee";
            }

            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("message2", "New passwords do not match!");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/warehouseManager/showEmployee";
            }

            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            loggedInEmployee.setPassword(hashedPassword);

            employ.updatePassword(loggedInEmployee);

            redirectAttributes.addFlashAttribute("message3", "Password changed successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
            return "redirect:/warehouseManager/showEmployee";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "An error occurred while changing the password.");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/warehouseManager/showEmployee";
        }
    }


}
