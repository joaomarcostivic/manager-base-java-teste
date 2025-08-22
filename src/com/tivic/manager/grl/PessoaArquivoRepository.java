package com.tivic.manager.grl;

import java.util.List;

import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class PessoaArquivoRepository implements IPessoaArquivoRepository {

	@Override
	public PessoaArquivo insert(PessoaArquivo pessoaArquivo, CustomConnection customConnection) throws Exception {
		int codigoRetorno = PessoaArquivoDAO.insert(pessoaArquivo, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao salvar o arquivo de pessoa.");
		return pessoaArquivo;	}
	
	@Override
	public PessoaArquivo update(PessoaArquivo pessoaArquivo, CustomConnection customConnection) throws Exception {
		int codigoRetorno = PessoaArquivoDAO.update(pessoaArquivo, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao atualizar arquivo de pessoa.");
		return pessoaArquivo;
	}

	@Override
	public void delete(int cdArquivo, int cdPessoa, CustomConnection customConnection) throws Exception {
		int codigoRetorno = PessoaArquivoDAO.delete(cdArquivo, cdPessoa, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao remover arquivo de pessoa.");
	}

	@Override
	public PessoaArquivo get(int cdArquivo, int cdPessoa) throws Exception {
		return get(cdArquivo, cdPessoa, new CustomConnection());
	}

	@Override
	public PessoaArquivo get(int cdArquivo, int cdPessoa, CustomConnection customConnection) throws Exception {
		return PessoaArquivoDAO.get(cdArquivo, cdPessoa, customConnection.getConnection());
	}

	@Override
	public List<PessoaArquivo> find(SearchCriterios searchCriterios) throws IllegalArgumentException, Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<PessoaArquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception{
		ResultSetMapper<PessoaArquivo> rsm = new ResultSetMapper<PessoaArquivo>(PessoaArquivoDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), PessoaArquivo.class);
		return rsm.toList();
	}
	
}
