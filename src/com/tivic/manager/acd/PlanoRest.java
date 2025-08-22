package com.tivic.manager.acd;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
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
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.sol.connection.Conexao;
import com.tivic.sol.report.ReportServices;
import com.tivic.manager.util.ContextManager;
import com.tivic.manager.util.Util;

@Path("/acd/plano/")

public class PlanoRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Plano plano){
		try {
			Result result = PlanoServices.save(plano);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@PUT
	@Path("/inativar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String inativar(Plano plano){
		try {
			Result result = PlanoServices.inativar(plano);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@PUT
	@Path("/validar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String validar(Plano plano){
		try {
			Result result = PlanoServices.validar(plano);
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
	public String remove(Plano plano){
		try {
			Result result = PlanoServices.remove(plano.getCdPlano());
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
			ResultSetMap rsm = PlanoServices.getAll();
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
			ResultSetMap rsm = PlanoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/findplanoscompartilhados")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findPlanosCompartilhados(ArrayList<ItemComparator> criterios) {
		try {
			
			criterios.add(new ItemComparator("ST_PLANO", "" + PlanoServices.INATIVO, ItemComparator.DIFFERENT, Types.INTEGER));
			criterios.add(new ItemComparator("LG_COMPARTILHADO", "1", ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsm = PlanoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/gerarimpressaoplanocurso")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String gerarImpressaoPlanoCurso(RestData restData) {
		try {
			
			Connection connect = Conexao.conectar();
			
			int cdPlano = Integer.parseInt(String.valueOf(restData.getArg("cdPlano")));
			
			int tpResposta = 0;
			
			Plano plano = PlanoDAO.get(cdPlano);
			Curso curso = CursoDAO.get(plano.getCdCurso());
			Disciplina disciplina = DisciplinaDAO.get(plano.getCdDisciplina());
			Instituicao instituicao = InstituicaoDAO.get(plano.getCdInstituicao());
			InstituicaoPeriodo periodoLetivo = InstituicaoPeriodoDAO.get(plano.getCdPeriodoLetivo());
			Professor professor = ProfessorDAO.get(plano.getCdProfessor());
			Turma turma = TurmaDAO.get(plano.getCdTurma());
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("NM_EMPRESA", instituicao.getNmPessoa());
			params.put("NM_TURMA_COMPLETA", curso.getNmProdutoServico() + " - " + turma.getNmTurma());
			params.put("NM_DISCIPLINA", (disciplina != null ? disciplina.getNmProdutoServico() : null));
			params.put("NM_PROFESSOR", professor.getNmPessoa());
			params.put("QT_CARGA_HORARIA", plano.getQtCargaHoraria());
			
			String txtPlanoCurso = "";
			ResultSetMap rsmPlanoSecao = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_plano_secao ORDER BY id_secao").executeQuery());
			while(rsmPlanoSecao.next()){
				PlanoSecao planoSecao = PlanoSecaoDAO.get(rsmPlanoSecao.getInt("cd_secao"), connect);
				txtPlanoCurso += "<br /><br /><b>" + planoSecao.getIdSecao() + ". " + planoSecao.getNmSecao() + "</b>";
				ResultSetMap rsmPlanoTopico = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_plano_topico A "
																			+ "				WHERE A.cd_plano = " + cdPlano
																			+ "				  AND A.cd_secao = " + rsmPlanoSecao.getInt("cd_secao")
																			+ "          ORDER BY A.nr_ordem").executeQuery());
				while(rsmPlanoTopico.next()){
					
					PlanoTopico planoTopico = PlanoTopicoDAO.get(rsmPlanoTopico.getInt("cd_plano"), rsmPlanoTopico.getInt("cd_secao"), rsmPlanoTopico.getInt("cd_topico"), connect);
					
					txtPlanoCurso += "<br /><t /><t /><t /><t /> -" + planoTopico.getNmTitulo();
					
				}
				rsmPlanoTopico.beforeFirst();
				
			}
			rsmPlanoSecao.beforeFirst();
			
			params.put("TXT_PLANO_CURSO", txtPlanoCurso);
			
			ResultSetMap rsm = PlanoServices.gerarImpressaoPlanoCurso(cdPlano, connect);
			
			if(rsm.size() > 0){
			
				JasperPrint print = ReportServices.getJasperPrint(null, "acd/plano_curso", params, rsm, null);
				
				File directory = new File(ContextManager.getRealPath() + "/plano_curso");
				if (!directory.exists()){
					directory.mkdir();
	            }
				
				File pdf = new File(ContextManager.getRealPath() + "/plano_curso/plano"+cdPlano+".pdf");
				FileOutputStream pdfOutPut = new FileOutputStream(pdf);
				JasperExportManager.exportReportToPdfStream(print, pdfOutPut);
				pdfOutPut.close();
			}
			else{
				tpResposta = 1;
			}
			//Result result = new Result(1, "", "caminhoPdf", "http://edf.pmvc.ba.gov.br//edf//requerimento//requerimento"+cdMatricula+".pdf");
			Result result = new Result(1, "");
			result.addObject("caminhoPdf", "//edf//plano_curso//plano"+cdPlano+".pdf");
			result.addObject("rsmPlano", rsm);
			result.addObject("tpResposta", tpResposta);
			//Result result = new Result(1, "", "caminhoPdf", "http://localhost:8080//edf//requerimento//requerimento"+cdMatricula+".pdf");
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
