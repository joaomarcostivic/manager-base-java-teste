package com.tivic.manager.mob.relatorioestatisticas;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RelatorioEstatisticasNipDTO {
	private GregorianCalendar dtNotificacao;
	private int qtNotificacao;
	private int mesNotificacao;
	private int anoNotificacao;
	private GregorianCalendar dtVencimento;
	private int qtVencida;
	private int mesVencimento;
	private int anoVencimento;
	private int qtDeferimento;
	private int qtIndeferimento;
	private double prDeferimento;
	private double prIndeferimento;
	private int qtRecorrida;
	private int prRecorrida;
	private int tempoJulgamento;
	private double qtAntesVencimento;
	private double qtPagasAtraso;
	private double qtInadimplentes;
	private int qtNipsPagasEmDia;
	private String categoryAxis;
	
	public String getCategoryAxis() {
		return categoryAxis;
	}

	public void setCategoryAxis(String categoryAxis) {
		this.categoryAxis = categoryAxis;
	}

	public int getQtNipsPagasEmDia() {
		return qtNipsPagasEmDia;
	}

	public void setQtNipsPagasEmDia(int qtNipsPagasEmDia) {
		this.qtNipsPagasEmDia = qtNipsPagasEmDia;
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

	public GregorianCalendar getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(GregorianCalendar dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public int getQtVencida() {
		return qtVencida;
	}

	public void setQtVencida(int qtVencida) {
		this.qtVencida = qtVencida;
	}

	public int getMesVencimento() {
		return mesVencimento;
	}

	public void setMesVencimento(int mesVencimento) {
		this.mesVencimento = mesVencimento;
	}

	public int getAnoVencimento() {
		return anoVencimento;
	}

	public void setAnoVencimento(int anoVencimento) {
		this.anoVencimento = anoVencimento;
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

	public int getQtRecorrida() {
		return qtRecorrida;
	}

	public void setQtRecorrida(int qtRecorrida) {
		this.qtRecorrida = qtRecorrida;
	}

	public int getPrRecorrida() {
		return prRecorrida;
	}

	public void setPrRecorrida(int prRecorrida) {
		this.prRecorrida = prRecorrida;
	}

	public int getTempoJulgamento() {
		return tempoJulgamento;
	}

	public void setTempoJulgamento(int tempoJulgamento) {
		this.tempoJulgamento = tempoJulgamento;
	}

	public double getQtAntesVencimento() {
		return qtAntesVencimento;
	}

	public void setQtAntesVencimento(double qtAntesVencimento) {
		this.qtAntesVencimento = qtAntesVencimento;
	}

	public double getQtPagasAtraso() {
		return qtPagasAtraso;
	}

	public void setQtPagasAtraso(double qtPagasAtraso) {
		this.qtPagasAtraso = qtPagasAtraso;
	}

	public double getQtInadimplentes() {
		return qtInadimplentes;
	}

	public void setQtInadimplentes(double qtInadimplentes) {
		this.qtInadimplentes = qtInadimplentes;
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
