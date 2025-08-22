package com.tivic.manager.grl;

import java.util.ArrayList;
import java.util.Map;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.RestData;
import sol.util.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.seg.AcaoServices;
import com.tivic.manager.util.Util;
import com.tivic.sol.auth.jwt.JWTIgnore;

import edu.emory.mathcs.backport.java.util.Arrays;

@Path("/grl/parametro/")

public class ParametroRest {

	
	@POST
	@Path("/getParamsValues")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getParamsValues(RestData restData) {
		try {
			String[] idsArray = String.valueOf(restData.getArg("ids")).split(",");
			ObjectMapper objectMapper = new ObjectMapper();
			
			if(Util.isStrBaseAntiga()) {
				return objectMapper.writeValueAsString(ParametroServices.getParamsBaseAntiga(idsArray));				
			}
			
			ResultSetMap rsm = new ResultSetMap();
			rsm.addRegister(ParametroServices.getParamsValues(idsArray, Integer.parseInt(String.valueOf(restData.getArg("cdEmpresa")))));
			String json = objectMapper.writeValueAsString(rsm.getLines());
			return json;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	

	@POST
	@Path("/getParamValue")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getParamValue(String params) {
		try {			
			ObjectMapper objectMapper = new ObjectMapper();
			String[] idsArray = new String[1];
			idsArray[0] = new JSONObject(params).getString("idParam");
			
			if(Util.isStrBaseAntiga()) {
				return objectMapper.writeValueAsString(ParametroServices.getParamsBaseAntiga(idsArray));				
			}
					
			ResultSetMap rsm = new ResultSetMap();
			rsm.addRegister(ParametroServices.getParamsValues(idsArray, 0));
			String json = objectMapper.writeValueAsString(rsm.getLines());
			return json;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/setValoresOfParametro")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String setValoresOfParametro(RestData restData) {
		try {
			
			int cdParametro = Integer.parseInt(String.valueOf(restData.getArg("cdParametro")));
			int cdEmpresa = Integer.parseInt(String.valueOf(restData.getArg("cdEmpresa")));
			int cdPessoa = Integer.parseInt(String.valueOf(restData.getArg("cdPessoa")));
			
			ObjectMapper mapper = new ObjectMapper();
			
			ArrayList<ParametroValor> values = new ArrayList<ParametroValor>();
			
			//Transformação de cada elemento de valuesArray para ParametroValor, pois no RestData o Array vem como um Array de LinkedMap
			ArrayList<ParametroValor> valuesArray = (ArrayList<ParametroValor>)restData.getArg("valores");
			for(int i = 0; i < valuesArray.size(); i++){
				String jsonInString = mapper.writeValueAsString(valuesArray.get(i));
				ObjectMapper om = new ObjectMapper();
				TypeFactory tf = om.getTypeFactory();
				JavaType type = tf.constructType( ParametroValor.class);
				ParametroValor parametroValor = om.readValue( jsonInString, type );
				values.add(parametroValor);
			}
			
			int retorno = ParametroServices.setValoresOfParametro(cdParametro, cdEmpresa, cdPessoa, values);
			
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(new Result(retorno));
			return json;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAll() {
		try {
			ResultSetMap rsm = ParametroServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/find")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String find(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = ParametroServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String init2() {
		try {
			
			JSONObject response = new JSONObject();
			int i = ParametroServices.init();
			
			if(i > 0) {
				response.put("code", i);
				response.put("message", "Parametros inicializados com sucesso!");
			} else {
				response.put("code", -1);
				response.put("message", "Houve um problema ao inicializar os parametros.");
			}
			
			return response.toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}