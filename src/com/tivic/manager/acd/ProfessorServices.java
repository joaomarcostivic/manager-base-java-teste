package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.Escolaridade;
import com.tivic.manager.grl.EscolaridadeDAO;
import com.tivic.manager.grl.EscolaridadeServices;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.LogradouroServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEmpresa;
import com.tivic.manager.grl.PessoaEmpresaServices;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.PessoaFichaMedica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaFisicaServices;
import com.tivic.manager.grl.PessoaNecessidadeEspecialServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.PessoaTipoDocumentacao;
import com.tivic.manager.grl.PessoaTipoDocumentacaoServices;
import com.tivic.manager.grl.ProdutoServicoServices;
import com.tivic.manager.grl.Setor;
import com.tivic.manager.grl.SetorDAO;
import com.tivic.manager.log.Sistema;
import com.tivic.manager.log.SistemaDAO;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioEmpresa;
import com.tivic.manager.seg.UsuarioEmpresaDAO;
import com.tivic.manager.seg.UsuarioModulo;
import com.tivic.manager.seg.UsuarioModuloDAO;
import com.tivic.manager.seg.UsuarioModuloEmpresa;
import com.tivic.manager.seg.UsuarioModuloEmpresaDAO;
import com.tivic.manager.seg.UsuarioPermissaoAcao;
import com.tivic.manager.seg.UsuarioPermissaoAcaoDAO;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.srh.DadosFuncionais;
import com.tivic.manager.srh.DadosFuncionaisDAO;
import com.tivic.manager.srh.DadosFuncionaisServices;
import com.tivic.manager.srh.Funcao;
import com.tivic.manager.srh.FuncaoDAO;
import com.tivic.manager.srh.FuncaoServices;
import com.tivic.manager.srh.Lotacao;
import com.tivic.manager.srh.LotacaoDAO;
import com.tivic.manager.srh.LotacaoServices;
import com.tivic.manager.srh.TipoAdmissao;
import com.tivic.manager.srh.TipoAdmissaoDAO;
import com.tivic.manager.srh.TipoAdmissaoServices;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.Validator;
import com.tivic.manager.util.ValidatorResult;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.RestData;
import sol.util.Result;

public class ProfessorServices {

	public static final String TP_ESCOLARIDADE_FUND_INCOMPLETO 					= "1";
	public static final String TP_ESCOLARIDADE_FUND_COMPLETO 					= "2";
	public static final String TP_ESCOLARIDADE_ENS_MEDIO_MAGISTERIO 			= "72";
	public static final String TP_ESCOLARIDADE_ENS_MEDIO_MAGISTERIO_INDIGENA 	= "74";
	public static final String TP_ESCOLARIDADE_ENS_MEDIO 						= "71";
	public static final String TP_ESCOLARIDADE_ENS_SUPERIOR 					= "6";
	public static final String TP_ESCOLARIDADE_ENS_MEDIO_TECNICO				= "73";
	
	public static final String TP_FUNCAO_DOCENTE 					= "01";
	public static final String TP_FUNCAO_AUXILIAR 					= "02";
	public static final String TP_FUNCAO_MONITOR 					= "03";
	public static final String TP_FUNCAO_TRADUTOR 					= "04";
	
	public static final String TP_ADMISSAO_CONCURSADO 					= "01";
	public static final String TP_ADMISSAO_TEMPORARIO 					= "02";
	public static final String TP_ADMISSAO_TERCERIZADO 					= "03";
	public static final String TP_ADMISSAO_CLT    					    = "04";

	//Grupos de Validação
	public static final int GRUPO_VALIDACAO_INSERT = 0;
	public static final int GRUPO_VALIDACAO_UPDATE = 1;
	public static final int GRUPO_VALIDACAO_EDUCACENSO = 2;
	//Validações
	public static final int VALIDATE_NOME = 0;
	public static final int VALIDATE_SEXO = 1;
	public static final int VALIDATE_DATA_NASCIMENTO = 2;
	public static final int VALIDATE_DATA_NASCIMENTO_MUITO_ANTIGA = 3;
	public static final int VALIDATE_NACIONALIDADE = 4;
	public static final int VALIDATE_NATURALIDADE = 5;
	public static final int VALIDATE_RACA = 6;
	public static final int VALIDATE_NOME_MAE = 7;
	public static final int VALIDATE_NOME_PAI_INVALIDO = 8;
	public static final int VALIDATE_NOME_PAI_NAO_DECLARADO = 9;
	public static final int VALIDATE_NOME_PAI_IGUAL_MAE = 10;
	public static final int VALIDATE_CPF_VALIDO = 11;
	public static final int VALIDATE_CPF_JA_CADASTRADO = 12;
	public static final int VALIDATE_RG_QUANT_MINIMA = 13;
	public static final int VALIDATE_RG_JA_CADASTRADO = 14;
	
	public static final int VALIDATE_ENDERECO = 15;
	public static final int VALIDATE_ENDERECO_CEP = 16;
	public static final int VALIDATE_ENDERECO_LOGRADOURO = 17;
	public static final int VALIDATE_ENDERECO_NUMERO = 18;
	public static final int VALIDATE_ENDERECO_BAIRRO = 19;
	public static final int VALIDATE_ENDERECO_CIDADE = 20;
	public static final int VALIDATE_ENDERECO_ZONA = 21;
	
	public static final int VALIDATE_NOME_PROFESSOR_ESPACOS = 22;
	public static final int VALIDATE_NOME_PROFESSOR_ABREVIADO = 23;
	public static final int VALIDATE_NOME_PROFESSOR_CARACTERES_ESPECIAIS = 24;
	public static final int VALIDATE_NOME_PROFESSOR_SEM_SOBRENOME = 25;
	public static final int VALIDATE_NOME_MAE_ESPACOS = 26;
	public static final int VALIDATE_NOME_MAE_ABREVIADO = 27;
	public static final int VALIDATE_NOME_MAE_CARACTERES_ESPECIAIS = 28;
	public static final int VALIDATE_NOME_MAE_SEM_SOBRENOME = 29;
	public static final int VALIDATE_NOME_PAI_ESPACOS = 30;
	public static final int VALIDATE_NOME_PAI_ABREVIADO = 31;
	public static final int VALIDATE_NOME_PAI_CARACTERES_ESPECIAIS = 32;
	public static final int VALIDATE_NOME_PAI_SEM_SOBRENOME = 33;
	public static final int VALIDATE_RG_INFORMACOES = 34;
	public static final int VALIDATE_DATA_NASCIMENTO_POSTERIOR_ATUAL = 35;
	public static final int VALIDATE_RG_DATA_POSTERIOR = 36;
	public static final int VALIDATE_RG_DATA_IGUAL_NASCIMENTO = 37;
	public static final int VALIDATE_PAIS_DE_ORIGEM_NACIONALIDADE = 38;
	public static final int VALIDATE_NACIONALIDADE_BRASILEIRA = 39;
	public static final int VALIDATE_EMAIL_INVALIDO = 40;
	public static final int VALIDATE_CADASTRO_DUPLICADO = 41;
	public static final int VALIDATE_RG_DATA_ANTERIOR = 42;
	public static final int VALIDATE_RG_CONTEM_APENAS_ZEROS = 43;
	public static final int VALIDATE_ESCOLARIDADE = 44;
	public static final int VALIDATE_OFERTAS = 45;
	public static final int VALIDATE_DISCIPLINA_NAO_CADASTRADA = 46;
	public static final int VALIDATE_DISCIPLINA_NAO_PERMITIDA = 47;
	public static final int VALIDATE_ENSINO_SUPERIOR_NAO_CADASTRADO = 48;
	public static final int VALIDATE_CURSO_SUPERIOR_INSTITUICAO = 49;
	public static final int VALIDATE_CURSO_SUPERIOR_ANO_INICIO = 50;
	public static final int VALIDATE_CURSO_SUPERIOR_ANO_TERMINO = 51;
	public static final int VALIDATE_FORMACAO_DUPLICADA = 52;
	public static final int VALIDATE_ENSINO_SUPERIOR_CADASTRADO_INVALIDO = 53;
	public static final int VALIDATE_DADOS_FUNCIONAIS_TIPO_ADMISSAO = 54;
	
	public static final int VALIDATE_HORARIOS_CONFLITANTES = 55;
	public static final int VALIDATE_FUNCAO_ATENDIMENTO = 56;
	public static final int VALIDATE_SUPERIOR_SEM_FORMACAO_CONCLUIDA = 57;
	
	public static final int VALIDATE_ENDERECO_CEP_CIDADE = 58;
	
	public static Result save(Professor professor){
		return save(professor, null/*endereco*/, 0/*cdEmpresa*/, 0/*cdVinculo*/, null/*formacao*/, 0/*cdAreaConhecimento*/, null/*connect*/);		
	}
	public static Result save(Professor professor, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, Formacao formacao, int cdAreaConhecimento){
		return save(professor, endereco, cdEmpresa, cdVinculo, formacao, cdAreaConhecimento, null);		
	}

	public static Result save(Professor professor, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, Formacao formacao, int cdAreaConhecimento, int cdUsuario){
		return save(professor, endereco, cdEmpresa, cdVinculo, formacao, cdAreaConhecimento, cdUsuario, null);		
	}
	
	public static Result save(Professor professor, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, Formacao formacao, int cdAreaConhecimento, Connection connect){
		return save(professor, endereco, cdEmpresa, cdVinculo, formacao, cdAreaConhecimento, 0, connect);
	}
	
	/**
	 * Insere ou atualiza um professor
	 * @param professor
	 * @param endereco
	 * @param cdEmpresa
	 * @param cdVinculo
	 * @param formacao
	 * @param cdAreaConhecimento
	 * @param cdUsuario
	 * @param connect
	 * @return
	 */
	public static Result save(Professor professor, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, Formacao formacao, int cdAreaConhecimento, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(professor==null)
				return new Result(-1, "Erro ao salvar. Professor é nulo");
			
			int retorno;
			ValidatorResult resultadoValidacao;
			
			//Correção de horário prevendo que horários de verão possam modificar a exibição
			if(professor.getDtNascimento() != null){
				GregorianCalendar dtNascimento = professor.getDtNascimento();
				
				GregorianCalendar dtNascimentoNovo = new GregorianCalendar();
				dtNascimentoNovo.set(Calendar.DAY_OF_MONTH, dtNascimento.get(Calendar.DAY_OF_MONTH));
				dtNascimentoNovo.set(Calendar.MONTH, dtNascimento.get(Calendar.MONTH));
				dtNascimentoNovo.set(Calendar.YEAR, dtNascimento.get(Calendar.YEAR));
				dtNascimentoNovo.set(Calendar.HOUR_OF_DAY, 12);
				dtNascimentoNovo.set(Calendar.MINUTE, 0);
				dtNascimentoNovo.set(Calendar.SECOND, 0);
				dtNascimentoNovo.set(Calendar.MILLISECOND, 0);
				
				professor.setDtNascimento(dtNascimentoNovo);
				
			}
			
			if(ProfessorDAO.get(professor.getCdProfessor(), connect)==null){
				resultadoValidacao = validate(professor, endereco, 0, 0, GRUPO_VALIDACAO_INSERT, connect);
				if(!resultadoValidacao.hasPassed()){
					return resultadoValidacao;
				}
				
				retorno = ProfessorDAO.insert(professor, connect);
				professor.setCdProfessor(retorno);
			}
			else {
				resultadoValidacao = validate(professor, endereco, 0, 0, GRUPO_VALIDACAO_UPDATE, connect);
				if(!resultadoValidacao.hasPassed()){
					return resultadoValidacao;
				}
				
				retorno = ProfessorDAO.update(professor, connect);
			}

			if (retorno > 0) {
				
				/*
				 * FORMAÇÃO ACADÊMICA
				 */
				if (formacao!=null && cdAreaConhecimento != 0) {
					formacao.setCdPessoa(professor.getCdPessoa()!=0?professor.getCdPessoa():0);
					Result r = FormacaoServices.save(formacao, connect);
					if (r.getCode()>0) {
						formacao.setCdFormacao(r.getCode());
						
					}
					else {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-3, "Erro ao tentar salvar dados da formação!");
					}
				}
				
				/*
				 * VÍNCULO
				 */
				if(cdVinculo<=0) {
					cdVinculo = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_PROFESSOR", 0, 0, connect);
				}				
				if (cdEmpresa > 0 && cdVinculo > 0) {
					Result r = PessoaEmpresaServices.save(new PessoaEmpresa(cdEmpresa, professor.getCdProfessor(), cdVinculo, new GregorianCalendar(), PessoaEmpresaServices.ST_ATIVO), connect);
					if (r.getCode()<=0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-4, "Erro ao tentar salvar dados do vínculo!");
					}
				}
				
				/*
				 * ENDEREÇO
				 */
				if (endereco != null) {
					if (endereco.getCdEndereco() <= 0) {
						endereco.setCdPessoa(professor.getCdProfessor()!=0?professor.getCdProfessor():0);
						endereco.setLgCobranca(1);
						endereco.setLgPrincipal(1);
					}
					if (PessoaEnderecoServices.save(endereco, connect).getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-5, "Erro ao tentar salvar endereço!");
					}
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PROFESSOR", professor);
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
	
	public static Result saveFuncao(int cdProfessor, int cdFuncao, int cdInstituicao){
		return saveFuncao(cdProfessor, cdFuncao, cdInstituicao, null);
	}
	
	/**
	 * Salva uma função para o professor
	 * @param cdProfessor
	 * @param cdFuncao
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static Result saveFuncao(int cdProfessor, int cdFuncao, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			DadosFuncionais dadosFuncionais = null;
			ArrayList<ItemComparator> criterios = new ArrayList<>();
			criterios.add(new ItemComparator("cd_pessoa", "" + cdProfessor, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmDadosFuncionais = DadosFuncionaisDAO.find(criterios, connect);
			if(rsmDadosFuncionais.next()){
				dadosFuncionais = DadosFuncionaisDAO.get(rsmDadosFuncionais.getInt("cd_matricula"), connect);
			}
			
			Setor setor = null;
			criterios = new ArrayList<>();
			criterios.add(new ItemComparator("nm_setor", "CORPO DOCENTE", ItemComparator.EQUAL, Types.VARCHAR));
			criterios.add(new ItemComparator("cd_empresa", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmSetor = SetorDAO.find(criterios, connect);
			if(rsmSetor.next()){
				setor = SetorDAO.get(rsmSetor.getInt("cd_setor"), connect);
			}
			
			Lotacao lotacao = null;
			criterios = new ArrayList<>();
			criterios.add(new ItemComparator("cd_matricula", "" + dadosFuncionais.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_setor", "" + setor.getCdSetor(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmLotacao = LotacaoDAO.find(criterios, connect);
			if(rsmLotacao.next()){
				lotacao = LotacaoDAO.get(rsmLotacao.getInt("cd_lotacao"), rsmLotacao.getInt("cd_matricula"), connect);
			}
			
			
			lotacao.setCdFuncao(cdFuncao);
			if(LotacaoDAO.update(lotacao, connect) <= 0){
				return new Result(-1, "Erro ao atualizar função de professor");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Alteração realizada com sucesso");
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na atualização de função");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveTipoAdmissao(int cdProfessor, int cdTipoAdmissao){
		return saveTipoAdmissao(cdProfessor, cdTipoAdmissao, null);
	}
	
	/**
	 * Salva um tipo de admissão para o professor
	 * @param cdProfessor
	 * @param cdTipoAdmissao
	 * @param connect
	 * @return
	 */
	public static Result saveTipoAdmissao(int cdProfessor, int cdTipoAdmissao, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			DadosFuncionais dadosFuncionais = null;
			ArrayList<ItemComparator> criterios = new ArrayList<>();
			criterios.add(new ItemComparator("cd_pessoa", "" + cdProfessor, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmDadosFuncionais = DadosFuncionaisDAO.find(criterios, connect);
			if(rsmDadosFuncionais.next()){
				dadosFuncionais = DadosFuncionaisDAO.get(rsmDadosFuncionais.getInt("cd_matricula"), connect);
			}
			
			dadosFuncionais.setCdTipoAdmissao(cdTipoAdmissao);
			if(DadosFuncionaisDAO.update(dadosFuncionais, connect) <= 0){
				return new Result(-1, "Erro ao atualizar função de professor");
			}
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Alteração realizada com sucesso");
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na atualização de tipo de admissao");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdProfessor){
		return remove(cdProfessor, false, null);
	}
	public static Result remove(int cdProfessor, boolean cascade){
		return remove(cdProfessor, cascade, null);
	}
	public static Result remove(int cdProfessor, boolean cascade, Connection connect){
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
			retorno = ProfessorDAO.delete(cdProfessor, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_professor");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllInstrutoresEducarte() {
		return getAllInstrutoresEducarte(null);
	}

	/**
	 * Busca todos os professores relacionados ao educarte
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAllInstrutoresEducarte(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			int cdVinculoInstrutorEducarte = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_INSTRUTOR_EDUCARTE", 0, 0, connect);
			
			pstmt = connect.prepareStatement("SELECT C.nr_cpf, B.nm_pessoa AS NM_INSTRUTOR, E.nm_modalidade FROM acd_professor A"
					+ "							JOIN grl_pessoa B ON (A.cd_professor = B.cd_pessoa)"
					+ "							JOIN grl_pessoa_fisica C ON (B.cd_pessoa = C.cd_pessoa)"
					+ "							JOIN grl_pessoa_empresa D ON (B.cd_pessoa = D.cd_pessoa)"
					+ "							LEFT OUTER JOIN acd_modalidade_educarte E ON (A.cd_modalidade_educarte = E.cd_modalidade)"
					+ "							WHERE D.cd_empresa = " + cdSecretaria
					+ " 						  AND D.cd_vinculo = " + cdVinculoInstrutorEducarte);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.getAllInstrutoresEducarte: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.getAllInstrutoresEducarte: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	public static ResultSetMap getAllByInstituicao(int cdInstituicao) {
		return getAllByInstituicao(cdInstituicao, null);
	}

	/**
	 * Busca todos os professores relacionados de uma instituição
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAllByInstituicao(int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, P.nm_pessoa AS nm_professor FROM acd_professor A"
					+ "							JOIN grl_pessoa P ON (A.cd_professor = P.cd_pessoa) "
					+ "							JOIN srh_dados_funcionais B ON (A.cd_professor = B.cd_pessoa) "
					+ "							JOIN srh_lotacao C ON (B.cd_matricula = C.cd_matricula) "
					+ "							JOIN grl_setor D ON (C.cd_setor = D.cd_setor) "
					+ "							JOIN acd_instituicao E ON (D.cd_empresa = E.cd_instituicao) "
					+ "                       WHERE E.cd_instituicao = " + cdInstituicao
					+ "						  ORDER BY P.nm_pessoa");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Seleciona as turmas que o professor está ministrando na instituicao no período letivo vigente
	 * @param codigo do professor, codigo do periodo letivo e codigo da instituicao
	 * @return turmas
	 */
	public static ResultSetMap getTurmasByProfessor(int cdProfessor, int cdInstituicao) {
		return getTurmasByProfessor(cdProfessor, cdInstituicao, null);
	}

	public static ResultSetMap getTurmasByProfessor(int cdProfessor, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			// busca o periodo letivo vigente na instituicao
			ResultSetMap rsmPeriodoLetivoVigente = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao);
			
			int cdPeriodoLetivo = 0;
			if(rsmPeriodoLetivoVigente.next()) {
				cdPeriodoLetivo = rsmPeriodoLetivoVigente.getInt("CD_PERIODO_LETIVO");
			}
			
			pstmt = connect.prepareStatement("SELECT A.cd_turma, A.id_turma, B.tp_turno, B.cd_curso, C.tp_etapa_ensino, D.nm_produto_servico AS nm_curso" + 
											" FROM acd_turma A" +
											" JOIN acd_curso C ON (C.cd_curso = A.cd_curso)" +
											" LEFT JOIN acd_oferta B ON (A.cd_curso = B.cd_curso and A.cd_turma = B.cd_turma)" +
											" LEFT OUTER JOIN grl_produto_servico D ON (B.cd_curso = D.cd_produto_servico)" +
											" WHERE B.cd_professor = " + cdProfessor +
											" AND B.cd_periodo_letivo = " + cdPeriodoLetivo +
											" AND A.cd_instituicao = " + cdInstituicao);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Seleciona as turmas que o professor está ministrando na instituicao no período letivo vigente
	 * @param codigo do professor, codigo do periodo letivo e codigo da instituicao
	 * @return turmas
	 */
	public static ResultSetMap getTurmasByProfessor(int cdProfessor, int cdInstituicao, int cdCurso) {
		return getTurmasByProfessor(cdProfessor, cdInstituicao, cdCurso, null);
	}

	public static ResultSetMap getTurmasByProfessor(int cdProfessor, int cdInstituicao, int cdCurso, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			// busca o periodo letivo vigente na instituicao
			ResultSetMap rsmPeriodoLetivoVigente = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao);
			
			int cdPeriodoLetivo = 0;
			if(rsmPeriodoLetivoVigente.next()) {
				cdPeriodoLetivo = rsmPeriodoLetivoVigente.getInt("CD_PERIODO_LETIVO");
			}
			
			pstmt = connect.prepareStatement("SELECT DISTINCT(A.cd_turma), A.*, B.tp_turno, B.cd_curso, C.tp_etapa_ensino, C.tp_avaliacao, D.nm_produto_servico AS nm_curso" + 
											" FROM acd_turma A" +
											" JOIN acd_curso C ON (C.cd_curso = A.cd_curso)" +
											" LEFT JOIN acd_oferta B ON (A.cd_curso = B.cd_curso and A.cd_turma = B.cd_turma)" +
											" LEFT OUTER JOIN grl_produto_servico D ON (B.cd_curso = D.cd_produto_servico)" +
											" WHERE B.cd_periodo_letivo = " + cdPeriodoLetivo +
											"   AND A.cd_instituicao = " + cdInstituicao + 
											"   AND A.cd_curso = " + cdCurso + 
											(cdProfessor > 0 ? " AND EXISTS(SELECT * FROM acd_pessoa_oferta PO WHERE PO.cd_oferta = B.cd_oferta AND PO.cd_pessoa = " + cdProfessor + ")" : ""));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("NM_TP_TURNO", TurmaServices.tiposTurno[rsm.getInt("TP_TURNO")]);
				rsm.setValueToField("NR_ALUNOS", TurmaServices.getAlunos(rsm.getInt("cd_turma"), connect).size());
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getTurmasOfertaByProfessor(int cdProfessor) {
		return getTurmasOfertaByProfessor(cdProfessor, 0, null);
	}
	
	public static ResultSetMap getTurmasOfertaByProfessor(int cdProfessor, Connection connect) {
		return getTurmasOfertaByProfessor(cdProfessor, 0, connect);
	}
	
	public static ResultSetMap getTurmasOfertaByProfessor(int cdProfessor, int cdInstituicao) {
		return getTurmasOfertaByProfessor(cdProfessor, cdInstituicao, null);
	}

	/**
	 * Busca todas as enturmações de um professor
	 * @param cdProfessor
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getTurmasOfertaByProfessor(int cdProfessor, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			// busca o periodo letivo vigente na instituicao
			ResultSetMap rsmPeriodoLetivoVigente = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao);
			
			int cdPeriodoLetivo = 0;
			if(rsmPeriodoLetivoVigente.next()) {
				cdPeriodoLetivo = rsmPeriodoLetivoVigente.getInt("CD_PERIODO_LETIVO");
			}
			
			pstmt = connect.prepareStatement("SELECT A.cd_turma " + 
											" FROM acd_turma A" +
											" WHERE EXISTS (SELECT * FROM acd_oferta O, acd_pessoa_oferta PO WHERE O.cd_oferta = PO.cd_oferta AND O.cd_turma = A.cd_turma AND O.st_oferta = "+OfertaServices.ST_ATIVO+" AND PO.st_pessoa_oferta = "+PessoaOfertaServices.ST_ATIVO+" AND PO.cd_pessoa = "+cdProfessor+")" +
											"   AND A.st_turma = " + TurmaServices.ST_ATIVO +
											(cdInstituicao > 0 ? " AND A.cd_instituicao = " + cdInstituicao + " AND A.cd_periodo_letivo = " + cdPeriodoLetivo : ""));
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Seleciona as disciplinas que o professor está ministrando na turma no periodo letivo
	 * @param codigo do professor, turma e periodo letivo
	 * @return disciplinas
	 */
	public static ResultSetMap getDisciplinas(int cdProfessor, int cdTurma, int cdInstituicao) {
		return getDisciplinas(cdProfessor, cdTurma, cdInstituicao, null);
	}

	public static ResultSetMap getDisciplinas(int cdProfessor, int cdTurma, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			// busca o periodo letivo vigente na instituicao
			ResultSetMap rsmPeriodoLetivoVigente = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
			
			int cdPeriodoLetivo = 0;
			if(rsmPeriodoLetivoVigente.next()) {
				cdPeriodoLetivo = rsmPeriodoLetivoVigente.getInt("CD_PERIODO_LETIVO");
			}
						
			pstmt = connect.prepareStatement("SELECT A.cd_disciplina, nm_produto_servico AS nm_disciplina FROM acd_oferta A" +
											" LEFT OUTER JOIN grl_produto_servico B ON (A.cd_disciplina = B.cd_produto_servico)" +
											"WHERE (A.cd_professor = " + cdProfessor + " OR EXISTS (SELECT * FROM acd_pessoa_oferta PO WHERE PO.cd_pessoa = "+cdProfessor+" AND PO.cd_oferta = A.cd_oferta AND PO.st_pessoa_oferta = "+PessoaOfertaServices.ST_ATIVO+")) " +
											(cdTurma > 0 ? " AND A.cd_turma = " + cdTurma : "") +
											" AND A.cd_periodo_letivo = " + cdPeriodoLetivo + 
											" AND A.st_oferta = " + OfertaServices.ST_ATIVO);
			ResultSetMap rsmDisciplinas = new ResultSetMap(pstmt.executeQuery());
			ArrayList<Integer> codigosDisciplina = new ArrayList<Integer>();
			int x = 0;
			while(rsmDisciplinas.next()){
				if(codigosDisciplina.contains(rsmDisciplinas.getInt("cd_disciplina"))){
					rsmDisciplinas.deleteRow();
					if(x == 0)
						rsmDisciplinas.beforeFirst();
					else
						rsmDisciplinas.previous();
					continue;
				}
				
				codigosDisciplina.add(rsmDisciplinas.getInt("cd_disciplina"));
				
				x++;
			}
			rsmDisciplinas.beforeFirst();
			return rsmDisciplinas;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_professor", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	/**
	  * Retorna os horários do professor para a semana atual
	  * @param codigo do professor, codigo da instituicao
	  * @return
	  */
	 public static ResultSetMap getHorariosSemana(int cdProfessor, int cdInstituicao) {
		 return getHorariosSemana(cdProfessor, cdInstituicao, null);
	 }

	 public static ResultSetMap getHorariosSemana(int cdProfessor, int cdInstituicao, Connection connect) {
		  boolean isConnectionNull = connect==null;
		  if (isConnectionNull)
			  connect = Conexao.conectar();
		  PreparedStatement pstmt;
		 
		  try {
		   
			   GregorianCalendar dtAtual = new GregorianCalendar();
			   
			   //verificar o limite minimo da semana 
			   GregorianCalendar dtInicialSemana = new GregorianCalendar();
			   dtInicialSemana.set(Calendar.DAY_OF_WEEK, dtAtual.getActualMinimum(Calendar.DAY_OF_WEEK));
			   dtInicialSemana.add(Calendar.DATE, -7);
			   
			   pstmt = connect.prepareStatement("SELECT A.*, C.nm_produto_servico as nm_disciplina, D.id_turma "+
					  " FROM acd_oferta_horario A "+
					  " LEFT OUTER JOIN acd_oferta B ON (B.cd_oferta = A.cd_oferta) "+
					  " LEFT OUTER JOIN grl_produto_servico C ON (B.cd_disciplina = C.cd_produto_servico) "+
					  " LEFT OUTER JOIN acd_turma D ON (B.cd_turma = D.cd_turma) "+
					  " WHERE B.cd_professor = "+ cdProfessor +
					  "   AND D.cd_instituicao = " + cdInstituicao +
					  " ORDER BY nr_dia_semana");
			   ResultSetMap rsm =  new ResultSetMap(pstmt.executeQuery());
			   
			   while(rsm.next()) {
				   //Clonar o objeto data permite que não ocorra acesso à mesma variável na memória
				   GregorianCalendar dt = (GregorianCalendar)dtInicialSemana.clone();
				   //Adicionando valor de dia da semana à data inicial que corresponde ao domingo
				   dt.add(Calendar.DATE, rsm.getInt("NR_DIA_SEMANA"));
				   
				   GregorianCalendar dtHrInicio = rsm.getGregorianCalendar("HR_INICIO");
				   dtHrInicio.set(Calendar.DATE, dt.get(Calendar.DATE));
				   dtHrInicio.set(Calendar.MONTH, dt.get(Calendar.MONTH));
				   dtHrInicio.set(Calendar.YEAR, dt.get(Calendar.YEAR));
				   
				   GregorianCalendar dtHrTermino = rsm.getGregorianCalendar("HR_TERMINO");
				   dtHrTermino.set(Calendar.DATE, dt.get(Calendar.DATE));
				   dtHrTermino.set(Calendar.MONTH, dt.get(Calendar.MONTH));
				   dtHrTermino.set(Calendar.YEAR, dt.get(Calendar.YEAR));
				   
				   rsm.setValueToField("DT_INICIO", dtHrInicio);
				   rsm.setValueToField("DT_TERMINO", dtHrTermino);
			   }
			   
			   rsm.beforeFirst();
			   
			   return rsm;
			   
		  }
		  catch(SQLException sqlExpt) {
			   sqlExpt.printStackTrace(System.out);
			   System.err.println("Erro! ProfessorServices.getAll: " + sqlExpt);
			   return null;
		  }
		  catch(Exception e) {
			   e.printStackTrace(System.out);
			   System.err.println("Erro! ProfessorServices.getAll: " + e);
			   return null;
		  }
		  finally {
			   if (isConnectionNull)
			    Conexao.desconectar(connect);
		  }
	 }
	 public static Result findRelatorioQuantidade(ArrayList<ItemComparator> criterios) {
			return findRelatorioQuantidade(criterios, null);
	 }

	 /**
	  * Faz a busca do relatório, mas apenas com a quantidade de professores segundo os critérios
	  * @param criterios
	  * @param connect
	  * @return
	  */
	 public static Result findRelatorioQuantidade(ArrayList<ItemComparator> criterios, Connection connect) {
		 boolean isConnectionNull = connect==null;
		 try{
		 	if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		 	
		 	
		 	Result ret = findRelatorio(criterios, connect);
		 	
		 	ResultSetMap rsm = (ResultSetMap)ret.getObjects().get("RSM");
		 	ResultSetMap rsmFinal = new ResultSetMap();
		 	HashMap<String, Object> conjuntoRegistro = new HashMap<String, Object>();
		 	HashMap<String, ArrayList<Integer>> conjuntoRegistroInstituicaoProfessor = new HashMap<String, ArrayList<Integer>>();
		 	while(rsm.next()){
		 		if(!conjuntoRegistro.containsKey(rsm.getString("nm_instituicao"))){
		 			conjuntoRegistro.put(rsm.getString("nm_instituicao"), 1);
		 			ArrayList<Integer> codProfessores = new ArrayList<Integer>();
		 			codProfessores.add(rsm.getInt("cd_pessoa"));
		 			conjuntoRegistroInstituicaoProfessor.put(rsm.getString("nm_instituicao"), codProfessores);
		 		}
		 		else{
		 			ArrayList<Integer> codProfessores = (ArrayList<Integer>)conjuntoRegistroInstituicaoProfessor.get(rsm.getString("nm_instituicao"));
		 			if(!codProfessores.contains(rsm.getInt("cd_pessoa"))){
		 				conjuntoRegistro.put(rsm.getString("nm_instituicao"), (Integer.parseInt(String.valueOf(conjuntoRegistro.get(rsm.getString("nm_instituicao")))) + 1));
		 				codProfessores.add(rsm.getInt("cd_pessoa"));
		 			}
		 		}
		 	}
		 	
		 	for(String key : conjuntoRegistro.keySet()){
		 		HashMap<String, Object> register = new HashMap<String, Object>();
		 		register.put("NM_INSTITUICAO", key);
		 		criterios = new ArrayList<ItemComparator>();
		 		criterios.add(new ItemComparator("nm_pessoa", key, ItemComparator.EQUAL, Types.VARCHAR));
		 		ResultSetMap rsmPessoaEscola = PessoaDAO.find(criterios, connect);
		 		InstituicaoEducacenso instituicaoEducacenso = null;
		 		if(rsmPessoaEscola.next()){
		 			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(rsmPessoaEscola.getInt("cd_pessoa"), connect);
					int cdPeriodoLetivoAtual = 0;
					if(rsmPeriodoAtual.next()){
						cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
					}
		 			instituicaoEducacenso = InstituicaoEducacensoDAO.get(rsmPessoaEscola.getInt("cd_pessoa"), cdPeriodoLetivoAtual, connect);
		 		}
		 		register.put("QT_PROFESSORES", Integer.parseInt(String.valueOf((conjuntoRegistro.get(key)))));
		 		if(key.contains("CRECHE") || key.contains("Creche")){
		 			register.put("NM_ZONA", "Creche");
		 		}
		 		else
		 			register.put("NM_ZONA", InstituicaoEducacensoServices.tipoLocalizacao[(instituicaoEducacenso.getTpLocalizacao()-1)]);
		 		rsmFinal.addRegister(register);
		 	}
		 	rsmFinal.beforeFirst();
		 	
		 	ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_ZONA");
			fields.add("NM_INSTITUICAO");
			rsmFinal.orderBy(fields);
		 	
		 	ret.addObject("RSM", rsmFinal);
		 	ret.addObject("RSMPROFESSORES", rsmFinal);
		 	
		 	return ret;
			
		 }
		 catch(Exception e) {		
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		 }
		 finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		 }
	 }
 
	 
	 
	 /**
	  * Busca para o relatório de professores
	  * @param criterios
	  * @return
	  */
	 public static Result findRelatorio(ArrayList<ItemComparator> criterios) {
			return findRelatorio(criterios, null);
		}

		public static Result findRelatorio(ArrayList<ItemComparator> criterios, Connection connect) {
			boolean isConnectionNull = connect==null;
			try{
				
				if (isConnectionNull){
					connect = Conexao.conectar();
					connect.setAutoCommit(false);
				}
				
				int qtLimite = 0;
				String nmPessoa = "";
				int cdInstituicao = 0;
				String nrIdadeInicial = null;
				String nrIdadeFinal   = null;
				int cdDeficiencia = -1;
				int cdDoenca = -1;
				int cdAlergia = -1;
				int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
				ResultSetMap rsmPeriodoLetivo = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdSecretaria, connect);
				int cdPeriodoLetivoSecretaria = 0;
				if(rsmPeriodoLetivo.next())
					cdPeriodoLetivoSecretaria = rsmPeriodoLetivo.getInt("cd_periodo_letivo");
				InstituicaoPeriodo instituicaoPeriodoSecretaria = InstituicaoPeriodoDAO.get(cdPeriodoLetivoSecretaria, connect);
				criterios.add(new ItemComparator("TPL.nm_periodo_letivo", instituicaoPeriodoSecretaria.getNmPeriodoLetivo(), ItemComparator.EQUAL, Types.VARCHAR));
				int tpPosGraduacao = -1;
				int cdFormacaoCurso = 0;
				int cdFormacaoOutrosCursos = 0;
				int cdInstituicaoSuperior = 0;
				int tpRelatorio = 0;
				int tpModalidade = 0;
				int cdTipoAdmissao = 0;
				int cdFuncao = 0;
				
				ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
				for (int i=0; criterios!=null && i<criterios.size(); i++) {
					if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
						qtLimite = Integer.parseInt(criterios.get(i).getValue());
					else if (criterios.get(i).getColumn().equalsIgnoreCase("A.nm_pessoa")) {
						nmPessoa = Util.limparTexto(criterios.get(i).getValue());
						nmPessoa = "%"+nmPessoa.replaceAll(" ", "%")+"%";
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("nrIdadeInicial")) {
						nrIdadeInicial = criterios.get(i).getValue();
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("nrIdadeFinal")) {
						nrIdadeFinal = criterios.get(i).getValue();
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("cdDeficiencia")) {
						cdDeficiencia = Integer.parseInt(criterios.get(i).getValue());
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("cdDoenca")) {
						cdDoenca = Integer.parseInt(criterios.get(i).getValue());
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("cdAlergia")) {
						cdAlergia = Integer.parseInt(criterios.get(i).getValue());
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("tpPosGraduacao")) {
						tpPosGraduacao = Integer.parseInt(criterios.get(i).getValue());
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("cdFormacaoCurso")) {
						cdFormacaoCurso = Integer.parseInt(criterios.get(i).getValue());
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("cdFormacaoOutrosCursos")) {
						cdFormacaoOutrosCursos = Integer.parseInt(criterios.get(i).getValue());
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("cdInstituicaoSuperior")) {
						cdInstituicaoSuperior = Integer.parseInt(criterios.get(i).getValue());
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("tpRelatorio")) {
						tpRelatorio = Integer.parseInt(criterios.get(i).getValue());
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("tpModalidade")) {
						tpModalidade = Integer.parseInt(criterios.get(i).getValue());
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("cdTipoAdmissao")) {
						cdTipoAdmissao = Integer.parseInt(criterios.get(i).getValue());
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("cdFuncao")) {
						cdFuncao = Integer.parseInt(criterios.get(i).getValue());
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("cdInstituicao")) {
						cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
					}
					else
						crt.add(criterios.get(i));
					
				}
				String nmInstituicaoSuperior = null;
				InstituicaoSuperior instituicaoSuperior = InstituicaoSuperiorDAO.get(cdInstituicaoSuperior, connect);
				if(instituicaoSuperior != null){
					nmInstituicaoSuperior = instituicaoSuperior.getNmInstituicaoSuperior();
				}
				
				String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
				String sql =
			 			  "SELECT "+sqlLimit[0]+" P.cd_pessoa, P.nm_pessoa AS nm_professor, PF.dt_nascimento, PF.nr_cpf, P.nm_email, P.nr_telefone1, P.nr_telefone2, P.nr_celular, PE.cd_cidade, PE.nm_logradouro, PE.nm_bairro, PE.nr_endereco, PE.nm_complemento,  " +
			 			  "  TE.nm_etapa, CPS.nm_produto_servico AS nm_modalidade_curso, T.nm_turma, T.cd_curso, T.tp_turno, T.cd_instituicao, DPS.nm_produto_servico AS nm_disciplina, IE.tp_localizacao AS tp_zona, " +
			 			  "  CPS.id_produto_servico AS id_produto_servico_curso, IP.nm_pessoa as nm_instituicao " +		  
	 					  "FROM grl_pessoa P " +
	 					  "LEFT OUTER JOIN grl_pessoa_fisica 	PF ON (P.cd_pessoa = PF.cd_pessoa) " +
	 					  "LEFT OUTER JOIN grl_pessoa_empresa 	F ON (P.cd_pessoa = F.cd_pessoa) " +
	 					  "LEFT OUTER JOIN grl_pessoa_endereco 	PE ON (P.cd_pessoa = PE.cd_pessoa AND PE.lg_principal = 1) " +
	 					  "LEFT OUTER JOIN grl_pessoa_ficha_medica PFM ON (P.cd_pessoa = PFM.cd_pessoa) " +
	 					  "JOIN acd_professor 				    PROF ON (P.cd_pessoa = PROF.cd_professor) " +
	 					  (cdInstituicao <= 0 ? " LEFT OUTER " : "") + "JOIN acd_pessoa_oferta	PO ON (P.cd_pessoa = PO.cd_pessoa) " +
	 					  (cdInstituicao <= 0 ? " LEFT OUTER " : "") + "JOIN acd_oferta	 	    O ON (PO.cd_oferta = O.cd_oferta) " +
	 					  (cdInstituicao <= 0 ? " LEFT OUTER " : "") + "JOIN acd_turma	 	    T ON (O.cd_turma = T.cd_turma) " +
	 					  (cdInstituicao <= 0 ? " LEFT OUTER " : "") + "JOIN acd_instituicao      I ON (T.cd_instituicao = I.cd_instituicao) " +
	 					  "LEFT OUTER JOIN grl_pessoa           IP ON (I.cd_instituicao = IP.cd_pessoa) " +
	 					  "LEFT OUTER JOIN acd_instituicao_periodo TPL ON (T.cd_periodo_letivo = TPL.cd_periodo_letivo) " +
	 					 "LEFT OUTER JOIN acd_instituicao_educacenso IE ON (T.cd_instituicao = IE.cd_instituicao AND IE.cd_periodo_letivo = TPL.cd_periodo_letivo) " +
	 					  "LEFT OUTER JOIN acd_disciplina 	    D ON (O.cd_disciplina = D.cd_disciplina) " +
	 					  "LEFT OUTER JOIN grl_produto_servico             DPS ON (D.cd_disciplina = DPS.cd_produto_servico) " +
	 					  "LEFT OUTER JOIN acd_curso            C ON (T.cd_curso = C.cd_curso) " +
	 					  "LEFT OUTER JOIN grl_produto_servico             CPS ON (C.cd_curso = CPS.cd_produto_servico) " +
	 					  "LEFT OUTER JOIN acd_curso_etapa      CE ON (C.cd_curso = CE.cd_curso) " +
	 					  "LEFT OUTER JOIN acd_tipo_etapa       TE ON (CE.cd_etapa = TE.cd_etapa) " +
	 					  "LEFT OUTER JOIN srh_dados_funcionais DF ON (P.cd_pessoa = DF.cd_pessoa"+ 
	 					  "												AND DF.cd_empresa = "+cdSecretaria+") " +
	 					  "LEFT OUTER JOIN srh_tipo_admissao    TA ON (DF.cd_tipo_admissao = TA.cd_tipo_admissao) " +
	 					  "WHERE 1 = 1 " +
	 				   (cdInstituicao > 0 ? " AND T.cd_instituicao = " + cdInstituicao + " AND O.st_oferta = " + OfertaServices.ST_ATIVO + " AND PO.st_pessoa_oferta = " + PessoaOfertaServices.ST_ATIVO : "") +  
					   (!nmPessoa.equals("") ? 
						  "	  AND TRANSLATE(P.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE TRANSLATE('"+nmPessoa+"', 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') ": "") +
					   (cdTipoAdmissao > 0 ? " AND (TA.cd_tipo_admissao = "+cdTipoAdmissao+" OR TA.cd_tipo_admissao_superior = "+cdTipoAdmissao+")" : "")+
					   (cdFuncao > 0 ? " AND (DF.cd_funcao = "+cdFuncao+")" : "")+
					   (cdDeficiencia >= 0 ? (cdDeficiencia == 0 ? " AND EXISTS ( SELECT PNE.cd_pessoa FROM grl_pessoa_necessidade_especial PNE WHERE PNE.cd_pessoa = PROF.cd_professor ) " : " AND EXISTS ( SELECT PNE.cd_pessoa FROM grl_pessoa_necessidade_especial PNE WHERE PNE.cd_pessoa = PROF.cd_professor AND PNE.cd_tipo_necessidade_especial = "+cdDeficiencia+" ) ") : "") + 
					   (cdDoenca >= 0 ? (cdDoenca == 0 ? " AND EXISTS ( SELECT PD.cd_pessoa FROM grl_pessoa_doenca PD WHERE PD.cd_pessoa = PROF.cd_professor ) " : " AND EXISTS ( SELECT PD.cd_pessoa FROM grl_pessoa_doenca PD WHERE PD.cd_pessoa = PROF.cd_professor AND PD.cd_doenca = "+cdDoenca+" ) ") : "")+
					   (cdAlergia >= 0 ? (cdAlergia == 0 ? " AND EXISTS ( SELECT PA.cd_pessoa FROM grl_pessoa_alergia PA WHERE PA.cd_pessoa = PROF.cd_professor ) " : " AND EXISTS ( SELECT PA.cd_pessoa FROM grl_pessoa_alergia PA WHERE PA.cd_pessoa = PROF.cd_professor AND PA.cd_alergia = "+cdAlergia+" ) ") : "") +
					   (tpPosGraduacao == 0 ? " AND PROF.lg_pos = 1 " : (tpPosGraduacao == 1 ? " AND PROF.lg_mestrado = 1 " : (tpPosGraduacao == 2 ? " AND PROF.lg_doutorado = 1 " : (tpPosGraduacao == 3 ? " AND PROF.lg_pos = 0 AND PROF.lg_mestrado = 0 AND PROF.lg_doutorado = 0 " : "")))) + 
					   (cdFormacaoCurso > 0 ? " AND EXISTS (SELECT FORM.cd_formacao_curso FROM acd_formacao FORM, acd_formacao_curso FORM_C WHERE FORM.cd_formacao_curso = FORM_C.cd_formacao_curso AND FORM.cd_formacao_curso = "+cdFormacaoCurso+" AND FORM.cd_pessoa = PROF.cd_professor) " : "")+
					   (cdFormacaoOutrosCursos > 0 ? " AND EXISTS (SELECT FORM.cd_formacao_curso FROM acd_formacao FORM, acd_formacao_curso FORM_C WHERE FORM.cd_formacao_curso = FORM_C.cd_formacao_curso AND FORM.cd_formacao_curso = "+cdFormacaoOutrosCursos+" AND FORM.cd_pessoa = PROF.cd_professor) " : "")+
					   (nmInstituicaoSuperior != null ? " AND EXISTS (SELECT FORM.cd_formacao_curso FROM acd_formacao FORM, acd_formacao_curso FORM_C WHERE FORM.cd_formacao_curso = FORM_C.cd_formacao_curso AND nm_instituicao = '"+nmInstituicaoSuperior+"' AND FORM.cd_pessoa = PROF.cd_professor) " : "");
				
				ResultSetMap rsm = Search.find(sql, "ORDER BY P.nm_pessoa "+sqlLimit[1], crt, connect!=null ? connect : Conexao.conectar(), connect==null);
				
				int qtMatriculadosZonaUrbana = 0;
				int qtMatriculadosZonaRural = 0;
				int qtMatriculadosCreche = 0;
				int qtMatriculadosTotal = 0;
				int x = 0;
				HashMap<String, ArrayList<Integer>> conjuntoCodigosProfessorZona = new HashMap<String, ArrayList<Integer>>();
				ArrayList<Integer> codigosProfessores = new ArrayList<Integer>();
				ResultSetMap rsmProfessores = new ResultSetMap();
				while(rsm.next()){
					
					rsm.setValueToField("NM_ENDERECO_COMPLETO", rsm.getString("nm_logradouro") + ", " + rsm.getString("nr_endereco") + ", " + rsm.getString("nm_bairro"));
					
					if(tpModalidade == 1){
						boolean achado = false;
						ResultSetMap rsmCursosInfantil = ParametroServices.getValoresOfParametro("CD_CURSO_EDUCACAO_INFANTIL");
						while(rsmCursosInfantil.next()){
							if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosInfantil.getString("VL_REAL"))){
								achado = true;
								break;
							}
									
						}
						if(!achado){
							rsm.deleteRow();
							if(x == 0)
								rsm.beforeFirst();
							else
								rsm.previous();
							continue;
						}
						
					}
					else if(tpModalidade == 2){
						boolean achado = false;
						ResultSetMap rsmCursosFundamental1 = ParametroServices.getValoresOfParametro("CD_CURSO_FUNDAMENTAL_1");
						while(rsmCursosFundamental1.next()){
							if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental1.getString("VL_REAL"))){
								achado = true;
								break;
							}
									
						}
						if(!achado){
							rsm.deleteRow();
							if(x == 0)
								rsm.beforeFirst();
							else
								rsm.previous();
							continue;
						}
					}
					else if(tpModalidade == 3){
						boolean achado = false;
						ResultSetMap rsmCursosFundamental2 = ParametroServices.getValoresOfParametro("CD_CURSO_FUNDAMENTAL_2");
						while(rsmCursosFundamental2.next()){
							if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental2.getString("VL_REAL"))){
								achado = true;
								break;
							}
									
						}
						
						if(!achado){
							rsm.deleteRow();
							if(x == 0)
								rsm.beforeFirst();
							else
								rsm.previous();
							continue;
						}
					}
					
					rsm.setValueToField("NM_ZONA", PessoaEnderecoServices.tiposZona[rsm.getInt("tp_zona")]);
					rsm.setValueToField("CL_TURNO", TurmaServices.tiposTurno[rsm.getInt("tp_turno")]);
					
					GregorianCalendar dtNascimento = rsm.getGregorianCalendar("dt_nascimento");
					
					Cidade naturalidade = CidadeDAO.get(rsm.getInt("cd_naturalidade"), connect);
					if(naturalidade != null){
						rsm.setValueToField("NM_NATURALIDADE", naturalidade.getNmCidade());
						Estado estadoNaturalidade = EstadoDAO.get(naturalidade.getCdEstado(), connect);
						if(estadoNaturalidade != null)
							rsm.setValueToField("SG_NATURALIDADE", estadoNaturalidade.getSgEstado());
					}
					rsm.setValueToField("NR_TELEFONE", (rsm.getString("NR_TELEFONE1") != null && !rsm.getString("NR_TELEFONE1").trim().equals("") ? rsm.getString("NR_TELEFONE1") : (rsm.getString("NR_TELEFONE2") != null && !rsm.getString("NR_TELEFONE2").trim().equals("") ? rsm.getString("NR_TELEFONE2") : (rsm.getString("NR_CELULAR") != null && !rsm.getString("NR_CELULAR").trim().equals("") ? rsm.getString("NR_CELULAR") : rsm.getString("NR_TELEFONE")))));
					
					
					if(dtNascimento != null){
						int nrIdadeProfessor = Util.getIdade(dtNascimento);
						rsm.setValueToField("DT_NM_NASCIMENTO", (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.DAY_OF_MONTH))) > 9 ? dtNascimento.get(Calendar.DAY_OF_MONTH) : "0" + dtNascimento.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1)) + "/" + dtNascimento.get(Calendar.YEAR) + " (" + nrIdadeProfessor + " anos)");
						rsm.setValueToField("NR_IDADE_PROFESSOR", nrIdadeProfessor);
						
						if(nrIdadeInicial != null && nrIdadeFinal != null){
							if(nrIdadeProfessor < Integer.parseInt(nrIdadeInicial) || nrIdadeProfessor > Integer.parseInt(nrIdadeFinal)){
								rsm.deleteRow();
								if(x == 0)
									rsm.beforeFirst();
								else
									rsm.previous();
								continue;
							}
						}
					}
					
					Instituicao instituicao = InstituicaoDAO.get(rsm.getInt("cd_instituicao"), connect);
					if(instituicao.getTpInstituicao()==InstituicaoServices.TP_INSTITUICAO_CRECHE){
						rsm.setValueToField("NM_ZONA", "Creche");
					}
					
					String txtFormacao = "";
					ResultSetMap rsmFormacao = new ResultSetMap(connect.prepareStatement("SELECT * "
			  				+ "																		FROM acd_formacao A, acd_formacao_curso B "
			  				+ "																	   WHERE A.nm_curso = B.nm_curso AND A.cd_pessoa = "+rsm.getInt("cd_pessoa") + " AND B.cd_formacao_area_conhecimento > 0").executeQuery());
					while(rsmFormacao.next()){
						
						if(rsmFormacao.getString("NM_CURSO") != null && !rsmFormacao.getString("NM_CURSO").equals("") && !rsmFormacao.getString("NM_CURSO").equals("null"))
							txtFormacao += rsmFormacao.getString("NM_CURSO") + ", ";
						
					}
					
					txtFormacao = (txtFormacao.length() > 0 ? txtFormacao.substring(0, txtFormacao.length()-2) : txtFormacao);
					
					if(txtFormacao.equals("null") || txtFormacao == null)
						txtFormacao = "";
					
					rsm.setValueToField("TXT_FORMACAO", txtFormacao);
					
					
					if(codigosProfessores.size() <= 0 || !codigosProfessores.contains(rsm.getInt("cd_pessoa"))){
						codigosProfessores.add(rsm.getInt("cd_pessoa"));
						
						if(instituicao != null){
							
							InstituicaoEducacenso instituicaoEducacenso = InstituicaoEducacensoDAO.get(instituicao.getCdInstituicao(), InstituicaoServices.getPeriodoLetivoCorrespondente(instituicao.getCdInstituicao(), cdPeriodoLetivoSecretaria, connect), connect);
							if(instituicaoEducacenso != null){
								
								if(instituicaoEducacenso.getTpLocalizacao() == PessoaEnderecoServices.TP_ZONA_RURAL && (instituicao.getTpInstituicao() == InstituicaoServices.TP_INSTITUICAO_ESCOLA)){
									qtMatriculadosZonaRural += 1;
								}
								else if(instituicaoEducacenso.getTpLocalizacao() == PessoaEnderecoServices.TP_ZONA_URBANA && (instituicao.getTpInstituicao() == InstituicaoServices.TP_INSTITUICAO_ESCOLA)){
									qtMatriculadosZonaUrbana += 1;
								}
								else if(instituicao.getTpInstituicao() == InstituicaoServices.TP_INSTITUICAO_CRECHE){
									qtMatriculadosCreche += 1;
								}
								
								qtMatriculadosTotal++;
							}
							
						}
						
						rsmProfessores.addRegister(rsm.getRegister());
						
						ArrayList<Integer> codigosProfessorZona = conjuntoCodigosProfessorZona.get(rsm.getString("NM_ZONA"));
						if(codigosProfessorZona == null){
							codigosProfessorZona = new ArrayList<Integer>();
						}
						codigosProfessorZona.add(rsm.getInt("cd_pessoa"));
						conjuntoCodigosProfessorZona.put(rsm.getString("NM_ZONA"), codigosProfessorZona);
					}
					else if(codigosProfessores.contains(rsm.getInt("cd_pessoa"))){
						
						ArrayList<Integer> codigosProfessorZona = conjuntoCodigosProfessorZona.get(rsm.getString("NM_ZONA"));
						if(!codigosProfessorZona.contains(rsm.getInt("cd_pessoa"))){
							if(instituicao != null){
								InstituicaoEducacenso instituicaoEducacenso = InstituicaoEducacensoDAO.get(instituicao.getCdInstituicao(), InstituicaoServices.getPeriodoLetivoCorrespondente(instituicao.getCdInstituicao(), cdPeriodoLetivoSecretaria, connect), connect);
								if(instituicaoEducacenso != null){
									
									if(instituicaoEducacenso.getTpLocalizacao() == PessoaEnderecoServices.TP_ZONA_RURAL && (instituicao.getTpInstituicao() == InstituicaoServices.TP_INSTITUICAO_ESCOLA)){
										qtMatriculadosZonaRural += 1;
									}
									else if(instituicaoEducacenso.getTpLocalizacao() == PessoaEnderecoServices.TP_ZONA_URBANA && (instituicao.getTpInstituicao() == InstituicaoServices.TP_INSTITUICAO_ESCOLA)){
										qtMatriculadosZonaUrbana += 1;
									}
									else if(instituicao.getTpInstituicao() == InstituicaoServices.TP_INSTITUICAO_CRECHE){
										qtMatriculadosCreche += 1;
									}
									
								}
								
							}
							
							codigosProfessorZona.add(rsm.getInt("cd_pessoa"));
							conjuntoCodigosProfessorZona.put(rsm.getString("NM_ZONA"), codigosProfessorZona);
						}
					}
					
					x++;
				}
				rsm.beforeFirst();
				
				ArrayList<String> fields = new ArrayList<String>();
				
				if(tpRelatorio == 0 || tpRelatorio == 1){
					fields.add("NM_ZONA");
					fields.add("NM_INSTITUICAO");
					fields.add("NM_PROFESSOR");
					fields.add("ID_PRODUTO_SERVICO_CURSO");
					fields.add("NM_TURMA");
					fields.add("NM_DISCIPLINA");
				}
				else if(tpRelatorio == 2){
					fields.add("NM_PROFESSOR");
					fields.add("NM_INSTITUICAO");
					fields.add("ID_PRODUTO_SERVICO_CURSO");
					fields.add("NM_TURMA");
					fields.add("NM_DISCIPLINA");
				}
				
				rsm.orderBy(fields);
				rsm.beforeFirst();
				
				rsmProfessores.orderBy(fields);
				rsmProfessores.beforeFirst();
				
				Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsm);
				result.addObject("RSMPROFESSORES", rsmProfessores);
				result.addObject("QT_MATRICULADOS_ZONA_URBANA", qtMatriculadosZonaUrbana);
				result.addObject("QT_MATRICULADOS_ZONA_RURAL", qtMatriculadosZonaRural);
				result.addObject("QT_MATRICULADOS_CRECHE", qtMatriculadosCreche);
				result.addObject("QT_MATRICULADOS_TOTAL", qtMatriculadosTotal);
				return result;
			}
			catch(Exception e) {		
				e.printStackTrace(System.out);
				Util.registerLog(e);
				return null;
			}
			finally {
				if (isConnectionNull)
					Conexao.desconectar(connect);
			}
		}
		
	@Deprecated 
	public static Result autenticar(String nmLogin, String nmSenha) {
		return autenticar(nmLogin, nmSenha, null);
	}

	@Deprecated
	public static Result autenticar(String nmLogin, String nmSenha, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM seg_usuario " +
															   "WHERE nm_login	 = ? " +
															   "  AND st_usuario = 1");
			pstmt.setString(1, nmLogin);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				Usuario usuario = new Usuario(rs.getInt("cd_usuario"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_pergunta_secreta"),
						rs.getString("nm_login"),
						rs.getString("nm_senha"),
						rs.getInt("tp_usuario"),
						rs.getString("nm_resposta_secreta"),
						rs.getInt("st_usuario"));
				
				Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);
				//TODO Remover teste de loginHash após atualização do banco
				int lgLoginHash = ParametroServices.getValorOfParametroAsInteger("LG_LOGIN_HASH", 0, 0, connect);
				if( lgLoginHash > 0 )
					nmSenha = UsuarioServices.getPasswordHash(nmSenha);
				if(nmSenha.equals(usuario.getNmSenha())){
					Result r = new Result(usuario.getCdUsuario(), "Autenticado com sucesso...");
					
					Empresa empresa = EmpresaServices.getDefaultEmpresa(connect);
					
					r.addObject("USUARIO", usuario);
					r.addObject("PESSOA", pessoa);
					r.addObject("EMPRESA", empresa);
					
					//SETORES
					ResultSetMap rsmSetores = new ResultSetMap();
					rsmSetores = LotacaoServices.getAllByPessoa(usuario.getCdPessoa(), connect);
					
					if(rsmSetores==null || rsmSetores.getLines().size()==0)
						return new Result(-4, "Este usuário não está vinculado à nenhum setor.");
					
					r.addObject("SETORES", rsmSetores);
					
					//EMPRESAS
					ResultSetMap rsmEmpresas = new ResultSetMap();
					rsmEmpresas = LotacaoServices.getAllEmpresasByPessoa(usuario.getCdPessoa(), connect);
					
					if(rsmEmpresas==null || rsmEmpresas.getLines().size()==0)
						return new Result(-5, "Este usuário não está vinculado à nenhuma empresa.");
					
					r.addObject("EMPRESAS", rsmEmpresas);
					
					/* REGISTRAR LOG */
					if(usuario!=null && pessoa!=null){
						GregorianCalendar dtLog = new GregorianCalendar();
						String remoteIP = null;
						String txtExecucao = "Autenticação de acesso ao módulo de professores às " + Util.formatDateTime(dtLog, "dd/MM/yyyy HH:mm:ss", "") + " por " +
							pessoa.getNmPessoa() + ", através da conta " + usuario.getNmLogin() +
							((remoteIP!=null)?", IP "+remoteIP:"");
						
						Sistema log = new Sistema(0, //cdLog
								dtLog, //dtLog
								txtExecucao, //txtLog
								com.tivic.manager.log.SistemaServices.TP_LOGIN, //tpLog
								usuario.getCdUsuario());//cdUsuario
						SistemaDAO.insert(log, connect);
					}
					
					return r;
				}
				else
					return new Result(-2, "Senha inválida...");
			}
			else
				return new Result(-1, "Login inválido...");
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			Util.registerLog(e);
			return new Result(-3, "Erro ao autenticar...");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result validacaoProfessores(int cdInstituicao, int cdUsuario){
		return validacaoProfessores(cdInstituicao, cdUsuario, null);
	}
	
	public static Result validacaoProfessores(int cdInstituicao, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsmProfessores = InstituicaoServices.getAllProfessoresLotados(cdInstituicao, connect);
			int count = 0;
			while(rsmProfessores.next()){
				
				Professor professor = ProfessorDAO.get(rsmProfessores.getInt("cd_professor"), connect);
				System.out.println((++count) + " - Professor: " + professor.getNmPessoa());
				
				Result resultado = validacaoProfessor(cdInstituicao, rsmProfessores.getInt("cd_professor"), cdUsuario, connect);
				if(resultado.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return resultado;
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Sucesso ao validar");
			return result;
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro em InstituicaoServices.validacaoAlunos");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result validacaoProfessor(int cdInstituicao, int cdProfessor, int cdUsuario){
		return validacaoProfessor(cdInstituicao, cdProfessor, cdUsuario, null);
	}
	
	public static Result validacaoProfessor(int cdInstituicao, int cdProfessor, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Professor professor = ProfessorDAO.get(cdProfessor, connect);
			
			//Endereco
			ArrayList<ItemComparator> criterios = new ArrayList<>();
			criterios.add(new ItemComparator("cd_pessoa", "" + professor.getCdProfessor(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmEndereco = PessoaEnderecoDAO.find(criterios, connect);
			PessoaEndereco professorEndereco = null;
			if(rsmEndereco.next()){
				professorEndereco = PessoaEnderecoDAO.get(rsmEndereco.getInt("cd_endereco"), professor.getCdProfessor(), connect);
			}
			
			ValidatorResult resultadoValidacao = validate(professor, professorEndereco, 0, cdInstituicao, GRUPO_VALIDACAO_EDUCACENSO, connect);
			Result resultValidacoesPendencia = InstituicaoPendenciaServices.atualizarValidacaoPendencia(resultadoValidacao, cdInstituicao, 0/*cdTurma*/, 0/*cdAluno*/, professor.getCdProfessor(), InstituicaoPendenciaServices.TP_REGISTRO_PROFESSOR_CENSO, cdUsuario, GRUPO_VALIDACAO_EDUCACENSO, connect);
			if(resultValidacoesPendencia.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return resultValidacoesPendencia;
			}
			
			Result ret = PessoaTipoDocumentacaoServices.validacaoDocumentacao(professor.getCdProfessor(), cdInstituicao, cdUsuario, connect);
			if(ret.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return ret;
			}
			
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Sucesso ao validar");
			return result;
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro em InstituicaoServices.validacaoAlunos");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
			
	
	public static Result getProfessoresDadosBasicos(int cdPeriodoLetivo) {
		return getProfessoresDadosBasicos(cdPeriodoLetivo, 0, null);
	}
	
	public static Result getProfessoresDadosBasicos(int cdPeriodoLetivo, int cdInstituicao) {
		return getProfessoresDadosBasicos(cdPeriodoLetivo, cdInstituicao, null);
	}
	
	/**
	 * Faz um quantidade de perfil dos professores
	 * @param cdPeriodoLetivo
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static Result getProfessoresDadosBasicos(int cdPeriodoLetivo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT PES.nm_pessoa AS NM_PROFESSOR, PES_FIS.dt_nascimento, PES_FIS.nm_mae, PES_FIS.nr_cpf, PES_FIS.tp_sexo, PES_FIS.tp_raca, PES_END.nr_cep, (PES_END.nm_logradouro || ', ' || PES_END.nr_endereco || ', ' || PES_END.nm_bairro) AS NM_ENDERECO, PES.nm_email, PES.nr_telefone1 AS NR_TELEFONE FROM acd_professor PROF, "
					+ "										grl_pessoa PES, "
					+ "										grl_pessoa_fisica PES_FIS, "
					+ "										grl_pessoa_endereco PES_END,"
					+ "										grl_pessoa_empresa PES_EMP"
					+ "							WHERE PROF.cd_professor = PES.cd_pessoa"
					+ "							  AND PROF.cd_professor = PES_FIS.cd_pessoa"
					+ "							  AND PROF.cd_professor = PES_END.cd_pessoa"
					+ "							  AND PES_END.lg_principal = 1"
					+ "							  AND PROF.cd_professor = PES_EMP.cd_pessoa"
					+ "							  AND PES_EMP.cd_empresa = " + cdSecretaria
					+ "							  AND EXISTS (SELECT * FROM srh_dados_funcionais DAD_FUN, grl_setor SET, srh_lotacao LOT WHERE PROF.cd_professor = DAD_FUN.cd_pessoa AND DAD_FUN.cd_matricula = LOT.cd_matricula AND LOT.cd_setor = SET.cd_setor AND SET.nm_setor = 'CORPO DOCENTE' "+(cdInstituicao > 0 ? "	  AND SET.cd_empresa = " + cdInstituicao : "")+" )"
					+ "							ORDER BY PES.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int qtSexoMasculino = 0;
			int qtSexoFeminino = 0;
			int qtRacaNaoDeclarada = 0;
			int qtRacaBranca = 0;
			int qtRacaPreta = 0;
			int qtRacaParda = 0;
			int qtRacaAmarela = 0;
			int qtRacaIndigena = 0;
			while(rsm.next()){
				if(rsm.getGregorianCalendar("dt_nascimento") != null)
					rsm.setValueToField("DT_NM_NASCIMENTO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_nascimento")));
				
				rsm.setValueToField("CL_SEXO", PessoaFisicaServices.tipoSexo[rsm.getInt("tp_sexo")]);
				rsm.setValueToField("CL_RACA", PessoaFisicaServices.tipoRaca[rsm.getInt("tp_raca")]);
				
				switch(rsm.getInt("tp_sexo")){
					case PessoaFisicaServices.TP_SEXO_FEMININO:
						qtSexoFeminino++;
					break;
					
					case PessoaFisicaServices.TP_SEXO_MASCULINO:
						qtSexoMasculino++;
					break;
				}
				
				switch(rsm.getInt("tp_raca")){
					case PessoaFisicaServices.TP_RACA_AMARELA:
						qtRacaAmarela++;
					break;
					
					case PessoaFisicaServices.TP_RACA_BRANCA:
						qtRacaBranca++;
					break;
					
					case PessoaFisicaServices.TP_RACA_INDIGENA:
						qtRacaIndigena++;
					break;
					
					case PessoaFisicaServices.TP_RACA_NAO_DECLARADA:
						qtRacaNaoDeclarada++;
					break;
					
					case PessoaFisicaServices.TP_RACA_PARDA:
						qtRacaParda++;
					break;
					
					case PessoaFisicaServices.TP_RACA_PRETA:
						qtRacaPreta++;
					break;
				}
				
			}
			rsm.beforeFirst();
			
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "Número de professoras: " + qtSexoFeminino + "\n"
												   + "Numero de professores: " + qtSexoMasculino + "\n"
												   + "\nNumero de professores(as) de Raça \"Não declarada\": " + qtRacaNaoDeclarada + "\n"
												   + "Numero de professores(as) de Raça \"Branca\": " + qtRacaBranca+ "\n"
												   + "Numero de professores(as) de Raça \"Preta\": " + qtRacaPreta + "\n"
												   + "Numero de professores(as) de Raça \"Parda\": " + qtRacaParda + "\n"
												   + "Numero de professores(as) de Raça \"Amarela\": " + qtRacaAmarela + "\n"
												   + "Numero de professores(as) de Raça \"Indigena\": " + qtRacaIndigena + "\n");
			return result;			
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getProfessoresDeficiencias(int cdPeriodoLetivo) {
		return getProfessoresDeficiencias(cdPeriodoLetivo, 0, null);
	}
	
	public static Result getProfessoresDeficiencias(int cdPeriodoLetivo, int cdInstituicao) {
		return getProfessoresDeficiencias(cdPeriodoLetivo, cdInstituicao, null);
	}
	
	/**
	 * Busca todos os professores deficientes do sistema
	 * @param cdPeriodoLetivo
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static Result getProfessoresDeficiencias(int cdPeriodoLetivo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT PROF.cd_professor, PES.nm_pessoa AS NM_PROFESSOR FROM acd_professor PROF, "
					+ "										grl_pessoa PES, "
					+ "										grl_pessoa_empresa PES_EMP"
					+ "							WHERE PROF.cd_professor = PES.cd_pessoa"
					+ "							  AND PROF.cd_professor = PES_EMP.cd_pessoa"
					+ "							  AND PES_EMP.cd_empresa = " + cdSecretaria
					+ "							  AND EXISTS (SELECT * FROM srh_dados_funcionais DAD_FUN, grl_setor SET, srh_lotacao LOT WHERE PROF.cd_professor = DAD_FUN.cd_pessoa AND DAD_FUN.cd_matricula = LOT.cd_matricula AND LOT.cd_setor = SET.cd_setor AND SET.nm_setor = 'CORPO DOCENTE' "+(cdInstituicao > 0 ? "	  AND SET.cd_empresa = " + cdInstituicao : "")+" )"
					+ "							  AND EXISTS (SELECT * FROM grl_pessoa_necessidade_especial PNE WHERE PROF.cd_professor = PNE.cd_pessoa)"
					+ "							ORDER BY PES.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int qtCegueira = 0;
			int qtBaixaVisao = 0;
			int qtSurdez = 0;
			int qtDeficienciaAuditiva = 0;
			int qtSurdoCegueira = 0;
			int qtDeficienciaFisica = 0;
			int qtDeficienciaIntelectual = 0;
			int qtDeficienciaMultipla = 0;
			int qtAutismoInfantil = 0;
			int qtSindromeAsperger = 0;
			int qtSindromeRett = 0;
			int qtTranstornoDesintegrativo = 0;
			int qtSuperdotacao = 0;
			while(rsm.next()){
				
				String nmDeficiencias = "";
				ResultSetMap rsmPessoaNecessidadeEspecial = PessoaNecessidadeEspecialServices.getNecessidadeEspecialByPessoa(rsm.getInt("cd_professor"), connect);
				while(rsmPessoaNecessidadeEspecial.next()){
					nmDeficiencias += rsmPessoaNecessidadeEspecial.getString("nm_tipo_necessidade_especial") + ", ";
					
					switch(rsmPessoaNecessidadeEspecial.getString("id_tipo_necessidade_especial")){
						case InstituicaoEducacensoServices.TP_CEGUEIRA:
							qtCegueira++;
						break;
						
						case InstituicaoEducacensoServices.TP_BAIXA_VISAO:
							qtBaixaVisao++;
						break;
						
						case InstituicaoEducacensoServices.TP_SURDEZ:
							qtSurdez++;
						break;
						
						case InstituicaoEducacensoServices.TP_DEFICIENCIA_AUDITIVA:
							qtDeficienciaAuditiva++;
						break;
						
						case InstituicaoEducacensoServices.TP_SURDOCEGUEIRA:
							qtSurdoCegueira++;
						break;
						
						case InstituicaoEducacensoServices.TP_DEFICIENCIA_FISICA:
							qtDeficienciaFisica++;
						break;
						
						case InstituicaoEducacensoServices.TP_DEFICIENCIA_INTELECTUAL:
							qtDeficienciaIntelectual++;
						break;
						
						case InstituicaoEducacensoServices.TP_DEFICIENCIA_MULTIPLA:
							qtDeficienciaMultipla++;
						break;
						
						case InstituicaoEducacensoServices.TP_DEFICIENCIA_AUTISMO_INFANTIL:
							qtAutismoInfantil++;
						break;
						
						case InstituicaoEducacensoServices.TP_DEFICIENCIA_SINDROME_ASPERGER:
							qtSindromeAsperger++;
						break;
						
						case InstituicaoEducacensoServices.TP_DEFICIENCIA_SINDROME_RETT:
							qtSindromeRett++;
						break;
						
						case InstituicaoEducacensoServices.TP_DEFICIENCIA_TRANSTORNO_DESINTEGRATIVO:
							qtTranstornoDesintegrativo++;
						break;
						
						case InstituicaoEducacensoServices.TP_DEFICIENCIA_SUPERDOTACAO:
							qtSuperdotacao++;
						break;
					}
				}
				nmDeficiencias = nmDeficiencias.substring(0, nmDeficiencias.length()-2);
				rsm.setValueToField("NM_DEFICIENCIAS", nmDeficiencias);
				
			}
			rsm.beforeFirst();
			
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "Alunos com Cegueira: " + qtCegueira + "\n"
													+ "Alunos com Baixa visão: " + qtBaixaVisao + "\n"
													+ "Alunos com Surdez: " + qtSurdez + "\n"
													+ "Alunos com Deficiência Auditiva: " + qtDeficienciaAuditiva + "\n"
													+ "Alunos com Surdo-Cegueira: " + qtSurdoCegueira + "\n"
													+ "Alunos com Deficiência Física: " + qtDeficienciaFisica + "\n"
													+ "Alunos com Deficiência Intelectual: " + qtDeficienciaIntelectual + "\n"
													+ "Alunos com Deficiência Multipla: " + qtDeficienciaMultipla + "\n"
													+ "Alunos com Autismo Infantil: " + qtAutismoInfantil + "\n"
													+ "Alunos com Sindrome de Asperger: " + qtSindromeAsperger + "\n"
													+ "Alunos com Sindrome de Rett: " + qtSindromeRett + "\n"
													+ "Alunos com Transtorno Desintegrativo: " + qtTranstornoDesintegrativo + "\n"
													+ "Alunos com Superdotação: " + qtSuperdotacao + "\n");
			return result;			
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result getProfessoresFormacao(int cdPeriodoLetivo) {
		return getProfessoresFormacao(cdPeriodoLetivo, 0, null);
	}
	
	public static Result getProfessoresFormacao(int cdPeriodoLetivo, int cdInstituicao) {
		return getProfessoresFormacao(cdPeriodoLetivo, cdInstituicao, null);
	}
	
	/**
	 * Busca as formações dos professores
	 * @param cdPeriodoLetivo
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static Result getProfessoresFormacao(int cdPeriodoLetivo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT PROF.cd_professor, PES.nm_pessoa AS NM_PROFESSOR, ESC.cd_escolaridade, nm_escolaridade, PROF.lg_pos, PROF.lg_mestrado, PROF.lg_doutorado FROM acd_professor PROF, "
					+ "										grl_pessoa PES, "
					+ "										grl_pessoa_empresa PES_EMP,"
					+ "										grl_pessoa_fisica PES_FIS,"
					+ "										grl_escolaridade ESC"
					+ "							WHERE PROF.cd_professor = PES.cd_pessoa"
					+ "							  AND PROF.cd_professor = PES_EMP.cd_pessoa"
					+ "							  AND PES_EMP.cd_empresa = " + cdSecretaria
					+ "							  AND PROF.cd_professor = PES_FIS.cd_pessoa"
					+ "							  AND PES_FIS.cd_escolaridade = ESC.cd_escolaridade"
					+ "							  AND EXISTS (SELECT * FROM srh_dados_funcionais DAD_FUN, grl_setor SET, srh_lotacao LOT WHERE PROF.cd_professor = DAD_FUN.cd_pessoa AND DAD_FUN.cd_matricula = LOT.cd_matricula AND LOT.cd_setor = SET.cd_setor AND SET.nm_setor = 'CORPO DOCENTE' "+(cdInstituicao > 0 ? "	  AND SET.cd_empresa = " + cdInstituicao : "")+" )"
					+ "							ORDER BY PES.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			HashMap<String, Integer> registerQuantEscolaridade = new HashMap<String, Integer>();
			HashMap<String, Integer> registerQuantPosGraduacao = new HashMap<String, Integer>();
			HashMap<String, Integer> registerQuantFormacao = new HashMap<String, Integer>();
			while(rsm.next()){
				
				if(registerQuantEscolaridade.get(rsm.getString("nm_escolaridade")) != null){
					registerQuantEscolaridade.put(rsm.getString("nm_escolaridade"), (registerQuantEscolaridade.get(rsm.getString("nm_escolaridade")) + 1));
				}
				else{
					registerQuantEscolaridade.put(rsm.getString("nm_escolaridade"), 1);
				}
				
				
				
				String posGraduacao = "";
				if(rsm.getInt("lg_pos") == 1){
					
					if(registerQuantPosGraduacao.get("Pós-Graduação") != null){
						registerQuantPosGraduacao.put("Pós-Graduação", (registerQuantPosGraduacao.get("Pós-Graduação") + 1));
					}
					else{
						registerQuantPosGraduacao.put("Pós-Graduação", 1);
					}
					
					posGraduacao = "Pós-Graduação, ";
				}
				if(rsm.getInt("lg_mestrado") == 1){
					
					if(registerQuantPosGraduacao.get("Mestrado") != null){
						registerQuantPosGraduacao.put("Mestrado", (registerQuantPosGraduacao.get("Mestrado") + 1));
					}
					else{
						registerQuantPosGraduacao.put("Mestrado", 1);
					}
					
					posGraduacao = "Mestrado, ";
				}
				if(rsm.getInt("lg_doutorado") == 1){
					
					if(registerQuantPosGraduacao.get("Doutorado") != null){
						registerQuantPosGraduacao.put("Doutorado", (registerQuantPosGraduacao.get("Doutorado") + 1));
					}
					else{
						registerQuantPosGraduacao.put("Doutorado", 1);
					}
					
					posGraduacao = "Doutorado, ";
				}
				
				if(posGraduacao != null && !posGraduacao.equals("")){
					posGraduacao = posGraduacao.substring(0, posGraduacao.length()-2);
				}
				
				rsm.setValueToField("NM_POS_GRADUACAO", (posGraduacao != null && !posGraduacao.equals("") ? posGraduacao : "Nenhum"));
				
				
				ResultSetMap rsmFormacao = FormacaoServices.getFormacaoByPessoa(rsm.getInt("cd_professor"), true, connect);
				String nmFormacao = "";
				while(rsmFormacao.next()){
					if(registerQuantFormacao.get(rsmFormacao.getString("nm_curso")) != null){
						registerQuantFormacao.put(rsmFormacao.getString("nm_curso"), (registerQuantFormacao.get(rsmFormacao.getString("nm_curso")) + 1));
					}
					else{
						registerQuantFormacao.put(rsmFormacao.getString("nm_curso"), 1);
					}
					nmFormacao += rsmFormacao.getString("nm_curso") + ", ";
				}
				rsmFormacao.beforeFirst();
				
				if(nmFormacao != null && !nmFormacao.equals("")){
					nmFormacao = nmFormacao.substring(0, nmFormacao.length()-2);
				}
				
				rsm.setValueToField("NM_CURSOS", (nmFormacao != null && !nmFormacao.equals("") ? nmFormacao : "Nenhum"));
				
				rsmFormacao = FormacaoServices.getFormacaoByPessoa(rsm.getInt("cd_professor"), false, connect);
				nmFormacao = "";
				while(rsmFormacao.next()){
					if(registerQuantFormacao.get(rsmFormacao.getString("nm_curso")) != null){
						registerQuantFormacao.put(rsmFormacao.getString("nm_curso"), (registerQuantFormacao.get(rsmFormacao.getString("nm_curso")) + 1));
					}
					else{
						registerQuantFormacao.put("Curso " + rsmFormacao.getString("nm_curso"), 1);
					}
					nmFormacao += rsmFormacao.getString("nm_curso") + ", ";
				}
				rsmFormacao.beforeFirst();
				
				if(nmFormacao != null && !nmFormacao.equals("")){
					nmFormacao = nmFormacao.substring(0, nmFormacao.length()-2);
				}
				
				rsm.setValueToField("NM_OUTROS_CURSOS", (nmFormacao != null && !nmFormacao.equals("") ? nmFormacao : "Nenhum"));
				
				
			}
			rsm.beforeFirst();
			
			String resultado = "";
			SortedSet<String> keys = new TreeSet<String>(registerQuantEscolaridade.keySet());
			for(String chave : keys){
				resultado += "Professores com escolaridade " + chave + ": " + registerQuantEscolaridade.get(chave) + "\n";
			}
			
			resultado+="\n";
			
			keys = new TreeSet<String>(registerQuantPosGraduacao.keySet());
			for(String chave : keys){
				resultado += "Professores com " + chave + ": " + registerQuantPosGraduacao.get(chave) + "\n";
			}
			
			resultado+="\n";
			
			keys = new TreeSet<String>(registerQuantFormacao.keySet());
			for(String chave : keys){
				resultado += "Professores com formação em " + chave + ": " + registerQuantFormacao.get(chave) + "\n";
			}
			
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", resultado);
			return result;			
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getProfessoresAtuacao(int cdPeriodoLetivo) {
		return getProfessoresAtuacao(cdPeriodoLetivo, 0, null);
	}
	
	public static Result getProfessoresAtuacao(int cdPeriodoLetivo, int cdInstituicao) {
		return getProfessoresAtuacao(cdPeriodoLetivo, cdInstituicao, null);
	}
	
	/**
	 * Busca as áreas de atuação dos professores
	 * @param cdPeriodoLetivo
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static Result getProfessoresAtuacao(int cdPeriodoLetivo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT PROF.cd_professor, PES.nm_pessoa AS NM_PROFESSOR, ESC.cd_escolaridade, nm_escolaridade, PROF.lg_pos, PROF.lg_mestrado, PROF.lg_doutorado FROM acd_professor PROF, "
					+ "										grl_pessoa PES, "
					+ "										grl_pessoa_empresa PES_EMP,"
					+ "										grl_pessoa_fisica PES_FIS,"
					+ "										grl_escolaridade ESC"
					+ "							WHERE PROF.cd_professor = PES.cd_pessoa"
					+ "							  AND PROF.cd_professor = PES_EMP.cd_pessoa"
					+ "							  AND PES_EMP.cd_empresa = " + cdSecretaria
					+ "							  AND PROF.cd_professor = PES_FIS.cd_pessoa"
					+ "							  AND PES_FIS.cd_escolaridade = ESC.cd_escolaridade"
					+ "							  AND EXISTS (SELECT * FROM srh_dados_funcionais DAD_FUN, grl_setor SET, srh_lotacao LOT WHERE PROF.cd_professor = DAD_FUN.cd_pessoa AND DAD_FUN.cd_matricula = LOT.cd_matricula AND LOT.cd_setor = SET.cd_setor AND SET.nm_setor = 'CORPO DOCENTE' "+(cdInstituicao > 0 ? "	  AND SET.cd_empresa = " + cdInstituicao : "")+" )"
					+ "							ORDER BY PES.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			HashMap<String, Integer> registerQuantDisciplinas = new HashMap<String, Integer>();
			while(rsm.next()){
				
				ArrayList<Integer> registerQuantEscolas = new ArrayList<Integer>();
				ArrayList<Integer> registerQuantTurmas = new ArrayList<Integer>();
				ArrayList<String> registerDisciplinas = new ArrayList<String>();
				int qtEscolas = 0;
				int qtTurmas = 0;
				int qtAlunos = 0;
				ResultSetMap rsmOfertas = new ResultSetMap(connect.prepareStatement("SELECT O.cd_oferta, O.cd_turma, D.cd_disciplina, E.nm_produto_servico AS NM_DISCIPLINA FROM acd_oferta O, acd_instituicao_periodo IP, acd_disciplina D, grl_produto_servico E WHERE O.cd_periodo_letivo = IP.cd_periodo_letivo AND (IP.cd_periodo_letivo = "+cdPeriodoLetivo+" OR IP.cd_periodo_letivo_superior = " + cdPeriodoLetivo + ") AND EXISTS (SELECT * FROM acd_pessoa_oferta PO WHERE PO.cd_oferta = O.cd_oferta AND PO.cd_pessoa = "+rsm.getInt("cd_professor")+" AND PO.st_pessoa_oferta = "+PessoaOfertaServices.ST_ATIVO+") AND O.st_oferta = " + OfertaServices.ST_ATIVO + " AND O.cd_disciplina = D.cd_disciplina AND D.cd_disciplina = E.cd_produto_servico").executeQuery());
				String nmDisciplina = "";
				while(rsmOfertas.next()){
					Turma turma = TurmaDAO.get(rsmOfertas.getInt("cd_turma"), connect);
					
					if(!registerQuantEscolas.contains(turma.getCdInstituicao())){
						qtEscolas++;
						registerQuantEscolas.add(turma.getCdInstituicao());
					}
					
					if(!registerQuantTurmas.contains(turma.getCdTurma())){
						qtTurmas++;
						qtAlunos += TurmaServices.getAlunos(turma.getCdTurma(), true, connect).size();
						registerQuantTurmas.add(turma.getCdTurma());
					}
					
					if(registerQuantDisciplinas.get(rsmOfertas.getString("NM_DISCIPLINA")) != null){
						registerQuantDisciplinas.put(rsmOfertas.getString("NM_DISCIPLINA"), (registerQuantDisciplinas.get(rsmOfertas.getString("NM_DISCIPLINA")) + 1));
					}
					else{
						registerQuantDisciplinas.put(rsmOfertas.getString("NM_DISCIPLINA"), 1);
					}
					
					if(!registerDisciplinas.contains(rsmOfertas.getString("NM_DISCIPLINA"))){
						registerDisciplinas.add(rsmOfertas.getString("NM_DISCIPLINA"));
						nmDisciplina += rsmOfertas.getString("NM_DISCIPLINA") + ", ";
					}
					
				}
				rsmOfertas.beforeFirst();
				
				if(nmDisciplina != null && !nmDisciplina.equals("")){
					nmDisciplina = nmDisciplina.substring(0, nmDisciplina.length()-2);
				}
				
				rsm.setValueToField("NM_DISCIPLINAS", (nmDisciplina != null && !nmDisciplina.equals("") ? nmDisciplina : "Nenhuma"));
				
				rsm.setValueToField("NR_ESCOLAS", qtEscolas);
				rsm.setValueToField("NR_TURMAS", qtTurmas);
				rsm.setValueToField("NR_ALUNOS", qtAlunos);
				
			}
			rsm.beforeFirst();
			
			String resultado = "";
			SortedSet<String> keys = new TreeSet<String>(registerQuantDisciplinas.keySet());
			for(String chave : keys){
				resultado += "Professores que atuam com " + chave + ": " + registerQuantDisciplinas.get(chave) + "\n";
			}
			
			resultado+="\n";
			
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", resultado);
			return result;			
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que retorna os cursos ministrados pelo professor, de acordo as ofertas
	 * @param cdInstituicao
	 * @param cdProfessor
	 * @return rsmCursos
	 */
	public static ResultSetMap getCursos(int cdProfessor, int cdInstituicao) {
		return getCursos(cdProfessor, cdInstituicao, null);
	}
	
	public static ResultSetMap getCursos(int cdProfessor, int cdInstituicao, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			ResultSetMap rsmPeriodoLetivo = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdSecretaria, connect);
			int cdPeriodoLetivoSecretaria = 0;
			if(rsmPeriodoLetivo.next())
				cdPeriodoLetivoSecretaria = rsmPeriodoLetivo.getInt("cd_periodo_letivo");
			
			// Busca das ofertas
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cdProfessor", Integer.toString(cdProfessor), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.ST_OFERTA", Integer.toString(OfertaServices.ST_ATIVO), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("B3.cd_periodo_letivo_superior", ""+cdPeriodoLetivoSecretaria, ItemComparator.EQUAL, Types.INTEGER));
			if(cdInstituicao > 0)
				criterios.add(new ItemComparator("G.cd_instituicao", ""+cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsmOfertas = OfertaServices.find(criterios);
			ResultSetMap rsmCursos = new ResultSetMap();
			
			boolean insert = true;
			
			if (rsmOfertas!=null) {
				while (rsmOfertas.next()) {
					
					if (rsmCursos!=null) {
						while (rsmCursos.next()) {
							
							if (rsmOfertas.getInt("CD_CURSO") == rsmCursos.getInt("CD_CURSO")) {
								insert = false;
								break;
							} else
								insert = true;
						}
						
						rsmCursos.beforeFirst();
					}
					
					if(insert) {
						rsmCursos.addRegister(rsmOfertas.getRegister());
					}
				}
			}
			
			return rsmCursos;
			
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap getTurmasDisciplinas(int cdProfessor, int cdInstituicao) {
		return getTurmasDisciplinas(cdProfessor, cdInstituicao, null);
	}
	
	/**
	 * Busca as turmas e disciplinas do professor em uma instituição
	 * @param cdProfessor
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getTurmasDisciplinas(int cdProfessor, int cdInstituicao, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			ResultSetMap rsmPeriodoLetivo = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdSecretaria, connect);
			int cdPeriodoLetivoSecretaria = 0;
			if(rsmPeriodoLetivo.next())
				cdPeriodoLetivoSecretaria = rsmPeriodoLetivo.getInt("cd_periodo_letivo");
			
			// Busca das ofertas
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_PROFESSOR", Integer.toString(cdProfessor), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.ST_OFERTA", Integer.toString(OfertaServices.ST_ATIVO), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("B3.cd_periodo_letivo_superior", ""+cdPeriodoLetivoSecretaria, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A3.cd_instituicao", ""+cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsmOfertas = OfertaServices.find(criterios);
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_CURSO");
			fields.add("NM_TURMA");
			fields.add("NM_DISCIPLINA");
			rsmOfertas.orderBy(fields);
			rsmOfertas.beforeFirst();
			
			ResultSetMap rsmTurmasDisciplinas = new ResultSetMap();
			
			ResultSetMap rsmDisciplinas = new ResultSetMap();
			
			String nmCursoTurma = "";
			int cdTurma = 0;
			
			while (rsmOfertas.next()) {
				
				if(nmCursoTurma.equals("") || nmCursoTurma.equals(rsmOfertas.getString("NM_CURSO") + " - " + rsmOfertas.getString("NM_TURMA"))){
					
					nmCursoTurma = rsmOfertas.getString("NM_CURSO") + " - " + rsmOfertas.getString("NM_TURMA");
					cdTurma = rsmOfertas.getInt("CD_TURMA");
					
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("CD_DISCIPLINA", rsmOfertas.getInt("CD_DISCIPLINA"));
					register.put("NM_DISCIPLINA", rsmOfertas.getString("NM_DISCIPLINA"));
					
					rsmDisciplinas.addRegister(register);
				}
				else{
					
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("CD_TURMA", cdTurma);
					register.put("NM_TURMA_COMPLETO", nmCursoTurma);
					register.put("RSM_DISCIPLINAS", rsmDisciplinas);
					
					rsmTurmasDisciplinas.addRegister(register);
					
					rsmDisciplinas = new ResultSetMap();
					nmCursoTurma = rsmOfertas.getString("NM_CURSO") + " - " + rsmOfertas.getString("NM_TURMA");
					cdTurma = rsmOfertas.getInt("CD_TURMA");
				}
				
			}
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("CD_TURMA", cdTurma);
			register.put("NM_TURMA_COMPLETO", nmCursoTurma);
			register.put("RSM_DISCIPLINAS", rsmDisciplinas);
			
			rsmTurmasDisciplinas.addRegister(register);
			
			rsmTurmasDisciplinas.beforeFirst();
				
			return rsmTurmasDisciplinas;
			
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result saveUsuario(Usuario usuario, int cdInstituicao) {
		return saveUsuario(usuario, cdInstituicao, null);
	}
	
	/**
	 * Salva o usuário do professor
	 * @param usuario
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static Result saveUsuario(Usuario usuario, int cdInstituicao, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		
		try {
			
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Result result = UsuarioServices.save(usuario, connect);
			if(result.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao salvar usuario do professor");
			}
			
			ResultSetMap rsmModulos = new ResultSetMap(connect.prepareStatement("SELECT * FROM seg_modulo WHERE id_modulo IN ('ped')").executeQuery());
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_empresa", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_usuario", "" + usuario.getCdUsuario(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmUsuarioEmpresa = UsuarioEmpresaDAO.find(criterios, connect);
			//Verifica se o professor já possui um registro na escola
			if(!rsmUsuarioEmpresa.next()){
				
				//Caso não possuir, esse registro é adicionado
				UsuarioEmpresa usuarioEmpresa = new UsuarioEmpresa(cdInstituicao, usuario.getCdUsuario(), 0, null, null);
				if(UsuarioEmpresaDAO.insert(usuarioEmpresa, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar usuário empresa");
				}
				
				//Passa pelo módulo pedagógico
				while(rsmModulos.next()){
					
					//Caso o professor não tenha registro no modulo pedagógico, é criado
					if(UsuarioModuloDAO.get(usuario.getCdUsuario(), rsmModulos.getInt("cd_modulo"), rsmModulos.getInt("cd_sistema"), connect) == null){
						UsuarioModulo usuarioModulo = new UsuarioModulo(usuario.getCdUsuario(), rsmModulos.getInt("cd_modulo"), rsmModulos.getInt("cd_sistema"), 1);
						if(UsuarioModuloDAO.insert(usuarioModulo, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao cadastrar usuário modulo");
						}
					}
					
					//Caso o professor não tenha registro na escola, é criado
					if(UsuarioModuloEmpresaDAO.get(usuario.getCdUsuario(), cdInstituicao, rsmModulos.getInt("cd_modulo"), rsmModulos.getInt("cd_sistema"), connect) == null){
						UsuarioModuloEmpresa usuarioModuloEmpresa = new UsuarioModuloEmpresa(usuario.getCdUsuario(), cdInstituicao, rsmModulos.getInt("cd_modulo"), rsmModulos.getInt("cd_sistema"));
						if(UsuarioModuloEmpresaDAO.insert(usuarioModuloEmpresa, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao cadastrar usuário modulo empresa");
						}
					}
					
					//É habilitado todas as ações para o professor
					ResultSetMap rsmAcoes = new ResultSetMap(connect.prepareStatement("SELECT * FROM seg_acao WHERE cd_modulo = " + rsmModulos.getString("cd_modulo")).executeQuery());
					while(rsmAcoes.next()){
						if(UsuarioPermissaoAcaoDAO.get(usuario.getCdUsuario(), rsmAcoes.getInt("cd_acao"), rsmModulos.getInt("cd_modulo"), rsmModulos.getInt("cd_sistema"), connect) == null){
							UsuarioPermissaoAcao usuarioPermissaoAcao = new UsuarioPermissaoAcao(usuario.getCdUsuario(), rsmAcoes.getInt("cd_acao"), rsmModulos.getInt("cd_modulo"), rsmModulos.getInt("cd_sistema"), 1);
							if(UsuarioPermissaoAcaoDAO.insert(usuarioPermissaoAcao, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao cadastrar usuário permissao acao");
							}
						}
					}
					
				}
				rsmModulos.beforeFirst();
			}
			
			if(isConnectionNull)
				connect.commit();
			
			result.setMessage("Usuário do professor salvo com sucesso");
			
			return result;
			
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao salvar usuário do professor");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getListaPendencias(int cdProfessor, int cdInstituicao) {
		return getListaPendencias(cdProfessor, cdInstituicao, null);
	}
	
	/**
	 * Busca a lista de pendencias do professor
	 * Pendencias possíveis:
	 * - Aulas não fechadas
	 * - Atividades não avaliadas
	 * @param cdProfessor
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getListaPendencias(int cdProfessor, int cdInstituicao, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		
		try {
			
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsmPeriodoLetivoVigente = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
			
			int cdPeriodoLetivo = 0;
			if(rsmPeriodoLetivoVigente.next()) {
				cdPeriodoLetivo = rsmPeriodoLetivoVigente.getInt("CD_PERIODO_LETIVO");
			}
			
			ResultSetMap rsmFinal = new ResultSetMap();
			
			ResultSetMap rsmAtividadesAtrasadas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_oferta_avaliacao A "
																	+ "	    JOIN acd_oferta B ON (A.cd_oferta = B.cd_oferta)"
																	+ "	    JOIN acd_turma C ON (B.cd_turma = C.cd_turma)"
																	+ "	    JOIN acd_curso D ON (C.cd_curso = D.cd_curso)"
																	+ "	    JOIN grl_produto_servico E ON (D.cd_curso = E.cd_produto_servico)"
																	+ "	    JOIN acd_curso_unidade F ON (A.cd_curso = F.cd_curso"
																	+ "										AND A.cd_unidade = F.cd_unidade)"
																	+ "	  WHERE B.cd_professor = " + cdProfessor  
																	+ "	    AND C.cd_instituicao = " + cdInstituicao
																	+ "		AND C.cd_periodo_letivo = " + cdPeriodoLetivo
																	+ "     AND A.st_oferta_avaliacao IN ("+OfertaAvaliacaoServices.ST_OFERTA_AVALIACAO_APLICADA+")").executeQuery());
			while(rsmAtividadesAtrasadas.next()){
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("DT_PENDENCIA", rsmAtividadesAtrasadas.getGregorianCalendar("dt_avaliacao"));
				register.put("DT_NM_PENDENCIA", Util.convCalendarString3(rsmAtividadesAtrasadas.getGregorianCalendar("dt_avaliacao")));
				register.put("CL_PENDENCIA", rsmAtividadesAtrasadas.getString("NM_AVALIACAO") + "("+rsmAtividadesAtrasadas.getString("NM_UNIDADE")+") precisa ser avaliada, da turma " + rsmAtividadesAtrasadas.getString("NM_PRODUTO_SERVICO") + " - " + rsmAtividadesAtrasadas.getString("NM_TURMA"));
				register.put("CL_TIPO_PENDENCIA", "ATIVIDADE");
				
				rsmFinal.addRegister(register);
			}
			rsmAtividadesAtrasadas.beforeFirst();
			
			
			ResultSetMap rsmAulasNaoFechadas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_aula A "
								+ "	    JOIN acd_oferta B ON (A.cd_oferta = B.cd_oferta)"
								+ "	    JOIN acd_turma C ON (B.cd_turma = C.cd_turma)"
								+ "	    JOIN acd_curso D ON (C.cd_curso = D.cd_curso)"
								+ "	    JOIN grl_produto_servico E ON (D.cd_curso = E.cd_produto_servico)"
								+ "	  WHERE B.cd_professor = " + cdProfessor  
								+ "	    AND C.cd_instituicao = " + cdInstituicao
								+ "		AND C.cd_periodo_letivo = " + cdPeriodoLetivo
								+ "     AND A.st_aula IN ("+AulaServices.ST_EM_ABERTO+")").executeQuery());
			
			int x = 0;
			
			while(rsmAulasNaoFechadas.next()){
				
				if(rsmAulasNaoFechadas.getGregorianCalendar("dt_aula").after(Util.getDataAtual())){
					rsmAulasNaoFechadas.deleteRow();
					if(x == 0)
						rsmAulasNaoFechadas.beforeFirst();
					else
						rsmAulasNaoFechadas.previous();
					continue;
				}
				
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("DT_PENDENCIA", rsmAulasNaoFechadas.getGregorianCalendar("dt_aula"));
				register.put("DT_NM_PENDENCIA", Util.convCalendarString3(rsmAulasNaoFechadas.getGregorianCalendar("dt_aula")));
				register.put("CL_PENDENCIA", "Aula não fechada, da turma " + rsmAulasNaoFechadas.getString("NM_PRODUTO_SERVICO") + " - " + rsmAulasNaoFechadas.getString("NM_TURMA"));
				register.put("CL_TIPO_PENDENCIA", "AULA");
				
				rsmFinal.addRegister(register);
				x++;
			}
			rsmAtividadesAtrasadas.beforeFirst();
			
			rsmFinal.beforeFirst();
			
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DT_PENDENCIA DESC");
			rsmFinal.orderBy(fields);
			rsmFinal.beforeFirst();
			
			if(isConnectionNull)
				connect.commit();
			
			return rsmFinal;
			
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllInstituicoesAtual(int cdProfessor, String nmPeriodoLetivo) {
		return getAllInstituicoesAtual(cdProfessor, nmPeriodoLetivo, null);
	}
	
	public static ResultSetMap getAllInstituicoesAtual(int cdProfessor, String nmPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao INS"
					+ "											  JOIN acd_instituicao_periodo PER ON (INS.cd_instituicao = PER.cd_instituicao)"
					+ "											  JOIN grl_pessoa INS_P ON (INS.cd_instituicao = INS_P.cd_pessoa)"
					+ "											WHERE PER.nm_periodo_letivo = '" + nmPeriodoLetivo + "'"
					+ "											  AND EXISTS (SELECT T.cd_turma FROM acd_turma T"
					+ "															JOIN acd_oferta O ON (T.cd_turma = O.cd_turma)"
					+ "														  WHERE T.cd_instituicao = INS.cd_instituicao "
					+ "															AND T.st_turma = "+TurmaServices.ST_ATIVO
					+ "															AND O.st_oferta = "+OfertaServices.ST_ATIVO
					+ "															AND O.cd_professor = " +cdProfessor+")");
			
			ResultSetMap rsmInstituicoes = new ResultSetMap(pstmt.executeQuery());
			return rsmInstituicoes;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	
	public static ValidatorResult validate(Professor professor, int idGrupo){
		return validate(professor, null, 0, 0, idGrupo, null);
	}
	
	public static ValidatorResult validate(Professor professor, int idGrupo, Connection connect){
		return validate(professor, null, 0, 0, idGrupo, connect);
	}
	
	public static ValidatorResult validate(Professor professor, PessoaEndereco endereco, int cdUsuario, int cdInstituicao, int idGrupo){
		return validate(professor, endereco, cdUsuario, cdInstituicao, idGrupo, null);
	}
	
	public static ValidatorResult validate(Professor professor, PessoaEndereco endereco, int cdUsuario, int cdInstituicao, int idGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(professor == null){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new ValidatorResult(ValidatorResult.ERROR, "Professor não encontrado");
			}
			
			ResultSetMap rsmOfertas = OfertaServices.getAllByPessoa(professor.getCdProfessor(), cdInstituicao, connect);
			ResultSetMap rsmDadosFuncionais = DadosFuncionaisServices.getByProfessor(professor.getCdProfessor(), cdInstituicao, connect);
			
			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			ResultSetMap rsmGestor = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_instituicao_educacenso A, acd_instituicao B "
					+ "																WHERE A.cd_instituicao = B.cd_instituicao "
					+ "																  AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE 
					+ " 															  AND A.cd_periodo_letivo = " + cdPeriodoLetivoAtual
					+ "																  AND B.cd_administrador = " + professor.getCdProfessor()).executeQuery());
			
			boolean isGestor = rsmGestor.next();
			
			ValidatorResult result = new ValidatorResult(ValidatorResult.VALID, "Aluno passou pela validação");
			HashMap<Integer, Validator> listValidator = getListValidation();
			
			//ENDERECO - Validações de Endereço
			validateEndereco(listValidator, endereco, connect);
			
			
			//NOME - Verificar se o professor possui nome
			if(professor.getNmPessoa() == null || professor.getNmPessoa().trim().equals("") || professor.getNmPessoa().trim().length() > 100){
				listValidator.get(VALIDATE_NOME).add(ValidatorResult.ERROR, "Faltando nome ou o mesmo é inválido");
			}
			
			//SEXO - Verificar se o valor do sexo do professor é válido
			if(professor.getTpSexo() < PessoaFisicaServices.TP_SEXO_MASCULINO || professor.getTpSexo() > PessoaFisicaServices.TP_SEXO_FEMININO){
				listValidator.get(VALIDATE_SEXO).add(ValidatorResult.ERROR, "Faltando sexo do professor");
			}
			
			//DATA DE NASCIMENTO - Verifica se o professor possui data de nascimento
			if(professor.getDtNascimento() == null){
				listValidator.get(VALIDATE_DATA_NASCIMENTO).add(ValidatorResult.ERROR, "Faltando data de nascimento");
			}
			
			//DATA DE NASCIMENTO - Verifica se a data de nascimento é muito antiga (pessoa com mais de 120 anos)
			GregorianCalendar dtMuitoAntiga = Util.getDataAtual();
			dtMuitoAntiga.add(Calendar.YEAR, -120);
			if(professor.getDtNascimento() != null && professor.getDtNascimento().before(dtMuitoAntiga)){
				listValidator.get(VALIDATE_DATA_NASCIMENTO_MUITO_ANTIGA).add(ValidatorResult.ERROR, "Data de nascimento muito antiga");
			}
			
			//NACIONALIDADE - Verifica se o valor de nacionalidade é válido
			if(professor.getTpNacionalidade() < PessoaFisicaServices.TP_NACIONALIDADE_BRASILEIRA || professor.getTpNacionalidade() > PessoaFisicaServices.TP_NACIONALIDADE_ESTRANGEIRA){
				listValidator.get(VALIDATE_NACIONALIDADE).add(ValidatorResult.ERROR, "O valor de nacionalidade está inválido");
			}
			
			//NATURALIDADE - Verifica se o professor possui naturalidade cadastrada
			if(professor.getCdNaturalidade() == 0 && professor.getTpNacionalidade() != PessoaFisicaServices.TP_NACIONALIDADE_ESTRANGEIRA){
				listValidator.get(VALIDATE_NATURALIDADE).add(ValidatorResult.ERROR, "Faltando naturalidade");
			}
			
			//RACA - Verifica se o valor de raça é válido
			if(professor.getTpRaca() < PessoaFisicaServices.TP_RACA_NAO_DECLARADA || professor.getTpRaca() > PessoaFisicaServices.TP_RACA_INDIGENA){
				listValidator.get(VALIDATE_RACA).add(ValidatorResult.ERROR, "Faltando raça do professor");
			}
			
			//NOME DA MAE - Verifica se existe o nome da mae
			if(professor.getNmMae() == null || professor.getNmMae().trim().equals("")){
				listValidator.get(VALIDATE_NOME_MAE).add(ValidatorResult.ERROR, "Faltando nome da mãe");
			}
			
			if(professor.getNmPai() != null){
				//NOME PAI - Verificar se o nome do pai contem sequencias inválidas
				if(professor.getNmPai().contains("XX")){
					listValidator.get(VALIDATE_NOME_PAI_INVALIDO).add(ValidatorResult.ERROR, "Nome do pai está inválido");
				}
								
				//NOME PAI - Verifica se não foi informada palavras que indicariam que o pai é não declarado
				if(professor.getNmPai().contains("DECLARADO") || professor.getNmPai().contains("INFORMADO")){
					listValidator.get(VALIDATE_NOME_PAI_NAO_DECLARADO).add(ValidatorResult.ERROR, "Para registrar que o pai do professor é Não declarado ou Não informado, utilize a seleção \"Não declarado/informado\" que está ao lado do nome do pai");
				}
				
			}
			
			//NOME PAI - Verifica se o nome do pai não é igual ao da mãe
			if(professor.getNmPai() != null && professor.getNmMae() != null){
				if(professor.getNmPai().trim().equals(professor.getNmMae().trim())){
					listValidator.get(VALIDATE_NOME_PAI_IGUAL_MAE).add(ValidatorResult.ERROR, "O nome do pai não pode ser igual ao nome da mãe");
				}
				
			}
			
			
			if(professor.getNrCpf() != null && !professor.getNrCpf().trim().equals("")){
				//CPF - Verifica se o CPF do professor é válido
				if((professor.getTpNacionalidade() == PessoaFisicaServices.TP_NACIONALIDADE_BRASILEIRA || professor.getTpNacionalidade() == PessoaFisicaServices.TP_NACIONALIDADE_BRASILEIRA_EXTERIOR) && !Util.isCpfValido(professor.getNrCpf())){
					listValidator.get(VALIDATE_CPF_VALIDO).add(ValidatorResult.ERROR, "CPF não é válido");
				}
				
				
				//CPF - Verifica se já existe alguém com esse CPF cadastrado no Banco
				if(Util.isCpfValido(professor.getNrCpf())){
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>(); 
					criterios.add(new ItemComparator("cd_pessoa", "" + professor.getCdPessoa(), ItemComparator.DIFFERENT, Types.INTEGER));
					criterios.add(new ItemComparator("nr_cpf", "" + professor.getNrCpf(), ItemComparator.EQUAL, Types.VARCHAR));
					ResultSetMap rsmPessoaCpf = PessoaFisicaDAO.find(criterios, connect);
					if(rsmPessoaCpf.next()){
						listValidator.get(VALIDATE_CPF_JA_CADASTRADO).add(ValidatorResult.ERROR, "Já existe alguém com esse CPF cadastrado");
					}
					
				}
			}
			
			//RG - Verifica a quantidade mínima de caracteres de um RG (cinco)
			if(professor.getNrRg() != null && professor.getNrRg().trim().length() < 5){
				listValidator.get(VALIDATE_RG_QUANT_MINIMA).add(ValidatorResult.ERROR, "Número do RG não pode conter menos de 5 dígitos");
			}
			
			
			//RG - Verifica se já existe alguem com mesmo número e estado de RG cadastrado no Banco
			ResultSetMap rsmRgProfessor = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_pessoa_fisica A WHERE A.cd_pessoa <> " + professor.getCdProfessor() + " AND A.nr_rg = '"+professor.getNrRg()+"' AND A.cd_estado_rg = " + professor.getCdEstadoRg()).executeQuery());
			if(rsmRgProfessor.next()){
				listValidator.get(VALIDATE_RG_JA_CADASTRADO).add(ValidatorResult.ERROR, "Já existe uma pessoa com o mesmo número de RG e mesmo estado cadastrado no sistema");
			}
				
			
			System.out.println("professor = " + professor);
			
			//NOME DO PROFESSOR
			String[] partesNome = professor.getNmPessoa().split(" ");
			for(String parteNome : partesNome){
				//NOME DO PROFESSOR - Verifica se existe espaços dentro do nome do professor
				if(parteNome.trim().equals("")){
					listValidator.get(VALIDATE_NOME_PROFESSOR_ESPACOS).add(ValidatorResult.ERROR, "Há muitos espaços dentro do nome do professor");
				}
				
				
				//NOME DO PROFESSOR - Verifica se existe abreviações dentro do nome do professor
				if(parteNome.length() <= 1 && !parteNome.trim().equals("E") && !parteNome.trim().equals("D")){
					listValidator.get(VALIDATE_NOME_PROFESSOR_ABREVIADO).add(ValidatorResult.ERROR, "Uma parte do nome do professor está abreviado");
				}
				
				
				//NOME DO PROFESSOR - Verifica se existe caracteres especiais no nome do professor
				if(parteNome.contains(",") || parteNome.contains(".") || parteNome.contains(";") || parteNome.contains("-") || parteNome.contains(":") || parteNome.contains("'") || parteNome.contains("*") || parteNome.contains("+") || parteNome.contains("/") || parteNome.contains("!") || parteNome.contains("@") || parteNome.contains("#") || parteNome.contains("$") || parteNome.contains("&")
				|| parteNome.contains("0") || parteNome.contains("1") || parteNome.contains("2") || parteNome.contains("3") || parteNome.contains("4") || parteNome.contains("5") || parteNome.contains("6") || parteNome.contains("7") || parteNome.contains("8") || parteNome.contains("9")){
					listValidator.get(VALIDATE_NOME_PROFESSOR_CARACTERES_ESPECIAIS).add(ValidatorResult.ERROR, "O nome do professor não pode conter carateres especiais ou números");
				}
				
			}
			//NOME DO PROFESSOR - Verifica se o nome do professor possui sobrenome
			if(partesNome.length <= 1){
				listValidator.get(VALIDATE_NOME_PROFESSOR_SEM_SOBRENOME).add(ValidatorResult.ERROR, "Nome do professor está sem sobrenome");
			}
			
			//NOME DA MAE
			if(professor.getNmMae() != null && !professor.getNmMae().equals("")){
				partesNome = professor.getNmMae().split(" ");
				for(String parteNome : partesNome){ 
					//NOME DA MAE - Verifica se existe espaços dentro do nome da mãe
					if(parteNome.trim().equals("")){
						listValidator.get(VALIDATE_NOME_MAE_ESPACOS).add(ValidatorResult.ERROR, "Há muitos espaços dentro do nome da mãe");
					}
					//NOME DA MAE - Verifica se existe abreviações dentro do nome da mãe
					if(parteNome.length() <= 1 && !parteNome.trim().equals("E") && !parteNome.trim().equals("D")){
						listValidator.get(VALIDATE_NOME_MAE_ABREVIADO).add(ValidatorResult.ERROR, "Uma parte do nome da mãe está abreviado");
					}
					//NOME DA MAE - Verifica se existe caracteres especiais no nome da mãe
					if(parteNome.contains(",") || parteNome.contains(".") || parteNome.contains(";") || parteNome.contains("-") || parteNome.contains(":") || parteNome.contains("'") || parteNome.contains("*") || parteNome.contains("+") || parteNome.contains("/") || parteNome.contains("!") || parteNome.contains("@") || parteNome.contains("#") || parteNome.contains("$") || parteNome.contains("&")
					|| parteNome.contains("0") || parteNome.contains("1") || parteNome.contains("2") || parteNome.contains("3") || parteNome.contains("4") || parteNome.contains("5") || parteNome.contains("6") || parteNome.contains("7") || parteNome.contains("8") || parteNome.contains("9")){
						listValidator.get(VALIDATE_NOME_MAE_CARACTERES_ESPECIAIS).add(ValidatorResult.ERROR, "O nome da mãe não pode conter carateres especiais ou números");
					}
				}
				//NOME DA MAE - Verifica se existe caracteres especiais no nome da mãe
				if(partesNome.length <= 1){
					listValidator.get(VALIDATE_NOME_MAE_SEM_SOBRENOME).add(ValidatorResult.ERROR, "Nome da mãe está sem sobrenome");
				}
			}
			
			if(professor.getNmPai() != null && !professor.getNmPai().equals("")){
				partesNome = professor.getNmPai().split(" ");
				for(String parteNome : partesNome){
					//NOME DA PAI - Verifica se existe espaços dentro do nome do pai
					if(parteNome.trim().equals("")){
						listValidator.get(VALIDATE_NOME_PAI_ESPACOS).add(ValidatorResult.ERROR, "Há muitos espaços dentro do nome do pai");
					}
					//NOME DA PAI - Verifica se existe abreviações dentro do nome do pai
					if(parteNome.length() <= 1 && !parteNome.trim().equals("E") && !parteNome.trim().equals("D")){
						listValidator.get(VALIDATE_NOME_PAI_ABREVIADO).add(ValidatorResult.ERROR, "Uma parte do nome do pai está abreviado");
					}
					//NOME DA PAI - Verifica se existe caracteres especiais no nome do pai
					if(parteNome.contains(",") || parteNome.contains(".") || parteNome.contains(";") || parteNome.contains("-") || parteNome.contains(":") || parteNome.contains("'") || parteNome.contains("*") || parteNome.contains("+") || parteNome.contains("/") || parteNome.contains("!") || parteNome.contains("@") || parteNome.contains("#") || parteNome.contains("$") || parteNome.contains("&")
					|| parteNome.contains("0") || parteNome.contains("1") || parteNome.contains("2") || parteNome.contains("3") || parteNome.contains("4") || parteNome.contains("5") || parteNome.contains("6") || parteNome.contains("7") || parteNome.contains("8") || parteNome.contains("9")){
						listValidator.get(VALIDATE_NOME_PAI_CARACTERES_ESPECIAIS).add(ValidatorResult.ERROR, "O nome do pai não pode conter carateres especiais ou números");
					}
				}
				//NOME DA PAI - Verifica se existe caracteres especiais no nome do pai
				if(partesNome.length <= 1){
					listValidator.get(VALIDATE_NOME_PAI_SEM_SOBRENOME).add(ValidatorResult.ERROR, "Nome do pai está sem sobrenome");
				}
			}
			
			//RG - Verifica se os dados de RG foram preenchidos
			if(professor.getNrRg() != null && (professor.getDtEmissaoRg() == null || professor.getCdEstadoRg() == 0 || professor.getSgOrgaoRg() == null)){
				listValidator.get(VALIDATE_RG_INFORMACOES).add(ValidatorResult.ERROR, "Ao se colocar um número de RG deve-se colocar data de emissão, orgão do RG e UF do RG", GRUPO_VALIDACAO_INSERT);
				listValidator.get(VALIDATE_RG_INFORMACOES).add(ValidatorResult.ERROR, "Ao se colocar um número de RG deve-se colocar data de emissão, orgão do RG e UF do RG", GRUPO_VALIDACAO_UPDATE);
			}
			
			//DATA NASCIMENTO - Verifica se a data de nascimento é anterior a data atual
			if(professor.getDtNascimento() != null && professor.getDtNascimento().after(Util.getDataAtual())){
				listValidator.get(VALIDATE_DATA_NASCIMENTO_POSTERIOR_ATUAL).add(ValidatorResult.ERROR, "A data de nascimento não pode ser posterior a data atual");
			}
			
			//RG - Verificar se a data de emissão do RG é posterior a data atual
			if(professor.getDtEmissaoRg() != null){
				if(professor.getDtEmissaoRg().after(Util.getDataAtual())){
					listValidator.get(VALIDATE_RG_DATA_POSTERIOR).add(ValidatorResult.ERROR, "A data de emissão de RG não pode ser posterior a data atual", GRUPO_VALIDACAO_INSERT);
					listValidator.get(VALIDATE_RG_DATA_POSTERIOR).add(ValidatorResult.ERROR, "A data de emissão de RG não pode ser posterior a data atual", GRUPO_VALIDACAO_UPDATE);
				}
			}
			
			//RG - Verificar se a data de emissão é igual a data de nascimento
			if(professor.getDtEmissaoRg() != null){
				
				GregorianCalendar dtEmissaoRg  = (GregorianCalendar)professor.getDtEmissaoRg().clone();
				dtEmissaoRg.set(Calendar.HOUR_OF_DAY, 0);
				dtEmissaoRg.set(Calendar.MINUTE, 0);
				dtEmissaoRg.set(Calendar.SECOND, 0);
				dtEmissaoRg.set(Calendar.MILLISECOND, 0);
				GregorianCalendar dtNascimento = (GregorianCalendar)professor.getDtNascimento().clone();
				dtNascimento.set(Calendar.HOUR_OF_DAY, 0);
				dtNascimento.set(Calendar.MINUTE, 0);
				dtNascimento.set(Calendar.SECOND, 0);
				dtNascimento.set(Calendar.MILLISECOND, 0);
				
				if(dtEmissaoRg.equals(dtNascimento)){
					listValidator.get(VALIDATE_RG_DATA_IGUAL_NASCIMENTO).add(ValidatorResult.ERROR, "A data de emissão de RG não pode ser igual a data de nascimento", GRUPO_VALIDACAO_INSERT);
					listValidator.get(VALIDATE_RG_DATA_IGUAL_NASCIMENTO).add(ValidatorResult.ERROR, "A data de emissão de RG não pode ser igual a data de nascimento", GRUPO_VALIDACAO_UPDATE);
				}
			}
			
			//PAIS DE ORIGEM - Verifica se o país de origem marcado corresponde ao tipo de nacionalidade indicada
			if(((professor.getTpNacionalidade() == PessoaFisicaServices.TP_NACIONALIDADE_BRASILEIRA) && professor.getCdPais() != 1/*BRASIL*/) || 
			   ((professor.getTpNacionalidade() == PessoaFisicaServices.TP_NACIONALIDADE_ESTRANGEIRA || professor.getTpNacionalidade() == PessoaFisicaServices.TP_NACIONALIDADE_BRASILEIRA_EXTERIOR) && professor.getCdPais() == 1/*BRASIL*/)){
				listValidator.get(VALIDATE_PAIS_DE_ORIGEM_NACIONALIDADE).add(ValidatorResult.ERROR, "País de Origem não corresponde à nacionalidade");
			}
			
			//NATURALIDADE BRASILEIRA - Verifica se a nacionalidade corr
			if(professor.getTpNacionalidade() == PessoaFisicaServices.TP_NACIONALIDADE_BRASILEIRA && professor.getCdNaturalidade() == 0){
				listValidator.get(VALIDATE_NACIONALIDADE_BRASILEIRA).add(ValidatorResult.ERROR, "Quando o professor é Brasileiro, ele deve ter sua naturalidade cadastrada");
			}
			
			//EMAIL - Verificar se o email do professor está inválido
			if(professor.getNmEmail() != null && !professor.getNmEmail().equals("")){
				if(!Util.isEmail(professor.getNmEmail())){
					listValidator.get(VALIDATE_EMAIL_INVALIDO).add(ValidatorResult.ERROR, "Email do professor está inválido");
				}
			}
			
			//CADASTRO DUPLICADO - Professor já possui cadastro no banco de dados
			if((professor.getNmPessoa() != null && !professor.getNmPessoa().equals("")) && professor.getDtNascimento() != null && (professor.getNmMae() != null && !professor.getNmMae().equals(""))){
				ResultSetMap rsmProfessor = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_pessoa A, acd_professor B, grl_pessoa_fisica C WHERE A.cd_pessoa = B.cd_professor AND A.cd_pessoa = C.cd_pessoa AND A.nm_pessoa = '"+professor.getNmPessoa()+"' AND C.dt_nascimento = '"+Util.convCalendarStringSql(professor.getDtNascimento())+"' AND C.nm_mae = '"+professor.getNmMae()+"' AND A.cd_pessoa <> '"+professor.getCdPessoa()+"'").executeQuery());
				if(rsmProfessor.next()){
					listValidator.get(VALIDATE_CADASTRO_DUPLICADO).add(ValidatorResult.ERROR, "O professor já possui cadastro no banco de dados do sistema");
				}
			}
			
			//RG - Data de emissão anterior a data de nascimento
			if(professor.getDtEmissaoRg() != null){
				if(professor.getDtEmissaoRg().before(professor.getDtNascimento())){
					listValidator.get(VALIDATE_RG_DATA_ANTERIOR).add(ValidatorResult.ERROR, "A data de emissão do RG não pode ser anterior a data de nascimento", GRUPO_VALIDACAO_INSERT);
					listValidator.get(VALIDATE_RG_DATA_ANTERIOR).add(ValidatorResult.ERROR, "A data de emissão do RG não pode ser anterior a data de nascimento", GRUPO_VALIDACAO_UPDATE);
				}
			}
			
			//RG - Verifica se o RG contém apenas zeros
			if(professor.getNrRg() != null && !professor.getNrRg().trim().equals("")){
				String nrRg[] = professor.getNrRg().split("");
				boolean contemApenasZero = true;
				for(int i = 0; i < nrRg.length; i++){
					if(!nrRg[i].equals("0"))
						contemApenasZero = false;
				}
				if(contemApenasZero){
					listValidator.get(VALIDATE_RG_CONTEM_APENAS_ZEROS).add(ValidatorResult.ERROR, "O RG não pode conter apenas zeros", GRUPO_VALIDACAO_INSERT);
					listValidator.get(VALIDATE_RG_CONTEM_APENAS_ZEROS).add(ValidatorResult.ERROR, "O RG não pode conter apenas zeros", GRUPO_VALIDACAO_UPDATE);
				}
			}
			
			//ESCOLARIDADE - Verifica se o professor possui escolaridade registrada
			
			Escolaridade escolaridade = EscolaridadeDAO.get(professor.getCdEscolaridade(), connect);
			
			if(escolaridade == null || professor.getCdEscolaridade() == 0){
				listValidator.get(VALIDATE_ESCOLARIDADE).add(ValidatorResult.ERROR, "Professor não possui escolaridade");
			}
			
			//OFERTA - Verifica se o professor está enturmado
			if(cdInstituicao > 0 && rsmOfertas.size() == 0 && !isGestor){
				listValidator.get(VALIDATE_OFERTAS).add(ValidatorResult.ERROR, "Professor não enturmado", GRUPO_VALIDACAO_EDUCACENSO);
			}
			
			while(rsmOfertas.next()){
				Curso curso = CursoDAO.get(rsmOfertas.getInt("cd_curso"), connect);
				Disciplina disciplina = DisciplinaDAO.get(rsmOfertas.getInt("cd_disciplina"), connect);
				
				if(curso != null && disciplina != null){
					CursoDisciplinaRegra cursoDisciplinaRegra = CursoDisciplinaRegraDAO.get(curso.getCdCurso(), disciplina.getCdDisciplina(), connect);
					//OFERTA - Verifica se a disciplina que o professor esta lecionando está nas regras do educacenso
					if(cursoDisciplinaRegra == null){
						listValidator.get(VALIDATE_DISCIPLINA_NAO_CADASTRADA).add(ValidatorResult.ERROR, "Professor com disciplina não cadastrada na base", GRUPO_VALIDACAO_EDUCACENSO);
					}
					//OFERTA - Verifica se a disciplina que o professor esta lecionando esta permitida para a turma que esta tomando ela
					else if(cursoDisciplinaRegra.getTpPermissao() == CursoDisciplinaRegraServices.TP_PROIBIDO){
						listValidator.get(VALIDATE_DISCIPLINA_NAO_PERMITIDA).add(ValidatorResult.ERROR, "Professor com disciplina não permitida para a turma", GRUPO_VALIDACAO_EDUCACENSO);
					}
				}
			}
			rsmOfertas.beforeFirst();
			
			ResultSetMap rsmCursos = FormacaoServices.getFormacaoByPessoa(professor.getCdProfessor(), true, connect);
			if(professor.getCdEscolaridade() != 0){
				escolaridade = EscolaridadeDAO.get(professor.getCdEscolaridade(), connect);
				if(escolaridade.getIdEscolaridade().equals(PessoaFisicaServices.TP_ESCOLARIDADE_ENS_SUPERIOR)){
					if(rsmCursos.size()==0){
						listValidator.get(VALIDATE_ENSINO_SUPERIOR_NAO_CADASTRADO).add(ValidatorResult.ERROR, "Professor cadastrado com Ensino Superior, porém não há curso superior cadastrado para o mesmo", GRUPO_VALIDACAO_EDUCACENSO);
					}
					else{
						while(rsmCursos.next()){
							Formacao formacao = FormacaoDAO.get(professor.getCdProfessor(), rsmCursos.getInt("cd_formacao"), connect);
							FormacaoCurso formacaoCurso = FormacaoCursoDAO.get(formacao.getCdFormacaoCurso(), connect);
							if(formacaoCurso.getCdFormacaoAreaConhecimento() > 0){
								if(formacao.getNmInstituicao() == null || formacao.getNmInstituicao().equals("")){
									listValidator.get(VALIDATE_CURSO_SUPERIOR_INSTITUICAO).add(ValidatorResult.ERROR, "Necessário lançar a instituição superior do curso", GRUPO_VALIDACAO_EDUCACENSO);
								}
								
								if((formacao.getStFormacao() == FormacaoServices.ST_EM_ANDAMENTO || formacao.getStFormacao() == FormacaoServices.ST_INCOMPLETO) && (formacao.getNrAnoInicio() == null || formacao.getNrAnoInicio().equals(""))){
									listValidator.get(VALIDATE_CURSO_SUPERIOR_ANO_INICIO).add(ValidatorResult.ERROR, "Nos cursos Em Andamento, é necessário informar o ano de início do curso", GRUPO_VALIDACAO_EDUCACENSO);
								}
								
								if((formacao.getNrAnoTermino() == null || formacao.getNrAnoTermino().equals("")) && formacao.getStFormacao() == FormacaoServices.ST_CONCLUIDO){
									listValidator.get(VALIDATE_CURSO_SUPERIOR_ANO_TERMINO).add(ValidatorResult.ERROR, "É necessário informar o ano de término do curso", GRUPO_VALIDACAO_EDUCACENSO);
								}
								else if(formacao.getNrAnoTermino() != null && !formacao.getNrAnoTermino().equals("") && Integer.parseInt(formacao.getNrAnoTermino()) > com.tivic.manager.util.Util.getDataAtual().get(Calendar.YEAR) && formacao.getStFormacao() == FormacaoServices.ST_CONCLUIDO){
									listValidator.get(VALIDATE_CURSO_SUPERIOR_ANO_TERMINO).add(ValidatorResult.ERROR, "O ano de término do curso nao pode ser posterior ao ano atual", GRUPO_VALIDACAO_EDUCACENSO);
								}
								
							}
						}
						rsmCursos.beforeFirst();
					}
				}
				else{
					if(rsmCursos.size() > 0){
						//listValidator.get(VALIDATE_ENSINO_SUPERIOR_CADASTRADO_INVALIDO).add(ValidatorResult.ERROR, "Cadastrado curso de instituto superior, porém a escolaridade não é de Ensino Superior", GRUPO_VALIDACAO_EDUCACENSO);
						//listValidator.get(VALIDATE_ENSINO_SUPERIOR_CADASTRADO_INVALIDO).add(ValidatorResult.ALERT, "Cadastrado curso de instituto superior, porém a escolaridade não é de Ensino Superior", ValidatorResult.STANDARD);
					}
				}
			}
			
			boolean formacaoConcluida = false;
			
			while(rsmCursos.next()){
				Formacao formacao = FormacaoDAO.get(professor.getCdProfessor(), rsmCursos.getInt("cd_formacao"), connect);
				
				if(formacao.getStFormacao() == FormacaoServices.ST_CONCLUIDO){
					formacaoConcluida = true;
				}
				
				ArrayList<ItemComparator> criterios =new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", "" + formacao.getCdPessoa(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_formacao_curso", "" + formacao.getCdFormacaoCurso(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_formacao", "" + formacao.getCdFormacao(), ItemComparator.DIFFERENT, Types.INTEGER));
				ResultSetMap rsmFormacao = FormacaoDAO.find(criterios, connect);
				if(rsmFormacao.next()){
					listValidator.get(VALIDATE_FORMACAO_DUPLICADA).add(ValidatorResult.ERROR, "Formação cadastrada mais de uma vez", GRUPO_VALIDACAO_EDUCACENSO);
					listValidator.get(VALIDATE_FORMACAO_DUPLICADA).add(ValidatorResult.ALERT, "Formação cadastrada mais de uma vez", ValidatorResult.STANDARD);
				}
			}
			rsmCursos.beforeFirst();
			
			escolaridade = EscolaridadeDAO.get(professor.getCdEscolaridade(), connect);
			 
			if(escolaridade != null && escolaridade.getIdEscolaridade().equals(EscolaridadeServices.ID_ENSINO_SUPERIOR) && !formacaoConcluida){
				listValidator.get(VALIDATE_SUPERIOR_SEM_FORMACAO_CONCLUIDA).add(ValidatorResult.ERROR, "Possui ensino superior, porém não foi cadastrado curso com formação concluída", GRUPO_VALIDACAO_EDUCACENSO);
			}
			
			//Dados Funcionais
			if(rsmDadosFuncionais.next()){
				DadosFuncionais dadosFuncionais = DadosFuncionaisDAO.get(rsmDadosFuncionais.getInt("cd_matricula"), connect);
				
				//Tipo de Admissão
				if(dadosFuncionais.getCdTipoAdmissao() == 0){
					listValidator.get(VALIDATE_DADOS_FUNCIONAIS_TIPO_ADMISSAO).add(ValidatorResult.ERROR, "Não cadastrado tipo de admissão", GRUPO_VALIDACAO_EDUCACENSO);
					listValidator.get(VALIDATE_DADOS_FUNCIONAIS_TIPO_ADMISSAO).add(ValidatorResult.ALERT, "Não cadastrado tipo de admissão", ValidatorResult.STANDARD);
				}
				
			}
			
			
			//HORARIOS - Verifica se o aluno possui horários conflitantes em suas matrículas
			ResultSetMap rsmTurmasProfessor = getTurmasOfertaByProfessor(professor.getCdProfessor(), connect);
			while(rsmTurmasProfessor.next()){
				Turma turma = TurmaDAO.get(rsmTurmasProfessor.getInt("cd_turma"), connect);
				
				Funcao funcao = null;
				
				ResultSetMap rsmLotacao = LotacaoServices.getAllByPessoa(professor.getCdProfessor(), turma.getCdInstituicao(), connect);
				while(rsmLotacao.next()){

					funcao = FuncaoDAO.get(rsmLotacao.getInt("cd_funcao"), connect);
				
				}
				
				if(funcao != null){
					if((turma.getTpAtendimento() == TurmaServices.TP_ATENDIMENTO_ATIVIDADE_COMPLEMENTAR && !funcao.getIdFuncao().equals(FuncaoServices.ID_MONITOR))
							|| (turma.getTpAtendimento() != TurmaServices.TP_ATENDIMENTO_ATIVIDADE_COMPLEMENTAR && funcao.getIdFuncao().equals(FuncaoServices.ID_MONITOR))){
						listValidator.get(VALIDATE_FUNCAO_ATENDIMENTO).add(ValidatorResult.ERROR, "A função do professor não condiz com o tipo de turma em que ele está enturmado", GRUPO_VALIDACAO_EDUCACENSO);
					}
				}
				
//				ResultSetMap rsmHorariosTurma = TurmaServices.getHorariosByTurma(turma.getCdTurma());
//				
//				ResultSetMap rsmTurmasProfessorComparacao = getTurmasOfertaByProfessor(professor.getCdProfessor(), connect);
//				while(rsmTurmasProfessorComparacao.next()){
//					boolean identificado = false;
//					
//					Turma turmaComparacao = TurmaDAO.get(rsmTurmasProfessorComparacao.getInt("cd_turma"), connect);
//					
//					if(turma.getCdTurma() == turmaComparacao.getCdTurma())
//						continue;
//					
//					ResultSetMap rsmHorariosTurmaComparacao = TurmaServices.getHorariosByTurma(turmaComparacao.getCdTurma());
//					while(rsmHorariosTurmaComparacao.next()){
//						while(rsmHorariosTurma.next()){
//							InstituicaoHorario instituicaoHorarioTurmaComparacao = InstituicaoHorarioDAO.get(rsmHorariosTurmaComparacao.getInt("cd_horario"), connect);
//							InstituicaoHorario instituicaoHorarioTurma			 = InstituicaoHorarioDAO.get(rsmHorariosTurma.getInt("cd_horario"), connect);
//							
//							if((instituicaoHorarioTurmaComparacao.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) && 
//								 instituicaoHorarioTurmaComparacao.getHrInicio().get(Calendar.MINUTE) == instituicaoHorarioTurma.getHrInicio().get(Calendar.MINUTE)) ||
//							   ((instituicaoHorarioTurmaComparacao.getHrInicio().get(Calendar.HOUR_OF_DAY) > instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) || 
//								(instituicaoHorarioTurmaComparacao.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) && 
//								 instituicaoHorarioTurmaComparacao.getHrInicio().get(Calendar.MINUTE) > instituicaoHorarioTurma.getHrInicio().get(Calendar.MINUTE))) && 
//								 instituicaoHorarioTurmaComparacao.getHrInicio().get(Calendar.HOUR_OF_DAY) < instituicaoHorarioTurma.getHrTermino().get(Calendar.HOUR_OF_DAY) || 
//								(instituicaoHorarioTurmaComparacao.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurma.getHrTermino().get(Calendar.HOUR_OF_DAY) && 
//								 instituicaoHorarioTurmaComparacao.getHrInicio().get(Calendar.MINUTE) < instituicaoHorarioTurma.getHrTermino().get(Calendar.MINUTE))) ||
//							   ((instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) > instituicaoHorarioTurmaComparacao.getHrInicio().get(Calendar.HOUR_OF_DAY) || 
//							    (instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurmaComparacao.getHrInicio().get(Calendar.HOUR_OF_DAY) && 
//							     instituicaoHorarioTurma.getHrInicio().get(Calendar.MINUTE) > instituicaoHorarioTurmaComparacao.getHrInicio().get(Calendar.MINUTE))) && 
//							     instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) < instituicaoHorarioTurmaComparacao.getHrTermino().get(Calendar.HOUR_OF_DAY) || 
//							    (instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurmaComparacao.getHrTermino().get(Calendar.HOUR_OF_DAY) && 
//							     instituicaoHorarioTurma.getHrInicio().get(Calendar.MINUTE) < instituicaoHorarioTurmaComparacao.getHrTermino().get(Calendar.MINUTE))) &&
//							     instituicaoHorarioTurmaComparacao.getNrDiaSemana() == instituicaoHorarioTurma.getNrDiaSemana()){
//								
//								listValidator.get(VALIDATE_HORARIOS_CONFLITANTES).add(ValidatorResult.ERROR, "Este professor possui aulas em horários conflitantes");
//							}
//						}
//						rsmHorariosTurma.beforeFirst();
//						if(identificado){
//							break;
//						}
//					}
//				}
			}
			
			System.out.println();
			
			//Cria a mensagem de erro em um objeto de retorno chamado 'RESULT'
			result.addListValidator(listValidator);
			result.verify(idGrupo);
			
			//RETORNO
			return result;
		
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoServices.validate: " + e);
			return new ValidatorResult(ValidatorResult.ERROR, "Erro em InstituicaoEducacensoServices.validate");
		}
		
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static void validateEndereco(HashMap<Integer, Validator> listValidator, PessoaEndereco endereco, Connection connect){
		boolean isConnectionNull = connect==null;
		
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//ENDERECO - Verifica se existe um endereço
			if(endereco == null){
				listValidator.get(VALIDATE_ENDERECO).add(ValidatorResult.ERROR, "Não possui endereço cadastrado");
				return;
			}
			
			//CEP - Verificar se existe um CEP cadastrado e se o mesmo é válido
			ResultSetMap rsmLogradouro = LogradouroServices.getEnderecoByCep(endereco.getNrCep(), connect);
			if(endereco.getNrCep() == null || endereco.getNrCep().trim().equals("") || endereco.getNrCep().length() != 8 || endereco.getNrCep().equals("45000000") || rsmLogradouro == null  || rsmLogradouro.size() == 0){
				listValidator.get(VALIDATE_ENDERECO_CEP).add(ValidatorResult.ERROR, "O endereço não possui CEP, ou o mesmo é inválido");
			}
			
			
			//LOGRADOURO - Verificar se existe logradouro cadastrado
			if(endereco != null && (endereco.getNmLogradouro() == null || endereco.getNmLogradouro().trim().equals("") || endereco.getNmLogradouro().length() > 100)){
				listValidator.get(VALIDATE_ENDERECO_LOGRADOURO).add(ValidatorResult.ERROR, "O endereço não possui logradouro");
			}
			
			
			//NUMERO - Verificar se existe número de endereço cadastrado
			if(endereco != null && (endereco.getNrEndereco() == null || endereco.getNrEndereco().trim().equals("") || endereco.getNrEndereco().length() > 10)){
				listValidator.get(VALIDATE_ENDERECO_NUMERO).add(ValidatorResult.ERROR, "O endereço não possui número");
			}
			
			
			//BAIRRO - Verificar se existe bairro cadastrado
			if(endereco != null && (endereco.getNmBairro() == null || endereco.getNmBairro().trim().equals("") || endereco.getNmBairro().length() > 50)){
				listValidator.get(VALIDATE_ENDERECO_BAIRRO).add(ValidatorResult.ERROR, "O endereço não possui bairro");
			}
			
			//CIDADE - Verificar se existe cidade cadastrada
			if(endereco != null && (endereco.getCdCidade() == 0)){
				listValidator.get(VALIDATE_ENDERECO_CIDADE).add(ValidatorResult.ERROR, "O endereço não possui cidade");
			}
			
			if(rsmLogradouro != null && rsmLogradouro.next()){
				if(rsmLogradouro.getInt("cd_cidade") != endereco.getCdCidade()){
					listValidator.get(VALIDATE_ENDERECO_CEP_CIDADE).add(ValidatorResult.ERROR, "A cidade do endereço não corresponde ao cep cadastrado");
				}
			}
			
			
			
			//ZONA - Verificar se existe zona cadastrada
			if(endereco != null && (endereco.getTpZona() == 0)){
				listValidator.get(VALIDATE_ENDERECO_ZONA).add(ValidatorResult.ERROR, "O endereço não possui zona");
			}
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoServices.validateEndereco: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static HashMap<Integer, Validator> getListValidation(){
		HashMap<Integer, Validator> list = new HashMap<Integer, Validator>();
		
		list.put(VALIDATE_NOME, new Validator(VALIDATE_NOME, ValidatorResult.VALID));
		list.put(VALIDATE_SEXO, new Validator(VALIDATE_SEXO, ValidatorResult.VALID));
		list.put(VALIDATE_DATA_NASCIMENTO, new Validator(VALIDATE_DATA_NASCIMENTO, ValidatorResult.VALID));
		list.put(VALIDATE_DATA_NASCIMENTO_MUITO_ANTIGA, new Validator(VALIDATE_DATA_NASCIMENTO_MUITO_ANTIGA, ValidatorResult.VALID));
		list.put(VALIDATE_NACIONALIDADE, new Validator(VALIDATE_NACIONALIDADE, ValidatorResult.VALID));
		list.put(VALIDATE_NATURALIDADE, new Validator(VALIDATE_NATURALIDADE, ValidatorResult.VALID));
		list.put(VALIDATE_RACA, new Validator(VALIDATE_RACA, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_MAE, new Validator(VALIDATE_NOME_MAE, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_PAI_INVALIDO, new Validator(VALIDATE_NOME_PAI_INVALIDO, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_PAI_NAO_DECLARADO, new Validator(VALIDATE_NOME_PAI_NAO_DECLARADO, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_PAI_IGUAL_MAE, new Validator(VALIDATE_NOME_PAI_IGUAL_MAE, ValidatorResult.VALID));
		list.put(VALIDATE_CPF_VALIDO, new Validator(VALIDATE_CPF_VALIDO, ValidatorResult.VALID));
		list.put(VALIDATE_CPF_JA_CADASTRADO, new Validator(VALIDATE_CPF_JA_CADASTRADO, ValidatorResult.VALID));
		list.put(VALIDATE_RG_QUANT_MINIMA, new Validator(VALIDATE_RG_QUANT_MINIMA, ValidatorResult.VALID));
		list.put(VALIDATE_RG_JA_CADASTRADO, new Validator(VALIDATE_RG_JA_CADASTRADO, ValidatorResult.VALID));
		list.put(VALIDATE_ENDERECO, new Validator(VALIDATE_ENDERECO, ValidatorResult.VALID));
		list.put(VALIDATE_ENDERECO_CEP, new Validator(VALIDATE_ENDERECO_CEP, ValidatorResult.VALID));
		list.put(VALIDATE_ENDERECO_LOGRADOURO, new Validator(VALIDATE_ENDERECO_LOGRADOURO, ValidatorResult.VALID));
		list.put(VALIDATE_ENDERECO_NUMERO, new Validator(VALIDATE_ENDERECO_NUMERO, ValidatorResult.VALID));
		list.put(VALIDATE_ENDERECO_BAIRRO, new Validator(VALIDATE_ENDERECO_BAIRRO, ValidatorResult.VALID));
		list.put(VALIDATE_ENDERECO_CIDADE, new Validator(VALIDATE_ENDERECO_CIDADE, ValidatorResult.VALID));
		list.put(VALIDATE_ENDERECO_ZONA, new Validator(VALIDATE_ENDERECO_ZONA, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_PROFESSOR_ESPACOS, new Validator(VALIDATE_NOME_PROFESSOR_ESPACOS, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_PROFESSOR_ABREVIADO, new Validator(VALIDATE_NOME_PROFESSOR_ABREVIADO, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_PROFESSOR_CARACTERES_ESPECIAIS, new Validator(VALIDATE_NOME_PROFESSOR_CARACTERES_ESPECIAIS, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_PROFESSOR_SEM_SOBRENOME, new Validator(VALIDATE_NOME_PROFESSOR_SEM_SOBRENOME, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_MAE_ESPACOS, new Validator(VALIDATE_NOME_MAE_ESPACOS, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_MAE_ABREVIADO, new Validator(VALIDATE_NOME_MAE_ABREVIADO, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_MAE_CARACTERES_ESPECIAIS, new Validator(VALIDATE_NOME_MAE_CARACTERES_ESPECIAIS, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_MAE_SEM_SOBRENOME, new Validator(VALIDATE_NOME_MAE_SEM_SOBRENOME, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_PAI_ESPACOS, new Validator(VALIDATE_NOME_PAI_ESPACOS, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_PAI_ABREVIADO, new Validator(VALIDATE_NOME_PAI_ABREVIADO, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_PAI_CARACTERES_ESPECIAIS, new Validator(VALIDATE_NOME_PAI_CARACTERES_ESPECIAIS, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_PAI_SEM_SOBRENOME, new Validator(VALIDATE_NOME_PAI_SEM_SOBRENOME, ValidatorResult.VALID));
		list.put(VALIDATE_RG_INFORMACOES, new Validator(VALIDATE_RG_INFORMACOES, ValidatorResult.VALID));
		list.put(VALIDATE_DATA_NASCIMENTO_POSTERIOR_ATUAL, new Validator(VALIDATE_DATA_NASCIMENTO_POSTERIOR_ATUAL, ValidatorResult.VALID));
		list.put(VALIDATE_RG_DATA_POSTERIOR, new Validator(VALIDATE_RG_DATA_POSTERIOR, ValidatorResult.VALID));
		list.put(VALIDATE_RG_DATA_IGUAL_NASCIMENTO, new Validator(VALIDATE_RG_DATA_IGUAL_NASCIMENTO, ValidatorResult.VALID));
		list.put(VALIDATE_PAIS_DE_ORIGEM_NACIONALIDADE, new Validator(VALIDATE_PAIS_DE_ORIGEM_NACIONALIDADE, ValidatorResult.VALID));
		list.put(VALIDATE_NACIONALIDADE_BRASILEIRA, new Validator(VALIDATE_NACIONALIDADE_BRASILEIRA, ValidatorResult.VALID));
		list.put(VALIDATE_EMAIL_INVALIDO, new Validator(VALIDATE_EMAIL_INVALIDO, ValidatorResult.VALID));
		list.put(VALIDATE_CADASTRO_DUPLICADO, new Validator(VALIDATE_CADASTRO_DUPLICADO, ValidatorResult.VALID));
		list.put(VALIDATE_RG_DATA_ANTERIOR, new Validator(VALIDATE_RG_DATA_ANTERIOR, ValidatorResult.VALID));
		list.put(VALIDATE_RG_CONTEM_APENAS_ZEROS, new Validator(VALIDATE_RG_CONTEM_APENAS_ZEROS, ValidatorResult.VALID));
		list.put(VALIDATE_ESCOLARIDADE, new Validator(VALIDATE_ESCOLARIDADE, ValidatorResult.VALID));
		list.put(VALIDATE_OFERTAS, new Validator(VALIDATE_OFERTAS, ValidatorResult.VALID));
		list.put(VALIDATE_DISCIPLINA_NAO_CADASTRADA, new Validator(VALIDATE_DISCIPLINA_NAO_CADASTRADA, ValidatorResult.VALID));
		list.put(VALIDATE_DISCIPLINA_NAO_PERMITIDA, new Validator(VALIDATE_DISCIPLINA_NAO_PERMITIDA, ValidatorResult.VALID));
		list.put(VALIDATE_ENSINO_SUPERIOR_NAO_CADASTRADO, new Validator(VALIDATE_ENSINO_SUPERIOR_NAO_CADASTRADO, ValidatorResult.VALID));
		list.put(VALIDATE_CURSO_SUPERIOR_INSTITUICAO, new Validator(VALIDATE_CURSO_SUPERIOR_INSTITUICAO, ValidatorResult.VALID));
		list.put(VALIDATE_CURSO_SUPERIOR_ANO_INICIO, new Validator(VALIDATE_CURSO_SUPERIOR_ANO_INICIO, ValidatorResult.VALID));
		list.put(VALIDATE_CURSO_SUPERIOR_ANO_TERMINO, new Validator(VALIDATE_CURSO_SUPERIOR_ANO_TERMINO, ValidatorResult.VALID));
		list.put(VALIDATE_FORMACAO_DUPLICADA, new Validator(VALIDATE_FORMACAO_DUPLICADA, ValidatorResult.VALID));
		list.put(VALIDATE_ENSINO_SUPERIOR_CADASTRADO_INVALIDO, new Validator(VALIDATE_ENSINO_SUPERIOR_CADASTRADO_INVALIDO, ValidatorResult.VALID));
		list.put(VALIDATE_DADOS_FUNCIONAIS_TIPO_ADMISSAO, new Validator(VALIDATE_DADOS_FUNCIONAIS_TIPO_ADMISSAO, ValidatorResult.VALID));
		list.put(VALIDATE_HORARIOS_CONFLITANTES, new Validator(VALIDATE_HORARIOS_CONFLITANTES, ValidatorResult.VALID));
		list.put(VALIDATE_FUNCAO_ATENDIMENTO, new Validator(VALIDATE_FUNCAO_ATENDIMENTO, ValidatorResult.VALID));
		list.put(VALIDATE_SUPERIOR_SEM_FORMACAO_CONCLUIDA, new Validator(VALIDATE_SUPERIOR_SEM_FORMACAO_CONCLUIDA, ValidatorResult.VALID));
		list.put(VALIDATE_ENDERECO_CEP_CIDADE, new Validator(VALIDATE_ENDERECO_CEP_CIDADE, ValidatorResult.VALID));
		
		
		
		return list;
	}
	
	
	public static ArrayList<HashMap<String, Object>> sincronizacaoInicial(int cdProfessor, Connection connect) {
		boolean isConnectionNull = connect==null;		
		try {		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			ArrayList<HashMap<String, Object>> response = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> professores = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> ofertas = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> cursoUnidade = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> ofertasAvaliacao = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> disciplinaAvaliacao = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> tiposAvaliacao = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> aulas = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> atividadesDesenvolvidas = new ArrayList<HashMap<String, Object>>();
			
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			ResultSetMap rsmPeriodoLetivo = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdSecretaria, connect);
			int cdPeriodoLetivoSecretaria = 0;
			if(rsmPeriodoLetivo.next())
				cdPeriodoLetivoSecretaria = rsmPeriodoLetivo.getInt("cd_periodo_letivo");
			InstituicaoPeriodo instituicaoPeriodoSecretaria = InstituicaoPeriodoDAO.get(cdPeriodoLetivoSecretaria, connect);
			
			
			ResultSetMap rsmInstituicoes = getAllInstituicoesAtual(cdProfessor, instituicaoPeriodoSecretaria.getNmPeriodoLetivo(), connect);
			
			ArrayList<HashMap<String, Object>> instituicoes = new ArrayList<HashMap<String, Object>>();
			instituicoes.addAll(rsmInstituicoes.getLines());

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CD_PROFESSOR", String.valueOf(cdProfessor), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmProfessor = ProfessorServices.find(criterios, connect);
			professores.addAll(rsmProfessor.getLines());
			
			ResultSetMap rsmOfertaAvaliacao = OfertaAvaliacaoServices.getAll(connect);
			ofertasAvaliacao.addAll(rsmOfertaAvaliacao.getLines());
			
			for(HashMap<String, Object> oA : ofertasAvaliacao) {	
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.CD_OFERTA_AVALIACAO", String.valueOf(oA.get("CD_OFERTA_AVALIACAO")), ItemComparator.IN, Types.INTEGER));
				criterios.add(new ItemComparator("A.CD_OFERTA", String.valueOf(oA.get("CD_OFERTA")), ItemComparator.IN, Types.INTEGER));
				ResultSetMap rsm = DisciplinaAvaliacaoAlunoServices.find(criterios, connect);
				disciplinaAvaliacao.addAll(rsm.getLines());
			}

			ResultSetMap rsmTipoAvaliacao = TipoAvaliacaoServices.getAll(connect);
			tiposAvaliacao.addAll(rsmTipoAvaliacao.getLines());

			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cdProfessor", String.valueOf(cdProfessor), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("B3.nm_periodo_letivo", instituicaoPeriodoSecretaria.getNmPeriodoLetivo(), ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmOfertas = OfertaServices.find(criterios, connect);
			ofertas.addAll(rsmOfertas.getLines());
			
			for(HashMap<String, Object> oferta : ofertas) {
				int cdOferta = (int) oferta.get("CD_OFERTA");
				ResultSetMap rsmAulas = AulaServices.getAllByOferta(cdOferta, connect);
				aulas.addAll(rsmAulas.getLines());
			}
			
			
			while(rsmInstituicoes.next()) {				
				ArrayList<HashMap<String, Object>> alunos = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> turmas = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> pessoas = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> pessoasEnderecos = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> matriculas = new ArrayList<HashMap<String, Object>>();	
				ArrayList<HashMap<String, Object>> disciplinas = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> aulaMatriculas = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> produtoServico = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> instituicoesCursos = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> cursos = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> cursosEtapas = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> matriculaUnidade = new ArrayList<HashMap<String, Object>>();
				
				int cdPeriodoLetivo = 0;
				
				int cdInstituicao = rsmInstituicoes.getInt("CD_INSTITUICAO");
				rsmPeriodoLetivo = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
				while(rsmPeriodoLetivo.next()) {
					if(rsmPeriodoLetivo.getInt("CD_INSTITUICAO") == cdInstituicao) {
						cdPeriodoLetivo = rsmPeriodoLetivo.getInt("CD_PERIODO_LETIVO");
						break;
					}
				}

				ResultSetMap rsmInstituicaoCurso = InstituicaoServices.getCursosByInstituicao(cdInstituicao, connect);
				instituicoesCursos.addAll(rsmInstituicaoCurso.getLines());

				while(rsmInstituicaoCurso.next()) {
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("A.CD_CURSO", "" + rsmInstituicaoCurso.getString("cd_curso"), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmCurso = CursoServices.find(criterios, connect);
					cursos.addAll(rsmCurso.getLines());
				}
				rsmInstituicaoCurso.beforeFirst();
				
				ResultSetMap rsmCursosTurma = OfertaServices.findCursosTurmasByProfessor(cdProfessor, cdInstituicao, cdPeriodoLetivo, connect);			
				
				cursos = rsmCursosTurma.getLines();
				for(HashMap<String, Object> r : cursos) {	
					r.remove("RSM_TURMAS");
				}
				
				for(HashMap<String, Object> curso : cursos) {
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("CD_CURSO", "" + curso.get("CD_CURSO"), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmCursosUnidade = CursoUnidadeServices.find(criterios, connect);
					cursoUnidade.addAll(rsmCursosUnidade.getLines());
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("A.CD_CURSO", "" + curso.get("CD_CURSO"), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmCursosEtapa = CursoEtapaServices.find(criterios, connect);
					cursosEtapas.addAll(rsmCursosEtapa.getLines());
					
				}

				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.ID_PRODUTO_SERVICO", "0", ItemComparator.DIFFERENT, Types.VARCHAR));
				ResultSetMap rsmProdutoServico = ProdutoServicoServices.find(criterios, connect);
				produtoServico.addAll(rsmProdutoServico.getLines());

				ResultSetMap rsmTurmas = OfertaServices.findTurmasByProfessor(cdProfessor, cdInstituicao, cdPeriodoLetivo, connect);
				turmas = rsmTurmas.getLines();

				ResultSetMap rsmDisciplinas = OfertaServices.findDisciplinasCursosTurmasByProfessor(cdProfessor, cdInstituicao, cdPeriodoLetivo, connect);
				disciplinas = rsmDisciplinas.getLines();
				String strInDisciplinas = "";


				if(strInDisciplinas.length() > 0) {
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("A.CD_PRODUTO_SERVICO", strInDisciplinas.substring(0, strInDisciplinas.length()-2), ItemComparator.IN, Types.INTEGER));
					ResultSetMap rsmProdutoServicoDisciplina = ProdutoServicoServices.find(criterios, connect);
					produtoServico.addAll(rsmProdutoServicoDisciplina.getLines());
				}

				for(HashMap<String, Object> aula : aulas) {
					ResultSetMap rsmAulaMatricula = AulaMatriculaServices.getAllByAula((int) aula.get("CD_AULA"), connect);		    	
					aulaMatriculas.addAll(rsmAulaMatricula.getLines());
				}
				
//				for(HashMap<String, Object> aulaMatricula : aulaMatriculas) {
//					criterios = new ArrayList<ItemComparator>();
//					criterios.add(new ItemComparator("A.CD_MATRICULA", String.valueOf((int)aulaMatricula.get("CD_MATRICULA")), ItemComparator.EQUAL, Types.INTEGER));
//					ResultSetMap rsmMatricula = MatriculaServices.findSimplificado(criterios, connect);
//					matriculas.addAll(rsmMatricula.getLines());
//				}

				for(HashMap<String, Object> turma : turmas) {
					criterios = new ArrayList<ItemComparator>();
					ResultSetMap rsmAlunos = TurmaServices.getAlunos((int) turma.get("CD_TURMA"), connect);
					alunos.addAll(rsmAlunos.getLines());
					matriculas.addAll(rsmAlunos.getLines());
				}

				for(HashMap<String, Object> matricula : matriculas) {
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("CD_MATRICULA", String.valueOf((int) matricula.get("CD_MATRICULA")), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmMatriculasUnidade = MatriculaUnidadeServices.find(criterios, connect);
					matriculaUnidade.addAll(rsmMatriculasUnidade.getLines());
				}
				
				for(HashMap<String, Object> aluno : alunos) {
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("A.CD_PESSOA", String.valueOf((int) aluno.get("CD_PESSOA")), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmPessoa = PessoaServices.find(criterios, connect);		    	
					pessoas.addAll(rsmPessoa.getLines());
					
					ResultSetMap rsmPessoaEndereco = PessoaEnderecoServices.find(criterios, connect);
					pessoasEnderecos.addAll(rsmPessoaEndereco.getLines());
				}
				
				ResultSetMap rsmAtividadesDesenvolvidas = AtividadeDesenvolvidaServices.getAllByProfessorInstituicao(cdProfessor, cdInstituicao, connect);
				while(rsmAtividadesDesenvolvidas.next()) {
					atividadesDesenvolvidas.addAll(rsmAtividadesDesenvolvidas.getLines());
				}

				HashMap<String, Object> instituicao = new HashMap<String, Object>();
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("grl_pessoa", pessoas);
				data.put("grl_pessoa_endereco", pessoasEnderecos);
				data.put("acd_aluno", alunos);
				data.put("acd_curso", cursos);
				data.put("acd_curso_etapa", cursosEtapas);
				data.put("acd_instituicao_curso", instituicoesCursos);
				data.put("acd_turma", turmas);
				data.put("acd_disciplina", disciplinas);
				data.put("acd_aula_matricula", aulaMatriculas);
				data.put("acd_matricula", matriculas);
				data.put("acd_matricula_unidade", matriculaUnidade);
				data.put("acd_atividade_desenvolvida", atividadesDesenvolvidas);

				if(rsmInstituicoes.getPointer() == 0) {
					data.put("acd_aula", aulas);
					data.put("acd_professor", professores);
					data.put("grl_produto_servico", produtoServico);
					data.put("acd_oferta", ofertas);
					data.put("acd_oferta_avaliacao", ofertasAvaliacao);
					data.put("acd_curso_unidade", cursoUnidade);
					data.put("acd_tipo_avaliacao", tiposAvaliacao);
					data.put("acd_disciplina_avaliacao_aluno", disciplinaAvaliacao);
				}

				instituicao.put("instituicao", rsmInstituicoes.getRegister());
				instituicao.put("periodoLetivo", cdPeriodoLetivo);
				instituicao.put("data", data);

				response.add(instituicao);
			}
			
			return response;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.sincronizacaoInicial: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result sincronizacaoFinal(RestData restData, Connection connect) {
		boolean isConnectionNull = connect==null;		
		try {		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			ArrayList<HashMap<String, Object>> aulas = (ArrayList)restData.getArg("aulas");		
			ArrayList<HashMap<String, Object>> aulaMatriculas = (ArrayList)restData.getArg("aulaMatriculas");
			ArrayList<HashMap<String, Object>> ofertaAvaliacoes = (ArrayList)restData.getArg("ofertaAvaliacoes");
			ArrayList<HashMap<String, Object>> disciplinaAvaliacaoAlunos = (ArrayList)restData.getArg("disciplinaAvaliacaoAlunos");
			
			
			
			
			
			
			return new Result(1);
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.sincronizacaoFinal: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
}
