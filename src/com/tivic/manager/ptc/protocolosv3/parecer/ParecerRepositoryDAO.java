package com.tivic.manager.ptc.protocolosv3.parecer;

import java.sql.SQLException;
import java.util.List;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ParecerRepositoryDAO implements IParecerRepository {

	@Override
	public void insert(Parecer parecer, CustomConnection customConnection) throws Exception {
		try {
			int retorno = ParecerDAO.insert(parecer, customConnection.getConnection());
			if (retorno < 0) {
				throw new Exception("Erro ao inserir Parecer.");
			}
		} catch (SQLException e) {
			throw new ValidacaoException("Erro ao inserir Parecer.");
		}
	}
	
	@Override
	public void update(Parecer parecer, CustomConnection customConnection) throws ValidacaoException {
		int retorno = ParecerDAO.update(parecer, customConnection.getConnection());
		if (retorno <= 0) {
			throw new ValidacaoException("Erro ao atualizar parecer");
		}
	}

	@Override
	public Parecer get(int cdParecer, CustomConnection customConnection) throws ValidacaoException {
		return ParecerDAO.get(cdParecer, customConnection.getConnection());
	}

	@Override
	public List<Parecer> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception {
	    try {
	    	List<Parecer> parecerList = ParecerDAO.find(searchCriterios, customConnection);
	        return parecerList;
	    } catch (SQLException e) {
	        throw new ValidacaoException("Erro ao encontrar pareceres");
	    }
	}

	@Override
	public void delete(int cdParecer, CustomConnection customConnection) throws ValidacaoException {
		int retorno = ParecerDAO.delete(cdParecer, customConnection.getConnection());
		if (retorno <= 0) {
			throw new ValidacaoException("Erro ao deletar parecer");
		}
	}
}