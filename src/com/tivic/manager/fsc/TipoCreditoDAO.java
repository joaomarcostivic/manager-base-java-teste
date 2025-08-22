package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoCreditoDAO{

	public static int insert(TipoCredito objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoCredito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fsc_tipo_credito", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoCredito(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_tipo_credito (cd_tipo_credito,"+
			                                  "nm_tipo_credito,"+
			                                  "nr_tipo_credito,"+
			                                  "tp_grupo_tipo_credito) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoCredito());
			pstmt.setString(3,objeto.getNrTipoCredito());
			pstmt.setInt(4,objeto.getTpGrupoTipoCredito());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCreditoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCreditoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoCredito objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoCredito objeto, int cdTipoCreditoOld) {
		return update(objeto, cdTipoCreditoOld, null);
	}

	public static int update(TipoCredito objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoCredito objeto, int cdTipoCreditoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_tipo_credito SET cd_tipo_credito=?,"+
												      		   "nm_tipo_credito=?,"+
												      		   "nr_tipo_credito=?,"+
												      		   "tp_grupo_tipo_credito=? WHERE cd_tipo_credito=?");
			pstmt.setInt(1,objeto.getCdTipoCredito());
			pstmt.setString(2,objeto.getNmTipoCredito());
			pstmt.setString(3,objeto.getNrTipoCredito());
			pstmt.setInt(4,objeto.getTpGrupoTipoCredito());
			pstmt.setInt(5, cdTipoCreditoOld!=0 ? cdTipoCreditoOld : objeto.getCdTipoCredito());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCreditoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCreditoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoCredito) {
		return delete(cdTipoCredito, null);
	}

	public static int delete(int cdTipoCredito, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fsc_tipo_credito WHERE cd_tipo_credito=?");
			pstmt.setInt(1, cdTipoCredito);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCreditoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCreditoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoCredito get(int cdTipoCredito) {
		return get(cdTipoCredito, null);
	}

	public static TipoCredito get(int cdTipoCredito, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_tipo_credito WHERE cd_tipo_credito=?");
			pstmt.setInt(1, cdTipoCredito);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoCredito(rs.getInt("cd_tipo_credito"),
						rs.getString("nm_tipo_credito"),
						rs.getString("nr_tipo_credito"),
						rs.getInt("tp_grupo_tipo_credito"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCreditoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCreditoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_tipo_credito ORDER BY CAST(nr_tipo_credito AS INTEGER), tp_grupo_tipo_credito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCreditoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCreditoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fsc_tipo_credito", " ORDER BY CAST(nr_tipo_credito AS INTEGER), tp_grupo_tipo_credito ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
