package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.models.Order;
import com.utils.Views;

public class Order_mapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order item = new Order();

        item.setId(rs.getInt(Views.COL_ORDER_ID));
        item.setCustomer_id(rs.getInt(Views.COL_ORDER_CUSTOMER_ID));
        item.setEmployee_id(rs.getInt(Views.COL_ORDER_EMPLOYEE));
        item.setPayment_id(rs.getInt(Views.COL_ORDER_PAYMENT_ID));
        item.setStatus(rs.getString(Views.COL_ORDER_STATUS));
        item.setCus_Name(rs.getString(Views.COL_ORDER_CUSNAME));
        item.setPhone(rs.getString(Views.COL_ORDER_PHONE));
        item.setAddress(rs.getString(Views.COL_ORDER_ADDRESS));
        item.setPay_status(rs.getString(Views.COL_ORDER_PAYSTATUS));
        
        
        Timestamp timestampCreationTime1 = rs.getTimestamp(Views.COL_ORDER_DATE);
        if (timestampCreationTime1 != null) {
            item.setDate(timestampCreationTime1.toLocalDateTime().toLocalDate());
        }

        
        item.setCoupon_id(rs.getInt(Views.COL_ORDER_COUPONID));
        item.setDiscount(rs.getDouble(Views.COL_ORDER_DISCOUNT));
        item.setTotalAmount(rs.getDouble(Views.COL_ORDER_TOTALAMOUNT));
        item.setShippingFee(rs.getDouble(Views.COL_ORDER_SHIPPINGFEE));
        item.setNotes(rs.getString(Views.COL_ORDER_NOTES));
        item.setOrderID(rs.getString(Views.COL_ORDER_ORDERID));
        item.setWareHouse_Id(rs.getInt(Views.COL_ORDER_WAREHOUSE_ID));
        item.setTransactionId(rs.getString(Views.COL_ORDER_TRANSMOMOID));
        item.setDistrict_Id(rs.getInt(Views.COL_ORDER_DISTRICT_ID));
        item.setProvince_Id(rs.getInt(Views.COL_ORDER_PROVINCE_ID));
        item.setWard_Id(rs.getString(Views.COL_ORDER_WARD_ID));
        item.setGhn_order_code(rs.getString(Views.COL_ORDER_GHN_ORDER_CODE));
        item.setTracking_code(rs.getString(Views.COL_ORDER_TRACKING_CODE));
     // Map Date
        Timestamp timestampCreationTime2 = rs.getTimestamp(Views.COL_ORDER_EXPECTED_DELIVERY_TIME);
        if (timestampCreationTime2 != null) {
            item.setExpected_delivery_time(timestampCreationTime2.toLocalDateTime().toLocalDate());
        }
        item.setShipping_status(rs.getString(Views.COL_ORDER_SHIPPING_STATUS));

        return item;
    }
}