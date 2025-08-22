package com.tivic.manager.mob.lote.impressao.publicacao.service;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.TipoLoteDocumentoEnum;
import com.tivic.manager.mob.lote.impressao.publicacao.IPublicacaoNotificacaoService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class ClientPublicacaoNotificacaoTask implements IClientPublicacaoNotificacaoTask {
	
	private IPublicacaoNotificacaoService publicacaoNotificacaoService;
	private ManagerLog managerLog;
	private CustomConnection customConnection;
	
	public ClientPublicacaoNotificacaoTask() throws Exception {
		this.publicacaoNotificacaoService = (IPublicacaoNotificacaoService) BeansFactory.get(IPublicacaoNotificacaoService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.customConnection = new CustomConnection();
	}

	@Override
	public void taskEnviarPublicacaoNAI() throws NoContentException, Exception {
		try {
			this.customConnection.initConnection(true);
			managerLog.info("TASK PUBLICAÇÃO NAI INICIADA: ", new GregorianCalendar().getTime().toString());
			List<LoteImpressao> lotePublicacaoList = this.publicacaoNotificacaoService
					.buscarLotesAguardandoEnvio(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_NAI.getKey());
			managerLog.info("Quantidade de Lotes Pendentes: ", String.valueOf(lotePublicacaoList.size()));
			for (LoteImpressao loteImpressao :  lotePublicacaoList) {
				managerLog.info("Enviando Publicações para Lote : " + loteImpressao.getCdLoteImpressao(), new GregorianCalendar().getTime().toString());
				this.publicacaoNotificacaoService.enviarPublicacoesLote(loteImpressao, this.customConnection);
			}
			this.customConnection.finishConnection();
		} catch(NoContentException nce){
			managerLog.info("NENHUMA PUBLICAÇÃO NAI ENCONTRADA: ", new GregorianCalendar().getTime().toString());
		} catch(Exception e) {
			managerLog.showLog(e);
		} finally {
			this.customConnection.closeConnection();
			managerLog.info("TASK PUBLICAÇÃO NAI FINALIZADA: ", new GregorianCalendar().getTime().toString());			
					
		}
	}

	@Override
	public void taskEnviarPublicacaoNIP() throws Exception {
		try {
			this.customConnection.initConnection(true);
			managerLog.info("TASK PUBLICAÇÃO NIP INICIADA: ", new GregorianCalendar().getTime().toString());
			List<LoteImpressao> lotePublicacaoList = this.publicacaoNotificacaoService
					.buscarLotesAguardandoEnvio(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_NIP.getKey());
			managerLog.info("Quantidade de Lotes Pendentes: ", String.valueOf(lotePublicacaoList.size()));
			for (LoteImpressao loteImpressao :  lotePublicacaoList) {
				managerLog.info("Enviando Publicações para Lote : " + loteImpressao.getCdLoteImpressao(), new GregorianCalendar().getTime().toString());
				this.publicacaoNotificacaoService.enviarPublicacoesLote(loteImpressao, this.customConnection);
			}
			this.customConnection.finishConnection();
		} catch(NoContentException nce){
			managerLog.info("NENHUMA PUBLICAÇÃO NIP ENCONTRADA: ", new GregorianCalendar().getTime().toString());
		} catch(Exception e) {
			managerLog.showLog(e);
		} finally {
			this.customConnection.closeConnection();
			managerLog.info("TASK PUBLICAÇÃO NIP FINALIZADA: ", new GregorianCalendar().getTime().toString());			
					
		}
	}

}
