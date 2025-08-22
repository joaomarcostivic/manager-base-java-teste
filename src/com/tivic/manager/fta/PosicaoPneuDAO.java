package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class PosicaoPneuDAO{

	public static int insert(PosicaoPneu objeto) {
		return insert(objeto, null);
	}

	public static int insert(PosicaoPneu objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_posicao_pneu", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPosicao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_posicao_pneu (cd_posicao,"+
			                                  "tp_local,"+
			                                  "nr_eixo,"+
			                                  "tp_lado,"+
			                                  "tp_disposicao) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getTpLocal());
			pstmt.setInt(3,objeto.getNrEixo());
			pstmt.setInt(4,objeto.getTpLado());
			pstmt.setInt(5,objeto.getTpDisposicao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PosicaoPneuDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PosicaoPneuDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PosicaoPneu objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PosicaoPneu objeto, int cdPosicaoOld) {
		return update(objeto, cdPosicaoOld, null);
	}

	public static int update(PosicaoPneu objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PosicaoPneu objeto, int cdPosicaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_posicao_pneu SET cd_posicao=?,"+
												      		   "tp_local=?,"+
												      		   "nr_eixo=?,"+
												      		   "tp_lado=?,"+
												      		   "tp_disposicao=? WHERE cd_posicao=?");
			pstmt.setInt(1,objeto.getCdPosicao());
			pstmt.setInt(2,objeto.getTpLocal());
			pstmt.setInt(3,objeto.getNrEixo());
			pstmt.setInt(4,objeto.getTpLado());
			pstmt.setInt(5,objeto.getTpDisposicao());
			pstmt.setInt(6, cdPosicaoOld!=0 ? cdPosicaoOld : objeto.getCdPosicao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PosicaoPneuDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PosicaoPneuDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPosicao) {
		return delete(cdPosicao, null);
	}

	public static int delete(int cdPosicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_posicao_pneu WHERE cd_posicao=?");
			pstmt.setInt(1, cdPosicao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PosicaoPneuDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PosicaoPneuDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PosicaoPneu get(int cdPosicao) {
		return get(cdPosicao, null);
	}

	public static PosicaoPneu get(int cdPosicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_posicao_pneu WHERE cd_posicao=?");
			pstmt.setInt(1, cdPosicao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PosicaoPneu(rs.getInt("cd_posicao"),
						rs.getInt("tp_local"),
						rs.getInt("nr_eixo"),
						rs.getInt("tp_lado"),
						rs.getInt("tp_disposicao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PosicaoPneuDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PosicaoPneuDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_posicao_pneu ORDER BY nr_eixo, tp_local, tp_lado, tp_disposicao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PosicaoPneuDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PosicaoPneuDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_posicao_pneu", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
