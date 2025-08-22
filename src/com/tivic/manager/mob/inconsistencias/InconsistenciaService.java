package com.tivic.manager.mob.inconsistencias;

import java.util.List;

import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.mob.ait.CancelamentoInconsistencia;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;


public class InconsistenciaService implements IInconsistenciaService {
	
	private InconsistenciaRepository inconsistenciaRepository;
	
	public InconsistenciaService() throws Exception {
		this.inconsistenciaRepository = (InconsistenciaRepository) BeansFactory.get(InconsistenciaRepository.class);
	}
	
	@Override
	public Inconsistencia get(int cdInconsistencia) throws Exception {
		return get(cdInconsistencia, new CustomConnection());
	}
	
	@Override
	public Inconsistencia get(int cdInconsistencia, CustomConnection customConnection) throws Exception {
		return this.inconsistenciaRepository.get(cdInconsistencia, customConnection);
	}
	
	@Override
	public PagedResponse<AitInconsistenciaDTO> filtrarAitsComInconsistencias(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		Search<AitInconsistenciaDTO> list = searchAitsComInconsistencia(searchCriterios);
		if(list.getList(AitInconsistenciaDTO.class).isEmpty()) {
			throw new ValidacaoException ("Não há AIT's inconsistentes com esse filtro.");
		} 
		return new AitInconsistenciaDTOListBuilder(list.getList(AitInconsistenciaDTO.class), list.getRsm().getTotal()).build();
	}
	
	private Search<AitInconsistenciaDTO> searchAitsComInconsistencia(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		Search<AitInconsistenciaDTO> search = new SearchBuilder<AitInconsistenciaDTO>("mob_ait_inconsistencia A")
				.fields("A.cd_ait, A.tp_status_pretendido, A.cd_inconsistencia, "
					  + "MAX(date(A.dt_inclusao_inconsistencia)) AS dt_inclusao_inconsistencia, "
					  + "A.st_inconsistencia, A.dt_resolucao_inconsistencia, "
					  + "B.nm_inconsistencia, B.tp_inconsistencia, C.dt_infracao, C.id_ait, COUNT(C.id_ait) as total_inconsistencias")
				.addJoinTable("LEFT OUTER JOIN mob_inconsistencia B ON (A.cd_inconsistencia = B.cd_inconsistencia)")
				.addJoinTable("LEFT OUTER JOIN mob_ait C ON (C.cd_ait = A.cd_ait) ")
				.groupBy("A.cd_ait, A.tp_status_pretendido, A.cd_inconsistencia, " 
						+ "A.st_inconsistencia, A.dt_resolucao_inconsistencia, " 
						+ "B.nm_inconsistencia, B.tp_inconsistencia, C.dt_infracao, C.id_ait ")
        		.searchCriterios(searchCriterios)
				.orderBy("C.dt_infracao DESC")
				.count()
			.build();
		return search;	
	}
	
	@Override
	public Search<TiposInconsistenciasDTO> buscarTiposInconsistencias(SearchCriterios searchCriterios) throws ValidacaoException, Exception{ 
		Search<TiposInconsistenciasDTO> list = searchLotes(searchCriterios);
		if(list.getList(TiposInconsistenciasDTO.class).isEmpty()) {
			throw new ValidacaoException ("Nenhuma inconsistência encontrada.");
		} 
		return list;
	}
	
	private Search<TiposInconsistenciasDTO> searchLotes(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		Search<TiposInconsistenciasDTO> search = new SearchBuilder<TiposInconsistenciasDTO>("mob_inconsistencia B")
        		.searchCriterios(searchCriterios)
				.count()
				.build();
		return search;	
	}

	@Override
	public void cancelarListaInconsistencia(List<AitInconsistenciaDTO> aitInconsistenciaDTOList) throws Exception {
		for(AitInconsistenciaDTO aitInconsistenciaDTO: aitInconsistenciaDTOList) {
			CancelamentoInconsistencia cancelamentoInconsistencia = new CancelamentoInconsistencia(aitInconsistenciaDTO);
			if (!cancelamentoInconsistencia.isCancelado())
				cancelamentoInconsistencia.cancelar();
			if (cancelamentoInconsistencia.isCancelado() && cancelamentoInconsistencia.isNotAtualizado()) {
				atualizarInconsistencia(aitInconsistenciaDTO, cancelamentoInconsistencia);
				continue;
			}
		}
	}
	
	private void atualizarInconsistencia(AitInconsistenciaDTO aitInconsistenciaDTO, CancelamentoInconsistencia cancelamentoInconsistencia) throws Exception {
		CustomConnection customConnection = new CustomConnection(); 
		cancelamentoInconsistencia.atualizarAitInconsistencia(aitInconsistenciaDTO.getCdAit(), aitInconsistenciaDTO.getTpStatusPretendido(), customConnection);
	}
}
