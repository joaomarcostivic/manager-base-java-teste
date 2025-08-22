package com.tivic.manager.util.pagination;

import java.util.List;

public class PagedResponse<T> {
	
    private List<T> items;
    private int total;
    
    public PagedResponse(List<T> items, int total) {
    	this.setItems(items);
    	this.setTotal(total);
    }

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
