package com.tivic.manager.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.conf.jwt.JWTConf;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.AgenteDAO;
import com.tivic.manager.mob.BoatServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.seg.ModuloServices;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.seg.UsuarioDTO;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.ddl.DDLManager;
import com.tivic.manager.util.ddl.DDLManagerFactory;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.resource.ResourceManager;
import sol.util.ConfManager;
import sol.util.Result;

import java.security.*;
import java.math.*;

public class Util extends sol.util.Util	{
	/* Perfis de Acesso */
	public static final int PF_ADMINISTRADOR = 0;
	public static final int PF_PROFESSOR     = 1;
	public static final int PF_ALUNO         = 2;


//	private static ConfManager confManager         = null;
	private static ResourceManager resourceManager = null;
	/** Constante MASK_CURRENCY. **/
	public final static String MASK_CURRENCY         = "#,###,##0.00";
	public final static String MASK_CURRENCY_PTBR    = "#.###.##0,00";
	public final static String MASK_ENDERECO_DEFAULT = "#NM_TIPO_LOGRADOURO# #NM_LOGRADOURO#, #NR_DOMICILIO#, #NM_COMPLEMENTO# - #NM_MUNICIPIO#/#SG_ESTADO#";

	private static String packageRoot = "com.tivic.manager";
	
	
	public static String[] meses = {"Janeiro", "Fevereiro", "MarÃ§o", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
	
	static {
		try {
//			InputStream inDat = null;
//			if(Util.class.getResource("/"+getPackageRootPath()+"/conf/manager.dat") != null)
//				inDat = Util.class.getResourceAsStream("/"+getPackageRootPath()+"/conf/manager.dat");
//			confManager = new ConfManager(Util.class.getResourceAsStream("/"+getPackageRootPath()+"/conf/manager.conf"), inDat);
			
			/* Inicializando a biblioteca do OpenCV */
			if(initOpenCVLib())
				OpencvUtils.loadNativeLibrary();

			/* Inicializando a biblioteca do OpenCV */
			if(initOpenCVStreamServer())
				OpencvUtils.initStreamServer();
			
			/* Sincroniza de usu?rios da base antiga de transito */
			UsuarioServices.syncBaseAntiga();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
		}
	}
	
	public static String getPackageRoot() {
		return packageRoot;
	}
	
	public static String getPackageRootPath() {
		return packageRoot.replaceAll("\\.", "/");
	}
	
	public static String getSystemName() {
		return ManagerConf.getInstance().get("NAME");
	}

	public static boolean initTasks() {
		return ManagerConf.getInstance().getAsBoolean("INIT_TASKS");
	}
	
	public static boolean initDestinations() {
		return ManagerConf.getInstance().getAsBoolean("INIT_DESTINATIONS");
	}
	
	public static int getDestinationInitType() {
		return ManagerConf.getInstance().getAsInteger("INIT_DESTINATIONS");
	}
	
	public static boolean initQuark() {
		return ManagerConf.getInstance().getAsBoolean("INIT_QUARK");
	}
	
	public static boolean initOpenCVLib() {
		return ManagerConf.getInstance().getAsBoolean("INIT_OPENCV_LIB");
	}
	
	public static boolean initOpenCVStreamServer() {
		return ManagerConf.getInstance().getAsBoolean("INIT_OPENCV_STREAM_SERVER");
	}

	public static boolean initDeviceMonitor() {
		return ManagerConf.getInstance().getAsBoolean("INIT_DEVICE_MONITOR");
	}

	@Deprecated
	public static boolean isJwtEnabled() {
		return JWTConf.getInstance().isEnabled();
	}

	@Deprecated
	public static ConfManager getConfManagerFromConfigurationFile() {
		try {
			return ManagerConf.getInstance();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * Inicializa modulos, cria ou atualiza parametros de modulos, executa DDLs.  
	 * @return
	 */
	public static int init() {
		try {
			ModuloServices.init();
			
			com.tivic.manager.grl.ParametroServices.init();
			
			UsuarioServices.init();
			
			DDLManager ddlManager = DDLManagerFactory.generate();
			ddlManager.executar();
			
			System.out.println("Inicializacao concluida com sucesso!");
			
			return 1;
		}
		catch(Exception e) {
			registerLog(e);
			e.printStackTrace(System.out);
			return -1;
		}
	}

	public static ArrayList<String> getStructureSecurities() {
		return null;
	}

	public static Timestamp convCalendarToTimestamp(GregorianCalendar data) {
		return data==null ? null : new Timestamp(data.getTimeInMillis());
	}

    public static GregorianCalendar convTimestampToCalendar(Timestamp data) {
    	return data==null ? null : convStringToCalendar(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(data));
    }

    public static String formatDate(GregorianCalendar data, String mask) {
    	
    	if(data==null)
    		return "";
    	
		Timestamp timestamp = new Timestamp(data.getTimeInMillis());
    	return formatDate(timestamp, mask);
	}

    public static String formatDate(Timestamp data, String mask) {
    	
    	if(data==null)
    		return "";
    	
		return new SimpleDateFormat(mask).format(data);
	}
    
    /*NÃÆÃÂ£o se adequada a qualquer plataforma*/
    @Deprecated
    public static String retornaDataNFCe(GregorianCalendar calendar) {  
        XMLGregorianCalendar xmlCalendar = null;  
        try {  
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);  
            xmlCalendar.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);  
          
            return(xmlCalendar.toString());  
        } catch (DatatypeConfigurationException ex) {
        	ex.printStackTrace();
            return null;  
        }  
    }  
    
    public static GregorianCalendar convStringToCalendar(String data) {
    	return convStringToCalendar(data, null);
    }

    public static GregorianCalendar convStringToCalendar(String data, GregorianCalendar defaultDate) {
		try {
	        if(data==null || data.trim().equals(""))
	        	return null;
	        StringTokenizer token = new StringTokenizer(data, " ");
	        String d = token.nextToken(), h = "";
	        int hora =0, min = 0, sec = 0, millisec=0;
	        if(token.hasMoreTokens())	{
	        	h	=	token.nextToken();
				StringTokenizer token2	= new StringTokenizer(h, ":");
				hora =	Integer.parseInt(token2.nextToken().trim());
				min  =	Integer.parseInt(token2.nextToken().trim());
				String token3 = token2.hasMoreTokens() ? token2.nextToken() : null;
				sec  =	token3==null ? 0 : Integer.parseInt(token3.trim());
				token3 = token2.hasMoreTokens() ? token2.nextToken() : null;
				millisec  =	token3==null ? 0 : Integer.parseInt(token3.trim());
			}
        	token = new StringTokenizer(d, "/");
        	int dia = Integer.parseInt(token.nextToken());
        	int mes = Integer.parseInt(token.nextToken());
        	int ano = Integer.parseInt(token.nextToken());

	        GregorianCalendar date = new GregorianCalendar(ano, mes-1, dia, hora, min, sec);
	        date.set(Calendar.MILLISECOND, millisec);
	        return date;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return defaultDate;
		}
	}
    
    public static GregorianCalendar convDateToCalendar(Date data) {
    	
    	long timeInMillis = data.getTime();
    	
    	GregorianCalendar newDate = new GregorianCalendar();
    	newDate.setTimeInMillis(timeInMillis);
    	
    	return newDate;
    }
    
    public static GregorianCalendar convStringToCalendar2(String data) {
		try {

	        if(data==null || data.trim().equals(""))
	        	return null;
	        StringTokenizer token = new StringTokenizer(data, "T");
	        String d = token.nextToken(), h = "";
	        int hora =0, min = 0, sec = 0, millisec=0;
	        if(token.hasMoreTokens())	{
	        	h	=	token.nextToken();
				StringTokenizer token2	= new StringTokenizer(h, ":");
				hora =	Integer.parseInt(token2.nextToken().trim());
				min  =	Integer.parseInt(token2.nextToken().trim());
				String token3 = token2.hasMoreTokens() ? token2.nextToken() : null;
				if(token3.contains(".")){
					StringTokenizer token4 = new StringTokenizer(token3, ".");
					sec  =	token3==null ? 0 : Integer.parseInt(token4.nextToken());
				}
				else if(token3.contains("-")){
					StringTokenizer token4 = new StringTokenizer(token3, "-");
					sec  =	token3==null ? 0 : Integer.parseInt(token4.nextToken());
				}
				else{
					sec  =	token3==null ? 0 : Integer.parseInt(token3.trim());
					token3 = token2.hasMoreTokens() ? token2.nextToken() : null;
					millisec  =	token3==null ? 0 : Integer.parseInt(token3.trim());
				}
			}
        	token = new StringTokenizer(d, "-");
        	int ano = Integer.parseInt(token.nextToken());
        	int mes = Integer.parseInt(token.nextToken());
        	int dia = Integer.parseInt(token.nextToken());

	        GregorianCalendar date = new GregorianCalendar(ano, mes-1, dia, hora, min, sec);
	        date.set(Calendar.MILLISECOND, millisec);
	        return date;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
    
    public static GregorianCalendar convStringToCalendar3(String data) {
    	return convStringToCalendar3(data, null);
    }

    public static GregorianCalendar convStringToCalendar3(String data, GregorianCalendar defaultDate) {
		try {

			if(data == null || data.trim().length() < 20)
				return null;
			
			data = data.substring(0, 19);
			
			if(data==null || data.trim().equals(""))
	        	return null;
	        StringTokenizer token = new StringTokenizer(data, " ");
	        String d = token.nextToken(), h = "";

	        int hora =0, min = 0, sec = 0, millisec=0;
	        if(token.hasMoreTokens())	{
	        	h	=	token.nextToken();
				StringTokenizer token2	= new StringTokenizer(h, ":");
				hora =	Integer.parseInt(token2.nextToken().trim());
				min  =	Integer.parseInt(token2.nextToken().trim());
				String token3 = token2.hasMoreTokens() ? token2.nextToken() : null;
				sec  =	token3==null ? 0 : Integer.parseInt(token3.trim());
				token3 = token2.hasMoreTokens() ? token2.nextToken() : null;
				millisec  =	token3==null ? 0 : Integer.parseInt(token3.trim());
			}
        	token = new StringTokenizer(d, "-");
        	int ano = Integer.parseInt(token.nextToken());
        	int mes = Integer.parseInt(token.nextToken());
        	int dia = Integer.parseInt(token.nextToken());
        	
			GregorianCalendar date = new GregorianCalendar(ano, mes-1, dia, hora, min, sec);
	        date.set(Calendar.MILLISECOND, millisec);
	        return date;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return defaultDate;
		}
	}
    
    public static GregorianCalendar convStringToCalendar4(String data) {
    	return convStringToCalendar4(data, null);
    }

    public static GregorianCalendar convStringToCalendar4(String data, GregorianCalendar defaultDate) {
		try {
	        if(data==null || data.trim().equals(""))
	        	return null;
	        StringTokenizer token = new StringTokenizer(data, "/");
        	int dia = Integer.parseInt(token.nextToken());
        	int mes = Integer.parseInt(token.nextToken());
        	int ano = Integer.parseInt(token.nextToken());

	        GregorianCalendar date = new GregorianCalendar(ano, mes-1, dia);
	        return date;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return defaultDate;
		}
	}

    public static GregorianCalendar convStringToCalendar5(String data) {
    	return convStringToCalendar5(data, null);
    }

    public static GregorianCalendar convStringToCalendar5(String data, GregorianCalendar defaultDate) {
		try {
	        if(data==null || data.trim().equals(""))
	        	return null;
	        StringTokenizer token = new StringTokenizer(data, " ");
	        int dia = Integer.parseInt(token.nextToken());
        	int mes = Integer.parseInt(token.nextToken());
        	int ano = Integer.parseInt(token.nextToken());

	        GregorianCalendar date = new GregorianCalendar(ano, mes-1, dia);
	        return date;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return defaultDate;
		}
	}
    
    public static Date convStringToDate(String data) {
    	return convStringToDate(data, false);
    }
    
    public static Date convStringToDate(String data, boolean correcaoAno) {
    	try {
	        if(data==null || data.trim().equals(""))
	        	return null;
	        StringTokenizer token = new StringTokenizer(data, " ");
	        String d = token.nextToken(), h = "";
	        int hora =0, min = 0, sec = 0, millisec=0;
	        if(token.hasMoreTokens())	{
	        	h	=	token.nextToken();
				StringTokenizer token2	= new StringTokenizer(h, ":");
				hora =	Integer.parseInt(token2.nextToken().trim());
				min  =	Integer.parseInt(token2.nextToken().trim());
				String token3 = token2.hasMoreTokens() ? token2.nextToken() : null;
				sec  =	token3==null ? 0 : Integer.parseInt(token3.trim());
				token3 = token2.hasMoreTokens() ? token2.nextToken() : null;
				millisec  =	token3==null ? 0 : Integer.parseInt(token3.trim());
			}
        	token = new StringTokenizer(d, "/");
        	int dia = Integer.parseInt(token.nextToken());
        	int mes = Integer.parseInt(token.nextToken());
        	int ano = Integer.parseInt(token.nextToken());

        	GregorianCalendar date = new GregorianCalendar();
			
			date.set(Calendar.DAY_OF_MONTH, dia);
			date.set(Calendar.MONTH, mes-1);
			date.set(Calendar.YEAR, (correcaoAno ? ano - 1900 : ano));
			
			date.set(Calendar.HOUR_OF_DAY, hora);
			date.set(Calendar.MINUTE, min);
			date.set(Calendar.SECOND, sec);
			date.set(Calendar.MILLISECOND, millisec);
	        
	        return new Date(date.getTimeInMillis());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Date();
		}
	}
    
    public static GregorianCalendar convStringSemFormatacaoToGregorianCalendar(String data) {
    	try {
	        if(data==null || data.trim().equals(""))
	        	return null;
	        
	        System.out.println("data = " + data);
	        
	        String[] setData = data.split("");
	        
	        GregorianCalendar date = new GregorianCalendar();
	        
	        date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(setData[0] + setData[1]));
	        date.set(Calendar.MONTH, (Integer.parseInt(setData[2] + setData[3])-1));
	        date.set(Calendar.YEAR, Integer.parseInt(setData[4] + setData[5] + setData[6] + setData[7]));
	        
	        date.set(Calendar.HOUR_OF_DAY, 0);
	        date.set(Calendar.MINUTE, 0);
	        date.set(Calendar.SECOND, 0);
	        
	        return date;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
    
    public static GregorianCalendar convStringToGregorianTimestampFormat(String data) {
    	try {
	        if(data==null || data.trim().equals(""))
	        	return null;
	        
	        String[] setData = data.split("");
	        
	        GregorianCalendar date = new GregorianCalendar();
	        
	        date.set(Calendar.YEAR, Integer.parseInt(setData[0] + setData[1] + setData[2] + setData[3]));
	        date.set(Calendar.MONTH, (Integer.parseInt(setData[5] + setData[6])-1));
	        date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(setData[8] + setData[9]));
	        
	        date.set(Calendar.HOUR_OF_DAY, 0);
	        date.set(Calendar.MINUTE, 0);
	        date.set(Calendar.SECOND, 0);
	        
	        return date;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
    
    public static GregorianCalendar convStringSemFormatacaoReverseSToGregorianCalendar(String data) {
    	try {
	        if(data==null || data.trim().equals(""))
	        	return null;
	        
	        String[] setData = data.split("");
	        
	        GregorianCalendar date = new GregorianCalendar();
	        
	        date.set(Calendar.YEAR, Integer.parseInt(setData[0] + setData[1] + setData[2] + setData[3]));
	        date.set(Calendar.MONTH, (Integer.parseInt(setData[4] + setData[5])-1));
	        date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(setData[6] + setData[7]));
	        
	        date.set(Calendar.HOUR_OF_DAY, 0);
	        date.set(Calendar.MINUTE, 0);
	        date.set(Calendar.SECOND, 0);
	        
	        return date;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
    

    public static GregorianCalendar convStringSemFormatacaoReverseSToGregorianCalendarHora(String data) {
    	try {
	        if(data==null || data.trim().equals(""))
	        	return null;
	        
	        String[] setData = data.split("");
	        
	        GregorianCalendar date = new GregorianCalendar();
	        
	        date.set(Calendar.YEAR, 1970);
	        date.set(Calendar.MONTH, 0);
	        date.set(Calendar.DAY_OF_MONTH, 1);
	        
	        date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(setData[0] + setData[1]));
	        date.set(Calendar.MINUTE, Integer.parseInt(setData[2] + setData[3])-1);
	        
	        return date;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
    
    
    public static String convCalendarStringCompleto(GregorianCalendar data){
    	try {
    	
    		String dataStr = "";
    		
    		dataStr += ((data.get(Calendar.DAY_OF_MONTH)) < 10) ? "0" + data.get(Calendar.DAY_OF_MONTH) + "/" : data.get(Calendar.DAY_OF_MONTH) + "/";
    		dataStr += ((data.get(Calendar.MONTH) + 1) < 10) ? "0" + (data.get(Calendar.MONTH) + 1) + "/" : (data.get(Calendar.MONTH) + 1) + "/";
    		dataStr += data.get(Calendar.YEAR) + " ";
    		dataStr += ((data.get(Calendar.HOUR_OF_DAY)) < 10) ? "0" + data.get(Calendar.HOUR_OF_DAY) + ":" : data.get(Calendar.HOUR_OF_DAY) + ":";
    		dataStr += ((data.get(Calendar.MINUTE)) < 10) ? "0" + data.get(Calendar.MINUTE) + ":" : data.get(Calendar.MINUTE) + ":";
    		dataStr += ((data.get(Calendar.SECOND)) < 10) ? "0" + data.get(Calendar.SECOND) : data.get(Calendar.SECOND);
    		
    		return dataStr;
    		
    	}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
    }
    
    public static String convCalendarStringHourMinute(GregorianCalendar data){
    	try {
    	
    		String dataStr = "";
    		
    		dataStr += ((data.get(Calendar.HOUR_OF_DAY)) < 10) ? "0" + data.get(Calendar.HOUR_OF_DAY) + ":" : data.get(Calendar.HOUR_OF_DAY) + ":";
    		dataStr += ((data.get(Calendar.MINUTE)) < 10) ? "0" + data.get(Calendar.MINUTE) : data.get(Calendar.MINUTE);
    		
    		return dataStr;
    		
    	}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
    }
    
    public static String convCalendarString2(GregorianCalendar data){
    	try {
    	
    		String dataStr = "";
    		
    		dataStr += data.get(Calendar.YEAR) + "-";
    		dataStr += ((data.get(Calendar.MONTH) + 1) < 10) ? "0" + (data.get(Calendar.MONTH) + 1) + "-" : (data.get(Calendar.MONTH) + 1) + "-";
    		dataStr += ((data.get(Calendar.DAY_OF_MONTH)) < 10) ? "0" + data.get(Calendar.DAY_OF_MONTH) : data.get(Calendar.DAY_OF_MONTH);
    		dataStr += "T";
    		dataStr += ((data.get(Calendar.HOUR_OF_DAY)) < 10) ? "0" + data.get(Calendar.HOUR_OF_DAY) + ":" : data.get(Calendar.HOUR_OF_DAY) + ":";
    		dataStr += ((data.get(Calendar.MINUTE)) < 10) ? "0" + data.get(Calendar.MINUTE) + ":" : data.get(Calendar.MINUTE) + ":";
    		dataStr += ((data.get(Calendar.SECOND)) < 10) ? "0" + data.get(Calendar.SECOND) : data.get(Calendar.SECOND);
    		
    		return dataStr;
    		
    	}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
    }
    
    public static String convCalendarString3(GregorianCalendar data){
    	try {
    	
    		if(data == null)
    			return "";
    		
    		String dataStr = "";
    		
    		dataStr += ((data.get(Calendar.DAY_OF_MONTH)) < 10) ? "0" + data.get(Calendar.DAY_OF_MONTH) + "/" : data.get(Calendar.DAY_OF_MONTH) + "/";
    		dataStr += ((data.get(Calendar.MONTH) + 1) < 10) ? "0" + (data.get(Calendar.MONTH) + 1) + "/" : (data.get(Calendar.MONTH) + 1) + "/";
    		dataStr += data.get(Calendar.YEAR);
    		
    		return dataStr;
    		
    	}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
    }
    
    public static String convCalendarString4(GregorianCalendar data){
    	try {
    	
    		if(data == null)
    			return "";
    		
    		String dataStr = "";
    		
    		dataStr += ((data.get(Calendar.DAY_OF_MONTH)) < 10) ? "0" + data.get(Calendar.DAY_OF_MONTH) + "/" : data.get(Calendar.DAY_OF_MONTH) + "/";
    		dataStr += ((data.get(Calendar.MONTH) + 1) < 10) ? "0" + (data.get(Calendar.MONTH) + 1) + "/" : (data.get(Calendar.MONTH) + 1) + "/";
    		dataStr += data.get(Calendar.YEAR) + " ";
    		
    		dataStr += ((data.get(Calendar.HOUR_OF_DAY)) < 10) ? "0" + data.get(Calendar.HOUR_OF_DAY) + ":" : data.get(Calendar.HOUR_OF_DAY) + ":";
    		dataStr += data.get(Calendar.MINUTE);
    		
    		return dataStr;
    		
    	}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
    }
    
    public static String convCalendarStringSql(GregorianCalendar data){
    	try {
    	
    		String dataStr = "";
    		
    		dataStr += data.get(Calendar.YEAR) + "-";
    		dataStr += ((data.get(Calendar.MONTH) + 1) < 10) ? "0" + (data.get(Calendar.MONTH) + 1) + "-" : (data.get(Calendar.MONTH) + 1) + "-";
    		dataStr += ((data.get(Calendar.DAY_OF_MONTH)) < 10) ? "0" + data.get(Calendar.DAY_OF_MONTH) : data.get(Calendar.DAY_OF_MONTH);
    		
    		return dataStr;
    		
    	}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
    }
    
    public static String convCalendarStringSqlCompleto(GregorianCalendar data){
    	try {
    	
    		String dataStr = "";
    		
    		dataStr += data.get(Calendar.YEAR) + "-";
    		dataStr += ((data.get(Calendar.MONTH) + 1) < 10) ? "0" + (data.get(Calendar.MONTH) + 1) + "-" : (data.get(Calendar.MONTH) + 1) + "-";
    		dataStr += ((data.get(Calendar.DAY_OF_MONTH)) < 10) ? "0" + data.get(Calendar.DAY_OF_MONTH) + " " : data.get(Calendar.DAY_OF_MONTH) + " ";
    		dataStr += ((data.get(Calendar.HOUR_OF_DAY)) < 10) ? "0" + data.get(Calendar.HOUR_OF_DAY) + ":" : data.get(Calendar.HOUR_OF_DAY) + ":";
    		dataStr += ((data.get(Calendar.MINUTE)) < 10) ? "0" + data.get(Calendar.MINUTE) + ":" : data.get(Calendar.MINUTE) + ":";
    		dataStr += ((data.get(Calendar.SECOND)) < 10) ? "0" + data.get(Calendar.SECOND) : data.get(Calendar.SECOND);
    		
    		return dataStr;
    		
    	}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
    }
    
    public static String convCalendarStringIso(GregorianCalendar data){
    	try {
    	
    		if(data == null)
    			return "";
    			
    		String dataStr = "";
    		
    		dataStr += data.get(Calendar.YEAR) + "-";
    		dataStr += ((data.get(Calendar.MONTH) + 1) < 10) ? "0" + (data.get(Calendar.MONTH) + 1) + "-" : (data.get(Calendar.MONTH) + 1) + "-";
    		dataStr += ((data.get(Calendar.DAY_OF_MONTH)) < 10) ? "0" + data.get(Calendar.DAY_OF_MONTH) + "T" : data.get(Calendar.DAY_OF_MONTH) + "T";
    		dataStr += ((data.get(Calendar.HOUR_OF_DAY)) < 10) ? "0" + data.get(Calendar.HOUR_OF_DAY) + ":" : data.get(Calendar.HOUR_OF_DAY) + ":";
    		dataStr += ((data.get(Calendar.MINUTE)) < 10) ? "0" + data.get(Calendar.MINUTE) + ":" : data.get(Calendar.MINUTE) + ":";
    		dataStr += ((data.get(Calendar.SECOND)) < 10) ? "0" + data.get(Calendar.SECOND) : data.get(Calendar.SECOND);
    		dataStr += "-03:00";
    		return dataStr;
    	}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
    }
    
    public static String convCalendarStringDataSp(GregorianCalendar data){
    	try {
    	
    		if(data == null)
    			return "";
    			
    		String dataStr = "";
    		
    		dataStr += ((data.get(Calendar.DAY_OF_MONTH)) < 10) ? "0" + data.get(Calendar.DAY_OF_MONTH) + "/" : data.get(Calendar.DAY_OF_MONTH) + "/";
    		dataStr += ((data.get(Calendar.MONTH) + 1) < 10) ? "0" + (data.get(Calendar.MONTH) + 1) + "/" : (data.get(Calendar.MONTH) + 1) + "/";
    		dataStr += data.get(Calendar.YEAR) + " ";
    		dataStr += ((data.get(Calendar.HOUR_OF_DAY)) < 10) ? "0" + data.get(Calendar.HOUR_OF_DAY) + ":" : data.get(Calendar.HOUR_OF_DAY) + ":";
    		dataStr += ((data.get(Calendar.MINUTE)) < 10) ? "0" + data.get(Calendar.MINUTE) + ":" : data.get(Calendar.MINUTE) + ":";
    		dataStr += ((data.get(Calendar.SECOND)) < 10) ? "0" + data.get(Calendar.SECOND) : data.get(Calendar.SECOND);
    		return dataStr;
    	}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
    }

    public static String convCalendarStringDataSemSeparador(GregorianCalendar data){
    	try {
    	
    		if(data == null)
    			return "";
    			
    		String dataStr = "";
    		dataStr += data.get(Calendar.YEAR);
    		dataStr += ((data.get(Calendar.MONTH) + 1) < 10) ? "0" + (data.get(Calendar.MONTH) + 1) : (data.get(Calendar.MONTH) + 1);
    		dataStr += ((data.get(Calendar.DAY_OF_MONTH)) < 10) ? "0" + data.get(Calendar.DAY_OF_MONTH) : data.get(Calendar.DAY_OF_MONTH);
    		return dataStr;
    	}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
    }
    
    @Deprecated
	public static ConfManager getConfManager() {
		return ManagerConf.getInstance();
	}

	public static ResourceManager getResourceManager() {
		return resourceManager;
	}

	public static GregorianCalendar getDataAtual() {
		return new GregorianCalendar();
	}
	
	public static GregorianCalendar getHoraAtual() {
		GregorianCalendar hrAtual = new GregorianCalendar();
		hrAtual.set(Calendar.YEAR, 1900);
		hrAtual.set(Calendar.MONTH, 0);
		hrAtual.set(Calendar.DAY_OF_MONTH, 1);
		
		return hrAtual;
	}
	
	public static GregorianCalendar getDataValidadeBoi() {
		GregorianCalendar date = new GregorianCalendar();
		date.add(Calendar.YEAR, 3);
		date.add(Calendar.MONTH, 6);
		return date;
	}

	public static void setObjectToSession(javax.servlet.http.HttpSession session, Object obj, String name) {
		System.out.println("called setObjectToSession = "+obj+":"+name);
		session.setAttribute(name, obj);
	}
	
	public static int modulo11(String chave) {  
        int total = 0;  
        int peso = 2;  
              
        for (int i = 0; i < chave.length(); i++) {  
            total += (chave.charAt((chave.length()-1) - i) - '0') * peso;  
            peso ++;  
            if (peso == 10)  
                peso = 2;  
        }  
        int resto = total % 11;  
        return (resto == 0 || resto == 1) ? 0 : (11 - resto);  
    }
	
	// DIV 11 - Dois digitos
	public static String getMod11(String num){
	    int base = 10;
	    int soma = 0;
	    int fator = 2;
	    String[] numeros,parcial;
	    numeros = new String[num.length()+1];
	    parcial = new String[num.length()+1];
	    for (int i = 0; i < num.length(); i++) {
	        numeros[i] = num.substring(i,i+1);
	        parcial[i] = String.valueOf(Integer.parseInt(numeros[i]) * fator);
	        soma += Integer.parseInt(parcial[i]);
	        if (fator == base) {
	            fator = -1;
	        }
	        fator++;
	    }
	    int digito1 = 0;
	    int r = soma % 11;
	    if (r == 10) {
	    	digito1 = 1;
	    } else {
	    	digito1 = soma % 11;
	        
	    }
	    
	    soma = 0;
	    fator = 1;
	    num += digito1;
	    numeros = new String[num.length()+1];
	    parcial = new String[num.length()+1];
	    for (int i = 0; i < num.length(); i++) {
	    	numeros[i] = num.substring(i,i+1);
	        parcial[i] = String.valueOf(Integer.parseInt(numeros[i]) * fator);
	        soma += Integer.parseInt(parcial[i]);
	        if (fator == base) {
	            fator = -1;
	        }
	        fator++;
	    }
	    
	    int digito2 = 0;
	    r = soma % 11;
	    if (r == 10) {
	    	digito2 = 1;
	    } else {
	    	digito2 = soma % 11;
	        
	    }
	    
	    return String.valueOf(digito1) + String.valueOf(digito2);
	}
	
	
	
	public static String getDvMod11(String num)	{
		return getDvMod11(num, "0", "0", 9, 2, true);
	}
	public static String getDvMod11(String num, int min, int max)	{
		return getDvMod11(num, "0", "0", min, max, true);
	}
	public static String getDvMod11(String num, String whenZero, String whenGreather9, int pesoInicial, int pesoFinal, boolean isRestoDV)	{
		int m = pesoFinal, sum = 0;
		for(int i=num.length()-1; i>=0; i--)	{
			sum += (m * Integer.parseInt(num.substring(i, i+1)));
			if(pesoInicial>pesoFinal)
				m = (m+1>pesoInicial) ? pesoFinal : m+1;
			else
				m = (m-1<pesoInicial) ? pesoFinal : m-1;
		}
		int result =  isRestoDV ? sum % 11 : (11 - (sum % 11));
		String dv = (result >= 10) ? whenGreather9 : String.valueOf(result);
		dv = (result == 0 ? whenZero : dv);
		return dv;
	}
	// DIV 10
	public static int getDvMod10(String num)	{
		int m = 2, sum = 0;
		for(int i=num.length()-1; i>=0; i--)	{
			int fator = (m * Integer.parseInt(num.substring(i, i+1)));
			sum += (fator < 10 ? fator : fator==10 ? 1 : (fator%10)+1);
			m = (m==2) ? 1 : 2;
		}
		return (sum % 10)==0 ? 0 : (10 - (sum % 10));
	}

	public static String fillNum(int s, int length)	{
		return fill(String.valueOf(s), length, '0', 'E');
	}
	
	public static String fillLong(long s, int length)	{
		return fill(String.valueOf(s), length, '0', 'E');
	}

	public static String fillAlpha(String s, int length)	{
		return fill(s, length, ' ', 'D');
	}

	public static String fill(String s, int length, char c, char pos)	{
		s = s == null ? "" : s;
		
		if(s.length()>length)
			return s.substring(0, length);
		
		for(int i = s.length(); i<length; i++)
			if(pos=='D')
				s += c;
			else
				s = c + s;
		

		return s;
	}
	
	public static String fill(String s, int length, String c, char pos)	{
		s = s==null ? "" : s;
		if(s.length()>length)
			return s.substring(0, length);
		for(int i=s.length(); i<length; i++)
			if(pos=='D')
				s += c;
			else
				s = c + s;
		return s;
	}

	/**
	 * Format number.
	 *
	 * @param number o(a) number
	 *
	 * @return string
	 */
	public static String formatNumber(double number, int casas)	{
		String mask = "#.";
		for(int i = 0; i < casas; i++){
			mask += "#";
		}
		return new DecimalFormat(mask).format(number);
	}
	
	public static String formatNumber(double number, String mask)	{
		return new DecimalFormat(mask).format(number);
	}

	public static String formatNumber(double number)	{
		DecimalFormatSymbols decimalformat = new DecimalFormatSymbols();
		decimalformat.setMonetaryDecimalSeparator(',');
		decimalformat.setGroupingSeparator('.');
		DecimalFormat formatador = new DecimalFormat(MASK_CURRENCY, decimalformat);
		return formatador.format(number);
	}
	
	public static String formatNumberSemSimbolos(double number, int casas)	{
		String dsCasas = "";
		while (dsCasas.length() < casas)
			dsCasas += "0"; 
		return formatNumber(number, "0"+(dsCasas.equals("") ? "" : "."+dsCasas)).replaceAll(",", ".");
	}
	
	public static String formatExtenso(float valor, boolean isCurrency)	{
		Extenso extenso = new Extenso(valor);
		return extenso.toString(isCurrency);
	}
	public static String formatExtenso(double valor, boolean isCurrency)	{
		Extenso extenso = new Extenso(valor);
		return extenso.toString(isCurrency);
	}
	
	public static String formatExtensoOrdinal(int valor)	{
		Extenso extenso = new Extenso();
		return extenso.numExtToString(valor);
	}

	public static void printInFile(String nomeArquivo, String texto)	{
		try	{
			RandomAccessFile arquivo = new RandomAccessFile(nomeArquivo, "rw");
			arquivo.seek(arquivo.length());
			arquivo.writeBytes(texto);
			arquivo.close();
		}
		catch(Exception e){
			registerLog(e);
			e.printStackTrace(System.out);
		}

	}
	
	public static void printLogTestPDV(String texto)	{
		try	{
			String nomeArquivo = "C:/TIVIC/pdv/logErro/Test.log";
			RandomAccessFile arquivo = new RandomAccessFile(nomeArquivo, "rw");
			arquivo.seek(arquivo.length());
			arquivo.writeBytes("\n"+texto);
			arquivo.close();
		}
		catch(Exception e){
			registerLog(e);
			e.printStackTrace(System.out);
		}

	}

	public static boolean equalsDate(GregorianCalendar date1, GregorianCalendar date2) {
		return (date1==null && date2==null) ||
			(date1!=null && date2!=null && date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH) &&
			 date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH) &&
			 date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR));
	}

	public static String[] getFieldsAndGroupBy(ArrayList<String> groupBy, String fields, String groups, String agregateFields) {
		groups = "";
		if(groupBy == null)
			groupBy = new ArrayList<String>();
		for(int i=0; i<groupBy.size();i++)	{
			String nmToGroup = ((String)groupBy.get(i)).toUpperCase();
			String nmToField = ((String)groupBy.get(i)).toUpperCase();
			if(nmToGroup.indexOf("CAST")>=0 || nmToGroup.indexOf("EXTRACT")>=0)
				nmToGroup = String.valueOf(i+1);
			else if(nmToGroup.toUpperCase().indexOf(" AS ")>=0)
				nmToGroup = nmToGroup.substring(0, nmToGroup.toUpperCase().lastIndexOf(" AS "));
			groups += (i==0?"GROUP BY ":", ")+nmToGroup;
			fields = (i==0?"":fields+", ")+nmToField;
		}
		fields = groupBy.size()==0 ? fields : fields + ", "+agregateFields+" ";
		return new String[] {fields, groups};
	}

	public static int getQuantidadeDias(int mes, int ano) {
		int[] meses = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		return mes == Calendar.FEBRUARY && ano % 4 == 0 ? 29 : meses[mes];
	}

	public static GregorianCalendar getPrimeiroDiaMes(int mes, int ano) {
		return new GregorianCalendar(ano, mes, 1);
	}
	
	public static GregorianCalendar getUltimoDiaMes(int mes, int ano) {
		return new GregorianCalendar(ano, mes, getQuantidadeDias(mes, ano));
	}

	public static boolean isWellFormattedIntegerValue(String value) {
		try {
			Integer.parseInt(value);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}

	public static void registerLog(Exception e)	{
		StackTraceElement[] st = e.getStackTrace();
		String log = e.getMessage()+"\n";
		for(int i=0; i<st.length; i++)
			log += "\t at "+st[i].getClassName()+"."+st[i].getMethodName()+"("+st[i].getFileName()+":"+st[i].getLineNumber()+") \n";
		try	{
  			String dirLog = "/var/log/tomcat5.5/";
  			File fileDir = new File(dirLog);
  			if(!fileDir.exists())
  				dirLog = "/tivic/pdv/logErro/";
			RandomAccessFile arquivo = new RandomAccessFile(dirLog+"logErro_"+Util.formatDateTime(new GregorianCalendar(), "yyyy-MM-dd")+".log", "rw");
			arquivo.seek(arquivo.length());
	  		arquivo.writeBytes("\n"+log);
			arquivo.close();
		}
		catch(Exception e2)	{

		}
	}

	public static String resumir(String texto, int tamanho){
	  	return (texto.length()>tamanho)?texto.substring(0,tamanho-3)+"...":texto;
	}

	public static String capitular(String texto){
	  	return (texto.length()>0)?texto.substring(0,1).toUpperCase()+texto.substring(1).toLowerCase():texto;
	}

	public static String decapitular(String texto){
	  	return (texto.length()>0)?texto.substring(0,1).toLowerCase()+texto.substring(1):texto;
	}

	public static float roundFloat(float number, int nrCasasDecimais) {
		try {
			String format = "0";
			for (int i=0; i<nrCasasDecimais; i++)
				format += i==0 ? ".0" : "0";
			return new Float(new DecimalFormat(format).format(number).replace(',', '.')).floatValue();
		}
		catch(Exception e) {
			return number;
		}
	}
	public static double roundDouble(double number, int nrCasasDecimais) {
		try {
			String format = "0";
			for (int i=0; i<nrCasasDecimais; i++)
				format += i==0 ? ".0" : "0";
			return new Double(new DecimalFormat(format).format(number).replace(',', '.'));
		}
		catch(Exception e) {
			return number;
		}
	}

	public static String format(String value, String mask, boolean fill) {
		if (fill) {
			String maskTemp = mask.replaceAll("[.-/]", "");
			int diff = maskTemp.length() - value.length();
			for (; diff>0; diff--)
				value = "0" + value;
		}

        String valueMasked = "";
        // remove caracteres nao numericos
        for (int i=0; i<value.length(); i++ )  {
            char c = value.charAt(i);
            if (Character.isDigit(c))
            	valueMasked += c;
        }

        int indMask = mask.length();
        int indField = valueMasked.length();

        for (; indField>0 && indMask>0;) {
            if (mask.charAt(--indMask) == '#')
            	indField--;
        }

        String output = "";
        for (; indMask<mask.length(); indMask++) {
        	output += mask.charAt(indMask) == '#' ? valueMasked.charAt(indField++) : mask.charAt(indMask);
        }
        return output;
    }

    public static String formatCpf(String nrCpf) {
    	return formatCpf(nrCpf, null);
    }

    public static String formatCpf(String nrCpf, String defaultValue) {
    	try {
            return format(nrCpf, "###.###.###-##", true);
    	}
    	catch(Exception e) {
    		return defaultValue;
    	}
    }

    public static String formatCnpj(String nrCnpj) {
    	return formatCnpj(nrCnpj, null);
    }

    public static String formatCnpj(String nrCnpj, String defaultValue) {
    	try {
            return format(nrCnpj, "##.###.###/####-##", true);
    	}
    	catch(Exception e) {
    		return defaultValue;
    	}
    }
    
    public static String formatCep(String nrCep) {
    	return formatCep(nrCep, null);
    }

    public static String formatCep(String nrCep, String defaultValue) {
    	try {
            return format(nrCep, "#####-###", true);
    	}
    	catch(Exception e) {
    		return defaultValue;
    	}
    }
    
    public static String formatTelefone(String nrTelefone) {
    	return formatTelefone(nrTelefone, null);
    }

    public static String formatTelefone(String nrTelefone, String defaultValue) {
    	try {
    		if(nrTelefone == null) {
    			return "";
    		}
    					
    		nrTelefone = nrTelefone.replaceAll("[^\\d.]", "");
            return format(nrTelefone.substring(0, 6), "(##)####-", true) + nrTelefone.substring(6);
    	}
    	catch(Exception e) {
    		return defaultValue;
    	}
    }
    
    public static String formatCelular(String nrCelular) {
    	return formatTelefone(nrCelular, null);
    }

    public static String formatCelular(String nrCelular, String defaultValue) {
    	try {
    		if(nrCelular == null) {
    			return "";
    		}
    		
    		nrCelular = nrCelular.replaceAll("[^\\d.]", "");
            return format(nrCelular.substring(0, 6), "(##)#####-", true) + nrCelular.substring(6);
    	}
    	catch(Exception e) {
    		return defaultValue;
    	}
    }
    
    public static String formatCurrency(Double value) {
    	return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(value);
    }

	public static void main(String[] args) throws IllegalArgumentException, Exception {
//		System.out.println(Calendar.JANUARY);
		
//		{ //teste de HASH
//			
//			long timestamp = new GregorianCalendar(2019, Calendar.AUGUST, 9, 14, 52, 52).getTimeInMillis();
//			
//			String src = "190809015006";
//			System.out.println("SRC");
//			System.out.println(src);
//			System.out.println("timestamp");
//			System.out.println(timestamp);
//			System.out.println("MD5");
//			System.out.println(SecretServices.generateHmac(src+timestamp, SecretServices.KEY, SecretServices.HMAC_MD5));
//			
//			System.out.println("SHA256");
//			System.out.println(SecretServices.generateHmac(src+timestamp, SecretServices.KEY, SecretServices.HMAC_SHA256));
//			System.out.println("onlyPrimes SHA256");
//			System.out.println(SecretServices.onlyPrimes(SecretServices.generateHmac(src+timestamp, SecretServices.KEY, SecretServices.HMAC_SHA256)));
//			
//			System.out.println("SHA512");
//			System.out.println(SecretServices.generateHmac(src+timestamp, SecretServices.KEY, SecretServices.HMAC_SHA512));
//			System.out.println("onlyPrimes SHA512");
//			System.out.println(SecretServices.onlyPrimes(SecretServices.generateHmac(src+timestamp, SecretServices.KEY, SecretServices.HMAC_SHA512)));
//		}
//		
//		{
//			System.out.println("..:: VALIDAR HASH ::..");
//			System.out.println(BoatServices.validarHash("190801121074", "DDA647A08C981996DB"));
//		}
		
		{
			ResultSetMap rsm = UsuarioServices.find(new Criterios());
			
			List<UsuarioDTO> usuarios = new UsuarioDTO.ListBuilder(rsm).build();
			System.out.println(usuarios);
			
		}
		
	}

	public static String generateRandomPassword(int length, String key) {
		char[] keys = ("1234567890"+
				       "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+ 
				       "abcdefghijklmnopqrstuvwxyz").toCharArray();

		ArrayList<String> chars = new ArrayList<String>();
		Random random = new Random();
		String senha = "";

		for (int i = 0; i < keys.length; i++)
			chars.add(String.valueOf(keys[i]));

		for (int i = 0; key!=null && i < key.length(); i++)
			chars.add(key.substring(i, i + 1));

		for (int i = 0; i < length; i++)
			senha += chars.get(random.nextInt(chars.size()));

		return senha;
	}

	public static String generateRandomString(int length) {
	    String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
	    String CHAR_UPPER = CHAR_LOWER.toUpperCase();
	    String NUMBER = "0123456789";

	    String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
	    SecureRandom random = new SecureRandom();

	    if (length < 1) throw new IllegalArgumentException();

	    StringBuilder sb = new StringBuilder(length);
	    
	    for (int i = 0; i < length; i++) {
	        int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
	        char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
	        sb.append(rndChar);
	    }

	    return sb.toString();
	}

	public static String truncAsString(BigDecimal bigDecimal, int precision) {
		String bigDecToString = bigDecimal.toString();
		String intPart = bigDecToString.indexOf('.')==-1 ? bigDecToString : bigDecToString.substring(0, bigDecToString.indexOf('.'));
		String floatPart = bigDecToString.indexOf('.')==-1 ? "0" : bigDecToString.substring(bigDecToString.indexOf('.')+1);
		floatPart = floatPart.equals("0") ? "0" : floatPart.length()<=precision ? floatPart : floatPart.substring(0, precision);
		return intPart + "." + floatPart;
	}

	public static BigDecimal trunc(BigDecimal bigDecimal, int precision) {
		return new BigDecimal(truncAsString(bigDecimal, precision));
	}

	public static String formatEndereco(String nmTipoLogradouro, String nmLogradouro, String nrDomicilio, String nmComplemento,
										String nmBairro, String nrCep, String nmMunicipio, String sgEstado, String mask) {
		String descricao = nmTipoLogradouro==null ? "" : nmTipoLogradouro.trim() + " ";
		descricao += (nmLogradouro==null ? "" : nmLogradouro.trim());
		if ((nrDomicilio!=null) && !nrDomicilio.trim().equals(""))
			descricao += ", " + nrDomicilio.trim();
		if ((nmComplemento!=null) && !(nmComplemento.trim().equals("")))
			descricao += ", " + nmComplemento.trim();
		if ((nmBairro!=null) && !nmBairro.trim().equals(""))
			descricao += " - " + nmBairro.trim();
		if ((nrCep!=null) && !nrCep.trim().equals(""))
			descricao += " - " + nrCep.trim();
		if ((nmMunicipio!=null) && !nmMunicipio.trim().equals(""))
			descricao += " - " + nmMunicipio.trim();
		if ((sgEstado!=null) && !sgEstado.trim().equals(""))
			descricao += " / " + sgEstado.trim();
		return descricao;
	}

	public static int getQuantidadeDiasUteis(GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		return getQuantidadeDiasUteis(dtInicial, dtFinal, null);
	}
	
	public static int getQuantidadeDiasUteis(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect){
		boolean isConnectionNull = connect == null;
		PreparedStatement pstmt;		
		int count = 0;
		try{
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			dtInicial.set(GregorianCalendar.HOUR, 0);
			dtInicial.set(GregorianCalendar.MINUTE, 0);
			dtInicial.set(GregorianCalendar.SECOND, 0);
			dtInicial.set(GregorianCalendar.MILLISECOND, 0);

			dtFinal.set(GregorianCalendar.HOUR, 0);
			dtFinal.set(GregorianCalendar.MINUTE, 0);
			dtFinal.set(GregorianCalendar.SECOND, 0);
			dtFinal.set(GregorianCalendar.MILLISECOND, 0);

			
			GregorianCalendar dtTemp = new GregorianCalendar(dtInicial.get(GregorianCalendar.YEAR),
														 dtInicial.get(GregorianCalendar.MONTH),
														 dtInicial.get(GregorianCalendar.DAY_OF_MONTH));
			pstmt = connect.prepareStatement("SELECT * FROM grl_feriado "+
											 "WHERE (tp_feriado = 1 "+
											 "   AND DATE_PART('day', dt_feriado) = ?  "+
											 "   AND DATE_PART('month', dt_feriado)  = ?) "+
											 "  OR (tp_feriado = 2 AND dt_feriado = ?)");
			do{
				pstmt.setInt(1, dtTemp.get(Calendar.DAY_OF_MONTH));
				pstmt.setInt(2, dtTemp.get(Calendar.MONTH)+1);
				pstmt.setTimestamp(3, new Timestamp(dtTemp.getTimeInMillis()));
				if(dtTemp.get(Calendar.DAY_OF_WEEK)>Calendar.SUNDAY && dtTemp.get(Calendar.DAY_OF_WEEK)<Calendar.SATURDAY
				   && !pstmt.executeQuery().next())
				{
					count++;
			   	}
				dtTemp.add(Calendar.DATE, 1);
			}while(dtTemp.before(dtFinal));

			return count;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}

		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}

	/**
	 * @see {@link DateUtil#addUtilsDays(GregorianCalendar, int)}
	 */
	@Deprecated
	public static GregorianCalendar addDiaUtil(GregorianCalendar dtInicial, int quantidade){
		return addDiaUtil(dtInicial, quantidade, null);
	}

	/**
	 * @see {@link DateUtil#addUtilsDays(GregorianCalendar, int)}
	 */
	@Deprecated
	public static GregorianCalendar addDiaUtil(GregorianCalendar dtInicial, int quantidade, Connection connect){
		boolean isConnectionNull = connect == null;
		PreparedStatement pstmt;		
		try{
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			dtInicial.set(GregorianCalendar.HOUR, 0);
			dtInicial.set(GregorianCalendar.MINUTE, 0);
			dtInicial.set(GregorianCalendar.SECOND, 0);
			dtInicial.set(GregorianCalendar.MILLISECOND, 0);

			
			GregorianCalendar dtTemp = new GregorianCalendar(dtInicial.get(GregorianCalendar.YEAR),
														 dtInicial.get(GregorianCalendar.MONTH),
														 dtInicial.get(GregorianCalendar.DAY_OF_MONTH));
			pstmt = connect.prepareStatement("SELECT * FROM grl_feriado "+
											 "WHERE (tp_feriado = 1 "+
											 "   AND DATE_PART('day', dt_feriado) = ?  "+
											 "   AND DATE_PART('month', dt_feriado)  = ?) "+
											 "  OR (tp_feriado = 2 AND dt_feriado = ?)");
			for(int i = 0; i < quantidade; i++){
				pstmt.setInt(1, dtTemp.get(Calendar.DAY_OF_MONTH));
				pstmt.setInt(2, dtTemp.get(Calendar.MONTH)+1);
				pstmt.setTimestamp(3, new Timestamp(dtTemp.getTimeInMillis()));
				if(dtTemp.get(Calendar.DAY_OF_WEEK)>Calendar.SUNDAY && dtTemp.get(Calendar.DAY_OF_WEEK)<Calendar.SATURDAY
				   && !pstmt.executeQuery().next())
				{
					dtTemp.add(Calendar.DATE, 1);
			   	}
				
			}

			return dtTemp;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}

		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}

	
	public static int getQuantidadeDias(GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		int count = 0;
		try{
			GregorianCalendar dtI = new GregorianCalendar(dtInicial.get(Calendar.YEAR), dtInicial.get(Calendar.MONTH), dtInicial.get(Calendar.DAY_OF_MONTH));
			GregorianCalendar dtF = new GregorianCalendar(dtFinal.get(Calendar.YEAR), dtFinal.get(Calendar.MONTH), dtFinal.get(Calendar.DAY_OF_MONTH));
			
			if(dtI.before(dtF)) {
				GregorianCalendar dtTemp = (GregorianCalendar)dtI.clone();
				while(dtTemp.before(dtF)) {
					count++;
					dtTemp.add(Calendar.DATE, 1);
				}
				
			} else if(dtF.before(dtI)) {
				GregorianCalendar dtTemp = (GregorianCalendar)dtF.clone();
				while(dtTemp.before(dtI)) {
					count--;
					dtTemp.add(Calendar.DATE, 1);
				}
				
			}
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		
		return count;
	}
	
	public static int getQuantidadeMeses(GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		int count = 0;
		try{
			dtInicial.set(GregorianCalendar.HOUR, 0);
			dtInicial.set(GregorianCalendar.MINUTE, 0);
			dtInicial.set(GregorianCalendar.SECOND, 0);
			dtInicial.set(GregorianCalendar.MILLISECOND, 0);

			dtFinal.set(GregorianCalendar.HOUR, 23);
			dtFinal.set(GregorianCalendar.MINUTE, 59);
			dtFinal.set(GregorianCalendar.SECOND, 59);
			dtFinal.set(GregorianCalendar.MILLISECOND, 0);

			if(compareDates(dtInicial, dtFinal)>0)
				return -1;


			GregorianCalendar dtTemp = (GregorianCalendar)dtInicial.clone();
			
			while(compareDates(dtTemp, dtFinal)<=0) {
				count++;
				dtTemp.add(Calendar.MONTH, 1);
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		return --count;
	}

	public static boolean isCNPJ(String CNPJ) {
		if(CNPJ == null || CNPJ.trim().equals(""))
			return false;
		CNPJ = Util.limparFormatos(CNPJ);
		// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
		if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
		    CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
		    CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
		    CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
		    CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
		    (CNPJ.length() != 14))	{
			return(false);
		}
		 
		char dig13, dig14;
		int  sm, i, r, num, peso;
		try {
			// Calculo do 1o. Digito Verificador
			sm   = 0;
		    peso = 2;
		    for (i=11; i>=0; i--) {
		    	// converte o i-ÃÆÃÂ©simo caractere do CNPJ em um nÃÆÃÂºmero: por exemplo, transforma o caractere '0' no inteiro 0 (48 eh a posiÃÆÃÂ§ÃÆÃÂ£o de '0' na tabela ASCII)
		        num  = (int)(CNPJ.charAt(i) - 48);
		        sm   = sm + (num * peso);
		        peso = peso + 1;
		        if (peso == 10)
		           peso = 2;
		    }
		    r = sm % 11;
		    if ((r == 0) || (r == 1))
		    	dig13 = '0';
		    else 
		    	dig13 = (char)((11-r) + 48);
		    // Calculo do 2o. Digito Verificador
		    sm  = 0;
		    peso = 2;
		    for (i=12; i>=0; i--) {
		    	num = (int)(CNPJ.charAt(i)- 48);
		        sm = sm + (num * peso);
		        peso = peso + 1;
		        if (peso == 10)
		           peso = 2;
		    }
		    r = sm % 11;
		    if ((r == 0) || (r == 1))
		    	dig14 = '0';
		    else 
		    	dig14 = (char)((11-r) + 48);
		    // Verifica se os dÃÆÃÂ­gitos calculados conferem com os dÃÆÃÂ­gitos informados.
		    return((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)));
		} 
		catch (InputMismatchException erro) {
			return(false);
		}
		    	
	}

	public static boolean isEmail(String email){
		if(email == null || email.trim().equals(""))
			return false;
		String patternEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";  
		Pattern padrao = Pattern.compile(patternEmail);
		Matcher pesquisa = padrao.matcher(email);
		if(pesquisa.matches())
			return true;
		else
			return false;
	}
	
	public static boolean isCpfValido(String nrCpf)	{
		if(nrCpf == null || nrCpf.trim().equals("") || nrCpf.length() < 11
		|| nrCpf.trim().equals("00000000000")
		|| nrCpf.trim().equals("11111111111")
		|| nrCpf.trim().equals("22222222222")
		|| nrCpf.trim().equals("33333333333")
		|| nrCpf.trim().equals("44444444444")
		|| nrCpf.trim().equals("55555555555")
		|| nrCpf.trim().equals("66666666666")
		|| nrCpf.trim().equals("77777777777")
		|| nrCpf.trim().equals("88888888888")
		|| nrCpf.trim().equals("99999999999"))
			return false;
		nrCpf = "00000000000".substring(0, 11-nrCpf.length()) + nrCpf;
		String nrDv  = nrCpf.substring(9,11);
		nrCpf = nrCpf.substring(0,9);
		int soma=0;
	  	for(int mult=2; mult<=10; mult++)
	    	soma += (mult * Integer.parseInt(nrCpf.substring(10-mult,11-mult)));
		String d1 = (11-(soma % 11))>=10?"0":String.valueOf(11-(soma % 11));
		soma = 0;
		nrCpf = nrCpf+d1;
	  	for(int mult=2; mult<=11; mult++)
	    	soma += (mult * Integer.parseInt(nrCpf.substring(11-mult,12-mult)));
		String d2 = (11-(soma % 11))>=10?"0":String.valueOf(11-(soma % 11));
	    return nrDv.equals(d1+d2);
	}
	public static boolean isIEBahia(String ie){
		return isIEBahia8Digitos(ie) || isIEBahia9Digitos(ie);
	}

	public static boolean isIEBahia8Digitos(String ie){
		if(ie.length() != 8)
            return false;
        
        if(!Util.isNumber(ie))
    		return false;
        
        String numeros = ie.substring(0, 6);
        String digitos = ie.substring(6);

        //------------------ MODULO 10 -------------------------------------------------
        //Calculo do segundo digito
        int soma = 0;
        int cont = 7;
        char[] tokens = numeros.toCharArray();
        for(int i = 0; i < tokens.length; i++){
        	soma += Integer.parseInt("" + tokens[i]) * cont;
            cont--;
        }
        int mod = soma % 10;

        String segundoDigito10 = "" + (mod == 0 ? 0 : (10 - mod));

        //Calculo do primeiro digito
        cont = 8;
        tokens = numeros.toCharArray();
        soma = 0;
        for(int i = 0; i < tokens.length; i++){
            soma += Integer.parseInt("" + tokens[i]) * cont;
            cont--;
        }

        soma += Integer.parseInt("" + segundoDigito10) * cont;

        mod = soma % 10;

        String primeiroDigito10 = "" + (mod == 0 ? 0 : (10 - mod));

        //------------------ MODULO 11 -------------------------------------------------
        //Calculo do segundo digito
        soma = 0;
        cont = 7;
        tokens = numeros.toCharArray();
        for(int i = 0; i < tokens.length; i++){
            soma += Integer.parseInt("" + tokens[i]) * cont;
            cont--;
        }

        mod = soma % 11;

        String segundoDigito11 = "" + (mod == 0 ? 0 : (11 - mod));

        //Calculo do primeiro digito
        cont = 8;
        tokens = numeros.toCharArray();
        soma = 0;
        for(int i = 0; i < tokens.length; i++){
            soma += Integer.parseInt("" + tokens[i]) * cont;
            cont--;
        }

        soma += Integer.parseInt("" + segundoDigito11) * 2;
        mod = soma % 11;
        String primeiroDigito11 = "" + (mod == 0 || mod == 1 ? 0 : (11 - mod));

        return digitos.equals(primeiroDigito10 + segundoDigito10) || digitos.equals(primeiroDigito11 + segundoDigito11);
    }
	
	
	
	public static boolean isIEBahia9Digitos(String ie){
		if(ie.length() != 9)
            return false;
        
        if(!Util.isNumber(ie))
    		return false;
        
        String numeros = ie.substring(0, 7);
        String digitos = ie.substring(7);

        //------------------ MODULO 10 -------------------------------------------------
        //Calculo do segundo digito
        int soma = 0;
        int cont = 8;
        char[] tokens = numeros.toCharArray();
        for(int i = 0; i < tokens.length; i++){
        	soma += Integer.parseInt("" + tokens[i]) * cont;
            cont--;
        }
        int mod = soma % 10;

        String segundoDigito10 = "" + (mod == 0 ? 0 : (10 - mod));

        //Calculo do primeiro digito
        cont = 9;
        tokens = numeros.toCharArray();
        soma = 0;
        for(int i = 0; i < tokens.length; i++){
            soma += Integer.parseInt("" + tokens[i]) * cont;
            cont--;
        }

        soma += Integer.parseInt("" + segundoDigito10) * cont;

        mod = soma % 10;

        String primeiroDigito10 = "" + (mod == 0 ? 0 : (10 - mod));

        //------------------ MODULO 11 -------------------------------------------------
        //Calculo do segundo digito
        soma = 0;
        cont = 8;
        tokens = numeros.toCharArray();
        for(int i = 0; i < tokens.length; i++){
            soma += Integer.parseInt("" + tokens[i]) * cont;
            cont--;
        }

        mod = soma % 11;

        String segundoDigito11 = "" + (mod == 0 ? 0 : (11 - mod));

        //Calculo do primeiro digito
        cont = 9;
        tokens = numeros.toCharArray();
        soma = 0;
        for(int i = 0; i < tokens.length; i++){
            soma += Integer.parseInt("" + tokens[i]) * cont;
            cont--;
        }

        soma += Integer.parseInt("" + segundoDigito11) * 2;
        mod = soma % 11;
        String primeiroDigito11 = "" + (mod == 0 || mod == 1 ? 0 : (11 - mod));

        return digitos.equals(primeiroDigito10 + segundoDigito10) || digitos.equals(primeiroDigito11 + segundoDigito11);
    }
	
	public static GregorianCalendar dateToCalendar(Date data) {
        if(data==null)
        	return null;
        
		GregorianCalendar dt = new GregorianCalendar();
		dt.setTimeInMillis(data.getTime());
		
        return dt;
    }
	
	public static Timestamp dateToTimestamp(Date data) {
        if(data==null)
        	return null;
        
        Timestamp dt = new Timestamp(data.getTime());
        return dt;
    }

	/**
     * Adiciona o(a) dias uteis.
     *
     * @param data o(a) data
     * @param nrDias o(a) nÃÆÃÂºmero do(a) dias
     * @param connect o(a) connect
     *
     * @return gregorian calendar
     */
    public static GregorianCalendar addDiasUteis(GregorianCalendar data, int nrDias, Connection connect) {
    	boolean isConnectionNull = connect==null;
    	if(isConnectionNull)
    		connect = Conexao.conectar();
    	try	{
	    	while(nrDias>0)	{
	    		data.add(Calendar.DATE, 1);
	    		if(data.get(Calendar.DAY_OF_WEEK)>1 && data.get(Calendar.DAY_OF_WEEK)<7 && !com.tivic.manager.grl.FeriadoServices.isFeriado(data, connect))
	    			nrDias--;
	    	}
	    	return data;
    	}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally{
	    	if(isConnectionNull)
	    		Conexao.desconectar(connect);
		}
    }

	/**
	 * Retorna um array com SQL de limite para consulta
	 * @param limit
	 * @param skip / offset
	 * @return retorna um array, sendo o ÃÆÃÂ­ndice 0 a clÃÆÃÂ¡usula referente ao Firebird ou MSQServer,
	 *  e o ÃÆÃÂ­ndice 1 ao ANSI
	 */
	public static String[] getLimitAndSkip(int limit, int skip)	{
		String ret1 = "", ret2 = "";
		try	{
			if(getConfManager().getIdOfDbUsed().equals("FB") || getConfManager().getIdOfDbUsed().equals("FIREBIRD"))	{
				ret1 = limit>0 ? " FIRST "+limit : "";
				ret1 += skip>0 ? " SKIP "+skip : "";
			}
			else if(getConfManager().getIdOfDbUsed().equals("MSSQL") || getConfManager().getIdOfDbUsed().equals("SQLSERVER"))	{
				ret1 = limit>0 ? " TOP "+limit : "";
				ret1 += skip>0 ? " SKIP "+skip : "";
			}
			else	{
				ret2 = limit>0 ? " LIMIT "+limit : "";
				ret2 += skip>0 ? " OFFSET "+skip : "";
			}
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
		}
		return new String[] {ret1+" ", ret2+" "};
	}
	
	/**
     * Adiciona o(a) dias uteis.
     *
     * @param data o(a) data
     * @param nrDias o(a) nÃÆÃÂºmero do(a) dias
     * @param connect o(a) connect
     *
     * @return gregorian calendar
     */
    public static String limparTexto(String texto) {
    	try	{
    		texto = texto.replaceAll("  ", " ");
    		texto = texto.replaceAll("  ", " ");
    		texto = texto.replaceAll("Á", "A");
    		texto = texto.replaceAll("É", "E");
    		texto = texto.replaceAll("Í", "I");
    		texto = texto.replaceAll("Ó", "O");
    		texto = texto.replaceAll("Ú", "U");
    		texto = texto.replaceAll("À", "A");
    		texto = texto.replaceAll("È", "E");
    		texto = texto.replaceAll("Ì", "I");
    		texto = texto.replaceAll("Ò", "O");
    		texto = texto.replaceAll("Ù", "U");
    		texto = texto.replaceAll("Ã", "A");
    		texto = texto.replaceAll("Õ", "O");
    		texto = texto.replaceAll("Â", "A");
    		texto = texto.replaceAll("Ê", "E");
    		texto = texto.replaceAll("Î", "I");
    		texto = texto.replaceAll("Ô", "O");
    		texto = texto.replaceAll("Û", "U");
    		texto = texto.replaceAll("Ä", "A");
    		texto = texto.replaceAll("Ë", "E");
    		texto = texto.replaceAll("Ï", "I");
    		texto = texto.replaceAll("Ö", "O");
    		texto = texto.replaceAll("Ü", "U");
    		texto = texto.replaceAll("Ç", "C");
    		texto = texto.replaceAll("\\/", " ");
    		texto = texto.replaceAll("\\-", " ");
    		//texto = texto.replaceAll("%", " ");
    		texto = texto.replaceAll(";", " ");
    		texto = texto.replaceAll(",", " ");
    		texto = texto.replaceAll("\\.", " ");
    		texto = texto.replaceAll("\\(", " ");
    		texto = texto.replaceAll("\\)", " ");
    		texto = texto.replaceAll("\\{", " ");
    		texto = texto.replaceAll("\\}", " ");
	    	return texto;
    	}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
    }
    
    public static String limparAcentos(String texto) {
    	return limparAcentos(texto, null);
    }
    
    public static String limparAcentos(String texto, String regex) {
    	try	{
    		if(texto == null)
    			return "";
    		
    		texto = texto.toUpperCase().trim();
    		
    		texto = texto.replaceAll("'", "");
    		texto = texto.replaceAll("ÃÂ´", "");
    		texto = texto.replaceAll("`", "");
    		texto = texto.replaceAll("\\(", "");
    		texto = texto.replaceAll("\\)", "");
    		texto = texto.replaceAll(";", "");
    		texto = texto.replaceAll("Ç", "C");
    		texto = texto.replaceAll("Á", "A");
    		texto = texto.replaceAll("À", "A");
    		texto = texto.replaceAll("Ã", "A");
    		texto = texto.replaceAll("Â", "A");
    		texto = texto.replaceAll("É", "E");
    		texto = texto.replaceAll("È", "E");
    		texto = texto.replaceAll("Ê", "E");
    		texto = texto.replaceAll("Í", "I");
    		texto = texto.replaceAll("Ó", "O");
    		texto = texto.replaceAll("Ò", "O");
    		texto = texto.replaceAll("Õ", "O");
    		texto = texto.replaceAll("Ô", "O");
    		texto = texto.replaceAll("Ú", "U");
    		texto = texto.replaceAll("Ñ", "N");
    		
    		texto = texto.trim();
    		
    		if(regex != null){
    			String[] strTexto = texto.split("");
    			String textoNovo = "";
    			for(String caractere : strTexto){
    				if(caractere.matches(regex)){
    					textoNovo += caractere;
    				}
    			}
    			texto = textoNovo;
    		}
    		
	    	return texto;
    	}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
    }
        
    public static String limparFormatos(String texto) {
    	try	{
    		if(texto == null)
    			return "";
    		texto = texto.replaceAll(" ", "");
    		texto = texto.replaceAll("-", "");
    		texto = texto.replaceAll("_", "");
    		texto = texto.replaceAll("\\.", "");
    		texto = texto.replaceAll("/", "");
    		texto = texto.replaceAll(",", "");
    		texto = texto.replaceAll(":", "");
    		texto = texto.replaceAll(";", "");
    		texto = texto.replaceAll("|", "");
    		
//    		texto = texto.replaceAll("\\", "");
    		texto = texto.replaceAll("#", "");
    		
//    		texto = texto.replaceAll("?", "");
    		texto = texto.replaceAll("\\/", "");
    		texto = texto.replaceAll("\\-", "");
    		texto = texto.replaceAll("%", "");
    		texto = texto.replaceAll("\\.", " ");
    		
    		return texto;
    	}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
    }
    
    public static String limparFormatos(String texto, char verificador) {
    	try	{
    		if(texto == null)
    			return "";
    		
    		texto = retirarEspacos(texto);
    		
    		//Retira tudo que nÃÆÃÂ£o for nÃÆÃÂºmero da String
    		if(verificador == 'N')	{
    			char[] charTest = texto.toCharArray();
    			String textoFinal = "";
    			for(int i = 0; i < charTest.length; i++){
    				char cT = charTest[i];
    				if(cT >= 48 && cT <= 57){
    					textoFinal += cT;
    				}
    				
    				
    			}
    			texto = textoFinal;
    		}
    		return texto;
    	}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
    }
    
    /**
     * Retira qualquer caractere que nÃÆÃÂ£o seja nÃÆÃÂºmeros no texto passado, usado para a retirada de formatos
     * @param texto
     * @return
     */
    public static String apenasNumeros(String texto) {
    	try	{
    		if(texto == null)
    			return "";
    		String textoNovo = "";
    		char[] conjuntoTexto = texto.toCharArray();
    		for(int i = 0; i < conjuntoTexto.length; i++){
    			if(Character.isDigit(conjuntoTexto[i])){
    				textoNovo += conjuntoTexto[i];
    			}
    		}
    		
    		return textoNovo;
    	}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
    }
    
    public static boolean isLong(String texto) {
    	try	{
    		if(texto == null)
    			return true;
    		
    		char[] conjuntoTexto = texto.toCharArray();
    		for(int i = 0; i < conjuntoTexto.length; i++){
    			if(!Character.isDigit(conjuntoTexto[i]) && conjuntoTexto[i] != '-'){
    				return false;
    			}
    		}
    		
    		return true;
    	}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return false;
		}
    }
    
    
    public static String retirarEspacos(String texto) {
    	try	{
    		texto = texto.replaceAll(" ", "");
    		if(texto.equals("null"))
    			return null;
    		return texto;
    	}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
    }
    
    
 	/**
	  * Exec sql.
	  *
	  * @param sql o(a) sql
	  *
	  * @return result set map
	  */
	 public static ResultSetMap execSQL(String sql){
		Connection connect = Conexao.conectar();
		try {
			ResultSetMap rsm = new ResultSetMap();;
			StringTokenizer tokens = new StringTokenizer(sql, ";");
			while(tokens.hasMoreElements())	{
				sql = tokens.nextToken();
				if(sql.toUpperCase().indexOf("UPDATE")>=0 || sql.toUpperCase().indexOf("CREATE")>=0 ||
				   sql.toUpperCase().indexOf("INSERT")>=0 || sql.toUpperCase().indexOf("ALTER")>=0 ||
				   sql.toUpperCase().indexOf("DELETE")>=0 || sql.toUpperCase().indexOf("DROP")>=0)
				{
					rsm = new ResultSetMap();
					int ret = connect.prepareStatement(sql).executeUpdate();
					HashMap<String,Object> register = new HashMap<String,Object>();
					register.put("RETORNO", ret+" rows commited.");
					rsm.addRegister(register);
				}
				else
					rsm = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
			}
			return rsm;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			System.err.println("Erro! Util.execSQL: " +  e);
			StackTraceElement[] st = e.getStackTrace();
			String log = e.getMessage()+"\n";
			for(int i=0; i<st.length; i++)
				log += "\t at "+st[i].getClassName()+"."+st[i].getMethodName()+"("+st[i].getFileName()+":"+st[i].getLineNumber()+") \n";
			log += "\n"+e.getMessage();
			//
			ResultSetMap rsm = new ResultSetMap();
			HashMap<String,Object> register = new HashMap<String,Object>();
			register.put("RETORNO", log);
			rsm.addRegister(register);
			return rsm;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	 
	 
	 public static double arredondar(double valor, int casas) {
		 if(casas == 0)
			 return Math.rint(valor);
		 //
		 return ((double)Math.rint(valor * Math.pow(10, casas))) / Math.pow(10, casas);   
	 }  
	 
	 public static byte[] getBytesFromFile(File file) throws IOException {    
	    InputStream is = null;  
	    byte[] ret;  
	      try {    
	         long length = file.length();    
	             ret = new byte [(int) length];    
	             is = new FileInputStream (file);    
	             is.read (ret);  
	         } finally {    
	             if (is != null) try { is.close(); } catch (IOException ex) {}    
	         }    
	         return ret;  
	}
	 
	 public static boolean isNumber(String texto) {
		 boolean isNumber = true;
		 for(int i = 0; i < texto.length(); i++) {
			 if(!Character.isDigit(texto.charAt(i)))
				 isNumber = false;
		 }
		 return isNumber;
	 }
	 
	 public static boolean inArrayInteger(Integer elem, ArrayList<Integer> lista){
		 for(int i = 0; i < lista.size(); i++){
			 if(lista.get(i).intValue() == elem.intValue())
				 return true;
		 }
		 return false;
	 }
	 
	 public static boolean inArrayString(String elem, ArrayList<String> lista){
		 for(int i = 0; i < lista.size(); i++){
			 if(lista.get(i).equals(elem))
				 return true;
		 }
		 return false;
	 }
	 
	 /**
	  * @see {@link #validatePlaca(String, String...)}
	  * @param placa
	  * @return
	  */
	 @Deprecated
	 public static boolean isPlacaValida(String placa){
		 Pattern pattern = Pattern.compile("[A-Z]{3}[0-9]{4}");  
		 Matcher matcher = pattern.matcher(placa);  
		 return matcher.matches();
	 }
	
	/**@
	 * 
	 * @param datainicio GregoriaCalendar
	 * @param datafim GregoriaCalendar
	 * @return O valor 0 se as datas forem iguais; 
	 * 			Um valor menor que 0 se a  data de inicio for anterior ÃÆÃÂ  final;
	 * 			e um valor maior que 0 se a data inicial for posterior a data final
	 */
	
    public static int compareDates(GregorianCalendar datainicio, GregorianCalendar datafim){  
        try {  
            SimpleDateFormat formatdata = new SimpleDateFormat("dd/MM/yyyy");  
            Date dtinicio = datainicio.getTime();  
            Date dtfim = datafim.getTime();  
            String inicio = formatdata.format(dtinicio);  
            String fim = formatdata.format(dtfim);  
            dtinicio = formatdata.parse(inicio);  
            dtfim = formatdata.parse(fim);   
            return dtinicio.compareTo(dtfim);  
        } catch (java.text.ParseException ex) {  
            ex.printStackTrace();
            return -1;  
        }   
    }  
    
    public static boolean contemCadeia(String cadeia, String subcadeia){
    	if(subcadeia.length() > cadeia.length())
    		return false;
    	else if(subcadeia.length() == cadeia.length() && !subcadeia.equals(cadeia))
    		return false;
    	else if(subcadeia.length() == cadeia.length() && subcadeia.equals(cadeia))
    		return true;
    	
    	for(int i = 0; i < (cadeia.length() - subcadeia.length() + 1); i++){
                if(subcadeia.equals(cadeia.substring(i, (subcadeia.length() + i))))
    			return true;
    	}
    	
    	return false;
    }
    
    public static boolean isSgEstado(String sgEstado){
    	return sgEstado.equals("AC") ||
    			sgEstado.equals("AL") ||
    			sgEstado.equals("AP") ||
    			sgEstado.equals("AM") ||
    			sgEstado.equals("BA") ||
    			sgEstado.equals("CE") ||
    			sgEstado.equals("DF") ||
    			sgEstado.equals("ES") ||
    			sgEstado.equals("GO") ||
    			sgEstado.equals("MA") ||
    			sgEstado.equals("MT") ||
    			sgEstado.equals("MS") ||
    			sgEstado.equals("MG") ||
    			sgEstado.equals("PA") ||
    			sgEstado.equals("PB") ||
    			sgEstado.equals("PR") ||
    			sgEstado.equals("PE") ||
    			sgEstado.equals("PI") ||
    			sgEstado.equals("RJ") ||
    			sgEstado.equals("RN") ||
    			sgEstado.equals("RS") ||
    			sgEstado.equals("RO") ||
    			sgEstado.equals("RR") ||
    			sgEstado.equals("SC") ||
    			sgEstado.equals("SP") ||
    			sgEstado.equals("SE") ||
    			sgEstado.equals("TO");
    }
    
    public static String eliminarZeroAEsquerda(String texto){
    	while(texto.charAt(0) == ' ' || texto.charAt(0) == '0')
    		texto = texto.substring(1);
    	return texto;
    }

	public static HashMap<String, Object> convertJSONtoHashMap(JSONObject jsonRegistro) {
		try{
			HashMap<String, Object> register = new HashMap<String, Object>();
			
			Iterator<?> keys = jsonRegistro.keys();
			
			while(keys.hasNext()){
				String chave = (String) keys.next();
				Object value = jsonRegistro.get(chave);
				
				register.put(chave, value);
			}
			
			return register;
		}
		catch(Exception e){e.printStackTrace();return null;}
	}
    
	public static String getCurrentDbName(){
		ConfManager conf = Util.getConfManager();
		return conf.getNameOfDbUsed();
	}
	
	
	public static int getIdade(GregorianCalendar dtNascimento){
		return getIdade(dtNascimento, 0, 0);
	}
	
	public static int getIdade(GregorianCalendar dtNascimento, int diaReferencia, int mesReferencia){
		return getIdade(dtNascimento, diaReferencia, mesReferencia, 0);
	}
	
	public static int getIdade(GregorianCalendar dtNascimento, int diaReferencia, int mesReferencia, int anoReferencia){
		
		GregorianCalendar dtHoje = new GregorianCalendar();
		if(diaReferencia > 0)
			dtHoje.set(Calendar.DAY_OF_MONTH, diaReferencia);
		if(mesReferencia > 0)
			dtHoje.set(Calendar.MONTH, mesReferencia);
		if(anoReferencia > 0)
			dtHoje.set(Calendar.YEAR, anoReferencia);
		
		int idade = dtHoje.get(Calendar.YEAR) - dtNascimento.get(Calendar.YEAR);
		
		if(dtNascimento.get(Calendar.MONTH) > dtHoje.get(Calendar.MONTH)){
			idade--;
		}
		else if(dtNascimento.get(Calendar.MONTH) == dtHoje.get(Calendar.MONTH)){
			if(dtNascimento.get(Calendar.DAY_OF_MONTH) > dtHoje.get(Calendar.DAY_OF_MONTH)){
				idade--;
			}
		}
		
		return idade;
		
	}
	
	public static GregorianCalendar getDataNascimentoByIdade(int qtIdade){
		return getDataNascimentoByIdade(qtIdade, 0, 0);
	}
	
	public static GregorianCalendar getDataNascimentoByIdade(int qtIdade, int diaReferencia, int mesReferencia){
		return getDataNascimentoByIdade(qtIdade, diaReferencia, mesReferencia, 0);
	}
	
	public static GregorianCalendar getDataNascimentoByIdade(int qtIdade, int diaReferencia, int mesReferencia, int anoReferencia){
		
		GregorianCalendar dtHoje = new GregorianCalendar();
		if(diaReferencia > 0)
			dtHoje.set(Calendar.DAY_OF_MONTH, diaReferencia);
		if(mesReferencia > 0)
			dtHoje.set(Calendar.MONTH, mesReferencia);
		if(anoReferencia > 0)
			dtHoje.set(Calendar.YEAR, anoReferencia);
		
		GregorianCalendar dtNascimento = new GregorianCalendar();
		
		dtNascimento.set(Calendar.YEAR, dtHoje.get(Calendar.YEAR) - qtIdade);
		dtNascimento.set(Calendar.MONTH, mesReferencia);
		dtNascimento.set(Calendar.DAY_OF_MONTH, diaReferencia);
		
		return dtNascimento;
		
	}
	
	public static boolean isDataNascimento(String data) {
		try {
			
			data = data.trim();
			
	        if(data==null || data.equals("") || (data.length() != 10 && data.length() != 19))
	        	return false;
	        
	        StringTokenizer token = new StringTokenizer(data, " ");
	        String d = token.nextToken(), h = "";
	        int hora =0, min = 0, sec = 0;
	        if(token.hasMoreTokens())	{
	        	h	=	token.nextToken();
				StringTokenizer token2	= new StringTokenizer(h, ":");
				hora =	Integer.parseInt(token2.nextToken().trim());
				min  =	Integer.parseInt(token2.nextToken().trim());
				String token3 = token2.hasMoreTokens() ? token2.nextToken() : null;
				sec  =	token3==null ? 0 : Integer.parseInt(token3.trim());
				token3 = token2.hasMoreTokens() ? token2.nextToken() : null;
			}
        	token = new StringTokenizer(d, "/");
        	int dia = Integer.parseInt(token.nextToken());
        	int mes = Integer.parseInt(token.nextToken());
        	int ano = Integer.parseInt(token.nextToken());

        	if(hora > 23 || hora < 0){
        		return false;
        	}
        	if(min > 59 || min < 0){
        		return false;
        	}
        	if(sec > 59 || sec < 0){
        		return false;
        	}
        	if(ano > Util.getDataAtual().get(Calendar.YEAR) || ano < 1900){
        		return false;
        	}
        	if(mes > 12 || mes < 1){
        		return false;
        	}
        	if(((dia > 31 || dia < 1) && (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12)) ||
        	   ((dia > 30 || dia < 1) && (mes == 4 || mes == 6 || mes == 9 || mes == 11)) ||
        	   ((dia > 28 || dia < 1) && mes == 2 && !isAnoBissexto(ano)) ||
        	   ((dia > 29 || dia < 1) && mes == 2 && isAnoBissexto(ano))){
        		return false;
        	}
        	
	        return true;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return false;
		}
	}
	   
	public static boolean isAnoBissexto(int ano) {
	    if ( ( ano % 4 == 0 && ano % 100 != 0 ) || ( ano % 400 == 0 ) ){
	        return true;
	    }
	    else{
	        return false;
	    }
	}
	
	public static String retirarAcentos(String texto) {
    	try	{
    		
    		texto = texto.replaceAll("á", "a");
    		texto = texto.replaceAll("é", "e");
    		texto = texto.replaceAll("í", "i");
    		texto = texto.replaceAll("ó", "o");
    		texto = texto.replaceAll("ú", "u");
    		texto = texto.replaceAll("à", "a");
    		texto = texto.replaceAll("è", "e");
    		texto = texto.replaceAll("ì", "i");
    		texto = texto.replaceAll("ò", "o");
    		texto = texto.replaceAll("ù", "u");
    		texto = texto.replaceAll("ã", "a");
    		texto = texto.replaceAll("õ", "o");
    		texto = texto.replaceAll("â", "a");
    		texto = texto.replaceAll("ê", "e");
    		texto = texto.replaceAll("î", "i");
    		texto = texto.replaceAll("ô", "o");
    		texto = texto.replaceAll("ô", "o");
    		texto = texto.replaceAll("ä", "a");
    		texto = texto.replaceAll("ë", "e");
    		texto = texto.replaceAll("ï", "i");
    		texto = texto.replaceAll("ö", "o");
    		texto = texto.replaceAll("ü", "u");
    		texto = texto.replaceAll("ç", "c");
    		texto = texto.replaceAll("Á", "A");
    		texto = texto.replaceAll("É", "E");
    		texto = texto.replaceAll("Í", "I");
    		texto = texto.replaceAll("Ó", "O");
    		texto = texto.replaceAll("Ú", "U");
    		texto = texto.replaceAll("À", "A");
    		texto = texto.replaceAll("È", "E");
    		texto = texto.replaceAll("Ì", "I");
    		texto = texto.replaceAll("Ò", "O");
    		texto = texto.replaceAll("Ù", "U");
    		texto = texto.replaceAll("Ã", "A");
    		texto = texto.replaceAll("Õ", "O");
    		texto = texto.replaceAll("Â", "A");
    		texto = texto.replaceAll("Ê", "E");
    		texto = texto.replaceAll("Î", "I");
    		texto = texto.replaceAll("Ô", "O");
    		texto = texto.replaceAll("Û", "U");
    		texto = texto.replaceAll("Ä", "A");
    		texto = texto.replaceAll("Ë", "E");
    		texto = texto.replaceAll("Ï", "I");
    		texto = texto.replaceAll("Ö", "O");
    		texto = texto.replaceAll("Ü", "U");
    		texto = texto.replaceAll("Ç", "C");
    			
	    	return texto;
    	}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
    }
	
	public static GregorianCalendar getDataNascimentoPossivel(String nrIdade){

		int qtIdade = Integer.parseInt(nrIdade);
		
		GregorianCalendar dtHoje = new GregorianCalendar();
		
		dtHoje.set(Calendar.YEAR, dtHoje.get(Calendar.YEAR) - qtIdade);
		dtHoje.set(Calendar.DAY_OF_MONTH, dtHoje.get(Calendar.DAY_OF_MONTH) - 1);

		return dtHoje;
	}
	
	/**
	 * Concatena os itens de um ArrayList em uma String, separados por vÃÆÃÂ­rgula.
	 * 
	 * @param array ArrayList
	 * @return String
	 * 
	 * @author MaurÃÆÃÂ­cio
	 * 
	 * @see
	 * {@link #join(ArrayList, String)}
	 */
	public static String join(ArrayList<?> array) {
		return join(array, ",");
	}
	
	/**
	 * Concatena os itens de um ArrayList em uma String, separados pelo
	 * delimitador especificado.
	 * 
	 * @param array ArrayList
	 * @param delimiter String delimitador
	 * @return String
	 * 
	 * @author MaurÃÆÃÂ­cio
	 * 
	 * @see
	 * {@link #join(ArrayList)}
	 */
	public static String join(ArrayList<?> array, String delimiter) {
		try {
			StringBuilder sb = new StringBuilder();

		    for (int i = 0; i< array.size(); i++){
		      sb.append(array.get(i));

		      if (i != array.size()-1)
		        sb.append(delimiter);
		    }
			
		    return sb.toString();
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	/**
	 * Retorna uma string contendo os elementos do array passado.
	 * container: "
	 * delimiter: ,
	 * 
	 * @param array Elementos a serem juntados
	 * @return String
	 * @see
	 * {@link #join(String[], String, String)}
	 */
	public static String join(String[] array) {
		return join(array, "\"", ",");
	}
	
	/**
	 * Retorna uma string contendo os elementos do array passado
	 * @param array Elementos a serem juntados
	 * @param container Conteiner do elemento dentro da string. Ex.: container="'" --> 'elemento'
	 * @param delimiter Delimitador dos elementos. Ex.: delimiter="," -- > 'elemento1', 'elemento2'
	 * @return String
	 * 
	 * @see
	 * {@link #join(String[])}
	 */
	public static String join(String[] array, String container, String delimiter) {
		String str = "";
		
		for (int i=0; i<array.length; i++) {
			str += container+array[i]+container;
			
			if((i+1)<array.length)
				str += delimiter;
		}
		
		return str;
	}
	
	/**
	 * Retorna uma string com os valores do campo indicado
	 * @param rsm ResultSetMap
	 * @param field o campo do rsm
	 * @return String
	 */
	public static String join(ResultSetMap rsm, String field) {
		return join(rsm, field, false);
	}
	/**
	 * Retorna uma string com os valores do campo indicado
	 * @param rsm ResultSetMap
	 * @param field o campo do rsm
	 * @param skipNull desconsidera field com valor nulo
	 * @return String
	 */
	public static String join(ResultSetMap rsm, String field, boolean skipNull) {
		String str = "";
		rsm.beforeFirst();
		while(rsm.next()) {
			if(rsm.getObject(field)==null && skipNull)
				continue;
			
			str += rsm.getObject(field);
			if(rsm.hasMore())
				str += ",";
		}
		rsm.beforeFirst();
		return str;
	}
	
	/**
	 * Concatena dois arrays de String em um ÃÆÃÂºnico array
	 * 
	 * @param array1
	 * @param array2
	 * @param skipDuplicate ignora elementos duplicados
	 * @return String[]
	 */
	public static String[] concat(String[] array1, String[] array2) {
		return concat(array1, array2, false);
	}
	public static String[] concat(String[] array1, String[] array2, boolean skipDuplicate) {
		ArrayList<String> array = new ArrayList<>();
		for(int i=0; i<array1.length; i++) {
			if(skipDuplicate && array.contains(array1[i])) 
				continue;
			
			array.add(array1[i]);
		}
		for(int i=0; i<array2.length; i++) {
			if(skipDuplicate && array.contains(array2[i])) 
				continue;
			
			array.add(array2[i]);
		}
		
		String[] resultArray = new String[array.size()];
		return array.toArray(resultArray);
	}
	
	/**
	 * Concatena n arrays, ignorando valores repetidos
	 * @param arrays
	 * @return 
	 */
	public static String[] concat(String[]... arrays) {
		ArrayList<String> total = new ArrayList<>();
		for (String[] array : arrays) {
			for (String i : array) {
				if(!total.contains(i))
					total.add(i);
			}
		}
		
		String[] result = new String[total.size()];
		return total.toArray(result);
	}
	
	public static boolean contains(ResultSetMap rsm, String field, Object value) {
		boolean contains = false;
		while(rsm.next()) {
			if(rsm.getObject(field) instanceof Integer) {
				if(rsm.getInt(field) == (Integer)value) {
					contains = true;
					break;
				}
			} else {
				if(rsm.getObject(field).equals(value)) {
					contains = true;
					break;
				}
			}
		}
		rsm.beforeFirst();
		
		return contains;
	}
	
	/**
	 * ComparaÃÆÃÂ§ÃÆÃÂ£o de Strings, porcentagem de similaridade
	 */
	private static String[] letterPairs(String str) {
		
		if(str.length() <= 0){
			return new String[0];
		}
		
	   int numPairs = str.length()-1;

       String[] pairs = new String[numPairs];

       for (int i=0; i<numPairs; i++) {

           pairs[i] = str.substring(i,i+2);

       }

       return pairs;

   }


	private static ArrayList<String> wordLetterPairs(String str) {

       ArrayList<String> allPairs = new ArrayList<String>();

       // Tokenize the string and put the tokens/words into an array 
       String[] words = str.split("\\s");

       // For each word
       for (int w=0; w < words.length; w++) {

           // Find the pairs of characters
           String[] pairsInWord = letterPairs(words[w]);

           for (int p=0; p < pairsInWord.length; p++) {
               allPairs.add(pairsInWord[p]);
           }
       }

       return allPairs;

   }

	public static double compareStrings(String str1, String str1Aux, GregorianCalendar dtNascimento, String str2, String str2Aux, GregorianCalendar dtNascimento2) {
		return compareStrings(str1, str1Aux, dtNascimento, str2, str2Aux, dtNascimento2, -1);
	}
	
	public static double compareStrings(String str1, String str1Aux, GregorianCalendar dtNascimento, String str2, String str2Aux, GregorianCalendar dtNascimento2, float porcentagemReferencia) {
		
		double porcentagem1 = 1;
		double porcentagem2 = 1;
		
		if(str1 != null && !str1.trim().equals("") && str2 != null && !str2.trim().equals(""))
			porcentagem1 = compareStrings(str1, str2);
		
		if(str1Aux != null && !str1Aux.trim().equals("") && str2Aux != null && !str2Aux.trim().equals(""))
			porcentagem2 = compareStrings(str1Aux, str2Aux);
		
		GregorianCalendar dtAcima  = null;
		GregorianCalendar dtAbaixo = null;
		
		if(dtNascimento2 != null){
			dtAcima  = (GregorianCalendar)dtNascimento2.clone();
			dtAcima.add(Calendar.DAY_OF_MONTH, 1);
			dtAbaixo = (GregorianCalendar)dtNascimento2.clone();
			dtAbaixo.add(Calendar.DAY_OF_MONTH, -1);
		}
		
		boolean mesmaData = true; 
				
		if(dtNascimento != null && dtNascimento2 != null){		
			 mesmaData = (dtNascimento.equals(dtNascimento2) || dtNascimento.equals(dtAcima) || dtNascimento.equals(dtAbaixo));
		}
		if(porcentagem1 < (porcentagemReferencia > -1 ? porcentagemReferencia : 0.8)  || porcentagem2 < (porcentagemReferencia > -1 ? porcentagemReferencia : 0.8)|| !mesmaData){
			return 0.4;
		}
		else if(porcentagem1 == 1 && porcentagem2 == 1 && mesmaData){
			return 1;
		}
		else
			return 0.9;
		
	}
	

	public static double compareStrings(String str1, String str2) {

		ArrayList<?> pairs1 = wordLetterPairs(str1.toUpperCase());
		ArrayList<?> pairs2 = wordLetterPairs(str2.toUpperCase());

		int intersection = 0;
		int union = pairs1.size() + pairs2.size();

		for (int i=0; i<pairs1.size(); i++) {
			
			Object pair1=pairs1.get(i);
			for(int j=0; j<pairs2.size(); j++) {

              	Object pair2=pairs2.get(j);
              	if (pair1.equals(pair2)) {
              		intersection++;
              		pairs2.remove(j);
              		break;

              	}

          	}

		}

      	return (2.0*intersection)/union;

   }
	
	
	public static void testeString(){
		
//		String referencial = "GABRIEL";
//		String[] comparacao = {"GABRIEL",
//							   "GBRIEL",
//							   "GABIEL",
//							   "GABRIL",
//							   "GÃÆÃâBRIEL",
//							   "GGABRIEL",
//							   "GABRRIEL",
//							   "GABSIEL",
//							   "GABRIIL",
//							   "GABRIELL"};
		
//		String referencial = "GABRIEL ALMEIDA DIAS";
//		String[] comparacao = {"GABRIEL DOS SANTOS DIAS",
//							   "GBRIEL ALMEIDA DIAS",
//							   "GABIEL ALMEIDA DIAS",
//							   "GABRIL ALMEIDA DIAS",
//							   "GÃÆÃâBRIEL ALMEIDA DIAS",
//							   "GGABRIEL ALMEIDA DIAS",
//							   "GABRRIEL ALMEIDA DIAS",
//							   "GABSIEL ALMEIDA DIAS",
//							   "GABRIIL ALMEIDA DIAS",
//							   "GABRIELL ALMEIDA DIAS",
//							   "GABRIELL ALMEIDA DOS SANTOS DIAS"};
		
		String referencial = "ABRAÃÆÃâO CARVALHO SILVA";
		String[] comparacao = {"ABRAAO CARVALHO SILVA"};
		
		for(String comparador : comparacao){
			System.out.println(compareStrings(referencial, comparador));
		}
		
	}

	public static void teste(){
		//Faz a conexao com o Local
		Connection connectLocal = Conexao.conectar();
		try{
			connectLocal.setAutoCommit(false);
			
			String nome1 = "LAIZ SOUSA SANTOS";
			String nome2 = "LAIS SOUSA SANTOS";
			
			System.out.println(Util.compareStrings(nome1, nome2));
			
		}
		
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connectLocal);
		}
		finally{
			Conexao.desconectar(connectLocal);
		}
	}
	
	public static void testeValidacao(){
		//Faz a conexao com o Local
		Connection connectLocal = Conexao.conectar();
		try{
			connectLocal.setAutoCommit(false);
			
			ResultSetMap rsm = new ResultSetMap(connectLocal.prepareStatement("SELECT * FROM acd_instituicao A, acd_instituicao_educacenso B WHERE A.cd_instituicao = B.cd_instituicao AND B.st_instituicao_publica = 1").executeQuery());
			
			while(rsm.next()){
//				System.out.println(InstituicaoServices.validacaoInstituicao(rsm.getInt("cd_instituicao"), connectLocal).getMessage());
//				System.out.println(TurmaServices.validacaoTurmas(rsm.getInt("cd_instituicao"), connectLocal).getMessage());
			}
			
		}
		
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connectLocal);
		}
		finally{
			Conexao.desconectar(connectLocal);
		}
	}
	
	/**
	 * Remove o 0s a esquerda em nÃÆÃÂºmeros no formato String
	 * 
	 * @param num 
	 * @return
	 */
	public static String removeZeroEsquerda(String num) {
		int ini = 0;
		
		if(num.length()>0) {
			for(int i=0; i<=num.length(); i++) {
				if(num.charAt(i)=='0') {
					ini++;
				}
				else
					break;
			}
			num = num.substring(ini);
		}
		
		return num;
	}
	
	/**
	 * Verifica se um item jÃÆÃÂ¡ existe no ArrayList indicado.
	 * @param array
	 * @param valor
	 * @return
	 */
	public static boolean existsInArray(ArrayList<String> array, String valor){
		if(array==null)
			return false;
		else
			for(int i=0; i<array.size(); i++)
				if((array.get(i)).equals(valor))
					return true;
		return false;
	}
	
	public static String UTF8toIso88591(String str) {
		try {
			EnsureEncoding encoding = new EnsureEncoding();
			
			return encoding.decode(str.getBytes());
		}
		catch(Exception e) {
			e.printStackTrace();
			return str;
		}
	}
	
	public static boolean testIso88591(String text){
        return Charset.forName("ISO-8859-1").newEncoder().canEncode(text);
    }
	
	public static boolean testUtf8(String text){
        return Charset.forName("UTF-8").newEncoder().canEncode(text);
    }
	
	public static String toIso88591(String str) {
		byte[] blbValue = str.getBytes();
		
		if(blbValue!=null)
			str = new String(blbValue, StandardCharsets.ISO_8859_1);
		
		return str;
	}
	
	public static String toUTF8(String str) {
		byte[] blbValue = str.getBytes();
		
		if(blbValue!=null)
			str = new String(blbValue, StandardCharsets.UTF_8);
		
		return str;
	}
	
	/**
	 * Converte um {@link ResultSetMap} em um JSON
	 * @see #rsm2Json(ResultSetMap)
	 */
	@Deprecated
	public static String rsmToJSON(ResultSetMap rsm) {
		try {
			if(rsm == null)
				rsm = new ResultSetMap();
			
			JSONObject json = new JSONObject();
			json.put("pointer", rsm.getPointer());
			json.put("lines", rsm.getLines());
			String jsonStr = json.toString();
			return jsonStr;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String rsmToJSONSync(ResultSetMap rsm) {
		HashMap<String, Object> json;
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			if(rsm == null)
				rsm = new ResultSetMap();
			
			json = new HashMap<String, Object>();
			json.put("pointer", rsm.getPointer());
			json.put("lines", rsm.getLines());
			return mapper.writeValueAsString(json);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			json = null;
			rsm = null;
		}
	}
	
	public static HashMap<String, Object> existsInResultSetMap(ResultSetMap rsm, ArrayList<String> campos, ArrayList<String> valores){
		if(rsm==null || rsm.size() == 0)
			return null;
		else{
			rsm.beforeFirst();
			while(rsm.next()){
				boolean comparador = true;
				for(int i = 0; i < campos.size(); i++){
					if(!rsm.getString(campos.get(i)).equals(valores.get(i))){
						comparador = false;
						break;
					}
				}
				
				if(comparador){
					return rsm.getRegister();
				}
			}
			rsm.beforeFirst();
		}
		return null;
	}
	
	public static File createTmpFile(String filename, String ext) {
		String tempDir = System.getProperty("java.io.tmpdir");
		String fileName = filename + ext ;
		return new File(tempDir, fileName);
	}
	
	public static File getTmpFile(String filename) {
		String tempDir = System.getProperty("java.io.tmpdir");
		String fileName = filename;
		return new File(tempDir, fileName);
	}
	
	public static boolean isStrBaseAntiga() {
		return ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
	}
	
	/**
	 * Converte uma data em {@link String} para um objeto {@link GregorianCalendar}
	 * @param strDate A data
	 * @param format O formato da data
	 * @return O Objeto {@link GregorianCalendar}
	 * @throws ParseException
	 */
	public static GregorianCalendar stringToCalendar(String strDate, String format) throws ParseException {
		GregorianCalendar dt = new GregorianCalendar();
								
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date = formatter.parse(strDate);
		
		dt.setTimeInMillis(date.getTime());
		
		return dt;
	}
	
	/**
	 * Converte um ResultSet para HashMap
	 * 
	 * @author Edgard H. <edgard@tivic.com.br>
	 * @param ResultSet rs
	 */	
	public static List<Object> resultSetToArrayList(ResultSet rs) throws SQLException {
		  ResultSetMetaData md = rs.getMetaData();
		  int columns = md.getColumnCount();
		  ArrayList<Object> list = new ArrayList<Object>(50);
		  while (rs.next()){
		     HashMap<String, Object> row = new HashMap<String, Object>(columns);
		     for(int i=1; i<=columns; ++i){           
		      row.put(md.getColumnName(i),rs.getObject(i));
		     }
		      list.add(row);
		  }
		
		 return list;
	}
	
	/**
	 * Converte texto de snake_case para camelCase
	 * 
	 * @author MaurÃ­cio Cordeiro <mauricio@tivic.com.br>
	 * @since 16/04/2020
	 */
	public static String snake2Camel(String str) throws Exception {
		str = str.toLowerCase();
	    while(str.contains("_")) {
	    	str = str.replaceFirst("_[a-z]", String.valueOf(Character.toUpperCase(str.charAt(str.indexOf("_") + 1))));
        }
        return str;
	}
	
	/**
	 * Converte o {@link ResultSetMap} em um {@link String}, na notaÃ§Ã£o correta JSON
	 * @param  {@link ResultSetMap} rsm
	 * @return {@link String} json
	 * @throws Exception
	 * 
	 * @author MaurÃ­cio Cordeiro <mauricio@tivic.com.br>
	 * @since 16/04/2020
	 * 
	 */
	public static JSONArray rsm2Json(ResultSetMap rsm) throws Exception {
		JSONArray array = new JSONArray();
		
		rsm.beforeFirst();
		
		while(rsm.next()) {
			array.put(map2Json(rsm.getRegister()));
		}
		
		rsm.beforeFirst();
		
		return array;
	}
	
	/**
	 * Converte o {@link HashMap} em um {@link String}, na notaÃ§Ã£o correta JSON
	 * @param {@link HashMap} map
	 * @return {@link String} json
	 * @throws Exception
	 * 
	 * @author MaurÃ­cio Cordeiro <mauricio@tivic.com.br>
	 * @since 16/04/2020
	 * 
	 */
	public static JSONObject map2Json(HashMap<String, Object> map)  throws Exception 
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");	

		JSONObject obj = new JSONObject();
		Set<String> keys = map.keySet();
		for (String key : keys) {
			if(key.startsWith("DT_") && map.get(key) != null){
				map.put(key, map.get(key) instanceof Timestamp ? map.get(key) : new Timestamp(((Date) map.get(key)).getTime()));
				obj.put(snake2Camel(key), Util.convCalendarStringIso(Util.convTimestampToCalendar((Timestamp) map.get(key)))); 
			}
			else if(map.get(key) != null) {
				obj.put(snake2Camel(key), map.get(key));
			}
		}
			
		return obj;
	}
	
	/**
	 * Indica se o nrPlaca indicado é válido, segundo o padrão brasileiro e do Mercosul.
	 * 
	 * @param nrPlaca No. da placa {@link String}
	 * @return  {@link boolean}
	 * @throws Exception
	 * 
	 * @author Mauriio Cordeiro <mauricio@tivic.com.br>
	 * @since 20/05/2020
	 */
	public static boolean validarPlaca(String nrPlaca) throws Exception {
		nrPlaca = nrPlaca.replaceAll("-", "").toUpperCase();
		
		String placaBR 		 = "^[A-Z]{3}[0-9]{4}$";
		String placaMercosul = "^[A-Z]{3}[0-9]{1}[A-Z]{1}[0-9]{2}$";
		
		return validatePattern(nrPlaca, placaBR) || validatePattern(nrPlaca, placaMercosul);
	}
	
	/**
	 * Indica se a string Ã© vÃ¡lida, segundo os padrÃµes indicados
	 * 
	 * @param str  String a ser validada {@link String}
	 * @param patterns PadrÃµes {@link String...}
	 * @return {@link boolean}
	 * @throws Exception
	 * 
	 * @author Mauríio Cordeiro <mauricio@tivic.com.br>
	 * @since 20/05/2020
	 */
	public static boolean validatePattern(String str, String... patterns) throws Exception {
		
		boolean isValid = true;
		
		for (String pattern : patterns) {
			if(!Pattern.matches(pattern, str)) {
				isValid = false;
				break;
			}
		}
		
		return isValid;
	}
	
	public static double convMesToDias(double mes) {
		double qtdDias = 31;
		
		if (mes == 2.0 ) {
			qtdDias = 28;
		} else if (mes == 4.0 || mes == 6.0 || mes == 9.0 || mes == 11.0) {
			qtdDias -= 1;
		}
		
		return qtdDias;
	}
	
    public static double diferencaEmDias(GregorianCalendar c1, GregorianCalendar c2)
    {
       long m1 = c1.getTimeInMillis();
       long m2 = c2.getTimeInMillis();
       return (double) ((m2 - m1) / (24*60*60*1000));
    }
    
    public static double double5(double value) {
    	DecimalFormat df5 = new DecimalFormat("#.#####");
		Double dbl = Double.parseDouble(df5.format(value).replace(",", "."));
		return dbl;
    }
    
    public static int countDouble(double value) {
    	String conv = String.valueOf(value);
    	return (conv.replace(",", "").replace(".", "").length());
    }
    
    public static void validateDec(double value) {
    	String conv = String.valueOf(value);
    	
    	String[] divs = conv.split(",");
    	System.out.println(divs);
    }
    public static byte[] convertBase64(String base64) {
		byte[] conv = base64.getBytes();
		
		return conv;
	}
	
    public static String convertByteArrayImg(byte[] file) {
		String conv = "data:image/png;base64," + new String(file);
		return conv;
	}
    
    public static String convertByteArrayPdf(byte[] pdf) {
		String conv = "data:application/pdf;base64," + new String(pdf);
		return conv;
	}
    
    public static String convertStringToMD5(String value){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        BigInteger hash = new BigInteger(1, md.digest(value.getBytes()));
        return hash.toString(16);
     }
    
    public static boolean indexInBound(String[] data, int index){
        return data != null && index >= 0 && index < data.length;
    }
    
    public static GregorianCalendar stringToCalendar(String data) {
		try {
			if(data==null || data.trim().equals(""))
	        	return null;
	        StringTokenizer token = new StringTokenizer(data.trim(), " ");
	        int dia = token.hasMoreTokens()?Integer.parseInt(token.nextToken()):0;
        	int mes = token.hasMoreTokens()?Integer.parseInt(token.nextToken()):0;
        	int ano = token.hasMoreTokens()?Integer.parseInt(token.nextToken()):0;
        	GregorianCalendar date = new GregorianCalendar(ano, mes-1, dia);
	        return date;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}