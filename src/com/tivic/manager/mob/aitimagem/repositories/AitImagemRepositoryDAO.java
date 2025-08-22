package com.tivic.manager.mob.aitimagem.repositories;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.AitImagemDAO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitImagemRepositoryDAO implements IAitImagemRepository {

	@Override
	public void insert(AitImagem aitImagem, CustomConnection customConnection) throws ValidacaoException {
		int codRetorno = AitImagemDAO.insert(aitImagem, customConnection.getConnection());
		if (codRetorno <= 0) {
			throw new ValidacaoException("Erro ao inserir imagem");
		}
	}

	@Override
	public void update(AitImagem aitImagem, CustomConnection customConnection) throws ValidacaoException, Exception {
		int codRetorno = AitImagemDAO.update(aitImagem, customConnection.getConnection());
		if (codRetorno <= 0) {
			throw new ValidacaoException("Erro ao inserir imagem");
		}		
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
	public AitImagem get(int cdImagem, int cdAit, CustomConnection customConnection) throws Exception {
		return AitImagemDAO.get(cdImagem, cdAit, customConnection.getConnection());
	}
	
	@Override
	public List<AitImagem> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<AitImagem> aitImagemList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return aitImagemList;
		} finally {
			customConnection.closeConnection();
		}
	} 
	
	@Override
	public List<AitImagem> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<AitImagem> search = new SearchBuilder<AitImagem>("mob_ait_imagem A")
			.searchCriterios(searchCriterios)
			.customConnection(customConnection)
			.orderBy("lg_impressao DESC")
			.build();
		return search.getList(AitImagem.class);
	}
	
	@Override
	public Image pegarImagemVeiculo(int cdAit) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Image image = pegarImagemVeiculo(cdAit, customConnection);
			customConnection.finishConnection();
			return image;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Image pegarImagemVeiculo(int cdAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		List<AitImagem> aitImagemList = find(searchCriterios, customConnection);
		Image imgMulta = null;
		if(!aitImagemList.isEmpty()) {
			imgMulta = new ImageIcon(aitImagemList.get(0).getBlbImagem()).getImage();
		}
		return imgMulta;
	}
}
