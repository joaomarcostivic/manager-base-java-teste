package com.tivic.manager.mob.lotes.validator;

import java.util.List;

import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.mob.inconsistencias.InconsistenciaRepository;
import com.tivic.manager.mob.lotes.service.impressao.exceptions.LoteNotificacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ModeloVeiculoValidator implements IValidator<Ait> {
	
	private InconsistenciaRepository inconsistenciaRepository;
	private IParametroRepository parametroRepository;
	
	public ModeloVeiculoValidator() throws Exception {
		this.inconsistenciaRepository = (InconsistenciaRepository) BeansFactory.get(InconsistenciaRepository.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		List<MarcaModelo> listMarcaModelo = searchModeloVeiculo(object).getList(MarcaModelo.class);
		int tpSemModeloVeiculo = parametroRepository.getValorOfParametroAsInt("MOB_INCONSISTENCIA_SEM_MODELO_VEICULO");
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
