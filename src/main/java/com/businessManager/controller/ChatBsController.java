package com.businessManager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.customer.repository.ChatService;
import com.models.ChatRoom;
import com.models.Employee;
import com.models.PageView;
import com.utils.Views;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/businessManager/chat")
public class ChatBsController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/unassigned")
    public String showUnassignedChats(Model model,
            @RequestParam(name = "cp", required = false, defaultValue = "1") int cp) {
        try {
          
            PageView pv = new PageView();
            pv.setPage_current(cp);
            pv.setPage_size(10); 
            
           
            List<ChatRoom> unassignedChats = chatService.findAllUnassignedChatRooms(pv);
            
           
            model.addAttribute("chatRooms", unassignedChats);
            model.addAttribute("pv", pv);
            
            return Views.PAGE_LISTCHAT_NOEM;
            
        } catch (Exception e) {
            
            return Views.PAGE_LISTCHAT_NOEM;
        }
    }
    @GetMapping("/room")
    public String showEmployeeChats(Model model,HttpServletRequest request) {
        try {
        	Employee emp = (Employee)  request.getSession().getAttribute("loggedInEmployee");
          
            List<ChatRoom> employeeChats = chatService.findChatRoomsByEmployeeId(emp.getId());
            
            // Thêm vào model
            model.addAttribute("chatRooms", employeeChats);
          
            model.addAttribute("activeEmployee", emp);
            
            return Views.PAGE_CHAT_EMPLOYEE;

        } catch (Exception e) {
            System.err.println("Error in showEmployeeChats: " + e.getMessage());
            model.addAttribute("error", "Có lỗi xảy ra khi tải danh sách chat");
            return Views.PAGE_CHAT_EMPLOYEE;
        }
    }

    
    @PostMapping("/accept")
    @ResponseBody
    public ResponseEntity<?> acceptChatRequest(@RequestParam int chatId,HttpServletRequest request) {
        try {
            Employee employee = (Employee) request.getSession().getAttribute("loggedInEmployee");
            if (employee == null) {
                return ResponseEntity.status(401).body("Unauthorized");
            }

            boolean success = chatService.assignChatToEmployee(chatId, employee.getId());
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("Chat already assigned");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
}