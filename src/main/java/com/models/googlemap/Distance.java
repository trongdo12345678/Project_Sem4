package com.models.googlemap;

import lombok.Data;
import java.util.List;

@Data
public class Distance {
    private Double value;

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
    
}