package com.tivic.manager.ptc.protocolos.julgamento;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class CartaJulgamentoDTO {

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtMovimento;
	private int cdAit;
	private String idAit;
	private int tpResultado;
	private int cdLoteImpressao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtCriacao;
	private int tpImpressao;
	private int cdUsuario;
	private int qtdDocumentos;
	private String idLote;
	private String nmPessoa;
	private int qtDocumentosGerados;
	private int totalGerados;
	
	public GregorianCalendar getDtMovimento() {
		return dtMovimento;
	}
	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
	}
	public String getIdAit() {
		return idAit;
	}
	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	public int getTpResultado() {
		return tpResultado;
	}
	public void setTpResultado(int tpResultado) {
		this.tpResultado = tpResultado;
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
	public int getTpImpressao() {
		return tpImpressao;
	}
	public void setTpImpressao(int tpImpressao) {
		this.tpImpressao = tpImpressao;
	}
	public int getCdUsuario() {
		return cdUsuario;
	}
	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}
	public int getQtdDocumentos() {
		return qtdDocumentos;
	}
	public void setQtdDocumentos(int qtdDocumentos) {
		this.qtdDocumentos = qtdDocumentos;
	}
	public String getIdLote() {
		return idLote;
	}
	public void setIdLote(String idLote) {
		this.idLote = idLote;
	}
	public String getNmPessoa() {
		return nmPessoa;
	}
	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}
	public int getQtDocumentosGerados() {
		return qtDocumentosGerados;
	}
	public void setQtDocumentosGerados(int qtDocumentosGerados) {
		this.qtDocumentosGerados = qtDocumentosGerados;
	}
	public int getTotalGerados() {
		return totalGerados;
	}
	public void setTotalGerados(int totalGerados) {
		this.totalGerados = totalGerados;
	}
	
	public int getCdAit() {
		return cdAit;
	}
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	
	@Override
	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } 
        catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
	}
}


