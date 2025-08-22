package com.tivic.manager.mob.inconsistencias;

import java.util.List;

import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IAitInconsistenciaService {
	
	public void update(AitInconsistencia aitInconsistenciam, CustomConnection customConnection) throws Exception;
	public void updateSituacao(AitInconsistencia aitInconsistencia) throws Exception;
	public AitInconsistencia salvarInconsistencia(AitInconsistencia aitInconsistencia) throws Exception;
	public AitInconsistencia salvarInconsistencia(AitInconsistencia aitInconsistencia, CustomConnection customConnection) throws Exception;
	public List<AitInconsistencia> find(SearchCriterios searchCriterios) throws Exception;
	public List<AitInconsistencia> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public AitInconsistenciaDTO getAitInconsistencia(int cdAit, int cdInconsitencias) throws Exception;
	public PagedResponse<AitInconsistenciaDTO> findInconsistenciasAit(SearchCriterios searchCriterios) throws ValidacaoException, Exception;
}
