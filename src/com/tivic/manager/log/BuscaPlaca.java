package com.tivic.manager.log;

import java.util.GregorianCalendar;

public class BuscaPlaca {

	private int cdLog;
	private int tpDestino;
	private GregorianCalendar dtLog;
	private String nrPlaca;
	private String urlRequisicao;
	private String txtRequisicao;
	private String txtResposta;
	private String idOrgao;
	private int cdUsuario;
	private int tpLog;

	public BuscaPlaca() { }

	public BuscaPlaca(int cdLog,
			int tpDestino,
			GregorianCalendar dtLog,
			String nrPlaca,
			String urlRequisicao,
			String txtRequisicao,
			String txtResposta,
			String idOrgao,
			int cdUsuario,
			int tpLog) {
		setCdLog(cdLog);
		setTpDestino(tpDestino);
		setDtLog(dtLog);
		setNrPlaca(nrPlaca);
		setUrlRequisicao(urlRequisicao);
		setTxtRequisicao(txtRequisicao);
		setTxtResposta(txtResposta);
		setIdOrgao(idOrgao);
		setCdUsuario(cdUsuario);
		setTpLog(tpLog);
	}
	public void setCdLog(int cdLog){
		this.cdLog=cdLog;
	}
	public int getCdLog(){
		return this.cdLog;
	}
	public void setTpDestino(int tpDestino){
		this.tpDestino=tpDestino;
	}
	public int getTpDestino(){
		return this.tpDestino;
	}
	public void setDtLog(GregorianCalendar dtLog){
		this.dtLog=dtLog;
	}
	public GregorianCalendar getDtLog(){
		return this.dtLog;
	}
	public void setNrPlaca(String nrPlaca){
		this.nrPlaca=nrPlaca;
	}
	public String getNrPlaca(){
		return this.nrPlaca;
	}
	public void setUrlRequisicao(String urlRequisicao){
		this.urlRequisicao=urlRequisicao;
	}
	public String getUrlRequisicao(){
		return this.urlRequisicao;
	}
	public void setTxtRequisicao(String txtRequisicao){
		this.txtRequisicao=txtRequisicao;
	}
	public String getTxtRequisicao(){
		return this.txtRequisicao;
	}
	public void setTxtResposta(String txtResposta){
		this.txtResposta=txtResposta;
	}
	public String getTxtResposta(){
		return this.txtResposta;
	}
	public void setIdOrgao(String idOrgao){
		this.idOrgao=idOrgao;
	}
	public String getIdOrgao(){
		return this.idOrgao;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setTpLog(int tpLog){
		this.tpLog=tpLog;
	}
	public int getTpLog(){
		return this.tpLog;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLog: " +  getCdLog();
		valueToString += ", tpDestino: " +  getTpDestino();
		valueToString += ", dtLog: " +  sol.util.Util.formatDateTime(getDtLog(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrPlaca: " +  getNrPlaca();
		valueToString += ", urlRequisicao: " +  getUrlRequisicao();
		valueToString += ", txtRequisicao: " +  getTxtRequisicao();
		valueToString += ", txtResposta: " +  getTxtResposta();
		valueToString += ", idOrgao: " +  getIdOrgao();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", tpLog: " +  getTpLog();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BuscaPlaca(getCdLog(),
			getTpDestino(),
			getDtLog()==null ? null : (GregorianCalendar)getDtLog().clone(),
			getNrPlaca(),
			getUrlRequisicao(),
			getTxtRequisicao(),
			getTxtResposta(),
			getIdOrgao(),
			getCdUsuario(),
			getTpLog());
	}

}