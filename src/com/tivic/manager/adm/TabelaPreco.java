package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class TabelaPreco {

	private int cdTabelaPreco;
	private int cdEmpresa;
	private int cdMoeda;
	private int nrVersao;
	private String nmTabelaPreco;
	private GregorianCalendar dtInicioValidade;
	private GregorianCalendar dtFinalValidade;
	private int lgAtivo;
	private String idTabelaPreco;
	private byte[] imgTabelaPreco;
	private int tpAplicacaoRegras;
	private int gnTabelaPreco;
	private int stTabelaPreco;
	private int lgPadrao;
	private int lgPrecoVenda;
	private int lgImpostoIncluso;
	private int nrPrecisaoPreco;
	private String txtDescricao;

	public TabelaPreco(int cdTabelaPreco,
			int cdEmpresa,
			int cdMoeda,
			int nrVersao,
			String nmTabelaPreco,
			GregorianCalendar dtInicioValidade,
			GregorianCalendar dtFinalValidade,
			int lgAtivo,
			String idTabelaPreco,
			byte[] imgTabelaPreco,
			int tpAplicacaoRegras,
			int gnTabelaPreco){
		setCdTabelaPreco(cdTabelaPreco);
		setCdEmpresa(cdEmpresa);
		setCdMoeda(cdMoeda);
		setNrVersao(nrVersao);
		setNmTabelaPreco(nmTabelaPreco);
		setDtInicioValidade(dtInicioValidade);
		setDtFinalValidade(dtFinalValidade);
		setLgAtivo(lgAtivo);
		setIdTabelaPreco(idTabelaPreco);
		setImgTabelaPreco(imgTabelaPreco);
		setTpAplicacaoRegras(tpAplicacaoRegras);
		setGnTabelaPreco(gnTabelaPreco);
	}

	public TabelaPreco(int cdTabelaPreco,
			int cdEmpresa,
			int cdMoeda,
			int nrVersao,
			String nmTabelaPreco,
			GregorianCalendar dtInicioValidade,
			GregorianCalendar dtFinalValidade,
			int lgAtivo,
			String idTabelaPreco,
			byte[] imgTabelaPreco,
			int tpAplicacaoRegras,
			int gnTabelaPreco,
			int stTabelaPreco,
			int lgPadrao,
			int lgPrecoVenda,
			int lgImpostoIncluso,
			int nrPrecisaoPreco,
			String txtDescricao){
		setCdTabelaPreco(cdTabelaPreco);
		setCdEmpresa(cdEmpresa);
		setCdMoeda(cdMoeda);
		setNrVersao(nrVersao);
		setNmTabelaPreco(nmTabelaPreco);
		setDtInicioValidade(dtInicioValidade);
		setDtFinalValidade(dtFinalValidade);
		setLgAtivo(lgAtivo);
		setIdTabelaPreco(idTabelaPreco);
		setImgTabelaPreco(imgTabelaPreco);
		setTpAplicacaoRegras(tpAplicacaoRegras);
		setGnTabelaPreco(gnTabelaPreco);
		setStTabelaPreco(stTabelaPreco);
		setLgPadrao(lgPadrao);
		setLgPrecoVenda(lgPrecoVenda);
		setLgImpostoIncluso(lgImpostoIncluso);
		setNrPrecisaoPreco(nrPrecisaoPreco);
		setTxtDescricao(txtDescricao);
	}
	public void setCdTabelaPreco(int cdTabelaPreco){
		this.cdTabelaPreco=cdTabelaPreco;
	}
	public int getCdTabelaPreco(){
		return this.cdTabelaPreco;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdMoeda(int cdMoeda){
		this.cdMoeda=cdMoeda;
	}
	public int getCdMoeda(){
		return this.cdMoeda;
	}
	public void setNrVersao(int nrVersao){
		this.nrVersao=nrVersao;
	}
	public int getNrVersao(){
		return this.nrVersao;
	}
	public void setNmTabelaPreco(String nmTabelaPreco){
		this.nmTabelaPreco=nmTabelaPreco;
	}
	public String getNmTabelaPreco(){
		return this.nmTabelaPreco;
	}
	public void setDtInicioValidade(GregorianCalendar dtInicioValidade){
		this.dtInicioValidade=dtInicioValidade;
	}
	public GregorianCalendar getDtInicioValidade(){
		return this.dtInicioValidade;
	}
	public void setDtFinalValidade(GregorianCalendar dtFinalValidade){
		this.dtFinalValidade=dtFinalValidade;
	}
	public GregorianCalendar getDtFinalValidade(){
		return this.dtFinalValidade;
	}
	public void setLgAtivo(int lgAtivo){
		this.lgAtivo=lgAtivo;
	}
	public int getLgAtivo(){
		return this.lgAtivo;
	}
	public void setIdTabelaPreco(String idTabelaPreco){
		this.idTabelaPreco=idTabelaPreco;
	}
	public String getIdTabelaPreco(){
		return this.idTabelaPreco;
	}
	public void setImgTabelaPreco(byte[] imgTabelaPreco){
		this.imgTabelaPreco=imgTabelaPreco;
	}
	public byte[] getImgTabelaPreco(){
		return this.imgTabelaPreco;
	}
	public void setTpAplicacaoRegras(int tpAplicacaoRegras){
		this.tpAplicacaoRegras=tpAplicacaoRegras;
	}
	public int getTpAplicacaoRegras(){
		return this.tpAplicacaoRegras;
	}
	public void setGnTabelaPreco(int gnTabelaPreco){
		this.gnTabelaPreco=gnTabelaPreco;
	}
	public int getGnTabelaPreco(){
		return this.gnTabelaPreco;
	}
	public void setStTabelaPreco(int stTabelaPreco){
		this.stTabelaPreco=stTabelaPreco;
	}
	public int getStTabelaPreco(){
		return this.stTabelaPreco;
	}
	public void setLgPadrao(int lgPadrao){
		this.lgPadrao=lgPadrao;
	}
	public int getLgPadrao(){
		return this.lgPadrao;
	}
	public void setLgPrecoVenda(int lgPrecoVenda){
		this.lgPrecoVenda=lgPrecoVenda;
	}
	public int getLgPrecoVenda(){
		return this.lgPrecoVenda;
	}
	public void setLgImpostoIncluso(int lgImpostoIncluso){
		this.lgImpostoIncluso=lgImpostoIncluso;
	}
	public int getLgImpostoIncluso(){
		return this.lgImpostoIncluso;
	}
	public void setNrPrecisaoPreco(int nrPrecisaoPreco){
		this.nrPrecisaoPreco=nrPrecisaoPreco;
	}
	public int getNrPrecisaoPreco(){
		return this.nrPrecisaoPreco;
	}
	public void setTxtDescricao(String txtDescricao) {
		this.txtDescricao = txtDescricao;
	}
	public String getTxtDescricao() {
		return txtDescricao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabelaPreco: " +  getCdTabelaPreco();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdMoeda: " +  getCdMoeda();
		valueToString += ", nrVersao: " +  getNrVersao();
		valueToString += ", nmTabelaPreco: " +  getNmTabelaPreco();
		valueToString += ", dtInicioValidade: " +  sol.util.Util.formatDateTime(getDtInicioValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinalValidade: " +  sol.util.Util.formatDateTime(getDtFinalValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgAtivo: " +  getLgAtivo();
		valueToString += ", idTabelaPreco: " +  getIdTabelaPreco();
		valueToString += ", imgTabelaPreco: " +  getImgTabelaPreco();
		valueToString += ", tpAplicacaoRegras: " +  getTpAplicacaoRegras();
		valueToString += ", gnTabelaPreco: " +  getGnTabelaPreco();
		valueToString += ", stTabelaPreco: " +  getStTabelaPreco();
		valueToString += ", lgPadrao: " +  getLgPadrao();
		valueToString += ", lgPrecoVenda: " +  getLgPrecoVenda();
		valueToString += ", lgImpostoIncluso: " +  getLgImpostoIncluso();
		valueToString += ", nrPrecisaoPreco: " +  getNrPrecisaoPreco();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TabelaPreco(getCdTabelaPreco(),
			getCdEmpresa(),
			getCdMoeda(),
			getNrVersao(),
			getNmTabelaPreco(),
			getDtInicioValidade()==null ? null : (GregorianCalendar)getDtInicioValidade().clone(),
			getDtFinalValidade()==null ? null : (GregorianCalendar)getDtFinalValidade().clone(),
			getLgAtivo(),
			getIdTabelaPreco(),
			getImgTabelaPreco(),
			getTpAplicacaoRegras(),
			getGnTabelaPreco(),
			getStTabelaPreco(),
			getLgPadrao(),
			getLgPrecoVenda(),
			getLgImpostoIncluso(),
			getNrPrecisaoPreco(),
			getTxtDescricao());
	}

}