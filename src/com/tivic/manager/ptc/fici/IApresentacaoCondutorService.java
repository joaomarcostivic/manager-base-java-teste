package com.tivic.manager.ptc.fici;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.NoContentException;

import com.tivic.sol.connection.CustomConnection;

public interface IApresentacaoCondutorService {
	public ApresentacaoCondutor get(int id) throws BadRequestException, Exception, NoContentException;
	public ApresentacaoCondutor save(ApresentacaoCondutor obj) throws BadRequestException, Exception;
	public ApresentacaoCondutor save(ApresentacaoCondutor obj, CustomConnection customConnection) throws BadRequestException, Exception;
}
