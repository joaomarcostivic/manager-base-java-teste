package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class Viagem {

	private int cdViagem;
	private GregorianCalendar dtViagem;
	private GregorianCalendar hrPartida;
	private GregorianCalendar hrChegada;
	private int tpViagem;
	private int stViagem;
	private int cdInstituicao;
	private int cdSetor;
	private String txtDescricao;
	private int cdConcessaoVeiculo;
	private int cdMotorista;
	private int lgManutencao;
	private int cdViagemAnterior;
	private int nrCapacidade;
	private int cdViagemAgendamento;

	public Viagem() { }

	public Viagem(int cdViagem,
			GregorianCalendar dtViagem,
			GregorianCalendar hrPartida,
			GregorianCalendar hrChegada,
			int tpViagem,
			int stViagem,
			int cdInstituicao,
			int cdSetor,
			String txtDescricao,
			int cdConcessaoVeiculo,
			int cdMotorista,
			int lgManutencao,
			int cdViagemAnterior,
			int nrCapacidade,
			int cdViagemAgendamento) {
		setCdViagem(cdViagem);
		setDtViagem(dtViagem);
		setHrPartida(hrPartida);
		setHrChegada(hrChegada);
		setTpViagem(tpViagem);
		setStViagem(stViagem);
		setCdInstituicao(cdInstituicao);
		setCdSetor(cdSetor);
		setTxtDescricao(txtDescricao);
		setCdConcessaoVeiculo(cdConcessaoVeiculo);
		setCdMotorista(cdMotorista);
		setLgManutencao(lgManutencao);
		setCdViagemAnterior(cdViagemAnterior);
		setNrCapacidade(nrCapacidade);
		setCdViagemAgendamento(cdViagemAgendamento);
	}
	public void setCdViagem(int cdViagem){
		this.cdViagem=cdViagem;
	}
	public int getCdViagem(){
		return this.cdViagem;
	}
	public void setDtViagem(GregorianCalendar dtViagem){
		this.dtViagem=dtViagem;
	}
	public GregorianCalendar getDtViagem(){
		return this.dtViagem;
	}
	public void setHrPartida(GregorianCalendar hrPartida){
		this.hrPartida=hrPartida;
	}
	public GregorianCalendar getHrPartida(){
		return this.hrPartida;
	}
	public void setHrChegada(GregorianCalendar hrChegada){
		this.hrChegada=hrChegada;
	}
	public GregorianCalendar getHrChegada(){
		return this.hrChegada;
	}
	public void setTpViagem(int tpViagem){
		this.tpViagem=tpViagem;
	}
	public int getTpViagem(){
		return this.tpViagem;
	}
	public void setStViagem(int stViagem){
		this.stViagem=stViagem;
	}
	public int getStViagem(){
		return this.stViagem;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public void setTxtDescricao(String txtDescricao){
		this.txtDescricao=txtDescricao;
	}
	public String getTxtDescricao(){
		return this.txtDescricao;
	}
	public void setCdConcessaoVeiculo(int cdConcessaoVeiculo){
		this.cdConcessaoVeiculo=cdConcessaoVeiculo;
	}
	public int getCdConcessaoVeiculo(){
		return this.cdConcessaoVeiculo;
	}
	public void setCdMotorista(int cdMotorista){
		this.cdMotorista=cdMotorista;
	}
	public int getCdMotorista(){
		return this.cdMotorista;
	}
	public void setLgManutencao(int lgManutencao){
		this.lgManutencao=lgManutencao;
	}
	public int getLgManutencao(){
		return this.lgManutencao;
	}
	public void setCdViagemAnterior(int cdViagemAnterior){
		this.cdViagemAnterior=cdViagemAnterior;
	}
	public int getCdViagemAnterior(){
		return this.cdViagemAnterior;
	}
	public void setNrCapacidade(int nrCapacidade){
		this.nrCapacidade=nrCapacidade;
	}
	public int getNrCapacidade(){
		return this.nrCapacidade;
	}
	public void setCdViagemAgendamento(int cdViagemAgendamento){
		this.cdViagemAgendamento=cdViagemAgendamento;
	}
	public int getCdViagemAgendamento(){
		return this.cdViagemAgendamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdViagem: " +  getCdViagem();
		valueToString += ", dtViagem: " +  sol.util.Util.formatDateTime(getDtViagem(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrPartida: " +  sol.util.Util.formatDateTime(getHrPartida(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrChegada: " +  sol.util.Util.formatDateTime(getHrChegada(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpViagem: " +  getTpViagem();
		valueToString += ", stViagem: " +  getStViagem();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", txtDescricao: " +  getTxtDescricao();
		valueToString += ", cdConcessaoVeiculo: " +  getCdConcessaoVeiculo();
		valueToString += ", cdMotorista: " +  getCdMotorista();
		valueToString += ", lgManutencao: " +  getLgManutencao();
		valueToString += ", cdViagemAnterior: " +  getCdViagemAnterior();
		valueToString += ", nrCapacidade: " +  getNrCapacidade();
		valueToString += ", cdViagemAgendamento: " +  getCdViagemAgendamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Viagem(getCdViagem(),
			getDtViagem()==null ? null : (GregorianCalendar)getDtViagem().clone(),
			getHrPartida()==null ? null : (GregorianCalendar)getHrPartida().clone(),
			getHrChegada()==null ? null : (GregorianCalendar)getHrChegada().clone(),
			getTpViagem(),
			getStViagem(),
			getCdInstituicao(),
			getCdSetor(),
			getTxtDescricao(),
			getCdConcessaoVeiculo(),
			getCdMotorista(),
			getLgManutencao(),
			getCdViagemAnterior(),
			getNrCapacidade(),
			getCdViagemAgendamento());
	}

}