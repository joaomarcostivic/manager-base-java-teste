package com.tivic.manager.mob;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tivic.manager.mob.AgenteServices;

import sol.dao.ResultSetMap;
import sol.util.RequestUtilities;


public class GeoTrackerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  { 
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {  
	    try{

	    	int cdOrgao  = 0;
	    	int cdAgente = 0;
	    	String idOrgao       = RequestUtilities.getParameterAsString(request, "idOrg", "");	    	
	    	String idEquipamento = RequestUtilities.getParameterAsString(request, "idEq", "");
	    	String nrMatriculaAgente = RequestUtilities.getParameterAsString(request, "idAg", "");
	    	double vlLatitude  = RequestUtilities.getParameterAsDouble(request, "lat", 0);
	    	double vlLongitude = RequestUtilities.getParameterAsDouble(request, "lng", 0);
	    	
	    	long time = new Long(RequestUtilities.getParameterAsString(request, "t", "0"));
    	
//	    	
//	    	/*
//	    	 * Tratamento de geoposicionamento para o caso do GPS veicular
//	    	 * 
//	    	 * O formato de recebimento é :
//	    	 * 	160823162847,
//			 *	+5577991105207,
//			 *	GPRMC,
//			 *		142847.000,
//			 *		A,
//			 *		1450.9971,
//			 *		S,
//			 *		04050.1775,
//			 *		W,
//			 *		0.00,
//			 *		258.11,
//			 *		230816,
//			 *		,
//			 *		,
//			 *		A*6F,
//			 *		F,
//			 *	message	battery,
//			 *	imei:013949007303293,
//			 *	08,
//			 *	931.8,
//			 *	F:4.11V,
//			 *	0,
//			 *	149,
//			 *	4952,
//			 *	724,
//			 *	31,
//			 *	0D9A,
//			 *	1F91
//	    	 */
//	    	if(idEquipamento.equals("")) {
//		    	String queryString = request.getQueryString();
//		    	
//		    	String parts[] = queryString.split("GPRMC");
//		    	System.out.println(parts);
//		    	String[] part0Values = parts[0].split(",", -1);
//		    	String[] part1Values = parts[1].split(",", -1);
//		    	
//		    	DateFormat df = new SimpleDateFormat("ddMMyyHHmmss.S");
//		    	Date parsed = df.parse(part1Values[9]+part1Values[1]);
//		    	time = parsed.getTime();
//		    	
//		    	int dlat = new Integer(part1Values[3].substring(0, 2));
//		    	int mlat = new Integer(part1Values[3].substring(2, 4));
//		    	int slat = new Integer(part1Values[3].substring(5));
//		    			    	
//		    	double ddlat = Math.signum(dlat) * (Math.abs(dlat) + (mlat / 60.0) + (slat / 3600.0));
//		    	
//		    	int dlng = new Integer(part1Values[5].substring(0, 3));
//		    	int mlng = new Integer(part1Values[5].substring(3, 5));
//		    	int slng = new Integer(part1Values[5].substring(6));
//		    	
//		    	double ddlng = Math.signum(dlng) * (Math.abs(dlng) + (mlng / 60.0) + (slng / 3600.0));
//		    	
//		    	vlLatitude = ddlat; //new Double(part1Values[3]);
//		    	vlLongitude = ddlng; //new Double(part1Values[5]);
//		    	
//		    	idEquipamento = part0Values[1];
//		    	idOrgao = "teste";		    	
//	    	}	
	    	
	    	GregorianCalendar dtHistorico = new GregorianCalendar();
	    	dtHistorico.setTimeInMillis(time);
	    	GeoTracker tracker = new GeoTracker();
	    	
	    	if (idOrgao.equalsIgnoreCase("GPS")){
	    		ResultSetMap rsmOrgao = OrgaoServices.getOrgaoByEquipamento(idEquipamento);
	    		if(rsmOrgao.next())
	    			idOrgao = rsmOrgao.getString("id_orgao");
	    		
	    		dtHistorico.add(Calendar.HOUR_OF_DAY, -3);
	    	}
	    	if (idOrgao.equalsIgnoreCase("SEMOBPMVC")){
	    		ResultSetMap rsmOrgao = OrgaoServices.getOrgaoByEquipamento(idEquipamento);
	    		if(rsmOrgao.next()){
	    			idOrgao = rsmOrgao.getString("id_orgao");
	    			cdOrgao = rsmOrgao.getInt("cd_orgao");
	    		}
	    		
	    		ResultSetMap rsmAgente = AgenteServices.findByNrMatricula(nrMatriculaAgente.trim());
	    		if(rsmAgente.next())
	    			cdAgente = rsmAgente.getInt("cd_agente");
	    		
	    		//dtHistorico.add(Calendar.HOUR_OF_DAY, -3);
	    		
	    		tracker = new GeoTracker(0, idOrgao, idEquipamento, nrMatriculaAgente, vlLatitude, vlLongitude, dtHistorico, cdOrgao, cdAgente);
	    	}else
	    		tracker = new GeoTracker(0, idOrgao, idEquipamento, nrMatriculaAgente, vlLatitude, vlLongitude, dtHistorico);
	    	
	    	GeoTrackerDAO.insert(tracker);
	    	System.out.println(tracker.toString());
	    } 
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }
}
