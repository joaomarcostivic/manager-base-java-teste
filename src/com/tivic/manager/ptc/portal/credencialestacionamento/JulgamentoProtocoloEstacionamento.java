package com.tivic.manager.ptc.portal.credencialestacionamento;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JulgamentoProtocoloEstacionamento {

	private int cdDocumento;
	private String nmTipoOcorrencia;
	private int cdUsuario;
	private String txtOcorrencia;

	public int getCdDocumento() {
		return cdDocumento;
	}

	public void setCdDocumento(int cdDocumento) {
		this.cdDocumento = cdDocumento;
	}

	public String getNmTipoOcorrencia() {
		return nmTipoOcorrencia;
	}

	public void setCdTipoOcorrencia(String nmTipoOcorrencia) {
		this.nmTipoOcorrencia = nmTipoOcorrencia;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public String getTxtOcorrencia() {
		return txtOcorrencia;
	}

	public void setTxtOcorrencia(String txtOcorrencia) {
		this.txtOcorrencia = txtOcorrencia;
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
