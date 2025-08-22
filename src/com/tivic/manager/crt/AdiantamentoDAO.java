package com.tivic.manager.crt;

import java.sql.*;
import sol.util.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

public class AdiantamentoDAO{

	public static int insert(Adiantamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(Adiantamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("SCE_ADIANTAMENTO");
			pstmt = connect.prepareStatement("INSERT INTO SCE_ADIANTAMENTO (CD_ADIANTAMENTO,"+
			                                  "CD_EMPRESA,"+
			                                  "CD_PESSOA,"+
			                                  "CD_VINCULO,"+
			                                  "DT_ADIANTAMENTO,"+
			                                  "VL_ADIANTAMENTO,"+
			                                  "QT_PARCELAS,"+
			                                  "ST_ADIANTAMENTO,"+
			                                  "TP_PARCELAMENTO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			if(objeto.getCdVinculo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdVinculo());
			if(objeto.getDtAdiantamento()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtAdiantamento().getTimeInMillis()));
			pstmt.setFloat(6,objeto.getVlAdiantamento());
			pstmt.setInt(7,objeto.getQtParcelas());
			pstmt.setInt(8,objeto.getStAdiantamento());
			pstmt.setInt(9,objeto.getTpParcelamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AdiantamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AdiantamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Adiantamento objeto) {
		return update(objeto, null);
	}

	public static int update(Adiantamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE SCE_ADIANTAMENTO SET CD_EMPRESA=?,"+
			                                  "CD_PESSOA=?,"+
			                                  "CD_VINCULO=?,"+
			                                  "DT_ADIANTAMENTO=?,"+
			                                  "VL_ADIANTAMENTO=?,"+
			                                  "QT_PARCELAS=?,"+
			                                  "ST_ADIANTAMENTO=?,"+
			                                  "TP_PARCELAMENTO=? WHERE CD_ADIANTAMENTO=?");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdVinculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdVinculo());
			if(objeto.getDtAdiantamento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtAdiantamento().getTimeInMillis()));
			pstmt.setFloat(5,objeto.getVlAdiantamento());
			pstmt.setInt(6,objeto.getQtParcelas());
			pstmt.setInt(7,objeto.getStAdiantamento());
			pstmt.setInt(8,objeto.getTpParcelamento());
			pstmt.setInt(9,objeto.getCdAdiantamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AdiantamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AdiantamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAdiantamento) {
		return delete(cdAdiantamento, null);
	}

	public static int delete(int cdAdiantamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM SCE_ADIANTAMENTO WHERE CD_ADIANTAMENTO=?");
			pstmt.setInt(1, cdAdiantamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AdiantamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AdiantamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Adiantamento get(int cdAdiantamento) {
		return get(cdAdiantamento, null);
	}

	public static Adiantamento get(int cdAdiantamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM SCE_ADIANTAMENTO WHERE CD_ADIANTAMENTO=?");
			pstmt.setInt(1, cdAdiantamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Adiantamento(rs.getInt("CD_ADIANTAMENTO"),
						rs.getInt("CD_EMPRESA"),
						rs.getInt("CD_PESSOA"),
						rs.getInt("CD_VINCULO"),
						(rs.getTimestamp("DT_ADIANTAMENTO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_ADIANTAMENTO").getTime()),
						rs.getFloat("VL_ADIANTAMENTO"),
						rs.getInt("QT_PARCELAS"),
						rs.getInt("ST_ADIANTAMENTO"),
						rs.getInt("TP_PARCELAMENTO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AdiantamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AdiantamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM SCE_ADIANTAMENTO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AdiantamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * FROM SCE_ADIANTAMENTO", criterios, Conexao.conectar());
	}

}