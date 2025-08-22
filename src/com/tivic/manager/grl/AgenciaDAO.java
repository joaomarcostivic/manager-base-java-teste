package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class AgenciaDAO{

	public static int insert(Agencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(Agencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.grl.PessoaJuridicaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_agencia (cd_agencia,"+
			                                  "cd_banco,"+
			                                  "nm_contato,"+
			                                  "nr_agencia) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1,code);
			if(objeto.getCdBanco()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdBanco());
			pstmt.setString(3,objeto.getNmContato());
			pstmt.setString(4,objeto.getNrAgencia());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenciaDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenciaDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Agencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Agencia objeto, int cdAgenciaOld) {
		return update(objeto, cdAgenciaOld, null);
	}

	public static int update(Agencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Agencia objeto, int cdAgenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_agencia SET cd_agencia=?,"+
												      		   "cd_banco=?,"+
												      		   "nm_contato=?,"+
												      		   "nr_agencia=? WHERE cd_agencia=?");
			pstmt.setInt(1,objeto.getCdAgencia());
			if(objeto.getCdBanco()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdBanco());
			pstmt.setString(3,objeto.getNmContato());
			pstmt.setString(4,objeto.getNrAgencia());
			pstmt.setInt(5, cdAgenciaOld!=0 ? cdAgenciaOld : objeto.getCdAgencia());
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.PessoaJuridicaDAO.update(objeto)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenciaDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenciaDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAgencia) {
		return delete(cdAgencia, null);
	}

	public static int delete(int cdAgencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_agencia WHERE cd_agencia=?");
			pstmt.setInt(1, cdAgencia);
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.PessoaJuridicaDAO.delete(cdAgencia, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenciaDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenciaDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Agencia get(int cdAgencia) {
		return get(cdAgencia, null);
	}

	public static Agencia get(int cdAgencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * " +
					                         "FROM grl_agencia A, grl_pessoa_juridica B, grl_pessoa C " +
					                         "WHERE A.cd_agencia=B.cd_pessoa " +
					                         "  AND A.cd_agencia=C.cd_pessoa " +
					                         "  AND A.cd_agencia=?");
			pstmt.setInt(1, cdAgencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Agencia(rs.getInt("cd_pessoa"),
						rs.getInt("cd_pessoa_superior"),
						rs.getInt("cd_pais"),
						rs.getString("nm_pessoa"),
						rs.getString("nr_telefone1"),
						rs.getString("nr_telefone2"),
						rs.getString("nr_celular"),
						rs.getString("nr_fax"),
						rs.getString("nm_email"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						rs.getInt("gn_pessoa"),
						rs.getBytes("img_foto")==null?null:rs.getBytes("img_foto"),
						rs.getInt("st_cadastro"),
						rs.getString("nm_url"),
						rs.getString("nm_apelido"),
						rs.getString("txt_observacao"),
						rs.getInt("lg_notificacao"),
						rs.getString("id_pessoa"),
						rs.getInt("cd_classificacao"),
						rs.getInt("cd_forma_divulgacao"),
						(rs.getTimestamp("dt_chegada_pais")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_chegada_pais").getTime()),
						rs.getString("nr_cnpj"),
						rs.getString("nm_razao_social"),
						rs.getString("nr_inscricao_estadual"),
						rs.getString("nr_inscricao_municipal"),
						rs.getInt("nr_funcionarios"),
						(rs.getTimestamp("dt_inicio_atividade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_atividade").getTime()),
						rs.getInt("cd_natureza_juridica"),
						rs.getInt("tp_empresa"),
						(rs.getTimestamp("dt_termino_atividade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_termino_atividade").getTime()),
						rs.getInt("cd_banco"),
						rs.getString("nm_contato"),
						rs.getString("nr_agencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_agencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_agencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
