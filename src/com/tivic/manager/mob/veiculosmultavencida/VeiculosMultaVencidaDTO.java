package com.tivic.manager.mob.veiculosmultavencida;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VeiculosMultaVencidaDTO {
	private String nrPlaca;
	private String nmProprietario;
	private int qtMultasVencidas;
	private double vlTotalMultasVencidas;
	
	public String getNrPlaca() {
		return nrPlaca;
	}
	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}
	public String getNmProprietario() {
		return nmProprietario;
	}
	public void setNmProprietario(String nmProprietario) {
		this.nmProprietario = nmProprietario;
	}
	public int getQtMultasVencidas() {
		return qtMultasVencidas;
	}
	public void setQtMultasVencidas(int qtMultasVencidas) {
		this.qtMultasVencidas = qtMultasVencidas;
	}
	public double getVlTotalMultasVencidas() {
		return vlTotalMultasVencidas;
	}
	public void setVlTotalMultasVencidas(double vlTotalMultasVencidas) {
		this.vlTotalMultasVencidas = vlTotalMultasVencidas;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possivel serializar o objeto.";
		}
	}
}
