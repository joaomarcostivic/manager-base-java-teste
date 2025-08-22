package com.tivic.manager.grl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.Search;
import sol.util.RestData;

@Path("/grl/pessoa/")

public class PessoaRest {
	
	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Pessoa pessoa){
		try {
			Result result = PessoaServices.save(pessoa, null, 0, 0);
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
			
			PessoaEndereco endereco 		  = objectMapper.convertValue(args.getArg("endereco"), 		PessoaEndereco.class);
			Integer cdEmpresa 			      = objectMapper.convertValue(args.getArg("cdEmpresa"), 	Integer.class);
			PessoaContaBancaria contaBancaria = objectMapper.convertValue(args.getArg("contaBancaria"), PessoaContaBancaria.class);
			ArrayList<Integer> vinculos       = objectMapper.convertValue(args.getArg("vinculos"), 		ArrayList.class);
			
			System.out.println("pessoa = " + pessoa);
			System.out.println("endereco = " + endereco);
			System.out.println("cdEmpresa = " + cdEmpresa);
			System.out.println("contaBancaria = " + contaBancaria);
			System.out.println("vinculos = " + vinculos);
			
			Result result = PessoaServices.save(pessoa, endereco, cdEmpresa, 0, contaBancaria, vinculos);
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
	public String remove(Pessoa pessoa){
		try {
			Result result = PessoaServices.remove(pessoa.getCdPessoa());
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findRest(
		@QueryParam("cdPessoa") int cdPessoa,
		@QueryParam("findProfessor") int findProfessor
	) {
		try {
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_pessoa", "" + cdPessoa, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("findProfessor", "" + findProfessor, ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsm = PessoaServices.find(criterios);
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
			ResultSetMap rsm = PessoaServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/findSimplificado")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findSimplificado(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = Search.find("SELECT * FROM grl_pessoa", criterios, Conexao.conectar());
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/findAllCartorios")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findAllCartorios(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = PessoaServices.findAllCartorios(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/vinculos/{cdEmpresa}/{cdPessoa}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getVinculos(@PathParam("cdEmpresa") int cdEmpresa, @PathParam("cdPessoa") int cdPessoa) {
		try {
			ResultSetMap rsm = PessoaServices.getAllVinculosOfPessoa(cdPessoa, cdEmpresa);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/arquivos/{cdPessoa}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getArquivos(@PathParam("cdPessoa") int cdPessoa) {
		try {
			ResultSetMap rsm = PessoaServices.getAllArquivosOfPessoa(cdPessoa);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@PUT
	@Path("/save/arquivos")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String saveFile(@FormDataParam("pessoa") FormDataBodyPart cdPessoa, 
						   @FormDataParam("usuario") FormDataBodyPart cdUsuario, 
						   @FormDataParam("arquivo") FormDataBodyPart nmArquivo, 
						   @FormDataParam("files[]") FormDataBodyPart files){
		try {			
			Result result = PessoaArquivoServices.save(cdPessoa, cdUsuario, nmArquivo, files);
			return new JSONObject(result).toString();
		} catch(Exception e) {
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
			
			ArrayList<Pessoa> pessoa = new ArrayList<Pessoa>();
			
			if(bodyParsed != null) {
				pessoa = mapper.convertValue(bodyParsed.get("Pessoa"), new TypeReference<ArrayList<Pessoa>>() {});				
			}
			 
			HashMap<String, Object> registers = PessoaServices.getSyncDataTransporte();
			
			return mapper.writeValueAsString(registers);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
