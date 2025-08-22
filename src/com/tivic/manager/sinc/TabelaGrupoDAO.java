package com.tivic.manager.sinc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TabelaGrupoDAO{

	public static int insert(TabelaGrupo objeto) {
		return insert(objeto, null);
	}

	public static int insert(TabelaGrupo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO sinc_tabela_grupo (cd_tabela,"+
			                                  "cd_grupo) VALUES (?, ?)");
			if(objeto.getCdTabela()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTabela());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdGrupo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaGrupoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaGrupoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TabelaGrupo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TabelaGrupo objeto, int cdTabelaOld, int cdGrupoOld) {
		return update(objeto, cdTabelaOld, cdGrupoOld, null);
	}

	public static int update(TabelaGrupo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TabelaGrupo objeto, int cdTabelaOld, int cdGrupoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE sinc_tabela_grupo SET cd_tabela=?,"+
												      		   "cd_grupo=? WHERE cd_tabela=? AND cd_grupo=?");
			pstmt.setInt(1,objeto.getCdTabela());
			pstmt.setInt(2,objeto.getCdGrupo());
			pstmt.setInt(3, cdTabelaOld!=0 ? cdTabelaOld : objeto.getCdTabela());
			pstmt.setInt(4, cdGrupoOld!=0 ? cdGrupoOld : objeto.getCdGrupo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaGrupoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaGrupoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabela, int cdGrupo) {
		return delete(cdTabela, cdGrupo, null);
	}

	public static int delete(int cdTabela, int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM sinc_tabela_grupo WHERE cd_tabela=? AND cd_grupo=?");
			pstmt.setInt(1, cdTabela);
			pstmt.setInt(2, cdGrupo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaGrupoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaGrupoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaGrupo get(int cdTabela, int cdGrupo) {
		return get(cdTabela, cdGrupo, null);
	}

	public static TabelaGrupo get(int cdTabela, int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM sinc_tabela_grupo WHERE cd_tabela=? AND cd_grupo=?");
			pstmt.setInt(1, cdTabela);
			pstmt.setInt(2, cdGrupo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TabelaGrupo(rs.getInt("cd_tabela"),
						rs.getInt("cd_grupo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaGrupoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaGrupoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM sinc_tabela_grupo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaGrupoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaGrupoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TabelaGrupo> getList() {
		return getList(null);
	}

	public static ArrayList<TabelaGrupo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TabelaGrupo> list = new ArrayList<TabelaGrupo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TabelaGrupo obj = TabelaGrupoDAO.get(rsm.getInt("cd_tabela"), rsm.getInt("cd_grupo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaGrupoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM sinc_tabela_grupo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
