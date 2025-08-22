package com.tivic.manager.mob.aitpagamento;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class AitPagamentoRecebidoDTO {

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPeriodoPagamento;
	private int mesPagamento;
	private int anoPagamento;
	private int qtdPagamentos;
	private Double vlTotal;
	private Double vlFunset;
	private Double vlLiquido;
	private String sgUfVeiculo;
	private String nrBanco;

	public String getSgUfVeiculo() {
		return sgUfVeiculo;
	}

	public void setSgUfVeiculo(String sgUfVeiculo) {
		this.sgUfVeiculo = sgUfVeiculo;
	}

	public String getNrBanco() {
		return nrBanco;
	}

	public void setNrBanco(String nrBanco) {
		this.nrBanco = nrBanco;
	}

	public int getMesPagamento() {
		return mesPagamento;
	}

	public void setMesPagamento(int mesPagamento) {
		this.mesPagamento = mesPagamento;
	}

	public int getAnoPagamento() {
		return anoPagamento;
	}

	public void setAnoPagamento(int anoPagamento) {
		this.anoPagamento = anoPagamento;
	}

	public GregorianCalendar getDtPeriodoPagamento() {
		return dtPeriodoPagamento;
	}

	public void setDtPeriodoPagamento(GregorianCalendar dtPeriodoPagamento) {
		this.dtPeriodoPagamento = dtPeriodoPagamento;
	}

	public int getQtdPagamentos() {
		return qtdPagamentos;
	}

	public void setQtdPagamentos(int qtdPagamentos) {
		this.qtdPagamentos = qtdPagamentos;
	}

	public Double getVlTotal() {
		return vlTotal;
	}

	public void setVlTotal(Double vlTotal) {
		this.vlTotal = vlTotal;
	}

	public Double getVlFunset() {
		return vlFunset;
	}

	public void setVlFunset(Double vlFunset) {
		this.vlFunset = vlFunset;
	}

	public Double getVlLiquido() {
		return vlLiquido;
	}

	public void setVlLiquido(Double vlLiquido) {
		this.vlLiquido = vlLiquido;
	}
}
