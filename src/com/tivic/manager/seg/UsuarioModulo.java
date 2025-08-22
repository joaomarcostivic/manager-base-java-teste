package com.tivic.manager.seg;

public class UsuarioModulo {

	private int cdUsuario;
	private int cdModulo;
	private int cdSistema;
	private int lgNatureza;

	public UsuarioModulo(){ }

	public UsuarioModulo(int cdUsuario,
			int cdModulo,
			int cdSistema,
			int lgNatureza){
		setCdUsuario(cdUsuario);
		setCdModulo(cdModulo);
		setCdSistema(cdSistema);
		setLgNatureza(lgNatureza);
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
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
	public void setLgNatureza(int lgNatureza){
		this.lgNatureza=lgNatureza;
	}
	public int getLgNatureza(){
		return this.lgNatureza;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdUsuario: " +  getCdUsuario();
		valueToString += ", cdModulo: " +  getCdModulo();
		valueToString += ", cdSistema: " +  getCdSistema();
		valueToString += ", lgNatureza: " +  getLgNatureza();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new UsuarioModulo(getCdUsuario(),
			getCdModulo(),
			getCdSistema(),
			getLgNatureza());
	}

}