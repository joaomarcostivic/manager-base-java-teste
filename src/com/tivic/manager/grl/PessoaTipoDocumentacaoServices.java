package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.acd.Aluno;
import com.tivic.manager.acd.AlunoDAO;
import com.tivic.manager.acd.InstituicaoPendenciaServices;
import com.tivic.manager.acd.Professor;
import com.tivic.manager.acd.ProfessorDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.log.LogServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.ComplianceManager;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.Validator;
import com.tivic.manager.util.ValidatorResult;

public class PessoaTipoDocumentacaoServices {

	public static final int TP_MODELO_NENHUM = 0;
	public static final int TP_MODELO_ANTIGO = 1;
	public static final int TP_MODELO_NOVO   = 2;
	
	public static final int CD_TIPO_DOCUMENTACAO_NIS = 1;
	public static final int CD_TIPO_DOCUMENTACAO_PASSAPORTE = 3;
	public static final int CD_TIPO_DOCUMENTACAO_NASCIMENTO = 4;
	public static final int CD_TIPO_DOCUMENTACAO_CASAMENTO  = 5;
	public static final int CD_TIPO_DOCUMENTACAO_SUS        = 7;
	
	//Grupos de Validação
	public static final int GRUPO_VALIDACAO_INSERT = 0;
	public static final int GRUPO_VALIDACAO_UPDATE = 1;
	public static final int GRUPO_VALIDACAO_EDUCACENSO = 2;
	//Validações
	public static final int VALIDATE_MODELO = 0;
	public static final int VALIDATE_TIPO_REGISTRO = 1;
	public static final int VALIDATE_CARTORIO = 2;
	public static final int VALIDATE_MAX_NUMERO_REGISTRO_NOVO = 3;
	public static final int VALIDATE_MAX_NUMERO_REGISTRO_ANTIGO = 4;
	public static final int VALIDATE_DIGITO_VALIDADOR = 5;
	public static final int VALIDATE_REGISTRO_DUPLICADO = 6;
	public static final int VALIDATE_DATA_EMISSAO_VALIDA = 7;
	public static final int VALIDATE_DATA_EMISSAO_ANO_MENOR_NASCIMENTO = 8;
	public static final int VALIDATE_DATA_EMISSAO_ANO_MAIOR_ATUAL = 9;
	public static final int VALIDATE_DATA_EMISSAO_DIFERENTE_REGISTRO = 10;
	public static final int VALIDATE_SERVENTIA = 11;
	public static final int VALIDATE_CARTORIO_SERVENTIA = 12;
	public static final int VALIDATE_RG_DATA_EMISSAO_ANTERIOR_NASCIMENTO = 13;
	public static final int VALIDATE_RG_QUANTIDADE_MINIMA_DIGITOS = 14;
	public static final int VALIDATE_RG_CONTEM_APENAS_ZERO = 15;
	public static final int VALIDATE_REGISTRO_ANTIGO_SEM_FOLHA = 16;
	public static final int VALIDATE_REGISTRO_ANTIGO_SEM_LIVRO = 17;
	public static final int VALIDATE_DATA_EMISSAO_MENOR_NASCIMENTO = 18;
	public static final int VALIDATE_DATA_EMISSAO_MAIOR_ATUAL = 19;
	public static final int VALIDATE_QUANT_CPF = 20;
	public static final int VALIDATE_QUANT_NIS = 21;
	public static final int VALIDATE_NIS_INVALIDO = 22;
	public static final int VALIDATE_NIS_DUPLICADO = 23;
	public static final int VALIDATE_SUS_DUPLICADO = 24;
	
	public static Result save(PessoaTipoDocumentacao pessoaTipoDocumentacao){
		return save(pessoaTipoDocumentacao, null, null);
	}

	public static Result save(PessoaTipoDocumentacao pessoaTipoDocumentacao, Connection connect){
		return save(pessoaTipoDocumentacao, null, connect);
	}
	
	public static Result save(PessoaTipoDocumentacao pessoaTipoDocumentacao, AuthData authData){
		return save(pessoaTipoDocumentacao, authData, null);
	}

	public static Result save(PessoaTipoDocumentacao pessoaTipoDocumentacao, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			//COMPLIANCE
			int tpAcao = ComplianceManager.TP_ACAO_ANY;
			
			//LOG
			String idAcao = "";
			PessoaTipoDocumentacao oldValue = null;
			
			if(pessoaTipoDocumentacao==null)
				return new Result(-1, "Erro ao salvar. PessoaTipoDocumentacao é nulo");
			
			if(pessoaTipoDocumentacao.getNrDocumento().equals("null") || pessoaTipoDocumentacao.getNrDocumento().equals("")){
				return new Result(-1, "Erro ao salvar. Número de documento não pode ser 'null' ou vazio");
			}
			
			PessoaFisica pessoa = PessoaFisicaDAO.get(pessoaTipoDocumentacao.getCdPessoa(), connect);
			
			//Correção de horário prevendo que horários de verão possam modificar a exibição
			if(pessoaTipoDocumentacao.getDtEmissao() != null){
				GregorianCalendar dtEmissao = pessoaTipoDocumentacao.getDtEmissao();
				
				GregorianCalendar dtEmissaoNovo = new GregorianCalendar();
				dtEmissaoNovo.set(Calendar.DAY_OF_MONTH, dtEmissao.get(Calendar.DAY_OF_MONTH));
				dtEmissaoNovo.set(Calendar.MONTH, dtEmissao.get(Calendar.MONTH));
				dtEmissaoNovo.set(Calendar.YEAR, dtEmissao.get(Calendar.YEAR));
				dtEmissaoNovo.set(Calendar.HOUR_OF_DAY, 12);
				dtEmissaoNovo.set(Calendar.MINUTE, 0);
				dtEmissaoNovo.set(Calendar.SECOND, 0);
				dtEmissaoNovo.set(Calendar.MILLISECOND, 0);
				
				pessoaTipoDocumentacao.setDtEmissao(dtEmissaoNovo);
				
			}
			
			if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_RG, connect).getCdTipoDocumentacao())){
				
				pessoa.setNrRg(pessoaTipoDocumentacao.getNrDocumento());
				pessoa.setDtEmissaoRg(pessoaTipoDocumentacao.getDtEmissao());
				if(PessoaFisicaDAO.update(pessoa, connect) <= 0){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar pessoa");
				}
				
			}
			
			if(pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) ||
			   pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)){
				int ret = connect.prepareStatement("DELETE FROM grl_pessoa_tipo_documentacao WHERE cd_pessoa = " + pessoaTipoDocumentacao.getCdPessoa() + " AND cd_tipo_documentacao IN ("+ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)+")").executeUpdate();
				if(ret < 0){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao excluir certidão anterior do aluno");
				}
			}
			
			//Correção de horário prevendo que horários de verão possam modificar a exibição
			if(pessoaTipoDocumentacao.getDtEmissao() != null){
				GregorianCalendar dtEmissao = pessoaTipoDocumentacao.getDtEmissao();
				dtEmissao.set(Calendar.HOUR, 12);
				pessoaTipoDocumentacao.setDtEmissao(dtEmissao);
			}
			
			int retorno;
			if(PessoaTipoDocumentacaoDAO.get(pessoaTipoDocumentacao.getCdPessoa(), pessoaTipoDocumentacao.getCdTipoDocumentacao(), connect)==null){
				ValidatorResult resultadoValidacao = validate(pessoaTipoDocumentacao, GRUPO_VALIDACAO_INSERT, connect);
				if(!resultadoValidacao.hasPassed()){
					return resultadoValidacao;
				}
				
				//Compliance
				tpAcao = ComplianceManager.TP_ACAO_INSERT;
				
				//Log
				if(authData!=null)
					idAcao = authData.getIdAcaoInsert();
				
				retorno = PessoaTipoDocumentacaoDAO.insert(pessoaTipoDocumentacao, connect);
				pessoaTipoDocumentacao.setCdPessoa(retorno);
			}
			else {
				ValidatorResult resultadoValidacao = validate(pessoaTipoDocumentacao, GRUPO_VALIDACAO_UPDATE, connect);
				if(!resultadoValidacao.hasPassed()){
					return resultadoValidacao;
				}
				
				//Compliance e Log
				tpAcao = ComplianceManager.TP_ACAO_UPDATE;
				
				//Log
				if(authData!=null)
					idAcao = authData.getIdAcaoUpdate();
				oldValue = PessoaTipoDocumentacaoDAO.get(pessoaTipoDocumentacao.getCdPessoa(), pessoaTipoDocumentacao.getCdTipoDocumentacao(), connect);
				
				retorno = PessoaTipoDocumentacaoDAO.update(pessoaTipoDocumentacao, connect);
			}

			if(retorno < 0){
				if(retorno==-666) {
					//LOG
					LogServices.log(LogServices.ANY, idAcao, authData, pessoaTipoDocumentacao, oldValue, 
							"CHANGE_PROCESSO_EXCEPTION\n"
							+ "A mudança de processo não é permitida.\n"
							+ "De:\n"+LogServices.formatValues(oldValue)+"\nPara:\n"+LogServices.formatValues(pessoaTipoDocumentacao));
					return new Result(retorno, "A mudança de processo não é permitida.");
				}
				else 
					return new Result(-2, "Erro ao salvar pessoa tipo documentacao.");
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			//COMPLIANCE
			Result result = ComplianceManager.process("saveCompliance", pessoaTipoDocumentacao, authData, tpAcao, connect);
			//LOG
			result = LogServices.log(tpAcao, idAcao, authData, pessoaTipoDocumentacao, oldValue);
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PESSOATIPODOCUMENTACAO", pessoaTipoDocumentacao);
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
	public static Result remove(int cdPessoa, int cdTipoDocumentacao){
		return remove(cdPessoa, cdTipoDocumentacao, false, null);
	}
	public static Result remove(int cdPessoa, int cdTipoDocumentacao, boolean cascade){
		return remove(cdPessoa, cdTipoDocumentacao, cascade, null);
	}
	public static Result remove(int cdPessoa, int cdTipoDocumentacao, boolean cascade, Connection connect){
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
			
			if(!cascade || retorno>0){
				retorno = PessoaTipoDocumentacaoDAO.delete(cdPessoa, cdTipoDocumentacao, connect);
			}
			
			if((cdTipoDocumentacao == TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_RG, connect).getCdTipoDocumentacao())){
				PessoaFisica pessoaFisica = PessoaFisicaDAO.get(cdPessoa, connect);
				pessoaFisica.setNrRg(null);
				pessoaFisica.setDtEmissaoRg(null);
				pessoaFisica.setSgOrgaoRg(null);
				pessoaFisica.setCdEstadoRg(0);
				if(PessoaFisicaDAO.update(pessoaFisica, connect) <= 0){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar pessoa");
				}
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull){
				connect.commit();
			}
			
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_tipo_documentacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaTipoDocumentacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaTipoDocumentacaoServices.getAll: " + e);
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
		return Search.find("SELECT A.*, B.* FROM grl_pessoa_tipo_documentacao A, grl_tipo_documentacao B WHERE A.cd_tipo_documentacao = B.cd_tipo_documentacao ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static Result validacaoDocumentacao(int cdPessoa, int cdInstituicao, int cdUsuario){
		return validacaoDocumentacao(cdPessoa, cdInstituicao, cdUsuario, null);
	}
	
	public static Result validacaoDocumentacao(int cdPessoa, int cdInstituicao, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_pessoa", "" + cdPessoa, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmDocumentacao = find(criterios, connect);
			while(rsmDocumentacao.next()){
				
				PessoaTipoDocumentacao pessoaTipoDocumentacao = PessoaTipoDocumentacaoDAO.get(cdPessoa, rsmDocumentacao.getInt("cd_tipo_documentacao"), connect);
				ValidatorResult resultadoValidacao = validate(pessoaTipoDocumentacao, GRUPO_VALIDACAO_EDUCACENSO, connect);
				
				int cdAluno = 0;
				int cdProfessor = 0;
				
				Aluno aluno = AlunoDAO.get(pessoaTipoDocumentacao.getCdPessoa(), connect);
				if(aluno != null){
					cdAluno = aluno.getCdAluno();
				}
				else{
					Professor professor = ProfessorDAO.get(pessoaTipoDocumentacao.getCdPessoa(), connect);
					if(professor != null){
						cdProfessor = professor.getCdProfessor();
					}
				}
				
				Result resultValidacoesPendencia = InstituicaoPendenciaServices.atualizarValidacaoPendencia(resultadoValidacao, cdInstituicao, 0/*cdTurma*/, cdAluno, cdProfessor, InstituicaoPendenciaServices.TP_REGISTRO_DOCUMENTACAO_CENSO, cdUsuario, GRUPO_VALIDACAO_EDUCACENSO, connect);
								
				if(resultValidacoesPendencia.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return resultValidacoesPendencia;
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
			return new Result(-1, "Erro em PessoaTipoDocumentacaoServices.validacaoDocumentacao");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ValidatorResult validate(PessoaTipoDocumentacao pessoaTipoDocumentacao, int idGrupo){
		return validate(pessoaTipoDocumentacao, idGrupo, null);
	}
	
	public static ValidatorResult validate(PessoaTipoDocumentacao pessoaTipoDocumentacao, int idGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(pessoaTipoDocumentacao == null){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new ValidatorResult(ValidatorResult.ERROR, "Pessoa Tipo Documentação não encontrado");
			}
			
			ValidatorResult result = new ValidatorResult(ValidatorResult.VALID, "Pessoa Tipo Documentação passou pela validação");
			HashMap<Integer, Validator> listValidator = new HashMap<>();
			
			if(pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
					pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)){
				listValidator = getListValidationRegistro();
			}
			else if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_RG, connect).getCdTipoDocumentacao())){
				listValidator = getListValidationRg();
			}
			else if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_CPF, connect).getCdTipoDocumentacao())){
				listValidator = getListValidationCpf();
			}
			else if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_NIS, connect).getCdTipoDocumentacao())){
				listValidator = getListValidationNis();
			}
			else if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_SUS, connect).getCdTipoDocumentacao())){
				listValidator = getListValidationSus();
			}
			
			
			Aluno aluno = AlunoDAO.get(pessoaTipoDocumentacao.getCdPessoa(), connect);
			if(aluno != null && aluno.getLgFaltaDocumento() == 1){
				return result;
			}
			
			if(pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
					pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)){
			
				//MODELO - Verifica se não existe modelo registrado, caso a documentação for de casamento ou nascimento
				if(pessoaTipoDocumentacao.getTpModelo() == 0 && (pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
				   pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0))){
					listValidator.get(VALIDATE_MODELO).add(ValidatorResult.ERROR, "Não selecionado o modelo do registro civil");
				}
				
				//TIPO DOCUMENTACAO - Verifica se não existe tipo de documentação caso for de modelo novo ou antigo
				if(pessoaTipoDocumentacao.getCdTipoDocumentacao() == 0 && (pessoaTipoDocumentacao.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_NOVO || pessoaTipoDocumentacao.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_ANTIGO)){
					listValidator.get(VALIDATE_TIPO_REGISTRO).add(ValidatorResult.ERROR, "Não selecionado o tipo de registro civil", GRUPO_VALIDACAO_INSERT);
					listValidator.get(VALIDATE_TIPO_REGISTRO).add(ValidatorResult.ERROR, "Não selecionado o tipo de registro civil", GRUPO_VALIDACAO_UPDATE);
				}
				
				if(pessoaTipoDocumentacao.getNrDocumento().length() == 32 && (pessoaTipoDocumentacao.getNrDocumento().split("")[0].equals("") ? !pessoaTipoDocumentacao.getNrDocumento().split("")[15].equals("1") : !pessoaTipoDocumentacao.getNrDocumento().split("")[14].equals("1")) && (pessoaTipoDocumentacao.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_NOVO)){
					listValidator.get(VALIDATE_TIPO_REGISTRO).add(ValidatorResult.ERROR, "O 15º dígito, que identifica o tipo de registro, não está marcado como registro de nascimento", GRUPO_VALIDACAO_EDUCACENSO);
				}
				
				//CARTORIO - Verifica se o registro civil do modeo antigo possui cartorio
				if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
					pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)) && 
					pessoaTipoDocumentacao.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_ANTIGO){
	
					if(pessoaTipoDocumentacao.getCdCartorio() == 0){
						listValidator.get(VALIDATE_CARTORIO).add(ValidatorResult.ERROR, "Selecionado o modelo antigo para certidão, deve-se colocar o cartório");
					}
					
				}
				
				//REGISTRO - Verifica a quantidade de digitos no numero de registro novo
				
				if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
					pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)) && 
					pessoaTipoDocumentacao.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_NOVO &&
					pessoaTipoDocumentacao.getNrDocumento() != null &&
					pessoaTipoDocumentacao.getNrDocumento().trim().length() != 32){
					listValidator.get(VALIDATE_MAX_NUMERO_REGISTRO_NOVO).add(ValidatorResult.ERROR, "O registro novo deve conter exatamente 32 dígitos");
				}
				
				//REGISTRO - Verifica a quantidade de digitos no numero de registro antigo
				if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
					pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)) && 
					pessoaTipoDocumentacao.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_ANTIGO && 
					pessoaTipoDocumentacao.getNrDocumento() != null &&
					pessoaTipoDocumentacao.getNrDocumento().trim().length() > 8){
					listValidator.get(VALIDATE_MAX_NUMERO_REGISTRO_ANTIGO).add(ValidatorResult.ERROR, "O registro antigo deve conter no máximo 8 dígitos");
				}
				
				//REGISTRO - Verifica se a folha de certidao do modelo antigo foi registrada
				if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
					pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)) && 
					pessoaTipoDocumentacao.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_ANTIGO && (pessoaTipoDocumentacao.getFolha() == null || pessoaTipoDocumentacao.getFolha().trim().equals(""))){
					listValidator.get(VALIDATE_REGISTRO_ANTIGO_SEM_FOLHA).add(ValidatorResult.ERROR, "O registro antigo deve ter o registro da folha");
				}
				
				//REGISTRO - Verifica se o livro de certidao do modelo antigo foi registrada
				if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
					pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)) && 
					pessoaTipoDocumentacao.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_ANTIGO && (pessoaTipoDocumentacao.getLivro() == null || pessoaTipoDocumentacao.getLivro().trim().equals(""))){
					listValidator.get(VALIDATE_REGISTRO_ANTIGO_SEM_LIVRO).add(ValidatorResult.ERROR, "O registro antigo deve ter o registro do livro");
				}
				
				//DIGITO VALIDADOR - Verifica se o dígito validador do registro tipo novo é válido
				if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
						pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)) &&
						pessoaTipoDocumentacao.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_NOVO &&
						pessoaTipoDocumentacao.getNrDocumento() != null &&
						pessoaTipoDocumentacao.getNrDocumento().trim().length() == 32){
					String nrDv = Util.getMod11(pessoaTipoDocumentacao.getNrDocumento().substring(0, 30));
					if(!nrDv.equals(pessoaTipoDocumentacao.getNrDocumento().substring(30, 32))){
						listValidator.get(VALIDATE_DIGITO_VALIDADOR).add(ValidatorResult.ERROR, "O dígito verificador do registro novo não é válido");
					}
				}
				
				//REGISTRO DUPLICADO - Verifica se já existe alguém com o mesmo número de registro novo
				if(pessoaTipoDocumentacao.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_NOVO){
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_pessoa", "" + pessoaTipoDocumentacao.getCdPessoa(), ItemComparator.DIFFERENT, Types.INTEGER));
					criterios.add(new ItemComparator("nr_documento", pessoaTipoDocumentacao.getNrDocumento(), ItemComparator.EQUAL, Types.VARCHAR));
					criterios.add(new ItemComparator("cd_tipo_documentacao", "" + pessoaTipoDocumentacao.getCdTipoDocumentacao(), ItemComparator.EQUAL, Types.INTEGER));
					if(PessoaTipoDocumentacaoDAO.find(criterios, connect).next()){
						listValidator.get(VALIDATE_REGISTRO_DUPLICADO).add(ValidatorResult.ERROR, "Uma pessoa com esse número de documento (registro no modelo novo) já existe no banco de dados");
					}
				}
				
				//REGISTRO DUPLICADO - Verifica se já existe alguém com o mesmo número, folha, livro e data de emissão no registro antigo
				else if(pessoaTipoDocumentacao.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_ANTIGO){
					
					GregorianCalendar dtAnterior = null;
					GregorianCalendar dtPosterior = null;
					
					if(pessoaTipoDocumentacao.getDtEmissao() != null){
						dtAnterior = (GregorianCalendar)pessoaTipoDocumentacao.getDtEmissao().clone();
						dtAnterior.add(Calendar.HOUR, 0);
						dtAnterior.add(Calendar.MINUTE, 0);
						dtAnterior.add(Calendar.SECOND, 0);
						
						dtPosterior = (GregorianCalendar)pessoaTipoDocumentacao.getDtEmissao().clone();
						dtPosterior.add(Calendar.HOUR, 23);
						dtPosterior.add(Calendar.MINUTE, 59);
						dtPosterior.add(Calendar.SECOND, 59);
					}
					
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_pessoa", "" + pessoaTipoDocumentacao.getCdPessoa(), ItemComparator.DIFFERENT, Types.INTEGER));
					criterios.add(new ItemComparator("nr_documento", pessoaTipoDocumentacao.getNrDocumento(), ItemComparator.EQUAL, Types.VARCHAR));
					criterios.add(new ItemComparator("folha", pessoaTipoDocumentacao.getFolha(), ItemComparator.EQUAL, Types.VARCHAR));
					criterios.add(new ItemComparator("livro", pessoaTipoDocumentacao.getLivro(), ItemComparator.EQUAL, Types.VARCHAR));
					if(pessoaTipoDocumentacao.getDtEmissao() != null){
						criterios.add(new ItemComparator("dt_emissao", Util.convCalendarStringSqlCompleto(dtAnterior), ItemComparator.GREATER_EQUAL, Types.VARCHAR));
						criterios.add(new ItemComparator("dt_emissao", Util.convCalendarStringSqlCompleto(dtPosterior), ItemComparator.MINOR_EQUAL, Types.VARCHAR));
					}
					criterios.add(new ItemComparator("cd_cartorio", "" + pessoaTipoDocumentacao.getCdCartorio(), ItemComparator.EQUAL, Types.VARCHAR));
					criterios.add(new ItemComparator("cd_tipo_documentacao", "" + pessoaTipoDocumentacao.getCdTipoDocumentacao(), ItemComparator.EQUAL, Types.INTEGER));
					if(PessoaTipoDocumentacaoDAO.find(criterios, connect).next()){
						listValidator.get(VALIDATE_REGISTRO_DUPLICADO).add(ValidatorResult.ERROR, "Uma pessoa com esse número de documento (registro no modelo antigo) já existe no banco de dados");
					}
				}
				
				if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
						pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)) &&
						pessoaTipoDocumentacao.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_NOVO && pessoaTipoDocumentacao.getNrDocumento() != null && pessoaTipoDocumentacao.getNrDocumento().length() > 15){
					String nrAnoEmissaoParte[] = pessoaTipoDocumentacao.getNrDocumento().substring(10, 14).split("");
					boolean todosDigitos = true;
					String nrAnoEmissaoStr = "";
					for(String nrAnoEmissaoParteNome : nrAnoEmissaoParte){
						nrAnoEmissaoStr += nrAnoEmissaoParteNome;
						if(!Util.isNumber(nrAnoEmissaoParteNome)){
							todosDigitos = false;
							break;
						}
					}
					//DATA EMISSAO - Verifica se a data de emissão é válida
					if(!todosDigitos){
						listValidator.get(VALIDATE_DATA_EMISSAO_VALIDA).add(ValidatorResult.ERROR, "O ano da data de emissão do registro está fora do padrão");
					}
					
					PessoaFisica pessoa = PessoaFisicaDAO.get(pessoaTipoDocumentacao.getCdPessoa(), connect);
					
					//DATA EMISSAO - Verifica se a data de emissão é menor que a data de nascimento
					if(pessoa.getDtNascimento() != null && pessoa.getDtNascimento().get(Calendar.YEAR) > Integer.parseInt(nrAnoEmissaoStr)){
						listValidator.get(VALIDATE_DATA_EMISSAO_ANO_MENOR_NASCIMENTO).add(ValidatorResult.ERROR, "O ano da data de registro está menor do que o ano da data de nascimento");
					}
					
					//DATA EMISSAO - Verifica se a data de emissão é maior do que a atual
					if(Util.getDataAtual().get(Calendar.YEAR) < Integer.parseInt(nrAnoEmissaoStr)){
						listValidator.get(VALIDATE_DATA_EMISSAO_ANO_MAIOR_ATUAL).add(ValidatorResult.ERROR, "O ano da data de registro está maior do que o ano da data atual");
					}
					
					if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0) || 
							pessoaTipoDocumentacao.getCdTipoDocumentacao() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)) &&
							pessoaTipoDocumentacao.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_NOVO){
						
						String idCartorio = pessoaTipoDocumentacao.getNrDocumento().substring(0, 6);
						
						String sql = "SELECT A.cd_pessoa AS cd_cartorio " +
								     "FROM grl_pessoa A " +
									 "JOIN grl_pessoa_juridica D ON (A.cd_pessoa = D.cd_pessoa) " +
							      	 " WHERE A.cd_pessoa = D.cd_pessoa AND D.id_serventia = '"+idCartorio+"'";
						ResultSetMap rsmCartorio = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
						if(rsmCartorio.next()){
							int cdCartorio = rsmCartorio.getInt("cd_cartorio");
							
							//CARTORIO SERVENTIA - Verifica se o cartorio informado é o mesmo da serventia
							if(pessoaTipoDocumentacao.getCdCartorio() != cdCartorio){
								listValidator.get(VALIDATE_SERVENTIA).add(ValidatorResult.ERROR, "A serventia do número de registro não condiz com o cartório cadastrado");
							}
						}
						else{
							//CARTORIO SERVENTIA - Verifica se existe cartorio com a serventia informada no número de registro
							listValidator.get(VALIDATE_CARTORIO_SERVENTIA).add(ValidatorResult.ERROR, "Não foi encontrado cartório com a serventia informada");
						}
						
					}
					
				}
				
				PessoaFisica pessoa = PessoaFisicaDAO.get(pessoaTipoDocumentacao.getCdPessoa(), connect);
				
				//DATA EMISSAO - Verifica se a data de emissão é menor que a data de nascimento
				if(pessoaTipoDocumentacao.getDtEmissao() != null){
					if(pessoa.getDtNascimento() != null && pessoa.getDtNascimento().after(pessoaTipoDocumentacao.getDtEmissao())){
						listValidator.get(VALIDATE_DATA_EMISSAO_MENOR_NASCIMENTO).add(ValidatorResult.ERROR, "A data de registro está menor do que o ano da data de nascimento");
					}
					
					//DATA EMISSAO - Verifica se a data de emissão é maior do que a atual
					if(Util.getDataAtual().before(pessoaTipoDocumentacao.getDtEmissao())){
						listValidator.get(VALIDATE_DATA_EMISSAO_MAIOR_ATUAL).add(ValidatorResult.ERROR, "A data de registro está maior do que o ano da data atual");
					}
				}
				
				//DATA EMISSAO - Verifica se a data de emissão é diferente da do número de registro
				if(pessoaTipoDocumentacao.getNrDocumento() != null && pessoaTipoDocumentacao.getNrDocumento().length() > 15){
					int nrAnoEmissaoRegistro = Integer.parseInt(pessoaTipoDocumentacao.getNrDocumento().substring(10, 14));
					if(pessoaTipoDocumentacao.getDtEmissao() != null){
						int nrAnoEmissao = pessoaTipoDocumentacao.getDtEmissao().get(Calendar.YEAR);
						if(nrAnoEmissaoRegistro > nrAnoEmissao){
							listValidator.get(VALIDATE_DATA_EMISSAO_DIFERENTE_REGISTRO).add(ValidatorResult.ERROR, "A data de emissão está menor do que a data que consta no registro");
						}
					}
				}
				
				
	//			if(pessoaTipoDocumentacao.getDtEmissao() != null && pessoa.getDtNascimento() != null){
	//				if(pessoaTipoDocumentacao.getDtEmissao().before(pessoa.getDtNascimento())){
	//					if (isConnectionNull)
	//						Conexao.rollback(connect);
	//					return new Result(-1, ". Por favor verifique");
	//				}
	//			}
	//			if(pessoaTipoDocumentacao.getDtEmissao() != null){
	//				if(pessoaTipoDocumentacao.getDtEmissao().after(Util.getDataAtual())){
	//					if (isConnectionNull)
	//						Conexao.rollback(connect);
	//					return new Result(-1, "A data de emissão do documento não pode ser posterior a data atual. Por favor verifique");
	//				}
	//			}
				
				if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_RG, connect).getCdTipoDocumentacao())){
					if(pessoa.getDtNascimento() != null && pessoaTipoDocumentacao.getDtEmissao() != null){
						if(pessoaTipoDocumentacao.getDtEmissao().before(pessoa.getDtNascimento())){
							listValidator.get(VALIDATE_RG_DATA_EMISSAO_ANTERIOR_NASCIMENTO).add(ValidatorResult.ERROR, "A data de emissão do RG não pode ser anterior a data de nascimento");
						}
					}
					
					if(pessoaTipoDocumentacao.getNrDocumento() != null && pessoaTipoDocumentacao.getNrDocumento().trim().length() < 5){
						listValidator.get(VALIDATE_RG_QUANTIDADE_MINIMA_DIGITOS).add(ValidatorResult.ERROR, "Número do RG não pode conter menos de 5 dígitos");
					}
					
					String nrRg[] = pessoaTipoDocumentacao.getNrDocumento().split("");
					boolean contemApenasZero = true;
					for(int i = 0; i < nrRg.length; i++){
						if(!nrRg[i].equals("0"))
							contemApenasZero = false;
					}
					if(contemApenasZero){
						listValidator.get(VALIDATE_RG_CONTEM_APENAS_ZERO).add(ValidatorResult.ERROR, "O RG não pode conter apenas zeros");
					}
					
				}
			}
			
			if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_NIS, connect).getCdTipoDocumentacao()) && pessoaTipoDocumentacao.getNrDocumento() != null && !pessoaTipoDocumentacao.getNrDocumento().equals("")){
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", "" + pessoaTipoDocumentacao.getCdPessoa(), ItemComparator.DIFFERENT, Types.INTEGER));
				criterios.add(new ItemComparator("nr_documento", pessoaTipoDocumentacao.getNrDocumento(), ItemComparator.EQUAL, Types.VARCHAR));
				criterios.add(new ItemComparator("cd_tipo_documentacao", "" + pessoaTipoDocumentacao.getCdTipoDocumentacao(), ItemComparator.EQUAL, Types.INTEGER));
				if(PessoaTipoDocumentacaoDAO.find(criterios, connect).next()){
					listValidator.get(VALIDATE_NIS_DUPLICADO).add(ValidatorResult.ERROR, "Uma pessoa com esse número de NIS já existe no banco de dados");
				}
				
			}
			
			if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_SUS, connect).getCdTipoDocumentacao()) && pessoaTipoDocumentacao.getNrDocumento() != null && !pessoaTipoDocumentacao.getNrDocumento().equals("")){
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", "" + pessoaTipoDocumentacao.getCdPessoa(), ItemComparator.DIFFERENT, Types.INTEGER));
				criterios.add(new ItemComparator("nr_documento", pessoaTipoDocumentacao.getNrDocumento(), ItemComparator.EQUAL, Types.VARCHAR));
				criterios.add(new ItemComparator("cd_tipo_documentacao", "" + pessoaTipoDocumentacao.getCdTipoDocumentacao(), ItemComparator.EQUAL, Types.INTEGER));
				if(PessoaTipoDocumentacaoDAO.find(criterios, connect).next()){
					listValidator.get(VALIDATE_SUS_DUPLICADO).add(ValidatorResult.ERROR, "Uma pessoa com esse número de SUS já existe no banco de dados", GRUPO_VALIDACAO_INSERT);
					listValidator.get(VALIDATE_SUS_DUPLICADO).add(ValidatorResult.ERROR, "Uma pessoa com esse número de SUS já existe no banco de dados", GRUPO_VALIDACAO_UPDATE);
				}
				
			}
			
			if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_CPF, connect).getCdTipoDocumentacao()) && pessoaTipoDocumentacao.getNrDocumento() != null && !pessoaTipoDocumentacao.getNrDocumento().equals("")){
				if(pessoaTipoDocumentacao.getNrDocumento().length() != 11){
					listValidator.get(VALIDATE_QUANT_CPF).add(ValidatorResult.ERROR, "Quantidade de caracteres para número de CPF foi inválido. Total deve ser de 11 caracteres.");
				}
			}
			
			if((pessoaTipoDocumentacao.getCdTipoDocumentacao() == TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_NIS, connect).getCdTipoDocumentacao()) && pessoaTipoDocumentacao.getNrDocumento() != null && !pessoaTipoDocumentacao.getNrDocumento().equals("")){
				if(pessoaTipoDocumentacao.getNrDocumento().length() != 11){
					listValidator.get(VALIDATE_QUANT_NIS).add(ValidatorResult.ERROR, "Quantidade de caracteres para número de NIS foi inválido. Total deve ser de 11 caracteres.");
				}
				
				if(pessoaTipoDocumentacao.getNrDocumento().equals("00000000000") || pessoaTipoDocumentacao.getNrDocumento().length() != 11){
					listValidator.get(VALIDATE_NIS_INVALIDO).add(ValidatorResult.ERROR, "NIS inválido");
				}
			}
			
			
			//Cria a mensagem de erro em um objeto de retorno chamado 'RESULT'
			result.addListValidator(listValidator);
			result.verify(idGrupo);
			
			//RETORNO
			return result;
		
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaTipoDocumentacaoServices.validate: " + e);
			return new ValidatorResult(ValidatorResult.ERROR, "Erro em PessoaTipoDocumentacaoServices.validate");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static HashMap<Integer, Validator> getListValidationRegistro(){
		HashMap<Integer, Validator> list = new HashMap<Integer, Validator>();
		
		list.put(VALIDATE_MODELO, new Validator(VALIDATE_MODELO, ValidatorResult.VALID));
		list.put(VALIDATE_TIPO_REGISTRO, new Validator(VALIDATE_TIPO_REGISTRO, ValidatorResult.VALID));
		list.put(VALIDATE_CARTORIO, new Validator(VALIDATE_CARTORIO, ValidatorResult.VALID));
		list.put(VALIDATE_MAX_NUMERO_REGISTRO_NOVO, new Validator(VALIDATE_MAX_NUMERO_REGISTRO_NOVO, ValidatorResult.VALID));
		list.put(VALIDATE_MAX_NUMERO_REGISTRO_ANTIGO, new Validator(VALIDATE_MAX_NUMERO_REGISTRO_ANTIGO, ValidatorResult.VALID));
		list.put(VALIDATE_DIGITO_VALIDADOR, new Validator(VALIDATE_DIGITO_VALIDADOR, ValidatorResult.VALID));
		list.put(VALIDATE_REGISTRO_DUPLICADO, new Validator(VALIDATE_REGISTRO_DUPLICADO, ValidatorResult.VALID));
		list.put(VALIDATE_DATA_EMISSAO_VALIDA, new Validator(VALIDATE_DATA_EMISSAO_VALIDA, ValidatorResult.VALID));
		list.put(VALIDATE_DATA_EMISSAO_ANO_MENOR_NASCIMENTO, new Validator(VALIDATE_DATA_EMISSAO_ANO_MENOR_NASCIMENTO, ValidatorResult.VALID));
		list.put(VALIDATE_DATA_EMISSAO_ANO_MAIOR_ATUAL, new Validator(VALIDATE_DATA_EMISSAO_ANO_MAIOR_ATUAL, ValidatorResult.VALID));
		list.put(VALIDATE_DATA_EMISSAO_DIFERENTE_REGISTRO, new Validator(VALIDATE_DATA_EMISSAO_DIFERENTE_REGISTRO, ValidatorResult.VALID));
		list.put(VALIDATE_SERVENTIA, new Validator(VALIDATE_SERVENTIA, ValidatorResult.VALID));
		list.put(VALIDATE_CARTORIO_SERVENTIA, new Validator(VALIDATE_CARTORIO_SERVENTIA, ValidatorResult.VALID));
		list.put(VALIDATE_REGISTRO_ANTIGO_SEM_FOLHA, new Validator(VALIDATE_REGISTRO_ANTIGO_SEM_FOLHA, ValidatorResult.VALID));
		list.put(VALIDATE_REGISTRO_ANTIGO_SEM_LIVRO, new Validator(VALIDATE_REGISTRO_ANTIGO_SEM_LIVRO, ValidatorResult.VALID));
		list.put(VALIDATE_DATA_EMISSAO_MENOR_NASCIMENTO, new Validator(VALIDATE_DATA_EMISSAO_MENOR_NASCIMENTO, ValidatorResult.VALID));
		list.put(VALIDATE_DATA_EMISSAO_MAIOR_ATUAL, new Validator(VALIDATE_DATA_EMISSAO_MAIOR_ATUAL, ValidatorResult.VALID));
		
		return list;
	}
	
	public static HashMap<Integer, Validator> getListValidationNis(){
		HashMap<Integer, Validator> list = new HashMap<Integer, Validator>();
		
		list.put(VALIDATE_QUANT_NIS, new Validator(VALIDATE_QUANT_NIS, ValidatorResult.VALID));
		list.put(VALIDATE_NIS_INVALIDO, new Validator(VALIDATE_NIS_INVALIDO, ValidatorResult.VALID));
		list.put(VALIDATE_NIS_DUPLICADO, new Validator(VALIDATE_NIS_DUPLICADO, ValidatorResult.VALID));
		
		return list;
	}
	
	public static HashMap<Integer, Validator> getListValidationCpf(){
		HashMap<Integer, Validator> list = new HashMap<Integer, Validator>();
		
		list.put(VALIDATE_QUANT_CPF, new Validator(VALIDATE_QUANT_CPF, ValidatorResult.VALID));
		
		return list;
	}
	
	public static HashMap<Integer, Validator> getListValidationRg(){
		HashMap<Integer, Validator> list = new HashMap<Integer, Validator>();
		
		list.put(VALIDATE_RG_DATA_EMISSAO_ANTERIOR_NASCIMENTO, new Validator(VALIDATE_RG_DATA_EMISSAO_ANTERIOR_NASCIMENTO, ValidatorResult.VALID));
		list.put(VALIDATE_RG_QUANTIDADE_MINIMA_DIGITOS, new Validator(VALIDATE_RG_QUANTIDADE_MINIMA_DIGITOS, ValidatorResult.VALID));
		list.put(VALIDATE_RG_CONTEM_APENAS_ZERO, new Validator(VALIDATE_RG_CONTEM_APENAS_ZERO, ValidatorResult.VALID));
		
		return list;
	}
	
	public static HashMap<Integer, Validator> getListValidationSus(){
		HashMap<Integer, Validator> list = new HashMap<Integer, Validator>();
		
		list.put(VALIDATE_SUS_DUPLICADO, new Validator(VALIDATE_SUS_DUPLICADO, ValidatorResult.VALID));
		
		return list;
	}
	
	public static Result saveCompliance(PessoaTipoDocumentacao objeto, AuthData authData, int tpAcao, Connection connect) {
		try {
						
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_tipo_documentacao (cd_pessoa,"+
																					                    "cd_tipo_documentacao,"+
																					                    "nr_documento,"+
																					                    "folha,"+
																					                    "livro,"+
																					                    "dt_emissao,"+
																					                    "cd_orgao_emissor,"+
																					                    "tp_modelo,"+
																					                    "cd_cartorio,"+
																					                    "cd_usuario_compliance,"+
																					                    "dt_compliance,"+
																					                    "tp_acao_compliance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdTipoDocumentacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoDocumentacao());
			pstmt.setString(3,objeto.getNrDocumento());
			pstmt.setString(4,objeto.getFolha());
			pstmt.setString(5,objeto.getLivro());
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			if(objeto.getCdOrgaoEmissor()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdOrgaoEmissor());
			pstmt.setInt(8,objeto.getTpModelo());
			if(objeto.getCdCartorio()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdCartorio());
			if(authData==null)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10, authData.getUsuario().getCdUsuario());
			pstmt.setTimestamp(11, new Timestamp(new GregorianCalendar().getTimeInMillis()));
			pstmt.setInt(12, tpAcao);
			
			int retorno = pstmt.executeUpdate();
			if(retorno <= 0){
				return new Result(retorno);
			}
			
			return new Result((retorno<0 ? retorno : objeto.getCdPessoa()));
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static ResultSetMap findCompliance(int cdAluno) {
		return ComplianceManager.find("acd", "aluno", cdAluno);
	}
}
