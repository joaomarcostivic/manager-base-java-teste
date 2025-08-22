package com.tivic.manager.seg;

import java.util.GregorianCalendar;

public class UsuarioChave {

	private int cdChave;
	private int cdUsuario;
	private int tpChave;
	private byte[] blbChavePublica;
	private byte[] blbChavePrivada;
	private GregorianCalendar dtCriacao;
	private GregorianCalendar dtInativacao;
	private int stChave;

	public UsuarioChave() { }

	public UsuarioChave(int cdChave,
			int cdUsuario,
			int tpChave,
			byte[] blbChavePublica,
			byte[] blbChavePrivada,
			GregorianCalendar dtCriacao,
			GregorianCalendar dtInativacao,
			int stChave) {
		setCdChave(cdChave);
		setCdUsuario(cdUsuario);
		setTpChave(tpChave);
		setBlbChavePublica(blbChavePublica);
		setBlbChavePrivada(blbChavePrivada);
		setDtCriacao(dtCriacao);
		setDtInativacao(dtInativacao);
		setStChave(stChave);
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
	public void setTpChave(int tpChave){
		this.tpChave=tpChave;
	}
	public int getTpChave(){
		return this.tpChave;
	}
	public void setBlbChavePublica(byte[] blbChavePublica){
		this.blbChavePublica=blbChavePublica;
	}
	public byte[] getBlbChavePublica(){
		return this.blbChavePublica;
	}
	public void setBlbChavePrivada(byte[] blbChavePrivada){
		this.blbChavePrivada=blbChavePrivada;
	}
	public byte[] getBlbChavePrivada(){
		return this.blbChavePrivada;
	}
	public void setDtCriacao(GregorianCalendar dtCriacao){
		this.dtCriacao=dtCriacao;
	}
	public GregorianCalendar getDtCriacao(){
		return this.dtCriacao;
	}
	public void setDtInativacao(GregorianCalendar dtInativacao){
		this.dtInativacao=dtInativacao;
	}
	public GregorianCalendar getDtInativacao(){
		return this.dtInativacao;
	}
	public void setStChave(int stChave){
		this.stChave=stChave;
	}
	public int getStChave(){
		return this.stChave;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdChave: " +  getCdChave();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", tpChave: " +  getTpChave();
		valueToString += ", blbChavePublica: " +  getBlbChavePublica();
		valueToString += ", blbChavePrivada: " +  getBlbChavePrivada();
		valueToString += ", dtCriacao: " +  sol.util.Util.formatDateTime(getDtCriacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtInativacao: " +  sol.util.Util.formatDateTime(getDtInativacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stChave: " +  getStChave();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new UsuarioChave(getCdChave(),
			getCdUsuario(),
			getTpChave(),
			getBlbChavePublica(),
			getBlbChavePrivada(),
			getDtCriacao()==null ? null : (GregorianCalendar)getDtCriacao().clone(),
			getDtInativacao()==null ? null : (GregorianCalendar)getDtInativacao().clone(),
			getStChave());
	}

}