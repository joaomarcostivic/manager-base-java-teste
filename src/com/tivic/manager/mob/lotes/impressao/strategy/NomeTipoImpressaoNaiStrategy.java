package com.tivic.manager.mob.lotes.impressao.strategy;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.lotes.enums.correios.TipoRemessaCorreiosEnum;

public class NomeTipoImpressaoNaiStrategy {
	private final Map<Integer, Map<Integer, String>> mapaImpressao = new HashMap<>();
	
	public NomeTipoImpressaoNaiStrategy() {
        mapaImpressao.put(TipoRemessaCorreiosEnum.CARTA_SIMPLES.getKey(), criarMapaCartaSimples());
        mapaImpressao.put(TipoRemessaCorreiosEnum.CARTA_REGISTRADA.getKey(), criarMapaAr());
        mapaImpressao.put(TipoRemessaCorreiosEnum.REMESSA_ECONOMICA.getKey(), criarMapaAr());
    }
    
    private Map<Integer, String> criarMapaCartaSimples() {
    	Map<Integer, String> mapa = new HashMap<Integer, String>();
    	mapa.put(TipoStatusEnum.NAI_ENVIADO.getKey(), "mob/na_carta_simples");
    	mapa.put(TipoStatusEnum.NIC_ENVIADA.getKey(), "mob/nic_na_np_generico");
    	return mapa;
    }
    	
    private Map<Integer, String> criarMapaAr(){
    	 Map<Integer, String> mapa = new HashMap<>();
    	 mapa.put(TipoStatusEnum.NAI_ENVIADO.getKey(), "mob/na_com_ar");
    	 mapa.put(TipoStatusEnum.NIC_ENVIADA.getKey(), "mob/nic_na_np_generico");
    	 return mapa;
    };

    public String buscar(String idAitGeradora, TipoRemessaCorreiosEnum tpRemessa) {
        Map<Integer, String> mapaSelecionado = mapaImpressao.get(tpRemessa.getKey());
        int tpStatus = idAitGeradora != null ? TipoStatusEnum.NIC_ENVIADA.getKey() : TipoStatusEnum.NAI_ENVIADO.getKey();
        return  mapaSelecionado.get(tpStatus);
    }
}
