package com.tivic.manager.mob.edat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TermosECondicoes {
	
	private int cdTermo;
	private String dsTermo;
	private int nrPosicao;
	
	public TermosECondicoes() {
    }

	public TermosECondicoes(int cdTermo, String dsTermo, int nrPosicao) {
        this.cdTermo = cdTermo;
        this.dsTermo = dsTermo;
        this.nrPosicao = nrPosicao;
    }

	public int getCdTermo() {
		return cdTermo;
	}

	public void setCdTermo(int cdTermo) {
		this.cdTermo = cdTermo;
	}

	public String getDsTermo() {
		return dsTermo;
	}

	public void setDsTermo(String dsTermo) {
		this.dsTermo = dsTermo;
	}
	
	public int getNrPosicao() {
		return nrPosicao;
	}
	
	public void setNrPosicao(int nrPosicao) {
		this.nrPosicao = nrPosicao;
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
