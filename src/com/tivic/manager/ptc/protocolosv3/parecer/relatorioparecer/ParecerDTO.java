package com.tivic.manager.ptc.protocolosv3.parecer.relatorioparecer;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class ParecerDTO {

	private String idAit;
	private String nmRequerente;
	private String nrPlaca;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtProtocolo;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtOcorrencia;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtVencimento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPrazoDefesa;
	private String dsAssunto;
	private String txtOcorrencia;
	private Integer cdSituacaoDocumento;
	private String nmSituacaoDocumento;
	private int tpConsistencia;
	private int cdTipoDocumento;
	private int cdAta;
	private String nrDocumento;
	
	
	public int getCdTipoDocumento() {
		return cdTipoDocumento;
	}
	public void setCdTipoDocumento(int cdTipoDocumento) {
		this.cdTipoDocumento = cdTipoDocumento;
	}
	public String getIdAit() {
		return idAit;
	}
	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	public String getNmRequerente() {
		return nmRequerente;
	}
	public void setNmRequerente(String nmRequerente) {
		this.nmRequerente = nmRequerente;
	}
	public String getNrPlaca() {
		return nrPlaca;
	}
	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}
	public GregorianCalendar getDtProtocolo() {
		return dtProtocolo;
	}
	public void setDtProtocolo(GregorianCalendar dtProtocolo) {
		this.dtProtocolo = dtProtocolo;
	}
	public GregorianCalendar getDtOcorrencia() {
		return dtOcorrencia;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia) {
		this.dtOcorrencia = dtOcorrencia;
	}
	public String getDsAssunto() {
		return dsAssunto;
	}
	public void setDsAssunto(String dsAssunto) {
		this.dsAssunto = dsAssunto;
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
	public String getNmSituacaoDocumento() {
		return nmSituacaoDocumento;
	}
	public void setNmSituacaoDocumento(String nmSituacaoDocumento) {
		this.nmSituacaoDocumento = nmSituacaoDocumento;
	}
	public GregorianCalendar getDtVencimento() {
		return dtVencimento;
	}
	public void setDtVencimento(GregorianCalendar dtVencimento) {
		this.dtVencimento = dtVencimento;
	}
	public int getTpConsistencia() {
		return tpConsistencia;
	}
	public void setTpConsistencia(int tpConsistencia) {
		this.tpConsistencia = tpConsistencia;
	}
	public GregorianCalendar getDtPrazoDefesa() {
		return dtPrazoDefesa;
	}
	public void setDtPrazoDefesa(GregorianCalendar dtPrazoDefesa) {
		this.dtPrazoDefesa = dtPrazoDefesa;
	}
	
	public int getCdAta() {
		return cdAta;
	}
	public void setCdAta(int cdAta) {
		this.cdAta = cdAta;
	}
	
	public String getNrDocumento() {
		return nrDocumento;
	}
	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
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