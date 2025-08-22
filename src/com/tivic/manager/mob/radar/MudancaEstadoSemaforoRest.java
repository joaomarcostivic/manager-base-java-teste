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
import com.dataprom.radar.ws.dominio.EstadoSemaforo;
import com.dataprom.radar.ws.semaforo.TransmitirMudancaEstadoSemaforoV1Request;
import com.dataprom.radar.ws.semaforo.TransmitirMudancaEstadoSemaforoV1Request.ItemTransmitirMudancaEstadoSemaforoV1;
import com.dataprom.radar.ws.semaforo.TransmitirMudancaEstadoSemaforoV1Response;
import com.tivic.manager.util.GzipUtil;

@Path("/mudancaestadosemaforo/")
public class MudancaEstadoSemaforoRest {

	@GET
	  @Consumes("application/x-www-form-urlencoded")
	  @Path("/TransmitirMudancaEstadoSemaforoV1/")
	  @Produces(MediaType.TEXT_PLAIN)
	  public String transmitirMudancaEstadoSemaforo(@Context UriInfo ui) {
		  try {
			  	MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		    	return transmitirMudancaEstadoSemaforo(queryParams);
		    } 
		  catch(Exception e) {
		    	System.out.println(e.getMessage());
		    	return null;
		    }
	  }

	  @POST
	  //@Consumes("application/x-www-form-urlencoded")
	  @Path("/TransmitirMudancaEstadoSemaforoV1/")
	  @Produces(MediaType.TEXT_PLAIN)
	  public String transmitirMudancaEstadoSemaforo(final MultivaluedMap<String, String> formParams) {
		  try {
			  	List<ItemTransmitirMudancaEstadoSemaforoV1> list = getMudancaEstadoSemaforoObject(formParams.get("req").get(0));
			  	
			  	processaMudancaEstadoSemaforo(list);
			  	
			  	
			  	TransmitirMudancaEstadoSemaforoV1Response response = new TransmitirMudancaEstadoSemaforoV1Response();
			  	
		  		String encodedResponse = RestMessageBase64.encode(response);
				return encodedResponse;
		    } 
		  	catch(Exception e) {
		    	System.out.println(e.getMessage());
		    	return null;
		    }
	  }
	  
		private List<ItemTransmitirMudancaEstadoSemaforoV1> getMudancaEstadoSemaforoObject(String value) {
			try {
				ObjectInputStream ois = getObjectInputStream(value);
				
				TransmitirMudancaEstadoSemaforoV1Request req = (TransmitirMudancaEstadoSemaforoV1Request) ois.readObject();
				List<ItemTransmitirMudancaEstadoSemaforoV1> list = req.getItems();
				return list;
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	  

		public static void processaMudancaEstadoSemaforo(List<ItemTransmitirMudancaEstadoSemaforoV1> list) {
			
			try {
			
				System.out.println(list.size()+" mudanca de estado de semaforo recebido(s)");
				for (ItemTransmitirMudancaEstadoSemaforoV1 item : list) {

					System.out.println("Desc.:\t"+item.getEquipamento());
					System.out.println("Estado:\t"+EstadoSemaforo.fromByte(item.getEstadoSemaforo()));
					System.out.println("Pista.:\t"+item.getPista());
					System.out.println("Data:\t"+item.getData()+"\n");
				}
			}
			catch(Exception e) {
				e.printStackTrace(System.out);
				System.err.println("Erro! processaMudancaEstadoSemaforo: " + e);
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