package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class ClienteProgramaFaturaDAO{

	public static int insert(ClienteProgramaFatura objeto) {
		return insert(objeto, null);
	}

	public static int insert(ClienteProgramaFatura objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_cliente_programa_fatura");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_programa_fatura");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdProgramaFatura()));
			int code = Conexao.getSequenceCode("adm_cliente_programa_fatura", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdClienteProgramaFatura(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_cliente_programa_fatura (cd_empresa,"+
			                                  "cd_pessoa,"+
			                                  "cd_programa_fatura,"+
			                                  "cd_classificacao,"+
			                                  "cd_cliente_programa_fatura) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdProgramaFatura()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProgramaFatura());
			if(objeto.getCdClassificacao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdClassificacao());
			pstmt.setInt(5, code);
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteProgramaFaturaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteProgramaFaturaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ClienteProgramaFatura objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ClienteProgramaFatura objeto, int cdClienteProgramaFaturaOld, int cdProgramaFaturaOld) {
		return update(objeto, cdClienteProgramaFaturaOld, cdProgramaFaturaOld, null);
	}

	public static int update(ClienteProgramaFatura objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ClienteProgramaFatura objeto, int cdClienteProgramaFaturaOld, int cdProgramaFaturaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_cliente_programa_fatura SET cd_empresa=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_programa_fatura=?,"+
												      		   "cd_classificacao=?,"+
												      		   "cd_cliente_programa_fatura=? WHERE cd_cliente_programa_fatura=? AND cd_programa_fatura=?");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getCdProgramaFatura());
			if(objeto.getCdClassificacao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdClassificacao());
			pstmt.setInt(5,objeto.getCdClienteProgramaFatura());
			pstmt.setInt(6, cdClienteProgramaFaturaOld!=0 ? cdClienteProgramaFaturaOld : objeto.getCdClienteProgramaFatura());
			pstmt.setInt(7, cdProgramaFaturaOld!=0 ? cdProgramaFaturaOld : objeto.getCdProgramaFatura());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteProgramaFaturaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteProgramaFaturaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdClienteProgramaFatura, int cdProgramaFatura) {
		return delete(cdClienteProgramaFatura, cdProgramaFatura, null);
	}

	public static int delete(int cdClienteProgramaFatura, int cdProgramaFatura, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_cliente_programa_fatura WHERE cd_cliente_programa_fatura=? AND cd_programa_fatura=?");
			pstmt.setInt(1, cdClienteProgramaFatura);
			pstmt.setInt(2, cdProgramaFatura);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteProgramaFaturaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteProgramaFaturaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ClienteProgramaFatura get(int cdClienteProgramaFatura, int cdProgramaFatura) {
		return get(cdClienteProgramaFatura, cdProgramaFatura, null);
	}

	public static ClienteProgramaFatura get(int cdClienteProgramaFatura, int cdProgramaFatura, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_cliente_programa_fatura WHERE cd_cliente_programa_fatura=? AND cd_programa_fatura=?");
			pstmt.setInt(1, cdClienteProgramaFatura);
			pstmt.setInt(2, cdProgramaFatura);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ClienteProgramaFatura(rs.getInt("cd_empresa"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_programa_fatura"),
						rs.getInt("cd_classificacao"),
						rs.getInt("cd_cliente_programa_fatura"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteProgramaFaturaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteProgramaFaturaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_cliente_programa_fatura");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteProgramaFaturaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteProgramaFaturaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_cliente_programa_fatura", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
