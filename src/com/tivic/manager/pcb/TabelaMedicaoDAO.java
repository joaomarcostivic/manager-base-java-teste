package com.tivic.manager.pcb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TabelaMedicaoDAO{

	public static int insert(TabelaMedicao objeto) {
		return insert(objeto, null);
	}

	public static int insert(TabelaMedicao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("pcb_tabela_medicao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTabelaMedicao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO pcb_tabela_medicao (cd_tabela_medicao,"+
			                                  "vl_cm,"+
			                                  "vl_litros,"+
			                                  "cd_tipo_tanque) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getVlCm());
			pstmt.setInt(3,objeto.getVlLitros());
			if(objeto.getCdTipoTanque()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoTanque());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaMedicaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaMedicaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TabelaMedicao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TabelaMedicao objeto, int cdTabelaMedicaoOld) {
		return update(objeto, cdTabelaMedicaoOld, null);
	}

	public static int update(TabelaMedicao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TabelaMedicao objeto, int cdTabelaMedicaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE pcb_tabela_medicao SET cd_tabela_medicao=?,"+
												      		   "vl_cm=?,"+
												      		   "vl_litros=?,"+
												      		   "cd_tipo_tanque=? WHERE cd_tabela_medicao=?");
			pstmt.setInt(1,objeto.getCdTabelaMedicao());
			pstmt.setInt(2,objeto.getVlCm());
			pstmt.setInt(3,objeto.getVlLitros());
			if(objeto.getCdTipoTanque()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoTanque());
			pstmt.setInt(5, cdTabelaMedicaoOld!=0 ? cdTabelaMedicaoOld : objeto.getCdTabelaMedicao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaMedicaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaMedicaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabelaMedicao) {
		return delete(cdTabelaMedicao, null);
	}

	public static int delete(int cdTabelaMedicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM pcb_tabela_medicao WHERE cd_tabela_medicao=?");
			pstmt.setInt(1, cdTabelaMedicao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaMedicaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaMedicaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaMedicao get(int cdTabelaMedicao) {
		return get(cdTabelaMedicao, null);
	}

	public static TabelaMedicao get(int cdTabelaMedicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM pcb_tabela_medicao WHERE cd_tabela_medicao=?");
			pstmt.setInt(1, cdTabelaMedicao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TabelaMedicao(rs.getInt("cd_tabela_medicao"),
						rs.getInt("vl_cm"),
						rs.getInt("vl_litros"),
						rs.getInt("cd_tipo_tanque"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaMedicaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaMedicaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM pcb_tabela_medicao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaMedicaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaMedicaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM pcb_tabela_medicao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	/*
	 * INSERT E UPDATE RETORNA OBJETO
	 */
	public static TabelaMedicao insertObjeto(TabelaMedicao objeto) {
		return insertObjeto(objeto, null);
	}

	public static TabelaMedicao insertObjeto(TabelaMedicao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("pcb_tabela_medicao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return null;
			}
			objeto.setCdTabelaMedicao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO pcb_tabela_medicao (cd_tabela_medicao,"+
			                                  "vl_cm,"+
			                                  "vl_litros,"+
			                                  "cd_tipo_tanque) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getVlCm());
			pstmt.setInt(3,objeto.getVlLitros());
			if(objeto.getCdTipoTanque()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoTanque());
			pstmt.executeUpdate();
			return objeto;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaMedicaoDAO.insert: " + sqlExpt);
			return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaMedicaoDAO.insert: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static TabelaMedicao updateObjeto(TabelaMedicao objeto) {
		return updateObjeto(objeto, 0, null);
	}

	public static TabelaMedicao updateObjeto(TabelaMedicao objeto, int cdTabelaMedicaoOld) {
		return updateObjeto(objeto, cdTabelaMedicaoOld, null);
	}

	public static TabelaMedicao updateObjeto(TabelaMedicao objeto, Connection connect) {
		return updateObjeto(objeto, 0, connect);
	}

	public static TabelaMedicao updateObjeto(TabelaMedicao objeto, int cdTabelaMedicaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE pcb_tabela_medicao SET cd_tabela_medicao=?,"+
												      		   "vl_cm=?,"+
												      		   "vl_litros=?,"+
												      		   "cd_tipo_tanque=? WHERE cd_tabela_medicao=?");
			pstmt.setInt(1,objeto.getCdTabelaMedicao());
			pstmt.setInt(2,objeto.getVlCm());
			pstmt.setInt(3,objeto.getVlLitros());
			if(objeto.getCdTipoTanque()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoTanque());
			pstmt.setInt(5, cdTabelaMedicaoOld!=0 ? cdTabelaMedicaoOld : objeto.getCdTabelaMedicao());
			pstmt.executeUpdate();
			return objeto;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaMedicaoDAO.update: " + sqlExpt);
			return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaMedicaoDAO.update: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

}
