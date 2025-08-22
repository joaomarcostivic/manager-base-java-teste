package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class PedidoCompra {

	private int cdPedidoCompra;
	private int cdEmpresa;
	private String nmPedidoCompra;
	private String txtPedidoCompra;
	private int nrPrazoEntrega;
	private GregorianCalendar dtPedidoCompra;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private int lgWeb;
	private int lgFornecedorSelecionado;
	private int tpPedidoCompra;
	private int stPedidoCompra;
	private String idPedidoCompra;
	private String nrPedidoCompra;

	public PedidoCompra(int cdPedidoCompra,
			int cdEmpresa,
			String nmPedidoCompra,
			String txtPedidoCompra,
			int nrPrazoEntrega,
			GregorianCalendar dtPedidoCompra,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			int lgWeb,
			int lgFornecedorSelecionado,
			int tpPedidoCompra,
			int stPedidoCompra,
			String idPedidoCompra,
			String nrPedidoCompra){
		setCdPedidoCompra(cdPedidoCompra);
		setCdEmpresa(cdEmpresa);
		setNmPedidoCompra(nmPedidoCompra);
		setTxtPedidoCompra(txtPedidoCompra);
		setNrPrazoEntrega(nrPrazoEntrega);
		setDtPedidoCompra(dtPedidoCompra);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setLgWeb(lgWeb);
		setLgFornecedorSelecionado(lgFornecedorSelecionado);
		setTpPedidoCompra(tpPedidoCompra);
		setStPedidoCompra(stPedidoCompra);
		setIdPedidoCompra(idPedidoCompra);
		setNrPedidoCompra(nrPedidoCompra);
	}
	public void setCdPedidoCompra(int cdPedidoCompra){
		this.cdPedidoCompra=cdPedidoCompra;
	}
	public int getCdPedidoCompra(){
		return this.cdPedidoCompra;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setNmPedidoCompra(String nmPedidoCompra){
		this.nmPedidoCompra=nmPedidoCompra;
	}
	public String getNmPedidoCompra(){
		return this.nmPedidoCompra;
	}
	public void setTxtPedidoCompra(String txtPedidoCompra){
		this.txtPedidoCompra=txtPedidoCompra;
	}
	public String getTxtPedidoCompra(){
		return this.txtPedidoCompra;
	}
	public void setNrPrazoEntrega(int nrPrazoEntrega){
		this.nrPrazoEntrega=nrPrazoEntrega;
	}
	public int getNrPrazoEntrega(){
		return this.nrPrazoEntrega;
	}
	public void setDtPedidoCompra(GregorianCalendar dtPedidoCompra){
		this.dtPedidoCompra=dtPedidoCompra;
	}
	public GregorianCalendar getDtPedidoCompra(){
		return this.dtPedidoCompra;
	}
	public void setDtInicial(GregorianCalendar dtInicial){
		this.dtInicial=dtInicial;
	}
	public GregorianCalendar getDtInicial(){
		return this.dtInicial;
	}
	public void setDtFinal(GregorianCalendar dtFinal){
		this.dtFinal=dtFinal;
	}
	public GregorianCalendar getDtFinal(){
		return this.dtFinal;
	}
	public void setLgWeb(int lgWeb){
		this.lgWeb=lgWeb;
	}
	public int getLgWeb(){
		return this.lgWeb;
	}
	public void setLgFornecedorSelecionado(int lgFornecedorSelecionado){
		this.lgFornecedorSelecionado=lgFornecedorSelecionado;
	}
	public int getLgFornecedorSelecionado(){
		return this.lgFornecedorSelecionado;
	}
	public void setTpPedidoCompra(int tpPedidoCompra){
		this.tpPedidoCompra=tpPedidoCompra;
	}
	public int getTpPedidoCompra(){
		return this.tpPedidoCompra;
	}
	public void setStPedidoCompra(int stPedidoCompra){
		this.stPedidoCompra=stPedidoCompra;
	}
	public int getStPedidoCompra(){
		return this.stPedidoCompra;
	}
	public void setIdPedidoCompra(String idPedidoCompra){
		this.idPedidoCompra=idPedidoCompra;
	}
	public String getIdPedidoCompra(){
		return this.idPedidoCompra;
	}
	public void setNrPedidoCompra(String nrPedidoCompra){
		this.nrPedidoCompra=nrPedidoCompra;
	}
	public String getNrPedidoCompra(){
		return this.nrPedidoCompra;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPedidoCompra: " +  getCdPedidoCompra();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", nmPedidoCompra: " +  getNmPedidoCompra();
		valueToString += ", txtPedidoCompra: " +  getTxtPedidoCompra();
		valueToString += ", nrPrazoEntrega: " +  getNrPrazoEntrega();
		valueToString += ", dtPedidoCompra: " +  sol.util.Util.formatDateTime(getDtPedidoCompra(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgWeb: " +  getLgWeb();
		valueToString += ", lgFornecedorSelecionado: " +  getLgFornecedorSelecionado();
		valueToString += ", tpPedidoCompra: " +  getTpPedidoCompra();
		valueToString += ", stPedidoCompra: " +  getStPedidoCompra();
		valueToString += ", idPedidoCompra: " +  getIdPedidoCompra();
		valueToString += ", nrPedidoCompra: " +  getNrPedidoCompra();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PedidoCompra(getCdPedidoCompra(),
			getCdEmpresa(),
			getNmPedidoCompra(),
			getTxtPedidoCompra(),
			getNrPrazoEntrega(),
			getDtPedidoCompra()==null ? null : (GregorianCalendar)getDtPedidoCompra().clone(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getLgWeb(),
			getLgFornecedorSelecionado(),
			getTpPedidoCompra(),
			getStPedidoCompra(),
			getIdPedidoCompra(),
			getNrPedidoCompra());
	}

}
