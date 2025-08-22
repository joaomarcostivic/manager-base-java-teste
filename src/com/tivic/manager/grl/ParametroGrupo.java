package com.tivic.manager.grl;

public class ParametroGrupo {
	
	private int cdParametro;
	private int cdGrupoParametro;
	
	public ParametroGrupo() {}
	
	public ParametroGrupo(int cdParametro, int cdGrupoParametro) {
		this.cdParametro = cdParametro;
		this.cdGrupoParametro = cdGrupoParametro;
	}

	public int getCdParametro() {
		return cdParametro;
	}

	public void setCdParametro(int cdParametro) {
		this.cdParametro = cdParametro;
	}

	public int getCdGrupoParametro() {
		return cdGrupoParametro;
	}

	public void setCdGrupoParametro(int cdGrupoParametro) {
		this.cdGrupoParametro = cdGrupoParametro;
	}
	
}
