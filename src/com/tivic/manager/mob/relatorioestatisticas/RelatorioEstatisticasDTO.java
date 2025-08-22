package com.tivic.manager.mob.relatorioestatisticas;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RelatorioEstatisticasDTO {
	private GregorianCalendar dtNotificacao;
	private int qtNotificacao;
	private int mesNotificacao;
	private int anoNotificacao;
	private int qtDeferimento;
	private int qtIndeferimento;
	private double prDeferimento;
	private double prIndeferimento;
	private int tempoJulgamento;
	private int qtRecorrida;
	private String categoryAxis;
	
	public String getCategoryAxis() {
		return categoryAxis;
	}
	public void setCategoryAxis(String categoryAxis) {
		this.categoryAxis = categoryAxis;
	}
	public int getQtRecorrida() {
		return qtRecorrida;
	}
	public void setQtRecorrida(int qtRecorrida) {
		this.qtRecorrida = qtRecorrida;
	}
	public double getPrDeferimento() {
		return prDeferimento;
	}
	public void setPrDeferimento(double prDeferimento) {
		this.prDeferimento = prDeferimento;
	}
	public double getPrIndeferimento() {
		return prIndeferimento;
	}
	public void setPrIndeferimento(double prIndeferimento) {
		this.prIndeferimento = prIndeferimento;
	}
	public GregorianCalendar getDtNotificacao() {
		return dtNotificacao;
	}
	public void setDtNotificacao(GregorianCalendar dtNotificacao) {
		this.dtNotificacao = dtNotificacao;
	}
	public int getQtNotificacao() {
		return qtNotificacao;
	}
	public void setQtNotificacao(int qtNotificacao) {
		this.qtNotificacao = qtNotificacao;
	}
	public int getMesNotificacao() {
		return mesNotificacao;
	}
	public void setMesNotificacao(int mesNotificacao) {
		this.mesNotificacao = mesNotificacao;
	}
	public int getAnoNotificacao() {
		return anoNotificacao;
	}
	public void setAnoNotificacao(int anoNotificacao) {
		this.anoNotificacao = anoNotificacao;
	}
	public int getQtDeferimento() {
		return qtDeferimento;
	}
	public void setQtDeferimento(int qtDeferimento) {
		this.qtDeferimento = qtDeferimento;
	}
	public int getQtIndeferimento() {
		return qtIndeferimento;
	}
	public void setQtIndeferimento(int qtIndeferimento) {
		this.qtIndeferimento = qtIndeferimento;
	}
	public int getTempoJulgamento() {
		return tempoJulgamento;
	}
	public void setTempoJulgamento(int tempoJulgamento) {
		this.tempoJulgamento = tempoJulgamento;
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
