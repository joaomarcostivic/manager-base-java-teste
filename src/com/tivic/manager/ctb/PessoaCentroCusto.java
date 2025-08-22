package com.tivic.manager.ctb;

public class PessoaCentroCusto {

	private int cdPessoaCentroCusto;
	private int cdPessoa;
	private int cdCentroCusto;
	private int cdAreaDireito;

	public PessoaCentroCusto(){ }

	public PessoaCentroCusto(int cdPessoaCentroCusto,
			int cdPessoa,
			int cdCentroCusto,
			int cdAreaDireito){
		setCdPessoaCentroCusto(cdPessoaCentroCusto);
		setCdPessoa(cdPessoa);
		setCdCentroCusto(cdCentroCusto);
		setCdAreaDireito(cdAreaDireito);
	}
	public void setCdPessoaCentroCusto(int cdPessoaCentroCusto){
		this.cdPessoaCentroCusto=cdPessoaCentroCusto;
	}
	public int getCdPessoaCentroCusto(){
		return this.cdPessoaCentroCusto;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdCentroCusto(int cdCentroCusto){
		this.cdCentroCusto=cdCentroCusto;
	}
	public int getCdCentroCusto(){
		return this.cdCentroCusto;
	}
	public void setCdAreaDireito(int cdAreaDireito){
		this.cdAreaDireito=cdAreaDireito;
	}
	public int getCdAreaDireito(){
		return this.cdAreaDireito;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoaCentroCusto: " +  getCdPessoaCentroCusto();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdCentroCusto: " +  getCdCentroCusto();
		valueToString += ", cdAreaDireito: " +  getCdAreaDireito();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaCentroCusto(getCdPessoaCentroCusto(),
			getCdPessoa(),
			getCdCentroCusto(),
			getCdAreaDireito());
	}

}
