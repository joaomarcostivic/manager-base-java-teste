package com.tivic.manager.acd;

import org.json.JSONException;
import org.json.JSONObject;

public class TipoEquipamento {

	private int cdTipoEquipamento;
	private String nmTipoEquipamento;
	private String idTipoEquipamento;

	public TipoEquipamento() { }
			
	public TipoEquipamento(int cdTipoEquipamento,
			String nmTipoEquipamento,
			String idTipoEquipamento){
		setCdTipoEquipamento(cdTipoEquipamento);
		setNmTipoEquipamento(nmTipoEquipamento);
		setIdTipoEquipamento(idTipoEquipamento);
	}
	
	public void setCdTipoEquipamento(int cdTipoEquipamento){
		this.cdTipoEquipamento=cdTipoEquipamento;
	}
	
	public int getCdTipoEquipamento(){
		return this.cdTipoEquipamento;
	}
	
	public void setNmTipoEquipamento(String nmTipoEquipamento){
		this.nmTipoEquipamento=nmTipoEquipamento;
	}
	
	public String getNmTipoEquipamento(){
		return this.nmTipoEquipamento;
	}
	
	public void setIdTipoEquipamento(String idTipoEquipamento){
		this.idTipoEquipamento=idTipoEquipamento;
	}
	
	public String getIdTipoEquipamento(){
		return this.idTipoEquipamento;
	}
	
	public String toString() {
		try {
			JSONObject json = new JSONObject();
			json.put("cdTipoEquipamento", getCdTipoEquipamento());
			json.put("nmTipoEquipamento", getNmTipoEquipamento());
			json.put("idTipoEquipamento", getIdTipoEquipamento());
			return json.toString();
		} catch (JSONException e) {
			return "cdTipoEquipamento: " + getCdTipoEquipamento();
		}
		
	}

	public Object clone() {
		return new TipoEquipamento(getCdTipoEquipamento(),
			getNmTipoEquipamento(),
			getIdTipoEquipamento());
	}

}