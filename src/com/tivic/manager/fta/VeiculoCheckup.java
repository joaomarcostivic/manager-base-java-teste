package com.tivic.manager.fta;

import java.util.GregorianCalendar;

public class VeiculoCheckup {

	private int cdCheckup;
	private int cdTipoCheckup;
	private int cdVeiculo;
	private GregorianCalendar dtCheckup;
	private String txtObservacao;
	private int cdViagem;
	private int cdAgendamento;
	private String txtDiagnostico;
	private int stCheckup;
	private int tpOrigem;
	private GregorianCalendar dtPrazoConclusao;
	private int cdUsuario;
	private int cdUsuarioResponsavel;

	public VeiculoCheckup(int cdCheckup,
			int cdTipoCheckup,
			int cdVeiculo,
			GregorianCalendar dtCheckup,
			String txtObservacao,
			int cdViagem,
			int cdAgendamento,
			String txtDiagnostico,
			int stCheckup,
			int tpOrigem,
			GregorianCalendar dtPrazoConclusao,
			int cdUsuario,
			int cdUsuarioResponsavel){
		setCdCheckup(cdCheckup);
		setCdTipoCheckup(cdTipoCheckup);
		setCdVeiculo(cdVeiculo);
		setDtCheckup(dtCheckup);
		setTxtObservacao(txtObservacao);
		setCdViagem(cdViagem);
		setCdAgendamento(cdAgendamento);
		setTxtDiagnostico(txtDiagnostico);
		setStCheckup(stCheckup);
		setTpOrigem(tpOrigem);
		setDtPrazoConclusao(dtPrazoConclusao);
		setCdUsuario(cdUsuario);
		setCdUsuarioResponsavel(cdUsuarioResponsavel);
	}
	public void setCdCheckup(int cdCheckup){
		this.cdCheckup=cdCheckup;
	}
	public int getCdCheckup(){
		return this.cdCheckup;
	}
	public void setCdTipoCheckup(int cdTipoCheckup){
		this.cdTipoCheckup=cdTipoCheckup;
	}
	public int getCdTipoCheckup(){
		return this.cdTipoCheckup;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setDtCheckup(GregorianCalendar dtCheckup){
		this.dtCheckup=dtCheckup;
	}
	public GregorianCalendar getDtCheckup(){
		return this.dtCheckup;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdViagem(int cdViagem){
		this.cdViagem=cdViagem;
	}
	public int getCdViagem(){
		return this.cdViagem;
	}
	public void setCdAgendamento(int cdAgendamento){
		this.cdAgendamento=cdAgendamento;
	}
	public int getCdAgendamento(){
		return this.cdAgendamento;
	}
	public void setTxtDiagnostico(String txtDiagnostico){
		this.txtDiagnostico=txtDiagnostico;
	}
	public String getTxtDiagnostico(){
		return this.txtDiagnostico;
	}
	public void setStCheckup(int stCheckup){
		this.stCheckup=stCheckup;
	}
	public int getStCheckup(){
		return this.stCheckup;
	}
	public void setTpOrigem(int tpOrigem){
		this.tpOrigem=tpOrigem;
	}
	public int getTpOrigem(){
		return this.tpOrigem;
	}
	public void setDtPrazoConclusao(GregorianCalendar dtPrazoConclusao){
		this.dtPrazoConclusao=dtPrazoConclusao;
	}
	public GregorianCalendar getDtPrazoConclusao(){
		return this.dtPrazoConclusao;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdUsuarioResponsavel(int cdUsuarioResponsavel){
		this.cdUsuarioResponsavel=cdUsuarioResponsavel;
	}
	public int getCdUsuarioResponsavel(){
		return this.cdUsuarioResponsavel;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCheckup: " +  getCdCheckup();
		valueToString += ", cdTipoCheckup: " +  getCdTipoCheckup();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", dtCheckup: " +  sol.util.Util.formatDateTime(getDtCheckup(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdViagem: " +  getCdViagem();
		valueToString += ", cdAgendamento: " +  getCdAgendamento();
		valueToString += ", txtDiagnostico: " +  getTxtDiagnostico();
		valueToString += ", stCheckup: " +  getStCheckup();
		valueToString += ", tpOrigem: " +  getTpOrigem();
		valueToString += ", dtPrazoConclusao: " +  sol.util.Util.formatDateTime(getDtPrazoConclusao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdUsuarioResponsavel: " +  getCdUsuarioResponsavel();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new VeiculoCheckup(getCdCheckup(),
			getCdTipoCheckup(),
			getCdVeiculo(),
			getDtCheckup()==null ? null : (GregorianCalendar)getDtCheckup().clone(),
			getTxtObservacao(),
			getCdViagem(),
			getCdAgendamento(),
			getTxtDiagnostico(),
			getStCheckup(),
			getTpOrigem(),
			getDtPrazoConclusao()==null ? null : (GregorianCalendar)getDtPrazoConclusao().clone(),
			getCdUsuario(),
			getCdUsuarioResponsavel());
	}

}
