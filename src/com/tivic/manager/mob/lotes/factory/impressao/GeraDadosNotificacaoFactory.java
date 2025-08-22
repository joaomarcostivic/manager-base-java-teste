package com.tivic.manager.mob.lotes.factory.impressao;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;
import com.tivic.manager.mob.lotes.impressao.ait.DadosNotificacaoAitBase;
import com.tivic.manager.mob.lotes.impressao.ait.GeraDadosNotificacaoAit;
import com.tivic.manager.mob.lotes.impressao.ait.GeraDadosNotificacaoAitNic;

public class GeraDadosNotificacaoFactory {

    private final AitRepository aitRepository;

    public GeraDadosNotificacaoFactory(AitRepository aitRepository) {
        this.aitRepository = aitRepository;
    }
    
    public Notificacao getNotificacao(int cdAit, int cdLoteImpressao) throws Exception {
        DadosNotificacaoAitBase dadosNotificacaoAitBase = getDadosNotificacao(cdAit);        
        Notificacao notificacao = dadosNotificacaoAitBase.getNotificacaoByCdAit(cdAit);
        notificacao.setCdLoteImpressao(cdLoteImpressao);
        notificacao.setNomeEquipamento(notificacao.getCdEquipamento() > 0
            ? EquipamentoEnum.valueOf(notificacao.getTpEquipamento())
            : EquipamentoEnum.NENHUM.getValue());

        return notificacao;
    }
    
    public DadosNotificacaoAitBase getDadosNotificacao(int cdAit) throws Exception {
        Ait ait = aitRepository.get(cdAit);
        if (ait != null && ait.getCdAitOrigem() > 0) {
            return new GeraDadosNotificacaoAitNic();
        } else {
            return new GeraDadosNotificacaoAit();
        }
    }

}

