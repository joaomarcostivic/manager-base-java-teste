package com.tivic.manager.acd;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Types;
import java.util.ArrayList;
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
import javax.ws.rs.core.Response;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.PessoaFichaMedica;
import com.tivic.sol.report.ReportServices;
import com.tivic.manager.util.ContextManager;
import com.tivic.manager.util.Util;

@Path("/acd/matricula/")

public class MatriculaRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(RestData restData){
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			Matricula matricula = objectMapper.convertValue(restData.getArg("matricula"), Matricula.class);
			int cdCursoModulo = Integer.parseInt(String.valueOf(restData.getArg("cdCursoModulo")));
			boolean lgRecebeBolsaFamilia = Boolean.parseBoolean(String.valueOf(restData.getArg("lgRecebeBolsaFamilia")));
			boolean lgParticipaMaisEducacao = Boolean.parseBoolean(String.valueOf(restData.getArg("lgParticipaMaisEducacao")));
			boolean permitirAlunoCidadeDiferente = Boolean.parseBoolean(String.valueOf(restData.getArg("permitirAlunoCidadeDiferente")));
			boolean permitirAlunoIdadeDivergente = Boolean.parseBoolean(String.valueOf(restData.getArg("permitirAlunoIdadeDivergente")));
			int cdTipoTransporte = Integer.parseInt(String.valueOf(restData.getArg("cdTipoTransporte")));
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			
			Result result = MatriculaServices.save(matricula, cdCursoModulo, lgRecebeBolsaFamilia, lgParticipaMaisEducacao, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, cdTipoTransporte, cdUsuario, null);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@PUT
	@Path("/saveativar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String saveAtivar(RestData restData){
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			Matricula matricula = objectMapper.convertValue(restData.getArg("matricula"), Matricula.class);
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			Aluno aluno = objectMapper.convertValue(restData.getArg("aluno"), Aluno.class);
			PessoaEndereco endereco = objectMapper.convertValue(restData.getArg("endereco"), PessoaEndereco.class);
			boolean permitirAlunoCidadeDiferente = Boolean.parseBoolean(String.valueOf(restData.getArg("permitirAlunoCidadeDiferente")));
			boolean permitirAlunoIdadeDivergente = Boolean.parseBoolean(String.valueOf(restData.getArg("permitirAlunoIdadeDivergente")));
			String nrSus = String.valueOf(restData.getArg("nrSus"));
			int cdTipoTransporte = Integer.parseInt(String.valueOf(restData.getArg("cdTipoTransporte")));
			
			Result result = MatriculaServices.saveAtivar(matricula, cdUsuario, 15 /*tpOcorrencia*/, aluno, endereco, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, nrSus, cdTipoTransporte);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@PUT
	@Path("/savesolicitacaotransferencia")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String saveSolicitacaoTransferencia(RestData restData){
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			Matricula matricula = objectMapper.convertValue(restData.getArg("matricula"), Matricula.class);
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			
			Result result = MatriculaServices.saveSolicitacaoTransferencia(matricula, cdUsuario);
			
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = MatriculaServices.find(criterios);
			
			JasperPrint print = ReportServices.getJasperPrint(null, "acd/atestado_transferencia_codigo", null, rsm, null);
			
			File directory = new File(ContextManager.getRealPath() + "/atestado_transferencia");
			if (!directory.exists()){
				directory.mkdir();
            }
			
			File pdf = new File(ContextManager.getRealPath() + "/atestado_transferencia/atestado_transferencia"+matricula.getCdMatricula()+".pdf");
			FileOutputStream pdfOutPut = new FileOutputStream(pdf);
			JasperExportManager.exportReportToPdfStream(print, pdfOutPut);
			pdfOutPut.close();
			
			result.addObject("caminhoPdf", "//edf//atestado_transferencia//atestado_transferencia"+matricula.getCdMatricula()+".pdf");
			
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@PUT
	@Path("/savetransferencia")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String saveTransferencia(RestData restData){
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			Matricula matriculaAntiga = objectMapper.convertValue(restData.getArg("matriculaAntiga"), Matricula.class);
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			int cdPeriodoLetivo = Integer.parseInt(String.valueOf(restData.getArg("cdPeriodoLetivo")));
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			boolean lancarIdadeDivergente = Boolean.parseBoolean(String.valueOf(restData.getArg("lancarIdadeDivergente")));
			
			Result result = MatriculaServices.saveTransferencia(matriculaAntiga, cdUsuario, cdPeriodoLetivo, cdTurma, lancarIdadeDivergente);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@PUT
	@Path("/savetransferenciadireta")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String saveTransferenciaDireta(RestData restData){
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			Matricula matriculaAntiga = objectMapper.convertValue(restData.getArg("matriculaAntiga"), Matricula.class);
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			int cdPeriodoLetivo = Integer.parseInt(String.valueOf(restData.getArg("cdPeriodoLetivo")));
			int cdCurso = Integer.parseInt(String.valueOf(restData.getArg("cdCurso")));
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			
			
			Result result = MatriculaServices.saveTransferenciaDireta(matriculaAntiga, cdUsuario, cdPeriodoLetivo, cdCurso, cdTurma);
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
	public String remove(Matricula matricula){
		try {
			Result result = MatriculaServices.remove(matricula.getCdMatricula());
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
			ResultSetMap rsm = MatriculaServices.getAll();
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
			ResultSetMap rsm = MatriculaServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getdadosrequerimentobymatricula")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getDadosRequerimentoByMatricula(RestData restData) {
		try {
			
			int cdMatricula = Integer.parseInt(String.valueOf(restData.getArg("cdMatricula")));
			
			ResultSetMap rsm = MatriculaServices.getDadosRequerimentoByMatricula(cdMatricula);
			
//			JasperPrint print = ReportServices.getJasperPrint(null, "acd/requerimento_matricula", null, rsm, null);
//			
//			File directory = new File(ContextManager.getRealPath() + "/requerimento");
//			if (!directory.exists()){
//				directory.mkdir();
//            }
//			
//			File pdf = new File(ContextManager.getRealPath() + "/requerimento/requerimento"+cdMatricula+".pdf");
//			FileOutputStream pdfOutPut = new FileOutputStream(pdf);
//			JasperExportManager.exportReportToPdfStream(print, pdfOutPut);
//			pdfOutPut.close();
			//Result result = new Result(1, "", "caminhoPdf", "http://edf.pmvc.ba.gov.br//edf//requerimento//requerimento"+cdMatricula+".pdf");
			Result result = new Result(1, "");
//			result.addObject("caminhoPdf", "//edf//requerimento//requerimento"+cdMatricula+".pdf");
			result.addObject("rsmMatricula", rsm);
			//Result result = new Result(1, "", "caminhoPdf", "http://localhost:8080//edf//requerimento//requerimento"+cdMatricula+".pdf");
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getdadosatestadotransferencia")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getDadosAtestadoTransferencia(RestData restData) {
		try {
			
			int cdMatricula = Integer.parseInt(String.valueOf(restData.getArg("cdMatricula")));
			String txtDeclaracao = String.valueOf(restData.getArg("txtDeclaracao"));
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula", "" + cdMatricula, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = MatriculaServices.find(criterios);
			
			Instituicao secretaria = InstituicaoDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0));
			
			Matricula matricula = MatriculaDAO.get(cdMatricula);
			Turma turma = TurmaDAO.get(matricula.getCdTurma());
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao());
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("TXT_DECLARACAO", txtDeclaracao);
			params.put("IMG_LOGOMARCA", secretaria.getImgLogomarca());
			params.put("NM_INSTITUICAO", instituicao.getNmPessoa());
			params.put("NM_ENDERECO_COMPLETO", PessoaEnderecoServices.getEnderecoCompleto(instituicao.getCdInstituicao()));
			params.put("NR_AUTORIZACAO", "");
			params.put("NR_INEP", instituicao.getNrInep());
			
//			JasperPrint print = ReportServices.getJasperPrint(null, "acd/atestado_transferencia", params, rsm, null);
//			File directory = new File(ContextManager.getRealPath() + "/atestado_transferencia");
//			if (!directory.exists()){
//				directory.mkdir();
//            }
//			File pdf = new File(ContextManager.getRealPath() + "/atestado_transferencia/atestado_transferencia"+cdMatricula+".pdf");
//			FileOutputStream pdfOutPut = new FileOutputStream(pdf);
//			JasperExportManager.exportReportToPdfStream(print, pdfOutPut);
//			pdfOutPut.close();
			Result result = new Result(1);
			
			//result.addObject("caminhoPdf", "//edf//atestado_transferencia//atestado_transferencia"+cdMatricula+".pdf");
			result.addObject("rsm", rsm);
			result.addObject("params", params);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/findsecretariacurso")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findSecretariaCurso(RestData restData) {
		try {
			
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			int cdCurso = Integer.parseInt(String.valueOf(restData.getArg("cdCurso")));
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("B.CD_INSTITUICAO", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.CD_CURSO", "" + cdCurso, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = MatriculaServices.findSecretaria(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/findsecretaria")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findSecretaria(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = MatriculaServices.findSecretaria(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/sync")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String sync(RestData restData) {
		try {

			ObjectMapper objectMapper = new ObjectMapper();
			Matricula matriculas[] = objectMapper.convertValue(restData.getArg("matricula"), Matricula[].class);
			
			MatriculaServices.sync(matriculas);
			
			
			return "Sincronização finalizada";
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/conservaralunomatriculaorigem")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String conservarAlunoMatriculaOrigem(RestData restData) {
		try {

			int cdMatricula = Integer.parseInt(String.valueOf(restData.getArg("cdMatricula")));
			int cdTurmaDestino = Integer.parseInt(String.valueOf(restData.getArg("cdTurmaDestino")));
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			
			Result result = MatriculaServices.conservarAlunoMatriculaOrigem(cdMatricula, cdTurmaDestino, cdUsuario);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/cancelamentorecebimento")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String cancelamentoRecebimento(RestData restData) {
		try {

			int cdMatricula = Integer.parseInt(String.valueOf(restData.getArg("cdMatricula")));
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			
			Result result = MatriculaServices.cancelamentoRecebimento(cdMatricula, cdUsuario);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/cancelamentodesistenciaevasao")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String cancelamentoDesistenciaEvasao(RestData restData) {
		try {

			int cdMatricula = Integer.parseInt(String.valueOf(restData.getArg("cdMatricula")));
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			
			Result result = MatriculaServices.cancelamentoDesistenciaEvasao(cdMatricula, cdUsuario);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getcartorio")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getCartorio(RestData restData) {
		try {

			String nrDocumento = String.valueOf(restData.getArg("nrDocumento"));
			
			ResultSetMap rsm = MatriculaServices.getCartorio(nrDocumento);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	

	@GET
	@Path("/{cdMatricula}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response get(@PathParam("cdMatricula") int cdMatricula) {
		try {
			
			Matricula matricula = MatriculaDAO.get(cdMatricula);
			return Response.ok(new JSONObject(matricula.toORM()).toString()).build();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return Response.serverError().build();
		}
	}
	

}
