package com.tivic.manager.mob.funset.parts;

import java.util.List;

import com.tivic.manager.mob.funset.FunsetAitDTO;

public class FunsetRegistro {

	private FunsetCabecalho funsetCabecalho;
	private List<List<FunsetCampo>> listFunsetMultasPagas;
	private List<List<FunsetCampo>> listFunsetRestituicao;
	private FunsetResumo funsetResumo;
	
	public FunsetRegistro() {
		// TODO Auto-generated constructor stub
	}

	public FunsetCabecalho getFunsetCabecalho() {
		return funsetCabecalho;
	}

	public void setFunsetCabecalho(FunsetCabecalho funsetCabecalho) {
		this.funsetCabecalho = funsetCabecalho;
	}
	
	public List<List<FunsetCampo>> getListFunsetMultasPagas() {
		return listFunsetMultasPagas;
	}
	
	public void setListFunsetMultasPagas(List<List<FunsetCampo>> listFunsetMultasPagas) {
		this.listFunsetMultasPagas = listFunsetMultasPagas;
	}
	
	public List<List<FunsetCampo>> getListFunsetRestituicao() {
		return listFunsetRestituicao;
	}
	
	public void setListFunsetRestituicao(List<List<FunsetCampo>> listFunsetRestituicao) {
		this.listFunsetRestituicao = listFunsetRestituicao;
	}
	
	public void setFunsetResumo(FunsetResumo funsetResumo) {
		this.funsetResumo = funsetResumo;
	}
	
	public FunsetResumo getFunsetResumo() {
		return funsetResumo;
	}
	
	public void adicionarMultaPaga(List<FunsetCampo> registroFunset, FunsetAitDTO funsetAitDTO) throws Exception {
		for (FunsetCampo funsetCampo : registroFunset) {
			if(funsetCampo.getFunsetValidation() != null)
				funsetCampo.getFunsetValidation().validate(funsetCampo, funsetAitDTO);
		}
		this.listFunsetMultasPagas.add(registroFunset);
	}
	
	public void adicionarRestituicao(List<FunsetCampo> registroFunset, FunsetAitDTO funsetAitDTO) throws Exception {
		for (FunsetCampo funsetCampo : registroFunset) {
			if(funsetCampo.getFunsetValidation() != null)
				funsetCampo.getFunsetValidation().validate(funsetCampo, funsetAitDTO);
		}
		this.listFunsetRestituicao.add(registroFunset);
	}
	
	public String build() {
		String registro = funsetCabecalho.build() + "\n";
		for (List<FunsetCampo> registroFunset : listFunsetMultasPagas) {
			for (FunsetCampo funsetCampo : registroFunset) {
				registro += funsetCampo.build();
			}
			registro += "\n";
		}
		for (List<FunsetCampo> registroFunset : listFunsetRestituicao) {
			for (FunsetCampo funsetCampo : registroFunset) {
				registro += funsetCampo.build();
			}
			registro += "\n";
		}
		registro += funsetResumo.build();
		return registro;
	}
	
}
