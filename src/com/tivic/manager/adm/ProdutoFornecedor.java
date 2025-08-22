package com.tivic.manager.adm;

public class ProdutoFornecedor {

	private int cdFornecedor;
	private int cdEmpresa;
	private int cdProdutoServico;
	private int cdRepresentante;
	private float vlUltimoPreco;
	private int qtDiasEntrega;
	private int qtDiasUltimaEntrega;
	private String idProduto;
	private float qtPedidoMinimo;
	private int cdMoeda;
	private int cdUnidadeMedida;

	public ProdutoFornecedor() {
	}
	
	public ProdutoFornecedor(int cdFornecedor,
			int cdEmpresa,
			int cdProdutoServico,
			int cdRepresentante,
			float vlUltimoPreco,
			int qtDiasEntrega,
			int qtDiasUltimaEntrega,
			String idProduto,
			float qtPedidoMinimo,
			int cdMoeda,
			int cdUnidadeMedida){
		setCdFornecedor(cdFornecedor);
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setCdRepresentante(cdRepresentante);
		setVlUltimoPreco(vlUltimoPreco);
		setQtDiasEntrega(qtDiasEntrega);
		setQtDiasUltimaEntrega(qtDiasUltimaEntrega);
		setIdProduto(idProduto);
		setQtPedidoMinimo(qtPedidoMinimo);
		setCdMoeda(cdMoeda);
		setCdUnidadeMedida(cdUnidadeMedida);
	}
	public void setCdFornecedor(int cdFornecedor){
		this.cdFornecedor=cdFornecedor;
	}
	public int getCdFornecedor(){
		return this.cdFornecedor;
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
	public void setCdRepresentante(int cdRepresentante){
		this.cdRepresentante=cdRepresentante;
	}
	public int getCdRepresentante(){
		return this.cdRepresentante;
	}
	public void setVlUltimoPreco(float vlUltimoPreco){
		this.vlUltimoPreco=vlUltimoPreco;
	}
	public float getVlUltimoPreco(){
		return this.vlUltimoPreco;
	}
	public void setQtDiasEntrega(int qtDiasEntrega){
		this.qtDiasEntrega=qtDiasEntrega;
	}
	public int getQtDiasEntrega(){
		return this.qtDiasEntrega;
	}
	public void setQtDiasUltimaEntrega(int qtDiasUltimaEntrega){
		this.qtDiasUltimaEntrega=qtDiasUltimaEntrega;
	}
	public int getQtDiasUltimaEntrega(){
		return this.qtDiasUltimaEntrega;
	}
	public void setIdProduto(String idProduto){
		this.idProduto=idProduto;
	}
	public String getIdProduto(){
		return this.idProduto;
	}
	public void setQtPedidoMinimo(float qtPedidoMinimo){
		this.qtPedidoMinimo=qtPedidoMinimo;
	}
	public float getQtPedidoMinimo(){
		return this.qtPedidoMinimo;
	}
	public void setCdMoeda(int cdMoeda){
		this.cdMoeda=cdMoeda;
	}
	public int getCdMoeda(){
		return this.cdMoeda;
	}
	public void setCdUnidadeMedida(int cdUnidadeMedida){
		this.cdUnidadeMedida=cdUnidadeMedida;
	}
	public int getCdUnidadeMedida(){
		return this.cdUnidadeMedida;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFornecedor: " +  getCdFornecedor();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdRepresentante: " +  getCdRepresentante();
		valueToString += ", vlUltimoPreco: " +  getVlUltimoPreco();
		valueToString += ", qtDiasEntrega: " +  getQtDiasEntrega();
		valueToString += ", qtDiasUltimaEntrega: " +  getQtDiasUltimaEntrega();
		valueToString += ", idProduto: " +  getIdProduto();
		valueToString += ", qtPedidoMinimo: " +  getQtPedidoMinimo();
		valueToString += ", cdMoeda: " +  getCdMoeda();
		valueToString += ", cdUnidadeMedida: " +  getCdUnidadeMedida();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProdutoFornecedor(getCdFornecedor(),
			getCdEmpresa(),
			getCdProdutoServico(),
			getCdRepresentante(),
			getVlUltimoPreco(),
			getQtDiasEntrega(),
			getQtDiasUltimaEntrega(),
			getIdProduto(),
			getQtPedidoMinimo(),
			getCdMoeda(),
			getCdUnidadeMedida());
	}

}
