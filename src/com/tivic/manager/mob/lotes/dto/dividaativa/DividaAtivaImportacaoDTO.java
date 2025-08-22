package com.tivic.manager.mob.lotes.dto.dividaativa;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DividaAtivaImportacaoDTO {

	private ArrayList<DividaAtivaRetornoDTO> dividasRetorno;
	private byte[] csv;

	public ArrayList<DividaAtivaRetornoDTO> getDividasRetorno() {
		return dividasRetorno;
	}
	
	public void setDividasRetorno(ArrayList<DividaAtivaRetornoDTO> dividasRetorno) {
		this.dividasRetorno = dividasRetorno;
	}
	
	public byte[] getCsv() {
		return csv;
	}
	
	public void setCsv(byte[] csv) {
		this.csv = csv;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
}
