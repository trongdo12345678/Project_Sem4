package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Warehouse;
import com.utils.Views;

public class WarehouseDISTANCE_mapper implements RowMapper<Warehouse>{
	public Warehouse mapRow(ResultSet r , int rowNum) throws SQLException{
		Warehouse item = new Warehouse();
		item.setId(r.getInt(Views.COL_WAREHOUSE_ID));
		item.setName(r.getString(Views.COL_WAREHOUSE_NAME));
		item.setAddress(r.getString(Views.COL_WAREHOUSE_ADDRESS));
		item.setWh_type_id(r.getInt(Views.COL_WAREHOUSE_TYPE_WAREHOUSE_ID));
		item.setLat(r.getDouble(Views.COL_WAREHOUSE_LAT));
		item.setLng(r.getDouble(Views.COL_WAREHOUSE_LNG));
        item.setDistrict_Id(r.getInt(Views.COL_WAREHOUSE_DISTRICT_ID));
		item.setProvince_Id(r.getInt(Views.COL_WAREHOUSE_PROVINCE_ID));
		item.setWard_Id(r.getString(Views.COL_WAREHOUSE_WARD_ID));
		item.setGhn_store_code(r.getString(Views.COL_WAREHOUSE_GHN_STORE_ID));

		return item;
	}

}
