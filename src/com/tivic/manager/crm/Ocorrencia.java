package com.tivic.manager.crm;

import java.util.GregorianCalendar;

public class Ocorrencia extends com.tivic.manager.grl.Ocorrencia {

	private int cdAtendimento;
	private int cdAgendamento;
	private int cdCentral;
	private int cdAtendente;

	public Ocorrencia(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario,
			int cdAtendimento,
			int cdAgendamento,
			int cdCentral,
			int cdAtendente){
		super(cdOcorrencia,
			cdPessoa,
			txtOcorrencia,
			dtOcorrencia,
			cdTipoOcorrencia,
			stOcorrencia,
			cdSistema,
			cdUsuario);
		setCdAtendimento(cdAtendimento);
		setCdAgendamento(cdAgendamento);
		setCdCentral(cdCentral);
		setCdAtendente(cdAtendente);
	}
	public void setCdAtendimento(int cdAtendimento){
		this.cdAtendimento=cdAtendimento;
	}
	public int getCdAtendimento(){
		return this.cdAtendimento;
	}
	public void setCdAgendamento(int cdAgendamento){
		this.cdAgendamento=cdAgendamento;
	}
	public int getCdAgendamento(){
		return this.cdAgendamento;
	}
	public void setCdCentral(int cdCentral){
		this.cdCentral=cdCentral;
	}
	public int getCdCentral(){
		return this.cdCentral;
	}
	public void setCdAtendente(int cdAtendente){
		this.cdAtendente=cdAtendente;
	}
	public int getCdAtendente(){
		return this.cdAtendente;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdAtendimento: " +  getCdAtendimento();
		valueToString += ", cdAgendamento: " +  getCdAgendamento();
		valueToString += ", cdCentral: " +  getCdCentral();
		valueToString += ", cdAtendente: " +  getCdAtendente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Ocorrencia(getCdOcorrencia(),
			getCdPessoa(),
			getTxtOcorrencia(),
			getDtOcorrencia(),
			getCdTipoOcorrencia(),
			getStOcorrencia(),
			getCdSistema(),
			getCdUsuario(),
			getCdAtendimento(),
			getCdAgendamento(),
			getCdCentral(),
			getCdAtendente());
	}

}
