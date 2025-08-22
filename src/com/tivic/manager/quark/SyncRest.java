package com.tivic.manager.quark;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.RestData;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.crt.PessoaServices;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.mob.ConcessaoDAO;
import com.tivic.manager.mob.ConcessaoVeiculoDAO;
import com.tivic.manager.mob.LacreCatracaDAO;
import com.tivic.manager.mob.LacreDAO;
import com.tivic.manager.mob.PlanoVistoriaDAO;
import com.tivic.manager.mob.PlanoVistoriaItemDAO;
import com.tivic.manager.mob.VistoriaDAO;
import com.tivic.manager.util.DeveloperServices;
import com.tivic.manager.util.Util;

@Path("/sync/")

public class SyncRest {

	@GET
	@Path("/tables")
	public String tables(){
		try {
			System.out.println("Buscando tabelas...");
			Connection connect = Conexao.conectar();
			
			ResultSetMap rsmTables = DeveloperServices.getTables(connect);
			
//			while(rsmTables.next()){
//				HashMap<String, String> mapColumns = DeveloperServices.getFieldTypes(rsmTables.getString("TABLE_NAME"), connect);
//				
//				if(mapColumns != null && mapColumns.size() > 0) {
//					rsmTables.getRegister().put("columns", mapColumns);
//				}
//			}
			
			return Util.rsmToJSON(rsmTables);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/tables/count")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String size(String data){
		try {
			ResultSetMap rsm = new ResultSetMap();
			JSONObject json = new JSONObject(data);
			ObjectMapper objectMapper = new ObjectMapper();
			
			@SuppressWarnings("unchecked")
			ArrayList<String> tables = objectMapper.readValue(json.get("tables").toString(), ArrayList.class);
			
			for(String table : tables){
				ResultSetMap rsmTableData = DeveloperServices.getTableCount(table);
				HashMap<String, Object> register = new HashMap<String, Object>();
				HashMap<String, Object> info     = new HashMap<String, Object>();
				
				info.put("COUNT", rsmTableData.getLines().get(0).get("COUNT"));
				//info.put("COLUMNS", DeveloperServices.getFields(table, null));
				
				register.put(table, info);
				rsm.addRegister(register);
			}
			
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/data")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String data(String data){
		try {
			ResultSetMap rsm = new ResultSetMap();
			JSONObject json = new JSONObject(data);
			ObjectMapper objectMapper = new ObjectMapper();
			
			@SuppressWarnings("unchecked")
			HashMap<String, Object> tables = objectMapper.readValue(json.toString(), HashMap.class);
						
			ResultSetMap rsmTableData = DeveloperServices.getData((String)tables.get("table"), (int)tables.get("offset"), (int)tables.get("limit"), (ArrayList<String>)tables.get("columns"), (String)tables.get("orderBy"));
			System.out.println("rsmTableData = " + rsmTableData);
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put((String)tables.get("table"), rsmTableData.getLines());
			rsm.addRegister(register);
			
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/data/etransporte")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String syncEtransporte() {
		try {
			
			Connection connect = Conexao.conectar();
			
			HashMap<String, Object> syncTables = new HashMap<String, Object>();
			
			syncTables.put("grl_pessoa", PessoaDAO.getAll(connect));
			syncTables.put("grl_pessoa_fisica", PessoaFisicaDAO.getAll(connect));
			syncTables.put("grl_pessoa_juridica", PessoaJuridicaDAO.getAll(connect));
			syncTables.put("mob_vistoria", VistoriaDAO.getAll(connect));
			syncTables.put("mob_concessao", ConcessaoDAO.getAll(connect));
			syncTables.put("mob_concessao_veiculo", ConcessaoVeiculoDAO.getAll(connect));
			syncTables.put("mob_lacre_catraca", LacreCatracaDAO.getAll(connect));
			syncTables.put("mob_lacre", LacreDAO.getAll(connect));
			syncTables.put("mob_plano_vistoria", PlanoVistoriaDAO.getAll(connect));
			syncTables.put("mob_plano_vistoria", PlanoVistoriaDAO.getAll(connect));
			syncTables.put("mob_plano_vistoria_item", PlanoVistoriaItemDAO.getAll(connect));
			
			
			
			ResultSetMap rsm = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("Data", syncTables);
			rsm.addRegister(register);
			
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
			ResultSetMap rsm = AitServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
