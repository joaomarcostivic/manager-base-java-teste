package com.tivic.manager.mob.lote.impressao.viaunica.nic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NicSimplificadaDTO {
	private int cdAit;
	private int cdAitOrigem;
	private byte[] arquivo;
	private int lgEnviado;
	private int cdLoteImpressao;
	
	public int getCdAit() {
		return cdAit;
	}
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	public byte[] getArquivo() {
		return arquivo;
	}
	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}
	public int getLgEnviado() {
		return lgEnviado;
	}
	public void setLgEnviado(int lgEnviado) {
		this.lgEnviado = lgEnviado;
	}
	public int getCdAitOrigem() {
		return cdAitOrigem;
	}
	public void setCdAitOrigem(int cdAitOrigem) {
		this.cdAitOrigem = cdAitOrigem;
	}
	public int getCdLoteImpressao() {
		return cdLoteImpressao;
	}
	public void setCdLoteImpressao(int cdLoteImpressao) {
		this.cdLoteImpressao = cdLoteImpressao;
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
