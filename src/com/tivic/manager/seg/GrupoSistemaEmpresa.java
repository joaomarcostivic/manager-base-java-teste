package com.tivic.manager.seg;

public class GrupoSistemaEmpresa {

	private int cdModulo;
	private int cdSistema;
	private int cdGrupo;
	private int cdEmpresa;

	public GrupoSistemaEmpresa(int cdModulo,
			int cdSistema,
			int cdGrupo,
			int cdEmpresa){
		setCdModulo(cdModulo);
		setCdSistema(cdSistema);
		setCdGrupo(cdGrupo);
		setCdEmpresa(cdEmpresa);
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
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdModulo: " +  getCdModulo();
		valueToString += ", cdSistema: " +  getCdSistema();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new GrupoSistemaEmpresa(getCdModulo(),
			getCdSistema(),
			getCdGrupo(),
			getCdEmpresa());
	}

}
