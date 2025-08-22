package com.tivic.manager.bdv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

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

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.importacao.WebImport;
import com.tivic.manager.log.BuscaPlaca;
import com.tivic.manager.log.BuscaPlacaServices;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.util.Result;

@Path("/bdv/veiculo")
public class VeiculoRest {

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String post(Veiculo veiculo){
		try {
			
			Result result = VeiculoServices.save(veiculo);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getList() {
		return "{\"code\": 200 }";
//		try {
//			ResultSetMap rsm = VeiculoServices.getAll();
//			return Util.rsmToJSON(rsm);
//		} catch(Exception e) {
//			e.printStackTrace(System.out);
//			return null;
//		}
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String get(@PathParam("id") int cdVeiculo) {
		try {
			return new JSONObject(VeiculoDAO.get(cdVeiculo)).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String put(@PathParam("id") int cdVeiculo, Veiculo veiculo){
		try {
			veiculo.setCdVeiculo(cdVeiculo);
			Result result = VeiculoServices.save(veiculo);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/consulta/{placa}")
	@Produces(MediaType.APPLICATION_JSON)
	public String find(@PathParam("placa") String nrPlaca) {
		return find(null, 0, nrPlaca);
	}
	
	@GET
	@Path("/consulta/{id}/{placa}")
	@Produces(MediaType.APPLICATION_JSON)
	@Deprecated
	public String find(@PathParam("id") int cdUsuario, @PathParam("placa") String nrPlaca) {
		return find(null, cdUsuario, nrPlaca);
	}
	
	@GET
	@Path("/consulta/{orgao}/{id}/{placa}")
	@Produces(MediaType.APPLICATION_JSON)
	@Deprecated
	public String find(@PathParam("orgao") String idOrgao, @PathParam("id") int cdUsuario, @PathParam("placa") String nrPlaca) {
		return findPlaca(idOrgao, cdUsuario, nrPlaca);
	}
	
//	@GET
//	@Path("/busca")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public String search(@QueryParam("placa") String nrPlaca) {
//		return findPlaca(null, 0, nrPlaca);
//	}
//	
//	@GET
//	@Path("/busca")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public String search(@QueryParam("placa") String nrPlaca, @QueryParam("id") int cdUsuario) {
//		return findPlaca(null, cdUsuario, nrPlaca);
//	}
//	
//	@GET
//	@Path("/busca")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public String search(@QueryParam("placa") String nrPlaca, @QueryParam("id") int cdUsuario, @QueryParam("orgao") String idOrgao) {
//		return findPlaca(idOrgao, cdUsuario, nrPlaca);
//	}
	
	private String findPlaca(String idOrgao, int cdUsuario, String nrPlaca) {

		BuscaPlaca log = new BuscaPlaca(0, BuscaPlacaServices.TP_TIVIC, new GregorianCalendar(), nrPlaca, null, null, null, idOrgao, cdUsuario, BuscaPlacaServices.TP_LOG_SUCESSO);
		
		try {
			
			if(nrPlaca.length() < 7 || nrPlaca.length() > 8) {
				throw new Exception("Placa inválida.");
			}
			
			nrPlaca = nrPlaca.replaceAll("-", "").toUpperCase();
			
			String placaBR = "^[A-Z]{3}[0-9]{4}$";
			String placaMercosul = "^[A-Z]{3}[0-9]{1}[A-Z]{1}[0-9]{2}$";	
			
			if(!Pattern.matches(placaBR, nrPlaca) && !Pattern.matches(placaMercosul, nrPlaca)) {
				throw new Exception("Placa inválida.");
			}
			
			Veiculo veiculo = VeiculoServices.get(nrPlaca);
			
			if(veiculo == null) {
				throw new Exception("Erro ao processar a busca. Por favor, entre em contato com a TIVIC.");
			}
			
			if(veiculo.getNrPlaca() == null) {
				return "{\"code\":-2, \"message\":\"Veículo não encontrado\", \"detail\":\"A placa '"+nrPlaca+"' não se encontra em nossas bases de dados.\"}";
			}
			
			JSONObject json = new JSONObject(veiculo);
			json.remove("cdVeiculo");
			json.remove("tpVeiculo");
			json.remove("tpCarroceria");
			json.remove("nrCodigoEspecie");
			json.remove("dtInformacao");
			json.put("dtInformacao", Util.formatDate(veiculo.getDtInformacao(), "yyyy-MM-dd'T'HH:mm:ss.SSS"));
			
			if(json.has("proprietarios")) {
				JSONArray array = (JSONArray) json.get("proprietarios");
				json.remove("proprietarios");
				if(array.length() > 0) {
					JSONObject p = (JSONObject) array.get(0);
					p.remove("cdVeiculo");
					p.remove("cdProprietario");				
					json.put("proprietario", p);
				}
			}
			
			if(json.has("restricoes")) {
				JSONArray array = (JSONArray) json.get("restricoes");
				if(array.length() > 0) {
					for(int i = 0; i < array.length(); i++) {
						array.getJSONObject(i).remove("cdVeiculo");
						array.getJSONObject(i).remove("cdRestricao");
					}
				}
			}

			if(json.has("debitos")) {
				JSONArray array = (JSONArray) json.get("debitos");
				if(array.length() > 0) {
					for(int i = 0; i < array.length(); i++) {
						array.getJSONObject(i).remove("cdVeiculo");
						array.getJSONObject(i).remove("cdDebito");
					}
				}
			}
						
			return json.toString();
		} catch(Exception e) {
			log.setTpLog(BuscaPlacaServices.TP_LOG_ERRO);
			
			return "{\"code\":-1, \"message\":\"Erro ao consultar placa.\", \"detail\":\""+e.getMessage()+"\"}";
		} finally {
			BuscaPlacaServices.save(log);
		}
	}
	
	@GET
	@Path("/migrate")
	@Produces(MediaType.APPLICATION_JSON)
	public static String doMigration() {
		try {
			return new JSONObject(WebImport.bdvMigrar()).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/auth")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String auth(Usuario usuario) {
		try {
			return new JSONObject(VeiculoServices.auth(usuario, null)).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/alpr")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String alpr() {
		try {
			
			ProcessBuilder builder = new ProcessBuilder("wsl", "alpr", "-j");
			builder.redirectErrorStream(true);
			final Process process = builder.start();

			watch(process);
			
			return "done";
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	private static void watch(final Process process) {
	    new Thread() {
	        public void run() {
	            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            String line = null; 
	            try {
	                while ((line = input.readLine()) != null) {
	                    System.out.println(line);
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }.start();
	}
	
	private static void removeNotTagged() {
		try {
			
			String sPath = "C:/Users/mauri/Projetos/lpr/tagged";
			String dPath = "C:/Users/mauri/Projetos/lpr/ref/img-tag";
			
			File folder = new File(sPath);
			File[] listOfFiles = folder.listFiles();
			
			for (File file : listOfFiles) {
				if(file.getName().contains("-0.yaml")) {
					
					String fileName = file.getName().replace("-0.yaml", "");
					
					copyFile(new File(sPath+"/"+fileName+".jpg"), new File(dPath+"/"+fileName+".jpg"));
					copyFile(new File(sPath+"/"+fileName+"-0.yaml"), new File(dPath+"/"+fileName+"-0.yaml"));
					
					System.out.println("current file: "+fileName);
					Thread.sleep(100);
				}
				
			}
			
			
			
		} catch(Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}
	
	private static void copyFile(File sourceFile, File destFile) throws Exception {
		if (!sourceFile.exists()) {
			throw new Exception("Source file not exists. "+sourceFile.getName());
		}
		if (!destFile.exists()) {
			destFile.createNewFile();
		}
		FileChannel source = null;
		FileChannel destination = null;
		source = new FileInputStream(sourceFile).getChannel();
		destination = new FileOutputStream(destFile).getChannel();
		if (destination != null && source != null) {
			destination.transferFrom(source, 0, source.size());
		}
		if (source != null) {
			source.close();
		}
		if (destination != null) {
			destination.close();
		}

	}
	
	public static void main(String[] args) {
//		alpr();
//		removeNotTagged();
	}

}
