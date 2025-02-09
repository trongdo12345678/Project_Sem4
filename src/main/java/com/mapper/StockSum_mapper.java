package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.StockSumByWarehouseId;
import com.utils.Views;

public class StockSum_mapper implements RowMapper<StockSumByWarehouseId> {
	public StockSumByWarehouseId mapRow(ResultSet rs ,int rowNum) throws SQLException {
		StockSumByWarehouseId item = new StockSumByWarehouseId();
		item.setProductId(rs.getInt(Views.COL_PRODUCT_ID));
		item.setProductName(rs.getString(Views.COL_PRODUCT_NAME));
		item.setQuantity(rs.getInt("total_Quantity"));
		item.setUnitName(rs.getString(Views.COL_UNIT_NAME));		
        item.setPrice(rs.getDouble("Average_Wh_price"));
        item.setDate(rs.getDate(Views.COL_WAREHOUSE_RECEIPT_DATE));
		return item;
	}

}
