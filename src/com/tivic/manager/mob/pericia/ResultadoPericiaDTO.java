package com.tivic.manager.mob.pericia;
 
import java.util.GregorianCalendar;

public class ResultadoPericiaDTO {
	GregorianCalendar dtRealizacao;
	GregorianCalendar dtAlteracao;
	int cdDocumento;
	String nmSolicitante;
	String nrDocumento;
	String txtObservacao;
	GregorianCalendar dtValidade;
	int lgAcompanhante; 
	String nmSituacaoDocumento;
	String nmMedico;
	
	public ResultadoPericiaDTO() {
		super();
	}
	
	public ResultadoPericiaDTO(GregorianCalendar dtRealizacao,	GregorianCalendar dtAlteracao,	int cdDocumento, String nmSolicitante, String nrDocumento, String txtObservacao, GregorianCalendar dtValidade,	int lgAcompanhante, String nmSituacaoDocumento, 	String nmMedico) {
		 this.dtRealizacao = dtRealizacao;
		 this.dtAlteracao = dtAlteracao;
		 this.cdDocumento = cdDocumento;
		 this.nmSolicitante = nmSolicitante;
		 this.nrDocumento = nrDocumento;
		 this.txtObservacao = txtObservacao;
		 this.dtValidade = dtValidade;
		 this.lgAcompanhante = lgAcompanhante; 
		 this.nmSituacaoDocumento = nmSituacaoDocumento;
		 this.nmMedico = nmMedico;
	}

	public GregorianCalendar getDtRealizacao() {
		return dtRealizacao;
	}

	public void setDtRealizacao(GregorianCalendar dtRealizacao) {
		this.dtRealizacao = dtRealizacao;
	}

	public GregorianCalendar getDtAlteracao() {
		return dtAlteracao;
	}

	public void setDtAlteracao(GregorianCalendar dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}

	public int getCdDocumento() {
		return cdDocumento;
	}

	public void setCdDocumento(int cdDocumento) {
		this.cdDocumento = cdDocumento;
	}

	public String getNmSolicitante() {
		return nmSolicitante;
	}

	public void setNmSolicitante(String nmSolicitante) {
		this.nmSolicitante = nmSolicitante;
	}

	public String getNrDocumento() {
		return nrDocumento;
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public String getTxtObservacao() {
		return txtObservacao;
	}

	public void setTxtObservacao(String txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	public GregorianCalendar getDtValidade() {
		return dtValidade;
	}

	public void setDtValidade(GregorianCalendar dtValidade) {
		this.dtValidade = dtValidade;
	}

	public int getLgAcompanhante() {
		return lgAcompanhante;
	}

	public void setLgAcompanhante(int lgAcompanhante) {
		this.lgAcompanhante = lgAcompanhante;
	}

	public String getNmSituacaoDocumento() {
		return nmSituacaoDocumento;
	}

	public void setNmSituacaoDocumento(String nmSituacaoDocumento) {
		this.nmSituacaoDocumento = nmSituacaoDocumento;
	}

	public String getNmMedico() {
		return nmMedico;
	}

	public void setNmMedico(String nmMedico) {
		this.nmMedico = nmMedico;
	}
}
