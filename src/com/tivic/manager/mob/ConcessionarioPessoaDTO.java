package com.tivic.manager.mob;

import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaFisica;

public class ConcessionarioPessoaDTO {
		
	private ConcessionarioPessoa concessionarioPessoa;
	private Pessoa pessoa;
	private PessoaFisica pessoaFisica;
	private PessoaEndereco pessoaEndereco;
	private int cdEmpresa;
	private int cdVinculo;
	
	public ConcessionarioPessoaDTO() {
		super();
	}

	public ConcessionarioPessoaDTO(ConcessionarioPessoa concessionarioPessoa, Pessoa pessoa, PessoaFisica pessoaFisica,
			PessoaEndereco pessoaEndereco, int cdEmpresa, int cdVinculo) {
		super();
		this.concessionarioPessoa = concessionarioPessoa;
		this.pessoa = pessoa;
		this.pessoaFisica = pessoaFisica;
		this.pessoaEndereco = pessoaEndereco;
		this.cdEmpresa = cdEmpresa;
		this.cdVinculo = cdVinculo;
	}

	public ConcessionarioPessoa getConcessionarioPessoa() {
		return concessionarioPessoa;
	}

	public void setConcessionarioPessoa(ConcessionarioPessoa concessionarioPessoa) {
		this.concessionarioPessoa = concessionarioPessoa;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	public PessoaEndereco getPessoaEndereco() {
		return pessoaEndereco;
	}

	public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
		this.pessoaEndereco = pessoaEndereco;
	}

	public int getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(int cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public int getCdVinculo() {
		return cdVinculo;
	}

	public void setCdVinculo(int cdVinculo) {
		this.cdVinculo = cdVinculo;
	}


}