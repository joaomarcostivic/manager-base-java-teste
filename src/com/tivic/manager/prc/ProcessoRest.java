package com.tivic.manager.prc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.RestData;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.manager.agd.AgendamentoServices;
import com.tivic.manager.util.Util;

@Path("/prc/processo/")

public class ProcessoRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(RestData restData){
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
			//objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
			TypeReference<ArrayList<HashMap<String,Object>>> typeRef = new TypeReference<ArrayList<HashMap<String,Object>>>() {};
			
            Processo processo = objectMapper.convertValue(restData.getArg("processo"), Processo.class);
            ArrayList<HashMap<String,Object>> clientes = objectMapper.convertValue(restData.getArg("clientes"), typeRef);
            ArrayList<HashMap<String,Object>> adversos = objectMapper.convertValue(restData.getArg("adversos"), typeRef);
            
            ResultSetMap rsmClientes = new ResultSetMap();
            rsmClientes.setLines(clientes);
            
            ResultSetMap rsmAdversos = new ResultSetMap();
            rsmAdversos.setLines(adversos);
            
			Result result = ProcessoServices.save(processo, rsmClientes, rsmAdversos, null);
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
	public String remove(Processo processo){
		try {
			Result result = ProcessoServices.remove(processo.getCdProcesso());
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

//	@POST
//	@Path("/getAll")
//	@Produces(MediaType.APPLICATION_JSON)
//	public static String getAll() {
//		try {
//			ResultSetMap rsm = ProcessoServices.getAll();
//			return Util.rsmToJSON(rsm);
//		} catch(Exception e) {
//			e.printStackTrace(System.out);
//			return null;
//		}
//	}

	@POST
	@Path("/find")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String find(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = ProcessoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/find/cliente")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findCliente(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = ProcessoServices.findCliente(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/find/responsavel")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findResponsavel(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = ProcessoServices.findAdvogado(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/find/partecontraria")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findParteContraria(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = ProcessoServices.findAdverso(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/advogadosempresa")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAdvogadosEmpresa() {
		try {
			ResultSetMap rsm = ProcessoServices.getAdvogadosEmpresa();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/find/advpartecontraria")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findAdvParteContraria(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = ProcessoServices.findAdvogadoAdverso(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	@GET
	@Path("/get/processoandamento/{cdUsuario}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getProcessosMovimentados(@PathParam("cdUsuario") int cdUsuario) {
		try {
			ResultSetMap rsm = ProcessoServices.getProcessosMovimentados(cdUsuario);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	@GET
	@Path("/get/processoandamento/{cdUsuario}/{nrRegistro}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getProcessosMovimentados(@PathParam("cdUsuario") int cdUsuario, @PathParam("nrRegistro") int nrRegistro) {
		try {
			ResultSetMap rsm = ProcessoServices.getProcessosMovimentados(cdUsuario, nrRegistro);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/chart/cadastrados/{mes}/{ano}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getGraficoCadastrados(@PathParam("mes") int mes, @PathParam("ano") int ano) {
		try {			
			return Util.rsmToJSON(ProcessoServices.getCadastrosMensais(mes, ano));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/chart/ativos/grupo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getGraficoAtivosGrupo() {
		try {			
			return Util.rsmToJSON(ProcessoServices.getAtivosPorGrupo());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/chart/ativos/fase")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getGraficoAtivosFase() {
		try {			
			return Util.rsmToJSON(ProcessoServices.getAtivosPorFase());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/chart/ativos/cidade/{limite}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getGraficoAtivosCidade(@PathParam("limite") int limite) {
		try {			
			return Util.rsmToJSON(ProcessoServices.getAtivosPorCidade(limite));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/chart/ativos/estado")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getGraficoAtivosEstado() {
		try {			
			return Util.rsmToJSON(ProcessoServices.getAtivosPorEstado());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/chart/repasse/mensal")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getGraficoRepasseMensal() {
		try {			
			return Util.rsmToJSON(ProcessoServices.getRepasseMensal());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/chart/encerramento/mensal")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getGraficoEncerramentoMensal() {
		try {			
			return Util.rsmToJSON(ProcessoServices.getEncerramentoMensal());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/chart/encerramento/agrupado/grupotrabalho")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getGraficoEncerramentoAgrupado() {
		try {			
			return Util.rsmToJSON(ProcessoServices.getEncerramentoAgrupado());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/chart/encerramento/tipo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getGraficoEncerramentoTipo() {
		try {			
			return Util.rsmToJSON(ProcessoServices.getEncerramentosTipo());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/chart/ativos")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getGraficoBaseAtiva() {
		try {			
			return Util.rsmToJSON(ProcessoServices.getBaseAtiva());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/{cdProcesso}/partecliente")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getParteCliente(@PathParam("cdProcesso") int cdProcesso) {
		try {
			ResultSetMap rsm = ProcessoServices.getParteCliente(cdProcesso);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/{cdProcesso}/partecontraria")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getOutraParte(@PathParam("cdProcesso") int cdProcesso) {
		try {
			ResultSetMap rsm = ProcessoServices.getOutraParte(cdProcesso);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/{cdProcesso}/getandamentos")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAndamentos(@PathParam("cdProcesso") int cdProcesso) {
		try {
			ResultSetMap rsm = ProcessoServices.getAndamentos(cdProcesso);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/{cdProcesso}/getAgendas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAgendas(@PathParam("cdProcesso") int cdProcesso) {
		try {
			ResultSetMap rsm = ProcessoServices.getAgendas(cdProcesso);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	@GET
	@Path("/{cdProcesso}/getArquivos")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getArquivos(@PathParam("cdProcesso") int cdProcesso) {
		try {
			ResultSetMap rsm = ProcessoServices.getArquivos(cdProcesso);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	@GET
	@Path("/{cdProcesso}/getEventoFinanceiro")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getEventoFinanceiro(@PathParam("cdProcesso") int cdProcesso) {
		try {
			ResultSetMap rsm = ProcessoServices.getEventoFinanceiro(cdProcesso);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	@GET
	@Path("/getTipoPedido/{cdProcesso}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getTipoPedido(@PathParam("cdProcesso") int cdProcesso) {
		try {
			ResultSetMap rsm = ProcessoServices.getTipoPedido(cdProcesso);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	@GET
	@Path("/getOutrosAdvogados/{cdProcesso}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getOutrosAdvogados(@PathParam("cdProcesso") int cdProcesso) {
		try {
			ResultSetMap rsm = ProcessoServices.getOutrosAdvogados(cdProcesso);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	@GET
	@Path("/getOutrosInteressados/{cdProcesso}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getOutrosInteressados(@PathParam("cdProcesso") int cdProcesso) {
		try {
			ResultSetMap rsm = ProcessoServices.getOutrosInteressados(cdProcesso);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	@GET
	@Path("/getTestemunhas/{cdProcesso}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getTestemunhas(@PathParam("cdProcesso") int cdProcesso) {
		try {
			ResultSetMap rsm = ProcessoServices.getTestemunhas(cdProcesso);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	@GET
	@Path("/getMensagensByProcesso/{cdProcesso}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getMensagensByProcesso(@PathParam("cdProcesso") int cdProcesso) {
		try {
			ResultSetMap rsm = ProcessoServices.getMensagensByProcesso(cdProcesso);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
}
