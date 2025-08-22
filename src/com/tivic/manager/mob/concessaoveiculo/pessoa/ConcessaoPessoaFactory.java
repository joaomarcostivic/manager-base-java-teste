package com.tivic.manager.mob.concessaoveiculo.pessoa;

public class ConcessaoPessoaFactory {
	
	private static final int TAMANHO_CPF = 11;
	private static final int TAMANHO_CNPJ = 14;
	
	public ConcessaoVeiculoPessoa build(String nrCpfCnpj) throws Exception {
		if(nrCpfCnpj == null) {
			throw new Exception("CPF/CNPJ não foi passado.");
		}
		switch(nrCpfCnpj.length()) {
			case TAMANHO_CPF: 
				return new ConcessaoVeiculoPessoaFisica();
		case TAMANHO_CNPJ:
			return new ConcessaoVeiculoPessoaJuridica();
		}
		throw new Exception("Tamanho de CPF/CNPJ inválido.");
	}
}
