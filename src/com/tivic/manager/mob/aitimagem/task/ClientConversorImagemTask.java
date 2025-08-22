package com.tivic.manager.mob.aitimagem.task;

import java.sql.Types;
import java.util.List;

import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.log.builders.ErrorLogBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class ClientConversorImagemTask implements IClientConversorImagemTask {
	
	private ManagerLog managerLog;
	private ConversorBaseAntigaNovaFactory conversorBaseAntigaNovaFactory;
	private IImageReencoder imageReencoder;
	
	public ClientConversorImagemTask() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		conversorBaseAntigaNovaFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);
		imageReencoder = (IImageReencoder) BeansFactory.get(IImageReencoder.class);
	}
	
	@Override
	public void execute(Integer cdAit) throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	        customConnection.initConnection(true);
	        managerLog.info("Iniciando conversão para AIT: ", String.valueOf(cdAit));
	        List<AitImagem> aitImagemList = identificadoresImagem(cdAit, customConnection);
	        converterImagens(aitImagemList, customConnection);
	        customConnection.finishConnection();
	        managerLog.info("Conversão concluída para AIT: ", String.valueOf(cdAit));
	    } catch (Exception e) {
	        managerLog.showLog(new ErrorLogBuilder("ConversorImagens", 
	            "Erro no AIT " + cdAit + ": " + e.getMessage()).build());
	    } finally {
	        customConnection.closeConnection();
	    }
	}
	
	private void converterImagens(List<AitImagem> aitImagemList, CustomConnection customConnection) throws Exception {
		for (AitImagem aitImagem : aitImagemList) {
			byte[] newBlbImagem = imageReencoder.reencodeToJpg(aitImagem.getBlbImagem());
			if (newBlbImagem != null) {
				aitImagem.setBlbImagem(newBlbImagem);
				conversorBaseAntigaNovaFactory.getAitImagemRepository().update(aitImagem, customConnection);
			}
		}
	}

	private List<AitImagem> identificadoresImagem(Integer cdAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, cdAit > 0);
		searchCriterios.getCriterios().add(
			    new ItemComparator(
			        "ENCODE(SUBSTRING(blb_imagem FROM 1 FOR 4), 'hex') IN ('ffd8ffe1', 'ffd8ffe2')",
			        "true",
			        ItemComparator.AND,
			        Types.VARCHAR
			    )
			);
	    return conversorBaseAntigaNovaFactory.getAitImagemRepository().find(searchCriterios, customConnection);
	}
}
