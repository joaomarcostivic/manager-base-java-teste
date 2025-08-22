package com.tivic.manager.mob;

public class ConcessaoLote {

	private int cdConcessaoLote;
	private int cdConcessao;
	private int nrLote;
	private int tpVeiculo;
	private int nrCapacidadeTipoVeiculo;
	private int vlKmMes;
	private Double vlUnitarioKm;
	private Double vlMensal;
	private String txtHistorico;
	private int tpTransportados;
	private int tpTurno;
	private int cdLinha;
	private int cdDistrito;
	private int cdCidade;

	public ConcessaoLote() { }

	public ConcessaoLote(int cdConcessaoLote,
			int cdConcessao,
			int nrLote,
			int tpVeiculo,
			int nrCapacidadeTipoVeiculo,
			int vlKmMes,
			Double vlUnitarioKm,
			Double vlMensal,
			String txtHistorico,
			int tpTransportados,
			int tpTurno,
			int cdLinha,
			int cdDistrito,
			int cdCidade) {
		setCdConcessaoLote(cdConcessaoLote);
		setCdConcessao(cdConcessao);
		setNrLote(nrLote);
		setTpVeiculo(tpVeiculo);
		setNrCapacidadeTipoVeiculo(nrCapacidadeTipoVeiculo);
		setVlKmMes(vlKmMes);
		setVlUnitarioKm(vlUnitarioKm);
		setVlMensal(vlMensal);
		setTxtHistorico(txtHistorico);
		setTpTransportados(tpTransportados);
		setTpTurno(tpTurno);
		setCdLinha(cdLinha);
		setCdDistrito(cdDistrito);
		setCdCidade(cdCidade);
	}
	public void setCdConcessaoLote(int cdConcessaoLote){
		this.cdConcessaoLote=cdConcessaoLote;
	}
	public int getCdConcessaoLote(){
		return this.cdConcessaoLote;
	}
	public void setCdConcessao(int cdConcessao){
		this.cdConcessao=cdConcessao;
	}
	public int getCdConcessao(){
		return this.cdConcessao;
	}
	public void setNrLote(int nrLote){
		this.nrLote=nrLote;
	}
	public int getNrLote(){
		return this.nrLote;
	}
	public void setTpVeiculo(int tpVeiculo){
		this.tpVeiculo=tpVeiculo;
	}
	public int getTpVeiculo(){
		return this.tpVeiculo;
	}
	public void setNrCapacidadeTipoVeiculo(int nrCapacidadeTipoVeiculo){
		this.nrCapacidadeTipoVeiculo=nrCapacidadeTipoVeiculo;
	}
	public int getNrCapacidadeTipoVeiculo(){
		return this.nrCapacidadeTipoVeiculo;
	}
	public void setVlKmMes(int vlKmMes){
		this.vlKmMes=vlKmMes;
	}
	public int getVlKmMes(){
		return this.vlKmMes;
	}
	public void setVlUnitarioKm(Double vlUnitarioKm){
		this.vlUnitarioKm=vlUnitarioKm;
	}
	public Double getVlUnitarioKm(){
		return this.vlUnitarioKm;
	}
	public void setVlMensal(Double vlMensal){
		this.vlMensal=vlMensal;
	}
	public Double getVlMensal(){
		return this.vlMensal;
	}
	public void setTxtHistorico(String txtHistorico){
		this.txtHistorico=txtHistorico;
	}
	public String getTxtHistorico(){
		return this.txtHistorico;
	}
	public void setTpTransportados(int tpTransportados){
		this.tpTransportados=tpTransportados;
	}
	public int getTpTransportados(){
		return this.tpTransportados;
	}
	public void setTpTurno(int tpTurno){
		this.tpTurno=tpTurno;
	}
	public int getTpTurno(){
		return this.tpTurno;
	}
	public void setCdLinha(int cdLinha){
		this.cdLinha=cdLinha;
	}
	public int getCdLinha(){
		return this.cdLinha;
	}
	public void setCdDistrito(int cdDistrito){
		this.cdDistrito=cdDistrito;
	}
	public int getCdDistrito(){
		return this.cdDistrito;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConcessaoLote: " +  getCdConcessaoLote();
		valueToString += ", cdConcessao: " +  getCdConcessao();
		valueToString += ", nrLote: " +  getNrLote();
		valueToString += ", tpVeiculo: " +  getTpVeiculo();
		valueToString += ", nrCapacidadeTipoVeiculo: " +  getNrCapacidadeTipoVeiculo();
		valueToString += ", vlKmMes: " +  getVlKmMes();
		valueToString += ", vlUnitarioKm: " +  getVlUnitarioKm();
		valueToString += ", vlMensal: " +  getVlMensal();
		valueToString += ", txtHistorico: " +  getTxtHistorico();
		valueToString += ", tpTransportados: " +  getTpTransportados();
		valueToString += ", tpTurno: " +  getTpTurno();
		valueToString += ", cdLinha: " +  getCdLinha();
		valueToString += ", cdDistrito: " +  getCdDistrito();
		valueToString += ", cdCidade: " +  getCdCidade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ConcessaoLote(getCdConcessaoLote(),
			getCdConcessao(),
			getNrLote(),
			getTpVeiculo(),
			getNrCapacidadeTipoVeiculo(),
			getVlKmMes(),
			getVlUnitarioKm(),
			getVlMensal(),
			getTxtHistorico(),
			getTpTransportados(),
			getTpTurno(),
			getCdLinha(),
			getCdDistrito(),
			getCdCidade());
	}

}