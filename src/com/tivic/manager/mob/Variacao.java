package com.tivic.manager.mob;

public class Variacao {

	private int cdVariacao;
	private String nmVariacao;
	private Double qtKm;

	public Variacao() { }

	public Variacao(int cdVariacao,
			String nmVariacao,
			Double qtKm) {
		setCdVariacao(cdVariacao);
		setNmVariacao(nmVariacao);
		setQtKm(qtKm);
	}
	public void setCdVariacao(int cdVariacao){
		this.cdVariacao=cdVariacao;
	}
	public int getCdVariacao(){
		return this.cdVariacao;
	}
	public void setNmVariacao(String nmVariacao){
		this.nmVariacao=nmVariacao;
	}
	public String getNmVariacao(){
		return this.nmVariacao;
	}
	public void setQtKm(Double qtKm){
		this.qtKm=qtKm;
	}
	public Double getQtKm(){
		return this.qtKm;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdVariacao: " +  getCdVariacao();
		valueToString += ", nmVariacao: " +  getNmVariacao();
		valueToString += ", qtKm: " +  getQtKm();
		return "{" + valueToString + "}";
	}
	

	public Object clone() {
		Variacao variacao = new Variacao();
		variacao.setCdVariacao(getCdVariacao());
		variacao.setNmVariacao(getNmVariacao());
		variacao.setQtKm(getQtKm());
		return variacao;
	}

}