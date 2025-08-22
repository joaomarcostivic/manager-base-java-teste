package com.tivic.manager.print;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import sol.util.Result;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

public class TLFServices {
	
	public static Result exportImages(int pageFormat, float marginTop, float marginBottom, float marginLeft, float marginRight, 
			ArrayList<byte[]> imgPaginas) {
		return exportImages(pageFormat, marginTop, marginBottom, marginLeft, marginRight, imgPaginas, null);
	}
	
	public static Result exportImages(int pageFormat, float marginTop, float marginBottom, float marginLeft, float marginRight, 
			ArrayList<byte[]> imgPaginas, String fileName) {
		
		try {
			 
			Rectangle rect = PageSize.A4;
			switch(pageFormat) {
				case 0:
					rect = PageSize.A4;
					break;
				case 1:
					rect = PageSize.LETTER;
					break;
				case 2:
					rect = PageSize.LEDGER;
					break;
				default:
					rect = PageSize.A4;
			}
			
			//desconsiderar as margens agora que o screenshot é da página toda
			//Document document = new Document(rect, marginLeft, marginRight, marginTop, marginBottom);
			Document document = new Document(rect, 0, 0, 0, 0);
			
			ByteArrayOutputStream baout = new ByteArrayOutputStream();
			File file = null;
			
			if(fileName!=null) {
				fileName = fileName.replaceAll("[/\\\\]", "_");
				
				file = new File(fileName);
				
				//System.out.println(file.getAbsolutePath());
				
				PdfWriter.getInstance(document , new FileOutputStream(file));
			}
			else {
				PdfWriter.getInstance(document , baout);
			}
			
			document.addAuthor("Sol Framework (http://www.nuix.com.br)" );
			document.addTitle("sol.tlf.PDFImageExport - Documento exportado via Sol Framework");
			document.addCreator("http://www.nuix.com.br");
			//document.addSubject("This is the result of a Test."); 
			
			document.open(); 
			
			for (byte[] imgBytes : imgPaginas) {
				Image img = Image.getInstance(imgBytes);
				img.setAlignment(Image.LEFT);
				img.setCompressionLevel(0);
				
				//desconsiderar as margens agora que o screenshot é da página toda
				/*img.scaleToFit(rect.getWidth() - marginLeft - marginRight, 
						   rect.getHeight() - marginTop - marginBottom); */
				
				img.scaleToFit(rect.getWidth(), rect.getHeight());
			
				document.add(img);
				//System.out.println("lenght: "+imgBytes.length);
			}
			document.close();
			
			if(file!=null)
				return new Result(1, "Arquivo exportado com sucesso!", "fileName", file.getName());
			else
				return new Result(1, "Arquivo exportado com sucesso!", "bytes", baout.toByteArray());
		} 
		catch (Exception e) {
		    e.printStackTrace();
		    return new Result(-1, "Erro ao gerar exportação em PDF do TLF");
		}
	}
}
