package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class PedidoVendaItem {

	private int cdPedidoVenda;
	private int cdEmpresa;
	private int cdProdutoServico;
	private int cdUnidadeMedida;
	private float qtSolicitada;
	private String txtPedidoItem;
	private float vlUnitario;
	private float vlDesconto;
	private float vlAcrescimo;
	private float vlDescontoPromocao;
	private int lgReservaEstoque;
	private GregorianCalendar dtEntregaPrevista;
	private int cdTabelaPreco;
	private int cdTabelaPrecoPromocao;
	private int cdProdutoServicoPreco;
	private int cdRegraPromocao;
	private int cdOrdemServicoItem;
	private int cdOrdemServico;

	public PedidoVendaItem(int cdPedidoVenda,
			int cdEmpresa,
			int cdProdutoServico,
			int cdUnidadeMedida,
			float qtSolicitada,
			String txtPedidoItem,
			float vlUnitario,
			float vlDesconto,
			float vlAcrescimo,
			float vlDescontoPromocao,
			int lgReservaEstoque,
			GregorianCalendar dtEntregaPrevista,
			int cdTabelaPreco,
			int cdTabelaPrecoPromocao,
			int cdProdutoServicoPreco,
			int cdRegraPromocao,
			int cdOrdemServicoItem,
			int cdOrdemServico){
		setCdPedidoVenda(cdPedidoVenda);
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setCdUnidadeMedida(cdUnidadeMedida);
		setQtSolicitada(qtSolicitada);
		setTxtPedidoItem(txtPedidoItem);
		setVlUnitario(vlUnitario);
		setVlDesconto(vlDesconto);
		setVlAcrescimo(vlAcrescimo);
		setVlDescontoPromocao(vlDescontoPromocao);
		setLgReservaEstoque(lgReservaEstoque);
		setDtEntregaPrevista(dtEntregaPrevista);
		setCdTabelaPreco(cdTabelaPreco);
		setCdTabelaPrecoPromocao(cdTabelaPrecoPromocao);
		setCdProdutoServicoPreco(cdProdutoServicoPreco);
		setCdRegraPromocao(cdRegraPromocao);
		setCdOrdemServicoItem(cdOrdemServicoItem);
		setCdOrdemServico(cdOrdemServico);
	}
	public void setCdPedidoVenda(int cdPedidoVenda){
		this.cdPedidoVenda=cdPedidoVenda;
	}
	public int getCdPedidoVenda(){
		return this.cdPedidoVenda;
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
	public void setTxtPedidoItem(String txtPedidoItem){
		this.txtPedidoItem=txtPedidoItem;
	}
	public String getTxtPedidoItem(){
		return this.txtPedidoItem;
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
	public void setVlDescontoPromocao(float vlDescontoPromocao){
		this.vlDescontoPromocao=vlDescontoPromocao;
	}
	public float getVlDescontoPromocao(){
		return this.vlDescontoPromocao;
	}
	public void setLgReservaEstoque(int lgReservaEstoque){
		this.lgReservaEstoque=lgReservaEstoque;
	}
	public int getLgReservaEstoque(){
		return this.lgReservaEstoque;
	}
	public void setDtEntregaPrevista(GregorianCalendar dtEntregaPrevista){
		this.dtEntregaPrevista=dtEntregaPrevista;
	}
	public GregorianCalendar getDtEntregaPrevista(){
		return this.dtEntregaPrevista;
	}
	public void setCdTabelaPreco(int cdTabelaPreco){
		this.cdTabelaPreco=cdTabelaPreco;
	}
	public int getCdTabelaPreco(){
		return this.cdTabelaPreco;
	}
	public void setCdTabelaPrecoPromocao(int cdTabelaPrecoPromocao){
		this.cdTabelaPrecoPromocao=cdTabelaPrecoPromocao;
	}
	public int getCdTabelaPrecoPromocao(){
		return this.cdTabelaPrecoPromocao;
	}
	public void setCdProdutoServicoPreco(int cdProdutoServicoPreco){
		this.cdProdutoServicoPreco=cdProdutoServicoPreco;
	}
	public int getCdProdutoServicoPreco(){
		return this.cdProdutoServicoPreco;
	}
	public void setCdRegraPromocao(int cdRegraPromocao){
		this.cdRegraPromocao=cdRegraPromocao;
	}
	public int getCdRegraPromocao(){
		return this.cdRegraPromocao;
	}
	public void setCdOrdemServicoItem(int cdOrdemServicoItem){
		this.cdOrdemServicoItem=cdOrdemServicoItem;
	}
	public int getCdOrdemServicoItem(){
		return this.cdOrdemServicoItem;
	}
	public void setCdOrdemServico(int cdOrdemServico){
		this.cdOrdemServico=cdOrdemServico;
	}
	public int getCdOrdemServico(){
		return this.cdOrdemServico;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPedidoVenda: " +  getCdPedidoVenda();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdUnidadeMedida: " +  getCdUnidadeMedida();
		valueToString += ", qtSolicitada: " +  getQtSolicitada();
		valueToString += ", txtPedidoItem: " +  getTxtPedidoItem();
		valueToString += ", vlUnitario: " +  getVlUnitario();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		valueToString += ", vlAcrescimo: " +  getVlAcrescimo();
		valueToString += ", vlDescontoPromocao: " +  getVlDescontoPromocao();
		valueToString += ", lgReservaEstoque: " +  getLgReservaEstoque();
		valueToString += ", dtEntregaPrevista: " +  sol.util.Util.formatDateTime(getDtEntregaPrevista(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdTabelaPreco: " +  getCdTabelaPreco();
		valueToString += ", cdTabelaPrecoPromocao: " +  getCdTabelaPrecoPromocao();
		valueToString += ", cdProdutoServicoPreco: " +  getCdProdutoServicoPreco();
		valueToString += ", cdRegraPromocao: " +  getCdRegraPromocao();
		valueToString += ", cdOrdemServicoItem: " +  getCdOrdemServicoItem();
		valueToString += ", cdOrdemServico: " +  getCdOrdemServico();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PedidoVendaItem(getCdPedidoVenda(),
			getCdEmpresa(),
			getCdProdutoServico(),
			getCdUnidadeMedida(),
			getQtSolicitada(),
			getTxtPedidoItem(),
			getVlUnitario(),
			getVlDesconto(),
			getVlAcrescimo(),
			getVlDescontoPromocao(),
			getLgReservaEstoque(),
			getDtEntregaPrevista()==null ? null : (GregorianCalendar)getDtEntregaPrevista().clone(),
			getCdTabelaPreco(),
			getCdTabelaPrecoPromocao(),
			getCdProdutoServicoPreco(),
			getCdRegraPromocao(),
			getCdOrdemServicoItem(),
			getCdOrdemServico());
	}

}
