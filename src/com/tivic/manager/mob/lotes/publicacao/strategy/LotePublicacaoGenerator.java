package com.tivic.manager.mob.lotes.publicacao.strategy;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.lotes.builders.LoteBuilder;
import com.tivic.manager.mob.lotes.builders.publicacao.LotePublicacaoAitBuilder;
import com.tivic.manager.mob.lotes.builders.publicacao.LotePublicacaoBuilder;
import com.tivic.manager.mob.lotes.dto.publicacao.NotificacaoPublicacaoPendenteDTO;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacao;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacaoAit;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.mob.lotes.service.publicacao.ILotePublicacaoService;
import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.util.date.DateUtil;

public class LotePublicacaoGenerator {
	
	private ILotePublicacaoService lotePublicacaoService;
	private int cdUsuario;
	private int tpPublicacao;
	private List<NotificacaoPublicacaoPendenteDTO> notificacaoPublicacaoPendenteDtos;
	private LoteRepository loteRepository;

	public LotePublicacaoGenerator(int cdUsuario, int tpPublicacao, List<NotificacaoPublicacaoPendenteDTO> notificacaoPublicacaoPendenteDtos) throws Exception {
		lotePublicacaoService = (ILotePublicacaoService) BeansFactory.get(ILotePublicacaoService.class);
		loteRepository = (LoteRepository) BeansFactory.get(LoteRepository.class);
		this.cdUsuario = cdUsuario;
		this.tpPublicacao = tpPublicacao;
		this.notificacaoPublicacaoPendenteDtos = notificacaoPublicacaoPendenteDtos;
	}

    public LotePublicacao gerarLoteImpressao(CustomConnection customConnection) throws Exception {
    	Lote lote = criarLote(customConnection);
        LotePublicacao lotePublicacao = new LotePublicacaoBuilder()
        		.setCdLote(lote.getCdLote())
        		.setTpPublicacao(this.tpPublicacao)
                .build();
        List<LotePublicacaoAit> listLoteImpressaoAit = gerarLotePublicacaoAits(notificacaoPublicacaoPendenteDtos);
        lotePublicacao.setAits(listLoteImpressaoAit);
        return this.lotePublicacaoService.save(lotePublicacao, customConnection);
    }
    
    private Lote criarLote(CustomConnection customConnection) throws Exception {
	    Lote lote = new LoteBuilder()
	        .setDtCriacao(DateUtil.getDataAtual())
	        .setCdCriador(this.cdUsuario)
	        .setIdLote(Util.generateRandomString(5))
	        .build();
	    loteRepository.insert(lote, customConnection);
	    return lote;
	}

    private List<LotePublicacaoAit> gerarLotePublicacaoAits(List<NotificacaoPublicacaoPendenteDTO> aitList) {
        List<LotePublicacaoAit> listLotePublicacaoAit = new ArrayList<>();

        for (NotificacaoPublicacaoPendenteDTO notificacaoPublicacaoPendenteDto : aitList) {
        	LotePublicacaoAit lotePublicacaoAit = new LotePublicacaoAitBuilder()
    				.setCdAit(notificacaoPublicacaoPendenteDto.getCdAit())
    				.build();
        	listLotePublicacaoAit.add(lotePublicacaoAit);
        }
        return listLotePublicacaoAit;
    }

}
