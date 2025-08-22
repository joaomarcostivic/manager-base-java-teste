package com.tivic.manager.grl;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Arquivo {

	private int cdArquivo;
	private String nmArquivo;
	private GregorianCalendar dtArquivamento;
	private String nmDocumento;
	private int nrRegistros;
	private byte[] blbArquivo;
	private int cdTipoArquivo;
	private GregorianCalendar dtCriacao;
	private int cdUsuario;
	private int stArquivo;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private int cdAssinatura;
	private String txtOcr;
	private String txtCaminhoArquivo;
	private String dsArquivo;
	
	public Arquivo(){ }
	
	public Arquivo(int cdArquivo,
			String nmArquivo,
			GregorianCalendar dtArquivamento,
			String nmDocumento,
			byte[] blbArquivo,
			int cdTipoArquivo,
			GregorianCalendar dtCriacao,
			int cdUsuario,
			int stArquivo,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			int cdAssinatura,
			String txtOcr,
			int nrRegistro,
			String txtCaminhoArquivo,
			String dsArquivo) {
		setCdArquivo(cdArquivo);
		setNmArquivo(nmArquivo);
		setDtArquivamento(dtArquivamento);
		setNmDocumento(nmDocumento);
		setBlbArquivo(blbArquivo);
		setCdTipoArquivo(cdTipoArquivo);
		setDtCriacao(dtCriacao);
		setCdUsuario(cdUsuario);
		setStArquivo(stArquivo);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setCdAssinatura(cdAssinatura);
		setTxtOcr(txtOcr);
		setNrRegistros(nrRegistro);
		setTxtCaminhoArquivo(txtCaminhoArquivo);
		setDsArquivo(dsArquivo);
	}
	

	public String getDsArquivo() {
		return dsArquivo;
	}

	public void setDsArquivo(String dsArquivo) {
		this.dsArquivo = dsArquivo;
	}

	public String getTxtCaminhoArquivo() {
		return txtCaminhoArquivo;
	}

	public void setTxtCaminhoArquivo(String txtCaminhoArquivo) {
		this.txtCaminhoArquivo = txtCaminhoArquivo;
	}

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
	public void setCdTipoArquivo(int cdTipoArquivo){
		this.cdTipoArquivo=cdTipoArquivo;
	}
	public int getCdTipoArquivo(){
		return this.cdTipoArquivo;
	}
	public void setDtCriacao(GregorianCalendar dtCriacao){
		this.dtCriacao=dtCriacao;
	}
	public GregorianCalendar getDtCriacao(){
		return this.dtCriacao;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setStArquivo(int stArquivo){
		this.stArquivo=stArquivo;
	}
	public int getStArquivo(){
		return this.stArquivo;
	}
	public void setDtInicial(GregorianCalendar dtInicial){
		this.dtInicial=dtInicial;
	}
	public GregorianCalendar getDtInicial(){
		return this.dtInicial;
	}
	public void setDtFinal(GregorianCalendar dtFinal){
		this.dtFinal=dtFinal;
	}
	public GregorianCalendar getDtFinal(){
		return this.dtFinal;
	}
	public void setCdAssinatura(int cdAssinatura){
		this.cdAssinatura=cdAssinatura;
	}
	public int getCdAssinatura(){
		return this.cdAssinatura;
	}
	public void setTxtOcr(String txtOcr){
		this.txtOcr=txtOcr;
	}
	public String getTxtOcr(){
		return this.txtOcr;
	}

	public int getNrRegistros() {
		return nrRegistros;
	}

	public void setNrRegistros(int nrRegistros) {
		this.nrRegistros = nrRegistros;
	}

	@Override
	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } 
        catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
	}

	public Object clone() {
		return new Arquivo(getCdArquivo(),
			getNmArquivo(),
			getDtArquivamento()==null ? null : (GregorianCalendar)getDtArquivamento().clone(),
			getNmDocumento(),
			getBlbArquivo(),
			getCdTipoArquivo(),
			getDtCriacao()==null ? null : (GregorianCalendar)getDtCriacao().clone(),
			getCdUsuario(),
			getStArquivo(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getCdAssinatura(),
			getTxtOcr(),
			getNrRegistros(),
			getTxtCaminhoArquivo(),
			getDsArquivo());
	}

}