package com.tivic.manager.grl;

public class GrupoEquipamento {

	private int cdGrupo;
	private int tpEquipamento;
	private String nmGrupo;
	private String sgGrupo;

	public GrupoEquipamento() { }

	public GrupoEquipamento(int cdGrupo,
			int tpEquipamento,
			String nmGrupo,
			String sgGrupo) {
		setCdGrupo(cdGrupo);
		setTpEquipamento(tpEquipamento);
		setNmGrupo(nmGrupo);
		setSgGrupo(sgGrupo);
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setTpEquipamento(int tpEquipamento){
		this.tpEquipamento=tpEquipamento;
	}
	public int getTpEquipamento(){
		return this.tpEquipamento;
	}
	public void setNmGrupo(String nmGrupo){
		this.nmGrupo=nmGrupo;
	}
	public String getNmGrupo(){
		return this.nmGrupo;
	}
	public void setSgGrupo(String sgGrupo){
		this.sgGrupo=sgGrupo;
	}
	public String getSgGrupo(){
		return this.sgGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupo: " +  getCdGrupo();
		valueToString += ", tpEquipamento: " +  getTpEquipamento();
		valueToString += ", nmGrupo: " +  getNmGrupo();
		valueToString += ", sgGrupo: " +  getSgGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new GrupoEquipamento(getCdGrupo(),
			getTpEquipamento(),
			getNmGrupo(),
			getSgGrupo());
	}

}