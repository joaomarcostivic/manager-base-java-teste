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
import com.dataprom.radar.ws.infracao.TransmitirInfracaoASVV1Request;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoASVV1Request.ItemTransmitirInfracaoASVV1;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoASVV1Response;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoESTV1Request;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoESTV1Request.ItemTransmitirInfracaoESTV1;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoESTV1Response;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoIMTV1Request;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoIMTV1Request.ItemTransmitirInfracaoIMTV1;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoIMTV1Response;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoPSFV1Request;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoPSFV1Request.ItemTransmitirInfracaoPSFV1;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoPSFV1Response;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoVALV1Request;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoVALV1Request.ItemTransmitirInfracaoVALV1;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoVALV1Response;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.mob.EventoEquipamentoServices;
import com.tivic.manager.mob.MedicaoTrafegoServices;
import com.tivic.manager.util.GzipUtil;
import com.tivic.manager.util.Util;

import sol.util.Result;

@Path("")
public class InfracaoRest {

  @GET
  @Consumes("application/x-www-form-urlencoded")
  @Path("/TransmitirInfracaoIMTV1/")
  @Produces(MediaType.TEXT_PLAIN)
  public String transmitirInfracaoIMT(@Context UriInfo ui) {
	  try {
		  	MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
	    	return transmitirInfracaoIMT(queryParams);
	    } 
	  catch(Exception e) {
		  e.printStackTrace(System.out);
	    	System.out.println(e.getMessage());
	    	return null;
	    }
  }

  @POST
  //@Consumes("application/x-www-form-urlencoded")
  @Path("/TransmitirInfracaoIMTV1/")
  @Produces(MediaType.TEXT_PLAIN)
  public String transmitirInfracaoIMT(final MultivaluedMap<String, String> formParams) {
	  try {
		  	List<ItemTransmitirInfracaoIMTV1> list = getInfracaoIMTObject(formParams.get("req").get(0));
		  	
		  	Result r = EventoEquipamentoServices.saveIMT(list);
		  	
		  	
		  	TransmitirInfracaoIMTV1Response response = null;
			
		  	if(r.getCode()>0) {
		  		response = new TransmitirInfracaoIMTV1Response();
		  	}

	  		String encodedResponse = RestMessageBase64.encode(response);
			return encodedResponse;
	    } 
	  catch(Exception e) {
	    	System.out.println(e.getMessage());
		  e.printStackTrace(System.out);
    	return null;
    }
  }
  
  @GET
  @Consumes("application/x-www-form-urlencoded")
  @Path("/TransmitirInfracaoVALV1/")
  @Produces(MediaType.TEXT_PLAIN)
  public String transmitirInfracaoVAL(@Context UriInfo ui) {
	  try {
		  	MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
	    	return transmitirInfracaoVAL(queryParams);
	    } 
	  catch(Exception e) {
		  e.printStackTrace(System.out);
	    	System.out.println(e.getMessage());
	    	return null;
	    }
  }
  
  @POST
  @Consumes("application/x-www-form-urlencoded")
  @Path("/TransmitirInfracaoVALV1/")
  @Produces(MediaType.TEXT_PLAIN)
  public String transmitirInfracaoVAL(final MultivaluedMap<String, String> formParams) {
    try {
    	List<ItemTransmitirInfracaoVALV1> list = getInfracaoVALObject(formParams.get("req").get(0));
    	
	  	Result r = EventoEquipamentoServices.saveVAL(list);

		TransmitirInfracaoVALV1Response response = null;
		
	  	if(r.getCode()>0) {
	  		response = new TransmitirInfracaoVALV1Response();
	  	}

  		String encodedResponse = RestMessageBase64.encode(response);
		return encodedResponse;
    } 
    catch(Exception e) {
		  e.printStackTrace(System.out);
    	System.out.println(e.getMessage());
    	return null;
    }
  }
  
  @GET
  @Consumes("application/x-www-form-urlencoded")
  @Path("/TransmitirInfracaoASVV1/")
  @Produces(MediaType.TEXT_PLAIN)
  public String transmitirInfracaoASV(@Context UriInfo ui) {
	  try {
		  	MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
	    	return transmitirInfracaoASV(queryParams);
	    } 
	  catch(Exception e) {
		  e.printStackTrace(System.out);
	    	System.out.println(e.getMessage());
	    	return null;
	    }
  }
  
  @POST
  @Consumes("application/x-www-form-urlencoded")
  @Path("/TransmitirInfracaoASVV1/")
  @Produces(MediaType.TEXT_PLAIN)
  public String transmitirInfracaoASV(final MultivaluedMap<String, String> formParams) {
    try {
    	List<ItemTransmitirInfracaoASVV1> list = getInfracaoASVObject(formParams.get("req").get(0));
	  	
    	
	  	Result r = EventoEquipamentoServices.saveASV(list);

	  	TransmitirInfracaoASVV1Response response = null;
		
	  	if(r.getCode()>0) {
	  		response = new TransmitirInfracaoASVV1Response();
	  	}

  		String encodedResponse = RestMessageBase64.encode(response);
  		
		return encodedResponse;
    } 
    catch(Exception e) {
		  e.printStackTrace(System.out);
    	System.out.println(e.getMessage());
    	return null;
    }
  }
  
  @GET
  @Consumes("application/x-www-form-urlencoded")
  @Path("/TransmitirInfracaoPSFV1/")
  @Produces(MediaType.TEXT_PLAIN)
  public String transmitirInfracaoPSF(@Context UriInfo ui) {
	  try {
		  	MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
	    	return transmitirInfracaoPSF(queryParams);
	    } 
	  catch(Exception e) {
		  e.printStackTrace(System.out);
	    	System.out.println(e.getMessage());
	    	return null;
	    }
  }
  
  @POST
  @Consumes("application/x-www-form-urlencoded")
  @Path("/TransmitirInfracaoPSFV1/")
  @Produces(MediaType.TEXT_PLAIN)
  public String transmitirInfracaoPSF(final MultivaluedMap<String, String> formParams) {
    try {
    	List<ItemTransmitirInfracaoPSFV1> list = getInfracaoPSFObject(formParams.get("req").get(0));
	  	
    	
	  	Result r = EventoEquipamentoServices.savePSF(list);

	  	TransmitirInfracaoPSFV1Response response = null;
		
	  	if(r.getCode()>0) {
	  		response = new TransmitirInfracaoPSFV1Response();
	  	}

  		String encodedResponse = RestMessageBase64.encode(response);
  		
		return encodedResponse;
    } 
    catch(Exception e) {
		  e.printStackTrace(System.out);
    	System.out.println(e.getMessage());
    	return null;
    }
  }

  @GET
  @Consumes("application/x-www-form-urlencoded")
  @Path("/TransmitirInfracaoESTV1/")
  @Produces(MediaType.TEXT_PLAIN)
  public String transmitirInfracaoEST(@Context UriInfo ui) {
	  try {
		  	MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
	    	return transmitirInfracaoEST(queryParams);
	    } 
	  catch(Exception e) {
		  e.printStackTrace(System.out);
	    	System.out.println(e.getMessage());
	    	return null;
	    }
  }
  
  @POST
  @Consumes("application/x-www-form-urlencoded")
  @Path("/TransmitirInfracaoESTV1/")
  @Produces(MediaType.TEXT_PLAIN)
  public String transmitirInfracaoEST(final MultivaluedMap<String, String> formParams) {
    try {
    	List<ItemTransmitirInfracaoESTV1> list = getInfracaoESTObject(formParams.get("req").get(0));

    	registrarMedicaoTrafego(list);
    	
    	Result r = MedicaoTrafegoServices.processarLista(list, null);
    	//Result r = EventoEquipamentoServices.saveEST(list);
    	
    	//TODO: salvar medição de tráfego    	
    	
    	//Result r = new Result(1);
	  	TransmitirInfracaoESTV1Response response = null;
		
	  	if(r.getCode()>0) {
	  		response = new TransmitirInfracaoESTV1Response();
	  	}

  		String encodedResponse = RestMessageBase64.encode(response);
		return encodedResponse;
    } 
    catch(Exception e) {
		  e.printStackTrace(System.out);
    	System.out.println(e.getMessage());
    	return null;
    }
  }
	  
	private void registrarMedicaoTrafego(List<ItemTransmitirInfracaoESTV1> list) {
		
		if(!TrafegoMonitor.started())
			TrafegoMonitor.start(10); //medicoes a cada 10 minutos
		
		for (ItemTransmitirInfracaoESTV1 item : list) {
			
			//1. BUSCAR EQUIPAMENTO
			Equipamento equipamento = EquipamentoServices.getByIdEquipamento(item.getEquipamento());

			//2. REGISTRAR MEDICAO
			MedicaoTrafego medicao = new MedicaoTrafego();
			
			medicao.setDtInicialMedicao(Util.convDateToCalendar(item.getDataConclusao()));
			medicao.setDtFinalMedicao(Util.convDateToCalendar(item.getDataConclusao()));
			medicao.setIdEquipamento(equipamento.getIdEquipamento());
			medicao.setNmVia(equipamento.getDsLocal());
			medicao.setNrPista(item.getPista());
			medicao.setQtVeiculos(1);
			medicao.setTpVeiculo(item.getTipoVeiculo());
			
			TrafegoMonitor.addMedicaoTrafego(medicao);
			
		}
	}

//  @GET
//  @Path("/decryptAll/")
//  @Produces(MediaType.TEXT_PLAIN)
//  public String decryptAll() {
//	  try {
//		  	Result r = EventoArquivoServices.decryptAll();
//	    	return r.getMessage();
//	    } 
//	  catch(Exception e) {
//	    	System.out.println(e.getMessage());
//	    	return null;
//	    }
//  }
  
  
  	private List<ItemTransmitirInfracaoIMTV1> getInfracaoIMTObject(String value) {
		try {
			ObjectInputStream ois = getObjectInputStream(value);
			
			TransmitirInfracaoIMTV1Request req = (TransmitirInfracaoIMTV1Request) ois.readObject();
			List<ItemTransmitirInfracaoIMTV1> list = req.getItems();
			return list;
		}
		catch (Exception e) {
			  e.printStackTrace(System.out);
			e.printStackTrace();
			return null;
		}
	}
  	
  	private List<ItemTransmitirInfracaoVALV1> getInfracaoVALObject(String value) {
		try {
			ObjectInputStream ois = getObjectInputStream(value);
			
			TransmitirInfracaoVALV1Request req = (TransmitirInfracaoVALV1Request) ois.readObject();
			List<ItemTransmitirInfracaoVALV1> list = req.getItems();
			return list;
		}
		catch (Exception e) {
			  e.printStackTrace(System.out);
			e.printStackTrace();
			return null;
		}
	}
  	
  	private List<ItemTransmitirInfracaoASVV1> getInfracaoASVObject(String value) {
		try {
			ObjectInputStream ois = getObjectInputStream(value);
			
			TransmitirInfracaoASVV1Request req = (TransmitirInfracaoASVV1Request) ois.readObject();
			List<ItemTransmitirInfracaoASVV1> list = req.getItems();
			return list;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
  	
  	private List<ItemTransmitirInfracaoPSFV1> getInfracaoPSFObject(String value) {
		try {
			ObjectInputStream ois = getObjectInputStream(value);
			
			TransmitirInfracaoPSFV1Request req = (TransmitirInfracaoPSFV1Request) ois.readObject();
			List<ItemTransmitirInfracaoPSFV1> list = req.getItems();
			return list;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
  	
	private List<ItemTransmitirInfracaoESTV1> getInfracaoESTObject(String value) {
		try {
			ObjectInputStream ois = getObjectInputStream(value);
			
			TransmitirInfracaoESTV1Request req = (TransmitirInfracaoESTV1Request) ois.readObject();
			List<ItemTransmitirInfracaoESTV1> list = req.getItems();
			return list;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
  
	
	private ObjectInputStream getObjectInputStream(String value) {
		try {
			byte[] objectBytes = decode(value);

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