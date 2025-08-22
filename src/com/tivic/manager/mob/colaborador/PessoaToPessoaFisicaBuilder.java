package com.tivic.manager.mob.colaborador;

import java.util.GregorianCalendar;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaFisica;

public class PessoaToPessoaFisicaBuilder {
	private Pessoa pessoa;
	private PessoaFisica pessoaFisica;
	
	public PessoaToPessoaFisicaBuilder(ColaboradorDTO colaboradorDTO) {
		this.pessoa = colaboradorDTO.getPessoa();
		this.pessoaFisica = colaboradorDTO.getPessoaFisica();
		pupulate();
	}
	
	private void pupulate() {
		pessoaFisica.setCdPessoa(pessoa.getCdPessoa());
		pessoaFisica.setNmPessoa(pessoa.getNmPessoa());
		pessoaFisica.setNrCelular(pessoa.getNrCelular());
		pessoaFisica.setNmEmail(pessoa.getNmEmail());
		pessoaFisica.setDtCadastro(new GregorianCalendar());
		pessoaFisica.setNrTelefone1(pessoa.getNrTelefone1());
	}
	
	public PessoaFisica build() {
		return this.pessoaFisica;
	}
	
}
