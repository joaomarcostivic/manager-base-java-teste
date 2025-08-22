package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class TabelaComissao {

	private int cdTabelaComissao;
	private String nmTabelaComissao;
	private GregorianCalendar dtInicioValidade;
	private GregorianCalendar dtFinalValidade;
	private String idTabelaComissao;
	private int lgProgressiva;
	private float vlAplicacao;
	private int cdEmpresa;

	public TabelaComissao(int cdTabelaComissao,
			String nmTabelaComissao,
			GregorianCalendar dtInicioValidade,
			GregorianCalendar dtFinalValidade,
			String idTabelaComissao,
			int lgProgressiva,
			float vlAplicacao,
			int cdEmpresa){
		setCdTabelaComissao(cdTabelaComissao);
		setNmTabelaComissao(nmTabelaComissao);
		setDtInicioValidade(dtInicioValidade);
		setDtFinalValidade(dtFinalValidade);
		setIdTabelaComissao(idTabelaComissao);
		setLgProgressiva(lgProgressiva);
		setVlAplicacao(vlAplicacao);
		setCdEmpresa(cdEmpresa);
	}
	public void setCdTabelaComissao(int cdTabelaComissao){
		this.cdTabelaComissao=cdTabelaComissao;
	}
	public int getCdTabelaComissao(){
		return this.cdTabelaComissao;
	}
	public void setNmTabelaComissao(String nmTabelaComissao){
		this.nmTabelaComissao=nmTabelaComissao;
	}
	public String getNmTabelaComissao(){
		return this.nmTabelaComissao;
	}
	public void setDtInicioValidade(GregorianCalendar dtInicioValidade){
		this.dtInicioValidade=dtInicioValidade;
	}
	public GregorianCalendar getDtInicioValidade(){
		return this.dtInicioValidade;
	}
	public void setDtFinalValidade(GregorianCalendar dtFinalValidade){
		this.dtFinalValidade=dtFinalValidade;
	}
	public GregorianCalendar getDtFinalValidade(){
		return this.dtFinalValidade;
	}
	public void setIdTabelaComissao(String idTabelaComissao){
		this.idTabelaComissao=idTabelaComissao;
	}
	public String getIdTabelaComissao(){
		return this.idTabelaComissao;
	}
	public void setLgProgressiva(int lgProgressiva){
		this.lgProgressiva=lgProgressiva;
	}
	public int getLgProgressiva(){
		return this.lgProgressiva;
	}
	public void setVlAplicacao(float vlAplicacao){
		this.vlAplicacao=vlAplicacao;
	}
	public float getVlAplicacao(){
		return this.vlAplicacao;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabelaComissao: " +  getCdTabelaComissao();
		valueToString += ", nmTabelaComissao: " +  getNmTabelaComissao();
		valueToString += ", dtInicioValidade: " +  sol.util.Util.formatDateTime(getDtInicioValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinalValidade: " +  sol.util.Util.formatDateTime(getDtFinalValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idTabelaComissao: " +  getIdTabelaComissao();
		valueToString += ", lgProgressiva: " +  getLgProgressiva();
		valueToString += ", vlAplicacao: " +  getVlAplicacao();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TabelaComissao(getCdTabelaComissao(),
			getNmTabelaComissao(),
			getDtInicioValidade()==null ? null : (GregorianCalendar)getDtInicioValidade().clone(),
			getDtFinalValidade()==null ? null : (GregorianCalendar)getDtFinalValidade().clone(),
			getIdTabelaComissao(),
			getLgProgressiva(),
			getVlAplicacao(),
			getCdEmpresa());
	}

}
