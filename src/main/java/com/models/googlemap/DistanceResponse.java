package com.models.googlemap;

import lombok.Data;
import java.util.List;

@Data
public class DistanceResponse {
    private List<Row> rows;

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
    
}