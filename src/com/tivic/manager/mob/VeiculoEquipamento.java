package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import org.json.JSONException;
import org.json.JSONObject;

public class VeiculoEquipamento {

	private int cdEquipamento;
	private int cdVeiculo;
	private int cdInstalacao;
	private GregorianCalendar dtInstalacao;
	private int stInstalacao;
	private String txtObservacao;
	
	public VeiculoEquipamento(){ }

	public VeiculoEquipamento(
			int cdEquipamento,
			int cdVeiculo,
			int cdInstalacao,
			GregorianCalendar dtInstalacao,
			int stInstalacao,
			String txtObservacao){
		setCdEquipamento(cdEquipamento);
		setCdVeiculo(cdVeiculo);
		setCdInstalacao(cdInstalacao);
		setDtInstalacao(dtInstalacao);
		setStInstalacao(stInstalacao);
		setTxtObservacao(txtObservacao);
	}
	public void setCdEquipamento(int cdEquipamento){
		this.cdEquipamento=cdEquipamento;
	}
	public int getCdEquipamento(){
		return this.cdEquipamento;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setCdInstalacao(int cdInstalacao){
		this.cdInstalacao=cdInstalacao;
	}
	public int getCdInstalacao(){
		return this.cdInstalacao;
	}
	public void setDtInstalacao(GregorianCalendar dtInstalacao){
		this.dtInstalacao=dtInstalacao;
	}
	public GregorianCalendar getDtInstalacao(){
		return this.dtInstalacao;
	}
	public void setStInstalacao(int stInstalacao){
		this.stInstalacao=stInstalacao;
	}
	public int getStInstalacao(){
		return this.stInstalacao;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	
	public String toString() {
		try {
			JSONObject json = new JSONObject();
			json.put("cdEquipamento", getCdEquipamento());
			json.put("cdVeiculo", getCdVeiculo());
			json.put("cdInstalacao", getCdInstalacao());
			json.put("dtInstalacao", sol.util.Util.formatDateTime(getDtInstalacao(), "yyyy-MM-dd'T'HH:mm:ss:SSS", ""));
			json.put("stInstalacao", getStInstalacao());
			json.put("txtObservacao", getTxtObservacao());
			return json.toString();
		} catch(JSONException e) {
			return new StringBuilder().append("cdEquipamento: ").append(getCdEquipamento()).toString();
		}
	}

	public Object clone() {
		return new VeiculoEquipamento(
			getCdEquipamento(),
			getCdVeiculo(),
			getCdInstalacao(),
			getDtInstalacao()==null ? null : (GregorianCalendar)getDtInstalacao().clone(),
			getStInstalacao(),
			getTxtObservacao());
	}
}