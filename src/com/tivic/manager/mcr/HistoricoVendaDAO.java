package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class HistoricoVendaDAO{

	public static int insert(HistoricoVenda objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(HistoricoVenda objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			@SuppressWarnings("rawtypes")
			HashMap[] keys = new HashMap[1];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_empreendimento");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdEmpreendimento()));
			int code = Conexao.getSequenceCode("mcr_historico_venda", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO mcr_historico_venda (cd_empreendimento,"+
			                                  "tp_conceito_mes1,"+
			                                  "tp_conceito_mes2,"+
			                                  "tp_conceito_mes3,"+
			                                  "tp_conceito_mes4,"+
			                                  "tp_conceito_mes5,"+
			                                  "tp_conceito_mes6,"+
			                                  "tp_conceito_mes7,"+
			                                  "tp_conceito_mes8,"+
			                                  "tp_conceito_mes9,"+
			                                  "tp_conceito_mes10,"+
			                                  "tp_conceito_mes11,"+
			                                  "tp_conceito_mes12) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpreendimento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpreendimento());
			pstmt.setInt(2,objeto.getTpConceitoMes1());
			pstmt.setInt(3,objeto.getTpConceitoMes2());
			pstmt.setInt(4,objeto.getTpConceitoMes3());
			pstmt.setInt(5,objeto.getTpConceitoMes4());
			pstmt.setInt(6,objeto.getTpConceitoMes5());
			pstmt.setInt(7,objeto.getTpConceitoMes6());
			pstmt.setInt(8,objeto.getTpConceitoMes7());
			pstmt.setInt(9,objeto.getTpConceitoMes8());
			pstmt.setInt(10,objeto.getTpConceitoMes9());
			pstmt.setInt(11,objeto.getTpConceitoMes10());
			pstmt.setInt(12,objeto.getTpConceitoMes11());
			pstmt.setInt(13,objeto.getTpConceitoMes12());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HistoricoVendaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! HistoricoVendaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(HistoricoVenda objeto) {
		return update(objeto, null);
	}

	public static int update(HistoricoVenda objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE mcr_historico_venda SET tp_conceito_mes1=?,"+
			                                  "tp_conceito_mes2=?,"+
			                                  "tp_conceito_mes3=?,"+
			                                  "tp_conceito_mes4=?,"+
			                                  "tp_conceito_mes5=?,"+
			                                  "tp_conceito_mes6=?,"+
			                                  "tp_conceito_mes7=?,"+
			                                  "tp_conceito_mes8=?,"+
			                                  "tp_conceito_mes9=?,"+
			                                  "tp_conceito_mes10=?,"+
			                                  "tp_conceito_mes11=?,"+
			                                  "tp_conceito_mes12=? WHERE cd_empreendimento=?");
			pstmt.setInt(1,objeto.getTpConceitoMes1());
			pstmt.setInt(2,objeto.getTpConceitoMes2());
			pstmt.setInt(3,objeto.getTpConceitoMes3());
			pstmt.setInt(4,objeto.getTpConceitoMes4());
			pstmt.setInt(5,objeto.getTpConceitoMes5());
			pstmt.setInt(6,objeto.getTpConceitoMes6());
			pstmt.setInt(7,objeto.getTpConceitoMes7());
			pstmt.setInt(8,objeto.getTpConceitoMes8());
			pstmt.setInt(9,objeto.getTpConceitoMes9());
			pstmt.setInt(10,objeto.getTpConceitoMes10());
			pstmt.setInt(11,objeto.getTpConceitoMes11());
			pstmt.setInt(12,objeto.getTpConceitoMes12());
			pstmt.setInt(13,objeto.getCdEmpreendimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HistoricoVendaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! HistoricoVendaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpreendimento) {
		return delete(cdEmpreendimento, null);
	}

	public static int delete(int cdEmpreendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM mcr_historico_venda WHERE cd_empreendimento=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HistoricoVendaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! HistoricoVendaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static HistoricoVenda get(int cdEmpreendimento) {
		return get(cdEmpreendimento, null);
	}

	public static HistoricoVenda get(int cdEmpreendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_historico_venda WHERE cd_empreendimento=?");
			pstmt.setInt(1, cdEmpreendimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new HistoricoVenda(rs.getInt("cd_empreendimento"),
						rs.getInt("tp_conceito_mes1"),
						rs.getInt("tp_conceito_mes2"),
						rs.getInt("tp_conceito_mes3"),
						rs.getInt("tp_conceito_mes4"),
						rs.getInt("tp_conceito_mes5"),
						rs.getInt("tp_conceito_mes6"),
						rs.getInt("tp_conceito_mes7"),
						rs.getInt("tp_conceito_mes8"),
						rs.getInt("tp_conceito_mes9"),
						rs.getInt("tp_conceito_mes10"),
						rs.getInt("tp_conceito_mes11"),
						rs.getInt("tp_conceito_mes12"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HistoricoVendaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HistoricoVendaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_historico_venda");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HistoricoVendaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HistoricoVendaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM mcr_historico_venda", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
