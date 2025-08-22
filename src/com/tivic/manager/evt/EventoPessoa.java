package com.tivic.manager.evt;

import java.util.GregorianCalendar;

public class EventoPessoa {

	private int cdPessoa;
	private int cdEvento;
	private int tpParticipacao;
	private String idCadastro;
	private String nrMatricula;
	private int tpCargoPublico;
	private int cdContaReceber;
	private int cdSubevento;
	private int cdInscricao;
	private GregorianCalendar dtInscricao;
	private int cdLocal;

	public EventoPessoa() { }

	public EventoPessoa(int cdPessoa,
			int cdEvento,
			int tpParticipacao,
			String idCadastro,
			String nrMatricula,
			int tpCargoPublico,
			int cdContaReceber,
			int cdSubevento,
			int cdInscricao,
			GregorianCalendar dtInscricao,
			int cdLocal) {
		setCdPessoa(cdPessoa);
		setCdEvento(cdEvento);
		setTpParticipacao(tpParticipacao);
		setIdCadastro(idCadastro);
		setNrMatricula(nrMatricula);
		setTpCargoPublico(tpCargoPublico);
		setCdContaReceber(cdContaReceber);
		setCdSubevento(cdSubevento);
		setCdInscricao(cdInscricao);
		setDtInscricao(dtInscricao);
		setCdLocal(cdLocal);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdEvento(int cdEvento){
		this.cdEvento=cdEvento;
	}
	public int getCdEvento(){
		return this.cdEvento;
	}
	public void setTpParticipacao(int tpParticipacao){
		this.tpParticipacao=tpParticipacao;
	}
	public int getTpParticipacao(){
		return this.tpParticipacao;
	}
	public void setIdCadastro(String idCadastro){
		this.idCadastro=idCadastro;
	}
	public String getIdCadastro(){
		return this.idCadastro;
	}
	public void setNrMatricula(String nrMatricula){
		this.nrMatricula=nrMatricula;
	}
	public String getNrMatricula(){
		return this.nrMatricula;
	}
	public void setTpCargoPublico(int tpCargoPublico){
		this.tpCargoPublico=tpCargoPublico;
	}
	public int getTpCargoPublico(){
		return this.tpCargoPublico;
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public void setCdSubevento(int cdSubevento){
		this.cdSubevento=cdSubevento;
	}
	public int getCdSubevento(){
		return this.cdSubevento;
	}
	public void setCdInscricao(int cdInscricao){
		this.cdInscricao=cdInscricao;
	}
	public int getCdInscricao(){
		return this.cdInscricao;
	}
	public void setDtInscricao(GregorianCalendar dtInscricao){
		this.dtInscricao=dtInscricao;
	}
	public GregorianCalendar getDtInscricao(){
		return this.dtInscricao;
	}
	public void setCdLocal(int cdLocal){
		this.cdLocal=cdLocal;
	}
	public int getCdLocal(){
		return this.cdLocal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdEvento: " +  getCdEvento();
		valueToString += ", tpParticipacao: " +  getTpParticipacao();
		valueToString += ", idCadastro: " +  getIdCadastro();
		valueToString += ", nrMatricula: " +  getNrMatricula();
		valueToString += ", tpCargoPublico: " +  getTpCargoPublico();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		valueToString += ", cdSubevento: " +  getCdSubevento();
		valueToString += ", cdInscricao: " +  getCdInscricao();
		valueToString += ", dtInscricao: " +  sol.util.Util.formatDateTime(getDtInscricao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdLocal: " +  getCdLocal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EventoPessoa(getCdPessoa(),
			getCdEvento(),
			getTpParticipacao(),
			getIdCadastro(),
			getNrMatricula(),
			getTpCargoPublico(),
			getCdContaReceber(),
			getCdSubevento(),
			getCdInscricao(),
			getDtInscricao()==null ? null : (GregorianCalendar)getDtInscricao().clone(),
			getCdLocal());
	}

}