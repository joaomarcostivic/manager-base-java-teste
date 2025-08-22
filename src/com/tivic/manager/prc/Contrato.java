package com.tivic.manager.prc;

import java.util.GregorianCalendar;

public class Contrato {

	private int cdContrato;
	private int cdDevedor;
	private int cdProcesso;
	private String nrAgencia;
	private String nrConta;
	private String nrDv;
	private String nrCarteira;
	private String nrContrato;
	private String nrCnpjIncorporacao;
	private String nrAgenciaIncorporacao;
	private String nrContaIncorporacao;
	private String nrDvIncorporacao;
	private String nrCarteiraIncorporacao;
	private String nrContratoIncorporacao;
	private GregorianCalendar dtInicio;
	private Double vlPrincipal;
	private Double vlLiquido;
	private Double prJuros;
	private int tpJuros;
	private int qtParcelas;
	private Double vlParcela;
	private GregorianCalendar dtVencimentoPrimeira;
	private Double vlSaldoVencido;
	private Double vlCobranca;
	private int qtParcelasAvencer;
	private Double vlSaldoAvencer;
	private Double vlAvista;
	private Double vlAprazo;
	private Double vlTotal;
	private GregorianCalendar dtAtualizacaoEdi;
	private int stAtualizacaoEdi;
	private int stContrato;
	private GregorianCalendar dtUltimoPagamento;

	public Contrato() { }

	public Contrato(int cdContrato,
			int cdDevedor,
			int cdProcesso,
			String nrAgencia,
			String nrConta,
			String nrDv,
			String nrCarteira,
			String nrContrato,
			String nrCnpjIncorporacao,
			String nrAgenciaIncorporacao,
			String nrContaIncorporacao,
			String nrDvIncorporacao,
			String nrCarteiraIncorporacao,
			String nrContratoIncorporacao,
			GregorianCalendar dtInicio,
			Double vlPrincipal,
			Double vlLiquido,
			Double prJuros,
			int tpJuros,
			int qtParcelas,
			Double vlParcela,
			GregorianCalendar dtVencimentoPrimeira,
			Double vlSaldoVencido,
			Double vlCobranca,
			int qtParcelasAvencer,
			Double vlSaldoAvencer,
			Double vlAvista,
			Double vlAprazo,
			Double vlTotal,
			GregorianCalendar dtAtualizacaoEdi,
			int stAtualizacaoEdi,
			int stContrato,
			GregorianCalendar dtUltimoPagamento) {
		setCdContrato(cdContrato);
		setCdDevedor(cdDevedor);
		setCdProcesso(cdProcesso);
		setNrAgencia(nrAgencia);
		setNrConta(nrConta);
		setNrDv(nrDv);
		setNrCarteira(nrCarteira);
		setNrContrato(nrContrato);
		setNrCnpjIncorporacao(nrCnpjIncorporacao);
		setNrAgenciaIncorporacao(nrAgenciaIncorporacao);
		setNrContaIncorporacao(nrContaIncorporacao);
		setNrDvIncorporacao(nrDvIncorporacao);
		setNrCarteiraIncorporacao(nrCarteiraIncorporacao);
		setNrContratoIncorporacao(nrContratoIncorporacao);
		setDtInicio(dtInicio);
		setVlPrincipal(vlPrincipal);
		setVlLiquido(vlLiquido);
		setPrJuros(prJuros);
		setTpJuros(tpJuros);
		setQtParcelas(qtParcelas);
		setVlParcela(vlParcela);
		setDtVencimentoPrimeira(dtVencimentoPrimeira);
		setVlSaldoVencido(vlSaldoVencido);
		setVlCobranca(vlCobranca);
		setQtParcelasAvencer(qtParcelasAvencer);
		setVlSaldoAvencer(vlSaldoAvencer);
		setVlAvista(vlAvista);
		setVlAprazo(vlAprazo);
		setVlTotal(vlTotal);
		setDtAtualizacaoEdi(dtAtualizacaoEdi);
		setStAtualizacaoEdi(stAtualizacaoEdi);
		setStContrato(stContrato);
		setDtUltimoPagamento(dtUltimoPagamento);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdDevedor(int cdDevedor){
		this.cdDevedor=cdDevedor;
	}
	public int getCdDevedor(){
		return this.cdDevedor;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setNrAgencia(String nrAgencia){
		this.nrAgencia=nrAgencia;
	}
	public String getNrAgencia(){
		return this.nrAgencia;
	}
	public void setNrConta(String nrConta){
		this.nrConta=nrConta;
	}
	public String getNrConta(){
		return this.nrConta;
	}
	public void setNrDv(String nrDv){
		this.nrDv=nrDv;
	}
	public String getNrDv(){
		return this.nrDv;
	}
	public void setNrCarteira(String nrCarteira){
		this.nrCarteira=nrCarteira;
	}
	public String getNrCarteira(){
		return this.nrCarteira;
	}
	public void setNrContrato(String nrContrato){
		this.nrContrato=nrContrato;
	}
	public String getNrContrato(){
		return this.nrContrato;
	}
	public void setNrCnpjIncorporacao(String nrCnpjIncorporacao){
		this.nrCnpjIncorporacao=nrCnpjIncorporacao;
	}
	public String getNrCnpjIncorporacao(){
		return this.nrCnpjIncorporacao;
	}
	public void setNrAgenciaIncorporacao(String nrAgenciaIncorporacao){
		this.nrAgenciaIncorporacao=nrAgenciaIncorporacao;
	}
	public String getNrAgenciaIncorporacao(){
		return this.nrAgenciaIncorporacao;
	}
	public void setNrContaIncorporacao(String nrContaIncorporacao){
		this.nrContaIncorporacao=nrContaIncorporacao;
	}
	public String getNrContaIncorporacao(){
		return this.nrContaIncorporacao;
	}
	public void setNrDvIncorporacao(String nrDvIncorporacao){
		this.nrDvIncorporacao=nrDvIncorporacao;
	}
	public String getNrDvIncorporacao(){
		return this.nrDvIncorporacao;
	}
	public void setNrCarteiraIncorporacao(String nrCarteiraIncorporacao){
		this.nrCarteiraIncorporacao=nrCarteiraIncorporacao;
	}
	public String getNrCarteiraIncorporacao(){
		return this.nrCarteiraIncorporacao;
	}
	public void setNrContratoIncorporacao(String nrContratoIncorporacao){
		this.nrContratoIncorporacao=nrContratoIncorporacao;
	}
	public String getNrContratoIncorporacao(){
		return this.nrContratoIncorporacao;
	}
	public void setDtInicio(GregorianCalendar dtInicio){
		this.dtInicio=dtInicio;
	}
	public GregorianCalendar getDtInicio(){
		return this.dtInicio;
	}
	public void setVlPrincipal(Double vlPrincipal){
		this.vlPrincipal=vlPrincipal;
	}
	public Double getVlPrincipal(){
		return this.vlPrincipal;
	}
	public void setVlLiquido(Double vlLiquido){
		this.vlLiquido=vlLiquido;
	}
	public Double getVlLiquido(){
		return this.vlLiquido;
	}
	public void setPrJuros(Double prJuros){
		this.prJuros=prJuros;
	}
	public Double getPrJuros(){
		return this.prJuros;
	}
	public void setTpJuros(int tpJuros){
		this.tpJuros=tpJuros;
	}
	public int getTpJuros(){
		return this.tpJuros;
	}
	public void setQtParcelas(int qtParcelas){
		this.qtParcelas=qtParcelas;
	}
	public int getQtParcelas(){
		return this.qtParcelas;
	}
	public void setVlParcela(Double vlParcela){
		this.vlParcela=vlParcela;
	}
	public Double getVlParcela(){
		return this.vlParcela;
	}
	public void setDtVencimentoPrimeira(GregorianCalendar dtVencimentoPrimeira){
		this.dtVencimentoPrimeira=dtVencimentoPrimeira;
	}
	public GregorianCalendar getDtVencimentoPrimeira(){
		return this.dtVencimentoPrimeira;
	}
	public void setVlSaldoVencido(Double vlSaldoVencido){
		this.vlSaldoVencido=vlSaldoVencido;
	}
	public Double getVlSaldoVencido(){
		return this.vlSaldoVencido;
	}
	public void setVlCobranca(Double vlCobranca){
		this.vlCobranca=vlCobranca;
	}
	public Double getVlCobranca(){
		return this.vlCobranca;
	}
	public void setQtParcelasAvencer(int qtParcelasAvencer){
		this.qtParcelasAvencer=qtParcelasAvencer;
	}
	public int getQtParcelasAvencer(){
		return this.qtParcelasAvencer;
	}
	public void setVlSaldoAvencer(Double vlSaldoAvencer){
		this.vlSaldoAvencer=vlSaldoAvencer;
	}
	public Double getVlSaldoAvencer(){
		return this.vlSaldoAvencer;
	}
	public void setVlAvista(Double vlAvista){
		this.vlAvista=vlAvista;
	}
	public Double getVlAvista(){
		return this.vlAvista;
	}
	public void setVlAprazo(Double vlAprazo){
		this.vlAprazo=vlAprazo;
	}
	public Double getVlAprazo(){
		return this.vlAprazo;
	}
	public void setVlTotal(Double vlTotal){
		this.vlTotal=vlTotal;
	}
	public Double getVlTotal(){
		return this.vlTotal;
	}
	public void setDtAtualizacaoEdi(GregorianCalendar dtAtualizacaoEdi){
		this.dtAtualizacaoEdi=dtAtualizacaoEdi;
	}
	public GregorianCalendar getDtAtualizacaoEdi(){
		return this.dtAtualizacaoEdi;
	}
	public void setStAtualizacaoEdi(int stAtualizacaoEdi){
		this.stAtualizacaoEdi=stAtualizacaoEdi;
	}
	public int getStAtualizacaoEdi(){
		return this.stAtualizacaoEdi;
	}
	public void setStContrato(int stContrato){
		this.stContrato=stContrato;
	}
	public int getStContrato(){
		return this.stContrato;
	}
	public void setDtUltimoPagamento(GregorianCalendar dtUltimoPagamento){
		this.dtUltimoPagamento=dtUltimoPagamento;
	}
	public GregorianCalendar getDtUltimoPagamento(){
		return this.dtUltimoPagamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdDevedor: " +  getCdDevedor();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", nrAgencia: " +  getNrAgencia();
		valueToString += ", nrConta: " +  getNrConta();
		valueToString += ", nrDv: " +  getNrDv();
		valueToString += ", nrCarteira: " +  getNrCarteira();
		valueToString += ", nrContrato: " +  getNrContrato();
		valueToString += ", nrCnpjIncorporacao: " +  getNrCnpjIncorporacao();
		valueToString += ", nrAgenciaIncorporacao: " +  getNrAgenciaIncorporacao();
		valueToString += ", nrContaIncorporacao: " +  getNrContaIncorporacao();
		valueToString += ", nrDvIncorporacao: " +  getNrDvIncorporacao();
		valueToString += ", nrCarteiraIncorporacao: " +  getNrCarteiraIncorporacao();
		valueToString += ", nrContratoIncorporacao: " +  getNrContratoIncorporacao();
		valueToString += ", dtInicio: " +  sol.util.Util.formatDateTime(getDtInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlPrincipal: " +  getVlPrincipal();
		valueToString += ", vlLiquido: " +  getVlLiquido();
		valueToString += ", prJuros: " +  getPrJuros();
		valueToString += ", tpJuros: " +  getTpJuros();
		valueToString += ", qtParcelas: " +  getQtParcelas();
		valueToString += ", vlParcela: " +  getVlParcela();
		valueToString += ", dtVencimentoPrimeira: " +  sol.util.Util.formatDateTime(getDtVencimentoPrimeira(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlSaldoVencido: " +  getVlSaldoVencido();
		valueToString += ", vlCobranca: " +  getVlCobranca();
		valueToString += ", qtParcelasAvencer: " +  getQtParcelasAvencer();
		valueToString += ", vlSaldoAvencer: " +  getVlSaldoAvencer();
		valueToString += ", vlAvista: " +  getVlAvista();
		valueToString += ", vlAprazo: " +  getVlAprazo();
		valueToString += ", vlTotal: " +  getVlTotal();
		valueToString += ", dtAtualizacaoEdi: " +  sol.util.Util.formatDateTime(getDtAtualizacaoEdi(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stAtualizacaoEdi: " +  getStAtualizacaoEdi();
		valueToString += ", stContrato: " +  getStContrato();
		valueToString += ", dtUltimoPagamento: " +  sol.util.Util.formatDateTime(getDtUltimoPagamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Contrato(getCdContrato(),
			getCdDevedor(),
			getCdProcesso(),
			getNrAgencia(),
			getNrConta(),
			getNrDv(),
			getNrCarteira(),
			getNrContrato(),
			getNrCnpjIncorporacao(),
			getNrAgenciaIncorporacao(),
			getNrContaIncorporacao(),
			getNrDvIncorporacao(),
			getNrCarteiraIncorporacao(),
			getNrContratoIncorporacao(),
			getDtInicio()==null ? null : (GregorianCalendar)getDtInicio().clone(),
			getVlPrincipal(),
			getVlLiquido(),
			getPrJuros(),
			getTpJuros(),
			getQtParcelas(),
			getVlParcela(),
			getDtVencimentoPrimeira()==null ? null : (GregorianCalendar)getDtVencimentoPrimeira().clone(),
			getVlSaldoVencido(),
			getVlCobranca(),
			getQtParcelasAvencer(),
			getVlSaldoAvencer(),
			getVlAvista(),
			getVlAprazo(),
			getVlTotal(),
			getDtAtualizacaoEdi()==null ? null : (GregorianCalendar)getDtAtualizacaoEdi().clone(),
			getStAtualizacaoEdi(),
			getStContrato(),
			getDtUltimoPagamento()==null ? null : (GregorianCalendar)getDtUltimoPagamento().clone());
	}

}