package com.tivic.manager.ptc.protocolosv3.situacaodocumento;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SituacaoDocumentoDTO {
	private String nmStatusProtocolo;
	private String idAta;
	private int cdDocumento;
	private String txtOcorrencia;
	
	public String getIdAta() {
		return idAta;
	}
	public void setIdAta(String idAta) {
		this.idAta = idAta;
	}
	public int getCdDocumento() {
		return cdDocumento;
	}
	public void setCdDocumento(int cdDocumento) {
		this.cdDocumento = cdDocumento;
	}
	
	public String getNmStatusProtocolo() {
		return nmStatusProtocolo;
	}
	public void setNmStatusProtocolo(String nmStatusProtocolo) {
		this.nmStatusProtocolo = nmStatusProtocolo;
	}
	
	public String getTxtOcorrencia() {
		return txtOcorrencia;
	}
	
	public void setTxtOcorrencia(String txtOcorrencia) {
		this.txtOcorrencia = txtOcorrencia;
	}
	
	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
    }
}
