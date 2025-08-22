package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class Ocorrencia {

	private int cdOcorrencia;
	private int cdPessoa;
	private String txtOcorrencia;
	private GregorianCalendar dtOcorrencia;
	private int cdTipoOcorrencia;
	private int stOcorrencia;
	private int cdSistema;
	private int cdUsuario;

	public Ocorrencia() {}
	
	public Ocorrencia(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario){
		setCdOcorrencia(cdOcorrencia);
		setCdPessoa(cdPessoa);
		setTxtOcorrencia(txtOcorrencia);
		setDtOcorrencia(dtOcorrencia);
		setCdTipoOcorrencia(cdTipoOcorrencia);
		setStOcorrencia(stOcorrencia);
		setCdSistema(cdSistema);
		setCdUsuario(cdUsuario);
	}
	public void setCdOcorrencia(int cdOcorrencia){
		this.cdOcorrencia=cdOcorrencia;
	}
	public int getCdOcorrencia(){
		return this.cdOcorrencia;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setTxtOcorrencia(String txtOcorrencia){
		this.txtOcorrencia=txtOcorrencia;
	}
	public String getTxtOcorrencia(){
		return this.txtOcorrencia;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia){
		this.dtOcorrencia=dtOcorrencia;
	}
	public GregorianCalendar getDtOcorrencia(){
		return this.dtOcorrencia;
	}
	public void setCdTipoOcorrencia(int cdTipoOcorrencia){
		this.cdTipoOcorrencia=cdTipoOcorrencia;
	}
	public int getCdTipoOcorrencia(){
		return this.cdTipoOcorrencia;
	}
	public void setStOcorrencia(int stOcorrencia){
		this.stOcorrencia=stOcorrencia;
	}
	public int getStOcorrencia(){
		return this.stOcorrencia;
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
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", txtOcorrencia: " +  getTxtOcorrencia();
		valueToString += ", dtOcorrencia: " +  sol.util.Util.formatDateTime(getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdTipoOcorrencia: " +  getCdTipoOcorrencia();
		valueToString += ", stOcorrencia: " +  getStOcorrencia();
		valueToString += ", cdSistema: " +  getCdSistema();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Ocorrencia(getCdOcorrencia(),
			getCdPessoa(),
			getTxtOcorrencia(),
			getDtOcorrencia()==null ? null : (GregorianCalendar)getDtOcorrencia().clone(),
			getCdTipoOcorrencia(),
			getStOcorrencia(),
			getCdSistema(),
			getCdUsuario());
	}

}
