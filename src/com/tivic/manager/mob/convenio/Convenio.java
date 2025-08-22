package com.tivic.manager.mob.convenio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Convenio {

	private int cdConvenio;
	private String nmConvenio;
	private int tpConvenio;
	private int lgDefault;
	
	public Convenio() {}
	
	public Convenio(int cdConvenio, String nmConvenio, int tpConvenio, int lgDefault) {
		this.cdConvenio = cdConvenio;
		this.nmConvenio = nmConvenio;
		this.lgDefault = lgDefault;
	}
	
	public int getCdConvenio() {
		return cdConvenio;
	}
	
	public void setCdConvenio(int cdConvenio) {
		this.cdConvenio = cdConvenio;
	}
	
	public String getNmConvenio() {
		return nmConvenio;
	}
	
	public void setNmConvenio(String nmConvenio) {
		this.nmConvenio = nmConvenio;
	}
	
	public int getLgDefault() {
		return lgDefault;
	}
	
	public void setLgDefault(int lgDefault) {
		this.lgDefault = lgDefault;
	}

	public int getTpConvenio() {
		return tpConvenio;
	}

	public void setTpConvenio(int tpConvenio) {
		this.tpConvenio = tpConvenio;
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
