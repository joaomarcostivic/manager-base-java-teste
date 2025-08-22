package com.tivic.manager.seg;

public class GrupoSistema {

	private int cdModulo;
	private int cdSistema;
	private int cdGrupo;
	private int lgNatureza;

	public GrupoSistema(int cdModulo,
			int cdSistema,
			int cdGrupo,
			int lgNatureza){
		setCdModulo(cdModulo);
		setCdSistema(cdSistema);
		setCdGrupo(cdGrupo);
		setLgNatureza(lgNatureza);
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
	public void setLgNatureza(int lgNatureza){
		this.lgNatureza=lgNatureza;
	}
	public int getLgNatureza(){
		return this.lgNatureza;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdModulo: " +  getCdModulo();
		valueToString += ", cdSistema: " +  getCdSistema();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		valueToString += ", lgNatureza: " +  getLgNatureza();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new GrupoSistema(getCdModulo(),
			getCdSistema(),
			getCdGrupo(),
			getLgNatureza());
	}

}
