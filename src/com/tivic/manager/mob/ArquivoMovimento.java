package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class ArquivoMovimento {

	private int cdArquivoMovimento;
	private int cdMovimento;
	private int cdAit;
	private int tpArquivo;
	private int nrRemessa;
	private int nrSequencial;
	private int tpStatus;
	private String nrErro;
	private String dsEntrada;
	private String dsSaida;
	private int tpOrigem;
	private GregorianCalendar dtArquivo;
	private String dsSugestaoCorrecao;
	
	public ArquivoMovimento() {
		// TODO Auto-generated constructor stub
	}

	public ArquivoMovimento(int cdArquivoMovimento, int cdMovimento, int cdAit, int tpArquivo, int nrRemessa,
			int nrSequencial, int tpStatus, String nrErro, String dsEntrada, String dsSaida, int tpOrigem, GregorianCalendar dtArquivo) {
		super();
		this.cdArquivoMovimento = cdArquivoMovimento;
		this.cdMovimento = cdMovimento;
		this.cdAit = cdAit;
		this.tpArquivo = tpArquivo;
		this.nrRemessa = nrRemessa;
		this.nrSequencial = nrSequencial;
		this.tpStatus = tpStatus;
		this.nrErro = nrErro;
		this.dsEntrada = dsEntrada;
		this.dsSaida = dsSaida;
		this.tpOrigem = tpOrigem;
		this.dtArquivo = dtArquivo;
	}

	public int getCdArquivoMovimento() {
		return cdArquivoMovimento;
	}

	public void setCdArquivoMovimento(int cdArquivoMovimento) {
		this.cdArquivoMovimento = cdArquivoMovimento;
	}

	public int getCdMovimento() {
		return cdMovimento;
	}

	public void setCdMovimento(int cdMovimento) {
		this.cdMovimento = cdMovimento;
	}

	public int getCdAit() {
		return cdAit;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}

	public int getTpArquivo() {
		return tpArquivo;
	}

	public void setTpArquivo(int tpArquivo) {
		this.tpArquivo = tpArquivo;
	}

	public int getNrRemessa() {
		return nrRemessa;
	}

	public void setNrRemessa(int nrRemessa) {
		this.nrRemessa = nrRemessa;
	}

	public int getNrSequencial() {
		return nrSequencial;
	}

	public void setNrSequencial(int nrSequencial) {
		this.nrSequencial = nrSequencial;
	}

	public int getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}

	public String getNrErro() {
		return nrErro;
	}

	public void setNrErro(String nrErro) {
		this.nrErro = nrErro;
	}

	public String getDsEntrada() {
		return dsEntrada;
	}

	public void setDsEntrada(String dsEntrada) {
		this.dsEntrada = dsEntrada;
	}

	public String getDsSaida() {
		return dsSaida;
	}

	public void setDsSaida(String dsSaida) {
		this.dsSaida = dsSaida;
	}

	public int getTpOrigem() {
		return tpOrigem;
	}

	public void setTpOrigem(int tpOrigem) {
		this.tpOrigem = tpOrigem;
	}
	
	public GregorianCalendar getDtArquivo() {
		return dtArquivo;
	}
	
	public void setDtArquivo(GregorianCalendar dtArquivo) {
		this.dtArquivo = dtArquivo;
	}
	
	public String getDsSugestaoCorrecao() {
		return dsSugestaoCorrecao;
	}

	public void setDsSugestaoCorrecao(String dsSugestaoCorrecao) {
		this.dsSugestaoCorrecao = dsSugestaoCorrecao;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdArquivoMovimento: " +  getCdMovimento();
		valueToString += ", cdMovimento: " +  getCdMovimento();
		valueToString += ", cdAit: " +  getCdAit();
		valueToString += ", tpArquivo: " +  getTpArquivo();
		valueToString += ", nrRemessa: " +  getNrRemessa();
		valueToString += ", nrSequencial: " +  getNrSequencial();
		valueToString += ", tpStatus: " +  getTpStatus();
		valueToString += ", nrErro: " +  getNrErro();
		valueToString += ", dsEntrada: " +  getDsEntrada();
		valueToString += ", dsSaida: " +  getDsSaida();
		valueToString += ", tpOrigem: " +  getTpOrigem();
		valueToString += ", dtArquivo: " +  sol.util.Util.formatDateTime(getDtArquivo(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dsSugestaoCorrecao: " +  getDsSugestaoCorrecao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ArquivoMovimento(getCdArquivoMovimento(),
			getCdMovimento(),
			getCdAit(),
			getTpArquivo(),
			getNrRemessa(),
			getNrSequencial(),
			getTpStatus(),
			getNrErro(),
			getDsEntrada(),
			getDsSaida(),
			getTpOrigem(),
			getDtArquivo()==null ? null : (GregorianCalendar)getDtArquivo().clone());
	}
}
