package com.tivic.manager.mob.alpr;

import java.io.InputStream;
import java.util.GregorianCalendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ImagemServices;
import com.tivic.manager.util.Util;

import io.swagger.annotations.Api;

@Api(value = "ALPR", tags = {"mob"})
@Path("/v2/mob/alpr")
@Produces(MediaType.APPLICATION_JSON)
public class AlprController {
	
	@POST
	@Path("/openalpr")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response openalpr(@FormDataParam("image") FormDataBodyPart formdata) {
		try {
			AlprService alpr = new OpenALPRServices();
			
			byte[] img = ImagemServices.writeToByteArray(formdata.getEntityAs(InputStream.class)).toByteArray();
			
			AlprResult result = alpr.recognize(img, Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss")+".jpg");
			
			return ResponseFactory.ok(result);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/cateye")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response alpr(@FormDataParam("image") FormDataBodyPart formdata) {
		try {
			AlprService alpr = new CateyeALPRServices();
			
			byte[] img = ImagemServices.writeToByteArray(formdata.getEntityAs(InputStream.class)).toByteArray();
			
			AlprResult result = alpr.recognize(img, Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss")+".jpg");
			
			return ResponseFactory.ok(result);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
