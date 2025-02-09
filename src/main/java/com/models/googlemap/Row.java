package com.models.googlemap;

import lombok.Data;
import java.util.List;

@Data
public class Row {
    private List<Element> elements;

	public List<Element> getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}
    
}