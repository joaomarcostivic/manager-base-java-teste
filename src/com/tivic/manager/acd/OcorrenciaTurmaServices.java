package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.TipoOcorrenciaServices;

public class OcorrenciaTurmaServices {

	public static Result save(OcorrenciaTurma ocorrenciaTurma){
		return save(ocorrenciaTurma, null);
	}

	public static Result save(OcorrenciaTurma ocorrenciaTurma, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ocorrenciaTurma==null)
				return new Result(-1, "Erro ao salvar. OcorrenciaTurma é nulo");

			int retorno;
			if(ocorrenciaTurma.getCdOcorrencia()==0){
				retorno = OcorrenciaTurmaDAO.insert(ocorrenciaTurma, connect);
				ocorrenciaTurma.setCdOcorrencia(retorno);
			}
			else {
				retorno = OcorrenciaTurmaDAO.update(ocorrenciaTurma, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "OCORRENCIATURMA", ocorrenciaTurma);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(int cdOcorrencia){
		return remove(cdOcorrencia, false, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade){
		return remove(cdOcorrencia, cascade, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = OcorrenciaTurmaDAO.delete(cdOcorrencia, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_ocorrencia_turma");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_ocorrencia_turma A, grl_ocorrencia B WHERE A.cd_ocorrencia = B.cd_ocorrencia ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getAllByTurmaSituacaoCenso( int cdTurma ) {
		return getAllByTurmaSituacaoCenso(cdTurma, null);
	}

	public static ResultSetMap getAllByTurmaSituacaoCenso(int cdTurma, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT *, P.nm_pessoa AS nm_usuario FROM acd_ocorrencia_turma A"
					+ "							JOIN grl_ocorrencia B ON (A.cd_ocorrencia = B.cd_ocorrencia) "
					+ "							JOIN seg_usuario U ON (B.cd_usuario = U.cd_usuario) "
					+ "							JOIN grl_pessoa P ON (U.cd_pessoa = P.cd_pessoa) "
					+ "							LEFT OUTER JOIN grl_tipo_ocorrencia C ON (B.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia) "
					+ "						  WHERE A.cd_turma = "+cdTurma
					+ "							AND B.cd_tipo_ocorrencia IN ("+InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO+", "+InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO+")"
					+ "						  ORDER BY dt_ocorrencia DESC");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaServices.getAllByTurmaSituacaoCenso: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaServices.getAllByTurmaSituacaoCenso: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByTurmaMatricula( int cdTurma ) {
		return getAllByTurmaMatricula(cdTurma, null);
	}

	public static ResultSetMap getAllByTurmaMatricula(int cdTurma, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT *, P.nm_pessoa AS nm_usuario, 'Turma' AS CL_NM_TP_OCORRENCIA FROM acd_ocorrencia_turma A"
					+ "							JOIN grl_ocorrencia B ON (A.cd_ocorrencia = B.cd_ocorrencia) "
					+ "							JOIN seg_usuario U ON (B.cd_usuario = U.cd_usuario) "
					+ "							JOIN grl_pessoa P ON (U.cd_pessoa = P.cd_pessoa) "
					+ "							LEFT OUTER JOIN grl_tipo_ocorrencia C ON (B.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia) "
					+ "						  WHERE A.cd_turma = "+cdTurma
					+ "						  ORDER BY dt_ocorrencia DESC");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			pstmt = connect.prepareStatement("SELECT *, P.nm_pessoa AS nm_usuario, 'Matrícula' AS CL_NM_TP_OCORRENCIA FROM acd_ocorrencia_matricula A"
					+ "							JOIN grl_ocorrencia B ON (A.cd_ocorrencia = B.cd_ocorrencia) "
					+ "							JOIN acd_matricula M ON (A.cd_matricula_origem = M.cd_matricula OR A.cd_matricula_destino = M.cd_matricula) "
					+ "							JOIN seg_usuario U ON (B.cd_usuario = U.cd_usuario) "
					+ "							JOIN grl_pessoa P ON (U.cd_pessoa = P.cd_pessoa) "
					+ "							LEFT OUTER JOIN grl_tipo_ocorrencia C ON (B.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia) "
					+ "						  WHERE M.cd_turma = " + cdTurma
					+ "						  ORDER BY dt_ocorrencia DESC");
			ResultSetMap rsm2 = new ResultSetMap(pstmt.executeQuery());
			
			rsm.getLines().addAll(rsm2.getLines());
			
//			pstmt = connect.prepareStatement("SELECT *, P.nm_pessoa AS nm_usuario, 'Enturmação' AS CL_NM_TP_OCORRENCIA FROM acd_ocorrencia_pessoa_oferta A"
//					+ "							JOIN grl_ocorrencia B ON (A.cd_ocorrencia = B.cd_ocorrencia) "
//					+ "							JOIN seg_usuario U ON (B.cd_usuario = U.cd_usuario) "
//					+ "							JOIN grl_pessoa P ON (U.cd_pessoa = P.cd_pessoa) "
//					+ "							LEFT OUTER JOIN grl_tipo_ocorrencia C ON (B.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia) "
//					+ "						  WHERE M.cd_turma = " + cdTurma
//					+ "						  ORDER BY dt_ocorrencia DESC");
//			ResultSetMap rsm3 = new ResultSetMap(pstmt.executeQuery());
//			
//			rsm.getLines().addAll(rsm3.getLines());
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DT_OCORRENCIA DESC");
			rsm.orderBy(fields);
			
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaServices.getAllByTurmaSituacaoCenso: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaServices.getAllByTurmaSituacaoCenso: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap getAllOfHorariosByInstituicao( int cdInstituicao ) {
		return getAllOfHorariosByInstituicao(cdInstituicao, null);
	}

	public static ResultSetMap getAllOfHorariosByInstituicao(int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int cdPeriodoLetivo = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			
			pstmt = connect.prepareStatement("SELECT *, P.nm_pessoa AS nm_usuario FROM grl_ocorrencia B "
					+ "							JOIN seg_usuario U ON (B.cd_usuario = U.cd_usuario) "
					+ "							JOIN grl_pessoa P ON (U.cd_pessoa = P.cd_pessoa) "
					+ "							JOIN grl_tipo_ocorrencia C ON (B.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia) "
					+ "							JOIN acd_ocorrencia_turma D ON (B.cd_ocorrencia = D.cd_ocorrencia) "
					+ "							JOIN acd_turma E ON (D.cd_turma = E.cd_turma) "
					+ "						  WHERE E.cd_instituicao = "+cdInstituicao
					+ "							AND E.cd_periodo_letivo = " +cdPeriodoLetivo
					+ "							AND C.cd_tipo_ocorrencia IN ("+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ADICIONAR_HORARIO_TURMA, connect).getCdTipoOcorrencia()
																		 +", "+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_REMOVER_HORARIO_TURMA, connect).getCdTipoOcorrencia()+")"
					+ "						  ORDER BY dt_ocorrencia DESC");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			pstmt = connect.prepareStatement("SELECT *, P.nm_pessoa AS nm_usuario FROM grl_ocorrencia B "
					+ "							JOIN seg_usuario U ON (B.cd_usuario = U.cd_usuario) "
					+ "							JOIN grl_pessoa P ON (U.cd_pessoa = P.cd_pessoa) "
					+ "							JOIN grl_tipo_ocorrencia C ON (B.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia) "
					+ "							JOIN acd_ocorrencia_instituicao D ON (B.cd_ocorrencia = D.cd_ocorrencia) "
					+ "						  WHERE D.cd_instituicao = "+cdInstituicao
					+ "							AND D.cd_periodo_letivo = " +cdPeriodoLetivo
					+ "							AND C.cd_tipo_ocorrencia IN ("+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ADICIONAR_HORARIO_TURMA, connect).getCdTipoOcorrencia()
																		 +", "+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_REMOVER_HORARIO_TURMA, connect).getCdTipoOcorrencia()
																		 +", "+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_INSTITUICAO_HORARIO, connect).getCdTipoOcorrencia()
																		 +", "+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_REMOCAO_INSTITUICAO_HORARIO, connect).getCdTipoOcorrencia()
																		 +", "+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_INSTITUICAO_HORARIO, connect).getCdTipoOcorrencia()+")"
					+ "						  ORDER BY dt_ocorrencia DESC");
			ResultSetMap rsm2 = new ResultSetMap(pstmt.executeQuery());
			
			rsm.getLines().addAll(rsm2.getLines());
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DT_OCORRENCIA DESC");
			rsm.orderBy(fields);
			
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaServices.getAllByTurmaSituacaoCenso: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaServices.getAllByTurmaSituacaoCenso: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
