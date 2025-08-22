package com.tivic.manager.sinc;

public class TabelaGrupo {

	private int cdTabela;
	private int cdGrupo;

	public TabelaGrupo(){ }

	public TabelaGrupo(int cdTabela,
			int cdGrupo){
		setCdTabela(cdTabela);
		setCdGrupo(cdGrupo);
	}
	public void setCdTabela(int cdTabela){
		this.cdTabela=cdTabela;
	}
	public int getCdTabela(){
		return this.cdTabela;
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabela: " +  getCdTabela();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TabelaGrupo(getCdTabela(),
			getCdGrupo());
	}

}