package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class TabelaComissaoMeta {

	private int cdTabelaComissao;
	private int cdMeta;
	private int cdGrupo;
	private int cdProdutoServico;
	private int cdRegiao;
	private int cdAgente;
	private int cdTipoOperacao;
	private int tpMeta;
	private int tpPeriodo;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private float vlMeta;

	public TabelaComissaoMeta(int cdTabelaComissao,
			int cdMeta,
			int cdGrupo,
			int cdProdutoServico,
			int cdRegiao,
			int cdAgente,
			int cdTipoOperacao,
			int tpMeta,
			int tpPeriodo,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			float vlMeta){
		setCdTabelaComissao(cdTabelaComissao);
		setCdMeta(cdMeta);
		setCdGrupo(cdGrupo);
		setCdProdutoServico(cdProdutoServico);
		setCdRegiao(cdRegiao);
		setCdAgente(cdAgente);
		setCdTipoOperacao(cdTipoOperacao);
		setTpMeta(tpMeta);
		setTpPeriodo(tpPeriodo);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setVlMeta(vlMeta);
	}
	public void setCdTabelaComissao(int cdTabelaComissao){
		this.cdTabelaComissao=cdTabelaComissao;
	}
	public int getCdTabelaComissao(){
		return this.cdTabelaComissao;
	}
	public void setCdMeta(int cdMeta){
		this.cdMeta=cdMeta;
	}
	public int getCdMeta(){
		return this.cdMeta;
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdRegiao(int cdRegiao){
		this.cdRegiao=cdRegiao;
	}
	public int getCdRegiao(){
		return this.cdRegiao;
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
	}
	public void setCdTipoOperacao(int cdTipoOperacao){
		this.cdTipoOperacao=cdTipoOperacao;
	}
	public int getCdTipoOperacao(){
		return this.cdTipoOperacao;
	}
	public void setTpMeta(int tpMeta){
		this.tpMeta=tpMeta;
	}
	public int getTpMeta(){
		return this.tpMeta;
	}
	public void setTpPeriodo(int tpPeriodo){
		this.tpPeriodo=tpPeriodo;
	}
	public int getTpPeriodo(){
		return this.tpPeriodo;
	}
	public void setDtInicial(GregorianCalendar dtInicial){
		this.dtInicial=dtInicial;
	}
	public GregorianCalendar getDtInicial(){
		return this.dtInicial;
	}
	public void setDtFinal(GregorianCalendar dtFinal){
		this.dtFinal=dtFinal;
	}
	public GregorianCalendar getDtFinal(){
		return this.dtFinal;
	}
	public void setVlMeta(float vlMeta){
		this.vlMeta=vlMeta;
	}
	public float getVlMeta(){
		return this.vlMeta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabelaComissao: " +  getCdTabelaComissao();
		valueToString += ", cdMeta: " +  getCdMeta();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdRegiao: " +  getCdRegiao();
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", cdTipoOperacao: " +  getCdTipoOperacao();
		valueToString += ", tpMeta: " +  getTpMeta();
		valueToString += ", tpPeriodo: " +  getTpPeriodo();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlMeta: " +  getVlMeta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TabelaComissaoMeta(getCdTabelaComissao(),
			getCdMeta(),
			getCdGrupo(),
			getCdProdutoServico(),
			getCdRegiao(),
			getCdAgente(),
			getCdTipoOperacao(),
			getTpMeta(),
			getTpPeriodo(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getVlMeta());
	}

}
