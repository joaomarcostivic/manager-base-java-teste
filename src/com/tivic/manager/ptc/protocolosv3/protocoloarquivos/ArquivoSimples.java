package com.tivic.manager.ptc.protocolosv3.protocoloarquivos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ArquivoSimples {

	private int cdArquivo;
	private String nmArquivo;
	private String nmDocumento;
	private String nmTipoArquivo;
	private String dsArquivo;
	
	public ArquivoSimples(){ }
	
	public ArquivoSimples(int cdArquivo,
			String nmArquivo,
			String nmDocumento,
			String nmTipoArquivo,
			String dsArquivo) {
		setCdArquivo(cdArquivo);
		setNmArquivo(nmArquivo);
		setNmDocumento(nmDocumento);
		setNmTipoArquivo(nmTipoArquivo);
		setDsArquivo(dsArquivo);
	}
	
	public String getDsArquivo() {
		return dsArquivo;
	}

	public void setDsArquivo(String dsArquivo) {
		this.dsArquivo = dsArquivo;
	}

	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setNmArquivo(String nmArquivo){
		this.nmArquivo=nmArquivo;
	}
	public String getNmArquivo(){
		return this.nmArquivo;
	}
	public void setNmDocumento(String nmDocumento){
		this.nmDocumento=nmDocumento;
	}
	public String getNmDocumento(){
		return this.nmDocumento;
	}

	public String getNmTipoArquivo() {
		return nmTipoArquivo;
	}

	public void setNmTipoArquivo(String nmTipoArquivo) {
		this.nmTipoArquivo = nmTipoArquivo;
	}

	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
    }
	
	public Object clone() {
		return new ArquivoSimples(getCdArquivo(), getNmArquivo(), getNmDocumento(), getNmTipoArquivo(), getDsArquivo());
	}

}
