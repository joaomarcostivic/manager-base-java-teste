package com.tivic.manager.crt;

import java.sql.*;
import sol.util.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

public class MetaPremiacaoDAO{

	public static int insert(MetaPremiacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(MetaPremiacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("SCE_META_PREMIACAO");
			pstmt = connect.prepareStatement("INSERT INTO SCE_META_PREMIACAO (CD_META_PREMIACAO,"+
			                                  "GN_META_PREMIACAO,"+
			                                  "DT_INICIAL,"+
			                                  "DT_FINAL,"+
			                                  "VL_META,"+
			                                  "CD_EMPRESA,"+
			                                  "CD_VINCULO,"+
			                                  "CD_OPERACAO,"+
			                                  "TP_VALOR,"+
			                                  "TP_META,"+
			                                  "TP_PERIODO, "+
			                                  "CD_PESSOA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getGnMetaPremiacao());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setFloat(5,objeto.getVlMeta());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEmpresa());
			if(objeto.getCdVinculo()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdVinculo());
			if(objeto.getCdOperacao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdOperacao());
			pstmt.setInt(9,objeto.getTpValor());
			pstmt.setInt(10,objeto.getTpMeta());
			pstmt.setInt(11,objeto.getTpPeriodo());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdPessoa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MetaPremiacao objeto) {
		return update(objeto, null);
	}

	public static int update(MetaPremiacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE SCE_META_PREMIACAO SET GN_META_PREMIACAO=?,"+
			                                  "DT_INICIAL=?,"+
			                                  "DT_FINAL=?,"+
			                                  "VL_META=?,"+
			                                  "CD_EMPRESA=?,"+
			                                  "CD_VINCULO=?,"+
			                                  "CD_OPERACAO=?,"+
			                                  "TP_VALOR=?,"+
			                                  "TP_META=?,"+
			                                  "TP_PERIODO=?, "+
			                                  "CD_PESSOA=? WHERE CD_META_PREMIACAO=?");
			pstmt.setInt(1,objeto.getGnMetaPremiacao());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setFloat(4,objeto.getVlMeta());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			if(objeto.getCdVinculo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdVinculo());
			if(objeto.getCdOperacao()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdOperacao());
			pstmt.setInt(8,objeto.getTpValor());
			pstmt.setInt(9,objeto.getTpMeta());
			pstmt.setInt(10,objeto.getTpPeriodo());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdPessoa());
			pstmt.setInt(12,objeto.getCdMetaPremiacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMetaPremiacao) {
		return delete(cdMetaPremiacao, null);
	}

	public static int delete(int cdMetaPremiacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM SCE_META_PREMIACAO WHERE CD_META_PREMIACAO=?");
			pstmt.setInt(1, cdMetaPremiacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MetaPremiacao get(int cdMetaPremiacao) {
		return get(cdMetaPremiacao, null);
	}

	public static MetaPremiacao get(int cdMetaPremiacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM SCE_META_PREMIACAO WHERE CD_META_PREMIACAO=?");
			pstmt.setInt(1, cdMetaPremiacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MetaPremiacao(rs.getInt("CD_META_PREMIACAO"),
						rs.getInt("GN_META_PREMIACAO"),
						(rs.getTimestamp("DT_INICIAL")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_INICIAL").getTime()),
						(rs.getTimestamp("DT_FINAL")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_FINAL").getTime()),
						rs.getFloat("VL_META"),
						rs.getInt("CD_EMPRESA"),
						rs.getInt("CD_VINCULO"),
						rs.getInt("CD_OPERACAO"),
						rs.getInt("TP_VALOR"),
						rs.getInt("TP_META"),
						rs.getInt("TP_PERIODO"),
						rs.getInt("CD_PESSOA"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM SCE_META_PREMIACAO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * FROM SCE_META_PREMIACAO", criterios, Conexao.conectar());
	}

}