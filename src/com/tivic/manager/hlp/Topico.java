package com.tivic.manager.hlp;

import java.util.GregorianCalendar;

public class Topico {

	private int cdTopico;
	private int cdTopicoSuperior;
	private int cdUsuarioCriacao;
	private int cdUsuarioAlteracao;
	private int cdUsuarioRevisao;
	private String nmTitulo;
	private String nmSubtitulo;
	private String txtTexto;
	private String txtResumo;
	private GregorianCalendar dtCriacao;
	private GregorianCalendar dtAlteracao;
	private GregorianCalendar dtRevisao;
	private String idReferencia;
	private int lgRevisado;
	private int nrOrdem;

	public Topico(int cdTopico,
			int cdTopicoSuperior,
			int cdUsuarioCriacao,
			int cdUsuarioAlteracao,
			int cdUsuarioRevisao,
			String nmTitulo,
			String nmSubtitulo,
			String txtTexto,
			String txtResumo,
			GregorianCalendar dtCriacao,
			GregorianCalendar dtAlteracao,
			GregorianCalendar dtRevisao,
			String idReferencia,
			int lgRevisado,
			int nrOrdem){
		setCdTopico(cdTopico);
		setCdTopicoSuperior(cdTopicoSuperior);
		setCdUsuarioCriacao(cdUsuarioCriacao);
		setCdUsuarioAlteracao(cdUsuarioAlteracao);
		setCdUsuarioRevisao(cdUsuarioRevisao);
		setNmTitulo(nmTitulo);
		setNmSubtitulo(nmSubtitulo);
		setTxtTexto(txtTexto);
		setTxtResumo(txtResumo);
		setDtCriacao(dtCriacao);
		setDtAlteracao(dtAlteracao);
		setDtRevisao(dtRevisao);
		setIdReferencia(idReferencia);
		setLgRevisado(lgRevisado);
		setNrOrdem(nrOrdem);
	}
	public void setCdTopico(int cdTopico){
		this.cdTopico=cdTopico;
	}
	public int getCdTopico(){
		return this.cdTopico;
	}
	public void setCdTopicoSuperior(int cdTopicoSuperior){
		this.cdTopicoSuperior=cdTopicoSuperior;
	}
	public int getCdTopicoSuperior(){
		return this.cdTopicoSuperior;
	}
	public void setCdUsuarioCriacao(int cdUsuarioCriacao){
		this.cdUsuarioCriacao=cdUsuarioCriacao;
	}
	public int getCdUsuarioCriacao(){
		return this.cdUsuarioCriacao;
	}
	public void setCdUsuarioAlteracao(int cdUsuarioAlteracao){
		this.cdUsuarioAlteracao=cdUsuarioAlteracao;
	}
	public int getCdUsuarioAlteracao(){
		return this.cdUsuarioAlteracao;
	}
	public void setCdUsuarioRevisao(int cdUsuarioRevisao){
		this.cdUsuarioRevisao=cdUsuarioRevisao;
	}
	public int getCdUsuarioRevisao(){
		return this.cdUsuarioRevisao;
	}
	public void setNmTitulo(String nmTitulo){
		this.nmTitulo=nmTitulo;
	}
	public String getNmTitulo(){
		return this.nmTitulo;
	}
	public void setNmSubtitulo(String nmSubtitulo){
		this.nmSubtitulo=nmSubtitulo;
	}
	public String getNmSubtitulo(){
		return this.nmSubtitulo;
	}
	public void setTxtTexto(String txtTexto){
		this.txtTexto=txtTexto;
	}
	public String getTxtTexto(){
		return this.txtTexto;
	}
	public void setTxtResumo(String txtResumo){
		this.txtResumo=txtResumo;
	}
	public String getTxtResumo(){
		return this.txtResumo;
	}
	public void setDtCriacao(GregorianCalendar dtCriacao){
		this.dtCriacao=dtCriacao;
	}
	public GregorianCalendar getDtCriacao(){
		return this.dtCriacao;
	}
	public void setDtAlteracao(GregorianCalendar dtAlteracao){
		this.dtAlteracao=dtAlteracao;
	}
	public GregorianCalendar getDtAlteracao(){
		return this.dtAlteracao;
	}
	public void setDtRevisao(GregorianCalendar dtRevisao){
		this.dtRevisao=dtRevisao;
	}
	public GregorianCalendar getDtRevisao(){
		return this.dtRevisao;
	}
	public void setIdReferencia(String idReferencia){
		this.idReferencia=idReferencia;
	}
	public String getIdReferencia(){
		return this.idReferencia;
	}
	public void setLgRevisado(int lgRevisado){
		this.lgRevisado=lgRevisado;
	}
	public int getLgRevisado(){
		return this.lgRevisado;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTopico: " +  getCdTopico();
		valueToString += ", cdTopicoSuperior: " +  getCdTopicoSuperior();
		valueToString += ", cdUsuarioCriacao: " +  getCdUsuarioCriacao();
		valueToString += ", cdUsuarioAlteracao: " +  getCdUsuarioAlteracao();
		valueToString += ", cdUsuarioRevisao: " +  getCdUsuarioRevisao();
		valueToString += ", nmTitulo: " +  getNmTitulo();
		valueToString += ", nmSubtitulo: " +  getNmSubtitulo();
		valueToString += ", txtTexto: " +  getTxtTexto();
		valueToString += ", txtResumo: " +  getTxtResumo();
		valueToString += ", dtCriacao: " +  sol.util.Util.formatDateTime(getDtCriacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtAlteracao: " +  sol.util.Util.formatDateTime(getDtAlteracao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtRevisao: " +  sol.util.Util.formatDateTime(getDtRevisao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idReferencia: " +  getIdReferencia();
		valueToString += ", lgRevisado: " +  getLgRevisado();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Topico(getCdTopico(),
			getCdTopicoSuperior(),
			getCdUsuarioCriacao(),
			getCdUsuarioAlteracao(),
			getCdUsuarioRevisao(),
			getNmTitulo(),
			getNmSubtitulo(),
			getTxtTexto(),
			getTxtResumo(),
			getDtCriacao()==null ? null : (GregorianCalendar)getDtCriacao().clone(),
			getDtAlteracao()==null ? null : (GregorianCalendar)getDtAlteracao().clone(),
			getDtRevisao()==null ? null : (GregorianCalendar)getDtRevisao().clone(),
			getIdReferencia(),
			getLgRevisado(),
			getNrOrdem());
	}

}