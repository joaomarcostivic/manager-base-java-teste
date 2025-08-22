package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoEquipamentoAcessoInternetAlunoDAO{

	public static int insert(TipoEquipamentoAcessoInternetAluno objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoEquipamentoAcessoInternetAluno objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_tipo_equipamento_acesso_internet_aluno", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoEquipamentoAcessoInternetAluno(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_tipo_equipamento_acesso_internet_aluno (cd_tipo_equipamento_acesso_internet_aluno,"+
			                                  "nm_tipo_equipamento_acesso_internet_aluno,"+
			                                  "id_tipo_equipamento_acesso_internet_aluno,"+
			                                  "st_tipo_equipamento_acesso_internet_aluno) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoEquipamentoAcessoInternetAluno());
			pstmt.setString(3,objeto.getIdTipoEquipamentoAcessoInternetAluno());
			pstmt.setInt(4,objeto.getStTipoEquipamentoAcessoInternetAluno());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoAcessoInternetAlunoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoAcessoInternetAlunoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoEquipamentoAcessoInternetAluno objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoEquipamentoAcessoInternetAluno objeto, int cdTipoEquipamentoAcessoInternetAlunoOld) {
		return update(objeto, cdTipoEquipamentoAcessoInternetAlunoOld, null);
	}

	public static int update(TipoEquipamentoAcessoInternetAluno objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoEquipamentoAcessoInternetAluno objeto, int cdTipoEquipamentoAcessoInternetAlunoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_tipo_equipamento_acesso_internet_aluno SET cd_tipo_equipamento_acesso_internet_aluno=?,"+
												      		   "nm_tipo_equipamento_acesso_internet_aluno=?,"+
												      		   "id_tipo_equipamento_acesso_internet_aluno=?,"+
												      		   "st_tipo_equipamento_acesso_internet_aluno=? WHERE cd_tipo_equipamento_acesso_internet_aluno=?");
			pstmt.setInt(1,objeto.getCdTipoEquipamentoAcessoInternetAluno());
			pstmt.setString(2,objeto.getNmTipoEquipamentoAcessoInternetAluno());
			pstmt.setString(3,objeto.getIdTipoEquipamentoAcessoInternetAluno());
			pstmt.setInt(4,objeto.getStTipoEquipamentoAcessoInternetAluno());
			pstmt.setInt(5, cdTipoEquipamentoAcessoInternetAlunoOld!=0 ? cdTipoEquipamentoAcessoInternetAlunoOld : objeto.getCdTipoEquipamentoAcessoInternetAluno());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoAcessoInternetAlunoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoAcessoInternetAlunoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoEquipamentoAcessoInternetAluno) {
		return delete(cdTipoEquipamentoAcessoInternetAluno, null);
	}

	public static int delete(int cdTipoEquipamentoAcessoInternetAluno, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_tipo_equipamento_acesso_internet_aluno WHERE cd_tipo_equipamento_acesso_internet_aluno=?");
			pstmt.setInt(1, cdTipoEquipamentoAcessoInternetAluno);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoAcessoInternetAlunoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoAcessoInternetAlunoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoEquipamentoAcessoInternetAluno get(int cdTipoEquipamentoAcessoInternetAluno) {
		return get(cdTipoEquipamentoAcessoInternetAluno, null);
	}

	public static TipoEquipamentoAcessoInternetAluno get(int cdTipoEquipamentoAcessoInternetAluno, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_equipamento_acesso_internet_aluno WHERE cd_tipo_equipamento_acesso_internet_aluno=?");
			pstmt.setInt(1, cdTipoEquipamentoAcessoInternetAluno);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoEquipamentoAcessoInternetAluno(rs.getInt("cd_tipo_equipamento_acesso_internet_aluno"),
						rs.getString("nm_tipo_equipamento_acesso_internet_aluno"),
						rs.getString("id_tipo_equipamento_acesso_internet_aluno"),
						rs.getInt("st_tipo_equipamento_acesso_internet_aluno"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoAcessoInternetAlunoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoAcessoInternetAlunoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_equipamento_acesso_internet_aluno");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoAcessoInternetAlunoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoAcessoInternetAlunoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoEquipamentoAcessoInternetAluno> getList() {
		return getList(null);
	}

	public static ArrayList<TipoEquipamentoAcessoInternetAluno> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoEquipamentoAcessoInternetAluno> list = new ArrayList<TipoEquipamentoAcessoInternetAluno>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoEquipamentoAcessoInternetAluno obj = TipoEquipamentoAcessoInternetAlunoDAO.get(rsm.getInt("cd_tipo_equipamento_acesso_internet_aluno"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoAcessoInternetAlunoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_equipamento_acesso_internet_aluno", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}