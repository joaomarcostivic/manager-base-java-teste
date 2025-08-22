package com.tivic.manager.grl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sol.util.RequestUtilities;

import com.tivic.manager.print.Converter;


public class ModeloDocumentoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final int BUFSIZE = 4096;
	
	private static final int DOWNLOAD = 0;
	private static final int PREVIEW = 1;
	private static final int OPEN = 2;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  { 
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {  
	    try{
	    	int cdModeloDocumento = RequestUtilities.getParameterAsInteger(request, "m", 0);
	    	
	    	int cdProcesso = RequestUtilities.getParameterAsInteger(request, "p", 0);
	    	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "e", 0);
	    	int tpSaida = RequestUtilities.getParameterAsInteger(request, "t", OPEN);
	    	
	    	ModeloDocumento modelo = ModeloDocumentoDAO.get(cdModeloDocumento);

	    	if(modelo.getTpModelo()==ModeloDocumentoServices.TP_WORD) {
	    		
		    	
		    	byte[] bytes;
		    	ServletOutputStream outStream = response.getOutputStream();
		        //ServletContext context  = getServletConfig().getServletContext();
	
		        File inputFile = File.createTempFile("MOD_", ".mod");
		        int length = 0;
		        byte[] byteBuffer = new byte[BUFSIZE];
		        FileOutputStream fos = new FileOutputStream(inputFile);
		        DataInputStream in = new DataInputStream(new ByteArrayInputStream(modelo.getBlbConteudo()));
		        
		        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
		        	fos.write(byteBuffer, 0, length);
		        }
		        
		        in.close();
		        fos.close();
		        
		        File pdfFile = File.createTempFile("MOD_", ".pdf");	
		        Converter.docxToPdf(inputFile, pdfFile, true, cdProcesso, cdEmpresa);
		        
		    	if(tpSaida == PREVIEW) {

			        response.setContentType("application/x-shockwave-flash");
			        
	    			File swfFile = File.createTempFile("MOD_", ".swf");	
		    		
			        Converter.pdfToSwf(pdfFile, swfFile);
			        bytes = Converter.getBytesFromFile(swfFile);
			        
			        swfFile.delete();
		    	}
		    	else {//OPEN/DOWNLOAD
		    		
		    		response.setContentType("application/pdf");
		    		
		    		if(tpSaida == DOWNLOAD)
		    			response.setHeader("Content-Disposition", "attachment; filename=\"" + modelo.getNmModelo() + ".pdf\"");
		    		
		    		bytes = Converter.getBytesFromFile(pdfFile);
		    	}
		    	
		    	pdfFile.delete();
		    	inputFile.delete();
		    	 
		        response.setContentLength((int)bytes.length);
		        
		    	length = 0;
		        byteBuffer = new byte[BUFSIZE];
		        in = new DataInputStream(new ByteArrayInputStream(bytes));
		        
		        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
		            outStream.write(byteBuffer, 0, length);
		        }
	        
		        in.close();
		        outStream.close();
	    	}
	    } 
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }
}
