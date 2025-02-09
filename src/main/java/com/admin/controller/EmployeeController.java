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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.admin.repository.EmployeeRepository;
import com.admin.repository.SalaryRepository;
import com.models.Employee;
import com.models.EmployeeSalaryHistory;
import com.models.PageView;
import com.utils.Views;


@Controller
@RequestMapping("admin/employee")
public class EmployeeController {
	@Autowired
    private EmployeeRepository emprep;
	@Autowired
	SalaryRepository salaryRepo;
	// show nhân viên ra 
	@GetMapping("/showEmp")
	public String showEmp(Model model, @RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
	    try {
	        PageView pv = new PageView();
	        pv.setPage_current(cp);
	        pv.setPage_size(12);
	        List<Employee> emp = emprep.findAll(pv);
	        model.addAttribute("employees", emp);
	        model.addAttribute("pv", pv);
	        return Views.EMPLOYEE_SHOWEMP;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    }
	}
	
	// show chi tiết nhân viên ra và nhân viên đó quản lý kho nào
	@GetMapping("/showEmployeeDetail")
	public String showEmployeeDetail(@RequestParam("id") String employeeId, Model model) {
	    try {
	        int idemp = Integer.parseInt(employeeId);
	        Employee emp = emprep.findId(idemp);
	        model.addAttribute("employee", emp);
	        List<EmployeeSalaryHistory> currentSalaries = salaryRepo.getCurrentSalaries(idemp);
	        model.addAttribute("currentSalaries", currentSalaries);
	        return Views.EMPLOYEE_SHOWEMPLOYEEDETAIL;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    }
	}
	
	// show thêm nhân viên cũng là đăng ký tài khoản cho nhân viên
	@GetMapping("/showRegister")
	public String showRegister(Model model) {
	    try {
	        model.addAttribute("roles", emprep.findAllRole());
	        return Views.EMPLOYEE_SHOWREGISTER;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    }
	}
	@PostMapping("/registerEmp")
	public String registerEmp(@RequestParam String finame,
	                          @RequestParam String laname,
	                          @RequestParam String pwd,
	                          @RequestParam String phone,
	                          @RequestParam int roleId, RedirectAttributes redirectAttributes) {
	    
	    if (emprep.existsByPhone(phone)) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Phone number already exists!");
	        return "redirect:/admin/employee/showRegister";
	    }

	    Employee emp = new Employee();
	    emp.setFirst_name(finame);
	    emp.setLast_name(laname);
	    emp.setPassword(pwd);
	    emp.setPhone(phone);
	    emp.setRole_id(roleId);
	    emprep.saveEmp(emp);

	    redirectAttributes.addFlashAttribute("newEmployeeId", emp.getId());
	    redirectAttributes.addFlashAttribute("message", "✔ Employee added successfully !");
	    return "redirect:/admin/employee/showEmp";
	}

	@GetMapping("showUpdateEmployee")
	public String showUpdateEmployee(Model model, @RequestParam String id) {
	    try {
	        int ide = Integer.parseInt(id);
	        model.addAttribute("employee", emprep.findId(ide));
	        model.addAttribute("roles", emprep.findAllRole());
	        List<EmployeeSalaryHistory> currentSalaries = salaryRepo.getCurrentSalaries(ide);
	        model.addAttribute("currentSalaries", currentSalaries); 

	        
	        return Views.EMPLOYEE_SHOWUPDATEMPLOYEE;
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    }
	}
	@PostMapping("updateEmp")
	public String updateEmp(@RequestParam("id") int id,
	                        @RequestParam("first_name") String finame,
	                        @RequestParam("last_name") String laname,
	                        @RequestParam("password") String pwd,
	                        @RequestParam("phone") String phone,
	                        @RequestParam("role_id") int roleId, RedirectAttributes redirectAttributes) {

	    // Kiểm tra nếu số điện thoại đã tồn tại và không phải của nhân viên hiện tại
	    if (emprep.existsByPhoneAndIdNot(phone, id)) {
	        // Nếu đã tồn tại, trả về trang cập nhật và hiển thị thông báo lỗi
	        redirectAttributes.addFlashAttribute("errorMessage", "Phone number already exists");
	        return "redirect:/admin/employee/showRegister"; // hoặc trang cập nhật của bạn
	    }

	    // Nếu số điện thoại không trùng, tiến hành cập nhật thông tin nhân viên
	    Employee emp = new Employee();
	    emp.setId(id);
	    emp.setFirst_name(finame);
	    emp.setLast_name(laname);
	    emp.setPassword(pwd);
	    emp.setPhone(phone);
	    emp.setRole_id(roleId);
	    
	    // Cập nhật thông tin nhân viên
	    emprep.updateEmp(emp); // Giả sử bạn đã có phương thức updateEmp trong repository

	    // Thêm thông báo thành công và chuyển hướng đến danh sách nhân viên
	    redirectAttributes.addFlashAttribute("message", "✔ Employee updated successfully !");
	    return "redirect:/admin/employee/showEmp";
	}

	
	// xóa nhân viên 
	@GetMapping("deleteEmployee")
	public String deleteEmployee(@RequestParam("id") String id,@RequestParam int cp, RedirectAttributes redirectAttributes) {
	    try {
	        int ide = Integer.parseInt(id);
	        emprep.deleteEmployee(ide);
			PageView pv = new PageView();
	        int totalCount = emprep.countEmp();
	        if ((cp - 1) * pv.getPage_size() >= totalCount) {
	            cp = cp > 1 ? cp - 1 : 1;
	        }
	        redirectAttributes.addFlashAttribute("message", "✔ Employee deleted successfully !");
	        return "redirect:/admin/employee/showEmp?cp=" + cp;
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    }
	}


}
