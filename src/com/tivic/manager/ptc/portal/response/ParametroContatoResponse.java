package com.tivic.manager.ptc.portal.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ParametroContatoResponse {
	private String nmEmail;
	private String nrTelefone;
	
	private String nrCep;
	private String nmLogradouro;
	private String nrEndereco;
	private String nmBairro;
	private String nmCidade;
	private String ufCidade;
	private String urlSiteOrgao;
	
	public String getNmEmail() {
		return nmEmail;
	}
	public void setNmEmail(String nmEmail) {
		this.nmEmail = nmEmail;
	}
	
	public String getNrTelefone() {
		return nrTelefone;
	}
	public void setNrTelefone(String nrTelefone) {
		this.nrTelefone = nrTelefone;
	}
	public String getNrCep() {
		return nrCep;
	}
	public void setNrCep(String nrCep) {
		this.nrCep = nrCep;
	}
	public String getNmLogradouro() {
		return nmLogradouro;
	}
	public void setNmLogradouro(String nmLogradouro) {
		this.nmLogradouro = nmLogradouro;
	}
	public String getNrEndereco() {
		return nrEndereco;
	}
	public void setNrEndereco(String nrEndereco) {
		this.nrEndereco = nrEndereco;
	}	
	public String getNmBairro() {
		return nmBairro;
	}
	public void setNmBairro(String nmBairro) {
		this.nmBairro = nmBairro;
	}
	public String getNmCidade() {
		return nmCidade;
	}
	public void setNmCidade(String nmCidade) {
		this.nmCidade = nmCidade;
	}
	public String getUfCidade() {
		return ufCidade;
	}
	public void setUfCidade(String ufCidade) {
		this.ufCidade = ufCidade;
	}
	public String getUrlSiteOrgao() {
		return urlSiteOrgao;
	}
	public void setUrlSiteOrgao(String urlSiteOrgao) {
		this.urlSiteOrgao = urlSiteOrgao;
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
