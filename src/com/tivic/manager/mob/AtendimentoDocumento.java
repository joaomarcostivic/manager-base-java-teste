package com.tivic.manager.mob;

public class AtendimentoDocumento {

	private int cdConcessao;
	private int cdDocumento;
	private int cdLinha;
	private int cdRota;
	private int cdVeiculo;

	public AtendimentoDocumento() { }

	public AtendimentoDocumento(int cdConcessao,
			int cdDocumento,
			int cdLinha,
			int cdRota,
			int cdVeiculo) {
		setCdConcessao(cdConcessao);
		setCdDocumento(cdDocumento);
		setCdLinha(cdLinha);
		setCdRota(cdRota);
		setCdVeiculo(cdVeiculo);
	}
	public void setCdConcessao(int cdConcessao){
		this.cdConcessao=cdConcessao;
	}
	public int getCdConcessao(){
		return this.cdConcessao;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setCdLinha(int cdLinha){
		this.cdLinha=cdLinha;
	}
	public int getCdLinha(){
		return this.cdLinha;
	}
	public void setCdRota(int cdRota){
		this.cdRota=cdRota;
	}
	public int getCdRota(){
		return this.cdRota;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConcessao: " +  getCdConcessao();
		valueToString += ", cdDocumento: " +  getCdDocumento();
		valueToString += ", cdLinha: " +  getCdLinha();
		valueToString += ", cdRota: " +  getCdRota();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AtendimentoDocumento(getCdConcessao(),
			getCdDocumento(),
			getCdLinha(),
			getCdRota(),
			getCdVeiculo());
	}

}