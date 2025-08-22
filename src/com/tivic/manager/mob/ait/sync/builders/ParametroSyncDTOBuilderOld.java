package com.tivic.manager.mob.ait.sync.builders;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.adapter.base.antiga.parametro.ParametroOldRepositoryDAO;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.ait.sync.entities.ParametroSyncDTO;
import com.tivic.sol.cdi.BeansFactory;

public class ParametroSyncDTOBuilderOld extends ParametroSyncDTOBuilderBase {

    private final IParametroRepository parametroRepository;

    private static final Map<String, Integer> PARAMETROS = new HashMap<String, Integer>() {{
        put("NM_BAIRRO", 1);
        put("NM_EMAIL", 1);
        put("NM_LOGRADOURO", 1);
        put("NR_ENDERECO", 0);
        put("NR_TELEFONE", 1);
        put("SG_DEPARTAMENTO", 1);
        put("MOB_IMPRESSOS_NM_MUNICIPIO_AUTUADOR", 1);
        put("MOB_IMPRESSOS_NM_ORGAO_AUTUADOR", 1);
        put("CD_ORGAO_AUTUADOR", 0);
        put("NM_TITULO_IMPRESSAO_1", 1);
        put("NM_TITULO_IMPRESSAO_2", 1);
        put("NM_TITULO_IMPRESSAO_3", 1);
        put("LG_CONVENIO", 3);
        put("MOB_IMPRESSAO_RODAPE_APP_AIT", 1);
        put("NM_SENHA_CONSULTA_DETRAN", 1);
        put("NM_USUARIO_CONSULTA_DETRAN", 1);
        put("URL_DETRAN", 1);
        put("LG_CADASTRO_HORA_INFRACAO_ALTERAVEL", 0);
        put("MOB_OSM_VIEWBOX", 1);
        put("LG_WIFI_SYNC", 3);
        put("API_URL_NOMINATIM", 1);
        put("MOB_OSM_ESTADO", 1);
        put("MOB_OSM_CIDADE", 1);
        put("URL_DADOS_SYNC", 1);
    }};

    public ParametroSyncDTOBuilderOld() throws Exception {
    	super();
        parametroRepository = new ParametroOldRepositoryDAO();
    }

    @Override
    protected void setParametroSync() throws Exception {
        for (Map.Entry<String, Integer> entry : PARAMETROS.entrySet()) {
            String nmParametro = entry.getKey();
            Integer tpDado = entry.getValue();

            try {
                ParametroSyncDTO parametroSyncDTO = new ParametroSyncDTO();

                String parametroValor = parametroRepository.getValorOfParametroAsString(nmParametro);

                parametroSyncDTO.setCdParametro(null);
                parametroSyncDTO.setNmParametro(nmParametro);
                parametroSyncDTO.setTpDado(tpDado); 
                parametroSyncDTO.setVlParametro(parametroValor);

                parametroSyncList.add(parametroSyncDTO);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                throw new Exception("Erro ao configurar o parâmetro: " + nmParametro, e);
            }
        }
    }
}