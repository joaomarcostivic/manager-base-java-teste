package com.tivic.manager.log;

import java.util.GregorianCalendar;

public class ExecucaoAcao {

	private int cdExecucao;
	private int cdAcao;
	private int cdModulo;
	private int cdSistema;
	private int cdUsuario;
	private GregorianCalendar dtExecucao;
	private String txtExecucao;
	private String txtResultadoExecucao;
	private int tpResultadoExecucao;

	public ExecucaoAcao(){ }

	public ExecucaoAcao(int cdExecucao,
			int cdAcao,
			int cdModulo,
			int cdSistema,
			int cdUsuario,
			GregorianCalendar dtExecucao,
			String txtExecucao,
			String txtResultadoExecucao,
			int tpResultadoExecucao){
				
		setCdExecucao(cdExecucao);
		setCdAcao(cdAcao);
		setCdModulo(cdModulo);
		setCdSistema(cdSistema);
		setCdUsuario(cdUsuario);
		setDtExecucao(dtExecucao);
		setTxtExecucao(txtExecucao);
		setTxtResultadoExecucao(txtResultadoExecucao);
		setTpResultadoExecucao(tpResultadoExecucao);
	}
	public void setCdExecucao(int cdExecucao){
		this.cdExecucao=cdExecucao;
	}
	public int getCdExecucao(){
		return this.cdExecucao;
	}
	public void setCdAcao(int cdAcao){
		this.cdAcao=cdAcao;
	}
	public int getCdAcao(){
		return this.cdAcao;
	}
	public void setCdModulo(int cdModulo){
		this.cdModulo=cdModulo;
	}
	public int getCdModulo(){
		return this.cdModulo;
	}
	public void setCdSistema(int cdSistema){
		this.cdSistema=cdSistema;
	}
	public int getCdSistema(){
		return this.cdSistema;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setDtExecucao(GregorianCalendar dtExecucao){
		this.dtExecucao=dtExecucao;
	}
	public GregorianCalendar getDtExecucao(){
		return this.dtExecucao;
	}
	public void setTxtExecucao(String txtExecucao){
		this.txtExecucao=txtExecucao;
	}
	public String getTxtExecucao(){
		return this.txtExecucao;
	}
	public void setTxtResultadoExecucao(String txtResultadoExecucao){
		this.txtResultadoExecucao=txtResultadoExecucao;
	}
	public String getTxtResultadoExecucao(){
		return this.txtResultadoExecucao;
	}
	public void setTpResultadoExecucao(int tpResultadoExecucao){
		this.tpResultadoExecucao=tpResultadoExecucao;
	}
	public int getTpResultadoExecucao(){
		return this.tpResultadoExecucao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdExecucao: " +  getCdExecucao();
		valueToString += ", cdAcao: " +  getCdAcao();
		valueToString += ", cdModulo: " +  getCdModulo();
		valueToString += ", cdSistema: " +  getCdSistema();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", dtExecucao: " +  sol.util.Util.formatDateTime(getDtExecucao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtExecucao: " +  getTxtExecucao();
		valueToString += ", txtResultadoExecucao: " +  getTxtResultadoExecucao();
		valueToString += ", tpResultadoExecucao: " +  getTpResultadoExecucao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ExecucaoAcao(getCdExecucao(),
			getCdAcao(),
			getCdModulo(),
			getCdSistema(),
			getCdUsuario(),
			getDtExecucao()==null ? null : (GregorianCalendar)getDtExecucao().clone(),
			getTxtExecucao(),
			getTxtResultadoExecucao(),
			getTpResultadoExecucao());
	}

}