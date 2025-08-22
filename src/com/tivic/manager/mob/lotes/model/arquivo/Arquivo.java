package com.tivic.manager.mob.lotes.model.arquivo;

import java.util.GregorianCalendar;

public class Arquivo {
	private int cdArquivo;
	private int cdUsuario;
	private byte[] blbArquivo;
	private String nmArquivo;
	private String nmDocumento;
	private GregorianCalendar dtArquivamento;
	private GregorianCalendar dtCriacao;
	
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	
	public void setNmArquivo(String nmArquivo){
		this.nmArquivo=nmArquivo;
	}
	
	public String getNmArquivo(){
		return this.nmArquivo;
	}
	
	public void setDtArquivamento(GregorianCalendar dtArquivamento){
		this.dtArquivamento=dtArquivamento;
	}
	
	public GregorianCalendar getDtArquivamento(){
		return this.dtArquivamento;
	}
	
	public void setNmDocumento(String nmDocumento){
		this.nmDocumento=nmDocumento;
	}
	
	public String getNmDocumento(){
		return this.nmDocumento;
	}
	
	public void setBlbArquivo(byte[] blbArquivo){
		this.blbArquivo=blbArquivo;
	}
	
	public byte[] getBlbArquivo(){
		return this.blbArquivo;
	}
	
	public void setDtCriacao(GregorianCalendar dtCriacao){
		this.dtCriacao=dtCriacao;
	}
	
	public GregorianCalendar getDtCriacao(){
		return this.dtCriacao;
	}
	
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	
	public com.tivic.manager.grl.Arquivo toModel() {
		com.tivic.manager.grl.Arquivo arquivo = new com.tivic.manager.grl.Arquivo();
		arquivo.setCdArquivo(this.cdArquivo);
		arquivo.setNmArquivo(this.nmArquivo);
		arquivo.setDtArquivamento(this.dtArquivamento);
		arquivo.setNmDocumento(this.nmDocumento);
		arquivo.setBlbArquivo(this.blbArquivo);
		arquivo.setDtCriacao(this.dtCriacao);
		arquivo.setCdUsuario(this.cdUsuario);
		return arquivo;
	}
	
	public static Arquivo fromModel(com.tivic.manager.grl.Arquivo arquivoModel) {
		Arquivo arquivo = new Arquivo();
		arquivo.setCdArquivo(arquivoModel.getCdArquivo());
		arquivo.setNmArquivo(arquivoModel.getNmArquivo());
		arquivo.setDtArquivamento(arquivoModel.getDtArquivamento());
		arquivo.setNmDocumento(arquivoModel.getNmDocumento());
		arquivo.setBlbArquivo(arquivoModel.getBlbArquivo());
		arquivo.setDtCriacao(arquivoModel.getDtCriacao());
		arquivo.setCdUsuario(arquivoModel.getCdUsuario());
		return arquivo;
	}
}