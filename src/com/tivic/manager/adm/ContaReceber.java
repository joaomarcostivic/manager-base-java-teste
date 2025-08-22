package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class ContaReceber {

	private int cdContaReceber;
	private int cdPessoa;
	private int cdEmpresa;
	private int cdContrato;
	private int cdContaOrigem;
	private int cdDocumentoSaida;
	private int cdContaCarteira;
	private int cdConta;
	private int cdFrete;
	private String nrDocumento;
	private String idContaReceber;
	private int nrParcela;
	private String nrReferencia;
	private int cdTipoDocumento;
	private String dsHistorico;
	private GregorianCalendar dtVencimento;
	private GregorianCalendar dtEmissao;
	private GregorianCalendar dtRecebimento;
	private GregorianCalendar dtProrrogacao;
	private Double vlConta;
	private Double vlAbatimento;
	private Double vlAcrescimo;
	private Double vlRecebido;
	private int stConta;
	private int tpFrequencia;
	private int qtParcelas;
	private int tpContaReceber;
	private int cdNegociacao;
	private String txtObservacao;
	private int cdPlanoPagamento;
	private int cdFormaPagamento;
	private GregorianCalendar dtDigitacao;
	private GregorianCalendar dtVencimentoOriginal;
	private int cdTurno;
	private Double prJuros;
	private Double prMulta;
	private int lgProtesto;
	private int lgPrioritaria;
	private int cdFormaPagamentoPreferencial;
	private int cdContaSacado;
	private int cdUsuario;
	
	public ContaReceber(){}
	
	/**
	 *@deprecated
	 */
	public ContaReceber(int cdContaReceber,
			int cdPessoa,
			int cdEmpresa,
			int cdContrato,
			int cdContaOrigem,
			int cdDocumentoSaida,
			int cdContaCarteira,
			int cdConta,
			int cdFrete,
			String nrDocumento,
			String idContaReceber,
			int nrParcela,
			String nrReferencia,
			int cdTipoDocumento,
			String dsHistorico,
			GregorianCalendar dtVencimento,
			GregorianCalendar dtEmissao,
			GregorianCalendar dtRecebimento,
			GregorianCalendar dtProrrogacao,
			double vlConta,
			double vlAbatimento,
			double vlAcrescimo,
			double vlRecebido,
			int stConta,
			int tpFrequencia,
			int qtParcelas,
			int tpContaReceber,
			int cdNegociacao,
			String txtObservacao,
			int cdPlanoPagamento,
			int cdFormaPagamento,
			GregorianCalendar dtDigitacao,
			GregorianCalendar dtVencimentoOriginal,
			int cdTurno,
			int cdContaSacado){
		setCdContaReceber(cdContaReceber);
		setCdPessoa(cdPessoa);
		setCdEmpresa(cdEmpresa);
		setCdContrato(cdContrato);
		setCdContaOrigem(cdContaOrigem);
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdContaCarteira(cdContaCarteira);
		setCdConta(cdConta);
		setCdFrete(cdFrete);
		setNrDocumento(nrDocumento);
		setIdContaReceber(idContaReceber);
		setNrParcela(nrParcela);
		setNrReferencia(nrReferencia);
		setCdTipoDocumento(cdTipoDocumento);
		setDsHistorico(dsHistorico);
		setDtVencimento(dtVencimento);
		setDtEmissao(dtEmissao);
		setDtRecebimento(dtRecebimento);
		setDtProrrogacao(dtProrrogacao);
		setVlConta(vlConta);
		setVlAbatimento(vlAbatimento);
		setVlAcrescimo(vlAcrescimo);
		setVlRecebido(vlRecebido);
		setStConta(stConta);
		setTpFrequencia(tpFrequencia);
		setQtParcelas(qtParcelas);
		setTpContaReceber(tpContaReceber);
		setCdNegociacao(cdNegociacao);
		setTxtObservacao(txtObservacao);
		setCdPlanoPagamento(cdPlanoPagamento);
		setCdFormaPagamento(cdFormaPagamento);
		setDtDigitacao(dtDigitacao);
		setDtVencimentoOriginal(dtVencimentoOriginal);
		setCdTurno(cdTurno);
	}
	/**
	 *@deprecated
	 */
	public ContaReceber(int cdContaReceber,
			int cdPessoa,
			int cdEmpresa,
			int cdContrato,
			int cdContaOrigem,
			int cdDocumentoSaida,
			int cdContaCarteira,
			int cdConta,
			int cdFrete,
			String nrDocumento,
			String idContaReceber,
			int nrParcela,
			String nrReferencia,
			int cdTipoDocumento,
			String dsHistorico,
			GregorianCalendar dtVencimento,
			GregorianCalendar dtEmissao,
			GregorianCalendar dtRecebimento,
			GregorianCalendar dtProrrogacao,
			double vlConta,
			double vlAbatimento,
			double vlAcrescimo,
			double vlRecebido,
			int stConta,
			int tpFrequencia,
			int qtParcelas,
			int tpContaReceber,
			int cdNegociacao,
			String txtObservacao,
			int cdPlanoPagamento,
			int cdFormaPagamento,
			GregorianCalendar dtDigitacao,
			GregorianCalendar dtVencimentoOriginal,
			int cdTurno,
			double prJuros,
			double prMulta,
			int lgProtesto){
		setCdContaReceber(cdContaReceber);
		setCdPessoa(cdPessoa);
		setCdEmpresa(cdEmpresa);
		setCdContrato(cdContrato);
		setCdContaOrigem(cdContaOrigem);
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdContaCarteira(cdContaCarteira);
		setCdConta(cdConta);
		setCdFrete(cdFrete);
		setNrDocumento(nrDocumento);
		setIdContaReceber(idContaReceber);
		setNrParcela(nrParcela);
		setNrReferencia(nrReferencia);
		setCdTipoDocumento(cdTipoDocumento);
		setDsHistorico(dsHistorico);
		setDtVencimento(dtVencimento);
		setDtEmissao(dtEmissao);
		setDtRecebimento(dtRecebimento);
		setDtProrrogacao(dtProrrogacao);
		setVlConta(vlConta);
		setVlAbatimento(vlAbatimento);
		setVlAcrescimo(vlAcrescimo);
		setVlRecebido(vlRecebido);
		setStConta(stConta);
		setTpFrequencia(tpFrequencia);
		setQtParcelas(qtParcelas);
		setTpContaReceber(tpContaReceber);
		setCdNegociacao(cdNegociacao);
		setTxtObservacao(txtObservacao);
		setCdPlanoPagamento(cdPlanoPagamento);
		setCdFormaPagamento(cdFormaPagamento);
		setDtDigitacao(dtDigitacao);
		setDtVencimentoOriginal(dtVencimentoOriginal);
		setCdTurno(cdTurno);
		setPrJuros(prJuros);
		setPrMulta(prMulta);
		setLgProtesto(lgProtesto);
	}
	
	public ContaReceber(int cdContaReceber,
			int cdPessoa,
			int cdEmpresa,
			int cdContrato,
			int cdContaOrigem,
			int cdDocumentoSaida,
			int cdContaCarteira,
			int cdConta,
			int cdFrete,
			String nrDocumento,
			String idContaReceber,
			int nrParcela,
			String nrReferencia,
			int cdTipoDocumento,
			String dsHistorico,
			GregorianCalendar dtVencimento,
			GregorianCalendar dtEmissao,
			GregorianCalendar dtRecebimento,
			GregorianCalendar dtProrrogacao,
			double vlConta,
			double vlAbatimento,
			double vlAcrescimo,
			double vlRecebido,
			int stConta,
			int tpFrequencia,
			int qtParcelas,
			int tpContaReceber,
			int cdNegociacao,
			String txtObservacao,
			int cdPlanoPagamento,
			int cdFormaPagamento,
			GregorianCalendar dtDigitacao,
			GregorianCalendar dtVencimentoOriginal,
			int cdTurno,
			double prJuros,
			double prMulta,
			int lgProtesto,
			int lgPrioritaria,
			int cdFormaPagamentoPreferencial,
			int cdContaSacado,
			int cdUsuario){
		setCdContaReceber(cdContaReceber);
		setCdPessoa(cdPessoa);
		setCdEmpresa(cdEmpresa);
		setCdContrato(cdContrato);
		setCdContaOrigem(cdContaOrigem);
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdContaCarteira(cdContaCarteira);
		setCdConta(cdConta);
		setCdFrete(cdFrete);
		setNrDocumento(nrDocumento);
		setIdContaReceber(idContaReceber);
		setNrParcela(nrParcela);
		setNrReferencia(nrReferencia);
		setCdTipoDocumento(cdTipoDocumento);
		setDsHistorico(dsHistorico);
		setDtVencimento(dtVencimento);
		setDtEmissao(dtEmissao);
		setDtRecebimento(dtRecebimento);
		setDtProrrogacao(dtProrrogacao);
		setVlConta(vlConta);
		setVlAbatimento(vlAbatimento);
		setVlAcrescimo(vlAcrescimo);
		setVlRecebido(vlRecebido);
		setStConta(stConta);
		setTpFrequencia(tpFrequencia);
		setQtParcelas(qtParcelas);
		setTpContaReceber(tpContaReceber);
		setCdNegociacao(cdNegociacao);
		setTxtObservacao(txtObservacao);
		setCdPlanoPagamento(cdPlanoPagamento);
		setCdFormaPagamento(cdFormaPagamento);
		setDtDigitacao(dtDigitacao);
		setDtVencimentoOriginal(dtVencimentoOriginal);
		setCdTurno(cdTurno);
		setPrJuros(prJuros);
		setPrMulta(prMulta);
		setLgProtesto(lgProtesto);
		setLgPrioritaria(lgPrioritaria);
		setCdFormaPagamentoPreferencial(cdFormaPagamentoPreferencial);
		setCdContaSacado(cdContaSacado);
		setCdUsuario(cdUsuario);
	}

	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdContaOrigem(int cdContaOrigem){
		this.cdContaOrigem=cdContaOrigem;
	}
	public int getCdContaOrigem(){
		return this.cdContaOrigem;
	}
	public void setCdDocumentoSaida(int cdDocumentoSaida){
		this.cdDocumentoSaida=cdDocumentoSaida;
	}
	public int getCdDocumentoSaida(){
		return this.cdDocumentoSaida;
	}
	public void setCdContaCarteira(int cdContaCarteira){
		this.cdContaCarteira=cdContaCarteira;
	}
	public int getCdContaCarteira(){
		return this.cdContaCarteira;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdFrete(int cdFrete){
		this.cdFrete=cdFrete;
	}
	public int getCdFrete(){
		return this.cdFrete;
	}
	public void setNrDocumento(String nrDocumento){
		this.nrDocumento=nrDocumento;
	}
	public String getNrDocumento(){
		return this.nrDocumento;
	}
	public void setIdContaReceber(String idContaReceber){
		this.idContaReceber=idContaReceber;
	}
	public String getIdContaReceber(){
		return this.idContaReceber;
	}
	public void setNrParcela(int nrParcela){
		this.nrParcela=nrParcela;
	}
	public int getNrParcela(){
		return this.nrParcela;
	}
	public void setNrReferencia(String nrReferencia){
		this.nrReferencia=nrReferencia;
	}
	public String getNrReferencia(){
		return this.nrReferencia;
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setDsHistorico(String dsHistorico){
		this.dsHistorico=dsHistorico;
	}
	public String getDsHistorico(){
		return this.dsHistorico;
	}
	public void setDtVencimento(GregorianCalendar dtVencimento){
		this.dtVencimento=dtVencimento;
	}
	public GregorianCalendar getDtVencimento(){
		return this.dtVencimento;
	}
	public void setDtEmissao(GregorianCalendar dtEmissao){
		this.dtEmissao=dtEmissao;
	}
	public GregorianCalendar getDtEmissao(){
		return this.dtEmissao;
	}
	public void setDtRecebimento(GregorianCalendar dtRecebimento){
		this.dtRecebimento=dtRecebimento;
	}
	public GregorianCalendar getDtRecebimento(){
		return this.dtRecebimento;
	}
	public void setDtProrrogacao(GregorianCalendar dtProrrogacao){
		this.dtProrrogacao=dtProrrogacao;
	}
	public GregorianCalendar getDtProrrogacao(){
		return this.dtProrrogacao;
	}
	// Chamada do PDV, ainda nao encontrei uma forma de chamar a classe Double direto por ele.
	public void setVlConta(double vlConta){
		this.vlConta=vlConta;
	}
	public void setVlConta(Double vlConta){
		this.vlConta=vlConta;
	}
	public Double getVlConta(){
		return this.vlConta;
	}
	public void setVlAbatimento(Double vlAbatimento){
		this.vlAbatimento=vlAbatimento;
	}
	public Double getVlAbatimento(){
		return this.vlAbatimento;
	}
	public void setVlAcrescimo(Double vlAcrescimo){
		this.vlAcrescimo=vlAcrescimo;
	}
	public Double getVlAcrescimo(){
		return this.vlAcrescimo;
	}
	public void setVlRecebido(Double vlRecebido){
		this.vlRecebido=vlRecebido;
	}
	public Double getVlRecebido(){
		return this.vlRecebido;
	}
	public void setStConta(int stConta){
		this.stConta=stConta;
	}
	public int getStConta(){
		return this.stConta;
	}
	public void setTpFrequencia(int tpFrequencia){
		this.tpFrequencia=tpFrequencia;
	}
	public int getTpFrequencia(){
		return this.tpFrequencia;
	}
	public void setQtParcelas(int qtParcelas){
		this.qtParcelas=qtParcelas;
	}
	public int getQtParcelas(){
		return this.qtParcelas;
	}
	public void setTpContaReceber(int tpContaReceber){
		this.tpContaReceber=tpContaReceber;
	}
	public int getTpContaReceber(){
		return this.tpContaReceber;
	}
	public void setCdNegociacao(int cdNegociacao){
		this.cdNegociacao=cdNegociacao;
	}
	public int getCdNegociacao(){
		return this.cdNegociacao;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdPlanoPagamento(int cdPlanoPagamento){
		this.cdPlanoPagamento=cdPlanoPagamento;
	}
	public int getCdPlanoPagamento(){
		return this.cdPlanoPagamento;
	}
	public void setCdFormaPagamento(int cdFormaPagamento){
		this.cdFormaPagamento=cdFormaPagamento;
	}
	public int getCdFormaPagamento(){
		return this.cdFormaPagamento;
	}
	public void setDtDigitacao(GregorianCalendar dtDigitacao){
		this.dtDigitacao=dtDigitacao;
	}
	public GregorianCalendar getDtDigitacao(){
		return this.dtDigitacao;
	}
	public void setDtVencimentoOriginal(GregorianCalendar dtVencimentoOriginal){
		this.dtVencimentoOriginal=dtVencimentoOriginal;
	}
	public GregorianCalendar getDtVencimentoOriginal(){
		return this.dtVencimentoOriginal;
	}
	public void setCdTurno(int cdTurno) {
		this.cdTurno = cdTurno;
	}
	public int getCdTurno() {
		return cdTurno;
	}
	public void setPrJuros(Double prJuros) {
		this.prJuros = prJuros;
	}
	public Double getPrJuros() {
		return prJuros;
	}
	public void setPrMulta(Double prMulta) {
		this.prMulta = prMulta;
	}
	public Double getPrMulta() {
		return prMulta;
	}
	public void setLgProtesto(int lgProtesto) {
		this.lgProtesto = lgProtesto;
	}
	public int getLgProtesto() {
		return lgProtesto;
	}
	public void setLgPrioritaria(int lgPrioritaria) {
		this.lgPrioritaria = lgPrioritaria;
	}
	public int getLgPrioritaria() {
		return lgPrioritaria;
	}
	
	public void setCdFormaPagamentoPreferencial(int cdFormaPagamentoPreferencial){
		this.cdFormaPagamentoPreferencial=cdFormaPagamentoPreferencial;
	}
	public int getCdFormaPagamentoPreferencial(){
		return this.cdFormaPagamentoPreferencial;
	}
	
	public int getCdContaSacado() {
		return cdContaSacado;
	}

	public void setCdContaSacado(int cdContaSacado) {
		this.cdContaSacado = cdContaSacado;
	}
	
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContaReceber: " +  getCdContaReceber();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdContrato: " +  getCdContrato();
		valueToString += ", cdContaOrigem: " +  getCdContaOrigem();
		valueToString += ", cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", cdContaCarteira: " +  getCdContaCarteira();
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", cdFrete: " +  getCdFrete();
		valueToString += ", nrDocumento: " +  getNrDocumento();
		valueToString += ", idContaReceber: " +  getIdContaReceber();
		valueToString += ", nrParcela: " +  getNrParcela();
		valueToString += ", nrReferencia: " +  getNrReferencia();
		valueToString += ", cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", dsHistorico: " +  getDsHistorico();
		valueToString += ", dtVencimento: " +  sol.util.Util.formatDateTime(getDtVencimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtEmissao: " +  sol.util.Util.formatDateTime(getDtEmissao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtRecebimento: " +  sol.util.Util.formatDateTime(getDtRecebimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtProrrogacao: " +  sol.util.Util.formatDateTime(getDtProrrogacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlConta: " +  getVlConta();
		valueToString += ", vlAbatimento: " +  getVlAbatimento();
		valueToString += ", vlAcrescimo: " +  getVlAcrescimo();
		valueToString += ", vlRecebido: " +  getVlRecebido();
		valueToString += ", stConta: " +  getStConta();
		valueToString += ", tpFrequencia: " +  getTpFrequencia();
		valueToString += ", qtParcelas: " +  getQtParcelas();
		valueToString += ", tpContaReceber: " +  getTpContaReceber();
		valueToString += ", cdNegociacao: " +  getCdNegociacao();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdPlanoPagamento: " +  getCdPlanoPagamento();
		valueToString += ", cdFormaPagamento: " +  getCdFormaPagamento();
		valueToString += ", dtDigitacao: " +  sol.util.Util.formatDateTime(getDtDigitacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtVencimentoOriginal: " +  sol.util.Util.formatDateTime(getDtVencimentoOriginal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdTurno: " +  getCdTurno();
		valueToString += ", prJuros: " +  getPrJuros();
		valueToString += ", prMulta: " +  getPrMulta();
		valueToString += ", lgProtesto: " +  getLgProtesto();
		valueToString += ", lgPrioritaria: " +  getLgPrioritaria();
		valueToString += ", cdFormaPagamentoPreferencial: " +  getCdFormaPagamentoPreferencial();
		valueToString += ", cdContaSacado: " +  getCdContaSacado();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaReceber(getCdContaReceber(),
			getCdPessoa(),
			getCdEmpresa(),
			getCdContrato(),
			getCdContaOrigem(),
			getCdDocumentoSaida(),
			getCdContaCarteira(),
			getCdConta(),
			getCdFrete(),
			getNrDocumento(),
			getIdContaReceber(),
			getNrParcela(),
			getNrReferencia(),
			getCdTipoDocumento(),
			getDsHistorico(),
			getDtVencimento()==null ? null : (GregorianCalendar)getDtVencimento().clone(),
			getDtEmissao()==null ? null : (GregorianCalendar)getDtEmissao().clone(),
			getDtRecebimento()==null ? null : (GregorianCalendar)getDtRecebimento().clone(),
			getDtProrrogacao()==null ? null : (GregorianCalendar)getDtProrrogacao().clone(),
			getVlConta(),
			getVlAbatimento(),
			getVlAcrescimo(),
			getVlRecebido(),
			getStConta(),
			getTpFrequencia(),
			getQtParcelas(),
			getTpContaReceber(),
			getCdNegociacao(),
			getTxtObservacao(),
			getCdPlanoPagamento(),
			getCdFormaPagamento(),
			getDtDigitacao()==null ? null : (GregorianCalendar)getDtDigitacao().clone(),
			getDtVencimentoOriginal()==null ? null : (GregorianCalendar)getDtVencimentoOriginal().clone(),
			getCdTurno(),
			getPrJuros(),
			getPrMulta(),
			getLgProtesto(),
			getLgPrioritaria(),
			getCdFormaPagamentoPreferencial(),
			getCdContaSacado(),
			getCdUsuario());
	}

}
