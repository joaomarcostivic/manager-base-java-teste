package com.tivic.manager.mob.tipoevento;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.TipoEvento;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ITipoEventoService {
	public TipoEvento insert(TipoEvento tipoEvento) throws BadRequestException, Exception;
	public TipoEvento insert(TipoEvento tipoEvento, CustomConnection customConnection) throws BadRequestException, Exception;
	public TipoEvento update(TipoEvento tipoEvento) throws Exception;
	public TipoEvento update(TipoEvento tipoEvento, CustomConnection customConnection) throws Exception;
	public TipoEvento get(int cdTipoEvento) throws Exception;
	public TipoEvento get(int cdTipoEvento, CustomConnection customConnection) throws Exception;
	public List<TipoEvento> find(SearchCriterios searchCriterios) throws Exception;
	public List<TipoEvento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public TipoEvento getByIdTipoEvento(String idTipoEvento) throws Exception;
	public TipoEvento getByIdTipoEvento(String idTipoEvento, CustomConnection customConnection) throws Exception;
}
