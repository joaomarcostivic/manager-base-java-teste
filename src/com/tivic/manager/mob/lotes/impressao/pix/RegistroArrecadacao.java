package com.tivic.manager.mob.lotes.impressao.pix;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;

public class RegistroArrecadacao {

    private DadosArrecadacaoEnvio dadosArrecadacaoEnvio;
    private ManagerLog managerLog;
    private String token;

    public RegistroArrecadacao(DadosArrecadacaoEnvio dadosArrecadacaoEnvio, String token) throws Exception {
        this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
        this.dadosArrecadacaoEnvio = dadosArrecadacaoEnvio;
        this.token = token;
    }

    public DadosArrecadacaoRetorno executarRegistro() {
        try {
            String jsonRetorno = enviarDadosRegistro();
            managerLog.info("RETORNO REGISTRO PIX:", jsonRetorno);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(jsonRetorno, DadosArrecadacaoRetorno.class);
        } catch (Exception e) {
        	return null;
        }
    }

    private String enviarDadosRegistro() {
    	HttpURLConnection connection = null;
    	try {
    		connection = criarConexao(dadosArrecadacaoEnvio.getUrl());
    		enviarJson(connection);
    		String retorno = lerResposta(connection);
    		
    		managerLog.info("REGISTRO PIX FINALIZADO: ", new GregorianCalendar().getTime().toString());
    		return retorno;
    	} catch (Exception e) {
    		managerLog.info("Falha ao registrar PIX na API do Banco: ", e.getMessage());
    		return null;
    	} finally {
    		if (connection != null) {
    			connection.disconnect();
    		}
    	}
    }
    
    private HttpURLConnection criarConexao(String urlRequisicao) throws Exception {
        URL url = new URL(urlRequisicao);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setUseCaches(false);
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(15000);
        return connection;
    }

    private String gerarIdentificador() {
        if (dadosArrecadacaoEnvio.getCpfDevedor() != null) {
            return "CPF do Proprietário: " + dadosArrecadacaoEnvio.getCpfDevedor();
        } else if (dadosArrecadacaoEnvio.getCnpjDevedor() != null) {
            return "CNPJ do Proprietário: " + dadosArrecadacaoEnvio.getCnpjDevedor();
        }
        return "Identificador não informado";
    }

    private void enviarJson(HttpURLConnection connection) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String identificador = gerarIdentificador();
        managerLog.info("REGISTRO PIX INICIADO - ", identificador);

        String json = objectMapper.writeValueAsString(dadosArrecadacaoEnvio.getItens());
        managerLog.info("DADOS DE ENVIO: ", json);

        try (DataOutputStream saidaDados = new DataOutputStream(connection.getOutputStream())) {
            saidaDados.write(json.getBytes(StandardCharsets.UTF_8));
        }
    }

    private String lerResposta(HttpURLConnection connection) throws Exception {
        int responseCode = connection.getResponseCode();
        managerLog.info("CÓDIGO DE RESPOSTA HTTP: ", String.valueOf(responseCode));

        InputStream inputStream = (responseCode >= 200 && responseCode < 300)
                ? connection.getInputStream()
                : connection.getErrorStream();

        StringBuilder jsonRetorno = new StringBuilder();
        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String linhaEntrada;
            while ((linhaEntrada = leitor.readLine()) != null) {
                jsonRetorno.append(linhaEntrada);
            }
        }
        return jsonRetorno.toString();
    }

}
