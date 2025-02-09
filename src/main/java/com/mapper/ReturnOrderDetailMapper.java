package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Brand;
import com.models.ReturnOrderDetail;
import com.utils.Views;



public class ReturnOrderDetailMapper implements RowMapper<ReturnOrderDetail> {
    @Override
    public ReturnOrderDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReturnOrderDetail detail = new ReturnOrderDetail();
        detail.setId(rs.getInt(Views.COL_RETURN_DETAIL_ID));
        detail.setReturnOrderId(rs.getInt(Views.COL_RETURN_DETAIL_RETURN_ID));
        detail.setOrderDetailId(rs.getInt(Views.COL_RETURN_DETAIL_ORDER_DETAIL_ID));
        detail.setQuantity(rs.getInt(Views.COL_RETURN_DETAIL_QUANTITY));
        detail.setReason(rs.getString(Views.COL_RETURN_DETAIL_REASON));
        detail.setAmount(rs.getDouble(Views.COL_RETURN_DETAIL_AMOUNT));
        
        
        try {
        	detail.setProductId(rs.getInt("ProductId"));
            detail.setProductName(rs.getString("ProductName"));
            detail.setProductImage(rs.getString("ProductImage"));
            detail.setProductPrice(rs.getDouble("ProductPrice"));
            detail.setOriginalQuantity(rs.getInt("OriginalQuantity"));
        } catch (SQLException e) {
            // Ignore if not joined
        }
        
        return detail;
    }
}