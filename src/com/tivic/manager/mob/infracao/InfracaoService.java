package com.tivic.manager.mob.infracao;

import java.sql.Types;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.Infracao;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class InfracaoService implements IInfracaoService {
	private InfracaoRepository infracaoRepository;
	
	public InfracaoService() throws Exception {
		this.infracaoRepository = (InfracaoRepository) BeansFactory.get(InfracaoRepository.class);
	}
	
	@Override
	public Infracao getByCodDetran(Integer codDetran) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Infracao infracao = getByCodDetran(codDetran, customConnection);
			customConnection.finishConnection();
			return infracao;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Infracao getByCodDetran(Integer codDetran, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("nr_cod_detran", codDetran, true);
		searchCriterios.addCriterios("dt_fim_vigencia", "", ItemComparator.ISNULL, Types.VARCHAR);
		List<Infracao> infracoes = this.infracaoRepository.find(searchCriterios, customConnection);
		if(infracoes.isEmpty())
			throw new Exception("Nenhuma infração encontrada");
		return infracoes.get(0);
	}
	
	@Override
	public Infracao insert(Infracao infracao) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			insert(infracao, customConnection);
			customConnection.finishConnection();
			return infracao;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Infracao insert(Infracao infracao, CustomConnection customConnection) throws Exception{
		infracaoRepository.insert(infracao, customConnection);
		return infracao;
	}
	
	@Override
	public Infracao update(Infracao infracao) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(infracao, customConnection);
			customConnection.finishConnection();
			return infracao;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Infracao update(Infracao infracao, CustomConnection customConnection) throws Exception{
		infracaoRepository.update(infracao, customConnection);
		return infracao;
	}
	
	@Override
	public Infracao get(int cdInfracao) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Infracao infracao = get(cdInfracao, customConnection);
			customConnection.finishConnection();
			return infracao;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Infracao get(int cdInfracao, CustomConnection customConnection) throws Exception {
		Infracao infracao = this.infracaoRepository.get(cdInfracao, customConnection);
		
		if(infracao == null)
			throw new NoContentException("Nenhuma infração encontrado");
		
		return infracao;
	}
	
	@Override
	public List<Infracao> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<Infracao> infracoes = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return infracoes;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Infracao> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		return this.infracaoRepository.find(searchCriterios, customConnection);
	}
	
	@Override
	public Infracao findInfracaoByData(SearchCriterios searchCriterios, String dtInfracao ) throws Exception {
		return findInfracaoByData(searchCriterios, dtInfracao, new CustomConnection());
	}

	@Override
	public Infracao findInfracaoByData(SearchCriterios searchCriterios, String dtInfracao, CustomConnection customConnection) throws Exception {
		Search<Infracao> search = new SearchBuilder<Infracao>("mob_infracao")
				.searchCriterios(searchCriterios)
				.additionalCriterias("(dt_fim_vigencia >= '" + dtInfracao + "' OR " + "dt_fim_vigencia IS null)")
				.orderBy("dt_fim_vigencia")
				.build();

		return search.getList(Infracao.class).get(0);
	}
	
	@Override
	public List<Infracao> getInfracoesVigentes() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try { 
			customConnection.initConnection(false);
			List<Infracao> infracoesVigentes = getInfracoesVigentes(customConnection);
			customConnection.finishConnection();
			return infracoesVigentes;
		} finally { 
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Infracao> getInfracoesVigentes(CustomConnection customConnection)	throws Exception {
		return infracaoRepository.getInfracoesVigentes(customConnection);
	}
	
	@Override
	public List<Infracao> getAll(String keyword) throws Exception {
		return getAll(keyword, new CustomConnection());
	}

	@Override
	public List<Infracao> getAll(String keyword, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(false);
			List<Infracao> infracoes = this.infracaoRepository.getAll(keyword, customConnection);
			customConnection.finishConnection();
			return infracoes;
		} finally {
			customConnection.closeConnection();
		}
	}
}
