package com.tivic.manager.mob.v2.ait;

import java.io.InputStream;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.util.ImagemServices;

import sol.util.RestData;
import sol.util.Result;

@Path("/mob/ait/v2")
public class AitRest {

	private EmissaoAitUseCase emissaoAitUseCase;
	private ObjectMapper objectMapper;
	
	public AitRest() throws Exception {
		objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		emissaoAitUseCase = new EmissaoAitUseCase();
	}
	
	@POST
	@Path("/emitir-ait")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(@FormDataParam("restData") FormDataBodyPart data,
			@FormDataParam("files[]") FormDataBodyPart body) {
		try {
			data.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			RestData restData = data.getValueAs(RestData.class);
			InformacoesVeiculoDTO informacoesVeiculoDTO = InformacoesVeiculoDTOMapper.map(objectMapper.convertValue(restData.getArg("jsonVeiculo"), JSONObject.class));
			ArrayList<AitImagem> imagensAit = getImagens(body);
			InformacoesAitDTO informacoesAitDTO = objectMapper.convertValue(restData.getArg("informacoesAitDto"), InformacoesAitDTO.class);
			Result result = emissaoAitUseCase.execute(informacoesAitDTO, informacoesVeiculoDTO, imagensAit);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private ArrayList<AitImagem> getImagens(FormDataBodyPart body){
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
		return imagensAit;
	}
	

}
