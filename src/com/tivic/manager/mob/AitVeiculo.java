package com.tivic.manager.mob;

public class AitVeiculo {

	private int cdAitVeiculo;
	private int cdAit;
	private int cdEspecie;
	private int cdCor;
	private int cdTipo;
	private int cdCategoria;
	private int cdMarca;
	private int cdMarcaAutuacao;
	private String ufVeiculo;
	private String nrRenavam;
	private String dsAnoFabricacao;
	private String dsAnoModelo;
	private int cdVeiculo;

	public AitVeiculo(){ }

	public AitVeiculo(int cdAitVeiculo,
			int cdAit,
			int cdEspecie,
			int cdCor,
			int cdTipo,
			int cdCategoria,
			int cdMarca,
			int cdMarcaAutuacao,
			String ufVeiculo,
			String nrRenavam,
			String dsAnoFabricacao,
			String dsAnoModelo,
			int cdVeiculo){
		setCdAitVeiculo(cdAitVeiculo);
		setCdAit(cdAit);
		setCdEspecie(cdEspecie);
		setCdCor(cdCor);
		setCdTipo(cdTipo);
		setCdCategoria(cdCategoria);
		setCdMarca(cdMarca);
		setCdMarcaAutuacao(cdMarcaAutuacao);
		setUfVeiculo(ufVeiculo);
		setNrRenavam(nrRenavam);
		setDsAnoFabricacao(dsAnoFabricacao);
		setDsAnoModelo(dsAnoModelo);
		setCdVeiculo(cdVeiculo);
	}
	public void setCdAitVeiculo(int cdAitVeiculo){
		this.cdAitVeiculo=cdAitVeiculo;
	}
	public int getCdAitVeiculo(){
		return this.cdAitVeiculo;
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setCdEspecie(int cdEspecie){
		this.cdEspecie=cdEspecie;
	}
	public int getCdEspecie(){
		return this.cdEspecie;
	}
	public void setCdCor(int cdCor){
		this.cdCor=cdCor;
	}
	public int getCdCor(){
		return this.cdCor;
	}
	public void setCdTipo(int cdTipo){
		this.cdTipo=cdTipo;
	}
	public int getCdTipo(){
		return this.cdTipo;
	}
	public void setCdCategoria(int cdCategoria){
		this.cdCategoria=cdCategoria;
	}
	public int getCdCategoria(){
		return this.cdCategoria;
	}
	public void setCdMarca(int cdMarca){
		this.cdMarca=cdMarca;
	}
	public int getCdMarca(){
		return this.cdMarca;
	}
	public void setCdMarcaAutuacao(int cdMarcaAutuacao){
		this.cdMarcaAutuacao=cdMarcaAutuacao;
	}
	public int getCdMarcaAutuacao(){
		return this.cdMarcaAutuacao;
	}
	public void setUfVeiculo(String ufVeiculo){
		this.ufVeiculo=ufVeiculo;
	}
	public String getUfVeiculo(){
		return this.ufVeiculo;
	}
	public void setNrRenavam(String nrRenavam){
		this.nrRenavam=nrRenavam;
	}
	public String getNrRenavam(){
		return this.nrRenavam;
	}
	public void setDsAnoFabricacao(String dsAnoFabricacao){
		this.dsAnoFabricacao=dsAnoFabricacao;
	}
	public String getDsAnoFabricacao(){
		return this.dsAnoFabricacao;
	}
	public void setDsAnoModelo(String dsAnoModelo){
		this.dsAnoModelo=dsAnoModelo;
	}
	public String getDsAnoModelo(){
		return this.dsAnoModelo;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAitVeiculo: " +  getCdAitVeiculo();
		valueToString += ", cdAit: " +  getCdAit();
		valueToString += ", cdEspecie: " +  getCdEspecie();
		valueToString += ", cdCor: " +  getCdCor();
		valueToString += ", cdTipo: " +  getCdTipo();
		valueToString += ", cdCategoria: " +  getCdCategoria();
		valueToString += ", cdMarca: " +  getCdMarca();
		valueToString += ", cdMarcaAutuacao: " +  getCdMarcaAutuacao();
		valueToString += ", ufVeiculo: " +  getUfVeiculo();
		valueToString += ", cdRenavan: " +  getNrRenavam();
		valueToString += ", dsAnoFabricacao: " +  getDsAnoFabricacao();
		valueToString += ", dsAnoModelo: " +  getDsAnoModelo();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AitVeiculo(getCdAitVeiculo(),
			getCdAit(),
			getCdEspecie(),
			getCdCor(),
			getCdTipo(),
			getCdCategoria(),
			getCdMarca(),
			getCdMarcaAutuacao(),
			getUfVeiculo(),
			getNrRenavam(),
			getDsAnoFabricacao(),
			getDsAnoModelo(),
			getCdVeiculo());
	}

}
