package com.tivic.manager.seg;

public class UsuarioModuloEmpresa {

	private int cdUsuario;
	private int cdEmpresa;
	private int cdModulo;
	private int cdSistema;

	public UsuarioModuloEmpresa(){ }

	public UsuarioModuloEmpresa(int cdUsuario,
			int cdEmpresa,
			int cdModulo,
			int cdSistema){
		setCdUsuario(cdUsuario);
		setCdEmpresa(cdEmpresa);
		setCdModulo(cdModulo);
		setCdSistema(cdSistema);
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
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
	public String toString() {
		String valueToString = "";
		valueToString += "cdUsuario: " +  getCdUsuario();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdModulo: " +  getCdModulo();
		valueToString += ", cdSistema: " +  getCdSistema();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new UsuarioModuloEmpresa(getCdUsuario(),
			getCdEmpresa(),
			getCdModulo(),
			getCdSistema());
	}

}