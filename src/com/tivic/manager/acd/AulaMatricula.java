package com.tivic.manager.acd;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AulaMatricula {

	private int cdAula;
	private int lgPresenca;
	private String txtObservacao;
	private int cdMatricula;

	public AulaMatricula() { }

	public AulaMatricula(int cdAula,
			int lgPresenca,
			String txtObservacao,
			int cdMatricula) {
		setCdAula(cdAula);
		setLgPresenca(lgPresenca);
		setTxtObservacao(txtObservacao);
		setCdMatricula(cdMatricula);
	}
	public void setCdAula(int cdAula){
		this.cdAula=cdAula;
	}
	public int getCdAula(){
		return this.cdAula;
	}
	public void setLgPresenca(int lgPresenca){
		this.lgPresenca=lgPresenca;
	}
	public int getLgPresenca(){
		return this.lgPresenca;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAula: " +  getCdAula();
		valueToString += ", lgPresenca: " +  getLgPresenca();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdMatricula: " +  getCdMatricula();
		return "{" + valueToString + "}";
	}
	
	public static String fromRegister(Map<String, Object> register) {
		String valueToString = "";
		valueToString += "\"cdAula\": " +  register.get("CD_AULA");
		valueToString += ", \"lgPresenca\": " + register.get("LG_PRESENCA");
		valueToString += ", \"txtObservacao\": \"" + register.get("TXT_OBSERVACAO")+"\"";
		valueToString += ", \"cdMatricula\": " + register.get("CD_MATRICULA");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AulaMatricula(getCdAula(),
			getLgPresenca(),
			getTxtObservacao(),
			getCdMatricula());
	}

}