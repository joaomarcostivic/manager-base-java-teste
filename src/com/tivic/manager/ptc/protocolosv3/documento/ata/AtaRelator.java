package com.tivic.manager.ptc.protocolosv3.documento.ata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AtaRelator {
	private int cdAta;
	private int cdPessoa;
	
	public AtaRelator() {}
	
	public AtaRelator(int cdAta, int cdPessoa) {
		this.cdAta = cdAta;
		this.cdPessoa = cdPessoa;
	}

	public int getCdAta() {
		return cdAta;
	}

	public void setCdAta(int cdAta) {
		this.cdAta = cdAta;
	}

	public int getCdPessoa() {
		return cdPessoa;
	}

	public void setCdPessoa(int cdPessoa) {
		this.cdPessoa = cdPessoa;
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
