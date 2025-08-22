package com.tivic.manager.ptc.protocolos.julgamento;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaBuilder;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaRepository;
import com.tivic.manager.mob.lotes.builders.LoteBuilder;
import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoAitBuilder;
import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoBuilder;
import com.tivic.manager.mob.lotes.dto.CreateAitLoteImpressaoDTO;
import com.tivic.manager.mob.lotes.dto.impressao.CreateLoteImpressaoDTO;
import com.tivic.manager.mob.lotes.enums.impressao.StatusLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressaoAit;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.mob.lotes.repository.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lotes.repository.impressao.LoteImpressaoRepository;
import com.tivic.manager.mob.lotes.service.impressao.exceptions.LoteNotificacaoException;
import com.tivic.manager.mob.lotes.validator.LoteNotificacaoImpressaoValidations;
import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.util.date.DateUtil;

public class GerarLoteJulgamento implements IGerarLoteJulgamento {

	private ICartaJulgamentoService cartaJulgamentoService;
	private LoteRepository loteRepository;
	private LoteImpressaoRepository loteImpressaoRepository;
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private AitInconsistenciaRepository aitInconsistenciaRepository;
	private AitRepository aitRepository;
	private ManagerLog managerLog;
	
	public GerarLoteJulgamento() throws Exception {
		cartaJulgamentoService = (ICartaJulgamentoService) BeansFactory.get(ICartaJulgamentoService.class);
		this.loteRepository = (LoteRepository) BeansFactory.get(LoteRepository.class);
		this.loteImpressaoRepository = (LoteImpressaoRepository) BeansFactory.get(LoteImpressaoRepository.class);
		this.loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
		this.aitInconsistenciaRepository = (AitInconsistenciaRepository) BeansFactory.get(AitInconsistenciaRepository.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	public LoteImpressao gerarLoteJulgamento(CreateLoteImpressaoDTO createLoteImpressao, int cdUsuario, CustomConnection customConnection) throws Exception {	    
		Lote lote = criarLote(createLoteImpressao, customConnection);
		LoteImpressao loteImpressao = criarLoteImpressao(createLoteImpressao, lote, customConnection);
		loteImpressao.setIdLote(lote.getIdLote());
	    List<Ait> aitList = processarAitInconsistente(createLoteImpressao.getAits(), createLoteImpressao.getTpImpressao(), customConnection);    
		for (Ait aitLote : aitList) {
			criarLoteImpressaoAit(loteImpressao, aitLote.getCdAit(), customConnection);
		}
		
		List<LoteImpressaoAit> loteImpressaoAits = aitList.stream()
			    .map(ait -> new LoteImpressaoAitBuilder()
			        .setCdAit(ait.getCdAit())
			        .setCdLoteImpressao(loteImpressao.getCdLoteImpressao())
			        .setStImpressao(StatusLoteImpressaoEnum.AGUARDANDO_GERACAO.getKey())
			        .build())
			    .collect(Collectors.toList());

		loteImpressao.setAits(loteImpressaoAits);
		return cartaJulgamentoService.save(loteImpressao, customConnection);
	}

	private Lote criarLote(CreateLoteImpressaoDTO createLoteImpressao, CustomConnection customConnection) throws Exception {
		Lote lote = new LoteBuilder()
				.setDtCriacao(DateUtil.getDataAtual())
				.setCdCriador(createLoteImpressao.getCdCriador())
				.setIdLote(Util.generateRandomString(5))
				.build();
		loteRepository.insert(lote, customConnection);
		return lote;
	}

	private LoteImpressao criarLoteImpressao(CreateLoteImpressaoDTO createLoteImpressao, Lote lote, CustomConnection customConnection) throws Exception {
		LoteImpressao loteImpressao = new LoteImpressaoBuilder()
				.setCdLote(lote.getCdLote())
				.setStLote(StatusLoteImpressaoEnum.AGUARDANDO_IMPRESSAO.getKey())
				.setTpImpressao(createLoteImpressao.getTpImpressao())
				.build();
		loteImpressaoRepository.insert(loteImpressao, customConnection);
		return loteImpressao;
	}

	private void criarLoteImpressaoAit(LoteImpressao loteImpressao, int cdAit, CustomConnection customConnection) throws Exception {
		LoteImpressaoAit loteImpressaoAit = new LoteImpressaoAitBuilder()
				.setCdLoteImpressao(loteImpressao.getCdLoteImpressao())
				.setCdAit(cdAit)
				.setStImpressao(StatusLoteImpressaoEnum.AGUARDANDO_GERACAO.getKey())
				.build();
		loteImpressaoAitRepository.insert(loteImpressaoAit, customConnection);
	}
	
	private List<Ait> processarAitInconsistente(List<CreateAitLoteImpressaoDTO> aits, int tpImpressao, CustomConnection customConnection) throws Exception {
	    List<Ait> aitList = new ArrayList<Ait>();
		
		for (CreateAitLoteImpressaoDTO aitDto: aits) {
			Ait aitLote = aitRepository.get(aitDto.getCdAit());
			if (validarERegistrarInconsistencia(aitLote, tpImpressao, customConnection)) {
				aitList.add(aitLote);
			}			
		}
	    return aitList;
	}	
	
	private boolean validarERegistrarInconsistencia(Ait ait, int tpLoteImpressao, CustomConnection customConnection) throws Exception {
	    try {
	        new LoteNotificacaoImpressaoValidations().validate(ait, customConnection);
	        return true;
	    } catch (LoteNotificacaoException e) {
	        this.managerLog.info("Erro de validação inconsistencia para o AIT: " + ait.getCdAit() + " - ", e.getMessage());
	        AitInconsistencia aitInconsistencia = new AitInconsistenciaBuilder()
	            .build(ait, e.getCodErro(), tpLoteImpressao);
	        this.aitInconsistenciaRepository.insert(aitInconsistencia, customConnection);
	        return false;
	    }
	}
}
