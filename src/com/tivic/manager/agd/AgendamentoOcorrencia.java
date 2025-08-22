package com.tivic.manager.agd;

import java.util.GregorianCalendar;

public class AgendamentoOcorrencia {

	private int cdTipoOcorrencia;
	private int cdAgendamento;
	private int cdUsuario;
	private GregorianCalendar dtOcorrencia;
	private String txtOcorrencia;
	private int tpVisibilidade;
	private GregorianCalendar dtCadastro;

	public AgendamentoOcorrencia() { }

	public AgendamentoOcorrencia(int cdTipoOcorrencia,
			int cdAgendamento,
			int cdUsuario,
			GregorianCalendar dtOcorrencia,
			String txtOcorrencia,
			int tpVisibilidade,
			GregorianCalendar dtCadastro) {
		setCdTipoOcorrencia(cdTipoOcorrencia);
		setCdAgendamento(cdAgendamento);
		setCdUsuario(cdUsuario);
		setDtOcorrencia(dtOcorrencia);
		setTxtOcorrencia(txtOcorrencia);
		setTpVisibilidade(tpVisibilidade);
		setDtCadastro(dtCadastro);
	}
	public void setCdTipoOcorrencia(int cdTipoOcorrencia){
		this.cdTipoOcorrencia=cdTipoOcorrencia;
	}
	public int getCdTipoOcorrencia(){
		return this.cdTipoOcorrencia;
	}
	public void setCdAgendamento(int cdAgendamento){
		this.cdAgendamento=cdAgendamento;
	}
	public int getCdAgendamento(){
		return this.cdAgendamento;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia){
		this.dtOcorrencia=dtOcorrencia;
	}
	public GregorianCalendar getDtOcorrencia(){
		return this.dtOcorrencia;
	}
	public void setTxtOcorrencia(String txtOcorrencia){
		this.txtOcorrencia=txtOcorrencia;
	}
	public String getTxtOcorrencia(){
		return this.txtOcorrencia;
	}
	public void setTpVisibilidade(int tpVisibilidade){
		this.tpVisibilidade=tpVisibilidade;
	}
	public int getTpVisibilidade(){
		return this.tpVisibilidade;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoOcorrencia: " +  getCdTipoOcorrencia();
		valueToString += ", cdAgendamento: " +  getCdAgendamento();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", dtOcorrencia: " +  sol.util.Util.formatDateTime(getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtOcorrencia: " +  getTxtOcorrencia();
		valueToString += ", tpVisibilidade: " +  getTpVisibilidade();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AgendamentoOcorrencia(getCdTipoOcorrencia(),
			getCdAgendamento(),
			getCdUsuario(),
			getDtOcorrencia()==null ? null : (GregorianCalendar)getDtOcorrencia().clone(),
			getTxtOcorrencia(),
			getTpVisibilidade(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone());
	}

}