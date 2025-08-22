package com.tivic.manager.ord;

public class TipoProdutoServico {

	private int cdTipoProdutoServico;
	private String nmTipoProdutoServico;
	private String idTipoProdutoServico;
	private String txtDescricao;

	public TipoProdutoServico() { }

	public TipoProdutoServico(int cdTipoProdutoServico,
			String nmTipoProdutoServico,
			String idTipoProdutoServico,
			String txtDescricao) {
		setCdTipoProdutoServico(cdTipoProdutoServico);
		setNmTipoProdutoServico(nmTipoProdutoServico);
		setIdTipoProdutoServico(idTipoProdutoServico);
		setTxtDescricao(txtDescricao);
	}
	public void setCdTipoProdutoServico(int cdTipoProdutoServico){
		this.cdTipoProdutoServico=cdTipoProdutoServico;
	}
	public int getCdTipoProdutoServico(){
		return this.cdTipoProdutoServico;
	}
	public void setNmTipoProdutoServico(String nmTipoProdutoServico){
		this.nmTipoProdutoServico=nmTipoProdutoServico;
	}
	public String getNmTipoProdutoServico(){
		return this.nmTipoProdutoServico;
	}
	public void setIdTipoProdutoServico(String idTipoProdutoServico){
		this.idTipoProdutoServico=idTipoProdutoServico;
	}
	public String getIdTipoProdutoServico(){
		return this.idTipoProdutoServico;
	}
	public void setTxtDescricao(String txtDescricao){
		this.txtDescricao=txtDescricao;
	}
	public String getTxtDescricao(){
		return this.txtDescricao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoProdutoServico: " +  getCdTipoProdutoServico();
		valueToString += ", nmTipoProdutoServico: " +  getNmTipoProdutoServico();
		valueToString += ", idTipoProdutoServico: " +  getIdTipoProdutoServico();
		valueToString += ", txtDescricao: " +  getTxtDescricao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoProdutoServico(getCdTipoProdutoServico(),
			getNmTipoProdutoServico(),
			getIdTipoProdutoServico(),
			getTxtDescricao());
	}

}