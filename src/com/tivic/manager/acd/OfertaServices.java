package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.SetorDAO;
import com.tivic.manager.grl.TipoOcorrenciaServices;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.seg.UsuarioEmpresa;
import com.tivic.manager.seg.UsuarioEmpresaDAO;
import com.tivic.manager.seg.UsuarioModulo;
import com.tivic.manager.seg.UsuarioModuloDAO;
import com.tivic.manager.seg.UsuarioModuloEmpresa;
import com.tivic.manager.seg.UsuarioModuloEmpresaDAO;
import com.tivic.manager.seg.UsuarioPermissaoAcao;
import com.tivic.manager.seg.UsuarioPermissaoAcaoDAO;
import com.tivic.manager.srh.DadosFuncionais;
import com.tivic.manager.srh.DadosFuncionaisDAO;
import com.tivic.manager.srh.FuncaoDAO;
import com.tivic.manager.srh.Lotacao;
import com.tivic.manager.srh.LotacaoDAO;
import com.tivic.manager.srh.LotacaoServices;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;


public class OfertaServices {

	public static final int TP_MATUTINO = 0;
	public static final int TP_VESPERTINO = 1;
	public static final int TP_NOTURNO = 2;
	public static final int TP_DIURNO = 3;
	public static final int TP_INTEGRAL = 4;

	public static final String[] tiposTurno = {"Matutino",
		"Vespertino",
		"Noturno",
		"Diurno",
		"Integral"};

	public static final int ST_OFERTA_ABERTA = 0;
	public static final int ST_OFERTA_FECHADA = 1;
	public static final int ST_OFERTA_PENDENTE = 2;

	public static final String[] situacaoClasse = {"Aberta","Fechada", "Pendente"};

	public static final int FRQ_PADRAO = 0;
	public static final int FRQ_ENTRDA_SAIDA = 1;

	public static final String[] controleFrequencia = {"Padrão",
		"Entrada/saída"};
	
	public static final int ST_ATIVO   = 0;
	public static final int ST_INATIVO = 1;
	
	public static Result save( ArrayList<Oferta> ofertas, boolean lgCadastrarFuncaoAuto ){
		return save( ofertas, lgCadastrarFuncaoAuto, null, null );
	}
	
	public static Result save(ArrayList<Oferta> ofertas, boolean lgCadastrarFuncaoAuto, Connection connection){
		return save( ofertas, lgCadastrarFuncaoAuto, null, connection );
	}
	
	public static Result save( ArrayList<Oferta> ofertas, boolean lgCadastrarFuncaoAuto, String idCargo ){
		return save( ofertas, lgCadastrarFuncaoAuto, idCargo, null );
	}
	
	public static Result save( ArrayList<Oferta> ofertas, boolean lgCadastrarFuncaoAuto, String idCargo, int cdUsuario ){
		return save( ofertas, lgCadastrarFuncaoAuto, idCargo, cdUsuario, null );
	}
	
	public static Result save(ArrayList<Oferta> ofertas, boolean lgCadastrarFuncaoAuto, String idCargo, Connection connection){
		return save( ofertas, lgCadastrarFuncaoAuto, idCargo, 0, connection );
	}
	
	public static Result save(ArrayList<Oferta> ofertas, boolean lgCadastrarFuncaoAuto, String idCargo, int cdUsuario, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			for(Oferta oferta : ofertas){
				Result ret = save(oferta, lgCadastrarFuncaoAuto, idCargo, cdUsuario, connection);
				if(ret.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return ret;
				}
			}
			
			if (isConnectionNull)
				connection.commit();
			
			return new Result(1, "Salvo com sucesso...");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
		
		
	}
	
	public static Result save( Oferta oferta ){
		return save( oferta, false, null, null );
	}
	
	public static Result save(Oferta oferta, Connection connection){
		return save( oferta, false, null, connection );
	}
	
	public static Result save( Oferta oferta, boolean lgCadastrarFuncaoAuto ){
		return save( oferta, lgCadastrarFuncaoAuto, null, null );
	}
	
	public static Result save( Oferta oferta, boolean lgCadastrarFuncaoAuto, String idCargo ){
		return save( oferta, lgCadastrarFuncaoAuto, idCargo, null );
	}
	
	public static Result save( Oferta oferta, boolean lgCadastrarFuncaoAuto, String idCargo, int cdUsuario ){
		return save( oferta, lgCadastrarFuncaoAuto, idCargo, cdUsuario, null );
	}
	
	public static Result save(Oferta oferta, boolean lgCadastrarFuncaoAuto, String idCargo, Connection connection){
		return save( oferta, lgCadastrarFuncaoAuto, idCargo, 0, connection );
	}
	
	public static Result save(Oferta oferta, boolean lgCadastrarFuncaoAuto, String idCargo, int cdUsuario, Connection connection){
		boolean isConnectionNull = connection==null;
		int retorno = 1;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			if(oferta==null)
				return new Result(-1, "Erro ao salvar. Oferta é nula");
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			if(oferta.getCdDisciplina() > 0){
				
				
				Curso curso = CursoDAO.get(oferta.getCdCurso(), connection);
				Disciplina disciplina = DisciplinaDAO.get(oferta.getCdDisciplina(), connection);
				
				
				
				CursoDisciplinaRegra cursoDisciplinaRegra = CursoDisciplinaRegraDAO.get(curso.getCdCurso(), disciplina.getCdDisciplina(), connection);
				if(cursoDisciplinaRegra == null){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Disciplina "+disciplina.getNmProdutoServico()+" não cadastrada na base");
				}
				
				else if(cursoDisciplinaRegra.getTpPermissao() == CursoDisciplinaRegraServices.TP_PROIBIDO){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Para turma da etapa " + curso.getNmProdutoServico() + " não é permitido oferecer a disciplina " + disciplina.getNmProdutoServico());
				}
				
				
				CursoDisciplina cursoDisciplina = CursoDisciplinaDAO.get(oferta.getCdCurso(), oferta.getCdCursoModulo(), oferta.getCdDisciplina(), oferta.getCdMatriz(), connection);
				if(cursoDisciplina == null){
					cursoDisciplina = new CursoDisciplina(oferta.getCdCurso(), oferta.getCdCursoModulo(), oferta.getCdDisciplina(), oferta.getCdMatriz(), 0, 0, 0, 0, 0);
					if(CursoDisciplinaDAO.insert(cursoDisciplina, connection) <= 0){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao salvar curso disciplina");
					}
				}
			}
			
			Turma turma = TurmaDAO.get(oferta.getCdTurma(), connection);
			int cdSetor = 0;
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_empresa", "" + turma.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("nm_setor", "CORPO DOCENTE", ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmSetor = SetorDAO.find(criterios, connection);
			if(rsmSetor.next()){
				cdSetor = rsmSetor.getInt("cd_setor");
			}
			
			int cdPessoa = oferta.getCdProfessor();
			
			int cdFuncao = 0;
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("id_funcao", "" + idCargo, ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmFuncao = FuncaoDAO.find(criterios, connection);
			if(rsmFuncao.next()){
				cdFuncao = rsmFuncao.getInt("cd_funcao");
			}
			
			int cdMatricula = 0;
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_pessoa", "" + cdPessoa, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmDadosFuncionais = DadosFuncionaisDAO.find(criterios, connection);
			if(rsmDadosFuncionais.next()){
				cdMatricula = rsmDadosFuncionais.getInt("cd_matricula");
				if(cdFuncao == 0){
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_matricula", "" + cdMatricula, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_setor", "" + cdSetor, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmLotacao = LotacaoDAO.find(criterios, connection);
					if(rsmLotacao.next()){
						cdFuncao = rsmLotacao.getInt("cd_funcao");
					}
				}
			}
			else{
				DadosFuncionais dadosFuncionais = new DadosFuncionais();
				dadosFuncionais.setCdPessoa(cdPessoa);
				dadosFuncionais.setCdEmpresa(cdSecretaria);
				dadosFuncionais.setCdFuncao(cdFuncao);
				dadosFuncionais.setCdSetor(cdSetor);
				dadosFuncionais.setCdMatricula(DadosFuncionaisDAO.insert(dadosFuncionais, connection));
				if(dadosFuncionais.getCdMatricula() < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao inserir dados funcionais");
				}
				
				cdMatricula = dadosFuncionais.getCdMatricula();
				if(cdFuncao == 0){
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_matricula", "" + cdMatricula, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_setor", "" + cdSetor, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmLotacao = LotacaoDAO.find(criterios, connection);
					if(rsmLotacao.next()){
						cdFuncao = rsmLotacao.getInt("cd_funcao");
					}
				}
				
			}
			
			if(cdFuncao != ParametroServices.getValorOfParametroAsInteger("CD_FUNCAO_PROFESSOR", 0)){
				if( oferta.getCdOferta()==0){
					oferta.setCdProfessor(0);
				}
				else{
					oferta.setCdProfessor(OfertaDAO.get(oferta.getCdOferta(), connection).getCdProfessor());
				}
			}
				
			
			if(cdFuncao == 0 || idCargo != null){
				if(lgCadastrarFuncaoAuto){
					if(cdFuncao == 0){
						PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM acd_curso_multi WHERE cd_curso_multi = ? AND cd_curso = " + oferta.getCdCurso());
						pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_CRECHE", 0));
						ResultSetMap rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
						if(rsmCursoMatricula.next()){
							cdFuncao = 2;//Auxiliar
						}
						else if(oferta.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)){
							cdFuncao = 3;//Monitor
						}
						else{
							cdFuncao = ParametroServices.getValorOfParametroAsInteger("CD_FUNCAO_PROFESSOR", 0);
						}
						
					}
						
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_matricula", "" + cdMatricula, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_setor", "" + cdSetor, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmLotacao = LotacaoDAO.find(criterios, connection);
					if(!rsmLotacao.next()){
						if(LotacaoDAO.insert(new Lotacao(0, cdMatricula, cdSetor, cdFuncao, null, null, 0, null), connection) <= 0){
							if(isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao cadastrar lotação");
						}
					}
				}
				else{
					if(isConnectionNull)
						Conexao.rollback(connection);
					
					String cargo = "";
					idCargo = "";
					
					PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM acd_curso_multi WHERE cd_curso_multi = ? AND cd_curso = " + oferta.getCdCurso());
					pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_CRECHE", 0));
					ResultSetMap rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
					if(rsmCursoMatricula.next()){
						cargo = "Auxiliar";
						idCargo = ProfessorServices.TP_FUNCAO_AUXILIAR;
					}
					else if(oferta.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)){
						cargo = "Monitor";
						idCargo = ProfessorServices.TP_FUNCAO_MONITOR;
					}
					else{
						cargo = "Professor";
						idCargo = ProfessorServices.TP_FUNCAO_DOCENTE;
					}
					
					return new Result(-11, cargo, "IDCARGO", idCargo);
				}
			}
			
			oferta.setStOferta(ST_ATIVO);
			oferta.setVlDisciplina(0);//Fixo para correção de problemas de NaN
			
			if( oferta.getCdOferta()==0 ){
				
				
				//Criação do Plano de Curso
				Plano plano = new Plano(0/*cdPlano*/, "PLANO DE CURSO", PlanoServices.CURSO, turma.getCdInstituicao(), oferta.getCdPeriodoLetivo(), oferta.getCdCurso(), oferta.getCdTurma(), oferta.getCdDisciplina(), oferta.getCdProfessor(), PlanoServices.PENDENTE, 0, 200);
				int ret = PlanoDAO.insert(plano, connection);
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao inserir plano de curso");
				}
				
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_periodo_letivo", "" + oferta.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("st_oferta", "" + ST_ATIVO, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_curso", "" + oferta.getCdCurso(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_turma", "" + oferta.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_matriz", "" + oferta.getCdMatriz(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_curso_modulo", "" + oferta.getCdCursoModulo(), ItemComparator.EQUAL, Types.INTEGER));
				if(oferta.getCdDisciplina() > 0)
					criterios.add(new ItemComparator("cd_disciplina", "" + oferta.getCdDisciplina(), ItemComparator.EQUAL, Types.INTEGER));
				else{
					criterios.add(new ItemComparator("cd_disciplina", "", ItemComparator.ISNULL, Types.INTEGER));
				}
				ResultSetMap rsmOfertaExistente = OfertaDAO.find(criterios, connection);
				if(rsmOfertaExistente.next()){
					PessoaOferta pessoaOferta = PessoaOfertaDAO.get(cdPessoa, rsmOfertaExistente.getInt("cd_oferta"), cdFuncao, connection);
					if(pessoaOferta==null){
						retorno = PessoaOfertaDAO.insert(new PessoaOferta(cdPessoa, rsmOfertaExistente.getInt("cd_oferta"), cdFuncao, PessoaOfertaServices.ST_ATIVO), connection);
					}
					else{
						if(pessoaOferta.getStPessoaOferta() == PessoaOfertaServices.ST_INATIVO){
							pessoaOferta.setStPessoaOferta(PessoaOfertaServices.ST_ATIVO);
							if(PessoaOfertaDAO.update(pessoaOferta, connection) < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return new Result(-1, "Erro ao atualizar pessoa oferta");
							}
						}
						else{
							if(isConnectionNull)
								Conexao.rollback(connection);
							
							Oferta ofertaExistente = OfertaDAO.get(pessoaOferta.getCdOferta(), connection);
							Disciplina disciplina = DisciplinaDAO.get(ofertaExistente.getCdDisciplina(), connection); 
							
							return new Result(-1, disciplina.getNmProdutoServico() + " já cadastrada para esse professor na turma indicada");
						}
					}
				}
				else{
					retorno = OfertaDAO.insert(oferta, connection);
					oferta.setCdOferta(retorno);
					retorno = PessoaOfertaDAO.insert(new PessoaOferta(cdPessoa, oferta.getCdOferta(), cdFuncao, PessoaOfertaServices.ST_ATIVO), connection);
				}
			}else{
				retorno = OfertaDAO.update(oferta, connection);
			}
			
			
			ResultSetMap rsmAlunosMatricula = TurmaServices.getAlunos(turma.getCdTurma(), true, connection);
			while(rsmAlunosMatricula.next()){
				ArrayList<ItemComparator> criteriosMatriculaDiciplina = new ArrayList<ItemComparator>();
				criteriosMatriculaDiciplina.add(new ItemComparator("A.cd_matricula", rsmAlunosMatricula.getString("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
				criteriosMatriculaDiciplina.add(new ItemComparator("A.cd_periodo_letivo", "" + turma.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
				criteriosMatriculaDiciplina.add(new ItemComparator("A.cd_curso", "" + rsmAlunosMatricula.getString("cd_curso"), ItemComparator.EQUAL, Types.INTEGER));
				if(oferta.getCdDisciplina() > 0)
					criteriosMatriculaDiciplina.add(new ItemComparator("A.cd_disciplina", "" + oferta.getCdDisciplina(), ItemComparator.EQUAL, Types.INTEGER));
				criteriosMatriculaDiciplina.add(new ItemComparator("A.cd_oferta", "" + oferta.getCdOferta(), ItemComparator.EQUAL, Types.INTEGER));
				//Validação necessária para enturmações de cursos sem disciplina (creche e educação infantil)
				if(MatriculaDisciplinaServices.find(criteriosMatriculaDiciplina, connection).size() == 0){
					
					ArrayList<ItemComparator> criteriosMatriculaPeriodoLetivo = new ArrayList<ItemComparator>();
					criteriosMatriculaPeriodoLetivo.add(new ItemComparator("cd_matricula", rsmAlunosMatricula.getString("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
					criteriosMatriculaPeriodoLetivo.add(new ItemComparator("cd_periodo_letivo", "" + turma.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
					if(MatriculaPeriodoLetivoServices.find(criteriosMatriculaPeriodoLetivo, connection).size() == 0){
						MatriculaPeriodoLetivo matriculaPeriodoLetivo = new MatriculaPeriodoLetivo(rsmAlunosMatricula.getInt("cd_matricula"), turma.getCdPeriodoLetivo(), 0/*cdContrato*/, 
																								   null/*dtMatricula*/, 0/*stMatricula*/, 0/*cdMotivoTrancamento*/, 0/*cdPedidoVenda*/, 0/*cdRota*/);
						
						Result resultadoMatriculaPeriodoLetivo = MatriculaPeriodoLetivoServices.save(matriculaPeriodoLetivo, connection);
						if(resultadoMatriculaPeriodoLetivo.getCode() < 0){
							if(isConnectionNull)
								Conexao.rollback(connection);
							return resultadoMatriculaPeriodoLetivo;
						}
					}
					
					MatriculaDisciplina matriculaDisciplina = new MatriculaDisciplina(0, rsmAlunosMatricula.getInt("cd_matricula"), turma.getCdPeriodoLetivo(), 
																					0/*cdConceito*/, Util.getDataAtual(), null/*dtConclusao*/, 0/*nrFaltas*/, MatriculaDisciplinaServices.TP_NORMAL, 
																					0/*vlConceito*/, 0/*qtChComplemento*/, 0/*vlConceitoAproveitamento*/, null/*nmInstitiuicaoAproveitamento*/, 
																					MatriculaDisciplinaServices.ST_EM_CURSO, oferta.getCdProfessor(), 0/*cdSupervisorPratica*/, 0/*cdInstituicaoPratica*/, 
																					oferta.getCdMatriz(), oferta.getCdCurso(), oferta.getCdCursoModulo(), oferta.getCdDisciplina(), oferta.getCdOferta(), 0);
					Result resultadoMatriculaDisciplina = MatriculaDisciplinaServices.save(matriculaDisciplina, connection);
					if(resultadoMatriculaDisciplina.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return resultadoMatriculaDisciplina;
					}
				}
			}
			rsmAlunosMatricula.beforeFirst();
			
			
			ResultSetMap rsmModulos = new ResultSetMap(connection.prepareStatement("SELECT * FROM seg_modulo WHERE id_modulo IN ('ped')").executeQuery());
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_pessoa", "" + cdPessoa, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmUsuario = UsuarioDAO.find(criterios, connection);
			if(rsmUsuario.next()){
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_empresa", "" + turma.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_usuario", rsmUsuario.getString("cd_usuario"), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmUsuarioEmpresa = UsuarioEmpresaDAO.find(criterios, connection);
				UsuarioEmpresa usuarioEmpresa = null;
				if(!rsmUsuarioEmpresa.next()){
					
					usuarioEmpresa = new UsuarioEmpresa(turma.getCdInstituicao(), rsmUsuario.getInt("cd_usuario"), 0, null, null);
					if(UsuarioEmpresaDAO.insert(usuarioEmpresa, connection) < 0){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao cadastrar usuário empresa");
					}
				}
				else{
					usuarioEmpresa = UsuarioEmpresaDAO.get(turma.getCdInstituicao(), cdUsuario, rsmUsuarioEmpresa.getInt("nrHorario"), connection);
				}
				
				while(rsmModulos.next()){
					
					if(UsuarioModuloDAO.get(rsmUsuario.getInt("cd_usuario"), rsmModulos.getInt("cd_modulo"), rsmModulos.getInt("cd_sistema"), connection) == null){
						UsuarioModulo usuarioModulo = new UsuarioModulo(rsmUsuario.getInt("cd_usuario"), rsmModulos.getInt("cd_modulo"), rsmModulos.getInt("cd_sistema"), 1);
						if(UsuarioModuloDAO.insert(usuarioModulo, connection) < 0){
							if(isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao cadastrar usuário modulo");
						}
					}
					
					UsuarioModuloEmpresa usuarioModuloEmpresa = new UsuarioModuloEmpresa(rsmUsuario.getInt("cd_usuario"), turma.getCdInstituicao(), rsmModulos.getInt("cd_modulo"), rsmModulos.getInt("cd_sistema"));
					if(UsuarioModuloEmpresaDAO.get(rsmUsuario.getInt("cd_usuario"), turma.getCdInstituicao(), rsmModulos.getInt("cd_modulo"), rsmModulos.getInt("cd_sistema"), connection) == null)
						if(UsuarioModuloEmpresaDAO.insert(usuarioModuloEmpresa, connection) < 0){
							if(isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao cadastrar usuário modulo empresa");
						}
					
					ResultSetMap rsmAcoes = new ResultSetMap(connection.prepareStatement("SELECT * FROM seg_acao WHERE cd_modulo = " + rsmModulos.getString("cd_modulo")).executeQuery());
					while(rsmAcoes.next()){
						if(UsuarioPermissaoAcaoDAO.get(rsmUsuario.getInt("cd_usuario"), rsmAcoes.getInt("cd_acao"), rsmModulos.getInt("cd_modulo"), rsmModulos.getInt("cd_sistema"), connection) == null){
							UsuarioPermissaoAcao usuarioPermissaoAcao = new UsuarioPermissaoAcao(rsmUsuario.getInt("cd_usuario"), rsmAcoes.getInt("cd_acao"), rsmModulos.getInt("cd_modulo"), rsmModulos.getInt("cd_sistema"), 1);
							if(UsuarioPermissaoAcaoDAO.insert(usuarioPermissaoAcao, connection) < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return new Result(-1, "Erro ao cadastrar usuário permissao acao");
							}
						}
					}
					
				}
				rsmModulos.beforeFirst();
				
				
			}
		
			
			int cdTipoOcorrenciaCadastroPessoaOferta = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_PESSOA_OFERTA, connection).getCdTipoOcorrencia();
			OcorrenciaPessoaOferta ocorrenciaPessoaOferta = null;
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_oferta", "" + oferta.getCdOferta(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_pessoa", "" + cdPessoa, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmOcorrenciaPessoaOferta = OcorrenciaPessoaOfertaDAO.find(criterios, connection);
			if(rsmOcorrenciaPessoaOferta.next()){
				ocorrenciaPessoaOferta = OcorrenciaPessoaOfertaDAO.get(rsmOcorrenciaPessoaOferta.getInt("cd_ocorrencia"), connection);
			}
			
			
			if(ocorrenciaPessoaOferta == null){
				ocorrenciaPessoaOferta = new OcorrenciaPessoaOferta(0, cdPessoa, "Oferta cadastrada", Util.getDataAtual(), cdTipoOcorrenciaCadastroPessoaOferta, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, oferta.getCdOferta(), cdFuncao, Util.getDataAtual(), cdUsuario, oferta.getStOferta(), oferta.getStOferta());
				OcorrenciaPessoaOfertaServices.save(ocorrenciaPessoaOferta, connection);
			}
			else{
				ocorrenciaPessoaOferta.setDtUltimaModificacao(Util.getDataAtual());
				ocorrenciaPessoaOferta.setCdUsuarioModificador(cdUsuario);
				ocorrenciaPessoaOferta.setStPessoaOfertaPosterior(PessoaOfertaServices.ST_ATIVO);
				OcorrenciaPessoaOfertaServices.save(ocorrenciaPessoaOferta, connection);
			}
			
			int cdTipoOcorrenciaCadastroOferta = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_OFERTA, connection).getCdTipoOcorrencia();
			OcorrenciaOferta ocorrencia = null;
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_oferta", "" + oferta.getCdOferta(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmOcorrenciaOferta = OcorrenciaOfertaDAO.find(criterios, connection);
			if(rsmOcorrenciaOferta.next()){
				ocorrencia = OcorrenciaOfertaDAO.get(rsmOcorrenciaOferta.getInt("cd_ocorrencia"), connection);
			}
			
			
			if(ocorrencia == null){
				ocorrencia = new OcorrenciaOferta(0, 0, "Oferta cadastrada", Util.getDataAtual(), cdTipoOcorrenciaCadastroOferta, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, oferta.getCdOferta(), Util.getDataAtual(), cdUsuario, oferta.getStOferta(), oferta.getStOferta());
				OcorrenciaOfertaServices.save(ocorrencia, connection);
			}
			else{
				ocorrencia.setDtUltimaModificacao(Util.getDataAtual());
				ocorrencia.setCdUsuarioModificador(cdUsuario);
				ocorrencia.setStOfertaPosterior(oferta.getStOferta());
				OcorrenciaOfertaServices.save(ocorrencia, connection);
			}
			
			if(retorno<=0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "OFERTA", oferta);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result gerarOfertas( ArrayList<Oferta> ofertas ){
		return gerarOfertas( ofertas, null );
	}
	
	public static Result gerarOfertas( ArrayList<Oferta> ofertas, Connection connection ){
		boolean isConnectionNull = connection==null;
		Result retorno  = new Result(-1,"","", null );
		Result retornoAvaliacoes  = new Result(-1,"","", null );
		
		try{
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			for (Oferta oferta : ofertas) {
				
				ResultSetMap rsmTurmas = CursoServices.getAllTurmas(oferta.getCdCurso(), connection);
				while(rsmTurmas.next()){
					Turma turma = TurmaDAO.get(rsmTurmas.getInt("cd_turma"), connection);
					Oferta ofertaNova = (Oferta)oferta.clone();
					ofertaNova.setCdTurma(turma.getCdTurma());
				
					retorno = save( ofertaNova, connection );
					
					retornoAvaliacoes = OfertaAvaliacaoServices.gerarOfertaAvaliacao( ofertaNova, connection );
					
					if( retorno.getCode()<=0 || retornoAvaliacoes.getCode()<=0 ){
						Conexao.rollback(connection);
						return new Result( retorno.getCode(), "Erro ao salvar...", "OFERTAS", ofertas );
					}
				}
				
			}
			
			if(isConnectionNull)
				connection.commit();
			
			return new Result( 1, "Salvo com sucesso...", "OFERTAS", ofertas );
		}catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, e.getMessage());
		}finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result remove(int cdOferta){
		return remove(cdOferta, false, null);
	}
	
	public static Result remove(int cdOferta, boolean cascade){
		return remove(cdOferta, cascade, null);
	}
	
	public static Result remove(int cdOferta, boolean cascade, Connection connect){
		return remove(cdOferta, 0, cascade, connect);
	}
	
	public static Result remove(int cdOferta, int cdPessoa){
		return remove(cdOferta, cdPessoa, false, null);
	}
	
	public static Result remove(int cdOferta, int cdPessoa, boolean cascade){
		return remove(cdOferta, cdPessoa, cascade, null);
	}
	
	public static Result remove(int cdOferta, int cdPessoa, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 1;
			
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = connect.prepareStatement(" DELETE FROM acd_matricula_disciplina where cd_oferta = "+cdOferta).executeUpdate();
				retorno = connect.prepareStatement(" DELETE FROM acd_oferta_horario where cd_oferta = "+cdOferta).executeUpdate();
				
			}
			
			if(cdPessoa > 0)
				retorno = connect.prepareStatement(" DELETE FROM acd_pessoa_oferta where cd_oferta = "+cdOferta+ " AND cd_pessoa = " + cdPessoa).executeUpdate();
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_oferta", "" + cdOferta, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmPessoaOferta = PessoaOfertaDAO.find(criterios, connect);
			if(!rsmPessoaOferta.next()){
				if(!cascade || retorno>=0)
					retorno = OfertaDAO.delete(cdOferta, connect);
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta Oferta está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Oferta excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir oferta!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	public static Result inativar(ArrayList<Integer> codigosOferta, int cdPessoa, int cdUsuario){
		return inativar(codigosOferta, cdPessoa, cdUsuario, null);
	}
	
	public static Result inativar(ArrayList<Integer> codigosOferta, int cdPessoa, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
	
			for(int cdOferta : codigosOferta){
				Result result = inativar(cdOferta, cdPessoa, cdUsuario, connect);
				if(result.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
				
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Oferta excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir oferta!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
			
	public static Result inativar(int cdOferta, int cdPessoa, int cdUsuario){
		return inativar(cdOferta, cdPessoa, cdUsuario, null);
	}
	
	public static Result inativar(int cdOferta, int cdPessoa, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 1;
			
			Pessoa pessoa = PessoaDAO.get(cdPessoa, connect);
			
			PessoaOferta pessoaOferta = null;
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_oferta", "" + cdOferta, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_pessoa", "" + cdPessoa, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmPessoaOferta = PessoaOfertaServices.find(criterios, connect);
			if(rsmPessoaOferta.next()){
				pessoaOferta = PessoaOfertaDAO.get(cdPessoa, cdOferta, rsmPessoaOferta.getInt("cd_funcao"), connect);
			}
			
			int stPessoaOferta = pessoaOferta.getStPessoaOferta();
			pessoaOferta.setStPessoaOferta(PessoaOfertaServices.ST_INATIVO);
			retorno = PessoaOfertaDAO.update(pessoaOferta, connect);
			
			int cdTipoOcorrencia = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_PESSOA_OFERTA, connect).getCdTipoOcorrencia();
			
			OcorrenciaPessoaOferta ocorrenciaPessoaOferta = null;
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_oferta", "" + pessoaOferta.getCdOferta(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_pessoa", "" + pessoaOferta.getCdPessoa(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_funcao", "" + pessoaOferta.getCdFuncao(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("B.cd_tipo_ocorrencia", "" + cdTipoOcorrencia, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmOcorrenciaPessoaOferta = OcorrenciaPessoaOfertaServices.find(criterios, connect);
			if(rsmOcorrenciaPessoaOferta.next()){
				ocorrenciaPessoaOferta = OcorrenciaPessoaOfertaDAO.get(rsmOcorrenciaPessoaOferta.getInt("cd_ocorrencia"), connect);
				ocorrenciaPessoaOferta.setDtUltimaModificacao(Util.getDataAtual());
				ocorrenciaPessoaOferta.setCdUsuarioModificador(cdUsuario);
				ocorrenciaPessoaOferta.setStPessoaOfertaPosterior(pessoaOferta.getStPessoaOferta());
				OcorrenciaPessoaOfertaServices.save(ocorrenciaPessoaOferta, connect);
			}
			else{
				ocorrenciaPessoaOferta = new OcorrenciaPessoaOferta(0/*cdOcorrencia*/, cdPessoa, "Pessoa Oferta " + pessoa.getNmPessoa() + " atualizada" , Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 1, cdUsuario, cdOferta, pessoaOferta.getCdFuncao(), Util.getDataAtual(), cdUsuario, stPessoaOferta, pessoaOferta.getStPessoaOferta());
				if(OcorrenciaPessoaOfertaDAO.insert(ocorrenciaPessoaOferta, connect) <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao insertir ocorrencia pessoa oferta");
				}
			}
			
			cdTipoOcorrencia = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_OFERTA, connect).getCdTipoOcorrencia();
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_oferta", "" + cdOferta, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("st_pessoa_oferta", "" + PessoaOfertaServices.ST_ATIVO, ItemComparator.EQUAL, Types.INTEGER));
			rsmPessoaOferta = PessoaOfertaServices.find(criterios, connect);
			if(!rsmPessoaOferta.next()){
				Oferta oferta = OfertaDAO.get(cdOferta, connect);
				int stOferta = oferta.getStOferta();
				oferta.setStOferta(ST_INATIVO);
				
				retorno = OfertaDAO.update(oferta, connect);
				
				
				int ret = connect.prepareStatement("DELETE FROM acd_matricula_disciplina WHERE cd_oferta = " + cdOferta).executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao remover matricula disciplina");
				}
				
				
				OcorrenciaOferta ocorrencia = null;
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_oferta", "" + oferta.getCdOferta(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + cdTipoOcorrencia, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmOcorrenciaOferta = OcorrenciaOfertaServices.find(criterios, connect);
				if(rsmOcorrenciaOferta.next()){
					ocorrencia = OcorrenciaOfertaDAO.get(rsmOcorrenciaOferta.getInt("cd_ocorrencia"), connect);
					ocorrencia.setDtUltimaModificacao(Util.getDataAtual());
					ocorrencia.setCdUsuarioModificador(cdUsuario);
					ocorrencia.setStOfertaPosterior(oferta.getStOferta());
					OcorrenciaOfertaServices.save(ocorrencia, connect);
				}
				else{
					ocorrencia = new OcorrenciaOferta(0/*cdOcorrencia*/, cdPessoa, "Oferta atualizada" , Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 1, cdUsuario, cdOferta, Util.getDataAtual(), cdUsuario, stOferta, oferta.getStOferta());
					if(OcorrenciaOfertaDAO.insert(ocorrencia, connect) <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao insertir ocorrencia oferta");
					}
				}
				
				
				ArrayList<ItemComparator> criteriosPlano = new ArrayList<ItemComparator>();
				criteriosPlano.add(new ItemComparator("cd_turma", "" + oferta.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
				criteriosPlano.add(new ItemComparator("cd_disciplina", "" + oferta.getCdDisciplina(), ItemComparator.EQUAL, Types.INTEGER));
				criteriosPlano.add(new ItemComparator("cd_curso", "" + oferta.getCdCurso(), ItemComparator.EQUAL, Types.INTEGER));
				criteriosPlano.add(new ItemComparator("cd_periodo_letivo", "" + oferta.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPlano = PlanoDAO.find(criteriosPlano, connect);
				while(rsmPlano.next()){
					Plano plano = PlanoDAO.get(rsmPlano.getInt("cd_plano"), connect);
					plano.setStPlano(PlanoServices.INATIVO);
					if(PlanoDAO.update(plano, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar plano");
					}
				}
			}
			
			
			
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_pessoa", "" + cdPessoa, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("nm_setor", "CORPO DOCENTE", ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmLotacao = LotacaoServices.find(criterios, connect);
			while(rsmLotacao.next()){
			
				boolean possuiVinculo = false;
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", "" + cdPessoa, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("st_pessoa_oferta", "" + PessoaOfertaServices.ST_ATIVO, ItemComparator.EQUAL, Types.INTEGER));
				rsmPessoaOferta = PessoaOfertaDAO.find(criterios, connect);
				while(rsmPessoaOferta.next()){
					Oferta oferta = OfertaDAO.get(rsmPessoaOferta.getInt("cd_oferta"), connect);
					InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(oferta.getCdPeriodoLetivo(), connect);
					if(oferta != null && oferta.getStOferta() == OfertaServices.ST_ATIVO && instituicaoPeriodo.getStPeriodoLetivo() == InstituicaoPeriodoServices.ST_ATUAL){
						Turma turma = TurmaDAO.get(oferta.getCdTurma(), connect);
						if(turma.getCdInstituicao() == rsmLotacao.getInt("cd_empresa")){
							possuiVinculo = true;
						}
					}
					
				}
				
				if(!possuiVinculo){
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_pessoa", "" + cdPessoa, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmUsuario = UsuarioDAO.find(criterios, connect);
					Usuario usuarioProfessor = null;
					while(rsmUsuario.next()){
						usuarioProfessor = UsuarioDAO.get(rsmUsuario.getInt("cd_usuario"), connect);
						
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_usuario", "" + usuarioProfessor.getCdUsuario(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_modulo", "25", ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmUsuarioModuloEmpresa = UsuarioModuloEmpresaDAO.find(criterios, connect);
						while(rsmUsuarioModuloEmpresa.next()){
						
							if(UsuarioModuloEmpresaDAO.delete(usuarioProfessor.getCdUsuario(), rsmUsuarioModuloEmpresa.getInt("cd_empresa"), rsmUsuarioModuloEmpresa.getInt("cd_modulo"), 1, connect) < 0){
								if(isConnectionNull){
									Conexao.rollback(connect);
								}
								return new Result(-1, "Erro ao deletar usuario modulo empresa");
							}
						}
					}
					
					
					if(LotacaoDAO.delete(rsmLotacao.getInt("cd_lotacao"), rsmLotacao.getInt("cd_matricula"), connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao excluir lotação");
					}
					
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_professor", "" + cdPessoa, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmHorarios = ProfessorHorarioDAO.find(criterios, connect);
					while(rsmHorarios.next()){
						if(ProfessorHorarioDAO.delete(rsmHorarios.getInt("cd_horario"), cdPessoa, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao excluir horários de professor");
						}
					}
					
				}
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta Oferta está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Oferta excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir oferta!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findPeriodoAtual(int cdProfessor, int cdInstituicao){
		int cdPeriodoLetivoAtual = 0;
		ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao);
		if(rsmPeriodoAtual.next()){
			cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
		}
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("G.cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.cd_periodo_letivo", "" + cdPeriodoLetivoAtual, ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("cdProfessor", "" + cdProfessor, ItemComparator.EQUAL, Types.INTEGER));
		
		return find(criterios);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		
		int cdProfessor = 0;
		
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("cdProfessor"))
				cdProfessor = Integer.parseInt(criterios.get(i).getValue());
			else
				crt.add(criterios.get(i));
			
		}
		
		ResultSetMap rsm = Search.find(
				"SELECT A.*, A3.*, B.nm_produto_servico AS nm_curso, B2.*, B3.nm_periodo_letivo, C.nm_produto_servico AS nm_modulo, " +
						"       C2.*, D.nm_matriz, E.qt_carga_horaria, F.nm_produto_servico AS nm_disciplina, G.*, " +
						"       J.nm_pessoa AS nm_professor, L.nm_pessoa AS nm_instituicao_pratica, M.nm_pessoa AS nm_supervisor_pratica, N.nm_pessoa as nm_instituicao, " +
						"      (SELECT COUNT(*) FROM acd_matricula_disciplina WHERE cd_oferta = A.cd_oferta) AS qt_matriculados " +
						"FROM acd_oferta A " +
						"JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico) " +
						"JOIN acd_instituicao_periodo B3 ON (A.cd_periodo_letivo = B3.cd_periodo_letivo) " +
						"JOIN acd_instituicao_curso  A2 ON (A.cd_curso = A2.cd_curso AND B3.cd_periodo_letivo = A2.cd_periodo_letivo) " +
						"JOIN acd_instituicao A3 ON (A2.cd_instituicao = A3.cd_instituicao) " +
						"JOIN acd_curso_modulo  B2 ON (A.cd_curso_modulo = B2.cd_curso_modulo) " +
						"JOIN grl_produto_servico  C ON (A.cd_curso_modulo = C.cd_produto_servico) " +
						"LEFT OUTER JOIN acd_instituicao_dependencia C2 ON (A.cd_dependencia = C2.cd_dependencia AND B3.cd_periodo_letivo = C2.cd_periodo_letivo) " +
						"JOIN acd_curso_matriz     D ON (A.cd_matriz      = D.cd_matriz " +
						"							 AND A.cd_curso       = D.cd_curso) " +
						"LEFT OUTER JOIN acd_curso_disciplina E ON (A.cd_curso         = E.cd_curso " +
						"                            AND A.cd_matriz        = E.cd_matriz " +
						"                            AND A.cd_curso_modulo = E.cd_curso_modulo " +
						"                            AND A.cd_disciplina    = E.cd_disciplina) " +
						"LEFT OUTER JOIN grl_produto_servico  F ON (A.cd_disciplina = F.cd_produto_servico) " +
						"JOIN acd_turma            G ON (A.cd_turma = G.cd_turma) " +
						"LEFT OUTER JOIN grl_pessoa J ON (A.cd_professor = J.cd_pessoa) " +
						"LEFT OUTER JOIN grl_pessoa L ON (A.cd_instituicao_pratica = L.cd_pessoa) " +
						"LEFT OUTER JOIN grl_pessoa M ON (A.cd_supervisor_pratica = M.cd_pessoa) "+
						"LEFT OUTER JOIN grl_pessoa N ON (A3.cd_instituicao = N.cd_pessoa) "+
						"WHERE 1=1 "+
						(cdProfessor > 0 ? " AND EXISTS(SELECT * FROM acd_pessoa_oferta PO WHERE PO.cd_oferta = A.cd_oferta AND PO.cd_pessoa = " + cdProfessor + ")" : ""), "",
						crt, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		return rsm;
		
	}
	
	public static ResultSetMap getAllHorarios(int cdOferta, int cdPeriodoLetivo) {
		return getAllHorarios(cdOferta, cdPeriodoLetivo, null);
	}

	public static ResultSetMap getAllHorarios(int cdOferta, int cdPeriodoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.* " +
					"FROM acd_oferta_horario A " + 
					" JOIN acd_oferta B ON (A.cd_oferta = B.cd_oferta) " +
					"WHERE A.cd_oferta = ? AND B.cd_periodo_letivo = ? ");
			pstmt.setInt(1, cdOferta);
			pstmt.setInt(2, cdPeriodoLetivo);
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

	public static ResultSetMap getAlunosByOferta(int cdOferta)	{
		Connection connect = Conexao.conectar();
		ResultSetMap rsmAlunos;
		try	{
			
			Oferta oferta = OfertaDAO.get(cdOferta, connect);
			
			rsmAlunos = new ResultSetMap(connect.prepareStatement("SELECT A.*, C.nm_pessoa AS nm_aluno, D.* " +
															 "FROM acd_matricula A " +
															 "JOIN acd_matricula_periodo_letivo B ON (A.cd_matricula = B.cd_matricula) " +
															 "JOIN grl_pessoa               C ON (A.cd_aluno = C.cd_pessoa) " +
															 "LEFT OUTER JOIN acd_turma     D ON (A.cd_turma = D.cd_turma) " +
															 "LEFT OUTER JOIN acd_oferta E ON ( E.cd_turma = D.cd_turma ) " +
															 "WHERE E.cd_oferta = "+cdOferta+
															 "  AND A.st_matricula = " + MatriculaServices.ST_ATIVA + 
															 " ORDER BY C.nm_pessoa").executeQuery());
			//System.out.println("rsmAlunos = " + rsmAlunos);
			while( rsmAlunos.next() ){
				
				ResultSetMap rsmAvaliacaoes = DisciplinaAvaliacaoAlunoServices.getAllByMatricula( rsmAlunos.getInt("cd_matricula") , connect);
				rsmAlunos.setValueToField("AVALIACOES", rsmAvaliacaoes);
				ResultSetMap rsmAulasMatricula = MatriculaDisciplinaServices.getAllByMatricula( rsmAlunos.getInt("cd_matricula") , connect); 
				rsmAlunos.setValueToField("MATRICULA_DISCIPLINAS", rsmAulasMatricula);
				
				ResultSetMap rsmAulaMatriculaFaltas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_aula_matricula A, acd_matricula_disciplina B, acd_aula C "+ 
										"	WHERE A.cd_matricula_disciplina = B.cd_matricula_disciplina"+
										"     AND A.cd_aula = C.cd_aula " +
										"     AND B.cd_matricula = " + rsmAlunos.getInt("cd_matricula") +
										"  	  AND B.cd_oferta = "+cdOferta+
										" 	  AND lg_presenca = 0"+
										"	  AND C.st_aula = " + AulaServices.ST_FECHADA).executeQuery());
				
				rsmAlunos.setValueToField("NR_FALTAS", rsmAulaMatriculaFaltas.size());
//				
//				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
//				criterios.add(new ItemComparator("cd_matricula", rsmAlunos.getString("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
//				criterios.add(new ItemComparator("cd_disciplina", "" + oferta.getCdDisciplina(), ItemComparator.EQUAL, Types.INTEGER));
//				MatriculaDisciplina matriculaDisciplina = null;
//				ResultSetMap rsmMatriculaDisciplina = MatriculaDisciplinaDAO.find(criterios, connect);
//				if(rsmMatriculaDisciplina.next()){
//					matriculaDisciplina = MatriculaDisciplinaDAO.get(rsmMatriculaDisciplina.getInt("cd_matricula_disciplina"), connect);
//				}
//				
//				ResultSetMap rsmRendimentoAluno = new ResultSetMap(connect.prepareStatement("SELECT A.vl_conceito, B.vl_peso FROM acd_disciplina_avaliacao_aluno A, acd_oferta_avaliacao B "+ 
//						"	WHERE A.cd_oferta_avaliacao = B.cd_oferta_avaliacao"+
//						"     AND A.cd_oferta = B.cd_oferta" +
//						"     AND A.cd_oferta = " + cdOferta+
//						"     AND A.cd_matricula_disciplina = " + matriculaDisciplina.getCdMatriculaDisciplina()+
//						"	  AND B.st_oferta_avaliacao = " + OfertaAvaliacaoServices.ST_OFERTA_AVALIACAO_AVALIADA).executeQuery());
//				float vlTotalPeso = 0;
//				float vlTotalConceito = 0;
//				while(rsmRendimentoAluno.next()){
//					vlTotalPeso += rsmRendimentoAluno.getFloat("vl_peso");
//					vlTotalConceito += rsmRendimentoAluno.getFloat("vl_conceito");
//				}
//				rsmRendimentoAluno.beforeFirst();
				
				float prNotasMatricula = DisciplinaAvaliacaoAlunoServices.getMediaNotaByTurma(rsmAlunos.getInt("cd_matricula"), rsmAlunos.getInt("cd_turma"), connect);
				
				float prFrequenciaMatricula = AulaMatriculaServices.getMediaFrequenciaByTurma(rsmAlunos.getInt("cd_matricula"), rsmAlunos.getInt("cd_turma"), connect);
				
				rsmAlunos.setValueToField("PR_NOTAS", prNotasMatricula);
				rsmAlunos.setValueToField("CL_NOTAS", Util.formatNumber((rsmAlunos.getFloat("PR_NOTAS")*100), 2) + "%");
				
				rsmAlunos.setValueToField("PR_FREQUENCIA", prFrequenciaMatricula);
				rsmAlunos.setValueToField("CL_FREQUENCIA", Util.formatNumber((rsmAlunos.getFloat("PR_FREQUENCIA")*100), 2) + "%");
				
				rsmAlunos.setValueToField("PR_RENDIMENTO", (rsmAlunos.getFloat("PR_NOTAS")*rsmAlunos.getFloat("PR_FREQUENCIA")));
				rsmAlunos.setValueToField("CL_RENDIMENTO", Util.formatNumber((rsmAlunos.getFloat("PR_RENDIMENTO")*100), 2) + "%");

//				rsmAlunos.setValueToField("CL_RENDIMENTO", Util.formatNumber((vlTotalConceito*100/(vlTotalPeso==0?1:vlTotalPeso)), 2) + "%");
//				rsmAlunos.setValueToField("VL_RENDIMENTO", (vlTotalConceito*100/(vlTotalPeso==0?1:vlTotalPeso)));
//				rsmAlunos.setValueToField("VL_TOTAL_CONCEITO", vlTotalConceito);
//				rsmAlunos.setValueToField("VL_TOTAL_PESO", vlTotalPeso);
			}
			rsmAlunos.beforeFirst();
			
			//System.out.println("após rsmAlunos = " + rsmAlunos);
			
			return rsmAlunos;	
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAtividadesByOferta(int cdOferta)	{
		Connection connect = Conexao.conectar();
		ResultSetMap rsmAtividades;
		try	{
			
			rsmAtividades = new ResultSetMap(connect.prepareStatement("SELECT * " +
															 "			FROM acd_oferta_avaliacao A  " +
															 "		  JOIN acd_curso_unidade ACU ON (A.cd_unidade = ACU.cd_unidade"+ 
															 "											AND A.cd_curso = ACU.cd_curso)  " +
															 "		  WHERE A.cd_oferta = "+cdOferta +
															 "			AND A.st_oferta_avaliacao NOT IN (" + OfertaAvaliacaoServices.ST_OFERTA_AVALIACAO_CANCELADA + ") " + 
															 "        ORDER BY nr_ordem, dt_avaliacao DESC").executeQuery());
			
			while( rsmAtividades.next() ){
				
				GregorianCalendar dtAvaliacao = rsmAtividades.getGregorianCalendar("dt_avaliacao");
				if(dtAvaliacao != null)
					rsmAtividades.setValueToField("DT_NM_AVALIACAO", (Integer.parseInt(String.valueOf(dtAvaliacao.get(Calendar.DAY_OF_MONTH))) > 9 ? dtAvaliacao.get(Calendar.DAY_OF_MONTH) : "0" + dtAvaliacao.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtAvaliacao.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtAvaliacao.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtAvaliacao.get(Calendar.MONTH))) + 1)) + "/" + dtAvaliacao.get(Calendar.YEAR));
				
				rsmAtividades.setValueToField("CL_ST_AVALIACAO", OfertaAvaliacaoServices.situacaoOfertaAvaliacao[rsmAtividades.getInt("ST_OFERTA_AVALIACAO")]);
				
				rsmAtividades.setValueToField("CL_VL_PESO", Util.formatNumber(rsmAtividades.getDouble("vl_peso"), 2));
				
				
				float vlTotal = 0;
				int quantNotas = 0;
				ResultSetMap rsmDisciplinaAvaliacaoAluno = new ResultSetMap(connect.prepareStatement("SELECT * " +
																							 "			FROM acd_disciplina_avaliacao_aluno  " +
																							 "		  WHERE cd_oferta = " + rsmAtividades.getInt("cd_oferta") + 
																							 "			AND cd_oferta_avaliacao = " + rsmAtividades.getInt("cd_oferta_avaliacao")).executeQuery());
				while(rsmDisciplinaAvaliacaoAluno.next()){
					vlTotal += rsmDisciplinaAvaliacaoAluno.getFloat("VL_CONCEITO");
					quantNotas++;
				}
				rsmDisciplinaAvaliacaoAluno.beforeFirst();
				
				rsmAtividades.setValueToField("CL_VL_MEDIA_TURMA", Util.formatNumber((vlTotal/(quantNotas == 0 ? 1 : quantNotas)), 2));
				
			}
			rsmAtividades.beforeFirst();
			
			return rsmAtividades;	
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAlunosDisciplinaByOferta(int cdOferta, int cdDisciplina)	{
		Connection connect = Conexao.conectar();
		ResultSetMap rsmAlunos;
		try	{
			rsmAlunos = new ResultSetMap(connect.prepareStatement("SELECT A.*, C.nm_pessoa AS nm_aluno, D.* " +
															 "FROM acd_matricula A " +
															 "JOIN acd_matricula_periodo_letivo B ON (A.cd_matricula = B.cd_matricula) " +
															 "JOIN grl_pessoa               C ON (A.cd_aluno = C.cd_pessoa) " +
															 "LEFT OUTER JOIN acd_turma     D ON (A.cd_turma = D.cd_turma) " +
															 "LEFT OUTER JOIN acd_oferta E ON ( E.cd_turma = D.cd_turma ) " +
															 "WHERE E.cd_oferta = "+cdOferta).executeQuery());
			
			while( rsmAlunos.next() ){
				
				HashMap<String, Object> matriculaDisciplina = new HashMap<String, Object>();
				ResultSetMap rsm =  MatriculaDisciplinaServices.getByAlunoDisciplina(rsmAlunos.getInt("cd_matricula"), cdDisciplina, connect);
				
				if (rsm!=null && rsm.size() > 0)
					matriculaDisciplina.put("CD_DOCUMENTO", rsm.getRegister());
				
				//HashMap<String, Object> matriculaDisciplina = MatriculaDisciplinaServices.getByAlunoDisciplina(rsmAlunos.getInt("cd_matricula"), cdDisciplina, connect).getRegister(); 
				rsmAlunos.setValueToField("DISCIPLINA", matriculaDisciplina);
			}
			
			return rsmAlunos;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getHorariosPossiveis( int cdInstituicao, int cdProfessor, int tpTurno )	{
		Connection connect = Conexao.conectar();
		try	{
			
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
			int cdPeriodoLetivoAtual = 0;
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add( new ItemComparator("cd_professor", String.valueOf( cdProfessor ), ItemComparator.EQUAL, Types.INTEGER) );
			criterios.add( new ItemComparator("tp_turno", String.valueOf( tpTurno ), ItemComparator.EQUAL, Types.INTEGER) );
			criterios.add( new ItemComparator("cd_instituicao", String.valueOf( cdInstituicao ), ItemComparator.EQUAL, Types.INTEGER) );
			criterios.add( new ItemComparator("cd_periodo_letivo", String.valueOf( cdPeriodoLetivoAtual ), ItemComparator.EQUAL, Types.INTEGER) );
			
			return Search.find("SELECT A.*, B.cd_professor FROM acd_instituicao_horario A "+
							   "JOIN acd_professor_horario B ON ( A.cd_horario = B.cd_horario )",
							   "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByProfessor(int cdProfessor) {
		return getAllByProfessor(cdProfessor, null);
	}

	public static ResultSetMap getAllByProfessor(int cdProfessor, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT TUR.tp_turno, A.*, INS.cd_pessoa as cd_instituicao, INS.nm_pessoa AS nm_instituicao, CUR.nm_produto_servico AS nm_curso, TUR.nm_turma, DIS.nm_produto_servico AS nm_disciplina, CUR_DIS.qt_carga_horaria, INS_P.nm_periodo_letivo FROM acd_oferta A"
					+ "							JOIN grl_produto_servico CUR ON (A.cd_curso = CUR.cd_produto_servico) "
					+ "							JOIN acd_turma TUR ON (A.cd_turma = TUR.cd_turma) "
					+ "							JOIN grl_pessoa INS ON (TUR.cd_instituicao = INS.cd_pessoa) "
					+ "							JOIN acd_instituicao_periodo INS_P ON (TUR.cd_instituicao = INS_P.cd_instituicao AND INS_P.st_periodo_letivo = "+InstituicaoPeriodoServices.ST_ATUAL+") "
					+ "							JOIN grl_produto_servico DIS ON (A.cd_disciplina = DIS.cd_produto_servico) "
					+ "							JOIN acd_curso_disciplina CUR_DIS ON (A.cd_curso = CUR_DIS.cd_curso AND TUR.cd_curso_modulo = CUR_DIS.cd_curso_modulo AND A.cd_disciplina = CUR_DIS.cd_disciplina)"
					+ "WHERE A.st_oferta = "+ST_ATIVO+" AND A.cd_professor = "+cdProfessor+" ORDER BY INS.nm_pessoa, CUR.nm_produto_servico, TUR.nm_turma, DIS.nm_produto_servico");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.getAllByProfessor: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByTurma(int cdTurma) {
		return getAllByTurma(cdTurma, null);
	}

	public static ResultSetMap getAllByTurma(int cdTurma, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT TUR.tp_turno, A.*, INS.cd_pessoa as cd_instituicao, INS.nm_pessoa AS nm_instituicao, CUR.nm_produto_servico AS nm_curso, TUR.nm_turma, DIS.nm_produto_servico AS nm_disciplina, CUR_DIS.qt_carga_horaria, INS_P.nm_periodo_letivo FROM acd_oferta A"
					+ "							JOIN grl_produto_servico CUR ON (A.cd_curso = CUR.cd_produto_servico) "
					+ "							JOIN acd_turma TUR ON (A.cd_turma = TUR.cd_turma) "
					+ "							JOIN grl_pessoa INS ON (TUR.cd_instituicao = INS.cd_pessoa) "
					+ "							JOIN acd_instituicao_periodo INS_P ON (TUR.cd_instituicao = INS_P.cd_instituicao AND INS_P.st_periodo_letivo = "+InstituicaoPeriodoServices.ST_ATUAL+") "
					+ "							LEFT OUTER JOIN grl_produto_servico DIS ON (A.cd_disciplina = DIS.cd_produto_servico) "
					+ "							LEFT OUTER JOIN acd_curso_disciplina CUR_DIS ON (A.cd_curso = CUR_DIS.cd_curso AND TUR.cd_curso_modulo = CUR_DIS.cd_curso_modulo AND A.cd_disciplina = CUR_DIS.cd_disciplina)"
					+ "WHERE A.st_oferta = "+ST_ATIVO
					+"   AND A.cd_turma = "+cdTurma
					+" ORDER BY INS.nm_pessoa, CUR.nm_produto_servico, TUR.nm_turma, DIS.nm_produto_servico");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_oferta", rsm.getString("cd_oferta"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("st_pessoa_oferta", "" + PessoaOfertaServices.ST_ATIVO, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPessoaOferta = PessoaOfertaServices.find(criterios, connect);
				String nmProfessores = "";
				while(rsmPessoaOferta.next()){
					nmProfessores += rsmPessoaOferta.getString("NM_PESSOA") + ", ";
				}
				rsmPessoaOferta.beforeFirst();
				
				nmProfessores = nmProfessores.substring(0, nmProfessores.length()-2);
				
				rsm.setValueToField("NM_PROFESSOR", nmProfessores);
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.getAllByProfessor: " + e);
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

	public static ResultSetMap getAllByInstituicao(int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT TUR.tp_turno, A.*, INS.cd_pessoa as cd_instituicao, INS.nm_pessoa AS nm_instituicao, CUR.nm_produto_servico AS nm_curso, TUR.nm_turma, DIS.nm_produto_servico AS nm_disciplina, CUR_DIS.qt_carga_horaria, INS_P.nm_periodo_letivo FROM acd_oferta A"
					+ "							JOIN grl_produto_servico CUR ON (A.cd_curso = CUR.cd_produto_servico) "
					+ "							JOIN acd_turma TUR ON (A.cd_turma = TUR.cd_turma) "
					+ "							JOIN grl_pessoa INS ON (TUR.cd_instituicao = INS.cd_pessoa) "
					+ "							JOIN acd_instituicao_periodo INS_P ON (TUR.cd_instituicao = INS_P.cd_instituicao AND INS_P.st_periodo_letivo = "+InstituicaoPeriodoServices.ST_ATUAL+") "
					+ "							LEFT OUTER JOIN grl_produto_servico DIS ON (A.cd_disciplina = DIS.cd_produto_servico) "
					+ "							LEFT OUTER JOIN acd_curso_disciplina CUR_DIS ON (A.cd_curso = CUR_DIS.cd_curso AND TUR.cd_curso_modulo = CUR_DIS.cd_curso_modulo AND A.cd_disciplina = CUR_DIS.cd_disciplina)"
					+ "WHERE A.st_oferta = "+ST_ATIVO
					+"   AND TUR.cd_instituicao= "+cdInstituicao
					+" ORDER BY INS.nm_pessoa, CUR.nm_produto_servico, TUR.nm_turma, DIS.nm_produto_servico");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_oferta", rsm.getString("cd_oferta"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("st_pessoa_oferta", "" + PessoaOfertaServices.ST_ATIVO, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPessoaOferta = PessoaOfertaServices.find(criterios, connect);
				String nmProfessores = "";
				while(rsmPessoaOferta.next()){
					nmProfessores += rsmPessoaOferta.getString("NM_PESSOA") + ", ";
				}
				rsmPessoaOferta.beforeFirst();
				
				nmProfessores = nmProfessores.substring(0, nmProfessores.length()-2);
				
				rsm.setValueToField("NM_PROFESSOR", nmProfessores);
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.getAllByProfessor: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByPessoa(int cdPessoa) {
		return getAllByPessoa(cdPessoa, 0, null);
	}

	public static ResultSetMap getAllByPessoa(int cdPessoa, Connection connect) {
		return getAllByPessoa(cdPessoa, 0, connect);
	}
	
	public static ResultSetMap getAllByPessoa(int cdPessoa, int cdEmpresa) {
		return getAllByPessoa(cdPessoa, cdEmpresa, null);
	}

	public static ResultSetMap getAllByPessoa(int cdPessoa, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			

			int cdPeriodoAtual = 0;
			ResultSetMap rsmPeriodo = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0), connect);
			if(rsmPeriodo.next())
				cdPeriodoAtual = rsmPeriodo.getInt("cd_periodo_letivo");
			
			pstmt = connect.prepareStatement("SELECT A.*, INS.cd_pessoa as cd_instituicao, INS.nm_pessoa AS nm_instituicao, CUR.nm_produto_servico AS nm_curso, (TUR_CUR.nm_produto_servico || ' - ' || TUR.nm_turma) as nm_turma, DIS.nm_produto_servico AS nm_disciplina FROM acd_oferta A"
					+ "							JOIN grl_produto_servico CUR ON (A.cd_curso = CUR.cd_produto_servico) "
					+ "							JOIN acd_turma TUR ON (A.cd_turma = TUR.cd_turma) "
					+ "							JOIN grl_produto_servico TUR_CUR ON (TUR.cd_curso = TUR_CUR.cd_produto_servico) "
					+ "							JOIN grl_pessoa INS ON (TUR.cd_instituicao = INS.cd_pessoa) "
					+ "							JOIN acd_instituicao_periodo INS_P ON (A.cd_periodo_letivo = INS_P.cd_periodo_letivo) "
					+ "							LEFT OUTER JOIN grl_produto_servico DIS ON (A.cd_disciplina = DIS.cd_produto_servico) "
					+ "							JOIN acd_pessoa_oferta PO ON (A.cd_oferta = PO.cd_oferta) "
					+ "						WHERE A.st_oferta = "+ST_ATIVO+" "
					+ "						  AND (INS_P.cd_periodo_letivo = "+cdPeriodoAtual+" OR INS_P.cd_periodo_letivo_superior = " + cdPeriodoAtual+") "
					+ " 					  AND PO.st_pessoa_oferta = "+PessoaOfertaServices.ST_ATIVO
					+ " 					  AND PO.cd_pessoa = "+cdPessoa
					+(cdInstituicao > 0 && cdInstituicao != ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0) ? " AND TUR.cd_instituicao = " + cdInstituicao : "")
					+" 						ORDER BY INS.nm_pessoa, CUR.nm_produto_servico, TUR.nm_turma, DIS.nm_produto_servico");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				ResultSetMap rsmOfertaHorario = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_oferta_horario OH WHERE OH.cd_oferta = " + rsm.getInt("cd_oferta") + " ORDER BY nr_dia_semana, hr_inicio").executeQuery());
				String clHorarioAulas = ""; 
				while(rsmOfertaHorario.next()){
					
					GregorianCalendar hrInicio  = rsmOfertaHorario.getGregorianCalendar("hr_inicio");
					GregorianCalendar hrTermino = rsmOfertaHorario.getGregorianCalendar("hr_termino");
					
					clHorarioAulas += OfertaHorarioServices.nrDiasSemana[rsmOfertaHorario.getInt("nr_dia_semana")] + " ("+(hrInicio.get(Calendar.HOUR_OF_DAY) > 9 ? hrInicio.get(Calendar.HOUR_OF_DAY) : "0" + hrInicio.get(Calendar.HOUR_OF_DAY))+":"+(hrInicio.get(Calendar.MINUTE) > 9 ? hrInicio.get(Calendar.MINUTE) : "0" + hrInicio.get(Calendar.MINUTE))+" - "+(hrTermino.get(Calendar.HOUR_OF_DAY) > 9 ? hrTermino.get(Calendar.HOUR_OF_DAY) : "0" + hrTermino.get(Calendar.HOUR_OF_DAY))+":"+(hrTermino.get(Calendar.MINUTE) > 9 ? hrTermino.get(Calendar.MINUTE) : "0" + hrTermino.get(Calendar.MINUTE))+"), ";
				}
				rsmOfertaHorario.beforeFirst();
				
				if(clHorarioAulas.length() > 0)
					clHorarioAulas = clHorarioAulas.substring(0, clHorarioAulas.length()-2);
				
				rsm.setValueToField("CL_HORARIO_AULAS", clHorarioAulas);
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.getAllByPessoa: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getDisciplinasByProfessorPeriodo(int cdProfessor, int cdTurma) {
		return getDisciplinasByProfessorPeriodo(cdProfessor, cdTurma, null);
	}
	
	public static ResultSetMap getDisciplinasByProfessorPeriodo(int cdProfessor, int cdTurma, Connection connect) {
		boolean isConnectionNull = connect == null;
		
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Turma turma = TurmaDAO.get(cdTurma, connect);
						
			PreparedStatement pstmt = connect.prepareStatement("SELECT D.*, DPS.*, DPS.nm_produto_servico AS NM_DISCIPLINA FROM acd_oferta A "
					+ "			  												JOIN acd_disciplina D ON (A.cd_disciplina = D.cd_disciplina) "
					+ "			  												JOIN grl_produto_servico DPS ON (D.cd_disciplina = DPS.cd_produto_servico) "
					+ "           										  WHERE A.cd_periodo_letivo = " + turma.getCdPeriodoLetivo() 
					+ " 													AND A.cd_turma = "+cdTurma
					+ " 													AND A.st_oferta = "+ST_ATIVO
					+ " 													AND EXISTS (SELECT * FROM acd_pessoa_oferta PO WHERE PO.cd_oferta = A.cd_oferta " 
					+ "																										 AND PO.cd_pessoa = "+cdProfessor
					+ " 																									 AND PO.st_pessoa_oferta = "+PessoaOfertaServices.ST_ATIVO
					+ "																	)"
					+ "											 ORDER BY DPS.nm_produto_servico");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.getDisciplinasByProfessorPeriodo: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.getDisciplinasByProfessorPeriodo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getDisciplinasByAlunoTurma(int cdAluno, int cdTurma) {
		return getDisciplinasByAlunoTurma(cdAluno, cdTurma, null);
	}
	
	public static ResultSetMap getDisciplinasByAlunoTurma(int cdAluno, int cdTurma, Connection connect) {
		boolean isConnectionNull = connect == null;
		
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Turma turma = TurmaDAO.get(cdTurma, connect);
						
			PreparedStatement pstmt = connect.prepareStatement("SELECT D.*, DPS.*, DPS.nm_produto_servico AS NM_DISCIPLINA FROM acd_oferta A "
					+ "			  												JOIN acd_disciplina D ON (A.cd_disciplina = D.cd_disciplina) "
					+ "			  												JOIN grl_produto_servico DPS ON (D.cd_disciplina = DPS.cd_produto_servico) "
					+ "           										  WHERE A.cd_periodo_letivo = " + turma.getCdPeriodoLetivo() 
					+ " 													AND A.cd_turma = "+cdTurma
					+ " 													AND A.st_oferta = "+ST_ATIVO
					+ " 													AND EXISTS (SELECT * FROM acd_pessoa_oferta PO WHERE PO.cd_oferta = A.cd_oferta " 
					+ "																										 AND PO.cd_pessoa = "+cdAluno
					+ " 																									 AND PO.st_pessoa_oferta = "+PessoaOfertaServices.ST_ATIVO
					+ "																	)"
					+ "											 ORDER BY DPS.nm_produto_servico");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.getDisciplinasByProfessorPeriodo: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.getDisciplinasByProfessorPeriodo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getDisciplinasByTurma(int cdTurma) {
		return getDisciplinasByTurma(cdTurma, null);
	}
	
	public static ResultSetMap getDisciplinasByTurma(int cdTurma, Connection connect) {
		boolean isConnectionNull = connect == null;
		
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
						
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, D.*, DPS.*, DPS.nm_produto_servico AS NM_DISCIPLINA FROM acd_oferta A "
					+ "			  												JOIN acd_disciplina D ON (A.cd_disciplina = D.cd_disciplina) "
					+ "			  												JOIN grl_produto_servico DPS ON (D.cd_disciplina = DPS.cd_produto_servico) "
					+ "           										  WHERE A.cd_turma = "+cdTurma+" AND A.st_oferta = "+ST_ATIVO);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.getDisciplinasByProfessorPeriodo: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.getDisciplinasByProfessorPeriodo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap findCursosTurmasByProfessor(int cdProfessor, int cdInstituicao, int cdPeriodoLetivo) {
		return findCursosTurmasByProfessor(cdProfessor, cdInstituicao, cdPeriodoLetivo, null);
	}
	
	public static ResultSetMap findCursosTurmasByProfessor(int cdProfessor, int cdInstituicao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect == null;
		
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
				if(rsmPeriodoAtual.next())
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			ResultSetMap rsmCursos =  new ResultSetMap(connect.prepareStatement("SELECT A.*, B.nm_produto_servico AS NM_CURSO FROM acd_curso A"
					+ "																JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico) "
					+ "															  WHERE EXISTS (SELECT * FROM acd_oferta O "
					+ "																				JOIN acd_turma T ON (O.cd_turma = T.cd_turma)"
					+ "																			WHERE O.cd_curso = A.cd_curso "
					+ "																			  AND O.st_oferta = " + ST_ATIVO
					+ "																			  AND T.cd_instituicao = "+cdInstituicao
					+ "																			  AND T.cd_periodo_letivo = "+cdPeriodoLetivo
					+ " 																			  AND EXISTS(SELECT * FROM acd_pessoa_oferta PO "
					+ "																							WHERE O.cd_oferta = PO.cd_oferta "
					+ "																							  AND PO.cd_pessoa = "+cdProfessor+")"
					+ "																			)").executeQuery());
			
			while(rsmCursos.next()){
				ResultSetMap rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT T.* FROM acd_turma T"
						+ "															  WHERE T.cd_instituicao = "+cdInstituicao
						+ "																AND T.cd_curso = " + rsmCursos.getInt("cd_curso")
						+ "															    AND T.cd_periodo_letivo = "+cdPeriodoLetivo
						+ "																AND EXISTS (SELECT * FROM acd_oferta O "
						+ "																			WHERE O.cd_turma = T.cd_turma"
						+ "																			  AND O.st_oferta = " + ST_ATIVO
						+ " 																		  AND EXISTS(SELECT * FROM acd_pessoa_oferta PO "
						+ "																							WHERE O.cd_oferta = PO.cd_oferta "
						+ "																							  AND PO.cd_pessoa = "+cdProfessor+")"
						+ "																			)").executeQuery());
				
				while(rsmTurmas.next()){
					rsmTurmas.setValueToField("NM_TURMA_LABEL", "Turma " + rsmTurmas.getString("NM_TURMA"));
					rsmTurmas.setValueToField("NM_TURMA_COMPLETO", rsmCursos.getString("NM_CURSO") + " - " + rsmTurmas.getString("NM_TURMA"));
				}
				rsmTurmas.beforeFirst();
				
				rsmCursos.setValueToField("RSM_TURMAS", rsmTurmas);
			}
			rsmCursos.beforeFirst();
			return rsmCursos;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.findCursosOfertasByProfessor: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.findCursosOfertasByProfessor: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findTurmasByProfessor(int cdProfessor, int cdInstituicao, int cdPeriodoLetivo) {
		return findTurmasByProfessor(cdProfessor, cdInstituicao, cdPeriodoLetivo, null);
	}
	
	public static ResultSetMap findTurmasByProfessor(int cdProfessor, int cdInstituicao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect == null;
		
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
				if(rsmPeriodoAtual.next())
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			ResultSetMap rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT T.*, C.tp_avaliacao, C.nr_ordem FROM acd_turma T, acd_curso C"
					+ "															  WHERE T.cd_curso = C.cd_curso "
					+ "																AND T.cd_instituicao = "+cdInstituicao
					+ "															    AND T.cd_periodo_letivo = "+cdPeriodoLetivo
					+ "																AND EXISTS (SELECT * FROM acd_oferta O "
					+ "																			WHERE O.cd_turma = T.cd_turma"
					+ "																			  AND O.st_oferta = " + ST_ATIVO
					+ " 																		  AND EXISTS(SELECT * FROM acd_pessoa_oferta PO "
					+ "																							WHERE O.cd_oferta = PO.cd_oferta "
					+ "																							  AND PO.cd_pessoa = "+cdProfessor+")"
					+ "																			)").executeQuery());
			
			while(rsmTurmas.next()){
				
				Curso curso = CursoDAO.get(rsmTurmas.getInt("cd_curso"), connect);
				
				rsmTurmas.setValueToField("NM_TURMA_LABEL", "Turma " + rsmTurmas.getString("NM_TURMA"));
				rsmTurmas.setValueToField("NM_TURMA_COMPLETO", curso.getNmProdutoServico() + " - " + rsmTurmas.getString("NM_TURMA"));
				
				
				if(CursoServices.isCreche(rsmTurmas.getInt("cd_curso"), connect) || CursoServices.isEducacaoInfantil(rsmTurmas.getInt("cd_curso"), connect)){
					rsmTurmas.setValueToField("IS_INFANTIL", 1);
				}
				else{
					rsmTurmas.setValueToField("IS_INFANTIL", 0);
				}
				
				if(CursoServices.isFundamentalRegular(rsmTurmas.getInt("cd_curso"), connect) && rsmTurmas.getInt("nr_ordem") >= 5){
					rsmTurmas.setValueToField("IS_FUND_1", 0);
					rsmTurmas.setValueToField("IS_FUND_2", 1);
				}
				else if(CursoServices.isFundamentalRegular(rsmTurmas.getInt("cd_curso"), connect) || rsmTurmas.getInt("cd_curso") == 89 /*Multi*/){
					rsmTurmas.setValueToField("IS_FUND_1", 1);
					rsmTurmas.setValueToField("IS_FUND_2", 0);
				}
				
			}
			rsmTurmas.beforeFirst();
			
			return rsmTurmas;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.findTurmasByProfessor: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.findTurmasByProfessor: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findTurmasDisciplinasByProfessor(int cdProfessor, int cdInstituicao, int cdPeriodoLetivo) {
		return findTurmasDisciplinasByProfessor(cdProfessor, cdInstituicao, cdPeriodoLetivo, null);
	}
	
	public static ResultSetMap findTurmasDisciplinasByProfessor(int cdProfessor, int cdInstituicao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect == null;
		
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
				if(rsmPeriodoAtual.next())
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			ResultSetMap rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT T.*, (B.nm_produto_servico || ' - ' || T.nm_turma) AS NM_TURMA_COMPLETO, O.cd_oferta, O.cd_professor, O.cd_disciplina, PS_DIS.nm_produto_servico AS NM_DISCIPLINA, C.tp_avaliacao FROM acd_turma T"
					+ "																JOIN grl_produto_servico B ON (T.cd_curso = B.cd_produto_servico) "
					+ "																JOIN acd_oferta O ON (O.cd_turma = T.cd_turma) "
					+ "																JOIN acd_curso C ON (C.cd_curso = T.cd_curso) "
					+ "																LEFT OUTER JOIN grl_produto_servico PS_DIS ON (O.cd_disciplina = PS_DIS.cd_produto_servico) "
					+ "															  WHERE T.cd_instituicao = "+cdInstituicao
					+ "															    AND T.cd_periodo_letivo = "+cdPeriodoLetivo
					+ "																AND O.st_oferta = " + ST_ATIVO
					+ " 															AND EXISTS(SELECT * FROM acd_pessoa_oferta PO "
					+ "																			WHERE O.cd_oferta = PO.cd_oferta "
					+ "																			  AND PO.cd_pessoa = "+cdProfessor
					+ "																			)"
					+ "															  ORDER BY PS_DIS.nm_produto_servico").executeQuery());
			
			while(rsmTurmas.next()){
				rsmTurmas.setValueToField("NM_OFERTA", (rsmTurmas.getString("NM_DISCIPLINA") != null ? rsmTurmas.getString("NM_DISCIPLINA") + " - " : "") + rsmTurmas.getString("NM_TURMA_COMPLETO"));
			}
			rsmTurmas.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_DISCIPLINA");
			fields.add("NM_TURMA_COMPLETO");
			rsmTurmas.orderBy(fields);
			
			rsmTurmas.beforeFirst();
			
			return rsmTurmas;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.findCursosOfertasByProfessor: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.findCursosOfertasByProfessor: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findDisciplinasCursosTurmasByProfessor(int cdProfessor, int cdInstituicao, int cdPeriodoLetivo) {
		return findDisciplinasCursosTurmasByProfessor(cdProfessor, cdInstituicao, cdPeriodoLetivo, null);
	}
	
	public static ResultSetMap findDisciplinasCursosTurmasByProfessor(int cdProfessor, int cdInstituicao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect == null;
		
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
				if(rsmPeriodoAtual.next())
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			ResultSetMap rsmDisciplinas =  new ResultSetMap(connect.prepareStatement("SELECT A.*, B.nm_produto_servico AS NM_DISCIPLINA, O.cd_oferta FROM acd_disciplina A"
					+ "																JOIN grl_produto_servico B ON (A.cd_disciplina = B.cd_produto_servico) "
					+ "																JOIN acd_oferta O ON (O.cd_disciplina = A.cd_disciplina) "
					+ "																JOIN acd_turma T ON (O.cd_turma = T.cd_turma) "
					+ "															  WHERE O.st_oferta = " + ST_ATIVO
					+ "																AND T.cd_instituicao = "+cdInstituicao
					+ "																AND T.cd_periodo_letivo = "+cdPeriodoLetivo
					+ " 															AND EXISTS(SELECT * FROM acd_pessoa_oferta PO "
					+ "																			WHERE O.cd_oferta = PO.cd_oferta "
					+ "																			  AND PO.cd_pessoa = "+cdProfessor
					+"																			  AND PO.st_pessoa_oferta = "+PessoaOfertaServices.ST_ATIVO+")"
					+ "																			").executeQuery());
			
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
				
				ResultSetMap rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT T.*, (B.nm_produto_servico || ' - ' || T.nm_turma) AS NM_TURMA_COMPLETO, O.cd_oferta, O.cd_professor FROM acd_turma T"
						+ "																JOIN grl_produto_servico B ON (T.cd_curso = B.cd_produto_servico) "
						+ "																JOIN acd_oferta O ON (O.cd_turma = T.cd_turma) "
						+ "															  WHERE T.cd_instituicao = "+cdInstituicao
						+ "															    AND T.cd_periodo_letivo = "+cdPeriodoLetivo
						+ "																AND O.st_oferta = " + ST_ATIVO
						+ "															    AND O.cd_disciplina = " + rsmDisciplinas.getInt("cd_disciplina")
						+ " 															AND EXISTS(SELECT * FROM acd_pessoa_oferta PO "
						+ "																			WHERE O.cd_oferta = PO.cd_oferta "
						+ "																			  AND PO.cd_pessoa = "+cdProfessor+")").executeQuery());
				
				while(rsmTurmas.next()){
					rsmTurmas.setValueToField("CD_DISCIPLINA", rsmDisciplinas.getInt("CD_DISCIPLINA"));
					rsmTurmas.setValueToField("NM_DISCIPLINA", rsmDisciplinas.getString("NM_DISCIPLINA"));
				}
				rsmTurmas.beforeFirst();
				
				//rsmDisciplinas.setValueToField("RSM_TURMAS", rsmTurmas);
				x++;
			}
			rsmDisciplinas.beforeFirst();
			return rsmDisciplinas;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.findCursosOfertasByProfessor: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.findCursosOfertasByProfessor: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findDisciplinasCursosTurmasByAluno(int cdAluno, int cdInstituicao, int cdPeriodoLetivo) {
		return findDisciplinasCursosTurmasByAluno(cdAluno, cdInstituicao, cdPeriodoLetivo, null);
	}
	
	public static ResultSetMap findDisciplinasCursosTurmasByAluno(int cdAluno, int cdInstituicao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect == null;
		
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdInstituicao, connect);
				if(rsmPeriodoRecente.next())
					cdPeriodoLetivo = rsmPeriodoRecente.getInt("cd_periodo_letivo");
			}
			
			ResultSetMap rsmMatricula = MatriculaServices.getMatriculaRegularRecenteByAluno(cdAluno, connect);
			
			Matricula matricula = null;
			while(rsmMatricula.next()){
				matricula = MatriculaDAO.get(rsmMatricula.getInt("cd_matricula"), connect);
			}
			
			ResultSetMap rsmDisciplinas =  new ResultSetMap(connect.prepareStatement("SELECT A.*, B.nm_produto_servico AS NM_DISCIPLINA, O.cd_oferta, O.cd_curso, PROF.NM_PESSOA AS nm_professor, MD.cd_matricula_disciplina FROM acd_disciplina A"
					+ "																JOIN grl_produto_servico B ON (A.cd_disciplina = B.cd_produto_servico) "
					+ "																JOIN acd_oferta O ON (O.cd_disciplina = A.cd_disciplina) "
					+ "																JOIN acd_turma T ON (O.cd_turma = T.cd_turma) "
					+ "																JOIN acd_matricula_disciplina MD ON (MD.cd_matricula = "+matricula.getCdMatricula()+" AND MD.cd_disciplina = A.cd_disciplina) "
					+ "																JOIN grl_pessoa PROF ON (O.cd_professor = PROF.cd_pessoa) "
					+ "															  WHERE O.st_oferta = " + ST_ATIVO
					+ "																AND T.cd_instituicao = "+cdInstituicao
					+ "																AND T.cd_periodo_letivo = "+cdPeriodoLetivo 
					+ "															  ORDER BY B.nm_produto_servico").executeQuery());
			
			GregorianCalendar dtHoje = Util.getDataAtual();
		
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
				System.out.println(rsmDisciplinas);
				ResultSetMap rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT T.*, (B.nm_produto_servico || ' - ' || T.nm_turma) AS NM_TURMA_COMPLETO, O.cd_oferta FROM acd_turma T"
						+ "																JOIN grl_produto_servico B ON (T.cd_curso = B.cd_produto_servico) "
						+ "																JOIN acd_oferta O ON (O.cd_turma = T.cd_turma) "
						+ "															  WHERE T.cd_instituicao = "+cdInstituicao
						+ "															    AND T.cd_periodo_letivo = "+cdPeriodoLetivo
						+ "																AND O.st_oferta = " + ST_ATIVO
						+ "															    AND O.cd_disciplina = " + rsmDisciplinas.getInt("cd_disciplina")
						+ " 															AND EXISTS(SELECT * FROM acd_matricula MAT "
						+ "																			WHERE T.cd_turma = MAT.cd_turma "
						+ "																			  AND MAT.cd_aluno = "+cdAluno
						+"																			  AND MAT.st_matricula = "+MatriculaServices.ST_ATIVA+")").executeQuery());
				
				while(rsmTurmas.next()){
					rsmTurmas.setValueToField("CD_DISCIPLINA", rsmDisciplinas.getInt("CD_DISCIPLINA"));
					rsmTurmas.setValueToField("NM_DISCIPLINA", rsmDisciplinas.getString("NM_DISCIPLINA"));
					rsmTurmas.setValueToField("CD_MATRICULA_DISCIPLINA", rsmDisciplinas.getString("CD_MATRICULA_DISCIPLINA"));
				}
				rsmTurmas.beforeFirst();
				
				rsmDisciplinas.setValueToField("RSM_TURMAS", rsmTurmas);
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				GregorianCalendar dtProximaAula = null;
				ResultSetMap rsmOfertaHorario = OfertaHorarioServices.getAllByOferta(rsmDisciplinas.getInt("cd_oferta"), connect);
				while(rsmOfertaHorario.next()){
					if(dtHoje.get(Calendar.DAY_OF_WEEK) == (rsmOfertaHorario.getInt("nr_dia_semana")+1)){
						dtProximaAula = (GregorianCalendar)dtHoje.clone();
						register.put("dtProximaAula", dtProximaAula);
						register.put("hrInicio", rsmOfertaHorario.getGregorianCalendar("hr_inicio"));
						register.put("hrTermino", rsmOfertaHorario.getGregorianCalendar("hr_termino"));
						break;
					}
					else{
						int nrDiaSemana = (rsmOfertaHorario.getInt("nr_dia_semana")+1);
						GregorianCalendar dtAula = new GregorianCalendar();
						while(dtAula.get(Calendar.DAY_OF_WEEK) != nrDiaSemana){
							dtAula.add(Calendar.DAY_OF_WEEK, 1);
						}
						
						if(dtProximaAula == null || dtAula.before(dtProximaAula)){
							dtProximaAula = (GregorianCalendar) dtAula.clone();
							register.put("dtProximaAula", dtProximaAula);
							register.put("hrInicio", rsmOfertaHorario.getGregorianCalendar("hr_inicio"));
							register.put("hrTermino", rsmOfertaHorario.getGregorianCalendar("hr_termino"));
						}
						
					}
				}
				
				String txtDia = Util.convCalendarString3(dtProximaAula);
				GregorianCalendar dtProximaAulaCopy = (GregorianCalendar) dtProximaAula.clone();
				dtProximaAulaCopy.add(Calendar.DAY_OF_MONTH, (-1)*1);
				if(dtHoje.get(Calendar.DAY_OF_MONTH) == dtProximaAula.get(Calendar.DAY_OF_MONTH) &&
					dtHoje.get(Calendar.MONTH) == dtProximaAula.get(Calendar.MONTH) &&
					dtHoje.get(Calendar.YEAR) == dtProximaAula.get(Calendar.YEAR)){
					txtDia = "Hoje";
				}
				else if(dtHoje.get(Calendar.DAY_OF_MONTH) == dtProximaAulaCopy.get(Calendar.DAY_OF_MONTH) &&
						dtHoje.get(Calendar.MONTH) == dtProximaAulaCopy.get(Calendar.MONTH) &&
						dtHoje.get(Calendar.YEAR) == dtProximaAulaCopy.get(Calendar.YEAR)){
					txtDia = "Amanhã";
				}
				
				rsmDisciplinas.setValueToField("TXT_PROXIMA_AULA", txtDia + " ("+Util.convCalendarStringHourMinute((GregorianCalendar)register.get("hrInicio"))+" - "+Util.convCalendarStringHourMinute((GregorianCalendar)register.get("hrTermino"))+")");
				
				
				
				x++;
			}
			rsmDisciplinas.beforeFirst();
			
			return rsmDisciplinas;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.findCursosOfertasByProfessor: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaServices.findCursosOfertasByProfessor: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}

