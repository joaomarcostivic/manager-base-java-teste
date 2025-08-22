package com.tivic.manager.mob.inconsistencias;

import java.util.List;

import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface AitInconsistenciaRepository {

	AitInconsistencia insert(AitInconsistencia aitInconsistencia, CustomConnection customConnection) throws Exception;
	AitInconsistencia update(AitInconsistencia listaInconsistencia, CustomConnection customConnection) throws Exception;
	AitInconsistencia get(int cdAitInconsistencia) throws Exception;
	AitInconsistencia get(int cdAitInconsistencia, CustomConnection customConnection) throws Exception;
	List<AitInconsistencia> find(SearchCriterios searchCriterios) throws Exception;
	List<AitInconsistencia> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
