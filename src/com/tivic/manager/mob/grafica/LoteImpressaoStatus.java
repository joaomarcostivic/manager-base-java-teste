package com.tivic.manager.mob.grafica;

import java.util.GregorianCalendar;

public class LoteImpressaoStatus {
	private int cdLoteImpressao;
	private String idLoteImpressaoAit;
	private GregorianCalendar dtInicio;
	private int totalDocumentos;
	private int qtDocumentosGerados;
	private int stDocumento;
	
	public int getStDocumento() {
		return stDocumento;
	}
	public void setStDocumento(int stDocumento) {
		this.stDocumento = stDocumento;
	}
	public int getCdLoteImpressao() {
		return cdLoteImpressao;
	}
	public void setCdLoteImpressao(int cdLoteImpressao) {
		this.cdLoteImpressao = cdLoteImpressao;
	}
	public String getIdLoteImpressaoAit() {
		return idLoteImpressaoAit;
	}
	public void setIdLoteImpressaoAit(String idLoteImpressaoAit) {
		this.idLoteImpressaoAit = idLoteImpressaoAit;
	}
	public GregorianCalendar getDtInicio() {
		return dtInicio;
	}
	public void setDtInicio(GregorianCalendar dtInicio) {
		this.dtInicio = dtInicio;
	}
	public int getTotalDocumentos() {
		return totalDocumentos;
	}
	public void setTotalDocumentos(int totalDocumentos) {
		this.totalDocumentos = totalDocumentos;
	}
	public int getQtDocumentosGerados() {
		return qtDocumentosGerados;
	}
	public void setQtDocumentosGerados(int qtDocumentosGerados) {
		this.qtDocumentosGerados = qtDocumentosGerados;
	}
}
