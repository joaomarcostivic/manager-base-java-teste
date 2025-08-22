package com.tivic.manager.mob.lotes.impressao.strategy;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.lotes.enums.correios.TipoRemessaCorreiosEnum;

public class NomeTipoImpressaoNipStrategy {
	private final Map<Integer, Map<Integer, String>> mapaImpressao = new HashMap<>();
	
    public NomeTipoImpressaoNipStrategy() {
        mapaImpressao.put(TipoRemessaCorreiosEnum.CARTA_SIMPLES.getKey(), criarMapaCartaSimples());
        mapaImpressao.put(TipoRemessaCorreiosEnum.CARTA_REGISTRADA.getKey(), criarMapaAr());
        mapaImpressao.put(TipoRemessaCorreiosEnum.REMESSA_ECONOMICA.getKey(), criarMapaAr());
    }
    
    private Map<Integer, String> criarMapaCartaSimples() {
    	Map<Integer, String> mapa = new HashMap<>();
        mapa.put(TipoStatusEnum.NIP_ENVIADA.getKey(), "mob/np_carta_simples");
        mapa.put(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey(), "mob/np_carta_simples");
        mapa.put(TipoStatusEnum.NIC_ENVIADA.getKey(), "mob/nic_na_np_generico");
        return mapa;
    };
    	
    private Map<Integer, String> criarMapaAr(){
    	 Map<Integer, String> mapa = new HashMap<>();
    	 mapa.put(TipoStatusEnum.NIP_ENVIADA.getKey(), "mob/np_com_ar");
    	 mapa.put(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey(),"mob/np_com_ar");
    	 mapa.put(TipoStatusEnum.NIC_ENVIADA.getKey(), "mob/nic_na_np_generico");
    	 return mapa;
    };

    public String buscar(int tpStatus, TipoRemessaCorreiosEnum tpRemessa) {
        Map<Integer, String> mapaSelecionado = mapaImpressao.get(tpRemessa.getKey());
        return  mapaSelecionado.get(tpStatus);
    }

}
