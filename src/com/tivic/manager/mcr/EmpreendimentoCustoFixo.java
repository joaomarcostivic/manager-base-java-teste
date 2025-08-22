package com.tivic.manager.mcr;

public class EmpreendimentoCustoFixo {

	private int cdEmpreendimento;
	private float vlMaoObra;
	private float vlMaoObraSemCustos;
	private float vlAguaLuz;
	private float vlVeiculo;
	private float vlContador;
	private float vlRetirada;
	private float vlManutencao;
	private float vlAluguel;

	public EmpreendimentoCustoFixo(int cdEmpreendimento,
			float vlMaoObra,
			float vlMaoObraSemCustos,
			float vlAguaLuz,
			float vlVeiculo,
			float vlContador,
			float vlRetirada,
			float vlManutencao,
			float vlAluguel){
		setCdEmpreendimento(cdEmpreendimento);
		setVlMaoObra(vlMaoObra);
		setVlMaoObraSemCustos(vlMaoObraSemCustos);
		setVlAguaLuz(vlAguaLuz);
		setVlVeiculo(vlVeiculo);
		setVlContador(vlContador);
		setVlRetirada(vlRetirada);
		setVlManutencao(vlManutencao);
		setVlAluguel(vlAluguel);
	}
	public void setCdEmpreendimento(int cdEmpreendimento){
		this.cdEmpreendimento=cdEmpreendimento;
	}
	public int getCdEmpreendimento(){
		return this.cdEmpreendimento;
	}
	public void setVlMaoObra(float vlMaoObra){
		this.vlMaoObra=vlMaoObra;
	}
	public float getVlMaoObra(){
		return this.vlMaoObra;
	}
	public void setVlMaoObraSemCustos(float vlMaoObraSemCustos){
		this.vlMaoObraSemCustos=vlMaoObraSemCustos;
	}
	public float getVlMaoObraSemCustos(){
		return this.vlMaoObraSemCustos;
	}
	public void setVlAguaLuz(float vlAguaLuz){
		this.vlAguaLuz=vlAguaLuz;
	}
	public float getVlAguaLuz(){
		return this.vlAguaLuz;
	}
	public void setVlVeiculo(float vlVeiculo){
		this.vlVeiculo=vlVeiculo;
	}
	public float getVlVeiculo(){
		return this.vlVeiculo;
	}
	public void setVlContador(float vlContador){
		this.vlContador=vlContador;
	}
	public float getVlContador(){
		return this.vlContador;
	}
	public void setVlRetirada(float vlRetirada){
		this.vlRetirada=vlRetirada;
	}
	public float getVlRetirada(){
		return this.vlRetirada;
	}
	public void setVlManutencao(float vlManutencao){
		this.vlManutencao=vlManutencao;
	}
	public float getVlManutencao(){
		return this.vlManutencao;
	}
	public void setVlAluguel(float vlAluguel){
		this.vlAluguel=vlAluguel;
	}
	public float getVlAluguel(){
		return this.vlAluguel;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpreendimento: " +  getCdEmpreendimento();
		valueToString += ", vlMaoObra: " +  getVlMaoObra();
		valueToString += ", vlMaoObraSemCustos: " +  getVlMaoObraSemCustos();
		valueToString += ", vlAguaLuz: " +  getVlAguaLuz();
		valueToString += ", vlVeiculo: " +  getVlVeiculo();
		valueToString += ", vlContador: " +  getVlContador();
		valueToString += ", vlRetirada: " +  getVlRetirada();
		valueToString += ", vlManutencao: " +  getVlManutencao();
		valueToString += ", vlAluguel: " +  getVlAluguel();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EmpreendimentoCustoFixo(cdEmpreendimento,
			vlMaoObra,
			vlMaoObraSemCustos,
			vlAguaLuz,
			vlVeiculo,
			vlContador,
			vlRetirada,
			vlManutencao,
			vlAluguel);
	}

}