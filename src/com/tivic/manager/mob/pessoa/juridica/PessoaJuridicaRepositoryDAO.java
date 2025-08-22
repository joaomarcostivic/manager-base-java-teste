package com.tivic.manager.mob.pessoa.juridica;

import java.util.List;

import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class PessoaJuridicaRepositoryDAO implements PessoaJuridicaRepository {
	
	@Override
	public PessoaJuridica insert(PessoaJuridica pessoaJuridica, CustomConnection customConnection) throws Exception {
		int cdRetorno = PessoaJuridicaDAO.insert(pessoaJuridica, customConnection.getConnection());
		if (cdRetorno <= 0) {
			throw new ValidacaoException("Erro ao inserir pessoa juridica");
		}
		return pessoaJuridica;
	}
	
	@Override
	public PessoaJuridica update(PessoaJuridica pessoaJuridica, CustomConnection customConnection) throws Exception {
		int cdRetorno = PessoaJuridicaDAO.update(pessoaJuridica, customConnection.getConnection());
		if (cdRetorno <= 0) {
			throw new ValidacaoException("Erro ao atualizar pessoa juridica");
		}
		return pessoaJuridica;
	}

	@Override
	public PessoaJuridica delete(PessoaJuridica pessoaJuridica, CustomConnection customConnection) throws Exception {
		int cdRetorno = PessoaJuridicaDAO.delete(pessoaJuridica.getCdPessoa(), customConnection.getConnection());
		if (cdRetorno <= 0) {
			throw new ValidacaoException("Erro ao remover pessoa juridica");
		}
		return pessoaJuridica;
	}
	
	@Override
	public PessoaJuridica get(int cdPessoaJuridica) throws Exception {
		return get(cdPessoaJuridica, new CustomConnection());
	}

	@Override
	public PessoaJuridica get(int cdPessoaJuridica, CustomConnection customConnection) throws Exception {
		return PessoaJuridicaDAO.get(cdPessoaJuridica, customConnection.getConnection());
	}

	@Override
	public List<PessoaJuridica> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception {
		ResultSetMapper<PessoaJuridica> rsm = new ResultSetMapper<PessoaJuridica>(PessoaJuridicaDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), PessoaJuridica.class);
		return rsm.toList();
	}
}
