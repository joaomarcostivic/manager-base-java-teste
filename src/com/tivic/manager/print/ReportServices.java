package com.tivic.manager.print;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.lowagie.text.pdf.PdfWriter;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.ContextManager;
import com.tivic.manager.util.Util;

import flex.messaging.FlexContext;
import flex.messaging.FlexSession;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
import sol.dao.ResultSetMap;
import sol.util.ConfManager;
import sol.util.Result;

public class ReportServices {
	

	public static final int DESVIO_RELATORIO = -199;
	
	/**
	 * PDF
	 * @param name
	 * @param paramns
	 * @param resultset
	 * @return
	 */
	public static byte[] getPdfReport(String name, Map<String, Object> paramns, ResultSetMap resultset) {
		return getPdfReport(name, paramns, resultset, null);
	}
	
	public static byte[] getPdfReport(String name, Map<String, Object> paramns, ResultSetMap resultset, Connection connect){
		try {
			JasperPrint print = getJasperPrint(name, paramns, resultset, connect);
			return getPdfReport(print);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
	}
	public static byte[] getPdfReport(String name, Map<String, Object> paramns, List list) {
		return getPdfReport(name, paramns, list, null);
	}
	
	public static byte[] getPdfReport(String name, Map<String, Object> paramns, List list, Connection connect){
		try {
			JasperPrint print = getJasperPrint(name, paramns, list, connect);
			return getPdfReport(print);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
	}

	public static byte[] getPdfReport(JasperPrint print){
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
//	    	JRPdfExporter exporter = new JRPdfExporter();
//	    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
//	    	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
//	    	exporter.exportReport();
	    	
	    	JRPdfExporter exporter = new JRPdfExporter();
	    	exporter.setExporterInput(new SimpleExporterInput(print));
	    	exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
	    	SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
	    	configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
	    	configuration.setCompressed(true);
	    	exporter.setConfiguration(configuration);
	    	exporter.exportReport();
			
			return out.toByteArray();
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * XML (JRPXML)
	 * @param name
	 * @param paramns
	 * @param resultset
	 * @return
	 */
	public static byte[] getXmlReport(String name, Map<String, Object> paramns, ResultSetMap resultset) {
		return getXmlReport(name, paramns, resultset, null);
	}
	
	public static byte[] getXmlReport(String name, Map<String, Object> paramns, ResultSetMap resultset, Connection connect){
		try {
			JasperPrint print = getJasperPrint(name, paramns, resultset, connect);
			return getXmlReport(print);
		} 
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] getXmlReport(JasperPrint print){
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
	    	JRXmlExporter exporter = new JRXmlExporter();
	    	exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
			exporter.exportReport();
			
			return out.toByteArray();
		} 
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/** 
	 * XSL
	 * @param name
	 * @param paramns
	 * @param resultset
	 * @return
	 */
	public static byte[] getXlsReport(String name, Map<String, Object> paramns, ResultSetMap resultset) {
		return getXlsReport(name, paramns, resultset, null);
	}

	public static byte[] getXlsReport(String name, Map<String, Object> paramns, ResultSetMap resultset, Connection connect){
		try {
			
			if(paramns!=null)
				paramns.put("IS_IGNORE_PAGINATION ", true);
				
			JasperPrint print = getJasperPrint(name, paramns, resultset, connect);
			return getXlsReport(print);
		} 
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] getXlsReport(JasperPrint print){
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			JRXlsxExporter exporter = new JRXlsxExporter(); 		
			exporter.setExporterInput(new SimpleExporterInput(print));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
			
			SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration(); 
			configuration.setCollapseRowSpan(true);
			configuration.setRemoveEmptySpaceBetweenColumns(true);
			configuration.setRemoveEmptySpaceBetweenRows(true);
			configuration.setOnePagePerSheet(false);
			configuration.setDetectCellType(false);
			configuration.setWhitePageBackground(false);
			configuration.setIgnoreGraphics(true);
			configuration.setIgnoreCellBorder(true);
			configuration.setIgnoreCellBackground(true);
			configuration.setIgnorePageMargins(true);
			
			configuration.setIgnoreAnchors(true);
			configuration.setShowGridLines(false);
			configuration.setWrapText(false);
			
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			
			return out.toByteArray();
			
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			
//			JRXlsExporter exporter = new JRXlsExporter(); 			
//			exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, print); 
//			exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, out); 
//			exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
//			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
//			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
//			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
//			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.FALSE);
//			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
//			exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
//			exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
//			exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BACKGROUND, Boolean.TRUE);
//			exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
//
//			exporter.exportReport();
//			
//			return out.toByteArray();
		} 
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/** 
	 * DOC
	 * @param name
	 * @param paramns
	 * @param resultset
	 * @return
	 */
	public static byte[] getDocReport(String name, Map<String, Object> paramns, ResultSetMap resultset) {
		return getDocReport(name, paramns, resultset, null);
	}

	public static byte[] getDocReport(String name, Map<String, Object> paramns, ResultSetMap resultset, Connection connect){
		try {
			JasperPrint print = getJasperPrint(name, paramns, resultset, connect);
			return getDocReport(print);
		} 
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] getDocReport(String name, Map<String, Object> paramns, List list) {
		return getDocReport(name, paramns, list, null);
	}
	
	public static byte[] getDocReport(String name, Map<String, Object> paramns, List list, Connection connect){
		try {
			JasperPrint print = getJasperPrint(name, paramns, list, connect);
			return getDocReport(print);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static byte[] getDocReport(JasperPrint print){
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			JRDocxExporter exporter = new JRDocxExporter(); 
			exporter.setParameter(JRDocxExporterParameter.JASPER_PRINT, print); 
			exporter.setParameter(JRDocxExporterParameter.OUTPUT_STREAM, out); 
			exporter.exportReport();
			
			return out.toByteArray();
		} 
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * GERAL
	 */
	public static Result setReportSession(String name, Map<String, Object> paramns, ResultSetMap resultset){
		return setReportSession(name, paramns, resultset, null);
	}
	
	public static Result setReportSession(String name, Map<String, Object> paramns, ResultSetMap resultset, Connection connect){
		try {
			JasperPrint print = getJasperPrint(name, paramns, resultset, connect);
			
			if(print==null)
				return new Result(-1, "Relatório não pode ser preenchido.");
			
			FlexSession session = FlexContext.getFlexSession();
		    session.setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, print);
		    
		    return new Result(1, "Relatório atualizado na sessão.");
		} 
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, "Erro ao atualizar relatório na sessão.");
		}
	}
	
	public static Result setReportSession(HttpSession session, String name, Map<String, Object> paramns){
		return setReportSession(session, null, name, paramns, null, null);
	}
	
	public static Result setReportSession(HttpSession session, String name, Map<String, Object> paramns, ResultSetMap resultset){
		return setReportSession(session, null, name, paramns, resultset, null);
	}
	
	public static Result setReportSession(HttpSession session, JasperPrint print, Map<String, Object> paramns){
		return setReportSession(session, print, null, paramns, null, null);
	}
	
	public static Result setReportSession(HttpSession session, JasperPrint print, String name, Map<String, Object> paramns, ResultSetMap resultset, Connection connect){
		try {
			if(print == null)
				print = getJasperPrint(name, paramns, resultset, connect);
			
			if(print==null)
				return new Result(-1, "Relatório não pode ser preenchido.");
			
			session.setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, print);
		    
		    return new Result(1, "Relatório atualizado na sessão.");
		} 
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, "Erro ao atualizar relatório na sessão.");
		}
	}
	
	private static JasperPrint getJasperPrint(String name, Map<String, Object> paramns, 
			ResultSetMap resultset, Connection connect){
		return getJasperPrint(null, name, paramns, resultset, null, connect);
	}

	private static JasperPrint getJasperPrint(String name, Map<String, Object> paramns, 
			List list, Connection connect){
		return getJasperPrint(null, name, paramns, null, list, connect);
	}
	
	public static JasperPrint getJasperPrint(String reportPath, String name, Map<String, Object> paramns, 
			ResultSetMap resultset, Connection connect){
		return getJasperPrint(reportPath, name, paramns, resultset, null, connect);
	}

	public static JasperPrint getJasperPrint(String reportPath, String name, Map<String, Object> paramns, 
			List list, Connection connect){
		return getJasperPrint(reportPath, name, paramns, null, list, connect);
	}
	
	public static JasperPrint getJasperPrint(String reportPath, String name, Map<String, Object> paramns, 
			ResultSetMap resultset, List list, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			
			ConfManager conf = Util.getConfManager();
			
			String path = null;
			
			if(reportPath==null) {
					reportPath = conf.getProps().getProperty("REPORT_PATH");
					path = ContextManager.getRealPath()+"/"+reportPath;
			}
			else {
				path = reportPath;
			}
			
			JasperReport relatorioJasper;
	    	if(paramns==null) {
    			paramns = new HashMap<String, Object>();
    		}

	    	if(paramns.get("SUBREPORT_NAME")!=null && !paramns.get("SUBREPORT_NAME").equals(""))
	    		JasperCompileManager.compileReportToFile(path+"/"+paramns.get("SUBREPORT_NAME")+".jrxml");
	    	
	    	if(paramns.get("SUBREPORT_NAMES")!=null && paramns.get("SUBREPORT_NAMES") instanceof ArrayList<?>) {
	    		ArrayList<String> subreports = (ArrayList<String>)paramns.get("SUBREPORT_NAMES");
	    		for (String subreportName : subreports) {
    				JasperCompileManager.compileReportToFile(path+"/"+subreportName+".jrxml");
	    		}
	    	}
	    		    	
	    	
	    	if(paramns.get("DYNAMIC_COLUMNS")!=null) {
	    		JasperDesign jasperDesign = getJasperDesign(path+"/"+name);
	    		
	    		JRDesignFrame headerColumn = new JRDesignFrame();
	    		JRDesignFrame detailColumn = new JRDesignFrame();
	    		JRDesignFrame totalColumn = new JRDesignFrame();
	    		JRDesignBand headerBand = new JRDesignBand();
	    		JRDesignBand detailBand = new JRDesignBand();
	    		JRDesignBand totalBand = new JRDesignBand();;
	    		String headerColumnKey = (String)paramns.get("DYNAMIC_COLUMNS_HEADER_KEY");
	    		String detailColumnKey = (String)paramns.get("DYNAMIC_COLUMNS_DETAIL_KEY");
	    		String totalColumnKey = (String)paramns.get("DYNAMIC_COLUMNS_TOTAL_KEY");
	    		
	    		org.apache.commons.collections4.iterators.ArrayIterator<?> bandIterator = new org.apache.commons.collections4.iterators.ArrayIterator<>( jasperDesign.getAllBands() );
	    		JRDesignBand band;
	    		while( bandIterator.hasNext() ){
	    			band = (JRDesignBand) bandIterator.next();
	    			JRElement[] elements = band.getElements();
	    			JRElement element;
	    			for( int i=0; i<elements.length; i++  ){
	    				element = elements[i];
	    				if( element.getKey() instanceof String ){
	    					if( element.getKey().equals( headerColumnKey ) ){
	    						headerBand = band;
	    						headerColumn = (JRDesignFrame) element;
	    					}else if( element.getKey().equals( detailColumnKey ) ){
	    						detailBand = band;
	    						detailColumn = (JRDesignFrame) element;
	    					}else if( element.getKey().equals( totalColumnKey ) ){
	    						totalBand = band;
	    						totalColumn = (JRDesignFrame) element;
	    					}
	    				}
	    				
	    			}
	    			
	    		}
	    		 
	            ArrayList< HashMap<String, Object> > columns = (ArrayList< HashMap<String, Object> >)paramns.get("DYNAMIC_COLUMNS");
	            
	            int xPosHeader = 0;
	            int yPosHeader = 0;
	            int xPosDetail = 0;
	            int yPosDetail = 0;
	            int xPosTotal = 0;
	            int yPosTotal = 0;
	            
	            final int HEADER_COLUMN_WIDTH = (int) Math.floor( (double) headerColumn.getWidth() / (double) columns.size() );
	            final int HEADER_COLUMN_HEIGTH = headerColumn.getHeight();
	            final int HEADER_COLUMN_WIDTH_LIMIT = headerColumn.getX()+headerColumn.getWidth();
	            
	            final int DETAIL_COLUMN_WIDTH = (int) Math.floor( (double) detailColumn.getWidth() / (double) columns.size() );
	            final int DETAIL_COLUMN_HEIGHT = detailColumn.getHeight();
	            final int DETAIL_COLUMN_WIDTH_LIMIT = detailColumn.getX()+detailColumn.getWidth();

	            final int TOTAL_COLUMN_WIDTH = (int) Math.floor( (double) totalColumn.getWidth() / (double) columns.size() );
	            final int TOTAL_COLUMN_HEIGHT = totalColumn.getHeight();
	            final int TOTAL_COLUMN_WIDTH_LIMIT = totalColumn.getX()+totalColumn.getWidth();
	            xPosHeader = headerColumn.getX();
	            yPosHeader = headerColumn.getY();

	            xPosDetail = detailColumn.getX();
	            yPosDetail = detailColumn.getY();

	            xPosTotal = totalColumn.getX();
	            yPosTotal = totalColumn.getY();
	            String label, style, styleField, totalFieldExpression, fieldName, fieldExpression, fieldClass;
	            for (int i = 0; i < columns.size(); i++) {
	            	
	            	HashMap<String, Object> objColumn = (HashMap<String, Object>)columns.get(i);

	            	label = (String) objColumn.get("LABEL");
	            	fieldName = (String) objColumn.get("FIELD");
	            	fieldClass = (String) objColumn.get("FIELD_CLASS");
	            	fieldExpression = (objColumn.containsKey("FIELD_EXPRESSION"))? (String) objColumn.get("FIELD_EXPRESSION"): null;
	            	
	            	totalFieldExpression = (objColumn.containsKey("TOTAL_FIELD"))? (String) objColumn.get("TOTAL_FIELD"):"";
	            	
	            	//style = (objColumn.containsKey("STYLE"))? (String) objColumn.get("STYLE"):"DynamicDefault";
	            	//styleField = (objColumn.containsKey("STYLE_FIELD"))? (String) objColumn.get("STYLE_FIELD"):"DynamicDefault";
	            	
	            	JRDesignField field = new JRDesignField();
	            	field.setName( fieldName );
	            	field.setValueClassName(fieldClass);
	            	jasperDesign.addField(field);
	            	
	            	//ADICIONA A CÉLULA DE CABEÇALHO
	            	JRDesignStaticText colHeaderField = new JRDesignStaticText();
	                colHeaderField.setX(xPosHeader);
	                colHeaderField.setY(yPosHeader);
	                //colHeaderField.setStyleNameReference(style);
	                colHeaderField.setWidth(HEADER_COLUMN_WIDTH);
	                colHeaderField.setHeight(HEADER_COLUMN_HEIGTH);
 	                colHeaderField.setText( label );
 	                colHeaderField.setBold(true);
 	                colHeaderField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER); //setHorizontalAlignment(HorizontalAlignEnum.CENTER);
 	                colHeaderField.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE); //setVerticalAlignment(VerticalAlignEnum.MIDDLE);
 	                headerBand.addElement(colHeaderField);
	            
//	                Adiciona linha lateral como separador 
//	                JRDesignLine lineHeader = new JRDesignLine();
//	                lineHeader.setHeight( HEADER_COLUMN_HEIGTH );
//	            	lineHeader.setWidth(1);
//	            	lineHeader.setX( xPosHeader+HEADER_COLUMN_WIDTH );
//	            	lineHeader.setY(yPosHeader);
//	                
//	            	if( lineHeader.getX() < HEADER_COLUMN_WIDTH_LIMIT  )
//	                	headerBand.addElement(lineHeader);
 	                
 	                //ADICIONA A CÉLULA DE VALOR
	                JRDesignTextField textField = new JRDesignTextField();
	                textField.setX(xPosDetail);
	                textField.setY(yPosDetail);
	                //textField.setStyleNameReference(styleField);
	                textField.setWidth(DETAIL_COLUMN_WIDTH);
	                textField.setHeight(DETAIL_COLUMN_HEIGHT);
	                
	                textField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER); //setHorizontalAlignment(HorizontalAlignEnum.CENTER);
	                textField.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE); //setVerticalAlignment(VerticalAlignEnum.MIDDLE);
	                JRDesignExpression expression = new JRDesignExpression();
	            	//expression.setValueClass(java.lang.String.class);
	            	expression.setText(fieldExpression != null ? fieldExpression : "$F{"+fieldName+"}");
	                
	                textField.setExpression( expression );
	                textField.setBlankWhenNull(true);	                
	                
	                detailBand.addElement(textField);
	                
//	                Adiciona linha lateral como separador 
//	                JRDesignLine lineDetail = new JRDesignLine();
//	                lineDetail.setHeight( DETAIL_COLUMN_HEIGHT );
//	            	lineDetail.setWidth(1);
//	            	lineDetail.setX( xPosDetail+DETAIL_COLUMN_WIDTH );
//	            	lineDetail.setY(yPosDetail);
//	            	
//	            	if( lineDetail.getX() < DETAIL_COLUMN_WIDTH_LIMIT  )
//	                	detailBand.addElement(lineDetail);
	                //ADICIONA A CÉLULA DE SUMARIZAÇÃO
	                if( totalFieldExpression != null ){
	                	JRDesignTextField totalField = new JRDesignTextField();
		                totalField.setX(xPosTotal);
		                totalField.setY(yPosTotal);
		               // totalField.setStyleNameReference(styleField);
		                totalField.setWidth(TOTAL_COLUMN_WIDTH);
		                totalField.setHeight(TOTAL_COLUMN_HEIGHT);
		                totalField.setExpression( new JRDesignExpression( totalFieldExpression ) );
		                totalBand.addElement( totalField );
	                }
	                	                
	                xPosHeader +=  HEADER_COLUMN_WIDTH;
	                xPosDetail +=  DETAIL_COLUMN_WIDTH;
	                xPosTotal  +=  TOTAL_COLUMN_WIDTH;
	            }
	            relatorioJasper =  JasperCompileManager.compileReport(jasperDesign);
	    	}
	    	else {
	    		relatorioJasper = getJasperReport(path+"/"+name);
	    	}
	    	
			JasperPrint print = null;
		
	    	if(resultset!=null) {
	    		
	    		if(paramns.get("SUBREPORT_DIR") == null || paramns.get("SUBREPORT_DIR").equals(""))
	    			paramns.put("SUBREPORT_DIR", path);
	    		JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(resultset.getLines());
	    			    		
	    		print = JasperFillManager.fillReport(relatorioJasper, paramns, datasource);
    		}
	    	else if(list!=null) {
	    		if(paramns.get("SUBREPORT_DIR") == null || paramns.get("SUBREPORT_DIR").equals(""))
	    			paramns.put("SUBREPORT_DIR", path);
	    		JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(list);
	    			    		
	    		print = JasperFillManager.fillReport(relatorioJasper, paramns, datasource);
    		}
    		else{
    			if (isConnectionNull) {
    				connect = Conexao.conectar();
    				connect.setAutoCommit(false);
    			}
    			print = JasperFillManager.fillReport(relatorioJasper, paramns, connect);
    		}
	    	
	    	return print;
		} 
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static JasperReport getJasperReport(String filePath) {
		try {
			JasperReport relatorioJasper;
	    	
			if(new File(filePath+".jasper").exists()) {
				relatorioJasper = (JasperReport)JRLoader.loadObjectFromFile(filePath+".jasper");
			}
			else {
				relatorioJasper = JasperCompileManager.compileReport(filePath+".jrxml");
			}
			
			return relatorioJasper;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private static JasperDesign getJasperDesign(String filePath) {
		try {
			JasperDesign design = JRXmlLoader.load(filePath+".jrxml");
			
			return design;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
