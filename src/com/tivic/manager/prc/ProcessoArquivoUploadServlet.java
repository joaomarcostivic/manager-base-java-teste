package com.tivic.manager.prc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.tivic.manager.grl.ModeloDocumento;
import com.tivic.manager.grl.ModeloDocumentoDAO;
import com.tivic.manager.grl.ModeloDocumentoServices;
import com.tivic.manager.util.Util;

import sol.util.Result;

/**
 * Utilizado para realizar upload de arquivos diretamente para um processo
 * 
 * @author Sapucaia
 *
 */
public class ProcessoArquivoUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final int UPLOAD_DOCUMENTO = 0;
	private static final int UPLOAD_MODELO = 1;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  { 
		doPost(request, response);
	}
	
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {  
		try {
		    
			response.setHeader("Cache-Control", "no-cache");
      		response.setContentType("text/xml; charset=iso-8859-1");
      		
			boolean isMultipart = ServletFileUpload.isMultipartContent(request); 
			
			Result r; 
					
			if(isMultipart) {
				List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
	            
				int type = request.getParameter("t") != null ? new Integer(request.getParameter("t")) : UPLOAD_DOCUMENTO;
				
				int cdArquivo = 0;
				int cdProcesso = 0;
				int cdTipoDocumento = 0;
	            String nmDocumento = null;
	            String nmArquivo = null;
				
	            try {
	            	cdArquivo = (request.getParameter("cdArquivo") != null || !request.getParameter("cdArquivo").equals("")) ? new Integer(request.getParameter("cdArquivo")) : 0;
	            	//XXX:
	            	System.out.println("\t\tcdArquivo: "+cdArquivo);
	            } catch(Exception ex) {
	            	//ex.printStackTrace(System.out);
	            }
	            
				cdProcesso = request.getParameter("cdProcesso") != null ? new Integer(request.getParameter("cdProcesso")) : 0;
	            cdTipoDocumento = request.getParameter("cdTipoDocumento") != null ? new Integer(request.getParameter("cdTipoDocumento")) : 0;
                nmDocumento = request.getParameter("nmDocumento");
                nmArquivo = request.getParameter("nmArquivo");
                				
                //TODO:
                if(nmDocumento!=null && !nmDocumento.equals(""))
                	nmDocumento = Util.toUTF8(nmDocumento); 
                
                if(nmDocumento!=null && !nmDocumento.equals(""))
                	nmArquivo = Util.toUTF8(nmArquivo); 
                
                //TODO:
//				if(nmDocumento!=null && !nmDocumento.equals(""))
//                	nmDocumento = Util.retirarAcentos(nmDocumento); 
//                
//                if(nmDocumento!=null && !nmDocumento.equals(""))
//                	nmArquivo = Util.retirarAcentos(nmArquivo); 
				
	            int cdModelo = 0;
	            String nmModelo = null;
	        	int tpModelo = 0;
	        	String txtConteudo = null;
	        	int cdEmpresa = 0;
	        	
				cdModelo = request.getParameter("cdModelo") != null ? new Integer(request.getParameter("cdModelo")) : 0;
				nmModelo = request.getParameter("nmModelo");
				tpModelo = request.getParameter("tpModelo") != null ? new Integer(request.getParameter("tpModelo")) : 0;
				txtConteudo = request.getParameter("txtConteudo");
				cdEmpresa = request.getParameter("cdEmpresa") != null ? new Integer(request.getParameter("cdEmpresa")) : 0;
								
				//TODO:
				if(nmModelo!=null && !nmModelo.equals(""))
                	nmModelo = Util.toUTF8(nmModelo); 
				
				if(txtConteudo!=null && !txtConteudo.equals(""))
                	txtConteudo = Util.toUTF8(txtConteudo); 

				//TODO:
//				if(nmModelo!=null && !nmModelo.equals(""))
//                	nmModelo = Util.retirarAcentos(nmModelo); 
//				
//				if(txtConteudo!=null && !txtConteudo.equals(""))
//                	txtConteudo = Util.retirarAcentos(txtConteudo); 
				
	            byte[] fileBytes = null;
	            
	            for (FileItem item : items) {
            		if (!item.isFormField()) {
	                	fileBytes = item.get();
	                } 
		        }
	            
//	            System.out.println("nmModelo: "+nmModelo);
//	            System.out.println("\t\tisUTF8: "+Util.testUtf8(nmModelo));
//	            System.out.println("\t\tisISO-8859-1: "+Util.testIso88591(nmModelo));
//	            System.out.println("\t\tUTF-8 to ISO-8859-1: "+Util.UTF8toIso88591(nmModelo));
//	            System.out.println("\t\tto UTF-8: "+Util.toUTF8(nmModelo));
	            
	            //XXX:
            	System.out.println("\t\ttype: "+type);
	            if(type == UPLOAD_DOCUMENTO) {
	            	
	            	if(cdProcesso>0 && 
		               cdTipoDocumento > 0 &&
		               nmDocumento != null &&
		               fileBytes != null) {
		            	
		            	ProcessoArquivo arquivo = new ProcessoArquivo(cdArquivo, cdProcesso, 0, nmArquivo, nmDocumento, new GregorianCalendar(), fileBytes, 0, cdTipoDocumento, null);
		            	
		            	r = ProcessoArquivoServices.save(arquivo);
		            }
		            else
		            	r = new Result(-3, "Os dados enviados nao estao dados corretos.");
	            }
	            else if(type == UPLOAD_MODELO) {
	            	ModeloDocumento modelo = ModeloDocumentoDAO.get(cdModelo);
	            	
	            	if(modelo == null) {
	            		modelo = new ModeloDocumento(cdModelo, 
	            				nmModelo, 
	            				null, /*txtModelo*/
	            				tpModelo, 
	            				fileBytes, 
	            				txtConteudo, 
	            				1, /*stModelo*/
	            				null, /*nmTitulo*/
	            				cdTipoDocumento, 
	            				null, /*idModelo*/
	            				null, /*urlModelo*/
	            				null, /*idRepositorio*/
	            				cdEmpresa);
	            	}
	            	else {
	            		modelo.setNmModelo(nmModelo);
	            		modelo.setTpModelo(tpModelo);
	            		modelo.setBlbConteudo(fileBytes);
	            		modelo.setTxtConteudo(txtConteudo);
	            		modelo.setCdTipoDocumento(cdTipoDocumento);
	            	}
	            	
	            	r = ModeloDocumentoServices.save(modelo);
	            }
	            else
	            	r = new Result(-4, "Tipo de ação não pode ser processada.");
			}
			else 
				r = new Result(-2, "O envio não possui um arquivo anexo."); 
				
			PrintWriter out = response.getWriter();
        	out.print(sol.util.Jso.getStream(r));
        } 
		catch (Exception e) {
            Result r = new Result(-1, "Erro ao gravar arquivo no processo.");

        	PrintWriter out = response.getWriter();
        	out.print(sol.util.Jso.getStream(r));
        	
            e.printStackTrace();
        }
    }
}
