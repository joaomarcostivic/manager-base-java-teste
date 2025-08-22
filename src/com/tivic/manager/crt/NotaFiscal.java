package com.tivic.manager.crt;

import java.util.GregorianCalendar;

public class NotaFiscal {

	private int cdNotaFiscal;
	private int nrNotaFiscal;
	private GregorianCalendar dtNotaFiscal;
	private GregorianCalendar dtEmissao;
	private int stNotaFiscal;
	private int cdEmpresa;
	private int cdPessoa;
	private float vlTotalNota;
	private float vlNotaFiscal;
	private float vlIss;
	private int qtItens;

	public NotaFiscal(int cdNotaFiscal,
			int nrNotaFiscal,
			GregorianCalendar dtNotaFiscal,
			GregorianCalendar dtEmissao,
			int stNotaFiscal,
			int cdEmpresa,
			int cdPessoa,
			float vlTotalNota,
			float vlNotaFiscal,
			float vlIss,
			int qtItens){
		setCdNotaFiscal(cdNotaFiscal);
		setNrNotaFiscal(nrNotaFiscal);
		setDtNotaFiscal(dtNotaFiscal);
		setDtEmissao(dtEmissao);
		setStNotaFiscal(stNotaFiscal);
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setVlTotalNota(vlTotalNota);
		setVlNotaFiscal(vlNotaFiscal);
		setVlIss(vlIss);
		setQtItens(qtItens);
	}
	public void setCdNotaFiscal(int cdNotaFiscal){
		this.cdNotaFiscal=cdNotaFiscal;
	}
	public int getCdNotaFiscal(){
		return this.cdNotaFiscal;
	}
	public void setNrNotaFiscal(int nrNotaFiscal){
		this.nrNotaFiscal=nrNotaFiscal;
	}
	public int getNrNotaFiscal(){
		return this.nrNotaFiscal;
	}
	public void setDtNotaFiscal(GregorianCalendar dtNotaFiscal){
		this.dtNotaFiscal=dtNotaFiscal;
	}
	public GregorianCalendar getDtNotaFiscal(){
		return this.dtNotaFiscal;
	}
	public void setDtEmissao(GregorianCalendar dtEmissao){
		this.dtEmissao=dtEmissao;
	}
	public GregorianCalendar getDtEmissao(){
		return this.dtEmissao;
	}
	public void setStNotaFiscal(int stNotaFiscal){
		this.stNotaFiscal=stNotaFiscal;
	}
	public int getStNotaFiscal(){
		return this.stNotaFiscal;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setVlTotalNota(float vlTotalNota){
		this.vlTotalNota=vlTotalNota;
	}
	public float getVlTotalNota(){
		return this.vlTotalNota;
	}
	public void setVlNotaFiscal(float vlNotaFiscal){
		this.vlNotaFiscal=vlNotaFiscal;
	}
	public float getVlNotaFiscal(){
		return this.vlNotaFiscal;
	}
	public void setVlIss(float vlIss){
		this.vlIss=vlIss;
	}
	public float getVlIss(){
		return this.vlIss;
	}
	public void setQtItens(int qtItens){
		this.qtItens=qtItens;
	}
	public int getQtItens(){
		return this.qtItens;
	}
}