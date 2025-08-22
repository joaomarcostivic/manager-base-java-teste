package com.tivic.manager.ptc.protocolosv3.parecer;

import java.util.List;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IParecerRepository {
	public void update(Parecer parecer, CustomConnection customConnection) throws ValidacaoException;
	public Parecer get(int cdParecer, CustomConnection customConnection) throws ValidacaoException;
	public void insert(Parecer parecer, CustomConnection customConnection) throws Exception;
	public void delete(int cdParecer, CustomConnection customConnection) throws ValidacaoException;
	public List<Parecer> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception;
}