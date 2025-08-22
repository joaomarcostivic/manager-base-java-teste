package com.tivic.manager.alm;

public class ProdutoEstoque {

	private int cdLocalArmazenamento;
	private int cdProdutoServico;
	private int cdEmpresa;
	private float qtEstoque;
	private float qtIdeal;
	private float qtMinima;
	private float qtMaxima;
	private int qtDiasEstoque;
	private float qtMinimaEcommerce;
	private float qtEstoqueConsignado;
	private int lgDefault;
	private int cdLocalArmazenamentoOrigem;
	private int tpAbastecimento;
	private float qtTransferencia;
	private int stEstoque;

	public ProdutoEstoque(){ }

	public ProdutoEstoque(int cdLocalArmazenamento,
			int cdProdutoServico,
			int cdEmpresa,
			float qtEstoque,
			float qtIdeal,
			float qtMinima,
			float qtMaxima,
			int qtDiasEstoque,
			float qtMinimaEcommerce,
			float qtEstoqueConsignado,
			int lgDefault){
		setCdLocalArmazenamento(cdLocalArmazenamento);
		setCdProdutoServico(cdProdutoServico);
		setCdEmpresa(cdEmpresa);
		setQtEstoque(qtEstoque);
		setQtIdeal(qtIdeal);
		setQtMinima(qtMinima);
		setQtMaxima(qtMaxima);
		setQtDiasEstoque(qtDiasEstoque);
		setQtMinimaEcommerce(qtMinimaEcommerce);
		setQtEstoqueConsignado(qtEstoqueConsignado);
		setLgDefault(lgDefault);
	}
	
	public ProdutoEstoque(int cdLocalArmazenamento,
			int cdProdutoServico,
			int cdEmpresa,
			float qtEstoque,
			float qtIdeal,
			float qtMinima,
			float qtMaxima,
			int qtDiasEstoque,
			float qtMinimaEcommerce,
			float qtEstoqueConsignado,
			int lgDefault,
			int cdLocalArmazenamentoOrigem,
			int tpAbastecimento,
			float qtTransferencia,
			int stEstoque){
		setCdLocalArmazenamento(cdLocalArmazenamento);
		setCdProdutoServico(cdProdutoServico);
		setCdEmpresa(cdEmpresa);
		setQtEstoque(qtEstoque);
		setQtIdeal(qtIdeal);
		setQtMinima(qtMinima);
		setQtMaxima(qtMaxima);
		setQtDiasEstoque(qtDiasEstoque);
		setQtMinimaEcommerce(qtMinimaEcommerce);
		setQtEstoqueConsignado(qtEstoqueConsignado);
		setLgDefault(lgDefault);
		setCdLocalArmazenamentoOrigem(cdLocalArmazenamentoOrigem);
		setTpAbastecimento(tpAbastecimento);
		setQtTransferencia(qtTransferencia);
		setStEstoque(stEstoque);
	}
	public void setCdLocalArmazenamento(int cdLocalArmazenamento){
		this.cdLocalArmazenamento=cdLocalArmazenamento;
	}
	public int getCdLocalArmazenamento(){
		return this.cdLocalArmazenamento;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setQtEstoque(float qtEstoque){
		this.qtEstoque=qtEstoque;
	}
	public float getQtEstoque(){
		return this.qtEstoque;
	}
	public void setQtIdeal(float qtIdeal){
		this.qtIdeal=qtIdeal;
	}
	public float getQtIdeal(){
		return this.qtIdeal;
	}
	public void setQtMinima(float qtMinima){
		this.qtMinima=qtMinima;
	}
	public float getQtMinima(){
		return this.qtMinima;
	}
	public void setQtMaxima(float qtMaxima){
		this.qtMaxima=qtMaxima;
	}
	public float getQtMaxima(){
		return this.qtMaxima;
	}
	public void setQtDiasEstoque(int qtDiasEstoque){
		this.qtDiasEstoque=qtDiasEstoque;
	}
	public int getQtDiasEstoque(){
		return this.qtDiasEstoque;
	}
	public void setQtMinimaEcommerce(float qtMinimaEcommerce){
		this.qtMinimaEcommerce=qtMinimaEcommerce;
	}
	public float getQtMinimaEcommerce(){
		return this.qtMinimaEcommerce;
	}
	public void setQtEstoqueConsignado(float qtEstoqueConsignado){
		this.qtEstoqueConsignado=qtEstoqueConsignado;
	}
	public float getQtEstoqueConsignado(){
		return this.qtEstoqueConsignado;
	}
	public void setLgDefault(int lgDefault){
		this.lgDefault=lgDefault;
	}
	public int getLgDefault(){
		return this.lgDefault;
	}
	public void setCdLocalArmazenamentoOrigem(int cdLocalArmazenamentoOrigem){
		this.cdLocalArmazenamentoOrigem=cdLocalArmazenamentoOrigem;
	}
	public int getCdLocalArmazenamentoOrigem(){
		return this.cdLocalArmazenamentoOrigem;
	}
	public void setTpAbastecimento(int tpAbastecimento){
		this.tpAbastecimento=tpAbastecimento;
	}
	public int getTpAbastecimento(){
		return this.tpAbastecimento;
	}
	public void setQtTransferencia(float qtTransferencia){
		this.qtTransferencia=qtTransferencia;
	}
	public float getQtTransferencia(){
		return this.qtTransferencia;
	}
	public void setStEstoque(int stEstoque){
		this.stEstoque=stEstoque;
	}
	public int getStEstoque(){
		return this.stEstoque;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLocalArmazenamento: " +  getCdLocalArmazenamento();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", qtEstoque: " +  getQtEstoque();
		valueToString += ", qtIdeal: " +  getQtIdeal();
		valueToString += ", qtMinima: " +  getQtMinima();
		valueToString += ", qtMaxima: " +  getQtMaxima();
		valueToString += ", qtDiasEstoque: " +  getQtDiasEstoque();
		valueToString += ", qtMinimaEcommerce: " +  getQtMinimaEcommerce();
		valueToString += ", qtEstoqueConsignado: " +  getQtEstoqueConsignado();
		valueToString += ", lgDefault: " +  getLgDefault();
		valueToString += ", cdLocalArmazenamentoOrigem: " +  getCdLocalArmazenamentoOrigem();
		valueToString += ", tpAbastecimento: " +  getTpAbastecimento();
		valueToString += ", qtTransferencia: " +  getQtTransferencia();
		valueToString += ", stEstoque: " +  getStEstoque();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProdutoEstoque(getCdLocalArmazenamento(),
			getCdProdutoServico(),
			getCdEmpresa(),
			getQtEstoque(),
			getQtIdeal(),
			getQtMinima(),
			getQtMaxima(),
			getQtDiasEstoque(),
			getQtMinimaEcommerce(),
			getQtEstoqueConsignado(),
			getLgDefault(),
			getCdLocalArmazenamentoOrigem(),
			getTpAbastecimento(),
			getQtTransferencia(),
			getStEstoque());
	}

}