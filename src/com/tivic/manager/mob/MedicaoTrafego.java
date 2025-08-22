package com.tivic.manager.mob;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class MedicaoTrafego {

	private int cdMedicao;
	private int cdEquipamento;
	private int cdVia;
	private int cdFaixa;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private int tpVeiculo;
	private int qtVeiculos;
	private Double vlVelocidadeConsiderada;
	private Double vlVelocidadeLimite;
	private Double vlVelocidadeMedida;
	private Double vlVelocidadeTolerada;
	private Double vlComprimentoVeiculo;
	
	private ArrayList<OcorrenciaTrafego> ocorrencias;

	public MedicaoTrafego() { }

	public MedicaoTrafego(int cdMedicao,
			int cdEquipamento,
			int cdVia,
			int cdFaixa,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			int tpVeiculo,
			int qtVeiculos,
			Double vlVelocidadeConsiderada,
			Double vlVelocidadeLimite,
			Double vlVelocidadeMedida,
			Double vlVelocidadeTolerada,
			Double vlComprimentoVeiculo) {
		setCdMedicao(cdMedicao);
		setCdEquipamento(cdEquipamento);
		setCdVia(cdVia);
		setCdFaixa(cdFaixa);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setTpVeiculo(tpVeiculo);
		setQtVeiculos(qtVeiculos);
		setVlVelocidadeConsiderada(vlVelocidadeConsiderada);
		setVlVelocidadeLimite(vlVelocidadeLimite);
		setVlVelocidadeMedida(vlVelocidadeMedida);
		setVlVelocidadeTolerada(vlVelocidadeTolerada);
		setVlComprimentoVeiculo(vlComprimentoVeiculo);
	}
	
	public MedicaoTrafego(int cdMedicao,
			int cdEquipamento,
			int cdVia,
			int cdFaixa,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			int tpVeiculo,
			int qtVeiculos,
			Double vlVelocidadeConsiderada,
			Double vlVelocidadeLimite,
			Double vlVelocidadeMedida,
			Double vlVelocidadeTolerada,
			Double vlComprimentoVeiculo,
			
			ArrayList<OcorrenciaTrafego> ocorrencias) {
		
		setCdMedicao(cdMedicao);
		setCdEquipamento(cdEquipamento);
		setCdVia(cdVia);
		setCdFaixa(cdFaixa);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setTpVeiculo(tpVeiculo);
		setQtVeiculos(qtVeiculos);
		setVlVelocidadeConsiderada(vlVelocidadeConsiderada);
		setVlVelocidadeLimite(vlVelocidadeLimite);
		setVlVelocidadeMedida(vlVelocidadeMedida);
		setVlVelocidadeTolerada(vlVelocidadeTolerada);
		setVlComprimentoVeiculo(vlComprimentoVeiculo);
		
		setOcorrencias(ocorrencias);
	}
	
	public void setCdMedicao(int cdMedicao){
		this.cdMedicao=cdMedicao;
	}
	public int getCdMedicao(){
		return this.cdMedicao;
	}
	public void setCdEquipamento(int cdEquipamento){
		this.cdEquipamento=cdEquipamento;
	}
	public int getCdEquipamento(){
		return this.cdEquipamento;
	}
	public void setCdVia(int cdVia){
		this.cdVia=cdVia;
	}
	public int getCdVia(){
		return this.cdVia;
	}
	public void setCdFaixa(int cdFaixa){
		this.cdFaixa=cdFaixa;
	}
	public int getCdFaixa(){
		return this.cdFaixa;
	}
	public void setDtInicial(GregorianCalendar dtInicial){
		this.dtInicial=dtInicial;
	}
	public GregorianCalendar getDtInicial(){
		return this.dtInicial;
	}
	public void setDtFinal(GregorianCalendar dtFinal){
		this.dtFinal=dtFinal;
	}
	public GregorianCalendar getDtFinal(){
		return this.dtFinal;
	}
	public void setTpVeiculo(int tpVeiculo){
		this.tpVeiculo=tpVeiculo;
	}
	public int getTpVeiculo(){
		return this.tpVeiculo;
	}
	public void setQtVeiculos(int qtVeiculos){
		this.qtVeiculos=qtVeiculos;
	}
	public int getQtVeiculos(){
		return this.qtVeiculos;
	}
	public void setVlVelocidadeConsiderada(Double vlVelocidadeConsiderada){
		this.vlVelocidadeConsiderada=vlVelocidadeConsiderada;
	}
	public Double getVlVelocidadeConsiderada(){
		return this.vlVelocidadeConsiderada;
	}
	public void setVlVelocidadeLimite(Double vlVelocidadeLimite){
		this.vlVelocidadeLimite=vlVelocidadeLimite;
	}
	public Double getVlVelocidadeLimite(){
		return this.vlVelocidadeLimite;
	}
	public void setVlVelocidadeMedida(Double vlVelocidadeMedida){
		this.vlVelocidadeMedida=vlVelocidadeMedida;
	}
	public Double getVlVelocidadeMedida(){
		return this.vlVelocidadeMedida;
	}
	public void setVlVelocidadeTolerada(Double vlVelocidadeTolerada){
		this.vlVelocidadeTolerada=vlVelocidadeTolerada;
	}
	public Double getVlVelocidadeTolerada(){
		return this.vlVelocidadeTolerada;
	}
	public void setVlComprimentoVeiculo(Double vlComprimentoVeiculo){
		this.vlComprimentoVeiculo=vlComprimentoVeiculo;
	}
	public Double getVlComprimentoVeiculo(){
		return this.vlComprimentoVeiculo;
	}
	public ArrayList<OcorrenciaTrafego> getOcorrencias() {
		return ocorrencias;
	}

	public void setOcorrencias(ArrayList<OcorrenciaTrafego> ocorrencias) {
		this.ocorrencias = ocorrencias;
	}

	public String toString() {
		String valueToString = "";
		valueToString += "cdMedicao: " +  getCdMedicao();
		valueToString += ", cdEquipamento: " +  getCdEquipamento();
		valueToString += ", cdVia: " +  getCdVia();
		valueToString += ", cdFaixa: " +  getCdFaixa();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpVeiculo: " +  getTpVeiculo();
		valueToString += ", qtVeiculos: " +  getQtVeiculos();
		valueToString += ", vlVelocidadeConsiderada: " +  getVlVelocidadeConsiderada();
		valueToString += ", vlVelocidadeLimite: " +  getVlVelocidadeLimite();
		valueToString += ", vlVelocidadeMedida: " +  getVlVelocidadeMedida();
		valueToString += ", vlVelocidadeTolerada: " +  getVlVelocidadeTolerada();
		valueToString += ", vlComprimentoVeiculo: " +  getVlComprimentoVeiculo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MedicaoTrafego(getCdMedicao(),
			getCdEquipamento(),
			getCdVia(),
			getCdFaixa(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getTpVeiculo(),
			getQtVeiculos(),
			getVlVelocidadeConsiderada(),
			getVlVelocidadeLimite(),
			getVlVelocidadeMedida(),
			getVlVelocidadeTolerada(),
			getVlComprimentoVeiculo());
	}

}