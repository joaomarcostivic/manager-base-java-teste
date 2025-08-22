package com.tivic.manager.seg;

public class UsuarioSistemaEmpresa {

	private int cdUsuario;
	private int cdSistema;
	private int cdEmpresa;

	public UsuarioSistemaEmpresa(int cdUsuario,
			int cdSistema,
			int cdEmpresa){
		setCdUsuario(cdUsuario);
		setCdSistema(cdSistema);
		setCdEmpresa(cdEmpresa);
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
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdUsuario: " +  getCdUsuario();
		valueToString += ", cdSistema: " +  getCdSistema();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new UsuarioSistemaEmpresa(getCdUsuario(),
			getCdSistema(),
			getCdEmpresa());
	}

}
