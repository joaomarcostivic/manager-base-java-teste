package com.tivic.manager.agd;

import java.util.GregorianCalendar;

public class Ocorrencia extends com.tivic.manager.grl.Ocorrencia {

	private int lgRepetivel;
	private int cdAgendamento;

	public Ocorrencia(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario,
			int lgRepetivel,
			int cdAgendamento){
		super(cdOcorrencia,
			cdPessoa,
			txtOcorrencia,
			dtOcorrencia,
			cdTipoOcorrencia,
			stOcorrencia,
			cdSistema,
			cdUsuario);
		setLgRepetivel(lgRepetivel);
		setCdAgendamento(cdAgendamento);
	}
	public void setLgRepetivel(int lgRepetivel){
		this.lgRepetivel=lgRepetivel;
	}
	public int getLgRepetivel(){
		return this.lgRepetivel;
	}
	public void setCdAgendamento(int cdAgendamento){
		this.cdAgendamento=cdAgendamento;
	}
	public int getCdAgendamento(){
		return this.cdAgendamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", lgRepetivel: " +  getLgRepetivel();
		valueToString += ", cdAgendamento: " +  getCdAgendamento();
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
			getLgRepetivel(),
			getCdAgendamento());
	}

}
