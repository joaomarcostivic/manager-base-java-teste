package com.tivic.manager.mob.lotes.repository.dividaativa;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.lotes.dao.dividaativa.LoteDividaAtivaDAO;
import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaDTO;
import com.tivic.manager.mob.lotes.dto.dividaativa.LoteDividaAtivaDTO;
import com.tivic.manager.mob.lotes.enums.dividaativa.LoteStatusDividaAtivaEnum;
import com.tivic.manager.mob.lotes.model.dividaativa.LoteDividaAtiva;
import com.tivic.manager.mob.lotes.service.dividaativa.exceptions.LoteFinalizadoException;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class LoteDividaAtivaRepositoryDAO implements LoteDividaAtivaRepository {
	
	private IParametroRepository parametroRepository;
	
	public LoteDividaAtivaRepositoryDAO() throws Exception {
		parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}
	
	@Override
	public void insert(LoteDividaAtiva loteDividaAtiva, CustomConnection customConnection) throws Exception {
		int cdLoteDividaAtiva = LoteDividaAtivaDAO.insert(loteDividaAtiva, customConnection.getConnection());
		if (cdLoteDividaAtiva <= 0)
			throw new Exception("Erro ao inserir LoteDividaAtiva.");
		loteDividaAtiva.setCdLoteDividaAtiva(cdLoteDividaAtiva);
	}

	@Override
	public void update(LoteDividaAtiva loteDividaAtiva, CustomConnection customConnection) throws Exception {
		LoteDividaAtivaDAO.update(loteDividaAtiva, customConnection.getConnection());
	}

	@Override
	public LoteDividaAtiva get(int cdLoteDividaAtiva, CustomConnection customConnection) throws Exception {
		return LoteDividaAtivaDAO.get(cdLoteDividaAtiva, customConnection.getConnection());
	}

	@Override
	public List<LoteDividaAtiva> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<LoteDividaAtiva> search = new SearchBuilder<LoteDividaAtiva>("grl_lote_divida_ativa2")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(LoteDividaAtiva.class);
	}

	@Override
	public LoteDividaAtiva getLoteGerado(String idAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_ait", idAit);
		searchCriterios.addCriteriosEqualInteger("B.st_lote", LoteStatusDividaAtivaEnum.GERADO.getKey());
		Search<LoteDividaAtiva> search = new SearchBuilder<LoteDividaAtiva>("grl_lote A")
				.fields("B.*")
				.addJoinTable("JOIN mob_lote_divida_ativa B ON (A.cd_lote = B.cd_lote)")
				.addJoinTable("JOIN mob_lote_divida_ativa_ait C ON (B.cd_lote_divida_ativa = C.cd_lote_divida_ativa)")
				.addJoinTable("JOIN mob_ait D ON (C.cd_ait = D.cd_ait)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		if(search.getList(LoteDividaAtiva.class).isEmpty()) {
			throw new LoteFinalizadoException();
		}
		
		return search.getList(LoteDividaAtiva.class).get(0);
	}

	@Override
	public List<DividaAtivaDTO> getInfoCSVByCdLote(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<DividaAtivaDTO> search = new SearchBuilder<DividaAtivaDTO>("grl_lote A")
				.fields("A.id_lote, C.cd_ait, D.id_ait, D.nr_placa, D.nr_cpf_cnpj_proprietario, "
						+ "D.vl_multa, D.dt_vencimento, E.dt_movimento, D.nm_proprietario AS nm_pessoa,"
						+ "D.ds_logradouro, h.nm_bairro, F.nm_cidade, G.nm_estado, D.nr_cep, D.ds_nr_imovel as nr_logradouro")
				.addJoinTable("JOIN mob_lote_divida_ativa B ON (A.cd_lote = B.cd_lote)")
				.addJoinTable("JOIN mob_lote_divida_ativa_ait C ON (B.cd_lote_divida_ativa = C.cd_lote_divida_ativa)")
				.addJoinTable("JOIN mob_ait D ON (C.cd_ait = D.cd_ait)")
				.addJoinTable("JOIN mob_ait_movimento E ON (D.cd_ait = E.cd_ait)")
				.addJoinTable("LEFT JOIN grl_cidade F ON (D.cd_cidade = F.cd_cidade)")
				.addJoinTable("LEFT JOIN grl_estado G ON (F.cd_estado = G.cd_estado)")
				.addJoinTable("LEFT JOIN grl_Bairro H ON (D.cd_bairro = H.cd_bairro)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(DividaAtivaDTO.class);
	}

	@Override
	public PagedResponse<LoteDividaAtivaDTO> findLotesDividaAtiva(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			PagedResponse<LoteDividaAtivaDTO> LotesDivida = findLotesDividaAtiva(searchCriterios, customConnection);
			customConnection.finishConnection();
			return LotesDivida;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public PagedResponse<LoteDividaAtivaDTO> findLotesDividaAtiva(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<LoteDividaAtivaDTO> search = new SearchBuilder<LoteDividaAtivaDTO>("grl_lote A")
				.fields("A.*, B.cd_lote_divida_ativa, B.st_lote, "
						+ "(SELECT COUNT(F.cd_ait) "
						+ " FROM mob_lote_divida_ativa_ait F "
						+ " WHERE F.cd_lote_divida_ativa = B.cd_lote_divida_ativa) as qtd_ait, MAX(C.dt_envio) AS dt_envio ")
				.addJoinTable("JOIN mob_lote_divida_ativa B ON (A.cd_lote = B.cd_lote)")
				.addJoinTable("JOIN mob_lote_divida_ativa_ait C ON (B.cd_lote_divida_ativa = C.cd_lote_divida_ativa)")
				.addJoinTable("JOIN mob_ait D ON (C.cd_ait = D.cd_ait)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.groupBy("A.cd_lote, A.id_lote, A.dt_criacao, B.cd_lote_divida_ativa, B.st_lote ")
				.orderBy("A.dt_criacao DESC")
				.count()
				.build();
		if(search.getList(LoteDividaAtivaDTO.class).isEmpty()) {
			throw new NoContentException("Nenhum Lote Encontrado");
		}
		return new PagedResponse<LoteDividaAtivaDTO>(search.getList(LoteDividaAtivaDTO.class), search.getRsm().getTotal());
	}

	@Override
	public PagedResponse<DividaAtivaDTO> findCandidatos(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			PagedResponse<DividaAtivaDTO> dividaAtivaDTO = findCandidatos(searchCriterios, customConnection);
			customConnection.finishConnection();
			return dividaAtivaDTO;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public PagedResponse<DividaAtivaDTO> findCandidatos(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		ItemComparator vlMultaInicial = searchCriterios.getAndRemoveCriterio("vl_multa_inicial");
		ItemComparator vlMultaFinal = searchCriterios.getAndRemoveCriterio("vl_multa_final");
		Search<DividaAtivaDTO> search = new SearchBuilder<DividaAtivaDTO>("mob_ait A")
				.fields("A.cd_ait, A.id_ait, A.nr_placa, A.nr_cpf_cnpj_proprietario, "
						+ "A.nm_proprietario AS nm_pessoa, A.vl_multa, A.dt_vencimento, B.dt_movimento ")
				.addJoinTable("JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait)")
				.additionalCriterias("(A.cd_ait IN (SELECT D.cd_ait FROM mob_ait_movimento D WHERE D.tp_status IN (" 
																				+ TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey() 
																		 + ", " + TipoStatusEnum.CETRAN_INDEFERIDO.getKey() + ")"
												+" AND D.cd_ait NOT IN (SELECT D.cd_ait FROM mob_ait_movimento D WHERE D.tp_status IN ("
																		 	   + TipoStatusEnum.MULTA_PAGA.getKey()  
																		+ ", " + TipoStatusEnum.PENALIDADE_SUSPENSA.getKey() 
																		+ ", " + TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey() 
																		+ ", " + TipoStatusEnum.SUSPENSAO_DIVIDA_ATIVA.getKey() + ")))"
									+ " OR (A.cd_ait NOT IN (SELECT C.cd_ait FROM mob_ait_movimento C WHERE C.tp_status IN (" 
																		 + TipoStatusEnum.MULTA_PAGA.getKey()
																  + ", " + TipoStatusEnum.PENALIDADE_SUSPENSA.getKey() 
																  + ", " + TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey() 
																  + ", " + TipoStatusEnum.SUSPENSAO_DIVIDA_ATIVA.getKey() + ")))"
									+")"
									+ " AND (CURRENT_DATE - A.dt_vencimento) > INTERVAL '" 
									+ parametroRepository.getValorOfParametroAsString("NR_DIAS_VENCIMENTO_DIVIDA_ATIVA", customConnection)
									+ " days' "
									+ " AND EXTRACT(YEAR FROM CURRENT_DATE - A.dt_vencimento) <= " + 5
									+ " AND A.cd_ait NOT IN (SELECT C.cd_ait FROM grl_lote A "
														  +" JOIN mob_lote_divida_ativa B ON (A.cd_lote = B.cd_lote)"
														  +" JOIN mob_lote_divida_ativa_ait C ON (B.cd_lote_divida_ativa = c.cd_lote_divida_ativa)"
														  +" WHERE C.lg_erro = 0) "
									+ setCriteriosVlMulta(vlMultaInicial, vlMultaFinal)
									+ setCriteriosCpfCnpNotNull())
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.orderBy("B.dt_movimento DESC")
				.count()
				.build();
		
		if(search.getList(DividaAtivaDTO.class).isEmpty()) {
			throw new Exception("Nenhum Ait Encontrado");
		}
		
		return new PagedResponse<DividaAtivaDTO>(search.getList(DividaAtivaDTO.class), search.getRsm().getTotal());
	}	
	
	private String setCriteriosVlMulta(ItemComparator vlMultaInicial, ItemComparator vlMultaFinal) throws Exception {
		String multaInicial = vlMultaInicial != null ? " AND A.vl_multa" + " >= " + vlMultaInicial.getValue()  : " ";
		String multaFinal = vlMultaFinal != null ? " AND A.vl_multa" + " <= " + vlMultaFinal.getValue() : " ";
		return multaInicial+multaFinal;
	}
	
	private String setCriteriosCpfCnpNotNull() throws Exception {
	    return " AND A.nr_cpf_cnpj_proprietario IS NOT NULL " +
	           "AND A.nr_cpf_cnpj_proprietario NOT LIKE '00000000000%' ";
	}
}
