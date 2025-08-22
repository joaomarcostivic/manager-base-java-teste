package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class OcorrenciaTrafego {

	private int cdOcorrencia;
	private int cdMedicao;
	private int cdEquipamento;
	private int cdVia;
	private int cdFaixa;
	private GregorianCalendar dtOcorrencia;
	private String nrPlaca;
	private Double vlVelocidadeMedida;
	private Double vlComprimentoVeiculo;
	private int tpOcorrencia;

	public OcorrenciaTrafego() { }

	public OcorrenciaTrafego(int cdOcorrencia,
			int cdMedicao,
			int cdEquipamento,
			int cdVia,
			int cdFaixa,
			GregorianCalendar dtOcorrencia,
			String nrPlaca,
			Double vlVelocidadeMedida,
			Double vlComprimentoVeiculo,
			int tpOcorrencia) {
		setCdOcorrencia(cdOcorrencia);
		setCdMedicao(cdMedicao);
		setCdEquipamento(cdEquipamento);
		setCdVia(cdVia);
		setCdFaixa(cdFaixa);
		setDtOcorrencia(dtOcorrencia);
		setNrPlaca(nrPlaca);
		setVlVelocidadeMedida(vlVelocidadeMedida);
		setVlComprimentoVeiculo(vlComprimentoVeiculo);
		setTpOcorrencia(tpOcorrencia);
	}
	public void setCdOcorrencia(int cdOcorrencia){
		this.cdOcorrencia=cdOcorrencia;
	}
	public int getCdOcorrencia(){
		return this.cdOcorrencia;
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
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia){
		this.dtOcorrencia=dtOcorrencia;
	}
	public GregorianCalendar getDtOcorrencia(){
		return this.dtOcorrencia;
	}
	public void setNrPlaca(String nrPlaca){
		this.nrPlaca=nrPlaca;
	}
	public String getNrPlaca(){
		return this.nrPlaca;
	}
	public void setVlVelocidadeMedida(Double vlVelocidadeMedida){
		this.vlVelocidadeMedida=vlVelocidadeMedida;
	}
	public Double getVlVelocidadeMedida(){
		return this.vlVelocidadeMedida;
	}
	public void setVlComprimentoVeiculo(Double vlComprimentoVeiculo){
		this.vlComprimentoVeiculo=vlComprimentoVeiculo;
	}
	public Double getVlComprimentoVeiculo(){
		return this.vlComprimentoVeiculo;
	}
	public void setTpOcorrencia(int tpOcorrencia){
		this.tpOcorrencia=tpOcorrencia;
	}
	public int getTpOcorrencia(){
		return this.tpOcorrencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdMedicao: " +  getCdMedicao();
		valueToString += ", cdEquipamento: " +  getCdEquipamento();
		valueToString += ", cdVia: " +  getCdVia();
		valueToString += ", cdFaixa: " +  getCdFaixa();
		valueToString += ", dtOcorrencia: " +  sol.util.Util.formatDateTime(getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrPlaca: " +  getNrPlaca();
		valueToString += ", vlVelocidadeMedida: " +  getVlVelocidadeMedida();
		valueToString += ", vlComprimentoVeiculo: " +  getVlComprimentoVeiculo();
		valueToString += ", tpOcorrencia: " +  getTpOcorrencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OcorrenciaTrafego(getCdOcorrencia(),
			getCdMedicao(),
			getCdEquipamento(),
			getCdVia(),
			getCdFaixa(),
			getDtOcorrencia()==null ? null : (GregorianCalendar)getDtOcorrencia().clone(),
			getNrPlaca(),
			getVlVelocidadeMedida(),
			getVlComprimentoVeiculo(),
			getTpOcorrencia());
	}

}