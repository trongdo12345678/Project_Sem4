package com.models.googlemap;

import lombok.Data;
import java.util.List;


@Data
public class Element {
    private Distance distance;

	public Distance getDistance() {
		return distance;
	}

	public void setDistance(Distance distance) {
		this.distance = distance;
	}
    
}