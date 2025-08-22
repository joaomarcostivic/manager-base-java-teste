package com.tivic.manager.mob.aitmovimento.cancelamentomovimentos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.Arquivo;

public class ArquivoAitDTO {
	private int cdAit;
	private int cdMovimento;
	private Arquivo arquivo;
	
	public int getCdAit() {
		return cdAit;
	}
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	public int getCdMovimento() {
		return cdMovimento;
	}
	public void setCdMovimento(int cdMovimento) {
		this.cdMovimento = cdMovimento;
	}
	public Arquivo getArquivo() {
		return arquivo;
	}
	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}
	
	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
    }

}
