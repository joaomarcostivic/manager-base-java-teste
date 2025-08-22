package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class MatriculaPeriodoLetivo {

	private int cdMatricula;
	private int cdPeriodoLetivo;
	private int cdContrato;
	private GregorianCalendar dtMatricula;
	private int stMatricula;
	private int cdMotivoTrancamento;
	private int cdPedidoVenda;
	private int cdRota;

	public MatriculaPeriodoLetivo(){ }

	public MatriculaPeriodoLetivo(int cdMatricula,
			int cdPeriodoLetivo,
			int cdContrato,
			GregorianCalendar dtMatricula,
			int stMatricula,
			int cdMotivoTrancamento,
			int cdPedidoVenda,
			int cdRota){
		setCdMatricula(cdMatricula);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setCdContrato(cdContrato);
		setDtMatricula(dtMatricula);
		setStMatricula(stMatricula);
		setCdMotivoTrancamento(cdMotivoTrancamento);
		setCdPedidoVenda(cdPedidoVenda);
		setCdRota(cdRota);
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setDtMatricula(GregorianCalendar dtMatricula){
		this.dtMatricula=dtMatricula;
	}
	public GregorianCalendar getDtMatricula(){
		return this.dtMatricula;
	}
	public void setStMatricula(int stMatricula){
		this.stMatricula=stMatricula;
	}
	public int getStMatricula(){
		return this.stMatricula;
	}
	public void setCdMotivoTrancamento(int cdMotivoTrancamento){
		this.cdMotivoTrancamento=cdMotivoTrancamento;
	}
	public int getCdMotivoTrancamento(){
		return this.cdMotivoTrancamento;
	}
	public void setCdPedidoVenda(int cdPedidoVenda){
		this.cdPedidoVenda=cdPedidoVenda;
	}
	public int getCdPedidoVenda(){
		return this.cdPedidoVenda;
	}
	public void setCdRota(int cdRota){
		this.cdRota=cdRota;
	}
	public int getCdRota(){
		return this.cdRota;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMatricula: " +  getCdMatricula();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		valueToString += ", cdContrato: " +  getCdContrato();
		valueToString += ", dtMatricula: " +  sol.util.Util.formatDateTime(getDtMatricula(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stMatricula: " +  getStMatricula();
		valueToString += ", cdMotivoTrancamento: " +  getCdMotivoTrancamento();
		valueToString += ", cdPedidoVenda: " +  getCdPedidoVenda();
		valueToString += ", cdRota: " +  getCdRota();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MatriculaPeriodoLetivo(getCdMatricula(),
			getCdPeriodoLetivo(),
			getCdContrato(),
			getDtMatricula()==null ? null : (GregorianCalendar)getDtMatricula().clone(),
			getStMatricula(),
			getCdMotivoTrancamento(),
			getCdPedidoVenda(),
			getCdRota());
	}

}