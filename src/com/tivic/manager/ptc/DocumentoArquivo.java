package com.tivic.manager.ptc;

import java.util.GregorianCalendar;

public class DocumentoArquivo {

	private int cdArquivo;
	private int cdDocumento;
	private GregorianCalendar dtArquivamento;
	private String nmArquivo;
	private String nmDocumento;
	private int lgComprimido;
	private int cdTipoDocumento;
	private int stArquivo;
	private String idRepositorio;
	private int cdAssinatura;
	
	public DocumentoArquivo() { }

	public DocumentoArquivo(int cdArquivo,
			int cdDocumento,
			String nmArquivo,
			String nmDocumento,
			GregorianCalendar dtArquivamento,
			byte[] blbArquivo,
			int lgComprimido,
			int cdTipoDocumento,
			int stArquivo,
			String idRepositorio,
			int cdAssinatura,
			String txtOcr){
			
		setCdArquivo(cdArquivo);
		setCdDocumento(cdDocumento);
	}
	
	public int getStArquivo() {
		return stArquivo;
	}

	public void setStArquivo(int stArquivo) {
		this.stArquivo = stArquivo;
	}

	public String getIdRepositorio() {
		return idRepositorio;
	}

	public void setIdRepositorio(String idRepositorio) {
		this.idRepositorio = idRepositorio;
	}

	public int getCdAssinatura() {
		return cdAssinatura;
	}

	public void setCdAssinatura(int cdAssinatura) {
		this.cdAssinatura = cdAssinatura;
	}

	public String getNmArquivo() {
		return nmArquivo;
	}

	public void setNmArquivo(String nmArquivo) {
		this.nmArquivo = nmArquivo;
	}

	public String getNmDocumento() {
		return nmDocumento;
	}

	public void setNmDocumento(String nmDocumento) {
		this.nmDocumento = nmDocumento;
	}

	public int getLgComprimido() {
		return lgComprimido;
	}

	public void setLgComprimido(int lgComprimido) {
		this.lgComprimido = lgComprimido;
	}

	public int getCdTipoDocumento() {
		return cdTipoDocumento;
	}

	public void setCdTipoDocumento(int cdTipoDocumento) {
		this.cdTipoDocumento = cdTipoDocumento;
	}

	public GregorianCalendar getDtArquivamento() {
		return dtArquivamento;
	}

	public void setDtArquivamento(GregorianCalendar dtArquivamento) {
		this.dtArquivamento = dtArquivamento;
	}

	public DocumentoArquivo(int cdArquivo,
			int cdDocumento) {
		setCdArquivo(cdArquivo);
		setCdDocumento(cdDocumento);
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdArquivo: " +  getCdArquivo();
		valueToString += ", cdDocumento: " +  getCdDocumento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DocumentoArquivo(getCdArquivo(),
			getCdDocumento());
	}

	public void setBlbArquivo(byte[] blbArquivo) {
		this.setBlbArquivo(blbArquivo);
	}
	
	public byte[] getBlbArquivo() {
		return getBlbArquivo();
	}

	public void setTxtOcr(String string) {
		this.setTxtOcr(string);
	}

	public String getTxtOcr() {
		return this.getTxtOcr();
	}

}