package com.tivic.manager.mob;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import org.json.JSONObject;

import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.tivic.manager.agd.AgendaItemServices;
import com.tivic.sol.auth.jwt.JWTIgnore;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.str.ALPRServlet;
import com.tivic.manager.util.ImagemServices;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.RestData;
import sol.util.Result;

@Path("/mob/eventoequipamento/")

public class EventoEquipamentoRest {
	
	@Context protected HttpServletRequest request;
	
	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(EventoEquipamento eventoEquipamento) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Result result = EventoEquipamentoServices.save(eventoEquipamento);
			return objectMapper.writeValueAsString(result);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@DELETE
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(EventoEquipamento eventoEquipamento) {
		try {
			Result result = EventoEquipamentoServices.remove(eventoEquipamento);
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
			ResultSetMap rsm = EventoEquipamentoServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/importEventos")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public static String importEventos(@FormDataParam("restData") FormDataBodyPart data, @FormDataParam("files[]") FormDataBodyPart body) {
		Connection conexao = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//			objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));

			data.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			RestData restData = data.getValueAs(RestData.class);

			EventoEquipamento evento = objectMapper.convertValue(restData.getArg("evento"), EventoEquipamento.class);
									
			ArrayList<Arquivo> arquivos = new ArrayList<Arquivo>();			
			for (BodyPart part : body.getParent().getBodyParts()) {
				InputStream is = part.getEntityAs(InputStream.class);
				ContentDisposition meta = part.getContentDisposition();
				Arquivo arquivo = new Arquivo();

				if (meta.getFileName() != null) {
					arquivo.setNmArquivo(meta.getFileName());
					arquivo.setBlbArquivo(ImagemServices.writeToByteArray(is).toByteArray());
					arquivo.setDtCriacao(new GregorianCalendar());
					arquivos.add(arquivo);
				}

			}
			
			return objectMapper.writeValueAsString(EventoEquipamentoServices.saveCameraEventos(evento, arquivos, null));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (conexao != null)
				Conexao.desconectar(conexao);
		}
	}

	@POST
	@Path("/find")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String find(ArrayList<ItemComparator> criterios) {
		try {			
			ResultSetMap rsm = EventoEquipamentoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@GET
	@Path("/file/{cdArquivo}{blur:(/blur/[^/]+?)?}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({"image/jpg"})
	@JWTIgnore
	public static Response file(@PathParam("cdArquivo") int cdArquivo, @PathParam("blur") String blur, @QueryParam("agente") int cdAgente) {
		try {			

			boolean lgOpenCV = Util.getConfManager().getProps().getProperty("INIT_OPENCV_LIB").equals("1");
			
	    	//TODO
			Arquivo arquivo = ArquivoDAO.get(cdArquivo);		
			
			if(lgOpenCV) {
				ByteArrayOutputStream im = new ByteArrayOutputStream();
				ImageIO.write(EventoArquivoServices.removerPlacas(arquivo.getBlbArquivo(), blur), "jpg", im);
	
				BufferedImage bi = EventoArquivoServices.removerRostos(im.toByteArray());			
				ImageIO.write(bi, "jpg", im);
				arquivo.setBlbArquivo(im.toByteArray());
			}			

			if(cdAgente > 0) {
				File file = Util.createTmpFile("ait_tmp_image_" + cdAgente, ".jpg");
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(arquivo.getBlbArquivo());
				fos.close();
				
				return Response.ok(Files.readAllBytes(file.toPath())).cookie(new NewCookie("tmp_image", file.getName())).build();
			}
			
			if(arquivo == null || arquivo.getBlbArquivo() == null) {
				return Response.status(404).build();
			}
			
			return Response.ok(arquivo.getBlbArquivo()).build();
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return Response.status(Response.Status.CONFLICT).build();
	    }
	}

	
	@GET
	@Path("/alpr/file/{cdArquivo}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response alprFile(@PathParam("cdArquivo") int cdArquivo) {
		try {
			try {
    			Arquivo arquivo = ArquivoDAO.get(cdArquivo);
    			
    			Result result = ALPRServlet.recognizeImage(arquivo.getBlbArquivo());
    			
    		     return Response.ok(result).build();
		    } catch (Exception e) {
		        return Response.status(Response.Status.CONFLICT).build();
		    }
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	/*
	 * STATS
	 */
	
	@GET
	@Path("/stats/infracao/comprimentoveiculo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String statsQtInfracaoComprimentoVeiculo() {
		try {
			return Util.rsmToJSON(EventoEquipamentoServices.statsQtInfracaoComprimentoVeiculo());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/stats/infracao/comprimentoveiculo/{idOrgao}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String statsQtInfracaoComprimentoVeiculo(@PathParam("idOrgao") String idOrgao) {
		try {
			return Util.rsmToJSON(EventoEquipamentoServices.statsQtInfracaoComprimentoVeiculo(idOrgao));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/stats/infracao/velocidade")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String statsQtInfracaoVelocidade() {
		try {
			return Util.rsmToJSON(EventoEquipamentoServices.statsQtInfracaoVelocidade());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/stats/infracao/velocidade/{idOrgao}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String statsQtInfracaoVelocidade(@PathParam("idOrgao") String idOrgao) {
		try {
			return Util.rsmToJSON(EventoEquipamentoServices.statsQtInfracaoVelocidade(idOrgao));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
}
