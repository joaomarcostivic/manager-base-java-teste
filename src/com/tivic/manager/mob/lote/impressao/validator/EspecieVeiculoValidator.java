package com.tivic.manager.mob.lote.impressao.validator;

import java.util.List;

import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.mob.inconsistencias.InconsistenciaRepository;
import com.tivic.manager.mob.lote.impressao.LoteNotificacaoException;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class EspecieVeiculoValidator implements Validator<Ait> {
	
	private InconsistenciaRepository inconsistenciaRepository;
	
	public EspecieVeiculoValidator() throws Exception {
		this.inconsistenciaRepository = (InconsistenciaRepository) BeansFactory.get(InconsistenciaRepository.class);
	}

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		List<EspecieVeiculo> listEspecieVeiculo = searchEspecieVeiculo(object).getList(EspecieVeiculo.class);
		int tpSemEspecieVeiculo = ParametroServices.getValorOfParametroAsInteger("MOB_INCONSISTENCIA_SEM_ESPECIE_VEICULO", 0);
		Inconsistencia inconsistencia = inconsistenciaRepository.get(tpSemEspecieVeiculo);
		for (EspecieVeiculo especieVeiculo: listEspecieVeiculo) {
			if (especieVeiculo.getDsEspecie() == null){
				throw new LoteNotificacaoException(inconsistencia.getCdInconsistencia(), inconsistencia.getNmInconsistencia());
			}
			return;
		}
		throw new LoteNotificacaoException(inconsistencia.getCdInconsistencia(), inconsistencia.getNmInconsistencia());
	}
	
	private Search<EspecieVeiculo> searchEspecieVeiculo(Ait ait) throws Exception {
		Search<EspecieVeiculo> search = new SearchBuilder<EspecieVeiculo>("fta_especie_veiculo A")
				.fields("A.*")
				.searchCriterios(criteriosEspecieVeiculo(ait))
				.build();
		return search;
	}
	
	private SearchCriterios criteriosEspecieVeiculo(Ait ait) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_especie", ait.getCdEspecieVeiculo(), true);
		return searchCriterios;
	}

}
