package com.tivic.manager.acd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.RestData;
import sol.util.Result;
import sun.misc.BASE64Decoder;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaFichaMedica;
import com.tivic.manager.grl.PessoaTipoDocumentacao;
import com.tivic.sol.report.ReportServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ContextManager;
import com.tivic.manager.util.Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;


@Api(value = "Aluno", authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/alunos")
@Produces(MediaType.APPLICATION_JSON)
public class AlunoController {
	
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Alunos"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Turma encontrado", response = Aluno[].class),
			@ApiResponse(code = 204, message = "Turma n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("qtLimite") int qtLimite,
			@ApiParam(value = "Nome da turma") @QueryParam("aluno") String nmPessoa,
			@ApiParam(value = "Nome da turma") @QueryParam("instituicao")  int cdInstituicao
		) {
		try {
			
			Criterios crt = new Criterios("qtLimite", Integer.toString(qtLimite), 0, 0);
			

			if(nmPessoa != null) {
				crt.add("nm_pessoa", nmPessoa, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			if(cdInstituicao > 0) {
				crt.add("cd_instituicao", Integer.toString(cdInstituicao), ItemComparator.LIKE_ANY, Types.INTEGER);
			}			
			
			ResultSetMap _rsm = AlunoServices.findSimplificado(crt);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum Instituicao encontrada");
			}
			
			return ResponseFactory.ok(_rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	
	//Antigos
	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(RestData restData){
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			Aluno aluno = objectMapper.convertValue(restData.getArg("aluno"), Aluno.class);
			Matricula matricula = objectMapper.convertValue(restData.getArg("matricula"), Matricula.class);
			PessoaEndereco pessoaEndereco = objectMapper.convertValue(restData.getArg("pessoaEndereco"), PessoaEndereco.class);
			PessoaFichaMedica pessoaFichaMedica = objectMapper.convertValue(restData.getArg("pessoaFichaMedica"), PessoaFichaMedica.class);
			int cdEmpresa = Integer.parseInt(String.valueOf(restData.getArg("cdEmpresa")));
			int cdVinculo = Integer.parseInt(String.valueOf(restData.getArg("cdVinculo")));
			PessoaTipoDocumentacao pessoaTipoDocumentacao = objectMapper.convertValue(restData.getArg("pessoaTipoDocumentacao"), PessoaTipoDocumentacao.class);
			int possuiDeficiencia = (restData.getArg("possuiDeficiencia") != null ? Integer.parseInt(String.valueOf(restData.getArg("possuiDeficiencia"))) : -1);
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			int cdTipoTransporte = -1;
			if(restData.getArg("cdTipoTransporte") != null)
				cdTipoTransporte = Integer.parseInt(String.valueOf(restData.getArg("cdTipoTransporte")));
			String nrNis = String.valueOf(restData.getArg("nrNis"));
			if(nrNis.equals("null"))
				nrNis = null;
			String nrPassaporte = String.valueOf(restData.getArg("nrPassaporte"));
			if(nrPassaporte.equals("null"))
				nrPassaporte = null;
			String nrSus = String.valueOf(restData.getArg("nrSus"));
			if(nrSus.equals("null"))
				nrSus = null;
			Result result = AlunoServices.save(aluno, pessoaEndereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacao, possuiDeficiencia, matricula, cdUsuario, nrNis, nrPassaporte, nrSus, cdTipoTransporte);
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
	public String remove(Aluno aluno){
		try {
			Result result = AlunoServices.remove(aluno.getCdAluno());
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@GET
	@Path("/{cdAluno}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String get(@PathParam("cdAluno") int cdAluno) {
		try {
			Aluno aluno = AlunoDAO.get(cdAluno);
			return aluno.toString();
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
			ResultSetMap rsm = AlunoServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	

	@POST
	@Path("/getinstituicaobyaluno")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getInstituicaoByAluno(RestData restData) {
		try {
			int cdAluno = Integer.parseInt(String.valueOf(restData.getArg("cdAluno")));
			
			Result result = AlunoServices.getInstituicaoByAluno(cdAluno);
			
			return new JSONObject(result).toString();
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
			ResultSetMap rsm = AlunoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/findsimplificado")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findSimplificado(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = AlunoServices.findSimplificado(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/boletim")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String boletim(RestData restData) {
		try {
			
			int cdAluno = Integer.parseInt(String.valueOf(restData.getArg("cdAluno")));
			
			
			ResultSetMap rsmMatricula = MatriculaServices.getMatriculaRegularAtualByAluno(cdAluno);
			Matricula matricula = null;
			if(rsmMatricula.next()){
				matricula = MatriculaDAO.get(rsmMatricula.getInt("cd_matricula"));
			}
			
			Aluno aluno = AlunoDAO.get(cdAluno);
			Turma turma = TurmaDAO.get(matricula.getCdTurma());
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao());
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(turma.getCdPeriodoLetivo());
			Curso curso = CursoDAO.get(turma.getCdCurso());
			Instituicao secretaria = InstituicaoDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0));
			
			
			Result result = OfertaAvaliacaoServices.getBoletimByAluno(cdAluno);
			
			ResultSetMap rsmBoletim = (ResultSetMap) result.getObjects().get("RSM_BOLETIM");
			ResultSetMap rsmUnidades = (ResultSetMap) result.getObjects().get("RSM_UNIDADES");
			
			ArrayList<HashMap<String, Object>> colunas = new ArrayList<HashMap<String,Object>>();
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("LABEL", "Disciplina");
			register.put("WIDTH", 100);
			register.put("HEIGHT", 15);
			register.put("FIELD", "NM_DISCIPLINA");
			register.put("FIELD_CLASS", "java.lang.String");
			colunas.add(register);
			
			while(rsmUnidades.next()){
				register = new HashMap<String, Object>();
				register.put("LABEL", rsmUnidades.getString("NM_UNIDADE"));
				register.put("WIDTH", 100);
				register.put("HEIGHT", 15);
				register.put("FIELD", rsmUnidades.getString("COLUMN_UNIDADE"));
				register.put("FIELD_CLASS", "java.lang.String");
				colunas.add(register);
			}
			
			register = new HashMap<String, Object>();
			register.put("LABEL", "Total");
			register.put("WIDTH", 100);
			register.put("HEIGHT", 15);
			register.put("FIELD", "TOTAL_NOTAS");
			register.put("FIELD_CLASS", "java.lang.String");
			colunas.add(register);
			
			register = new HashMap<String, Object>();
			register.put("LABEL", "Média");
			register.put("WIDTH", 100);
			register.put("HEIGHT", 15);
			register.put("FIELD", "MEDIA_NOTAS");
			register.put("FIELD_CLASS", "java.lang.String");
			colunas.add(register);
			
			register = new HashMap<String, Object>();
			register.put("LABEL", "Faltas");
			register.put("WIDTH", 100);
			register.put("HEIGHT", 15);
			register.put("FIELD", "NR_FALTAS");
			register.put("FIELD_CLASS", "java.lang.String");
			colunas.add(register);
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("IMG_LOGOMARCA", secretaria.getImgLogomarca());
			params.put("NM_PERIODO_LETIVO", instituicaoPeriodo.getNmPeriodoLetivo());
			params.put("NM_ALUNO", aluno.getNmPessoa());
			params.put("NR_MATRICULA", matricula.getNrMatricula());
			params.put("NM_INSTITUICAO", instituicao.getNmPessoa());
			params.put("CL_TURMA", curso.getNmProdutoServico() + " - " + turma.getNmTurma());
			params.put("CL_TURNO", TurmaServices.tiposTurno[turma.getTpTurno()]);
			params.put("DYNAMIC_COLUMNS", colunas);
			params.put("DYNAMIC_COLUMNS_HEADER_KEY", "DYNAMIC_COLUMNS_HEADER_KEY");
			params.put("DYNAMIC_COLUMNS_DETAIL_KEY", "DYNAMIC_COLUMNS_DETAIL_KEY");
			
			JasperPrint print = ReportServices.getJasperPrint(null, "acd/boletim", params, rsmBoletim, null);
			
			File directory = new File(ContextManager.getRealPath() + "/boletins");
			if (!directory.exists()){
				directory.mkdir();
            }
			
			directory = new File(ContextManager.getRealPath() + "/boletins/"+instituicaoPeriodo.getNmPeriodoLetivo());
			if (!directory.exists()){
				directory.mkdir();
            }
			
			File pdf = new File(ContextManager.getRealPath() + "/boletins/"+instituicaoPeriodo.getNmPeriodoLetivo()+"/aluno"+cdAluno+".pdf");
			FileOutputStream pdfOutPut = new FileOutputStream(pdf);
			JasperExportManager.exportReportToPdfStream(print, pdfOutPut);
			pdfOutPut.close();
			//Result result = new Result(1, "", "caminhoPdf", "http://edf.pmvc.ba.gov.br//edf//requerimento//requerimento"+cdMatricula+".pdf");
			result = new Result(1, "");
			result.addObject("caminhoPdf", "//edf//boletins//"+instituicaoPeriodo.getNmPeriodoLetivo()+"//aluno"+cdAluno+".pdf");
			result.addObject("rsm", rsmBoletim);
			//Result result = new Result(1, "", "caminhoPdf", "http://localhost:8080//edf//requerimento//requerimento"+cdMatricula+".pdf");
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	@POST
	@Path("/findalunobynome")
	@Produces(MediaType.APPLICATION_JSON)
	public static String findAlunoByNome(String parametros, 
            						 @HeaderParam("authorization") String authString) {
		try {
			if(!isUserAuthenticated(authString)){
	            return "{\"erro\":\"Usuário não foi autenticado\"}";
	        }
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			
			JSONObject json = new JSONObject(parametros);
			Iterator<?> keys = json.keys();

			while( keys.hasNext() ) {
			    String key = (String)keys.next();
			    if(key.equals("nome"))
			    	criterios.add(new ItemComparator("B.NM_PESSOA", "" + json.get(key), ItemComparator.LIKE_ANY, Types.VARCHAR));
			}
			
			if(criterios.size() == 0){
				return "{\"erro\": \"Nenhum critério foi adicionado\"}";
			}
			
			ResultSetMap rsm = AlunoServices.findCatraca(criterios);
			
			if(rsm.size() == 0){
				return "{\"erro\": \"Nenhum aluno com esse nome foi encontrado\"}";
			}
			
			String jsonResponse = "{";
			while(rsm.next()){
				for(String key : rsm.getRegister().keySet()){
					jsonResponse += "\"" + key + "\":\"" + rsm.getString(key) + "\", ";
				}
			}
			
			jsonResponse = jsonResponse.substring(0, jsonResponse.length()-2) + "}";
			
			return jsonResponse;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	
	
	
	@POST
	@Path("/findalunobymatricula")
	@Produces(MediaType.APPLICATION_JSON)
	public static String findAlunoByMatricula(String parametros, 
            						 @HeaderParam("authorization") String authString) {
		try {
			if(!isUserAuthenticated(authString)){
	            return "{\"erro\":\"Usuário não foi autenticado\"}";
	        }
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			
			JSONObject json = new JSONObject(parametros);
			Iterator<?> keys = json.keys();

			while( keys.hasNext() ) {
			    String key = (String)keys.next();
			    if(key.equals("matricula"))
			    	criterios.add(new ItemComparator("F.NR_MATRICULA", "" + json.get(key), ItemComparator.EQUAL, Types.VARCHAR));
			}
			
			if(criterios.size() == 0){
				return "{\"erro\": \"Nenhum critério foi adicionado\"}";
			}
			
			ResultSetMap rsm = AlunoServices.findCatraca(criterios);
			
			if(rsm.size() == 0){
				return "{\"erro\": \"Nenhum aluno com esse numero de matricula foi encontrada\"}";
			}
			
			String jsonResponse = "{";
			while(rsm.next()){
				for(String key : rsm.getRegister().keySet()){
					jsonResponse += "\"" + key + "\":\"" + rsm.getString(key) + "\", ";
				}
			}
			
			jsonResponse = jsonResponse.substring(0, jsonResponse.length()-2) + "}";
			
			return jsonResponse;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	private static boolean isUserAuthenticated(String authString){
        
        String decodedAuth = "";
        // Decode the data back to original string
        byte[] bytes = null;
        try {
            bytes = new BASE64Decoder().decodeBuffer(authString);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        decodedAuth = new String(bytes);
        
        String[] decodedAuthSplit = decodedAuth.split(":");
        String nome = decodedAuthSplit[0];
        String senha = decodedAuthSplit[1];
        
        if(!nome.equals("prefeituraVCA") || !senha.equals("pref#SMED")){
        	return false;
        }
         
        return true;
    }
	
	

}

