package com.tivic.manager.mob.inconsistencias;

import java.util.List;

import com.tivic.manager.mob.Inconsistencia;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface InconsistenciaRepository {
	
	Inconsistencia insert(Inconsistencia inconsistencia, CustomConnection customConnection) throws Exception;
	public void update(Inconsistencia inconsistencia, CustomConnection customConnection) throws Exception;
	Inconsistencia get(int cdInconsistencia) throws Exception;
	Inconsistencia get(int cdInconsistencia, CustomConnection customConnection) throws Exception;
	List<Inconsistencia> find(SearchCriterios searchCriterios) throws Exception;
	List<Inconsistencia> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;

}
