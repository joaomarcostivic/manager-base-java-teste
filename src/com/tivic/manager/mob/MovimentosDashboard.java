package com.tivic.manager.mob;

public class MovimentosDashboard {

	private int qtdRegistroInfracao;
	private int qtdNai;
	private int qtdNip;

	public MovimentosDashboard() {
	}

	public MovimentosDashboard(int qtdRegistroInfracao, int qtdNai, int qtdNip) {
		super();
		this.qtdRegistroInfracao = qtdRegistroInfracao;
		this.qtdNai = qtdNai;
		this.qtdNip = qtdNip;
	}

	public int getQtdRegistroInfracao() {
		return qtdRegistroInfracao;
	}

	public void setQtdRegistroInfracao(int qtdRegistroInfracao) {
		this.qtdRegistroInfracao = qtdRegistroInfracao;
	}

	public int getQtdNai() {
		return qtdNai;
	}

	public void setQtdNai(int qtdNai) {
		this.qtdNai = qtdNai;
	}

	public int getQtdNip() {
		return qtdNip;
	}

	public void setQtdNip(int qtdNip) {
		this.qtdNip = qtdNip;
	}

}
