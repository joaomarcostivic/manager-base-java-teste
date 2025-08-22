package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class Equipamento extends com.tivic.manager.grl.Equipamento {

	public Equipamento(){ }

	public Equipamento(int cdEquipamento,
			String nmEquipamento,
			String idEquipamento,
			int tpEquipamento,
			String txtObservacao,
			String nmMarca,
			String nmModelo,
			int stEquipamento,
			int cdLogradouro,
			Double vlLatitude,
			Double vlLongitude){
		super(cdEquipamento,
			nmEquipamento,
			idEquipamento,
			tpEquipamento,
			txtObservacao,
			nmMarca,
			nmModelo,
			stEquipamento,
			cdLogradouro,
			vlLatitude,
			vlLongitude);
	}
	
	public Equipamento(int cdEquipamento,
			String nmEquipamento,
			String idEquipamento,
			int tpEquipamento,
			String txtObservacao,
			String nmMarca,
			String nmModelo,
			int stEquipamento,
			int cdLogradouro,
			Double vlLatitude,
			Double vlLongitude,
			int cdOrgao,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			String nmHost,
			int nrPort,
			String nmPwd,
			int nrChannel,
			String nmLogin,
			String nmUrlSnapshot,
			String nmUrlStream,
			String dsLocal,
			int lgCriptografia,
			int lgSyncFtp,
			GregorianCalendar dtAfericao,
			String nrLacre,
			String nrInventarioInmetro){
		super(cdEquipamento,
			nmEquipamento,
			idEquipamento,
			tpEquipamento,
			txtObservacao,
			nmMarca,
			nmModelo,
			stEquipamento,
			cdLogradouro,
			vlLatitude,
			vlLongitude,
			cdOrgao,
			dtInicial,
			dtFinal);
	}
	
	public String toString() {
		return super.toString();
	}

	public Object clone() {
		return super.clone();
	}
}