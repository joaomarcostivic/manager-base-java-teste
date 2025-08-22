package com.tivic.manager.mob.lotes.impressao.strategy.lote;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.lotes.builders.LoteBuilder;
import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoAitBuilder;
import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoBuilder;
import com.tivic.manager.mob.lotes.enums.impressao.StatusLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressaoAit;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.util.date.DateUtil;

public class LoteImpressaoGenerator {

    private ILoteImpressaoService loteImpressaoService;
	private LoteRepository loteRepository;

    public LoteImpressaoGenerator() throws Exception {
    	this.loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
		this.loteRepository = (LoteRepository) BeansFactory.get(LoteRepository.class);
    }

    public LoteImpressao gerarLoteImpressao(List<Ait> aitList, int cdUsuario, int tipoLote, CustomConnection customConnection) throws Exception {
    	Lote lote = criarLote(cdUsuario, tipoLote, customConnection);
        LoteImpressao loteImpressao = new LoteImpressaoBuilder()
        		.setCdLote(lote.getCdLote())
        		.setStLote(StatusLoteImpressaoEnum.AGUARDANDO_GERACAO.getKey())
        		.setTpImpressao(tipoLote)
                .build();
        List<LoteImpressaoAit> listLoteImpressaoAit = gerarLoteImpressaoAits(aitList);
        loteImpressao.setAits(listLoteImpressaoAit);
        loteImpressao.setIdLote(lote.getIdLote());
        return this.loteImpressaoService.save(loteImpressao, customConnection);
    }
    
    private Lote criarLote(int cdUsuario, int tipLote, CustomConnection customConnection) throws Exception {
	    Lote lote = new LoteBuilder()
	        .setDtCriacao(DateUtil.getDataAtual())
	        .setCdCriador(cdUsuario)
	        .setIdLote(this.loteImpressaoService.getIdLote(tipLote))
	        .build();
	    loteRepository.insert(lote, customConnection);
	    return lote;
	}

    private List<LoteImpressaoAit> gerarLoteImpressaoAits(List<Ait> aitList) {
        List<LoteImpressaoAit> listLoteImpressaoAit = new ArrayList<>();

        for (Ait ait : aitList) {
            LoteImpressaoAit loteImpressaoAit = new LoteImpressaoAitBuilder()
    				.setCdAit(ait.getCdAit())
    				.setStImpressao(StatusLoteImpressaoEnum.AGUARDANDO_GERACAO.getKey())
    				.build();
            listLoteImpressaoAit.add(loteImpressaoAit);
        }
        return listLoteImpressaoAit;
    }
    
}
