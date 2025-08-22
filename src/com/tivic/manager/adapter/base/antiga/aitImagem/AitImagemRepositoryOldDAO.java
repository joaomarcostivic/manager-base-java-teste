package com.tivic.manager.adapter.base.antiga.aitImagem;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.aitimagem.repositories.IAitImagemRepository;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class AitImagemRepositoryOldDAO implements IAitImagemRepository {
	
	private IAdapterService<AitImagemOld, AitImagem> adapterService;
	
	public AitImagemRepositoryOldDAO() throws Exception {
		this.adapterService = new AdapterAitImagemService();
	}

	@Override
	public void insert(AitImagem aitImagem, CustomConnection customConnection) throws Exception {
		AitImagemOld aitImagemOld = this.adapterService.toBaseAntiga(aitImagem);
		int codRetorno = AitImagemOldDAO.insert(aitImagemOld, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new ValidacaoException("Erro ao inserir imagem.");
		aitImagem.setCdImagem(codRetorno);
	}

	@Override
	public void update(AitImagem aitImagem, CustomConnection customConnection) throws ValidacaoException, Exception {
		AitImagemOld aitImagemOld = this.adapterService.toBaseAntiga(aitImagem);
		int codRetorno = AitImagemOldDAO.update(aitImagemOld, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new ValidacaoException("Erro ao inserir imagem.");
		aitImagem.setCdImagem(codRetorno);
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
		AitImagemOld aitImagemOld = AitImagemOldDAO.get(cdImagem, customConnection.getConnection());
		AitImagem aitImagem = this.adapterService.toBaseNova(aitImagemOld);
		return aitImagem;
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
		List<AitImagem> aitImagemList = new ArrayList<AitImagem>();
		ResultSetMapper<AitImagemOld> rsm = new ResultSetMapper<AitImagemOld>(AitImagemOldDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), AitImagemOld.class);
		List<AitImagemOld> aitImagemOldList = rsm.toList();
		for (AitImagemOld aitImagemOld: aitImagemOldList) {
			aitImagemList.add(this.adapterService.toBaseNova(aitImagemOld));
		}
		return aitImagemList;
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
		Image imgMulta = null;
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		List<AitImagem> aitImagemList = find(searchCriterios, customConnection);
		if (!aitImagemList.isEmpty()) {
			AitImagem aitImagem = aitImagemList.get(0);
			imgMulta = new ImageIcon(aitImagem.getBlbImagem()).getImage();
		}
		return imgMulta;
	}



}
