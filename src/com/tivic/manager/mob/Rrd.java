package com.tivic.manager.mob;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Rrd {

	private int cdRrd;
	private int nrRrd;
	private GregorianCalendar dtOcorrencia;
	private int cdUsuario;
	private int cdAgente;
	private String dsObservacao;
	private String dsLocalOcorrencia;
	private String dsPontoReferencia;
	private Double vlLatitude;
	private Double vlLongitude;
	private int cdCidade;
	private GregorianCalendar dtRegularizar;
	private int cdRrdOrgao;

	private ArrayList<RecolhimentoDocumentacao> documentacao;
	private ArrayList<Exame> exame;
	private ArrayList<RrdAit> aitvinculadas;

	public Rrd() { }

	public Rrd(int cdRrd,
			int nrRrd,
			GregorianCalendar dtOcorrencia,
			int cdUsuario,
			int cdAgente,
			String dsObservacao,
			String dsLocalOcorrencia,
			String dsPontoReferencia,
			Double vlLatitude,
			Double vlLongitude,
			int cdCidade,
			GregorianCalendar dtRegularizar,
			int cdRrdOrgao,
			
			ArrayList<RecolhimentoDocumentacao> documentacao,
			ArrayList<Exame> exame,
			ArrayList<RrdAit> aitvinculadas) {
		setCdRrd(cdRrd);
		setNrRrd(nrRrd);
		setDtOcorrencia(dtOcorrencia);
		setCdUsuario(cdUsuario);
		setCdAgente(cdAgente);
		setDsObservacao(dsObservacao);
		setDsLocalOcorrencia(dsLocalOcorrencia);
		setDsPontoReferencia(dsPontoReferencia);
		setVlLatitude(vlLatitude);
		setVlLongitude(vlLongitude);
		setCdCidade(cdCidade);
		setDtRegularizar(dtRegularizar);
		setCdRrdOrgao(cdRrdOrgao);
		setDocumentacao(documentacao);
		setExame(exame);
		setAitvinculadas(aitvinculadas);
	}
	public ArrayList<RecolhimentoDocumentacao> getDocumentacao() {
		return documentacao;
	}
	public void setDocumentacao(ArrayList<RecolhimentoDocumentacao> documentacao) {
		this.documentacao = documentacao;
	}
	public ArrayList<Exame> getExame() {
		return exame;
	}
	public void setExame(ArrayList<Exame> exame) {
		this.exame = exame;
	}
	public ArrayList<RrdAit> getAitvinculadas() {
		return aitvinculadas;
	}
	public void setAitvinculadas(ArrayList<RrdAit> aitvinculadas) {
		this.aitvinculadas = aitvinculadas;
	}	
	public void setCdRrd(int cdRrd){
		this.cdRrd=cdRrd;
	}
	public int getCdRrd(){
		return this.cdRrd;
	}
	public void setNrRrd(int nrRrd){
		this.nrRrd=nrRrd;
	}
	public int getNrRrd(){
		return this.nrRrd;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia){
		this.dtOcorrencia=dtOcorrencia;
	}
	public GregorianCalendar getDtOcorrencia(){
		return this.dtOcorrencia;
	}	
	public void setDtRegularizar(GregorianCalendar dtRegularizar){
		this.dtRegularizar=dtRegularizar;
	}
	public GregorianCalendar getDtRegularizar(){
		return this.dtRegularizar;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}	
	public int getCdAgente(){
		return this.cdAgente;
	}	
	public void setCdRrdOrgao(int cdRrdOrgao){
		this.cdRrdOrgao=cdRrdOrgao;
	}		
	public int getCdRrdOrgao(){
		return this.cdRrdOrgao;
	}
	public void setDsObservacao(String dsObservacao){
		this.dsObservacao=dsObservacao;
	}
	public String getDsObservacao(){
		return this.dsObservacao;
	}
	public void setDsLocalOcorrencia(String dsLocalOcorrencia){
		this.dsLocalOcorrencia=dsLocalOcorrencia;
	}
	public String getDsLocalOcorrencia(){
		return this.dsLocalOcorrencia;
	}
	public void setDsPontoReferencia(String dsPontoReferencia){
		this.dsPontoReferencia=dsPontoReferencia;
	}
	public String getDsPontoReferencia(){
		return this.dsPontoReferencia;
	}
	public void setVlLatitude(Double vlLatitude){
		this.vlLatitude=vlLatitude;
	}
	public Double getVlLatitude(){
		return this.vlLatitude;
	}
	public void setVlLongitude(Double vlLongitude){
		this.vlLongitude=vlLongitude;
	}
	public Double getVlLongitude(){
		return this.vlLongitude;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRrd: " +  getCdRrd();
		valueToString += ", nrRrd: " +  getNrRrd();
		valueToString += ", dtOcorrencia: " +  sol.util.Util.formatDateTime(getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", dsObservacao: " +  getDsObservacao();
		valueToString += ", dsLocalOcorrencia: " +  getDsLocalOcorrencia();
		valueToString += ", dsPontoReferencia: " +  getDsPontoReferencia();
		valueToString += ", vlLatitude: " +  getVlLatitude();
		valueToString += ", vlLongitude: " +  getVlLongitude();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", dtRegularizar: " +  sol.util.Util.formatDateTime(getDtRegularizar(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Rrd(getCdRrd(),
			getNrRrd(),
			getDtOcorrencia()==null ? null : (GregorianCalendar)getDtOcorrencia().clone(),
			getCdUsuario(),
			getCdAgente(),
			getDsObservacao(),
			getDsLocalOcorrencia(),
			getDsPontoReferencia(),
			getVlLatitude(),
			getVlLongitude(),
			getCdCidade(),
			getDtRegularizar(),
			getCdRrdOrgao(),
			getDocumentacao(),
			getExame(),
			getAitvinculadas());
	}

}