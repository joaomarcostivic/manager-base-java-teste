package com.tivic.manager.fix.mob.aitMovimento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepositoryDAO;
import com.tivic.manager.mob.lote.impressao.TipoAdesaoSneEnum;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class FixAitMovimento {

	public static void fixMovimentoCancelamento(Integer a) {
		CustomConnection customConnection = null;
		try {
			ManagerLog managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
			customConnection = new CustomConnection();
			customConnection.initConnection(true);
			AitMovimentoRepository aitMovimentoRepository = new AitMovimentoRepositoryDAO();
			managerLog.info("----------------------------------- INICIADO ", "---------------------------------------");
			List<AitMovimento> aits = aitsComCancelamento(customConnection);
			List<String> errors = new ArrayList<String>();
			for(AitMovimento ait: aits) {
				managerLog.info(".....................................................................................", "");
				managerLog.info("AIT: " + ait.getCdAit(), "" );
				managerLog.info(".....................................................................................", "");
				List<AitMovimento> aitMovimento = aitMovimentos(ait.getCdAit(), customConnection);
				List<Integer> existTpStatusRepetido = cancelamentoDuplicado(aitMovimento);				
				for(AitMovimento movimentoAit: aitMovimento) {
					String dtMovimento = formataData(movimentoAit.getDtMovimento());
					int cdMovimento = verificaCancelamento(movimentoAit.getCdAit(), movimentoAit.getTpStatus(), dtMovimento);
					if(cdMovimento == -1)
						continue;
					
					if(cdMovimento == 0){
						managerLog.info("Aits sincronizados", aits.size() + " aits sincronizados com sucesso");
						errors.add("O AIT " + ait.getCdAit() + " não tem movimento que originou o cancelamento com tpStatus " + movimentoAit.getTpStatus());
						continue;
					}
					
					if(!existTpStatusRepetido.isEmpty()) {
						boolean lgEviadoDetranVerificado = verificaLgEnviadoDetran(existTpStatusRepetido, movimentoAit.getTpStatus(), movimentoAit.getLgEnviadoDetran());
						if(lgEviadoDetranVerificado)
							movimentoAit.setCdMovimentoCancelamento(cdMovimento);
						else
							continue;
					}
					else
						movimentoAit.setCdMovimentoCancelamento(cdMovimento);
					
					aitMovimentoRepository.update(movimentoAit, customConnection);
				}
 				
			}
			managerLog.info("-----------------------------------Lista de Erros-------------------------------------", "");
			for (String erros : errors) {
				managerLog.info("AIT sem alteração: ", erros);
			}
			
			managerLog.info("----------------------------------- FINALIZADO ---------------------------------------", "");
			customConnection.finishConnection();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				customConnection.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static List<AitMovimento> aitsComCancelamento(CustomConnection customConnection) throws Exception {
		List<AitMovimento> ait = new ArrayList<AitMovimento>();
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.fields("A.cd_ait")
				.additionalCriterias("A.tp_status IN ("
						+ TipoStatusEnum.CANCELAMENTO_DEFESA_PREVIA.getKey() + ", " 
						+ TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_PREVIA.getKey() + ", "
						+ TipoStatusEnum.CANCELAMENTO_FICI.getKey() + ", "
						+ TipoStatusEnum.CANCELAMENTO_DEFESA_DEFERIDA.getKey() + ", "
						+ TipoStatusEnum.CANCELAMENTO_DEFESA_INDEFERIDA.getKey() + ", "
						+ TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA.getKey() + ", "
						+ TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA.getKey() + ", "
						+ TipoStatusEnum.CANCELAMENTO_RECURSO_JARI.getKey() + ", "
						+ TipoStatusEnum.CANCELAMENTO_JARI_COM_PROVIMENTO.getKey() + ", "
						+ TipoStatusEnum.CANCELAMENTO_JARI_SEM_ACOLHIMENTO.getKey() + ", "
						+ TipoStatusEnum.CANCELAMENTO_RECURSO_CETRAN.getKey() + ", "
						+ TipoStatusEnum.CANCELAMENTO_NIP.getKey() + ")")
				.customConnection(customConnection)
				.searchCriterios(searchCriterios)
				.build();
		
		ait = search.getList(AitMovimento.class);
	    if (ait == null) {
	        throw new NoContentException("Nenhum movimento encontrado.");
	    }

	    return ait;
	}
	
	private static List<AitMovimento> aitMovimentos(int cdAit, CustomConnection customConnection) throws Exception {
		List<AitMovimento> aitMovimento = new ArrayList<AitMovimento>();
		SearchCriterios criterios = new SearchCriterios();
		criterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.fields("*")
				.customConnection(customConnection)
				.searchCriterios(criterios)
				.build();

		aitMovimento = search.getList(AitMovimento.class);
	    if (aitMovimento == null) {
	        throw new NoContentException("Nenhum movimento encontrado para este AIT.");
	    }

	    return aitMovimento;
	}
	
	private static int verificaCancelamento(int cdAit, int tpStatus, String dtMovimento) throws Exception{
		if (tpStatus == TipoStatusEnum.CANCELAMENTO_DEFESA_PREVIA.getKey()) {
			return cdMovimento(cdAit, TipoStatusEnum.DEFESA_PREVIA.getKey(), dtMovimento, new CustomConnection());
		} 
		else if (tpStatus == TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_PREVIA.getKey()) {
			return cdMovimento(cdAit, TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey(), dtMovimento, new CustomConnection());
		} 
		else if (tpStatus == TipoStatusEnum.CANCELAMENTO_FICI.getKey()) {
			return cdMovimento(cdAit, TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey(), dtMovimento, new CustomConnection());
		}
		else if (tpStatus == TipoStatusEnum.CANCELAMENTO_DEFESA_DEFERIDA.getKey()) {
			return cdMovimento(cdAit, TipoStatusEnum.DEFESA_DEFERIDA.getKey(), dtMovimento, new CustomConnection());
		}
		else if (tpStatus == TipoStatusEnum.CANCELAMENTO_DEFESA_INDEFERIDA.getKey()) {
			return cdMovimento(cdAit, TipoStatusEnum.DEFESA_INDEFERIDA.getKey(), dtMovimento, new CustomConnection());
		}
		else if (tpStatus == TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA.getKey()) {
			return cdMovimento(cdAit, TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey(), dtMovimento, new CustomConnection());
		}
		else if (tpStatus == TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA.getKey()) {
			return cdMovimento(cdAit, TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey(), dtMovimento, new CustomConnection());
		}
		else if (tpStatus == TipoStatusEnum.CANCELAMENTO_RECURSO_JARI.getKey()) {
			return cdMovimento(cdAit, TipoStatusEnum.RECURSO_JARI.getKey(), dtMovimento, new CustomConnection());
		}
		else if (tpStatus == TipoStatusEnum.CANCELAMENTO_JARI_COM_PROVIMENTO.getKey()) {
			return cdMovimento(cdAit, TipoStatusEnum.JARI_COM_PROVIMENTO.getKey(), dtMovimento, new CustomConnection());
		}
		else if (tpStatus == TipoStatusEnum.CANCELAMENTO_JARI_SEM_ACOLHIMENTO.getKey()) {
			return cdMovimento(cdAit, TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey(), dtMovimento, new CustomConnection());
		}
		else if (tpStatus == TipoStatusEnum.CANCELAMENTO_RECURSO_CETRAN.getKey()) {
			return cdMovimento(cdAit, TipoStatusEnum.RECURSO_CETRAN.getKey(), dtMovimento, new CustomConnection());
		}
		else if (tpStatus == TipoStatusEnum.CANCELAMENTO_NIP.getKey()) {
			return cdMovimento(cdAit, TipoStatusEnum.NIP_ENVIADA.getKey(), dtMovimento, new CustomConnection());
		}
		else {
			return -1;
		}
	}
	
	private static int cdMovimento(int cdAit, int tpStatus, String dtMovimento, CustomConnection customConnection) throws Exception {
		AitMovimento cdMovimento = new AitMovimento();
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, cdAit > 0);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", tpStatus, tpStatus > 0);
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.fields("*")
				.searchCriterios(searchCriterios)
				.additionalCriterias("A.dt_movimento <= " + "'" + dtMovimento + "'")
				.customConnection(customConnection)
				.build();
		
		List<AitMovimento> aitMovimento = search.getList(AitMovimento.class);
		if (aitMovimento.isEmpty()) {
		    return 0; 
		}

		cdMovimento = aitMovimento.get(0);

	    return cdMovimento.getCdMovimento();
	}
	
	private static String formataData(GregorianCalendar date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dataFormatada = dateFormat.format(date.getTime());
		
		return dataFormatada;
	}
	
	private static List<Integer> cancelamentoDuplicado(List<AitMovimento> movimentos) throws Exception {
		List<Integer> tpRepetido = new ArrayList<Integer>();
		List<Integer> numerosVistos = new ArrayList<Integer>();
		
		for (AitMovimento movimento : movimentos) {
	        int tpStatus = movimento.getTpStatus();

	        if (numerosVistos.contains(tpStatus)) {
	        	tpRepetido.add(tpStatus);
	        } else {
	            numerosVistos.add(tpStatus);
	        }
	    }
		
		return tpRepetido;
	}

	private static boolean verificaLgEnviadoDetran(List<Integer> listaTpStatus, int tpStatus, int lgEnviadoDetran) {
		for(Integer lista: listaTpStatus) {
			if(lista == tpStatus && lgEnviadoDetran == 1) {
				return true;
			}
		}
				
		return false;
	}
	
	
	private static List<AitMovimento> aitMovimentosParaAdicionarSne(CustomConnection customConnection) throws Exception {
        SearchCriterios criterios = new SearchCriterios();
        criterios.addCriteriosEqualInteger("A.st_adesao_sne", TipoAdesaoSneEnum.SEM_OPCAO_SNE.getKey());
        criterios.addCriteriosEqualInteger("A.tp_status", TipoStatusEnum.MULTA_PAGA.getKey());
        Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
                .fields("A.cd_Ait, A.cd_movimento, A.tp_status, A.dt_movimento, A.st_adesao_sne")
                .customConnection(customConnection)
                .searchCriterios(criterios)
                .additionalCriterias("EXISTS "
                        + "(SELECT 1 FROM mob_ait_movimento B "
                        + "WHERE B.cd_Ait = A.cd_Ait "
                        + " AND B.tp_status = "+ TipoStatusEnum.REGISTRO_INFRACAO.getKey()
                        + " AND B.st_adesao_sne = "+ TipoAdesaoSneEnum.COM_OPCAO_SNE.getKey()
                        +")")
                .orderBy("cd_ait DESC")
                .build();
        List<AitMovimento> aitMovimentoList = new ArrayList<>();
        aitMovimentoList = search.getList(AitMovimento.class);
        if (aitMovimentoList.isEmpty()) {
            throw new NoContentException("Nenhum movimento encontrado para este AIT.");
        }

        return aitMovimentoList;
    }

	public static void fixUpdateMovimentosSne(Integer a) {
		CustomConnection customConnection = null;
		try {
			customConnection = new CustomConnection();
			customConnection.initConnection(true);
			AitMovimentoRepository aitMovimentoRepository = new AitMovimentoRepositoryDAO();
			System.out.println("--------------- INICIADO----------------");
			List<AitMovimento> movimentoParaAtualizar = aitMovimentosParaAdicionarSne(customConnection);
			for (AitMovimento movimentos : movimentoParaAtualizar) {
				movimentos.setStAdesaoSne(TipoAdesaoSneEnum.COM_OPCAO_SNE.getKey());
				aitMovimentoRepository.update(movimentos, customConnection);
				System.out.println("cdAit: " + movimentos.getCdAit() + " tpStatus: " + movimentos.getTpStatus()
						+ " StSne: " + movimentos.getStAdesaoSne());
			}
			System.out.println(movimentoParaAtualizar.size() + " movimentos atualizados.");
			System.out.println("----------------- FINALIZADO --------------------");
			customConnection.finishConnection();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				customConnection.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}