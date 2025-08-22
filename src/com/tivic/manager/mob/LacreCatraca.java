package com.tivic.manager.mob;

public class LacreCatraca {

	private int cdLacreCatraca;
	private int cdLacre;
	private int cdConcessaoVeiculo;
	private int cdAfericaoAplicacao;
	private int cdAfericaoRemocao;

	public LacreCatraca(){ }

	public LacreCatraca(int cdLacreCatraca,
			int cdLacre,
			int cdConcessaoVeiculo,
			int cdAfericaoAplicacao,
			int cdAfericaoRemocao){
		setCdLacreCatraca(cdLacreCatraca);
		setCdLacre(cdLacre);
		setCdConcessaoVeiculo(cdConcessaoVeiculo);
		setCdAfericaoAplicacao(cdAfericaoAplicacao);
		setCdAfericaoRemocao(cdAfericaoRemocao);
	}
	public void setCdLacreCatraca(int cdLacreCatraca){
		this.cdLacreCatraca=cdLacreCatraca;
	}
	public int getCdLacreCatraca(){
		return this.cdLacreCatraca;
	}
	public void setCdLacre(int cdLacre){
		this.cdLacre=cdLacre;
	}
	public int getCdLacre(){
		return this.cdLacre;
	}
	public void setCdConcessaoVeiculo(int cdConcessaoVeiculo){
		this.cdConcessaoVeiculo=cdConcessaoVeiculo;
	}
	public int getCdConcessaoVeiculo(){
		return this.cdConcessaoVeiculo;
	}
	public void setCdAfericaoAplicacao(int cdAfericaoAplicacao){
		this.cdAfericaoAplicacao=cdAfericaoAplicacao;
	}
	public int getCdAfericaoAplicacao(){
		return this.cdAfericaoAplicacao;
	}
	public void setCdAfericaoRemocao(int cdAfericaoRemocao){
		this.cdAfericaoRemocao=cdAfericaoRemocao;
	}
	public int getCdAfericaoRemocao(){
		return this.cdAfericaoRemocao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLacreCatraca: " +  getCdLacreCatraca();
		valueToString += ", cdLacre: " +  getCdLacre();
		valueToString += ", cdConcessaoVeiculo: " +  getCdConcessaoVeiculo();
		valueToString += ", cdAfericaoAplicacao: " +  getCdAfericaoAplicacao();
		valueToString += ", cdAfericaoRemocao: " +  getCdAfericaoRemocao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new LacreCatraca(getCdLacreCatraca(),
			getCdLacre(),
			getCdConcessaoVeiculo(),
			getCdAfericaoAplicacao(),
			getCdAfericaoRemocao());
	}

}