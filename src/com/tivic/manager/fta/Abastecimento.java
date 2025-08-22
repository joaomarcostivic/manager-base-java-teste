package com.tivic.manager.fta;

import java.util.GregorianCalendar;

public class Abastecimento {

	private int cdAbastecimento;
	private int cdContaPagar;
	private int cdVeiculo;
	private float qtHodometro;
	private int lgCompleto;
	private int stAbastecimento;
	private float qtLitrosAutorizada;
	private float qtLitrosAbastecida;
	private float vlAutorizado;
	private float vlAbastecido;
	private GregorianCalendar dtAutorizacao;
	private GregorianCalendar dtAbastecimento;
	private float vlLitroCombustivel;
	private int tpCombustivel;
	private int cdResponsavel;
	private String nrAutorizacao;
	private int qtViasImpressas;
	private int cdFornecedor;
	private int cdAutorizacao;
	private int tpAbastecimento;

	public Abastecimento(int cdAbastecimento,
			int cdContaPagar,
			int cdVeiculo,
			float qtHodometro,
			int lgCompleto,
			int stAbastecimento,
			float qtLitrosAutorizada,
			float qtLitrosAbastecida,
			float vlAutorizado,
			float vlAbastecido,
			GregorianCalendar dtAutorizacao,
			GregorianCalendar dtAbastecimento,
			float vlLitroCombustivel,
			int tpCombustivel,
			int cdResponsavel,
			String nrAutorizacao,
			int qtViasImpressas,
			int cdFornecedor,
			int cdAutorizacao,
			int tpAbastecimento){
		setCdAbastecimento(cdAbastecimento);
		setCdContaPagar(cdContaPagar);
		setCdVeiculo(cdVeiculo);
		setQtHodometro(qtHodometro);
		setLgCompleto(lgCompleto);
		setStAbastecimento(stAbastecimento);
		setQtLitrosAutorizada(qtLitrosAutorizada);
		setQtLitrosAbastecida(qtLitrosAbastecida);
		setVlAutorizado(vlAutorizado);
		setVlAbastecido(vlAbastecido);
		setDtAutorizacao(dtAutorizacao);
		setDtAbastecimento(dtAbastecimento);
		setVlLitroCombustivel(vlLitroCombustivel);
		setTpCombustivel(tpCombustivel);
		setCdResponsavel(cdResponsavel);
		setNrAutorizacao(nrAutorizacao);
		setQtViasImpressas(qtViasImpressas);
		setCdFornecedor(cdFornecedor);
		setCdAutorizacao(cdAutorizacao);
		setTpAbastecimento(tpAbastecimento);
	}
	public void setCdAbastecimento(int cdAbastecimento){
		this.cdAbastecimento=cdAbastecimento;
	}
	public int getCdAbastecimento(){
		return this.cdAbastecimento;
	}
	public void setCdContaPagar(int cdContaPagar){
		this.cdContaPagar=cdContaPagar;
	}
	public int getCdContaPagar(){
		return this.cdContaPagar;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setQtHodometro(float qtHodometro){
		this.qtHodometro=qtHodometro;
	}
	public float getQtHodometro(){
		return this.qtHodometro;
	}
	public void setLgCompleto(int lgCompleto){
		this.lgCompleto=lgCompleto;
	}
	public int getLgCompleto(){
		return this.lgCompleto;
	}
	public void setStAbastecimento(int stAbastecimento){
		this.stAbastecimento=stAbastecimento;
	}
	public int getStAbastecimento(){
		return this.stAbastecimento;
	}
	public void setQtLitrosAutorizada(float qtLitrosAutorizada){
		this.qtLitrosAutorizada=qtLitrosAutorizada;
	}
	public float getQtLitrosAutorizada(){
		return this.qtLitrosAutorizada;
	}
	public void setQtLitrosAbastecida(float qtLitrosAbastecida){
		this.qtLitrosAbastecida=qtLitrosAbastecida;
	}
	public float getQtLitrosAbastecida(){
		return this.qtLitrosAbastecida;
	}
	public void setVlAutorizado(float vlAutorizado){
		this.vlAutorizado=vlAutorizado;
	}
	public float getVlAutorizado(){
		return this.vlAutorizado;
	}
	public void setVlAbastecido(float vlAbastecido){
		this.vlAbastecido=vlAbastecido;
	}
	public float getVlAbastecido(){
		return this.vlAbastecido;
	}
	public void setDtAutorizacao(GregorianCalendar dtAutorizacao){
		this.dtAutorizacao=dtAutorizacao;
	}
	public GregorianCalendar getDtAutorizacao(){
		return this.dtAutorizacao;
	}
	public void setDtAbastecimento(GregorianCalendar dtAbastecimento){
		this.dtAbastecimento=dtAbastecimento;
	}
	public GregorianCalendar getDtAbastecimento(){
		return this.dtAbastecimento;
	}
	public void setVlLitroCombustivel(float vlLitroCombustivel){
		this.vlLitroCombustivel=vlLitroCombustivel;
	}
	public float getVlLitroCombustivel(){
		return this.vlLitroCombustivel;
	}
	public void setTpCombustivel(int tpCombustivel){
		this.tpCombustivel=tpCombustivel;
	}
	public int getTpCombustivel(){
		return this.tpCombustivel;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public void setNrAutorizacao(String nrAutorizacao){
		this.nrAutorizacao=nrAutorizacao;
	}
	public String getNrAutorizacao(){
		return this.nrAutorizacao;
	}
	public void setQtViasImpressas(int qtViasImpressas){
		this.qtViasImpressas=qtViasImpressas;
	}
	public int getQtViasImpressas(){
		return this.qtViasImpressas;
	}
	public void setCdFornecedor(int cdFornecedor){
		this.cdFornecedor=cdFornecedor;
	}
	public int getCdFornecedor(){
		return this.cdFornecedor;
	}
	public void setCdAutorizacao(int cdAutorizacao){
		this.cdAutorizacao=cdAutorizacao;
	}
	public int getCdAutorizacao(){
		return this.cdAutorizacao;
	}
	public void setTpAbastecimento(int tpAbastecimento){
		this.tpAbastecimento=tpAbastecimento;
	}
	public int getTpAbastecimento(){
		return this.tpAbastecimento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAbastecimento: " +  getCdAbastecimento();
		valueToString += ", cdContaPagar: " +  getCdContaPagar();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", qtHodometro: " +  getQtHodometro();
		valueToString += ", lgCompleto: " +  getLgCompleto();
		valueToString += ", stAbastecimento: " +  getStAbastecimento();
		valueToString += ", qtLitrosAutorizada: " +  getQtLitrosAutorizada();
		valueToString += ", qtLitrosAbastecida: " +  getQtLitrosAbastecida();
		valueToString += ", vlAutorizado: " +  getVlAutorizado();
		valueToString += ", vlAbastecido: " +  getVlAbastecido();
		valueToString += ", dtAutorizacao: " +  sol.util.Util.formatDateTime(getDtAutorizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtAbastecimento: " +  sol.util.Util.formatDateTime(getDtAbastecimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlLitroCombustivel: " +  getVlLitroCombustivel();
		valueToString += ", tpCombustivel: " +  getTpCombustivel();
		valueToString += ", cdResponsavel: " +  getCdResponsavel();
		valueToString += ", nrAutorizacao: " +  getNrAutorizacao();
		valueToString += ", qtViasImpressas: " +  getQtViasImpressas();
		valueToString += ", cdFornecedor: " +  getCdFornecedor();
		valueToString += ", cdAutorizacao: " +  getCdAutorizacao();
		valueToString += ", tpAbastecimento: " +  getTpAbastecimento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Abastecimento(getCdAbastecimento(),
			getCdContaPagar(),
			getCdVeiculo(),
			getQtHodometro(),
			getLgCompleto(),
			getStAbastecimento(),
			getQtLitrosAutorizada(),
			getQtLitrosAbastecida(),
			getVlAutorizado(),
			getVlAbastecido(),
			getDtAutorizacao()==null ? null : (GregorianCalendar)getDtAutorizacao().clone(),
			getDtAbastecimento()==null ? null : (GregorianCalendar)getDtAbastecimento().clone(),
			getVlLitroCombustivel(),
			getTpCombustivel(),
			getCdResponsavel(),
			getNrAutorizacao(),
			getQtViasImpressas(),
			getCdFornecedor(),
			getCdAutorizacao(),
			getTpAbastecimento());
	}

}
