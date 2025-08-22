package com.tivic.manager.egov;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.adm.ContaReceberServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.DateServices;

public class DAMUtils{
	
	public static final String codigoEmp  = "001";
	public static final String codigoTtx  = "002";
	public static final String codigoAgp  = "00002";
	public static final String codigoTax  = "000000044";
	
	/*
	 * CONEXAO
	 */
	public static Connection conectarEL() {
		try {
			String driver = Util.getConfManager().getProps().getProperty("EL_DRIVER");
			String dbPath = Util.getConfManager().getProps().getProperty("EL_DBPATH");
			String login = Util.getConfManager().getProps().getProperty("EL_LOGIN");
			String pass = Util.getConfManager().getProps().getProperty("EL_PASS");

			Class.forName(driver).newInstance();
	  		DriverManager.setLoginTimeout(1200);
			return DriverManager.getConnection(dbPath, login, pass);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public static void desconectarEL(Connection connect){
		try	{
			connect.close();
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
		}
	}

	/*
	 * CADASTRO DE PESSOA
	 */
	public static String insertPessoa(String nome, String tipoPessoa, String documento, String nmFantasia) {
		return insertPessoa(nome, tipoPessoa, documento, nmFantasia, null);
	}

	public static String insertPessoa(String nome, String tipoPessoa, String documento, String nmFantasia, Connection connect){
		boolean isConnectionNull = connect==null;
		PreparedStatement pstmt;
		ResultSet rs;
		
		try {
			if (isConnectionNull)
				connect = conectarEL();
			
			pstmt = connect.prepareStatement("select * from  func_wb_incluir_pessoa(?, ?, ?, ?, ?, ?, ?)");
			pstmt.setString(1, codigoEmp);
			pstmt.setString(2, nome);
			pstmt.setString(3, tipoPessoa);
			pstmt.setString(4, documento);
			pstmt.setString(5, nmFantasia);
			pstmt.setString(6, "");
			pstmt.setString(7, "I");
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				String codigoG = rs.getString("func_wb_incluir_pessoa");
				
				if(codigoG.indexOf("S")>-1) {
					String[] parts = codigoG.split("S");
					return parts[1];
				}
				else
					return codigoG;
				
			} else {
				return null;
			}
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DAMUtils.insertPessoa: " + sqlExpt);
			return null;
		}catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DAMUtils.insertPessoa: " + e);
			return null;
		}
		finally{
			if (isConnectionNull)
				desconectarEL(connect);
		}
	}

	/**
	 * Metodo que busca a pessoa no banco, caso nao encontre, ele chama o metodo para inserir a pessoa no banco
	 * @param documento
	 * @return codigo geral da pessoa inserida
	 */
	public static String getPessoa(String nome, String tipoPessoa, String documento, String nmFantasia) {
		return getPessoa(nome, tipoPessoa, documento, nmFantasia, null);
	}

	public static String getPessoa(String nome, String tipoPessoa, String documento, String nmFantasia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEL();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("select * from  func_wb_incluir_pessoa(?, ?, ?, ?, ?, ?, ?)");
			pstmt.setString(1, codigoEmp);
			pstmt.setString(2, "");
			pstmt.setString(3, tipoPessoa);
			pstmt.setString(4, documento);
			pstmt.setString(5, "");
			pstmt.setString(6, "");
			pstmt.setString(7, "C");
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				String codigoG = rs.getString("func_wb_incluir_pessoa");
				
				
				// Se nao tiver a pessoa cadastrada no banco, ele insere a pessoa no banco.
				if (codigoG == null) 
					 return insertPessoa(nome, tipoPessoa, documento, nmFantasia);

				return codigoG;
			} 
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DAMUtils.getPessoa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DAMUtils.getPessoa: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEL(connect);
		}
	}
	
	/*
	 * CADASTRO DE TAXA
	 */
	public static String insertTaxa(String ano, String codigoG, GregorianCalendar dtVencimento, float valor, float fator) {
		return insertTaxa(ano, codigoG, dtVencimento, valor, fator, null);
	}
	
	public static String insertTaxa(String ano, String codigoG, GregorianCalendar dtVencimento, float valor, float fator, Connection connect){
		boolean isConnectionNull = connect==null;
		PreparedStatement pstmt;
		ResultSet rs;
		
		try {
			if (isConnectionNull)
				connect = conectarEL();
			
			pstmt = connect.prepareStatement("select * from  func_wb_incluir_taxas(CAST ('"+codigoEmp+"' as VARCHAR), "
					+ "CAST ('"+ano+"' as VARCHAR), "
					+ "CAST ('"+codigoTtx+"' as VARCHAR), "
					+ "CAST ('"+codigoAgp+"' as VARCHAR), "
					+ "CAST ('"+codigoTax+"' as VARCHAR), "
					+ "CAST ('"+codigoG+"' as VARCHAR), "
					+ "CAST ('"+Util.convCalendarStringSql(dtVencimento)+"' as Date), "
					+ "CAST ("+valor+" as numeric), "
					+ "CAST ('' as VARCHAR), "
					+ "CAST ("+fator+" as numeric), "
					+ "CAST ('' as VARCHAR), "
					+ "CAST ('I' as VARCHAR))");
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				return rs.getString("func_wb_incluir_taxas");
			} else {
				return null;
			}
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DAMUtils.insertTaxa: " + sqlExpt);
			return null;
		}catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DAMUtils.insertTaxa: " + e);
			return null;
		}
		finally{
			if (isConnectionNull)
				desconectarEL(connect);
		}
	}
	
	public static String[] getTaxa(String ano, String livre) {
		return getTaxa(ano, livre, null);
	}

	public static String[] getTaxa(String ano, String livre, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEL();
		PreparedStatement pstmt;
		ResultSet rs;
		
		Date dt = new Date(0);
		try {
			
			pstmt = connect.prepareStatement("select * from  func_wb_incluir_taxas(CAST ('"+codigoEmp+"' as VARCHAR), "
					+ "CAST ('"+ano+"' as VARCHAR), "
					+ "CAST ('' as VARCHAR), "
					+ "CAST ('' as VARCHAR), "
					+ "CAST ('' as VARCHAR), "
					+ "CAST ('' as VARCHAR), "
					+ "CAST ('"+dt+"' as Date), "
					+ "CAST ("+0+" as numeric), "
					+ "CAST ('' as VARCHAR), "
					+ "CAST ("+0+" as numeric), "
					+ "CAST ('"+livre+"' as VARCHAR), "
					+ "CAST ('C' as VARCHAR))");
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				
				String result = rs.getString("func_wb_incluir_taxas");
				
				if (result!="" && result!=null) {
					String[] parts = result.split("#");
					return parts;
				}
				return null;
			} 
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DAMUtils.getTaxa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DAMUtils.getTaxa: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEL(connect);
		}
	}
	
	public static int deleteTaxa(String cdEmpresa, String ano, String livre) {
		return deleteTaxa(cdEmpresa, ano, livre, null);
	}

	public static int deleteTaxa(String cdEmpresa, String ano, String livre, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = conectarEL();
			
			GregorianCalendar dt = new GregorianCalendar();
			
			PreparedStatement pstmt = connect.prepareStatement("select * from  func_wb_incluir_taxas(CAST ('"+codigoEmp+"' as VARCHAR), "
					+ "CAST ('"+ano+"' as VARCHAR), "
					+ "CAST ('' as VARCHAR), "
					+ "CAST ('' as VARCHAR), "
					+ "CAST ('' as VARCHAR), "
					+ "CAST ('' as VARCHAR), "
					+ "CAST ('"+Util.convCalendarStringSql(dt)+"' as Date), "
					+ "CAST ("+0+" as numeric), "
					+ "CAST ('' as VARCHAR), "
					+ "CAST ("+0+" as numeric), "
					+ "CAST ('"+livre+"' as VARCHAR), "
					+ "CAST ('E' as VARCHAR))");
			
			ResultSet rs;
			rs = pstmt.executeQuery();

			if(rs.next()){
				return 1;
			} else {
				return 0;
			}

		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DAMUtils.deleteTaxa: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DAMUtils.deleteTaxa: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				desconectarEL(connect);
		}
	}
}
