package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class InstituicaoRecurso {

	private int cdInstituicaoRecurso;
	private int cdInstituicao;
	private int cdRecurso;
	private int cdTipoRecurso;
	private int cdUsuario;
	private GregorianCalendar dtEntrega;
	private GregorianCalendar dtPrevisaoDevolucao;
	private GregorianCalendar dtDevolucao;
	private int stInstituicaoRecurso;
	private String txtObservacao;

	public InstituicaoRecurso() { }

	public InstituicaoRecurso(int cdInstituicaoRecurso,
			int cdInstituicao,
			int cdRecurso,
			int cdTipoRecurso,
			int cdUsuario,
			GregorianCalendar dtEntrega,
			GregorianCalendar dtPrevisaoDevolucao,
			GregorianCalendar dtDevolucao,
			int stInstituicaoRecurso,
			String txtObservacao) {
		setCdInstituicaoRecurso(cdInstituicaoRecurso);
		setCdInstituicao(cdInstituicao);
		setCdRecurso(cdRecurso);
		setCdTipoRecurso(cdTipoRecurso);
		setCdUsuario(cdUsuario);
		setDtEntrega(dtEntrega);
		setDtPrevisaoDevolucao(dtPrevisaoDevolucao);
		setDtDevolucao(dtDevolucao);
		setStInstituicaoRecurso(stInstituicaoRecurso);
		setTxtObservacao(txtObservacao);
	}
	public void setCdInstituicaoRecurso(int cdInstituicaoRecurso){
		this.cdInstituicaoRecurso=cdInstituicaoRecurso;
	}
	public int getCdInstituicaoRecurso(){
		return this.cdInstituicaoRecurso;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdRecurso(int cdRecurso){
		this.cdRecurso=cdRecurso;
	}
	public int getCdRecurso(){
		return this.cdRecurso;
	}
	public void setCdTipoRecurso(int cdTipoRecurso){
		this.cdTipoRecurso=cdTipoRecurso;
	}
	public int getCdTipoRecurso(){
		return this.cdTipoRecurso;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setDtEntrega(GregorianCalendar dtEntrega){
		this.dtEntrega=dtEntrega;
	}
	public GregorianCalendar getDtEntrega(){
		return this.dtEntrega;
	}
	public void setDtPrevisaoDevolucao(GregorianCalendar dtPrevisaoDevolucao){
		this.dtPrevisaoDevolucao=dtPrevisaoDevolucao;
	}
	public GregorianCalendar getDtPrevisaoDevolucao(){
		return this.dtPrevisaoDevolucao;
	}
	public void setDtDevolucao(GregorianCalendar dtDevolucao){
		this.dtDevolucao=dtDevolucao;
	}
	public GregorianCalendar getDtDevolucao(){
		return this.dtDevolucao;
	}
	public void setStInstituicaoRecurso(int stInstituicaoRecurso){
		this.stInstituicaoRecurso=stInstituicaoRecurso;
	}
	public int getStInstituicaoRecurso(){
		return this.stInstituicaoRecurso;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdInstituicaoRecurso: " +  getCdInstituicaoRecurso();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdRecurso: " +  getCdRecurso();
		valueToString += ", cdTipoRecurso: " +  getCdTipoRecurso();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", dtEntrega: " +  sol.util.Util.formatDateTime(getDtEntrega(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtPrevisaoDevolucao: " +  sol.util.Util.formatDateTime(getDtPrevisaoDevolucao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtDevolucao: " +  sol.util.Util.formatDateTime(getDtDevolucao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stInstituicaoRecurso: " +  getStInstituicaoRecurso();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoRecurso(getCdInstituicaoRecurso(),
			getCdInstituicao(),
			getCdRecurso(),
			getCdTipoRecurso(),
			getCdUsuario(),
			getDtEntrega()==null ? null : (GregorianCalendar)getDtEntrega().clone(),
			getDtPrevisaoDevolucao()==null ? null : (GregorianCalendar)getDtPrevisaoDevolucao().clone(),
			getDtDevolucao()==null ? null : (GregorianCalendar)getDtDevolucao().clone(),
			getStInstituicaoRecurso(),
			getTxtObservacao());
	}

}