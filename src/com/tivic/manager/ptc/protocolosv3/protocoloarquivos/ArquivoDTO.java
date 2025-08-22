package com.tivic.manager.ptc.protocolosv3.protocoloarquivos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.Arquivo;

public class ArquivoDTO {
	private int cdDocumento;
	private Arquivo arquivo;
	
	public ArquivoDTO() { }

	public ArquivoDTO(int cdDocumento, Arquivo arquivo) {
		setArquivo(arquivo);
		setCdDocumento(cdDocumento);
	}
	
	public int getCdDocumento() {
		return cdDocumento;
	}

	public void setCdDocumento(int cdDocumento) {
		this.cdDocumento = cdDocumento;
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
	
	public Object clone() {
		return new ArquivoDTO(getCdDocumento(), getArquivo());
	}
}
