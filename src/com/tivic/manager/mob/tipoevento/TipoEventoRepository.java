package com.tivic.manager.mob.tipoevento;

import java.util.List;

import com.tivic.manager.mob.TipoEvento;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface TipoEventoRepository {
	public void insert(TipoEvento tipoEvento, CustomConnection customConnection) throws Exception;
	public void update(TipoEvento tipoEvento, CustomConnection customConnection) throws Exception;
	public TipoEvento get(int cdTipoEvento, CustomConnection customConnection) throws Exception;
	public List<TipoEvento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
