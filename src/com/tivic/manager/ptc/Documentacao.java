package com.tivic.manager.ptc;

import java.util.GregorianCalendar;

public class Documentacao {

	private int cdDocumentacao;
	private int cdDocumento;
	private int cdArquivo;
	private GregorianCalendar dtRecepcao;
	private GregorianCalendar dtDevolucao;
	private int lgOriginal;
	private int stDocumento;
	private GregorianCalendar dtValidade;
	private int stAnexo;
	private int cdTipoDocumentacao;
	private String txtDocumentacao;
	
	public Documentacao() { }
	
	public Documentacao(int cdDocumentacao,
			int cdDocumento,
			int cdArquivo,
			GregorianCalendar dtRecepcao,
			GregorianCalendar dtDevolucao,
			int lgOriginal,
			int stDocumento,
			GregorianCalendar dtValidade,
			int stAnexo,
			int cdTipoDocumentacao,
			String txtDocumentacao){
		setCdDocumentacao(cdDocumentacao);
		setCdDocumento(cdDocumento);
		setCdArquivo(cdArquivo);
		setDtRecepcao(dtRecepcao);
		setDtDevolucao(dtDevolucao);
		setLgOriginal(lgOriginal);
		setStDocumento(stDocumento);
		setDtValidade(dtValidade);
		setStAnexo(stAnexo);
		setCdTipoDocumentacao(cdTipoDocumentacao);
		setTxtDocumentacao(txtDocumentacao);
	}
	public void setCdDocumentacao(int cdDocumentacao){
		this.cdDocumentacao=cdDocumentacao;
	}
	public int getCdDocumentacao(){
		return this.cdDocumentacao;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setDtRecepcao(GregorianCalendar dtRecepcao){
		this.dtRecepcao=dtRecepcao;
	}
	public GregorianCalendar getDtRecepcao(){
		return this.dtRecepcao;
	}
	public void setDtDevolucao(GregorianCalendar dtDevolucao){
		this.dtDevolucao=dtDevolucao;
	}
	public GregorianCalendar getDtDevolucao(){
		return this.dtDevolucao;
	}
	public void setLgOriginal(int lgOriginal){
		this.lgOriginal=lgOriginal;
	}
	public int getLgOriginal(){
		return this.lgOriginal;
	}
	public void setStDocumento(int stDocumento){
		this.stDocumento=stDocumento;
	}
	public int getStDocumento(){
		return this.stDocumento;
	}
	public void setDtValidade(GregorianCalendar dtValidade){
		this.dtValidade=dtValidade;
	}
	public GregorianCalendar getDtValidade(){
		return this.dtValidade;
	}
	public void setStAnexo(int stAnexo) {
		this.stAnexo = stAnexo;
	}
	public int getStAnexo() {
		return stAnexo;
	}
	public void setCdTipoDocumentacao(int cdTipoDocumentacao){
		this.cdTipoDocumentacao=cdTipoDocumentacao;
	}
	public int getCdTipoDocumentacao(){
		return this.cdTipoDocumentacao;
	}
	public void setTxtDocumentacao(String txtDocumentacao){
		this.txtDocumentacao=txtDocumentacao;
	}
	public String getTxtDocumentacao(){
		return this.txtDocumentacao;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocumentacao: " +  getCdDocumentacao();
		valueToString += ", cdDocumento: " +  getCdDocumento();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		valueToString += ", dtRecepcao: " +  sol.util.Util.formatDateTime(getDtRecepcao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtDevolucao: " +  sol.util.Util.formatDateTime(getDtDevolucao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgOriginal: " +  getLgOriginal();
		valueToString += ", stDocumento: " +  getStDocumento();
		valueToString += ", dtValidade: " +  sol.util.Util.formatDateTime(getDtValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stAnexo: " +  getStAnexo();
		valueToString += ", cdTipoDocumentacao: " +  getCdTipoDocumentacao();
		valueToString += ", txtDocumentacao: " +  getTxtDocumentacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Documentacao(getCdDocumentacao(),
			getCdDocumento(),
			getCdArquivo(),
			getDtRecepcao()==null ? null : (GregorianCalendar)getDtRecepcao().clone(),
			getDtDevolucao()==null ? null : (GregorianCalendar)getDtDevolucao().clone(),
			getLgOriginal(),
			getStDocumento(),
			getDtValidade()==null ? null : (GregorianCalendar)getDtValidade().clone(),
			getStAnexo(),
			getCdTipoDocumentacao(),
			getTxtDocumentacao());
	}

}