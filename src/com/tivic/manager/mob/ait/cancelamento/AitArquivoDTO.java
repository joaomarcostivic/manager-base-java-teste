package com.tivic.manager.mob.ait.cancelamento;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AitArquivoDTO {
	private int cdArquivo;
	private String nmArquivo;
	private String nmDocumento;
	
	public int getCdArquivo() {
		return cdArquivo;
	}
	
	public void setCdArquivo(int cdArquivo) {
		this.cdArquivo = cdArquivo;
	}
	
	public String getNmArquivo() {
		return nmArquivo;
	}
	
	public void setNmArquivo(String nmArquivo) {
		this.nmArquivo = nmArquivo;
	}
	
	public String getNmDocumento() {
		return nmDocumento;
	}
	
	public void setNmDocumento(String nmDocumento) {
		this.nmDocumento = nmDocumento;
	}
	
	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
    }
}
