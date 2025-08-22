package com.tivic.manager.mob.lotes.repository.aitimagem;

import java.util.List;

import com.tivic.manager.mob.lotes.enums.impressao.ImpressaoEnum;
import com.tivic.manager.mob.lotes.model.aitimagem.AitImagem;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitImagemRepositoryDAO implements AitImagemRepository {
	
	public AitImagem getMelhorImagem(int cdAit) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("A.lg_impressao", ImpressaoEnum.IMPRIMIR.getKey());
		Search<AitImagem> search = new SearchBuilder<AitImagem>("mob_ait_imagem A")
				.searchCriterios(searchCriterios)
				.build();
		List<AitImagem> aitImagens = search.getList(AitImagem.class);
		
		if(aitImagens.isEmpty()) {
			throw new Exception("Nenhuma das imagens do AIT está elegível para impressão.");
		}
		
		return aitImagens.get(0);
	}
}
