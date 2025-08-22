package com.tivic.manager.mob;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Agente implements Serializable {
	private static final long serialVersionUID = -513252839160960513L;
	
	private int cdAgente;
	private int cdUsuario;
	private String nmAgente;
	private String dsEndereco;
	private String nmBairro;
	private String nrCep;
	private int cdCidade;
	private String nrMatricula;
	private int tpAgente;
	private int cdOrgao;
	private int stAgente;

	public Agente(){ }

	public Agente(int cdAgente,
			int cdUsuario,
			String nmAgente,
			String dsEndereco,
			String nmBairro,
			String nrCep,
			int cdCidade,
			String nrMatricula,
			int tpAgente){
		setCdAgente(cdAgente);
		setCdUsuario(cdUsuario);
		setNmAgente(nmAgente);
		setDsEndereco(dsEndereco);
		setNmBairro(nmBairro);
		setNrCep(nrCep);
		setCdCidade(cdCidade);
		setNrMatricula(nrMatricula);
		setTpAgente(tpAgente);
	}
	
	public Agente(int cdAgente,
			int cdUsuario,
			String nmAgente,
			String dsEndereco,
			String nmBairro,
			String nrCep,
			int cdCidade,
			String nrMatricula,
			int tpAgente,
			int cdOrgao,
			int stAgente){
		setCdAgente(cdAgente);
		setCdUsuario(cdUsuario);
		setNmAgente(nmAgente);
		setDsEndereco(dsEndereco);
		setNmBairro(nmBairro);
		setNrCep(nrCep);
		setCdCidade(cdCidade);
		setNrMatricula(nrMatricula);
		setTpAgente(tpAgente);
		setCdOrgao(cdOrgao);
		setStAgente(stAgente);
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
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
	public void setNmBairro(String nmBairro){
		this.nmBairro=nmBairro;
	}
	public String getNmBairro(){
		return this.nmBairro;
	}
	public void setNrCep(String nrCep){
		this.nrCep=nrCep;
	}
	public String getNrCep(){
		return this.nrCep;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setNrMatricula(String nrMatricula){
		this.nrMatricula=nrMatricula;
	}
	public String getNrMatricula(){
		return this.nrMatricula;
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
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAgente: " +  getCdAgente();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", nmAgente: " +  getNmAgente();
		valueToString += ", dsEndereco: " +  getDsEndereco();
		valueToString += ", nmBairro: " +  getNmBairro();
		valueToString += ", nrCep: " +  getNrCep();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", nrMatricula: " +  getNrMatricula();
		valueToString += ", tpAgente: " +  getTpAgente();
		valueToString += ", cdOrgao: " +  getCdOrgao();
		valueToString += ", stAgente: " +  getStAgente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Agente(getCdAgente(),
			getCdUsuario(),
			getNmAgente(),
			getDsEndereco(),
			getNmBairro(),
			getNrCep(),
			getCdCidade(),
			getNrMatricula(),
			getTpAgente(),
			getCdOrgao(),
			getStAgente());
	}

}