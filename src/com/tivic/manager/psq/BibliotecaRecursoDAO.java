package com.tivic.manager.psq;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class BibliotecaRecursoDAO{

	public static int insert(BibliotecaRecurso objeto) {
		return insert(objeto, null);
	}

	public static int insert(BibliotecaRecurso objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("psq_biblioteca_recurso", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdBibliotecaRecurso(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO psq_biblioteca_recurso (cd_biblioteca_recurso,"+
			                                  "nm_objeto,"+
			                                  "id_objeto,"+
			                                  "tp_objeto,"+
			                                  "blb_objeto,"+
			                                  "cd_empresa,"+
			                                  "cd_pessoa,"+
			                                  "cd_vinculo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmObjeto());
			pstmt.setString(3,objeto.getIdObjeto());
			pstmt.setInt(4,objeto.getTpObjeto());
			if(objeto.getBlbObjeto()==null)
				pstmt.setNull(5, Types.BINARY);
			else
				pstmt.setBytes(5,objeto.getBlbObjeto());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdPessoa());
			if(objeto.getCdVinculo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdVinculo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaRecursoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaRecursoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BibliotecaRecurso objeto) {
		return update(objeto, 0, null);
	}

	public static int update(BibliotecaRecurso objeto, int cdBibliotecaRecursoOld) {
		return update(objeto, cdBibliotecaRecursoOld, null);
	}

	public static int update(BibliotecaRecurso objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(BibliotecaRecurso objeto, int cdBibliotecaRecursoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE psq_biblioteca_recurso SET cd_biblioteca_recurso=?,"+
												      		   "nm_objeto=?,"+
												      		   "id_objeto=?,"+
												      		   "tp_objeto=?,"+
												      		   "blb_objeto=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_vinculo=? WHERE cd_biblioteca_recurso=?");
			pstmt.setInt(1,objeto.getCdBibliotecaRecurso());
			pstmt.setString(2,objeto.getNmObjeto());
			pstmt.setString(3,objeto.getIdObjeto());
			pstmt.setInt(4,objeto.getTpObjeto());
			if(objeto.getBlbObjeto()==null)
				pstmt.setNull(5, Types.BINARY);
			else
				pstmt.setBytes(5,objeto.getBlbObjeto());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdPessoa());
			if(objeto.getCdVinculo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdVinculo());
			pstmt.setInt(9, cdBibliotecaRecursoOld!=0 ? cdBibliotecaRecursoOld : objeto.getCdBibliotecaRecurso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaRecursoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaRecursoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBibliotecaRecurso) {
		return delete(cdBibliotecaRecurso, null);
	}

	public static int delete(int cdBibliotecaRecurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM psq_biblioteca_recurso WHERE cd_biblioteca_recurso=?");
			pstmt.setInt(1, cdBibliotecaRecurso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaRecursoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaRecursoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BibliotecaRecurso get(int cdBibliotecaRecurso) {
		return get(cdBibliotecaRecurso, null);
	}

	public static BibliotecaRecurso get(int cdBibliotecaRecurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM psq_biblioteca_recurso WHERE cd_biblioteca_recurso=?");
			pstmt.setInt(1, cdBibliotecaRecurso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BibliotecaRecurso(rs.getInt("cd_biblioteca_recurso"),
						rs.getString("nm_objeto"),
						rs.getString("id_objeto"),
						rs.getInt("tp_objeto"),
						rs.getBytes("blb_objeto")==null?null:rs.getBytes("blb_objeto"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_vinculo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaRecursoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaRecursoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM psq_biblioteca_recurso");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaRecursoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaRecursoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM psq_biblioteca_recurso", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
