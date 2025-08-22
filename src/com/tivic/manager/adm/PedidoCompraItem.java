package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class PedidoCompraItem {

	private int cdEmpresa;
	private int cdProdutoServico;
	private int cdPedidoCompra;
	private int cdUnidadeMedida;
	private float qtSolicitada;
	private float qtAtendida;
	private int nrPrazoEntrega;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private int lgAtivo;
	private String txtPedidoItem;

	public PedidoCompraItem(int cdEmpresa,
			int cdProdutoServico,
			int cdPedidoCompra,
			int cdUnidadeMedida,
			float qtSolicitada,
			float qtAtendida,
			int nrPrazoEntrega,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			int lgAtivo,
			String txtPedidoItem){
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setCdPedidoCompra(cdPedidoCompra);
		setCdUnidadeMedida(cdUnidadeMedida);
		setQtSolicitada(qtSolicitada);
		setQtAtendida(qtAtendida);
		setNrPrazoEntrega(nrPrazoEntrega);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setLgAtivo(lgAtivo);
		setTxtPedidoItem(txtPedidoItem);
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdPedidoCompra(int cdPedidoCompra){
		this.cdPedidoCompra=cdPedidoCompra;
	}
	public int getCdPedidoCompra(){
		return this.cdPedidoCompra;
	}
	public void setCdUnidadeMedida(int cdUnidadeMedida){
		this.cdUnidadeMedida=cdUnidadeMedida;
	}
	public int getCdUnidadeMedida(){
		return this.cdUnidadeMedida;
	}
	public void setQtSolicitada(float qtSolicitada){
		this.qtSolicitada=qtSolicitada;
	}
	public float getQtSolicitada(){
		return this.qtSolicitada;
	}
	public void setQtAtendida(float qtAtendida){
		this.qtAtendida=qtAtendida;
	}
	public float getQtAtendida(){
		return this.qtAtendida;
	}
	public void setNrPrazoEntrega(int nrPrazoEntrega){
		this.nrPrazoEntrega=nrPrazoEntrega;
	}
	public int getNrPrazoEntrega(){
		return this.nrPrazoEntrega;
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
	public void setLgAtivo(int lgAtivo){
		this.lgAtivo=lgAtivo;
	}
	public int getLgAtivo(){
		return this.lgAtivo;
	}
	public void setTxtPedidoItem(String txtPedidoItem){
		this.txtPedidoItem=txtPedidoItem;
	}
	public String getTxtPedidoItem(){
		return this.txtPedidoItem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdPedidoCompra: " +  getCdPedidoCompra();
		valueToString += ", cdUnidadeMedida: " +  getCdUnidadeMedida();
		valueToString += ", qtSolicitada: " +  getQtSolicitada();
		valueToString += ", qtAtendida: " +  getQtAtendida();
		valueToString += ", nrPrazoEntrega: " +  getNrPrazoEntrega();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgAtivo: " +  getLgAtivo();
		valueToString += ", txtPedidoItem: " +  getTxtPedidoItem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PedidoCompraItem(getCdEmpresa(),
			getCdProdutoServico(),
			getCdPedidoCompra(),
			getCdUnidadeMedida(),
			getQtSolicitada(),
			getQtAtendida(),
			getNrPrazoEntrega(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getLgAtivo(),
			getTxtPedidoItem());
	}

}
