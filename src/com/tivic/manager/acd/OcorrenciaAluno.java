package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class OcorrenciaAluno extends com.tivic.manager.grl.Ocorrencia {

	private int cdAluno;
	private GregorianCalendar dtUltimaModificacao;
	private int cdUsuarioModificador;

	public OcorrenciaAluno(){ }

	public OcorrenciaAluno(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario,
			int cdAluno,
			GregorianCalendar dtUltimaModificacao,
			int cdUsuarioModificador){
		super(cdOcorrencia,
			cdPessoa,
			txtOcorrencia,
			dtOcorrencia,
			cdTipoOcorrencia,
			stOcorrencia,
			cdSistema,
			cdUsuario);
		setCdAluno(cdAluno);
		setDtUltimaModificacao(dtUltimaModificacao);
		setCdUsuarioModificador(cdUsuarioModificador);
	}
	public void setCdAluno(int cdAluno){
		this.cdAluno=cdAluno;
	}
	public int getCdAluno(){
		return this.cdAluno;
	}
	public void setDtUltimaModificacao(GregorianCalendar dtUltimaModificacao){
		this.dtUltimaModificacao=dtUltimaModificacao;
	}
	public GregorianCalendar getDtUltimaModificacao(){
		return this.dtUltimaModificacao;
	}
	public void setCdUsuarioModificador(int cdUsuarioModificador){
		this.cdUsuarioModificador=cdUsuarioModificador;
	}
	public int getCdUsuarioModificador(){
		return this.cdUsuarioModificador;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdAluno: " +  getCdAluno();
		valueToString += ", dtUltimaModificacao: " +  sol.util.Util.formatDateTime(getDtUltimaModificacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUsuarioModificador: " +  getCdUsuarioModificador();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OcorrenciaAluno(getCdOcorrencia(),
			getCdPessoa(),
			getTxtOcorrencia(),
			getDtOcorrencia(),
			getCdTipoOcorrencia(),
			getStOcorrencia(),
			getCdSistema(),
			getCdUsuario(),
			getCdAluno(),
			getDtUltimaModificacao()==null ? null : (GregorianCalendar)getDtUltimaModificacao().clone(),
			getCdUsuarioModificador());
	}

}