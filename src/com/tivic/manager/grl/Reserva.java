package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class Reserva {

	private int cdReserva;
	private GregorianCalendar dtReserva;
	private GregorianCalendar dtValidade;
	private int tpReserva;
	private int stReserva;
	private int cdUsuario;
	private int cdAtendimento;
	private int cdReferencia;
	private int cdProdutoServico;
	private int cdEmpresa;
	private String txtReserva;
	private int cdPessoa;
	private int cdResponsavel;

	public Reserva(int cdReserva,
			GregorianCalendar dtReserva,
			GregorianCalendar dtValidade,
			int tpReserva,
			int stReserva,
			int cdUsuario,
			int cdAtendimento,
			int cdReferencia,
			int cdProdutoServico,
			int cdEmpresa,
			String txtReserva,
			int cdPessoa,
			int cdResponsavel){
		setCdReserva(cdReserva);
		setDtReserva(dtReserva);
		setDtValidade(dtValidade);
		setTpReserva(tpReserva);
		setStReserva(stReserva);
		setCdUsuario(cdUsuario);
		setCdAtendimento(cdAtendimento);
		setCdReferencia(cdReferencia);
		setCdProdutoServico(cdProdutoServico);
		setCdEmpresa(cdEmpresa);
		setTxtReserva(txtReserva);
		setCdPessoa(cdPessoa);
		setCdResponsavel(cdResponsavel);
	}
	public void setCdReserva(int cdReserva){
		this.cdReserva=cdReserva;
	}
	public int getCdReserva(){
		return this.cdReserva;
	}
	public void setDtReserva(GregorianCalendar dtReserva){
		this.dtReserva=dtReserva;
	}
	public GregorianCalendar getDtReserva(){
		return this.dtReserva;
	}
	public void setDtValidade(GregorianCalendar dtValidade){
		this.dtValidade=dtValidade;
	}
	public GregorianCalendar getDtValidade(){
		return this.dtValidade;
	}
	public void setTpReserva(int tpReserva){
		this.tpReserva=tpReserva;
	}
	public int getTpReserva(){
		return this.tpReserva;
	}
	public void setStReserva(int stReserva){
		this.stReserva=stReserva;
	}
	public int getStReserva(){
		return this.stReserva;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdAtendimento(int cdAtendimento){
		this.cdAtendimento=cdAtendimento;
	}
	public int getCdAtendimento(){
		return this.cdAtendimento;
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setTxtReserva(String txtReserva){
		this.txtReserva=txtReserva;
	}
	public String getTxtReserva(){
		return this.txtReserva;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdReserva: " +  getCdReserva();
		valueToString += ", dtReserva: " +  sol.util.Util.formatDateTime(getDtReserva(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtValidade: " +  sol.util.Util.formatDateTime(getDtValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpReserva: " +  getTpReserva();
		valueToString += ", stReserva: " +  getStReserva();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdAtendimento: " +  getCdAtendimento();
		valueToString += ", cdReferencia: " +  getCdReferencia();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", txtReserva: " +  getTxtReserva();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdResponsavel: " +  getCdResponsavel();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Reserva(getCdReserva(),
			getDtReserva()==null ? null : (GregorianCalendar)getDtReserva().clone(),
			getDtValidade()==null ? null : (GregorianCalendar)getDtValidade().clone(),
			getTpReserva(),
			getStReserva(),
			getCdUsuario(),
			getCdAtendimento(),
			getCdReferencia(),
			getCdProdutoServico(),
			getCdEmpresa(),
			getTxtReserva(),
			getCdPessoa(),
			getCdResponsavel());
	}

}