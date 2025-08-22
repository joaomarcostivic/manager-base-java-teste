package com.tivic.manager.mob.arquivomovimento;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.ArquivoMovimento;
import com.tivic.sol.search.SearchCriterios;

public interface ArquivoMovimentoRepository {
	public void insert(ArquivoMovimento arquivoMovimento, CustomConnection customConnection) throws Exception;
	public void update(ArquivoMovimento arquivoMovimento, CustomConnection customConnection) throws Exception;
	public ArquivoMovimento get(int cdArquivoMovimento, int cdMovimento, int cdAit) throws Exception;
	public ArquivoMovimento get(int cdArquivoMovimento, int cdMovimento, int cdAit, CustomConnection customConnection) throws Exception;
	public List<ArquivoMovimento> find(SearchCriterios searchCriterios) throws Exception;
	public List<ArquivoMovimento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
