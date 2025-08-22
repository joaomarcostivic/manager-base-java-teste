package com.tivic.manager.mob.lotes.dto.impressao;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class LoteNotificacaoDTO {
	
	private int cdLoteImpressao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtCriacao;
	private String idAit;
	private int tpDocumento;
	private int cdUsuario;
	private int nrEtiqueta;
	private int qtdDocumentos;
	private String idLoteImpressao;
	private String nmPessoa;
	private int qtDocumentosGerados;
	private int stLoteImpressao;
	private int totalGerados;
	
	public LoteNotificacaoDTO() {}
	
	public int getTotalGerados() {
		return totalGerados;
	}

	public void setTotalGerados(int totalGerados) {
		this.totalGerados = totalGerados;
	}

	public int getQtDocumentosGerados() {
		return qtDocumentosGerados;
	}

	public void setQtDocumentosGerados(int qtDocumentosGerados) {
		this.qtDocumentosGerados = qtDocumentosGerados;
	}
	
	public int getCdLoteImpressao() {
		return cdLoteImpressao;
	}
	public void setCdLoteImpressao(int cdLoteImpressao) {
		this.cdLoteImpressao = cdLoteImpressao;
	}
	public GregorianCalendar getDtCriacao() {
		return dtCriacao;
	}
	public void setDtCriacao(GregorianCalendar dtCriacao) {
		this.dtCriacao = dtCriacao;
	}
	public String getIdAit() {
		return idAit;
	}
	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	public int getTpDocumento() {
		return tpDocumento;
	}
	public void setTpDocumento(int tpDocumento) {
		this.tpDocumento = tpDocumento;
	}
	public int getCdUsuario() {
		return cdUsuario;
	}
	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}
	public int getNrEtiqueta() {
		return nrEtiqueta;
	}
	public void setNrEtiqueta(int nrEtiqueta) {
		this.nrEtiqueta = nrEtiqueta;
	}
	public int getQtdDocumentos() {
		return qtdDocumentos;
	}
	public void setQtdDocumentos(int qtdDocumentos) {
		this.qtdDocumentos = qtdDocumentos;
	}
	public String getIdLoteImpressao() {
		return idLoteImpressao;
	}
	public void setIdLoteImpressao(String idLoteImpressao) {
		this.idLoteImpressao = idLoteImpressao;
	}
	public String getNmPessoa() {
		return nmPessoa;
	}
	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}

	public int getStLoteImpressao() {
		return stLoteImpressao;
	}

	public void setStLoteImpressao(int stLoteImpressao) {
		this.stLoteImpressao = stLoteImpressao;
	}

}
