package com.tivic.manager.mob.inconsistencias;

import java.util.List;

import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistenciaDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitInconsistenciaRepositoryDAO implements AitInconsistenciaRepository {
	
	@Override
	public AitInconsistencia insert(AitInconsistencia aitInconsistencia, CustomConnection customConnection) throws Exception {
		int cdAitInconsistencia = AitInconsistenciaDAO.insert(aitInconsistencia, customConnection.getConnection());
		if (cdAitInconsistencia <= 0)
			throw new Exception("Erro ao inserir AIT com Inconsistência.");
		return aitInconsistencia;
	}
	
	@Override
	public AitInconsistencia update(AitInconsistencia aitInconsistencia, CustomConnection customConnection) throws Exception {
		int atualizarAitInconsistencia = AitInconsistenciaDAO.update(aitInconsistencia, customConnection.getConnection());
		if (atualizarAitInconsistencia <= 0)
			throw new Exception("Erro ao atualizar Inconsistência.");
		return aitInconsistencia;
	}
	
	@Override
	public AitInconsistencia get(int cdAitInconsistencia) throws Exception {
		return get(cdAitInconsistencia, new CustomConnection());
	}

	@Override
	public AitInconsistencia get(int cdAitInconsistencia, CustomConnection customConnection) throws Exception {
		return AitInconsistenciaDAO.get(cdAitInconsistencia, customConnection.getConnection());
	}
	
	@Override
	public List<AitInconsistencia> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<AitInconsistencia> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<AitInconsistencia> search = new SearchBuilder<AitInconsistencia>("mob_ait_inconsistencia")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(AitInconsistencia.class);
	} 
}
