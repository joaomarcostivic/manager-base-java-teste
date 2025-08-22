package com.tivic.manager.grl.banco;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.grl.Banco;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IBancoService {
	String pegarBancoConveniado() throws Exception;
	public Banco insert(Banco banco) throws BadRequestException, Exception;
	public Banco insert(Banco banco, CustomConnection customConnection) throws BadRequestException, Exception;
	public Banco update(Banco banco) throws Exception;
	public Banco update(Banco banco, CustomConnection customConnection) throws Exception;
	public Banco get(int cdBanco) throws Exception;
	public PagedResponse<Banco> find(SearchCriterios bancoSearch) throws Exception;
}
