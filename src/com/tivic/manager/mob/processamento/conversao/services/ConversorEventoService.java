package com.tivic.manager.mob.processamento.conversao.services;

import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.arquivo.IArquivoService;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitDtInfracaoDeserialize;
import com.tivic.manager.mob.AitEvento;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.EventoArquivo;
import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.ait.IAitService;
import com.tivic.manager.mob.ait.imagemvideo.enums.TipoMidiaEnum;
import com.tivic.manager.mob.aitevento.IAitEventoService;
import com.tivic.manager.mob.aitimagem.AitImagemBuilder;
import com.tivic.manager.mob.aitimagem.repositories.IAitImagemRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.eventoarquivo.IEventoArquivoService;
import com.tivic.manager.mob.eventoequipamento.EventoEquipamentoRepository;
import com.tivic.manager.mob.eventoequipamento.enums.SituacaoEventoEnum;
import com.tivic.manager.mob.processamento.conversao.builders.AitConversaoBuilder;
import com.tivic.manager.mob.processamento.conversao.builders.AitMovimentoConversaoBuilder;
import com.tivic.manager.mob.processamento.conversao.builders.AitParamsDTOBuilder;
import com.tivic.manager.mob.processamento.conversao.dtos.AitParamsDTO;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.manager.mob.tipoevento.enums.IdTipoEventoEnum;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigitalImagem;
import com.tivic.manager.triagem.repositories.IEstacionamentoDigitalImagemRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.SearchCriterios;

public class ConversorEventoService implements IConversorEventoService {
	
	private ManagerLog managerLog;
	private IEventoArquivoService eventoArquivoService;
	private IArquivoService arquivoService;
	private AitRepository aitRepository;
	private IAitService aitService;
	private IAitEventoService aitEventoService;
	private EventoEquipamentoRepository eventoEquipamentoRepository;
	private IEstacionamentoDigitalImagemRepository estacionamentoDigitalImagemRepository;
	private AitMovimentoRepository aitMovimentoRepository;
	private IAitImagemRepository aitImagemRepository;
	private ConversorBaseAntigaNovaFactory conversorBaseAntigaNovaFactory;
	
	public ConversorEventoService() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.eventoArquivoService = (IEventoArquivoService) BeansFactory.get(IEventoArquivoService.class);
		this.arquivoService = (IArquivoService) BeansFactory.get(IArquivoService.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitService = (IAitService) BeansFactory.get(IAitService.class);
		this.aitEventoService = (IAitEventoService) BeansFactory.get(IAitEventoService.class);
		this.eventoEquipamentoRepository = (EventoEquipamentoRepository) BeansFactory.get(EventoEquipamentoRepository.class);
		estacionamentoDigitalImagemRepository = (IEstacionamentoDigitalImagemRepository) BeansFactory.get(IEstacionamentoDigitalImagemRepository.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.aitImagemRepository = (IAitImagemRepository) BeansFactory.get(IAitImagemRepository.class);
		this.conversorBaseAntigaNovaFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);
	}
	
    @Override
	public void convert(EventoEquipamento evento, Orgao orgao, int cdUsuario, CustomConnection customConnection) throws Exception {
		boolean eventoHasAit = aitService.eventoHasAit(evento.getCdEvento(), customConnection);
		if (eventoHasAit) {
			throw new Exception("Evento já possui um AIT vinculado");
		}
		AitParamsDTOBuilder aitParamsDTOBuilder = new AitParamsDTOBuilder(customConnection)
				.setEvento(evento)
				.setOrgao(orgao)
				.setCdUsuario(cdUsuario)
				.setVeiculo()
				.setTipoEvento()
				.setInfracao()
				.setEquipamento()
				.setTpConvenio();
		synchronized(this) {
			aitParamsDTOBuilder.setTalao();
			aitParamsDTOBuilder.setCdAgente();
			
			managerLog.info("Conversão de eventos", "Convertendo evento em AIT: " + evento.getCdEvento());
			AitParamsDTO aitParamsDTO = aitParamsDTOBuilder.build();
			Ait ait = saveAit(evento, aitParamsDTO, customConnection);
			AitMovimento aitMovimento = saveAitMovimento(ait, customConnection);
			
			saveAitImagens(ait.getCdAit(), evento.getCdEvento(), customConnection);
			saveAitEvento(ait.getCdAit(), evento.getCdEvento(), customConnection);
			confirmarSituacaoEvento(evento, customConnection);
		    updateNrUltimoAitTalao(aitParamsDTO.getTalao(), ait, customConnection);
			updateCdMovimentoAit(ait, aitMovimento.getCdMovimento(), customConnection);
			
			customConnection.getConnection().commit();
			managerLog.info("Conversão de eventos", "Evento convertido com sucesso: " + evento.getCdEvento());
		}
	}
	
	private Ait saveAit(EventoEquipamento evento, AitParamsDTO aitParamsDTO, CustomConnection customConnection) throws Exception {
		AitDtInfracaoDeserialize ait = buildAit(evento, aitParamsDTO, customConnection);
		
		if(ait.getDtInfracao() != null && ait.getDtInfracao().compareTo(evento.getDtConclusao()) != 0) {
			throw new Exception("A data de conclusão do evento está diferente da data da infração do AIT!");
		}
		
		if(aitParamsDTO.getTipoEvento().getIdTipoEvento().equals(IdTipoEventoEnum.VAL.getKey())) {
			handleEventoVAL(ait, evento);
		}

        this.aitRepository = conversorBaseAntigaNovaFactory.getAitRepository();
        this.aitRepository.insert(ait, customConnection);
		return ait;
	}
	
	private AitMovimento saveAitMovimento(Ait ait, CustomConnection customConnection) throws Exception {
		AitMovimento aitMovimento = new AitMovimentoConversaoBuilder()
				.setCdAit(ait.getCdAit())
				.setCdUsuario(ait.getCdUsuario())
				.build();
	             
	    this.aitMovimentoRepository = conversorBaseAntigaNovaFactory.getAitMovimentoRepository();
	    this.aitMovimentoRepository.insert(aitMovimento, customConnection);
		return aitMovimento;
	}
	
	private void saveAitImagens(int cdAit, int cdEvento, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_evento", cdEvento);
		List<EventoArquivo> eventoArquivos = eventoArquivoService.find(searchCriterios, customConnection);
		
		for(EventoArquivo eventoArquivo : eventoArquivos) {
			Arquivo arquivo = arquivoService.get(eventoArquivo.getCdArquivo());
			
			if (arquivo.getBlbArquivo() != null) {
				eventoArquivo.setLgImpressao(processarLgImpressao(arquivo.getBlbArquivo(), eventoArquivo.getLgImpressao()));
			}
			
			AitImagem aitImagem = new AitImagemBuilder()
				.setCdAit(cdAit)
				.setBlbImagem(arquivo.getBlbArquivo())
				.setLgImpressao(eventoArquivo.getLgImpressao())
				.build();
	                 
	        this.aitImagemRepository = conversorBaseAntigaNovaFactory.getAitImagemRepository();
	        this.aitImagemRepository.insert(aitImagem, customConnection);
		}
	}

	private void saveAitEvento(int cdAit, int cdEvento, CustomConnection customConnection) throws Exception {
		AitEvento aitEvento = new AitEvento(cdAit, cdEvento);
		aitEventoService.insert(aitEvento, customConnection);
	}
	
	private void confirmarSituacaoEvento(EventoEquipamento evento, CustomConnection customConnection) throws Exception {
		evento.setStEvento(SituacaoEventoEnum.ST_EVENTO_CONFIRMADO.getKey());
		eventoEquipamentoRepository.update(evento, customConnection);
	}
	
	private void updateNrUltimoAitTalao(Talonario talao, Ait ait, CustomConnection customConnection) throws Exception {
		talao.setNrUltimoAit(ait.getNrAit());
		conversorBaseAntigaNovaFactory.getTalonarioRepository().update(talao, customConnection);
	}
	
	private void updateCdMovimentoAit(Ait ait, int cdMovimento, CustomConnection customConnection) throws Exception {
		ait.setCdMovimentoAtual(cdMovimento);
		this.aitRepository = conversorBaseAntigaNovaFactory.getAitRepository();
        this.aitRepository.update(ait, customConnection);
	}
	
	private AitDtInfracaoDeserialize buildAit(EventoEquipamento evento, AitParamsDTO aitParams, CustomConnection customConnection) throws Exception {
		try {

			AitParamsDTO aitParamsDTO = processarCoordenadas(evento, aitParams, customConnection);
			
			return new AitConversaoBuilder()
					.nrPlaca(evento.getNrPlaca())
					.cdEquipamento(evento.getCdEquipamento())
					.dtInfracao(evento.getDtConclusao())
					.dsLocalInfracao(evento.getDsLocal())
					.cdAgente(aitParamsDTO.getCdAgente())
					.cdUsuario(aitParamsDTO.getCdUsuario())
					.vlLatitude(aitParamsDTO.getEquipamento().getVlLatitude())
					.vlLongitude(aitParamsDTO.getEquipamento().getVlLongitude())
					.cdInfracao(aitParamsDTO.getInfracao().getCdInfracao())
					.vlMulta(aitParamsDTO.getInfracao().getVlInfracao())
					.cdCidade(aitParamsDTO.getOrgao().getCdCidade())
					.dtAfericao(aitParamsDTO.getEquipamento().getDtAfericao())
					.nrLacre(aitParamsDTO.getEquipamento().getNrLacre())
					.nrInventarioInmetro(aitParamsDTO.getEquipamento().getNrInventarioInmetro())
					.cdMarcaAutuacao(aitParamsDTO.getVeiculo().getMarcaModelo().getCdMarca())
					.cdMarcaVeiculo(aitParamsDTO.getVeiculo().getMarcaModelo().getCdMarca())
					.cdEspecieVeiculo(aitParamsDTO.getVeiculo().getEspecieVeiculo().getCdEspecie())
					.cdCorVeiculo(aitParamsDTO.getVeiculo().getCor().getCdCor())
					.cdEventoEquipamento(evento.getCdEvento())
					.nrAit(aitParamsDTO.getTalao())
					.idAit(aitParamsDTO.getTalao())
					.cdTalao(aitParamsDTO.getTalao().getCdTalao())
					.tpConvenio(aitParams.getTpConvenio())
					.build();
		} catch (NullPointerException e) {
			managerLog.showLog(e);
			throw new Exception("Um ou mais campos do AIT estão inválidos.");
		} catch (Exception e) {
			managerLog.showLog(e);
			throw new Exception("Erro ao converter o evento em AIT.");
		}
	}
	
	private AitParamsDTO processarCoordenadas(EventoEquipamento evento, AitParamsDTO aitParamsDTO, CustomConnection customConnection) throws Exception {
		EventoEstacionamentoDigitalImagem eventoEstacionamentoDigitalImagem = this.estacionamentoDigitalImagemRepository.get(evento.getCdEvento(), customConnection);
		if (eventoEstacionamentoDigitalImagem != null && isEquipamentoIndisponivel(aitParamsDTO)) {
		    Double vlLatitude = eventoEstacionamentoDigitalImagem.getVlLatitude() != null 
		        ? eventoEstacionamentoDigitalImagem.getVlLatitude().doubleValue() 
		        : 0;

		    Double vlLongitude = eventoEstacionamentoDigitalImagem.getVlLongitude() != null 
		        ? eventoEstacionamentoDigitalImagem.getVlLongitude().doubleValue() 
		        : 0;

		    aitParamsDTO.getEquipamento().setVlLatitude(vlLatitude);
		    aitParamsDTO.getEquipamento().setVlLongitude(vlLongitude);
		}
		return aitParamsDTO;
	}
	
	private boolean isEquipamentoIndisponivel(AitParamsDTO aitParamsDTO) {
		Double vlLatitude = aitParamsDTO.getEquipamento().getVlLatitude();
	    Double vlLongitude = aitParamsDTO.getEquipamento().getVlLongitude();

	    return (vlLatitude == null || vlLatitude == 0) || (vlLongitude == null || vlLongitude == 0);
	}
	
	private void handleEventoVAL(Ait ait, EventoEquipamento evento) {
		ait.setVlVelocidadeAferida(Double.parseDouble(String.valueOf(evento.getVlMedida())));
		ait.setVlVelocidadePenalidade(Double.parseDouble(String.valueOf(evento.getVlConsiderada())));
		ait.setVlVelocidadePermitida(Double.parseDouble(String.valueOf(evento.getVlLimite())));
	}
	
	private int processarLgImpressao(byte[] blbArquivo, int lgImpressao) {
		return checarTipoArquivo(blbArquivo).startsWith("video/") ? 0 : lgImpressao;
	}
	
	private String checarTipoArquivo(byte[] blbArquivo) {
		return TipoMidiaEnum.detectFileType(blbArquivo);
	}
}
