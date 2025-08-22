package com.tivic.manager.mob.aitmovimento;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class AitMovimentoDTO extends AitMovimento {
	private int cdAit;
	private int tpStatus;
	private String idAit;
	private int lgCorrecao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtVencimento;
	private String nrErro;
	private String ultimoRetorno;
	private Integer qtArquivoMovimento;
	private String nrControle;
	private String nmLogin;
	private String retornoMensagem;
	private int cdMovimentoCancelamento;
	
	public AitMovimentoDTO() {
		super();
	}
	
	public AitMovimentoDTO(int cdAit, int tpStatus, String idAit, int lgCorrecao, GregorianCalendar dtVencimento, GregorianCalendar dtMovimento, String nrErro, String ultimoRetorno, Integer qtArquivoMovimento, String retornoMensagem, int cdMovimentoCancelamento) {
		this.cdAit = cdAit;
		this.tpStatus = tpStatus;
		this.idAit = idAit;
		this.lgCorrecao = lgCorrecao;
		this.dtVencimento = dtVencimento;
		this.nrErro = nrErro;
		this.ultimoRetorno = ultimoRetorno;
		this.qtArquivoMovimento = qtArquivoMovimento;
		this.retornoMensagem = retornoMensagem;
		this.cdMovimentoCancelamento = cdMovimentoCancelamento;
	}

	public int getCdAit() {
		return cdAit;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}

	public int getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}

	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	
	public int getLgCorrecao() {
		return lgCorrecao;
	}

	public void setLgCorrecao(int lgCorrecao) {
		this.lgCorrecao = lgCorrecao;
	}

	public GregorianCalendar getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(GregorianCalendar dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public String getNrErro() {
		return nrErro;
	}

	public void setNrErro(String nrErro) {
		this.nrErro = nrErro;
	}

	public String getUltimoRetorno() {
		return ultimoRetorno;
	}

	public void setUltimoRetorno(String ultimoRetorno) {
		this.ultimoRetorno = ultimoRetorno;
	}

	public Integer getQtArquivoMovimento() {
		return qtArquivoMovimento;
	}

	public void setQtArquivoMovimento(Integer qtArquivoMovimento) {
		this.qtArquivoMovimento = qtArquivoMovimento;
	}

	public String getNrControle() {
		return nrControle;
	}

	public void setNrControle(String nrControle) {
		this.nrControle = nrControle;
	}

	public String getNmLogin() {
		return nmLogin;
	}

	public void setNmLogin(String nmLogin) {
		this.nmLogin = nmLogin;
	}
	
	public String getRetornoMensagem() {
		return retornoMensagem;
	}

	public void setRetornoMensagem(String retornoMensagem) {
		this.retornoMensagem = retornoMensagem;
	}
	
	public int getCdMovimentoCancelamento() {
		return this.cdMovimentoCancelamento;
	}

	public void setCdMovimentoCancelamento(int cdMovimentoCancelamento) {
		this.cdMovimentoCancelamento = cdMovimentoCancelamento;
	}
}
