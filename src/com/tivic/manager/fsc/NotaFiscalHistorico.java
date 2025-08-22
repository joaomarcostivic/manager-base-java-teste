package com.tivic.manager.fsc;

import java.util.GregorianCalendar;

public class NotaFiscalHistorico {

	private int cdNotaFiscal;
	private int cdHistorico;
	private int tpAmbiente;
	private String nrVersao;
	private GregorianCalendar dtEnvio;
	private GregorianCalendar dtRecebimento;
	private String nrChave;
	private String nrProtocolo;
	private String nrDigito;
	private String tpStatus;
	private String dsMensagem;
	private String idTransacao;
	private String txtXml;
	private int tpHistorico;
	
	public NotaFiscalHistorico(int cdNotaFiscal,
			int cdHistorico,
			int tpAmbiente,
			String nrVersao,
			GregorianCalendar dtEnvio,
			GregorianCalendar dtRecebimento,
			String nrChave,
			String nrProtocolo,
			String nrDigito,
			String tpStatus,
			String dsMensagem,
			String idTransacao,
			String txtXml,
			int tpHistorico){
		setCdNotaFiscal(cdNotaFiscal);
		setCdHistorico(cdHistorico);
		setTpAmbiente(tpAmbiente);
		setNrVersao(nrVersao);
		setDtEnvio(dtEnvio);
		setDtRecebimento(dtRecebimento);
		setNrChave(nrChave);
		setNrProtocolo(nrProtocolo);
		setNrDigito(nrDigito);
		setTpStatus(tpStatus);
		setDsMensagem(dsMensagem);
		setIdTransacao(idTransacao);
		setTxtXml(txtXml);
		setTpHistorico(tpHistorico);
	}
	public void setCdNotaFiscal(int cdNotaFiscal){
		this.cdNotaFiscal=cdNotaFiscal;
	}
	public int getCdNotaFiscal(){
		return this.cdNotaFiscal;
	}
	public void setCdHistorico(int cdHistorico){
		this.cdHistorico=cdHistorico;
	}
	public int getCdHistorico(){
		return this.cdHistorico;
	}
	public void setTpAmbiente(int tpAmbiente){
		this.tpAmbiente=tpAmbiente;
	}
	public int getTpAmbiente(){
		return this.tpAmbiente;
	}
	public void setNrVersao(String nrVersao){
		this.nrVersao=nrVersao;
	}
	public String getNrVersao(){
		return this.nrVersao;
	}
	public void setDtEnvio(GregorianCalendar dtEnvio){
		this.dtEnvio=dtEnvio;
	}
	public GregorianCalendar getDtEnvio(){
		return this.dtEnvio;
	}
	public void setDtRecebimento(GregorianCalendar dtRecebimento){
		this.dtRecebimento=dtRecebimento;
	}
	public GregorianCalendar getDtRecebimento(){
		return this.dtRecebimento;
	}
	public void setNrChave(String nrChave){
		this.nrChave=nrChave;
	}
	public String getNrChave(){
		return this.nrChave;
	}
	public void setNrProtocolo(String nrProtocolo){
		this.nrProtocolo=nrProtocolo;
	}
	public String getNrProtocolo(){
		return this.nrProtocolo;
	}
	public void setNrDigito(String nrDigito){
		this.nrDigito=nrDigito;
	}
	public String getNrDigito(){
		return this.nrDigito;
	}
	public void setTpStatus(String tpStatus){
		this.tpStatus=tpStatus;
	}
	public String getTpStatus(){
		return this.tpStatus;
	}
	public void setDsMensagem(String dsMensagem){
		this.dsMensagem=dsMensagem;
	}
	public String getDsMensagem(){
		return this.dsMensagem;
	}
	public void setIdTransacao(String idTransacao){
		this.idTransacao=idTransacao;
	}
	public String getIdTransacao(){
		return this.idTransacao;
	}
	public void setTxtXml(String txtXml){
		this.txtXml=txtXml;
	}
	public String getTxtXml(){
		return this.txtXml;
	}
	public void setTpHistorico(int tpHistorico) {
		this.tpHistorico = tpHistorico;
	}
	public int getTpHistorico() {
		return tpHistorico;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNotaFiscal: " +  getCdNotaFiscal();
		valueToString += ", cdHistorico: " +  getCdHistorico();
		valueToString += ", tpAmbiente: " +  getTpAmbiente();
		valueToString += ", nrVersao: " +  getNrVersao();
		valueToString += ", dtEnvio: " +  sol.util.Util.formatDateTime(getDtEnvio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtRecebimento: " +  sol.util.Util.formatDateTime(getDtRecebimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrChave: " +  getNrChave();
		valueToString += ", nrProtocolo: " +  getNrProtocolo();
		valueToString += ", nrDigito: " +  getNrDigito();
		valueToString += ", tpStatus: " +  getTpStatus();
		valueToString += ", dsMensagem: " +  getDsMensagem();
		valueToString += ", idTransacao: " +  getIdTransacao();
		valueToString += ", txtXml: " +  getTxtXml();
		valueToString += ", tpHistorico: " +  getTpHistorico();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new NotaFiscalHistorico(getCdNotaFiscal(),
			getCdHistorico(),
			getTpAmbiente(),
			getNrVersao(),
			getDtEnvio()==null ? null : (GregorianCalendar)getDtEnvio().clone(),
			getDtRecebimento()==null ? null : (GregorianCalendar)getDtRecebimento().clone(),
			getNrChave(),
			getNrProtocolo(),
			getNrDigito(),
			getTpStatus(),
			getDsMensagem(),
			getIdTransacao(),
			getTxtXml(),
			getTpHistorico());
	}

}