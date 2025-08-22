package com.tivic.manager.mob.ait.relatorios;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RelatorioAitCompetenciaEstadualSearch {
	
	private int ctMovimento;
	private String dtInicialInfracao;
	private String dtFinalInfracao;
	private int tpCompetencia;

	public int getCtMovimento() {
		return ctMovimento;
	}

	public void setCtMovimento(int ctMovimento) {
		this.ctMovimento = ctMovimento;
	}

	public String getDtInicialInfracao() {
		return dtInicialInfracao;
	}

	public void setDtInicialInfracao(String dtInicialInfracao) {
		this.dtInicialInfracao = dtInicialInfracao;
	}

	public String getDtFinalInfracao() {
		return dtFinalInfracao;
	}

	public void setDtFinalInfracao(String dtFinalInfracao) {
		this.dtFinalInfracao = dtFinalInfracao;
	}

	public int getTpCompetencia() {
		return tpCompetencia;
	}

	public void setTpCompetencia(int tpCompetencia) {
		this.tpCompetencia = tpCompetencia;
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
