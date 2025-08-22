package com.tivic.manager.fsc;

import java.util.GregorianCalendar;

public class NotaFiscal {

	private int cdNotaFiscal;
	private int cdEmpresa;
	private int cdEnderecoRetirada;
	private int cdNaturezaOperacao;
	private int cdCidade;
	private int cdDestinatario;
	private int cdEnderecoDestinatario;
	private int cdEnderecoEntrega;
	private int cdLote;
	private int tpModelo;
	private String nrSerie;
	private String nrNotaFiscal;
	private int stNotaFiscal;
	private GregorianCalendar dtEmissao;
	private int tpMovimento;
	private GregorianCalendar dtMovimentacao;
	private int tpPagamento;
	private int tpEmissao;
	private int tpFinalidade;
	private int tpDanfe;
	private float vlTotalProduto;
	private float vlSeguro;
	private float vlOutrasDespesas;
	private float vlTotalNota;
	private int tpModalidadeFrete;
	private String txtObservacao;
	private String txtInformacaoFisco;
	private int lgDanfeImpresso;
	private String nrChaveAcesso;
	private int nrDv;
	private String nrProtocoloAutorizacao;
	private GregorianCalendar dtAutorizacao;
	private int cdTransportador;
	private int cdNaturezaOperacaoFrete;
	private float vlFrete;
	private float vlFreteBaseIcms;
	private float vlFreteIcmsRetido;
	private String nrRecebimento;
	private String nrPlaca;
	private String qtVolume;
	private String dsEspecie;
	private String dsMarca;
	private String dsNumeracao;
	private float vlPesoBruto;
	private float vlPesoLiquido;
	private int cdVeiculo;
	private String sgUfVeiculo;
	private String nrRntc;
	private int cdMotivoCancelamento;
	private String txtXml;
	private float prDesconto;
	private int lgConsumidorFinal;
	private int tpVendaPresenca;
	private String nrChaveAcessoReferencia;
	
	public NotaFiscal() {
		
	}
	
	public NotaFiscal(int cdNotaFiscal,
			int cdEmpresa,
			int cdEnderecoRetirada,
			int cdNaturezaOperacao,
			int cdCidade,
			int cdDestinatario,
			int cdEnderecoDestinatario,
			int cdEnderecoEntrega,
			int cdLote,
			int tpModelo,
			String nrSerie,
			String nrNotaFiscal,
			int stNotaFiscal,
			GregorianCalendar dtEmissao,
			int tpMovimento,
			GregorianCalendar dtMovimentacao,
			int tpPagamento,
			int tpEmissao,
			int tpFinalidade,
			int tpDanfe,
			float vlTotalProduto,
			float vlSeguro,
			float vlOutrasDespesas,
			float vlTotalNota,
			int tpModalidadeFrete,
			String txtObservacao,
			String txtInformacaoFisco,
			int lgDanfeImpresso,
			String nrChaveAcesso,
			int nrDv,
			String nrProtocoloAutorizacao,
			GregorianCalendar dtAutorizacao,
			int cdTransportador,
			int cdNaturezaOperacaoFrete,
			float vlFrete,
			float vlFreteBaseIcms,
			float vlFreteIcmsRetido,
			String nrRecebimento,
			String nrPlaca,
			String qtVolume,
			String dsEspecie,
			String dsMarca,
			String dsNumeracao,
			float vlPesoBruto,
			float vlPesoLiquido,
			int cdVeiculo,
			String sgUfVeiculo,
			String nrRntc,
			int cdMotivoCancelamento,
			String txtXml){
		setCdNotaFiscal(cdNotaFiscal);
		setCdEmpresa(cdEmpresa);
		setCdEnderecoRetirada(cdEnderecoRetirada);
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setCdCidade(cdCidade);
		setCdDestinatario(cdDestinatario);
		setCdEnderecoDestinatario(cdEnderecoDestinatario);
		setCdEnderecoEntrega(cdEnderecoEntrega);
		setCdLote(cdLote);
		setTpModelo(tpModelo);
		setNrSerie(nrSerie);
		setNrNotaFiscal(nrNotaFiscal);
		setStNotaFiscal(stNotaFiscal);
		setDtEmissao(dtEmissao);
		setTpMovimento(tpMovimento);
		setDtMovimentacao(dtMovimentacao);
		setTpPagamento(tpPagamento);
		setTpEmissao(tpEmissao);
		setTpFinalidade(tpFinalidade);
		setTpDanfe(tpDanfe);
		setVlTotalProduto(vlTotalProduto);
		setVlSeguro(vlSeguro);
		setVlOutrasDespesas(vlOutrasDespesas);
		setVlTotalNota(vlTotalNota);
		setTpModalidadeFrete(tpModalidadeFrete);
		setTxtObservacao(txtObservacao);
		setTxtInformacaoFisco(txtInformacaoFisco);
		setLgDanfeImpresso(lgDanfeImpresso);
		setNrChaveAcesso(nrChaveAcesso);
		setNrDv(nrDv);
		setNrProtocoloAutorizacao(nrProtocoloAutorizacao);
		setDtAutorizacao(dtAutorizacao);
		setCdTransportador(cdTransportador);
		setCdNaturezaOperacaoFrete(cdNaturezaOperacaoFrete);
		setVlFrete(vlFrete);
		setVlFreteBaseIcms(vlFreteBaseIcms);
		setVlFreteIcmsRetido(vlFreteIcmsRetido);
		setNrRecebimento(nrRecebimento);
		setNrPlaca(nrPlaca);
		setQtVolume(qtVolume);
		setDsEspecie(dsEspecie);
		setDsMarca(dsMarca);
		setDsNumeracao(dsNumeracao);
		setVlPesoBruto(vlPesoBruto);
		setVlPesoLiquido(vlPesoLiquido);
		setCdVeiculo(cdVeiculo);
		setSgUfVeiculo(sgUfVeiculo);
		setNrRntc(nrRntc);
		setCdMotivoCancelamento(cdMotivoCancelamento);
		setTxtXml(txtXml);
	}
	
	public NotaFiscal(int cdNotaFiscal,
			int cdEmpresa,
			int cdEnderecoRetirada,
			int cdNaturezaOperacao,
			int cdCidade,
			int cdDestinatario,
			int cdEnderecoDestinatario,
			int cdEnderecoEntrega,
			int cdLote,
			int tpModelo,
			String nrSerie,
			String nrNotaFiscal,
			int stNotaFiscal,
			GregorianCalendar dtEmissao,
			int tpMovimento,
			GregorianCalendar dtMovimentacao,
			int tpPagamento,
			int tpEmissao,
			int tpFinalidade,
			int tpDanfe,
			float vlTotalProduto,
			float vlSeguro,
			float vlOutrasDespesas,
			float vlTotalNota,
			int tpModalidadeFrete,
			String txtObservacao,
			String txtInformacaoFisco,
			int lgDanfeImpresso,
			String nrChaveAcesso,
			int nrDv,
			String nrProtocoloAutorizacao,
			GregorianCalendar dtAutorizacao,
			int cdTransportador,
			int cdNaturezaOperacaoFrete,
			float vlFrete,
			float vlFreteBaseIcms,
			float vlFreteIcmsRetido,
			String nrRecebimento,
			String nrPlaca,
			String qtVolume,
			String dsEspecie,
			String dsMarca,
			String dsNumeracao,
			float vlPesoBruto,
			float vlPesoLiquido,
			int cdVeiculo,
			String sgUfVeiculo,
			String nrRntc,
			int cdMotivoCancelamento,
			String txtXml,
			float prDesconto,
			int lgConsumidorFinal,
			int tpVendaPresenca,
			String nrChaveAcessoReferencia){
		setCdNotaFiscal(cdNotaFiscal);
		setCdEmpresa(cdEmpresa);
		setCdEnderecoRetirada(cdEnderecoRetirada);
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setCdCidade(cdCidade);
		setCdDestinatario(cdDestinatario);
		setCdEnderecoDestinatario(cdEnderecoDestinatario);
		setCdEnderecoEntrega(cdEnderecoEntrega);
		setCdLote(cdLote);
		setTpModelo(tpModelo);
		setNrSerie(nrSerie);
		setNrNotaFiscal(nrNotaFiscal);
		setStNotaFiscal(stNotaFiscal);
		setDtEmissao(dtEmissao);
		setTpMovimento(tpMovimento);
		setDtMovimentacao(dtMovimentacao);
		setTpPagamento(tpPagamento);
		setTpEmissao(tpEmissao);
		setTpFinalidade(tpFinalidade);
		setTpDanfe(tpDanfe);
		setVlTotalProduto(vlTotalProduto);
		setVlSeguro(vlSeguro);
		setVlOutrasDespesas(vlOutrasDespesas);
		setVlTotalNota(vlTotalNota);
		setTpModalidadeFrete(tpModalidadeFrete);
		setTxtObservacao(txtObservacao);
		setTxtInformacaoFisco(txtInformacaoFisco);
		setLgDanfeImpresso(lgDanfeImpresso);
		setNrChaveAcesso(nrChaveAcesso);
		setNrDv(nrDv);
		setNrProtocoloAutorizacao(nrProtocoloAutorizacao);
		setDtAutorizacao(dtAutorizacao);
		setCdTransportador(cdTransportador);
		setCdNaturezaOperacaoFrete(cdNaturezaOperacaoFrete);
		setVlFrete(vlFrete);
		setVlFreteBaseIcms(vlFreteBaseIcms);
		setVlFreteIcmsRetido(vlFreteIcmsRetido);
		setNrRecebimento(nrRecebimento);
		setNrPlaca(nrPlaca);
		setQtVolume(qtVolume);
		setDsEspecie(dsEspecie);
		setDsMarca(dsMarca);
		setDsNumeracao(dsNumeracao);
		setVlPesoBruto(vlPesoBruto);
		setVlPesoLiquido(vlPesoLiquido);
		setCdVeiculo(cdVeiculo);
		setSgUfVeiculo(sgUfVeiculo);
		setNrRntc(nrRntc);
		setCdMotivoCancelamento(cdMotivoCancelamento);
		setTxtXml(txtXml);
		setPrDesconto(prDesconto);
		setLgConsumidorFinal(lgConsumidorFinal);
		setTpVendaPresenca(tpVendaPresenca);
		setNrChaveAcessoReferencia(nrChaveAcessoReferencia);
	}
	
	public void setCdNotaFiscal(int cdNotaFiscal){
		this.cdNotaFiscal=cdNotaFiscal;
	}
	public int getCdNotaFiscal(){
		return this.cdNotaFiscal;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdEnderecoRetirada(int cdEnderecoRetirada){
		this.cdEnderecoRetirada=cdEnderecoRetirada;
	}
	public int getCdEnderecoRetirada(){
		return this.cdEnderecoRetirada;
	}
	public void setCdNaturezaOperacao(int cdNaturezaOperacao){
		this.cdNaturezaOperacao=cdNaturezaOperacao;
	}
	public int getCdNaturezaOperacao(){
		return this.cdNaturezaOperacao;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setCdDestinatario(int cdDestinatario){
		this.cdDestinatario=cdDestinatario;
	}
	public int getCdDestinatario(){
		return this.cdDestinatario;
	}
	public void setCdEnderecoDestinatario(int cdEnderecoDestinatario){
		this.cdEnderecoDestinatario=cdEnderecoDestinatario;
	}
	public int getCdEnderecoDestinatario(){
		return this.cdEnderecoDestinatario;
	}
	public void setCdEnderecoEntrega(int cdEnderecoEntrega){
		this.cdEnderecoEntrega=cdEnderecoEntrega;
	}
	public int getCdEnderecoEntrega(){
		return this.cdEnderecoEntrega;
	}
	public void setCdLote(int cdLote){
		this.cdLote=cdLote;
	}
	public int getCdLote(){
		return this.cdLote;
	}
	public void setTpModelo(int tpModelo){
		this.tpModelo=tpModelo;
	}
	public int getTpModelo(){
		return this.tpModelo;
	}
	public void setNrSerie(String nrSerie){
		this.nrSerie=nrSerie;
	}
	public String getNrSerie(){
		return this.nrSerie;
	}
	public void setNrNotaFiscal(String nrNotaFiscal){
		this.nrNotaFiscal=nrNotaFiscal;
	}
	public String getNrNotaFiscal(){
		return this.nrNotaFiscal;
	}
	public void setStNotaFiscal(int stNotaFiscal){
		this.stNotaFiscal=stNotaFiscal;
	}
	public int getStNotaFiscal(){
		return this.stNotaFiscal;
	}
	public void setDtEmissao(GregorianCalendar dtEmissao){
		this.dtEmissao=dtEmissao;
	}
	public GregorianCalendar getDtEmissao(){
		return this.dtEmissao;
	}
	public void setTpMovimento(int tpMovimento){
		this.tpMovimento=tpMovimento;
	}
	public int getTpMovimento(){
		return this.tpMovimento;
	}
	public void setDtMovimentacao(GregorianCalendar dtMovimentacao){
		this.dtMovimentacao=dtMovimentacao;
	}
	public GregorianCalendar getDtMovimentacao(){
		return this.dtMovimentacao;
	}
	public void setTpPagamento(int tpPagamento){
		this.tpPagamento=tpPagamento;
	}
	public int getTpPagamento(){
		return this.tpPagamento;
	}
	public void setTpEmissao(int tpEmissao){
		this.tpEmissao=tpEmissao;
	}
	public int getTpEmissao(){
		return this.tpEmissao;
	}
	public void setTpFinalidade(int tpFinalidade){
		this.tpFinalidade=tpFinalidade;
	}
	public int getTpFinalidade(){
		return this.tpFinalidade;
	}
	public void setTpDanfe(int tpDanfe){
		this.tpDanfe=tpDanfe;
	}
	public int getTpDanfe(){
		return this.tpDanfe;
	}
	public void setVlTotalProduto(float vlTotalProduto){
		this.vlTotalProduto=vlTotalProduto;
	}
	public float getVlTotalProduto(){
		return this.vlTotalProduto;
	}
	public void setVlSeguro(float vlSeguro){
		this.vlSeguro=vlSeguro;
	}
	public float getVlSeguro(){
		return this.vlSeguro;
	}
	public void setVlOutrasDespesas(float vlOutrasDespesas){
		this.vlOutrasDespesas=vlOutrasDespesas;
	}
	public float getVlOutrasDespesas(){
		return this.vlOutrasDespesas;
	}
	public void setVlTotalNota(float vlTotalNota){
		this.vlTotalNota=vlTotalNota;
	}
	public float getVlTotalNota(){
		return this.vlTotalNota;
	}
	public void setTpModalidadeFrete(int tpModalidadeFrete){
		this.tpModalidadeFrete=tpModalidadeFrete;
	}
	public int getTpModalidadeFrete(){
		return this.tpModalidadeFrete;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setTxtInformacaoFisco(String txtInformacaoFisco){
		this.txtInformacaoFisco=txtInformacaoFisco;
	}
	public String getTxtInformacaoFisco(){
		return this.txtInformacaoFisco;
	}
	public void setLgDanfeImpresso(int lgDanfeImpresso){
		this.lgDanfeImpresso=lgDanfeImpresso;
	}
	public int getLgDanfeImpresso(){
		return this.lgDanfeImpresso;
	}
	public void setNrChaveAcesso(String nrChaveAcesso){
		this.nrChaveAcesso=nrChaveAcesso;
	}
	public String getNrChaveAcesso(){
		return this.nrChaveAcesso;
	}
	public void setNrProtocoloAutorizacao(String nrProtocoloAutorizacao){
		this.nrProtocoloAutorizacao=nrProtocoloAutorizacao;
	}
	public String getNrProtocoloAutorizacao(){
		return this.nrProtocoloAutorizacao;
	}
	public void setDtAutorizacao(GregorianCalendar dtAutorizacao){
		this.dtAutorizacao=dtAutorizacao;
	}
	public GregorianCalendar getDtAutorizacao(){
		return this.dtAutorizacao;
	}
	public void setCdTransportador(int cdTransportador){
		this.cdTransportador=cdTransportador;
	}
	public int getCdTransportador(){
		return this.cdTransportador;
	}
	public void setCdNaturezaOperacaoFrete(int cdNaturezaOperacaoFrete){
		this.cdNaturezaOperacaoFrete=cdNaturezaOperacaoFrete;
	}
	public int getCdNaturezaOperacaoFrete(){
		return this.cdNaturezaOperacaoFrete;
	}
	public void setVlFrete(float vlFrete){
		this.vlFrete=vlFrete;
	}
	public float getVlFrete(){
		return this.vlFrete;
	}
	public void setVlFreteBaseIcms(float vlFreteBaseIcms){
		this.vlFreteBaseIcms=vlFreteBaseIcms;
	}
	public float getVlFreteBaseIcms(){
		return this.vlFreteBaseIcms;
	}
	public void setVlFreteIcmsRetido(float vlFreteIcmsRetido){
		this.vlFreteIcmsRetido=vlFreteIcmsRetido;
	}
	public float getVlFreteIcmsRetido(){
		return this.vlFreteIcmsRetido;
	}
	public void setNrRecebimento(String nrRecebimento){
		this.nrRecebimento=nrRecebimento;
	}
	public String getNrRecebimento(){
		return this.nrRecebimento;
	}
	public void setNrDv(int nrDv){
		this.nrDv=nrDv;
	}
	public int getNrDv(){
		return this.nrDv;
	}
	public void setNrPlaca(String nrPlaca){
		this.nrPlaca=nrPlaca;
	}
	public String getNrPlaca(){
		return this.nrPlaca;
	}
	public void setQtVolume(String qtVolume){
		this.qtVolume=qtVolume;
	}
	public String getQtVolume(){
		return this.qtVolume;
	}
	public void setDsEspecie(String dsEspecie){
		this.dsEspecie=dsEspecie;
	}
	public String getDsEspecie(){
		return this.dsEspecie;
	}
	public void setDsMarca(String dsMarca){
		this.dsMarca=dsMarca;
	}
	public String getDsMarca(){
		return this.dsMarca;
	}
	public void setDsNumeracao(String dsNumeracao){
		this.dsNumeracao=dsNumeracao;
	}
	public String getDsNumeracao(){
		return this.dsNumeracao;
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
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setSgUfVeiculo(String sgUfVeiculo){
		this.sgUfVeiculo=sgUfVeiculo;
	}
	public String getSgUfVeiculo(){
		return this.sgUfVeiculo;
	}
	public void setNrRntc(String nrRntc){
		this.nrRntc=nrRntc;
	}
	public String getNrRntc(){
		return this.nrRntc;
	}
	public void setCdMotivoCancelamento(int cdMotivoCancelamento) {
		this.cdMotivoCancelamento = cdMotivoCancelamento;
	}
	public int getCdMotivoCancelamento() {
		return cdMotivoCancelamento;
	}
	public void setTxtXml(String txtXml) {
		this.txtXml = txtXml;
	}
	public String getTxtXml() {
		return txtXml;
	}
	public void setPrDesconto(float prDesconto) {
		this.prDesconto = prDesconto;
	}
	public float getPrDesconto() {
		return prDesconto;
	}
	public void setLgConsumidorFinal(int lgConsumidorFinal) {
		this.lgConsumidorFinal = lgConsumidorFinal;
	}
	public int getLgConsumidorFinal() {
		return lgConsumidorFinal;
	}
	public void setTpVendaPresenca(int tpVendaPresenca) {
		this.tpVendaPresenca = tpVendaPresenca;
	}
	public int getTpVendaPresenca() {
		return tpVendaPresenca;
	}
	public void setNrChaveAcessoReferencia(String nrChaveAcessoReferencia) {
		this.nrChaveAcessoReferencia = nrChaveAcessoReferencia;
	}
	public String getNrChaveAcessoReferencia() {
		return nrChaveAcessoReferencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNotaFiscal: " +  getCdNotaFiscal();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdEnderecoRetirada: " +  getCdEnderecoRetirada();
		valueToString += ", cdNaturezaOperacao: " +  getCdNaturezaOperacao();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", cdDestinatario: " +  getCdDestinatario();
		valueToString += ", cdEnderecoDestinatario: " +  getCdEnderecoDestinatario();
		valueToString += ", cdEnderecoEntrega: " +  getCdEnderecoEntrega();
		valueToString += ", cdLote: " +  getCdLote();
		valueToString += ", tpModelo: " +  getTpModelo();
		valueToString += ", nrSerie: " +  getNrSerie();
		valueToString += ", nrNotaFiscal: " +  getNrNotaFiscal();
		valueToString += ", stNotaFiscal: " +  getStNotaFiscal();
		valueToString += ", dtEmissao: " +  sol.util.Util.formatDateTime(getDtEmissao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpMovimento: " +  getTpMovimento();
		valueToString += ", dtMovimentacao: " +  sol.util.Util.formatDateTime(getDtMovimentacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpPagamento: " +  getTpPagamento();
		valueToString += ", tpEmissao: " +  getTpEmissao();
		valueToString += ", tpFinalidade: " +  getTpFinalidade();
		valueToString += ", tpDanfe: " +  getTpDanfe();
		valueToString += ", vlTotalProduto: " +  getVlTotalProduto();
		valueToString += ", vlSeguro: " +  getVlSeguro();
		valueToString += ", vlOutrasDespesas: " +  getVlOutrasDespesas();
		valueToString += ", vlTotalNota: " +  getVlTotalNota();
		valueToString += ", tpModalidadeFrete: " +  getTpModalidadeFrete();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", txtInformacaoFisco: " +  getTxtInformacaoFisco();
		valueToString += ", lgDanfeImpresso: " +  getLgDanfeImpresso();
		valueToString += ", nrChaveAcesso: " +  getNrChaveAcesso();
		valueToString += ", nrProtocoloAutorizacao: " +  getNrProtocoloAutorizacao();
		valueToString += ", dtAutorizacao: " +  sol.util.Util.formatDateTime(getDtAutorizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdTransportador: " +  getCdTransportador();
		valueToString += ", cdNaturezaOperacaoFrete: " +  getCdNaturezaOperacaoFrete();
		valueToString += ", vlFrete: " +  getVlFrete();
		valueToString += ", vlFreteBaseIcms: " +  getVlFreteBaseIcms();
		valueToString += ", vlFreteIcmsRetido: " +  getVlFreteIcmsRetido();
		valueToString += ", nrRecebimento: " +  getNrRecebimento();
		valueToString += ", nrDv: " +  getNrDv();
		valueToString += ", nrPlaca: " +  getNrPlaca();
		valueToString += ", qtVolume: " +  getQtVolume();
		valueToString += ", dsEspecie: " +  getDsEspecie();
		valueToString += ", dsMarca: " +  getDsMarca();
		valueToString += ", dsNumeracao: " +  getDsNumeracao();
		valueToString += ", vlPesoBruto: " +  getVlPesoBruto();
		valueToString += ", vlPesoLiquido: " +  getVlPesoLiquido();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", sgUfVeiculo: " +  getSgUfVeiculo();
		valueToString += ", nrRntc: " +  getNrRntc();
		valueToString += ", cdMotivoCancelamento: " +  getCdMotivoCancelamento();
		valueToString += ", txtXml: " +  getTxtXml();
		valueToString += ", prDesconto: " +  getPrDesconto();
		valueToString += ", lgConsumidorFinal: " +  getLgConsumidorFinal();
		valueToString += ", tpVendaPresenca: " +  getTpVendaPresenca();
		valueToString += ", nrChaveAcessoReferencia: " +  getNrChaveAcessoReferencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new NotaFiscal(getCdNotaFiscal(),
			getCdEmpresa(),
			getCdEnderecoRetirada(),
			getCdNaturezaOperacao(),
			getCdCidade(),
			getCdDestinatario(),
			getCdEnderecoDestinatario(),
			getCdEnderecoEntrega(),
			getCdLote(),
			getTpModelo(),
			getNrSerie(),
			getNrNotaFiscal(),
			getStNotaFiscal(),
			getDtEmissao()==null ? null : (GregorianCalendar)getDtEmissao().clone(),
			getTpMovimento(),
			getDtMovimentacao()==null ? null : (GregorianCalendar)getDtMovimentacao().clone(),
			getTpPagamento(),
			getTpEmissao(),
			getTpFinalidade(),
			getTpDanfe(),
			getVlTotalProduto(),
			getVlSeguro(),
			getVlOutrasDespesas(),
			getVlTotalNota(),
			getTpModalidadeFrete(),
			getTxtObservacao(),
			getTxtInformacaoFisco(),
			getLgDanfeImpresso(),
			getNrChaveAcesso(),
			getNrDv(),
			getNrProtocoloAutorizacao(),
			getDtAutorizacao()==null ? null : (GregorianCalendar)getDtAutorizacao().clone(),
			getCdTransportador(),
			getCdNaturezaOperacaoFrete(),
			getVlFrete(),
			getVlFreteBaseIcms(),
			getVlFreteIcmsRetido(),
			getNrRecebimento(),
			getNrPlaca(),
			getQtVolume(),
			getDsEspecie(),
			getDsMarca(),
			getDsNumeracao(),
			getVlPesoBruto(),
			getVlPesoLiquido(),
			getCdVeiculo(),
			getSgUfVeiculo(),
			getNrRntc(),
			getCdMotivoCancelamento(),
			getTxtXml(),
			getPrDesconto(),
			getLgConsumidorFinal(),
			getTpVendaPresenca(),
			getNrChaveAcessoReferencia());
	}

}