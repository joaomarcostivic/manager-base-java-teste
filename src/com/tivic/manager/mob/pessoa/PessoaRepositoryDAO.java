package com.tivic.manager.mob.pessoa;

import java.util.List;

import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class PessoaRepositoryDAO implements PessoaRepository {
	
	@Override
	public Pessoa insert(Pessoa pessoa, CustomConnection customConnection) throws Exception {
		int cdRetorno = PessoaDAO.insert(pessoa, customConnection.getConnection());
		if (cdRetorno <= 0) {
			throw new ValidacaoException("Erro ao inserir pessoa");
		}
		return pessoa;
	}

	@Override
	public Pessoa update(Pessoa pessoa, CustomConnection customConnection) throws Exception {
		int cdRetorno = PessoaDAO.update(pessoa, customConnection.getConnection());
		if (cdRetorno <= 0) {
			throw new ValidacaoException("Erro ao atualizar pessoa");
		}
		return pessoa;
	}

	@Override
	public Pessoa delete(Pessoa pessoa, CustomConnection customConnection) throws Exception {
		int cdRetorno = PessoaDAO.delete(pessoa.getCdPessoa(), customConnection.getConnection());
		if (cdRetorno <= 0) {
			throw new ValidacaoException("Erro ao remover pessoa");
		}
		return pessoa;
	}

	@Override
	public Pessoa get(int cdPessoa) throws Exception {
		return get(cdPessoa, new CustomConnection());
	}
	
	@Override
	public Pessoa get(int cdPessoa, CustomConnection customConnection) throws Exception {
		return PessoaDAO.get(cdPessoa, customConnection.getConnection());
	}

	@Override
	public List<Pessoa> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		ResultSetMapper<Pessoa> rsm = new ResultSetMapper<Pessoa>(PessoaDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), Pessoa.class);
		return rsm.toList();
	}
	
}
