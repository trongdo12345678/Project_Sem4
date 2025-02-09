package com.customer.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.customer.repository.ChatService;
import com.customer.repository.MomoService;
import com.customer.repository.OrderRepository;
import com.customer.repository.Order_detailRepository;
import com.customer.repository.ReturnOrderRepository;
import com.models.ChatMessage;
import com.models.ChatRoom;
import com.models.Order;
import com.models.PageView;
import com.models.ReturnOrder;
import com.models.ReturnOrderDetail;
import com.utils.Views;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("order")
public class OrderController {
	@Autowired
	OrderRepository repo;

	@Autowired
	Order_detailRepository repod;

	@Autowired
	ReturnOrderRepository returnOrderRepository;
	@Autowired
	MomoService momoser;
	@Autowired
	ChatService chatService;
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@GetMapping("/showorder")
	public String showpage(Model model, @RequestParam(name = "cp", required = false, defaultValue = "1") int cp,
			HttpServletRequest request) {

		PageView pv = new PageView();
		pv.setPage_current(cp);
		pv.setPage_size(5);
		int idlogined = (int) request.getSession().getAttribute("logined");
		List<Order> listo = repo.getOrdersByCustomerId(pv, idlogined, null, null, null);
		model.addAttribute("listo", listo);
		model.addAttribute("pv", pv);
		return Views.CUS_ORDEREDPAGE;
	}

	@GetMapping("/showdetailor")
	public String showdetailor(Model model, @RequestParam int id, HttpServletRequest request) {
	    
	    model.addAttribute("order", repo.getOrderById(id));
	    model.addAttribute("orderdetail", repod.findAllOrderDetailsByOrderId(id));
	    ReturnOrder returnOrder = returnOrderRepository.findReturnOrderByOrderId(id);
	    model.addAttribute("hasReturnOrder", returnOrder != null);
	    if (returnOrder != null) {
	        List<ReturnOrderDetail> returnOrderDetails = returnOrderRepository
	                .findReturnOrderDetailsByReturnOrderId(returnOrder.getId());
	        model.addAttribute("returnOrder", returnOrder);
	        model.addAttribute("returnOrderDetails", returnOrderDetails);
	    }
	    
	    
	    try {
	        ChatRoom chatRoom = chatService.findChatRoomByOrderId(id);
	        if (chatRoom != null) {
	            List<ChatMessage> messages = chatService.getChatMessages(chatRoom.getId());
	            model.addAttribute("chatRoom", chatRoom);
	            model.addAttribute("chatMessages", messages);
	            
	        }
	    } catch (Exception e) {
	       
	    }
	    
	    
	    model.addAttribute("orderId", id);
	    
	    return Views.CUS_ORDEREDDETAILPAGE;
	}

	@GetMapping("/gocancel/{id}")
	public String gocancel(@PathVariable int id, RedirectAttributes redirectAttributes) {
		try {
			Order order = repo.getOrderById(id);
			if (order == null) {
				throw new RuntimeException("Không tìm thấy đơn hàng");
			}

			

			String status = order.getPay_status().trim().toLowerCase();

			if ("not pay yet".equals(status)) {
				repo.updateOrderStatusToCanceled(id, "Canceled");
				redirectAttributes.addFlashAttribute("message", "Đã hủy đơn hàng thành công");

			} else if ("paid".equals(status)) {
				// Kiểm tra transaction_id
				if (order.getTransactionId() == null || order.getTransactionId().isEmpty()) {
					throw new RuntimeException("Không tìm thấy mã giao dịch MoMo");
				}


				boolean refundSuccess = momoser.refundPayment(order, "Refund order: " + order.getOrderID());

				if (refundSuccess) {
					repo.updateOrderStatusToCanceled(id, "Canceled");
					repo.updateOrderpaymentstatus(id, "Refunded");
					redirectAttributes.addFlashAttribute("message", "Đã hủy đơn hàng và hoàn tiền thành công");
				} else {
					throw new RuntimeException("Không thể hoàn tiền, vui lòng liên hệ CSKH");
				}
			} else {
				throw new RuntimeException("Không thể hủy đơn hàng ở trạng thái: " + order.getPay_status());
			}

			return "redirect:/order/showdetailor?id=" + id;

		} catch (Exception e) {
			logger.error("Error cancelling order: " + e.getMessage(), e);
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/order/showdetailor?id=" + id;
		}
	}
}
