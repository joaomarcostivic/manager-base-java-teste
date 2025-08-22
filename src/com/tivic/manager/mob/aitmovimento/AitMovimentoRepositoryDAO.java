package com.tivic.manager.mob.aitmovimento;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDAO;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.relatorios.quantitativo.RelatorioQuantitativoDTO;
import com.tivic.manager.mob.ait.relatorios.quantitativo.enums.TipoMovimentoQuantitativoEnum;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitMovimentoRepositoryDAO implements AitMovimentoRepository {

	@Override
	public int insert(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		int cdMovimento = AitMovimentoDAO.insert(aitMovimento, customConnection.getConnection());
		if (cdMovimento < 0)
			throw new Exception("Erro ao inserir AitMovimento.");
		return cdMovimento;
	}

	@Override
	public void update(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		AitMovimentoDAO.update(aitMovimento, customConnection.getConnection());
	}

	@Override
	public AitMovimento get(int cdMovimento, int cdAit) throws Exception {
		return get(cdMovimento, cdAit, new CustomConnection());
	}

	@Override
	public AitMovimento get(int cdMovimento, int cdAit, CustomConnection customConnection) throws Exception {
		return AitMovimentoDAO.get(cdMovimento, cdAit, customConnection.getConnection());
	}

	@Override
	public List<AitMovimento> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<AitMovimento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento")
			.searchCriterios(searchCriterios)
			.customConnection(customConnection)
			.orderBy("dt_movimento")
			.build();
		return search.getList(AitMovimento.class);
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
		AitMovimento aitMovimento = AitMovimentoDAO.getByStatus(cdAit, tpStatus, customConnection.getConnection());
		if (aitMovimento.getCdMovimento() <= 0)
			throw new NoContentException("Nenhum movimento encontrado com este status.");
		customConnection.finishConnection();
		return aitMovimento;
	} 
	
	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoMovimentos(SearchCriterios searchCriterios) throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	        customConnection.initConnection(false);
	        Search<RelatorioQuantitativoDTO> quantitativoMovimentos = findQuantitativoMovimentos(searchCriterios, customConnection);
	        customConnection.finishConnection();
	        return quantitativoMovimentos;
	    } finally {
	        customConnection.closeConnection();
	    }
	}
	
	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoMovimentos(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<RelatorioQuantitativoDTO> search = new SearchBuilder<RelatorioQuantitativoDTO>("mob_ait_movimento A")
				.fields(" A.tp_status, COUNT(*) AS quantidade_movimentos ")
            	.searchCriterios(searchCriterios)
            	.groupBy(" A.tp_status ")
				.orderBy(" A.tp_status DESC ")
				.count()
				.customConnection(customConnection)
				.build();
		return search;
	}
	
	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoPagamentoNip(SearchCriterios searchCriterios, int tpMovimentoQuantitativo) throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	        customConnection.initConnection(false);
	        Search<RelatorioQuantitativoDTO> quantitativoMovimentos = findQuantitativoPagamentoNip(searchCriterios, tpMovimentoQuantitativo, customConnection);
	        customConnection.finishConnection();
	        return quantitativoMovimentos;
	    } finally {
	        customConnection.closeConnection();
	    }
	}
	
	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoPagamentoNip(SearchCriterios searchCriterios, int tpMovimentoQuantitativo,  CustomConnection customConnection) throws Exception {
		Search<RelatorioQuantitativoDTO> search = new SearchBuilder<RelatorioQuantitativoDTO>("mob_ait_movimento A")
				.fields(" COUNT(*) AS quantidade_movimentos, SUM(B.vl_multa) AS vl_total_multas ")
				.addJoinTable(" JOIN mob_ait B ON (B.cd_ait = A.cd_ait) ")
            	.searchCriterios(searchCriterios)
            	.additionalCriterias(incluirBuscaNipPagamento(tpMovimentoQuantitativo))
				.count()
				.customConnection(customConnection)
				.build();
		return search;
	}
	
	private String incluirBuscaNipPagamento(int tpMovimentoQuantitativo) throws Exception {
		if (tpMovimentoQuantitativo == TipoMovimentoQuantitativoEnum.RECEBIMENTO_PENALIDADE_MULTA_PROCESSADAS_PERIODO.getKey() ) {
 			return " A.cd_ait IN ( "
			        + " SELECT A2.cd_ait FROM mob_ait_movimento A2 " 
			        + "		WHERE A2.tp_status = " + TipoStatusEnum.MULTA_PAGA.getKey()
			        + "		AND A2.lg_enviado_detran = " + TipoLgEnviadoDetranEnum.REGISTRADO.getKey() 
			        + ") ";
 		}
		return  " A.cd_ait NOT IN ( "
        + " SELECT A2.cd_ait FROM mob_ait_movimento A2 " 
        + "		WHERE A2.tp_status = " + TipoStatusEnum.MULTA_PAGA.getKey()
        + "		AND A2.lg_enviado_detran = " + TipoLgEnviadoDetranEnum.REGISTRADO.getKey() 
        + ") ";
	}
	
	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoPagamentosRecebidos(SearchCriterios searchCriterios) throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	        customConnection.initConnection(false);
	        Search<RelatorioQuantitativoDTO> quantitativoMovimentos = findQuantitativoPagamentosRecebidos(searchCriterios, customConnection);
	        customConnection.finishConnection();
	        return quantitativoMovimentos;
	    } finally {
	        customConnection.closeConnection();
	    }
	}
	
	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoPagamentosRecebidos(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<RelatorioQuantitativoDTO> search = new SearchBuilder<RelatorioQuantitativoDTO>("mob_ait_movimento A")
				.fields(" COUNT(*) AS quantidade_movimentos, SUM(B.vl_multa) AS vl_total_multas ")
				.addJoinTable(" JOIN mob_ait B ON (B.cd_ait = A.cd_ait) ")
            	.searchCriterios(searchCriterios)
				.count()
				.customConnection(customConnection)
				.build();
		return search;
	}
}
