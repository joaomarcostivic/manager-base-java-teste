package com.tivic.manager.mob.ait.circuitoait;

public class CircuitoAitItemPrazo {

	private Integer circuitoAitItem;
	private Integer prazoDias;
	
	public CircuitoAitItemPrazo(Integer circuitoAitItem) {
		this.circuitoAitItem = circuitoAitItem;
	}

	public CircuitoAitItemPrazo(Integer circuitoAitItem, Integer prazoDias) {
		this.circuitoAitItem = circuitoAitItem;
		this.prazoDias = prazoDias;
	}
	
	public void setCirculoAitItem(Integer circuloAitItem) {
		this.circuitoAitItem = circuloAitItem;
	}
	
	public Integer getCirculoAitItem() {
		return circuitoAitItem;
	}
	
	public void setPrazoDias(Integer prazoDias) {
		this.prazoDias = prazoDias;
	}
	
	public Integer getPrazoDias() {
		return prazoDias;
	}
	
}
