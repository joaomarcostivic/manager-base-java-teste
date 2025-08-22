package com.tivic.manager.mob.correios.validator;

import java.sql.Types;
import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.CorreiosLote;
import com.tivic.manager.mob.correios.ICorreiosEtiquetaService;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class CorreiosLoteUpdateValidator implements Validator<CorreiosLote>{
	private ICorreiosEtiquetaService correiosEtiquetaService;
	
	public CorreiosLoteUpdateValidator() throws Exception {
		correiosEtiquetaService = (ICorreiosEtiquetaService) BeansFactory.get(ICorreiosEtiquetaService.class);
	}
	
	@Override
	public void validate(CorreiosLote object, CustomConnection customConnection) throws Exception {
		findEtiquetas(object, customConnection);		
	}

	private SearchCriterios gerarCriteriosEtiquetasLivres(CorreiosLote correiosLote) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_lote", correiosLote.getCdLote());
		searchCriterios.addCriterios("cd_ait", "" , ItemComparator.NOTISNULL, Types.INTEGER);
		return searchCriterios;
	}
	
	private void findEtiquetas(CorreiosLote correiosLote, CustomConnection customConnection) throws Exception {
		List<CorreiosEtiqueta> correiosEtiquetaList =  correiosEtiquetaService.find(gerarCriteriosEtiquetasLivres(correiosLote));
		if(!correiosEtiquetaList.isEmpty()) {
			throw new BadRequestException("Etiquetas já utilizadas. Por favor, crie um novo lote.");
		}		
	}
}
