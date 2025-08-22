package com.tivic.manager.mob.grafica;

import java.util.GregorianCalendar;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tivic.manager.util.Util;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoteImpressaoAit {

	private int cdLoteImpressao;
	private int cdAit;
	private String idLoteImpressaoAit;
	private int stImpressao;
	private GregorianCalendar dtInclusao;
	private GregorianCalendar dtImpressao;
	private GregorianCalendar dtEnvio;
	private int cdArquivo;
	private String txtErro;
	

	public LoteImpressaoAit() { }

	public LoteImpressaoAit(int cdLoteImpressao,
			int cdAit,
			String idLoteImpressaoAit,
			int stImpressao,
			GregorianCalendar dtInclusao,
			GregorianCalendar dtImpressao,
			GregorianCalendar dtEnvio,
			int cdArquivo,
			String txtErro) {
		setCdLoteImpressao(cdLoteImpressao);
		setCdAit(cdAit);
		setIdLoteImpressaoAit(idLoteImpressaoAit);
		setStImpressao(stImpressao);
		setDtInclusao(dtInclusao);
		setDtImpressao(dtImpressao);
		setDtEnvio(dtEnvio);
		setCdArquivo(cdArquivo);
		setTxtErro(txtErro);
	}
	public void setCdLoteImpressao(int cdLoteImpressao){
		this.cdLoteImpressao=cdLoteImpressao;
	}
	public int getCdLoteImpressao(){
		return this.cdLoteImpressao;
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setIdLoteImpressaoAit(String idLoteImpressaoAit){
		this.idLoteImpressaoAit=idLoteImpressaoAit;
	}
	public String getIdLoteImpressaoAit(){
		return this.idLoteImpressaoAit;
	}
	public void setStImpressao(int stImpressao){
		this.stImpressao=stImpressao;
	}
	public int getStImpressao(){
		return this.stImpressao;
	}
	public void setDtInclusao(GregorianCalendar dtInclusao){
		this.dtInclusao=dtInclusao;
	}
	public GregorianCalendar getDtInclusao(){
		return this.dtInclusao;
	}
	public void setDtImpressao(GregorianCalendar dtImpressao){
		this.dtImpressao=dtImpressao;
	}
	public GregorianCalendar getDtImpressao(){
		return this.dtImpressao;
	}
	public void setDtEnvio(GregorianCalendar dtEnvio){
		this.dtEnvio=dtEnvio;
	}
	public GregorianCalendar getDtEnvio(){
		return this.dtEnvio;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public String getTxtErro() {
		return txtErro;
	}
	public void setTxtErro(String txtErro) {
		this.txtErro = txtErro;
	}
	
	@Override
	public String toString() {
		try {
			JSONObject json = new JSONObject();
			json.put("cdLoteImpressao", getCdLoteImpressao());
			json.put("cdAit", getCdAit());
			json.put("idLoteImpressaoAit", getIdLoteImpressaoAit());
			json.put("stImpressao", getStImpressao());
			json.put("dtInclusao", Util.convCalendarStringIso(getDtInclusao()));
			json.put("dtImpressao", Util.convCalendarStringIso(getDtImpressao()));
			json.put("dtEnvio", Util.convCalendarStringIso(getDtEnvio()));
			json.put("cdArquivo", getCdArquivo());
			return json.toString();
		} catch(Exception e) {
			return super.toString();
		}
		
	}

	public Object clone() {
		return new LoteImpressaoAit(getCdLoteImpressao(),
			getCdAit(),
			getIdLoteImpressaoAit(),
			getStImpressao(),
			getDtInclusao()==null ? null : (GregorianCalendar)getDtInclusao().clone(),
			getDtImpressao()==null ? null : (GregorianCalendar)getDtImpressao().clone(),
			getDtEnvio()==null ? null : (GregorianCalendar)getDtEnvio().clone(),
			getCdArquivo(),
			getTxtErro());
	}

}