package com.tivic.manager.grl;

public class ParametroOpcao {

	private int cdParametro;
	private int cdOpcao;
	private String vlApresentacao;
	private String vlReal;
	private int cdPessoa;
	private int cdEmpresa;

	public ParametroOpcao(int cdParametro,
			int cdOpcao,
			String vlApresentacao,
			String vlReal,
			int cdPessoa,
			int cdEmpresa){
		setCdParametro(cdParametro);
		setCdOpcao(cdOpcao);
		setVlApresentacao(vlApresentacao);
		setVlReal(vlReal);
		setCdPessoa(cdPessoa);
		setCdEmpresa(cdEmpresa);
	}
	public void setCdParametro(int cdParametro){
		this.cdParametro=cdParametro;
	}
	public int getCdParametro(){
		return this.cdParametro;
	}
	public void setCdOpcao(int cdOpcao){
		this.cdOpcao=cdOpcao;
	}
	public int getCdOpcao(){
		return this.cdOpcao;
	}
	public void setVlApresentacao(String vlApresentacao){
		this.vlApresentacao=vlApresentacao;
	}
	public String getVlApresentacao(){
		return this.vlApresentacao;
	}
	public void setVlReal(String vlReal){
		this.vlReal=vlReal;
	}
	public String getVlReal(){
		return this.vlReal;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdParametro: " +  getCdParametro();
		valueToString += ", cdOpcao: " +  getCdOpcao();
		valueToString += ", vlApresentacao: " +  getVlApresentacao();
		valueToString += ", vlReal: " +  getVlReal();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ParametroOpcao(getCdParametro(),
			getCdOpcao(),
			getVlApresentacao(),
			getVlReal(),
			getCdPessoa(),
			getCdEmpresa());
	}

}
