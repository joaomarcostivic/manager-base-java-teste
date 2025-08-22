package com.tivic.manager.adapter.base.antiga.agente;

public class AgenteOldBuilder {
	private AgenteOld agenteOld;
	
	public AgenteOldBuilder() {
		this.agenteOld = new AgenteOld();
	}
	
	public AgenteOldBuilder setCodAgente(int codAgente) {
		this.agenteOld.setCodAgente(codAgente);
		return this;
	}
	
	public AgenteOldBuilder setNmAgente(String nmAgente) {
		this.agenteOld.setNmAgente(nmAgente);
		return this;
	}
	
	public AgenteOldBuilder setDsEndereco(String dsEndereco) {
		this.agenteOld.setDsEndereco(dsEndereco);
		return this;
	}
	
	public AgenteOldBuilder setBairro(String bairro) {
		this.agenteOld.setBairro(bairro);
		return this;
	}
	
	public AgenteOldBuilder setNrCep(String nrCep) {
		this.agenteOld.setNrCep(nrCep);
		return this;
	}
	
	public AgenteOldBuilder setCodMunicipio(int codMunicipio) {
		this.agenteOld.setCodMunicipio(codMunicipio);
		return this;
	}
	
	public AgenteOldBuilder setNrMatricula(String nrMatricula) {
		this.agenteOld.setNrMatricula(nrMatricula);
		return this;
	}
	
	public AgenteOldBuilder setCodUsuario(int codUsuario) {
		this.agenteOld.setCodUsuario(codUsuario);
		return this;
	}
	
	public AgenteOldBuilder setTpAgente(int tpAgente) {
		this.agenteOld.setTpAgente(tpAgente);
		return this;
	}
	
	public AgenteOldBuilder setStAgente(int stAgente) {
		this.agenteOld.setStAgente(stAgente);
		return this;
	}
	
	
	public AgenteOld build() {
		return this.agenteOld;
	}
}
