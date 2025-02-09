package com.customer.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mapper.Order_mapper;
import com.models.ChatMessage;
import com.models.ChatRoom;
import com.models.Order;
import com.models.Order_detail;
import com.models.ReturnOrder;
import com.models.ReturnOrderDetail;
import com.utils.Views;
@Repository 
public class OrderAPIRepository {
	@Autowired
	JdbcTemplate db;
	
	@Autowired
	OrderRepository repo;

	@Autowired
	Order_detailRepository repod;

	@Autowired
	ReturnOrderRepository reprt;

	@Autowired
	ChatService chatService;
	
	
	public List<Order> getOrdersByCustomerIdAPI(int customerId) {
		

		try {
			StringBuilder queryBuilder = new StringBuilder(
					String.format("SELECT * FROM [%s] WHERE [%s] = ? ORDER BY Id DESC", Views.TBL_ORDER, Views.COL_ORDER_CUSTOMER_ID));

			List<Object> params = new ArrayList<>();
			params.add(customerId);
			@SuppressWarnings("deprecation")
			List<Order> orders = db.query(queryBuilder.toString(), params.toArray(), new Order_mapper());
	        
	        
	        for (Order order : orders) {
	            // Lấy order details
	            List<Order_detail> orderDetails = repod.findAllOrderDetailsByOrderId(order.getId());
	            order.setOrderDetails(orderDetails);

	            // Lấy return order nếu có
	            ReturnOrder returnOrder = reprt.findReturnOrderByOrderId(order.getId());
	            if (returnOrder != null) {
	                // Lấy return order details
	                List<ReturnOrderDetail> returnOrderDetails = reprt
	                    .findReturnOrderDetailsByReturnOrderId(returnOrder.getId());
	                returnOrder.setReturnDetails(returnOrderDetails);
	                order.setReturnOrder(returnOrder);
	            }

	            
	            try {
	                ChatRoom chatRoom = chatService.findChatRoomByOrderId(order.getId());
	                if (chatRoom != null) {
	                    order.setChatRoom(chatRoom);
	                    List<ChatMessage> messages = chatService.getChatMessages(chatRoom.getId());
	                    order.setChatMessages(messages);
	                }
	            } catch (Exception e) {
	                
	                System.err.println("Error fetching chat data for order " + order.getId() + ": " + e.getMessage());
	            }
	        }

	        return orders;
		} catch (DataAccessException e) {
			System.err.println("Error fetching orders for customer ID " + customerId + ": " + e.getMessage());
			return new ArrayList<>();
		}
	}
}
