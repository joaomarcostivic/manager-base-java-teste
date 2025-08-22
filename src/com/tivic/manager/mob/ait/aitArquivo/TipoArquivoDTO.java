package com.tivic.manager.mob.ait.aitArquivo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TipoArquivoDTO {
	
	private int cdArquivo;
	private String nmDocumento;
	private String nmArquivo;
	private String dsArquivo;
	private String nmTipoArquivo;
	
	public int getCdArquivo() {
		return cdArquivo;
	}
	public void setCdArquivo(int cdArquivo) {
		this.cdArquivo = cdArquivo;
	}
	public String getNmDocumento() {
		return nmDocumento;
	}
	public void setNmDocumento(String nmDocumento) {
		this.nmDocumento = nmDocumento;
	}
	public String getNmArquivo() {
		return nmArquivo;
	}
	public void setNmArquivo(String nmArquivo) {
		this.nmArquivo = nmArquivo;
	}
	public String getDsArquivo() {
		return dsArquivo;
	}
	public void setDsArquivo(String dsArquivo) {
		this.dsArquivo = dsArquivo;
	}
	public String getNmTipoArquivo() {
		return nmTipoArquivo;
	}
	public void setNmTipoArquivo(String nmTipoArquivo) {
		this.nmTipoArquivo = nmTipoArquivo;
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