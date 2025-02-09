package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.models.Warehouse_receipt;
import com.utils.Views;

public class Warehouse_recript_mapper implements RowMapper<Warehouse_receipt>{
	public Warehouse_receipt mapRow(ResultSet rs,int rowNum) throws SQLException{
		Warehouse_receipt item = new Warehouse_receipt();
		item.setId(rs.getInt(Views.COL_WAREHOUSE_RECEIPT_ID));
		item.setName(rs.getString(Views.COL_WAREHOUSE_RECEIPT_NAME));
		item.setWh_id(rs.getInt(Views.COL_WAREHOUSE_RECEIPT_IDWH));
		item.setStatus(rs.getString(Views.COL_WAREHOUSE_RECEIPT_STATUS));
		item.setWh_name(rs.getString("wh_name"));
		item.setShipping_fee(rs.getDouble(Views.COL_WAREHOUSE_RECEIPT_SHIPPINGFEE));
		item.setOther_fee(rs.getDouble(Views.COL_WAREHOUSE_RECEIPT_OTHERFEE));
		item.setTotal_fee(rs.getDouble(Views.COL_WAREHOUSE_RECEIPT_TOTALFEE));
		item.setEmployee_id(rs.getInt(Views.COL_WAREHOUSE_RECEIPT_EMP_ID));
        Timestamp timestamps = rs.getTimestamp(Views.COL_WAREHOUSE_RECEIPT_DATE);
        if (timestamps != null) {
            item.setDate(timestamps.toLocalDateTime());
        }
				return item;
	}

}