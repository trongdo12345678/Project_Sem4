
package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.models.Request;
import com.utils.Views;

public class Request_mapper implements RowMapper<Request>{
	public Request mapRow(ResultSet r,int rowNum) throws SQLException{
		Request item = new Request();
		item.setId(r.getInt(Views.COL_REQUEST_ID));
		item.setName(r.getString(Views.COL_REQUEST_NAME));
		item.setStatusRequest(r.getString(Views.COL_REQUEST_STATUS));
		item.setEmployee_Id(r.getInt(Views.COL_REQUEST_EMPLOYEE_ID));
		item.setWarehouse_Id(r.getInt(Views.COL_REQUEST_WAREHOUSE));
		item.setType(r.getString(Views.COL_REQUEST_TYPE));
		item.setOrder_id(r.getInt(Views.COL_REQUEST_ORDERID));

		Timestamp ts = r.getTimestamp(Views.COL_REQUEST_DATE);
		
		if(ts != null) {
			item.setDate(ts.toLocalDateTime());
		}
		return item;
	}

}

