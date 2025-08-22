package com.tivic.manager.acd;

import java.sql.*;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.PessoaFisicaDAO;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;

import java.util.ArrayList;

public class ProfessorDAO{

	public static int insert(Professor objeto) {
		return insert(objeto, null);
	}

	public static int insert(Professor objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = 1;
			if(PessoaFisicaDAO.get(objeto.getCdPessoa(), connect) == null){
				code = PessoaFisicaDAO.insert(objeto, connect);
				if (code <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
			}
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_professor (cd_professor,"+
			                                  "lg_pos,"+
			                                  "lg_mestrado,"+
			                                  "lg_doutorado,"+
			                                  "nr_inep,"+
			                                  "cd_modalidade_educarte) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getLgPos());
			pstmt.setInt(3,objeto.getLgMestrado());
			pstmt.setInt(4,objeto.getLgDoutorado());
			pstmt.setString(5,objeto.getNrInep());
			if(objeto.getCdModalidadeEducarte()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdModalidadeEducarte());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Professor objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Professor objeto, int cdProfessorOld) {
		return update(objeto, cdProfessorOld, null);
	}

	public static int update(Professor objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Professor objeto, int cdProfessorOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			Professor objetoTemp = get(objeto.getCdPessoa(), connect);
			if (objetoTemp == null) 
				pstmt = connect.prepareStatement("INSERT INTO acd_professor (cd_professor,"+
			                                  "lg_pos,"+
			                                  "lg_mestrado,"+
			                                  "lg_doutorado,"+
			                                  "nr_inep,"+
			                                  "cd_modalidade_educarte) VALUES (?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE acd_professor SET cd_professor=?,"+
												      		   "lg_pos=?,"+
												      		   "lg_mestrado=?,"+
												      		   "lg_doutorado=?,"+
												      		   "nr_inep=?,"+
												      		   "cd_modalidade_educarte=? WHERE cd_professor=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getLgPos());
			pstmt.setInt(3,objeto.getLgMestrado());
			pstmt.setInt(4,objeto.getLgDoutorado());
			pstmt.setString(5,objeto.getNrInep());
			if(objeto.getCdModalidadeEducarte()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdModalidadeEducarte());
			if (objetoTemp != null) {
				pstmt.setInt(7, cdProfessorOld!=0 ? cdProfessorOld : objeto.getCdPessoa());
			}
			pstmt.executeUpdate();
			if (PessoaFisicaDAO.update(objeto, connect)<=0) {
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
			System.err.println("Erro! ProfessorDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProfessor) {
		return delete(cdProfessor, null);
	}

	public static int delete(int cdProfessor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_professor WHERE cd_professor=?");
			pstmt.setInt(1, cdProfessor);
			pstmt.executeUpdate();
			if (PessoaFisicaDAO.delete(cdProfessor, connect)<=0) {
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
			System.err.println("Erro! ProfessorDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Professor get(int cdProfessor) {
		return get(cdProfessor, null);
	}

	public static Professor get(int cdProfessor, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_professor A, grl_pessoa_fisica B, grl_pessoa C WHERE A.cd_professor=B.cd_pessoa AND B.cd_pessoa = C.cd_pessoa AND A.cd_professor=?");
			pstmt.setInt(1, cdProfessor);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Professor(rs.getInt("cd_pessoa"),
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
						rs.getInt("cd_naturalidade"),
						rs.getInt("cd_escolaridade"),
						(rs.getTimestamp("dt_nascimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_nascimento").getTime()),
						rs.getString("nr_cpf"),
						rs.getString("sg_orgao_rg"),
						rs.getString("nm_mae"),
						rs.getString("nm_pai"),
						rs.getInt("tp_sexo"),
						rs.getInt("st_estado_civil"),
						rs.getString("nr_rg"),
						rs.getString("nr_cnh"),
						(rs.getTimestamp("dt_validade_cnh")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_validade_cnh").getTime()),
						(rs.getTimestamp("dt_primeira_habilitacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_primeira_habilitacao").getTime()),
						rs.getInt("tp_categoria_habilitacao"),
						rs.getInt("tp_raca"),
						rs.getInt("lg_deficiente_fisico"),
						rs.getString("nm_forma_tratamento"),
						rs.getInt("cd_estado_rg"),
						(rs.getTimestamp("dt_emissao_rg")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao_rg").getTime()),
						rs.getBytes("blb_fingerprint")==null?null:rs.getBytes("blb_fingerprint"),
						rs.getInt("cd_conjuge"),
						rs.getInt("qt_membros_familia"),
						rs.getFloat("vl_renda_familiar_per_capta"),
						rs.getInt("tp_nacionalidade"),
						rs.getInt("lg_pos"),
						rs.getInt("lg_mestrado"),
						rs.getInt("lg_doutorado"),
						rs.getString("nr_inep"),
						rs.getInt("cd_modalidade_educarte"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_professor");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Professor> getList() {
		return getList(null);
	}

	public static ArrayList<Professor> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Professor> list = new ArrayList<Professor>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Professor obj = ProfessorDAO.get(rsm.getInt("cd_professor"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_professor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
