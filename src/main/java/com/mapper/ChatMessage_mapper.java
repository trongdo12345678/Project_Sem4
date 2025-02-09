package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.models.ChatMessage;
import com.utils.Views;

public class ChatMessage_mapper implements RowMapper<ChatMessage> {
    public ChatMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
        ChatMessage item = new ChatMessage();
        item.setId(rs.getInt(Views.COL_CHAT_MESSAGE_ID));
        item.setChatroomId(rs.getInt(Views.COL_CHAT_MESSAGE_CHATROOM_ID));
        item.setEmployeeId(rs.getInt(Views.COL_CHAT_MESSAGE_EMPLOYEE_ID));
        item.setMessage(rs.getString(Views.COL_CHAT_MESSAGE_CONTENT));
        item.setTimestamp(rs.getTimestamp(Views.COL_CHAT_MESSAGE_TIMESTAMP).toLocalDateTime());
        item.setSenderType(rs.getString(Views.COL_CHAT_MESSAGE_SENDER_TYPE));
        item.setRead(rs.getBoolean(Views.COL_CHAT_MESSAGE_IS_READ));
        return item;
    }
}