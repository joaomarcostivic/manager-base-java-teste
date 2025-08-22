package com.tivic.manager.mob.lotes.dto.impressao;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class LoteImpressaoDTO {

	private int cdLoteImpressao;
	private int cdAit;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtCriacao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	private String idAit;
	private int tpImpressao;
	private int cdUsuario;
	private int qtdDocumentos;
	private String idLote;
	private String nmPessoa;
	private int stLote;
	private int qtDocumentosGerados;
	private int totalGerados;
	private String nrPlaca;
	private String nrRenavan;
	private String nmProprietario;
	
	public LoteImpressaoDTO() {}

	public int getCdLoteImpressao() {
		return cdLoteImpressao;
	}

	public void setCdLoteImpressao(int cdLoteImpressao) {
		this.cdLoteImpressao = cdLoteImpressao;
	}

	public int getCdAit() {
		return cdAit;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
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

	public int getStLote() {
		return stLote;
	}

	public void setStLote(int stLote) {
		this.stLote = stLote;
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
	
	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}

	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
	}

	public String getNrPlaca() {
		return nrPlaca;
	}

	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}

	public String getNrRenavan() {
		return nrRenavan;
	}

	public void setNrRenavan(String nrRenavan) {
		this.nrRenavan = nrRenavan;
	}

	public String getNmProprietario() {
		return nmProprietario;
	}

	public void setNmProprietario(String nmProprietario) {
		this.nmProprietario = nmProprietario;
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
