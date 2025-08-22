package com.tivic.manager.mob.ait.sync;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoSituacaoAitEnum;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.StConsistenciaAitEnum;
import com.tivic.manager.mob.ait.sync.builders.AitSyncDTOBuilder;
import com.tivic.manager.mob.ait.sync.entities.AitSyncResponse;
import com.tivic.manager.mob.ait.sync.entities.SyncResponse;
import com.tivic.manager.mob.ait.sync.observer.IAitSyncSubject;
import com.tivic.manager.mob.aitimagem.IAitImagemService;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.log.builders.InfoLogBuilder;
import com.tivic.sol.util.date.DateUtil;

@SuppressWarnings("unchecked")
public class AitSyncService implements IAitSyncService {
	
	private IAitImagemService aitImagemService;
    private IAtualizaTalonarioAit atualizaTalonarioAit;
    private IAitSyncTools<Ait> aitSyncTools;
    private ManagerLog managerLog;
    private ConversorBaseAntigaNovaFactory conversorBaseAntigaNovaFactory;
    private IAitSyncSubject aitSyncSubject;
	
	public AitSyncService() throws Exception {
		aitImagemService = (IAitImagemService)  BeansFactory.get(IAitImagemService.class);
		atualizaTalonarioAit = (IAtualizaTalonarioAit) BeansFactory.get(IAtualizaTalonarioAit.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		aitSyncTools = (IAitSyncTools<Ait>) BeansFactory.get(IAitSyncTools.class);
		aitSyncSubject = (IAitSyncSubject) BeansFactory.get(IAitSyncSubject.class);
		conversorBaseAntigaNovaFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);
	}
	
	@Override
	public List<AitSyncResponse> syncReceive(List<Ait> aitList) throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    List<Integer> cdAitPersistedList = new ArrayList<>();
	    List<AitSyncResponse> aitSyncResponseList = null;
	    
	    try {
	        customConnection.initConnection(true);
	        aitSyncResponseList = syncReceive(aitList, cdAitPersistedList, customConnection);
	        customConnection.finishConnection();
	        aitSyncSubject.notifyObservers(cdAitPersistedList);
	    } finally {
	        customConnection.closeConnection();
	    }
	    
	    return aitSyncResponseList;
	}

	@Override
	public List<AitSyncResponse> syncReceive(List<Ait> aitList, List<Integer> cdAitPersistedList, CustomConnection customConnection) throws Exception {
	    List<Ait> list = aitSyncTools.verificarAitsDuplicados(aitList);
	    atualizaTalonarioAit.atualizar(aitList, customConnection);
	    
	    List<AitSyncResponse> aitSyncResponseList = new ArrayList<>();
	    boolean isparametroConsistenciaOn = parametroConsistencia();
	    
	    for(Ait ait: list) {            
	        boolean aitexistente = conversorBaseAntigaNovaFactory.getAitRepository().hasAit(ait.getIdAit(), customConnection);
	        if(aitexistente) {
	            managerLog.showLog(new InfoLogBuilder("AIT " + ait.getIdAit(), "Já sincronizado anteriormente...").build());
	            aitSyncTools.setSincronizados(ait.getIdAit(), aitSyncResponseList, false, true);
	            continue;
	        }
	        
	        ait.setDtDigitacao(new GregorianCalendar());
	        ait.setStAit(!isparametroConsistenciaOn && ait.getCdOcorrencia() == 0
	                ? StConsistenciaAitEnum.ST_CONSISTENTE.getKey() 
	                : StConsistenciaAitEnum.ST_PENDENTE_CONFIRMACAO.getKey());
	        
	        conversorBaseAntigaNovaFactory.getAitRepository().insert(ait, customConnection);
	        criarMovimento(ait, customConnection);
	        managerLog.showLog(new InfoLogBuilder("AIT " + ait.getIdAit(), "Sincronizado com sucesso...").build());
	        aitImagemService.insertImageSync(ait, customConnection);
	        aitSyncTools.setSincronizados(ait.getIdAit(), aitSyncResponseList, true, false);
	        cdAitPersistedList.add(ait.getCdAit());
	    }
	    return aitSyncResponseList;
	}
	
	private boolean parametroConsistencia() throws ValidacaoException {
		int orgaoOptanteConsistencia = ParametroServices.getValorOfParametroAsInteger("LG_OBRIGAR_CONSISTENCIA_AIT",-1);
		if(orgaoOptanteConsistencia == -1) {
			throw new ValidacaoException("O parâmetro LG_OBRIGAR_CONSISTENCIA_AIT não foi configurado.");
		}
		return orgaoOptanteConsistencia == 1 ? true : false;
	}
	
	private void criarMovimento(Ait ait, CustomConnection customConnection) throws Exception {
		AitMovimento movimento = new AitMovimentoBuilder()
				.setCdAit(ait.getCdAit())
				.setDtMovimento(DateUtil.getDataAtual())
				.setTpStatus(TipoStatusEnum.REGISTRO_INFRACAO.getKey())
				.setLgEnviadoDetran(TipoSituacaoAitEnum.NAO_ENVIADO.getKey())
				.setDtDigitacao(DateUtil.getDataAtual())
				.setCdUsuario(ait.getCdUsuario())
				.setDtDigitacao(ait.getDtDigitacao())
			.build();
		conversorBaseAntigaNovaFactory.getAitMovimentoRepository().insert(movimento, customConnection);	
	}
	
	@Override
	public List<SyncResponse<?>> sync(int cdAgente) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<SyncResponse<?>> aitSyncDTO = sync(cdAgente, customConnection);
			customConnection.finishConnection();
			return aitSyncDTO;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<SyncResponse<?>> sync(int cdAgente, CustomConnection customConnection) throws Exception {
		List<SyncResponse<?>> aitSyncDTO = new AitSyncDTOBuilder()
				.addAllAitSync(cdAgente, customConnection)
				.build();
		return aitSyncDTO;
	}
}