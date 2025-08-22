package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class ViagemAgendamento {

	private int cdViagemAgendamento;
	private int cdMotorista;
	private int cdConcessaoVeiculo;
	private String txtDescricao;
	private GregorianCalendar dtRegistro;
	private GregorianCalendar hrPartidaInicio;
	private GregorianCalendar hrChegadaInicio;
	private GregorianCalendar hrPartidaFinal;
	private GregorianCalendar hrChegadaFinal;
	private int lgLivreIntervalo;
	private int stViagemAgendamento;
	private int cdInstituicao;
	private int cdSetor;
	private int nrCapacidade;
	private int lgSegunda;
	private int lgTerca;
	private int lgQuarta;
	private int lgQuinta;
	private int lgSexta;
	private int lgSabado;
	private int lgDomingo;

	public ViagemAgendamento() { }

	public ViagemAgendamento(int cdViagemAgendamento,
			int cdMotorista,
			int cdConcessaoVeiculo,
			String txtDescricao,
			GregorianCalendar dtRegistro,
			GregorianCalendar hrPartidaInicio,
			GregorianCalendar hrChegadaInicio,
			GregorianCalendar hrPartidaFinal,
			GregorianCalendar hrChegadaFinal,
			int lgLivreIntervalo,
			int stViagemAgendamento,
			int cdInstituicao,
			int cdSetor,
			int nrCapacidade,
			int lgSegunda,
			int lgTerca,
			int lgQuarta,
			int lgQuinta,
			int lgSexta,
			int lgSabado,
			int lgDomingo) {
		setCdViagemAgendamento(cdViagemAgendamento);
		setCdMotorista(cdMotorista);
		setCdConcessaoVeiculo(cdConcessaoVeiculo);
		setTxtDescricao(txtDescricao);
		setDtRegistro(dtRegistro);
		setHrPartidaInicio(hrPartidaInicio);
		setHrChegadaInicio(hrChegadaInicio);
		setHrPartidaFinal(hrPartidaFinal);
		setHrChegadaFinal(hrChegadaFinal);
		setLgLivreIntervalo(lgLivreIntervalo);
		setStViagemAgendamento(stViagemAgendamento);
		setCdInstituicao(cdInstituicao);
		setCdSetor(cdSetor);
		setNrCapacidade(nrCapacidade);
		setLgSegunda(lgSegunda);
		setLgTerca(lgTerca);
		setLgQuarta(lgQuarta);
		setLgQuinta(lgQuinta);
		setLgSexta(lgSexta);
		setLgSabado(lgSabado);
		setLgDomingo(lgDomingo);
	}
	public void setCdViagemAgendamento(int cdViagemAgendamento){
		this.cdViagemAgendamento=cdViagemAgendamento;
	}
	public int getCdViagemAgendamento(){
		return this.cdViagemAgendamento;
	}
	public void setCdMotorista(int cdMotorista){
		this.cdMotorista=cdMotorista;
	}
	public int getCdMotorista(){
		return this.cdMotorista;
	}
	public void setCdConcessaoVeiculo(int cdConcessaoVeiculo){
		this.cdConcessaoVeiculo=cdConcessaoVeiculo;
	}
	public int getCdConcessaoVeiculo(){
		return this.cdConcessaoVeiculo;
	}
	public void setTxtDescricao(String txtDescricao){
		this.txtDescricao=txtDescricao;
	}
	public String getTxtDescricao(){
		return this.txtDescricao;
	}
	public void setDtRegistro(GregorianCalendar dtRegistro){
		this.dtRegistro=dtRegistro;
	}
	public GregorianCalendar getDtRegistro(){
		return this.dtRegistro;
	}
	public void setHrPartidaInicio(GregorianCalendar hrPartidaInicio){
		this.hrPartidaInicio=hrPartidaInicio;
	}
	public GregorianCalendar getHrPartidaInicio(){
		return this.hrPartidaInicio;
	}
	public void setHrChegadaInicio(GregorianCalendar hrChegadaInicio){
		this.hrChegadaInicio=hrChegadaInicio;
	}
	public GregorianCalendar getHrChegadaInicio(){
		return this.hrChegadaInicio;
	}
	public void setHrPartidaFinal(GregorianCalendar hrPartidaFinal){
		this.hrPartidaFinal=hrPartidaFinal;
	}
	public GregorianCalendar getHrPartidaFinal(){
		return this.hrPartidaFinal;
	}
	public void setHrChegadaFinal(GregorianCalendar hrChegadaFinal){
		this.hrChegadaFinal=hrChegadaFinal;
	}
	public GregorianCalendar getHrChegadaFinal(){
		return this.hrChegadaFinal;
	}
	public void setLgLivreIntervalo(int lgLivreIntervalo){
		this.lgLivreIntervalo=lgLivreIntervalo;
	}
	public int getLgLivreIntervalo(){
		return this.lgLivreIntervalo;
	}
	public void setStViagemAgendamento(int stViagemAgendamento){
		this.stViagemAgendamento=stViagemAgendamento;
	}
	public int getStViagemAgendamento(){
		return this.stViagemAgendamento;
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
	public void setNrCapacidade(int nrCapacidade){
		this.nrCapacidade=nrCapacidade;
	}
	public int getNrCapacidade(){
		return this.nrCapacidade;
	}
	public void setLgSegunda(int lgSegunda){
		this.lgSegunda=lgSegunda;
	}
	public int getLgSegunda(){
		return this.lgSegunda;
	}
	public void setLgTerca(int lgTerca){
		this.lgTerca=lgTerca;
	}
	public int getLgTerca(){
		return this.lgTerca;
	}
	public void setLgQuarta(int lgQuarta){
		this.lgQuarta=lgQuarta;
	}
	public int getLgQuarta(){
		return this.lgQuarta;
	}
	public void setLgQuinta(int lgQuinta){
		this.lgQuinta=lgQuinta;
	}
	public int getLgQuinta(){
		return this.lgQuinta;
	}
	public void setLgSexta(int lgSexta){
		this.lgSexta=lgSexta;
	}
	public int getLgSexta(){
		return this.lgSexta;
	}
	public void setLgSabado(int lgSabado){
		this.lgSabado=lgSabado;
	}
	public int getLgSabado(){
		return this.lgSabado;
	}
	public void setLgDomingo(int lgDomingo){
		this.lgDomingo=lgDomingo;
	}
	public int getLgDomingo(){
		return this.lgDomingo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdViagemAgendamento: " +  getCdViagemAgendamento();
		valueToString += ", cdMotorista: " +  getCdMotorista();
		valueToString += ", cdConcessaoVeiculo: " +  getCdConcessaoVeiculo();
		valueToString += ", txtDescricao: " +  getTxtDescricao();
		valueToString += ", dtRegistro: " +  sol.util.Util.formatDateTime(getDtRegistro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrPartidaInicio: " +  sol.util.Util.formatDateTime(getHrPartidaInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrChegadaInicio: " +  sol.util.Util.formatDateTime(getHrChegadaInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrPartidaFinal: " +  sol.util.Util.formatDateTime(getHrPartidaFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrChegadaFinal: " +  sol.util.Util.formatDateTime(getHrChegadaFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgLivreIntervalo: " +  getLgLivreIntervalo();
		valueToString += ", stViagemAgendamento: " +  getStViagemAgendamento();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", nrCapacidade: " +  getNrCapacidade();
		valueToString += ", lgSegunda: " +  getLgSegunda();
		valueToString += ", lgTerca: " +  getLgTerca();
		valueToString += ", lgQuarta: " +  getLgQuarta();
		valueToString += ", lgQuinta: " +  getLgQuinta();
		valueToString += ", lgSexta: " +  getLgSexta();
		valueToString += ", lgSabado: " +  getLgSabado();
		valueToString += ", lgDomingo: " +  getLgDomingo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ViagemAgendamento(getCdViagemAgendamento(),
			getCdMotorista(),
			getCdConcessaoVeiculo(),
			getTxtDescricao(),
			getDtRegistro()==null ? null : (GregorianCalendar)getDtRegistro().clone(),
			getHrPartidaInicio()==null ? null : (GregorianCalendar)getHrPartidaInicio().clone(),
			getHrChegadaInicio()==null ? null : (GregorianCalendar)getHrChegadaInicio().clone(),
			getHrPartidaFinal()==null ? null : (GregorianCalendar)getHrPartidaFinal().clone(),
			getHrChegadaFinal()==null ? null : (GregorianCalendar)getHrChegadaFinal().clone(),
			getLgLivreIntervalo(),
			getStViagemAgendamento(),
			getCdInstituicao(),
			getCdSetor(),
			getNrCapacidade(),
			getLgSegunda(),
			getLgTerca(),
			getLgQuarta(),
			getLgQuinta(),
			getLgSexta(),
			getLgSabado(),
			getLgDomingo());
	}

}