package com.tivic.manager.mob.lotes.task.publicacao;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.lotes.enums.publicacao.TipoLotePublicacaoEnum;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacao;
import com.tivic.manager.mob.lotes.service.publicacao.ILotePublicacaoService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class ClientPublicacaoNotificacaoTask implements IClientPublicacaoNotificacaoTask {
	
	private ILotePublicacaoService lotePublicacaoService;
	private ManagerLog managerLog;
	private CustomConnection customConnection;
	
	public ClientPublicacaoNotificacaoTask() throws Exception {
		this.lotePublicacaoService = (ILotePublicacaoService) BeansFactory.get(ILotePublicacaoService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.customConnection = new CustomConnection();
	}

	@Override
	public void taskEnviarPublicacaoNAI() throws NoContentException, Exception {
		try {
			this.customConnection.initConnection(true);
			managerLog.info("TASK PUBLICAÇÃO NAI INICIADA: ", new GregorianCalendar().getTime().toString());
			List<LotePublicacao> lotePublicacaoList = this.lotePublicacaoService
					.buscarLotesAguardandoEnvio(TipoLotePublicacaoEnum.LOTE_PUBLICACAO_NAI.getKey());
			managerLog.info("Quantidade de Lotes Pendentes: ", String.valueOf(lotePublicacaoList.size()));
			for (LotePublicacao lotePublicacao :  lotePublicacaoList) {
				managerLog.info("Enviando Publicações para Lote : " + lotePublicacao.getCdLotePublicacao(), new GregorianCalendar().getTime().toString());
				this.lotePublicacaoService.enviarPublicacoesLote(lotePublicacao, this.customConnection);
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
			List<LotePublicacao> lotePublicacaoList = this.lotePublicacaoService
					.buscarLotesAguardandoEnvio(TipoLotePublicacaoEnum.LOTE_PUBLICACAO_NIP.getKey());
			managerLog.info("Quantidade de Lotes Pendentes: ", String.valueOf(lotePublicacaoList.size()));
			for (LotePublicacao lotePublicacao :  lotePublicacaoList) {
				managerLog.info("Enviando Publicações para Lote : " + lotePublicacao.getCdLotePublicacao(), new GregorianCalendar().getTime().toString());
				this.lotePublicacaoService.enviarPublicacoesLote(lotePublicacao, this.customConnection);
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
