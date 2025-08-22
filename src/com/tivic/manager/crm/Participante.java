package com.tivic.manager.crm;

import java.util.GregorianCalendar;

public class Participante {

	private int cdCentral;
	private int cdUsuario;
	private int cdAtendimento;
	private GregorianCalendar dtAdmissao;
	private int cdTipoParticipante;

	public Participante(int cdCentral,
			int cdUsuario,
			int cdAtendimento,
			GregorianCalendar dtAdmissao,
			int cdTipoParticipante){
		setCdCentral(cdCentral);
		setCdUsuario(cdUsuario);
		setCdAtendimento(cdAtendimento);
		setDtAdmissao(dtAdmissao);
		setCdTipoParticipante(cdTipoParticipante);
	}
	public void setCdCentral(int cdCentral){
		this.cdCentral=cdCentral;
	}
	public int getCdCentral(){
		return this.cdCentral;
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
	public void setDtAdmissao(GregorianCalendar dtAdmissao){
		this.dtAdmissao=dtAdmissao;
	}
	public GregorianCalendar getDtAdmissao(){
		return this.dtAdmissao;
	}
	public void setCdTipoParticipante(int cdTipoParticipante){
		this.cdTipoParticipante=cdTipoParticipante;
	}
	public int getCdTipoParticipante(){
		return this.cdTipoParticipante;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCentral: " +  getCdCentral();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdAtendimento: " +  getCdAtendimento();
		valueToString += ", dtAdmissao: " +  sol.util.Util.formatDateTime(getDtAdmissao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdTipoParticipante: " +  getCdTipoParticipante();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Participante(getCdCentral(),
			getCdUsuario(),
			getCdAtendimento(),
			getDtAdmissao()==null ? null : (GregorianCalendar)getDtAdmissao().clone(),
			getCdTipoParticipante());
	}

}