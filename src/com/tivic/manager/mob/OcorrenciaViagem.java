package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class OcorrenciaViagem extends com.tivic.manager.grl.Ocorrencia {

	private int cdViagemAnterior;
	private int cdViagemPosterior;
	private int cdConcessaoVeiculoAnterior;
	private int cdConcessaoVeiculoPosterior;
	private int cdMotoristaAnterior;
	private int cdMotoristaPosterior;
	private int stViagemAnterior;
	private int stViagemPosterior;
	private GregorianCalendar hrPartidaAnterior;
	private GregorianCalendar hrChegadaAnterior;
	private GregorianCalendar hrPartidaPosterior;
	private GregorianCalendar hrChegadaPosterior;
	private int cdViagemAgendamento;

	public OcorrenciaViagem() { }

	public OcorrenciaViagem(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario,
			int cdViagemAnterior,
			int cdViagemPosterior,
			int cdConcessaoVeiculoAnterior,
			int cdConcessaoVeiculoPosterior,
			int cdMotoristaAnterior,
			int cdMotoristaPosterior,
			int stViagemAnterior,
			int stViagemPosterior,
			GregorianCalendar hrPartidaAnterior,
			GregorianCalendar hrChegadaAnterior,
			GregorianCalendar hrPartidaPosterior,
			GregorianCalendar hrChegadaPosterior,
			int cdViagemAgendamento) {
		super(cdOcorrencia,
			cdPessoa,
			txtOcorrencia,
			dtOcorrencia,
			cdTipoOcorrencia,
			stOcorrencia,
			cdSistema,
			cdUsuario);
		setCdViagemAnterior(cdViagemAnterior);
		setCdViagemPosterior(cdViagemPosterior);
		setCdConcessaoVeiculoAnterior(cdConcessaoVeiculoAnterior);
		setCdConcessaoVeiculoPosterior(cdConcessaoVeiculoPosterior);
		setCdMotoristaAnterior(cdMotoristaAnterior);
		setCdMotoristaPosterior(cdMotoristaPosterior);
		setStViagemAnterior(stViagemAnterior);
		setStViagemPosterior(stViagemPosterior);
		setHrPartidaAnterior(hrPartidaAnterior);
		setHrChegadaAnterior(hrChegadaAnterior);
		setHrPartidaPosterior(hrPartidaPosterior);
		setHrChegadaPosterior(hrChegadaPosterior);
		setCdViagemAgendamento(cdViagemAgendamento);
	}
	public void setCdViagemAnterior(int cdViagemAnterior){
		this.cdViagemAnterior=cdViagemAnterior;
	}
	public int getCdViagemAnterior(){
		return this.cdViagemAnterior;
	}
	public void setCdViagemPosterior(int cdViagemPosterior){
		this.cdViagemPosterior=cdViagemPosterior;
	}
	public int getCdViagemPosterior(){
		return this.cdViagemPosterior;
	}
	public void setCdConcessaoVeiculoAnterior(int cdConcessaoVeiculoAnterior){
		this.cdConcessaoVeiculoAnterior=cdConcessaoVeiculoAnterior;
	}
	public int getCdConcessaoVeiculoAnterior(){
		return this.cdConcessaoVeiculoAnterior;
	}
	public void setCdConcessaoVeiculoPosterior(int cdConcessaoVeiculoPosterior){
		this.cdConcessaoVeiculoPosterior=cdConcessaoVeiculoPosterior;
	}
	public int getCdConcessaoVeiculoPosterior(){
		return this.cdConcessaoVeiculoPosterior;
	}
	public void setCdMotoristaAnterior(int cdMotoristaAnterior){
		this.cdMotoristaAnterior=cdMotoristaAnterior;
	}
	public int getCdMotoristaAnterior(){
		return this.cdMotoristaAnterior;
	}
	public void setCdMotoristaPosterior(int cdMotoristaPosterior){
		this.cdMotoristaPosterior=cdMotoristaPosterior;
	}
	public int getCdMotoristaPosterior(){
		return this.cdMotoristaPosterior;
	}
	public void setStViagemAnterior(int stViagemAnterior){
		this.stViagemAnterior=stViagemAnterior;
	}
	public int getStViagemAnterior(){
		return this.stViagemAnterior;
	}
	public void setStViagemPosterior(int stViagemPosterior){
		this.stViagemPosterior=stViagemPosterior;
	}
	public int getStViagemPosterior(){
		return this.stViagemPosterior;
	}
	public void setHrPartidaAnterior(GregorianCalendar hrPartidaAnterior){
		this.hrPartidaAnterior=hrPartidaAnterior;
	}
	public GregorianCalendar getHrPartidaAnterior(){
		return this.hrPartidaAnterior;
	}
	public void setHrChegadaAnterior(GregorianCalendar hrChegadaAnterior){
		this.hrChegadaAnterior=hrChegadaAnterior;
	}
	public GregorianCalendar getHrChegadaAnterior(){
		return this.hrChegadaAnterior;
	}
	public void setHrPartidaPosterior(GregorianCalendar hrPartidaPosterior){
		this.hrPartidaPosterior=hrPartidaPosterior;
	}
	public GregorianCalendar getHrPartidaPosterior(){
		return this.hrPartidaPosterior;
	}
	public void setHrChegadaPosterior(GregorianCalendar hrChegadaPosterior){
		this.hrChegadaPosterior=hrChegadaPosterior;
	}
	public GregorianCalendar getHrChegadaPosterior(){
		return this.hrChegadaPosterior;
	}
	public void setCdViagemAgendamento(int cdViagemAgendamento){
		this.cdViagemAgendamento=cdViagemAgendamento;
	}
	public int getCdViagemAgendamento(){
		return this.cdViagemAgendamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdViagemAnterior: " +  getCdViagemAnterior();
		valueToString += ", cdViagemPosterior: " +  getCdViagemPosterior();
		valueToString += ", cdConcessaoVeiculoAnterior: " +  getCdConcessaoVeiculoAnterior();
		valueToString += ", cdConcessaoVeiculoPosterior: " +  getCdConcessaoVeiculoPosterior();
		valueToString += ", cdMotoristaAnterior: " +  getCdMotoristaAnterior();
		valueToString += ", cdMotoristaPosterior: " +  getCdMotoristaPosterior();
		valueToString += ", stViagemAnterior: " +  getStViagemAnterior();
		valueToString += ", stViagemPosterior: " +  getStViagemPosterior();
		valueToString += ", hrPartidaAnterior: " +  sol.util.Util.formatDateTime(getHrPartidaAnterior(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrChegadaAnterior: " +  sol.util.Util.formatDateTime(getHrChegadaAnterior(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrPartidaPosterior: " +  sol.util.Util.formatDateTime(getHrPartidaPosterior(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrChegadaPosterior: " +  sol.util.Util.formatDateTime(getHrChegadaPosterior(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdViagemAgendamento: " +  getCdViagemAgendamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OcorrenciaViagem(getCdOcorrencia(),
			getCdPessoa(),
			getTxtOcorrencia(),
			getDtOcorrencia(),
			getCdTipoOcorrencia(),
			getStOcorrencia(),
			getCdSistema(),
			getCdUsuario(),
			getCdViagemAnterior(),
			getCdViagemPosterior(),
			getCdConcessaoVeiculoAnterior(),
			getCdConcessaoVeiculoPosterior(),
			getCdMotoristaAnterior(),
			getCdMotoristaPosterior(),
			getStViagemAnterior(),
			getStViagemPosterior(),
			getHrPartidaAnterior()==null ? null : (GregorianCalendar)getHrPartidaAnterior().clone(),
			getHrChegadaAnterior()==null ? null : (GregorianCalendar)getHrChegadaAnterior().clone(),
			getHrPartidaPosterior()==null ? null : (GregorianCalendar)getHrPartidaPosterior().clone(),
			getHrChegadaPosterior()==null ? null : (GregorianCalendar)getHrChegadaPosterior().clone(),
			getCdViagemAgendamento());
	}

}