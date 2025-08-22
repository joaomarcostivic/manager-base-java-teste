package com.tivic.manager.mob.ait.aitArquivo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AitArquivo {

	private int cdArquivo;
	private int cdAit;
	
	public AitArquivo() {}
	
	public AitArquivo(int cdArquivo, int cdAit) {
		this.cdArquivo = cdArquivo;
		this.cdAit = cdAit;
	}
	
	public int getCdArquivo() {
		return cdArquivo;
	}
	public void setCdArquivo(int cdArquivo) {
		this.cdArquivo = cdArquivo;
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
        } catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
    }

}