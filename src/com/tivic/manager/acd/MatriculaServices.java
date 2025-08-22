package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.adm.Contrato;
import com.tivic.manager.adm.ContratoDesconto;
import com.tivic.manager.adm.ContratoPlanoPagto;
import com.tivic.manager.adm.ContratoPlanoPagtoDAO;
import com.tivic.manager.adm.ContratoServices;
import com.tivic.manager.adm.FormaPagamentoServices;
import com.tivic.manager.agd.AgendaItem;
import com.tivic.manager.agd.AgendaItemDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.LogradouroServices;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.Pais;
import com.tivic.manager.grl.PaisDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaAlergiaServices;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaDoencaServices;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.PessoaFichaMedicaServices;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaFisicaServices;
import com.tivic.manager.grl.PessoaNecessidadeEspecialServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.PessoaTipoDocumentacao;
import com.tivic.manager.grl.PessoaTipoDocumentacaoDAO;
import com.tivic.manager.grl.PessoaTipoDocumentacaoServices;
import com.tivic.manager.grl.TipoDocumentacaoDAO;
import com.tivic.manager.grl.TipoDocumentacaoServices;
import com.tivic.manager.grl.TipoNecessidadeEspecialServices;
import com.tivic.manager.grl.TipoOcorrenciaServices;
import com.tivic.manager.log.LogServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.ComplianceManager;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.Validator;
import com.tivic.manager.util.ValidatorResult;

import flex.messaging.io.ArrayCollection;

public class MatriculaServices {

	/* situação finais do censo */
	public static final int ST_CENSO_FINAL_LANCADO 			 = 1;
	public static final int ST_CENSO_FINAL_NAO_LANCADO 		 = 2;
	public static final int ST_CENSO_FINAL_NAO_ENCONTRADO 	 = 3;
	public static final int ST_CENSO_FINAL_LANCADO_POS_CENSO = 4;
	
	public static final String[] situacaoCensoFinal = {"Nenhum",
		"Lançado",
		"Não lançado",
		"Não encontrado",
		"Lançado Pós-Censo"};
	
	/* situações de aluno para Censo */
	public static final int ST_ALUNO_CENSO_APROVADO 	= 0;
	public static final int ST_ALUNO_CENSO_REPROVADO 	= 1;
	public static final int ST_ALUNO_CENSO_TRANSFERIDO  = 2;
	public static final int ST_ALUNO_CENSO_DESISTENTE 	= 3;
	public static final int ST_ALUNO_CENSO_EVADIDO  	= 4;
	public static final int ST_ALUNO_CENSO_FALECIDO     = 5;

	public static final String[] situacaoAlunoCenso = {"Aprovado",
		"Reprovado",
		"Transferido",
		"Desistente",
		"Evadido",
		"Falecido"};
	
	public static final int ST_ALUNO_CENSO_TRANSFERIDO_CONVERSAO         = 1;
	public static final int ST_ALUNO_CENSO_DEIXOU_FREQUENTAR_CONVERSAO 	 = 2;
	public static final int ST_ALUNO_CENSO_FALECIDO_CONVERSAO 	         = 3;
	public static final int ST_ALUNO_CENSO_REPROVADO_CONVERSAO           = 4;
	public static final int ST_ALUNO_CENSO_APROVADO_CONVERSAO  	         = 5;
	public static final int ST_ALUNO_CENSO_APROVADO_CONCLUINTE_CONVERSAO = 6;
	public static final int ST_ALUNO_CENSO_EM_ANDAMENTO_CONVERSAO  	     = 7;
		
	public static final String[] situacaoAlunoCensoConversao = {"Nenhum",
		"Transferido",
		"Deixou de Frequentar",
		"Falecido",
		"Reprovado",
		"Aprovado",
		"Aprovado concluinte",
		"Em andamento/Sem movimentação"};
	
	/* situações da matricula */
	public static final int ST_ATIVA     		 = 0;
	public static final int ST_CONCLUIDA 		 = 1;
	public static final int ST_TRANCADA  		 = 2;
	public static final int ST_CANCELADO 		 = 3;
	public static final int ST_PENDENTE  		 = 4;
	public static final int ST_EM_TRANSFERENCIA  = 5;
	public static final int ST_TRANSFERIDO  	 = 6;
	public static final int ST_EVADIDO    	     = 7;
	public static final int ST_INATIVO    	     = 8;
	public static final int ST_DESISTENTE  	     = 9;
	public static final int ST_RECLASSIFICACAO   = 10;
	public static final int ST_REALOCACAO	     = 11;
	public static final int ST_FALECIDO	     	 = 12;
	public static final int ST_FECHADA	     	 = 13;
	
	public static final String[] situacao = {"Ativa",
		"Concluída",
		"Trancada",
		"Cancelado",
		"Pendente",
		"Em Transferência",
		"Transferido",
		"Evadido",
		"Inativo",
		"Desistente",
		"Reclassificado",
		"Realocação",
		"Falecido",
		"Fechada"};

	/* tipo de matrícula */
	public static final int TP_NORMAL         = 0;
	public static final int TP_COMPLEMENTO    = 1;
	public static final int TP_TRANSF_INTERNA = 2;
	public static final int TP_TRANSF_EXTERNA = 3;
	public static final int TP_RETORNO        = 4;

	public static final String[] tipo = {"Normal",
		"Complemento",
		"Transferência interna",
		"Transferência externa",
		"Retorno"};

	/* tipo de curso anterior */
	public static final int TP_ANT_ENSINO_MEDIO  = 0;
	public static final int TP_ANT_CURSO_TECNICO = 1;
	public static final int TP_ANT_SUPLETIVO     = 2;
	public static final int TP_CURSO_SUPERIOR    = 3;

	public static final String[] tipoCursoAnterior = {"Ensino Médio",
		"Curso Técnico",
		"Supletivo",
		"Curso Superior"};

	/* codificacao de erros reportados por rotinas de com.tivic.manager.adm.MatriculaServices */
	public static final int ERR_INEXISTENCIA_VAGAS 		= -2;
	public static final int ERR_DESCONTO_INVALIDO  		= -3;
	public static final int ERR_MATRICULA_PERIODO  		= -4;
	public static final int ERR_CIDADE_ALUNO_DIFERENTE  = -5;
	public static final int ERR_ALUNO_FORA_IDADE        = -6;
	public static final int ERR_ALUNO_FORA_CRECHE_ED_INFANTIL = -7;
	public static final int ERR_ALUNO_FORA_EJA = -8;
	public static final int ERR_ALUNO_FORA_IDADE_PERMISSAO_ESPECIAL = -9;
	public static final int ERR_ESCOLA_OFFLINE = -10;
	
	
	//Grupos de Validação
	public static final int GRUPO_VALIDACAO_INSERT = 0;
	public static final int GRUPO_VALIDACAO_UPDATE = 1;
	public static final int GRUPO_VALIDACAO_EDUCACENSO = 2;
	//VALIDACOES
	public static final int VALIDATE_TRANSPORTE_ESCOLAR 					= 0;
	public static final int VALIDATE_TIPOS_TRANSPORTE   					= 1;
	public static final int VALIDATE_MAX_TIPOS_TRANSPORTE   				= 2;
	public static final int VALIDATE_CURSO_MULTI   							= 3;
	public static final int VALIDATE_HORARIOS_CONFLITANTES   				= 6;
	public static final int VALIDATE_MATRICULA_PERIODO_ANTERIOR				= 7;
	public static final int VALIDATE_MATRICULA_CURSO_2_ANOS					= 9;
	public static final int VALIDATE_MATRICULA_CURSO_3_ANOS					= 10;
	public static final int VALIDATE_MATRICULA_CURSO_4_ANOS					= 11;
	public static final int VALIDATE_MATRICULA_CURSO_5_ANOS					= 12;
	public static final int VALIDATE_MATRICULA_EJA							= 13;
	public static final int VALIDATE_MODALIDADES_INCOMPATIVEIS   		 	= 15;
	public static final int VALIDATE_DATA_MATRICULA		   		 			= 16;
	public static final int VALIDATE_TURMA_CHEIA		   		 			= 17;
	public static final int VALIDATE_MAIORES_18_DIURNO	   		 			= 18;
	
	public static Result saveAtivar(Matricula matricula, int tpOcorrencia){
		return saveAtivar(matricula, 0, tpOcorrencia, null, null, false, false, null);
	}
	
	public static Result saveAtivar(Matricula matricula, int tpOcorrencia, Connection connect){
		return saveAtivar(matricula, 0, tpOcorrencia, null, null, false, false, null, connect);
	}
	
	public static Result saveAtivar(Matricula matricula, int cdUsuario, int tpOcorrencia){
		return saveAtivar(matricula, cdUsuario, tpOcorrencia, null, null, false, false, null);
	}
	public static Result saveAtivar(Matricula matricula, int cdUsuario, int tpOcorrencia, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente){
		return saveAtivar(matricula, cdUsuario, tpOcorrencia, null, null, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, null);
	}
	public static Result saveAtivar(Matricula matricula, int cdUsuario, int tpOcorrencia, Aluno aluno, PessoaEndereco endereco, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente){
		return saveAtivar(matricula, cdUsuario, tpOcorrencia, aluno, endereco, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, null, null);
	}
	public static Result saveAtivar(Matricula matricula, int cdUsuario, int tpOcorrencia, Aluno aluno, PessoaEndereco endereco, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, String nrSus){
		return saveAtivar(matricula, cdUsuario, tpOcorrencia, aluno, endereco, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, nrSus, null);
	}
	public static Result saveAtivar(Matricula matricula, int cdUsuario, int tpOcorrencia, Aluno aluno, PessoaEndereco endereco, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, String nrSus, int cdTipoTransporte){
		return saveAtivar(matricula, cdUsuario, tpOcorrencia, aluno, endereco, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, nrSus, cdTipoTransporte, null);
	}
	/**
	 * Método utilizado para Ativar uma matrícula
	 * @param matricula
	 * @param cdUsuario
	 * @param tpOcorrencia A ativação poderá ser do tipo CONFIRMAÇÃO (Confirmação de uma matrícula pendente com requerimento para os pais) ou ATIVAÇÃO (Ativação de uma matrícula como método corretivo). 
	 * @param connect
	 * @return
	 */
	public static Result saveAtivar(Matricula matricula, int cdUsuario, int tpOcorrencia, Aluno aluno, PessoaEndereco endereco, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, String nrSus, Connection connect){
		return saveAtivar(matricula, cdUsuario, tpOcorrencia, aluno, endereco, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, nrSus, -1, connect);
	}
	public static Result saveAtivar(Matricula matricula, int cdUsuario, int tpOcorrencia, Aluno aluno, PessoaEndereco endereco, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, String nrSus, int cdTipoTransporte, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(matricula.getStMatricula() == ST_ATIVA){
				return new Result(-1, "Essa matrícula já está ativa");
			}
			
			if(matricula.getStMatricula() == ST_ATIVA || matricula.getStMatricula() == ST_CONCLUIDA || matricula.getStMatricula() == ST_REALOCACAO || matricula.getStMatricula() == ST_RECLASSIFICACAO  || matricula.getStMatricula() == ST_INATIVO){
				return new Result(-1, "Matrículas Ativas, Concluidas, Realocadas, Reclassificadas ou Inativos não podem ser ativadas");
			}
			
			
			if(matricula.getCdCurso() != ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0) && matricula.getCdCurso() != ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)){
				if(temMatriculaRegularNoPeriodo(matricula.getCdAluno(), matricula.getCdPeriodoLetivo(), matricula.getCdMatricula(), connect)){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(ERR_MATRICULA_PERIODO, "Aluno já matriculado nesse período letivo");
				}
			}
			
			//Apenas o usuário de suporte pode fazer matrículas fora do periodo letivo
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(turma.getCdInstituicao(), connect);
			rsmPeriodoAtual.next();
			if(!UsuarioServices.isSuporte(cdUsuario, connect) && Integer.parseInt(InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect).getNmPeriodoLetivo()) < Integer.parseInt(InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect).getNmPeriodoLetivo())){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Não é possível realizar essa ação para matrícula de períodos anteriores");
			}
			
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);
			
			//Verificação para impedir usuários de trabalharem em escolas que estão offline, criando possíveis inconsistências
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem fazer ativacao de matricula");
			}
			
			//Verifica se o CEP é valido
			if(endereco != null && endereco.getNrCep() != null){
				ResultSetMap rsmLogradouro = LogradouroServices.getEnderecoByCep(endereco.getNrCep(), connect);
				if(endereco.getNrCep() == null || endereco.getNrCep().trim().equals("") || endereco.getNrCep().equals("45000000") || rsmLogradouro == null  || rsmLogradouro.size() == 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "O endereço não possui CEP, ou o mesmo é inválido");
				}
			}
			
			//Caso o transporte seja passado, insere-o entre um dos transportes usados pelo aluno
			if(cdTipoTransporte > 0){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_matricula", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tipo_transporte", "" + cdTipoTransporte, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmTipoTransporte = MatriculaTipoTransporteDAO.find(criterios, connect);
				if(!rsmTipoTransporte.next()){
					if(MatriculaTipoTransporteDAO.insert(new MatriculaTipoTransporte(matricula.getCdMatricula(), cdTipoTransporte), connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-2, "Erro ao adicionar transporte");
					}
				}
			}
			//Caso seja passado como zero, então delete qualquer transporte que o aluno venha a ter
			else if(cdTipoTransporte == 0){
				if(connect.prepareStatement("DELETE FROM acd_matricula_tipo_transporte WHERE cd_matricula = " + matricula.getCdMatricula()).executeUpdate() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao deletar transportes");
				}
			}
			
			int stMatriculaOrigem = matricula.getStMatricula();
			matricula.setStMatricula(ST_ATIVA);
			int stAlunoCensoAntigo = matricula.getStAlunoCenso();
			matricula.setStAlunoCenso(ST_ALUNO_CENSO_APROVADO);
			
			//Atualiza a data da matricula para a data em que foi 'Ativada'
			GregorianCalendar dtHoje = Util.getDataAtual();
			dtHoje.set(Calendar.HOUR_OF_DAY, 12);
			dtHoje.set(Calendar.MINUTE, 0);
			dtHoje.set(Calendar.SECOND, 0);
			matricula.setDtMatricula(dtHoje);
			
			Result ret = MatriculaServices.save(matricula, cdUsuario, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, cdTipoTransporte, connect);
			if(ret.getCode() <= 0 || (!permitirAlunoCidadeDiferente && ret.getCode() == ERR_CIDADE_ALUNO_DIFERENTE) || (!permitirAlunoIdadeDivergente && ret.getCode() == ERR_ALUNO_FORA_IDADE)){
				return ret;
			}
			
			//Verifica se o email é válido
			if(aluno != null){
				if(aluno.getNmEmail() != null && !aluno.getNmEmail().trim().equals("") && !Util.isEmail(aluno.getNmEmail())){
					return new Result(-1, "Email inválido");
				}
				
				//Atualiza o cadastro do aluno
				if(AlunoDAO.update(aluno, connect) < 0){
					return new Result(-1, "Erro ao atualizar aluno");
				}
			}
			
			//Atualiza o endereço, caso o mesmo tenha sido passado
			if(endereco != null){
				if(PessoaEnderecoDAO.update(endereco, connect) < 0){
					return new Result(-1, "Erro ao atualizar endereco");
				}
			}

			//Atualiza o número de sus, caso o mesmo tenha sido passado
			if(nrSus != null){
				int cdTipoDocumentacaoSus = TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_SUS, connect).getCdTipoDocumentacao();
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", "" + aluno.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tipo_documentacao", "" + cdTipoDocumentacaoSus, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPessoaTipoDocumentacao = PessoaTipoDocumentacaoDAO.find(criterios, connect);
				if(rsmPessoaTipoDocumentacao.next()){
					PessoaTipoDocumentacao pessoaTipoDocumentacaoSus = PessoaTipoDocumentacaoDAO.get(rsmPessoaTipoDocumentacao.getInt("cd_pessoa"), rsmPessoaTipoDocumentacao.getInt("cd_tipo_documentacao"), connect);
					pessoaTipoDocumentacaoSus.setNrDocumento(nrSus);
					if(PessoaTipoDocumentacaoDAO.update(pessoaTipoDocumentacaoSus, connect) <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar Sus do aluno");
					}
				}
				else{
					PessoaTipoDocumentacao pessoaTipoDocumentacaoSus = new PessoaTipoDocumentacao(aluno.getCdAluno(), cdTipoDocumentacaoSus, nrSus, null, null, null, 0, 0, 0);
					if(PessoaTipoDocumentacaoDAO.insert(pessoaTipoDocumentacaoSus, connect) <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao inserir Sus do aluno");
					}
				}
			}
			
			//Registra as ocorrencias da ativação da matrícula
			int cdTipoOcorrencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(String.valueOf(tpOcorrencia), connect).getCdTipoOcorrencia();
			if(cdTipoOcorrencia > 0){
				
				if(aluno == null){
					aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				}
				
				String txtOcorrencia = (tpOcorrencia == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_CONFIRMACAO_MATRICULA) ? "Confirmada Matrícula do aluno " + aluno.getNmPessoa() : "Ativada matrícula do aluno " + aluno.getNmPessoa());
				
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), txtOcorrencia, Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), stMatriculaOrigem, cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de confirmação de matrícula");
				}
			}
			
			int cdTipoOcorrenciaAlteracaoSituacaoCenso = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_SITUACAO_CENSO, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaAlteracaoSituacaoCenso > 0){
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect);
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " alterado situação do Censo de " + situacaoAlunoCenso[stAlunoCensoAntigo] + " para " + situacaoAlunoCenso[matricula.getStAlunoCenso()] + " para o período letivo de " + instituicaoPeriodo.getNmPeriodoLetivo(), Util.getDataAtual(), cdTipoOcorrenciaAlteracaoSituacaoCenso, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), matricula.getCdTurma(), matricula.getCdTurma(), matricula.getStMatricula(), cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de situacao do censo");
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Ativação realizada com sucesso");
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			result.addObject("rsm", rsm);
			result.addObject("MATRICULA", matricula);
			
			return result;
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na ativação");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que torna uma matrícula pendente
	 * @param matricula
	 * @return
	 */
	public static Result savePendente(Matricula matricula, int cdUsuario){
		return savePendente(matricula, cdUsuario, null);
	}
	
	public static Result savePendente(Matricula matricula, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(matricula.getStMatricula() == ST_PENDENTE){
				return new Result(-1, "Essa matrícula já está pendente");
			}
			
			if(matricula.getStMatricula() == ST_REALOCACAO || matricula.getStMatricula() == ST_RECLASSIFICACAO  || matricula.getStMatricula() == ST_INATIVO){
				return new Result(-1, "Matrículas Realocadas, Reclassificadas ou Inativos não podem ser colocadas como pendente");
			}
			
			if(matricula.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0) ||
			   matricula.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Matrículas do Mais Educação e de AEE não podem ser colocadas como pendentes");
			}
			
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(turma.getCdInstituicao(), connect);
			rsmPeriodoAtual.next();
			
			//Apenas o usuário de suporte pode realizar a ação
			if(!UsuarioServices.isSuporte(cdUsuario, connect) && Integer.parseInt(InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect).getNmPeriodoLetivo()) < Integer.parseInt(InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect).getNmPeriodoLetivo())){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Não é possível realizar essa ação para matrícula de períodos anteriores");
			}
			
			//Impede de se alterar dados de alunos em escolas que estão offline
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem colocar matriculas em estado de pendencia");
			}
			
			int stMatriculaOrigem = matricula.getStMatricula();
			
			matricula.setStMatricula(ST_PENDENTE);
			int ret = MatriculaDAO.update(matricula, connect); 
			if(ret <= 0){
				return new Result(-1, "Erro ao atualizar matrícula");
			}
			
			
			int cdTipoOcorrenciaPendente = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_PENDENTE, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaPendente > 0){
				Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " retornado para o estado de pendente", Util.getDataAtual(), cdTipoOcorrenciaPendente, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), stMatriculaOrigem, cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de pendencia de matrícula");
				}
			}
			

			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Pendência realizada com sucesso");
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			result.addObject("rsm", rsm);
			return result;
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na pendente");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que torna um aluno falecido a partir da matrícula
	 * @param matricula
	 * @return
	 */
	public static Result saveFalecido(Matricula matricula, int cdUsuario){
		return saveFalecido(matricula, cdUsuario, null);
	}
	
	public static Result saveFalecido(Matricula matricula, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(matricula.getStMatricula() == ST_FALECIDO){
				return new Result(-1, "Essa matrícula já está com o status de aluno falecido");
			}
			
			if(matricula.getStMatricula() != ST_ATIVA && matricula.getStMatricula() != ST_CONCLUIDA && matricula.getStMatricula() != ST_DESISTENTE && matricula.getStMatricula() != ST_EVADIDO){
				return new Result(-1, "Somente matrículas Ativas, Concluidas, Desistentes ou Evadidas podem ser colocadas com o aluno como Falecido");
			}
			
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			
			//Impede alteração de alunos que estão em escolas offline, impedindo inconsistencias
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem colocar matriculas em estado de falecido");
			}
			
			ResultSetMap rsmTurmaSituacaoAlunoCenso = new ResultSetMap(connect.prepareStatement("SELECT cd_tipo_ocorrencia FROM grl_ocorrencia A, acd_ocorrencia_turma B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO + ") AND cd_turma = "+matricula.getCdTurma()+" order by dt_ocorrencia DESC").executeQuery());
			if(rsmTurmaSituacaoAlunoCenso.next()){
				if(rsmTurmaSituacaoAlunoCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO)){
					return new Result(-1, "Não é mais possível mudar o status dessa matrícula, pois a sua turma já foi fechada em relação a Situação do Aluno para o Censo");
				}
			}
			
			int stMatriculaOrigem = matricula.getStMatricula();
			
			matricula.setStMatricula(ST_FALECIDO);
			int stAlunoCensoAntigo = matricula.getStAlunoCenso();
			matricula.setStAlunoCenso(ST_ALUNO_CENSO_FALECIDO);
			int ret = MatriculaDAO.update(matricula, connect); 
			if(ret <= 0){
				return new Result(-1, "Erro ao atualizar matrícula");
			}
			
			
			int cdTipoOcorrenciaFalecido = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_FALECIDO, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaFalecido > 0){
				Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " considerado falecido", Util.getDataAtual(), cdTipoOcorrenciaFalecido, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), stMatriculaOrigem, cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de falecido do aluno");
				}
			}
			
			int cdTipoOcorrenciaAlteracaoSituacaoCenso = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_SITUACAO_CENSO, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaAlteracaoSituacaoCenso > 0){
				Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect);
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " alterado situação do Censo de " + situacaoAlunoCenso[stAlunoCensoAntigo] + " para " + situacaoAlunoCenso[matricula.getStAlunoCenso()] + " para o período letivo de " + instituicaoPeriodo.getNmPeriodoLetivo(), Util.getDataAtual(), cdTipoOcorrenciaAlteracaoSituacaoCenso, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), matricula.getCdTurma(), matricula.getCdTurma(), matricula.getStMatricula(), cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de situacao do censo");
				}
			}

			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Declaração de falecido realizada com sucesso");
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			result.addObject("rsm", rsm);
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na declaração de falecido");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo que torna uma matrícula como Concluida
	 * @param matricula
	 * @param cdUsuario
	 * @return
	 */
	public static Result saveConcluido(Matricula matricula, int cdUsuario){
		return saveConcluido(matricula, cdUsuario, null);
	}
	
	public static Result saveConcluido(Matricula matricula, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(matricula.getStMatricula() == ST_CONCLUIDA){
				return new Result(-1, "Essa matrícula já está com o status de concluida");
			}
			
			if(matricula.getStMatricula() != ST_DESISTENTE && matricula.getStMatricula() != ST_EVADIDO && matricula.getStMatricula() != ST_CANCELADO && matricula.getStMatricula() != ST_EM_TRANSFERENCIA){
				return new Result(-1, "Somente matrículas Desistentes, Evadidas, Canceladas ou Em Transferência podem ser colocadas como Concluídas");
			}
			
			if(matricula.getStMatricula() == ST_EM_TRANSFERENCIA && !UsuarioServices.isAdmSecretariaEducacao(cdUsuario, connect)){
				return new Result(-1, "Apenas a SMED pode realizar conclusões de matrículas Em Transferência");
			}
			
			ResultSetMap rsmTurmaSituacaoAlunoCenso = new ResultSetMap(connect.prepareStatement("SELECT cd_tipo_ocorrencia FROM grl_ocorrencia A, acd_ocorrencia_turma B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO + ") AND cd_turma = "+matricula.getCdTurma()+" order by dt_ocorrencia DESC").executeQuery());
			if(rsmTurmaSituacaoAlunoCenso.next()){
				if(rsmTurmaSituacaoAlunoCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO)){
					return new Result(-1, "Não é mais possível mudar o status dessa matrícula, pois a sua turma já foi fechada em relação a Situação do Aluno para o Censo");
				}
			}
			
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(turma.getCdInstituicao(), connect);
			rsmPeriodoAtual.next();
			if(!UsuarioServices.isSuporte(cdUsuario, connect) && Integer.parseInt(InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect).getNmPeriodoLetivo()) < Integer.parseInt(InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect).getNmPeriodoLetivo())){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Não é possível realizar essa ação para matrícula do período atual");
			}
			
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem colocar matriculas em estado de concluido");
			}
			
			int stMatriculaOrigem = matricula.getStMatricula();
			
			matricula.setStMatricula(ST_CONCLUIDA);
			int stAlunoCensoAntigo = matricula.getStAlunoCenso();
			matricula.setStAlunoCenso(ST_ALUNO_CENSO_APROVADO);
			int ret = MatriculaDAO.update(matricula, connect); 
			if(ret <= 0){
				return new Result(-1, "Erro ao atualizar matrícula");
			}
			
			
			int cdTipoOcorrenciaConcluido = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_MATRICULA_CONCLUIDA, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaConcluido > 0){
				Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Matrícula do Aluno " + aluno.getNmPessoa() + " foi concluída", Util.getDataAtual(), cdTipoOcorrenciaConcluido, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), stMatriculaOrigem, cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de conclusão de matrícula");
				}
			}
			
			int cdTipoOcorrenciaAlteracaoSituacaoCenso = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_SITUACAO_CENSO, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaAlteracaoSituacaoCenso > 0){
				Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect);
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " alterado situação do Censo de " + situacaoAlunoCenso[stAlunoCensoAntigo] + " para " + situacaoAlunoCenso[matricula.getStAlunoCenso()] + " para o período letivo de " + instituicaoPeriodo.getNmPeriodoLetivo(), Util.getDataAtual(), cdTipoOcorrenciaAlteracaoSituacaoCenso, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), matricula.getCdTurma(), matricula.getCdTurma(), matricula.getStMatricula(), cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de situacao do censo");
				}
			}

			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Conclusão de matrícula realizada com sucesso");
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			result.addObject("rsm", rsm);
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na conclusão de matrícula");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que realiza a evasão de um aluno
	 * @param matricula
	 * @param cdUsuario
	 * @return
	 */
	public static Result saveEvasao(Matricula matricula, int cdUsuario){
		return saveEvasao(matricula, cdUsuario, null);
	}
	
	public static Result saveEvasao(Matricula matricula, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(matricula.getStMatricula() == ST_EVADIDO){
				return new Result(-1, "Esse aluno já foi considerado evadido nessa matrícula");
			}
			
			if(matricula.getStMatricula() != ST_ATIVA && matricula.getStMatricula() != ST_CONCLUIDA){
				return new Result(-1, "Somente matrículas Ativas ou Concluidas podem ser evadidas");
			}
			
			//Impede alterações em escolas que estão offline, impedindo inconsistencias
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem colocar matriculas em estado de evadido");
			}
			
			ResultSetMap rsmTurmaSituacaoAlunoCenso = new ResultSetMap(connect.prepareStatement("SELECT cd_tipo_ocorrencia FROM grl_ocorrencia A, acd_ocorrencia_turma B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO + ") AND cd_turma = "+matricula.getCdTurma()+" order by dt_ocorrencia DESC").executeQuery());
			if(rsmTurmaSituacaoAlunoCenso.next()){
				if(rsmTurmaSituacaoAlunoCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO)){
					return new Result(-1, "Não é mais possível mudar o status dessa matrícula, pois a sua turma já foi fechada em relação a Situação do Aluno para o Censo");
				}
			}
			
			int stMatriculaOrigem = matricula.getStMatricula();
			
			matricula.setStMatricula(ST_EVADIDO);
			int stAlunoCensoAntigo = matricula.getStAlunoCenso();
			matricula.setStAlunoCenso(ST_ALUNO_CENSO_EVADIDO);
			int ret = MatriculaDAO.update(matricula, connect); 
			if(ret <= 0){
				return new Result(-1, "Erro ao atualizar matrícula");
			}
			
			
			int cdTipoOcorrenciaEvasao = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_EVADIDO, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaEvasao > 0){
				Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " considerado evadido", Util.getDataAtual(), cdTipoOcorrenciaEvasao, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), stMatriculaOrigem, cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de evasão de matrícula");
				}
			}
			
			int cdTipoOcorrenciaAlteracaoSituacaoCenso = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_SITUACAO_CENSO, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaAlteracaoSituacaoCenso > 0){
				Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect);
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " alterado situação do Censo de " + situacaoAlunoCenso[stAlunoCensoAntigo] + " para " + situacaoAlunoCenso[matricula.getStAlunoCenso()] + " para o período letivo de " + instituicaoPeriodo.getNmPeriodoLetivo(), Util.getDataAtual(), cdTipoOcorrenciaAlteracaoSituacaoCenso, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), matricula.getCdTurma(), matricula.getCdTurma(), matricula.getStMatricula(), cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de situacao do censo");
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Evasão realizada com sucesso");
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			result.addObject("rsm", rsm);
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na evasão");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que realiza a desistencia de um aluno
	 * @param matricula
	 * @param cdUsuario
	 * @return
	 */
	public static Result saveDesistente(Matricula matricula, int cdUsuario){
		return saveDesistente(matricula, cdUsuario, null);
	}
	
	public static Result saveDesistente(Matricula matricula, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(matricula.getStMatricula() == ST_DESISTENTE){
				return new Result(-1, "Esse aluno já foi considerado desistente nessa matrícula");
			}
			
			if(matricula.getStMatricula() != ST_ATIVA && matricula.getStMatricula() != ST_CONCLUIDA && matricula.getStMatricula() != ST_EVADIDO){
				return new Result(-1, "Somente matrículas Ativas, Concluidas ou Evadidas podem ser colocadas desistência");
			}
			
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			
			//Impede alterações em escolas que estão offline, impedindo inconsistencias
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem colocar matriculas em estado de desistente");
			}
			
			ResultSetMap rsmTurmaSituacaoAlunoCenso = new ResultSetMap(connect.prepareStatement("SELECT cd_tipo_ocorrencia FROM grl_ocorrencia A, acd_ocorrencia_turma B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO + ") AND cd_turma = "+matricula.getCdTurma()+" order by dt_ocorrencia DESC").executeQuery());
			if(rsmTurmaSituacaoAlunoCenso.next()){
				if(rsmTurmaSituacaoAlunoCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO)){
					return new Result(-1, "Não é mais possível mudar o status dessa matrícula, pois a sua turma já foi fechada em relação a Situação do Aluno para o Censo");
				}
			}
			
			int stMatriculaOrigem = matricula.getStMatricula();
			
			matricula.setStMatricula(ST_DESISTENTE);
			int stAlunoCensoAntigo = matricula.getStAlunoCenso();
			matricula.setStAlunoCenso(ST_ALUNO_CENSO_DESISTENTE);
			int ret = MatriculaDAO.update(matricula, connect); 
			if(ret <= 0){
				return new Result(-1, "Erro ao atualizar matrícula");
			}
			
			
			int cdTipoOcorrenciaDesistente = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_DESISTENTE, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaDesistente > 0){
				Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " considerado desistente", Util.getDataAtual(), cdTipoOcorrenciaDesistente, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), stMatriculaOrigem, cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de desistencia de matrícula");
				}
			}
			
			int cdTipoOcorrenciaAlteracaoSituacaoCenso = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_SITUACAO_CENSO, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaAlteracaoSituacaoCenso > 0){
				Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect);
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " alterado situação do Censo de " + situacaoAlunoCenso[stAlunoCensoAntigo] + " para " + situacaoAlunoCenso[matricula.getStAlunoCenso()] + " para o período letivo de " + instituicaoPeriodo.getNmPeriodoLetivo(), Util.getDataAtual(), cdTipoOcorrenciaAlteracaoSituacaoCenso, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), matricula.getCdTurma(), matricula.getCdTurma(), matricula.getStMatricula(), cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de situacao do censo");
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Desistência realizada com sucesso");
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			result.addObject("rsm", rsm);
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace();
			return new Result(-1, "Erro na desistencia");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que realiza o cancelamento de um aluno com um texto de justificativa do cancelamento que será inserido junto ao texto da ocorrência gerada
	 * @param matricula
	 * @param txtJustificativa
	 * @param cdUsuario
	 * @return
	 */
	public static Result saveCancelamento(Matricula matricula, String txtJustificativa, int cdUsuario){
		return saveCancelamento(matricula, txtJustificativa, cdUsuario, null);
	}
	
	public static Result saveCancelamento(Matricula matricula, String txtJustificativa, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(matricula.getStMatricula() == ST_CANCELADO){
				return new Result(-1, "Essa matrícula já foi cancelada");
			}
			
			if(matricula.getStMatricula() != ST_ATIVA && matricula.getStMatricula() != ST_PENDENTE){
				return new Result(-1, "Somente matrículas Ativas ou Pendentes podem ser canceladas");
			}
			
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			
			//Impede alterações em escolas que estão offline, impedindo inconsistencias
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem cancelar matriculas");
			}
			
			if(txtJustificativa.contains("TRANSFERENCIA")
			  || txtJustificativa.contains("TRANSFERÊNCIA")
			  || txtJustificativa.contains("Transferencia")
			  || txtJustificativa.contains("Transferência")
			  || txtJustificativa.contains("transferencia")
			  || txtJustificativa.contains("transferência")
			  || txtJustificativa.contains("TRANSFERIDO")
			  || txtJustificativa.contains("Transferido")
			  || txtJustificativa.contains("transferido")
			  || txtJustificativa.contains("TRANSFERIDA")
			  || txtJustificativa.contains("Transferida")
			  || txtJustificativa.contains("transferida")){
				return new Result(-1, "Caso o aluno tenha sido transferido, utilize o recurso de solicitação de transferência");
			}
			
			if(txtJustificativa.contains("FALECEU")
			  || txtJustificativa.contains("Faleceu")
			  || txtJustificativa.contains("faleceu")
			  || txtJustificativa.contains("MORREU")
			  || txtJustificativa.contains("Morreu")
			  || txtJustificativa.contains("morreu")
			  || txtJustificativa.contains("MORTO")
			  || txtJustificativa.contains("Morto")
			  || txtJustificativa.contains("morto")
			  || txtJustificativa.contains("OBITO")
			  || txtJustificativa.contains("Obito")
			  || txtJustificativa.contains("obito")
			  || txtJustificativa.contains("ÓBITO")
			  || txtJustificativa.contains("Óbito")
			  || txtJustificativa.contains("óbito")
			  || txtJustificativa.contains("FALECIMENTO")
			  || txtJustificativa.contains("Falecimento")
			  || txtJustificativa.contains("falecimento")){
				return new Result(-1, "Caso o aluno tenha vindo a falecer, utilize a mudança de status correspondente");
			}
			
			if(txtJustificativa.contains("DESISTENCIA")
			  || txtJustificativa.contains("Desistencia")
			  || txtJustificativa.contains("desistencia")
			  || txtJustificativa.contains("DESISTÊNCIA")
			  || txtJustificativa.contains("Desistência")
			  || txtJustificativa.contains("desistência")
			  || txtJustificativa.contains("DESISTENTE")
			  || txtJustificativa.contains("Desistente")
			  || txtJustificativa.contains("desistente")){
				return new Result(-1, "Caso o aluno tenha vindo a desistir, utilize a mudança de status correspondente");
			}
			
			if(txtJustificativa.contains("EVADIDO")
			  || txtJustificativa.contains("Evadido")
			  || txtJustificativa.contains("evadido")
			  || txtJustificativa.contains("EVADIU")
			  || txtJustificativa.contains("Evadiu")
			  || txtJustificativa.contains("evadiu")){
				return new Result(-1, "Caso o aluno tenha vindo a evadir, utilize a mudança de status correspondente");
			}
			
			
			int stMatriculaOrigem = matricula.getStMatricula();
			
			matricula.setStMatricula(ST_CANCELADO);
			int ret = MatriculaDAO.update(matricula, connect); 
			if(ret <= 0){
				return new Result(-1, "Erro ao atualizar matrícula");
			}
			
			
			int cdTipoOcorrenciaDesistente = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CANCELAMENTO_MATRICULA, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaDesistente > 0){
				Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Matrícula do aluno " + aluno.getNmPessoa() + " cancelada. Motivo: " + txtJustificativa, Util.getDataAtual(), cdTipoOcorrenciaDesistente, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), stMatriculaOrigem, cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de confirmação de matrícula");
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Cancelamento de matrícula realizado com sucesso");
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			result.addObject("rsm", rsm);
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace();
			return new Result(-1, "Erro no cancelamento");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que realiza a inativação de um aluno
	 * @param matricula
	 * @param cdUsuario
	 * @return
	 */
	public static Result saveInativacao(Matricula matricula, int cdUsuario){
		return saveInativacao(matricula, cdUsuario, true, null);
	}
	
	public static Result saveInativacao(Matricula matricula, int cdUsuario, Connection connect){
		return saveInativacao(matricula, cdUsuario, true, connect);
	}
	public static Result saveInativacao(Matricula matricula, int cdUsuario, boolean lgCarregarMatricula){
		return saveInativacao(matricula, cdUsuario, lgCarregarMatricula, null);
	}
	
	public static Result saveInativacao(Matricula matricula, int cdUsuario, boolean lgCarregarMatricula, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(matricula.getStMatricula() == ST_INATIVO){
				return new Result(-1, "Esse aluno já foi considerado inativo nessa matrícula");
			}
			
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem inativar matriculas");
			}
			
			int stMatriculaOrigem = matricula.getStMatricula();
			
			matricula.setStMatricula(ST_INATIVO);
			int ret = MatriculaDAO.update(matricula, connect); 
			if(ret <= 0){
				return new Result(-1, "Erro ao atualizar matrícula");
			}
			
			
			int cdTipoOcorrenciaInativo = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_INATIVO, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaInativo > 0){
				Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " considerado inativo", Util.getDataAtual(), cdTipoOcorrenciaInativo, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), stMatriculaOrigem, cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de inatividade de matrícula");
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Inativação realizada com sucesso");
			if(lgCarregarMatricula){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.cd_matricula", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("stInativacao", "" + 1, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsm = find(criterios, connect);
				result.addObject("rsm", rsm);
			}
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace();
			return new Result(-1, "Erro na desistencia");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que realiza o fechamento da matrícula de um aluno
	 * @param matricula
	 * @param cdUsuario
	 * @return
	 */
	public static Result saveFechada(Matricula matricula, int cdUsuario){
		return saveFechada(matricula, cdUsuario, true, null);
	}
	
	public static Result saveFechada(Matricula matricula, int cdUsuario, Connection connect){
		return saveFechada(matricula, cdUsuario, true, connect);
	}
	public static Result saveFechada(Matricula matricula, int cdUsuario, boolean lgCarregarMatricula){
		return saveFechada(matricula, cdUsuario, lgCarregarMatricula, null);
	}
	
	public static Result saveFechada(Matricula matricula, int cdUsuario, boolean lgCarregarMatricula, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(matricula.getStMatricula() == ST_FECHADA){
				return new Result(-1, "Esse aluno já foi considerado com matrícula fechada");
			}
			
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem fechar matriculas");
			}
			
			int stMatriculaOrigem = matricula.getStMatricula();
			
			matricula.setStMatricula(ST_FECHADA);
			int ret = MatriculaDAO.update(matricula, connect); 
			if(ret <= 0){
				return new Result(-1, "Erro ao atualizar matrícula");
			}
			
			
			int cdTipoOcorrenciaFechada = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_FECHADA, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaFechada > 0){
				Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " considerado com matricula fechada", Util.getDataAtual(), cdTipoOcorrenciaFechada, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), stMatriculaOrigem, cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de fechamento de matrícula");
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Fechamento de matrícula realizado com sucesso");
			if(lgCarregarMatricula){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.cd_matricula", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsm = find(criterios, connect);
				result.addObject("rsm", rsm);
			}
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace();
			return new Result(-1, "Erro no fechamento de matricula");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que realiza a alteração do numero de uma matrícula, o número anterior é salvo no texto da ocorrência 
	 * @param matricula
	 * @param nrNovoNumero
	 * @param cdUsuario
	 * @return
	 */
	public static Result saveAlterarNumero(Matricula matricula, String nrNovoNumero, int cdUsuario){
		return saveAlterarNumero(matricula, nrNovoNumero, cdUsuario, null, null);
	}
	
	public static Result saveAlterarNumero(Matricula matricula, String nrNovoNumero, int cdUsuario, Connection connect){
		return saveAlterarNumero(matricula, nrNovoNumero, cdUsuario, null, connect);
	}
	public static Result saveAlterarNumero(Matricula matricula, String nrNovoNumero, int cdUsuario, AuthData authData){
		return saveAlterarNumero(matricula, nrNovoNumero, cdUsuario, authData, null);
	}
	
	public static Result saveAlterarNumero(Matricula matricula, String nrNovoNumero, int cdUsuario, AuthData authData, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem alterar o numero de matricula");
			}
			
			//COMPLIANCE
			int tpAcao = ComplianceManager.TP_ACAO_UPDATE;
			
			//LOG
			String idAcao = "";
			if(authData!=null)
				idAcao = authData.getIdAcaoUpdate();
			Matricula oldValue = MatriculaDAO.get(matricula.getCdMatricula(), connect);
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nr_matricula", nrNovoNumero, ItemComparator.EQUAL, Types.VARCHAR));
			criterios.add(new ItemComparator("cd_aluno", "" + matricula.getCdAluno(), ItemComparator.DIFFERENT, Types.INTEGER));
			if(MatriculaDAO.find(criterios, connect).next()){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Número de matrícula já cadastrado para outro aluno");
			}
		
			String nrAntigoNumero = matricula.getNrMatricula();
			
			matricula.setNrMatricula(nrNovoNumero);
			int ret = MatriculaDAO.update(matricula, connect); 
			if(ret <= 0){
				return new Result(-1, "Erro ao atualizar matrícula");
			}
			
			
			int stMatriculaOrigem = matricula.getStMatricula();
			
			int cdTipoOcorrenciaAlterarNumeroMatricula = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERAR_NUMERO_MATRICULA, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaAlterarNumeroMatricula > 0){
				Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				
				
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " teve seu número de matrícula alterado de " +nrAntigoNumero+ " para " +matricula.getNrMatricula(), Util.getDataAtual(), cdTipoOcorrenciaAlterarNumeroMatricula, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), stMatriculaOrigem, cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de alteração do numero de matrícula");
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			//COMPLIANCE
			Result result = ComplianceManager.process("saveCompliance", matricula, authData, tpAcao, connect);
			//LOG
			result = LogServices.log(tpAcao, idAcao, authData, matricula, oldValue);
			
			result = new Result(1, "Alteração do número de matrícula realizada com sucesso");
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			result.addObject("rsm", rsm);
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace();
			return new Result(-1, "Erro na desistencia");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 *  Coloca como transferido um conjunto de matrículas
	 * @param matriculas
	 * @param cdUsuario
	 * @return
	 */
	public static Result saveTransferido(ArrayList<Integer> matriculas, int cdUsuario){
		return saveTransferido(matriculas, cdUsuario, null);
	}
	
	public static Result saveTransferido(ArrayList<Integer> matriculas, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			for(Integer cdMatricula : matriculas){
				Result resultMatricula = saveTransferido(MatriculaDAO.get(cdMatricula, connect), cdUsuario, connect);
				if(resultMatricula.getCode() < 0){
					return resultMatricula;
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Ocorrencia de Transferidos realizada com sucesso");
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace();
			return new Result(-1, "Erro na transferência");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Coloca como transferido uma matrícula
	 * @param matricula
	 * @param cdUsuario
	 * @return
	 */
	public static Result saveTransferido(Matricula matricula, int cdUsuario){
		return saveTransferido(matricula, cdUsuario, null);
	}
	
	public static Result saveTransferido(Matricula matricula, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(matricula.getStMatricula() == ST_TRANSFERIDO){
				return new Result(-1, "Esse aluno já foi considerado transferido nessa matrícula");
			}
			
			int stMatriculaOrigem = matricula.getStMatricula();
			
			matricula.setStMatricula(ST_TRANSFERIDO);
			int ret = MatriculaDAO.update(matricula, connect); 
			if(ret <= 0){
				return new Result(-1, "Erro ao atualizar matrícula");
			}
			
			
			int cdTipoOcorrenciaInativo = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_TRANSFERIDO, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaInativo > 0){
				Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
				
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " transferido", Util.getDataAtual(), cdTipoOcorrenciaInativo, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), stMatriculaOrigem, cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de matricula transferida");
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Ocorrencia de Transferido realizada com sucesso");
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			result.addObject("rsm", rsm);
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace();
			return new Result(-1, "Erro na transferência");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que realiza a transferência de um aluno, criando uma matrícula nova quando for em outra instituição, e apenas atualizando a matrícula antiga, quando o aluno retornar à propria escola
	 * @param matriculaAntiga
	 * @param cdUsuario
	 * @param cdPeriodoLetivo
	 * @param cdTurma
	 * @return
	 */
	public static Result saveTransferencia(Matricula matriculaAntiga, int cdUsuario, int cdPeriodoLetivo, int cdTurma, boolean lancarIdadeDivergente){
		return saveTransferencia(matriculaAntiga, cdUsuario, cdPeriodoLetivo, cdTurma, lancarIdadeDivergente, null);
	}

	public static Result saveTransferencia(Matricula matriculaAntiga, int cdUsuario, int cdPeriodoLetivo, int cdTurma, boolean lancarIdadeDivergente, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(matriculaAntiga.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0) ||
			   matriculaAntiga.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Matrículas do Mais Educação e de AEE não podem ser transferidas");
			}
			
			if(matriculaAntiga.getStMatricula() != ST_EM_TRANSFERENCIA){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Transferência não pode ser realizada para matrículas que não estejam Em Transferência");
			}
			
			Turma turma = TurmaDAO.get(cdTurma, connect);
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem receber alunos de transferencia");
			}
			
			Curso cursoMatriculaAntiga = CursoDAO.get(matriculaAntiga.getCdCurso(), connect);
			
			//Ao se transferir um aluno, é criado um novo registro baseado no antigo (para manutenção de histórico)
			Matricula matriculaNova = (Matricula)matriculaAntiga.clone();
			
			InstituicaoPeriodo instituicaoPeriodoMatricula = InstituicaoPeriodoDAO.get(matriculaNova.getCdPeriodoLetivo(), connect);
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			int cdPeriodoLetivoSecretaria = 0;
			ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdSecretaria, connect);
			if(rsmPeriodoRecente.next()){
				cdPeriodoLetivoSecretaria = rsmPeriodoRecente.getInt("cd_periodo_letivo");
			}
			
			//Apenas usuários ligados à secretaria podem realizar essa ação
			if(!UsuarioServices.isAdmSecretariaEducacao(cdUsuario, connect) && instituicaoPeriodoMatricula.getCdPeriodoLetivoSuperior() != cdPeriodoLetivoSecretaria){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Transferência não pode ser realizada para o periodo de " + instituicaoPeriodoMatricula.getNmPeriodoLetivo());
			}
			
			//Alguns campos são alterados para iniciar o novo registro de matricula
			matriculaNova.setCdMatricula(0);
			matriculaNova.setDtMatricula(Util.getDataAtual());
			matriculaNova.setStMatricula(MatriculaServices.ST_ATIVA);
			matriculaNova.setStAlunoCenso(ST_ALUNO_CENSO_APROVADO);
			matriculaNova.setCdMatriculaOrigem(matriculaAntiga.getCdMatricula());
			matriculaNova.setCdPeriodoLetivo(cdPeriodoLetivo);
			matriculaNova.setCdTurma(cdTurma);
			matriculaNova.setNrMatricula(matriculaAntiga.getNrMatricula());
			matriculaNova.setLgTransportePublico(0);
			
			Pessoa aluno = PessoaDAO.get(matriculaNova.getCdAluno(), connect);
			Turma turmaAntiga = TurmaDAO.get(matriculaAntiga.getCdTurma(), connect);
			Curso cursoTurmaAntiga = CursoDAO.get(turmaAntiga.getCdCurso(), connect);
			Pessoa instituicaoAntiga = PessoaDAO.get(turmaAntiga.getCdInstituicao(), connect);
			Turma turmaNova = TurmaDAO.get(matriculaNova.getCdTurma(), connect);
			Curso cursoTurmaNova = CursoDAO.get(turmaNova.getCdCurso(), connect);
			Curso cursoMatriculaNova = CursoDAO.get(matriculaNova.getCdCurso(), connect);
			
			//Coloca a escola antiga como ultima escola da matricula nova
			matriculaNova.setNmUltimaEscola(instituicaoAntiga.getNmPessoa());
			
			//Caso a turma não seja de um curso multi, o curso da matrícula receberá o mesmo curso da turma
			if(cursoTurmaNova.getLgMulti() == 0)
				matriculaNova.setCdCurso(turmaNova.getCdCurso());
			else
				matriculaNova.setCdCurso(CursoServices.getCursoCompativel(turmaNova, matriculaAntiga, connect));
			
			
			Pessoa instituicaoNova = PessoaDAO.get(turmaNova.getCdInstituicao(), connect);
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(turmaNova.getCdPeriodoLetivo(), connect);
			int cdTipoOcorrenciaTransferencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CONCLUSAO_TRANSFERENCIA, connect).getCdTipoOcorrencia();
			
			String txtDescricao = "";
			
			//Caso a instituição ed transferencia seja diferente, a matricula antiga será modificada e considerada "Transferida"
			if(instituicaoAntiga.getCdPessoa() != instituicaoNova.getCdPessoa()){
				matriculaAntiga.setStMatricula(ST_TRANSFERIDO);
				matriculaAntiga.setStAlunoCenso(ST_ALUNO_CENSO_TRANSFERIDO);
				matriculaAntiga.setDtConclusao(Util.getDataAtual());
				int result = MatriculaDAO.update(matriculaAntiga, connect);
				if(result <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar matrícula antiga");
				}
				
				
				Result resultado = save(matriculaNova, 0, cdUsuario, lancarIdadeDivergente, true, false, connect);
				if(resultado.getCode() <= 0 && resultado.getCode() != ERR_ALUNO_FORA_IDADE){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return resultado;
				}
				
				
				txtDescricao = "Conclusão de transferência do aluno " + aluno.getNmPessoa() + ", da instituição " + instituicaoAntiga.getNmPessoa() + ", turma "+cursoTurmaAntiga.getNmProdutoServico() + " - " + turmaAntiga.getNmTurma() + " da modalidade "+cursoMatriculaAntiga.getNmProdutoServico()+" para a instituição " + instituicaoNova.getNmPessoa()+  ", turma "+cursoTurmaNova.getNmProdutoServico() + " - " + turmaNova.getNmTurma()+" da modalidade "+cursoMatriculaNova.getNmProdutoServico()+" no período letivo de " + instituicaoPeriodo.getNmPeriodoLetivo();
			}
			//Caso a instituicao de transferencia for a mesma, a matrícula é apenas atualizada
			else{
				matriculaAntiga.setStMatricula(MatriculaServices.ST_ATIVA);
				matriculaAntiga.setStAlunoCenso(ST_ALUNO_CENSO_APROVADO);
				matriculaAntiga.setCdTurma(cdTurma);
				if(matriculaAntiga.getDtMatricula() == null){
					matriculaAntiga.setDtMatricula(new GregorianCalendar());
				}
					
				turmaNova = TurmaDAO.get(cdTurma, connect);
				if(cursoTurmaNova.getLgMulti() == 0)
					matriculaAntiga.setCdCurso(turmaNova.getCdCurso());
				else
					matriculaAntiga.setCdCurso(CursoServices.getCursoCompativel(turmaNova, matriculaAntiga, connect));
				
				Result result = save(matriculaAntiga, 0, cdUsuario, lancarIdadeDivergente, true, false, connect);
				if(result.getCode() <= 0 && result.getCode() != ERR_ALUNO_FORA_IDADE){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
				
				txtDescricao = "Conclusão de transferência do aluno " + aluno.getNmPessoa() + ", da instituição " + instituicaoAntiga.getNmPessoa() + ", turma "+cursoTurmaAntiga.getNmProdutoServico() + " - " + turmaAntiga.getNmTurma() + " da modalidade "+cursoMatriculaAntiga.getNmProdutoServico()+ " para a turma "+cursoTurmaNova.getNmProdutoServico() + " - " + turmaNova.getNmTurma()+" da modalidade "+cursoMatriculaNova.getNmProdutoServico()+" no período letivo de " + instituicaoPeriodo.getNmPeriodoLetivo();
			}
			
			//A situação original é guardada para caso for necessário retoma-la
			int stMatriculaOrigem = matriculaAntiga.getStMatricula();
			OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matriculaNova.getCdAluno(), txtDescricao, Util.getDataAtual(), 
																	cdTipoOcorrenciaTransferencia, OcorrenciaServices.ST_CONCLUIDO, 0, 
																	cdUsuario, matriculaAntiga.getCdMatricula(), matriculaNova.getCdMatricula(), 
																	turmaAntiga.getCdTurma(), turmaNova.getCdTurma(), stMatriculaOrigem, cdUsuario);
			OcorrenciaMatriculaServices.save(ocorrencia, connect);
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Conclusão de transferência realizada com sucesso");
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula", "" + (instituicaoAntiga.getCdPessoa() != instituicaoNova.getCdPessoa() ? matriculaNova.getCdMatricula() : matriculaAntiga.getCdMatricula()), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			result.addObject("MATRICULA", (matriculaNova.getCdMatricula() > 0 ? matriculaNova : matriculaAntiga));
			result.addObject("rsm", rsm);
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na conclusão de transferência");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result cancelamentoDesistenciaEvasao(int cdMatricula, int cdUsuario){
		return cancelamentoDesistenciaEvasao(cdMatricula, cdUsuario, null);
	}

	public static Result cancelamentoDesistenciaEvasao(int cdMatricula, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Matricula matricula = MatriculaDAO.get(cdMatricula, connect);

			if(matricula.getStMatricula() != MatriculaServices.ST_DESISTENTE && matricula.getStMatricula() != MatriculaServices.ST_EVADIDO) {
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Apenas matrícula Desistentes e Evadidas podem ter esse cancelamento");
			}
			
			//Apenas o usuário de suporte pode fazer matrículas fora do periodo letivo
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(turma.getCdInstituicao(), connect);
			rsmPeriodoAtual.next();
			if(!UsuarioServices.isSuporte(cdUsuario, connect) && Integer.parseInt(InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect).getNmPeriodoLetivo()) < Integer.parseInt(InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect).getNmPeriodoLetivo())){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Não é possível realizar essa ação para matrícula de períodos anteriores");
			}
			
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);
			
			//Verificação para impedir usuários de trabalharem em escolas que estão offline, criando possíveis inconsistências
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem fazer ativacao de matricula");
			}
			
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect);
			
			
			int stMatriculaOrigem = 0;
			int stAlunoCensoAntigo = 0;
			
			if(instituicaoPeriodo.getStPeriodoLetivo() == InstituicaoPeriodoServices.ST_CONCLUIDO) {
				stMatriculaOrigem = matricula.getStMatricula();
				matricula.setStMatricula(ST_CONCLUIDA);
				stAlunoCensoAntigo = matricula.getStAlunoCenso();
				matricula.setStAlunoCenso(ST_ALUNO_CENSO_APROVADO);
			}
			else {
				stMatriculaOrigem = matricula.getStMatricula();
				matricula.setStMatricula(ST_ATIVA);
				stAlunoCensoAntigo = matricula.getStAlunoCenso();
				matricula.setStAlunoCenso(ST_ALUNO_CENSO_APROVADO);
			}
			
			//Atualiza a data da matricula para a data em que foi 'Ativada'
			GregorianCalendar dtHoje = Util.getDataAtual();
			dtHoje.set(Calendar.HOUR_OF_DAY, 12);
			dtHoje.set(Calendar.MINUTE, 0);
			dtHoje.set(Calendar.SECOND, 0);
			matricula.setDtMatricula(dtHoje);
			
			Result ret = MatriculaServices.save(matricula, cdUsuario, true, true, -1, connect);
			if(ret.getCode() <= 0){
				return ret;
			}

			Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
			
			//Registra as ocorrencias da ativação da matrícula
			int cdTipoOcorrencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(String.valueOf(InstituicaoEducacensoServices.TP_OCORRENCIA_CANCELAMENTO_DESISTENCIA_EVASAO), connect).getCdTipoOcorrencia();
			if(cdTipoOcorrencia > 0){
				
				if(aluno == null){
					aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				}
				
				String txtOcorrencia = "Cancelada desistência/evasão do aluno " + aluno.getNmPessoa() + " no período letivo de " + instituicaoPeriodo.getNmPeriodoLetivo();
				
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), txtOcorrencia, Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), stMatriculaOrigem, cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de confirmação de matrícula");
				}
			}
			
			int cdTipoOcorrenciaAlteracaoSituacaoCenso = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_SITUACAO_CENSO, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaAlteracaoSituacaoCenso > 0){
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " alterado situação do Censo de " + situacaoAlunoCenso[stAlunoCensoAntigo] + " para " + situacaoAlunoCenso[matricula.getStAlunoCenso()] + " para o período letivo de " + instituicaoPeriodo.getNmPeriodoLetivo(), Util.getDataAtual(), cdTipoOcorrenciaAlteracaoSituacaoCenso, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), matricula.getCdTurma(), matricula.getCdTurma(), matricula.getStMatricula(), cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de situacao do censo");
				}
			}
			
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Cancelamento de desistência/evasão feita com sucesso");
			return result;
		}
		
		catch(Exception e){
		
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace();
			return new Result(-1, "Erro no cancelamento de desistência/evasão");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	

	
	/**
	 * Método que realiza o cancelamento de uma conclusão de transferência, fazendo com que o aluno volte ao estado de Em transferência
	 * @param cdMatricula
	 * @param cdUsuario
	 * @return
	 */
	public static Result cancelamentoRecebimento(int cdMatricula, int cdUsuario){
		return cancelamentoRecebimento(cdMatricula, cdUsuario, null);
	}

	public static Result cancelamentoRecebimento(int cdMatricula, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Matricula matricula = MatriculaDAO.get(cdMatricula, connect);
			
			if(matricula.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0) ||
					matricula.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Matrículas do Mais Educação e de AEE não têm cancelamento de recebimento");
			}
			
			if(matricula.getStMatricula() != ST_ATIVA){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Cancelamento de transferência só pode ser realizada em matrículas ativas");
			}
			
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem fazer cancelamento de recebimento");
			}
			
			InstituicaoPeriodo instituicaoPeriodoMatricula = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect);
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			int cdPeriodoLetivoSecretaria = 0;
			ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdSecretaria, connect);
			if(rsmPeriodoRecente.next()){
				cdPeriodoLetivoSecretaria = rsmPeriodoRecente.getInt("cd_periodo_letivo");
			}
			
			if(!UsuarioServices.isAdmSecretariaEducacao(cdUsuario, connect) && instituicaoPeriodoMatricula.getCdPeriodoLetivoSuperior() != cdPeriodoLetivoSecretaria){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Cancelamento de recebimento não pode ser realizado para o periodo de " + instituicaoPeriodoMatricula.getNmPeriodoLetivo());
			}
			
			
			Matricula matriculaAnterior = MatriculaDAO.get(matricula.getCdMatriculaOrigem(), connect);
			
			if(matriculaAnterior.getStMatricula() != ST_TRANSFERIDO){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "O cancelamento so pode ser realizado se a matricula anterior estiver como TRANSFERIDA");
			}
					
			
			matricula.setStMatricula(ST_INATIVO);
			int ret = MatriculaDAO.update(matricula, connect);
			if(ret < 0){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Erro ao atualizar matricula");
			}
			
			matriculaAnterior.setStMatricula(ST_EM_TRANSFERENCIA);
			ret = MatriculaDAO.update(matriculaAnterior, connect);
			if(ret < 0){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Erro ao atualizar matricula");
			}
			
			int cdTipoOcorrenciaTransferencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CONCLUSAO_TRANSFERENCIA, connect).getCdTipoOcorrencia();
			
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + cdTipoOcorrenciaTransferencia, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_matricula_origem", "" + matriculaAnterior.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_matricula_destino", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmOcorrencia = OcorrenciaMatriculaServices.find(criterios, connect);
			if(rsmOcorrencia.next()){
				Result resultado = OcorrenciaMatriculaServices.remove(rsmOcorrencia.getInt("cd_ocorrencia"), true, connect);
				if(resultado.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return resultado;	
				}
			}
			
			int cdTipoOcorrenciaCadastroMatricula = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_MATRICULA, connect).getCdTipoOcorrencia();
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + cdTipoOcorrenciaCadastroMatricula, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_matricula_origem", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_matricula_destino", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			rsmOcorrencia = OcorrenciaMatriculaServices.find(criterios, connect);
			if(rsmOcorrencia.next()){
				Result resultado = OcorrenciaMatriculaServices.remove(rsmOcorrencia.getInt("cd_ocorrencia"), true, connect);
				if(resultado.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return resultado;	
				}
			}
			
					
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Cancelamento de recebimento feito com sucesso");
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na conclusão de transferência");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	/**
	 * Método para fazer uma transferência direta de uma instituição para outra. Só pode ser realizada pela Secretaria. Utiliza o metodo de solicitação
	 * de transferencia, porém repete (necessário analise de até que ponto) o metodo de saveTransferencia
	 * @param matriculaAntiga
	 * @param cdUsuario
	 * @param cdPeriodoLetivo
	 * @param cdCurso
	 * @param cdTurma
	 * @return
	 */
	public static Result saveTransferenciaDireta(Matricula matriculaAntiga, int cdUsuario, int cdPeriodoLetivo, int cdCurso, int cdTurma){
		return saveTransferenciaDireta(matriculaAntiga, cdUsuario, cdPeriodoLetivo, cdCurso, cdTurma, null);
	}
	
	public static Result saveTransferenciaDireta(Matricula matriculaAntiga, int cdUsuario, int cdPeriodoLetivo, int cdCurso, int cdTurma, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Result result = saveSolicitacaoTransferencia(matriculaAntiga, cdUsuario, true, connect);
			if(result.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}
			
			if(matriculaAntiga.getStMatricula() != ST_EM_TRANSFERENCIA){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Transferência não pode ser realizada para matrículas que não estejam Em Transferência");
			}
			
			Turma turma = TurmaDAO.get(cdTurma, connect);
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem receber alunos de transferencia");
			}
			
			Matricula matriculaNova = (Matricula)matriculaAntiga.clone();
			
			InstituicaoPeriodo instituicaoPeriodoMatricula = InstituicaoPeriodoDAO.get(matriculaNova.getCdPeriodoLetivo(), connect);
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			int cdPeriodoLetivoSecretaria = 0;
			ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdSecretaria, connect);
			if(rsmPeriodoRecente.next()){
				cdPeriodoLetivoSecretaria = rsmPeriodoRecente.getInt("cd_periodo_letivo");
			}
			
			matriculaNova.setCdMatricula(0);
			matriculaNova.setDtMatricula(Util.getDataAtual());
			matriculaNova.setStMatricula(MatriculaServices.ST_ATIVA);
			matriculaNova.setStAlunoCenso(ST_ALUNO_CENSO_APROVADO);
			matriculaNova.setCdMatriculaOrigem(matriculaAntiga.getCdMatricula());
			matriculaNova.setCdPeriodoLetivo(cdPeriodoLetivo);
			matriculaNova.setCdCurso(cdCurso);
			matriculaNova.setCdTurma(cdTurma);
			matriculaNova.setNrMatricula(matriculaAntiga.getNrMatricula());
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_curso", "" + cdCurso, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmCursoMatriz = CursoMatrizDAO.find(criterios, connect);
			if(rsmCursoMatriz.next())
				matriculaNova.setCdMatriz(rsmCursoMatriz.getInt("cd_matriz"));
			
			Pessoa aluno = PessoaDAO.get(matriculaNova.getCdAluno(), connect);
			Turma turmaAntiga = TurmaDAO.get(matriculaAntiga.getCdTurma(), connect);
			Pessoa instituicaoAntiga = PessoaDAO.get(turmaAntiga.getCdInstituicao(), connect);
			Turma turmaNova = TurmaDAO.get(matriculaNova.getCdTurma(), connect);
			Curso cursoTurmaNova = CursoDAO.get(turmaNova.getCdCurso(), connect);
			Curso cursoMatriculaNova = CursoDAO.get(matriculaNova.getCdCurso(), connect);
			
			//Coloca na matricula nova o nome da ultima escola pela matricula antiga
			matriculaNova.setNmUltimaEscola(instituicaoAntiga.getNmPessoa());
			
			if(!TurmaServices.verificarCompatibilidadeTurmaMatricula(cursoTurmaNova, cursoMatriculaNova, connect)){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Turma não compatível com a matrícula");
			}
			
			//Faz a verificação para saber se a turma escolhida para matricular o aluno é compatível com a turma em que ele já estava matriculado
			if(!CursoServices.verificarCorrespondenciMatricula(matriculaAntiga.getCdCurso(), cursoMatriculaNova.getCdCurso(), connect)){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "O curso escolhido não tem correspondência com o anterior");
			}
			
			Pessoa instituicaoNova = PessoaDAO.get(turmaNova.getCdInstituicao(), connect);
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(turmaNova.getCdPeriodoLetivo(), connect);
			int cdTipoOcorrenciaTransferencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CONCLUSAO_TRANSFERENCIA, connect).getCdTipoOcorrencia();
			
			String txtDescricao = "";
			
			if(instituicaoAntiga.getCdPessoa() != instituicaoNova.getCdPessoa()){
				
				matriculaAntiga.setStMatricula(ST_TRANSFERIDO);
				matriculaAntiga.setStAlunoCenso(ST_ALUNO_CENSO_TRANSFERIDO);
				matriculaAntiga.setDtConclusao(Util.getDataAtual());
				if(MatriculaDAO.update(matriculaAntiga, connect) <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar matrícula antiga");
				}
				
				Result resultado = save(matriculaNova, 0, cdUsuario, true, connect);
				if(resultado.getCode() <= 0 && result.getCode() != ERR_ALUNO_FORA_IDADE){
					return resultado;
				}
				
				txtDescricao = "Conclusão de transferência do aluno " + aluno.getNmPessoa() + ", da instituição " + instituicaoAntiga.getNmPessoa() + ", turma "+turmaAntiga.getNmTurma() + " para a instituição " + instituicaoNova.getNmPessoa()+  ", turma "+turmaNova.getNmTurma()+" no período letivo de " + instituicaoPeriodo.getNmPeriodoLetivo();
			}
			else{
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Não é possível fazer uma transferência direta para dentro da própria Instituição");
			}
			
			int stMatriculaOrigem = matriculaAntiga.getStMatricula();
			
			OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matriculaNova.getCdAluno(), txtDescricao, Util.getDataAtual(), cdTipoOcorrenciaTransferencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matriculaAntiga.getCdMatricula(), matriculaNova.getCdMatricula(), turmaAntiga.getCdTurma(), turmaNova.getCdTurma(), stMatriculaOrigem, cdUsuario);
			OcorrenciaMatriculaServices.save(ocorrencia, connect);
			
			if(isConnectionNull)
				connect.commit();
			
			result = new Result(1, "Conclusão de transferência realizada com sucesso");
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula", "" + (instituicaoAntiga.getCdPessoa() != instituicaoNova.getCdPessoa() ? matriculaNova.getCdMatricula() : matriculaAntiga.getCdMatricula()), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			result.addObject("rsm", rsm);
			result.addObject("MATRICULA", (matriculaNova.getCdMatricula() > 0 ? matriculaNova : matriculaAntiga));
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace();
			return new Result(-1, "Erro na conclusão de transferência");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Faz uma solicitação de transferência, alterando a matrícula para a situação e transferência, "tirando-a" da turma e fazendo-a aparecer na pesquisa de conclusão de transferência
	 * @param matricula
	 * @param cdUsuario
	 * @return
	 */
	public static Result saveSolicitacaoTransferencia(Matricula matricula, int cdUsuario){
		return saveSolicitacaoTransferencia(matricula, cdUsuario, false, null);
	}
	
	public static Result saveSolicitacaoTransferencia(Matricula matricula, int cdUsuario, Connection connect){
		return saveSolicitacaoTransferencia(matricula, cdUsuario, false, connect);
	}
	
	public static Result saveSolicitacaoTransferencia(Matricula matricula, int cdUsuario, boolean transferenciaDireta){
		return saveSolicitacaoTransferencia(matricula, cdUsuario, transferenciaDireta, null);
	}
	
	public static Result saveSolicitacaoTransferencia(Matricula matricula, int cdUsuario, boolean transferenciaDireta, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(matricula.getStMatricula() != MatriculaServices.ST_ATIVA && matricula.getStMatricula() != MatriculaServices.ST_PENDENTE && matricula.getStMatricula() != MatriculaServices.ST_FECHADA){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Solicitações de transferência não podem ser realizados para matrículas que não estejam Ativas, Pendentes ou Fechadas");
			}
			
			ResultSetMap rsmTurmaSituacaoAlunoCenso = new ResultSetMap(connect.prepareStatement("SELECT cd_tipo_ocorrencia FROM grl_ocorrencia A, acd_ocorrencia_turma B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO + ") AND cd_turma = "+matricula.getCdTurma()+" order by dt_ocorrencia DESC").executeQuery());
			if(rsmTurmaSituacaoAlunoCenso.next()){
				if(rsmTurmaSituacaoAlunoCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO)){
					return new Result(-1, "Não é mais possível mudar o status dessa matrícula, pois a sua turma já foi fechada em relação a Situação do Aluno para o Censo");
				}
			}
			
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);			
			if(!transferenciaDireta && instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem solicitar transferencia");
			}
			
			
			InstituicaoPeriodo instituicaoPeriodoMatricula = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect);
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			int cdPeriodoLetivoSecretaria = 0;
			ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdSecretaria, connect);
			if(rsmPeriodoRecente.next()){
				cdPeriodoLetivoSecretaria = rsmPeriodoRecente.getInt("cd_periodo_letivo");
			}
			
			if(!UsuarioServices.isAdmSecretariaEducacao(cdUsuario, connect) && instituicaoPeriodoMatricula.getCdPeriodoLetivoSuperior() != cdPeriodoLetivoSecretaria){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Transferência não pode ser realizada para o periodo de " + instituicaoPeriodoMatricula.getNmPeriodoLetivo());
			}
			
			if(matricula.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0) ||
			   matricula.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Matrículas do Mais Educação e de AEE não podem ser transferidas");
			}
			
			int stMatriculaOrigem = matricula.getStMatricula();
			
			matricula.setStMatricula(ST_EM_TRANSFERENCIA);
			int stAlunoCensoAntigo = matricula.getStAlunoCenso();
			matricula.setStAlunoCenso(ST_ALUNO_CENSO_TRANSFERIDO);
			
			int result = MatriculaDAO.update(matricula, connect);
			
			if(result <= 0){
				return new Result(-1, "Erro ao atualizar matricula de transferência");
			}
			
			Pessoa aluno = PessoaDAO.get(matricula.getCdAluno(), connect);
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(turma.getCdPeriodoLetivo(), connect);
			
			if(instituicaoPeriodo.getStPeriodoLetivo() == InstituicaoPeriodoServices.ST_CONCLUIDO){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Matrícula pertence a um periodo letivo concluído, portanto não pode ser transferida");
			}
			int cdTipoOcorrenciaTransferencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_SOLICITACAO_TRANSFERENCIA, connect).getCdTipoOcorrencia();
			
			OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Solicitação de transferência do aluno " + aluno.getNmPessoa() + ", da instituição " + instituicao.getNmPessoa() + " no período letivo de " + instituicaoPeriodo.getNmPeriodoLetivo(), Util.getDataAtual(), cdTipoOcorrenciaTransferencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), 0, turma.getCdTurma(), 0, stMatriculaOrigem, cdUsuario);
			
			OcorrenciaMatriculaServices.save(ocorrencia, connect);
			
			int cdTipoOcorrenciaAlteracaoSituacaoCenso = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_SITUACAO_CENSO, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaAlteracaoSituacaoCenso > 0){
				
				ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " alterado situação do Censo de " + situacaoAlunoCenso[stAlunoCensoAntigo] + " para " + situacaoAlunoCenso[matricula.getStAlunoCenso()] + " para o período letio de " + instituicaoPeriodo.getNmPeriodoLetivo(), Util.getDataAtual(), cdTipoOcorrenciaAlteracaoSituacaoCenso, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), matricula.getCdTurma(), matricula.getCdTurma(), matricula.getStMatricula(), cdUsuario);
				if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de situacao do censo");
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmMatricula = find(criterios);
			
			return new Result(1, "Solicitação realizada com sucesso", "rsm", rsmMatricula);
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao solicitar transferência");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo que irá reclassificar a matrícula de um aluno. Essa ação é usada pela secretaria quando há um processo para adiantar um determinado aluno de curso, por o mesmo já demonstrar
	 * capacidade de adiantar
	 * @param matriculaAntiga
	 * @param cdUsuario
	 * @param cdPeriodoLetivo
	 * @param cdCurso
	 * @param cdTurma
	 * @return
	 */
	public static Result saveReclassificacao(Matricula matriculaAntiga, int cdUsuario, int cdPeriodoLetivo, int cdCurso, int cdTurma){
		return saveReclassificacao(matriculaAntiga, cdUsuario, cdPeriodoLetivo, cdCurso, cdTurma, null);
	}
	
	public static Result saveReclassificacao(Matricula matriculaAntiga, int cdUsuario, int cdPeriodoLetivo, int cdCurso, int cdTurma, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(matriculaAntiga.getStMatricula() != MatriculaServices.ST_ATIVA && matriculaAntiga.getStMatricula() != MatriculaServices.ST_PENDENTE){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Reclassificação não podem ser realizados para matrículas que não estejam Ativas ou Pendentes");
			}
			
			if(matriculaAntiga.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0) ||
			   matriculaAntiga.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Matrículas do Mais Educação e de AEE não podem ser reclassificadas");
			}
			
			Turma turma = TurmaDAO.get(cdTurma, connect);
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem reclassificar matriculas");
			}
			
			Matricula matriculaNova = (Matricula)matriculaAntiga.clone();
			
			InstituicaoPeriodo instituicaoPeriodoMatricula = InstituicaoPeriodoDAO.get(matriculaNova.getCdPeriodoLetivo(), connect);
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			int cdPeriodoLetivoSecretaria = 0;
			ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdSecretaria, connect);
			if(rsmPeriodoRecente.next()){
				cdPeriodoLetivoSecretaria = rsmPeriodoRecente.getInt("cd_periodo_letivo");
			}
			
			if(!UsuarioServices.isAdmSecretariaEducacao(cdUsuario, connect) && instituicaoPeriodoMatricula.getCdPeriodoLetivoSuperior() != cdPeriodoLetivoSecretaria){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Reclassificação não pode ser realizada para o periodo de " + instituicaoPeriodoMatricula.getNmPeriodoLetivo());
			}
			
			matriculaNova.setCdMatricula(0);
			matriculaNova.setDtMatricula(Util.getDataAtual());
			matriculaNova.setStMatricula(MatriculaServices.ST_ATIVA);
			matriculaNova.setCdMatriculaOrigem(matriculaAntiga.getCdMatricula());
			matriculaNova.setCdPeriodoLetivo(cdPeriodoLetivo);
			matriculaNova.setCdCurso(cdCurso);
			matriculaNova.setCdTurma(cdTurma);
			matriculaNova.setNrMatricula(matriculaAntiga.getNrMatricula());
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_curso", "" + cdCurso, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmCursoMatriz = CursoMatrizDAO.find(criterios, connect);
			if(rsmCursoMatriz.next())
				matriculaNova.setCdMatriz(rsmCursoMatriz.getInt("cd_matriz"));
			
			Pessoa aluno = PessoaDAO.get(matriculaNova.getCdAluno(), connect);
			Turma turmaAntiga = TurmaDAO.get(matriculaAntiga.getCdTurma(), connect);
			Curso cursoAntigo = CursoDAO.get(turmaAntiga.getCdCurso(), connect);
			Pessoa instituicaoAntiga = PessoaDAO.get(turmaAntiga.getCdInstituicao(), connect);
			Turma turmaNova = TurmaDAO.get(matriculaNova.getCdTurma(), connect);
			Curso cursoNovo = CursoDAO.get(turmaNova.getCdCurso(), connect);
			
			matriculaNova.setNmUltimaEscola(matriculaAntiga.getNmUltimaEscola());
			
			
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(turmaNova.getCdPeriodoLetivo(), connect);
			int cdTipoOcorrenciaRealocacao = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_RECLASSIFICACAO, connect).getCdTipoOcorrencia();
			
			String txtDescricao = "";
				
			matriculaAntiga.setStMatricula(ST_RECLASSIFICACAO);
			matriculaAntiga.setDtConclusao(Util.getDataAtual());
			if(MatriculaDAO.update(matriculaAntiga, connect) <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar matrícula antiga");
			}
			
			Result resultado = save(matriculaNova, 0, cdUsuario, true, true, false, connect);
			if(resultado.getCode() <= 0 && resultado.getCode() != ERR_ALUNO_FORA_IDADE){
				return resultado;
			}
			
			ResultSetMap rsmMatriculaTransporte = MatriculaTipoTransporteServices.getTipoTransporteByMatricula(matriculaAntiga.getCdMatricula(), connect);
			while(rsmMatriculaTransporte.next()){
				if(MatriculaTipoTransporteDAO.insert(new MatriculaTipoTransporte(matriculaNova.getCdMatricula(), rsmMatriculaTransporte.getInt("cd_tipo_transporte")), connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao reclassificar tipos de transporte");
				}
			}
			
			txtDescricao = "Reclassificação do aluno " + aluno.getNmPessoa() + ", da instituição " + instituicaoAntiga.getNmPessoa() + ", turma "+cursoAntigo.getNmProdutoServico() + " - " + turmaAntiga.getNmTurma() + " para a turma "+cursoNovo.getNmProdutoServico() + " - " + turmaNova.getNmTurma()+" no período letivo de " + instituicaoPeriodo.getNmPeriodoLetivo();
			
			int stMatriculaOrigem = matriculaAntiga.getStMatricula();
			
			OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matriculaNova.getCdAluno(), txtDescricao, Util.getDataAtual(), cdTipoOcorrenciaRealocacao, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matriculaAntiga.getCdMatricula(), matriculaNova.getCdMatricula(), turmaAntiga.getCdTurma(), turmaNova.getCdTurma(), stMatriculaOrigem, cdUsuario);
			OcorrenciaMatriculaServices.save(ocorrencia, connect);
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Reclassificação realizada com sucesso");
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula", "" + matriculaNova.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			result.addObject("rsm", rsm);
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace();
			return new Result(-1, "Erro na conclusão de transferência");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que faz a correção da matrícula de um aluno, alterando seu curso e turma
	 * @param matriculaAntiga
	 * @param cdUsuario
	 * @param cdPeriodoLetivo
	 * @param cdCurso
	 * @param cdTurma
	 * @return
	 */
	public static Result saveRealocacao(Matricula matriculaAntiga, int cdUsuario, int cdPeriodoLetivo, int cdCurso, int cdTurma){
		return saveRealocacao(matriculaAntiga, cdUsuario, cdPeriodoLetivo, cdCurso, cdTurma, null);
	}
	
	public static Result saveRealocacao(Matricula matriculaAntiga, int cdUsuario, int cdPeriodoLetivo, int cdCurso, int cdTurma, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(matriculaAntiga.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0) ||
			   matriculaAntiga.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Matrículas do Mais Educação e de AEE não podem ser realocadas");
			}
			
			if(matriculaAntiga.getStMatricula() != MatriculaServices.ST_ATIVA && matriculaAntiga.getStMatricula() != MatriculaServices.ST_PENDENTE){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Realocações não podem ser realizados para matrículas que não estejam Ativas ou Pendentes");
			}
			
			Turma turma = TurmaDAO.get(cdTurma, connect);
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem realocar matriculas");
			}
			
			Matricula matriculaNova = (Matricula)matriculaAntiga.clone();
			
			InstituicaoPeriodo instituicaoPeriodoMatricula = InstituicaoPeriodoDAO.get(matriculaNova.getCdPeriodoLetivo(), connect);
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			int cdPeriodoLetivoSecretaria = 0;
			ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdSecretaria, connect);
			if(rsmPeriodoRecente.next()){
				cdPeriodoLetivoSecretaria = rsmPeriodoRecente.getInt("cd_periodo_letivo");
			}
			
			if(!UsuarioServices.isAdmSecretariaEducacao(cdUsuario, connect) && instituicaoPeriodoMatricula.getCdPeriodoLetivoSuperior() != cdPeriodoLetivoSecretaria){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Realocação não pode ser realizada para o periodo de " + instituicaoPeriodoMatricula.getNmPeriodoLetivo());
			}
			
			matriculaNova.setCdMatricula(0);
			matriculaNova.setDtMatricula(Util.getDataAtual());
			matriculaNova.setStMatricula(matriculaAntiga.getStMatricula());
			matriculaNova.setCdMatriculaOrigem(matriculaAntiga.getCdMatricula());
			matriculaNova.setCdPeriodoLetivo(cdPeriodoLetivo);
			matriculaNova.setCdCurso(cdCurso);
			matriculaNova.setCdTurma(cdTurma);
			matriculaNova.setNrMatricula(matriculaAntiga.getNrMatricula());
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_curso", "" + cdCurso, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmCursoMatriz = CursoMatrizDAO.find(criterios, connect);
			if(rsmCursoMatriz.next())
				matriculaNova.setCdMatriz(rsmCursoMatriz.getInt("cd_matriz"));
			
			Pessoa aluno = PessoaDAO.get(matriculaNova.getCdAluno(), connect);
			Turma turmaAntiga = TurmaDAO.get(matriculaAntiga.getCdTurma(), connect);
			Curso cursoTurmaAntigo = CursoDAO.get(turmaAntiga.getCdCurso(), connect);
			Pessoa instituicaoAntiga = PessoaDAO.get(turmaAntiga.getCdInstituicao(), connect);
			Turma turmaNova = TurmaDAO.get(matriculaNova.getCdTurma(), connect);
			Curso cursoTurmaNovo = CursoDAO.get(turmaNova.getCdCurso(), connect);
			
			matriculaNova.setNmUltimaEscola(matriculaAntiga.getNmUltimaEscola());
			
			
//			if(!TurmaServices.verificarCompatibilidadeTurmaMatricula(cursoTurmaNovo, cursoMatriculaNovo, connect)){
//				if(isConnectionNull){
//					Conexao.rollback(connect);
//				}
//				return new Result(-1, "Turma não compatível com a matrícula");
//			}
			
			
			
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(turmaNova.getCdPeriodoLetivo(), connect);
			int cdTipoOcorrenciaRealocacao = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_REALOCACAO, connect).getCdTipoOcorrencia();
			
			String txtDescricao = "";
				
			matriculaAntiga.setStMatricula(ST_REALOCACAO);
			matriculaAntiga.setDtConclusao(Util.getDataAtual());
			if(MatriculaDAO.update(matriculaAntiga, connect) <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar matrícula antiga");
			}
			Result resultado = save(matriculaNova, 0, cdUsuario, true, true, false, connect);
			if(resultado.getCode() <= 0 && resultado.getCode() != ERR_ALUNO_FORA_IDADE){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return resultado;
			}
			
			ResultSetMap rsmMatriculaTransporte = MatriculaTipoTransporteServices.getTipoTransporteByMatricula(matriculaAntiga.getCdMatricula(), connect);
			while(rsmMatriculaTransporte.next()){
				if(MatriculaTipoTransporteDAO.insert(new MatriculaTipoTransporte(matriculaNova.getCdMatricula(), rsmMatriculaTransporte.getInt("cd_tipo_transporte")), connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao realocar tipos de transporte");
				}
			}
			
			
			txtDescricao = "Realocação do aluno " + aluno.getNmPessoa() + ", da instituição " + instituicaoAntiga.getNmPessoa() + ", turma "+cursoTurmaAntigo.getNmProdutoServico() + " - " + turmaAntiga.getNmTurma() + " para a turma "+cursoTurmaNovo.getNmProdutoServico() + " - " + turmaNova.getNmTurma()+" no período letivo de " + instituicaoPeriodo.getNmPeriodoLetivo();
			
			int stMatriculaOrigem = matriculaAntiga.getStMatricula();
			
			OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matriculaNova.getCdAluno(), txtDescricao, Util.getDataAtual(), cdTipoOcorrenciaRealocacao, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matriculaAntiga.getCdMatricula(), matriculaNova.getCdMatricula(), turmaAntiga.getCdTurma(), turmaNova.getCdTurma(), stMatriculaOrigem, cdUsuario);
			OcorrenciaMatriculaServices.save(ocorrencia, connect);
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Realocação realizada com sucesso");
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula", "" + matriculaNova.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			result.addObject("rsm", rsm);
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace();
			return new Result(-1, "Erro na realocação");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Realoação em massa de alunos
	 * @param codigosMatricula
	 * @param cdUsuario
	 * @param cdPeriodoLetivo
	 * @param cdCurso
	 * @param cdTurma
	 * @return
	 */
	public static Result saveAllRealocacao(ArrayList<Integer> codigosMatricula, int cdUsuario, int cdPeriodoLetivo, int cdCurso, int cdTurma){
		return saveAllRealocacao(codigosMatricula, cdUsuario, cdPeriodoLetivo, cdCurso, cdTurma, null);
	}
	
	public static Result saveAllRealocacao(ArrayList<Integer> codigosMatricula, int cdUsuario, int cdPeriodoLetivo, int cdCurso, int cdTurma, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			for(Integer cdMatricula : codigosMatricula){
				Matricula matricula = MatriculaDAO.get(cdMatricula, connect);
				Result result = saveRealocacao(matricula, cdUsuario, cdPeriodoLetivo, cdCurso, cdTurma, connect);
				if(result.getCode() < 0){
					if(isConnectionNull){
						Conexao.rollback(connect);
					}
					return result;
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Realocações realizadas com sucesso");
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace();
			return new Result(-1, "Erro na realocação");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result save(Matricula matricula){
		return save(matricula, 0, true, null);
	}
	
	public static Result save(Matricula matricula, Connection connect){
		return save(matricula, 0, true, connect);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, int cdUsuario){
		return save(matricula, cdCursoModulo, cdUsuario, null);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, int cdUsuario, Connection connect){
		return save(matricula, cdCursoModulo, true, 0, null, 0, null, true, false, -1, cdUsuario, connect);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, int cdUsuario, boolean permitirAlunoIdadeDivergente , Connection connect){
		return save(matricula, cdCursoModulo, true, 0, null, 0, null, true, permitirAlunoIdadeDivergente, -1, cdUsuario, connect);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, int cdUsuario, boolean permitirAlunoIdadeDivergente, boolean permitirAlunoSemTransporte , Connection connect){
		return save(matricula, cdCursoModulo, true, 0, null, 0, null, true, permitirAlunoIdadeDivergente, -1, cdUsuario, permitirAlunoSemTransporte, connect);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, int cdUsuario, boolean permitirAlunoIdadeDivergente, boolean permitirAlunoSemTransporte, boolean verificarDeficiencia , Connection connect){
		return save(matricula, cdCursoModulo, true, 0, null, 0, null, true, permitirAlunoIdadeDivergente, -1, cdUsuario, permitirAlunoSemTransporte, verificarDeficiencia, connect);
	}
	
	public static Result save(Matricula matricula, MatriculaPrograma matriculaProgramaBolsaFamilia, MatriculaPrograma matriculaProgramaMaisEducacao){
		return save(matricula, 0, true, matriculaProgramaBolsaFamilia, matriculaProgramaMaisEducacao, null);
	}
	
	public static Result save(Matricula matricula, MatriculaPrograma matriculaProgramaBolsaFamilia, MatriculaPrograma matriculaProgramaMaisEducacao, Connection connect){
		return save(matricula, 0, true, matriculaProgramaBolsaFamilia, matriculaProgramaMaisEducacao, connect);
	}
	
	public static Result save(Matricula matricula, boolean verificarMatriculaNoPeriodo){
		return save(matricula, 0, verificarMatriculaNoPeriodo, null);
	}
	
	public static Result save(Matricula matricula, boolean verificarMatriculaNoPeriodo, Connection connect){
		return save(matricula, 0, verificarMatriculaNoPeriodo, connect);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo){
		return save(matricula, cdCursoModulo, true, null);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, boolean lgBolsaFamilia, boolean lgMaisEducacao){
		return save(matricula, cdCursoModulo, (lgBolsaFamilia?1:0), null, (lgMaisEducacao?1:0), null, true, true);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, boolean lgBolsaFamilia, boolean lgMaisEducacao, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente){
		return save(matricula, cdCursoModulo, true, (lgBolsaFamilia?1:0), null, (lgMaisEducacao?1:0), null, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, null);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, boolean lgBolsaFamilia, boolean lgMaisEducacao, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, int cdTipoTransporte){
		return save(matricula, cdCursoModulo, true, (lgBolsaFamilia?1:0), null, (lgMaisEducacao?1:0), null, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, cdTipoTransporte, null);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, boolean lgBolsaFamilia, boolean lgMaisEducacao, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, int cdTipoTransporte, int cdUsuario, AuthData authData){
		return save(matricula, cdCursoModulo, true, (lgBolsaFamilia?1:0), null, (lgMaisEducacao?1:0), null, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, cdTipoTransporte, cdUsuario, authData, null);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, boolean verificarMatriculaNoPeriodo){
		return save(matricula, cdCursoModulo, verificarMatriculaNoPeriodo, null);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, MatriculaPrograma matriculaProgramaBolsaFamilia, MatriculaPrograma matriculaProgramaMaisEducacao){
		return save(matricula, cdCursoModulo, true, matriculaProgramaBolsaFamilia, matriculaProgramaMaisEducacao, null);
	}

	public static Result save(Matricula matricula, int cdCursoModulo, boolean verificarMatriculaNoPeriodo, Connection connect){
		return save(matricula, cdCursoModulo, verificarMatriculaNoPeriodo, null, null, connect);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, boolean verificarMatriculaNoPeriodo, int cdUsuario){
		return save(matricula, cdCursoModulo, verificarMatriculaNoPeriodo, -1, null, -1, null, true, true, -1, cdUsuario, null);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, boolean verificarMatriculaNoPeriodo, int cdUsuario, Connection connect){
		return save(matricula, cdCursoModulo, verificarMatriculaNoPeriodo, -1, null, -1, null, true, true, -1, cdUsuario, connect);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, int lgBolsaFamilia, MatriculaPrograma matriculaProgramaBolsaFamilia, int lgMaisEducacao, MatriculaPrograma matriculaProgramaMaisEducacao, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente){
		return save(matricula, cdCursoModulo, true, lgBolsaFamilia, matriculaProgramaBolsaFamilia, lgMaisEducacao, matriculaProgramaMaisEducacao, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, null);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, int lgBolsaFamilia, MatriculaPrograma matriculaProgramaBolsaFamilia, int lgMaisEducacao, MatriculaPrograma matriculaProgramaMaisEducacao, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, int cdUsuario, AuthData authData){
		return save(matricula, cdCursoModulo, true, lgBolsaFamilia, matriculaProgramaBolsaFamilia, lgMaisEducacao, matriculaProgramaMaisEducacao, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, -1, cdUsuario, authData, null);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, boolean verificarMatriculaNoPeriodo, MatriculaPrograma matriculaProgramaBolsaFamilia, MatriculaPrograma matriculaProgramaMaisEducacao, Connection connect){
		return save(matricula, cdCursoModulo, verificarMatriculaNoPeriodo, -1, matriculaProgramaBolsaFamilia, -1, matriculaProgramaMaisEducacao, connect);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, boolean verificarMatriculaNoPeriodo, int lgBolsaFamilia, MatriculaPrograma matriculaProgramaBolsaFamilia, int lgMaisEducacao, MatriculaPrograma matriculaProgramaMaisEducacao, Connection connect){
		return save(matricula, cdCursoModulo, verificarMatriculaNoPeriodo, lgBolsaFamilia, matriculaProgramaBolsaFamilia, lgMaisEducacao, matriculaProgramaMaisEducacao, true, true, connect);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, boolean verificarMatriculaNoPeriodo, int lgBolsaFamilia, MatriculaPrograma matriculaProgramaBolsaFamilia, int lgMaisEducacao, MatriculaPrograma matriculaProgramaMaisEducacao, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente){
		return save(matricula, cdCursoModulo, verificarMatriculaNoPeriodo, lgBolsaFamilia, matriculaProgramaBolsaFamilia, lgMaisEducacao, matriculaProgramaMaisEducacao, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, null);
	}
	
	/**
	 * Metodo que realiza o salvamente de uma matrícula
	 * @param matricula
	 * @param cdCursoModulo
	 * @param verificarMatriculaNoPeriodo Verificação utilizada quando se deseja que não se verifique se a matrícula já existe naquele periodo (Ativa, concluida ou pendente), por padrão o método faz a verificação e não permite o salvamento
	 * @param lgBolsaFamilia Passa um booleano para saber se o aluno recebe ou não bolsa familia, é ligado diretamente no cadastro do aluno (temporario)
	 * @param matriculaProgramaBolsaFamilia
	 * @param lgMaisEducacao Passa um booleano para saber se o aluno esta ou não no Mais Educação, é ligado diretamente no cadastro do aluno (temporario)
	 * @param matriculaProgramaMaisEducacao
	 * @param connect
	 * @return
	 */
	public static Result save(Matricula matricula, int cdCursoModulo, boolean verificarMatriculaNoPeriodo, int lgBolsaFamilia, MatriculaPrograma matriculaProgramaBolsaFamilia, int lgMaisEducacao, MatriculaPrograma matriculaProgramaMaisEducacao, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, Connection connect){
		return save(matricula, cdCursoModulo, verificarMatriculaNoPeriodo, lgBolsaFamilia, matriculaProgramaBolsaFamilia, lgMaisEducacao, matriculaProgramaMaisEducacao, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, -1, connect);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, boolean verificarMatriculaNoPeriodo, int lgBolsaFamilia, MatriculaPrograma matriculaProgramaBolsaFamilia, int lgMaisEducacao, MatriculaPrograma matriculaProgramaMaisEducacao, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, int cdTipoTransporte, Connection connect){
		return save(matricula, cdCursoModulo, verificarMatriculaNoPeriodo, lgBolsaFamilia, matriculaProgramaBolsaFamilia, lgMaisEducacao, matriculaProgramaMaisEducacao, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, cdTipoTransporte, 0, connect);
	}
	
	public static Result save(Matricula matricula, boolean permitirSemTransporte, int cdUsuario, Connection connect){
		return save(matricula, 0, true, -1, null, -1, null, true, true, 0, cdUsuario, permitirSemTransporte, connect);
	}
	
	public static Result save(Matricula matricula, int cdUsuario, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, Connection connect){
		return save(matricula, 0, true, -1, null, -1, null, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, -1, cdUsuario, false, connect);
	}
	
	public static Result save(Matricula matricula, int cdUsuario, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, int cdTipoTransporte, Connection connect){
		return save(matricula, 0, true, -1, null, -1, null, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, cdTipoTransporte, cdUsuario, false, connect);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, boolean verificarMatriculaNoPeriodo, int lgBolsaFamilia, MatriculaPrograma matriculaProgramaBolsaFamilia, int lgMaisEducacao, MatriculaPrograma matriculaProgramaMaisEducacao, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, int cdTipoTransporte, int cdUsuario, Connection connect){
		return save(matricula, cdCursoModulo, verificarMatriculaNoPeriodo, lgBolsaFamilia, matriculaProgramaBolsaFamilia, lgMaisEducacao, matriculaProgramaMaisEducacao, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, cdTipoTransporte, cdUsuario, false, connect);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, boolean verificarMatriculaNoPeriodo, int lgBolsaFamilia, MatriculaPrograma matriculaProgramaBolsaFamilia, int lgMaisEducacao, MatriculaPrograma matriculaProgramaMaisEducacao, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, int cdTipoTransporte, int cdUsuario, AuthData authData, Connection connect){
		return save(matricula, cdCursoModulo, verificarMatriculaNoPeriodo, lgBolsaFamilia, matriculaProgramaBolsaFamilia, lgMaisEducacao, matriculaProgramaMaisEducacao, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, cdTipoTransporte, cdUsuario, false, authData, connect);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, boolean verificarMatriculaNoPeriodo, int lgBolsaFamilia, MatriculaPrograma matriculaProgramaBolsaFamilia, int lgMaisEducacao, MatriculaPrograma matriculaProgramaMaisEducacao, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, int cdTipoTransporte, int cdUsuario, boolean permitirSemTransporte, Connection connect){
		return save(matricula, cdCursoModulo, verificarMatriculaNoPeriodo, lgBolsaFamilia, matriculaProgramaBolsaFamilia, lgMaisEducacao, matriculaProgramaMaisEducacao, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, cdTipoTransporte, cdUsuario, false, true, connect);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, boolean verificarMatriculaNoPeriodo, int lgBolsaFamilia, MatriculaPrograma matriculaProgramaBolsaFamilia, int lgMaisEducacao, MatriculaPrograma matriculaProgramaMaisEducacao, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, int cdTipoTransporte, int cdUsuario, boolean permitirSemTransporte, AuthData authData, Connection connect){
		return save(matricula, cdCursoModulo, verificarMatriculaNoPeriodo, lgBolsaFamilia, matriculaProgramaBolsaFamilia, lgMaisEducacao, matriculaProgramaMaisEducacao, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, cdTipoTransporte, cdUsuario, false, true, authData, connect);
	}
	
	public static Result save(Matricula matricula, int cdCursoModulo, boolean verificarMatriculaNoPeriodo, int lgBolsaFamilia, MatriculaPrograma matriculaProgramaBolsaFamilia, int lgMaisEducacao, MatriculaPrograma matriculaProgramaMaisEducacao, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, int cdTipoTransporte, int cdUsuario, boolean permitirSemTransporte, boolean verificaDeficiencia, Connection connect){
		return save(matricula, cdCursoModulo, verificarMatriculaNoPeriodo, lgBolsaFamilia, matriculaProgramaBolsaFamilia, lgMaisEducacao, matriculaProgramaMaisEducacao, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, cdTipoTransporte, cdUsuario, permitirSemTransporte, verificaDeficiencia, null, connect);
	}
	
	/**
	 * Método que realiza o salvamento geral das matrículas do sistema
	 * @param matricula
	 * @param cdCursoModulo
	 * @param verificarMatriculaNoPeriodo Impede que seja feita mais de uma matrícula no mesmo periodo letivo
	 * @param lgBolsaFamilia
	 * @param matriculaProgramaBolsaFamilia
	 * @param lgMaisEducacao
	 * @param matriculaProgramaMaisEducacao
	 * @param permitirAlunoCidadeDiferente Permissão para que o aluno possa ser matrículado mesmo morando em cidade diferente da escola
	 * @param permitirAlunoIdadeDivergente Permissão para que o aluno possa ser matrículado mesmo que possua uma idade divergente do curso (Obs.: Essa permissão vale apenas para matrícula regular. No caso da 
	 * matrícula do EJA, não se pode dar permissão para matrícula abaixo dos 15 anos, e para o caso de Creche e Educação Infantil, tem que se matrícular na idade exata, de acordo às resoluções)
	 * @param cdTipoTransporte
	 * @param cdUsuario
	 * @param permitirSemTransporte Parametro defasado
	 * @param verificaDeficiencia Verifica a relação entre deficiência e deficiência multipla
	 * @param authData
	 * @param connect
	 * @return
	 */
	public static Result save(Matricula matricula, int cdCursoModulo, boolean verificarMatriculaNoPeriodo, int lgBolsaFamilia, MatriculaPrograma matriculaProgramaBolsaFamilia, int lgMaisEducacao, MatriculaPrograma matriculaProgramaMaisEducacao, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, int cdTipoTransporte, int cdUsuario, boolean permitirSemTransporte, boolean verificaDeficiencia, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(matricula==null)
				return new Result(-1, "Erro ao salvar. Matricula é nulo");

			int retorno;
			
			//COMPLIANCE
			int tpAcao = ComplianceManager.TP_ACAO_ANY;
			
			//LOG
			String idAcao = "";
			Matricula oldValue = null;
			
			InstituicaoPeriodo periodoLetivo = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect);
			
			Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			matricula.setCdMatriz(turma.getCdMatriz());
			
			InstituicaoPeriodo periodo = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect);
			
			boolean isInsert = false;
			
			//Matricula Nova
			if(matricula.getCdMatricula() <= 0){
				
				isInsert = true;
				
				//Caso o parametro 'verificarMatriculaNoPeriodo' tenha sido passado como verdadeiro
				//o sistema permitirá apenas que sejam matriculados alunos que não tenham matricula regular no periodo letivo passado
				if(verificarMatriculaNoPeriodo  
						&& temMatriculaRegularNoPeriodo(matricula.getCdAluno(), matricula.getCdPeriodoLetivo(), matricula.getCdMatricula(), connect)
						){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(ERR_MATRICULA_PERIODO, "Aluno já matriculado nesse período letivo");
				}
				
				//Compliance
				tpAcao = ComplianceManager.TP_ACAO_INSERT;
				
				//Log
				if(authData!=null)
					idAcao = authData.getIdAcaoInsert();
				
				//Insere a matricula
				retorno = MatriculaDAO.insert(matricula, connect);
				matricula.setCdMatricula(retorno);
				
				
				//Registra ocorrencia do cadastro da matricula
				int cdTipoOcorrenciaCadastroMatricula = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_MATRICULA, connect).getCdTipoOcorrencia();
				OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Matricula do aluno " + aluno.getNmPessoa() + " cadastrada no periodo " + periodo.getNmPeriodoLetivo(), Util.getDataAtual(), cdTipoOcorrenciaCadastroMatricula, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), matricula.getCdTurma(), matricula.getCdTurma(), matricula.getStMatricula(), cdUsuario);
				OcorrenciaMatriculaServices.save(ocorrencia, connect);
				
				//Registra a permissão especial concedida para o usuario registrado fazer a matricula fora da idade para casos de infantil e creche
				if(permitirAlunoIdadeDivergente && cdUsuario == ParametroServices.getValorOfParametroAsInteger("CD_USUARIO_PERMISSAO_ESPECIAL", 0)){
					int cdTipoOcorrencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_MATRICULA_PERMISSAO_ESPECIAL, connect).getCdTipoOcorrencia();
					if(cdTipoOcorrencia > 0){
						String txtOcorrencia = "Permissão concedida pelo usuário especial para aluno de creche e educação infantil cursar turma mesmo estando fora da idade";
						ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), txtOcorrencia, Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), matricula.getStMatricula(), cdUsuario);
						OcorrenciaMatriculaServices.save(ocorrencia, connect);
					}
				}
				
				// Formacao do numero de matricula
				if(matricula.getNrMatricula() == null || matricula.getNrMatricula().equals("")){
					ResultSetMap rsmMatriculaAntiga = new ResultSetMap(connect.prepareStatement("SELECT A.nr_matricula from acd_matricula A, acd_instituicao_periodo B " +
																								"  WHERE A.cd_periodo_letivo = B.cd_periodo_letivo " +
																								"    AND A.cd_aluno = " + matricula.getCdAluno() +
																								"    AND A.nr_matricula IS NOT NULL " +
																								"    AND A.nr_matricula <> ''" +
																								"  ORDER BY CAST(nm_periodo_letivo AS INTEGER) DESC, A.dt_matricula DESC").executeQuery());
					if(rsmMatriculaAntiga.next())
						matricula.setNrMatricula(rsmMatriculaAntiga.getString("nr_matricula"));
					else 
						matricula.setNrMatricula(periodoLetivo.getNmPeriodoLetivo() + Util.fillNum(matricula.getCdAluno(), 6));
				}
				// Update para gravar o numero de matricula
				retorno = MatriculaDAO.update(matricula, connect);
				
				if (matricula.getCdPeriodoLetivo() > 0) {
					MatriculaPeriodoLetivoServices.save(new MatriculaPeriodoLetivo(matricula.getCdMatricula(), matricula.getCdPeriodoLetivo(), 0, matricula.getDtMatricula(), matricula.getStMatricula(), 0, 0, 0), connect);
					/**
					 * Salvando matriculaModulo
					 */
					//TODO: Alterar quando a matrícula por módulo for implementada
					CursoModulo cursoModulo = CursoModuloServices.getById(matricula.getCdCurso()+"001", matricula.getCdCurso(), connect);
					if(matricula.getCdCurso() > 0 && (cdCursoModulo > 0 || (cursoModulo != null && cursoModulo.getCdCursoModulo() > 0 ))){
						MatriculaModuloServices.save(new MatriculaModulo(matricula.getCdMatricula(), 
													(cdCursoModulo > 0 ? cdCursoModulo : cursoModulo.getCdCursoModulo()), 
													matricula.getCdPeriodoLetivo()), connect);
					}
					
					/**
					 * Salvando matriculaDisciplinas
					 */
					ResultSetMap matriculaDisciplinas = OfertaServices.getDisciplinasByTurma(matricula.getCdTurma(), connect);
					if(matriculaDisciplinas.size() == 0){
						
						ResultSetMap rsmOfertas = OfertaServices.getAllByTurma(matricula.getCdTurma(), connect);
						while(rsmOfertas.next()){
							MatriculaDisciplinaServices.save(new MatriculaDisciplina(0, matricula.getCdMatricula(), matricula.getCdPeriodoLetivo(), 
									0/*cdConceito*/, null/*dtInicio*/, null/*dtConclusao*/, 0/*nrFaltas*/, 
									0/*tpMatricula*/, 0F/*vlConceito*/, 0/*qtChComplemento*/, 
									0F/*vlConceitoAproveitamento*/, ""/*nmInstitiuicaoAproveitamento*/, 
									0/*stMatriculaDisciplina*/, 0/*cdProfessor*/, 0/*cdSupervisorPratica*/, 
									0/*cdInstituicaoPratica*/, rsmOfertas.getInt("cd_matriz"), rsmOfertas.getInt("cd_curso"), 
									rsmOfertas.getInt("cd_curso_modulo"), 0/*cdDisciplina*/, rsmOfertas.getInt("cd_oferta"), 0), connect);
						}
					}
					else
						while (matriculaDisciplinas.next()) {
							MatriculaDisciplinaServices.save(new MatriculaDisciplina(0, matricula.getCdMatricula(), matricula.getCdPeriodoLetivo(), 
									0/*cdConceito*/, null/*dtInicio*/, null/*dtConclusao*/, 0/*nrFaltas*/, 
									0/*tpMatricula*/, 0F/*vlConceito*/, 0/*qtChComplemento*/, 
									0F/*vlConceitoAproveitamento*/, ""/*nmInstitiuicaoAproveitamento*/, 
									0/*stMatriculaDisciplina*/, 0/*cdProfessor*/, 0/*cdSupervisorPratica*/, 
									0/*cdInstituicaoPratica*/, matriculaDisciplinas.getInt("cd_matriz"), matriculaDisciplinas.getInt("cd_curso"), 
									matriculaDisciplinas.getInt("cd_curso_modulo"), matriculaDisciplinas.getInt("cd_disciplina"), matriculaDisciplinas.getInt("cd_oferta"), 0), connect);
						}
				}
				
			}
			else {
				
				isInsert = false;
				
				//Compliance e Log
				tpAcao = ComplianceManager.TP_ACAO_UPDATE;
				
				//Log
				if(authData!=null)
					idAcao = authData.getIdAcaoUpdate();
				oldValue = MatriculaDAO.get(matricula.getCdMatricula(), connect);
				
				retorno = MatriculaDAO.update(matricula, connect);
			}

			if(retorno < 0){
				if(retorno==-666) {
					//LOG
					LogServices.log(LogServices.ANY, idAcao, authData, matricula, oldValue, 
							"CHANGE_PROCESSO_EXCEPTION\n"
							+ "A mudança de processo não é permitida.\n"
							+ "De:\n"+LogServices.formatValues(oldValue)+"\nPara:\n"+LogServices.formatValues(matricula));
					return new Result(retorno, "A mudança de processo não é permitida.");
				}
				else 
					return new Result(-2, "Erro ao salvar matricula.");
			}
			
			
			if(lgBolsaFamilia >= 0)
				aluno.setLgBolsaFamilia(lgBolsaFamilia);
			if(lgMaisEducacao >= 0)
				aluno.setLgMaisEducacao(lgMaisEducacao);
			if(AlunoDAO.update(aluno, connect) <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-2, "Erro ao atualizar aluno");
			}
			
			//Se o tipo de transporte for passado, ele irá acrescentar um registro desse transporte para a matrícula
			if(cdTipoTransporte > 0){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_matricula", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tipo_transporte", "" + cdTipoTransporte, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmTipoTransporte = MatriculaTipoTransporteDAO.find(criterios, connect);
				if(!rsmTipoTransporte.next()){
					if(MatriculaTipoTransporteDAO.insert(new MatriculaTipoTransporte(matricula.getCdMatricula(), cdTipoTransporte), connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-2, "Erro ao adicionar transporte");
					}
				}
			}
			//Se for passado um tipo de transporte 0, significa que o aluno não possui transporte vinculado, o sistema então irá apagar qualquer registro que tenha anteriormente
			else if(cdTipoTransporte == 0){
				if(connect.prepareStatement("DELETE FROM acd_matricula_tipo_transporte WHERE cd_matricula = " + matricula.getCdMatricula()).executeUpdate() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao deletar transportes");
				}
			}

			//Faz a verificação das validações caso seja inserção ou atualização de dados
			if(isInsert){
				ValidatorResult resultadoValidacao = validate(matricula, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, verificaDeficiencia, cdUsuario, GRUPO_VALIDACAO_INSERT, connect);
				if(!resultadoValidacao.hasPassed()){
					return resultadoValidacao;
				}
			}
			else{
				ValidatorResult resultadoValidacao = validate(matricula, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, verificaDeficiencia, cdUsuario, GRUPO_VALIDACAO_UPDATE, connect);
				if(!resultadoValidacao.hasPassed()){
					return resultadoValidacao;
				}
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
			}
			else if (isConnectionNull)
				connect.commit();

			//COMPLIANCE
			Result result = ComplianceManager.process("saveCompliance", matricula, authData, tpAcao, connect);
			//LOG
			result = LogServices.log(tpAcao, idAcao, authData, matricula, oldValue);
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MATRICULA", matricula);
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
	
	public static Result remove(int cdMatricula){
		return remove(cdMatricula, false, null);
	}
	
	public static Result remove(int cdMatricula, boolean cascade){
		return remove(cdMatricula, cascade, null);
	}
	
	public static Result remove(int cdMatricula, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				// Excluindo matriculaModulo pela matricula
//				retorno = MatriculaModuloServices.removeByMatricula(cdMatricula, connect).getCode();
				retorno = connect.prepareStatement("DELETE FROM acd_matricula_modulo WHERE cd_matricula = " + cdMatricula).executeUpdate();
				if(retorno<0){
					Conexao.rollback(connect);
					return new Result(-2, "Este registro está vinculado a modulo e não pode ser excluído!");
				}
				// Excluindo matriculaDisciplinas pela matricula
//				retorno = MatriculaDisciplinaServices.removeByMatricula(cdMatricula, connect).getCode();
				retorno = connect.prepareStatement("DELETE FROM acd_matricula_disciplina WHERE cd_matricula = " + cdMatricula).executeUpdate();
				if(retorno<0){
					Conexao.rollback(connect);
					return new Result(-2, "Este registro está vinculado a disciplina e não pode ser excluído!");
				}
				// Excluindo matriculaPeriodoLetivo pela matricula
//				retorno = MatriculaPeriodoLetivoServices.removeByMatricula(cdMatricula, connect).getCode();
				retorno = connect.prepareStatement("DELETE FROM acd_matricula_periodo_letivo WHERE cd_matricula = " + cdMatricula).executeUpdate();
				if(retorno<0){
					Conexao.rollback(connect);
					return new Result(-2, "Este registro está vinculado a periodo letivo e não pode ser excluído!");
				}
				// Excluindo matriculaTipoDocumentacao pela matricula
//				retorno = MatriculaTipoDocumentacaoServices.removeByMatricula(cdMatricula, connect).getCode();
				retorno = connect.prepareStatement("DELETE FROM acd_matricula_tipo_documentacao WHERE cd_matricula = " + cdMatricula).executeUpdate();
				if(retorno<0){
					Conexao.rollback(connect);
					return new Result(-2, "Este registro está vinculado a tipo de documentação e não pode ser excluído!");
				}
				// Excluindo matriculaTipoDocumentacao pela matricula
//				retorno = OcorrenciaMatriculaServices.removeByMatricula(cdMatricula, connect).getCode();
				retorno = connect.prepareStatement("DELETE FROM acd_ocorrencia_matricula WHERE (cd_matricula_origem = " + cdMatricula + " OR cd_matricula_destino = " + cdMatricula + ")").executeUpdate();
				if(retorno<0){
					Conexao.rollback(connect);
					return new Result(-2, "Este registro está vinculado a ocorrência e não pode ser excluído!");
				}
				
				retorno = connect.prepareStatement("DELETE FROM acd_matricula_programa WHERE cd_matricula = " + cdMatricula).executeUpdate();
				if(retorno<0){
					Conexao.rollback(connect);
					return new Result(-2, "Este registro está vinculado a programa e não pode ser excluído!");
				}
			}
			if(!cascade || retorno>=0)
				retorno = MatriculaDAO.delete(cdMatricula, connect);
			
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
	
	/**
	 * Método que busca todos os horários a partir de uma instituição e um curso/turma passados
	 * @param cdInstituicao
	 * @param cdCurso
	 * @param cdTurma
	 * @return
	 */
	public static ArrayCollection getAllHorariosByIntituicaoCurso(int cdInstituicao, int cdCurso, int cdTurma) {
		return getAllHorariosByIntituicaoCurso(cdInstituicao, cdCurso, cdTurma, null);
	}
	
	public static ArrayCollection getAllHorariosByIntituicaoCurso(int cdInstituicao, int cdCurso, int cdTurma, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		
		ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
		int cdPeriodoLetivoAtual = 0;
		if(rsmPeriodoAtual.next()){
			cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
		}
		
		
		ResultSetMap rsm = new ResultSetMap();
		ResultSetMap rsmDias = new ResultSetMap();
		ArrayCollection horarios = new ArrayCollection();
		HashMap<String, String> elemento = new HashMap<String, String>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		try {
			pstmt = connect.prepareStatement("SELECT DISTINCT (A.hr_inicio), A.hr_termino FROM acd_oferta_horario A  " +
											 "LEFT OUTER JOIN acd_oferta 		B ON (A.cd_oferta = B.cd_oferta) " +
											 "LEFT OUTER JOIN acd_curso 		C ON (B.cd_curso = C.cd_curso) " +
											 "LEFT OUTER JOIN acd_instituicao_curso 	D ON (C.cd_curso = D.cd_curso) " +
											 "WHERE C.cd_curso = "+cdCurso+" AND D.cd_instituicao = "+cdInstituicao + " AND B.cd_turma = " + cdTurma + " AND D.cd_periodo_letivo = " + cdPeriodoLetivoAtual+
											 "ORDER BY A.hr_inicio");
			rsm = new ResultSetMap(pstmt.executeQuery());
			pstmt = connect.prepareStatement("SELECT A.*, F.sg_produto_servico AS sg_disciplina FROM acd_oferta_horario A " +
											 "LEFT OUTER JOIN acd_oferta 		B ON (A.cd_oferta = B.cd_oferta) " +
											 "LEFT OUTER JOIN acd_curso 		C ON (B.cd_curso = C.cd_curso) " +
											 "LEFT OUTER JOIN acd_instituicao_curso 	D ON (C.cd_curso = D.cd_curso) " +
											 "LEFT OUTER JOIN acd_disciplina		E ON (B.cd_disciplina = E.cd_disciplina) " + 
											 "LEFT OUTER JOIN grl_produto_servico 	F ON (E.cd_disciplina = F.cd_produto_servico) " +
											 "WHERE C.cd_curso = "+cdCurso+" AND D.cd_instituicao = "+cdInstituicao + " AND B.cd_turma = " + cdTurma + " AND D.cd_periodo_letivo = " + cdPeriodoLetivoAtual+
					 						 "ORDER BY A.hr_inicio");
			rsmDias = new ResultSetMap(pstmt.executeQuery());
			while (rsm.next()) {
				while (rsmDias.next()) {
					if (rsm.getTimestamp("HR_INICIO").compareTo(rsmDias.getTimestamp("HR_INICIO")) == 0) {
						switch (rsmDias.getInt("NR_DIA_SEMANA")) {
						case 1:
							elemento.put("SG_DISCIPLINA_SEGUNDA", rsmDias.getString("sg_disciplina"));
							break;
						case 2:
							elemento.put("SG_DISCIPLINA_TERCA", rsmDias.getString("sg_disciplina"));
							break;
						case 3:
							elemento.put("SG_DISCIPLINA_QUARTA", rsmDias.getString("sg_disciplina"));
							break;
						case 4:
							elemento.put("SG_DISCIPLINA_QUINTA", rsmDias.getString("sg_disciplina"));
							break;
						case 5:
							elemento.put("SG_DISCIPLINA_SEXTA", rsmDias.getString("sg_disciplina"));
							break;
						case 6:
							elemento.put("SG_DISCIPLINA_SABADO", rsmDias.getString("sg_disciplina"));
							break;
						}
					}
				}
				rsmDias.beforeFirst();
				
				String hrInicio = simpleDateFormat.format(rsm.getTimestamp("hr_inicio"));
				String[] hrInicioSet = hrInicio.split(":");
				hrInicio = (Integer.parseInt(hrInicioSet[0]) + 1) + ":" + hrInicioSet[1];
				String hrFinal = simpleDateFormat.format(rsm.getTimestamp("hr_termino"));
				String[] hrFinalSet = hrFinal.split(":");
				hrFinal = (Integer.parseInt(hrFinalSet[0]) + 1) + ":" + hrFinalSet[1];
				elemento.put("HR_INICIO", hrInicio);
				elemento.put("HR_FINAL", hrFinal);
				horarios.add(elemento);
				elemento = new HashMap<String, String>();
			}
						
			return horarios;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaServices.getAllHorariosByCurso: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaServices.getAllHorariosByCurso: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaServices.getAll: " + e);
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

	/**
	 * Busca todas as matrículas de um determinado aluno
	 * @param cdAluno
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAllByAluno(int cdAluno, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula WHERE cd_aluno = " + cdAluno);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaServices.getAllByAluno: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaServices.getAllByAluno: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca um ResultSetMap de uma matrícula a partir de seu código
	 * @param cdMatricula
	 * @param stInativacao
	 * @return
	 */
	public static ResultSetMap getMatriculaByCd(int cdMatricula, int stInativacao){
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_matricula", "" + cdMatricula, ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("stInativacao", "" + stInativacao, ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios);
	}
	
	public static ResultSetMap findByCodigo(int cdMatricula) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_matricula", "" + cdMatricula, ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			String sqlAdicional = "";//Variavel para que o usuário do metodo possa construir as condições de SQL que deseja
			String limit = "";
			int nrAno = 0;
			int lgNecessidadeEspecial = -1;
			int stInativacao = 0;//Usado para filtrar e retirar matriculas inativas da pesquisa
			int cdCurso = 0;
			int nrOrdem = 0;//Fazer buscas apenas de matrículas em determinadas ordens de curso (que classificam como NR_ORDEM=0 1º ano, NR_ORGEM=1 2º ano etc.)
			int cdInstituicao = 0;
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			ResultSetMap rsmPeriodoAtualSecretaria = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdSecretaria, connect);
			InstituicaoPeriodo instituicaoPeriodoAtualSecretaria = null;
			if(rsmPeriodoAtualSecretaria.next()){
				instituicaoPeriodoAtualSecretaria = InstituicaoPeriodoDAO.get(rsmPeriodoAtualSecretaria.getInt("cd_periodo_letivo"), connect);
			}
			
			String nmPessoa = "";
			String stMatricula = null;
			int lgPeriodoAtual = 0;
			String codigoTransferencia = null;
			
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("sqlAdicional")){
					sqlAdicional = criterios.get(i).getValue();
				}
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("limit")){
					limit = "LIMIT " + criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrAno")) {
					nrAno = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgNecessidadeEspecial")) {
					lgNecessidadeEspecial = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("stInativacao")) {
					stInativacao = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_CURSO")) {
					cdCurso = Integer.parseInt(criterios.get(i).getValue());
					Curso curso = CursoDAO.get(cdCurso, connect);
					nrOrdem = curso.getNrOrdem();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("D.NM_PESSOA")) {
					nmPessoa = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdInstituicao")) {
					cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("stMatricula")) {
					stMatricula = criterios.get(i).getValue();
					
//					if(codigoTransferencia != null && Integer.parseInt(stMatricula) == ST_EM_TRANSFERENCIA){
//						stMatricula = ST_ATIVA + ", " + ST_PENDENTE + ", " + ST_EM_TRANSFERENCIA;
//					}
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgPeriodoAtual")) {
					lgPeriodoAtual = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("codigoTransferencia")) {
					codigoTransferencia = criterios.get(i).getValue();
				}
				else
					crt.add(criterios.get(i));
					
			}
			
			ResultSetMap rsm = Search.find(
								"SELECT A.*, A.cd_aluno AS cd_pessoa, ('' || A.cd_aluno || A.cd_matricula) AS CODIGO_TRANSFERENCIA, " +
								"		B.*, C.*, D.*, D.nm_pessoa AS nm_aluno, D.nr_telefone1 AS nr_telefone, M.sg_estado AS sg_estado_naturalidade, " + //E.nm_pessoa AS nm_responsavel, " +
								"		F.nm_matriz, H.nm_produto_servico AS nm_curso, H.nm_produto_servico, G.nm_turma, G.cd_instituicao, G.tp_turno, G.qt_vagas, G.cd_turma_anterior, G.tp_atendimento, GP.nm_produto_servico AS NM_CURSO_TURMA, " + 
								"		I.nm_cidade AS nm_naturalidade, J.cd_periodo_letivo, J.nm_periodo_letivo, J.st_periodo_letivo, L.nm_pessoa AS nm_instituicao, " +
								"		KE.img_logomarca, P.cd_curso_modulo, R.nm_etapa, R.lg_eja, PTD.*, PTD.cd_tipo_documentacao AS TP_DOCUMENTACAO, PTD.dt_emissao AS dt_emissao_registro, " +
								
								"   N.cd_endereco, N.ds_endereco, N.cd_tipo_logradouro, N.cd_tipo_endereco, " +
								"   N.cd_logradouro, N.cd_bairro, N.cd_cidade, N.nm_logradouro, N.nm_bairro, N.nr_cep, " +
								"   N.nr_endereco, N.nm_complemento, N.nr_telefone, N.nm_ponto_referencia, N.lg_cobranca, N.lg_principal, N.tp_zona, " +
								"   N1.nm_tipo_logradouro, N1.sg_tipo_logradouro, " +
								"   N4.nm_cidade, N4.id_ibge, N5.nm_estado, N5.sg_estado, N5.cd_estado, AC.nr_ordem, AC.lg_referencia, Q.cd_curso_etapa " +
								 
								"FROM acd_matricula							A " +
								"LEFT OUTER JOIN acd_aluno					B ON (A.cd_aluno = B.cd_aluno) " +
								"LEFT OUTER JOIN grl_pessoa_fisica			C ON (B.cd_aluno = C.cd_pessoa) " +
								"LEFT OUTER JOIN grl_pessoa					D ON (C.cd_pessoa = D.cd_pessoa) " + 
								"LEFT OUTER JOIN grl_pessoa					E ON (B.cd_responsavel = E.cd_pessoa) " +
								"LEFT OUTER JOIN acd_curso			        AC ON (A.cd_curso = AC.cd_curso) " +
								"LEFT OUTER JOIN acd_curso_matriz			F ON (A.cd_matriz = F.cd_matriz " +
								"                                   				AND A.cd_curso = F.cd_curso) " +
								"LEFT OUTER JOIN grl_produto_servico		H ON (A.cd_curso = H.cd_produto_servico) " +
								"LEFT OUTER JOIN acd_turma					G ON (A.cd_turma = G.cd_turma) " +
								"LEFT OUTER JOIN grl_produto_servico		GP ON (G.cd_curso = GP.cd_produto_servico) " +
								"LEFT OUTER JOIN grl_cidade					I ON (C.cd_naturalidade = I.cd_cidade) " +
								"LEFT OUTER JOIN acd_instituicao_periodo	J ON (A.cd_periodo_letivo = J.cd_periodo_letivo) " +
								"LEFT OUTER JOIN acd_instituicao			K ON (G.cd_instituicao = K.cd_instituicao) " +
								"LEFT OUTER JOIN grl_empresa   			    KE ON (K.cd_instituicao = KE.cd_empresa) " +
								"LEFT OUTER JOIN grl_pessoa					L ON (K.cd_instituicao = L.cd_pessoa) " +
								"LEFT OUTER JOIN grl_estado					M ON (I.cd_estado = M.cd_estado) " +
								
								"LEFT OUTER JOIN grl_pessoa_endereco		N ON (D.cd_pessoa = N.cd_pessoa AND N.lg_principal = 1) " +
								"LEFT OUTER JOIN grl_tipo_logradouro 	N1 ON (N1.cd_tipo_logradouro = N.cd_tipo_logradouro) " +
								"LEFT OUTER JOIN grl_cidade 		N4 ON (N4.cd_cidade = N.cd_cidade) " +
								"LEFT OUTER JOIN grl_estado 		N5 ON (N5.cd_estado = N4.cd_estado) " +
								
								"LEFT OUTER JOIN acd_matricula_modulo		O ON (A.cd_matricula = O.cd_matricula) " +
								"LEFT OUTER JOIN acd_curso_modulo		    P ON (O.cd_curso_modulo = P.cd_curso_modulo"+ 
								"													AND A.cd_curso = P.cd_curso) " +
								"JOIN acd_curso_etapa  		   				Q ON (A.cd_curso = Q.cd_curso) " +
								"LEFT OUTER JOIN acd_tipo_etapa  		    R ON (Q.cd_etapa = R.cd_etapa) " +
								"LEFT OUTER JOIN grl_pessoa_tipo_documentacao PTD ON (A.cd_aluno = PTD.cd_pessoa " +
			 					"												AND PTD.cd_tipo_documentacao = "+ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0)+") " +
			 					  
								"WHERE 1 = 1 " + (stInativacao == 0 ? " AND A.st_matricula <> "+ST_INATIVO : "") + " AND N.lg_principal = 1 " + sqlAdicional + (nrAno != 0 ? " AND EXTRACT(YEAR FROM A.nr_matricula) = " + nrAno + " " : " ") +
								(cdCurso > 0 && nrOrdem > 0 ? " AND (A.CD_CURSO = "+cdCurso+" OR AC.NR_ORDEM = "+nrOrdem+")" : " ") +
								(lgNecessidadeEspecial < 0 ? "" : " AND " + (lgNecessidadeEspecial == 0 ? " NOT " : "") + " EXISTS (SELECT * FROM grl_pessoa_necessidade_especial GPD WHERE A.cd_aluno = GPD.cd_pessoa) ")+
								(!nmPessoa.equals("") ? 
										   "	  AND TRANSLATE(D.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE TRANSLATE('%"+nmPessoa+"%', 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') ": "") +
								(cdInstituicao > 0 && cdInstituicao != cdSecretaria ? " AND G.cd_instituicao = " + cdInstituicao : "") + 
								(stMatricula != null ? " AND st_matricula IN (" + stMatricula+")" : "") + 
								(lgPeriodoAtual > 0 ? " AND J.nm_periodo_letivo = '" + instituicaoPeriodoAtualSecretaria.getNmPeriodoLetivo() + "'" : "") + 
								(codigoTransferencia != null ? " AND ('' || A.cd_aluno || A.cd_matricula) = '" + codigoTransferencia + "'" : ""),
								"ORDER BY D.nm_pessoa, A.dt_matricula DESC " + limit, crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			while(rsm.next()){
				
				rsm.setValueToField("IMG_FOTO", "");
				rsm.setValueToField("IMG_LOGOMARCA", "");
				
				rsm.setValueToField("NM_ST_MATRICULA", situacao[rsm.getInt("ST_MATRICULA")]);
				
				rsm.setValueToField("NM_DT_NASCIMENTO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_nascimento")));
				rsm.setValueToField("DT_NM_NASCIMENTO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_nascimento")));
				
				GregorianCalendar dtEmissaoRegistro = rsm.getGregorianCalendar("dt_emissao_registro");
				if(dtEmissaoRegistro != null)
					rsm.setValueToField("DT_NM_EMISSAO_REGISTRO", (Integer.parseInt(String.valueOf(dtEmissaoRegistro.get(Calendar.DAY_OF_MONTH))) > 9 ? dtEmissaoRegistro.get(Calendar.DAY_OF_MONTH) : "0" + dtEmissaoRegistro.get(Calendar.DAY_OF_MONTH)) + "/" + 
																  (Integer.parseInt(String.valueOf(dtEmissaoRegistro.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtEmissaoRegistro.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtEmissaoRegistro.get(Calendar.MONTH))) + 1)) + "/" + 
																  dtEmissaoRegistro.get(Calendar.YEAR));
				
				GregorianCalendar dtEmissaoRg = rsm.getGregorianCalendar("dt_emissao_rg");
				if(dtEmissaoRg != null)
					rsm.setValueToField("DT_NM_EMISSAO_RG", (Integer.parseInt(String.valueOf(dtEmissaoRg.get(Calendar.DAY_OF_MONTH))) > 9 ? dtEmissaoRg.get(Calendar.DAY_OF_MONTH) : "0" + dtEmissaoRg.get(Calendar.DAY_OF_MONTH)) + "/" + 
															(Integer.parseInt(String.valueOf(dtEmissaoRg.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtEmissaoRg.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtEmissaoRg.get(Calendar.MONTH))) + 1)) + "/" + 
															dtEmissaoRg.get(Calendar.YEAR));
				
				GregorianCalendar dtChegadaPais = rsm.getGregorianCalendar("dt_chegada_pais");
				if(dtChegadaPais != null)
					rsm.setValueToField("DT_NM_CHEGADA_PAIS", (Integer.parseInt(String.valueOf(dtChegadaPais.get(Calendar.DAY_OF_MONTH))) > 9 ? dtChegadaPais.get(Calendar.DAY_OF_MONTH) : "0" + dtChegadaPais.get(Calendar.DAY_OF_MONTH)) + "/" + 
															(Integer.parseInt(String.valueOf(dtChegadaPais.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtChegadaPais.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtChegadaPais.get(Calendar.MONTH))) + 1)) + "/" + 
															dtChegadaPais.get(Calendar.YEAR));
				
				
				
				GregorianCalendar dtMatricula = rsm.getGregorianCalendar("dt_matricula");
				if(dtMatricula != null)
					rsm.setValueToField("DT_NM_MATRICULA", (Integer.parseInt(String.valueOf(dtMatricula.get(Calendar.DAY_OF_MONTH))) > 9 ? dtMatricula.get(Calendar.DAY_OF_MONTH) : "0" + dtMatricula.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtMatricula.get(Calendar.MONTH))) > 9 ? dtMatricula.get(Calendar.MONTH) : "0" + dtMatricula.get(Calendar.MONTH)) + "/" + dtMatricula.get(Calendar.YEAR));
				
				rsm.setValueToField("CL_TP_MATRICULA", tipo[rsm.getInt("TP_MATRICULA")]);
				rsm.setValueToField("CL_ST_MATRICULA", situacao[rsm.getInt("ST_MATRICULA")]);
				
				rsm.setValueToField("NM_ST_ALUNO_CENSO", (rsm.getInt("CD_PERIODO_LETIVO") > 835 ? situacaoAlunoCenso[rsm.getInt("ST_ALUNO_CENSO")] : "----"));
				rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO", (rsm.getInt("CD_PERIODO_LETIVO") > 835 ? situacaoAlunoCensoConversao[InstituicaoEducacensoServices.convertSituacaoAlunoCenso(rsm.getInt("CD_MATRICULA"), rsm.getInt("ST_ALUNO_CENSO"), connect)] : "----"));
				
				if(rsm.getInt("ST_MATRICULA") == ST_CANCELADO){
					rsm.setValueToField("NM_ST_ALUNO_CENSO", "----");
					rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO", "----");
				}
				if(rsm.getInt("ST_MATRICULA") == ST_REALOCACAO){
					rsm.setValueToField("NM_ST_ALUNO_CENSO", "----");
					rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO", "----");
				}
				if(rsm.getInt("ST_MATRICULA") == ST_RECLASSIFICACAO){
					rsm.setValueToField("NM_ST_ALUNO_CENSO", "----");
					rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO", "----");
				}
				if(rsm.getInt("ST_MATRICULA") == ST_INATIVO){
					rsm.setValueToField("NM_ST_ALUNO_CENSO", "----");
					rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO", "----");
				}
				if(rsm.getInt("ST_MATRICULA") == ST_PENDENTE){
					rsm.setValueToField("NM_ST_ALUNO_CENSO", "----");
					rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO", "----");
				}
				
				
				
				/*Verificação de documentos necessários na matrícula*/
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("B.id_tipo_documentacao", InstituicaoEducacensoServices.TP_DOCUMENTACAO_NIS, ItemComparator.EQUAL, Types.VARCHAR));
				criterios.add(new ItemComparator("A.cd_matricula", rsm.getString("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmAux = MatriculaTipoDocumentacaoServices.find(criterios, connect);
				if(rsmAux.next())
					rsm.setValueToField("LG_NIS", 1);
				else
					rsm.setValueToField("LG_NIS", 0);
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("B.id_tipo_documentacao", InstituicaoEducacensoServices.TP_DOCUMENTACAO_RG, ItemComparator.EQUAL, Types.VARCHAR));
				criterios.add(new ItemComparator("A.cd_matricula", rsm.getString("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
				rsmAux = MatriculaTipoDocumentacaoServices.find(criterios, connect);
				if(rsmAux.next())
					rsm.setValueToField("LG_RG", 1);
				else
					rsm.setValueToField("LG_RG", 0);
				
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("B.id_tipo_documentacao", InstituicaoEducacensoServices.TP_DOCUMENTACAO_PASSAPORTE, ItemComparator.EQUAL, Types.VARCHAR));
				criterios.add(new ItemComparator("A.cd_matricula", rsm.getString("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
				rsmAux = MatriculaTipoDocumentacaoServices.find(criterios, connect);
				if(rsmAux.next())
					rsm.setValueToField("LG_PASSAPORTE", 1);
				else
					rsm.setValueToField("LG_PASSAPORTE", 0);
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", "" + rsm.getInt("cd_aluno") , ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tipo_documentacao", "" + TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_PASSAPORTE).getCdTipoDocumentacao() , ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPessoaTipoDocumentacaoPassaporte = PessoaTipoDocumentacaoDAO.find(criterios, connect);
				if(rsmPessoaTipoDocumentacaoPassaporte.next()){
					rsm.setValueToField("NR_PASSAPORTE", rsmPessoaTipoDocumentacaoPassaporte.getString("nr_documento"));
				}
				else{
					rsm.setValueToField("NR_PASSAPORTE", "");
				}
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", "" + rsm.getInt("cd_aluno") , ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tipo_documentacao", "" + TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_SUS).getCdTipoDocumentacao() , ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPessoaTipoDocumentacaoSus = PessoaTipoDocumentacaoDAO.find(criterios, connect);
				if(rsmPessoaTipoDocumentacaoSus.next()){
					rsm.setValueToField("NR_SUS", rsmPessoaTipoDocumentacaoSus.getString("nr_documento"));
				}
				else{
					rsm.setValueToField("NR_SUS", "");
				}
				
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("B.id_tipo_documentacao", InstituicaoEducacensoServices.TP_DOCUMENTACAO_REG_NASCIMENTO, ItemComparator.EQUAL, Types.VARCHAR));
				criterios.add(new ItemComparator("A.cd_matricula", rsm.getString("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
				rsmAux = MatriculaTipoDocumentacaoServices.find(criterios, connect);
				if(rsmAux.next())
					rsm.setValueToField("LG_CERTIDAO_NASCIMENTO", 1);
				else
					rsm.setValueToField("LG_CERTIDAO_NASCIMENTO", 0);
				
				String nrDocumentoCompleto = rsm.getString("nr_documento");
				
				if(nrDocumentoCompleto != null && nrDocumentoCompleto.length() == 32){
					rsm.setValueToField("NR_DOCUMENTO_SERVENTIA", nrDocumentoCompleto.substring(0, 6));
					rsm.setValueToField("NR_DOCUMENTO_ACERVO", nrDocumentoCompleto.substring(6, 8));
					rsm.setValueToField("NR_DOCUMENTO_RCPN", nrDocumentoCompleto.substring(8, 10));
					rsm.setValueToField("NR_DOCUMENTO_ANO_REGISTRO", nrDocumentoCompleto.substring(10, 14));
					rsm.setValueToField("NR_DOCUMENTO_ATO", nrDocumentoCompleto.substring(14, 15));
					rsm.setValueToField("NR_DOCUMENTO_LIVRO", nrDocumentoCompleto.substring(15, 20));
					rsm.setValueToField("NR_DOCUMENTO_FOLHA", nrDocumentoCompleto.substring(20, 23));
					rsm.setValueToField("NR_DOCUMENTO_REGISTRO", nrDocumentoCompleto.substring(23, 30));
					rsm.setValueToField("NR_DOCUMENTO_DV", nrDocumentoCompleto.substring(30, 32));
				}
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("B.id_tipo_documentacao", InstituicaoEducacensoServices.TP_DOCUMENTACAO_REG_CASAMENTO, ItemComparator.EQUAL, Types.VARCHAR));
				criterios.add(new ItemComparator("A.cd_matricula", rsm.getString("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
				rsmAux = MatriculaTipoDocumentacaoServices.find(criterios, connect);
				if(rsmAux.next())
					rsm.setValueToField("LG_CERTIDAO_CASAMENTO", 1);
				else
					rsm.setValueToField("LG_CERTIDAO_CASAMENTO", 0);
				
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("B.id_tipo_documentacao", InstituicaoEducacensoServices.TP_DOCUMENTACAO_CPF, ItemComparator.EQUAL, Types.VARCHAR));
				criterios.add(new ItemComparator("A.cd_matricula", rsm.getString("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
				rsmAux = MatriculaTipoDocumentacaoServices.find(criterios, connect);
				if(rsmAux.next())
					rsm.setValueToField("LG_CPF", 1);
				else
					rsm.setValueToField("LG_CPF", 0);
				
				rsm.setValueToField("NM_TURMA_COMPLETO", rsm.getString("NM_CURSO_TURMA") + " - " + rsm.getString("NM_TURMA"));
				
				int qtVagas = rsm.getInt("qt_vagas");
				int qtMatriculas = TurmaServices.getAlunos(rsm.getInt("cd_turma")).getLines().size();
				int qtVagasDisponiveis = qtVagas - qtMatriculas;
				
				rsm.setValueToField("QT_VAGAS_DISPONIVEIS", qtVagasDisponiveis);
				
//				int cdProgramaBolsaFamilia = ParametroServices.getValorOfParametroAsInteger("CD_PROGRAMA_BOLSA_FAMILIA", 0);
//				if(cdProgramaBolsaFamilia > 0){
//					criterios = new ArrayList<ItemComparator>();
//					criterios.add(new ItemComparator("cd_programa", "" + cdProgramaBolsaFamilia, ItemComparator.EQUAL, Types.VARCHAR));
//					criterios.add(new ItemComparator("cd_matricula", rsm.getString("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
//					rsmAux = MatriculaProgramaDAO.find(criterios, connect);
//					if(rsmAux.next()){
//						rsm.setValueToField("LG_BOLSA_FAMILIA", 1);
//						rsm.setValueToField("NR_DOCUMENTO_BOLSA_FAMILIA", rsmAux.getString("nr_matricula_programa"));
//					}
//				}
//				
//				int cdProgramaMaisEducacao = ParametroServices.getValorOfParametroAsInteger("CD_PROGRAMA_MAIS_EDUCACAO", 0);
//				if(cdProgramaBolsaFamilia > 0){
//					criterios = new ArrayList<ItemComparator>();
//					criterios.add(new ItemComparator("cd_programa", "" + cdProgramaMaisEducacao, ItemComparator.EQUAL, Types.VARCHAR));
//					criterios.add(new ItemComparator("cd_matricula", rsm.getString("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
//					rsmAux = MatriculaProgramaDAO.find(criterios, connect);
//					if(rsmAux.next()){
//						rsm.setValueToField("LG_MAIS_EDUCACAO", 1);
//						rsm.setValueToField("NR_DOCUMENTO_MAIS_EDUCACAO", rsmAux.getString("nr_matricula_programa"));
//					}
//				}
				
				//Para indicar o nome do curso atual, o metodo primeiro verifica se o curso é multi, se o for ele considera o curso da matrícula, caso contrário considera o da turma
				Turma turma = TurmaDAO.get(rsm.getInt("cd_turma"), connect);
				if(turma != null){				
					Curso curso = CursoDAO.get(turma.getCdCurso(), connect);
					if(curso.getLgMulti() == 1)
						curso = CursoDAO.get(rsm.getInt("cd_curso"), connect);
					if(curso != null)
						rsm.setValueToField("NM_CURSO_ATUAL", curso.getNmProdutoServico());
				}
				
//				int stMatriculaOrigem = ST_ATIVA;
	//			Connection connectAux = Conexao.conectar();
//				try{
//				com.tivic.manager.grl.TipoOcorrencia tipoOcorrencia = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_SOLICITACAO_TRANSFERENCIA);
//				if(tipoOcorrencia != null){
//					ResultSetMap rsmUltimaTransferencia = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_ocorrencia_matricula A, grl_ocorrencia B "
//							+ "																			WHERE A.cd_ocorrencia = B.cd_ocorrencia "
//							+ "																			  AND A.cd_matricula_origem = " + rsm.getString("cd_matricula")
//							+ "																	  		  AND cd_tipo_ocorrencia = " + tipoOcorrencia.getCdTipoOcorrencia()
//							+ "																		 ORDER BY dt_ocorrencia desc").executeQuery());
//					if(rsmUltimaTransferencia.next()){
//						stMatriculaOrigem = rsmUltimaTransferencia.getInt("st_matricula_origem");
//					}
//				}
//				}
//				catch(Exception e){
//					e.printStackTrace();
//				}
	//			finally{
	//				Conexao.desconectar(connectAux);
	//			}
				
				//Busca o curso de referencia, usado no atestado de transferência para escolas fora da rede que não usam o sistema de ciclos e segmentos, apenas anos
				
				
				
				
				PreparedStatement pstmt =connect.prepareStatement("SELECT * FROM acd_curso_multi WHERE cd_curso_multi = ? AND cd_curso = " + rsm.getInt("cd_curso"));
				
				boolean lgCursoCrechePreEja = false;
				pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_CRECHE", 0));
				ResultSetMap rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
				if(rsmCursoMatricula.next()){
					lgCursoCrechePreEja = true;
				}
				pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_PRE_ESCOLA", 0));
				rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
				if(rsmCursoMatricula.next()){
					lgCursoCrechePreEja = true;
				}
				if(rsm.getInt("lg_eja")==1){
					lgCursoCrechePreEja = true;
				}
				if(!lgCursoCrechePreEja){
					ResultSetMap rsmCursoReferencia = new ResultSetMap(connect.prepareStatement("SELECT B.nm_produto_servico AS nm_curso, D.nm_etapa FROM acd_curso A, grl_produto_servico B, acd_curso_etapa C, acd_tipo_etapa D "
							+ "																			WHERE A.cd_curso = B.cd_produto_servico "
							+ "																			  AND A.cd_curso = C.cd_curso "
							+ "																			  AND C.cd_etapa = D.cd_etapa "
							+ "																			  AND A.nr_ordem = " + rsm.getInt("nr_ordem") 
							+ "																			  AND A.lg_referencia = 1").executeQuery());
					if(rsmCursoReferencia.next()){
						rsm.setValueToField("NM_CURSO_REFERENCIA", rsmCursoReferencia.getString("nm_curso") + " da " + rsmCursoReferencia.getString("nm_etapa"));	
					}
					else{
						rsm.setValueToField("NM_CURSO_REFERENCIA", rsm.getString("nm_curso"));
					}
				}
			
				//Utilizado para saber o curso posterior do curso atual da matricula, seguindo o que esta considerado na etapa
				if(rsm.getInt("nr_ordem") != CursoServices.TP_ORDEM_9_ANO_8_SERIE){
					ResultSetMap rsmCursoPosterior = new ResultSetMap(connect.prepareStatement("SELECT C.cd_curso, D.nm_produto_servico AS nm_curso FROM acd_curso A, acd_curso_etapa B, acd_curso_etapa C, grl_produto_servico D WHERE A.cd_curso = B.cd_curso AND B.cd_curso_etapa = " + rsm.getInt("cd_curso_etapa") + " AND B.cd_curso_etapa_posterior = C.cd_curso_etapa AND C.cd_curso = D.cd_produto_servico").executeQuery());
					if(rsmCursoPosterior.next()){
						rsm.setValueToField("CD_CURSO_POSTERIOR", rsmCursoPosterior.getInt("cd_curso"));
						rsm.setValueToField("NM_CURSO_POSTERIOR", rsmCursoPosterior.getString("nm_curso"));
						
						
						pstmt =connect.prepareStatement("SELECT * FROM acd_curso_multi WHERE cd_curso_multi = ? AND cd_curso = " + rsmCursoPosterior.getInt("cd_curso"));
						
						lgCursoCrechePreEja = false;
						pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_CRECHE", 0));
						rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
						if(rsmCursoMatricula.next()){
							lgCursoCrechePreEja = true;
						}
						pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_PRE_ESCOLA", 0));
						rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
						if(rsmCursoMatricula.next()){
							lgCursoCrechePreEja = true;
						}
						if(rsm.getInt("lg_eja")==1){
							lgCursoCrechePreEja = true;
						}
						
						if(!lgCursoCrechePreEja){
							ResultSetMap rsmCursoReferencia = new ResultSetMap(connect.prepareStatement("SELECT B.nm_produto_servico AS nm_curso, D.nm_etapa FROM acd_curso A, grl_produto_servico B, acd_curso_etapa C, acd_tipo_etapa D "
									+ "																			WHERE A.cd_curso = B.cd_produto_servico "
									+ "																			  AND A.cd_curso = C.cd_curso "
									+ "																			  AND C.cd_etapa = D.cd_etapa "
									+ "																			  AND A.nr_ordem = " + (rsm.getInt("nr_ordem")+1) 
									+ "																			  AND A.lg_referencia = 1").executeQuery());
							if(rsmCursoReferencia.next()){
								rsm.setValueToField("NM_CURSO_REFERENCIA_POSTERIOR", rsmCursoReferencia.getString("nm_curso") + " da " + rsmCursoReferencia.getString("nm_etapa"));	
							}
							else{
								rsm.setValueToField("NM_CURSO_REFERENCIA_POSTERIOR", rsm.getString("nm_curso"));
							}
						}
						
					}
				}
				else{
					rsm.setValueToField("CD_CURSO_POSTERIOR", rsm.getInt("cd_curso"));
					rsm.setValueToField("NM_CURSO_POSTERIOR", rsm.getString("nm_curso"));
					
					ResultSetMap rsmCursoReferencia = new ResultSetMap(connect.prepareStatement("SELECT B.nm_produto_servico AS nm_curso, D.nm_etapa FROM acd_curso A, grl_produto_servico B, acd_curso_etapa C, acd_tipo_etapa D "
							+ "																			WHERE A.cd_curso = B.cd_produto_servico "
							+ "																			  AND A.cd_curso = C.cd_curso "
							+ "																			  AND C.cd_etapa = D.cd_etapa "
							+ "																			  AND A.nr_ordem = " + rsm.getInt("nr_ordem") 
							+ "																			  AND A.lg_referencia = 1").executeQuery());
					if(rsmCursoReferencia.next()){
						rsm.setValueToField("NM_CURSO_REFERENCIA", rsmCursoReferencia.getString("nm_curso") + " da " + rsmCursoReferencia.getString("nm_etapa"));	
					}
					else{
						rsm.setValueToField("NM_CURSO_REFERENCIA", rsm.getString("nm_curso"));
					}
					
					if(!lgCursoCrechePreEja){
						rsm.setValueToField("NM_CURSO_REFERENCIA_POSTERIOR", rsmCursoReferencia.getString("nm_curso") + " da " + rsmCursoReferencia.getString("nm_etapa"));
					}
				}
			
				//Utilizado para saber o curso anterior do curso atual da matricula, seguindo o que esta considerado na etapa
				ResultSetMap rsmCursoAnterior = new ResultSetMap(connect.prepareStatement("SELECT A.cd_curso, A.nr_ordem, C.nm_produto_servico AS nm_curso FROM acd_curso A, acd_curso_etapa B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND A.cd_curso = C.cd_produto_servico AND B.cd_curso_etapa_posterior = " + rsm.getInt("cd_curso_etapa")).executeQuery());
				if(rsmCursoAnterior.next()){
					rsm.setValueToField("CD_CURSO_ANTERIOR", rsmCursoAnterior.getInt("cd_curso"));
					rsm.setValueToField("NM_CURSO_ANTERIOR", rsmCursoAnterior.getString("nm_curso"));
					rsm.setValueToField("NR_ORDEM_ANTERIOR", rsmCursoAnterior.getString("nr_ordem"));
				}
				Curso cursoMatricula = CursoDAO.get(rsm.getInt("CD_CURSO"), connect);
				if(rsm.getInt("CD_CURSO_ANTERIOR") == 0){
					ResultSetMap rsmCursoEquivalente = new ResultSetMap(connect.prepareStatement("SELECT A.cd_curso, A.nr_ordem, B.cd_curso_etapa FROM acd_curso A, acd_curso_etapa B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND A.cd_curso = C.cd_produto_servico AND C.id_produto_servico = '" + cursoMatricula.getIdProdutoServico() + "' AND A.lg_referencia = 0").executeQuery());
					if(rsmCursoEquivalente.next()){
						rsmCursoAnterior = new ResultSetMap(connect.prepareStatement("SELECT A.cd_curso, C.nm_produto_servico AS nm_curso FROM acd_curso A, acd_curso_etapa B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND A.cd_curso = C.cd_produto_servico AND B.cd_curso_etapa_posterior = " + rsmCursoEquivalente.getInt("cd_curso_etapa")).executeQuery());
						if(rsmCursoAnterior.next()){
							rsm.setValueToField("CD_CURSO_ANTERIOR", rsmCursoAnterior.getInt("cd_curso"));
							rsm.setValueToField("NM_CURSO_ANTERIOR", rsmCursoAnterior.getString("nm_curso"));
							rsm.setValueToField("NR_ORDEM_ANTERIOR", rsmCursoAnterior.getString("nr_ordem"));
						}
					}
				}
				
				
				ResultSetMap rsmMatriculaAnterior = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula MAT_ANT, acd_curso CUR_MAT_ANT, grl_produto_servico PROD_MAT_ANT, acd_instituicao_periodo PERIOD_MAT_ANT "
						+ "																		WHERE MAT_ANT.cd_matricula = " + rsm.getInt("cd_matricula_origem")
						+ " 																	  AND MAT_ANT.cd_curso = CUR_MAT_ANT.cd_curso"
						+ " 																	  AND MAT_ANT.cd_curso = PROD_MAT_ANT.cd_produto_servico"
						+ " 																	  AND MAT_ANT.cd_periodo_letivo = PERIOD_MAT_ANT.cd_periodo_letivo").executeQuery());
				if(rsmMatriculaAnterior.next()){
					rsm.setValueToField("ST_MATRICULA_ANTERIOR", rsmMatriculaAnterior.getInt("st_matricula"));
					rsm.setValueToField("CD_CURSO_MATRICULA_ANTERIOR", rsmMatriculaAnterior.getInt("cd_curso"));
					rsm.setValueToField("NM_CURSO_MATRICULA_ANTERIOR", rsmMatriculaAnterior.getString("nm_produto_servico"));
					rsm.setValueToField("NR_ORDEM_MATRICULA_ANTERIOR", rsmMatriculaAnterior.getString("nr_ordem"));
					rsm.setValueToField("NM_PERIODO_LETIVO_MATRICULA_ANTERIOR", rsmMatriculaAnterior.getString("nm_periodo_letivo"));
					
					rsm.setValueToField("NM_ST_ALUNO_CENSO_MATRICULA_ANTERIOR", (rsmMatriculaAnterior.getInt("CD_PERIODO_LETIVO") > 835 ? situacaoAlunoCenso[rsmMatriculaAnterior.getInt("ST_ALUNO_CENSO")] : "----"));
					rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO_MATRICULA_ANTERIOR", (rsmMatriculaAnterior.getInt("CD_PERIODO_LETIVO") > 835 ? situacaoAlunoCensoConversao[InstituicaoEducacensoServices.convertSituacaoAlunoCenso(rsmMatriculaAnterior.getInt("CD_MATRICULA"), rsmMatriculaAnterior.getInt("ST_ALUNO_CENSO"), connect)] : "----"));
					
					if(rsmMatriculaAnterior.getInt("ST_MATRICULA") == ST_CANCELADO){
						rsm.setValueToField("NM_ST_ALUNO_CENSO_MATRICULA_ANTERIOR", "----");
						rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO_MATRICULA_ANTERIOR", "----");
					}
					if(rsmMatriculaAnterior.getInt("ST_MATRICULA") == ST_REALOCACAO){
						rsm.setValueToField("NM_ST_ALUNO_CENSO_MATRICULA_ANTERIOR", "----");
						rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO_MATRICULA_ANTERIOR", "----");
					}
					if(rsmMatriculaAnterior.getInt("ST_MATRICULA") == ST_RECLASSIFICACAO){
						rsm.setValueToField("NM_ST_ALUNO_CENSO_MATRICULA_ANTERIOR", "----");
						rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO_MATRICULA_ANTERIOR", "----");
					}
					if(rsmMatriculaAnterior.getInt("ST_MATRICULA") == ST_INATIVO){
						rsm.setValueToField("NM_ST_ALUNO_CENSO_MATRICULA_ANTERIOR", "----");
						rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO_MATRICULA_ANTERIOR", "----");
					}
					if(rsmMatriculaAnterior.getInt("ST_MATRICULA") == ST_PENDENTE){
						rsm.setValueToField("NM_ST_ALUNO_CENSO_MATRICULA_ANTERIOR", "----");
						rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO_MATRICULA_ANTERIOR", "----");
					}
					
					int cdTipoOcorrenciaTransferencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CONCLUSAO_TRANSFERENCIA, connect).getCdTipoOcorrencia();
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + cdTipoOcorrenciaTransferencia, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_matricula_origem", "" + rsm.getInt("cd_matricula_origem"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_matricula_destino", "" + rsm.getInt("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmOcorrencia = OcorrenciaMatriculaServices.find(criterios, connect);
					if(rsmOcorrencia.next() && rsm.getInt("ST_MATRICULA_ANTERIOR") == ST_TRANSFERIDO && rsm.getInt("st_matricula") == ST_ATIVA){
						rsm.setValueToField("LG_PERMITE_CANCELAMENTO_RECEBIMENTO", 1);
					}
					else{
						rsm.setValueToField("LG_PERMITE_CANCELAMENTO_RECEBIMENTO", 0);
					}
					
				}
				else{
					rsm.setValueToField("LG_PERMITE_CANCELAMENTO_RECEBIMENTO", 0);
				}
				
				ResultSetMap rsmMatriculaPosterior = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula MAT_ANT, acd_curso CUR_MAT_ANT, grl_produto_servico PROD_MAT_ANT, acd_instituicao_periodo PERIOD_MAT_ANT "
						+ "																		WHERE MAT_ANT.cd_matricula_origem = " + rsm.getInt("cd_matricula")
						+ " 																	  AND MAT_ANT.cd_curso = CUR_MAT_ANT.cd_curso"
						+ " 																	  AND MAT_ANT.cd_curso = PROD_MAT_ANT.cd_produto_servico"
						+ " 																	  AND MAT_ANT.cd_periodo_letivo = PERIOD_MAT_ANT.cd_periodo_letivo").executeQuery());
				if(rsmMatriculaPosterior.next()){
					rsm.setValueToField("CD_CURSO_MATRICULA_POSTERIOR", rsmMatriculaPosterior.getInt("cd_curso"));
					rsm.setValueToField("NM_CURSO_MATRICULA_POSTERIOR", rsmMatriculaPosterior.getString("nm_produto_servico"));
					rsm.setValueToField("NR_ORDEM_MATRICULA_POSTERIOR", rsmMatriculaPosterior.getString("nr_ordem"));
					rsm.setValueToField("NM_PERIODO_LETIVO_MATRICULA_POSTERIOR", rsmMatriculaPosterior.getString("nm_periodo_letivo"));
					
					rsm.setValueToField("NM_ST_ALUNO_CENSO_MATRICULA_POSTERIOR", (rsmMatriculaPosterior.getInt("CD_PERIODO_LETIVO") > 835 ? situacaoAlunoCenso[rsmMatriculaPosterior.getInt("ST_ALUNO_CENSO")] : "----"));
					rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO_MATRICULA_POSTERIOR", (rsmMatriculaPosterior.getInt("CD_PERIODO_LETIVO") > 835 ? situacaoAlunoCensoConversao[InstituicaoEducacensoServices.convertSituacaoAlunoCenso(rsmMatriculaPosterior.getInt("CD_MATRICULA"), rsmMatriculaPosterior.getInt("ST_ALUNO_CENSO"), connect)] : "----"));
					
					if(rsmMatriculaPosterior.getInt("ST_MATRICULA") == ST_CANCELADO){
						rsm.setValueToField("NM_ST_ALUNO_CENSO_MATRICULA_POSTERIOR", "----");
						rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO_MATRICULA_POSTERIOR", "----");
					}
					if(rsmMatriculaPosterior.getInt("ST_MATRICULA") == ST_REALOCACAO){
						rsm.setValueToField("NM_ST_ALUNO_CENSO_MATRICULA_POSTERIOR", "----");
						rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO_MATRICULA_POSTERIOR", "----");
					}
					if(rsmMatriculaPosterior.getInt("ST_MATRICULA") == ST_RECLASSIFICACAO){
						rsm.setValueToField("NM_ST_ALUNO_CENSO_MATRICULA_POSTERIOR", "----");
						rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO_MATRICULA_POSTERIOR", "----");
					}
					if(rsmMatriculaPosterior.getInt("ST_MATRICULA") == ST_INATIVO){
						rsm.setValueToField("NM_ST_ALUNO_CENSO_MATRICULA_POSTERIOR", "----");
						rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO_MATRICULA_POSTERIOR", "----");
					}
					if(rsmMatriculaPosterior.getInt("ST_MATRICULA") == ST_PENDENTE){
						rsm.setValueToField("NM_ST_ALUNO_CENSO_MATRICULA_POSTERIOR", "----");
						rsm.setValueToField("NM_ST_ALUNO_CENSO_CONVERSAO_MATRICULA_POSTERIOR", "----");
					}
					
				}
								
				rsm.setValueToField("NM_PERIODO_LETIVO_ANTERIOR", String.valueOf(Integer.parseInt(rsm.getString("NM_PERIODO_LETIVO"))-1));
				rsm.setValueToField("NM_PERIODO_LETIVO_POSTERIOR", String.valueOf(Integer.parseInt(rsm.getString("NM_PERIODO_LETIVO"))+1));
			}
			rsm.beforeFirst();
			
			return rsm;
		
		}
		catch(SQLException e){
			if(isConnectionNull){
				Conexao.rollback(connect);
			}
			e.printStackTrace();
			return null;
		}
		catch(Exception e){
			if(isConnectionNull){
				Conexao.rollback(connect);
			}
			e.printStackTrace();
			return null;
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Utilizado para fazer busca de matrícula com menos campos, para que não atrase muito a busca
	 * @param criterios
	 * @return
	 */
	public static ResultSetMap findSimplificado(ArrayList<ItemComparator> criterios) {
		return findSimplificado(criterios, null);
	}
	
	public static ResultSetMap findSimplificado(ArrayList<ItemComparator> criterios, Connection connect) {
		String sqlAdicional = "";//Variavel para que o usuário do metodo possa construir as condições de SQL que deseja
		String limit = "";
		
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("sqlAdicional")){
				sqlAdicional = criterios.get(i).getValue();
			}
			else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("limit")){
				limit = "LIMIT " + criterios.get(i).getValue();
			}
			else
				crt.add(criterios.get(i));
				
		}
		
		ResultSetMap rsm = Search.find(
							"SELECT A.*, P.*, AL.*, PE.*, P.nm_pessoa AS NM_ALUNO, ('' || H.nm_produto_servico || ' - ' || G.nm_turma) AS NM_TURMA_COMPLETO, H.nm_produto_servico AS NM_CURSO " +
							"FROM acd_matricula							A " +
							"LEFT OUTER JOIN grl_pessoa					P ON (A.cd_aluno = P.cd_pessoa) " +
							"LEFT OUTER JOIN acd_aluno					AL ON (A.cd_aluno = AL.cd_aluno) " +
							"LEFT OUTER JOIN grl_pessoa_endereco		PE ON (A.cd_aluno = PE.cd_pessoa " + 
							"														AND PE.lg_principal = 1) " +
							"LEFT OUTER JOIN acd_turma					G ON (A.cd_turma = G.cd_turma) " +
							"LEFT OUTER JOIN grl_produto_servico		H ON (A.cd_curso = H.cd_produto_servico) " +
							"LEFT OUTER JOIN acd_instituicao_periodo	J ON (A.cd_periodo_letivo = J.cd_periodo_letivo) " +
							"WHERE 1 = 1 AND A.st_matricula <> " + ST_INATIVO + " " + sqlAdicional, " ORDER BY P.nm_pessoa " + limit, crt, connect!=null ? connect : Conexao.conectar(), connect==null);
		return rsm;
	}
	
	/**
	 * Método usado para fazer busca e trata-la para exibição no relatório
	 * @param criterios
	 * @return
	 */
	public static ResultSetMap findRelatorio(ArrayList<ItemComparator> criterios) {
		return findRelatorio(criterios, null);
	}

	public static ResultSetMap findRelatorio(ArrayList<ItemComparator> criterios, Connection connect) {
		String sqlAdicional = "";
		String limit = "";
		int nrAno = 0;
		int lgNecessidadeEspecial = -1;
		String dtMatriculaString = "";
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("sqlAdicional")){
				sqlAdicional = criterios.get(i).getValue();
			}
			else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("limit")){
				limit = "LIMIT " + criterios.get(i).getValue();
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("nrAno")) {
				nrAno = Integer.parseInt(criterios.get(i).getValue());
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("lgNecessidadeEspecial")) {
				lgNecessidadeEspecial = Integer.parseInt(criterios.get(i).getValue());
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("dtMatricula")) {
				dtMatriculaString = Util.convCalendarStringSql(Util.convStringSemFormatacaoToGregorianCalendar(criterios.get(i).getValue()));
			}
			else
				crt.add(criterios.get(i));
				
		}
		
		ResultSetMap rsm = Search.find(
							"SELECT A.*, A.cd_aluno AS cd_pessoa, D.nm_pessoa AS nm_aluno, H.nm_produto_servico AS nm_curso, G.nm_turma, L.nm_pessoa AS nm_instituicao, R.nm_etapa " +
							"FROM acd_matricula							A " +
							"LEFT OUTER JOIN acd_aluno					B ON (A.cd_aluno = B.cd_aluno) " +
							"LEFT OUTER JOIN grl_pessoa_fisica			C ON (B.cd_aluno = C.cd_pessoa) " +
							"LEFT OUTER JOIN grl_pessoa					D ON (C.cd_pessoa = D.cd_pessoa) " + 
							"LEFT OUTER JOIN grl_produto_servico		H ON (A.cd_curso = H.cd_produto_servico) " +
							"LEFT OUTER JOIN acd_turma					G ON (A.cd_turma = G.cd_turma) " +
							"LEFT OUTER JOIN grl_pessoa					L ON (G.cd_instituicao = L.cd_pessoa) " +
							"LEFT OUTER JOIN acd_instituicao_periodo	J ON (A.cd_periodo_letivo = J.cd_periodo_letivo) " +
							"LEFT OUTER JOIN acd_curso_etapa  		    Q ON (A.cd_curso = Q.cd_curso) " +
							"LEFT OUTER JOIN acd_tipo_etapa  		    R ON (Q.cd_etapa = R.cd_etapa) " +
							"WHERE 1 = 1 " + sqlAdicional + (nrAno != 0 ? " AND EXTRACT(YEAR FROM A.nr_matricula) = " + nrAno + " " : " ") + 
							(lgNecessidadeEspecial < 0 ? "" : " AND " + (lgNecessidadeEspecial == 0 ? " NOT " : "") + " EXISTS (SELECT * FROM grl_pessoa_necessidade_especial GPD WHERE A.cd_aluno = GPD.cd_pessoa) ") + 
							(dtMatriculaString != null && !dtMatriculaString.trim().equals("") ? " AND CAST(A.dt_matricula AS DATE) = '" + dtMatriculaString + "'" : ""), 
							"ORDER BY D.nm_pessoa " + limit, crt, connect!=null ? connect : Conexao.conectar(), connect==null);
		while(rsm.next()){
			rsm.setValueToField("NM_ST_MATRICULA", situacao[rsm.getInt("ST_MATRICULA")]);
			
			GregorianCalendar dtMatricula = rsm.getGregorianCalendar("dt_matricula");
			if(dtMatricula != null)
				rsm.setValueToField("DT_NM_MATRICULA", (Integer.parseInt(String.valueOf(dtMatricula.get(Calendar.DAY_OF_MONTH))) > 9 ? dtMatricula.get(Calendar.DAY_OF_MONTH) : "0" + dtMatricula.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtMatricula.get(Calendar.MONTH))) > 9 ? dtMatricula.get(Calendar.MONTH) : "0" + dtMatricula.get(Calendar.MONTH)) + "/" + dtMatricula.get(Calendar.YEAR));
			
			rsm.setValueToField("CL_TP_MATRICULA", tipo[rsm.getInt("TP_MATRICULA")]);
			rsm.setValueToField("CL_ST_MATRICULA", situacao[rsm.getInt("ST_MATRICULA")]);
			
			rsm.setValueToField("NM_TURMA_COMPLETO", rsm.getString("NM_CURSO") + " - " + rsm.getString("NM_TURMA"));
			
		}
		rsm.beforeFirst();
		
		return rsm;
	}

	public static ResultSetMap findGroupTurmasPrincipal(ArrayList<ItemComparator> criterios) {
		int cdInstituicaoSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
		
		int cdPeriodoLetivo = 0;
		ResultSetMap rsmPeriodoLetivo = InstituicaoPeriodoServices.getPeriodoOfInstituicao(cdInstituicaoSecretaria);
		if(rsmPeriodoLetivo.next())
			cdPeriodoLetivo = rsmPeriodoLetivo.getInt("cd_periodo_letivo");
		
		criterios.add(new ItemComparator("cdPeriodoLetivo", "" + cdPeriodoLetivo, ItemComparator.EQUAL, Types.INTEGER));
		return findGroupTurmasPrincipal(criterios, null);
	}

	public static ResultSetMap findGroupTurmasPrincipal(ArrayList<ItemComparator> criterios, Connection connect) {
		return findGroupTurmas(criterios, connect);
	}
	
	/**
	 * Busca para utilização no relatório de matrícula
	 * @param criterios
	 * @return
	 */
	public static ResultSetMap findGroupTurmas(ArrayList<ItemComparator> criterios) {
		return findGroupTurmas(criterios, null);
	}

	public static ResultSetMap findGroupTurmas(ArrayList<ItemComparator> criterios, Connection connect) {
		String limit = "";
		String nrAno = "";
		int lgNecessidadeEspecial = -1;
		String dtMatricula = "";
		int cdPeriodoLetivo = 0;
		
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("limit")){
				limit = "LIMIT " + criterios.get(i).getValue();
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("nrAno")) {
				nrAno = criterios.get(i).getValue();
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("lgNecessidadeEspecial")) {
				lgNecessidadeEspecial = Integer.parseInt(criterios.get(i).getValue());
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("dtMatricula")) {
				dtMatricula = Util.convCalendarStringSql(Util.convStringSemFormatacaoToGregorianCalendar(criterios.get(i).getValue()));
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("cdPeriodoLetivo")) {
				cdPeriodoLetivo = Integer.parseInt(criterios.get(i).getValue());
			}
			else
				crt.add(criterios.get(i));
				
		}
		
		ResultSetMap rsm = Search.find(
							"SELECT B.nr_inep, C.nm_produto_servico AS nm_curso, B.nm_turma, B.tp_turno, B.qt_vagas, E.nm_etapa, G.nm_pessoa AS nm_instituicao, COUNT(A.cd_matricula) AS nr_alunos " +
							"FROM acd_matricula							A " +
							"LEFT OUTER JOIN acd_turma					B ON (A.cd_turma = B.cd_turma) " +
							"LEFT OUTER JOIN grl_produto_servico        C ON (A.cd_curso = C.cd_produto_servico) " +
							"LEFT OUTER JOIN acd_curso_etapa            D ON (A.cd_curso = D.cd_curso) " +
							"LEFT OUTER JOIN acd_tipo_etapa             E ON (D.cd_etapa = E.cd_etapa) " +
							"LEFT OUTER JOIN acd_aluno                  F ON (A.cd_aluno = F.cd_aluno) " +
							"LEFT OUTER JOIN grl_pessoa					G ON (B.cd_instituicao = G.cd_pessoa) " +
							"LEFT OUTER JOIN acd_instituicao_periodo    H ON (B.cd_periodo_letivo = H.cd_periodo_letivo) " +
							"WHERE 1 = 1 " + (nrAno != null && !nrAno.trim().equals("") ? " AND EXTRACT(YEAR FROM A.nr_matricula) = " + nrAno + " " : " ") + 
							(lgNecessidadeEspecial < 0 ? "" : " AND " + (lgNecessidadeEspecial == 0 ? " NOT " : "") + " EXISTS (SELECT * FROM grl_pessoa_necessidade_especial GPD WHERE A.cd_aluno = GPD.cd_pessoa) ") + 
							(dtMatricula != null && !dtMatricula.trim().equals("") ? " AND CAST(A.dt_matricula AS DATE) = '" + dtMatricula + "'" : "") + " AND (st_matricula = " + ST_ATIVA + " OR st_matricula = " + ST_CONCLUIDA + ") " + 
							(cdPeriodoLetivo > 0 ? " AND (H.cd_periodo_letivo = " + cdPeriodoLetivo + " OR H.cd_periodo_letivo_superior = " + cdPeriodoLetivo + ")" : ""), 
							" GROUP BY B.nr_inep, C.nm_produto_servico, B.nm_turma, B.tp_turno, B.qt_vagas, E.nm_etapa, G.nm_pessoa ORDER BY G.nm_pessoa, B.nm_turma " + limit,
							crt, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		while(rsm.next()){
			rsm.setValueToField("CL_TP_TURNO", TurmaServices.tiposTurno[rsm.getInt("TP_TURNO")]);
			if(rsm.getInt("QT_VAGAS") > 0)
				rsm.setValueToField("PR_ALUNOS_VAGAS", Util.formatNumber((rsm.getFloat("NR_ALUNOS") / rsm.getFloat("QT_VAGAS")) * 100, 2) + "%");
			else
				rsm.setValueToField("PR_ALUNOS_VAGAS", "0%");
			
		}
		rsm.beforeFirst();
		
		ArrayList<String> fields = new ArrayList<String>();
		fields.add("NM_INSTITUICAO");
		fields.add("NM_CURSO");
		fields.add("NM_TURMA");
		rsm.orderBy(fields);
		rsm.beforeFirst();
		return rsm;
	}
	
	/**
	 * Metodo usado para busca das matrículas para exibição no dasboard
	 * Utiliza a estrutura de hierarquia, onde o rsm principal contem a instituição e cada uma contem um rsm com o conjunto de turmas
	 * Não vem ordenado, não é possivel da forma como esta pois não da para colocar o codigo da instituição no find (por conta do agrupamento)
	 * @param criterios
	 * @return
	 */
	public static ResultSetMap findSecretaria(ArrayList<ItemComparator> criterios) {
		return findSecretaria(criterios, null);
	}

	@SuppressWarnings("unchecked")
	public static ResultSetMap findSecretaria(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			int cdPeriodoLetivo = 0;
			int cdInstituicao = 0;
			int cdCurso = 0;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("cdPeriodoLetivo")) {
					cdPeriodoLetivo = Integer.parseInt(criterios.get(i).getValue());
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_CURSO")) {
					cdCurso = Integer.parseInt(criterios.get(i).getValue());
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("B.CD_INSTITUICAO")) {
					cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
					if(cdPeriodoLetivo == 0){
						ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdInstituicao, connect);
						if(rsmPeriodoRecente.next()){
							cdPeriodoLetivo = rsmPeriodoRecente.getInt("cd_periodo_letivo");
						}
					}
				}
			}
			
			ResultSetMap rsm = new ResultSetMap();
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT D.qt_vagas, count(*) AS qt_alunos, D.cd_turma, D.nm_turma, G.nm_etapa, E.nm_produto_servico, E.nm_produto_servico AS NM_CURSO, D.tp_turno, A.st_matricula  "+
							"FROM acd_matricula A  "+
							"LEFT OUTER JOIN acd_instituicao_periodo B ON (A.cd_periodo_letivo = B.cd_periodo_letivo)  "+
							"LEFT OUTER JOIN grl_pessoa C ON (B.cd_instituicao = C.cd_pessoa)  "+
							"JOIN acd_turma D ON (A.cd_turma = D.cd_turma)  "+
							"JOIN acd_instituicao_educacenso DE ON (D.cd_instituicao = DE.cd_instituicao AND D.cd_periodo_letivo = DE.cd_periodo_letivo)  "+
							"LEFT OUTER JOIN grl_produto_servico E ON (D.cd_curso = E.cd_produto_servico)  "+
							"LEFT OUTER JOIN acd_curso_etapa F ON (A.cd_curso = F.cd_curso)  "+
							"LEFT OUTER JOIN acd_tipo_etapa G ON (F.cd_etapa = G.cd_etapa)  "+
							"WHERE D.nm_turma NOT LIKE 'TRANS%' "+
							"  AND D.ST_TURMA <> "+TurmaServices.ST_INATIVO+
							"  "+(cdInstituicao > 0 ? " AND D.cd_instituicao = " + cdInstituicao : "" )+
							"  AND DE.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE +
							(cdCurso > 0 ? " AND A.cd_curso = " + cdCurso : "" )+
							" AND (B.cd_periodo_letivo = " + cdPeriodoLetivo + " OR B.cd_periodo_letivo_superior = " + cdPeriodoLetivo + ")  "+
							"GROUP BY D.qt_vagas, D.cd_turma, D.nm_turma, G.nm_etapa, E.nm_produto_servico, D.tp_turno, A.st_matricula "+
							"ORDER BY E.nm_produto_servico, D.nm_turma, D.qt_vagas, D.cd_turma, G.nm_etapa, D.tp_turno");
			
			//Hash usado para relacionar uma instituição a um conjunto de turmas
			HashMap<Integer, HashMap<String, Object>> hashPrincipal = new HashMap<Integer, HashMap<String,Object>>();
			
			//Chamado de rsmMatriculas mas que na verdade faz um agrupamento ao nível da turma
			ResultSetMap rsmMatriculas = new ResultSetMap(pstmt.executeQuery());
			
			//Marca os códigos da turma para impedir duplicidade de soma por conta do agrupamento em st_matricula (haveria repetição da quantidade de turmas quando as mesmas tivessem matriculas com varias situações) 
			ArrayList<Integer> codigosTurma = new ArrayList<Integer>();
			
			while(rsmMatriculas.next()){
				Turma turma = TurmaDAO.get(rsmMatriculas.getInt("cd_turma"), connect);
				if(turma == null)
					continue;
				Pessoa instituicao = PessoaDAO.get(turma.getCdInstituicao(), connect);
				rsmMatriculas.setValueToField("CD_INSTITUICAO", instituicao.getCdPessoa());
				rsmMatriculas.setValueToField("NM_INSTITUICAO", instituicao.getNmPessoa());
				
				//Faz a inserção inicial da instituição no hashPrincipal
				if(!hashPrincipal.containsKey(instituicao.getCdPessoa())){
					HashMap<String, Object> registerInstituicao = new HashMap<String, Object>();
					registerInstituicao.put("CD_INSTITUICAO", instituicao.getCdPessoa());
					registerInstituicao.put("NM_INSTITUICAO", instituicao.getNmPessoa());
					registerInstituicao.put("NM_IDENTIFICADOR", instituicao.getNmPessoa());
					registerInstituicao.put("QT_ST_MATRICULA_ATIVA", 0);
					registerInstituicao.put("NR_ALUNOS", 0);
					registerInstituicao.put("NR_ALUNOS_ABSOLUTO", 0);
					registerInstituicao.put("NR_TURMAS", 0);
					registerInstituicao.put("QT_VAGAS", 0);
					registerInstituicao.put("PR_ALUNOS_VAGAS", Util.formatNumber(0, 2) + "%");
					registerInstituicao.put("VL_PR_ALUNOS_VAGAS", 0);
					registerInstituicao.put("MATRICULAS", new ResultSetMap().getLines());
					
					if(cdInstituicao != cdSecretaria){
						
						ResultSetMap rsmAlunosProblemasCadastro = new ResultSetMap(connect.prepareStatement("SELECT COUNT(A.cd_aluno) AS QT_ALUNOS FROM ACD_ALUNO A, ACD_MATRICULA B WHERE A.cd_aluno = B.cd_aluno AND A.lg_cadastro_problema = 1 AND B.st_matricula = "+ST_ATIVA+" AND B.cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery());
						if(rsmAlunosProblemasCadastro.next())
							registerInstituicao.put("QT_PROBLEMAS_CADASTRO_CENSO", rsmAlunosProblemasCadastro.getInt("QT_ALUNOS"));
					}
					
					hashPrincipal.put(instituicao.getCdPessoa(), registerInstituicao);
				}
				
				//Utiliza o hash daquela instituição (com o bloco anterior, é garantido que sempre haverá um hash correspondente)
				HashMap<String, Object> registerInstituicao = hashPrincipal.get(instituicao.getCdPessoa());
				
				//Impede a duplicação da contagem de vagas no somatorio de instituicao
				Turma turmaMatricula = TurmaDAO.get(rsmMatriculas.getInt("cd_turma"), connect);
				if(!codigosTurma.contains(rsmMatriculas.getInt("cd_turma"))){
					if(turmaMatricula != null){
						registerInstituicao.put("QT_VAGAS", Integer.parseInt(String.valueOf(registerInstituicao.get("QT_VAGAS"))) + turmaMatricula.getQtVagas());
						codigosTurma.add(rsmMatriculas.getInt("cd_turma"));
						registerInstituicao.put("NR_TURMAS", (((Integer)registerInstituicao.get("NR_TURMAS")) + 1));
						registerInstituicao.put("CODIGOS_TURMA", codigosTurma);
					}
				}
				int qtAlunos = 0;
				int qtAlunosAbsoluto = 0;
				//Apenas soma a quantidade de alunos com matriculas ativas
				if(((rsmMatriculas.getInt("st_matricula") == ST_ATIVA) || (rsmMatriculas.getInt("st_matricula") == ST_CONCLUIDA))){
					qtAlunos = rsmMatriculas.getInt("qt_alunos");
					if(turmaMatricula.getCdCurso() != ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0) && turmaMatricula.getCdCurso() != ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)){
						qtAlunosAbsoluto = rsmMatriculas.getInt("qt_alunos");
					}
				}
				
				registerInstituicao.put("NR_ALUNOS", Integer.parseInt(String.valueOf(registerInstituicao.get("NR_ALUNOS"))) + qtAlunos);
				registerInstituicao.put("NR_ALUNOS_ABSOLUTO", Integer.parseInt(String.valueOf(registerInstituicao.get("NR_ALUNOS_ABSOLUTO"))) + qtAlunosAbsoluto);
				
				registerInstituicao.put("QT_ST_MATRICULA_ATIVA", Integer.parseInt(String.valueOf(registerInstituicao.get("QT_ST_MATRICULA_ATIVA"))) + qtAlunos);
				//Calculo de preenchimento de vagas (na instituição, sempre refaz o calculo quando encontrado novas matriculas dessa instituição no rsmMatriculas)
				double vlPrAlunoVaga = (Double.parseDouble(String.valueOf(registerInstituicao.get("NR_ALUNOS"))) / Double.parseDouble(String.valueOf(registerInstituicao.get("QT_VAGAS")))) * 100;
				registerInstituicao.put("PR_ALUNOS_VAGAS", Util.formatNumber(vlPrAlunoVaga, 2) + "%");
				registerInstituicao.put("VL_PR_ALUNOS_VAGAS", vlPrAlunoVaga);
				
				
				rsmMatriculas.setValueToField("CL_TP_TURNO", TurmaServices.tiposTurno[rsmMatriculas.getInt("TP_TURNO")]);
				rsmMatriculas.setValueToField("NR_ALUNOS",qtAlunos);
				rsmMatriculas.setValueToField("QT_ST_MATRICULA_ATIVA", qtAlunos);
				//Calculo de preenchimento de vagas no nivel da turma
				vlPrAlunoVaga = ((double)qtAlunos / (double)rsmMatriculas.getInt("QT_VAGAS")) * 100;
				rsmMatriculas.setValueToField("PR_ALUNOS_VAGAS", Util.formatNumber(vlPrAlunoVaga, 2) + "%");
				rsmMatriculas.setValueToField("VL_PR_ALUNOS_VAGAS", vlPrAlunoVaga);
				
				rsmMatriculas.setValueToField("NM_IDENTIFICADOR", rsmMatriculas.getString("NM_CURSO") + " - Turma " + rsmMatriculas.getString("NM_TURMA") + " (" + rsmMatriculas.getString("CL_TP_TURNO") + ")");
				ArrayList<HashMap<String, Object>> rsmRegistro = (ArrayList<HashMap<String, Object>>)registerInstituicao.get("MATRICULAS");
				boolean achou = false;
				for(HashMap<String, Object> registerRsm : rsmRegistro){
					//Reune registros do rsmMatriculas da mesma turma
					if(Integer.parseInt(String.valueOf(registerRsm.get("CD_TURMA"))) == rsmMatriculas.getInt("CD_TURMA")){
						registerRsm.put("NR_ALUNOS",Integer.parseInt(String.valueOf(registerRsm.get("NR_ALUNOS"))) + rsmMatriculas.getInt("NR_ALUNOS"));
						registerRsm.put("QT_ST_MATRICULA_ATIVA",Integer.parseInt(String.valueOf(registerRsm.get("QT_ST_MATRICULA_ATIVA"))) + rsmMatriculas.getInt("QT_ST_MATRICULA_ATIVA"));
						registerRsm.put("QT_VAGAS",Integer.parseInt(String.valueOf(registerRsm.get("QT_VAGAS"))));
						vlPrAlunoVaga = (Double.parseDouble(String.valueOf(registerRsm.get("NR_ALUNOS"))) / (double)Double.parseDouble(String.valueOf(registerRsm.get("QT_VAGAS")))) * 100;
						registerRsm.put("PR_ALUNOS_VAGAS", Util.formatNumber(vlPrAlunoVaga, 2) + "%");
						registerRsm.put("VL_PR_ALUNOS_VAGAS", vlPrAlunoVaga);
						
						achou = true;
						break;
					}
					
				}
				if(!achou)
					rsmRegistro.add(rsmMatriculas.getRegister());
				
				registerInstituicao.put("MATRICULAS", rsmRegistro);
			}
			
			for(Integer cdInstituicaoHash : hashPrincipal.keySet()){
				rsm.addRegister(hashPrincipal.get(cdInstituicaoHash));
			}
			rsm.beforeFirst();
			
			while(rsm.next()){
				//Turmas que não possuem alunos matriculados
				ResultSetMap rsmTurmasNaoCadastradas = new ResultSetMap(connect.prepareStatement("SELECT A.cd_turma, A.nm_turma, A.cd_curso, A.cd_instituicao, A.tp_turno, A.qt_vagas, PS.nm_produto_servico, PS.nm_produto_servico AS NM_CURSO, TE.nm_etapa, INS_P.nm_pessoa AS NM_INSTITUICAO FROM acd_turma A, "
						+ "																								 acd_instituicao_educacenso IPE,"
						+ "																								 acd_instituicao_periodo IP,"
						+ "																								 grl_produto_servico PS,"
						+ "																								 acd_curso_etapa CE, "
						+ "																								 acd_tipo_etapa TE, "
						+ "																								 grl_pessoa INS_P "
						+ "																			WHERE A.cd_periodo_letivo = IP.cd_periodo_letivo "
						+ "																			  AND A.cd_instituicao = " + rsm.getInt("cd_instituicao")
						+ "																			  AND A.cd_instituicao = IPE.cd_instituicao "
						+ " 																		  AND A.cd_periodo_letivo = IPE.cd_periodo_letivo "
						+ "																			  AND IPE.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
						+ "																			  AND A.cd_curso = PS.cd_produto_servico "
						+ "																			  AND A.cd_curso = CE.cd_curso "
						+ "																			  AND CE.cd_etapa = TE.cd_etapa "
						+ "																			  AND A.cd_instituicao = INS_P.cd_pessoa "
						+ "																			  AND A.nm_turma <> 'TRANSFERENCIA'"
						+ "																			  AND (IP.cd_periodo_letivo = "+cdPeriodoLetivo+" "
						+ "																					OR IP.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") "
						+ "																			  AND A.st_turma = " + TurmaServices.ST_ATIVO
						+ "																			  AND cd_turma NOT IN ("+((ArrayList<Integer>)rsm.getObject("CODIGOS_TURMA")).toString().substring(1, ((ArrayList<Integer>)rsm.getObject("CODIGOS_TURMA")).toString().length()-1)+")").executeQuery());
				while(rsmTurmasNaoCadastradas.next()){
					rsm.setValueToField("QT_VAGAS", (rsm.getInt("QT_VAGAS") + TurmaDAO.get(rsmTurmasNaoCadastradas.getInt("cd_turma"), connect).getQtVagas()));
					rsm.setValueToField("NR_TURMAS", (rsm.getInt("NR_TURMAS") + 1));
					
					double vlPrAlunoVaga = (Double.parseDouble(rsm.getString("NR_ALUNOS")) / (double)Double.parseDouble(rsm.getString("QT_VAGAS"))) * 100;
					rsm.setValueToField("PR_ALUNOS_VAGAS", Util.formatNumber(vlPrAlunoVaga, 2) + "%");
					rsm.setValueToField("VL_PR_ALUNOS_VAGAS", vlPrAlunoVaga);
					
					rsmTurmasNaoCadastradas.setValueToField("CL_TURNO", TurmaServices.tiposTurno[rsmTurmasNaoCadastradas.getInt("TP_TURNO")]);
					
					ArrayList<HashMap<String, Object>> rsmRegistro = (ArrayList<HashMap<String, Object>>)rsm.getObject("MATRICULAS");
					
					HashMap<String, Object> registerNovo = new HashMap<String, Object>();
					registerNovo.put("NR_ALUNOS", 0);
					registerNovo.put("NR_ALUNOS_ABSOLUTO", 0);
					registerNovo.put("QT_ST_MATRICULA_ATIVA", 0);
					registerNovo.put("QT_VAGAS", rsmTurmasNaoCadastradas.getInt("QT_VAGAS"));
					registerNovo.put("PR_ALUNOS_VAGAS", "0%");
					registerNovo.put("VL_PR_ALUNOS_VAGAS", 0);
					registerNovo.put("NM_IDENTIFICADOR", rsmTurmasNaoCadastradas.getString("NM_CURSO") + " - Turma " + rsmTurmasNaoCadastradas.getString("NM_TURMA") + " ("+rsmTurmasNaoCadastradas.getString("CL_TURNO")+")");
					registerNovo.put("NM_ETAPA", rsmTurmasNaoCadastradas.getString("NM_ETAPA"));
					registerNovo.put("NM_INSTITUICAO", rsmTurmasNaoCadastradas.getString("NM_INSTITUICAO"));
					registerNovo.put("NM_CURSO", rsmTurmasNaoCadastradas.getString("NM_CURSO"));
					registerNovo.put("CD_INSTITUICAO", rsmTurmasNaoCadastradas.getString("CD_INSTITUICAO"));
					registerNovo.put("NM_PRODUTO_SERVICO", rsmTurmasNaoCadastradas.getString("NM_PRODUTO_SERVICO"));
					registerNovo.put("TP_TURNO", rsmTurmasNaoCadastradas.getInt("TP_TURNO"));
					registerNovo.put("NM_TURMA", rsmTurmasNaoCadastradas.getString("NM_TURMA"));
					registerNovo.put("CD_TURMA", rsmTurmasNaoCadastradas.getString("CD_TURMA"));
					registerNovo.put("QT_ALUNOS", 0);
					rsmRegistro.add(registerNovo);
					rsm.setValueToField("MATRICULAS", rsmRegistro);
				}
			}
			rsm.beforeFirst();
			
			return rsm;
		
		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
		
	@Deprecated
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, ArrayList<String> groupByFields,
			ArrayList<String> orderByFields) {
		return find(criterios, groupByFields, orderByFields, null);
	}

	@Deprecated
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, ArrayList<String> groupByFields,
			ArrayList<String> orderByFields, Connection connection) {
		String fields = "A.*, C.nr_cpf, C.nr_rg, C.tp_sexo, C.sg_orgao_rg, C.dt_nascimento, " +
				" H.sg_estado AS sg_estado_rg, " +
				"D.nm_pessoa AS nm_aluno, I.nm_produto_servico AS nm_curso, E.tp_contratacao, F.nm_turma, F.tp_turno, " +
				"F.st_turma, G.nm_area_interesse, M.nm_pessoa AS nm_funcionario, N.nm_periodo_letivo, D.nr_telefone1, " +
				"D.nr_telefone2, D.nr_celular, D.nr_fax, D.nm_email, J.cd_usuario, " +
				"P.ds_endereco, P.nm_logradouro, P.nm_bairro, P.nr_cep, P.nr_endereco, P.nm_complemento, " +
			    "P.nm_ponto_referencia, Q.nm_tipo_logradouro, Q.sg_tipo_logradouro, R.nm_logradouro AS nm_logradouro_enderecamento, " +
			    "S.nm_tipo_logradouro AS nm_tipo_logradouro_enderecamento, S.sg_tipo_logradouro AS sg_tipo_logradouro_enderecamento, " +
			    "T.nm_bairro AS nm_bairro_enderecamento, U.nm_cidade, V.nm_estado, V.sg_estado ";
		String groups = "";
		String [] retorno = Util.getFieldsAndGroupBy(groupByFields, fields, groups,
					        "COUNT(*) AS qt_matriculas");
		fields = retorno[0];
		groups = retorno[1];

		ResultSetMap rsm = Search.find("SELECT " + fields + " " +
				"FROM acd_matricula A " +
				"LEFT OUTER JOIN acd_matricula_periodo_letivo J ON (A.cd_matricula = J.cd_matricula) " +
				"LEFT OUTER JOIN seg_usuario L ON (J.cd_usuario = L.cd_usuario) " +
				"LEFT OUTER JOIN grl_pessoa M ON (L.cd_pessoa = M.cd_pessoa) " +
				"JOIN acd_aluno B ON (A.cd_aluno = B.cd_aluno) " +
				"JOIN grl_pessoa_fisica C ON (B.cd_aluno = C.cd_pessoa) " +
				"JOIN grl_pessoa D ON (C.cd_pessoa = D.cd_pessoa) " +
			    "LEFT OUTER JOIN grl_pessoa_endereco P ON (D.cd_pessoa = P.cd_pessoa AND P.lg_principal = 1) " +
			    "LEFT OUTER JOIN grl_tipo_logradouro Q ON (P.cd_tipo_logradouro = Q.cd_tipo_logradouro) " +
			    "LEFT OUTER JOIN grl_logradouro R ON (P.cd_logradouro = R.cd_logradouro) " +
			    "LEFT OUTER JOIN grl_tipo_logradouro S ON (R.cd_tipo_logradouro = S.cd_tipo_logradouro) " +
			    "LEFT OUTER JOIN grl_bairro T ON (P.cd_bairro = T.cd_bairro) " +
			    "LEFT OUTER JOIN grl_cidade U ON (P.cd_cidade = U.cd_cidade) " +
			    "LEFT OUTER JOIN grl_estado V ON (U.cd_estado = V.cd_estado) " +
				"JOIN acd_curso E ON (A.cd_curso = E.cd_curso) " +
				"JOIN grl_produto_servico I ON (E.cd_curso = I.cd_produto_servico) " +
				"JOIN acd_turma F ON (A.cd_turma = F.cd_turma) " +
				"LEFT OUTER JOIN acd_instituicao_periodo N ON (F.cd_periodo_letivo = N.cd_periodo_letivo) " +
				"LEFT OUTER JOIN acd_area_interesse G ON (A.cd_area_interesse = G.cd_area_interesse) " +
				"LEFT OUTER JOIN grl_estado H ON (C.cd_estado_rg = H.cd_estado) " +
			    "WHERE 1 = 1 ", groups, criterios,
			    connection!=null ? connection : Conexao.conectar(), connection==null);
		
		if (orderByFields != null && orderByFields.size()>0) {
			rsm.orderBy(orderByFields);
		}
		return rsm;
	}

	/**
	 * Produz um novo número de matrícula baseado nas informações do aluno e do ano de ingresso
	 * @param idPeriodoLetivo
	 * @param nrInep
	 * @param cdMatricula
	 * @return Um número de matrícula
	 */
	public static String getNrMatricula(String idPeriodoLetivo, String nrInep, int cdMatricula) {
		return (idPeriodoLetivo != null ? idPeriodoLetivo.substring(idPeriodoLetivo.length() - 2) : "") + (nrInep != null ? new DecimalFormat("0000").format(Integer.parseInt(String.valueOf(nrInep).substring(String.valueOf(nrInep).length() - 4))): "") + new DecimalFormat("000000").format(Integer.parseInt(String.valueOf(cdMatricula).substring(String.valueOf(cdMatricula).length() > 6 ? String.valueOf(cdMatricula).length() - 6 : 0)));
	}
	
	public static Matricula getMatricula(int cdAluno, int cdTurma, int cdPeriodoLetivo) {
		return getMatricula(cdAluno, cdTurma, cdPeriodoLetivo, null);
	}
	
	public static Matricula getMatricula(int cdAluno, int cdTurma, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula WHERE cd_aluno=? AND cd_turma=? AND cd_periodo_letivo=? ORDER BY st_matricula, dt_matricula DESC");
			pstmt.setInt(1, cdAluno);
			pstmt.setInt(2, cdTurma);
			pstmt.setInt(3, cdPeriodoLetivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return MatriculaDAO.get(rs.getInt("cd_matricula"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo que busca no periodo letivo passado, se determinado aluno esta matriculado
	 * O metodo percorre todos os periodos que são irmãos, além do que é passado
	 * @param cdAluno
	 * @param cdPeriodoLetivo
	 * @return
	 */
	public static boolean temMatriculaRegularNoPeriodo(int cdAluno, int cdPeriodoLetivo, int cdMatricula) {
		return temMatriculaRegularNoPeriodo(cdAluno, cdPeriodoLetivo, cdMatricula, null);
	}
	
	public static boolean temMatriculaRegularNoPeriodo(int cdAluno, int cdPeriodoLetivo, int cdMatricula, Connection connect) {
		
		boolean isConnectionNull = connect == null;
		try{
			int cdInstituicaoSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect);
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nm_periodo_letivo", "" + instituicaoPeriodo.getNmPeriodoLetivo(), ItemComparator.EQUAL, Types.VARCHAR));
			criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicaoSecretaria, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = InstituicaoPeriodoDAO.find(criterios, connect);
			if(rsm.next()){
				ResultSetMap rsmMatriculaNoPeriodo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula A, acd_instituicao_periodo B "
						+ "																		  WHERE A.cd_periodo_letivo = B.cd_periodo_letivo "
						+ "																		    AND A.cd_aluno = " + cdAluno
						+ "																		    AND A.st_matricula IN (" + ST_ATIVA + ", " + ST_PENDENTE + ", " + ST_CONCLUIDA + ", " + ST_EM_TRANSFERENCIA + ")"
						+ "																		    AND B.cd_periodo_letivo_superior = " + rsm.getInt("cd_periodo_letivo")
						+ "																		    AND (A.cd_curso <> " + ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0) + " AND A.cd_curso <> " + ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0) + ")"
						+ (cdMatricula > 0 ? "														AND A.cd_matricula <> " + cdMatricula : "")).executeQuery());
				return rsmMatriculaNoPeriodo.next();
			}
			
			return false;
		}
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace();
			return true;
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result gerarHistorico(int cdMatricula, int cdPeriodoLetivo, int cdInstituicao) {
		return gerarHistorico(cdMatricula, cdPeriodoLetivo, cdInstituicao, null);
	}
	
	/**
	 * Gero o histórico de um aluno em um determinado periodo letivo
	 * @param cdMatricula
	 * @param cdPeriodoLetivo
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static Result gerarHistorico(int cdMatricula, int cdPeriodoLetivo, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("D.cd_periodo_letivo_superior", "" + cdPeriodoLetivo, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmInstituicaoPeriodo = InstituicaoPeriodoServices.find(criterios, connect);
			if(rsmInstituicaoPeriodo.next()){
				cdPeriodoLetivo = rsmInstituicaoPeriodo.getInt("cd_periodo_letivo");
			}
			
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect);
			Instituicao instituicao = InstituicaoDAO.get(instituicaoPeriodo.getCdInstituicao(), connect);
			PessoaFisica secretario = PessoaFisicaDAO.get(instituicao.getCdSecretario(), connect);
			PessoaFisica diretor = PessoaFisicaDAO.get(instituicao.getCdDiretor(), connect);
			
			ResultSetMap rsm = new ResultSetMap();
			
			ResultSetMap rsmMatriculaDisciplina = MatriculaDisciplinaServices.getAllByMatricula(cdMatricula, connect);
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("A.CD_PERIODO_LETIVO");
			fields.add("C.NM_PRODUTO_SERVICO");
			rsmMatriculaDisciplina.orderBy(fields);
			HashMap<String, Object> param = new HashMap<String, Object>();
			while(rsmMatriculaDisciplina.next()){
				
				Curso curso = CursoDAO.get(rsmMatriculaDisciplina.getInt("cd_curso"), connect);
				CursoDisciplina cursoDisciplina = CursoDisciplinaDAO.get(curso.getCdCurso(), rsmMatriculaDisciplina.getInt("cd_curso_modulo"), rsmMatriculaDisciplina.getInt("cd_disciplina"), rsmMatriculaDisciplina.getInt("cd_matriz"), connect);
				
				if(cdPeriodoLetivo == rsmMatriculaDisciplina.getInt("cd_periodo_letivo")){
					param.put("NR_ANO_CONCLUIDO", (curso.getNrOrdem() + 1));
				}
					
				
				if(!param.containsKey("NR_ANO_" + curso.getNrOrdem()))
					param.put("NR_ANO_" + curso.getNrOrdem(), instituicaoPeriodo.getNmPeriodoLetivo());
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_oferta", rsmMatriculaDisciplina.getString("cd_oferta"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_curso", rsmMatriculaDisciplina.getString("cd_curso"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_curso_modulo", rsmMatriculaDisciplina.getString("cd_curso_modulo"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_disciplina", rsmMatriculaDisciplina.getString("cd_disciplina"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_matriz", rsmMatriculaDisciplina.getString("cd_matriz"), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmOfertaAvaliacao = OfertaAvaliacaoServices.find(criterios, connect);
				while(rsmOfertaAvaliacao.next()){
					DisciplinaAvaliacaoAluno disciplinaAvaliacaoAluno = DisciplinaAvaliacaoAlunoDAO.get(rsmMatriculaDisciplina.getInt("cd_matricula_disciplina"), rsmOfertaAvaliacao.getInt("cd_oferta_avaliacao"), rsmOfertaAvaliacao.getInt("cd_oferta"), connect);
					Disciplina disciplina = DisciplinaDAO.get(rsmMatriculaDisciplina.getInt("cd_disciplina"), connect);
					
					rsm.setValueToField("QT_CARGA_HORARIO", cursoDisciplina.getQtCargaHoraria());
					rsm.setValueToField("NM_CLASSIFICACAO", CursoDisciplinaServices.tiposClassificacao[cursoDisciplina.getTpClassificacao()]);
					rsm.setValueToField("NM_DISCIPLINA", disciplina.getNmProdutoServico());
					rsm.setValueToField("NR_NOTA_" + curso.getNrOrdem(), rsmOfertaAvaliacao.getString("vl_conceito"));
					
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			Matricula matricula = MatriculaDAO.get(cdMatricula, connect);
			PessoaFisica aluno = PessoaFisicaDAO.get(matricula.getCdAluno(), connect);
			Cidade cidadeNaturalidade = CidadeDAO.get(aluno.getCdNaturalidade(), connect);
			Estado estadoNaturalidade = EstadoDAO.get(cidadeNaturalidade.getCdEstado(), connect);
			Pais paisNaturalidade = PaisDAO.get(estadoNaturalidade.getCdPais(), connect);
			
			param.put("NM_ALUNO", aluno.getNmPessoa());
			param.put("NR_RG", aluno.getNrRg());
			if(cidadeNaturalidade != null)
				param.put("NM_NATURALIDADE", cidadeNaturalidade.getNmCidade());
			if(estadoNaturalidade != null)
				param.put("SG_ESTADO_NATURALIDADE", cidadeNaturalidade.getNmCidade());
			if(paisNaturalidade != null)
				param.put("NM_PAIS_NATURALIDADE", cidadeNaturalidade.getNmCidade());
			param.put("NM_MAE", aluno.getNmMae());
			param.put("DT_NASCIMENTO", Util.formatDate(aluno.getDtNascimento(), "dd/MM/yyyy"));
			param.put("TXT_OBSERVACAO", matricula.getTxtObservacao());
			param.put("NM_INSTITUICAO", instituicao.getNmPessoa());
			param.put("NM_PERIODO_LETIVO", instituicaoPeriodo.getNmPeriodoLetivo());
			param.put("DT_HOJE", Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"));
			param.put("NR_RG_SECRETARIO", (secretario!=null ? secretario.getNmPessoa() : ""));
			param.put("NR_RG_DIRETOR", (diretor!=null ? diretor.getNmPessoa() : ""));
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsmMatriculaDisciplina);
			result.addObject("params", param);
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar gerar histórico!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	@Deprecated
	public static boolean verifyExistenciaVagas(int cdTurma, int cdMatricula) {
		return verifyExistenciaVagas(cdTurma, cdMatricula, null);
	}

	@Deprecated
	public static boolean verifyExistenciaVagas(int cdTurma, int cdMatricula, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) " +
					"FROM acd_matricula " +
					"WHERE cd_turma = ? " +
					"  AND st_matricula =? " +
					(cdMatricula>0 ? "  AND cd_matricula = ?" : ""));
			pstmt.setInt(1, cdTurma);
			pstmt.setInt(2, ST_ATIVA);
			if (cdMatricula > 0)
				pstmt.setInt(3, cdMatricula);
			ResultSet rs = pstmt.executeQuery();
			int countMatriculas = rs.next() ? rs.getInt(1) : 0;
			Turma turma = TurmaDAO.get(cdTurma, connection);
			return turma.getQtVagas() > countMatriculas;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return false;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static ResultSetMap getAllContasReceberForPrint(int cdMatricula) {
		return getAllContasReceberForPrint(cdMatricula, null);
	}

	@Deprecated
	public static ResultSetMap getAllContasReceberForPrint(int cdMatricula, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			ResultSetMap rsm = new ResultSetMap();

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.vl_conta + A.vl_acrescimo - A.vl_abatimento AS vl_liquido, " +
					"A.dt_vencimento, A.cd_conta_receber, B.nm_tipo_documento " +
					"FROM adm_conta_receber A " +
					"join acd_matricula_periodo_letivo L ON (A.cd_contrato = L.cd_contrato) " +
					"LEFT OUTER JOIN adm_tipo_documento B ON (A.cd_tipo_documento = B.cd_tipo_documento) " +
					"WHERE L.cd_matricula = ? " +
					"  AND A.tp_conta_receber = 1 " +
					"  AND A.cd_documento_saida IS NULL " +
					"  ORDER BY A.dt_vencimento");
			pstmt.setInt(1, cdMatricula);
			ResultSetMap rsmTemp = new ResultSetMap(pstmt.executeQuery());
			while (rsmTemp.next()) {
				float vlTotal = rsmTemp.getFloat("vl_liquido");
				float vlPago = 0;
				pstmt = connection.prepareStatement("SELECT A.vl_movimento, B.nm_forma_pagamento, B.tp_forma_pagamento, C.*, " +
						"A.DT_MOVIMENTO " +
						"FROM adm_movimento_conta A, adm_forma_pagamento B, adm_movimento_conta_receber C " +
						"WHERE A.cd_forma_pagamento = B.cd_forma_pagamento " +
						"  AND A.cd_movimento_conta = C.cd_movimento_conta " +
						"  AND A.cd_conta = C.cd_conta " +
						"  AND C.cd_conta_receber = ?");
				pstmt.setInt(1, rsmTemp.getInt("cd_conta_receber"));
				ResultSetMap rsmMov = new ResultSetMap(pstmt.executeQuery());
				while (rsmMov.next()) {
					float vlPagoConta = rsmMov.getFloat("vl_recebido") - rsmMov.getFloat("vl_juros") - rsmMov.getFloat("vl_multa") +
										rsmMov.getFloat("vl_desconto") - rsmMov.getFloat("vl_tarifa_cobranca");
					float vlMovimento = rsmMov.getFloat("vl_movimento");
					String nmFormaPagamento = rsmMov.getString("nm_forma_pagamento");
					nmFormaPagamento = nmFormaPagamento==null ? "" : nmFormaPagamento;
					if (rsmMov.getInt("tp_forma_pagamento") != FormaPagamentoServices.MOEDA_CORRENTE) {
						pstmt = connection.prepareStatement("SELECT C.vl_conta + C.vl_acrescimo - C.vl_abatimento AS vl_liquido, " +
								"C.dt_vencimento " +
								"FROM adm_movimento_titulo_credito A, adm_titulo_credito B, adm_conta_receber C " +
								"WHERE A.cd_titulo_credito = B.cd_titulo_credito " +
								"  AND B.cd_conta_receber = C.cd_conta_receber " +
								"  AND A.cd_movimento_conta = ? " +
								"  AND A.cd_conta = ?");
						pstmt.setInt(1, rsmMov.getInt("cd_movimento_conta"));
						pstmt.setInt(2, rsmMov.getInt("cd_conta"));
						ResultSetMap rsmTitulos = new ResultSetMap(pstmt.executeQuery());
						while (rsmTitulos.next()) {
							rsmTitulos.getRegister().put("NM_FORMA_PAGAMENTO", nmFormaPagamento);
							rsmTitulos.getRegister().put("VL_LIQUIDO", rsmTitulos.getFloat("VL_LIQUIDO") - ((vlMovimento - vlPagoConta)/rsmTitulos.size()));
							rsm.addRegister(rsmTitulos.getRegister());
						}
					}
					else {
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("NM_FORMA_PAGAMENTO", nmFormaPagamento);
						register.put("VL_LIQUIDO", vlPagoConta);
						register.put("DT_VENCIMENTO", rsmMov.getTimestamp("DT_MOVIMENTO"));
						rsm.addRegister(register);
					}
					vlPago += vlPagoConta;
				}
				if (vlTotal - vlPago > 0) {
					rsmTemp.getRegister().put("VL_LIQUIDO", vlTotal - vlPago);
					rsmTemp.getRegister().put("NM_FORMA_PAGAMENTO", rsmTemp.getString("nm_tipo_documento", ""));
					rsm.addRegister(rsmTemp.getRegister());
				}
			}

			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DT_VENCIMENTO");
			rsm.orderBy(fields);

			rsm.beforeFirst();
			for (int i=1; rsm.next(); i++)
				rsm.getRegister().put("NR_PARCELA", i);
			rsm.beforeFirst();

			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaServices.getAllContasReceberForPrint: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static Result insert(Matricula matricula, MatriculaPeriodoLetivo matriculaPeriodoLetivo, Contrato contrato,
			ArrayList<ContratoDesconto> descontos, ArrayList<Integer> disciplinas, ArrayList<Integer> periodos,
			HashMap<String, Object> dadosPedidoVenda, int cdPlanoPagamento) {
		return insert(matricula, matriculaPeriodoLetivo, contrato, descontos, disciplinas, periodos, dadosPedidoVenda, cdPlanoPagamento, null);
	}

	@Deprecated
	@SuppressWarnings("unchecked")
	public static Result insert(Matricula matricula, MatriculaPeriodoLetivo matriculaPeriodoLetivo, Contrato contrato,
			ArrayList<ContratoDesconto> descontos, ArrayList<Integer> disciplinas, ArrayList<Integer> modulos,
			HashMap<String, Object> dadosPedidoVenda, int cdPlanoPagamento, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			if (!verifyExistenciaVagas(matricula.getCdTurma(), 0 /*cdMatricula*/, connection)) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(ERR_INEXISTENCIA_VAGAS, "Vaga inexistente!");
			}

			Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connection);
			if (aluno==null) {
				PreparedStatement pstmt = connection.prepareStatement("INSERT INTO acd_aluno (cd_aluno) VALUES(?)");
				pstmt.setInt(1, matricula.getCdAluno());
				pstmt.execute();
			}
			/*
			 * PEDIDO DE VENDA
			 *
			int cdPedidoVenda    = matriculaPeriodoLetivo.getCdPedidoVenda();
			String nrPedidoVenda = "";
			ArrayList<PedidoVendaItem> itens  = dadosPedidoVenda==null ? null : (ArrayList<PedidoVendaItem>)dadosPedidoVenda.get("itens");
			GregorianCalendar dtLimiteEntrega = dadosPedidoVenda==null ? null : (GregorianCalendar)dadosPedidoVenda.get("dtLimiteEntrega");
			int cdTipoOperacao = dadosPedidoVenda==null || dadosPedidoVenda.get("cdTipoOperacao")==null ? 0 : (Integer)dadosPedidoVenda.get("cdTipoOperacao");
			Usuario usuario    = matriculaPeriodoLetivo.getCdUsuario()<=0 ? null : UsuarioDAO.get(matriculaPeriodoLetivo.getCdUsuario(), connection);
			int cdVendedor     = usuario==null ? 0 : usuario.getCdPessoa();
			DocumentoSaida docSaida = null;
			PedidoVenda pedidoVenda = null;
			if (cdPedidoVenda<=0 && itens!=null && itens.size()>0) {
				nrPedidoVenda = PedidoVendaServices.getProximoNrPedido(matricula.getCdInstituicao(), connection);
				cdPedidoVenda = PedidoVendaDAO.insert((pedidoVenda = new PedidoVenda(0 /*cdPedidoVenda* /,
						matricula.getCdAluno() /*cdCliente* /, matricula.getDtMatricula() /*dtPedido* /,
						dtLimiteEntrega /*dtLimiteEntrega* /, nrPedidoVenda /*idPedidoVenda* /, 0 /*vlAcrescimo* /,
						0 /*vlDesconto* /, PedidoVendaServices.TP_PEDIDO_VENDA, PedidoVendaServices.ST_FECHADO,
						0 /*lgWeb* /, "" /*txtObservacao* /, 0 /*cdEnderecoEntrega* /, 0 /*cdEnderecoCobranca* /,
						matricula.getCdInstituicao() /*cdEmpresa* /, cdVendedor /*cdVendedor* /, cdTipoOperacao /*cdTipoOperacao* /,
						nrPedidoVenda /*nrPedidoVenda* /,  0 /*cdPlanoPagamento* /, null /*dtAutorizacao* /)));
				if (cdPedidoVenda<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return null;
				}

				for(int i=0; itens!=null && i<itens.size(); i++){
					PedidoVendaItem item = (PedidoVendaItem)itens.get(i);
					int cdRetorno = 0;
					if(PedidoVendaItemDAO.get(item.getCdPedidoVenda(), item.getCdEmpresa(), item.getCdProdutoServico(), connection)==null){
						item.setCdPedidoVenda(cdPedidoVenda);
						cdRetorno = PedidoVendaItemDAO.insert(item, connection);
					}
					else
						cdRetorno = PedidoVendaItemDAO.update(item, connection);
					if (cdRetorno <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
				}

				/* fechamento pedido venda
				Object objFechamentoPedido = PedidoVendaServices.fecharPedido(cdPedidoVenda, true, false, connection);
				if (objFechamentoPedido==null || ((Integer)((HashMap<String, Object>)objFechamentoPedido).get("code")).intValue()<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return null;
				}
				pedidoVenda.setStPedidoVenda(PedidoVendaServices.ST_FECHADO);
				docSaida = (DocumentoSaida)((HashMap<String, Object>)objFechamentoPedido).get("docSaida");

				matriculaPeriodoLetivo.setCdPedidoVenda(cdPedidoVenda);
			}
			*/
			/*
			 * SALVANDO MATRÍCULA
			 */
			matricula.setNrMatricula(getNrMatricula(matricula.getDtMatricula(), connection));
			Result result = new Result(MatriculaDAO.insert(matricula, connection));
			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}
			matricula.setCdMatricula(result.getCode());
			/*
			 * SALVANDO CONTRATO
			 */
			if(contrato!=null)	{
				int cdContrato = 0;
				contrato.setDtAssinatura(contrato.getDtAssinatura()==null ? matricula.getDtMatricula() : contrato.getDtAssinatura());
				contrato.setNrContrato(contrato.getNrContrato()!=null && !contrato.getNrContrato().trim().equals("") ?
						contrato.getNrContrato() : matricula.getNrMatricula()!=null &&
						!matricula.getNrMatricula().trim().equals("") ? matricula.getNrMatricula().trim() :
						Integer.toString(matricula.getCdMatricula()));
				contrato.setDtInicioVigencia(contrato.getDtInicioVigencia()!=null ? contrato.getDtInicioVigencia() :
											 matricula.getDtMatricula());
				contrato.setCdPessoa(contrato.getCdPessoa()<=0 ? matricula.getCdAluno() : contrato.getCdPessoa());
				Object objContrato = null;
				if ((objContrato = ContratoServices.insert(contrato, 0 /*cdUsuario*/, false /*registerDoc*/, descontos, null, connection))==null ||
					((Integer)((HashMap<String, Object>)objContrato).get("cdContrato")).intValue()<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return null;
				}
				cdContrato = ((Integer)((HashMap<String, Object>)objContrato).get("cdContrato")).intValue();
				if(matriculaPeriodoLetivo!=null)
					matriculaPeriodoLetivo.setCdContrato(cdContrato);
			}
			/*
			 * MATRÍCULA - PERÍODO
			 */
			if(matriculaPeriodoLetivo!=null)	{
				matriculaPeriodoLetivo.setCdMatricula(matricula.getCdMatricula());
				result = new Result(MatriculaPeriodoLetivoDAO.insert(matriculaPeriodoLetivo, connection));
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar salvar matrícula período!");
				}
			}
			/*
			 * SALVANDO DISCIPLINAS
			 */
			for (int i=0; disciplinas!=null && i<disciplinas.size(); i++) {
				if (MatriculaDisciplinaServices.insert(new MatriculaDisciplina(0 /*cdMatriculaDisciplina*/, 
										matricula.getCdMatricula(), matriculaPeriodoLetivo.getCdPeriodoLetivo(),
										0 /*cdConceito*/, null /*dtInicio*/, null /*dtConclusao*/, 0 /*nrFaltas*/,
										MatriculaDisciplinaServices.TP_NORMAL /*tpMatricula*/,  0F /*vlConceito*/,
										0 /*qtChComplemento*/, 0F /*vlConceitoAproveitamento*/, "" /*nmInstituicaoAproveitamento*/,
										MatriculaDisciplinaServices.ST_EM_CURSO /*stMatriculaDisciplina*/, 0 /*cdProfessor*/,
										0 /*cdSupervidorPratica*/, 0 /*cdInstituicaoPratica*/, matricula.getCdMatriz(), matricula.getCdCurso(), 
										matricula.getCdPeriodoLetivo(), disciplinas.get(i).intValue() /*cdDisciplina*/,
										0 /*0*/, 0/*lgAprovado*/), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar incluir disciplinas na matrícula!");
				}
			}
			/*
			 * SALVANDO MODULOS
			 */
			for (int i=0; modulos!=null && i<modulos.size(); i++) {
				if (MatriculaModuloServices.save(new MatriculaModulo(matricula.getCdMatricula(), modulos.get(i) /*cdPeriodo*/,
						matriculaPeriodoLetivo.getCdPeriodoLetivo()), connection).getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar incluir disciplinas na matrícula!");
				}
			}
			/*
			 * SALVANDO CONDIÇÕES DE PAGAMENTO
			 */
			if (cdPlanoPagamento>0)
				if (ContratoPlanoPagtoDAO.insert(new ContratoPlanoPagto(contrato!=null?contrato.getCdContrato():0,cdPlanoPagamento,
						                                                0 /*cdFormaPagamento*/,0 /*cdPagamento*/, contrato.getVlContrato() /*vlPagamento*/,
						                                                0 /*cdUsuario*/, 0 /*cdMovimentoConta*/, 0 /*cdConta*/), connection)<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar salvar plano de pagamento do contrato!");
				}

			if (isConnectionNull)
				connection.commit();
			result.addObject("cdContrato", contrato!=null?contrato.getCdContrato() : 0);
			result.addObject("nrMatricula", matricula.getNrMatricula());
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar incluir matrícula!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static Result insert(Matricula matricula, int cdCursoModulo){
		return insert(matricula, cdCursoModulo, null);
	}
	
	@Deprecated
	public static Result insert(Matricula matricula, int cdCursoModulo, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			if (!verifyExistenciaVagas(matricula.getCdTurma(), 0 /*cdMatricula*/, connection)) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(ERR_INEXISTENCIA_VAGAS, "Vaga inexistente!");
			}

			Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connection);
			if (aluno==null) {
				PreparedStatement pstmt = connection.prepareStatement("INSERT INTO acd_aluno (cd_aluno) VALUES(?)");
				pstmt.setInt(1, matricula.getCdAluno());
				pstmt.execute();
			}
			/*
			 * SALVANDO MATRÍCULA
			 */
			matricula.setNrMatricula(getNrMatricula(matricula.getDtMatricula(), connection));
			Result result = new Result(MatriculaDAO.insert(matricula, connection));
			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}
			matricula.setCdMatricula(result.getCode());
			/*
			 * MATRÍCULA - PERÍODO LETIVO
			 */
			MatriculaPeriodoLetivo matPeriodoLetivo = new MatriculaPeriodoLetivo(matricula.getCdMatricula(), matricula.getCdPeriodoLetivo(),
								0 /*cdContrato*/,matricula.getDtMatricula(), MatriculaPeriodoLetivoServices.ST_ATIVO,0 /*cdUsuario*/, 0/*cdMotivoTrancamento*/,0/*cdPedidoVenda*/);
			MatriculaPeriodoLetivoDAO.insert(matPeriodoLetivo, connection);
			/*
			 * MATRÍCULA - PERÍODO/ETAPA DO CURSO
			 */
			if(cdCursoModulo>0)	{
				MatriculaModulo matriculaModulo = new MatriculaModulo(matricula.getCdMatricula(),cdCursoModulo,matricula.getCdPeriodoLetivo());
				result = MatriculaModuloServices.save(matriculaModulo, connection);
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return result;
				}
				/*
				 * SALVANDO DISCIPLINAS
				 */
				ResultSet rs = connection.prepareStatement("SELECT * FROM acd_curso_disciplina " +
						                                   "WHERE cd_curso = "+matricula.getCdCurso()+
						                                   "  AND cd_curso_modulo = "+cdCursoModulo).executeQuery();
				while(rs.next())	{
					//
				}

			}

			if (isConnectionNull)
				connection.commit();
			result.addObject("nrMatricula", matricula.getNrMatricula());
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar incluir matrícula!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static final int updateNumerosMatriculas() {
		return updateNumerosMatriculas(null);
	}

	@Deprecated
	public static final int updateNumerosMatriculas(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_matricula, A.dt_matricula " +
																  "FROM acd_matricula A " +
																  "ORDER BY A.cd_matricula");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm.next()) {
				pstmt = connection.prepareStatement("SELECT MAX(nr_matricula) AS last " +
													"FROM acd_matricula " +
													"WHERE dt_matricula >= ? " +
													"  AND dt_matricula <= ? " +
													"  AND cd_matricula < ?");
				GregorianCalendar dtMatricula = rsm.getGregorianCalendar("dt_matricula")==null ? new GregorianCalendar() : rsm.getGregorianCalendar("dt_matricula");
				pstmt.setTimestamp(1, Util.convCalendarToTimestamp(new GregorianCalendar(dtMatricula.get(Calendar.YEAR), Calendar.JANUARY,
						1)));
				pstmt.setTimestamp(2, Util.convCalendarToTimestamp(new GregorianCalendar(dtMatricula.get(Calendar.YEAR), Calendar.DECEMBER, 31)));
				pstmt.setInt(3, rsm.getInt("cd_matricula"));
				ResultSetMap rsmTemp = new ResultSetMap(pstmt.executeQuery());
				int lastMatricula = rsmTemp==null || !rsmTemp.next() || rsmTemp.getString("last")==null || rsmTemp.getString("last").trim().equals("") ? 101 :
					Integer.parseInt(rsmTemp.getString("last").substring(0, 4)) + 1;
				String nrMatricula = new DecimalFormat("0000").format(lastMatricula) + Integer.toString(dtMatricula.get(Calendar.YEAR)).substring(2);
				Matricula matricula = MatriculaDAO.get(rsm.getInt("cd_matricula"), connection);
				matricula.setNrMatricula(nrMatricula);
				if (MatriculaDAO.update(matricula, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
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
	
	@Deprecated
	public static Result update(Matricula matricula){
		return update(matricula, null);
	}
	
	@Deprecated
	public static Result update(Matricula matricula, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			if (!verifyExistenciaVagas(matricula.getCdTurma(), 0 /*cdMatricula*/, connection)) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(ERR_INEXISTENCIA_VAGAS, "Vaga inexistente!");
			}

			/*
			 * SALVANDO MATRÍCULA
			 */
			matricula.setNrMatricula(getNrMatricula(matricula.getDtMatricula(), connection));
			Result result = new Result(MatriculaDAO.update(matricula, connection));
			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}

			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar incluir matrícula!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static Result update(Matricula matricula, MatriculaPeriodoLetivo matriculaPeriodoLetivo, Contrato contrato,
			ArrayList<ContratoDesconto> descontos, HashMap<String, Object> dadosPedidoVenda, int cdPlanoPagamento) {
		return update(matricula, matriculaPeriodoLetivo, contrato, descontos, null, null, dadosPedidoVenda, cdPlanoPagamento, null);
	}

	@Deprecated
	public static Result update(Matricula matricula, MatriculaPeriodoLetivo matriculaPeriodoLetivo, Contrato contrato,
			ArrayList<ContratoDesconto> descontos, ArrayList<Integer> disciplinas, ArrayList<Integer> periodos,
			HashMap<String, Object> dadosPedidoVenda, int cdPlanoPagamento) {
		return update(matricula, matriculaPeriodoLetivo, contrato, descontos, disciplinas, periodos, dadosPedidoVenda, cdPlanoPagamento, null);
	}

	@Deprecated
	@SuppressWarnings("unchecked")
	public static Result update(Matricula matricula, MatriculaPeriodoLetivo matriculaPeriodoLetivo, Contrato contrato,
			ArrayList<ContratoDesconto> descontos, ArrayList<Integer> disciplinas, ArrayList<Integer> periodos,
			HashMap<String, Object> dadosPedidoVenda, int cdPlanoPagamento, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			if (!verifyExistenciaVagas(matricula.getCdTurma(), matricula.getCdMatricula(), connection)) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(ERR_INEXISTENCIA_VAGAS, "Vaga inexistente!");
			}

			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connection);
			if (turma.getCdTabelaPreco()>0) {
				float vlDesconto = 0;
				for (int i=0; descontos!=null && i<descontos.size(); i++)
					vlDesconto += contrato.getVlContrato() * (descontos.get(i).getPrDesconto()/(float)100);
				PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM adm_tabela_preco_regra " +
																	  "WHERE cd_produto_servico = "+matricula.getCdCurso()+
																	  "  AND cd_tabela_preco    = "+turma.getCdTabelaPreco()+
																	  "  AND cd_fornecedor        IS NULL " +
																	  "  AND cd_grupo             IS NULL " +
																	  "  AND cd_tabela_preco_base IS NULL");
				ResultSet rs = pstmt.executeQuery();
				float prDescontoMax = !rs.next() ? 0 : rs.getFloat("pr_desconto");
				if (prDescontoMax>0 && (contrato.getVlContrato() * (prDescontoMax/(float)100)) < vlDesconto) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(ERR_DESCONTO_INVALIDO, "Desconto inválido!");
				}
			}
			Result result = new Result (MatriculaDAO.update(matricula, connection));
			if (MatriculaDAO.update(matricula, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}
			if(contrato!=null)	{
				contrato.setDtAssinatura(contrato.getDtAssinatura()==null ? matricula.getDtMatricula() : contrato.getDtAssinatura());
				contrato.setNrContrato(contrato.getNrContrato()!=null && !contrato.getNrContrato().trim().equals("") ?
						contrato.getNrContrato() : matricula.getNrMatricula()!=null &&
						!matricula.getNrMatricula().trim().equals("") ? matricula.getNrMatricula().trim() :
						Integer.toString(matricula.getCdMatricula()));
				contrato.setDtInicioVigencia(contrato.getDtInicioVigencia()!=null ? contrato.getDtInicioVigencia() :
											 matricula.getDtMatricula());
				contrato.setCdPessoa(contrato.getCdPessoa()<=0 ? matricula.getCdAluno() : contrato.getCdPessoa());
				System.out.println(contrato);
				Object objContrato = null;
				if ((objContrato = ContratoServices.update(contrato, descontos, null, connection))==null ||
						((Integer)((HashMap<String, Object>)objContrato).get("cdContrato")).intValue()<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return null;
				}
			}
			boolean saveMatrPeriodoLetivo = false;
			/*
			int cdPedidoVenda = matriculaPeriodoLetivo.getCdPedidoVenda();
			String nrPedidoVenda = "";
			ArrayList<PedidoVendaItem> itens = dadosPedidoVenda==null ? null : (ArrayList<PedidoVendaItem>)dadosPedidoVenda.get("itens");
			GregorianCalendar dtLimiteEntrega = dadosPedidoVenda==null ? null : (GregorianCalendar)dadosPedidoVenda.get("dtLimiteEntrega");
			int cdTipoOperacao = dadosPedidoVenda==null ? null : (Integer)dadosPedidoVenda.get("cdTipoOperacao");
			Usuario usuario = matriculaPeriodoLetivo.getCdUsuario()<=0 ? null : UsuarioDAO.get(matriculaPeriodoLetivo.getCdUsuario(), connection);
			int cdVendedor = usuario==null ? 0 : usuario.getCdPessoa();
			PedidoVenda pedidoVenda = null;
			DocumentoSaida docSaida = null;
			if(itens!=null && itens.size()>0) {
				pedidoVenda = cdPedidoVenda<=0 ? null : PedidoVendaDAO.get(cdPedidoVenda, connection);
				if (pedidoVenda == null) {
					nrPedidoVenda = PedidoVendaServices.getProximoNrPedido(matricula.getCdInstituicao(), connection);
					cdPedidoVenda = PedidoVendaDAO.insert((pedidoVenda = new PedidoVenda(0 /*cdPedidoVenda* /, matricula.getCdAluno() /*cdCliente* /,
																						 matricula.getDtMatricula() /*dtPedido* /,
																						 dtLimiteEntrega /*dtLimiteEntrega* /,
																						 nrPedidoVenda /*idPedidoVenda* /,0 /*vlAcrescimo* /,
																						 0 /*vlDesconto* /,PedidoVendaServices.TP_PEDIDO_VENDA,
																						 PedidoVendaServices.ST_ABERTO, 0 /*lgWeb* /,
																						 "" /*txtObservacao* /, 0 /*cdEnderecoEntrega* /,
																						 0 /*cdEnderecoCobranca* /, matricula.getCdInstituicao() /*cdEmpresa* /,
																						 cdVendedor /*cdVendedor* /, cdTipoOperacao /*cdTipoOperacao* /,
																						 nrPedidoVenda /*nrPedidoVenda* /, 0 /*cdPlanoPagamento* /, null /*dtAutorizacao* /)));
					if (cdPedidoVenda<=0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}

					for(int i=0; itens!=null && i<itens.size(); i++){
						PedidoVendaItem item = (PedidoVendaItem)itens.get(i);
						int cdRetorno = 0;
						if(PedidoVendaItemDAO.get(item.getCdPedidoVenda(), item.getCdEmpresa(), item.getCdProdutoServico(), connection)==null){
							item.setCdPedidoVenda(cdPedidoVenda);
							cdRetorno = PedidoVendaItemDAO.insert(item, connection);
						}
						else
							cdRetorno = PedidoVendaItemDAO.update(item, connection);
						if (cdRetorno <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return null;
						}
					}
				}
				else {
					pedidoVenda.setDtLimiteEntrega(dtLimiteEntrega);
					pedidoVenda.setCdTipoOperacao(cdTipoOperacao);
					if (PedidoVendaDAO.update(pedidoVenda, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
				}

				if (pedidoVenda.getStPedidoVenda() != PedidoVendaServices.ST_FECHADO) {
					Object objFechPedido = PedidoVendaServices.fecharPedido(cdPedidoVenda, true, false, connection);
					if (objFechPedido==null || ((Integer)((HashMap<String, Object>)objFechPedido).get("code")).intValue()<=0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
					if (pedidoVenda.getStPedidoVenda() != PedidoVendaServices.ST_FECHADO)
						pedidoVenda.setStPedidoVenda(PedidoVendaServices.ST_FECHADO);
					docSaida = (DocumentoSaida)((HashMap<String, Object>)objFechPedido).get("docSaida");
				}
				else {
					PreparedStatement pstmt = connection.prepareStatement(
							"SELECT DISTINCT A.cd_documento_saida " +
							"FROM alm_documento_saida_item A, adm_venda_saida_item B " +
							"WHERE A.cd_documento_saida = B.cd_documento_saida " +
							"  AND A.cd_produto_servico = B.cd_produto_servico " +
							"  AND A.cd_empresa         = B.cd_empresa " +
							"  AND A.cd_item            = B.cd_item " +
							"  AND B.cd_pedido_venda    = "+pedidoVenda.getCdPedidoVenda());
					ResultSet rs = pstmt.executeQuery();
					int cdDocumentoSaida = !rs.next() ? 0 : rs.getInt("cd_documento_saida");
					docSaida = cdDocumentoSaida<=0 ? null : DocumentoSaidaDAO.get(cdDocumentoSaida, connection);
					float vlTotalPedido = 0;

					/* Atualizacao de itens de pedido/documento de saida * /
					for(int i=0; itens!=null && i<itens.size(); i++){
						vlTotalPedido += itens.get(i).getQtSolicitada() * itens.get(i).getVlUnitario() + itens.get(i).getVlAcrescimo() - itens.get(i).getVlDesconto();
						PedidoVendaItem item = PedidoVendaItemDAO.get(cdPedidoVenda, pedidoVenda.getCdEmpresa(), itens.get(i).getCdProdutoServico(), connection);
						int cdRetorno = 0;
						if(item==null){
							item.setCdPedidoVenda(cdPedidoVenda);
							cdRetorno = PedidoVendaItemDAO.insert(item, connection);
						}
						else {
							cdRetorno = PedidoVendaItemDAO.update(itens.get(i), connection);
							item = cdRetorno>0 ? itens.get(i) : item;
						}
						if (cdRetorno <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return null;
						}

						pstmt = connection.prepareStatement("SELECT cd_documento_saida, qt_saida, cd_item " +
															"FROM adm_venda_saida_item " +
															"WHERE cd_pedido_venda    = "+cdPedidoVenda+
															"  AND cd_produto_servico = "+item.getCdProdutoServico()+
															"  AND cd_empresa         = "+pedidoVenda.getCdEmpresa());
						rs = pstmt.executeQuery();
						int cdItem = !rs.next() ? 0 : rs.getInt("cd_item");
						if (cdItem <= 0) {
							pstmt = connection.prepareStatement("SELECT * FROM alm_documento_saida_item " +
																"WHERE cd_documento_saida = "+cdDocumentoSaida+
																"  AND cd_produto_servico = "+item.getCdProdutoServico()+
																"  AND cd_empresa         = "+pedidoVenda.getCdEmpresa());
							rs = pstmt.executeQuery();
							cdItem = !rs.next() ? 0 : rs.getInt("cd_item");
							if (cdItem<=0) {
								if ((cdItem = DocumentoSaidaItemDAO.insert(new DocumentoSaidaItem(cdDocumentoSaida,item.getCdProdutoServico(),
										pedidoVenda.getCdEmpresa(), item.getQtSolicitada() /*qtSaida* /, item.getVlUnitario(),
										item.getVlAcrescimo(), item.getVlDesconto(), item.getDtEntregaPrevista(), item.getCdUnidadeMedida(),
										0 /*cdTabelaPreco* /, 0 /*cdItem* /))) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return null;
								}
							}
							else {
								if (DocumentoSaidaItemDAO.update(new DocumentoSaidaItem(cdDocumentoSaida, item.getCdProdutoServico(),
										pedidoVenda.getCdEmpresa(), item.getQtSolicitada() /*qtSaida* /, item.getVlUnitario(),
										item.getVlAcrescimo(), item.getVlDesconto(), item.getDtEntregaPrevista(), item.getCdUnidadeMedida(),
										0 /*cdTabelaPreco* /, cdItem), connection) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return null;
								}
							}
							if (VendaSaidaItemDAO.insert(new VendaSaidaItem(pedidoVenda.getCdPedidoVenda(), pedidoVenda.getCdEmpresa(),
									item.getCdProdutoServico(), cdDocumentoSaida, item.getQtSolicitada() /*qtSaida* /, cdItem), connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return null;
							}
						}
						else {
							if (DocumentoSaidaItemDAO.update(new DocumentoSaidaItem(cdDocumentoSaida, item.getCdProdutoServico(),
									pedidoVenda.getCdEmpresa(), item.getQtSolicitada() /*qtSaida* /, item.getVlUnitario(),
									item.getVlAcrescimo(), item.getVlDesconto(), item.getDtEntregaPrevista(),
									item.getCdUnidadeMedida(), 0 /*cdTabelaPreco* /, cdItem), connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return null;
							}
							if (VendaSaidaItemDAO.update(new VendaSaidaItem(pedidoVenda.getCdPedidoVenda(), pedidoVenda.getCdEmpresa(),
									item.getCdProdutoServico(), cdDocumentoSaida, item.getQtSolicitada() /*qtSaida* /, cdItem), connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return null;
							}
						}
					}

					if (docSaida!=null && DocumentoSaidaServices.updatTotaisDocumentoSaida(cdDocumentoSaida, vlTotalPedido, 0 /*vlAcrescimo* /, 0 /*vlDesconto* /, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
				}

				matriculaPeriodoLetivo.setCdPedidoVenda(cdPedidoVenda);
			}
			else {
				if (matriculaPeriodoLetivo.getCdPedidoVenda()>0) {
					int cdPedidoVendaTemp = matriculaPeriodoLetivo.getCdPedidoVenda();
					cdPedidoVenda = 0;
					matriculaPeriodoLetivo.setCdPedidoVenda(cdPedidoVenda);
					if (MatriculaPeriodoLetivoDAO.update(matriculaPeriodoLetivo, connection).getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
					saveMatrPeriodoLetivo = true;

					if (PedidoVendaServices.delete(cdPedidoVendaTemp, true, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
				}
			}
			*/
			if (!saveMatrPeriodoLetivo && MatriculaPeriodoLetivoDAO.update(matriculaPeriodoLetivo, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			if (disciplinas != null) {
				PreparedStatement pstmt = connection.prepareStatement("DELETE FROM acd_matricula_disciplina " +
																	  "WHERE cd_matricula = "+matricula.getCdMatricula()+
																	  "  AND cd_periodo_letivo = "+matriculaPeriodoLetivo.getCdPeriodoLetivo());
				pstmt.execute();

				for (int i=0; i<disciplinas.size(); i++) {
					if (MatriculaDisciplinaServices.insert(new MatriculaDisciplina(0 /*cdMatriculaDisciplina*/, 
															matricula.getCdMatricula(), matriculaPeriodoLetivo.getCdPeriodoLetivo(),
															0 /*cdConceito*/, null /*dtInicio*/, null /*dtConclusao*/, 0 /*nrFaltas*/,
															MatriculaDisciplinaServices.TP_NORMAL /*tpMatricula*/,  0F /*vlConceito*/,
															0 /*qtChComplemento*/, 0F /*vlConceitoAproveitamento*/, "" /*nmInstituicaoAproveitamento*/,
															MatriculaDisciplinaServices.ST_EM_CURSO /*stMatriculaDisciplina*/, 0 /*cdProfessor*/,
															0 /*cdSupervidorPratica*/, 0 /*cdInstituicaoPratica*/, matricula.getCdMatriz(), matricula.getCdCurso(), 
															matricula.getCdPeriodoLetivo(), disciplinas.get(i).intValue() /*cdDisciplina*/,
															0 /*0*/, 0/*lgAprovado*/), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
				}
			}
			
			/*
			 * ATUALIZANDO MATRÍCULAS EM MODULOS
			 */
			if (periodos != null) {
				connection.prepareStatement("DELETE FROM acd_matricula_periodo " +
											"WHERE cd_matricula      = "+matriculaPeriodoLetivo.getCdMatricula()+
											"  AND cd_periodo_letivo = "+matriculaPeriodoLetivo.getCdPeriodoLetivo()).execute();;
				for (int i=0; i<periodos.size(); i++) {
					if (MatriculaModuloServices.save(new MatriculaModulo(matricula.getCdMatricula(), periodos.get(i) /*cdPeriodo*/,
							matriculaPeriodoLetivo.getCdPeriodoLetivo()), connection).getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
				}
			}
			/*
			 * ATUALIZANDO PLANOS DE PAGAMENTOS DO CONTRATO
			 */
			if (cdPlanoPagamento>0) {
				connection.prepareStatement("DELETE FROM adm_contrato_plano_pagto " +
											"WHERE cd_contrato = "+contrato.getCdContrato()).execute();
				if (ContratoPlanoPagtoDAO.insert(new ContratoPlanoPagto(contrato.getCdContrato(), cdPlanoPagamento, 0 /*cdFormaPagamento*/,
																		0 /*cdPagamento*/, contrato.getVlContrato() /*vlPagamento*/,
																		0 /*cdUsuario*/, 0 /*cdMovimentoConta*/,0 /*cdConta*/), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return null;
				}
			}
			if (isConnectionNull)
				connection.commit();
			result.addObject("cdMatricula", matricula.getCdMatricula());
			result.addObject("nrMatricula", matricula.getNrMatricula());
			result.addObject("cdContrato", contrato.getCdContrato());
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar atualizar matrícula!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static Result delete(int cdMatricula) {
		return delete(cdMatricula, null);
	}
	
	@Deprecated
	public static Result delete(int cdMatricula, Connection con){
		boolean isConnectionNull = con==null;
		try {
			con = isConnectionNull ? Conexao.conectar() : con;
			con.setAutoCommit(isConnectionNull ? false : con.getAutoCommit());
			/* EXCLUINDO DISCIPLINAS DO CURRÍCULO */
			con.prepareStatement("DELETE FROM acd_matricula_curriculo WHERE cd_matricula = "+cdMatricula).execute();
			/* BUSCANDO MATRÍCULAS EM MODULOS */
			ResultSet rs = con.prepareStatement("SELECT cd_periodo_letivo, cd_contrato, cd_matricula_periodo_letivo " +
					 							"FROM acd_matricula_periodo_letivo " +
					 							"WHERE cd_matricula = "+cdMatricula).executeQuery();
			while(rs.next()) {
				// Tenta excluir contratos
				if (ContratoServices.delete(rs.getInt("cd_contrato"), false, con) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(con);
					return new Result(-1, "Erro ao tentar excluir contratos da matrícula!");
				}
				if (MatriculaPeriodoLetivoServices.delete(cdMatricula, rs.getInt("cd_periodo_letivo"), con) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(con);
					return new Result(-1, "Erro ao tentar excluir matrícula em módulos/etapas!");
				}
			}
			Result result = new Result(MatriculaDAO.delete(cdMatricula, con));
			if (result.getCode() <= 0){
				if (isConnectionNull)
					Conexao.rollback(con);
			}
			if (isConnectionNull)
				con.commit();
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(con);
			return new Result(-1, "Erro ao tentar excluir matrícula!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(con);
		}
	}
	
	public static Result gerarRelatorioMatriculas(int cdEmpresa, int cdPeriodoLetivo, int cdAluno, int cdCurso, int cdTurma, int tpMatricula){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_empresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
//			criterios.add(new ItemComparator("M.cd_cidade", "" + cdCidade, ItemComparator.EQUAL, Types.INTEGER));
//			criterios.add(new ItemComparator("N.tp_localizacao", "" + tpLocalizacao, ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsm = find(criterios, connection);

			while(rsm.next()){
//				rsm.setValueToField("DS_ENDERECO", rsm.getString("NM_LOGRADOURO") + ", " + (rsm.getString("NR_ENDERECO") != null && !rsm.getString("NR_ENDERECO").trim().equals("") ? rsm.getString("NR_ENDERECO") + ", " : "") + (rsm.getString("NM_BAIRRO") != null && !rsm.getString("NM_BAIRRO").trim().equals("") ? rsm.getString("NM_BAIRRO") : ""));
//				rsm.setValueToField("CL_ST_INSTITUICAO", InstituicaoEducacensoServices.situacaoInstituicao[rsm.getInt("ST_INSTITUICAO_PUBLICA")]);
			}
			
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_INSTITUICAO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	/**
	 * Metodo que faz a conservação do aluno, atualizando a matricula passado de acordo a nova turma e curso
	 * @param cdMatricula
	 * @param cdTurmaDestino
	 * @param cdUsuario
	 * @return
	 */
	public static Result conservarAlunoMatriculaOrigem(int cdMatricula, int cdTurmaDestino, int cdUsuario) {
		return conservarAlunoMatriculaOrigem(cdMatricula, cdTurmaDestino, 0, cdUsuario, null);
	}
	
	public static Result conservarAlunoMatriculaOrigem(int cdMatricula, int cdTurmaDestino, int cdCursoDestino, int cdUsuario) {
		return conservarAlunoMatriculaOrigem(cdMatricula, cdTurmaDestino, cdCursoDestino, cdUsuario, null);
	}

	public static Result conservarAlunoMatriculaOrigem(int cdMatricula, int cdTurmaDestino, int cdCursoDestino, int cdUsuario, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			Matricula matricula = MatriculaDAO.get(cdMatricula, connection);
			InstituicaoPeriodo instituicaoPeriodoMatricula = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connection);
			
			Instituicao instituicao = InstituicaoDAO.get(instituicaoPeriodoMatricula.getCdInstituicao(), connection);
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connection);
				return new Result(ERR_ESCOLA_OFFLINE, "Escolas que estao no modo offline nao podem fazer conservacao de alunos");
			}
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			int cdPeriodoLetivoSecretaria = 0;
			ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdSecretaria, connection);
			if(rsmPeriodoRecente.next()){
				cdPeriodoLetivoSecretaria = rsmPeriodoRecente.getInt("cd_periodo_letivo");
			}
			
			if(matricula.getStMatricula() != ST_ATIVA && matricula.getStMatricula() != ST_PENDENTE && matricula.getStMatricula() != ST_EM_TRANSFERENCIA && matricula.getStMatricula() != ST_FECHADA){
				if(isConnectionNull){
					Conexao.rollback(connection);
				}
				return new Result(-1, "A conservação não pode ser realizada para matrículas que não estejam Ativas, Pendentes, Em Transferência ou Fechadas");
			}
			
			if(!UsuarioServices.isAdmSecretariaEducacao(cdUsuario, connection) && instituicaoPeriodoMatricula.getCdPeriodoLetivoSuperior() != cdPeriodoLetivoSecretaria){
				if(isConnectionNull){
					Conexao.rollback(connection);
				}
				return new Result(-1, "A conservação não pode ser realizada para o periodo de " + instituicaoPeriodoMatricula.getNmPeriodoLetivo());
			}
			
			int cdTurmaAntiga = matricula.getCdTurma();
			
			Turma turmaAntiga = TurmaDAO.get(cdTurmaAntiga, connection);
			Curso cursoAntigo = CursoDAO.get(turmaAntiga.getCdCurso(), connection);
			
			
			ResultSetMap rsmOcorrenciaMatriculaConservacao = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_ocorrencia_matricula A, grl_ocorrencia B "
					+ "																						WHERE A.cd_ocorrencia = B.cd_ocorrencia "
					+ "																						  AND B.cd_tipo_ocorrencia = " + TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CONSERVACAO, connection).getCdTipoOcorrencia()
					+ "																				  		  AND A.cd_matricula_origem = " + cdMatricula).executeQuery());
			
			if(rsmOcorrenciaMatriculaConservacao.next()){
				if(isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Essa matrícula já foi conservada.");
			}
			
			Turma turmaNova = TurmaDAO.get(cdTurmaDestino, connection);
			Curso cursoTurmaNova = CursoDAO.get(turmaNova.getCdCurso(), connection);
			
			int cdTurmaNova = cdTurmaDestino;
			
			if(cdTurmaNova == 0){
				if(isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Não há turma para o aluno ser matriculado");
			}
			matricula.setCdTurma(cdTurmaNova);
			if(cursoTurmaNova.getLgMulti() == 0){
				matricula.setCdCurso(cursoTurmaNova.getCdCurso());
			}
			else if(cdCursoDestino > 0){
				
				
				ResultSetMap rsmCursoCompativel = new ResultSetMap(connection.prepareStatement("SELECT A.cd_curso FROM acd_instituicao_curso A, acd_curso B WHERE A.cd_curso = B.cd_curso AND A.cd_instituicao = " + turmaNova.getCdInstituicao() + " AND A.cd_periodo_letivo = " + turmaNova.getCdPeriodoLetivo()
																							+"	 AND EXISTS (SELECT * FROM acd_curso_multi C_MUL WHERE C_MUL.cd_curso_multi = "+cursoTurmaNova.getCdCurso()+" AND C_MUL.cd_curso = A.cd_curso)"
																							+"	 AND (EXISTS (SELECT * FROM acd_curso_correspondencia C_COR WHERE (C_COR.cd_curso = A.cd_curso AND C_COR.cd_curso_correspondencia = "+cdCursoDestino+") OR (C_COR.cd_curso_correspondencia = A.cd_curso AND C_COR.cd_curso = "+cdCursoDestino+")) OR A.cd_curso = "+cdCursoDestino+")").executeQuery());
				if(rsmCursoCompativel.next()){
					matricula.setCdCurso(rsmCursoCompativel.getInt("cd_curso"));
				}
				else
					matricula.setCdCurso(cdCursoDestino);
			}
			else{
				ResultSetMap rsmCursoAnterior = new ResultSetMap(connection.prepareStatement("SELECT B.cd_curso FROM acd_curso_etapa A, acd_curso_etapa B WHERE A.cd_curso_etapa = B.cd_curso_etapa_posterior AND A.cd_curso = " + matricula.getCdCurso()).executeQuery());
				if(rsmCursoAnterior.next())
					matricula.setCdCurso(rsmCursoAnterior.getInt("cd_curso"));
				else
					matricula.setCdCurso(turmaNova.getCdCurso());
			}
			matricula.setCdMatriz(1/*Matriz padrão*/);
			
			//Caso a matrícula original esteja em transferencia, a matrícula é alterada para o status de pendente
			if(turmaAntiga.getNmTurma().equals("TRANSFERENCIA")){
				
				int cdTipoOcorrencia = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_MATRICULAS, connection).getCdTipoOcorrencia();
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_instituicao", "" + instituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_periodo_letivo", "" + matricula.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + cdTipoOcorrencia, ItemComparator.EQUAL, Types.INTEGER));
				if(OcorrenciaInstituicaoServices.find(criterios, connection).size() == 0){
					matricula.setStMatricula(ST_PENDENTE);
				}
				else
					matricula.setStMatricula(ST_FECHADA);
				
			}
			
			//Se a matrícula for posta em uma turma de transferencia, a matrícula ficará em transferencia
			if(turmaNova.getNmTurma().equals("TRANSFERENCIA")){
				matricula.setStMatricula(ST_EM_TRANSFERENCIA);
			}
			
			Result result = MatriculaServices.save(matricula, 0, cdUsuario, true, connection); 
			if(result.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}
			
			
			//Buscar matrícula anterior para poder coloca-la como reprovada
			ResultSetMap rsmMatriculaAnterior = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_matricula WHERE cd_matricula = " + matricula.getCdMatriculaOrigem()).executeQuery());
			if(rsmMatriculaAnterior.next()){
				Matricula matriculaAnterior = MatriculaDAO.get(rsmMatriculaAnterior.getInt("cd_matricula"), connection);
				if(matriculaAnterior.getCdCurso() != 1159 && matriculaAnterior.getCdCurso() != 1179 && matriculaAnterior.getCdCurso() != 1147 && matriculaAnterior.getCdCurso() != 1181){
					if(matriculaAnterior.getStAlunoCenso() == MatriculaServices.ST_ALUNO_CENSO_APROVADO){
						matriculaAnterior.setStAlunoCenso(ST_ALUNO_CENSO_REPROVADO);
						if(MatriculaDAO.update(matriculaAnterior, connection) < 0){
							if(isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao atualizar matricula de origem");
						}
					}
				}
			}
			
			
			int cdTipoOcorrenciaConservacao = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CONSERVACAO, connection).getCdTipoOcorrencia();
			
			Curso cursoNovo  = CursoDAO.get(matricula.getCdCurso(), connection);
			Curso cursoTurma = CursoDAO.get(turmaNova.getCdCurso(), connection);
			
			OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno conservado da turma "+turmaAntiga.getNmTurma()+" da modalidade " + cursoAntigo.getNmProdutoServico() + " para a turma " + turmaNova.getNmTurma() + "/"+cursoTurma.getNmProdutoServico()+" na modalidade " + cursoNovo.getNmProdutoServico(), Util.getDataAtual(), cdTipoOcorrenciaConservacao, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), cdTurmaAntiga, cdTurmaNova, matricula.getStMatricula(), cdUsuario);
			OcorrenciaMatriculaServices.save(ocorrencia, connection);
			
			
			if(isConnectionNull)
				connection.commit();
			
			return new Result(1, "Aluno conservado com sucesso", "stMatricula", matricula.getStMatricula());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaServices.conservarAluno: " +  e);
			return new Result(-1, "Erro");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Metodo que realiza a finalização das matrículas, inativando todas as matriculas daquele periodo letivo que estiverem pendentes
	 * @param cdPeriodoLetivo
	 * @return
	 */
	public static Result fecharAlunoMatricula(int cdPeriodoLetivo, int cdUsuario) {
		return fecharAlunoMatricula(cdPeriodoLetivo, cdUsuario, null);
	}

	public static Result fecharAlunoMatricula(int cdPeriodoLetivo, int cdUsuario, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			if(cdPeriodoLetivo <= 0){
				if(isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Carregue um periodo letivo para finalizar as matrículas.");
			}
			
			ResultSetMap rsmPeriodos = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE (cd_periodo_letivo = "+cdPeriodoLetivo+" OR cd_periodo_letivo_superior = " + cdPeriodoLetivo+")").executeQuery());
			int x = 1;
			while(rsmPeriodos.next()){
				ResultSetMap rsmMatriculas = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_matricula WHERE cd_periodo_letivo = " + rsmPeriodos.getString("cd_periodo_letivo") + " AND st_matricula = " + ST_PENDENTE).executeQuery());
				while(rsmMatriculas.next()){
					System.out.println("Linha " + (x++));
					Matricula matricula = MatriculaDAO.get(rsmMatriculas.getInt("cd_matricula"), connection);
					Turma turma = TurmaDAO.get(matricula.getCdTurma(), connection);
					if(turma.getStTurma() == TurmaServices.ST_PENDENTE){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "As turmas precisam estar fechadas para se poder finalizar o periodo de matrícula.");
					}
						
					Result result = saveFechada(matricula, cdUsuario, false, connection);
					if(result.getCode() <= 0 && result.getCode() != ERR_ESCOLA_OFFLINE){
						return result;
					}
					
				}
			}
			
			ResultSetMap rsmEscolas = InstituicaoServices.getAllAtivas();
			while(rsmEscolas.next()){
				int cdTipoOcorrencia = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_MATRICULAS, connection).getCdTipoOcorrencia();
				
				Instituicao instituicao = InstituicaoDAO.get(rsmEscolas.getInt("cd_instituicao"), connection);
				
				ResultSetMap rsmPeriodo = InstituicaoPeriodoServices.getPeriodoOfInstituicao(instituicao.getCdInstituicao(), cdPeriodoLetivo, connection);
				if(rsmPeriodo.next()){
					
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_instituicao", "" + instituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_periodo_letivo", "" + rsmPeriodo.getInt("cd_periodo_letivo"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + cdTipoOcorrencia, ItemComparator.EQUAL, Types.INTEGER));
					if(OcorrenciaInstituicaoServices.find(criterios, connection).size() == 0){
						OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, instituicao.getCdInstituicao(), "Fechada matriculas pendentes da " + instituicao.getNmPessoa(), Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicao.getCdInstituicao(), Util.getDataAtual(), cdUsuario, rsmPeriodo.getInt("cd_periodo_letivo"));
						OcorrenciaInstituicaoServices.save(ocorrencia, connection);
					}
				}
			}
			
			if(isConnectionNull)
				connection.commit();
			
			return new Result(1, "Finalização realizada com sucesso");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaServices.inativarAlunoMatricula: " +  e);
			return new Result(-1, "Erro");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	@Deprecated
	public static String getNrMatricula(GregorianCalendar dtMatricula) {
		return getNrMatricula(dtMatricula, null);
	}

	@Deprecated
	public static String getNrMatricula(GregorianCalendar dtMatricula, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT MAX(nr_matricula) AS last " +
					"FROM acd_matricula " +
					"WHERE dt_matricula >= ? " +
					"  AND dt_matricula <= ?");
			dtMatricula = dtMatricula==null ? new GregorianCalendar() : dtMatricula;
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(new GregorianCalendar(dtMatricula.get(Calendar.YEAR), Calendar.JANUARY,
					1)));
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(new GregorianCalendar(dtMatricula.get(Calendar.YEAR), Calendar.DECEMBER, 31)));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int lastMatricula = rsm==null || !rsm.next() || rsm.getString("last")==null || rsm.getString("last").trim().equals("") ? 101 :
				Integer.parseInt(rsm.getString("last").substring(0, 4)) + 1;
			return new DecimalFormat("0000").format(lastMatricula) + Integer.toString(dtMatricula.get(Calendar.YEAR)).substring(2);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaServices.getNrMatricula: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getDadosRequerimentoByInstituicao(int cdInstituicao)	{
		return getDadosRequerimento(0, 0, cdInstituicao, null);
	}
	
	public static ResultSetMap getDadosRequerimentoByInstituicao(int cdInstituicao, boolean lgAnoSeguinte)	{
		return getDadosRequerimento(0, 0, cdInstituicao, lgAnoSeguinte, null);
	}
	
	public static ResultSetMap getDadosRequerimentoByInstituicao(int cdInstituicao, Connection connect)	{
		return getDadosRequerimento(0, 0, cdInstituicao, connect);
	}
	
	public static ResultSetMap getDadosRequerimentoByTurma(int cdTurma)	{
		return getDadosRequerimento(0, cdTurma, 0, null);
	}
	
	public static ResultSetMap getDadosRequerimentoByTurma(int cdTurma, Connection connect)	{
		return getDadosRequerimento(0, cdTurma, 0, connect);
	}
	
	public static ResultSetMap getDadosRequerimentoByMatricula(int cdMatricula)	{
		return getDadosRequerimento(cdMatricula, 0, 0, null);
	}
	
	public static ResultSetMap getDadosRequerimentoByMatricula(int cdMatricula, boolean lgAnoSeguinte)	{
		return getDadosRequerimento(cdMatricula, 0, 0, lgAnoSeguinte, null);
	}
	
	public static ResultSetMap getDadosRequerimento(int cdMatricula, int cdTurma, int cdInstituicao, Connection connect)	{
		return getDadosRequerimento(cdMatricula, cdTurma, cdInstituicao, false, connect);
	}
	
	public static ResultSetMap getDadosRequerimento(int cdMatricula, int cdTurma, int cdInstituicao, boolean lgAnoSeguinte, Connection connect)	{
		boolean isConnectionNull = connect == null;
		try	{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdPeriodoLetivoAtual = 0;
			if(cdInstituicao > 0) {
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT "
					//ALUNO
					+ "A.cd_matricula, B.nm_pessoa AS NM_ALUNO, D.nm_mae, D.nm_pai, BB.nm_responsavel, BB.nm_parentesco, "
					+ "D.nr_rg, D.sg_orgao_rg, E.nr_documento AS NR_REGISTRO_NASCIMENTO, E.folha, E.livro, E.dt_emissao AS DT_EMISSAO_REGISTRO, "
					+ "GG.nm_cidade AS NM_MUNICIPIO_CARTORIO, F.nm_pessoa AS NM_CARTORIO, "
					+ "DD.nm_cidade AS nm_naturalidade, DDD.sg_estado AS SG_UF_NATURALIDADE, "
					+ "C.nm_logradouro AS NM_ENDERECO, C.nr_endereco, C.nm_complemento, C.nm_bairro, C.nr_cep, "
					+ "D.tp_raca, D.tp_sexo,"
					+ "D.nr_cpf, B.nr_telefone1, B.nr_telefone2, B.nr_celular, D.dt_nascimento, D.dt_emissao_rg, "
					+ "L.SG_ESTADO AS SG_UF_RG, O.NM_PESSOA AS NM_CARTORIO, Q.NM_CIDADE AS NM_MUNICIPIO_CARTORIO, R.SG_ESTADO AS SG_UF_CARTORIO, " 
					
					//INSTITUICAO
					+ "J.nm_pessoa as NM_INSTITUICAO, "
					+ "K.nm_logradouro AS NM_ENDERECO_INSTITUICAO, K.nm_bairro AS NM_BAIRRO_INSTITUICAO, K.nr_endereco AS NR_ENDERECO_INSTITUICAO, "
					+ "K.nm_complemento AS NM_COMPLEMENTO_INSTITUICAO, J.nr_telefone1 AS NR_TELEFONE_INSTITUICAO, "
					
					//MATRICULA
					+" A.cd_aluno, A.lg_transporte_publico, BB.lg_mais_educacao, BB.lg_bolsa_familia, A.cd_curso, H.nm_produto_servico AS nm_curso, H.id_produto_servico, "
					+ "I.nm_turma, I.tp_turno, A.nr_matricula, I.cd_instituicao, AA.cd_periodo_letivo, AA.nm_periodo_letivo, A.nm_ultima_escola "+
					
					
					 "FROM acd_matricula A " +
					 "LEFT OUTER JOIN acd_instituicao_periodo AA ON (A.cd_periodo_letivo = AA.cd_periodo_letivo) " +
					 
                     "LEFT OUTER JOIN grl_pessoa B ON (A.cd_aluno = B.cd_pessoa) " +
                     "LEFT OUTER JOIN acd_aluno BB ON (A.cd_aluno = BB.cd_aluno) " +
                     "LEFT OUTER JOIN grl_pessoa_ficha_medica BBB ON (A.cd_aluno = BBB.cd_pessoa) " +
                     "LEFT OUTER JOIN grl_pessoa_endereco C ON (A.cd_aluno = C.cd_pessoa AND C.lg_principal = 1) " +
                     "LEFT OUTER JOIN grl_pessoa_fisica D ON (A.cd_aluno = D.cd_pessoa) " +
                     "LEFT OUTER JOIN grl_cidade DD ON (D.cd_naturalidade = DD.cd_cidade) " +
                     "LEFT OUTER JOIN grl_estado DDD ON (DD.cd_estado = DDD.cd_estado) " +
                     "LEFT OUTER JOIN grl_pessoa_tipo_documentacao E ON (A.cd_aluno = E.cd_pessoa AND (E.cd_tipo_documentacao = "+TipoDocumentacaoDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0), connect).getCdTipoDocumentacao()+" OR E.cd_tipo_documentacao = "+TipoDocumentacaoDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0), connect).getCdTipoDocumentacao()+")) " +
                     "LEFT OUTER JOIN grl_pessoa F ON (E.cd_cartorio = F.cd_pessoa) " +
                     "LEFT OUTER JOIN grl_pessoa_endereco G ON (F.cd_pessoa = G.cd_pessoa AND G.lg_principal = 1) " +
                     "LEFT OUTER JOIN grl_cidade GG ON (G.cd_cidade = GG.cd_cidade) " +
                     "LEFT OUTER JOIN grl_produto_servico H ON (A.cd_curso = H.cd_produto_servico) " +
                     "LEFT OUTER JOIN acd_turma I ON (A.cd_turma = I.cd_turma) " +
                     "LEFT OUTER JOIN grl_pessoa J ON (I.cd_instituicao = J.cd_pessoa) " +
                     "LEFT OUTER JOIN grl_pessoa_endereco K ON (J.cd_pessoa = K.cd_pessoa AND K.lg_principal = 1) " +
                     "LEFT OUTER JOIN grl_estado L ON (D.cd_estado_rg = L.cd_estado)" +
                     "LEFT OUTER JOIN grl_pessoa_tipo_documentacao M ON (A.cd_aluno = M.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_tipo_documentacao N ON (M.cd_tipo_documentacao = N.cd_tipo_documentacao AND N.cd_tipo_documentacao = 4) " +
					 "LEFT OUTER JOIN grl_pessoa O ON (O.cd_pessoa = M.cd_cartorio) " +
					 "LEFT OUTER JOIN grl_pessoa_endereco P ON (P.cd_pessoa = O.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_cidade Q ON (Q.cd_cidade = P.cd_cidade) " +
					 "LEFT OUTER JOIN grl_estado R ON (R.cd_estado = Q.cd_estado) " +

					 //para resolver a progressao de matriculas não ativas
//					 "INNER JOIN acd_matricula AAA ON (AAA.cd_matricula = A.cd_matricula_origem AND AAA.st_matricula = "+MatriculaServices.ST_ATIVA+") " +
					 
                     " WHERE A.st_matricula IN (" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_CONCLUIDA + (lgAnoSeguinte ? "" : ", " + MatriculaServices.ST_PENDENTE) + ") " + 
                     
                     //by matricula
                     (cdMatricula > 0 ? " AND A.cd_matricula = "+cdMatricula : "") +
                     //by turma
                     (cdTurma > 0 ? " AND A.cd_turma = "+cdTurma : "") +
                     //by Instituição
                     (cdInstituicao > 0 ? " AND I.cd_instituicao = "+cdInstituicao+ " AND A.cd_periodo_letivo = " + cdPeriodoLetivoAtual : "") +
                     
                     "AND A.cd_curso NOT IN ("+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)+")"+
                     
                     " ORDER BY H.id_produto_servico, I.nm_turma, B.nm_pessoa").executeQuery());
			
			ArrayList<Integer> codigosMatricula = new ArrayList<Integer>();
			int x = 0;
			while(rsm.next()){
				
				rsm.setValueToField("NM_TP_TURNO", TurmaServices.tiposTurno[(rsm.getInt("tp_turno"))]);
				rsm.setValueToField("NM_TP_RACA", PessoaFisicaServices.tipoRaca[(rsm.getInt("tp_raca"))]);
				rsm.setValueToField("NM_TP_SEXO", PessoaFisicaServices.tipoSexo[rsm.getInt("tp_sexo")]);
				
				//tratamento de mascaras
				rsm.setValueToField("NR_TELEFONE1", Util.format((rsm.getString("nr_telefone1")!=null && !rsm.getString("nr_telefone1").equals("") ? 
						rsm.getString("nr_telefone1").replaceAll("[\\(,\\),\\.\\-]", "") : ""), "## ####-####", false));
				
				String nrCelular = rsm.getString("nr_celular")!=null && !rsm.getString("nr_celular").equals("") ? 
						rsm.getString("nr_celular").replaceAll("[\\(,\\),\\.\\-]", "") : "";
				nrCelular = nrCelular.length()==10 ? nrCelular.substring(0, 2)+"9"+nrCelular.substring(2):nrCelular;
				rsm.setValueToField("NR_CELULAR", Util.format(nrCelular, "## #####-####", false));
				
				rsm.setValueToField("NR_CEP", Util.format((rsm.getString("nr_cep")!=null && !rsm.getString("nr_cep").equals("") ? 
						rsm.getString("nr_cep").replaceAll("[\\.\\-]", "") : ""), "##.###-###", false));
				
				rsm.setValueToField("NR_TELEFONE_INSTITUICAO", Util.format((rsm.getString("NR_TELEFONE_INSTITUICAO")!=null && !rsm.getString("NR_TELEFONE_INSTITUICAO").equals("") ? 
						rsm.getString("NR_TELEFONE_INSTITUICAO").replaceAll("[\\(,\\),\\.\\-]", "") : ""), "## ####-####", false));
				
				if(rsm.getString("NR_REGISTRO_NASCIMENTO")!=null && rsm.getString("NR_REGISTRO_NASCIMENTO").length()==32) {
					String nrRegistroNovo = Util.format(rsm.getString("NR_REGISTRO_NASCIMENTO"), "######-##-##-####-#-#####-###-#######-##", false);
					rsm.setValueToField("NR_REGISTRO_NOVO", nrRegistroNovo);
					rsm.setValueToField("NR_REGISTRO_NASCIMENTO", "");
				}
				
				//doencas
				rsm.setValueToField("LG_DIABETICO", PessoaDoencaServices.isDiabetico(rsm.getInt("CD_ALUNO"), connect) ? 1 : 0);
				
				ResultSetMap rsmDoencas = PessoaDoencaServices.getDoencaByPessoa(rsm.getInt("CD_ALUNO"), false, connect);
				String dsDoencas = "";
				while(rsmDoencas.next())
					dsDoencas += (rsmDoencas.getPointer()>0 ? ", " : "")+rsmDoencas.getString("NM_DOENCA");
				rsm.setValueToField("DS_DOENCAS", dsDoencas);
				
				//alergias
				ResultSetMap rsmAlergias = PessoaAlergiaServices.getAlergiaByPessoa(rsm.getInt("CD_ALUNO"), connect);
				String dsAlergias = "";
				while(rsmAlergias.next())
					dsAlergias += (rsmAlergias.getPointer()>0 ? ", " : "")+rsmAlergias.getString("NM_ALERGIA");
				rsm.setValueToField("DS_ALERGIAS", dsAlergias);
				
				//necessidades especiais
				ResultSetMap rsmNecessidadesEspeciais = PessoaNecessidadeEspecialServices.getNecessidadeEspecialByPessoa(rsm.getInt("CD_ALUNO"), connect);
				String dsNecessidadesEspeciais = "";
				while(rsmNecessidadesEspeciais.next())
					dsNecessidadesEspeciais += (rsmNecessidadesEspeciais.getPointer()>0 ? ", " : "")+rsmNecessidadesEspeciais.getString("NM_TIPO_NECESSIDADE_ESPECIAL");
				rsm.setValueToField("DS_NECESSIDADES_ESPECIAIS", dsNecessidadesEspeciais);
				
				
				//transporte publico
				ResultSetMap rsmTransportePublico = MatriculaTipoTransporteServices.getTipoTransporteByMatricula(rsm.getInt("CD_MATRICULA"), connect);
				String dsTransportesPublicos = "";
				while(rsmTransportePublico.next())
					dsTransportesPublicos += (rsmTransportePublico.getPointer()>0 ? ", " : "")+rsmTransportePublico.getString("NM_TIPO_TRANSPORTE");
				rsm.setValueToField("DS_TRANSPORTE_PUBLICO", dsTransportesPublicos);
				
				
				Curso cursoAtual = CursoDAO.get(rsm.getInt("cd_curso"), connect);
				if(lgAnoSeguinte){
					rsm.setValueToField("NM_PERIODO_LETIVO", String.valueOf(Integer.parseInt(rsm.getString("nm_periodo_letivo"))+1));
					
					Curso cursoMatricula = CursoEtapaServices.getProximoCurso(cursoAtual.getCdCurso(), cdInstituicao, connect);
					if(cursoMatricula != null){
						
						InstituicaoCurso instituicaoCurso = InstituicaoCursoDAO.get(rsm.getInt("cd_instituicao"), cursoMatricula.getCdCurso(), rsm.getInt("cd_periodo_letivo"), connect);
						if(instituicaoCurso != null){
							rsm.setValueToField("CD_CURSO", cursoMatricula.getCdCurso());
							rsm.setValueToField("NM_CURSO", cursoMatricula.getNmProdutoServico());
						}
						else{
							rsm.deleteRow();
							if(x == 0)
								rsm.beforeFirst();
							else
								rsm.previous();
							continue;
						}
					}
					else{
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
					
					
				}
				if(!codigosMatricula.contains(rsm.getInt("cd_matricula")))
					codigosMatricula.add(rsm.getInt("cd_matricula"));
				else{
					rsm.deleteRow();
					if(x == 0)
						rsm.beforeFirst();
					else
						rsm.previous();
					continue;
				}
				x++;	
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getDadosFichaCensoByMatricula(int cdMatricula)	{
		return getDadosFichaCenso(cdMatricula, 0, null);
	}
	
	/**
	 * Monta os dados de ficha do censo, para ser impresso e passado a escolas sem acesso ao sistema online
	 * @param cdMatricula
	 * @param cdTurma
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getDadosFichaCenso(int cdMatricula, int cdTurma, Connection connect)	{
		boolean isConnectionNull = connect == null;
		try	{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT "
					//ALUNO
					+ "A.cd_matricula, A.tp_escolarizacao_outro_espaco, A.cd_curso, B.nm_pessoa AS NM_ALUNO, D.nm_mae, D.nm_pai, "
					+ "BB.nr_cpf, D.nr_rg, D.sg_orgao_rg, D.sg_orgao_rg, D.tp_nacionalidade, E.cd_tipo_documentacao, E.nr_documento AS NR_REGISTRO_NASCIMENTO, E.folha, E.livro, E.dt_emissao AS DT_EMISSAO_REGISTRO, "
					+ "GG.nm_cidade AS NM_MUNICIPIO_CARTORIO, F.nm_pessoa AS NM_CARTORIO, GGG.SG_ESTADO AS SG_UF_CARTORIO,"
					+ "DD.nm_cidade AS nm_naturalidade, DDD.sg_estado AS SG_UF_NATURALIDADE, "
					+ "C.nm_logradouro AS NM_ENDERECO, C.nr_endereco, C.nm_complemento, C.nm_bairro, C.nr_cep, C.tp_zona,"
					+ "D.tp_raca, D.tp_sexo,"
					+ "D.dt_nascimento, D.dt_emissao_rg, "
					+ "L.SG_ESTADO AS SG_UF_RG, " 
					+ "CC.nm_cidade, CCC.sg_estado AS sg_uf,"
					//INSTITUICAO
					+ "J.nm_pessoa as NM_INSTITUICAO, "
					
					//MATRICULA
					+" A.cd_aluno, A.lg_transporte_publico, H.nm_produto_servico AS nm_curso, "
					+ "I.nm_turma, I.cd_curso AS cd_curso_turma, I.tp_turno, A.nr_matricula, AA.nm_periodo_letivo, N.nm_etapa, "+
					"  W.lg_pai_nao_declarado, INS.nr_inep "+
					
					
					 "FROM acd_matricula A " +
					 "LEFT OUTER JOIN acd_instituicao_periodo AA ON (A.cd_periodo_letivo = AA.cd_periodo_letivo) " +
					 "LEFT OUTER JOIN acd_instituicao INS ON (AA.cd_instituicao = INS.cd_instituicao) " +
					 "LEFT OUTER JOIN acd_aluno W ON (A.cd_aluno = W.cd_aluno) " +
                     "LEFT OUTER JOIN grl_pessoa B ON (A.cd_aluno = B.cd_pessoa) " +
                     "LEFT OUTER JOIN grl_pessoa_fisica BB ON (A.cd_aluno = BB.cd_pessoa) " +
                     "LEFT OUTER JOIN grl_pessoa_endereco C ON (A.cd_aluno = C.cd_pessoa AND C.lg_principal = 1) " +
                     "LEFT OUTER JOIN grl_cidade CC ON (C.cd_cidade = CC.cd_cidade) " +
                     "LEFT OUTER JOIN grl_estado CCC ON (CC.cd_estado = CCC.cd_estado) " +
                     "LEFT OUTER JOIN grl_pessoa_fisica D ON (A.cd_aluno = D.cd_pessoa) " +
                     "LEFT OUTER JOIN grl_cidade DD ON (D.cd_naturalidade = DD.cd_cidade) " +
                     "LEFT OUTER JOIN grl_estado DDD ON (DD.cd_estado = DDD.cd_estado) " +
                     "LEFT OUTER JOIN grl_pessoa_tipo_documentacao E ON (A.cd_aluno = E.cd_pessoa AND (E.cd_tipo_documentacao = "+TipoDocumentacaoDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0), connect).getCdTipoDocumentacao()+" OR E.cd_tipo_documentacao = "+TipoDocumentacaoDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0), connect).getCdTipoDocumentacao()+")) " +
                     "LEFT OUTER JOIN grl_pessoa F ON (E.cd_cartorio = F.cd_pessoa) " +
                     "LEFT OUTER JOIN grl_pessoa_endereco G ON (F.cd_pessoa = G.cd_pessoa AND G.lg_principal = 1) " +
                     "LEFT OUTER JOIN grl_cidade GG ON (G.cd_cidade = GG.cd_cidade) " +
                     "LEFT OUTER JOIN grl_estado GGG ON (GG.cd_estado = GGG.cd_estado) " +
                     "LEFT OUTER JOIN grl_produto_servico H ON (A.cd_curso = H.cd_produto_servico) " +
                     "LEFT OUTER JOIN acd_turma I ON (A.cd_turma = I.cd_turma) " +
                     "LEFT OUTER JOIN grl_pessoa J ON (I.cd_instituicao = J.cd_pessoa) " +
                     "LEFT OUTER JOIN grl_pessoa_endereco K ON (J.cd_pessoa = K.cd_pessoa AND K.lg_principal = 1) " +
                     "LEFT OUTER JOIN grl_estado L ON (D.cd_estado_rg = L.cd_estado)" +
                     
                     "LEFT OUTER JOIN acd_curso_etapa M ON (A.cd_curso = M.cd_curso)" +
                     "LEFT OUTER JOIN acd_tipo_etapa N ON (M.cd_etapa = N.cd_etapa)" +
//                     "LEFT OUTER JOIN grl_pessoa_tipo_documentacao M ON (A.cd_aluno = M.cd_pessoa) " +
//					 "LEFT OUTER JOIN grl_tipo_documentacao N ON (M.cd_tipo_documentacao = N.cd_tipo_documentacao AND N.cd_tipo_documentacao = 4) " +
//					 "LEFT OUTER JOIN grl_pessoa O ON (O.cd_pessoa = M.cd_cartorio) " +
//					 "LEFT OUTER JOIN grl_pessoa_endereco P ON (P.cd_pessoa = O.cd_pessoa) " +
//					 "LEFT OUTER JOIN grl_cidade Q ON (Q.cd_cidade = P.cd_cidade) " +
//					 "LEFT OUTER JOIN grl_estado R ON (R.cd_estado = Q.cd_estado) " +

					 //para resolver a progressao de matriculas não ativas
//					 "INNER JOIN acd_matricula AAA ON (AAA.cd_matricula = A.cd_matricula_origem AND AAA.st_matricula = "+MatriculaServices.ST_ATIVA+") " +
					 
 					 " WHERE A.st_matricula IN (" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_CONCLUIDA + ")"+
                     "   AND I.nm_turma NOT LIKE 'TRANS%' "+ 
                     "   AND I.tp_atendimento NOT IN ("+TurmaServices.TP_ATENDIMENTO_AEE+") " + 
 					 
                     //by matricula
                     (cdMatricula > 0 ? " AND A.cd_matricula = "+cdMatricula : "") +
                     //by turma
                     (cdTurma > 0 ? " AND A.cd_turma = "+cdTurma+" AND I.st_turma <> " + TurmaServices.ST_INATIVO : "") +
                                          
                     " ORDER BY B.nm_pessoa").executeQuery());
			
			ArrayList<Integer> codigosMatricula = new ArrayList<Integer>();
			int x = 0;
			while(rsm.next()){
				
				rsm.setValueToField("NM_TURMA", rsm.getString("NM_ETAPA") + " - " + rsm.getString("NM_CURSO") + " - " + rsm.getString("NM_TURMA"));
				
				rsm.setValueToField("NM_TP_TURNO", TurmaServices.tiposTurno[(rsm.getInt("tp_turno"))]);
				rsm.setValueToField("NM_TP_RACA", PessoaFisicaServices.tipoRaca[(rsm.getInt("tp_raca"))]);
				rsm.setValueToField("NM_TP_SEXO", PessoaFisicaServices.tipoSexo[rsm.getInt("tp_sexo")]);
				rsm.setValueToField("NM_TP_CERTIDAO", "");
				rsm.setValueToField("NM_TP_CERTIDAO", (rsm.getInt("cd_tipo_documentacao") == PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_NASCIMENTO ? "Certidão de Nascimento" : (rsm.getInt("cd_tipo_documentacao") == PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_CASAMENTO ? "Certidão de Casamento" : "")));
				//ACRESCENTAR BUSCA
				rsm.setValueToField("LG_JUSTIFICATIVA_FALTA_DOCUMENTO", 0);
				
				rsm.setValueToField("NR_CEP", Util.format((rsm.getString("nr_cep")!=null && !rsm.getString("nr_cep").equals("") ? 
						rsm.getString("nr_cep").replaceAll("[\\.\\-]", "") : ""), "##.###-###", false));
				
				if(rsm.getString("NR_REGISTRO_NASCIMENTO")!=null && rsm.getString("NR_REGISTRO_NASCIMENTO").length()==32) {
					String nrRegistroNovo = Util.format(rsm.getString("NR_REGISTRO_NASCIMENTO"), "######-##-##-####-#-#####-###-#######-##", false);
					rsm.setValueToField("NR_REGISTRO_NOVO", nrRegistroNovo);
					rsm.setValueToField("NR_REGISTRO_NASCIMENTO", "");
				}
				
				rsm.setValueToField("LG_CEGUEIRA", 0);
				rsm.setValueToField("LG_DEFICIENCIA_AUDITIVA", 0);
				rsm.setValueToField("LG_DEFICIENCIA_INTELECTUAL", 0);
				rsm.setValueToField("LG_BAIXA_VISAO", 0);
				rsm.setValueToField("LG_SURDOCEGUEIRA", 0);
				rsm.setValueToField("LG_DEFICIENCIA_MULTIPLA", 0);
				rsm.setValueToField("LG_SURDEZ", 0);
				rsm.setValueToField("LG_DEFICIENCIA_FISICA", 0);
				rsm.setValueToField("LG_AUTISMO_INFANTIL", 0);
				rsm.setValueToField("LG_SINDROME_RELT", 0);
				rsm.setValueToField("LG_SINDROME_ASPERGER", 0);
				rsm.setValueToField("LG_TDI", 0);
				rsm.setValueToField("LG_SUPERDOTACAO", 0);
				
				ResultSetMap rsmPessoaNecessidade = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_pessoa_necessidade_especial A, grl_tipo_necessidade_especial B WHERE A.cd_tipo_necessidade_especial = B.cd_tipo_necessidade_especial AND A.cd_pessoa = " + rsm.getInt("cd_aluno")).executeQuery());
				if(rsmPessoaNecessidade.size() == 0){
					rsm.setValueToField("LG_DEFICIENCIA", 0);
				}
				else{
					rsm.setValueToField("LG_DEFICIENCIA", 1);
					while(rsmPessoaNecessidade.next()){
						if(rsmPessoaNecessidade.getString("id_tipo_necessidade_especial").equals(InstituicaoEducacensoServices.TP_CEGUEIRA)){
							rsm.setValueToField("LG_CEGUEIRA", 1);
						}
						else if(rsmPessoaNecessidade.getString("id_tipo_necessidade_especial").equals(InstituicaoEducacensoServices.TP_DEFICIENCIA_AUDITIVA)){
							rsm.setValueToField("LG_DEFICIENCIA_AUDITIVA", 1);
						}
						else if(rsmPessoaNecessidade.getString("id_tipo_necessidade_especial").equals(InstituicaoEducacensoServices.TP_DEFICIENCIA_INTELECTUAL)){
							rsm.setValueToField("LG_DEFICIENCIA_INTELECTUAL", 1);
						}
						else if(rsmPessoaNecessidade.getString("id_tipo_necessidade_especial").equals(InstituicaoEducacensoServices.TP_BAIXA_VISAO)){
							rsm.setValueToField("LG_BAIXA_VISAO", 1);
						}
						else if(rsmPessoaNecessidade.getString("id_tipo_necessidade_especial").equals(InstituicaoEducacensoServices.TP_SURDOCEGUEIRA)){
							rsm.setValueToField("LG_SURDOCEGUEIRA", 1);
						}
						else if(rsmPessoaNecessidade.getString("id_tipo_necessidade_especial").equals(InstituicaoEducacensoServices.TP_DEFICIENCIA_MULTIPLA)){
							rsm.setValueToField("LG_DEFICIENCIA_MULTIPLA", 1);
						}
						else if(rsmPessoaNecessidade.getString("id_tipo_necessidade_especial").equals(InstituicaoEducacensoServices.TP_SURDEZ)){
							rsm.setValueToField("LG_SURDEZ", 1);
						}
						else if(rsmPessoaNecessidade.getString("id_tipo_necessidade_especial").equals(InstituicaoEducacensoServices.TP_DEFICIENCIA_FISICA)){
							rsm.setValueToField("LG_DEFICIENCIA_FISICA", 1);
						}
						else if(rsmPessoaNecessidade.getString("id_tipo_necessidade_especial").equals(InstituicaoEducacensoServices.TP_DEFICIENCIA_AUTISMO_INFANTIL)){
							rsm.setValueToField("LG_AUTISMO_INFANTIL", 1);
						}
						else if(rsmPessoaNecessidade.getString("id_tipo_necessidade_especial").equals(InstituicaoEducacensoServices.TP_DEFICIENCIA_SINDROME_RETT)){
							rsm.setValueToField("LG_SINDROME_RELT", 1);
						}
						else if(rsmPessoaNecessidade.getString("id_tipo_necessidade_especial").equals(InstituicaoEducacensoServices.TP_DEFICIENCIA_SINDROME_ASPERGER)){
							rsm.setValueToField("LG_SINDROME_ASPERGER", 1);
						}
						else if(rsmPessoaNecessidade.getString("id_tipo_necessidade_especial").equals(InstituicaoEducacensoServices.TP_DEFICIENCIA_TRANSTORNO_DESINTEGRATIVO)){
							rsm.setValueToField("LG_TDI", 1);
						}
						else if(rsmPessoaNecessidade.getString("id_tipo_necessidade_especial").equals(InstituicaoEducacensoServices.TP_DEFICIENCIA_SUPERDOTACAO)){
							rsm.setValueToField("LG_SUPERDOTACAO", 1);
						}
						
					}
				}
				
				
				
				rsm.setValueToField("LG_AUXILIO_LEDOR", 0);
				rsm.setValueToField("LG_LEITURA_LABIAL", 0);
				rsm.setValueToField("LG_PROVA_24", 0);
				rsm.setValueToField("LG_AUXILIO_TRANSCRICAO", 0);
				rsm.setValueToField("LG_PROVA_BRAILLE", 0);
				rsm.setValueToField("LG_RECURSO_NENHUM", 1);
				rsm.setValueToField("LG_GUIA_INTERPRETE", 0);
				rsm.setValueToField("LG_PROVA_16", 0);
				rsm.setValueToField("LG_INTERPRETE_LIBRAS", 0);
				rsm.setValueToField("LG_PROVA_20", 0);
				
				ResultSetMap rsmAlunoRecursoProva = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_aluno_recurso_prova A, acd_tipo_recurso_prova B WHERE A.cd_tipo_recurso_prova = B.cd_tipo_recurso_prova AND A.cd_aluno = " + rsm.getInt("cd_aluno")).executeQuery());
				if(rsmPessoaNecessidade.size() == 0){
					rsm.setValueToField("LG_RECURSO_NENHUM", 1);
				}
				else{
					rsm.setValueToField("LG_RECURSO_NENHUM", 0);
					while(rsmAlunoRecursoProva.next()){
						if(rsmAlunoRecursoProva.getString("id_tipo_recurso").equals(InstituicaoEducacensoServices.TP_RECURSO_AUXILIO_LEDOR)){
							rsm.setValueToField("LG_AUXILIO_LEDOR", 1);
						}
						else if(rsmAlunoRecursoProva.getString("id_tipo_recurso").equals(InstituicaoEducacensoServices.TP_RECURSO_LEITURA_LABIAL)){
							rsm.setValueToField("LG_LEITURA_LABIAL", 1);
						}
						else if(rsmAlunoRecursoProva.getString("id_tipo_recurso").equals(InstituicaoEducacensoServices.TP_RECURSO_PROVA_AMPLIADA_24)){
							rsm.setValueToField("LG_PROVA_24", 1);
						}
						else if(rsmAlunoRecursoProva.getString("id_tipo_recurso").equals(InstituicaoEducacensoServices.TP_RECURSO_AUXILIO_TRANSCRICAO)){
							rsm.setValueToField("LG_AUXILIO_TRANSCRICAO", 1);
						}
						else if(rsmAlunoRecursoProva.getString("id_tipo_recurso").equals(InstituicaoEducacensoServices.TP_RECURSO_PROVA_BRAILLE)){
							rsm.setValueToField("LG_PROVA_BRAILLE", 1);
						}
						else if(rsmAlunoRecursoProva.getString("id_tipo_recurso").equals(InstituicaoEducacensoServices.TP_RECURSO_GUIA_INTERPRETE)){
							rsm.setValueToField("LG_GUIA_INTERPRETE", 1);
						}
						else if(rsmAlunoRecursoProva.getString("id_tipo_recurso").equals(InstituicaoEducacensoServices.TP_RECURSO_PROVA_AMPLIADA_16)){
							rsm.setValueToField("LG_PROVA_16", 1);
						}
						else if(rsmAlunoRecursoProva.getString("id_tipo_recurso").equals(InstituicaoEducacensoServices.TP_RECURSO_INTERPRETE_LIBRAS)){
							rsm.setValueToField("LG_INTERPRETE_LIBRAS", 1);
						}
						else if(rsmAlunoRecursoProva.getString("id_tipo_recurso").equals(InstituicaoEducacensoServices.TP_RECURSO_PROVA_AMPLIADA_20)){
							rsm.setValueToField("LG_PROVA_20", 1);
						}
						
					}
				}
				
				rsm.setValueToField("LG_MULTI_CRECHE", 0);
				rsm.setValueToField("LG_MULTI_PRE_ESCOLA", 0);
				rsm.setValueToField("LG_MULTI_ANO_REGULAR", 0);
				rsm.setValueToField("LG_MULTI_ANOS_INICIAIS", 0);
				rsm.setValueToField("LG_MULTI_ANOS_FINAIS", 0);
				
				Curso cursoTurma = CursoDAO.get(rsm.getInt("cd_curso_turma"), connect);
				Curso cursoMatricula = CursoDAO.get(rsm.getInt("cd_curso"), connect);
				if(cursoTurma != null){
					PreparedStatement pstmt =connect.prepareStatement("SELECT * FROM acd_curso_multi WHERE cd_curso_multi = ? AND cd_curso = " + cursoMatricula.getCdCurso());
					
					boolean entrou = false;
					pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_CRECHE", 0));
					ResultSetMap rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
					if(rsmCursoMatricula.next()){
						rsm.setValueToField("LG_MULTI_CRECHE", 1);
						entrou = true;
					}
					pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_PRE_ESCOLA", 0));
					rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
					if(rsmCursoMatricula.next()){
						rsm.setValueToField("LG_MULTI_PRE_ESCOLA", 1);
						entrou = true;
					}
					
					
					PreparedStatement pstmt2 =connect.prepareStatement("SELECT * FROM acd_curso_multi WHERE cd_curso_multi = ? AND cd_curso_multi = ? AND cd_curso = " + cursoMatricula.getCdCurso());
					pstmt2.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_ANOS_INICIAIS", 0));
					pstmt2.setInt(2, cursoTurma.getCdCurso());
					rsmCursoMatricula = new ResultSetMap(pstmt2.executeQuery());
					if(rsmCursoMatricula.next()){
						rsm.setValueToField("LG_MULTI_ANOS_INICIAIS", 1);
						entrou = true;
					}
					pstmt2.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_ANOS_FINAIS", 0));
					rsmCursoMatricula = new ResultSetMap(pstmt2.executeQuery());
					if(rsmCursoMatricula.next()){
						rsm.setValueToField("LG_MULTI_ANOS_FINAIS", 1);
						entrou = true;
					}
					
					pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_REGULAR", 0));
					rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
					if(rsmCursoMatricula.next() && !entrou){
						rsm.setValueToField("LG_MULTI_ANO_REGULAR", 1);
						rsm.setValueToField("NR_MULTI_ANO_REGULAR", String.valueOf((cursoMatricula.getNrOrdem()+1)));
					}
					
					
				}
						
				
				rsm.setValueToField("LG_VAN", 0);
				rsm.setValueToField("LG_ONIBUS", 0);
				rsm.setValueToField("LG_MICROONIBUS", 0);
				rsm.setValueToField("LG_BICICLETA", 0);
				rsm.setValueToField("LG_TRACAO_ANIMAL", 0);
				rsm.setValueToField("LG_OUTROS_VEICULOS", 0);
				
				ResultSetMap rsmPessoaTransporte = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_aluno_tipo_transporte A, fta_tipo_transporte B WHERE A.cd_tipo_transporte = B.cd_tipo_transporte AND A.cd_aluno = " + rsm.getInt("cd_aluno")).executeQuery());
				while(rsmPessoaTransporte.next()){
					if(rsmPessoaTransporte.getString("id_tipo_transporte").equals(InstituicaoEducacensoServices.TP_TRANSPORTE_VAN)){
						rsm.setValueToField("LG_VAN", 1);
					}
					else if(rsmPessoaTransporte.getString("id_tipo_transporte").equals(InstituicaoEducacensoServices.TP_TRANSPORTE_ONIBUS)){
						rsm.setValueToField("LG_ONIBUS", 1);
					}
					else if(rsmPessoaTransporte.getString("id_tipo_transporte").equals(InstituicaoEducacensoServices.TP_TRANSPORTE_MICRO)){
						rsm.setValueToField("LG_MICROONIBUS", 1);
					}
					else if(rsmPessoaTransporte.getString("id_tipo_transporte").equals(InstituicaoEducacensoServices.TP_TRANSPORTE_BICICLETA)){
						rsm.setValueToField("LG_BICICLETA", 1);
					}
					else if(rsmPessoaTransporte.getString("id_tipo_transporte").equals(InstituicaoEducacensoServices.TP_TRANSPORTE_TRACAO_ANIMAL)){
						rsm.setValueToField("LG_TRACAO_ANIMAL", 1);
					}
					else if(rsmPessoaTransporte.getString("id_tipo_transporte").equals(InstituicaoEducacensoServices.TP_TRANSPORTE_OUTRO)){
						rsm.setValueToField("LG_OUTROS_VEICULOS", 1);
					}
										
				}
				
				
				if(!codigosMatricula.contains(rsm.getInt("cd_matricula")))
					codigosMatricula.add(rsm.getInt("cd_matricula"));
				else{
					rsm.deleteRow();
					if(x == 0)
						rsm.beforeFirst();
					else
						rsm.previous();
				}
				x++;	
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Faz a busca de um cartório a partir do número da serventia
	 * @param nrDocumento
	 * @return
	 */
	public static ResultSetMap getCartorio(String nrDocumento) {
		String idServentia = (nrDocumento.length() > 6 ? nrDocumento.substring(0, 6) : (nrDocumento.length() == 6 ? nrDocumento : ""));
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("D.id_serventia", idServentia, ItemComparator.EQUAL, Types.VARCHAR));
		ResultSetMap rsm = PessoaServices.findAllCartorios(criterios);
		return rsm;
	}
	
	@Deprecated
	public static Result saveTransportePublico(int cdMatricula, int lgTransportePublico){
		return saveTransportePublico(cdMatricula, lgTransportePublico, null);
	}
	
	@Deprecated
	public static Result saveTransportePublico(int cdMatricula, int lgTransportePublico, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Matricula matricula = MatriculaDAO.get(cdMatricula, connect);
			
			matricula.setLgTransportePublico(lgTransportePublico);
			if(MatriculaDAO.update(matricula, connect) <= 0){
				return new Result(-1, "Erro ao atualizar transporte publico de matricula");
			}
			
			if(lgTransportePublico == 0){
				if(connect.prepareStatement("DELETE FROM acd_aluno_tipo_transporte WHERE cd_aluno = " + matricula.getCdAluno()).executeUpdate() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar transportes");
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Alteração realizada com sucesso");
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na atualização de transporte");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveSituacaoAlunoCenso(ResultSetMap rsmAlunos, int cdUsuario){
		return saveSituacaoAlunoCenso(rsmAlunos, cdUsuario, null);
	}
	
	public static Result saveSituacaoAlunoCenso(ResultSetMap rsmAlunos, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			while(rsmAlunos.next()){
				Result result = saveSituacaoAlunoCenso(rsmAlunos.getInt("cd_matricula"), Integer.parseInt(rsmAlunos.getString("st_aluno_censo")), cdUsuario, connect);
				if(result.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
			}
			rsmAlunos.beforeFirst();
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Alterações realizadas com sucesso");
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na atualização de transporte");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveSituacaoAlunoCenso(int cdMatricula, int stAlunoCenso, int cdUsuario){
		return saveSituacaoAlunoCenso(cdMatricula, stAlunoCenso, cdUsuario, null);
	}
	
	public static Result saveSituacaoAlunoCenso(int cdMatricula, int stAlunoCenso, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Matricula matricula = MatriculaDAO.get(cdMatricula, connect);
			int stAlunoCensoAntigo = matricula.getStAlunoCenso();
			
			if((matricula.getStMatricula() == ST_DESISTENTE 
					&& stAlunoCenso != ST_ALUNO_CENSO_DESISTENTE) ||
				(matricula.getStMatricula() == ST_EVADIDO 
					&& stAlunoCenso != ST_ALUNO_CENSO_EVADIDO)) {
				
				Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar situacao do aluno: Aluno(a) " + aluno.getNmPessoa() + " apresenta situação de aluno divergente com sua situação de matrícula");
			}
			
			if(stAlunoCensoAntigo != stAlunoCenso){
				matricula.setStAlunoCenso(stAlunoCenso);
				if(MatriculaDAO.update(matricula, connect) <= 0){
					return new Result(-1, "Erro ao atualizar situacao do aluno - censo");
				}
				
				int cdTipoOcorrenciaAlteracaoSituacaoCenso = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_SITUACAO_CENSO, connect).getCdTipoOcorrencia();
				if(cdTipoOcorrenciaAlteracaoSituacaoCenso > 0){
					Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
					InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect);
					OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " alterado situação do Censo de " + situacaoAlunoCenso[stAlunoCensoAntigo] + " para " + situacaoAlunoCenso[matricula.getStAlunoCenso()] + " para o período letivo de " + instituicaoPeriodo.getNmPeriodoLetivo(), Util.getDataAtual(), cdTipoOcorrenciaAlteracaoSituacaoCenso, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), matricula.getCdTurma(), matricula.getCdTurma(), matricula.getStMatricula(), cdUsuario);
					if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao registrar ocorrencia de situacao do censo");
					}
				}
				
				
				if(stAlunoCenso == ST_ALUNO_CENSO_TRANSFERIDO){
					int stMatriculaOrigem = matricula.getStMatricula();
					matricula.setStMatricula(ST_EM_TRANSFERENCIA);
					if(MatriculaDAO.update(matricula, connect) <= 0){
						return new Result(-1, "Erro ao atualizar situacao do aluno - censo");
					}
					
					Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
					Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);
					InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(turma.getCdPeriodoLetivo(), connect);
					Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
					
					int cdTipoOcorrenciaTransferencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_SOLICITACAO_TRANSFERENCIA, connect).getCdTipoOcorrencia();
					
					OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Solicitação de transferência do aluno " + aluno.getNmPessoa() + ", da instituição " + instituicao.getNmPessoa() + " no período letivo de " + instituicaoPeriodo.getNmPeriodoLetivo(), Util.getDataAtual(), cdTipoOcorrenciaTransferencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), 0, turma.getCdTurma(), 0, stMatriculaOrigem, cdUsuario);
					
					Result ret = OcorrenciaMatriculaServices.save(ocorrencia, connect);
					if(ret.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return ret;
					}
					
				}
				
				
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Alteração realizada com sucesso");
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na atualização de transporte");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result saveSituacaoCensoFinal(ResultSetMap rsmAlunos, int cdUsuario){
		return saveSituacaoCensoFinal(rsmAlunos, cdUsuario, null);
	}
	
	public static Result saveSituacaoCensoFinal(ResultSetMap rsmAlunos, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			while(rsmAlunos.next()){
				
				Aluno aluno = AlunoDAO.get(rsmAlunos.getInt("CD_ALUNO"), connect);
				Matricula matricula = MatriculaDAO.get(rsmAlunos.getInt("CD_MATRICULA"), connect);
				
				aluno.setNmAlunoCenso(rsmAlunos.getString("NM_ALUNO_CENSO"));
				aluno.setNmMae(rsmAlunos.getString("NM_MAE"));
				aluno.setDtNascimento(rsmAlunos.getGregorianCalendar("DT_NASCIMENTO"));
				aluno.setNrInep(rsmAlunos.getString("NR_INEP"));
				
				Result result = AlunoServices.save(aluno, cdUsuario, connect);
				if(result.getCode() < 0){
					if(isConnectionNull){
						Conexao.rollback(connect);
					}
					return result;
				}
				
				matricula.setNrMatriculaCenso(rsmAlunos.getString("NR_MATRICULA_CENSO"));
				matricula.setStCensoFinal(rsmAlunos.getInt("ST_CENSO_FINAL"));
				
				result = MatriculaServices.save(matricula, 0, cdUsuario, true, connect);
				if(result.getCode() < 0){
					if(isConnectionNull){
						Conexao.rollback(connect);
					}
					return result;
				}
				
			}
			rsmAlunos.beforeFirst();
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Alterações realizadas com sucesso");
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na atualização de Situação Final do Censo");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveMatriculaCurso(int cdMatricula, int cdCurso){
		return saveMatriculaCurso(cdMatricula, cdCurso, null);
	}
	
	public static Result saveMatriculaCurso(int cdMatricula, int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cdCurso == 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Curso inválido");
			}
				
			
			int retorno = connect.prepareStatement("UPDATE acd_matricula SET cd_curso = "+cdCurso+" WHERE cd_matricula = " + cdMatricula).executeUpdate();

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Modalidade/Curso cadastrado com sucesso!");
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
	 * Método que cria as matrículas do periodo letivo novo, baseado no periodo letivo antigo. Considerando que todos os alunos passaram (pois o periodo normalmente é criado antes do ano letivo terminar)
	 * Os alunos que estão no ultimo ano da rede (9 ano) não são renovadas as matrículas. Já os que estão no ultimo ano da escola, porém ainda com curso da rede municipal, são criados Em Transferência, 
	 * para já serem buscados por outras escolas
	 * @param cdInstituicao
	 * @return
	 */
	public static Result reaproveitamentoMatriculas(int cdInstituicao)	{
		return reaproveitamentoMatriculas(cdInstituicao, null);
	}
	
	public static Result reaproveitamentoMatriculas(int cdInstituicao, Connection connect)	{
		boolean isConnectionNull = connect == null;
		try	{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			//Faz a busca de todos os alunos com matrícula ativa, excetuando as matrículas de mais educação e atendimento especializado 
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT A.cd_matricula, I.cd_instituicao, A.cd_curso, A.nr_matricula, A.cd_aluno, A.tp_escolarizacao_outro_espaco, A.cd_periodo_letivo, A.cd_turma, T.nm_turma, T.cd_curso AS cd_curso_turma, T.tp_turno, IP.nm_pessoa AS nm_instituicao FROM acd_matricula A " +
					 " JOIN acd_turma T ON (A.cd_turma = T.cd_turma) " +
					 " JOIN acd_instituicao I ON (T.cd_instituicao = I.cd_instituicao) " +
					 " JOIN grl_pessoa P ON (A.cd_aluno = P.cd_pessoa) " +
					 " JOIN grl_produto_servico PS ON (T.cd_curso = PS.cd_produto_servico) " +
					 " JOIN grl_pessoa IP ON (I.cd_instituicao = IP.cd_pessoa) " +
					 " WHERE A.st_matricula IN (" + MatriculaServices.ST_ATIVA + ")" + 
                     "   AND A.cd_curso NOT IN ("+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)+")" +
					 (cdInstituicao != ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0) ? "   AND T.cd_instituicao = " + cdInstituicao : "")+
                     "  ORDER BY IP.nm_pessoa, PS.nm_produto_servico, T.nm_turma, P.nm_pessoa").executeQuery());
			
			int x =0;
			while(rsm.next()){
				//Busca o curso atual da matrícula
				Curso cursoAtual = CursoDAO.get(rsm.getInt("cd_curso"), connect);
				
				//Busca o curso posterior ao curso da matrícula, para poder matrícular o aluno no periodo letivo em construção
				//No caso dos 9 anos, a matrícula não continua a partir daqui, pois o cursoMatricula fica nulo
				Curso cursoMatricula = CursoEtapaServices.getProximoCurso(cursoAtual.getCdCurso(), rsm.getInt("cd_instituicao"), connect);
				if(cursoMatricula != null){
					InstituicaoCurso instituicaoCurso = InstituicaoCursoDAO.get(rsm.getInt("cd_instituicao"), cursoMatricula.getCdCurso(), rsm.getInt("cd_periodo_letivo"), connect);
					
					//Busca a matriz do curso
					int cdMatriz = 0;
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_curso", "" + cursoMatricula.getCdCurso(), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmCursoMatriz = CursoMatrizDAO.find(criterios, connect);
					if(rsmCursoMatriz.next()){
						cdMatriz = rsmCursoMatriz.getInt("cd_matriz");
					}
					
					//Busca o codigo do novo periodo letivo
					int cdPeriodoLetivo = 0;
					ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(rsm.getInt("cd_instituicao"), connect);
					if(rsmPeriodoRecente.next())
						cdPeriodoLetivo = rsmPeriodoRecente.getInt("cd_periodo_letivo");
					 
					//busca a turma criada, quando o quadro de vagas e as turmas também são reaproveitadas do ano anterior
					int cdTurma = 0;
					ResultSetMap rsmTurmaNova = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_turma WHERE cd_turma_anterior = " + rsm.getInt("cd_turma")).executeQuery());
					if(rsmTurmaNova.next())
						cdTurma = rsmTurmaNova.getInt("cd_turma");
					
					//Situação de matrícula padrão será pendente
					int stMatricula = ST_PENDENTE;
					
					//Caso o periodo tenha sido criado zerado (em relação a turmas)
					if(cdTurma == 0){
						//Busca o nome da turma em que o aluno já estava
						String nmTurmaAntiga = rsm.getString("nm_turma");
						
						//Se o sistema identificar que a instituicao possui instituicao curso, do novo curso para onde o aluno irá
						if(instituicaoCurso != null){
							Curso cursoTurma = CursoDAO.get(rsm.getInt("cd_curso_turma"), connect);
							
							//Itera sobre as turmas e descobre se há uma turma de mesmo nome e turno que o periodo anterior.
							ResultSetMap rsmTurmas = TurmaServices.getAllTurmasByPeriodo(cdPeriodoLetivo, (cursoTurma.getLgMulti()==1?cursoTurma.getCdCurso():cursoMatricula.getCdCurso()));
							boolean encontrado = false;
							while(rsmTurmas.next()){
								if(rsmTurmas.getString("nm_turma").equals(nmTurmaAntiga) && rsmTurmas.getInt("tp_turno") == rsm.getInt("tp_turno")){
									cdTurma = rsmTurmas.getInt("cd_turma");
									encontrado = true;
									break;
								}
							}
							rsmTurmas.beforeFirst();
							
							//Caso não ache uma turma de mesmo nome e turno
							if(!encontrado){
								
								//O sistema vai para a segunda parte do primeiro criterio critério, colocando o aluno na primeira turma da mesma ordem e turno que o anterior
								encontrado = false;
								while(rsmTurmas.next()){
									if(TurmaServices.getOrdemTurma(rsmTurmas.getInt("cd_turma"), connect) == TurmaServices.getOrdemTurma(rsm.getInt("cd_turma"), connect) && 
											rsmTurmas.getInt("tp_turno") == rsm.getInt("tp_turno")){
										cdTurma = rsmTurmas.getInt("cd_turma");
										encontrado = true;
										break;
									}
								}
								rsmTurmas.beforeFirst();
								
								//Caso não ache uma turma de mesmo nome (ou ordem) e turno
								if(!encontrado){
									
									//O sistema vai para o segundo critério, colocando o aluno na primeira turma do mesmo turno que o anterior
									encontrado = false;
									while(rsmTurmas.next()){
										if(rsmTurmas.getInt("tp_turno") == rsm.getInt("tp_turno")){
											cdTurma = rsmTurmas.getInt("cd_turma");
											encontrado = true;
											break;
										}
									}
									rsmTurmas.beforeFirst();
									
									//Caso não ache uma turma de mesmo turno
									if(!encontrado){
										
										//O sistema vai para o terceiro critério, colocando o aluno na primeira turma do mesmo nome que o anterior
										encontrado = false;
										while(rsmTurmas.next()){
											if(rsmTurmas.getString("nm_turma").equals(nmTurmaAntiga)){
												cdTurma = rsmTurmas.getInt("cd_turma");
												encontrado = true;
												break;
											}
										}
										rsmTurmas.beforeFirst();
										
										//Caso ainda sim não seja encontrado
										if(!encontrado){
											int qtTurmas = rsmTurmas.size();
											while(rsmTurmas.next()){
												//Coloca o aluno na primeira turma com vaga disponível
												int qtMatriculados = TurmaServices.getAlunos(rsmTurmas.getInt("cd_turma"), connect).size();
												if(rsmTurmas.getInt("qt_vagas") > qtMatriculados){
													cdTurma = rsmTurmas.getInt("cd_turma");
													encontrado = true;
													break;
												}
												qtTurmas--;
												if(qtTurmas == 0 && !encontrado){
													cdTurma = rsmTurmas.getInt("cd_turma");
												}
											}
											rsmTurmas.beforeFirst();
										}
									}
								}
							}
						}
						//Caso não tenha instituição curso, significa que o curso não tem na escola, logo o aluno é colocado em transferência (criando uma turma de transferencia, caso ainda não tenha para o curso em questão)
						else{
							
							//Caso ele seja registrado em uma turma de transferencia, ficará com a matrícula em transferencia
							stMatricula = ST_EM_TRANSFERENCIA;
							
							criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cd_periodo_letivo", "" + cdPeriodoLetivo, ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("cd_curso", "" + cursoMatricula.getCdCurso(), ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("nm_turma", "TRANSFERENCIA", ItemComparator.EQUAL, Types.VARCHAR));
							ResultSetMap rsmTurmaTransferencia = TurmaDAO.find(criterios, connect);
							if(rsmTurmaTransferencia.next()){
								cdTurma = rsmTurmaTransferencia.getInt("cd_turma");
							}
							else{
								//Insere a turma de transferência onde serão colocados os alunos que irão para outras escolas, por transferencia
								cdTurma = TurmaDAO.insert(new Turma(0/*cdTurma*/, cdMatriz, cdPeriodoLetivo, "TRANSFERENCIA", Util.getDataAtual(), null/*dtConclusao*/, 
																	0/*tpTurno*/, 0/*cdCategoriaMensalidade*/, 0/*cdCategoriaMatricula*/, TurmaServices.ST_ATIVO/*stTurma*/, 0/*cdTabelaPreco*/, 
																	rsm.getInt("cd_instituicao"), cursoMatricula.getCdCurso(), 0/*qtVagas*/, 0/*cdCursoModulo*/, null/*nrInep*/, 0/*qtDiasSemanaAtividade*/, 
																	0/*tpAtendimento*/, 0/*tpModalidadeEnsino*/, null/*idTurma*/, 0/*tpEducacaoInfantil*/, 0/*lgMaisEduca*/, 
																	0/*cdTurmaAnterior*/, 0/*tpTurnoAtividadeComplementar*/, 0/*tpLocalDiferenciado*/), connect);
								
								if(cdTurma < 0){
									if(isConnectionNull){
										Conexao.rollback(connect);
									}
									return new Result(-1, "Erro ao criar turma de transferencia");
								}
								
							}
							
							
						}
					}
					boolean possuiMatricula = false;
					
					//Caso o aluno já tenha matrícula, o processo não é continuado
					ResultSetMap rsmMatriculaAluno = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula WHERE cd_aluno = " + rsm.getInt("cd_aluno") + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery());
					possuiMatricula = rsmMatriculaAluno.next();
					
					if(cdTurma != 0 && !possuiMatricula){
						System.out.println("Matriculas feitas: " + (++x));
						Matricula matriculaNova = new Matricula(0/*cdMatricula*/, cdMatriz, cdTurma, cdPeriodoLetivo, Util.getDataAtual(), null/*dtConclusao*/, stMatricula, TP_NORMAL, rsm.getString("nr_matricula"), rsm.getInt("cd_aluno"), 
								rsm.getInt("cd_matricula"), 0/*cdReserva*/, 0/*cdAreaInteresse*/, null/*txtObservacao*/, null/*txtBoletim*/, cursoMatricula.getCdCurso(), 0/*cdPreMatricula*/, 
								rsm.getInt("tp_escolarizacao_outro_espaco"), 0/*lgTransportePublico*/, 0/*tpPoderResponsavel*/, 0/*tpFormaIngresso*/, null/*txtDocumentoOficial*/, 
								null/*dtInterrupcao*/, 0/*lgAutorizacaoRematricula*/, 0/*lgAtividadeComplementar*/, 0/*lgReprovacao*/, 0/*stMatriculaCentaurus*/, ST_ALUNO_CENSO_APROVADO, null/*nrMatriculaCenso*/, ST_CENSO_FINAL_NAO_LANCADO, rsm.getString("NM_INSTITUICAO"));
						
						if(MatriculaDAO.insert(matriculaNova, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao inserir matricula");
						}
					}
				}
				
			}
			rsm.beforeFirst();
			
			System.out.println("FINALIZADO");
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, (rsm.size() == 0 ? "Não houveram matrículas no ano anterior" : "Reaproveitamento realizado com sucesso"));
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Faz a busca da matricula regular de um aluno no periodo atual
	 * @param cdAluno
	 * @return
	 */
	public static ResultSetMap getMatriculaRegularAtualByAluno(int cdAluno)	{
		return getMatriculaRegularAtualByAluno(cdAluno, null);
	}
	
	public static ResultSetMap getMatriculaRegularAtualByAluno(int cdAluno, Connection connect)	{
		boolean isConnectionNull = connect == null;
		try	{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			ResultSetMap rsmPeriodoAtualSecretaria = InstituicaoServices.getPeriodoLetivoVigente(cdSecretaria, connect);
			InstituicaoPeriodo instituicaoPeriodoAtualSecretaria = null;
			if(rsmPeriodoAtualSecretaria.next()){
				instituicaoPeriodoAtualSecretaria = InstituicaoPeriodoDAO.get(rsmPeriodoAtualSecretaria.getInt("cd_periodo_letivo"), connect);
			}
			
			ResultSetMap rsmMatriculaNoPeriodo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula A, acd_instituicao_periodo B "
					+ "																			WHERE A.cd_periodo_letivo = B.cd_periodo_letivo "
					+ "																			  AND A.cd_aluno = " + cdAluno 
					+ " 																		  AND B.nm_periodo_letivo = '"+instituicaoPeriodoAtualSecretaria.getNmPeriodoLetivo()+ "'"
					+ " 																		  AND A.st_matricula = " + MatriculaServices.ST_ATIVA
					+ "																			  AND A.cd_curso NOT IN ("+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)+")").executeQuery());
			
			return rsmMatriculaNoPeriodo;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Faz a busca da matricula regular de um aluno no periodo recente
	 * @param cdAluno
	 * @return
	 */
	public static ResultSetMap getMatriculaRegularRecenteByAluno(int cdAluno)	{
		return getMatriculaRegularRecenteByAluno(cdAluno, null);
	}
	
	public static ResultSetMap getMatriculaRegularRecenteByAluno(int cdAluno, Connection connect)	{
		boolean isConnectionNull = connect == null;
		try	{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			ResultSetMap rsmPeriodoRecenteSecretaria = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdSecretaria, connect);
			InstituicaoPeriodo instituicaoPeriodoRecenteSecretaria = null;
			if(rsmPeriodoRecenteSecretaria.next()){
				instituicaoPeriodoRecenteSecretaria = InstituicaoPeriodoDAO.get(rsmPeriodoRecenteSecretaria.getInt("cd_periodo_letivo"), connect);
			}
			
			ResultSetMap rsmMatriculaNoPeriodo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula A, acd_instituicao_periodo B "
					+ "																			WHERE A.cd_periodo_letivo = B.cd_periodo_letivo "
					+ "																			  AND A.cd_aluno = " + cdAluno 
					+ " 																		  AND B.nm_periodo_letivo = '"+instituicaoPeriodoRecenteSecretaria.getNmPeriodoLetivo()+ "'"
					+ " 																		  AND A.st_matricula = " + MatriculaServices.ST_ATIVA
					+ "																			  AND A.cd_curso NOT IN ("+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)+")").executeQuery());
			
			return rsmMatriculaNoPeriodo;
			
			
			
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Faz a busca da matricula regular para o aplicativo do aluno
	 * @param cdAluno
	 * @return
	 */
	public static ResultSetMap getMatriculaRegularByAluno(int cdAluno)	{
		return getMatriculaRegularRecenteByAluno(cdAluno, null);
	}
	
	public static ResultSetMap getMatriculaRegularByAluno(int cdAluno, Connection connect)	{
		boolean isConnectionNull = connect == null;
		try	{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			ResultSetMap rsmPeriodoRecenteSecretaria = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdSecretaria, connect);
			InstituicaoPeriodo instituicaoPeriodoRecenteSecretaria = null;
			if(rsmPeriodoRecenteSecretaria.next()){
				instituicaoPeriodoRecenteSecretaria = InstituicaoPeriodoDAO.get(rsmPeriodoRecenteSecretaria.getInt("cd_periodo_letivo"), connect);
			}
			
			ResultSetMap rsmMatriculaNoPeriodo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula A, acd_instituicao_periodo B "
					+ "																			WHERE A.cd_periodo_letivo = B.cd_periodo_letivo "
					+ "																			  AND A.cd_aluno = " + cdAluno 
					+ " 																		  AND B.nm_periodo_letivo = '"+instituicaoPeriodoRecenteSecretaria.getNmPeriodoLetivo()+ "'"
					+ " 																		  AND A.st_matricula = " + MatriculaServices.ST_ATIVA
					+ "																			  AND A.cd_curso NOT IN ("+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)+")").executeQuery());
			
			
			
			while(rsmMatriculaNoPeriodo.next()){
				 Curso cursoAtual = CursoDAO.get(rsmMatriculaNoPeriodo.getInt("cd_curso"), connect);
				rsmMatriculaNoPeriodo.setValueToField("NM_CURSO", cursoAtual.getNmProdutoServico());
				 Turma turmaAtual = TurmaDAO.get(rsmMatriculaNoPeriodo.getInt("cd_turma"), connect);
				 Pessoa aluno = PessoaDAO.get(rsmMatriculaNoPeriodo.getInt("cd_aluno"), connect);
				 rsmMatriculaNoPeriodo.setValueToField("NM_ALUNO", aluno.getNmPessoa());
				 rsmMatriculaNoPeriodo.setValueToField("NM_TURMA", turmaAtual.getNmTurma());
				 rsmMatriculaNoPeriodo.setValueToField("TP_CURSO", turmaAtual.getTpTurno());
				 rsmMatriculaNoPeriodo.setValueToField("NM_TURNO", TurmaServices.tiposTurno[rsmMatriculaNoPeriodo.getInt("TP_TURNO")]);
				 
			}
			rsmMatriculaNoPeriodo.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_CURSO");
			fields.add("NM_TURMA");
			fields.add("NM_TURNO");
			fields.add("NM_ALUNO");
			//rsmMatriculaNoPeriodo.orderBy(fields);
			
			rsmMatriculaNoPeriodo.beforeFirst();
			
			return rsmMatriculaNoPeriodo;
			
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result validacaoMatricula(int cdMatricula, int cdUsuario){
		return validacaoMatricula(cdMatricula, cdUsuario, null);
	}
	
	public static Result validacaoMatricula(int cdMatricula, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Matricula matricula = MatriculaDAO.get(cdMatricula, connect);
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			
			ValidatorResult resultadoValidacao = validate(matricula, true, true, true, cdUsuario, GRUPO_VALIDACAO_EDUCACENSO, connect);
			Result resultValidacoesPendencia = InstituicaoPendenciaServices.atualizarValidacaoPendencia(resultadoValidacao, turma.getCdInstituicao(), turma.getCdTurma(), matricula.getCdAluno(), 0/*cdProfessor*/, InstituicaoPendenciaServices.TP_REGISTRO_MATRICULA_CENSO, cdUsuario, GRUPO_VALIDACAO_EDUCACENSO, connect);
			if(resultValidacoesPendencia.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return resultValidacoesPendencia;
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
	
	public static ValidatorResult validate(Matricula matricula, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, boolean verificarDeficiencia, int cdUsuario, int idGrupo){
		return validate(matricula, permitirAlunoCidadeDiferente, permitirAlunoIdadeDivergente, verificarDeficiencia, cdUsuario, idGrupo, null);
	}
	
	public static ValidatorResult validate(Matricula matricula, boolean permitirAlunoCidadeDiferente, boolean permitirAlunoIdadeDivergente, boolean verificarDeficiencia, int cdUsuario, int idGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(matricula == null){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new ValidatorResult(ValidatorResult.ERROR, "Matrícula não encontrada");
			}
			
			
			ResultSetMap rsmTransporte = MatriculaTipoTransporteServices.getTipoTransporteByMatricula(matricula.getCdMatricula(), connect);
			ResultSetMap rsmDeficiencia = PessoaNecessidadeEspecialServices.getNecessidadeEspecialByPessoa(matricula.getCdAluno(), connect);
			
			ValidatorResult result = new ValidatorResult(ValidatorResult.VALID, "Matrícula passou pela validação");
			HashMap<Integer, Validator> listValidator = getListValidation();
			
			//TRANSPORTE PUBLICO - Verifica caso haja a informação de transporte publico na matrícula, se há tipos de transporte cadastrado
			if(matricula.getLgTransportePublico()==1 && rsmTransporte.size()==0){
				listValidator.get(VALIDATE_TRANSPORTE_ESCOLAR).add(ValidatorResult.ERROR, "Não informado o tipo de transporte que o aluno necessita", GRUPO_VALIDACAO_EDUCACENSO);
				listValidator.get(VALIDATE_TRANSPORTE_ESCOLAR).add(ValidatorResult.ERROR, "Não informado o tipo de transporte que o aluno necessita", GRUPO_VALIDACAO_UPDATE);
			}
			
			//TRANSPORTE PUBLICO - Verifica caso não haja a informação de transporte publico na matrícula, se há tipos de transporte cadastrado
			if(matricula.getLgTransportePublico()==0 && rsmTransporte.size()>0){
				listValidator.get(VALIDATE_TIPOS_TRANSPORTE).add(ValidatorResult.ERROR, "Informado o tipo de transporte que o aluno necessita, porém não marcado que o mesmo necessita de transporte", GRUPO_VALIDACAO_EDUCACENSO);
				listValidator.get(VALIDATE_TIPOS_TRANSPORTE).add(ValidatorResult.ALERT, "Informado o tipo de transporte que o aluno necessita, porém não marcado que o mesmo necessita de transporte", GRUPO_VALIDACAO_UPDATE);
			}
			
			//TRANSPOTE PUBLICO - Verifica se não mais de 3 tipos de transporte cadastrados para o aluno
			if(rsmTransporte.size() > 3){
				listValidator.get(VALIDATE_MAX_TIPOS_TRANSPORTE).add(ValidatorResult.ERROR, "Possui mais do que 3 tipos de transporte cadastrado", GRUPO_VALIDACAO_EDUCACENSO);
				listValidator.get(VALIDATE_MAX_TIPOS_TRANSPORTE).add(ValidatorResult.ALERT, "Possui mais do que 3 tipos de transporte cadastrado", GRUPO_VALIDACAO_UPDATE);
			}
			
			//CURSO MULTI - Verifica se o curso da matricula é um curso multi
			if(  matricula.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_REGULAR", 0)
			  || matricula.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_CRECHE", 0)
			  || matricula.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_PRE_ESCOLA", 0)
			  || matricula.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_ANOS_INICIAIS", 0)
			  || matricula.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_ANOS_FINAIS", 0)){
				listValidator.get(VALIDATE_CURSO_MULTI).add(ValidatorResult.ERROR, "Modalidade/Curso da matrícula está como Multi, altere para a modalidade/curso correta");
			}
			
			//HORARIOS - Verifica se o aluno possui horários conflitantes em suas matrículas
			ResultSetMap rsmHorariosTurma = TurmaServices.getHorariosByTurma(matricula.getCdTurma());
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect);
			ResultSetMap rsmMatriculaNoPeriodo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula A, acd_instituicao_periodo B WHERE A.cd_periodo_letivo = B.cd_periodo_letivo AND cd_matricula <> " + matricula.getCdMatricula() + " AND cd_aluno = " + matricula.getCdAluno() + " AND B.nm_periodo_letivo = '"+instituicaoPeriodo.getNmPeriodoLetivo()+"' AND st_matricula = " + MatriculaServices.ST_ATIVA).executeQuery());
			while(rsmMatriculaNoPeriodo.next()){
				
				Turma turmaMatricula = TurmaDAO.get(matricula.getCdTurma(), connect);
				Turma turmaMatriculaNoPeriodo = TurmaDAO.get(rsmMatriculaNoPeriodo.getInt("cd_turma"), connect);
				
				if((turmaMatricula.getTpAtendimento() == TurmaServices.TP_ATENDIMENTO_AEE && (turmaMatriculaNoPeriodo.getTpAtendimento() == TurmaServices.TP_ATENDIMENTO_ATIVIDADE_COMPLEMENTAR || turmaMatriculaNoPeriodo.getLgMaisEduca() == 1)) || 
				   ((turmaMatricula.getTpAtendimento() == TurmaServices.TP_ATENDIMENTO_ATIVIDADE_COMPLEMENTAR || turmaMatriculaNoPeriodo.getLgMaisEduca() == 1) && turmaMatriculaNoPeriodo.getTpAtendimento() == TurmaServices.TP_ATENDIMENTO_AEE)){
					continue;
				}
				
				
				boolean identificado = false;
				ResultSetMap rsmHorariosTurmaCadastrada = TurmaServices.getHorariosByTurma(rsmMatriculaNoPeriodo.getInt("cd_turma"));
				while(rsmHorariosTurmaCadastrada.next()){
					while(rsmHorariosTurma.next()){
						InstituicaoHorario instituicaoHorarioTurmaCadastrada = InstituicaoHorarioDAO.get(rsmHorariosTurmaCadastrada.getInt("cd_horario"), connect);
						InstituicaoHorario instituicaoHorarioTurma			 = InstituicaoHorarioDAO.get(rsmHorariosTurma.getInt("cd_horario"), connect);
						
						if((instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) && 
							 instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.MINUTE) == instituicaoHorarioTurma.getHrInicio().get(Calendar.MINUTE)) ||
						   ((instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) > instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) || 
							(instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) && 
							 instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.MINUTE) > instituicaoHorarioTurma.getHrInicio().get(Calendar.MINUTE))) && 
							 instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) < instituicaoHorarioTurma.getHrTermino().get(Calendar.HOUR_OF_DAY) || 
							(instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurma.getHrTermino().get(Calendar.HOUR_OF_DAY) && 
							 instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.MINUTE) < instituicaoHorarioTurma.getHrTermino().get(Calendar.MINUTE))) ||
						   ((instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) > instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) || 
						    (instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) && 
						     instituicaoHorarioTurma.getHrInicio().get(Calendar.MINUTE) > instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.MINUTE))) && 
						     instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) < instituicaoHorarioTurmaCadastrada.getHrTermino().get(Calendar.HOUR_OF_DAY) || 
						    (instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurmaCadastrada.getHrTermino().get(Calendar.HOUR_OF_DAY) && 
						     instituicaoHorarioTurma.getHrInicio().get(Calendar.MINUTE) < instituicaoHorarioTurmaCadastrada.getHrTermino().get(Calendar.MINUTE))) &&
						     instituicaoHorarioTurmaCadastrada.getNrDiaSemana() == instituicaoHorarioTurma.getNrDiaSemana()){
							
							listValidator.get(VALIDATE_HORARIOS_CONFLITANTES).add(ValidatorResult.ERROR, "Este aluno possui matrícula em horários conflitantes");
						}
					}
					rsmHorariosTurma.beforeFirst();
					if(identificado){
						break;
					}
				}
				rsmHorariosTurmaCadastrada.beforeFirst();
				
			}
			rsmMatriculaNoPeriodo.beforeFirst();
			
			//PERIODO ANTERIOR - Verificar se o aluno esta sendo matriculado em periodo anterior ao atual
			Turma turmaMatricula = TurmaDAO.get(matricula.getCdTurma(), connect);
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(turmaMatricula.getCdInstituicao(), connect);
			rsmPeriodoAtual.next();
			if(!UsuarioServices.isSuporte(cdUsuario, connect) && Integer.parseInt(InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect).getNmPeriodoLetivo()) < Integer.parseInt(InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect).getNmPeriodoLetivo())){
				listValidator.get(VALIDATE_MATRICULA_PERIODO_ANTERIOR).add(ValidatorResult.ERROR, "Não é possível realizar essa ação para matrícula de períodos anteriores");
			}
			
			//CIDADE DIFERENTE - Verifica se a cidade do aluno é diferente da instituição
			if(!permitirAlunoCidadeDiferente){
				if(turmaMatricula != null){
					int cdCidadeInstituicao = 0;
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_pessoa", "" + turmaMatricula.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmEnderecoInstituicao = PessoaEnderecoDAO.find(criterios, connect);
					if(rsmEnderecoInstituicao.next()){
						cdCidadeInstituicao = rsmEnderecoInstituicao.getInt("cd_cidade");
					}
					
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_pessoa", "" + matricula.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmEnderecoAluno = PessoaEnderecoDAO.find(criterios, connect);
					if(rsmEnderecoAluno.next()){
						int cdCidadeAluno = rsmEnderecoAluno.getInt("cd_cidade");
						if(cdCidadeInstituicao > 0 && cdCidadeAluno > 0 && cdCidadeInstituicao != cdCidadeAluno){
							result.setCode(ERR_CIDADE_ALUNO_DIFERENTE);
							result.setMessage("Aluno possui endereço em cidade diferente da Escola.");
							return result;
						}
					}
				}
			}
			
			InstituicaoPeriodo periodoLetivo = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect);
			
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_matricula_origem", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_MATRICULA_PERMISSAO_ESPECIAL, connect).getCdTipoOcorrencia(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmOcorrenciaPermissaoEspecial = OcorrenciaMatriculaServices.find(criterios, connect);
			boolean validacaoForaIdade = true;
			
			if((cdUsuario == ParametroServices.getValorOfParametroAsInteger("CD_USUARIO_PERMISSAO_ESPECIAL", 0)) || rsmOcorrenciaPermissaoEspecial.size() > 0){
				validacaoForaIdade = false;
			}
			
			Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			int qtIdade = Util.getIdade(aluno.getDtNascimento(), 31, 2, Integer.parseInt(periodoLetivo.getNmPeriodoLetivo()));
			Curso cursoTurma = CursoDAO.get(turma.getCdCurso(), connect);
			Curso cursoMatricula = CursoDAO.get(matricula.getCdCurso(), connect);
			ResultSetMap rsmPessoaNecessidadeEspecial = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_pessoa_necessidade_especial WHERE cd_pessoa = " + aluno.getCdAluno()).executeQuery());
			boolean possuiDeficiencia = (rsmPessoaNecessidadeEspecial.next());
			
			if(cursoTurma != null && matricula.getLgPermissaoForaIdade() == 0){
				PreparedStatement pstmt =connect.prepareStatement("SELECT * FROM acd_curso_multi WHERE cd_curso_multi = ? AND cd_curso = " + cursoMatricula.getCdCurso());
				
				boolean entrou = false;
				if(!possuiDeficiencia){
					pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_CRECHE", 0));
					ResultSetMap rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
					if(rsmCursoMatricula.next()){
						//CURSO IDADE - Verifica se o aluno esta sendo matriculado em creche de 2 anos, fora da idade
						if(cursoMatricula.getCdCurso() == 1179/*Código de Curso fixo para Creche 2 anos*/ && qtIdade != 2){
							listValidator.get(VALIDATE_MATRICULA_CURSO_2_ANOS).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade diferente de 2 anos na modalidade Creche (2 Anos)", GRUPO_VALIDACAO_INSERT);
							listValidator.get(VALIDATE_MATRICULA_CURSO_2_ANOS).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade diferente de 2 anos na modalidade Creche (2 Anos)", GRUPO_VALIDACAO_UPDATE);
							validacaoForaIdade = true;
						}
						//CURSO IDADE - Verifica se o aluno esta sendo matriculado em creche de 3 anos, fora da idade
						else if(cursoMatricula.getCdCurso() == 1147/*Código de Curso fixo para Creche 3 anos*/ && qtIdade != 3){
							listValidator.get(VALIDATE_MATRICULA_CURSO_3_ANOS).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade diferente de 3 anos na modalidade Creche (3 Anos)", GRUPO_VALIDACAO_INSERT);
							listValidator.get(VALIDATE_MATRICULA_CURSO_3_ANOS).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade diferente de 3 anos na modalidade Creche (3 Anos)", GRUPO_VALIDACAO_UPDATE);
							validacaoForaIdade = true;
						}
						entrou = true;
					}
					else{
						//CURSO IDADE - Verifica se o aluno esta sendo matriculado em creche de 2 anos, fora da idade
						if(cursoMatricula.getCdCurso() != 1179/*Código de Curso fixo para Creche 2 anos*/ && qtIdade == 2){
							listValidator.get(VALIDATE_MATRICULA_CURSO_2_ANOS).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade de 2 anos em modalidade diferente de Creche (2 Anos)", GRUPO_VALIDACAO_INSERT);
							listValidator.get(VALIDATE_MATRICULA_CURSO_2_ANOS).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade de 2 anos em modalidade diferente de Creche (2 Anos)", GRUPO_VALIDACAO_UPDATE);
							validacaoForaIdade = true;
						}
						//CURSO IDADE - Verifica se o aluno esta sendo matriculado em creche de 3 anos, fora da idade
						else if(cursoMatricula.getCdCurso() != 1147/*Código de Curso fixo para Creche 3 anos*/ && qtIdade == 3){
							listValidator.get(VALIDATE_MATRICULA_CURSO_3_ANOS).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade de 3 anos em modalidade diferente de Creche (3 Anos)", GRUPO_VALIDACAO_INSERT);
							listValidator.get(VALIDATE_MATRICULA_CURSO_3_ANOS).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade de 3 anos em modalidade diferente de Creche (3 Anos)", GRUPO_VALIDACAO_UPDATE);
							validacaoForaIdade = true;
						}
					}
					
					pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_PRE_ESCOLA", 0));
					rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
					if(rsmCursoMatricula.next()){
						if(cursoMatricula.getCdCurso() == 1181/*Código de Curso fixo para Educação Infantil - 04 Anos*/ && qtIdade != 4){
							//CURSO IDADE - Verifica se o aluno esta sendo matriculado em educacao infantil de 4 anos, fora da idade
							listValidator.get(VALIDATE_MATRICULA_CURSO_4_ANOS).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade diferente de 4 anos na modalidade Educação Infantil - 04 Anos", GRUPO_VALIDACAO_INSERT);
							listValidator.get(VALIDATE_MATRICULA_CURSO_4_ANOS).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade diferente de 4 anos na modalidade Educação Infantil - 04 Anos", GRUPO_VALIDACAO_UPDATE);
							validacaoForaIdade = true;
						}
						//CURSO IDADE - Verifica se o aluno esta sendo matriculado em educacao infantil de 5 anos, fora da idade
						else if(cursoMatricula.getCdCurso() == 1159/*Código de Curso fixo para Educação Infantil - 05 Anos*/ && qtIdade != 5){
							listValidator.get(VALIDATE_MATRICULA_CURSO_5_ANOS).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade diferente de 5 anos na modalidade Educação Infantil - 05 Anos", GRUPO_VALIDACAO_INSERT);
							listValidator.get(VALIDATE_MATRICULA_CURSO_5_ANOS).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade diferente de 5 anos na modalidade Educação Infantil - 05 Anos", GRUPO_VALIDACAO_UPDATE);
							validacaoForaIdade = true;
						}
						entrou = true;
					}
					else{
						if(cursoMatricula.getCdCurso() != 1181/*Código de Curso fixo para Educação Infantil - 04 Anos*/ && qtIdade == 4){
							//CURSO IDADE - Verifica se o aluno esta sendo matriculado em educacao infantil de 4 anos, fora da idade
							listValidator.get(VALIDATE_MATRICULA_CURSO_4_ANOS).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade de 4 anos em modalidade diferente de Educação Infantil - 04 Anos", GRUPO_VALIDACAO_INSERT);
							listValidator.get(VALIDATE_MATRICULA_CURSO_4_ANOS).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade de 4 anos em modalidade diferente de Educação Infantil - 04 Anos", GRUPO_VALIDACAO_UPDATE);
							validacaoForaIdade = true;
						}
						//CURSO IDADE - Verifica se o aluno esta sendo matriculado em educacao infantil de 5 anos, fora da idade
						else if(cursoMatricula.getCdCurso() != 1159/*Código de Curso fixo para Educação Infantil - 05 Anos*/ && qtIdade == 5){
							listValidator.get(VALIDATE_MATRICULA_CURSO_5_ANOS).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade de 5 anos em modalidade diferente de Educação Infantil - 05 Anos", GRUPO_VALIDACAO_INSERT);
							listValidator.get(VALIDATE_MATRICULA_CURSO_5_ANOS).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade de 5 anos em modalidade diferente de Educação Infantil - 05 Anos", GRUPO_VALIDACAO_UPDATE);
							validacaoForaIdade = true;
						}
					}
				}
				
				if(CursoServices.isEja(cursoTurma.getCdCurso(), connect)){
					//CURSO IDADE - Verifica se o aluno esta sendo matriculado em eja, menor do que 15 anos
					if(qtIdade < 15){
						listValidator.get(VALIDATE_MATRICULA_EJA).add(ValidatorResult.ERROR, "Não é possível matricular aluno com idade menor do que 15 anos no EJA");
						validacaoForaIdade = true;
					}
					entrou = true;
				}

//				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
//				criterios.add(new ItemComparator("cd_matricula_origem", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
//				criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_MATRICULA_PERMISSAO_ESPECIAL, connect).getCdTipoOcorrencia(), ItemComparator.EQUAL, Types.INTEGER));
//				criterios.add(new ItemComparator("cd_usuario", "" + ParametroServices.getValorOfParametroAsInteger("CD_USUARIO_PERMISSAO_ESPECIAL", 0), ItemComparator.EQUAL, Types.INTEGER));
//				ResultSetMap rsmOcorrenciaPermissaoEspecial = OcorrenciaMatriculaServices.find(criterios, connect);
				
				
// 				if(validacaoForaIdade && cdUsuario == ParametroServices.getValorOfParametroAsInteger("CD_USUARIO_PERMISSAO_ESPECIAL", 0)){
// 					result.setCode(ERR_ALUNO_FORA_IDADE_PERMISSAO_ESPECIAL);
//					result.setMessage("Aluno do Infantil ou do EJA com idade fora do permitido. Necessário permissão especial.");
//					return result;
//				}
//				else 
				//Verificar se tanto o usuário especial, quanto uma matricula feita com o usuário especial está atualizando o listValidator
				if((validacaoForaIdade && cdUsuario == ParametroServices.getValorOfParametroAsInteger("CD_USUARIO_PERMISSAO_ESPECIAL", 0)) || rsmOcorrenciaPermissaoEspecial.size() > 0){
					if(!permitirAlunoIdadeDivergente && rsmOcorrenciaPermissaoEspecial.size() == 0) {
						result.setCode(ERR_ALUNO_FORA_IDADE);
						result.setMessage("Aluno com idade divergente do curso.");
						return result;
					}
					listValidator.get(VALIDATE_MATRICULA_CURSO_2_ANOS).add(ValidatorResult.VALID, "", GRUPO_VALIDACAO_INSERT);
					listValidator.get(VALIDATE_MATRICULA_CURSO_2_ANOS).add(ValidatorResult.VALID, "", GRUPO_VALIDACAO_UPDATE);
					
					listValidator.get(VALIDATE_MATRICULA_CURSO_3_ANOS).add(ValidatorResult.VALID, "", GRUPO_VALIDACAO_INSERT);
					listValidator.get(VALIDATE_MATRICULA_CURSO_3_ANOS).add(ValidatorResult.VALID, "", GRUPO_VALIDACAO_UPDATE);
					
					listValidator.get(VALIDATE_MATRICULA_CURSO_4_ANOS).add(ValidatorResult.VALID, "", GRUPO_VALIDACAO_INSERT);
					listValidator.get(VALIDATE_MATRICULA_CURSO_4_ANOS).add(ValidatorResult.VALID, "", GRUPO_VALIDACAO_UPDATE);
					
					listValidator.get(VALIDATE_MATRICULA_CURSO_5_ANOS).add(ValidatorResult.VALID, "", GRUPO_VALIDACAO_INSERT);
					listValidator.get(VALIDATE_MATRICULA_CURSO_5_ANOS).add(ValidatorResult.VALID, "", GRUPO_VALIDACAO_UPDATE);
					
					listValidator.get(VALIDATE_MATRICULA_EJA).add(ValidatorResult.VALID, "", GRUPO_VALIDACAO_INSERT);
					listValidator.get(VALIDATE_MATRICULA_EJA).add(ValidatorResult.VALID, "", GRUPO_VALIDACAO_UPDATE);
				}

				
				if(!permitirAlunoIdadeDivergente && rsmOcorrenciaPermissaoEspecial.size() == 0){
					pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_REGULAR", 0));
					ResultSetMap rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
					//CURSO IDADE - Verifica se o aluno esta sendo matriculado no fundamental regular, e esta fora da idade por 2 anos ou mais
					if(rsmCursoMatricula.next() && !entrou && !permitirAlunoIdadeDivergente){
						if(qtIdade > (cursoMatricula.getNrIdade()+1) || qtIdade < (cursoMatricula.getNrIdade()-1)){
							result.setCode(ERR_ALUNO_FORA_IDADE);
							result.setMessage("Aluno do regular com idade divergente do curso.");
							return result;
						}
					}
				}
				
			}
			
			//MODALIDADE INCOMPATIVEL - Modalidade de matrícula incompativel com modalidade de turma
			if(cursoTurma.getLgMulti() == 0 && cursoTurma.getCdCurso() != cursoMatricula.getCdCurso() || 
			   cursoTurma.getLgMulti() == 1 && CursoMultiDAO.get(cursoTurma.getCdCurso(), cursoMatricula.getCdCurso(), connect) == null ){
				listValidator.get(VALIDATE_MODALIDADES_INCOMPATIVEIS).add(ValidatorResult.ERROR, "Modalidade/Curso de matrícula incompatível com a modalidade/curso da turma");
			}
			
			//DATA DE MATRICULA - Verifica se a matrícula possui data de matrícula
			if(matricula.getDtMatricula() == null){
				listValidator.get(VALIDATE_DATA_MATRICULA).add(ValidatorResult.ERROR, "Faltando Data de matrícula");
			}
			
			//Faz a verificação se a turma atingiu seu limite, excluindo as turmas do EJA
			ResultSetMap rsmAlunosAtivos = TurmaServices.getAlunosSimplificado(turma.getCdTurma(), connect);
			if(matricula.getStMatricula() == ST_ATIVA && !CursoServices.isEja(cursoTurma.getCdCurso(), connect) && !turma.getNmTurma().equals("TRANSFERENCIA") && (turma.getQtVagas() + (turma.getQtVagas() * 10 / 100)) < rsmAlunosAtivos.size()){
				listValidator.get(VALIDATE_TURMA_CHEIA).add(ValidatorResult.ERROR, "A turma "+cursoTurma.getNmProdutoServico() + " - " + turma.getNmTurma()+" atingiu seu limite de vagas ("+turma.getQtVagas()+" + "+(turma.getQtVagas() * 10 / 100)+" extra)");
			}
			
			//Impede alunos de mais de 18 anos ficarem no diurno
			if(turma.getTpLocalDiferenciado() != TurmaServices.TP_LOCAL_DIFERENCIADO_PRISIONAL && qtIdade >= 18 && !possuiDeficiencia && (turma.getTpTurno() == TurmaServices.TP_MATUTINO || turma.getTpTurno() == TurmaServices.TP_VESPERTINO || turma.getTpTurno() == TurmaServices.TP_INTEGRAL) && cdUsuario != ParametroServices.getValorOfParametroAsInteger("CD_USUARIO_PERMISSAO_ESPECIAL", 0)){
				listValidator.get(VALIDATE_MAIORES_18_DIURNO).add(ValidatorResult.ERROR, "O aluno com mais de 18 anos não pode ser matriculado em turno diurno", GRUPO_VALIDACAO_INSERT);
				listValidator.get(VALIDATE_MAIORES_18_DIURNO).add(ValidatorResult.ERROR, "O aluno com mais de 18 anos não pode ser matriculado em turno diurno", GRUPO_VALIDACAO_UPDATE);
			}
			
			System.out.println("listValidator = " + listValidator);
			
			//Cria a mensagem de erro em um objeto de retorno chamado 'RESULT'
			result.addListValidator(listValidator);
			result.verify(idGrupo);
			
			//RETORNO
			return result;
		
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaServices.validate: " + e);
			return new ValidatorResult(ValidatorResult.ERROR, "Erro em MatriculaServices.validate");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static HashMap<Integer, Validator> getListValidation(){
		HashMap<Integer, Validator> list = new HashMap<Integer, Validator>();
		
		list.put(VALIDATE_TRANSPORTE_ESCOLAR, new Validator(VALIDATE_TRANSPORTE_ESCOLAR, ValidatorResult.VALID));
		list.put(VALIDATE_TIPOS_TRANSPORTE, new Validator(VALIDATE_TIPOS_TRANSPORTE, ValidatorResult.VALID));
		list.put(VALIDATE_MAX_TIPOS_TRANSPORTE, new Validator(VALIDATE_MAX_TIPOS_TRANSPORTE, ValidatorResult.VALID));
		list.put(VALIDATE_CURSO_MULTI, new Validator(VALIDATE_CURSO_MULTI, ValidatorResult.VALID));
		list.put(VALIDATE_HORARIOS_CONFLITANTES, new Validator(VALIDATE_HORARIOS_CONFLITANTES, ValidatorResult.VALID));
		list.put(VALIDATE_MATRICULA_PERIODO_ANTERIOR, new Validator(VALIDATE_MATRICULA_PERIODO_ANTERIOR, ValidatorResult.VALID));
		list.put(VALIDATE_MATRICULA_CURSO_2_ANOS, new Validator(VALIDATE_MATRICULA_CURSO_2_ANOS, ValidatorResult.VALID));
		list.put(VALIDATE_MATRICULA_CURSO_3_ANOS, new Validator(VALIDATE_MATRICULA_CURSO_3_ANOS, ValidatorResult.VALID));
		list.put(VALIDATE_MATRICULA_CURSO_4_ANOS, new Validator(VALIDATE_MATRICULA_CURSO_4_ANOS, ValidatorResult.VALID));
		list.put(VALIDATE_MATRICULA_CURSO_5_ANOS, new Validator(VALIDATE_MATRICULA_CURSO_5_ANOS, ValidatorResult.VALID));
		list.put(VALIDATE_MATRICULA_EJA, new Validator(VALIDATE_MATRICULA_EJA, ValidatorResult.VALID));
		list.put(VALIDATE_MODALIDADES_INCOMPATIVEIS, new Validator(VALIDATE_MODALIDADES_INCOMPATIVEIS, ValidatorResult.VALID));	
		list.put(VALIDATE_DATA_MATRICULA, new Validator(VALIDATE_DATA_MATRICULA, ValidatorResult.VALID));
		list.put(VALIDATE_TURMA_CHEIA, new Validator(VALIDATE_TURMA_CHEIA, ValidatorResult.VALID));
		list.put(VALIDATE_MAIORES_18_DIURNO, new Validator(VALIDATE_MAIORES_18_DIURNO, ValidatorResult.VALID));
		return list;
	}
	
	public static Result saveCompliance(Matricula objeto, AuthData authData, int tpAcao, Connection connect) {
		try {
						
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_matricula (cd_matricula,"+
                    "cd_matriz,"+
                    "cd_turma,"+
                    "cd_periodo_letivo,"+
                    "dt_matricula,"+
                    "dt_conclusao,"+
                    "st_matricula,"+
                    "tp_matricula,"+
                    "nr_matricula,"+
                    "cd_aluno,"+
                    "cd_matricula_origem,"+
                    "cd_reserva,"+
                    "cd_area_interesse,"+
                    "txt_observacao,"+
                    "txt_boletim,"+
                    "cd_curso,"+
                    "cd_pre_matricula,"+
                    "tp_escolarizacao_outro_espaco,"+
                    "lg_transporte_publico,"+
                    "tp_poder_responsavel,"+
                    "tp_forma_ingresso,"+
                    "txt_documento_oficial,"+
                    "dt_interrupcao,"+
                    "lg_autorizacao_rematricula,"+
                    "lg_atividade_complementar,"+
                    "lg_reprovacao,"+
                    "st_matricula_centaurus,"+
                    "st_aluno_censo,"+
                    "nr_matricula_censo,"+
                    "st_censo_final,"+
                    "nm_ultima_escola,"+
                    "lg_autorizacao,"+
                    "nr_autorizacao,"+
                    "lg_matricula_em_curso,"+
                    "cd_usuario_compliance,"+
                    "dt_compliance,"+
                    "tp_acao_compliance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdMatricula());
			if(objeto.getCdMatriz()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMatriz());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTurma());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPeriodoLetivo());
			if(objeto.getDtMatricula()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtMatricula().getTimeInMillis()));
			if(objeto.getDtConclusao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtConclusao().getTimeInMillis()));
			pstmt.setInt(7,objeto.getStMatricula());
			pstmt.setInt(8,objeto.getTpMatricula());
			pstmt.setString(9,objeto.getNrMatricula());
			if(objeto.getCdAluno()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdAluno());
			if(objeto.getCdMatriculaOrigem()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdMatriculaOrigem());
			if(objeto.getCdReserva()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdReserva());
			if(objeto.getCdAreaInteresse()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdAreaInteresse());
			pstmt.setString(14,objeto.getTxtObservacao());
			pstmt.setString(15,objeto.getTxtBoletim());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdCurso());
			if(objeto.getCdPreMatricula()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdPreMatricula());
			pstmt.setInt(18,objeto.getTpEscolarizacaoOutroEspaco());
			pstmt.setInt(19,objeto.getLgTransportePublico());
			pstmt.setInt(20,objeto.getTpPoderResponsavel());
			pstmt.setInt(21,objeto.getTpFormaIngresso());
			pstmt.setString(22,objeto.getTxtDocumentoOficial());
			if(objeto.getDtInterrupcao()==null)
				pstmt.setNull(23, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(23,new Timestamp(objeto.getDtInterrupcao().getTimeInMillis()));
			pstmt.setInt(24,objeto.getLgAutorizacaoRematricula());
			pstmt.setInt(25,objeto.getLgAtividadeComplementar());
			pstmt.setInt(26,objeto.getLgReprovacao());
			pstmt.setInt(27,objeto.getStMatriculaCentaurus());
			pstmt.setInt(28,objeto.getStAlunoCenso());
			pstmt.setString(29,objeto.getNrMatriculaCenso());
			pstmt.setInt(30,objeto.getStCensoFinal());
			pstmt.setString(31,objeto.getNmUltimaEscola());
			pstmt.setInt(32,objeto.getLgAutorizacao());
			pstmt.setString(33,objeto.getNrAutorizacao());
			pstmt.setInt(34,objeto.getLgMatriculaEmCurso());
			if(authData==null)
				pstmt.setNull(35, Types.INTEGER);
			else
				pstmt.setInt(35, authData.getUsuario().getCdUsuario());
			pstmt.setTimestamp(36, new Timestamp(new GregorianCalendar().getTimeInMillis()));
			pstmt.setInt(37, tpAcao);
			
			int retorno = pstmt.executeUpdate();
						
			return new Result((retorno<0 ? retorno : objeto.getCdMatricula()));
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static ResultSetMap findCompliance(int cdMatricula) {
		return ComplianceManager.find("acd", "matricula", cdMatricula);
	}
	
	public static Result sync(Matricula[] matriculas){
		return sync(matriculas, null);
	}
	
	public static Result sync(Matricula[] matriculas, Connection connect) {
		boolean isConnectionNull = connect==null;
		int totalSync = 0;
		
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			for(Matricula matricula : matriculas){
				Result save = MatriculaServices.save(matricula, connect);
				
				if(save.getCode() >= 0)
					totalSync++;
			}
			
			if(totalSync < matriculas.length)
				connect.rollback();
			
			connect.commit();
			
			return new Result(1, "Sincronização concluída com sucesso!", "totalSync", totalSync);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
}
