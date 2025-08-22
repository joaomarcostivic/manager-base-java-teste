package com.tivic.manager.bpm;

public class MarcaAgrupamento {

	private int cdGrupo;
	private int cdMarca;

	public MarcaAgrupamento(int cdGrupo,
			int cdMarca){
		setCdGrupo(cdGrupo);
		setCdMarca(cdMarca);
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setCdMarca(int cdMarca){
		this.cdMarca=cdMarca;
	}
	public int getCdMarca(){
		return this.cdMarca;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupo: " +  getCdGrupo();
		valueToString += ", cdMarca: " +  getCdMarca();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MarcaAgrupamento(getCdGrupo(),
			getCdMarca());
	}

}
