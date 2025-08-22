package com.tivic.manager.wsdl.detran.mg.consultarmovimentacoes;

import com.tivic.manager.util.JsonToStringBuilder;

public class Movimentacao {
	private Integer codigoMovimentacao;
	private String descricaoMovimentacao1;
	private String descricaoMovimentacao2;
	
	public Movimentacao() {
		// TODO Auto-generated constructor stub
	}

	public Integer getCodigoMovimentacao() {
		return codigoMovimentacao;
	}

	public void setCodigoMovimentacao(Integer codigoMovimentacao) {
		this.codigoMovimentacao = codigoMovimentacao;
	}

	public String getDescricaoMovimentacao1() {
		return descricaoMovimentacao1;
	}

	public void setDescricaoMovimentacao1(String descricaoMovimentacao1) {
		this.descricaoMovimentacao1 = descricaoMovimentacao1;
	}

	public String getDescricaoMovimentacao2() {
		return descricaoMovimentacao2;
	}

	public void setDescricaoMovimentacao2(String descricaoMovimentacao2) {
		this.descricaoMovimentacao2 = descricaoMovimentacao2;
	}
	
	@Override
	public String toString() {
		JsonToStringBuilder builder = new JsonToStringBuilder(this);
		builder.append("codigoMovimentacao", codigoMovimentacao);
		builder.append("descricaoMovimentacao1", descricaoMovimentacao1);
		builder.append("descricaoMovimentacao2", descricaoMovimentacao2);
		return builder.toString();
	}
}
