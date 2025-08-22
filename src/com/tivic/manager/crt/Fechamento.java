package com.tivic.manager.crt;

import java.util.GregorianCalendar;

public class Fechamento {

	private int cdFechamento;
	private int cdEmpresa;
	private GregorianCalendar dtFechamento;
	private float vlFechamento;
	private int stFechamento;

	public Fechamento(int cdFechamento,
			int cdEmpresa,
			GregorianCalendar dtFechamento,
			float vlFechamento,
			int stFechamento){
		setCdFechamento(cdFechamento);
		setCdEmpresa(cdEmpresa);
		setDtFechamento(dtFechamento);
		setVlFechamento(vlFechamento);
		setStFechamento(stFechamento);
	}
	public void setCdFechamento(int cdFechamento){
		this.cdFechamento=cdFechamento;
	}
	public int getCdFechamento(){
		return this.cdFechamento;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setDtFechamento(GregorianCalendar dtFechamento){
		this.dtFechamento=dtFechamento;
	}
	public GregorianCalendar getDtFechamento(){
		return this.dtFechamento;
	}
	public void setVlFechamento(float vlFechamento){
		this.vlFechamento=vlFechamento;
	}
	public float getVlFechamento(){
		return this.vlFechamento;
	}
	public void setStFechamento(int stFechamento){
		this.stFechamento=stFechamento;
	}
	public int getStFechamento(){
		return this.stFechamento;
	}
}