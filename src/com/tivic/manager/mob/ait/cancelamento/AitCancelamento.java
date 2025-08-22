package com.tivic.manager.mob.ait.cancelamento;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AitCancelamento {
	private int cdArquivo;
	private int cdMovimento;
	private int cdAit;
	
	public AitCancelamento() {}
	
	public AitCancelamento(int cdArquivo, int cdMovimento, int cdAit) {
		setCdArquivo(cdArquivo);
		setCdMovimento(cdMovimento);
		setCdAit(cdAit);
	}
	
	public int getCdArquivo() {
		return cdArquivo;
	}
	public void setCdArquivo(int cdArquivo) {
		this.cdArquivo = cdArquivo;
	}
	public int getCdMovimento() {
		return cdMovimento;
	}
	public void setCdMovimento(int cdMovimento) {
		this.cdMovimento = cdMovimento;
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
        } 
        catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
	}
	
}
