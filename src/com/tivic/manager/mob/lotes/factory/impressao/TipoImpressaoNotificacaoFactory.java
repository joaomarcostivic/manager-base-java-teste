package com.tivic.manager.mob.lotes.factory.impressao;

import java.sql.Types;
import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.lotes.builders.impressao.TipoimpressaoAutuacaoBuilder;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.impressao.strategy.viaunica.TipoImpressaoNotificacao;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class TipoImpressaoNotificacaoFactory {

    public static TipoImpressaoNotificacao criarAdvertenciaNip(int cdAit, int tpNotificacao, int cdUsuario) throws Exception {
        IAitMovimentoService aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);

        AitMovimento aitMovimento = aitMovimentoServices.getMovimentoTpStatus(cdAit, tpNotificacao);
        AitMovimento aitMovimentoAdvertencia = aitMovimentoServices.getMovimentoTpStatus(cdAit, TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey());
        AitMovimento aitMovimentoCancelado = aitMovimentoServices.getMovimentoTpStatus(cdAit, TipoStatusEnum.CANCELAMENTO_NIP.getKey());

        List<LoteImpressao> loteImpressao = buscarLotes(cdAit);

        TipoImpressaoNotificacao tipoImpressaoNotificacao = new TipoimpressaoAutuacaoBuilder()
                .setCdAit(cdAit)
                .setCdUsuario(cdUsuario)
                .setMovimento(aitMovimento)
                .setMovimentoCancelado(aitMovimento, aitMovimentoCancelado)
                .setLote(loteImpressao)
                .setAdvertencia(aitMovimentoAdvertencia)
                .build();

        return tipoImpressaoNotificacao;
    }

    private static List<LoteImpressao> buscarLotes(int cdAit) throws Exception {
        Search<LoteImpressao> search = new SearchBuilder<LoteImpressao>("mob_lote_impressao A")
            .fields("A.cd_lote_impressao, A.tp_impressao, B.cd_ait")
            .addJoinTable(" JOIN mob_lote_impressao_ait B ON (B.cd_lote_impressao = A.cd_lote_impressao) ")
            .searchCriterios(searchCriteriosLote(cdAit))
            .build();

        return search.getList(LoteImpressao.class);
    }

    private static SearchCriterios searchCriteriosLote(int cdAit) {
        SearchCriterios search = new SearchCriterios();
        search.addCriterios(
            "A.tp_impressao",
            String.valueOf(TipoLoteImpressaoEnum.LOTE_NIP.getKey()) + ", " +
            String.valueOf(TipoLoteImpressaoEnum.LOTE_NIC_NIP.getKey()),
            ItemComparator.IN, Types.INTEGER
        );
        search.addCriteriosEqualInteger("B.cd_ait", cdAit, true);
        return search;
    }
}