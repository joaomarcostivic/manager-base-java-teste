package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class ArquivoEdi extends Arquivo {

	private int nrRemessa;
	private GregorianCalendar dtCriacao;
	private int cdConta;
	private int stArquivo;

	public ArquivoEdi(int cdArquivo,
			String nmArquivo,
			GregorianCalendar dtArquivamento,
			String nmDocumento,
			byte[] blbArquivo,
			int cdTipoArquivo,
			int nrRemessa,
			GregorianCalendar dtCriacao,
			int cdConta,
			int stArquivo){
		super(cdArquivo,
			nmArquivo,
			dtArquivamento,
			nmDocumento,
			blbArquivo,
			cdTipoArquivo,
			null/*dtCriacao*/, 0/*cdUsuario*/,0/*stArquivo*/, null /*dtInicial*/, null/*dtFinal*/, 0/*cdAssinatura*/, null/*txtOcr*/, 0, null, null);
		setNrRemessa(nrRemessa);
		setDtCriacao(dtCriacao);
		setCdConta(cdConta);
		setStArquivo(stArquivo);
	}
	public void setNrRemessa(int nrRemessa){
		this.nrRemessa=nrRemessa;
	}
	public int getNrRemessa(){
		return this.nrRemessa;
	}
	public void setDtCriacao(GregorianCalendar dtCriacao){
		this.dtCriacao=dtCriacao;
	}
	public GregorianCalendar getDtCriacao(){
		return this.dtCriacao;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setStArquivo(int stArquivo){
		this.stArquivo=stArquivo;
	}
	public int getStArquivo(){
		return this.stArquivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdArquivo: " +  getCdArquivo();
		valueToString += ", nrRemessa: " +  getNrRemessa();
		valueToString += ", dtCriacao: " +  sol.util.Util.formatDateTime(getDtCriacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", stArquivo: " +  getStArquivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ArquivoEdi(getCdArquivo(),
			getNmArquivo(),
			getDtArquivamento(),
			getNmDocumento(),
			getBlbArquivo(),
			getCdTipoArquivo(),
			getNrRemessa(),
			getDtCriacao()==null ? null : (GregorianCalendar)getDtCriacao().clone(),
			getCdConta(),
			getStArquivo());
	}

}