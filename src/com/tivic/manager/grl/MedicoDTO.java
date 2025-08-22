package com.tivic.manager.grl;

public class MedicoDTO {
	
	int cdPessoa;
	String nmPessoa;
	int cdVinculo;
	int cdEmpresa;
	
	public MedicoDTO() {
		
	}
	
	public MedicoDTO(int cdPessoa, String nmPessoa, int cdVinculo, int cdEmpresa) {
		this.cdPessoa = cdPessoa;
		this.nmPessoa = nmPessoa;
		this.cdVinculo = cdVinculo;
		this.cdEmpresa = cdEmpresa;
	}

	public int getCdPessoa() {
		return cdPessoa;
	}

	public void setCdPessoa(int cdPessoa) {
		this.cdPessoa = cdPessoa;
	}

	public String getNmPessoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}

	public int getCdVinculo() {
		return cdVinculo;
	}

	public void setCdVinculo(int cdVinculo) {
		this.cdVinculo = cdVinculo;
	}

	public int getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(int cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}
	
	
}