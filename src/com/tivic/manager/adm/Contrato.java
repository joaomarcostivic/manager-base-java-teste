package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class Contrato {

	private int cdContrato;
	private int cdConvenio;
	private int cdCategoriaParcelas;
	private int cdEmpresa;
	private int cdPessoa;
	private int cdModeloContrato;
	private int cdIndicador;
	private GregorianCalendar dtAssinatura;
	private GregorianCalendar dtPrimeiraParcela;
	private int nrDiaVencimento;
	private int nrParcelas;
	private float prJurosMora;
	private float prMultaMora;
	private float prDescontoAdimplencia;
	private float prDesconto;
	private int tpContrato;
	private float vlParcelas;
	private float vlAdesao;
	private float vlContrato;
	private String nrContrato;
	private String txtContrato;
	private int stContrato;
	private String idContrato;
	private GregorianCalendar dtInicioVigencia;
	private GregorianCalendar dtFinalVigencia;
	private int cdAgente;
	private int cdContaCarteira;
	private int cdConta;
	private int tpAmortizacao;
	private int gnContrato;
	private float prJuros;
	private int cdTipoOperacao;
	private int cdDocumento;
	private int tpDesconto;
	private float vlDesconto;
	private int cdContratoOrigem;
	private String txtObservacao;
	private int cdCategoriaAdesao;

	public Contrato(int cdContrato,
			int cdConvenio,
			int cdCategoriaParcelas,
			int cdEmpresa,
			int cdPessoa,
			int cdModeloContrato,
			int cdIndicador,
			GregorianCalendar dtAssinatura,
			GregorianCalendar dtPrimeiraParcela,
			int nrDiaVencimento,
			int nrParcelas,
			float prJurosMora,
			float prMultaMora,
			float prDescontoAdimplencia,
			float prDesconto,
			int tpContrato,
			float vlParcelas,
			float vlAdesao,
			float vlContrato,
			String nrContrato,
			String txtContrato,
			int stContrato,
			String idContrato,
			GregorianCalendar dtInicioVigencia,
			GregorianCalendar dtFinalVigencia,
			int cdAgente,
			int cdContaCarteira,
			int cdConta,
			int tpAmortizacao,
			int gnContrato,
			float prJuros,
			int cdTipoOperacao,
			int cdDocumento,
			int tpDesconto,
			float vlDesconto,
			int cdContratoOrigem,
			String txtObservacao,
			int cdCategoriaAdesao){
		setCdContrato(cdContrato);
		setCdConvenio(cdConvenio);
		setCdCategoriaParcelas(cdCategoriaParcelas);
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setCdModeloContrato(cdModeloContrato);
		setCdIndicador(cdIndicador);
		setDtAssinatura(dtAssinatura);
		setDtPrimeiraParcela(dtPrimeiraParcela);
		setNrDiaVencimento(nrDiaVencimento);
		setNrParcelas(nrParcelas);
		setPrJurosMora(prJurosMora);
		setPrMultaMora(prMultaMora);
		setPrDescontoAdimplencia(prDescontoAdimplencia);
		setPrDesconto(prDesconto);
		setTpContrato(tpContrato);
		setVlParcelas(vlParcelas);
		setVlAdesao(vlAdesao);
		setVlContrato(vlContrato);
		setNrContrato(nrContrato);
		setTxtContrato(txtContrato);
		setStContrato(stContrato);
		setIdContrato(idContrato);
		setDtInicioVigencia(dtInicioVigencia);
		setDtFinalVigencia(dtFinalVigencia);
		setCdAgente(cdAgente);
		setCdContaCarteira(cdContaCarteira);
		setCdConta(cdConta);
		setTpAmortizacao(tpAmortizacao);
		setGnContrato(gnContrato);
		setPrJuros(prJuros);
		setCdTipoOperacao(cdTipoOperacao);
		setCdDocumento(cdDocumento);
		setTpDesconto(tpDesconto);
		setVlDesconto(vlDesconto);
		setCdContratoOrigem(cdContratoOrigem);
		setTxtObservacao(txtObservacao);
		setCdCategoriaAdesao(cdCategoriaAdesao);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdConvenio(int cdConvenio){
		this.cdConvenio=cdConvenio;
	}
	public int getCdConvenio(){
		return this.cdConvenio;
	}
	public void setCdCategoriaParcelas(int cdCategoriaParcelas){
		this.cdCategoriaParcelas=cdCategoriaParcelas;
	}
	public int getCdCategoriaParcelas(){
		return this.cdCategoriaParcelas;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdModeloContrato(int cdModeloContrato){
		this.cdModeloContrato=cdModeloContrato;
	}
	public int getCdModeloContrato(){
		return this.cdModeloContrato;
	}
	public void setCdIndicador(int cdIndicador){
		this.cdIndicador=cdIndicador;
	}
	public int getCdIndicador(){
		return this.cdIndicador;
	}
	public void setDtAssinatura(GregorianCalendar dtAssinatura){
		this.dtAssinatura=dtAssinatura;
	}
	public GregorianCalendar getDtAssinatura(){
		return this.dtAssinatura;
	}
	public void setDtPrimeiraParcela(GregorianCalendar dtPrimeiraParcela){
		this.dtPrimeiraParcela=dtPrimeiraParcela;
	}
	public GregorianCalendar getDtPrimeiraParcela(){
		return this.dtPrimeiraParcela;
	}
	public void setNrDiaVencimento(int nrDiaVencimento){
		this.nrDiaVencimento=nrDiaVencimento;
	}
	public int getNrDiaVencimento(){
		return this.nrDiaVencimento;
	}
	public void setNrParcelas(int nrParcelas){
		this.nrParcelas=nrParcelas;
	}
	public int getNrParcelas(){
		return this.nrParcelas;
	}
	public void setPrJurosMora(float prJurosMora){
		this.prJurosMora=prJurosMora;
	}
	public float getPrJurosMora(){
		return this.prJurosMora;
	}
	public void setPrMultaMora(float prMultaMora){
		this.prMultaMora=prMultaMora;
	}
	public float getPrMultaMora(){
		return this.prMultaMora;
	}
	public void setPrDescontoAdimplencia(float prDescontoAdimplencia){
		this.prDescontoAdimplencia=prDescontoAdimplencia;
	}
	public float getPrDescontoAdimplencia(){
		return this.prDescontoAdimplencia;
	}
	public void setPrDesconto(float prDesconto){
		this.prDesconto=prDesconto;
	}
	public float getPrDesconto(){
		return this.prDesconto;
	}
	public void setTpContrato(int tpContrato){
		this.tpContrato=tpContrato;
	}
	public int getTpContrato(){
		return this.tpContrato;
	}
	public void setVlParcelas(float vlParcelas){
		this.vlParcelas=vlParcelas;
	}
	public float getVlParcelas(){
		return this.vlParcelas;
	}
	public void setVlAdesao(float vlAdesao){
		this.vlAdesao=vlAdesao;
	}
	public float getVlAdesao(){
		return this.vlAdesao;
	}
	public void setVlContrato(float vlContrato){
		this.vlContrato=vlContrato;
	}
	public float getVlContrato(){
		return this.vlContrato;
	}
	public void setNrContrato(String nrContrato){
		this.nrContrato=nrContrato;
	}
	public String getNrContrato(){
		return this.nrContrato;
	}
	public void setTxtContrato(String txtContrato){
		this.txtContrato=txtContrato;
	}
	public String getTxtContrato(){
		return this.txtContrato;
	}
	public void setStContrato(int stContrato){
		this.stContrato=stContrato;
	}
	public int getStContrato(){
		return this.stContrato;
	}
	public void setIdContrato(String idContrato){
		this.idContrato=idContrato;
	}
	public String getIdContrato(){
		return this.idContrato;
	}
	public void setDtInicioVigencia(GregorianCalendar dtInicioVigencia){
		this.dtInicioVigencia=dtInicioVigencia;
	}
	public GregorianCalendar getDtInicioVigencia(){
		return this.dtInicioVigencia;
	}
	public void setDtFinalVigencia(GregorianCalendar dtFinalVigencia){
		this.dtFinalVigencia=dtFinalVigencia;
	}
	public GregorianCalendar getDtFinalVigencia(){
		return this.dtFinalVigencia;
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
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
	public void setTpAmortizacao(int tpAmortizacao){
		this.tpAmortizacao=tpAmortizacao;
	}
	public int getTpAmortizacao(){
		return this.tpAmortizacao;
	}
	public void setGnContrato(int gnContrato){
		this.gnContrato=gnContrato;
	}
	public int getGnContrato(){
		return this.gnContrato;
	}
	public void setPrJuros(float prJuros){
		this.prJuros=prJuros;
	}
	public float getPrJuros(){
		return this.prJuros;
	}
	public void setCdTipoOperacao(int cdTipoOperacao){
		this.cdTipoOperacao=cdTipoOperacao;
	}
	public int getCdTipoOperacao(){
		return this.cdTipoOperacao;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setTpDesconto(int tpDesconto){
		this.tpDesconto=tpDesconto;
	}
	public int getTpDesconto(){
		return this.tpDesconto;
	}
	public void setVlDesconto(float vlDesconto){
		this.vlDesconto=vlDesconto;
	}
	public float getVlDesconto(){
		return this.vlDesconto;
	}
	public void setCdContratoOrigem(int cdContratoOrigem){
		this.cdContratoOrigem=cdContratoOrigem;
	}
	public int getCdContratoOrigem(){
		return this.cdContratoOrigem;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdCategoriaAdesao(int cdCategoriaAdesao){
		this.cdCategoriaAdesao=cdCategoriaAdesao;
	}
	public int getCdCategoriaAdesao(){
		return this.cdCategoriaAdesao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdConvenio: " +  getCdConvenio();
		valueToString += ", cdCategoriaParcelas: " +  getCdCategoriaParcelas();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdModeloContrato: " +  getCdModeloContrato();
		valueToString += ", cdIndicador: " +  getCdIndicador();
		valueToString += ", dtAssinatura: " +  sol.util.Util.formatDateTime(getDtAssinatura(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtPrimeiraParcela: " +  sol.util.Util.formatDateTime(getDtPrimeiraParcela(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrDiaVencimento: " +  getNrDiaVencimento();
		valueToString += ", nrParcelas: " +  getNrParcelas();
		valueToString += ", prJurosMora: " +  getPrJurosMora();
		valueToString += ", prMultaMora: " +  getPrMultaMora();
		valueToString += ", prDescontoAdimplencia: " +  getPrDescontoAdimplencia();
		valueToString += ", prDesconto: " +  getPrDesconto();
		valueToString += ", tpContrato: " +  getTpContrato();
		valueToString += ", vlParcelas: " +  getVlParcelas();
		valueToString += ", vlAdesao: " +  getVlAdesao();
		valueToString += ", vlContrato: " +  getVlContrato();
		valueToString += ", nrContrato: " +  getNrContrato();
		valueToString += ", txtContrato: " +  getTxtContrato();
		valueToString += ", stContrato: " +  getStContrato();
		valueToString += ", idContrato: " +  getIdContrato();
		valueToString += ", dtInicioVigencia: " +  sol.util.Util.formatDateTime(getDtInicioVigencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinalVigencia: " +  sol.util.Util.formatDateTime(getDtFinalVigencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", cdContaCarteira: " +  getCdContaCarteira();
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", tpAmortizacao: " +  getTpAmortizacao();
		valueToString += ", gnContrato: " +  getGnContrato();
		valueToString += ", prJuros: " +  getPrJuros();
		valueToString += ", cdTipoOperacao: " +  getCdTipoOperacao();
		valueToString += ", cdDocumento: " +  getCdDocumento();
		valueToString += ", tpDesconto: " +  getTpDesconto();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		valueToString += ", cdContratoOrigem: " +  getCdContratoOrigem();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdCategoriaAdesao: " +  getCdCategoriaAdesao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Contrato(getCdContrato(),
			getCdConvenio(),
			getCdCategoriaParcelas(),
			getCdEmpresa(),
			getCdPessoa(),
			getCdModeloContrato(),
			getCdIndicador(),
			getDtAssinatura()==null ? null : (GregorianCalendar)getDtAssinatura().clone(),
			getDtPrimeiraParcela()==null ? null : (GregorianCalendar)getDtPrimeiraParcela().clone(),
			getNrDiaVencimento(),
			getNrParcelas(),
			getPrJurosMora(),
			getPrMultaMora(),
			getPrDescontoAdimplencia(),
			getPrDesconto(),
			getTpContrato(),
			getVlParcelas(),
			getVlAdesao(),
			getVlContrato(),
			getNrContrato(),
			getTxtContrato(),
			getStContrato(),
			getIdContrato(),
			getDtInicioVigencia()==null ? null : (GregorianCalendar)getDtInicioVigencia().clone(),
			getDtFinalVigencia()==null ? null : (GregorianCalendar)getDtFinalVigencia().clone(),
			getCdAgente(),
			getCdContaCarteira(),
			getCdConta(),
			getTpAmortizacao(),
			getGnContrato(),
			getPrJuros(),
			getCdTipoOperacao(),
			getCdDocumento(),
			getTpDesconto(),
			getVlDesconto(),
			getCdContratoOrigem(),
			getTxtObservacao(),
			getCdCategoriaAdesao());
	}

}
