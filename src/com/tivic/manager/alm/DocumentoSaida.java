package com.tivic.manager.alm;

import java.util.GregorianCalendar;

public class DocumentoSaida {

	private int cdDocumentoSaida;
	private int cdTransportadora;
	private int cdEmpresa;
	private int cdCliente;
	private GregorianCalendar dtDocumentoSaida;
	private int stDocumentoSaida;
	private String nrDocumentoSaida;
	private int tpDocumentoSaida;
	private int tpSaida;
	private String nrConhecimento;
	private float vlDesconto;
	private float vlAcrescimo;
	private GregorianCalendar dtEmissao;
	private int tpFrete;
	private String txtMensagem;
	private String txtObservacao;
	private String nrPlacaVeiculo;
	private String sgPlacaVeiculo;
	private String nrVolumes;
	private GregorianCalendar dtSaidaTransportadora;
	private String dsViaTransporte;
	private int cdNaturezaOperacao;
	private String txtCorpoNotaFiscal;
	private float vlPesoLiquido;
	private float vlPesoBruto;
	private String dsEspecieVolumes;
	private String dsMarcaVolumes;
	private float qtVolumes;
	private int tpMovimentoEstoque;
	private int cdVendedor;
	private int cdMoeda;
	private int cdReferenciaEcf;
	private int cdSolicitacaoMaterial;
	private int cdTipoOperacao;
	private float vlTotalDocumento;
	private int cdContrato;
	private float vlFrete;
	private float vlSeguro;
	private int cdDigitador;
	private int cdDocumento;
	private int cdConta;
	private int cdTurno;
	private float vlTotalItens;
	private int nrSerie;
	private int cdViagem;
	private int cdDocumentoEntradaOrigem;
	
	public DocumentoSaida()	{
		
	}
	//Usado para reabastecimento
	public DocumentoSaida(int cdDocumentoSaida,
			int cdEmpresa,
			int cdCliente,
			GregorianCalendar dtDocumentoSaida,
			int stDocumentoSaida,
			String nrDocumentoSaida,
			int tpDocumentoSaida,
			int tpSaida,
			GregorianCalendar dtEmissao,
			int cdNaturezaOperacao,
			int cdVendedor,
			int cdMoeda,
			float vlTotalDocumento,
			int cdDigitador,
			float vlTotalItens){
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdEmpresa(cdEmpresa);
		setCdCliente(cdCliente);
		setDtDocumentoSaida(dtDocumentoSaida);
		setStDocumentoSaida(stDocumentoSaida);
		setNrDocumentoSaida(nrDocumentoSaida);
		setTpDocumentoSaida(tpDocumentoSaida);
		setTpSaida(tpSaida);
		setDtEmissao(dtEmissao);
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setCdVendedor(cdVendedor);
		setCdMoeda(cdMoeda);
		setVlTotalDocumento(vlTotalDocumento);
		setCdDigitador(cdDigitador);
		setVlTotalItens(vlTotalItens);
	}
	
	public DocumentoSaida(int cdDocumentoSaida,
			int cdTransportadora,
			int cdEmpresa,
			int cdCliente,
			GregorianCalendar dtDocumentoSaida,
			int stDocumentoSaida,
			String nrDocumentoSaida,
			int tpDocumentoSaida,
			int tpSaida,
			String nrConhecimento,
			float vlDesconto,
			float vlAcrescimo,
			GregorianCalendar dtEmissao,
			int tpFrete,
			String txtMensagem,
			String txtObservacao,
			String nrPlacaVeiculo,
			String sgPlacaVeiculo,
			String nrVolumes,
			GregorianCalendar dtSaidaTransportadora,
			String dsViaTransporte,
			int cdNaturezaOperacao,
			String txtCorpoNotaFiscal,
			float vlPesoLiquido,
			float vlPesoBruto,
			String dsEspecieVolumes,
			String dsMarcaVolumes,
			float qtVolumes,
			int tpMovimentoEstoque,
			int cdVendedor,
			int cdMoeda,
			int cdReferenciaEcf,
			int cdSolicitacaoMaterial,
			int cdTipoOperacao,
			float vlTotalDocumento,
			int cdContrato,
			float vlFrete,
			float vlSeguro,
			int cdDigitador,
			int cdDocumento,
			int cdConta,
			int cdTurno,
			float vlTotalItens,
			int nrSerie){
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdTransportadora(cdTransportadora);
		setCdEmpresa(cdEmpresa);
		setCdCliente(cdCliente);
		setDtDocumentoSaida(dtDocumentoSaida);
		setStDocumentoSaida(stDocumentoSaida);
		setNrDocumentoSaida(nrDocumentoSaida);
		setTpDocumentoSaida(tpDocumentoSaida);
		setTpSaida(tpSaida);
		setNrConhecimento(nrConhecimento);
		setVlDesconto(vlDesconto);
		setVlAcrescimo(vlAcrescimo);
		setDtEmissao(dtEmissao);
		setTpFrete(tpFrete);
		setTxtMensagem(txtMensagem);
		setTxtObservacao(txtObservacao);
		setNrPlacaVeiculo(nrPlacaVeiculo);
		setSgPlacaVeiculo(sgPlacaVeiculo);
		setNrVolumes(nrVolumes);
		setDtSaidaTransportadora(dtSaidaTransportadora);
		setDsViaTransporte(dsViaTransporte);
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setTxtCorpoNotaFiscal(txtCorpoNotaFiscal);
		setVlPesoLiquido(vlPesoLiquido);
		setVlPesoBruto(vlPesoBruto);
		setDsEspecieVolumes(dsEspecieVolumes);
		setDsMarcaVolumes(dsMarcaVolumes);
		setQtVolumes(qtVolumes);
		setTpMovimentoEstoque(tpMovimentoEstoque);
		setCdVendedor(cdVendedor);
		setCdMoeda(cdMoeda);
		setCdReferenciaEcf(cdReferenciaEcf);
		setCdSolicitacaoMaterial(cdSolicitacaoMaterial);
		setCdTipoOperacao(cdTipoOperacao);
		setVlTotalDocumento(vlTotalDocumento);
		setCdContrato(cdContrato);
		setVlFrete(vlFrete);
		setVlSeguro(vlSeguro);
		setCdDigitador(cdDigitador);
		setCdDocumento(cdDocumento);
		setCdConta(cdConta);
		setCdTurno(cdTurno);
		setVlTotalItens(vlTotalItens);
		setNrSerie(nrSerie);
	}
	
	public DocumentoSaida(int cdDocumentoSaida,
			int cdTransportadora,
			int cdEmpresa,
			int cdCliente,
			GregorianCalendar dtDocumentoSaida,
			int stDocumentoSaida,
			String nrDocumentoSaida,
			int tpDocumentoSaida,
			int tpSaida,
			String nrConhecimento,
			float vlDesconto,
			float vlAcrescimo,
			GregorianCalendar dtEmissao,
			int tpFrete,
			String txtMensagem,
			String txtObservacao,
			String nrPlacaVeiculo,
			String sgPlacaVeiculo,
			String nrVolumes,
			GregorianCalendar dtSaidaTransportadora,
			String dsViaTransporte,
			int cdNaturezaOperacao,
			String txtCorpoNotaFiscal,
			float vlPesoLiquido,
			float vlPesoBruto,
			String dsEspecieVolumes,
			String dsMarcaVolumes,
			float qtVolumes,
			int tpMovimentoEstoque,
			int cdVendedor,
			int cdMoeda,
			int cdReferenciaEcf,
			int cdSolicitacaoMaterial,
			int cdTipoOperacao,
			float vlTotalDocumento,
			int cdContrato,
			float vlFrete,
			float vlSeguro,
			int cdDigitador,
			int cdDocumento,
			int cdConta,
			int cdTurno,
			float vlTotalItens,
			int nrSerie,
			int cdViagem){
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdTransportadora(cdTransportadora);
		setCdEmpresa(cdEmpresa);
		setCdCliente(cdCliente);
		setDtDocumentoSaida(dtDocumentoSaida);
		setStDocumentoSaida(stDocumentoSaida);
		setNrDocumentoSaida(nrDocumentoSaida);
		setTpDocumentoSaida(tpDocumentoSaida);
		setTpSaida(tpSaida);
		setNrConhecimento(nrConhecimento);
		setVlDesconto(vlDesconto);
		setVlAcrescimo(vlAcrescimo);
		setDtEmissao(dtEmissao);
		setTpFrete(tpFrete);
		setTxtMensagem(txtMensagem);
		setTxtObservacao(txtObservacao);
		setNrPlacaVeiculo(nrPlacaVeiculo);
		setSgPlacaVeiculo(sgPlacaVeiculo);
		setNrVolumes(nrVolumes);
		setDtSaidaTransportadora(dtSaidaTransportadora);
		setDsViaTransporte(dsViaTransporte);
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setTxtCorpoNotaFiscal(txtCorpoNotaFiscal);
		setVlPesoLiquido(vlPesoLiquido);
		setVlPesoBruto(vlPesoBruto);
		setDsEspecieVolumes(dsEspecieVolumes);
		setDsMarcaVolumes(dsMarcaVolumes);
		setQtVolumes(qtVolumes);
		setTpMovimentoEstoque(tpMovimentoEstoque);
		setCdVendedor(cdVendedor);
		setCdMoeda(cdMoeda);
		setCdReferenciaEcf(cdReferenciaEcf);
		setCdSolicitacaoMaterial(cdSolicitacaoMaterial);
		setCdTipoOperacao(cdTipoOperacao);
		setVlTotalDocumento(vlTotalDocumento);
		setCdContrato(cdContrato);
		setVlFrete(vlFrete);
		setVlSeguro(vlSeguro);
		setCdDigitador(cdDigitador);
		setCdDocumento(cdDocumento);
		setCdConta(cdConta);
		setCdTurno(cdTurno);
		setVlTotalItens(vlTotalItens);
		setNrSerie(nrSerie);
		setCdViagem(cdViagem);
	}
	
	/*Devolução de fornecedor*/
	public DocumentoSaida(int cdDocumentoSaida,
			int cdTransportadora,
			int cdEmpresa,
			int cdCliente,
			GregorianCalendar dtDocumentoSaida,
			int stDocumentoSaida,
			String nrDocumentoSaida,
			int tpDocumentoSaida,
			int tpSaida,
			String nrConhecimento,
			float vlDesconto,
			float vlAcrescimo,
			GregorianCalendar dtEmissao,
			int tpFrete,
			String txtMensagem,
			String txtObservacao,
			String nrPlacaVeiculo,
			String sgPlacaVeiculo,
			String nrVolumes,
			GregorianCalendar dtSaidaTransportadora,
			String dsViaTransporte,
			int cdNaturezaOperacao,
			String txtCorpoNotaFiscal,
			float vlPesoLiquido,
			float vlPesoBruto,
			String dsEspecieVolumes,
			String dsMarcaVolumes,
			float qtVolumes,
			int tpMovimentoEstoque,
			int cdVendedor,
			int cdMoeda,
			int cdReferenciaEcf,
			int cdSolicitacaoMaterial,
			int cdTipoOperacao,
			float vlTotalDocumento,
			int cdContrato,
			float vlFrete,
			float vlSeguro,
			int cdDigitador,
			int cdDocumento,
			int cdConta,
			int cdTurno,
			float vlTotalItens,
			int nrSerie,
			int cdViagem,
			int cdDocumentoEntradaOrigem){
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdTransportadora(cdTransportadora);
		setCdEmpresa(cdEmpresa);
		setCdCliente(cdCliente);
		setDtDocumentoSaida(dtDocumentoSaida);
		setStDocumentoSaida(stDocumentoSaida);
		setNrDocumentoSaida(nrDocumentoSaida);
		setTpDocumentoSaida(tpDocumentoSaida);
		setTpSaida(tpSaida);
		setNrConhecimento(nrConhecimento);
		setVlDesconto(vlDesconto);
		setVlAcrescimo(vlAcrescimo);
		setDtEmissao(dtEmissao);
		setTpFrete(tpFrete);
		setTxtMensagem(txtMensagem);
		setTxtObservacao(txtObservacao);
		setNrPlacaVeiculo(nrPlacaVeiculo);
		setSgPlacaVeiculo(sgPlacaVeiculo);
		setNrVolumes(nrVolumes);
		setDtSaidaTransportadora(dtSaidaTransportadora);
		setDsViaTransporte(dsViaTransporte);
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setTxtCorpoNotaFiscal(txtCorpoNotaFiscal);
		setVlPesoLiquido(vlPesoLiquido);
		setVlPesoBruto(vlPesoBruto);
		setDsEspecieVolumes(dsEspecieVolumes);
		setDsMarcaVolumes(dsMarcaVolumes);
		setQtVolumes(qtVolumes);
		setTpMovimentoEstoque(tpMovimentoEstoque);
		setCdVendedor(cdVendedor);
		setCdMoeda(cdMoeda);
		setCdReferenciaEcf(cdReferenciaEcf);
		setCdSolicitacaoMaterial(cdSolicitacaoMaterial);
		setCdTipoOperacao(cdTipoOperacao);
		setVlTotalDocumento(vlTotalDocumento);
		setCdContrato(cdContrato);
		setVlFrete(vlFrete);
		setVlSeguro(vlSeguro);
		setCdDigitador(cdDigitador);
		setCdDocumento(cdDocumento);
		setCdConta(cdConta);
		setCdTurno(cdTurno);
		setVlTotalItens(vlTotalItens);
		setNrSerie(nrSerie);
		setCdViagem(cdViagem);
		setCdDocumentoEntradaOrigem(cdDocumentoEntradaOrigem);
	}
	public void setCdDocumentoSaida(int cdDocumentoSaida){
		this.cdDocumentoSaida=cdDocumentoSaida;
	}
	public int getCdDocumentoSaida(){
		return this.cdDocumentoSaida;
	}
	public void setCdTransportadora(int cdTransportadora){
		this.cdTransportadora=cdTransportadora;
	}
	public int getCdTransportadora(){
		return this.cdTransportadora;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdCliente(int cdCliente){
		this.cdCliente=cdCliente;
	}
	public int getCdCliente(){
		return this.cdCliente;
	}
	public void setDtDocumentoSaida(GregorianCalendar dtDocumentoSaida){
		this.dtDocumentoSaida=dtDocumentoSaida;
	}
	public GregorianCalendar getDtDocumentoSaida(){
		return this.dtDocumentoSaida;
	}
	public void setStDocumentoSaida(int stDocumentoSaida){
		this.stDocumentoSaida=stDocumentoSaida;
	}
	public int getStDocumentoSaida(){
		return this.stDocumentoSaida;
	}
	public void setNrDocumentoSaida(String nrDocumentoSaida){
		this.nrDocumentoSaida=nrDocumentoSaida;
	}
	public String getNrDocumentoSaida(){
		return this.nrDocumentoSaida;
	}
	public void setTpDocumentoSaida(int tpDocumentoSaida){
		this.tpDocumentoSaida=tpDocumentoSaida;
	}
	public int getTpDocumentoSaida(){
		return this.tpDocumentoSaida;
	}
	public void setTpSaida(int tpSaida){
		this.tpSaida=tpSaida;
	}
	public int getTpSaida(){
		return this.tpSaida;
	}
	public void setNrConhecimento(String nrConhecimento){
		this.nrConhecimento=nrConhecimento;
	}
	public String getNrConhecimento(){
		return this.nrConhecimento;
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
	public void setDtEmissao(GregorianCalendar dtEmissao){
		this.dtEmissao=dtEmissao;
	}
	public GregorianCalendar getDtEmissao(){
		return this.dtEmissao;
	}
	public void setTpFrete(int tpFrete){
		this.tpFrete=tpFrete;
	}
	public int getTpFrete(){
		return this.tpFrete;
	}
	public void setTxtMensagem(String txtMensagem){
		this.txtMensagem=txtMensagem;
	}
	public String getTxtMensagem(){
		return this.txtMensagem;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
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
	public void setNrVolumes(String nrVolumes){
		this.nrVolumes=nrVolumes;
	}
	public String getNrVolumes(){
		return this.nrVolumes;
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
	public void setCdNaturezaOperacao(int cdNaturezaOperacao){
		this.cdNaturezaOperacao=cdNaturezaOperacao;
	}
	public int getCdNaturezaOperacao(){
		return this.cdNaturezaOperacao;
	}
	public void setTxtCorpoNotaFiscal(String txtCorpoNotaFiscal){
		this.txtCorpoNotaFiscal=txtCorpoNotaFiscal;
	}
	public String getTxtCorpoNotaFiscal(){
		return this.txtCorpoNotaFiscal;
	}
	public void setVlPesoLiquido(float vlPesoLiquido){
		this.vlPesoLiquido=vlPesoLiquido;
	}
	public float getVlPesoLiquido(){
		return this.vlPesoLiquido;
	}
	public void setVlPesoBruto(float vlPesoBruto){
		this.vlPesoBruto=vlPesoBruto;
	}
	public float getVlPesoBruto(){
		return this.vlPesoBruto;
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
	public void setQtVolumes(float qtVolumes){
		this.qtVolumes=qtVolumes;
	}
	public float getQtVolumes(){
		return this.qtVolumes;
	}
	public void setTpMovimentoEstoque(int tpMovimentoEstoque){
		this.tpMovimentoEstoque=tpMovimentoEstoque;
	}
	public int getTpMovimentoEstoque(){
		return this.tpMovimentoEstoque;
	}
	public void setCdVendedor(int cdVendedor){
		this.cdVendedor=cdVendedor;
	}
	public int getCdVendedor(){
		return this.cdVendedor;
	}
	public void setCdMoeda(int cdMoeda){
		this.cdMoeda=cdMoeda;
	}
	public int getCdMoeda(){
		return this.cdMoeda;
	}
	public void setCdReferenciaEcf(int cdReferenciaEcf){
		this.cdReferenciaEcf=cdReferenciaEcf;
	}
	public int getCdReferenciaEcf(){
		return this.cdReferenciaEcf;
	}
	public void setCdSolicitacaoMaterial(int cdSolicitacaoMaterial){
		this.cdSolicitacaoMaterial=cdSolicitacaoMaterial;
	}
	public int getCdSolicitacaoMaterial(){
		return this.cdSolicitacaoMaterial;
	}
	public void setCdTipoOperacao(int cdTipoOperacao){
		this.cdTipoOperacao=cdTipoOperacao;
	}
	public int getCdTipoOperacao(){
		return this.cdTipoOperacao;
	}
	public void setVlTotalDocumento(float vlTotalDocumento){
		this.vlTotalDocumento=vlTotalDocumento;
	}
	public float getVlTotalDocumento(){
		return this.vlTotalDocumento;
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
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
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdTurno(int cdTurno){
		this.cdTurno=cdTurno;
	}
	public int getCdTurno(){
		return this.cdTurno;
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
	public void setCdDocumentoEntradaOrigem(int cdDocumentoEntradaOrigem) {
		this.cdDocumentoEntradaOrigem = cdDocumentoEntradaOrigem;
	}
	public int getCdDocumentoEntradaOrigem() {
		return cdDocumentoEntradaOrigem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", cdTransportadora: " +  getCdTransportadora();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdCliente: " +  getCdCliente();
		valueToString += ", dtDocumentoSaida: " +  sol.util.Util.formatDateTime(getDtDocumentoSaida(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stDocumentoSaida: " +  getStDocumentoSaida();
		valueToString += ", nrDocumentoSaida: " +  getNrDocumentoSaida();
		valueToString += ", tpDocumentoSaida: " +  getTpDocumentoSaida();
		valueToString += ", tpSaida: " +  getTpSaida();
		valueToString += ", nrConhecimento: " +  getNrConhecimento();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		valueToString += ", vlAcrescimo: " +  getVlAcrescimo();
		valueToString += ", dtEmissao: " +  sol.util.Util.formatDateTime(getDtEmissao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpFrete: " +  getTpFrete();
		valueToString += ", txtMensagem: " +  getTxtMensagem();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", nrPlacaVeiculo: " +  getNrPlacaVeiculo();
		valueToString += ", sgPlacaVeiculo: " +  getSgPlacaVeiculo();
		valueToString += ", nrVolumes: " +  getNrVolumes();
		valueToString += ", dtSaidaTransportadora: " +  sol.util.Util.formatDateTime(getDtSaidaTransportadora(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dsViaTransporte: " +  getDsViaTransporte();
		valueToString += ", cdNaturezaOperacao: " +  getCdNaturezaOperacao();
		valueToString += ", txtCorpoNotaFiscal: " +  getTxtCorpoNotaFiscal();
		valueToString += ", vlPesoLiquido: " +  getVlPesoLiquido();
		valueToString += ", vlPesoBruto: " +  getVlPesoBruto();
		valueToString += ", dsEspecieVolumes: " +  getDsEspecieVolumes();
		valueToString += ", dsMarcaVolumes: " +  getDsMarcaVolumes();
		valueToString += ", qtVolumes: " +  getQtVolumes();
		valueToString += ", tpMovimentoEstoque: " +  getTpMovimentoEstoque();
		valueToString += ", cdVendedor: " +  getCdVendedor();
		valueToString += ", cdMoeda: " +  getCdMoeda();
		valueToString += ", cdReferenciaEcf: " +  getCdReferenciaEcf();
		valueToString += ", cdSolicitacaoMaterial: " +  getCdSolicitacaoMaterial();
		valueToString += ", cdTipoOperacao: " +  getCdTipoOperacao();
		valueToString += ", vlTotalDocumento: " +  getVlTotalDocumento();
		valueToString += ", cdContrato: " +  getCdContrato();
		valueToString += ", vlFrete: " +  getVlFrete();
		valueToString += ", vlSeguro: " +  getVlSeguro();
		valueToString += ", cdDigitador: " +  getCdDigitador();
		valueToString += ", cdDocumento: " +  getCdDocumento();
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", vlTotalItens: " +  getVlTotalItens();
		valueToString += ", nrSerie: " +  getNrSerie();
		valueToString += ", cdViagem: " +  getCdViagem();
		valueToString += ", cdDocumentoEntradaOrigem: " +  getCdDocumentoEntradaOrigem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DocumentoSaida(getCdDocumentoSaida(),
			getCdTransportadora(),
			getCdEmpresa(),
			getCdCliente(),
			getDtDocumentoSaida()==null ? null : (GregorianCalendar)getDtDocumentoSaida().clone(),
			getStDocumentoSaida(),
			getNrDocumentoSaida(),
			getTpDocumentoSaida(),
			getTpSaida(),
			getNrConhecimento(),
			getVlDesconto(),
			getVlAcrescimo(),
			getDtEmissao()==null ? null : (GregorianCalendar)getDtEmissao().clone(),
			getTpFrete(),
			getTxtMensagem(),
			getTxtObservacao(),
			getNrPlacaVeiculo(),
			getSgPlacaVeiculo(),
			getNrVolumes(),
			getDtSaidaTransportadora()==null ? null : (GregorianCalendar)getDtSaidaTransportadora().clone(),
			getDsViaTransporte(),
			getCdNaturezaOperacao(),
			getTxtCorpoNotaFiscal(),
			getVlPesoLiquido(),
			getVlPesoBruto(),
			getDsEspecieVolumes(),
			getDsMarcaVolumes(),
			getQtVolumes(),
			getTpMovimentoEstoque(),
			getCdVendedor(),
			getCdMoeda(),
			getCdReferenciaEcf(),
			getCdSolicitacaoMaterial(),
			getCdTipoOperacao(),
			getVlTotalDocumento(),
			getCdContrato(),
			getVlFrete(),
			getVlSeguro(),
			getCdDigitador(),
			getCdDocumento(),
			getCdConta(),
			getCdTurno(),
			getVlTotalItens(),
			getNrSerie(),
			getCdViagem(),
			getCdDocumentoEntradaOrigem());
	}

}
