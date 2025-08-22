package com.tivic.manager.print;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
import sol.util.RequestUtilities;


public class ReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public static final int PDF = 0;
	public static final int XML = 1;
	public static final int XLS = 2;
	public static final int DOC = 3;
	public static final int ARQUIVO = 4;
	public static final int BASE64_IMAGE = 5;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  { 
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {  
	    try{
	    	int reportType   = RequestUtilities.getParameterAsInteger(request, "type", PDF);
	    	int downloadFile = RequestUtilities.getParameterAsInteger(request, "download", 0);
	    	String   fName   = RequestUtilities.getParameterAsString(request, "filename", "file");
	    	
	    	byte[] bytes = new byte[0];
	    	JasperPrint print = (JasperPrint)request.getSession().getAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
    		
	    	switch(reportType){
	    		case PDF:
		    		response.setContentType("application/pdf");
	    			bytes = ReportServices.getPdfReport(print);
	    			break;
	    		case XML:
	    			response.setContentType("application/xml");
	    			bytes = ReportServices.getXmlReport(print);
	    			break;
	    		case XLS:
	    			response.setContentType("application/vnd.ms-excel");
	    			bytes = ReportServices.getXlsReport(print);
	    			break;
	    		case DOC:
	    			response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
	    			bytes = ReportServices.getDocReport(print);
	    			break;
	    		case ARQUIVO:
	    			String fileName = RequestUtilities.getParameterAsString(request, "n", null);
	    			int deleteFile = RequestUtilities.getParameterAsInteger(request, "d", 0);
	    			
	    			if(fileName!=null){
	    				File file = new File(fileName);
		    			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		    			bytes = Converter.getBytesFromFile(file);
		    			
		    			if(deleteFile==1){
		    				file.delete();
		    			}
	    			}
	    			break;
	    		case BASE64_IMAGE:
	    			String encodedImage = RequestUtilities.getParameterAsString(request, "e", null);
	    			String mime = RequestUtilities.getParameterAsString(request, "m", "jpg");
	    			response.setContentType("image/"+mime);
	    			
	    			bytes = DatatypeConverter.parseBase64Binary(encodedImage);
	    			break;
	    	}
	    	
	    	if(downloadFile == 1) {
	    		response.setHeader("Content-Disposition", "attachment; filename=\""+fName+"\"");
	    	}

	    	if (bytes != null && bytes.length > 0) {  
	            response.setContentLength(bytes.length);  
	            ServletOutputStream ouputStream = response.getOutputStream();  
	            ouputStream.write(bytes, 0, bytes.length);  
	            ouputStream.flush();  
	            ouputStream.close();
	        }
	    } 
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }
}
