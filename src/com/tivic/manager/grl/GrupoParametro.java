package com.tivic.manager.grl;

public class GrupoParametro {
	
	private int cdGrupoParametro;
	private String nmGrupoParametro;
	
	public GrupoParametro() {}
	
	public GrupoParametro(int cdGrupoParametro, String nmGrupoParametro) {
		this.cdGrupoParametro = cdGrupoParametro;
		this.nmGrupoParametro = nmGrupoParametro;
	}

	public int getCdGrupoParametro() {
		return cdGrupoParametro;
	}

	public void setCdGrupoParametro(int cdGrupoParametro) {
		this.cdGrupoParametro = cdGrupoParametro;
	}

	public String getNmGrupoParametro() {
		return nmGrupoParametro;
	}

	public void setNmGrupoParametro(String nmGrupoParametro) {
		this.nmGrupoParametro = nmGrupoParametro;
	}	

}
