package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class ContratoDesconto {

	private int cdContrato;
	private int cdDesconto;
	private int cdTipoDesconto;
	private int cdFaixaDesconto;
	private int cdEmpresa;
	private GregorianCalendar dtInclusao;
	private float prDesconto;
	private int cdMovimentoFidelidade;

	public ContratoDesconto(int cdContrato,
			int cdDesconto,
			int cdTipoDesconto,
			int cdFaixaDesconto,
			int cdEmpresa,
			GregorianCalendar dtInclusao,
			float prDesconto,
			int cdMovimentoFidelidade){
		setCdContrato(cdContrato);
		setCdDesconto(cdDesconto);
		setCdTipoDesconto(cdTipoDesconto);
		setCdFaixaDesconto(cdFaixaDesconto);
		setCdEmpresa(cdEmpresa);
		setDtInclusao(dtInclusao);
		setPrDesconto(prDesconto);
		setCdMovimentoFidelidade(cdMovimentoFidelidade);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdDesconto(int cdDesconto){
		this.cdDesconto=cdDesconto;
	}
	public int getCdDesconto(){
		return this.cdDesconto;
	}
	public void setCdTipoDesconto(int cdTipoDesconto){
		this.cdTipoDesconto=cdTipoDesconto;
	}
	public int getCdTipoDesconto(){
		return this.cdTipoDesconto;
	}
	public void setCdFaixaDesconto(int cdFaixaDesconto){
		this.cdFaixaDesconto=cdFaixaDesconto;
	}
	public int getCdFaixaDesconto(){
		return this.cdFaixaDesconto;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setDtInclusao(GregorianCalendar dtInclusao){
		this.dtInclusao=dtInclusao;
	}
	public GregorianCalendar getDtInclusao(){
		return this.dtInclusao;
	}
	public void setPrDesconto(float prDesconto){
		this.prDesconto=prDesconto;
	}
	public float getPrDesconto(){
		return this.prDesconto;
	}
	public void setCdMovimentoFidelidade(int cdMovimentoFidelidade){
		this.cdMovimentoFidelidade=cdMovimentoFidelidade;
	}
	public int getCdMovimentoFidelidade(){
		return this.cdMovimentoFidelidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdDesconto: " +  getCdDesconto();
		valueToString += ", cdTipoDesconto: " +  getCdTipoDesconto();
		valueToString += ", cdFaixaDesconto: " +  getCdFaixaDesconto();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", dtInclusao: " +  sol.util.Util.formatDateTime(getDtInclusao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", prDesconto: " +  getPrDesconto();
		valueToString += ", cdMovimentoFidelidade: " +  getCdMovimentoFidelidade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContratoDesconto(getCdContrato(),
			getCdDesconto(),
			getCdTipoDesconto(),
			getCdFaixaDesconto(),
			getCdEmpresa(),
			getDtInclusao()==null ? null : (GregorianCalendar)getDtInclusao().clone(),
			getPrDesconto(),
			getCdMovimentoFidelidade());
	}

}
