package com.tivic.manager.grl;

public class ProdutoServicoComposicao {

	private int cdComposicao;
	private int cdEmpresa;
	private int cdProdutoServico;
	private int cdProdutoServicoComponente;
	private int cdReferencia;
	private int cdUnidadeMedida;
	private float qtProdutoServico;
	private float prPerda;
	private int lgCalculoCusto;

	public ProdutoServicoComposicao(int cdComposicao,
			int cdEmpresa,
			int cdProdutoServico,
			int cdProdutoServicoComponente,
			int cdReferencia,
			int cdUnidadeMedida,
			float qtProdutoServico,
			float prPerda,
			int lgCalculoCusto){
		setCdComposicao(cdComposicao);
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setCdProdutoServicoComponente(cdProdutoServicoComponente);
		setCdReferencia(cdReferencia);
		setCdUnidadeMedida(cdUnidadeMedida);
		setQtProdutoServico(qtProdutoServico);
		setPrPerda(prPerda);
		setLgCalculoCusto(lgCalculoCusto);
	}
	public void setCdComposicao(int cdComposicao){
		this.cdComposicao=cdComposicao;
	}
	public int getCdComposicao(){
		return this.cdComposicao;
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
	public void setCdProdutoServicoComponente(int cdProdutoServicoComponente){
		this.cdProdutoServicoComponente=cdProdutoServicoComponente;
	}
	public int getCdProdutoServicoComponente(){
		return this.cdProdutoServicoComponente;
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setCdUnidadeMedida(int cdUnidadeMedida){
		this.cdUnidadeMedida=cdUnidadeMedida;
	}
	public int getCdUnidadeMedida(){
		return this.cdUnidadeMedida;
	}
	public void setQtProdutoServico(float qtProdutoServico){
		this.qtProdutoServico=qtProdutoServico;
	}
	public float getQtProdutoServico(){
		return this.qtProdutoServico;
	}
	public void setPrPerda(float prPerda){
		this.prPerda=prPerda;
	}
	public float getPrPerda(){
		return this.prPerda;
	}
	public void setLgCalculoCusto(int lgCalculoCusto){
		this.lgCalculoCusto=lgCalculoCusto;
	}
	public int getLgCalculoCusto(){
		return this.lgCalculoCusto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdComposicao: " +  getCdComposicao();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdProdutoServicoComponente: " +  getCdProdutoServicoComponente();
		valueToString += ", cdReferencia: " +  getCdReferencia();
		valueToString += ", cdUnidadeMedida: " +  getCdUnidadeMedida();
		valueToString += ", qtProdutoServico: " +  getQtProdutoServico();
		valueToString += ", prPerda: " +  getPrPerda();
		valueToString += ", lgCalculoCusto: " +  getLgCalculoCusto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProdutoServicoComposicao(getCdComposicao(),
			getCdEmpresa(),
			getCdProdutoServico(),
			getCdProdutoServicoComponente(),
			getCdReferencia(),
			getCdUnidadeMedida(),
			getQtProdutoServico(),
			getPrPerda(),
			getLgCalculoCusto());
	}

}
