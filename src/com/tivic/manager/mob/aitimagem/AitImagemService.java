package com.tivic.manager.mob.aitimagem;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.aitimagem.repositories.IAitImagemRepository;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitImagemService implements IAitImagemService{
	
	private IAitImagemRepository aitImagemRepository;
	private ConversorBaseAntigaNovaFactory conversorBaseAntigaNovaFactory;
	
	public AitImagemService() throws Exception {
		aitImagemRepository = (IAitImagemRepository) BeansFactory.get(IAitImagemRepository.class);
		conversorBaseAntigaNovaFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);;
	}

	@Override
	public AitImagem getFromAit(int cdAit) throws Exception {
		List<AitImagem> aitImagemList = searchAitImagem(searchCriteriosImagemAit(cdAit)).getList(AitImagem.class);
		if (aitImagemList.isEmpty()) {
			return new AitImagem();
		}
		return aitImagemList.get(0);
	}
	
	private SearchCriterios searchCriteriosImagemAit(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		return searchCriterios; 
	}
	
	private Search<AitImagem> searchAitImagem(SearchCriterios searchCriterios) throws Exception {
		Search<AitImagem> search = new SearchBuilder<AitImagem>("mob_ait_imagem A")
				.searchCriterios(searchCriterios)
				.additionalCriterias("(ENCODE(SUBSTRING(blb_imagem FROM 1 FOR 4), 'hex') IN ('ffd8ffdb', 'ffd8ffe0')"
						+ "OR ENCODE(SUBSTRING(blb_imagem FROM 1 FOR 4), 'hex') = '89504e47'"
						+ "OR ENCODE(SUBSTRING(blb_imagem FROM 1 FOR 4), 'hex') = '47494638'"
						+ "OR ENCODE(SUBSTRING(blb_imagem FROM 1 FOR 2), 'hex') = '424d')")
				.orderBy("lg_impressao DESC")
				.build();
		return search;
	}

	@Override
	public void insertImageSync(Ait ait) throws Exception {
		 CustomConnection customConnection = new CustomConnection();
		 try {
			 customConnection.initConnection(true);
			 insertImageSync(ait, customConnection);
			 customConnection.finishConnection();
		 } finally {
			 customConnection.closeConnection();
		 }
	}

	@Override
	public void insertImageSync(Ait ait, CustomConnection customConnection) throws Exception {
		if(ait.getImagens() != null && !ait.getImagens().isEmpty()) {			
			for(AitImagem aitImagem: ait.getImagens()) {
				aitImagem.setCdAit(ait.getCdAit());
				conversorBaseAntigaNovaFactory.getAitImagemRepository().insert(aitImagem, customConnection);
			}
		}
	}

	@Override
	public AitImagem insert(AitImagem aitImagem) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			insert(aitImagem, customConnection);
			customConnection.finishConnection();
			return aitImagem;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public AitImagem insert(AitImagem aitImagem, CustomConnection customConnection) throws Exception{
		aitImagemRepository.insert(aitImagem, customConnection);
		return aitImagem;
	}
	
	@Override
	public AitImagem update(AitImagem aitImagem) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(aitImagem, customConnection);
			customConnection.finishConnection();
			return aitImagem;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public AitImagem update(AitImagem aitImagem, CustomConnection customConnection) throws Exception{
		aitImagemRepository.update(aitImagem, customConnection);
		return aitImagem;
	}
	
	@Override
	public AitImagem get(int cdImagem, int cdAit) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			AitImagem aitImagem = get(cdImagem, cdAit, customConnection);
			customConnection.finishConnection();
			return aitImagem;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public AitImagem get(int cdEvento, int cdAit, CustomConnection customConnection) throws Exception {
		AitImagem aitImagem = this.aitImagemRepository.get(cdEvento, cdAit, customConnection);
		
		if(aitImagem == null)
			throw new NoContentException("Nenhuma imagem de AIT encontrado");
		
		return aitImagem;
	}
	
	@Override
	public List<AitImagem> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<AitImagem> aitImagens = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return aitImagens;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<AitImagem> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		return this.aitImagemRepository.find(searchCriterios, customConnection);
	}
	
}
