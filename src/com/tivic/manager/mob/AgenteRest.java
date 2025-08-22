package com.tivic.manager.mob;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.Util;

@Path("/mob/agente/")

public class AgenteRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Agente agente){
		try {
			Result result = AgenteServices.save(agente);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@DELETE
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(Agente agente){
		try {
			Result result = AgenteServices.remove(agente.getCdAgente());
			return new JSONObject(result).toString();
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

			ResultSetMap rsm = new ResultSetMap();
			boolean lgBaseAntiga = Util.isStrBaseAntiga();
			
			if(!lgBaseAntiga)
				rsm = AgenteServices.getAll();
			else 
				rsm = com.tivic.manager.str.AgenteServices.getAllAgentes();
			
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
			ResultSetMap rsm = AgenteServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/autenticar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@SuppressWarnings("unchecked")
	public String autenticar(Usuario usuario){
		try {
			Result result = AgenteServices.autenticar(usuario.getNmLogin(), usuario.getNmSenha(), 0);
			if(result.getCode() > 0){
				Usuario usuarioAgente = UsuarioDAO.get((int)((HashMap<String, Object>)result.getObjects().get("AGENTE")).get("CD_USUARIO"));
				result.addObject("PESSOA", PessoaDAO.get(usuarioAgente.getCdPessoa()));
			}			
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/{cdAgente}/evento/{cdEvento}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String ultimoId(@PathParam("cdAgente") int cdAgente, @PathParam("cdEvento") int cdEvento){
		Connection connect = Conexao.conectar();
		try {
			Agente agente = AgenteDAO.get(cdAgente);
			EventoEquipamento evento = EventoEquipamentoDAO.get(cdEvento);
			com.tivic.manager.grl.equipamento.Equipamento equipamento = com.tivic.manager.grl.equipamento.repository.EquipamentoDAO.get(evento.getCdEquipamento());
			Talonario talao = TalonarioServices.getTalaoRadar(agente, equipamento, connect);
			
			if(talao != null) {
				return "{\"nrUltimoNumeroAit\": " + (talao.getNrUltimoAit() + 1) + "}";
			}
			
			return "{\"nrUltimoNumeroAit\": null}";		
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	@GET
	@Path("/monitoramento/nrUltimoAit/{cdAgente}/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String ultimoNrAit(@PathParam("cdAgente") int cdAgente){
		Connection connect = Conexao.conectar();
		try {
			Agente agente;
			
			if(Util.isStrBaseAntiga())
				agente = AgenteServices.getBaseAntiga(cdAgente, connect);
			else 
				agente = AgenteDAO.get(cdAgente);
			
			Talonario talao = TalonarioServices.getTalaoMonitoramento(agente, connect);
						
			if(talao != null) {
				return "{\"nrUltimoNumeroAit\": " + (talao.getNrUltimoAit() + 1) + "}";
			}
			
			return "{\"nrUltimoNumeroAit\": null}";		
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	@GET
	@Path("/boat/nrUltimoBoat/{cdAgente}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@SuppressWarnings("unchecked")
	public String ultimoNrBoat(@PathParam("cdAgente") int cdAgente){
		Connection connect = Conexao.conectar();
		try {
			Agente agente = AgenteDAO.get(cdAgente, connect);			
			Result taloes = TalonarioServices.getTalonariosByAgente(agente, connect);
			
			System.out.println(taloes);
			
			for(Talonario talao : (ArrayList<Talonario>)taloes.getObjects().get("TALOES")) {
				if(talao.getTpTalao() == TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT) {
					return "{\"nrUltimoNumeroBoat\": " + (talao.getNrUltimoAit() + 1) + "}";		
				}
			}
			
			return "{\"nrUltimoNumeroBoat\": -1}";		
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
}
