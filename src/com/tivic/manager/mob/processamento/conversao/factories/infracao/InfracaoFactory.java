package com.tivic.manager.mob.processamento.conversao.factories.infracao;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.TipoEvento;
import com.tivic.manager.mob.infracao.IInfracaoService;
import com.tivic.manager.mob.tipoevento.enums.IdTipoEventoEnum;
import com.tivic.sol.cdi.BeansFactory;

public class InfracaoFactory {
    private IInfracaoService infracaoService;
    private Map<String, InfracaoStrategy> strategyMap;

    public InfracaoFactory(EventoEquipamento evento) throws Exception {
        this.infracaoService = (IInfracaoService) BeansFactory.get(IInfracaoService.class);
        initializeStrategyMap(evento);
    }

    private void initializeStrategyMap(EventoEquipamento evento) {
        strategyMap = new HashMap<>();
        strategyMap.put(IdTipoEventoEnum.VAL.getKey(), new InfracaoVALStrategy(evento, infracaoService));
        strategyMap.put(IdTipoEventoEnum.NED.getKey(), new InfracaoNEDStrategy(infracaoService));
        strategyMap.put(IdTipoEventoEnum.ASV.getKey(), new InfracaoASVStrategy(infracaoService));
        strategyMap.put(IdTipoEventoEnum.PSF.getKey(), new InfracaoPSFStrategy(infracaoService));
    }

    public InfracaoStrategy getStrategy(TipoEvento tipoEvento) throws Exception {
    	String idTipoEvento = tipoEvento.getIdTipoEvento();
    	
        InfracaoStrategy strategy = strategyMap.get(idTipoEvento);

        if (strategy != null) {
            return strategy;
        }

        throw new IllegalArgumentException("O tipo de evento " + idTipoEvento + " não é suportado.");
    }
}
