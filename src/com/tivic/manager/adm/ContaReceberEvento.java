package com.tivic.manager.adm;

public class ContaReceberEvento {

	private int cdContaReceber;
	private int cdEventoFinanceiro;
	private float vlEventoFinanceiro;
	private int cdContaReceberEvento;
	private int cdPessoa;
	private int cdContrato;
	private int stEvento;

	public ContaReceberEvento(int cdContaReceber,
			int cdEventoFinanceiro,
			float vlEventoFinanceiro,
			int cdContaReceberEvento,
			int cdPessoa,
			int cdContrato,
			int stEvento){
		setCdContaReceber(cdContaReceber);
		setCdEventoFinanceiro(cdEventoFinanceiro);
		setVlEventoFinanceiro(vlEventoFinanceiro);
		setCdContaReceberEvento(cdContaReceberEvento);
		setCdPessoa(cdPessoa);
		setCdContrato(cdContrato);
		setStEvento(stEvento);
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public void setCdEventoFinanceiro(int cdEventoFinanceiro){
		this.cdEventoFinanceiro=cdEventoFinanceiro;
	}
	public int getCdEventoFinanceiro(){
		return this.cdEventoFinanceiro;
	}
	public void setVlEventoFinanceiro(float vlEventoFinanceiro){
		this.vlEventoFinanceiro=vlEventoFinanceiro;
	}
	public float getVlEventoFinanceiro(){
		return this.vlEventoFinanceiro;
	}
	public void setCdContaReceberEvento(int cdContaReceberEvento){
		this.cdContaReceberEvento=cdContaReceberEvento;
	}
	public int getCdContaReceberEvento(){
		return this.cdContaReceberEvento;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setStEvento(int stEvento){
		this.stEvento=stEvento;
	}
	public int getStEvento(){
		return this.stEvento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContaReceber: " +  getCdContaReceber();
		valueToString += ", cdEventoFinanceiro: " +  getCdEventoFinanceiro();
		valueToString += ", vlEventoFinanceiro: " +  getVlEventoFinanceiro();
		valueToString += ", cdContaReceberEvento: " +  getCdContaReceberEvento();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdContrato: " +  getCdContrato();
		valueToString += ", stEvento: " +  getStEvento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaReceberEvento(getCdContaReceber(),
			getCdEventoFinanceiro(),
			getVlEventoFinanceiro(),
			getCdContaReceberEvento(),
			getCdPessoa(),
			getCdContrato(),
			getStEvento());
	}

}
