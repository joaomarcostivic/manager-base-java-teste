package com.tivic.manager.seg;

public class UsuarioSistema {

	private int cdUsuario;
	private int cdSistema;
	private int lgNatureza;

	public UsuarioSistema(int cdUsuario,
			int cdSistema,
			int lgNatureza){
		setCdUsuario(cdUsuario);
		setCdSistema(cdSistema);
		setLgNatureza(lgNatureza);
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdSistema(int cdSistema){
		this.cdSistema=cdSistema;
	}
	public int getCdSistema(){
		return this.cdSistema;
	}
	public void setLgNatureza(int lgNatureza){
		this.lgNatureza=lgNatureza;
	}
	public int getLgNatureza(){
		return this.lgNatureza;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdUsuario: " +  getCdUsuario();
		valueToString += ", cdSistema: " +  getCdSistema();
		valueToString += ", lgNatureza: " +  getLgNatureza();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new UsuarioSistema(getCdUsuario(),
			getCdSistema(),
			getLgNatureza());
	}

}
