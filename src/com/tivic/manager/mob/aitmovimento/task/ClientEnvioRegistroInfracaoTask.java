package com.tivic.manager.mob.aitmovimento.task;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.SituacaoAitEnum;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ClientEnvioRegistroInfracaoTask implements IClientEnvioRegistroInfracaoTask {
	
	private ManagerLog managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	private CustomConnection customConnection;
	
	public ClientEnvioRegistroInfracaoTask() throws Exception {
		this.customConnection = new CustomConnection();
	}
	
	@Override
    public void taskEnviarMovimentoRegistroInfracao() throws Exception {
        try {
    		ServicoDetranServices serviceDetran = ServicoDetranServicesFactory.gerarServico();
            this.customConnection.initConnection(true);
            managerLog.info("TASK ENVIO REGISTROS DE INFRAÇÃO INICIADA: ", new GregorianCalendar().getTime().toString());
            List<AitMovimento> aitMovimentoList = buscarRegistrosInfracao();
            managerLog.info("QUANTIDADE DE REGISTROS DE INFRAÇÃO: ", String.valueOf(aitMovimentoList.size()));
            serviceDetran.remessa(aitMovimentoList);
            this.customConnection.finishConnection();
        } catch (NoContentException nce) {
            managerLog.info("NENHUM MOVIMENTO DE REGISTRO DE INFRAÇÃO ENCONTRADO: ", new GregorianCalendar().getTime().toString());
        } catch (Exception e) {
            managerLog.showLog(e);
        } finally {
            this.customConnection.closeConnection();
            managerLog.info("TASK ENVIO REGISTROS DE INFRAÇÃO FINALIZADA: ", new GregorianCalendar().getTime().toString());
        }
    }
	
	public List<AitMovimento> buscarRegistrosInfracao() throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
	    List<AitMovimento> registroInfracaoList = new SearchBuilder<AitMovimento>(" mob_ait_movimento A")
	            .fields(" DISTINCT A.*, B.cd_ait, C.cd_arquivo_movimento ")    
	            .addJoinTable(" JOIN mob_ait B ON (A.cd_ait = B.cd_ait) ")
	            .addJoinTable(" LEFT OUTER JOIN mob_arquivo_movimento C ON (A.cd_ait = C.cd_ait AND A.cd_movimento = C.cd_movimento) ")
	            .additionalCriterias(" C.cd_arquivo_movimento is null "
	            		+ "	AND A.tp_status = " + TipoStatusEnum.REGISTRO_INFRACAO.getKey() 
	            		+ " AND A.lg_enviado_detran = " + TipoStatusEnum.CADASTRADO.getKey()
	            		+ " AND B.st_ait = " + SituacaoAitEnum.ST_CONFIRMADO.getKey()
	            		+ " AND ("
	            		+ "		B.tp_status IN (" + TipoStatusEnum.CADASTRADO.getKey() 
	            		+ "     , " + TipoStatusEnum.REGISTRO_INFRACAO.getKey() 
	            		+ "     ) OR B.tp_status IS NULL"
	            		+ "	) "
	            )
				.searchCriterios(searchCriterios)
	            .orderBy(" A.dt_movimento DESC ")
	            .build()
	            .getList(AitMovimento.class);

	    if (registroInfracaoList.isEmpty()) {
	        throw new NoContentException("Não há Registros de Infração pendentes de envio.");
	    }
	    return registroInfracaoList;
	}

}
