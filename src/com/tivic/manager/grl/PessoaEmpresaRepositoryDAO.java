package com.tivic.manager.grl;

import java.util.List;

import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class PessoaEmpresaRepositoryDAO implements IPessoaEmpresaRepository {

	@Override
	public PessoaEmpresa insert(PessoaEmpresa pessoaEmpresa, CustomConnection customConnection) throws Exception {
		int cdRetorno = PessoaEmpresaDAO.insert(pessoaEmpresa, customConnection.getConnection());
		if (cdRetorno <= 0) {
			throw new ValidacaoException("Erro ao inserir empresa");
		}
		return pessoaEmpresa;
	}

	@Override
	public PessoaEmpresa update(PessoaEmpresa pessoaEmpresa, CustomConnection customConnection) throws Exception {
		int cdRetorno = PessoaEmpresaDAO.update(pessoaEmpresa, customConnection.getConnection());
		if (cdRetorno <= 0) {
			throw new ValidacaoException("Erro ao atualizar empresa");
		}
		return pessoaEmpresa;
	}

	@Override
	public PessoaEmpresa delete(PessoaEmpresa pessoaEmpresa, CustomConnection customConnection) throws Exception {
		int cdRetorno = PessoaEmpresaDAO.delete(pessoaEmpresa.getCdEmpresa(), pessoaEmpresa.getCdPessoa(), pessoaEmpresa.getCdVinculo(), customConnection.getConnection());
		if (cdRetorno <= 0) {
			throw new ValidacaoException("Erro ao remover empresa");
		}
		return pessoaEmpresa;
	}
	
	@Override
	public PessoaEmpresa get(int cdPessoaEmpresa, int cdPessoa, int cdVinculo) throws Exception {
		return get(cdPessoaEmpresa, cdPessoa, cdVinculo, new CustomConnection());
	}

	@Override
	public PessoaEmpresa get(int cdPessoaEmpresa, int cdPessoa, int cdVinculo, CustomConnection customConnection) throws Exception {
		PessoaEmpresa pessoaEmpresa = PessoaEmpresaDAO.get(cdPessoaEmpresa, cdPessoa, cdVinculo, customConnection.getConnection());
		if (pessoaEmpresa.getCdPessoa() <= 0) {
			throw new ValidacaoException("Empresa não encontrada.");
		}
		return pessoaEmpresa;
	}

	@Override
	public List<PessoaEmpresa> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		ResultSetMapper<PessoaEmpresa> rsm = new ResultSetMapper<PessoaEmpresa>(PessoaEmpresaDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), PessoaEmpresa.class);
		return rsm.toList();
	}

}
