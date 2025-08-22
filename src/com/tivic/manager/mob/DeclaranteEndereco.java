package com.tivic.manager.mob;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeclaranteEndereco {

	private int cdDeclarante;
	private int cdCidade;
	private String nrCep;
	private String nmLogradouro;
	private String nrEndereco;
	private String nmBairro;
	private String dsComplemento;

	public DeclaranteEndereco() { }

	public DeclaranteEndereco(int cdDeclarante,
			int cdCidade,
			String nrCep,
			String nmLogradouro,
			String nrEndereco,
			String nmBairro,
			String dsComplemento) {
		setCdDeclarante(cdDeclarante);
		setCdCidade(cdCidade);
		setNrCep(nrCep);
		setNmLogradouro(nmLogradouro);
		setNrEndereco(nrEndereco);
		setNmBairro(nmBairro);
		setDsComplemento(dsComplemento);
	}
	public void setCdDeclarante(int cdDeclarante){
		this.cdDeclarante=cdDeclarante;
	}
	public int getCdDeclarante(){
		return this.cdDeclarante;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setNrCep(String nrCep){
		this.nrCep=nrCep;
	}
	public String getNrCep(){
		return this.nrCep;
	}
	public void setNmLogradouro(String nmLogradouro){
		this.nmLogradouro=nmLogradouro;
	}
	public String getNmLogradouro(){
		return this.nmLogradouro;
	}
	public void setNrEndereco(String nrEndereco){
		this.nrEndereco=nrEndereco;
	}
	public String getNrEndereco(){
		return this.nrEndereco;
	}
	public void setNmBairro(String nmBairro){
		this.nmBairro=nmBairro;
	}
	public String getNmBairro(){
		return this.nmBairro;
	}
	public void setDsComplemento(String dsComplemento){
		this.dsComplemento=dsComplemento;
	}
	public String getDsComplemento(){
		return this.dsComplemento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDeclarante: " +  getCdDeclarante();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", nrCep: " +  getNrCep();
		valueToString += ", nmLogradouro: " +  getNmLogradouro();
		valueToString += ", nrEndereco: " +  getNrEndereco();
		valueToString += ", nmBairro: " +  getNmBairro();
		valueToString += ", dsComplemento: " +  getDsComplemento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DeclaranteEndereco(getCdDeclarante(),
			getCdCidade(),
			getNrCep(),
			getNmLogradouro(),
			getNrEndereco(),
			getNmBairro(),
			getDsComplemento());
	}

}