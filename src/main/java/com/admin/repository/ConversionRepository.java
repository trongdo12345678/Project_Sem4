package com.admin.repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mapper.Conversion_mapper;
import com.models.Conversion;
import com.models.Unit;
import com.utils.Views;

@Repository
public class ConversionRepository {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<Map<String, Object>> findAllConversions(int product_Id) {
        String query = String.format(
                "SELECT c.*, p.%s as product_name, u1.%s as from_unit_name, u2.%s as to_unit_name, c.%s as conversion_rate " +
                "FROM %s c " +
                "INNER JOIN %s p ON c.%s = p.%s " +
                "INNER JOIN %s u1 ON c.%s = u1.%s " +
                "INNER JOIN %s u2 ON c.%s = u2.%s " +
                "WHERE c.%s = ? " +
                "ORDER BY c.%s ASC",
                Views.COL_PRODUCT_NAME, 
                Views.COL_UNIT_NAME,   
                Views.COL_UNIT_NAME,
                Views.COL_CONVERSION_RATE,
                Views.TBL_CONVERSION,   
                Views.TBL_PRODUCT, Views.COL_CONVERSION_PRODUCT_ID, Views.COL_PRODUCT_ID, 
                Views.TBL_UNIT, Views.COL_CONVERSION_FROM_UNIT_ID, Views.COL_UNIT_ID,     
                Views.TBL_UNIT, Views.COL_CONVERSION_TO_UNIT_ID, Views.COL_UNIT_ID,       
                Views.COL_CONVERSION_PRODUCT_ID, 
                Views.COL_CONVERSION_ID 
        );

        try {
            return jdbcTemplate.queryForList(query, product_Id);
        } catch (DataAccessException e) {
            e.printStackTrace(); 
            return Collections.emptyList(); 
        }
    }



    public List<Unit> findAllUnit() {
        String sql = "SELECT Id, Name FROM Unit"; 
        try {
            List<Unit> units = jdbcTemplate.query(sql, (rs, rowNum) -> {
                Unit item = new Unit();
                item.setId(rs.getInt(Views.COL_UNIT_ID));
                item.setName(rs.getString(Views.COL_UNIT_NAME));
                return item;
            });
            

            if (units.isEmpty()) {
                throw new RuntimeException("No unit found in the database.");
            }
            
            return units;
        } catch (DataAccessException e) {

            System.err.println("Database query error: " + e.getMessage());
            throw new RuntimeException("Error fetching products from the database.", e);
        }
    }
    
    public int addConversion(Conversion conversion) {
        String sql = "INSERT INTO conversion (Product_Id,from_unit_id, to_unit_id, conversion_rate) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
        	conversion.getProduct_id(),
            conversion.getFrom_unit_id(), 
            conversion.getTo_unit_id(), 
            conversion.getConversion_rate()
        );
    }
    public String getUnitNameById(int unitId) {
        String sql = "SELECT name AS unitName FROM Unit WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getString("unitName"), unitId);
    }


    public int deleteConversion(int conversionId, int productId) {
        String sql = "DELETE FROM Conversion WHERE id = ? AND Product_Id = ?";
        return jdbcTemplate.update(sql, conversionId, productId);
    }


    public int updateConversion(Conversion conversion) {
        String sql = "UPDATE Conversion SET from_unit_id = ?, to_unit_id = ?, conversion_rate = ? WHERE id = ?";
        return jdbcTemplate.update(sql, 
            conversion.getFrom_unit_id(),
            conversion.getTo_unit_id(),
            conversion.getConversion_rate(),
            conversion.getId()
        );
    }
    
    public Conversion findById(int id) {
        String sql = "SELECT c.*, u1.name AS from_unit_name, u2.name AS to_unit_name " +
                     "FROM Conversion c " +
                     "JOIN Unit u1 ON c.from_unit_id = u1.id " +
                     "JOIN Unit u2 ON c.to_unit_id = u2.id " +
                     "WHERE c.id = ?";
        return jdbcTemplate.queryForObject(sql, new Conversion_mapper(), id);
    }


}