package com.tivic.manager.mob;

public class VistoriaItem {

	private int cdVistoriaItem;
	private int cdVistoriaItemGrupo;
	private String nmVistoriaItem;
	private int cdEquipamento;

	public VistoriaItem(){ }

	public VistoriaItem(int cdVistoriaItem,
			int cdVistoriaItemGrupo,
			String nmVistoriaItem,
			int cdEquipamento){
		setCdVistoriaItem(cdVistoriaItem);
		setCdVistoriaItemGrupo(cdVistoriaItemGrupo);
		setNmVistoriaItem(nmVistoriaItem);
		setCdEquipamento(cdEquipamento);
	}
	public void setCdVistoriaItem(int cdVistoriaItem){
		this.cdVistoriaItem=cdVistoriaItem;
	}
	public int getCdVistoriaItem(){
		return this.cdVistoriaItem;
	}
	public void setCdVistoriaItemGrupo(int cdVistoriaItemGrupo){
		this.cdVistoriaItemGrupo=cdVistoriaItemGrupo;
	}
	public int getCdVistoriaItemGrupo(){
		return this.cdVistoriaItemGrupo;
	}
	public void setNmVistoriaItem(String nmVistoriaItem){
		this.nmVistoriaItem=nmVistoriaItem;
	}
	public String getNmVistoriaItem(){
		return this.nmVistoriaItem;
	}
	public void setCdEquipamento(int cdEquipamento){
		this.cdEquipamento=cdEquipamento;
	}
	public int getCdEquipamento(){
		return this.cdEquipamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdVistoriaItem: " +  getCdVistoriaItem();
		valueToString += ", cdVistoriaItemGrupo: " +  getCdVistoriaItemGrupo();
		valueToString += ", nmVistoriaItem: " +  getNmVistoriaItem();
		valueToString += ", cdEquipamento: " +  getCdEquipamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new VistoriaItem(getCdVistoriaItem(),
			getCdVistoriaItemGrupo(),
			getNmVistoriaItem(),
			getCdEquipamento());
	}

}