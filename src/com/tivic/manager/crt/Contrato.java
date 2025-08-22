package com.tivic.manager.crt;

import java.util.GregorianCalendar;

public class Contrato {

	private int cdContratoEmprestimo;
	private int cdContratante;
	private int cdAgente;
	private int cdEmpresa;
	private int cdProduto;
	private int cdPlano;
	private int stContrato;
	private int qtParcelas;
	private float vlParcelas;
	private GregorianCalendar dtPagamento;
	private float vlFinanciado;
	private float vlTac;
	private float vlLiberado;
	private int cdVinculo;
	private int cdSituacao;
	private float prJuros;
	private GregorianCalendar dtContrato;
	private int cdOperacao;
	private int cdMotivo;
	private int cdUsuario;
	private GregorianCalendar dtOperacao;
	private String nrContrato;
	private int cdSubagente;
	private int lgExpontaneo;
	private int cdOrgao;
	private String nrInscricao;
	private int cdTabelaComissao;
	private GregorianCalendar dtCadastro;
	private int cdAtendente;
	private String dsObservacao;

	public Contrato(int cdContratoEmprestimo,
			int cdContratante,
			int cdAgente,
			int cdEmpresa,
			int cdProduto,
			int cdPlano,
			int stContrato,
			int qtParcelas,
			float vlParcelas,
			GregorianCalendar dtPagamento,
			float vlFinanciado,
			float vlTac,
			float vlLiberado,
			int cdVinculo,
			int cdSituacao,
			float prJuros,
			GregorianCalendar dtContrato,
			int cdOperacao,
			int cdMotivo,
			int cdUsuario,
			GregorianCalendar dtOperacao,
			String nrContrato,
			int cdSubagente,
			int lgExpontaneo,
			int cdOrgao,
			String nrInscricao,
			int cdTabelaComissao,
			GregorianCalendar dtCadastro,
			int cdAtendente,
			String dsObservacao){
		setCdContratoEmprestimo(cdContratoEmprestimo);
		setCdContratante(cdContratante);
		setCdAgente(cdAgente);
		setCdEmpresa(cdEmpresa);
		setCdProduto(cdProduto);
		setCdPlano(cdPlano);
		setStContrato(stContrato);
		setQtParcelas(qtParcelas);
		setVlParcelas(vlParcelas);
		setDtPagamento(dtPagamento);
		setVlFinanciado(vlFinanciado);
		setVlTac(vlTac);
		setVlLiberado(vlLiberado);
		setCdVinculo(cdVinculo);
		setCdSituacao(cdSituacao);
		setPrJuros(prJuros);
		setDtContrato(dtContrato);
		setCdOperacao(cdOperacao);
		setCdMotivo(cdMotivo);
		setCdUsuario(cdUsuario);
		setDtOperacao(dtOperacao);
		setNrContrato(nrContrato);
		setCdSubagente(cdSubagente);
		setLgExpontaneo(lgExpontaneo);
		setCdOrgao(cdOrgao);
		setNrInscricao(nrInscricao);
		setCdTabelaComissao(cdTabelaComissao);
		setDtCadastro(dtCadastro);
		setCdAtendente(cdAtendente);
		setDsObservacao(dsObservacao);
	}
	public void setCdContratoEmprestimo(int cdContratoEmprestimo){
		this.cdContratoEmprestimo=cdContratoEmprestimo;
	}
	public int getCdContratoEmprestimo(){
		return this.cdContratoEmprestimo;
	}
	public void setCdContratante(int cdContratante){
		this.cdContratante=cdContratante;
	}
	public int getCdContratante(){
		return this.cdContratante;
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdProduto(int cdProduto){
		this.cdProduto=cdProduto;
	}
	public int getCdProduto(){
		return this.cdProduto;
	}
	public void setCdPlano(int cdPlano){
		this.cdPlano=cdPlano;
	}
	public int getCdPlano(){
		return this.cdPlano;
	}
	public void setStContrato(int stContrato){
		this.stContrato=stContrato;
	}
	public int getStContrato(){
		return this.stContrato;
	}
	public void setQtParcelas(int qtParcelas){
		this.qtParcelas=qtParcelas;
	}
	public int getQtParcelas(){
		return this.qtParcelas;
	}
	public void setVlParcelas(float vlParcelas){
		this.vlParcelas=vlParcelas;
	}
	public float getVlParcelas(){
		return this.vlParcelas;
	}
	public void setDtPagamento(GregorianCalendar dtPagamento){
		this.dtPagamento=dtPagamento;
	}
	public GregorianCalendar getDtPagamento(){
		return this.dtPagamento;
	}
	public void setVlFinanciado(float vlFinanciado){
		this.vlFinanciado=vlFinanciado;
	}
	public float getVlFinanciado(){
		return this.vlFinanciado;
	}
	public void setVlTac(float vlTac){
		this.vlTac=vlTac;
	}
	public float getVlTac(){
		return this.vlTac;
	}
	public void setVlLiberado(float vlLiberado){
		this.vlLiberado=vlLiberado;
	}
	public float getVlLiberado(){
		return this.vlLiberado;
	}
	public void setCdVinculo(int cdVinculo){
		this.cdVinculo=cdVinculo;
	}
	public int getCdVinculo(){
		return this.cdVinculo;
	}
	public void setCdSituacao(int cdSituacao){
		this.cdSituacao=cdSituacao;
	}
	public int getCdSituacao(){
		return this.cdSituacao;
	}
	public void setPrJuros(float prJuros){
		this.prJuros=prJuros;
	}
	public float getPrJuros(){
		return this.prJuros;
	}
	public void setDtContrato(GregorianCalendar dtContrato){
		this.dtContrato=dtContrato;
	}
	public GregorianCalendar getDtContrato(){
		return this.dtContrato;
	}
	public void setCdOperacao(int cdOperacao){
		this.cdOperacao=cdOperacao;
	}
	public int getCdOperacao(){
		return this.cdOperacao;
	}
	public void setCdMotivo(int cdMotivo){
		this.cdMotivo=cdMotivo;
	}
	public int getCdMotivo(){
		return this.cdMotivo;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setDtOperacao(GregorianCalendar dtOperacao){
		this.dtOperacao=dtOperacao;
	}
	public GregorianCalendar getDtOperacao(){
		return this.dtOperacao;
	}
	public void setNrContrato(String nrContrato){
		this.nrContrato=nrContrato;
	}
	public String getNrContrato(){
		return this.nrContrato;
	}
	public void setCdSubagente(int cdSubagente){
		this.cdSubagente=cdSubagente;
	}
	public int getCdSubagente(){
		return this.cdSubagente;
	}
	public void setLgExpontaneo(int lgExpontaneo){
		this.lgExpontaneo=lgExpontaneo;
	}
	public int getLgExpontaneo(){
		return this.lgExpontaneo;
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public void setNrInscricao(String nrInscricao){
		this.nrInscricao=nrInscricao;
	}
	public String getNrInscricao(){
		return this.nrInscricao;
	}
	public void setCdTabelaComissao(int cdTabelaComissao){
		this.cdTabelaComissao=cdTabelaComissao;
	}
	public int getCdTabelaComissao(){
		return this.cdTabelaComissao;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setCdAtendente(int cdAtendente){
		this.cdAtendente=cdAtendente;
	}
	public int getCdAtendente(){
		return this.cdAtendente;
	}
	public void setDsObservacao(String dsObservacao){
		this.dsObservacao=dsObservacao;
	}
	public String getDsObservacao(){
		return this.dsObservacao;
	}
}