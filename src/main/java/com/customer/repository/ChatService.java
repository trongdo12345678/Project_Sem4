package com.customer.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.mapper.*;
import com.models.*;
import com.utils.Views;

@Service
public class ChatService {

    @Autowired
    private JdbcTemplate db;
    @Autowired
    OrderRepository orderService;
    public ChatRoom createChatRoom(Integer orderId) {
        String sql = String.format(
            "INSERT INTO %s (%s, %s, %s) VALUES (?, 'WAITING', ?)",
            Views.TBL_CHAT_ROOM,
            Views.COL_CHAT_ROOM_ORDER_ID,
            Views.COL_CHAT_ROOM_STATUS,
            Views.COL_CHAT_ROOM_LAST_ACTIVITY 
        );
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, orderId);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);

        return findChatRoomByOrderId(orderId);
    }

    public ChatRoom acceptChat(Integer roomId, Integer employeeId) {
        String sql = String.format(
            "UPDATE %s SET %s = ?, %s = 'ACTIVE' WHERE %s = ?",
            Views.TBL_CHAT_ROOM,
            Views.COL_CHAT_ROOM_EMPLOYEE_ID,
            Views.COL_CHAT_ROOM_STATUS,
            Views.COL_CHAT_ROOM_ID);
            
        db.update(sql, employeeId, roomId);
        return findChatRoomById(roomId);
    }

    public ChatMessage saveMessage(ChatMessage message) {
        String sql = String.format(
            "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)",
            Views.TBL_CHAT_MESSAGE,
            Views.COL_CHAT_MESSAGE_CHATROOM_ID,
            Views.COL_CHAT_MESSAGE_EMPLOYEE_ID,
            Views.COL_CHAT_MESSAGE_CONTENT,
            Views.COL_CHAT_MESSAGE_SENDER_TYPE,
            Views.COL_CHAT_MESSAGE_TIMESTAMP);
            
        db.update(sql, 
            message.getChatroomId(),
            message.getEmployeeId(),
            message.getMessage(),
            message.getSenderType(),
            LocalDateTime.now());
            
        return message;
    }

  
    public ChatRoom findChatRoomById(Integer id) {
        String sql = String.format("SELECT * FROM %s WHERE %s = ?",
            Views.TBL_CHAT_ROOM,
            Views.COL_CHAT_ROOM_ID);
            
        return db.queryForObject(sql, new ChatRoom_mapper(), id);
    }

    public ChatRoom findChatRoomByOrderId(Integer orderId) {
        try {
            String sql = String.format("SELECT * FROM %s WHERE %s = ?",
                Views.TBL_CHAT_ROOM,
                Views.COL_CHAT_ROOM_ORDER_ID);
            
            return db.queryForObject(sql, new ChatRoom_mapper(), orderId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<ChatMessage> getChatMessages(Integer roomId) {
        String sql = String.format("SELECT * FROM %s WHERE %s = ? ORDER BY %s",
            Views.TBL_CHAT_MESSAGE,
            Views.COL_CHAT_MESSAGE_CHATROOM_ID,
            Views.COL_CHAT_MESSAGE_TIMESTAMP);
            
        return db.query(sql, new ChatMessage_mapper(), roomId);
    }
    public ChatMessage save(ChatMessage message) {
    	String sql = String.format(
    	        "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, 0)",
    	        Views.TBL_CHAT_MESSAGE,
    	        Views.COL_CHAT_MESSAGE_CHATROOM_ID,
    	        Views.COL_CHAT_MESSAGE_EMPLOYEE_ID,
    	        Views.COL_CHAT_MESSAGE_CONTENT,
    	        Views.COL_CHAT_MESSAGE_SENDER_TYPE,
    	        Views.COL_CHAT_MESSAGE_TIMESTAMP,
    	        Views.COL_CHAT_MESSAGE_IS_READ
    	    );
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getChatroomId());
            ps.setObject(2, message.getEmployeeId()); // Có thể NULL nếu là customer gửi
            ps.setString(3, message.getMessage());
            ps.setString(4, message.getSenderType());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);
        
       
        Number key = keyHolder.getKey();
        if (key != null) {
            message.setId(key.intValue());
        }
        message.setTimestamp(LocalDateTime.now());
        message.setRead(false);
        
       
        String updateRoomSql = String.format(
            "UPDATE %s SET %s = ? WHERE %s = ?",
            Views.TBL_CHAT_ROOM,
            Views.COL_CHAT_ROOM_LAST_ACTIVITY,
            Views.COL_CHAT_ROOM_ID
        );
        db.update(updateRoomSql, Timestamp.valueOf(LocalDateTime.now()), message.getChatroomId());
        
        return message;
    }
    
    public List<ChatRoom> findAllUnassignedChatRooms(PageView pageView) {
        try {
            
            StringBuilder str_query = new StringBuilder(
                    String.format("SELECT cr.* FROM %s cr WHERE cr.%s IS NULL ORDER BY cr.%s ASC",
                            Views.TBL_CHAT_ROOM, Views.COL_CHAT_ROOM_EMPLOYEE_ID, Views.COL_CHAT_ROOM_ID));

            List<Object> params = new ArrayList<>();

           
            String countQuery = String.format("SELECT COUNT(*) FROM %s WHERE %s IS NULL", 
                    Views.TBL_CHAT_ROOM, Views.COL_CHAT_ROOM_EMPLOYEE_ID);

            int count = db.queryForObject(countQuery, Integer.class);

            
            int total_page = (int) Math.ceil((double) count / pageView.getPage_size());
            pageView.setTotal_page(total_page);

            
            if (pageView.isPaginationEnabled()) {
                str_query.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
                params.add((pageView.getPage_current() - 1) * pageView.getPage_size());
                params.add(pageView.getPage_size());
            }

            List<ChatRoom> chatRooms = db.query(str_query.toString(), new ChatRoom_mapper(), params.toArray());

            return chatRooms;

        } catch (DataAccessException e) {
            System.err.println("Error in findAllUnassignedChatRooms: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public List<ChatRoom> findChatRoomsByEmployeeId(int employeeId) {
        try {
            // Query lấy chat rooms đang active
            String query = String.format(
                "SELECT cr.* FROM %s cr " +
                "WHERE cr.%s = ? " +
                "AND cr.%s = 'Active' " +
                "ORDER BY cr.%s DESC",
                Views.TBL_CHAT_ROOM,
                Views.COL_CHAT_ROOM_EMPLOYEE_ID, 
                Views.COL_CHAT_ROOM_STATUS,
                Views.COL_CHAT_ROOM_LAST_ACTIVITY
            );

            List<ChatRoom> chatRooms = db.query(query, new ChatRoom_mapper(), employeeId);

          
            for (ChatRoom chatRoom : chatRooms) {
               
                Order order = orderService.getOrderById(chatRoom.getOrderId());
                chatRoom.setOrder(order);
                
              
            }

            return chatRooms;

        } catch (DataAccessException e) {
            System.err.println("Error in findChatRoomsByEmployeeId: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public boolean assignChatToEmployee(int chatId, int employeeId) {
        try {
            String checkQuery = String.format(
                "SELECT %s FROM %s WHERE %s = ? AND %s IS NULL",
                Views.COL_CHAT_ROOM_ID,
                Views.TBL_CHAT_ROOM,
                Views.COL_CHAT_ROOM_ID,
                Views.COL_CHAT_ROOM_EMPLOYEE_ID
            );
            
            List<Integer> result = db.queryForList(checkQuery, Integer.class, chatId);
            
            if (result.isEmpty()) {
                return false;
            }

           
            String updateQuery = String.format(
                "UPDATE %s SET %s = ?, %s = 'Active', %s = CURRENT_TIMESTAMP WHERE %s = ?",
                Views.TBL_CHAT_ROOM,
                Views.COL_CHAT_ROOM_EMPLOYEE_ID,
                Views.COL_CHAT_ROOM_STATUS,
                Views.COL_CHAT_ROOM_LAST_ACTIVITY,
                Views.COL_CHAT_ROOM_ID
            );

            int rowsAffected = db.update(updateQuery, employeeId, chatId);

            return rowsAffected > 0;

        } catch (DataAccessException e) {
            System.err.println("Error in assignChatToEmployee: " + e.getMessage());
            return false;
        }
    }
   
    
    public boolean closeChatRoom(int roomId, int employeeId) {
        String query = String.format(
            "UPDATE %s SET %s = 'Closed', %s = CURRENT_TIMESTAMP " +
            "WHERE %s = ? AND %s = ?",
            Views.TBL_CHAT_ROOM,
            Views.COL_CHAT_ROOM_STATUS,
            Views.COL_CHAT_ROOM_LAST_ACTIVITY, 
            Views.COL_CHAT_ROOM_ID,
            Views.COL_CHAT_ROOM_EMPLOYEE_ID
        );

        int rowsAffected = db.update(query, roomId, employeeId);
        return rowsAffected > 0;
    }
    public void markMessagesAsRead(int roomId, int employeeId) {
        String query = String.format(
            "UPDATE %s SET %s = 1 " +
            "WHERE %s = ? AND %s != ? AND %s = 0",
            Views.TBL_CHAT_MESSAGE,
            Views.COL_CHAT_MESSAGE_IS_READ,
            Views.COL_CHAT_MESSAGE_CHATROOM_ID,
            Views.COL_CHAT_MESSAGE_EMPLOYEE_ID,
            Views.COL_CHAT_MESSAGE_IS_READ
        );
        
        db.update(query, roomId, employeeId);
    }
    public int getUnreadMessageCount(int employeeId) {
        String query = String.format(
            "SELECT COUNT(*) FROM %s m " +
            "JOIN %s r ON m.%s = r.%s " +
            "WHERE r.%s = ? AND m.%s = 0 AND m.%s != ?",
            Views.TBL_CHAT_MESSAGE,
            Views.TBL_CHAT_ROOM,
            Views.COL_CHAT_MESSAGE_CHATROOM_ID,
            Views.COL_CHAT_ROOM_ID,
            Views.COL_CHAT_ROOM_EMPLOYEE_ID,
            Views.COL_CHAT_MESSAGE_IS_READ,
            Views.COL_CHAT_MESSAGE_EMPLOYEE_ID
        );
        
        return db.queryForObject(query, Integer.class, employeeId, employeeId);
    }
    public String getLastMessage(int roomId) {
        String query = String.format(
            "SELECT TOP 1 %s FROM %s " +
            "WHERE %s = ? " +
            "ORDER BY %s DESC",
            Views.COL_CHAT_MESSAGE_CONTENT,
            Views.TBL_CHAT_MESSAGE,
            Views.COL_CHAT_MESSAGE_CHATROOM_ID,
            Views.COL_CHAT_MESSAGE_TIMESTAMP
        );
        
        try {
            return db.queryForObject(query, String.class, roomId);
        } catch (EmptyResultDataAccessException e) {
            return "";
        }
    }
}