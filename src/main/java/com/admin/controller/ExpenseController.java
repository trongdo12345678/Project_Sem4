package com.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.repository.ExpenseRepository;
import com.models.Employee;
import com.models.ExpenseHistory;
import com.models.ExpenseType;
import com.models.PageView;
import com.utils.Views;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/expense")
public class ExpenseController {
	@Autowired
	private ExpenseRepository expenseRepo;

	@GetMapping("/showexpense")
	public String showExpense(Model model, @RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
		try {
			PageView pv = new PageView();
			pv.setPage_current(cp);
			pv.setPage_size(8);

			List<ExpenseHistory> expenseList = expenseRepo.findAll(pv);
			model.addAttribute("expenseList", expenseList);
			model.addAttribute("pv", pv);

			List<ExpenseType> expenseTypes = expenseRepo.findAllTypes();
			model.addAttribute("expenseTypes", expenseTypes);

			return Views.EXPENSE_SHOWEXPENSEPAGE;

		} catch (Exception e) {
			System.err.println("Error in showExpense: " + e.getMessage());
			return "redirect:/admin/expense/showexpense";
		}
	}

	@PostMapping("/add")
	@ResponseBody
	public ResponseEntity<ExpenseHistory> addExpense(@RequestBody ExpenseHistory expense, HttpServletRequest request) {
		try {
			
			Employee emp = (Employee) request.getSession().getAttribute("loggedInEmployee");
			expense.setCreatedBy(emp.getId());
			ExpenseHistory savedExpense = expenseRepo.saveHistory(expense);
			return ResponseEntity.ok(savedExpense);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	@PostMapping("/delete/{id}")
	@ResponseBody
	public ResponseEntity<Void> deleteExpense(@PathVariable int id) {
	    try {
	        expenseRepo.deleteHistoryById(id);
	        return ResponseEntity.ok().build();
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	@PostMapping("/type/add")
	@ResponseBody
	public ResponseEntity<ExpenseType> addExpenseType(@RequestBody ExpenseType expenseType) {
		try {
			
			ExpenseType savedType = expenseRepo.saveType(expenseType);
			return ResponseEntity.ok(savedType);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/type/update")
	public ResponseEntity<ExpenseType> updateExpenseType(@RequestBody ExpenseType type) {
		try {
			ExpenseType updatedType = expenseRepo.updateType(type);
			return ResponseEntity.ok(updatedType);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/type/{id}")
	public ResponseEntity<ExpenseType> getExpenseType(@PathVariable("id") int id) {
		try {
			ExpenseType type = expenseRepo.findTypeById(id);
			if (type != null) {
				return ResponseEntity.ok(type);
			}
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
}