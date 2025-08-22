package com.tivic.manager.prc;

public class CidadeOrgao {

	private int cdOrgao;
	private int cdCidade;
	private float qtDistancia;
	private int lgPrincipal;

	public CidadeOrgao(){ }

	public CidadeOrgao(int cdOrgao,
			int cdCidade,
			float qtDistancia,
			int lgPrincipal){
		setCdOrgao(cdOrgao);
		setCdCidade(cdCidade);
		setQtDistancia(qtDistancia);
		setLgPrincipal(lgPrincipal);
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setQtDistancia(float qtDistancia){
		this.qtDistancia=qtDistancia;
	}
	public float getQtDistancia(){
		return this.qtDistancia;
	}
	public int getLgPrincipal() {
		return lgPrincipal;
	}

	public void setLgPrincipal(int lgPrincipal) {
		this.lgPrincipal = lgPrincipal;
	}

	public String toString() {
		String valueToString = "";
		valueToString += "cdOrgao: " +  getCdOrgao();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", qtDistancia: " +  getQtDistancia();
		valueToString += ", lgPrincipal: " +  getLgPrincipal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CidadeOrgao(getCdOrgao(),
			getCdCidade(),
			getQtDistancia(),
			getLgPrincipal());
	}

}
