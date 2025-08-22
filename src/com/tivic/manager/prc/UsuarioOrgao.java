package com.tivic.manager.prc;

public class UsuarioOrgao {

	private int cdOrgao;
	private int cdUsuario;

	public UsuarioOrgao(){ }

	public UsuarioOrgao(int cdOrgao,
			int cdUsuario){
		setCdOrgao(cdOrgao);
		setCdUsuario(cdUsuario);
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOrgao: " +  getCdOrgao();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new UsuarioOrgao(getCdOrgao(),
			getCdUsuario());
	}

}
