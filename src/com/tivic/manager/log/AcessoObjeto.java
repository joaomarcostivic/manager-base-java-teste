package com.tivic.manager.log;

import java.util.GregorianCalendar;

public class AcessoObjeto extends Sistema {

	private int cdAcesso;
	private int cdFormulario;
	private int cdObjeto;
	private int cdSistema;
	private String txtResultadoAcesso;
	private int tpResultadoAcesso;

	public AcessoObjeto(int cdLog,
			GregorianCalendar dtLog,
			String txtLog,
			int tpLog,
			int cdUsuario,
			int cdFormulario,
			int cdObjeto,
			int cdSistema,
			String txtResultadoAcesso,
			int tpResultadoAcesso){
		super(cdLog,
			dtLog,
			txtLog,
			tpLog,
			cdUsuario);
		setCdAcesso(cdLog);
		setCdFormulario(cdFormulario);
		setCdObjeto(cdObjeto);
		setCdSistema(cdSistema);
		setTxtResultadoAcesso(txtResultadoAcesso);
		setTpResultadoAcesso(tpResultadoAcesso);
	}
	public void setCdAcesso(int cdAcesso){
		this.cdAcesso=cdAcesso;
	}
	public int getCdAcesso(){
		return this.cdAcesso;
	}
	public void setCdFormulario(int cdFormulario){
		this.cdFormulario=cdFormulario;
	}
	public int getCdFormulario(){
		return this.cdFormulario;
	}
	public void setCdObjeto(int cdObjeto){
		this.cdObjeto=cdObjeto;
	}
	public int getCdObjeto(){
		return this.cdObjeto;
	}
	public void setCdSistema(int cdSistema){
		this.cdSistema=cdSistema;
	}
	public int getCdSistema(){
		return this.cdSistema;
	}
	public void setTxtResultadoAcesso(String txtResultadoAcesso){
		this.txtResultadoAcesso=txtResultadoAcesso;
	}
	public String getTxtResultadoAcesso(){
		return this.txtResultadoAcesso;
	}
	public void setTpResultadoAcesso(int tpResultadoAcesso){
		this.tpResultadoAcesso=tpResultadoAcesso;
	}
	public int getTpResultadoAcesso(){
		return this.tpResultadoAcesso;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAcesso: " +  getCdAcesso();
		valueToString += ", cdFormulario: " +  getCdFormulario();
		valueToString += ", cdObjeto: " +  getCdObjeto();
		valueToString += ", cdSistema: " +  getCdSistema();
		valueToString += ", txtResultadoAcesso: " +  getTxtResultadoAcesso();
		valueToString += ", tpResultadoAcesso: " +  getTpResultadoAcesso();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AcessoObjeto(getCdLog(),
			getDtLog(),
			getTxtLog(),
			getTpLog(),
			getCdUsuario(),
			getCdFormulario(),
			getCdObjeto(),
			getCdSistema(),
			getTxtResultadoAcesso(),
			getTpResultadoAcesso());
	}

}
