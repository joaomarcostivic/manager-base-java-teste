package com.tivic.manager.adapter.base.antiga.agente;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AgenteOld {
	private int codAgente;
	private String nmAgente;
	private String dsEndereco;
	private String bairro;
	private String nrCep;
	private int codMunicipio;
	private String nrMatricula;
	private int codUsuario;
	private int tpAgente;
	private int stAgente;
	
	public AgenteOld() { }

	public AgenteOld(int codAgente,
			String nmAgente,
			String dsEndereco,
			String bairro,
			String nrCep,
			int codMunicipio,
			String nrMatricula,
			int codUsuario,
			int tpAgente,
			int stAgente) {
		setCodAgente(codAgente);
		setNmAgente(nmAgente);
		setDsEndereco(dsEndereco);
		setBairro(bairro);
		setNrCep(nrCep);
		setCodMunicipio(codMunicipio);
		setNrMatricula(nrMatricula);
		setCodUsuario(codUsuario);
		setTpAgente(tpAgente);
		setStAgente(stAgente);
	}
	public void setCodAgente(int codAgente){
		this.codAgente=codAgente;
	}
	public int getCodAgente(){
		return this.codAgente;
	}
	public void setNmAgente(String nmAgente){
		this.nmAgente=nmAgente;
	}
	public String getNmAgente(){
		return this.nmAgente;
	}
	public void setDsEndereco(String dsEndereco){
		this.dsEndereco=dsEndereco;
	}
	public String getDsEndereco(){
		return this.dsEndereco;
	}
	public void setBairro(String bairro){
		this.bairro=bairro;
	}
	public String getBairro(){
		return this.bairro;
	}
	public void setNrCep(String nrCep){
		this.nrCep=nrCep;
	}
	public String getNrCep(){
		return this.nrCep;
	}
	public void setCodMunicipio(int codMunicipio){
		this.codMunicipio=codMunicipio;
	}
	public int getCodMunicipio(){
		return this.codMunicipio;
	}
	public void setNrMatricula(String nrMatricula){
		this.nrMatricula=nrMatricula;
	}
	public String getNrMatricula(){
		return this.nrMatricula;
	}
	public void setCodUsuario(int codUsuario){
		this.codUsuario=codUsuario;
	}
	public int getCodUsuario(){
		return this.codUsuario;
	}
	public void setTpAgente(int tpAgente){
		this.tpAgente=tpAgente;
	}
	public int getTpAgente(){
		return this.tpAgente;
	}
	public void setStAgente(int stAgente){
		this.stAgente=stAgente;
	}
	public int getStAgente(){
		return this.stAgente;
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
}
