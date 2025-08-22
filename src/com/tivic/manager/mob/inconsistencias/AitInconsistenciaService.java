package com.tivic.manager.mob.inconsistencias;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitInconsistenciaService implements IAitInconsistenciaService {
	
	private AitInconsistenciaRepository aitInconsistenciaRepository;
	private AitRepository aitRepository;
	
	public AitInconsistenciaService() throws Exception {
		this.aitInconsistenciaRepository = (AitInconsistenciaRepository) BeansFactory.get(AitInconsistenciaRepository.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}
	
	@Override
	public void updateSituacao(AitInconsistencia aitInconsistencia) throws Exception {
		update(aitInconsistencia, new CustomConnection());
	}

	public void updateSituacao(AitInconsistencia aitInconsistencia, CustomConnection customConnection) throws Exception {
	    try {
	        customConnection.initConnection(true);
	        verificarMovimento(aitInconsistencia);
	        List<AitInconsistencia> listaInconsistencia = searchInconsistencias(aitInconsistencia.getCdAit(), aitInconsistencia.getCdInconsistencia());
	        for (AitInconsistencia inconsistencia : listaInconsistencia) { 
	        	inconsistencia.setStInconsistencia(aitInconsistencia.getStInconsistencia());
	        	inconsistencia.setDtResolucaoInconsistencia(aitInconsistencia.getDtResolucaoInconsistencia());
	            this.aitInconsistenciaRepository.update(inconsistencia, customConnection);
	        }
	        customConnection.finishConnection();
	    } finally {
	        customConnection.closeConnection();
	    }
	}
	
	@Override
	public void update(AitInconsistencia aitInconsistencia, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			this.aitInconsistenciaRepository.update(aitInconsistencia, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private void verificarMovimento(AitInconsistencia aitInconsistencia) throws Exception {
		Ait ait = aitRepository.get(aitInconsistencia.getCdAit(), new CustomConnection());
		if (ait.getTpStatus() > aitInconsistencia.getTpStatusPretendido() 
				&& aitInconsistencia.getStInconsistencia() == TipoSituacaoInconsistencia.PENDENTE.getKey()) {
			throw new ValidacaoException("Não é possível anular resolução, há movimento posterior.");
		}
	}
	
	private List<AitInconsistencia> searchInconsistencias(int cdAit, int cdInconsistencia) throws Exception {
	    SearchCriterios searchCriterios = new SearchCriterios();
	    Search<AitInconsistencia> search = new SearchBuilder<AitInconsistencia>("mob_ait_inconsistencia A")
	            .fields("A.*")
	            .searchCriterios(searchCriterios)
	            .additionalCriterias("A.cd_ait = " + cdAit + " AND A.cd_inconsistencia = " + cdInconsistencia) 
	            .build();
	    return search.getList(AitInconsistencia.class);
	}

	@Override
	public AitInconsistencia salvarInconsistencia(AitInconsistencia aitInconsistencia) throws Exception {
		return salvarInconsistencia(aitInconsistencia, new CustomConnection());
	}
	
	@Override
	public AitInconsistencia salvarInconsistencia(AitInconsistencia aitInconsistencia, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			aitInconsistenciaRepository.insert(aitInconsistencia, customConnection);
			customConnection.finishConnection();
			return aitInconsistencia;
		}
		finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<AitInconsistencia> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<AitInconsistencia> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(false);
			Search<AitInconsistencia> search = new SearchBuilder<AitInconsistencia>("mob_ait_inconsistencia")
					.searchCriterios(searchCriterios)
					.customConnection(customConnection)
					.build();
			List<AitInconsistencia> aitInconsistenciaList = search.getList(AitInconsistencia.class);
			customConnection.finishConnection();
			return aitInconsistenciaList;
		} finally {
			customConnection.closeConnection();
		}
	}	
	
	@Override
	public AitInconsistenciaDTO getAitInconsistencia(int cdAit, int cdInconsistencia) throws Exception {
		Search<AitInconsistenciaDTO> search = getInconsistenciaAit(cdAit, cdInconsistencia, new CustomConnection());
		if(search.getList(AitInconsistenciaDTO.class).isEmpty()) 
			throw new ValidacaoException ("Não há Inconsistências registradas com esse AIT");
		return search.getList(AitInconsistenciaDTO.class).get(0);
	}
	
	private Search<AitInconsistenciaDTO> getInconsistenciaAit(int cdAit, int cdInconsistencia, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(false);
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
			searchCriterios.addCriteriosEqualInteger("B.cd_inconsistencia", cdInconsistencia, true);
			Search<AitInconsistenciaDTO> search = new SearchBuilder<AitInconsistenciaDTO>("mob_ait_inconsistencia A ")
					.fields("A.cd_ait, B.cd_inconsistencia, A.cd_ait_inconsistencia, A.tp_status_pretendido, A.tp_status_atual, "
							  + "DATE(A.dt_inclusao_inconsistencia) AS dt_inclusao_inconsistencia, "
							  + "A.st_inconsistencia, A.dt_resolucao_inconsistencia, "
							  + "B.nm_inconsistencia, B.tp_inconsistencia, C.dt_infracao, C.id_ait,"
							  + "COUNT(B.nm_inconsistencia) over() AS total_inconsistencias")
					.addJoinTable("LEFT OUTER JOIN mob_inconsistencia B ON (A.cd_inconsistencia = B.cd_inconsistencia)")
					.addJoinTable("LEFT OUTER JOIN mob_ait C ON (C.cd_ait = A.cd_ait)")
					.searchCriterios(searchCriterios)
					.orderBy("A.dt_inclusao_inconsistencia DESC")
					.customConnection(customConnection)
					.count()
					.build();
			return search;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public PagedResponse<AitInconsistenciaDTO> findInconsistenciasAit(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		Search<AitInconsistenciaDTO> list = listarInconsistenciasAit(searchCriterios);
		return new AitInconsistenciaDTOListBuilder(list.getList(AitInconsistenciaDTO.class), list.getRsm().getTotal()).build();
	}
	
	private Search<AitInconsistenciaDTO> listarInconsistenciasAit(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		Search<AitInconsistenciaDTO> search = new SearchBuilder<AitInconsistenciaDTO>("mob_ait_inconsistencia A")
				.fields("A.cd_ait, A.cd_ait_inconsistencia, A.cd_inconsistencia, A.tp_status_pretendido, A.tp_status_atual, "
						+ "DATE(A.dt_inclusao_inconsistencia) AS dt_inclusao_inconsistencia, "
					  + "A.st_inconsistencia, A.dt_resolucao_inconsistencia, "
					  + "B.nm_inconsistencia, B.tp_inconsistencia, C.dt_infracao, C.id_ait")
				.addJoinTable("LEFT OUTER JOIN mob_inconsistencia B ON (A.cd_inconsistencia = B.cd_inconsistencia)")
				.addJoinTable("LEFT OUTER JOIN mob_ait C ON (C.cd_ait = A.cd_ait) ")
        		.searchCriterios(searchCriterios)
				.orderBy("A.dt_inclusao_inconsistencia DESC")
				.count()
			.build();
		return search;	
	}
}
