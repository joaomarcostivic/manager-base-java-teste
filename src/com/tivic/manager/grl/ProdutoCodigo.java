package com.tivic.manager.grl;

public class ProdutoCodigo {

	private int cdProdutoCodigo;
	private String txtDescricao;
	private String idProdutoServico;
	private String idReduzido;
	private int cdProdutoServico;
	private int lgCodigoBarras;

	public ProdutoCodigo(){ }

	public ProdutoCodigo(int cdProdutoCodigo,
			String txtDescricao,
			String idProdutoServico,
			String idReduzido,
			int cdProdutoServico,
			int lgCodigoBarras){
		setCdProdutoCodigo(cdProdutoCodigo);
		setTxtDescricao(txtDescricao);
		setIdProdutoServico(idProdutoServico);
		setIdReduzido(idReduzido);
		setCdProdutoServico(cdProdutoServico);
		setLgCodigoBarras(lgCodigoBarras);
	}
	
	/**
	 * Por padrão o lgCodigoBarras virá com 1, caso não seja passado
	 * @param cdProdutoCodigo
	 * @param txtDescricao
	 * @param idProdutoServico
	 * @param idReduzido
	 * @param cdProdutoServico
	 */
	public ProdutoCodigo(int cdProdutoCodigo,
			String txtDescricao,
			String idProdutoServico,
			String idReduzido,
			int cdProdutoServico){
		setCdProdutoCodigo(cdProdutoCodigo);
		setTxtDescricao(txtDescricao);
		setIdProdutoServico(idProdutoServico);
		setIdReduzido(idReduzido);
		setCdProdutoServico(cdProdutoServico);
		setLgCodigoBarras(1);
	}
	
	public void setCdProdutoCodigo(int cdProdutoCodigo){
		this.cdProdutoCodigo=cdProdutoCodigo;
	}
	public int getCdProdutoCodigo(){
		return this.cdProdutoCodigo;
	}
	public void setTxtDescricao(String txtDescricao){
		this.txtDescricao=txtDescricao;
	}
	public String getTxtDescricao(){
		return this.txtDescricao;
	}
	public void setIdProdutoServico(String idProdutoServico){
		this.idProdutoServico=idProdutoServico;
	}
	public String getIdProdutoServico(){
		return this.idProdutoServico;
	}
	public void setIdReduzido(String idReduzido){
		this.idReduzido=idReduzido;
	}
	public String getIdReduzido(){
		return this.idReduzido;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setLgCodigoBarras(int lgCodigoBarras){
		this.lgCodigoBarras=lgCodigoBarras;
	}
	public int getLgCodigoBarras(){
		return this.lgCodigoBarras;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProdutoCodigo: " +  getCdProdutoCodigo();
		valueToString += ", txtDescricao: " +  getTxtDescricao();
		valueToString += ", idProdutoServico: " +  getIdProdutoServico();
		valueToString += ", idReduzido: " +  getIdReduzido();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", lgCodigoBarras: " +  getLgCodigoBarras();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProdutoCodigo(getCdProdutoCodigo(),
			getTxtDescricao(),
			getIdProdutoServico(),
			getIdReduzido(),
			getCdProdutoServico(),
			getLgCodigoBarras());
	}

}