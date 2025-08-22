package com.tivic.manager.blb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class AssuntoDAO{

	public static int insert(Assunto objeto) {
		return insert(objeto, null);
	}

	public static int insert(Assunto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("blb_assunto", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAssunto(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO blb_assunto (cd_assunto,"+
			                                  "nm_assunto,"+
			                                  "id_assunto,"+
			                                  "tp_tabela_assunto,"+
			                                  "cd_assunto_superior) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmAssunto());
			pstmt.setString(3,objeto.getIdAssunto());
			pstmt.setInt(4,objeto.getTpTabelaAssunto());
			if(objeto.getCdAssuntoSuperior()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAssuntoSuperior());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssuntoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Assunto objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Assunto objeto, int cdAssuntoOld) {
		return update(objeto, cdAssuntoOld, null);
	}

	public static int update(Assunto objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Assunto objeto, int cdAssuntoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE blb_assunto SET cd_assunto=?,"+
												      		   "nm_assunto=?,"+
												      		   "id_assunto=?,"+
												      		   "tp_tabela_assunto=?,"+
												      		   "cd_assunto_superior=? WHERE cd_assunto=?");
			pstmt.setInt(1,objeto.getCdAssunto());
			pstmt.setString(2,objeto.getNmAssunto());
			pstmt.setString(3,objeto.getIdAssunto());
			pstmt.setInt(4,objeto.getTpTabelaAssunto());
			if(objeto.getCdAssuntoSuperior()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAssuntoSuperior());
			pstmt.setInt(6, cdAssuntoOld!=0 ? cdAssuntoOld : objeto.getCdAssunto());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssuntoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAssunto) {
		return delete(cdAssunto, null);
	}

	public static int delete(int cdAssunto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM blb_assunto WHERE cd_assunto=?");
			pstmt.setInt(1, cdAssunto);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssuntoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Assunto get(int cdAssunto) {
		return get(cdAssunto, null);
	}

	public static Assunto get(int cdAssunto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM blb_assunto WHERE cd_assunto=?");
			pstmt.setInt(1, cdAssunto);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Assunto(rs.getInt("cd_assunto"),
						rs.getString("nm_assunto"),
						rs.getString("id_assunto"),
						rs.getInt("tp_tabela_assunto"),
						rs.getInt("cd_assunto_superior"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssuntoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_assunto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssuntoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Assunto> getList() {
		return getList(null);
	}

	public static ArrayList<Assunto> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Assunto> list = new ArrayList<Assunto>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Assunto obj = AssuntoDAO.get(rsm.getInt("cd_assunto"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM blb_assunto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
