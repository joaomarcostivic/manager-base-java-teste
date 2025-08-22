package com.tivic.manager.mob.pagamento;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BancoDadosRetorno {
	private double vlTarifa;
	private String nrBanco;
	private String nrAgencia;
	private String nrAgenciaCredito;
	private String nrContaCredito;
	private int tpArrecadacao;
	private int tpPagamento;
	private double vlPago;
	private String nrControleSemZeros;
	private GregorianCalendar dtPagamento;
	private GregorianCalendar dtCredito;
	private String nrCodigoBarras;
	private String nrControleComZeros;
	private String nrPlaca;
	private String nrCodigoBarra;

	public double getVlTarifa() {
		return vlTarifa;
	}

	public void setVlTarifa(double vlTarifa) {
		this.vlTarifa = vlTarifa;
	}

	public String getNrBanco() {
		return nrBanco;
	}

	public void setNrBanco(String nrBanco) {
		this.nrBanco = nrBanco;
	}

	public String getNrAgencia() {
		return nrAgencia;
	}

	public void setNrAgencia(String nrAgencia) {
		this.nrAgencia = nrAgencia;
	}

	public String getNrAgenciaCredito() {
		return nrAgenciaCredito;
	}

	public void setNrAgenciaCredito(String nrAgenciaCredito) {
		this.nrAgenciaCredito = nrAgenciaCredito;
	}

	public String getNrContaCredito() {
		return nrContaCredito;
	}

	public void setNrContaCredito(String nrContaCredito) {
		this.nrContaCredito = nrContaCredito;
	}

	public int getTpArrecadacao() {
		return tpArrecadacao;
	}

	public void setTpArrecadacao(int tpArrecadacao) {
		this.tpArrecadacao = tpArrecadacao;
	}

	public int getTpPagamento() {
		return tpPagamento;
	}

	public void setTpPagamento(int tpPagamento) {
		this.tpPagamento = tpPagamento;
	}

	public double getVlPago() {
		return vlPago;
	}

	public void setVlPago(double vlPago) {
		this.vlPago = vlPago;
	}

	public String getNrControleSemZeros() {
		return nrControleSemZeros;
	}

	public void setNrControleSemZeros(String nrControleSemZeros) {
		this.nrControleSemZeros = nrControleSemZeros;
	}

	public GregorianCalendar getDtPagamento() {
		return dtPagamento;
	}

	public void setDtPagamento(GregorianCalendar dtPagamento) {
		this.dtPagamento = dtPagamento;
	}

	public GregorianCalendar getDtCredito() {
		return dtCredito;
	}

	public void setDtCredito(GregorianCalendar dtCredito) {
		this.dtCredito = dtCredito;
	}

	public String getNrCodigoBarras() {
		return nrCodigoBarras;
	}

	public void setNrCodigoBarras(String nrCodigoBarras) {
		this.nrCodigoBarras = nrCodigoBarras;
	}

	public String getNrControleComZeros() {
		return nrControleComZeros;
	}

	public void setNrControleComZeros(String nrControleComZeros) {
		this.nrControleComZeros = nrControleComZeros;
	}

	public String getNrPlaca() {
		return nrPlaca;
	}

	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}

	public String getNrCodigoBarra() {
		return nrCodigoBarra;
	}

	public void setNrCodigoBarra(String nrCodigoBarra) {
		this.nrCodigoBarra = nrCodigoBarra;
	}

	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
}