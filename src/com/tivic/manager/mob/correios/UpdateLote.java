package com.tivic.manager.mob.correios;

import java.util.List;

import com.tivic.manager.mob.CorreiosEtiquetaDTO;
import com.tivic.manager.mob.CorreiosLote;
import com.tivic.manager.mob.correios.builder.GerarEtiquetasBuilder;
import com.tivic.manager.mob.correios.validator.CorreiosLoteUpdateValidations;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class UpdateLote implements IUpdateLoteEtiqueta{
	
	private ICorreiosEtiquetaService correiosEtiquetaService;
	private CorreiosLoteRepository correiosLoteRepository;

	public UpdateLote() throws Exception {
		correiosEtiquetaService = (ICorreiosEtiquetaService) BeansFactory.get(ICorreiosEtiquetaService.class);
		correiosLoteRepository = (CorreiosLoteRepository) BeansFactory.get(CorreiosLoteRepository.class);
	}

	@Override
	public CorreiosLote updateLoteEtiqueta(CorreiosLote correiosLote, CustomConnection customConnection) throws Exception {
		new CorreiosLoteUpdateValidations(correiosLote.getCdLote()).validate(correiosLote, customConnection);
		List<CorreiosEtiquetaDTO> listCorreiosEtiqueta = correiosEtiquetaService.findEtiquetaDTO(gerarCriteriosDeleteEtiquetasLivres(correiosLote));
		correiosEtiquetaService.deleteListEtiquetas(listCorreiosEtiqueta);
		correiosLoteRepository.update(correiosLote, customConnection);
		new GerarEtiquetasBuilder().gerarEtiquetas(correiosLote, customConnection);
		return correiosLote;
	}
	
	public SearchCriterios gerarCriteriosDeleteEtiquetasLivres(CorreiosLote correiosLote) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_lote", correiosLote.getCdLote(), correiosLote.getCdLote() > 0);
		return searchCriterios;
	}
}
