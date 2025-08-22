package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class UnidadeExecutoraDAO{

	public static int insert(UnidadeExecutora objeto) {
		return insert(objeto, null);
	}

	public static int insert(UnidadeExecutora objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_unidade_executora (cd_unidade_executora,"+
			                                  "dt_criacao,"+
			                                  "cd_dirigente,"+
			                                  "tp_unidade_executora) VALUES (?, ?, ?, ?)");
			if(objeto.getCdUnidadeExecutora()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdUnidadeExecutora());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getCdDirigente()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdDirigente());
			pstmt.setInt(4,objeto.getTpUnidadeExecutora());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(UnidadeExecutora objeto) {
		return update(objeto, 0, null);
	}

	public static int update(UnidadeExecutora objeto, int cdUnidadeExecutoraOld) {
		return update(objeto, cdUnidadeExecutoraOld, null);
	}

	public static int update(UnidadeExecutora objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(UnidadeExecutora objeto, int cdUnidadeExecutoraOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_unidade_executora SET cd_unidade_executora=?,"+
												      		   "dt_criacao=?,"+
												      		   "cd_dirigente=?,"+
												      		   "tp_unidade_executora=? WHERE cd_unidade_executora=?");
			pstmt.setInt(1,objeto.getCdUnidadeExecutora());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getCdDirigente()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdDirigente());
			pstmt.setInt(4,objeto.getTpUnidadeExecutora());
			pstmt.setInt(5, cdUnidadeExecutoraOld!=0 ? cdUnidadeExecutoraOld : objeto.getCdUnidadeExecutora());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdUnidadeExecutora) {
		return delete(cdUnidadeExecutora, null);
	}

	public static int delete(int cdUnidadeExecutora, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_unidade_executora WHERE cd_unidade_executora=?");
			pstmt.setInt(1, cdUnidadeExecutora);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static UnidadeExecutora get(int cdUnidadeExecutora) {
		return get(cdUnidadeExecutora, null);
	}

	public static UnidadeExecutora get(int cdUnidadeExecutora, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_unidade_executora WHERE cd_unidade_executora=?");
			pstmt.setInt(1, cdUnidadeExecutora);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new UnidadeExecutora(rs.getInt("cd_unidade_executora"),
						(rs.getTimestamp("dt_criacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_criacao").getTime()),
						rs.getInt("cd_dirigente"),
						rs.getInt("tp_unidade_executora"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_unidade_executora");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<UnidadeExecutora> getList() {
		return getList(null);
	}

	public static ArrayList<UnidadeExecutora> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<UnidadeExecutora> list = new ArrayList<UnidadeExecutora>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				UnidadeExecutora obj = UnidadeExecutoraDAO.get(rsm.getInt("cd_unidade_executora"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_unidade_executora", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
