package com.tivic.manager.mob.concessao.relatorio;

import java.util.HashMap;

import sol.dao.ResultSetMap;

public class RelatorioConcessaoQuantitativoDTO {

	private String nmTpConcessao;
	private int quantidadeConcessao;
	private int quantidadeRepresentantes;
	private int quantidadeAuxiliares;
	private int quantidadeVeiculos;
	
	public void setNmTpConcessao(String nmTpConcessao) {
		this.nmTpConcessao = nmTpConcessao;
	}
	
	public String getNmTpConcessao() {
		return nmTpConcessao;
	}
	
	public void setQuantidadeAuxiliares(int quantidadeAuxiliares) {
		this.quantidadeAuxiliares = quantidadeAuxiliares;
	}
	
	public int getQuantidadeAuxiliares() {
		return quantidadeAuxiliares;
	}
	
	public void setQuantidadeConcessao(int quantidadeConcessao) {
		this.quantidadeConcessao = quantidadeConcessao;
	}
	
	public int getQuantidadeConcessao() {
		return quantidadeConcessao;
	}
	
	public void setQuantidadeRepresentantes(int quantidadeRepresentantes) {
		this.quantidadeRepresentantes = quantidadeRepresentantes;
	}
	
	public int getQuantidadeRepresentantes() {
		return quantidadeRepresentantes;
	}
	
	public void setQuantidadeVeiculos(int quantidadeVeiculos) {
		this.quantidadeVeiculos = quantidadeVeiculos;
	}
	
	public int getQuantidadeVeiculos() {
		return quantidadeVeiculos;
	}
	
	public ResultSetMap getResultSetMap() {
		ResultSetMap rsm = new ResultSetMap();
		HashMap<String, Object> register = new HashMap<String, Object>();
		register.put("NM_TP_CONCESSAO", getNmTpConcessao());
		register.put("QTD_CONCESSIONARIO", getQuantidadeConcessao());
		register.put("QTD_MOTORISTA_AUXILIAR", getQuantidadeAuxiliares());
		register.put("QTD_VEICULOS", getQuantidadeVeiculos());
		register.put("QTD_REPRESENTANTE", getQuantidadeRepresentantes());
		rsm.addRegister(register);
		return rsm;
	}
	
}
