package com.tivic.manager.ptc.protocolosv3.processos;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class ProcessosSearchDTO {

	private int cdAit;
	private String idAit;
	private String nmTipoDocumento;
	private String nmFase;
	private int cdDocumento;
	private String nrDocumento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtMovimento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtProtocolo;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPrazo;
	private String nmSituacaoDocumento;
	private int cdSituacaoDocumento;
	private int cdTipoDocumento;
	private int tpStatus;
	private int cdApresentacaoCondutor;
	private int tpDocumento;
	private int publicacao;
	private int cancelamentoPublicacao;
	private int stAtual;
	
	public int getCdAit() {
		return cdAit;
	}
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	public String getIdAit() {
		return idAit;
	}
	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	public String getNmTipoDocumento() {
		return nmTipoDocumento;
	}
	public void setNmTipoDocumento(String nmTipoDocumento) {
		this.nmTipoDocumento = nmTipoDocumento;
	}
	public String getNmFase() {
		return nmFase;
	}
	public void setNmFase(String nmFase) {
		this.nmFase = nmFase;
	}
	public int getCdDocumento() {
		return cdDocumento;
	}
	public void setCdDocumento(int cdDocumento) {
		this.cdDocumento = cdDocumento;
	}
	public String getNrDocumento() {
		return nrDocumento;
	}
	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}
	public GregorianCalendar getDtMovimento() {
		return dtMovimento;
	}
	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
	}	
	public GregorianCalendar getDtProtocolo() {
		return dtProtocolo;
	}
	public void setDtProtocolo(GregorianCalendar dtProtocolo) {
		this.dtProtocolo = dtProtocolo;
	}
	public String getNmSituacaoDocumento() {
		return nmSituacaoDocumento;
	}
	public void setNmSituacaoDocumento(String nmSituacaoDocumento) {
		this.nmSituacaoDocumento = nmSituacaoDocumento;
	}	
	public int getCdSituacaoDocumento() {
		return cdSituacaoDocumento;
	}
	public void setCdSituacaoDocumento(int cdSituacaoDocumento) {
		this.cdSituacaoDocumento = cdSituacaoDocumento;
	}
	public int getCdTipoDocumento() {
		return cdTipoDocumento;
	}
	public void setCdTipoDocumento(int cdTipoDocumento) {
		this.cdTipoDocumento = cdTipoDocumento;
	}
	public int getTpStatus() {
		return tpStatus;
	}
	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}
	public int getCdApresentacaoCondutor() {
		return cdApresentacaoCondutor;
	}
	public void setCdApresentacaoCondutor(int cdApresentacaoCondutor) {
		this.cdApresentacaoCondutor = cdApresentacaoCondutor;
	}
	public int getPublicacao() {
		return publicacao;
	}
	public void setPublicacao(int publicacao) {
		this.publicacao = publicacao;
	}
	public int getCancelamentoPublicacao() {
		return cancelamentoPublicacao;
	}
	public void setCancelamentoPublicacao(int cancelamentoPublicacao) {
		this.cancelamentoPublicacao = cancelamentoPublicacao;
	}
	public int getStAtual() {
		return stAtual;
	}
	public void setStAtual(int stAtual) {
		this.stAtual = stAtual;
	}
	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}
	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
	}
	@Override
	 public String toString() {
       try {
           return new ObjectMapper().writeValueAsString(this);
       } catch (JsonProcessingException e) {
           return "Não foi possível serializar o objeto informado";
       }
   }
	public int getTpDocumento() {
		return tpDocumento;
	}
	public void setTpDocumento(int tpDocumento) {
		this.tpDocumento = tpDocumento;
	}
	
}
