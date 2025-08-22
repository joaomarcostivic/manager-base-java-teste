package com.tivic.manager.mob.pagamento.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.pagamento.BancoDadosRetorno;

public class BancoDadosRetornoBuilder {
	private BancoDadosRetorno bancoDadosRetorno;

	public BancoDadosRetornoBuilder() {
		bancoDadosRetorno = new BancoDadosRetorno();
	}

	public BancoDadosRetornoBuilder(BancoDadosRetorno bancoDadosRetorno) {
		this.bancoDadosRetorno = bancoDadosRetorno;
	}

	public BancoDadosRetornoBuilder setVlTarifa(double vlTarifa) {
		bancoDadosRetorno.setVlTarifa(vlTarifa);
		return this;
	}

	public BancoDadosRetornoBuilder setNrBanco(String nrBanco) {
		bancoDadosRetorno.setNrBanco(nrBanco);
		return this;
	}

	public BancoDadosRetornoBuilder setNrAgencia(String nrAgencia) {
		bancoDadosRetorno.setNrAgencia(nrAgencia);
		return this;
	}

	public BancoDadosRetornoBuilder setNrAgenciaCredito(String nrAgenciaCredito) {
		bancoDadosRetorno.setNrAgenciaCredito(nrAgenciaCredito);
		return this;
	}

	public BancoDadosRetornoBuilder setNrContaCredito(String nrContaCredito) {
		bancoDadosRetorno.setNrContaCredito(nrContaCredito);
		return this;
	}

	public BancoDadosRetornoBuilder setTpArrecadacao(int tpArrecadacao) {
		bancoDadosRetorno.setTpArrecadacao(tpArrecadacao);
		return this;
	}

	public BancoDadosRetornoBuilder setVlPago(double vlPago) {
		bancoDadosRetorno.setVlPago(vlPago);
		return this;
	}

	public BancoDadosRetornoBuilder setNrControleSemZeros(String nrControleSemZeros) {
		bancoDadosRetorno.setNrControleSemZeros(nrControleSemZeros);
		return this;
	}

	public BancoDadosRetornoBuilder setDtPagamento(GregorianCalendar stringDtPagamento) throws Exception {
		bancoDadosRetorno.setDtPagamento(stringDtPagamento);
		return this;
	}

	public BancoDadosRetornoBuilder setDtCredito(GregorianCalendar stringDtCredito) throws Exception {
		bancoDadosRetorno.setDtCredito(stringDtCredito);
		return this;
	}

	public BancoDadosRetornoBuilder setNrCodigoBarras(String nrCodigoBarra) {
		bancoDadosRetorno.setNrCodigoBarras(nrCodigoBarra);
		return this;
	}

	public BancoDadosRetornoBuilder setNrControleComZeros(String nrControleComZeros) {
		bancoDadosRetorno.setNrControleComZeros(nrControleComZeros);
		return this;
	}

	public BancoDadosRetornoBuilder setNrPlaca(String nrPlaca) {
		bancoDadosRetorno.setNrPlaca(nrPlaca);
		return this;
	}

	public BancoDadosRetornoBuilder setTpPagamento(int tpPagamento) {
		bancoDadosRetorno.setTpPagamento(tpPagamento);
		return this;
	}

	public BancoDadosRetorno build() {
		return bancoDadosRetorno;
	}
}