package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class OcorrenciaTurma extends com.tivic.manager.grl.Ocorrencia {

	private int cdTurma;
	private GregorianCalendar dtUltimaModificacao;
	private int cdUsuarioModificador;
	private int stTurmaAnterior;
	private int stTurmaPosterior;

	public OcorrenciaTurma(){ }

	public OcorrenciaTurma(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario,
			int cdTurma,
			GregorianCalendar dtUltimaModificacao,
			int cdUsuarioModificador,
			int stTurmaAnterior,
			int stTurmaPosterior){
		super(cdOcorrencia,
			cdPessoa,
			txtOcorrencia,
			dtOcorrencia,
			cdTipoOcorrencia,
			stOcorrencia,
			cdSistema,
			cdUsuario);
		setCdTurma(cdTurma);
		setDtUltimaModificacao(dtUltimaModificacao);
		setCdUsuarioModificador(cdUsuarioModificador);
		setStTurmaAnterior(stTurmaAnterior);
		setStTurmaPosterior(stTurmaPosterior);
	}
	public void setCdTurma(int cdTurma){
		this.cdTurma=cdTurma;
	}
	public int getCdTurma(){
		return this.cdTurma;
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
	public void setStTurmaAnterior(int stTurmaAnterior){
		this.stTurmaAnterior=stTurmaAnterior;
	}
	public int getStTurmaAnterior(){
		return this.stTurmaAnterior;
	}
	public void setStTurmaPosterior(int stTurmaPosterior){
		this.stTurmaPosterior=stTurmaPosterior;
	}
	public int getStTurmaPosterior(){
		return this.stTurmaPosterior;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdTurma: " +  getCdTurma();
		valueToString += ", dtUltimaModificacao: " +  sol.util.Util.formatDateTime(getDtUltimaModificacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUsuarioModificador: " +  getCdUsuarioModificador();
		valueToString += ", stTurmaAnterior: " +  getStTurmaAnterior();
		valueToString += ", stTurmaPosterior: " +  getStTurmaPosterior();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OcorrenciaTurma(getCdOcorrencia(),
			getCdPessoa(),
			getTxtOcorrencia(),
			getDtOcorrencia(),
			getCdTipoOcorrencia(),
			getStOcorrencia(),
			getCdSistema(),
			getCdUsuario(),
			getCdTurma(),
			getDtUltimaModificacao()==null ? null : (GregorianCalendar)getDtUltimaModificacao().clone(),
			getCdUsuarioModificador(),
			getStTurmaAnterior(),
			getStTurmaPosterior());
	}

}