package com.tivic.manager.mob;

import java.util.List;

public class Via {

	private int cdVia;
	private String nmVia;
	private String idVia;
	private int tpLocalizacao;
	private int tpVia;
	private int cdReferencia;
	private int cdLogradouro;
	private int cdOrgao;
	
	private List<Faixa> faixas;

	public Via() { }

	public Via(int cdVia,
			String nmVia,
			String idVia,
			int tpLocalizacao,
			int tpVia,
			int cdReferencia,
			int cdLogradouro,
			int cdOrgao) {
		setCdVia(cdVia);
		setNmVia(nmVia);
		setIdVia(idVia);
		setTpLocalizacao(tpLocalizacao);
		setTpVia(tpVia);
		setCdReferencia(cdReferencia);
		setCdLogradouro(cdLogradouro);
		setCdOrgao(cdOrgao);
	}
	public void setCdVia(int cdVia){
		this.cdVia=cdVia;
	}
	public int getCdVia(){
		return this.cdVia;
	}
	public void setNmVia(String nmVia){
		this.nmVia=nmVia;
	}
	public String getNmVia(){
		return this.nmVia;
	}
	public void setIdVia(String idVia){
		this.idVia=idVia;
	}
	public String getIdVia(){
		return this.idVia;
	}
	public void setTpLocalizacao(int tpLocalizacao){
		this.tpLocalizacao=tpLocalizacao;
	}
	public int getTpLocalizacao(){
		return this.tpLocalizacao;
	}
	public void setTpVia(int tpVia){
		this.tpVia=tpVia;
	}
	public int getTpVia(){
		return this.tpVia;
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setCdLogradouro(int cdLogradouro){
		this.cdLogradouro=cdLogradouro;
	}
	public int getCdLogradouro(){
		return this.cdLogradouro;
	}
	public int getCdOrgao() {
		return cdOrgao;
	}
	public void setCdOrgao(int cdOrgao) {
		this.cdOrgao = cdOrgao;
	}
	public List<Faixa> getFaixas() {
		return faixas;
	}
	public void setFaixas(List<Faixa> faixas) {
		this.faixas = faixas;
	}

	public String toString() {
		String valueToString = "";
		valueToString += "cdVia: " +  getCdVia();
		valueToString += ", nmVia: " +  getNmVia();
		valueToString += ", idVia: " +  getIdVia();
		valueToString += ", tpLocalizacao: " +  getTpLocalizacao();
		valueToString += ", tpVia: " +  getTpVia();
		valueToString += ", cdReferencia: " +  getCdReferencia();
		valueToString += ", cdLogradouro: " +  getCdLogradouro();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Via(getCdVia(),
			getNmVia(),
			getIdVia(),
			getTpLocalizacao(),
			getTpVia(),
			getCdReferencia(),
			getCdLogradouro(),
			getCdOrgao());
	}

}