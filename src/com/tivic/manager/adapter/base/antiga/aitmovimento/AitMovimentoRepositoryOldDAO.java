package com.tivic.manager.adapter.base.antiga.aitmovimento;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ait.relatorios.quantitativo.RelatorioQuantitativoDTO;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public class AitMovimentoRepositoryOldDAO implements AitMovimentoRepository {
	
	private IAdapterService<AitMovimentoOld, AitMovimento> adapterService;
	
	public AitMovimentoRepositoryOldDAO() throws Exception {
		this.adapterService = new AdapterAitMovimentoService();
	}

	@Override
	public int insert(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		AitMovimentoOld aitMovimentoOld =  this.adapterService.toBaseAntiga(aitMovimento);
		int codRetorno = AitMovimentoOldDAO.insert(aitMovimentoOld, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new Exception("Erro ao inserir AitMovimento.");
		aitMovimento.setCdMovimento(codRetorno);
		return codRetorno;
	}

	@Override
	public void update(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		AitMovimentoOld aitMovimentoOld = this.adapterService.toBaseAntiga(aitMovimento);
		int codRetorno = AitMovimentoOldDAO.update(aitMovimentoOld, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new Exception("Erro ao atualizar AitMovimento.");
		aitMovimento.setCdMovimento(codRetorno);
	}

	@Override
	public AitMovimento get(int cdMovimento, int cdAit) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			AitMovimento aitMovimento = get(cdMovimento, cdAit, customConnection);
			customConnection.finishConnection();
			return aitMovimento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public AitMovimento get(int cdMovimento, int cdAit, CustomConnection customConnection) throws Exception {
		AitMovimentoOld aitMovimentoOld = AitMovimentoOldDAO.get(cdAit, cdMovimento, customConnection.getConnection());
		AitMovimento aitMovimento = this.adapterService.toBaseNova(aitMovimentoOld);
		return aitMovimento;
	}

	@Override
	public List<AitMovimento> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<AitMovimento> aitMovimentoList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return aitMovimentoList;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<AitMovimento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<AitMovimento> aitMovimentoList = new ArrayList<AitMovimento>();
		ResultSetMapper<AitMovimentoOld> rsm = new ResultSetMapper<AitMovimentoOld>(AitMovimentoOldDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), AitMovimentoOld.class);
		List<AitMovimentoOld> aitMovimentoOldList = rsm.toList();
		for (AitMovimentoOld aitMovimentoOld : aitMovimentoOldList) {
			AitMovimento movimento = this.adapterService.toBaseNova(aitMovimentoOld);
			aitMovimentoList.add(movimento);
		}
		return aitMovimentoList;
	}

	@Override
	public AitMovimento getByStatus(int cdAit, int tpStatus) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			AitMovimento aitMovimento = getByStatus(cdAit, tpStatus, customConnection);
			customConnection.finishConnection();
			return aitMovimento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public AitMovimento getByStatus(int cdAit, int tpStatus, CustomConnection customConnection) throws Exception {
		AitMovimentoOld aitMovimentoOld = AitMovimentoOldDAO.getByStatus(cdAit, tpStatus, customConnection.getConnection());
		if (aitMovimentoOld.getCodigoAit() <= 0)
			throw new NoContentException("Nenhum movimento encontrado com este status."); 
		AitMovimento aitMovimento = this.adapterService.toBaseNova(aitMovimentoOld);
		return aitMovimento;
	}

	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoMovimentos(SearchCriterios searchCriterios)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoMovimentos(SearchCriterios searchCriterios,
			CustomConnection customConnection) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoPagamentoNip(SearchCriterios searchCriterios,
			int tpMovimentoQuantitativo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoPagamentoNip(SearchCriterios searchCriterios,
			int tpMovimentoQuantitativo, CustomConnection customConnection) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoPagamentosRecebidos(SearchCriterios searchCriterios)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoPagamentosRecebidos(SearchCriterios searchCriterios,
			CustomConnection customConnection) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
