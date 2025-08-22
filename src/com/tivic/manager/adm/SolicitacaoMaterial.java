package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class SolicitacaoMaterial {

	private int cdSolicitacaoMaterial;
	private int cdSetorSolicitante;
	private GregorianCalendar dtSolicitacao;
	private int stSolicitacaoMaterial;
	private String idSolicitacaoMaterial;
	private int cdDocumento;
	private String txtObservacao;
	private int cdSolicitante;
	private int cdEmpresa;

	public SolicitacaoMaterial(int cdSolicitacaoMaterial,
			int cdSetorSolicitante,
			GregorianCalendar dtSolicitacao,
			int stSolicitacaoMaterial,
			String idSolicitacaoMaterial,
			int cdDocumento,
			String txtObservacao,
			int cdSolicitante,
			int cdEmpresa){
		setCdSolicitacaoMaterial(cdSolicitacaoMaterial);
		setCdSetorSolicitante(cdSetorSolicitante);
		setDtSolicitacao(dtSolicitacao);
		setStSolicitacaoMaterial(stSolicitacaoMaterial);
		setIdSolicitacaoMaterial(idSolicitacaoMaterial);
		setCdDocumento(cdDocumento);
		setTxtObservacao(txtObservacao);
		setCdSolicitante(cdSolicitante);
		setCdEmpresa(cdEmpresa);
	}
	public void setCdSolicitacaoMaterial(int cdSolicitacaoMaterial){
		this.cdSolicitacaoMaterial=cdSolicitacaoMaterial;
	}
	public int getCdSolicitacaoMaterial(){
		return this.cdSolicitacaoMaterial;
	}
	public void setCdSetorSolicitante(int cdSetorSolicitante){
		this.cdSetorSolicitante=cdSetorSolicitante;
	}
	public int getCdSetorSolicitante(){
		return this.cdSetorSolicitante;
	}
	public void setDtSolicitacao(GregorianCalendar dtSolicitacao){
		this.dtSolicitacao=dtSolicitacao;
	}
	public GregorianCalendar getDtSolicitacao(){
		return this.dtSolicitacao;
	}
	public void setStSolicitacaoMaterial(int stSolicitacaoMaterial){
		this.stSolicitacaoMaterial=stSolicitacaoMaterial;
	}
	public int getStSolicitacaoMaterial(){
		return this.stSolicitacaoMaterial;
	}
	public void setIdSolicitacaoMaterial(String idSolicitacaoMaterial){
		this.idSolicitacaoMaterial=idSolicitacaoMaterial;
	}
	public String getIdSolicitacaoMaterial(){
		return this.idSolicitacaoMaterial;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdSolicitante(int cdSolicitante){
		this.cdSolicitante=cdSolicitante;
	}
	public int getCdSolicitante(){
		return this.cdSolicitante;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdSolicitacaoMaterial: " +  getCdSolicitacaoMaterial();
		valueToString += ", cdSetorSolicitante: " +  getCdSetorSolicitante();
		valueToString += ", dtSolicitacao: " +  sol.util.Util.formatDateTime(getDtSolicitacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stSolicitacaoMaterial: " +  getStSolicitacaoMaterial();
		valueToString += ", idSolicitacaoMaterial: " +  getIdSolicitacaoMaterial();
		valueToString += ", cdDocumento: " +  getCdDocumento();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdSolicitante: " +  getCdSolicitante();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new SolicitacaoMaterial(getCdSolicitacaoMaterial(),
			getCdSetorSolicitante(),
			getDtSolicitacao()==null ? null : (GregorianCalendar)getDtSolicitacao().clone(),
			getStSolicitacaoMaterial(),
			getIdSolicitacaoMaterial(),
			getCdDocumento(),
			getTxtObservacao(),
			getCdSolicitante(),
			getCdEmpresa());
	}

}
