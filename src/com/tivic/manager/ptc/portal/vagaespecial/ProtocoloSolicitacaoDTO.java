package com.tivic.manager.ptc.portal.vagaespecial;

import java.util.GregorianCalendar;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.Arquivo;

public class ProtocoloSolicitacaoDTO {
	private int cdApresentacaoCondutor;
	private String nmTipoDocumento;
	private String nmFase;
	private int cdDocumento;
	private String nrDocumento;
	private GregorianCalendar dtMovimento;
	private GregorianCalendar dtProtocolo;
	private GregorianCalendar dtPrazo;
	private String nmSituacaoDocumento;
	private int cdTipoDocumento;
	private int cdFase;
	private int tpStatus;
	private int cdSituacaoDocumento;
	private int tpDocumento;
	private String nrCpf;
	private String nmEmail;
	private String nrTelefone1;
	private String nmPessoa;
	private String txtOcorrencia;
	private List<Arquivo> anexos;
	
	public int getCdApresentacaoCondutor() {
		return cdApresentacaoCondutor;
	}
	public void setCdApresentacaoCondutor(int cdApresentacaoCondutor) {
		this.cdApresentacaoCondutor = cdApresentacaoCondutor;
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
	public GregorianCalendar getDtPrazo() {
		return dtPrazo;
	}
	public void setDtPrazo(GregorianCalendar dtPrazo) {
		this.dtPrazo = dtPrazo;
	}
	public String getNmSituacaoDocumento() {
		return nmSituacaoDocumento;
	}
	public void setNmSituacaoDocumento(String nmSituacaoDocumento) {
		this.nmSituacaoDocumento = nmSituacaoDocumento;
	}
	public int getCdTipoDocumento() {
		return cdTipoDocumento;
	}
	public void setCdTipoDocumento(int cdTipoDocumento) {
		this.cdTipoDocumento = cdTipoDocumento;
	}
	public int getCdFase() {
		return cdFase;
	}
	public void setCdFase(int cdFase) {
		this.cdFase = cdFase;
	}
	public int getTpStatus() {
		return tpStatus;
	}
	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}
	public int getCdSituacaoDocumento() {
		return cdSituacaoDocumento;
	}
	public void setCdSituacaoDocumento(int cdSituacaoDocumento) {
		this.cdSituacaoDocumento = cdSituacaoDocumento;
	}
	public int getTpDocumento() {
		return tpDocumento;
	}
	public void setTpDocumento(int tpDocumento) {
		this.tpDocumento = tpDocumento;
	}
	public String getNrCpf() {
		return nrCpf;
	}
	public void setNrCpf(String nrCpf) {
		this.nrCpf = nrCpf;
	}
	public String getNmEmail() {
		return nmEmail;
	}
	public void setNmEmail(String nmEmail) {
		this.nmEmail = nmEmail;
	}
	public String getNrTelefone1() {
		return nrTelefone1;
	}
	public void setNrTelefone1(String nrTelefone1) {
		this.nrTelefone1 = nrTelefone1;
	}
	public String getNmPessoa() {
		return nmPessoa;
	}
	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}
	public String getTxtOcorrencia() {
		return txtOcorrencia;
	}
	public void setTxtOcorrencia(String txtOcorrencia) {
		this.txtOcorrencia = txtOcorrencia;
	}
	public List<Arquivo> getAnexos() {
		return anexos;
	}
	public void setAnexos(List<Arquivo> anexos) {
		this.anexos = anexos;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
}
