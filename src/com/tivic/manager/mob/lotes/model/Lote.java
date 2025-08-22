package com.tivic.manager.mob.lotes.model;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Lote {
	private int cdLote;
	private String idLote;
	private GregorianCalendar dtCriacao;
	private int cdCriador;
	private int cdArquivo;

	public Lote() {}

	public Lote(GregorianCalendar dtCriacao, int cdCriador, String idLote) {
		setDtCriacao(dtCriacao);
		setCdCriador(cdCriador);
		setIdLote(idLote);
	}

	public Lote(int cdLote, String idLote, GregorianCalendar dtCriacao, int cdCriador, int cdArquivo) {
		setCdLote(cdLote);
		setIdLote(idLote);
		setDtCriacao(dtCriacao);
		setCdCriador(cdCriador);
		setCdArquivo(cdArquivo);
	}

	public void setCdLote(int cdLote) {
		this.cdLote = cdLote;
	}

	public int getCdLote() {
		return this.cdLote;
	}

	public void setIdLote(String idLote) {
		this.idLote = idLote;
	}

	public String getIdLote() {
		return this.idLote;
	}

	public void setDtCriacao(GregorianCalendar dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public GregorianCalendar getDtCriacao() {
		return this.dtCriacao;
	}

	public void setCdCriador(int cdCriador) {
		this.cdCriador = cdCriador;
	}

	public int getCdCriador() {
		return this.cdCriador;
	}

	public void setCdArquivo(int cdArquivo) {
		this.cdArquivo = cdArquivo;
	}

	public int getCdArquivo() {
		return this.cdArquivo;
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
