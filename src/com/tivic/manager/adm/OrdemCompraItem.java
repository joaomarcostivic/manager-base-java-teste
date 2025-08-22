package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class OrdemCompraItem {

	private int cdOrdemCompra;
	private int cdEmpresa;
	private int cdProdutoServico;
	private int cdCotacaoPedidoItem;
	private float qtComprada;
	private float vlUnitario;
	private float vlDesconto;
	private float vlAcrescimo;
	private GregorianCalendar dtPrevisaoEntrega;
	private int cdPedidoVenda;
	private int cdUnidadeMedida;

	public OrdemCompraItem() {
	}
	
	public OrdemCompraItem(int cdOrdemCompra,
			int cdEmpresa,
			int cdProdutoServico,
			int cdCotacaoPedidoItem,
			float qtComprada,
			float vlUnitario,
			float vlDesconto,
			float vlAcrescimo,
			GregorianCalendar dtPrevisaoEntrega,
			int cdPedidoVenda,
			int cdUnidadeMedida){
		setCdOrdemCompra(cdOrdemCompra);
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setCdCotacaoPedidoItem(cdCotacaoPedidoItem);
		setQtComprada(qtComprada);
		setVlUnitario(vlUnitario);
		setVlDesconto(vlDesconto);
		setVlAcrescimo(vlAcrescimo);
		setDtPrevisaoEntrega(dtPrevisaoEntrega);
		setCdPedidoVenda(cdPedidoVenda);
		setCdUnidadeMedida(cdUnidadeMedida);
	}
	public void setCdOrdemCompra(int cdOrdemCompra){
		this.cdOrdemCompra=cdOrdemCompra;
	}
	public int getCdOrdemCompra(){
		return this.cdOrdemCompra;
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
	public void setCdCotacaoPedidoItem(int cdCotacaoPedidoItem){
		this.cdCotacaoPedidoItem=cdCotacaoPedidoItem;
	}
	public int getCdCotacaoPedidoItem(){
		return this.cdCotacaoPedidoItem;
	}
	public void setQtComprada(float qtComprada){
		this.qtComprada=qtComprada;
	}
	public float getQtComprada(){
		return this.qtComprada;
	}
	public void setVlUnitario(float vlUnitario){
		this.vlUnitario=vlUnitario;
	}
	public float getVlUnitario(){
		return this.vlUnitario;
	}
	public void setVlDesconto(float vlDesconto){
		this.vlDesconto=vlDesconto;
	}
	public float getVlDesconto(){
		return this.vlDesconto;
	}
	public void setVlAcrescimo(float vlAcrescimo){
		this.vlAcrescimo=vlAcrescimo;
	}
	public float getVlAcrescimo(){
		return this.vlAcrescimo;
	}
	public void setDtPrevisaoEntrega(GregorianCalendar dtPrevisaoEntrega){
		this.dtPrevisaoEntrega=dtPrevisaoEntrega;
	}
	public GregorianCalendar getDtPrevisaoEntrega(){
		return this.dtPrevisaoEntrega;
	}
	public void setCdPedidoVenda(int cdPedidoVenda){
		this.cdPedidoVenda=cdPedidoVenda;
	}
	public int getCdPedidoVenda(){
		return this.cdPedidoVenda;
	}
	public void setCdUnidadeMedida(int cdUnidadeMedida){
		this.cdUnidadeMedida=cdUnidadeMedida;
	}
	public int getCdUnidadeMedida(){
		return this.cdUnidadeMedida;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOrdemCompra: " +  getCdOrdemCompra();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdCotacaoPedidoItem: " +  getCdCotacaoPedidoItem();
		valueToString += ", qtComprada: " +  getQtComprada();
		valueToString += ", vlUnitario: " +  getVlUnitario();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		valueToString += ", vlAcrescimo: " +  getVlAcrescimo();
		valueToString += ", dtPrevisaoEntrega: " +  sol.util.Util.formatDateTime(getDtPrevisaoEntrega(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdPedidoVenda: " +  getCdPedidoVenda();
		valueToString += ", cdUnidadeMedida: " +  getCdUnidadeMedida();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OrdemCompraItem(getCdOrdemCompra(),
			getCdEmpresa(),
			getCdProdutoServico(),
			getCdCotacaoPedidoItem(),
			getQtComprada(),
			getVlUnitario(),
			getVlDesconto(),
			getVlAcrescimo(),
			getDtPrevisaoEntrega()==null ? null : (GregorianCalendar)getDtPrevisaoEntrega().clone(),
			getCdPedidoVenda(),
			getCdUnidadeMedida());
	}

}
