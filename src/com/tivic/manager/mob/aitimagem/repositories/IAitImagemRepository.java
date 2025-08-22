package com.tivic.manager.mob.aitimagem.repositories;

import java.awt.Image;
import java.util.List;

import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IAitImagemRepository {
	void insert(AitImagem aitImagem, CustomConnection customConnection) throws ValidacaoException, Exception; 
	void update(AitImagem aitImagem, CustomConnection customConnection) throws ValidacaoException, Exception; 
	AitImagem get(int cdImagem, int cdAit) throws Exception; 
	AitImagem get(int cdImagem, int cdAit, CustomConnection customConnection) throws Exception;
	List<AitImagem> find(SearchCriterios searchCriterios) throws Exception; 
	List<AitImagem> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	Image pegarImagemVeiculo(int cdAit) throws Exception; 
	Image pegarImagemVeiculo(int cdAit, CustomConnection customConnection) throws Exception; 
}
