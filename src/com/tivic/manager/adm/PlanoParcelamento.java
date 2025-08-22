package com.tivic.manager.adm;

public class PlanoParcelamento {

	private int cdPlanoPagamento;
	private int nrOrdem;
	private int nrDias;
	private float prValorTotal;
	private int tpIntervalo;
	private int qtParcelas;

	public PlanoParcelamento(int cdPlanoPagamento,
			int nrOrdem,
			int nrDias,
			float prValorTotal,
			int tpIntervalo,
			int qtParcelas){
		setCdPlanoPagamento(cdPlanoPagamento);
		setNrOrdem(nrOrdem);
		setNrDias(nrDias);
		setPrValorTotal(prValorTotal);
		setTpIntervalo(tpIntervalo);
		setQtParcelas(qtParcelas);
	}
	public void setCdPlanoPagamento(int cdPlanoPagamento){
		this.cdPlanoPagamento=cdPlanoPagamento;
	}
	public int getCdPlanoPagamento(){
		return this.cdPlanoPagamento;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setNrDias(int nrDias){
		this.nrDias=nrDias;
	}
	public int getNrDias(){
		return this.nrDias;
	}
	public void setPrValorTotal(float prValorTotal){
		this.prValorTotal=prValorTotal;
	}
	public float getPrValorTotal(){
		return this.prValorTotal;
	}
	public void setTpIntervalo(int tpIntervalo){
		this.tpIntervalo=tpIntervalo;
	}
	public int getTpIntervalo(){
		return this.tpIntervalo;
	}
	public void setQtParcelas(int qtParcelas){
		this.qtParcelas=qtParcelas;
	}
	public int getQtParcelas(){
		return this.qtParcelas;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlanoPagamento: " +  getCdPlanoPagamento();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", nrDias: " +  getNrDias();
		valueToString += ", prValorTotal: " +  getPrValorTotal();
		valueToString += ", tpIntervalo: " +  getTpIntervalo();
		valueToString += ", qtParcelas: " +  getQtParcelas();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoParcelamento(getCdPlanoPagamento(),
			getNrOrdem(),
			getNrDias(),
			getPrValorTotal(),
			getTpIntervalo(),
			getQtParcelas());
	}

}
