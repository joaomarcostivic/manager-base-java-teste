package com.tivic.manager.ptc.protocolosv3.ocorrencia;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class ProtocoloOcorrenciaDTO {
	private int cdDocumento;
	private String nmTipoOcorrencia;
	private String txtOcorrencia;
	private String nmPessoa;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtOcorrencia;

	public int getCdDocumento() {
		return cdDocumento;
	}

	public void setCdDocumento(int cdDocumento) {
		this.cdDocumento = cdDocumento;
	}

	public String getNmTipoOcorrencia() {
		return nmTipoOcorrencia;
	}

	public void setNmTipoOcorrencia(String nmTipoOcorrencia) {
		this.nmTipoOcorrencia = nmTipoOcorrencia;
	}

	public String getTxtOcorrencia() {
		return txtOcorrencia;
	}

	public void setTxtOcorrencia(String txtOcorrencia) {
		this.txtOcorrencia = txtOcorrencia;
	}

	public String getNmPesssoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}

	public GregorianCalendar getDtOcorrencia() {
		return dtOcorrencia;
	}

	public void setDtOcorrencia(GregorianCalendar dtOcorrencia) {
		this.dtOcorrencia = dtOcorrencia;
	}
	
	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
    }
}
