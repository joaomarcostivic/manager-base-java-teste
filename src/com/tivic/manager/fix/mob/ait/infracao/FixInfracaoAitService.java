package com.tivic.manager.fix.mob.ait.infracao;

import java.sql.Types;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class FixInfracaoAitService implements IFixInfracaoAitService {
	
	private AitRepository aitRepository;
	private ManagerLog managerLog;
	
	public FixInfracaoAitService() throws Exception {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@Override
	public void corrigirInfracaoAit(int cdInfracaoAntigo, int cdInfracaoNovo, String dtInfracao) throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	        customConnection.initConnection(true);
			managerLog.info("------- INICIADO ------- ",  new GregorianCalendar().getTime().toString());
	        SearchCriterios searchCriterios = new SearchCriterios();
	        List<Ait> aitList = buscarAitsInfracaoErrada(searchCriterios, dtInfracao, cdInfracaoAntigo);
			managerLog.info("QUANTIDADE: ", String.valueOf(aitList.size()));
	        
	        for (Ait ait : aitList) {
	            if (ait.getCdInfracao() == cdInfracaoAntigo) {
	                managerLog.info("Atualização para o AIT: ",  ait.getIdAit());
	                managerLog.info("CD Infracao Antigo: ",  String.valueOf(ait.getCdInfracao()));
	                
                    ait.setCdInfracao(cdInfracaoNovo);
                    aitRepository.update(ait, customConnection);
	                managerLog.info("CD Infracao atualizado: ",  String.valueOf(ait.getCdInfracao()));     
	            }
	        }
	        managerLog.info("------- FINALIZADO ------- ",  new GregorianCalendar().getTime().toString());
	        
	        customConnection.finishConnection();
	    } finally {
	        customConnection.closeConnection();
	    }
	}
	
	private List<Ait> buscarAitsInfracaoErrada(SearchCriterios searchCriterios, String dtInfracao, int cdInfracaoAntigo) throws ValidacaoException, Exception {
	    searchCriterios.addCriterios("A.dt_infracao", dtInfracao, ItemComparator.GREATER_EQUAL, Types.VARCHAR);
	    searchCriterios.addCriteriosEqualInteger("A.cd_infracao", cdInfracaoAntigo);
	    Search<Ait> search = searchAitsInfracaoErrada(searchCriterios);
	    List<Ait> aits = search.getList(Ait.class);
	    if(aits.isEmpty()) {
	        throw new ValidacaoException("Não há AITs para fazer a correção.");
	    } 
	    return aits;
	}
	
	private Search<Ait> searchAitsInfracaoErrada(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
	    Search<Ait> search = new SearchBuilder<Ait>("mob_ait A")
	            .fields("*") 
	            .addJoinTable("JOIN mob_infracao B ON (A.cd_infracao = B.cd_infracao)")
	            .addJoinTable("JOIN mob_ait_movimento C ON (A.cd_ait = C.cd_ait)")
	            .searchCriterios(searchCriterios)
	            .additionalCriterias("C.tp_status = " + TipoStatusEnum.REGISTRO_INFRACAO.getKey() 
	                   + " AND DATE(B.dt_fim_vigencia) <= CURRENT_DATE"
	                   + " AND DATE(B.dt_fim_vigencia) IS NOT null "
	             )
	            .orderBy("A.dt_infracao DESC") 
	            .build();
	    return search;
	}

	@Override
	public void corrigirEventoAit(int cdLoteImpressao) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			this.managerLog.info("INICIANDO", "Fix iniciado");
			customConnection.initConnection(true);
			List<AitEventoFixDTO> aitEventoList = buscarAitCorrecao(cdLoteImpressao, customConnection);
			this.managerLog.info("LISTA AIT SENDO ATUALIZADOS", "Existem " + aitEventoList.size() + " AITs");
			atualizarAit(aitEventoList, customConnection);
	        managerLog.info("------- FINALIZADO ------- ",  new GregorianCalendar().getTime().toString());
			customConnection.finishConnection();			
		} finally {
			customConnection.closeConnection();
		}
	}

	private List<AitEventoFixDTO> buscarAitCorrecao(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_lote_impressao", cdLoteImpressao, cdLoteImpressao > 0);
		searchCriterios.addCriteriosEqualInteger("E.st_equipamento",  EquipamentoServices.ATIVO);
		Search<AitEventoFixDTO> search = new SearchBuilder<AitEventoFixDTO>(" mob_ait_evento A ")
				.fields("A.cd_ait, E.cd_equipamento")
				.addJoinTable(" LEFT OUTER JOIN mob_lote_impressao_ait B ON(A.cd_ait = B.cd_ait)")
				.addJoinTable(" LEFT OUTER JOIN mob_evento_equipamento C ON(A.cd_evento = C.cd_evento)")
				.addJoinTable(" LEFT OUTER JOIN mob_ait D ON(A.cd_ait= D.cd_ait)")
				.addJoinTable(" LEFT OUTER JOIN grl_equipamento E ON(C.nr_serial = E.id_serial)")
				.searchCriterios(searchCriterios)
				.additionalCriterias(" D.cd_equipamento <>  E.cd_equipamento ")
				.customConnection(customConnection)
				.build();
		List<AitEventoFixDTO> aitEventoList = search.getList(AitEventoFixDTO.class);
		if(aitEventoList.isEmpty())
			throw new ValidacaoException("Nenhum equipamento divergente.");
		return aitEventoList;
	}
	
	private void atualizarAit(List<AitEventoFixDTO> aitEventoList, CustomConnection customConnection) throws Exception {
		this.managerLog.info("Iniciando atualização" , "Aits sendo atualizados");
		int count = 1; 
		for (AitEventoFixDTO aitEventoFixDTO : aitEventoList) {
			Ait ait = this.aitRepository.get(aitEventoFixDTO.getCdAit());
			this.managerLog.info(count +"º AIT sendo atualizado", "idAit " + ait.getIdAit() + "\n cdEquipamento = " + ait.getCdEquipamento());
			ait.setCdEquipamento(aitEventoFixDTO.getCdEquipamento());
			ait = this.aitRepository.update(ait, customConnection);
			this.managerLog.info("AIT ATUALIZADO", "idAit " + ait.getIdAit() + "\n cdEquipamento = " + ait.getCdEquipamento());
		}
	}
}
