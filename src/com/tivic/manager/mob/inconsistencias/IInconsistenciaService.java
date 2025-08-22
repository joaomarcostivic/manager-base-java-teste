package com.tivic.manager.mob.inconsistencias;

import java.util.List;

import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface IInconsistenciaService {

	public Inconsistencia get(int cdInconsistencia) throws Exception;
	public Inconsistencia get(int cdInconsistencia, CustomConnection customConnection) throws Exception;
	public PagedResponse<AitInconsistenciaDTO> filtrarAitsComInconsistencias(SearchCriterios searchCriterios) throws ValidacaoException, Exception;
	public Search<TiposInconsistenciasDTO> buscarTiposInconsistencias(SearchCriterios searchCriterios) throws ValidacaoException, Exception;
	public void cancelarListaInconsistencia(List<AitInconsistenciaDTO> aitInconsistenciaDTOList) throws Exception; 
}
