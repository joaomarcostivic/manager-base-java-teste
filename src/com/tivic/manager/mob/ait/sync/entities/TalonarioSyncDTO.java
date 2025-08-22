package com.tivic.manager.mob.ait.sync.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TalonarioSyncDTO {

	private int cdTalao;
	private int nrTalao;
	private int nrInicial;
	private int nrFinal;
	private int cdAgente;
	private long dtEntrega;
	private long dtDevolucao;
	private int stTalao;
	private int tpTalao;
	private String sgTalao;
	private int nrUltimoAit;

	public int getCdTalao() {
		return cdTalao;
	}

	public void setCdTalao(int cdTalao) {
		this.cdTalao = cdTalao;
	}

	public int getNrTalao() {
		return nrTalao;
	}

	public void setNrTalao(int nrTalao) {
		this.nrTalao = nrTalao;
	}

	public int getNrInicial() {
		return nrInicial;
	}

	public void setNrInicial(int nrInicial) {
		this.nrInicial = nrInicial;
	}

	public int getNrFinal() {
		return nrFinal;
	}

	public void setNrFinal(int nrFinal) {
		this.nrFinal = nrFinal;
	}

	public int getCdAgente() {
		return cdAgente;
	}

	public void setCdAgente(int cdAgente) {
		this.cdAgente = cdAgente;
	}

	public long getDtEntrega() {
		return dtEntrega;
	}

	public void setDtEntrega(long dtEntrega) {
		this.dtEntrega = dtEntrega;
	}

	public long getDtDevolucao() {
		return dtDevolucao;
	}

	public void setDtDevolucao(long dtDevolucao) {
		this.dtDevolucao = dtDevolucao;
	}

	public int getStTalao() {
		return stTalao;
	}

	public void setStTalao(int stTalao) {
		this.stTalao = stTalao;
	}

	public int getTpTalao() {
		return tpTalao;
	}

	public void setTpTalao(int tpTalao) {
		this.tpTalao = tpTalao;
	}

	public String getSgTalao() {
		return sgTalao;
	}

	public void setSgTalao(String sgTalao) {
		this.sgTalao = sgTalao;
	}

	public int getNrUltimoAit() {
		return nrUltimoAit;
	}

	public void setNrUltimoAit(int nrUltimoAit) {
		this.nrUltimoAit = nrUltimoAit;
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
