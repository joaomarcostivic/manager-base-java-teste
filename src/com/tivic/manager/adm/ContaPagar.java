package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class ContaPagar {

	private int cdContaPagar;
	private int cdContrato;
	private int cdPessoa;
	private int cdEmpresa;
	private int cdContaOrigem;
	private int cdDocumentoEntrada;
	private int cdConta;
	private int cdContaBancaria;
	private GregorianCalendar dtVencimento;
	private GregorianCalendar dtEmissao;
	private GregorianCalendar dtPagamento;
	private GregorianCalendar dtAutorizacao;
	private String nrDocumento;
	private String nrReferencia;
	private int nrParcela;
	private int cdTipoDocumento;
	private double vlConta;
	private double vlAbatimento;
	private double vlAcrescimo;
	private double vlPago;
	private String dsHistorico;
	private int stConta;
	private int lgAutorizado;
	private int tpFrequencia;
	private int qtParcelas;
	private double vlBaseAutorizacao;
	private int cdViagem;
	private int cdManutencao;
	private String txtObservacao;
	private GregorianCalendar dtDigitacao;
	private GregorianCalendar dtVencimentoOriginal;
	private int cdTurno;
	private int cdArquivo;
	private int lgPrioritaria;
	private int cdFormaPagamentoPreferencial;
	private String nrCodigoBarras;
	private int cdContaFavorecido;
	private int cdUsuario;
	
	public ContaPagar(){}
	
	public ContaPagar(int cdContaPagar,
			int cdContrato,
			int cdPessoa,
			int cdEmpresa,
			int cdContaOrigem,
			int cdDocumentoEntrada,
			int cdConta,
			int cdContaBancaria,
			GregorianCalendar dtVencimento,
			GregorianCalendar dtEmissao,
			GregorianCalendar dtPagamento,
			GregorianCalendar dtAutorizacao,
			String nrDocumento,
			String nrReferencia,
			int nrParcela,
			int cdTipoDocumento,
			double vlConta,
			double vlAbatimento,
			double vlAcrescimo,
			double vlPago,
			String dsHistorico,
			int stConta,
			int lgAutorizado,
			int tpFrequencia,
			int qtParcelas,
			double vlBaseAutorizacao,
			int cdViagem,
			int cdManutencao,
			String txtObservacao,
			GregorianCalendar dtDigitacao,
			GregorianCalendar dtVencimentoOriginal,
			int cdArquivo,
			int cdTurno,
			int lgPrioritaria,
			int cdFormaPagamentoPreferencial,
			String nrCodigoBarras,
			int cdContaFavorecido,
			int cdUsuario){
		setCdContaPagar(cdContaPagar);
		setCdContrato(cdContrato);
		setCdPessoa(cdPessoa);
		setCdEmpresa(cdEmpresa);
		setCdContaOrigem(cdContaOrigem);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdConta(cdConta);
		setCdContaBancaria(cdContaBancaria);
		setDtVencimento(dtVencimento);
		setDtEmissao(dtEmissao);
		setDtPagamento(dtPagamento);
		setDtAutorizacao(dtAutorizacao);
		setNrDocumento(nrDocumento);
		setNrReferencia(nrReferencia);
		setNrParcela(nrParcela);
		setCdTipoDocumento(cdTipoDocumento);
		setVlConta(vlConta);
		setVlAbatimento(vlAbatimento);
		setVlAcrescimo(vlAcrescimo);
		setVlPago(vlPago);
		setDsHistorico(dsHistorico);
		setStConta(stConta);
		setLgAutorizado(lgAutorizado);
		setTpFrequencia(tpFrequencia);
		setQtParcelas(qtParcelas);
		setVlBaseAutorizacao(vlBaseAutorizacao);
		setCdViagem(cdViagem);
		setCdManutencao(cdManutencao);
		setTxtObservacao(txtObservacao);
		setDtDigitacao(dtDigitacao);
		setDtVencimentoOriginal(dtVencimentoOriginal);
		setCdArquivo(cdArquivo);
		setCdTurno(cdTurno);
		setLgPrioritaria(lgPrioritaria);
		setCdFormaPagamentoPreferencial(cdFormaPagamentoPreferencial);
		setNrCodigoBarras(nrCodigoBarras);
		setCdContaFavorecido(cdContaFavorecido);
		setCdUsuario(cdUsuario);
	}
	
	public ContaPagar(int cdContaPagar,
			int cdPessoa,
			int cdContrato,
			int cdPortador,
			int cdEmpresa,
			GregorianCalendar dtVencimento,
			GregorianCalendar dtEmissao,
			double vlConta,
			GregorianCalendar dtPagamento,
			GregorianCalendar dtAutorizacao,
			String nrDocumento,
			int tpDocumento,
			double vlAbatimento,
			int tpFrequencia,
			double vlAcrescimo,
			double vlPago,
			String nrReferencia,
			double vlBaseAutorizacao,
			int nrParcela,
			int qtParcelas,
			int lgAutorizado,
			int stConta,
			int cdContaOrigem,
			String dsHistorico,
			int cdEntrada,
			int cdCarteira,
			int cdConta,
			int cdContaBancaria,
			int cdFechamento){
		setCdContaPagar(cdContaPagar);
		setCdPessoa(cdPessoa);
		setCdContrato(cdContrato);
		// setCdPortador(cdPortador);
		setCdEmpresa(cdEmpresa);
		setDtVencimento(dtVencimento);
		setDtEmissao(dtEmissao);
		setVlConta(vlConta);
		setDtPagamento(dtPagamento);
		setDtAutorizacao(dtAutorizacao);
		setNrDocumento(nrDocumento);
		// setTpDocumento(tpDocumento);
		setVlAbatimento(vlAbatimento);
		setTpFrequencia(tpFrequencia);
		setVlAcrescimo(vlAcrescimo);
		setVlPago(vlPago);
		setNrReferencia(nrReferencia);
		setVlBaseAutorizacao(vlBaseAutorizacao);
		setNrParcela(nrParcela);
		setQtParcelas(qtParcelas);
		setLgAutorizado(lgAutorizado);
		setStConta(stConta);
		setCdContaOrigem(cdContaOrigem);
		setDsHistorico(dsHistorico);
		// setCdEntrada(cdEntrada);
		// setCdCarteira(cdCarteira);
		setCdConta(cdConta);
		setCdContaBancaria(cdContaBancaria);
		//setCdFechamento(cdFechamento);
	}
	
	public ContaPagar(int cdContaPagar,
			int cdContrato,
			int cdPessoa,
			int cdEmpresa,
			int cdContaOrigem,
			int cdDocumentoEntrada,
			int cdConta,
			int cdContaBancaria,
			GregorianCalendar dtVencimento,
			GregorianCalendar dtEmissao,
			GregorianCalendar dtPagamento,
			GregorianCalendar dtAutorizacao,
			String nrDocumento,
			String nrReferencia,
			int nrParcela,
			int cdTipoDocumento,
			double vlConta,
			double vlAbatimento,
			double vlAcrescimo,
			double vlPago,
			String dsHistorico,
			int stConta,
			int lgAutorizado,
			int tpFrequencia,
			int qtParcelas,
			double vlBaseAutorizacao,
			int cdViagem,
			int cdManutencao,
			String txtObservacao,
			GregorianCalendar dtDigitacao,
			GregorianCalendar dtVencimentoOriginal,
			int cdTurno){
		setCdContaPagar(cdContaPagar);
		setCdContrato(cdContrato);
		setCdPessoa(cdPessoa);
		setCdEmpresa(cdEmpresa);
		setCdContaOrigem(cdContaOrigem);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdConta(cdConta);
		setCdContaBancaria(cdContaBancaria);
		setDtVencimento(dtVencimento);
		setDtEmissao(dtEmissao);
		setDtPagamento(dtPagamento);
		setDtAutorizacao(dtAutorizacao);
		setNrDocumento(nrDocumento);
		setNrReferencia(nrReferencia);
		setNrParcela(nrParcela);
		setCdTipoDocumento(cdTipoDocumento);
		setVlConta(vlConta);
		setVlAbatimento(vlAbatimento);
		setVlAcrescimo(vlAcrescimo);
		setVlPago(vlPago);
		setDsHistorico(dsHistorico);
		setStConta(stConta);
		setLgAutorizado(lgAutorizado);
		setTpFrequencia(tpFrequencia);
		setQtParcelas(qtParcelas);
		setVlBaseAutorizacao(vlBaseAutorizacao);
		setCdViagem(cdViagem);
		setCdManutencao(cdManutencao);
		setTxtObservacao(txtObservacao);
		setDtDigitacao(dtDigitacao);
		setDtVencimentoOriginal(dtVencimentoOriginal);
		setCdTurno(cdTurno);
	}
	
	public ContaPagar(int cdContaPagar,
			int cdContrato,
			int cdPessoa,
			int cdEmpresa,
			int cdContaOrigem,
			int cdDocumentoEntrada,
			int cdConta,
			int cdContaBancaria,
			GregorianCalendar dtVencimento,
			GregorianCalendar dtEmissao,
			GregorianCalendar dtPagamento,
			GregorianCalendar dtAutorizacao,
			String nrDocumento,
			String nrReferencia,
			int nrParcela,
			int cdTipoDocumento,
			double vlConta,
			double vlAbatimento,
			double vlAcrescimo,
			double vlPago,
			String dsHistorico,
			int stConta,
			int lgAutorizado,
			int tpFrequencia,
			int qtParcelas,
			double vlBaseAutorizacao,
			int cdViagem,
			int cdManutencao,
			String txtObservacao,
			GregorianCalendar dtDigitacao,
			GregorianCalendar dtVencimentoOriginal,
			int cdTurno,
			int cdArquivo){
		setCdContaPagar(cdContaPagar);
		setCdContrato(cdContrato);
		setCdPessoa(cdPessoa);
		setCdEmpresa(cdEmpresa);
		setCdContaOrigem(cdContaOrigem);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdConta(cdConta);
		setCdContaBancaria(cdContaBancaria);
		setDtVencimento(dtVencimento);
		setDtEmissao(dtEmissao);
		setDtPagamento(dtPagamento);
		setDtAutorizacao(dtAutorizacao);
		setNrDocumento(nrDocumento);
		setNrReferencia(nrReferencia);
		setNrParcela(nrParcela);
		setCdTipoDocumento(cdTipoDocumento);
		setVlConta(vlConta);
		setVlAbatimento(vlAbatimento);
		setVlAcrescimo(vlAcrescimo);
		setVlPago(vlPago);
		setDsHistorico(dsHistorico);
		setStConta(stConta);
		setLgAutorizado(lgAutorizado);
		setTpFrequencia(tpFrequencia);
		setQtParcelas(qtParcelas);
		setVlBaseAutorizacao(vlBaseAutorizacao);
		setCdViagem(cdViagem);
		setCdManutencao(cdManutencao);
		setTxtObservacao(txtObservacao);
		setDtDigitacao(dtDigitacao);
		setDtVencimentoOriginal(dtVencimentoOriginal);
		setCdTurno(cdTurno);
		setCdArquivo(cdTurno);
	}
	public void setCdContaPagar(int cdContaPagar){
		this.cdContaPagar=cdContaPagar;
	}
	public int getCdContaPagar(){
		return this.cdContaPagar;
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
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
	public void setCdContaOrigem(int cdContaOrigem){
		this.cdContaOrigem=cdContaOrigem;
	}
	public int getCdContaOrigem(){
		return this.cdContaOrigem;
	}
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdContaBancaria(int cdContaBancaria){
		this.cdContaBancaria=cdContaBancaria;
	}
	public int getCdContaBancaria(){
		return this.cdContaBancaria;
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
	public void setDtPagamento(GregorianCalendar dtPagamento){
		this.dtPagamento=dtPagamento;
	}
	public GregorianCalendar getDtPagamento(){
		return this.dtPagamento;
	}
	public void setDtAutorizacao(GregorianCalendar dtAutorizacao){
		this.dtAutorizacao=dtAutorizacao;
	}
	public GregorianCalendar getDtAutorizacao(){
		return this.dtAutorizacao;
	}
	public void setNrDocumento(String nrDocumento){
		this.nrDocumento=nrDocumento;
	}
	public String getNrDocumento(){
		return this.nrDocumento;
	}
	public void setNrReferencia(String nrReferencia){
		this.nrReferencia=nrReferencia;
	}
	public String getNrReferencia(){
		return this.nrReferencia;
	}
	public void setNrParcela(int nrParcela){
		this.nrParcela=nrParcela;
	}
	public int getNrParcela(){
		return this.nrParcela;
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
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
	public void setVlPago(Double vlPago){
		this.vlPago=vlPago;
	}
	public Double getVlPago(){
		return this.vlPago;
	}
	public void setDsHistorico(String dsHistorico){
		this.dsHistorico=dsHistorico;
	}
	public String getDsHistorico(){
		return this.dsHistorico;
	}
	public void setStConta(int stConta){
		this.stConta=stConta;
	}
	public int getStConta(){
		return this.stConta;
	}
	public void setLgAutorizado(int lgAutorizado){
		this.lgAutorizado=lgAutorizado;
	}
	public int getLgAutorizado(){
		return this.lgAutorizado;
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
	public void setVlBaseAutorizacao(Double vlBaseAutorizacao){
		this.vlBaseAutorizacao=vlBaseAutorizacao;
	}
	public Double getVlBaseAutorizacao(){
		return this.vlBaseAutorizacao;
	}
	public void setCdViagem(int cdViagem){
		this.cdViagem=cdViagem;
	}
	public int getCdViagem(){
		return this.cdViagem;
	}
	public void setCdManutencao(int cdManutencao){
		this.cdManutencao=cdManutencao;
	}
	public int getCdManutencao(){
		return this.cdManutencao;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
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
	public void setCdArquivo(int cdArquivo) {
		this.cdArquivo = cdArquivo;
	}
	public int getCdArquivo() {
		return cdArquivo;
	}
	public void setLgPrioritaria(int lgPrioritaria){
		this.lgPrioritaria=lgPrioritaria;
	}
	public int getLgPrioritaria(){
		return this.lgPrioritaria;
	}
	public void setCdFormaPagamentoPreferencial(int cdFormaPagamentoPreferencial){
		this.cdFormaPagamentoPreferencial=cdFormaPagamentoPreferencial;
	}
	public int getCdFormaPagamentoPreferencial(){
		return this.cdFormaPagamentoPreferencial;
	}
	public void setNrCodigoBarras(String nrCodigoBarras){
		this.nrCodigoBarras=nrCodigoBarras;
	}
	public String getNrCodigoBarras(){
		return this.nrCodigoBarras;
	}
	
	public int getCdContaFavorecido() {
		return cdContaFavorecido;
	}

	public void setCdContaFavorecido(int cdContaFavorecido) {
		this.cdContaFavorecido = cdContaFavorecido;
	}

	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContaPagar: " +  getCdContaPagar();
		valueToString += ", cdContrato: " +  getCdContrato();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdContaOrigem: " +  getCdContaOrigem();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", cdContaBancaria: " +  getCdContaBancaria();
		valueToString += ", dtVencimento: " +  sol.util.Util.formatDateTime(getDtVencimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtEmissao: " +  sol.util.Util.formatDateTime(getDtEmissao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtPagamento: " +  sol.util.Util.formatDateTime(getDtPagamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtAutorizacao: " +  sol.util.Util.formatDateTime(getDtAutorizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrDocumento: " +  getNrDocumento();
		valueToString += ", nrReferencia: " +  getNrReferencia();
		valueToString += ", nrParcela: " +  getNrParcela();
		valueToString += ", cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", vlConta: " +  getVlConta();
		valueToString += ", vlAbatimento: " +  getVlAbatimento();
		valueToString += ", vlAcrescimo: " +  getVlAcrescimo();
		valueToString += ", vlPago: " +  getVlPago();
		valueToString += ", dsHistorico: " +  getDsHistorico();
		valueToString += ", stConta: " +  getStConta();
		valueToString += ", lgAutorizado: " +  getLgAutorizado();
		valueToString += ", tpFrequencia: " +  getTpFrequencia();
		valueToString += ", qtParcelas: " +  getQtParcelas();
		valueToString += ", vlBaseAutorizacao: " +  getVlBaseAutorizacao();
		valueToString += ", cdViagem: " +  getCdViagem();
		valueToString += ", cdManutencao: " +  getCdManutencao();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", dtDigitacao: " +  sol.util.Util.formatDateTime(getDtDigitacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtVencimentoOriginal: " +  sol.util.Util.formatDateTime(getDtVencimentoOriginal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdTurno: " +  getCdTurno();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		valueToString += ", lgPrioritaria: " +  getLgPrioritaria();
		valueToString += ", cdFormaPagamentoPreferencial: " +  getCdFormaPagamentoPreferencial();
		valueToString += ", nrCodigoBarras: " +  getNrCodigoBarras();
		valueToString += ", cdContaFavorecido: " +  getCdContaFavorecido();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaPagar(getCdContaPagar(),
			getCdContrato(),
			getCdPessoa(),
			getCdEmpresa(),
			getCdContaOrigem(),
			getCdDocumentoEntrada(),
			getCdConta(),
			getCdContaBancaria(),
			getDtVencimento()==null ? null : (GregorianCalendar)getDtVencimento().clone(),
			getDtEmissao()==null ? null : (GregorianCalendar)getDtEmissao().clone(),
			getDtPagamento()==null ? null : (GregorianCalendar)getDtPagamento().clone(),
			getDtAutorizacao()==null ? null : (GregorianCalendar)getDtAutorizacao().clone(),
			getNrDocumento(),
			getNrReferencia(),
			getNrParcela(),
			getCdTipoDocumento(),
			getVlConta(),
			getVlAbatimento(),
			getVlAcrescimo(),
			getVlPago(),
			getDsHistorico(),
			getStConta(),
			getLgAutorizado(),
			getTpFrequencia(),
			getQtParcelas(),
			getVlBaseAutorizacao(),
			getCdViagem(),
			getCdManutencao(),
			getTxtObservacao(),
			getDtDigitacao()==null ? null : (GregorianCalendar)getDtDigitacao().clone(),
			getDtVencimentoOriginal()==null ? null : (GregorianCalendar)getDtVencimentoOriginal().clone(),
			getCdTurno(),
			getCdArquivo(),
			getLgPrioritaria(),
			getCdFormaPagamentoPreferencial(),
			getNrCodigoBarras(),
			getCdContaFavorecido(),
			getCdUsuario());
	}

}