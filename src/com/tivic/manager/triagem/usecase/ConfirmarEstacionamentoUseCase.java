package com.tivic.manager.triagem.usecase;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.EventoArquivo;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.manager.triagem.dtos.ProcessamentoEventoDTO;
import com.tivic.manager.triagem.dtos.RetornoTriagemEventoEstacionamentoDTO;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigital;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigitalDetalhes;
import com.tivic.manager.triagem.exceptions.AitByEventoNotFoundException;
import com.tivic.manager.triagem.exceptions.EventoArquivoNotFoundException;
import com.tivic.manager.triagem.exceptions.EventoEstacionamentoDetalhesNotFoundException;
import com.tivic.manager.triagem.exceptions.EventoEstacionamentoNotFoundException;
import com.tivic.manager.triagem.repositories.IEventoEstacionamentoDigitalDetalhesRepository;
import com.tivic.manager.triagem.repositories.IEventoEstacionamentoDigitalRepository;
import com.tivic.manager.triagem.services.arquivo.IEventoArquivoEstacionamentoService;
import com.tivic.manager.triagem.services.eventos.IEventoEstacionamentoService;
import com.tivic.manager.triagem.validator.ProcessamentoEventoValidatorBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ConfirmarEstacionamentoUseCase {
	
	private final IEventoEstacionamentoService eventoEstacionamentoService;
	private final IEventoEstacionamentoDigitalRepository eventoEstacionamentoDigitalRepository;
	private final IEventoArquivoEstacionamentoService eventoArquivoEstacionamentoService;
	private final IEventoEstacionamentoDigitalDetalhesRepository eventoEstacionamentoDigitalDetalhesRepository;
	private ConversorBaseAntigaNovaFactory conversorBaseAntigaNovaFactory;

	public ConfirmarEstacionamentoUseCase() throws Exception {
		eventoEstacionamentoService = (IEventoEstacionamentoService) BeansFactory.get(IEventoEstacionamentoService.class);
		eventoEstacionamentoDigitalRepository = (IEventoEstacionamentoDigitalRepository) BeansFactory.get(IEventoEstacionamentoDigitalRepository.class);
		eventoArquivoEstacionamentoService = (IEventoArquivoEstacionamentoService) BeansFactory.get(IEventoArquivoEstacionamentoService.class);
		eventoEstacionamentoDigitalDetalhesRepository = (IEventoEstacionamentoDigitalDetalhesRepository) BeansFactory.get(IEventoEstacionamentoDigitalDetalhesRepository.class);
		conversorBaseAntigaNovaFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);;
	}

	public RetornoTriagemEventoEstacionamentoDTO execute(ProcessamentoEventoDTO processamentoEventoDTO) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			
			new ProcessamentoEventoValidatorBuilder().validate(processamentoEventoDTO, customConnection);
			
			setDependencias(processamentoEventoDTO, customConnection);
			Ait ait = confirmarAit(processamentoEventoDTO, customConnection);
	        
	        EventoEstacionamentoDigital eventoEstacionamento = buscarEventoEstacionamento(processamentoEventoDTO.getCdEvento(), customConnection);
			customConnection.finishConnection();
			return mapearParaRetornoTriagem(ait, eventoEstacionamento);
		} finally {
			customConnection.closeConnection();
		}
    }
    
    private Ait buscarAitPorEvento(int cdEvento, CustomConnection customConnection) throws Exception {
        SearchCriterios criterios = new SearchCriterios();
        criterios.addCriteriosEqualInteger("cd_evento_equipamento", cdEvento);
        
        List<Ait> aits = conversorBaseAntigaNovaFactory.getAitRepository().find(criterios, customConnection);
        
        if (aits.isEmpty())
            throw new AitByEventoNotFoundException(cdEvento);
        
        return aits.get(0);
    }
    
    private EventoEstacionamentoDigital buscarEventoEstacionamento(int cdEvento, CustomConnection customConnection) throws Exception {
        EventoEstacionamentoDigital evento = eventoEstacionamentoDigitalRepository.get(cdEvento, customConnection);
        
        if (evento == null)
            throw new EventoEstacionamentoNotFoundException(cdEvento);
        
        return evento;
    }
    
    private Ait confirmarAit(ProcessamentoEventoDTO processamentoEventoDTO, CustomConnection customConnection) throws Exception {
		eventoEstacionamentoService.confirmar(processamentoEventoDTO, customConnection);
        Ait ait = buscarAitPorEvento(processamentoEventoDTO.getCdEvento(), customConnection);
        ait.setDsObservacao(processamentoEventoDTO.getTxtObservacaoInfracao());
        return conversorBaseAntigaNovaFactory.getAitRepository().update(ait, customConnection);
    }
    
    private void setDependencias(ProcessamentoEventoDTO eventoDTO, CustomConnection customConnection) throws Exception {
        atualizarImagemPrincipal(eventoDTO, customConnection);
        atualizarDetalhesNotificacao(eventoDTO, customConnection);
    }

    private void atualizarImagemPrincipal(ProcessamentoEventoDTO eventoDTO, CustomConnection connection) throws Exception {
        List<EventoArquivo> arquivos = eventoArquivoEstacionamentoService
            .getEventoArquivosByEvento(eventoDTO.getCdEvento(), connection);
        
        if (arquivos == null)
            throw new EventoArquivoNotFoundException(eventoDTO.getCdEvento());
            
        for (EventoArquivo arquivo : arquivos) {
            if (arquivo.getCdArquivo() == eventoDTO.getCdMelhorImagem()) {
                arquivo.setLgImpressao(1);
                eventoArquivoEstacionamentoService.update(arquivo, connection);
                break;
            }
        }
    }

    private void atualizarDetalhesNotificacao(ProcessamentoEventoDTO eventoDTO, CustomConnection connection) throws Exception {
        EventoEstacionamentoDigitalDetalhes detalhes = eventoEstacionamentoDigitalDetalhesRepository
            .getByCdEvento(eventoDTO.getCdEvento(), connection);
        
        if (detalhes == null)
            throw new EventoEstacionamentoDetalhesNotFoundException(eventoDTO.getCdEvento());
        
        detalhes.setNmRuaNotificacao(eventoDTO.getNmRuaNotificacao());
        detalhes.setNrImovelReferencia(eventoDTO.getNrImovelReferencia());
        eventoEstacionamentoDigitalDetalhesRepository.update(detalhes, connection);
    }
    
    private RetornoTriagemEventoEstacionamentoDTO mapearParaRetornoTriagem(Ait ait, EventoEstacionamentoDigital eventoDigital) {
        return RetornoTriagemEventoEstacionamentoDTO.builder()
            .cdAit(ait.getCdAit())
            .cdEventoEquipamento(ait.getCdEventoEquipamento())
            .idAit(ait.getIdAit())
            .vlLatitude(ait.getVlLatitude())
            .vlLongitude(ait.getVlLongitude())
            .nrNotificacao(eventoDigital.getNrNotificacao())
            .build();
    }

}
