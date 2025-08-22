package com.tivic.manager.mob.lote.impressao;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.mob.ait.IAitService;
import com.tivic.manager.mob.correios.ICorreiosEtiquetaService;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaBuilder;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaService;
import com.tivic.manager.mob.lote.impressao.viaunica.nip.IVerificaInfracaoAdvertencia;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.SearchCriterios;

public class ClientNotificacaoTask implements IClientNotificacaoTask {
	private IAitService aitService;
	private ILoteNotificacaoService loteNotificacaoService;
	private int userInitTask = ParametroServices.getValorOfParametroAsInteger("MOB_USER_TIVIC", 1);
	private ManagerLog managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	private CustomConnection customConnection;
	private List<Ait> nips;
	private List<Ait> advertencias;
	private IVerificaInfracaoAdvertencia verificaInfracaoAdvertencia;
	private ICorreiosEtiquetaService correiosEtiquetaService;
	
	public ClientNotificacaoTask() throws Exception {
		this.loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
		this.aitService = (IAitService) BeansFactory.get(IAitService.class);
		this.customConnection = new CustomConnection();
		this.verificaInfracaoAdvertencia = (IVerificaInfracaoAdvertencia) BeansFactory.get(IVerificaInfracaoAdvertencia.class);
		this.correiosEtiquetaService = (ICorreiosEtiquetaService) BeansFactory.get(ICorreiosEtiquetaService.class);
	}

	@Override
	public void taskGerarMovimentoNAI() throws NoContentException, Exception {
		try {
			this.customConnection.initConnection(false);
			managerLog.info("TASK NAI INICIADA: ", new GregorianCalendar().getTime().toString());
			List<Ait> aitList = loteNotificacaoService.getAllAitsEmitirNAI(this.customConnection);
			managerLog.info("QUANTIDADE NAI CANDIDATAS: ", String.valueOf(aitList.size()));
			List<Ait> listAitAtualizada = buscarDadosAITDetran(aitList, TipoStatusEnum.NAI_ENVIADO.getKey());
			loteNotificacaoService.gerarMovimentoNotificacaoNaiLote(listAitAtualizada, userInitTask);
			this.customConnection.finishConnection();
		} catch(NoContentException nce){
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
			List<Ait> aitList = loteNotificacaoService.getAllAitsEmitirFimPrazoDefesa(this.customConnection);
			managerLog.info("QUANTIDADE FIM PRAZO DEFESA CANDIDATAS: ", String.valueOf(aitList.size()));
			List<Ait> listAitAtualizada = buscarDadosAITDetran(aitList, TipoStatusEnum.FIM_PRAZO_DEFESA.getKey());
			loteNotificacaoService.gerarMovimentoNotificacaoFimPrazoDefesa(listAitAtualizada, userInitTask);
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
			List<Ait> aitList = loteNotificacaoService.getAllAitsEmitirNIP(this.customConnection);
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
	    new GerarMovimentoNotificacaoAdvertenciaLote().gerarMovimentos(listAdvertenciaAtualizada, userInitTask);
	}
	
	private void gerarMovimentoNip() throws Exception {
		List<Ait> listNipAtualizada = buscarDadosAITDetran(nips, TipoStatusEnum.NIP_ENVIADA.getKey());
	    loteNotificacaoService.gerarMovimentoNotificacaoNipLote(listNipAtualizada, userInitTask);
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
		int tpErroAoConsultarDadosDetran = ParametroServices.getValorOfParametroAsInteger("MOB_INCONSISTENCIA_ERRO_AO_CONSULTAR_DADOS_DETRAN", 0);
		AitInconsistencia aitInconsistencia = new AitInconsistenciaBuilder().build(ait, tpErroAoConsultarDadosDetran, tpStatus);
		new AitInconsistenciaService().salvarInconsistencia(aitInconsistencia);
	}
	
	@Override
	public void taskGerarLoteImpressaoNAI() throws ValidacaoException, Exception {
		try {
			this.customConnection.initConnection(false);
			managerLog.info("TASK LOTE IMPRESSAO NAI INICIADA: ", new GregorianCalendar().getTime().toString());
			criacaoLoteImpressao(TipoLoteNotificacaoEnum.LOTE_NAI.getKey());		
			this.customConnection.finishConnection();
		} catch(NoContentException nce){
			managerLog.info("NENHUMA NAI ENCONTRADA: ", new GregorianCalendar().getTime().toString());
		} catch(Exception e) {
			managerLog.showLog(e);
		} finally {
			this.customConnection.closeConnection();
			managerLog.info("TASK LOTE IMPRESSAO NAI FINALIZADA: ", new GregorianCalendar().getTime().toString());			
		}		
	}

	private List<LoteImpressaoAit> montarAitList(List<AitDTO> aitDtoList, CustomConnection customConnection) throws Exception {
		List<LoteImpressaoAit> aitList = new ArrayList<LoteImpressaoAit>();
		for (AitDTO aitDto : aitDtoList) {
			LoteImpressaoAit loteImpressaoAit = new LoteImpressaoAit(); 
			if(aitDto.getCdAit() > 0 ) {
				loteImpressaoAit.setCdAit(aitDto.getCdAit());
				aitList.add(loteImpressaoAit);
			}
		}
		return aitList;
	}
	
	@Override
	public void taskGerarLoteImpressaoNIP() throws ValidacaoException, Exception {
		try {
			this.customConnection.initConnection(false);
			managerLog.info("TASK LOTE IMPRESSAO NIP INICIADA: ", new GregorianCalendar().getTime().toString());
			criacaoLoteImpressao(TipoLoteNotificacaoEnum.LOTE_NIP.getKey());
			this.customConnection.finishConnection();
		} catch(NoContentException nce){
			managerLog.info("NENHUMA NIP ENCONTRADA: ", new GregorianCalendar().getTime().toString());
		} catch(Exception e) {
			managerLog.showLog(e);
		} finally {
			this.customConnection.closeConnection();
			managerLog.info("TASK LOTE IMPRESSAO NIP FINALIZADA: ", new GregorianCalendar().getTime().toString());			
		}		
	}
	
	private void criacaoLoteImpressao(int tpLote)  throws ValidacaoException, Exception {
		boolean isTask = true;
		String nmTipoLote = tpLote == TipoLoteNotificacaoEnum.LOTE_NIP.getKey() ? "NIP": "NAI";
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("K.tp_status", tpLote);
		List<AitDTO> aitDtoList =  loteNotificacaoService.buscarAitsParaLoteImpressao(tpLote, searchCriterios).getItems();
		managerLog.info("QUANTIDADE "+nmTipoLote+" CANDIDATAS: ", String.valueOf(aitDtoList.size()));
		LoteImpressao loteImpressao = new LoteImpressaoBuilder()
				.addAits(montarAitList(aitDtoList, customConnection))
				.addCdUsuario(userInitTask)
				.addTpLoteImpressao(tpLote)
				.build();
		loteImpressao = loteNotificacaoService.gerarLoteNotificacao(loteImpressao, isTask);
		managerLog.info("LOTE IMPRESSAO CRIADO: ", loteImpressao.toString());
		loteNotificacaoService.iniciarGeracaoDocumentos(loteImpressao.getCdLoteImpressao(), userInitTask);
		managerLog.info("GERAÇÃO DO LOTE IMPRESSAO INICIADO: ", loteImpressao.toString());		
	}
	
	@Override
	public void taskArquivoRetornoCorreios() throws ValidacaoException, Exception {
		try {
			managerLog.info("TASK ARQUIVO RETORNO CORREIOS INICIADA: ", new GregorianCalendar().getTime().toString());
			correiosEtiquetaService.getFilesCorreios(userInitTask);
		} catch(NoContentException nce){
			managerLog.info("NENHUMA ARQUIVO ENCONTRADO: ", new GregorianCalendar().getTime().toString());
		} catch(Exception e) {
			managerLog.showLog(e);
		} finally {
			managerLog.info("TASK ARQUIVO RETORNO CORREIOS FINALIZADA: ", new GregorianCalendar().getTime().toString());	
		}
	}
}
