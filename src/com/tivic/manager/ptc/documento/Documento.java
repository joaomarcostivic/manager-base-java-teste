package com.tivic.manager.ptc.documento;

import java.util.GregorianCalendar;

public class Documento {

	private int cdDocumento;
	private int cdUsuario;
	private GregorianCalendar dtProtocolo;
	private String txtObservacao;
	private String nrDocumento;
	private int cdTipoDocumento;
	private int cdFase;
	private int cdSituacaoDocumento;
	private String txtDocumento;
	private String nmRequerente;

	public Documento() { }

	public Documento(int cdDocumento,
			int cdUsuario,
			GregorianCalendar dtProtocolo,
			String txtObservacao,
			String nrDocumento,
			int cdTipoDocumento,
			int cdFase,
			int cdSituacaoDocumento,
			String txtDocumento,
			String nmRequerente) {
		setCdDocumento(cdDocumento);
		setCdUsuario(cdUsuario);
		setDtProtocolo(dtProtocolo);
		setTxtObservacao(txtObservacao);
		setNrDocumento(nrDocumento);
		setCdTipoDocumento(cdTipoDocumento);
		setCdFase(cdFase);
		setCdSituacaoDocumento(cdSituacaoDocumento);
		setTxtDocumento(txtDocumento);
		setNmRequerente(nmRequerente);
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setDtProtocolo(GregorianCalendar dtProtocolo){
		this.dtProtocolo=dtProtocolo;
		
	}
	public GregorianCalendar getDtProtocolo(){
		return this.dtProtocolo;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setNrDocumento(String nrDocumento){
		this.nrDocumento=nrDocumento;
	}
	public String getNrDocumento(){
		return this.nrDocumento;
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setCdFase(int cdFase){
		this.cdFase=cdFase;
	}
	public int getCdFase(){
		return this.cdFase;
	}
	public void setCdSituacaoDocumento(int cdSituacaoDocumento){
		this.cdSituacaoDocumento=cdSituacaoDocumento;
	}
	public int getCdSituacaoDocumento(){
		return this.cdSituacaoDocumento;
	}
	public void setTxtDocumento(String txtDocumento){
		this.txtDocumento=txtDocumento;
	}
	public String getTxtDocumento(){
		return this.txtDocumento;
	}
	public void setNmRequerente(String nmRequerente){
		this.nmRequerente=nmRequerente;
	}
	public String getNmRequerente(){
		return this.nmRequerente;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocumento: " +  getCdDocumento();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", dtProtocolo: " +  sol.util.Util.formatDateTime(getDtProtocolo(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", nrDocumento: " +  getNrDocumento();
		valueToString += ", cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", cdFase: " +  getCdFase();
		valueToString += ", cdSituacaoDocumento: " +  getCdSituacaoDocumento();
		valueToString += ", txtDocumento: " +  getTxtDocumento();
		valueToString += ", nmRequerente: " +  getNmRequerente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Documento(getCdDocumento(),
			getCdUsuario(),
			getDtProtocolo()==null ? null : (GregorianCalendar)getDtProtocolo().clone(),
			getTxtObservacao(),
			getNrDocumento(),
			getCdTipoDocumento(),
			getCdFase(),
			getCdSituacaoDocumento(),
			getTxtDocumento(),
			getNmRequerente());
	}

}