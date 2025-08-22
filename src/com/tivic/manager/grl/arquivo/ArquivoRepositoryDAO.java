package com.tivic.manager.grl.arquivo;

import java.util.List;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class ArquivoRepositoryDAO implements IArquivoRepository {

	@Override
	public Arquivo insert(Arquivo arquivo, CustomConnection customConnection) throws Exception {
		int cdArquivo = ArquivoDAO.insert(arquivo, customConnection.getConnection());
		if (cdArquivo <= 0) 
			throw new ValidacaoException("Erro ao inserir arquivo.");
		arquivo.setCdArquivo(cdArquivo);
		return arquivo;
	}
	
	@Override
	public void insertCodeSync(Arquivo arquivo, CustomConnection customConnection) throws Exception {
		ArquivoDAO.insert(arquivo, customConnection.getConnection(), true);
	}

	@Override
	public Arquivo update(Arquivo arquivo, CustomConnection customConnection) throws Exception {
		int codigoRetorno = ArquivoDAO.update(arquivo, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao atualizar arquivo.");
		return arquivo;
	}
	
	@Override
	public void delete(Integer cdArquivo, CustomConnection customConnection) throws Exception {
		int codigoRetorno = ArquivoDAO.delete(cdArquivo, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao remover arquivo.");
	}
	
	@Override
	public Arquivo get(int cdArquivo) throws Exception {
		return get(cdArquivo, new CustomConnection());
	}

	@Override
	public Arquivo get(int cdArquivo, CustomConnection customConnection) throws Exception {
		return ArquivoDAO.get(cdArquivo, customConnection.getConnection());
	}
	
	@Override
	public List<Arquivo> find(SearchCriterios searchCriterios) throws IllegalArgumentException, Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Arquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception {
		ResultSetMapper<Arquivo> rsm = new ResultSetMapper<Arquivo>(ArquivoDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), Arquivo.class);
		return rsm.toList();
	}

}
