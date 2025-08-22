package com.tivic.manager.mob.ait.sync.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InfracaoSyncDTO {

	private int cdInfracao;
	private String dsInfracao;
	private int nrPontuacao;
	private int nrCodDetran;
	private String nmNatureza;
	private String nrArtigo;
	private String nrParagrafo;
	private String nrInciso;
	private String nrAlinea;
	private int tpCompetencia;
	private int lgPrioritaria;
	private long dtFimVigencia;
	private Double vlInfracao;
	private int lgSuspensaoCnh;
	private int tpResponsabilidade;

	public int getCdInfracao() {
		return cdInfracao;
	}

	public void setCdInfracao(int cdInfracao) {
		this.cdInfracao = cdInfracao;
	}

	public String getDsInfracao() {
		return dsInfracao;
	}

	public void setDsInfracao(String dsInfracao) {
		this.dsInfracao = dsInfracao;
	}

	public int getNrPontuacao() {
		return nrPontuacao;
	}

	public void setNrPontuacao(int nrPontuacao) {
		this.nrPontuacao = nrPontuacao;
	}

	public int getNrCodDetran() {
		return nrCodDetran;
	}

	public void setNrCodDetran(int nrCodDetran) {
		this.nrCodDetran = nrCodDetran;
	}

	public String getNmNatureza() {
		return nmNatureza;
	}

	public void setNmNatureza(String nmNatureza) {
		this.nmNatureza = nmNatureza;
	}

	public String getNrArtigo() {
		return nrArtigo;
	}

	public void setNrArtigo(String nrArtigo) {
		this.nrArtigo = nrArtigo;
	}

	public String getNrParagrafo() {
		return nrParagrafo;
	}

	public void setNrParagrafo(String nrParagrafo) {
		this.nrParagrafo = nrParagrafo;
	}

	public String getNrInciso() {
		return nrInciso;
	}

	public void setNrInciso(String nrInciso) {
		this.nrInciso = nrInciso;
	}

	public String getNrAlinea() {
		return nrAlinea;
	}

	public void setNrAlinea(String nrAlinea) {
		this.nrAlinea = nrAlinea;
	}

	public int getTpCompetencia() {
		return tpCompetencia;
	}

	public void setTpCompetencia(int tpCompetencia) {
		this.tpCompetencia = tpCompetencia;
	}

	public int getLgPrioritaria() {
		return lgPrioritaria;
	}

	public void setLgPrioritaria(int lgPrioritaria) {
		this.lgPrioritaria = lgPrioritaria;
	}

	public long getDtFimVigencia() {
		return dtFimVigencia;
	}

	public void setDtFimVigencia(long dtFimVigencia) {
		this.dtFimVigencia = dtFimVigencia;
	}

	public Double getVlInfracao() {
		return vlInfracao;
	}

	public void setVlInfracao(Double vlInfracao) {
		this.vlInfracao = vlInfracao;
	}

	public int getLgSuspensaoCnh() {
		return lgSuspensaoCnh;
	}

	public void setLgSuspensaoCnh(int lgSuspensaoCnh) {
		this.lgSuspensaoCnh = lgSuspensaoCnh;
	}

	public int getTpResponsabilidade() {
		return tpResponsabilidade;
	}

	public void setTpResponsabilidade(int tpResponsabilidade) {
		this.tpResponsabilidade = tpResponsabilidade;
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
