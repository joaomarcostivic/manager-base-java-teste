package com.tivic.manager.mob.lotes.impressao.pix.facades;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.mob.lotes.impressao.pix.DadosAutenticacaoEnvio;
import com.tivic.sol.log.ManagerLog;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.GregorianCalendar;

public class AutenticacaoPixFacade {

    private final ManagerLog managerLog;
    private final DadosAutenticacaoEnvio dadosAutenticacaoEnvio;

    public AutenticacaoPixFacade(ManagerLog managerLog, DadosAutenticacaoEnvio dadosAutenticacaoEnvio) {
        this.managerLog = managerLog;
        this.dadosAutenticacaoEnvio = dadosAutenticacaoEnvio;
    }

    public String enviarDadosRegistro() throws Exception {
        String urlRequisicao = "http://host.tivic.com.br:9547/auth/authenticate";
        URL url = new URL(urlRequisicao);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("charset", "utf-8");
        connection.setUseCaches(false);
        try (DataOutputStream saidaDados = new DataOutputStream(connection.getOutputStream())) {
            ObjectMapper objectMapper = new ObjectMapper();
            managerLog.info("AUTENTICACAO PIX INICIADA: ", new GregorianCalendar().getTime().toString());
            String json = objectMapper.writeValueAsString(dadosAutenticacaoEnvio.getItens());
            saidaDados.write(json.getBytes(StandardCharsets.UTF_8));
        }

        StringBuilder jsonRetorno = new StringBuilder();
        int status = connection.getResponseCode();
        managerLog.info("STATUS HTTP DA AUTENTICAÇÃO PIX: ", String.valueOf(status));

        InputStreamReader streamReader;
        if (status >= 200 && status < 300) {
            streamReader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
        } else {
            streamReader = new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8);
        }

        try (BufferedReader leitor = new BufferedReader(streamReader)) {
            String linhaEntrada;
            while ((linhaEntrada = leitor.readLine()) != null) {
                jsonRetorno.append(linhaEntrada);
            }
        }

        managerLog.info("AUTENTICAÇÃO PIX FINALIZADA: ", new GregorianCalendar().getTime().toString());
        return jsonRetorno.toString();
    }

}

