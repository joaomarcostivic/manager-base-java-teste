package com.tivic.manager.mob.inconsistencias;

import java.util.List;

import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.mob.InconsistenciaDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class InconsistenciaRepositoryDAO implements InconsistenciaRepository {
	
	@Override
	public Inconsistencia insert(Inconsistencia inconsistencia, CustomConnection customConnection) throws Exception {
		int cdInconsistencia = InconsistenciaDAO.insert(inconsistencia, customConnection.getConnection());
		if (cdInconsistencia <= 0)
			throw new Exception("Erro ao inserir Inconsistência.");
		return inconsistencia;
	}
	
	@Override
	public void update(Inconsistencia inconsistencia, CustomConnection customConnection) throws Exception {
		int atualizarInconsistencia = InconsistenciaDAO.update(inconsistencia, customConnection.getConnection());
		if (atualizarInconsistencia <= 0)
			throw new Exception("Erro ao atualizar Inconsistência.");
	}
	
	
	@Override
	public Inconsistencia get(int cdInconsistencia) throws Exception {
		return get(cdInconsistencia, new CustomConnection());
	}

	@Override
	public Inconsistencia get(int cdInconsistencia, CustomConnection customConnection) throws Exception {
		return InconsistenciaDAO.get(cdInconsistencia, customConnection.getConnection());
	}
	
	@Override
	public List<Inconsistencia> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Inconsistencia> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Inconsistencia> search = new SearchBuilder<Inconsistencia>("mob_inconsistencia")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(Inconsistencia.class);
	} 
	
		
}
