package com.tivic.manager.ptc;

import java.util.GregorianCalendar;

public class DocumentoPendencia {

	private int cdDocumento;
	private int cdTipoPendencia;
	private GregorianCalendar dtPendencia;
	private GregorianCalendar dtBaixa;
	private String txtPendencia;
	private int cdUsuarioRegistro;
	private int cdUsuarioBaixa;
	private String txtBaixa;

	public DocumentoPendencia() { }
	
	public DocumentoPendencia(int cdDocumento,
			int cdTipoPendencia,
			GregorianCalendar dtPendencia,
			GregorianCalendar dtBaixa,
			String txtPendencia,
			int cdUsuarioRegistro,
			int cdUsuarioBaixa,
			String txtBaixa){
		setCdDocumento(cdDocumento);
		setCdTipoPendencia(cdTipoPendencia);
		setDtPendencia(dtPendencia);
		setDtBaixa(dtBaixa);
		setTxtPendencia(txtPendencia);
		setCdUsuarioRegistro(cdUsuarioRegistro);
		setCdUsuarioBaixa(cdUsuarioBaixa);
		setTxtBaixa(txtBaixa);
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setCdTipoPendencia(int cdTipoPendencia){
		this.cdTipoPendencia=cdTipoPendencia;
	}
	public int getCdTipoPendencia(){
		return this.cdTipoPendencia;
	}
	public void setDtPendencia(GregorianCalendar dtPendencia){
		this.dtPendencia=dtPendencia;
	}
	public GregorianCalendar getDtPendencia(){
		return this.dtPendencia;
	}
	public void setDtBaixa(GregorianCalendar dtBaixa){
		this.dtBaixa=dtBaixa;
	}
	public GregorianCalendar getDtBaixa(){
		return this.dtBaixa;
	}
	public void setTxtPendencia(String txtPendencia){
		this.txtPendencia=txtPendencia;
	}
	public String getTxtPendencia(){
		return this.txtPendencia;
	}
	public void setCdUsuarioRegistro(int cdUsuarioRegistro){
		this.cdUsuarioRegistro=cdUsuarioRegistro;
	}
	public int getCdUsuarioRegistro(){
		return this.cdUsuarioRegistro;
	}
	public void setCdUsuarioBaixa(int cdUsuarioBaixa){
		this.cdUsuarioBaixa=cdUsuarioBaixa;
	}
	public int getCdUsuarioBaixa(){
		return this.cdUsuarioBaixa;
	}
	public void setTxtBaixa(String txtBaixa){
		this.txtBaixa=txtBaixa;
	}
	public String getTxtBaixa(){
		return this.txtBaixa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocumento: " +  getCdDocumento();
		valueToString += ", cdTipoPendencia: " +  getCdTipoPendencia();
		valueToString += ", dtPendencia: " +  sol.util.Util.formatDateTime(getDtPendencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtBaixa: " +  sol.util.Util.formatDateTime(getDtBaixa(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtPendencia: " +  getTxtPendencia();
		valueToString += ", cdUsuarioRegistro: " +  getCdUsuarioRegistro();
		valueToString += ", cdUsuarioBaixa: " +  getCdUsuarioBaixa();
		valueToString += ", txtBaixa: " + getTxtBaixa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DocumentoPendencia(getCdDocumento(),
			getCdTipoPendencia(),
			getDtPendencia()==null ? null : (GregorianCalendar)getDtPendencia().clone(),
			getDtBaixa()==null ? null : (GregorianCalendar)getDtBaixa().clone(),
			getTxtPendencia(),
			getCdUsuarioRegistro(),
			getCdUsuarioBaixa(),
			getTxtBaixa());
	}

}