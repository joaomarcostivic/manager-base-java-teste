package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import sol.dao.Util;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;

public class MovimentoEcfDAO{

	public static int insert(MovimentoEcf objeto) {
		return insert(objeto, null);
	}

	public static int insert(MovimentoEcf objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_movimento_ecf (cd_referencia,"+
			                                  "dt_movimento,"+
			                                  "nr_contador_inicial,"+
			                                  "nr_contador_final,"+
			                                  "nr_reducao,"+
			                                  "nr_contador_reinicio,"+
			                                  "vl_venda_bruta,"+
			                                  "vl_geral_acumulado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdReferencia());
			pstmt.setTimestamp(2, new Timestamp(objeto.getDtMovimento().getTimeInMillis()));
			pstmt.setString(3,objeto.getNrContadorInicial());
			pstmt.setString(4,objeto.getNrContadorFinal());
			pstmt.setString(5,objeto.getNrReducao());
			pstmt.setString(6,objeto.getNrContadorReinicio());
			pstmt.setFloat(7,objeto.getVlVendaBruta());
			pstmt.setFloat(8,objeto.getVlGeralAcumulado());
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoEcfDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MovimentoEcf objeto) {
		return update(objeto, 0, null, null);
	}

	public static int update(MovimentoEcf objeto, Connection connect) {
		return update(objeto, 0, null, connect);
	}

	public static int update(MovimentoEcf objeto, int cdReferenciaOld, GregorianCalendar dtMovimentoOld) {
		return update(objeto, cdReferenciaOld, dtMovimentoOld, null);
	}

	public static int update(MovimentoEcf objeto, int cdReferenciaOld, GregorianCalendar dtMovimentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_movimento_ecf SET cd_referencia=?,"+
												      		   "dt_movimento=?,"+
												      		   "nr_contador_inicial=?,"+
												      		   "nr_contador_final=?,"+
												      		   "nr_reducao=?,"+
												      		   "nr_contador_reinicio=?,"+
												      		   "vl_venda_bruta=?,"+
												      		   "vl_geral_acumulado=? WHERE cd_referencia=? AND dt_movimento=?");
			pstmt.setInt(1,objeto.getCdReferencia());
			pstmt.setTimestamp(2, new Timestamp(objeto.getDtMovimento().getTimeInMillis()));
			pstmt.setString(3,objeto.getNrContadorInicial());
			pstmt.setString(4,objeto.getNrContadorFinal());
			pstmt.setString(5,objeto.getNrReducao());
			pstmt.setString(6,objeto.getNrContadorReinicio());
			pstmt.setFloat(7,objeto.getVlVendaBruta());
			pstmt.setFloat(8,objeto.getVlGeralAcumulado());
			pstmt.setInt(9, cdReferenciaOld!=0 ? cdReferenciaOld : objeto.getCdReferencia());
			pstmt.setTimestamp(10, new Timestamp(dtMovimentoOld!=null ? dtMovimentoOld.getTimeInMillis() : objeto.getDtMovimento().getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoEcfDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoEcfDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdReferencia, GregorianCalendar dtMovimento) {
		return delete(cdReferencia, dtMovimento, null);
	}

	public static int delete(int cdReferencia, GregorianCalendar dtMovimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_movimento_ecf WHERE cd_referencia=? AND dt_movimento=?");
			pstmt.setInt(1, cdReferencia);
			pstmt.setTimestamp(2, new Timestamp(dtMovimento.getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoEcfDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoEcfDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MovimentoEcf get(int cdReferencia, GregorianCalendar dtMovimento) {
		return get(cdReferencia, dtMovimento, null);
	}

	public static MovimentoEcf get(int cdReferencia, GregorianCalendar dtMovimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_ecf WHERE cd_referencia=? AND dt_movimento=?");
			pstmt.setInt(1, cdReferencia);
			pstmt.setTimestamp(2, new Timestamp(dtMovimento.getTimeInMillis()));
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MovimentoEcf(rs.getInt("cd_referencia"),
						(rs.getTimestamp("dt_movimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_movimento").getTime()),
						rs.getString("nr_contador_inicial"),
						rs.getString("nr_contador_final"),
						rs.getString("nr_reducao"),
						rs.getString("nr_contador_reinicio"),
						rs.getFloat("vl_venda_bruta"),
						rs.getFloat("vl_geral_acumulado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoEcfDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoEcfDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_ecf");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoEcfDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoEcfDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM adm_movimento_ecf", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
