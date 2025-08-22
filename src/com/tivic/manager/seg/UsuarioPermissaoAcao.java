package com.tivic.manager.seg;

public class UsuarioPermissaoAcao {

	private int cdUsuario;
	private int cdAcao;
	private int cdModulo;
	private int cdSistema;
	private int lgNatureza;

	public UsuarioPermissaoAcao(
			int cdUsuario,
			int cdAcao,
			int cdModulo,
			int cdSistema,
			int lgNatureza){
		setCdUsuario(cdUsuario);
		setCdAcao(cdAcao);
		setCdModulo(cdModulo);
		setCdSistema(cdSistema);
		setLgNatureza(lgNatureza);
	}
	
	public UsuarioPermissaoAcao() {}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
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
	public void setLgNatureza(int lgNatureza){
		this.lgNatureza=lgNatureza;
	}
	public int getLgNatureza(){
		return this.lgNatureza;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdUsuario: " +  getCdUsuario();
		valueToString += ", cdAcao: " +  getCdAcao();
		valueToString += ", cdModulo: " +  getCdModulo();
		valueToString += ", cdSistema: " +  getCdSistema();
		valueToString += ", lgNatureza: " +  getLgNatureza();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new UsuarioPermissaoAcao(getCdUsuario(),
			getCdAcao(),
			getCdModulo(),
			getCdSistema(),
			getLgNatureza());
	}

}
