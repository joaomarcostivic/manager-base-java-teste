package com.tivic.manager.mob.radar;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.dataprom.dprest.util.RestMessageBase64;
import com.dataprom.radar.ws.alerta.TransmitirAlertaV1Request;
import com.dataprom.radar.ws.alerta.TransmitirAlertaV1Request.ItemTransmitirAlertaV1;
import com.dataprom.radar.ws.alerta.TransmitirAlertaV1Response;
import com.dataprom.radar.ws.dominio.TipoAlerta;
import com.tivic.manager.util.GzipUtil;


@Path("/alerta/")
public class AlertaRest {

  @GET
  @Consumes("application/x-www-form-urlencoded")
  @Path("/TransmitirAlertaV1/")
  @Produces(MediaType.TEXT_PLAIN)
  public String transmitirAlerta(@Context UriInfo ui) {
	  try {
		  	MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
	    	return transmitirAlerta(queryParams);
	    } 
	  catch(Exception e) {
	    	System.out.println(e.getMessage());
	    	return null;
	    }
  }

  @POST
  //@Consumes("application/x-www-form-urlencoded")
  @Path("/TransmitirAlertaV1/")
  @Produces(MediaType.TEXT_PLAIN)
  public String transmitirAlerta(final MultivaluedMap<String, String> formParams) {
	  try {
		  	List<ItemTransmitirAlertaV1> list = getAlertaObject(formParams.get("req").get(0));
		  	
		  	processaAlerta(list);
		  	
		  	
		  	TransmitirAlertaV1Response response = new TransmitirAlertaV1Response();
		  	
	  		String encodedResponse = RestMessageBase64.encode(response);
			return encodedResponse;
	    } 
	  	catch(Exception e) {
	    	System.out.println(e.getMessage());
	    	return null;
	    }
  }
  
	private List<ItemTransmitirAlertaV1> getAlertaObject(String value) {
		try {
			ObjectInputStream ois = getObjectInputStream(value);
			
			TransmitirAlertaV1Request req = (TransmitirAlertaV1Request) ois.readObject();
			List<ItemTransmitirAlertaV1> list = req.getItems();
			return list;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
  

	public static void processaAlerta(List<ItemTransmitirAlertaV1> list) {
		
		
		try {
		
			System.out.println(list.size()+" alerta(s) recebido(s)");
			for (ItemTransmitirAlertaV1 item : list) {
				System.out.println("Tipo.:\t"+TipoAlerta.fromByte(item.getCodigoTipo()));
				System.out.println("Desc.:\t"+item.getDescricao());
				System.out.println("Equip.:\t"+item.getEquipamento());
				System.out.println("Data:\t"+item.getData()+"\n");
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! processaAlerta: " + e);
		}
	}
  	
  	
	
	private ObjectInputStream getObjectInputStream(String value) {
		try {
			byte[] objectBytes = decode(value);

			//System.out.println(objectBytes);
			ByteArrayInputStream baos = new ByteArrayInputStream(objectBytes);  
			ObjectInputStream ois = new ObjectInputStream(baos);
			return ois;
		}
      catch (Exception e) {
      	e.printStackTrace();
      	return null;
      }
	}

	private byte[] decode(String msg){
      try {
          byte[] data = Base64.getDecoder().decode(msg);
          byte[] result = GzipUtil.decompress(data);
          
          return result;
          
      } 
      catch (IOException e) {
      	e.printStackTrace();
      	return null;
      }

  }
  
	
  
  
}