package com.tivic.manager.mob.grafica;

import java.util.GregorianCalendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoteImpressao {

	private int cdLoteImpressao;
	private String idLoteImpressao;
	private String txtObservacao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtCriacao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtFinalizacao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtAtualizacao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtEnvio;
	private int stLoteImpressao;
	private int tpTransporte;
	private String txtTransporte;
	private int tpDestino;
	private int cdUsuario;
	private int cdArquivo;
	private int cdOrgao;
	private int tpLoteImpressao;
	private int tpDocumento;
	private int qtdDocumentos;
	
	private List<LoteImpressaoAit> aits;
	
	public LoteImpressao() { }
	
	public LoteImpressao(int cdLoteImpressao,
			String idLoteImpressao,
			String txtObservacao,
			GregorianCalendar dtCriacao,
			GregorianCalendar dtFinalizacao,
			GregorianCalendar dtAtualizacao,
			GregorianCalendar dtEnvio,
			int stLoteImpressao,
			int tpTransporte,
			String txtTransporte,
			int tpDestino,
			int cdUsuario,
			int cdArquivo,
			int cdOrgao,
			int tpLoteImpressao,
			int tpDocumento) {
		setCdLoteImpressao(cdLoteImpressao);
		setIdLoteImpressao(idLoteImpressao);
		setTxtObservacao(txtObservacao);
		setDtCriacao(dtCriacao);
		setDtFinalizacao(dtFinalizacao);
		setDtAtualizacao(dtAtualizacao);
		setDtEnvio(dtEnvio);
		setStLoteImpressao(stLoteImpressao);
		setTpTransporte(tpTransporte);
		setTxtTransporte(txtTransporte);
		setTpDestino(tpDestino);
		setCdUsuario(cdUsuario);
		setCdArquivo(cdArquivo);
		setCdOrgao(cdOrgao);
		setTpLoteImpressao(tpLoteImpressao);
		setTpDocumento(tpDocumento);
	}

	public LoteImpressao(int cdLoteImpressao,
			String idLoteImpressao,
			String txtObservacao,
			GregorianCalendar dtCriacao,
			GregorianCalendar dtFinalizacao,
			GregorianCalendar dtAtualizacao,
			GregorianCalendar dtEnvio,
			int stLoteImpressao,
			int tpTransporte,
			String txtTransporte,
			int tpDestino,
			int cdUsuario,
			int cdArquivo,
			int cdOrgao,
			int tpLoteImpressao,
			int tpDocumento,
			int qtdDocumentos) {
		setCdLoteImpressao(cdLoteImpressao);
		setIdLoteImpressao(idLoteImpressao);
		setTxtObservacao(txtObservacao);
		setDtCriacao(dtCriacao);
		setDtFinalizacao(dtFinalizacao);
		setDtAtualizacao(dtAtualizacao);
		setDtEnvio(dtEnvio);
		setStLoteImpressao(stLoteImpressao);
		setTpTransporte(tpTransporte);
		setTxtTransporte(txtTransporte);
		setTpDestino(tpDestino);
		setCdUsuario(cdUsuario);
		setCdArquivo(cdArquivo);
		setCdOrgao(cdOrgao);
		setTpLoteImpressao(tpLoteImpressao);
		setTpDocumento(tpDocumento);
		setQtdDocumentos(qtdDocumentos);
	}
	
	public void setCdLoteImpressao(int cdLoteImpressao){
		this.cdLoteImpressao=cdLoteImpressao;
	}
	public int getCdLoteImpressao(){
		return this.cdLoteImpressao;
	}
	public void setIdLoteImpressao(String idLoteImpressao){
		this.idLoteImpressao=idLoteImpressao;
	}
	public String getIdLoteImpressao(){
		return this.idLoteImpressao;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setDtCriacao(GregorianCalendar dtCriacao){
		this.dtCriacao=dtCriacao;
	}
	public GregorianCalendar getDtCriacao(){
		return this.dtCriacao;
	}
	public void setDtFinalizacao(GregorianCalendar dtFinalizacao){
		this.dtFinalizacao=dtFinalizacao;
	}
	public GregorianCalendar getDtFinalizacao(){
		return this.dtFinalizacao;
	}
	public void setDtAtualizacao(GregorianCalendar dtAtualizacao){
		this.dtAtualizacao=dtAtualizacao;
	}
	public GregorianCalendar getDtAtualizacao(){
		return this.dtAtualizacao;
	}
	public void setDtEnvio(GregorianCalendar dtEnvio){
		this.dtEnvio=dtEnvio;
	}
	public GregorianCalendar getDtEnvio(){
		return this.dtEnvio;
	}
	public void setStLoteImpressao(int stLoteImpressao){
		this.stLoteImpressao=stLoteImpressao;
	}
	public int getStLoteImpressao(){
		return this.stLoteImpressao;
	}
	public void setTpTransporte(int tpTransporte){
		this.tpTransporte=tpTransporte;
	}
	public int getTpTransporte(){
		return this.tpTransporte;
	}
	public void setTxtTransporte(String txtTransporte){
		this.txtTransporte=txtTransporte;
	}
	public String getTxtTransporte(){
		return this.txtTransporte;
	}
	public void setTpDestino(int tpDestino){
		this.tpDestino=tpDestino;
	}
	public int getTpDestino(){
		return this.tpDestino;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public void setTpLoteImpressao(int tpLoteImpressao){
		this.tpLoteImpressao=tpLoteImpressao;
	}
	public int getTpLoteImpressao(){
		return this.tpLoteImpressao;
	}
	public void setTpDocumento(int tpDocumento){
		this.tpDocumento=tpDocumento;
	}


	public int getQtdDocumentos(){
		return this.qtdDocumentos;
	}
	public void setQtdDocumentos(int qtdDocumentos){
		this.qtdDocumentos=qtdDocumentos;
	}
	
	public int getTpDocumento(){
		return this.tpDocumento;
	}
	public void setTpDocument(int tpDocumento){
		this.tpDocumento=tpDocumento;
	}
	
	public List<LoteImpressaoAit> getAits() {
		return aits;
	}
	public void setAits(List<LoteImpressaoAit> aits) {
		this.aits = aits;
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
		return new LoteImpressao(getCdLoteImpressao(),
			getIdLoteImpressao(),
			getTxtObservacao(),
			getDtCriacao()==null ? null : (GregorianCalendar)getDtCriacao().clone(),
			getDtFinalizacao()==null ? null : (GregorianCalendar)getDtFinalizacao().clone(),
			getDtAtualizacao()==null ? null : (GregorianCalendar)getDtAtualizacao().clone(),
			getDtEnvio()==null ? null : (GregorianCalendar)getDtEnvio().clone(),
			getStLoteImpressao(),
			getTpTransporte(),
			getTxtTransporte(),
			getTpDestino(),
			getCdUsuario(),
			getCdArquivo(),
			getCdOrgao(),
			getTpLoteImpressao(),
			getTpDocumento(),
			getQtdDocumentos());
	}

}