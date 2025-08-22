package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class InstituicaoGrupoPessoa {

	private int cdPessoa;
	private int cdGrupo;
	private int cdInstituicao;
	private GregorianCalendar dtIngresso;
	private GregorianCalendar dtSaida;
	private int stParticipacao;
	private String nmFuncao;
	private int lgAluno;

	public InstituicaoGrupoPessoa(){ }

	public InstituicaoGrupoPessoa(int cdPessoa,
			int cdGrupo,
			int cdInstituicao,
			GregorianCalendar dtIngresso,
			GregorianCalendar dtSaida,
			int stParticipacao,
			String nmFuncao,
			int lgAluno){
		setCdPessoa(cdPessoa);
		setCdGrupo(cdGrupo);
		setCdInstituicao(cdInstituicao);
		setDtIngresso(dtIngresso);
		setDtSaida(dtSaida);
		setStParticipacao(stParticipacao);
		setNmFuncao(nmFuncao);
		setLgAluno(lgAluno);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setDtIngresso(GregorianCalendar dtIngresso){
		this.dtIngresso=dtIngresso;
	}
	public GregorianCalendar getDtIngresso(){
		return this.dtIngresso;
	}
	public void setDtSaida(GregorianCalendar dtSaida){
		this.dtSaida=dtSaida;
	}
	public GregorianCalendar getDtSaida(){
		return this.dtSaida;
	}
	public void setStParticipacao(int stParticipacao){
		this.stParticipacao=stParticipacao;
	}
	public int getStParticipacao(){
		return this.stParticipacao;
	}
	public void setNmFuncao(String nmFuncao){
		this.nmFuncao=nmFuncao;
	}
	public String getNmFuncao(){
		return this.nmFuncao;
	}
	public void setLgAluno(int lgAluno){
		this.lgAluno=lgAluno;
	}
	public int getLgAluno(){
		return this.lgAluno;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", dtIngresso: " +  sol.util.Util.formatDateTime(getDtIngresso(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtSaida: " +  sol.util.Util.formatDateTime(getDtSaida(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stParticipacao: " +  getStParticipacao();
		valueToString += ", nmFuncao: " +  getNmFuncao();
		valueToString += ", lgAluno: " +  getLgAluno();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoGrupoPessoa(getCdPessoa(),
			getCdGrupo(),
			getCdInstituicao(),
			getDtIngresso()==null ? null : (GregorianCalendar)getDtIngresso().clone(),
			getDtSaida()==null ? null : (GregorianCalendar)getDtSaida().clone(),
			getStParticipacao(),
			getNmFuncao(),
			getLgAluno());
	}

}
