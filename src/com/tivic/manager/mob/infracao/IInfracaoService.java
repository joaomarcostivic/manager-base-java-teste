package com.tivic.manager.mob.infracao;

import java.util.List;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.manager.mob.Infracao;
import com.tivic.sol.connection.CustomConnection;

public interface IInfracaoService {
	public Infracao getByCodDetran(Integer codDetran) throws Exception;
	public Infracao getByCodDetran(Integer codDetran, CustomConnection customConnection) throws Exception;
	public List<Infracao> getInfracoesVigentes() throws Exception;
	public List<Infracao> getInfracoesVigentes(CustomConnection customConnection) throws Exception;
	public List<Infracao> find(SearchCriterios searchCriterios) throws Exception;
	public List<Infracao> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public Infracao get(int cdInfracao) throws Exception;
	public Infracao get(int cdInfracao, CustomConnection customConnection) throws Exception;
	public Infracao insert(Infracao infracao) throws Exception;
	public Infracao insert(Infracao infracao, CustomConnection customConnection) throws Exception;
	public Infracao update(Infracao infracao) throws Exception;
	public Infracao update(Infracao infracao, CustomConnection customConnection) throws Exception;
	public Infracao findInfracaoByData(SearchCriterios searchCriterios, String dtInfracao) throws Exception;
	public Infracao findInfracaoByData(SearchCriterios searchCriterios, String dtInfracao, CustomConnection customConnection) throws Exception;
	public List<Infracao> getAll(String keyword) throws Exception;
	public List<Infracao> getAll(String keyword,CustomConnection customConnection) throws Exception;
}
