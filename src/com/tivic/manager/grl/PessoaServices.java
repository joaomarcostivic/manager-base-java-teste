package com.tivic.manager.grl;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import com.tivic.manager.acd.Aluno;
import com.tivic.manager.acd.AlunoDAO;
import com.tivic.manager.acd.Formacao;
import com.tivic.manager.acd.FormacaoCurso;
import com.tivic.manager.acd.FormacaoCursoDAO;
import com.tivic.manager.acd.FormacaoDAO;
import com.tivic.manager.acd.FormacaoServices;
import com.tivic.manager.acd.InstituicaoEducacensoServices;
import com.tivic.manager.acd.OcorrenciaProfessor;
import com.tivic.manager.acd.OcorrenciaProfessorDAO;
import com.tivic.manager.acd.OcorrenciaProfessorServices;
import com.tivic.manager.acd.Professor;
import com.tivic.manager.acd.ProfessorDAO;
import com.tivic.manager.acd.ProfessorServices;
import com.tivic.manager.adm.Cliente;
import com.tivic.manager.adm.ClienteDAO;
import com.tivic.manager.adm.ClienteProduto;
import com.tivic.manager.adm.ClienteProdutoDAO;
import com.tivic.manager.adm.CondicaoPagamento;
import com.tivic.manager.adm.CondicaoPagamentoDAO;
import com.tivic.manager.adm.ContaReceberServices;
import com.tivic.manager.adm.ContratoDependenteServices;
import com.tivic.manager.agd.AgendaParticipanteServices;
import com.tivic.manager.agd.GrupoServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.mob.Concessao;
import com.tivic.manager.mob.ConcessaoServices;
import com.tivic.manager.mob.MotoristaFolgaAgendamento;
import com.tivic.manager.mob.MotoristaFolgaAgendamentoDAO;
import com.tivic.manager.mob.MotoristaFolgaAgendamentoServices;
import com.tivic.manager.mob.MotoristaFolgaServices;
import com.tivic.manager.prc.CidadeOrgaoServices;
import com.tivic.manager.prc.CidadeOrgaoServicoServices;
import com.tivic.manager.prc.EstadoOrgaoServices;
import com.tivic.manager.prc.EstadoOrgaoServicoServices;
import com.tivic.manager.prc.Orgao;
import com.tivic.manager.prc.OrgaoDAO;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.srh.DadosFuncionais;
import com.tivic.manager.srh.DadosFuncionaisDAO;
import com.tivic.manager.srh.DadosFuncionaisServices;
import com.tivic.manager.util.ImagemServices;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.ValidatorResult;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class PessoaServices {
	public static final int PARCEIRO   = 1;
	public static final int CORRETOR   = 2;
	public static final int PROMOTOR   = 3;


	public static final int TP_JURIDICA = 0;
	public static final int TP_FISICA   = 1;

	public static String[] tipoPessoa   = {"Jurídica", "Física"};

	public static final int ERR_GERAL        = -1;
	public static final int ERR_NR_DOCUMENTO = -2;

	/*Factoring - Para o campo st_cadastro -- Deve ser alterado*/
	public static final int ST_DESBLOQUEADO = 0;
	public static final int ST_BLOQUEADO 	= 1;
	/* Para o campo st_cadastro*/
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO   = 1;

	public static String[] situacaoPessoa = {"Inativo","Ativo"};
	
	public static Result save(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, PessoaContaBancaria pessoaContaBancaria, DadosFuncionais dadosFuncionais, Professor professor, int cdUsuario){
		return save(pessoa, endereco, cdEmpresa, cdVinculo, pessoaContaBancaria, dadosFuncionais, professor, 0, cdUsuario, null, null);
	}
	public static Result save(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, PessoaContaBancaria pessoaContaBancaria, DadosFuncionais dadosFuncionais, Professor professor){
		return save(pessoa, endereco, cdEmpresa, cdVinculo, pessoaContaBancaria, dadosFuncionais, professor, 0, 0, null, null);
	}
	
	public static Result save(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, PessoaContaBancaria pessoaContaBancaria, DadosFuncionais dadosFuncionais, Professor professor, int possuiDeficiencia, int cdUsuario){
		return save(pessoa, endereco, cdEmpresa, cdVinculo, pessoaContaBancaria, dadosFuncionais, professor, possuiDeficiencia, cdUsuario, null, null);
	}
	
	public static Result save(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, PessoaContaBancaria pessoaContaBancaria, DadosFuncionais dadosFuncionais){
		return save(pessoa, endereco, cdEmpresa, cdVinculo, pessoaContaBancaria, dadosFuncionais, null, null);
	}
	
	public static Result save(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, PessoaContaBancaria pessoaContaBancaria){
		return save(pessoa, endereco, cdEmpresa, cdVinculo, pessoaContaBancaria, null, null, null);
	}
	
	public static Result save(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, PessoaContaBancaria pessoaContaBancaria, ArrayList<Integer> vinculos){
		//return save(pessoa, endereco, cdEmpresa, cdVinculo, pessoaContaBancaria, null, null, null);
		
		return save(pessoa, endereco, cdEmpresa, cdVinculo, pessoaContaBancaria, null, null, 0, 0, null, vinculos, null);
	}
		
	public static Result save(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo){
		return save(pessoa, endereco, cdEmpresa, cdVinculo, null, null, null, null);
	}
	
	public static Result save(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, PessoaFichaMedica pessoaFichaMedica){
		return save(pessoa, endereco, cdEmpresa, cdVinculo, null, null, null, 0, 0, pessoaFichaMedica);
	}
	
	public static Result save(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, Connection connect){
		return save(pessoa, endereco, cdEmpresa, cdVinculo, null, null, null, connect);
	}

	public static Result save(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, PessoaContaBancaria pessoaContaBancaria, DadosFuncionais dadosFuncionais, Professor professor, Connection connect){
		return save(pessoa, endereco, cdEmpresa, cdVinculo, pessoaContaBancaria, dadosFuncionais, professor, -1, connect);
	}
	
	public static Result save(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, PessoaContaBancaria pessoaContaBancaria, DadosFuncionais dadosFuncionais, Professor professor, int possuiDeficiencia, Connection connect){
		return save(pessoa, endereco, cdEmpresa, cdVinculo, pessoaContaBancaria, dadosFuncionais, professor, possuiDeficiencia, 0, connect);
	}
	
	public static Result save(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, PessoaContaBancaria pessoaContaBancaria, DadosFuncionais dadosFuncionais, Professor professor, int possuiDeficiencia, int cdUsuario, Connection connect){
		return save(pessoa, endereco, cdEmpresa, cdVinculo, pessoaContaBancaria, dadosFuncionais, professor, possuiDeficiencia, cdUsuario, null, null, connect);
	}
	
	public static Result save(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, PessoaContaBancaria pessoaContaBancaria, DadosFuncionais dadosFuncionais, Professor professor, 
			int possuiDeficiencia, int cdUsuario, PessoaFichaMedica pessoaFichaMedica){
				
		return save(pessoa, endereco, cdEmpresa, cdVinculo, pessoaContaBancaria, dadosFuncionais, professor, possuiDeficiencia, cdUsuario, pessoaFichaMedica, null, null);
	}

	public static Result save(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, PessoaContaBancaria pessoaContaBancaria, DadosFuncionais dadosFuncionais, Professor professor, 
			int possuiDeficiencia, int cdUsuario, PessoaFichaMedica pessoaFichaMedica, ArrayList<Integer> vinculos){
		
		if(cdVinculo>0) {
			if(vinculos==null) 
				vinculos = new ArrayList<>();
			vinculos.add(cdVinculo);
		}
		
		return save(pessoa, endereco, cdEmpresa, cdVinculo, pessoaContaBancaria, dadosFuncionais, professor, possuiDeficiencia, cdUsuario, pessoaFichaMedica, vinculos, null);
	}
	
	public static Result save(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, 
			PessoaContaBancaria pessoaContaBancaria, DadosFuncionais dadosFuncionais, Professor professor, 
			int possuiDeficiencia, int cdUsuario, PessoaFichaMedica pessoaFichaMedica, ArrayList<Integer> vinculos,
			Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			boolean isInsert = false;
			
			if(pessoa==null)
				return new Result(-1, "Erro ao salvar. Pessoa é nula");
			Result retorno;
			ValidatorResult resultadoValidacao = new ValidatorResult(1);
			
			if(pessoa.getNmPessoa()!=null)
				pessoa.setNmPessoa(pessoa.getNmPessoa().trim());
			

			int cdOrgao = 0;
			if(pessoa.getCdPessoa()==0){
				isInsert = true;
				pessoa.setDtCadastro(new GregorianCalendar());			
				retorno = insert(pessoa, endereco, null, cdEmpresa, cdVinculo, null, 0, null, 0, null, pessoaContaBancaria, dadosFuncionais, connect);
				
				if(retorno.getCode()<0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return retorno;
				}
				
				if(retorno.getCode()>0) {
					pessoa.setCdPessoa(((Pessoa)retorno.getObjects().get("pessoa")).getCdPessoa());
				}
				
				/*
				 * vinculos
				 */
				if(vinculos!=null && vinculos.size()>0) {
					for (Integer cdVinc : vinculos) {
						retorno = addVinculo(pessoa.getCdPessoa(), cdEmpresa, cdVinc, connect);
						LogUtils.debug("pessoa.getCdPessoa(): "+pessoa.getCdPessoa());
						if(retorno.getCode()<0) {
							if (isConnectionNull)
								Conexao.rollback(connect);
							return retorno;
						}
						cdOrgao = retorno.getObjects().get("CD_ORGAO")!=null ? (int)retorno.getObjects().get("CD_ORGAO") : 0;
					}
				}
			}
			else {
				retorno = update(pessoa, endereco, null, cdEmpresa, cdVinculo, cdVinculo, null, 0, null, 0, null, pessoaContaBancaria, dadosFuncionais, connect);
			}
			
			/*
			 * Ficha Médica
			 */
			if (pessoaFichaMedica != null) {
				pessoaFichaMedica.setCdPessoa(pessoa.getCdPessoa());
				int r = 0;
				if(PessoaFichaMedicaDAO.get(pessoa.getCdPessoa(), connect) == null){
					r = PessoaFichaMedicaDAO.insert(pessoaFichaMedica, connect);
				}
				else{
					r = PessoaFichaMedicaDAO.update(pessoaFichaMedica, connect);
				}
				if (r <=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-4, "Erro ao tentar salvar ficha médica!");
				}
			}else if((isInsert || PessoaFichaMedicaDAO.get(pessoa.getCdPessoa(), connect) == null) && PessoaFisicaDAO.get(pessoa.getCdPessoa(), connect) != null){
				
				pessoaFichaMedica = new PessoaFichaMedica();
				pessoaFichaMedica.setCdPessoa(pessoa.getCdPessoa());
				int r = PessoaFichaMedicaDAO.insert(pessoaFichaMedica, connect);
				if (r <=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-4, "Erro ao tentar salvar ficha médica!");
				}	
			}
			

			/*
			 * Inativar usuario
			 */
			if(pessoa.getStCadastro()==PessoaServices.ST_INATIVO) {
				Usuario user = UsuarioServices.getByPessoa(pessoa.getCdPessoa(), connect);
				
				if(user!=null) {
					user.setStUsuario(UsuarioServices.ST_INATIVO);
					retorno = UsuarioServices.save(user, connect);
				}
			}
			
			if(retorno.getCode()<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			if(resultadoValidacao.getCode() == ValidatorResult.VALID){
				Result result = new Result(retorno.getCode(), (retorno.getCode()<0) ? retorno.getMessage() : "Registro salvo com sucesso..."+
											(pessoa.getStCadastro()==PessoaServices.ST_INATIVO ? "\nO usuário dessa pessoa foi inativada." : ""), "PESSOA", pessoa);
				
				/*
				 * Adiciona código da matrícula (srh_dados_funcionais)
				 * caso o vínculo da pessoa seja de PROFESSOR
				 */
				if(cdVinculo==ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_PROFESSOR", 0)) {
					DadosFuncionais df = (DadosFuncionais)retorno.getObjects().get("dadosFuncionais");
					if(df!=null) 
						result.addObject("CD_MATRICULA", df.getCdMatricula());
				}
				
				if(cdOrgao>0)
					result.addObject("CD_ORGAO", cdOrgao);
				
				return result;
			}
			else{
				resultadoValidacao.getObjects().put("PESSOA", pessoa);
				if(pessoa.getGnPessoa() == TP_FISICA)
					resultadoValidacao.getObjects().put("PESSOAFISICA", pessoa);
				else if(pessoa.getGnPessoa() == TP_JURIDICA)
					resultadoValidacao.getObjects().put("PESSOAJURIDICA", pessoa);
				
				resultadoValidacao.setMessage((retorno.getCode()<0) ? retorno.getMessage() : "Registro salvo com sucesso..."+ (pessoa.getStCadastro()==PessoaServices.ST_INATIVO ? "\nO usuário dessa pessoa foi inativada." : ""));
				
				if(cdVinculo==ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_PROFESSOR", 0)) {
					DadosFuncionais df = (DadosFuncionais)retorno.getObjects().get("dadosFuncionais");
					if(df!=null) 
						resultadoValidacao.getObjects().put("CD_MATRICULA", df.getCdMatricula());
				}
				
				if(cdOrgao>0)
					resultadoValidacao.getObjects().put("CD_ORGAO", cdOrgao);
				
				return resultadoValidacao;
			}
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
	
	public static Result addVinculo(int cdPessoa, int cdEmpresa, int cdVinculo) {
		return addVinculo(cdPessoa, cdEmpresa, cdVinculo, new GregorianCalendar(), PessoaEmpresaServices.ST_ATIVO, null);
	}
	
	public static Result addVinculo(int cdPessoa, int cdEmpresa, int cdVinculo, Connection connect){
		return addVinculo(cdPessoa, cdEmpresa, cdVinculo, new GregorianCalendar(), PessoaEmpresaServices.ST_ATIVO, connect);
	}
	
	public static Result addVinculo(int cdPessoa, int cdEmpresa, int cdVinculo, GregorianCalendar dtValidade, int stVinculo) {
		return addVinculo(cdPessoa, cdEmpresa, cdVinculo, dtValidade, stVinculo, null);
	}
	
	public static Result addVinculo(int cdPessoa, int cdEmpresa, int cdVinculo, GregorianCalendar dtValidade, int stVinculo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if (EmpresaDAO.get(cdEmpresa, connect)==null)
				return new Result(-1, "Erro ao salvar. Empresa indicada não existe");
			
			if (VinculoDAO.get(cdVinculo, connect)==null)
				return new Result(-2, "Erro ao salvar. Vínculo indicado não existe");
			//Verifica se o vinculo já existe. Caso exista, ele apenas atualiza
			PreparedStatement pstmtUpdate = connect.prepareStatement("SELECT B.* FROM grl_pessoa_empresa A, grl_vinculo B " +
					  										         " WHERE A.cd_vinculo = B.cd_vinculo " +
					  										         "   AND A.cd_vinculo = " + cdVinculo +
					  										         "   AND A.cd_pessoa  = " + cdPessoa +
					  										         "   AND A.cd_empresa = " + cdEmpresa);
			
			ResultSetMap rsmVinculo = new ResultSetMap(pstmtUpdate.executeQuery());
			//Se encontrar, atualizam. Se não encontrar, insere.
			if(rsmVinculo.next()){
				if (PessoaEmpresaDAO.update(new PessoaEmpresa(cdEmpresa, cdPessoa, cdVinculo, dtValidade, stVinculo), connect) <= 0) {
					if(isConnectionNull)
						connect.rollback();
					return new Result(-3, "Erro ao tentar Atualizar dados do vínculo!");
				}					
				return new Result(1, "Vínculo atualizado com sucesso...");
			}else{
				if (PessoaEmpresaDAO.insert(new PessoaEmpresa(cdEmpresa, cdPessoa, cdVinculo, dtValidade, stVinculo), connect) <= 0) {
					if(isConnectionNull)
						connect.rollback();
					return new Result(-3, "Erro ao tentar Salvar dados do vínculo!");
				}
				else {
					/*
					 * Se o vinculo adicionado for de CORRESPONDENTE e se a pessoa não for pessoa 
					 * de um orgão, cria o orgão.
					 */
					Pessoa pessoa = PessoaDAO.get(cdPessoa, connect);
					int cdOrgao = 0;
					if(cdVinculo==ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CORRESPONDENTE", 0, 0, connect)) {
						PreparedStatement pstmt = connect.prepareStatement("SELECT cd_orgao FROM prc_orgao"
								+ " WHERE cd_pessoa=?");
						pstmt.setInt(1, cdPessoa);
						
						ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
						if(!rsm.next()) {
							Orgao correspondente = new Orgao(0, 0, pessoa.getNmPessoa(), null, 0, pessoa.getCdPessoa(), 0);
							int retorno = OrgaoDAO.insert(correspondente, connect);
							if(retorno<0) {
								if(isConnectionNull)
									connect.rollback();
								return new Result(-4, "Erro ao criar registro de orgão/correspondente.");
							}
							cdOrgao = retorno;
						}
					}
					else if(cdVinculo==ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_PROFESSOR", 0)) {
						PreparedStatement pstmt = connect.prepareStatement("SELECT cd_professor FROM acd_professor"
								+ " WHERE cd_professor=?");
						pstmt.setInt(1, cdPessoa);
						ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
						if(!rsm.next()) {
							Professor professor = new Professor();
							professor.setCdProfessor(cdPessoa);
							professor.setCdPessoa(cdPessoa);
							if(ProfessorDAO.insert(professor, connect) <= 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao inserir professor");
							}
						}
						
						pstmt = connect.prepareStatement("SELECT cd_pessoa FROM srh_dados_funcionais"
								+ " WHERE cd_pessoa=?");
						pstmt.setInt(1, cdPessoa);
						rsm = new ResultSetMap(pstmt.executeQuery());
						if(!rsm.next()) {
							DadosFuncionais dadosFuncionais = new DadosFuncionais();
							dadosFuncionais.setCdPessoa(cdPessoa);
							dadosFuncionais.setCdFuncao(ParametroServices.getValorOfParametroAsInteger("CD_FUNCAO_PROFESSOR", 0));
							dadosFuncionais.setCdEmpresa(ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0));
							if(DadosFuncionaisDAO.insert(dadosFuncionais, connect) <= 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao inserir professor");
							}
						}
					}
					
					
					if(isConnectionNull)
						connect.commit();
					Result result = new Result(1, "Vínculo adicionado com sucesso...");
					result.addObject("CD_ORGAO", cdOrgao);
					return result;
				}
			}			
		}
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
		
	public static Result delVinculo(int cdPessoa, int cdEmpresa, int cdVinculo) {
		return delVinculo(cdPessoa, cdEmpresa, cdVinculo, null);
	}
	
	public static Result delVinculo(int cdPessoa, int cdEmpresa, int cdVinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if (EmpresaDAO.get(cdEmpresa, connect)==null)
				return new Result(-1, "Erro ao excluir. Empresa indicada não existe");
			
			if (VinculoDAO.get(cdVinculo, connect)==null)
				return new Result(-2, "Erro ao excluir. Vínculo indicado não existe");
			
			/*
			 * Excluir orgão se o vínculo for de CORRESPONDENTE
			 */
			if(cdVinculo==ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CORRESPONDENTE", 0, 0, connect)){
				PreparedStatement pstmt = connect.prepareStatement(
						"DELETE FROM prc_orgao WHERE cd_pessoa="+cdPessoa
				);
				
				try {
					pstmt.executeUpdate();
				}
				catch(Exception e) {
					if(isConnectionNull)
						connect.rollback();
					return new Result(-3, "Este correspondente está associado a outros registros e não pode ter o vínculo removido.");
				}
			}
			
			if(cdVinculo==ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_PROFESSOR", 0)){
				PreparedStatement pstmt = connect.prepareStatement(
						"DELETE FROM acd_professor WHERE cd_professor="+cdPessoa
				);
				
				try {
					pstmt.executeUpdate();
				}
				catch(Exception e) {
					if(isConnectionNull)
						connect.rollback();
					return new Result(-3, "Este professor está associado a outros registros e não pode ter o vínculo removido.");
				}
			}
			
			if (PessoaEmpresaDAO.delete(cdEmpresa, cdPessoa, cdVinculo, connect) <= 0) {
				if(isConnectionNull)
					connect.rollback();
				return new Result(-4, "Erro ao tentar excluir dados do vínculo!");
			}
			else {
				if(isConnectionNull)
					connect.commit();
				
				return new Result(1, "Vínculo excluído com sucesso...");
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, int cdClassificacaoCliente, float vlLimiteFactoring, float vlLimiteFactoringEmissor, float vlLimiteFactoringUnitario, int qtPrazoMinimoFactoring, int qtPrazoMaximoFactoring, int qtIdadeMinimaFactoring, float vlGanhoMinimoFactoring, float prTaxaMinimaFactoring, float vlTaxaDevolucaoFactoring, float prTaxaPadraoFactoring, float prTaxaJurosFactoring, float prTaxaProrrogacaoFactoring, int qtMaximoDocumento) {
		return insert(pessoa, endereco, cdEmpresa, cdVinculo, cdClassificacaoCliente, vlLimiteFactoring, vlLimiteFactoringEmissor, vlLimiteFactoringUnitario, qtPrazoMinimoFactoring, qtPrazoMaximoFactoring, qtIdadeMinimaFactoring, vlGanhoMinimoFactoring, prTaxaMinimaFactoring, vlTaxaDevolucaoFactoring, prTaxaPadraoFactoring, prTaxaJurosFactoring, prTaxaProrrogacaoFactoring, qtMaximoDocumento, null);
	}
	
	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, int cdClassificacaoCliente, float vlLimiteFactoring, float vlLimiteFactoringEmissor, float vlLimiteFactoringUnitario, int qtPrazoMinimoFactoring, int qtPrazoMaximoFactoring, int qtIdadeMinimaFactoring, float vlGanhoMinimoFactoring, float prTaxaMinimaFactoring, float vlTaxaDevolucaoFactoring, float prTaxaPadraoFactoring, float prTaxaJurosFactoring, float prTaxaProrrogacaoFactoring, int qtMaximoDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			Result rs = insert(pessoa, endereco, null, cdEmpresa, cdVinculo, null/*FormacaoAcademica*/, 0/*Area conhecimento*/, null/*Atributos*/, 0/*cdTipoDocumento*/, null/*nrDocumento*/, null/*conexão*/);
			if(rs.getCode()<0){
				Conexao.rollback(connect);
				return new Result(-1, rs.getMessage());
			}
			Cliente cliente = new Cliente();
			cliente.setCdEmpresa(cdEmpresa);
			cliente.setCdPessoa(rs.getCode());
			cliente.setLgLimiteCredito(1);
			cliente.setVlLimiteFactoring(vlLimiteFactoring);
			cliente.setVlLimiteFactoringEmissor(vlLimiteFactoringEmissor);
			cliente.setVlLimiteFactoringUnitario(vlLimiteFactoringUnitario);
			cliente.setQtPrazoMaximoFactoring(qtPrazoMaximoFactoring);
			cliente.setQtPrazoMinimoFactoring(qtPrazoMinimoFactoring);
			cliente.setQtIdadeMinimaFactoring(qtIdadeMinimaFactoring);
			cliente.setVlGanhoMinimoFactoring(vlGanhoMinimoFactoring);
			cliente.setPrTaxaMinimaFactoring(prTaxaMinimaFactoring);
			cliente.setVlTaxaDevolucaoFactoring(vlTaxaDevolucaoFactoring);
			cliente.setPrTaxaPadraoFactoring(prTaxaPadraoFactoring);
			cliente.setPrTaxaJurosFactoring(prTaxaJurosFactoring);
			cliente.setPrTaxaProrrogacaoFactoring(prTaxaProrrogacaoFactoring);
			cliente.setQtMaximoDocumento(qtMaximoDocumento);
			cliente.setCdClassificacaoCliente(cdClassificacaoCliente);
			
			//System.out.println(ClienteDAO.get(cdEmpresa, rs.getCode()));
			if(ClienteDAO.get(cdEmpresa, rs.getCode()) == null)
				if(ClienteDAO.insert(cliente, connect) <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir o cliente");
				}
			else
				if(ClienteDAO.update(cliente, connect) <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir o cliente");
				}
			if(rs.getCode()<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			rs.addObject("cliente", cliente);
			
			return rs;
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
	
	public static Result insertEmitente(int cdEmpresa, String nmEmitente, int stCadastro, String nrCpfCnpj) {
		return insertEmitente(cdEmpresa, nmEmitente, stCadastro, nrCpfCnpj, null);
	}
	
	public static Result insertEmitente(int cdEmpresa, String nmEmitente, int stCadastro, String nrCpfCnpj, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = 0;
			nrCpfCnpj = Util.limparFormatos(nrCpfCnpj);
			if(nrCpfCnpj.trim().length() == 11){
				PessoaFisica pessoaFisica = new PessoaFisica();
				pessoaFisica.setNmPessoa(nmEmitente.toUpperCase());
				pessoaFisica.setStCadastro(stCadastro);
				pessoaFisica.setNrCpf(nrCpfCnpj);
				pessoaFisica.setDtCadastro(Util.getDataAtual());
				pessoaFisica.setGnPessoa(PessoaServices.TP_FISICA);
				code = PessoaFisicaDAO.insert(pessoaFisica, connect);
				if(code <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro 2 ao cadastrar emitente!");
				}
				
			}
			
			else if(nrCpfCnpj.trim().length() == 14){
				PessoaJuridica pessoaJuridica = new PessoaJuridica();
				pessoaJuridica.setNmPessoa(nmEmitente.toUpperCase());
				pessoaJuridica.setStCadastro(stCadastro);
				pessoaJuridica.setNrCnpj(nrCpfCnpj);
				pessoaJuridica.setDtCadastro(Util.getDataAtual());
				pessoaJuridica.setGnPessoa(PessoaServices.TP_JURIDICA);
				code = PessoaJuridicaDAO.insert(pessoaJuridica, connect);
				if(code <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro 3 ao cadastrar emitente!");
				}
				
			}
			
			else{
				Conexao.rollback(connect);
				return new Result(-1, "Cpf/Cnpj inválido!");
			}
			
			PessoaEmpresa pessoaEmp = new PessoaEmpresa();
			pessoaEmp.setCdEmpresa(cdEmpresa);
			pessoaEmp.setCdPessoa(code);
			pessoaEmp.setCdVinculo(ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_EMITENTE", 0));
			pessoaEmp.setDtVinculo(Util.getDataAtual());
			pessoaEmp.setStVinculo(PessoaEmpresaServices.ST_ATIVO);
			if(PessoaEmpresaDAO.insert(pessoaEmp, connect) <= 0){
				Conexao.rollback(connect);
				return new Result(-1, "Erro 4 ao cadastrar emitente!");
			}
			
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(code, "Emitente cadastrado!");
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
	
	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo, ArrayList<ItemComparator> criterios, Cliente cliente) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		int lgPermiteLimiteUltrapassado = 0;
		int lgPedeSenhaSupervisor       = 0;
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("lgPermiteLimiteUltrapassado")){
				lgPermiteLimiteUltrapassado = Integer.valueOf(criterios.get(i).getValue());				
				parametros.put("lgPermiteLimiteUltrapassado", lgPermiteLimiteUltrapassado);
			}else if(((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("lgPedeSenhaSupervisor")){
				lgPedeSenhaSupervisor = Integer.valueOf(criterios.get(i).getValue());				
				parametros.put("lgPedeSenhaSupervisor", lgPedeSenhaSupervisor);
			}
		}
		return insert(pessoa, endereco, socio, cdEmpresa, cdVinculo, null/*FormacaoAcademica*/, 0/*Area conhecimento*/, null/*Atributos*/, 
				cliente, null /*pessoaCobranca*/, null /*pessoaConjuge*/, parametros, null /*lista*/, 0/*cdTipoDocumento*/, 
				null/*nrDocumento*/, null/*pessoaContaBancaria*/, null/*dadosFuncionais*/, null/*fichaMedica*/, null/*conexão*/);
	}
	
	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo) {
		return insert(pessoa, endereco, socio, cdEmpresa, cdVinculo, null/*FormacaoAcademica*/, 0/*Area conhecimento*/, null/*Atributos*/, 0/*cdTipoDocumento*/, null/*nrDocumento*/, null/*conexão*/);
	}
	
	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo) {
		return insert(pessoa, endereco, null/*Socio*/, cdEmpresa, cdVinculo, null/*FormacaoAcademica*/, 0/*Area conhecimento*/, null/*Atributos*/, 0/*cdTipoDocumento*/, null/*nrDocumento*/, null/*conexão*/);
	}
	
	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, Cliente cliente, Pessoa pessoaCobranca, HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista) {
		return insert(pessoa, endereco, null/*Socio*/, cdEmpresa, cdVinculo, null/*FormacaoAcademica*/, 0/*Area conhecimento*/, null/*Atributos*/, cliente, pessoaCobranca, null, parametros, lista, 0/*cdTipoDocumento*/, null/*nrDocumento*/, null/*conexão*/);
	}

	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, Cliente cliente, Pessoa pessoaCobranca, Pessoa pessoaConjuge, HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista) {
		return insert(pessoa, endereco, null/*Socio*/, cdEmpresa, cdVinculo, null/*FormacaoAcademica*/, 0/*Area conhecimento*/, null/*Atributos*/, cliente, pessoaCobranca, pessoaConjuge, parametros, lista, 0/*cdTipoDocumento*/, null/*nrDocumento*/, null/*conexão*/);
	}
	
	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, Cliente cliente, HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista) {
		return insert(pessoa, endereco, null/*Socio*/, cdEmpresa, cdVinculo, null/*FormacaoAcademica*/, 0/*Area conhecimento*/, null/*Atributos*/, cliente, null, null, parametros, lista, 0/*cdTipoDocumento*/, null/*nrDocumento*/, null/*conexão*/);
	}

	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, Connection connection) {
		return insert(pessoa, endereco, null/*Socio*/, cdEmpresa, cdVinculo, null/*FormacaoAcademica*/, 0/*Area conhecimento*/, null/*Atributos*/, 0/*cdTipoDocumento*/, null/*nrDocumento*/, connection/*conexão*/);
	}

	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, int cdTipoDocumento, String nrDocumento) {
		return insert(pessoa, endereco, null/*Socio*/, cdEmpresa, cdVinculo, null/*FormacaoAcademica*/, 0/*Area conhecimento*/, null/*Atributos*/, cdTipoDocumento, nrDocumento, null/*conexão*/);
	}

	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, ArrayList<FormularioAtributoValor> atributos) {
		return insert(pessoa, endereco, null/*Socio*/, cdEmpresa, cdVinculo, null/*FormacaoAcademica*/, 0/*Area conhecimento*/, atributos, 0/*cdTipoDocumento*/, null/*nrDocumento*/, null/*conexão*/);
	}
	
	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, ArrayList<FormularioAtributoValor> atributos, Cliente cliente, Pessoa pessoaCobranca, Pessoa pessoaConjuge, HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista) {
		return insert(pessoa, endereco, null/*Socio*/, cdEmpresa, cdVinculo, null/*FormacaoAcademica*/, 0/*Area conhecimento*/, atributos, cliente, pessoaCobranca, pessoaConjuge, parametros, lista, 0/*cdTipoDocumento*/, null/*nrDocumento*/, null/*conexão*/);
	}

	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, ArrayList<FormularioAtributoValor> atributos, Cliente cliente, Pessoa pessoaCobranca, HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista) {
		return insert(pessoa, endereco, null/*Socio*/, cdEmpresa, cdVinculo, null/*FormacaoAcademica*/, 0/*Area conhecimento*/, atributos, cliente, pessoaCobranca, null, parametros, lista, 0/*cdTipoDocumento*/, null/*nrDocumento*/, null/*conexão*/);
	}

	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, ArrayList<FormularioAtributoValor> atributos, Cliente cliente, HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista) {
		return insert(pessoa, endereco, null/*Socio*/, cdEmpresa, cdVinculo, null/*FormacaoAcademica*/, 0/*Area conhecimento*/, atributos, cliente, null, null, parametros, lista, 0/*cdTipoDocumento*/, null/*nrDocumento*/, null/*conexão*/);
	}

	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, Formacao formacaoAcad, int cdAreaConhecimento) {
		return insert(pessoa, endereco, null/*Socio*/, cdEmpresa, cdVinculo, formacaoAcad, cdAreaConhecimento, null/*Atributos*/, 0/*cdTipoDocumento*/, null/*nrDocumento*/, null/*conexão*/);
	}

	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo,
	            Formacao formAcademica, int cdAreaConhecimento, ArrayList<FormularioAtributoValor> atributos,
	            int cdTipoDocumento, String nrDocumento, Connection connection) {
		return insert(pessoa, endereco, socio, cdEmpresa, cdVinculo, formAcademica, cdAreaConhecimento, atributos,
		cdTipoDocumento, nrDocumento, null/*pessoaContaBancaria*/, null/*dadosFuncionais*/, connection);
	}
	
	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo,
			                    Formacao formAcademica, int cdAreaConhecimento, ArrayList<FormularioAtributoValor> atributos, Cliente cliente, Pessoa pessoaCobranca, Pessoa pessoaConjuge, HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista,
			                    int cdTipoDocumento, String nrDocumento, Connection connection) {
		return insert(pessoa, endereco, socio, cdEmpresa, cdVinculo, formAcademica, cdAreaConhecimento, atributos, cliente, pessoaCobranca, pessoaConjuge, parametros, lista,
                cdTipoDocumento, nrDocumento, null/*pessoaContaBancaria*/, null/*dadosFuncionais*/, null/*fichaMedica*/, connection);
	}
	
	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo,
					            Formacao formAcademica, int cdAreaConhecimento, ArrayList<FormularioAtributoValor> atributos, Cliente cliente, Pessoa pessoaCobranca, HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista,
					            int cdTipoDocumento, String nrDocumento, Connection connection) {
					return insert(pessoa, endereco, socio, cdEmpresa, cdVinculo, formAcademica, cdAreaConhecimento, atributos, cliente, pessoaCobranca, null, parametros, lista,
					cdTipoDocumento, nrDocumento, null/*pessoaContaBancaria*/, null/*dadosFuncionais*/, null/*fichaMedica*/, connection);
	}

	
	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo,
			Formacao formAcademica, int cdAreaConhecimento, ArrayList<FormularioAtributoValor> atributos,
			int cdTipoDocumento, String nrDocumento, PessoaContaBancaria pessoaContaBancaria, Connection connection) {
		return insert(pessoa, endereco, socio, cdEmpresa, cdVinculo, formAcademica, cdAreaConhecimento, atributos, null, null, null, null, null,
                cdTipoDocumento, nrDocumento, pessoaContaBancaria,  null/*dadosFuncionais*/, null/*fichaMedica*/, connection);
	}
	
	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo,
			Formacao formAcademica, int cdAreaConhecimento, ArrayList<FormularioAtributoValor> atributos,
			int cdTipoDocumento, String nrDocumento, PessoaContaBancaria pessoaContaBancaria, DadosFuncionais dadosFuncionais, Connection connection) {
		return insert(pessoa, endereco, socio, cdEmpresa, cdVinculo, formAcademica, cdAreaConhecimento, atributos, null, null, null, null, null,
				cdTipoDocumento, nrDocumento, pessoaContaBancaria, dadosFuncionais, null/*fichaMedica*/, connection);
	}
	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo,
			Formacao formAcademica, int cdAreaConhecimento, ArrayList<FormularioAtributoValor> atributos, Cliente cliente, Pessoa pessoaCobranca, Pessoa pessoaConjuge,HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista,
			int cdTipoDocumento, String nrDocumento, PessoaContaBancaria pessoaContaBancaria, DadosFuncionais dadosFuncionais, Connection connection) {
		return insert(pessoa, endereco, socio, cdEmpresa, cdVinculo, formAcademica, cdAreaConhecimento, atributos, cliente, pessoaCobranca, pessoaConjuge, parametros, lista,
				cdTipoDocumento, nrDocumento, pessoaContaBancaria, dadosFuncionais, null/*fichaMedica*/, connection);
	}
	public static Result insert(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo,
				Formacao formAcademica, int cdAreaConhecimento, ArrayList<FormularioAtributoValor> atributos, Cliente cliente, Pessoa pessoaCobranca, Pessoa pessoaConjuge,HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista,
				int cdTipoDocumento, String nrDocumento, PessoaContaBancaria pessoaContaBancaria, DadosFuncionais dadosFuncionais, PessoaFichaMedica pessoaFichaMedica, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			//:TODO Verificar se acao eh valida, 
			// Verificar com Hugo finalidade desta regra
//			int cdVinculoCliente = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0);
//			int cdVinculoEmitente = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_EMITENTE", 0);
//			if(cdVinculo != 0 && (cdVinculoCliente == cdVinculo || cdVinculoEmitente == cdVinculo)){
//				pessoa.setStCadastro(ST_DESBLOQUEADO);
//			}
			int cdPessoa = pessoa.getCdPessoa();
			
			/*
			 * Valida duplicidade/cpf/cnpj
			 */
			if(cdPessoa <= 0)	{
				PreparedStatement pstmt = connection.prepareStatement("SELECT nm_pessoa FROM grl_pessoa A " +
                    								                  "WHERE nm_pessoa = ?");
				pstmt.setString(1, pessoa.getNmPessoa());
				
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				if(rsm.next()) {
					
					if(pessoa instanceof PessoaJuridica && 
					   rsm.getInt("GN_PESSOA") == TP_JURIDICA) {
						return new Result(-1, "Empresa já cadastrada!");
					}
					
					if(pessoa instanceof PessoaFisica && 
					   ((PessoaFisica)pessoa).getDtNascimento() != null &&
					   ((PessoaFisica)pessoa).getDtNascimento() == rsm.getGregorianCalendar("DT_NASCIMENTO")){
						return new Result(-1, "Pessoa já cadastrada!");
					}
				}
				
				/*
				 * Valida e verifica o CPF ou CPNJ
				 */
				Result result = validateCpfCnpj(pessoa, connection);
				if(result.getCode()<=0)
					return result;
			}
			
			/*
			 * PESSOA FÍSICA
			 */
			if (pessoa instanceof PessoaFisica) {
				// Pessoa física
				if ((cdPessoa = PessoaFisicaDAO.insert((PessoaFisica)pessoa, connection))<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar salvar dados da pessoa física!");
				}
				// Aluno
				if (pessoa instanceof Aluno)
					if ((cdPessoa = AlunoDAO.insert((Aluno)pessoa, connection)) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar salvar dados do aluno!");
					}
			}
			/*
			 * PESSOA JURÍDICA
			 */
			else if (pessoa instanceof PessoaJuridica) {
				if ((cdPessoa = PessoaJuridicaDAO.insert((PessoaJuridica)pessoa, connection))<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar salvar dados da pessoa jurídica!");
				}
			}
			else if ((cdPessoa = PessoaDAO.insert(pessoa, connection))<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao tentar salvar de pessoa!");
			}
			pessoa.setCdPessoa(cdPessoa);
			/*
			 * VÍNCULO
			 */
			if (cdEmpresa > 0 && cdVinculo > 0) {
				if (PessoaEmpresaDAO.insert(new PessoaEmpresa(cdEmpresa, cdPessoa, cdVinculo, new GregorianCalendar(), PessoaEmpresaServices.ST_ATIVO), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar salvar dados do vínculo!");
				}
			}
			/*
			 * ENDEREÇO
			 */
			if (endereco != null) {
				endereco.setCdPessoa(cdPessoa);
				endereco.setLgPrincipal(1); // ENDERECO PRINCIPAL
				endereco.setCdEndereco(PessoaEnderecoDAO.insert(endereco, connection));
				if (endereco.getCdEndereco() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar salvar endereço!");
				}
			}
			
			/*
			 * PESSOA CONTA BANCARIA
			 */
			if (pessoaContaBancaria != null) {
				pessoaContaBancaria.setCdPessoa(cdPessoa);
				pessoaContaBancaria.setStConta(1);
				pessoaContaBancaria.setLgPrincipal(1);
				
				Result rPcb = PessoaContaBancariaServices.save(pessoaContaBancaria, connection);
				
//				pessoaContaBancaria.setCdContaBancaria(PessoaContaBancariaDAO.insert(pessoaContaBancaria, connection));
				if (rPcb.getCode()<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar salvar a conta bancária!");
				}
				pessoaContaBancaria = (PessoaContaBancaria)rPcb.getObjects().get("PESSOACONTABANCARIA");
			}
			
			/*
			 * DADOS FUNCIONAIS
			 */
			if (dadosFuncionais != null) {
				dadosFuncionais.setCdPessoa(pessoa.getCdPessoa()!=0?pessoa.getCdPessoa():0);
				Result r = (DadosFuncionaisServices.save(dadosFuncionais, connection));
				if (r.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar salvar os dados funcionais!");
				}
				else
					dadosFuncionais.setCdMatricula(r.getCode());
				
			}
			
			

			/*
			 * DOCUMENTO
			 */

			if(cdTipoDocumento > 0 && nrDocumento != null && !nrDocumento.equals("")){

				TipoDocumentoPessoaDAO.insert(new TipoDocumentoPessoa(cdTipoDocumento, cdPessoa, nrDocumento));

			}

			if (atributos != null) {
				/* configura os atributos do produto */
				for (int i = 0; i < atributos.size(); i++) {
					FormularioAtributoValor atributo = atributos.get(i);
					atributo.setCdPessoa(cdPessoa);
					if (FormularioAtributoValorDAO.insert(atributo, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar salvar atributos dinamicos!");
					}
				}
			}
			/*
			 * Sócio
			 */
			if (socio != null) {				
				socio.setCdPessoa(cdPessoa);
				int ret = SocioDAO.insert(socio, connection);					
				if (ret < 0) {					
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar gravar sócio!");
				}
			}
			
			/*
			 * Cliente
			 */
			if(cliente != null){
				cliente.setCdPessoa(cdPessoa);
				int ret = ClienteDAO.insert(cliente, connection);					
				if (ret < 0) {					
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar gravar cliente!");
				}
			}
			
			/*
			 * Pessoa Cobranca
			 */
			if(pessoaCobranca != null){
				if(pessoaCobranca.getDtCadastro() == null){
					pessoa.setDtCadastro(Util.getDataAtual());
				}
				if(pessoaCobranca.getCdPessoa() > 0){
					if(pessoaCobranca instanceof PessoaFisica){
						int ret = PessoaFisicaDAO.update((PessoaFisica)pessoaCobranca, connection);					
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar atualizar pessoa cobranca!");
						}
						cliente.setCdPessoaCobranca(pessoaCobranca.getCdPessoa());
						ret = ClienteDAO.update(cliente, connection);					
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar atualizar pessoa cobrança de cliente!");
						}
					}
					
					else if(pessoaCobranca instanceof PessoaJuridica){
						int ret = PessoaJuridicaDAO.update((PessoaJuridica)pessoaCobranca, connection);					
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar atualizar pessoa cobranca!");
						}
						cliente.setCdPessoaCobranca(pessoaCobranca.getCdPessoa());
						ret = ClienteDAO.update(cliente, connection);					
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar atualizar pessoa cobrança de cliente!");
						}
					}
				}
				else{
					if(pessoaCobranca instanceof PessoaFisica){
						int ret = PessoaFisicaDAO.insert((PessoaFisica)pessoaCobranca, connection);					
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar gravar pessoa cobranca!");
						}
						cliente.setCdPessoaCobranca(ret);
						ret = ClienteDAO.update(cliente, connection);					
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar atualizar pessoa cobrança de cliente!");
						}
					}
					
					else if(pessoaCobranca instanceof PessoaJuridica){
						int ret = PessoaJuridicaDAO.insert((PessoaJuridica)pessoaCobranca, connection);					
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar gravar pessoa cobranca!");
						}
						cliente.setCdPessoaCobranca(ret);
						ret = ClienteDAO.update(cliente, connection);					
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar atualizar pessoa cobrança de cliente!");
						}
					}
				}
			}
			
			/*
			 * Pessoa Conjuge
			 */
			if(pessoaConjuge != null){
				if(pessoaConjuge.getCdPessoa() != 0){
					int ret = PessoaFisicaDAO.update((PessoaFisica)pessoaConjuge, connection);					
					if (ret < 0) {					
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar atualizar pessoa conjuge!");
					}
					((PessoaFisica)pessoa).setCdConjuge(pessoaConjuge.getCdPessoa());
					ret = PessoaFisicaDAO.update((PessoaFisica)pessoa, connection);					
					if (ret < 0) {					
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar atualizar pessoa conjuge de fisica!");
					}
				}
				else{
					int ret = PessoaFisicaDAO.insert((PessoaFisica)pessoaConjuge, connection);					
					if (ret < 0) {					
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar gravar pessoa conjuge!");
					}
					((PessoaFisica)pessoa).setCdConjuge(ret);
					ret = PessoaFisicaDAO.update((PessoaFisica)pessoa, connection);					
					if (ret < 0) {					
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar atualizar pessoa conjuge de pessoa fisica!");
					}
						
				}
			}
			
			/*
			 * Limites 
			 */
			if(lista != null){
				for(int i = 0; i < lista.size(); i++){
					int cdCombustivel = Integer.parseInt((String)lista.get(i).get("cdCombustivel"));
					float vlLimite  = (Float)lista.get(i).get("vlLimite");
					
					if(ClienteProdutoDAO.insert(new ClienteProduto(cdEmpresa, cdCombustivel, cdPessoa, 0/*qtLimite*/, vlLimite, cliente.getCdTabelaPreco(), 0), connection) <= 0){
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar inserir Limite!");
					}
					
				}
			}
			
			/*
			 * Parametros 
			 */
			if(parametros != null){
				for(String key : parametros.keySet()){
					int cdParametro = ParametroServices.insert(new Parametro(0, 0, key, ParametroServices.NUMERICO, ParametroServices.SINGLE_TEXT, null, null, pessoa.getCdPessoa(), ParametroServices.TP_PESSOA, 0, 0, ParametroServices.NIVEL_ACESSO_OPERADOR), null, connection);
					ParametroValor parametroValor = new ParametroValor(cdParametro, 0, 0, 0, pessoa.getCdPessoa(), null, (parametros.get(key) != null ? String.valueOf(parametros.get(key)) : "0"), null);
					if(ParametroValorDAO.insert(parametroValor, connection) <= 0){
						Conexao.rollback(connection);
						return new Result(-1, "Erro ao inserir valor em parametro!");
					}
				}
			}
			
			//Limite de credito
			float vlLimiteCredito = (cliente != null ? cliente.getVlLimiteCredito() : 0);
			if(vlLimiteCredito > 0){
				float somaLimite = 0;
				PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM adm_condicao_pagamento_cliente WHERE cd_pessoa = " + pessoa.getCdPessoa() + " AND cd_empresa = " + cdEmpresa);
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				while(rsm.next()){
					CondicaoPagamento condicaoPagamento = CondicaoPagamentoDAO.get(rsm.getInt("cd_condicao_pagamento"), connection);
					somaLimite += condicaoPagamento.getVlLimite();
				}

				pstmt = connection.prepareStatement(" SELECT vl_limite FROM adm_cliente_produto A " +
													" WHERE A.cd_pessoa = " + pessoa.getCdPessoa() +
													" AND A.cd_empresa = "  + cdEmpresa +
													" AND A.cd_produto_servico = ?");
				
				PreparedStatement pstmtCombustivel = connection.prepareStatement("SELECT B.nm_produto_servico AS nm_combustivel, B.cd_produto_servico AS cd_combustivel " +
																				  "FROM alm_produto_grupo A " +
																				  "JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) " +
																				  "WHERE A.cd_grupo = " + ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa));
				
				ResultSetMap rsmCombustivel = new ResultSetMap(pstmtCombustivel.executeQuery());
				while(rsmCombustivel.next()){
					pstmt.setInt(1, rsmCombustivel.getInt("cd_combustivel"));
					rsm = new ResultSetMap(pstmt.executeQuery());
					while(rsm.next()){
						somaLimite += rsm.getFloat("vl_limite");
					}
				}
				
				somaLimite += cliente.getVlLimiteVale();
				somaLimite += cliente.getVlLimiteFactoring();
				
				if(vlLimiteCredito < somaLimite){
					if(isConnectionNull)
						Conexao.rollback(connection);
					NumberFormat formatoPreco = NumberFormat.getCurrencyInstance();  
			        formatoPreco.setMaximumFractionDigits(2);
			        
					return new Result(-1, "A soma dos limites de combustíveis, vale, troca de cheque e condições de pagamento ultrapassou o limite geral. " +
										  "O valor do limite é "+formatoPreco.format(vlLimiteCredito)+" e a soma desses limites foi de "+formatoPreco.format(somaLimite)+"!");
				}
			}
			
			if (isConnectionNull)
				connection.commit();

			
			Result result = new Result(cdPessoa);
			result.addObject("pessoa", pessoa);
			result.addObject("endereco", endereco);
			result.addObject("dadosFuncionais", dadosFuncionais);
			if (formAcademica!=null)
				result.addObject("cdFormacao", formAcademica.getCdFormacao());
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar incluir pessoa!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * @see #inativarPessoa(int, Connection)
	 * @param cdPessoa
	 * @return
	 */
	public static Result inativarPessoa (int cdPessoa){
		return inativarPessoa(cdPessoa, null);
	}
	
	/**
	 * Método que inativa uma pessoa em caso de tentar deletar uma pessoa que contenha registro em outras tabelas.
	 * 
	 * @param cdPessoa
	 * @param connect
	 * @return
	 * @author Luiz Romario Filho
	 * @since 18/12/2014
	 */
	public static Result inativarPessoa(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		Result result;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstm = connect.prepareStatement("UPDATE grl_pessoa SET st_cadastro = ? WHERE cd_pessoa = ?");
			pstm.setInt(1, ST_INATIVO);
			pstm.setInt(2, cdPessoa);
			int executeUpdate = pstm.executeUpdate();
			if(executeUpdate > 0){
				result = new Result(1, "Registro atualizado com sucesso!");
			} else{
				result = new Result(-1, "Ocorreu um erro ao atualizar o registro!");
			}
			if (isConnectionNull)
				connect.commit();
		} catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			result = new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		return result;
	}

	public static Result update(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, int cdVinculoOld, int cdClassificacaoCliente, float vlLimiteFactoring, float vlLimiteFactoringEmissor, float vlLimiteFactoringUnitario, int qtPrazoMinimoFactoring, int qtPrazoMaximoFactoring, int qtIdadeMinimaFactoring, float vlGanhoMinimoFactoring, float prTaxaMinimaFactoring, float vlTaxaDevolucaoFactoring, float prTaxaPadraoFactoring, float prTaxaJurosFactoring, float prTaxaProrrogacaoFactoring, int qtMaximoDocumento) {
		return update(pessoa, endereco, cdEmpresa, cdVinculo, cdVinculoOld, cdClassificacaoCliente, vlLimiteFactoring, vlLimiteFactoringEmissor, vlLimiteFactoringUnitario, qtPrazoMinimoFactoring, qtPrazoMaximoFactoring, qtIdadeMinimaFactoring, vlGanhoMinimoFactoring, prTaxaMinimaFactoring, vlTaxaDevolucaoFactoring, prTaxaPadraoFactoring, prTaxaJurosFactoring, prTaxaProrrogacaoFactoring, qtMaximoDocumento, null);
	}
	
	public static Result update(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, int cdVinculoOld, int cdClassificacaoCliente, float vlLimiteFactoring, float vlLimiteFactoringEmissor, float vlLimiteFactoringUnitario, int qtPrazoMinimoFactoring, int qtPrazoMaximoFactoring, int qtIdadeMinimaFactoring, float vlGanhoMinimoFactoring, float prTaxaMinimaFactoring, float vlTaxaDevolucaoFactoring, float prTaxaPadraoFactoring, float prTaxaJurosFactoring, float prTaxaProrrogacaoFactoring, int qtMaximoDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			Result rs = update(pessoa, endereco, null, cdEmpresa, cdVinculo, cdVinculoOld, null/*FormacaoAcademica*/, 0/*Area conhecimento*/, null/*Atributos*/, 0/*cdTipoDocumento*/, null/*nrDocumento*/, null/*conexão*/);
			if(rs.getCode()<0)
				Conexao.rollback(connect);
			
			
			Cliente cliente = new Cliente();
			cliente.setCdEmpresa(cdEmpresa);
			cliente.setCdPessoa(pessoa.getCdPessoa());
			cliente.setLgLimiteCredito(1);
			cliente.setVlLimiteFactoring(vlLimiteFactoring);
			cliente.setVlLimiteFactoringEmissor(vlLimiteFactoringEmissor);
			cliente.setVlLimiteFactoringUnitario(vlLimiteFactoringUnitario);
			cliente.setQtPrazoMaximoFactoring(qtPrazoMaximoFactoring);
			cliente.setQtPrazoMinimoFactoring(qtPrazoMinimoFactoring);
			cliente.setQtIdadeMinimaFactoring(qtIdadeMinimaFactoring);
			cliente.setVlGanhoMinimoFactoring(vlGanhoMinimoFactoring);
			cliente.setPrTaxaMinimaFactoring(prTaxaMinimaFactoring);
			cliente.setVlTaxaDevolucaoFactoring(vlTaxaDevolucaoFactoring);
			cliente.setPrTaxaPadraoFactoring(prTaxaPadraoFactoring);
			cliente.setPrTaxaJurosFactoring(prTaxaJurosFactoring);
			cliente.setPrTaxaProrrogacaoFactoring(prTaxaProrrogacaoFactoring);
			cliente.setQtMaximoDocumento(qtMaximoDocumento);
			cliente.setCdClassificacaoCliente(cdClassificacaoCliente);
			int ret = ClienteDAO.update(cliente, connect);
			if(ret < 0){
				Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar o cliente");
			}
			else if(ret == 0){
				if(ClienteDAO.insert(cliente, connect) < 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir o cliente");
				}
			}
			
			if(rs.getCode()<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			rs.addObject("cliente", cliente);
			
			return rs;
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
	
	public static Result updateEmitente(int cdEmpresa, int cdPessoa, String nmEmitente, int stCadastro, String nrCpfCnpj) {
		return updateEmitente(cdEmpresa, cdPessoa, nmEmitente, stCadastro, nrCpfCnpj, null);
	}
	
	public static Result updateEmitente(int cdEmpresa, int cdPessoa, String nmEmitente, int stCadastro, String nrCpfCnpj, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			nrCpfCnpj = Util.limparTexto(nrCpfCnpj);
			if(nrCpfCnpj.trim().length() == 11){
				PessoaFisica pessoaFisica = PessoaFisicaDAO.get(cdPessoa, connect);
				if(pessoaFisica != null){
					pessoaFisica.setNmPessoa(nmEmitente.toUpperCase());
					pessoaFisica.setStCadastro(stCadastro);
					pessoaFisica.setNrCpf(nrCpfCnpj);
					pessoaFisica.setGnPessoa(PessoaServices.TP_FISICA);
					if(PessoaFisicaDAO.update(pessoaFisica, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro 2 ao atualizar emitente!");
					}
				}
				else{
					Conexao.rollback(connect);
					return new Result(-1, "Essa pessoa é jurídica, o cnpj deve ter 14 digitos!");
				}
			}
			
			else if(nrCpfCnpj.trim().length() == 14){
				PessoaJuridica pessoaJuridica = PessoaJuridicaDAO.get(cdPessoa, connect);
				if(pessoaJuridica != null){
					pessoaJuridica.setNmPessoa(nmEmitente.toUpperCase());
					pessoaJuridica.setStCadastro(stCadastro);
					pessoaJuridica.setNrCnpj(nrCpfCnpj);
					pessoaJuridica.setGnPessoa(PessoaServices.TP_JURIDICA);
					if(PessoaJuridicaDAO.update(pessoaJuridica, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro 3 ao atualizar emitente!");
					}
				}
				else{
					Conexao.rollback(connect);
					return new Result(-1, "Essa pessoa é física, o cpf deve ter 11 digitos!");
				}
				
			}
			
			else{
				Conexao.rollback(connect);
				return new Result(-1, "Cpf/Cnpj inválido!");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(cdPessoa, "Emitente atualizado!");
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
	
	public static Result update(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo, int cdVinculoOld) {
		return update(pessoa, endereco, socio, cdEmpresa, cdVinculo, cdVinculoOld, null/*Formação*/, 0/*cdAreaConhecimento*/, null/*Atributos*/, 0, null , null/*conexão*/);
	}
	
	public static Result update(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, int cdVinculoOld) {
		return update(pessoa, endereco, null, cdEmpresa, cdVinculo, cdVinculoOld, null/*Formação*/, 0/*cdAreaConhecimento*/, null/*Atributos*/, 0, null , null/*conexão*/);
	}
	
	public static Result update(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, int cdVinculoOld, Cliente cliente, Pessoa pessoaCobranca, Pessoa pessoaConjuge, HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista) {
		return update(pessoa, endereco, null, cdEmpresa, cdVinculo, cdVinculoOld, null/*Formação*/, 0/*cdAreaConhecimento*/, null/*Atributos*/, cliente, pessoaCobranca, pessoaConjuge, parametros, lista, 0, null , null/*conexão*/);
	}

	public static Result update(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, int cdVinculoOld, Cliente cliente, Pessoa pessoaCobranca, HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista) {
		return update(pessoa, endereco, null, cdEmpresa, cdVinculo, cdVinculoOld, null/*Formação*/, 0/*cdAreaConhecimento*/, null/*Atributos*/, cliente, pessoaCobranca, null, parametros, lista, 0, null , null/*conexão*/);
	}

	public static Result update(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, int cdVinculoOld, Cliente cliente, HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista) {
		return update(pessoa, endereco, null, cdEmpresa, cdVinculo, cdVinculoOld, null/*Formação*/, 0/*cdAreaConhecimento*/, null/*Atributos*/, cliente, null, null, parametros, lista, 0, null , null/*conexão*/);
	}

	public static Result update(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, int cdVinculoOld, int cdTipoDocumento, String nrDocumento) {
		return update(pessoa, endereco, null, cdEmpresa, cdVinculo, cdVinculoOld, null/*Formação*/, 0/*cdAreaConhecimento*/, null/*Atributos*/, cdTipoDocumento, nrDocumento, null/*conexão*/);
	}

	public static Result update(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, int cdVinculoOld, ArrayList<FormularioAtributoValor> atributos) {
		return update(pessoa, endereco, null, cdEmpresa, cdVinculo, cdVinculoOld, null/*Formação*/, 0/*cdAreaConhecimento*/, atributos, 0, null , null/*conexão*/);
	}
	
	public static Result update(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, int cdVinculoOld, ArrayList<FormularioAtributoValor> atributos, Cliente cliente, Pessoa pessoaCobranca, Pessoa pessoaConjuge, HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista) {
		return update(pessoa, endereco, null, cdEmpresa, cdVinculo, cdVinculoOld, null/*Formação*/, 0/*cdAreaConhecimento*/, atributos, cliente, pessoaCobranca, pessoaConjuge, parametros, lista, 0, null , null/*conexão*/);
	}

	public static Result update(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, int cdVinculoOld, ArrayList<FormularioAtributoValor> atributos, Cliente cliente, Pessoa pessoaCobranca, HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista) {
		return update(pessoa, endereco, null, cdEmpresa, cdVinculo, cdVinculoOld, null/*Formação*/, 0/*cdAreaConhecimento*/, atributos, cliente, pessoaCobranca, null, parametros, lista, 0, null , null/*conexão*/);
	}
	
	public static Result update(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, int cdVinculoOld, ArrayList<FormularioAtributoValor> atributos, Cliente cliente, HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista) {
		return update(pessoa, endereco, null, cdEmpresa, cdVinculo, cdVinculoOld, null/*Formação*/, 0/*cdAreaConhecimento*/, atributos, cliente, null, null, parametros, lista, 0, null , null/*conexão*/);
	}

	public static Result update(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, int cdVinculoOld, Formacao formacaoAcad, int cdAreaConhecimento) {
		return update(pessoa, endereco, null, cdEmpresa, cdVinculo, cdVinculoOld, formacaoAcad, cdAreaConhecimento, null /*atributos*/, 0, null , null/*conexão*/);
	}

	public static Result update(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo, int cdVinculoOld,
			Formacao formacaoAcad, int cdAreaConhecimento, ArrayList<FormularioAtributoValor> atributos, int cdTipoDocumento, String nrDocumento, Connection connection) {
		return update(pessoa, endereco, socio, cdEmpresa, cdVinculo, cdVinculoOld,
			formacaoAcad, cdAreaConhecimento, atributos, null, null, null, null, null, cdTipoDocumento, nrDocumento, null/*pessoaContaBancaria*/, null/*dadosFuncionais*/, connection);
	}
	
	public static Result update(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo, int cdVinculoOld,
			Formacao formacaoAcad, int cdAreaConhecimento, ArrayList<FormularioAtributoValor> atributos, Cliente cliente, Pessoa pessoaCobranca, Pessoa pessoaConjuge, HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista, int cdTipoDocumento, String nrDocumento, Connection connection) {
		return update(pessoa, endereco, socio, cdEmpresa, cdVinculo, cdVinculoOld,
			formacaoAcad, cdAreaConhecimento, atributos, cliente, pessoaCobranca, pessoaConjuge, parametros, lista, cdTipoDocumento, nrDocumento, null/*pessoaContaBancaria*/, null/*dadosFuncionais*/, connection);
	}
	
	public static Result update(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo, int cdVinculoOld,
			Formacao formacaoAcad, int cdAreaConhecimento, ArrayList<FormularioAtributoValor> atributos, Cliente cliente, Pessoa pessoaCobranca, HashMap<String, Object> parametros, ArrayList<HashMap<String, Object>> lista, int cdTipoDocumento, String nrDocumento, Connection connection) {
		return update(pessoa, endereco, socio, cdEmpresa, cdVinculo, cdVinculoOld,
			formacaoAcad, cdAreaConhecimento, atributos, cliente, pessoaCobranca, null, parametros, lista, cdTipoDocumento, nrDocumento, null/*pessoaContaBancaria*/, null/*dadosFuncionais*/, connection);
	}
	
	public static Result update(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo, int cdVinculoOld,
			Formacao formacaoAcad, int cdAreaConhecimento, ArrayList<FormularioAtributoValor> atributos, int cdTipoDocumento, String nrDocumento, PessoaContaBancaria pessoaContaBancaria, Connection connection) {
		return update(pessoa, endereco, socio, cdEmpresa, cdVinculo, cdVinculoOld,
				formacaoAcad, cdAreaConhecimento, atributos, null, null, null, null, null, cdTipoDocumento, nrDocumento, null/*pessoaContaBancaria*/, null/*dadosFuncionais*/, connection);
	}
	
	public static Result update(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo, int cdVinculoOld,
			Formacao formacaoAcad, int cdAreaConhecimento, ArrayList<FormularioAtributoValor> atributos, int cdTipoDocumento, String nrDocumento, PessoaContaBancaria pessoaContaBancaria, DadosFuncionais dadosFuncionais, Connection connection) {
		return update(pessoa, endereco, socio, cdEmpresa, cdVinculo, cdVinculoOld,
				formacaoAcad, cdAreaConhecimento, atributos, null, null, null, null, null, cdTipoDocumento, nrDocumento, pessoaContaBancaria, dadosFuncionais, connection);
	}
	
	public static Result update(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo, int cdVinculoOld, ArrayList<ItemComparator> criterios, Cliente cliente) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		int lgPermiteLimiteUltrapassado = 0;
		int lgPedeSenhaSupervisor       = 0;
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("lgPermiteLimiteUltrapassado")){
				lgPermiteLimiteUltrapassado = Integer.valueOf(criterios.get(i).getValue());				
				parametros.put("lgPermiteLimiteUltrapassado", lgPermiteLimiteUltrapassado);
			}else if(((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("lgPedeSenhaSupervisor")){
				lgPedeSenhaSupervisor = Integer.valueOf(criterios.get(i).getValue());				
				parametros.put("lgPedeSenhaSupervisor", lgPedeSenhaSupervisor);
			}
		}
		return update(pessoa, endereco, socio, cdEmpresa, cdVinculo, cdVinculoOld,
				null, 0, null, cliente, null, null, parametros, null, 0, null, null, null, null/*conexão*/);
	}
	
	@SuppressWarnings("deprecation")
	public static Result update(Pessoa pessoa, PessoaEndereco endereco, Socio socio, int cdEmpresa, int cdVinculo, int cdVinculoOld,
				Formacao formacaoAcad, int cdAreaConhecimento, ArrayList<FormularioAtributoValor> atributos, Cliente cliente, Pessoa pessoaCobranca, Pessoa pessoaConjuge, HashMap<String, Object> parametros, 
				ArrayList<HashMap<String, Object>> lista, int cdTipoDocumento, String nrDocumento, PessoaContaBancaria pessoaContaBancaria, DadosFuncionais dadosFuncionais, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			/*
			 * Valida e verifica o CPF ou CPNJ
			 */
			Result result = validateCpfCnpj(pessoa, connection);
			if(result.getCode()<=0)
				return result;
			

			LogUtils.debug("PessoaServices.save: Pessoa -> "+pessoa);
			/*
			 * PESSOA FÍSICA
			 */			
			if (pessoa instanceof PessoaFisica) {
				// Verifica se existe registro em pessoa jurídica
				if(connection.prepareStatement("SELECT * FROM grl_pessoa_juridica " +
                                               "WHERE cd_pessoa = "+pessoa.getCdPessoa()).executeQuery().next()) {
					//int ret = PessoaJuridicaDAO.delete(pessoa.getCdPessoa(), connection);
					
					PreparedStatement pstmt = connection.prepareStatement("DELETE FROM grl_pessoa_juridica WHERE cd_pessoa=?");
					pstmt.setInt(1, pessoa.getCdPessoa());
					int ret = pstmt.executeUpdate();
					
					if(ret <= 0)	{
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar transformar pessoa jurídica em física!");
					}
				}
				else if (PessoaFisicaDAO.update((PessoaFisica)pessoa, connection)<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar atualizar dados da pessoa física!");
				}

			}
			/*
			 * PESSOA JURÍDICA
			 */
			else if (pessoa instanceof PessoaJuridica) {
				// Verifica se existe registro em pessoa jurídica
				if(connection.prepareStatement("SELECT * FROM grl_pessoa_fisica " +
                                               "WHERE cd_pessoa = "+pessoa.getCdPessoa()).executeQuery().next()) {
					//int ret = PessoaFisicaDAO.delete(pessoa.getCdPessoa(), connection);

					PreparedStatement pstmt = connection.prepareStatement("DELETE FROM grl_pessoa_fisica WHERE cd_pessoa=?");
					pstmt.setInt(1, pessoa.getCdPessoa());
					int ret = pstmt.executeUpdate();

					if(ret <= 0)	{
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar transformar pessoa física em jurídica!");
					}
				}

				if (PessoaJuridicaDAO.update((PessoaJuridica)pessoa, connection)<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao atualizar dados de pessoa jurídica!");
				}

			}
			/*
			 * PESSOA
			 */
			if (PessoaDAO.update(pessoa, connection)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao atualizar dados de pessoa!");
			}

			if (cdEmpresa!=0 && cdVinculo!=0) {
				if (PessoaEmpresaDAO.update(new PessoaEmpresa(cdEmpresa, pessoa.getCdPessoa(), cdVinculo, new GregorianCalendar(), PessoaEmpresaServices.ST_ATIVO), cdEmpresa, pessoa.getCdPessoa(), cdVinculoOld, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao atualizar dados do vínculo!");
				}
			}

			/*
			 * ENDERECO
			 */
			if (endereco != null) {
				int cdEndereco = 0;
				if (endereco.getCdEndereco() == 0) {
					if ((cdEndereco = PessoaEnderecoDAO.insert(endereco, connection)) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao atualizar dados do endereço!");
					}
					endereco.setCdEndereco(cdEndereco);
				}
				else if (PessoaEnderecoDAO.update(endereco, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao atualizar dados do endereço!");
				}
				if(endereco.getLgPrincipal()==1)
					connection.prepareStatement("UPDATE grl_pessoa_endereco SET lg_principal = 0 " +
							                    "WHERE cd_pessoa    = "+pessoa.getCdPessoa()+
							                    "  AND cd_endereco <> "+endereco.getCdEndereco()).executeUpdate();
			}
			
			/*
			 * PESSOA CONTA BANCARIA
			 */
			if (pessoaContaBancaria != null) {
//				int cdContaBancaria = 0;
//				pessoaContaBancaria.setCdPessoa(pessoa.getCdPessoa()!=0?pessoa.getCdPessoa():0);
//				if(pessoaContaBancaria.getLgPrincipal()<1)
//					pessoaContaBancaria.setLgPrincipal(1);
//				if (pessoaContaBancaria.getCdContaBancaria() == 0) {
//					if ((cdContaBancaria = PessoaContaBancariaDAO.insert(pessoaContaBancaria, connection)) <= 0) {
//						if (isConnectionNull)
//							Conexao.rollback(connection);
//						return new Result(-1, "Erro ao inserir dados da conta bancária!");
//					}
//					pessoaContaBancaria.setCdContaBancaria(cdContaBancaria);
//				}
//				else if (PessoaContaBancariaDAO.update(pessoaContaBancaria, connection) <= 0) {
//					if (isConnectionNull)
//						Conexao.rollback(connection);
//					return new Result(-1, "Erro ao atualizar dados da conta bancária!");
//				}
				Result rPcb = PessoaContaBancariaServices.save(pessoaContaBancaria, connection);
				if (rPcb.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar salvar dados de conta bancária.");
				}
			}
			
			/*
			 * DADOS FUNCIONAIS
			 */
			if (dadosFuncionais != null) {
				Result r = (DadosFuncionaisServices.save(dadosFuncionais, connection));
				if (r.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar atualizar os dados funcionais!");
				}
				dadosFuncionais.setCdMatricula(r.getCode());
			}
			
			/*
			 * FORMACAO ACADEMICA
			 */
			int cdFormacao = formacaoAcad==null ? 0 : formacaoAcad.getCdFormacao();
			if (formacaoAcad!=null) {
				if (cdFormacao<=0) {
					if ((cdFormacao = FormacaoServices.insert(formacaoAcad, connection)) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao atualizar dados do formação acadêmica!");
					}
					formacaoAcad.setCdFormacao(cdFormacao);
				}
				else if (FormacaoServices.update(formacaoAcad, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao atualizar dados do formação acadêmica!");
				}
			}
			/*
			 * DOCUMENTO
			 */
			if(cdTipoDocumento > 0 && nrDocumento != null && !nrDocumento.equals("")){
				if(TipoDocumentoPessoaDAO.get(cdTipoDocumento, pessoa.getCdPessoa()) != null)
					TipoDocumentoPessoaDAO.update(new TipoDocumentoPessoa(cdTipoDocumento, pessoa.getCdPessoa(), nrDocumento));
				else
					TipoDocumentoPessoaDAO.insert(new TipoDocumentoPessoa(cdTipoDocumento, pessoa.getCdPessoa(), nrDocumento));
			}
			
			/*
			 * Cliente
			 */
			if(cliente != null){
				if(cliente.getCdPessoa() > 0){
					int ret = ClienteDAO.update(cliente, connection);					
					if (ret < 0) {					
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar atualizar cliente!");
					}
					else if(ret == 0){
						ret = ClienteDAO.insert(cliente, connection);					
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar inserir cliente!");
						}
					}
				}
				else{
					cliente.setCdPessoa(pessoa.getCdPessoa());
					int ret = ClienteDAO.insert(cliente, connection);					
					if (ret < 0) {					
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar inserir cliente!");
					}
				}
				
				
			}
			
			/*
			 * Pessoa Cobranca
			 */
			if(pessoaCobranca != null){
				if(pessoaCobranca.getDtCadastro() == null){
					pessoa.setDtCadastro(Util.getDataAtual());
				}
				if(pessoaCobranca.getCdPessoa() > 0){
					if(pessoaCobranca instanceof PessoaFisica){
						int ret = PessoaFisicaDAO.update((PessoaFisica)pessoaCobranca, connection);					
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar atualizar pessoa cobranca!");
						}
						cliente.setCdPessoaCobranca(pessoaCobranca.getCdPessoa());
						ret = ClienteDAO.update(cliente, connection);					
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar atualizar pessoa cobrança de cliente!");
						}
					}
					
					else if(pessoaCobranca instanceof PessoaJuridica){
						int ret = PessoaJuridicaDAO.update((PessoaJuridica)pessoaCobranca, connection);
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar atualizar pessoa cobranca!");
						}
						cliente.setCdPessoaCobranca(pessoaCobranca.getCdPessoa());
						ret = ClienteDAO.update(cliente, connection);					
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar atualizar pessoa cobrança de cliente!");
						}
					}
				}
				else{
					if(pessoaCobranca instanceof PessoaFisica){
						int ret = PessoaFisicaDAO.insert((PessoaFisica)pessoaCobranca, connection);					
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar gravar pessoa cobranca!");
						}
						cliente.setCdPessoaCobranca(ret);
						ret = ClienteDAO.update(cliente, connection);					
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar atualizar pessoa cobrança de cliente!");
						}
					}
					
					else if(pessoaCobranca instanceof PessoaJuridica){
						int ret = PessoaJuridicaDAO.insert((PessoaJuridica)pessoaCobranca, connection);
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar gravar pessoa cobranca!");
						}
						cliente.setCdPessoaCobranca(ret);
						ret = ClienteDAO.update(cliente, connection);					
						if (ret < 0) {					
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar atualizar pessoa cobrança de cliente!");
						}
					}
				}
			}
			
			/*
			 * Pessoa Conjuge
			 */
			if(pessoaConjuge != null){
				if(pessoaConjuge.getCdPessoa() > 0){
					int ret = PessoaFisicaDAO.update((PessoaFisica)pessoaConjuge, connection);					
					if (ret < 0) {					
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar atualizar pessoa conjuge!");
					}
					((PessoaFisica)pessoa).setCdConjuge(pessoaConjuge.getCdPessoa());
					ret = PessoaFisicaDAO.update((PessoaFisica)pessoaConjuge, connection);					
					if (ret < 0) {					
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar atualizar pessoa!");
					}
				}
				else{
					int ret = PessoaFisicaDAO.insert((PessoaFisica)pessoaConjuge, connection);					
					if (ret < 0) {					
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar gravar pessoa conjuge!");
					}
					((PessoaFisica)pessoa).setCdConjuge(ret);
					ret = PessoaFisicaDAO.update((PessoaFisica)pessoaConjuge, connection);					
					if (ret < 0) {					
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar atualizar pessoa!");
					}
					
				}
			}

			/*
			 * Limites 
			 */
			if(lista != null){
				for(int i = 0; i < lista.size(); i++){
					int cdCombustivel = (Integer.parseInt((String)lista.get(i).get("cdCombustivel")));
					float vlLimite  = (Float)lista.get(i).get("vlLimite");		
					
					ClienteProduto clienteProduto = ClienteProdutoDAO.get(cdEmpresa, cdCombustivel, pessoa.getCdPessoa(), connection);
					if(clienteProduto != null){
						clienteProduto.setVlLimite(vlLimite);
						if(ClienteProdutoDAO.update(clienteProduto, connection) < 0){
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar alterar Limite!");
						}
					}
					else{
						if(ClienteProdutoDAO.insert(new ClienteProduto(cdEmpresa, cdCombustivel, pessoa.getCdPessoa(), 0/*qtLimite*/, vlLimite, cliente.getCdTabelaPreco(), 0), connection) <= 0){
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar inserir Limite!");
						}
					}
					
				}
					
					
					
					
			}
			
			/*
			 * Parametros 
			 */
			
			if(parametros != null){
				for(String key : parametros.keySet()){
					Parametro parametro = ParametroServices.getByNameCdPessoa(key, pessoa.getCdPessoa());
					if(parametro != null){
						if(key.equals("lgConveniados")){
							ResultSetMap rsmConveniados = new ResultSetMap(connection.prepareStatement("SELECT * FROM adm_cliente where cd_convenio = " + pessoa.getCdPessoa()).executeQuery());
							if(rsmConveniados.next() && (((Integer)parametros.get(key)) == 0)){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return new Result(-1, "Este parceiro possui conveniados ligados a ele. O parâmetro não pode ser alterado!");
							}
						}
						
						int cdParametro = parametro.getCdParametro();
						ParametroValor parametroValor = null;		
						ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_parametro", "" + cdParametro, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_pessoa", "" + pessoa.getCdPessoa(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsm = ParametroValorDAO.find(criterios, connection);
						if(rsm.next()){
							parametroValor = ParametroValorDAO.get(rsm.getInt("cd_parametro"), rsm.getInt("cd_valor"), connection);
							parametroValor.setVlInicial(String.valueOf(parametros.get(key)));
							if(ParametroValorDAO.update(parametroValor, connection) <= 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return new Result(-1, "Erro ao atualizar valor em parametro!");
							}
						}
						else{
							parametroValor = new ParametroValor(cdParametro, 0, 0, 0, pessoa.getCdPessoa(), null, (parametros.get(key) != null ? String.valueOf(parametros.get(key)) : "0"), null);
							if(ParametroValorDAO.insert(parametroValor, connection) <= 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return new Result(-1, "Erro ao inserir valor em parametro!");
							}
						}
					}
					else{
						int cdParametro = ParametroServices.insert(new Parametro(0, 0, key, ParametroServices.NUMERICO, ParametroServices.SINGLE_TEXT, null, null, pessoa.getCdPessoa(), ParametroServices.TP_PESSOA, 0, 0, ParametroServices.NIVEL_ACESSO_OPERADOR), null, connection);
						ParametroValor parametroValor = new ParametroValor(cdParametro, 0, 0, 0, pessoa.getCdPessoa(), null, (parametros.get(key) != null ? String.valueOf(parametros.get(key)) : "0"), null);
						if(ParametroValorDAO.insert(parametroValor, connection) <= 0){
							if(isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao inserir valor em parametro!");
						}
					}
				}
			}
			
			
			//Limite de credito
			float vlLimiteCredito = (cliente != null ? cliente.getVlLimiteCredito() : 0);
			if(vlLimiteCredito > 0){
				float somaLimite = 0;
				PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM adm_condicao_pagamento_cliente WHERE cd_pessoa = " + pessoa.getCdPessoa() + " AND cd_empresa = " + cdEmpresa);
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				while(rsm.next()){
					CondicaoPagamento condicaoPagamento = CondicaoPagamentoDAO.get(rsm.getInt("cd_condicao_pagamento"), connection);
					somaLimite += condicaoPagamento.getVlLimite();
				}
				pstmt = connection.prepareStatement(" SELECT vl_limite FROM adm_cliente_produto A " +
													" WHERE A.cd_pessoa = " + pessoa.getCdPessoa() +
													" AND A.cd_empresa = "  + cdEmpresa +
													" AND A.cd_produto_servico = ?");
				
				PreparedStatement pstmtCombustivel = connection.prepareStatement("SELECT B.nm_produto_servico AS nm_combustivel, B.cd_produto_servico AS cd_combustivel " +
																				  "FROM alm_produto_grupo A " +
																				  "JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) " +
																				  "WHERE A.cd_grupo = " + ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa));
				
				ResultSetMap rsmCombustivel = new ResultSetMap(pstmtCombustivel.executeQuery());
				while(rsmCombustivel.next()){
					pstmt.setInt(1, rsmCombustivel.getInt("cd_combustivel"));
					rsm = new ResultSetMap(pstmt.executeQuery());
					while(rsm.next()){
						somaLimite += rsm.getFloat("vl_limite");
					}
				}
				
				somaLimite += cliente.getVlLimiteVale();
				somaLimite += cliente.getVlLimiteFactoring();
				
				if(vlLimiteCredito < somaLimite){
					if(isConnectionNull)
						Conexao.rollback(connection);
					NumberFormat formatoPreco = NumberFormat.getCurrencyInstance();  
			        formatoPreco.setMaximumFractionDigits(2);
			        
					return new Result(-1, "A soma dos limites de combustíveis, vale, troca de cheque e condições de pagamento ultrapassou o limite geral. " +
										  "O valor do limite é "+formatoPreco.format(vlLimiteCredito)+" e a soma desses limites foi de "+formatoPreco.format(somaLimite)+"!");
				}
			}
			
			
			/*
			 * VINCULO
			 */
			Vinculo vinculo = cdVinculo == 0 ? null : VinculoDAO.get(cdVinculo, connection);
			int cdFormulario = vinculo == null ? 0 : vinculo.getCdFormulario();
			if (atributos != null) {
				/* remove atributos configurados anteriormente para a pessoa */
				PreparedStatement pstmt = connection.prepareStatement("DELETE FROM grl_formulario_atributo_valor " +
						"WHERE cd_pessoa = ? " +
						(cdFormulario > 0 ? "  AND cd_formulario_atributo IN (SELECT cd_formulario_atributo FROM grl_formulario_atributo WHERE cd_formulario = ?)" : ""));
				pstmt.setInt(1, pessoa.getCdPessoa());
				if (cdFormulario > 0)
					pstmt.setInt(2, cdFormulario);
				pstmt.execute();

				/* configura os atributos da pessoa */
				for (int i = 0; i < atributos.size(); i++) {
					FormularioAtributoValor atributo = (FormularioAtributoValor)atributos.get(i);
					if (FormularioAtributoValorDAO.insert(atributo, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao atualizar dados de atributos dinâmicos!");
					}
				}
			}			
			
			/*
			 * Sócio
			 */			
			if (socio != null) {
				PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM grl_socio " +
																	  "WHERE cd_pessoa = ?" +
																	  "AND cd_socio = ?");		
				pstmt.setInt(1, pessoa.getCdPessoa());
				pstmt.setInt(2, socio.getCdSocio());
				if(pstmt.executeQuery().next())
					return new Result(-1, "Sócios já vinculados!");

				PreparedStatement pstmt1 = connection.prepareStatement("SELECT cd_pessoa FROM grl_socio " +
																	   "WHERE cd_pessoa = ? " +
																	   "AND cd_socio > 0 ");		
				pstmt1.setInt(1, pessoa.getCdPessoa());				
				if(pstmt1.executeQuery().next()){					
					pstmt1.close();
					pstmt1 = connection.prepareStatement("UPDATE grl_socio SET cd_socio = ? WHERE cd_pessoa = ?");								                 
					pstmt1.setInt(1, socio.getCdSocio());
					pstmt1.setInt(2, socio.getCdPessoa());
					pstmt1.execute();
				}else{								
					int ret = SocioDAO.insert(socio, connection);					
					if (ret < 0) {					
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao tentar gravar sócio!");
					}
				}
			}
			//
			if (isConnectionNull)
				connection.commit();

			result = new Result(1);
			result.addObject("pessoa", pessoa);
			result.addObject("endereco", endereco);
			result.addObject("dadosFuncionais", dadosFuncionais);
			if (formacaoAcad != null)
				result.addObject("cdFormacao", cdFormacao);
			return result;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar atualizar dados de pessoa!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result update(Pessoa pessoa, PessoaEndereco endereco) {
		return  update(pessoa, endereco, null);
	}
	
	public static Result update(Pessoa pessoa, PessoaEndereco endereco, Connection connection) {
	boolean isConnectionNull = connection==null;
	try {
		connection = isConnectionNull ? Conexao.conectar() : connection;
		connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
		/*
		 * Valida e verifica o CPF ou CPNJ
		 */
		Result result = validateCpfCnpj(pessoa, connection);
		if(result.getCode()<=0)
			return result;
		
		/*
		 * PESSOA FÍSICA
		 */			
		if (pessoa instanceof PessoaFisica) {
			// Verifica se existe registro em pessoa jurídica
			if(connection.prepareStatement("SELECT * FROM grl_pessoa_juridica " +
                                           "WHERE cd_pessoa = "+pessoa.getCdPessoa()).executeQuery().next()) {
				
				PreparedStatement pstmt = connection.prepareStatement("DELETE FROM grl_pessoa_juridica WHERE cd_pessoa=?");
				pstmt.setInt(1, pessoa.getCdPessoa());
				int ret = pstmt.executeUpdate();
				
				if(ret <= 0)	{
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar transformar pessoa jurídica em física!");
				}
			}
			else if (PessoaFisicaDAO.update((PessoaFisica)pessoa, connection)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao tentar atualizar dados da pessoa física!");
			}

		}
		/*
		 * PESSOA JURÍDICA
		 */
		else if (pessoa instanceof PessoaJuridica) {
			// Verifica se existe registro em pessoa jurídica
			if(connection.prepareStatement("SELECT * FROM grl_pessoa_fisica " +
                                           "WHERE cd_pessoa = "+pessoa.getCdPessoa()).executeQuery().next()) {

				PreparedStatement pstmt = connection.prepareStatement("DELETE FROM grl_pessoa_fisica WHERE cd_pessoa=?");
				pstmt.setInt(1, pessoa.getCdPessoa());
				int ret = pstmt.executeUpdate();

				if(ret <= 0)	{
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar transformar pessoa física em jurídica!");
				}
			}

			if (PessoaJuridicaDAO.update((PessoaJuridica)pessoa, connection)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao atualizar dados de pessoa jurídica!");
			}

		}
		
		/*
		 * PESSOA
		 */
		if (PessoaDAO.update(pessoa, connection)<=0) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao atualizar dados de pessoa!");
		}

		/*
		 * ENDERECO
		 */
		if (endereco != null) {
			int cdEndereco = 0;
			if (endereco.getCdEndereco() == 0) {
				if ((cdEndereco = PessoaEnderecoDAO.insert(endereco, connection)) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao atualizar dados do endereço!");
				}
				endereco.setCdEndereco(cdEndereco);
			}
			else if (PessoaEnderecoDAO.update(endereco, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao atualizar dados do endereço!");
			}
			if(endereco.getLgPrincipal()==1)
				connection.prepareStatement("UPDATE grl_pessoa_endereco SET lg_principal = 0 " +
						                    "WHERE cd_pessoa    = "+pessoa.getCdPessoa()+
						                    "  AND cd_endereco <> "+endereco.getCdEndereco()).executeUpdate();
		}

		if (isConnectionNull)
			connection.commit();

		result = new Result(1);
		result.addObject("pessoa", pessoa);
		result.addObject("endereco", endereco);
		return result;
	}
	catch(Exception e) {
		Util.registerLog(e);
		e.printStackTrace(System.out);
		if (isConnectionNull)
			Conexao.rollback(connection);
		return new Result(-1, "Erro ao tentar atualizar dados de pessoa!", e);
	}
	finally {
		if (isConnectionNull)
			Conexao.desconectar(connection);
	}
}

	public static Result insert(Pessoa pessoa, PessoaFisica pessoaFisica, PessoaEndereco pessoaEndereco,
			int cdEmpresa, int cdVinculo){
		return insert(pessoa, pessoaFisica, pessoaEndereco, cdEmpresa, cdVinculo, null);
	}
	public static Result insert(Pessoa pessoa, PessoaFisica pessoaFisica, PessoaEndereco pessoaEndereco,
			int cdEmpresa, int cdVinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(pessoa==null)
				return new Result(-1, "Erro ao salvar. Pessoa é nula.");
			if(pessoaFisica==null)
				return new Result(-1, "Erro ao salvar. Pessoa Física é nula.");
			if(pessoaEndereco==null)
				return new Result(-1, "Erro ao salvar. Endereço é nulo.");
			if(cdEmpresa<=0)
				return new Result(-1, "Erro ao salvar. Empresa é nula.");
			if(cdVinculo<=0)
				return new Result(-1, "Erro ao vinculo. Vinculo é nulo.");
			
			Result r = new Result(-1, "Erro ao cadastrar contratado.");
			
			if(pessoa.getCdPessoa() == 0){
				r.setCode(PessoaFisicaDAO.insert(pessoaFisica, connect));
				pessoa.setCdPessoa(r.getCode());
			}
			
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			pessoaFisica.setCdPessoa(r.getCode());
			pessoaEndereco.setCdPessoa(r.getCode());
			
			r.setCode(PessoaEnderecoDAO.insert(pessoaEndereco, connect));
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			//TODO: Parametrizar empresa e vinculo
			r.setCode(PessoaEmpresaDAO.insert(new PessoaEmpresa(cdEmpresa, pessoaFisica.getCdPessoa(), cdVinculo, new GregorianCalendar(), PessoaEmpresaServices.ST_ATIVO), connect));
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			if(r.getCode()<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			r.setCode(pessoa.getCdPessoa());
			r.setMessage("Pessoa cadastrada com sucesso.");
			return r;
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
	
	public static byte[] getBytesImage(int cdPessoa) {
		return getBytesImage(cdPessoa, null);
	}

	public static byte[] getBytesImage(int cdPessoa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			Pessoa pessoa = PessoaDAO.get(cdPessoa, connection);
			return pessoa==null ? null : pessoa.getImgFoto();
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

	public static byte[] getBytesImageScaled(int cdPessoa, int width, int height, String urlImagemDefault){
		try{
			return ImagemServices.getBytesImageScaled(getBytesImage(cdPessoa), width, height, urlImagemDefault);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
    }


	public static Vinculo getVinculoOfPessoa(int cdPessoa, int cdEmpresa) {
		return getVinculoOfPessoa(cdPessoa, cdEmpresa, null);
	}

	public static Vinculo getVinculoOfPessoa(int cdPessoa, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT B.* " +
				  "FROM grl_pessoa_empresa A, grl_vinculo B " +
				  " WHERE A.cd_vinculo = B.cd_vinculo " +
				  "   AND A.cd_pessoa = ? " +
				  "   AND A.cd_empresa = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdEmpresa);
			ResultSet rs = pstmt.executeQuery();
			return rs.next() ? new Vinculo(rs.getInt("cd_vinculo"),
					rs.getString("nm_vinculo"),
					rs.getInt("lg_estatico"),
					rs.getInt("lg_funcao"),
					rs.getInt("cd_formulario"),
					rs.getInt("lg_cadastro")) : null;
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
	
	public static ResultSetMap getAllDocumentosOfPessoa(int cdPessoa) {
		return getAllDocumentosOfPessoa(cdPessoa, null);
	}

	public static ResultSetMap getAllDocumentosOfPessoa(int cdPessoa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_tipo_documento, B.sg_tipo_documento " +
																  "FROM grl_tipo_documento_pessoa A, grl_tipo_documento B " +
																  "WHERE A.cd_tipo_documento = B.cd_tipo_documento " +
																  "  AND A.cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
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
	
	public static ResultSetMap getAllDocumentosOfAluno(int cdPessoa) {
		return getAllDocumentosOfAluno(cdPessoa, false, null);
	}

	public static ResultSetMap getAllDocumentosOfAluno(int cdPessoa, Connection connection) {
		return getAllDocumentosOfAluno(cdPessoa, false, null);
	}
	
	public static ResultSetMap getAllDocumentosOfAluno(int cdPessoa, boolean onlyNecessarios) {
		return getAllDocumentosOfAluno(cdPessoa, onlyNecessarios, null);
	}

	public static ResultSetMap getAllDocumentosOfAluno(int cdPessoa, boolean onlyNecessarios, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.*, C.nm_pessoa as nm_cartorio " +
																  " FROM grl_pessoa_tipo_documentacao A " +
																  " JOIN grl_tipo_documentacao B ON (A.cd_tipo_documentacao = B.cd_tipo_documentacao) " +
																  " LEFT OUTER JOIN grl_pessoa C ON (A.cd_cartorio = C.cd_pessoa)" +
																  "WHERE A.cd_pessoa = ? " + (onlyNecessarios ? " AND A.cd_tipo_documentacao IN (2, 3, 4, 5, 6)" : ""));
			pstmt.setInt(1, cdPessoa);
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

	public static ResultSetMap getAllVinculosOfPessoa(int cdPessoa, int cdEmpresa) {
		return getAllVinculosOfPessoa(cdPessoa, cdEmpresa, null);
	}

	public static ResultSetMap getAllVinculosOfPessoa(int cdPessoa, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			
			int cdInstituicaoSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0, 0, connection);

			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_vinculo, B.lg_estatico, B.lg_funcao " +
																  "FROM grl_pessoa_empresa A, grl_vinculo B " +
																  "WHERE A.cd_vinculo = B.cd_vinculo " +
																  "  AND A.cd_pessoa = ? " +
																  (cdEmpresa != cdInstituicaoSecretaria ? "  AND A.cd_empresa = ?" : ""));
			pstmt.setInt(1, cdPessoa);
			if(cdEmpresa != cdInstituicaoSecretaria)
				pstmt.setInt(2, cdEmpresa);
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

	public static ResultSetMap getAllOcorrenciasOfPessoa(int cdPessoa) {
		return getAllOcorrenciasOfPessoa(cdPessoa, null);
	}

	public static ResultSetMap getAllOcorrenciasOfPessoa(int cdPessoa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_tipo_ocorrencia, D.nm_pessoa AS NM_RESPONSAVEL " +
																  "FROM grl_ocorrencia A " +
																  "LEFT OUTER JOIN grl_tipo_ocorrencia B ON (A.cd_tipo_ocorrencia = B.cd_tipo_ocorrencia) " +
																  "LEFT OUTER JOIN seg_usuario C ON (A.cd_usuario = C.cd_usuario) " +
																  "LEFT OUTER JOIN grl_pessoa D ON (C.cd_pessoa = D.cd_pessoa) " +
																  "WHERE A.cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
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

	public static ResultSetMap getAllArquivosOfPessoa(int cdPessoa) {
		return getAllArquivosOfPessoa(cdPessoa, null);
	}

	public static ResultSetMap getAllArquivosOfPessoa(int cdPessoa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, "+
																  "       B.cd_arquivo, B.nm_arquivo, B.dt_arquivamento, B.nm_documento, B.cd_tipo_arquivo, "+
																  "		  B.dt_criacao, B.cd_usuario, B.st_arquivo, B.dt_inicial, B.dt_final, B.cd_assinatura, B.txt_ocr, " +
																  "		  C.nm_tipo_arquivo, D.nm_nivel_acesso, E.nm_setor, " +
																  " 	  F.cd_pessoa, F.nm_pessoa, F.img_foto, G.nr_cnh, H.tp_sangue, tp_fator_rh " +
																  "FROM grl_pessoa_arquivo A " +
																  "JOIN grl_arquivo                 B ON (A.cd_arquivo = B.cd_arquivo) " +
																  "LEFT OUTER JOIN grl_tipo_arquivo C ON (B.cd_tipo_arquivo = C.cd_tipo_arquivo) " +
																  "LEFT OUTER JOIN grl_nivel_acesso D ON (A.cd_nivel_acesso = D.cd_nivel_acesso) " +
																  "LEFT OUTER JOIN grl_setor        E ON (A.cd_setor = E.cd_setor) " +
																  "LEFT OUTER JOIN grl_pessoa              F ON (A.cd_pessoa = F.cd_pessoa) "+
																  "LEFT OUTER JOIN grl_pessoa_fisica       G ON (F.cd_pessoa = G.cd_pessoa) "+
																  "LEFT OUTER JOIN grl_pessoa_ficha_medica H ON (F.cd_pessoa = H.cd_pessoa) "+
																  "WHERE A.cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
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

	public static ResultSetMap getAllEnderecosOfPessoa(int cdPessoa) {
		return getAllEnderecosOfPessoa(cdPessoa, null);
	}

	public static ResultSetMap getAllEnderecosOfPessoa(int cdPessoa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			int tpEnderecamento = ParametroServices.getValorOfParametroAsInteger("TP_ENDERECAMENTO", PessoaEnderecoServices.TP_DIGITAVEL, 0);
			return  new ResultSetMap(connection.prepareStatement(
					    "SELECT A.*, "+tpEnderecamento+" AS tp_enderecamento, "+
                    	" 	    B.nm_tipo_endereco, C.nm_tipo_logradouro, C.sg_tipo_logradouro, " +
                    	"       G.nm_tipo_logradouro AS nm_tipo_logradouro_enderecamento, " +
                    	"       G.sg_tipo_logradouro AS sg_tipo_logradouro_enderecamento, " +
                    	"       D.nm_logradouro AS nm_logradouro_enderecamento, D.id_logradouro, " +
                    	"       E.nm_cidade, F.nm_bairro AS nm_bairro_enderecamento, F.id_bairro," +
                    	"       H.nm_estado, H.sg_estado " +
                    	"FROM grl_pessoa_endereco A " +
                    	"LEFT OUTER JOIN grl_tipo_endereco B ON (A.cd_tipo_endereco = B.cd_tipo_endereco) " +
                    	"LEFT OUTER JOIN grl_tipo_logradouro C ON (A.cd_tipo_logradouro = C.cd_tipo_logradouro) " +
                    	"LEFT OUTER JOIN grl_logradouro D ON (A.cd_logradouro = D.cd_logradouro) "+
                    	"LEFT OUTER JOIN grl_tipo_logradouro G ON (D.cd_tipo_logradouro = G.cd_tipo_logradouro) " +
                    	"LEFT OUTER JOIN grl_cidade E ON (A.cd_cidade = E.cd_cidade) " +
                    	"LEFT OUTER JOIN grl_bairro F ON (A.cd_bairro = F.cd_bairro) " +
                    	"LEFT OUTER JOIN grl_estado H ON (E.cd_estado = H.cd_estado) " +
                    	"WHERE cd_pessoa = "+cdPessoa).executeQuery());
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getEnderecoPrincipalOfPessoa(int cdPessoa) {
		return getEnderecoPrincipalOfPessoa(cdPessoa, null);
	}

	public static ResultSetMap getEnderecoPrincipalOfPessoa(int cdPessoa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			int tpEnderecamento = ParametroServices.getValorOfParametroAsInteger("TP_ENDERECAMENTO", PessoaEnderecoServices.TP_DIGITAVEL, 0);
			return  new ResultSetMap(connection.prepareStatement(
					    "SELECT A.*, "+tpEnderecamento+" AS tp_enderecamento, "+
                    	" 	    B.nm_tipo_endereco, C.nm_tipo_logradouro, C.sg_tipo_logradouro, " +
                    	"       G.nm_tipo_logradouro AS nm_tipo_logradouro_enderecamento, " +
                    	"       G.sg_tipo_logradouro AS sg_tipo_logradouro_enderecamento, " +
                    	"       D.nm_logradouro AS nm_logradouro_enderecamento, D.id_logradouro, " +
                    	"       E.nm_cidade, F.nm_bairro AS nm_bairro_enderecamento, F.id_bairro," +
                    	"       H.nm_estado, H.sg_estado " +
                    	"FROM grl_pessoa_endereco A " +
                    	"LEFT OUTER JOIN grl_tipo_endereco B ON (A.cd_tipo_endereco = B.cd_tipo_endereco) " +
                    	"LEFT OUTER JOIN grl_tipo_logradouro C ON (A.cd_tipo_logradouro = C.cd_tipo_logradouro) " +
                    	"LEFT OUTER JOIN grl_logradouro D ON (A.cd_logradouro = D.cd_logradouro) "+
                    	"LEFT OUTER JOIN grl_tipo_logradouro G ON (D.cd_tipo_logradouro = G.cd_tipo_logradouro) " +
                    	"LEFT OUTER JOIN grl_cidade E ON (A.cd_cidade = E.cd_cidade) " +
                    	"LEFT OUTER JOIN grl_bairro F ON (A.cd_bairro = F.cd_bairro) " +
                    	"LEFT OUTER JOIN grl_estado H ON (E.cd_estado = H.cd_estado) " +
                    	"WHERE cd_pessoa = "+cdPessoa+" AND lg_principal = 1").executeQuery());
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

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}
	
	public static ResultSetMap findDest(ArrayList<ItemComparator> criterios) {
		criterios.add(new ItemComparator("findEnderecoPrincipal", "true", ItemComparator.EQUAL, Types.BOOLEAN));
		return find(criterios, null);
	}

	public static ResultSetMap findColaboradorSetor(ArrayList<ItemComparator> criterios) {		
		criterios.add(new ItemComparator("findNotProfessorSecretaria", "true", ItemComparator.EQUAL, Types.BOOLEAN));
		return find(criterios, null);
	}
	
	public static ResultSetMap findByCpfCnpjCompleto(String cpfCnpj){
		ResultSetMap rsm = findByCpfCnpj(cpfCnpj);
		
		return rsm;
	}
	
	
	public static ResultSetMap findByCpfCnpj(String cpfCnpj) {
		Connection connect = Conexao.conectar();
		try{
			cpfCnpj = Util.limparFormatos(cpfCnpj);
			
			if(cpfCnpj == null || cpfCnpj.trim().equals("")) {
				return null;
			}
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.cd_proprietario, B.nm_pessoa, B.nr_celular, B.gn_pessoa, C.nr_cpf, D.nr_cnpj, E.* FROM fta_veiculo A " +
					"   JOIN grl_pessoa                       B ON (A.cd_proprietario = B.cd_pessoa)"+
					"   LEFT OUTER JOIN grl_pessoa_fisica 	C ON (B.cd_pessoa = C.cd_pessoa) " +
					"	LEFT OUTER JOIN grl_pessoa_juridica D ON (B.cd_pessoa = D.cd_pessoa)" + 
					"	LEFT OUTER JOIN grl_pessoa_endereco E ON (B.cd_pessoa = E.cd_pessoa)" +
					"   WHERE C.nr_cpf = ? OR D.nr_cnpj = ?");
			
			pstmt.setString(1, cpfCnpj);
			pstmt.setString(2, cpfCnpj);
			
			ResultSetMap rsmPessoa = new ResultSetMap(pstmt.executeQuery());
			
//			while(rsmPessoa.next()){
//				if((rsmPessoa.getString("nr_cpf") != null && rsmPessoa.getString("nr_cpf").equals(cpfCnpj)) || (rsmPessoa.getString("nr_cnpj") != null && rsmPessoa.getString("nr_cnpj").equals(cpfCnpj))){
//					HashMap<String, Object> registerPessoa = rsmPessoa.getRegister();
//					ResultSetMap rsmPessoaEncontrada = new ResultSetMap();
//					rsmPessoaEncontrada.addRegister(registerPessoa);
//					return rsmPessoaEncontrada;
//				}
//			}
			
			System.out.println(rsmPessoa);
			return rsmPessoa;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findByVinculo(ArrayList<ItemComparator> criterios) {
		return findByVinculo(criterios, null);
	}

	public static ResultSetMap findByVinculo(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull)
				connect = Conexao.conectar();
			int qtLimite = 0;
			
			Iterator<ItemComparator> iterator = criterios.iterator();
			while(iterator.hasNext()) {
				ItemComparator crt = iterator.next();
				if (crt.getColumn().equalsIgnoreCase("qtLimite")) {
					qtLimite = Integer.parseInt(crt.getValue());
					iterator.remove();
				}
			}
			
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
			
			ResultSetMap rsm = Search.find(
				"   SELECT A.*, C.nr_cpf, D.nr_cnpj " + 
			    "     FROM grl_pessoa A " + 
				"     JOIN grl_pessoa_empresa B ON (A.cd_pessoa = B.cd_pessoa)  " + 
				"LEFT JOIN grl_pessoa_fisica C ON (A.cd_pessoa = C.cd_pessoa)   " + 
				"LEFT JOIN grl_pessoa_juridica D ON (A.cd_pessoa = D.cd_pessoa) " + 
			    "    WHERE 1=1 ",
				" ORDER BY A.nm_pessoa ASC " + sqlLimit[1],
				criterios, null, connect, false, true
			);
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			boolean cdCboOrCnaePrincipal  = false;
			boolean findCountAtendimentos = false;
			boolean findEnderecoPrincipal = false;
			boolean findDependentesDisp   = false;
			boolean findSupervisor        = false;
			boolean findAlunos            = false;
			boolean findCliente           = false;
			boolean findSocio             = false;
			boolean findProfessor         = false;
			boolean lgDadosFuncionais     = false;
			boolean findNotProfessorSecretaria = false;
			boolean lgMultiVinculo        = false;
			boolean lgGetVinculos        = false;
			int cdDisciplina        = 0;
			int cdProprietario      = 0;
			int cdContrato          = 0;
			int qtLimite            = 0;
			int cdEmpresa           = 0;
			int cdVinculo           = 0;
			
			int cdCidadeOrgao		= 0;
			int cdEstadoOrgao		= 0;
			int cdSevicoOrgao		= 0;
			String vlServicoMin		= null;
			String vlServicoMax		= null;
			
			boolean lgDocumentoAusente = false;
			boolean agrupamentoCorrespondenteCidade = false;
			boolean agrupamentoCorrespondenteEstado = false;
			
			@SuppressWarnings("unused") 
			int cdInstituicaoLotado = 0;
			
			String nmPessoaSocial = "", nmPessoa = "";
			
			LogUtils.debug("PessoaServices.find");
			LogUtils.createTimer("PESSOA_FIND_TIMER");
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("cdCboOrCnaePrincipal"))
					cdCboOrCnaePrincipal = true;
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("findCountAtendimentos"))
					findCountAtendimentos = true;
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("findEnderecoPrincipal"))
					findEnderecoPrincipal = true;
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("findAlunos"))
					findAlunos = true;
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("findDependentesDisp"))
					findDependentesDisp = true;
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("findCliente"))
					findCliente = "TRUE".equalsIgnoreCase(criterios.get(i).getValue());
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("findSocio"))
					findSocio = true;
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("findProfessor"))
					findProfessor = true;
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("findSupervisor"))
					findSupervisor = true;
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("agrupamentoCorrespondenteCidade"))
					agrupamentoCorrespondenteCidade = true;
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("agrupamentoCorrespondenteEstado"))
					agrupamentoCorrespondenteEstado = true;
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("cdDisciplina"))
					cdDisciplina = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("cd_proprietario"))
					cdProprietario = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("cd_contrato"))
					cdContrato = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("cd_empresa"))
					cdEmpresa = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());		
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("cd_vinculo"))
					cdVinculo = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());		
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.nm_pessoa")) {
					nmPessoa = Util.limparTexto(criterios.get(i).getValue().trim());
					nmPessoa = "%"+nmPessoa.replaceAll(" ", "%")+"%";
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nm_pessoa_social")) {
					nmPessoaSocial = Util.limparTexto(criterios.get(i).getValue());
					nmPessoaSocial = "%"+nmPessoaSocial.replaceAll(" ", "%")+"%";
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lInicial") ||
						criterios.get(i).getColumn().equalsIgnoreCase("lFinal")) {
					criterios.get(i).setColumn("A.nm_pessoa");
					crt.add(criterios.get(i));
				} else if(criterios.get(i).getColumn().equalsIgnoreCase("A.st_cadastro") ){
					if( "-1".equalsIgnoreCase(criterios.get(i).getValue()) ){
						criterios.remove(i);
					}else{
						crt.add(criterios.get(i));
					}
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgDadosFuncionais")) {
					lgDadosFuncionais = Boolean.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("findNotProfessorSecretaria"))
					findNotProfessorSecretaria = true;
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("cdInstituicaoLotado"))
					cdInstituicaoLotado = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());		
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("lgAdminEscolar"))//Depreciar, utilizar lgMultiVinculo
					lgMultiVinculo = true;
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("lgMultiVinculo"))
					lgMultiVinculo = true;
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("lgGetVinculos"))
					lgGetVinculos = true;
				else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("lgDocumentoAusente"))
					lgDocumentoAusente = true;
				else if (criterios.get(i).getColumn().equalsIgnoreCase("PO1.CD_CIDADE")) {
					if(!agrupamentoCorrespondenteCidade) {
						cdCidadeOrgao = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
						criterios.remove(i);
						i--;
					}
					else
						crt.add(criterios.get(i));
						
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("PO4.CD_ESTADO")) {
					if(!agrupamentoCorrespondenteEstado) {
						cdEstadoOrgao = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
						criterios.remove(i);
						i--;
					}
					else
						crt.add(criterios.get(i));
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("PS.CD_PRODUTO_SERVICO")) {
					if(!agrupamentoCorrespondenteCidade | !agrupamentoCorrespondenteEstado) {
						cdSevicoOrgao = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
						criterios.remove(i);
						i--;
					}
					else
						crt.add(criterios.get(i));
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("PS.VL_SERVICO_MIN")) {
					if(!agrupamentoCorrespondenteCidade | !agrupamentoCorrespondenteEstado) {
						vlServicoMin = ((ItemComparator)criterios.get(i)).getValue();
						criterios.remove(i);
						i--;
					}
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("PS.VL_SERVICO_MAX")) {
					if(!agrupamentoCorrespondenteCidade | !agrupamentoCorrespondenteEstado) {
						vlServicoMax = ((ItemComparator)criterios.get(i)).getValue();
						criterios.remove(i);
						i--;
					}
				}
				else
					crt.add(criterios.get(i));
			}
			
			/************************
			 ** WHAT HAVE WE DONE? **
			 ************************/
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
			String sql =
	 			  "SELECT "+sqlLimit[0]+" A.*, " +
				  "   B.nm_pais, C.nm_forma_divulgacao, D.nm_classificacao, D.id_classificacao, " +
				  "   E.nr_cpf, E.cd_escolaridade, E.dt_nascimento, E.sg_orgao_rg, E.nm_mae, E.nm_pai, E.tp_sexo, E.st_estado_civil, " +
				  "   E.nr_rg, E.nr_cnh, E.dt_validade_cnh, E.dt_primeira_habilitacao, E.tp_categoria_habilitacao, E.tp_raca, E.lg_deficiente_fisico, E.lg_documento_ausente AS lg_cpf_ausente, " +
				  "   E.nm_forma_tratamento, E.cd_estado_rg, E.sg_orgao_rg, E.dt_emissao_rg, E1.sg_estado AS sg_estado_rg, FM.nr_cartao_sus, F.nr_cnpj, F.nm_razao_social, F.lg_documento_ausente AS lg_cnpj_ausente, " +
				  "   GE.sg_estado AS sg_estado_naturalidade, GP.nm_pais AS nm_pais_naturalidade," +
				  //O software juridico desktop utilizava os campos sg_uf_rg e sg_orgao_rg na tabela pessoa por isso criamos alias para os campos de pessoa fisica 
				  "   E.sg_orgao_rg AS sg_orgao_rg_pessoa_fisica, E.cd_estado_rg AS cd_estado_rg_pessoa_fisica, E.tp_nacionalidade," +
				  "   F.nr_inscricao_estadual, F.nr_inscricao_municipal, F.nr_funcionarios, F.dt_inicio_atividade, " +
				  "   F.tp_empresa, F.cd_natureza_juridica, F.dt_termino_atividade, F.cd_naturalidade AS cd_naturalidade_jur, FCC.nr_cnae, F1.nm_cidade AS nm_cidade_jur, F2.sg_estado AS sg_estado_jur, F3.nm_pais AS nm_pais_jur, G.cd_cidade AS cd_naturalidade, " +
				  "   G.nm_cidade AS nm_naturalidade, CONCAT(G.nm_cidade,' - ', GE.sg_estado) AS ds_naturalidade, H.nm_escolaridade, H.id_escolaridade, I.id_natureza_juridica, I.nm_natureza_juridica, " +
				  "	  I1.cd_conta_bancaria, I1.cd_banco, I1.nr_conta, I1.nr_dv, I1.nr_agencia, I1.tp_operacao, I1.nr_cpf_cnpj, I1.nm_titular, I1.st_conta, I1.tp_conta, I1.lg_conta_conjunta, " +
				  "	  PO.cd_orgao, PO.tp_contratacao, "+
				  (agrupamentoCorrespondenteCidade ? "  PO2.cd_cidade AS cd_cidade_orgao, PO2.nm_cidade AS nm_cidade_orgao, PO21.sg_estado AS sg_estado_cidade_orgao, " : "")+
				  (agrupamentoCorrespondenteEstado ? "  PO3. cd_estado AS cd_estado_orgao, PO3.nm_estado AS nm_estado_orgao, PO3.sg_estado AS sg_estado_orgao, " :"")+
				  "	  FM.cd_pessoa AS CD_TP_SANGUE_FATOR_RH, FM.tp_sangue, FM.tp_fator_rh, " +
				  "   UC1.nm_pessoa AS nm_usuario_cadastro "+
				  (lgDadosFuncionais ? "   ,DF.*" : "")+
				  "	  ,J1.nm_banco, J1.nr_banco, J1.id_banco, J1.nm_url " +
			      (findCliente ?  ", CL.lg_convenio AS lg_convenio_trabalho, CL.lg_ecommerce AS lg_ecommerce_trabalho, CL.lg_limite_credito AS lg_limite_credito_trabalho, CL.lg_agenda AS lg_agenda_trabalho, CL.nr_dias_carencia_fatura AS nr_dias_carencia_fatura_trabalho, " +
				      "   CL.nr_dias_carencia_fatura AS nr_dias_carencia_pdv, CL.cd_empresa AS cd_empresa_trabalho, CL.vl_limite_credito AS vl_limite_credito_trabalho, CL.vl_limite_mensal AS vl_limite_mensal_trabalho, CL.vl_limite_factoring AS vl_limite_factoring_trabalho, CL.vl_limite_factoring_emissor AS vl_limite_factoring_emissor_trabalho, " +
				      "   CL.vl_limite_factoring_unitario AS vl_limite_factoring_unitario_trabalho, CL.lg_pista AS lg_pista_trabalho, CL.lg_loja AS lg_loja_trabalho, CL.lg_veiculos_cadastrados AS lg_veiculos_cadastrados_trabalho, CL.nr_limite_abastecimentos AS nr_limite_abastecimentos_trabalho, CL.vl_limite_vale AS vl_limite_vale_trabalho, " +
				      "   CL.cd_convenio AS cd_convenio_trabalho, CL.cd_tabela_preco AS cd_tabela_preco_trabalho, CL.cd_cidade AS cd_cidade_trabalho, CL.cd_tipo_logradouro AS cd_tipo_logradouro_trabalho, CL.nm_cargo AS nm_cargo_trabalho, CL.lg_SPC, CL.lg_SERASA, " +
				      "   CL.nm_logradouro AS nm_logradouro_trabalho, CL.nr_endereco AS nr_endereco_trabalho, CL.nm_complemento AS nm_complemento_trabalho, CL.nm_bairro AS nm_bairro_trabalho, CL.nm_ponto_referencia AS nm_ponto_referencia_trabalho, " +
				      "   CL.nr_cep AS nr_cep_trabalho, CL.nm_contato AS nm_contato_trabalho, CL.nr_dependentes AS nr_dependentes_trabalho, CL.vl_salario AS vl_salario_trabalho, CL.st_aluguel AS st_aluguel_trabalho, CL.vl_aluguel AS vl_aluguel_trabalho, " +
				      "   CL.dt_admissao AS dt_admissao_trabalho, CL.nm_outra_renda AS nm_outra_renda_trabalho, CL.vl_outra_renda AS vl_outra_renda_trabalho, CL.cd_faixa_renda AS cd_faixa_renda_trabalho, CL.lg_controle_veiculo AS lg_controle_veiculo_trabalho, " +
				      "   CL.nm_empresa_trabalho, CL.nr_telefone_trabalho, CL.qt_prazo_minimo_factoring AS qt_prazo_minimo_factoring_trabalho, CL.qt_prazo_maximo_factoring AS qt_prazo_maximo_factoring_trabalho, CL.qt_idade_minima_factoring AS qt_idade_minima_factoring_trabalho, " +
				      "	  CL.vl_ganho_minimo_factoring AS vl_ganho_minimo_factoring_trabalho, CL.pr_taxa_minima_factoring AS pr_taxa_minima_factoring_trabalho, CL.vl_taxa_devolucao_factoring AS vl_taxa_devolucao_factoring_trabalho, CL.pr_taxa_padrao_factoring AS pr_taxa_padrao_factoring_trabalho," +
				      "	  CL.pr_taxa_juros_factoring AS pr_taxa_juros_factoring_trabalho, CL.pr_taxa_prorrogacao_factoring AS pr_taxa_prorrogacao_factoring_trabalho, CL.qt_maximo_documento AS qt_maximo_documento_trabalho, CL.cd_classificacao_cliente AS cd_classificacao_cliente_trabalho, CL.nr_codigo_barras AS nr_codigo_barras_trabalho," +
				      "   CL.lg_convenio AS lg_convenio_trabalho, CL.cd_rota AS cd_rota_trabalho, CL.cd_cobranca AS cd_cobranca_trabalho, CB.nm_cobranca AS nm_cobranca_trabalho, CL.cd_programa_fatura AS cd_programa_fatura_trabalho, CPF.nm_programa_fatura AS nm_programa_fatura_trabalho, CL.txt_observacao AS txt_observacao_trabalho, CL.tp_credito AS tp_credito_trabalho, FR.nm_rota AS nm_rota_trabalho, FRV.cd_pessoa AS cd_vendedor_trabalho, FRV.nm_pessoa AS nm_vendedor_trabalho, CL.cd_convenio AS cd_convenio_trabalho, GPC.nm_pessoa AS nm_convenio_trabalho, CL.lg_analise AS lg_analise_trabalho, CL.cd_profissao AS cd_profissao_trabalho," +
				      "   PC.cd_pessoa AS cd_pessoa_cobranca, PC.nm_pessoa AS nm_pessoa_cobranca, PC.nm_apelido AS nm_apelido_cobranca, PCF.nr_cpf AS nr_cpf_cobranca, PCJ.nr_cnpj AS nr_cnpj_cobranca, PCJ.nm_razao_social AS nm_razao_social_cobranca, PCF.nr_rg AS nr_rg_cobranca, PCF.dt_emissao_rg AS dt_emissao_rg_cobranca, PCF.sg_orgao_rg AS sg_orgao_rg_cobranca, PCF.cd_estado_rg AS cd_estado_rg_cobranca," +
				      "	  PCF.nm_forma_tratamento AS nm_forma_tratamento_cobranca, PC.nm_email AS nm_email_cobranca, PC.gn_pessoa AS gn_pessoa_cobranca, EPC.cd_pessoa AS cd_pessoa_conjuge, EPC.nm_pessoa AS nm_pessoa_conjuge, EPCF.nr_cpf AS nr_cpf_conjuge, EPCF.nr_rg AS nr_rg_conjuge, EPCF.dt_emissao_rg AS dt_emissao_rg_conjuge, EPCF.sg_orgao_rg AS sg_orgao_rg_conjuge, EPCF.cd_estado_rg AS cd_estado_rg_conjuge," +
				      "	  EPCF.nm_forma_tratamento AS nm_forma_tratamento_conjuge, EPCF.dt_nascimento AS dt_nascimento_conjuge " : "") +
				  (findSocio ? ", SO.cd_socio " : "") +
				  (findCountAtendimentos ? ", (SELECT COUNT(*) FROM crm_atendimento WHERE cd_pessoa = A.cd_pessoa) AS qt_atendimentos " : "") +
			      (cdProprietario > 0    ? ", N.cd_grupo, O.nm_grupo " : "") +
			      (cdCboOrCnaePrincipal  ? ", M.cd_cnae, L.cd_cbo " : "") +
			      (findEnderecoPrincipal ? ", P.cd_endereco, P.ds_endereco, P.cd_tipo_logradouro, P.cd_tipo_endereco, " +
			    		"P.cd_logradouro, P.cd_bairro, P.cd_cidade, P.nm_logradouro, P.nm_bairro, P.nr_cep, " +
			    		"P.nr_endereco, P.nm_complemento, P.nr_telefone, P.nm_ponto_referencia, P.lg_cobranca, P.lg_principal, P.tp_zona, " +
			      		"Q.nm_tipo_logradouro, Q.sg_tipo_logradouro, " +
			      		"S.nm_tipo_logradouro AS nm_tipo_logradouro_enderecamento, S.sg_tipo_logradouro AS sg_tipo_logradouro_enderecamento, " +
			      		"R.nm_logradouro AS nm_logradouro_enderecamento, R.id_logradouro, U.nm_cidade, U.id_ibge, T.nm_bairro AS nm_bairro_enderecamento, " +
			      		"T.id_bairro, V.nm_estado, V.sg_estado, V.cd_estado " : "") +
			      (ItemComparator.getItemComparatorByColumn(criterios, "J.CD_EMPRESA") != null ? ", J.cd_empresa " :"") + 
			      (lgMultiVinculo ? ", JV.nm_vinculo, J.cd_vinculo " :"" ) +
			      "FROM grl_pessoa A " +
				  "LEFT OUTER JOIN grl_pais 			 B ON (A.cd_pais = B.cd_pais) " +
				  "LEFT OUTER JOIN grl_forma_divulgacao  C ON (A.cd_forma_divulgacao = C.cd_forma_divulgacao) " +
				  "LEFT OUTER JOIN adm_classificacao 	 D ON (A.cd_classificacao = D.cd_classificacao) " +
				  "LEFT OUTER JOIN grl_pessoa_fisica 	 E ON (A.cd_pessoa = E.cd_pessoa) " +
				  "LEFT OUTER JOIN grl_pessoa_ficha_medica FM ON (E.cd_pessoa = FM.cd_pessoa) "+
				  "LEFT OUTER JOIN grl_estado 			E1 ON (E.cd_estado_rg = E1.cd_estado) " +
				  "LEFT OUTER JOIN grl_pessoa_juridica 	 F ON (A.cd_pessoa = F.cd_pessoa) " +
				  "LEFT OUTER JOIN grl_cnae_pessoa_juridica FC ON (A.cd_pessoa = FC.cd_pessoa " +
				  "														AND FC.lg_principal = 1) " +
				  "LEFT OUTER JOIN grl_cnae FCC ON (FC.cd_cnae = FCC.cd_cnae) " +
				  "LEFT OUTER JOIN grl_cidade 	        F1 ON (F1.cd_cidade = F.cd_naturalidade) " +
				  "LEFT OUTER JOIN grl_estado 	        F2 ON (F1.cd_estado = F2.cd_estado) " +
				  "LEFT OUTER JOIN grl_pais 	        F3 ON (F2.cd_pais   = F3.cd_pais) " +
				  "LEFT OUTER JOIN grl_pessoa_conta_bancaria I1 ON (A.cd_pessoa = I1.cd_pessoa and I1.lg_principal = 1 ) " +
				  "LEFT OUTER JOIN grl_banco J1 ON (I1.cd_banco = J1.cd_banco) " +
				  (findSocio ? "LEFT OUTER JOIN grl_socio SO ON (F.cd_pessoa = SO.cd_pessoa) " : "") +
		      	  "LEFT OUTER JOIN grl_cidade 			 G   ON (E.cd_naturalidade = G.cd_cidade) " +
		      	  "LEFT OUTER JOIN grl_estado 			 GE  ON (GE.cd_estado = G.cd_estado) " +
		      	  "LEFT OUTER JOIN grl_pais 			 GP  ON (GP.cd_pais = GE.cd_pais) " +
		          "LEFT OUTER JOIN grl_escolaridade 	 H   ON (E.cd_escolaridade = H.cd_escolaridade) " +
		      	  (lgDadosFuncionais ? "LEFT OUTER JOIN srh_dados_funcionais  DF  ON (A.cd_pessoa = DF.cd_pessoa) " : "") +
		          (findCliente ? "LEFT OUTER JOIN adm_cliente CL ON (A.cd_pessoa   = CL.cd_pessoa " +
		        		                     (cdEmpresa > 0 ? "  AND CL.cd_empresa = " + cdEmpresa + " )" :")") +
		          "LEFT OUTER JOIN fta_rota   	         FR  ON (FR.cd_rota = CL.cd_rota) " +
		          "LEFT OUTER JOIN grl_pessoa  	         FRV ON (FRV.cd_pessoa = FR.cd_vendedor) " +
		          "LEFT OUTER JOIN grl_pessoa 	         GPC ON (GPC.cd_pessoa = CL.cd_convenio) " +
		          "LEFT OUTER JOIN grl_pessoa 	         PC  ON (PC.cd_pessoa = CL.cd_pessoa_cobranca) " +
		          "LEFT OUTER JOIN grl_pessoa_fisica     PCF ON (PCF.cd_pessoa = CL.cd_pessoa_cobranca) " + 
		          "LEFT OUTER JOIN grl_pessoa_juridica   PCJ ON (PCJ.cd_pessoa = CL.cd_pessoa_cobranca) " + 
		          "LEFT OUTER JOIN grl_pessoa 	         EPC  ON (E.cd_conjuge = EPC.cd_pessoa) " + 
		          "LEFT OUTER JOIN adm_cobranca          CB  ON (CL.cd_cobranca = CB.cd_cobranca) " +
		          "LEFT OUTER JOIN adm_programa_fatura   CPF ON (CL.cd_programa_fatura = CPF.cd_programa_fatura) " +
		          "LEFT OUTER JOIN grl_pessoa_fisica     EPCF ON (EPCF.cd_pessoa = EPC.cd_pessoa) ": "") +
		          "LEFT OUTER JOIN grl_natureza_juridica I   ON (F.cd_natureza_juridica = I.cd_natureza_juridica) " +
		          "LEFT OUTER JOIN prc_orgao 			 PO ON (A.cd_pessoa = PO.cd_pessoa)" +
		          (agrupamentoCorrespondenteCidade ? 
		        		  "LEFT OUTER JOIN prc_cidade_orgao		 	PO1 ON (PO.cd_orgao = PO1.cd_orgao) "+
				          "LEFT OUTER JOIN grl_cidade			 	PO2 ON (PO1.cd_cidade = PO2.cd_cidade) "+
		        		  "LEFT OUTER JOIN grl_estado			 	PO21 ON (PO2.cd_estado = PO21.cd_estado) ":"")+
				         // "LEFT OUTER JOIN prc_cidade_orgao_servico PS ON (PO1.cd_cidade = PS.cd_cidade AND PO1.cd_orgao = PS.cd_orgao) ": "")+
		          (agrupamentoCorrespondenteEstado ? 
		        		  "LEFT OUTER JOIN prc_estado_orgao			PO4 ON (PO.cd_orgao = PO4.cd_orgao) "+
				          "LEFT OUTER JOIN grl_estado			 	PO3 ON (PO4.cd_estado = PO3.cd_estado) ":"")+
		        		  //"LEFT OUTER JOIN prc_estado_orgao_servico PS ON (PO4.cd_estado = PS.cd_estado AND PO4.cd_orgao = PS.cd_orgao)": "")+
		          "LEFT OUTER JOIN seg_usuario 			 UC ON (A.cd_usuario_cadastro = UC.cd_usuario) " +
		          "LEFT OUTER JOIN grl_pessoa			 UC1 ON (UC.cd_pessoa = UC1.cd_pessoa) "+
		          (cdCboOrCnaePrincipal ? 
		        		  "LEFT OUTER JOIN grl_cbo_pessoa_fisica L ON (A.cd_pessoa = L.cd_pessoa AND L.lg_principal = 1) " +
		        		  "LEFT OUTER JOIN grl_cnae_pessoa_juridica M ON (A.cd_pessoa = M.cd_pessoa AND M.lg_principal = 1) " : "") + " " +
		          (cdProprietario > 0 ? 
		        		  "JOIN agd_grupo_pessoa N ON (A.cd_pessoa = N.cd_pessoa) " +
		        		  "JOIN agd_grupo O ON (N.cd_grupo = O.cd_grupo AND (O.cd_proprietario = " + cdProprietario + " OR O.tp_visibilidade = " + GrupoServices.VIEW_PUBLICO + ")) " : "");
			if (ItemComparator.getItemComparatorByColumn(criterios, "J.CD_EMPRESA") != null) {
				sql += " LEFT OUTER JOIN grl_pessoa_empresa J ON (A.cd_pessoa = J.cd_pessoa) ";
			}
			else if (cdVinculo > 0 && cdEmpresa > 0 && !findProfessor) {
				sql += " JOIN grl_pessoa_empresa J ON (A.cd_pessoa = J.cd_pessoa"
						+ "											AND J.cd_vinculo = "+cdVinculo
						+ "											AND J.cd_empresa = "+cdEmpresa+") ";
			}
			if(lgMultiVinculo){
				sql += " JOIN grl_pessoa_empresa J ON (A.cd_pessoa = J.cd_pessoa) ";
				sql += " JOIN grl_vinculo JV ON (J.cd_vinculo = JV.cd_vinculo) ";
			}
			
			if (findEnderecoPrincipal) {
				sql += "LEFT OUTER JOIN grl_pessoa_endereco P ON (A.cd_pessoa = P.cd_pessoa AND P.lg_principal = 1) " +
					   "LEFT OUTER JOIN grl_tipo_logradouro Q ON (P.cd_tipo_logradouro = Q.cd_tipo_logradouro) " +
					   "LEFT OUTER JOIN grl_logradouro R ON (P.cd_logradouro = R.cd_logradouro) " +
					   "LEFT OUTER JOIN grl_tipo_logradouro S ON (R.cd_tipo_logradouro = S.cd_tipo_logradouro) " +
					   "LEFT OUTER JOIN grl_bairro T ON (P.cd_bairro = T.cd_bairro) " +
					   "LEFT OUTER JOIN grl_cidade U ON (P.cd_cidade = U.cd_cidade) " +
					   "LEFT OUTER JOIN grl_estado V ON (U.cd_estado = V.cd_estado) ";
			}
			if(findSupervisor) {
				sql += "JOIN seg_usuario SUSER ON (A.cd_pessoa = SUSER.cd_pessoa) " +
						"JOIN seg_usuario_grupo SUGRP ON (SUSER.cd_usuario = SUGRP.cd_usuario) " + 
						"JOIN seg_grupo SGRP ON (SUGRP.cd_grupo = SGRP.cd_grupo AND SGRP.cd_grupo = 2) ";
			}
			
			sql += "WHERE 1 = 1 " +
					(lgDocumentoAusente ? " AND (E.lg_documento_ausente=1 OR F.lg_documento_ausente=1) ":"")+
				(Util.getConfManager().getIdOfDbUsed().equals("FB") ? (!nmPessoa.equals("") ? " AND A.nm_pessoa LIKE '" + nmPessoa + "'":"") :  
					   ((!nmPessoa.equals("") ?
							   " AND TRANSLATE(A.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmPessoa+"' ": "")+
					    (!nmPessoaSocial.equals("") ? 
							   " AND (TRANSLATE(A.nm_pessoa,       'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmPessoaSocial+"' " +
							   "   OR TRANSLATE(F.nm_razao_social, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmPessoaSocial+"') ": "") 
					    )) +
				   (!agrupamentoCorrespondenteCidade && cdCidadeOrgao>0 ? 
						   " AND EXISTS (SELECT PO1.* "
						   + "            FROM prc_cidade_orgao PO1 "
						   + "            WHERE PO.cd_orgao = PO1.cd_orgao "
						   + "            AND PO1.cd_cidade = "+cdCidadeOrgao
						   + ")": "") +
				   (!agrupamentoCorrespondenteEstado && cdEstadoOrgao>0 ? 
						   " AND EXISTS (SELECT PO4.* "
						   + "	         FROM prc_estado_orgao PO4 "
						   + "           WHERE PO.cd_orgao = PO4.cd_orgao"
						   + "           AND PO4.cd_estado = "+cdEstadoOrgao
						   + ")": "") +
				   (cdSevicoOrgao>0 ? 
						     " AND (EXISTS (SELECT * FROM prc_cidade_orgao_servico WHERE PO.cd_orgao = cd_orgao AND cd_produto_servico = "+cdSevicoOrgao+
						     					(vlServicoMin!=null ? " AND vl_servico >= "+vlServicoMin : "") +
						     					(vlServicoMax!=null ? " AND vl_servico <= "+vlServicoMax : "") +
						     			   " ) "
						   + " 			OR "
						   + "		EXISTS (SELECT * FROM prc_estado_orgao_servico WHERE PO.cd_orgao = cd_orgao AND cd_produto_servico = "+cdSevicoOrgao+
						   						(vlServicoMin!=null ? " AND vl_servico >= "+vlServicoMin : "") +
						     					(vlServicoMax!=null ? " AND vl_servico <= "+vlServicoMax : "") +
						   				   " ) "
						   + " )" : "")
				   + "";
		
			String orderBy = " ORDER BY A.nm_pessoa "+sqlLimit[1];
			
			LogUtils.debug("SQL:\n"+Search.getStatementSQL(sql, orderBy, crt, true));
			
			ResultSetMap rsm = Search.find(sql + " ", orderBy, crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			/**
			 * INJECAO DE DADOS ADICIONAIS NO RESULTADO
			 */
			
			LogUtils.logTimer("PESSOA_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			LogUtils.debug("PessoaServices.find: Iniciando injeção de dados adicionais...");
			

			int cdInstituicaoSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0, 0, connect);
			int cdVinculoProfessor = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_PROFESSOR", 0, 0, connect);
			int cdVinculoCliente = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0, 0, connect);
			
			ResultSetMap rsmFinal = new ResultSetMap();
			while(rsm.next()){	
				
				rsm.setValueToField("NR_CPF_CNPJ", (rsm.getString("nr_cpf") != null && !rsm.getString("nr_cpf").equals("") ? rsm.getString("nr_cpf") : rsm.getString("nr_cnpj")));
				
				
				ResultSetMap rsmParametrosValor = new ResultSetMap(connect.prepareStatement("SELECT B.nm_parametro, A.vl_inicial FROM grl_parametro_valor A " +
																				"		    JOIN  grl_parametro B ON (A.cd_parametro = B.cd_parametro) " +
																				"			WHERE A.cd_pessoa = " + rsm.getString("cd_pessoa")).executeQuery());
				while(rsmParametrosValor.next()){
					rsm.setValueToField(rsmParametrosValor.getString("nm_parametro"), rsmParametrosValor.getObject("vl_inicial"));
				}
				if(ClienteDAO.get(cdEmpresa, rsm.getInt("cd_pessoa"), connect) == null)
					rsm.setValueToField("LG_CLIENTE", 0);
				else
					rsm.setValueToField("LG_CLIENTE", 1);
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", "" + rsm.getInt("cd_pessoa"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa","" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_vinculo", "" + cdVinculoCliente, ItemComparator.EQUAL, Types.INTEGER));
				if(PessoaEmpresaDAO.find(criterios, connect).next())
					rsm.setValueToField("LG_CLIENTE_VINCULO", 1);
				else
					rsm.setValueToField("LG_CLIENTE_VINCULO", 0);
				
				ResultSetMap rsmPessoaNecessidadeEspecial = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_pessoa_necessidade_especial WHERE cd_pessoa = " + rsm.getInt("cd_pessoa")).executeQuery());
				if(rsmPessoaNecessidadeEspecial.next())
					rsm.setValueToField("TP_POSSUI_DEFICIENCIA", 1);
				else
					rsm.setValueToField("TP_POSSUI_DEFICIENCIA", 2);
				
				if(cdVinculo > 0 && cdVinculo == cdVinculoProfessor){
					rsm.setValueToField("TP_POS_GRADUACAO", (rsm.getInt("lg_pos") == 1 ? 0 : (rsm.getInt("lg_mestrado") == 1 ? 1 : (rsm.getInt("lg_doutorado") == 1 ? 2 : 3))));
				}
				

				if(lgGetVinculos) {
					ResultSetMap rsmVinculos = getAllVinculosOfPessoa(rsm.getInt("cd_pessoa"), cdEmpresa, connect);
					String dsVinculo = "";
					while(rsmVinculos.next()) {
						dsVinculo += rsmVinculos.getString("nm_vinculo");
						if(rsmVinculos.hasMore())
							dsVinculo+=", ";
					}
					rsm.setValueToField("DS_VINCULOS", dsVinculo);
				}
				
				if(findNotProfessorSecretaria){
					if(PessoaEmpresaDAO.get(cdInstituicaoSecretaria, rsm.getInt("cd_pessoa"), cdVinculoProfessor, connect) == null){
						rsmFinal.addRegister(rsm.getRegister());
					}
				}
				else{
					rsmFinal.addRegister(rsm.getRegister());
				}
				
			}
			
			rsmFinal.beforeFirst();
			
			LogUtils.debug(rsmFinal.size() + " registro(s)");
			LogUtils.logTimer("PESSOA_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			LogUtils.destroyTimer("PESSOA_FIND_TIMER");
			
			return rsmFinal;
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

	public ResultSetMap findCorrespondente(ArrayList<ItemComparator> criterios) {
		return findCorrespondente(criterios, null);
	}
	
	public ResultSetMap findCorrespondente(ArrayList<ItemComparator> criterios, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ResultSetMap rsm = find(criterios, connect);
			while(rsm.next()) {
				//comarcas
				ResultSetMap rsmComarca = CidadeOrgaoServices.getAllCidadeByOrgao(rsm.getInt("cd_orgao"), connect);
				
				while(rsmComarca.next()) {
					ResultSetMap rsmServico = CidadeOrgaoServicoServices.getServicos(rsm.getInt("cd_orgao"), rsmComarca.getInt("cd_cidade"), connect);
					while(rsmServico.next()) {
						rsmServico.setValueToField("DS_VL_SERVICO", Util.formatNumber(rsmServico.getDouble("vl_servico"), 2));
					} rsmServico.beforeFirst();
					rsmComarca.setValueToField("RSM_SERVICO_COMARCA", rsmServico);
				} 
				rsmComarca.beforeFirst();
				rsm.setValueToField("RSM_COMARCAS", rsmComarca);
				
				//estados
				ResultSetMap rsmEstado = EstadoOrgaoServices.getAllEstadoByOrgao(rsm.getInt("cd_orgao"), connect);
				
				while(rsmEstado.next()) {
					ResultSetMap rsmServico = EstadoOrgaoServicoServices.getServicos(rsm.getInt("cd_orgao"), rsmEstado.getInt("cd_estado"), connect);
					while(rsmServico.next()) {
						rsmServico.setValueToField("DS_VL_SERVICO", Util.formatNumber(rsmServico.getDouble("vl_servico"), 2));
					} rsmServico.beforeFirst();
					rsmEstado.setValueToField("RSM_SERVICO_ESTADO", rsmServico);
				} 
				rsmEstado.beforeFirst();
				rsm.setValueToField("RSM_ESTADOS", rsmEstado);
				
			} rsm.beforeFirst();
			
			return rsm;
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
	
	
	public static ResultSetMap findParticipantes(ArrayList<ItemComparator> criterios) {
		return findParticipantes(criterios, null);
	}

	public static ResultSetMap findParticipantes(ArrayList<ItemComparator> criterios, Connection connect) {
		int cdAgenda = 0;
		boolean existsUser = false;
		int cdVinculo = 0;
		int cdEmpresa = 0;
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("cdAgenda")) {
				cdAgenda = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
				criterios.remove(i);
				i--;
			}
			else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("existsUser")) {
				existsUser = true;
				criterios.remove(i);
				i--;
			}
			else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("cdVinculo")) {
				cdVinculo = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
				criterios.remove(i);
				i--;
			}
			else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("cdEmpresa")) {
				cdEmpresa = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
				criterios.remove(i);
				i--;
			}
		}
		String sql = "SELECT A.* " +
				"FROM grl_pessoa A " +
				"WHERE 1=1 " +
				"  AND NOT EXISTS (SELECT B.cd_participante " +
				"				   FROM agd_agenda_participante B " +
				"				   WHERE B.cd_participante = A.cd_pessoa " +
				"					 AND B.st_participante = " + AgendaParticipanteServices.ST_ATIVO + " " +
				"					 AND B.cd_agenda = " + cdAgenda + ") " +
				(cdVinculo<=0 ? "" : "  AND A.cd_pessoa IN (SELECT cd_pessoa " +
				"					   FROM grl_pessoa_empresa " +
				"					   WHERE cd_vinculo = " + cdVinculo + " " +
				"						 " + (cdEmpresa<=0 ? "" : " AND cd_empresa = " + cdEmpresa) +") ") +
				(existsUser ? "  AND A.cd_pessoa IN (SELECT B.cd_pessoa " +
				"					   FROM seg_usuario B " +
				"					   WHERE B.cd_pessoa = A.cd_pessoa)" : "");
		return Search.find(sql, " ORDER BY A.nm_pessoa ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap findPessoa(int cdEmpresa, int codigoInicial, int codigoFinal, String letraInicial, String letraFinal, int quantidadePesquisa, int cdVinculoPesquisa) {
		return findPessoa(cdEmpresa, codigoInicial, codigoFinal, letraInicial, letraFinal, quantidadePesquisa, cdVinculoPesquisa, null);
	}

	public static ResultSetMap findPessoa(int cdEmpresa, int codigoInicial, int codigoFinal, String letraInicial, String letraFinal, int quantidadePesquisa, int cdVinculoPesquisa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			
			if(codigoInicial > 0)
				criterios.add(new ItemComparator("A.cd_pessoa", String.valueOf(codigoInicial), ItemComparator.GREATER_EQUAL, Types.VARCHAR));
			if(codigoFinal > 0)	
				criterios.add(new ItemComparator("A.cd_pessoa", String.valueOf(codigoFinal), ItemComparator.MINOR_EQUAL, Types.VARCHAR));
			
			char lInicial = '0';
			char lFinal   = '0';
			
			if(letraInicial.length() == 1)
				lInicial = letraInicial.charAt(0);
			if(letraFinal.length() == 1)
				lFinal = letraFinal.charAt(0);
			
			if(lInicial != '0')
				criterios.add(new ItemComparator("lInicial", String.valueOf(lInicial), ItemComparator.GREATER_EQUAL, Types.VARCHAR));
			if(lFinal != '0')
				criterios.add(new ItemComparator("lFinal", String.valueOf((char)(lFinal+1)), ItemComparator.MINOR_EQUAL, Types.VARCHAR));
			
			if(cdVinculoPesquisa > 0)
				criterios.add(new ItemComparator("cd_vinculo", String.valueOf(cdVinculoPesquisa), ItemComparator.EQUAL, Types.INTEGER));
			
			criterios.add(new ItemComparator("qtLimite", String.valueOf(quantidadePesquisa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_empresa", String.valueOf(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
//			criterios.add(new ItemComparator("ordenar", "1", ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("findEnderecoPrincipal", String.valueOf(0), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("findCliente", String.valueOf(0), ItemComparator.EQUAL, Types.INTEGER));
			return find(criterios);
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
	
	public static ResultSetMap findPessoa(int cdPessoa, int cdEmpresa) {
		return findPessoa(cdPessoa, cdEmpresa, null);
	}

	public static ResultSetMap findPessoa(int cdPessoa, int cdEmpresa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_pessoa", String.valueOf(cdPessoa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_empresa", String.valueOf(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("findEnderecoPrincipal", "1", ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("findCliente", "TRUE", ItemComparator.EQUAL, Types.INTEGER));
			return find(criterios);
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
	
	public static String getNrCpfCnpj(int cdPessoa) {
		return getNrCpfCnpj(cdPessoa, null);
	}

	public static String getNrCpfCnpj(int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			if(cdPessoa<=0) 
				return null;
			
			String sql = "SELECT A.gn_pessoa, B.nr_cpf, C.nr_cnpj FROM grl_pessoa A " +
						" LEFT OUTER JOIN grl_pessoa_fisica B ON (A.cd_pessoa = B.cd_pessoa) " +
						" LEFT OUTER JOIN grl_pessoa_juridica C ON (A.cd_pessoa = C.cd_pessoa) " +
						" WHERE A.cd_pessoa = ?";
			
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdPessoa);	
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if (rsm!= null && rsm.getLines().size()>0) {
				rsm.next();
				if (rsm.getInt("GN_PESSOA") == TP_FISICA)
					return rsm.getString("NR_CPF");
				else
					return rsm.getString("NR_CNPJ");
			}
			
			return null;	
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getNrCpfCnpj: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Pessoa get(int cdPessoa){
		return get(cdPessoa, null);
	}
	
	public static Pessoa get(int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			return PessoaDAO.get(cdPessoa, connect);
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	public static ResultSetMap getAsResultSet(int cdPessoa) {
		return getAsResultSet(cdPessoa, false, 0, null);
	}

	public static ResultSetMap getAsResultSet(int cdPessoa, boolean getCboOrCnaePrincipal, int cdProprietario) {
		return getAsResultSet(cdPessoa, getCboOrCnaePrincipal, cdProprietario, null);
	}

	public static ResultSetMap getAsResultSet(int cdPessoa, boolean getCboOrCnaePrincipal, int cdProprietario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			String sql =
	 			  "SELECT A.*, " +
				  "   B.nm_pais, " +
				  "   C.nm_forma_divulgacao, " +
				  "	  D.nm_classificacao, D.id_classificacao, " +
				  "   E.nr_cpf, E.cd_escolaridade, " +
				  "     E.dt_nascimento, E.nr_cpf, E.sg_orgao_rg, E.nm_mae, " +
				  "     E.nm_pai, E.tp_sexo, E.st_estado_civil, " +
				  "     E.nr_rg, E.nr_cnh, E.dt_validade_cnh, E.dt_primeira_habilitacao, " +
				  "     E.tp_categoria_habilitacao, E.tp_raca, E.lg_deficiente_fisico, " +
				  "     E.nm_forma_tratamento, E.cd_estado_rg, " +
				  "	  F.nr_cnpj, F.nm_razao_social, F.nr_inscricao_estadual, " +
				  "     F.nr_inscricao_municipal, F.nr_funcionarios, F.dt_inicio_atividade, " +
				  "     F.tp_empresa, F.cd_natureza_juridica, " +
				  "   G.cd_cidade AS cd_naturalidade, G.nm_cidade AS nm_naturalidade, " +
				  "	  H.nm_escolaridade, H.id_escolaridade, " +
			      "   I.id_natureza_juridica, I.nm_natureza_juridica " +
			      (cdProprietario > 0 ? ", N.cd_grupo, O.nm_grupo " : "") +
			      (getCboOrCnaePrincipal ? "	  , M.cd_cnae, L.cd_cbo " : "") +
			      "FROM grl_pessoa A " +
				  "LEFT OUTER JOIN grl_pais B ON (A.cd_pais = B.cd_pais) " +
				  "LEFT OUTER JOIN grl_forma_divulgacao C ON (A.cd_forma_divulgacao = C.cd_forma_divulgacao) " +
				  "LEFT OUTER JOIN adm_classificacao D ON (A.cd_classificacao = D.cd_classificacao) " +
				  "LEFT OUTER JOIN grl_pessoa_fisica E ON (A.cd_pessoa = E.cd_pessoa) " +
				  "LEFT OUTER JOIN grl_pessoa_juridica F ON (A.cd_pessoa = F.cd_pessoa) " +
		      	  "LEFT OUTER JOIN grl_cidade G ON (E.cd_naturalidade = G.cd_cidade) " +
		          "LEFT OUTER JOIN grl_escolaridade H ON (E.cd_escolaridade = H.cd_escolaridade) " +
		          "LEFT OUTER JOIN grl_natureza_juridica I ON (F.cd_natureza_juridica = I.cd_natureza_juridica) " +
		          (getCboOrCnaePrincipal ? "LEFT OUTER JOIN grl_cbo_pessoa_fisica L ON (A.cd_pessoa = L.cd_pessoa AND L.lg_principal = 1) " +
		          "LEFT OUTER JOIN grl_cnae_pessoa_juridica M ON (A.cd_pessoa = M.cd_pessoa AND M.lg_principal = 1) " : "") + " " +
		          (cdProprietario > 0 ? "JOIN agd_grupo_pessoa N ON (A.cd_pessoa = N.cd_pessoa) " +
		          "JOIN agd_grupo O ON (N.cd_grupo = O.cd_grupo AND (O.cd_proprietario = " + cdProprietario + " OR O.tp_visibilidade = " + GrupoServices.VIEW_PUBLICO + ")) " : "");
			sql += " WHERE A.cd_pessoa = " + cdPessoa;
			Statement pstmt = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			return new ResultSetMap(pstmt.executeQuery(sql), false);
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

	public static ResultSetMap findConveniados(int cdPessoa, int cdEmpresa){
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("L.CD_CONVENIO", String.valueOf(cdPessoa), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		criterios.add(new ItemComparator("J.CD_EMPRESA", String.valueOf(cdEmpresa), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		
		return findPessoaEmpresa(criterios);
		
	}
	
	public static ResultSetMap findPessoaEmpresa(ArrayList<ItemComparator> criterios) {
		return findPessoaEmpresa(criterios, null);
	}

	public static ResultSetMap findPessoaEmpresa(ArrayList<ItemComparator> criterios, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			int lgConveniados = 0;
			for(int i = 0; i < criterios.size(); i++){
				if(criterios.get(i).getColumn().equals("lgConveniados")){
					lgConveniados = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
				}
			}
			connection = isConnectionNull ? Conexao.conectar() : connection;
			String sql = "SELECT * " +
					     "FROM grl_pessoa A " +
						 "LEFT OUTER JOIN grl_pais B ON (A.cd_pais = B.cd_pais) " +
						 "LEFT OUTER JOIN grl_pessoa_fisica E ON (A.cd_pessoa = E.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_pessoa_juridica F ON (A.cd_pessoa = F.cd_pessoa) " +
				      	 "LEFT OUTER JOIN grl_cidade G ON (E.cd_naturalidade = G.cd_cidade) "+
						 "LEFT OUTER JOIN grl_pessoa_empresa J ON (A.cd_pessoa = J.cd_pessoa)" +
				      	 "LEFT OUTER JOIN adm_cliente L ON (L.cd_pessoa = A.cd_pessoa " +
				      	 "										AND L.cd_empresa = J.cd_empresa) ";
			ResultSetMap rsm = Search.find(sql, "ORDER BY A.nm_pessoa", criterios, connection, false);
			ResultSetMap rsmFinal = new ResultSetMap();
			if(lgConveniados > 0){
				while(rsm.next()){
					ResultSetMap rsmConv = new ResultSetMap(connection.prepareStatement("SELECT * FROM grl_parametro_valor GPV " +
							"															JOIN grl_parametro GP ON (GPV.cd_parametro = GP.cd_parametro" +
							"																							AND GP.nm_parametro = 'lgConveniados')" +
							"																WHERE GPV.cd_pessoa = " + rsm.getInt("cd_pessoa") + 
																									" AND GPV.vl_inicial = '1'").executeQuery());
					//System.out.println("rsmConv = " + rsmConv);
					if(rsmConv.next()){
						rsmFinal.addRegister(rsm.getRegister());
					}
				}
			}else {
				rsmFinal = rsm;
			}
			rsm.beforeFirst();
			rsmFinal.beforeFirst();
			return rsmFinal;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap findAllCartorios(ArrayList<ItemComparator> criterios) {
		return findAllCartorios(criterios, null);
	}

	public static ResultSetMap findAllCartorios(ArrayList<ItemComparator> criterios, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			
			String qtLimite = ""; 
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
					qtLimite = criterios.get(i).getValue();
				}
				else
					crt.add(criterios.get(i));
			}

			crt.add(new ItemComparator("B.cd_vinculo", ParametroServices.getValorOfParametro("CD_VINCULO_CARTORIO"), ItemComparator.EQUAL, Types.INTEGER));
			
			connection = isConnectionNull ? Conexao.conectar() : connection;
			String sql = "SELECT A.*, C.*, D.*, A.cd_pessoa AS cd_cartorio, A.nm_pessoa AS nm_cartorio, G.nm_cidade, H.sg_estado " +
					     "FROM grl_pessoa A " +
						 "JOIN grl_pessoa_empresa B ON (A.cd_pessoa = B.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_pessoa_endereco C ON (A.cd_pessoa = C.cd_pessoa " + 
						 "											        AND C.lg_principal = 1) " +
						 "JOIN grl_pessoa_juridica D ON (A.cd_pessoa = D.cd_pessoa) " +
				      	 "LEFT OUTER JOIN grl_cidade G ON (C.cd_cidade = G.cd_cidade) " +
				      	 "LEFT OUTER JOIN grl_estado H ON (G.cd_estado = H.cd_estado) ";
			//ResultSetMap rsm = Search.find(sql, "ORDER BY A.nm_pessoa", criterios, connection, false, false);
			ResultSetMap rsm = Search.find(sql, "ORDER BY A.nm_pessoa" + (!qtLimite.equals("") ? " LIMIT " + qtLimite : ""), crt, connection, false);
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	

	public static ResultSetMap findPessoaEmpresaAndEndereco(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy) {
		return findPessoaEmpresaAndEndereco(criterios, groupBy, null);
	}

	public static ResultSetMap findPessoaEmpresaAndEndereco(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			int cdEmpresa = 0;
			int cdVinculo = 0;
			int stVinculo = 1;
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++)
				if (criterios.get(i).getColumn().indexOf("cd_empresa") >= 0)
					cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().indexOf("cd_vinculo") >= 0)
					cdVinculo = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().indexOf("st_vinculo") >= 0)
					stVinculo = Integer.parseInt(criterios.get(i).getValue());
				else if (!criterios.get(i).getColumn().equalsIgnoreCase("lgEnderecoPrincipal"))
					crt.add(criterios.get(i));

			String groups = "";
			String fields = " A.*, B.*, C.*, F.*, " +
						    " G.nm_cidade, H.nm_bairro AS nm_bairro_enderecamento, I.nm_logradouro AS nm_logradouro_enderecamento, " +
						    " J.nm_tipo_logradouro, J.sg_tipo_logradouro, L.nm_distrito, M.nm_estado, M.sg_estado, O.nm_pais, P.nm_forma_divulgacao, " +
						    " O1.nm_regiao AS nm_regiao_pais, O2.nm_regiao AS nm_regiao_estado, O3.nm_regiao AS nm_regiao_cidade "+
						    ((cdEmpresa>0)?" ,Q.*,Q2.* ":"");
			// Processa agrupamentos enviados em groupBy
			String [] retorno = com.tivic.manager.util.Util.getFieldsAndGroupBy(groupBy, fields, groups, "COUNT(*) AS QT_PESSOA");
			fields = retorno[0];
			groups = retorno[1];

			String sql =  "SELECT " + fields + " " +
					      "FROM grl_pessoa A " +
						  "LEFT OUTER JOIN grl_pessoa_juridica  B ON (A.cd_pessoa = B.cd_pessoa) " +
						  "LEFT OUTER JOIN grl_pessoa_fisica    C ON (A.cd_pessoa = C.cd_pessoa) " +
						  // Endereçamento
						  "LEFT OUTER JOIN grl_pessoa_endereco  F ON (A.cd_pessoa   = F.cd_pessoa " +
						  " 									  AND F.lg_principal = 1) " +
				      	  "LEFT OUTER JOIN grl_cidade           G  ON (F.cd_cidade = G.cd_cidade) "+
				      	  "LEFT OUTER JOIN grl_bairro           H  ON (F.cd_bairro = H.cd_bairro) " +
				      	  "LEFT OUTER JOIN grl_logradouro       I  ON (F.cd_logradouro = I.cd_logradouro) " +
				      	  "LEFT OUTER JOIN grl_tipo_logradouro  J  ON (I.cd_tipo_logradouro = J.cd_tipo_logradouro)"+
				      	  "LEFT OUTER JOIN grl_distrito         L  ON (I.cd_distrito = L.cd_distrito) "+
				      	  "LEFT OUTER JOIN grl_estado           M  ON (G.cd_estado = M.cd_estado) "+
				      	  "LEFT OUTER JOIN grl_pais		  	    O  ON (M.cd_pais   = O.cd_pais) " +
				      	  "LEFT OUTER JOIN grl_regiao		    O1 ON (O.cd_regiao = O1.cd_regiao) " +
				      	  "LEFT OUTER JOIN grl_regiao		    O2 ON (M.cd_regiao = O2.cd_regiao) " +
				      	  "LEFT OUTER JOIN grl_regiao		    O3 ON (G.cd_regiao = O3.cd_regiao) " +
				      	  "LEFT OUTER JOIN grl_forma_divulgacao P ON (A.cd_forma_divulgacao = P.cd_forma_divulgacao) " +
				      	  ((cdEmpresa>0)?
				      	  " LEFT OUTER JOIN grl_pessoa_empresa Q on ( A.cd_pessoa = Q.cd_pessoa AND Q.cd_empresa = "+cdEmpresa+" ) "+
				      	  " LEFT OUTER JOIN grl_vinculo Q2 ON ( Q.cd_vinculo = Q2.cd_vinculo ) ":"")+
				      	  "WHERE 1=1 "+
				      	  (cdEmpresa>0 || cdVinculo>0 ? " AND EXISTS (SELECT * FROM grl_pessoa_empresa D " +
				      	  		                        "             WHERE A.cd_pessoa  = D.cd_pessoa " +
				      	  		                        "               AND D.st_vinculo = " +stVinculo+
				      	  		                        (cdEmpresa>0?"  AND D.cd_empresa = "+cdEmpresa : "")+
				      	  		                        (cdVinculo>0?"  AND D.cd_vinculo = "+cdVinculo : "")+")" : "");
			return Search.find(sql, groups+(groupBy.size()==0?" ORDER BY A.nm_pessoa":""), crt, connect, false);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * @category JURIS
	 *
	 * OBS: 'delete' é uma palavra reservada no AS3.
	 * Para fins de chamada remota costumo usar remove ou deleteNomeDaEntidade
	 */
	public static Result remove(int cdPessoa) {
		return remove(cdPessoa, -1, false, null);
	}
	
	public static Result remove(int cdPessoa, boolean cascade) {
		return remove(cdPessoa, -1, cascade, null);
	}
	
	public static Result remove(int cdPessoa, int gnPessoa, boolean cascade) {
		return remove(cdPessoa, gnPessoa, cascade, null);
	}
	
	public static Result remove(int cdPessoa, int gnPessoa, boolean cascade, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				/* remocao de necessidades especiais */
				PreparedStatement pstmt = connect.prepareStatement("DELETE " +
						"FROM grl_pessoa_necessidade_especial " +
						"WHERE cd_pessoa = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();
				
				/* remocao de doencas */
				pstmt = connect.prepareStatement("DELETE " +
						"FROM grl_pessoa_doenca " +
						"WHERE cd_pessoa = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();
				
				/* remocao de alergias */
				pstmt = connect.prepareStatement("DELETE " +
						"FROM grl_pessoa_alergia " +
						"WHERE cd_pessoa = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();
				
				/* remocao de ficha medica */
				pstmt = connect.prepareStatement("DELETE " +
						"FROM grl_pessoa_ficha_medica " +
						"WHERE cd_pessoa = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();
				
				/* remocao de vinculos */
				pstmt = connect.prepareStatement("DELETE " +
						"FROM grl_pessoa_empresa " +
						"WHERE cd_pessoa = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();

				/* remocao de vinculos */
				pstmt = connect.prepareStatement("DELETE " +
						"FROM agd_grupo_pessoa " +
						"WHERE cd_pessoa = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();

				/* remocao de contatos por endereco */
				pstmt = connect.prepareStatement("DELETE " +
						"FROM grl_pessoa_contato " +
						"WHERE cd_pessoa_juridica = ? ");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();

				/* remocao de enderecos */
				pstmt = connect.prepareStatement("DELETE " +
						"FROM grl_pessoa_endereco " +
						"WHERE cd_pessoa = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();

				/* remocao de relacionamentos com participantes de agenda/agendamento */
				pstmt = connect.prepareStatement("DELETE " +
						"FROM agd_agendamento_participante " +
						"WHERE cd_participante = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();

				pstmt = connect.prepareStatement("DELETE " +
						"FROM agd_agenda_participante " +
						"WHERE cd_participante = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();

				/* remocao de ocorrencias */
				pstmt = connect.prepareStatement("DELETE " +
						"FROM grl_ocorrencia " +
						"WHERE cd_pessoa = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();

				/* remocao de contas bancarias */
				pstmt = connect.prepareStatement("DELETE " +
						"FROM grl_pessoa_conta_bancaria " +
						"WHERE cd_pessoa = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();
				
				/* remocao de dados funcionais */
				pstmt = connect.prepareStatement("DELETE " +
						"FROM srh_dados_funcionais " +
						"WHERE cd_pessoa = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();

				/* remocao de documentos */
				pstmt = connect.prepareStatement("DELETE " +
						"FROM grl_tipo_documento_pessoa " +
						"WHERE cd_pessoa = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();
				
				/* remoção de documento_pessoa*/
				pstmt = connect.prepareStatement("DELETE " +
						"FROM ptc_documento_pessoa " +
						"WHERE cd_pessoa = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();
				
				/* remoção de parte cliente*/
				pstmt = connect.prepareStatement("DELETE " +
						"FROM prc_parte_cliente " +
						"WHERE cd_pessoa = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();
				
				/* remoção de outra parte*/
				pstmt = connect.prepareStatement("DELETE " +
						"FROM prc_outra_parte " +
						"WHERE cd_pessoa = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();
				
				/* remoção de orgao*/
				pstmt = connect.prepareStatement("DELETE " +
						"FROM prc_orgao " +
						"WHERE cd_pessoa = ?");
				pstmt.setInt(1, cdPessoa);
				pstmt.execute();
				
				retorno = 1;
			}
				
			if(!cascade || retorno>0) {
				if (gnPessoa == -1) {
					Pessoa pessoa = PessoaDAO.get(cdPessoa, connect);
					gnPessoa = pessoa.getGnPessoa();
				}
				if(gnPessoa==TP_JURIDICA)
					retorno = PessoaJuridicaServices.delete(cdPessoa, false, connect);
				else
					retorno = PessoaFisicaServices.delete(cdPessoa, false, connect);
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta pessoa está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Pessoa excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir pessoa!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa) {
		return delete(cdPessoa, -1, null);
	}

	public static int delete(int cdPessoa, int gnPessoa) {
		return delete(cdPessoa, gnPessoa, null);
	}

	public static int delete(int cdPessoa, int gnPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			/* remocao de vinculos */
			PreparedStatement pstmt = connect.prepareStatement("DELETE " +
					"FROM grl_pessoa_empresa " +
					"WHERE cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();

			/* remocao de vinculos */
			pstmt = connect.prepareStatement("DELETE " +
					"FROM agd_grupo_pessoa " +
					"WHERE cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();

			/* remocao de contatos por endereco */
			pstmt = connect.prepareStatement("DELETE " +
					"FROM grl_pessoa_contato " +
					"WHERE cd_pessoa = ? ");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();

			/* remocao de enderecos */
			pstmt = connect.prepareStatement("DELETE " +
					"FROM grl_pessoa_endereco " +
					"WHERE cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();

			/* remocao de relacionamentos com participantes de agenda/agendamento */
			pstmt = connect.prepareStatement("DELETE " +
					"FROM agd_agendamento_participante " +
					"WHERE cd_participante = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();

			pstmt = connect.prepareStatement("DELETE " +
					"FROM agd_agenda_participante " +
					"WHERE cd_participante = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();

			/* remocao de ocorrencias */
			pstmt = connect.prepareStatement("DELETE " +
					"FROM grl_ocorrencia " +
					"WHERE cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();

			/* remocao de contas bancarias */
			pstmt = connect.prepareStatement("DELETE " +
					"FROM grl_pessoa_conta_bancaria " +
					"WHERE cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();
			
			/* remocao de dados funcionais */
			pstmt = connect.prepareStatement("DELETE " +
					"FROM srh_dados_funcionais " +
					"WHERE cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();

			/* remocao de documentos */
			pstmt = connect.prepareStatement("DELETE " +
					"FROM grl_tipo_documento_pessoa " +
					"WHERE cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();
			
			/* remocao de parametros */
			pstmt = connect.prepareStatement("DELETE " +
					"FROM grl_parametro_valor " +
					"WHERE cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();

			if (gnPessoa == -1) {
				Pessoa pessoa = PessoaDAO.get(cdPessoa, connect);
				gnPessoa = pessoa.getGnPessoa();
			}
			int r = 0;
			if(gnPessoa==TP_JURIDICA)
				r = PessoaJuridicaServices.delete(cdPessoa, false, connect);
			else
				r = PessoaFisicaServices.delete(cdPessoa, false, connect);
			if (r <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllContaBancaria(int cdPessoa) {
		return getAllContaBancaria(cdPessoa, null);
	}
	
	public static ResultSetMap getAllContaBancaria(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			String sql =  "SELECT A.*, B.nr_banco, B.nm_banco "+
			  			  "FROM grl_pessoa_conta_bancaria A " +
			              "LEFT OUTER JOIN grl_banco B ON (A.cd_banco = B.cd_banco) "+
			              "WHERE A.cd_pessoa = "+cdPessoa;
			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getAllContaBancaria: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllAtividadeEconomica(int cdPessoa) {
		Connection connect = Conexao.conectar();
		try {
			String sql =  "SELECT A.*, B.nr_banco, B.nm_banco "+
			  			  "FROM grl_pessoa A " +
			              "LEFT OUTER JOIN grl_banco B ON (A.cd_banco = B.cd_banco) "+
			              "WHERE A.cd_pessoa = "+cdPessoa;
			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getAllContaBancaria: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllFormacoes(int cdPessoa) {
		return getAllFormacoes(cdPessoa, null);
	}

	public static ResultSetMap getAllFormacoes(int cdPessoa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, " +
					"B.nm_pessoa AS nm_instituicao, C.cd_area_conhecimento " +
					"FROM acd_formacao A " +
					"LEFT OUTER JOIN grl_pessoa B ON (A.cd_instituicao = B.cd_pessoa) " +
					"LEFT OUTER JOIN acd_formacao_area_conhecimento C ON (A.cd_pessoa = C.cd_pessoa AND " +
					"													  A.cd_formacao = C.cd_formacao AND " +
					"													  C.cd_area_conhecimento = (SELECT MAX(D.cd_area_conhecimento) " +
					"																				FROM acd_formacao_area_conhecimento D " +
					"																				WHERE D.cd_pessoa = A.cd_pessoa " +
					"																				  AND D.cd_formacao = A.cd_formacao))" +
					"WHERE A.cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getAllFormacoes: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllAgenciasFinanciadoras(int cdEmpresa) {
		return getAllAgenciasFinanciadoras(cdEmpresa, null);
	}

	public static ResultSetMap getAllAgenciasFinanciadoras(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		PreparedStatement pstmt = null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			int cdVinculo = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_AGENCIA_FINANCIADORA", 0);
			pstmt = cdVinculo<=0 ? null : connection.prepareStatement("SELECT * FROM grl_pessoa A, grl_pessoa_empresa B " +
																	  "WHERE A.cd_pessoa = B.cd_pessoa " +
																	  "  AND B.cd_empresa = ? " +
																	  "  AND B.cd_vinculo = ?");
			if (pstmt != null) {
				pstmt.setInt(1, cdEmpresa);
				pstmt.setInt(2, cdVinculo);
			}
			ResultSetMap rsm = new ResultSetMap(pstmt==null ? null :  pstmt.executeQuery());
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_PESSOA");
			rsm.orderBy(fields);
			if(pstmt!=null)
				pstmt.close();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getAllAgenciasFinanciadoras: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getAllCartorios() {
		return getAllCartorios(null);
	}

	public static ResultSetMap getAllCartorios(Connection connection) {
		boolean isConnectionNull = connection==null;
		PreparedStatement pstmt = null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			int cdVinculo = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CARTORIO", 0);
			pstmt = cdVinculo<=0 ? null : connection.prepareStatement("SELECT A.*, C.*, D.*, A.cd_pessoa AS cd_cartorio, A.nm_pessoa AS nm_cartorio, G.nm_cidade, H.sg_estado " +
																     "     FROM grl_pessoa A " +
																	 " JOIN grl_pessoa_empresa B ON (A.cd_pessoa = B.cd_pessoa) " +
																	 " LEFT OUTER JOIN grl_pessoa_endereco C ON (A.cd_pessoa = C.cd_pessoa " + 
																	 "											        AND C.lg_principal = 1) " +
																	 " JOIN grl_pessoa_juridica D ON (A.cd_pessoa = D.cd_pessoa) " +
															      	 " LEFT OUTER JOIN grl_cidade G ON (C.cd_cidade = G.cd_cidade) " +
															      	 " LEFT OUTER JOIN grl_estado H ON (G.cd_estado = H.cd_estado) " +
																	 "WHERE B.cd_vinculo = ?");
			if (pstmt != null) {
				pstmt.setInt(1, cdVinculo);
			}
			
			ResultSetMap rsm = new ResultSetMap(pstmt==null ? null :  pstmt.executeQuery());
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_PESSOA");
			rsm.orderBy(fields);
			if(pstmt!=null)
				pstmt.close();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getAllAgenciasFinanciadoras: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getVendedores(int cdEmpresa) {
		return getVendedores(cdEmpresa, null);
	}

	public static ResultSetMap getVendedores(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			int cdVinculo = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_VENDEDOR", 0);
			if(cdVinculo <= 0)
				return new ResultSetMap();
			//
			return new ResultSetMap(connection.prepareStatement("SELECT A.cd_pessoa, A.nm_pessoa " +
																"FROM grl_pessoa A, grl_pessoa_empresa B " +
																"WHERE A.cd_pessoa  = B.cd_pessoa " +
																"  AND B.cd_empresa = " +cdEmpresa+
																"  AND B.cd_vinculo = " +cdVinculo+
																" ORDER BY nm_pessoa ").executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getVendedores: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	public static ResultSetMap getFornecedores() {
		return getFornecedores(null);
	}

	public static ResultSetMap getFornecedores(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			int cdVinculo = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_FORNECEDOR", 0);
			if(cdVinculo <= 0)
				return new ResultSetMap();
			//
			return new ResultSetMap(connection.prepareStatement("SELECT A.cd_pessoa AS cd_fornecedor, A.nm_pessoa AS nm_fornecedor " +
																"FROM grl_pessoa A, grl_pessoa_empresa B " +
																"WHERE A.cd_pessoa  = B.cd_pessoa " +
																"  AND B.cd_vinculo = " +cdVinculo+
																" ORDER BY nm_pessoa ").executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getVendedores: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
		
	public static ResultSetMap getConcessionarios(int cdEmpresa) {
		return getConcessionarios(cdEmpresa, null);
	}

	public static ResultSetMap getConcessionarios(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			int cdVinculo = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CONCESSIONARIO_ESCOLAR", 0);
			if(cdVinculo <= 0)
				return new ResultSetMap();
			//
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT A.cd_pessoa, A.cd_pessoa AS CD_CONCESSIONARIO, A.nm_pessoa AS NM_CONCESSIONARIO " +
																"FROM grl_pessoa A, grl_pessoa_empresa B " +
																"WHERE A.cd_pessoa  = B.cd_pessoa " +
																"  AND B.cd_empresa = " +cdEmpresa+
																"  AND B.cd_vinculo = " +cdVinculo+
																" ORDER BY nm_pessoa ").executeQuery());
			while(rsm.next()){
				
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getMotoristas: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int updateGenerator() {
		return updateGenerator(null);
	}

	public static int updateGenerator(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT MAX(cd_pessoa) FROM GRL_PESSOA");
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				pstmt = connection.prepareStatement("SELECT cd_generator " +
						"FROM grl_generator " +
						"WHERE nm_generator = ?");
				pstmt.setString(1, "GRL_PESSOA");
				ResultSet rsTemp = pstmt.executeQuery();
				//
				if (!rsTemp.next()) {
					pstmt = connection.prepareStatement("INSERT INTO GRL_GENERATOR (nm_generator, cd_generator) " +
							"VALUES(?, ?)");
					pstmt.setString(1, "GRL_PESSOA");
					pstmt.setInt(2, rs.getInt(1));
					pstmt.execute();
				}
				else {
					pstmt.close();
					pstmt = connection.prepareStatement("UPDATE grl_generator SET cd_generator = ? WHERE nm_generator = ?");
					pstmt.setInt(1, rs.getInt(1));
					pstmt.setString(2, "GRL_PESSOA");
					pstmt.execute();
				}
				return rs.getInt(1);
			}
			else
				return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap loadEndereco(int cdPessoa, int cdEndereco) {
		return loadEndereco(cdPessoa, cdEndereco, null);
	}

	public static ResultSetMap loadEndereco(int cdPessoa, int cdEndereco, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			int tpEnderecamento = ParametroServices.getValorOfParametroAsInteger("TP_ENDERECAMENTO", PessoaEnderecoServices.TP_DIGITAVEL, 0);
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_tipo_endereco, C.nm_tipo_logradouro, C.sg_tipo_logradouro, " +
					                                              "       G.nm_tipo_logradouro AS nm_tipo_logradouro_enderecamento, " +
																  "       G.sg_tipo_logradouro AS sg_tipo_logradouro_enderecamento, " +
																  "       D.nm_logradouro AS nm_logradouro_enderecamento, D.id_logradouro, " +
																  "       E.nm_cidade, F.nm_bairro AS nm_bairro_enderecamento, F.id_bairro," +
																  "       H.nm_estado, H.sg_estado, " + tpEnderecamento + " as TP_ENDERECAMENTO " +
																  "FROM grl_pessoa_endereco A " +
																  "LEFT OUTER JOIN grl_tipo_endereco B ON (A.cd_tipo_endereco = B.cd_tipo_endereco) " +
																  "LEFT OUTER JOIN grl_tipo_logradouro C ON (A.cd_tipo_logradouro = C.cd_tipo_logradouro) " +
																  "LEFT OUTER JOIN grl_logradouro D ON (A.cd_logradouro = D.cd_logradouro) "+
																  "LEFT OUTER JOIN grl_tipo_logradouro G ON (D.cd_tipo_logradouro = G.cd_tipo_logradouro) " +
																  "LEFT OUTER JOIN grl_cidade E ON (A.cd_cidade = E.cd_cidade) " +
																  "LEFT OUTER JOIN grl_bairro F ON (A.cd_bairro = F.cd_bairro) " +
																  "LEFT OUTER JOIN grl_estado H ON (E.cd_estado = H.cd_estado) " +
																  "WHERE A.cd_pessoa = ? " +
																  "  AND A.cd_endereco = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdEndereco);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.loadEndereco: " +  e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int hasContaReceberVencidas(int cdPessoa) {
		return hasContaReceberVencidas(cdPessoa, null);
	}

	public static int hasContaReceberVencidas(int cdPessoa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM adm_conta_receber " +
											"	WHERE cd_pessoa = ? " +
											"	  AND dt_vencimento < ? " +
											"	  AND st_conta = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			pstmt.setInt(3, ContaReceberServices.ST_EM_ABERTO);
			ResultSet rs = pstmt.executeQuery();
			return (rs.next())?1:0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.hasContaReceberVencidas: " +  e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getContaReceberVencidas(int cdPessoa) {
		return getContaReceberVencidas(cdPessoa, null);
	}

	public static ResultSetMap getContaReceberVencidas(int cdPessoa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM adm_conta_receber " +
					"	WHERE cd_pessoa = ? " +
					"	  AND dt_vencimento < ? " +
					"	  AND st_conta = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			pstmt.setInt(3, ContaReceberServices.ST_EM_ABERTO);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getContaReceberVencidas: " +  e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllContaReceber(ArrayList<sol.dao.ItemComparator> criterios) {
		return getAllContaReceber(criterios, null);
	}

	public static ResultSetMap getAllContaReceber(ArrayList<sol.dao.ItemComparator> criterios, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			return ContaReceberServices.find(criterios, connection);
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

	public static HashMap<String,Object> getAtributos(int cdPessoa, int cdEmpresa, int cdVinculo) {
		return getAtributos(cdPessoa, cdEmpresa, cdVinculo, null);
	}

	public static HashMap<String,Object> getAtributos(int cdPessoa, int cdEmpresa, int cdVinculo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			com.tivic.manager.grl.Vinculo vinculo = cdVinculo > 0 ? com.tivic.manager.grl.VinculoDAO.get(cdVinculo, connect) : null;
			int cdFormulario = vinculo == null ? 0 : vinculo.getCdFormulario();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.tp_dado, C.nm_pessoa " +
					"FROM grl_formulario_atributo_valor A " +
					"JOIN grl_formulario_atributo B ON (A.cd_formulario_atributo = B.cd_formulario_atributo) " +
					"LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa_valor = C.cd_pessoa) " +
					"WHERE A.cd_pessoa = ? " +
					(cdFormulario > 0 ? "  AND A.cd_formulario_atributo IN (SELECT cd_formulario_atributo " +
					"									FROM grl_formulario_atributo " +
					"									WHERE cd_formulario = ?)" : ""));
			pstmt.setInt(1, cdPessoa);
			if (cdFormulario > 0)
				pstmt.setInt(2, cdFormulario);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			HashMap<String,Object> atributos = new HashMap<String,Object>();
			while (rsm != null && rsm.next()) {
				int cdFormularioAtributo = rsm.getInt("cd_formulario_atributo");
				int tpDado = rsm.getInt("tp_dado");
				Object atributoValor = tpDado == FormularioAtributoServices.TP_OPCOES ? (Object)new Integer(rsm.getInt("cd_opcao")) :
									   tpDado == FormularioAtributoServices.TP_PESSOA ? (Object)new Integer(rsm.getInt("cd_pessoa_valor")) : (Object)rsm.getString("txt_atributo_valor");
				atributos.put("ATRIBUTO_" + cdFormularioAtributo, atributoValor);
				if (tpDado == FormularioAtributoServices.TP_PESSOA)
					atributos.put("ATRIBUTO_PESSOA_VALOR_" + cdFormularioAtributo, rsm.getString("nm_pessoa"));
			}
			return atributos;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getAtributos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result substituirPessoa(int cdPessoaOld, int cdPessoa) {
		return substituirPessoa(cdPessoaOld, cdPessoa, null);
	}

	public static Result substituirPessoa(int cdPessoaOld, int cdPessoa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (cdPessoaOld<=0)
				return new Result(-1, "Registro a ser substituído não especificado.");

			if (cdPessoa<=0)
				return new Result(-1, "Registro a ser usado na substituição não especificado.");

			if (cdPessoa == cdPessoaOld)
				return new Result(-1, "Registro de origem e destino são idênticos.");

			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			// Acadêmico
			connection.prepareStatement("UPDATE acd_aluno SET cd_responsavel = "+cdPessoa+" WHERE cd_responsavel = "+cdPessoaOld).execute();
			// Administrativo / Financeiro
			connection.prepareStatement("UPDATE adm_avalista_contrato SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_conta_financeira SET cd_responsavel = "+cdPessoa+" WHERE cd_responsavel = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_conta_receber SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_conta_receber_evento SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_cotacao_pedido_item SET cd_fornecedor = "+cdPessoa+" WHERE cd_fornecedor = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_fornecedor_fabricante SET cd_fornecedor = "+cdPessoa+" WHERE cd_fornecedor = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_historico_classificacao SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_ordem_compra SET cd_fornecedor = "+cdPessoa+" WHERE cd_fornecedor = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_pedido_compra_fornecedor SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_pedido_venda SET cd_cliente = "+cdPessoa+" WHERE cd_cliente = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_plano_pagto_produto_fornec SET cd_fornecedor = "+cdPessoa+" WHERE cd_fornecedor = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_produto_fornecedor SET cd_fornecedor = "+cdPessoa+" WHERE cd_fornecedor = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_produto_fornecedor SET cd_representante = "+cdPessoa+" WHERE cd_representante = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_tabela_preco_regra SET cd_fornecedor = "+cdPessoa+" WHERE cd_fornecedor = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_conta_pagar SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_contrato SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_contrato SET cd_agente = "+cdPessoa+" WHERE cd_agente = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE adm_contrato_dependente SET cd_agente = "+cdPessoa+" WHERE cd_agente = "+cdPessoaOld).execute();
			// Agenda
			connection.prepareStatement("UPDATE agd_grupo_pessoa SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			// Estoque
			connection.prepareStatement("UPDATE alm_acerto_consignacao_entrada SET cd_fornecedor = "+cdPessoa+" WHERE cd_fornecedor = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE alm_acerto_consignacao_saida SET cd_cliente = "+cdPessoa+" WHERE cd_cliente = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE alm_documento_entrada SET cd_fornecedor = "+cdPessoa+" WHERE cd_fornecedor = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE alm_documento_entrada SET cd_transportadora = "+cdPessoa+" WHERE cd_transportadora = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE alm_documento_saida SET cd_cliente = "+cdPessoa+" WHERE cd_cliente = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE alm_documento_saida SET cd_transportadora = "+cdPessoa+" WHERE cd_transportadora = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE alm_documento_saida SET cd_vendedor = "+cdPessoa+" WHERE cd_vendedor = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE alm_local_armazenamento SET cd_responsavel = "+cdPessoa+" WHERE cd_responsavel = "+cdPessoaOld).execute();
			// Auditoria
			connection.prepareStatement("UPDATE aud_checklist_resultado SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			// Biblioteca
			connection.prepareStatement("UPDATE blb_exemplar_ocorrencia SET cd_operador = "+cdPessoa+" WHERE cd_operador = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE blb_exemplar_ocorrencia SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE blb_usuario_penalidade  SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			// Bem Patrimonial
			connection.prepareStatement("UPDATE bpm_bem_manutencao SET cd_fornecedor = "+cdPessoa+" WHERE cd_fornecedor = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE bpm_bem_manutencao SET cd_responsavel = "+cdPessoa+" WHERE cd_responsavel = "+cdPessoaOld).execute();
			// CMS
			connection.prepareStatement("UPDATE cms_conteudo SET cd_autor = "+cdPessoa+" WHERE cd_autor = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE cms_usuario SET cd_usuario = "+cdPessoa+" WHERE cd_usuario = "+cdPessoaOld).execute();
			// CRM
			connection.prepareStatement("UPDATE crm_atendimento SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE crm_mailing_destino SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			// Contabilidade
			connection.prepareStatement("UPDATE ctb_parametro SET cd_cliente = "+cdPessoa+" WHERE cd_cliente = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE ctb_parametro SET cd_fornecedor = "+cdPessoa+" WHERE cd_fornecedor = "+cdPessoaOld).execute();
			// E-Commerce
			connection.prepareStatement("UPDATE ecm_produto_empresa_comentario SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			// Frota
			connection.prepareStatement("UPDATE fta_abastecimento SET cd_fornecedor = "+cdPessoa+" WHERE cd_fornecedor = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE fta_abastecimento SET cd_responsavel = "+cdPessoa+" WHERE cd_responsavel = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE fta_autorizacao_saida SET cd_responsavel = "+cdPessoa+" WHERE cd_responsavel = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE fta_frete SET cd_cliente = "+cdPessoa+" WHERE cd_cliente = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE fta_motorista_veiculo SET cd_motorista = "+cdPessoa+" WHERE cd_motorista = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE fta_multa SET cd_responsavel = "+cdPessoa+" WHERE cd_responsavel = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE fta_veiculo SET cd_proprietario = "+cdPessoa+" WHERE cd_proprietario = "+cdPessoaOld).execute();
			// Geral
			connection.prepareStatement("UPDATE grl_formulario_atributo_valor SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE grl_formulario_atributo_valor SET cd_pessoa_valor = "+cdPessoa+" WHERE cd_pessoa_valor = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE grl_grupo_pessoa SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE grl_ocorrencia SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE grl_parametro SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE grl_parametro_opcao SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE grl_parametro_valor SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE grl_pessoa SET cd_pessoa_superior = "+cdPessoa+" WHERE cd_pessoa_superior = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE grl_pessoa_arquivo SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE grl_pessoa_conta_bancaria SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE grl_pessoa_orgao_credito SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE grl_pessoa_orgao_credito SET cd_orgao_credito = "+cdPessoa+" WHERE cd_orgao_credito = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE grl_pessoa_referencia SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE grl_produto_servico SET cd_fabricante = "+cdPessoa+" WHERE cd_fabricante = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE grl_setor SET cd_responsavel = "+cdPessoa+" WHERE cd_responsavel = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE grl_socio SET cd_socio = "+cdPessoa+" WHERE cd_socio = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE grl_tipo_documento_pessoa SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			// Micro-Crédito
			connection.prepareStatement("UPDATE mcr_credito_solicitado SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE mcr_grupo_solidario_pessoa SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			// SRH
			connection.prepareStatement("UPDATE srh_dados_funcionais SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			// Jurídico
			connection.prepareStatement("UPDATE prc_atendimento SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE prc_orgao_judicial_pessoa SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE prc_outra_parte SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE prc_parte_cliente SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE prc_processo SET cd_advogado = "+cdPessoa+" WHERE cd_advogado = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE prc_processo SET cd_advogado_contrario = "+cdPessoa+" WHERE cd_advogado_contrario = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE prc_processo_advogado SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE prc_testemunha SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			// Pesquisa
			connection.prepareStatement("UPDATE psq_grupo_questionario SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE psq_pessoa_resposta SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			// Protocolo
			connection.prepareStatement("UPDATE ptc_documento_pessoa SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			// SICOE
			connection.prepareStatement("UPDATE sce_contrato SET cd_contratante = "+cdPessoa+" WHERE cd_contratante = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE sce_contrato SET cd_subagente = "+cdPessoa+" WHERE cd_subagente = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE sce_contrato_comissao SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			connection.prepareStatement("UPDATE sce_produto SET cd_instituicao_financeira = "+cdPessoa+" WHERE cd_instituicao_financeira = "+cdPessoaOld).execute();
			// Segurança
			connection.prepareStatement("UPDATE seg_usuario SET cd_pessoa = "+cdPessoa+" WHERE cd_pessoa = "+cdPessoaOld).execute();
			// Excluindo pessoa
			Pessoa pessoa = PessoaDAO.get(cdPessoaOld, connection);
			if (PessoaServices.delete(cdPessoaOld, pessoa.getGnPessoa(), connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao excluir registro subtituído.");
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1, "");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erros reportados ao substituir registro. Anote a mensagem de erro e entre em contato com o suporte técnico.\n" + e.getMessage());
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	/**
	 * @category SICOE
	 */
	public static ResultSetMap findWithVinculoAndConta(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.nm_cidade AS nm_cidade_endereco, B.sg_estado AS sg_estado_endereco, "+
						   "       C.nm_cidade AS nm_cidade_naturalidade, C.sg_estado AS nm_estado_naturalidade, "+
						   "       D.*, E.nm_empresa, F.nm_vinculo, G.nr_conta, G.nr_dv, G.nr_agencia," +
						   "       G.tp_operacao, G.nr_cpf_cnpj AS nr_cpf_cnpj_titular, G.nm_titular," +
						   "       H.nr_banco, H.nm_banco "+
		                   "FROM GRL_PESSOA A "+
		                   "LEFT OUTER JOIN grl_cidade B ON (A.CD_CIDADE_ENDERECO = B.CD_CIDADE)"+
		                   "LEFT OUTER JOIN grl_cidade C ON (A.CD_CIDADE_NATURALIDADE = C.CD_CIDADE)"+
		                   "     INNER JOIN grl_pessoa_empresa D ON (A.CD_PESSOA = D.CD_PESSOA) "+
		                   "JOIN grl_empresa E ON (D.cd_empresa = E.cd_empresa) "+
		                   "JOIN grl_vinculo F ON (D.cd_vinculo = F.cd_vinculo) "+
		                   "LEFT OUTER JOIN grl_pessoa_conta_bancaria G ON (A.cd_pessoa = G.cd_pessoa) " +
		                   "LEFT OUTER JOIN grl_banco                 H ON (G.cd_banco  = H.cd_banco) ",
		                   "ORDER BY A.nm_pessoa", criterios, Conexao.conectar(), true);
	}

	/**
	 * @category SICOE
	 */
	public static ResultSetMap getColaboradorBloqueado() {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
						   "SELECT A.cd_pessoa, A.nm_pessoa, D.*, E.nm_empresa, F.nm_vinculo "+
		                   "FROM grl_pessoa_empresa D, grl_pessoa A, grl_empresa E, grl_vinculo F "+
		                   "WHERE A.cd_pessoa   = D.cd_pessoa "+
						   "  AND D.st_vinculo  = 2 "+
						   "  AND D.cd_vinculo IN (2,3) "+
						   "  AND D.cd_empresa  = E.cd_empresa "+
						   "  AND D.cd_vinculo  = F.cd_vinculo ");
		   return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getColaboradorBloqueado: " + sqlExpt);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/**
	 * @category SICOE
	 */
	public static ResultSetMap findColaborador(ArrayList<ItemComparator> criterios) {
		int cdEmpresa = 0;
		for(int i=0; i<criterios.size(); i++)	{
			if(criterios.get(i).getColumn().toUpperCase().equals("CD_EMPRESA"))	{
				cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				break;
			}
		}
		return Search.find("SELECT A.*, B.nm_cidade AS nm_cidade_endereco, B.sg_estado AS sg_estado_endereco, "+
						   "       C.nm_cidade AS nm_cidade_naturalidade, C.sg_estado AS nm_estado_naturalidade, "+
						   "       D.nm_pessoa AS nm_superior  "+
		                   "FROM GRL_PESSOA A "+
		                   "LEFT OUTER JOIN GRL_CIDADE B ON (A.CD_CIDADE_ENDERECO = B.CD_CIDADE)"+
		                   "LEFT OUTER JOIN GRL_CIDADE C ON (A.CD_CIDADE_NATURALIDADE = C.CD_CIDADE)"+
		                   "LEFT OUTER JOIN GRL_PESSOA D ON (A.CD_SUPERIOR = D.CD_PESSOA)"+
		                   "WHERE EXISTS (SELECT * FROM grl_pessoa_empresa D "+
		                   "              WHERE A.cd_pessoa  = D.cd_pessoa "+
		                   (cdEmpresa>0?" AND D.cd_empresa = "+cdEmpresa:"")+
		                   "                AND D.cd_vinculo <> "+Vinculo.PARCEIRO+")",
		                   "ORDER BY A.nm_pessoa", criterios, Conexao.conectar(), true);
	}

	/**
	 * @category SICOE
	 */
	public static ResultSetMap findCliente(ArrayList<ItemComparator> criterios) {
		Connection connect = Conexao.conectar();
		try	{
			int top  = 0;
			int skip = 0;
			int lgOrgao   = 0;
			int lgProduto = 0;
			int qtContratoInicial = 0;
			int qtContratoFinal   = 0;
			for(int i=0; i<criterios.size(); i++)	{
				if(criterios.get(i).getColumn().equalsIgnoreCase("top"))
					top = Integer.parseInt(criterios.get(i).getValue());
				else if(criterios.get(i).getColumn().equalsIgnoreCase("skip"))
					skip = Integer.parseInt(criterios.get(i).getValue());
				else if(criterios.get(i).getColumn().equalsIgnoreCase("lgProduto"))
					lgProduto = Integer.parseInt(criterios.get(i).getValue());
				else if(criterios.get(i).getColumn().equalsIgnoreCase("lgOrgao"))
					lgOrgao = Integer.parseInt(criterios.get(i).getValue());
				else if(criterios.get(i).getColumn().equalsIgnoreCase("qt_contrato_inicial"))
					qtContratoInicial = Integer.parseInt(criterios.get(i).getValue());
				else if(criterios.get(i).getColumn().equalsIgnoreCase("qt_contrato_final"))
					qtContratoFinal = Integer.parseInt(criterios.get(i).getValue());
			}
			String sql = "SELECT "+(top>0?"TOP "+top:"")+" A.*, B.nm_cidade AS nm_cidade_endereco, B.sg_estado AS sg_estado_endereco, "+
						 "       C.nm_cidade AS nm_cidade_naturalidade, C.sg_estado AS sg_estado_naturalidade "+
				         "FROM GRL_PESSOA A "+
				         "LEFT OUTER JOIN GRL_CIDADE B ON (A.CD_CIDADE_ENDERECO = B.CD_CIDADE)"+
				         "LEFT OUTER JOIN GRL_CIDADE C ON (A.CD_CIDADE_NATURALIDADE = C.CD_CIDADE)"+
				         "WHERE NOT EXISTS (SELECT * FROM grl_pessoa_empresa D "+
				         "                  WHERE A.CD_PESSOA = D.CD_PESSOA) ";
			String cla = "";
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for(int i=0; i<criterios.size(); i++)	{
				String field = criterios.get(i).getColumn();
				if(field.equalsIgnoreCase("cd_orgao"))	{
					cla += " AND EXISTS (SELECT * FROM grl_pessoa_orgao X " +
						   "             WHERE A.cd_pessoa = X.cd_pessoa " +
						   "               AND X.cd_orgao = "+criterios.get(i).getValue()+")";
				}
				else if(field.equalsIgnoreCase("top") || field.equalsIgnoreCase("skip") ||
						field.equalsIgnoreCase("lgOrgao") || field.equalsIgnoreCase("lgProduto") ||
						field.equalsIgnoreCase("qt_contrato_inicial") || field.equalsIgnoreCase("qt_contrato_final"))	{

				}
				else if(field.equalsIgnoreCase("dt_contrato") || field.equalsIgnoreCase("cd_instituicao_financeira") || field.equalsIgnoreCase("cd_empresa") ||
						field.equalsIgnoreCase("cd_parceiro") || field.equalsIgnoreCase("cd_produto") || field.equalsIgnoreCase("cd_plano"))	{
					cla += 	" AND EXISTS (SELECT * FROM sce_contrato X, sce_produto Y " +
					   		"             WHERE A.cd_pessoa  = X.cd_contratante " +
					   		"               AND X.cd_produto = Y.cd_produto ";
					if(field.equalsIgnoreCase("cd_empresa"))
						cla += " AND X.cd_empresa = "+criterios.get(i).getValue();
					if(field.equalsIgnoreCase("cd_instituicao_financeira"))
						cla += " AND Y.cd_instituicao_financeira = "+criterios.get(i).getValue();
					if(field.equalsIgnoreCase("cd_produto"))
						cla += " AND X.cd_produto = "+criterios.get(i).getValue();
					if(field.equalsIgnoreCase("cd_plano"))
						cla += " AND X.cd_plano = "+criterios.get(i).getValue();
					if(field.equalsIgnoreCase("dt_contrato"))	{
						GregorianCalendar data = com.tivic.manager.util.Util.stringToCalendar(criterios.get(i).getValue());
						cla += " AND X.dt_contrato "+criterios.get(i).getOperatorComparation()+" \'"+com.tivic.manager.util.Util.formatDateTime(data, "MM/dd/yyyy")+"\'";
					}
					cla += ")";
				}
				else
					crt.add(criterios.get(i));
			}
			if (qtContratoInicial > 0)
				cla += " AND (SELECT count(*) FROM sce_contrato X WHERE A.cd_pessoa = X.cd_contratante) >= "+qtContratoInicial;
			if (qtContratoFinal > 0)
				cla += " AND (SELECT count(*) FROM sce_contrato X WHERE A.cd_pessoa = X.cd_contratante) <= "+qtContratoFinal;
			sql += cla;
			if(skip>0)	{
				cla = cla.replaceAll("A\\.", "A2.");
				sql += " AND A.cd_pessoa NOT IN (SELECT TOP "+skip+" cd_pessoa FROM grl_pessoa A2 " +
					   "                         WHERE NOT EXISTS (SELECT * FROM grl_pessoa_empresa D2 "+
				       "                                           WHERE A2.CD_PESSOA = D2.CD_PESSOA) " +
				       cla+
				       "                         ORDER BY A2.nm_pessoa) ";
			}
			ResultSetMap rsm = Search.find(sql, "ORDER BY nm_pessoa", crt, Conexao.conectar(), true);
			if(lgProduto==1 || lgOrgao==1)	{
				PreparedStatement pstmtOrgao = connect.prepareStatement("SELECT * FROM grl_pessoa_orgao A, sce_orgao B " +
						                                                "WHERE A.cd_orgao = B.cd_orgao " +
						                                                "  AND A.cd_pessoa = ?");
				PreparedStatement pstmtProduto = connect.prepareStatement(
						"SELECT DISTINCT nm_produto, nm_pessoa AS nm_parceiro FROM sce_contrato A, sce_produto B, grl_pessoa C " +
                        "WHERE A.cd_produto = B.cd_produto " +
                        "  AND B.cd_instituicao_financeira = C.cd_pessoa " +
                        "  AND A.cd_contratante = ?");
				while(rsm.next())	{
					// Orgão
					pstmtOrgao.setInt(1, rsm.getInt("cd_pessoa"));
					ResultSet rs = pstmtOrgao.executeQuery();
					int i = 0;
					while(rs.next())	{
						rsm.setValueToField("NM_ORGAO"+i, rs.getString("NM_ORGAO"));
						rsm.setValueToField("NR_INSCRICAO"+i, rs.getString("NR_INSCRICAO"));
					}
					// Produto
					pstmtProduto.setInt(1, rsm.getInt("cd_pessoa"));
					rs = pstmtProduto.executeQuery();
					i = 0;
					while(rs.next())	{
						rsm.setValueToField("NM_PARCEIRO"+i, rs.getString("NM_PARCEIRO"));
						rsm.setValueToField("NM_PRODUTO"+i, rs.getString("NM_PRODUTO"));
					}
				}
			}
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
	}

	/**
	 * @category SICOE
	 */
	public static ResultSetMap findParceiro(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.nm_cidade AS nm_cidade_endereco, B.sg_estado AS sg_estado_endereco, "+
						   "       C.nm_cidade AS nm_cidade_naturalidade, C.sg_estado AS nm_estado_naturalidade "+
		                   "FROM grl_pessoa A "+
		                   "LEFT OUTER JOIN grl_cidade    B ON (A.cd_cidade_endereco = B.cd_cidade)"+
		                   "LEFT OUTER JOIN grl_cidade    C ON (A.cd_cidade_naturalidade = B.cd_cidade)"+
		                   "INNER JOIN grl_pessoa_empresa D ON (A.cd_pessoa = D.cd_pessoa) "+
		                   "WHERE D.cd_vinculo = "+Vinculo.PARCEIRO, "ORDER BY A.nm_pessoa", criterios, Conexao.conectar(), true);
	}
	/**
	 * @category SICOE
	 */
	public static ResultSetMap findPessoaOrgao(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.nm_orgao "+
		                   "FROM grl_pessoa_orgao A "+
		                   "JOIN sce_orgao B ON (A.cd_orgao = B.cd_orgao)", criterios, Conexao.conectar(), true);
	}
	/**
	 * @category SICOE
	 */
	public static int insertPessoaOrgao(int cdPessoa, int cdOrgao, String nrInscricao,
				int cdOrgaoOld, String nrInscricaoOld, Connection connect)
	{
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_orgao "+
		                                     "WHERE CD_PESSOA    = ? "+
		                                     "  AND CD_ORGAO     = ? "+
		                                     "  AND NR_INSCRICAO = ? ");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdOrgao);
			pstmt.setString(3, nrInscricao);
			if(!pstmt.executeQuery().next() && cdOrgaoOld<=0)	{
				pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_orgao "+
			                                     "(CD_PESSOA, CD_ORGAO, NR_INSCRICAO) "+
			                                     "VALUES (?,?,?)");
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, cdOrgao);
				pstmt.setString(3, nrInscricao);
				return pstmt.executeUpdate();
			}
			else if(cdOrgao>0 && cdOrgaoOld>0) {
				pstmt.close();
				pstmt = connect.prepareStatement("UPDATE grl_pessoa_orgao SET CD_ORGAO=?, NR_INSCRICAO=? "+
			                                     "WHERE CD_PESSOA =? AND CD_ORGAO = ? AND NR_INSCRICAO=?");
				pstmt.setInt(1, cdOrgao);
				pstmt.setString(2, nrInscricao);
				pstmt.setInt(3, cdPessoa);
				pstmt.setInt(4, cdOrgaoOld);
				pstmt.setString(5, nrInscricaoOld);
				return pstmt.executeUpdate();
			}
			else if(cdOrgaoOld>0)	{
				pstmt.close();
				pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_orgao "+
			                                     "WHERE CD_PESSOA =? AND CD_ORGAO = ? AND NR_INSCRICAO=?");
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, cdOrgaoOld);
				pstmt.setString(3, nrInscricaoOld);
				return pstmt.executeUpdate();
			}
			return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.insertPessoaOrgao: " + e);
			return -1;
		}
	}
	/**
	 * @category SICOE
	 */
	public static int insertContaBancaria(int cdPessoa, int cdContaBancaria, int cdBanco, String nrAgencia,
			String nrConta, String nrDv, int tpOperacao, int stConta, String nrCpfCnpj, String nmTitular) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			// Verificando duplicidade
			pstmt = connect.prepareStatement("SELECT * FROM GRL_PESSOA_CONTA_BANCARIA "+
			                                 "WHERE cd_banco   = ? "+
			                                 "  AND nr_agencia = ? "+
			                                 "  AND nr_conta   = ? "+
			                                 "  AND cd_conta_bancaria <> ?");
			pstmt.setInt(1, cdBanco);
			pstmt.setString(2, nrAgencia.trim());
			pstmt.setString(3, nrConta.trim());
			pstmt.setInt(4, cdContaBancaria);
			if(pstmt.executeQuery().next())
				return -10;
			if(cdContaBancaria==0)	{
				cdContaBancaria = Conexao.getSequenceCode("GRL_PESSOA_CONTA_BANCARIA");
				pstmt = connect.prepareStatement("INSERT INTO GRL_PESSOA_CONTA_BANCARIA "+
			                                     "(CD_PESSOA, CD_CONTA_BANCARIA, CD_BANCO, NR_AGENCIA, "+
			                                     " NR_CONTA, NR_DV, TP_OPERACAO, ST_CONTA, NR_CPF_CNPJ, NM_TITULAR)"+
			                                     "VALUES (?,?,?,?,?,?,?,?,?,?)");
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, cdContaBancaria);
				pstmt.setInt(3, cdBanco);
				pstmt.setString(4, nrAgencia.trim());
				pstmt.setString(5, nrConta.trim());
				pstmt.setString(6, nrDv.trim());
				pstmt.setInt(7, tpOperacao);
				pstmt.setInt(8, stConta);
				pstmt.setString(9, (nrCpfCnpj==null)?"":nrCpfCnpj.replaceAll("[\\.\\-/]", "").trim());
				pstmt.setString(10, nmTitular);
			}
			else	{
				pstmt.close();
				pstmt = connect.prepareStatement("UPDATE grl_pessoa_conta_bancaria " +
						                         "SET cd_banco=?, nr_agencia=?, nr_conta=?, nr_dv=?, tp_operacao=?, st_conta=?, nr_cpf_cnpj=?, "+
			                                     "    nm_titular=? " +
			                                     "WHERE CD_PESSOA         = ? " +
			                                     "  AND CD_CONTA_BANCARIA = ? ");
				pstmt.setInt(1, cdBanco);
				pstmt.setString(2, nrAgencia);
				pstmt.setString(3, nrConta);
				pstmt.setString(4, nrDv);
				pstmt.setInt(5, tpOperacao);
				pstmt.setInt(6, stConta);
				pstmt.setString(7, (nrCpfCnpj==null)?"":nrCpfCnpj.replaceAll("[\\.\\-/]", "").trim());
				pstmt.setString(8, nmTitular);
				pstmt.setInt(9, cdPessoa);
				pstmt.setInt(10, cdContaBancaria);
			}
			pstmt.executeUpdate();
			if(stConta==1 && cdContaBancaria>0)	{
				pstmt = connect.prepareStatement("UPDATE grl_pessoa_conta_bancaria SET st_conta = 0 "+
				                                 "WHERE cd_pessoa = ? AND cd_conta_bancaria <> ? ");
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, cdContaBancaria);
				pstmt.executeUpdate();
				// Atualizando contas
				pstmt = connect.prepareStatement("UPDATE adm_conta_pagar SET cd_conta_bancaria = ? "+
												 "WHERE cd_pessoa = ? AND cd_conta_bancaria IS NULL ");
				pstmt.setInt(1, cdContaBancaria);
				pstmt.setInt(2, cdPessoa);
				pstmt.executeUpdate();
			}
			return cdContaBancaria;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.insertContaBancaria: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	/**
	 * @category SICOE
	 */
	public static int deleteContaOfPessoa(int cdPessoa, int cdContaBancaria) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM GRL_PESSOA_CONTA_BANCARIA "+
		                                     "WHERE CD_PESSOA  = ? "+
		                                     "  AND CD_CONTA_BANCARIA = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdContaBancaria);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.deleteContaOfPessoa: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	/**
	 * @category SICOE
	 */
	public static ResultSetMap findContaBancaria(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.nm_banco, B.nr_banco "+
		                   "FROM GRL_PESSOA_CONTA_BANCARIA A, GRL_BANCO B "+
		                   "WHERE A.CD_BANCO = B.CD_BANCO", criterios, Conexao.conectar(), true);
	}

	/**
	 * @category SICOE
	 */
	public static ResultSetMap getAgente(int cdEmpresa, int cdOperacao) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.cd_pessoa, A.nm_pessoa, A.nr_cpf_cnpj "+
											 "FROM grl_pessoa_empresa B, grl_pessoa A, sce_vinculo_operacao C "+
											 "WHERE A.cd_pessoa   = B.cd_pessoa" +
											 "  AND B.st_contrato = 1 " +
											 "  AND B.cd_vinculo  = C.cd_vinculo "+
											 (cdOperacao>-10?" AND C.cd_operacao = "+cdOperacao:" AND C.cd_operacao > 0")+
											 (cdEmpresa>0?" AND B.cd_empresa = "+cdEmpresa:"")+
											 " ORDER BY nm_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getAgente: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	/**
	 * @category SICOE
	 */
	public static int getCdContaBancaria(int cdPessoa) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT cd_conta_bancaria "+
											 "FROM grl_pessoa_conta_bancaria "+
											 "WHERE cd_pessoa = ? "+
											 "  AND st_conta = 1");
			pstmt.setInt(1, cdPessoa);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getCdContaBancaria: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	/**
	 * @category SICOE
	 */
	public static int deletePessoa(int cdPessoa) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			connect.setAutoCommit(false);
			// Deletando Vínculos
			pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_empresa "+
											 "WHERE cd_pessoa = ? ");
			pstmt.setInt(1, cdPessoa);
			pstmt.executeUpdate();
			// Deletando Conta Bancária
			pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_conta_bancaria "+
											 "WHERE cd_pessoa = ? ");
			pstmt.setInt(1, cdPessoa);
			pstmt.executeUpdate();
			int ret = PessoaDAO.delete(cdPessoa, connect);
			connect.commit();
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.deletePessoa: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	private static Result validateCpfCnpj(Pessoa pessoa, Connection connect){
		try	{
			/* se informado CPF, verifica se o numero já está cadastrado */
			if(pessoa instanceof PessoaFisica)	{
				String nrCpf = ((PessoaFisica)pessoa).getNrCpf();
				if (nrCpf!=null && !nrCpf.trim().equals("")) {
					ResultSet rs = connect.prepareStatement("SELECT nm_pessoa FROM grl_pessoa A, grl_pessoa_fisica B " +
							                                "WHERE A.cd_pessoa = B.cd_pessoa AND nr_cpf = \'"+nrCpf.trim()+"\'"+
							                               	(pessoa.getCdPessoa()>0?"  AND A.cd_pessoa <> "+pessoa.getCdPessoa() : "")).executeQuery();
					if (rs.next())
						return new Result(ERR_NR_DOCUMENTO, "Já existe uma pessoa cadastrada com esse CPF: "+rs.getString("nm_pessoa"));
				}
			}
			/* se informado CNPJ, verifica se o numero já está cadastrado */
			if(pessoa instanceof PessoaJuridica)	{
				String nrCnpj = ((PessoaJuridica)pessoa).getNrCnpj();
				if (nrCnpj!=null && !nrCnpj.trim().equals("")) {
					ResultSet rs = connect.prepareStatement("SELECT nm_pessoa FROM grl_pessoa A, grl_pessoa_juridica B " +
							                                "WHERE A.cd_pessoa = B.cd_pessoa AND nr_cnpj = \'"+nrCnpj.trim()+"\'"+
							                                (pessoa.getCdPessoa()>0?"  AND A.cd_pessoa <> "+pessoa.getCdPessoa() : "")).executeQuery();
					if (rs.next())
						return new Result(ERR_NR_DOCUMENTO, "Já existe uma pessoa jurídica cadastrada com esse CNPJ: "+rs.getString("nm_pessoa"));
				}
			}
			return new Result(1);
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao verificar cpf ou cnpj", e);
		}
	}
	
	public static ResultSetMap findDesenvolvedor() {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		int cdVinculoDesenvolvedor = ParametroServices.getValorOfParametroAsInteger("CD_DESENVOLVEDOR", 0);
		//Util.printInFile("C:/log.log", String.valueOf(cdVinculoDesenvolvedor));
		try {
			pstmt = connect.prepareStatement("SELECT P.cd_pessoa, P.nm_pessoa, PJ.nm_razao_social, PJ.nr_cnpj, PJ.nr_inscricao_estadual, " +
											 "		 L.nm_tipo_logradouro, PL.nm_logradouro, PL.nr_endereco, PL.nm_complemento, P.nm_apelido, "+ 
											 "		 PL.nm_bairro, PL.nr_cep, C.nm_cidade, P.nr_telefone1, PJ.nr_inscricao_municipal "+
											 "FROM grl_pessoa P " +
											 "LEFT OUTER JOIN grl_pessoa_juridica   PJ ON (P.cd_pessoa 			 = PJ.cd_pessoa) "+
											 "LEFT OUTER JOIN grl_pessoa_contato    PC ON (PJ.cd_pessoa = PC.cd_pessoa) "+
											 "LEFT OUTER JOIN grl_pessoa_endereco   PL ON (P.cd_pessoa 			 = PL.cd_pessoa) "+
											 "LEFT OUTER JOIN grl_tipo_logradouro   L  ON (PL.cd_tipo_logradouro = L.cd_tipo_logradouro) "+
											 "JOIN grl_cidade          		   	    C  ON (PL.cd_cidade 		 = C.cd_cidade) "+											 
											 "WHERE P.cd_pessoa = " + cdVinculoDesenvolvedor);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			System.err.println("Erro! PessoaServices.findDesenvolvedor: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findUltimoFornecedor(int cdEmpresa, int cdVinculoFornecedor, int cdProdutoServico) {
		Connection connect = Conexao.conectar();
		try {
			PreparedStatement pstmt= connect.prepareStatement("SELECT A.cd_pessoa, A.nm_pessoa, DE.dt_emissao " +
															  "	FROM alm_documento_entrada_item DEI " +
															  "JOIN alm_documento_entrada DE ON (DEI.cd_documento_entrada = DE.cd_documento_entrada) " +
															  "JOIN grl_pessoa 		   A ON (DE.cd_fornecedor = A.cd_pessoa) " +
															  "JOIN grl_pessoa_empresa B ON (A.cd_pessoa = B.cd_pessoa) " +
															  "WHERE B.cd_empresa = ? AND B.cd_vinculo = ? AND cd_produto_servico = ? " +
															  "ORDER BY DE.dt_emissao DESC LIMIT 1");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdVinculoFornecedor);
			pstmt.setInt(3, cdProdutoServico);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.findUltimoFornecedor: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findPessoaFactoring(ArrayList<ItemComparator> criterios) {
		return findPessoaFactoring(criterios, null);
	}

	public static ResultSetMap findPessoaFactoring(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			int cdEmpresa = 0;
			int cdVinculo = 0;
			int stVinculo = 1;
			int stTitulo  = -1;
			GregorianCalendar dtInicial = null;
			GregorianCalendar dtFinal 	= null;
			String dsCampoData = null;
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++)
				if (criterios.get(i).getColumn().indexOf("cd_empresa") >= 0)
					cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().indexOf("cdVinculo") >= 0)
					cdVinculo = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().indexOf("st_vinculo") >= 0)
					stVinculo = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().indexOf("stTitulo") >= 0)
					stTitulo = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().indexOf("dtInicial") >= 0)
					dtInicial = Util.convStringToCalendar(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().indexOf("dtFinal") >= 0)
					dtFinal = Util.convStringToCalendar(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().indexOf("dsCampoData") >= 0)
					dsCampoData = criterios.get(i).getValue();
				else if (!criterios.get(i).getColumn().equalsIgnoreCase("lgEnderecoPrincipal"))
					crt.add(criterios.get(i));
			String sql =  "SELECT A.cd_pessoa, A.nm_pessoa, C.nr_cpf, B.nr_cnpj, E.vl_limite_factoring, D.nm_logradouro AS nm_endereco, D.nm_bairro, F.nm_cidade, G.sg_estado, D.nr_cep, A.nr_telefone1, A.nm_email " +
					      "FROM grl_pessoa A " +
						  "LEFT OUTER JOIN grl_pessoa_juridica  B ON (A.cd_pessoa = B.cd_pessoa) " +
						  "LEFT OUTER JOIN grl_pessoa_fisica    C ON (A.cd_pessoa = C.cd_pessoa) " +
						  "LEFT OUTER JOIN grl_pessoa_endereco  D ON (A.cd_pessoa = D.cd_pessoa" +
						  "												AND lg_principal = 1) " +
						  "LEFT OUTER JOIN adm_cliente		    E ON (A.cd_pessoa = E.cd_pessoa" +
						  "												AND E.cd_empresa = "+cdEmpresa+") " +
						  "LEFT OUTER JOIN grl_cidade    		F ON (D.cd_cidade = F.cd_cidade) " +
						  "LEFT OUTER JOIN grl_estado    		G ON (F.cd_estado = G.cd_estado) " +
						  "WHERE 1=1 "+
				      	  (cdEmpresa>0 || cdVinculo>0 ? " AND EXISTS (SELECT * FROM grl_pessoa_empresa D " +
				      	  		                        "             WHERE A.cd_pessoa  = D.cd_pessoa " +
				      	  		                        "               AND D.st_vinculo = " +stVinculo+
				      	  		                        (cdEmpresa>0?"  AND D.cd_empresa = "+cdEmpresa : "")+
				      	  		                        (cdVinculo>0?"  AND D.cd_vinculo = "+cdVinculo : "")+")" : "");
			
			ResultSetMap rsm = Search.find(sql, " ORDER BY A.nm_pessoa", crt, connect, false);
			PreparedStatement pstmt = connect.prepareStatement("SELECT  SUM(CR.vl_conta) AS vl_total_bruto, AVG(CR.vl_conta) AS vl_media_bruto, " +
																"		SUM(CR.vl_conta - CR.vl_abatimento + CR.vl_acrescimo) AS vl_total_liquido, " +
																"		AVG(CR.vl_conta - CR.vl_abatimento + CR.vl_acrescimo) AS vl_media_liquido, " +
																"		COUNT(*) AS QT_CHEQUES " +
												                "			FROM adm_conta_factoring A " +
												                "			JOIN adm_conta_receber CR ON (A.cd_conta_receber_antecipada = CR.cd_conta_receber) " +
												                "			JOIN adm_titulo_credito   I ON (CR.cd_conta_receber = I.cd_conta_receber) " +
												                "			JOIN adm_conta_pagar 	CP ON (A.cd_conta_pagar = CP.cd_conta_pagar) " +
												                "			JOIN grl_pessoa PS ON (CR.cd_pessoa = PS.cd_pessoa) " +
												                "			JOIN grl_pessoa PC ON (CP.cd_pessoa = PC.cd_pessoa) " +
												                "			WHERE "+ ((cdVinculo == ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0)) ? "PC.cd_pessoa" : "PS.cd_pessoa") +"= ? " +( stTitulo >=0 ? "AND I.st_titulo = " + stTitulo : "" )+ 
												                ((dsCampoData != null) ? " AND "+dsCampoData+" >= '" + Util.convCalendarStringSql(dtInicial) + "' AND "+dsCampoData+" <= '" + Util.convCalendarStringSql(dtFinal) + "'" : "" ));
			
			//Acrescentado pois não faz sentido buscar o limite utilizado apenas pelos filtros, ou o limite disponivel ficará inconsistente
			PreparedStatement pstmtLimite = connect.prepareStatement("SELECT  SUM(CR.vl_conta) AS vl_total_bruto " +
	                "			FROM adm_conta_factoring A " +
	                "			JOIN adm_conta_receber CR ON (A.cd_conta_receber_antecipada = CR.cd_conta_receber) " +
	                "			JOIN adm_titulo_credito   I ON (CR.cd_conta_receber = I.cd_conta_receber) " +
	                "			JOIN adm_conta_pagar 	CP ON (A.cd_conta_pagar = CP.cd_conta_pagar) " +
	                "			JOIN grl_pessoa PS ON (CR.cd_pessoa = PS.cd_pessoa) " +
	                "			JOIN grl_pessoa PC ON (CP.cd_pessoa = PC.cd_pessoa) " +
	                "			WHERE "+ ((cdVinculo == ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0)) ? "PC.cd_pessoa" : "PS.cd_pessoa") +"= ?" +( stTitulo >=0 ? "AND I.st_titulo = " + stTitulo : ""));
			
			
			ResultSetMap rsmResult = new ResultSetMap();
			while(rsm.next()){
				
				pstmt.setInt(1, rsm.getInt("cd_pessoa"));
				ResultSetMap rsmTotais = new ResultSetMap(pstmt.executeQuery());
				rsmTotais.beforeFirst();
				if(rsmTotais.next()){
					rsm.setValueToField("VL_TOTAL_BRUTO", rsmTotais.getFloat("vl_total_bruto"));
					rsm.setValueToField("VL_MEDIA_BRUTO", rsmTotais.getFloat("vl_media_bruto"));
					rsm.setValueToField("VL_TOTAL_LIQUIDO", rsmTotais.getFloat("vl_total_liquido"));
					rsm.setValueToField("VL_MEDIA_LIQUIDO", rsmTotais.getFloat("vl_media_liquido"));
					rsm.setValueToField("QT_CHEQUES", rsmTotais.getInt("QT_CHEQUES"));
				}
				
				pstmtLimite.setInt(1, rsm.getInt("cd_pessoa"));
				rsmTotais = new ResultSetMap(pstmtLimite.executeQuery());
				rsmTotais.beforeFirst();
				if(rsmTotais.next()){
					rsm.setValueToField("VL_LIMITE", rsm.getFloat("vl_limite_factoring"));
					//Usado para caso o cliente nao tenha limite e esteja sendo usado o limite geral para ela
					//por consequencia, caso o limite geral mude, ou a ela seja atribuido um novo limite
					//o limite disponivel pode acabar sendo negativo
					if(rsm.getFloat("VL_LIMITE") == 0){
						rsm.setValueToField("VL_LIMITE", ParametroServices.getValorOfParametroAsFloat("VL_LIMITE_FACTORING", 0, cdEmpresa));
					}
					rsm.setValueToField("VL_LIMITE_UTILIZADO", rsmTotais.getFloat("vl_total_bruto"));
					rsm.setValueToField("VL_LIMITE_DISPONIVEL", (rsm.getFloat("VL_LIMITE") - rsm.getFloat("VL_LIMITE_UTILIZADO")));
				}
				
//				if(rsm.getInt("QT_CHEQUES") > 0){
					rsmResult.addRegister(rsm.getRegister());
//				}
			}
			rsmResult.beforeFirst();
			return rsmResult;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllCombustivelLimiteByPessoa(int cdPessoa, int cdEmpresa) {
		return getAllCombustivelLimiteByPessoa(cdPessoa, cdEmpresa, null);
	}

	public static ResultSetMap getAllCombustivelLimiteByPessoa(int cdPessoa, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmtCombustivel = connection.prepareStatement("SELECT B.nm_produto_servico AS nm_combustivel, B.cd_produto_servico AS cd_combustivel " +
																			  "FROM alm_produto_grupo A " +
																			  "JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) " +
																			  "WHERE A.cd_grupo IN " + com.tivic.manager.alm.GrupoServices.getAllCombustivel(cdEmpresa, connection));

//			PreparedStatement pstmtLimite = connection.prepareStatement("SELECT A.vl_inicial AS vl_limite " +
//																		  "FROM grl_parametro_valor A " +
//																		  "JOIN grl_parametro B ON (A.cd_parametro = B.cd_parametro) " +
//																		  "WHERE A.cd_pessoa = " + cdPessoa + 
//																		  " AND B.nm_parametro = ?");
			
			PreparedStatement pstmtLimite = connection.prepareStatement(" SELECT vl_limite FROM adm_cliente_produto A " +
																	" WHERE A.cd_pessoa = " + cdPessoa +
																	" AND A.cd_empresa = "  + cdEmpresa +
																	" AND A.cd_produto_servico = ?");
			ResultSetMap rsmCombustivel = new ResultSetMap(pstmtCombustivel.executeQuery());
			ResultSetMap rsmLimite = new ResultSetMap();
			while(rsmCombustivel.next()){
//				pstmtLimite.setString(1, "vlLimite" + Util.retirarEspacos(rsmCombustivel.getString("NM_COMBUSTIVEL")).trim() + Util.fillNum(rsmCombustivel.getInt("CD_COMBUSTIVEL"), 10));
				pstmtLimite.setInt(1, rsmCombustivel.getInt("CD_COMBUSTIVEL"));
				rsmLimite = new ResultSetMap(pstmtLimite.executeQuery());
				if(rsmLimite.next())
					rsmCombustivel.setValueToField("VL_LIMITE", Float.parseFloat(rsmLimite.getString("VL_LIMITE").replace(",", ".")));
				else
					rsmCombustivel.setValueToField("VL_LIMITE", "0");
			}
			rsmCombustivel.beforeFirst();
			return rsmCombustivel;
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
	
	public static Result gerarRelatorioCliente(ArrayList<ItemComparator> criterios) {
		Connection connect = Conexao.conectar();
		try	{
			
			String sql = " SELECT A.*, A2.*, " +
						 " B.*, " +
			             " C.nm_cidade AS nm_cidade_endereco, C.id_cidade AS sg_estado_endereco, " +       
				         " D.vl_limite_credito " +
				         " FROM GRL_PESSOA                                A                                "+
				         " LEFT OUTER JOIN GRL_PESSOA_FISICA             A2 ON (A.CD_PESSOA = A2.CD_PESSOA) "+
				         " LEFT OUTER JOIN GRL_PESSOA_ENDERECO            B ON (A.CD_PESSOA = B.CD_PESSOA) "+
				         " LEFT OUTER JOIN GRL_CIDADE                     C ON (B.CD_CIDADE = C.CD_CIDADE) "+
				         " LEFT OUTER JOIN ADM_CLIENTE                    D ON (A.CD_PESSOA = D.CD_PESSOA) " +
				         " WHERE A.gn_pessoa = 1";
			
			ResultSetMap rsm = Search.find(sql, "ORDER BY nm_pessoa", criterios, connect, true);
			rsm.beforeFirst();
			
			
			while(rsm.next()){
				rsm.setValueToField("NR_TELEFONES",
						(rsm.getString("NR_TELEFONE1") != null ? rsm.getString("NR_TELEFONE1") + ", " : "") +
						(rsm.getString("NR_TELEFONE2") != null ? rsm.getString("NR_TELEFONE2") + ", " : "") +
						(rsm.getString("NR_CELULAR") != null ? rsm.getString("NR_CELULAR") : "")
				);
				rsm.setValueToField("DT_NASCIMENTO_DMY", rsm.getDateFormat("DT_NASCIMENTO", "dd/MM/yyyy"));
				rsm.setValueToField("NM_ENDERECO_COMPLETO",
						(rsm.getString("NM_LOGRADOURO") != null ? rsm.getString("NM_LOGRADOURO") : "") + ", " +
						(rsm.getString("NM_BAIRRO") != null ? rsm.getString("NM_BAIRRO") : "")
				);
				rsm.setValueToField("STR_LIMITE_CREDITO",
						(rsm.getInt("LG_LIMITE_CREDITO") == 0 ? "NÃO AUTORIZADO" : "AUTORIZADO")				
						);
				
			}
			
			HashMap<String, Object> param = new HashMap<String, Object>();
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static Pessoa getById(String idPessoa) {
		return getById(idPessoa, null);
	}
	
	public static Pessoa getById(String idPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa WHERE id_pessoa=?");
			pstmt.setString(1, idPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return PessoaDAO.get(rs.getInt("cd_pessoa"), connect);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}	
	
	public static Result gerarRelatorioClientesCsv(int cdEmpresa) {
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("CD_VINCULO", ParametroServices.getValorOfParametro("CD_VINCULO_CLIENTE"), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("CD_EMPRESA", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("findEnderecoPrincipal", "1", ItemComparator.EQUAL, Types.INTEGER));
		
		ResultSetMap rsm = find(criterios);
		String registro = "CNPJ_CPF;IE_RG;Razão Social;Nome Fantasia;Endereço_Logradouro;Endereço_Numero;Endereço_Bairro;Endereço_Complemento;Endereço_Referência;Endereço_Cidade;Endereço_Cidade_IBGE;Email;Telefone\r\n";
		
		while(rsm.next()){
			String novoRegistro = (rsm.getString("NR_CPF") != null && !rsm.getString("NR_CPF").equals("") ? rsm.getString("NR_CPF") : (rsm.getString("NR_CNPJ") != null && !rsm.getString("NR_CNPJ").trim().equals("") ? rsm.getString("NR_CNPJ") : "")) + ";" +
						(rsm.getString("NR_INSCRICAO_ESTADUAL") != null && !rsm.getString("NR_INSCRICAO_ESTADUAL").equals("") ? rsm.getString("NR_INSCRICAO_ESTADUAL") : (rsm.getString("NR_RG") != null && !rsm.getString("NR_RG").trim().equals("") ? rsm.getString("NR_RG") : "")) + ";" +
						(rsm.getString("NM_RAZAO_SOCIAL") != null && !rsm.getString("NM_RAZAO_SOCIAL").trim().equals("") ? rsm.getString("NM_RAZAO_SOCIAL") : "") + ";" +
					    (rsm.getString("NM_FANTASIA") != null && !rsm.getString("NM_FANTASIA").equals("") ? rsm.getString("NM_FANTASIA") : (rsm.getString("NM_PESSOA") != null && !rsm.getString("NM_PESSOA").trim().equals("") ? rsm.getString("NM_PESSOA") : "")) + ";" +
					    (rsm.getString("NM_LOGRADOURO") != null && !rsm.getString("NM_LOGRADOURO").trim().equals("") ? rsm.getString("NM_LOGRADOURO") : "") + ";" +
					    (rsm.getString("NR_ENDERECO") != null && !rsm.getString("NR_ENDERECO").trim().equals("") ? rsm.getString("NR_ENDERECO") : "") + ";" +
					    (rsm.getString("NM_BAIRRO") != null && !rsm.getString("NM_BAIRRO").trim().equals("") ? rsm.getString("NM_BAIRRO") : "") + ";" +
					    (rsm.getString("NM_COMPLEMENTO") != null && !rsm.getString("NM_COMPLEMENTO").trim().equals("") ? rsm.getString("NM_COMPLEMENTO") : "") + ";" +
					    (rsm.getString("NM_PONTO_REFERENCIA") != null && !rsm.getString("NM_PONTO_REFERENCIA").trim().equals("") ? rsm.getString("NM_PONTO_REFERENCIA") : "") + ";" +
					    (rsm.getString("NM_CIDADE") != null && !rsm.getString("NM_CIDADE").trim().equals("") ? rsm.getString("NM_CIDADE") : "") + ";" +
					    (rsm.getString("ID_IBGE") != null && !rsm.getString("ID_IBGE").trim().equals("") ? rsm.getString("ID_IBGE") : "") + ";" +
					    (rsm.getString("NM_EMAIL") != null && !rsm.getString("NM_EMAIL").trim().equals("") ? rsm.getString("NM_EMAIL") : "") + ";" +
					    (rsm.getString("NR_TELEFONE1") != null && rsm.getString("NR_TELEFONE1").trim().length() > 2 ? rsm.getString("NR_TELEFONE1") : rsm.getString("NR_TELEFONE2") != null && rsm.getString("NR_TELEFONE2").trim().length() > 2 ? rsm.getString("NR_TELEFONE2") : (rsm.getString("NR_CELULAR") != null && !rsm.getString("NR_CELULAR").trim().equals("") ? rsm.getString("NR_CELULAR") : "")) + "\r\n";
			
			System.out.println(novoRegistro);
			
			registro += novoRegistro;
		}
		System.out.println("Numero de registro " + rsm.size());
		
		try{
			//
			FileOutputStream gravar;
			File arquivo = new File("relatorio_clientes.csv"); 
			//
			File diretorio = new File("C:\\TIVIC");
			if(!diretorio.exists()){
				diretorio.mkdir();
			}
			//
			gravar = new FileOutputStream("C:\\TIVIC\\" + arquivo);
			gravar.write(registro.getBytes());
			gravar.close();
			
		}
		catch(Exception e){System.out.println("ERRO na gravacao: " + e); return new Result(-1, "Falha na gravação do arquivo");}
		
		return new Result(1, "Arquivo gravado com sucesso");
	}
	
	public static ResultSetMap findFornecedor(ArrayList<ItemComparator> criterios) {
		int cdEmpresa = 0;
		for(int i=0; i<criterios.size(); i++)	{
			if(criterios.get(i).getColumn().toUpperCase().equals("CD_EMPRESA"))	{
				cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				break;
			}
		}
		return Search.find("SELECT A.*, B.nm_cidade AS nm_cidade_endereco, B.sg_estado AS sg_estado_endereco, "+
						   "       C.nm_cidade AS nm_cidade_naturalidade, C.sg_estado AS nm_estado_naturalidade, "+
						   "       D.nm_pessoa AS nm_superior  "+
		                   "FROM GRL_PESSOA A "+
		                   "LEFT OUTER JOIN GRL_CIDADE B ON (A.CD_CIDADE_ENDERECO = B.CD_CIDADE)"+
		                   "LEFT OUTER JOIN GRL_CIDADE C ON (A.CD_CIDADE_NATURALIDADE = C.CD_CIDADE)"+
		                   "LEFT OUTER JOIN GRL_PESSOA D ON (A.CD_SUPERIOR = D.CD_PESSOA)"+
		                   "WHERE EXISTS (SELECT * FROM grl_pessoa_empresa D "+
		                   "              WHERE A.cd_pessoa  = D.cd_pessoa "+
		                   (cdEmpresa>0?" AND D.cd_empresa = "+cdEmpresa:"")+
		                   "                AND D.cd_vinculo = "+ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_FORNECEDOR", 0)+")",
		                   "ORDER BY A.nm_pessoa", criterios, Conexao.conectar(), true);
	}
	
	public static ResultSetMap findAllFornecedores(ArrayList<ItemComparator> criterios) {
		int cdEmpresa = 0;
		for(int i=0; i<criterios.size(); i++)	{
			if(criterios.get(i).getColumn().toUpperCase().equals("CD_EMPRESA"))	{
				cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				break;
			}
		}
		return Search.find("SELECT A.*, B.nm_cidade AS nm_cidade_endereco, BE.sg_estado AS sg_estado_endereco, "+
						   "       C.nm_cidade AS nm_cidade_naturalidade, CE.sg_estado AS nm_estado_naturalidade, "+
						   "       D.nm_pessoa AS nm_superior  "+
		                   "FROM GRL_PESSOA A "+
		                   "LEFT OUTER JOIN GRL_PESSOA_ENDERECO E ON (A.CD_PESSOA = E.CD_PESSOA AND E.LG_PRINCIPAL = 1)"+
		                   "LEFT OUTER JOIN GRL_CIDADE B ON (E.CD_CIDADE = B.CD_CIDADE)"+
		                   "LEFT OUTER JOIN GRL_ESTADO BE ON (B.CD_ESTADO = BE.CD_ESTADO)"+
		                   "LEFT OUTER JOIN GRL_PESSOA_JURIDICA J ON (A.CD_PESSOA = J.CD_PESSOA)"+
		                   "LEFT OUTER JOIN GRL_CIDADE C ON (J.CD_NATURALIDADE = C.CD_CIDADE)"+
		                   "LEFT OUTER JOIN GRL_ESTADO CE ON (C.CD_ESTADO = CE.CD_ESTADO)"+
		                   "LEFT OUTER JOIN GRL_PESSOA D ON (A.CD_PESSOA_SUPERIOR = D.CD_PESSOA)"+
		                   "WHERE EXISTS (SELECT * FROM grl_pessoa_empresa D "+
		                   "              WHERE A.cd_pessoa  = D.cd_pessoa "+
		                   (cdEmpresa>0?" AND D.cd_empresa = "+cdEmpresa:"")+
		                   "                AND D.cd_vinculo = "+ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_FORNECEDOR", 0)+")",
		                   "ORDER BY A.nm_pessoa", criterios, Conexao.conectar(), true);
	}
	
	public static Result saveDataNascimento(int cdPessoa, GregorianCalendar dtNascimento){
		return saveDataNascimento(cdPessoa, dtNascimento, null);
	}
	
	public static Result saveDataNascimento(int cdPessoa, GregorianCalendar dtNascimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(dtNascimento == null){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Data inválida");
			}
			
			System.out.println(dtNascimento);
			
			String sql = "UPDATE grl_pessoa_fisica SET dt_nascimento = ? WHERE cd_pessoa = " + cdPessoa;
			
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtNascimento.getTimeInMillis()));
			
			//'"+Util.convCalendarStringSql(dtNascimento)+"'
					
			int retorno = pstmt.executeUpdate();

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Data de Nascimento cadastrada com sucesso!", "NM_DT_NASCIMENTO", Util.formatDate(dtNascimento, "dd/MM/yyyy"));
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
	
	
	public static Result saveCartorio(PessoaJuridica pessoa, int cdCidade){
		return saveCartorio(pessoa, cdCidade, null);
	}
	
	public static Result saveCartorio(PessoaJuridica pessoa, int cdCidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(pessoa==null)
				return new Result(-1, "Erro ao salvar. Cartório é nulo");
			
			int retorno;
			if(pessoa.getCdPessoa()==0){
				retorno = PessoaJuridicaDAO.insert(pessoa, connect);
				pessoa.setCdPessoa(retorno);
				
				PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
				pessoaEmpresa.setCdPessoa(pessoa.getCdPessoa());
				pessoaEmpresa.setCdEmpresa(ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0));
				pessoaEmpresa.setCdVinculo(ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CARTORIO", 0));
				pessoaEmpresa.setStVinculo(PessoaEmpresaServices.ST_ATIVO);
				pessoaEmpresa.setDtVinculo(Util.getDataAtual());
				if(PessoaEmpresaDAO.insert(pessoaEmpresa, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir cartorio secretaria");
				}
			}
			else {
				retorno = PessoaDAO.update(pessoa, connect);
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_pessoa", "" + pessoa.getCdPessoa(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmEndereco = PessoaEnderecoDAO.find(criterios, connect);
			if(rsmEndereco.next()){
				PessoaEndereco pessoaEndereco = PessoaEnderecoDAO.get(rsmEndereco.getInt("cd_endereco"), rsmEndereco.getInt("cd_pessoa"), connect);
				pessoaEndereco.setCdCidade(cdCidade);
				pessoaEndereco.setLgPrincipal(1);
				if(PessoaEnderecoDAO.update(pessoaEndereco, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar endereço");
				}
			}
			else{
				PessoaEndereco pessoaEndereco = new PessoaEndereco();
				pessoaEndereco.setCdPessoa(pessoa.getCdPessoa());
				pessoaEndereco.setCdCidade(cdCidade);
				pessoaEndereco.setLgPrincipal(1);
				if(PessoaEnderecoDAO.insert(pessoaEndereco, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir endereço");
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PESSOAJURIDICA", pessoa);
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
	 * @TODO
	 * Uma gambiarra básica só pra terminar um recurso aqui e mostrar pra SMED, mas vou voltar aqui pra concertar
	 * 
	 * @author Anonimo
	 * @param nmPessoa
	 * @return ResultSetMap
	 * 
	 * @comment Estamos de olho.
	 * @comment "consertar" ;-)
	 */
	
	public static ResultSetMap findPessoaByNome(String nmPessoa) {
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			crt.add(new ItemComparator("B.NM_PESSOA", nmPessoa, ItemComparator.LIKE_ANY, Types.VARCHAR));
//			crt.add(new ItemComparator("B.GN_PESSOA", "1", ItemComparator.EQUAL, Types.VARCHAR));
//			crt.add(new ItemComparator("qtLimite", "50", ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsmPessoa = UsuarioServices.find(crt, connect);
			
			return rsmPessoa;
		} 
		catch(Exception e){
			e.printStackTrace();
			Conexao.rollback(connect);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static Result getEstatisticaCadastro(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return getEstatisticaCadastro(dtInicial, dtFinal, 0, null);
	}
	
	public static Result getEstatisticaCadastro(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int lgAlerta) {
		return getEstatisticaCadastro(dtInicial, dtFinal, lgAlerta, null);
	}
	
	public static Result getEstatisticaCadastro(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int lgAlerta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
							
			LogUtils.debug("getEstatisticaCadastro.dtInicial: "+Util.formatDate(dtInicial, "dd/MM/yyyy"));
			LogUtils.debug("getEstatisticaCadastro.dtFinal: "+Util.formatDate(dtFinal, "dd/MM/yyyy"));
			
			Result result = new Result(1);
			ResultSetMap rsm = new ResultSetMap();
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.cd_vinculo, A.nm_vinculo FROM grl_vinculo A"
					+ " WHERE A.cd_vinculo<>"+ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_COLABORADOR", 0)
					+ " AND EXISTS ("
					+ "		SELECT Z.* FROM grl_pessoa_empresa Z"
					+ "		WHERE Z.cd_vinculo = A.cd_vinculo"
					+ ")");
			
			ResultSetMap rsmVinculos = new ResultSetMap(pstmt.executeQuery());
			
			while(rsmVinculos.next()) { //pessoas por vinculo
				HashMap<String, Object> register = new HashMap<String, Object>();
				
				register.put("NM_INFO", rsmVinculos.getString("nm_vinculo"));
				register.put("NM_INFO_LABEL", rsmVinculos.getString("nm_vinculo"));
				
				pstmt = connect.prepareStatement(
						"SELECT COUNT(A.*) AS qt_pessoas FROM grl_pessoa A"
						+ " WHERE EXISTS ("
						+ "		SELECT Z.* FROM grl_pessoa_empresa Z"
						+ "		WHERE A.cd_pessoa = Z.cd_pessoa"
						+ "		AND Z.cd_vinculo=?"
						+ ")"
						+ " AND ((A.dt_cadastro>=? AND A.dt_cadastro<=?) OR A.dt_cadastro IS NULL)");
				pstmt.setInt(1, rsmVinculos.getInt("cd_vinculo"));
				pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
				pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
				
				ResultSet rs = pstmt.executeQuery();
				
				register.put("QT_PESSOAS", (rs.next()?rs.getInt("QT_PESSOAS"):0));
				
				rsm.addRegister(register);
			}
			
			//pessoas sem vínculo
			HashMap<String, Object> register = new HashMap<String, Object>();
			
			register.put("NM_INFO", "SEM VÍNCULO");
			register.put("NM_INFO_LABEL", "SEM VÍNCULO");
			pstmt = connect.prepareStatement(
					"SELECT COUNT(A.*) AS qt_pessoas FROM grl_pessoa A"
					+ " WHERE NOT EXISTS ("
					+ "		SELECT Z.* FROM grl_pessoa_empresa Z"
					+ "		WHERE A.cd_pessoa = Z.cd_pessoa"
					+ ")"
					+ " AND ((A.dt_cadastro>=? AND A.dt_cadastro<=?) OR A.dt_cadastro IS NULL)");
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			
			ResultSet rs = pstmt.executeQuery();
			
			register.put("QT_PESSOAS", (rs.next()?rs.getInt("QT_PESSOAS"):0));
			
			rsm.addRegister(register);
			
			
			if(isConnectionNull)
				connect.commit();
			
			result.addObject("RSM", rsm);
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getNomeByVinculo(int cdVinculo) {
		return getNomeByVinculo(cdVinculo, null);
	}
	
	public static ResultSetMap getNomeByVinculo(int cdVinculo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			PreparedStatement pstmt = connect.prepareStatement(
					  " SELECT nm_pessoa, nm_apelido "
					+ " FROM grl_pessoa A"
					+ " JOIN grl_pessoa_empresa B on(a.cd_pessoa=b.cd_pessoa)"
					+ " WHERE b.cd_vinculo=?"
					+ " AND a.st_cadastro=?"
					+ " ORDER BY nm_pessoa");
			pstmt.setInt(1, cdVinculo);
			pstmt.setInt(2, ST_ATIVO);
			
			return new ResultSetMap(pstmt.executeQuery());
		} 
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getRelatorios(int cdPessoa) {
		return getRelatorios(cdPessoa, null);
	}

	public static ResultSetMap getRelatorios(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			return FiltroRelatorioServices.getAllByPessoa(cdPessoa, connect);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getRelatorios: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findPrestador(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * "+
		                   "FROM grl_pessoa A "+
		                   "INNER JOIN grl_pessoa_empresa D ON (A.cd_pessoa = D.cd_pessoa) "+
		                   "WHERE D.cd_vinculo = "+ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CONCESSIONARIO_ESCOLAR", 0) + " AND D.cd_empresa = " + ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0), "ORDER BY A.nm_pessoa", criterios, Conexao.conectar(), true);
	}
	
	public static HashMap<String, Object> getSyncDataTransporte() {
		return getSyncDataTransporte(null);
	}

	public static HashMap<String, Object> getSyncDataTransporte(Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {

			String sql = " SELECT DISTINCT ON (A.cd_pessoa) A.* FROM grl_pessoa A " +
					     " JOIN mob_concessao C ON (A.cd_pessoa = C.cd_concessionario) " +
					     " WHERE C.st_concessao = ? ";

			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, 1);

			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("Pessoa", Util.resultSetToArrayList(pstmt.executeQuery()));
			
			return register;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}