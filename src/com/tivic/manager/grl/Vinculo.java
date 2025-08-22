package com.tivic.manager.grl;

public class Vinculo {

	public static final int PARCEIRO = 1;

	private int cdVinculo;
	private String nmVinculo;
	private int lgEstatico;
	private int lgFuncao;
	private int cdFormulario;
	private int lgCadastro;

	public Vinculo() { }
	
	public Vinculo(int cdVinculo,
			String nmVinculo,
			int lgEstatico,
			int lgFuncao,
			int cdFormulario,
			int lgCadastro){
		setCdVinculo(cdVinculo);
		setNmVinculo(nmVinculo);
		setLgEstatico(lgEstatico);
		setLgFuncao(lgFuncao);
		setCdFormulario(cdFormulario);
		setLgCadastro(lgCadastro);
	}
	public void setCdVinculo(int cdVinculo){
		this.cdVinculo=cdVinculo;
	}
	public int getCdVinculo(){
		return this.cdVinculo;
	}
	public void setNmVinculo(String nmVinculo){
		this.nmVinculo=nmVinculo;
	}
	public String getNmVinculo(){
		return this.nmVinculo;
	}
	public void setLgEstatico(int lgEstatico){
		this.lgEstatico=lgEstatico;
	}
	public int getLgEstatico(){
		return this.lgEstatico;
	}
	public void setLgFuncao(int lgFuncao){
		this.lgFuncao=lgFuncao;
	}
	public int getLgFuncao(){
		return this.lgFuncao;
	}
	public void setCdFormulario(int cdFormulario){
		this.cdFormulario=cdFormulario;
	}
	public int getCdFormulario(){
		return this.cdFormulario;
	}
	public void setLgCadastro(int lgCadastro){
		this.lgCadastro=lgCadastro;
	}
	public int getLgCadastro(){
		return this.lgCadastro;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdVinculo: " +  getCdVinculo();
		valueToString += ", nmVinculo: " +  getNmVinculo();
		valueToString += ", lgEstatico: " +  getLgEstatico();
		valueToString += ", lgFuncao: " +  getLgFuncao();
		valueToString += ", cdFormulario: " +  getCdFormulario();
		valueToString += ", lgCadastro: " +  getLgCadastro();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Vinculo(getCdVinculo(),
			getNmVinculo(),
			getLgEstatico(),
			getLgFuncao(),
			getCdFormulario(),
			getLgCadastro());
	}

}
