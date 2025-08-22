package com.tivic.manager.ord;

import java.util.GregorianCalendar;

public class OrdemServicoItemDefeito {

	private int cdOrdemServicoItem;
	private int cdDefeito;
	private int cdOrdemServico;
	private int nrHorasPrevisaoReparo;
	private GregorianCalendar dtAnalise;
	private String txtObservacao;
	private int lgRelatoCliente;

	public OrdemServicoItemDefeito(){ }

	public OrdemServicoItemDefeito(int cdOrdemServicoItem,
			int cdDefeito,
			int cdOrdemServico,
			int nrHorasPrevisaoReparo,
			GregorianCalendar dtAnalise,
			String txtObservacao,
			int lgRelatoCliente){
		setCdOrdemServicoItem(cdOrdemServicoItem);
		setCdDefeito(cdDefeito);
		setCdOrdemServico(cdOrdemServico);
		setNrHorasPrevisaoReparo(nrHorasPrevisaoReparo);
		setDtAnalise(dtAnalise);
		setTxtObservacao(txtObservacao);
		setLgRelatoCliente(lgRelatoCliente);
	}
	public void setCdOrdemServicoItem(int cdOrdemServicoItem){
		this.cdOrdemServicoItem=cdOrdemServicoItem;
	}
	public int getCdOrdemServicoItem(){
		return this.cdOrdemServicoItem;
	}
	public void setCdDefeito(int cdDefeito){
		this.cdDefeito=cdDefeito;
	}
	public int getCdDefeito(){
		return this.cdDefeito;
	}
	public void setCdOrdemServico(int cdOrdemServico){
		this.cdOrdemServico=cdOrdemServico;
	}
	public int getCdOrdemServico(){
		return this.cdOrdemServico;
	}
	public void setNrHorasPrevisaoReparo(int nrHorasPrevisaoReparo){
		this.nrHorasPrevisaoReparo=nrHorasPrevisaoReparo;
	}
	public int getNrHorasPrevisaoReparo(){
		return this.nrHorasPrevisaoReparo;
	}
	public void setDtAnalise(GregorianCalendar dtAnalise){
		this.dtAnalise=dtAnalise;
	}
	public GregorianCalendar getDtAnalise(){
		return this.dtAnalise;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setLgRelatoCliente(int lgRelatoCliente){
		this.lgRelatoCliente=lgRelatoCliente;
	}
	public int getLgRelatoCliente(){
		return this.lgRelatoCliente;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOrdemServicoItem: " +  getCdOrdemServicoItem();
		valueToString += ", cdDefeito: " +  getCdDefeito();
		valueToString += ", cdOrdemServico: " +  getCdOrdemServico();
		valueToString += ", nrHorasPrevisaoReparo: " +  getNrHorasPrevisaoReparo();
		valueToString += ", dtAnalise: " +  sol.util.Util.formatDateTime(getDtAnalise(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", lgRelatoCliente: " +  getLgRelatoCliente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OrdemServicoItemDefeito(getCdOrdemServicoItem(),
			getCdDefeito(),
			getCdOrdemServico(),
			getNrHorasPrevisaoReparo(),
			getDtAnalise()==null ? null : (GregorianCalendar)getDtAnalise().clone(),
			getTxtObservacao(),
			getLgRelatoCliente());
	}

}
