package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class InstituicaoGrupoPessoaDAO{

	public static int insert(InstituicaoGrupoPessoa objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoGrupoPessoa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_grupo_pessoa (cd_pessoa,"+
			                                  "cd_grupo,"+
			                                  "cd_instituicao,"+
			                                  "dt_ingresso,"+
			                                  "dt_saida,"+
			                                  "st_participacao,"+
			                                  "nm_funcao,"+
			                                  "lg_aluno) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdGrupo());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdInstituicao());
			if(objeto.getDtIngresso()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtIngresso().getTimeInMillis()));
			if(objeto.getDtSaida()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtSaida().getTimeInMillis()));
			pstmt.setInt(6,objeto.getStParticipacao());
			pstmt.setString(7,objeto.getNmFuncao());
			pstmt.setInt(8,objeto.getLgAluno());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoPessoaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoPessoaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoGrupoPessoa objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(InstituicaoGrupoPessoa objeto, int cdPessoaOld, int cdGrupoOld, int cdInstituicaoOld) {
		return update(objeto, cdPessoaOld, cdGrupoOld, cdInstituicaoOld, null);
	}

	public static int update(InstituicaoGrupoPessoa objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(InstituicaoGrupoPessoa objeto, int cdPessoaOld, int cdGrupoOld, int cdInstituicaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_grupo_pessoa SET cd_pessoa=?,"+
												      		   "cd_grupo=?,"+
												      		   "cd_instituicao=?,"+
												      		   "dt_ingresso=?,"+
												      		   "dt_saida=?,"+
												      		   "st_participacao=?,"+
												      		   "nm_funcao=?,"+
												      		   "lg_aluno=? WHERE cd_pessoa=? AND cd_grupo=? AND cd_instituicao=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdGrupo());
			pstmt.setInt(3,objeto.getCdInstituicao());
			if(objeto.getDtIngresso()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtIngresso().getTimeInMillis()));
			if(objeto.getDtSaida()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtSaida().getTimeInMillis()));
			pstmt.setInt(6,objeto.getStParticipacao());
			pstmt.setString(7,objeto.getNmFuncao());
			pstmt.setInt(8,objeto.getLgAluno());
			pstmt.setInt(9, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(10, cdGrupoOld!=0 ? cdGrupoOld : objeto.getCdGrupo());
			pstmt.setInt(11, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoPessoaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoPessoaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdGrupo, int cdInstituicao) {
		return delete(cdPessoa, cdGrupo, cdInstituicao, null);
	}

	public static int delete(int cdPessoa, int cdGrupo, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_grupo_pessoa WHERE cd_pessoa=? AND cd_grupo=? AND cd_instituicao=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdGrupo);
			pstmt.setInt(3, cdInstituicao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoPessoaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoPessoaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoGrupoPessoa get(int cdPessoa, int cdGrupo, int cdInstituicao) {
		return get(cdPessoa, cdGrupo, cdInstituicao, null);
	}

	public static InstituicaoGrupoPessoa get(int cdPessoa, int cdGrupo, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_grupo_pessoa WHERE cd_pessoa=? AND cd_grupo=? AND cd_instituicao=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdGrupo);
			pstmt.setInt(3, cdInstituicao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoGrupoPessoa(rs.getInt("cd_pessoa"),
						rs.getInt("cd_grupo"),
						rs.getInt("cd_instituicao"),
						(rs.getTimestamp("dt_ingresso")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ingresso").getTime()),
						(rs.getTimestamp("dt_saida")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_saida").getTime()),
						rs.getInt("st_participacao"),
						rs.getString("nm_funcao"),
						rs.getInt("lg_aluno"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoPessoaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoPessoaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_grupo_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoPessoaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoPessoaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoGrupoPessoa> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoGrupoPessoa> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoGrupoPessoa> list = new ArrayList<InstituicaoGrupoPessoa>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoGrupoPessoa obj = InstituicaoGrupoPessoaDAO.get(rsm.getInt("cd_pessoa"), rsm.getInt("cd_grupo"), rsm.getInt("cd_instituicao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoPessoaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_grupo_pessoa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
