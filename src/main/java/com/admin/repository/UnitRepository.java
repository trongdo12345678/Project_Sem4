package com.admin.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mapper.Unit_mapper;
import com.models.Unit;

@Repository
public class UnitRepository {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public int addUnit(Unit unit) {
        String sql = "INSERT INTO Unit (Name) VALUES (?)";
        return jdbcTemplate.update(sql, unit.getName());
    }
    public int countByUnitId(int brandId) {
		try {
			String sql = "SELECT COUNT(*) FROM Conversion WHERE From_unit_id = ?";
		    return jdbcTemplate.queryForObject(sql, Integer.class, brandId);
		} catch (Exception e) {
			return 0;
		}
	}
    public List<Unit> getAllUnits() {
        String sql = "SELECT * FROM Unit";
        return jdbcTemplate.query(sql, new Unit_mapper());
    }
    
    public Unit findUnitById(int unitId) {
        String sql = "SELECT * FROM Unit WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Unit_mapper(), unitId);
    }

    public int deleteUnit(int unitId) {
    	
    	String deleteConversionSql = "DELETE FROM Conversion WHERE from_unit_id = ? OR to_unit_id = ?";
        jdbcTemplate.update(deleteConversionSql, unitId, unitId);
        
    	String sql = "DELETE FROM Unit WHERE id = ?";
    	return jdbcTemplate.update(sql,unitId);
    }
    
    public int updateUnit(Unit unit) {
        String sql = "UPDATE Unit SET Name = ? WHERE id = ?";
        return jdbcTemplate.update(sql, unit.getName(), unit.getId());
    }

}
