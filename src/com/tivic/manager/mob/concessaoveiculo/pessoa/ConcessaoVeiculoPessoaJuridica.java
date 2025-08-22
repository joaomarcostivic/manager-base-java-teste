package com.tivic.manager.mob.concessaoveiculo.pessoa;

import java.sql.Connection;

import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.mob.ConcessaoVeiculoDTO;

import sol.util.Result;

public class ConcessaoVeiculoPessoaJuridica implements ConcessaoVeiculoPessoa {

	@Override
	public void save(ConcessaoVeiculoDTO concessaoVeiculoDTO, Connection connect) throws Exception {
		PessoaJuridica pessoaJuridica = PessoaJuridicaServices.getByCnpj(concessaoVeiculoDTO.getNrCpfCnpj(), connect);
		if (pessoaJuridica == null) {
			pessoaJuridica = new PessoaJuridica();
		}
		pessoaJuridica.setNrCnpj(concessaoVeiculoDTO.getNrCpfCnpj());
		pessoaJuridica.setNmPessoa(concessaoVeiculoDTO.getPessoa().getNmPessoa());
		pessoaJuridica.setGnPessoa(PessoaServices.TP_JURIDICA);
		pessoaJuridica.setNrCelular(concessaoVeiculoDTO.getPessoa().getNrCelular());
		Result result = PessoaJuridicaServices.save(pessoaJuridica, concessaoVeiculoDTO.getPessoaEndereco(), connect);
		if (result.getCode() <= 0) {
			throw new Exception("Erro no salvamento de Pessoa Jurídica.");
		}
		concessaoVeiculoDTO.getVeiculo().setCdProprietario(pessoaJuridica.getCdPessoa());
	}
}
