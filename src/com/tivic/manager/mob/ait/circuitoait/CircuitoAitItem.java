package com.tivic.manager.mob.ait.circuitoait;

import java.util.ArrayList;
import java.util.List;

public class CircuitoAitItem {
	private Integer tpStatus;
	private Integer itemCancelamento;
	private List<Integer> itensImpeditivos; 
	private CircuitoAitItemPrazo itemExigido;
	private List<Integer> itensPossiveis;
	
	public CircuitoAitItem() {
		this.itensImpeditivos = new ArrayList<Integer>();
		this.itensPossiveis = new ArrayList<Integer>();
		this.tpStatus = 0;
		this.itemCancelamento = 0; 
	}
	
	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}
	
	public int getTpStatus() {
		return this.tpStatus;
	}
	
	public Integer getItemCancelamento() {
		return this.itemCancelamento;
	}
	
	public void setItemCancelamento(Integer itemCancelamento) {
		this.itemCancelamento = itemCancelamento;
	}
	
	public List<Integer> getItensImpeditivos() {
		return this.itensImpeditivos;
	}
	
	public void addItensImpeditivos(Integer circuitoAitItem) {
		this.itensImpeditivos.add(circuitoAitItem);
	}
	
	public CircuitoAitItemPrazo getItemExigido() {
		return itemExigido;
	}
	
	public void setItemExigido(CircuitoAitItemPrazo itemExigido) {
		this.itemExigido = itemExigido;
	}
	
	public List<Integer> getItensPossiveis() {
		return itensPossiveis;
	}

	public void addItensPossiveis(Integer circuitoAitItem) {
		this.itensPossiveis.add(circuitoAitItem);
	}
	
}
