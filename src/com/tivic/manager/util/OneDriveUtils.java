package com.tivic.manager.util;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.tivic.manager.grl.ParametroServices;

import sol.util.OneDriveClient;

public class OneDriveUtils {
	
	public static OneDriveClient getClient() {
		return getClient(null);
	}
	
	public static OneDriveClient getClient(Connection connect) {
		String clientId = ParametroServices.getValorOfParametro("GED_ONEDRIVE_CLIENT_ID", "", connect);
		String clientSecret = ParametroServices.getValorOfParametro("GED_ONEDRIVE_CLIENT_SECRET", "", connect);
		String refreshToken = ParametroServices.getValorOfParametro("GED_ONEDRIVE_REFRESH_TOKEN", "", connect);
		
		if(clientId==null || clientId.equals("")) {
			System.out.println("O Id do cliente para acesso ao repositório OneDrive não foi indicado.");
			return null;
		}
		if(clientSecret==null || clientSecret.equals("")) {
			System.out.println("O segredo do cliente para acesso ao repositório OneDrive não foi indicado."); 
			return null;
		}
		if(refreshToken==null || refreshToken.equals("")) {
			System.out.println("O token de atualização para acesso ao repositório OneDrive não foi indicado.");
			return null;
		}
		
		return new OneDriveClient(clientId, clientSecret, refreshToken);
	}
	
	
	/**
	 * Substitui palavras de um documento .docx
	 * 
	 * @param document
	 * @param map mapeamento das palavras a serem substituidas
	 * 
	 * @author Maurício
	 * @since 13/12/2017
	 * 
	 * @see OneDriveUtils#replace(XWPFDocument, String, Object)
	 * 
	 * @fonte {@link https://stackoverflow.com/questions/22268898/replacing-a-text-in-apache-poi-xwpf/40226361#40226361}
	 */
	@Deprecated
	public static <V> void replace(XWPFDocument document, Map<String, V> map) {
	    List<XWPFParagraph> paragraphs = document.getParagraphs();
	    for (XWPFParagraph paragraph : paragraphs) {
	        replace(paragraph, map);
	    }
	}

	@Deprecated
	public static <V> void replace(XWPFDocument document, String searchText, V replacement) {
	    List<XWPFParagraph> paragraphs = document.getParagraphs();
	    for (XWPFParagraph paragraph : paragraphs) {
	        replace(paragraph, searchText, replacement);
	    }
	}

	private static <V> void replace(XWPFParagraph paragraph, Map<String, V> map) {
	    for (Map.Entry<String, V> entry : map.entrySet()) {
	        replace(paragraph, entry.getKey(), entry.getValue());
	    }
	}

	public static <V> void replace(XWPFParagraph paragraph, String searchText, V replacement) {
	    boolean found = true;
	    while (found) {
	    	//XXX:	    	System.out.print(".");
	        found = false;
	        int pos = paragraph.getText().indexOf(searchText);
	        if(replacement==null)
	        	replacement = (V)searchText;
	        if (pos >= 0) {
	            found = true;
	            Map<Integer, XWPFRun> posToRuns = getPosToRuns(paragraph);
	            XWPFRun run = posToRuns.get(pos);
	            if(run==null)
	            	break;
	            XWPFRun lastRun = posToRuns.get(pos + searchText.length() - 1);
	            int runNum = paragraph.getRuns().indexOf(run);
	            int lastRunNum = paragraph.getRuns().indexOf(lastRun);
	            String texts[] = replacement.toString().split("\n");
	            run.setText(texts[0], 0);
	            XWPFRun newRun = run;
	            for (int i = 1; i < texts.length; i++) {
	                newRun.addCarriageReturn();
	                newRun = paragraph.insertNewRun(runNum + i);
	                /*
	                    We should copy all style attributes
	                    to the newRun from run
	                    also from background color, ...
	                    Here we duplicate only the simple attributes...
	                 */
	                newRun.setText(texts[i]);
	                newRun.setBold(run.isBold());
	                newRun.setCapitalized(run.isCapitalized());
	                newRun.setCharacterSpacing(run.getCharacterSpacing());
	                newRun.setColor(run.getColor());
	                newRun.setDoubleStrikethrough(run.isDoubleStrikeThrough());
	                newRun.setEmbossed(run.isEmbossed());
	                newRun.setFontFamily(run.getFontFamily());
	                newRun.setFontSize(run.getFontSize());
	                newRun.setImprinted(run.isImprinted());
	                newRun.setItalic(run.isItalic());
	                newRun.setKerning(run.getKerning());
	                newRun.setShadow(run.isShadowed());
	                newRun.setSmallCaps(run.isSmallCaps());
	                newRun.setStrikeThrough(run.isStrikeThrough());
	                newRun.setSubscript(run.getSubscript());
	                newRun.setUnderline(run.getUnderline());
	            }
	            for (int i = lastRunNum + texts.length - 1; i > runNum + texts.length - 1; i--) {
	                paragraph.removeRun(i);
	            }
	        }// else {found = false;}
	    }
	}
	
	private static Map<Integer, XWPFRun> getPosToRuns(XWPFParagraph paragraph) {
	    int pos = 0;
	    Map<Integer, XWPFRun> map = new HashMap<Integer, XWPFRun>(10);
	    for (XWPFRun run : paragraph.getRuns()) {
	        String runText = run.text();
	        if (runText != null) {
	            for (int i = 0; i < runText.length(); i++) {
	                map.put(pos + i, run);
	            }
	            pos += runText.length();
	        }
	    }
	    return (map);
	}

}
