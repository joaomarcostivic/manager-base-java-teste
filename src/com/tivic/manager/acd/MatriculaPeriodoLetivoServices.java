package com.tivic.manager.acd;

import java.sql.*;
import java.util.*;

import com.tivic.manager.adm.*;
import com.tivic.manager.alm.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.*;
import com.tivic.manager.ptc.*;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.util.Result;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.Search;

public class MatriculaPeriodoLetivoServices {

	/* situação da rematrícula */
	public static final int ST_ATIVO     = 0;
	public static final int ST_CONCLUIDO = 1;
	public static final int ST_TRANCADO  = 2;

	public static final int ERR_FAT_VALOR_ADESAO_INVALIDO   = -1000;
	public static final int ERR_FAT_VALOR_CONTRATO_INVALIDO = -1001;
	public static final int ERR_FAT_VALOR_VENDA 			= -1002;
	public static final int ERR_FAT_CLASSIF_VENDA 			= -1003;
	public static final int ERR_FAT_VALOR_TOTAL 			= -1004;
	public static final int ERR_NOT_TIPO_DOC_RESCISAO 		= -1005;

	public static final String[] stMatricula = {"Ativa","Concluída","Cancelada"};

	/* codigos de erros retornardos por rotinas da classe */
	public static final int ERR_MATR_CANCELADA = -2;
	
	public static Result save(MatriculaPeriodoLetivo matriculaPeriodoLetivo){
		return save(matriculaPeriodoLetivo, null);
	}

	public static Result save(MatriculaPeriodoLetivo matriculaPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(matriculaPeriodoLetivo==null)
				return new Result(-1, "Erro ao salvar. MatriculaPeriodoLetivo é nulo");

			int retorno;
			if(get(matriculaPeriodoLetivo) == null){
				retorno = MatriculaPeriodoLetivoDAO.insert(matriculaPeriodoLetivo, connect);
				matriculaPeriodoLetivo.setCdMatricula(retorno);
			}
			else {
				retorno = MatriculaPeriodoLetivoDAO.update(matriculaPeriodoLetivo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MATRICULAPERIODOLETIVO", matriculaPeriodoLetivo);
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
	
	public static Result remove(int cdMatricula, int cdPeriodoLetivo){
		return remove(cdMatricula, cdPeriodoLetivo, false, null);
	}
	
	public static Result remove(int cdMatricula, int cdPeriodoLetivo, boolean cascade){
		return remove(cdMatricula, cdPeriodoLetivo, cascade, null);
	}
	
	public static Result remove(int cdMatricula, int cdPeriodoLetivo, boolean cascade, Connection connect){
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
			if(!cascade || retorno>=0)
				retorno = MatriculaPeriodoLetivoDAO.delete(cdMatricula, cdPeriodoLetivo, connect);
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

	public static Result removeByMatricula(int cdMatricula) {
		return removeByMatricula(cdMatricula, null);
	}
	
	public static Result removeByMatricula(int cdMatricula, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			ResultSetMap rsm = getAllByMatricula(cdMatricula, connect);
			while (rsm.next()) {
				if (rsm.getInt("cd_matricula") == cdMatricula) {
					retorno = remove(cdMatricula, rsm.getInt("cd_periodo_letivo"), true, connect).getCode();
					if(retorno<=0){
						Conexao.rollback(connect);
						return new Result(-2, "Esta matrícula está vinculada a outros registros e não pode ser excluída!");
					}
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Matrícula excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir matrícula!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static MatriculaPeriodoLetivo get(MatriculaPeriodoLetivo matriculaPeriodoLetivo) {
		return get(matriculaPeriodoLetivo, null);
	}
	
	public static MatriculaPeriodoLetivo get(MatriculaPeriodoLetivo matriculaPeriodoLetivo, Connection connect) {
		return MatriculaPeriodoLetivoDAO.get(matriculaPeriodoLetivo.getCdMatricula(), matriculaPeriodoLetivo.getCdPeriodoLetivo(), connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_periodo_letivo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaPeriodoLetivoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaPeriodoLetivoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByMatricula(int cdMatricula) {
		return getAllByMatricula(cdMatricula, null);
	}

	public static ResultSetMap getAllByMatricula(int cdMatricula, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_periodo_letivo WHERE cd_matricula = " + cdMatricula);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaPeriodoLetivoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaPeriodoLetivoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_matricula_periodo_letivo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getAllDisciplinas(int cdMatricula, int cdPeriodoLetivo) {
		return getAllDisciplinas(cdMatricula, cdPeriodoLetivo, null);
	}

	public static ResultSetMap getAllDisciplinas(int cdMatricula, int cdMatriculaPeridoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, C.nm_produto_servico AS nm_disciplina " +
					"FROM acd_matricula_disciplina A, acd_disciplina B, grl_produto_servico C " +
					"WHERE A.cd_disciplina = B.cd_disciplina " +
					"  AND B.cd_disciplina = C.cd_produto_servico " +
					"  AND A.cd_matricula = ? " +
					"  AND A.cd_matricula_periodo_letivo = ?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdMatriculaPeridoLetivo);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
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

	public static ResultSetMap getAllDisciplinasNaoMatriculadas(int cdMatricula, int cdInstituicao, int cdCurso,
			int cdMatriz) {
		return getAllDisciplinasNaoMatriculadas(cdMatricula, cdInstituicao, cdCurso, cdMatriz, null);
	}

	public static ResultSetMap getAllDisciplinasNaoMatriculadas(int cdMatricula, int cdInstituicao, int cdCurso,
			int cdMatriz, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, C.nm_produto_servico AS nm_disciplina, " +
					"C.cd_produto_servico AS cd_disciplina " +
					"FROM acd_curso_disciplina A, grl_produto_servico_composicao B, grl_produto_servico C " +
					"WHERE A.cd_curso_disciplina = B.cd_composicao " +
					"  AND B.cd_produto_servico_componente = C.cd_produto_servico " +
					"  AND A.cd_instituicao = ? " +
					"  AND A.cd_curso = ? " +
					"  AND A.cd_matriz = ? " +
					"  AND NOT C.cd_produto_servico IN (SELECT D.cd_disciplina " +
					"									FROM acd_matricula_disciplina D " +
					"									WHERE D.cd_matricula = ?)");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdCurso);
			pstmt.setInt(3, cdMatriz);
			pstmt.setInt(4, cdMatricula);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
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

	@Deprecated
	public static ResultSetMap getAllPeriodos(int cdMatricula, int cdPeriodoLetivo) {
		return getAllPeriodos(cdMatricula, cdPeriodoLetivo, null);
	}
	@Deprecated
	public static ResultSetMap getAllPeriodos(int cdMatricula, int cdPeriodoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, D.nm_produto_servico AS nm_periodo " +
					"FROM acd_matricula_periodo A, acd_curso_periodo B, grl_produto_servico_composicao C, grl_produto_servico D " +
					"WHERE A.cd_periodo = B.cd_periodo " +
					"  AND B.cd_periodo = C.cd_composicao " +
					"  AND C.cd_produto_servico_componente = D.cd_produto_servico " +
					"  AND A.cd_matricula = ? " +
					"  AND A.cd_periodo_letivo = ?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdPeriodoLetivo);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
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
	
	public static ResultSetMap getAllPeriodosLetivo(int cdMatricula, int cdPeriodoLetivo) {
		return getAllPeriodosLetivo(cdMatricula, cdPeriodoLetivo, null);
	}

	public static ResultSetMap getAllPeriodosLetivo(int cdMatricula, int cdPeriodoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM acd_matricula_periodo " +
					"WHERE cd_matricula = ? " +
					"  AND cd_periodo_letivo = ?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdPeriodoLetivo);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
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

	public static ResultSetMap getAllPeriodosNaoMatriculados(int cdMatricula, int cdPeriodoLetivo, int cdInstituicao, int cdCurso) {
		return getAllPeriodosNaoMatriculados(cdMatricula, cdPeriodoLetivo, cdInstituicao, cdCurso, null);
	}

	public static ResultSetMap getAllPeriodosNaoMatriculados(int cdMatricula, int cdPeriodoLetivo, int cdInstituicao, int cdCurso,
			Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, D.cd_produto_servico AS cd_produto_servico_periodo, " +
					"D.nm_produto_servico AS nm_periodo " +
					"FROM acd_curso_periodo A, grl_produto_servico_composicao B, " +
					"	  grl_produto_servico_empresa C, grl_produto_servico D " +
					"WHERE A.cd_periodo = B.cd_composicao " +
					"  AND B.cd_empresa = C.cd_empresa " +
					"  AND B.cd_produto_servico_componente = C.cd_produto_servico " +
					"  AND C.cd_produto_servico = D.cd_produto_servico " +
					"  AND B.cd_empresa = ? " +
					"  AND B.cd_produto_servico = ? " +
					"  AND NOT A.cd_periodo IN (SELECT E.cd_periodo " +
					"							FROM acd_matricula_periodo E " +
					"							WHERE E.cd_matricula = ? " +
					"							  AND E.cd_periodo_letivo = ?)");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdCurso);
			pstmt.setInt(3, cdMatricula);
			pstmt.setInt(4, cdPeriodoLetivo);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("nr_ordem");
			rsm.orderBy(fields);
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

	public static int delete(int cdMatricula, int cdPeriodoLetivo) {
		return delete(cdMatricula, cdPeriodoLetivo, null);
	}

	public static int delete(int cdMatricula, int cdPeriodoLetivo, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("DELETE " +
					"FROM acd_matricula_periodo " +
					"WHERE cd_matricula = ? " +
					"  AND cd_periodo_letivo = ?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdPeriodoLetivo);
			pstmt.execute();

			pstmt = connection.prepareStatement("DELETE " +
					"FROM acd_matricula_disciplina " +
					"WHERE cd_matricula = ? " +
					"  AND cd_periodo_letivo = ?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdPeriodoLetivo);
			pstmt.execute();

			if (MatriculaPeriodoLetivoDAO.delete(cdMatricula, cdPeriodoLetivo, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaPeriodoLetivoServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result solicitarRescisao(int cdMatricula, int cdPeriodoLetivo,
			int cdUsuario, GregorianCalendar dtSolicitacao, int cdMotivoTrancamento, String txtObservacao) {
		return solicitarRescisao(cdMatricula, cdPeriodoLetivo, cdUsuario, dtSolicitacao, cdMotivoTrancamento,
				txtObservacao, null);
	}

	public static Result solicitarRescisao(int cdMatricula, int cdPeriodoLetivo,
			int cdUsuario, GregorianCalendar dtSolicitacao, int cdMotivoTrancamento, String txtObservacao,
			Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			/* verifica se a matricula já está cancelada; em caso afirmativo, aborta rotina */
			MatriculaPeriodoLetivo matriculaPeriodo = MatriculaPeriodoLetivoDAO.get(cdMatricula, cdPeriodoLetivo, connection);
			Contrato contrato = ContratoDAO.get(matriculaPeriodo.getCdContrato(), connection);

			if (matriculaPeriodo.getStMatricula() == ST_TRANCADO || contrato.getStContrato()!=ContratoServices.ST_ATIVO) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(ERR_MATR_CANCELADA, "Certifique-se de que a matrícula e/ou contrato estejam ativos.");
			}
			Matricula matricula = MatriculaDAO.get(cdMatricula, connection);

			int cdTipoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOC_SOLIC_RESCISAO", 0, 0, connection);
			if (cdTipoDocumento <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(ERR_NOT_TIPO_DOC_RESCISAO, "Tipo de documento referente à solicitação de rescisão de matrículas não configurado. " +
						"Entre em contato com o administrador ou o suporte técnico.");
			}

			Documento documento = new Documento(0 /*cdDocumento*/, 0 /*cdArquivo*/, 0 /*cdSetor*/, cdUsuario, "" /*nmLocalOrigem*/, dtSolicitacao /*dtProtocolo*/,
												DocumentoServices.TP_PUBLICO /*tpDocumento*/, txtObservacao, "" /*idDocumento*/, "" /*nrDocumento*/,
												cdTipoDocumento, 0 /*cdServico*/, 0 /*cdAtendimento*/, "" /*txtDocumento*/, 0/*cdSetorAtual*/,
												0/*cdSituacaoDocumento*/, 0/*cdFase*/, contrato.getCdEmpresa(), 0, 0 /*tpPrioridade*/, 0/*cdDocumentoSuperior*/, 
												null /*dsAssunto*/, null /*nrAtendimento*/, 0/*lgNotificacao*/, 0 /*cdTipoDocumentoAnterior*/, null/*nrDocumentoExterno*/,
												null/*nrAssunto*/,null,null, 0, 1);
			ArrayList<DocumentoPessoa> solicitantes = new ArrayList<DocumentoPessoa>();
			solicitantes.add(new DocumentoPessoa(0 /*cdDocumento*/,
					matricula.getCdAluno() /*cdPessoa*/, "Aluno"));
			if (DocumentoServices.insert(documento, solicitantes, connection, null) == null || documento.getCdDocumento()<=0){
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao protocolar solicitação de rescisão. " +
							"Entre em contato com o administrador ou o suporte técnico.");
			}

			ContratoRescisao rescisao = ContratoRescisaoDAO.get(matriculaPeriodo.getCdContrato(), connection);
			boolean insertedRescisao = rescisao!=null;
			rescisao = rescisao!=null ? rescisao : new ContratoRescisao(matriculaPeriodo.getCdContrato(),
					documento.getCdDocumento(),
					null /*dtRescisao*/,
					ContratoRescisaoServices.TP_FORM_PESSOAL,
					0 /*cdNegociacao*/);
			rescisao.setCdDocumento(documento.getCdDocumento());
			if ((insertedRescisao ? ContratoRescisaoDAO.update(rescisao, connection) : ContratoRescisaoDAO.insert(rescisao, connection)) <= 0){
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao protocolar solicitação de rescisão. " +
									  "Entre em contato com o administrador ou o suporte técnico.");
			}

			matriculaPeriodo.setCdMotivoTrancamento(cdMotivoTrancamento);
			if (MatriculaPeriodoLetivoDAO.update(matriculaPeriodo, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao atualizar matrícula. Entre em contato com o administrador ou o suporte técnico");
			}

			if (isConnectionNull)
				connection.commit();

			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("documento", documento);
			return new Result(1, "Solicitação de rescisão protolocada com sucesso.", "hash", hash);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erros reportados ao protocolar solicitação de rescisão. Anote a mensagem de erro e entre em " +
					              "contato com o administrador ou o suporte técnico:\n "+e.getMessage() , e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result gerarParcelas(int cdMatricula, int cdPeriodoLetivo, int cdTipoDocumento, int cdConta, int cdContaCarteira)	{
		return gerarParcelas(cdMatricula, cdPeriodoLetivo, cdTipoDocumento, cdConta, cdContaCarteira, null);
	}

	public static Result gerarParcelas(int cdMatricula, int cdPeriodoLetivo, int cdTipoDocumento, int cdConta, int cdContaCarteira, Connection connection)	{
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			MatriculaPeriodoLetivo matriculaPeriodo = MatriculaPeriodoLetivoDAO.get(cdMatricula, cdPeriodoLetivo, connection);
			Matricula matricula = MatriculaDAO.get(cdMatricula, connection);
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connection);
			int cdCategoriaAdesao = turma.getCdCategoriaMatricula();
			int cdCategoriaParcelas = turma.getCdCategoriaMensalidade();
			int cdContrato = matriculaPeriodo.getCdContrato();

			Result result = ContratoServices.gerarParcelasOfContrato(cdContrato, cdTipoDocumento, cdConta, cdContaCarteira, cdCategoriaAdesao,
					                                                 cdCategoriaParcelas, connection);
			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}

			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar gerar parcelas!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result lancarFaturamento(int cdMatricula, int cdPeriodoLetivo, GregorianCalendar dtFaturamento,
			int cdPlanoPagamento, int cdFormaPagamento, int cdConta, int cdUsuario, float vlAdesao, float vlContrato, float vlVenda,
			HashMap<String, Object> dadosConfigFat)	{
		return lancarFaturamento(cdMatricula, cdPeriodoLetivo, dtFaturamento, cdPlanoPagamento, cdFormaPagamento, cdConta,
				cdUsuario, vlAdesao, vlContrato, vlVenda, dadosConfigFat, null);
	}

	public static Result lancarFaturamento(int cdMatricula, int cdPeriodoLetivo, GregorianCalendar dtFaturamento,
			int cdPlanoPagamento, int cdFormaPagamento, int cdConta, int cdUsuario, float vlAdesao, float vlContrato, float vlVenda,
			HashMap<String, Object> dadosConfigFat, Connection connection)
	{
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			HashMap<String, Object> valoresFaturarMax = getValoresToFaturar(cdMatricula, cdPeriodoLetivo, connection);
			float vlAdesaoMax   = (Float)valoresFaturarMax.get("vlAdesaoFat");
			float vlContratoMax = (Float)valoresFaturarMax.get("vlContratoFat");
			float vlVendaMax    = (Float)valoresFaturarMax.get("vlDocSaidaFat");

			MatriculaPeriodoLetivo matriculaPeriodo  = MatriculaPeriodoLetivoDAO.get(cdMatricula, cdPeriodoLetivo, connection);
			Matricula matricula                      = MatriculaDAO.get(cdMatricula, connection);
			Turma turma                              = TurmaDAO.get(matricula.getCdTurma(), connection);
			MatriculaPeriodoLetivo matrPeriodoLetivo = MatriculaPeriodoLetivoDAO.get(cdMatricula, cdPeriodoLetivo, connection);
			InstituicaoPeriodo periodo               = matriculaPeriodo.getCdPeriodoLetivo()<=0 ? null : InstituicaoPeriodoDAO.get(matrPeriodoLetivo.getCdPeriodoLetivo());
			ProdutoServico curso                     = ProdutoServicoDAO.get(turma.getCdCurso(), connection);
			//
			String nmPeriodoLetivo  = periodo==null ? "" : periodo.getNmPeriodoLetivo();
			int cdCategoriaParcelas = turma.getCdCategoriaMensalidade()>0 ? turma.getCdCategoriaMensalidade() :
				                                                            ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_MENSALIDADE", 0, 0, connection);
			int cdCategoriaTaxas    = turma.getCdCategoriaMatricula()>0 ? turma.getCdCategoriaMatricula() :
																		  ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_TAXA_MATRICULA", 0, 0, connection);
			int cdContrato          = matriculaPeriodo.getCdContrato();
			int cdEmpresa           = turma.getCdInstituicao();
			Contrato contrato       = cdContrato<=0 ? null : ContratoDAO.get(cdContrato, connection);
			int cdSacado            = contrato==null ? 0 : contrato.getCdPessoa();

			ArrayList<HashMap<String, Object>> valoresFaturar = new ArrayList<HashMap<String,Object>>();
			if (vlAdesao>0) {
				if (vlAdesao > vlAdesaoMax) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(ERR_FAT_VALOR_ADESAO_INVALIDO, "Valor da matrícula inválido!");
				}

				HashMap<String, Object> valorFaturar = new HashMap<String, Object>();
				valorFaturar.put("cdForeignKey", cdContrato);
				valorFaturar.put("tpForeignKey", PlanoPagamentoServices.OF_CONTRATO);
				valorFaturar.put("vlFaturar", vlAdesao);
				valorFaturar.put("tpContaReceber", ContaReceberServices.TP_TAXA_ADESAO);
				valorFaturar.put("dsHistorico", "Taxa Matrícula " + matricula.getNrMatricula() + " - " +
						                        curso.getNmProdutoServico() + (nmPeriodoLetivo!=null ? "/" + nmPeriodoLetivo : ""));
				valorFaturar.put("prefixDoc", matricula.getNrMatricula());
				ArrayList<ContaReceberCategoria> categorias = new ArrayList<ContaReceberCategoria>();
				categorias.add(new ContaReceberCategoria(0 /*cdContaReceber*/, cdCategoriaTaxas /*cdCategoriaEconomica*/, vlAdesao /*vlContaCategoria*/, 0/*Centro de Custo*/));
				valorFaturar.put("categorias", categorias);
				valoresFaturar.add(valorFaturar);
			}
			if (vlContrato>0) {
				if (vlContrato > vlContratoMax) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(ERR_FAT_VALOR_CONTRATO_INVALIDO, "Valor do contrato inválido! [vlContrato:"+vlContrato+",vlContratoMax: "+vlContratoMax+"]");
				}
				HashMap<String, Object> valorFaturar = new HashMap<String, Object>();
				valorFaturar.put("cdForeignKey", cdContrato);
				valorFaturar.put("tpForeignKey", PlanoPagamentoServices.OF_CONTRATO);
				valorFaturar.put("vlFaturar", vlContrato);
				valorFaturar.put("tpContaReceber", ContaReceberServices.TP_PARCELA);
				valorFaturar.put("dsHistorico", "Matrícula " + matricula.getNrMatricula() + " - " +
								                curso.getNmProdutoServico() + (nmPeriodoLetivo!=null ? "/" + nmPeriodoLetivo : ""));
				valorFaturar.put("prefixDoc", matricula.getNrMatricula());
				ArrayList<ContaReceberCategoria> categorias = new ArrayList<ContaReceberCategoria>();
				categorias.add(new ContaReceberCategoria(0 /*cdContaReceber*/, cdCategoriaParcelas /*cdCategoriaEconomica*/, vlContrato /*vlContaCategoria*/, 0/*Centro de Custo*/));
				valorFaturar.put("categorias", categorias);
				valoresFaturar.add(valorFaturar);
			}
			//
			if (vlVenda>0) {
				if (vlVenda > vlVendaMax) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(ERR_FAT_VALOR_VENDA, "Valor de venda inválido! [vlVenda: "+vlVenda+", vlVendaMax: "+vlVendaMax+"]");
				}
				PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_documento_saida, B.nr_documento_saida " +
						                                              "FROM adm_venda_saida_item A, alm_documento_saida B " +
						                                              "WHERE A.cd_documento_saida = B.cd_documento_saida" +
																	  "  AND A.cd_pedido_venda = ?");
				//pstmt.setInt(1, matrPeriodoLetivo.getCdPedidoVenda());
				ResultSet rs = pstmt.executeQuery();
				int cdDocumentoSaida = !rs.next() ? 0 : rs.getInt("cd_documento_saida");
				String dsHistorico = "";
				ResultSetMap rsmItens = cdDocumentoSaida<=0 ? null : DocumentoSaidaServices.getAllItens(cdDocumentoSaida, connection);
				for (int i=0; rsmItens!=null && rsmItens.next(); i++)
					dsHistorico += (i>0 ? ", " : "") + Util.formatNumber(rsmItens.getFloat("qt_saida")) + " " +
							(rsmItens.getString("sg_unidade_medida")!=null ? rsmItens.getString("sg_unidade_medida") + " " : "") +
							rsmItens.getString("nm_produto_servico");
				dsHistorico = dsHistorico.length()>100 ? dsHistorico.substring(0, 100) : dsHistorico;
				DocumentoSaida docSaida = cdDocumentoSaida<=0 ? null : DocumentoSaidaDAO.get(cdDocumentoSaida, connection);
				if (cdDocumentoSaida>0) {
					float vlDocumentoSaida = docSaida.getVlTotalDocumento();
					HashMap<String, Object> valorFaturar = new HashMap<String, Object>();
					valorFaturar.put("cdDocumentoSaida", cdDocumentoSaida);
					valorFaturar.put("cdContrato", cdContrato);
					valorFaturar.put("tpForeignKey", PlanoPagamentoServices.OF_CONTRATO_DOC_SAIDA);
					valorFaturar.put("vlFaturar", vlVenda);
					valorFaturar.put("tpContaReceber", ContaReceberServices.TP_PARCELA);
					valorFaturar.put("prefixDoc", docSaida.getNrDocumentoSaida());
					valorFaturar.put("dsHistorico", dsHistorico);
					ArrayList<ContaReceberCategoria> categorias = DocumentoSaidaServices.getClassificacaoEmCategorias(docSaida, connection);
					if (categorias==null) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(ERR_FAT_CLASSIF_VENDA, "Erro ao classificar o faturamento!");
					}
					for (int i=0; i<categorias.size(); i++) {
						System.out.println(categorias.get(i));
						categorias.get(i).setVlContaCategoria(categorias.get(i).getVlContaCategoria()/vlDocumentoSaida * vlVenda);
					}
					valorFaturar.put("categorias", categorias);
					valoresFaturar.add(valorFaturar);
				}
			}

			Result result = PlanoPagamentoServices.lancarFaturamento(cdEmpresa, valoresFaturar, dtFaturamento,
					   cdConta, cdSacado, cdFormaPagamento, cdPlanoPagamento, cdUsuario, dadosConfigFat /*dadosConfigFat*/,  false /*simulateFat*/, connection);
			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}

			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar lançar faturamento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result getValorItensContratados(int cdCurso, int cdTurma, ArrayList<HashMap<String, Object>> itens) {
		return getValorItensContratados(cdCurso, cdTurma, itens, null);
	}

	public static Result getValorItensContratados(int cdCurso, int cdTurma, ArrayList<HashMap<String, Object>> itens, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			float vlTotal = 0;
			Turma turma = TurmaDAO.get(cdTurma, connection);
			int cdTabelaPreco = turma.getCdTabelaPreco();
			for (int i=0; itens!=null && i<itens.size(); i++) {
				HashMap<String, Object> item = itens.get(i);
				int tpItem = (Integer)item.get("tpItem");

				if (tpItem == 0) {
					int cdPeriodo = (Integer)item.get("cdPeriodo");
					CursoModulo periodo = CursoModuloDAO.get(cdPeriodo, connection);
					PreparedStatement pstmt = connection.prepareStatement("SELECT vl_preco " +
							"FROM adm_produto_servico_preco " +
							"WHERE cd_tabela_preco = ? " +
							"  AND cd_produto_servico = ? " +
							"  AND dt_termino_validade IS NULL");
					pstmt.setInt(1, cdTabelaPreco);
					pstmt.setInt(2, periodo.getCdCursoModulo());
					ResultSet rs = pstmt.executeQuery();
					vlTotal += !rs.next() ? 0 : rs.getFloat("vl_preco");
				}
				else {
					int cdDisciplina = (Integer)item.get("cdDisciplina");
					CursoDisciplina periodo = CursoDisciplinaDAO.get(0/*cdMatriz*/,0/*cdCurso*/,0/*cdCursoModulo*/,cdDisciplina, connection);
					PreparedStatement pstmt = connection.prepareStatement("SELECT vl_preco " +
							"FROM adm_produto_servico_preco " +
							"WHERE cd_tabela_preco = ? " +
							"  AND cd_produto_servico = ? " +
							"  AND dt_termino_validade IS NULL");
					pstmt.setInt(1, cdTabelaPreco);
					pstmt.setInt(2, periodo.getCdDisciplina());
					ResultSet rs = pstmt.executeQuery();
					vlTotal += !rs.next() ? 0 : rs.getFloat("vl_preco");
				}
			}

			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("vlTotal", vlTotal);

			return new Result(1, "", "results", hash);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erros reportados ao totalizar itens contratados.", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result lancarFaturamento(int cdMatricula, int cdPeriodoLetivo,
			GregorianCalendar dtFaturamento, int cdPlanoPagamento, int cdFormaPagamento, int cdConta, int cdUsuario, float vlTotal,
			HashMap<String, Object> dadosConfigFat)	{
		return lancarFaturamento(cdMatricula, cdPeriodoLetivo, dtFaturamento, cdPlanoPagamento,
				cdFormaPagamento, cdConta, cdUsuario, vlTotal, dadosConfigFat, null);
	}

	public static Result lancarFaturamento(int cdMatricula, int cdPeriodoLetivo,
			GregorianCalendar dtFaturamento, int cdPlanoPagamento, int cdFormaPagamento, int cdConta, int cdUsuario, float vlTotal,
			HashMap<String, Object> dadosConfigFat, Connection connection)	{
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			HashMap<String, Object> valoresFaturarMax = getValoresToFaturar(cdMatricula, cdPeriodoLetivo, connection);
			float vlAdesaoMax = (Float)valoresFaturarMax.get("vlAdesaoFat");
			float vlContratoMax = (Float)valoresFaturarMax.get("vlContratoFat");
			float vlVendaMax = (Float)valoresFaturarMax.get("vlDocSaidaFat");
			float vlTotalMax = vlAdesaoMax + vlContratoMax + vlVendaMax;
			System.out.println(vlTotal + " - " + vlAdesaoMax + " - " + vlContratoMax + " - " + vlVendaMax);

			if (vlTotal<=0 || vlTotal>vlTotalMax) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(ERR_FAT_VALOR_TOTAL, "Erro no valor do faturamento! [vlTotal:"+vlTotal+",vlTotalMax:"+vlTotalMax+"]");
			}

			float vlAdesao   = vlAdesaoMax/vlTotalMax * vlTotal;
			float vlContrato = vlContratoMax/vlTotalMax * vlTotal;
			float vlVenda    = vlVendaMax/vlTotalMax * vlTotal;


			Result results = lancarFaturamento(cdMatricula, cdPeriodoLetivo, dtFaturamento, cdPlanoPagamento,
					                           cdFormaPagamento, cdConta, cdUsuario, vlAdesao, vlContrato, vlVenda, dadosConfigFat, connection);
			if (results.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return results;
			}

			if (isConnectionNull)
				connection.commit();

			return results;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao lançar faturamento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static HashMap<String, Object> getValoresToFaturar(int cdMatricula, int cdPeriodoLetivo) {
		return getValoresToFaturar(cdMatricula, cdPeriodoLetivo, null);
	}

	public static HashMap<String, Object> getValoresToFaturar(int cdMatricula, int cdPeriodoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			HashMap<String, Object> hash = new HashMap<String, Object>();
			MatriculaPeriodoLetivo matriculaPeriodo = MatriculaPeriodoLetivoDAO.get(cdMatricula, cdPeriodoLetivo, connection);
			Contrato contrato = matriculaPeriodo==null || matriculaPeriodo.getCdContrato()<=0 ? null : ContratoDAO.get(matriculaPeriodo.getCdContrato(), connection);
			// float vlContrato = contrato==null ? 0 : contrato.getVlContrato();
			//float vlAdesao = contrato==null ? 0 : contrato.getVlAdesao();
			// vlContrato-= contrato.getVlDesconto();

			/* verifica a existencia de descontos */
			ResultSetMap rsmDescontos = contrato==null ? null : ContratoServices.getAllDescontos(contrato.getCdContrato(), connection);
			// float prDesconto = 0;
			while (rsmDescontos!=null && rsmDescontos.next()) {
				// prDesconto += rsmDescontos.getFloat("pr_desconto");
			}
			//float vlContratoFinal = prDesconto==0 ? vlContrato : vlContrato - vlContrato * prDesconto/((float)100);
			//float vlAdesaoFinal = vlAdesao;

			/* localiza a classificacao de receitas provenientes da referida matricula (parcelas, mensalidades e taxas) */
			//Matricula matricula = MatriculaDAO.get(cdMatricula, connection);
			//Turma turma = matricula==null || matricula.getCdTurma()<=0 ? null : TurmaDAO.get(matricula.getCdTurma(), connection);
			//int cdCategMensalidade = turma==null ? 0 : turma.getCdCategoriaMensalidade();
			//int cdCategTaxaMatricula = turma==null ? 0 : turma.getCdCategoriaMatricula();

			/* consulta faturamento de taxas de matriculas *
			PreparedStatement pstmt = contrato==null ? null : connection.prepareStatement("SELECT SUM(vl_conta_categoria) AS vl_total " +
																						  "FROM adm_conta_receber A, adm_conta_receber_categoria B " +
																						  "WHERE A.cd_conta_receber       = B.cd_conta_receber " +
																						  "  AND A.cd_contrato            = " +contrato.getCdContrato()+
																						  "  AND cd_conta_origem         IS NULL " +
																						  "  AND cd_documento_saida      IS NULL " +
																						  "  AND B.cd_categoria_economica = "+cdCategTaxaMatricula);
			//ResultSet rs = pstmt==null ? null : pstmt.executeQuery();
			//float vlAdesaoFaturado = rs==null || !rs.next() ? 0 : rs.getFloat("vl_total"); */

			/* consulta faturamento de mensalidades *
			pstmt = contrato==null ? null : connection.prepareStatement("SELECT SUM(vl_conta_categoria) AS vl_total " +
					"FROM adm_conta_receber A, adm_conta_receber_categoria B " +
					"WHERE A.cd_conta_receber = B.cd_conta_receber " +
					"  AND A.cd_contrato = ? " +
					"  AND cd_conta_origem IS NULL " +
					"  AND cd_documento_saida IS NULL " +
					"  AND B.cd_categoria_economica = ?");
			if (pstmt!=null) {
				pstmt.setInt(1, contrato.getCdContrato());
				pstmt.setInt(2, cdCategMensalidade);
			}
			*/
			//rs = pstmt==null ? null : pstmt.executeQuery();
			//float vlContratoFaturado = rs==null || !rs.next() ? 0 : rs.getFloat("vl_total");
			/*
			pstmt = matriculaPeriodo==null || matriculaPeriodo.getCdPedidoVenda()<=0 ? null :
				connection.prepareStatement("SELECT DISTINCT A.cd_documento_saida " +
						"FROM alm_documento_saida_item A, adm_venda_saida_item B " +
						"WHERE A.cd_documento_saida = B.cd_documento_saida " +
						"  AND A.cd_produto_servico = B.cd_produto_servico " +
						"  AND A.cd_empresa = B.cd_empresa " +
						"  AND A.cd_item = B.cd_item " +
						"  AND B.cd_pedido_venda = ?");
			if (pstmt!=null)
				pstmt.setInt(1, matriculaPeriodo.getCdPedidoVenda());
			rs = pstmt==null ? null : pstmt.executeQuery();
			int cdDocumentoSaida = rs==null || !rs.next() ? 0 : rs.getInt("cd_documento_saida");
			pstmt = cdDocumentoSaida<=0 ? null : connection.prepareStatement("SELECT " +
					"SUM(qt_saida * vl_unitario + vl_acrescimo - vl_desconto) AS vl_total " +
					"FROM alm_documento_saida_item " +
					"WHERE cd_documento_saida = ?");
			if (pstmt!=null)
				pstmt.setInt(1, cdDocumentoSaida);
			rs = pstmt==null ? null : pstmt.executeQuery();
			float vlDocSaida = rs==null || !rs.next() ? 0 : rs.getFloat("vl_total");

			/* consulta faturamento de venda avulso *
			pstmt = cdDocumentoSaida<=0 ? null : connection.prepareStatement("SELECT SUM(vl_conta + vl_acrescimo - vl_abatimento) AS vl_total " +
					"FROM adm_conta_receber " +
					"WHERE cd_documento_saida = ? " +
					"  AND cd_conta_origem IS NULL ");
			if (cdDocumentoSaida>0)
				pstmt.setInt(1, cdDocumentoSaida);
			rs = pstmt==null ? null : pstmt.executeQuery();
			float vlDocSaidaFaturado = rs==null || !rs.next() ? 0 : rs.getFloat("vl_total");

			hash.put("vlContrato", vlContrato);
			hash.put("vlAdesao", vlAdesao);
			hash.put("vlContratoFinal", vlContratoFinal);
			hash.put("vlAdesaoFinal", vlAdesaoFinal);
			hash.put("vlAdesaoFaturado", vlAdesaoFaturado);
			hash.put("vlContratoFaturado", vlContratoFaturado);
			hash.put("vlDocSaida", vlDocSaida);
			hash.put("vlDocSaidaFaturado", vlDocSaidaFaturado);
			hash.put("vlContratoFat", vlContratoFinal - vlContratoFaturado > 0 ? vlContratoFinal - vlContratoFaturado : 0);
			hash.put("vlAdesaoFat", vlAdesao - vlAdesaoFaturado > 0 ? vlAdesao - vlAdesaoFaturado : 0);
			hash.put("vlDocSaidaFat", vlDocSaida - vlDocSaidaFaturado > 0 ? vlDocSaida - vlDocSaidaFaturado : 0);
			*/
			return hash;
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