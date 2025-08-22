package com.tivic.manager.acd;

import java.sql.*;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;

import java.util.ArrayList;

public class AlunoDAO{

	public static int insert(Aluno objeto) {
		return insert(objeto, null);
	}

	public static int insert(Aluno objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.grl.PessoaFisicaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_aluno (cd_aluno,"+
                    "cd_responsavel,"+
                    "nm_parentesco,"+
                    "nm_plano_saude,"+
                    "nm_medico,"+
                    "nm_profissao_pai,"+
                    "nm_profissao_mae,"+
                    "tp_filiacao,"+
                    "nr_inep,"+
                    "id_aluno,"+
                    "lg_bolsa_familia,"+
                    "lg_mais_educacao,"+
                    "nm_responsavel,"+
                    "tp_escolaridade_mae,"+
                    "tp_escolaridade_pai,"+
                    "tp_escolaridade_responsavel,"+
                    "lg_cadastro_problema,"+
                    "lg_pai_nao_declarado,"+
                    "lg_falta_documento,"+
                    "id_aluno_centaurus,"+
                    "nm_aluno_censo,"+
                    "nr_codigo_estadual,"+
                    "tp_avaliacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdResponsavel());
			pstmt.setString(3,objeto.getNmParentesco());
			pstmt.setString(4,objeto.getNmPlanoSaude());
			pstmt.setString(5,objeto.getNmMedico());
			pstmt.setString(6,objeto.getNmProfissaoPai());
			pstmt.setString(7,objeto.getNmProfissaoMae());
			pstmt.setInt(8,objeto.getTpFiliacao());
			pstmt.setString(9,objeto.getNrInep());
			pstmt.setString(10,objeto.getIdAluno());
			pstmt.setInt(11,objeto.getLgBolsaFamilia());
			pstmt.setInt(12,objeto.getLgMaisEducacao());
			pstmt.setString(13,objeto.getNmResponsavel());
			pstmt.setInt(14,objeto.getTpEscolaridadeMae());
			pstmt.setInt(15,objeto.getTpEscolaridadePai());
			pstmt.setInt(16,objeto.getTpEscolaridadeResponsavel());
			pstmt.setInt(17,objeto.getLgCadastroProblema());
			pstmt.setInt(18,objeto.getLgPaiNaoDeclarado());
			pstmt.setInt(19,objeto.getLgFaltaDocumento());
			pstmt.setInt(20,objeto.getIdAlunoCentaurus());
			pstmt.setString(21,objeto.getNmAlunoCenso());
			pstmt.setString(22,objeto.getNrCodigoEstadual());
			pstmt.setInt(23,objeto.getTpAvaliacao());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Aluno objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Aluno objeto, int cdAlunoOld) {
		return update(objeto, cdAlunoOld, null);
	}

	public static int update(Aluno objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Aluno objeto, int cdAlunoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			Aluno objetoTemp = get(objeto.getCdPessoa(), connect);
			if (objetoTemp == null) 
				pstmt = connect.prepareStatement("INSERT INTO acd_aluno (cd_aluno,"+
			                                  "cd_responsavel,"+
			                                  "nm_parentesco,"+
			                                  "nm_plano_saude,"+
			                                  "nm_medico,"+
			                                  "nm_profissao_pai,"+
			                                  "nm_profissao_mae,"+
			                                  "tp_filiacao,"+
			                                  "nr_inep,"+
			                                  "id_aluno,"+
			                                  "lg_bolsa_familia,"+
			                                  "lg_mais_educacao,"+
			                                  "nm_responsavel,"+
			                                  "tp_escolaridade_mae,"+
			                                  "tp_escolaridade_pai,"+
			                                  "tp_escolaridade_responsavel,"+
			                                  "lg_cadastro_problema,"+
			                                  "lg_pai_nao_declarado,"+
			                                  "lg_falta_documento,"+
			                                  "id_aluno_centaurus,"+
			                                  "nm_aluno_censo,"+
			                                  "nr_codigo_estadual,"+
			                                  "tp_avaliacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE acd_aluno SET cd_aluno=?,"+
												      		   "cd_responsavel=?,"+
												      		   "nm_parentesco=?,"+
												      		   "nm_plano_saude=?,"+
												      		   "nm_medico=?,"+
												      		   "nm_profissao_pai=?,"+
												      		   "nm_profissao_mae=?,"+
												      		   "tp_filiacao=?,"+
												      		   "nr_inep=?,"+
												      		   "id_aluno=?,"+
												      		   "lg_bolsa_familia=?,"+
												      		   "lg_mais_educacao=?,"+
												      		   "nm_responsavel=?,"+
												      		   "tp_escolaridade_mae=?,"+
												      		   "tp_escolaridade_pai=?,"+
												      		   "tp_escolaridade_responsavel=?,"+
												      		   "lg_cadastro_problema=?,"+
												      		   "lg_pai_nao_declarado=?,"+
												      		   "lg_falta_documento=?,"+
												      		   "id_aluno_centaurus=?,"+
												      		   "nm_aluno_censo=?,"+
												      		   "nr_codigo_estadual=?,"+
												      		   "tp_avaliacao=? WHERE cd_aluno=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdResponsavel());
			pstmt.setString(3,objeto.getNmParentesco());
			pstmt.setString(4,objeto.getNmPlanoSaude());
			pstmt.setString(5,objeto.getNmMedico());
			pstmt.setString(6,objeto.getNmProfissaoPai());
			pstmt.setString(7,objeto.getNmProfissaoMae());
			pstmt.setInt(8,objeto.getTpFiliacao());
			pstmt.setString(9,objeto.getNrInep());
			pstmt.setString(10,objeto.getIdAluno());
			pstmt.setInt(11,objeto.getLgBolsaFamilia());
			pstmt.setInt(12,objeto.getLgMaisEducacao());
			pstmt.setString(13,objeto.getNmResponsavel());
			pstmt.setInt(14,objeto.getTpEscolaridadeMae());
			pstmt.setInt(15,objeto.getTpEscolaridadePai());
			pstmt.setInt(16,objeto.getTpEscolaridadeResponsavel());
			pstmt.setInt(17,objeto.getLgCadastroProblema());
			pstmt.setInt(18,objeto.getLgPaiNaoDeclarado());
			pstmt.setInt(19,objeto.getLgFaltaDocumento());
			pstmt.setInt(20,objeto.getIdAlunoCentaurus());
			pstmt.setString(21,objeto.getNmAlunoCenso());
			pstmt.setString(22,objeto.getNrCodigoEstadual());
			pstmt.setInt(23,objeto.getTpAvaliacao());
			if (objetoTemp != null) {
				pstmt.setInt(24, cdAlunoOld!=0 ? cdAlunoOld : objeto.getCdPessoa());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.PessoaFisicaDAO.update(objeto, connect)<=0) {
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
			System.err.println("Erro! AlunoDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAluno) {
		return delete(cdAluno, null);
	}

	public static int delete(int cdAluno, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_aluno WHERE cd_aluno=?");
			pstmt.setInt(1, cdAluno);
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.PessoaFisicaDAO.delete(cdAluno, connect)<=0) {
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
			System.err.println("Erro! AlunoDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Aluno get(int cdAluno) {
		return get(cdAluno, null);
	}

	public static Aluno get(int cdAluno, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_aluno A, grl_pessoa_fisica B, grl_pessoa C WHERE A.cd_aluno=B.cd_pessoa AND B.cd_pessoa=C.cd_pessoa AND A.cd_aluno=?");
			pstmt.setInt(1, cdAluno);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Aluno(rs.getInt("cd_pessoa"),
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
						rs.getString("nr_celular2"),
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
						rs.getInt("cd_responsavel"),
						rs.getString("nm_parentesco"),
						rs.getString("nm_plano_saude"),
						rs.getString("nm_medico"),
						rs.getString("nm_profissao_pai"),
						rs.getString("nm_profissao_mae"),
						rs.getInt("tp_filiacao"),
						rs.getString("nr_inep"),
						rs.getString("id_aluno"),
						rs.getInt("lg_bolsa_familia"),
						rs.getInt("lg_mais_educacao"),
						rs.getString("nm_responsavel"),
						rs.getInt("tp_escolaridade_mae"),
						rs.getInt("tp_escolaridade_pai"),
						rs.getInt("tp_escolaridade_responsavel"),
						rs.getInt("lg_cadastro_problema"),
						rs.getInt("lg_pai_nao_declarado"),
						rs.getInt("lg_falta_documento"),
						rs.getInt("id_aluno_centaurus"),
						rs.getString("nm_aluno_censo"),
						rs.getString("nr_codigo_estadual"),
						rs.getInt("tp_avaliacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_aluno");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Aluno> getList() {
		return getList(null);
	}

	public static ArrayList<Aluno> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Aluno> list = new ArrayList<Aluno>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Aluno obj = AlunoDAO.get(rsm.getInt("cd_aluno"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_aluno", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
