package com.tivic.manager.mob.listagempagamentos;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class RelatorioPagamentoDTO {
	private int cdAit;
	private String idAit;
	private String nrCodDetran;
	private int stPagamento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPagamento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtCredito;
	private float vlMulta;
	private float vlPago;
	private String sgUfPagamento;
	private int tpArrecadacao;
	private int tpFormaPagamento;
	private String nmBanco;
	private String dsStPagamento;
	private String dsTpFormaPagamento;
	private String dsTpArrecadacao;
	private int valueAxis;
	private String categoryAxis;
	
	public String getDsTpArrecadacao() {
		return dsTpArrecadacao;
	}
	public void setDsTpArrecadacao(String dsTpArrecadacao) {
		this.dsTpArrecadacao = dsTpArrecadacao;
	}
	public String getCategoryAxis() {
		return categoryAxis;
	}
	public void setCategoryAxis(String categoryAxis) {
		this.categoryAxis = categoryAxis;
	}
	public int getValueAxis() {
		return valueAxis;
	}
	public void setValueAxis(int valueAxis) {
		this.valueAxis = valueAxis;
	}
	public String getDsTpFormaPagamento() {
		return dsTpFormaPagamento;
	}
	public void setDsTpFormaPagamento(String dsTpFormaPagamento) {
		this.dsTpFormaPagamento = dsTpFormaPagamento;
	}
	public String getDsStPagamento() {
		return dsStPagamento;
	}
	public void setDsStPagamento(String dsStPagamento) {
		this.dsStPagamento = dsStPagamento;
	}
	public int getCdAit() {
		return cdAit;
	}
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	public String getIdAit() {
		return idAit;
	}
	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	public String getNrCodDetran() {
		return nrCodDetran;
	}
	public void setNrCodDetran(String nrCodDetran) {
		this.nrCodDetran = nrCodDetran;
	}
	public int getStPagamento() {
		return stPagamento;
	}
	public void setStPagamento(int stPagamento) {
		this.stPagamento = stPagamento;
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
	public float getVlMulta() {
		return vlMulta;
	}
	public void setVlMulta(float vlMulta) {
		this.vlMulta = vlMulta;
	}
	public float getVlPago() {
		return vlPago;
	}
	public void setVlPago(float vlPago) {
		this.vlPago = vlPago;
	}
	public String getSgUfPagamento() {
		return sgUfPagamento;
	}
	public void setSgUfPagamento(String sgUfPagamento) {
		this.sgUfPagamento = sgUfPagamento;
	}
	public int getTpArrecadacao() {
		return tpArrecadacao;
	}
	public void setTpArrecadacao(int tpArrecadacao) {
		this.tpArrecadacao = tpArrecadacao;
	}
	public int getTpFormaPagamento() {
		return tpFormaPagamento;
	}
	public void setTpFormaPagamento(int tpFormaPagamento) {
		this.tpFormaPagamento = tpFormaPagamento;
	}
	public String getNmBanco() {
		return nmBanco;
	}
	public void setNmBanco(String nmBanco) {
		this.nmBanco = nmBanco;
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
