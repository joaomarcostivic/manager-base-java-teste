package com.tivic.manager.ptc.fici;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import javax.ws.rs.BadRequestException;
import com.tivic.sol.search.SearchCriterios;

public interface ApresentacaoCondutorRepository {
	public ApresentacaoCondutor insert(ApresentacaoCondutor objeto) throws BadRequestException, Exception;
	public ApresentacaoCondutor insert(ApresentacaoCondutor objeto, CustomConnection connection) throws BadRequestException, Exception;
	public ApresentacaoCondutor update(ApresentacaoCondutor objeto) throws BadRequestException, Exception;
	public ApresentacaoCondutor update(ApresentacaoCondutor objet, CustomConnection connection) throws BadRequestException, Exception;
	public ApresentacaoCondutor delete(ApresentacaoCondutor objeto) throws BadRequestException, Exception;
	public ApresentacaoCondutor get(int cdApresentacaoCondutor) throws BadRequestException, Exception;
	public ApresentacaoCondutor get(int cdApresentacaoCondutor,CustomConnection connection) throws BadRequestException, Exception;
	public List<ApresentacaoCondutor> getAll() throws BadRequestException, Exception;
	public List<ApresentacaoCondutor> find(SearchCriterios criterios) throws BadRequestException, Exception;
}
