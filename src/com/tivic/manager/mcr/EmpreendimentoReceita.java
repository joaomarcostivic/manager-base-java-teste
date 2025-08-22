package com.tivic.manager.mcr;

public class EmpreendimentoReceita {

	private int cdEmpreendimento;
	private int cdReceita;
	private String nmProduto;
	private int lgReceita;
	private float vlPrecoUnitario;
	private float qtVendida;
	private String sgUnidade;

	public EmpreendimentoReceita(int cdEmpreendimento,
			int cdReceita,
			String nmProduto,
			int lgReceita,
			float vlPrecoUnitario,
			float qtVendida,
			String sgUnidade){
		setCdEmpreendimento(cdEmpreendimento);
		setCdReceita(cdReceita);
		setNmProduto(nmProduto);
		setLgReceita(lgReceita);
		setVlPrecoUnitario(vlPrecoUnitario);
		setQtVendida(qtVendida);
		setSgUnidade(sgUnidade);
	}
	public void setCdEmpreendimento(int cdEmpreendimento){
		this.cdEmpreendimento=cdEmpreendimento;
	}
	public int getCdEmpreendimento(){
		return this.cdEmpreendimento;
	}
	public void setCdReceita(int cdReceita){
		this.cdReceita=cdReceita;
	}
	public int getCdReceita(){
		return this.cdReceita;
	}
	public void setNmProduto(String nmProduto){
		this.nmProduto=nmProduto;
	}
	public String getNmProduto(){
		return this.nmProduto;
	}
	public void setLgReceita(int lgReceita){
		this.lgReceita=lgReceita;
	}
	public int getLgReceita(){
		return this.lgReceita;
	}
	public void setVlPrecoUnitario(float vlPrecoUnitario){
		this.vlPrecoUnitario=vlPrecoUnitario;
	}
	public float getVlPrecoUnitario(){
		return this.vlPrecoUnitario;
	}
	public void setQtVendida(float qtVendida){
		this.qtVendida=qtVendida;
	}
	public float getQtVendida(){
		return this.qtVendida;
	}
	public void setSgUnidade(String sgUnidade){
		this.sgUnidade=sgUnidade;
	}
	public String getSgUnidade(){
		return this.sgUnidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpreendimento: " +  getCdEmpreendimento();
		valueToString += ", cdReceita: " +  getCdReceita();
		valueToString += ", nmProduto: " +  getNmProduto();
		valueToString += ", lgReceita: " +  getLgReceita();
		valueToString += ", vlPrecoUnitario: " +  getVlPrecoUnitario();
		valueToString += ", qtVendida: " +  getQtVendida();
		valueToString += ", sgUnidade: " +  getSgUnidade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EmpreendimentoReceita(cdEmpreendimento,
			cdReceita,
			nmProduto,
			lgReceita,
			vlPrecoUnitario,
			qtVendida,
			sgUnidade);
	}

}