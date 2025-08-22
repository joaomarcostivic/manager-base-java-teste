package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.connection.CustomConnection;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class ParametroDAO{

	public static int insert(Parametro objeto) {
		return insert(objeto, null);
	}

	public static int insert(Parametro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_parametro", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdParametro(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_parametro (cd_parametro,"+
			                                  "cd_empresa,"+
			                                  "nm_parametro,"+
			                                  "tp_dado,"+
			                                  "tp_apresentacao,"+
			                                  "nm_rotulo,"+
			                                  "txt_url_filtro,"+
			                                  "cd_pessoa,"+
			                                  "tp_parametro,"+
			                                  "cd_modulo,"+
			                                  "cd_sistema,"+
			                                  "tp_nivel_acesso) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setString(3,objeto.getNmParametro());
			pstmt.setInt(4,objeto.getTpDado());
			pstmt.setInt(5,objeto.getTpApresentacao());
			pstmt.setString(6,objeto.getNmRotulo());
			pstmt.setString(7,objeto.getTxtUrlFiltro());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdPessoa());
			pstmt.setInt(9,objeto.getTpParametro());
			if(objeto.getCdModulo()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdModulo());
			if(objeto.getCdSistema()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdSistema());
			pstmt.setInt(12,objeto.getTpNivelAcesso());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Parametro objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Parametro objeto, int cdParametroOld) {
		return update(objeto, cdParametroOld, null);
	}

	public static int update(Parametro objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Parametro objeto, int cdParametroOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_parametro SET cd_parametro=?,"+
												      		   "cd_empresa=?,"+
												      		   "nm_parametro=?,"+
												      		   "tp_dado=?,"+
												      		   "tp_apresentacao=?,"+
												      		   "nm_rotulo=?,"+
												      		   "txt_url_filtro=?,"+
												      		   "cd_pessoa=?,"+
												      		   "tp_parametro=?,"+
												      		   "cd_modulo=?,"+
												      		   "cd_sistema=?,"+
												      		   "tp_nivel_acesso=? WHERE cd_parametro=?");
			pstmt.setInt(1,objeto.getCdParametro());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setString(3,objeto.getNmParametro());
			pstmt.setInt(4,objeto.getTpDado());
			pstmt.setInt(5,objeto.getTpApresentacao());
			pstmt.setString(6,objeto.getNmRotulo());
			pstmt.setString(7,objeto.getTxtUrlFiltro());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdPessoa());
			pstmt.setInt(9,objeto.getTpParametro());
			if(objeto.getCdModulo()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdModulo());
			if(objeto.getCdSistema()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdSistema());
			pstmt.setInt(12,objeto.getTpNivelAcesso());
			pstmt.setInt(13, cdParametroOld!=0 ? cdParametroOld : objeto.getCdParametro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdParametro) {
		return delete(cdParametro, null);
	}

	public static int delete(int cdParametro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_parametro WHERE cd_parametro=?");
			pstmt.setInt(1, cdParametro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Parametro get(int cdParametro) {
		return get(cdParametro, null);
	}

	public static Parametro get(int cdParametro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_parametro WHERE cd_parametro=?");
			pstmt.setInt(1, cdParametro);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Parametro(rs.getInt("cd_parametro"),
						rs.getInt("cd_empresa"),
						rs.getString("nm_parametro"),
						rs.getInt("tp_dado"),
						rs.getInt("tp_apresentacao"),
						rs.getString("nm_rotulo"),
						rs.getString("txt_url_filtro"),
						rs.getInt("cd_pessoa"),
						rs.getInt("tp_parametro"),
						rs.getInt("cd_modulo"),
						rs.getInt("cd_sistema"),
						rs.getInt("tp_nivel_acesso"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_parametro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.getAll: " + e);
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
		return Search.find("SELECT A.*, B.nm_modulo, B.id_modulo " +
				           "FROM grl_parametro A " +
				           "LEFT OUTER JOIN seg_modulo B ON (A.cd_sistema = B.cd_sistema" +
				           "                             AND A.cd_modulo  = B.cd_modulo)" +
				           "", "ORDER BY nm_modulo, nm_rotulo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
		
	public static String getValorOfParametroAsString(String nmParametro, CustomConnection customConnection) throws SQLException, ValidacaoException {
		String sql = " SELECT A.cd_parametro, A.nm_parametro, B.vl_inicial FROM grl_parametro A"
				   + " LEFT OUTER JOIN grl_parametro_valor B ON (B.cd_parametro = A.cd_parametro) "
				   + " WHERE A.nm_parametro = ?";
		PreparedStatement pstmt = customConnection.getConnection().prepareStatement(sql);
		pstmt.setString(1, nmParametro);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			return rs.getString("vl_inicial");
		else
			throw new ValidacaoException("Nenhum parâmetro encontrado com o nome: " + nmParametro);
	}
	
	public static int getValorOfParametroAsInt(String nmParametro, CustomConnection customConnection) throws Exception {
		String sql = " SELECT A.cd_parametro, A.nm_parametro, B.vl_inicial FROM grl_parametro A"
				   + " LEFT OUTER JOIN grl_parametro_valor B ON (B.cd_parametro = A.cd_parametro) "
				   + " WHERE A.nm_parametro = ?";
		PreparedStatement pstmt = customConnection.getConnection().prepareStatement(sql);
		pstmt.setString(1, nmParametro);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			return Integer.valueOf(rs.getString("vl_inicial") != null ? rs.getString("vl_inicial") : "0");
		else
			throw new ValidacaoException("Nenhum parâmetro encontrado com o nome: " + nmParametro);
	}
	
	public static byte[] getValorOfParametroAsByte(String nmParametro, CustomConnection customConnection) throws Exception {
		String sql = " SELECT A.cd_parametro, A.nm_parametro, B.blb_valor FROM grl_parametro A"
				   + " LEFT OUTER JOIN grl_parametro_valor B ON (B.cd_parametro = A.cd_parametro) "
				   + " WHERE A.nm_parametro = ?";
		PreparedStatement pstmt = customConnection.getConnection().prepareStatement(sql);
		pstmt.setString(1, nmParametro);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			return rs.getBytes("blb_valor");
		else
			throw new ValidacaoException("Nenhum parâmetro encontrado com o nome: " + nmParametro);
	}
	
	public static boolean getValorOfParametroAsBoolean(String nmParametro, CustomConnection customConnection) throws Exception {
		String sql = " SELECT A.cd_parametro, A.nm_parametro, B.vl_inicial FROM grl_parametro A"
				   + " LEFT OUTER JOIN grl_parametro_valor B ON (B.cd_parametro = A.cd_parametro) "
				   + " WHERE A.nm_parametro = ?";
		PreparedStatement pstmt = customConnection.getConnection().prepareStatement(sql);
		pstmt.setString(1, nmParametro);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			try {
				return Integer.valueOf(rs.getString("vl_inicial")) > 0;
			} catch (Exception e) {
				throw new ValidacaoException("O parâmetro " + nmParametro + " não esta configurado corretamente.");
			}			
		else
			throw new ValidacaoException("Nenhum parâmetro encontrado com o nome: " + nmParametro);
	}
	
	public static Parametro getByName(String name, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_parametro WHERE nm_parametro=?");
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Parametro(rs.getInt("cd_parametro"),
						rs.getInt("cd_empresa"),
						rs.getString("nm_parametro"),
						rs.getInt("tp_dado"),
						rs.getInt("tp_apresentacao"),
						rs.getString("nm_rotulo"),
						rs.getString("txt_url_filtro"),
						rs.getInt("cd_pessoa"),
						rs.getInt("tp_parametro"),
						rs.getInt("cd_modulo"),
						rs.getInt("cd_sistema"),
						rs.getInt("tp_nivel_acesso"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
