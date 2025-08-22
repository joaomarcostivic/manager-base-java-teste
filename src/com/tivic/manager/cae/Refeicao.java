package com.tivic.manager.cae;

public class Refeicao {

	private int cdCardapio;
	private int cdPreparacao;
	private int cdRefeicao;
	private int nrDia;
	private int tpHorario;

	public Refeicao(){ }

	public Refeicao(int cdCardapio,
			int cdPreparacao,
			int cdRefeicao,
			int nrDia,
			int tpHorario){
		setCdCardapio(cdCardapio);
		setCdPreparacao(cdPreparacao);
		setCdRefeicao(cdRefeicao);
		setNrDia(nrDia);
		setTpHorario(tpHorario);
	}
	public void setCdCardapio(int cdCardapio){
		this.cdCardapio=cdCardapio;
	}
	public int getCdCardapio(){
		return this.cdCardapio;
	}
	public void setCdPreparacao(int cdPreparacao){
		this.cdPreparacao=cdPreparacao;
	}
	public int getCdPreparacao(){
		return this.cdPreparacao;
	}
	public void setCdRefeicao(int cdRefeicao){
		this.cdRefeicao=cdRefeicao;
	}
	public int getCdRefeicao(){
		return this.cdRefeicao;
	}
	public void setNrDia(int nrDia){
		this.nrDia=nrDia;
	}
	public int getNrDia(){
		return this.nrDia;
	}
	public void setTpHorario(int tpHorario){
		this.tpHorario=tpHorario;
	}
	public int getTpHorario(){
		return this.tpHorario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCardapio: " +  getCdCardapio();
		valueToString += ", cdPreparacao: " +  getCdPreparacao();
		valueToString += ", cdRefeicao: " +  getCdRefeicao();
		valueToString += ", nrDia: " +  getNrDia();
		valueToString += ", tpHorario: " +  getTpHorario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Refeicao(getCdCardapio(),
			getCdPreparacao(),
			getCdRefeicao(),
			getNrDia(),
			getTpHorario());
	}

}
