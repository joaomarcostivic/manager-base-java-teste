package com.tivic.manager.mob.servlets;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tivic.manager.log.BuscaPlaca;
import com.tivic.manager.log.BuscaPlacaServices;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.wsdl.detran.ba.DadosRetornoBA;

import sol.util.Result;


public class DetranConsultaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  { 
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {
		
		BuscaPlaca log = new BuscaPlaca();
		try{
	    	DetranConsultaServletRequest detranConsultaServletRequest = new DetranConsultaServletRequest(request);
			detranConsultaServletRequest.validate();
			validateAuthentication(detranConsultaServletRequest);
			List<DetranConsulta> lista = DetranConsultaFactory.createLocalPriority();
			DadosRetornoBA dadosRetornoBa = EnvioConsulta.consultar(request, lista);
			montarResponse(response, dadosRetornoBa);
	    } 
        catch (Exception e) {
	    	log.setTpLog(BuscaPlacaServices.TP_LOG_ERRO);
	    	log.setTxtResposta(e.getMessage());  
	    	OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
	    	writer.write("{}");
			writer.close();
        }
    }
	
	private void montarResponse(HttpServletResponse response, DadosRetornoBA dadosRetornoBa) throws Exception{
		OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
		System.out.println();
		System.out.println("JSON de retorno = " + dadosRetornoBa.exportJson());
		writer.write(preparaJsonRetorno(dadosRetornoBa.exportJson()));
		writer.close();
	}
	
	private String preparaJsonRetorno(JSONObject jsonInicial) throws JSONException {
		JSONObject jsonFinal = new JSONObject();
		jsonFinal.put("nm_cor", jsonInicial.get("cor"));
		jsonFinal.put("nr_placa", jsonInicial.get("paramPlaca"));
		jsonFinal.put("restricoes", new JSONArray());
		jsonFinal.put("nm_municipio", jsonInicial.get("nomeMunicipio"));
		jsonFinal.put("nm_proprietario", jsonInicial.get("nomeProprietario"));
		jsonFinal.put("tp_combustivel", "");
		jsonFinal.put("cd_retorno", jsonInicial.get("codigoRetExec"));
		jsonFinal.put("nm_marca_modelo", jsonInicial.get("marcaModelo"));
		jsonFinal.put("nm_especie", jsonInicial.get("especie"));
		jsonFinal.put("nr_renavam", jsonInicial.get("paramRenavam"));
		jsonFinal.put("nm_categoria", jsonInicial.get("categoriaVeiculo"));
		jsonFinal.put("tp_carroceria", jsonInicial.get("carroceria"));
		jsonFinal.put("sg_estado", "");
		jsonFinal.put("tp_veiculo", jsonInicial.get("tipoVeiculo"));
		jsonFinal.put("nr_ano_modelo", jsonInicial.get("anoModelo"));
		jsonFinal.put("nr_ano_fabricacao", jsonInicial.get("anoFabricacao"));
		return jsonFinal.toString();
	}
	
	private static void validateAuthentication(DetranConsultaServletRequest detranConsultaServletRequest) throws Exception{
		Result result = UsuarioServices.autenticar(detranConsultaServletRequest.getNmUsuario(), detranConsultaServletRequest.getNmSenha(), detranConsultaServletRequest.getIdModulo());
		if(result.getCode()==UsuarioServices.ERR_DENIED_LOGIN_INVALIDO) {
    		throw new Exception("{\n \"code\": \"-4\",\n \"message\": \"Usuario invalido.\"\n}");	
    	}
		else if(result.getCode()==UsuarioServices.ERR_DENIED_SENHA_INVALIDA) {
			throw new Exception("{\n \"code\": \"-5\",\n \"message\": \"Usuario e senha nao conferem.\"\n}");	
    	}
		else if(result.getCode()==UsuarioServices.ERR_MODULO_INATIVO) {
			throw new Exception("{\n \"code\": \"-6\",\n \"message\": \"A consulta ao DETRAN está inativa. Procure um administrador.\"\n}");	
    	}
		else if(result.getCode()==UsuarioServices.ERR_DENIED_USUARIO_MODULO) {
			throw new Exception("{\n \"code\": \"-7\",\n \"message\": \"O usuario nao tem acesso a consulta ao DETRAN. Procure um administrador.\"\n}");	
    	}
	}
	
}
