package com.tivic.manager.adm;

public class CompraSolicitacaoItem {

	private int cdEmpresa;
	private int cdPedidoCompra;
	private int cdProdutoServico;
	private int cdSolicitacaoMaterial;
	private float qtAtendida;

	public CompraSolicitacaoItem(int cdEmpresa,
			int cdPedidoCompra,
			int cdProdutoServico,
			int cdSolicitacaoMaterial,
			float qtAtendida){
		setCdEmpresa(cdEmpresa);
		setCdPedidoCompra(cdPedidoCompra);
		setCdProdutoServico(cdProdutoServico);
		setCdSolicitacaoMaterial(cdSolicitacaoMaterial);
		setQtAtendida(qtAtendida);
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdPedidoCompra(int cdPedidoCompra){
		this.cdPedidoCompra=cdPedidoCompra;
	}
	public int getCdPedidoCompra(){
		return this.cdPedidoCompra;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdSolicitacaoMaterial(int cdSolicitacaoMaterial){
		this.cdSolicitacaoMaterial=cdSolicitacaoMaterial;
	}
	public int getCdSolicitacaoMaterial(){
		return this.cdSolicitacaoMaterial;
	}
	public void setQtAtendida(float qtAtendida){
		this.qtAtendida=qtAtendida;
	}
	public float getQtAtendida(){
		return this.qtAtendida;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdPedidoCompra: " +  getCdPedidoCompra();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdSolicitacaoMaterial: " +  getCdSolicitacaoMaterial();
		valueToString += ", qtAtendida: " +  getQtAtendida();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CompraSolicitacaoItem(getCdEmpresa(),
			getCdPedidoCompra(),
			getCdProdutoServico(),
			getCdSolicitacaoMaterial(),
			getQtAtendida());
	}

}
