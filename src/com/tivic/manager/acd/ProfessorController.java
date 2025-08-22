package com.tivic.manager.acd;

import java.net.HttpURLConnection;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.xml.internal.ws.developer.SerializationFeature;
import com.tivic.manager.grl.EmpresaDAO;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaContaBancaria;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaFichaMedica;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.mob.Ait;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.srh.DadosFuncionais;
import com.tivic.manager.util.Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Professor", authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/acd/professor/")
@Produces(MediaType.APPLICATION_JSON)
public class ProfessorController {
	
	

	@GET
	@Path("/{cdProfessor}/cursos")
	@ApiOperation(
			value = "Busca os cursos de um professor em uma determinada instituição"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado dos cursos"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getCursos(
			@ApiParam(value = "Código do professor", required = true) @PathParam("cdProfessor") int cdProfessor, 
			@ApiParam(value = "Código da instituição onde serão buscados os cursos") @QueryParam("cdInstituicao") int cdInstituicao) {
		try {
			
			if(cdProfessor <= 0) 
				return ResponseFactory.badRequest("Código do professor é nulo ou inválido");
			
			ResultSetMap rsm = CursoServices.getAllByProfessor(cdProfessor, cdInstituicao);
			
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Este professor não está vinculado a nenhum curso");
			}
			
			//:TODO Modificar para Factory quando tiver
			JSONArray array = new JSONArray();
			while(rsm.next()){
				array.put(new JSONObject(Curso.fromRegister(rsm.getRegister())));
			}
			return ResponseFactory.ok(array);
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdProfessor}/turmas")
	@ApiOperation(
			value = "Busca as turmas de um professor em uma determinada instituição e curso"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado das turmas"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getTurmas(
			@ApiParam(value = "Código do professor", required = true) @PathParam("cdProfessor") int cdProfessor, 
			@ApiParam(value = "Código do curso", required = true) @QueryParam("cdCurso") int cdCurso, 
			@ApiParam(value = "Código do instituicao", required = true) @QueryParam("cdInstituicao") int cdInstituicao
		) {
		try {
			
			if(cdProfessor <= 0) 
				return ResponseFactory.badRequest("Código do professor é nulo ou inválido");
			if(cdCurso <= 0) 
				return ResponseFactory.badRequest("Código do curso é nulo ou inválido");
			if(cdInstituicao <= 0) 
				return ResponseFactory.badRequest("Código da instituicao é nulo ou inválido");
			
			
			ResultSetMap rsm = ProfessorServices.getTurmasByProfessor(cdProfessor, cdInstituicao, cdCurso);
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Este professor não está vinculado a nenhuma turma");
			}
			
			//:TODO Modificar para Factory quando tiver
			JSONArray array = new JSONArray();
			while(rsm.next()){
				array.put(new JSONObject(Turma.fromRegister(rsm.getRegister())));
			}
			return ResponseFactory.ok(array);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	

	@GET
	@Path("/sync/{cdProfessor}")
	@Produces(MediaType.APPLICATION_JSON)	
	public static String sincronizacaoInicial(@PathParam("cdProfessor") int cdProfessor) {
		try {			
			ObjectMapper mapper = new ObjectMapper();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			mapper.setDateFormat(df);

			return mapper.writeValueAsString(ProfessorServices.sincronizacaoInicial(cdProfessor, null));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/{cdProfessor}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response get(
		@PathParam("cdProfessor") int cdProfessor
	) {
		try {
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_professor", "" + cdProfessor, ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsm = ProfessorDAO.find(criterios);
			if(rsm.next()) {
				Professor professor = ProfessorDAO.get(rsm.getInt("cd_professor"));
				return Response.ok(new JSONObject(professor.toORM()).toString()).build();
			}
			return Response.status(HttpURLConnection.HTTP_NOT_FOUND, "Professor não encontrado").build();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return Response.serverError().build();
		}
	}
	
	
	
//  ANTIGOS
	
	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Professor professor){
		try {
			Result result = ProfessorServices.save(professor);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@PUT
	@Path("/full/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(RestData args){		
		try {
			ObjectMapper objectMapper = new ObjectMapper();	
			
			Integer gnPessoa				  = objectMapper.convertValue(args.getArg("gnPessoa"), 		Integer.class);
      
			Pessoa pessoa = null;
			if(gnPessoa==PessoaServices.TP_FISICA)
				pessoa = objectMapper.convertValue(args.getArg("pessoa"), PessoaFisica.class);
			else
				pessoa = objectMapper.convertValue(args.getArg("pessoa"), PessoaJuridica.class);
			
			Professor professor 				= objectMapper.convertValue(args.getArg("professor"), Professor.class);
			PessoaEndereco endereco 		  	= objectMapper.convertValue(args.getArg("endereco"), 		PessoaEndereco.class);
			Integer cdEmpresa 			      	= objectMapper.convertValue(args.getArg("cdEmpresa"), 	Integer.class);
			PessoaContaBancaria contaBancaria 	= objectMapper.convertValue(args.getArg("contaBancaria"), PessoaContaBancaria.class);
			DadosFuncionais dadosFuncionais   	= objectMapper.convertValue(args.getArg("dadosFuncionais"), DadosFuncionais.class);
			Boolean possuiDeficiencia	      	= objectMapper.convertValue(args.getArg("possuiDeficiencia"), 	Boolean.class);
			int cdUsuario 			      		= Integer.parseInt(String.valueOf((args.getArg("cdUsuario") != null && !args.getArg("cdUsuario").equals("") ? args.getArg("cdUsuario") : "0")));
			PessoaFichaMedica fichaMedica   	= objectMapper.convertValue(args.getArg("fichaMedica"), PessoaFichaMedica.class);
			String nmLogin 			      		= String.valueOf(args.getArg("nmLogin"));
			String nmSenha  			      	= String.valueOf(args.getArg("nmSenha"));
			
			System.out.println("pessoa = " + pessoa);
			System.out.println("professor = " + professor);
			System.out.println("endereco = " + endereco);
			System.out.println("cdEmpresa = " + cdEmpresa);
			System.out.println("contaBancaria = " + contaBancaria);
			System.out.println("dadosFuncionais = " + dadosFuncionais);
			System.out.println("possuiDeficiencia = " + possuiDeficiencia);
			System.out.println("cdUsuario = " + cdUsuario);
			System.out.println("fichaMedica = " + fichaMedica);
			System.out.println("nmLogin = " + nmLogin);
			System.out.println("nmSenha = " + nmSenha);
			
			Result result = PessoaServices.save(pessoa, endereco, cdEmpresa, 1/*CD_VINCULO_PROFESSOR*/, contaBancaria, dadosFuncionais, professor, (possuiDeficiencia ? 1:0), cdUsuario, fichaMedica);

			Usuario usuario = UsuarioDAO.get(cdUsuario);
			if(usuario == null){
				usuario = new Usuario();
			}
			
			usuario.setNmLogin(nmLogin);
			usuario.setNmSenha(nmSenha);
			usuario.setTpUsuario(UsuarioServices.OPERADOR);
			usuario.setCdPessoa(professor.getCdProfessor());
			
			Result resultUsuario = ProfessorServices.saveUsuario(usuario, cdEmpresa);
			if(resultUsuario.getCode() <= 0){
				return new JSONObject(resultUsuario).toString();
			}
			
			
			
			usuario.setCdUsuario(((Usuario)resultUsuario.getObjects().get("USUARIO")).getCdUsuario());
			
			professor.setCdUsuarioCadastro(((Usuario)resultUsuario.getObjects().get("USUARIO")).getCdUsuario());
			ProfessorDAO.update(professor);
			
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
	public String remove(Professor professor){
		try {
			Result result = ProfessorServices.remove(professor.getCdProfessor());
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
			ResultSetMap rsm = ProfessorServices.getAll();
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
			ResultSetMap rsm = ProfessorServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getdisciplinas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getDisciplinas(RestData restData) {
		try {
			
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			
			ResultSetMap rsm = ProfessorServices.getDisciplinas(cdProfessor, cdTurma, cdInstituicao);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getcursos")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getCursos(RestData restData) {
		try {
			
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			
			ResultSetMap rsm = ProfessorServices.getCursos(cdProfessor, cdInstituicao);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	@POST
	@Path("/getturmasbyprofessor")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getTurmasByProfessor(RestData restData) {
		try {
			
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			int cdCurso = Integer.parseInt(String.valueOf(restData.getArg("cdCurso")));
			
			
			ResultSetMap rsm = ProfessorServices.getTurmasByProfessor(cdProfessor, cdInstituicao, cdCurso);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getturmasdisciplinas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getTurmasDisciplinas(RestData restData) {
		try {
			
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			
			ResultSetMap rsm = ProfessorServices.getTurmasDisciplinas(cdProfessor, cdInstituicao);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
		
	@PUT
	@Path("/saveusuario")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String saveUsuario(RestData restData){
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Usuario usuario = objectMapper.convertValue(restData.getArg("usuario"), Usuario.class);
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			
			Result result = ProfessorServices.saveUsuario(usuario, cdInstituicao);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getlistapendencias")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getListaPendencias(RestData restData) {
		try {
			
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			
			ResultSetMap rsm = ProfessorServices.getListaPendencias(cdProfessor, cdInstituicao);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallinstrutoreseducarte")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllInstrutoresEducarte() {
		try {
			ResultSetMap rsm = ProfessorServices.getAllInstrutoresEducarte();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
