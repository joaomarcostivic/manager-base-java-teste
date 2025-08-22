package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Pessoa;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class OrgaoDAO extends Pessoa{

	public static int insert(Orgao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Orgao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.grl.PessoaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPessoa(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_orgao (cd_orgao,"+
			                                  "nm_orgao,"+
			                                  "id_orgao,"+
			                                  "cd_responsavel,"+
			                                  "cd_funcao_responsavel,"+
			                                  "cd_pessoa_orgao,"+
			                                  "cd_cidade,"+
			                                  "cd_agente_responsavel," + 
			                                  "lg_emitir_ait) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmOrgao());
			pstmt.setString(3,objeto.getIdOrgao());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdResponsavel());
			if(objeto.getCdFuncaoResponsavel()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFuncaoResponsavel());
			if(objeto.getCdPessoaOrgao()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdPessoaOrgao());
			pstmt.setInt(7,objeto.getCdCidade());
			if(objeto.getCdAgenteResponsavel()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdAgenteResponsavel());
			pstmt.setInt(9,objeto.getLgEmitirAit());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Orgao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Orgao objeto, int cdOrgaoOld) {
		return update(objeto, cdOrgaoOld, null);
	}

	public static int update(Orgao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Orgao objeto, int cdOrgaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			pstmt = connect.prepareStatement("UPDATE mob_orgao SET cd_orgao=?,"+
												      		   "nm_orgao=?,"+
												      		   "id_orgao=?,"+
												      		   "cd_responsavel=?,"+
												      		   "cd_funcao_responsavel=?,"+
												      		   "cd_pessoa_orgao=?,"+
												      		   "cd_cidade=?,"+
												      		   "cd_agente_responsavel=?, " + 
												      		   "lg_emitir_ait=? WHERE cd_orgao=?");
			pstmt.setInt(1,objeto.getCdOrgao());
			pstmt.setString(2,objeto.getNmOrgao());
			pstmt.setString(3,objeto.getIdOrgao());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdResponsavel());
			if(objeto.getCdFuncaoResponsavel()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFuncaoResponsavel());
			if(objeto.getCdPessoaOrgao()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdPessoaOrgao());
			pstmt.setInt(7,objeto.getCdCidade());
			if(objeto.getCdAgenteResponsavel()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdAgenteResponsavel());			
			pstmt.setInt(9,objeto.getLgEmitirAit());			
			pstmt.setInt(10, cdOrgaoOld!=0 ? cdOrgaoOld : objeto.getCdOrgao());
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.PessoaDAO.update(objeto, connect)<=0) {
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
			System.err.println("Erro! OrgaoDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrgao) {
		return delete(cdOrgao, null);
	}

	public static int delete(int cdOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_orgao WHERE cd_orgao=?");
			pstmt.setInt(1, cdOrgao);
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.PessoaDAO.delete(cdOrgao, connect)<=0) {
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
			System.err.println("Erro! OrgaoDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Orgao get(int cdOrgao) {
		return get(cdOrgao, null);
	}

	public static Orgao get(int cdOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_orgao A WHERE A.cd_orgao=?");
			pstmt.setInt(1, cdOrgao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Orgao(rs.getInt("cd_orgao"),
						rs.getString("nm_orgao"),
						rs.getString("id_orgao"),
						rs.getInt("cd_responsavel"),
						rs.getInt("cd_funcao_responsavel"),
						rs.getInt("cd_pessoa_orgao"),
						rs.getInt("cd_cidade"),
						rs.getInt("cd_agente_responsavel"),
						rs.getInt("lg_emitir_ait"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_orgao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Orgao> getList() {
		return getList(null);
	}

	public static ArrayList<Orgao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Orgao> list = new ArrayList<Orgao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Orgao obj = OrgaoDAO.get(rsm.getInt("cd_orgao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_orgao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
