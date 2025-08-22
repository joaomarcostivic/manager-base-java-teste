package com.tivic.manager.mob.lotes.dto.impressao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NipImpressaoDTO {
	
	private int cdAit;
	private byte[] arquivo;
	private NicSimplificadaDTO nicSimplificadaDTO;
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
	public NicSimplificadaDTO getNicSimplificadaDTO() {
		return nicSimplificadaDTO;
	}
	public void setNicSimplificadaDTO(NicSimplificadaDTO nicSimplificadaDTO) {
		this.nicSimplificadaDTO = nicSimplificadaDTO;
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
