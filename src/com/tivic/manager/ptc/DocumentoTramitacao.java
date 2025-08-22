package com.tivic.manager.ptc;

import java.util.GregorianCalendar;

public class DocumentoTramitacao {

	private int cdTramitacao;
	private int cdDocumento;
	private int cdSetorDestino;
	private int cdUsuarioDestino;
	private int cdSetorOrigem;
	private int cdUsuarioOrigem;
	private GregorianCalendar dtTramitacao;
	private GregorianCalendar dtRecebimento;
	private String nmLocalDestino;
	private String txtTramitacao;
	private int cdSituacaoDocumento;
	private int cdFase;
	private String nmLocalOrigem;
	private String nrRemessa;

	public DocumentoTramitacao(){}
	
	public DocumentoTramitacao(int cdTramitacao,
			int cdDocumento,
			int cdSetorDestino,
			int cdUsuarioDestino,
			int cdSetorOrigem,
			int cdUsuarioOrigem,
			GregorianCalendar dtTramitacao,
			GregorianCalendar dtRecebimento,
			String nmLocalDestino,
			String txtTramitacao,
			int cdSituacaoDocumento,
			int cdFase,
			String nmLocalOrigem,
			String nrRemessa){
		setCdTramitacao(cdTramitacao);
		setCdDocumento(cdDocumento);
		setCdSetorDestino(cdSetorDestino);
		setCdUsuarioDestino(cdUsuarioDestino);
		setCdSetorOrigem(cdSetorOrigem);
		setCdUsuarioOrigem(cdUsuarioOrigem);
		setDtTramitacao(dtTramitacao);
		setDtRecebimento(dtRecebimento);
		setNmLocalDestino(nmLocalDestino);
		setTxtTramitacao(txtTramitacao);
		setCdSituacaoDocumento(cdSituacaoDocumento);
		setCdFase(cdFase);
		setNmLocalOrigem(nmLocalOrigem);
	}
	public void setCdTramitacao(int cdTramitacao){
		this.cdTramitacao=cdTramitacao;
	}
	public int getCdTramitacao(){
		return this.cdTramitacao;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setCdSetorDestino(int cdSetorDestino){
		this.cdSetorDestino=cdSetorDestino;
	}
	public int getCdSetorDestino(){
		return this.cdSetorDestino;
	}
	public void setCdUsuarioDestino(int cdUsuarioDestino){
		this.cdUsuarioDestino=cdUsuarioDestino;
	}
	public int getCdUsuarioDestino(){
		return this.cdUsuarioDestino;
	}
	public void setCdSetorOrigem(int cdSetorOrigem){
		this.cdSetorOrigem=cdSetorOrigem;
	}
	public int getCdSetorOrigem(){
		return this.cdSetorOrigem;
	}
	public void setCdUsuarioOrigem(int cdUsuarioOrigem){
		this.cdUsuarioOrigem=cdUsuarioOrigem;
	}
	public int getCdUsuarioOrigem(){
		return this.cdUsuarioOrigem;
	}
	public void setDtTramitacao(GregorianCalendar dtTramitacao){
		this.dtTramitacao=dtTramitacao;
	}
	public GregorianCalendar getDtTramitacao(){
		return this.dtTramitacao;
	}
	public void setDtRecebimento(GregorianCalendar dtRecebimento){
		this.dtRecebimento=dtRecebimento;
	}
	public GregorianCalendar getDtRecebimento(){
		return this.dtRecebimento;
	}
	public void setNmLocalDestino(String nmLocalDestino){
		this.nmLocalDestino=nmLocalDestino;
	}
	public String getNmLocalDestino(){
		return this.nmLocalDestino;
	}
	public void setTxtTramitacao(String txtTramitacao){
		this.txtTramitacao=txtTramitacao;
	}
	public String getTxtTramitacao(){
		return this.txtTramitacao;
	}
	public void setCdSituacaoDocumento(int cdSituacaoDocumento){
		this.cdSituacaoDocumento=cdSituacaoDocumento;
	}
	public int getCdSituacaoDocumento(){
		return this.cdSituacaoDocumento;
	}
	public void setCdFase(int cdFase){
		this.cdFase=cdFase;
	}
	public int getCdFase(){
		return this.cdFase;
	}
	public void setNmLocalOrigem(String nmLocalOrigem){
		this.nmLocalOrigem=nmLocalOrigem;
	}
	public String getNmLocalOrigem(){
		return this.nmLocalOrigem;
	}
	public String getNrRemessa() {
		return nrRemessa;
	}
	public void setNrRemessa(String nrRemessa) {
		this.nrRemessa = nrRemessa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTramitacao: " +  getCdTramitacao();
		valueToString += ", cdDocumento: " +  getCdDocumento();
		valueToString += ", cdSetorDestino: " +  getCdSetorDestino();
		valueToString += ", cdUsuarioDestino: " +  getCdUsuarioDestino();
		valueToString += ", cdSetorOrigem: " +  getCdSetorOrigem();
		valueToString += ", cdUsuarioOrigem: " +  getCdUsuarioOrigem();
		valueToString += ", dtTramitacao: " +  sol.util.Util.formatDateTime(getDtTramitacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtRecebimento: " +  sol.util.Util.formatDateTime(getDtRecebimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nmLocalDestino: " +  getNmLocalDestino();
		valueToString += ", txtTramitacao: " +  getTxtTramitacao();
		valueToString += ", cdSituacaoDocumento: " +  getCdSituacaoDocumento();
		valueToString += ", cdFase: " +  getCdFase();
		valueToString += ", nmLocalOrigem: " + getNmLocalOrigem();
		valueToString += ", nrRemessa: " +getNrRemessa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DocumentoTramitacao(getCdTramitacao(),
			getCdDocumento(),
			getCdSetorDestino(),
			getCdUsuarioDestino(),
			getCdSetorOrigem(),
			getCdUsuarioOrigem(),
			getDtTramitacao()==null ? null : (GregorianCalendar)getDtTramitacao().clone(),
			getDtRecebimento()==null ? null : (GregorianCalendar)getDtRecebimento().clone(),
			getNmLocalDestino(),
			getTxtTramitacao(),
			getCdSituacaoDocumento(),
			getCdFase(),
			getNmLocalOrigem(),
			getNrRemessa());
	}

}