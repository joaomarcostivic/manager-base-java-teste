package com.tivic.manager.mob.orgaoexterno;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrgaoExterno {

	private int cdOrgaoExterno; 
	private String nmOrgaoExterno;
	private String sgOrgaoExterno;
	private int cdTipoLogradouro;
	private String nmLogradouro;
	private int cdCidade;
	private String nmBairro;
	private String nrCep;
	private String nrEndereco;
	
	public OrgaoExterno() {}
	
	public OrgaoExterno(int cdOrgaoExterno, 
			String nmOrgaoExterno, 
			String sgOrgaoExterno, 
			int cdTipoLogradouro,
			String nmLogradouro,
			int cdCidade,
			String nmBairro,
			String nrCep,
			String nrEndereco) {
		setCdOrgaoExterno(cdOrgaoExterno);
		setNmOrgaoExterno(nmOrgaoExterno);
		setSgOrgaoExterno(sgOrgaoExterno);
		setCdTipoLogradouro(cdTipoLogradouro);
		setNmLogradouro(nmLogradouro);
		setCdCidade(cdCidade);
		setNmBairro(nmBairro);
		setNrCep(nrCep);
		setNrEndereco(nrEndereco);
	}

	public int getCdOrgaoExterno() {
		return cdOrgaoExterno;
	}
	
	public void setCdOrgaoExterno(int cdOrgaoExterno) {
		this.cdOrgaoExterno = cdOrgaoExterno;
	}
	
	public String getNmOrgaoExterno() {
		return nmOrgaoExterno;
	}
	
	public void setNmOrgaoExterno(String nmOrgaoExterno) {
		this.nmOrgaoExterno = nmOrgaoExterno;
	}
	
	public String getSgOrgaoExterno() {
		return sgOrgaoExterno;
	}
	
	public void setSgOrgaoExterno(String sgOrgaoExterno) {
		this.sgOrgaoExterno = sgOrgaoExterno;
	}
	
	public int getCdTipoLogradouro() {
		return cdTipoLogradouro;
	}
	
	public void setCdTipoLogradouro(int cdTipoLogradouro) {
		this.cdTipoLogradouro = cdTipoLogradouro;
	}
	
	public String getNmLogradouro() {
		return nmLogradouro;
	}
	
	public void setNmLogradouro(String nmLogradouro) {
		this.nmLogradouro = nmLogradouro;
	}
	
	public int getCdCidade() {
		return cdCidade;
	}
	
	public void setCdCidade(int cdCidade) {
		this.cdCidade = cdCidade;
	}
	
	public String getNmBairro() {
		return nmBairro;
	}
	
	public void setNmBairro(String nmBairro) {
		this.nmBairro = nmBairro;
	}
	
	public String getNrCep() {
		return nrCep;
	}
	
	public void setNrCep(String nrCep) {
		this.nrCep = nrCep;
	}

	public String getNrEndereco() {
		return nrEndereco;
	}

	public void setNrEndereco(String nrEndereco) {
		this.nrEndereco = nrEndereco;
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
