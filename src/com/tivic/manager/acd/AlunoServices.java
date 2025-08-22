package com.tivic.manager.acd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.inject.Inject;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.EstadoServices;
import com.tivic.manager.grl.LogradouroServices;
import com.tivic.manager.grl.Ocorrencia;
import com.tivic.manager.grl.OcorrenciaDAO;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaAlergia;
import com.tivic.manager.grl.PessoaAlergiaDAO;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaDoenca;
import com.tivic.manager.grl.PessoaDoencaDAO;
import com.tivic.manager.grl.PessoaEmpresa;
import com.tivic.manager.grl.PessoaEmpresaDAO;
import com.tivic.manager.grl.PessoaEmpresaServices;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.PessoaFichaMedica;
import com.tivic.manager.grl.PessoaFichaMedicaDAO;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaFisicaServices;
import com.tivic.manager.grl.PessoaNecessidadeEspecial;
import com.tivic.manager.grl.PessoaNecessidadeEspecialDAO;
import com.tivic.manager.grl.PessoaNecessidadeEspecialServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.PessoaTipoDocumentacao;
import com.tivic.manager.grl.PessoaTipoDocumentacaoDAO;
import com.tivic.manager.grl.PessoaTipoDocumentacaoServices;
import com.tivic.manager.grl.ProdutoServicoServices;
import com.tivic.manager.grl.TipoDocumentacaoDAO;
import com.tivic.manager.grl.TipoDocumentacaoServices;
import com.tivic.manager.grl.TipoOcorrenciaServices;
import com.tivic.manager.log.LogServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.ComplianceManager;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.Validator;
import com.tivic.manager.util.ValidatorResult;

public class AlunoServices {
	
	//Grupos de ValidaÃ§Ã£o
	public static final int GRUPO_VALIDACAO_INSERT = 0;
	public static final int GRUPO_VALIDACAO_UPDATE = 1;
	public static final int GRUPO_VALIDACAO_EDUCACENSO = 2;
	//ValidaÃ§Ãµes
	public static final int VALIDATE_NOME = 0;
	public static final int VALIDATE_SEXO = 1;
	public static final int VALIDATE_DATA_NASCIMENTO = 2;
	public static final int VALIDATE_DATA_NASCIMENTO_MUITO_ANTIGA = 3;
	public static final int VALIDATE_NACIONALIDADE = 4;
	public static final int VALIDATE_NATURALIDADE = 5;
	public static final int VALIDATE_RACA = 6;
	public static final int VALIDATE_NOME_MAE = 7;
	public static final int VALIDATE_NOME_PAI_INVALIDO = 8;
	public static final int VALIDATE_NOME_PAI_CAIXA_VALIDACAO = 9;
	public static final int VALIDATE_NOME_PAI_NAO_DECLARADO = 10;
	public static final int VALIDATE_NOME_PAI_IGUAL_MAE = 11;
	public static final int VALIDATE_CAIXA_DE_SELECAO_NOME_PAI = 12;
	public static final int VALIDATE_CPF_VALIDO = 13;
	public static final int VALIDATE_CPF_JA_CADASTRADO = 14;
	public static final int VALIDATE_RG_QUANT_MINIMA = 15;
	public static final int VALIDATE_RG_JA_CADASTRADO = 16;
	
	public static final int VALIDATE_ENDERECO = 17;
	public static final int VALIDATE_ENDERECO_CEP = 18;
	public static final int VALIDATE_ENDERECO_LOGRADOURO = 19;
	public static final int VALIDATE_ENDERECO_NUMERO = 20;
	public static final int VALIDATE_ENDERECO_BAIRRO = 21;
	public static final int VALIDATE_ENDERECO_CIDADE = 22;
	public static final int VALIDATE_ENDERECO_ZONA = 23;
	
	public static final int VALIDATE_NOME_ALUNO_ESPACOS = 24;
	public static final int VALIDATE_NOME_ALUNO_ABREVIADO = 25;
	public static final int VALIDATE_NOME_ALUNO_CARACTERES_ESPECIAIS = 26;
	public static final int VALIDATE_NOME_ALUNO_SEM_SOBRENOME = 27;
	public static final int VALIDATE_NOME_MAE_ESPACOS = 28;
	public static final int VALIDATE_NOME_MAE_ABREVIADO = 29;
	public static final int VALIDATE_NOME_MAE_CARACTERES_ESPECIAIS = 30;
	public static final int VALIDATE_NOME_MAE_SEM_SOBRENOME = 31;
	public static final int VALIDATE_NOME_PAI_ESPACOS = 32;
	public static final int VALIDATE_NOME_PAI_ABREVIADO = 33;
	public static final int VALIDATE_NOME_PAI_CARACTERES_ESPECIAIS = 34;
	public static final int VALIDATE_NOME_PAI_SEM_SOBRENOME = 35;
	public static final int VALIDATE_RG_INFORMACOES = 36;
	public static final int VALIDATE_DATA_NASCIMENTO_POSTERIOR_ATUAL = 37;
	public static final int VALIDATE_IDADE_INCONSISTENTE_2_ANOS = 38;
	public static final int VALIDATE_IDADE_INCONSISTENTE_3_ANOS = 39;
	public static final int VALIDATE_IDADE_INCONSISTENTE_4_ANOS = 40;
	public static final int VALIDATE_IDADE_INCONSISTENTE_5_ANOS = 41;
	public static final int VALIDATE_IDADE_INCONSISTENTE_EJA = 42;
	public static final int VALIDATE_RG_DATA_POSTERIOR = 43;
	public static final int VALIDATE_RG_DATA_IGUAL_NASCIMENTO = 44;
	public static final int VALIDATE_PAIS_DE_ORIGEM_NACIONALIDADE = 45;
	public static final int VALIDATE_NACIONALIDADE_BRASILEIRA = 46;
	public static final int VALIDATE_EMAIL_INVALIDO = 47;
	public static final int VALIDATE_NUMERO_NIS = 48;
	public static final int VALIDATE_PERMISSAO_ALUNO_SEM_DOCUMENTO = 49;
	public static final int VALIDATE_CADASTRO_DUPLICADO = 50;
	public static final int VALIDATE_RG_DATA_ANTERIOR = 51;
	public static final int VALIDATE_RG_CONTEM_APENAS_ZEROS = 52;
	public static final int VALIDATE_FALTA_DOCUMENTO = 53;
	
	public static final int VALIDATE_NOME_CENSO_INVALIDO = 54;
	
	public static final int VALIDATE_ENDERECO_CEP_CIDADE = 55;
	
	
	public static Result save(Aluno aluno){
		return save(aluno, null/*endereco*/, 0/*cdEmpresa*/, 0/*cdVinculo*/, null/*formacao*/, 0/*cdAreaConhecimento*/, null/*connect*/);		
	}
	public static Result save(Aluno aluno, int cdUsuario, Connection connect){
		return save(aluno, null/*endereco*/, null/*pessoaFichaMedica*/, 0/*cdEmpresa*/, 0/*cdVinculo*/, null/*pessoaTipoDocumentacao*/, null/*formacao*/, 0/*cdAreaConhecimento*/, -1/*possuiDeficiencia*/, null/*matricula*/, cdUsuario, null, null, (Connection)null);
	}
	public static Result save(Aluno aluno, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, Formacao formacao, int cdAreaConhecimento){
		return save(aluno, endereco, null, cdEmpresa, cdVinculo, null, formacao, cdAreaConhecimento, -1, (Connection)null);		
	}
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacaoRegistro){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacaoRegistro, null, 0, -1, (Connection)null);		
	}
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacaoRegistro, int possuiDeficiencia){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacaoRegistro, null, 0, possuiDeficiencia, (Connection)null);		
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacaoRegistro, int possuiDeficiencia, int cdUsuario, String nrNis){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacaoRegistro, null, 0, possuiDeficiencia, null, cdUsuario, nrNis, null, null, (Connection)null);		
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacaoRegistro, int possuiDeficiencia, int cdUsuario, String nrNis, String nrPassaporte){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacaoRegistro, null, 0, possuiDeficiencia, null, cdUsuario, nrNis, nrPassaporte, null, (Connection)null);		
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacaoRegistro, int possuiDeficiencia, int cdUsuario, String nrNis, String nrPassaporte, String nrSus){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacaoRegistro, null, 0, possuiDeficiencia, null, cdUsuario, nrNis, nrPassaporte, nrSus, null, (Connection)null);		
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacaoRegistro, int possuiDeficiencia, Matricula matricula, int cdUsuario, String nrNis, String nrPassaporte, String nrSus, int cdTipoTransporte){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacaoRegistro, null, 0, possuiDeficiencia, matricula, cdUsuario, nrNis, nrPassaporte, nrSus, cdTipoTransporte, null, (Connection)null);		
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacaoRegistro, int possuiDeficiencia, Matricula matricula, int cdUsuario, String nrNis, String nrPassaporte, String nrSus){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacaoRegistro, null, 0, possuiDeficiencia, matricula, cdUsuario, nrNis, nrPassaporte, nrSus, null, (Connection)null);		
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacaoRegistro, int possuiDeficiencia, int cdUsuario, String nrNis, AuthData authData){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacaoRegistro, null, 0, possuiDeficiencia, null, cdUsuario, nrNis, null, authData, (Connection)null);		
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, Formacao formacao, int cdAreaConhecimento){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, null, formacao, cdAreaConhecimento, -1, (Connection)null);		
	}

	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, Formacao formacao, int cdAreaConhecimento, Matricula matricula){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, null, formacao, cdAreaConhecimento, -1, matricula, (Connection)null);		
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, Formacao formacao, int cdAreaConhecimento, int possuiDeficiencia, Matricula matricula){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, null, formacao, cdAreaConhecimento, possuiDeficiencia, matricula, null);		
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacao, Formacao formacao, int cdAreaConhecimento, int possuiDeficiencia, Matricula matricula){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacao, formacao, cdAreaConhecimento, possuiDeficiencia, matricula, null);		
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacao, Formacao formacao, int cdAreaConhecimento, int possuiDeficiencia, Matricula matricula, int cdUsuario){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacao, formacao, cdAreaConhecimento, possuiDeficiencia, matricula, cdUsuario, null, null, (Connection)null);		
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacao, Formacao formacao, int cdAreaConhecimento, int possuiDeficiencia, Matricula matricula, int cdUsuario, String nrNis, String nrPassaporte){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacao, formacao, cdAreaConhecimento, possuiDeficiencia, matricula, cdUsuario, nrNis, nrPassaporte, (Connection)null);		
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacao, Formacao formacao, int cdAreaConhecimento, int possuiDeficiencia, Matricula matricula, int cdUsuario, String nrNis, String nrPassaporte, AuthData authData){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacao, formacao, cdAreaConhecimento, possuiDeficiencia, matricula, cdUsuario, nrNis, nrPassaporte, authData, (Connection)null);		
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacao, Formacao formacao, int cdAreaConhecimento, int possuiDeficiencia, Matricula matricula, int cdUsuario, String nrNis, String nrPassaporte, String nrSus, AuthData authData){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacao, formacao, cdAreaConhecimento, possuiDeficiencia, matricula, cdUsuario, nrNis, nrPassaporte, nrSus, authData, (Connection)null);		
	}

	public static Result save(Aluno aluno, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, Formacao formacao, int cdAreaConhecimento, Connection connect){
		return save(aluno, endereco, null, cdEmpresa, cdVinculo, null, formacao, cdAreaConhecimento, -1, connect);
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacaoRegistro, Formacao formacao, int cdAreaConhecimento, int possuiDeficiencia, Connection connect){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacaoRegistro, formacao, cdAreaConhecimento, possuiDeficiencia, null, connect);
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacaoRegistro, Formacao formacao, int cdAreaConhecimento, int possuiDeficiencia, Matricula matricula, Connection connect){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacaoRegistro, formacao, cdAreaConhecimento, possuiDeficiencia, matricula, 0, null, null, connect);
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacaoRegistro, Formacao formacao, int cdAreaConhecimento, int possuiDeficiencia, Matricula matricula, int cdUsuario, String nrNis, String nrPassaporte, Connection connect){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacaoRegistro, formacao, cdAreaConhecimento, possuiDeficiencia, matricula, cdUsuario, nrNis, nrPassaporte, null, connect);
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacaoRegistro, Formacao formacao, int cdAreaConhecimento, int possuiDeficiencia, Matricula matricula, int cdUsuario, String nrNis, String nrPassaporte, AuthData authData, Connection connect){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacaoRegistro, formacao, cdAreaConhecimento, possuiDeficiencia, matricula, cdUsuario, nrNis, nrPassaporte, null, authData, connect);
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacaoRegistro, Formacao formacao, int cdAreaConhecimento, int possuiDeficiencia, Matricula matricula, int cdUsuario, String nrNis, String nrPassaporte, String nrSus, AuthData authData, Connection connect){
		return save(aluno, endereco, pessoaFichaMedica, cdEmpresa, cdVinculo, pessoaTipoDocumentacaoRegistro, formacao, cdAreaConhecimento, possuiDeficiencia, matricula, cdUsuario, nrNis, nrPassaporte, nrSus, -1, authData, connect);
	}
	
	public static Result save(Aluno aluno, PessoaEndereco endereco, PessoaFichaMedica pessoaFichaMedica, int cdEmpresa, int cdVinculo, PessoaTipoDocumentacao pessoaTipoDocumentacaoRegistro, Formacao formacao, int cdAreaConhecimento, int possuiDeficiencia, Matricula matricula, int cdUsuario, String nrNis, String nrPassaporte, String nrSus, int cdTipoTransporte, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(aluno==null)
				return new Result(-1, "Erro ao salvar. Aluno Ã© nulo");
			int retorno;
			ValidatorResult resultadoValidacao;
			
			//COMPLIANCE
			int tpAcao = ComplianceManager.TP_ACAO_ANY;
			
			//LOG
			String idAcao = "";
			Aluno oldValue = null;
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			
			boolean isInsert = false;
			
			//retira espaÃ§os extras no nome do aluno, nome da mÃ£e e nome do pai
			if(aluno.getNmPessoa() != null)
				aluno.setNmPessoa(aluno.getNmPessoa().trim().replaceAll("  ", " "));
			if(aluno.getNmMae() != null)
				aluno.setNmMae(aluno.getNmMae().trim().replaceAll("  ", " "));
			if(aluno.getNmPai() != null)
				aluno.setNmPai(aluno.getNmPai().trim().replaceAll("  ", " "));
	
			if(aluno.getNrRg() == null || aluno.getNrRg().trim().equals("")){
				aluno.setCdEstadoRg(0);
				aluno.setSgOrgaoRg(null);
				aluno.setDtEmissaoRg(null);
			}
			
			aluno.setLgCadastroProblema(0);
			
			//Atualiza o nome do censo para o nome do aluno, caso o nome para o censo esteja em branco
			if(aluno.getNmAlunoCenso() == null || aluno.getNmAlunoCenso().trim().equals("")){
				aluno.setNmAlunoCenso(aluno.getNmPessoa());
			}
			
			
			//CorreÃ§Ã£o de horÃ¡rio prevendo que horÃ¡rios de verÃ£o possam modificar a exibiÃ§Ã£o
			if(aluno.getDtNascimento() != null){
				GregorianCalendar dtNascimento = aluno.getDtNascimento();
				
				GregorianCalendar dtNascimentoNovo = new GregorianCalendar();
				dtNascimentoNovo.set(Calendar.DAY_OF_MONTH, dtNascimento.get(Calendar.DAY_OF_MONTH));
				dtNascimentoNovo.set(Calendar.MONTH, dtNascimento.get(Calendar.MONTH));
				dtNascimentoNovo.set(Calendar.YEAR, dtNascimento.get(Calendar.YEAR));
				dtNascimentoNovo.set(Calendar.HOUR_OF_DAY, 12);
				dtNascimentoNovo.set(Calendar.MINUTE, 0);
				dtNascimentoNovo.set(Calendar.SECOND, 0);
				dtNascimentoNovo.set(Calendar.MILLISECOND, 0);
				
				aluno.setDtNascimento(dtNascimentoNovo);
				
			}
			
			//CorreÃ§Ã£o de horÃ¡rio prevendo que horÃ¡rios de verÃ£o possam modificar a exibiÃ§Ã£o
			if(aluno.getDtEmissaoRg() != null){
				GregorianCalendar dtEmissaoRg = aluno.getDtEmissaoRg();

				GregorianCalendar dtEmissaoRgNovo = new GregorianCalendar();
				dtEmissaoRgNovo.set(Calendar.DAY_OF_MONTH, dtEmissaoRg.get(Calendar.DAY_OF_MONTH));
				dtEmissaoRgNovo.set(Calendar.MONTH, dtEmissaoRg.get(Calendar.MONTH));
				dtEmissaoRgNovo.set(Calendar.YEAR, dtEmissaoRg.get(Calendar.YEAR));
				dtEmissaoRgNovo.set(Calendar.HOUR_OF_DAY, 12);
				dtEmissaoRgNovo.set(Calendar.MINUTE, 0);
				dtEmissaoRgNovo.set(Calendar.SECOND, 0);
				dtEmissaoRgNovo.set(Calendar.MILLISECOND, 0);
				
				aluno.setDtEmissaoRg(dtEmissaoRgNovo);
			}
			
			if(AlunoDAO.get(aluno.getCdAluno(), connect)==null){
				
				//Compliance
				tpAcao = ComplianceManager.TP_ACAO_INSERT;
				
				//Log
				if(authData!=null)
					idAcao = authData.getIdAcaoInsert();
								
				
				isInsert = true;
				retorno = AlunoDAO.insert(aluno, connect);
				aluno.setCdAluno(retorno);
				
				int cdTipoOcorrenciaCadastroAluno = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_ALUNO, connect).getCdTipoOcorrencia();
				OcorrenciaAluno ocorrencia = new OcorrenciaAluno(0, aluno.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " cadastrada", Util.getDataAtual(), cdTipoOcorrenciaCadastroAluno, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, aluno.getCdAluno(), Util.getDataAtual(), cdUsuario);
				OcorrenciaAlunoServices.save(ocorrencia, connect);
				
			}
			else {
				//Compliance e Log
				tpAcao = ComplianceManager.TP_ACAO_UPDATE;
				
				//Log
				if(authData!=null)
					idAcao = authData.getIdAcaoUpdate();
				oldValue = AlunoDAO.get(aluno.getCdAluno(), connect);
				
				retorno = AlunoDAO.update(aluno, connect);
				int cdTipoOcorrenciaCadastroAluno = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_ALUNO, connect).getCdTipoOcorrencia();
				OcorrenciaAluno ocorrencia = null;
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_aluno", "" + aluno.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + cdTipoOcorrenciaCadastroAluno, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmOcorrenciaAluno = OcorrenciaAlunoServices.find(criterios, connect);
				if(rsmOcorrenciaAluno.next()){
					ocorrencia = OcorrenciaAlunoDAO.get(rsmOcorrenciaAluno.getInt("cd_ocorrencia"), connect);
				}
				if(ocorrencia == null){
					ocorrencia = new OcorrenciaAluno(0, aluno.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " atualizado", Util.getDataAtual(), cdTipoOcorrenciaCadastroAluno, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, aluno.getCdAluno(), Util.getDataAtual(), cdUsuario);
					OcorrenciaAlunoServices.save(ocorrencia, connect);
				}
				else{
					ocorrencia.setDtUltimaModificacao(Util.getDataAtual());
					ocorrencia.setCdUsuarioModificador(cdUsuario);
					OcorrenciaAlunoServices.save(ocorrencia, connect);
				}
				
			}
			if(retorno < 0){
				if(retorno==-666) {
					//LOG
					LogServices.log(LogServices.ANY, idAcao, authData, aluno, oldValue, 
							"CHANGE_PROCESSO_EXCEPTION\n"
							+ "A mudanÃ§a de processo nÃ£o Ã© permitida.\n"
							+ "De:\n"+LogServices.formatValues(oldValue)+"\nPara:\n"+LogServices.formatValues(aluno));
					return new Result(retorno, "A mudanÃ§a de processo nÃ£o Ã© permitida.");
				}
				else 
					return new Result(-2, "Erro ao salvar aluno.");
			}
			
			//Verifica se o aluno possui RG, criando um registro na tabela de tipo documentaÃ§Ã£o
			if(aluno.getNrRg() == null || aluno.getNrRg().trim().equals("") && aluno.getLgFaltaDocumento() == 0){
				PessoaFisica pessoaFisica = PessoaFisicaDAO.get(aluno.getCdAluno(), connect);
				if(pessoaFisica != null){
					pessoaFisica.setDtEmissaoRg(null);
					pessoaFisica.setSgOrgaoRg(null);
					if(PessoaFisicaDAO.update(pessoaFisica, connect) <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar pessoa");
					}
				}
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", "" + aluno.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tipo_documentacao", "" + TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_RG).getCdTipoDocumentacao(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmTipoDocumentacao = PessoaTipoDocumentacaoDAO.find(criterios, connect);
				if(rsmTipoDocumentacao.next()){
					Result result = PessoaTipoDocumentacaoServices.remove(aluno.getCdAluno(), TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_RG).getCdTipoDocumentacao(), true, connect);
					if(result.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, result.getMessage());
					}
				}
			}
			else if(aluno.getLgFaltaDocumento() == 0){
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", "" + aluno.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tipo_documentacao", "" + TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_RG).getCdTipoDocumentacao(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmTipoDocumentacao = PessoaTipoDocumentacaoDAO.find(criterios, connect);
				if(rsmTipoDocumentacao.next()){
					PessoaTipoDocumentacao pessoaTipoDocumentacao = PessoaTipoDocumentacaoDAO.get(aluno.getCdAluno(), TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_RG).getCdTipoDocumentacao(), connect);
					pessoaTipoDocumentacao.setNrDocumento(aluno.getNrRg());
					if(aluno.getDtEmissaoRg() != null)
						pessoaTipoDocumentacao.setDtEmissao(aluno.getDtEmissaoRg());
					if(PessoaTipoDocumentacaoDAO.update(pessoaTipoDocumentacao, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar documentaÃ§Ã£o");
					}
				}
				else{
					PessoaTipoDocumentacao pessoaTipoDocumentacao = new PessoaTipoDocumentacao(aluno.getCdAluno(), TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_RG).getCdTipoDocumentacao(), aluno.getNrRg(), null, null, aluno.getDtEmissaoRg(), 0, 0, 0);
					if(PessoaTipoDocumentacaoDAO.insert(pessoaTipoDocumentacao, connect) <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao inserir documentaÃ§Ã£o");
					}
				}
			}
			
			if (retorno > 0) {
				
				/*
				 * FORMAÃ‡ÃƒO ACADÃŠMICA
				 */
				if (formacao!=null && cdAreaConhecimento != 0) {
					formacao.setCdPessoa(aluno.getCdPessoa()!=0?aluno.getCdPessoa():0);
					Result r = FormacaoServices.save(formacao, connect);
					if (r.getCode()>0) {
						formacao.setCdFormacao(r.getCode());
						
					}
					else {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-3, "Erro ao tentar salvar dados da formaÃ§Ã£o!");
					}
				}
				/*
				 * VÃ�NCULO
				 */
				if(cdVinculo<=0) {
					cdVinculo = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_ALUNO", 0, 0, connect);
				}		
				
				if (cdEmpresa > 0 && cdVinculo > 0) {
					Result r = PessoaEmpresaServices.save(new PessoaEmpresa(cdEmpresa, aluno.getCdAluno(), cdVinculo, new GregorianCalendar(), PessoaEmpresaServices.ST_ATIVO), connect);
					if (r.getCode()<=0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-4, "Erro ao tentar salvar dados do vÃ­nculo!");
					}
				}
				
				/*
				 * ENDEREÃ‡O
				 */
				if (endereco != null) {
					
					if (endereco.getCdEndereco() <= 0) {
						endereco.setCdPessoa(aluno.getCdAluno()!=0?aluno.getCdAluno():0);
						endereco.setLgCobranca(1);
						endereco.setLgPrincipal(1);
					}
					if (PessoaEnderecoServices.save(endereco, connect).getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-5, "Erro ao tentar salvar endereÃ§o!");
					}
				}
				
				/*
				 * Ficha MÃ©dica
				 */
				if (pessoaFichaMedica != null) {
					pessoaFichaMedica.setCdPessoa(aluno.getCdAluno());
					int r = 0;
					if(PessoaFichaMedicaDAO.get(aluno.getCdAluno(), connect) == null)
						r = PessoaFichaMedicaDAO.insert(pessoaFichaMedica, connect);
					else
						r = PessoaFichaMedicaDAO.update(pessoaFichaMedica, connect);
					if (r <=0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-4, "Erro ao tentar salvar ficha mÃ©dica!");
					}
				}
				else if(isInsert || PessoaFichaMedicaDAO.get(aluno.getCdAluno(), connect) == null){
					pessoaFichaMedica = new PessoaFichaMedica();
					pessoaFichaMedica.setCdPessoa(aluno.getCdAluno());
					int r = PessoaFichaMedicaDAO.insert(pessoaFichaMedica, connect);
					if (r <=0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-4, "Erro ao tentar salvar ficha mÃ©dica!");
					}
				}
				/*
				 * Registro
				 */
				if(pessoaTipoDocumentacaoRegistro == null){
					ResultSetMap rsmDocumentacao = PessoaServices.getAllDocumentosOfAluno(aluno.getCdAluno(), connect);
					while(rsmDocumentacao.next()){
						if(rsmDocumentacao.getInt("cd_tipo_documentacao") == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0)
						|| rsmDocumentacao.getInt("cd_tipo_documentacao") == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)){
							pessoaTipoDocumentacaoRegistro = PessoaTipoDocumentacaoDAO.get(aluno.getCdAluno(), rsmDocumentacao.getInt("cd_tipo_documentacao"), connect);
							break;
						}
					}
				}
				
				//Se o modelo do tipo de documentaÃ§Ã£o de certidÃ£o foi passado como 0, o sistema entende que o aluno nÃ£o possui certidÃ£o, e apaga todas as que tiverem sido registradas
				if(pessoaTipoDocumentacaoRegistro != null && pessoaTipoDocumentacaoRegistro.getTpModelo() == PessoaTipoDocumentacaoServices.TP_MODELO_NENHUM){
					pessoaTipoDocumentacaoRegistro.setCdPessoa(aluno.getCdAluno());
					if(pessoaTipoDocumentacaoRegistro.getCdTipoDocumentacao() == 0)
						pessoaTipoDocumentacaoRegistro.setCdTipoDocumentacao(TipoDocumentacaoDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0), connect).getCdTipoDocumentacao());
					
					int ret = connect.prepareStatement("DELETE FROM grl_pessoa_tipo_documentacao WHERE cd_pessoa = " + aluno.getCdAluno() + " AND cd_tipo_documentacao IN ("+ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)+")").executeUpdate();
					if(ret < 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao excluir certidÃ£o anterior do aluno");
					}
				}
				else if(pessoaTipoDocumentacaoRegistro != null && pessoaTipoDocumentacaoRegistro.getTpModelo() > PessoaTipoDocumentacaoServices.TP_MODELO_NENHUM && aluno.getLgFaltaDocumento() == 0){
					pessoaTipoDocumentacaoRegistro.setCdPessoa(aluno.getCdAluno());
					if(pessoaTipoDocumentacaoRegistro.getCdTipoDocumentacao() == 0)
						pessoaTipoDocumentacaoRegistro.setCdTipoDocumentacao(TipoDocumentacaoDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0), connect).getCdTipoDocumentacao());
					
					int ret = connect.prepareStatement("DELETE FROM grl_pessoa_tipo_documentacao WHERE cd_pessoa = " + aluno.getCdAluno() + " AND cd_tipo_documentacao IN ("+ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)+")").executeUpdate();
					if(ret < 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao excluir certidÃ£o anterior do aluno");
					}
					
					Result result = PessoaTipoDocumentacaoServices.save(pessoaTipoDocumentacaoRegistro, authData, connect);
					if(result.getCode() < 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return result;
					}
				}
				
				//Relaciona o NIS ao aluno
				if(nrNis != null && !nrNis.trim().equals("") && !nrNis.trim().equals("null")){
					int cdTipoDocumentacaoNis = TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_NIS, connect).getCdTipoDocumentacao();
					
					PessoaTipoDocumentacao pessoaTipoDocumentacaoNis = new PessoaTipoDocumentacao(aluno.getCdAluno(), cdTipoDocumentacaoNis, nrNis, null, null, null, 0, 0, 0);
					Result result = PessoaTipoDocumentacaoServices.save(pessoaTipoDocumentacaoNis, authData, connect);
					if(result.getCode() < 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return result;
					}
					
				}
				else{
					int cdTipoDocumentacaoNis = TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_NIS, connect).getCdTipoDocumentacao();
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_pessoa", "" + aluno.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_tipo_documentacao", "" + cdTipoDocumentacaoNis, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmPessoaTipoDocumentacao = PessoaTipoDocumentacaoDAO.find(criterios, connect);
					if(rsmPessoaTipoDocumentacao.next()){
						if(PessoaTipoDocumentacaoServices.remove(rsmPessoaTipoDocumentacao.getInt("cd_pessoa"), rsmPessoaTipoDocumentacao.getInt("cd_tipo_documentacao"), true, connect).getCode() < 0){
							if (isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao remover NIS do aluno");
						}
					}
					
				}
				
				//Relaciona o nÃºmero de passaporte ao aluno
				if(nrPassaporte != null && !nrPassaporte.trim().equals("")){
					int cdTipoDocumentacaoPassaporte = TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_PASSAPORTE, connect).getCdTipoDocumentacao();
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_pessoa", "" + aluno.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_tipo_documentacao", "" + cdTipoDocumentacaoPassaporte, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmPessoaTipoDocumentacao = PessoaTipoDocumentacaoDAO.find(criterios, connect);
					if(rsmPessoaTipoDocumentacao.next()){
						PessoaTipoDocumentacao pessoaTipoDocumentacaoPassaporte = PessoaTipoDocumentacaoDAO.get(rsmPessoaTipoDocumentacao.getInt("cd_pessoa"), rsmPessoaTipoDocumentacao.getInt("cd_tipo_documentacao"), connect);
						pessoaTipoDocumentacaoPassaporte.setNrDocumento(nrPassaporte);
						if(PessoaTipoDocumentacaoDAO.update(pessoaTipoDocumentacaoPassaporte, connect) <= 0){
							if (isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar Passaporte do aluno");
						}
					}
					else{
						PessoaTipoDocumentacao pessoaTipoDocumentacaoPassaporte = new PessoaTipoDocumentacao(aluno.getCdAluno(), cdTipoDocumentacaoPassaporte, nrPassaporte, null, null, null, 0, 0, 0);
						if(PessoaTipoDocumentacaoDAO.insert(pessoaTipoDocumentacaoPassaporte, connect) <= 0){
							if (isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao inserir Passaporte do aluno");
						}
					}
				}
				
				//Relaciona o nÃºmero do SUS ao aluno
				if(nrSus != null && !nrSus.trim().equals("")){
					int cdTipoDocumentacaoSus = TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_SUS, connect).getCdTipoDocumentacao();
					
					PessoaTipoDocumentacao pessoaTipoDocumentacaoSus = new PessoaTipoDocumentacao(aluno.getCdAluno(), cdTipoDocumentacaoSus, nrSus, null, null, null, 0, 0, 0);
					Result result = PessoaTipoDocumentacaoServices.save(pessoaTipoDocumentacaoSus, authData, connect);
					if(result.getCode() < 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return result;
					}
				}
				else{
					int cdTipoDocumentacaoSus = TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_SUS, connect).getCdTipoDocumentacao();
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_pessoa", "" + aluno.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_tipo_documentacao", "" + cdTipoDocumentacaoSus, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmPessoaTipoDocumentacao = PessoaTipoDocumentacaoDAO.find(criterios, connect);
					if(rsmPessoaTipoDocumentacao.next()){
						if(PessoaTipoDocumentacaoDAO.delete(rsmPessoaTipoDocumentacao.getInt("cd_pessoa"), rsmPessoaTipoDocumentacao.getInt("cd_tipo_documentacao"), connect) <= 0){
							if (isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao deletar Sus do aluno");
						}
					}
				}
				
				//Verifica se a informaÃ§Ã£o de 'possui deficiÃªncia' Ã© correta de acordo aos registros de deficiencia desse aluno
				if(possuiDeficiencia >= 0){
					Result resultado = possuiDeficiencia(aluno.getCdAluno(), (possuiDeficiencia==1));
					if(resultado.getCode() < 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, resultado.getMessage());
					}
				}
			}
			
			if(matricula != null && matricula.getCdMatricula() > 0 && matricula.getCdTurma() > 0){
				Result ret = MatriculaServices.save(matricula, cdUsuario, true, true, cdTipoTransporte, connect); 
				if(ret.getCode() <= 0){
					return ret;
				}
			}
			if(isInsert){
				resultadoValidacao = validate(aluno, endereco, cdUsuario, GRUPO_VALIDACAO_INSERT, connect);
				if(!resultadoValidacao.hasPassed()){
					return resultadoValidacao;
				}
			}
			else{
				resultadoValidacao = validate(aluno, endereco, cdUsuario, GRUPO_VALIDACAO_UPDATE, connect);
				if(!resultadoValidacao.hasPassed()){
					return resultadoValidacao;
				}
			}
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			//COMPLIANCE
			ComplianceManager.process("saveCompliance", aluno, authData, tpAcao, connect);
			//LOG
			LogServices.log(tpAcao, idAcao, authData, aluno, oldValue);
			
			if(resultadoValidacao.getCode() == ValidatorResult.VALID){
				Result resultado = new Result(retorno);
				resultado.getObjects().put("ALUNO", aluno);
				resultado.getObjects().put("ENDERECO", endereco);
				resultado.setMessage((retorno<=0)?"Erro ao salvar...":"Salvo com sucesso..." + (matricula != null && matricula.getCdTurma() <= 0 ? " (InformaÃ§Ã£o de transporte ainda nÃ£o pode ser salva, pois aluno nÃ£o possui matrÃ­cula)" : ""));
				return resultado;
			}
			else{
				resultadoValidacao.getObjects().put("ALUNO", aluno);
				resultadoValidacao.getObjects().put("ENDERECO", endereco);
				resultadoValidacao.setMessage("Salvo com sucesso..." + (matricula != null && matricula.getCdTurma() <= 0 ? " (InformaÃ§Ã£o de transporte ainda nÃ£o pode ser salva, pois aluno nÃ£o possui matrÃ­cula)" : ""));
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
	public static Result remove(int cdAluno){
		return remove(cdAluno, false, null);
	}
	public static Result remove(int cdAluno, boolean cascade){
		return remove(cdAluno, cascade, null);
	}
	public static Result remove(int cdAluno, boolean cascade, Connection connect){
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
			retorno = AlunoDAO.delete(cdAluno, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro estÃ¡ vinculado a outros e nÃ£o pode ser excluÃ­do!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluÃ­do com sucesso!");
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
	
	public static ArrayList<HashMap<String, Object>> sincronizacaoInicialTeste(int cdAluno, Connection connect) {
		boolean isConnectionNull = connect==null;		
		try {		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			ArrayList<HashMap<String, Object>> response = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> ofertas = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> cursoUnidade = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> ofertasAvaliacao = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> disciplinaAvaliacao = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> tiposAvaliacao = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> aulas = new ArrayList<HashMap<String, Object>>();
			//ArrayList<HashMap<String, Object>> atividadesDesenvolvidas = new ArrayList<HashMap<String, Object>>();
			
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			ResultSetMap rsmPeriodoLetivo = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdSecretaria, connect);
			int cdPeriodoLetivoSecretaria = 0;
			if(rsmPeriodoLetivo.next())
				cdPeriodoLetivoSecretaria = rsmPeriodoLetivo.getInt("cd_periodo_letivo");
			InstituicaoPeriodo instituicaoPeriodoSecretaria = InstituicaoPeriodoDAO.get(cdPeriodoLetivoSecretaria, connect);
			
			
			ResultSetMap rsmInstituicoes = getAllInstituicoesAtual(cdAluno, instituicaoPeriodoSecretaria.getNmPeriodoLetivo(), connect);
			
			ArrayList<HashMap<String, Object>> instituicoes = new ArrayList<HashMap<String, Object>>();
			instituicoes.addAll(rsmInstituicoes.getLines());

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			//criterios.add(new ItemComparator("CD_ALUNO", String.valueOf(cdAluno), ItemComparator.EQUAL, Types.INTEGER));
			//ResultSetMap rsmAluno = AlunoDAO.find(criterios, connect);
			//aluno.addAll(rsmAluno.getLines());
			
			ResultSetMap rsmOfertaAvaliacao = OfertaAvaliacaoServices.getAll(connect);
			ofertasAvaliacao.addAll(rsmOfertaAvaliacao.getLines());
			
			for(HashMap<String, Object> oA : ofertasAvaliacao) {	
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.CD_OFERTA_AVALIACAO", String.valueOf(oA.get("CD_OFERTA_AVALIACAO")), ItemComparator.IN, Types.INTEGER));
				criterios.add(new ItemComparator("A.CD_OFERTA", String.valueOf(oA.get("CD_OFERTA")), ItemComparator.IN, Types.INTEGER));
				ResultSetMap rsm = DisciplinaAvaliacaoAlunoDAO.find(criterios, connect);
				disciplinaAvaliacao.addAll(rsm.getLines());
			}

			ResultSetMap rsmTipoAvaliacao = TipoAvaliacaoServices.getAll(connect);
			tiposAvaliacao.addAll(rsmTipoAvaliacao.getLines());

			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cdAluno", String.valueOf(cdAluno), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("B3.nm_periodo_letivo", instituicaoPeriodoSecretaria.getNmPeriodoLetivo(), ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmOfertas = OfertaDAO.find(criterios, connect);
			ofertas.addAll(rsmOfertas.getLines());
			
			for(HashMap<String, Object> oferta : ofertas) {
				int cdOferta = (int) oferta.get("CD_OFERTA");
				ResultSetMap rsmAulas = AulaServices.getAllByOferta(cdOferta, connect);
				aulas.addAll(rsmAulas.getLines());
			}
			
			
			while(rsmInstituicoes.next()) {				
				
				
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
				
				ResultSetMap rsmCursosTurma = OfertaServices.findCursosTurmasByProfessor(cdAluno, cdInstituicao, cdPeriodoLetivo, connect);			
				
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

				//ResultSetMap rsmTurmas = OfertaServices.findTurmasByProfessor(cdProfessor, cdInstituicao, cdPeriodoLetivo, connect);
				//turmas = rsmTurmas.getLines();

				ResultSetMap rsmDisciplinas = OfertaServices.findDisciplinasCursosTurmasByAluno(cdAluno, cdInstituicao, cdPeriodoLetivo, connect);
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

				

				for(HashMap<String, Object> matricula : matriculas) {
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("CD_MATRICULA", String.valueOf((int) matricula.get("CD_MATRICULA")), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmMatriculasUnidade = MatriculaUnidadeServices.find(criterios, connect);
					matriculaUnidade.addAll(rsmMatriculasUnidade.getLines());
				}
				
				
				//ResultSetMap rsmAtividadesDesenvolvidas = AtividadeDesenvolvidaServices.getAllByProfessorInstituicao(cdProfessor, cdInstituicao, connect);
				/*while(rsmAtividadesDesenvolvidas.next()) {
					atividadesDesenvolvidas.addAll(rsmAtividadesDesenvolvidas.getLines());
				}*/

				HashMap<String, Object> instituicao = new HashMap<String, Object>();
				HashMap<String, Object> data = new HashMap<String, Object>();
				//data.put("grl_pessoa", pessoas);
				//data.put("grl_pessoa_endereco", pessoasEnderecos);
				//data.put("acd_aluno", alunos);
				data.put("acd_curso", cursos);
				data.put("acd_curso_etapa", cursosEtapas);
				//data.put("acd_instituicao_curso", instituicoesCursos);
				//data.put("acd_turma", turmas);
				data.put("acd_disciplina", disciplinas);
				//data.put("acd_aula_matricula", aulaMatriculas);
				//data.put("acd_matricula", matriculas);
				data.put("acd_matricula_unidade", matriculaUnidade);
				//data.put("acd_atividade_desenvolvida", atividadesDesenvolvidas);

				if(rsmInstituicoes.getPointer() == 0) {
					data.put("acd_aula", aulas);
					//data.put("acd_professor", professores);
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
	public static ArrayList<HashMap<String, Object>> sincronizacaoInicial(int cdAluno, Connection connect) {
        boolean isConnectionNull = connect==null;       
        try {       
            if (isConnectionNull){
                connect = Conexao.conectar();
                connect.setAutoCommit(false);
            }
            ArrayList<HashMap<String, Object>> response = new ArrayList<HashMap<String, Object>>();
            ArrayList<HashMap<String, Object>> aluno = new ArrayList<HashMap<String, Object>>();
            ArrayList<HashMap<String, Object>> ofertas = new ArrayList<HashMap<String, Object>>();
            ArrayList<HashMap<String, Object>> cursoUnidade = new ArrayList<HashMap<String, Object>>();
            ArrayList<HashMap<String, Object>> ofertasAvaliacao = new ArrayList<HashMap<String, Object>>();
            ArrayList<HashMap<String, Object>> disciplinaAvaliacao = new ArrayList<HashMap<String, Object>>();
            ArrayList<HashMap<String, Object>> tiposAvaliacao = new ArrayList<HashMap<String, Object>>();
            ArrayList<HashMap<String, Object>> boletim = new ArrayList<HashMap<String, Object>>();
            int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
            ResultSetMap rsmPeriodoLetivo = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdSecretaria, connect);
            int cdPeriodoLetivoSecretaria = 0;
            if(rsmPeriodoLetivo.next())
                cdPeriodoLetivoSecretaria = rsmPeriodoLetivo.getInt("cd_periodo_letivo");
            InstituicaoPeriodo instituicaoPeriodoSecretaria = InstituicaoPeriodoDAO.get(cdPeriodoLetivoSecretaria, connect);
            
            
            ResultSetMap rsmInstituicoes = getAllInstituicoesAtual(cdAluno, instituicaoPeriodoSecretaria.getNmPeriodoLetivo(), connect);
            System.out.println(rsmInstituicoes);
            
            ArrayList<HashMap<String, Object>> instituicoes = new ArrayList<HashMap<String, Object>>();
            instituicoes.addAll(rsmInstituicoes.getLines());
            ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
            criterios.add(new ItemComparator("CD_ALUNO", String.valueOf(cdAluno), ItemComparator.EQUAL, Types.INTEGER));
            ResultSetMap rsmAluno = AlunoServices.find(criterios, connect);
            aluno.addAll(rsmAluno.getLines());
            
            System.out.println(rsmAluno);
            
            ResultSetMap rsmOfertaAvaliacao = OfertaAvaliacaoServices.getAll(connect);
            ofertasAvaliacao.addAll(rsmOfertaAvaliacao.getLines());
            for(HashMap<String, Object> oA : ofertasAvaliacao) {  
                criterios = new ArrayList<ItemComparator>();
                criterios.add(new ItemComparator("A.CD_OFERTA_AVALIACAO", String.valueOf(oA.get("CD_OFERTA_AVALIACAO")), ItemComparator.IN, Types.INTEGER));                
                ResultSetMap rsm = DisciplinaAvaliacaoAlunoServices.find(criterios, connect);
                disciplinaAvaliacao.addAll(rsm.getLines());
            }
            ResultSetMap rsmTipoAvaliacao = TipoAvaliacaoServices.getAll(connect);
            tiposAvaliacao.addAll(rsmTipoAvaliacao.getLines());
            criterios = new ArrayList<ItemComparator>();
            criterios.add(new ItemComparator("cdAluno", String.valueOf(cdAluno), ItemComparator.EQUAL, Types.INTEGER));
            criterios.add(new ItemComparator("B3.nm_periodo_letivo", instituicaoPeriodoSecretaria.getNmPeriodoLetivo(), ItemComparator.EQUAL, Types.VARCHAR));
            ResultSetMap rsmOfertas = OfertaServices.find(criterios, connect);
            ofertas.addAll(rsmOfertas.getLines());
            
            while(rsmInstituicoes.next()) {             
                ArrayList<HashMap<String, Object>> aulas = new ArrayList<HashMap<String, Object>>();        
                ArrayList<HashMap<String, Object>> alunos = new ArrayList<HashMap<String, Object>>();
                ArrayList<HashMap<String, Object>> turmas = new ArrayList<HashMap<String, Object>>();
                ArrayList<HashMap<String, Object>> matriculas = new ArrayList<HashMap<String, Object>>();   
                ArrayList<HashMap<String, Object>> disciplinas = new ArrayList<HashMap<String, Object>>();
                ArrayList<HashMap<String, Object>> aulaMatriculas = new ArrayList<HashMap<String, Object>>();
                ArrayList<HashMap<String, Object>> produtoServico = new ArrayList<HashMap<String, Object>>();
                ArrayList<HashMap<String, Object>> instituicoesCursos = new ArrayList<HashMap<String, Object>>();
                ArrayList<HashMap<String, Object>> cursos = new ArrayList<HashMap<String, Object>>();
                
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
                
                ResultSetMap rsmCursosTurma = OfertaServices.findCursosTurmasByProfessor(cdAluno, cdInstituicao, cdPeriodoLetivo, connect);         
                
                cursos = rsmCursosTurma.getLines();
                for(HashMap<String, Object> r : cursos) { 
                    r.remove("RSM_TURMAS");
                }
                criterios = new ArrayList<ItemComparator>();
                criterios.add(new ItemComparator("A.ID_PRODUTO_SERVICO", "0", ItemComparator.DIFFERENT, Types.VARCHAR));
                ResultSetMap rsmProdutoServico = ProdutoServicoServices.find(criterios, connect);
                System.out.println(rsmProdutoServico);
                produtoServico.addAll(rsmProdutoServico.getLines());
                ResultSetMap rsmTurmas = OfertaServices.findTurmasByProfessor(cdAluno, cdInstituicao, cdPeriodoLetivo, connect);
                turmas = rsmTurmas.getLines();
                ResultSetMap rsmDisciplinas = OfertaServices.findDisciplinasCursosTurmasByAluno(cdAluno, cdInstituicao, cdPeriodoLetivo, connect);
                disciplinas = rsmDisciplinas.getLines();
                System.out.println(rsmDisciplinas);
                String strInDisciplinas = "";
                for(HashMap<String, Object> disciplina : disciplinas) {
                    int cdDisciplina = (int) disciplina.get("CD_DISCIPLINA");
                    strInDisciplinas += cdDisciplina + ", ";
                    disciplina.remove("RSM_TURMAS");
                    for(HashMap<String, Object> turma : turmas) {
                        int cdTurma = (int) turma.get("CD_TURMA");
                        criterios = new ArrayList<ItemComparator>();
                        criterios.add(new ItemComparator("cdAluno", String.valueOf(cdAluno), ItemComparator.EQUAL, Types.INTEGER));
                        criterios.add(new ItemComparator("A.CD_PERIODO_LETIVO", String.valueOf(cdPeriodoLetivo), ItemComparator.EQUAL, Types.INTEGER));
                        criterios.add(new ItemComparator("A.CD_TURMA", String.valueOf(cdTurma), ItemComparator.EQUAL, Types.INTEGER));
                        criterios.add(new ItemComparator("A.CD_DISCIPLINA", String.valueOf(cdDisciplina), ItemComparator.EQUAL, Types.INTEGER));
                        criterios.add(new ItemComparator("A.ST_OFERTA", String.valueOf(OfertaServices.ST_ATIVO), ItemComparator.EQUAL, Types.INTEGER));
                        ResultSetMap rsmOfertasAula = OfertaServices.find(criterios, connect);
                        while(rsmOfertasAula.next()) {
                            ResultSetMap rsmAulas = AulaServices.getAllByOferta(rsmOfertasAula.getInt("CD_OFERTA"), connect);
                            aulas.addAll(rsmAulas.getLines());
                        }
                    }
                }
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
                
//              for(HashMap<String, Object> aulaMatricula : aulaMatriculas) {
//                  criterios = new ArrayList<ItemComparator>();
//                  criterios.add(new ItemComparator("A.CD_MATRICULA", String.valueOf((int)aulaMatricula.get("CD_MATRICULA")), ItemComparator.EQUAL, Types.INTEGER));
//                  ResultSetMap rsmMatricula = MatriculaServices.findSimplificado(criterios, connect);
//                  matriculas.addAll(rsmMatricula.getLines());
//              }
                for(HashMap<String, Object> turma : turmas) {
                    criterios = new ArrayList<ItemComparator>();
                    ResultSetMap rsmAlunos = TurmaServices.getAlunos((int) turma.get("CD_TURMA"), connect);
                    alunos.addAll(rsmAlunos.getLines());
                    matriculas.addAll(rsmAlunos.getLines());
                }
              
                HashMap<String, Object> instituicao = new HashMap<String, Object>();
                HashMap<String, Object> data = new HashMap<String, Object>();
               
                data.put("acd_aluno", alunos);
                data.put("acd_curso", cursos);
                data.put("acd_instituicao_curso", instituicoesCursos);
                data.put("acd_turma", turmas);
                data.put("acd_disciplina", disciplinas);
                data.put("acd_aula_matricula", aulaMatriculas);
                data.put("acd_matricula", matriculas);
                if(rsmInstituicoes.getPointer() == 0) {
                    data.put("acd_aula", aulas);
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
	
	
	public static ResultSetMap getAllInstituicoesAtual(int cdAluno, String nmPeriodoLetivo) {
		return getAllInstituicoesAtual(cdAluno, nmPeriodoLetivo, null);
	}
	
	public static ResultSetMap getAllInstituicoesAtual(int cdAluno, String nmPeriodoLetivo, Connection connect) {
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
					+ "															AND O.cd_aluno = " +cdAluno+")");
			
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
	
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_aluno");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllMatriculas(int cdAluno, int cdInstituicao, int stMatricula) {
		return getAllMatriculas(cdAluno, cdInstituicao, stMatricula, null);
	}
	
	public static ResultSetMap getAllMatriculas(int cdAluno, int cdInstituicao) {
		return getAllMatriculas(cdAluno, cdInstituicao, -1, null);
	}
	
	public static ResultSetMap getAllMatriculas(int cdAluno, int cdInstituicao, Connection connect) {
		return getAllMatriculas(cdAluno, cdInstituicao, -1, connect);
	}
	
	public static ResultSetMap getAllMatriculas(int cdAluno, int cdInstituicao, int stMatricula, Connection connect) {
		return getAllMatriculas(cdAluno, cdInstituicao, stMatricula, -1, connect);
	}
	
	public static ResultSetMap getAllMatriculas(int cdAluno, int cdInstituicao, int stMatricula, int stTurma, Connection connect) {
		return getAllMatriculas(cdAluno, cdInstituicao, stMatricula, stTurma, false, connect);
	}
	
	/**
	 * Busca todas as matriculas de um aluno
	 * @param cdAluno
	 * @param cdInstituicao
	 * @param stMatricula
	 * @param stTurma
	 * @param lancarCenso
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAllMatriculas(int cdAluno, int cdInstituicao, int stMatricula, int stTurma, boolean lancarCenso, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ResultSetMap rsm = null;
			
			if(!lancarCenso){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.cd_aluno", "" + cdAluno, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cdInstituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
				if(stMatricula>=0){
					criterios.add(new ItemComparator("st_matricula", "" + stMatricula, ItemComparator.EQUAL, Types.INTEGER));
				}
				if(stTurma>=0){
					criterios.add(new ItemComparator("st_turma", "" + stTurma, ItemComparator.EQUAL, Types.INTEGER));
				}
				
				rsm = MatriculaServices.find(criterios, connect);
			}
			//Caso seja um lanÃ§amento para o censo, usarÃ¡ a data de referencia para verificar se o mesmo entra ou nÃ£o na relaÃ§Ã£o
			else{
				String dtReferenciaCenso = ParametroServices.getValorOfParametro("DT_REFERENCIA_CENSO", connect);
				dtReferenciaCenso = dtReferenciaCenso + "/" + Util.getDataAtual().get(Calendar.YEAR);
				GregorianCalendar dtReferenciaCensoCalendar = Util.convStringToCalendar4(dtReferenciaCenso);
				
				ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
				InstituicaoPeriodo instituicaoPeriodoAtual = null;
				if(rsmPeriodoAtual.next()){
					instituicaoPeriodoAtual = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect);
				}
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.cd_aluno", "" + cdAluno, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cdInstituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
				
				rsm = MatriculaServices.find(criterios, connect);
				
				int x = 0;
				while(rsm.next()){
					boolean excluido = false;
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_aluno", "" + rsm.getInt("cd_aluno"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_ALUNO, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmAlunosOcorrenciaAluno = OcorrenciaAlunoServices.find(criterios, connect);
					while(rsmAlunosOcorrenciaAluno.next()){
						if(rsmAlunosOcorrenciaAluno.getGregorianCalendar("dt_ocorrencia").after(dtReferenciaCensoCalendar)){
							rsm.deleteRow();
							if(x == 0)
								rsm.beforeFirst();
							else
								rsm.previous();
							excluido = true;
							break;
						}
					}
					rsmAlunosOcorrenciaAluno.beforeFirst();
					
					if(excluido)
						continue;
					
					excluido = false;
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_matricula_origem", "" + rsm.getInt("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_MATRICULA, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmAlunosOcorrenciaMatricula = OcorrenciaMatriculaServices.find(criterios, connect);
					while(rsmAlunosOcorrenciaMatricula.next()){
						if(rsmAlunosOcorrenciaMatricula.getGregorianCalendar("dt_ocorrencia").after(dtReferenciaCensoCalendar)){
							rsm.deleteRow();
							if(x == 0)
								rsm.beforeFirst();
							else
								rsm.previous();
							excluido = true;
							break;
						}
					}
					rsmAlunosOcorrenciaAluno.beforeFirst();
					
					if(excluido)
						continue;
					
					rsmAlunosOcorrenciaMatricula = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_ocorrencia_matricula A, grl_ocorrencia B "
							+ "																				WHERE A.cd_ocorrencia = B.cd_ocorrencia "
							+ "																				  AND (cd_matricula_origem = "+rsm.getInt("cd_matricula") + " OR cd_matricula_destino = " + rsm.getInt("cd_matricula") + ")"
							+ "																				ORDER BY dt_ocorrencia DESC").executeQuery());
					while(rsmAlunosOcorrenciaMatricula.next()){
						Matricula matricula = MatriculaDAO.get(rsmAlunosOcorrenciaMatricula.getInt("cd_matricula_origem"), connect);
						InstituicaoPeriodo instituicaoPeriodoMatricula = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect);
						if(!instituicaoPeriodoMatricula.getNmPeriodoLetivo().equals(instituicaoPeriodoAtual.getNmPeriodoLetivo())){
							continue;
						}
						
						if(rsmAlunosOcorrenciaMatricula.getGregorianCalendar("dt_ocorrencia").after(dtReferenciaCensoCalendar)){
							rsm.setValueToField("ST_MATRICULA", rsmAlunosOcorrenciaMatricula.getInt("st_matricula_origem"));
						}
					}
					rsmAlunosOcorrenciaMatricula.beforeFirst();
					
					if(rsm.getInt("ST_MATRICULA") != MatriculaServices.ST_ATIVA){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						excluido = true;
						continue;
					}
					x++;
				}
				rsm.beforeFirst();
				
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findByName(String nmAluno, String nmMae) {
		return findByName(nmAluno, nmMae, null);
	}
	
	/**
	 * Faz a busca do aluno a partir do nome e do nome da mÃ£e, fazendo um comparativo de porcentagem para saber se eles possuem mais de 80% de aproximaÃ§Ã£o
	 * @param nmAluno
	 * @param nmMae
	 * @param connect
	 * @return
	 */
	public static ResultSetMap findByName(String nmAluno, String nmMae, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsmFinal = new ResultSetMap();
			
			ResultSetMap rsmAlunos = new ResultSetMap(connect.prepareStatement("SELECT P.cd_pessoa, P.nm_pessoa, P.nm_pessoa AS nm_aluno, P.nr_telefone1, P.nr_telefone2, P.nr_celular, PF.dt_nascimento, PF.nm_mae, PF.nm_pai, PF.nr_rg, PF.cd_estado_rg, PF.tp_sexo, A.nm_responsavel, F.cd_cidade, F.nm_logradouro, F.nm_bairro, F.nr_endereco, F.nm_complemento, F.nr_telefone, F.tp_zona AS tp_zona_aluno, F.nr_cep, G.nm_cidade, " 
	 			    +"	  																						A.cd_aluno, A.nr_inep, A.lg_bolsa_familia, A.lg_pai_nao_declarado, A.lg_cadastro_problema, B.cd_matricula, B.nr_matricula, B.st_matricula, B.dt_matricula, PF.cd_naturalidade, IP.nm_periodo_letivo, C.nr_idade,  "
	 			    +"																							H.nm_produto_servico AS nm_curso, PTD.nr_documento, PTD.livro, PTD.folha, B.nr_matricula, IP.nm_periodo_letivo, B.st_matricula, H.nm_produto_servico AS nm_curso  FROM grl_pessoa P "
					+ "																			      LEFT OUTER JOIN grl_pessoa_tipo_documentacao PTD ON (P.cd_pessoa = PTD.cd_pessoa AND PTD.cd_tipo_documentacao IN ("+PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_NASCIMENTO+", "+PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_CASAMENTO+")) "
					+ "																				  LEFT OUTER JOIN grl_pessoa_fisica PF ON (P.cd_pessoa = PF.cd_pessoa)"
					+ "																				  LEFT OUTER JOIN grl_estado ERG ON (PF.cd_estado_rg = ERG.cd_estado)"
					+ "																				  JOIN acd_aluno A ON (P.cd_pessoa = A.cd_aluno) "
					+ "																				  LEFT OUTER JOIN acd_matricula B ON (A.cd_aluno = B.cd_aluno) "
					+ "																				  LEFT OUTER JOIN acd_instituicao_periodo IP ON (B.cd_periodo_letivo = IP.cd_periodo_letivo) "
					+ "																				  LEFT OUTER JOIN acd_curso C ON (B.cd_curso = C.cd_curso) "
					+ "																				  LEFT OUTER JOIN acd_curso_etapa D ON (C.cd_curso = D.cd_curso) "
					+ "																				  LEFT OUTER JOIN acd_tipo_etapa E ON (D.cd_etapa = E.cd_etapa) "
					+ "																				  LEFT OUTER JOIN grl_produto_servico H ON (C.cd_curso = H.cd_produto_servico) "
					+ "																				  LEFT OUTER JOIN grl_pessoa_endereco F ON (P.cd_pessoa = F.cd_pessoa AND F.lg_principal = 1) "
					+ "																				  LEFT OUTER JOIN grl_cidade G ON (F.cd_cidade = G.cd_cidade) "
					+ "																			ORDER BY P.nm_pessoa ").executeQuery());
			 
			while(rsmAlunos.next()){
				double porcentagem = Util.compareStrings(rsmAlunos.getString("nm_aluno").trim(), rsmAlunos.getString("nm_mae").trim(), null, nmAluno.trim(), nmMae.trim(), null, Float.parseFloat("0.8"));
				if(porcentagem > 0.8){
					GregorianCalendar dtNascimento = rsmAlunos.getGregorianCalendar("dt_nascimento");
					if(dtNascimento != null)
						rsmAlunos.setValueToField("DT_NM_NASCIMENTO", (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.DAY_OF_MONTH))) > 9 ? dtNascimento.get(Calendar.DAY_OF_MONTH) : "0" + dtNascimento.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1)) + "/" + dtNascimento.get(Calendar.YEAR));
					
					rsmAlunos.setValueToField("CL_TP_SEXO", PessoaFisicaServices.tipoSexo[rsmAlunos.getInt("tp_sexo")]);
					
					Estado estado = EstadoDAO.get(rsmAlunos.getInt("cd_estado_rg"));
					if(estado != null)
						rsmAlunos.setValueToField("SG_UF_RG", estado.getSgEstado());
					
					rsmAlunos.setValueToField("CL_ST_MATRICULA", MatriculaServices.situacao[rsmAlunos.getInt("st_matricula")]);
					
					rsmFinal.addRegister(rsmAlunos.getRegister());
				}
			}
			rsmFinal.beforeFirst();
			
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
	
	public static Result corrigirDuplicidade(int cdAluno, ResultSetMap rsmAlunos) {
		return corrigirDuplicidade(cdAluno, rsmAlunos, null);
	}
	
	/**
	 * Pega um determinado cÃ³digo de aluno (que seria o aluno com os dados corretos) e atualiza todos os dados relacionados a uma lista de alunos que seriam duplicatas do primeiro, apagando ao final o registro de acd_aluno
	 * dos mesmos
	 * @param cdAluno
	 * @param rsmAlunos
	 * @param connect
	 * @return
	 */
	public static Result corrigirDuplicidade(int cdAluno, ResultSetMap rsmAlunos, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			while(rsmAlunos.next()){
				Aluno aluno = AlunoDAO.get(cdAluno, connect);
				if((aluno.getNrInep() == null || aluno.getNrInep().equals("")) && (rsmAlunos.getString("NR_INEP") != null && !rsmAlunos.getString("NR_INEP").equals(""))){
					aluno.setNrInep(rsmAlunos.getString("NR_INEP"));
					if(AlunoDAO.update(aluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar aluno");
					}
				}
				
				if(rsmAlunos.getInt("cd_aluno") != cdAluno){
					
					//Tabelas para atualizar com o novo cÃ³digo de aluno
					ResultSetMap rsmMatriculas = MatriculaServices.getAllByAluno(rsmAlunos.getInt("cd_aluno"), connect);
					while(rsmMatriculas.next()){
						Matricula matricula = MatriculaDAO.get(rsmMatriculas.getInt("cd_matricula"), connect);
						matricula.setCdAluno(cdAluno);
						if(MatriculaDAO.update(matricula, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar matricula");
						}
					}
					
					ResultSetMap rsmOcorrencias = OcorrenciaServices.getAllByPessoa(rsmAlunos.getInt("cd_aluno"), connect);
					while(rsmOcorrencias.next()){
						Ocorrencia ocorrencia = OcorrenciaDAO.get(rsmOcorrencias.getInt("cd_ocorrencia"), connect);
						ocorrencia.setCdPessoa(cdAluno);
						if(OcorrenciaDAO.update(ocorrencia, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar ocorrencia");
						}
						
						OcorrenciaAluno ocorrenciaAluno = OcorrenciaAlunoDAO.get(rsmOcorrencias.getInt("cd_ocorrencia"), connect);
						if(ocorrenciaAluno != null){
							ocorrenciaAluno.setCdAluno(cdAluno);
							if(OcorrenciaAlunoDAO.update(ocorrenciaAluno, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao atualizar ocorrencia aluno");
							}
						}
					}
					
					ResultSetMap rsmInstituicaoPendencia = InstituicaoPendenciaServices.getAllByAluno(rsmAlunos.getInt("cd_aluno"), connect);
					while(rsmInstituicaoPendencia.next()){
						InstituicaoPendencia instituicaoPendencia = InstituicaoPendenciaDAO.get(rsmInstituicaoPendencia.getInt("cd_instituicao_pendencia"), rsmInstituicaoPendencia.getInt("cd_instituicao"), connect);
						instituicaoPendencia.setCdAluno(cdAluno);
						if(InstituicaoPendenciaDAO.update(instituicaoPendencia, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar instituicao pendencia");
						}
					}
					
					ResultSetMap rsmAlunoResponsavel = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_aluno WHERE cd_responsavel = " + rsmAlunos.getInt("cd_aluno")).executeQuery());
					while(rsmAlunoResponsavel.next()){
						Aluno alunoDependente = AlunoDAO.get(rsmAlunos.getInt("cd_aluno"), connect);
						alunoDependente.setCdResponsavel(cdAluno);
						if(AlunoDAO.update(alunoDependente, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar aluno dependente");
						}
					}
					
					//Tabelas para apagar
					int ret = connect.prepareStatement("DELETE FROM grl_pessoa_empresa WHERE cd_pessoa = " + rsmAlunos.getInt("cd_aluno")).executeUpdate();
					if(ret < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao deletar grl_pessoa_empresa");
					}
					ret = connect.prepareStatement("DELETE FROM grl_pessoa_doenca WHERE cd_pessoa = " + rsmAlunos.getInt("cd_aluno")).executeUpdate();
					if(ret < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao deletar grl_pessoa_doenca");
					}
					ret = connect.prepareStatement("DELETE FROM grl_pessoa_alergia WHERE cd_pessoa = " + rsmAlunos.getInt("cd_aluno")).executeUpdate();
					if(ret < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao deletar grl_pessoa_alergia");
					}
					ret = connect.prepareStatement("DELETE FROM grl_pessoa_ficha_medica WHERE cd_pessoa = " + rsmAlunos.getInt("cd_aluno")).executeUpdate();
					if(ret < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao deletar grl_pessoa_ficha_medica");
					}
					ret = connect.prepareStatement("DELETE FROM acd_aluno_recurso_prova WHERE cd_aluno = " + rsmAlunos.getInt("cd_aluno")).executeUpdate();
					if(ret < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao deletar acd_aluno_recurso_prova");
					}
					ret = connect.prepareStatement("DELETE FROM acd_aluno_tipo_transporte WHERE cd_aluno = " + rsmAlunos.getInt("cd_aluno")).executeUpdate();
					if(ret < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao deletar acd_aluno_tipo_transporte");
					}
					ret = connect.prepareStatement("DELETE FROM acd_aluno WHERE cd_aluno = " + rsmAlunos.getInt("cd_aluno")).executeUpdate();
					if(ret < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao deletar acd_aluno");
					}
					ret = connect.prepareStatement("DELETE FROM grl_pessoa_fisica WHERE cd_pessoa = " + rsmAlunos.getInt("cd_aluno")).executeUpdate();
					if(ret < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao deletar grl_pessoa_fisica");
					}
					ret = connect.prepareStatement("DELETE FROM grl_pessoa_endereco WHERE cd_pessoa = " + rsmAlunos.getInt("cd_aluno")).executeUpdate();
					if(ret < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao deletar grl_pessoa_endereco");
					}
					ret = connect.prepareStatement("DELETE FROM grl_pessoa_tipo_documentacao WHERE cd_pessoa = " + rsmAlunos.getInt("cd_aluno")).executeUpdate();
					if(ret < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao deletar grl_pessoa_tipo_documentacao");
					}
					ret = connect.prepareStatement("DELETE FROM grl_pessoa WHERE cd_pessoa = " + rsmAlunos.getInt("cd_aluno")).executeUpdate();
					if(ret < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao deletar grl_pessoa");
					}
				}
				
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "CorreÃ§Ã£o feita com sucesso");
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
	
	public static Result corrigirCorrecaoDuplicidade(){
		String nmUrlDatabase = "jdbc:postgresql://127.0.0.1/edf_21092016";
		String nmLoginDataBase = "postgres";
		String nmSenhaDatBase = "CDF3DP!";
		
		return corrigirCorrecaoDuplicidade(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
	}
	
	public static Result corrigirCorrecaoDuplicidade(String nmUrlDatabase, String nmLoginDataBase, String nmSenhaDatBase){
		//Faz a conexao com o Local
		Connection connectLocal = Conexao.conectar();
		try{
			connectLocal.setAutoCommit(false);
			

			Connection connectServer = Conexao.conectar(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
			
			int cdEmpresa = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			try{
				
				connectServer.setAutoCommit(false);
				int qt = 0;
				ResultSetMap rsmServer = new ResultSetMap(connectServer.prepareStatement("select * from acd_matricula WHERE cd_matricula IN (197912, " +
																							"152263, " +
																							"66619, " +
																							"233121, " +
																							"234786, " +
																							"182236, " +
																							"187270, " +
																							"191010, " +
																							"64207, " +
																							"198962, " +
																							"196501, " +
																							"144827 " +
																							")").executeQuery());
				HashMap<Integer, Integer> codigos = new HashMap<Integer, Integer>();
				while(rsmServer.next()){
					Aluno alunoServer = AlunoDAO.get(rsmServer.getInt("cd_aluno"), connectServer);
					int cdAlunoAntigo = alunoServer.getCdAluno();
					alunoServer.setCdPessoa(0);
					alunoServer.setCdAluno(0);
					int cdAluno = 0;
					
					if(!codigos.containsKey(rsmServer.getInt("cd_aluno"))){
						cdAluno = AlunoDAO.insert(alunoServer, connectLocal);
						codigos.put(rsmServer.getInt("cd_aluno"), cdAluno);
					
						if(cdAluno < 0){
							Conexao.rollback(connectServer);
							Conexao.rollback(connectLocal);
							return new Result(-1, "Erro ao inserir pessoa");
						}
						
						alunoServer.setCdAluno(cdAlunoAntigo);
						alunoServer.setCdPessoa(cdAlunoAntigo);
						
						ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_pessoa", "" + alunoServer.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmEnderecoServer = PessoaEnderecoDAO.find(criterios, connectServer);
						PessoaEndereco pessoaEnderecoServer = null;
						if(rsmEnderecoServer.next()){
							pessoaEnderecoServer = PessoaEnderecoDAO.get(rsmEnderecoServer.getInt("cd_endereco"), rsmEnderecoServer.getInt("cd_pessoa"), connectServer);
						}
						if(pessoaEnderecoServer != null){
							pessoaEnderecoServer.setCdPessoa(cdAluno);
							if(PessoaEnderecoDAO.insert(pessoaEnderecoServer, connectLocal) < 0){
								Conexao.rollback(connectServer);
								Conexao.rollback(connectLocal);
								return new Result(-1, "Erro ao inserir pessoa endereco");
							}
						}
						
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_pessoa", "" + alunoServer.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmDocumentacaoServer = PessoaTipoDocumentacaoDAO.find(criterios, connectServer);
						PessoaTipoDocumentacao pessoaDocumentacaoServer = null;
						while(rsmDocumentacaoServer.next()){
							pessoaDocumentacaoServer = PessoaTipoDocumentacaoDAO.get(rsmDocumentacaoServer.getInt("cd_pessoa"), rsmDocumentacaoServer.getInt("cd_tipo_documentacao"), connectServer);
							
							pessoaDocumentacaoServer.setCdPessoa(cdAluno);
							if(PessoaTipoDocumentacaoDAO.insert(pessoaDocumentacaoServer, connectLocal) < 0){
								Conexao.rollback(connectServer);
								Conexao.rollback(connectLocal);
								return new Result(-1, "Erro ao inserir pessoa documentaÃ§Ã£o");
							}
						}
						
						
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_aluno", "" + alunoServer.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmTipoTransporteServer = AlunoTipoTransporteDAO.find(criterios, connectServer);
						AlunoTipoTransporte alunoTipoTransporte = null;
						while(rsmTipoTransporteServer.next()){
							alunoTipoTransporte = AlunoTipoTransporteDAO.get(rsmTipoTransporteServer.getInt("cd_aluno"), rsmTipoTransporteServer.getInt("cd_tipo_transporte"), connectServer);
							
							alunoTipoTransporte.setCdAluno(cdAluno);
							if(AlunoTipoTransporteDAO.insert(alunoTipoTransporte, connectLocal) < 0){
								Conexao.rollback(connectServer);
								Conexao.rollback(connectLocal);
								return new Result(-1, "Erro ao inserir aluno tipo transporte");
							}
						}
						
						
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_aluno", "" + alunoServer.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmRecursoProvaServer = AlunoRecursoProvaDAO.find(criterios, connectServer);
						AlunoRecursoProva alunoRecursoProva = null;
						while(rsmRecursoProvaServer.next()){
							alunoRecursoProva = AlunoRecursoProvaDAO.get(rsmRecursoProvaServer.getInt("cd_aluno"), rsmRecursoProvaServer.getInt("cd_tipo_recurso_prova"), connectServer);
							
							alunoRecursoProva.setCdAluno(cdAluno);
							if(AlunoRecursoProvaDAO.insert(alunoRecursoProva, connectLocal) < 0){
								Conexao.rollback(connectServer);
								Conexao.rollback(connectLocal);
								return new Result(-1, "Erro ao inserir aluno recurso prova");
							}
						}
						
						
						
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_pessoa", "" + alunoServer.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmPessoaFichaMedicaServer = PessoaFichaMedicaDAO.find(criterios, connectServer);
						PessoaFichaMedica pessoaFichaMedica = null;
						if(rsmPessoaFichaMedicaServer.next()){
							pessoaFichaMedica = PessoaFichaMedicaDAO.get(rsmPessoaFichaMedicaServer.getInt("cd_pessoa"), connectServer);
						}
						if(pessoaFichaMedica != null){
							pessoaFichaMedica.setCdPessoa(cdAluno);
							if(PessoaFichaMedicaDAO.insert(pessoaFichaMedica, connectLocal) < 0){
								Conexao.rollback(connectServer);
								Conexao.rollback(connectLocal);
								return new Result(-1, "Erro ao inserir pessoa ficha medica");
							}
						}
						
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_pessoa", "" + alunoServer.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmPessoaDoencaServer = PessoaDoencaDAO.find(criterios, connectServer);
						PessoaDoenca pessoaDoenca = null;
						while(rsmPessoaDoencaServer.next()){
							pessoaDoenca = PessoaDoencaDAO.get(rsmPessoaDoencaServer.getInt("cd_pessoa"), rsmPessoaDoencaServer.getInt("cd_doenca"), connectServer);
							
							pessoaDoenca.setCdPessoa(cdAluno);
							if(PessoaDoencaDAO.insert(pessoaDoenca, connectLocal) < 0){
								Conexao.rollback(connectServer);
								Conexao.rollback(connectLocal);
								return new Result(-1, "Erro ao inserir pessoa doenÃ§a");
							}
						}
						
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_pessoa", "" + alunoServer.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmPessoaAlergiaServer = PessoaAlergiaDAO.find(criterios, connectServer);
						PessoaAlergia pessoaAlergia = null;
						while(rsmPessoaAlergiaServer.next()){
							pessoaAlergia = PessoaAlergiaDAO.get(rsmPessoaAlergiaServer.getInt("cd_pessoa"), rsmPessoaAlergiaServer.getInt("cd_alergia"), connectServer);
							
							pessoaAlergia.setCdPessoa(cdAluno);
							if(PessoaAlergiaDAO.insert(pessoaAlergia, connectLocal) < 0){
								Conexao.rollback(connectServer);
								Conexao.rollback(connectLocal);
								return new Result(-1, "Erro ao inserir pessoa alergia");
							}
						}
						
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_pessoa", "" + alunoServer.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmPessoaNecessidadeEspecialServer = PessoaNecessidadeEspecialDAO.find(criterios, connectServer);
						PessoaNecessidadeEspecial pessoaNecessidadeEspecial = null;
						while(rsmPessoaNecessidadeEspecialServer.next()){
							pessoaNecessidadeEspecial = PessoaNecessidadeEspecialDAO.get(rsmPessoaNecessidadeEspecialServer.getInt("cd_pessoa"), rsmPessoaNecessidadeEspecialServer.getInt("cd_tipo_necessidade_especial"), connectServer);
							
							pessoaNecessidadeEspecial.setCdPessoa(cdAluno);
							if(PessoaNecessidadeEspecialDAO.insert(pessoaNecessidadeEspecial, connectLocal) < 0){
								Conexao.rollback(connectServer);
								Conexao.rollback(connectLocal);
								return new Result(-1, "Erro ao inserir pessoa necessidade especial");
							}
						}
						
						
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_pessoa", "" + alunoServer.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmPessoaEmpresaServer = PessoaEmpresaDAO.find(criterios, connectServer);
						PessoaEmpresa pessoaEmpresa = null;
						while(rsmPessoaEmpresaServer.next()){
							pessoaEmpresa = PessoaEmpresaDAO.get(rsmPessoaEmpresaServer.getInt("cd_empresa"), rsmPessoaEmpresaServer.getInt("cd_pessoa"), rsmPessoaEmpresaServer.getInt("cd_vinculo"), connectServer);
							
							pessoaEmpresa.setCdPessoa(cdAluno);
							if(PessoaEmpresaDAO.insert(pessoaEmpresa, connectLocal) < 0){
								Conexao.rollback(connectServer);
								Conexao.rollback(connectLocal);
								return new Result(-1, "Erro ao inserir pessoa empresa");
							}
							
						}
						
						
						ResultSetMap rsmOcorrencias = OcorrenciaServices.getAllByPessoa(alunoServer.getCdAluno(), connectServer);
						while(rsmOcorrencias.next()){
							Ocorrencia ocorrencia = OcorrenciaDAO.get(rsmOcorrencias.getInt("cd_ocorrencia"), connectLocal);
							ocorrencia.setCdPessoa(cdAluno);
							if(OcorrenciaDAO.update(ocorrencia, connectLocal) < 0){
								Conexao.rollback(connectLocal);
								Conexao.rollback(connectServer);
								return new Result(-1, "Erro ao atualizar ocorrencia");
							}
						}
						
						ResultSetMap rsmAlunoResponsavel = new ResultSetMap(connectServer.prepareStatement("SELECT * FROM acd_aluno WHERE cd_responsavel = " + alunoServer.getCdAluno()).executeQuery());
						while(rsmAlunoResponsavel.next()){
							Aluno alunoDependente = AlunoDAO.get(alunoServer.getCdAluno(), connectLocal);
							alunoDependente.setCdResponsavel(cdAluno);
							if(AlunoDAO.update(alunoDependente, connectLocal) < 0){
								Conexao.rollback(connectLocal);
								Conexao.rollback(connectServer);
								return new Result(-1, "Erro ao atualizar aluno dependente");
							}
						}
					
					}
					else{
						cdAluno = codigos.get(rsmServer.getInt("cd_aluno"));
					}
					
					Matricula matricula = MatriculaDAO.get(rsmServer.getInt("cd_matricula"), connectLocal);
					matricula.setCdAluno(cdAluno);
					if(MatriculaDAO.update(matricula, connectLocal) < 0){
						Conexao.rollback(connectLocal);
						Conexao.rollback(connectServer);
						return new Result(-1, "Erro ao atualizar matricula");
					}
					
					qt++;
					
				}
				System.out.println(qt);
				
				System.out.println("final");
				//Da commit em ambos
				connectLocal.commit();
				connectServer.commit();
			
				return new Result(1, "CorreÃ§Ãµes realizadas");
			}
			catch(Exception e){
				Conexao.rollback(connectLocal);
				Conexao.rollback(connectServer);
				//registra log de erro quando a classe Ã© utilizad pelo pdv
				Util.registerLog(e);
				e.printStackTrace();
				return new Result(-1, "Erro na importaÃ§Ã£o");
			}
			finally{
				Conexao.desconectar(connectServer);
			}
		}
		
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connectLocal);
			return new Result(-1, "Erro na sincronizaÃ§Ã£o");
		}
		finally{
			Conexao.desconectar(connectLocal);
		}
	}
	
	
	public static ResultSetMap findByCod(int cdAluno) {
		return findByCod(cdAluno, 0, 0, 0, null);
	}
	
	public static ResultSetMap findByCod(int cdAluno, int cdEmpresa, int cdInstituicao, int cdVinculo) {
		return findByCod(cdAluno, cdEmpresa, cdInstituicao, cdVinculo, null);
	}
	
	/**
	 * Busca de aluno a partir do cÃ³digo
	 * @param cdAluno
	 * @param cdEmpresa
	 * @param cdInstituicao
	 * @param cdVinculo
	 * @param connect
	 * @return
	 */
	public static ResultSetMap findByCod(int cdAluno, int cdEmpresa, int cdInstituicao, int cdVinculo, Connection connect) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_pessoa", "" + cdAluno, ItemComparator.EQUAL, Types.INTEGER));
		if(cdEmpresa > 0)
			criterios.add(new ItemComparator("F.CD_EMPRESA", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
		if(cdInstituicao > 0)
			criterios.add(new ItemComparator("A.CD_INSTITUICAO", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
		if(cdVinculo > 0)
			criterios.add(new ItemComparator("F.CD_VINCULO", "" + cdVinculo, ItemComparator.EQUAL, Types.INTEGER));
		
		criterios.add(new ItemComparator("lgIncluirPendente", "1", ItemComparator.EQUAL, Types.INTEGER));
		
		ResultSetMap rsm = find(criterios, null);
		return rsm;
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int qtLimite = 0;
			String nmPessoa = "";
			int cdInstituicao = 0;
			String nrAno = "";
			int cdCursoAtual = 0;
			int tpNomePaiDeclarado = 0;
			String nmPeriodoLetivo = "";
			int cdPeriodoLetivo = 0;
			int cdPeriodoLetivoSecretaria = 0;
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			String nrMatricula = null;
			boolean lgMatriculaPeriodo = false;
			boolean semMatriculaPeriodo = false;
			boolean lgComDeficiencia = false;
			boolean lgSemMatriculaAee = false;
			boolean lgIncluirPendente = false;
			boolean lgBuscarMatriculaPeriodoAnterior = false;
			boolean permitirVisualizarAluno = false;//Variavel usada para permitir a busca do aluno mesmo que ele esteja matriculado em outra instituiÃ§Ã£o
			String stMatricula = "" + MatriculaServices.ST_ATIVA;
			boolean lgBuscaMaisEducacao = false;
			int cdTurma = 0;
			Turma turma = null;
			ResultSetMap rsmPeriodoSecretaria = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdSecretaria, connect);
			if(rsmPeriodoSecretaria.next())
				cdPeriodoLetivoSecretaria = rsmPeriodoSecretaria.getInt("cd_periodo_letivo");
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.nm_pessoa")) {
					//Faz uma limpeza na string passada e passa o acractere %, para que a busca seja feito com tudo que contenha, independendo do que tenha antes, depois e no meio das palavras
					nmPessoa = Util.limparTexto(criterios.get(i).getValue());
					nmPessoa = "%"+nmPessoa.replaceAll(" ", "%")+"%";
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_INSTITUICAO")) {
					cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
					if(cdPeriodoLetivo == 0){
						ResultSetMap rsm = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdInstituicao, connect);
						if(rsm.next()){
							cdPeriodoLetivo = rsm.getInt("cd_periodo_letivo");
						}
					}
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nmPeriodoLetivo")) {
					nmPeriodoLetivo = criterios.get(i).getValue();
					
					if(cdInstituicao == 0){
						for (int j=0; criterios!=null && j<criterios.size(); j++) {
							if (criterios.get(j).getColumn().equalsIgnoreCase("A.CD_INSTITUICAO")) {
								cdInstituicao = Integer.parseInt(criterios.get(j).getValue());
								break;
							}
						}
					}
					
					ArrayList<ItemComparator> criteriosPeriodoLetivo = new ArrayList<ItemComparator>();
					criteriosPeriodoLetivo.add(new ItemComparator("nm_periodo_letivo", nmPeriodoLetivo, ItemComparator.EQUAL, Types.VARCHAR));
					criteriosPeriodoLetivo.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmInstituicaoPeriodo = InstituicaoPeriodoDAO.find(criteriosPeriodoLetivo, connect);
					InstituicaoPeriodo instituicaoPeriodo = null;
					if(rsmInstituicaoPeriodo.next()){
						instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmInstituicaoPeriodo.getInt("cd_periodo_letivo"), connect);
						cdPeriodoLetivo = instituicaoPeriodo.getCdPeriodoLetivo();
					}
					
					if(instituicaoPeriodo != null)
						cdPeriodoLetivoSecretaria = (instituicaoPeriodo.getCdPeriodoLetivoSuperior() == 0 ? instituicaoPeriodo.getCdPeriodoLetivo() : instituicaoPeriodo.getCdPeriodoLetivoSuperior());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrAno")) {
					nrAno = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdCursoAtual")) {
					cdCursoAtual = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpNomePaiDeclarado")) {
					tpNomePaiDeclarado = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrMatricula")) {
					nrMatricula = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgMatriculaPeriodo")) {
					lgMatriculaPeriodo = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgComDeficiencia")) {
					lgComDeficiencia = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgSemMatriculaAee")) {
					lgSemMatriculaAee = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("semMatriculaPeriodo")) {
					semMatriculaPeriodo = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgIncluirPendente")) {
					lgIncluirPendente = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgBuscarMatriculaPeriodoAnterior")) {
					lgBuscarMatriculaPeriodoAnterior = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("stMatricula")) {
					stMatricula = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("permitirVisualizarAluno")) {
					permitirVisualizarAluno = true;
				}	
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgBuscaMaisEducacao")) {
					lgBuscaMaisEducacao = true;
				}	
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdTurma")) {
					cdTurma = Integer.parseInt(criterios.get(i).getValue());
					turma = TurmaDAO.get(cdTurma, connect);
				}
				else
					crt.add(criterios.get(i));
				
			}
			
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
			String sql =
		 			  "SELECT "+sqlLimit[0]+" *, A.nm_pessoa AS NM_ALUNO, E.sg_orgao_rg AS sg_orgao_rg_pessoa_fisica, E.cd_estado_rg AS cd_estado_rg_pessoa_fisica, T.nm_bairro AS nm_bairro_enderecamento, " +
 					  "   Z.nm_pessoa AS nm_responsavel, PTD.cd_tipo_documentacao AS TP_DOCUMENTACAO, PTD.dt_emissao AS dt_emissao_registro, PTDCP.cd_pessoa AS cd_cartorio, " + 
 					  "   PTDCP.nm_pessoa AS nm_cartorio, PTDCPEC.cd_cidade AS cd_cidade_cartorio, PTDCPEC.nm_cidade AS nm_cidade_cartorio, PTDCPECE.sg_estado AS sg_estado_cartorio, " +
 					  "   F1.nm_bairro AS nm_bairro_enderecamento_resp, H1.nm_estado AS nm_estado_resp, H1.sg_estado AS sg_estado_resp " +
 					  "FROM grl_pessoa A " +
 					  "LEFT OUTER JOIN grl_pais 		B ON (A.cd_pais = B.cd_pais) " +
 					  "LEFT OUTER JOIN grl_forma_divulgacao  	C ON (A.cd_forma_divulgacao = C.cd_forma_divulgacao) " +
 					  "LEFT OUTER JOIN adm_classificacao 	D ON (A.cd_classificacao = D.cd_classificacao) " +
 					  "LEFT OUTER JOIN grl_pessoa_fisica 	E ON (A.cd_pessoa = E.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_pessoa_empresa 	F ON (A.cd_pessoa = F.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_pessoa_endereco 	P ON (A.cd_pessoa = P.cd_pessoa AND P.lg_principal = 1) " +
 					  "LEFT OUTER JOIN grl_tipo_logradouro 	Q ON (P.cd_tipo_logradouro = Q.cd_tipo_logradouro) " +
 					  "LEFT OUTER JOIN grl_logradouro 		R ON (P.cd_logradouro = R.cd_logradouro) " +
 					  "LEFT OUTER JOIN grl_tipo_logradouro 	S ON (R.cd_tipo_logradouro = S.cd_tipo_logradouro) " +
 					  "LEFT OUTER JOIN grl_bairro 		T ON (P.cd_bairro = T.cd_bairro) " +
 					  "LEFT OUTER JOIN grl_cidade 		U ON (P.cd_cidade = U.cd_cidade) " +
 					  "LEFT OUTER JOIN grl_estado 		V ON (U.cd_estado = V.cd_estado) " +
 					  "LEFT OUTER JOIN grl_pais 		VP ON (V.cd_pais = VP.cd_pais) " +
 					  "JOIN acd_aluno 				W ON (A.cd_pessoa = W.cd_aluno) " +
 					  "LEFT OUTER JOIN grl_pessoa_ficha_medica PFM ON (A.cd_pessoa = PFM.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_pessoa 		Z ON (W.cd_responsavel = Z.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_pessoa_fisica 	X ON (Z.cd_pessoa = X.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_pessoa_juridica 	A1 ON (Z.cd_pessoa = A1.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_pessoa_endereco 	B1 ON (Z.cd_pessoa = B1.cd_pessoa " +
 					  "												AND B1.lg_principal = 1) " +
 					  "LEFT OUTER JOIN grl_tipo_logradouro 	C1 ON (B1.cd_tipo_logradouro = C1.cd_tipo_logradouro) " +
 					  "LEFT OUTER JOIN grl_logradouro 		D1 ON (B1.cd_logradouro = D1.cd_logradouro) " +
 					  "LEFT OUTER JOIN grl_tipo_logradouro 	E1 ON (D1.cd_tipo_logradouro = E1.cd_tipo_logradouro) " +
 					  "LEFT OUTER JOIN grl_bairro 		F1 ON (B1.cd_bairro = F1.cd_bairro) " +
 					  "LEFT OUTER JOIN grl_cidade 		G1 ON (B1.cd_cidade = G1.cd_cidade) " +
 					  "LEFT OUTER JOIN grl_estado 		H1 ON (G1.cd_estado = H1.cd_estado) " +
 					  "LEFT OUTER JOIN grl_pessoa_tipo_documentacao PTD ON (A.cd_pessoa = PTD.cd_pessoa "
 					  + "												AND (PTD.cd_tipo_documentacao = "+ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0)+" OR PTD.cd_tipo_documentacao = "+ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)+")) " +
 					  "LEFT OUTER JOIN grl_pessoa 		PTDCP ON (PTD.cd_cartorio = PTDCP.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_pessoa_endereco		PTDCPE ON (PTDCP.cd_pessoa = PTDCPE.cd_pessoa AND PTDCPE.lg_principal = 1) " +
 					  "LEFT OUTER JOIN grl_cidade 		PTDCPEC ON (PTDCPE.cd_cidade = PTDCPEC.cd_cidade) " +
 					  "LEFT OUTER JOIN grl_estado 		PTDCPECE ON (PTDCPEC.cd_estado = PTDCPECE.cd_estado) " +
 					  "WHERE 1 = 1 " +
 				   (cdInstituicao != cdSecretaria && !semMatriculaPeriodo ? " AND EXISTS (SELECT SUB_MAT.cd_aluno FROM acd_matricula SUB_MAT WHERE SUB_MAT.cd_aluno = W.cd_aluno AND SUB_MAT.st_matricula IN ("+stMatricula+ (lgIncluirPendente ? ", " + MatriculaServices.ST_PENDENTE : "")+") AND SUB_MAT.cd_periodo_letivo = "+cdPeriodoLetivo+ (nrMatricula != null ? " AND SUB_MAT.nr_matricula = '"+nrMatricula+"'" : "")+") " : "") +
 				   (cdInstituicao == cdSecretaria && nrMatricula != null ? " AND EXISTS (SELECT SUB_MAT.cd_aluno FROM acd_matricula SUB_MAT WHERE SUB_MAT.cd_aluno = W.cd_aluno AND SUB_MAT.nr_matricula = '"+nrMatricula+"'"+") " : "") +
 				   (lgComDeficiencia ? " AND EXISTS (SELECT * FROM grl_pessoa_necessidade_especial PNE WHERE PNE.cd_pessoa = W.cd_aluno)" : "") +
				   (!nmPessoa.equals("") ? 
				   "	  AND TRANSLATE(A.nm_pessoa, 'Ã¡Ã©Ã­Ã³ÃºÃ Ã¨Ã¬Ã²Ã¹Ã£ÃµÃ¢ÃªÃ®Ã´Ã´Ã¤Ã«Ã¯Ã¶Ã¼Ã§Ã�Ã‰Ã�Ã“ÃšÃ€ÃˆÃŒÃ’Ã™ÃƒÃ•Ã‚ÃŠÃŽÃ”Ã›Ã„Ã‹Ã�Ã–ÃœÃ‡', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE TRANSLATE('%"+nmPessoa+"%', 'Ã¡Ã©Ã­Ã³ÃºÃ Ã¨Ã¬Ã²Ã¹Ã£ÃµÃ¢ÃªÃ®Ã´Ã´Ã¤Ã«Ã¯Ã¶Ã¼Ã§Ã�Ã‰Ã�Ã“ÃšÃ€ÃˆÃŒÃ’Ã™ÃƒÃ•Ã‚ÃŠÃŽÃ”Ã›Ã„Ã‹Ã�Ã–ÃœÃ‡', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') ": "") +
				   (nrAno != null && !nrAno.trim().equals("") ? " AND EXTRACT(YEAR FROM E.dt_nascimento) = " + nrAno + " " : " ") + 
				   (tpNomePaiDeclarado > 0 ? " AND (E.nm_pai " + (tpNomePaiDeclarado == 1 ? " <> " : " = ") + " '' "+(tpNomePaiDeclarado == 1 ? " AND " : " OR ")+" E.nm_pai IS " + (tpNomePaiDeclarado == 1 ? " NOT " : " ") + " NULL) " : "") + 
				   (lgBuscaMaisEducacao ? " AND NOT EXISTS (SELECT SUB_MAT.cd_aluno FROM acd_matricula SUB_MAT, acd_turma SUB_T "
				   		+ "									 WHERE SUB_MAT.cd_aluno = W.cd_aluno "
				   		+ "									   AND SUB_MAT.cd_turma = SUB_T.cd_turma "
				   		+ "									   AND SUB_MAT.st_matricula = "+MatriculaServices.ST_ATIVA
				   		+ " 								   AND SUB_MAT.cd_periodo_letivo = "+cdPeriodoLetivo
				   		+ " 								   AND (SUB_MAT.cd_curso IN (1159, 1181, 1179, 1147, 1187, 1189, 1197, 1195, 1193, 1191, 1282, 1271) "
				   		+ "									   OR SUB_T.tp_turno = " + turma.getTpTurno()+")"
				   		+ "                                ) " : "");
			ResultSetMap rsm = Search.find(sql, "ORDER BY A.nm_pessoa "+sqlLimit[1], crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			ArrayList<Integer> codigosAluno = new ArrayList<Integer>();
			int x = 0;
			while(rsm.next()){
				//Caso o parametro esteja como true, busca os alunos mesmo que nÃ£o tenham matricula e indica a situaÃ§Ã£o dos mesmos (usado principalmente na busca ao se realizar uma matricula nova, em que Ã© necessÃ¡rio saber a atual situaÃ§Ã£o do cadastro do aluno, impedindo matriculas duplicadas)
				if(semMatriculaPeriodo){
					ResultSetMap rsmSemMatriculaPeriodo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula A, acd_instituicao_periodo B, grl_pessoa C "
							+ "													WHERE A.cd_periodo_letivo = B.cd_periodo_letivo "
							+ "													  AND A.cd_aluno = " + rsm.getInt("cd_aluno")
							+ "													  AND B.cd_instituicao = C.cd_pessoa "
							+ "													  AND B.nm_periodo_letivo = '" + InstituicaoPeriodoDAO.get(cdPeriodoLetivoSecretaria, connect).getNmPeriodoLetivo()+"'"
							+ " 												  AND (A.st_matricula = " + MatriculaServices.ST_ATIVA +" OR A.st_matricula = " + MatriculaServices.ST_PENDENTE +" OR A.st_matricula = " + MatriculaServices.ST_EM_TRANSFERENCIA +") ORDER BY dt_matricula DESC").executeQuery());
					
					if(rsmSemMatriculaPeriodo.next()){
						rsm.setValueToField("CL_POSSUI_MATRICULA", "Sim - SituaÃ§Ã£o da MatrÃ­cula: "+ MatriculaServices.situacao[rsmSemMatriculaPeriodo.getInt("st_matricula")]+" ("+rsmSemMatriculaPeriodo.getString("nm_pessoa")+")");
						rsm.setValueToField("NR_POSSUI_MATRICULA", 0);
					}
					else{
						rsm.setValueToField("CL_POSSUI_MATRICULA", "NÃ£o");
						rsm.setValueToField("NR_POSSUI_MATRICULA", 1);
					}
				}
				//Busca a ultima escola em que o aluno estudou (no sistema)
				if(lgBuscarMatriculaPeriodoAnterior){
					ResultSetMap rsmMatriculaPeriodoAnterior = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula A, acd_instituicao_periodo B, grl_pessoa C "
							+ "													WHERE A.cd_periodo_letivo = B.cd_periodo_letivo "
							+ "													  AND A.cd_aluno = " + rsm.getInt("cd_aluno")
							+ "													  AND B.cd_instituicao = C.cd_pessoa "
							+ "													  AND B.nm_periodo_letivo = '" + (Integer.parseInt(InstituicaoPeriodoDAO.get(cdPeriodoLetivoSecretaria, connect).getNmPeriodoLetivo())-1)+"'"
							+ " 												  AND (A.st_matricula = " + MatriculaServices.ST_CONCLUIDA+") ORDER BY dt_matricula DESC").executeQuery());
					if(rsmMatriculaPeriodoAnterior.next()){
						rsm.setValueToField("NM_ULTIMA_ESCOLA", rsmMatriculaPeriodoAnterior.getString("NM_PESSOA"));
					}
				}
				if(rsm.getString("nm_logradouro") != null && !rsm.getString("nm_logradouro").equals(""))
					rsm.setValueToField("CL_ENDERECO", rsm.getString("nm_logradouro") + ((rsm.getString("nr_endereco") != null && !rsm.getString("nr_endereco").equals("")) ? ", " + rsm.getString("nr_endereco") :"") + ((rsm.getString("nm_bairro") != null && !rsm.getString("nm_bairro").equals("")) ? ", " + rsm.getString("nm_bairro") :""));
				
				rsm.setValueToField("NR_IDADE", Util.getIdade(rsm.getGregorianCalendar("dt_nascimento")));
				
				
				if(lgSemMatriculaAee){
					ResultSetMap rsmMatriculaRecenteAee = new ResultSetMap(connect.prepareStatement("SELECT A.*, B.nm_periodo_letivo, C.nm_produto_servico AS nm_curso, "
							+ "																		(C.nm_produto_servico || ' - ' || D.nm_turma) AS nm_turma, "
							+ "																		E.cd_pessoa AS cd_instituicao, "
							+ "																		E.nm_pessoa AS nm_instituicao FROM acd_matricula A, acd_instituicao_periodo B, grl_produto_servico C, acd_turma D, grl_pessoa E "
							+ "																	  WHERE A.cd_periodo_letivo = B.cd_periodo_letivo "
							+ "																		AND A.cd_curso = C.cd_produto_servico "
							+ "																		AND A.cd_turma = D.cd_turma "
							+ "																		AND D.cd_instituicao = E.cd_pessoa "
							+ "																		AND D.tp_atendimento IN ("+TurmaServices.TP_ATENDIMENTO_AEE + (lgBuscaMaisEducacao ? "" : ", " + TurmaServices.TP_ATENDIMENTO_ATIVIDADE_COMPLEMENTAR)+")"
							+ "																		AND A.cd_aluno = " + rsm.getInt("cd_aluno") 
							+ "																		AND B.nm_periodo_letivo = '" + InstituicaoPeriodoDAO.get(cdPeriodoLetivoSecretaria, connect).getNmPeriodoLetivo()+"'" 
							+ " 																	AND (A.st_matricula IN (" + stMatricula +")) ORDER BY dt_matricula DESC").executeQuery());
					if(rsmMatriculaRecenteAee.next()){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				
				//Faz a busca e acrescenta os campos da matricula mais recente no result set map principal
				ResultSetMap rsmMatriculaRecente = new ResultSetMap(connect.prepareStatement("SELECT A.*, B.nm_periodo_letivo, C.nm_produto_servico AS nm_curso, "
						+ "																		(C.nm_produto_servico || ' - ' || D.nm_turma) AS nm_turma, D.nm_turma AS nm_turma_simples, D.tp_turno, "
						+ "																		E.cd_pessoa AS cd_instituicao, "
						+ "																		E.nm_pessoa AS nm_instituicao, F.cd_curso_etapa, CC.nr_ordem FROM acd_matricula A, acd_instituicao_periodo B, grl_produto_servico C, acd_curso CC, acd_turma D, grl_pessoa E, acd_curso_etapa F "
						+ "																	  WHERE A.cd_periodo_letivo = B.cd_periodo_letivo "
						+ "																		AND A.cd_curso = C.cd_produto_servico "
						+ "																		AND A.cd_curso = CC.cd_curso "
						+ "																		AND A.cd_turma = D.cd_turma "
						+ "																		AND D.cd_instituicao = E.cd_pessoa "
						+ "																		AND A.cd_curso = F.cd_curso "
						+ "																		AND D.tp_atendimento NOT IN ("+TurmaServices.TP_ATENDIMENTO_AEE + (lgBuscaMaisEducacao ? "" : ", " + TurmaServices.TP_ATENDIMENTO_ATIVIDADE_COMPLEMENTAR)+")"
						+ "																		AND A.cd_aluno = " + rsm.getInt("cd_aluno") 
						+ "																		AND B.nm_periodo_letivo = '" + InstituicaoPeriodoDAO.get(cdPeriodoLetivoSecretaria, connect).getNmPeriodoLetivo()+"'" 
						+ " 																	AND (A.st_matricula IN (" + stMatricula +")) ORDER BY dt_matricula DESC").executeQuery());
				if(rsmMatriculaRecente.next()){
					rsm.setValueToField("cd_matricula", rsmMatriculaRecente.getInt("cd_matricula"));
					if(rsmMatriculaRecente.getGregorianCalendar("dt_matricula") != null)
						rsm.setValueToField("dt_nm_matricula", Util.convCalendarString3(rsmMatriculaRecente.getGregorianCalendar("dt_matricula")));
					rsm.setValueToField("nr_matricula", rsmMatriculaRecente.getString("nr_matricula"));
					rsm.setValueToField("st_matricula", rsmMatriculaRecente.getInt("st_matricula"));
					rsm.setValueToField("nm_st_matricula", MatriculaServices.situacao[rsmMatriculaRecente.getInt("st_matricula")]);
					rsm.setValueToField("cd_curso", rsmMatriculaRecente.getInt("cd_curso"));
					rsm.setValueToField("nr_ordem", rsmMatriculaRecente.getInt("nr_ordem"));
					rsm.setValueToField("cd_curso_etapa", rsmMatriculaRecente.getInt("cd_curso_etapa"));
					rsm.setValueToField("nm_curso", rsmMatriculaRecente.getString("nm_curso"));
					rsm.setValueToField("cd_matriz", rsmMatriculaRecente.getInt("cd_matriz"));
					rsm.setValueToField("nm_tp_turno", TurmaServices.tiposTurno[rsmMatriculaRecente.getInt("tp_turno")]);
					rsm.setValueToField("cd_turma", rsmMatriculaRecente.getInt("cd_turma"));
					rsm.setValueToField("nm_turma", rsmMatriculaRecente.getString("nm_turma"));
					rsm.setValueToField("nm_turma_completo_turno", rsmMatriculaRecente.getString("nm_turma") + " ("+rsm.getString("nm_tp_turno")+")");
					rsm.setValueToField("nm_turma_simples", rsmMatriculaRecente.getString("nm_turma_simples"));
					rsm.setValueToField("cd_instituicao", rsmMatriculaRecente.getInt("cd_instituicao"));
					rsm.setValueToField("nm_instituicao", rsmMatriculaRecente.getString("nm_instituicao"));
					rsm.setValueToField("cd_periodo_letivo", rsmMatriculaRecente.getInt("cd_periodo_letivo"));
					rsm.setValueToField("nm_periodo_letivo", rsmMatriculaRecente.getString("nm_periodo_letivo"));
					rsm.setValueToField("dt_matricula", rsmMatriculaRecente.getGregorianCalendar("dt_matricula"));
					rsm.setValueToField("dt_conclusao", rsmMatriculaRecente.getGregorianCalendar("dt_conclusao"));
					rsm.setValueToField("tp_matricula", rsmMatriculaRecente.getInt("tp_matricula"));
					rsm.setValueToField("cd_aluno", rsmMatriculaRecente.getInt("cd_aluno"));
					rsm.setValueToField("cd_matricula_origem", rsmMatriculaRecente.getInt("cd_matricula_origem"));
					rsm.setValueToField("cd_reserva", rsmMatriculaRecente.getInt("cd_reserva"));
					rsm.setValueToField("cd_area_interesse", rsmMatriculaRecente.getInt("cd_area_interesse"));
					rsm.setValueToField("txt_observacao", rsmMatriculaRecente.getString("txt_observacao"));
					rsm.setValueToField("txt_boletim", rsmMatriculaRecente.getString("txt_boletim"));
					rsm.setValueToField("cd_pre_matricula", rsmMatriculaRecente.getInt("cd_pre_matricula"));
					rsm.setValueToField("tp_escolarizacao_outro_espaco", rsmMatriculaRecente.getInt("tp_escolarizacao_outro_espaco"));
					rsm.setValueToField("lg_transporte_publico", rsmMatriculaRecente.getInt("lg_transporte_publico"));
					rsm.setValueToField("tp_poder_responsavel", rsmMatriculaRecente.getInt("tp_poder_responsavel"));
					rsm.setValueToField("tp_forma_ingresso", rsmMatriculaRecente.getInt("tp_forma_ingresso"));
					rsm.setValueToField("txt_documento_oficial", rsmMatriculaRecente.getString("txt_documento_oficial"));
					rsm.setValueToField("lg_autorizacao_rematricula", rsmMatriculaRecente.getInt("lg_autorizacao_rematricula"));
					rsm.setValueToField("lg_atividade_complementar", rsmMatriculaRecente.getInt("lg_atividade_complementar"));
					rsm.setValueToField("dt_interrupcao", rsmMatriculaRecente.getGregorianCalendar("dt_interrupcao"));
					rsm.setValueToField("lg_reprovacao", rsmMatriculaRecente.getInt("lg_reprovacao"));
					rsm.setValueToField("st_matricula_centaurus", rsmMatriculaRecente.getInt("st_matricula_centaurus"));
					rsm.setValueToField("nm_ultima_escola", rsmMatriculaRecente.getString("nm_ultima_escola"));
					
					//Faz a busca do principal transporte usado pelo aluno (caso ele tenha mais de um)
					ResultSetMap rsmTipoTransporte = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula_tipo_transporte A, fta_tipo_transporte B WHERE A.cd_tipo_transporte = B.cd_tipo_transporte AND A.cd_matricula = " + rsmMatriculaRecente.getInt("cd_matricula")).executeQuery());
					if(rsmTipoTransporte.next()){
						rsm.setValueToField("cd_tipo_transporte", rsmTipoTransporte.getInt("cd_tipo_transporte"));
					}
					
					//Utilizado para saber o curso anterior do curso atual da matricula, seguindo o que esta considerado na etapa
					ResultSetMap rsmCursoAnterior = new ResultSetMap(connect.prepareStatement("SELECT A.cd_curso, C.nm_produto_servico AS nm_curso FROM acd_curso A, acd_curso_etapa B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND A.cd_curso = C.cd_produto_servico AND B.cd_curso_etapa_posterior = " + rsm.getInt("cd_curso_etapa")).executeQuery());
					if(rsmCursoAnterior.next()){
						rsm.setValueToField("CD_CURSO_ANTERIOR", rsmCursoAnterior.getInt("cd_curso"));
						rsm.setValueToField("NM_CURSO_ANTERIOR", rsmCursoAnterior.getString("nm_curso"));
					}
					Curso cursoMatricula = CursoDAO.get(rsm.getInt("CD_CURSO"), connect);
					if(rsm.getInt("CD_CURSO_ANTERIOR") == 0){
						ResultSetMap rsmCursoEquivalente = new ResultSetMap(connect.prepareStatement("SELECT A.cd_curso, B.cd_curso_etapa FROM acd_curso A, acd_curso_etapa B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND A.cd_curso = C.cd_produto_servico AND C.id_produto_servico = '" + cursoMatricula.getIdProdutoServico() + "' AND A.lg_referencia = 0").executeQuery());
						if(rsmCursoEquivalente.next()){
							rsmCursoAnterior = new ResultSetMap(connect.prepareStatement("SELECT A.cd_curso, C.nm_produto_servico AS nm_curso FROM acd_curso A, acd_curso_etapa B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND A.cd_curso = C.cd_produto_servico AND B.cd_curso_etapa_posterior = " + rsmCursoEquivalente.getInt("cd_curso_etapa")).executeQuery());
							if(rsmCursoAnterior.next()){
								rsm.setValueToField("CD_CURSO_ANTERIOR", rsmCursoAnterior.getInt("cd_curso"));
								rsm.setValueToField("NM_CURSO_ANTERIOR", rsmCursoAnterior.getString("nm_curso"));
							}
						}
					}
					
					//Usado para saber se pode ser aplicado o cancelamento de recebimento nesse aluno
					Matricula matricula = MatriculaDAO.get(rsmMatriculaRecente.getInt("cd_matricula"), connect);
					Matricula matriculaOrigem = MatriculaDAO.get(matricula.getCdMatriculaOrigem(), connect);
					if(matriculaOrigem != null){
						int cdTipoOcorrenciaTransferencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CONCLUSAO_TRANSFERENCIA, connect).getCdTipoOcorrencia();
						
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + cdTipoOcorrenciaTransferencia, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_matricula_origem", "" + matriculaOrigem.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_matricula_destino", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmOcorrencia = OcorrenciaMatriculaServices.find(criterios, connect);
						if(rsmOcorrencia.next() && matriculaOrigem.getStMatricula() == MatriculaServices.ST_TRANSFERIDO && matricula.getStMatricula() == MatriculaServices.ST_ATIVA){
							rsm.setValueToField("LG_PERMITE_CANCELAMENTO_RECEBIMENTO", 1);
						}
						else{
							rsm.setValueToField("LG_PERMITE_CANCELAMENTO_RECEBIMENTO", 0);
						}
					}
					
				}
				else{
					//Caso a variavel tenha sido passada como false, o metodo deletarÃ¡ o registro dos alunos sem matrÃ­cula no periodo
					if(lgMatriculaPeriodo){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
					
				}
				
				if(cdCursoAtual > 0 && cdCursoAtual != rsm.getInt("cd_curso")){
					rsm.deleteRow();
					if(x == 0)
						rsm.beforeFirst();
					else
						rsm.previous();
					continue;
				}
				
				//FormataÃ§Ã£o de datas
				GregorianCalendar dtNascimento = rsm.getGregorianCalendar("dt_nascimento");
				if(dtNascimento != null)
					rsm.setValueToField("DT_NM_NASCIMENTO", (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.DAY_OF_MONTH))) > 9 ? dtNascimento.get(Calendar.DAY_OF_MONTH) : "0" + dtNascimento.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1)) + "/" + dtNascimento.get(Calendar.YEAR));
				
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
				
				
				
				Cidade naturalidade = CidadeDAO.get(rsm.getInt("cd_naturalidade"), connect);
				if(naturalidade != null){
					rsm.setValueToField("NM_NATURALIDADE", naturalidade.getNmCidade());
					Estado estadoNaturalidade = EstadoDAO.get(naturalidade.getCdEstado(), connect);
					if(estadoNaturalidade != null)
						rsm.setValueToField("SG_NATURALIDADE", estadoNaturalidade.getSgEstado());
				}
				
				//Busca o numero de telefone principal do aluno
				rsm.setValueToField("NR_TELEFONE", (rsm.getString("NR_TELEFONE1") != null && !rsm.getString("NR_TELEFONE1").trim().equals("") ? rsm.getString("NR_TELEFONE1") : (rsm.getString("NR_TELEFONE2") != null && !rsm.getString("NR_TELEFONE2").trim().equals("") ? rsm.getString("NR_TELEFONE2") : (rsm.getString("NR_CELULAR") != null && !rsm.getString("NR_CELULAR").trim().equals("") ? rsm.getString("NR_CELULAR") : rsm.getString("NR_TELEFONE")))));
				
				//Busca de deficiencia
				ResultSetMap rsmPessoaNecessidadeEspecial = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_pessoa_necessidade_especial A, grl_tipo_necessidade_especial B WHERE A.cd_tipo_necessidade_especial = B.cd_tipo_necessidade_especial AND cd_pessoa = " + rsm.getInt("cd_aluno")).executeQuery());
				if(rsmPessoaNecessidadeEspecial.next()){
					rsm.setValueToField("TP_POSSUI_DEFICIENCIA", 1);
					
					rsmPessoaNecessidadeEspecial.beforeFirst();
					String clDeficiencia = "";
					while(rsmPessoaNecessidadeEspecial.next()){
						clDeficiencia += rsmPessoaNecessidadeEspecial.getString("nm_tipo_necessidade_especial") + ", ";
					}
					rsmPessoaNecessidadeEspecial.beforeFirst();
					
					if(clDeficiencia.length() > 0){
						clDeficiencia = clDeficiencia.substring(0, clDeficiencia.length()-2);
					}
					
					rsm.setValueToField("CL_DEFICIENCIA", clDeficiencia);
				}
				else{
					rsm.setValueToField("TP_POSSUI_DEFICIENCIA", 2);
					rsm.setValueToField("CL_DEFICIENCIA", "");
				}
				
				//Retira o registro caso o aluno esteja matriculado em outra instituiÃ§Ã£o
				if(cdSecretaria != cdInstituicao && cdInstituicao > 0 && cdPeriodoLetivoSecretaria > 0 && !permitirVisualizarAluno){
					ResultSetMap rsmMatriculaOutraInstituicao = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula A, acd_instituicao_periodo B, acd_turma C "
							+ "																				WHERE A.cd_periodo_letivo = B.cd_periodo_letivo "
							+ "																				  AND B.nm_periodo_letivo = '" + InstituicaoPeriodoDAO.get(cdPeriodoLetivoSecretaria, connect).getNmPeriodoLetivo()+"'"
							+ "																				  AND B.cd_instituicao <> " + cdInstituicao + " "
							+ "																				  AND A.cd_aluno = " + rsm.getInt("cd_aluno")
							+ "																				  AND A.cd_turma = C.cd_turma "
							+ "																				  AND C.tp_atendimento NOT IN ("+TurmaServices.TP_ATENDIMENTO_AEE + (lgBuscaMaisEducacao ? "" : ", " + TurmaServices.TP_ATENDIMENTO_ATIVIDADE_COMPLEMENTAR)+")"
							+ "																				  AND A.st_matricula IN("+MatriculaServices.ST_ATIVA+", "+MatriculaServices.ST_PENDENTE+")").executeQuery());
					if(rsmMatriculaOutraInstituicao.next()){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				
				
				/**
				 * Registro dos documentos do aluno
				 */
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", "" + rsm.getInt("cd_aluno") , ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tipo_documentacao", "" + TipoDocumentacaoServices.getById(TipoDocumentacaoServices.TP_NIS).getCdTipoDocumentacao() , ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPessoaTipoDocumentacaoNis = PessoaTipoDocumentacaoDAO.find(criterios, connect);
				if(rsmPessoaTipoDocumentacaoNis.next()){
					rsm.setValueToField("NR_NIS", rsmPessoaTipoDocumentacaoNis.getString("nr_documento"));
				}
				else{
					rsm.setValueToField("NR_NIS", "");
				}
					
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
				
				//Retira alunos que vierem duplicados da consulta
				if(codigosAluno.size() > 0 && codigosAluno.contains(rsm.getInt("cd_pessoa"))){
					rsm.deleteRow();
					if(x == 0)
						rsm.beforeFirst();
					else
						rsm.previous();
					continue;
				}
				else{
					codigosAluno.add(rsm.getInt("cd_pessoa"));
				}
				
				x++;
			}
			rsm.beforeFirst();
			
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
	
	/**
	 * Metodo usado para buscar alunos de forma mais rÃ¡pida
	 * Usado pela solicitaÃ§Ã£o de transferÃªncia, pela consulta de alunos e pela matricula completa no angular
	 * @param criterios
	 * @return
	 */
	public static ResultSetMap findSimplificado(ArrayList<ItemComparator> criterios) {
		return findSimplificado(criterios, null);
	}

	public static ResultSetMap findSimplificado(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int qtLimite = 0;
			String nmPessoa = "";
			int cdInstituicao = 0;
			String nrAno = "";
			String nmPeriodoLetivo = "";
			int cdPeriodoLetivo = 0;
			int cdPeriodoLetivoSecretaria = 0;
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			String nrMatricula = null;
			boolean lgMatriculaPeriodo = false;//Variavel usada para filtrar apenas alunos com matricula no periodo atual
			boolean semMatriculaPeriodo = false;//Variavel usada para incluir alunos sem matricula no periodo atual
			boolean lgComDeficiencia = false;//Variavel usada para incluir apenas alunos com deficiencia
			boolean lgSemMatriculaAee = false;//Variavel usada para filtrar apenas alunos sem matricula em turmas de Atendimento Especializado
			boolean lgIncluirPendente = false;//Variavel usada pra incluir alunos com matricula pendente
			boolean lgBuscarMatriculaPeriodoAnterior = false;//Variavel usada para buscar informaÃ§Ãµes da matricula no periodo anterior ao atual
			String stMatricula = "" + MatriculaServices.ST_ATIVA;
			
			//Usado para verificar o periodo mais recente, para a busca da matricula regular do aluno, e se ele tem ou nÃ£o matricula regular no periodo
			ResultSetMap rsmPeriodoSecretaria = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdSecretaria, connect);
			if(rsmPeriodoSecretaria.next())
				cdPeriodoLetivoSecretaria = rsmPeriodoSecretaria.getInt("cd_periodo_letivo");
			
			
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.nm_pessoa")) {
					nmPessoa = Util.limparTexto(criterios.get(i).getValue());
					nmPessoa = "%"+nmPessoa.replaceAll(" ", "%")+"%";
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_INSTITUICAO")) {
					cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
					if(cdPeriodoLetivo == 0){
						ResultSetMap rsm = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdInstituicao, connect);
						if(rsm.next()){
							cdPeriodoLetivo = rsm.getInt("cd_periodo_letivo");
						}
					}
					
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nmPeriodoLetivo")) {
					nmPeriodoLetivo = criterios.get(i).getValue();
					ArrayList<ItemComparator> criteriosPeriodoLetivo = new ArrayList<ItemComparator>();
					criteriosPeriodoLetivo.add(new ItemComparator("nm_periodo_letivo", nmPeriodoLetivo, ItemComparator.EQUAL, Types.VARCHAR));
					criteriosPeriodoLetivo.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmInstituicaoPeriodo = InstituicaoPeriodoDAO.find(criteriosPeriodoLetivo, connect);
					InstituicaoPeriodo instituicaoPeriodo = null;
					if(rsmInstituicaoPeriodo.next()){
						instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmInstituicaoPeriodo.getInt("cd_periodo_letivo"), connect);
						cdPeriodoLetivo = instituicaoPeriodo.getCdPeriodoLetivo();
					}
					
					cdPeriodoLetivoSecretaria = (instituicaoPeriodo.getCdPeriodoLetivoSuperior() == 0 ? instituicaoPeriodo.getCdPeriodoLetivo() : instituicaoPeriodo.getCdPeriodoLetivoSuperior());
					
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrAno")) {
					nrAno = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrMatricula")) {
					nrMatricula = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgMatriculaPeriodo")) {
					lgMatriculaPeriodo = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgComDeficiencia")) {
					lgComDeficiencia = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgSemMatriculaAee")) {
					lgSemMatriculaAee = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("semMatriculaPeriodo")) {
					semMatriculaPeriodo = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgIncluirPendente")) {
					lgIncluirPendente = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgBuscarMatriculaPeriodoAnterior")) {
					lgBuscarMatriculaPeriodoAnterior = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("stMatricula")) {
					stMatricula = criterios.get(i).getValue();
				}
				else
					crt.add(criterios.get(i));
				
			}
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
			String sql =
		 			  "SELECT "+sqlLimit[0]+" A.cd_pessoa, A.cd_pessoa AS cd_aluno, A.nm_pessoa, E.nm_mae, E.dt_nascimento, PTD.nr_documento " +
 					  "FROM grl_pessoa A " +
 					  "JOIN acd_aluno 				W ON (A.cd_pessoa = W.cd_aluno) " +
 					  "LEFT OUTER JOIN grl_pessoa_fisica 	E ON (A.cd_pessoa = E.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_pessoa_empresa 	F ON (A.cd_pessoa = F.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_pessoa_tipo_documentacao PTD ON (A.cd_pessoa = PTD.cd_pessoa "
 					  + "												AND (PTD.cd_tipo_documentacao = "+ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0)+" OR PTD.cd_tipo_documentacao = "+ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)+")) " +
 					  (semMatriculaPeriodo ? " LEFT OUTER JOIN acd_matricula SUB_MAT ON (SUB_MAT.cd_aluno = A.cd_pessoa AND SUB_MAT.st_matricula IN ("+stMatricula+ (lgIncluirPendente ? ", " + MatriculaServices.ST_PENDENTE : "")+") AND SUB_MAT.cd_periodo_letivo = "+cdPeriodoLetivo+ (nrMatricula != null ? " AND SUB_MAT.nr_matricula = '"+nrMatricula+"'" : "")+")" : "") +
 					  "WHERE 1 = 1 " +
 					 ((!semMatriculaPeriodo || nrMatricula != null) && cdInstituicao != cdSecretaria ? " AND EXISTS (SELECT SUB_MAT.cd_aluno FROM acd_matricula SUB_MAT WHERE SUB_MAT.cd_aluno = A.cd_pessoa "  + (cdPeriodoLetivo > 0 ?  "AND SUB_MAT.cd_periodo_letivo = "+cdPeriodoLetivo : "") + (nrMatricula != null ? " AND SUB_MAT.nr_matricula = '"+nrMatricula+"'" : "") + (!stMatricula.equals("-1") ? " AND SUB_MAT.st_matricula IN ("+stMatricula+ (lgIncluirPendente ? ", " + MatriculaServices.ST_PENDENTE : "") +") " : "" ) +") " : "") +
 				   (!semMatriculaPeriodo && cdInstituicao == cdSecretaria && nrMatricula != null ? " AND EXISTS (SELECT SUB_MAT.cd_aluno FROM acd_matricula SUB_MAT WHERE SUB_MAT.cd_aluno = A.cd_pessoa AND SUB_MAT.nr_matricula = '"+nrMatricula+"'"+") " : "") +
 				   (lgComDeficiencia ? " AND EXISTS (SELECT * FROM grl_pessoa_necessidade_especial PNE WHERE PNE.cd_pessoa = A.cd_pessoa)" : "") +
				   (!nmPessoa.equals("") ? 
				   "	  AND TRANSLATE(A.nm_pessoa, 'Ã¡Ã©Ã­Ã³ÃºÃ Ã¨Ã¬Ã²Ã¹Ã£ÃµÃ¢ÃªÃ®Ã´Ã´Ã¤Ã«Ã¯Ã¶Ã¼Ã§Ã�Ã‰Ã�Ã“ÃšÃ€ÃˆÃŒÃ’Ã™ÃƒÃ•Ã‚ÃŠÃŽÃ”Ã›Ã„Ã‹Ã�Ã–ÃœÃ‡', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE TRANSLATE('%"+nmPessoa+"%', 'Ã¡Ã©Ã­Ã³ÃºÃ Ã¨Ã¬Ã²Ã¹Ã£ÃµÃ¢ÃªÃ®Ã´Ã´Ã¤Ã«Ã¯Ã¶Ã¼Ã§Ã�Ã‰Ã�Ã“ÃšÃ€ÃˆÃŒÃ’Ã™ÃƒÃ•Ã‚ÃŠÃŽÃ”Ã›Ã„Ã‹Ã�Ã–ÃœÃ‡', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') ": "") +
				   (nrAno != null && !nrAno.trim().equals("") ? " AND EXTRACT(YEAR FROM E.dt_nascimento) = " + nrAno + " " : " ");
			ResultSetMap rsm = Search.find(sql, "ORDER BY A.nm_pessoa "+sqlLimit[1], crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			ArrayList<Integer> codigosAluno = new ArrayList<Integer>();
			int x = 0;
			while(rsm.next()){
				//Usado pela matricula completa para saber se o aluno jÃ¡ possui matricula
				if(semMatriculaPeriodo){
					ResultSetMap rsmSemMatriculaPeriodo = new ResultSetMap(connect.prepareStatement("SELECT A.cd_matricula, A.st_matricula, A.nr_matricula, C.nm_pessoa FROM acd_matricula A"
							+ "													JOIN acd_instituicao_periodo B ON (A.cd_periodo_letivo = B.cd_periodo_letivo)"
							+ "													JOIN grl_pessoa C ON (B.cd_instituicao = C.cd_pessoa)"
							+ "													WHERE A.cd_aluno = " + rsm.getInt("cd_aluno")
							+ "													  AND B.nm_periodo_letivo = '" + InstituicaoPeriodoDAO.get(cdPeriodoLetivoSecretaria, connect).getNmPeriodoLetivo()+"'"
							+ " 												  AND (A.st_matricula = " + MatriculaServices.ST_ATIVA +" OR A.st_matricula = " + MatriculaServices.ST_PENDENTE +" OR A.st_matricula = " + MatriculaServices.ST_EM_TRANSFERENCIA +") ORDER BY dt_matricula DESC").executeQuery());
					
					if(rsmSemMatriculaPeriodo.next()){
						rsm.setValueToField("CL_POSSUI_MATRICULA", "Sim - SituaÃ§Ã£o da MatrÃ­cula: "+ MatriculaServices.situacao[rsmSemMatriculaPeriodo.getInt("st_matricula")]+" ("+rsmSemMatriculaPeriodo.getString("nm_pessoa")+")");
						rsm.setValueToField("NR_POSSUI_MATRICULA", 0);
					}
					else{
						rsm.setValueToField("CL_POSSUI_MATRICULA", "NÃ£o");
						rsm.setValueToField("NR_POSSUI_MATRICULA", 1);
					}
				}
				
				GregorianCalendar dtNascimento = rsm.getGregorianCalendar("dt_nascimento");
				if(dtNascimento != null)
					rsm.setValueToField("DT_NM_NASCIMENTO", (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.DAY_OF_MONTH))) > 9 ? dtNascimento.get(Calendar.DAY_OF_MONTH) : "0" + dtNascimento.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1)) + "/" + dtNascimento.get(Calendar.YEAR));
				
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
				
				//Faz a busca d matricula regular mais recente do aluno
				ResultSetMap rsmMatriculaRecente = new ResultSetMap(connect.prepareStatement("SELECT A.*, B.nm_periodo_letivo, C.nm_produto_servico AS nm_curso, D.nm_turma AS nm_turma_simples, "
						+ "																		(C.nm_produto_servico || ' - ' || D.nm_turma) AS nm_turma, "
						+ "																		E.cd_pessoa AS cd_instituicao, "
						+ "																		E.nm_pessoa AS nm_instituicao, F.cd_curso_etapa FROM acd_matricula A, acd_instituicao_periodo B, grl_produto_servico C, acd_turma D, grl_pessoa E, acd_curso_etapa F "
						+ "																	  WHERE A.cd_periodo_letivo = B.cd_periodo_letivo "
						+ "																		AND A.cd_curso = C.cd_produto_servico "
						+ "																		AND A.cd_turma = D.cd_turma "
						+ "																		AND D.cd_instituicao = E.cd_pessoa "
						+ "																		AND A.cd_curso = F.cd_curso "
						+ "																		AND D.tp_atendimento NOT IN ("+TurmaServices.TP_ATENDIMENTO_AEE+", "+TurmaServices.TP_ATENDIMENTO_ATIVIDADE_COMPLEMENTAR+")"
						+ "																		AND A.cd_aluno = " + rsm.getInt("cd_aluno") 
						+ "																		AND B.nm_periodo_letivo = '" + InstituicaoPeriodoDAO.get(cdPeriodoLetivoSecretaria, connect).getNmPeriodoLetivo()+"'"
						+ "																		" + (cdPeriodoLetivo > 0 && cdPeriodoLetivo != cdPeriodoLetivoSecretaria ? " AND B.cd_periodo_letivo = " + cdPeriodoLetivo : "")
						+ " 																	" + (stMatricula != null ? "AND (A.st_matricula IN (" + stMatricula +"))" : "") + " ORDER BY dt_matricula DESC").executeQuery());
				
				//Incluir matricula mais recente do aluno no ResultSetMap principal
				if(rsmMatriculaRecente.next()){
					rsm.setValueToField("cd_matricula", rsmMatriculaRecente.getInt("cd_matricula"));
					if(rsmMatriculaRecente.getGregorianCalendar("dt_matricula") != null)
						rsm.setValueToField("dt_nm_matricula", Util.convCalendarString3(rsmMatriculaRecente.getGregorianCalendar("dt_matricula")));
					rsm.setValueToField("nr_matricula", rsmMatriculaRecente.getString("nr_matricula"));
					rsm.setValueToField("st_matricula", rsmMatriculaRecente.getInt("st_matricula"));
					rsm.setValueToField("nm_st_matricula", MatriculaServices.situacao[rsmMatriculaRecente.getInt("st_matricula")]);
					rsm.setValueToField("cd_curso", rsmMatriculaRecente.getInt("cd_curso"));
					rsm.setValueToField("cd_curso_etapa", rsmMatriculaRecente.getInt("cd_curso_etapa"));
					rsm.setValueToField("nm_curso", rsmMatriculaRecente.getString("nm_curso"));
					rsm.setValueToField("cd_matriz", rsmMatriculaRecente.getInt("cd_matriz"));
					rsm.setValueToField("cd_turma", rsmMatriculaRecente.getInt("cd_turma"));
					rsm.setValueToField("nm_turma", rsmMatriculaRecente.getString("nm_turma"));
					rsm.setValueToField("nm_turma_completa", rsmMatriculaRecente.getString("nm_turma"));
					rsm.setValueToField("nm_turma_simples", rsmMatriculaRecente.getString("nm_turma_simples"));
					rsm.setValueToField("cd_instituicao", rsmMatriculaRecente.getInt("cd_instituicao"));
					rsm.setValueToField("nm_instituicao", rsmMatriculaRecente.getString("nm_instituicao"));
					rsm.setValueToField("cd_periodo_letivo", rsmMatriculaRecente.getInt("cd_periodo_letivo"));
					rsm.setValueToField("nm_periodo_letivo", rsmMatriculaRecente.getString("nm_periodo_letivo"));
					rsm.setValueToField("dt_matricula", rsmMatriculaRecente.getGregorianCalendar("dt_matricula"));
					rsm.setValueToField("dt_conclusao", rsmMatriculaRecente.getGregorianCalendar("dt_conclusao"));
					rsm.setValueToField("tp_matricula", rsmMatriculaRecente.getInt("tp_matricula"));
					rsm.setValueToField("cd_aluno", rsmMatriculaRecente.getInt("cd_aluno"));
					rsm.setValueToField("cd_matricula_origem", rsmMatriculaRecente.getInt("cd_matricula_origem"));
					rsm.setValueToField("cd_reserva", rsmMatriculaRecente.getInt("cd_reserva"));
					rsm.setValueToField("cd_area_interesse", rsmMatriculaRecente.getInt("cd_area_interesse"));
					rsm.setValueToField("txt_observacao", rsmMatriculaRecente.getString("txt_observacao"));
					rsm.setValueToField("txt_boletim", rsmMatriculaRecente.getString("txt_boletim"));
					rsm.setValueToField("cd_pre_matricula", rsmMatriculaRecente.getInt("cd_pre_matricula"));
					rsm.setValueToField("tp_escolarizacao_outro_espaco", rsmMatriculaRecente.getInt("tp_escolarizacao_outro_espaco"));
					rsm.setValueToField("lg_transporte_publico", rsmMatriculaRecente.getInt("lg_transporte_publico"));
					rsm.setValueToField("tp_poder_responsavel", rsmMatriculaRecente.getInt("tp_poder_responsavel"));
					rsm.setValueToField("tp_forma_ingresso", rsmMatriculaRecente.getInt("tp_forma_ingresso"));
					rsm.setValueToField("txt_documento_oficial", rsmMatriculaRecente.getString("txt_documento_oficial"));
					rsm.setValueToField("lg_autorizacao_rematricula", rsmMatriculaRecente.getInt("lg_autorizacao_rematricula"));
					rsm.setValueToField("lg_atividade_complementar", rsmMatriculaRecente.getInt("lg_atividade_complementar"));
					rsm.setValueToField("dt_interrupcao", rsmMatriculaRecente.getGregorianCalendar("dt_interrupcao"));
					rsm.setValueToField("lg_reprovacao", rsmMatriculaRecente.getInt("lg_reprovacao"));
					rsm.setValueToField("st_matricula_centaurus", rsmMatriculaRecente.getInt("st_matricula_centaurus"));
					rsm.setValueToField("nm_ultima_escola", rsmMatriculaRecente.getString("nm_ultima_escola"));
					
					ResultSetMap rsmTipoTransporte = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula_tipo_transporte A, fta_tipo_transporte B WHERE A.cd_tipo_transporte = B.cd_tipo_transporte AND A.cd_matricula = " + rsmMatriculaRecente.getInt("cd_matricula")).executeQuery());
					if(rsmTipoTransporte.next()){
						rsm.setValueToField("cd_tipo_transporte", rsmTipoTransporte.getInt("cd_tipo_transporte"));
					}
					
					//Utilizado para saber o curso anterior do curso atual da matricula, seguindo o que esta considerado na etapa
					ResultSetMap rsmCursoAnterior = new ResultSetMap(connect.prepareStatement("SELECT A.cd_curso, C.nm_produto_servico AS nm_curso FROM acd_curso A, acd_curso_etapa B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND A.cd_curso = C.cd_produto_servico AND B.cd_curso_etapa_posterior = " + rsm.getInt("cd_curso_etapa")).executeQuery());
					if(rsmCursoAnterior.next()){
						rsm.setValueToField("CD_CURSO_ANTERIOR", rsmCursoAnterior.getInt("cd_curso"));
						rsm.setValueToField("NM_CURSO_ANTERIOR", rsmCursoAnterior.getString("nm_curso"));
					}
					Curso cursoMatricula = CursoDAO.get(rsm.getInt("CD_CURSO"), connect);
					if(rsm.getInt("CD_CURSO_ANTERIOR") == 0){
						ResultSetMap rsmCursoEquivalente = new ResultSetMap(connect.prepareStatement("SELECT A.cd_curso, B.cd_curso_etapa FROM acd_curso A, acd_curso_etapa B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND A.cd_curso = C.cd_produto_servico AND C.id_produto_servico = '" + cursoMatricula.getIdProdutoServico() + "' AND A.lg_referencia = 0").executeQuery());
						if(rsmCursoEquivalente.next()){
							rsmCursoAnterior = new ResultSetMap(connect.prepareStatement("SELECT A.cd_curso, C.nm_produto_servico AS nm_curso FROM acd_curso A, acd_curso_etapa B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND A.cd_curso = C.cd_produto_servico AND B.cd_curso_etapa_posterior = " + rsmCursoEquivalente.getInt("cd_curso_etapa")).executeQuery());
							if(rsmCursoAnterior.next()){
								rsm.setValueToField("CD_CURSO_ANTERIOR", rsmCursoAnterior.getInt("cd_curso"));
								rsm.setValueToField("NM_CURSO_ANTERIOR", rsmCursoAnterior.getString("nm_curso"));
							}
						}
					}
					
				}
				else{
					if(nrMatricula != null)
						rsm.setValueToField("nr_matricula", nrMatricula);
				}
				
				if(codigosAluno.size() > 0 && codigosAluno.contains(rsm.getInt("cd_pessoa"))){
					rsm.deleteRow();
					if(x == 0)
						rsm.beforeFirst();
					else
						rsm.previous();
					continue;
				}
				else{
					codigosAluno.add(rsm.getInt("cd_pessoa"));
				}
				
				x++;
			}
			rsm.beforeFirst();
			
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
	
	
	public static ResultSetMap findCatraca(ArrayList<ItemComparator> criterios) {
		return findCatraca(criterios, null);
	}

	public static ResultSetMap findCatraca(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int cdPeriodoLetivoSecretaria = 0;
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			ResultSetMap rsmPeriodoSecretaria = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdSecretaria, connect);
			if(rsmPeriodoSecretaria.next())
				cdPeriodoLetivoSecretaria = rsmPeriodoSecretaria.getInt("cd_periodo_letivo");
			
			String sql =
		 			  "SELECT F.nr_matricula, B.nm_pessoa AS NM_ALUNO, C.nm_mae, C.nm_pai, C.dt_nascimento, C.nr_cpf, C.nr_rg, C.dt_emissao_rg, E.nr_documento, E.livro, E.folha, E.dt_emissao, E2.nm_pessoa AS NM_CARTORIO, D.nr_cep, D.nm_bairro, D.nm_logradouro, D.nr_endereco, M.nm_pessoa AS NM_INSTITUICAO, (K.nm_produto_servico || ' - ' || I.nm_turma) AS NM_TURMA_COMPLETA, H.nm_produto_servico AS CURSO_MATRICULA FROM acd_aluno A" +
					"	 JOIN grl_pessoa B ON (A.cd_aluno = B.cd_pessoa)" +
					"	 JOIN grl_pessoa_fisica C ON (A.cd_aluno = C.cd_pessoa)" +
					"	 JOIN grl_pessoa_endereco D ON (A.cd_aluno = D.cd_pessoa AND lg_principal = 1)" +
					"	 LEFT OUTER JOIN grl_pessoa_tipo_documentacao E ON (A.cd_aluno = E.cd_pessoa AND E.cd_tipo_documentacao IN (4, 5))" +
					"	 LEFT OUTER JOIN grl_pessoa E2 ON (E.cd_cartorio = E2.cd_pessoa)" +
					"	 JOIN acd_matricula F ON (A.cd_aluno = F.cd_aluno)" +
					"	 JOIN acd_curso G ON (F.cd_curso = G.cd_curso)" +
					"	 JOIN grl_produto_servico H ON (F.cd_curso = H.cd_produto_servico)" +
					"	 JOIN acd_turma I ON (F.cd_turma = I.cd_turma)" +
					"	 JOIN acd_curso J ON (I.cd_curso = J.cd_curso)" +
					"	 JOIN grl_produto_servico K ON (I.cd_curso = K.cd_produto_servico)" +
					"	 JOIN acd_instituicao L ON (I.cd_instituicao = L.cd_instituicao)" +
					"	 JOIN grl_pessoa M ON (I.cd_instituicao = M.cd_pessoa)" +
					"	WHERE F.cd_periodo_letivo > " + cdPeriodoLetivoSecretaria +
					"	  AND F.st_matricula = " + MatriculaServices.ST_ATIVA +
					"	  AND J.cd_curso NOT IN ("+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)+") ";
			ResultSetMap rsm = Search.find(sql, "ORDER BY B.nm_pessoa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
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
	
	/**
	 * Busca matricula atual do aluno
	 * @param criterios
	 * @param connect
	 * @return
	 */
	public static Matricula findMatriculaAluno(int cdAluno) {
		return findMatriculaAluno(cdAluno);
	}

	public static Matricula findMatriculaAluno(int cdAluno, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
	
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_aluno", "" + cdAluno, ItemComparator.EQUAL, Types.INTEGER));
			
			Matricula matricula = null;
			ResultSetMap rsmAlunoMatricula= MatriculaServices.find(criterios, connect);
			if(rsmAlunoMatricula.next()) {
				matricula = MatriculaDAO.get(rsmAlunoMatricula.getInt("cd_matricula"));
			}
			
			
			return matricula;
		}
		catch(Exception e) {		
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		
	}
	

	
	/**
	 * Busca dados atuais do aluno
	 * @param criterios
	 * @param connect
	 * @return
	 */
	
	public static Aluno findAlunoMobile(int cdAluno, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
	
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_aluno", "" + cdAluno, ItemComparator.EQUAL, Types.INTEGER));
			
			Aluno aluno = null;
			ResultSetMap rsmAlunoMobile = AlunoServices.find(criterios, connect);
			if(rsmAlunoMobile.next()) {
				aluno = AlunoDAO.get(rsmAlunoMobile.getInt("cd_aluno"));
			}
			
			
			return aluno;
		}
		catch(Exception e) {		
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		
	}
	
	
	public static ResultSetMap  findDisciplinaAvaliacao(int cdMatricula, int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Oferta oferta = OfertaDAO.get(cdOferta, connect);
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			ResultSetMap rsmMatriculaDisciplina = MatriculaDisciplinaServices.getByAlunoDisciplina(cdMatricula, oferta.getCdDisciplina(), connect);
			rsmMatriculaDisciplina.next();
			
			criterios.add(new ItemComparator("A.CD_MATRICULA_DISCIPLINA", "" + rsmMatriculaDisciplina.getInt("cd_matricula_disciplina"), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.CD_OFERTA", "" + cdOferta, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmDisciplinaAvaliacao = DisciplinaAvaliacaoAlunoServices.find(criterios, connect);
		    return rsmDisciplinaAvaliacao;
		}
		catch(Exception e) {		
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		
	}
	
	
	public static ResultSetMap  frequenciaAlunoDisciplina(int cdMatricula, int cdDisciplina, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsmMatriculaDisciplina = MatriculaDisciplinaServices.getByAlunoDisciplina(cdMatricula, cdDisciplina, connect);
			rsmMatriculaDisciplina.next();
			
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_MATRICULA_DISCIPLINA", "" + rsmMatriculaDisciplina.getInt("cd_matricula_disciplina"), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.lg_presenca", "0", ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("B.cd_disciplina","" + cdDisciplina, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("B.st_aula", "" + AulaServices.ST_FECHADA, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmFrequenciaAluno = AulaMatriculaServices.find(criterios, connect);
				
			return rsmFrequenciaAluno;
		}
		catch(Exception e) {		
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		
	}
	
	
	
	/**
	 * Busca faltas do aluno da modalidade infantil
	 * @param criterios
	 * @param connect
	 * @return
	 */
	
	public static ResultSetMap  faltasAluno(int cdMatricula, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			ResultSetMap rsmMatriculaDisciplina = MatriculaDisciplinaServices.getAllByMatricula(cdMatricula, connect);
			rsmMatriculaDisciplina.next();
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_matricula_disciplina", "" + rsmMatriculaDisciplina.getInt("cd_matricula_disciplina"), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.lg_presenca", "0", ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("B.st_aula", "" + AulaServices.ST_FECHADA, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmFaltasAluno = AulaMatriculaServices.find(criterios, connect);
				
			return rsmFaltasAluno;
		}
		catch(Exception e) {		
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		
	}
	
	/**
	 * Busca endereço do aluno
	 * @param criterios
	 * @param connect
	 * @return
	 */
	public static PessoaEndereco findEnderecoAluno(int cdPessoa) {
		return findEnderecoAluno(cdPessoa);
	}

	public static PessoaEndereco findEnderecoAluno(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
	
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_pessoa", "" + cdPessoa, ItemComparator.EQUAL, Types.INTEGER));
			
			PessoaEndereco alunoEndereco = null;
			ResultSetMap rsmAlunoEndereco = PessoaEnderecoServices.find(criterios, connect);
			if(rsmAlunoEndereco.next()) {
				alunoEndereco = PessoaEnderecoDAO.get(rsmAlunoEndereco.getInt("cd_pessoa"));
			}
			
			
			return alunoEndereco;
		}
		catch(Exception e) {		
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		
	}
	
	
	
	public static DisciplinaAvaliacaoAluno getAlunoAvaliacaoByUnidade(int cdMatriculaDisciplina, int cdOfertaAvaliacao, int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		

			DisciplinaAvaliacaoAluno alunoAvaliacao = null;
			
			alunoAvaliacao = DisciplinaAvaliacaoAlunoDAO.get(cdMatriculaDisciplina,cdOfertaAvaliacao, cdOferta,null);
			
			return alunoAvaliacao;
		}
		catch(Exception e) {		
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		
	}
	
	
	


	public static PessoaFisica findAlunoPessoaFisica(int cdPessoa) {
		return findAlunoPessoaFisica(cdPessoa);
	}

	public static PessoaFisica findAlunoPessoaFisica(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
	
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_pessoa", "" + cdPessoa, ItemComparator.EQUAL, Types.INTEGER));
			
			PessoaFisica pessoafisica = null;
			ResultSetMap rsmAlunoPessoaFisica = PessoaFisicaServices.find(criterios, connect);
			if(rsmAlunoPessoaFisica.next()) {
				pessoafisica = PessoaFisicaDAO.get(rsmAlunoPessoaFisica.getInt("cd_pessoa"));
			}

			
			return pessoafisica;
		}
		catch(Exception e) {		
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		
	}
	

	public static Pessoa findAlunoPessoa(int cdPessoa) {
		return findAlunoPessoa(cdPessoa);
	}

	public static Pessoa findAlunoPessoa(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
	
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_pessoa", "" + cdPessoa, ItemComparator.EQUAL, Types.INTEGER));
			
			Pessoa pessoa = null;
			ResultSetMap rsmAlunoPessoa = PessoaServices.find(criterios, connect);
			if(rsmAlunoPessoa.next()) {
				pessoa = PessoaDAO.get(rsmAlunoPessoa.getInt("cd_pessoa"));
			}
	
			
			return pessoa;
		}
		catch(Exception e) {		
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		
	}
	
	
	
	
	
	public static Result findRelatorio(ArrayList<ItemComparator> criterios) {
		return findRelatorio(criterios, null);
	}

	/**
	 * Busca dos registros formatados para serem impressos em relatÃ³rio
	 * @param criterios
	 * @param connect
	 * @return
	 */
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
			int cdCursoAtual = 0;
			String codigosCursoReferenciados = "";//VariÃ¡vel usada para reunir os cÃ³digos de todos os cursos passado em cdCursoAtual, caso o mesmo seja um curso de referencia
			int cdTurmaAtual = 0;
			int tpNomePaiDeclarado = 0;
			int lgBolsaFamilia = -1;
			int lgTransportePublico = -1;
			int cdPeriodoLetivo = 0;
			int cdDeficiencia = -1;
			int cdDoenca = -1;
			int cdAlergia = -1;
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			boolean lgFiltroMes = false;//Usada para incluir na ordenaÃ§Ã£o o ano e mes da matrÃ­cula
			boolean lgSomenteDistorcao = false;//Usado para incluir apenas alunos com distorÃ§Ã£o de idade
			int tpEgressosRede = -1;//Usado para incluir apenas alunos que sÃ£o novos na rede
			int tpEgressosUltimoAno = -1;
			int lgAlunosProblemasCadastro = -1;
			int tpAtendimento   = -1;
			int tpModalidade   = -1;
			int lgRepetentes   = -1;//Busca de alunos que sÃ£o repetentes, ou seja, que estÃ£o no mesmo curso do ano escolar passado
			int tpCurso   = 0;//Usado para filtrar entre turmas multi ou nÃ£o
			int cdPeriodoLetivoSecretaria = 0;
			ResultSetMap rsmPeriodoSecretaria = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdSecretaria, connect);
			if(rsmPeriodoSecretaria.next())
				cdPeriodoLetivoSecretaria = rsmPeriodoSecretaria.getInt("cd_periodo_letivo");
			int lgPatologia = 0;
			int lgAlergia = 0;
			int lgTransferidosAbandono = 0;
			int lgDeficiencia = 0;
			int stTransferidosAbandono = -1;
			int stAlunoCenso = -1;
			int lgDivisaoCreche = 0;//Faz a divisÃ£o na pesquisa separando a Creche das Urbanas e Rurais
			int lgSecretaria = 0;
			int lgConservadosDaEscola = 0;
			int lgEgressosEscola = 0;
			int lgAlunosComNis = 0;//Busca apenas alunos que tenham nÃºmero de NIS
			int lgAlunosComSus = 0;//Busca apenas alunos com nÃºmero de SUS
			boolean lgRetirarOrdenacaoTurma = false;
			boolean lgQuantidadePorModalidade = false;
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.nm_pessoa")) {
					nmPessoa = Util.limparTexto(criterios.get(i).getValue());
					nmPessoa = "%"+nmPessoa.replaceAll(" ", "%")+"%";
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_INSTITUICAO")) {
					cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
					ResultSetMap rsm = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
					if(rsm.next() && cdPeriodoLetivo == 0){
						cdPeriodoLetivo = rsm.getInt("cd_periodo_letivo");
					}
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdPeriodoLetivo")) {
					cdPeriodoLetivo = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrIdadeInicial")) {
					nrIdadeInicial = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrIdadeFinal")) {
					nrIdadeFinal = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdCursoAtual")) {
					cdCursoAtual = Integer.parseInt(criterios.get(i).getValue());
					Curso cursoAtual = CursoDAO.get(cdCursoAtual, connect);
					if(cursoAtual.getLgReferencia()==1){
						
						ResultSetMap rsmCursosReferencia = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_produto_servico WHERE cd_produto_servico <> " + cdCursoAtual + " AND id_produto_servico = '"+cursoAtual.getIdProdutoServico()+"'").executeQuery());
						codigosCursoReferenciados = "(";
						while(rsmCursosReferencia.next()){
							codigosCursoReferenciados += rsmCursosReferencia.getInt("cd_produto_servico") + ", "; 
						}
						if(codigosCursoReferenciados.length() > 2)
							codigosCursoReferenciados = codigosCursoReferenciados.substring(0, codigosCursoReferenciados.length()-2) + ")";
						else
							codigosCursoReferenciados = "()";
						
					}
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdTurmaAtual")) {
					cdTurmaAtual = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpNomePaiDeclarado")) {
					tpNomePaiDeclarado = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpRecebeBolsaFamilia")) {
					lgBolsaFamilia = (Integer.parseInt(criterios.get(i).getValue()) == 0 ? -1 : Integer.parseInt(criterios.get(i).getValue()) == 2 ? 0 : Integer.parseInt(criterios.get(i).getValue()));
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpTransportado")) {
					lgTransportePublico = (Integer.parseInt(criterios.get(i).getValue()) == 0 ? -1 : Integer.parseInt(criterios.get(i).getValue()) == 2 ? 0 : Integer.parseInt(criterios.get(i).getValue()));
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
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgFiltroMes")) {
					lgFiltroMes = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgSomenteDistorcao")) {
					lgSomenteDistorcao = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpEgressosRede")) {
					tpEgressosRede = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpEgressosUltimoAno")) {
					tpEgressosUltimoAno = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgAlunosProblemasCadastro")) {
					lgAlunosProblemasCadastro = (Integer.parseInt(criterios.get(i).getValue()) == 0 ? -1 : Integer.parseInt(criterios.get(i).getValue()) == 2 ? 0 : Integer.parseInt(criterios.get(i).getValue()));
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpAtendimento")) {
					tpAtendimento = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpModalidade")) {
					tpModalidade = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgRepetentes")) {
					lgRepetentes = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpCurso")) {
					tpCurso = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgPatologia")) {
					lgPatologia = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgAlergia")) {
					lgAlergia = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgTransferidosAbandono")) {
					lgTransferidosAbandono = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgDeficiencia")) {
					lgDeficiencia = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("stAlunoCenso")) {
					stAlunoCenso = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgDivisaoCreche")) {
					lgDivisaoCreche = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgSecretaria")) {
					lgSecretaria = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgConservadosDaEscola")) {
					lgConservadosDaEscola = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgEgressosEscola")) {
					lgEgressosEscola = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgAlunosComNis")) {
					lgAlunosComNis = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgAlunosComSus")) {
					lgAlunosComSus = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgRetirarOrdenacaoTurma")) {
					lgRetirarOrdenacaoTurma = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgQuantidadePorModalidade")) {
					lgQuantidadePorModalidade = true;
				}
				else
					crt.add(criterios.get(i));
				
			}
			if((cdInstituicao == 0 || cdInstituicao == cdSecretaria) && cdPeriodoLetivo > 0){
				cdPeriodoLetivoSecretaria = cdPeriodoLetivo;
			}
			if(cdPeriodoLetivo == 0)
				cdPeriodoLetivo = cdPeriodoLetivoSecretaria;
			
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect);
			ArrayList<ItemComparator> criteriosPeriodo = new ArrayList<ItemComparator>();
			criteriosPeriodo.add(new ItemComparator("cd_instituicao", "" + instituicaoPeriodo.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
			criteriosPeriodo.add(new ItemComparator("nm_periodo_letivo", "" + (Integer.parseInt(instituicaoPeriodo.getNmPeriodoLetivo())-1), ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmPeriodoAnterior = InstituicaoPeriodoDAO.find(criteriosPeriodo, connect);
			InstituicaoPeriodo instituicaoPeriodoAnterior = null;
			if(rsmPeriodoAnterior.next()){
				instituicaoPeriodoAnterior = InstituicaoPeriodoDAO.get(rsmPeriodoAnterior.getInt("CD_PERIODO_LETIVO"), connect);
			}
			
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
			String sql =
		 			  "SELECT "+sqlLimit[0]+" A.cd_pessoa, A.nm_pessoa, A.nm_pessoa AS nm_aluno, A.nr_telefone1, A.nr_telefone2, A.nr_celular, E.dt_nascimento, E.nm_mae, E.nm_pai, E.tp_sexo, W.nm_responsavel, W.nm_parentesco, P.cd_cidade, P.nm_logradouro, P.nm_bairro, P.nr_endereco, P.nm_complemento, P.nr_telefone, P.tp_zona AS tp_zona_aluno, P.nr_cep, U.nm_cidade, V.sg_estado,"+ 
		 			  "	  W.cd_aluno, W.nr_inep, W.lg_bolsa_familia, W.lg_pai_nao_declarado, W.lg_cadastro_problema, MAT.cd_matricula, MAT.cd_curso, MAT.nr_matricula, MAT.st_matricula, MAT.dt_matricula, MATT.cd_instituicao, MATT.cd_turma, MATT.nm_turma, MATTCP.nm_produto_servico AS NM_CURSO_TURMA, MATINSP.nm_pessoa AS nm_instituicao, MATT.tp_turno, E.cd_naturalidade, MATI.tp_instituicao, MATIP.nm_periodo_letivo, MATCC.nr_idade, "+
		 			  "   MATIP.nm_periodo_letivo, MATCP.nm_produto_servico, MATCP.id_produto_servico, MATIE.tp_localizacao, (MATTCP.nm_produto_servico || ' - ' || MATT.nm_turma) AS NM_TURMA_COMPLETO, (NAT1.nm_cidade || ' - ' || NAT2.sg_estado) AS NM_NATURALIDADE  " +
 					 (cdCursoAtual > 0 ? ", MATCP.nm_produto_servico AS nm_curso " : "") + (lgPatologia > 0 ? ", MATDA.nm_doenca AS nm_patologia " : "") + (lgAlergia > 0 ? ", MATAA.nm_alergia " : "") + (lgTransferidosAbandono > 0 ?  ", DES_C.nm_pessoa AS NM_INSTITUICAO_DESTINO " : "") + (lgDeficiencia > 0 ? ", MATNE.nm_tipo_necessidade_especial AS NM_DEFICIENCIA " : "") + (lgAlunosComNis > 0 ? ", PTDN.nr_documento AS NR_NIS " : "") + (lgAlunosComSus > 0 ? ", PTDS.nr_documento AS NR_SUS " : "") + 
 					  "FROM grl_pessoa A " +
 					  "LEFT OUTER JOIN grl_pais 		B ON (A.cd_pais = B.cd_pais) " +
 					  "LEFT OUTER JOIN grl_forma_divulgacao  	C ON (A.cd_forma_divulgacao = C.cd_forma_divulgacao) " +
 					  "LEFT OUTER JOIN adm_classificacao 	D ON (A.cd_classificacao = D.cd_classificacao) " +
 					  "LEFT OUTER JOIN grl_pessoa_fisica 	E ON (A.cd_pessoa = E.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_cidade 			NAT1 ON (E.cd_naturalidade = NAT1.cd_cidade) " +
					  "LEFT OUTER JOIN grl_estado 			NAT2 ON (NAT1.cd_estado = NAT2.cd_estado) " +
 					  "LEFT OUTER JOIN grl_pessoa_empresa 	F ON (A.cd_pessoa = F.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_pessoa_endereco 	P ON (A.cd_pessoa = P.cd_pessoa AND P.lg_principal = 1) " +
 					  "LEFT OUTER JOIN grl_tipo_logradouro 	Q ON (P.cd_tipo_logradouro = Q.cd_tipo_logradouro) " +
 					  "LEFT OUTER JOIN grl_logradouro 		R ON (P.cd_logradouro = R.cd_logradouro) " +
 					  "LEFT OUTER JOIN grl_tipo_logradouro 	S ON (R.cd_tipo_logradouro = S.cd_tipo_logradouro) " +
 					  "LEFT OUTER JOIN grl_bairro 		T ON (P.cd_bairro = T.cd_bairro) " +
 					  "LEFT OUTER JOIN grl_cidade 		U ON (P.cd_cidade = U.cd_cidade) " +
 					  "LEFT OUTER JOIN grl_estado 		V ON (U.cd_estado = V.cd_estado) " +
 					  "JOIN acd_aluno 				W ON (A.cd_pessoa = W.cd_aluno) " +
 					  "LEFT OUTER JOIN grl_pessoa_ficha_medica PFM ON (A.cd_pessoa = PFM.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_pessoa 		Z ON (W.cd_responsavel = Z.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_pessoa_fisica 	X ON (Z.cd_pessoa = X.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_pessoa_endereco 	B1 ON (Z.cd_pessoa = B1.cd_pessoa " +
 					  "												AND B1.lg_principal = 1) " +
 					  "LEFT OUTER JOIN grl_tipo_logradouro 	C1 ON (B1.cd_tipo_logradouro = C1.cd_tipo_logradouro) " +
 					  "LEFT OUTER JOIN grl_logradouro 		D1 ON (B1.cd_logradouro = D1.cd_logradouro) " +
 					  "LEFT OUTER JOIN grl_tipo_logradouro 	E1 ON (D1.cd_tipo_logradouro = E1.cd_tipo_logradouro) " +
 					  "LEFT OUTER JOIN grl_bairro 		F1 ON (B1.cd_bairro = F1.cd_bairro) " +
 					  "LEFT OUTER JOIN grl_cidade 		G1 ON (B1.cd_cidade = G1.cd_cidade) " +
 					  "LEFT OUTER JOIN grl_estado 		H1 ON (G1.cd_estado = H1.cd_estado) " +
 					  " JOIN acd_matricula MAT ON (W.cd_aluno = MAT.cd_aluno) "+
 					  " JOIN acd_curso MATCC ON (MATCC.cd_curso = MAT.cd_curso) "+
 					  " JOIN grl_produto_servico MATCP ON (MAT.cd_curso = MATCP.cd_produto_servico) "+
 					  " JOIN acd_curso_etapa MATCCE ON (MATCCE.cd_curso = MAT.cd_curso) "+
 					  " JOIN acd_tipo_etapa MATCTE ON (MATCTE.cd_etapa = MATCCE.cd_etapa) "+
 					  " JOIN acd_turma MATT ON (MAT.cd_turma = MATT.cd_turma) "+
 					  " JOIN grl_pessoa MATINSP ON (MATT.cd_instituicao = MATINSP.cd_pessoa) "+
 					  " JOIN acd_curso MATTC ON (MATT.cd_curso = MATTC.cd_curso) "+
 					  " JOIN grl_produto_servico MATTCP ON (MATTC.cd_curso = MATTCP.cd_produto_servico) "+
 					  " JOIN acd_instituicao MATI ON (MATT.cd_instituicao = MATI.cd_instituicao) "+
 					  " JOIN acd_instituicao_periodo MATIP ON (MAT.cd_periodo_letivo = MATIP.cd_periodo_letivo) "+
 					  " JOIN acd_instituicao_educacenso MATIE ON (MATIE.cd_instituicao = MATT.cd_instituicao AND MATIE.cd_periodo_letivo = MATIP.cd_periodo_letivo) "+
 					  (lgPatologia > 0 ? " JOIN grl_pessoa_doenca MATPAD ON (MAT.cd_aluno = MATPAD.cd_pessoa) "+
 							  		 	 " JOIN grl_doenca MATDA ON (MATPAD.cd_doenca = MATDA.cd_doenca) " : "") +
		  		 	  (lgAlergia > 0 ? " JOIN grl_pessoa_alergia MATPAA ON (MAT.cd_aluno = MATPAA.cd_pessoa) "+
 							  		 	 " JOIN grl_alergia MATAA ON (MATPAA.cd_alergia = MATAA.cd_alergia) " : "") +
		  		 	 (lgDeficiencia > 0 ? " JOIN grl_pessoa_necessidade_especial MATPNP ON (MAT.cd_aluno = MATPNP.cd_pessoa) "+
		  		 			 			  " JOIN grl_tipo_necessidade_especial MATNE ON (MATPNP.cd_tipo_necessidade_especial = MATNE.cd_tipo_necessidade_especial) " : "") +
 					  (lgTransferidosAbandono > 0 ?  
 					  "  LEFT OUTER JOIN acd_matricula DES_A ON (MAT.cd_matricula = DES_A.cd_matricula_origem)" +
 					  "  LEFT OUTER JOIN acd_turma     DES_B ON (DES_A.cd_turma = DES_B.cd_turma)" +
 					  "  LEFT OUTER JOIN grl_pessoa    DES_C ON (DES_B.cd_instituicao = DES_C.cd_pessoa)" : "") +
 					  (lgAlunosComNis > 0 ? 
 					  "  JOIN grl_pessoa_tipo_documentacao PTDN ON (PTDN.cd_pessoa = A.cd_pessoa AND PTDN.cd_tipo_documentacao = "+PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_NIS+") " : "") +
 					  "  LEFT OUTER JOIN grl_pessoa_tipo_documentacao PTDS ON (PTDS.cd_pessoa = A.cd_pessoa AND PTDS.cd_tipo_documentacao = "+PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_SUS+") " +
 					  "WHERE 1 = 1 " +
 					 (tpEgressosUltimoAno == 1 ? " AND MAT.ST_MATRICULA NOT IN ("+MatriculaServices.ST_INATIVO+") " : (lgTransferidosAbandono == 0 ? (stAlunoCenso >= 0 ? "" : " AND MAT.ST_MATRICULA  IN ("+MatriculaServices.ST_ATIVA+", "+MatriculaServices.ST_CONCLUIDA+") ") : " AND MAT.ST_MATRICULA  IN ("+MatriculaServices.ST_DESISTENTE+", "+MatriculaServices.ST_EVADIDO+", "+MatriculaServices.ST_EM_TRANSFERENCIA+", "+MatriculaServices.ST_TRANSFERIDO+") ")) +
 				   (stAlunoCenso >= 0 ? (stAlunoCenso == MatriculaServices.ST_ALUNO_CENSO_APROVADO || stAlunoCenso == MatriculaServices.ST_ALUNO_CENSO_REPROVADO ? " AND MAT.ST_MATRICULA  IN ("+MatriculaServices.ST_ATIVA+", "+MatriculaServices.ST_CONCLUIDA+") " : " AND MAT.ST_MATRICULA  IN ("+MatriculaServices.ST_DESISTENTE+", "+MatriculaServices.ST_EVADIDO+", "+MatriculaServices.ST_EM_TRANSFERENCIA+", "+MatriculaServices.ST_TRANSFERIDO+", "+MatriculaServices.ST_FALECIDO+") ") : "")+
 				   (stAlunoCenso >= 0 ? " AND MAT.st_aluno_censo = " + stAlunoCenso + " " : "")+
				   (!nmPessoa.equals("") ? 
				   " AND TRANSLATE(A.nm_pessoa, 'Ã¡Ã©Ã­Ã³ÃºÃ Ã¨Ã¬Ã²Ã¹Ã£ÃµÃ¢ÃªÃ®Ã´Ã´Ã¤Ã«Ã¯Ã¶Ã¼Ã§Ã�Ã‰Ã�Ã“ÃšÃ€ÃˆÃŒÃ’Ã™ÃƒÃ•Ã‚ÃŠÃŽÃ”Ã›Ã„Ã‹Ã�Ã–ÃœÃ‡', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE TRANSLATE('"+nmPessoa+"', 'Ã¡Ã©Ã­Ã³ÃºÃ Ã¨Ã¬Ã²Ã¹Ã£ÃµÃ¢ÃªÃ®Ã´Ã´Ã¤Ã«Ã¯Ã¶Ã¼Ã§Ã�Ã‰Ã�Ã“ÃšÃ€ÃˆÃŒÃ’Ã™ÃƒÃ•Ã‚ÃŠÃŽÃ”Ã›Ã„Ã‹Ã�Ã–ÃœÃ‡', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') ": "") +
				   (cdInstituicao == 0 ? "" : (cdInstituicao != cdSecretaria ? " AND MATT.cd_instituicao = " + cdInstituicao : "")) + 
				   (tpAtendimento >= 0 ? " AND MATT.tp_atendimento = " + tpAtendimento : cdCursoAtual != 1271 && cdCursoAtual != 1282 ? " AND MATT.tp_atendimento NOT IN ("+TurmaServices.TP_ATENDIMENTO_AEE+", "+TurmaServices.TP_ATENDIMENTO_ATIVIDADE_COMPLEMENTAR+")    " : "") +
				   (cdInstituicao == cdSecretaria || cdInstituicao == 0 ? " AND MATIP.nm_periodo_letivo = '" + InstituicaoPeriodoDAO.get(cdPeriodoLetivoSecretaria, connect).getNmPeriodoLetivo()+"'" : " AND MATIP.cd_instituicao = "+cdInstituicao +" AND MATIP.cd_periodo_letivo = " + cdPeriodoLetivo)+
				   " AND MATT.nm_turma NOT LIKE 'TRANS%' "+//NÃ£o incluir turmas de transferÃªncia
				   " AND MATT.ST_TURMA <> " + TurmaServices.ST_INATIVO+//NÃ£o incluir turmas inativas
				   " AND MATIE.st_instituicao_publica = "+ InstituicaoEducacensoServices.ST_EM_ATIVIDADE + 
				   (tpNomePaiDeclarado > 0 ? " AND (E.nm_pai " + (tpNomePaiDeclarado == 1 ? " <> " : " = ") + " '' "+(tpNomePaiDeclarado == 1 ? " AND " : " OR ")+" E.nm_pai IS " + (tpNomePaiDeclarado == 1 ? " NOT " : " ") + " NULL) " : "") + 
				   (lgBolsaFamilia >= 0 ? " AND W.lg_bolsa_familia = " + lgBolsaFamilia + " " : "") +
				   (lgTransportePublico >= 0 ? " AND MAT.lg_transporte_publico = " + lgTransportePublico + " " : "") +
				   (cdCursoAtual > 0 ? " AND ((MAT.cd_curso = " + cdCursoAtual + " OR MATT.cd_curso = " + cdCursoAtual + ") " + (codigosCursoReferenciados.length() > 2 ? " OR MAT.cd_curso IN " + codigosCursoReferenciados : "") +") " : "") +
				   (cdTurmaAtual > 0 ? " AND MAT.cd_turma = " + cdTurmaAtual + " " : "") +
				   (cdDeficiencia >= 0 ? (cdDeficiencia == 0 ? " AND EXISTS ( SELECT PNE.cd_pessoa FROM grl_pessoa_necessidade_especial PNE WHERE PNE.cd_pessoa = W.cd_aluno ) " : " AND EXISTS ( SELECT PNE.cd_pessoa FROM grl_pessoa_necessidade_especial PNE WHERE PNE.cd_pessoa = W.cd_aluno AND PNE.cd_tipo_necessidade_especial = "+cdDeficiencia+" ) ") : "") + 
				   (cdDoenca >= 0 ? (cdDoenca == 0 ? " AND EXISTS ( SELECT PD.cd_pessoa FROM grl_pessoa_doenca PD WHERE PD.cd_pessoa = W.cd_aluno ) " : " AND EXISTS ( SELECT PD.cd_pessoa FROM grl_pessoa_doenca PD WHERE PD.cd_pessoa = W.cd_aluno AND PD.cd_doenca = "+cdDoenca+" ) ") : "")+
				   (cdAlergia >= 0 ? (cdAlergia == 0 ? " AND EXISTS ( SELECT PA.cd_pessoa FROM grl_pessoa_alergia PA WHERE PA.cd_pessoa = W.cd_aluno ) " : " AND EXISTS ( SELECT PA.cd_pessoa FROM grl_pessoa_alergia PA WHERE PA.cd_pessoa = W.cd_aluno AND PA.cd_alergia = "+cdAlergia+" ) ") : "")+
				   (lgAlunosProblemasCadastro >= 0 ? " AND W.lg_cadastro_problema = " + lgAlunosProblemasCadastro + " " : "") +
				   (tpEgressosRede > 0 ? (tpEgressosRede == 1 ? " AND NOT EXISTS ( SELECT MATEG.cd_aluno FROM acd_matricula MATEG WHERE MATEG.cd_aluno = W.cd_aluno AND MATEG.cd_matricula <> MAT.cd_matricula AND MATEG.ST_MATRICULA NOT IN ("+MatriculaServices.ST_CANCELADO+", "+MatriculaServices.ST_INATIVO+") ) " : " AND EXISTS ( SELECT MATEG.cd_pessoa FROM acd_matricula MATEG WHERE MATEG.cd_aluno = W.cd_aluno AND MATEG.cd_matricula <> MAT.cd_matricula AND MATEG.ST_MATRICULA NOT IN ("+MatriculaServices.ST_CANCELADO+", "+MatriculaServices.ST_INATIVO+") ) ") : "") + 
				   (tpEgressosUltimoAno > 0 ? (tpEgressosUltimoAno == 1 ? " AND NOT EXISTS ( SELECT MATEG.cd_aluno FROM acd_matricula MATEG "
							+ "																	 JOIN acd_instituicao_periodo PEREG ON (MATEG.cd_periodo_letivo = PEREG.cd_periodo_letivo )"
					   		+ "																		WHERE MATEG.cd_aluno = W.cd_aluno "
					   		+ "																		  AND MATEG.cd_matricula <> MAT.cd_matricula "
					   		+ "																		  AND (PEREG.cd_periodo_letivo = "+instituicaoPeriodoAnterior.getCdPeriodoLetivo()+" OR PEREG.cd_periodo_letivo_superior = "+instituicaoPeriodoAnterior.getCdPeriodoLetivo()+" )) " : 	
					   		" 													AND EXISTS ( SELECT MATEG.cd_pessoa FROM acd_matricula MATEG "
					   		+ "																	WHERE MATEG.cd_aluno = W.cd_aluno "
					   		+ "																	  AND MATEG.cd_matricula <> MAT.cd_matricula ) ") : "")+
				   (tpCurso > 0 ? " AND MATTC.lg_multi = "+ (tpCurso == 1 ? "1" : "0") : "") + 
				   (lgConservadosDaEscola > 0 ? " AND EXISTS(SELECT * FROM acd_matricula MAT_CON, acd_turma MAT_CON_T, acd_instituicao_periodo MAT_CON_P "
				   		+ "										WHERE MAT_CON.cd_periodo_letivo = MAT_CON_P.cd_periodo_letivo"
						+ "										  AND MAT_CON.cd_turma = MAT_CON_T.cd_turma"
				   		+ "										  AND (MAT_CON_P.cd_periodo_letivo = "+instituicaoPeriodoAnterior.getCdPeriodoLetivo()+" OR MAT_CON_P.cd_periodo_letivo_superior = "+instituicaoPeriodoAnterior.getCdPeriodoLetivo()+")"
				   		+ "										  AND MAT_CON.st_matricula = "+MatriculaServices.ST_CONCLUIDA
				   		+ "										  AND MAT_CON.st_aluno_censo = "+MatriculaServices.ST_ALUNO_CENSO_REPROVADO
				   		+ "										  AND MAT_CON.cd_curso NOT IN (" + ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0) + ", " + ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0) + ")"
				   		+ "										  AND MAT_CON.cd_aluno = W.cd_aluno"
				   		+ "										  AND MAT_CON_T.cd_instituicao = MATT.cd_instituicao)": "") + 
				   	(lgEgressosEscola > 0 ? " AND NOT EXISTS ( SELECT MATEGE.cd_aluno FROM acd_matricula MATEGE, acd_instituicao_periodo MATEGEP WHERE MATEGE.cd_aluno = W.cd_aluno AND MATEGE.cd_periodo_letivo = MATEGEP.cd_periodo_letivo AND MATEGEP.cd_instituicao = MATT.cd_instituicao AND MATEGEP.nm_periodo_letivo = '"+instituicaoPeriodoAnterior.getNmPeriodoLetivo()+"' AND MATEGE.st_matricula IN ("+MatriculaServices.ST_CONCLUIDA+", "+MatriculaServices.ST_ATIVA+")) " : "");
			
			ResultSetMap rsm = Search.find(sql, " "+sqlLimit[1], crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			int qtMatriculadosZonaUrbana = 0;
			int qtMatriculadosZonaRural = 0;
			int qtMatriculadosCreche = 0;
			
			HashMap<String, Integer> escolasCreche = new HashMap<String, Integer>();
			HashMap<String, Integer> escolasRural = new HashMap<String, Integer>();
			HashMap<String, Integer> escolasUrbana = new HashMap<String, Integer>();
			
			ResultSetMap modalidadeEscolasCreche = new ResultSetMap();
			ResultSetMap modalidadeEscolasRural = new ResultSetMap();
			ResultSetMap modalidadeEscolasUrbana = new ResultSetMap();
			
			ResultSetMap registerQuantEscola = new ResultSetMap();
			
			int x = 0;
			
			ResultSetMap rsmCursosEducacaoInfantil 	= ParametroServices.getValoresOfParametro("CD_CURSO_EDUCACAO_INFANTIL");
			ResultSetMap rsmCursosFundamental1 		= ParametroServices.getValoresOfParametro("CD_CURSO_FUNDAMENTAL_1");
			ResultSetMap rsmCursosFundamental2 		= ParametroServices.getValoresOfParametro("CD_CURSO_FUNDAMENTAL_2");
			
			while(rsm.next()){
				rsm.setValueToField("CL_SITUACAO", MatriculaServices.situacao[rsm.getInt("st_matricula")]);
				rsm.setValueToField("CD_CURSO_MATRICULA", rsm.getInt("cd_curso"));
				rsm.setValueToField("ID_PRODUTO_SERVICO_CURSO_MATRICULA", rsm.getString("id_produto_servico"));
				rsm.setValueToField("NM_MODALIDADE_CURSO_MATRICULA", rsm.getString("nm_produto_servico"));
				rsm.setValueToField("CL_TURMA", rsm.getString("nm_turma") + " - " + TurmaServices.tiposTurno[rsm.getInt("tp_turno")]);
				rsm.setValueToField("CL_TURNO", TurmaServices.tiposTurno[rsm.getInt("tp_turno")]);
				rsm.setValueToField("ID_PRODUTO_SERVICO_CURSO", rsm.getString("id_produto_servico"));
				rsm.setValueToField("NM_MODALIDADE_CURSO", rsm.getString("nm_produto_servico"));
				rsm.setValueToField("NM_TURMA_COMPLETO", rsm.getString("NM_TURMA_COMPLETO") + " (" + rsm.getString("CL_TURNO") + ")");
				rsm.setValueToField("CL_BOLSA_FAMILIA", rsm.getInt("lg_bolsa_familia") == 1 ? "Sim" : "NÃ£o");
				
				rsm.setValueToField("NM_ENDERECO_COMPLETO", rsm.getString("nm_logradouro") + ", " + rsm.getString("nr_endereco") + ", " + rsm.getString("nm_bairro"));
				
				
				if(lgEgressosEscola > 0){
			  		ResultSetMap rsmMatriculaAnterior = new ResultSetMap(connect.prepareStatement("SELECT PES.nm_pessoa "
			  				+ "																		FROM acd_matricula MAT, acd_instituicao_periodo P, grl_pessoa PES "
			  				+ "																	   WHERE PES.cd_pessoa = P.cd_instituicao "
			  				+ "																		 AND MAT.cd_periodo_letivo = P.cd_periodo_letivo "
			  				+ "																		 AND MAT.cd_aluno = "+rsm.getInt("cd_aluno")+" "
			  				+ "																		 AND MAT.st_matricula IN ("+MatriculaServices.ST_CONCLUIDA+", "+MatriculaServices.ST_ATIVA+") "
			  				+ "																		 AND P.nm_periodo_letivo = '"+instituicaoPeriodoAnterior.getNmPeriodoLetivo()+"'").executeQuery());
					if(rsmMatriculaAnterior.next()){
						rsm.setValueToField("NM_ESCOLA_ORIGEM", rsmMatriculaAnterior.getString("NM_PESSOA"));
					}
				}
				
				if(lgSecretaria == 0){
					boolean achadoTurma = false;
					while(registerQuantEscola.next()){
						if(registerQuantEscola.getInt("CD_TURMA") == rsm.getInt("CD_TURMA")){
							registerQuantEscola.setValueToField("QT_MATRICULADOS", registerQuantEscola.getInt("QT_MATRICULADOS") + 1);
							achadoTurma = true;
							break;
						}
					}
					registerQuantEscola.beforeFirst();
					
					if(!achadoTurma){
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("NM_MODALIDADE_CURSO", rsm.getString("NM_CURSO_TURMA"));
						register.put("CD_TURMA", rsm.getInt("CD_TURMA"));
						register.put("NM_TURMA", rsm.getString("NM_TURMA"));
						register.put("QT_MATRICULADOS", 1);
						registerQuantEscola.addRegister(register);
						registerQuantEscola.beforeFirst();
					}
				}
				
				if(lgTransferidosAbandono > 0 && (rsm.getInt("st_matricula") != MatriculaServices.ST_TRANSFERIDO || rsm.getString("NM_INSTITUICAO_DESTINO") == null || rsm.getString("NM_INSTITUICAO_DESTINO").equals(""))){
					rsm.setValueToField("NM_INSTITUICAO_DESTINO", "---");
				}
				
				if(rsm.getInt("tp_sexo") == 0){
					rsm.setValueToField("sg_sexo", "M");
				}
				else{
					rsm.setValueToField("sg_sexo", "F");
				}
				
				String[] telefones = new String[3];
				int i = 0;
				
				if(rsm.getString("NR_TELEFONE1") != null && !rsm.getString("NR_TELEFONE1").trim().equals(""))
					telefones[i++] = rsm.getString("NR_TELEFONE1");
				
				if(rsm.getString("NR_TELEFONE2") != null && !rsm.getString("NR_TELEFONE2").trim().equals(""))
					telefones[i++] = rsm.getString("NR_TELEFONE2");
				
				if(rsm.getString("NR_CELULAR") != null && !rsm.getString("NR_CELULAR").trim().equals(""))
					telefones[i++] = rsm.getString("NR_CELULAR");
				
				String nrTelefones = "";
				for(String telefone : telefones){
					nrTelefones += (telefone != null ? telefone + " / " : "");
				}
				if(nrTelefones.equals("")){
					nrTelefones = "NÃ£o possui telefone cadastrado";
				}
				else{
					nrTelefones = nrTelefones.substring(0, nrTelefones.length()-3);
				}
				
				rsm.setValueToField("NR_TELEFONES", nrTelefones);
				
				if(lgRepetentes == 1){
					ResultSetMap rsmMatriculaAnoAnterior = new ResultSetMap(connect.prepareStatement("SELECT * FROM matricula A, acd_instituicao_periodo B WHERE A.cd_periodo_letivo = B.cd_periodo_letivo AND A.cd_aluno = "+rsm.getInt("cd_aluno")+" AND A.st_matricula IN ("+MatriculaServices.ST_ATIVA+", "+MatriculaServices.ST_CONCLUIDA+", "+MatriculaServices.ST_DESISTENTE+", "+MatriculaServices.ST_EVADIDO+", "+MatriculaServices.ST_TRANCADA+") AND B.nm_periodo_letivo = '"+(Integer.parseInt(rsm.getString("nm_periodo_letivo")) - 1)+"' ORDER BY st_matricula").executeQuery());
					if(rsmMatriculaAnoAnterior.next()){
						boolean achado = false;
						//Saber se o curso Ã© do Fundamental 1
						if(!achado){
							while(rsmCursosFundamental1.next()){
								if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental1.getString("VL_REAL"))){
									
									achado = true;
									break;
								}
										
							}
							rsmCursosFundamental1.beforeFirst();
							//Saber se o curso Ã© do Fundamental 2
							if(!achado){
								while(rsmCursosFundamental2.next()){
									if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental2.getString("VL_REAL"))){
										
										achado = true;
										break;
									}
											
								}
								rsmCursosFundamental2.beforeFirst();
							}
						}
						
						if(achado){
							
							ResultSetMap rsmCursoCorrespondencia = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_curso_correspondencia WHERE cd_curso = " + rsm.getInt("cd_curso") + " AND cd_curso_correspondencia = " + rsmMatriculaAnoAnterior.getInt("cd_curso")).executeQuery());
							if(!rsmCursoCorrespondencia.next()){
								rsm.deleteRow();
								if(x == 0)
									rsm.beforeFirst();
								else
									rsm.previous();
								continue;
							}
							
						}
						//Se nÃ£o for fundamental nem EJA, Ã© considerado infantil
						else{
							if(rsm.getInt("cd_curso") != rsmMatriculaAnoAnterior.getInt("cd_curso")){
								rsm.deleteRow();
								if(x == 0)
									rsm.beforeFirst();
								else
									rsm.previous();
								continue;
							}
						}
						
					}
				}
				else if(lgRepetentes == 0){
					ResultSetMap rsmMatriculaAnoAnterior = new ResultSetMap(connect.prepareStatement("SELECT * FROM matricula A, acd_instituicao_periodo B WHERE A.cd_periodo_letivo = B.cd_periodo_letivo AND A.cd_aluno = "+rsm.getInt("cd_aluno")+" AND A.st_matricula IN ("+MatriculaServices.ST_ATIVA+", "+MatriculaServices.ST_CONCLUIDA+", "+MatriculaServices.ST_DESISTENTE+", "+MatriculaServices.ST_EVADIDO+", "+MatriculaServices.ST_TRANCADA+") AND B.nm_periodo_letivo = '"+(Integer.parseInt(rsm.getString("nm_periodo_letivo")) - 1)+"' ORDER BY st_matricula").executeQuery());
					if(rsmMatriculaAnoAnterior.next()){
						boolean achado = false;
						//Saber se o curso Ã© do Fundamental 1
						if(!achado){
							while(rsmCursosFundamental1.next()){
								if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental1.getString("VL_REAL"))){
									achado = true;
									break;
								}
										
							}
							rsmCursosFundamental1.beforeFirst();
							//Saber se o curso Ã© do Fundamental 2
							if(!achado){
								while(rsmCursosFundamental2.next()){
									if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental2.getString("VL_REAL"))){
										achado = true;
										break;
									}
											
								}
								rsmCursosFundamental2.beforeFirst();
							}
						}
						
						if(achado){
							
							ResultSetMap rsmCursoCorrespondencia = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_curso_correspondencia WHERE cd_curso = " + rsm.getInt("cd_curso") + " AND cd_curso_correspondencia = " + rsmMatriculaAnoAnterior.getInt("cd_curso")).executeQuery());
							if(rsmCursoCorrespondencia.next()){
								rsm.deleteRow();
								if(x == 0)
									rsm.beforeFirst();
								else
									rsm.previous();
								continue;
							}
							
						}
						//Se nÃ£o for fundamental, Ã© considerado infantil
						else{
							if(rsm.getInt("cd_curso") == rsmMatriculaAnoAnterior.getInt("cd_curso")){
								rsm.deleteRow();
								if(x == 0)
									rsm.beforeFirst();
								else
									rsm.previous();
								continue;
							}
						}
						
					}
				} 
				
				if(tpModalidade == 1){
					boolean achado = false;
					while(rsmCursosEducacaoInfantil.next()){
						if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosEducacaoInfantil.getString("VL_REAL"))){
							achado = true;
							break;
						}
								
					}
					rsmCursosEducacaoInfantil.beforeFirst();
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
					while(rsmCursosFundamental1.next()){
						if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental1.getString("VL_REAL"))){
							achado = true;
							break;
						}
								
					}
					rsmCursosFundamental1.beforeFirst();
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
					rsmCursosFundamental2 = ParametroServices.getValoresOfParametro("CD_CURSO_FUNDAMENTAL_2");
					while(rsmCursosFundamental2.next()){
						if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental2.getString("VL_REAL"))){
							achado = true;
							break;
						}
								
					}
					rsmCursosFundamental2.beforeFirst();
					if(!achado){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				
				rsm.setValueToField("NM_ZONA_ALUNO", PessoaEnderecoServices.tiposZona[rsm.getInt("tp_zona_aluno")]);
				
				GregorianCalendar dtNascimento = rsm.getGregorianCalendar("dt_nascimento");
				
				rsm.setValueToField("NR_TELEFONE", (rsm.getString("NR_TELEFONE1") != null && !rsm.getString("NR_TELEFONE1").trim().equals("") ? rsm.getString("NR_TELEFONE1") : (rsm.getString("NR_TELEFONE2") != null && !rsm.getString("NR_TELEFONE2").trim().equals("") ? rsm.getString("NR_TELEFONE2") : (rsm.getString("NR_CELULAR") != null && !rsm.getString("NR_CELULAR").trim().equals("") ? rsm.getString("NR_CELULAR") : rsm.getString("NR_TELEFONE")))));
				
				int nrIdadeAluno = Util.getIdade(dtNascimento, 31, 2, Integer.parseInt(rsm.getString("NM_PERIODO_LETIVO")));
				if(dtNascimento != null)
					rsm.setValueToField("DT_NM_NASCIMENTO", (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.DAY_OF_MONTH))) > 9 ? dtNascimento.get(Calendar.DAY_OF_MONTH) : "0" + dtNascimento.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1)) + "/" + dtNascimento.get(Calendar.YEAR) + " (" + nrIdadeAluno + " anos)");
				
				rsm.setValueToField("NR_IDADE_ALUNO", nrIdadeAluno);
				if(nrIdadeInicial != null){
					if(nrIdadeAluno < Integer.parseInt(nrIdadeInicial)){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				
				if(nrIdadeFinal != null){
					if(nrIdadeAluno > Integer.parseInt(nrIdadeFinal)){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				
				int nrDivergencia = rsm.getInt("nr_idade") - nrIdadeAluno;
				
				rsm.setValueToField("NR_DIVERGENCIA", (nrDivergencia < 0 ? (-1) * nrDivergencia : nrDivergencia));
				
				if(lgSomenteDistorcao && rsm.getInt("nr_idade") == 0){
					rsm.deleteRow();
					if(x == 0)
						rsm.beforeFirst();
					else
						rsm.previous();
					continue;
				}
				
				if(lgSomenteDistorcao && (rsm.getInt("NR_DIVERGENCIA") == 0 || rsm.getInt("NR_DIVERGENCIA") == 1)){
					rsm.deleteRow();
					if(x == 0)
						rsm.beforeFirst();
					else
						rsm.previous();
					continue;
				}
				
				if(lgSomenteDistorcao && CursoServices.isEja(rsm.getInt("cd_curso"), connect) && nrIdadeAluno >= 15){
					rsm.deleteRow();
					if(x == 0)
						rsm.beforeFirst();
					else
						rsm.previous();
					continue;
				}
				
				
				if(rsm.getGregorianCalendar("dt_matricula") != null){
					rsm.setValueToField("NR_MES", rsm.getGregorianCalendar("dt_matricula").get(Calendar.MONTH));
					rsm.setValueToField("NR_ANO", rsm.getGregorianCalendar("dt_matricula").get(Calendar.YEAR));
					rsm.setValueToField("NM_MES", Util.meses[rsm.getInt("NR_MES")] + "/" + rsm.getInt("NR_ANO"));
				}
				
				if(rsm.getInt("tp_localizacao") == 1 || rsm.getInt("tp_localizacao") == 2)
					rsm.setValueToField("NM_ZONA", Util.limparAcentos(InstituicaoEducacensoServices.tipoLocalizacao[(rsm.getInt("tp_localizacao")-1)]));
				else
					rsm.setValueToField("NM_ZONA", "");
				
				ArrayList<String> fieldsComparacao = new ArrayList<String>();
				fieldsComparacao.add("NM_INSTITUICAO");
				fieldsComparacao.add("NM_MODALIDADE_CURSO");
				
				ArrayList<String> fieldsComparacaoValor = new ArrayList<String>();
				fieldsComparacaoValor.add(rsm.getString("NM_INSTITUICAO"));
				fieldsComparacaoValor.add(rsm.getString("NM_MODALIDADE_CURSO"));
				
				if(lgDivisaoCreche == 0 && rsm.getInt("TP_INSTITUICAO") == InstituicaoServices.TP_INSTITUICAO_CRECHE){
					rsm.setValueToField("NM_ZONA", "CRECHE");
					qtMatriculadosCreche++;
					if(!lgQuantidadePorModalidade){
						if(!escolasCreche.containsKey(rsm.getString("NM_INSTITUICAO"))){
							escolasCreche.put(rsm.getString("NM_INSTITUICAO"), 1);
						}
						else{
							escolasCreche.put(rsm.getString("NM_INSTITUICAO"), (escolasCreche.get(rsm.getString("NM_INSTITUICAO")) + 1));
						}
					}
					else{
						HashMap<String, Object> register = Util.existsInResultSetMap(modalidadeEscolasCreche, fieldsComparacao, fieldsComparacaoValor);
						if(register == null){
							register = new HashMap<String, Object>();
							register.put("NM_MODALIDADE_CURSO", rsm.getString("NM_MODALIDADE_CURSO"));
							register.put("NM_ZONA", rsm.getString("NM_ZONA"));
							register.put("NM_INSTITUICAO", rsm.getString("NM_INSTITUICAO"));
							register.put("QT_MATRICULADOS", 1);
							modalidadeEscolasCreche.addRegister(register);
						}
						else{
							register.put("QT_MATRICULADOS", (Integer.parseInt(String.valueOf(register.get("QT_MATRICULADOS"))) + 1));
						}
					}
				}
				else if(rsm.getInt("tp_localizacao") == InstituicaoEducacensoServices.TP_LOCALIZACAO_URBANA){
					qtMatriculadosZonaUrbana++;
					
					if(!lgQuantidadePorModalidade){
						if(!escolasUrbana.containsKey(rsm.getString("NM_INSTITUICAO"))){
							escolasUrbana.put(rsm.getString("NM_INSTITUICAO"), 1);
						}
						else{
							escolasUrbana.put(rsm.getString("NM_INSTITUICAO"), (escolasUrbana.get(rsm.getString("NM_INSTITUICAO")) + 1));
						}
					}
					else{
						HashMap<String, Object> register = Util.existsInResultSetMap(modalidadeEscolasUrbana, fieldsComparacao, fieldsComparacaoValor);
						if(register == null){
							register = new HashMap<String, Object>();
							register.put("NM_MODALIDADE_CURSO", rsm.getString("NM_MODALIDADE_CURSO"));
							register.put("NM_ZONA", rsm.getString("NM_ZONA"));
							register.put("NM_INSTITUICAO", rsm.getString("NM_INSTITUICAO"));
							register.put("QT_MATRICULADOS", 1);
							modalidadeEscolasUrbana.addRegister(register);
						}
						else{
							register.put("QT_MATRICULADOS", (Integer.parseInt(String.valueOf(register.get("QT_MATRICULADOS"))) + 1));
						}
					}
				}
				else{
					qtMatriculadosZonaRural++;
					if(!lgQuantidadePorModalidade){
						if(!escolasRural.containsKey(rsm.getString("NM_INSTITUICAO"))){
							escolasRural.put(rsm.getString("NM_INSTITUICAO"), 1);
						}
						else{
							escolasRural.put(rsm.getString("NM_INSTITUICAO"), (escolasRural.get(rsm.getString("NM_INSTITUICAO")) + 1));
						}
					}
					else{
						HashMap<String, Object> register = Util.existsInResultSetMap(modalidadeEscolasRural, fieldsComparacao, fieldsComparacaoValor);
						if(register == null){
							register = new HashMap<String, Object>();
							register.put("NM_MODALIDADE_CURSO", rsm.getString("NM_MODALIDADE_CURSO"));
							register.put("NM_ZONA", rsm.getString("NM_ZONA"));
							register.put("NM_INSTITUICAO", rsm.getString("NM_INSTITUICAO"));
							register.put("QT_MATRICULADOS", 1);
							modalidadeEscolasRural.addRegister(register);
						}
						else{
							register.put("QT_MATRICULADOS", (Integer.parseInt(String.valueOf(register.get("QT_MATRICULADOS"))) + 1));
						}
					}
				}
				
				ResultSetMap rsmPessoaNecessidadeEspecial = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_pessoa_necessidade_especial A, grl_tipo_necessidade_especial B WHERE A.cd_tipo_necessidade_especial = B.cd_tipo_necessidade_especial AND cd_pessoa = " + rsm.getInt("cd_aluno")).executeQuery());
				if(rsmPessoaNecessidadeEspecial.next()){
					rsm.setValueToField("TP_POSSUI_DEFICIENCIA", 1);
					
					rsmPessoaNecessidadeEspecial.beforeFirst();
					String clDeficiencia = "";
					while(rsmPessoaNecessidadeEspecial.next()){
						clDeficiencia += rsmPessoaNecessidadeEspecial.getString("nm_tipo_necessidade_especial") + ", ";
					}
					rsmPessoaNecessidadeEspecial.beforeFirst();
					
					if(clDeficiencia.length() > 0){
						clDeficiencia = clDeficiencia.substring(0, clDeficiencia.length()-2);
					}
					
					rsm.setValueToField("CL_DEFICIENCIA", clDeficiencia);
				}
				else{
					rsm.setValueToField("TP_POSSUI_DEFICIENCIA", 2);
					rsm.setValueToField("CL_DEFICIENCIA", "");
				}
				
				
				
				x++;
			}
			rsm.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			
			fields.add("NM_ZONA");
			if(lgFiltroMes){
				fields.add("NR_ANO");
				fields.add("NR_MES");
			}
			fields.add("NM_INSTITUICAO");
			if(!lgRetirarOrdenacaoTurma)
				fields.add("CL_TURMA");
			fields.add("ID_PRODUTO_SERVICO_CURSO");
			fields.add("NM_PESSOA");
			
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			ResultSetMap rsmQuantidade = new ResultSetMap();
			if(lgDivisaoCreche == 0){
				if(!lgQuantidadePorModalidade){
					for(String key: escolasCreche.keySet()){
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("NM_ZONA", "CRECHE");
						register.put("NM_INSTITUICAO", key);
						register.put("QT_MATRICULADOS", escolasCreche.get(key));
						rsmQuantidade.addRegister(register);
					}
				}
				else{
					rsmQuantidade.getLines().addAll(modalidadeEscolasCreche.getLines());
				}
			}
			
			if(!lgQuantidadePorModalidade){
				for(String key: escolasUrbana.keySet()){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("NM_ZONA", "URBANA");
					register.put("NM_INSTITUICAO", key);
					register.put("QT_MATRICULADOS", escolasUrbana.get(key));
					rsmQuantidade.addRegister(register);
				}
			}
			else{
				rsmQuantidade.getLines().addAll(modalidadeEscolasUrbana.getLines());
			}
			
			if(!lgQuantidadePorModalidade){
				for(String key: escolasRural.keySet()){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("NM_ZONA", "RURAL");
					register.put("NM_INSTITUICAO", key);
					register.put("QT_MATRICULADOS", escolasRural.get(key));
					rsmQuantidade.addRegister(register);
				}
			}
			else{
				rsmQuantidade.getLines().addAll(modalidadeEscolasRural.getLines());
			}
			
			
			fields = new ArrayList<String>();
			fields.add("NM_ZONA");
			fields.add("NM_INSTITUICAO");
			if(lgQuantidadePorModalidade)
				fields.add("NM_MODALIDADE_CURSO");
			rsmQuantidade.orderBy(fields);
			rsmQuantidade.beforeFirst();
			
			if(lgSecretaria == 0){
				fields = new ArrayList<String>();
				fields.add("NM_MODALIDADE_CURSO");
				if(!lgQuantidadePorModalidade)
					fields.add("NM_TURMA");
				registerQuantEscola.orderBy(fields);
				registerQuantEscola.beforeFirst();
			}
			
			if(lgQuantidadePorModalidade){
				registerQuantEscola = rsmQuantidade;
			}
			
			Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsm);
			result.addObject("QT_MATRICULADOS_ZONA_URBANA", qtMatriculadosZonaUrbana);
			result.addObject("QT_MATRICULADOS_ZONA_RURAL", qtMatriculadosZonaRural);
			result.addObject("QT_MATRICULADOS_CRECHE", qtMatriculadosCreche);
			result.addObject("QT_TOTAL_ESCOLAS", escolasCreche.size() + escolasRural.size() + escolasUrbana.size());
			result.addObject("RSM_QUANTIDADE", (lgSecretaria == 0 ? registerQuantEscola :  rsmQuantidade));
			
//			HashMap<String, Object> params = new HashMap<String, Object>();
//			params.put("QT_MATRICULADOS_ZONA_URBANA", qtMatriculadosZonaUrbana);
//			params.put("QT_MATRICULADOS_ZONA_RURAL", qtMatriculadosZonaRural);
//			params.put("QT_MATRICULADOS_CRECHE", qtMatriculadosCreche);
//			params.put("QT_TOTAL_ESCOLAS", escolasCreche.size() + escolasRural.size() + escolasUrbana.size());
//			params.put("dtHoje", Util.convCalendarString3(Util.getDataAtual()));
//			params.put("hrHoje", Util.convCalendarStringHourMinute(Util.getDataAtual()));
//			params.put("nmUsuario", "tivic");
//			JasperPrint print = ReportServices.getJasperPrint(null, "acd/relatorio_alunos", params, rsm, null);
//			
//			File directory = new File(ContextManager.getRealPath() + "/relatorio_alunos");
//			if (!directory.exists()){
//				directory.mkdir();
//            }
//			
//			File pdf = new File(ContextManager.getRealPath() + "/relatorio_alunos/relatorio_alunos.pdf");
//			FileOutputStream pdfOutPut = new FileOutputStream(pdf);
//			JasperExportManager.exportReportToPdfStream(print, pdfOutPut);
//			pdfOutPut.close();
			
			
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
	
	public static Result findRelatorioPorZona(ArrayList<ItemComparator> criterios) {
		return findRelatorioPorZona(criterios, null);
	}

	/**
	 * Busca de relatÃ³rio separado por zona
	 * @param criterios
	 * @param connect
	 * @return
	 */
	public static Result findRelatorioPorZona(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Result result = findRelatorio(criterios, connect);
			ResultSetMap rsm = (ResultSetMap)result.getObjects().get("RSM");
						
			ArrayList<String> fields = new ArrayList<String>();
			
			fields.add("NM_ZONA");
			fields.add("NM_PESSOA");
			
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			result.addObject("RSM", rsm);
			
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
	
	public static Result findRelatorioPatologia(ArrayList<ItemComparator> criterios) {
		return findRelatorioPatologia(criterios, null);
	}

	/**
	 * Busca de relatÃ³rio separado por patologia
	 * @param criterios
	 * @param connect
	 * @return
	 */
	public static Result findRelatorioPatologia(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			criterios.add(new ItemComparator("lgPatologia", "" + 1, ItemComparator.EQUAL, Types.INTEGER));
			
			Result result = findRelatorio(criterios, connect);
			
			ResultSetMap rsm = (ResultSetMap) result.getObjects().get("RSM");
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_PATOLOGIA");
			fields.add("NM_ALUNO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			result.addObject("RSM", rsm);
			
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
	
	public static Result findRelatorioAlergia(ArrayList<ItemComparator> criterios) {
		return findRelatorioAlergia(criterios, null);
	}

	/**
	 * Busca de relatÃ³rio separado por alergia
	 * @param criterios
	 * @param connect
	 * @return
	 */
	public static Result findRelatorioAlergia(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			criterios.add(new ItemComparator("lgAlergia", "" + 1, ItemComparator.EQUAL, Types.INTEGER));
			
			Result result = findRelatorio(criterios, connect);
			
			ResultSetMap rsm = (ResultSetMap) result.getObjects().get("RSM");
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_ALERGIA");
			fields.add("NM_ALUNO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			result.addObject("RSM", rsm);
			
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
	
	public static Result findRelatorioAlunosTelefones(ArrayList<ItemComparator> criterios) {
		return findRelatorioAlunosTelefones(criterios, null);
	}

	/**
	 * Busca de relatÃ³rio mostrando os telefones
	 * @param criterios
	 * @param connect
	 * @return
	 */
	public static Result findRelatorioAlunosTelefones(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Result result = findRelatorio(criterios, connect);
			
			ResultSetMap rsm = (ResultSetMap) result.getObjects().get("RSM");
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_ZONA");
			fields.add("NM_INSTITUICAO");
			fields.add("NM_MODALIDADE_CURSO");
			fields.add("CL_TURMA");
			fields.add("NM_ALUNO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			result.addObject("RSM", rsm);
			
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
	
	public static Result findRelatorioBairroAlunos(ArrayList<ItemComparator> criterios) {
		return findRelatorioBairroAlunos(criterios, null);
	}

	/**
	 * Busca de relatÃ³rio separado por bairros
	 * @param criterios
	 * @param connect
	 * @return
	 */
	public static Result findRelatorioBairroAlunos(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int qtLimite = 0;
			String nmPessoa = "";
			int cdInstituicao = 0;
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			ResultSetMap rsmPeriodoLetivo = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdSecretaria, connect);
			int cdPeriodoLetivoSecretaria = 0;
			if(rsmPeriodoLetivo.next())
				cdPeriodoLetivoSecretaria = rsmPeriodoLetivo.getInt("cd_periodo_letivo");
			
			int lgAlunosProblemasCadastro = -1;
			boolean lgexigenciamatricula = false;
			int tpmodalidade = -1;
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.nm_pessoa")) {
					nmPessoa = Util.limparTexto(criterios.get(i).getValue());
					nmPessoa = "%"+nmPessoa.replaceAll(" ", "%")+"%";
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_INSTITUICAO")) {
					cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgAlunosProblemasCadastro")) {
					lgAlunosProblemasCadastro = (Integer.parseInt(criterios.get(i).getValue()) == 0 ? -1 : Integer.parseInt(criterios.get(i).getValue()) == 2 ? 0 : Integer.parseInt(criterios.get(i).getValue()));
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpmodalidade")) {
					tpmodalidade = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgexigenciamatricula")) {
					lgexigenciamatricula = true;
				}
				else
					crt.add(criterios.get(i));
				
			}
			
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
			String sql = "SELECT F.nm_pessoa, B.nm_bairro, C.nm_pessoa "
					+ "FROM acd_matricula 	 			A "
					+ "JOIN grl_pessoa_endereco 		B ON (A.cd_aluno = B.cd_pessoa) "
					+ "JOIN grl_pessoa 	 				C ON (A.cd_aluno = C.cd_pessoa) "
					+ "JOIN acd_turma		 			D ON (A.cd_turma = D.cd_turma) "
					+ "JOIN acd_instituicao				E ON (D.cd_instituicao = E.cd_instituicao) "
					+ "JOIN grl_pessoa	 	 			F ON (D.cd_instituicao = F.cd_pessoa) "
					+ "JOIN acd_instituicao_periodo		G ON (A.cd_periodo_letivo = G.cd_periodo_letivo) "
					+ "WHERE lg_principal = 1 "
					+ "AND A.st_matricula = 0 "
					+ "AND G.cd_periodo_letivo_superior = " + cdPeriodoLetivoSecretaria + " ";
			
			ResultSetMap rsm = Search.find(sql, "GROUP BY F.nm_pessoa, C.nm_pessoa, B.nm_bairro" + "ORDER BY F.nm_pessoa, B.nm_bairro, C.nm_pessoa" +sqlLimit[1], crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			int x = 0;

			ResultSetMap rsmCursosFundamental1 = ParametroServices.getValoresOfParametro("CD_CURSO_FUNDAMENTAL_1", connect);
			ResultSetMap rsmCursosFundamental2 = ParametroServices.getValoresOfParametro("CD_CURSO_FUNDAMENTAL_2", connect);
			while(rsm.next()){
				
				if(tpmodalidade == 1){
					boolean achado = false;
					while(rsmCursosFundamental1.next()){
						if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental1.getString("VL_REAL"))){
							achado = true;
							break;
						}
								
					}
					rsmCursosFundamental1.beforeFirst();
					if(!achado){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				else if(tpmodalidade == 2){
					boolean achado = false;
					while(rsmCursosFundamental2.next()){
						if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental2.getString("VL_REAL"))){
							achado = true;
							break;
						}
								
					}
					rsmCursosFundamental2.beforeFirst();
					if(!achado){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				
			}
			
			rsm.beforeFirst();			
			Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsm);
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
	
	public static Result findRelatorioDeficiencia(ArrayList<ItemComparator> criterios) {
		return findRelatorioDeficiencia(criterios, null);
	}

	/**
	 * Busca de relatÃ³rio separado por deficiencia
	 * @param criterios
	 * @param connect
	 * @return
	 */
	public static Result findRelatorioDeficiencia(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			criterios.add(new ItemComparator("lgDeficiencia", "1", ItemComparator.EQUAL, Types.INTEGER));
			Result result = findRelatorio(criterios, connect);
			
			ResultSetMap rsm = (ResultSetMap) result.getObjects().get("RSM");
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_DEFICIENCIA");
			fields.add("NM_ALUNO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			result.addObject("RSM", rsm);
			
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
	
	
	public static ResultSetMap findImportacao(ArrayList<ItemComparator> criterios) {
		return findImportacao(criterios, null);
	}

	public static ResultSetMap findImportacao(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			String sql =
		 			  "SELECT A.*, A.nm_pessoa AS nm_aluno, " +
 					  "   E.nr_cpf, E.cd_naturalidade, E.cd_escolaridade, E.dt_nascimento, E.sg_orgao_rg, E.nm_mae, E.nm_pai, E.tp_sexo, E.st_estado_civil, " +
 					  "   E.nr_rg, E.nr_cnh, E.dt_validade_cnh, E.dt_primeira_habilitacao, E.tp_categoria_habilitacao, E.tp_raca, E.lg_deficiente_fisico, " +
 					  "   E.nm_forma_tratamento, E.cd_estado_rg, E.dt_emissao_rg, E.sg_orgao_rg AS sg_orgao_rg_pessoa_fisica, E.cd_estado_rg AS cd_estado_rg_pessoa_fisica, " +
 					  "   E.qt_membros_familia, E.vl_renda_familiar_per_capta, " +
 					  "   P.cd_endereco, P.ds_endereco, P.cd_tipo_logradouro, P.cd_tipo_endereco, " +
 					  "   P.cd_logradouro, P.cd_bairro, P.cd_cidade, P.nm_logradouro, P.nm_bairro, P.nr_cep, " +
 					  "   P.nr_endereco, P.nm_complemento, P.nr_telefone, P.nm_ponto_referencia, P.lg_cobranca, P.lg_principal, " +
 					  "   W.* " +
 					  "FROM grl_pessoa A " +
 					  "LEFT OUTER JOIN grl_pessoa_fisica 	E ON (A.cd_pessoa = E.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_pessoa_endereco 	P ON (A.cd_pessoa = P.cd_pessoa AND P.lg_principal = 1) " +
 					  "JOIN acd_aluno 				W ON (A.cd_pessoa = W.cd_aluno) " +
 					  "WHERE 1 = 1 ";
			return Search.find(sql, "ORDER BY A.nm_pessoa ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
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
	 * Retorna as formaÃ§Ãµes acadÃªmicas de um aluno.
	 * 
	 * @param cdAluno CÃ³digo do aluno
	 * @return ResultSetMap com as formaÃ§Ãµes acadÃªmicas
	 */
	public static ResultSetMap getFormacaoAcademica(int cdAluno) {
		return getFormacaoAcademica(cdAluno, null);
	}
	
	public static ResultSetMap getFormacaoAcademica(int cdAluno, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			String sql = "SELECT DISTINCT(A.cd_formacao, A.cd_pessoa), A.*,"
					   + " B.cd_pessoa AS cd_agencia_financiadora, B.nm_pessoa AS nm_agencia_financiadora,"
					   + " C.cd_pessoa as cd_instituicao, C.nm_pessoa as nm_instituicao"
					   + " FROM acd_formacao A"
					   + " LEFT OUTER JOIN grl_pessoa B ON (A.cd_agencia_financiadora = B.cd_pessoa)"
					   + " LEFT OUTER JOIN grl_pessoa C ON (A.cd_instituicao = C.cd_pessoa)"
					   + " JOIN            grl_pessoa D ON (A.cd_pessoa = D.cd_pessoa AND A.cd_pessoa = "+cdAluno+")";
			
			PreparedStatement pstmt = connect.prepareStatement(sql);
									
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm;
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
	
	public static Result gerarRelatorioAlunos(int cdEmpresa, String nome, int nrAno){
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
	
	public static Result saveBolsaFamilia(int cdAluno, int lgBolsaFamilia){
		return saveBolsaFamilia(cdAluno, lgBolsaFamilia, null);
	}
	
	/**
	 * Salvamento do registro de bolsa familia em um determinado aluno
	 * @param cdAluno
	 * @param lgBolsaFamilia
	 * @param connect
	 * @return
	 */
	public static Result saveBolsaFamilia(int cdAluno, int lgBolsaFamilia, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Aluno aluno = AlunoDAO.get(cdAluno, connect);
			aluno.setLgBolsaFamilia(lgBolsaFamilia);
			if(AlunoDAO.update(aluno, connect) <= 0){
				return new Result(-1, "Erro ao atualizar bolsa familia de aluno");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "AlteraÃ§Ã£o realizada com sucesso");
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na evasÃ£o");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getUltimaMatricula(int cdAluno) {
		return getUltimaMatricula(cdAluno, null);
	}

	/**
	 * Buscar a matrÃ­cula mais recente de um aluno
	 * @param cdAluno
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getUltimaMatricula(int cdAluno, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT C.nm_pessoa AS nm_aluno, B.cd_periodo_letivo, INS_C.cd_pessoa AS cd_instituicao, INS_C.nm_pessoa AS nm_instituicao, A.nr_matricula, D.nm_mae, D.dt_nascimento, E.nm_cidade AS nm_naturalidade, F.sg_estado AS sg_estado_naturalidade, H.nm_produto_servico AS nm_curso_atual, B.nm_periodo_letivo, J.nm_produto_servico AS nm_curso_referencia "
					+ "						  FROM acd_matricula A  "
					+ "						    JOIN acd_instituicao_periodo B ON (A.cd_periodo_letivo = B.cd_periodo_letivo) "
					+ "						    JOIN grl_pessoa C ON (A.cd_aluno = C.cd_pessoa) "
					+ "						    JOIN grl_pessoa INS_C ON (B.cd_instituicao = INS_C.cd_pessoa) "
					+ "						    JOIN grl_pessoa_fisica D ON (A.cd_aluno = D.cd_pessoa) "
					+ "						    JOIN grl_cidade E ON (D.cd_naturalidade = E.cd_cidade) "
					+ "						    JOIN grl_estado F ON (E.cd_estado = F.cd_estado) "
					+ "						    JOIN acd_curso G ON (A.cd_curso = G.cd_curso) "
					+ "						    JOIN grl_produto_servico H ON (G.cd_curso = H.cd_produto_servico) "
					+ "						    LEFT OUTER JOIN acd_curso I ON (G.nr_ordem = I.nr_ordem AND I.lg_referencia = 1) "
					+ "						    LEFT OUTER JOIN grl_produto_servico J ON (I.cd_curso = J.cd_produto_servico) "
					+ "							WHERE A.st_matricula NOT IN ("+MatriculaServices.ST_CANCELADO+") "
					+ "							  AND A.cd_aluno = " + cdAluno
					+ "							ORDER BY A.dt_matricula DESC, A.cd_matricula DESC LIMIT 1");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(rsm.getInt("cd_instituicao"), connect);
				int cdPeriodoRecente = 0;
				if(rsmPeriodoRecente.next())
					cdPeriodoRecente = rsmPeriodoRecente.getInt("cd_periodo_letivo");
				
				if(rsm.getInt("cd_periodo_letivo") == cdPeriodoRecente){
					rsm.setValueToField("MESMO_PERIODO", 1);
				}
				else{
					rsm.setValueToField("MESMO_PERIODO", 0);
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Testa os nomes do aluno, da mae e do pai para saber se sÃ£o valido, nÃ£o possuindo caracteres especiais nem nÃºmeros
	 * @param nmAluno
	 * @param nmMae
	 * @param nmPai
	 * @return
	 */
	public static boolean isNomesValidos(String nmAluno, String nmMae, String nmPai) {
		try {
			String[] partesNome = nmAluno.split(" ");
			for(String parteNome : partesNome){
				if(parteNome.length() <= 1){
					return false;
				}
				
				if(parteNome.contains(",") || parteNome.contains("\\.") || parteNome.contains(";") || parteNome.contains("-") || parteNome.contains(":") || parteNome.contains("'") || parteNome.contains("*") || parteNome.contains("+") || parteNome.contains("/")
				|| parteNome.contains("0") || parteNome.contains("1") || parteNome.contains("2") || parteNome.contains("3") || parteNome.contains("4") || parteNome.contains("5") || parteNome.contains("6") || parteNome.contains("7") || parteNome.contains("8") || parteNome.contains("9")){
					return false;
				}
			}
			
			partesNome = nmMae.split(" ");
			for(String parteNome : partesNome){
				if(parteNome.length() <= 1){
					return false;
				}
				
				if(parteNome.contains(",") || parteNome.contains("\\.") || parteNome.contains(";") || parteNome.contains("-") || parteNome.contains(":") || parteNome.contains("'") || parteNome.contains("*") || parteNome.contains("+") || parteNome.contains("/")
				|| parteNome.contains("0") || parteNome.contains("1") || parteNome.contains("2") || parteNome.contains("3") || parteNome.contains("4") || parteNome.contains("5") || parteNome.contains("6") || parteNome.contains("7") || parteNome.contains("8") || parteNome.contains("9")){
					return false;
				}
			}
			
			if(nmPai != null && !nmPai.equals("")){
				partesNome = nmPai.split(" ");
				for(String parteNome : partesNome){
					if(parteNome.length() <= 1){
						return false;
					}
					
					if(parteNome.contains(",") || parteNome.contains("\\.") || parteNome.contains(";") || parteNome.contains("-") || parteNome.contains(":") || parteNome.contains("'") || parteNome.contains("*") || parteNome.contains("+") || parteNome.contains("/")
					|| parteNome.contains("0") || parteNome.contains("1") || parteNome.contains("2") || parteNome.contains("3") || parteNome.contains("4") || parteNome.contains("5") || parteNome.contains("6") || parteNome.contains("7") || parteNome.contains("8") || parteNome.contains("9")){
						return false;
					}
				}
			}
			
			
			return true;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoServices.getAll: " + e);
			return false;
		}
		
	}
	
	public static Result possuiDeficiencia(int cdAluno, boolean possuiDeficiencia) {
		return possuiDeficiencia(cdAluno, possuiDeficiencia, null);
	}

	/**
	 * Saber se o lgPossuiDeficiencia do aluno estÃ¡ condizente com os registros do aluno de deficiencia
	 * @param cdAluno
	 * @param possuiDeficiencia
	 * @param connect
	 * @return
	 */
	public static Result possuiDeficiencia(int cdAluno, boolean possuiDeficiencia, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_necessidade_especial WHERE cd_pessoa = " + cdAluno);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()){
				if(possuiDeficiencia)
					return new Result(1);
				else
					return new Result(-1, "Se o(a) aluno(a) possui necessidade especial, marque \"Sim\" na opÃ§Ã£o de \"Possui necessidade especial\"");
			}
			else{
				if(possuiDeficiencia)
					return new Result(-1, "Se o(a) aluno(a) nÃ£o possui necessidade especial, marque \"NÃ£o\" na opÃ§Ã£o de \"Possui necessidade especial\"");
				else
					return new Result(1);
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoServices.getAll: " + e);
			return new Result(-1, "Erro ao tentar salvar a matrÃ­cula. Contate os desenvolvedores");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result findRelatorioEjaEgressosUltimoAno(ArrayList<ItemComparator> criterios){
		String nmUrlDatabase = "jdbc:postgresql://127.0.0.1/edf_centaurus";
		String nmLoginDataBase = "postgres";
		String nmSenhaDatBase = "CDF3DP!";
		
		return findRelatorioEjaEgressosUltimoAno2(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
	}
	public static Result findRelatorioEjaEgressosUltimoAno(String nmUrlDatabase, String nmLoginDataBase, String nmSenhaDatBase){
		//Faz a conexao com o Local
		Connection connectLocal = Conexao.conectar();
		try{
			connectLocal.setAutoCommit(false);
			

			Connection connectServer = Conexao.conectar(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
			
			try{
				
				connectServer.setAutoCommit(false);


				int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdSecretaria, connectLocal);
				int cdPeriodoLetivoAtual = 0;
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
				InstituicaoPeriodo periodoLetivoAtual = InstituicaoPeriodoDAO.get(cdPeriodoLetivoAtual, connectLocal);
				
				
				
				ResultSetMap rsmAlunosEjaPeriodoAtual = new ResultSetMap(connectLocal.prepareStatement("SELECT P.cd_pessoa, P.nm_pessoa, P.nm_pessoa AS nm_aluno, P.nr_telefone1, P.nr_telefone2, P.nr_celular, PF.dt_nascimento, PF.nm_mae, PF.nm_pai, A.nm_responsavel, F.cd_cidade, F.nm_logradouro, F.nm_bairro, F.nr_endereco, F.nm_complemento, F.nr_telefone, F.tp_zona AS tp_zona_aluno, F.nr_cep, G.nm_cidade, " 
		 			    +"	  																						A.cd_aluno, A.lg_bolsa_familia, A.lg_pai_nao_declarado, A.lg_cadastro_problema, B.cd_matricula, B.nr_matricula, B.st_matricula, B.dt_matricula, PF.cd_naturalidade, IP.nm_periodo_letivo, C.nr_idade,  "
		 			    +"																							H.nm_produto_servico AS nm_curso  FROM grl_pessoa P "
						+ "																			      LEFT OUTER JOIN grl_pessoa_tipo_documentacao PTD ON (P.cd_pessoa = PTD.cd_pessoa AND PTD.cd_tipo_documentacao IN ("+PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_NASCIMENTO+", "+PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_CASAMENTO+")), "
						+ "																				grl_pessoa_fisica PF"
						+ "																				  LEFT OUTER JOIN grl_estado ERG ON (PF.cd_estado_rg = ERG.cd_estado), "
						+ "																				acd_aluno A, acd_matricula B, acd_instituicao_periodo IP, acd_curso C, acd_curso_etapa D, acd_tipo_etapa E, grl_pessoa_endereco F, grl_cidade G, grl_produto_servico H "
						+ "																					WHERE P.cd_pessoa = A.cd_aluno "
						+ "																					  AND P.cd_pessoa = PF.cd_pessoa "
						+ "																					  AND A.cd_aluno = B.cd_aluno "
						+ "																					  AND B.cd_periodo_letivo = IP.cd_periodo_letivo "
						+ "																					  AND IP.nm_periodo_letivo = '"+periodoLetivoAtual.getNmPeriodoLetivo()+"' "
						+ "																					  AND B.cd_curso = C.cd_curso "
						+ "																					  AND C.cd_curso = D.cd_curso "
						+ "																					  AND D.cd_etapa = E.cd_etapa "
						+ "																					  AND P.cd_pessoa = F.cd_pessoa "
						+ "																					  AND F.lg_principal = 1 "
						+ "																					  AND F.cd_cidade = G.cd_cidade "
						+ "																					  AND C.cd_curso = H.cd_produto_servico "
						+ "																					  AND E.lg_eja = 1 "
						+ "																					  AND st_matricula = "+MatriculaServices.ST_ATIVA+" ORDER BY P.nm_pessoa ").executeQuery());
				 
				
				
				System.out.println("rsmAlunosEjaPeriodoAtual.size = " + rsmAlunosEjaPeriodoAtual.size());
				int cont = 0;
				ResultSetMap rsmFinal = new ResultSetMap();
				while(rsmAlunosEjaPeriodoAtual.next()){
					
					PreparedStatement pstmt = connectServer.prepareStatement("SELECT * FROM aluno A"
							+ "														LEFT OUTER JOIN uf U ON (A.id_uf_identidade = U.id_uf), "
							+ "													matricula B, escola_modalidade_smed C, modalidade_smed D, modalidade E "
							+ "																							WHERE A.id_aluno = B.id_aluno "
							+ "																							  AND B.ano_matricula = "+(Integer.parseInt(periodoLetivoAtual.getNmPeriodoLetivo())-1)
							+ "																							  AND B.id_escola_modalidade_smed = C.id_escola_modalidade_smed "
							+ "                   																		  AND C.id_modalidade_smed = D.id_modalidade_smed "
							+ "																							  AND D.id_modalidade = E.id_modalidade "
							+ "																							  AND ((A.nm_aluno_search = ? AND A.nm_mae_search = ? AND (A.dt_nascimento = '"+Util.convCalendarStringSql(rsmAlunosEjaPeriodoAtual.getGregorianCalendar("dt_nascimento"))+"' OR A.nm_pai_search = ?)) OR (nr_identidade IS NOT NULL AND nr_identidade <> '' AND nr_identidade = ? AND U.cod_uf = ?) OR (cert_nr_regristo IS NOT NULL AND cert_nr_regristo <> '' AND cert_nr_regristo = ?) OR (cert_nr_termo IS NOT NULL AND cert_nr_termo <> '' AND cert_nr_termo = ? AND cert_nr_folha = ? AND cert_nr_livro = ?) OR (nr_cpf IS NOT NULL AND nr_cpf <> '' AND nr_cpf = ?)) "
							+ "																							  AND E.id_modalidade = 3 ORDER BY nm_aluno_search");

					
					pstmt.setString(1, rsmAlunosEjaPeriodoAtual.getString("nm_pessoa").replaceAll("Ã‡", "C").trim());
					pstmt.setString(2, rsmAlunosEjaPeriodoAtual.getString("nm_mae").replaceAll("Ã‡", "C").trim());
					pstmt.setString(3, (rsmAlunosEjaPeriodoAtual.getString("nm_pai") != null && !rsmAlunosEjaPeriodoAtual.getString("nm_pai").equals("NÃƒO DECLARADO") ? rsmAlunosEjaPeriodoAtual.getString("nm_pai").replaceAll("Ã‡", "C").trim() : null));
					pstmt.setString(4, rsmAlunosEjaPeriodoAtual.getString("nr_rg"));
					pstmt.setInt(5, (rsmAlunosEjaPeriodoAtual.getString("id_estado") != null ? Integer.parseInt(rsmAlunosEjaPeriodoAtual.getString("id_estado")) : 0));
					pstmt.setString(6, rsmAlunosEjaPeriodoAtual.getString("nr_documento"));
					pstmt.setString(7, rsmAlunosEjaPeriodoAtual.getString("nr_documento"));
					pstmt.setString(8, rsmAlunosEjaPeriodoAtual.getString("folha"));
					pstmt.setString(9, rsmAlunosEjaPeriodoAtual.getString("livro"));
					pstmt.setString(10, rsmAlunosEjaPeriodoAtual.getString("nr_cpf"));
					
					ResultSetMap rsmAlunosEjaPeriodoAnterior = new ResultSetMap(pstmt.executeQuery());
					if(!rsmAlunosEjaPeriodoAnterior.next()){
						rsmFinal.addRegister(rsmAlunosEjaPeriodoAtual.getRegister());
					}
					
				}
				
				System.out.println("Quantidade de alunos possivelmente duplicados: " + cont);
				
				rsmFinal.beforeFirst();
				
				
				int qtMatriculadosZonaUrbana = 0;
				int qtMatriculadosZonaRural = 0;
				int qtMatriculadosCreche = 0;
				ArrayList<Integer> codigosAluno = new ArrayList<Integer>();
				int x = 0;
				while(rsmFinal.next()){
					
					rsmFinal.setValueToField("NM_ZONA_ALUNO", PessoaEnderecoServices.tiposZona[rsmFinal.getInt("tp_zona_aluno")]);
					
					GregorianCalendar dtNascimento = rsmFinal.getGregorianCalendar("dt_nascimento");
					
					Cidade naturalidade = CidadeDAO.get(rsmFinal.getInt("cd_naturalidade"), connectLocal);
					if(naturalidade != null){
						rsmFinal.setValueToField("NM_NATURALIDADE", naturalidade.getNmCidade());
						Estado estadoNaturalidade = EstadoDAO.get(naturalidade.getCdEstado(), connectLocal);
						if(estadoNaturalidade != null)
							rsmFinal.setValueToField("SG_NATURALIDADE", estadoNaturalidade.getSgEstado());
					}
					rsmFinal.setValueToField("NR_TELEFONE", (rsmFinal.getString("NR_TELEFONE1") != null && !rsmFinal.getString("NR_TELEFONE1").trim().equals("") ? rsmFinal.getString("NR_TELEFONE1") : (rsmFinal.getString("NR_TELEFONE2") != null && !rsmFinal.getString("NR_TELEFONE2").trim().equals("") ? rsmFinal.getString("NR_TELEFONE2") : (rsmFinal.getString("NR_CELULAR") != null && !rsmFinal.getString("NR_CELULAR").trim().equals("") ? rsmFinal.getString("NR_CELULAR") : rsmFinal.getString("NR_TELEFONE")))));
					
					int nrIdadeAluno = Util.getIdade(dtNascimento);
					if(dtNascimento != null)
						rsmFinal.setValueToField("DT_NM_NASCIMENTO", (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.DAY_OF_MONTH))) > 9 ? dtNascimento.get(Calendar.DAY_OF_MONTH) : "0" + dtNascimento.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1)) + "/" + dtNascimento.get(Calendar.YEAR) + " (" + nrIdadeAluno + " anos)");
					
					rsmFinal.setValueToField("NR_IDADE_ALUNO", nrIdadeAluno);
					
					int nrDivergencia = rsmFinal.getInt("nr_idade") - nrIdadeAluno;
					
					rsmFinal.setValueToField("NR_DIVERGENCIA", (nrDivergencia < 0 ? (-1) * nrDivergencia : nrDivergencia));
					
					if(rsmFinal.getGregorianCalendar("dt_matricula") != null){
						rsmFinal.setValueToField("NR_MES", rsmFinal.getGregorianCalendar("dt_matricula").get(Calendar.MONTH));
						rsmFinal.setValueToField("NR_ANO", rsmFinal.getGregorianCalendar("dt_matricula").get(Calendar.YEAR));
						rsmFinal.setValueToField("NM_MES", Util.meses[rsmFinal.getInt("NR_MES")] + "/" + rsmFinal.getInt("NR_ANO"));
					}
					
					
					if(codigosAluno.size() > 0 && codigosAluno.contains(rsmFinal.getInt("cd_pessoa"))){
						rsmFinal.deleteRow();
						if(x == 0)
							rsmFinal.beforeFirst();
						else
							rsmFinal.previous();
						continue;
					}
					else{
						codigosAluno.add(rsmFinal.getInt("cd_pessoa"));
					}
					
					
					Matricula matricula = MatriculaDAO.get(rsmFinal.getInt("cd_matricula"), connectLocal);
					if(matricula != null){
						Turma turma = TurmaDAO.get(matricula.getCdTurma(), connectLocal);
						if(turma != null){
							rsmFinal.setValueToField("CL_TURMA", turma.getNmTurma() + " - " + TurmaServices.tiposTurno[turma.getTpTurno()]);
							
							rsmFinal.setValueToField("CL_TURNO", TurmaServices.tiposTurno[turma.getTpTurno()]);
							rsmFinal.setValueToField("TP_TURNO", turma.getTpTurno());
							
							Curso curso = CursoDAO.get(turma.getCdCurso(), connectLocal);
							if(curso != null){
								rsmFinal.setValueToField("CD_CURSO", curso.getCdCurso());
								rsmFinal.setValueToField("ID_PRODUTO_SERVICO_CURSO", curso.getIdProdutoServico());
								rsmFinal.setValueToField("NM_MODALIDADE_CURSO", curso.getNmProdutoServico());
							}
							
							Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connectLocal);
							if(instituicao != null){
								rsmFinal.setValueToField("NM_INSTITUICAO", instituicao.getNmPessoa());
								
//								PessoaEndereco instituicaoEndereco = PessoaEnderecoDAO.get(1, instituicao.getCdInstituicao(), connect);
//								if(instituicaoEndereco != null)
									
								
								
								InstituicaoEducacenso instituicaoEducacenso = InstituicaoEducacensoDAO.get(instituicao.getCdInstituicao(), matricula.getCdPeriodoLetivo(), connectLocal);
								if(instituicaoEducacenso != null){
									
									rsmFinal.setValueToField("NM_ZONA", PessoaEnderecoServices.tiposZona[instituicaoEducacenso.getTpLocalizacao()]);
									
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
						}
					}
					else{
						rsmFinal.deleteRow();
						if(x == 0)
							rsmFinal.beforeFirst();
						else
							rsmFinal.previous();
						continue;
					}
					x++;
				}
				rsmFinal.beforeFirst();
				
				ArrayList<String> fields = new ArrayList<String>();
				
				fields.add("NM_ZONA");
				fields.add("NM_INSTITUICAO");
				fields.add("ID_PRODUTO_SERVICO_CURSO");
				fields.add("CL_TURMA");
				fields.add("NM_PESSOA");
				
				rsmFinal.orderBy(fields);
				rsmFinal.beforeFirst();
				
				//Da commit em ambos
				connectLocal.commit();
				connectServer.commit();
				
				Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsmFinal);
				result.addObject("QT_MATRICULADOS_ZONA_URBANA", qtMatriculadosZonaUrbana);
				result.addObject("QT_MATRICULADOS_ZONA_RURAL", qtMatriculadosZonaRural);
				result.addObject("QT_MATRICULADOS_CRECHE", qtMatriculadosCreche);
				return result;
				
			}
			catch(Exception e){
				Conexao.rollback(connectLocal);
				Conexao.rollback(connectServer);
				//registra log de erro quando a classe Ã© utilizad pelo pdv
				Util.registerLog(e);
				e.printStackTrace();
				return new Result(-1, "Erro na importaÃ§Ã£o");
			}
			finally{
				Conexao.desconectar(connectServer);
			}
		}
		
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connectLocal);
			return new Result(-1, "Erro na sincronizaÃ§Ã£o");
		}
		finally{
			Conexao.desconectar(connectLocal);
		}
	}

	public static Result findRelatorioEjaEgressosUltimoAno2(ArrayList<ItemComparator> criterios){
		String nmUrlDatabase = "jdbc:postgresql://127.0.0.1/edf_centaurus";
		String nmLoginDataBase = "postgres";
		String nmSenhaDatBase = "CDF3DP!";
		
		return findRelatorioEjaEgressosUltimoAno2(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
	}
	public static Result findRelatorioEjaEgressosUltimoAno2(String nmUrlDatabase, String nmLoginDataBase, String nmSenhaDatBase){
		//Faz a conexao com o Local
		Connection connectLocal = Conexao.conectar();
		try{
			connectLocal.setAutoCommit(false);
			

			Connection connectServer = Conexao.conectar(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
			
			try{
				
				connectServer.setAutoCommit(false);


				int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdSecretaria, connectLocal);
				int cdPeriodoLetivoAtual = 0;
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
				InstituicaoPeriodo periodoLetivoAtual = InstituicaoPeriodoDAO.get(cdPeriodoLetivoAtual, connectLocal);
				
				
				
				ResultSetMap rsmAlunosEjaPeriodoAtual = new ResultSetMap(connectLocal.prepareStatement("SELECT P.cd_pessoa, P.nm_pessoa, P.nm_pessoa AS nm_aluno, P.nr_telefone1, P.nr_telefone2, P.nr_celular, PF.dt_nascimento, PF.nm_mae, PF.nm_pai, A.nm_responsavel, F.cd_cidade, F.nm_logradouro, F.nm_bairro, F.nr_endereco, F.nm_complemento, F.nr_telefone, F.tp_zona AS tp_zona_aluno, F.nr_cep, G.nm_cidade, " 
		 			    +"	  																						A.cd_aluno, A.lg_bolsa_familia, A.lg_pai_nao_declarado, A.lg_cadastro_problema, B.cd_matricula, B.nr_matricula, B.st_matricula, B.dt_matricula, PF.cd_naturalidade, IP.nm_periodo_letivo, C.nr_idade,  "
		 			    +"																							H.nm_produto_servico AS nm_curso  FROM grl_pessoa P "
						+ "																			      LEFT OUTER JOIN grl_pessoa_tipo_documentacao PTD ON (P.cd_pessoa = PTD.cd_pessoa AND PTD.cd_tipo_documentacao IN ("+PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_NASCIMENTO+", "+PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_CASAMENTO+")), "
						+ "																				grl_pessoa_fisica PF"
						+ "																				  LEFT OUTER JOIN grl_estado ERG ON (PF.cd_estado_rg = ERG.cd_estado), "
						+ "																				acd_aluno A, acd_matricula B, acd_instituicao_periodo IP, acd_curso C, acd_curso_etapa D, acd_tipo_etapa E, grl_pessoa_endereco F, grl_cidade G, grl_produto_servico H "
						+ "																					WHERE P.cd_pessoa = A.cd_aluno "
						+ "																					  AND P.cd_pessoa = PF.cd_pessoa "
						+ "																					  AND A.cd_aluno = B.cd_aluno "
						+ "																					  AND B.cd_periodo_letivo = IP.cd_periodo_letivo "
						+ "																					  AND IP.nm_periodo_letivo = '"+periodoLetivoAtual.getNmPeriodoLetivo()+"' "
						+ "																					  AND B.cd_curso = C.cd_curso "
						+ "																					  AND C.cd_curso = D.cd_curso "
						+ "																					  AND D.cd_etapa = E.cd_etapa "
						+ "																					  AND P.cd_pessoa = F.cd_pessoa "
						+ "																					  AND F.lg_principal = 1 "
						+ "																					  AND F.cd_cidade = G.cd_cidade "
						+ "																					  AND C.cd_curso = H.cd_produto_servico "
						+ "																					  AND E.lg_eja = 1 "
						+ "																					  AND st_matricula = "+MatriculaServices.ST_ATIVA+" ORDER BY P.nm_pessoa ").executeQuery());
				 
				
				
//				System.out.println("rsmAlunosEjaPeriodoAtual.size = " + rsmAlunosEjaPeriodoAtual.size());
//				int cont = 0;
				ResultSetMap rsmFinal = new ResultSetMap();
				while(rsmAlunosEjaPeriodoAtual.next()){
					
//					PreparedStatement pstmt = connectServer.prepareStatement("SELECT * FROM aluno A"
//							+ "														LEFT OUTER JOIN uf U ON (A.id_uf_identidade = U.id_uf), "
//							+ "													matricula B, escola_modalidade_smed C, modalidade_smed D, modalidade E "
//							+ "																							WHERE A.id_aluno = B.id_aluno "
//							+ "																							  AND B.ano_matricula = "+(Integer.parseInt(periodoLetivoAtual.getNmPeriodoLetivo())-1)
//							+ "																							  AND B.id_escola_modalidade_smed = C.id_escola_modalidade_smed "
//							+ "                   																		  AND C.id_modalidade_smed = D.id_modalidade_smed "
//							+ "																							  AND D.id_modalidade = E.id_modalidade "
//							+ "																							  AND ((A.nm_aluno_search = ? AND A.nm_mae_search = ? AND (A.dt_nascimento = '"+Util.convCalendarStringSql(rsmAlunosEjaPeriodoAtual.getGregorianCalendar("dt_nascimento"))+"' OR A.nm_pai_search = ?)) OR (nr_identidade IS NOT NULL AND nr_identidade <> '' AND nr_identidade = ? AND U.cod_uf = ?) OR (cert_nr_regristo IS NOT NULL AND cert_nr_regristo <> '' AND cert_nr_regristo = ?) OR (cert_nr_termo IS NOT NULL AND cert_nr_termo <> '' AND cert_nr_termo = ? AND cert_nr_folha = ? AND cert_nr_livro = ?) OR (nr_cpf IS NOT NULL AND nr_cpf <> '' AND nr_cpf = ?)) "
//							+ "																							  AND E.id_modalidade = 3 ORDER BY nm_aluno_search");
//
//					
//					pstmt.setString(1, rsmAlunosEjaPeriodoAtual.getString("nm_pessoa").replaceAll("Ã‡", "C").trim());
//					pstmt.setString(2, rsmAlunosEjaPeriodoAtual.getString("nm_mae").replaceAll("Ã‡", "C").trim());
//					pstmt.setString(3, (rsmAlunosEjaPeriodoAtual.getString("nm_pai") != null && !rsmAlunosEjaPeriodoAtual.getString("nm_pai").equals("NÃƒO DECLARADO") ? rsmAlunosEjaPeriodoAtual.getString("nm_pai").replaceAll("Ã‡", "C").trim() : null));
//					pstmt.setString(4, rsmAlunosEjaPeriodoAtual.getString("nr_rg"));
//					pstmt.setInt(5, (rsmAlunosEjaPeriodoAtual.getString("id_estado") != null ? Integer.parseInt(rsmAlunosEjaPeriodoAtual.getString("id_estado")) : 0));
//					pstmt.setString(6, rsmAlunosEjaPeriodoAtual.getString("nr_documento"));
//					pstmt.setString(7, rsmAlunosEjaPeriodoAtual.getString("nr_documento"));
//					pstmt.setString(8, rsmAlunosEjaPeriodoAtual.getString("folha"));
//					pstmt.setString(9, rsmAlunosEjaPeriodoAtual.getString("livro"));
//					pstmt.setString(10, rsmAlunosEjaPeriodoAtual.getString("nr_cpf"));
					PreparedStatement pstmt = connectLocal.prepareStatement("SELECT P.cd_pessoa  FROM grl_pessoa P, grl_pessoa_fisica PF, acd_aluno A, acd_matricula B, acd_instituicao_periodo IP, acd_curso_etapa D, acd_tipo_etapa E "
							+ "																					WHERE P.cd_pessoa = A.cd_aluno "
							+ "																					  AND P.cd_pessoa = PF.cd_pessoa "
							+ "																					  AND A.cd_aluno = B.cd_aluno "
							+ "																					  AND B.cd_periodo_letivo = IP.cd_periodo_letivo "
							+ "																					  AND IP.nm_periodo_letivo = '"+(Integer.parseInt(periodoLetivoAtual.getNmPeriodoLetivo())-1)+"' "
							+ "																					  AND B.cd_curso = D.cd_curso "
							+ "																					  AND D.cd_etapa = E.cd_etapa "
							+ "																					  AND E.lg_eja = 1 "
							+ "																					  AND P.cd_pessoa = ?"
							+ "																					  AND st_matricula <> "+MatriculaServices.ST_INATIVO+" ORDER BY P.nm_pessoa ");

					
					pstmt.setInt(1, rsmAlunosEjaPeriodoAtual.getInt("cd_pessoa"));
					ResultSetMap rsmAlunosEjaPeriodoAnterior = new ResultSetMap(pstmt.executeQuery());
					if(!rsmAlunosEjaPeriodoAnterior.next()){
						rsmFinal.addRegister(rsmAlunosEjaPeriodoAtual.getRegister());
						
						
//						pstmt = connectServer.prepareStatement("SELECT * FROM aluno A"
//								+ "														LEFT OUTER JOIN uf U ON (A.id_uf_identidade = U.id_uf), "
//								+ "													matricula B, escola_modalidade_smed C, modalidade_smed D, modalidade E "
//								+ "																							WHERE A.id_aluno = B.id_aluno "
//								+ "																							  AND B.ano_matricula = "+(Integer.parseInt(periodoLetivoAtual.getNmPeriodoLetivo())-1)
//								+ "																							  AND B.id_escola_modalidade_smed = C.id_escola_modalidade_smed "
//								+ "                   																		  AND C.id_modalidade_smed = D.id_modalidade_smed "
//								+ "																							  AND D.id_modalidade = E.id_modalidade "
////								+ "																							  AND A.nm_aluno_search = ?"
//								+ "																							  ORDER BY nm_aluno_search");
//			
//						
////						pstmt.setString(1, rsmAlunosEjaPeriodoAtual.getString("nm_pessoa").replaceAll("Ã‡", "C").trim());
//						ResultSetMap rsmCentaurus = new ResultSetMap(pstmt.executeQuery());
//						boolean achado = false;
//						while(rsmCentaurus.next()){
//							
//							if(rsmAlunosEjaPeriodoAtual.getString("nm_pessoa") == null || rsmAlunosEjaPeriodoAtual.getString("nm_pessoa").equals("")){
//								break;
//							}
//							if(rsmAlunosEjaPeriodoAtual.getString("nm_mae") == null || rsmAlunosEjaPeriodoAtual.getString("nm_mae").equals("")){
//								break;
//							}
//							if(rsmAlunosEjaPeriodoAtual.getGregorianCalendar("dt_nascimento") == null){
//								break;
//							}
//							
//							if(rsmCentaurus.getString("nm_aluno_search") == null || rsmCentaurus.getString("nm_aluno_search").equals("")){
//								continue;
//							}
//							if(rsmCentaurus.getString("nm_mae_search") == null || rsmCentaurus.getString("nm_mae_search").equals("")){
//								continue;
//							}
//							if(rsmCentaurus.getGregorianCalendar("dt_nascimento") == null){
//								continue;
//							}
//							
//							double porcentagem = Util.compareStrings(rsmAlunosEjaPeriodoAtual.getString("nm_pessoa").trim(), rsmAlunosEjaPeriodoAtual.getString("nm_mae").trim(), rsmAlunosEjaPeriodoAtual.getGregorianCalendar("dt_nascimento"), rsmCentaurus.getString("nm_aluno_search").trim(), rsmCentaurus.getString("nm_mae_search").trim(), rsmCentaurus.getGregorianCalendar("dt_nascimento"));
//							if(porcentagem > 0.8){
//								achado = true;
//								break;
//							}
//						}
//						if(achado){
//							cont++;
//							System.out.println("Nome no sistema: " + rsmAlunosEjaPeriodoAtual.getString("nm_pessoa").replaceAll("Ã‡", "C").trim());
//							System.out.println("Data de Nascimento no sistema: " + Util.convCalendarStringCompleto(rsmAlunosEjaPeriodoAtual.getGregorianCalendar("dt_nascimento")));
//							System.out.println("Nome da mÃ£e no sistema: " + rsmAlunosEjaPeriodoAtual.getString("nm_mae").replaceAll("Ã‡", "C").trim());
//							System.out.println("\tNome no centaurus: " + rsmCentaurus.getString("nm_aluno_search").replaceAll("Ã‡", "C").trim());
//							System.out.println("\tData de Nascimento no centaurus: " + Util.convCalendarStringCompleto(rsmCentaurus.getGregorianCalendar("dt_nascimento")));
//							System.out.println("\tNome da mÃ£e no centaurus: " + rsmCentaurus.getString("nm_mae_search").replaceAll("Ã‡", "C").trim());
//							System.out.println("\tNome da modalidade no centaurus: " + rsmCentaurus.getString("nm_modalidade_smed").replaceAll("Ã‡", "C").trim());
//							System.out.println();
//							System.out.println();
//						}
					}
				}
				
//				System.out.println("Quantidade de alunos possivelmente duplicados: " + cont);
				
				rsmFinal.beforeFirst();
				
				
				int qtMatriculadosZonaUrbana = 0;
				int qtMatriculadosZonaRural = 0;
				int qtMatriculadosCreche = 0;
				ArrayList<Integer> codigosAluno = new ArrayList<Integer>();
				int x = 0;
				while(rsmFinal.next()){
					
					rsmFinal.setValueToField("NM_ZONA_ALUNO", PessoaEnderecoServices.tiposZona[rsmFinal.getInt("tp_zona_aluno")]);
					
					GregorianCalendar dtNascimento = rsmFinal.getGregorianCalendar("dt_nascimento");
					
					Cidade naturalidade = CidadeDAO.get(rsmFinal.getInt("cd_naturalidade"), connectLocal);
					if(naturalidade != null){
						rsmFinal.setValueToField("NM_NATURALIDADE", naturalidade.getNmCidade());
						Estado estadoNaturalidade = EstadoDAO.get(naturalidade.getCdEstado(), connectLocal);
						if(estadoNaturalidade != null)
							rsmFinal.setValueToField("SG_NATURALIDADE", estadoNaturalidade.getSgEstado());
					}
					rsmFinal.setValueToField("NR_TELEFONE", (rsmFinal.getString("NR_TELEFONE1") != null && !rsmFinal.getString("NR_TELEFONE1").trim().equals("") ? rsmFinal.getString("NR_TELEFONE1") : (rsmFinal.getString("NR_TELEFONE2") != null && !rsmFinal.getString("NR_TELEFONE2").trim().equals("") ? rsmFinal.getString("NR_TELEFONE2") : (rsmFinal.getString("NR_CELULAR") != null && !rsmFinal.getString("NR_CELULAR").trim().equals("") ? rsmFinal.getString("NR_CELULAR") : rsmFinal.getString("NR_TELEFONE")))));
					
					int nrIdadeAluno = Util.getIdade(dtNascimento);
					if(dtNascimento != null)
						rsmFinal.setValueToField("DT_NM_NASCIMENTO", (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.DAY_OF_MONTH))) > 9 ? dtNascimento.get(Calendar.DAY_OF_MONTH) : "0" + dtNascimento.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1)) + "/" + dtNascimento.get(Calendar.YEAR) + " (" + nrIdadeAluno + " anos)");
					
					rsmFinal.setValueToField("NR_IDADE_ALUNO", nrIdadeAluno);
					
					int nrDivergencia = rsmFinal.getInt("nr_idade") - nrIdadeAluno;
					
					rsmFinal.setValueToField("NR_DIVERGENCIA", (nrDivergencia < 0 ? (-1) * nrDivergencia : nrDivergencia));
					
					if(rsmFinal.getGregorianCalendar("dt_matricula") != null){
						rsmFinal.setValueToField("NR_MES", rsmFinal.getGregorianCalendar("dt_matricula").get(Calendar.MONTH));
						rsmFinal.setValueToField("NR_ANO", rsmFinal.getGregorianCalendar("dt_matricula").get(Calendar.YEAR));
						rsmFinal.setValueToField("NM_MES", Util.meses[rsmFinal.getInt("NR_MES")] + "/" + rsmFinal.getInt("NR_ANO"));
					}
					
					
					if(codigosAluno.size() > 0 && codigosAluno.contains(rsmFinal.getInt("cd_pessoa"))){
						rsmFinal.deleteRow();
						if(x == 0)
							rsmFinal.beforeFirst();
						else
							rsmFinal.previous();
						continue;
					}
					else{
						codigosAluno.add(rsmFinal.getInt("cd_pessoa"));
					}
					
					
					Matricula matricula = MatriculaDAO.get(rsmFinal.getInt("cd_matricula"), connectLocal);
					if(matricula != null){
						Turma turma = TurmaDAO.get(matricula.getCdTurma(), connectLocal);
						if(turma != null){
							rsmFinal.setValueToField("CL_TURMA", turma.getNmTurma() + " - " + TurmaServices.tiposTurno[turma.getTpTurno()]);
							
							rsmFinal.setValueToField("CL_TURNO", TurmaServices.tiposTurno[turma.getTpTurno()]);
							rsmFinal.setValueToField("TP_TURNO", turma.getTpTurno());
							
							Curso curso = CursoDAO.get(turma.getCdCurso(), connectLocal);
							if(curso != null){
								rsmFinal.setValueToField("CD_CURSO", curso.getCdCurso());
								rsmFinal.setValueToField("ID_PRODUTO_SERVICO_CURSO", curso.getIdProdutoServico());
								rsmFinal.setValueToField("NM_MODALIDADE_CURSO", curso.getNmProdutoServico());
							}
							
							Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connectLocal);
							if(instituicao != null){
								rsmFinal.setValueToField("NM_INSTITUICAO", instituicao.getNmPessoa());
								
//								PessoaEndereco instituicaoEndereco = PessoaEnderecoDAO.get(1, instituicao.getCdInstituicao(), connect);
//								if(instituicaoEndereco != null)
									
								
								
								InstituicaoEducacenso instituicaoEducacenso = InstituicaoEducacensoDAO.get(instituicao.getCdInstituicao(), cdPeriodoLetivoAtual, connectLocal);
								if(instituicaoEducacenso != null){
									
									rsmFinal.setValueToField("NM_ZONA", PessoaEnderecoServices.tiposZona[instituicaoEducacenso.getTpLocalizacao()]);
									
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
						}
					}
					else{
						rsmFinal.deleteRow();
						if(x == 0)
							rsmFinal.beforeFirst();
						else
							rsmFinal.previous();
						continue;
					}
					x++;
				}
				rsmFinal.beforeFirst();
				
				ArrayList<String> fields = new ArrayList<String>();
				
				fields.add("NM_ZONA");
				fields.add("NM_INSTITUICAO");
				fields.add("ID_PRODUTO_SERVICO_CURSO");
				fields.add("CL_TURMA");
				fields.add("NM_PESSOA");
				
				rsmFinal.orderBy(fields);
				rsmFinal.beforeFirst();
				
				//Da commit em ambos
				connectLocal.commit();
				connectServer.commit();
				
				Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsmFinal);
				result.addObject("QT_MATRICULADOS_ZONA_URBANA", qtMatriculadosZonaUrbana);
				result.addObject("QT_MATRICULADOS_ZONA_RURAL", qtMatriculadosZonaRural);
				result.addObject("QT_MATRICULADOS_CRECHE", qtMatriculadosCreche);
				return result;
				
			}
			catch(Exception e){
				Conexao.rollback(connectLocal);
				Conexao.rollback(connectServer);
				//registra log de erro quando a classe Ã© utilizad pelo pdv
				Util.registerLog(e);
				e.printStackTrace();
				return new Result(-1, "Erro na importaÃ§Ã£o");
			}
			finally{
				Conexao.desconectar(connectServer);
			}
		}
		
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connectLocal);
			return new Result(-1, "Erro na sincronizaÃ§Ã£o");
		}
		finally{
			Conexao.desconectar(connectLocal);
		}
	}

	
	public static Result findRelatorioEjaEgressos(ArrayList<ItemComparator> criterios){
		String nmUrlDatabase = "jdbc:postgresql://127.0.0.1/edf_centaurus";
		String nmLoginDataBase = "postgres";
		String nmSenhaDatBase = "CDF3DP!";
		
		return findRelatorioEjaEgressos(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
	}
	public static Result findRelatorioEjaEgressos(String nmUrlDatabase, String nmLoginDataBase, String nmSenhaDatBase){
		//Faz a conexao com o Local
		Connection connectLocal = Conexao.conectar();
		try{
			connectLocal.setAutoCommit(false);
			

			Connection connectServer = Conexao.conectar(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
			
			try{
				
				connectServer.setAutoCommit(false);


				int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdSecretaria, connectLocal);
				int cdPeriodoLetivoAtual = 0;
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
				InstituicaoPeriodo periodoLetivoAtual = InstituicaoPeriodoDAO.get(cdPeriodoLetivoAtual, connectLocal);
				
				
				
				ResultSetMap rsmAlunosEjaPeriodoAtual = new ResultSetMap(connectLocal.prepareStatement("SELECT P.cd_pessoa, P.nm_pessoa, P.nm_pessoa AS nm_aluno, P.nr_telefone1, P.nr_telefone2, P.nr_celular, PF.dt_nascimento, PF.nm_mae, PF.nm_pai, A.nm_responsavel, F.cd_cidade, F.nm_logradouro, F.nm_bairro, F.nr_endereco, F.nm_complemento, F.nr_telefone, F.tp_zona AS tp_zona_aluno, F.nr_cep, G.nm_cidade, " 
		 			    +"	  																						A.cd_aluno, A.lg_bolsa_familia, A.lg_pai_nao_declarado, A.lg_cadastro_problema, B.cd_matricula, B.nr_matricula, B.st_matricula, B.dt_matricula, PF.cd_naturalidade, IP.nm_periodo_letivo, C.nr_idade,  "
		 			    +"																							H.nm_produto_servico AS nm_curso  FROM grl_pessoa P "
						+ "																			      LEFT OUTER JOIN grl_pessoa_tipo_documentacao PTD ON (P.cd_pessoa = PTD.cd_pessoa AND PTD.cd_tipo_documentacao IN ("+PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_NASCIMENTO+", "+PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_CASAMENTO+")), "
						+ "																				grl_pessoa_fisica PF"
						+ "																				  LEFT OUTER JOIN grl_estado ERG ON (PF.cd_estado_rg = ERG.cd_estado), "
						+ "																				acd_aluno A, acd_matricula B, acd_instituicao_periodo IP, acd_curso C, acd_curso_etapa D, acd_tipo_etapa E, grl_pessoa_endereco F, grl_cidade G, grl_produto_servico H "
						+ "																					WHERE P.cd_pessoa = A.cd_aluno "
						+ "																					  AND P.cd_pessoa = PF.cd_pessoa "
						+ "																					  AND A.cd_aluno = B.cd_aluno "
						+ "																					  AND B.cd_periodo_letivo = IP.cd_periodo_letivo "
						+ "																					  AND IP.nm_periodo_letivo = '"+periodoLetivoAtual.getNmPeriodoLetivo()+"' "
						+ "																					  AND B.cd_curso = C.cd_curso "
						+ "																					  AND C.cd_curso = D.cd_curso "
						+ "																					  AND D.cd_etapa = E.cd_etapa "
						+ "																					  AND P.cd_pessoa = F.cd_pessoa "
						+ "																					  AND F.lg_principal = 1 "
						+ "																					  AND F.cd_cidade = G.cd_cidade "
						+ "																					  AND C.cd_curso = H.cd_produto_servico "
						+ "																					  AND E.lg_eja = 1 "
						+ "																					  AND st_matricula = "+MatriculaServices.ST_ATIVA+" ORDER BY P.nm_pessoa ").executeQuery());
				 
				
				
				
				ResultSetMap rsmFinal = new ResultSetMap();
				while(rsmAlunosEjaPeriodoAtual.next()){
				
					PreparedStatement pstmt = connectServer.prepareStatement("SELECT * FROM aluno A"
							+ "														LEFT OUTER JOIN uf U ON (A.id_uf_identidade = U.id_uf), "
							+ "													matricula B, escola_modalidade_smed C, modalidade_smed D, modalidade E "
							+ "																							WHERE A.id_aluno = B.id_aluno "
							+ "																							  AND B.id_escola_modalidade_smed = C.id_escola_modalidade_smed "
							+ "                   																		  AND C.id_modalidade_smed = D.id_modalidade_smed "
							+ "																							  AND D.id_modalidade = E.id_modalidade "
							+ "																							  AND ((A.nm_aluno_search = ? AND A.nm_mae_search = ? AND (A.dt_nascimento = '"+Util.convCalendarStringSql(rsmAlunosEjaPeriodoAtual.getGregorianCalendar("dt_nascimento"))+"' OR A.nm_pai_search = ?)) OR (nr_identidade IS NOT NULL AND nr_identidade <> '' AND nr_identidade = ? AND U.cod_uf = ?) OR (cert_nr_regristo IS NOT NULL AND cert_nr_regristo <> '' AND cert_nr_regristo = ?) OR (cert_nr_termo IS NOT NULL AND cert_nr_termo <> '' AND cert_nr_termo = ? AND cert_nr_folha = ? AND cert_nr_livro = ?) OR (nr_cpf IS NOT NULL AND nr_cpf <> '' AND nr_cpf = ?)) "
							+ "																							  AND E.id_modalidade = 3 ORDER BY nm_aluno_search");

					
					pstmt.setString(1, rsmAlunosEjaPeriodoAtual.getString("nm_pessoa").replaceAll("Ã‡", "C").trim());
					pstmt.setString(2, rsmAlunosEjaPeriodoAtual.getString("nm_mae").replaceAll("Ã‡", "C").trim());
					pstmt.setString(3, (rsmAlunosEjaPeriodoAtual.getString("nm_pai") != null && !rsmAlunosEjaPeriodoAtual.getString("nm_pai").equals("NÃƒO DECLARADO") ? rsmAlunosEjaPeriodoAtual.getString("nm_pai").replaceAll("Ã‡", "C").trim() : null));
					pstmt.setString(4, rsmAlunosEjaPeriodoAtual.getString("nr_rg"));
					pstmt.setInt(5, (rsmAlunosEjaPeriodoAtual.getString("id_estado") != null ? Integer.parseInt(rsmAlunosEjaPeriodoAtual.getString("id_estado")) : 0));
					pstmt.setString(6, rsmAlunosEjaPeriodoAtual.getString("nr_documento"));
					pstmt.setString(7, rsmAlunosEjaPeriodoAtual.getString("nr_documento"));
					pstmt.setString(8, rsmAlunosEjaPeriodoAtual.getString("folha"));
					pstmt.setString(9, rsmAlunosEjaPeriodoAtual.getString("livro"));
					pstmt.setString(10, rsmAlunosEjaPeriodoAtual.getString("nr_cpf"));
					
					ResultSetMap rsmAlunosEjaPeriodoAnteriorCentaurus = new ResultSetMap(pstmt.executeQuery());
					if(!rsmAlunosEjaPeriodoAnteriorCentaurus.next()){
						rsmFinal.addRegister(rsmAlunosEjaPeriodoAtual.getRegister());
					}
					
				}
				
				
				rsmFinal.beforeFirst();
				
				int qtMatriculadosZonaUrbana = 0;
				int qtMatriculadosZonaRural = 0;
				int qtMatriculadosCreche = 0;
				ArrayList<Integer> codigosAluno = new ArrayList<Integer>();
				int x = 0;
				while(rsmFinal.next()){
					
					rsmFinal.setValueToField("NM_ZONA_ALUNO", PessoaEnderecoServices.tiposZona[rsmFinal.getInt("tp_zona_aluno")]);
					
					GregorianCalendar dtNascimento = rsmFinal.getGregorianCalendar("dt_nascimento");
					
					Cidade naturalidade = CidadeDAO.get(rsmFinal.getInt("cd_naturalidade"), connectLocal);
					if(naturalidade != null){
						rsmFinal.setValueToField("NM_NATURALIDADE", naturalidade.getNmCidade());
						Estado estadoNaturalidade = EstadoDAO.get(naturalidade.getCdEstado(), connectLocal);
						if(estadoNaturalidade != null)
							rsmFinal.setValueToField("SG_NATURALIDADE", estadoNaturalidade.getSgEstado());
					}
					rsmFinal.setValueToField("NR_TELEFONE", (rsmFinal.getString("NR_TELEFONE1") != null && !rsmFinal.getString("NR_TELEFONE1").trim().equals("") ? rsmFinal.getString("NR_TELEFONE1") : (rsmFinal.getString("NR_TELEFONE2") != null && !rsmFinal.getString("NR_TELEFONE2").trim().equals("") ? rsmFinal.getString("NR_TELEFONE2") : (rsmFinal.getString("NR_CELULAR") != null && !rsmFinal.getString("NR_CELULAR").trim().equals("") ? rsmFinal.getString("NR_CELULAR") : rsmFinal.getString("NR_TELEFONE")))));
					
					int nrIdadeAluno = Util.getIdade(dtNascimento);
					if(dtNascimento != null)
						rsmFinal.setValueToField("DT_NM_NASCIMENTO", (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.DAY_OF_MONTH))) > 9 ? dtNascimento.get(Calendar.DAY_OF_MONTH) : "0" + dtNascimento.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1)) + "/" + dtNascimento.get(Calendar.YEAR) + " (" + nrIdadeAluno + " anos)");
					
					rsmFinal.setValueToField("NR_IDADE_ALUNO", nrIdadeAluno);
					
					int nrDivergencia = rsmFinal.getInt("nr_idade") - nrIdadeAluno;
					
					rsmFinal.setValueToField("NR_DIVERGENCIA", (nrDivergencia < 0 ? (-1) * nrDivergencia : nrDivergencia));
					
					if(rsmFinal.getGregorianCalendar("dt_matricula") != null){
						rsmFinal.setValueToField("NR_MES", rsmFinal.getGregorianCalendar("dt_matricula").get(Calendar.MONTH));
						rsmFinal.setValueToField("NR_ANO", rsmFinal.getGregorianCalendar("dt_matricula").get(Calendar.YEAR));
						rsmFinal.setValueToField("NM_MES", Util.meses[rsmFinal.getInt("NR_MES")] + "/" + rsmFinal.getInt("NR_ANO"));
					}
					
					
					if(codigosAluno.size() > 0 && codigosAluno.contains(rsmFinal.getInt("cd_pessoa"))){
						rsmFinal.deleteRow();
						if(x == 0)
							rsmFinal.beforeFirst();
						else
							rsmFinal.previous();
						continue;
					}
					else{
						codigosAluno.add(rsmFinal.getInt("cd_pessoa"));
					}
					
					
					Matricula matricula = MatriculaDAO.get(rsmFinal.getInt("cd_matricula"), connectLocal);
					if(matricula != null){
						Turma turma = TurmaDAO.get(matricula.getCdTurma(), connectLocal);
						if(turma != null){
							rsmFinal.setValueToField("CL_TURMA", turma.getNmTurma() + " - " + TurmaServices.tiposTurno[turma.getTpTurno()]);
							
							rsmFinal.setValueToField("CL_TURNO", TurmaServices.tiposTurno[turma.getTpTurno()]);
							rsmFinal.setValueToField("TP_TURNO", turma.getTpTurno());
							
							Curso curso = CursoDAO.get(turma.getCdCurso(), connectLocal);
							if(curso != null){
								rsmFinal.setValueToField("CD_CURSO", curso.getCdCurso());
								rsmFinal.setValueToField("ID_PRODUTO_SERVICO_CURSO", curso.getIdProdutoServico());
								rsmFinal.setValueToField("NM_MODALIDADE_CURSO", curso.getNmProdutoServico());
							}
							
							Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connectLocal);
							if(instituicao != null){
								rsmFinal.setValueToField("NM_INSTITUICAO", instituicao.getNmPessoa());
								
//								PessoaEndereco instituicaoEndereco = PessoaEnderecoDAO.get(1, instituicao.getCdInstituicao(), connect);
//								if(instituicaoEndereco != null)
									
								
								
								InstituicaoEducacenso instituicaoEducacenso = InstituicaoEducacensoDAO.get(instituicao.getCdInstituicao(), matricula.getCdPeriodoLetivo(), connectLocal);
								if(instituicaoEducacenso != null){
									
									rsmFinal.setValueToField("NM_ZONA", PessoaEnderecoServices.tiposZona[instituicaoEducacenso.getTpLocalizacao()]);
									
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
						}
					}
					else{
						rsmFinal.deleteRow();
						if(x == 0)
							rsmFinal.beforeFirst();
						else
							rsmFinal.previous();
						continue;
					}
					x++;
				}
				rsmFinal.beforeFirst();
				
				ArrayList<String> fields = new ArrayList<String>();
				
				fields.add("NM_ZONA");
				fields.add("NM_INSTITUICAO");
				fields.add("ID_PRODUTO_SERVICO_CURSO");
				fields.add("CL_TURMA");
				fields.add("NM_PESSOA");
				
				rsmFinal.orderBy(fields);
				rsmFinal.beforeFirst();
				
				//Da commit em ambos
				connectLocal.commit();
				connectServer.commit();
				
				Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsmFinal);
				result.addObject("QT_MATRICULADOS_ZONA_URBANA", qtMatriculadosZonaUrbana);
				result.addObject("QT_MATRICULADOS_ZONA_RURAL", qtMatriculadosZonaRural);
				result.addObject("QT_MATRICULADOS_CRECHE", qtMatriculadosCreche);
				return result;
				
			}
			catch(Exception e){
				Conexao.rollback(connectLocal);
				Conexao.rollback(connectServer);
				//registra log de erro quando a classe Ã© utilizad pelo pdv
				Util.registerLog(e);
				e.printStackTrace();
				return new Result(-1, "Erro na importaÃ§Ã£o");
			}
			finally{
				Conexao.desconectar(connectServer);
			}
		}
		
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connectLocal);
			return new Result(-1, "Erro na sincronizaÃ§Ã£o");
		}
		finally{
			Conexao.desconectar(connectLocal);
		}
	}
	
	
	public static Result findInconsistenciaNome(){
		String nmUrlDatabase = "jdbc:postgresql://127.0.0.1/edf_centaurus";
		String nmLoginDataBase = "postgres";
		String nmSenhaDatBase = "CDF3DP!";
		
		return findInconsistenciaNome(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
	}
	public static Result findInconsistenciaNome(String nmUrlDatabase, String nmLoginDataBase, String nmSenhaDatBase){
		//Faz a conexao com o Local
		Connection connectLocal = Conexao.conectar();
		try{
			connectLocal.setAutoCommit(false);
			

			Connection connectServer = Conexao.conectar(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
			
			try{
				
				connectServer.setAutoCommit(false);


				int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdSecretaria, connectLocal);
				int cdPeriodoLetivoAtual = 0;
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
				InstituicaoPeriodo periodoLetivoAtual = InstituicaoPeriodoDAO.get(cdPeriodoLetivoAtual, connectLocal);
				
				
				
				ResultSetMap rsmAlunos = new ResultSetMap(connectLocal.prepareStatement("SELECT P.cd_pessoa, P.nm_pessoa, PF.nm_mae, PF.dt_nascimento  FROM grl_pessoa P, grl_pessoa_fisica PF, acd_aluno A, acd_matricula B, acd_instituicao_periodo IP "
						+ "																					WHERE P.cd_pessoa = A.cd_aluno "
						+ "																					  AND P.cd_pessoa = PF.cd_pessoa "
						+ "																					  AND A.cd_aluno = B.cd_aluno "
						+ "																					  AND B.cd_periodo_letivo = IP.cd_periodo_letivo "
						+ "																					  AND IP.nm_periodo_letivo = '"+periodoLetivoAtual.getNmPeriodoLetivo()+"' "
						+ "																					  AND st_matricula = "+MatriculaServices.ST_ATIVA+" ORDER BY P.nm_pessoa ").executeQuery());
				 
				
				
				boolean naoAchado = true;
				boolean cemPorcento = false;
				ResultSetMap rsmFinal = new ResultSetMap(connectServer.prepareStatement("SELECT * FROM aluno ORDER BY nm_aluno_search").executeQuery());
				System.out.println("primeiro " + rsmAlunos.size());
				System.out.println("segundo " + rsmFinal.size());
				while(rsmAlunos.next()){
					String nome1 = rsmAlunos.getString("nm_pessoa");
					String[] conj1 = nome1.split(" ");
					
					String strAchado = null;
					while(rsmFinal.next()){
						
						if(rsmAlunos.getString("nm_pessoa") == null || rsmAlunos.getString("nm_pessoa").equals("")){
							break;
						}
						if(rsmAlunos.getString("nm_mae") == null || rsmAlunos.getString("nm_mae").equals("")){
							break;
						}
						if(rsmAlunos.getGregorianCalendar("dt_nascimento") == null){
							break;
						}
						if(rsmFinal.getString("nm_aluno_search") == null || rsmFinal.getString("nm_aluno_search").equals("")){
							continue;
						}
						if(rsmFinal.getString("nm_mae_search") == null || rsmFinal.getString("nm_mae_search").equals("")){
							continue;
						}
						if(rsmFinal.getGregorianCalendar("dt_nascimento") == null){
							continue;
						}
						
						String nome2 = rsmFinal.getString("nm_aluno_search");
						String[] conj2 = nome2.split(" ");
						
						boolean achadoUm = false;
						for(String conjNome1 : conj1){
							for(String conjNome2 : conj2){
								if(conjNome1.equals(conjNome2) && conjNome1.length() > 3){
									achadoUm = true;
									break;
								}
							}
							if(achadoUm)
								break;
						}
						
						if(!achadoUm){
							continue;
						}
						
						double porcentagem = Util.compareStrings(rsmAlunos.getString("nm_pessoa").trim(), rsmAlunos.getString("nm_mae").trim(), rsmAlunos.getGregorianCalendar("dt_nascimento"), rsmFinal.getString("nm_aluno_search").trim(), rsmFinal.getString("nm_mae_search").trim(), rsmFinal.getGregorianCalendar("dt_nascimento"));
						if(porcentagem > 0.8 && porcentagem < 1){
							strAchado = rsmFinal.getString("nm_aluno_search").trim();
							naoAchado = false;
							break;
						}
						if(porcentagem == 1){
							cemPorcento = true;
							naoAchado = false;
							break;
						}
					}
					rsmFinal.beforeFirst();
					if(naoAchado){
						System.out.println("NAO ACHADO: " + rsmAlunos.getString("nm_pessoa").trim());
					}
					else{
						if(!cemPorcento)
							System.out.println("ACHADO: " + rsmAlunos.getString("nm_pessoa").trim() + " = " + strAchado);
					}
					cemPorcento = false;
					naoAchado = true;
				}
				
				
				rsmFinal.beforeFirst();
				
				
				
				//Da commit em ambos
				connectLocal.commit();
				connectServer.commit();
				
				Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsmFinal);
				return result;
				
			}
			catch(Exception e){
				Conexao.rollback(connectLocal);
				Conexao.rollback(connectServer);
				//registra log de erro quando a classe Ã© utilizad pelo pdv
				Util.registerLog(e);
				e.printStackTrace();
				return new Result(-1, "Erro na importaÃ§Ã£o");
			}
			finally{
				Conexao.desconectar(connectServer);
			}
		}
		
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connectLocal);
			return new Result(-1, "Erro na sincronizaÃ§Ã£o");
		}
		finally{
			Conexao.desconectar(connectLocal);
		}
	}

	public static ResultSetMap findPossiveisMaisEducacao(ArrayList<ItemComparator> criterios) {
		return findPossiveisMaisEducacao(criterios, null);
	}

	public static ResultSetMap findPossiveisMaisEducacao(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int qtLimite = 0;
			String nmPessoa = "";
			int cdInstituicao = 0;
			int cdCursoAtual = 0;
			String nmPeriodoLetivo = "";
			int cdPeriodoLetivo = 0;
			int cdPeriodoLetivoSecretaria = 0;
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			String nrMatricula = null;
			boolean lgMatriculaPeriodo = false;
			boolean semMatriculaPeriodo = false;
			boolean lgIncluirPendente = false;
			boolean permitirVisualizarAluno = false;
			String stMatricula = "" + MatriculaServices.ST_ATIVA;
			int cdTurma = 0;
			Turma turma = null;
			ResultSetMap rsmPeriodoSecretaria = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdSecretaria, connect);
			if(rsmPeriodoSecretaria.next())
				cdPeriodoLetivoSecretaria = rsmPeriodoSecretaria.getInt("cd_periodo_letivo");
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.nm_pessoa")) {
					nmPessoa = Util.limparTexto(criterios.get(i).getValue());
					nmPessoa = "%"+nmPessoa.replaceAll(" ", "%")+"%";
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_INSTITUICAO")) {
					cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
					if(cdPeriodoLetivo == 0){
						ResultSetMap rsm = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdInstituicao, connect);
						if(rsm.next()){
							cdPeriodoLetivo = rsm.getInt("cd_periodo_letivo");
						}
					}
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nmPeriodoLetivo")) {
					nmPeriodoLetivo = criterios.get(i).getValue();
					ArrayList<ItemComparator> criteriosPeriodoLetivo = new ArrayList<ItemComparator>();
					criteriosPeriodoLetivo.add(new ItemComparator("nm_periodo_letivo", nmPeriodoLetivo, ItemComparator.EQUAL, Types.VARCHAR));
					criteriosPeriodoLetivo.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmInstituicaoPeriodo = InstituicaoPeriodoDAO.find(criteriosPeriodoLetivo, connect);
					InstituicaoPeriodo instituicaoPeriodo = null;
					if(rsmInstituicaoPeriodo.next()){
						instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmInstituicaoPeriodo.getInt("cd_periodo_letivo"), connect);
						cdPeriodoLetivo = instituicaoPeriodo.getCdPeriodoLetivo();
					}
					
					cdPeriodoLetivoSecretaria = (instituicaoPeriodo.getCdPeriodoLetivoSuperior() == 0 ? instituicaoPeriodo.getCdPeriodoLetivo() : instituicaoPeriodo.getCdPeriodoLetivoSuperior());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdCursoAtual")) {
					cdCursoAtual = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrMatricula")) {
					nrMatricula = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgMatriculaPeriodo")) {
					lgMatriculaPeriodo = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("semMatriculaPeriodo")) {
					semMatriculaPeriodo = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgIncluirPendente")) {
					lgIncluirPendente = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("stMatricula")) {
					stMatricula = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("permitirVisualizarAluno")) {
					permitirVisualizarAluno = true;
				}	
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdTurma")) {
					cdTurma = Integer.parseInt(criterios.get(i).getValue());
					turma = TurmaDAO.get(cdTurma, connect);
				}
				else
					crt.add(criterios.get(i));
				
			}
			
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
			String sql =
		 			  "SELECT A.cd_pessoa,  A.nm_pessoa AS NM_ALUNO, W.cd_aluno, E.NM_MAE, E.dt_nascimento " +
 					  "FROM grl_pessoa A " +
 					  "LEFT OUTER JOIN grl_pessoa_fisica 	E ON (A.cd_pessoa = E.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_pessoa_empresa 	F ON (A.cd_pessoa = F.cd_pessoa) " +
 					  "JOIN acd_aluno 				W ON (A.cd_pessoa = W.cd_aluno) " +
 					  "WHERE 1 = 1 " +
 				   (cdInstituicao != cdSecretaria && !semMatriculaPeriodo ? " AND EXISTS (SELECT SUB_MAT.cd_aluno FROM acd_matricula SUB_MAT WHERE SUB_MAT.cd_aluno = W.cd_aluno AND SUB_MAT.st_matricula IN ("+stMatricula+ (lgIncluirPendente ? ", " + MatriculaServices.ST_PENDENTE : "")+") AND SUB_MAT.cd_periodo_letivo = "+cdPeriodoLetivo+ (nrMatricula != null ? " AND SUB_MAT.nr_matricula = '"+nrMatricula+"'" : "")+") " : "") +
 				   (cdInstituicao == cdSecretaria && nrMatricula != null ? " AND EXISTS (SELECT SUB_MAT.cd_aluno FROM acd_matricula SUB_MAT WHERE SUB_MAT.cd_aluno = W.cd_aluno AND SUB_MAT.nr_matricula = '"+nrMatricula+"'"+") " : "") +
 				   (!nmPessoa.equals("") ? 
				   "	  AND TRANSLATE(A.nm_pessoa, 'Ã¡Ã©Ã­Ã³ÃºÃ Ã¨Ã¬Ã²Ã¹Ã£ÃµÃ¢ÃªÃ®Ã´Ã´Ã¤Ã«Ã¯Ã¶Ã¼Ã§Ã�Ã‰Ã�Ã“ÃšÃ€ÃˆÃŒÃ’Ã™ÃƒÃ•Ã‚ÃŠÃŽÃ”Ã›Ã„Ã‹Ã�Ã–ÃœÃ‡', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE TRANSLATE('%"+nmPessoa+"%', 'Ã¡Ã©Ã­Ã³ÃºÃ Ã¨Ã¬Ã²Ã¹Ã£ÃµÃ¢ÃªÃ®Ã´Ã´Ã¤Ã«Ã¯Ã¶Ã¼Ã§Ã�Ã‰Ã�Ã“ÃšÃ€ÃˆÃŒÃ’Ã™ÃƒÃ•Ã‚ÃŠÃŽÃ”Ã›Ã„Ã‹Ã�Ã–ÃœÃ‡', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') ": "") +
				   " AND NOT EXISTS (SELECT SUB_MAT.cd_aluno FROM acd_matricula SUB_MAT, acd_turma SUB_T "
				   		+ "									 WHERE SUB_MAT.cd_aluno = W.cd_aluno "
				   		+ "									   AND SUB_MAT.cd_turma = SUB_T.cd_turma "
				   		+ "									   AND SUB_MAT.st_matricula = "+MatriculaServices.ST_ATIVA
				   		+ " 								   AND SUB_MAT.cd_periodo_letivo = "+cdPeriodoLetivo
				   		+ " 								   AND (SUB_MAT.cd_curso IN (1159, 1181, 1179, 1147, 1187, 1189, 1197, 1195, 1193, 1191, 1271) "
				   		+ "									   OR (SUB_T.tp_turno = " + turma.getTpTurno()+" AND SUB_T.cd_turma <> "+turma.getCdTurma()+"))"
				   		+ "                                ) ";
			ResultSetMap rsm = Search.find(sql, "ORDER BY A.nm_pessoa "+sqlLimit[1], crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			ArrayList<Integer> codigosAluno = new ArrayList<Integer>();
			int x = 0;
			while(rsm.next()){
				rsm.setValueToField("NR_IDADE", Util.getIdade(rsm.getGregorianCalendar("dt_nascimento")));
				
				
				ResultSetMap rsmMatriculaRecente = new ResultSetMap(connect.prepareStatement("SELECT A.*, B.nm_periodo_letivo, C.nm_produto_servico AS nm_curso, "
						+ "																		(C.nm_produto_servico || ' - ' || D.nm_turma) AS nm_turma, D.nm_turma AS nm_turma_simples, D.tp_turno, "
						+ "																		E.cd_pessoa AS cd_instituicao, "
						+ "																		E.nm_pessoa AS nm_instituicao, F.cd_curso_etapa, CC.nr_ordem FROM acd_matricula A, acd_instituicao_periodo B, grl_produto_servico C, acd_curso CC, acd_turma D, grl_pessoa E, acd_curso_etapa F "
						+ "																	  WHERE A.cd_periodo_letivo = B.cd_periodo_letivo "
						+ "																		AND A.cd_curso = C.cd_produto_servico "
						+ "																		AND A.cd_curso = CC.cd_curso "
						+ "																		AND A.cd_turma = D.cd_turma "
						+ "																		AND D.cd_instituicao = E.cd_pessoa "
						+ "																		AND A.cd_curso = F.cd_curso "
						+ "																		AND D.tp_atendimento NOT IN ("+TurmaServices.TP_ATENDIMENTO_AEE+", "+TurmaServices.TP_ATENDIMENTO_ATIVIDADE_COMPLEMENTAR+")"
						+ "																		AND A.cd_aluno = " + rsm.getInt("cd_aluno") 
						+ "																		AND B.nm_periodo_letivo = '" + InstituicaoPeriodoDAO.get(cdPeriodoLetivoSecretaria, connect).getNmPeriodoLetivo()+"'" 
						+ " 																	AND (A.st_matricula IN (" + stMatricula +")) ORDER BY dt_matricula DESC").executeQuery());
				if(rsmMatriculaRecente.next()){
					rsm.setValueToField("cd_matricula", rsmMatriculaRecente.getInt("cd_matricula"));
					if(rsmMatriculaRecente.getGregorianCalendar("dt_matricula") != null)
						rsm.setValueToField("dt_nm_matricula", Util.convCalendarString3(rsmMatriculaRecente.getGregorianCalendar("dt_matricula")));
					rsm.setValueToField("nr_matricula", rsmMatriculaRecente.getString("nr_matricula"));
					rsm.setValueToField("st_matricula", rsmMatriculaRecente.getInt("st_matricula"));
					rsm.setValueToField("nm_st_matricula", MatriculaServices.situacao[rsmMatriculaRecente.getInt("st_matricula")]);
					rsm.setValueToField("cd_curso", rsmMatriculaRecente.getInt("cd_curso"));
					rsm.setValueToField("nr_ordem", rsmMatriculaRecente.getInt("nr_ordem"));
					rsm.setValueToField("cd_curso_etapa", rsmMatriculaRecente.getInt("cd_curso_etapa"));
					rsm.setValueToField("nm_curso", rsmMatriculaRecente.getString("nm_curso"));
					rsm.setValueToField("cd_matriz", rsmMatriculaRecente.getInt("cd_matriz"));
					rsm.setValueToField("cd_turma", rsmMatriculaRecente.getInt("cd_turma"));
					rsm.setValueToField("nm_turma", rsmMatriculaRecente.getString("nm_turma"));
					rsm.setValueToField("nm_turma_simples", rsmMatriculaRecente.getString("nm_turma_simples"));
					rsm.setValueToField("nm_tp_turno", TurmaServices.tiposTurno[rsmMatriculaRecente.getInt("tp_turno")]);
					rsm.setValueToField("cd_instituicao", rsmMatriculaRecente.getInt("cd_instituicao"));
					rsm.setValueToField("nm_instituicao", rsmMatriculaRecente.getString("nm_instituicao"));
					rsm.setValueToField("cd_periodo_letivo", rsmMatriculaRecente.getInt("cd_periodo_letivo"));
					rsm.setValueToField("nm_periodo_letivo", rsmMatriculaRecente.getString("nm_periodo_letivo"));
					rsm.setValueToField("dt_matricula", rsmMatriculaRecente.getGregorianCalendar("dt_matricula"));
					rsm.setValueToField("dt_conclusao", rsmMatriculaRecente.getGregorianCalendar("dt_conclusao"));
					rsm.setValueToField("tp_matricula", rsmMatriculaRecente.getInt("tp_matricula"));
					rsm.setValueToField("cd_aluno", rsmMatriculaRecente.getInt("cd_aluno"));
					rsm.setValueToField("cd_matricula_origem", rsmMatriculaRecente.getInt("cd_matricula_origem"));
					rsm.setValueToField("cd_reserva", rsmMatriculaRecente.getInt("cd_reserva"));
					rsm.setValueToField("cd_area_interesse", rsmMatriculaRecente.getInt("cd_area_interesse"));
					rsm.setValueToField("txt_observacao", rsmMatriculaRecente.getString("txt_observacao"));
					rsm.setValueToField("txt_boletim", rsmMatriculaRecente.getString("txt_boletim"));
					rsm.setValueToField("cd_pre_matricula", rsmMatriculaRecente.getInt("cd_pre_matricula"));
					rsm.setValueToField("tp_escolarizacao_outro_espaco", rsmMatriculaRecente.getInt("tp_escolarizacao_outro_espaco"));
					rsm.setValueToField("lg_transporte_publico", rsmMatriculaRecente.getInt("lg_transporte_publico"));
					rsm.setValueToField("tp_poder_responsavel", rsmMatriculaRecente.getInt("tp_poder_responsavel"));
					rsm.setValueToField("tp_forma_ingresso", rsmMatriculaRecente.getInt("tp_forma_ingresso"));
					rsm.setValueToField("txt_documento_oficial", rsmMatriculaRecente.getString("txt_documento_oficial"));
					rsm.setValueToField("lg_autorizacao_rematricula", rsmMatriculaRecente.getInt("lg_autorizacao_rematricula"));
					rsm.setValueToField("lg_atividade_complementar", rsmMatriculaRecente.getInt("lg_atividade_complementar"));
					rsm.setValueToField("dt_interrupcao", rsmMatriculaRecente.getGregorianCalendar("dt_interrupcao"));
					rsm.setValueToField("lg_reprovacao", rsmMatriculaRecente.getInt("lg_reprovacao"));
					rsm.setValueToField("st_matricula_centaurus", rsmMatriculaRecente.getInt("st_matricula_centaurus"));
					rsm.setValueToField("nm_ultima_escola", rsmMatriculaRecente.getString("nm_ultima_escola"));
					
					ResultSetMap rsmTipoTransporte = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula_tipo_transporte A, fta_tipo_transporte B WHERE A.cd_tipo_transporte = B.cd_tipo_transporte AND A.cd_matricula = " + rsmMatriculaRecente.getInt("cd_matricula")).executeQuery());
					if(rsmTipoTransporte.next()){
						rsm.setValueToField("cd_tipo_transporte", rsmTipoTransporte.getInt("cd_tipo_transporte"));
					}
					
					//Utilizado para saber o curso anterior do curso atual da matricula, seguindo o que esta considerado na etapa
					ResultSetMap rsmCursoAnterior = new ResultSetMap(connect.prepareStatement("SELECT A.cd_curso, C.nm_produto_servico AS nm_curso FROM acd_curso A, acd_curso_etapa B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND A.cd_curso = C.cd_produto_servico AND B.cd_curso_etapa_posterior = " + rsm.getInt("cd_curso_etapa")).executeQuery());
					if(rsmCursoAnterior.next()){
						rsm.setValueToField("CD_CURSO_ANTERIOR", rsmCursoAnterior.getInt("cd_curso"));
						rsm.setValueToField("NM_CURSO_ANTERIOR", rsmCursoAnterior.getString("nm_curso"));
					}
					Curso cursoMatricula = CursoDAO.get(rsm.getInt("CD_CURSO"), connect);
					if(rsm.getInt("CD_CURSO_ANTERIOR") == 0){
						ResultSetMap rsmCursoEquivalente = new ResultSetMap(connect.prepareStatement("SELECT A.cd_curso, B.cd_curso_etapa FROM acd_curso A, acd_curso_etapa B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND A.cd_curso = C.cd_produto_servico AND C.id_produto_servico = '" + cursoMatricula.getIdProdutoServico() + "' AND A.lg_referencia = 0").executeQuery());
						if(rsmCursoEquivalente.next()){
							rsmCursoAnterior = new ResultSetMap(connect.prepareStatement("SELECT A.cd_curso, C.nm_produto_servico AS nm_curso FROM acd_curso A, acd_curso_etapa B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND A.cd_curso = C.cd_produto_servico AND B.cd_curso_etapa_posterior = " + rsmCursoEquivalente.getInt("cd_curso_etapa")).executeQuery());
							if(rsmCursoAnterior.next()){
								rsm.setValueToField("CD_CURSO_ANTERIOR", rsmCursoAnterior.getInt("cd_curso"));
								rsm.setValueToField("NM_CURSO_ANTERIOR", rsmCursoAnterior.getString("nm_curso"));
							}
						}
					}
					
				}
				else{
					if(lgMatriculaPeriodo){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				
				if(cdCursoAtual > 0 && cdCursoAtual != rsm.getInt("cd_curso")){
					rsm.deleteRow();
					if(x == 0)
						rsm.beforeFirst();
					else
						rsm.previous();
					continue;
				}
				
				GregorianCalendar dtNascimento = rsm.getGregorianCalendar("dt_nascimento");
				if(dtNascimento != null)
					rsm.setValueToField("DT_NM_NASCIMENTO", (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.DAY_OF_MONTH))) > 9 ? dtNascimento.get(Calendar.DAY_OF_MONTH) : "0" + dtNascimento.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1)) + "/" + dtNascimento.get(Calendar.YEAR));
				
				if(cdSecretaria != cdInstituicao && cdInstituicao > 0 && cdPeriodoLetivoSecretaria > 0 && !permitirVisualizarAluno){
					ResultSetMap rsmMatriculaOutraInstituicao = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula A, acd_instituicao_periodo B, acd_turma C "
							+ "																				WHERE A.cd_periodo_letivo = B.cd_periodo_letivo "
							+ "																				  AND B.nm_periodo_letivo = '" + InstituicaoPeriodoDAO.get(cdPeriodoLetivoSecretaria, connect).getNmPeriodoLetivo()+"'"
							+ "																				  AND B.cd_instituicao <> " + cdInstituicao + " "
							+ "																				  AND A.cd_aluno = " + rsm.getInt("cd_aluno")
							+ "																				  AND A.cd_turma = C.cd_turma "
							+ "																				  AND C.tp_atendimento NOT IN ("+TurmaServices.TP_ATENDIMENTO_AEE+")"
							+ "																				  AND A.st_matricula IN("+MatriculaServices.ST_ATIVA+", "+MatriculaServices.ST_PENDENTE+")").executeQuery());
					if(rsmMatriculaOutraInstituicao.next()){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				
				if(codigosAluno.size() > 0 && codigosAluno.contains(rsm.getInt("cd_pessoa"))){
					rsm.deleteRow();
					if(x == 0)
						rsm.beforeFirst();
					else
						rsm.previous();
					continue;
				}
				else{
					codigosAluno.add(rsm.getInt("cd_pessoa"));
				}
				
				x++;
			}
			rsm.beforeFirst();
			
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

	
	public static Result validacaoAlunos(int cdTurma, int cdUsuario){
		return validacaoAlunos(cdTurma, cdUsuario, null);
	}
	
	public static Result validacaoAlunos(int cdTurma, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsmAlunos = TurmaServices.getAlunos(cdTurma, true, connect);
			int cont = 0;
			while(rsmAlunos.next()){
				
				Aluno aluno = AlunoDAO.get(rsmAlunos.getInt("cd_aluno"), connect);
				
				System.out.println((++cont) + " - Aluno: " + aluno.getNmPessoa());
				
				Result resultado = validacaoAluno(cdTurma, rsmAlunos.getInt("cd_aluno"), cdUsuario, connect);
				if(resultado.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return resultado;
				}
			}
			
			Turma turma = TurmaDAO.get(cdTurma, connect);
			int cdPeriodoRecente = 0;
			ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(turma.getCdInstituicao(), connect);
			if(rsmPeriodoRecente.next())
				cdPeriodoRecente = rsmPeriodoRecente.getInt("cd_periodo_letivo");
			
			
			rsmAlunos = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula M, acd_turma A, acd_instituicao_periodo B "
					+ "												WHERE M.cd_turma = A.cd_turma "
					+ "												  AND A.cd_periodo_letivo = B.cd_periodo_letivo "
					+ "												  AND A.cd_instituicao = " + turma.getCdInstituicao()
					+ " 											  AND B.cd_periodo_letivo = " + cdPeriodoRecente
					+ "												  AND M.st_matricula NOT IN (" + MatriculaServices.ST_ATIVA + ") AND NOT EXISTS (SELECT * FROM acd_matricula MATA WHERE M.cd_aluno = MATA.cd_aluno AND M.cd_periodo_letivo = MATA.cd_periodo_letivo AND MATA.st_matricula = "+MatriculaServices.ST_ATIVA+")").executeQuery());
			while(rsmAlunos.next()){
				connect.prepareStatement("DELETE FROM acd_instituicao_pendencia "
						+ "												WHERE cd_instituicao = " + turma.getCdInstituicao() 
						+ " 											  AND cd_aluno = " + rsmAlunos.getInt("cd_aluno")).executeUpdate();
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
	
	public static Result validacaoAluno(int cdTurma, int cdAluno, int cdUsuario){
		return validacaoAluno(cdTurma, cdAluno, cdUsuario, null);
	}
	
	public static Result validacaoAluno(int cdTurma, int cdAluno, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
	
			Turma turma = TurmaDAO.get(cdTurma, connect);
			Aluno aluno = AlunoDAO.get(cdAluno, connect);

			Matricula matricula = MatriculaServices.getMatricula(aluno.getCdAluno(), cdTurma, turma.getCdPeriodoLetivo(), connect);
			//Endereco
			ArrayList<ItemComparator> criterios = new ArrayList<>();
			criterios.add(new ItemComparator("cd_pessoa", "" + aluno.getCdAluno(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmEndereco = PessoaEnderecoDAO.find(criterios, connect);
			PessoaEndereco alunoEndereco = null;
			if(rsmEndereco.next()){
				alunoEndereco = PessoaEnderecoDAO.get(rsmEndereco.getInt("cd_endereco"), aluno.getCdAluno(), connect);
			}
			ValidatorResult resultadoValidacao = validate(aluno, alunoEndereco, cdUsuario, GRUPO_VALIDACAO_EDUCACENSO, connect);
			
			Result resultValidacoesPendencia = InstituicaoPendenciaServices.atualizarValidacaoPendencia(resultadoValidacao, turma.getCdInstituicao(), turma.getCdTurma(), aluno.getCdAluno(), 0/*cdProfessor*/, InstituicaoPendenciaServices.TP_REGISTRO_ALUNO_CENSO, cdUsuario, GRUPO_VALIDACAO_EDUCACENSO, connect);
			if(resultValidacoesPendencia.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return resultValidacoesPendencia;
			}
			
			Result ret = PessoaTipoDocumentacaoServices.validacaoDocumentacao(aluno.getCdAluno(), turma.getCdInstituicao(), cdUsuario, connect);
			if(ret.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return ret;
			}
			ret = MatriculaServices.validacaoMatricula(matricula.getCdMatricula(), cdUsuario, connect);
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
	
	public static Result findRelatorioEjaEgressosPorPeriodo(){
		String nmUrlDatabase = "jdbc:postgresql://127.0.0.1/edf_centaurus";
		String nmLoginDataBase = "postgres";
		String nmSenhaDatBase = "CDF3DP!";
		
		return findRelatorioEjaEgressosPorPeriodo(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase, 2012, 2011);
	}
	public static Result findRelatorioEjaEgressosPorPeriodo(String nmUrlDatabase, String nmLoginDataBase, String nmSenhaDatBase, int periodoReferencia, int periodoAnterior){
		//Faz a conexao com o Local
		Connection connectLocal = Conexao.conectar(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
		try{
			connectLocal.setAutoCommit(false);
			
//			PreparedStatement pstmt = connectLocal.prepareStatement("SELECT * FROM aluno A"
//					+ "														LEFT OUTER JOIN uf U ON (A.id_uf_identidade = U.id_uf), "
//					+ "													matricula B, escola_modalidade_smed C, modalidade_smed D, modalidade E "
//					+ "																							WHERE A.id_aluno = B.id_aluno "
//					+ "																							  AND B.ano_matricula = "+periodoAnterior
//					+ "																							  AND B.id_escola_modalidade_smed = C.id_escola_modalidade_smed "
//					+ "                   																		  AND C.id_modalidade_smed = D.id_modalidade_smed "
//					+ "																							  AND D.id_modalidade = E.id_modalidade "
//					+ "																							  AND E.id_modalidade = 3 ORDER BY nm_aluno_search");

			
//			ResultSetMap rsmCentaurus = new ResultSetMap(pstmt.executeQuery());
			
			
			ResultSetMap rsmAlunosEjaPeriodoAtual = new ResultSetMap(connectLocal.prepareStatement("SELECT * FROM aluno A LEFT OUTER JOIN uf U ON (A.id_uf_identidade = U.id_uf), matricula B, escola_modalidade_smed C, modalidade_smed D, modalidade E "
					+ "																							WHERE A.id_aluno = B.id_aluno "
					+ "																							  AND B.ano_matricula = "+periodoReferencia
					+ "																							  AND B.id_escola_modalidade_smed = C.id_escola_modalidade_smed "
					+ "                   																		  AND C.id_modalidade_smed = D.id_modalidade_smed "
					+ "																							  AND D.id_modalidade = E.id_modalidade "
					+ "																							  AND E.id_modalidade = 3 ORDER BY nm_aluno_search").executeQuery());
			 
			
			
			System.out.println("rsmAlunosEjaPeriodoAtual.size = " + rsmAlunosEjaPeriodoAtual.size());
//			int cont = 0;
			ResultSetMap rsmFinal = new ResultSetMap();
			while(rsmAlunosEjaPeriodoAtual.next()){
				PreparedStatement pstmt = connectLocal.prepareStatement("SELECT * FROM aluno A"
						+ "														LEFT OUTER JOIN uf U ON (A.id_uf_identidade = U.id_uf), "
						+ "													matricula B, escola_modalidade_smed C, modalidade_smed D, modalidade E "
						+ "																							WHERE A.id_aluno = B.id_aluno "
						+ "																							  AND B.ano_matricula = "+periodoAnterior
						+ "																							  AND B.id_escola_modalidade_smed = C.id_escola_modalidade_smed "
						+ "                   																		  AND C.id_modalidade_smed = D.id_modalidade_smed "
						+ "																							  AND D.id_modalidade = E.id_modalidade "
						+ "																							  AND ((A.nm_aluno_search = ? AND A.nm_mae_search = ? AND (A.dt_nascimento = '"+Util.convCalendarStringSql(rsmAlunosEjaPeriodoAtual.getGregorianCalendar("dt_nascimento"))+"' OR A.nm_pai_search = ?)) OR (nr_identidade IS NOT NULL AND nr_identidade <> '' AND nr_identidade = ? AND U.cod_uf = ?) OR (cert_nr_regristo IS NOT NULL AND cert_nr_regristo <> '' AND cert_nr_regristo = ?) OR (cert_nr_termo IS NOT NULL AND cert_nr_termo <> '' AND cert_nr_termo = ? AND cert_nr_folha = ? AND cert_nr_livro = ?) OR (nr_cpf IS NOT NULL AND nr_cpf <> '' AND nr_cpf = ?) OR (A.id_aluno = ?)) "
						+ "																							  AND E.id_modalidade = 3 ORDER BY nm_aluno_search");

				
				pstmt.setString(1, rsmAlunosEjaPeriodoAtual.getString("nm_aluno_search"));
				pstmt.setString(2, rsmAlunosEjaPeriodoAtual.getString("nm_mae_search"));
				pstmt.setString(3, (rsmAlunosEjaPeriodoAtual.getString("nm_pai_search") != null && !rsmAlunosEjaPeriodoAtual.getString("nm_pai_search").equals("NÃƒO DECLARADO") ? rsmAlunosEjaPeriodoAtual.getString("nm_pai") : null));
				pstmt.setString(4, rsmAlunosEjaPeriodoAtual.getString("nr_identidade"));
				pstmt.setInt(5, rsmAlunosEjaPeriodoAtual.getInt("cod_uf"));
				pstmt.setString(6, rsmAlunosEjaPeriodoAtual.getString("cert_nr_regristo"));
				pstmt.setString(7, rsmAlunosEjaPeriodoAtual.getString("cert_nr_termo"));
				pstmt.setString(8, rsmAlunosEjaPeriodoAtual.getString("cert_nr_folha"));
				pstmt.setString(9, rsmAlunosEjaPeriodoAtual.getString("cert_nr_livro"));
				pstmt.setString(10, rsmAlunosEjaPeriodoAtual.getString("nr_cpf"));
				pstmt.setInt(11, rsmAlunosEjaPeriodoAtual.getInt("id_aluno"));
				ResultSetMap rsmAlunosEjaPeriodoAnterior = new ResultSetMap(pstmt.executeQuery());
				if(rsmAlunosEjaPeriodoAnterior.next()){
					rsmFinal.addRegister(rsmAlunosEjaPeriodoAtual.getRegister());
					
					
					
//					boolean achado = false;
//					while(rsmCentaurus.next()){
//						if(rsmAlunosEjaPeriodoAtual.getString("nm_aluno_search") == null || rsmAlunosEjaPeriodoAtual.getString("nm_aluno_search").equals("")){
//							break;
//						}
//						if(rsmAlunosEjaPeriodoAtual.getString("nm_mae_search") == null || rsmAlunosEjaPeriodoAtual.getString("nm_mae_search").equals("")){
//							break;
//						}
//						if(rsmAlunosEjaPeriodoAtual.getGregorianCalendar("dt_nascimento") == null){
//							break;
//						}
//						
//						if(rsmCentaurus.getString("nm_aluno_search") == null || rsmCentaurus.getString("nm_aluno_search").equals("")){
//							continue;
//						}
//						if(rsmCentaurus.getString("nm_mae_search") == null || rsmCentaurus.getString("nm_mae_search").equals("")){
//							continue;
//						}
//						if(rsmCentaurus.getGregorianCalendar("dt_nascimento") == null){
//							continue;
//						}
//						
//						double porcentagem = Util.compareStrings(rsmAlunosEjaPeriodoAtual.getString("nm_aluno_search").trim(), rsmAlunosEjaPeriodoAtual.getString("nm_mae_search").trim(), rsmAlunosEjaPeriodoAtual.getGregorianCalendar("dt_nascimento"), rsmCentaurus.getString("nm_aluno_search").trim(), rsmCentaurus.getString("nm_mae_search").trim(), rsmCentaurus.getGregorianCalendar("dt_nascimento"));
//						if(porcentagem > 0.8){
//							achado = true;
//							break;
//						}
//					}
//					
//					if(achado){
//						cont++;
//						System.out.println("Nome no sistema: " + rsmAlunosEjaPeriodoAtual.getString("nm_aluno_search"));
//						System.out.println("Data de Nascimento no sistema: " + Util.convCalendarStringCompleto(rsmAlunosEjaPeriodoAtual.getGregorianCalendar("dt_nascimento")));
//						System.out.println("Nome da mÃ£e no sistema: " + rsmAlunosEjaPeriodoAtual.getString("nm_mae_search"));
//						System.out.println("\tNome no centaurus: " + rsmCentaurus.getString("nm_aluno_search"));
//						System.out.println("\tData de Nascimento no centaurus: " + Util.convCalendarStringCompleto(rsmCentaurus.getGregorianCalendar("dt_nascimento")));
//						System.out.println("\tNome da mÃ£e no centaurus: " + rsmCentaurus.getString("nm_mae_search"));
//						System.out.println("\tNome da modalidade no centaurus: " + rsmCentaurus.getString("nm_modalidade_smed"));
//						System.out.println();
//						System.out.println();
//					}
//					rsmCentaurus.beforeFirst();
				}
			}
			rsmFinal.beforeFirst();
			System.out.println("Quantidade de alunos egressos "+periodoReferencia+" em referencia a "+periodoAnterior+": " + rsmFinal.size());
//			System.out.println("Quantidade de alunos possivelmente duplicados: " + cont);
			
			
			
			
			
//			int qtMatriculadosZonaUrbana = 0;
//			int qtMatriculadosZonaRural = 0;
//			int qtMatriculadosCreche = 0;
//			ArrayList<Integer> codigosAluno = new ArrayList<Integer>();
//			int x = 0;
//			while(rsmFinal.next()){
//				
//				rsmFinal.setValueToField("NM_ZONA_ALUNO", PessoaEnderecoServices.tiposZona[rsmFinal.getInt("tp_zona_aluno")]);
//				
//				GregorianCalendar dtNascimento = rsmFinal.getGregorianCalendar("dt_nascimento");
//				
//				Cidade naturalidade = CidadeDAO.get(rsmFinal.getInt("cd_naturalidade"), connectLocal);
//				if(naturalidade != null){
//					rsmFinal.setValueToField("NM_NATURALIDADE", naturalidade.getNmCidade());
//					Estado estadoNaturalidade = EstadoDAO.get(naturalidade.getCdEstado(), connectLocal);
//					if(estadoNaturalidade != null)
//						rsmFinal.setValueToField("SG_NATURALIDADE", estadoNaturalidade.getSgEstado());
//				}
//				rsmFinal.setValueToField("NR_TELEFONE", (rsmFinal.getString("NR_TELEFONE1") != null && !rsmFinal.getString("NR_TELEFONE1").trim().equals("") ? rsmFinal.getString("NR_TELEFONE1") : (rsmFinal.getString("NR_TELEFONE2") != null && !rsmFinal.getString("NR_TELEFONE2").trim().equals("") ? rsmFinal.getString("NR_TELEFONE2") : (rsmFinal.getString("NR_CELULAR") != null && !rsmFinal.getString("NR_CELULAR").trim().equals("") ? rsmFinal.getString("NR_CELULAR") : rsmFinal.getString("NR_TELEFONE")))));
//				
//				int nrIdadeAluno = Util.getIdade(dtNascimento);
//				if(dtNascimento != null)
//					rsmFinal.setValueToField("DT_NM_NASCIMENTO", (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.DAY_OF_MONTH))) > 9 ? dtNascimento.get(Calendar.DAY_OF_MONTH) : "0" + dtNascimento.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1)) + "/" + dtNascimento.get(Calendar.YEAR) + " (" + nrIdadeAluno + " anos)");
//				
//				rsmFinal.setValueToField("NR_IDADE_ALUNO", nrIdadeAluno);
//				
//				int nrDivergencia = rsmFinal.getInt("nr_idade") - nrIdadeAluno;
//				
//				rsmFinal.setValueToField("NR_DIVERGENCIA", (nrDivergencia < 0 ? (-1) * nrDivergencia : nrDivergencia));
//				
//				if(rsmFinal.getGregorianCalendar("dt_matricula") != null){
//					rsmFinal.setValueToField("NR_MES", rsmFinal.getGregorianCalendar("dt_matricula").get(Calendar.MONTH));
//					rsmFinal.setValueToField("NR_ANO", rsmFinal.getGregorianCalendar("dt_matricula").get(Calendar.YEAR));
//					rsmFinal.setValueToField("NM_MES", Util.meses[rsmFinal.getInt("NR_MES")] + "/" + rsmFinal.getInt("NR_ANO"));
//				}
//				
//				
//				if(codigosAluno.size() > 0 && codigosAluno.contains(rsmFinal.getInt("cd_pessoa"))){
//					rsmFinal.deleteRow();
//					if(x == 0)
//						rsmFinal.beforeFirst();
//					else
//						rsmFinal.previous();
//					continue;
//				}
//				else{
//					codigosAluno.add(rsmFinal.getInt("cd_pessoa"));
//				}
//				
//				
//				Matricula matricula = MatriculaDAO.get(rsmFinal.getInt("cd_matricula"), connectLocal);
//				if(matricula != null){
//					Turma turma = TurmaDAO.get(matricula.getCdTurma(), connectLocal);
//					if(turma != null){
//						rsmFinal.setValueToField("CL_TURMA", turma.getNmTurma() + " - " + TurmaServices.tiposTurno[turma.getTpTurno()]);
//						
//						rsmFinal.setValueToField("CL_TURNO", TurmaServices.tiposTurno[turma.getTpTurno()]);
//						rsmFinal.setValueToField("TP_TURNO", turma.getTpTurno());
//						
//						Curso curso = CursoDAO.get(turma.getCdCurso(), connectLocal);
//						if(curso != null){
//							rsmFinal.setValueToField("CD_CURSO", curso.getCdCurso());
//							rsmFinal.setValueToField("ID_PRODUTO_SERVICO_CURSO", curso.getIdProdutoServico());
//							rsmFinal.setValueToField("NM_MODALIDADE_CURSO", curso.getNmProdutoServico());
//						}
//						
//						Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connectLocal);
//						if(instituicao != null){
//							rsmFinal.setValueToField("NM_INSTITUICAO", instituicao.getNmPessoa());
//							
////								PessoaEndereco instituicaoEndereco = PessoaEnderecoDAO.get(1, instituicao.getCdInstituicao(), connect);
////								if(instituicaoEndereco != null)
//								
//							
//							
//							InstituicaoEducacenso instituicaoEducacenso = InstituicaoEducacensoDAO.get(instituicao.getCdInstituicao(), connectLocal);
//							if(instituicaoEducacenso != null){
//								
//								rsmFinal.setValueToField("NM_ZONA", PessoaEnderecoServices.tiposZona[instituicaoEducacenso.getTpLocalizacao()]);
//								
//								if(instituicaoEducacenso.getTpLocalizacao() == PessoaEnderecoServices.TP_ZONA_RURAL && (instituicao.getTpInstituicao() == InstituicaoServices.TP_INSTITUICAO_ESCOLA)){
//									qtMatriculadosZonaRural += 1;
//								}
//								else if(instituicaoEducacenso.getTpLocalizacao() == PessoaEnderecoServices.TP_ZONA_URBANA && (instituicao.getTpInstituicao() == InstituicaoServices.TP_INSTITUICAO_ESCOLA)){
//									qtMatriculadosZonaUrbana += 1;
//								}
//								else if(instituicao.getTpInstituicao() == InstituicaoServices.TP_INSTITUICAO_CRECHE){
//									qtMatriculadosCreche += 1;
//								}
//								
//							}
//							
//						}
//					}
//				}
//				else{
//					rsmFinal.deleteRow();
//					if(x == 0)
//						rsmFinal.beforeFirst();
//					else
//						rsmFinal.previous();
//					continue;
//				}
//				x++;
//			}
//			rsmFinal.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			
			fields.add("NM_ZONA");
			fields.add("NM_INSTITUICAO");
			fields.add("ID_PRODUTO_SERVICO_CURSO");
			fields.add("CL_TURMA");
			fields.add("NM_PESSOA");
			
			rsmFinal.orderBy(fields);
			rsmFinal.beforeFirst();
			
			//Da commit em ambos
			connectLocal.commit();
			
			Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsmFinal);
//			result.addObject("QT_MATRICULADOS_ZONA_URBANA", qtMatriculadosZonaUrbana);
//			result.addObject("QT_MATRICULADOS_ZONA_RURAL", qtMatriculadosZonaRural);
//			result.addObject("QT_MATRICULADOS_CRECHE", qtMatriculadosCreche);
			return result;
				
			
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connectLocal);
			return new Result(-1, "Erro na sincronizaÃ§Ã£o");
		}
		finally{
			Conexao.desconectar(connectLocal);
		}
	}

	
	public static Result findRelatorioEjaEgressosPorPeriodoModalidade(){
		String nmUrlDatabase = "jdbc:postgresql://127.0.0.1/edf_centaurus";
		String nmLoginDataBase = "postgres";
		String nmSenhaDatBase = "CDF3DP!";
		
		return findRelatorioEjaEgressosPorPeriodoModalidade(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase, 2012, 2011);
	}
	public static Result findRelatorioEjaEgressosPorPeriodoModalidade(String nmUrlDatabase, String nmLoginDataBase, String nmSenhaDatBase, int periodoReferencia, int periodoAnterior){
		//Faz a conexao com o Local
		Connection connectLocal = Conexao.conectar(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
		try{
			connectLocal.setAutoCommit(false);
			Connection connectEdf = Conexao.conectar();
			connectEdf.setAutoCommit(false);
			
//			ResultSetMap rsmAlunosEjaPeriodoAtual = new ResultSetMap(connectLocal.prepareStatement("SELECT * FROM aluno A LEFT OUTER JOIN uf U ON (A.id_uf_identidade = U.id_uf), matricula B, escola_modalidade_smed C, modalidade_smed D, modalidade E "
//					+ "																							WHERE A.id_aluno = B.id_aluno "
//					+ "																							  AND B.ano_matricula = "+periodoReferencia
//					+ "																							  AND B.id_escola_modalidade_smed = C.id_escola_modalidade_smed "
//					+ "                   																		  AND C.id_modalidade_smed = D.id_modalidade_smed "
//					+ "																							  AND D.id_modalidade = E.id_modalidade "
//					+ "																							  AND B.st_matricula = 1 "
//					+ "																							  AND E.id_modalidade = 3 ORDER BY nm_aluno_search").executeQuery());
//			 
			
			ResultSetMap rsmAlunosEjaPeriodoAtual = new ResultSetMap(connectEdf.prepareStatement("SELECT P.cd_pessoa, P.nm_pessoa, P.nm_pessoa AS nm_aluno, P.nr_telefone1, P.nr_telefone2, P.nr_celular, PF.dt_nascimento, PF.nm_mae, PF.nm_pai, A.nm_responsavel, F.cd_cidade, F.nm_logradouro, F.nm_bairro, F.nr_endereco, F.nm_complemento, F.nr_telefone, F.tp_zona AS tp_zona_aluno, F.nr_cep, G.nm_cidade, " 
	 			    +"	  																						A.cd_aluno, A.lg_bolsa_familia, A.lg_pai_nao_declarado, A.lg_cadastro_problema, B.cd_matricula, B.nr_matricula, B.st_matricula, B.dt_matricula, PF.cd_naturalidade, IP.nm_periodo_letivo, C.nr_idade,  "
	 			    +"																							H.nm_produto_servico AS nm_curso  FROM grl_pessoa P "
					+ "																			      LEFT OUTER JOIN grl_pessoa_tipo_documentacao PTD ON (P.cd_pessoa = PTD.cd_pessoa AND PTD.cd_tipo_documentacao IN ("+PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_NASCIMENTO+", "+PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_CASAMENTO+")), "
					+ "																				grl_pessoa_fisica PF"
					+ "																				  LEFT OUTER JOIN grl_estado ERG ON (PF.cd_estado_rg = ERG.cd_estado), "
					+ "																				acd_aluno A, acd_matricula B, acd_instituicao_periodo IP, acd_curso C, acd_curso_etapa D, acd_tipo_etapa E, grl_pessoa_endereco F, grl_cidade G, grl_produto_servico H "
					+ "																					WHERE P.cd_pessoa = A.cd_aluno "
					+ "																					  AND P.cd_pessoa = PF.cd_pessoa "
					+ "																					  AND A.cd_aluno = B.cd_aluno "
					+ "																					  AND B.cd_periodo_letivo = IP.cd_periodo_letivo "
					+ "																					  AND IP.nm_periodo_letivo = '"+periodoReferencia+"' "
					+ "																					  AND B.cd_curso = C.cd_curso "
					+ "																					  AND C.cd_curso = D.cd_curso "
					+ "																					  AND D.cd_etapa = E.cd_etapa "
					+ "																					  AND P.cd_pessoa = F.cd_pessoa "
					+ "																					  AND F.lg_principal = 1 "
					+ "																					  AND F.cd_cidade = G.cd_cidade "
					+ "																					  AND C.cd_curso = H.cd_produto_servico "
					+ "																					  AND E.lg_eja = 1 "
					+ "																					  AND st_matricula = "+MatriculaServices.ST_ATIVA+" ORDER BY P.nm_pessoa ").executeQuery());
			 
			
			
			
			System.out.println("rsmAlunosEjaPeriodoAtual.size = " + rsmAlunosEjaPeriodoAtual.size());
			int cont = 0;
			ResultSetMap rsmFinal = new ResultSetMap();
			while(rsmAlunosEjaPeriodoAtual.next()){
				PreparedStatement pstmt = connectLocal.prepareStatement("SELECT * FROM aluno A"
						+ "														LEFT OUTER JOIN uf U ON (A.id_uf_identidade = U.id_uf), "
						+ "													matricula B, escola_modalidade_smed C, modalidade_smed D, modalidade E "
						+ "																							WHERE A.id_aluno = B.id_aluno "
						+ "																							  AND B.ano_matricula = "+periodoAnterior
						+ "																							  AND B.id_escola_modalidade_smed = C.id_escola_modalidade_smed "
						+ "                   																		  AND C.id_modalidade_smed = D.id_modalidade_smed "
						+ "																							  AND D.id_modalidade = E.id_modalidade "
						+ "																							  AND ((A.nm_aluno_search = ? AND A.nm_mae_search = ? AND (A.dt_nascimento = '"+Util.convCalendarStringSql(rsmAlunosEjaPeriodoAtual.getGregorianCalendar("dt_nascimento"))+"' OR A.nm_pai_search = ?)) OR (nr_identidade IS NOT NULL AND nr_identidade <> '' AND nr_identidade = ? AND U.cod_uf = ?) OR (cert_nr_regristo IS NOT NULL AND cert_nr_regristo <> '' AND cert_nr_regristo = ?) OR (cert_nr_termo IS NOT NULL AND cert_nr_termo <> '' AND cert_nr_termo = ? AND cert_nr_folha = ? AND cert_nr_livro = ?) OR (nr_cpf IS NOT NULL AND nr_cpf <> '' AND nr_cpf = ?)) "
						+ "																							  AND B.st_matricula = 1 "
						+ "																							  AND E.id_modalidade <> 3 ORDER BY nm_aluno_search");

				
				pstmt.setString(1, rsmAlunosEjaPeriodoAtual.getString("nm_pessoa").replaceAll("Ã‡", "C").trim());
				pstmt.setString(2, rsmAlunosEjaPeriodoAtual.getString("nm_mae").replaceAll("Ã‡", "C").trim());
				pstmt.setString(3, (rsmAlunosEjaPeriodoAtual.getString("nm_pai") != null && !rsmAlunosEjaPeriodoAtual.getString("nm_pai").equals("NÃƒO DECLARADO") ? rsmAlunosEjaPeriodoAtual.getString("nm_pai").replaceAll("Ã‡", "C").trim() : null));
				pstmt.setString(4, rsmAlunosEjaPeriodoAtual.getString("nr_rg"));
				pstmt.setInt(5, (rsmAlunosEjaPeriodoAtual.getString("id_estado") != null ? Integer.parseInt(rsmAlunosEjaPeriodoAtual.getString("id_estado")) : 0));
				pstmt.setString(6, rsmAlunosEjaPeriodoAtual.getString("nr_documento"));
				pstmt.setString(7, rsmAlunosEjaPeriodoAtual.getString("nr_documento"));
				pstmt.setString(8, rsmAlunosEjaPeriodoAtual.getString("folha"));
				pstmt.setString(9, rsmAlunosEjaPeriodoAtual.getString("livro"));
				pstmt.setString(10, rsmAlunosEjaPeriodoAtual.getString("nr_cpf"));
				
				ResultSetMap rsmAlunosEjaPeriodoAnterior = new ResultSetMap(pstmt.executeQuery());
				if(rsmAlunosEjaPeriodoAnterior.next()){
					rsmFinal.addRegister(rsmAlunosEjaPeriodoAtual.getRegister());
					
					
					
//					boolean achado = false;
//					while(rsmCentaurus.next()){
//						if(rsmAlunosEjaPeriodoAtual.getString("nm_aluno_search") == null || rsmAlunosEjaPeriodoAtual.getString("nm_aluno_search").equals("")){
//							break;
//						}
//						if(rsmAlunosEjaPeriodoAtual.getString("nm_mae_search") == null || rsmAlunosEjaPeriodoAtual.getString("nm_mae_search").equals("")){
//							break;
//						}
//						if(rsmAlunosEjaPeriodoAtual.getGregorianCalendar("dt_nascimento") == null){
//							break;
//						}
//						
//						if(rsmCentaurus.getString("nm_aluno_search") == null || rsmCentaurus.getString("nm_aluno_search").equals("")){
//							continue;
//						}
//						if(rsmCentaurus.getString("nm_mae_search") == null || rsmCentaurus.getString("nm_mae_search").equals("")){
//							continue;
//						}
//						if(rsmCentaurus.getGregorianCalendar("dt_nascimento") == null){
//							continue;
//						}
//						
//						double porcentagem = Util.compareStrings(rsmAlunosEjaPeriodoAtual.getString("nm_aluno_search").trim(), rsmAlunosEjaPeriodoAtual.getString("nm_mae_search").trim(), rsmAlunosEjaPeriodoAtual.getGregorianCalendar("dt_nascimento"), rsmCentaurus.getString("nm_aluno_search").trim(), rsmCentaurus.getString("nm_mae_search").trim(), rsmCentaurus.getGregorianCalendar("dt_nascimento"));
//						if(porcentagem > 0.8){
//							achado = true;
//							break;
//						}
//					}
//					
//					if(achado){
//						cont++;
//						System.out.println("Nome no sistema: " + rsmAlunosEjaPeriodoAtual.getString("nm_aluno_search"));
//						System.out.println("Data de Nascimento no sistema: " + Util.convCalendarStringCompleto(rsmAlunosEjaPeriodoAtual.getGregorianCalendar("dt_nascimento")));
//						System.out.println("Nome da mÃ£e no sistema: " + rsmAlunosEjaPeriodoAtual.getString("nm_mae_search"));
//						System.out.println("\tNome no centaurus: " + rsmCentaurus.getString("nm_aluno_search"));
//						System.out.println("\tData de Nascimento no centaurus: " + Util.convCalendarStringCompleto(rsmCentaurus.getGregorianCalendar("dt_nascimento")));
//						System.out.println("\tNome da mÃ£e no centaurus: " + rsmCentaurus.getString("nm_mae_search"));
//						System.out.println("\tNome da modalidade no centaurus: " + rsmCentaurus.getString("nm_modalidade_smed"));
//						System.out.println();
//						System.out.println();
//					}
//					rsmCentaurus.beforeFirst();
				}
			}
			rsmFinal.beforeFirst();
			System.out.println("Quantidade de alunos egressos de outras modalidades "+periodoReferencia+" em referencia a "+periodoAnterior+": " + rsmFinal.size());
//			System.out.println("Quantidade de alunos possivelmente duplicados: " + cont);
			
			
			
			
			
//			int qtMatriculadosZonaUrbana = 0;
//			int qtMatriculadosZonaRural = 0;
//			int qtMatriculadosCreche = 0;
//			ArrayList<Integer> codigosAluno = new ArrayList<Integer>();
//			int x = 0;
//			while(rsmFinal.next()){
//				
//				rsmFinal.setValueToField("NM_ZONA_ALUNO", PessoaEnderecoServices.tiposZona[rsmFinal.getInt("tp_zona_aluno")]);
//				
//				GregorianCalendar dtNascimento = rsmFinal.getGregorianCalendar("dt_nascimento");
//				
//				Cidade naturalidade = CidadeDAO.get(rsmFinal.getInt("cd_naturalidade"), connectLocal);
//				if(naturalidade != null){
//					rsmFinal.setValueToField("NM_NATURALIDADE", naturalidade.getNmCidade());
//					Estado estadoNaturalidade = EstadoDAO.get(naturalidade.getCdEstado(), connectLocal);
//					if(estadoNaturalidade != null)
//						rsmFinal.setValueToField("SG_NATURALIDADE", estadoNaturalidade.getSgEstado());
//				}
//				rsmFinal.setValueToField("NR_TELEFONE", (rsmFinal.getString("NR_TELEFONE1") != null && !rsmFinal.getString("NR_TELEFONE1").trim().equals("") ? rsmFinal.getString("NR_TELEFONE1") : (rsmFinal.getString("NR_TELEFONE2") != null && !rsmFinal.getString("NR_TELEFONE2").trim().equals("") ? rsmFinal.getString("NR_TELEFONE2") : (rsmFinal.getString("NR_CELULAR") != null && !rsmFinal.getString("NR_CELULAR").trim().equals("") ? rsmFinal.getString("NR_CELULAR") : rsmFinal.getString("NR_TELEFONE")))));
//				
//				int nrIdadeAluno = Util.getIdade(dtNascimento);
//				if(dtNascimento != null)
//					rsmFinal.setValueToField("DT_NM_NASCIMENTO", (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.DAY_OF_MONTH))) > 9 ? dtNascimento.get(Calendar.DAY_OF_MONTH) : "0" + dtNascimento.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1)) + "/" + dtNascimento.get(Calendar.YEAR) + " (" + nrIdadeAluno + " anos)");
//				
//				rsmFinal.setValueToField("NR_IDADE_ALUNO", nrIdadeAluno);
//				
//				int nrDivergencia = rsmFinal.getInt("nr_idade") - nrIdadeAluno;
//				
//				rsmFinal.setValueToField("NR_DIVERGENCIA", (nrDivergencia < 0 ? (-1) * nrDivergencia : nrDivergencia));
//				
//				if(rsmFinal.getGregorianCalendar("dt_matricula") != null){
//					rsmFinal.setValueToField("NR_MES", rsmFinal.getGregorianCalendar("dt_matricula").get(Calendar.MONTH));
//					rsmFinal.setValueToField("NR_ANO", rsmFinal.getGregorianCalendar("dt_matricula").get(Calendar.YEAR));
//					rsmFinal.setValueToField("NM_MES", Util.meses[rsmFinal.getInt("NR_MES")] + "/" + rsmFinal.getInt("NR_ANO"));
//				}
//				
//				
//				if(codigosAluno.size() > 0 && codigosAluno.contains(rsmFinal.getInt("cd_pessoa"))){
//					rsmFinal.deleteRow();
//					if(x == 0)
//						rsmFinal.beforeFirst();
//					else
//						rsmFinal.previous();
//					continue;
//				}
//				else{
//					codigosAluno.add(rsmFinal.getInt("cd_pessoa"));
//				}
//				
//				
//				Matricula matricula = MatriculaDAO.get(rsmFinal.getInt("cd_matricula"), connectLocal);
//				if(matricula != null){
//					Turma turma = TurmaDAO.get(matricula.getCdTurma(), connectLocal);
//					if(turma != null){
//						rsmFinal.setValueToField("CL_TURMA", turma.getNmTurma() + " - " + TurmaServices.tiposTurno[turma.getTpTurno()]);
//						
//						rsmFinal.setValueToField("CL_TURNO", TurmaServices.tiposTurno[turma.getTpTurno()]);
//						rsmFinal.setValueToField("TP_TURNO", turma.getTpTurno());
//						
//						Curso curso = CursoDAO.get(turma.getCdCurso(), connectLocal);
//						if(curso != null){
//							rsmFinal.setValueToField("CD_CURSO", curso.getCdCurso());
//							rsmFinal.setValueToField("ID_PRODUTO_SERVICO_CURSO", curso.getIdProdutoServico());
//							rsmFinal.setValueToField("NM_MODALIDADE_CURSO", curso.getNmProdutoServico());
//						}
//						
//						Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connectLocal);
//						if(instituicao != null){
//							rsmFinal.setValueToField("NM_INSTITUICAO", instituicao.getNmPessoa());
//							
////								PessoaEndereco instituicaoEndereco = PessoaEnderecoDAO.get(1, instituicao.getCdInstituicao(), connect);
////								if(instituicaoEndereco != null)
//								
//							
//							
//							InstituicaoEducacenso instituicaoEducacenso = InstituicaoEducacensoDAO.get(instituicao.getCdInstituicao(), connectLocal);
//							if(instituicaoEducacenso != null){
//								
//								rsmFinal.setValueToField("NM_ZONA", PessoaEnderecoServices.tiposZona[instituicaoEducacenso.getTpLocalizacao()]);
//								
//								if(instituicaoEducacenso.getTpLocalizacao() == PessoaEnderecoServices.TP_ZONA_RURAL && (instituicao.getTpInstituicao() == InstituicaoServices.TP_INSTITUICAO_ESCOLA)){
//									qtMatriculadosZonaRural += 1;
//								}
//								else if(instituicaoEducacenso.getTpLocalizacao() == PessoaEnderecoServices.TP_ZONA_URBANA && (instituicao.getTpInstituicao() == InstituicaoServices.TP_INSTITUICAO_ESCOLA)){
//									qtMatriculadosZonaUrbana += 1;
//								}
//								else if(instituicao.getTpInstituicao() == InstituicaoServices.TP_INSTITUICAO_CRECHE){
//									qtMatriculadosCreche += 1;
//								}
//								
//							}
//							
//						}
//					}
//				}
//				else{
//					rsmFinal.deleteRow();
//					if(x == 0)
//						rsmFinal.beforeFirst();
//					else
//						rsmFinal.previous();
//					continue;
//				}
//				x++;
//			}
//			rsmFinal.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			
			fields.add("NM_ZONA");
			fields.add("NM_INSTITUICAO");
			fields.add("ID_PRODUTO_SERVICO_CURSO");
			fields.add("CL_TURMA");
			fields.add("NM_PESSOA");
			
			rsmFinal.orderBy(fields);
			rsmFinal.beforeFirst();
			
			//Da commit em ambos
			connectLocal.commit();
			
			Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsmFinal);
//			result.addObject("QT_MATRICULADOS_ZONA_URBANA", qtMatriculadosZonaUrbana);
//			result.addObject("QT_MATRICULADOS_ZONA_RURAL", qtMatriculadosZonaRural);
//			result.addObject("QT_MATRICULADOS_CRECHE", qtMatriculadosCreche);
			return result;
				
			
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connectLocal);
			return new Result(-1, "Erro na sincronizaÃ§Ã£o");
		}
		finally{
			Conexao.desconectar(connectLocal);
		}
	}

	public static Result atualizarInepAlunos2015(){
		//Faz a conexao com o Local
		Connection connectLocal = Conexao.conectar();
		try{
			connectLocal.setAutoCommit(false);
			
			File arquivo = new File("C://educacenso.txt"); 
			
			
			RandomAccessFile raf = new RandomAccessFile(arquivo, "rw");
			
			String codInepEscola      		  = "";
			String vlLatitude       		  = "";
			String vlLongitude       		  = "";
			
			String codInepAluno      		  = "";
			String nmAluno                    = "";
			String dtNascimentoAluno          = "";
			String nmMaeAluno                 = "";
			String nmPaiAluno                 = "";
			
			String nrRgAluno                  = "";
			String sgUfRgAluno                = "";
			String nrCertidaoCivilAluno       = "";
			String nrFolhaCertCivilAluno      = "";
			String nrLivroCertCivilAluno      = "";
			String nrCpfAluno   			  = "";
			
			
			ResultSetMap rsmFinal = new ResultSetMap();
			String line = null;
			int count = 0;
			for(int i = 0; (line = raf.readLine()) != null; i++)	{
				
				String tpRegistro = line.substring(0, 2).trim();
				
				String[] linhas = line.split("\\|");
				
//				if(tpRegistro.equals("00")){
//					if(linhas.length < 35){
//						String[] linhasAux = linhas.clone();
//						linhas = new String[35];
//						int j = 0;
//						for(; j < linhasAux.length; j++){
//							linhas[j] = linhasAux[j];
//						}
//						
//						for(; j < 35; j++){
//							linhas[j] = "";
//						}
//					}
//					
//					//---------------------------Busca das informaÃ§Ãµes na linha
//					codInepEscola  		= linhas[1].trim();
//					vlLatitude          = linhas[6].trim();
//					vlLongitude         = linhas[7].trim();
//					
//					ResultSetMap rsmInstituicao = new ResultSetMap(connectLocal.prepareStatement("SELECT * FROM acd_instituicao WHERE nr_inep = '"+codInepEscola+"'").executeQuery());
//					if(rsmInstituicao.next()){
//						ResultSetMap rsmLocalizacao = new ResultSetMap(connectLocal.prepareStatement("SELECT * FROM geo_localizacao WHERE cd_pessoa = " + rsmInstituicao.getInt("cd_instituicao")).executeQuery());
//						while(rsmLocalizacao.next()){
//							connectLocal.prepareStatement("UPDATE geo_ponto set vl_latitude = " + (vlLatitude.equals("") ? 0 : Float.parseFloat(vlLatitude.replaceAll(",", "."))) + ", vl_longitude = " + (vlLongitude.equals("") ? 0 : Float.parseFloat(vlLongitude.replaceAll(",", "."))) + " WHERE cd_referencia = " + rsmLocalizacao.getInt("cd_referencia")).executeUpdate();
//						}
//					}
//				}
				
				if(tpRegistro.equals("60")){
					if(linhas.length < 40){
						String[] linhasAux = linhas.clone();
						linhas = new String[40];
						int j = 0;
						for(; j < linhasAux.length; j++){
							linhas[j] = linhasAux[j];
						}
						
						for(; j < 40; j++){
							linhas[j] = "";
						}
					}
					
					
					
					codInepAluno      		   = linhas[2].trim();
					nmAluno                    = linhas[4].trim();
					dtNascimentoAluno          = linhas[5].trim();
					nmMaeAluno                 = linhas[9].trim();
					nmPaiAluno                 = linhas[10].trim();
					//2014
//					nmAluno                    = linhas[4].trim();
//					dtNascimentoAluno          = linhas[6].trim();
//					nmMaeAluno                 = linhas[10].trim();
//					nmPaiAluno                 = linhas[11].trim();
				}
				
				//Registro 70 - Documento do Aluno
				else if(tpRegistro.equals("70")){
					nrRgAluno                  = linhas[4].trim();
					sgUfRgAluno   		       = linhas[6].trim();
					nrCertidaoCivilAluno       = linhas[11].trim();
					nrFolhaCertCivilAluno      = linhas[12].trim();
					nrLivroCertCivilAluno      = linhas[13].trim();
					nrCpfAluno   			   = linhas[19].trim();
					
					Estado estado = EstadoServices.getBySg(sgUfRgAluno, connectLocal);
					
//					PreparedStatement pstmt = connectLocal.prepareStatement("SELECT * FROM aluno A"
//							+ "														LEFT OUTER JOIN uf U ON (A.id_uf_identidade = U.id_uf), "
//							+ "													matricula B, escola_modalidade_smed C, modalidade_smed D, modalidade E "
//							+ "																							WHERE A.id_aluno = B.id_aluno "
//							+ "																							  AND B.ano_matricula = "+periodoAnterior
//							+ "																							  AND B.id_escola_modalidade_smed = C.id_escola_modalidade_smed "
//							+ "                   																		  AND C.id_modalidade_smed = D.id_modalidade_smed "
//							+ "																							  AND D.id_modalidade = E.id_modalidade "
//							+ "																							  AND ((A.nm_aluno_search = ? AND A.nm_mae_search = ? AND (A.dt_nascimento = '"+Util.convCalendarStringSql(rsmAlunosEjaPeriodoAtual.getGregorianCalendar("dt_nascimento"))+"' OR A.nm_pai_search = ?)) OR (nr_identidade IS NOT NULL AND nr_identidade <> '' AND nr_identidade = ? AND U.cod_uf = ?) OR (cert_nr_regristo IS NOT NULL AND cert_nr_regristo <> '' AND cert_nr_regristo = ?) OR (cert_nr_termo IS NOT NULL AND cert_nr_termo <> '' AND cert_nr_termo = ? AND cert_nr_folha = ? AND cert_nr_livro = ?) OR (nr_cpf IS NOT NULL AND nr_cpf <> '' AND nr_cpf = ?) OR (A.id_aluno = ?)) "
//							+ "																							  AND B.st_matricula = 1 "
//							+ "																							  AND E.id_modalidade = 3 ORDER BY nm_aluno_search");
					PreparedStatement pstmt = connectLocal.prepareStatement("SELECT P.cd_pessoa, P.nm_pessoa, P.nm_pessoa AS nm_aluno, P.nr_telefone1, P.nr_telefone2, P.nr_celular, PF.dt_nascimento, PF.nm_mae, PF.nm_pai, A.nm_responsavel, F.cd_cidade, F.nm_logradouro, F.nm_bairro, F.nr_endereco, F.nm_complemento, F.nr_telefone, F.tp_zona AS tp_zona_aluno, F.nr_cep, G.nm_cidade, " 
			 			    +"	  																						A.cd_aluno, A.lg_bolsa_familia, A.lg_pai_nao_declarado, A.lg_cadastro_problema, B.cd_matricula, B.nr_matricula, B.st_matricula, B.dt_matricula, PF.cd_naturalidade, IP.nm_periodo_letivo, C.nr_idade,  "
			 			    +"																							H.nm_produto_servico AS nm_curso  FROM grl_pessoa P "
							+ "																			      LEFT OUTER JOIN grl_pessoa_tipo_documentacao PTD ON (P.cd_pessoa = PTD.cd_pessoa AND PTD.cd_tipo_documentacao IN ("+PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_NASCIMENTO+", "+PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_CASAMENTO+")), "
							+ "																				grl_pessoa_fisica PF"
							+ "																				  LEFT OUTER JOIN grl_estado ERG ON (PF.cd_estado_rg = ERG.cd_estado), "
							+ "																				acd_aluno A, acd_matricula B, acd_instituicao_periodo IP, acd_curso C, acd_curso_etapa D, acd_tipo_etapa E, grl_pessoa_endereco F, grl_cidade G, grl_produto_servico H "
							+ "																					WHERE P.cd_pessoa = A.cd_aluno "
							+ "																					  AND P.cd_pessoa = PF.cd_pessoa "
							+ "																					  AND A.cd_aluno = B.cd_aluno "
							+ "																					  AND B.cd_periodo_letivo = IP.cd_periodo_letivo "
							+ "																					  AND IP.nm_periodo_letivo = '2016' "
							+ "																					  AND B.cd_curso = C.cd_curso "
							+ "																					  AND C.cd_curso = D.cd_curso "
							+ "																					  AND D.cd_etapa = E.cd_etapa "
							+ "																					  AND P.cd_pessoa = F.cd_pessoa "
							+ "																					  AND F.lg_principal = 1 "
							+ "																					  AND F.cd_cidade = G.cd_cidade "
							+ "																					  AND C.cd_curso = H.cd_produto_servico "
							+ "																					  AND ((P.nm_pessoa = ? AND PF.nm_mae = ? AND (PF.dt_nascimento = '"+dtNascimentoAluno+"' OR PF.nm_pai = ?))) "
							+ "																					  AND st_matricula = "+MatriculaServices.ST_ATIVA+" ORDER BY P.nm_pessoa ");
					 
					
					pstmt.setString(1, nmAluno);
					pstmt.setString(2, nmMaeAluno);
					pstmt.setString(3, (nmPaiAluno != null ? nmPaiAluno : null));
					ResultSetMap rsmAlunos = new ResultSetMap(pstmt.executeQuery());
					if(rsmAlunos.next()){
						count++;
						System.out.println(i + ": " + line);
						System.out.println("codInep = " + codInepAluno);
						System.out.println("(BASE 2015) nome = " + nmAluno + " mae = " + nmMaeAluno + " nascimento = " + dtNascimentoAluno);
						Aluno aluno = AlunoDAO.get(rsmAlunos.getInt("cd_aluno"), connectLocal);
						System.out.println("cod = " + aluno.getCdAluno() + " nome = " + aluno.getNmPessoa() + " nome mae = " + aluno.getNmMae() + " data de nascimento = " + Util.convCalendarStringCompleto(aluno.getDtNascimento()));
						System.out.println();
						System.out.println();
						aluno.setNrInep(codInepAluno);
						if(AlunoDAO.update(aluno, connectLocal) < 0){
							Conexao.rollback(connectLocal);
							return new Result(-1);
						}
						
					}
					
					
					
				}
				
				
			}
			
			System.out.println("Quantidade de INEPs = " + count);
			rsmFinal.beforeFirst();
			
			//Da commit em ambos
			connectLocal.commit();
			
			Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsmFinal);
			return result;
				
			
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connectLocal);
			return new Result(-1, "Erro na sincronizaÃ§Ã£o");
		}
		finally{
			Conexao.desconectar(connectLocal);
		}
	}
	
	public static sol.util.Result importNISFromCSV() throws SQLException	{
		return importNISFromCSV(null);
	}
	
	public static sol.util.Result importNISFromCSV(Connection connect) throws SQLException	{
		boolean isConnectionNull = connect == null;
    	RandomAccessFile raf = null;
		
		/***********************
		 * Importando Pais
		 ***********************/
		
    	System.out.println("Importando paises de arquivo CSV...");
		ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("C:\\COLETA DO NIS.csv", ";", true);
  		System.out.println("Arquivo carregado...");
		try {
		
			connect   = (isConnectionNull ? Conexao.conectar() : connect);
			connect.setAutoCommit((isConnectionNull ? false : connect.getAutoCommit()));
			
			
			int qtAlunosNaoEncontrados = 0;
			int qtAlunosSemNis = 0;
			int qtAlunosComNis = 0;
			int qtAlunosEncontrados = 0;
			
			while(rsm.next())	{
				String nmAluno = rsm.getString("ALUNO").replaceAll("Ã�", "A")
														.replaceAll("Ã¡", "A")
														.replaceAll("Ã‰", "E")
														.replaceAll("Ã©", "E")
														.replaceAll("Ã�", "I")
														.replaceAll("Ã­", "I")
														.replaceAll("Ã“", "O")
														.replaceAll("Ã³", "O")
														.replaceAll("Ãš", "U")
														.replaceAll("Ãº", "u")
														.replaceAll("Ãƒ", "A")
														.replaceAll("Ã£", "A")
														.replaceAll("Ã•", "O")
														.replaceAll("Ãµ", "Ã•")
														.replaceAll("Ã‚", "A")
														.replaceAll("Ã¢", "A")
														.replaceAll("ÃŠ", "E")
														.replaceAll("Ãª", "E")
														.replaceAll("ÃŽ", "I")
														.replaceAll("Ã®", "I")
														.replaceAll("Ã”", "O")
														.replaceAll("Ã´", "O")
														.replaceAll("Ã›", "U")
														.replaceAll("Ã»", "U")
														.replaceAll("Ã€", "A")
														.replaceAll("Ã ", "A")
														.replaceAll("Ãˆ", "E")
														.replaceAll("Ã¨", "E")
														.replaceAll("ÃŒ", "I")
														.replaceAll("Ã¬", "I")
														.replaceAll("Ã’", "O")
														.replaceAll("Ã²", "O")
														.replaceAll("Ã™", "U")
														.replaceAll("Ã¹", "U").toUpperCase();
				
				String nmMae = rsm.getString("MAE").replaceAll("Ã�", "A")
														.replaceAll("Ã¡", "A")
														.replaceAll("Ã‰", "E")
														.replaceAll("Ã©", "E")
														.replaceAll("Ã�", "I")
														.replaceAll("Ã­", "I")
														.replaceAll("Ã“", "O")
														.replaceAll("Ã³", "O")
														.replaceAll("Ãš", "U")
														.replaceAll("Ãº", "u")
														.replaceAll("Ãƒ", "A")
														.replaceAll("Ã£", "A")
														.replaceAll("Ã•", "O")
														.replaceAll("Ãµ", "Ã•")
														.replaceAll("Ã‚", "A")
														.replaceAll("Ã¢", "A")
														.replaceAll("ÃŠ", "E")
														.replaceAll("Ãª", "E")
														.replaceAll("ÃŽ", "I")
														.replaceAll("Ã®", "I")
														.replaceAll("Ã”", "O")
														.replaceAll("Ã´", "O")
														.replaceAll("Ã›", "U")
														.replaceAll("Ã»", "U")
														.replaceAll("Ã€", "A")
														.replaceAll("Ã ", "A")
														.replaceAll("Ãˆ", "E")
														.replaceAll("Ã¨", "E")
														.replaceAll("ÃŒ", "I")
														.replaceAll("Ã¬", "I")
														.replaceAll("Ã’", "O")
														.replaceAll("Ã²", "O")
														.replaceAll("Ã™", "U")
														.replaceAll("Ã¹", "U").toUpperCase();
				
				
				String nmDataNascimento = rsm.getString("NASCIMENTO").split(" ")[0].trim();
				
				String nrNis = rsm.getString("NIS").replaceAll("\\.", "")
													.replaceAll("-", "");
				
				ResultSetMap rsmAluno = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_pessoa A, grl_pessoa_fisica B WHERE A.cd_pessoa = B.cd_pessoa AND nm_pessoa like '%"+nmAluno+"%' AND nm_mae like '%"+nmMae+"%' AND dt_nascimento = '"+nmDataNascimento+"'").executeQuery());
				if(rsmAluno.next()){
					qtAlunosEncontrados++;
					
					if(nrNis.trim().equals("")){
						qtAlunosSemNis++;
					}
					else{
						qtAlunosComNis++;
						ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_pessoa", rsmAluno.getString("cd_pessoa"), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_tipo_documentacao", "" + PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_NIS, ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmPessoaDocumentacao = PessoaTipoDocumentacaoDAO.find(criterios, connect);
						if(rsmPessoaDocumentacao.next()){
							PessoaTipoDocumentacao pessoaTipoDocumentacao = PessoaTipoDocumentacaoDAO.get(rsmPessoaDocumentacao.getInt("cd_pessoa"), rsmPessoaDocumentacao.getInt("cd_tipo_documentacao"), connect);
							pessoaTipoDocumentacao.setNrDocumento(nrNis);
							if(PessoaTipoDocumentacaoDAO.update(pessoaTipoDocumentacao, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao atualizar NIS");
							}
						}
						else{
							PessoaTipoDocumentacao pessoaTipoDocumentacao = new PessoaTipoDocumentacao();
							pessoaTipoDocumentacao.setCdPessoa(rsmAluno.getInt("cd_pessoa"));
							pessoaTipoDocumentacao.setCdTipoDocumentacao(PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_NIS);
							pessoaTipoDocumentacao.setNrDocumento(nrNis);
							if(PessoaTipoDocumentacaoDAO.insert(pessoaTipoDocumentacao, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao inserir NIS");
							}
								
						}
					}
					
				}
				else{
					
					nmAluno = rsm.getString("ALUNO").toUpperCase();
					nmMae = rsm.getString("MAE").toUpperCase();
					
					rsmAluno = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_pessoa A, grl_pessoa_fisica B WHERE A.cd_pessoa = B.cd_pessoa AND nm_pessoa like '%"+nmAluno+"%' AND nm_mae like '%"+nmMae+"%' AND dt_nascimento = '"+nmDataNascimento+"'").executeQuery());
					if(rsmAluno.next()){
						qtAlunosEncontrados++;
						
						if(nrNis.trim().equals("")){
							qtAlunosSemNis++;
						}
						else{
							System.out.println("Encontrado (NIS: "+nrNis+"): " + nmAluno + " - " + nmMae + " - " + nmDataNascimento);
							qtAlunosComNis++;
							ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cd_pessoa", rsmAluno.getString("cd_pessoa"), ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("cd_tipo_documentacao", "" + PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_NIS, ItemComparator.EQUAL, Types.INTEGER));
							ResultSetMap rsmPessoaDocumentacao = PessoaTipoDocumentacaoDAO.find(criterios, connect);
							if(rsmPessoaDocumentacao.next()){
								PessoaTipoDocumentacao pessoaTipoDocumentacao = PessoaTipoDocumentacaoDAO.get(rsmPessoaDocumentacao.getInt("cd_pessoa"), rsmPessoaDocumentacao.getInt("cd_tipo_documentacao"), connect);
								pessoaTipoDocumentacao.setNrDocumento(nrNis);
								if(PessoaTipoDocumentacaoDAO.update(pessoaTipoDocumentacao, connect) < 0){
									if(isConnectionNull)
										Conexao.rollback(connect);
									return new Result(-1, "Erro ao atualizar NIS");
								}
							}
							else{
								PessoaTipoDocumentacao pessoaTipoDocumentacao = new PessoaTipoDocumentacao();
								pessoaTipoDocumentacao.setCdPessoa(rsmAluno.getInt("cd_pessoa"));
								pessoaTipoDocumentacao.setCdTipoDocumentacao(PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_NIS);
								pessoaTipoDocumentacao.setNrDocumento(nrNis);
								if(PessoaTipoDocumentacaoDAO.insert(pessoaTipoDocumentacao, connect) < 0){
									if(isConnectionNull)
										Conexao.rollback(connect);
									return new Result(-1, "Erro ao inserir NIS");
								}
									
							}
						}
						
					}
					else{
						System.out.println("NÃ£o encontrado: " + nmAluno + " - " + nmMae + " - " + nmDataNascimento);
						qtAlunosNaoEncontrados++;
					}	
				}
			}
			
			if(isConnectionNull)
				connect.commit();
		
			System.out.println("ImportaÃ§Ã£o de NIS concluÃ­da\n"+qtAlunosNaoEncontrados+" alunos nÃ£o encontrados\n"+qtAlunosEncontrados+" alunos encontrados\n"+qtAlunosSemNis+" alunos sem NIS\n"+qtAlunosComNis+" alunos com NIS!");
			
			return new Result(1);
		
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar importar paises!", e);
		}
		finally{
			if(raf!=null)
		  		try {raf.close(); } catch(Exception e){};
	  		if(isConnectionNull)
	  			Conexao.desconectar(connect);
		}
		
    }
	
	
	public static Result findFormularioComdica() {
		return findFormularioComdica(null);
	}
	
	public static Result findFormularioComdica(Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				
				String nmUrlDatabase = "jdbc:postgresql://127.0.0.1/edf_centaurus";
				String nmLoginDataBase = "postgres";
				String nmSenhaDatBase = "CDF3DP!";
				
				connect = Conexao.conectar(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
				connect.setAutoCommit(false);
			}
			
			int anoReferencia = 2015;
			
			ResultSetMap rsmAlunos = new ResultSetMap(connect.prepareStatement("SELECT * FROM aluno P, matricula A, escola_modalidade_smed B, modalidade_smed C " + 
																				" WHERE P.id_aluno = A.id_aluno AND A.id_escola_modalidade_smed = B.id_escola_modalidade_smed AND B.id_modalidade_smed = C.id_modalidade_smed " +
																				
																				"   AND ano_matricula = " + anoReferencia).executeQuery());
			
			int qtTotal = 0;
			int qtComDeficiencia = 0;
			
			int qtCriancas     = 0;
			int qtAdolescentes = 0;
			int qtAdultos      = 0;
			int qtIdosos       = 0;
			
			int qtCriancasHomens     = 0;
			int qtAdolescentesHomens = 0;
			int qtAdultosHomens      = 0;
			int qtIdososHomens       = 0;
			
			int qtCriancasMulheres     = 0;
			int qtAdolescentesMulheres = 0;
			int qtAdultosMulheres      = 0;
			int qtIdososMulheres       = 0;
			
			int qtHomens = 0;
			int qtMulheres = 0;
			
			ArrayList<Integer> codigoAlunos = new ArrayList<Integer>();
			
			while(rsmAlunos.next()){
				
				if(!codigoAlunos.contains(rsmAlunos.getInt("id_aluno")))
					codigoAlunos.add(rsmAlunos.getInt("id_aluno"));
				else
					continue;
					
				qtTotal++;
				
				ResultSetMap rsmDeficiencia = new ResultSetMap(connect.prepareStatement("SELECT * FROM aluno_necessidade_especial WHERE id_aluno = " + rsmAlunos.getInt("id_aluno")).executeQuery());
				if(rsmDeficiencia.next())
					qtComDeficiencia++;
				
				int qtIdade = Util.getIdade(rsmAlunos.getGregorianCalendar("dt_nascimento"), 31, 2, anoReferencia);
				
				if(qtIdade < 12)
					qtCriancas++;
				else if(qtIdade < 18)
					qtAdolescentes++;
				else if(qtIdade < 65)
					qtAdultos++;
				else
					qtIdosos++;
				
				if(rsmAlunos.getInt("tp_sexo") == 1){
					qtHomens++;
					if(qtIdade < 12)
						qtCriancasHomens++;
					else if(qtIdade < 18)
						qtAdolescentesHomens++;
					else if(qtIdade < 65)
						qtAdultosHomens++;
					else
						qtIdososHomens++;
				}
				else{
					qtMulheres++;
					if(qtIdade < 12)
						qtCriancasMulheres++;
					else if(qtIdade < 18)
						qtAdolescentesMulheres++;
					else if(qtIdade < 65)
						qtAdultosMulheres++;
					else
						qtIdososMulheres++;
				}
			}
			
			System.out.println("Total de Alunos de "+anoReferencia+": " + qtTotal);
			System.out.println("Total de Alunos com deficiÃªncia de "+anoReferencia+": " + qtComDeficiencia);
			System.out.println("Total de CrianÃ§as (0 - 12 Incompletos) de "+anoReferencia+": " + qtCriancas);
			System.out.println("Total de CrianÃ§as homens (0 - 12 Incompletos) de "+anoReferencia+": " + qtCriancasHomens);
			System.out.println("Total de CrianÃ§as mulheres (0 - 12 Incompletos) de "+anoReferencia+": " + qtCriancasMulheres);
			System.out.println("Total de Adolescentes (12 - 18 Incompletos) de "+anoReferencia+": " + qtAdolescentes);
			System.out.println("Total de Adolescentes homens (12 - 18 Incompletos) de "+anoReferencia+": " + qtAdolescentesHomens);
			System.out.println("Total de Adolescentes mulheres (12 - 18 Incompletos) de "+anoReferencia+": " + qtAdolescentesMulheres);
			System.out.println("Total de Adultos (18 - 65 Incompletos) de "+anoReferencia+": " + qtAdultos);
			System.out.println("Total de Adulto homenss (18 - 65 Incompletos) de "+anoReferencia+": " + qtAdultosHomens);
			System.out.println("Total de Adultos mulheres (18 - 65 Incompletos) de "+anoReferencia+": " + qtAdultosMulheres);
			System.out.println("Total de Idosos (mais de 65) de "+anoReferencia+": " + qtIdosos);
			System.out.println("Total de Idosos homens (mais de 65) de "+anoReferencia+": " + qtIdososHomens);
			System.out.println("Total de Idosos mulheres (mais de 65) de "+anoReferencia+": " + qtIdososMulheres);
			System.out.println("Total de Alunos homens de "+anoReferencia+": " + qtHomens);
			System.out.println("Total de Alunas mulheres de "+anoReferencia+": " + qtMulheres);
			
			
			return new Result(1);
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
	
	public static Result getAlunosComBolsaFamilia(int cdPeriodoLetivo) {
		return getAlunosComBolsaFamilia(cdPeriodoLetivo, 0, null);
	}
	
	/**
	 * Busca todos os alunos que possuem bolsa familia
	 * @param cdPeriodoLetivo
	 * @param cdInstituicao
	 * @return
	 */
	public static Result getAlunosComBolsaFamilia(int cdPeriodoLetivo, int cdInstituicao) {
		return getAlunosComBolsaFamilia(cdPeriodoLetivo, cdInstituicao, null);
	}
	
	public static Result getAlunosComBolsaFamilia(int cdPeriodoLetivo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT AP.nm_pessoa AS NM_ALUNO, APF.dt_nascimento, APF.nm_mae, APF.tp_sexo, APF.tp_raca, C.nm_pessoa AS NM_INSTITUICAO, T.cd_curso, F.nm_produto_servico AS NM_CURSO, T.nm_turma, D.tp_localizacao FROM acd_matricula M, "
					+ "																														acd_aluno A, "
					+ "																														grl_pessoa AP, "
					+ "																														grl_pessoa_fisica APF, "
					+ "																														acd_turma T, "
					+ "																														acd_instituicao B, "
					+ "																														grl_pessoa C, "
					+ "																														acd_instituicao_educacenso D, "
					+ "																														acd_instituicao_periodo E, "
					+ "																														grl_produto_servico F "
					+ "						   WHERE M.cd_turma = T.cd_turma"
					+ "							 AND M.cd_aluno = A.cd_aluno "
					+ "							 AND M.cd_aluno = AP.cd_pessoa "
					+ "							 AND M.cd_aluno = APF.cd_pessoa "
					+ "							 AND T.cd_instituicao = B.cd_instituicao "
					+ "							 AND B.cd_instituicao = C.cd_pessoa "
					+ "							 AND B.cd_instituicao = D.cd_instituicao"
					+ "							 AND T.cd_curso = F.cd_produto_servico"
					+ "							 AND D.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "							 AND M.st_matricula = " + MatriculaServices.ST_ATIVA
					+ "							 AND M.cd_periodo_letivo = E.cd_periodo_letivo"
					+ "							 AND M.cd_periodo_letivo = D.cd_periodo_letivo"
					+ "							 AND B.cd_instituicao <> " + cdSecretaria
					+ (cdInstituicao > 0 ? " AND B.cd_instituicao = " + cdInstituicao : "")
					+ "							 AND (E.cd_periodo_letivo = "+cdPeriodoLetivo+" OR E.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")"
					+ "							 AND T.nm_turma <> 'TRANSFERENCIA'"
					+ "							 AND lg_bolsa_familia = 1"					
					+ "						  ORDER BY AP.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int qtSexoMasculino = 0;
			int qtSexoFeminino = 0;
			int qtRacaNaoDeclarada = 0;
			int qtRacaBranca = 0;
			int qtRacaPreta = 0;
			int qtRacaParda = 0;
			int qtRacaAmarela = 0;
			int qtRacaIndigena = 0;
			int qtInstituicaoRural = 0;
			int qtInstituicaoUrbana = 0;
			int qtCreche = 0;
			int qtEdInfantil = 0;
			int qtFundamentalRegular = 0;
			int qtEja = 0;
			while(rsm.next()){
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
				
				switch(rsm.getInt("tp_localizacao")){
					case InstituicaoEducacensoServices.TP_LOCALIZACAO_RURAL:
						qtInstituicaoRural++;
					break;
					
					case InstituicaoEducacensoServices.TP_LOCALIZACAO_URBANA:
						qtInstituicaoUrbana++;
					break;
				}
				
				if(CursoServices.isCreche(rsm.getInt("cd_curso"), connect)){
					qtCreche++;
				}
				else if(CursoServices.isEducacaoInfantil(rsm.getInt("cd_curso"), connect)){
					qtEdInfantil++;
				}
				else if(CursoServices.isFundamentalRegular(rsm.getInt("cd_curso"), connect)){
					qtFundamentalRegular++;
				}
				else if(CursoServices.isEja(rsm.getInt("cd_curso"), connect)){
					qtEja++;
				}
			}
			rsm.beforeFirst();
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "NÃºmero de alunas: " + qtSexoFeminino + "\n"
												   + "Numero de alunos: " + qtSexoMasculino + "\n"
												   + "\nNumero de alunos(as) de RaÃ§a \"NÃ£o declarada\": " + qtRacaNaoDeclarada + "\n"
												   + "Numero de alunos(as) de RaÃ§a \"Branca\": " + qtRacaBranca+ "\n"
												   + "Numero de alunos(as) de RaÃ§a \"Preta\": " + qtRacaPreta + "\n"
												   + "Numero de alunos(as) de RaÃ§a \"Parda\": " + qtRacaParda + "\n"
												   + "Numero de alunos(as) de RaÃ§a \"Amarela\": " + qtRacaAmarela + "\n"
												   + "Numero de alunos(as) de RaÃ§a \"Indigena\": " + qtRacaIndigena + "\n"
												   + "\nNumero de alunos(as) de Escolas de Ã¡rea Rural: " + qtInstituicaoRural + "\n"
												   + "Numero de alunos(as) de Escolas de Ã¡rea Urbana: " + qtInstituicaoUrbana + "\n"
												   + "\nNumero de alunos(as) de Creche: " + qtCreche + "\n"
												   + "Numero de alunos(as) de EducaÃ§Ã£o Infantil: " + qtEdInfantil + "\n"
												   + "Numero de alunos(as) de Fundamental Regular: " + qtFundamentalRegular + "\n"
												   + "Numero de alunos(as) de EJA: " + qtEja + "\n");
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
	
	public static Result getAlunosSemDocumentacao(int cdPeriodoLetivo) {
		return getAlunosSemDocumentacao(cdPeriodoLetivo, 0, null);
	}
	
	public static Result getAlunosSemDocumentacao(int cdPeriodoLetivo, int cdInstituicao) {
		return getAlunosSemDocumentacao(cdPeriodoLetivo, cdInstituicao, null);
	}
	
	/**
	 * Busca todos os alunos sem documentaÃ§Ã£o
	 * @param cdPeriodoLetivo
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static Result getAlunosSemDocumentacao(int cdPeriodoLetivo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT AP.nm_pessoa AS NM_ALUNO, APF.dt_nascimento, APF.nm_mae, APF.tp_sexo, APF.tp_raca, C.nm_pessoa AS NM_INSTITUICAO, T.cd_curso, F.nm_produto_servico AS NM_CURSO, T.nm_turma, D.tp_localizacao FROM acd_matricula M, "
					+ "																														acd_aluno A, "
					+ "																														grl_pessoa AP, "
					+ "																														grl_pessoa_fisica APF, "
					+ "																														acd_turma T, "
					+ "																														acd_instituicao B, "
					+ "																														grl_pessoa C, "
					+ "																														acd_instituicao_educacenso D, "
					+ "																														acd_instituicao_periodo E, "
					+ "																														grl_produto_servico F "
					+ "						   WHERE M.cd_turma = T.cd_turma"
					+ "							 AND M.cd_aluno = A.cd_aluno "
					+ "							 AND M.cd_aluno = AP.cd_pessoa "
					+ "							 AND M.cd_aluno = APF.cd_pessoa "
					+ "							 AND T.cd_instituicao = B.cd_instituicao "
					+ "							 AND B.cd_instituicao = C.cd_pessoa "
					+ "							 AND B.cd_instituicao = D.cd_instituicao"
					+ "							 AND T.cd_curso = F.cd_produto_servico"
					+ "							 AND D.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "							 AND M.st_matricula = " + MatriculaServices.ST_ATIVA
					+ "							 AND M.cd_periodo_letivo = E.cd_periodo_letivo"
					+ "							 AND M.cd_periodo_letivo = D.cd_periodo_letivo"
					+ "							 AND B.cd_instituicao <> " + cdSecretaria
					+ (cdInstituicao > 0 ? " AND B.cd_instituicao = " + cdInstituicao : "")
					+ "							 AND (E.cd_periodo_letivo = "+cdPeriodoLetivo+" OR E.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")"
					+ "							 AND T.nm_turma <> 'TRANSFERENCIA'"
					+ "							 AND ((APF.nr_cpf IS NULL OR APF.nr_cpf = '') "
					+ "									AND (APF.nr_rg IS NULL OR APF.nr_rg = '') "
					+ "									AND NOT EXISTS "
					+ "											(SELECT * FROM grl_pessoa_tipo_documentacao PTD "
					+ "												WHERE PTD.cd_pessoa = A.cd_aluno "
					+ "												  AND (PTD.cd_tipo_documentacao = "+ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_NASCIMENTO", 0)+" "
					+ "										  		   		OR PTD.cd_tipo_documentacao = "+ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTACAO_CASAMENTO", 0)+")"
					+ "									 		 )"
					+ "								 )"
					+ "						  ORDER BY AP.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("DT_NM_NASCIMENTO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_nascimento")));
			}
			rsm.beforeFirst();
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "");
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
	
	public static Result getAlunosDeficiencia(int cdPeriodoLetivo) {
		return getAlunosDeficiencia(cdPeriodoLetivo, 0, null);
	}
	
	public static Result getAlunosDeficiencia(int cdPeriodoLetivo, int cdInstituicao) {
		return getAlunosDeficiencia(cdPeriodoLetivo, cdInstituicao, null);
	}
	
	/**
	 * Busca todos os alunos que possuem deficiencia cadastrada
	 * @param cdPeriodoLetivo
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static Result getAlunosDeficiencia(int cdPeriodoLetivo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT A.cd_aluno, AP.nm_pessoa AS NM_ALUNO, APF.dt_nascimento, APF.nm_mae, APF.tp_sexo, APF.tp_raca, C.nm_pessoa AS NM_INSTITUICAO, T.cd_curso, F.nm_produto_servico AS NM_CURSO, T.nm_turma, D.tp_localizacao FROM acd_matricula M, "
					+ "																														acd_aluno A, "
					+ "																														grl_pessoa AP, "
					+ "																														grl_pessoa_fisica APF, "
					+ "																														acd_turma T, "
					+ "																														acd_instituicao B, "
					+ "																														grl_pessoa C, "
					+ "																														acd_instituicao_educacenso D, "
					+ "																														acd_instituicao_periodo E, "
					+ "																														grl_produto_servico F "
					+ "						   WHERE M.cd_turma = T.cd_turma"
					+ "							 AND M.cd_aluno = A.cd_aluno "
					+ "							 AND M.cd_aluno = AP.cd_pessoa "
					+ "							 AND M.cd_aluno = APF.cd_pessoa "
					+ "							 AND T.cd_instituicao = B.cd_instituicao "
					+ "							 AND B.cd_instituicao = C.cd_pessoa "
					+ "							 AND B.cd_instituicao = D.cd_instituicao"
					+ "							 AND T.cd_curso = F.cd_produto_servico"
					+ "							 AND D.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "							 AND M.st_matricula = " + MatriculaServices.ST_ATIVA
					+ "							 AND M.cd_periodo_letivo = E.cd_periodo_letivo"
					+ "							 AND M.cd_periodo_letivo = D.cd_periodo_letivo"
					+ "							 AND B.cd_instituicao <> " + cdSecretaria
					+ (cdInstituicao > 0 ? " AND B.cd_instituicao = " + cdInstituicao : "")
					+ "							 AND (E.cd_periodo_letivo = "+cdPeriodoLetivo+" OR E.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")"
					+ "							 AND T.nm_turma <> 'TRANSFERENCIA'"
					+ "							 AND EXISTS (SELECT * FROM grl_pessoa_necessidade_especial GPNE WHERE A.cd_aluno = GPNE.cd_pessoa)"
					+ "						  ORDER BY AP.nm_pessoa");
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
			
			int qtAlunosComRecursos = 0;
			int qtAlunosSemRecursos = 0;
			while(rsm.next()){
				rsm.setValueToField("DT_NM_NASCIMENTO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_nascimento")));
				
				String nmDeficiencias = "";
				ResultSetMap rsmPessoaNecessidadeEspecial = PessoaNecessidadeEspecialServices.getNecessidadeEspecialByPessoa(rsm.getInt("cd_aluno"), connect);
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
				
				ResultSetMap rsmRecursos = AlunoRecursoProvaServices.getRecursoProvaByAluno(rsm.getInt("cd_aluno"), connect);
				String nmRecursos = "";
				while(rsmRecursos.next()){
					nmRecursos += rsmRecursos.getString("nm_tipo_recurso") + ", ";
				}
				rsmRecursos.beforeFirst();
				
				if(rsmRecursos.size() > 0){
					nmRecursos = nmRecursos.substring(0, nmRecursos.length()-2);
					qtAlunosComRecursos++;
				}
				else{
					nmRecursos = "NÃ£o possui recursos";
					qtAlunosSemRecursos++;
				}
				
				rsm.setValueToField("NM_RECURSOS", nmRecursos);
					
				
			}
			rsm.beforeFirst();
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "Alunos com Cegueira: " + qtCegueira + "\n"
													+ "Alunos com Baixa visÃ£o: " + qtBaixaVisao + "\n"
													+ "Alunos com Surdez: " + qtSurdez + "\n"
													+ "Alunos com DeficiÃªncia Auditiva: " + qtDeficienciaAuditiva + "\n"
													+ "Alunos com Surdo-Cegueira: " + qtSurdoCegueira + "\n"
													+ "Alunos com DeficiÃªncia FÃ­sica: " + qtDeficienciaFisica + "\n"
													+ "Alunos com DeficiÃªncia Intelectual: " + qtDeficienciaIntelectual + "\n"
													+ "Alunos com DeficiÃªncia Multipla: " + qtDeficienciaMultipla + "\n"
													+ "Alunos com Autismo Infantil: " + qtAutismoInfantil + "\n"
													+ "Alunos com Sindrome de Asperger: " + qtSindromeAsperger + "\n"
													+ "Alunos com Sindrome de Rett: " + qtSindromeRett + "\n"
													+ "Alunos com Transtorno Desintegrativo: " + qtTranstornoDesintegrativo + "\n"
													+ "Alunos com SuperdotaÃ§Ã£o: " + qtSuperdotacao + "\n\n"
													+ "Alunos com Recursos para a realizaÃ§Ã£o das provas federais: " + qtAlunosComRecursos + "\n"
													+ "Alunos sem Recursos para a realizaÃ§Ã£o das provas federais: " + qtAlunosSemRecursos + "\n");
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
	
	public static Result getAlunosInep(int cdPeriodoLetivo) {
		return getAlunosInep(cdPeriodoLetivo, 0, null);
	}
	
	public static Result getAlunosInep(int cdPeriodoLetivo, int cdInstituicao) {
		return getAlunosInep(cdPeriodoLetivo, cdInstituicao, null);
	}
	
	/**
	 * Busca todos os alunos com a informaÃ§Ã£o de seu nÃºmero de INEP
	 * @param cdPeriodoLetivo
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static Result getAlunosInep(int cdPeriodoLetivo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT A.cd_aluno, A.nm_aluno_censo, AP.nm_pessoa AS NM_ALUNO, APF.dt_nascimento, APF.nm_mae, A.nr_inep, C.nm_pessoa AS NM_INSTITUICAO, T.cd_curso, F.nm_produto_servico AS NM_CURSO, T.nm_turma FROM acd_matricula M, "
					+ "																														acd_aluno A, "
					+ "																														grl_pessoa AP, "
					+ "																														grl_pessoa_fisica APF, "
					+ "																														acd_turma T, "
					+ "																														acd_instituicao B, "
					+ "																														grl_pessoa C, "
					+ "																														acd_instituicao_educacenso D, "
					+ "																														acd_instituicao_periodo E, "
					+ "																														grl_produto_servico F "
					+ "						   WHERE M.cd_turma = T.cd_turma"
					+ "							 AND M.cd_aluno = A.cd_aluno "
					+ "							 AND M.cd_aluno = AP.cd_pessoa "
					+ "							 AND M.cd_aluno = APF.cd_pessoa "
					+ "							 AND T.cd_instituicao = B.cd_instituicao "
					+ "							 AND B.cd_instituicao = C.cd_pessoa "
					+ "							 AND B.cd_instituicao = D.cd_instituicao"
					+ "							 AND T.cd_curso = F.cd_produto_servico"
					+ "							 AND D.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "							 AND M.st_matricula = " + MatriculaServices.ST_ATIVA
					+ "							 AND M.cd_periodo_letivo = E.cd_periodo_letivo"
					+ "							 AND M.cd_periodo_letivo = D.cd_periodo_letivo"
					+ "							 AND B.cd_instituicao <> " + cdSecretaria
					+ (cdInstituicao > 0 ? " AND B.cd_instituicao = " + cdInstituicao : "")
					+ "							 AND (E.cd_periodo_letivo = "+cdPeriodoLetivo+" OR E.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")"
					+ "							 AND T.nm_turma <> 'TRANSFERENCIA'"
					+ "						  ORDER BY AP.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int qtComInep = 0;
			int qtSemInep = 0;
			int qtComDivergenciaNome = 0;
			int qtSemDivergenciaNome = 0;
			while(rsm.next()){
				rsm.setValueToField("DT_NM_NASCIMENTO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_nascimento")));
				if(rsm.getString("nr_inep") == null || rsm.getString("nr_inep").equals("")){
					qtSemInep++;
				}
				else{
					qtComInep++;
				}
				
				if(Util.limparAcentos(rsm.getString("nm_aluno")).equals(Util.limparAcentos(rsm.getString("nm_aluno_censo")))){
					rsm.setValueToField("LG_NOME_DIVERGENTE", "NÃ£o");
					qtSemDivergenciaNome++;
				}
				else{
					rsm.setValueToField("LG_NOME_DIVERGENTE", "Sim");
					qtComDivergenciaNome++;
				}
				
			}
			rsm.beforeFirst();
			
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "Alunos com INEP: " + qtComInep + "\n"
													+"Alunos sem INEP: " + qtSemInep + "\n\n"
													+"Alunos com divergÃªncia no nome: " + qtComDivergenciaNome + "\n"
													+"Alunos sem divergÃªncia no nome: " + qtSemDivergenciaNome + "\n");
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
	
	public static Result getAlunosDivergenciaIdade(int cdPeriodoLetivo) {
		return getAlunosDivergenciaIdade(cdPeriodoLetivo, 0, null);
	}
	
	public static Result getAlunosDivergenciaIdade(int cdPeriodoLetivo, int cdInstituicao) {
		return getAlunosDivergenciaIdade(cdPeriodoLetivo, cdInstituicao, null);
	}
	
	/**
	 * Busca todos os alunos que possuem divergencia de idade com seu curso matriculado no periodo letivo informado
	 * @param cdPeriodoLetivo
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static Result getAlunosDivergenciaIdade(int cdPeriodoLetivo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT A.cd_aluno, AP.nm_pessoa AS NM_ALUNO, APF.dt_nascimento, APF.nm_mae, C.nm_pessoa AS NM_INSTITUICAO, M.cd_curso, H.nm_produto_servico AS NM_CURSO, (T.nm_turma || ' (' || F.nm_produto_servico || ')') AS NM_TURMA, I.nr_idade AS nr_idade_ideal, E.nm_periodo_letivo FROM acd_matricula M, "
					+ "																														acd_aluno A, "
					+ "																														grl_pessoa AP, "
					+ "																														grl_pessoa_fisica APF, "
					+ "																														acd_turma T, "
					+ "																														acd_instituicao B, "
					+ "																														grl_pessoa C, "
					+ "																														acd_instituicao_educacenso D, "
					+ "																														acd_instituicao_periodo E, "
					+ "																														grl_produto_servico F, "
					+ "																														acd_curso G, "
					+ "																														grl_produto_servico H, "
					+ "																														acd_curso I "
					+ "						   WHERE M.cd_turma = T.cd_turma"
					+ "							 AND M.cd_aluno = A.cd_aluno "
					+ "							 AND M.cd_aluno = AP.cd_pessoa "
					+ "							 AND M.cd_aluno = APF.cd_pessoa "
					+ "							 AND T.cd_instituicao = B.cd_instituicao "
					+ "							 AND B.cd_instituicao = C.cd_pessoa "
					+ "							 AND B.cd_instituicao = D.cd_instituicao"
					+ "							 AND T.cd_curso = F.cd_produto_servico"
					+ "							 AND T.cd_curso = G.cd_curso"
					+ "							 AND M.cd_curso = H.cd_produto_servico"
					+ "							 AND M.cd_curso = I.cd_curso"
					+ "							 AND D.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "							 AND M.st_matricula = " + MatriculaServices.ST_ATIVA
					+ "							 AND M.cd_periodo_letivo = E.cd_periodo_letivo"
					+ "							 AND M.cd_periodo_letivo = D.cd_periodo_letivo"
					+ "							 AND B.cd_instituicao <> " + cdSecretaria
					+ (cdInstituicao > 0 ? " AND B.cd_instituicao = " + cdInstituicao : "")
					+ "							 AND (E.cd_periodo_letivo = "+cdPeriodoLetivo+" OR E.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")"
					+ "							 AND T.nm_turma <> 'TRANSFERENCIA'"
					+ "						  ORDER BY AP.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int qtAlunoIdadeDivergente = 0;
			int qtAlunoIdadeNaoDivergente = 0;
			
			int qtAlunoIdadeDivergenteMaior = 0;
			int qtAlunoIdadeDivergenteMenor = 0;
			while(rsm.next()){
				rsm.setValueToField("DT_NM_NASCIMENTO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_nascimento")));
				
				rsm.setValueToField("NR_IDADE", Util.getIdade(rsm.getGregorianCalendar("dt_nascimento"), 31, 2, Integer.parseInt(rsm.getString("nm_periodo_letivo"))));
				
				if(rsm.getInt("NR_IDADE") == rsm.getInt("NR_IDADE_IDEAL") || rsm.getInt("NR_IDADE_IDEAL") == 0){
					qtAlunoIdadeNaoDivergente++;
					if(rsm.getInt("NR_IDADE_IDEAL") == 0){
						rsm.setValueToField("NR_IDADE_IDEAL", "NÃ£o possui");
					}
				}
				else{
					qtAlunoIdadeDivergente++;
					if(rsm.getInt("NR_IDADE") > rsm.getInt("NR_IDADE_IDEAL")){
						qtAlunoIdadeDivergenteMaior++;
					}
					else{
						qtAlunoIdadeDivergenteMenor++;
					}
				}
				
				
			}
			rsm.beforeFirst();
			
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "Alunos com idade divergente: " + qtAlunoIdadeDivergente + "\n"
					                                +"Alunos na idade ideal: " + qtAlunoIdadeNaoDivergente + "\n\n"
					                                +"Alunos com idade divergente (maior do que a ideal): " + qtAlunoIdadeDivergenteMaior + "\n"
					                                +"Alunos com idade divergente (menor do que a ideal): " + qtAlunoIdadeDivergenteMenor + "\n");
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
	
	
	public static Result getAlunosTransporte(int cdPeriodoLetivo) {
		return getAlunosTransporte(cdPeriodoLetivo, 0, null);
	}
	
	public static Result getAlunosTransporte(int cdPeriodoLetivo, int cdInstituicao) {
		return getAlunosTransporte(cdPeriodoLetivo, cdInstituicao, null);
	}
	
	/**
	 * Busca todos os alunos, indicando sua informaÃ§Ã£o de transporte
	 * @param cdPeriodoLetivo
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static Result getAlunosTransporte(int cdPeriodoLetivo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT M.cd_matricula, A.cd_aluno, AP.nm_pessoa AS NM_ALUNO, APF.dt_nascimento, APF.nm_mae, APF.tp_sexo, APF.tp_raca, C.nm_pessoa AS NM_INSTITUICAO, T.cd_curso, F.nm_produto_servico AS NM_CURSO, T.nm_turma, D.tp_localizacao, M.lg_transporte_publico FROM acd_matricula M, "
					+ "																														acd_aluno A, "
					+ "																														grl_pessoa AP, "
					+ "																														grl_pessoa_fisica APF, "
					+ "																														acd_turma T, "
					+ "																														acd_instituicao B, "
					+ "																														grl_pessoa C, "
					+ "																														acd_instituicao_educacenso D, "
					+ "																														acd_instituicao_periodo E, "
					+ "																														grl_produto_servico F "
					+ "						   WHERE M.cd_turma = T.cd_turma"
					+ "							 AND M.cd_aluno = A.cd_aluno "
					+ "							 AND M.cd_aluno = AP.cd_pessoa "
					+ "							 AND M.cd_aluno = APF.cd_pessoa "
					+ "							 AND T.cd_instituicao = B.cd_instituicao "
					+ "							 AND B.cd_instituicao = C.cd_pessoa "
					+ "							 AND B.cd_instituicao = D.cd_instituicao"
					+ "							 AND T.cd_curso = F.cd_produto_servico"
					+ "							 AND D.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "							 AND M.st_matricula = " + MatriculaServices.ST_ATIVA
					+ "							 AND M.cd_periodo_letivo = E.cd_periodo_letivo"
					+ "							 AND M.cd_periodo_letivo = D.cd_periodo_letivo"
					+ "							 AND B.cd_instituicao <> " + cdSecretaria
					+ (cdInstituicao > 0 ? " AND B.cd_instituicao = " + cdInstituicao : "")
					+ "							 AND (E.cd_periodo_letivo = "+cdPeriodoLetivo+" OR E.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")"
					+ "							 AND T.nm_turma <> 'TRANSFERENCIA'"
					+ "						  ORDER BY AP.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int qtVan = 0;
			int qtMicro = 0;
			int qtOnibus = 0;
			int qtBicicleta = 0;
			int qtTracaoAnimal = 0;
			int qtOutro = 0;
			int qtEmbarcacao5 = 0;
			int qtEmbarcacao515 = 0;
			int qtEmbarcacao1535 = 0;
			int qtEmbarcacao35 = 0;
			int qtMetro = 0;
			
			while(rsm.next()){
				rsm.setValueToField("DT_NM_NASCIMENTO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_nascimento")));
				
				String nmTransportes = "";
				ResultSetMap rsmMatriculaTipoTransporte = MatriculaTipoTransporteServices.getTipoTransporteByMatricula(rsm.getInt("cd_matricula"), connect);
				while(rsmMatriculaTipoTransporte.next()){
					nmTransportes += rsmMatriculaTipoTransporte.getString("nm_tipo_transporte") + ", ";
					
					switch(rsmMatriculaTipoTransporte.getString("id_tipo_transporte")){
						case InstituicaoEducacensoServices.TP_TRANSPORTE_VAN:
							qtVan++;
						break;
						
						case InstituicaoEducacensoServices.TP_TRANSPORTE_MICRO:
							qtMicro++;
						break;
						
						case InstituicaoEducacensoServices.TP_TRANSPORTE_ONIBUS:
							qtOnibus++;
						break;
						
						case InstituicaoEducacensoServices.TP_TRANSPORTE_BICICLETA:
							qtBicicleta++;
						break;
						
						case InstituicaoEducacensoServices.TP_TRANSPORTE_TRACAO_ANIMAL:
							qtTracaoAnimal++;
						break;
						
						case InstituicaoEducacensoServices.TP_TRANSPORTE_OUTRO:
							qtOutro++;
						break;
						
						case InstituicaoEducacensoServices.TP_TRANSPORTE_EMBARCACAO_5:
							qtEmbarcacao5++;
						break;
						
						case InstituicaoEducacensoServices.TP_TRANSPORTE_EMBARCACAO_5_15:
							qtEmbarcacao515++;
						break;
						
						case InstituicaoEducacensoServices.TP_TRANSPORTE_EMBARCACAO_15_35:
							qtEmbarcacao1535++;
						break;
						
						case InstituicaoEducacensoServices.TP_TRANSPORTE_EMBARCACAO_35:
							qtEmbarcacao35++;
						break;
						
						case InstituicaoEducacensoServices.TP_TRANSPORTE_METRO:
							qtMetro++;
						break;
					}
				}
				if(nmTransportes.length() > 0)
					nmTransportes = nmTransportes.substring(0, nmTransportes.length()-2);
				rsm.setValueToField("NM_TRANSPORTES", (nmTransportes.length() > 0 ? nmTransportes : "NÃ£o recebe"));
				
				rsm.setValueToField("CL_LG_TRANSPORTE_PUBLICO", (rsm.getInt("lg_transporte_publico") == 0 ? "NÃ£o" : "Sim"));
					
				
			}
			rsm.beforeFirst();
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "Alunos que recebem Van: " + qtVan + "\n"
													+ "Alunos que recebem MicroÃ´nibus: " + qtMicro + "\n"
													+ "Alunos que recebem Ã”nibus: " + qtOnibus + "\n"
													+ "Alunos que recebem Bicicleta: " + qtBicicleta + "\n"
													+ "Alunos que recebem Transporte de traÃ§Ã£o animal: " + qtTracaoAnimal + "\n"
													+ "Alunos que recebem Outro tipo de transporte: " + qtOutro + "\n"
													+ "Alunos que recebem EmbarcaÃ§Ã£o de atÃ© 5 pessoas: " + qtEmbarcacao5 + "\n"
													+ "Alunos que recebem EmbarcaÃ§Ã£o de 5 pessoas atÃ© 15 pessoas: " + qtEmbarcacao515 + "\n"
													+ "Alunos que recebem EmbarcaÃ§Ã£o de 15 pessoas atÃ© 35 pessoas: " + qtEmbarcacao1535 + "\n"
													+ "Alunos que recebem EmbarcaÃ§Ã£o de mais de 35 pessoas: " + qtEmbarcacao35 + "\n"
													+ "Alunos que recebem MetrÃ´: " + qtMetro + "\n");
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
	
	public static Result getInstituicaoByAluno(int cdAluno) {
		return getInstituicaoByAluno(cdAluno, null);
	}
	
	public static Result getInstituicaoByAluno(int cdAluno, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.cd_instituicao FROM acd_instituicao A, acd_turma B, acd_matricula C, acd_instituicao_periodo D "
					+ "							WHERE A.cd_instituicao = B.cd_instituicao "
					+ "							  AND B.cd_turma = C.cd_turma "
					+ "							  AND A.cd_instituicao = D.cd_instituicao AND C.cd_periodo_letivo = D.cd_periodo_letivo "
					+ "							  AND C.st_matricula = "+MatriculaServices.ST_ATIVA
					+ " 						  AND D.st_periodo_letivo = " + InstituicaoPeriodoServices.ST_ATUAL
					+ "							  AND C.cd_aluno = " + cdAluno);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			Instituicao instituicao = null;
			while(rsm.next()){
				instituicao = InstituicaoDAO.get(rsm.getInt("cd_instituicao"), connect);
			}
			
			return new Result(1, "Busca realizada com sucesso", "INSTITUICAO", instituicao);
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAllByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAllByOferta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ValidatorResult validate(Aluno aluno, int idGrupo){
		return validate(aluno, null, 0, idGrupo, null);
	}
	
	public static ValidatorResult validate(Aluno aluno, int idGrupo, Connection connect){
		return validate(aluno, null, 0, idGrupo, connect);
	}
	
	public static ValidatorResult validate(Aluno aluno, PessoaEndereco endereco, int cdUsuario, int idGrupo){
		return validate(aluno, endereco, cdUsuario, idGrupo, null);
	}
	
	public static ValidatorResult validate(Aluno aluno, PessoaEndereco endereco, int cdUsuario, int idGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(aluno == null){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new ValidatorResult(ValidatorResult.ERROR, "Aluno nÃ£o encontrado");
			}
			
			ValidatorResult result = new ValidatorResult(ValidatorResult.VALID, "Aluno passou pela validaÃ§Ã£o");
			HashMap<Integer, Validator> listValidator = getListValidation();
			
			//ENDERECO - ValidaÃ§Ãµes de EndereÃ§o
			if(endereco != null)
				validateEndereco(listValidator, endereco, connect);
			
			//NOME - Verificar se o aluno possui nome
			if(aluno.getNmPessoa() == null || aluno.getNmPessoa().trim().equals("")){
				listValidator.get(VALIDATE_NOME).add(ValidatorResult.ERROR, "Faltando nome");
			}
			
			//NOME - Verificar se o aluno possui nome (Educacenso)
			if(aluno.getNmAlunoCenso() == null || aluno.getNmAlunoCenso().trim().equals("") || aluno.getNmAlunoCenso().length() > 100){
				listValidator.get(VALIDATE_NOME_CENSO_INVALIDO).add(ValidatorResult.ERROR, "Faltando nome do censo, ou o mesmo Ã© invÃ¡lido");
			}
			
			//SEXO - Verificar se o valor do sexo do aluno Ã© vÃ¡lido
			if(aluno.getTpSexo() < PessoaFisicaServices.TP_SEXO_MASCULINO || aluno.getTpSexo() > PessoaFisicaServices.TP_SEXO_FEMININO){
				listValidator.get(VALIDATE_SEXO).add(ValidatorResult.ERROR, "Faltando sexo do aluno");
			}
			
			//DATA DE NASCIMENTO - Verifica se o aluno possui data de nascimento
			if(aluno.getDtNascimento() == null){
				listValidator.get(VALIDATE_DATA_NASCIMENTO).add(ValidatorResult.ERROR, "Faltando data de nascimento ou a mesma Ã© invÃ¡lida");
			}
			
			//DATA DE NASCIMENTO - Verifica se a data de nascimento Ã© muito antiga (pessoa com mais de 120 anos)
			GregorianCalendar dtMuitoAntiga = Util.getDataAtual();
			dtMuitoAntiga.add(Calendar.YEAR, -120);
			
			if(aluno.getDtNascimento() != null && aluno.getDtNascimento().before(dtMuitoAntiga)){
				listValidator.get(VALIDATE_DATA_NASCIMENTO_MUITO_ANTIGA).add(ValidatorResult.ERROR, "Data de nascimento muito antiga");
			}
			
			//NACIONALIDADE - Verifica se o valor de nacionalidade Ã© vÃ¡lido
			if(aluno.getTpNacionalidade() < PessoaFisicaServices.TP_NACIONALIDADE_BRASILEIRA || aluno.getTpNacionalidade() > PessoaFisicaServices.TP_NACIONALIDADE_ESTRANGEIRA){
				listValidator.get(VALIDATE_NACIONALIDADE).add(ValidatorResult.ERROR, "O valor de nacionalidade estÃ¡ invÃ¡lido");
			}
			
			//NATURALIDADE - Verifica se o aluno possui naturalidade cadastrada
			if(aluno.getCdNaturalidade() == 0 && aluno.getTpNacionalidade() == PessoaFisicaServices.TP_NACIONALIDADE_BRASILEIRA){
				listValidator.get(VALIDATE_NATURALIDADE).add(ValidatorResult.ERROR, "Faltando naturalidade");
			}
			
			//RACA - Verifica se o valor de raÃ§a Ã© vÃ¡lido
			if(aluno.getTpRaca() < PessoaFisicaServices.TP_RACA_NAO_DECLARADA || aluno.getTpRaca() > PessoaFisicaServices.TP_RACA_INDIGENA){
				listValidator.get(VALIDATE_RACA).add(ValidatorResult.ERROR, "Faltando raÃ§a do aluno");
			}
			
			//NOME DA MAE - Verifica se existe o nome da mae
			if(aluno.getNmMae() == null || aluno.getNmMae().trim().equals("")){
				listValidator.get(VALIDATE_NOME_MAE).add(ValidatorResult.ERROR, "Faltando nome da mÃ£e");
			}
			
			if(aluno.getNmPai() != null){
				//NOME PAI - Verificar se o nome do pai contem sequencias invÃ¡lidas
				if(aluno.getNmPai().contains("XX")){
					listValidator.get(VALIDATE_NOME_PAI_INVALIDO).add(ValidatorResult.ERROR, "Nome do pai estÃ¡ invÃ¡lido");
				}
				
				
				//NOME PAI - Verifica se a caixa de seleÃ§Ã£o estÃ¡ ativada mesmo havndo registro da informaÃ§Ã£o
				if(!aluno.getNmPai().trim().equals("") && aluno.getLgPaiNaoDeclarado() == 1){
					listValidator.get(VALIDATE_NOME_PAI_CAIXA_VALIDACAO).add(ValidatorResult.ERROR, "Caixa de seleÃ§Ã£o \"NÃ£o declarado/informado\" do nome do pai selecionada, mesmo havendo registro da informaÃ§Ã£o");
				}
				
				
				//NOME PAI - Verifica se nÃ£o foi informada palavras que indicariam que o pai Ã© nÃ£o declarado
				if(aluno.getNmPai().contains("DECLARADO") || aluno.getNmPai().contains("INFORMADO")){
					listValidator.get(VALIDATE_NOME_PAI_NAO_DECLARADO).add(ValidatorResult.ERROR, "Para registrar que o pai do aluno Ã© NÃ£o declarado ou NÃ£o informado, utilize a seleÃ§Ã£o \"NÃ£o declarado/informado\" que estÃ¡ ao lado do nome do pai");
				}
				
			}
			
			//NOME PAI - Verifica se o nome do pai nÃ£o Ã© igual ao da mÃ£e
			if(aluno.getNmPai() != null && aluno.getNmMae() != null){
				if(aluno.getNmPai().trim().equals(aluno.getNmMae().trim())){
					listValidator.get(VALIDATE_NOME_PAI_IGUAL_MAE).add(ValidatorResult.ERROR, "O nome do pai nÃ£o pode ser igual ao nome da mÃ£e");
				}
				
			}
			
			//NOME PAI - Verifica se a caixa de seleÃ§Ã£o foi marcada caso o nome do pai nÃ£o seja conhecido
			if((aluno.getNmPai() == null || aluno.getNmPai().trim().equals("")) && aluno.getLgPaiNaoDeclarado() == 0){
				listValidator.get(VALIDATE_CAIXA_DE_SELECAO_NOME_PAI).add(ValidatorResult.ERROR, "Caso o nome do pai do(a) aluno(a) nÃ£o seja conhecido, marque a seleÃ§Ã£o \"NÃ£o declarado/informado\"");
			}
			
			
			if(aluno.getNrCpf() != null && !aluno.getNrCpf().trim().equals("") && aluno.getLgFaltaDocumento() == 0){
				//CPF - Verifica se o CPF do aluno Ã© vÃ¡lido
				if(!Util.isCpfValido(aluno.getNrCpf())){
					listValidator.get(VALIDATE_CPF_VALIDO).add(ValidatorResult.ERROR, "CPF nÃ£o Ã© vÃ¡lido");
				}
				
				
				//CPF - Verifica se jÃ¡ existe alguÃ©m com esse CPF cadastrado no Banco
				if(Util.isCpfValido(aluno.getNrCpf())){
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>(); 
					criterios.add(new ItemComparator("cd_pessoa", "" + aluno.getCdPessoa(), ItemComparator.DIFFERENT, Types.INTEGER));
					criterios.add(new ItemComparator("nr_cpf", "" + aluno.getNrCpf(), ItemComparator.EQUAL, Types.VARCHAR));
					ResultSetMap rsmPessoaCpf = PessoaFisicaDAO.find(criterios, connect);
					if(rsmPessoaCpf.next()){
						listValidator.get(VALIDATE_CPF_JA_CADASTRADO).add(ValidatorResult.ERROR, "JÃ¡ existe alguÃ©m com esse CPF cadastrado");
					}
					
				}
			}
			
			//RG - Verifica a quantidade mÃ­nima de caracteres de um RG (cinco)
			if(aluno.getNrRg() != null && !aluno.getNrRg().equals("") && aluno.getNrRg().trim().length() < 5 && aluno.getLgFaltaDocumento() == 0){
				listValidator.get(VALIDATE_RG_QUANT_MINIMA).add(ValidatorResult.ERROR, "NÃºmero do RG nÃ£o pode conter menos de 5 dÃ­gitos");
			}
			
			
			//RG - Verifica se jÃ¡ existe alguem com mesmo nÃºmero e estado de RG cadastrado no Banco
			if(aluno.getLgFaltaDocumento() == 0){
				ResultSetMap rsmRgAluno = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_pessoa_fisica A WHERE A.cd_pessoa <> " + aluno.getCdAluno() + " AND A.nr_rg = '"+aluno.getNrRg()+"' AND A.cd_estado_rg = " + aluno.getCdEstadoRg()).executeQuery());
				if(rsmRgAluno.next()){
					listValidator.get(VALIDATE_RG_JA_CADASTRADO).add(ValidatorResult.ERROR, "JÃ¡ existe uma pessoa com o mesmo nÃºmero de RG e mesmo estado cadastrado no sistema");
				}
				
			}
			
			//NOME DO ALUNO
			String[] partesNome = aluno.getNmPessoa().split(" ");
			for(String parteNome : partesNome){
				//NOME DO ALUNO - Verifica se existe espaÃ§os dentro do nome do aluno
				if(parteNome.trim().equals("")){
					listValidator.get(VALIDATE_NOME_ALUNO_ESPACOS).add(ValidatorResult.ERROR, "HÃ¡ muitos espaÃ§os dentro do nome do aluno");
				}
				
				
				//NOME DO ALUNO - Verifica se existe abreviaÃ§Ãµes dentro do nome do aluno
				if(parteNome.length() <= 1 && !parteNome.trim().equals("E") && !parteNome.trim().equals("D")){
					listValidator.get(VALIDATE_NOME_ALUNO_ABREVIADO).add(ValidatorResult.ERROR, "Uma parte do nome do aluno estÃ¡ abreviado");
				}
				
				
				//NOME DO ALUNO - Verifica se existe caracteres especiais no nome do aluno
				if(parteNome.contains(",") || parteNome.contains(".") || parteNome.contains(";") || parteNome.contains("-") || parteNome.contains(":") || parteNome.contains("'") || parteNome.contains("*") || parteNome.contains("+") || parteNome.contains("/") || parteNome.contains("!") || parteNome.contains("@") || parteNome.contains("#") || parteNome.contains("$") || parteNome.contains("&")
				|| parteNome.contains("0") || parteNome.contains("1") || parteNome.contains("2") || parteNome.contains("3") || parteNome.contains("4") || parteNome.contains("5") || parteNome.contains("6") || parteNome.contains("7") || parteNome.contains("8") || parteNome.contains("9")){
					listValidator.get(VALIDATE_NOME_ALUNO_CARACTERES_ESPECIAIS).add(ValidatorResult.ERROR, "O nome do aluno nÃ£o pode conter carateres especiais ou nÃºmeros");
				}
				
			}
			//NOME DO ALUNO - Verifica se o nome do aluno possui sobrenome
			if(partesNome.length <= 1){
				listValidator.get(VALIDATE_NOME_ALUNO_SEM_SOBRENOME).add(ValidatorResult.ERROR, "Nome do aluno estÃ¡ sem sobrenome");
			}
			
			//NOME DA MAE
			partesNome = aluno.getNmMae().split(" ");
			for(String parteNome : partesNome){ 
				//NOME DA MAE - Verifica se existe espaÃ§os dentro do nome da mÃ£e
				if(parteNome.trim().equals("")){
					listValidator.get(VALIDATE_NOME_MAE_ESPACOS).add(ValidatorResult.ERROR, "HÃ¡ muitos espaÃ§os dentro do nome da mÃ£e");
				}
				//NOME DA MAE - Verifica se existe abreviaÃ§Ãµes dentro do nome da mÃ£e
				if(parteNome.length() <= 1 && !parteNome.trim().equals("E") && !parteNome.trim().equals("D")){
					listValidator.get(VALIDATE_NOME_MAE_ABREVIADO).add(ValidatorResult.ERROR, "Uma parte do nome da mÃ£e estÃ¡ abreviado");
				}
				//NOME DA MAE - Verifica se existe caracteres especiais no nome da mÃ£e
				if(parteNome.contains(",") || parteNome.contains(".") || parteNome.contains(";") || parteNome.contains("-") || parteNome.contains(":") || parteNome.contains("'") || parteNome.contains("*") || parteNome.contains("+") || parteNome.contains("/") || parteNome.contains("!") || parteNome.contains("@") || parteNome.contains("#") || parteNome.contains("$") || parteNome.contains("&")
				|| parteNome.contains("0") || parteNome.contains("1") || parteNome.contains("2") || parteNome.contains("3") || parteNome.contains("4") || parteNome.contains("5") || parteNome.contains("6") || parteNome.contains("7") || parteNome.contains("8") || parteNome.contains("9")){
					listValidator.get(VALIDATE_NOME_MAE_CARACTERES_ESPECIAIS).add(ValidatorResult.ERROR, "O nome da mÃ£e nÃ£o pode conter carateres especiais ou nÃºmeros");
				}
			}
			//NOME DA MAE - Verifica se existe caracteres especiais no nome da mÃ£e
			if(partesNome.length <= 1){
				listValidator.get(VALIDATE_NOME_MAE_SEM_SOBRENOME).add(ValidatorResult.ERROR, "Nome da mÃ£e estÃ¡ sem sobrenome");
			}
			
			if(aluno.getNmPai() != null && !aluno.getNmPai().equals("")){
				partesNome = aluno.getNmPai().split(" ");
				for(String parteNome : partesNome){
					//NOME DA PAI - Verifica se existe espaÃ§os dentro do nome do pai
					if(parteNome.trim().equals("")){
						listValidator.get(VALIDATE_NOME_PAI_ESPACOS).add(ValidatorResult.ERROR, "HÃ¡ muitos espaÃ§os dentro do nome do pai");
					}
					//NOME DA PAI - Verifica se existe abreviaÃ§Ãµes dentro do nome do pai
					if(parteNome.length() <= 1 && !parteNome.trim().equals("E") && !parteNome.trim().equals("D")){
						listValidator.get(VALIDATE_NOME_PAI_ABREVIADO).add(ValidatorResult.ERROR, "Uma parte do nome do pai estÃ¡ abreviado");
					}
					//NOME DA PAI - Verifica se existe caracteres especiais no nome do pai
					if(parteNome.contains(",") || parteNome.contains(".") || parteNome.contains(";") || parteNome.contains("-") || parteNome.contains(":") || parteNome.contains("'") || parteNome.contains("*") || parteNome.contains("+") || parteNome.contains("/") || parteNome.contains("!") || parteNome.contains("@") || parteNome.contains("#") || parteNome.contains("$") || parteNome.contains("&")
					|| parteNome.contains("0") || parteNome.contains("1") || parteNome.contains("2") || parteNome.contains("3") || parteNome.contains("4") || parteNome.contains("5") || parteNome.contains("6") || parteNome.contains("7") || parteNome.contains("8") || parteNome.contains("9")){
						listValidator.get(VALIDATE_NOME_PAI_CARACTERES_ESPECIAIS).add(ValidatorResult.ERROR, "O nome do pai nÃ£o pode conter carateres especiais ou nÃºmeros");
					}
				}
				//NOME DA PAI - Verifica se existe caracteres especiais no nome do pai
				if(partesNome.length <= 1){
					listValidator.get(VALIDATE_NOME_PAI_SEM_SOBRENOME).add(ValidatorResult.ERROR, "Nome do pai estÃ¡ sem sobrenome");
				}
			}
			
			//RG - Verifica se os dados de RG foram preenchidos
			if(aluno.getNrRg() != null && !aluno.getNrRg().equals("") && (aluno.getDtEmissaoRg() == null || aluno.getCdEstadoRg() == 0 || aluno.getSgOrgaoRg() == null) && aluno.getLgFaltaDocumento() == 0){
				listValidator.get(VALIDATE_RG_INFORMACOES).add(ValidatorResult.ERROR, "Ao se colocar um nÃºmero de RG deve-se colocar data de emissÃ£o, orgÃ£o do RG e UF do RG");
			}
			
			//DATA NASCIMENTO - Verifica se a data de nascimento Ã© anterior a data atual
			if(aluno.getDtNascimento() != null && aluno.getDtNascimento().after(Util.getDataAtual())){
				listValidator.get(VALIDATE_DATA_NASCIMENTO_POSTERIOR_ATUAL).add(ValidatorResult.ERROR, "A data de nascimento nÃ£o pode ser posterior a data atual");
			}
			
			//CURSO/SERIE - Verifica se a idade nÃ£o estÃ¡ inconsistente com o curso
			if(aluno.getDtNascimento() != null){
				int qtIdade = Util.getIdade(aluno.getDtNascimento(), 31, 2, Integer.parseInt(InstituicaoPeriodoServices.getPeriodoRecenteOfSecretaria().getNmPeriodoLetivo()));
				ResultSetMap rsmMatriculaRecente = MatriculaServices.getMatriculaRegularRecenteByAluno(aluno.getCdAluno(), connect);
				if(rsmMatriculaRecente.next()){
					if(possuiDeficiencia(aluno.getCdAluno(), false, connect).getCode() > 0){
						PreparedStatement pstmt =connect.prepareStatement("SELECT * FROM acd_curso_multi WHERE cd_curso_multi = ? AND cd_curso = " + rsmMatriculaRecente.getInt("cd_curso"));
						pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_CRECHE", 0));
						ResultSetMap rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
						if(rsmCursoMatricula.next()){
							if(rsmMatriculaRecente.getInt("cd_curso") == 1179/*CÃ³digo de Curso fixo para Creche 2 anos*/ && qtIdade != 2){
								listValidator.get(VALIDATE_IDADE_INCONSISTENTE_2_ANOS).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade diferente de 2 anos, pois o mesmo possui matricula na modalidade Creche (2 Anos)", GRUPO_VALIDACAO_INSERT);
								listValidator.get(VALIDATE_IDADE_INCONSISTENTE_2_ANOS).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade diferente de 2 anos, pois o mesmo possui matricula na modalidade Creche (2 Anos)", GRUPO_VALIDACAO_UPDATE);
							}
							
							
							if(rsmMatriculaRecente.getInt("cd_curso") == 1147/*CÃ³digo de Curso fixo para Creche 3 anos*/ && qtIdade != 3){
								listValidator.get(VALIDATE_IDADE_INCONSISTENTE_3_ANOS).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade diferente de 3 anos, pois o mesmo possui matricula na modalidade Creche (3 Anos)", GRUPO_VALIDACAO_INSERT);
								listValidator.get(VALIDATE_IDADE_INCONSISTENTE_3_ANOS).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade diferente de 3 anos, pois o mesmo possui matricula na modalidade Creche (3 Anos)", GRUPO_VALIDACAO_UPDATE);
							}
						}
						else{
							if(rsmMatriculaRecente.getInt("cd_curso") != 1179/*CÃ³digo de Curso fixo para Creche 2 anos*/ && qtIdade == 2){
								listValidator.get(VALIDATE_IDADE_INCONSISTENTE_2_ANOS).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade de 2 anos, pois o mesmo possui matricula em modalidade diferente de Creche (2 Anos)", GRUPO_VALIDACAO_INSERT);
								listValidator.get(VALIDATE_IDADE_INCONSISTENTE_2_ANOS).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade de 2 anos, pois o mesmo possui matricula em modalidade diferente de Creche (2 Anos)", GRUPO_VALIDACAO_UPDATE);
							}
							
							
							if(rsmMatriculaRecente.getInt("cd_curso") != 1147/*CÃ³digo de Curso fixo para Creche 3 anos*/ && qtIdade == 3){
								listValidator.get(VALIDATE_IDADE_INCONSISTENTE_3_ANOS).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade de 3 anos, pois o mesmo possui matricula em modalidade diferente de Creche (3 Anos)", GRUPO_VALIDACAO_INSERT);
								listValidator.get(VALIDATE_IDADE_INCONSISTENTE_3_ANOS).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade de 3 anos, pois o mesmo possui matricula em modalidade diferente de Creche (3 Anos)", GRUPO_VALIDACAO_UPDATE);
							}
						}
						pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_PRE_ESCOLA", 0));
						rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
						if(rsmCursoMatricula.next()){
							if(rsmMatriculaRecente.getInt("cd_curso") == 1181/*CÃ³digo de Curso fixo para EducaÃ§Ã£o Infantil - 04 Anos*/ && qtIdade != 4){
								listValidator.get(VALIDATE_IDADE_INCONSISTENTE_4_ANOS).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade diferente de 4 anos, pois o mesmo possui matricula na modalidade EducaÃ§Ã£o Infantil - 04 Anos", GRUPO_VALIDACAO_INSERT);
								listValidator.get(VALIDATE_IDADE_INCONSISTENTE_4_ANOS).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade diferente de 4 anos, pois o mesmo possui matricula na modalidade EducaÃ§Ã£o Infantil - 04 Anos", GRUPO_VALIDACAO_UPDATE);
							}
							
							if(rsmMatriculaRecente.getInt("cd_curso") == 1159/*CÃ³digo de Curso fixo para EducaÃ§Ã£o Infantil - 05 Anos*/ && qtIdade != 5){
								listValidator.get(VALIDATE_IDADE_INCONSISTENTE_5_ANOS).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade diferente de 5 anos, pois o mesmo possui matricula na modalidade EducaÃ§Ã£o Infantil - 05 Anos", GRUPO_VALIDACAO_INSERT);
								listValidator.get(VALIDATE_IDADE_INCONSISTENTE_5_ANOS).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade diferente de 5 anos, pois o mesmo possui matricula na modalidade EducaÃ§Ã£o Infantil - 05 Anos", GRUPO_VALIDACAO_UPDATE);
							}
							
						}
						else{
							if(rsmMatriculaRecente.getInt("cd_curso") != 1181/*CÃ³digo de Curso fixo para EducaÃ§Ã£o Infantil - 04 Anos*/ && qtIdade == 4){
								listValidator.get(VALIDATE_IDADE_INCONSISTENTE_4_ANOS).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade de 4 anos, pois o mesmo possui matricula em modalidade diferente de EducaÃ§Ã£o Infantil - 04 Anos", GRUPO_VALIDACAO_INSERT);
								listValidator.get(VALIDATE_IDADE_INCONSISTENTE_4_ANOS).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade de 4 anos, pois o mesmo possui matricula em modalidade diferente de EducaÃ§Ã£o Infantil - 04 Anos", GRUPO_VALIDACAO_UPDATE);
							}
							
							if(rsmMatriculaRecente.getInt("cd_curso") != 1159/*CÃ³digo de Curso fixo para EducaÃ§Ã£o Infantil - 05 Anos*/ && qtIdade == 5){
								listValidator.get(VALIDATE_IDADE_INCONSISTENTE_5_ANOS).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade de 5 anos, pois o mesmo possui matricula em modalidade diferente de EducaÃ§Ã£o Infantil - 05 Anos", GRUPO_VALIDACAO_INSERT);
								listValidator.get(VALIDATE_IDADE_INCONSISTENTE_5_ANOS).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade de 5 anos, pois o mesmo possui matricula em modalidade diferente de EducaÃ§Ã£o Infantil - 05 Anos", GRUPO_VALIDACAO_UPDATE);
							}
						}
					}
					
					if(CursoServices.isEja(rsmMatriculaRecente.getInt("cd_curso"), connect)){
						if(qtIdade < 15){
							listValidator.get(VALIDATE_IDADE_INCONSISTENTE_EJA).add(ValidatorResult.ERROR, "NÃ£o Ã© possÃ­vel salvar aluno com idade menor do que 15 anos, pois o mesmo possui matricula no EJA");
						}
					}
					
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_matricula_origem", rsmMatriculaRecente.getString("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_MATRICULA_PERMISSAO_ESPECIAL, connect).getCdTipoOcorrencia(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_usuario", "" + ParametroServices.getValorOfParametroAsInteger("CD_USUARIO_PERMISSAO_ESPECIAL", 0), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmOcorrenciaPermissaoEspecial = OcorrenciaMatriculaServices.find(criterios, connect);
					
					if(rsmOcorrenciaPermissaoEspecial.size() > 0){
						listValidator.get(VALIDATE_IDADE_INCONSISTENTE_2_ANOS).add(ValidatorResult.VALID, "");
						listValidator.get(VALIDATE_IDADE_INCONSISTENTE_3_ANOS).add(ValidatorResult.VALID, "");
						listValidator.get(VALIDATE_IDADE_INCONSISTENTE_4_ANOS).add(ValidatorResult.VALID, "");
						listValidator.get(VALIDATE_IDADE_INCONSISTENTE_5_ANOS).add(ValidatorResult.VALID, "");
						listValidator.get(VALIDATE_IDADE_INCONSISTENTE_EJA).add(ValidatorResult.VALID, "");
						
					}
				}
				
			}
			
			
			
			
			//RG - Verificar se a data de emissÃ£o do RG Ã© posterior a data atual
			if(aluno.getDtEmissaoRg() != null && aluno.getLgFaltaDocumento() == 0){
				if(aluno.getDtEmissaoRg().after(Util.getDataAtual())){
					listValidator.get(VALIDATE_RG_DATA_POSTERIOR).add(ValidatorResult.ERROR, "A data de emissÃ£o de RG nÃ£o pode ser posterior a data atual");
				}
			}
			
			//RG - Verificar se a data de emissÃ£o Ã© igual a data de nascimento
			if(aluno.getDtNascimento() != null && aluno.getDtEmissaoRg() != null && aluno.getLgFaltaDocumento() == 0){
				
				GregorianCalendar dtEmissaoRg  = (GregorianCalendar)aluno.getDtEmissaoRg().clone();
				dtEmissaoRg.set(Calendar.HOUR_OF_DAY, 0);
				dtEmissaoRg.set(Calendar.MINUTE, 0);
				dtEmissaoRg.set(Calendar.SECOND, 0);
				dtEmissaoRg.set(Calendar.MILLISECOND, 0);
				GregorianCalendar dtNascimento = (GregorianCalendar)aluno.getDtNascimento().clone();
				dtNascimento.set(Calendar.HOUR_OF_DAY, 0);
				dtNascimento.set(Calendar.MINUTE, 0);
				dtNascimento.set(Calendar.SECOND, 0);
				dtNascimento.set(Calendar.MILLISECOND, 0);
				
				if(dtEmissaoRg.equals(dtNascimento)){
					listValidator.get(VALIDATE_RG_DATA_IGUAL_NASCIMENTO).add(ValidatorResult.ERROR, "A data de emissÃ£o de RG nÃ£o pode ser igual a data de nascimento");
				}
			}
			
			//PAIS DE ORIGEM - Verifica se o paÃ­s de origem marcado corresponde ao tipo de nacionalidade indicada
			if(((aluno.getTpNacionalidade() == PessoaFisicaServices.TP_NACIONALIDADE_BRASILEIRA) && aluno.getCdPais() != 1/*BRASIL*/) || 
			   ((aluno.getTpNacionalidade() == PessoaFisicaServices.TP_NACIONALIDADE_ESTRANGEIRA || aluno.getTpNacionalidade() == PessoaFisicaServices.TP_NACIONALIDADE_BRASILEIRA_EXTERIOR) && aluno.getCdPais() == 1/*BRASIL*/)){
				listValidator.get(VALIDATE_PAIS_DE_ORIGEM_NACIONALIDADE).add(ValidatorResult.ERROR, "PaÃ­s de Origem nÃ£o corresponde Ã  nacionalidade");
			}
			
			//NATURALIDADE BRASILEIRA - Verifica se a nacionalidade corr
			if(aluno.getTpNacionalidade() == PessoaFisicaServices.TP_NACIONALIDADE_BRASILEIRA && aluno.getCdNaturalidade() == 0){
				listValidator.get(VALIDATE_NACIONALIDADE_BRASILEIRA).add(ValidatorResult.ERROR, "Quando o aluno Ã© Brasileiro, ele deve ter sua naturalidade cadastrada");
			}
			
			//EMAIL - Verificar se o email do aluno estÃ¡ invÃ¡lido
			if(aluno.getNmEmail() != null && !aluno.getNmEmail().equals("")){
				if(!Util.isEmail(aluno.getNmEmail())){
					listValidator.get(VALIDATE_EMAIL_INVALIDO).add(ValidatorResult.ERROR, "Email do aluno estÃ¡ invÃ¡lido");
				}
			}
			
			
			//SEM DOCUMENTO - Verificar se existe permissÃ£o da Secretaria para deixar o aluno sem documento
			ResultSetMap rsmDocumentacaoAluno = PessoaServices.getAllDocumentosOfAluno(aluno.getCdAluno(), true, connect);
			if(aluno.getLgFaltaDocumento() == 0 && (aluno.getTpNacionalidade() != PessoaFisicaServices.TP_NACIONALIDADE_ESTRANGEIRA && ((aluno.getNrRg() == null || aluno.getNrRg().trim().equals("")) && (aluno.getNrCpf() == null || aluno.getNrCpf().trim().equals("")) && (rsmDocumentacaoAluno.size() == 0)))){
				listValidator.get(VALIDATE_PERMISSAO_ALUNO_SEM_DOCUMENTO).add(ValidatorResult.ERROR,  "NecessÃ¡rio permissÃ£o da Secretaria para permitir que o aluno fique sem documentaÃ§Ã£o", GRUPO_VALIDACAO_INSERT);
				listValidator.get(VALIDATE_PERMISSAO_ALUNO_SEM_DOCUMENTO).add(ValidatorResult.ERROR,  "NecessÃ¡rio permissÃ£o da Secretaria para permitir que o aluno fique sem documentaÃ§Ã£o", GRUPO_VALIDACAO_UPDATE);
			}
			else if(aluno.getLgFaltaDocumento() == 0 && (aluno.getTpNacionalidade() == PessoaFisicaServices.TP_NACIONALIDADE_ESTRANGEIRA)){
				boolean hasPassaporte = false;
				while(rsmDocumentacaoAluno.next()){
					if(rsmDocumentacaoAluno.getInt("cd_tipo_documentacao") == PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_PASSAPORTE){
						hasPassaporte = true;
						break;
					}
				}
				rsmDocumentacaoAluno.beforeFirst();
				
				if(!hasPassaporte){
					listValidator.get(VALIDATE_PERMISSAO_ALUNO_SEM_DOCUMENTO).add(ValidatorResult.ERROR,  "NecessÃ¡rio permissÃ£o da Secretaria para permitir que o aluno fique sem documentaÃ§Ã£o", GRUPO_VALIDACAO_INSERT);
					listValidator.get(VALIDATE_PERMISSAO_ALUNO_SEM_DOCUMENTO).add(ValidatorResult.ERROR,  "NecessÃ¡rio permissÃ£o da Secretaria para permitir que o aluno fique sem documentaÃ§Ã£o", GRUPO_VALIDACAO_UPDATE);
				}
			}
			
			
			//CADASTRO DUPLICADO - Aluno jÃ¡ possui cadastro no banco de dados
			if(aluno.getDtNascimento() != null){
				ResultSetMap rsmAluno = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_pessoa A, acd_aluno B, grl_pessoa_fisica C WHERE A.cd_pessoa = B.cd_aluno AND A.cd_pessoa = C.cd_pessoa AND A.nm_pessoa = '"+aluno.getNmPessoa()+"' AND C.dt_nascimento = '"+Util.convCalendarStringSqlCompleto(aluno.getDtNascimento())+"' AND C.nm_mae = '"+aluno.getNmMae()+"' AND A.cd_pessoa <> '"+aluno.getCdPessoa()+"'").executeQuery());
				if(rsmAluno.next()){
					listValidator.get(VALIDATE_CADASTRO_DUPLICADO).add(ValidatorResult.ERROR, "O aluno jÃ¡ possui cadastro no banco de dados do sistema");
				}
			}
			
			//RG - Data de emissÃ£o anterior a data de nascimento
			if(aluno.getDtNascimento() != null && aluno.getDtEmissaoRg() != null && aluno.getLgFaltaDocumento() == 0){
				if(aluno.getDtEmissaoRg().before(aluno.getDtNascimento())){
					listValidator.get(VALIDATE_RG_DATA_ANTERIOR).add(ValidatorResult.ERROR, "A data de emissÃ£o do RG nÃ£o pode ser anterior a data de nascimento");
				}
			}
			
			//RG - Verifica se o RG contÃ©m apenas zeros
			if(aluno.getNrRg() != null && !aluno.getNrRg().trim().equals("") && aluno.getLgFaltaDocumento() == 0){
				String nrRg[] = aluno.getNrRg().split("");
				boolean contemApenasZero = true;
				for(int i = 0; i < nrRg.length; i++){
					if(!nrRg[i].equals("0"))
						contemApenasZero = false;
				}
				if(contemApenasZero){
					listValidator.get(VALIDATE_RG_CONTEM_APENAS_ZEROS).add(ValidatorResult.ERROR, "O RG nÃ£o pode conter apenas zeros");
				}
			}
			
			//Falta de Documento
			ResultSetMap rsmDocumentacaoAlunoNecessaria = PessoaServices.getAllDocumentosOfAluno(aluno.getCdAluno(), true, connect);
			if(aluno.getLgFaltaDocumento() > 0 && (aluno.getTpNacionalidade() != PessoaFisicaServices.TP_NACIONALIDADE_ESTRANGEIRA && ((aluno.getNrRg() != null && !aluno.getNrRg().trim().equals("")) || (aluno.getNrCpf() != null && !aluno.getNrCpf().trim().equals("")) || (rsmDocumentacaoAlunoNecessaria.size() > 0)))){
				listValidator.get(VALIDATE_FALTA_DOCUMENTO).add(ValidatorResult.ERROR, "Aluno possui documentos porÃ©m estÃ¡ marcado que o mesmo nÃ£o possui documentos");
			}
			else if(aluno.getLgFaltaDocumento() > 0 && (aluno.getTpNacionalidade() == PessoaFisicaServices.TP_NACIONALIDADE_ESTRANGEIRA)){
				boolean hasPassaporte = false;
				while(rsmDocumentacaoAluno.next()){
					if(rsmDocumentacaoAluno.getInt("cd_tipo_documentacao") == PessoaTipoDocumentacaoServices.CD_TIPO_DOCUMENTACAO_PASSAPORTE){
						hasPassaporte = true;
						break;
					}
				}
				rsmDocumentacaoAluno.beforeFirst();
				
				if(hasPassaporte)
					listValidator.get(VALIDATE_FALTA_DOCUMENTO).add(ValidatorResult.ERROR, "Aluno possui documentos porÃ©m estÃ¡ marcado que o mesmo nÃ£o possui documentos");
			}
			
			//Cria a mensagem de erro em um objeto de retorno chamado 'RESULT'
			result.addListValidator(listValidator);
			result.verify(idGrupo);
			
			//RETORNO
			return result;
		
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoServices.validate: " + e);
			return new ValidatorResult(ValidatorResult.ERROR, "Erro em AlunoServices.validate");
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
			
			//ENDERECO - Verifica se existe um endereÃ§o
			if(endereco == null){
				listValidator.get(VALIDATE_ENDERECO).add(ValidatorResult.ERROR, "NÃ£o possui endereÃ§o cadastrado");
				return;
			}
			
			//CEP - Verificar se existe um CEP cadastrado e se o mesmo Ã© vÃ¡lido
			ResultSetMap rsmLogradouro = LogradouroServices.getEnderecoByCep(endereco.getNrCep(), connect);
			if(endereco.getNrCep() == null || endereco.getNrCep().trim().equals("") || endereco.getNrCep().equals("45000000") || rsmLogradouro == null  || rsmLogradouro.size() == 0){
				listValidator.get(VALIDATE_ENDERECO_CEP).add(ValidatorResult.ERROR, "O endereÃ§o nÃ£o possui CEP, ou o mesmo Ã© invÃ¡lido");
			}
			
			
			//LOGRADOURO - Verificar se existe logradouro cadastrado
			if(endereco != null && (endereco.getNmLogradouro() == null || endereco.getNmLogradouro().trim().equals(""))){
				listValidator.get(VALIDATE_ENDERECO_LOGRADOURO).add(ValidatorResult.ERROR, "O endereÃ§o nÃ£o possui logradouro");
			}
			
			
			//NUMERO - Verificar se existe nÃºmero de endereÃ§o cadastrado
			if(endereco != null && (endereco.getNrEndereco() == null || endereco.getNrEndereco().trim().equals(""))){
				listValidator.get(VALIDATE_ENDERECO_NUMERO).add(ValidatorResult.ERROR, "O endereÃ§o nÃ£o possui nÃºmero");
			}
			
			
			//BAIRRO - Verificar se existe bairro cadastrado
			if(endereco != null && (endereco.getNmBairro() == null || endereco.getNmBairro().trim().equals(""))){
				listValidator.get(VALIDATE_ENDERECO_BAIRRO).add(ValidatorResult.ERROR, "O endereÃ§o nÃ£o possui bairro");
			}
			
			//CIDADE - Verificar se existe cidade cadastrada
			if(endereco != null && (endereco.getCdCidade() == 0)){
				listValidator.get(VALIDATE_ENDERECO_CIDADE).add(ValidatorResult.ERROR, "O endereÃ§o nÃ£o possui cidade");
			}
			
			if(rsmLogradouro != null && rsmLogradouro.next()){
				if(rsmLogradouro.getInt("cd_cidade") != endereco.getCdCidade()){
					listValidator.get(VALIDATE_ENDERECO_CEP_CIDADE).add(ValidatorResult.ERROR, "A cidade do endereÃ§o nÃ£o corresponde ao cep cadastrado");
				}
			}
			
			
			//ZONA - Verificar se existe zona cadastrada
			if(endereco != null && (endereco.getTpZona() == 0)){
				listValidator.get(VALIDATE_ENDERECO_ZONA).add(ValidatorResult.ERROR, "O endereÃ§o nÃ£o possui zona");
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
		list.put(VALIDATE_NOME_PAI_CAIXA_VALIDACAO, new Validator(VALIDATE_NOME_PAI_CAIXA_VALIDACAO, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_PAI_NAO_DECLARADO, new Validator(VALIDATE_NOME_PAI_NAO_DECLARADO, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_PAI_IGUAL_MAE, new Validator(VALIDATE_NOME_PAI_IGUAL_MAE, ValidatorResult.VALID));
		list.put(VALIDATE_CAIXA_DE_SELECAO_NOME_PAI, new Validator(VALIDATE_CAIXA_DE_SELECAO_NOME_PAI, ValidatorResult.VALID));
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
		list.put(VALIDATE_NOME_ALUNO_ESPACOS, new Validator(VALIDATE_NOME_ALUNO_ESPACOS, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_ALUNO_ABREVIADO, new Validator(VALIDATE_NOME_ALUNO_ABREVIADO, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_ALUNO_CARACTERES_ESPECIAIS, new Validator(VALIDATE_NOME_ALUNO_CARACTERES_ESPECIAIS, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_ALUNO_SEM_SOBRENOME, new Validator(VALIDATE_NOME_ALUNO_SEM_SOBRENOME, ValidatorResult.VALID));
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
		list.put(VALIDATE_IDADE_INCONSISTENTE_2_ANOS, new Validator(VALIDATE_IDADE_INCONSISTENTE_2_ANOS, ValidatorResult.VALID));
		list.put(VALIDATE_IDADE_INCONSISTENTE_3_ANOS, new Validator(VALIDATE_IDADE_INCONSISTENTE_3_ANOS, ValidatorResult.VALID));
		list.put(VALIDATE_IDADE_INCONSISTENTE_4_ANOS, new Validator(VALIDATE_IDADE_INCONSISTENTE_4_ANOS, ValidatorResult.VALID));
		list.put(VALIDATE_IDADE_INCONSISTENTE_5_ANOS, new Validator(VALIDATE_IDADE_INCONSISTENTE_5_ANOS, ValidatorResult.VALID));
		list.put(VALIDATE_IDADE_INCONSISTENTE_EJA, new Validator(VALIDATE_IDADE_INCONSISTENTE_EJA, ValidatorResult.VALID));
		list.put(VALIDATE_RG_DATA_POSTERIOR, new Validator(VALIDATE_RG_DATA_POSTERIOR, ValidatorResult.VALID));
		list.put(VALIDATE_RG_DATA_IGUAL_NASCIMENTO, new Validator(VALIDATE_RG_DATA_IGUAL_NASCIMENTO, ValidatorResult.VALID));
		list.put(VALIDATE_PAIS_DE_ORIGEM_NACIONALIDADE, new Validator(VALIDATE_PAIS_DE_ORIGEM_NACIONALIDADE, ValidatorResult.VALID));
		list.put(VALIDATE_NACIONALIDADE_BRASILEIRA, new Validator(VALIDATE_NACIONALIDADE_BRASILEIRA, ValidatorResult.VALID));
		list.put(VALIDATE_EMAIL_INVALIDO, new Validator(VALIDATE_EMAIL_INVALIDO, ValidatorResult.VALID));
		list.put(VALIDATE_PERMISSAO_ALUNO_SEM_DOCUMENTO, new Validator(VALIDATE_PERMISSAO_ALUNO_SEM_DOCUMENTO, ValidatorResult.VALID));
		list.put(VALIDATE_CADASTRO_DUPLICADO, new Validator(VALIDATE_CADASTRO_DUPLICADO, ValidatorResult.VALID));
		list.put(VALIDATE_RG_DATA_ANTERIOR, new Validator(VALIDATE_RG_DATA_ANTERIOR, ValidatorResult.VALID));
		list.put(VALIDATE_RG_CONTEM_APENAS_ZEROS, new Validator(VALIDATE_RG_CONTEM_APENAS_ZEROS, ValidatorResult.VALID));
		list.put(VALIDATE_FALTA_DOCUMENTO, new Validator(VALIDATE_FALTA_DOCUMENTO, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_CENSO_INVALIDO, new Validator(VALIDATE_NOME_CENSO_INVALIDO, ValidatorResult.VALID));
		list.put(VALIDATE_ENDERECO_CEP_CIDADE, new Validator(VALIDATE_ENDERECO_CEP_CIDADE, ValidatorResult.VALID));
		
		
		
		return list;
	}
	
	public static Result saveCompliance(Aluno objeto, AuthData authData, int tpAcao, Connection connect) {
		try {
						
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_aluno (cd_aluno,"+
                    "cd_responsavel,"+
                    "nm_parentesco,"+
                    "nm_plano_saude,"+
                    "nm_medico,"+
                    "nm_profissao_pai,"+
                    "nm_profissao_mae,"+
                    "tp_filiacao,"+
                    "nr_inep,"+
                    "id_aluno,"+
                    "lg_bolsa_familia,"+
                    "lg_mais_educacao,"+
                    "nm_responsavel,"+
                    "tp_escolaridade_mae,"+
                    "tp_escolaridade_pai,"+
                    "tp_escolaridade_responsavel,"+
                    "lg_cadastro_problema,"+
                    "lg_pai_nao_declarado,"+
                    "lg_falta_documento,"+
                    "id_aluno_centaurus,"+
                    "nm_aluno_censo,"+
                    "nr_codigo_estadual,"+
                    "cd_usuario_compliance,"+
                    "dt_compliance,"+
                    "tp_acao_compliance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdResponsavel());
			pstmt.setString(3,objeto.getNmParentesco());
			pstmt.setString(4,objeto.getNmPlanoSaude());
			pstmt.setString(5,objeto.getNmMedico());
			pstmt.setString(6,objeto.getNmProfissaoPai());
			pstmt.setString(7,objeto.getNmProfissaoMae());
			pstmt.setInt(8,objeto.getTpFiliacao());
			pstmt.setString(9,objeto.getNrInep());
			pstmt.setString(10,objeto.getIdAluno());
			pstmt.setInt(11,objeto.getLgBolsaFamilia());
			pstmt.setInt(12,objeto.getLgMaisEducacao());
			pstmt.setString(13,objeto.getNmResponsavel());
			pstmt.setInt(14,objeto.getTpEscolaridadeMae());
			pstmt.setInt(15,objeto.getTpEscolaridadePai());
			pstmt.setInt(16,objeto.getTpEscolaridadeResponsavel());
			pstmt.setInt(17,objeto.getLgCadastroProblema());
			pstmt.setInt(18,objeto.getLgPaiNaoDeclarado());
			pstmt.setInt(19,objeto.getLgFaltaDocumento());
			pstmt.setInt(20,objeto.getIdAlunoCentaurus());
			pstmt.setString(21,objeto.getNmAlunoCenso());
			pstmt.setString(22,objeto.getNrCodigoEstadual());
			if(authData==null)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23, authData.getUsuario().getCdUsuario());
			pstmt.setTimestamp(24, new Timestamp(new GregorianCalendar().getTimeInMillis()));
			pstmt.setInt(25, tpAcao);
			
			int retorno = pstmt.executeUpdate();
			if(retorno <= 0){
				return new Result(retorno);
			}
			
			pstmt = connect.prepareStatement("INSERT INTO grl_pessoa (cd_pessoa,"+
                    "cd_pessoa_superior,"+
                    "cd_pais,"+
                    "nm_pessoa,"+
                    "nr_telefone1,"+
                    "nr_telefone2,"+
                    "nr_celular,"+
                    "nr_fax,"+
                    "nm_email,"+
                    "dt_cadastro,"+
                    "gn_pessoa,"+
                    "st_cadastro,"+
                    "nm_url,"+
                    "nm_apelido,"+
                    "txt_observacao,"+
                    "lg_notificacao,"+
                    "id_pessoa,"+
                    "cd_classificacao,"+
                    "cd_forma_divulgacao,"+
                    "dt_chegada_pais,"+
                    "cd_usuario_cadastro,"+
                    "cd_usuario_compliance,"+
                    "dt_compliance,"+
                    "tp_acao_compliance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdPessoa());
			if(objeto.getCdPessoaSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoaSuperior());
			if(objeto.getCdPais()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPais());
			pstmt.setString(4,objeto.getNmPessoa());
			pstmt.setString(5,objeto.getNrTelefone1());
			pstmt.setString(6,objeto.getNrTelefone2());
			pstmt.setString(7,objeto.getNrCelular());
			pstmt.setString(8,objeto.getNrFax());
			pstmt.setString(9,objeto.getNmEmail());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setInt(11,objeto.getGnPessoa());
			pstmt.setInt(12,objeto.getStCadastro());
			pstmt.setString(13,objeto.getNmUrl());
			pstmt.setString(14,objeto.getNmApelido());
			pstmt.setString(15,objeto.getTxtObservacao());
			pstmt.setInt(16,objeto.getLgNotificacao());
			pstmt.setString(17,objeto.getIdPessoa());
			if(objeto.getCdClassificacao()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdClassificacao());
			if(objeto.getCdFormaDivulgacao()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdFormaDivulgacao());
			if(objeto.getDtChegadaPais()==null)
				pstmt.setNull(20, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(20,new Timestamp(objeto.getDtChegadaPais().getTimeInMillis()));
			if(objeto.getCdUsuarioCadastro()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdUsuarioCadastro());
			if(authData==null)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22, authData.getUsuario().getCdUsuario());
			pstmt.setTimestamp(23, new Timestamp(new GregorianCalendar().getTimeInMillis()));
			pstmt.setInt(24, tpAcao);
			
			retorno = pstmt.executeUpdate();
			if(retorno <= 0){
				return new Result(retorno);
			}
			
			pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_fisica (cd_pessoa,"+
                    "cd_naturalidade,"+
                    "cd_escolaridade,"+
                    "dt_nascimento,"+
                    "nr_cpf,"+
                    "sg_orgao_rg,"+
                    "nm_mae,"+
                    "nm_pai,"+
                    "tp_sexo,"+
                    "st_estado_civil,"+
                    "nr_rg,"+
                    "nr_cnh,"+
                    "dt_validade_cnh,"+
                    "dt_primeira_habilitacao,"+
                    "tp_categoria_habilitacao,"+
                    "tp_raca,"+
                    "lg_deficiente_fisico,"+
                    "nm_forma_tratamento,"+
                    "cd_estado_rg,"+
                    "dt_emissao_rg,"+
                    "cd_conjuge,"+
                    "qt_membros_familia,"+
                    "vl_renda_familiar_per_capta,"+
                    "tp_nacionalidade,"+
                    "tp_filiacao_pai,"+
                    "lg_documento_ausente,"+
                    "cd_usuario_compliance,"+
                    "dt_compliance,"+
                    "tp_acao_compliance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdNaturalidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdNaturalidade());
			if(objeto.getCdEscolaridade()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEscolaridade());
			if(objeto.getDtNascimento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtNascimento().getTimeInMillis()));
			pstmt.setString(5,objeto.getNrCpf());
			pstmt.setString(6,objeto.getSgOrgaoRg());
			pstmt.setString(7,objeto.getNmMae());
			pstmt.setString(8,objeto.getNmPai());
			pstmt.setInt(9,objeto.getTpSexo());
			pstmt.setInt(10,objeto.getStEstadoCivil());
			pstmt.setString(11,objeto.getNrRg());
			pstmt.setString(12,objeto.getNrCnh());
			if(objeto.getDtValidadeCnh()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtValidadeCnh().getTimeInMillis()));
			if(objeto.getDtPrimeiraHabilitacao()==null)
				pstmt.setNull(14, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(14,new Timestamp(objeto.getDtPrimeiraHabilitacao().getTimeInMillis()));
			pstmt.setInt(15,objeto.getTpCategoriaHabilitacao());
			pstmt.setInt(16,objeto.getTpRaca());
			pstmt.setInt(17,objeto.getLgDeficienteFisico());
			pstmt.setString(18,objeto.getNmFormaTratamento());
			if(objeto.getCdEstadoRg()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdEstadoRg());
			if(objeto.getDtEmissaoRg()==null)
				pstmt.setNull(20, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(20,new Timestamp(objeto.getDtEmissaoRg().getTimeInMillis()));
			if(objeto.getCdConjuge()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdConjuge());
			pstmt.setInt(22,objeto.getQtMembrosFamilia());
			pstmt.setFloat(23,objeto.getVlRendaFamiliarPerCapta());
			pstmt.setInt(24,objeto.getTpNacionalidade());
			pstmt.setInt(25,objeto.getTpFiliacaoPai());
			pstmt.setInt(26,objeto.getLgDocumentoAusente());
			if(authData==null)
				pstmt.setNull(27, Types.INTEGER);
			else
				pstmt.setInt(27, authData.getUsuario().getCdUsuario());
			pstmt.setTimestamp(28, new Timestamp(new GregorianCalendar().getTimeInMillis()));
			pstmt.setInt(29, tpAcao);
			
			retorno = pstmt.executeUpdate();
			
			return new Result((retorno<0 ? retorno : objeto.getCdAluno()));
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