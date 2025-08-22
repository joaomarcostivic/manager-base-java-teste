package com.tivic.manager.mob.lotes.impressao.pix;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.mob.lotes.impressao.pix.facades.AutenticacaoPixFacade;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;

public class AutenticacaoUsuarioPix {
	
	private DadosAutenticacaoEnvio dadosAutenticacaoEnvio;
    private ManagerLog managerLog;

    public AutenticacaoUsuarioPix(DadosAutenticacaoEnvio dadosAutenticacaoEnvio) throws Exception {
        this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
        this.dadosAutenticacaoEnvio = dadosAutenticacaoEnvio;
    }

    public DadosAutenticacaoRetorno autenticar() {
        try {
            AutenticacaoPixFacade autenticacaoPixFacade = new AutenticacaoPixFacade(managerLog, dadosAutenticacaoEnvio);
            String jsonRetorno = autenticacaoPixFacade.enviarDadosRegistro();
            managerLog.info("RETORNO AUTENTICAÇÃO PIX:", jsonRetorno);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            DadosAutenticacaoRetorno retorno = objectMapper.readValue(jsonRetorno, DadosAutenticacaoRetorno.class);
            return retorno;
        } catch (Exception e) {
            return null;
        }
    }
}
