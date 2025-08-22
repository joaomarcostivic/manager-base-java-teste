package com.tivic.manager.mob;

import com.tivic.manager.grl.Pessoa;

public class ConcessaoDTO {
	
	private Concessao concessao;
	private ConcessionarioPessoa concessionarioPessoa;
	private Pessoa pessoa;
	
	public ConcessaoDTO() {
		super();
	}

	public ConcessaoDTO(Concessao concessao, ConcessionarioPessoa concessionarioPessoa, Pessoa pessoa) {
		super();
		this.concessao = concessao;
		this.concessionarioPessoa = concessionarioPessoa;
		this.pessoa = pessoa;
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

	public Concessao getConcessao() {
		return concessao;
	}

	public void setConcessao(Concessao concessao) {
		this.concessao = concessao;
	}


}