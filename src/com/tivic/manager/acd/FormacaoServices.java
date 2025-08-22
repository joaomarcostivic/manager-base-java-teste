package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class FormacaoServices {

        /* niveis de formacao academica */
	public static final int NIV_ENSINO_FUNDAMENTAL = 0;
	public static final int NIV_ENSINO_MEDIO = 1;
	public static final int NIV_ENSINO_PROFISSIONAL = 2;
	public static final int NIV_APERFEICOAMENTO = 3;
	public static final int NIV_GRADUACAO = 4;
	public static final int NIV_ESPECIALIZACAO = 5;
	public static final int NIV_MESTRADO = 6;
	public static final int NIV_DOUTORADO = 7;
	public static final int NIV_POS_DOUTORADO = 8;
	public static final int NIV_LIVRE_DOCENCIA = 9;
	public static final int NIV_FORMACAO_COMPLEMENTAR = 10;
	
	
	public static final String ID_FORMACAO_ESPECIFICO_CRECHE = "01";
	public static final String ID_FORMACAO_ESPECIFICO_PRE_ESCOLA = "02";
	public static final String ID_FORMACAO_ESPECIFICO_ANOS_INICIAIS = "03";
	public static final String ID_FORMACAO_ESPECIFICO_ANOS_FINAIS = "04";
	public static final String ID_FORMACAO_ESPECIFICO_ENSINO_MEDIO = "05";
	public static final String ID_FORMACAO_ESPECIFICO_EJA = "06";
	public static final String ID_FORMACAO_ESPECIFICO_EDUCACAO_ESPECIAL = "07";
	public static final String ID_FORMACAO_ESPECIFICO_EDUCACAO_INDIGENA = "08";
	public static final String ID_FORMACAO_ESPECIFICO_EDUCACAO_CAMPO = "09";
	public static final String ID_FORMACAO_ESPECIFICO_EDUCACAO_AMBIENTAL = "10";
	public static final String ID_FORMACAO_ESPECIFICO_EDUCACAO_DIREITOS_HUMANOS = "11";
	public static final String ID_FORMACAO_GENERO_DIVERSIDADE_SEXUAL = "12";
	public static final String ID_FORMACAO_DIREITOS_CRIANCA_ADOLESCENTE = "13";
	public static final String ID_FORMACAO_RELACOES_ETNICORRACIONAIS = "14";
	public static final String ID_FORMACAO_OUTROS = "15";
	public static final String ID_FORMACAO_GESTAO_ESCOLAR = "16";
	
	public static final String[] niveis = {"Ensino Fundamental",
		"Ensino Médio", 
		"Ensino Profissional de Nível Técnico", 
		"Aperfeiçoamento", 
		"Graduação",
		"Especialização",
		"Mestrado",
		"Doutorado",
		"Pós-doutorado",
		"Livre-docência",
		"Formação complementar"};
	
	public static final int ST_EM_ANDAMENTO = 0;
	public static final int ST_CONCLUIDO = 1;
	public static final int ST_INCOMPLETO = 2;
	
	public static final String[] situacao = {"Em andamento", 
		"Concluído",
		"Incompleto"};
	
	public static final int DOUT_NORMAL = 0;
	public static final int DOUT_SANDUICHE = 1;
	public static final int DOUT_COTUTELA = 2;
	
	public static final String[] tiposDoutorado = {"Normal", 
		"Sanduíche",
		"Co-tutela"};
	
	public static final int FORM_CURSO_CURTA_DURACAO = 0;
	public static final int FORM_EXTENSAO_UNIVERSITARIA = 1;
	public static final int FORM_MBA = 2;
	public static final int FORM_OUTROS = 3;
	
	public static final String[] tiposFormComp = {"Curso de curta duração", 
		"Extensão universitária",
		"Mba",
		"Outros"};
	
	public static final int MEST_ACADEMICO = 0;
	public static final int MEST_PROFISSIONALIZANTE = 1;
	
	public static final String[] tiposMestrado = {"Acadêmico", 
		"Profissionalizante"};
	
	public static final int ESPEC_NORMAL = 0;
	public static final int ESPEC_RESIDENCIA_MEDICA = 1;
	
	public static final String[] tiposEspecializacao = {"Normal", 
		"Residência Médica"};

	public static Result save(Formacao formacao){
		return save(formacao, null);
	}

	public static Result save(Formacao formacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(formacao==null)
				return new Result(-1, "Erro ao salvar. Formacao é nulo");

			FormacaoCurso formacaoCurso = FormacaoCursoDAO.get(formacao.getCdFormacaoCurso(), connect);
			if(formacaoCurso.getCdFormacaoAreaConhecimento() > 0){
				if(formacao.getCdInstituicaoSuperior() == 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Necessário lançar a instituição superior do curso");
				}
				
				if((formacao.getStFormacao() == ST_EM_ANDAMENTO || formacao.getStFormacao() == ST_INCOMPLETO) && (formacao.getNrAnoInicio() == null || formacao.getNrAnoInicio().equals(""))){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Nos cursos Em Andamento, é necessário informar o ano de início do curso");
				}
				
				if(formacao.getStFormacao() == ST_CONCLUIDO && (formacao.getNrAnoTermino() == null || formacao.getNrAnoTermino().equals(""))){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Nos cursos Concluidos, é necessário informar o ano de término do curso");
				}
			}
			
			ArrayList<ItemComparator> criterios =new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_pessoa", "" + formacao.getCdPessoa(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_formacao_curso", "" + formacao.getCdFormacaoCurso(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_formacao", "" + formacao.getCdFormacao(), ItemComparator.DIFFERENT, Types.INTEGER));
			ResultSetMap rsmFormacao = FormacaoDAO.find(criterios, connect);
			if(rsmFormacao.next()){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Professor já possui um registro com essa formação cadastrada");
			}
			
			int retorno;
			if(FormacaoDAO.get(formacao.getCdPessoa(), formacao.getCdFormacao())==null){
				retorno = FormacaoDAO.insert(formacao, connect);
				formacao.setCdPessoa(retorno);
			}
			else {
				retorno = FormacaoDAO.update(formacao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FORMACAO", formacao);
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
	public static Result remove(int cdPessoa, int cdFormacao){
		return remove(cdPessoa, cdFormacao, false, null);
	}
	public static Result remove(int cdPessoa, int cdFormacao, boolean cascade){
		return remove(cdPessoa, cdFormacao, cascade, null);
	}
	public static Result remove(int cdPessoa, int cdFormacao, boolean cascade, Connection connect){
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
			retorno = FormacaoDAO.delete(cdPessoa, cdFormacao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_formacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoServices.getAll: " + e);
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
		return Search.findAndLog("SELECT * FROM acd_formacao A", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

    @Deprecated
	public static int insert(Formacao formacao, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			int cdFormacao = FormacaoDAO.insert(formacao, connection);
			if (cdFormacao <= 0) {
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
			System.err.println("Erro! FormacaoServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	@Deprecated
	public static int update(Formacao formacao, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			if (FormacaoDAO.update(formacao, connection) <= 0) {
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
			System.err.println("Erro! FormacaoServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getFormacaoByPessoa(int cdPessoa, boolean lgPrincipais) {
		return getFormacaoByPessoa(cdPessoa, lgPrincipais, null);
	}

	public static ResultSetMap getFormacaoByPessoa(int cdPessoa, boolean lgPrincipais, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT B.*, A.*, A.nm_instituicao AS nm_instituicao_superior FROM acd_formacao A, acd_formacao_curso B WHERE A.cd_formacao_curso = B.cd_formacao_curso AND A.cd_pessoa = ? AND B.cd_formacao_area_conhecimento " + (lgPrincipais ? "  IS NOT NULL " : "  IS NULL " ));
			pstmt.setInt(1, cdPessoa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("CL_ST_FORMACAO", situacao[rsm.getInt("st_formacao")]);
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaFormacaoServices.getFormacaoByPessoa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaFormacaoServices.getFormacaoByPessoa: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


}
