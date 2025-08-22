package com.tivic.manager.mob.lote.impressao.remessacorreios;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.remessacorreios.builders.DirectorRemessa;
import com.tivic.manager.mob.lote.impressao.remessacorreios.builders.IBuilderRemessa;
import com.tivic.manager.mob.lote.impressao.remessacorreios.builders.RemessaSimplesNAIBuilder;
import com.tivic.manager.mob.lote.impressao.remessacorreios.builders.RemessaSimplesNICNAIBuilder;
import com.tivic.manager.mob.lote.impressao.remessacorreios.builders.RemessaSimplesNICNIPBuilder;
import com.tivic.manager.mob.lote.impressao.remessacorreios.builders.RemessaSimplesNIPBuilder;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class CartaSimples implements IGerarDadosDocumento {

    private static final Map<Integer, Class<? extends IBuilderRemessa>> map = new HashMap<Integer, Class<? extends IBuilderRemessa>>();

    static {
        map.put(TipoLoteNotificacaoEnum.LOTE_NAI.getKey(), RemessaSimplesNAIBuilder.class);
        map.put(TipoLoteNotificacaoEnum.LOTE_NIP.getKey(), RemessaSimplesNIPBuilder.class);
        map.put(TipoLoteNotificacaoEnum.LOTE_NIC_NAI.getKey(), RemessaSimplesNICNAIBuilder.class);
        map.put(TipoLoteNotificacaoEnum.LOTE_NIC_NIP.getKey(), RemessaSimplesNICNIPBuilder.class);
    }

    @Override
    public DadosDocumento gerarDadosDocumento(int tpNotificacao, LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
        DirectorRemessa directorRemessa = new DirectorRemessa();
        IBuilderRemessa remessaSimplesBuilder = getBuilderNotificacao(tpNotificacao);
        directorRemessa.constructorRemessa(remessaSimplesBuilder, loteImpressao, customConnection);
        return remessaSimplesBuilder.build();
    }

    private IBuilderRemessa getBuilderNotificacao(int tpNotificacao) throws Exception {
        Class<? extends IBuilderRemessa> classe = map.get(tpNotificacao);
        if (classe == null) {
            throw new ValidacaoException("Tipo de notificação não identificada.");
        }
        return classe.newInstance();
    }
}