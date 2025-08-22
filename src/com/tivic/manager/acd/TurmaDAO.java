package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class TurmaDAO{

	public static int insert(Turma objeto) {
		return insert(objeto, null);
	}

	public static int insert(Turma objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_turma", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTurma(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_turma (cd_turma,"+
			                                  "cd_matriz,"+
			                                  "cd_periodo_letivo,"+
			                                  "nm_turma,"+
			                                  "dt_abertura,"+
			                                  "dt_conclusao,"+
			                                  "tp_turno,"+
			                                  "cd_categoria_mensalidade,"+
			                                  "cd_categoria_matricula,"+
			                                  "st_turma,"+
			                                  "cd_tabela_preco,"+
			                                  "cd_instituicao,"+
			                                  "cd_curso,"+
			                                  "qt_vagas,"+
			                                  "cd_curso_modulo,"+
			                                  "nr_inep,"+
			                                  "qt_dias_semana_atividade,"+
			                                  "tp_atendimento,"+
			                                  "tp_modalidade_ensino,"+
			                                  "id_turma,"+
			                                  "tp_educacao_infantil,"+
			                                  "lg_mais_educa,"+
			                                  "cd_turma_anterior,"+
			                                  "tp_turno_atividade_complementar,"+
			                                  "tp_local_diferenciado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdMatriz()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMatriz());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.setString(4,objeto.getNmTurma());
			if(objeto.getDtAbertura()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtAbertura().getTimeInMillis()));
			if(objeto.getDtConclusao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtConclusao().getTimeInMillis()));
			pstmt.setInt(7,objeto.getTpTurno());
			if(objeto.getCdCategoriaMensalidade()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdCategoriaMensalidade());
			if(objeto.getCdCategoriaMatricula()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdCategoriaMatricula());
			pstmt.setInt(10,objeto.getStTurma());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdTabelaPreco());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdInstituicao());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdCurso());
			pstmt.setInt(14,objeto.getQtVagas());
			if(objeto.getCdCursoModulo()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdCursoModulo());
			pstmt.setString(16,objeto.getNrInep());
			pstmt.setInt(17,objeto.getQtDiasSemanaAtividade());
			pstmt.setInt(18,objeto.getTpAtendimento());
			pstmt.setInt(19,objeto.getTpModalidadeEnsino());
			pstmt.setString(20,objeto.getIdTurma());
			pstmt.setInt(21,objeto.getTpEducacaoInfantil());
			pstmt.setInt(22,objeto.getLgMaisEduca());
			if(objeto.getCdTurmaAnterior()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdTurmaAnterior());
			pstmt.setInt(24,objeto.getTpTurnoAtividadeComplementar());
			pstmt.setInt(25,objeto.getTpLocalDiferenciado());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Turma objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Turma objeto, int cdTurmaOld) {
		return update(objeto, cdTurmaOld, null);
	}

	public static int update(Turma objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Turma objeto, int cdTurmaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_turma SET cd_turma=?,"+
												      		   "cd_matriz=?,"+
												      		   "cd_periodo_letivo=?,"+
												      		   "nm_turma=?,"+
												      		   "dt_abertura=?,"+
												      		   "dt_conclusao=?,"+
												      		   "tp_turno=?,"+
												      		   "cd_categoria_mensalidade=?,"+
												      		   "cd_categoria_matricula=?,"+
												      		   "st_turma=?,"+
												      		   "cd_tabela_preco=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_curso=?,"+
												      		   "qt_vagas=?,"+
												      		   "cd_curso_modulo=?,"+
												      		   "nr_inep=?,"+
												      		   "qt_dias_semana_atividade=?,"+
												      		   "tp_atendimento=?,"+
												      		   "tp_modalidade_ensino=?,"+
												      		   "id_turma=?,"+
												      		   "tp_educacao_infantil=?,"+
												      		   "lg_mais_educa=?,"+
												      		   "cd_turma_anterior=?,"+
												      		   "tp_turno_atividade_complementar=?,"+
												      		   "tp_local_diferenciado=? WHERE cd_turma=?");
			pstmt.setInt(1,objeto.getCdTurma());
			if(objeto.getCdMatriz()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMatriz());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.setString(4,objeto.getNmTurma());
			if(objeto.getDtAbertura()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtAbertura().getTimeInMillis()));
			if(objeto.getDtConclusao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtConclusao().getTimeInMillis()));
			pstmt.setInt(7,objeto.getTpTurno());
			if(objeto.getCdCategoriaMensalidade()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdCategoriaMensalidade());
			if(objeto.getCdCategoriaMatricula()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdCategoriaMatricula());
			pstmt.setInt(10,objeto.getStTurma());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdTabelaPreco());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdInstituicao());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdCurso());
			pstmt.setInt(14,objeto.getQtVagas());
			if(objeto.getCdCursoModulo()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdCursoModulo());
			pstmt.setString(16,objeto.getNrInep());
			pstmt.setInt(17,objeto.getQtDiasSemanaAtividade());
			pstmt.setInt(18,objeto.getTpAtendimento());
			pstmt.setInt(19,objeto.getTpModalidadeEnsino());
			pstmt.setString(20,objeto.getIdTurma());
			pstmt.setInt(21,objeto.getTpEducacaoInfantil());
			pstmt.setInt(22,objeto.getLgMaisEduca());
			if(objeto.getCdTurmaAnterior()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdTurmaAnterior());
			pstmt.setInt(24,objeto.getTpTurnoAtividadeComplementar());
			pstmt.setInt(25,objeto.getTpLocalDiferenciado());
			pstmt.setInt(26, cdTurmaOld!=0 ? cdTurmaOld : objeto.getCdTurma());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTurma) {
		return delete(cdTurma, null);
	}

	public static int delete(int cdTurma, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_turma WHERE cd_turma=?");
			pstmt.setInt(1, cdTurma);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Turma get(int cdTurma) {
		return get(cdTurma, null);
	}

	public static Turma get(int cdTurma, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_turma WHERE cd_turma=?");
			pstmt.setInt(1, cdTurma);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Turma(rs.getInt("cd_turma"),
						rs.getInt("cd_matriz"),
						rs.getInt("cd_periodo_letivo"),
						rs.getString("nm_turma"),
						(rs.getTimestamp("dt_abertura")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_abertura").getTime()),
						(rs.getTimestamp("dt_conclusao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_conclusao").getTime()),
						rs.getInt("tp_turno"),
						rs.getInt("cd_categoria_mensalidade"),
						rs.getInt("cd_categoria_matricula"),
						rs.getInt("st_turma"),
						rs.getInt("cd_tabela_preco"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_curso"),
						rs.getInt("qt_vagas"),
						rs.getInt("cd_curso_modulo"),
						rs.getString("nr_inep"),
						rs.getInt("qt_dias_semana_atividade"),
						rs.getInt("tp_atendimento"),
						rs.getInt("tp_modalidade_ensino"),
						rs.getString("id_turma"),
						rs.getInt("tp_educacao_infantil"),
						rs.getInt("lg_mais_educa"),
						rs.getInt("cd_turma_anterior"),
						rs.getInt("tp_turno_atividade_complementar"),
						rs.getInt("tp_local_diferenciado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_turma");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Turma> getList() {
		return getList(null);
	}

	public static ArrayList<Turma> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Turma> list = new ArrayList<Turma>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Turma obj = TurmaDAO.get(rsm.getInt("cd_turma"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_turma", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
