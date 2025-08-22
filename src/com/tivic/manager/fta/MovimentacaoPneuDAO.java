package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class MovimentacaoPneuDAO{

	public static int insert(MovimentacaoPneu objeto) {
		return insert(objeto, null);
	}

	public static int insert(MovimentacaoPneu objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_movimentacao_pneu (cd_componente_pneu,"+
			                                  "cd_referencia,"+
			                                  "cd_tipo_movimentacao,"+
			                                  "qt_hodometro,"+
			                                  "dt_movimentacao) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdComponentePneu()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdComponentePneu());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdReferencia());
			if(objeto.getCdTipoMovimentacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoMovimentacao());
			pstmt.setInt(4,objeto.getQtHodometro());
			if(objeto.getDtMovimentacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtMovimentacao().getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentacaoPneuDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentacaoPneuDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MovimentacaoPneu objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MovimentacaoPneu objeto, int cdComponentePneuOld, int cdReferenciaOld) {
		return update(objeto, cdComponentePneuOld, cdReferenciaOld, null);
	}

	public static int update(MovimentacaoPneu objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MovimentacaoPneu objeto, int cdComponentePneuOld, int cdReferenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_movimentacao_pneu SET cd_componente_pneu=?,"+
												      		   "cd_referencia=?,"+
												      		   "cd_tipo_movimentacao=?,"+
												      		   "qt_hodometro=?,"+
												      		   "dt_movimentacao=? WHERE cd_componente_pneu=? AND cd_referencia=?");
			pstmt.setInt(1,objeto.getCdComponentePneu());
			pstmt.setInt(2,objeto.getCdReferencia());
			if(objeto.getCdTipoMovimentacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoMovimentacao());
			pstmt.setInt(4,objeto.getQtHodometro());
			if(objeto.getDtMovimentacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtMovimentacao().getTimeInMillis()));
			pstmt.setInt(6, cdComponentePneuOld!=0 ? cdComponentePneuOld : objeto.getCdComponentePneu());
			pstmt.setInt(7, cdReferenciaOld!=0 ? cdReferenciaOld : objeto.getCdReferencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentacaoPneuDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentacaoPneuDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdComponentePneu, int cdReferencia) {
		return delete(cdComponentePneu, cdReferencia, null);
	}

	public static int delete(int cdComponentePneu, int cdReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_movimentacao_pneu WHERE cd_componente_pneu=? AND cd_referencia=?");
			pstmt.setInt(1, cdComponentePneu);
			pstmt.setInt(2, cdReferencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentacaoPneuDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentacaoPneuDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MovimentacaoPneu get(int cdComponentePneu, int cdReferencia) {
		return get(cdComponentePneu, cdReferencia, null);
	}

	public static MovimentacaoPneu get(int cdComponentePneu, int cdReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_movimentacao_pneu WHERE cd_componente_pneu=? AND cd_referencia=?");
			pstmt.setInt(1, cdComponentePneu);
			pstmt.setInt(2, cdReferencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MovimentacaoPneu(rs.getInt("cd_componente_pneu"),
						rs.getInt("cd_referencia"),
						rs.getInt("cd_tipo_movimentacao"),
						rs.getInt("qt_hodometro"),
						(rs.getTimestamp("dt_movimentacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_movimentacao").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentacaoPneuDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentacaoPneuDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_movimentacao_pneu");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentacaoPneuDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentacaoPneuDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_movimentacao_pneu", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
