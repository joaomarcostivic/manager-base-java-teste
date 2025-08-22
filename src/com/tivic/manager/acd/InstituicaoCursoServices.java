package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.adm.ProdutoServicoPrecoServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.TipoOcorrenciaServices;
import com.tivic.manager.util.Util;

public class InstituicaoCursoServices {
	
	public static Result save(InstituicaoCurso instituicaoCurso, int cdUsuario){
		return save(instituicaoCurso, cdUsuario, null);
	}

	public static Result save(InstituicaoCurso instituicaoCurso, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(instituicaoCurso==null)
				return new Result(-1, "Erro ao salvar. InstituicaoCurso é nulo");

			int retorno;
			if(InstituicaoCursoDAO.get(instituicaoCurso.getCdInstituicao(), instituicaoCurso.getCdCurso(), instituicaoCurso.getCdPeriodoLetivo(), connect) == null){
				retorno = InstituicaoCursoDAO.insert(instituicaoCurso, connect);
				
				int cdTipoOcorrenciaCadastroInstituicaoCurso = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_INSTITUICAO_CURSO, connect).getCdTipoOcorrencia();
				OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, instituicaoCurso.getCdInstituicao(), "Curso "+CursoDAO.get(instituicaoCurso.getCdCurso(), connect).getNmProdutoServico()+" cadastrado na Instituicao " + InstituicaoDAO.get(instituicaoCurso.getCdInstituicao(), connect).getNmPessoa() + " no periodo letivo " + InstituicaoPeriodoDAO.get(instituicaoCurso.getCdPeriodoLetivo(), connect).getNmPeriodoLetivo(), Util.getDataAtual(), cdTipoOcorrenciaCadastroInstituicaoCurso, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicaoCurso.getCdInstituicao(), Util.getDataAtual(), cdUsuario, instituicaoCurso.getCdPeriodoLetivo());
				Result ret = OcorrenciaInstituicaoServices.save(ocorrencia, connect);
				if(ret.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return ret;
				}
				
			}
			else {
				retorno = InstituicaoCursoDAO.update(instituicaoCurso, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOCURSO", instituicaoCurso);
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
	public static Result remove(int cdInstituicao, int cdCurso, int cdPeriodoLetivo, int cdUsuario){
		return remove(cdInstituicao, cdCurso, cdPeriodoLetivo, cdUsuario, false, null);
	}
	public static Result remove(int cdInstituicao, int cdCurso, int cdPeriodoLetivo, int cdUsuario, Connection connect){
		return remove(cdInstituicao, cdCurso, cdPeriodoLetivo, cdUsuario, false, connect);
	}
	public static Result remove(int cdInstituicao, int cdCurso, int cdPeriodoLetivo, int cdUsuario, boolean cascade){
		return remove(cdInstituicao, cdCurso, cdPeriodoLetivo, cdUsuario, cascade, null);
	}
	public static Result remove(int cdInstituicao, int cdCurso, int cdPeriodoLetivo, int cdUsuario, boolean cascade, Connection connect){
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
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_periodo_letivo", "" + cdPeriodoLetivo, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_curso", "" + cdCurso, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.st_turma", "" + TurmaServices.ST_INATIVO, ItemComparator.DIFFERENT, Types.INTEGER));
			ResultSetMap rsmTurmas = TurmaServices.find(criterios, connect);
			if(rsmTurmas.size() > 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Não é possível remover esse curso pois ainda há turmas ativas, pendentes ou concluídas ligadas a ele");
			}
			
			if(!cascade || retorno>0)
				retorno = InstituicaoCursoDAO.delete(cdInstituicao, cdCurso, cdPeriodoLetivo, connect);
			
			int cdTipoOcorrenciaCadastroInstituicaoCurso = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_REMOCAO_INSTITUICAO_CURSO, connect).getCdTipoOcorrencia();
			OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, cdInstituicao, "Curso "+CursoDAO.get(cdCurso, connect).getNmProdutoServico()+" removido da Instituicao " + InstituicaoDAO.get(cdInstituicao, connect).getNmPessoa() + " no periodo letivo de " + InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect).getNmPeriodoLetivo(), Util.getDataAtual(), cdTipoOcorrenciaCadastroInstituicaoCurso, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, cdInstituicao, Util.getDataAtual(), cdUsuario, cdPeriodoLetivo);
			Result ret = OcorrenciaInstituicaoServices.save(ocorrencia, connect);
			if(ret.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return ret;
			}
			
			if(retorno<=0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-2, "Este registro estão vinculado a outros e não pode ser excluído!");
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
	
	public static Result removeByCurso(int cdCurso, int cdUsuario) {
		return removeByCurso(cdCurso, cdUsuario, null);
	}
	
	public static Result removeByCurso(int cdCurso, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsm = getAll();
			while (rsm.next()) {
				
				int cdPeriodoLetivoAtual = 0;
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(rsm.getInt("cd_instituicao"), connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
				
				
				if (rsm.getInt("cd_curso") == cdCurso) {
					Result retorno = remove(rsm.getInt("cd_instituicao_curso"), cdCurso, cdPeriodoLetivoAtual, cdUsuario, true, connect);
					if(retorno.getCode()<0){
						Conexao.rollback(connect);
						return retorno;
					}
				}
			}
			
			if (isConnectionNull)
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
	
	public static Result removeByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_periodo_letivo", "" + cdPeriodoLetivo, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = InstituicaoCursoDAO.find(criterios, connect);
			while (rsm.next()) {
				int ret = InstituicaoCursoDAO.delete(cdInstituicao, rsm.getInt("cd_curso"), cdPeriodoLetivo, connect);
				if(ret<0){
					Conexao.rollback(connect);
					return new Result(ret, "Erro ao excluir instituição curso");
				}
			}
			
			if (isConnectionNull)
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_curso");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCursoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCursoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_curso", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static int deleteTabelaPreco(int cdCurso, int cdTabelaPreco) {
		return deleteTabelaPreco(cdCurso, cdTabelaPreco, null);
	}

	public static int deleteTabelaPreco(int cdCurso, int cdTabelaPreco, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			/* verifica se a tabela de preco a ser excluida estah em uso */
			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_turma " +
					"FROM acd_turma A " +
					"WHERE A.cd_tabela_preco = ?");
			pstmt.setInt(1, cdTabelaPreco);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (ProdutoServicoPrecoServices.delete(cdTabelaPreco, cdCurso, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			pstmt = connection.prepareStatement("DELETE " +
					"FROM adm_tabela_preco_regra " +
					"WHERE cd_produto_servico = ? " +
					"  AND cd_tabela_preco = ?");
			pstmt.setInt(1, cdCurso);
			pstmt.setInt(2, cdTabelaPreco);
			pstmt.execute();

			/* periodos e disciplinas configurados na referida tabela de preco */
			pstmt = connection.prepareStatement("SELECT A.cd_produto_servico " +
					"FROM grl_produto_servico A " +
					"WHERE (A.cd_produto_servico IN (SELECT C.cd_produto_servico_componente " +
					"			 					 FROM acd_curso_periodo B, grl_produto_servico_composicao C " +
					"								 WHERE B.cd_periodo = C.cd_composicao " +
					"								   AND C.cd_produto_servico = ?) OR " +
					"	    A.cd_produto_servico in (SELECT D.cd_disciplina " +
					"								 FROM acd_disciplina D)) AND " +
					"		A.cd_produto_servico IN (SELECT DISTINCT E.cd_produto_servico " +
					"								 FROM adm_produto_servico_preco E " +
					"								 WHERE E.cd_tabela_preco = ?)");
			pstmt.setInt(1, cdCurso);
			pstmt.setInt(2, cdCurso);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (ProdutoServicoPrecoServices.delete(cdTabelaPreco, rs.getInt("cd_produto_servico"), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

				pstmt = connection.prepareStatement("DELETE " +
						"FROM adm_tabela_preco_regra " +
						"WHERE cd_produto_servico = ? " +
						"  AND cd_tabela_preco = ?");
				pstmt.setInt(1, rs.getInt("cd_produto_servico"));
				pstmt.setInt(2, cdTabelaPreco);
				pstmt.execute();
			}

			if (isConnectionNull)
				connection.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllTabelasPrecos(int cdInstituicao, int cdCurso) {
		return getAllTabelasPrecos(cdInstituicao, cdCurso, null);
	}

	public static ResultSetMap getAllTabelasPrecos(int cdInstituicao, int cdCurso, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_tabela_preco, A.nm_tabela_preco, " +
					"B.vl_preco AS vl_curso, C.pr_desconto " +
					"FROM adm_tabela_preco A " +
					"JOIN adm_produto_servico_preco B ON (A.cd_tabela_preco = B.cd_tabela_preco) " +
					"LEFT OUTER JOIN adm_tabela_preco_regra C ON (A.cd_tabela_preco = C.cd_tabela_preco AND " +
					"											  C.cd_produto_servico = B.cd_produto_servico)" +
					"WHERE A.cd_empresa = ? " +
					"  AND B.cd_produto_servico = ? " +
					"  AND B.dt_termino_validade IS NULL");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdCurso);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllPlanosPagamento(int cdInstituicao, int cdCurso, int cdTabelaPreco) {
		return getAllPlanosPagamento(cdInstituicao, cdCurso, cdTabelaPreco,null);
	}

	public static ResultSetMap getAllPlanosPagamento(int cdInstituicao, int cdCurso, int cdTabelaPreco, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement("SELECT " + (cdTabelaPreco<=0 ? " D.vl_preco, " : "") +
					"A.cd_plano_pagto_produto_servico, " +
					"A.cd_plano_pagamento, B.nm_plano_pagamento, " +
					"A.cd_forma_pagamento, C.nm_forma_pagamento, C.sg_forma_pagamento " +
					"FROM adm_plano_pagto_produto_servico A " +
					"JOIN adm_plano_pagamento B ON (A.cd_plano_pagamento = B.cd_plano_pagamento) " +
					"LEFT OUTER JOIN adm_forma_pagamento C ON (A.cd_forma_pagamento = C.cd_forma_pagamento) " +
				    (cdTabelaPreco<=0 ? " LEFT OUTER JOIN adm_produto_servico_preco D ON (A.cd_tabela_preco = D.cd_tabela_preco AND " +
					"												 A.cd_produto_servico = D.cd_produto_servico AND " +
					"												 D.dt_termino_validade IS NULL) " : "") +
					"WHERE A.cd_empresa = ? " +
					"  AND A.cd_produto_servico = ? " +
					(cdTabelaPreco<=0 ? "" : "  AND A.cd_tabela_preco = ? "));
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdCurso);
			if (cdTabelaPreco>0)
				pstmt.setInt(3, cdTabelaPreco);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllPrecos(int cdInstituicao, int cdCurso, int cdTabelaPreco) {
		return getAllPrecos(cdInstituicao, cdCurso, cdTabelaPreco, null);
	}

	public static ResultSetMap getAllPrecos(int cdInstituicao, int cdCurso, int cdTabelaPreco, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			/* consulta de precos por periodos */
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_periodo, C.cd_produto_servico AS cd_produto_servico, " +
					"C.nm_produto_servico AS nm_periodo, D.vl_preco, E.pr_margem_maxima " +
					"FROM acd_curso_periodo A " +
					"JOIN grl_produto_servico_composicao B ON (A.cd_periodo = B.cd_composicao) " +
					"JOIN grl_produto_servico C ON (B.cd_produto_servico_componente = C.cd_produto_servico) " +
					"LEFT OUTER JOIN adm_produto_servico_preco D ON (C.cd_produto_servico = D.cd_produto_servico AND " +
					"												 D.cd_tabela_preco = ? AND " +
					"												 D.dt_termino_validade IS NULL) " +
					"LEFT OUTER JOIN adm_tabela_preco_regra E ON (C.cd_produto_servico = E.cd_produto_servico AND " +
					"											  E.cd_tabela_preco = ?) " +
					"WHERE B.cd_empresa = ? " +
					"  AND B.cd_produto_servico = ?");
			pstmt.setInt(1, cdTabelaPreco);
			pstmt.setInt(2, cdTabelaPreco);
			pstmt.setInt(3, cdInstituicao);
			pstmt.setInt(4, cdCurso);
			ResultSetMap rsmPeriodos = new ResultSetMap(pstmt.executeQuery());
			while (rsmPeriodos.next()) {
				rsmPeriodos.getRegister().put("NM_ITEM", rsmPeriodos.getString("NM_PERIODO"));
			}
			rsmPeriodos.beforeFirst();

			String sql = "SELECT DISTINCT C.cd_produto_servico AS cd_disciplina, " +
						 "C.nm_produto_servico AS nm_disciplina, D.vl_preco, E.pr_margem_maxima " +
						 "FROM acd_curso_disciplina A " +
						 "JOIN grl_produto_servico_composicao B ON (A.cd_curso_disciplina = B.cd_composicao) " +
						 "JOIN grl_produto_servico C ON (B.cd_produto_servico_componente = C.cd_produto_servico) " +
						 "LEFT OUTER JOIN adm_produto_servico_preco D ON (C.cd_produto_servico = D.cd_produto_servico AND " +
						 "												 D.cd_tabela_preco = ? AND " +
						 "												 D.dt_termino_validade IS NULL) " +
						 "LEFT OUTER JOIN adm_tabela_preco_regra E ON (C.cd_produto_servico = E.cd_produto_servico AND " +
						 "											  E.cd_tabela_preco = ?) " +
						 "WHERE B.cd_empresa = ? " +
						 "  AND (B.cd_produto_servico = ?";
			while (rsmPeriodos.next()) {
				sql += " OR B.cd_produto_servico = " + rsmPeriodos.getInt("cd_produto_servico");
			}
			rsmPeriodos.beforeFirst();
			sql += ")";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, cdTabelaPreco);
			pstmt.setInt(2, cdTabelaPreco);
			pstmt.setInt(3, cdInstituicao);
			pstmt.setInt(4, cdCurso);
			ResultSetMap rsmDisciplinas = new ResultSetMap(pstmt.executeQuery());
			while (rsmDisciplinas.next()) {
				rsmDisciplinas.getRegister().put("NM_ITEM", rsmDisciplinas.getString("NM_DISCIPLINA"));
			}
			rsmDisciplinas.beforeFirst();

			ResultSetMap rsm = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NM_ITEM", "Períodos/Módulos");
			register.put("subResultSetMap", rsmPeriodos);
			rsm.addRegister(register);
			register = new HashMap<String, Object>();
			register.put("NM_ITEM", "Disciplinas");
			register.put("subResultSetMap", rsmDisciplinas);
			rsm.addRegister(register);
			rsm.beforeFirst();

			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
}
