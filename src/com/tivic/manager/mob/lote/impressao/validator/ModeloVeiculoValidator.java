package com.tivic.manager.mob.lote.impressao.validator;

import java.util.List;

import com.tivic.manager.fta.MarcaModelo;
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

public class ModeloVeiculoValidator implements Validator<Ait> {
	
	private InconsistenciaRepository inconsistenciaRepository;
	
	public ModeloVeiculoValidator() throws Exception {
		this.inconsistenciaRepository = (InconsistenciaRepository) BeansFactory.get(InconsistenciaRepository.class);
	}

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		List<MarcaModelo> listMarcaModelo = searchModeloVeiculo(object).getList(MarcaModelo.class);
		int tpSemModeloVeiculo = ParametroServices.getValorOfParametroAsInteger("MOB_INCONSISTENCIA_SEM_MODELO_VEICULO", 0);
		Inconsistencia inconsistencia = inconsistenciaRepository.get(tpSemModeloVeiculo);
		for (MarcaModelo modelo: listMarcaModelo) {
			if (modelo.getNmModelo() == null){
				throw new LoteNotificacaoException(inconsistencia.getCdInconsistencia(), inconsistencia.getNmInconsistencia());
			}
			return;
		}
		throw new LoteNotificacaoException(inconsistencia.getCdInconsistencia(), inconsistencia.getNmInconsistencia());
	}
	
	private Search<MarcaModelo> searchModeloVeiculo(Ait ait) throws Exception {
		Search<MarcaModelo> search = new SearchBuilder<MarcaModelo>("fta_marca_modelo A")
				.fields("A.*")
				.searchCriterios(criteriosModeloVeiculo(ait))
				.build();
		return search;
	}
	
	private SearchCriterios criteriosModeloVeiculo(Ait ait) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_marca", ait.getCdMarcaVeiculo(), true);
		return searchCriterios;
	}

}
