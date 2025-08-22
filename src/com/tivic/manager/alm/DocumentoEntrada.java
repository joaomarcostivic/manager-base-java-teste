package com.tivic.manager.alm;

import java.util.GregorianCalendar;

public class DocumentoEntrada {

	private int cdDocumentoEntrada;
	private int cdEmpresa;
	private int cdTransportadora;
	private int cdFornecedor;
	private GregorianCalendar dtEmissao;
	private GregorianCalendar dtDocumentoEntrada;
	private int stDocumentoEntrada;
	private float vlDesconto;
	private float vlAcrescimo;
	private String nrDocumentoEntrada;
	private int tpDocumentoEntrada;
	private String nrConhecimento;
	private int tpEntrada;
	private String txtObservacao;
	private int cdNaturezaOperacao;
	private int tpFrete;
	private String nrPlacaVeiculo;
	private String sgPlacaVeiculo;
	private float qtVolumes;
	private GregorianCalendar dtSaidaTransportadora;
	private String dsViaTransporte;
	private String txtCorpoNotaFiscal;
	private float vlPesoBruto;
	private float vlPesoLiquido;
	private String dsEspecieVolumes;
	private String dsMarcaVolumes;
	private String nrVolumes;
	private int tpMovimentoEstoque;
	private int cdMoeda;
	private int cdTabelaPreco;
	private float vlTotalDocumento;
	private int cdDocumentoSaidaOrigem;
	private float vlFrete;
	private float vlSeguro;
	private int cdDigitador;
	private float vlTotalItens;
	private int nrSerie;
	private int cdViagem;
	
	public DocumentoEntrada() {
		
	}
	
	//Usado na transferência
	public DocumentoEntrada(int cdDocumentoEntrada,
			int cdEmpresa,
			int cdFornecedor,
			GregorianCalendar dtEmissao,
			GregorianCalendar dtDocumentoEntrada,
			int stDocumentoEntrada,
			String nrDocumentoEntrada,
			int tpDocumentoEntrada,
			int tpEntrada,
			int cdNaturezaOperacao,
			int tpMovimentoEstoque,
			int cdMoeda,
			float vlTotalDocumento,
			int cdDocumentoSaidaOrigem,
			int cdDigitador,
			float vlTotalItens){
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdEmpresa(cdEmpresa);
		setCdFornecedor(cdFornecedor);
		setDtEmissao(dtEmissao);
		setDtDocumentoEntrada(dtDocumentoEntrada);
		setStDocumentoEntrada(stDocumentoEntrada);
		setNrDocumentoEntrada(nrDocumentoEntrada);
		setTpDocumentoEntrada(tpDocumentoEntrada);
		setTpEntrada(tpEntrada);
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setTpMovimentoEstoque(tpMovimentoEstoque);
		setCdMoeda(cdMoeda);
		setCdTabelaPreco(cdTabelaPreco);
		setVlTotalDocumento(vlTotalDocumento);
		setCdDocumentoSaidaOrigem(cdDocumentoSaidaOrigem);
		setVlFrete(vlFrete);
		setVlSeguro(vlSeguro);
		setCdDigitador(cdDigitador);
		setVlTotalItens(vlTotalItens);
		setNrSerie(nrSerie);
		setCdViagem(cdViagem);
	}
	
	public DocumentoEntrada(int cdDocumentoEntrada,
			int cdEmpresa,
			int cdTransportadora,
			int cdFornecedor,
			GregorianCalendar dtEmissao,
			GregorianCalendar dtDocumentoEntrada,
			int stDocumentoEntrada,
			float vlDesconto,
			float vlAcrescimo,
			String nrDocumentoEntrada,
			int tpDocumentoEntrada,
			String nrConhecimento,
			int tpEntrada,
			String txtObservacao,
			int cdNaturezaOperacao,
			int tpFrete,
			String nrPlacaVeiculo,
			String sgPlacaVeiculo,
			float qtVolumes,
			GregorianCalendar dtSaidaTransportadora,
			String dsViaTransporte,
			String txtCorpoNotaFiscal,
			float vlPesoBruto,
			float vlPesoLiquido,
			String dsEspecieVolumes,
			String dsMarcaVolumes,
			String nrVolumes,
			int tpMovimentoEstoque,
			int cdMoeda,
			int cdTabelaPreco,
			float vlTotalDocumento,
			int cdDocumentoSaidaOrigem,
			float vlFrete,
			float vlSeguro,
			int cdDigitador,
			float vlTotalItens,
			int nrSerie,
			int cdViagem){
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdEmpresa(cdEmpresa);
		setCdTransportadora(cdTransportadora);
		setCdFornecedor(cdFornecedor);
		setDtEmissao(dtEmissao);
		setDtDocumentoEntrada(dtDocumentoEntrada);
		setStDocumentoEntrada(stDocumentoEntrada);
		setVlDesconto(vlDesconto);
		setVlAcrescimo(vlAcrescimo);
		setNrDocumentoEntrada(nrDocumentoEntrada);
		setTpDocumentoEntrada(tpDocumentoEntrada);
		setNrConhecimento(nrConhecimento);
		setTpEntrada(tpEntrada);
		setTxtObservacao(txtObservacao);
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setTpFrete(tpFrete);
		setNrPlacaVeiculo(nrPlacaVeiculo);
		setSgPlacaVeiculo(sgPlacaVeiculo);
		setQtVolumes(qtVolumes);
		setDtSaidaTransportadora(dtSaidaTransportadora);
		setDsViaTransporte(dsViaTransporte);
		setTxtCorpoNotaFiscal(txtCorpoNotaFiscal);
		setVlPesoBruto(vlPesoBruto);
		setVlPesoLiquido(vlPesoLiquido);
		setDsEspecieVolumes(dsEspecieVolumes);
		setDsMarcaVolumes(dsMarcaVolumes);
		setNrVolumes(nrVolumes);
		setTpMovimentoEstoque(tpMovimentoEstoque);
		setCdMoeda(cdMoeda);
		setCdTabelaPreco(cdTabelaPreco);
		setVlTotalDocumento(vlTotalDocumento);
		setCdDocumentoSaidaOrigem(cdDocumentoSaidaOrigem);
		setVlFrete(vlFrete);
		setVlSeguro(vlSeguro);
		setCdDigitador(cdDigitador);
		setVlTotalItens(vlTotalItens);
		setNrSerie(nrSerie);
		setCdViagem(cdViagem);
	}
	
	public DocumentoEntrada(int cdDocumentoEntrada,
			int cdEmpresa,
			int cdTransportadora,
			int cdFornecedor,
			GregorianCalendar dtEmissao,
			GregorianCalendar dtDocumentoEntrada,
			int stDocumentoEntrada,
			float vlDesconto,
			float vlAcrescimo,
			String nrDocumentoEntrada,
			int tpDocumentoEntrada,
			String nrConhecimento,
			int tpEntrada,
			String txtObservacao,
			int cdNaturezaOperacao,
			int tpFrete,
			String nrPlacaVeiculo,
			String sgPlacaVeiculo,
			float qtVolumes,
			GregorianCalendar dtSaidaTransportadora,
			String dsViaTransporte,
			String txtCorpoNotaFiscal,
			float vlPesoBruto,
			float vlPesoLiquido,
			String dsEspecieVolumes,
			String dsMarcaVolumes,
			String nrVolumes,
			int tpMovimentoEstoque,
			int cdMoeda,
			int cdTabelaPreco,
			float vlTotalDocumento,
			int cdDocumentoSaidaOrigem,
			float vlFrete,
			float vlSeguro,
			int cdDigitador,
			float vlTotalItens,
			int nrSerie){
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdEmpresa(cdEmpresa);
		setCdTransportadora(cdTransportadora);
		setCdFornecedor(cdFornecedor);
		setDtEmissao(dtEmissao);
		setDtDocumentoEntrada(dtDocumentoEntrada);
		setStDocumentoEntrada(stDocumentoEntrada);
		setVlDesconto(vlDesconto);
		setVlAcrescimo(vlAcrescimo);
		setNrDocumentoEntrada(nrDocumentoEntrada);
		setTpDocumentoEntrada(tpDocumentoEntrada);
		setNrConhecimento(nrConhecimento);
		setTpEntrada(tpEntrada);
		setTxtObservacao(txtObservacao);
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setTpFrete(tpFrete);
		setNrPlacaVeiculo(nrPlacaVeiculo);
		setSgPlacaVeiculo(sgPlacaVeiculo);
		setQtVolumes(qtVolumes);
		setDtSaidaTransportadora(dtSaidaTransportadora);
		setDsViaTransporte(dsViaTransporte);
		setTxtCorpoNotaFiscal(txtCorpoNotaFiscal);
		setVlPesoBruto(vlPesoBruto);
		setVlPesoLiquido(vlPesoLiquido);
		setDsEspecieVolumes(dsEspecieVolumes);
		setDsMarcaVolumes(dsMarcaVolumes);
		setNrVolumes(nrVolumes);
		setTpMovimentoEstoque(tpMovimentoEstoque);
		setCdMoeda(cdMoeda);
		setCdTabelaPreco(cdTabelaPreco);
		setVlTotalDocumento(vlTotalDocumento);
		setCdDocumentoSaidaOrigem(cdDocumentoSaidaOrigem);
		setVlFrete(vlFrete);
		setVlSeguro(vlSeguro);
		setCdDigitador(cdDigitador);
		setVlTotalItens(vlTotalItens);
		setNrSerie(nrSerie);
	}
	/**
	 * Constrtor usado na venda externa
	 * @param cdDocumentoEntrada
	 * @param cdEmpresa
	 * @param cdTransportadora
	 * @param cdFornecedor
	 * @param stDocumentoEntrada
	 * @param tpDocumentoEntrada
	 * @param tpEntrada
	 * @param cdNaturezaOperacao
	 * @param cdMoeda
	 * @param cdDigitador
	 * @param nrSerie
	 * @param cdViagem
	 */
	public DocumentoEntrada(int cdDocumentoEntrada,
			int cdEmpresa,
			int cdTransportadora,
			int cdFornecedor,
			GregorianCalendar dtEmissao,
			GregorianCalendar dtDocumentoEntrada,
			int stDocumentoEntrada,
			int tpDocumentoEntrada,
			int tpEntrada,
			int cdNaturezaOperacao,
			int tpMovimentoEstoque,
			int cdMoeda,
			int cdDigitador,
			int nrSerie,
			int cdViagem){
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdEmpresa(cdEmpresa);
		setCdTransportadora(cdTransportadora);
		setCdFornecedor(cdFornecedor);
		setDtEmissao(dtEmissao);
		setDtDocumentoEntrada(dtDocumentoEntrada);
		setStDocumentoEntrada(stDocumentoEntrada);
		setTpDocumentoEntrada(tpDocumentoEntrada);
		setTpEntrada(tpEntrada);
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setTpMovimentoEstoque(tpMovimentoEstoque);
		setCdMoeda(cdMoeda);
		setCdDigitador(cdDigitador);
		setNrSerie(nrSerie);
		setCdViagem(cdViagem);
	}
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdTransportadora(int cdTransportadora){
		this.cdTransportadora=cdTransportadora;
	}
	public int getCdTransportadora(){
		return this.cdTransportadora;
	}
	public void setCdFornecedor(int cdFornecedor){
		this.cdFornecedor=cdFornecedor;
	}
	public int getCdFornecedor(){
		return this.cdFornecedor;
	}
	public void setDtEmissao(GregorianCalendar dtEmissao){
		this.dtEmissao=dtEmissao;
	}
	public GregorianCalendar getDtEmissao(){
		return this.dtEmissao;
	}
	public void setDtDocumentoEntrada(GregorianCalendar dtDocumentoEntrada){
		this.dtDocumentoEntrada=dtDocumentoEntrada;
	}
	public GregorianCalendar getDtDocumentoEntrada(){
		return this.dtDocumentoEntrada;
	}
	public void setStDocumentoEntrada(int stDocumentoEntrada){
		this.stDocumentoEntrada=stDocumentoEntrada;
	}
	public int getStDocumentoEntrada(){
		return this.stDocumentoEntrada;
	}
	public void setVlDesconto(float vlDesconto){
		this.vlDesconto=vlDesconto;
	}
	public float getVlDesconto(){
		return this.vlDesconto;
	}
	public void setVlAcrescimo(float vlAcrescimo){
		this.vlAcrescimo=vlAcrescimo;
	}
	public float getVlAcrescimo(){
		return this.vlAcrescimo;
	}
	public void setNrDocumentoEntrada(String nrDocumentoEntrada){
		this.nrDocumentoEntrada=nrDocumentoEntrada;
	}
	public String getNrDocumentoEntrada(){
		return this.nrDocumentoEntrada;
	}
	public void setTpDocumentoEntrada(int tpDocumentoEntrada){
		this.tpDocumentoEntrada=tpDocumentoEntrada;
	}
	public int getTpDocumentoEntrada(){
		return this.tpDocumentoEntrada;
	}
	public void setNrConhecimento(String nrConhecimento){
		this.nrConhecimento=nrConhecimento;
	}
	public String getNrConhecimento(){
		return this.nrConhecimento;
	}
	public void setTpEntrada(int tpEntrada){
		this.tpEntrada=tpEntrada;
	}
	public int getTpEntrada(){
		return this.tpEntrada;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdNaturezaOperacao(int cdNaturezaOperacao){
		this.cdNaturezaOperacao=cdNaturezaOperacao;
	}
	public int getCdNaturezaOperacao(){
		return this.cdNaturezaOperacao;
	}
	public void setTpFrete(int tpFrete){
		this.tpFrete=tpFrete;
	}
	public int getTpFrete(){
		return this.tpFrete;
	}
	public void setNrPlacaVeiculo(String nrPlacaVeiculo){
		this.nrPlacaVeiculo=nrPlacaVeiculo;
	}
	public String getNrPlacaVeiculo(){
		return this.nrPlacaVeiculo;
	}
	public void setSgPlacaVeiculo(String sgPlacaVeiculo){
		this.sgPlacaVeiculo=sgPlacaVeiculo;
	}
	public String getSgPlacaVeiculo(){
		return this.sgPlacaVeiculo;
	}
	public void setQtVolumes(float qtVolumes){
		this.qtVolumes=qtVolumes;
	}
	public float getQtVolumes(){
		return this.qtVolumes;
	}
	public void setDtSaidaTransportadora(GregorianCalendar dtSaidaTransportadora){
		this.dtSaidaTransportadora=dtSaidaTransportadora;
	}
	public GregorianCalendar getDtSaidaTransportadora(){
		return this.dtSaidaTransportadora;
	}
	public void setDsViaTransporte(String dsViaTransporte){
		this.dsViaTransporte=dsViaTransporte;
	}
	public String getDsViaTransporte(){
		return this.dsViaTransporte;
	}
	public void setTxtCorpoNotaFiscal(String txtCorpoNotaFiscal){
		this.txtCorpoNotaFiscal=txtCorpoNotaFiscal;
	}
	public String getTxtCorpoNotaFiscal(){
		return this.txtCorpoNotaFiscal;
	}
	public void setVlPesoBruto(float vlPesoBruto){
		this.vlPesoBruto=vlPesoBruto;
	}
	public float getVlPesoBruto(){
		return this.vlPesoBruto;
	}
	public void setVlPesoLiquido(float vlPesoLiquido){
		this.vlPesoLiquido=vlPesoLiquido;
	}
	public float getVlPesoLiquido(){
		return this.vlPesoLiquido;
	}
	public void setDsEspecieVolumes(String dsEspecieVolumes){
		this.dsEspecieVolumes=dsEspecieVolumes;
	}
	public String getDsEspecieVolumes(){
		return this.dsEspecieVolumes;
	}
	public void setDsMarcaVolumes(String dsMarcaVolumes){
		this.dsMarcaVolumes=dsMarcaVolumes;
	}
	public String getDsMarcaVolumes(){
		return this.dsMarcaVolumes;
	}
	public void setNrVolumes(String nrVolumes){
		this.nrVolumes=nrVolumes;
	}
	public String getNrVolumes(){
		return this.nrVolumes;
	}
	public void setTpMovimentoEstoque(int tpMovimentoEstoque){
		this.tpMovimentoEstoque=tpMovimentoEstoque;
	}
	public int getTpMovimentoEstoque(){
		return this.tpMovimentoEstoque;
	}
	public void setCdMoeda(int cdMoeda){
		this.cdMoeda=cdMoeda;
	}
	public int getCdMoeda(){
		return this.cdMoeda;
	}
	public void setCdTabelaPreco(int cdTabelaPreco){
		this.cdTabelaPreco=cdTabelaPreco;
	}
	public int getCdTabelaPreco(){
		return this.cdTabelaPreco;
	}
	public void setVlTotalDocumento(float vlTotalDocumento){
		this.vlTotalDocumento=vlTotalDocumento;
	}
	public float getVlTotalDocumento(){
		return this.vlTotalDocumento;
	}
	public void setCdDocumentoSaidaOrigem(int cdDocumentoSaidaOrigem){
		this.cdDocumentoSaidaOrigem=cdDocumentoSaidaOrigem;
	}
	public int getCdDocumentoSaidaOrigem(){
		return this.cdDocumentoSaidaOrigem;
	}
	public void setVlFrete(float vlFrete){
		this.vlFrete=vlFrete;
	}
	public float getVlFrete(){
		return this.vlFrete;
	}
	public void setVlSeguro(float vlSeguro){
		this.vlSeguro=vlSeguro;
	}
	public float getVlSeguro(){
		return this.vlSeguro;
	}
	public void setCdDigitador(int cdDigitador){
		this.cdDigitador=cdDigitador;
	}
	public int getCdDigitador(){
		return this.cdDigitador;
	}
	public void setVlTotalItens(float vlTotalItens){
		this.vlTotalItens=vlTotalItens;
	}
	public float getVlTotalItens(){
		return this.vlTotalItens;
	}
	public void setNrSerie(int nrSerie){
		this.nrSerie=nrSerie;
	}
	public int getNrSerie(){
		return this.nrSerie;
	}
	public void setCdViagem(int cdViagem) {
		this.cdViagem = cdViagem;
	}
	public int getCdViagem() {
		return cdViagem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdTransportadora: " +  getCdTransportadora();
		valueToString += ", cdFornecedor: " +  getCdFornecedor();
		valueToString += ", dtEmissao: " +  sol.util.Util.formatDateTime(getDtEmissao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtDocumentoEntrada: " +  sol.util.Util.formatDateTime(getDtDocumentoEntrada(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stDocumentoEntrada: " +  getStDocumentoEntrada();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		valueToString += ", vlAcrescimo: " +  getVlAcrescimo();
		valueToString += ", nrDocumentoEntrada: " +  getNrDocumentoEntrada();
		valueToString += ", tpDocumentoEntrada: " +  getTpDocumentoEntrada();
		valueToString += ", nrConhecimento: " +  getNrConhecimento();
		valueToString += ", tpEntrada: " +  getTpEntrada();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdNaturezaOperacao: " +  getCdNaturezaOperacao();
		valueToString += ", tpFrete: " +  getTpFrete();
		valueToString += ", nrPlacaVeiculo: " +  getNrPlacaVeiculo();
		valueToString += ", sgPlacaVeiculo: " +  getSgPlacaVeiculo();
		valueToString += ", qtVolumes: " +  getQtVolumes();
		valueToString += ", dtSaidaTransportadora: " +  sol.util.Util.formatDateTime(getDtSaidaTransportadora(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dsViaTransporte: " +  getDsViaTransporte();
		valueToString += ", txtCorpoNotaFiscal: " +  getTxtCorpoNotaFiscal();
		valueToString += ", vlPesoBruto: " +  getVlPesoBruto();
		valueToString += ", vlPesoLiquido: " +  getVlPesoLiquido();
		valueToString += ", dsEspecieVolumes: " +  getDsEspecieVolumes();
		valueToString += ", dsMarcaVolumes: " +  getDsMarcaVolumes();
		valueToString += ", nrVolumes: " +  getNrVolumes();
		valueToString += ", tpMovimentoEstoque: " +  getTpMovimentoEstoque();
		valueToString += ", cdMoeda: " +  getCdMoeda();
		valueToString += ", cdTabelaPreco: " +  getCdTabelaPreco();
		valueToString += ", vlTotalDocumento: " +  getVlTotalDocumento();
		valueToString += ", cdDocumentoSaidaOrigem: " +  getCdDocumentoSaidaOrigem();
		valueToString += ", vlFrete: " +  getVlFrete();
		valueToString += ", vlSeguro: " +  getVlSeguro();
		valueToString += ", cdDigitador: " +  getCdDigitador();
		valueToString += ", vlTotalItens: " +  getVlTotalItens();
		valueToString += ", nrSerie: " +  getNrSerie();
		valueToString += ", cdViagem: " +  getCdViagem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DocumentoEntrada(getCdDocumentoEntrada(),
			getCdEmpresa(),
			getCdTransportadora(),
			getCdFornecedor(),
			getDtEmissao()==null ? null : (GregorianCalendar)getDtEmissao().clone(),
			getDtDocumentoEntrada()==null ? null : (GregorianCalendar)getDtDocumentoEntrada().clone(),
			getStDocumentoEntrada(),
			getVlDesconto(),
			getVlAcrescimo(),
			getNrDocumentoEntrada(),
			getTpDocumentoEntrada(),
			getNrConhecimento(),
			getTpEntrada(),
			getTxtObservacao(),
			getCdNaturezaOperacao(),
			getTpFrete(),
			getNrPlacaVeiculo(),
			getSgPlacaVeiculo(),
			getQtVolumes(),
			getDtSaidaTransportadora()==null ? null : (GregorianCalendar)getDtSaidaTransportadora().clone(),
			getDsViaTransporte(),
			getTxtCorpoNotaFiscal(),
			getVlPesoBruto(),
			getVlPesoLiquido(),
			getDsEspecieVolumes(),
			getDsMarcaVolumes(),
			getNrVolumes(),
			getTpMovimentoEstoque(),
			getCdMoeda(),
			getCdTabelaPreco(),
			getVlTotalDocumento(),
			getCdDocumentoSaidaOrigem(),
			getVlFrete(),
			getVlSeguro(),
			getCdDigitador(),
			getVlTotalItens(),
			getNrSerie(),
			getCdViagem());
	}

}
