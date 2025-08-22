package com.tivic.manager.alm;

import java.util.GregorianCalendar;

public class SaidaLocalItem {

	private int cdSaida;
	private int cdProdutoServico;
	private int cdDocumentoSaida;
	private int cdEmpresa;
	private int cdLocalArmazenamento;
	private int cdPedidoVenda;
	private GregorianCalendar dtSaida;
	private float qtSaida;
	private float qtSaidaConsignada;
	private int stSaidaLocalItem;
	private String idSaidaLocalItem;
	private int cdItem;

	public SaidaLocalItem(){}
	
	public SaidaLocalItem(int cdSaida,
			int cdProdutoServico,
			int cdDocumentoSaida,
			int cdEmpresa,
			int cdLocalArmazenamento,
			int cdPedidoVenda,
			GregorianCalendar dtSaida,
			float qtSaida,
			float qtSaidaConsignada,
			int stSaidaLocalItem,
			String idSaidaLocalItem,
			int cdItem){
		setCdSaida(cdSaida);
		setCdProdutoServico(cdProdutoServico);
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdEmpresa(cdEmpresa);
		setCdLocalArmazenamento(cdLocalArmazenamento);
		setCdPedidoVenda(cdPedidoVenda);
		setDtSaida(dtSaida);
		setQtSaida(qtSaida);
		setQtSaidaConsignada(qtSaidaConsignada);
		setStSaidaLocalItem(stSaidaLocalItem);
		setIdSaidaLocalItem(idSaidaLocalItem);
		setCdItem(cdItem);
	}
	public void setCdSaida(int cdSaida){
		this.cdSaida=cdSaida;
	}
	public int getCdSaida(){
		return this.cdSaida;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdDocumentoSaida(int cdDocumentoSaida){
		this.cdDocumentoSaida=cdDocumentoSaida;
	}
	public int getCdDocumentoSaida(){
		return this.cdDocumentoSaida;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdLocalArmazenamento(int cdLocalArmazenamento){
		this.cdLocalArmazenamento=cdLocalArmazenamento;
	}
	public int getCdLocalArmazenamento(){
		return this.cdLocalArmazenamento;
	}
	public void setCdPedidoVenda(int cdPedidoVenda){
		this.cdPedidoVenda=cdPedidoVenda;
	}
	public int getCdPedidoVenda(){
		return this.cdPedidoVenda;
	}
	public void setDtSaida(GregorianCalendar dtSaida){
		this.dtSaida=dtSaida;
	}
	public GregorianCalendar getDtSaida(){
		return this.dtSaida;
	}
	public void setQtSaida(float qtSaida){
		this.qtSaida=qtSaida;
	}
	public float getQtSaida(){
		return this.qtSaida;
	}
	public void setQtSaidaConsignada(float qtSaidaConsignada){
		this.qtSaidaConsignada=qtSaidaConsignada;
	}
	public float getQtSaidaConsignada(){
		return this.qtSaidaConsignada;
	}
	public void setStSaidaLocalItem(int stSaidaLocalItem){
		this.stSaidaLocalItem=stSaidaLocalItem;
	}
	public int getStSaidaLocalItem(){
		return this.stSaidaLocalItem;
	}
	public void setIdSaidaLocalItem(String idSaidaLocalItem){
		this.idSaidaLocalItem=idSaidaLocalItem;
	}
	public String getIdSaidaLocalItem(){
		return this.idSaidaLocalItem;
	}
	public void setCdItem(int cdItem){
		this.cdItem=cdItem;
	}
	public int getCdItem(){
		return this.cdItem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdSaida: " +  getCdSaida();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdLocalArmazenamento: " +  getCdLocalArmazenamento();
		valueToString += ", cdPedidoVenda: " +  getCdPedidoVenda();
		valueToString += ", dtSaida: " +  sol.util.Util.formatDateTime(getDtSaida(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", qtSaida: " +  getQtSaida();
		valueToString += ", qtSaidaConsignada: " +  getQtSaidaConsignada();
		valueToString += ", stSaidaLocalItem: " +  getStSaidaLocalItem();
		valueToString += ", idSaidaLocalItem: " +  getIdSaidaLocalItem();
		valueToString += ", cdItem: " +  getCdItem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new SaidaLocalItem(getCdSaida(),
			getCdProdutoServico(),
			getCdDocumentoSaida(),
			getCdEmpresa(),
			getCdLocalArmazenamento(),
			getCdPedidoVenda(),
			getDtSaida()==null ? null : (GregorianCalendar)getDtSaida().clone(),
			getQtSaida(),
			getQtSaidaConsignada(),
			getStSaidaLocalItem(),
			getIdSaidaLocalItem(),
			getCdItem());
	}

}
