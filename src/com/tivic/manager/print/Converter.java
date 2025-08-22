package com.tivic.manager.print;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import sol.util.ConfManager;

import com.lowagie.text.html.HtmlParser;
import com.lowagie.text.rtf.RtfWriter2;
import com.tivic.manager.util.Util;

public class Converter {
	
	public final static String[][] unicodeSet ={{"aacute", "#225"}, {"atilde", "#227"},
		{"acirc",  "#226"}, {"agrave", "#224"},
		{"Aacute", "#193"}, {"Atilde", "#195"},
		{"Acirc",  "#194"}, {"Agrave", "#192"},
		{"eacute", "#233"}, {"ecirc",  "#234"},
		{"egrave", "#232"}, {"Eacute", "#201"},
		{"Ecirc",  "#202"}, {"Egrave", "#200"},
		{"iacute", "#237"}, {"icirc",  "#238"},
		{"igrave", "#236"}, {"Iacute", "#205"},
		{"Icirc",  "#206"}, {"Igrave", "#204"},
		{"oacute", "#243"}, {"otilde", "#245"},
		{"ocirc",  "#244"}, {"ograve", "#242"},
		{"Oacute", "#211"}, {"Otilde", "#213"},
		{"Ocirc",  "#212"}, {"Ograve", "#210"},
		{"uacute", "#250"}, {"ucirc",  "#251"},
		{"ugrave", "#249"}, {"Uacute", "#218"},
		{"Ucirc",  "#219"}, {"Ugrave", "#217"},
		{"ccedil", "#231"}, {"Ccedil", "#199"},
		{"nbsp","#160"},    {"deg","#176"},
		{"ordm","#186"}};
	
	public static void pdfToSwf(File pdfFile, File swfFile) {
		try {
			String arguments = "\""+pdfFile.getAbsolutePath() + "\" -o \"" + swfFile.getAbsolutePath() + "\" -f -T 9 -t -s storeallcharacters";
			boolean verbose = getVerbose();
			
			if(verbose){
				System.out.println("getExecutablePath: "+getExecutablePath());
		    	System.out.println("getArguments: "+arguments);
			}
			
			Process process = Runtime.getRuntime().exec( getExecutablePath() + " " + arguments );
			
			BufferedReader r = new BufferedReader(
				new InputStreamReader(process.getInputStream())
			);
			String x;
			while ((x = r.readLine()) != null) {
				if(verbose)
					System.out.println(x);
			}
			r.close();
			
			process.waitFor();
			
			if(verbose)
				System.out.println("end.");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void docxToSwf(File docxFile, File swfFile) {
		docxToSwf(docxFile, swfFile, false, 0, 0);
	}

	public static void docxToSwf(File docxFile, File swfFile, boolean replacePlaceholders, int cdProcesso, int cdEmpresa) {
		try {
			boolean verbose = getVerbose();
			
			if(verbose)
				System.out.println("Iniciando conversão: "+ docxFile.getName() + " para "+swfFile.getName());
			
			

			if(verbose)
				System.out.println("Convertendo docx pdf...");

			File pdfFile = File.createTempFile("MOD_", ".pdf");
			
			docxToPdf(docxFile, pdfFile, replacePlaceholders, cdProcesso, cdEmpresa);
			pdfToSwf(pdfFile, swfFile);
			
//			pdfFile.delete();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void docxToPdf(File docxFile, File pdfFile, boolean replacePlaceholders, int cdProcesso, int cdEmpresa) {
		try {
			boolean verbose = getVerbose();
			
			if(verbose)
				System.out.println("Iniciando conversão: "+ docxFile.getName() + " para "+pdfFile.getName());
			

			if(verbose)
				System.out.println("Convertendo docx pdf...");

			byte[] pdfSource = docxToPdf(getBytesFromFile(docxFile));
			
			int length = 0;
	        byte[] byteBuffer = new byte[4096];
	        FileOutputStream fos = new FileOutputStream(pdfFile);
	        ByteArrayInputStream in= new ByteArrayInputStream(pdfSource);
	        
	        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
	        	fos.write(byteBuffer, 0, length);
	        }
	        
	        in.close();
	        fos.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] rtfToHtml(byte[] rtf) throws IOException, BadLocationException {
		boolean verbose = getVerbose();
		
		ByteArrayInputStream input= new ByteArrayInputStream(rtf);
        StringWriter writer = new StringWriter();
        
        RTFEditorKit rtfEditorKit = new RTFEditorKit();
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        javax.swing.text.Document doc = rtfEditorKit.createDefaultDocument();
        rtfEditorKit.read(input, doc, 0);
        htmlEditorKit.write(writer, doc, 0, doc.getLength());
        
        String html = writer.toString();
        
        if(verbose)
        	System.out.println(new String(rtf)+"\nrtf->html\n"+html);
       
        return html.getBytes();
	}
	
	public static byte[] htmlToRtf(byte[] html) throws IOException, BadLocationException {
		boolean verbose = getVerbose();
		
		String t = convertToUnicode(new String(html, "UTF-8"));
		t = t.trim().replaceFirst("^(.*)<","<");
		html = ("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><html><body>"+ t +"</body></html>").getBytes();
		
		if(verbose)
	          System.out.println(new String(html));
		
		com.lowagie.text.Document document = new com.lowagie.text.Document();
		ByteArrayOutputStream baout= new ByteArrayOutputStream();
		RtfWriter2.getInstance(document, baout);
		
		ByteArrayInputStream input = new ByteArrayInputStream(html);
		HtmlParser hp = new HtmlParser();
		hp.go(document, input);
		
		byte[] rtf = baout.toByteArray();
		
		if(verbose)
          System.out.println(html+"\nhtml->rtf\n"+rtf);
		
		return rtf;
	}

	public static byte[] htmlToDocx(byte[] html) {
		boolean verbose = getVerbose();
		
		try {

		} 
		catch (Exception e) {
			if(verbose)
				e.printStackTrace();
		}
		return null;
	}
	

	public static byte[] docxToHtml(byte[] docxContent) {
		boolean verbose = getVerbose();
		
		try {			
			
			byte[] html = null;
			try {

				ByteArrayInputStream in = new ByteArrayInputStream(docxContent);
				XWPFDocument document = new XWPFDocument(in);
				
				XHTMLOptions options = XHTMLOptions.create();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				XHTMLConverter.getInstance().convert(document, out, options);
				html = out.toByteArray();
		        out.close();
		        
			}
			catch(Exception oe) {		
				if(verbose) {
					System.out.println("Erro na conversão");
					oe.printStackTrace();
				}
				html = "Este modelo está em um formato que não pode ser convertido. (Ex: doc em vez de docx.)".getBytes();
//				
//				ByteArrayInputStream in = new ByteArrayInputStream(docxContent);
//				HWPFDocumentCore doc = WordToHtmlUtils.loadDoc(in);
//	
//		        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
//		                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
//		        wordToHtmlConverter.processDocument(doc);
//				org.w3c.dom.Document htmlDocument = wordToHtmlConverter.getDocument();
//		        
//		        ByteArrayOutputStream out = new ByteArrayOutputStream();
//		        DOMSource domSource = new DOMSource(htmlDocument);
//		        StreamResult streamResult = new StreamResult(out);
//
//		        TransformerFactory tf = TransformerFactory.newInstance();
//		        Transformer serializer = tf.newTransformer();
//		        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//		        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
//		        serializer.setOutputProperty(OutputKeys.METHOD, "html");
//		        serializer.transform(domSource, streamResult);
//		        
//		        html = out.toByteArray();
//		        out.close();
			}
			
	        return html;
		} 
		catch (Exception e) {
			if(verbose)
				e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] docxToPdf(byte[] docxContent) {
		boolean verbose = getVerbose();
		
		try {			
			
			byte[] result = null;
			try {
				ByteArrayInputStream in = new ByteArrayInputStream(docxContent);
				XWPFDocument document = new XWPFDocument(in);

				PdfOptions options = PdfOptions.create();

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				 
				PdfConverter.getInstance().convert(document, out, options);
				
				result = out.toByteArray();
		        
			}
			catch(Exception oe) {
				if(verbose) {
					System.out.println("Erro na conversão");
					oe.printStackTrace();
				}
				result = "Este modelo está em um formato que não pode ser convertido. (Ex: doc em vez de docx.)".getBytes();
			}

	        return result;
		} 
		catch (Exception e) {
			if(verbose)
				e.printStackTrace();
		}
		return null;
	}
	
	protected static String getExecutablePath() {
		ConfManager conf = Util.getConfManager();
    	return "\""+conf.getProps().getProperty("PDF2SWF_PATH")+"\"";
	}
	
	protected static boolean getVerbose() {
		ConfManager conf = Util.getConfManager();
		return conf.getProps().getProperty("PDF2SWF_VERBOSE")==null ? true : conf.getProps().getProperty("PDF2SWF_VERBOSE").equals("1");
	}
	
	public static String convertToUnicode(String str) {
		for(int i=0; i<unicodeSet.length; i++)
			str = str.replaceAll(unicodeSet[i][0], unicodeSet[i][1]);
		return str;
	}
	
	public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        long length = file.length();
    
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
    
        byte[] bytes = new byte[(int)length];
    
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
        is.close();
        
        if (offset < bytes.length) {
            throw new IOException("Erro ao ler arquivo: "+file.getName());
        }
        
        return bytes;
    }
	
	public static byte[] tlfToPdf(byte[] tlf) {
		
		return null;
	}
}
