package com.tivic.manager.mob.ait.imagemvideo.aitimagem;

import java.util.List;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.aitimagem.repositories.IAitImagemRepository;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitImagemService implements IAitImagemService {
	
	private IAitImagemRepository aitImagemRepository;

	public AitImagemService() throws Exception {
		this.aitImagemRepository = (IAitImagemRepository) BeansFactory.get(IAitImagemRepository.class);
	}

	@Override
	public List<AitImagem> buscarListaImagens(AitImagemSearch aitImagemSearch) throws ValidacaoException, Exception {
	    Search<AitImagem> searchAitImagem = searchListagemAits(setCriteriosSearch(aitImagemSearch));
	    List<AitImagem> aitImagensList = searchAitImagem.getList(AitImagem.class);
	    if (aitImagensList == null || aitImagensList.isEmpty()) {
	        return null;
	    }
		return aitImagensList;
	}
	
	private Search<AitImagem> searchListagemAits(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		Search<AitImagem> search = new SearchBuilder<AitImagem>("mob_ait_imagem")
				.fields("cd_imagem, cd_ait")
				.searchCriterios(searchCriterios)
				.additionalCriterias("(ENCODE(SUBSTRING(blb_imagem FROM 1 FOR 4), 'hex') IN ('ffd8ffdb', 'ffd8ffe0')"
						+ "OR ENCODE(SUBSTRING(blb_imagem FROM 1 FOR 4), 'hex') = '89504e47'"
						+ "OR ENCODE(SUBSTRING(blb_imagem FROM 1 FOR 4), 'hex') = '47494638'"
						+ "OR ENCODE(SUBSTRING(blb_imagem FROM 1 FOR 2), 'hex') = '424d')")
				.build();
		
		return search;
	}

	private SearchCriterios setCriteriosSearch(AitImagemSearch aitImagemSearch) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", aitImagemSearch.getCdAit(), aitImagemSearch.getCdAit() > 0);
		return searchCriterios;
	}
	
	@Override
	public AitImagem buscarImagem(AitImagemSearch aitImagemSearch) throws ValidacaoException, Exception {
		AitImagem aitImagem = aitImagemRepository.get(aitImagemSearch.getCdImagem(), aitImagemSearch.getCdAit());
		return aitImagem;
	}

}
