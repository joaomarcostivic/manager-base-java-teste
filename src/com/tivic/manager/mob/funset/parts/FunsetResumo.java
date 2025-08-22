package com.tivic.manager.mob.funset.parts;

import com.tivic.manager.util.Util;

public class FunsetResumo {

	private String tipoRegistro;
	private int quantidadeMultas;
	private double totalArrecadacao;
	private double totalRepasseFunset;
	private double totalRepasseRenainf;
	private int quantidadeRestituicoes;
	private double valorTotalRestituido;
	
	public FunsetResumo(String tipoRegistro) {
		setTipoRegistro(tipoRegistro);
	}

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public int getQuantidadeMultas() {
		return quantidadeMultas;
	}

	public void setQuantidadeMultas(int quantidadeMultas) {
		this.quantidadeMultas = quantidadeMultas;
	}
	
	public void addQuantidadeMultas() {
		this.quantidadeMultas++;
	}

	public double getTotalArrecadacao() {
		return totalArrecadacao;
	}

	public void setTotalArrecadacao(double totalArrecadacao) {
		this.totalArrecadacao = totalArrecadacao;
	}
	
	public void addTotalArrecadacao(double valorArrecadacao) {
		this.totalArrecadacao += valorArrecadacao;
	}

	public double getTotalRepasseFunset() {
		return totalRepasseFunset;
	}

	public void setTotalRepasseFunset(double totalRepasseFunset) {
		this.totalRepasseFunset = totalRepasseFunset;
	}

	public void addTotalRepasseFunset(double valorRepasseFunset) {
		this.totalRepasseFunset += valorRepasseFunset;
	}

	public double getTotalRepasseRenainf() {
		return totalRepasseRenainf;
	}

	public void setTotalRepasseRenainf(double totalRepasseRenainf) {
		this.totalRepasseRenainf = totalRepasseRenainf;
	}

	public void addTotalRepasseRenainf(double valorRepasseRenainf) {
		this.totalRepasseRenainf += valorRepasseRenainf;
	}

	public int getQuantidadeRestituicoes() {
		return quantidadeRestituicoes;
	}

	public void setQuantidadeRestituicoes(int quantidadeRestituicoes) {
		this.quantidadeRestituicoes = quantidadeRestituicoes;
	}

	public void addQuantidadeRestituicoes() {
		this.quantidadeRestituicoes++;
	}

	public double getValorTotalRestituido() {
		return valorTotalRestituido;
	}

	public void setValorTotalRestituido(double valorTotalRestituido) {
		this.valorTotalRestituido = valorTotalRestituido;
	}

	public void addTotalRestituido(double valorRestituido) {
		this.valorTotalRestituido += valorRestituido;
	}

	public String build() {
		return getTipoRegistro() + 
			  Util.fill(String.valueOf(getQuantidadeMultas()), 8, '0', 'E') +
			  Util.fill(Util.formatNumber(getTotalArrecadacao(), 2).replace(",", ""), 11, '0', 'E') +
			  Util.fill(Util.formatNumber(getTotalRepasseFunset(), 2).replace(",", ""), 9, '0', 'E') +
			  Util.fill(Util.formatNumber(getTotalRepasseRenainf(), 2).replace(",", ""), 9, '0', 'E') +
			  Util.fill(String.valueOf(getQuantidadeRestituicoes()), 8, '0', 'E') +
			  Util.fill(Util.formatNumber(getValorTotalRestituido(), 2).replace(",", ""), 11, '0', 'E');
			  
	}
}
