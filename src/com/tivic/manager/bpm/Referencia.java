package com.tivic.manager.bpm;

import java.util.GregorianCalendar;

public class Referencia {

	private int cdReferencia;
	private int cdBem;
	private int cdSetor;
	private int cdDocumentoEntrada;
	private int cdEmpresa;
	private int cdMarca;
	private GregorianCalendar dtAquisicao;
	private GregorianCalendar dtGarantia;
	private GregorianCalendar dtValidade;
	private GregorianCalendar dtBaixa;
	private String nrSerie;
	private String nrTombo;
	private int stReferencia;
	private String nmModelo;
	private GregorianCalendar dtIncorporacao;
	private float qtCapacidade;
	private int lgProducao;
	private String idReferencia;
	private int cdLocalArmazenamento;
	private String tpReferencia;
	private String txtVersao;

	public Referencia() { }
	
	public Referencia(int cdReferencia,
			int cdBem,
			int cdSetor,
			int cdDocumentoEntrada,
			int cdEmpresa,
			int cdMarca,
			GregorianCalendar dtAquisicao,
			GregorianCalendar dtGarantia,
			GregorianCalendar dtValidade,
			GregorianCalendar dtBaixa,
			String nrSerie,
			String nrTombo,
			int stReferencia,
			String nmModelo,
			GregorianCalendar dtIncorporacao,
			float qtCapacidade,
			int lgProducao,
			String idReferencia,
			int cdLocalArmazenamento,
			String tpReferencia,
			String txtVersao){
		setCdReferencia(cdReferencia);
		setCdBem(cdBem);
		setCdSetor(cdSetor);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdEmpresa(cdEmpresa);
		setCdMarca(cdMarca);
		setDtAquisicao(dtAquisicao);
		setDtGarantia(dtGarantia);
		setDtValidade(dtValidade);
		setDtBaixa(dtBaixa);
		setNrSerie(nrSerie);
		setNrTombo(nrTombo);
		setStReferencia(stReferencia);
		setNmModelo(nmModelo);
		setDtIncorporacao(dtIncorporacao);
		setQtCapacidade(qtCapacidade);
		setLgProducao(lgProducao);
		setIdReferencia(idReferencia);
		setCdLocalArmazenamento(cdLocalArmazenamento);
		setTpReferencia(tpReferencia);
		setTxtVersao(txtVersao);
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setCdBem(int cdBem){
		this.cdBem=cdBem;
	}
	public int getCdBem(){
		return this.cdBem;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
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
	public void setCdMarca(int cdMarca){
		this.cdMarca=cdMarca;
	}
	public int getCdMarca(){
		return this.cdMarca;
	}
	public void setDtAquisicao(GregorianCalendar dtAquisicao){
		this.dtAquisicao=dtAquisicao;
	}
	public GregorianCalendar getDtAquisicao(){
		return this.dtAquisicao;
	}
	public void setDtGarantia(GregorianCalendar dtGarantia){
		this.dtGarantia=dtGarantia;
	}
	public GregorianCalendar getDtGarantia(){
		return this.dtGarantia;
	}
	public void setDtValidade(GregorianCalendar dtValidade){
		this.dtValidade=dtValidade;
	}
	public GregorianCalendar getDtValidade(){
		return this.dtValidade;
	}
	public void setDtBaixa(GregorianCalendar dtBaixa){
		this.dtBaixa=dtBaixa;
	}
	public GregorianCalendar getDtBaixa(){
		return this.dtBaixa;
	}
	public void setNrSerie(String nrSerie){
		this.nrSerie=nrSerie;
	}
	public String getNrSerie(){
		return this.nrSerie;
	}
	public void setNrTombo(String nrTombo){
		this.nrTombo=nrTombo;
	}
	public String getNrTombo(){
		return this.nrTombo;
	}
	public void setStReferencia(int stReferencia){
		this.stReferencia=stReferencia;
	}
	public int getStReferencia(){
		return this.stReferencia;
	}
	public void setNmModelo(String nmModelo){
		this.nmModelo=nmModelo;
	}
	public String getNmModelo(){
		return this.nmModelo;
	}
	public void setDtIncorporacao(GregorianCalendar dtIncorporacao){
		this.dtIncorporacao=dtIncorporacao;
	}
	public GregorianCalendar getDtIncorporacao(){
		return this.dtIncorporacao;
	}
	public void setQtCapacidade(float qtCapacidade){
		this.qtCapacidade=qtCapacidade;
	}
	public float getQtCapacidade(){
		return this.qtCapacidade;
	}
	public void setLgProducao(int lgProducao){
		this.lgProducao=lgProducao;
	}
	public int getLgProducao(){
		return this.lgProducao;
	}
	public void setIdReferencia(String idReferencia){
		this.idReferencia=idReferencia;
	}
	public String getIdReferencia(){
		return this.idReferencia;
	}
	public void setCdLocalArmazenamento(int cdLocalArmazenamento){
		this.cdLocalArmazenamento = cdLocalArmazenamento;
	}

	public int getCdLocalArmazenamento(){
		return this.cdLocalArmazenamento;
	}
	
	public void setTpReferencia(String tpReferencia){
		this.tpReferencia = tpReferencia;
	}
	
	public String getTpReferencia(){
		return this.tpReferencia;
	}
	
	public void setTxtVersao(String txtVersao){
		this.txtVersao = txtVersao;
	}
	
	public String getTxtVersao(){
		return this.txtVersao;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdReferencia: " +  getCdReferencia();
		valueToString += ", cdBem: " +  getCdBem();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdMarca: " +  getCdMarca();
		valueToString += ", dtAquisicao: " +  sol.util.Util.formatDateTime(getDtAquisicao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtGarantia: " +  sol.util.Util.formatDateTime(getDtGarantia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtValidade: " +  sol.util.Util.formatDateTime(getDtValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtBaixa: " +  sol.util.Util.formatDateTime(getDtBaixa(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrSerie: " +  getNrSerie();
		valueToString += ", nrTombo: " +  getNrTombo();
		valueToString += ", stReferencia: " +  getStReferencia();
		valueToString += ", nmModelo: " +  getNmModelo();
		valueToString += ", dtIncorporacao: " +  sol.util.Util.formatDateTime(getDtIncorporacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", qtCapacidade: " +  getQtCapacidade();
		valueToString += ", lgProducao: " +  getLgProducao();
		valueToString += ", idReferencia: " +  getIdReferencia();
		valueToString += ", cdLocalArmazenamento: " +  getCdLocalArmazenamento();
		valueToString += ", tpReferencia: " +  getTpReferencia();
		valueToString += ", txtVersao: " +  getTxtVersao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Referencia(getCdReferencia(),
			getCdBem(),
			getCdSetor(),
			getCdDocumentoEntrada(),
			getCdEmpresa(),
			getCdMarca(),
			getDtAquisicao()==null ? null : (GregorianCalendar)getDtAquisicao().clone(),
			getDtGarantia()==null ? null : (GregorianCalendar)getDtGarantia().clone(),
			getDtValidade()==null ? null : (GregorianCalendar)getDtValidade().clone(),
			getDtBaixa()==null ? null : (GregorianCalendar)getDtBaixa().clone(),
			getNrSerie(),
			getNrTombo(),
			getStReferencia(),
			getNmModelo(),
			getDtIncorporacao()==null ? null : (GregorianCalendar)getDtIncorporacao().clone(),
			getQtCapacidade(),
			getLgProducao(),
			getIdReferencia(),
			getCdLocalArmazenamento(),
			getTpReferencia(),
			getTxtVersao());
	}

}
