package com.tivic.manager.grl;

public class ParametroValor {

	private int cdParametro;
	private int cdValor;
	private int cdOpcao;
	private int cdEmpresa;
	private int cdPessoa;
	private byte[] blbValor;
	private String vlInicial;
	private String vlFinal;

	public ParametroValor() { }
	
	public ParametroValor(int cdParametro,
			int cdValor,
			int cdOpcao,
			int cdEmpresa,
			int cdPessoa,
			byte[] blbValor,
			String vlInicial,
			String vlFinal){
		setCdParametro(cdParametro);
		setCdValor(cdValor);
		setCdOpcao(cdOpcao);
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setBlbValor(blbValor);
		setVlInicial(vlInicial);
		setVlFinal(vlFinal);
	}
	
	public ParametroValor(ParametroValor paramVal) {
		setCdParametro(paramVal.getCdParametro());
		setCdValor(paramVal.getCdValor());
		setCdOpcao(paramVal.getCdOpcao());
		setCdEmpresa(paramVal.getCdEmpresa());
		setCdPessoa(paramVal.getCdPessoa());
		setBlbValor(paramVal.getBlbValor());
		setVlInicial(paramVal.getVlInicial());
		setVlFinal(paramVal.getVlFinal());
	}
	public void setCdParametro(int cdParametro){
		this.cdParametro=cdParametro;
	}
	public int getCdParametro(){
		return this.cdParametro;
	}
	public void setCdValor(int cdValor){
		this.cdValor=cdValor;
	}
	public int getCdValor(){
		return this.cdValor;
	}
	public void setCdOpcao(int cdOpcao){
		this.cdOpcao=cdOpcao;
	}
	public int getCdOpcao(){
		return this.cdOpcao;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setBlbValor(byte[] blbValor){
		this.blbValor=blbValor;
	}
	public byte[] getBlbValor(){
		return this.blbValor;
	}
	public void setVlInicial(String vlInicial){
		this.vlInicial=vlInicial;
	}
	public String getVlInicial(){
		return this.vlInicial;
	}
	public void setVlFinal(String vlFinal){
		this.vlFinal=vlFinal;
	}
	public String getVlFinal(){
		return this.vlFinal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdParametro: " +  getCdParametro();
		valueToString += ", cdValor: " +  getCdValor();
		valueToString += ", cdOpcao: " +  getCdOpcao();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", blbValor: " +  getBlbValor();
		valueToString += ", vlInicial: " +  getVlInicial();
		valueToString += ", vlFinal: " +  getVlFinal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ParametroValor(getCdParametro(),
			getCdValor(),
			getCdOpcao(),
			getCdEmpresa(),
			getCdPessoa(),
			getBlbValor(),
			getVlInicial(),
			getVlFinal());
	}

}
