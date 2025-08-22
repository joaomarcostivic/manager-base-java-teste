package com.tivic.manager.grl;

import java.util.List;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class PessoaFisicaRepositoryDAO implements IPessoaFisicaRepository {

	@Override
	public PessoaFisica insert(PessoaFisica pessoaFisica, CustomConnection customConnection) throws Exception {
		int cdRetorno = PessoaFisicaDAO.insert(pessoaFisica, customConnection.getConnection());
		if (cdRetorno <= 0) {
			throw new ValidacaoException("Erro ao inserir pessoa fisica");
		}
		return pessoaFisica;
	}

	@Override
	public void insertCodeSync(PessoaFisica pessoaFisica, CustomConnection customConnection) throws Exception {
		PessoaFisicaDAO.insert(pessoaFisica, customConnection.getConnection(), true);
	}
	
	@Override
	public PessoaFisica update(PessoaFisica pessoaFisica, CustomConnection customConnection) throws Exception {
		int cdRetorno = PessoaFisicaDAO.update(pessoaFisica, customConnection.getConnection());
		if (cdRetorno <= 0) {
			throw new ValidacaoException("Erro ao atualizar pessoa fisica");
		}
		return pessoaFisica;
	}

	@Override
	public PessoaFisica delete(PessoaFisica pessoaFisica, CustomConnection customConnection) throws Exception {
		int cdRetorno = PessoaFisicaDAO.delete(pessoaFisica.getCdPessoa(), customConnection.getConnection());
		if (cdRetorno <= 0) {
			throw new ValidacaoException("Erro ao remover pessoa fisica");
		}
		return pessoaFisica;
	}
	
	@Override
	public PessoaFisica get(int cdPessoaFisica) throws Exception {
		return get(cdPessoaFisica, new CustomConnection());
	}

	@Override
	public PessoaFisica get(int cdPessoaFisica, CustomConnection customConnection) throws Exception {
		return PessoaFisicaDAO.get(cdPessoaFisica, customConnection.getConnection());
	}

	@Override
	public List<PessoaFisica> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception {
		ResultSetMapper<PessoaFisica> rsm = new ResultSetMapper<PessoaFisica>(PessoaFisicaDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), PessoaFisica.class);
		return rsm.toList();
	}
	
	@Override
	public PessoaFisica getByCpf(String nrCpfPessoaFisica, CustomConnection customConnection) throws Exception {
		return PessoaFisicaDAO.getByCpf(nrCpfPessoaFisica);
	}

}