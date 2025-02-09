package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.models.ChatRoom;
import com.utils.Views;

public class ChatRoom_mapper implements RowMapper<ChatRoom> {
    public ChatRoom mapRow(ResultSet rs, int rowNum) throws SQLException {
        ChatRoom item = new ChatRoom();
        item.setId(rs.getInt(Views.COL_CHAT_ROOM_ID));
        item.setOrderId(rs.getInt(Views.COL_CHAT_ROOM_ORDER_ID)); 
        item.setEmployeeId(rs.getInt(Views.COL_CHAT_ROOM_EMPLOYEE_ID));
        item.setStatus(rs.getString(Views.COL_CHAT_ROOM_STATUS));
        item.setLastActivity(rs.getTimestamp(Views.COL_CHAT_ROOM_LAST_ACTIVITY).toLocalDateTime());
        return item;
    }
}