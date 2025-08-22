package com.tivic.manager.mob.ait.imagemvideo.aitvideo;

import java.util.List;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.aitimagem.repositories.IAitImagemRepository;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitVideoService implements IAitVideoService {
	
	private IAitImagemRepository aitImagemRepository;

	public AitVideoService() throws Exception {
		this.aitImagemRepository = (IAitImagemRepository) BeansFactory.get(IAitImagemRepository.class);
	}

	@Override
	public List<AitImagem> buscarListaVideos(AitVideoSearch aitVideoSearch) throws ValidacaoException, Exception {
		Search<AitImagem> searchAitVideo = searchVideosAits(setCriteriosSearch(aitVideoSearch));
		List<AitImagem> aitVideosList = searchAitVideo.getList(AitImagem.class);
		
		if (aitVideosList == null || aitVideosList.isEmpty()) {
			return null;
		}
		return aitVideosList;
	}

	private Search<AitImagem> searchVideosAits(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		Search<AitImagem> search = new SearchBuilder<AitImagem>("mob_ait_imagem")
				.fields("cd_imagem, cd_ait")
				.searchCriterios(searchCriterios)
				.additionalCriterias("(ENCODE(SUBSTRING(blb_imagem FROM 5 FOR 4), 'hex') IN ('66747970') OR ENCODE(SUBSTRING(blb_imagem FROM 1 FOR 4), 'hex') IN ('52494646', '1A45DFA3', '3026B275')"
						+ "OR ENCODE(SUBSTRING(blb_imagem FROM 5 FOR 4), 'hex') IN ('71742020'))")
				.build();
		return search;
	}

	private SearchCriterios setCriteriosSearch(AitVideoSearch aitVideoSearch) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", aitVideoSearch.getCdAit(), aitVideoSearch.getCdAit() > 0);
		return searchCriterios;
	}
	
	@Override
	public AitImagem buscarVideo(AitVideoSearch aitVideoSearch) throws ValidacaoException, Exception {
		AitImagem aitVideo = aitImagemRepository.get(aitVideoSearch.getCdImagem(), aitVideoSearch.getCdAit());
		return aitVideo;
	}
}
