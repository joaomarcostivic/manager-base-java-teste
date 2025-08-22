package com.tivic.manager.mob.lote.impressao;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.TipoStatusEnum;

public class NomeArquivoImpressao {
	private int tpRemessa =  Integer.parseInt(ParametroServices.getValorOfParametro("MOB_TIPO_ENVIO_CORREIOS_NIP"));
	private int tipoAr = Integer.parseInt(ParametroServices.getValorOfParametro("mob_impressao_tp_modelo_nip"));
	private final Map<Integer, Map<Integer, String>> mapaImpressao = new HashMap<>();
	
	    public NomeArquivoImpressao() {

	        Map<Integer, String> mapaCartaSimples = new HashMap<Integer, String>();
	        mapaCartaSimples.put(TipoStatusEnum.NIP_ENVIADA.getKey(), "mob/nip_v2");
	        mapaCartaSimples.put(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey(), "mob/notificacao_advertencia");
	        mapaCartaSimples.put(TipoStatusEnum.NIC_ENVIADA.getKey(), "mob/nic_nip_v2");
	        mapaImpressao.put(TipoRemessaCorreiosEnum.CARTA_SIMPLES.getKey(), mapaCartaSimples);

	        Map<Integer, String> mapaCartaRegistrada = new HashMap<Integer, String>();
	        mapaCartaRegistrada.put(TipoStatusEnum.NIP_ENVIADA.getKey(),
	                tipoAr == TipoArDigitalEnum.AR_DIGITAL.getKey() 
	                		? "mob/nip_carta_registrada_ar_digital"
	                        : "mob/nip_carta_registrada_ar_digital_2D");
	        mapaCartaRegistrada.put(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey(),
	                tipoAr == TipoArDigitalEnum.AR_DIGITAL_2D.getKey() 
	                		? "mob/nip_advertencia_carta_registrada_ar_digital_2D"
	                		: "mob/nip_advertencia_carta_registrada_ar_digital");
	        mapaCartaRegistrada.put(TipoStatusEnum.NIC_ENVIADA.getKey(),
	        		tipoAr == TipoArDigitalEnum.AR_DIGITAL.getKey() 
            			    ? "mob/nic_nip_carta_registrada_ar_digital"
            				: "mob/nic_nip_carta_registrada_ar_digital_2D");
	        mapaImpressao.put(TipoRemessaCorreiosEnum.CARTA_REGISTRADA.getKey(), mapaCartaRegistrada);

	        Map<Integer, String> mapaRemessaEconomica = new HashMap<Integer, String>();
	        mapaRemessaEconomica.put(TipoStatusEnum.NIP_ENVIADA.getKey(),
	                tipoAr == TipoArDigitalEnum.AR_DIGITAL.getKey() 
	                		? "mob/nip_remessa_economica_ar_digital"
	                        : "mob/nip_remessa_economica_ar_digital_2D");
	        mapaRemessaEconomica.put(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey(),
	                tipoAr == TipoArDigitalEnum.AR_DIGITAL_2D.getKey() 
	                		? "mob/nip_advertencia_remessa_economica_ar_digital_2D"
	                        : "mob/nip_advertencia_remessa_economica_ar_digital");
	        mapaRemessaEconomica.put(TipoStatusEnum.NIC_ENVIADA.getKey(),
	        		tipoAr == TipoArDigitalEnum.AR_DIGITAL.getKey() 
            				? "mob/nic_nip_remessa_economica_ar_digital"
            				: "mob/nic_nip_remessa_economica_ar_digital_2D");
	        mapaImpressao.put(TipoRemessaCorreiosEnum.REMESSA_ECONOMICA.getKey(), mapaRemessaEconomica);
	    }

	    public String buscar(int tpStatus) {
	        Map<Integer, String> mapaSelecionado = mapaImpressao.get(tpRemessa);
	        return  mapaSelecionado.get(tpStatus);
	    }
}
