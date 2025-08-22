package com.tivic.manager.mob.correios;

import java.sql.Types;
import java.util.List;

import com.tivic.manager.mob.CorreiosEtiquetaDTO;
import com.tivic.manager.mob.CorreiosLote;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class UpdateLoteCancelado implements IUpdateLoteEtiqueta {
	private ICorreiosEtiquetaService correiosEtiquetaService;
	private CorreiosLoteRepository correiosLoteRepository;

	public UpdateLoteCancelado() throws Exception {
		correiosEtiquetaService = (ICorreiosEtiquetaService) BeansFactory.get(ICorreiosEtiquetaService.class);
		correiosLoteRepository = (CorreiosLoteRepository) BeansFactory.get(CorreiosLoteRepository.class);
	}
	
	@Override
	public CorreiosLote updateLoteEtiqueta(CorreiosLote correiosLote, CustomConnection customConnection) throws Exception {
		List<CorreiosEtiquetaDTO> listCorreiosEtiqueta = correiosEtiquetaService.findEtiquetaDTO(gerarCriteriosDeleteEtiquetasLivres(correiosLote));
		correiosEtiquetaService.deleteListEtiquetas(listCorreiosEtiqueta);
		CorreiosLote lote = correiosLoteRepository.get(correiosLote.getCdLote(), customConnection);
		lote.setStLote(correiosLote.getStLote());
		correiosLoteRepository.update(lote, customConnection);
		return correiosLote;
	}
	
	public SearchCriterios gerarCriteriosDeleteEtiquetasLivres(CorreiosLote correiosLote) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_lote", correiosLote.getCdLote(), correiosLote.getCdLote() > 0);
		searchCriterios.addCriterios("A.cd_ait", "" , ItemComparator.ISNULL, Types.INTEGER);
		searchCriterios.addCriterios("A.cd_lote_impressao", "" , ItemComparator.ISNULL, Types.INTEGER);
		return searchCriterios;
	}
}
