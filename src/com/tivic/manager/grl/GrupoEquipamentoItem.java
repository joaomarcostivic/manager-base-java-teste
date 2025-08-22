package com.tivic.manager.grl;

public class GrupoEquipamentoItem {

	private int cdGrupo;
	private int cdEquipamento;

	public GrupoEquipamentoItem() { }

	public GrupoEquipamentoItem(int cdGrupo,
			int cdEquipamento) {
		setCdGrupo(cdGrupo);
		setCdEquipamento(cdEquipamento);
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setCdEquipamento(int cdEquipamento){
		this.cdEquipamento=cdEquipamento;
	}
	public int getCdEquipamento(){
		return this.cdEquipamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupo: " +  getCdGrupo();
		valueToString += ", cdEquipamento: " +  getCdEquipamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new GrupoEquipamentoItem(getCdGrupo(),
			getCdEquipamento());
	}

}