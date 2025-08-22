package com.tivic.manager.prc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.POIXMLProperties;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import sol.util.OneDriveClient;
import sol.util.RequestUtilities;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.ModeloDocumento;
import com.tivic.manager.grl.ModeloDocumentoDAO;
import com.tivic.manager.print.Converter;
import com.tivic.manager.ptc.DocumentoArquivo;
import com.tivic.manager.ptc.DocumentoArquivoDAO;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioChave;
import com.tivic.manager.seg.UsuarioChaveDAO;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.OneDriveUtils;
import com.tivic.manager.util.ZlibCompression;


/**
 * ORIGINALMENTE UM SERVLET PARA DOWNLOAD DE ARQUIVOS DE UM PROCESSO
 * Atualmente é utilizado para baixar arquivos no cliente web de todas as tabelas de arquivo existentes no modelo
 * 
 * TODO: Organizar as tabelas de arquivos para que a maioria aponte para GRL_ARQUIVO. 
 * TODO: Renomear este servlet. 
 * 
 * @author Sapucaia
 *
 */
public class ProcessoArquivoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final int BUFSIZE = 4096;
	
	private static final int DOWNLOAD = 0;
	private static final int PREVIEW = 1;
	private static final int OPEN = 2;
	private static final int DOWNLOAD_ONEDRIVE = 3;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  { 
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {  
	    try{
	    	int cdArquivo = RequestUtilities.getParameterAsInteger(request, "a", 0);
	    	int cdProcesso = RequestUtilities.getParameterAsInteger(request, "p", 0);
	    	int cdDocumento = RequestUtilities.getParameterAsInteger(request, "d", 0);
	    	int cdModelo = RequestUtilities.getParameterAsInteger(request, "m", 0);
	    	
	    	
	    	int cdChave = RequestUtilities.getParameterAsInteger(request, "k", 0);
	    	int cdUsuario = RequestUtilities.getParameterAsInteger(request, "u", 0);
	    	
	    	int cdPessoa = RequestUtilities.getParameterAsInteger(request, "pessoa", 0);
	    	int cdConta = RequestUtilities.getParameterAsInteger(request, "c", 0);
	    	
	    	int cdOrdemServico = RequestUtilities.getParameterAsInteger(request, "os", 0);
	    	
	    	int tpSaida = RequestUtilities.getParameterAsInteger(request, "t", OPEN);
	    	
	    	String nmArquivo;
	    	byte[] blbArquivo;
	    	
	    	ProcessoArquivo processoArquivo = null;
	    	DocumentoArquivo documentoArquivo = null;
	    	ModeloDocumento modeloDocumento = null;
	    	
	    	if(cdChave > 0 && cdUsuario > 0) {
	    		UsuarioChave usuarioChave = UsuarioChaveDAO.get(cdChave, cdUsuario);
	    		
	    		nmArquivo = "private.key";
	    		blbArquivo = usuarioChave.getBlbChavePrivada();
	    	}
	    	else if(cdConta > 0) {
	    		//PessoaArquivo pa = PessoaArquivoDAO.get(cdArquivo, cdPessoa);
	    		Arquivo arquivo = ArquivoDAO.get(cdArquivo);
	    		
	    		nmArquivo = arquivo.getNmArquivo();
	    		blbArquivo = arquivo.getBlbArquivo();
	    	}
	    	else if(cdPessoa > 0) {
	    		//PessoaArquivo pa = PessoaArquivoDAO.get(cdArquivo, cdPessoa);
	    		Arquivo arquivo = ArquivoDAO.get(cdArquivo);
	    		
	    		nmArquivo = arquivo.getNmArquivo();
	    		blbArquivo = arquivo.getBlbArquivo();
	    	}
	    	else if(cdDocumento>0){
	    		documentoArquivo = DocumentoArquivoDAO.get(cdArquivo, cdDocumento);
	    		
	    		if(documentoArquivo.getLgComprimido()==1)
	    			documentoArquivo.setBlbArquivo(ZlibCompression.decompress(documentoArquivo.getBlbArquivo()));

	    		nmArquivo = documentoArquivo.getNmArquivo();
	    		blbArquivo = documentoArquivo.getBlbArquivo();
	    	}
	    	else if(cdModelo>0){
	    		modeloDocumento = ModeloDocumentoDAO.get(cdModelo);
	    		
	    		nmArquivo = modeloDocumento.getNmModelo();
	    		blbArquivo = modeloDocumento.getBlbConteudo();
	    	}
	    	else if(cdOrdemServico>0) {
	    		Arquivo arquivo = ArquivoDAO.get(cdArquivo);
	    		
	    		nmArquivo = arquivo.getNmArquivo();
	    		blbArquivo = arquivo.getBlbArquivo();
	    	}
	    	else {
	    		processoArquivo = ProcessoArquivoDAO.get(cdArquivo, cdProcesso);
	    		
	    		if(processoArquivo.getLgComprimido()==1)
	    			processoArquivo.setBlbArquivo(ZlibCompression.decompress(processoArquivo.getBlbArquivo()));
	    		
	    		//INSERINDO METADADOS EM ARQUIVOS .docx
	    		try {
	    			
	    			ByteArrayInputStream in = new ByteArrayInputStream(processoArquivo.getBlbArquivo());
	    			XWPFDocument document = new XWPFDocument(in);
					
					POIXMLProperties props = document.getProperties();
					POIXMLProperties.CustomProperties customProps = props.getCustomProperties();
					customProps.addProperty("CD_PROCESSO", cdProcesso);
					customProps.addProperty("CD_ARQUIVO", cdArquivo);
					customProps.addProperty("CD_TIPO_DOCUMENTO", processoArquivo.getCdTipoDocumento());
					customProps.addProperty("NM_DOCUMENTO", processoArquivo.getNmDocumento());
					customProps.addProperty("CD_USUARIO", cdUsuario);
										
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					document.write(baos);
					
					document.close();
					
					processoArquivo.setBlbArquivo(baos.toByteArray());

	    		} catch(Exception e) {
	    			//XXX:
	    			//System.out.println("ProcessoArquivoServlet: Erro ao incluir metadados.");
	    		}

	    		nmArquivo = processoArquivo.getNmArquivo();
	    		blbArquivo = processoArquivo.getBlbArquivo();
	    	}
	    	
	    	byte[] bytes = null;
	    	ServletOutputStream outStream = response.getOutputStream();
	        ServletContext context  = getServletConfig().getServletContext();
	        String mimetype = context.getMimeType(nmArquivo);
	        
	        //System.out.println(mimetype);
	        
	    	if(tpSaida == PREVIEW) {
	    		
	    		if(mimetype.equals("image/jpeg") ||
	    		   mimetype.equals("image/png") ||
	    		   mimetype.equals("image/gif")) {
		    		bytes = blbArquivo;
		    		response.setContentType(mimetype);
	    		}
	    		else {
	    		
	    		
		    		String suffix = ".pdf";
		    		
			        if(mimetype.equals("application/pdf"))
			    		suffix = ".pdf";
			        else if(mimetype.equals("application/msword"))
			    		suffix = ".doc";
			        else if(mimetype.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
			    		suffix = ".docx";
			        
			        File inputFile = File.createTempFile("JUR_", suffix);
			        File swfFile = File.createTempFile("JUR_", ".swf");	
		    		
			        int length = 0;
			        byte[] byteBuffer = new byte[BUFSIZE];
			        FileOutputStream fos = new FileOutputStream(inputFile);
			        DataInputStream in = new DataInputStream(new ByteArrayInputStream(blbArquivo));
			        
			        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
			        	fos.write(byteBuffer, 0, length);
			        }
			        
			        in.close();
			        fos.close();
			        
			        if(mimetype.equals("application/pdf"))
			        	Converter.pdfToSwf(inputFile, swfFile);
			        else if(mimetype.equals("application/msword") ||
			        		mimetype.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
			        	Converter.docxToSwf(inputFile, swfFile);
			        
			        bytes = Converter.getBytesFromFile(swfFile);
			        
			        inputFile.delete();
			        swfFile.delete();
			        
			        response.setContentType("application/x-shockwave-flash");
	    		}
	    	}
	    	else if(tpSaida == DOWNLOAD_ONEDRIVE) {
	    		if (mimetype == null) {
		            mimetype = "application/octet-stream";
		        }
	    		response.setContentType(mimetype);
	    		response.setHeader("Content-Disposition", "attachment; filename=\"" + nmArquivo + "\"");
	    		
	    		OneDriveClient odc = OneDriveUtils.getClient();
	    		if(odc==null) {
	    			System.out.println("Erro ao baixar arquivo do OneDrive.");
	    			return;
	    		}
	    		if(processoArquivo!=null) {
	    			
	    			byte[] docxContent = odc.downloadFile(processoArquivo.getIdRepositorio());
	    			if(docxContent==null) {
	    				System.out.println("Erro ao baixar arquivo do OneDrive.");
	    				return;
	    			}
	    			
	    			bytes = docxContent;
	    		}
	    	}
	    	else {//OPEN/DOWNLOAD
	    		
	    		if (mimetype == null) {
		            mimetype = "application/octet-stream";
		        }
	    		response.setContentType(mimetype);
	    		
	    		if(tpSaida == DOWNLOAD)
	    			response.setHeader("Content-Disposition", "attachment;filename=\"" + nmArquivo + "\"");
	    		else
	    			response.setHeader("Content-Disposition", "inline;filename=\"" + nmArquivo + "\"");
	    		
	    		bytes = blbArquivo;
	    	}
	    	
	    	if(bytes!=null){
	    		response.setContentLength((int)bytes.length);
	        
		    	int length = 0;
		        byte[] byteBuffer = new byte[BUFSIZE];
		        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
		        
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
