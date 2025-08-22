package com.tivic.manager.str;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.Util;
import sol.util.RequestUtilities;


public class AitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  { 
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {  
	    try{
	    	String nrPlaca = RequestUtilities.getParameterAsString(request, "p", "");
	    	int nrRenavam = RequestUtilities.getParameterAsInteger(request, "r", 0);
	    	
	    	response.setContentType("application/json");
	    	ServletOutputStream ouputStream = response.getOutputStream();  
            
	    	
	    	if((nrPlaca==null || nrPlaca.equals("")) && nrRenavam==0) {
	    		ouputStream.println("{\n code: -1,\n message: 'Numero da placa ou Renavam não informados.'\n}");	
	    	}
	    	else {
	    		
	    		Connection connect = Conexao.conectar();
	    		
	    		ResultSetMap rsm = AitServices.getAitsByNrPlacaRenavan(nrPlaca, nrRenavam, connect);
	    		
	    		if(rsm.size()==0)
		    		ouputStream.println("{\n code: 0,\n message: 'Nenhum AIT encontrado para a placa indicada.'\n}");
	    		else {
		    		ouputStream.println("{\n code: 1,\n message: 'AIT(s) encontrado(s).',\n aits:\n [");
		    		String aits = "";
		    		while(rsm.next()){
		    			if(rsm.getPosition()!=0)
		    				aits+=",\n  ";
		    			aits+="  {\n   "
	    					+ "nrAit: '"+		(rsm.getString("NR_AIT")!=null ? rsm.getString("NR_AIT") : "" )+"',\n   "
	    					+ "nrPlaca: '"+	(rsm.getString("NR_PLACA")!=null ? rsm.getString("NR_PLACA") : "" )+"',\n   "
	    					+ "codRenavan: '"+	(rsm.getString("CD_RENAVAN")!=null ? rsm.getString("CD_RENAVAN") : "" )+"',\n   "
	    					+ "nmCondutor: '"+	(rsm.getString("NM_CONDUTOR")!=null ? rsm.getString("NM_CONDUTOR") : "" )+"',\n   "
	    					+ "dtInfracao: '"+	(rsm.getString("DT_INFRACAO")!=null ? Util.convCalendarString(rsm.getGregorianCalendar("DT_INFRACAO")) : "" )+"',\n   "
	    					+ "dsInfracao: '"+	(rsm.getString("DS_INFRACAO2")!=null ? rsm.getString("DS_INFRACAO2").trim() : "" )+"',\n   "
	    					+ "nrPontuacao: '"+	(rsm.getString("NR_PONTUACAO")!=null ? rsm.getString("NR_PONTUACAO") : "" )+"',\n   "
	    					+ "nrArtigo: '"+		(rsm.getString("NR_ARTIGO")!=null ? rsm.getString("NR_ARTIGO") : "" )+"',\n   "
	    					+ "nrParagrafo: '"+	(rsm.getString("NR_PARAGRAFO")!=null ? rsm.getString("NR_PARAGRAFO") : "" )+"',\n   "
	    					+ "nrInciso: '"+		(rsm.getString("NR_INCISO")!=null ? rsm.getString("NR_INCISO") : "" )+"',\n   "
	    					+ "nrAlinea: '"+		(rsm.getString("NR_ALINEA")!=null ? rsm.getString("NR_ALINEA") : "" )+"',\n   "
	    					
	    					+ "nmCor: '"+		(rsm.getString("NM_COR")!=null ? rsm.getString("NM_COR") : "" )+"',\n   "
	    					+ "nmMarca: '"+		(rsm.getString("NM_MARCA")!=null ? rsm.getString("NM_MARCA") : "" )+"',\n   "
	    					+ "nmModelo: '"+		(rsm.getString("NM_MODELO")!=null ? rsm.getString("NM_MODELO") : "" )+"',\n   "
	    					+ "dsAnoFabricacao: '"+		(rsm.getString("DS_ANO_FABRICACAO")!=null ? rsm.getString("DS_ANO_FABRICACAO") : "" )+"',\n   "
	    					+ "dsAnoModelo: '"+		(rsm.getString("DS_ANO_MODELO")!=null ? rsm.getString("DS_ANO_MODELO") : "" )+"',\n   "
	    					+ "nmProprietario: '"+		(rsm.getString("NM_PROPRIETARIO")!=null ? rsm.getString("NM_PROPRIETARIO") : "" )+"',\n   ";
	    					
	    					ResultSetMap rsmMovimentos = AitServices.getMovimentosByCdAit(rsm.getInt("CODIGO_AIT"), connect);
		    			
			    			String movimentos = "movimentos:\n    [";
				    		while(rsmMovimentos.next()){
				    			if(rsmMovimentos.getPosition()!=0)
				    				movimentos+=",\n  ";
				    			
				    			movimentos+="    {\n    "
				    					+ "nrMovimento: '"+		(rsmMovimentos.getString("NR_MOVIMENTO")!=null ? rsmMovimentos.getString("NR_MOVIMENTO") : "" )+"',\n     "
				    					+ "dtMovimento: '"+	(rsm.getString("DT_MOVIMENTO")!=null ? Util.convCalendarString(rsm.getGregorianCalendar("DT_MOVIMENTO")) : "" )+"',\n     "
				    					+ "dtRegistroDetran: '"+	(rsm.getString("DT_REGISTRO_DETRAN")!=null ? Util.convCalendarString(rsm.getGregorianCalendar("DT_REGISTRO_DETRAN")) : "" )+"',\n     "
				    					+ "tpStatus: '"+	(rsmMovimentos.getString("TP_STATUS")!=null ? rsmMovimentos.getString("TP_STATUS") : "" )+"',\n     "
				    					+ "stRecurso: '"+		(rsmMovimentos.getString("ST_RECURSO")!=null ? rsmMovimentos.getString("ST_RECURSO") : "" )+"'}\n    ";
				    		}
				    		
				    		aits+=movimentos+"]";
				    		aits+="\n  }";
		    		}
		    		ouputStream.println(aits);
		    		ouputStream.println(" ]\n}");
	    		}
	    	}
	    	
	    	ouputStream.flush();  
            ouputStream.close();
	    } 
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }
}
