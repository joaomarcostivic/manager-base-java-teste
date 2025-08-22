package com.tivic.manager.ptc.protocolosv3.resultado;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class ResultadoDTO {
	private Integer cdAit;
	private Integer cdMovimento;
	private Integer cdDocumento;
	private Integer cdUsuario;
	private int cdOcorrencia;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtOcorrencia;
	private String txtOcorrencia;
	private String dsAssunto;
	private Integer cdTipoOcorrencia;
	private Integer cdSituacaoDocumento;
	private Integer tpStatus;
	private int tpConsistencia;
	
	public ResultadoDTO() {}

	public ResultadoDTO(Integer cdAit, Integer cdMovimento, Integer cdDocumento, Integer cdTipoOcorrencia, Integer cdUsuario,
			GregorianCalendar dtOcorrencia, String txtOcorrencia, String dsAssunto, Integer cdSituacaoDocumento,
			Integer tpStatus, int tpConsistencia, int cdOcorrencia) {
		this.cdAit = cdAit;
		this.cdMovimento = cdMovimento;
		this.cdDocumento = cdDocumento;
		this.cdTipoOcorrencia = cdTipoOcorrencia;
		this.cdUsuario = cdUsuario;
		this.dtOcorrencia = dtOcorrencia;
		this.txtOcorrencia = txtOcorrencia;
		this.dsAssunto = dsAssunto;
		this.cdSituacaoDocumento = cdSituacaoDocumento;
		this.tpStatus = tpStatus;
		this.tpConsistencia = tpConsistencia;
		this.cdOcorrencia = cdOcorrencia;
	}

	public Integer getCdAit() {
		return cdAit;
	}

	public void setCdAit(Integer cdAit) {
		this.cdAit = cdAit;
	}

	public Integer getCdMovimento() {
		return cdMovimento;
	}

	public void setCdMovimento(Integer cdMovimento) {
		this.cdMovimento = cdMovimento;
	}

	public Integer getCdDocumento() {
		return cdDocumento;
	}

	public void setCdDocumento(Integer cdDocumento) {
		this.cdDocumento = cdDocumento;
	}

	public Integer getCdTipoOcorrencia() {
		return cdTipoOcorrencia;
	}

	public void setCdTipoOcorrencia(Integer cdTipoOcorrencia) {
		this.cdTipoOcorrencia = cdTipoOcorrencia;
	}

	public Integer getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(Integer cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public GregorianCalendar getDtOcorrencia() {
		return dtOcorrencia;
	}

	public void setDtOcorrencia(GregorianCalendar dtOcorrencia) {
		this.dtOcorrencia = dtOcorrencia;
	}

	public String getTxtOcorrencia() {
		return txtOcorrencia;
	}

	public void setTxtOcorrencia(String txtOcorrencia) {
		this.txtOcorrencia = txtOcorrencia;
	}

	public Integer getCdSituacaoDocumento() {
		return cdSituacaoDocumento;
	}

	public void setCdSituacaoDocumento(Integer cdSituacaoDocumento) {
		this.cdSituacaoDocumento = cdSituacaoDocumento;
	}

	public Integer getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(Integer tpStatus) {
		this.tpStatus = tpStatus;
	}
	
	public String getDsAssunto() {
		return dsAssunto;
	}

	public void setDsAssunto(String dsAssunto) {
		this.dsAssunto = dsAssunto;
	}
	public int getTpConsistencia() {
		return tpConsistencia;
	}
	public void setTpConsistencia(int tpConsistencia) {
		this.tpConsistencia = tpConsistencia;
	}

	public int getCdOcorrencia() {
		return cdOcorrencia;
	}

	public void setCdOcorrencia(int cdOcorrencia) {
		this.cdOcorrencia = cdOcorrencia;
	}

	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
    }
	
}