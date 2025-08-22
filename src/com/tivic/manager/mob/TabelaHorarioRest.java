package com.tivic.manager.mob;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.manager.util.Util;

@Path("/mob/tabelahorario/")

public class TabelaHorarioRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(TabelaHorario tabelaHorario) {
		try {
			Result result = TabelaHorarioServices.save(tabelaHorario);
			return new JSONObject(result).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@DELETE
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(TabelaHorario tabelaHorario) {
		try {
			Result result = TabelaHorarioServices.remove(tabelaHorario.getCdTabelaHorario(),
					tabelaHorario.getCdLinha());
			return new JSONObject(result).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAll() {
		try {
			ResultSetMap rsm = TabelaHorarioServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch (Exception e) {
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
			ResultSetMap rsm = TabelaHorarioServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/sync")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String sync(String body) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode bodyParsed = mapper.readTree(body);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"));

			ArrayList<TabelaHorario> tabelaHorario = new ArrayList<TabelaHorario>();
			ArrayList<Horario> horario = new ArrayList<Horario>();

			if (bodyParsed != null) {
				tabelaHorario = mapper.convertValue(bodyParsed.get("TabelaHorario"), new TypeReference<ArrayList<TabelaHorario>>() {});
//				horario = mapper.convertValue(bodyParsed.get("Horario"), new TypeReference<ArrayList<Horario>>() {});
			}

			HashMap<String, Object> registers = TabelaHorarioServices.getSyncData(tabelaHorario);

			return mapper.writeValueAsString(registers);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
