package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class OfertaAvaliacaoServices {

	public static final int ST_OFERTA_AVALIACAO_ABERTA = 0;
	public static final int ST_OFERTA_AVALIACAO_APLICADA = 1;
	public static final int ST_OFERTA_AVALIACAO_AVALIADA = 2;
	public static final int ST_OFERTA_AVALIACAO_CANCELADA = 3;
	

	public static final String[] situacaoOfertaAvaliacao = {"Aberta", "Aplicada", "Avaliada", "Cancelada"};
	
	public static Result save(OfertaAvaliacao ofertaAvaliacao, int cdUsuario){
		return save(ofertaAvaliacao, null, cdUsuario, null);
	}
	
	public static Result save(OfertaAvaliacao ofertaAvaliacao, OfertaAvaliacao ofertaAvaliacaoAnterior, int cdUsuario){
		return save(ofertaAvaliacao, ofertaAvaliacaoAnterior, cdUsuario, null);
	}
	
	public static Result save(OfertaAvaliacao ofertaAvaliacao, int cdUsuario, Connection connect){
		return save(ofertaAvaliacao, null, cdUsuario, null);
	}
	
	public static Result save(OfertaAvaliacao ofertaAvaliacao, OfertaAvaliacao ofertaAvaliacaoAnterior, int cdUsuario, Connection connect){
		return save(ofertaAvaliacao, ofertaAvaliacaoAnterior, null, cdUsuario, connect);
	}
	
	public static Result save(OfertaAvaliacao ofertaAvaliacao, OfertaAvaliacao ofertaAvaliacaoAnterior, ArrayList<DisciplinaAvaliacaoAluno> disciplinasAvaliacaoAluno, int cdUsuario){
		return save(ofertaAvaliacao, ofertaAvaliacaoAnterior, disciplinasAvaliacaoAluno, cdUsuario, null);
	}

	public static Result save(OfertaAvaliacao ofertaAvaliacao, OfertaAvaliacao ofertaAvaliacaoAnterior, ArrayList<DisciplinaAvaliacaoAluno> disciplinasAvaliacaoAluno, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ofertaAvaliacao==null)
				return new Result(-1, "Erro ao salvar. OfertaAvaliacao é nulo");

			
			//RETIRADA VALIDACAO ENQUANTO AS ATIVIDADES FOREM COLOCADAS NESSA TABELA
//			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
//			criterios.add(new ItemComparator("A.cd_oferta", "" + ofertaAvaliacao.getCdOferta(), ItemComparator.EQUAL, Types.INTEGER));
//			criterios.add(new ItemComparator("dt_aula", "" + Util.convCalendarStringSql(ofertaAvaliacao.getDtAvaliacao()), ItemComparator.EQUAL, Types.VARCHAR));
//			ResultSetMap rsmAula = AulaServices.find(criterios, connect);
//			if(!rsmAula.next()){
//				if(isConnectionNull)
//					Conexao.rollback(connect);
//				return new Result(-1, "Não existe nenhuma aula cadastrada para essa data");
//			}
			
			
			if(ofertaAvaliacao.getVlPeso() > 0) {
				ResultSetMap rsmDisciplinasAvaliacao = DisciplinaAvaliacaoAlunoServices.getAllByOfertaAvaliacao(ofertaAvaliacao.getCdOfertaAvaliacao(), ofertaAvaliacao.getCdOferta(), connect);
				while(rsmDisciplinasAvaliacao.next()){
					if(rsmDisciplinasAvaliacao.getFloat("VL_CONCEITO") > ofertaAvaliacao.getVlPeso()){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "A avaliação não pode ser colocada em um valor menor do que a nota já colocada de um aluno");
					}
				}
				rsmDisciplinasAvaliacao.beforeFirst();
			}
			
			int retorno;
			if(OfertaAvaliacaoDAO.get(ofertaAvaliacao.getCdOfertaAvaliacao(), ofertaAvaliacao.getCdOferta(), connect)==null){
				retorno = OfertaAvaliacaoDAO.insert(ofertaAvaliacao, connect);
				ofertaAvaliacao.setCdOfertaAvaliacao(retorno);
			}
			else {
				retorno = OfertaAvaliacaoDAO.update(ofertaAvaliacao, connect);
			}
			
			if(disciplinasAvaliacaoAluno != null) {
				for(DisciplinaAvaliacaoAluno disciplinaAvaliacaoAluno : disciplinasAvaliacaoAluno) {
					disciplinaAvaliacaoAluno.setCdOfertaAvaliacao(ofertaAvaliacao.getCdOfertaAvaliacao());
					
					Result resultDisciplinaAvaliacaoAluno = DisciplinaAvaliacaoAlunoServices.save(disciplinaAvaliacaoAluno, 0, connect);
					if(resultDisciplinaAvaliacaoAluno.getCode() < 0) {
						if(isConnectionNull)
							Conexao.rollback(connect);
						return resultDisciplinaAvaliacaoAluno;
					}
					
				}
			}
			
			int cdTipoOcorrencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_OFERTA_AVALIACAO, connect).getCdTipoOcorrencia();
			if(ofertaAvaliacaoAnterior != null && cdUsuario > 0 && cdTipoOcorrencia > 0){
				String txtOcorrencia = "";
				if(ofertaAvaliacaoAnterior.getCdOfertaAvaliacao() > 0 && ofertaAvaliacaoAnterior.getCdOferta() > 0)
					txtOcorrencia = "Alterada a avaliação. Nota antiga: " + Util.formatNumber(ofertaAvaliacaoAnterior.getVlPeso(), 1) + ". Nota nova: " + Util.formatNumber(ofertaAvaliacao.getVlPeso(), 1)  + ". Data antiga: " + Util.convCalendarString3(ofertaAvaliacaoAnterior.getDtAvaliacao()) + ". Data nova: " + Util.convCalendarString3(ofertaAvaliacao.getDtAvaliacao())  + ". Situação antiga: " + OfertaAvaliacaoServices.situacaoOfertaAvaliacao[ofertaAvaliacaoAnterior.getStOfertaAvaliacao()] + ". Situação nova: " + OfertaAvaliacaoServices.situacaoOfertaAvaliacao[ofertaAvaliacao.getStOfertaAvaliacao()];
				else
					txtOcorrencia = "Adicionada a avaliação. Nota: " + Util.formatNumber(ofertaAvaliacao.getVlPeso(), 1)  + ". Data: " + Util.convCalendarString3(ofertaAvaliacao.getDtAvaliacao())  + ". Situação: " + OfertaAvaliacaoServices.situacaoOfertaAvaliacao[ofertaAvaliacao.getStOfertaAvaliacao()];
				
				OcorrenciaOfertaAvaliacao ocorrencia = new OcorrenciaOfertaAvaliacao(0, 0, txtOcorrencia, Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, ofertaAvaliacao.getCdOfertaAvaliacao(), ofertaAvaliacao.getCdOferta(), ofertaAvaliacaoAnterior.getVlPeso(), ofertaAvaliacao.getVlPeso(), ofertaAvaliacaoAnterior.getDtAvaliacao(), ofertaAvaliacao.getDtAvaliacao(), ofertaAvaliacaoAnterior.getStOfertaAvaliacao(), ofertaAvaliacao.getStOfertaAvaliacao());
				if(OcorrenciaOfertaAvaliacaoServices.save(ocorrencia, null, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de alteração na oferta avaliação");
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "OFERTAAVALIACAO", ofertaAvaliacao);
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
	
	/**
	 * Método para salvar um array de ofertaAvaliacao
	 * @param ofertasAvaliacao
	 * @return
	 */
	public static Result saveAvaliacoes(ArrayList<OfertaAvaliacao> ofertasAvaliacao) {
		return saveAvaliacoes(ofertasAvaliacao, null);
	}
	
	public static Result saveAvaliacoes(ArrayList<OfertaAvaliacao> ofertasAvaliacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(ofertasAvaliacao==null)
				return new Result(-1, "Erro ao salvar. Lista de avaliações é nula");

			int retorno=0;
			
			for (OfertaAvaliacao ofertaAvaliacao : ofertasAvaliacao) {
			
				if(OfertaAvaliacaoDAO.get(ofertaAvaliacao.getCdOfertaAvaliacao(), ofertaAvaliacao.getCdOferta(), connect)==null){
					retorno = OfertaAvaliacaoDAO.insert(ofertaAvaliacao, connect);
					ofertaAvaliacao.setCdOfertaAvaliacao(retorno);
				}
				else {
					retorno = OfertaAvaliacaoDAO.update(ofertaAvaliacao, connect);
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...");
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
	
	public static Result gerarOfertaAvaliacao( Oferta oferta ){
		return gerarOfertaAvaliacao( oferta, null );
	}
	
	public static Result gerarOfertaAvaliacao( Oferta oferta, Connection connection ){
		
		boolean isConnectionNull = connection==null;
		try{
			ResultSetMap disciplinasAvaliacoes = DisciplinaAvaliacaoServices.getAllByDisciplina( oferta.getCdDisciplina(), connection );
			while( disciplinasAvaliacoes.next() ){
				GregorianCalendar dtAvaliacao = new GregorianCalendar();
				OfertaAvaliacao novaOfertaAvaliacao = new OfertaAvaliacao(0, oferta.getCdOferta(), ParametroServices.getValorOfParametroAsInteger("CD_TIPO_AVALIACAO_FINAL", 0), 
						oferta.getCdCurso(), disciplinasAvaliacoes.getInt("cd_unidade"), disciplinasAvaliacoes.getInt("cd_disciplina_avaliacao"),
						oferta.getCdCursoModulo(), oferta.getCdDisciplina(), oferta.getCdMatriz(),
						disciplinasAvaliacoes.getInt("cd_formulario"), dtAvaliacao,
						disciplinasAvaliacoes.getString("nm_avaliacao"), disciplinasAvaliacoes.getString("txt_observacao"),
						disciplinasAvaliacoes.getFloat("vl_peso"), disciplinasAvaliacoes.getString("id_avaliacao"), ST_OFERTA_AVALIACAO_ABERTA, 0/*cdAula*/);
				save( novaOfertaAvaliacao, 0, connection );
			}
			
			return new Result( 1 , "", "", null);
		}catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, e.getMessage());
		}
	}
	
	public static Result remove(int cdOfertaAvaliacao, int cdOferta){
		return remove(cdOfertaAvaliacao, cdOferta, false, null);
	}
	public static Result remove(int cdOfertaAvaliacao, int cdOferta, boolean cascade){
		return remove(cdOfertaAvaliacao, cdOferta, cascade, null);
	}
	public static Result remove(int cdOfertaAvaliacao, int cdOferta, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				
				retorno = connect.prepareStatement("DELETE FROM acd_ocorrencia_oferta_avaliacao WHERE cd_oferta_avaliacao = " + cdOfertaAvaliacao + " AND cd_oferta = " + cdOferta).executeUpdate();
				
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao excluir ocorrencia oferta!");
				}
				
				retorno = connect.prepareStatement("DELETE FROM acd_disciplina_avaliacao_aluno WHERE cd_oferta_avaliacao = " + cdOfertaAvaliacao + " AND cd_oferta = " + cdOferta).executeUpdate();
				
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao excluir disciplina avaliação aluno!");
				}
				
			}
			
			if(!cascade || retorno>=0)
				retorno = OfertaAvaliacaoDAO.delete(cdOfertaAvaliacao, cdOferta, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Existem notas lançadas na atividade, por isso a mesma não pode ser excluída!");
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

	public static Result removeByDisciplinaAvaliacao(int cdDisciplinaAvaliacao) {
		return removeByDisciplinaAvaliacao(cdDisciplinaAvaliacao, null);
	}
	
	public static Result removeByDisciplinaAvaliacao(int cdDisciplinaAvaliacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			ResultSetMap rsm = getAll();
			while (rsm.next()) {
				if (rsm.getInt("cd_disciplina_avaliacao") == cdDisciplinaAvaliacao) {
					retorno = remove(rsm.getInt("cd_oferta_avaliacao"), rsm.getInt("cd_oferta"), true, connect).getCode();
					if(retorno<=0){
						Conexao.rollback(connect);
						return new Result(-2, "Este registro está vinculada a outros registros e não pode ser excluído!");
					}
				}
			}
			
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Registro excluída com sucesso!");
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_avaliacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllByOferta(int cdOferta, int cdUnidade ) {
		return getAllByOferta(cdOferta, cdUnidade, null);
	}

	public static ResultSetMap getAllByOferta(int cdOferta, int cdUnidade, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, PS_DIS.nm_produto_servico AS NM_DISCIPLINA FROM acd_oferta_avaliacao A "+
											// "LEFT JOIN acd_disciplina_avaliacao_aluno B ON (  A.cd_oferta_avaliacao = B.cd_oferta_avaliacao )"+
											 "LEFT JOIN grl_produto_servico PS_DIS ON (  A.cd_disciplina = PS_DIS.cd_produto_servico )"+
											 "WHERE A.cd_oferta = "+cdOferta+
											 (cdUnidade > 0 ? " AND A.cd_unidade = " + cdUnidade : "") +
											 " ORDER BY A.cd_unidade ASC");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()) {
				rsm.setValueToField("DT_NM_AVALIACAO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_avaliacao")));
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoServices.getAllByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoServices.getAllByOferta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllByOfertaUnidade(int cdOferta, int cdUnidade ) {
		return getAllByOfertaUnidade(cdOferta, cdUnidade, 0, null);
	}

	public static ResultSetMap getAllByOfertaUnidade(int cdOferta, int cdUnidade, Connection connect) {
		return getAllByOfertaUnidade(cdOferta, cdUnidade, 0, connect);
	}
	
	public static ResultSetMap getAllByOfertaUnidade(int cdOferta, int cdUnidade, int cdMatriculaDisciplina ) {
		return getAllByOfertaUnidade(cdOferta, cdUnidade, cdMatriculaDisciplina, null);
	}

	public static ResultSetMap getAllByOfertaUnidade(int cdOferta, int cdUnidade, int cdMatriculaDisciplina, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.NM_TIPO_AVALIACAO FROM acd_oferta_avaliacao A "+
											 "  JOIN acd_tipo_avaliacao B ON (  A.cd_tipo_avaliacao = B.cd_tipo_avaliacao)"+
											 "WHERE A.cd_oferta = "+cdOferta+
											 "  AND A.cd_unidade = "+cdUnidade+
											 "  AND A.st_oferta_avaliacao NOT IN ("+ST_OFERTA_AVALIACAO_CANCELADA+")" + 
											 " ORDER BY A.st_oferta_avaliacao, A.dt_avaliacao, A.nm_avaliacao");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				
				rsm.setValueToField("DT_NM_AVALIACAO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_avaliacao")));
				rsm.setValueToField("CL_PESO", Util.formatNumber(rsm.getDouble("vl_peso"), 2));
				rsm.setValueToField("CL_ST_OFERTA_AVALIACAO", situacaoOfertaAvaliacao[rsm.getInt("ST_OFERTA_AVALIACAO")]);
				
				if(rsm.getString("NM_AVALIACAO") == null || rsm.getString("NM_AVALIACAO").trim().equals("")){
					rsm.setValueToField("NM_AVALIACAO", "Sem Título");
				}
				
				if(cdMatriculaDisciplina > 0){
					DisciplinaAvaliacaoAluno disciplinaAvaliacaoAluno = DisciplinaAvaliacaoAlunoDAO.get(cdMatriculaDisciplina, rsm.getInt("cd_oferta_avaliacao"), rsm.getInt("cd_oferta"), connect);
					if(disciplinaAvaliacaoAluno != null){
						rsm.setValueToField("VL_CONCEITO", disciplinaAvaliacaoAluno.getVlConceito());
						rsm.setValueToField("CL_CONCEITO", Util.formatNumber(disciplinaAvaliacaoAluno.getVlConceito(), 1));
					}
					else{
						rsm.setValueToField("VL_CONCEITO", 0);
						rsm.setValueToField("CL_CONCEITO", "0");
					}
				}
			}
			rsm.beforeFirst();
			
			return rsm;
			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoServices.getAllByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoServices.getAllByOferta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	
	public static ResultSetMap getAllByProfessor(int cdProfessor) {
		return getAllByProfessor(cdProfessor, 0, null);
	}
	
	public static ResultSetMap getAllByProfessor(int cdProfessor, Connection connect) {
		return getAllByProfessor(cdProfessor, 0, connect);
	}
	
	public static ResultSetMap getAllByProfessor(int cdProfessor, int cdInstituicao) {
		return getAllByProfessor(cdProfessor, cdInstituicao, null);
	}
	
	
	public static ResultSetMap getAllByProfessor(int cdProfessor, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			int cdPeriodoLetivoSecretaria = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdSecretaria, connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoSecretaria = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			pstmt = connect.prepareStatement("SELECT A.*, B.NM_TIPO_AVALIACAO, DC.nm_produto_servico AS NM_CURSO, D.NM_TURMA, F.nm_produto_servico AS nm_disciplina FROM acd_oferta_avaliacao A "+
											 "  JOIN acd_tipo_avaliacao B ON (  A.cd_tipo_avaliacao = B.cd_tipo_avaliacao)"+
											 "  JOIN acd_oferta C ON (  A.cd_oferta = C.cd_oferta)"+
											 "  JOIN acd_turma D ON (  C.cd_turma = D.cd_turma)"+
											 "  JOIN acd_instituicao_periodo E ON (  D.cd_periodo_letivo = E.cd_periodo_letivo)"+
											 "  JOIN grl_produto_servico DC ON (  D.cd_curso = DC.cd_produto_servico)"+
											 "  JOIN grl_produto_servico F ON (  C.cd_disciplina = F.cd_produto_servico)"+
											 "WHERE C.cd_professor = "+cdProfessor+
											 "  AND E.cd_periodo_letivo_superior = " + cdPeriodoLetivoSecretaria +
											 (cdInstituicao > 0 ? " AND D.cd_instituicao = " + cdInstituicao : "") +
											 " ORDER BY A.st_oferta_avaliacao, A.dt_avaliacao, A.nm_avaliacao");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				
				rsm.setValueToField("DT_NM_AVALIACAO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_avaliacao")));
				rsm.setValueToField("CL_PESO", Util.formatNumber(rsm.getDouble("vl_peso"), 2));
				rsm.setValueToField("CL_ST_OFERTA_AVALIACAO", situacaoOfertaAvaliacao[rsm.getInt("ST_OFERTA_AVALIACAO")]);
				rsm.setValueToField("CL_TURMA", rsm.getString("NM_CURSO") + " - " + rsm.getString("NM_TURMA"));
				
				if(rsm.getString("NM_AVALIACAO") == null || rsm.getString("NM_AVALIACAO").trim().equals("")){
					rsm.setValueToField("NM_AVALIACAO", "Sem Título");
				}
			}
				
			rsm.beforeFirst();
			
			return rsm;
			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoServices.getAllByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoServices.getAllByOferta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByAluno(int cdAluno) {
		return getAllByAluno(cdAluno, null);
	}
	
	
	public static ResultSetMap getAllByAluno(int cdAluno, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			int cdPeriodoLetivoSecretaria = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdSecretaria, connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoSecretaria = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			ResultSetMap rsmFinal = new ResultSetMap();
			
			ResultSetMap rsmMatriculas = MatriculaServices.getAllByAluno(cdAluno, connect);
			while(rsmMatriculas.next()){
				pstmt = connect.prepareStatement("SELECT A.*, B.NM_TIPO_AVALIACAO, DC.nm_produto_servico AS NM_CURSO, D.NM_TURMA, F.nm_produto_servico AS nm_disciplina FROM acd_oferta_avaliacao A "+
										 "  JOIN acd_tipo_avaliacao B ON (  A.cd_tipo_avaliacao = B.cd_tipo_avaliacao)"+
										 "  JOIN acd_oferta C ON (  A.cd_oferta = C.cd_oferta)"+
										 "  JOIN acd_turma D ON (  C.cd_turma = D.cd_turma)"+
										 "  JOIN acd_instituicao_periodo E ON (  D.cd_periodo_letivo = E.cd_periodo_letivo)"+
										 "  JOIN grl_produto_servico DC ON (  D.cd_curso = DC.cd_produto_servico)"+
										 "  JOIN grl_produto_servico F ON (  C.cd_disciplina = F.cd_produto_servico)"+
										 "WHERE D.cd_turma = "+rsmMatriculas.getString("cd_turma")+
										 "  AND E.cd_periodo_letivo_superior = " + cdPeriodoLetivoSecretaria +
										 " ORDER BY A.st_oferta_avaliacao, A.dt_avaliacao, A.nm_avaliacao");
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				while(rsm.next()){
				
					rsm.setValueToField("DT_NM_AVALIACAO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_avaliacao")));
					rsm.setValueToField("CL_PESO", Util.formatNumber(rsm.getDouble("vl_peso"), 2));
					rsm.setValueToField("CL_ST_OFERTA_AVALIACAO", situacaoOfertaAvaliacao[rsm.getInt("ST_OFERTA_AVALIACAO")]);
					rsm.setValueToField("CL_TURMA", rsm.getString("NM_CURSO") + " - " + rsm.getString("NM_TURMA"));
					
					if(rsm.getString("NM_AVALIACAO") == null || rsm.getString("NM_AVALIACAO").trim().equals("")){
						rsm.setValueToField("NM_AVALIACAO", "Sem Título");
					}
				
				}
				rsm.beforeFirst();
				
				rsmFinal.getLines().addAll(rsm.getLines());
			}
			rsmMatriculas.beforeFirst();
			
			rsmFinal.beforeFirst();
			
			
			return rsmFinal;
			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoServices.getAllByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoServices.getAllByOferta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getBoletimByAluno(int cdAluno ) {
		return getBoletimByAluno(cdAluno, null);
	}

	public static Result getBoletimByAluno(int cdAluno, Connection connect ) {
		return getBoletimByAluno(cdAluno, 0, null);
	}
	
	public static Result getBoletimByAluno(int cdAluno, int cdProfessor ) {
		return getBoletimByAluno(cdAluno, cdProfessor, null);
	}
	
	public static Result getBoletimByAluno(int cdAluno, int cdProfessor, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			ResultSetMap rsmMatriculaAtual = MatriculaServices.getMatriculaRegularRecenteByAluno(cdAluno, connect);
			System.out.println(rsmMatriculaAtual);
			Matricula matriculaAtual = null;
			while(rsmMatriculaAtual.next()){
				matriculaAtual = MatriculaDAO.get(rsmMatriculaAtual.getInt("cd_matricula"), connect);
			}
			rsmMatriculaAtual.beforeFirst();
	
			
			Turma turma = TurmaDAO.get(matriculaAtual.getCdTurma(), connect);
			
			ResultSetMap rsmCursoUnidade = CursoUnidadeServices.getAllByCurso(turma.getCdCurso(), connect);
			ResultSetMap rsmBoletim = new ResultSetMap();
		
			HashMap<String, Object> register = new HashMap<String, Object>(); 
				
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			if(cdProfessor > 0)
				criterios.add(new ItemComparator("cdProfessor", String.valueOf(cdProfessor), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.CD_TURMA", String.valueOf(matriculaAtual.getCdTurma()), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmOferta = OfertaServices.find(criterios, connect);
			while(rsmOferta.next()){
			
				register = new HashMap<String, Object>(); 
				
				register.put("NM_DISCIPLINA", rsmOferta.getString("NM_DISCIPLINA"));
				register.put("CD_DISCIPLINA", rsmOferta.getString("CD_DISCIPLINA"));
				register.put("CD_OFERTA", rsmOferta.getString("CD_OFERTA"));
				
				float vlTotalNota = 0;
				float vlMediaNota = 0;
				
				while(rsmCursoUnidade.next()){
					
					String columnUnidade = "NOTA_UNIDADE_" + rsmCursoUnidade.getString("nm_unidade");
					rsmCursoUnidade.setValueToField("COLUMN_UNIDADE", columnUnidade);
					
					float vlNotaDisciplinaUnidade = 0;
					
					ResultSetMap rsmDisciplinaAvaliacaoAluno = DisciplinaAvaliacaoAlunoServices.getAllByMatriculaUnidadeDisciplina(matriculaAtual.getCdMatricula(), rsmCursoUnidade.getInt("cd_unidade"), rsmOferta.getInt("cd_disciplina"), connect);
					//ResultSetMap rsmDisciplinaAvaliacaoAluno = MatriculaDisciplinaServices.getAllByMatriculaSimples(matriculaAtual.getCdMatricula(), connect);
					
					while(rsmDisciplinaAvaliacaoAluno.next()){
						vlNotaDisciplinaUnidade += rsmDisciplinaAvaliacaoAluno.getFloat("vl_conceito");
						vlTotalNota += rsmDisciplinaAvaliacaoAluno.getFloat("vl_conceito");
								
					}
					rsmDisciplinaAvaliacaoAluno.beforeFirst();
					
					register.put(columnUnidade, Util.formatNumber(vlNotaDisciplinaUnidade, 1));
					
				}
				rsmCursoUnidade.beforeFirst();
				
				vlMediaNota = vlTotalNota / rsmCursoUnidade.size();
				
				register.put("TOTAL_NOTAS", Util.formatNumber(vlTotalNota, 1));
				register.put("MEDIA_NOTAS", Util.formatNumber(vlMediaNota, 1));
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.cd_matricula", "" + matriculaAtual.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("A.lg_presenca", "0", ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("B.cd_disciplina", rsmOferta.getString("CD_DISCIPLINA"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("B.st_aula", "" + AulaServices.ST_FECHADA, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmFaltas = AulaMatriculaServices.find(criterios, connect);
				
				register.put("NR_FALTAS", rsmFaltas.size());
				
				rsmBoletim.addRegister(register);
				
			}
			rsmOferta.beforeFirst();
				
			
			if(rsmOferta.size() == 0) {
				while(rsmCursoUnidade.next()){
					String columnUnidade = "NOTA_UNIDADE_" + rsmCursoUnidade.getString("nm_unidade");
					rsmCursoUnidade.setValueToField("COLUMN_UNIDADE", columnUnidade);
					float vlNotaDisciplinaUnidade = 0;
					register.put(columnUnidade, Util.formatNumber(vlNotaDisciplinaUnidade, 1));
				}
				rsmCursoUnidade.beforeFirst();
			}
			
			rsmBoletim.beforeFirst();
			System.out.println(rsmBoletim);
			Result result = new Result(1, "Sucesso ao buscar boletim");
			result.addObject("RSM_UNIDADES", rsmCursoUnidade);
			result.addObject("RSM_BOLETIM", rsmBoletim);
			return result;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoServices.getAllByOferta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllOcorrenciasByOfertaAvaliacao(int cdOferta, int cdOfertaAvaliacao ) {
		return getAllOcorrenciasByOfertaAvaliacao(cdOferta, cdOfertaAvaliacao, null);
	}

	public static ResultSetMap getAllOcorrenciasByOfertaAvaliacao(int cdOferta, int cdOfertaAvaliacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.* FROM grl_ocorrencia A "+
											 "  JOIN acd_ocorrencia_oferta_avaliacao B ON (  A.cd_ocorrencia = B.cd_ocorrencia )"+
											 " WHERE B.cd_oferta = "+cdOferta+
											 "   AND B.cd_oferta_avaliacao = " + cdOfertaAvaliacao);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("DT_NM_OCORRENCIA", Util.convCalendarString3(rsm.getGregorianCalendar("dt_ocorrencia")));
				Usuario usuario = UsuarioDAO.get(rsm.getInt("cd_usuario"), connect);
				Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);
				rsm.setValueToField("NM_USUARIO", pessoa.getNmPessoa());
			}
			rsm.beforeFirst();
			
			pstmt = connect.prepareStatement("SELECT A.* FROM grl_ocorrencia A "+
								 "  JOIN acd_ocorrencia_disciplina_avaliacao_aluno B ON (  A.cd_ocorrencia = B.cd_ocorrencia )"+
								 " WHERE B.cd_oferta = "+cdOferta+
								 "   AND B.cd_oferta_avaliacao = " + cdOfertaAvaliacao);
			ResultSetMap rsmOcorrenciaDisciplinaAvaliacaoAluno = new ResultSetMap(pstmt.executeQuery());
			while(rsmOcorrenciaDisciplinaAvaliacaoAluno.next()){
				rsmOcorrenciaDisciplinaAvaliacaoAluno.setValueToField("DT_NM_OCORRENCIA", Util.convCalendarString3(rsmOcorrenciaDisciplinaAvaliacaoAluno.getGregorianCalendar("dt_ocorrencia")));
				Usuario usuario = UsuarioDAO.get(rsmOcorrenciaDisciplinaAvaliacaoAluno.getInt("cd_usuario"), connect);
				Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);
				rsmOcorrenciaDisciplinaAvaliacaoAluno.setValueToField("NM_USUARIO", pessoa.getNmPessoa());
			}
			rsmOcorrenciaDisciplinaAvaliacaoAluno.beforeFirst();

			rsm.getLines().addAll(rsmOcorrenciaDisciplinaAvaliacaoAluno.getLines());
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("dt_ocorrencia DESC");
			rsm.orderBy(fields);
			
			return rsm;
			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoServices.getAllByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoServices.getAllByOferta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método para buscar todos os conceitos de ofertaAvaliacao dos alunos
	 * @param cdTurma
	 * @param cdDisciplina
	 * @param cdOferta
	 * @return rsmNotas
	 */
	public static ResultSetMap getConceitos(int cdTurma, int cdDisciplina, int cdOferta) {
		return getConceitos(cdTurma, cdDisciplina, cdOferta, null);
	}

	public static ResultSetMap getConceitos(int cdTurma, int cdDisciplina, int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {	
			
			// Busca dos alunos
			ResultSetMap rsmAlunos = TurmaServices.getAlunosByTurmaDisciplina(cdTurma, cdDisciplina, connect);
			System.out.println(rsmAlunos);
			
			while(rsmAlunos.next()){
				rsmAlunos.setValueToField("CD_OFERTA", cdOferta);
				pstmt = connect.prepareStatement("SELECT A.* FROM acd_disciplina_avaliacao_aluno A" +
												" JOIN acd_oferta_avaliacao B ON (A.cd_oferta_avaliacao = B.cd_oferta_avaliacao)" +
												" WHERE A.cd_matricula_disciplina = " + rsmAlunos.getInt("CD_MATRICULA_DISCIPLINA") +
												" AND B.cd_oferta = " + cdOferta);
				
				ResultSetMap rsmAvaliacaoAluno = new ResultSetMap(pstmt.executeQuery());
				System.out.println(rsmAvaliacaoAluno);
				while(rsmAvaliacaoAluno.next()) {
					rsmAlunos.setValueToField("CD_OFERTA_AVALIACAO", rsmAvaliacaoAluno.getDouble("CD_OFERTA_AVALIACAO"));
					
					String nmAvalicao = new String("VL_CONCEITO_" + rsmAvaliacaoAluno.getInt("CD_OFERTA_AVALIACAO"));
					rsmAlunos.setValueToField(nmAvalicao, rsmAvaliacaoAluno.getDouble("VL_CONCEITO"));
				}
			}
			
			rsmAlunos.beforeFirst();
			return rsmAlunos;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoServices.getConceitos: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoServices.getConceitos: " + e);
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
		return Search.find("SELECT * FROM acd_oferta_avaliacao A, acd_oferta B WHERE A.cd_oferta = B.cd_oferta", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static Result aplicar(int cdOfertaAvaliacao, int cdOferta) {
		return aplicar(cdOfertaAvaliacao, cdOferta, null);
	}

	public static Result aplicar(int cdOfertaAvaliacao, int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			OfertaAvaliacao ofertaAvaliacao = OfertaAvaliacaoDAO.get(cdOfertaAvaliacao, cdOferta, connect);
			
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_oferta", "" + cdOferta, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_oferta_avaliacao", "" + cdOfertaAvaliacao, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("tp_questao", "" + OfertaAvaliacaoQuestaoServices.TP_OBJETIVA + ", " + OfertaAvaliacaoQuestaoServices.TP_OBJETIVA_DESCRITIVA, ItemComparator.IN, Types.INTEGER));
			ResultSetMap rsmQuestoes = OfertaAvaliacaoQuestaoDAO.find(criterios, connect);
			while(rsmQuestoes.next()){
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_oferta", "" + cdOferta, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_oferta_avaliacao", "" + cdOfertaAvaliacao, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_oferta_avaliacao_questao", "" + rsmQuestoes.getInt("cd_oferta_avaliacao_questao"), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmAlternativas = OfertaAvaliacaoQuestaoAlternativaDAO.find(criterios, connect);
				if(rsmAlternativas.size() == 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "A questão " + rsmQuestoes.getString("TXT_QUESTAO") + " não possui alternativas cadastradas");
				}
				else{
					boolean possuiRespostaCorreta = false;
					while(rsmAlternativas.next()){
						possuiRespostaCorreta = rsmAlternativas.getInt("LG_CORRETA")==1;
						if(possuiRespostaCorreta){
							break;
						}
					}
					
					if(!possuiRespostaCorreta){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "A questão " + rsmQuestoes.getString("TXT_QUESTAO") + " não possui nenhuma alternativa correta");
					}
				}
				
				
			}
			rsmQuestoes.beforeFirst();
			
			float vlTotalQuestoes = 0;
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_oferta", "" + cdOferta, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_oferta_avaliacao", "" + cdOfertaAvaliacao, ItemComparator.EQUAL, Types.INTEGER));
			rsmQuestoes = OfertaAvaliacaoQuestaoDAO.find(criterios, connect);
			while(rsmQuestoes.next()){
				vlTotalQuestoes+=rsmQuestoes.getFloat("VL_PESO");
			}
			rsmQuestoes.beforeFirst();
			
			if(rsmQuestoes.size() > 0 && ofertaAvaliacao.getVlPeso()!= vlTotalQuestoes){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "O total de pontos das questões não corresponde à pontuação da atividade");
			}
			
			ofertaAvaliacao.setStOfertaAvaliacao(ST_OFERTA_AVALIACAO_APLICADA);
			if(OfertaAvaliacaoDAO.update(ofertaAvaliacao, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao aplicar atividade");
			}
			
			return new Result(1, "Atividade aplicada com sucesso");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.aplicar: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result fecharAvaliacao(int cdOfertaAvaliacao, int cdOferta) {
		return fecharAvaliacao(cdOfertaAvaliacao, cdOferta, null);
	}

	public static Result fecharAvaliacao(int cdOfertaAvaliacao, int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		try {
			
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			OfertaAvaliacao ofertaAvaliacao = OfertaAvaliacaoDAO.get(cdOfertaAvaliacao, cdOferta, connect);
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_oferta", "" + cdOferta, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_oferta_avaliacao", "" + cdOfertaAvaliacao, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmNotasAlunos = DisciplinaAvaliacaoAlunoDAO.find(criterios, connect);
			Oferta oferta = OfertaDAO.get(cdOferta, connect);
			ResultSetMap rsmAlunos = TurmaServices.getAlunosAtivosOf(oferta.getCdTurma());
			if(rsmNotasAlunos.size() < rsmAlunos.size()){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Alguns alunos não receberam nota");
			}
			
			
			ofertaAvaliacao.setStOfertaAvaliacao(ST_OFERTA_AVALIACAO_AVALIADA);
			if(OfertaAvaliacaoDAO.update(ofertaAvaliacao, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao fechar avaliação");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Avaliação fechada com sucesso");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.fecharAvaliacao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result abrirAvaliacao(int cdOfertaAvaliacao, int cdOferta) {
		return abrirAvaliacao(cdOfertaAvaliacao, cdOferta, null);
	}

	public static Result abrirAvaliacao(int cdOfertaAvaliacao, int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		try {
			
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			OfertaAvaliacao ofertaAvaliacao = OfertaAvaliacaoDAO.get(cdOfertaAvaliacao, cdOferta, connect);
			ofertaAvaliacao.setStOfertaAvaliacao(ST_OFERTA_AVALIACAO_APLICADA);
			if(OfertaAvaliacaoDAO.update(ofertaAvaliacao, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao abrir avaliação");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Avaliação aberta com sucesso");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.abrirAvaliacao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result cancelar(int cdOfertaAvaliacao, int cdOferta) {
		return cancelar(cdOfertaAvaliacao, cdOferta, null);
	}

	public static Result cancelar(int cdOfertaAvaliacao, int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		
		try {
			
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			OfertaAvaliacao ofertaAvaliacao = OfertaAvaliacaoDAO.get(cdOfertaAvaliacao, cdOferta, connect);
			ofertaAvaliacao.setStOfertaAvaliacao(ST_OFERTA_AVALIACAO_CANCELADA);
			if(OfertaAvaliacaoDAO.update(ofertaAvaliacao, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao fechar avaliação");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Avaliação fechada com sucesso");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.cancelar: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getImpressaoAtividade(int cdOfertaAvaliacao, int cdOferta) {
		return getImpressaoAtividade(cdOfertaAvaliacao, cdOferta, null);
	}

	public static ResultSetMap getImpressaoAtividade(int cdOfertaAvaliacao, int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {	
			
			Oferta oferta = OfertaDAO.get(cdOferta, connect);
			OfertaAvaliacao ofertaAvaliacao = OfertaAvaliacaoDAO.get(cdOfertaAvaliacao, cdOferta, connect);
			Turma turma = TurmaDAO.get(oferta.getCdTurma(), connect);
			
			// Busca dos alunos
			ResultSetMap rsmAlunos = TurmaServices.getAlunosAtivosOf(turma.getCdTurma(), connect);
			
			while(rsmAlunos.next()){
				
				ResultSetMap rsmQuestoes = OfertaAvaliacaoQuestaoServices.getAllByOfertaAvaliacao(cdOferta, cdOfertaAvaliacao, connect);
				while(rsmQuestoes.next()){
					
					String txtAlternativas = "";
					ResultSetMap rsmAlternativas = OfertaAvaliacaoQuestaoAlternativaServices.getAllByQuestao(rsmQuestoes.getInt("cd_oferta_avaliacao_questao"), cdOfertaAvaliacao, cdOferta, connect);
					char letraAlternativa = 'a';
					while(rsmAlternativas.next()){
						txtAlternativas += "&nbsp;&nbsp;&nbsp;&nbsp;" + (letraAlternativa++)+") " + rsmAlternativas.getString("txt_alternativa") + "<br /><br />";
					}
					rsmAlternativas.beforeFirst();
					
					rsmQuestoes.setValueToField("TXT_ALTERNATIVAS", txtAlternativas);
				}
				rsmQuestoes.beforeFirst();
				
				rsmAlunos.setValueToField("RSM_QUESTOES", rsmQuestoes.getLines());
			}
			
			rsmAlunos.beforeFirst();
			return rsmAlunos;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoServices.getImpressaoAtividade: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}