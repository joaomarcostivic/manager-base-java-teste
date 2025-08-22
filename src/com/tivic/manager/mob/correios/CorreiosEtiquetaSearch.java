package com.tivic.manager.mob.correios;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CorreiosEtiquetaSearch {
		
	private String nmArquivo;
	private int cdTipoArquivo;
	private String dtCriacaoInicial;
	private String dtCriacaoFinal;
	private int nrRegistro;
	private int page;
	private int limit;
	
	public String getNmArquivo() {
		return nmArquivo;
	}
	
	public void setNmArquivo(String nmArquivo) {
		this.nmArquivo = nmArquivo;
	}
	
	public int getCdTipoArquivo() {
		return cdTipoArquivo;
	}
	
	public void setCdTipoArquivo(int cdTipoArquivo) {
		this.cdTipoArquivo = cdTipoArquivo;
	}
	
	public String getDtCriacaoInicial() {
		return dtCriacaoInicial;
	}
	
	public void setDtCriacaoInicial(String dtCriacaoInicial) {
		this.dtCriacaoInicial = dtCriacaoInicial;
	}
	
	public String getDtCriacaoFinal() {
		return dtCriacaoFinal;
	}
	
	public void setDtCriacaoFinal(String dtCriacaoFinal) {
		this.dtCriacaoFinal = dtCriacaoFinal;
	}
	
	public int getNrRegistro() {
		return nrRegistro;
	}
	
	public void setNrRegistro(int nrRegistro) {
		this.nrRegistro = nrRegistro;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getLimit() {
		return limit;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
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
