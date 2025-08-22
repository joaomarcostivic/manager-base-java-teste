package com.tivic.manager.grl.pessoavinculohistorico.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.pessoavinculohistorico.PessoaVinculoHistorico;

public class PessoaVinculoHistoricoBuilder {
	private PessoaVinculoHistorico pessoaVinculoHistorico;
	
	public PessoaVinculoHistoricoBuilder() {
		pessoaVinculoHistorico = new PessoaVinculoHistorico();
	}
	
	public PessoaVinculoHistoricoBuilder addCdPessoa(int cdPessoa) {
		this.pessoaVinculoHistorico.setCdPessoa(cdPessoa);
		return this;
	}
	
	public PessoaVinculoHistoricoBuilder addCdVinculo(int cdVinculo) {
		this.pessoaVinculoHistorico.setCdVinculo(cdVinculo);
		return this;
	}
	
	public PessoaVinculoHistoricoBuilder addCdUsuario(int cdUsuario) {
		this.pessoaVinculoHistorico.setCdUsuario(cdUsuario);
		return this;
	}
	
	public PessoaVinculoHistoricoBuilder addStVinculo(int stVinculo) {
		this.pessoaVinculoHistorico.setStVinculo(stVinculo);
		return this;
	}
	
	public PessoaVinculoHistoricoBuilder addDtVinculoHistorico(GregorianCalendar dtVinculoHistorico) {
		this.pessoaVinculoHistorico.setDtVinculoHistorico(dtVinculoHistorico);
		return this;
	}
	
	public PessoaVinculoHistorico build() {
		return this.pessoaVinculoHistorico;
	}
}
