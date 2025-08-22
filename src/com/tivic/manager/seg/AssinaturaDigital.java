package com.tivic.manager.seg;

import java.util.GregorianCalendar;

public class AssinaturaDigital {

	private int cdAssinatura;
	private int cdChave;
	private int cdUsuario;
	private GregorianCalendar dtAssinatura;
	private byte[] blbAssinatura;

	public AssinaturaDigital() { }

	public AssinaturaDigital(int cdAssinatura,
			int cdChave,
			int cdUsuario,
			GregorianCalendar dtAssinatura,
			byte[] blbAssinatura) {
		setCdAssinatura(cdAssinatura);
		setCdChave(cdChave);
		setCdUsuario(cdUsuario);
		setDtAssinatura(dtAssinatura);
		setBlbAssinatura(blbAssinatura);
	}
	public void setCdAssinatura(int cdAssinatura){
		this.cdAssinatura=cdAssinatura;
	}
	public int getCdAssinatura(){
		return this.cdAssinatura;
	}
	public void setCdChave(int cdChave){
		this.cdChave=cdChave;
	}
	public int getCdChave(){
		return this.cdChave;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setDtAssinatura(GregorianCalendar dtAssinatura){
		this.dtAssinatura=dtAssinatura;
	}
	public GregorianCalendar getDtAssinatura(){
		return this.dtAssinatura;
	}
	public void setBlbAssinatura(byte[] blbAssinatura){
		this.blbAssinatura=blbAssinatura;
	}
	public byte[] getBlbAssinatura(){
		return this.blbAssinatura;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAssinatura: " +  getCdAssinatura();
		valueToString += ", cdChave: " +  getCdChave();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", dtAssinatura: " +  sol.util.Util.formatDateTime(getDtAssinatura(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", blbAssinatura: " +  getBlbAssinatura();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AssinaturaDigital(getCdAssinatura(),
			getCdChave(),
			getCdUsuario(),
			getDtAssinatura()==null ? null : (GregorianCalendar)getDtAssinatura().clone(),
			getBlbAssinatura());
	}

}