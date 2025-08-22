package com.tivic.manager.mob.orgaoaquivo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrgaoArquivo {
	private int cdOrgaoArquivo;
	private int cdOrgao;
	private int cdArquivo;
	private int tpArquivoDocumento;
	
	public int getCdOrgaoArquivo() {
		return cdOrgaoArquivo;
	}
	public void setCdOrgaoArquivo(int cdOrgaoArquivo) {
		this.cdOrgaoArquivo = cdOrgaoArquivo;
	}
	public int getCdOrgao() {
		return cdOrgao;
	}
	public void setCdOrgao(int cdOrgao) {
		this.cdOrgao = cdOrgao;
	}
	public int getCdArquivo() {
		return cdArquivo;
	}
	public void setCdArquivo(int cdArquivo) {
		this.cdArquivo = cdArquivo;
	}
	public int getTpArquivoDocumento() {
		return tpArquivoDocumento;
	}
	public void setTpArquivoDocumento(int tpArquivoDocumento) {
		this.tpArquivoDocumento = tpArquivoDocumento;
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
