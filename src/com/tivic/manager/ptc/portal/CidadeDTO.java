package com.tivic.manager.ptc.portal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CidadeDTO {

	private String nmCidade;
	private String nmUf;
	private int cdCidade;
	

	public CidadeDTO(){ }

	public CidadeDTO(String nmCidade, String nmUf, int cdCidade){
		setNmCidade(nmCidade);
		setNmUf(nmUf);
		setCdCidade(cdCidade);
	}

	public String getNmCidade() {
		return nmCidade;
	}

	public  void setNmCidade(String nmCidade) {
		this.nmCidade = nmCidade;
	}

	public  String getNmUf() {
		return nmUf;
	}

	public  void setNmUf(String nmUf) {
		this.nmUf = nmUf;
	}

	public  int getCdCidade() {
		return cdCidade;
	}

	public  void setCdCidade(int cdCidade) {
		this.cdCidade = cdCidade;
	}
	
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
	
	public Object clone() {
		return new CidadeDTO(getNmCidade(),
			getNmUf(),
			getCdCidade());
	}

}
