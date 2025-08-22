package com.tivic.manager.grl;

public class Produto extends ProdutoServico {

	private float vlPesoUnitario;
	private float vlPesoUnitarioEmbalagem;
	private float vlComprimento;
	private float vlLargura;
	private float vlAltura;
	private float vlComprimentoEmbalagem;
	private float vlLarguraEmbalagem;
	private float vlAlturaEmbalagem;
	private int qtEmbalagem;

	public Produto() {
		// TODO Auto-generated constructor stub
	}
	
	public Produto(int cdProdutoServico,
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
			float vlPesoUnitario,
			float vlPesoUnitarioEmbalagem,
			float vlComprimento,
			float vlLargura,
			float vlAltura,
			float vlComprimentoEmbalagem,
			float vlLarguraEmbalagem,
			float vlAlturaEmbalagem,
			int qtEmbalagem){
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
		setVlPesoUnitario(vlPesoUnitario);
		setVlPesoUnitarioEmbalagem(vlPesoUnitarioEmbalagem);
		setVlComprimento(vlComprimento);
		setVlLargura(vlLargura);
		setVlAltura(vlAltura);
		setVlComprimentoEmbalagem(vlComprimentoEmbalagem);
		setVlLarguraEmbalagem(vlLarguraEmbalagem);
		setVlAlturaEmbalagem(vlAlturaEmbalagem);
		setQtEmbalagem(qtEmbalagem);
	}
	public void setVlPesoUnitario(float vlPesoUnitario){
		this.vlPesoUnitario=vlPesoUnitario;
	}
	public float getVlPesoUnitario(){
		return this.vlPesoUnitario;
	}
	public void setVlPesoUnitarioEmbalagem(float vlPesoUnitarioEmbalagem){
		this.vlPesoUnitarioEmbalagem=vlPesoUnitarioEmbalagem;
	}
	public float getVlPesoUnitarioEmbalagem(){
		return this.vlPesoUnitarioEmbalagem;
	}
	public void setVlComprimento(float vlComprimento){
		this.vlComprimento=vlComprimento;
	}
	public float getVlComprimento(){
		return this.vlComprimento;
	}
	public void setVlLargura(float vlLargura){
		this.vlLargura=vlLargura;
	}
	public float getVlLargura(){
		return this.vlLargura;
	}
	public void setVlAltura(float vlAltura){
		this.vlAltura=vlAltura;
	}
	public float getVlAltura(){
		return this.vlAltura;
	}
	public void setVlComprimentoEmbalagem(float vlComprimentoEmbalagem){
		this.vlComprimentoEmbalagem=vlComprimentoEmbalagem;
	}
	public float getVlComprimentoEmbalagem(){
		return this.vlComprimentoEmbalagem;
	}
	public void setVlLarguraEmbalagem(float vlLarguraEmbalagem){
		this.vlLarguraEmbalagem=vlLarguraEmbalagem;
	}
	public float getVlLarguraEmbalagem(){
		return this.vlLarguraEmbalagem;
	}
	public void setVlAlturaEmbalagem(float vlAlturaEmbalagem){
		this.vlAlturaEmbalagem=vlAlturaEmbalagem;
	}
	public float getVlAlturaEmbalagem(){
		return this.vlAlturaEmbalagem;
	}
	public void setQtEmbalagem(int qtEmbalagem){
		this.qtEmbalagem=qtEmbalagem;
	}
	public int getQtEmbalagem(){
		return this.qtEmbalagem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", vlPesoUnitario: " +  getVlPesoUnitario();
		valueToString += ", vlPesoUnitarioEmbalagem: " +  getVlPesoUnitarioEmbalagem();
		valueToString += ", vlComprimento: " +  getVlComprimento();
		valueToString += ", vlLargura: " +  getVlLargura();
		valueToString += ", vlAltura: " +  getVlAltura();
		valueToString += ", vlComprimentoEmbalagem: " +  getVlComprimentoEmbalagem();
		valueToString += ", vlLarguraEmbalagem: " +  getVlLarguraEmbalagem();
		valueToString += ", vlAlturaEmbalagem: " +  getVlAlturaEmbalagem();
		valueToString += ", qtEmbalagem: " +  getQtEmbalagem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Produto(getCdProdutoServico(),
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
			getVlPesoUnitario(),
			getVlPesoUnitarioEmbalagem(),
			getVlComprimento(),
			getVlLargura(),
			getVlAltura(),
			getVlComprimentoEmbalagem(),
			getVlLarguraEmbalagem(),
			getVlAlturaEmbalagem(),
			getQtEmbalagem());
	}

}
