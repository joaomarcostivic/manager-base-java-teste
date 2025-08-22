package com.tivic.manager.mob.ait.relatorios.quantitativo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RelatorioQuantitativoDTO {
	
	private int tpStatus;
	private String dsTpStatus;
	private Double vlTotalMultas;
	private int quantidadeMovimentos;

	public int getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}

	public String getDsTpStatus() {
		return dsTpStatus;
	}

	public void setDsTpStatus(String dsTpStatus) {
		this.dsTpStatus = dsTpStatus;
	}

	public Double getVlTotalMultas() {
		return vlTotalMultas;
	}

	public void setVlTotalMultas(Double vlTotalMultas) {
		this.vlTotalMultas = vlTotalMultas;
	}

	public int getQuantidadeMovimentos() {
		return quantidadeMovimentos;
	}

	public void setQuantidadeMovimentos(int quantidadeMovimentos) {
		this.quantidadeMovimentos = quantidadeMovimentos;
	}

	@Override
	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } 
        catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
	}

}
