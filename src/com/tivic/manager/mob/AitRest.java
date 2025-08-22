package com.tivic.manager.mob;

import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.ImagemServices;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.RestData;
import sol.util.Result;

@Path("/mob/ait/")

public class AitRest {
	
	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(RestData args) {
		try {
			ObjectMapper objectMapper   = new ObjectMapper();
			
			Ait ait 						= objectMapper.convertValue(args.getArg("ait"), Ait.class);
			AitCondutor aitCondutor 		= objectMapper.convertValue(args.getArg("aitCondutor"), AitCondutor.class);
			AitVeiculo aitVeiculo		    = objectMapper.convertValue(args.getArg("aitVeiculo"), AitVeiculo.class);
			AitProprietario aitProprietario = objectMapper.convertValue(args.getArg("aitProprietario"), AitProprietario.class);
			AuthData authData 				= objectMapper.convertValue(args.getArg("authData"), AuthData.class);
			
			Result result = AitServices.save(ait, aitCondutor, aitVeiculo, aitProprietario, authData);
			
			return new JSONObject(result).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return new JSONObject(new Result(-6, "Erro ao salvar!")).toString();
		}
	}

	@DELETE
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(Ait ait) {
		try {
			Result result = AitServices.remove(ait);
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
			ResultSetMap rsm = AitServices.getAll();
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
			ResultSetMap rsm = AitServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/emitir-ait")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(@FormDataParam("restData") FormDataBodyPart data,
			@FormDataParam("files[]") FormDataBodyPart body) {
		Connection conn = null;
		try {
			ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

			data.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			RestData restData = data.getValueAs(RestData.class);
		
			int cdCidade = 0;
			
			JSONObject jsonVeiculo = objectMapper.convertValue(restData.getArg("jsonVeiculo"), JSONObject.class); //new JSONObject(restData.getArg("regVeiculo"));
			if(jsonVeiculo != null) {
				if(jsonVeiculo.has("nmMunicipio") && jsonVeiculo.get("nmMunicipio") != null 
				&& jsonVeiculo.has("sgEstado") && jsonVeiculo.get("sgEstado") != null
				&& jsonVeiculo.has("codMunicipio") && jsonVeiculo.get("codMunicipio") != null) {				
					cdCidade = AitServices.buscarCdMunicipioVeiculo(jsonVeiculo.getString("codMunicipio"), jsonVeiculo.getString("nmMunicipio"), jsonVeiculo.getString("sgEstado"));
				}
			}
			
			
			Ait ait = objectMapper.convertValue(restData.getArg("ait"), AitDtInfracaoDeserialize.class);
			ait.setDsLocalInfracao(Util.limparAcentos(ait.getDsLocalInfracao()));
			ait.setDsPontoReferencia(Util.limparAcentos(ait.getDsPontoReferencia()));
			ait.setDsObservacao(Util.limparAcentos(ait.getDsObservacao()));
			ait.setTpCnhCondutor(TipoCnhEnum.NAO_INFORMADO.getKey());
			ait.setCdCidade(cdCidade);
			
			EventoEquipamento evento = objectMapper.convertValue(restData.getArg("evento"), EventoEquipamento.class);			
			AitProprietario aitProprietario = objectMapper.convertValue(restData.getArg("aitProprietario"), AitProprietario.class);
			AitVeiculo aitVeiculo = objectMapper.convertValue(restData.getArg("aitVeiculo"), AitVeiculo.class);

			ArrayList<AitImagem> imagensAit = new ArrayList<AitImagem>();
			
			for (int i = 0; i < body.getParent().getBodyParts().size(); i++) {
				
				org.glassfish.jersey.media.multipart.BodyPart part = body.getParent().getBodyParts().get(i);
				InputStream is = part.getEntityAs(InputStream.class);
				org.glassfish.jersey.media.multipart.ContentDisposition meta = part.getContentDisposition();
				AitImagem imagemAit = new AitImagem();

				if (meta.getFileName() != null) {
					byte[] arquivo = ImagemServices.writeToByteArray(is).toByteArray();
					imagemAit.setBlbImagem(arquivo);
					imagensAit.add(imagemAit);
				}

			}
			
			imagensAit.get(0).setLgImpressao(1);

			Result result = AitServices.emitirAit(ait, evento, imagensAit, aitProprietario, aitVeiculo, null);

			return new JSONObject(result).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(conn != null) {
				Conexao.desconectar(conn);
			}
		}
	}

	@POST
	@Path("/get/observacoes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String observacoes(int codInfracao) {
		try {
			ResultSetMap rsm = AitServices.getObservacoesInfracao(codInfracao);
			return Util.rsmToJSON(rsm);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/confirmar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String confirmar(Ait ait) {
		try {
			
			int r = AitServices.mudarSituacaoAit(ait.getCdAit(), AitServices.ST_CONFIRMADO);
			Result result = new Result(r,
					r <= 0 ? "Não foi possível alterar a situação do AIT" : "AIT confirmado com sucesso!");

			return new JSONObject(result).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/cancelar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String cancelar(Ait ait) {
		try {

			int r = AitServices.mudarSituacaoAit(ait.getCdAit(), AitServices.ST_CANCELADO);
			Result result = new Result(r,
					r <= 0 ? "Não foi possível alterar a situação do AIT" : "AIT cancelada com sucesso!");

			return new JSONObject(result).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/validar/{nrAit}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String validate(@PathParam("nrAit") String nrAit) {
		try {			
			return new JSONObject(AitServices.validate(nrAit)).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/stats/ait/tipo/{inicio}/{fim}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String statsAitTipo(@PathParam("inicio") String inicio, @PathParam("fim") String fim) {
		try {
			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio.replaceAll("-", "/") +" 00:00:00");
			GregorianCalendar dtFinal   = Util.convStringToCalendar(fim.replaceAll("-", "/")    +" 23:59:59");
			return Util.rsmToJSON(AitBIServices.statsAitTipo(dtInicial, dtFinal, 5));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/stats/ait/agente/{inicio}/{fim}/{limit}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String statsAitAgente(@PathParam("inicio") String inicio, @PathParam("fim") String fim, @PathParam("limit") int limit) {
		try {
			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio.replaceAll("-", "/") +" 00:00:00");
			GregorianCalendar dtFinal   = Util.convStringToCalendar(fim.replaceAll("-", "/")    +" 23:59:59");
			return Util.rsmToJSON(AitBIServices.statsAitAgente(dtInicial, dtFinal, limit));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/stats/ait/{inicio}/{fim}/{tpEquipamento}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String statsAitTipo(@PathParam("inicio") String inicio, @PathParam("fim") String fim, @PathParam("tpEquipamento") int tpEquipamento) {
		try {
			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio.replaceAll("-", "/") +" 00:00:00");
			GregorianCalendar dtFinal   = Util.convStringToCalendar(fim.replaceAll("-", "/")    +" 23:59:59");
			return Util.rsmToJSON(AitBIServices.statsAitEquipamento(tpEquipamento, dtInicial, dtFinal, 10));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/stats/ait/equipamento/{inicio}/{fim}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String statsAitEquipamento(@PathParam("inicio") String inicio, @PathParam("fim") String fim) {
		try {
			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio.replaceAll("-", "/") +" 00:00:00");
			GregorianCalendar dtFinal   = Util.convStringToCalendar(fim.replaceAll("-", "/")    +" 23:59:59");
			return Util.rsmToJSON(AitBIServices.statsAitEquipamentoAgrupado(dtInicial, dtFinal));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/stats/ait/equipamento/periodo/{inicio}/{fim}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String statsAitEquipamentoPeriodo(@PathParam("inicio") String inicio, @PathParam("fim") String fim) {
		try {
			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio.replaceAll("-", "/") +" 00:00:00");
			GregorianCalendar dtFinal   = Util.convStringToCalendar(fim.replaceAll("-", "/")    +" 23:59:59");
			return Util.rsmToJSON(AitBIServices.statsAitEquipamentoAgrupadoPeriodo(dtInicial, dtFinal));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/stats/ait/natureza/{inicio}/{fim}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String statsAitNatureza(@PathParam("inicio") String inicio, @PathParam("fim") String fim) {
		try {
			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio.replaceAll("-", "/") +" 00:00:00");
			GregorianCalendar dtFinal   = Util.convStringToCalendar(fim.replaceAll("-", "/")    +" 23:59:59");
			return Util.rsmToJSON(AitBIServices.statsAitNatureza(dtInicial, dtFinal));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/stats/ait/valor/{inicio}/{fim}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String statsAitValor(@PathParam("inicio") String inicio, @PathParam("fim") String fim) {
		try {
			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio.replaceAll("-", "/") +" 00:00:00");
			GregorianCalendar dtFinal   = Util.convStringToCalendar(fim.replaceAll("-", "/")    +" 23:59:59");
			return new JSONObject(AitBIServices.statsAitValor(dtInicial, dtFinal)).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/stats/ait/valor/mensal/{inicio}/{fim}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String statsAitValorMensal(@PathParam("inicio") String inicio, @PathParam("fim") String fim) {
		try {
			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio.replaceAll("-", "/") +" 00:00:00");
			GregorianCalendar dtFinal   = Util.convStringToCalendar(fim.replaceAll("-", "/")    +" 23:59:59");
			return Util.rsmToJSON(AitBIServices.statsAitValorMensal(dtInicial, dtFinal));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}


}
