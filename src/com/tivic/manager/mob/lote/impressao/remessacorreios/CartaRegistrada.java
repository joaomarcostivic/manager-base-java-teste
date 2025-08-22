package com.tivic.manager.mob.lote.impressao.remessacorreios;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.remessacorreios.builders.CartaRegistradaNaiBuilder;
import com.tivic.manager.mob.lote.impressao.remessacorreios.builders.CartaRegistradaNicNaiBuilder;
import com.tivic.manager.mob.lote.impressao.remessacorreios.builders.CartaRegistradaNicNipBuilder;
import com.tivic.manager.mob.lote.impressao.remessacorreios.builders.CartaRegistradaNipBuilder;
import com.tivic.manager.mob.lote.impressao.remessacorreios.builders.DirectorRemessa;
import com.tivic.manager.mob.lote.impressao.remessacorreios.builders.IBuilderRemessa;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class CartaRegistrada implements IGerarDadosDocumento {

    private static final Map<Integer, Class<? extends IBuilderRemessa>> map = new HashMap<Integer, Class<? extends IBuilderRemessa>>();

    static {
        map.put(TipoLoteNotificacaoEnum.LOTE_NAI.getKey(), CartaRegistradaNaiBuilder.class);
        map.put(TipoLoteNotificacaoEnum.LOTE_NIP.getKey(), CartaRegistradaNipBuilder.class);
        map.put(TipoLoteNotificacaoEnum.LOTE_NIC_NAI.getKey(), CartaRegistradaNicNaiBuilder.class);
        map.put(TipoLoteNotificacaoEnum.LOTE_NIC_NIP.getKey(), CartaRegistradaNicNipBuilder.class);
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