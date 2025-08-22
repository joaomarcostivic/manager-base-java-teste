package com.tivic.manager.mob.lote.impressao;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class LoteImpressaoAitDTO extends LoteImpressaoAit {
	
	private String idAit;
	private int cdEtiqueta;
	private String nrPlaca;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtVencimento;
	private String nrRenavan;
	private String nmProprietario;
	private String idLoteImpressao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)	
	private GregorianCalendar dtCriacao;
	private String nmPessoa;
	private int qtdDocumentos;
	private int nrLote;
	private int cdLoteImpressao;
	private int nrEtiqueta;
	private int stLoteImpressao;
	private int tpRecurso;
	private int cdUsuario;
	
	public LoteImpressaoAitDTO() {}

	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}

	public String getNrPlaca() {
		return nrPlaca;
	}

	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}

	public int getCdEtiqueta() {
		return cdEtiqueta;
	}

	public void setCdEtiqueta(int cdEtiqueta) {
		this.cdEtiqueta = cdEtiqueta;
	}

	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}

	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
	}

	public GregorianCalendar getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(GregorianCalendar dtVencimento) {
		this.dtVencimento = dtVencimento;
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
	
	public String getIdLoteImpressao() {
		return idLoteImpressao;
	}

	public void setIdLoteImpressao(String idLoteImpressao) {
		this.idLoteImpressao = idLoteImpressao;
	}

	public GregorianCalendar getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(GregorianCalendar dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public String getNmPessoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}

	public int getQtdDocumentos() {
		return qtdDocumentos;
	}

	public void setQtdDocumentos(int qtdDocumentos) {
		this.qtdDocumentos = qtdDocumentos;
	}

	public int getNrLote() {
		return nrLote;
	}

	public void setNrLote(int nrLote) {
		this.nrLote = nrLote;
	}

	public int getCdLoteImpressao() {
		return cdLoteImpressao;
	}

	public void setCdLoteImpressao(int cdLoteImpressao) {
		this.cdLoteImpressao = cdLoteImpressao;
	}

	public int getNrEtiqueta() {
		return nrEtiqueta;
	}

	public void setNrEtiqueta(int nrEtiqueta) {
		this.nrEtiqueta = nrEtiqueta;
	}

	public int getStLoteImpressao() {
		return stLoteImpressao;
	}

	public void setStLoteImpressao(int stLoteImpressao) {
		this.stLoteImpressao = stLoteImpressao;
	}	
	
	public int getTpRecurso() {
		return tpRecurso;
	}

	public void setTpRecurso(int tpRecurso) {
		this.tpRecurso = tpRecurso;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
}
