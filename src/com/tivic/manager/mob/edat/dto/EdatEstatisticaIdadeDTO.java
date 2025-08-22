package com.tivic.manager.mob.edat.dto;

public class EdatEstatisticaIdadeDTO {
	
	private int qtIdade;
	private String nmIdade;
	
	public EdatEstatisticaIdadeDTO() {}
	
	public EdatEstatisticaIdadeDTO(int qtIdade, String nmIdade) {
		this.qtIdade = qtIdade;
		this.nmIdade = nmIdade;
	}
	
	public int getQtIdade() {
		return qtIdade;
	}
	public void setQtIdade(int qtIdade) {
		this.qtIdade = qtIdade;
	}
	public String getNmIdade() {
		return nmIdade;
	}
	public void setNmIdade(String nmIdade) {
		this.nmIdade = nmIdade;
	}

}
