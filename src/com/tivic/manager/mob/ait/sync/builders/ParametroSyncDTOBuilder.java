package com.tivic.manager.mob.ait.sync.builders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.tivic.manager.grl.Parametro;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.grl.parametrovalor.ParametroValorRepository;
import com.tivic.manager.mob.ait.sync.entities.ParametroSyncDTO;
import com.tivic.sol.cdi.BeansFactory;

public class ParametroSyncDTOBuilder extends ParametroSyncDTOBuilderBase {
	
	private IParametroRepository parametroRepository;
    private ParametroValorRepository parametroValorRepository;


    private String[] nmParametros = {
    	    "NM_TITULO_IMPRESSAO_1",
    	    "NM_TITULO_IMPRESSAO_2",
    	    "NM_TITULO_IMPRESSAO_3",
    	    "CD_ORGAO_AUTUADOR",
    	    "NM_BAIRRO",
    	    "NM_LOGRADOURO",
    	    "NR_ENDERECO",
    	    "SG_DEPARTAMENTO",
    	    "NR_TELEFONE",
    	    "NM_EMAIL",
    	    "MOB_IMPRESSOS_NM_ORGAO_AUTUADOR",
    	    "MOB_IMPRESSOS_NM_MUNICIPIO_AUTUADOR",
    	    "LG_CADASTRO_HORA_INFRACAO_ALTERAVEL",
    	    "MOB_IMPRESSAO_RODAPE_APP_AIT",
    	    "LG_CONVENIO",
    	    "URL_DETRAN",
    	    "NM_USUARIO_CONSULTA_DETRAN",
    	    "NM_SENHA_CONSULTA_DETRAN",
    	    "MOB_OSM_VIEWBOX",
    	    "LG_WIFI_SYNC",
    	    "API_URL_NOMINATIM",
    	    "MOB_OSM_ESTADO",
    	    "MOB_OSM_CIDADE",
            "URL_DADOS_SYNC"
    	};
    
    private static final Set<String> EMPTY_STRING_PARAMS = new HashSet<>(Arrays.asList(
    		"API_URL_NOMINATIM",
            "MOB_OSM_VIEWBOX",
            "MOB_OSM_ESTADO",
            "MOB_OSM_CIDADE"
        ));

    public ParametroSyncDTOBuilder() throws Exception {
    	super();
        parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
        parametroValorRepository = (ParametroValorRepository) BeansFactory.get(ParametroValorRepository.class);
    }

    @Override
    protected void setParametroSync() throws Exception {
        for (String nmParametro : nmParametros) {
            try {
                ParametroSyncDTO parametroSyncDTO = new ParametroSyncDTO();
                
                Parametro parametro = parametroRepository.getParametroByName(nmParametro);
                
                parametroSyncDTO.setCdParametro(null);
                parametroSyncDTO.setNmParametro(nmParametro);
                parametroSyncDTO.setTpDado(parametro.getTpDado());
                parametroSyncDTO.setVlParametro(getParametroValue(parametro));

                parametroSyncList.add(parametroSyncDTO);
            } catch (Exception e) {
                throw new Exception("É necessário configurar o parâmetro: " + nmParametro);
            }
        }
    }
    
    private String getParametroValue(Parametro parametro) throws Exception {
        if (EMPTY_STRING_PARAMS.contains(parametro.getNmParametro())) {
            String valor = ParametroServices.getValorOfParametroAsString(parametro.getNmParametro(), null);
            return "0".equals(valor) ? null : valor;
        }
        return parametroValorRepository.get(parametro.getCdParametro()).getVlInicial();
    }
}
