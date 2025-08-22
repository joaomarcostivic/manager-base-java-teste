package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConcessaoVeiculo {

	private int cdConcessaoVeiculo;
	private int cdConcessao;
	private int cdVeiculo;
	private int nrPrefixo;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtAssinatura;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtInicioOperacao;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtFinalOperacao;
	
	private int tpFrota;
	private int stConcessaoVeiculo;
	private int lgManutencao;

	public ConcessaoVeiculo(){ }

	public ConcessaoVeiculo(int cdConcessaoVeiculo,
			int cdConcessao,
			int cdVeiculo,
			int nrPrefixo,
			GregorianCalendar dtAssinatura,
			GregorianCalendar dtInicioOperacao,
			GregorianCalendar dtFinalOperacao,
			int tpFrota,
			int stConcessaoVeiculo,
			int lgManutencao){
		setCdConcessaoVeiculo(cdConcessaoVeiculo);
		setCdConcessao(cdConcessao);
		setCdVeiculo(cdVeiculo);
		setNrPrefixo(nrPrefixo);
		setDtAssinatura(dtAssinatura);
		setDtInicioOperacao(dtInicioOperacao);
		setDtFinalOperacao(dtFinalOperacao);
		setTpFrota(tpFrota);
		setStConcessaoVeiculo(stConcessaoVeiculo);
		setLgManutencao(lgManutencao);
	}
	public void setCdConcessaoVeiculo(int cdConcessaoVeiculo){
		this.cdConcessaoVeiculo=cdConcessaoVeiculo;
	}
	public int getCdConcessaoVeiculo(){
		return this.cdConcessaoVeiculo;
	}
	public void setCdConcessao(int cdConcessao){
		this.cdConcessao=cdConcessao;
	}
	public int getCdConcessao(){
		return this.cdConcessao;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setNrPrefixo(int nrPrefixo){
		this.nrPrefixo=nrPrefixo;
	}
	public int getNrPrefixo(){
		return this.nrPrefixo;
	}
	public void setDtAssinatura(GregorianCalendar dtAssinatura){
		this.dtAssinatura=dtAssinatura;
	}
	public GregorianCalendar getDtAssinatura(){
		return this.dtAssinatura;
	}
	public void setDtInicioOperacao(GregorianCalendar dtInicioOperacao){
		this.dtInicioOperacao=dtInicioOperacao;
	}
	public GregorianCalendar getDtInicioOperacao(){
		return this.dtInicioOperacao;
	}
	public void setDtFinalOperacao(GregorianCalendar dtFinalOperacao){
		this.dtFinalOperacao=dtFinalOperacao;
	}
	public GregorianCalendar getDtFinalOperacao(){
		return this.dtFinalOperacao;
	}
	public void setTpFrota(int tpFrota){
		this.tpFrota=tpFrota;
	}
	public int getTpFrota(){
		return this.tpFrota;
	}
	public void setStConcessaoVeiculo(int stConcessaoVeiculo){
		this.stConcessaoVeiculo=stConcessaoVeiculo;
	}
	public int getStConcessaoVeiculo(){
		return this.stConcessaoVeiculo;
	}
	public void setLgManutencao(int lgManutencao){
		this.lgManutencao=lgManutencao;
	}
	public int getLgManutencao(){
		return this.lgManutencao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConcessaoVeiculo: " +  getCdConcessaoVeiculo();
		valueToString += ", cdConcessao: " +  getCdConcessao();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", nrPrefixo: " +  getNrPrefixo();
		valueToString += ", dtAssinatura: " +  sol.util.Util.formatDateTime(getDtAssinatura(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtInicioOperacao: " +  sol.util.Util.formatDateTime(getDtInicioOperacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinalOperacao: " +  sol.util.Util.formatDateTime(getDtFinalOperacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpFrota: " +  getTpFrota();
		valueToString += ", stConcessaoVeiculo: " +  getStConcessaoVeiculo();
		valueToString += ", lgManutencao: " +  getLgManutencao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ConcessaoVeiculo(getCdConcessaoVeiculo(),
			getCdConcessao(),
			getCdVeiculo(),
			getNrPrefixo(),
			getDtAssinatura()==null ? null : (GregorianCalendar)getDtAssinatura().clone(),
			getDtInicioOperacao()==null ? null : (GregorianCalendar)getDtInicioOperacao().clone(),
			getDtFinalOperacao()==null ? null : (GregorianCalendar)getDtFinalOperacao().clone(),
			getTpFrota(),
			getStConcessaoVeiculo(),
			getLgManutencao());
	}

}