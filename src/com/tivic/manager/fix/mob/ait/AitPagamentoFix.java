package com.tivic.manager.fix.mob.ait;

import java.io.Serializable;
import java.util.GregorianCalendar;

import com.tivic.manager.util.JsonToStringBuilder;

public class AitPagamentoFix implements Serializable {
	private static final long serialVersionUID = -59578096725754786L;
	
	private String idAit;
	private GregorianCalendar dtPagamento;
	private Double vlPago;
	private String nmBanco;
	
	public AitPagamentoFix() {
		super();
	}
	public AitPagamentoFix(String idAit, GregorianCalendar dtPagamento, Double vlPago, String nmBanco) {
		super();
		this.idAit = idAit;
		this.dtPagamento = dtPagamento;
		this.vlPago = vlPago;
		this.nmBanco = nmBanco;
	}
	public String getIdAit() {
		return idAit;
	}
	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	public GregorianCalendar getDtPagamento() {
		return dtPagamento;
	}
	public void setDtPagamento(GregorianCalendar dtPagamento) {
		this.dtPagamento = dtPagamento;
	}
	public Double getVlPago() {
		return vlPago;
	}
	public void setVlPago(Double vlPago) {
		this.vlPago = vlPago;
	}
	public String getNmBanco() {
		return nmBanco;
	}
	public void setNmBanco(String nmBanco) {
		this.nmBanco = nmBanco;
	}
	
	@Override
	public String toString() {
		JsonToStringBuilder builder = new JsonToStringBuilder(this);
		builder.append("idAit", idAit);
		builder.append("dtPagamento", dtPagamento);
		builder.append("vlPago", vlPago);
		builder.append("nmBanco", nmBanco);
		return builder.toString();
	}
	
	

}
