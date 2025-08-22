package com.tivic.manager.mob.concessaoveiculo.pessoa;

import java.sql.Connection;

import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.mob.ConcessaoVeiculoDTO;

import sol.util.Result;

public class ConcessaoVeiculoPessoaFisica implements ConcessaoVeiculoPessoa {

	@Override
	public void save(ConcessaoVeiculoDTO concessaoVeiculoDTO, Connection connect) throws Exception {
		PessoaFisica pessoaFisica = PessoaFisicaServices.getByCpf(concessaoVeiculoDTO.getNrCpfCnpj(), connect);
		if (pessoaFisica == null) {
			pessoaFisica = new PessoaFisica();
		}
		pessoaFisica.setNrCpf(concessaoVeiculoDTO.getNrCpfCnpj());
		pessoaFisica.setNmPessoa(concessaoVeiculoDTO.getPessoa().getNmPessoa());
		pessoaFisica.setGnPessoa(PessoaServices.TP_FISICA);
		pessoaFisica.setNrCelular(concessaoVeiculoDTO.getPessoa().getNrCelular());
		Result result = PessoaFisicaServices.save(pessoaFisica, concessaoVeiculoDTO.getPessoaEndereco(), connect);
		if (result.getCode() <= 0) {
			throw new Exception("Erro no salvamento de Pessoa Física.");
		}
		concessaoVeiculoDTO.getVeiculo().setCdProprietario(pessoaFisica.getCdPessoa());
	}

}
