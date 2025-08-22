package com.tivic.manager.grl;

import java.util.List;

import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class PessoaEnderecoRepositoryDAO implements IPessoaEnderecoRepository {

	@Override
	public PessoaEndereco insert(PessoaEndereco pessoaEndereco, CustomConnection customConnection) throws Exception {
		int cdRetorno = PessoaEnderecoDAO.insert(pessoaEndereco, customConnection.getConnection());
		if (cdRetorno <= 0) {
			throw new ValidacaoException("Erro ao inserir endereço da pessoa");
		}
		return pessoaEndereco;
	}
	
	@Override
	public void insertCodeSync(PessoaEndereco pessoaEndereco, CustomConnection customConnection) throws Exception {
		PessoaEnderecoDAO.insert(pessoaEndereco, customConnection.getConnection(), true);
	}

	@Override
	public PessoaEndereco update(PessoaEndereco pessoaEndereco, CustomConnection customConnection) throws Exception {
		int cdRetorno = PessoaEnderecoDAO.update(pessoaEndereco, customConnection.getConnection());
		if (cdRetorno <= 0) {
			throw new ValidacaoException("Erro ao atualizar endereço da pessoa");
		}
		return pessoaEndereco;
	}

	@Override
	public PessoaEndereco delete(PessoaEndereco pessoaEndereco, CustomConnection customConnection) throws Exception {
		int cdRetorno = PessoaEnderecoDAO.insert(pessoaEndereco, customConnection.getConnection());
		if (cdRetorno <= 0) {
			throw new ValidacaoException("Erro ao remover endereço da pessoa");
		}
		return pessoaEndereco;
	}
	
	@Override
	public PessoaEndereco get(int cdPessoaEndereco) throws Exception {
		return get(cdPessoaEndereco, new CustomConnection());
	}

	@Override
	public PessoaEndereco get(int cdPessoaEndereco, CustomConnection customConnection) throws Exception {
		PessoaEndereco pessoaEndereco = PessoaEnderecoDAO.get(cdPessoaEndereco, customConnection.getConnection());
		if (pessoaEndereco.getCdPessoa() <= 0) {
			throw new ValidacaoException("Endereço não encontrada.");
		}
		return pessoaEndereco;
	}

	@Override
	public List<PessoaEndereco> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception {
		ResultSetMapper<PessoaEndereco> rsm = new ResultSetMapper<PessoaEndereco>(PessoaFisicaDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), PessoaEndereco.class);
		return rsm.toList();
	}

}