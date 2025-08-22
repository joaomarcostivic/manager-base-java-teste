package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoConteinerArmazenagemDAO{

	public static int insert(TipoConteinerArmazenagem objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoConteinerArmazenagem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_tipo_conteiner_armazenagem (cd_tipo_conteiner_pai,"+
			                                  "cd_tipo_conteiner,"+
			                                  "vl_capacidade) VALUES (?, ?, ?)");
			if(objeto.getCdTipoConteinerPai()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTipoConteinerPai());
			if(objeto.getCdTipoConteiner()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoConteiner());
			pstmt.setFloat(3,objeto.getVlCapacidade());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerArmazenagemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerArmazenagemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoConteinerArmazenagem objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TipoConteinerArmazenagem objeto, int cdTipoConteinerPaiOld, int cdTipoConteinerOld) {
		return update(objeto, cdTipoConteinerPaiOld, cdTipoConteinerOld, null);
	}

	public static int update(TipoConteinerArmazenagem objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TipoConteinerArmazenagem objeto, int cdTipoConteinerPaiOld, int cdTipoConteinerOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_tipo_conteiner_armazenagem SET cd_tipo_conteiner_pai=?,"+
												      		   "cd_tipo_conteiner=?,"+
												      		   "vl_capacidade=? WHERE cd_tipo_conteiner_pai=? AND cd_tipo_conteiner=?");
			pstmt.setInt(1,objeto.getCdTipoConteinerPai());
			pstmt.setInt(2,objeto.getCdTipoConteiner());
			pstmt.setFloat(3,objeto.getVlCapacidade());
			pstmt.setInt(4, cdTipoConteinerPaiOld!=0 ? cdTipoConteinerPaiOld : objeto.getCdTipoConteinerPai());
			pstmt.setInt(5, cdTipoConteinerOld!=0 ? cdTipoConteinerOld : objeto.getCdTipoConteiner());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerArmazenagemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerArmazenagemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoConteinerPai, int cdTipoConteiner) {
		return delete(cdTipoConteinerPai, cdTipoConteiner, null);
	}

	public static int delete(int cdTipoConteinerPai, int cdTipoConteiner, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_tipo_conteiner_armazenagem WHERE cd_tipo_conteiner_pai=? AND cd_tipo_conteiner=?");
			pstmt.setInt(1, cdTipoConteinerPai);
			pstmt.setInt(2, cdTipoConteiner);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerArmazenagemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerArmazenagemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoConteinerArmazenagem get(int cdTipoConteinerPai, int cdTipoConteiner) {
		return get(cdTipoConteinerPai, cdTipoConteiner, null);
	}

	public static TipoConteinerArmazenagem get(int cdTipoConteinerPai, int cdTipoConteiner, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_conteiner_armazenagem WHERE cd_tipo_conteiner_pai=? AND cd_tipo_conteiner=?");
			pstmt.setInt(1, cdTipoConteinerPai);
			pstmt.setInt(2, cdTipoConteiner);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoConteinerArmazenagem(rs.getInt("cd_tipo_conteiner_pai"),
						rs.getInt("cd_tipo_conteiner"),
						rs.getFloat("vl_capacidade"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerArmazenagemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerArmazenagemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_conteiner_armazenagem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerArmazenagemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoConteinerArmazenagemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_tipo_conteiner_armazenagem", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
