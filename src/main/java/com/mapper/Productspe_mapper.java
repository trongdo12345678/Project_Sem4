package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.Product;
import com.models.Product_spe;
import com.utils.Views;

public class Productspe_mapper implements RowMapper<Product_spe> {
    public Product_spe mapRow(ResultSet rs, int RowNum) throws SQLException {
        Product_spe item = new Product_spe();
        item.setId(rs.getInt(Views.COL_PRODUCT_ID));
        item.setName_spe(rs.getString(Views.COL_PRODUCT_SPE_NAME));
        item.setDes_spe(rs.getString(Views.COL_PRODUCT_SPE_DES));
        item.setProduct_id(rs.getInt(Views.COL_PRODUCT_SPE_PRODUCTID));
      

        return item;
    }
}

