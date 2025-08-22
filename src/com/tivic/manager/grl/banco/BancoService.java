package com.tivic.manager.grl.banco;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.grl.Banco;
import com.tivic.manager.grl.banco.builders.BancoSearchPaginatorBuilder;
import com.tivic.manager.grl.banco.repository.BancoRepository;
import com.tivic.manager.grl.banco.validations.BancoValidations;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class BancoService implements IBancoService {
	
	private BancoRepository bancoRepository;
	private ManagerLog managerLog;
	
	public BancoService() throws Exception {
		bancoRepository = (BancoRepository) BeansFactory.get(BancoRepository.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	public String pegarBancoConveniado() throws Exception {
		String bancosConveniados = "";
		List<Banco> listBancoConveniado = searchBancosConveniados().getList(Banco.class);
		if (listBancoConveniado.isEmpty()) 
			return null;
		for (Banco banco : listBancoConveniado) {
			bancosConveniados += banco.getNmBanco() + ", ";
		}
		return bancosConveniados.substring(0, bancosConveniados.length()-2);
	}
	
	private Search<Banco> searchBancosConveniados() throws Exception {
		Search<Banco> search = new SearchBuilder<Banco>("grl_banco A")
				.searchCriterios(searchCriterios())
				.build();
		return search;
	}
	
	private SearchCriterios searchCriterios() {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.banco_conveniado", TipoBancoConvenioEnum.COM_CONVENIO.getKey(), true);
		return searchCriterios;
	}
	
	@Override
	public Banco insert(Banco banco) throws BadRequestException, Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			Banco bancoInserted = insert(banco, customConnection);
			customConnection.finishConnection();
			return bancoInserted;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Banco insert(Banco banco, CustomConnection customConnection) throws BadRequestException, Exception{
		managerLog.info("Inserção de banco", "Banco a ser inserido: " + banco.getNmBanco());
		new BancoValidations().validate(banco, customConnection);
		managerLog.warning("Validações de banco", "Banco " + banco.getNmBanco()+ " passou em todas as validações");
		bancoRepository.insert(banco, customConnection);
		managerLog.info("Inserção de banco", banco.getNmBanco() + " inserido com sucesso");
		return banco;
		
	}
	
	@Override
	public Banco update(Banco banco) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			Banco bancoInserted = update(banco, customConnection);
			customConnection.finishConnection();
			return bancoInserted;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Banco update(Banco banco, CustomConnection customConnection) throws Exception{
		new BancoValidations().validate(banco, customConnection);
		bancoRepository.update(banco, customConnection);
		return banco;
		
	}

	@Override
	public Banco get(int cdBanco) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Banco banco = bancoRepository.get(cdBanco);
			customConnection.finishConnection();
			return banco;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public PagedResponse<Banco> find(SearchCriterios searchCriterios) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			
			Search<Banco> searchBancos = bancoRepository.find(searchCriterios);
			List<Banco> bancosList = searchBancos.getList(Banco.class);
			int bancosTotal = searchBancos.getRsm().getTotal();
			
			PagedResponse<Banco> bancos = new BancoSearchPaginatorBuilder(bancosList, bancosTotal).build();
			
			customConnection.finishConnection();
			return bancos;
		} finally {
			customConnection.closeConnection();
		}
	}
	
}
