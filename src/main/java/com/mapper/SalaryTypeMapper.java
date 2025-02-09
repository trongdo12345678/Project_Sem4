package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.SalaryType;
import com.utils.Views;

public class SalaryTypeMapper implements RowMapper<SalaryType> {
    @Override
    public SalaryType mapRow(ResultSet rs, int rowNum) throws SQLException {
        SalaryType type = new SalaryType();
        type.setId(rs.getInt(Views.COL_SALARY_TYPE_ID));
        type.setName(rs.getString(Views.COL_SALARY_TYPE_NAME));
        type.setDescription(rs.getString(Views.COL_SALARY_TYPE_DESC));
        type.setIsActive(rs.getString(Views.COL_SALARY_TYPE_IS_ACTIVE));
        return type;
    }
}