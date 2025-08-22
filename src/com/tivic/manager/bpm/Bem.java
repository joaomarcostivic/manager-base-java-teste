package com.tivic.manager.bpm;

public class Bem extends com.tivic.manager.grl.ProdutoServico {

	private int cdClassificacao;
	private float prDepreciacao;
	
	public Bem() {
		super();
	}
	public Bem(int cdProdutoServico,
			int cdCategoriaEconomica,
			String nmProdutoServico,
			String txtProdutoServico,
			String txtEspecificacao,
			String txtDadoTecnico,
			String txtPrazoEntrega,
			int tpProdutoServico,
			String idProdutoServico,
			String sgProdutoServico,
			int cdClassificacaoFiscal,
			int cdFabricante,
			int cdMarca,
			String nmModelo,
			int cdNcm,
			String nrReferencia,
			int cdClassificacao,
			float prDepreciacao){
		super(cdProdutoServico,
			cdCategoriaEconomica,
			nmProdutoServico,
			txtProdutoServico,
			txtEspecificacao,
			txtDadoTecnico,
			txtPrazoEntrega,
			tpProdutoServico,
			idProdutoServico,
			sgProdutoServico,
			cdClassificacaoFiscal,
			cdFabricante,
			cdMarca,
			nmModelo,
			cdNcm,
			nrReferencia);
		setCdClassificacao(cdClassificacao);
		setPrDepreciacao(prDepreciacao);
	}
	public void setCdBem(int cdBem){
		setCdProdutoServico(cdBem);
	}
	public int getCdBem(){
		return getCdProdutoServico();
	}
	public void setCdClassificacao(int cdClassificacao){
		this.cdClassificacao=cdClassificacao;
	}
	public int getCdClassificacao(){
		return this.cdClassificacao;
	}
	public void setPrDepreciacao(float prDepreciacao){
		this.prDepreciacao=prDepreciacao;
	}
	public float getPrDepreciacao(){
		return this.prDepreciacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdClassificacao: " +  getCdClassificacao();
		valueToString += ", prDepreciacao: " +  getPrDepreciacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Bem(getCdProdutoServico(),
			getCdCategoriaEconomica(),
			getNmProdutoServico(),
			getTxtProdutoServico(),
			getTxtEspecificacao(),
			getTxtDadoTecnico(),
			getTxtPrazoEntrega(),
			getTpProdutoServico(),
			getIdProdutoServico(),
			getSgProdutoServico(),
			getCdClassificacaoFiscal(),
			getCdFabricante(),
			getCdMarca(),
			getNmModelo(),
			getCdNcm(),
			getNrReferencia(),
			getCdClassificacao(),
			getPrDepreciacao());
	}

}
