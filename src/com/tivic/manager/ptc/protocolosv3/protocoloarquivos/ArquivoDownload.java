package com.tivic.manager.ptc.protocolosv3.protocoloarquivos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ArquivoDownload {
	private int cdArquivo;
	private String nmArquivo;
	private byte[] blbArquivo;
	
	public ArquivoDownload() { }

	public ArquivoDownload(int cdArquivo, byte[] blbArquivo, String nmArquivo) {
		setCdArquivo(cdArquivo);
		setBlbArquivo(blbArquivo);
		setNmArquivo(nmArquivo);
	}
	
	public int getCdArquivo() {
		return cdArquivo;
	}

	public void setCdArquivo(int cdArquivo) {
		this.cdArquivo = cdArquivo;
	}

	public byte[] getBlbArquivo() {
		return blbArquivo;
	}

	public void setBlbArquivo(byte[] blbArquivo) {
		this.blbArquivo = blbArquivo;
	}
	
	public String getNmArquivo() {
		return nmArquivo;
	}

	public void setNmArquivo(String nmDocumento) {
		this.nmArquivo = nmDocumento;
	}

	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
    }
	
	public Object clone() {
		return new ArquivoDownload(getCdArquivo(), getBlbArquivo(), getNmArquivo());
	}
}
