package com.tivic.manager.mob.lotes.task.notificacao;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.mob.ait.IAitService;
import com.tivic.manager.mob.correios.ICorreiosEtiquetaService;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaBuilder;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaService;
import com.tivic.manager.mob.lotes.movimento.IVerificaInfracaoAdvertencia;
import com.tivic.manager.mob.lotes.movimento.lote.GeraMovimentoNotificacaoNipLote;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class ClientNotificacaoTask implements IClientNotificacaoTask {
	private IAitService aitService;
	private ILoteImpressaoService loteImpressaoService;
	private IParametroRepository parametroRepository;
	private int userInitTask = ParametroServices.getValorOfParametroAsInteger("MOB_USER_TIVIC", 1);
	private ManagerLog managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	private CustomConnection customConnection;
	private List<Ait> nips;
	private List<Ait> advertencias;
	private IVerificaInfracaoAdvertencia verificaInfracaoAdvertencia;
	private ICorreiosEtiquetaService correiosEtiquetaService;

	public ClientNotificacaoTask() throws Exception {
		this.loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
		this.aitService = (IAitService) BeansFactory.get(IAitService.class);
		this.customConnection = new CustomConnection();
		this.verificaInfracaoAdvertencia = (IVerificaInfracaoAdvertencia) BeansFactory.get(IVerificaInfracaoAdvertencia.class);
		this.correiosEtiquetaService = (ICorreiosEtiquetaService) BeansFactory.get(ICorreiosEtiquetaService.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}

	@Override
	public void taskGerarMovimentoNAI() throws NoContentException, Exception {
		try {
			this.customConnection.initConnection(false);
			managerLog.info("TASK NAI INICIADA: ", new GregorianCalendar().getTime().toString());
			List<Ait> aitList = loteImpressaoService.getAllAitsEmitirNAI(this.customConnection);
			managerLog.info("QUANTIDADE NAI CANDIDATAS: ", String.valueOf(aitList.size()));
			List<Ait> listAitAtualizada = buscarDadosAITDetran(aitList, TipoStatusEnum.NAI_ENVIADO.getKey());
			loteImpressaoService.gerarMovimentoNotificacaoNaiLote(listAitAtualizada, this.userInitTask);
			this.customConnection.finishConnection();
		} catch(NoContentException nce) { 
			managerLog.info("NENHUMA NAI ENCONTRADA: ", new GregorianCalendar().getTime().toString()); 
		} catch(Exception e) {
			managerLog.showLog(e);
		} finally {
			this.customConnection.closeConnection();
			managerLog.info("TASK NAI FINALIZADA: ", new GregorianCalendar().getTime().toString());
		}
	}
	
	@Override
	public void taskGerarMovimentoFimPrazoDefesa() throws ValidacaoException, Exception {
		try {
			this.customConnection.initConnection(false);
			managerLog.info("TASK FIM PRAZO DEFESA INICIADA: ", new GregorianCalendar().getTime().toString());
			List<Ait> aitList = loteImpressaoService.getAllAitsEmitirFimPrazoDefesa(this.customConnection);
			managerLog.info("QUANTIDADE FIM PRAZO DEFESA CANDIDATAS: ", String.valueOf(aitList.size()));
			List<Ait> listAitAtualizada = buscarDadosAITDetran(aitList, TipoStatusEnum.FIM_PRAZO_DEFESA.getKey());
			loteImpressaoService.gerarMovimentoNotificacaoFimPrazoDefesa(listAitAtualizada, this.userInitTask);
			this.customConnection.finishConnection();
		} catch(NoContentException nce){
			managerLog.info("NENHUMA FIM PRAZO DEFESA ENCONTRADO: ", new GregorianCalendar().getTime().toString());
		} catch(Exception e) {
			managerLog.showLog(e);
		} finally {
			this.customConnection.closeConnection();
			managerLog.info("TASK FIM PRAZO DEFESA FINALIZADA: ", new GregorianCalendar().getTime().toString());			
		}
	}
	
	@Override
	public void taskGerarMovimentoNIP() throws ValidacaoException, Exception {
		try {
			taskGerarMovimentoFimPrazoDefesa();
			this.customConnection.initConnection(false);
			managerLog.info("TASK NIP INICIADA: ", new GregorianCalendar().getTime().toString());
			List<Ait> aitList = loteImpressaoService.getAllAitsEmitirNIP(this.customConnection);
			managerLog.info("QUANTIDADE TOTAL DE CANDIDATAS: ", String.valueOf(aitList.size()));
			filtrarNipsEAdvertencias(aitList);
			gerarMovimentoAdvertencia();
			gerarMovimentoNip();
			this.customConnection.finishConnection();
		} catch(NoContentException nce){
			managerLog.info("NENHUMA NIP ENCONTRADA: ", new GregorianCalendar().getTime().toString());
		} catch(Exception e) {
			managerLog.showLog(e);
		} finally {
			this.customConnection.closeConnection();
			managerLog.info("TASK NIP FINALIZADA: ", new GregorianCalendar().getTime().toString());			
		}
	}
	
	private void filtrarNipsEAdvertencias(List<Ait> aitList) throws Exception {
	    advertencias = new ArrayList<>();
	    nips = new ArrayList<>();
	    for (Ait ait : aitList) {   
	    	if (verificaInfracaoAdvertencia.isPassivelAdvertencia(ait.getCdAit())) {
	    		advertencias.add(ait);
	    	} else {
	    		nips.add(ait);
	    	}
	    }
	}
	
	private void gerarMovimentoAdvertencia() throws Exception {
	    List<Ait> listAdvertenciaAtualizada = buscarDadosAITDetran(advertencias, TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey());
	    new GeraMovimentoNotificacaoNipLote().gerarMovimentos(listAdvertenciaAtualizada, this.userInitTask);
	}
	
	private void gerarMovimentoNip() throws Exception {
		List<Ait> listNipAtualizada = buscarDadosAITDetran(nips, TipoStatusEnum.NIP_ENVIADA.getKey());
	    loteImpressaoService.gerarMovimentoNotificacaoNipLote(listNipAtualizada, this.userInitTask);
	}
	
	private List<Ait> buscarDadosAITDetran(List<Ait> aitList, int tpStatus) throws Exception {
		List<Ait> listAitAtualizada = new ArrayList<Ait>();
		try {
			this.customConnection.initConnection(true);
			for (Ait ait: aitList) {
				try {
					listAitAtualizada.add(aitService.updateDetran(ait, this.customConnection));
				} catch (Exception e) {
					e.printStackTrace();
					managerLog.info("Erro ao buscar dados DETRAN AIT: ", ait.getIdAit() + "   " + e.getMessage());
					salvarInconsistenciaDeConsulta(ait, tpStatus);
					continue;
				}
			}
			this.customConnection.finishConnection();
			return listAitAtualizada;
		} finally {
			this.customConnection.closeConnection();
		}
	}
	
	private void salvarInconsistenciaDeConsulta(Ait ait, int tpStatus) throws Exception {
		managerLog.info("Salvando inconsistencia: ", ait.getIdAit());
		int tpErroAoConsultarDadosDetran = parametroRepository.getValorOfParametroAsInt("MOB_INCONSISTENCIA_ERRO_AO_CONSULTAR_DADOS_DETRAN");
		AitInconsistencia aitInconsistencia = new AitInconsistenciaBuilder().build(ait, tpErroAoConsultarDadosDetran, tpStatus);
		new AitInconsistenciaService().salvarInconsistencia(aitInconsistencia);
	}
	
	@Override
	public void taskArquivoRetornoCorreios() throws ValidacaoException, Exception {
		try {
			managerLog.info("TASK ARQUIVO RETORNO CORREIOS INICIADA: ", new GregorianCalendar().getTime().toString());
			correiosEtiquetaService.getFilesCorreios(this.userInitTask);
		} catch(NoContentException nce){
			managerLog.info("NENHUMA ARQUIVO ENCONTRADO: ", new GregorianCalendar().getTime().toString());
		} catch(Exception e) {
			managerLog.showLog(e);
		} finally {
			managerLog.info("TASK ARQUIVO RETORNO CORREIOS FINALIZADA: ", new GregorianCalendar().getTime().toString());	
		}
	}

}