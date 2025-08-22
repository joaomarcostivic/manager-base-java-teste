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
import com.dataprom.radar.ws.dominio.TipoStatus;
import com.dataprom.radar.ws.status.ItemTransmitirDetalheStatusV1;
import com.dataprom.radar.ws.status.TransmitirStatusV1Request;
import com.dataprom.radar.ws.status.TransmitirStatusV1Request.ItemTransmitirStatusV1;
import com.dataprom.radar.ws.status.TransmitirStatusV1Response;
import com.tivic.manager.util.GzipUtil;


@Path("/status/")
public class StatusRest {

  @GET
  @Consumes("application/x-www-form-urlencoded")
  @Path("/TransmitirStatusV1/")
  @Produces(MediaType.TEXT_PLAIN)
  public String transmitirStatus(@Context UriInfo ui) {
	  try {
		  	MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
	    	return transmitirStatus(queryParams);
	    } 
	  catch(Exception e) {
	    	System.out.println(e.getMessage());
	    	return null;
	    }
  }

  @POST
  //@Consumes("application/x-www-form-urlencoded")
  @Path("/TransmitirStatusV1/")
  @Produces(MediaType.TEXT_PLAIN)
  public String transmitirStatus(final MultivaluedMap<String, String> formParams) {
	  try {
		  	List<ItemTransmitirStatusV1> list = getStatusObject(formParams.get("req").get(0));
		  	
		  	processaStatus(list);
		  	TransmitirStatusV1Response response = new TransmitirStatusV1Response();
		  	
	  		String encodedResponse = RestMessageBase64.encode(response);
			return encodedResponse;
	    } 
	  	catch(Exception e) {
	    	System.out.println(e.getMessage());
	    	return null;
	    }
  }
  
	private List<ItemTransmitirStatusV1> getStatusObject(String value) {
		try {
			ObjectInputStream ois = getObjectInputStream(value);
			
			TransmitirStatusV1Request req = (TransmitirStatusV1Request) ois.readObject();
			List<ItemTransmitirStatusV1> list = req.getItems();
			return list;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
  

	public static void processaStatus(List<ItemTransmitirStatusV1> list) {
		
		
		try {
		
			System.out.println(list.size()+" status(s) recebido(s)");
			for (ItemTransmitirStatusV1 item : list) {
				
				System.out.println("Item de Status");
				
				
				System.out.println("Equip.:\t"+item.getEquipamento());
				System.out.println("Data:\t"+item.getData());
				System.out.println("Det.:\t"+item.getData());
				for (ItemTransmitirDetalheStatusV1 detItem : item.getDetalhes()) {
					System.out.println("Tipo.:\t"+TipoStatus.fromByte(detItem.getTipoStatus()));
					System.out.println("\tDesc.:\t"+detItem.getDescricao()+"\n");
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! processaStatus: " + e);
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