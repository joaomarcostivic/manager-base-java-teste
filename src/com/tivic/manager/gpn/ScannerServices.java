package com.tivic.manager.gpn;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.FormularioAtributoValor;
import com.tivic.manager.grl.FormularioAtributoValorServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.prc.ProcessoArquivo;
import com.tivic.manager.prc.ProcessoArquivoServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoArquivo;
import com.tivic.manager.ptc.DocumentoArquivoServices;
import com.tivic.manager.ptc.DocumentoPessoa;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.util.LogUtils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.LoadLibs;

import sol.util.Result;

public class ScannerServices {
	
	public static final int TP_ASSINATURA_INTERNA = 0;
	public static final int TP_ASSINATURA_ICP_BRASIL = 1;
		
	public static Result processar(ArrayList<byte[]> paginas, String titulo, String assunto, 
			int cdTipoDocumento, ArrayList<FormularioAtributoValor> formulario, 
			Documento documento, ArrayList<DocumentoPessoa> solicitantes, int cdProcesso, int cdAndamento, int cdPessoa, 
			Usuario usuarioAssinatura, String nmSenha, int tpAssinatura, byte[] blbAssinatura, 
			AuthData authData) {
		
		return processar(paginas, titulo, assunto, cdTipoDocumento, formulario, documento, solicitantes, cdProcesso, cdAndamento, 
				cdPessoa, usuarioAssinatura, nmSenha, tpAssinatura, blbAssinatura, authData, null);
	}
	
	
	public static Result processar(ArrayList<byte[]> paginas, String titulo, String assunto, 
			int cdTipoDocumento, ArrayList<FormularioAtributoValor> formulario, 
			Documento documento, ArrayList<DocumentoPessoa> solicitantes, int cdProcesso, int cdAndamento, int cdPessoa, 
			Usuario usuarioAssinatura, String nmSenha, int tpAssinatura, byte[] blbAssinatura, 
			AuthData authData, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if(isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			/**
			 * 1. OCR DA IMAGEM
			 * 2. GERAR PDF
			 * 3. ASSINAR PDF
			 * 4. VINCULAR (PROCESSO/DOCUMENTO/PESSOA/AVULSO)
			 * 5. INDEXAR (FORMULÁRIO)
			 */
			
			Documento documentoResult = null;
			Result result = null;
			
			//1. OCR DA IMAGEM
			String txtOcr = "";
			for (byte[] page : paginas) {
				txtOcr += doOcr(page) + "\n\n";
			}
			
			//2. GERAR PDF
			Pessoa autor = null;
			if(usuarioAssinatura!=null)
				autor = PessoaServices.get(usuarioAssinatura.getCdPessoa(), connection);
			
			float margin = 2;
			
			boolean lgSignature = blbAssinatura!=null && blbAssinatura.length > 0;
			
			result = exportImagesToPdf(0, margin, margin, margin, margin, paginas, autor, titulo, assunto, lgSignature);
			
			if(result.getCode()<0) {
				if(isConnectionNull)
					connection.rollback();
				return result;
			}
			
			LogUtils.debug("2: "+result.getMessage());
			
			byte[] blbArquivo = (byte[])result.getObjects().get("bytes");
			
			if(blbArquivo==null || blbArquivo.length==0) {
				return new Result(-2, "Arquivo é nulo ou inválido.");
			}
			
			//3. ASSINAR PDF
			if(lgSignature){
				
				if(nmSenha==null || nmSenha.equals("")) {
					return new Result(-3, "Senha é nula ou inválida");
				}
				
				if(tpAssinatura == TP_ASSINATURA_ICP_BRASIL) {
					result = signPdfPfx(blbArquivo, blbAssinatura, nmSenha, paginas.size());
					
					if(result.getCode()<0) {
						if(isConnectionNull)
							connection.rollback();
						return result;
					}
					
					LogUtils.debug("3: "+result.getMessage());
					
					blbArquivo = (byte[])result.getObjects().get("PDF_BYTES");
				}
				else {
					
					if(usuarioAssinatura==null || usuarioAssinatura.getCdUsuario()<=0) {
						return new Result(-4, "Usuário da assinatura é nulo ou inválido");
					}
					
					blbArquivo = signPdf(blbArquivo, blbAssinatura, usuarioAssinatura, nmSenha);
				}
			}
			
			
			//4. VINCULAR (PROCESSO/DOCUMENTO/PESSOA/AVULSO)
			DocumentoArquivo docArquivoResult = null;
			ProcessoArquivo procArquivo = null;
//			AssinaturaDigital assinatura = null;
//			if(usuarioAssinatura!=null)
//				assinatura = AssinaturaDigitalServices.getAssinaturaByUsuario(usuarioAssinatura.getCdUsuario(), connection);

			int cdDocumento = 0;
			if(documento!=null) {
				if(documento.getCdDocumento()==0) {
					result = DocumentoServices.save(documento, solicitantes, connection);
					if(result.getCode()<0) {
						if(isConnectionNull)
							connection.rollback();
						return result;
					}
					documentoResult = (Documento)result.getObjects().get("DOCUMENTO");
					cdDocumento = result.getCode();
				}
				else {
					cdDocumento = documento.getCdDocumento();
				}
			}
			
			if(cdDocumento>0) {
				DocumentoArquivo arquivo = new DocumentoArquivo(0, cdDocumento, 
						titulo+".pdf", 
						titulo, 
						new GregorianCalendar()/*dtArquivamento*/, 
						blbArquivo, 
						0 /*lgComprimido*/, 
						cdTipoDocumento, 
						1/*stArquivo*/, 
						null/*idRepositorio*/, 
						0,//(assinatura!=null ? assinatura.getCdAssinatura() : 0)/*cdAssinatura*/, 
						txtOcr);
				
				result = DocumentoArquivoServices.save(arquivo, false, connection);
				if(result.getCode()<0) {
					connection.rollback();
					return result;
				}
				
				LogUtils.debug("4: "+result.getMessage());
				docArquivoResult = (DocumentoArquivo)result.getObjects().get("ARQUIVO");
			}
			else if(cdProcesso>0) {				
				ProcessoArquivo arquivo = new ProcessoArquivo(0, cdProcesso, cdAndamento, titulo+".pdf", titulo, new GregorianCalendar(), 
						blbArquivo, 0, 1, 0, null, null, 0, txtOcr, cdTipoDocumento);
				result = ProcessoArquivoServices.save(arquivo, false, connection);
				if(result.getCode()<0) {
					connection.rollback();
					return result;
				}
				
				procArquivo = (ProcessoArquivo)result.getObjects().get("ARQUIVO");
				
			}
			else if(cdPessoa>0) {
				
			}
			
			//5. INDEXAR (FORMULÁRIO)
			if(cdDocumento>0) {
				LogUtils.debug("formulario.length: "+formulario.size());
				if(formulario!=null && formulario.size()>0) {
					for (FormularioAtributoValor atributoValor : formulario) {
						atributoValor.setCdDocumento(cdDocumento);
						atributoValor.setCdArquivoDocumento(docArquivoResult.getCdArquivo());
						
						LogUtils.debug("atributoValor: "+atributoValor.toString());
						result = FormularioAtributoValorServices.save(atributoValor, connection);
						
						LogUtils.debug("5: "+result.getMessage());
					}
				}
			}
			
			if(result.getCode()>0) {
				if(isConnectionNull)
					connection.commit();
			}
			
			result = new Result(1, "Arquivo indexado com sucesso!");
			result.addObject("DOCUMENTO", documentoResult);
			
			return result;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if(isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	private static byte[] signPdf(byte[] blbArquivo, byte[] blbAssinatura, Usuario usuarioAssinatura, String nmSenha) {
		return blbArquivo;
	}


	private static Result signPdfPfx(byte[] blbArquivo, byte[] blbAssinatura, String nmSenha, int pageNumber) {
		try {
			KeyStore ks = KeyStore.getInstance("pkcs12");
			ByteArrayInputStream bais = new ByteArrayInputStream(blbAssinatura);
			ks.load(bais, nmSenha.toCharArray());
			String alias = (String)ks.aliases().nextElement();
			
			PrivateKey key = (PrivateKey)ks.getKey(alias, nmSenha.toCharArray());
			Certificate[] chain = ks.getCertificateChain(alias);
			 
			PdfReader reader = new PdfReader(blbArquivo);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			PdfStamper stp = PdfStamper.createSignature(reader, baos, '\0');
			PdfSignatureAppearance sap = stp.getSignatureAppearance();
			sap.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
			
			//sap.setVisibleSignature("signature");
			sap.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
	        sap.setReason("Autor do documento");
			sap.setLocation("Brasil");
			 
			sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), pageNumber, null);
			stp.close();
			
			return new Result(1, "Assinado com sucesso!", "PDF_BYTES", baos.toByteArray());
		}
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, "Erro ao assinar o documento.", "PDF_BYTES", blbArquivo);
		}
    }	


	public static String doOcr(byte[] pageImg) {
		try {
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(pageImg));
			
			ITesseract instance = new Tesseract();
			File tessDataFolder = LoadLibs.extractTessResources("tessdata");
	        instance.setDatapath(tessDataFolder.getParent());
	        instance.setLanguage("por");
			
			return instance.doOCR(img);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static Result exportImagesToPdf(int pageFormat, float marginTop, float marginBottom, float marginLeft, float marginRight, ArrayList<byte[]> imgPaginas,
			Pessoa autor, String titulo, String assunto, boolean graphicSignature) {
		return exportImagesToPdf(pageFormat, marginTop, marginBottom, marginLeft, marginRight, imgPaginas, null, autor, titulo, assunto, graphicSignature);
	}
	
	@SuppressWarnings("unused")
	public static Result exportImagesToPdf(int pageFormat, float marginTop, float marginBottom, float marginLeft, float marginRight, 
			ArrayList<byte[]> imgPaginas, String fileName, Pessoa autor, String titulo, String assunto, boolean graphicSignature) {
		
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
			
			PdfWriter writer = null;
			if(fileName!=null) {
				fileName = fileName.replaceAll("[/\\\\]", "_");
				file = new File(fileName);
				writer = PdfWriter.getInstance(document , new FileOutputStream(file));
			}
			else {
				writer = PdfWriter.getInstance(document , baout);
			}
			
			document.addCreationDate();
		    document.addCreator("MyManager - TIVIC");
		    
		    if(autor!=null)
				document.addAuthor(autor.getNmPessoa());
		    
		    if(titulo!=null && !titulo.equals(""))
		    	document.addTitle(titulo);
		    
		    if(assunto!=null && !assunto.equals(""))
		    	document.addSubject(assunto);
			
			document.open(); 
			
//			if(graphicSignature) {
//				PdfFormField field = PdfFormField.createSignature(writer);
//				field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_INVERT);
//				field.setFieldName("signature");
//				field.setFlags(PdfAnnotation.FLAGS_PRINT);
//				field.setPage();
//				field.setMKBorderColor(Color.BLACK);
//				field.setMKBackgroundColor(Color.WHITE);
//				
//				PdfAppearance tp = PdfAppearance.createAppearance(writer, 72, 48);
//				tp.rectangle(0.5f, 0.5f, 71.5f, 47.5f);
//				tp.stroke();
//				
//				field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, tp);
//				writer.addAnnotation(field);
//			}
			
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
		    return new Result(-1, "Erro ao gerar exportação em PDF para o scanner");
		}
	}

}
