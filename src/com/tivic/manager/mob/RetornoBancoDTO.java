package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class RetornoBancoDTO {
	String idAit;
	String nrControle;
	@JsonSerialize(converter = CalendarSerializer.class)
	GregorianCalendar dtPagamento;
	String nmBanco;
	double vlPago;
	String stImportacao;
	
	public RetornoBancoDTO() {
		super();
	}
	
	public RetornoBancoDTO(String idAit, String nrControle, GregorianCalendar dtPagamento, double vlPago, String stImportacao) {
		this.idAit = idAit;
		this.nrControle = nrControle;
		this.dtPagamento = dtPagamento;
		this.vlPago = vlPago;
		this.stImportacao = stImportacao;
	}

	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}

	public String getNrControle() {
		return nrControle;
	}

	public void setNrControle(String nrControle) {
		this.nrControle = nrControle;
	}

	public GregorianCalendar getDtPagamento() {
		return dtPagamento;
	}

	public void setDtPagamento(GregorianCalendar dtPagamento) {
		this.dtPagamento = dtPagamento;
	}

	public String getNmBanco() {
		return nmBanco;
	}

	public void setNmBanco(String nmBanco) {
		this.nmBanco = nmBanco;
	}

	public double getVlPago() {
		return vlPago;
	}

	public void setVlPago(double vlPago) {
		this.vlPago = vlPago;
	}

	public String getStImportacao() {
		return stImportacao;
	}

	public void setStImportacao(String stImportacao) {
		this.stImportacao = stImportacao;
	}
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
	
}
