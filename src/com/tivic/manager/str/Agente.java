
package com.tivic.manager.str;

public class Agente {

	private int cdAgente;
	private String nmAgente;
	private String dsEndereco;
	private String nmBairro;
	private String nrCep;
	private int cdMunicipio;
	private String nrMatricula;
	private int cdUsuario;
	private int tpAgente;

	public Agente(){ }

	public Agente(int cdAgente,
			String nmAgente,
			String dsEndereco,
			String nmBairro,
			String nrCep,
			int cdMunicipio,
			String nrMatricula,
			int cdUsuario,
			int tpAgente){
		setCdAgente(cdAgente);
		setNmAgente(nmAgente);
		setDsEndereco(dsEndereco);
		setNmBairro(nmBairro);
		setNrCep(nrCep);
		setCdMunicipio(cdMunicipio);
		setNrMatricula(nrMatricula);
		setCdUsuario(cdUsuario);
		setTpAgente(tpAgente);
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
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
	public void setCdMunicipio(int cdMunicipio){
		this.cdMunicipio=cdMunicipio;
	}
	public int getCdMunicipio(){
		return this.cdMunicipio;
	}
	public void setNrMatricula(String nrMatricula){
		this.nrMatricula=nrMatricula;
	}
	public String getNrMatricula(){
		return this.nrMatricula;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setTpAgente(int tpAgente){
		this.tpAgente=tpAgente;
	}
	public int getTpAgente(){
		return this.tpAgente;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "codAgente: " +  getCdAgente();
		valueToString += ", nmAgente: " +  getNmAgente();
		valueToString += ", dsEndereco: " +  getDsEndereco();
		valueToString += ", bairro: " +  getNmBairro();
		valueToString += ", nrCep: " +  getNrCep();
		valueToString += ", codMunicipio: " +  getCdMunicipio();
		valueToString += ", nrMatricula: " +  getNrMatricula();
		valueToString += ", codUsuario: " +  getCdUsuario();
		valueToString += ", tpAgente: " +  getTpAgente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Agente(getCdAgente(),
			getNmAgente(),
			getDsEndereco(),
			getNmBairro(),
			getNrCep(),
			getCdMunicipio(),
			getNrMatricula(),
			getCdUsuario(),
			getTpAgente());
	}

}