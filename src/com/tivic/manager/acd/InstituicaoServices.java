package com.tivic.manager.acd;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.docx4j.wml.R;
import org.eclipse.jdt.internal.compiler.flow.InsideSubRoutineFlowContext;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.geo.Localizacao;
import com.tivic.manager.geo.LocalizacaoDAO;
import com.tivic.manager.geo.LocalizacaoServices;
import com.tivic.manager.geo.Ponto;
import com.tivic.manager.geo.PontoDAO;
import com.tivic.manager.geo.PontoServices;
import com.tivic.manager.geo.Referencia;
import com.tivic.manager.geo.ReferenciaDAO;
import com.tivic.manager.geo.ReferenciaServices;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaDAO;
import com.tivic.manager.grl.Formulario;
import com.tivic.manager.grl.FormularioAtributo;
import com.tivic.manager.grl.FormularioAtributoDAO;
import com.tivic.manager.grl.FormularioAtributoOpcaoServices;
import com.tivic.manager.grl.FormularioAtributoServices;
import com.tivic.manager.grl.FormularioAtributoValor;
import com.tivic.manager.grl.FormularioAtributoValorDAO;
import com.tivic.manager.grl.FormularioDAO;
import com.tivic.manager.grl.LogradouroServices;
import com.tivic.manager.grl.OcorrenciaArquivoDAO;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.TipoOcorrenciaServices;
import com.tivic.manager.srh.DadosFuncionais;
import com.tivic.manager.srh.DadosFuncionaisDAO;
import com.tivic.manager.srh.FuncaoServices;
import com.tivic.manager.srh.TipoAdmissao;
import com.tivic.manager.srh.TipoAdmissaoDAO;
import com.tivic.manager.srh.TipoAdmissaoServices;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.Validator;
import com.tivic.manager.util.ValidatorResult;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;
import sun.security.krb5.internal.APOptions;

public class InstituicaoServices {

	public static final int LG_NAO_PERTENCE_REDE = 0;
	public static final int LG_PERTENCE_REDE 	 = 1;
	
	public static final int ST_INSTITUICAO_EM_ATIVIDADE  = 1;
	public static final int ST_INSTITUICAO_PARALISADA 	 = 2;
	public static final int ST_INSTITUICAO_EXTINTA  	 = 3;
	
	public static final int TP_OPCAO_CONEXAO_VELOX       = 1;
	public static final int TP_OPCAO_CONEXAO_CLARO       = 2;
	public static final int TP_OPCAO_CONEXAO_FIBRA       = 3;
	
	
	public static final int TP_INSTITUICAO_ESCOLA       = 0;
	public static final int TP_INSTITUICAO_CRECHE       = 1;
	
	//Grupos de Validação
	public static final int GRUPO_VALIDACAO_INSERT = 0;
	public static final int GRUPO_VALIDACAO_UPDATE = 1;
	public static final int GRUPO_VALIDACAO_EDUCACENSO = 2;
	
	//IDs de validação
	public static final int VALIDATE_GESTOR = 0;
	public static final int VALIDATE_GESTOR_NOME = 1;
	public static final int VALIDATE_GESTOR_CPF = 2;
	public static final int VALIDATE_GESTOR_EMAIL = 3;
	
	public static final int VALIDATE_EMAIL = 4;
	public static final int VALIDATE_SEM_TELEFONE = 5;
	public static final int VALIDATE_TELEFONE_1 = 6;
	public static final int VALIDATE_TELEFONE_2 = 7;
	public static final int VALIDATE_TELEFONE_CELULAR = 8;
	public static final int VALIDATE_TELEFONE_FAX = 9;
	
	public static final int VALIDATE_PERIODO_LETIVO_DATA_INICIAL = 10;
	public static final int VALIDATE_PERIODO_LETIVO_DATA_FINAL = 11;
	
	public static final int VALIDATE_ENDERECO = 12;
	public static final int VALIDATE_ENDERECO_CEP = 13;
	public static final int VALIDATE_ENDERECO_LOGRADOURO = 14;
	public static final int VALIDATE_ENDERECO_NUMERO = 15;
	public static final int VALIDATE_ENDERECO_BAIRRO = 16;
	public static final int VALIDATE_ENDERECO_CIDADE = 17;
	public static final int VALIDATE_ENDERECO_ZONA = 18;
	
	public static final int VALIDATE_LATITUDE  = 19;
	public static final int VALIDATE_LONGITUDE = 20;
	
	public static Result save(Instituicao instituicao, InstituicaoEducacenso educacenso, PessoaEndereco endereco){
		return save(instituicao, educacenso, endereco, null, null, null, null, null, null, null, null, null, null, null);
	}
	
	public static Result save(Instituicao instituicao, InstituicaoEducacenso educacenso, PessoaEndereco endereco, Connection connect){
		return save(instituicao, educacenso, endereco, null, null, null, null, null, null, null, null, null, null, connect);
	}
	
	public static Result save(Instituicao instituicao, InstituicaoEducacenso educacenso, PessoaEndereco endereco, 
			ArrayList<Integer> tiposMantenedora, ArrayList<Integer> locaisFuncionamento, 
			ArrayList<Integer> tiposEquipamento, ArrayList<Integer> tiposEtapa, Ponto pontoLocalizacao){
		return save(instituicao, educacenso, endereco, tiposMantenedora, locaisFuncionamento, 
				tiposEquipamento, tiposEtapa, pontoLocalizacao, null, null, null, null, null, null);
	}
	
	public static Result save(Instituicao instituicao, InstituicaoEducacenso educacenso, PessoaEndereco endereco, 
			ArrayList<Integer> tiposMantenedora, ArrayList<Integer> locaisFuncionamento, 
			ArrayList<Integer> tiposEquipamento, ArrayList<Integer> tiposEtapa, Ponto pontoLocalizacao,
			ArrayList<Integer> abastecimentoAgua, ArrayList<Integer> abastecimentoEnergia, ArrayList<Integer> esgotoSanitario, ArrayList<Integer> destinacaoLixo){
		return save(instituicao, educacenso, endereco, tiposMantenedora, locaisFuncionamento, 
				tiposEquipamento, tiposEtapa, pontoLocalizacao, null, abastecimentoAgua, abastecimentoEnergia, esgotoSanitario, destinacaoLixo, null);
	}
	
	public static Result save(Instituicao instituicao, InstituicaoEducacenso educacenso, PessoaEndereco endereco, Ponto pontoLocalizacao, int cdUsuario){
		return save(instituicao, educacenso, endereco, null, null, 
				null, null, pontoLocalizacao, null, null, null, null, null, cdUsuario, null);
	}
	
	public static Result save(Instituicao instituicao, InstituicaoEducacenso educacenso, PessoaEndereco endereco, Ponto pontoLocalizacao, DadosFuncionais dadosFuncionais, int cdUsuario){
		return save(instituicao, educacenso, endereco, null, null, 
				null, null, pontoLocalizacao, null, null, null, null, null, dadosFuncionais, cdUsuario, null);
	}
	
	public static Result save(Instituicao instituicao, InstituicaoEducacenso educacenso, PessoaEndereco endereco, 
			ArrayList<Integer> tiposMantenedora, ArrayList<Integer> locaisFuncionamento, 
			ArrayList<Integer> tiposEquipamento, ArrayList<Integer> tiposEtapa, Ponto pontoLocalizacao,
			ArrayList<Integer> abastecimentoAgua, ArrayList<Integer> abastecimentoEnergia, ArrayList<Integer> esgotoSanitario, ArrayList<Integer> destinacaoLixo, int cdUsuario){
		return save(instituicao, educacenso, endereco, tiposMantenedora, locaisFuncionamento, 
				tiposEquipamento, tiposEtapa, pontoLocalizacao, null, abastecimentoAgua, abastecimentoEnergia, esgotoSanitario, destinacaoLixo, cdUsuario, null);
	}
	
	public static Result save(Instituicao instituicao, InstituicaoEducacenso educacenso, PessoaEndereco endereco, 
			ArrayList<Integer> tiposMantenedora, ArrayList<Integer> locaisFuncionamento, 
			ArrayList<Integer> tiposEquipamento, ArrayList<Integer> tiposEtapa, Ponto pontoLocalizacao, ArrayList<FormularioAtributoValor> formulariosAtributoValor){
		return save(instituicao, educacenso, endereco, tiposMantenedora, locaisFuncionamento, 
				tiposEquipamento, tiposEtapa, pontoLocalizacao, formulariosAtributoValor, null, null, null, null, null);
	}

	public static Result save(Instituicao instituicao, InstituicaoEducacenso educacenso, PessoaEndereco endereco, 
			ArrayList<Integer> tiposMantenedora, ArrayList<Integer> locaisFuncionamento, 
			ArrayList<Integer> tiposEquipamento, ArrayList<Integer> tiposEtapa, Ponto pontoLocalizacao, Connection connect){
		return save(instituicao, educacenso, endereco, tiposMantenedora, locaisFuncionamento, 
				tiposEquipamento, tiposEtapa, pontoLocalizacao, null, null, null, null, null, null);
	}
	
	public static Result save(Instituicao instituicao, InstituicaoEducacenso educacenso, PessoaEndereco endereco, 
			ArrayList<Integer> tiposMantenedora, ArrayList<Integer> locaisFuncionamento, 
			ArrayList<Integer> tiposEquipamento, ArrayList<Integer> tiposEtapa, Ponto pontoLocalizacao, ArrayList<FormularioAtributoValor> formulariosAtributoValor,
			ArrayList<Integer> abastecimentoAgua, ArrayList<Integer> abastecimentoEnergia, ArrayList<Integer> esgotoSanitario, ArrayList<Integer> destinacaoLixo, Connection connect){
		return save(instituicao, educacenso, endereco, tiposMantenedora, locaisFuncionamento, 
				tiposEquipamento, tiposEtapa, pontoLocalizacao, formulariosAtributoValor, abastecimentoAgua, abastecimentoEnergia, esgotoSanitario, destinacaoLixo, 0, connect);
	}
	
	public static Result save(Instituicao instituicao, InstituicaoEducacenso educacenso, PessoaEndereco endereco, 
			ArrayList<Integer> tiposMantenedora, ArrayList<Integer> locaisFuncionamento, 
			ArrayList<Integer> tiposEquipamento, ArrayList<Integer> tiposEtapa, Ponto pontoLocalizacao, ArrayList<FormularioAtributoValor> formulariosAtributoValor,
			ArrayList<Integer> abastecimentoAgua, ArrayList<Integer> abastecimentoEnergia, ArrayList<Integer> esgotoSanitario, ArrayList<Integer> destinacaoLixo, int cdUsuario, Connection connect){
		return save(instituicao, educacenso, endereco, tiposMantenedora, locaisFuncionamento, 
				tiposEquipamento, tiposEtapa, pontoLocalizacao, formulariosAtributoValor, abastecimentoAgua, abastecimentoEnergia, esgotoSanitario, destinacaoLixo, null, cdUsuario, connect);
	}
	public static Result save(Instituicao instituicao, InstituicaoEducacenso educacenso, PessoaEndereco endereco, 
			ArrayList<Integer> tiposMantenedora, ArrayList<Integer> locaisFuncionamento, 
			ArrayList<Integer> tiposEquipamento, ArrayList<Integer> tiposEtapa, Ponto pontoLocalizacao, ArrayList<FormularioAtributoValor> formulariosAtributoValor,
			ArrayList<Integer> abastecimentoAgua, ArrayList<Integer> abastecimentoEnergia, ArrayList<Integer> esgotoSanitario, ArrayList<Integer> destinacaoLixo, DadosFuncionais dadosFuncionais, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(instituicao==null)
				return new Result(-1, "Erro ao salvar. Instituição é nula.");
			
			
			
			int retorno;
			ValidatorResult resultadoValidacao;
			
			//Variavel que testa se é uma inserção ou uma atualização
			boolean lgCadastroInstituicao = false;
			if(instituicao.getNmRazaoSocial() == null || instituicao.getNmRazaoSocial().trim().equals(""))
				instituicao.setNmRazaoSocial(instituicao.getNmPessoa());
			
			
			if(instituicao.getCdInstituicao() > 0){
				
				resultadoValidacao = validate(instituicao, endereco, pontoLocalizacao, cdUsuario, GRUPO_VALIDACAO_UPDATE, connect);
				if(!resultadoValidacao.hasPassed()){
					return resultadoValidacao;
				}
				
				retorno = InstituicaoDAO.update(instituicao, connect);
			}
			else {
				
				resultadoValidacao = validate(instituicao, endereco, pontoLocalizacao, cdUsuario, GRUPO_VALIDACAO_INSERT, connect);
				if(!resultadoValidacao.hasPassed()){
					return resultadoValidacao;
				}
				
				lgCadastroInstituicao = true;
				if(educacenso != null){
					if(educacenso.getStInstituicaoPublica() == InstituicaoEducacensoServices.ST_EM_ATIVIDADE){
						instituicao.setStCadastro(PessoaServices.ST_ATIVO);
					}
					else{
						instituicao.setStCadastro(PessoaServices.ST_INATIVO);	
					}
				}
				retorno = InstituicaoDAO.insert(instituicao, connect);
				instituicao.setCdInstituicao(retorno);
				
				InstituicaoPeriodo periodoSecretaria = InstituicaoPeriodoServices.getPeriodoRecenteOfSecretaria(connect);
				InstituicaoPeriodo instituicaoPeriodo = new InstituicaoPeriodo(0, instituicao.getCdInstituicao(), periodoSecretaria.getNmPeriodoLetivo(), Util.getDataAtual(), null, 0, InstituicaoPeriodoServices.ST_ATUAL, 1, periodoSecretaria.getCdPeriodoLetivo());
				int retPer = InstituicaoPeriodoDAO.insert(instituicaoPeriodo, connect);
				if(retPer < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir periodo letivo");
				}
				
				educacenso.setCdPeriodoLetivo(retPer);
				
			}
			
			if(retorno<=0)	{
				Conexao.rollback(connect);
				return new Result(-2, "Erro ao salvar Instituição!");
			}
			
			//Registra ocorrencia dependendo se for uma inserção ou atualização
			if(lgCadastroInstituicao){
				int cdTipoOcorrenciaCadastroInstituicao = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_INSTITUICAO, connect).getCdTipoOcorrencia();
				OcorrenciaInstituicao ocorrencia = null;
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_instituicao", "" + instituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("B.cd_tipo_ocorrencia", "" + InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_INSTITUICAO, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmOcorrenciaInstituicao = OcorrenciaInstituicaoServices.find(criterios, connect);
				if(rsmOcorrenciaInstituicao.next()){
					ocorrencia = OcorrenciaInstituicaoDAO.get(rsmOcorrenciaInstituicao.getInt("cd_ocorrencia"), connect);
				}
				
				if(ocorrencia == null){
					ocorrencia = new OcorrenciaInstituicao(0, instituicao.getCdInstituicao(), "Instituicao " + instituicao.getNmPessoa() + " cadastrada", Util.getDataAtual(), cdTipoOcorrenciaCadastroInstituicao, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicao.getCdInstituicao(), Util.getDataAtual(), cdUsuario, 0);
					Result ret = OcorrenciaInstituicaoServices.save(ocorrencia, connect);
					if(ret.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return ret;
					}
				}
				else{
					ocorrencia.setDtUltimaModificacao(Util.getDataAtual());
					ocorrencia.setCdUsuarioModificador(cdUsuario);
					Result ret = OcorrenciaInstituicaoServices.save(ocorrencia, connect);
					if(ret.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return ret;
					}
				}
			}
			else{
				int cdTipoOcorrenciaAlteracaoInstituicao = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_INSTITUICAO, connect).getCdTipoOcorrencia();
				OcorrenciaInstituicao ocorrencia = null;
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_instituicao", "" + instituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("B.cd_tipo_ocorrencia", "" + InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_INSTITUICAO, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmOcorrenciaInstituicao = OcorrenciaInstituicaoServices.find(criterios, connect);
				if(rsmOcorrenciaInstituicao.next()){
					ocorrencia = OcorrenciaInstituicaoDAO.get(rsmOcorrenciaInstituicao.getInt("cd_ocorrencia"), connect);
				}
				
				if(ocorrencia == null){
					ocorrencia = new OcorrenciaInstituicao(0, instituicao.getCdInstituicao(), "Instituicao " + instituicao.getNmPessoa() + " atualizada", Util.getDataAtual(), cdTipoOcorrenciaAlteracaoInstituicao, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicao.getCdInstituicao(), Util.getDataAtual(), cdUsuario, 0);
					OcorrenciaInstituicaoServices.save(ocorrencia, connect);
				}
				else{
					ocorrencia.setDtUltimaModificacao(Util.getDataAtual());
					ocorrencia.setCdUsuarioModificador(cdUsuario);
					OcorrenciaInstituicaoServices.save(ocorrencia, connect);
				}
			}
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			if(instituicao.getCdPessoaSuperior()==0){
				instituicao.setCdPessoaSuperior(cdSecretaria);
			}
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + instituicao.getCdInstituicao() + " AND nm_periodo_letivo = ?");
			
			//Atualiza as instituição vinculadas em relação ao periodo letivo
			ResultSetMap rsmPeriodosInstituicao = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + instituicao.getCdPessoaSuperior() + " ORDER BY CAST(nm_periodo_letivo AS INTEGER)").executeQuery());
			while(rsmPeriodosInstituicao.next()){
				
				pstmt.setString(1, rsmPeriodosInstituicao.getString("nm_periodo_letivo"));
				ResultSetMap rsmPeriodoInstituicaoVinculada = new ResultSetMap(pstmt.executeQuery());
				if(rsmPeriodoInstituicaoVinculada.next()){
					InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmPeriodoInstituicaoVinculada.getInt("cd_periodo_letivo"), connect);
					instituicaoPeriodo.setCdPeriodoLetivoSuperior(rsmPeriodosInstituicao.getInt("cd_periodo_letivo"));
					if(InstituicaoPeriodoDAO.update(instituicaoPeriodo, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar periodos");
					}
				}
			}
			rsmPeriodosInstituicao.beforeFirst();
			/*
			 * Atualiza ou inclui dados do educacenso
			 */
			if(educacenso != null){
				int cdPeriodoLetivoAtual = 0;
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(instituicao.getCdInstituicao(), connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
				
				int cdPeriodoLetivoSuperiorAtual = 0;
				rsmPeriodoAtual = getPeriodoLetivoVigente(instituicao.getCdPessoaSuperior(), connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivoSuperiorAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
				
				if(cdPeriodoLetivoAtual == 0){
					Result resultPeriodo = InstituicaoPeriodoServices.reaproveitamentoPeriodoLetivo(0, instituicao.getCdInstituicao(), cdPeriodoLetivoSuperiorAtual, cdUsuario, InstituicaoPeriodoServices.ST_ATUAL, connect);
					cdPeriodoLetivoAtual = ((InstituicaoPeriodo)resultPeriodo.getObjects().get("INSTITUICAOPERIODO")).getCdPeriodoLetivo();
				}
				
				if(educacenso.getCdInstituicao() == 0){
					educacenso.setCdInstituicao(instituicao.getCdInstituicao());
					educacenso.setCdPeriodoLetivo(cdPeriodoLetivoAtual);
					educacenso.setTpCategoriaPrivada(-1);
					educacenso.setTpConvenio(-1);
					educacenso.setStRegulamentacao(-1);
					educacenso.setLgPredioCompartilhado(-1);
					educacenso.setStAguaConsumida(-1);
					educacenso.setTpAbastecimentoAgua(-1);
					educacenso.setTpFornecimentoEnergia(-1);
					educacenso.setTpEsgotoSanitario(-1);
					educacenso.setTpDestinoLixo(-1);
					educacenso.setLgInternet(-1);
					educacenso.setLgBandaLarga(-1);
					educacenso.setLgAlimentacaoEscolar(-1);
					educacenso.setTpAtendimentoEspecializado(-1);
					educacenso.setTpAtividadeComplementar(-1);
					educacenso.setTpModalidadeEnsino(-1);
					educacenso.setLgEnsinoFundamentalCiclo(-1);
					educacenso.setTpLocalizacaoDiferenciada(-1);
					educacenso.setTpMaterialEspecifico(-1);
					educacenso.setLgEducacaoIndigena(-1);
					educacenso.setTpLingua(-1);
				}
				if(retorno>0)	{
					ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_educacenso " +
							                                "WHERE cd_instituicao = "+instituicao.getCdInstituicao() + " AND cd_periodo_letivo = " + cdPeriodoLetivoAtual).executeQuery();
					if(rs.next())
						retorno = InstituicaoEducacensoDAO.update(educacenso, connect);
					else
						retorno = InstituicaoEducacensoDAO.insert(educacenso, connect);
					
				}
			}

			/**
			 * ENDEREÇO
			 */
			if (endereco != null) {
				endereco.setCdPessoa(instituicao.getCdInstituicao());
				
				if (endereco.getCdEndereco() == 0) {
					retorno = PessoaEnderecoDAO.insert(endereco, connect);
					endereco.setCdEndereco(retorno);
				}
				else 
					retorno = PessoaEnderecoDAO.update(endereco, connect);
					
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar dados do endereço!");
				}
				
				if(endereco.getLgPrincipal()==1)
					connect.prepareStatement("UPDATE grl_pessoa_endereco SET lg_principal = 0 " +
							                    "WHERE cd_pessoa    = "+instituicao.getCdInstituicao()+
							                    "  AND cd_endereco <> "+endereco.getCdEndereco()).executeUpdate();
			}
			
			/**
			 * TIPOS DE MANTENEDORA
			 */
			if(tiposMantenedora!=null && tiposMantenedora.size()>0) {
				retorno = clearTipoMantenedora(instituicao.getCdInstituicao(), (educacenso == null ? 0 : educacenso.getCdPeriodoLetivo()), connect).getCode();
				for (Integer cd : tiposMantenedora) {
					retorno = addTipoMantenedora(instituicao.getCdInstituicao(), cd, (educacenso == null ? 0 : educacenso.getCdPeriodoLetivo()), connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar tipos de mantenedora!");
				}
			}
			
			/**
			 * LOCAIS DE FUNCIONAMENTO
			 */
			if(locaisFuncionamento!=null && locaisFuncionamento.size()>0) {
				retorno = clearLocalFuncionamento(instituicao.getCdInstituicao(), (educacenso == null ? 0 : educacenso.getCdPeriodoLetivo()), connect).getCode();
				
				for (Integer cd : locaisFuncionamento) {
					retorno = addLocalFuncionamento(instituicao.getCdInstituicao(), cd, (educacenso == null ? 0 : educacenso.getCdPeriodoLetivo()), connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar locais de funcionamento!");
				}
			}
			
			/**
			 * ABASTECIMENTO AGUA
			 */
			if(abastecimentoAgua!=null && abastecimentoAgua.size()>0) {
				retorno = clearAbastecimentoAgua(instituicao.getCdInstituicao(), (educacenso == null ? 0 : educacenso.getCdPeriodoLetivo()), connect).getCode();
				
				for (Integer cd : abastecimentoAgua) {
					retorno = addAbastecimentoAgua(instituicao.getCdInstituicao(), cd, (educacenso == null ? 0 : educacenso.getCdPeriodoLetivo()), connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar abastecimento de agua!");
				}
			}
			
			
			/**
			 * ABASTECIMENTO DE ENERGIA
			 */
			if(abastecimentoEnergia!=null && abastecimentoEnergia.size()>0) {
				retorno = clearAbastecimentoEnergia(instituicao.getCdInstituicao(), (educacenso == null ? 0 : educacenso.getCdPeriodoLetivo()), connect).getCode();
				
				for (Integer cd : abastecimentoEnergia) {
					retorno = addAbastecimentoEnergia(instituicao.getCdInstituicao(), cd, (educacenso == null ? 0 : educacenso.getCdPeriodoLetivo()), connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar abastecimento de energia!");
				}
			}
			
			
			/**
			 * ESGOTO SANITARIO
			 */
			if(esgotoSanitario!=null && esgotoSanitario.size()>0) {
				retorno = clearEsgotoSanitario(instituicao.getCdInstituicao(), (educacenso == null ? 0 : educacenso.getCdPeriodoLetivo()), connect).getCode();
				
				for (Integer cd : esgotoSanitario) {
					retorno = addEsgotoSanitario(instituicao.getCdInstituicao(), cd, (educacenso == null ? 0 : educacenso.getCdPeriodoLetivo()), connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar esgoto sanitario!");
				}
			}
			
			
			/**
			 * DESTINACAO DE LIXO
			 */
			if(destinacaoLixo!=null && destinacaoLixo.size()>0) {
				retorno = clearDestinacaoLixo(instituicao.getCdInstituicao(), (educacenso == null ? 0 : educacenso.getCdPeriodoLetivo()), connect).getCode();
				
				for (Integer cd : destinacaoLixo) {
					retorno = addDestinacaoLixo(instituicao.getCdInstituicao(), cd, (educacenso == null ? 0 : educacenso.getCdPeriodoLetivo()), connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar destinacao de lixo!");
				}
			}
			
			/**
			 * TIPO DE EQUIPAMENTO
			 */
			if(tiposEquipamento!=null && tiposEquipamento.size()>0) {
				retorno = clearTipoEquipamento(instituicao.getCdInstituicao(), connect).getCode();

				for (Integer cd : tiposEquipamento) {
					retorno = addTipoEquipamento(instituicao.getCdInstituicao(), cd, connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar tipos de equipamento!");
				}
			}

			/**
			 * TIPO DE ETAPA
			 */
			if(tiposEtapa!=null && tiposEtapa.size()>0) {
				retorno = clearTipoEtapa(instituicao.getCdInstituicao(), connect).getCode();
				
				for (Integer cd : tiposEtapa) {
					retorno = addTipoEtapa(instituicao.getCdInstituicao(), cd, connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar tipos de etapa!");
				}
			}
			
			/**
			 * LOCALIZACAO
			 */
						
			if(pontoLocalizacao != null) {
				
				if(pontoLocalizacao.getCdPonto()==0) { //inserir nova localizacao
					
					retorno = ReferenciaDAO.insert(new Referencia(0, instituicao.getNmPessoa(), ReferenciaServices.TP_REF_LOCALIZACAO, 
							ReferenciaServices.PONTO, null, null, 0), connect);
					
					if(retorno>0) {
						pontoLocalizacao.setCdReferencia(retorno);
						
						retorno = LocalizacaoDAO.insert(new Localizacao(0, retorno, 0, 0, 0, 0, 0, 0, 0, 0, instituicao.getCdInstituicao(), LocalizacaoServices.TP_LOCALIZACAO_POSICAO), connect);
						
						if(retorno>0) 
							retorno = PontoDAO.insert(pontoLocalizacao, connect);
					}
				}
				else {
					retorno = PontoDAO.update(pontoLocalizacao, connect);
				}

				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao gravar localização!");
				}
			}
			
			/**
			 * DADOS FUNCIONAIS
			 */	
			if(dadosFuncionais != null) {
				
				if(dadosFuncionais.getCdMatricula()==0) { //inserir novo dado funcional
					System.out.println("inserindo");
					retorno = DadosFuncionaisDAO.insert(dadosFuncionais, connect);
						
				}
				else {
					System.out.println("atualizando");
					retorno = DadosFuncionaisDAO.update(dadosFuncionais, connect);
				}

				if(retorno<0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao gravar dados funcionais!");
				}
			}
			
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			if(resultadoValidacao.getCode() == ValidatorResult.VALID)
				return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAO", instituicao);
			else{
				resultadoValidacao.getObjects().put("INSTITUICAO", instituicao);
				resultadoValidacao.setMessage("Salvo com sucesso...");
				return resultadoValidacao;
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-3, "Erro ao tentar salvar Instituição!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result remove(int cdInstituicao){
		return remove(cdInstituicao, false, null);
	}
	
	public static Result remove(int cdInstituicao, boolean cascade){
		return remove(cdInstituicao, cascade, null);
	}
	
	public static Result remove(int cdInstituicao, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				
			}
				
			if(!cascade || retorno>0)
				retorno = InstituicaoDAO.delete(cdInstituicao, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta instituição esta vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Instituição excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir instituição!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findAtivas(ArrayList<ItemComparator> criterios){
		ItemComparator itemComparatorNome = (criterios.size() > 0 ? criterios.get(0) : null);
		return getAllAtivas((itemComparatorNome != null ? itemComparatorNome.getValue() : null));
	}
	
	public static ResultSetMap getAllAtivas() {
		return getAll(0, false, true, null);
	}
	
	public static ResultSetMap getAllAtivas(int cdPeriodoLetivo) {
		return getAll(0, true, true, false, false, cdPeriodoLetivo, (Connection)null);
	}
	
	public static ResultSetMap getAllAtivas(String nmInstituicao) {
		return getAll(0, true, true, false, false, 0, nmInstituicao);
	}
	
	public static ResultSetMap getAllAtivas(boolean lgBuscarMatriculados) {
		return getAll(0, false, true, lgBuscarMatriculados, null);
	}
	
	/**
	 * Busca todas as instituições ativas do sistema
	 * @param lgBuscarMatriculados
	 * @param lgSituacaoAlunoCenso
	 * @return
	 */
	public static ResultSetMap getAllAtivas(boolean lgBuscarMatriculados, boolean lgSituacaoAlunoCenso) {
		return getAll(0, false, true, lgBuscarMatriculados, lgSituacaoAlunoCenso, false, null);
	}
	
	public static ResultSetMap getAllAtivasParalisadas(boolean lgBuscarMatriculados, boolean lgSituacaoAlunoCenso, boolean lgParalisadas) {
		return getAll(0, false, true, lgBuscarMatriculados, lgSituacaoAlunoCenso, lgParalisadas, null);
	}
		
	public static ResultSetMap getAll() {
		return getAll(0, false, false, null);
	}

	public static ResultSetMap getAll(Connection connect) {
		return getAll(0, false, false, connect);
	}

	public static ResultSetMap getAll(int cdInstituicaoSuperior) {
		return getAll(cdInstituicaoSuperior, false, false, null);
	}
	
	public static ResultSetMap getAll(int cdInstituicaoSuperior, Connection connect) {
		return getAll(cdInstituicaoSuperior, false, false, connect);
	}
	
	public static ResultSetMap getAll(boolean lgSemSecretaria) {
		return getAll(0, lgSemSecretaria, false, null);
	}
	
	public static ResultSetMap getAll(boolean lgSemSecretaria, boolean lgAtivas) {
		return getAll(0, lgSemSecretaria, lgAtivas, null);
	}
	
	public static ResultSetMap getAll(boolean lgSemSecretaria, Connection connect) {
		return getAll(0, lgSemSecretaria, false, connect);
	}
	
	public static ResultSetMap getAll(int cdInstituicaoSuperior, boolean lgSemSecretaria) {
		return getAll(cdInstituicaoSuperior, lgSemSecretaria, false, null);
	}
	
	public static ResultSetMap getAll(int cdInstituicaoSuperior, boolean lgSemSecretaria, boolean lgAtivas, Connection connect) {
		return getAll(cdInstituicaoSuperior, lgSemSecretaria, lgAtivas, false, null);
	}
	
	public static ResultSetMap getAll(int cdInstituicaoSuperior, boolean lgSemSecretaria, boolean lgAtivas, boolean lgBuscarMatriculados, Connection connect) {
		return getAll(cdInstituicaoSuperior, lgSemSecretaria, lgAtivas, lgBuscarMatriculados, false, connect);
	}
	
	public static ResultSetMap getAll(int cdInstituicaoSuperior, boolean lgSemSecretaria, boolean lgAtivas, boolean lgBuscarMatriculados, boolean lgSituacaoAlunoCenso, Connection connect) {
		return getAll(cdInstituicaoSuperior, lgSemSecretaria, lgAtivas, lgBuscarMatriculados, lgSituacaoAlunoCenso, 0, connect);
	}
	
	public static ResultSetMap getAll(int cdInstituicaoSuperior, boolean lgSemSecretaria, boolean lgAtivas, boolean lgBuscarMatriculados, boolean lgSituacaoAlunoCenso, boolean lgParalisadas, Connection connect) {
		return getAll(cdInstituicaoSuperior, lgSemSecretaria, lgAtivas, lgBuscarMatriculados, lgSituacaoAlunoCenso, 0, lgParalisadas, connect);
	}
	
	public static ResultSetMap getAll(int cdInstituicaoSuperior, boolean lgSemSecretaria, boolean lgAtivas, boolean lgBuscarMatriculados, boolean lgSituacaoAlunoCenso, int cdPeriodoLetivo, Connection connect) {
		return getAll(cdInstituicaoSuperior, lgSemSecretaria, lgAtivas, lgBuscarMatriculados, lgSituacaoAlunoCenso, cdPeriodoLetivo, null, connect);
	}
	
	public static ResultSetMap getAll(int cdInstituicaoSuperior, boolean lgSemSecretaria, boolean lgAtivas, boolean lgBuscarMatriculados, boolean lgSituacaoAlunoCenso, int cdPeriodoLetivo, boolean lgParalisadas, Connection connect) {
		return getAll(cdInstituicaoSuperior, lgSemSecretaria, lgAtivas, lgBuscarMatriculados, lgSituacaoAlunoCenso, cdPeriodoLetivo, null, lgParalisadas, connect);
	}
	
	public static ResultSetMap getAll(int cdInstituicaoSuperior, boolean lgSemSecretaria, boolean lgAtivas, boolean lgBuscarMatriculados, boolean lgSituacaoAlunoCenso, int cdPeriodoLetivo, String nmInstituicao) {
		return getAll(cdInstituicaoSuperior, lgSemSecretaria, lgAtivas, lgBuscarMatriculados, lgSituacaoAlunoCenso, cdPeriodoLetivo, nmInstituicao, null);
	}
	
	public static ResultSetMap getAll(int cdInstituicaoSuperior, boolean lgSemSecretaria, boolean lgAtivas, boolean lgBuscarMatriculados, boolean lgSituacaoAlunoCenso, int cdPeriodoLetivo, String nmInstituicao, Connection connect) {
		return getAll(cdInstituicaoSuperior, lgSemSecretaria, lgAtivas, lgBuscarMatriculados, lgSituacaoAlunoCenso, cdPeriodoLetivo, nmInstituicao, false, connect);
	}
	
	public static ResultSetMap getAll(int cdInstituicaoSuperior, boolean lgSemSecretaria, boolean lgAtivas, boolean lgBuscarMatriculados, boolean lgSituacaoAlunoCenso, int cdPeriodoLetivo, String nmInstituicao, boolean lgParalisadas, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			LogUtils.debug("InstituicaoServices.getAll");
			LogUtils.createTimer("INSTITUICAO_GETALL_TIMER");
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodo = getPeriodoLetivoRecente((cdInstituicaoSuperior == 0 ? ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0) : cdInstituicaoSuperior));
				if(rsmPeriodo.next())
					cdPeriodoLetivo = rsmPeriodo.getInt("cd_periodo_letivo");
			}
			
			String sql = "SELECT A.*, B.*, B.nm_pessoa AS nm_instituicao, "
				    + "B.nm_pessoa AS nm_fantasia, B.nr_telefone1, B.nr_telefone2, B.nr_fax, " 
			        + "B.nm_email, B.nm_url, B.nm_pessoa as nm_pessoa, C.cd_periodo_letivo, C.st_instituicao_publica, " 
			        + "D.cd_endereco, D.ds_endereco, D.cd_tipo_logradouro, D.cd_tipo_endereco, D.cd_logradouro, " 
			        + "D.cd_bairro, D.cd_cidade, D.nm_logradouro, D.nm_bairro, D.nr_cep, D.nr_endereco, " 
			        + "D.nm_complemento, D.nr_telefone, D.nm_ponto_referencia, " 
			        + "E.nm_cidade, GEO_P.vl_longitude, GEO_P.vl_latitude " 
					+ " FROM acd_instituicao A "
					+ " JOIN grl_pessoa B ON( A.cd_instituicao = B.cd_pessoa ) "
					+ " JOIN acd_instituicao_periodo IP ON( A.cd_instituicao = IP.cd_instituicao AND IP.cd_periodo_letivo_superior = "+cdPeriodoLetivo+" ) "
					+ " JOIN acd_instituicao_educacenso C ON( A.cd_instituicao = C.cd_instituicao AND C.cd_periodo_letivo = IP.cd_periodo_letivo ) "
					+ " LEFT OUTER JOIN grl_pessoa_endereco D ON (B.cd_pessoa = D.cd_pessoa AND D.lg_principal = 1) "
			        + " LEFT OUTER JOIN grl_cidade E ON (D.cd_cidade = E.cd_cidade) "
			        + " LEFT OUTER JOIN geo_localizacao GEO_L ON (A.cd_instituicao = GEO_L.cd_pessoa) "
			        + " LEFT OUTER JOIN geo_ponto 		GEO_P ON (GEO_L.cd_referencia = GEO_P.cd_referencia) " 
					+ " WHERE 1=1 " 
					+ (cdInstituicaoSuperior > 0 ? " AND (B.cd_pessoa = "+cdInstituicaoSuperior+" OR B.cd_pessoa_superior = "+cdInstituicaoSuperior+") " : "" ) 
					+ (lgSemSecretaria ? " AND A.cd_instituicao <> " + ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0) : "" ) 
					+ (lgAtivas ? " AND C.st_instituicao_publica IN (" + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + (lgParalisadas ?  ", " + InstituicaoEducacensoServices.ST_PARALISADA : "") + ")" : "")
					+ (lgParalisadas ? " AND C.st_instituicao_publica IN (" + InstituicaoEducacensoServices.ST_PARALISADA + (lgAtivas ?  ", " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE : "") + ")" : "")
					+ (nmInstituicao != null ? " AND B.nm_pessoa LIKE '%"+nmInstituicao.toUpperCase()+"%'" : "")
					+ " ORDER BY B.nm_pessoa";
			
			pstmt = connect.prepareStatement(sql);
			
			LogUtils.debug("SQL:\n"+Search.getStatementSQL(sql, (" ORDER BY B.NM_PESSOA"), null, true));
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			
			LogUtils.logTimer("INSTITUICAO_GETALL_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			LogUtils.debug("InstituicaoServices.getAll: Iniciando injeção de dados adicionais...");
			
			while(rsm.next()){
				
				//Acrescenta um sufixo caso a instituiçao seja paralisada
				if(rsm.getInt("st_instituicao_publica") == InstituicaoEducacensoServices.ST_PARALISADA){
					rsm.setValueToField("NM_INSTITUICAO", rsm.getString("NM_INSTITUICAO") + " (Paralisada)");
				}
				
				//Usado para saber o numero de alunos matriculados em cada escola, porém atrasa muito a busca
				if(lgBuscarMatriculados){
					ResultSetMap rsmMatriculas = new ResultSetMap(connect.prepareStatement("SELECT COUNT(*) AS NR_MATRICULADOS FROM acd_matricula A, acd_turma B WHERE A.cd_turma = B.cd_turma AND B.cd_instituicao = " + rsm.getInt("cd_instituicao") + " AND B.cd_periodo_letivo = " + rsm.getInt("cd_periodo_letivo") + " AND A.st_matricula IN (" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_CONCLUIDA + ") AND B.st_turma IN (" + TurmaServices.ST_ATIVO + ", " + TurmaServices.ST_CONCLUIDO + ")").executeQuery());
					int qtAlunos = 0;
					while(rsmMatriculas.next()){
						qtAlunos += rsmMatriculas.getInt("nr_matriculados");
					}
					rsm.setValueToField("NR_MATRICULADOS", qtAlunos);
				}
				
				//Caso a busca esteja sendo feita para a segunda fase do Educacenso, faz verificações em relação a fase em que cada escola está no periodo letivo atual
				if(lgSituacaoAlunoCenso){
					InstituicaoPeriodo instituicaoPeriodoAtual = null;
					ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(rsm.getInt("cd_instituicao"), connect);
					if(rsmPeriodoAtual.next()){
						instituicaoPeriodoAtual = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect);
					}
					
					int cdPeriodoLetivoSituacao = 0;
					ResultSetMap rsmPeriodoSituacaoAlunoCenso = new ResultSetMap(connect.prepareStatement("SELECT cd_periodo_letivo FROM acd_instituicao_periodo WHERE nm_periodo_letivo = '"+(Integer.parseInt(instituicaoPeriodoAtual.getNmPeriodoLetivo()))+"' AND cd_instituicao = " + rsm.getString("cd_instituicao")).executeQuery());
					if(rsmPeriodoSituacaoAlunoCenso.next()){
						cdPeriodoLetivoSituacao = rsmPeriodoSituacaoAlunoCenso.getInt("cd_periodo_letivo");
					}
					
					ResultSetMap rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_turma WHERE cd_instituicao = " + rsm.getInt("cd_instituicao") + " AND cd_periodo_letivo = " + cdPeriodoLetivoSituacao + " AND st_turma IN (" + TurmaServices.ST_ATIVO + ", " + TurmaServices.ST_CONCLUIDO + ") AND nm_turma NOT LIKE 'TRANS%' AND  cd_curso NOT IN ("+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)+")").executeQuery()); 
					float qtTurmas = rsmTurmas.size();
					float qtTurmasFechadas = 0;
					while(rsmTurmas.next()){
						ResultSetMap rsmTurmaSituacaoAlunoCenso = new ResultSetMap(connect.prepareStatement("SELECT cd_tipo_ocorrencia FROM grl_ocorrencia A, acd_ocorrencia_turma B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO + ") AND cd_turma = "+rsmTurmas.getInt("cd_turma")+" order by dt_ocorrencia DESC").executeQuery());
						if(rsmTurmaSituacaoAlunoCenso.next()){
							if(rsmTurmaSituacaoAlunoCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO)){
								qtTurmasFechadas++;
							}
						}
					}
					
					rsm.setValueToField("PR_TURMAS_FECHADAS", (qtTurmas > 0 ? (qtTurmasFechadas/qtTurmas * 100) : 0));
					rsm.setValueToField("NM_INSTITUICAO", rsm.getString("NM_INSTITUICAO") + " ("+(Util.formatNumber((qtTurmas > 0 ? (qtTurmasFechadas/qtTurmas * 100) : 0), 2)) + "% de turmas fechadas)");
					
					ResultSetMap rsmGeracaoArquivoSituacaoAlunoCenso = new ResultSetMap(connect.prepareStatement("SELECT cd_tipo_ocorrencia FROM grl_ocorrencia A, acd_ocorrencia_instituicao B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_GERACAO_ARQUIVO_SITUACAO_ALUNO_CENSO+ ") AND cd_instituicao = "+rsm.getInt("cd_instituicao")+" AND cd_periodo_letivo = "+cdPeriodoLetivoSituacao+" order by dt_ocorrencia DESC").executeQuery());
					if(rsmGeracaoArquivoSituacaoAlunoCenso.next()){
						rsm.setValueToField("LG_GERACAO_ARQUIVO_SITUACAO_ALUNO", 1);
						ResultSetMap rsmFinalizacaoSituacaoAlunoCenso = new ResultSetMap(connect.prepareStatement("SELECT cd_tipo_ocorrencia FROM grl_ocorrencia A, acd_ocorrencia_instituicao B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_ESCOLA_SITUACAO_ALUNO_CENSO + ") AND cd_instituicao = "+rsm.getInt("cd_instituicao")+" AND cd_periodo_letivo = "+cdPeriodoLetivoSituacao+" order by dt_ocorrencia DESC").executeQuery());
						if(rsmFinalizacaoSituacaoAlunoCenso.next()){
							if(rsmFinalizacaoSituacaoAlunoCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_SITUACAO_ALUNO_CENSO)){
								rsm.setValueToField("LG_FINALIZADA", 1);
							}
							else{
								rsm.setValueToField("LG_FINALIZADA", 0);
							}
						}
						else{
							rsm.setValueToField("LG_FINALIZADA", 0);
						}
					}
					else{
						rsm.setValueToField("LG_GERACAO_ARQUIVO_SITUACAO_ALUNO", 0);
						rsm.setValueToField("LG_FINALIZADA", 0);
					}
				}
				
				//Caso não seja da segunda fase do censo, o sistema irá fazer as verificações para a primeira fase
				else{
					
					ResultSetMap rsmInstituicaoValidada = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_instituicao_pendencia WHERE cd_instituicao = " + rsm.getInt("cd_instituicao")).executeQuery());
					if(rsmInstituicaoValidada.next()){
						rsm.setValueToField("LG_VALIDADA", 0);
					}
					else{
						rsm.setValueToField("LG_VALIDADA", 1);
					}
					
					ResultSetMap rsmGeracaoArquivoMatriculainicialCenso = new ResultSetMap(connect.prepareStatement("SELECT cd_tipo_ocorrencia FROM grl_ocorrencia A, acd_ocorrencia_instituicao B, acd_instituicao_periodo C "
							+ "																							WHERE A.cd_ocorrencia = B.cd_ocorrencia "
							+ "																							  AND B.cd_instituicao = C.cd_instituicao"
							+ "																							  AND B.cd_periodo_letivo = C.cd_periodo_letivo"
							+ "																							  AND C.cd_periodo_letivo_superior = "+cdPeriodoLetivo
							+ "																							  AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_GERACAO_ARQUIVO_MATRICULA_INICIAL+ ") "
							+ "																							  AND B.cd_instituicao = "+rsm.getInt("cd_instituicao")
							+ "																							ORDER BY dt_ocorrencia DESC").executeQuery());
					if(rsmGeracaoArquivoMatriculainicialCenso.next()){
						rsm.setValueToField("LG_GERACAO_ARQUIVO_MATRICULA_INICIAL", 1);
						ResultSetMap rsmFinalizacaoMatriculainicialCenso = new ResultSetMap(connect.prepareStatement("SELECT cd_tipo_ocorrencia FROM grl_ocorrencia A, acd_ocorrencia_instituicao B, acd_instituicao_periodo C "
								+ "																							WHERE A.cd_ocorrencia = B.cd_ocorrencia "
								+ "																							  AND B.cd_instituicao = C.cd_instituicao"
								+ "																							  AND B.cd_periodo_letivo = C.cd_periodo_letivo"
								+ "																							  AND C.cd_periodo_letivo_superior = "+cdPeriodoLetivo
								+ "																							  AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_MATRICULA_INICIAL + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_ESCOLA_MATRICULA_INICIAL+ ") "
								+ "																							  AND B.cd_instituicao = "+rsm.getInt("cd_instituicao")
								+ "																							ORDER BY dt_ocorrencia DESC").executeQuery());
						if(rsmFinalizacaoMatriculainicialCenso.next()){
							if(rsmFinalizacaoMatriculainicialCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_MATRICULA_INICIAL)){
								rsm.setValueToField("LG_FINALIZADA", 1);
							}
							else{
								rsm.setValueToField("LG_FINALIZADA", 0);
							}
						}
						else{
							rsm.setValueToField("LG_FINALIZADA", 0);
						}
					}
					else{
						rsm.setValueToField("LG_GERACAO_ARQUIVO_MATRICULA_INICIAL", 0);
						rsm.setValueToField("LG_FINALIZADA", 0);
					}
					
					
					
				}
			}
			rsm.beforeFirst();
			
//			ArrayList<String> fields = new ArrayList<String>();
//			fields.add("PR_TURMAS_FECHADAS");
//			fields.add("NM_INSTITUICAO");
//			rsm.orderBy(fields);
			
//			LogUtils.debug(rsm.size() + " registro(s)");
//			LogUtils.logTimer("INSTITUICAO_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
//			LogUtils.destroyTimer("INSTITUICAO_FIND_TIMER");
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap getAllByCirculo(int cdCirculo) {
		return getAllByCirculo(cdCirculo, null);
	}

	/**
	 * Busca todas as instituições que estão em um determinado circulo
	 * @param cdCirculo
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAllByCirculo(int cdCirculo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			ResultSetMap rsmParcial = new ResultSetMap();
			ResultSetMap rsmFinal = new ResultSetMap();
			
			int cdPeriodoSuperior = 0;
			ResultSetMap rsmPeriodo = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0), connect);
			if(rsmPeriodo.next())
				cdPeriodoSuperior = rsmPeriodo.getInt("cd_periodo_letivo");
			
			pstmt = connect.prepareStatement("SELECT A.cd_instituicao, B.tp_localizacao FROM acd_instituicao A, acd_instituicao_educacenso B, acd_instituicao_periodo C, grl_pessoa D "
					+ "							WHERE A.cd_instituicao = B.cd_instituicao "
					+ "							  AND A.cd_instituicao = C.cd_instituicao "
					+ "							  AND A.cd_instituicao = D.cd_pessoa "
					+ "							  AND B.cd_periodo_letivo = C.cd_periodo_letivo"
					+ " 						  AND C.cd_periodo_letivo_superior = " + cdPeriodoSuperior
					+ " 						  AND B.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "							ORDER BY D.nm_pessoa");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				
				if(cdCirculo == 999){
					if(rsm.getInt("tp_localizacao") == InstituicaoEducacensoServices.TP_LOCALIZACAO_URBANA){
						rsmParcial.addRegister(rsm.getRegister());
					}
				}
				else{
					if(CirculoServices.pertenceCirculo(cdCirculo, rsm.getInt("cd_instituicao"), connect)){
						rsmParcial.addRegister(rsm.getRegister());
					}
				}
				
			}
			rsm.beforeFirst();
			
			rsmParcial.beforeFirst();
			
			while(rsmParcial.next()){
				
				Result result = getResumo(rsmParcial.getInt("cd_instituicao"), connect);
				
				rsmFinal.addRegister((HashMap<String, Object>)result.getObjects().get("INSTITUICAO_RESUMO"));
			}
			rsmParcial.beforeFirst();
			
			rsmFinal.beforeFirst();
			
			return rsmFinal;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoServices.getAllByCirculo: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoServices.getAllByCirculo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getInstituicao(int cdInstituicao) {
		return getInstituicao(cdInstituicao, null);
	}

	/**
	 * Metodo que retorna uma instituição a partir de um código
	 * @param cdInstituicao
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getInstituicao(int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.*, B.nm_pessoa AS nm_instituicao FROM acd_instituicao A JOIN grl_pessoa B ON( A.cd_instituicao = B.cd_pessoa ) WHERE A.cd_instituicao = " + cdInstituicao);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	public static ResultSetMap getAllHorarios(int cdInstituicao) {
		return getAllHorarios(cdInstituicao, null);
	}

	/**
	 * Busca todos os horários de uma determinada instituição
	 * @param cdInstituicao
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getAllHorarios(int cdInstituicao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connection);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM acd_instituicao_horario " +
					"WHERE cd_instituicao = ?"+
					"  AND cd_periodo_letivo = ?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdPeriodoLetivoAtual);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("TP_TURNO");
			rsm.orderBy(fields);

			/* construção de resulset por agrupamento de horários (por turno) */
			ResultSetMap rsmFinal = new ResultSetMap();
			String[] idsDays = {"LG_DOMINGO", "LG_SEGUNDA", "LG_TERCA", "LG_QUARTA", "LG_QUINTA", "LG_SEXTA", "LG_SABADO"};
			int tpTurno = -1;
			HashMap<String, Object> register = null;
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			while (rsm.next()) {
				if (rsm.getInt("tp_turno") != tpTurno) {
					tpTurno = rsm.getInt("tp_turno");
					register = new HashMap<String, Object>();
					register.put("TP_TURNO", tpTurno);
					register.put("NM_TP_TURNO", InstituicaoHorarioServices.tiposTurno[tpTurno]);
					register.put("CD_INSTITUICAO", rsm.getInt("cd_instituicao"));
					register.put("HR_INICIO", sdf.format(rsm.getObject("hr_inicio")));
					register.put("HR_TERMINO", sdf.format(rsm.getObject("hr_termino")));
					rsmFinal.addRegister(register);
				}
				register.put("HR_INICIO_" + rsm.getInt("nr_dia_semana"), rsm.getObject("hr_inicio"));
				register.put("HR_TERMINO_" + rsm.getInt("nr_dia_semana"), rsm.getObject("hr_termino"));
				register.put(idsDays[rsm.getInt("nr_dia_semana")], 1);
			}
			while (rsmFinal.next())
				for (int i=0; i<idsDays.length; i++)
					rsmFinal.getRegister().put(idsDays[i], rsmFinal.getRegister().get(idsDays[i])!=null ? 1 : 0);
			rsmFinal.beforeFirst();
			
			return rsmFinal;
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

	public static ResultSetMap getAllPeriodos(int cdInstituicao) {
		return getAllPeriodos(cdInstituicao, null);
	}

	/**
	 * Busca todos os periodos letivos de uma instituição
	 * @param cdInstituicao
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getAllPeriodos(int cdInstituicao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_tipo_periodo " +
					"FROM acd_instituicao_periodo A " +
					"LEFT OUTER JOIN acd_tipo_periodo B ON (A.cd_tipo_periodo = B.cd_tipo_periodo) " +
					"WHERE cd_instituicao = "+cdInstituicao+
					" ORDER BY nm_periodo_letivo DESC");
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
	
	/**
	 * Retorna todos os períodos letivos de uma instituicao
	 * se nao houver periodos letivos proprios, serão retornados os períodos letivos da rede
	 * @param cdInstituicao
	 * @return periodos letivos
	 */
	public static ResultSetMap getAllPeriodosLetivos(int cdInstituicao) {
		return getAllPeriodosLetivos(cdInstituicao, null);
	}
	
	public static ResultSetMap getAllPeriodosLetivos(int cdInstituicao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			Pessoa instituicao = PessoaDAO.get(cdInstituicao, connection);
			if(instituicao == null)
				return new ResultSetMap();
					
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_tipo_periodo " +
					"FROM acd_instituicao_periodo A " +
					"LEFT OUTER JOIN acd_tipo_periodo B ON (A.cd_tipo_periodo = B.cd_tipo_periodo) " +
					"WHERE cd_instituicao = ? " + 
					"  AND A.cd_tipo_periodo = ? " +
					"  AND A.st_periodo_letivo IN (?, ?, ?) " +
					" ORDER BY nm_periodo_letivo DESC");
			
			pstmt.setInt(1, instituicao.getCdPessoa());
			pstmt.setInt(2, ParametroServices.getValorOfParametroAsInteger("CD_TIPO_PERIODO_LETIVO", 0));
			pstmt.setInt(3, InstituicaoPeriodoServices.ST_ATUAL);
			pstmt.setInt(4, InstituicaoPeriodoServices.ST_CONCLUIDO);
			pstmt.setInt(5, InstituicaoPeriodoServices.ST_PENDENTE);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("NM_PERIODO_LETIVO", rsm.getString("NM_PERIODO_LETIVO") + " - " + instituicao.getNmPessoa());
			}
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
	
	/**
	 * Retorna o período letivo vigente da instituicao
	 * se nao houver periodo letivo proprio, será retornado o período letivo da rede
	 * @param cdInstituicao
	 * @return periodos letivos
	 */
	public static ResultSetMap getPeriodoLetivoVigente(int cdInstituicao) {
		return getPeriodoLetivoVigente(cdInstituicao, null);
	}
	
	public static ResultSetMap getPeriodoLetivoVigente(int cdInstituicao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*" +
					" FROM acd_instituicao_periodo A" +
					" LEFT OUTER JOIN acd_tipo_periodo B ON (A.cd_tipo_periodo = B.cd_tipo_periodo)" +
					" WHERE cd_instituicao = ? " + 
					" AND A.cd_tipo_periodo = ? " +
					" AND A.st_periodo_letivo = " + InstituicaoPeriodoServices.ST_ATUAL);
			
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, ParametroServices.getValorOfParametroAsInteger("CD_TIPO_PERIODO_LETIVO", 0));
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			//se a instituição não possui períodos letivos próprios, procurar na instituição que representa os períodos da rede.
			if(rsm.size()==0) {
				rsm = getAllPeriodosLetivos(ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0), connection);
			}
			
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
	
	/**
	 * Busca o periodo recente de uma instituição
	 * @param cdInstituicao
	 * @return
	 */
	public static ResultSetMap getPeriodoLetivoRecente(int cdInstituicao) {
		return getPeriodoLetivoRecente(cdInstituicao, null);
	}
	
	public static ResultSetMap getPeriodoLetivoRecente(int cdInstituicao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.* " +
					" FROM acd_instituicao_periodo A" +
					" LEFT OUTER JOIN acd_tipo_periodo B ON (A.cd_tipo_periodo = B.cd_tipo_periodo)" +
					" WHERE cd_instituicao = ? " + 
					"   AND A.cd_tipo_periodo = ? " + 
					"  ORDER BY A.cd_periodo_letivo DESC");
			
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, ParametroServices.getValorOfParametroAsInteger("CD_TIPO_PERIODO_LETIVO", 0));
			
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			//se a instituição não possui períodos letivos próprios, procurar na instituição que representa os períodos da rede.
			if(rsm.size()==0) {
				rsm = getAllPeriodosLetivos(ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0), connection);
			}
			
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
	
	public static ResultSetMap getAllSalas(int cdInstituicao) {
		return getAllSalas(cdInstituicao, null);
	}
	
	public static ResultSetMap getAllSalas(int cdInstituicao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connection);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_tipo_dependencia " +
					"FROM acd_instituicao_dependencia A " +
					"LEFT OUTER JOIN acd_tipo_dependencia B ON (A.cd_tipo_dependencia = B.cd_tipo_dependencia) " +
					"WHERE cd_instituicao = ? " + 
					"  AND A.cd_tipo_dependencia = ? " +
					"  AND A.cd_periodo_letivo = ?" +
					" ORDER BY nm_dependencia");
			
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DEPENDENCIA_SALA", 0));
			pstmt.setInt(3, cdPeriodoLetivoAtual);
			
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
	
	public static ResultSetMap getAllGrupoEscolar(int cdInstituicao) {
		return getAllGrupoEscolar(cdInstituicao, null);
	}

	/**
	 * Busca todos os grupos escolares de uma instituição
	 * @param cdInstituicao
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getAllGrupoEscolar(int cdInstituicao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_grupo as nm_tipo_grupo " +
					"FROM acd_instituicao_grupo A " +
					"LEFT OUTER JOIN acd_tipo_grupo_escolar B ON (A.cd_tipo_grupo = B.cd_tipo_grupo) " +
					"WHERE A.cd_instituicao = "+cdInstituicao+
					" ORDER BY dt_criacao");
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
	

	public static ResultSetMap getAllDependencias(int cdInstituicao) {
		return getAllDependencias(cdInstituicao, 0, null);
	}
	
	public static ResultSetMap getAllDependencias(int cdInstituicao, Connection connection) {
		return getAllDependencias(cdInstituicao, 0, connection);
	}
	
	public static ResultSetMap getAllDependencias(int cdInstituicao, int cdPeriodoLetivo) {
		return getAllDependencias(cdInstituicao, cdPeriodoLetivo, null);
	}

	/**
	 * Busca todas as dependencias de uma instituição
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getAllDependencias(int cdInstituicao, int cdPeriodoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connection);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_tipo_dependencia " +
					"FROM acd_instituicao_dependencia A " +
					"LEFT OUTER JOIN acd_tipo_dependencia B ON (A.cd_tipo_dependencia = B.cd_tipo_dependencia) " +
					"WHERE A.cd_instituicao = "+cdInstituicao+
					"  AND A.cd_periodo_letivo = " +cdPeriodoLetivo+
					"  AND B.st_tipo_dependencia = " + TipoDependenciaServices.ST_ATIVADO +
					" ORDER BY B.nm_tipo_dependencia, A.nm_dependencia");
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
	

	/**
	 * Busca todos os recursos de acessibilidade de uma instituição
	 * @param cdInstituicao
	 * @return
	 */
	public static ResultSetMap getAllRecursosAcessibilidade(int cdInstituicao) {
		return getAllRecursosAcessibilidade(cdInstituicao, 0, null);
	}
	
	public static ResultSetMap getAllRecursosAcessibilidade(int cdInstituicao, Connection connection) {
		return getAllRecursosAcessibilidade(cdInstituicao, 0, connection);
	}
	
	public static ResultSetMap getAllRecursosAcessibilidade(int cdInstituicao, int cdPeriodoLetivo) {
		return getAllRecursosAcessibilidade(cdInstituicao, cdPeriodoLetivo, null);
	}

	public static ResultSetMap getAllRecursosAcessibilidade(int cdInstituicao, int cdPeriodoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connection);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_tipo_recurso_acessibilidade " +
					"FROM acd_instituicao_recurso_acessibilidade A " +
					"LEFT OUTER JOIN acd_tipo_recurso_acessibilidade B ON (A.cd_tipo_recurso_acessibilidade = B.cd_tipo_recurso_acessibilidade) " +
					"WHERE A.cd_instituicao = "+cdInstituicao+
					"  AND A.cd_periodo_letivo = " +cdPeriodoLetivo+
					" ORDER BY B.nm_tipo_recurso_acessibilidade");
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
	
	public static ResultSetMap getAllAcessoInternet(int cdInstituicao) {
		return getAllAcessoInternet(cdInstituicao, 0, null);
	}
	
	public static ResultSetMap getAllAcessoInternet(int cdInstituicao, Connection connection) {
		return getAllAcessoInternet(cdInstituicao, 0, connection);
	}
	
	public static ResultSetMap getAllAcessoInternet(int cdInstituicao, int cdPeriodoLetivo) {
		return getAllAcessoInternet(cdInstituicao, cdPeriodoLetivo, null);
	}

	/**
	 * Busca todos os tipo de acesso a internet de uma instituição
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getAllAcessoInternet(int cdInstituicao, int cdPeriodoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connection);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_tipo_acesso_internet " +
					"FROM acd_instituicao_acesso_internet A " +
					"LEFT OUTER JOIN acd_tipo_acesso_internet B ON (A.cd_tipo_acesso_internet = B.cd_tipo_acesso_internet) " +
					"WHERE A.cd_instituicao = "+cdInstituicao+
					"  AND A.cd_periodo_letivo = " +cdPeriodoLetivo+
					" ORDER BY B.nm_tipo_acesso_internet");
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
	
	public static ResultSetMap getAllEquipamentoAcessoInternetAluno(int cdInstituicao) {
		return getAllEquipamentoAcessoInternetAluno(cdInstituicao, 0, null);
	}
	
	public static ResultSetMap getAllEquipamentoAcessoInternetAluno(int cdInstituicao, Connection connection) {
		return getAllEquipamentoAcessoInternetAluno(cdInstituicao, 0, connection);
	}
	
	public static ResultSetMap getAllEquipamentoAcessoInternetAluno(int cdInstituicao, int cdPeriodoLetivo) {
		return getAllEquipamentoAcessoInternetAluno(cdInstituicao, cdPeriodoLetivo, null);
	}

	/**
	 * Busca todos os tipos de equipamento para acesso a internet usadas pelo aluno de uma instituição
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getAllEquipamentoAcessoInternetAluno(int cdInstituicao, int cdPeriodoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connection);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_tipo_equipamento_acesso_internet_aluno " +
					"FROM acd_instituicao_equipamento_acesso_internet_aluno A " +
					"LEFT OUTER JOIN acd_tipo_equipamento_acesso_internet_aluno B ON (A.cd_tipo_equipamento_acesso_internet_aluno = B.cd_tipo_equipamento_acesso_internet_aluno) " +
					"WHERE A.cd_instituicao = "+cdInstituicao+
					"  AND A.cd_periodo_letivo = " +cdPeriodoLetivo+
					" ORDER BY B.nm_tipo_equipamento_acesso_internet_aluno");
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
	
	public static ResultSetMap getAllRecursoInterligacaoComputadores(int cdInstituicao) {
		return getAllRecursoInterligacaoComputadores(cdInstituicao, 0, null);
	}
	
	public static ResultSetMap getAllRecursoInterligacaoComputadores(int cdInstituicao, Connection connection) {
		return getAllRecursoInterligacaoComputadores(cdInstituicao, 0, connection);
	}
	
	public static ResultSetMap getAllRecursoInterligacaoComputadores(int cdInstituicao, int cdPeriodoLetivo) {
		return getAllRecursoInterligacaoComputadores(cdInstituicao, cdPeriodoLetivo, null);
	}

	/**
	 * Busca todos os recursos de interligação de computadores de uma instituição
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getAllRecursoInterligacaoComputadores(int cdInstituicao, int cdPeriodoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connection);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_tipo_recurso_interligacao_computadores " +
					"FROM acd_instituicao_recurso_interligacao_computadores A " +
					"LEFT OUTER JOIN acd_tipo_recurso_interligacao_computadores B ON (A.cd_tipo_recurso_interligacao_computadores = B.cd_tipo_recurso_interligacao_computadores) " +
					"WHERE A.cd_instituicao = "+cdInstituicao+
					"  AND A.cd_periodo_letivo = " +cdPeriodoLetivo+
					" ORDER BY B.nm_tipo_recurso_interligacao_computadores");
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
	
	public static ResultSetMap getAllProfissionaisEscolares(int cdInstituicao) {
		return getAllProfissionaisEscolares(cdInstituicao, 0, null);
	}
	
	public static ResultSetMap getAllProfissionaisEscolares(int cdInstituicao, Connection connection) {
		return getAllProfissionaisEscolares(cdInstituicao, 0, connection);
	}
	
	public static ResultSetMap getAllProfissionaisEscolares(int cdInstituicao, int cdPeriodoLetivo) {
		return getAllProfissionaisEscolares(cdInstituicao, cdPeriodoLetivo, null);
	}

	/**
	 * Busca todos os tipos de profissionais escolares de uma instituição
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getAllProfissionaisEscolares(int cdInstituicao, int cdPeriodoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connection);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_tipo_profissionais_escolares " +
					"FROM acd_instituicao_profissionais_escolares A " +
					"LEFT OUTER JOIN acd_tipo_profissionais_escolares B ON (A.cd_tipo_profissionais_escolares = B.cd_tipo_profissionais_escolares) " +
					"WHERE A.cd_instituicao = "+cdInstituicao+
					"  AND A.cd_periodo_letivo = " +cdPeriodoLetivo+
					" ORDER BY B.nm_tipo_profissionais_escolares");
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
	
	public static ResultSetMap getAllInstrumentosPedagogicos(int cdInstituicao) {
		return getAllInstrumentosPedagogicos(cdInstituicao, 0, null);
	}
	
	public static ResultSetMap getAllInstrumentosPedagogicos(int cdInstituicao, Connection connection) {
		return getAllInstrumentosPedagogicos(cdInstituicao, 0, connection);
	}
	
	public static ResultSetMap getAllInstrumentosPedagogicos(int cdInstituicao, int cdPeriodoLetivo) {
		return getAllInstrumentosPedagogicos(cdInstituicao, cdPeriodoLetivo, null);
	}

	/**
	 * Busca todos os tipos de instrumentos pedagogicos de uma instituição
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getAllInstrumentosPedagogicos(int cdInstituicao, int cdPeriodoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connection);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_tipo_instrumentos_pedagogicos " +
					"FROM acd_instituicao_instrumentos_pedagogicos A " +
					"LEFT OUTER JOIN acd_tipo_instrumentos_pedagogicos B ON (A.cd_tipo_instrumentos_pedagogicos = B.cd_tipo_instrumentos_pedagogicos) " +
					"WHERE A.cd_instituicao = "+cdInstituicao+
					"  AND A.cd_periodo_letivo = " +cdPeriodoLetivo+
					" ORDER BY B.nm_tipo_instrumentos_pedagogicos");
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
	
	public static ResultSetMap getAllGruposEscolares(int cdInstituicao) {
		return getAllGruposEscolares(cdInstituicao, 0, null);
	}
	
	public static ResultSetMap getAllGruposEscolares(int cdInstituicao, Connection connection) {
		return getAllGruposEscolares(cdInstituicao, 0, connection);
	}
	
	public static ResultSetMap getAllGruposEscolares(int cdInstituicao, int cdPeriodoLetivo) {
		return getAllGruposEscolares(cdInstituicao, cdPeriodoLetivo, null);
	}

	/**
	 * Busca todos os grupos escolares de uma instituição
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getAllGruposEscolares(int cdInstituicao, int cdPeriodoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connection);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_tipo_grupos_escolares " +
					"FROM acd_instituicao_grupos_escolares A " +
					"LEFT OUTER JOIN acd_tipo_grupos_escolares B ON (A.cd_tipo_grupos_escolares = B.cd_tipo_grupos_escolares) " +
					"WHERE A.cd_instituicao = "+cdInstituicao+
					"  AND A.cd_periodo_letivo = " +cdPeriodoLetivo+
					" ORDER BY B.nm_tipo_grupos_escolares");
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
	
	
	
	
	

	public static Result addCursoToInstituicao(int cdInstituicao, int cdCurso, int cdPeriodoLetivo, int cdUsuario){
		return addCursoToInstituicao(cdInstituicao, cdCurso, cdPeriodoLetivo, cdUsuario, null);
	}

	/**
	 * Adiciona um curso à uma instituição, permitindo que seja criadas turmas deste curso
	 * @param cdInstituicao
	 * @param cdCurso
	 * @param cdPeriodoLetivo
	 * @param cdUsuario
	 * @param connect
	 * @return
	 */
	public static Result addCursoToInstituicao(int cdInstituicao, int cdCurso, int cdPeriodoLetivo, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Escolas que estao no modo offline nao podem adicionar cursos");
			}
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			/*
			 * Verifica se o curso já foi adicionado à instituição
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_curso " +
					                                "WHERE cd_instituicao = "+cdInstituicao+" AND cd_curso = "+cdCurso+" AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery();
			/*
			 * Inclui o curso
			 */
			if(!rs.next())	{
				
				Result result = InstituicaoCursoServices.save(new InstituicaoCurso(cdInstituicao, cdCurso, cdPeriodoLetivo), cdUsuario, connect);
				if(result.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
				
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o curso na instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result dropCursoFromInstituicao(int cdInstituicao, int cdCurso, int cdPeriodoLetivo, int cdUsuario){
		return dropCursoFromInstituicao(cdInstituicao, cdCurso, cdPeriodoLetivo, cdUsuario, null);
	}

	/**
	 * Retira um curso de uma instituição, impedindo que possa ser criadas turmas desses cursos
	 * @param cdInstituicao
	 * @param cdCurso
	 * @param cdPeriodoLetivo
	 * @param cdUsuario
	 * @param connect
	 * @return
	 */
	public static Result dropCursoFromInstituicao(int cdInstituicao, int cdCurso, int cdPeriodoLetivo, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
	
			Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Escolas que estao no modo offline nao podem remover cursos");
			}
			
			Result result = InstituicaoCursoServices.remove(cdInstituicao, cdCurso, cdPeriodoLetivo, cdUsuario, connect);
			if(result.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}
			
			if(result.getCode()<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			result.setMessage((result.getCode()<=0)?"Erro ao excluir...":"Excluído com sucesso...");
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o curso da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getLocalizacao(int cdInstituicao) {
		return getLocalizacao(cdInstituicao, null);
	}

	/**
	 * Busca a geolocalização de uma instituição
	 * @param cdInstituicao
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getLocalizacao(int cdInstituicao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_localizacao, A.cd_pessoa, B.*, C.vl_latitude, C.vl_longitude, C.cd_ponto " +
					"FROM geo_localizacao A " +
					"JOIN geo_referencia B ON (A.cd_referencia = B.cd_referencia) " +
					"JOIN geo_ponto C ON (A.cd_referencia = C.cd_referencia) " +
					"WHERE A.cd_pessoa = ? AND tp_localizacao = ?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, LocalizacaoServices.TP_LOCALIZACAO_POSICAO);
			
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
	

	public static ResultSetMap getCursosByInstituicao(int cdInstituicao) {
		return getCursosByInstituicao(cdInstituicao, null);
	}

	/**
	 * Busca os cursos permitidos de uma instituição, no periodo letivo atual
	 * @param cdInstituicao
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getCursosByInstituicao(int cdInstituicao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connection);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.*, C.nm_produto_servico AS nm_curso " +
					"FROM acd_instituicao_curso A " +
					"JOIN acd_curso           B ON (A.cd_curso = B.cd_curso) " +
					"JOIN grl_produto_servico C ON (A.cd_curso = C.cd_produto_servico) " +
					"WHERE A.cd_instituicao = "+cdInstituicao+
					"  AND A.cd_periodo_letivo = " + cdPeriodoLetivoAtual+
					" ORDER BY nm_produto_servico");
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

	
	/**
	 * TIPO MANTENEDORA x INSTITUICAO
	 * @param cdInstituicao
	 * @param cdTipoMantenedora
	 * @return
	 */
	
	public static Result addTipoMantenedora(int cdInstituicao, int cdTipoMantenedora){
		return addTipoMantenedora(cdInstituicao, cdTipoMantenedora, 0, null);
	}
	
	public static Result addTipoMantenedora(int cdInstituicao, int cdTipoMantenedora, Connection connect){
		return addTipoMantenedora(cdInstituicao, cdTipoMantenedora, 0, connect);
	}
	
	public static Result addTipoMantenedora(int cdInstituicao, int cdTipoMantenedora, int cdPeriodoLetivo){
		return addTipoMantenedora(cdInstituicao, cdTipoMantenedora, cdPeriodoLetivo, null);
	}

	public static Result addTipoMantenedora(int cdInstituicao, int cdTipoMantenedora, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			/*
			 * Verifica se o tipo de mantenadora já foi adicionado à instituição
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_tipo_mantenedora " +
					                                " WHERE cd_instituicao = " + cdInstituicao + " AND cd_tipo_mantenedora = " + cdTipoMantenedora + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery();
			
			if(rs.next())
				return new Result(-1, "Este tipo de mantenedora ja foi informado para essa instituicao!");
			else 
				return new Result(connect.prepareStatement("INSERT INTO acd_instituicao_tipo_mantenedora(cd_instituicao, cd_tipo_mantenedora, cd_periodo_letivo) " +
                                                           "VALUES (" + cdInstituicao + " ," + cdTipoMantenedora + ", " + cdPeriodoLetivo + ")").executeUpdate());
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o tipo de mantenadora na instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result dropTipoMantenedora(int cdInstituicao, int cdTipoMantenedora){
		return dropTipoMantenedora(cdInstituicao, cdTipoMantenedora, 0, null);
	}
	
	public static Result dropTipoMantenedora(int cdInstituicao, int cdTipoMantenedora, Connection connect){
		return dropTipoMantenedora(cdInstituicao, cdTipoMantenedora, 0, connect);
	}
	
	public static Result dropTipoMantenedora(int cdInstituicao, int cdTipoMantenedora, int cdPeriodoLetivo){
		return dropTipoMantenedora(cdInstituicao, cdTipoMantenedora, cdPeriodoLetivo, null);
	}

	public static Result dropTipoMantenedora(int cdInstituicao, int cdTipoMantenedora, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_tipo_mantenedora" +
					                                   "WHERE cd_instituicao = "+cdInstituicao+" AND cd_tipo_mantenedora = "+cdTipoMantenedora+" AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o tipo de mantenadora da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result clearTipoMantenedora(int cdInstituicao, Connection connect){
		return clearTipoMantenedora(cdInstituicao, 0, connect);
	}
	
	public static Result clearTipoMantenedora(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_tipo_mantenedora WHERE cd_instituicao = "+cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o tipos de mantenadora da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getTipoMantenedora(int cdInstituicao)	{
		return getTipoMantenedora(cdInstituicao, 0);
	}
	
	public static ResultSetMap getTipoMantenedora(int cdInstituicao, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_tipo_mantenedora A " +
					                                         "JOIN acd_instituicao_tipo_mantenedora B ON (B.cd_tipo_mantenedora = A.cd_tipo_mantenedora " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = "+cdPeriodoLetivo+") " +
					                                         "ORDER BY cd_instituicao ASC, nm_tipo_mantenedora ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * LOCAL FUNCIONAMENTO x INSTITUICAO
	 * @param cdInstituicao
	 * @param cdLocalFuncionamento
	 * @return
	 */
	public static Result addLocalFuncionamento(int cdInstituicao, int cdLocalFuncionamento){
		return addLocalFuncionamento(cdInstituicao, cdLocalFuncionamento, 0, null);
	}

	public static Result addLocalFuncionamento(int cdInstituicao, int cdLocalFuncionamento, Connection connect){
		return addLocalFuncionamento(cdInstituicao, cdLocalFuncionamento, 0, connect);
	}
	
	public static Result addLocalFuncionamento(int cdInstituicao, int cdLocalFuncionamento, int cdPeriodoLetivo){
		return addLocalFuncionamento(cdInstituicao, cdLocalFuncionamento, cdPeriodoLetivo, null);
	}

	public static Result addLocalFuncionamento(int cdInstituicao, int cdLocalFuncionamento, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			/*
			 * Verifica se o local de funcionamento já foi adicionado à instituição
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_local_funcionamento " +
					                                "WHERE cd_instituicao = " + cdInstituicao + " AND cd_local_funcionamento = " + cdLocalFuncionamento + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery();
			if(rs.next())
				return new Result(-1, "Este local de funcionamento já foi informado para essa instituição!");
			else 
				return new Result(connect.prepareStatement("INSERT INTO acd_instituicao_local_funcionamento(cd_instituicao, cd_local_funcionamento, cd_periodo_letivo) " +
                                                           "VALUES (" + cdInstituicao + " ," + cdLocalFuncionamento + " ," + cdPeriodoLetivo + ")").executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o local de funcionamento na instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result dropLocalFuncionamento(int cdInstituicao, int cdLocalFuncionamento){
		return dropLocalFuncionamento(cdInstituicao, cdLocalFuncionamento, 0, null);
	}

	public static Result dropLocalFuncionamento(int cdInstituicao, int cdLocalFuncionamento, Connection connect){
		return dropLocalFuncionamento(cdInstituicao, cdLocalFuncionamento, 0, connect);
	}
	
	public static Result dropLocalFuncionamento(int cdInstituicao, int cdLocalFuncionamento, int cdPeriodoLetivo){
		return dropLocalFuncionamento(cdInstituicao, cdLocalFuncionamento, cdPeriodoLetivo, null);
	}

	public static Result dropLocalFuncionamento(int cdInstituicao, int cdLocalFuncionamento, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){				
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_local_funcionamento " +
					                                   "WHERE cd_instituicao = "+cdInstituicao+" AND cd_local_funcionamento = "+cdLocalFuncionamento + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o local de funcionamento da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result clearLocalFuncionamento(int cdInstituicao, Connection connect){
		return clearLocalFuncionamento(cdInstituicao, 0, connect);
	}
	
	public static Result clearLocalFuncionamento(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_local_funcionamento WHERE cd_instituicao = "+cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o locais de funcionamento da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getLocalFuncionamento(int cdInstituicao)	{
		return getLocalFuncionamento(cdInstituicao, 0);
	}
	
	public static ResultSetMap getLocalFuncionamento(int cdInstituicao, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_local_funcionamento A " +
					                                         "JOIN acd_instituicao_local_funcionamento B ON (B.cd_local_funcionamento = A.cd_local_funcionamento " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = "+cdPeriodoLetivo+") " +
					                                         "ORDER BY cd_instituicao ASC, nm_local_funcionamento ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	
	/**
	 * ABASTECIMENTO AGUA x INSTITUICAO
	 * @param cdInstituicao
	 * @param cdAbastecimentoAgua
	 * @return
	 */
	public static Result addAbastecimentoAgua(int cdInstituicao, int cdAbastecimentoAgua){
		return addAbastecimentoAgua(cdInstituicao, cdAbastecimentoAgua, 0, null);
	}

	public static Result addAbastecimentoAgua(int cdInstituicao, int cdAbastecimentoAgua, Connection connect){
		return addAbastecimentoAgua(cdInstituicao, cdAbastecimentoAgua, 0, connect);
	}

	public static Result addAbastecimentoAgua(int cdInstituicao, int cdAbastecimentoAgua, int cdPeriodoLetivo){
		return addAbastecimentoAgua(cdInstituicao, cdAbastecimentoAgua, cdPeriodoLetivo, null);
	}
	
	public static Result addAbastecimentoAgua(int cdInstituicao, int cdAbastecimentoAgua, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){				
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			/*
			 * Verifica se o abastecimento de agua já foi adicionado à instituição
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_abastecimento_agua " +
					                                "WHERE cd_instituicao = " + cdInstituicao + " AND cd_abastecimento_agua = " + cdAbastecimentoAgua + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery();
			if(rs.next())
				return new Result(-1, "Este abastecimento de agua já foi informado para essa instituição!");
			else 
				return new Result(connect.prepareStatement("INSERT INTO acd_instituicao_abastecimento_agua(cd_instituicao, cd_abastecimento_agua, cd_periodo_letivo) " +
                                                           "VALUES (" + cdInstituicao + " ," + cdAbastecimentoAgua + " ," + cdPeriodoLetivo + ")").executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o abastecimento de agua na instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result dropAbastecimentoAgua(int cdInstituicao, int cdAbastecimentoAgua){
		return dropAbastecimentoAgua(cdInstituicao, cdAbastecimentoAgua, 0, null);
	}

	public static Result dropAbastecimentoAgua(int cdInstituicao, int cdAbastecimentoAgua, Connection connect){
		return dropAbastecimentoAgua(cdInstituicao, cdAbastecimentoAgua, 0, connect);
	}
	
	public static Result dropAbastecimentoAgua(int cdInstituicao, int cdAbastecimentoAgua, int cdPeriodoLetivo){
		return dropAbastecimentoAgua(cdInstituicao, cdAbastecimentoAgua, cdPeriodoLetivo, null);
	}

	public static Result dropAbastecimentoAgua(int cdInstituicao, int cdAbastecimentoAgua, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_abastecimento_agua " +
					                                   "WHERE cd_instituicao = "+cdInstituicao+" AND cd_abastecimento_agua = "+cdAbastecimentoAgua + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o abastecimento de agua da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result clearAbastecimentoAgua(int cdInstituicao, Connection connect){
		return clearAbastecimentoAgua(cdInstituicao, 0, connect);
	}
	
	public static Result clearAbastecimentoAgua(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_abastecimento_agua WHERE cd_instituicao = "+cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o abastecimento de agua da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAbastecimentoAgua(int cdInstituicao)	{
		return getAbastecimentoAgua(cdInstituicao, 0);
	}
	
	public static ResultSetMap getAbastecimentoAgua(int cdInstituicao, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_abastecimento_agua A " +
					                                         "JOIN acd_instituicao_abastecimento_agua B ON (B.cd_abastecimento_agua = A.cd_abastecimento_agua " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+ 
					                                         "													AND B.cd_periodo_letivo = "+cdPeriodoLetivo+") " +
					                                         "ORDER BY cd_instituicao ASC, nm_abastecimento_agua ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * ABASTECIMENTO ENERGIA x INSTITUICAO
	 * @param cdInstituicao
	 * @param cdAbastecimentoEnergia
	 * @return
	 */
	public static Result addAbastecimentoEnergia(int cdInstituicao, int cdAbastecimentoEnergia){
		return addAbastecimentoEnergia(cdInstituicao, cdAbastecimentoEnergia, 0, null);
	}

	public static Result addAbastecimentoEnergia(int cdInstituicao, int cdAbastecimentoEnergia, Connection connect){
		return addAbastecimentoEnergia(cdInstituicao, cdAbastecimentoEnergia, 0, connect);
	}
	
	public static Result addAbastecimentoEnergia(int cdInstituicao, int cdAbastecimentoEnergia, int cdPeriodoLetivo){
		return addAbastecimentoEnergia(cdInstituicao, cdAbastecimentoEnergia, cdPeriodoLetivo, null);
	}

	public static Result addAbastecimentoEnergia(int cdInstituicao, int cdAbastecimentoEnergia, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			/*
			 * Verifica se o abastecimento de energia já foi adicionado à instituição
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_abastecimento_energia " +
					                                "WHERE cd_instituicao = " + cdInstituicao + " AND cd_abastecimento_energia = " + cdAbastecimentoEnergia + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery();
			if(rs.next())
				return new Result(-1, "Este abastecimento de energia já foi informado para essa instituição!");
			else 
				return new Result(connect.prepareStatement("INSERT INTO acd_instituicao_abastecimento_energia(cd_instituicao, cd_abastecimento_energia, cd_periodo_letivo) " +
                                                           "VALUES (" + cdInstituicao + " ," + cdAbastecimentoEnergia + " ," + cdPeriodoLetivo + ")").executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o abastecimento de energia na instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result dropAbastecimentoEnergia(int cdInstituicao, int cdAbastecimentoEnergia){
		return dropAbastecimentoEnergia(cdInstituicao, cdAbastecimentoEnergia, 0, null);
	}

	public static Result dropAbastecimentoEnergia(int cdInstituicao, int cdAbastecimentoEnergia, Connection connect){
		return dropAbastecimentoEnergia(cdInstituicao, cdAbastecimentoEnergia, 0, connect);
	}
	
	public static Result dropAbastecimentoEnergia(int cdInstituicao, int cdAbastecimentoEnergia, int cdPeriodoLetivo){
		return dropAbastecimentoEnergia(cdInstituicao, cdAbastecimentoEnergia, cdPeriodoLetivo, null);
	}

	public static Result dropAbastecimentoEnergia(int cdInstituicao, int cdAbastecimentoEnergia, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_abastecimento_energia " +
					                                   "WHERE cd_instituicao = "+cdInstituicao+" AND cd_abastecimento_energia = "+cdAbastecimentoEnergia+" AND cd_periodo_letivo = "+cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o abastecimento de energia da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result clearAbastecimentoEnergia(int cdInstituicao, Connection connect){
		return clearAbastecimentoEnergia(cdInstituicao, 0, connect);
	}
	
	public static Result clearAbastecimentoEnergia(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_abastecimento_energia WHERE cd_instituicao = "+cdInstituicao+" AND cd_periodo_letivo = "+cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o abastecimento de energia da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAbastecimentoEnergia(int cdInstituicao)	{
		return getAbastecimentoEnergia(cdInstituicao, 0);
	}
	
	public static ResultSetMap getAbastecimentoEnergia(int cdInstituicao, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_abastecimento_energia A " +
					                                         "JOIN acd_instituicao_abastecimento_energia B ON (B.cd_abastecimento_energia = A.cd_abastecimento_energia " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = "+cdPeriodoLetivo+") " +
					                                         "ORDER BY cd_instituicao ASC, nm_abastecimento_energia ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * ESGOTO SANITARIO x INSTITUICAO
	 * @param cdInstituicao
	 * @param cdEsgotoSanitario
	 * @return
	 */
	public static Result addEsgotoSanitario(int cdInstituicao, int cdEsgotoSanitario){
		return addEsgotoSanitario(cdInstituicao, cdEsgotoSanitario, 0, null);
	}

	public static Result addEsgotoSanitario(int cdInstituicao, int cdEsgotoSanitario, Connection connect){
		return addEsgotoSanitario(cdInstituicao, cdEsgotoSanitario, 0, connect);
	}
	
	public static Result addEsgotoSanitario(int cdInstituicao, int cdEsgotoSanitario, int cdPeriodoLetivo){
		return addEsgotoSanitario(cdInstituicao, cdEsgotoSanitario, cdPeriodoLetivo, null);
	}

	public static Result addEsgotoSanitario(int cdInstituicao, int cdEsgotoSanitario, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			/*
			 * Verifica se o esgoto sanitario já foi adicionado à instituição
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_esgoto_sanitario " +
					                                "WHERE cd_instituicao = " + cdInstituicao + " AND cd_esgoto_sanitario = " + cdEsgotoSanitario + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery();
			if(rs.next())
				return new Result(-1, "Este esgoto sanitario já foi informado para essa instituição!");
			else 
				return new Result(connect.prepareStatement("INSERT INTO acd_instituicao_esgoto_sanitario(cd_instituicao, cd_esgoto_sanitario, cd_periodo_letivo) " +
                                                           "VALUES (" + cdInstituicao + " ," + cdEsgotoSanitario + ", "+cdPeriodoLetivo+")").executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o esgoto sanitario na instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result dropEsgotoSanitario(int cdInstituicao, int cdEsgotoSanitario){
		return dropEsgotoSanitario(cdInstituicao, cdEsgotoSanitario, 0, null);
	}

	public static Result dropEsgotoSanitario(int cdInstituicao, int cdEsgotoSanitario, Connection connect){
		return dropEsgotoSanitario(cdInstituicao, cdEsgotoSanitario, 0, connect);
	}
	
	public static Result dropEsgotoSanitario(int cdInstituicao, int cdEsgotoSanitario, int cdPeriodoLetivo){
		return dropEsgotoSanitario(cdInstituicao, cdEsgotoSanitario, cdPeriodoLetivo, null);
	}

	public static Result dropEsgotoSanitario(int cdInstituicao, int cdEsgotoSanitario, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_esgoto_sanitario " +
					                                   "WHERE cd_instituicao = "+cdInstituicao+" AND cd_esgoto_sanitario = "+cdEsgotoSanitario + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o esgoto sanitario da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result clearEsgotoSanitario(int cdInstituicao, Connection connect){
		return clearEsgotoSanitario(cdInstituicao, 0, connect);
	}
	
	public static Result clearEsgotoSanitario(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_esgoto_sanitario WHERE cd_instituicao = "+cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o esgoto sanitario da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getEsgotoSanitario(int cdInstituicao)	{
		return getEsgotoSanitario(cdInstituicao, 0);
	}
	
	public static ResultSetMap getEsgotoSanitario(int cdInstituicao, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_esgoto_sanitario A " +
					                                         "JOIN acd_instituicao_esgoto_sanitario B ON (B.cd_esgoto_sanitario = A.cd_esgoto_sanitario " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = "+cdPeriodoLetivo+") " +
					                                         "ORDER BY cd_instituicao ASC, nm_esgoto_sanitario ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * DESTINACAO DE LIXO x INSTITUICAO
	 * @param cdInstituicao
	 * @param cdDestinacaoLixo
	 * @return
	 */
	public static Result addDestinacaoLixo(int cdInstituicao, int cdDestinacaoLixo){
		return addDestinacaoLixo(cdInstituicao, cdDestinacaoLixo, 0, null);
	}

	public static Result addDestinacaoLixo(int cdInstituicao, int cdDestinacaoLixo, Connection connect){
		return addDestinacaoLixo(cdInstituicao, cdDestinacaoLixo, 0, connect);
	}
	
	public static Result addDestinacaoLixo(int cdInstituicao, int cdDestinacaoLixo, int cdPeriodoLetivo){
		return addDestinacaoLixo(cdInstituicao, cdDestinacaoLixo, cdPeriodoLetivo, null);
	}

	public static Result addDestinacaoLixo(int cdInstituicao, int cdDestinacaoLixo, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			/*
			 * Verifica se o local de funcionamento já foi adicionado à instituição
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_destinacao_lixo " +
					                                "WHERE cd_instituicao = " + cdInstituicao + " AND cd_destinacao_lixo = " + cdDestinacaoLixo + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery();
			if(rs.next())
				return new Result(-1, "Esta destinação de lixo já foi informado para essa instituição!");
			else 
				return new Result(connect.prepareStatement("INSERT INTO acd_instituicao_destinacao_lixo(cd_instituicao, cd_destinacao_lixo, cd_periodo_letivo) " +
                                                           "VALUES (" + cdInstituicao + " ," + cdDestinacaoLixo + " ," + cdPeriodoLetivo + ")").executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir a destinação de lixo na instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result dropDestinacaoLixo(int cdInstituicao, int cdDestinacaoLixo){
		return dropDestinacaoLixo(cdInstituicao, cdDestinacaoLixo, 0, null);
	}

	public static Result dropDestinacaoLixo(int cdInstituicao, int cdDestinacaoLixo, Connection connect){
		return dropDestinacaoLixo(cdInstituicao, cdDestinacaoLixo, 0, connect);
	}
	
	public static Result dropDestinacaoLixo(int cdInstituicao, int cdDestinacaoLixo, int cdPeriodoLetivo){
		return dropDestinacaoLixo(cdInstituicao, cdDestinacaoLixo, cdPeriodoLetivo, null);
	}

	public static Result dropDestinacaoLixo(int cdInstituicao, int cdDestinacaoLixo, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_destinacao_lixo " +
					                                   "WHERE cd_instituicao = "+cdInstituicao+" AND cd_destinacao_lixo = "+cdDestinacaoLixo + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir a destinacao de lixo da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result clearDestinacaoLixo(int cdInstituicao, Connection connect){
		return clearDestinacaoLixo(cdInstituicao, 0, connect);
	}
	
	public static Result clearDestinacaoLixo(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_destinacao_lixo WHERE cd_instituicao = "+cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o destinacao de lixo da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getDestinacaoLixo(int cdInstituicao)	{
		return getDestinacaoLixo(cdInstituicao, 0);
	}
	
	public static ResultSetMap getDestinacaoLixo(int cdInstituicao, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_destinacao_lixo A " +
					                                         "JOIN acd_instituicao_destinacao_lixo B ON (B.cd_destinacao_lixo = A.cd_destinacao_lixo " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = "+cdPeriodoLetivo+") " +
					                                         "ORDER BY cd_instituicao ASC, nm_destinacao_lixo ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * TIPO DE EQUIPAMENTO x INSTITUICAO
	 * @param cdInstituicao
	 * @param cdTipoDependencia
	 * @return
	 */
	public static Result addTipoEquipamento(int cdInstituicao, int cdTipoEquipamento){
		return addTipoEquipamento(cdInstituicao, cdTipoEquipamento, null);
	}

	public static Result addTipoEquipamento(int cdInstituicao, int cdTipoEquipamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
			int cdPeriodoLetivoAtual = 0;
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			/*
			 * Verifica se o TIPO DE EQUIPAMENTO já foi adicionado à instituição
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_tipo_equipamento  " +
					                                "WHERE cd_instituicao = " + cdInstituicao + " AND cd_tipo_equipamento = " + cdTipoEquipamento + " AND cd_periodo_letivo = " + cdPeriodoLetivoAtual).executeQuery();
			if(rs.next())
				return new Result(-1, "Este tipo de equipamento já foi informado para essa instituicao!");
			else 
				return new Result(connect.prepareStatement("INSERT INTO acd_instituicao_tipo_equipamento (cd_instituicao, cd_tipo_equipamento, cd_periodo_letivo) " +
                                                           "VALUES (" + cdInstituicao + " ," + cdTipoEquipamento + " ," + cdPeriodoLetivoAtual + ")").executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o tipo de equipamento na instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result dropTipoEquipamento(int cdInstituicao, int cdTipoEquipamento){
		return dropTipoEquipamento(cdInstituicao, cdTipoEquipamento, null);
	}

	public static Result dropTipoEquipamento(int cdInstituicao, int cdTipoEquipamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
			int cdPeriodoLetivoAtual = 0;
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_tipo_equipamento " +
					                                   "WHERE cd_instituicao = "+cdInstituicao+" AND cd_tipo_equipamento = "+cdTipoEquipamento+" AND cd_periodo_letivo = " + cdPeriodoLetivoAtual).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o tipo de equipamento da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result clearTipoEquipamento(int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
			int cdPeriodoLetivoAtual = 0;
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_tipo_equipamento WHERE cd_instituicao = "+cdInstituicao+" AND cd_periodo_letivo = " + cdPeriodoLetivoAtual).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o tipos de equipamento da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getTipoEquipamento(int cdInstituicao)	{
		return getTipoEquipamento(cdInstituicao, 0);
	}
	
	public static ResultSetMap getTipoEquipamento(int cdInstituicao, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao, B.qt_equipamento FROM acd_tipo_equipamento A " +
					                                         "JOIN acd_instituicao_tipo_equipamento  B ON (B.cd_tipo_equipamento = A.cd_tipo_equipamento " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = "+cdPeriodoLetivo+") " +
					                                         "ORDER BY cd_instituicao ASC, nm_tipo_equipamento ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getTipoProfissionaisEscolares(int cdInstituicao)	{
		return getTipoProfissionaisEscolares(cdInstituicao, 0);
	}
	
	public static ResultSetMap getTipoProfissionaisEscolares(int cdInstituicao, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao, B.qt_profissionais_escolares FROM acd_tipo_profissionais_escolares A " +
					                                         "JOIN acd_instituicao_profissionais_escolares  B ON (B.cd_tipo_profissionais_escolares = A.cd_tipo_profissionais_escolares " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = "+cdPeriodoLetivo+") " +
					                                         "ORDER BY cd_instituicao ASC, nm_tipo_profissionais_escolares ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * TIPO ETAPA x INSTITUICAO
	 * @param cdInstituicao
	 * @param cdTipoMantenedora
	 * @return
	 */
	public static Result addTipoEtapa(int cdInstituicao, int cdEtapa){
		return addTipoEtapa(cdInstituicao, cdEtapa, 0, null);
	}
	
	public static Result addTipoEtapa(int cdInstituicao, int cdEtapa, Connection connect){
		return addTipoEtapa(cdInstituicao, cdEtapa, 0, connect);
	}
	
	public static Result addTipoEtapa(int cdInstituicao, int cdEtapa, int cdPeriodoLetivo){
		return addTipoEtapa(cdInstituicao, cdEtapa, cdPeriodoLetivo, null);
	}

	public static Result addTipoEtapa(int cdInstituicao, int cdEtapa, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			/*
			 * Verifica se o tipo de mantenadora já foi adicionado à instituição
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_tipo_etapa " +
					                                "WHERE cd_instituicao = " + cdInstituicao + " AND cd_etapa = " + cdEtapa + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery();
			
			if(rs.next())
				return new Result(-1, "Este tipo de etapa já foi informado para essa instituição!");
			else 
				return new Result(connect.prepareStatement("INSERT INTO acd_instituicao_tipo_etapa (cd_instituicao, cd_etapa, cd_periodo_letivo) " +
                                                           "VALUES (" + cdInstituicao + " ," + cdEtapa + ", "+cdPeriodoLetivo+")").executeUpdate());
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o tipo de etapa na instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result dropTipoEtapa(int cdInstituicao, int cdEtapa){
		return dropTipoEtapa(cdInstituicao, cdEtapa, 0, null);
	}
	
	public static Result dropTipoEtapa(int cdInstituicao, int cdEtapa, Connection connect){
		return dropTipoEtapa(cdInstituicao, cdEtapa, 0, connect);
	}
	
	public static Result dropTipoEtapa(int cdInstituicao, int cdEtapa, int cdPeriodoLetivo){
		return dropTipoEtapa(cdInstituicao, cdEtapa, cdPeriodoLetivo, null);
	}

	public static Result dropTipoEtapa(int cdInstituicao, int cdEtapa, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_tipo_etapa " +
					                                   "WHERE cd_instituicao = "+cdInstituicao+" AND cd_etapa = "+cdEtapa+" AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o tipo de etapa da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result clearTipoEtapa(int cdInstituicao, Connection connect){
		return clearTipoEtapa(cdInstituicao, 0, connect);
	}
	
	public static Result clearTipoEtapa(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_tipo_etapa WHERE cd_instituicao = "+cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o tipos de etapa da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getTipoEtapa(int cdInstituicao)	{
		return getTipoEtapa(cdInstituicao, 0);
	}
	
	public static ResultSetMap getTipoEtapa(int cdInstituicao, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_tipo_etapa A " +
					                                         "JOIN acd_instituicao_tipo_etapa B ON (B.cd_etapa = A.cd_etapa " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = " +cdPeriodoLetivo+") " +
					                                         "ORDER BY A.cd_etapa ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * RECURSO ACESSIBILIDADE x INSTITUICAO
	 * @param cdInstituicao
	 * @param cdTipoMantenedora
	 * @return
	 */
	public static Result addRecursoAcessibilidade(int cdInstituicao, int cdTipoRecursoAcessibilidade){
		return addRecursoAcessibilidade(cdInstituicao, cdTipoRecursoAcessibilidade, 0, null);
	}
	
	public static Result addRecursoAcessibilidade(int cdInstituicao, int cdTipoRecursoAcessibilidade, Connection connect){
		return addRecursoAcessibilidade(cdInstituicao, cdTipoRecursoAcessibilidade, 0, connect);
	}

	public static Result addRecursoAcessibilidade(int cdInstituicao, int cdTipoRecursoAcessibilidade, int cdPeriodoLetivo){
		return addRecursoAcessibilidade(cdInstituicao, cdTipoRecursoAcessibilidade, cdPeriodoLetivo, null);
	}

	public static Result addRecursoAcessibilidade(int cdInstituicao, int cdTipoRecursoAcessibilidade, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			/*
			 * Verifica se o tipo de mantenadora já foi adicionado à instituição
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_recurso_acessibilidade " +
					                                "WHERE cd_instituicao = " + cdInstituicao + " AND cd_tipo_recurso_acessibilidade = " + cdTipoRecursoAcessibilidade + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery();
			
			if(rs.next())
				return new Result(-1, "Este recurso de acessibilidade já foi informado para essa instituição!");
			else 
				return new Result(connect.prepareStatement("INSERT INTO acd_instituicao_recurso_acessibilidade (cd_instituicao, cd_tipo_recurso_acessibilidade, cd_periodo_letivo) " +
                                                           "VALUES (" + cdInstituicao + " ," + cdTipoRecursoAcessibilidade + ", "+cdPeriodoLetivo+")").executeUpdate());
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o recurso de acessibilidade na instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result clearRecursoAcessibilidade(int cdInstituicao, Connection connect){
		return clearRecursoAcessibilidade(cdInstituicao, 0, connect);
	}
	
	public static Result clearRecursoAcessibilidade(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_recurso_acessibilidade WHERE cd_instituicao = "+cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o recurso de acessibilidade da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getRecursoAcessibilidade(int cdInstituicao)	{
		return getRecursoAcessibilidade(cdInstituicao, 0);
	}
	
	public static ResultSetMap getRecursoAcessibilidade(int cdInstituicao, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_tipo_recurso_acessibilidade A " +
					                                         "JOIN acd_instituicao_recurso_acessibilidade B ON (B.cd_tipo_recurso_acessibilidade = A.cd_tipo_recurso_acessibilidade " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = " +cdPeriodoLetivo+") " +
					                                         "ORDER BY A.cd_tipo_recurso_acessibilidade ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * ACESSO INTERNET x INSTITUICAO
	 * @param cdInstituicao
	 * @param cdTipoMantenedora
	 * @return
	 */
	public static Result addAcessoInternet(int cdInstituicao, int cdTipoAcessoInternet){
		return addAcessoInternet(cdInstituicao, cdTipoAcessoInternet, 0, null);
	}
	
	public static Result addAcessoInternet(int cdInstituicao, int cdTipoAcessoInternet, Connection connect){
		return addAcessoInternet(cdInstituicao, cdTipoAcessoInternet, 0, connect);
	}


	public static Result addAcessoInternet(int cdInstituicao, int cdTipoAcessoInternet, int cdPeriodoLetivo){
		return addAcessoInternet(cdInstituicao, cdTipoAcessoInternet, cdPeriodoLetivo, null);
	}

	public static Result addAcessoInternet(int cdInstituicao, int cdTipoAcessoInternet, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			/*
			 * Verifica se o tipo de mantenadora já foi adicionado à instituição
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_acesso_internet " +
					                                "WHERE cd_instituicao = " + cdInstituicao + " AND cd_tipo_acesso_internet = " + cdTipoAcessoInternet + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery();
			
			if(rs.next())
				return new Result(-1, "Este acesso a internet já foi informado para essa instituição!");
			else 
				return new Result(connect.prepareStatement("INSERT INTO acd_instituicao_acesso_internet (cd_instituicao, cd_tipo_acesso_internet, cd_periodo_letivo) " +
                                                           "VALUES (" + cdInstituicao + " ," + cdTipoAcessoInternet + ", "+cdPeriodoLetivo+")").executeUpdate());
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o acesso a internet na instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result clearAcessoInternet(int cdInstituicao, Connection connect){
		return clearAcessoInternet(cdInstituicao, 0, connect);
	}
	
	public static Result clearAcessoInternet(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_acesso_internet WHERE cd_instituicao = "+cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o tipos de etapa da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	public static ResultSetMap getAcessoInternet(int cdInstituicao)	{
		return getAcessoInternet(cdInstituicao, 0);
	}
	
	public static ResultSetMap getAcessoInternet(int cdInstituicao, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_tipo_acesso_internet A " +
					                                         "JOIN acd_instituicao_acesso_internet B ON (B.cd_tipo_acesso_internet = A.cd_tipo_acesso_internet " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = " +cdPeriodoLetivo+") " +
					                                         "ORDER BY A.cd_tipo_acesso_internet ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/**
	 * EQUIPAMENTO ACESSO INTERNET ALUNO x INSTITUICAO
	 * @param cdInstituicao
	 * @param cdTipoMantenedora
	 * @return
	 */
	public static Result addEquipamentoAcessoInternetAluno(int cdInstituicao, int cdTipoAcessoInternetAluno){
		return addEquipamentoAcessoInternetAluno(cdInstituicao, cdTipoAcessoInternetAluno, 0, null);
	}
	
	public static Result addEquipamentoAcessoInternetAluno(int cdInstituicao, int cdTipoAcessoInternetAluno, Connection connect){
		return addEquipamentoAcessoInternetAluno(cdInstituicao, cdTipoAcessoInternetAluno, 0, connect);
	}

	public static Result addEquipamentoAcessoInternetAluno(int cdInstituicao, int cdTipoAcessoInternetAluno, int cdPeriodoLetivo){
		return addEquipamentoAcessoInternetAluno(cdInstituicao, cdTipoAcessoInternetAluno, cdPeriodoLetivo, null);
	}

	public static Result addEquipamentoAcessoInternetAluno(int cdInstituicao, int cdTipoAcessoInternetAluno, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			/*
			 * Verifica se o tipo de mantenadora já foi adicionado à instituição
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_equipamento_acesso_internet_aluno " +
					                                "WHERE cd_instituicao = " + cdInstituicao + " AND cd_tipo_equipamento_acesso_internet_aluno = " + cdTipoAcessoInternetAluno + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery();
			
			if(rs.next())
				return new Result(-1, "Este tipo de etapa já foi informado para essa instituição!");
			else 
				return new Result(connect.prepareStatement("INSERT INTO acd_instituicao_equipamento_acesso_internet_aluno (cd_instituicao, cd_tipo_equipamento_acesso_internet_aluno, cd_periodo_letivo) " +
                                                           "VALUES (" + cdInstituicao + " ," + cdTipoAcessoInternetAluno + ", "+cdPeriodoLetivo+")").executeUpdate());
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o tipo de etapa na instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result clearEquipamentoAcessoInternetAluno(int cdInstituicao, Connection connect){
		return clearEquipamentoAcessoInternetAluno(cdInstituicao, 0, connect);
	}
	
	public static Result clearEquipamentoAcessoInternetAluno(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_equipamento_acesso_internet_aluno WHERE cd_instituicao = "+cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o tipos de etapa da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getEquipamentoAcessoInternetAluno(int cdInstituicao)	{
		return getEquipamentoAcessoInternetAluno(cdInstituicao, 0);
	}
	
	public static ResultSetMap getEquipamentoAcessoInternetAluno(int cdInstituicao, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_tipo_equipamento_acesso_internet_aluno A " +
					                                         "JOIN acd_instituicao_equipamento_acesso_internet_aluno B ON (B.cd_tipo_equipamento_acesso_internet_aluno = A.cd_tipo_equipamento_acesso_internet_aluno " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = " +cdPeriodoLetivo+") " +
					                                         "ORDER BY A.cd_tipo_equipamento_acesso_internet_aluno ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/**
	 * RECURSO INTERLIGACAO COMPUTADORES x INSTITUICAO
	 * @param cdInstituicao
	 * @param cdTipoMantenedora
	 * @return
	 */
	public static Result addRecursoInterligacaoComputadores(int cdInstituicao, int cdTipoRecursoInterligacaoComptuadores){
		return addRecursoInterligacaoComputadores(cdInstituicao, cdTipoRecursoInterligacaoComptuadores, 0, null);
	}
	
	public static Result addRecursoInterligacaoComputadores(int cdInstituicao, int cdTipoRecursoInterligacaoComptuadores, Connection connect){
		return addRecursoInterligacaoComputadores(cdInstituicao, cdTipoRecursoInterligacaoComptuadores, 0, connect);
	}

	public static Result addRecursoInterligacaoComputadores(int cdInstituicao, int cdTipoRecursoInterligacaoComptuadores, int cdPeriodoLetivo){
		return addRecursoInterligacaoComputadores(cdInstituicao, cdTipoRecursoInterligacaoComptuadores, cdPeriodoLetivo, null);
	}

	public static Result addRecursoInterligacaoComputadores(int cdInstituicao, int cdTipoRecursoInterligacaoComptuadores, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			/*
			 * Verifica se o tipo de mantenadora já foi adicionado à instituição
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_recurso_interligacao_computadores " +
					                                "WHERE cd_instituicao = " + cdInstituicao + " AND cd_tipo_recurso_interligacao_computadores = " + cdTipoRecursoInterligacaoComptuadores + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery();
			
			if(rs.next())
				return new Result(-1, "Este tipo de etapa já foi informado para essa instituição!");
			else 
				return new Result(connect.prepareStatement("INSERT INTO acd_instituicao_recurso_interligacao_computadores (cd_instituicao, cd_tipo_recurso_interligacao_computadores, cd_periodo_letivo) " +
                                                           "VALUES (" + cdInstituicao + " ," + cdTipoRecursoInterligacaoComptuadores + ", "+cdPeriodoLetivo+")").executeUpdate());
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o tipo de etapa na instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result clearRecursoInterligacaoComputadores(int cdInstituicao, Connection connect){
		return clearRecursoInterligacaoComputadores(cdInstituicao, 0, connect);
	}
	
	public static Result clearRecursoInterligacaoComputadores(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_recurso_interligacao_computadores WHERE cd_instituicao = "+cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o tipos de etapa da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getRecursoInterligacaoComputadores(int cdInstituicao)	{
		return getRecursoInterligacaoComputadores(cdInstituicao, 0);
	}
	
	public static ResultSetMap getRecursoInterligacaoComputadores(int cdInstituicao, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_tipo_recurso_interligacao_computadores A " +
					                                         "JOIN acd_instituicao_recurso_interligacao_computadores B ON (B.cd_tipo_recurso_interligacao_computadores = A.cd_tipo_recurso_interligacao_computadores " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = " +cdPeriodoLetivo+") " +
					                                         "ORDER BY A.cd_tipo_recurso_interligacao_computadores ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/**
	 * INSTRUMENTOS PEDAGOGICOS x INSTITUICAO
	 * @param cdInstituicao
	 * @param cdTipoMantenedora
	 * @return
	 */	
	public static Result addInstrumentosPedagogicos(int cdInstituicao, int cdTipoInstrumentosPedagogicos){
		return addInstrumentosPedagogicos(cdInstituicao, cdTipoInstrumentosPedagogicos, 0, null);
	}
	
	public static Result addInstrumentosPedagogicos(int cdInstituicao, int cdTipoInstrumentosPedagogicos, Connection connect){
		return addInstrumentosPedagogicos(cdInstituicao, cdTipoInstrumentosPedagogicos, 0, connect);
	}

	public static Result addInstrumentosPedagogicos(int cdInstituicao, int cdTipoInstrumentosPedagogicos, int cdPeriodoLetivo){
		return addInstrumentosPedagogicos(cdInstituicao, cdTipoInstrumentosPedagogicos, cdPeriodoLetivo, null);
	}

	public static Result addInstrumentosPedagogicos(int cdInstituicao, int cdTipoInstrumentosPedagogicos, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			/*
			 * Verifica se o tipo de mantenadora já foi adicionado à instituição
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_instrumentos_pedagogicos " +
					                                "WHERE cd_instituicao = " + cdInstituicao + " AND cd_tipo_instrumentos_pedagogicos = " + cdTipoInstrumentosPedagogicos + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery();
			
			if(rs.next())
				return new Result(-1, "Este tipo de etapa já foi informado para essa instituição!");
			else 
				return new Result(connect.prepareStatement("INSERT INTO acd_instituicao_instrumentos_pedagogicos (cd_instituicao, cd_tipo_instrumentos_pedagogicos, cd_periodo_letivo) " +
                                                           "VALUES (" + cdInstituicao + " ," + cdTipoInstrumentosPedagogicos + ", "+cdPeriodoLetivo+")").executeUpdate());
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o tipo de etapa na instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result clearInstrumentosPedagogicos(int cdInstituicao, Connection connect){
		return clearInstrumentosPedagogicos(cdInstituicao, 0, connect);
	}
	
	public static Result clearInstrumentosPedagogicos(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_instrumentos_pedagogicos WHERE cd_instituicao = "+cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o tipos de etapa da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getInstrumentosPedagogicos(int cdInstituicao)	{
		return getInstrumentosPedagogicos(cdInstituicao, 0);
	}
	
	public static ResultSetMap getInstrumentosPedagogicos(int cdInstituicao, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_tipo_instrumentos_pedagogicos A " +
					                                         "JOIN acd_instituicao_instrumentos_pedagogicos B ON (B.cd_tipo_instrumentos_pedagogicos = A.cd_tipo_instrumentos_pedagogicos " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = " +cdPeriodoLetivo+") " +
					                                         "ORDER BY A.cd_tipo_instrumentos_pedagogicos ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}


	/**
	 * GRUPOS ESCOLARES x INSTITUICAO
	 * @param cdInstituicao
	 * @param cdTipoMantenedora
	 * @return
	 */
	public static Result addGruposEscolares(int cdInstituicao, int cdTipoGruposEscolares){
		return addGruposEscolares(cdInstituicao, cdTipoGruposEscolares, 0, null);
	}
	
	public static Result addGruposEscolares(int cdInstituicao, int cdTipoGruposEscolares, Connection connect){
		return addGruposEscolares(cdInstituicao, cdTipoGruposEscolares, 0, connect);
	}
	
	public static Result addGruposEscolares(int cdInstituicao, int cdTipoGruposEscolares, int cdPeriodoLetivo){
		return addGruposEscolares(cdInstituicao, cdTipoGruposEscolares, cdPeriodoLetivo, null);
	}

	public static Result addGruposEscolares(int cdInstituicao, int cdTipoGruposEscolares, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			/*
			 * Verifica se o tipo de mantenadora já foi adicionado à instituição
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_grupos_escolares " +
					                                "WHERE cd_instituicao = " + cdInstituicao + " AND cd_tipo_grupos_escolares = " + cdTipoGruposEscolares + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery();
			
			if(rs.next())
				return new Result(-1, "Este tipo de etapa já foi informado para essa instituição!");
			else 
				return new Result(connect.prepareStatement("INSERT INTO acd_instituicao_grupos_escolares (cd_instituicao, cd_tipo_grupos_escolares, cd_periodo_letivo) " +
                                                           "VALUES (" + cdInstituicao + " ," + cdTipoGruposEscolares + ", "+cdPeriodoLetivo+")").executeUpdate());
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o tipo de etapa na instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result clearGruposEscolares(int cdInstituicao, Connection connect){
		return clearGruposEscolares(cdInstituicao, 0, connect);
	}
	
	public static Result clearGruposEscolares(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_grupos_escolares WHERE cd_instituicao = "+cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o tipos de etapa da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getGruposEscolares(int cdInstituicao)	{
		return getGruposEscolares(cdInstituicao, 0);
	}
	
	public static ResultSetMap getGruposEscolares(int cdInstituicao, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_tipo_grupos_escolares A " +
					                                         "JOIN acd_instituicao_grupos_escolares B ON (B.cd_tipo_grupos_escolares = A.cd_tipo_grupos_escolares " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = " +cdPeriodoLetivo+") " +
					                                         "ORDER BY A.cd_tipo_grupos_escolares ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}




	/**
	 * INSTITUICOES(HIERARQUIA) x INSTITUICAO
	 * @param cdInstituicao
	 * @param cdInstituicaoVinculada
	 * @return
	 */
	public static Result addInstituicao(int cdInstituicao, int cdInstituicaoVinculada){
		return addInstituicao(cdInstituicao, cdInstituicaoVinculada, null);
	}

	public static Result addInstituicao(int cdInstituicao, int cdInstituicaoVinculada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			/*
			 * Verifica se a instituição já foi vinculada à instituição superior
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM grl_pessoa " +
					                                "WHERE cd_pessoa_superior = " + cdInstituicao + " AND cd_pessoa = " + cdInstituicaoVinculada).executeQuery();
			if(rs.next())
				return new Result(-1, "A instituição indicada já está vinculada a esta unidade!");
			
			
			
			int ret = connect.prepareStatement("UPDATE grl_pessoa SET cd_pessoa_superior =  " + cdInstituicao +
                    						   " WHERE cd_pessoa = " + cdInstituicaoVinculada).executeUpdate(); 
			if(ret < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar hierarquia");
			}
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + cdInstituicaoVinculada + " AND nm_periodo_letivo = ?");
			
			ResultSetMap rsmPeriodosInstituicao = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + cdInstituicao + " ORDER BY CAST(nm_periodo_letivo AS INTEGER)").executeQuery());
			while(rsmPeriodosInstituicao.next()){
				
				pstmt.setString(1, rsmPeriodosInstituicao.getString("nm_periodo_letivo"));
				ResultSetMap rsmPeriodoInstituicaoVinculada = new ResultSetMap(pstmt.executeQuery());
				if(rsmPeriodoInstituicaoVinculada.next()){
					InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmPeriodoInstituicaoVinculada.getInt("cd_periodo_letivo"), connect);
					instituicaoPeriodo.setCdPeriodoLetivoSuperior(rsmPeriodosInstituicao.getInt("cd_periodo_letivo"));
					if(InstituicaoPeriodoDAO.update(instituicaoPeriodo, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar periodos");
					}
				}
			}

			if(isConnectionNull)
				connect.commit();
			
			
			return new Result(1, "Sucesso ao atualizar");
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1,"Erro ao tentar vincular esta instituição a unidade!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result dropInstituicao(int cdInstituicaoVinculada){
		return dropInstituicao(cdInstituicaoVinculada, null);
	}

	public static Result dropInstituicao(int cdInstituicaoVinculada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa SET cd_pessoa_superior = ? " +
                    											" WHERE cd_pessoa = " + cdInstituicaoVinculada);
			pstmt.setInt(1, cdSecretaria);
			
			if(pstmt.executeUpdate()<=0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar instituicao superior");
			}
			
			
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + cdInstituicaoVinculada + " AND nm_periodo_letivo = ?");
			
			ResultSetMap rsmPeriodosInstituicao = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + cdSecretaria + " ORDER BY CAST(nm_periodo_letivo AS INTEGER)").executeQuery());
			while(rsmPeriodosInstituicao.next()){
				
				pstmt.setString(1, rsmPeriodosInstituicao.getString("nm_periodo_letivo"));
				ResultSetMap rsmPeriodoInstituicaoVinculada = new ResultSetMap(pstmt.executeQuery());
				if(rsmPeriodoInstituicaoVinculada.next()){
					InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmPeriodoInstituicaoVinculada.getInt("cd_periodo_letivo"), connect);
					instituicaoPeriodo.setCdPeriodoLetivoSuperior(rsmPeriodosInstituicao.getInt("cd_periodo_letivo"));
					if(InstituicaoPeriodoDAO.update(instituicaoPeriodo, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar periodos");
					}
				}
			}

			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Deletado com sucesso");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao desvincular a instituição indicada!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllInstituicoesVinculadas(int cdInstituicao) {
		return getAllInstituicoesVinculadas(cdInstituicao, null);
	}
			
	public static ResultSetMap getAllInstituicoesVinculadas(int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			int cdPeriodoAtual = 0;
			ResultSetMap rsmPeriodo = getPeriodoLetivoVigente(cdInstituicao, connect);
			if(rsmPeriodo.next())
				cdPeriodoAtual = rsmPeriodo.getInt("cd_periodo_letivo");
			
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.cd_pessoa, A.nm_pessoa, A.nm_pessoa AS NM_INSTITUICAO " + 
					 "FROM grl_pessoa A " + 
					 " JOIN acd_instituicao_educacenso B ON (A.cd_pessoa = B.cd_instituicao AND B.cd_periodo_letivo = "+cdPeriodoAtual+")"+
					 "WHERE A.cd_pessoa_superior = ? " +
					 "  AND B.st_instituicao_publica = " + ST_INSTITUICAO_EM_ATIVIDADE + 
                     " ORDER BY nm_pessoa ");
			pstmt.setInt(1, cdInstituicao);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			while(rsm.next()) {
				rsm.setValueToField("INSTITUICOES", getAllInstituicoesVinculadas(rsm.getInt("CD_PESSOA"), connect).getLines());
			}
			
			return rsm;
			
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static Result vincularTurmaExtensao(ResultSetMap rsmTurma, int cdExtensao){
		return vincularTurmaExtensao(rsmTurma, cdExtensao, null);
	}

	public static Result vincularTurmaExtensao(ResultSetMap rsmTurma, int cdExtensao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			connect.setAutoCommit(false);
			
			while(rsmTurma.next()){
				Turma turma = TurmaDAO.get(rsmTurma.getInt("CD_TURMA"), connect);
				
				if(turma==null){
					return new Result(0, "A turma " + rsmTurma.getInt("NM_CURSO") +  " "  + rsmTurma.getInt("NM_TURMA") + " informada não existe");
				}
				
				PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_turma SET cd_instituicao_ext = ? WHERE cd_turma = ?");
				pstmt.setInt(1, cdExtensao);
				pstmt.setInt(2, rsmTurma.getInt("CD_TURMA"));
				
				if(pstmt.executeUpdate() == 0){
					return new Result(0, "Nenhuma turma foi vinculada à extensão");					
				}
			}
			
			connect.commit();
			return new Result(1, "Todas as turmas selecionadas foram vinculadas a extensão com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir o tipo de etapa da instituição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getInstituicaoCompleto(int cdInstituicao){
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("B.cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		int buscarAtual = 0;
		int qtLimite              = 0;
		
		LogUtils.debug("InstituicaoServices.find");
		LogUtils.createTimer("INSTITUICAO_FIND_TIMER");
		
		for(int i=0; criterios!=null && i<criterios.size(); i++){
			if (criterios.get(i).getColumn().equalsIgnoreCase("buscarAtual")) {
				buscarAtual = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i--);
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
				qtLimite = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i--);
			}
			
		}
		
		int cdPeriodoSuperior = 0;
		ResultSetMap rsmPeriodo = (buscarAtual==1 ? InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0), connect) : InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0), connect));
		if(rsmPeriodo.next())
			cdPeriodoSuperior = rsmPeriodo.getInt("cd_periodo_letivo");
			
		String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
		
		String sql =   "SELECT "+sqlLimit[0]+" K.*, A.*, L.*, B.*, N.*, M.*, K.nm_razao_social AS nm_empresa, L.nm_pessoa AS nm_fantasia, " +
					   "	   L.cd_pessoa as cd_instituicao, L.nm_pessoa as nm_instituicao, UPPER(L.nm_pessoa),  " +
					   "       C.cd_pessoa AS cd_administrador, C.nm_pessoa AS nm_administrador, " +
					   "       D.nm_pessoa AS nm_coordenador, E.nm_pessoa AS nm_diretor, F.nm_pessoa AS nm_secretario, " +
				       "       G.nm_pessoa AS nm_tesoureiro, H.nm_pessoa AS nm_vice_diretor, I.nm_cidade, J.nm_estado, J.sg_estado, " + 
					   "       P.nm_pessoa AS nm_pessoa_superior " +
				       "FROM grl_pessoa_juridica K " +
				       "JOIN grl_empresa                A ON (A.cd_empresa = K.cd_pessoa) " +
				       "JOIN grl_pessoa                 L ON (L.cd_pessoa = K.cd_pessoa) " +
				       "JOIN acd_instituicao B ON (A.cd_empresa = B.cd_instituicao) " +
				       "LEFT OUTER JOIN acd_instituicao_periodo IP ON (K.cd_pessoa = IP.cd_instituicao AND IP.cd_periodo_letivo_superior = "+cdPeriodoSuperior+") " +
				       "LEFT OUTER JOIN acd_instituicao_educacenso N ON (N.cd_instituicao = K.cd_pessoa AND IP.cd_periodo_letivo = N.cd_periodo_letivo) " +
				       "LEFT OUTER JOIN grl_pessoa 		C ON (B.cd_administrador = C.cd_pessoa) " +
				       //"LEFT OUTER JOIN srh_dados_funcionais  DF  ON (C.cd_pessoa = DF.cd_pessoa) " +
				       "LEFT OUTER JOIN grl_pessoa 		D ON (B.cd_coordenador = D.cd_pessoa) " +
				       "LEFT OUTER JOIN grl_pessoa 		E ON (B.cd_diretor = E.cd_pessoa) " +
				       "LEFT OUTER JOIN grl_pessoa 		F ON (B.cd_secretario = F.cd_pessoa) " +
				       "LEFT OUTER JOIN grl_pessoa 		G ON (B.cd_tesoureiro = G.cd_pessoa) " +
				       "LEFT OUTER JOIN grl_pessoa 		H ON (B.cd_vice_diretor = H.cd_pessoa) " +
				       "LEFT OUTER JOIN grl_pessoa_endereco M ON (A.cd_empresa = M.cd_pessoa AND M.lg_principal = 1) " +
				       "LEFT OUTER JOIN grl_cidade 		I ON (M.cd_cidade = I.cd_cidade) " +
				       "LEFT OUTER JOIN grl_estado 		J ON (I.cd_estado = J.cd_estado) " +
				       "LEFT OUTER JOIN grl_pessoa 		P ON (L.cd_pessoa_superior = P.cd_pessoa) " +
				       "WHERE 1=1 "; 
		
		LogUtils.debug("SQL:\n"+Search.getStatementSQL(sql, (" ORDER BY L.NM_PESSOA"), criterios, true));
		
		ResultSetMap rsm = Search.find(sql, "ORDER BY L.NM_PESSOA "+sqlLimit[1], criterios, null, connect, false, true);
		
		LogUtils.logTimer("INSTITUICAO_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
		LogUtils.debug("InstituicaoServices.find: Iniciando injeção de dados adicionais...");
		
		while(rsm.next()){
			

			try {
				ResultSetMap rsmDadosFuncionais = new ResultSetMap((connect!=null ? connect : Conexao.conectar()).prepareStatement("SELECT * FROM srh_dados_funcionais WHERE cd_pessoa = " + rsm.getString("cd_administrador") + " ORDER BY cd_matricula DESC").executeQuery());
				if(rsmDadosFuncionais.next()) {
					rsm.setValueToField("CD_MATRICULA", rsmDadosFuncionais.getInt("cd_matricula"));
					rsm.setValueToField("NR_MATRICULA", rsmDadosFuncionais.getString("nr_matricula"));
					rsm.setValueToField("CD_TIPO_ADMISSAO", rsmDadosFuncionais.getInt("cd_tipo_admissao"));
					rsm.setValueToField("TP_ACESSO_CARGO", rsmDadosFuncionais.getInt("tp_acesso_cargo"));
				}
			}catch(Exception e) {e.printStackTrace();}
			
			rsm.setValueToField("NR_TELEFONE", rsm.getString("NR_TELEFONE1"));
			rsm.setValueToField("DS_ENDERECO", rsm.getString("NM_LOGRADOURO") + (rsm.getString("NR_ENDERECO") != null && !rsm.getString("NR_ENDERECO").trim().equals("") ? ", " + rsm.getString("NR_ENDERECO") + ", " : "") + (rsm.getString("NM_BAIRRO") != null && !rsm.getString("NM_BAIRRO").trim().equals("") ? rsm.getString("NM_BAIRRO") : ""));
			rsm.setValueToField("CL_ST_INSTITUICAO", InstituicaoEducacensoServices.situacaoInstituicao[rsm.getInt("ST_INSTITUICAO_PUBLICA")]);
			rsm.setValueToField("CD_PERIODO_LETIVO_BUSCADO", rsm.getInt("cd_periodo_letivo"));
			
			ArrayList<ItemComparator> criteriosFormularioAtributo = new ArrayList<ItemComparator>();
			criteriosFormularioAtributo.add(new ItemComparator("cd_formulario", rsm.getString("cd_formulario"), ItemComparator.EQUAL, Types.INTEGER));
			criteriosFormularioAtributo.add(new ItemComparator("sg_atributo", "EPI", ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmFormularioAtributo = FormularioAtributoDAO.find(criteriosFormularioAtributo, connect);
			if(rsmFormularioAtributo.next()){
				ArrayList<ItemComparator> criteriosFormularioAtributoValor = new ArrayList<ItemComparator>();
				criteriosFormularioAtributoValor.add(new ItemComparator("cd_formulario_atributo", rsmFormularioAtributo.getString("cd_formulario_atributo"), ItemComparator.EQUAL, Types.INTEGER));
				criteriosFormularioAtributoValor.add(new ItemComparator("cd_pessoa", rsm.getString("cd_instituicao"), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmFormularioAtributoValor = FormularioAtributoValorDAO.find(criteriosFormularioAtributoValor, connect);
				if(rsmFormularioAtributoValor.next()){
					rsm.setValueToField("LG_POSSUI_INTERNET", Integer.parseInt(rsmFormularioAtributoValor.getString("txt_atributo_valor")));
				}
			}
			
			
			criteriosFormularioAtributo.add(new ItemComparator("cd_pessoa", rsm.getString("cd_instituicao"), ItemComparator.EQUAL, Types.INTEGER));
			
			criteriosFormularioAtributo.add(new ItemComparator("cd_pessoa", rsm.getString("cd_instituicao"), ItemComparator.EQUAL, Types.INTEGER));
			
		}
		
		rsm.beforeFirst();
				
		ArrayList<String> fields = new ArrayList<String>();
		fields.add("NM_INSTITUICAO");
		rsm.orderBy(fields);
		rsm.beforeFirst();
		
		LogUtils.debug(rsm.size() + " registro(s)");
		LogUtils.logTimer("INSTITUICAO_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
		LogUtils.destroyTimer("INSTITUICAO_FIND_TIMER");
		
		return rsm;
	}
	
	public static ResultSetMap getUnidadeByCodigo(int cdInstituicao) {
		try {			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			crt.add(new ItemComparator("L.cd_pessoa", String.valueOf(cdInstituicao), ItemComparator.EQUAL, Types.INTEGER));
			
			Result result      = findRelatorioUnidades(crt);		
			ResultSetMap rsm   = (ResultSetMap) result.getObjects().get("RSM"); 
			
			return rsm;
		} catch (Exception e) {
			return null;
		}
	}
	


	public static Result findRelatorioUnidades(ArrayList<ItemComparator> criterios) {
		return findRelatorioUnidades(criterios, null);
	}
	
	/**
	 * Metodo que faz a busca para os relatorios de instituição
	 * @param criterios
	 * @param connect
	 * @return
	 */
	public static Result findRelatorioUnidades(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			

			ResultSetMap rsmCursosEducacaoInfantil 	= ParametroServices.getValoresOfParametro("CD_CURSO_EDUCACAO_INFANTIL");
			ResultSetMap rsmCursosFundamental1 		= ParametroServices.getValoresOfParametro("CD_CURSO_FUNDAMENTAL_1");
			ResultSetMap rsmCursosFundamental2 		= ParametroServices.getValoresOfParametro("CD_CURSO_FUNDAMENTAL_2");
			
			
			int qtMaxAlunos = -1;
			String nrIdadeInicial = null;
			String nrIdadeFinal   = null;
			int tpAtendimento   = -1;
			int stRegulamentacao   = -1;
			int cdPeriodoLetivoSuperior = 0;
			boolean buscarAlunos = false;//Variavel para buscar a quantidade de alunos de cada escola (torna a busca bem mais lenta)
			boolean buscarMatriculasPorEtapa = false;//Variavel que busca a quantidade de matriculas por: Educação Infantil, Fundamental 1 e Fundamental 2
			boolean buscarAlunosComDivergencia = false;
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtMaxAlunos"))
					qtMaxAlunos = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrIdadeInicial")) {
					nrIdadeInicial = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrIdadeFinal")) {
					nrIdadeFinal = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpAtendimento")) {
					tpAtendimento = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("stRegulamentacao")) {
					stRegulamentacao = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdPeriodoLetivoSuperior")) {
					cdPeriodoLetivoSuperior = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("buscarAlunos")) {
					buscarAlunos = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("buscarMatriculasPorEtapa")) {
					buscarMatriculasPorEtapa = true;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("buscarAlunosComDivergencia")) {
					buscarAlunosComDivergencia = true;
				}
				else
					crt.add(criterios.get(i));
				
			}
			
			
			if(cdPeriodoLetivoSuperior == 0){
				ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0), connect);
				if(rsmPeriodoRecente.next()){
					cdPeriodoLetivoSuperior = rsmPeriodoRecente.getInt("cd_periodo_letivo");							
				}
			}
			
			ResultSetMap rsm = Search.find(
					   "SELECT L.*, B.*, N.*, M.*, L.nm_pessoa AS nm_fantasia, " +
					   "	   L.cd_pessoa as cd_instituicao, L.nm_pessoa as nm_instituicao, I.nm_cidade, J.nm_estado, J.sg_estado, " + 
					   "       P.nm_pessoa AS nm_pessoa_superior, GEO_P.vl_latitude, GEO_P.vl_longitude, IP.cd_periodo_letivo, IP.nm_periodo_letivo " +
				       "FROM grl_pessoa                 L  " +
				       "JOIN acd_instituicao B ON (L.cd_pessoa = B.cd_instituicao) " +
				       "LEFT OUTER JOIN acd_instituicao_periodo IP ON (B.cd_instituicao = IP.cd_instituicao) " +
				       "LEFT OUTER JOIN acd_instituicao_educacenso N ON (N.cd_instituicao = L.cd_pessoa AND IP.cd_periodo_letivo = N.cd_periodo_letivo) " +
				       "LEFT OUTER JOIN grl_pessoa_endereco M ON (L.cd_pessoa = M.cd_pessoa AND M.lg_principal = 1) " +
				       "LEFT OUTER JOIN grl_cidade 		I ON (M.cd_cidade = I.cd_cidade) " +
				       "LEFT OUTER JOIN grl_estado 		J ON (I.cd_estado = J.cd_estado) " +
				       "LEFT OUTER JOIN grl_pessoa 		P ON (L.cd_pessoa_superior = P.cd_pessoa) " +
				       "LEFT OUTER JOIN geo_localizacao GEO_L ON (L.cd_pessoa = GEO_L.cd_pessoa) " +
				       "LEFT OUTER JOIN geo_ponto 		GEO_P ON (GEO_L.cd_referencia = GEO_P.cd_referencia) " +
				       "WHERE 1=1 AND L.cd_pessoa <> " + ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0)
				       + (stRegulamentacao >= 0 ? " AND N.st_regulamentacao = " + stRegulamentacao + " " : " ")
				       + (cdPeriodoLetivoSuperior > 0 ? " AND IP.cd_periodo_letivo_superior = " + cdPeriodoLetivoSuperior : ""), "ORDER BY L.NM_PESSOA",
				       crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			int qtEscolasZonaUrbana = 0;
			int qtEscolasZonaRural = 0;
			int qtEscolasCreche = 0;
			int qtEscolasMatriculadosZonaUrbana = 0;
			int qtEscolasMatriculadosZonaRural = 0;
			int qtEscolasMatriculadosCreche = 0;
			int x = 0;
			while(rsm.next()){
				rsm.setValueToField("NR_TELEFONE", rsm.getString("NR_TELEFONE1"));
				rsm.setValueToField("DS_ENDERECO", rsm.getString("NM_LOGRADOURO") + (rsm.getString("NR_ENDERECO") != null && !rsm.getString("NR_ENDERECO").trim().equals("") ? ", " + rsm.getString("NR_ENDERECO") + ", " : "") + (rsm.getString("NM_BAIRRO") != null && !rsm.getString("NM_BAIRRO").trim().equals("") ? rsm.getString("NM_BAIRRO") : ""));
				rsm.setValueToField("CL_ST_INSTITUICAO", InstituicaoEducacensoServices.situacaoInstituicao[rsm.getInt("ST_INSTITUICAO_PUBLICA")]);
				rsm.setValueToField("NM_ZONA", "Zona " + PessoaEnderecoServices.tiposZona[rsm.getInt("TP_ZONA")]);
				rsm.setValueToField("NM_LOCALIZACAO", "Zona " + PessoaEnderecoServices.tiposLocalizacao[rsm.getInt("TP_LOCALIZACAO")]);
				if(rsm.getInt("tp_instituicao") == TP_INSTITUICAO_CRECHE){
					rsm.setValueToField("NM_ZONA", "Creches");
					rsm.setValueToField("TP_ZONA", 4);
					rsm.setValueToField("NM_LOCALIZACAO", "Creches");
					rsm.setValueToField("TP_LOCALIZACAO", 4);
				}
				
				if(rsm.getInt("TP_ZONA") == 0)
					rsm.setValueToField("TP_ZONA", 3);
				if(rsm.getInt("TP_LOCALIZACAO") == 0)
					rsm.setValueToField("TP_LOCALIZACAO", 3);
				
				//Retira o registro da instituição caso a mesma possua uma quantidade de alunos maior do que a passada na variavel qtMaxAlunos
				if(qtMaxAlunos >= 0)
					if(getAlunosOf(rsm.getInt("cd_instituicao"), rsm.getInt("cd_periodo_letivo")).size() > qtMaxAlunos){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				
				//Caso a variavel seja passada como true, o sistema irá buscar os alunos e discriminar a informação de matriculas e vagas no infantil, fundamental 1 e 2
				if(buscarAlunos){
					
					int nrMatriculadosEducacaoInfantil = 0;
					int nrVagasEducacaoInfantil = 0;
					int nrMatriculadosFundamental1 = 0;
					int nrVagasFundamental1 = 0;
					int nrMatriculadosFundamental2 = 0;
					int nrVagasFundamental2 = 0;
					int nrVagasMulti = 0;
					
					ResultSetMap rsmTurmas = getAllTurmasByInstituicaoPeriodoSimplificado(rsm.getInt("cd_instituicao"), rsm.getInt("cd_periodo_letivo"), tpAtendimento, connect);
					int qtVagas  = 0;
					int qtAlunos = 0;
					int qtTurmas = rsmTurmas.size();

					int qtAlunosIdadeDivergente = 0;
					
					while(rsmTurmas.next()){
						qtVagas  += rsmTurmas.getInt("QT_VAGAS");
						qtAlunos += rsmTurmas.getInt("NR_MATRICULADOS");
						if(nrIdadeInicial != null && nrIdadeFinal != null){
							int qtAlunosForaIdade = 0;
							try{
								
								//Quantidade os alunos que estão fora da idade para o seu curso (série)
								ResultSetMap rsmMatriculas = new ResultSetMap(connect.prepareStatement("SELECT B.dt_nascimento FROM acd_matricula A, grl_pessoa_fisica B WHERE A.cd_aluno = B.cd_pessoa AND A.cd_turma = " + rsmTurmas.getInt("cd_turma") + " AND A.st_matricula IN( " + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_CONCLUIDA + ")").executeQuery());
								while(rsmMatriculas.next()){ 
									GregorianCalendar dtNascimento = rsmMatriculas.getGregorianCalendar("dt_nascimento");
									int nrIdadeAluno = Util.getIdade(dtNascimento, 31, 2, Integer.parseInt(rsm.getString("NM_PERIODO_LETIVO")));
									if(nrIdadeAluno < Integer.parseInt(nrIdadeInicial) || nrIdadeAluno > Integer.parseInt(nrIdadeFinal)){
										qtAlunosForaIdade++;
									}
									
								}
							}
							catch(SQLException e){e.printStackTrace();}
							qtAlunos -= qtAlunosForaIdade;
							if(qtAlunos < 0)
								qtAlunos = 0;
						}
						
						if(buscarMatriculasPorEtapa){
							
							Turma turma = TurmaDAO.get(rsmTurmas.getInt("cd_turma"), connect);
							
							while(rsmCursosEducacaoInfantil.next()){
								if(turma.getCdCurso() == Integer.parseInt(rsmCursosEducacaoInfantil.getString("VL_REAL"))){
									nrVagasEducacaoInfantil += turma.getQtVagas();
									break;
								}
										
							}
							rsmCursosEducacaoInfantil.beforeFirst();
							
							while(rsmCursosFundamental1.next()){
								if(turma.getCdCurso() == Integer.parseInt(rsmCursosFundamental1.getString("VL_REAL"))){
									nrVagasFundamental1 += turma.getQtVagas();
									break;
								}
										
							}
							rsmCursosFundamental1.beforeFirst();
							
							while(rsmCursosFundamental2.next()){
								if(turma.getCdCurso() == Integer.parseInt(rsmCursosFundamental2.getString("VL_REAL"))){
									nrVagasFundamental2 += turma.getQtVagas();
									break;
								}
										
							}
							rsmCursosFundamental2.beforeFirst();
							
							
							
							ResultSetMap rsmMatriculas = new ResultSetMap(connect.prepareStatement("SELECT A.cd_curso FROM acd_matricula A WHERE A.cd_turma = " + rsmTurmas.getInt("cd_turma") + " AND A.st_matricula = " + MatriculaServices.ST_ATIVA).executeQuery());
							while(rsmMatriculas.next()){ 
								while(rsmCursosEducacaoInfantil.next()){
									if(rsmMatriculas.getInt("cd_curso") == Integer.parseInt(rsmCursosEducacaoInfantil.getString("VL_REAL"))){
										nrMatriculadosEducacaoInfantil++;
										break;
									}
											
								}
								rsmCursosEducacaoInfantil.beforeFirst();
								
								while(rsmCursosFundamental1.next()){
									if(rsmMatriculas.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental1.getString("VL_REAL"))){
										nrMatriculadosFundamental1++;
										break;
									}
											
								}
								rsmCursosFundamental1.beforeFirst();
								
								while(rsmCursosFundamental2.next()){
									if(rsmMatriculas.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental2.getString("VL_REAL"))){
										nrMatriculadosFundamental2++;
										break;
									}
											
								}
								rsmCursosFundamental2.beforeFirst();
							}
							
							
							
						}
						

						if(buscarAlunosComDivergencia) {
							
							
							
							ResultSetMap rsmMatriculas = new ResultSetMap(connect.prepareStatement("SELECT B.dt_nascimento FROM acd_matricula A, grl_pessoa_fisica B WHERE A.cd_aluno = B.cd_pessoa AND A.cd_turma = " + rsmTurmas.getInt("cd_turma") + " AND A.st_matricula IN (" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_CONCLUIDA + ")").executeQuery());
							while(rsmMatriculas.next()){ 
								GregorianCalendar dtNascimento = rsmMatriculas.getGregorianCalendar("dt_nascimento");
								int nrIdadeAluno = Util.getIdade(dtNascimento, 31, 2, Integer.parseInt(rsm.getString("NM_PERIODO_LETIVO")));
								Curso curso = CursoDAO.get(rsmTurmas.getInt("cd_curso"), connect);
								if(nrIdadeAluno != curso.getNrIdade() && (CursoServices.isCreche(curso.getCdCurso()) || CursoServices.isEducacaoInfantil(curso.getCdCurso()) || CursoServices.isFundamentalRegular(curso.getCdCurso()))) {
									qtAlunosIdadeDivergente++;
								}
								
							}
						}
					}
					
					System.out.println(rsm.getString("NM_INSTITUICAO"));
					System.out.println("Matriculados Ed. Infantil: " + nrMatriculadosEducacaoInfantil);
					System.out.println("Vagas Ed. Infantil: " + nrVagasEducacaoInfantil);
					System.out.println("Matriculos Fundamental 1: " + nrMatriculadosFundamental1);
					System.out.println("Vagas Fundamental 1: " + nrVagasFundamental1);
					System.out.println("Matriculados Fundamental 2: " + nrMatriculadosFundamental2);
					System.out.println("Vagas Fundamental 2: " + nrVagasFundamental2);
					System.out.println();
					
					rsm.setValueToField("NR_MATRICULADOS_EDUCACAO_INFANTIL", nrMatriculadosEducacaoInfantil);
					rsm.setValueToField("NR_VAGAS_EDUCACAO_INFANTIL", nrVagasEducacaoInfantil);
					rsm.setValueToField("NR_MATRICULADOS_FUNDAMENTAL_1", nrMatriculadosFundamental1);
					rsm.setValueToField("NR_VAGAS_FUNDAMENTAL_1", nrVagasFundamental1);
					rsm.setValueToField("NR_MATRICULADOS_FUNDAMENTAL_2", nrMatriculadosFundamental2);
					rsm.setValueToField("NR_VAGAS_FUNDAMENTAL_2", nrVagasFundamental2);
					
					
					
					//Separa a quantidade de alunos de zona rural, urbana e creche
					InstituicaoEducacenso instituicaoEducacenso = InstituicaoEducacensoDAO.get(rsm.getInt("cd_instituicao"), getPeriodoLetivoCorrespondente(rsm.getInt("cd_instituicao"), cdPeriodoLetivoSuperior), connect);
					if(instituicaoEducacenso != null){
						if(instituicaoEducacenso.getTpLocalizacao() == PessoaEnderecoServices.TP_ZONA_RURAL && (rsm.getInt("tp_instituicao") == TP_INSTITUICAO_ESCOLA)){
							qtEscolasZonaRural += 1;
							qtEscolasMatriculadosZonaRural += qtAlunos;
						}
						else if(instituicaoEducacenso.getTpLocalizacao() == PessoaEnderecoServices.TP_ZONA_URBANA && (rsm.getInt("tp_instituicao") == TP_INSTITUICAO_ESCOLA)){
							qtEscolasZonaUrbana += 1;
							qtEscolasMatriculadosZonaUrbana += qtAlunos;
						}
						else if(rsm.getInt("tp_instituicao") == TP_INSTITUICAO_CRECHE){
							qtEscolasCreche += 1;
							qtEscolasMatriculadosCreche += qtAlunos;
						}
					}
					rsm.setValueToField("NR_TURMAS", qtTurmas);
					rsm.setValueToField("NR_VAGAS", qtVagas);
					if(qtVagas > 0){
						rsm.setValueToField("PR_OCUPACAO", "" + ((qtAlunos * 100) / qtVagas) + "%");
						rsm.setValueToField("NR_MATRICULADOS", qtAlunos);
					}
					else{
						rsm.setValueToField("PR_OCUPACAO", "0%");
						rsm.setValueToField("NR_MATRICULADOS", 0);
					}
				}
				
				x++;
				
			}
			
			rsm.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("TP_LOCALIZACAO");
			fields.add("NM_INSTITUICAO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsm);
			result.addObject("QT_ESCOLAS_ZONA_URBANA", qtEscolasZonaUrbana);
			result.addObject("QT_ESCOLAS_ZONA_RURAL", qtEscolasZonaRural);
			result.addObject("QT_ESCOLAS_CRECHE", qtEscolasCreche);
			result.addObject("QT_ESCOLAS_MATRICULADOS_ZONA_URBANA", qtEscolasMatriculadosZonaUrbana);
			result.addObject("QT_ESCOLAS_MATRICULADOS_ZONA_RURAL", qtEscolasMatriculadosZonaRural);
			result.addObject("QT_ESCOLAS_MATRICULADOS_CRECHE", qtEscolasMatriculadosCreche);
			return result;
		
		}
		
		catch(Exception e){
			if(isConnectionNull){
				Conexao.rollback(connect);
			}
			
			return null;
		}
		
		finally{
			if(isConnectionNull){
				Conexao.desconectar(connect);
			}
		}
	}
	
	public static Result findRelatorioMatriculadosPorModalidade(ArrayList<ItemComparator> criterios) {
		return findRelatorioMatriculadosPorModalidade(criterios, null);
	}

	/**
	 * Faz a busca do relatórios de matrículados da instituição quantificando por modalidade/curso
	 * @param criterios
	 * @param connect
	 * @return
	 */
	public static Result findRelatorioMatriculadosPorModalidade(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int qtMaxAlunos = -1;
			String nrIdadeInicial = null;//Usado para filtrar as instituições que possuem alunos de acordo a uma faixa de idade
			String nrIdadeFinal   = null;//Usado para filtrar as instituições que possuem alunos de acordo a uma faixa de idade
			int tpAtendimento   = -1;
			int stRegulamentacao   = -1;
			int cdPeriodoLetivoSuperior = 0;
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtMaxAlunos"))
					qtMaxAlunos = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrIdadeInicial")) {
					nrIdadeInicial = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrIdadeFinal")) {
					nrIdadeFinal = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpAtendimento")) {
					tpAtendimento = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("stRegulamentacao")) {
					stRegulamentacao = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdPeriodoLetivoSuperior")) {
					cdPeriodoLetivoSuperior = Integer.parseInt(criterios.get(i).getValue());
				}
				else
					crt.add(criterios.get(i));
				
			}
			
			InstituicaoPeriodo instituicaoPeriodoSuperior = null;
			if(cdPeriodoLetivoSuperior > 0)
				instituicaoPeriodoSuperior = InstituicaoPeriodoDAO.get(cdPeriodoLetivoSuperior, connect);
				
			ResultSetMap rsm = Search.find(
					   "SELECT L.nm_pessoa, G.nm_produto_servico, B.cd_curso, COUNT(A.cd_matricula) AS NR_MATRICULADOS, " 
					   + "G.nm_produto_servico AS nm_modalidade, M.nm_logradouro, N.tp_localizacao_diferenciada, D.cd_periodo_letivo, D.nm_periodo_letivo, C.cd_instituicao "
					   + "FROM acd_matricula						 A "
					   + "JOIN acd_curso			        		 B ON (A.cd_curso = B.cd_curso) "
					   + "JOIN acd_turma							 C ON (A.cd_turma = C.cd_turma) "
					   + "JOIN acd_instituicao_periodo				 D ON (A.cd_periodo_letivo = D.cd_periodo_letivo) " 
					   + "JOIN acd_instituicao						 E ON (C.cd_instituicao = E.cd_instituicao) "
					   + "JOIN grl_pessoa                       	 L ON (E.cd_instituicao = L.cd_pessoa) "
					   + "JOIN grl_pessoa_fisica                   	 PF ON (A.cd_aluno = PF.cd_pessoa) "
					   + "JOIN grl_produto_servico              	 G ON (B.cd_curso = G.cd_produto_servico) "
					   + "LEFT OUTER JOIN grl_pessoa_endereco		 M ON (M.cd_pessoa = E.cd_instituicao) "
					   + "LEFT OUTER JOIN acd_instituicao_educacenso N ON (N.cd_instituicao = E.cd_instituicao AND N.cd_periodo_letivo = D.cd_periodo_letivo) "
					   + "WHERE A.st_matricula IN (" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_CONCLUIDA + ")"
					   + (stRegulamentacao >= 0 ? "AND P.st_regulamentacao = " + stRegulamentacao : "")
					   + (cdPeriodoLetivoSuperior > 0 ? " AND D.cd_periodo_letivo_superior = " + cdPeriodoLetivoSuperior : "")
					   + (nrIdadeInicial != null && nrIdadeFinal != null ? " AND PF.dt_nascimento <= '" + Util.convCalendarStringSqlCompleto(Util.getDataNascimentoByIdade(Integer.parseInt(nrIdadeInicial), 31, 2, (instituicaoPeriodoSuperior != null ? Integer.parseInt(instituicaoPeriodoSuperior.getNmPeriodoLetivo()) : 0))) + "' AND PF.dt_nascimento >= '" + Util.convCalendarStringSqlCompleto(Util.getDataNascimentoByIdade(Integer.parseInt(nrIdadeFinal), 31, 2, (instituicaoPeriodoSuperior != null ? Integer.parseInt(instituicaoPeriodoSuperior.getNmPeriodoLetivo()) : 0))) + "'" : ""),
					   " GROUP BY L.nm_pessoa, G.nm_produto_servico, B.cd_curso, M.nm_logradouro, N.tp_localizacao_diferenciada, D.cd_periodo_letivo, D.nm_periodo_letivo, C.cd_instituicao " + (qtMaxAlunos > 0 ? " HAVING COUNT(A.cd_matricula) <= " + qtMaxAlunos : "") + " ORDER BY L.nm_pessoa",
					   crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			int x = 0;
			while(rsm.next()){
				if(qtMaxAlunos >= 0)
					if(rsm.getInt("NR_MATRICULADOS") > qtMaxAlunos){
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
			
			Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsm);
			return result;
		
		}
		
		catch(Exception e){
			if(isConnectionNull){
				Conexao.rollback(connect);
			}
			
			return null;
		}
		
		finally{
			if(isConnectionNull){
				Conexao.desconectar(connect);
			}
		}
	}

	
	public static Result findRelatorioFornecedores(ArrayList<ItemComparator> criterios) {
		return findRelatorioFornecedores(criterios, null);
	}

	public static Result findRelatorioFornecedores(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			String idFornecedor = ""; 
			String nmFornecedor = ""; 
			String telTelefone = "";  
			String nmEmail = "";	  
			String nmLogradouro = ""; 
			String sgEstado = "";	  
			String nmCidade = ""; 	  
			int cdGrupo = -1;	  
			int qtProdutos = -1;
			String stCadastro = "";
			String nmBanco = "";
			
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("idFornecedor"))
					idFornecedor = criterios.get(i).getValue();
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nmFornecedor")) {
					nmFornecedor = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("telTelefone")) {
					telTelefone = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nmEmail")) {
					nmEmail = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nmLogradouro")) {
					nmLogradouro = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("sgEstado")) {
					sgEstado = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nmCidade")) {
					nmCidade = criterios.get(i).getValue();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdGrupo")) {
					cdGrupo = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("qtProdutos")) {
					qtProdutos = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("clSituacao")) {
					stCadastro = criterios.get(i).getValue();
				}
				else
					crt.add(criterios.get(i));
				
			}

			ResultSetMap rsm = Search.find("SELECT A.id_pessoa, A.cd_pessoa, A.nm_pessoa, A.nr_telefone1, A.nr_celular, A.nm_email, C.nm_logradouro, D.nm_cidade, E.sg_estado, G.nm_vinculo, "
					+ "(SELECT COUNT(cd_produto_servico) FROM adm_produto_fornecedor sA WHERE sA.cd_fornecedor = B.cd_pessoa) as QT_PRODUTOS "
					+ "FROM grl_pessoa A "
					+ "JOIN grl_pessoa_juridica			   B ON (A.cd_pessoa = B.cd_pessoa) "
					+ "LEFT OUTER JOIN grl_pessoa_endereco C ON (A.cd_pessoa = C.cd_pessoa) "
					+ "LEFT OUTER JOIN grl_cidade 		   D ON (C.cd_cidade = D.cd_cidade) "
					+ "LEFT OUTER JOIN grl_estado 		   E ON (D.cd_estado = E.cd_estado) "
					+ "LEFT OUTER JOIN grl_pessoa_empresa  F ON (A.cd_pessoa = F.cd_pessoa) "
					+ "LEFT OUTER JOIN grl_vinculo 		   G ON (F.cd_vinculo = G.cd_vinculo) "
					+ "WHERE F.cd_vinculo = 21 "
					+ (cdGrupo > 0 ? "AND EXISTS "
					+ "		(SELECT H.cd_fornecedor"
					+ "		FROM adm_produto_fornecedor H"
					+ "		JOIN alm_produto_grupo 		I on (H.cd_produto_servico = I.cd_produto_servico)"
					+ "		JOIN alm_grupo 				J on (I.cd_grupo = J.cd_grupo)"
					+ "		WHERE I.cd_grupo = " + cdGrupo
					+ "		AND A.cd_pessoa = H.cd_fornecedor)" : "")
					+ (qtProdutos > -1 ? " AND (SELECT COUNT(cd_produto_servico) "
					+ 								"FROM adm_produto_fornecedor) >= " + qtProdutos : "")
					, "", crt, connect!=null ? connect : Conexao.conectar(), connect==null);

			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_INSTITUICAO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsm);
			
			return result;
		
		}
		
		catch(Exception e){
			if(isConnectionNull){
				Conexao.rollback(connect);
			}
			e.printStackTrace();
			
			return null;
		}
		
		finally{
			if(isConnectionNull){
				Conexao.desconectar(connect);
			}
		}
	}
	
	public static Result findRelatorioPorPatologia(ArrayList<ItemComparator> criterios) {
		return findRelatorioPorPatologia(criterios, null);
	}

	/**
	 * Relatório da quantidade de alunos com cada uma das doenças do sistema
	 * @param criterios
	 * @param connect
	 * @return
	 */
	public static Result findRelatorioPorPatologia(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int qtMaxAlunos = -1;
			String nrIdadeInicial = null;
			GregorianCalendar dtNascimentoInicial = null;
			String nrIdadeFinal   = null;
			GregorianCalendar dtNascimentoFinal = null;
			int tpAtendimento   = -1;
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtMaxAlunos"))
					qtMaxAlunos = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrIdadeInicial")) {
					nrIdadeInicial = criterios.get(i).getValue();
					dtNascimentoInicial = Util.getDataNascimentoPossivel(nrIdadeInicial);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrIdadeFinal")) {
					nrIdadeFinal = criterios.get(i).getValue();
					dtNascimentoFinal = Util.getDataNascimentoPossivel(nrIdadeFinal);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpAtendimento")) {
					tpAtendimento = Integer.parseInt(criterios.get(i).getValue());
				}
				else
					crt.add(criterios.get(i));
				
			}
			
			ResultSetMap rsm = Search.find(
					   "SELECT DOE.cd_doenca, DOE.nm_doenca AS nm_patologia, L.cd_pessoa, L.nm_pessoa as nm_instituicao, N.tp_localizacao, TIP.cd_periodo_letivo, COUNT(MAT.cd_matricula) AS nr_alunos " +
				       "FROM grl_pessoa L " +
				       "JOIN acd_instituicao B ON (L.cd_pessoa = B.cd_instituicao) " +
				       "LEFT OUTER JOIN grl_pessoa_endereco M ON (L.cd_pessoa = M.cd_pessoa AND M.lg_principal = 1) " +
				       "JOIN acd_turma T ON (L.cd_pessoa = T.cd_instituicao) " +
				       "JOIN acd_instituicao_periodo TIP ON (T.cd_periodo_letivo = TIP.cd_periodo_letivo) " +
				       "LEFT OUTER JOIN acd_instituicao_educacenso N ON (N.cd_instituicao = L.cd_pessoa AND N.cd_periodo_letivo = TIP.cd_periodo_letivo) " +
				       "JOIN acd_matricula MAT ON (T.cd_turma = MAT.cd_turma) " +
				       "JOIN grl_pessoa_doenca PD ON (MAT.cd_aluno = PD.cd_pessoa) " +
				       "JOIN grl_doenca DOE ON (PD.cd_doenca = DOE.cd_doenca) " +
				       "JOIN grl_pessoa_fisica PF ON (MAT.cd_aluno = PF.cd_pessoa) " +
				       "WHERE 1=1 AND L.cd_pessoa <> " + ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0) +
				       (tpAtendimento >= 0 ? " AND T.tp_atendimento = " + tpAtendimento : "") +
					   						 " AND T.st_turma <> "+ TurmaServices.ST_INATIVO +
						                     " AND T.nm_turma NOT LIKE 'TRANS%'" + 
					   						 " AND TIP.st_periodo_letivo = " + InstituicaoPeriodoServices.ST_ATUAL + 
					   						 (dtNascimentoInicial != null ? " AND PF.dt_nascimento >= " + Util.convCalendarStringSql(dtNascimentoInicial) : "") + 
					   						 (dtNascimentoFinal != null ? " AND PF.dt_nascimento <= " + Util.convCalendarStringSql(dtNascimentoFinal) : ""), " GROUP BY DOE.cd_doenca, DOE.nm_doenca, L.cd_pessoa, L.nm_pessoa, N.tp_localizacao, TIP.cd_periodo_letivo ORDER BY DOE.nm_doenca, L.nm_pessoa",
				       crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			int x = 0;
			while(rsm.next()){
				
				rsm.setValueToField("NM_LOCALIZACAO", "Zona " + PessoaEnderecoServices.tiposLocalizacao[rsm.getInt("TP_LOCALIZACAO")]);
				if(rsm.getInt("tp_instituicao") == TP_INSTITUICAO_CRECHE){
					rsm.setValueToField("NM_ZONA", "Creches");
					rsm.setValueToField("TP_ZONA", 4);
					rsm.setValueToField("NM_LOCALIZACAO", "Creches");
					rsm.setValueToField("TP_LOCALIZACAO", 4);
				}
				
				if(qtMaxAlunos >= 0)
					if(getAlunosOf(rsm.getInt("cd_instituicao"), rsm.getInt("cd_periodo_letivo")).size() > qtMaxAlunos){
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
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_PATOLOGIA");
			fields.add("NM_INSTITUICAO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsm);
			return result;
		
		}
		
		catch(Exception e){
			if(isConnectionNull){
				Conexao.rollback(connect);
			}
			
			return null;
		}
		
		finally{
			if(isConnectionNull){
				Conexao.desconectar(connect);
			}
		}
	}
	
	public static ResultSetMap getAlunosOf(int cdInstituicao, int cdPeriodoLetivo)	{
		return getAlunosOf(cdInstituicao, cdPeriodoLetivo, null);
	}
	
	public static ResultSetMap getAlunosOf(int cdInstituicao, int cdPeriodoLetivo, Connection connect)	{
		return getAlunosOf(cdInstituicao, cdPeriodoLetivo, false, connect);
	}
	
	public static ResultSetMap getAlunosOf(int cdInstituicao, int cdPeriodoLetivo, boolean semCursosExtras)	{
		return getAlunosOf(cdInstituicao, cdPeriodoLetivo, semCursosExtras, null);
	}
	
	/**
	 * Busca todos os alunos de uma instituição e de um periodo letivo
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @param semCursosExtras
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAlunosOf(int cdInstituicao, int cdPeriodoLetivo, boolean semCursosExtras, Connection connect)	{
		boolean isConnection = connect == null;
		try	{
			
			if(isConnection){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_pessoa AS nm_aluno " +
					 "FROM acd_matricula A " +
                     "JOIN grl_pessoa B ON (A.cd_aluno = B.cd_pessoa) " +
                     "JOIN acd_turma  C ON (A.cd_turma = C.cd_turma) " +
                     "WHERE A.st_matricula IN ("+MatriculaServices.ST_ATIVA+", "+MatriculaServices.ST_CONCLUIDA+") " + 
                     "  AND C.cd_instituicao = "+cdInstituicao+
                     "  AND A.cd_periodo_letivo = " + cdPeriodoLetivo + 
                     (semCursosExtras ? " AND A.cd_curso NOT IN ("+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)+")" : ""));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e)	{
			if(isConnection)
				Conexao.rollback(connect);
			
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnection)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAlunosOfTurmas(int cdInstituicao, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			ResultSetMap rsmFinal = new ResultSetMap();
			ResultSetMap rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT cd_turma FROM acd_turma WHERE cd_instituicao = " + cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery());
			while(rsmTurmas.next()){
				rsmFinal.getLines().addAll(TurmaServices.getAlunosOf(rsmTurmas.getInt("CD_TURMA")).getLines());				
			}
			rsmFinal.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_ALUNO");
			rsmFinal.orderBy(fields);
			
			rsmFinal.beforeFirst();
			
			return rsmFinal;
					
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getCdAlunosOf(int cdInstituicao)	{
		return getCdAlunosOf(cdInstituicao, false);
	}
	public static ResultSetMap getCdAlunosOf(int cdInstituicao, boolean lancarCenso)	{
		return getCdAlunosOf(cdInstituicao, lancarCenso, false);
	}
	public static ResultSetMap getCdAlunosOf(int cdInstituicao, boolean lancarCenso, boolean lgSituacaoAluno)	{
		return getCdAlunosOf(cdInstituicao, 0, lancarCenso, lgSituacaoAluno);
	}
	public static ResultSetMap getCdAlunosOf(int cdInstituicao, int cdTurma, boolean lancarCenso, boolean lgSituacaoAluno)	{
		return getCdAlunosOf(cdInstituicao, cdTurma, 0, lancarCenso, lgSituacaoAluno);
	}
	
	public static ResultSetMap getMatriculaOf(int cdMatricula){
		return getCdAlunosOf(0, 0, cdMatricula, true, true);
	}
	
	public static ResultSetMap getCdAlunosOf(int cdInstituicao, int cdTurma, int cdMatricula, boolean lancarCenso, boolean lgSituacaoAluno)	{
		return getCdAlunosOf(cdInstituicao, cdTurma, cdMatricula, lancarCenso, lgSituacaoAluno, 0);
	}
	
	public static ResultSetMap getCdAlunosOf(int cdInstituicao, int cdTurma, int cdMatricula, boolean lancarCenso, boolean lgSituacaoAluno, int cdPeriodoLetivo)	{
		return getCdAlunosOf(cdInstituicao, cdTurma, cdMatricula, lancarCenso, lgSituacaoAluno, cdPeriodoLetivo, -1);
	}
	
	public static ResultSetMap getCdAlunosOf(int cdInstituicao, int cdTurma, int cdMatricula, boolean lancarCenso, boolean lgSituacaoAluno, int cdPeriodoLetivo, int tpAposCenso)	{
		return getCdAlunosOf(cdInstituicao, cdTurma, cdMatricula, lancarCenso, lgSituacaoAluno, cdPeriodoLetivo, tpAposCenso, new ArrayList<Integer>());
	}
	
	/**
	 * Método que faz a busca dos alunos do segundo, tanto para a primeira quanto para a segunda etapa
	 * @param cdInstituicao
	 * @param cdTurma
	 * @param cdMatricula
	 * @param lancarCenso
	 * @param lgSituacaoAluno
	 * @param cdPeriodoLetivo
	 * @param tpAposCenso
	 * @param codigos
	 * @return
	 */
	public static ResultSetMap getCdAlunosOf(int cdInstituicao, int cdTurma, int cdMatricula, boolean lancarCenso, boolean lgSituacaoAluno, int cdPeriodoLetivo, int tpAposCenso, ArrayList<Integer> codigos)	{
		Connection connect = Conexao.conectar();
		try	{
			
			ResultSetMap rsm = null;
			String dtReferenciaCenso = ParametroServices.getValorOfParametro("DT_REFERENCIA_CENSO", connect);
			dtReferenciaCenso = dtReferenciaCenso + "/" + ((cdPeriodoLetivo > 0 ? Integer.parseInt(InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect).getNmPeriodoLetivo()) : Util.getDataAtual().get(Calendar.YEAR)) - (lgSituacaoAluno && tpAposCenso != 1 ? 1 : 0));
			GregorianCalendar dtReferenciaCensoCalendar = Util.convStringToCalendar4(dtReferenciaCenso);
			int x = 0;
			
			//Variavel que receberá todos os alunos que devem aparecer no resultado
			ResultSetMap rsmFinal = new ResultSetMap();
			
			//Entra quando não é passado o parametro analisando se se deseja apenas os alunos após o censo, ou quando passado como 0
			if(tpAposCenso == -1 || tpAposCenso == 0){
				
				//Faz a busca de todos os alunos ativos ou concluidos
				rsm = new ResultSetMap(connect.prepareStatement("SELECT A.cd_aluno, A.cd_turma, A.cd_matricula, A.st_matricula, B.nm_pessoa AS nm_aluno, A.nr_matricula, A.st_aluno_censo, D.nm_produto_servico AS NM_CURSO_MATRICULA, PF.nm_mae, PF.dt_nascimento, C.cd_instituicao, C.cd_periodo_letivo " +
						 "FROM acd_matricula A " +
	                     "JOIN grl_pessoa B ON (A.cd_aluno = B.cd_pessoa) " +
	                     "JOIN grl_pessoa_fisica PF ON (A.cd_aluno = PF.cd_pessoa) " +
	                     "JOIN acd_turma  C ON (A.cd_turma = C.cd_turma) " +
	                     "JOIN grl_produto_servico  D ON (A.cd_curso = D.cd_produto_servico) " +
	                     "WHERE 1=1 AND A.st_matricula IN ("+MatriculaServices.ST_ATIVA + ", "+MatriculaServices.ST_CONCLUIDA + ")" + 
	                     (lancarCenso ? " AND C.cd_curso <> 1187 " : "") + (cdInstituicao > 0 ? " AND C.cd_instituicao = "+cdInstituicao : "") + (cdTurma > 0 ? " AND A.cd_turma = " + cdTurma : "") + (cdMatricula > 0 ? " AND A.cd_matricula = " + cdMatricula : "") + (cdPeriodoLetivo > 0 ? " AND A.cd_periodo_letivo = " + cdPeriodoLetivo : "") +
	                     (tpAposCenso == 0 ? " AND C.cd_curso NOT IN (" + ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0) + ", " + ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0) + ")" : "") +
	                     " AND C.nm_turma NOT LIKE 'TRANS%' " +
	                     " ORDER BY B.nm_pessoa").executeQuery());
				//Busca o periodo atual da escola
				InstituicaoPeriodo instituicaoPeriodoAtual = null;
				if(cdInstituicao > 0){
					ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
					if(rsmPeriodoAtual.next()){
						instituicaoPeriodoAtual = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect);
					}
				}
				
				while(rsm.next()){
					rsm.setValueToField("DT_NM_NASCIMENTO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_nascimento")));
					rsm.setValueToField("_LABEL", MatriculaServices.situacaoAlunoCenso[rsm.getInt("ST_ALUNO_CENSO")]);
					//cdTurma deve ser 0 pois aqui deverá haver uma mudança de turma nos alunos de acordo ao que eles forem lançados no censo
					//Por isso os secretários irão lançar as situações de aluno nas turmas finais deles (passando cdTurma) e ao fazer o arquivo
					//o sistema jogará ele na turma que ele realmente foi no censo
					if(cdTurma == 0 && tpAposCenso == 0){
						ResultSetMap rsmAlunosOcorrenciaMatricula = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_ocorrencia_matricula A, grl_ocorrencia B "
								+ "																				WHERE A.cd_ocorrencia = B.cd_ocorrencia "
								+ "																				  AND (cd_matricula_origem = "+rsm.getInt("cd_matricula") + " OR cd_matricula_destino = " + rsm.getInt("cd_matricula") + ")"
								+ "																				  AND B.cd_tipo_ocorrencia = " + InstituicaoEducacensoServices.TP_OCORRENCIA_REMANEJAMENTO_ALUNO
								+ "																				ORDER BY dt_ocorrencia DESC").executeQuery());
						
						while(rsmAlunosOcorrenciaMatricula.next()){
							if(rsmAlunosOcorrenciaMatricula.getGregorianCalendar("dt_ocorrencia").after(dtReferenciaCensoCalendar)){
								Turma turmaOrigem = TurmaDAO.get(rsmAlunosOcorrenciaMatricula.getInt("cd_turma_origem"), connect);
								rsm.setValueToField("CD_TURMA", turmaOrigem.getCdTurma());
								rsm.setValueToField("CD_CURSO", turmaOrigem.getCdCurso());
							}
						}
						rsmAlunosOcorrenciaMatricula.beforeFirst();
					}
					
					//Verifica se a turma já foi fechada ou não, na fase de Situação do Aluno
					ResultSetMap rsmTurmaSituacaoAlunoCenso = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_ocorrencia A, acd_ocorrencia_turma B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO + ") AND cd_turma = "+rsm.getInt("cd_turma")+" order by dt_ocorrencia DESC").executeQuery());
					if(rsmTurmaSituacaoAlunoCenso.size() == 0){
						rsm.setValueToField("ST_TURMA_SITUACAO_ALUNO_CENSO", TurmaServices.ST_TURMA_SITUACAO_ALUNO_CENSO_LIBERADA);
					}
					else{
						if(rsmTurmaSituacaoAlunoCenso.next()){
							if(rsmTurmaSituacaoAlunoCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO)){
								rsm.setValueToField("ST_TURMA_SITUACAO_ALUNO_CENSO", TurmaServices.ST_TURMA_SITUACAO_ALUNO_CENSO_LIBERADA);
							}
							else{
								rsm.setValueToField("ST_TURMA_SITUACAO_ALUNO_CENSO", TurmaServices.ST_TURMA_SITUACAO_ALUNO_CENSO_FECHADA);
							}
						}
					}
					
					if(lancarCenso){
						boolean excluido = false;
						ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
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
							if(instituicaoPeriodoAtual != null){
								InstituicaoPeriodo instituicaoPeriodoMatricula = InstituicaoPeriodoDAO.get(matricula.getCdPeriodoLetivo(), connect);
								if(!instituicaoPeriodoMatricula.getNmPeriodoLetivo().equals((tpAposCenso == 0 ? String.valueOf(Integer.parseInt(instituicaoPeriodoAtual.getNmPeriodoLetivo())-1) : instituicaoPeriodoAtual.getNmPeriodoLetivo()))){
									continue;
								}
							}
							
							if(rsmAlunosOcorrenciaMatricula.getGregorianCalendar("dt_ocorrencia").after(dtReferenciaCensoCalendar)){
								rsm.setValueToField("ST_MATRICULA", rsmAlunosOcorrenciaMatricula.getInt("st_matricula_origem"));
							}
						}
						rsmAlunosOcorrenciaMatricula.beforeFirst();
						
						if(rsm.getInt("ST_MATRICULA") != MatriculaServices.ST_ATIVA && rsm.getInt("ST_MATRICULA") != MatriculaServices.ST_CONCLUIDA){
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
					
					ResultSetMap rsmFinalizacaoSituacaoAlunoCenso = new ResultSetMap(connect.prepareStatement("SELECT cd_tipo_ocorrencia FROM grl_ocorrencia A, acd_ocorrencia_instituicao B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_ESCOLA_SITUACAO_ALUNO_CENSO + ") AND cd_instituicao = "+rsm.getInt("cd_instituicao")+" AND cd_periodo_letivo = "+rsm.getInt("cd_periodo_letivo")+" order by dt_ocorrencia DESC").executeQuery());
					if(rsmFinalizacaoSituacaoAlunoCenso.next()){
						if(rsmFinalizacaoSituacaoAlunoCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_SITUACAO_ALUNO_CENSO)){
							rsm.setValueToField("LG_FINALIZADA", 1);
						}
						else{
							rsm.setValueToField("LG_FINALIZADA", 0);
						}
					}
					else{
						rsm.setValueToField("LG_FINALIZADA", 0);
					}
					
					if(!codigos.contains(rsm.getInt("cd_aluno"))){
						rsmFinal.addRegister(rsm.getRegister());
						codigos.add(rsm.getInt("cd_aluno"));
					}
					
				}
			}
			if(lgSituacaoAluno && (tpAposCenso == -1 || tpAposCenso == 1))
			{
				rsm = new ResultSetMap(connect.prepareStatement("SELECT A.cd_aluno, A.cd_turma, A.cd_matricula, A.st_matricula, B.nm_pessoa AS nm_aluno, A.nr_matricula, A.st_aluno_censo, D.nm_produto_servico AS NM_CURSO_MATRICULA, PF.nm_mae, PF.dt_nascimento " +
						 "FROM acd_matricula A " +
	                    "JOIN grl_pessoa B ON (A.cd_aluno = B.cd_pessoa) " +
	                    "JOIN grl_pessoa_fisica PF ON (A.cd_aluno = PF.cd_pessoa) " +
	                    "JOIN acd_turma  C ON (A.cd_turma = C.cd_turma) " +
	                    "JOIN grl_produto_servico  D ON (A.cd_curso = D.cd_produto_servico) " +
	                    "WHERE 1=1 " + (cdInstituicao > 0 ? " AND C.cd_instituicao = "+cdInstituicao : "") + (cdTurma > 0 ? " AND A.cd_turma = " + cdTurma : "") + (cdMatricula > 0 ? " AND A.cd_matricula = " + cdMatricula : "") + (cdPeriodoLetivo > 0 ? " AND A.cd_periodo_letivo = " + cdPeriodoLetivo : "") +
	                    "  AND (EXISTS (SELECT * FROM grl_ocorrencia O, acd_ocorrencia_matricula OM WHERE O.cd_ocorrencia = OM.cd_ocorrencia AND OM.cd_matricula_origem = A.cd_matricula AND O.cd_tipo_ocorrencia IN ("+InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_MATRICULA+", "+InstituicaoEducacensoServices.TP_OCORRENCIA_CONFIRMACAO_MATRICULA+", "+InstituicaoEducacensoServices.TP_OCORRENCIA_ATIVACAO_MATRICULA+", "+InstituicaoEducacensoServices.TP_OCORRENCIA_CONCLUSAO_TRANSFERENCIA+") AND O.dt_ocorrencia > '"+Util.convCalendarStringSqlCompleto(dtReferenciaCensoCalendar)+"' )" +
	                    "    OR EXISTS (SELECT * FROM grl_ocorrencia O, acd_ocorrencia_aluno OA WHERE O.cd_ocorrencia = OA.cd_ocorrencia AND OA.cd_aluno = A.cd_aluno AND O.cd_tipo_ocorrencia IN ("+InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_ALUNO+") AND O.dt_ocorrencia > '"+Util.convCalendarStringSqlCompleto(dtReferenciaCensoCalendar)+"' ))" +
	                    (tpAposCenso == 0 ? " AND C.cd_curso NOT IN (" + ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0) + ", " + ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0) + ")" : "") +
	                    " AND C.nm_turma NOT LIKE 'TRANS%' " +
	                    " ORDER BY B.nm_pessoa").executeQuery());
				x = 0;
				while(rsm.next()){
					
					rsm.setValueToField("DT_NM_NASCIMENTO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_nascimento")));
					rsm.setValueToField("_LABEL", MatriculaServices.situacaoAlunoCenso[rsm.getInt("ST_ALUNO_CENSO")]);
					
					ResultSetMap rsmTurmaSituacaoAlunoCenso = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_ocorrencia A, acd_ocorrencia_turma B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO + ") AND cd_turma = "+rsm.getInt("cd_turma")+" order by dt_ocorrencia DESC").executeQuery());
					if(rsmTurmaSituacaoAlunoCenso.size() == 0){
						rsm.setValueToField("ST_TURMA_SITUACAO_ALUNO_CENSO", TurmaServices.ST_TURMA_SITUACAO_ALUNO_CENSO_LIBERADA);
					}
					else{
						if(rsmTurmaSituacaoAlunoCenso.next()){
							if(rsmTurmaSituacaoAlunoCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO)){
								rsm.setValueToField("ST_TURMA_SITUACAO_ALUNO_CENSO", TurmaServices.ST_TURMA_SITUACAO_ALUNO_CENSO_LIBERADA);
							}
							else{
								rsm.setValueToField("ST_TURMA_SITUACAO_ALUNO_CENSO", TurmaServices.ST_TURMA_SITUACAO_ALUNO_CENSO_FECHADA);
							}
						}
					}
					Aluno aluno = AlunoDAO.get(rsm.getInt("cd_aluno"), connect);
					if(lancarCenso){
						if(rsm.getInt("ST_MATRICULA") == MatriculaServices.ST_INATIVO || rsm.getInt("ST_MATRICULA") == MatriculaServices.ST_CANCELADO || rsm.getInt("ST_MATRICULA") == MatriculaServices.ST_FECHADA){
							rsm.deleteRow();
							if(x == 0)
								rsm.beforeFirst();
							else
								rsm.previous();
							continue;
						}
						
						x++;
					}
					
					ResultSetMap rsmFinalizacaoSituacaoAlunoCenso = new ResultSetMap(connect.prepareStatement("SELECT cd_tipo_ocorrencia FROM grl_ocorrencia A, acd_ocorrencia_instituicao B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_SITUACAO_ALUNO_CENSO + ") AND cd_instituicao = "+rsm.getInt("cd_instituicao")+" AND cd_periodo_letivo = "+rsm.getInt("cd_periodo_letivo")+" order by dt_ocorrencia DESC").executeQuery());
					if(rsmFinalizacaoSituacaoAlunoCenso.next()){
						rsm.setValueToField("LG_FINALIZADA", 1);
					}
					else{
						rsm.setValueToField("LG_FINALIZADA", 0);
					}
					
					if(!codigos.contains(rsm.getInt("cd_aluno"))){
						rsmFinal.addRegister(rsm.getRegister());
						codigos.add(rsm.getInt("cd_aluno"));
					}
				}
			}
			
			rsmFinal.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_ALUNO");
			rsmFinal.orderBy(fields);
			rsmFinal.beforeFirst();
			return rsmFinal;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca o relatórios de alunos com as informações da segunda fase do censo
	 */
	public static void getRelatorioAlunosSituacaoAluno()	{
		Connection connect = Conexao.conectar();
		try	{
			
			int qtAprovadosTotal = 0;
			int qtReprovadosTotal = 0;
			int qtDeixouDeFrequentarTotal = 0;
			int qtTransferidosTotal = 0;
			int qtFalecidosTotal = 0;
			
			int qtAprovadosTotal5Ano = 0;
			int qtReprovadosTotal5Ano = 0;
			int qtDeixouDeFrequentarTotal5Ano = 0;
			int qtTransferidosTotal5Ano = 0;
			int qtFalecidosTotal5Ano = 0;
			
			int qtAprovadosTotalFund1 = 0;
			int qtReprovadosTotalFund1 = 0;
			int qtDeixouDeFrequentarTotalFund1 = 0;
			int qtTransferidosTotalFund1 = 0;
			int qtFalecidosTotalFund1 = 0;
			
			FileOutputStream gravar;
			File arquivo = new File("relatorio_situacao_aluno.txt"); 
			//
			File diretorio = new File("/tivic/relatorios");
			if(!diretorio.exists()){
				diretorio.mkdir();
			}
			//
			gravar = new FileOutputStream("/tivic/relatorios/" + arquivo);
			
			String registro = "";
			
			ResultSetMap rsmInstituicoes = getAllAtivas(false, true);
			while(rsmInstituicoes.next()){
				Instituicao instituicao = InstituicaoDAO.get(rsmInstituicoes.getInt("cd_instituicao"), connect);
				
				InstituicaoPeriodo instituicaoPeriodo = null;
				ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(instituicao.getCdInstituicao(), connect);
				if(rsmPeriodoAtual.next()){
					instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect);
				}
				
				int qtAprovadosEscola = 0;
				int qtReprovadosEscola = 0;
				int qtDeixouDeFrequentarEscola = 0;
				int qtTransferidosEscola = 0;
				int qtFalecidosEscola = 0;
				
				ResultSetMap rsmCursos = CursoServices.getAllCursosOfComTurmas(instituicao.getCdInstituicao(), instituicaoPeriodo.getCdPeriodoLetivo(), true);
				while(rsmCursos.next()){
					Curso curso = CursoDAO.get(rsmCursos.getInt("cd_curso"), connect);
					
					ResultSetMap rsmTurmas = TurmaServices.getAllByCurso(instituicao.getCdInstituicao(), curso.getCdCurso(), instituicaoPeriodo.getCdPeriodoLetivo());
					while(rsmTurmas.next()){
						
						Turma turma = TurmaDAO.get(rsmTurmas.getInt("cd_turma"), connect);
						
						ResultSetMap rsmAlunos = getAlunosSituacaoAluno(turma.getCdTurma(), instituicaoPeriodo.getCdPeriodoLetivo());
						while(rsmAlunos.next()){
							
							int stAlunoCenso = rsmAlunos.getInt("st_aluno_censo");
							
							if(stAlunoCenso == MatriculaServices.ST_ALUNO_CENSO_APROVADO){
								qtAprovadosTotal++;
								qtAprovadosEscola++;
								
								if(curso.getCdCurso() == 1149 || 
									curso.getCdCurso() == 	1151 || 
									curso.getCdCurso() == 	1153 || 
									curso.getCdCurso() == 	1155 || 
									curso.getCdCurso() == 	1157 || 
									
									curso.getCdCurso() ==   1161 || 
									curso.getCdCurso() == 	1163 || 
									curso.getCdCurso() == 	1165 || 
									curso.getCdCurso() == 	1167 || 
									curso.getCdCurso() == 	1169){
									qtAprovadosTotalFund1++;
								}
								
								if(curso.getCdCurso() == 1157 || curso.getCdCurso() == 1169){
									qtAprovadosTotal5Ano++;
								}
								
							}
							
							else if(stAlunoCenso == MatriculaServices.ST_ALUNO_CENSO_REPROVADO){
								qtReprovadosTotal++;
								qtReprovadosEscola++;
								
								if(curso.getCdCurso() == 1149 || 
									curso.getCdCurso() == 	1151 || 
									curso.getCdCurso() == 	1153 || 
									curso.getCdCurso() == 	1155 || 
									curso.getCdCurso() == 	1157 || 
									
									curso.getCdCurso() ==   1161 || 
									curso.getCdCurso() == 	1163 || 
									curso.getCdCurso() == 	1165 || 
									curso.getCdCurso() == 	1167 || 
									curso.getCdCurso() == 	1169){
									qtReprovadosTotalFund1++;
								}
								
								if(curso.getCdCurso() == 1157 || curso.getCdCurso() == 1169){
									qtReprovadosTotal5Ano++;
								}
							}
							
							else if(stAlunoCenso == MatriculaServices.ST_ALUNO_CENSO_DESISTENTE || stAlunoCenso == MatriculaServices.ST_ALUNO_CENSO_EVADIDO){
								qtDeixouDeFrequentarTotal++;
								qtDeixouDeFrequentarEscola++;
								
								if(curso.getCdCurso() == 1149 || 
									curso.getCdCurso() == 	1151 || 
									curso.getCdCurso() == 	1153 || 
									curso.getCdCurso() == 	1155 || 
									curso.getCdCurso() == 	1157 || 
									
									curso.getCdCurso() ==   1161 || 
									curso.getCdCurso() == 	1163 || 
									curso.getCdCurso() == 	1165 || 
									curso.getCdCurso() == 	1167 || 
									curso.getCdCurso() == 	1169){
									qtDeixouDeFrequentarTotalFund1++;
								}
								
								if(curso.getCdCurso() == 1157 || curso.getCdCurso() == 1169){
									qtDeixouDeFrequentarTotal5Ano++;
								}
							}
							
							else if(stAlunoCenso == MatriculaServices.ST_ALUNO_CENSO_TRANSFERIDO){
								qtTransferidosTotal++;
								qtTransferidosEscola++;	
								
								if(curso.getCdCurso() == 1149 || 
									curso.getCdCurso() == 	1151 || 
									curso.getCdCurso() == 	1153 || 
									curso.getCdCurso() == 	1155 || 
									curso.getCdCurso() == 	1157 || 
									
									curso.getCdCurso() ==   1161 || 
									curso.getCdCurso() == 	1163 || 
									curso.getCdCurso() == 	1165 || 
									curso.getCdCurso() == 	1167 || 
									curso.getCdCurso() == 	1169){
									qtTransferidosTotalFund1++;
								}
								
								if(curso.getCdCurso() == 1157 || curso.getCdCurso() == 1169){
									qtTransferidosTotal5Ano++;
								}
							}
														
							else if(stAlunoCenso == MatriculaServices.ST_ALUNO_CENSO_FALECIDO){
								qtFalecidosTotal++;
								qtFalecidosEscola++;
								
								if(curso.getCdCurso() == 1149 || 
									curso.getCdCurso() == 	1151 || 
									curso.getCdCurso() == 	1153 || 
									curso.getCdCurso() == 	1155 || 
									curso.getCdCurso() == 	1157 || 
									
									curso.getCdCurso() ==   1161 || 
									curso.getCdCurso() == 	1163 || 
									curso.getCdCurso() == 	1165 || 
									curso.getCdCurso() == 	1167 || 
									curso.getCdCurso() == 	1169){
									qtFalecidosTotalFund1++;
								}
								
								if(curso.getCdCurso() == 1157 || curso.getCdCurso() == 1169){
									qtFalecidosTotal5Ano++;
								}
							}
							
							
						}
						
					}
					
					
				}
				
				registro += "---------------------------------------------\n";
				registro += instituicao.getNmPessoa() + "\n";
				registro += "Aprovados: " + qtAprovadosEscola + "\n";
				registro += "Reprovados: " + qtReprovadosEscola + "\n";
				registro += "Deixou de Frequentar: " + qtDeixouDeFrequentarEscola + "\n";
				registro += "Transferidos: " + qtTransferidosEscola + "\n";
				registro += "Falecidos: " + qtFalecidosEscola + "\n";
				
				System.out.println("---------------------------------------------");
				System.out.println(instituicao.getNmPessoa());
				System.out.println("Aprovados: " + qtAprovadosEscola);
				System.out.println("Reprovados: " + qtReprovadosEscola);
				System.out.println("Deixou de Frequentar: " + qtDeixouDeFrequentarEscola);
				System.out.println("Transferidos: " + qtTransferidosEscola);
				System.out.println("Falecidos: " + qtFalecidosEscola);
				
				
			}
			
			registro += "---------------------------------------------\n";
			registro += "5º ano\n";
			registro += "Aprovados: " + qtAprovadosTotal5Ano + "\n";
			registro += "Reprovados: " + qtReprovadosTotal5Ano + "\n";
			registro += "Deixou de Frequentar: " + qtDeixouDeFrequentarTotal5Ano + "\n";
			registro += "Transferidos: " + qtTransferidosTotal5Ano + "\n";
			registro += "Falecidos: " + qtFalecidosTotal5Ano + "\n";
			
			System.out.println("---------------------------------------------");
			System.out.println("5º ano");
			System.out.println("Aprovados: " + qtAprovadosTotal5Ano);
			System.out.println("Reprovados: " + qtReprovadosTotal5Ano);
			System.out.println("Deixou de Frequentar: " + qtDeixouDeFrequentarTotal5Ano);
			System.out.println("Transferidos: " + qtTransferidosTotal5Ano);
			System.out.println("Falecidos: " + qtFalecidosTotal5Ano);
			
			registro += "---------------------------------------------\n";
			registro += "Fundamental 1\n";
			registro += "Aprovados: " + qtAprovadosTotalFund1 + "\n";
			registro += "Reprovados: " + qtReprovadosTotalFund1 + "\n";
			registro += "Deixou de Frequentar: " + qtDeixouDeFrequentarTotalFund1 + "\n";
			registro += "Transferidos: " + qtTransferidosTotalFund1 + "\n";
			registro += "Falecidos: " + qtFalecidosTotalFund1 + "\n";
			
			System.out.println("---------------------------------------------");
			System.out.println("Fundamental 1");
			System.out.println("Aprovados: " + qtAprovadosTotalFund1);
			System.out.println("Reprovados: " + qtReprovadosTotalFund1);
			System.out.println("Deixou de Frequentar: " + qtDeixouDeFrequentarTotalFund1);
			System.out.println("Transferidos: " + qtTransferidosTotalFund1);
			System.out.println("Falecidos: " + qtFalecidosTotalFund1);
			
			registro += "---------------------------------------------\n";
			registro += "Total geral\n";
			registro += "Aprovados: " + qtAprovadosTotal + "\n";
			registro += "Reprovados: " + qtReprovadosTotal + "\n";
			registro += "Deixou de Frequentar: " + qtDeixouDeFrequentarTotal + "\n";
			registro += "Transferidos: " + qtTransferidosTotal + "\n";
			registro += "Falecidos: " + qtFalecidosTotal + "\n";
			
			System.out.println("---------------------------------------------");
			System.out.println("Total geral");
			System.out.println("Aprovados: " + qtAprovadosTotal);
			System.out.println("Reprovados: " + qtReprovadosTotal);
			System.out.println("Deixou de Frequentar: " + qtDeixouDeFrequentarTotal);
			System.out.println("Transferidos: " + qtTransferidosTotal);
			System.out.println("Falecidos: " + qtFalecidosTotal);
			
		
			
			gravar.write(registro.getBytes());
			gravar.close();
			
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);			
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAlunosSituacaoAluno(int cdTurma, int cdPeriodoLetivo)	{
		return getAlunosSituacaoAluno(0, cdTurma, cdPeriodoLetivo, -1);
	}
	
	public static ResultSetMap getAlunosSituacaoAlunoByInstituicao(int cdInstituicao, int cdPeriodoLetivo, int lgCensoPrimeiraFase)	{
		return getAlunosSituacaoAluno(cdInstituicao, 0, cdPeriodoLetivo, lgCensoPrimeiraFase);
	}
	
	/**
	 * Busca os alunos válidos em determinada turma para a segunda fase do censo
	 * Para isso ele retira todos os alunos que foram transferidos antes da primeira fase, mas retorna os alunos que foram transferidos ou evadiram ou faleceram entre uma etapa e outra, para indicar o status do mesmo
	 * @param cdInstituicao
	 * @param cdTurma
	 * @param cdPeriodoLetivo
	 * @param lgCensoPrimeiraFase
	 * @return
	 */
	private static ResultSetMap getAlunosSituacaoAluno(int cdInstituicao, int cdTurma, int cdPeriodoLetivo, int lgCensoPrimeiraFase)	{
		Connection connect = Conexao.conectar();
		try	{
			
			ResultSetMap rsm = null;
			String dtReferenciaCenso = ParametroServices.getValorOfParametro("DT_REFERENCIA_CENSO", connect);
			dtReferenciaCenso = dtReferenciaCenso + "/" + Integer.parseInt(InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect).getNmPeriodoLetivo());
			GregorianCalendar dtReferenciaCensoCalendar = Util.convStringToCalendar4(dtReferenciaCenso);
			dtReferenciaCensoCalendar.set(Calendar.HOUR_OF_DAY, 23);
			dtReferenciaCensoCalendar.set(Calendar.MINUTE, 59);
			dtReferenciaCensoCalendar.set(Calendar.SECOND, 59);
			int x = 0;
			
			//Faz a busca de todos os alunos ativos ou concluidos
			rsm = new ResultSetMap(connect.prepareStatement("SELECT A.cd_matricula, A.cd_aluno, A.cd_turma, A.cd_matricula, A.st_matricula, B.nm_pessoa AS nm_aluno, A.nr_matricula, A.st_aluno_censo, A.nr_matricula_censo, AL.nr_inep, D.nm_produto_servico AS NM_CURSO_MATRICULA, PF.nm_mae, PF.dt_nascimento, C.cd_instituicao, C.cd_periodo_letivo " +
					 "FROM acd_matricula A " +
					 "JOIN acd_aluno AL ON (A.cd_aluno = AL.cd_aluno) " +
                     "JOIN grl_pessoa B ON (A.cd_aluno = B.cd_pessoa) " +
                     "JOIN grl_pessoa_fisica PF ON (A.cd_aluno = PF.cd_pessoa) " +
                     "JOIN acd_turma  C ON (A.cd_turma = C.cd_turma) " +
                     "JOIN grl_produto_servico  D ON (A.cd_curso = D.cd_produto_servico) " +
                     "WHERE " + (cdTurma > 0 ? "A.cd_turma = " + cdTurma : (cdInstituicao > 0 ? "C.cd_instituicao = " + cdInstituicao : "")) +
                     "  AND C.cd_curso NOT IN (" + ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0) + ", " + ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0) + ")" +
                     "  AND C.nm_turma NOT LIKE 'TRANS%' " +
                     "  AND A.st_matricula NOT IN ("+MatriculaServices.ST_INATIVO+", "+MatriculaServices.ST_FECHADA+", "+MatriculaServices.ST_REALOCACAO+", "+MatriculaServices.ST_RECLASSIFICACAO+")"+
                     "  AND A.cd_periodo_letivo = " + cdPeriodoLetivo +
                     (lgCensoPrimeiraFase == 1 ? " AND A.dt_matricula <= '" + Util.convCalendarStringSql(dtReferenciaCensoCalendar) + "'" : (lgCensoPrimeiraFase == 0 ? " AND A.dt_matricula > '" + Util.convCalendarStringSql(dtReferenciaCensoCalendar) + "'" : "")) +
                     " ORDER BY B.nm_pessoa").executeQuery());
			while(rsm.next()){
				rsm.setValueToField("DT_NM_NASCIMENTO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_nascimento")));
				rsm.setValueToField("_LABEL", MatriculaServices.situacaoAlunoCenso[rsm.getInt("ST_ALUNO_CENSO")]);
				
				ResultSetMap rsmOcorrenciaMatricula = new ResultSetMap(connect.prepareStatement("SELECT A.*, B.*, TURMA_ORIGEM.cd_instituicao AS CD_INSTITUICAO_ORIGEM, TURMA_DESTINO.cd_instituicao AS CD_INSTITUICAO_DESTINO FROM acd_ocorrencia_matricula A"
						+ "																			JOIN grl_ocorrencia B ON (A.cd_ocorrencia = B.cd_ocorrencia)"
						+ "																			LEFT OUTER JOIN acd_turma TURMA_ORIGEM ON (A.cd_turma_origem = TURMA_ORIGEM.cd_turma)"
						+ "																			LEFT OUTER JOIN acd_turma TURMA_DESTINO ON (A.cd_turma_destino = TURMA_DESTINO.cd_turma)"
						+ "																		WHERE (cd_matricula_origem = "+rsm.getInt("cd_matricula") + " OR cd_matricula_destino = " + rsm.getInt("cd_matricula") + ")"
						+ "																		ORDER BY dt_ocorrencia").executeQuery());
				
				boolean foraDaTurma = false;
				while(rsmOcorrenciaMatricula.next()){
					
					//Analisa a data e as ocorrencias do aluno para saber se o mesmo participou da primeira fase para que entre na segunda
					if(rsmOcorrenciaMatricula.getGregorianCalendar("dt_ocorrencia").before(dtReferenciaCensoCalendar)){
						if(rsmOcorrenciaMatricula.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_EVADIDO)
								|| rsmOcorrenciaMatricula.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FALECIDO)
								|| rsmOcorrenciaMatricula.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_DESISTENTE)
								|| rsmOcorrenciaMatricula.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_INATIVO)
								|| rsmOcorrenciaMatricula.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_CANCELAMENTO_MATRICULA)	
								|| rsmOcorrenciaMatricula.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_SOLICITACAO_TRANSFERENCIA)
								|| (rsmOcorrenciaMatricula.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_CONCLUSAO_TRANSFERENCIA)
										&& rsmOcorrenciaMatricula.getInt("cd_matricula_origem") == rsm.getInt("cd_matricula"))
							){
							foraDaTurma = true;
							
						}
						
					}
					
					if(rsmOcorrenciaMatricula.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_ATIVACAO_MATRICULA)
							|| rsmOcorrenciaMatricula.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_MATRICULA)
							|| (rsmOcorrenciaMatricula.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_CONCLUSAO_TRANSFERENCIA)
									&& rsmOcorrenciaMatricula.getInt("cd_matricula_destino") == rsm.getInt("cd_matricula"))
							|| (rsmOcorrenciaMatricula.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_CONCLUSAO_TRANSFERENCIA)
									&& rsmOcorrenciaMatricula.getInt("cd_matricula_origem") == rsm.getInt("cd_matricula") && rsmOcorrenciaMatricula.getInt("cd_matricula_destino") == 0 && rsmOcorrenciaMatricula.getInt("CD_INSTITUICAO_ORIGEM") != 0 && rsmOcorrenciaMatricula.getInt("CD_INSTITUICAO_ORIGEM") == rsmOcorrenciaMatricula.getInt("CD_INSTITUICAO_DESTINO")
								)
							|| rsmOcorrenciaMatricula.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_CONFIRMACAO_MATRICULA)){
						foraDaTurma = false;
					}
					
				}
				rsmOcorrenciaMatricula.beforeFirst();
				
				
				if(foraDaTurma){
					rsm.deleteRow();
					if(x == 0)
						rsm.beforeFirst();
					else
						rsm.previous();
					continue;
				}
				
				
				//Verifica se a turma já foi fechada ou não, na fase de Situação do Aluno
				ResultSetMap rsmTurmaSituacaoAlunoCenso = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_ocorrencia A, acd_ocorrencia_turma B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO + ") AND cd_turma = "+rsm.getInt("cd_turma")+" order by dt_ocorrencia DESC").executeQuery());
				if(rsmTurmaSituacaoAlunoCenso.size() == 0){
					rsm.setValueToField("ST_TURMA_SITUACAO_ALUNO_CENSO", TurmaServices.ST_TURMA_SITUACAO_ALUNO_CENSO_LIBERADA);
				}
				else{
					if(rsmTurmaSituacaoAlunoCenso.next()){
						if(rsmTurmaSituacaoAlunoCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO)){
							rsm.setValueToField("ST_TURMA_SITUACAO_ALUNO_CENSO", TurmaServices.ST_TURMA_SITUACAO_ALUNO_CENSO_LIBERADA);
						}
						else{
							rsm.setValueToField("ST_TURMA_SITUACAO_ALUNO_CENSO", TurmaServices.ST_TURMA_SITUACAO_ALUNO_CENSO_FECHADA);
						}
					}
				}
				
				ResultSetMap rsmFinalizacaoSituacaoAlunoCenso = new ResultSetMap(connect.prepareStatement("SELECT cd_tipo_ocorrencia FROM grl_ocorrencia A, acd_ocorrencia_instituicao B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_ESCOLA_SITUACAO_ALUNO_CENSO + ") AND cd_instituicao = "+rsm.getInt("cd_instituicao")+" AND cd_periodo_letivo = "+rsm.getInt("cd_periodo_letivo")+" order by dt_ocorrencia DESC").executeQuery());
				if(rsmFinalizacaoSituacaoAlunoCenso.next()){
					if(rsmFinalizacaoSituacaoAlunoCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_SITUACAO_ALUNO_CENSO)){
						rsm.setValueToField("LG_FINALIZADA", 1);
					}
					else{
						rsm.setValueToField("LG_FINALIZADA", 0);
					}
				}
				else{
					rsm.setValueToField("LG_FINALIZADA", 0);
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
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca todos os matriculados de um curso e um turno determinados
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @param cdCurso
	 * @param tpTurno
	 * @return
	 */
	public static int getMatriculadosPorCursoTurno(int cdInstituicao, int cdPeriodoLetivo, int cdCurso, int tpTurno){
		Connection connect = Conexao.conectar();
		try	{
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT COUNT(A.cd_matricula) AS qt_matriculas " +
																		 "FROM acd_matricula A " +
														                    "JOIN acd_turma C ON (A.cd_turma = C.cd_turma) " +
														                    "WHERE C.cd_instituicao = "+cdInstituicao +
														                    " AND A.cd_periodo_letivo = " + cdPeriodoLetivo +
														                    " AND C.cd_curso = " + cdCurso +
														                    " AND C.tp_turno = " + tpTurno +
														                    " AND A.st_matricula = " + MatriculaServices.ST_ATIVA +
														                    " AND C.st_turma = " + TurmaServices.ST_ATIVO +
														                    " AND C.nm_turma NOT LIKE 'TRANS%' ").executeQuery());
			
			if(rsm.next()){
				return rsm.getInt("qt_matriculas");
			}
			
			return -1;
			
		}
		catch(Exception e)	{
			
			e.printStackTrace(System.out);
			return -1;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca todos os alunos que entraram na escola depois da primeira fase e não possuem número de INEP
	 * @param cdPeriodoLetivo
	 * @return
	 */
	public static ResultSetMap getAlunosPosCensoSemInep(int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			
			ResultSetMap rsm = null;
			String dtReferenciaCenso = ParametroServices.getValorOfParametro("DT_REFERENCIA_CENSO", connect);
			dtReferenciaCenso = dtReferenciaCenso + "/" + Integer.parseInt(InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect).getNmPeriodoLetivo());
			GregorianCalendar dtReferenciaCensoCalendar = Util.convStringToCalendar4(dtReferenciaCenso);
			ResultSetMap rsmFinal = new ResultSetMap();
			ArrayList<Integer> codigos = new ArrayList<Integer>();
			
			
			rsm = new ResultSetMap(connect.prepareStatement("SELECT A.cd_aluno, A.cd_turma, A.cd_matricula, A.st_matricula, B.nm_pessoa AS nm_aluno, A.nr_matricula, A.st_aluno_censo, D.nm_produto_servico AS NM_CURSO_MATRICULA, PF.nm_mae, PF.dt_nascimento " +
					 "FROM acd_matricula A " +
                    "JOIN grl_pessoa B ON (A.cd_aluno = B.cd_pessoa) " +
                    "JOIN grl_pessoa_fisica PF ON (A.cd_aluno = PF.cd_pessoa) " +
                    "JOIN acd_turma  C ON (A.cd_turma = C.cd_turma) " +
                    "JOIN grl_produto_servico  D ON (A.cd_curso = D.cd_produto_servico) " +
                    "JOIN acd_instituicao_periodo  E ON (C.cd_periodo_letivo = E.cd_periodo_letivo) " +
                    "JOIN acd_aluno F ON (A.cd_aluno = F.cd_aluno) " +
                    "WHERE A.st_matricula NOT IN ("+MatriculaServices.ST_INATIVO+", "+MatriculaServices.ST_FECHADA+") AND (E.cd_periodo_letivo = "+cdPeriodoLetivo+" OR E.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND (F.nr_inep IS NULL OR F.nr_inep = '') " +
                    " ORDER BY B.nm_pessoa").executeQuery());
			while(rsm.next()){
				
				rsm.setValueToField("DT_NM_NASCIMENTO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_nascimento")));
				
				if(!codigos.contains(rsm.getInt("cd_aluno"))){
					rsmFinal.addRegister(rsm.getRegister());
					codigos.add(rsm.getInt("cd_aluno"));
				}
			}
			
			
			rsmFinal.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_ALUNO");
			rsmFinal.orderBy(fields);
			rsmFinal.beforeFirst();
			return rsmFinal;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static Result importacaoDadosSuperior(ResultSetMap rsmHorariosSuperior, ResultSetMap rsmPeriodosSuperior, ResultSetMap rsmCursosSuperior, int cdInstituicao, int cdUsuario){
		return importacaoDadosSuperior(rsmHorariosSuperior, rsmPeriodosSuperior, rsmCursosSuperior, cdInstituicao, cdUsuario, null);
	}
	
	public static Result importacaoDadosSuperior(ResultSetMap rsmHorariosSuperior, ResultSetMap rsmPeriodosSuperior, ResultSetMap rsmCursosSuperior, int cdInstituicao, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			
			criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsmBuscaDelecao = InstituicaoHorarioDAO.find(criterios, connect);
			while(rsmBuscaDelecao.next()){
				Result resultado = InstituicaoHorarioServices.remove(rsmBuscaDelecao.getInt("cd_horario"), cdUsuario, connect);
				if(resultado.getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar instituicao horário: " + resultado.getMessage());
				}
					
			}
			
			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			
			
			while(rsmHorariosSuperior.next()){
				Result resultado = InstituicaoHorarioServices.save(new InstituicaoHorario(0, rsmHorariosSuperior.getInt("tp_turno"), rsmHorariosSuperior.getGregorianCalendar("hr_inicio"), rsmHorariosSuperior.getGregorianCalendar("hr_termino"), rsmHorariosSuperior.getInt("nr_dia_semana"), cdInstituicao, cdPeriodoLetivoAtual), cdUsuario, connect);
				if(resultado.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao salvar horário: " + resultado.getMessage());
				}
			}
			
			rsmBuscaDelecao = InstituicaoPeriodoDAO.find(criterios, connect);
			while(rsmBuscaDelecao.next()){
				Result resultado = InstituicaoPeriodoServices.remove(rsmBuscaDelecao.getInt("cd_periodo_letivo"), cdUsuario, connect);
				if(resultado.getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar instituicao periodo: " + resultado.getMessage());
				}
					
			}
			
			while(rsmPeriodosSuperior.next()){
				Result resultado = InstituicaoPeriodoServices.save(new InstituicaoPeriodo(0, cdInstituicao, rsmPeriodosSuperior.getString("nm_periodo_letivo"), rsmPeriodosSuperior.getGregorianCalendar("hr_inicio"), rsmPeriodosSuperior.getGregorianCalendar("hr_termino"), rsmPeriodosSuperior.getInt("nr_dias_letivo"), rsmPeriodosSuperior.getInt("st_periodo_letivo"), rsmPeriodosSuperior.getInt("cd_tipo_periodo"), rsmPeriodosSuperior.getInt("cd_periodo_letivo")), cdUsuario, connect);
				if(resultado.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao salvar periodo: " + resultado.getMessage());
				}
			}
			
			
			rsmBuscaDelecao = InstituicaoCursoDAO.find(criterios, connect);
			while(rsmBuscaDelecao.next()){
				Result resultado = InstituicaoCursoServices.remove(cdInstituicao, rsmBuscaDelecao.getInt("cd_curso"), cdPeriodoLetivoAtual, cdUsuario, connect);
				if(resultado.getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar instituicao curso: " + resultado.getMessage());
				}
					
			}
			
			while(rsmCursosSuperior.next()){
				Result resultado = InstituicaoCursoServices.save(new InstituicaoCurso(cdInstituicao, rsmCursosSuperior.getInt("cd_curso"), cdPeriodoLetivoAtual), cdUsuario, connect);
				if(resultado.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao salvar curso: " + resultado.getMessage());
				}
			}
			
			if(isConnectionNull)
				connect.commit();
				
			return new Result(1);
			
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	@Deprecated
	public static ResultSetMap find2(ArrayList<ItemComparator> criterios) {
		return find2(criterios, null);
	}

	@Deprecated
	public static ResultSetMap find2(ArrayList<ItemComparator> criterios, Connection connect) {
		
		String limit = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if(criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
				limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}
		ResultSetMap  _rsm = Search.find(
				   "SELECT K.*, A.*, L.*, B.*, M.*, K.nm_razao_social AS nm_empresa, L.nm_pessoa AS nm_fantasia, " +
				   "       C.nm_pessoa AS nm_administrador, " +
				   "       D.nm_pessoa AS nm_coordenador, E.nm_pessoa AS nm_diretor, F.nm_pessoa AS nm_secretario, " +
			       "       G.nm_pessoa AS nm_tesoureiro, H.nm_pessoa AS nm_vice_diretor, I.nm_cidade, J.nm_estado, J.sg_estado, " + 
				   "       P.nm_pessoa AS nm_pessoa_superior " +
			       "FROM grl_pessoa_juridica K " +
			       "JOIN grl_empresa                A ON (A.cd_empresa = K.cd_pessoa) " +
			       "JOIN grl_pessoa                 L ON (L.cd_pessoa = K.cd_pessoa) " +
			       "JOIN acd_instituicao B ON (A.cd_empresa = B.cd_instituicao) " +
			       "LEFT OUTER JOIN grl_pessoa 		C ON (B.cd_administrador = C.cd_pessoa) " +
			       "LEFT OUTER JOIN grl_pessoa 		D ON (B.cd_coordenador = D.cd_pessoa) " +
			       "LEFT OUTER JOIN grl_pessoa 		E ON (B.cd_diretor = E.cd_pessoa) " +
			       "LEFT OUTER JOIN grl_pessoa 		F ON (B.cd_secretario = F.cd_pessoa) " +
			       "LEFT OUTER JOIN grl_pessoa 		G ON (B.cd_tesoureiro = G.cd_pessoa) " +
			       "LEFT OUTER JOIN grl_pessoa 		H ON (B.cd_vice_diretor = H.cd_pessoa) " +
			       "LEFT OUTER JOIN grl_pessoa_endereco M ON (A.cd_empresa = M.cd_pessoa AND M.lg_principal = 1) " +
			       "LEFT OUTER JOIN grl_cidade 		I ON (M.cd_cidade = I.cd_cidade) " +
			       "LEFT OUTER JOIN grl_estado 		J ON (I.cd_estado = J.cd_estado) " +
			       "LEFT OUTER JOIN grl_pessoa 		P ON (L.cd_pessoa_superior = P.cd_pessoa) " +
			       "WHERE 1=1 ", "ORDER BY L.NM_PESSOA" + limit,
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		return _rsm;
	}
	

	public static ResultSetMap findSimplificado(ArrayList<ItemComparator> criterios) {
		return find2(criterios, null);
	}

	
	public static ResultSetMap findSimplificado(ArrayList<ItemComparator> criterios, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int cdPeriodoLetivoOrigem = 0;
			int cdInstituicao = 0;
			int cdPeriodoLetivoAtual = 0;
			for(int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("C.CD_INSTITUICAO")) {
					cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
					ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
					if(rsmPeriodoAtual.next()){
						cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
					}
					criterios.remove(i--);					
				}				
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdPeriodoLetivoOrigem")) {
					cdPeriodoLetivoOrigem = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
			}
			
			String limit = "";
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if(criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
					limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
			}
			
			ResultSetMap rsm = Search.find( "SELECT A.*, L.*, B.*, M.*, K.nm_razao_social AS nm_empresa, L.nm_pessoa AS nm_fantasia, " +
					   "       C.nm_pessoa AS nm_administrador, " +
					   "       D.nm_pessoa AS nm_coordenador, E.nm_pessoa AS nm_diretor, F.nm_pessoa AS nm_secretario, " +
				       "       G.nm_pessoa AS nm_tesoureiro, H.nm_pessoa AS nm_vice_diretor, I.nm_cidade, J.nm_estado, J.sg_estado, " + 
					   "       P.nm_pessoa AS nm_pessoa_superior " +
				       "FROM grl_pessoa_juridica K " +
				       "JOIN grl_empresa                A ON (A.cd_empresa = K.cd_pessoa) " +
				       "JOIN grl_pessoa                 L ON (L.cd_pessoa = K.cd_pessoa) " +
				       "JOIN acd_instituicao B ON (A.cd_empresa = B.cd_instituicao) " +
				       "LEFT OUTER JOIN grl_pessoa 		C ON (B.cd_administrador = C.cd_pessoa) " +
				       "LEFT OUTER JOIN grl_pessoa 		D ON (B.cd_coordenador = D.cd_pessoa) " +
				       "LEFT OUTER JOIN grl_pessoa 		E ON (B.cd_diretor = E.cd_pessoa) " +
				       "LEFT OUTER JOIN grl_pessoa 		F ON (B.cd_secretario = F.cd_pessoa) " +
				       "LEFT OUTER JOIN grl_pessoa 		G ON (B.cd_tesoureiro = G.cd_pessoa) " +
				       "LEFT OUTER JOIN grl_pessoa 		H ON (B.cd_vice_diretor = H.cd_pessoa) " +
				       "LEFT OUTER JOIN grl_pessoa_endereco M ON (A.cd_empresa = M.cd_pessoa AND M.lg_principal = 1) " +
				       "LEFT OUTER JOIN grl_cidade 		I ON (M.cd_cidade = I.cd_cidade) " +
				       "LEFT OUTER JOIN grl_estado 		J ON (I.cd_estado = J.cd_estado) " +
				       "LEFT OUTER JOIN grl_pessoa 		P ON (L.cd_pessoa_superior = P.cd_pessoa) " +
				       "WHERE 1=1 ", "ORDER BY L.NM_PESSOA" + limit,
		            criterios, null, connect!=null ? connect : Conexao.conectar(), connect==null, false);
			while(rsm.next()){
				if(cdInstituicao > 0){
					ResultSetMap rsmPeriodo = InstituicaoPeriodoServices.getPeriodoOfInstituicao(cdInstituicao, cdPeriodoLetivoOrigem, connect);
					if(rsmPeriodo.next())
						rsm.setValueToField("CD_PERIODO_LETIVO_BUSCADO", rsmPeriodo.getInt("cd_periodo_letivo"));
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(SQLException e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	@Deprecated
	public static int update(Instituicao instituicao, boolean noUpdateEmpresa) {
		return update(instituicao, noUpdateEmpresa, null);
	}
	@Deprecated
	public static int update(Instituicao instituicao, boolean noUpdateEmpresa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();

			Empresa empresa = noUpdateEmpresa ? EmpresaDAO.get(instituicao.getCdEmpresa(), connection) : null;
			if (empresa!=null) {
/*
				instituicao.setCdCidade(empresa.getCdCidade());
				instituicao.setNmEmpresa(empresa.getNmEmpresa());
				instituicao.setNmFantasia(empresa.getNmFantasia());
				instituicao.setNmLogradouro(empresa.getNmLogradouro());
				instituicao.setNmBairro(empresa.getNmBairro());
				instituicao.setNrEndereco(empresa.getNrEndereco());
				instituicao.setNmComplemento(empresa.getNmComplemento());
				instituicao.setNrCep(empresa.getNrCep());
				instituicao.setNrCnpj(empresa.getNrCnpj());
				instituicao.setNrTelefone(empresa.getNrTelefone());
				instituicao.setNrTelefone2(empresa.getNrTelefone2());
				instituicao.setNrFax(empresa.getNrFax());
				instituicao.setNmEmail(empresa.getNmEmail());
				instituicao.setDsMascaraCategoria(empresa.getDsMascaraCategoria());
				instituicao.setNmHomepage(empresa.getNmHomepage());
				instituicao.setLgMatriz(empresa.getLgMatriz());
				instituicao.setImgLogomarca(empresa.getImgLogomarca());
				instituicao.setIdEmpresa(empresa.getIdEmpresa());
				instituicao.setNrInscricaoEstadual(empresa.getNrInscricaoEstadual());
				instituicao.setNrInscricaoMunicipal(empresa.getNrInscricaoMunicipal());
				instituicao.setTpEmpresa(empresa.getTpEmpresa());
*/
			}

			return InstituicaoDAO.update(instituicao, connection);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoServices.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Instituicao getByInep(String nrInepColegioDocente) {
		return getByInep(nrInepColegioDocente, null);
	}
	
	/**
	 * Busca a instituição a partir do número de INEP
	 * @param nrInepColegioDocente
	 * @param connect
	 * @return
	 */
	public static Instituicao getByInep(String nrInepColegioDocente, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao WHERE nr_inep=?");
			pstmt.setString(1, nrInepColegioDocente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return InstituicaoDAO.get(rs.getInt("cd_instituicao"), connect);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Instituicao getById(String nrInepColegioDocente) {
		return getById(nrInepColegioDocente, null);
	}
	
	public static Instituicao getById(String nrInepColegioDocente, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao WHERE nr_inep=?");
			pstmt.setString(1, nrInepColegioDocente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return InstituicaoDAO.get(rs.getInt("cd_instituicao"), connect);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método usado para buscar as instituicoes ligadas a uma instituicao superior (incluindo a propria instituicao)
	 * @param cdInstituicaoSuperior
	 * @return
	 */
	public static ResultSetMap getAllInstituicoes(int cdInstituicaoSuperior) {
		return getAllInstituicoes(cdInstituicaoSuperior, null);
	}

	public static ResultSetMap getAllInstituicoes(int cdInstituicaoSuperior, ArrayList<ItemComparator> criterios) {
		return getAllInstituicoes(cdInstituicaoSuperior, criterios, null);
	}
	
	public static ResultSetMap getAllInstituicoes(int cdInstituicaoSuperior, ArrayList<ItemComparator> criterios, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			return Search.find(
					"	SELECT A.*, B.nm_pessoa AS nm_instituicao, B.nr_telefone1 AS nr_telefone, C.nm_pessoa AS nm_diretor, BE.img_logomarca " +
					"FROM acd_instituicao A " +
					"JOIN grl_pessoa B  ON (A.cd_instituicao = B.cd_pessoa) " +
					"JOIN grl_empresa BE ON (A.cd_instituicao = BE.cd_empresa) " +
					"LEFT OUTER JOIN grl_pessoa C ON (A.cd_diretor = C.cd_pessoa) " +
					"WHERE (B.cd_pessoa_superior = "+cdInstituicaoSuperior+" OR A.cd_instituicao = "+cdInstituicaoSuperior+") ", " ORDER BY B.nm_pessoa ",
					criterios, connection!=null ? connection : Conexao.conectar(), connection==null);
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
	
	/**
	 * Método usado para buscar as turmas de uma instituicao de um periodo letivo
	 * @param cdInstituicao
	 * @return
	 */
	public static ResultSetMap getAllTurmasByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo) {
		return getAllTurmasByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, false, null);
	}
	
	public static ResultSetMap getAllTurmasByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, boolean buscarSuperior) {
		return getAllTurmasByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, buscarSuperior, false, null);
	}
	
	public static ResultSetMap getAllTurmasByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, boolean buscarSuperior, Connection connection) {
		return getAllTurmasByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, buscarSuperior, false, connection);
	}
	
	public static ResultSetMap getAllTurmasByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, boolean buscarSuperior, boolean retirarEja) {
		return getAllTurmasByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, buscarSuperior, false, true, 0, -1, null);
	}
	
	public static ResultSetMap getAllTurmasByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, boolean buscarSuperior, boolean retirarEja, boolean buscarMatriculados) {
		return getAllTurmasByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, buscarSuperior, false, buscarMatriculados, 0, -1, null);
	}
	
	public static ResultSetMap getAllTurmasByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, boolean buscarSuperior, boolean retirarEja, boolean buscarMatriculados, int cdCurso) {
		return getAllTurmasByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, buscarSuperior, false, buscarMatriculados, cdCurso, -1, null);
	}

	public static ResultSetMap getAllTurmasByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, boolean buscarSuperior, boolean retirarEja, Connection connection) {
		return getAllTurmasByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, buscarSuperior, retirarEja, true, 0, -1, connection);
	}
	
	public static ResultSetMap getAllTurmasByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, boolean buscarSuperior, boolean retirarEja, int cdCurso, int tpTurno) {
		return getAllTurmasByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, buscarSuperior, retirarEja, true, cdCurso, tpTurno, null);
	}

	public static ResultSetMap getAllTurmasByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, boolean buscarSuperior, boolean retirarEja, boolean buscarMatriculados, int cdCurso, int tpTurno) {
		return getAllTurmasByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, buscarSuperior, retirarEja, buscarMatriculados, cdCurso, tpTurno, null);
	}
	
	public static ResultSetMap getAllTurmasByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, boolean buscarSuperior, boolean retirarEja, boolean buscarMatriculados, int cdCurso, int tpTurno, Connection connection) {
		return getAllTurmasByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, buscarSuperior, retirarEja, buscarMatriculados, cdCurso, tpTurno, false, connection);
	}
	
	public static ResultSetMap getAllTurmasByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, boolean buscarSuperior, boolean retirarEja, boolean buscarMatriculados, int cdCurso, int tpTurno, boolean lgSituacaoAlunoCenso) {
		return getAllTurmasByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, buscarSuperior, retirarEja, buscarMatriculados, cdCurso, tpTurno, lgSituacaoAlunoCenso, null);
	}
	
	public static ResultSetMap getAllTurmasByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, boolean buscarSuperior, boolean retirarEja, boolean buscarMatriculados, int cdCurso, int tpTurno, boolean lgSituacaoAlunoCenso, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_periodo_letivo, D.nm_produto_servico AS nm_curso, D.id_produto_servico AS id_curso, E.nr_ordem, F.*, IP.nm_pessoa AS nm_instituicao, IE.nm_pessoa as nm_extensao " +
					"FROM acd_turma A " +
					"JOIN acd_instituicao_periodo B ON (A.cd_periodo_letivo = B.cd_periodo_letivo) " +
					"JOIN acd_curso C ON (A.cd_curso = C.cd_curso) " +
					"JOIN acd_instituicao_curso IC ON (IC.cd_instituicao = A.cd_instituicao"+ 
					"										AND IC.cd_curso = A.cd_curso"+
					"										AND IC.cd_periodo_letivo = B.cd_periodo_letivo) " +
					"JOIN grl_produto_servico D ON (C.cd_curso = D.cd_produto_servico) " +
					"LEFT OUTER JOIN acd_curso_modulo E ON (A.cd_curso = E.cd_curso " +
					"								AND A.cd_curso_modulo = E.cd_curso_modulo) " +
					"LEFT OUTER JOIN acd_curso_matriz F ON (A.cd_curso = F.cd_curso " +
					"								AND A.cd_matriz = F.cd_matriz) " +
					"JOIN grl_pessoa       IP ON (A.cd_instituicao = IP.cd_pessoa) " +
					"JOIN acd_curso_etapa G ON (A.cd_curso = G.cd_curso) " +
					"JOIN acd_tipo_etapa H ON (G.cd_etapa = H.cd_etapa) " +
					"LEFT OUTER JOIN grl_pessoa IE ON (A.cd_instituicao_ext = IE.cd_pessoa) " +
					"WHERE (B.cd_periodo_letivo = " + cdPeriodoLetivo +(buscarSuperior ? " OR B.cd_periodo_letivo_superior = " + cdPeriodoLetivo : "") + " ) " + 
					(cdInstituicao > 0 ? "  AND (A.cd_instituicao = "+cdInstituicao+(buscarSuperior ? " OR IP.cd_pessoa_superior = " +cdInstituicao : "")+ " ) " : "") +
					(retirarEja ? " AND H.lg_eja = 0 " : " ") +
					(cdCurso > 0 ? " AND A.cd_curso = " + cdCurso : "") +
					(tpTurno > -1 ? " AND A.tp_turno = " + tpTurno : "") +
					" AND A.st_turma <> "+ TurmaServices.ST_INATIVO +
					" AND A.nm_turma NOT LIKE 'TRANS%' " +
					" ORDER BY IP.nm_pessoa, D.nm_produto_servico, A.nm_turma");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int x = 0;
			buscarMatriculados = false;
			while(rsm.next()){
				
				
				if(lgSituacaoAlunoCenso){
					ResultSetMap rsmTurmaSituacaoAlunoCenso = new ResultSetMap(connection.prepareStatement("SELECT * FROM grl_ocorrencia A, acd_ocorrencia_turma B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO + ") AND cd_turma = "+rsm.getInt("cd_turma")+" order by dt_ocorrencia DESC").executeQuery());
					if(rsmTurmaSituacaoAlunoCenso.size() == 0){
						rsm.setValueToField("ST_TURMA_SITUACAO_ALUNO_CENSO", TurmaServices.ST_TURMA_SITUACAO_ALUNO_CENSO_LIBERADA);
					}
					else{
						if(rsmTurmaSituacaoAlunoCenso.next()){
							if(rsmTurmaSituacaoAlunoCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO)){
								rsm.setValueToField("ST_TURMA_SITUACAO_ALUNO_CENSO", TurmaServices.ST_TURMA_SITUACAO_ALUNO_CENSO_LIBERADA);
							}
							else{
								rsm.setValueToField("ST_TURMA_SITUACAO_ALUNO_CENSO", TurmaServices.ST_TURMA_SITUACAO_ALUNO_CENSO_FECHADA);
							}
						}
					}
	
	
					if(rsm.getInt("cd_curso") == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)
					|| rsm.getInt("cd_curso") == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				
				if(buscarMatriculados){
					ResultSetMap rsmAlunos = TurmaServices.getAlunos(rsm.getInt("CD_TURMA"), connection);
					ResultSetMap rsmAlunosAtivos = new ResultSetMap();
					while(rsmAlunos.next()){
						if(rsmAlunos.getInt("st_matricula") == MatriculaServices.ST_ATIVA || rsmAlunos.getInt("st_matricula") == MatriculaServices.ST_CONCLUIDA){
							rsmAlunosAtivos.addRegister(rsmAlunos.getRegister());
						}
					}
					rsmAlunos.beforeFirst();
					rsmAlunosAtivos.beforeFirst();
					rsm.setValueToField("NR_MATRICULADOS", rsmAlunos.size());
					rsm.setValueToField("NR_MATRICULADOS_ATIVOS", rsmAlunosAtivos.size());
				}
				
				rsm.setValueToField("CL_TURNO", TurmaServices.tiposTurno[rsm.getInt("TP_TURNO")]);
				
				rsm.setValueToField("CL_TP_TURNO", TurmaServices.tiposTurno[rsm.getInt("TP_TURNO")]);
				
				rsm.setValueToField("NM_TP_TURNO", TurmaServices.tiposTurno[rsm.getInt("TP_TURNO")]);
				
				rsm.setValueToField("NM_TURMA_COMPLETO", rsm.getString("NM_CURSO") + " - " + rsm.getString("NM_TURMA"));
				
				rsm.setValueToField("NM_TURMA_LABEL", "Turma " + rsm.getString("NM_TURMA"));
				
				x++;
				
			}
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
	
	public static ResultSetMap getAllTurmasByInstituicaoPeriodoSimplificado(int cdInstituicao, int cdPeriodoLetivo) {
		return getAllTurmasByInstituicaoPeriodoSimplificado(cdInstituicao, cdPeriodoLetivo,  -1, null);
	}
	
	public static ResultSetMap getAllTurmasByInstituicaoPeriodoSimplificado(int cdInstituicao, int cdPeriodoLetivo, Connection connection) {
		return getAllTurmasByInstituicaoPeriodoSimplificado(cdInstituicao, cdPeriodoLetivo,  -1, connection);
	}
	
	public static ResultSetMap getAllTurmasByInstituicaoPeriodoSimplificado(int cdInstituicao, int cdPeriodoLetivo, int tpAtendimento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoRecente(cdInstituicao, connection);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, C.nm_pessoa, D.nm_produto_servico AS nm_curso " +
					"FROM acd_turma A " +
					"JOIN acd_instituicao_periodo B ON (A.cd_periodo_letivo = B.cd_periodo_letivo) " +
					"JOIN grl_pessoa C ON (A.cd_instituicao = C.cd_pessoa)"+
					"JOIN grl_produto_servico D ON (A.cd_curso = D.cd_produto_servico) " +
					"WHERE (B.cd_periodo_letivo = " + cdPeriodoLetivo + ") " + 
					"  AND (A.cd_instituicao = "+cdInstituicao+ " ) " +
					"  AND A.st_turma <> "+ TurmaServices.ST_INATIVO +
					"  AND A.nm_turma NOT LIKE 'TRANS%'" +
					(tpAtendimento >= 0 ? " AND A.tp_atendimento = " + tpAtendimento : "")+
					" ORDER BY A.cd_curso, A.nm_turma");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				ResultSetMap rsmAlunos = TurmaServices.getAlunosSimplificado(rsm.getInt("CD_TURMA"), connection);
				rsm.setValueToField("NR_MATRICULADOS", rsmAlunos.size());
				
				rsm.setValueToField("CL_TP_TURNO", TurmaServices.tiposTurno[rsm.getInt("TP_TURNO")]);
			}
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
	
	/**
	 * Método usado para buscar as ofertas do periodo letivo ativo, para que se possa reaproveitar no próximo periodo
	 * @param cdInstituicao
	 * @return
	 */
	public static ResultSetMap getAllOfertasAtivas(int cdInstituicao, int cdPeriodoLetivo, int cdTurma, boolean buscarSuperior) {
		return getAllOfertasAtivas(cdInstituicao, cdPeriodoLetivo, cdTurma, buscarSuperior, null);
	}

	public static ResultSetMap getAllOfertasAtivas(int cdInstituicao, int cdPeriodoLetivo, int cdTurma, boolean buscarSuperior, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, C.nm_turma, C.id_turma, C.qt_vagas, D.nm_pessoa AS nm_professor, F.nm_produto_servico AS nm_curso, H.nm_produto_servico AS nm_disciplina, "+ 
					"													F.id_produto_servico AS id_curso, I.nr_ordem, J.nm_matriz " +
					"FROM acd_oferta A " +
					"JOIN acd_instituicao_periodo B ON (A.cd_periodo_letivo = B.cd_periodo_letivo) " +
					"JOIN acd_turma C ON (A.cd_turma = C.cd_turma) " +
					"LEFT OUTER JOIN grl_pessoa D ON (A.cd_professor = D.cd_pessoa) " +
					"JOIN acd_curso E ON (A.cd_curso = E.cd_curso) " +
					"JOIN grl_produto_servico F ON (E.cd_curso = F.cd_produto_servico) " +
					"JOIN acd_disciplina G ON (A.cd_disciplina = G.cd_disciplina) " +
					"JOIN grl_produto_servico H ON (G.cd_disciplina = H.cd_produto_servico) " +
					"JOIN acd_curso_modulo I ON (A.cd_curso = I.cd_curso " +
					"								AND A.cd_curso_modulo = I.cd_curso_modulo) " +
					"JOIN acd_curso_matriz J ON (A.cd_curso = J.cd_curso " +
					"								AND A.cd_matriz = J.cd_matriz) " +
					"JOIN grl_pessoa L ON (C.cd_instituicao = L.cd_pessoa) " +
					"WHERE (B.cd_periodo_letivo = " + cdPeriodoLetivo + (buscarSuperior ? " OR B.cd_periodo_letivo_superior = " + cdPeriodoLetivo : "") + " ) " + 
					(cdInstituicao > 0 ? "  AND (C.cd_instituicao = "+cdInstituicao+(buscarSuperior ? " OR L.cd_pessoa_superior = "+cdInstituicao : "") + ")" : "") +
					(cdTurma > 0 ? "  AND C.cd_turma = "+cdTurma : "") +
					" ORDER BY F.nm_produto_servico, C.nm_turma, D.nm_pessoa, H.nm_produto_servico");
			
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
	
	/**
	 * Método usado para buscar as matriculas do periodo letivo ativo, para que se possa reaproveitar no próximo periodo
	 * @param cdInstituicao
	 * @return
	 */
	public static ResultSetMap getAllMatriculasAtivas(int cdInstituicao, int cdPeriodoLetivo, int cdTurma, boolean buscarSuperior) {
		return getAllMatriculasAtivas(cdInstituicao, cdPeriodoLetivo, cdTurma, buscarSuperior, false, null);
	}

	public static ResultSetMap getAllMatriculasAtivas(int cdInstituicao, int cdPeriodoLetivo, int cdTurma, boolean buscarSuperior, Connection connection) {
		return getAllMatriculasAtivas(cdInstituicao, cdPeriodoLetivo, cdTurma, buscarSuperior, false, null);
	}
	
	public static ResultSetMap getAllMatriculasAtivas(int cdInstituicao, int cdPeriodoLetivo, int cdTurma, boolean buscarSuperior, boolean buscaSimplificada) {
		return getAllMatriculasAtivas(cdInstituicao, cdPeriodoLetivo, cdTurma, buscarSuperior, buscaSimplificada, null);
	}

	public static ResultSetMap getAllMatriculasAtivas(int cdInstituicao, int cdPeriodoLetivo, int cdTurma, boolean buscarSuperior, boolean buscaSimplificada, Connection connection) {
		return getAllMatriculasAtivas(cdInstituicao, cdPeriodoLetivo, cdTurma, 0, buscarSuperior, buscaSimplificada, false, connection);
	}
	
	public static ResultSetMap getAllMatriculasAtivas(int cdInstituicao, int cdPeriodoLetivo, int cdTurma, boolean buscarSuperior, boolean buscaSimplificada, boolean buscaAtiva) {
		return getAllMatriculasAtivas(cdInstituicao, cdPeriodoLetivo, cdTurma, 0, buscarSuperior, buscaSimplificada, buscaAtiva, (Connection)null);
	}
	
	public static ResultSetMap getAllMatriculasAtivas(int cdInstituicao, int cdPeriodoLetivo, int cdTurma, boolean buscarSuperior, boolean buscaSimplificada, boolean buscaAtiva, Connection connect) {
		return getAllMatriculasAtivas(cdInstituicao, cdPeriodoLetivo, cdTurma, 0, buscarSuperior, buscaSimplificada, buscaAtiva, connect);
	}
	
	public static ResultSetMap getAllMatriculasAtivas(int cdInstituicao, int cdPeriodoLetivo, int cdTurma, int cdCurso, boolean buscarSuperior, boolean buscaSimplificada, boolean buscaAtiva) {
		return getAllMatriculasAtivas(cdInstituicao, cdPeriodoLetivo, cdTurma, cdCurso, buscarSuperior, buscaSimplificada, buscaAtiva, null, null);
	}
	
	public static ResultSetMap getAllMatriculasAtivas(int cdInstituicao, int cdPeriodoLetivo, int cdTurma, int cdCurso, boolean buscarSuperior, boolean buscaSimplificada, boolean buscaAtiva, Connection connection) {
		return getAllMatriculasAtivas(cdInstituicao, cdPeriodoLetivo, cdTurma, cdCurso, buscarSuperior, buscaSimplificada, buscaAtiva, null, connection);
	}

	public static ResultSetMap getAllMatriculasAtivas(int cdInstituicao, int cdPeriodoLetivo, int cdTurma, int cdCurso, boolean buscarSuperior, boolean buscaSimplificada, boolean buscaAtiva, String nmAluno) {
		return getAllMatriculasAtivas(cdInstituicao, cdPeriodoLetivo, cdTurma, cdCurso, buscarSuperior, buscaSimplificada, buscaAtiva, nmAluno, null);
	}
	
	public static ResultSetMap getAllMatriculasAtivas(int cdInstituicao, int cdPeriodoLetivo) {
		return getAllMatriculasAtivas(cdInstituicao, cdPeriodoLetivo);
	}
	
	public static ResultSetMap getAllMatriculasAtivas(int cdInstituicao, int cdPeriodoLetivo, int cdTurma, int cdCurso, boolean buscarSuperior, boolean buscaSimplificada, boolean buscaAtiva, String nmAluno, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			int cdPessoaSuperior = 0;
			if(cdInstituicao > 0){
				Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connection);
				cdPessoaSuperior = instituicao.getCdPessoaSuperior();
			}
				
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoAtual = getPeriodoLetivoRecente(cdInstituicao, connection);
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("sqlAdicional", "AND (J.cd_periodo_letivo = " + cdPeriodoLetivo + (buscarSuperior ? " OR J.cd_periodo_letivo_superior = " + cdPeriodoLetivo : "") + " ) " +
															 (cdPessoaSuperior > 0 ? " AND (G.cd_instituicao = "+cdInstituicao+ (buscarSuperior ? " OR G.cd_instituicao = "+cdPessoaSuperior : "") +")" : ""), ItemComparator.EQUAL, Types.VARCHAR));
			if(cdTurma > 0)
				criterios.add(new ItemComparator("G.cd_turma", "" + cdTurma, ItemComparator.EQUAL, Types.INTEGER));
			
			if(cdCurso > 0)
				criterios.add(new ItemComparator("G.cd_curso", "" + cdCurso, ItemComparator.EQUAL, Types.INTEGER));
			
			if(buscaAtiva)
				criterios.add(new ItemComparator("A.st_matricula", "" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_CONCLUIDA, ItemComparator.IN, Types.INTEGER));
			else{
				criterios.add(new ItemComparator("A.st_matricula", "" + MatriculaServices.ST_TRANSFERIDO, ItemComparator.DIFFERENT, Types.INTEGER));
				criterios.add(new ItemComparator("A.st_matricula", "" + MatriculaServices.ST_EM_TRANSFERENCIA, ItemComparator.DIFFERENT, Types.INTEGER));
				criterios.add(new ItemComparator("A.st_matricula", "" + MatriculaServices.ST_DESISTENTE, ItemComparator.DIFFERENT, Types.INTEGER));
				criterios.add(new ItemComparator("A.st_matricula", "" + MatriculaServices.ST_EVADIDO, ItemComparator.DIFFERENT, Types.INTEGER));
				criterios.add(new ItemComparator("A.st_matricula", "" + MatriculaServices.ST_INATIVO, ItemComparator.DIFFERENT, Types.INTEGER));
				criterios.add(new ItemComparator("A.st_matricula", "" + MatriculaServices.ST_FECHADA, ItemComparator.DIFFERENT, Types.INTEGER));
				criterios.add(new ItemComparator("A.st_matricula", "" + MatriculaServices.ST_CANCELADO, ItemComparator.DIFFERENT, Types.INTEGER));
				criterios.add(new ItemComparator("A.st_matricula", "" + MatriculaServices.ST_REALOCACAO, ItemComparator.DIFFERENT, Types.INTEGER));
				criterios.add(new ItemComparator("A.st_matricula", "" + MatriculaServices.ST_RECLASSIFICACAO, ItemComparator.DIFFERENT, Types.INTEGER));
			}
			
			if(nmAluno != null){
				if(buscaSimplificada)
					criterios.add(new ItemComparator("P.NM_PESSOA", "" + nmAluno, ItemComparator.LIKE_ANY, Types.VARCHAR));
				else
					criterios.add(new ItemComparator("D.NM_PESSOA", "" + nmAluno, ItemComparator.LIKE_ANY, Types.VARCHAR));
			}
			
			if(buscaSimplificada){
				return MatriculaServices.findSimplificado(criterios, connection);
			}
			else{
				return MatriculaServices.find(criterios, connection);
			}
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
	
	public static Result getFormulario(int cdInstituicao) {
		return getFormulario(cdInstituicao, null);
	}
	
	public static Result getFormulario(int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);
			if(instituicao==null) {
				return new Result(-1, "Erro ao buscar tipo de documento.");
			}
			
			if(instituicao.getCdFormulario()<=0) {
				return new Result(-2, "A escola selecionada não possui formulário dinâmico.");
			}			
			int cdFormulario = instituicao.getCdFormulario();
			
			Formulario formulario = FormularioDAO.get(cdFormulario, connect);
			
			/*
			 * Busca dos atributos
			 */
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CD_FORMULARIO", Integer.toString(cdFormulario), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmFormularioAtributo = FormularioAtributoServices.find(criterios, connect);
			
			PreparedStatement pstmtValor = connect.prepareStatement("SELECT * FROM grl_formulario_atributo_valor WHERE cd_formulario_atributo = ? AND cd_pessoa = " + cdInstituicao);
			HashMap<String, Object> hashValorFinal = new HashMap<String, Object>();
			/*
			 * Busca as opcoes dos atributos, caso existam
			 */
			while(rsmFormularioAtributo.next()) {
				int cdFormularioAtributo = rsmFormularioAtributo.getInt("cd_formulario_atributo");
				
				if(rsmFormularioAtributo.getInt("TP_DADO") == FormularioAtributoServices.TP_OPCOES) { //OPCOES
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("CD_FORMULARIO_ATRIBUTO", Integer.toString(cdFormularioAtributo), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmFormularioAtributoOpcao = FormularioAtributoOpcaoServices.find(criterios, connect);
					while(rsmFormularioAtributoOpcao.next()){
						rsmFormularioAtributoOpcao.setValueToField(Integer.toString(cdFormularioAtributo), rsmFormularioAtributoOpcao.getInt("CD_OPCAO"));
					}
					rsmFormularioAtributoOpcao.beforeFirst();
					
					rsmFormularioAtributo.setValueToField("RSM_OPCOES", rsmFormularioAtributoOpcao);
				}
				
				pstmtValor.setInt(1, cdFormularioAtributo);
				ResultSetMap rsmValor = new ResultSetMap(pstmtValor.executeQuery());
				if(rsmValor.next()){
					
					Object txtAtributoValor = rsmValor.getString("TXT_ATRIBUTO_VALOR");
					
					if(rsmFormularioAtributo.getInt("TP_DADO") == FormularioAtributoServices.TP_DATA) {
						txtAtributoValor = Util.convStringToDate(String.valueOf(txtAtributoValor), true);
					}
					
					hashValorFinal.put(rsmValor.getString("CD_FORMULARIO_ATRIBUTO"), txtAtributoValor);
				}
			}
			rsmFormularioAtributo.beforeFirst();
			
			Result r = new Result(1, "");
			
			r.addObject("FORMULARIO", formulario);
			r.addObject("RSM_ATRIBUTOS", rsmFormularioAtributo);
			r.addObject("HASH_VALORES", hashValorFinal);
			
			return r;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveFormulario(int cdInstituicao, HashMap<String, Object> registroFormulario){
		return saveFormulario(cdInstituicao, registroFormulario, null);
	}
	
	public static Result saveFormulario(int cdInstituicao, HashMap<String, Object> registroFormulario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);
			if(instituicao==null) {
				return new Result(-1, "Erro ao buscar tipo de documento.");
			}
			
			if(instituicao.getCdFormulario()<=0) {
				return new Result(-2, "A escola selecionada não possui formulário dinâmico.");
			}			
			
			for(String cdFormularioAtributo : registroFormulario.keySet()){
				String txtAtributoValor = String.valueOf(registroFormulario.get(cdFormularioAtributo));
				
				FormularioAtributo formularioAtributo = FormularioAtributoDAO.get(Integer.parseInt(cdFormularioAtributo), connect);
				
				if(formularioAtributo.getTpDado() == FormularioAtributoServices.TP_DATA && txtAtributoValor.length() == 8){
					txtAtributoValor = txtAtributoValor.substring(0, 2) + "/" + txtAtributoValor.substring(2, 4) + "/" + txtAtributoValor.substring(4, 8);
				}
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_formulario_atributo", cdFormularioAtributo, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_pessoa", "" + instituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmFormularioAtributoValor = FormularioAtributoValorDAO.find(criterios, connect);
				if(rsmFormularioAtributoValor.next()){
					FormularioAtributoValor formularioAtributoValor = FormularioAtributoValorDAO.get(rsmFormularioAtributoValor.getInt("cd_formulario_atributo"), rsmFormularioAtributoValor.getInt("cd_formulario_atributo_valor"), connect);
					formularioAtributoValor.setTxtAtributoValor(txtAtributoValor);
					if(formularioAtributo.getTpDado() == FormularioAtributoServices.TP_OPCOES)
						formularioAtributoValor.setCdOpcao(Integer.parseInt(txtAtributoValor));
					if(FormularioAtributoValorDAO.update(formularioAtributoValor, connect) <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar valor de atributo");
					}
				}
				else{
					int cdOpcao = 0;
					if(formularioAtributo.getTpDado() == FormularioAtributoServices.TP_OPCOES)
						cdOpcao = Integer.parseInt(txtAtributoValor);
					FormularioAtributoValor formularioAtributoValor = new FormularioAtributoValor(Integer.parseInt(cdFormularioAtributo), 0/*cdFormularioAtributoValor*/, 0/*cdProdutoServico*/, cdOpcao, 0/*cdEmpresa*/, 
							0/*cdDocumento*/, txtAtributoValor, instituicao.getCdInstituicao(), 0/*cdPessoaValor*/, 0/*cdCliente*/,
							0/*cdArquivo*/, 0/*cdArquivoDocumento*/);
					if(FormularioAtributoValorDAO.insert(formularioAtributoValor, connect) <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao gravar valor de atributo");
					}
				}
				
			}
			
			Result r = new Result(1, "Formulário salvo com sucesso!");
			return r;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int getQuantCepInvalido(int cdInstituicao, int cdPeriodoLetivo) {
		return getQuantCepInvalido(cdInstituicao, cdPeriodoLetivo, false, null);
	}
	
	public static int getQuantCepInvalido(int cdInstituicao, int cdPeriodoLetivo, boolean lgSomenteUmaOcorrencia) {
		return getQuantCepInvalido(cdInstituicao, cdPeriodoLetivo, lgSomenteUmaOcorrencia, null);
	}
	 
	/**
	 * Faz uma busca de todos os CEPs inválidos dos alunos de uma instituição matriculados em um periodo letivo
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @param lgSomenteUmaOcorrencia
	 * @param connect
	 * @return
	 */
	public static int getQuantCepInvalido(int cdInstituicao, int cdPeriodoLetivo, boolean lgSomenteUmaOcorrencia, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int quantAlunosCepInvalido = 0;
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmInstituicao = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect); 
				if(rsmInstituicao.next())
					cdPeriodoLetivo = rsmInstituicao.getInt("cd_periodo_letivo");
			}
			
			pstmt = connect.prepareStatement("SELECT A.cd_aluno, D.nr_cep FROM acd_aluno A, acd_matricula B, acd_turma C, grl_pessoa_endereco D WHERE A.cd_aluno = B.cd_aluno AND B.cd_turma = C.cd_turma AND A.cd_aluno = D.cd_pessoa AND D.lg_principal = 1 AND C.cd_periodo_letivo = "+cdPeriodoLetivo+" AND C.cd_instituicao = " + cdInstituicao + " AND B.st_matricula IN (" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_CONCLUIDA + ") AND C.st_turma IN (" + TurmaServices.ST_ATIVO + ", " + TurmaServices.ST_CONCLUIDO + ")");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				if(rsm.getString("nr_cep") != null && !rsm.getString("nr_cep").trim().equals("45000000"))
					quantAlunosCepInvalido += (LogradouroServices.getEnderecoByCep(rsm.getString("nr_cep"), connect).size() == 0 ? 1 : 0);
				else
					quantAlunosCepInvalido++; 
				
				if(lgSomenteUmaOcorrencia && quantAlunosCepInvalido > 0)
					return quantAlunosCepInvalido;
			}
			rsm.beforeFirst();
			return quantAlunosCepInvalido;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.getAll: " + sqlExpt);
			return -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.getAll: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int getQuantDataNascimentoInvalido(int cdInstituicao, int cdPeriodoLetivo) {
		return getQuantDataNascimentoInvalido(cdInstituicao, cdPeriodoLetivo, false, null);
	}
	
	public static int getQuantDataNascimentoInvalido(int cdInstituicao, int cdPeriodoLetivo, boolean lgSomenteUmaOcorrencia) {
		return getQuantDataNascimentoInvalido(cdInstituicao, cdPeriodoLetivo, lgSomenteUmaOcorrencia, null);
	}
	
	/**
	 * Faz uma busca de todas as datas de nascimento inválidas dos alunos de uma instituição matriculados em um periodo letivo
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @param lgSomenteUmaOcorrencia
	 * @param connect
	 * @return
	 */
	public static int getQuantDataNascimentoInvalido(int cdInstituicao, int cdPeriodoLetivo, boolean lgSomenteUmaOcorrencia, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int quantAlunosDataNascimentoInvalido = 0;
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmInstituicao = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect); 
				if(rsmInstituicao.next())
					cdPeriodoLetivo = rsmInstituicao.getInt("cd_periodo_letivo");
			}
			
			pstmt = connect.prepareStatement("SELECT A.cd_aluno, D.dt_nascimento FROM acd_aluno A, acd_matricula B, acd_turma C, grl_pessoa_fisica D WHERE A.cd_aluno = B.cd_aluno AND B.cd_turma = C.cd_turma AND A.cd_aluno = D.cd_pessoa AND C.cd_periodo_letivo = "+cdPeriodoLetivo+" AND C.cd_instituicao = " + cdInstituicao + " AND B.st_matricula IN (" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_CONCLUIDA + ") AND C.st_turma IN (" + TurmaServices.ST_ATIVO + ", " + TurmaServices.ST_CONCLUIDO + ")");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery()); 
			while(rsm.next()){
				if(rsm.getString("dt_nascimento") != null)
					quantAlunosDataNascimentoInvalido += (!Util.isDataNascimento(rsm.getString("dt_nascimento")) ? 1 : 0);
				else
					quantAlunosDataNascimentoInvalido++;
				if(lgSomenteUmaOcorrencia && quantAlunosDataNascimentoInvalido > 0)
					return quantAlunosDataNascimentoInvalido;
			}
			rsm.beforeFirst();
			return quantAlunosDataNascimentoInvalido;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.getAll: " + sqlExpt);
			return -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.getAll: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int getQuantNomesInvalido(int cdInstituicao, int cdPeriodoLetivo) {
		return getQuantNomesInvalido(cdInstituicao, cdPeriodoLetivo, false, null);
	}
	
	public static int getQuantNomesInvalido(int cdInstituicao, int cdPeriodoLetivo, boolean lgSomenteUmaOcorrencia) {
		return getQuantNomesInvalido(cdInstituicao, cdPeriodoLetivo, lgSomenteUmaOcorrencia, null);
	}
	
	/**
	 * Faz uma busca de todos os nomes inválidos dos alunos de uma instituição matriculados em um periodo letivo
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @param lgSomenteUmaOcorrencia
	 * @param connect
	 * @return
	 */
	public static int getQuantNomesInvalido(int cdInstituicao, int cdPeriodoLetivo, boolean lgSomenteUmaOcorrencia, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int quantAlunosNomesInvalido = 0;
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmInstituicao = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect); 
				if(rsmInstituicao.next())
					cdPeriodoLetivo = rsmInstituicao.getInt("cd_periodo_letivo");
			}
			
			pstmt = connect.prepareStatement("SELECT A.cd_aluno, D.nm_pessoa, E.nm_mae, E.nm_pai FROM acd_aluno A, acd_matricula B, acd_turma C, grl_pessoa D, grl_pessoa_fisica E WHERE A.cd_aluno = B.cd_aluno AND B.cd_turma = C.cd_turma AND A.cd_aluno = D.cd_pessoa AND A.cd_aluno = E.cd_pessoa AND C.cd_periodo_letivo = "+cdPeriodoLetivo+" AND C.cd_instituicao = " + cdInstituicao + " AND B.st_matricula IN (" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_CONCLUIDA + ") AND C.st_turma IN (" + TurmaServices.ST_ATIVO + ", " + TurmaServices.ST_CONCLUIDO + ")");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery()); 
			while(rsm.next()){
				if(rsm.getString("nm_pessoa") != null && rsm.getString("nm_mae") != null)
					quantAlunosNomesInvalido += (!AlunoServices.isNomesValidos(rsm.getString("nm_pessoa"), rsm.getString("nm_mae"), rsm.getString("nm_pai")) ? 1 : 0);
				else
					quantAlunosNomesInvalido++;
				if(lgSomenteUmaOcorrencia && quantAlunosNomesInvalido > 0)
					return quantAlunosNomesInvalido;
			}
			rsm.beforeFirst();
			return quantAlunosNomesInvalido;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.getAll: " + sqlExpt);
			return -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.getAll: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getReferenciasByCamada(int cdCamada){
		return getReferenciasByCamada(cdCamada, null);
	}
	
	public static ResultSetMap getReferenciasByCamada(int cdCamada, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			pstmt = connect.prepareStatement("SELECT A.cd_referencia, B.*, D.vl_latitude, D.vl_longitude, D.cd_ponto, E.NM_PESSOA, E.CD_PESSOA"
					+ " FROM geo_camada_referencia A, geo_localizacao B, "
					+ " geo_referencia C, geo_ponto D, grl_pessoa E "
					+ " WHERE A.cd_referencia = B. cd_referencia "
					+ " AND A.cd_referencia = C.cd_referencia "
					+ " AND C.cd_referencia = D.cd_referencia "
					+ " AND B.cd_pessoa = E.cd_pessoa "
					+ " AND A.cd_camada = ?");
			pstmt.setInt(1, cdCamada);
			
			ResultSet rs = pstmt.executeQuery();

			
			ResultSetMap rsm = new ResultSetMap(rs);
			
			while(rsm.next()){
				
				int cdPeriodoLetivo = 0;
				ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(rsm.getInt("CD_PESSOA"));
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivo = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
				
				Result resultResumo = getResumo(rsm.getInt("CD_PESSOA"));
				
				HashMap<String, Object> register = (HashMap<String, Object>) resultResumo.getObjects().get("INSTITUICAO_RESUMO");
				
				rsm.setValueToField("NR_ALUNOS", (register != null && register.get("NR_ALUNOS") != null  ? register.get("NR_ALUNOS") : "Não informado"));
				rsm.setValueToField("NR_INEP", (register != null && register.get("NR_INEP") != null  ? register.get("NR_INEP") : "Não informado"));
				rsm.setValueToField("NR_PROFESSORES", (register != null && register.get("NR_PROFESSORES") != null  ? register.get("NR_PROFESSORES") : "Não informado"));
				rsm.setValueToField("NM_GESTOR", (register != null && register.get("NM_GESTOR") != null  ? register.get("NM_GESTOR") : "Não informado"));
				rsm.setValueToField("NM_VICE_DIRETOR", (register != null && register.get("NM_VICE_DIRETOR") != null ? register.get("NM_VICE_DIRETOR") : "Não informado"));
				rsm.setValueToField("NM_SECRETARIO", (register != null && register.get("NM_SECRETARIO") != null  ? register.get("NM_SECRETARIO") : "Não informado"));
				rsm.setValueToField("NM_TP_LOCALIZACAO", (register != null && register.get("NM_TP_LOCALIZACAO") != null  ? register.get("NM_TP_LOCALIZACAO") : "Não informado"));
				rsm.setValueToField("CONJUNTO_ETAPAS", (register != null  && register.get("CONJUNTO_ETAPAS") != null ? register.get("CONJUNTO_ETAPAS") : "Não informado"));
				rsm.setValueToField("NR_SALAS", (register != null  && register.get("NR_SALAS") != null ? register.get("NR_SALAS") : "Não informado"));
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoServices.getReferenciasByCamada: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoServices.getReferenciasByCamada: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ArrayList<Referencia> getReferenciasByCamada(int cdCamada, boolean findEstatistica) {
		return getReferenciasByCamada(cdCamada, findEstatistica, null);
	}
	
	public static ArrayList<Referencia> getReferenciasByCamada(int cdCamada, boolean findEstatistica, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.cd_referencia, B.cd_pessoa "
					+ " FROM geo_camada_referencia A, geo_localizacao B "
					+ " WHERE A.cd_referencia = B. cd_referencia "
					+ " AND A.cd_camada = ?");
			pstmt.setInt(1, cdCamada);
			
			ResultSet rs = pstmt.executeQuery();
			
			ArrayList<Referencia> referencias = new ArrayList<Referencia>();
			Referencia r;
			while(rs.next()){
				r = ReferenciaServices.get(rs.getInt("cd_referencia"), true, connect);
				r.setPontos(PontoServices.getAllByReferencia(rs.getInt("cd_referencia"), connect));
				
				if(findEstatistica) {
					ResultSetMap rsmPeriodo = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(rs.getInt("cd_pessoa"), connect);
					
					ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
					crt.add(new ItemComparator("B.CD_INSTITUICAO", rs.getString("cd_pessoa"), ItemComparator.EQUAL, Types.INTEGER));
					
					if(rsmPeriodo.next())
						crt.add(new ItemComparator("cdPeriodoLetivo", rsmPeriodo.getString("cd_periodo_letivo"), ItemComparator.EQUAL, Types.INTEGER));
					
					ResultSetMap rsm = MatriculaServices.findSecretaria(crt, connect);
					
					if(rsm.next()) {
						String txtEstatistica = "";
						
						txtEstatistica += "<br/><br/><b>Turmas:</b> " + rsm.getInt("NR_TURMAS", 0);
						txtEstatistica += "<br/><b>Vagas:</b> " + rsm.getInt("QT_VAGAS", 0);
						txtEstatistica += "<br/><b>Matriculados:</b> " + rsm.getInt("NR_ALUNOS", 0);
						txtEstatistica += "<br/><b>%Ocupação:</b> " + rsm.getString("PR_ALUNOS_VAGAS", "0%");
						
						
						r.setTxtObservacao(txtEstatistica);
					}
				}
				
				referencias.add(r);
			}
			rs.close();
			
			return referencias;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoServices.getReferenciasByCamada: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoServices.getReferenciasByCamada: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllProfessoresLotados(int cdInstituicao) {
		return getAllProfessoresLotados(cdInstituicao, null);
	}
	
	public static ResultSetMap getAllProfessoresLotados(int cdInstituicao, int cdPeriodoLetivo) {
		return getAllProfessoresLotados(cdInstituicao, false, cdPeriodoLetivo, null);
	}
	
	public static ResultSetMap getAllProfessoresLotados(int cdInstituicao, Connection connect) {
		return getAllProfessoresLotados(cdInstituicao, false, 0, connect);
	}
	
	public static ResultSetMap getAllProfessoresLotados(int cdInstituicao, boolean lancarCenso) {
		return getAllProfessoresLotados(cdInstituicao, lancarCenso, 0, null);
	}
	
	public static ResultSetMap getAllProfessoresLotados(int cdInstituicao, boolean lancarCenso, Connection connect) {
		return getAllProfessoresLotados(cdInstituicao, lancarCenso, 0, connect);
	}
	
	
	/**
	 * Busca todos os professores lotados em uma instituição
	 * No momento está também trazendo o cadastro do Gestor como se fosse um professor sem nenhuma turma vinculada, porém ele deverá ter um cadastro separado
	 * @param cdInstituicao
	 * @param lancarCenso
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAllProfessoresLotados(int cdInstituicao, boolean lancarCenso, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			InstituicaoPeriodo instituicaoPeriodo = null;
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriooAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
				if(rsmPeriooAtual.next())
					instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmPeriooAtual.getInt("cd_periodo_letivo"), connect);
			}
			else{
				instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect);
			}
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.*, C.*, D.*, E.nm_pessoa, E.nm_pessoa AS nm_professor, E.cd_pessoa, F.cd_tipo_admissao_superior, H.nm_vinculo FROM srh_lotacao A"
					+ "											  LEFT OUTER JOIN srh_dados_funcionais B ON (A.cd_matricula = B.cd_matricula) "
					+ "											  LEFT OUTER JOIN grl_setor C ON (A.cd_setor = C.cd_setor) "
					+ "											  JOIN acd_professor D ON (B.cd_pessoa = D.cd_professor) "
					+ "											  JOIN grl_pessoa E ON (D.cd_professor = E.cd_pessoa) "
					+ "											  LEFT OUTER JOIN srh_tipo_admissao F ON (B.cd_tipo_admissao = F.cd_tipo_admissao) "
					+ "											  JOIN grl_pessoa_empresa G ON (D.cd_professor = G.cd_pessoa) "
					+ "											  JOIN grl_vinculo H ON (G.cd_vinculo = H.cd_vinculo) "
					+ "											WHERE C.cd_empresa = " + cdInstituicao 
					+ "											  AND C.nm_setor = 'CORPO DOCENTE'"
					+ " 										  AND H.cd_vinculo = "+ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_PROFESSOR", 0)
					+ "											ORDER BY E.nm_pessoa");
			
			ResultSetMap rsmProfessores = new ResultSetMap(pstmt.executeQuery());
			String dtReferenciaCenso = ParametroServices.getValorOfParametro("DT_REFERENCIA_CENSO", connect);
			dtReferenciaCenso = dtReferenciaCenso + "/" + (cdPeriodoLetivo > 0 ? Integer.parseInt(InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect).getNmPeriodoLetivo()) : Util.getDataAtual().get(Calendar.YEAR));
			GregorianCalendar dtReferenciaCensoCalendar = Util.convStringToCalendar(dtReferenciaCenso);
			
			int x = 0;
			while(rsmProfessores.next()){
				rsmProfessores.setValueToField("FUNCOES", FuncaoServices.getAllFuncaoEducacenso(connect));
				ResultSetMap rsmTipoAdmissao = TipoAdmissaoServices.getAll(connect);
				ResultSetMap rsmTipoAdmissaoFinal = new ResultSetMap();
				while(rsmTipoAdmissao.next()){
					if(!rsmTipoAdmissao.getString("nm_tipo_admissao").contains("terceirizado")){
						rsmTipoAdmissaoFinal.addRegister(rsmTipoAdmissao.getRegister());
					}
				}
				rsmTipoAdmissaoFinal.beforeFirst();
				rsmProfessores.setValueToField("TIPOSADMISSAOEDUCACENSO", rsmTipoAdmissaoFinal);
				if(rsmProfessores.getInt("cd_tipo_admissao_superior") > 0){
					TipoAdmissao tipoAdmissao = TipoAdmissaoDAO.get(rsmProfessores.getInt("cd_tipo_admissao_superior"), connect);
					rsmProfessores.setValueToField("CD_TIPO_ADMISSAO", tipoAdmissao.getCdTipoAdmissao());
					rsmProfessores.setValueToField("NM_TIPO_ADMISSAO", tipoAdmissao.getNmTipoAdmissao());
				}
				
				String nmCursosAtendidos = "";
				ResultSetMap rsmOfertas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_oferta A, acd_turma T, grl_produto_servico PS "
						+ "																WHERE EXISTS (SELECT * FROM acd_pessoa_oferta B "
						+ "																				WHERE A.cd_oferta = B.cd_oferta "
						+ "																				  AND B.cd_pessoa = " + rsmProfessores.getInt("cd_professor") 
						+ " 																			  AND B.st_pessoa_oferta = "+PessoaOfertaServices.ST_ATIVO+") "
						+ "																  AND A.cd_turma = T.cd_turma "
						+(lancarCenso ? " 												  AND T.cd_curso <> 1187 " : "")
						+ "																  AND T.cd_instituicao = " + cdInstituicao 
						+ " 															  AND A.st_oferta = " + OfertaServices.ST_ATIVO 
						+ " 															  AND A.cd_periodo_letivo = " + instituicaoPeriodo.getCdPeriodoLetivo() 
						+ " 															  AND T.cd_curso = PS.cd_produto_Servico "
						+ "															ORDER BY PS.nm_produto_servico").executeQuery());
				
				
				if(rsmOfertas.size() == 0){
					
					//Apenas o gestor pode estar sem turma vinculada
					ResultSetMap rsmGestor = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_instituicao_educacenso A, acd_instituicao B "
							+ "																WHERE A.cd_instituicao = B.cd_instituicao "
							+ "																  AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE 
							+ " 															  AND A.cd_periodo_letivo = " + instituicaoPeriodo.getCdPeriodoLetivo()
							+ "																  AND B.cd_administrador = " + rsmProfessores.getInt("cd_professor")).executeQuery());
					
					
					if(rsmGestor.size() == 0){
						rsmProfessores.deleteRow();
						if(x == 0)
							rsmProfessores.beforeFirst();
						else
							rsmProfessores.previous();
						continue;
					}
				}
				
				//Busca as turmas e cursos em que o professor possui oferta
				ArrayList<Integer> codigoCursos = new ArrayList<Integer>();
				while(rsmOfertas.next()){
					if(!codigoCursos.contains(rsmOfertas.getInt("cd_curso"))){
						
						Curso curso = CursoDAO.get(rsmOfertas.getInt("cd_curso"), connect);
						
						ResultSetMap rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_turma T WHERE EXISTS (SELECT * FROM acd_oferta A, acd_pessoa_oferta B WHERE A.cd_oferta = B.cd_oferta AND B.cd_pessoa = " + rsmProfessores.getInt("cd_professor") + " AND B.st_pessoa_oferta = "+PessoaOfertaServices.ST_ATIVO+" AND A.cd_turma = T.cd_turma AND A.st_oferta = " + OfertaServices.ST_ATIVO + " ) AND T.cd_instituicao = " + cdInstituicao + " AND T.cd_periodo_letivo = " + instituicaoPeriodo.getCdPeriodoLetivo() + " AND T.cd_curso = " + rsmOfertas.getInt("cd_curso") + " ORDER BY NM_TURMA").executeQuery());
						String nmTurmas = "(";
						while(rsmTurmas.next()){
							if(nmTurmas.equals("(")){
								nmTurmas += rsmTurmas.getString("nm_turma");
							}
							else{
								nmTurmas += ", " + rsmTurmas.getString("nm_turma");
							}
						}
						rsmTurmas.beforeFirst();
						nmTurmas += ")";
						
						if(nmCursosAtendidos.equals("")){
							nmCursosAtendidos = curso.getNmProdutoServico() + " " + nmTurmas;
						}
						else{
							nmCursosAtendidos += ", " + curso.getNmProdutoServico() + " " + nmTurmas;
						}
						
						rsmProfessores.setValueToField("NM_CURSOS_ATENDIDOS", nmCursosAtendidos);
						
						codigoCursos.add(rsmOfertas.getInt("cd_curso"));
					}
				}
				
				//Temporariamente retirado para verificação posterior
				if(false){
					boolean excluido = false;
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_professor", "" + rsmProfessores.getInt("cd_professor"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_PROFESSOR, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmProfessoresOcorrenciaProfessor = OcorrenciaProfessorServices.find(criterios, connect);
					while(rsmProfessoresOcorrenciaProfessor.next()){
						if(rsmProfessoresOcorrenciaProfessor.getGregorianCalendar("dt_ocorrencia").after(dtReferenciaCensoCalendar)){
							rsmProfessores.deleteRow();
							if(x == 0)
								rsmProfessores.beforeFirst();
							else
								rsmProfessores.previous();
							excluido = true;
							break;
						}
					}
					rsmProfessoresOcorrenciaProfessor.beforeFirst();
					
					if(excluido)
						continue;
					
					x++;
				}
				
			}
			rsmProfessores.beforeFirst();
			
			return rsmProfessores;
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
	
		
	/**
	 * Faz a busca de todos os principais funcionários da administração de uma instituição
	 * @param cdInstituicao
	 * @return
	 */
	public static ResultSetMap getAllProfissionaisByInstituicao(int cdInstituicao) {
		Connection connect = Conexao.conectar();
		
		try {
			HashMap<String, Object> result    = new HashMap<String, Object>();
			ResultSetMap            rsmPessoa = new ResultSetMap();			
			

			ArrayList<HashMap<String, Object>> administradores = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> professores     = new ArrayList<HashMap<String, Object>>();
			
			String sqlInsituicao = "SELECT "
								+ " B.nm_pessoa as nm_diretor, "
								+ " C.nm_pessoa as nm_coordenador, "
								+ " D.nm_pessoa as nm_vice_diretor, "
								+ " E.nm_pessoa as nm_secretario, "
								+ " F.nm_pessoa as nm_tesoureiro, "
								+ " G.nm_pessoa as nm_administrador "
								+ " FROM acd_instituicao A"
								+ " LEFT OUTER JOIN grl_pessoa B ON (A.cd_diretor       = B.cd_pessoa) "
								+ " LEFT OUTER JOIN grl_pessoa C ON (A.cd_coordenador   = C.cd_pessoa) "
								+ " LEFT OUTER JOIN grl_pessoa D ON (A.cd_vice_diretor  = D.cd_pessoa) "
								+ " LEFT OUTER JOIN grl_pessoa E ON (A.cd_secretario    = E.cd_pessoa) "
								+ " LEFT OUTER JOIN grl_pessoa F ON (A.cd_tesoureiro    = F.cd_pessoa) "
								+ " LEFT OUTER JOIN grl_pessoa G ON (A.cd_administrador = G.cd_pessoa) "
								+ " WHERE A.cd_instituicao = ?";
			
			PreparedStatement pstmt1 = connect.prepareStatement(sqlInsituicao);
			pstmt1.setInt(1, cdInstituicao);
			
			ResultSetMap rsmAdministradores = new ResultSetMap(pstmt1.executeQuery());
			rsmAdministradores.beforeFirst();
			
			while(rsmAdministradores.next()) {
				
				if(rsmAdministradores.getString("NM_DIRETOR") != null){
					HashMap<String,Object> register = new HashMap<>();
					register.put("NM_PESSOA", rsmAdministradores.getString("NM_DIRETOR"));
					register.put("NM_VINCULO", "Diretor");
					administradores.add(register);
				}
				
				if(rsmAdministradores.getString("NM_COORDENADOR") != null){
					HashMap<String,Object> register = new HashMap<>();
					register.put("NM_PESSOA", rsmAdministradores.getString("NM_COORDENADOR"));
					register.put("NM_VINCULO", "Coordenador");
					administradores.add(register);
				}
				
				if(rsmAdministradores.getString("NM_VICE_DIRETOR") != null){
					HashMap<String,Object> register = new HashMap<>();
					register.put("NM_PESSOA", rsmAdministradores.getString("NM_VICE_DIRETOR"));
					register.put("NM_VINCULO", "Vice Diretor");
					administradores.add(register);
				}
				
				if(rsmAdministradores.getString("NM_SECRETARIO") != null){
					HashMap<String,Object> register = new HashMap<>();
					register.put("NM_PESSOA", rsmAdministradores.getString("NM_SECRETARIO"));
					register.put("NM_VINCULO", "Secretário");
					administradores.add(register);
				}
				
				if(rsmAdministradores.getString("NM_TESOUREIRO") != null){
					HashMap<String,Object> register = new HashMap<>();
					register.put("NM_PESSOA", rsmAdministradores.getString("NM_TESOUREIRO"));
					register.put("NM_VINCULO", "Tesoureiro");
					administradores.add(register);
				}
			}
			result.put("administradores", administradores);
			
			
			ResultSetMap rsmProfessor = getAllProfessoresLotados(cdInstituicao, connect);			
			rsmProfessor.beforeFirst();
			
			while(rsmProfessor.next()) {
				HashMap<String,Object> register = new HashMap<>();
				register.put("NM_PESSOA", rsmProfessor.getString("NM_PESSOA"));
				register.put("NM_VINCULO", rsmProfessor.getString("NM_VINCULO"));
				
				if(register.get("NM_PESSOA") != null)
					professores.add(register);
			}
			result.put("professores", professores);
			
			rsmPessoa.addRegister(result);
			
			return rsmPessoa;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoServices.getAllProfissionaisByInstituicao: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca uma instituição por nome
	 * @param nmInstituicao
	 * @return
	 */
	public static ResultSetMap findInstituicaoByNome(String nmInstituicao) {
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			crt.add(new ItemComparator("L.nm_pessoa", nmInstituicao, ItemComparator.LIKE_ANY, Types.VARCHAR));
			crt.add(new ItemComparator("qtLimite", "10", ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsmPessoa = InstituicaoServices.find(crt, connect);
			
			return rsmPessoa;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getDadosFichaCensoByInstituicao(int cdInstituicao, int cdPeriodoLetivo){
		return getDadosFichaCensoByInstituicao(cdInstituicao, cdPeriodoLetivo, null);
	}
	
	/**
	 * Busca os dados da ficha do censo de todas as turmas e alunos de uma instituição 
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getDadosFichaCensoByInstituicao(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoVigente = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoVigente.next()){
					cdPeriodoLetivo = rsmPeriodoVigente.getInt("cd_periodo_letivo");
				}
				else{
					if(isConnectionNull)
						Conexao.rollback(connect);
					return null;
				}
			}
			
			
			ResultSetMap rsmGeral = new ResultSetMap();
			
			ResultSetMap rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT cd_turma, nm_turma, nm_produto_servico FROM acd_turma A, grl_produto_servico B WHERE A.cd_curso = B.cd_produto_servico AND cd_periodo_letivo = " + cdPeriodoLetivo + " AND st_turma <> " + TurmaServices.ST_INATIVO + " ORDER BY nm_produto_servico, nm_turma").executeQuery());
			while(rsmTurmas.next()){ 
				rsmGeral.getLines().addAll(TurmaServices.getDadosFichaCensoByTurma(rsmTurmas.getInt("cd_turma"), connect).getLines());
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return rsmGeral;
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
	
	public static Result alterarTipoTransporteAlunos(int cdInstituicao, int cdTipoTransporte){
		return alterarTipoTransporteAlunos(cdInstituicao, cdTipoTransporte, null);
	}
	
	/**
	 * Remove todos os tipos de transporte de todos os alunos de uma instituição, e coloca todos com apenas um tipo de transporte
	 * @param cdInstituicao
	 * @param cdTipoTransporte
	 * @param connect
	 * @return
	 */
	public static Result alterarTipoTransporteAlunos(int cdInstituicao, int cdTipoTransporte, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdPeriodoLetivo = 0;
			ResultSetMap rsmPeriodoVigente = getPeriodoLetivoVigente(cdInstituicao, connect);
			if(rsmPeriodoVigente.next()){
				cdPeriodoLetivo = rsmPeriodoVigente.getInt("cd_periodo_letivo");
			}
			else{
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao buscar periodo letivo da instituição");
			}
			
			ResultSetMap rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT cd_turma FROM acd_turma  WHERE cd_periodo_letivo = " + cdPeriodoLetivo + " AND st_turma <> " + TurmaServices.ST_INATIVO).executeQuery());
			while(rsmTurmas.next()){ 
				ResultSetMap rsmAlunos = new ResultSetMap(connect.prepareStatement("SELECT cd_matricula FROM acd_matricula  WHERE cd_periodo_letivo = " + cdPeriodoLetivo + " AND cd_turma = "+rsmTurmas.getInt("cd_turma")+" AND st_matricula IN (" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_CONCLUIDA + ")").executeQuery());
				while(rsmAlunos.next()){
					Matricula matricula = MatriculaDAO.get(rsmAlunos.getInt("cd_matricula"), connect);
					if(matricula.getLgTransportePublico() == 1){
						int ret = connect.prepareStatement("DELETE FROM acd_matricula_tipo_transporte WHERE cd_matricula = " + matricula.getCdMatricula()).executeUpdate();
						if(ret < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar tipo de transporte dos alunos da escola");
						}
						
						if(MatriculaTipoTransporteDAO.insert(new MatriculaTipoTransporte(matricula.getCdMatricula(), cdTipoTransporte), connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar tipo de transporte dos alunos da escola");
						}
					}
				}
			}
			
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tipos de transporte dos alunos atualizados com sucesso!");
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro em InstituicaoServices.alterarTipoTransporteAlunos");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que analisa todas as turmas e horários cadastrados na instituição, e coloca os horário a partir do turno das turmas
	 * @param cdInstituicao
	 * @param cdUsuario
	 * @return
	 */
	public static Result incluirHorariosEmTurmas(int cdInstituicao, int cdUsuario){
		return incluirHorariosEmTurmas(cdInstituicao, cdUsuario, null);
	}
	
	public static Result incluirHorariosEmTurmas(int cdInstituicao, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement pstmtDeleteHorarioTurma = connect.prepareStatement("DELETE FROM acd_turma_horario WHERE cd_turma = ?");
			
			int cdPeriodoLetivo = 0;
			ResultSetMap rsmPeriodoRecente = getPeriodoLetivoRecente(cdInstituicao, connect);
			if(rsmPeriodoRecente.next()){
				cdPeriodoLetivo = rsmPeriodoRecente.getInt("cd_periodo_letivo");
			}
			else{
				ResultSetMap rsmPeriodoVigente = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoVigente.next()){
					cdPeriodoLetivo = rsmPeriodoVigente.getInt("cd_periodo_letivo");
				}
				else{
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao buscar periodo letivo da instituição");
				}
			}
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_horario WHERE cd_instituicao = "+cdInstituicao+" AND cd_periodo_letivo = "+cdPeriodoLetivo+" AND tp_turno = ?");
			ResultSetMap rsmHorarios;
			
			ResultSetMap rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT cd_turma FROM acd_turma  WHERE cd_periodo_letivo = " + cdPeriodoLetivo + " AND st_turma <> " + TurmaServices.ST_INATIVO).executeQuery());
			while(rsmTurmas.next()){
				Turma turma = TurmaDAO.get(rsmTurmas.getInt("cd_turma"), connect);
				
				pstmtDeleteHorarioTurma.setInt(1, turma.getCdTurma());
				
				int ret = pstmtDeleteHorarioTurma.executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar turma horario antigo");
				}
				
				pstmt.setInt(1, turma.getTpTurno());
				rsmHorarios = new ResultSetMap(pstmt.executeQuery());
				while(rsmHorarios.next()){
					if(TurmaHorarioDAO.insert(new TurmaHorario(rsmHorarios.getInt("cd_horario"), turma.getCdTurma()), connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao inserir turma horario");
					}
				}
			}
			
			if(rsmTurmas.size() == 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Não há turmas cadastradas");
			}
			
			int cdTipoOcorrencia = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ADICIONAR_HORARIO_TURMA, connect).getCdTipoOcorrencia();
			
			Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);
			
			OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, instituicao.getCdInstituicao(), "Adicionado todos os horários das turmas da escola " + instituicao.getNmPessoa(), Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicao.getCdInstituicao(), Util.getDataAtual(), cdUsuario, cdPeriodoLetivo);
			OcorrenciaInstituicaoServices.save(ocorrencia, connect);
			
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Horários de turmas atualizados com sucesso!");
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro em InstituicaoServices.incluirHorariosEmTurmas");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result retirarHorariosEmTurmas(int cdInstituicao, int cdUsuario){
		return retirarHorariosEmTurmas(cdInstituicao, cdUsuario, null);
	}
	
	/**
	 * Retira todos os horários de todas as turmas
	 * @param cdInstituicao
	 * @param cdUsuario
	 * @param connect
	 * @return
	 */
	public static Result retirarHorariosEmTurmas(int cdInstituicao, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement pstmtDeleteHorarioTurma = connect.prepareStatement("DELETE FROM acd_turma_horario WHERE cd_turma = ?");
			
			int cdPeriodoLetivo = 0;
			ResultSetMap rsmPeriodoRecente = getPeriodoLetivoRecente(cdInstituicao, connect);
			if(rsmPeriodoRecente.next()){
				cdPeriodoLetivo = rsmPeriodoRecente.getInt("cd_periodo_letivo");
			}
			else{
				ResultSetMap rsmPeriodoVigente = getPeriodoLetivoVigente(cdInstituicao, connect);
				if(rsmPeriodoVigente.next()){
					cdPeriodoLetivo = rsmPeriodoVigente.getInt("cd_periodo_letivo");
				}
				else{
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao buscar periodo letivo da instituição");
				}
			}
			
			ResultSetMap rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT cd_turma FROM acd_turma  WHERE cd_periodo_letivo = " + cdPeriodoLetivo + " AND st_turma <> " + TurmaServices.ST_INATIVO).executeQuery());
			while(rsmTurmas.next()){
				Turma turma = TurmaDAO.get(rsmTurmas.getInt("cd_turma"), connect);
				
				pstmtDeleteHorarioTurma.setInt(1, turma.getCdTurma());
				
				int ret = pstmtDeleteHorarioTurma.executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar turma horario antigo");
				}
				
			}
			rsmTurmas.beforeFirst();
			
			if(rsmTurmas.size() == 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Não há turmas cadastradas");
			}
			
			int cdTipoOcorrencia = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_REMOVER_HORARIO_TURMA, connect).getCdTipoOcorrencia();
			
			Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);
			
			OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, instituicao.getCdInstituicao(), "Removido todos os horários das turmas da escola " + instituicao.getNmPessoa(), Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicao.getCdInstituicao(), Util.getDataAtual(), cdUsuario, cdPeriodoLetivo);
			OcorrenciaInstituicaoServices.save(ocorrencia, connect);
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Horários de turmas retirados com sucesso!");
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro em InstituicaoServices.incluirHorariosEmTurmas");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result validacaoCompletaInconsistencias(int cdUsuario){
		return validacaoCompletaInconsistencias(cdUsuario, null);
	}
	
	/**
	 * Faz a validação completa de todas as escolas a partir das validações necessárias para o Educacenso 
	 * @param cdUsuario
	 * @param connect
	 * @return
	 */
	public static Result validacaoCompletaInconsistencias(int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(cdUsuario != ParametroServices.getValorOfParametroAsInteger("CD_USUARIO_PERMISSAO_ESPECIAL", 0)){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Apenas o usuário especial poderá realizar esta ação");
			}
			
			ResultSetMap rsm = InstituicaoServices.getAllAtivas();
			System.out.println("Total de Escolas " + rsm.size());
			int x = 1;
			while(rsm.next()){
				int cdInstituicao = rsm.getInt("cd_instituicao");
				if(cdInstituicao > 0 && cdInstituicao != ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0)){
					System.out.println(x + ": Validação " + InstituicaoDAO.get(cdInstituicao, connect).getNmPessoa());
					Result ret = validacaoInstituicao(cdInstituicao, 0, cdUsuario, connect);
					if(ret.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return ret;
					}
				}
				x++;
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result ret = new Result(1, "Validação completa");
			return ret;
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro em InstituicaoServices.validacaoInconsistencias");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result validacaoInconsistencias(int cdUsuario){
		return validacaoInconsistencias(0, cdUsuario, null);
	}
	
	public static Result validacaoInconsistencias(int cdUsuario, Connection connect){
		return validacaoInconsistencias(0, cdUsuario, connect);
	}
	
	public static Result validacaoInconsistencias(int cdInstituicao, int cdUsuario){
		return validacaoInconsistencias(cdInstituicao, cdUsuario, null);
	}
	
	/**
	 * Faz a validação de todas as entidades (Escola, Turmas, Alunos e Professores) de uma instituição
	 * @param cdInstituicao
	 * @param cdUsuario
	 * @param connect
	 * @return
	 */
	public static Result validacaoInconsistencias(int cdInstituicao, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsm = new ResultSetMap();
			
			if(cdInstituicao > 0){
				Result ret = validacaoInstituicao(cdInstituicao, 0, cdUsuario, connect);
				if(ret.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return ret;
				}
				
				rsm = (ResultSetMap) ret.getObjects().get("RSM");
			}
			else{
				
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Instituição não passada");
				
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Validação completa", "RSM", rsm);
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro em InstituicaoServices.validacaoInconsistencias");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result validacaoInstituicao(int cdInstituicao, int tpValidacao, int cdUsuario){
		return validacaoInstituicao(cdInstituicao, tpValidacao, cdUsuario, null);
	}
	
	public static Result validacaoInstituicao(int cdInstituicao, int tpValidacao/*0 - Validação Completa, 1 - Validação apenas de Instituicao*/, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			//Instituicao
			Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);
			InstituicaoPeriodo periodoAtual = null;
			ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
			int cdPeriodoLetivoAtual = 0;
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				periodoAtual = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect);
			}
			//Endereco
			ArrayList<ItemComparator> criterios = new ArrayList<>();
			criterios.add(new ItemComparator("cd_pessoa", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmEndereco = PessoaEnderecoDAO.find(criterios, connect);
			PessoaEndereco instituicaoEndereco = null;
			if(rsmEndereco.next()){
				instituicaoEndereco = PessoaEnderecoDAO.get(rsmEndereco.getInt("cd_endereco"), cdInstituicao, connect);
			}
			//Localização
			ResultSetMap rsmLocalizacao = getLocalizacao(cdInstituicao, connect);
			Ponto pontoLocalizacao = null;
			if(rsmLocalizacao.next()){
				pontoLocalizacao = PontoDAO.get(rsmLocalizacao.getInt("cd_ponto"), rsmLocalizacao.getInt("cd_referencia"), connect);
			}
			
			//Apagar as validações anteriores
			connect.prepareStatement("DELETE FROM acd_instituicao_pendencia WHERE cd_instituicao = " + instituicao.getCdInstituicao()).executeUpdate();
			
			System.out.println("Iniciar validacao escola");
			
			ValidatorResult resultadoValidacao = validate(instituicao, instituicaoEndereco, pontoLocalizacao, cdUsuario, GRUPO_VALIDACAO_EDUCACENSO, connect);
			Result resultValidacoesPendencia = InstituicaoPendenciaServices.atualizarValidacaoPendencia(resultadoValidacao, cdInstituicao, 0/*cdTurma*/, 0/*cdAluno*/, 0/*cdProfessor*/, InstituicaoPendenciaServices.TP_REGISTRO_ESCOLA_CENSO, cdUsuario, GRUPO_VALIDACAO_EDUCACENSO, connect);
			if(resultValidacoesPendencia.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return resultValidacoesPendencia;
			}
			
			System.out.println("Termino validacao escola");
			
			System.out.println("Iniciar validacao periodo letivo");
			
			//PeriodoLetivo
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivoAtual, connect);
			Result ret = InstituicaoPeriodoServices.validacaoPeriodoLetivo(instituicaoPeriodo, tpValidacao, cdUsuario, connect);
			if(ret.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return ret;
			}
			
			System.out.println("Termino validacao periodo letivo");
			
			System.out.println("Iniciar validacao educacenso");
			
			//Educacenso
			InstituicaoEducacenso instituicaoEducacenso = InstituicaoEducacensoDAO.get(cdInstituicao, cdPeriodoLetivoAtual, connect);
			ret = InstituicaoEducacensoServices.validacaoEducacenso(instituicaoEducacenso, tpValidacao, cdUsuario, connect);
			if(ret.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return ret;
			}
			
			System.out.println("Termino validacao educacenso");
			
			if(tpValidacao == 0){
				//Turmas
				
				System.out.println("Iniciar validacao turmas");
				
				ret = TurmaServices.validacaoTurmas(cdInstituicao, tpValidacao, cdUsuario, connect);
				if(ret.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return ret;
				}
				
				System.out.println("Termino validacao turmas");
				
				System.out.println("Iniciar validacao professores");
				
				//Professores
				ret = ProfessorServices.validacaoProfessores(cdInstituicao, cdUsuario, connect);
				if(ret.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return ret;
				}
				
				System.out.println("Termino validacao professores");
			}
			
			
			System.out.println("Geracao de ocorrencias");
			
			int cdTipoOcorrenciaInstituicao = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_DECLARAR_PRONTA_MATRICULA_INICIAL, connect).getCdTipoOcorrencia();
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_instituicao", "" + instituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_periodo_letivo", "" + periodoAtual.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + cdTipoOcorrenciaInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmOcorrencia = OcorrenciaInstituicaoServices.find(criterios, connect);
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(
					   "SELECT A.*, B.nm_pessoa AS nm_instituicao " +
				       "FROM acd_instituicao_pendencia A " +
				       "JOIN grl_pessoa                B ON (B.cd_pessoa = A.cd_instituicao) " +
				       "WHERE 1=1 " + (cdInstituicao != ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0) ? " AND A.cd_instituicao = " + cdInstituicao : "") + 
				       " AND A.st_instituicao_pendencia = " + InstituicaoPendenciaServices.ST_PENDENTE).executeQuery());
			if(rsm.size() == 0){
				GregorianCalendar dtGeracao = null;
				if(rsmOcorrencia.next()){
					dtGeracao = rsmOcorrencia.getGregorianCalendar("dt_ocorrencia");
				}
				else{
					dtGeracao = Util.getDataAtual();
					OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, cdInstituicao, "Validação do Educacenso completa para a instituicao " + instituicao.getNmPessoa() + " na etapa de Matrícula Inicial no periodo de " + periodoAtual.getNmPeriodoLetivo(), 
																				Util.getDataAtual(), cdTipoOcorrenciaInstituicao, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicao.getCdInstituicao(), 
																				Util.getDataAtual(), cdUsuario, periodoAtual.getCdPeriodoLetivo());
					ret = OcorrenciaInstituicaoServices.save(ocorrencia, connect);
					if(ret.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return null;
					}
				}
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				
				register.put("TXT_INSTITUICAO_PENDENCIA", "Validação Completa");
				register.put("NM_INSTITUICAO", instituicao.getNmPessoa());
				register.put("CD_INSTITUICAO_PENDENCIA", 0);
				register.put("CD_TIPO", "99");
				register.put("DT_GERACAO", Util.convCalendarStringCompleto(dtGeracao));
				rsm.addRegister(register);
			}
			else{
				if(rsmOcorrencia.next()){
					ret = OcorrenciaInstituicaoServices.remove(rsmOcorrencia.getInt("cd_ocorrencia"), false, connect);
					if(ret.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return null;
					}
					
					int retorno = OcorrenciaServices.delete(rsmOcorrencia.getInt("cd_ocorrencia"), connect);
					if(retorno < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return null;
					}
				}
				
			}
			
			System.out.println("Termino de geracao de ocorrencias");
			
			if(isConnectionNull)
				connect.commit();
			
			Result result =  new Result(1, "Atualização na validação da escola realizada com sucesso");
			return result;
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao gerar validação");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllSimplificado() {
		return getAllSimplificado(null);
	}
	
	/**
	 * Busca todas as instituições de forma simplificada, para melhorar o processamento
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAllSimplificado(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			ResultSetMap rsmPeriodoRecente = getPeriodoLetivoRecente(ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0), connect);
			int cdPeriodoRecenteSecretaria = 0;
			if(rsmPeriodoRecente.next()){
				cdPeriodoRecenteSecretaria = rsmPeriodoRecente.getInt("cd_periodo_letivo");
			}
			
			pstmt = connect.prepareStatement("SELECT A.*, B.*, B.nm_pessoa AS nm_instituicao, "
				    + "B.nm_pessoa AS nm_fantasia, B.nr_telefone1, B.nr_telefone2, B.nr_fax, " 
			        + "B.nm_email, B.nm_url, B.nm_pessoa as nm_pessoa, C.cd_periodo_letivo, " 
			        + "D.cd_endereco, D.ds_endereco, D.cd_tipo_logradouro, D.cd_tipo_endereco, D.cd_logradouro, " 
			        + "D.cd_bairro, D.cd_cidade, D.nm_logradouro, D.nm_bairro, D.nr_cep, D.nr_endereco, " 
			        + "D.nm_complemento, D.nr_telefone, D.nm_ponto_referencia, " 
			        + "E.nm_cidade " 
					+ " FROM acd_instituicao A "
					+ " JOIN grl_pessoa B ON( A.cd_instituicao = B.cd_pessoa ) "
					+ " JOIN acd_instituicao_periodo IP ON( A.cd_instituicao = IP.cd_instituicao AND IP.cd_periodo_letivo_superior = "+cdPeriodoRecenteSecretaria+" ) "
					+ " JOIN acd_instituicao_educacenso C ON( A.cd_instituicao = C.cd_instituicao AND IP.cd_periodo_letivo = C.cd_periodo_letivo) "
					+ " LEFT OUTER JOIN grl_pessoa_endereco D ON (B.cd_pessoa = D.cd_pessoa AND D.lg_principal = 1) "
			        + " LEFT OUTER JOIN grl_cidade E ON (D.cd_cidade = E.cd_cidade)"
					+ " WHERE 1=1 "  
					+ " AND A.cd_instituicao <> " + ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0)  
					+ " AND C.st_instituicao_publica = 1"
					+ " ORDER BY B.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				
				
//				ResultSetMap rsmTurmas = getAllTurmasByInstituicaoPeriodo(rsm.getInt("cd_instituicao"), rsm.getInt("cd_periodo_letivo"));
//				while(rsmTurmas.next()){
//					ResultSetMap rsmAlunos = TurmaServices.getAlunos(rsmTurmas.getInt("cd_turma"), true);
//					rsm.setValueToField("NR_ALUNOS", (rsm.getInt("NR_ALUNOS") + rsmAlunos.size()));
//					rsm.setValueToField("NR_VAGAS", (rsm.getInt("NR_VAGAS") + rsmTurmas.getInt("QT_VAGAS")));
//				}
//				rsmTurmas.beforeFirst();
			}
			rsm.beforeFirst();
			return rsm;
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
	
	public static Result finalizarArquivoEducacensoMatriculaInicial(int cdInstituicao, int cdUsuario){
		return finalizarArquivoEducacensoMatriculaInicial(cdInstituicao, cdUsuario, null);
	}

	/**
	 * Faz a finalização da primeira fase do censo para uma instituição. Indicando que o arquivo já foi enviado e validado no sistema federal.
	 * @param cdInstituicao
	 * @param cdUsuario
	 * @param connect
	 * @return
	 */
	public static Result finalizarArquivoEducacensoMatriculaInicial(int cdInstituicao, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdTipoOcorrenciaInstituicao = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_MATRICULA_INICIAL, connect).getCdTipoOcorrencia();
			Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);
			ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
			InstituicaoPeriodo periodoAtual = null;
			if(rsmPeriodoAtual.next()){
				periodoAtual = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect);
			}
			OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, cdInstituicao, "Finalização para a instituicao " + instituicao.getNmPessoa() + " na etapa de Matrícula Inicial no periodo de " + periodoAtual.getNmPeriodoLetivo(), 
																		Util.getDataAtual(), cdTipoOcorrenciaInstituicao, com.tivic.manager.grl.OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicao.getCdInstituicao(), 
																		Util.getDataAtual(), cdUsuario, periodoAtual.getCdPeriodoLetivo());
			Result ret = OcorrenciaInstituicaoServices.save(ocorrencia, connect);
			if(ret.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao finalizar escola para a etapa de Matrícula Inicial");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Sucesso ao atualizar");
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1,"Erro ao tentar vincular esta instituição a unidade!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result liberarArquivoEducacensoMatriculaInicial(int cdInstituicao, int cdUsuario){
		return liberarArquivoEducacensoMatriculaInicial(cdInstituicao, cdUsuario, null);
	}

	/**
	 * Faz a liberação da instituição da primeira fase, caso seja necessário fazer alguma correção ou validação extra, ou seja preciso refazer o arquivo.
	 * @param cdInstituicao
	 * @param cdUsuario
	 * @param connect
	 * @return
	 */
	public static Result liberarArquivoEducacensoMatriculaInicial(int cdInstituicao, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdTipoOcorrenciaInstituicao = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_ESCOLA_MATRICULA_INICIAL, connect).getCdTipoOcorrencia();
			Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);
			ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
			InstituicaoPeriodo periodoAtual = null;
			if(rsmPeriodoAtual.next()){
				periodoAtual = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect);
			}
			OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, cdInstituicao, "Liberação para a instituicao " + instituicao.getNmPessoa() + " na etapa de Matrícula Inicial no periodo de " + periodoAtual.getNmPeriodoLetivo(), 
																		Util.getDataAtual(), cdTipoOcorrenciaInstituicao, com.tivic.manager.grl.OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicao.getCdInstituicao(), 
																		Util.getDataAtual(), cdUsuario, periodoAtual.getCdPeriodoLetivo());
			Result ret = OcorrenciaInstituicaoServices.save(ocorrencia, connect);
			if(ret.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao liberar escola para a etapa de Matrícula Inicial");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Sucesso ao atualizar");
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1,"Erro ao tentar vincular esta instituição a unidade!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Faz a confecção do arquivo de educacenso para a primeira fase de uma instituição
	 * @param cdInstituicao
	 * @param cdUsuario
	 * @param tpExport
	 * @return
	 */
	public static Result exportEducacenso(int cdInstituicao, int cdUsuario, int tpExport){
		return null;
	}
	
	/**
	 * Faz a confecção do arquivo de educacenso para a segunda fase de uma instituição
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @param cdUsuario
	 * @return
	 */
	public static Result exportEducacensoSituacaoAluno(int cdInstituicao, int cdPeriodoLetivo, int cdUsuario){
		return null;
	}
	
	/**
	 * Faz a confecção de um arquivo dos alunos sem inep de uma instituição
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @return
	 */
	public static Result exportEducacensoInepsRestantes(int cdInstituicao, int cdPeriodoLetivo){
		return null;
	}
	
	public static Result getEscolasSemTurma(int cdPeriodoLetivo) {
		return getEscolasSemTurma(cdPeriodoLetivo, null);
	}
	
	/**
	 * Busca todas as escolas que não tem ainda turma cadastrada em um determinado periodo letivo
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static Result getEscolasSemTurma(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT B.*, C.nm_pessoa AS NM_INSTITUICAO FROM acd_instituicao B, grl_pessoa C, acd_instituicao_educacenso D, acd_instituicao_periodo E "
					+ "						   WHERE B.cd_instituicao = C.cd_pessoa "
					+ "							 AND B.cd_instituicao = D.cd_instituicao"
					+ "							 AND B.cd_instituicao = E.cd_instituicao"
					+ "							 AND (E.cd_periodo_letivo = "+cdPeriodoLetivo+" OR E.cd_periodo_letivo_superior = " + cdPeriodoLetivo + ")"
					+ "							 AND E.cd_periodo_letivo = D.cd_cd_periodo_letivo"
					+ "							 AND D.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "							 AND B.cd_instituicao <> " + cdSecretaria
					+ "							 AND NOT EXISTS (SELECT * FROM acd_turma A "
					+ "											   JOIN acd_instituicao_periodo IP ON (A.cd_periodo_letivo = IP.cd_periodo_letivo)"
					+ "												WHERE A.cd_instituicao = B.cd_instituicao "
					+ "												  AND A.nm_turma <> 'TRANSFERENCIA'"
					+ "												  AND (IP.cd_periodo_letivo = "+cdPeriodoLetivo+" OR IP.cd_periodo_letivo_superior = "+cdPeriodoLetivo+"))"
					+ "						  ORDER BY C.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			ResultSetMap rsmTodasEscolas = getAll(true, true);
			
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "Número de Escolas sem Turmas: " + rsm.size() + "\n"
					+ "Número de Escolas com Turmas: " + (rsmTodasEscolas.size() - rsm.size()));
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
	
	public static Result getEscolasSemMatricula(int cdPeriodoLetivo) {
		return getEscolasSemMatricula(cdPeriodoLetivo, null);
	}
	
	/**
	 * Busca todas as escolas sem nenhuma matrícula registrada em um determinado perioodo letivo
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static Result getEscolasSemMatricula(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT B.*, C.nm_pessoa AS NM_INSTITUICAO FROM acd_instituicao B, grl_pessoa C, acd_instituicao_educacenso D "
					+ "						   WHERE B.cd_instituicao = C.cd_pessoa "
					+ "							 AND B.cd_instituicao = D.cd_instituicao"
					+ "							 AND B.cd_instituicao <> " + cdSecretaria
					+ "							 AND D.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "							 AND NOT EXISTS (SELECT * FROM acd_matricula A "
					+ "											   JOIN acd_turma T ON (A.cd_turma = T.cd_turma)"
					+ "											   JOIN acd_instituicao_periodo IP ON (A.cd_periodo_letivo = IP.cd_periodo_letivo)"
					+ "												WHERE T.cd_instituicao = B.cd_instituicao "
					+ "												  AND (IP.cd_periodo_letivo = "+cdPeriodoLetivo+" OR IP.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")"
					+ "												  AND A.st_matricula IN ("+MatriculaServices.ST_ATIVA+", "+MatriculaServices.ST_CONCLUIDA+"))"
					+ "						  ORDER BY C.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			ResultSetMap rsmTodasEscolas = getAll(true, true);
			
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "Número de Escolas sem Matrículas: " + rsm.size() + "\n"
					+ "Número de Escolas com Matrículas: " + (rsmTodasEscolas.size() - rsm.size()));
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
	
	public static Result getEscolasAee(int cdPeriodoLetivo) {
		return getEscolasAee(cdPeriodoLetivo, null);
	}
	
	/**
	 * Busca todas as escolas que possuem turmas de Atendimento Educacional Especializado
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static Result getEscolasAee(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT A.cd_instituicao, C.nm_pessoa AS NM_INSTITUICAO, COUNT(A.cd_turma) AS QT_TURMAS_AEE FROM acd_turma A, acd_instituicao B, grl_pessoa C, acd_instituicao_educacenso D, acd_instituicao_periodo IP "
					+ "						   WHERE A.cd_instituicao = B.cd_instituicao"
					+ "							 AND B.cd_instituicao = C.cd_pessoa "
					+ "							 AND B.cd_instituicao <> " + cdSecretaria
					+ "							 AND B.cd_instituicao = D.cd_instituicao"
					+ "							 AND A.cd_periodo_letivo = IP.cd_periodo_letivo"
					+ "							 AND D.cd_periodo_letivo = IP.cd_periodo_letivo"
					+ "							 AND D.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "							 AND A.cd_curso = "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)
					+ "							 AND (IP.cd_periodo_letivo = "+cdPeriodoLetivo+" OR IP.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")"
					+ "						  GROUP BY A.cd_instituicao, C.nm_pessoa"
					+ "						  ORDER BY C.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			ResultSetMap rsmTodasEscolas = getAll(true, true);
			
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "Número de Escolas com turmas AEE: " + rsm.size() + "\n"
					+ "Número de Escolas sem turmas AEE: " + (rsmTodasEscolas.size() - rsm.size()));
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
	 * Busca todas as turmas AEE de todas as escolas que possuem turmas de Atendimento Educacional Especializado
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static Result getEscolasAeeTurmas(int cdPeriodoLetivo, int cdInstituicao) {
		return getEscolasAeeTurmas(cdPeriodoLetivo, cdInstituicao, null);
	}
	
	public static Result getEscolasAeeTurmas(int cdPeriodoLetivo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("ACD.st_instituicao_publica", "" + InstituicaoEducacensoServices.ST_EM_ATIVIDADE, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_curso", "" + ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("sqlAdicional", " AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = TurmaServices.find(criterios, connect);
			while(rsm.next()){
				ResultSetMap rsmOferta = TurmaServices.getOfertasByTurma(rsm.getInt("cd_turma"));
				if(rsmOferta.next()){
					Oferta oferta = OfertaDAO.get(rsmOferta.getInt("cd_oferta"), connect);
					Professor professor = ProfessorDAO.get(oferta.getCdProfessor(), connect);
					if(professor != null){
						rsm.setValueToField("NM_PROFESSOR", professor.getNmPessoa());
					}
					else{
						String nmProfessores = "";
						ResultSetMap rsmPessoaOferta = PessoaOfertaServices.getAllByOferta(oferta.getCdOferta(), connect);
						while(rsmPessoaOferta.next()){
							professor = ProfessorDAO.get(rsmPessoaOferta.getInt("cd_pessoa"), connect);
							nmProfessores += professor.getNmPessoa() + ", ";
						}
						nmProfessores = nmProfessores.substring(0, nmProfessores.length()-2);

						rsm.setValueToField("NM_PROFESSOR", nmProfessores);
					}					
				}
				
				rsm.setValueToField("QT_ATENDIMENTOS", TurmaServices.getAtendimentoEspecializadoOf(rsm.getInt("cd_turma")).size());
			}
			rsm.beforeFirst();

			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "Número de turmas AEE: " + rsm.size());
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
	
	
	public static Result getEscolasMaisEducacao(int cdPeriodoLetivo) {
		return getEscolasMaisEducacao(cdPeriodoLetivo, null);
	}
	
	/**
	 * Busca todas as escolas que possuem turmas de Mais Educação
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static Result getEscolasMaisEducacao(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT A.cd_instituicao, C.nm_pessoa AS NM_INSTITUICAO, COUNT(A.cd_turma) AS QT_TURMAS_MAIS_EDUCACAO FROM acd_turma A, acd_instituicao B, grl_pessoa C, acd_instituicao_educacenso D, acd_instituicao_periodo IP "
					+ "						   WHERE A.cd_instituicao = B.cd_instituicao"
					+ "							 AND B.cd_instituicao = C.cd_pessoa "
					+ "							 AND B.cd_instituicao <> " + cdSecretaria
					+ "							 AND B.cd_instituicao = D.cd_instituicao"
					+ "							 AND A.cd_periodo_letivo = IP.cd_periodo_letivo"
					+ "							 AND D.cd_periodo_letivo = IP.cd_periodo_letivo"
					+ "							 AND D.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "							 AND A.cd_curso = "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)
					+ "							 AND (IP.cd_periodo_letivo = "+cdPeriodoLetivo+" OR IP.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")"
					+ "						  GROUP BY A.cd_instituicao, C.nm_pessoa"
					+ "						  ORDER BY C.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			ResultSetMap rsmTodasEscolas = getAll(true, true);
			
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "Número de Escolas com turmas de Mais Educação: " + rsm.size() + "\n"
					+ "Número de Escolas sem turmas de Mais Educação: " + (rsmTodasEscolas.size() - rsm.size()));
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
	
	public static Result getEscolasMaisEducacaoTurmas(int cdPeriodoLetivo, int cdInstituicao) {
		return getEscolasMaisEducacaoTurmas(cdPeriodoLetivo, cdInstituicao, null);
	}
	
	/**
	 * Busca todas as turmas de Mais Educação de todas as escolas que possuem turmas de Atendimento Educacional Especializado
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static Result getEscolasMaisEducacaoTurmas(int cdPeriodoLetivo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("ACD.st_instituicao_publica", "" + InstituicaoEducacensoServices.ST_EM_ATIVIDADE, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_curso", "" + ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("sqlAdicional", " AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = TurmaServices.find(criterios, connect);
			while(rsm.next()){
				ResultSetMap rsmOferta = TurmaServices.getOfertasByTurma(rsm.getInt("cd_turma"));
				if(rsmOferta.next()){
					Oferta oferta = OfertaDAO.get(rsmOferta.getInt("cd_oferta"), connect);
					Professor professor = ProfessorDAO.get(oferta.getCdProfessor(), connect);
					if(professor != null){
						rsm.setValueToField("NM_PROFESSOR", professor.getNmPessoa());
					}
					else{
						String nmProfessores = "";
						ResultSetMap rsmPessoaOferta = PessoaOfertaServices.getAllByOferta(oferta.getCdOferta(), connect);
						while(rsmPessoaOferta.next()){
							professor = ProfessorDAO.get(rsmPessoaOferta.getInt("cd_pessoa"), connect);
							nmProfessores += professor.getNmPessoa() + ", ";
						}
						nmProfessores = nmProfessores.substring(0, nmProfessores.length()-2);

						rsm.setValueToField("NM_PROFESSOR", nmProfessores);
					}
					
				}
				
				rsm.setValueToField("QT_ATIVIDADES", TurmaServices.getAtividadeComplementarOf(rsm.getInt("cd_turma")).size());
			}
			rsm.beforeFirst();

			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "Número de turmas Mais Educação: " + rsm.size());
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
	
	
	public static Result getEscolasInternetBandaLarga(int cdPeriodoLetivo) {
		return getEscolasInternetBandaLarga(cdPeriodoLetivo, null);
	}
	
	/**
	 * Busca a informação de internet de banda larga de todas as escolas
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static Result getEscolasInternetBandaLarga(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT A.cd_instituicao, C.nm_pessoa AS NM_INSTITUICAO, D.LG_INTERNET, D.LG_BANDA_LARGA FROM acd_instituicao A, grl_pessoa C, acd_instituicao_educacenso D, acd_instituicao_periodo IP "
					+ "						   WHERE A.cd_instituicao = C.cd_pessoa "
					+ "							 AND A.cd_instituicao = D.cd_instituicao"
					+ "							 AND A.cd_instituicao <> " + cdSecretaria
					+ "							 AND A.cd_instituicao = IP.cd_instituicao"
					+ "							 AND D.cd_periodo_letivo = IP.cd_periodo_letivo"
					+ "							 AND (IP.cd_periodo_letivo = "+cdPeriodoLetivo+" OR IP.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")"
					+ "							 AND D.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "						  ORDER BY C.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int qtEscolasComInternet = 0;
			int qtEscolasSemInternet = 0;
			int qtEscolasComBandaLarga = 0;
			int qtEscolasSemBandaLarga = 0;
			int qtEscolasComInternetEBandaLarga = 0;
			int qtEscolasSemInternetEBandaLarga = 0;
			while(rsm.next()){
				rsm.setValueToField("CL_LG_INTERNET", (rsm.getInt("lg_internet") == 0 ? "Não" : "Sim"));
				rsm.setValueToField("CL_LG_BANDA_LARGA", (rsm.getInt("lg_banda_larga") == 0 ? "Não" : "Sim"));
				
				if(rsm.getInt("lg_internet") == 1){
					qtEscolasComInternet++;
					if(rsm.getInt("lg_banda_larga") == 1){
						qtEscolasComBandaLarga++;
						qtEscolasComInternetEBandaLarga++;
					}
					else{
						qtEscolasSemBandaLarga++;
					}
				}
				else{
					qtEscolasSemInternet++;
					if(rsm.getInt("lg_banda_larga") == 1){
						qtEscolasComBandaLarga++;
					}
					else{
						qtEscolasSemBandaLarga++;
						qtEscolasSemInternetEBandaLarga++;
					}
				}
			}
			rsm.beforeFirst();

			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "Número de Escolas com Internet: " + qtEscolasComInternet + "\n"
					+ "Número de Escolas sem Internet: " + qtEscolasSemInternet + "\n"
					+ "Número de Escolas com Banda Larga: " + qtEscolasComBandaLarga + "\n"
					+ "Número de Escolas sem Banda Larga: " + qtEscolasSemBandaLarga + "\n"
					+ "Número de Escolas com Internet e Banda Larga: " + qtEscolasComInternetEBandaLarga + "\n"
					+ "Número de Escolas sem Internet e Banda Larga: " + qtEscolasSemInternetEBandaLarga);
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
	
	public static Result getEscolasAbastecimentosEsgotoDestinacao(int cdPeriodoLetivo) {
		return getEscolasAbastecimentosEsgotoDestinacao(cdPeriodoLetivo, null);
	}
	
	/**
	 * Busca a informação de abastecimento de energia, água, esgoto e destinação de lixo de todas as escolas
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static Result getEscolasAbastecimentosEsgotoDestinacao(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT A.cd_instituicao, C.nm_pessoa AS NM_INSTITUICAO FROM acd_instituicao A, grl_pessoa C, acd_instituicao_educacenso D, acd_instituicao_periodo IP "
					+ "						   WHERE A.cd_instituicao = C.cd_pessoa "
					+ "							 AND A.cd_instituicao = D.cd_instituicao"
					+ "							 AND A.cd_instituicao <> " + cdSecretaria
					+ "							 AND A.cd_instituicao = IP.cd_instituicao"
					+ "							 AND D.cd_periodo_letivo = IP.cd_periodo_letivo"
					+ "							 AND (IP.cd_periodo_letivo = "+cdPeriodoLetivo+" OR IP.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")"
					+ "							 AND D.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "						  ORDER BY C.nm_pessoa");
			int qtEscolasAbastecimentoAguaRedePublica= 0;
			int qtEscolasAbastecimentoAguaPocoArtesiano= 0;
			int qtEscolasAbastecimentoAguaCacimba= 0;
			int qtEscolasAbastecimentoAguaFonte= 0;
			int qtEscolasAbastecimentoAguaInexistente= 0;
			
			int qtEscolasAbastecimentoEnergiaRedePublica = 0;
			int qtEscolasAbastecimentoEnergiaGerador = 0;
			int qtEscolasAbastecimentoEnergiaOutros= 0;
			int qtEscolasAbastecimentoEnergiaInexistente= 0;
			
			int qtEscolasEsgotoSanitarioRedePublica= 0;
			int qtEscolasEsgotoSanitarioFossa= 0;
			int qtEscolasEsgotoSanitarioInexistente= 0;
			
			int qtEscolasDestinacaoLixoColeta= 0;
			int qtEscolasDestinacaoLixoQueima= 0;
			int qtEscolasDestinacaoLixoJoga= 0;
			int qtEscolasDestinacaoLixoRecicla= 0;
			int qtEscolasDestinacaoLixoEnterra= 0;
			int qtEscolasDestinacaoLixoOutros= 0;
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				String clAbastecimentoAgua = "";
				ResultSetMap rsmAbastecimentoAgua = getAbastecimentoAgua(rsm.getInt("cd_instituicao"));
				while(rsmAbastecimentoAgua.next()){
					clAbastecimentoAgua += rsmAbastecimentoAgua.getString("nm_abastecimento_agua") + ", ";
					switch(rsmAbastecimentoAgua.getString("id_abastecimento_agua")){
						case InstituicaoEducacensoServices.TP_ABASTECIMENTO_AGUA_REDE_PUBLICA:
							qtEscolasAbastecimentoAguaRedePublica++;
							break;
						case InstituicaoEducacensoServices.TP_ABASTECIMENTO_AGUA_POCO_ARTESIANO:
							qtEscolasAbastecimentoAguaPocoArtesiano++;
							break;
						case InstituicaoEducacensoServices.TP_ABASTECIMENTO_AGUA_CACIMBA:
							qtEscolasAbastecimentoAguaCacimba++;
							break;
						case InstituicaoEducacensoServices.TP_ABASTECIMENTO_AGUA_FONTE:
							qtEscolasAbastecimentoAguaFonte++;
							break;
						case InstituicaoEducacensoServices.TP_ABASTECIMENTO_AGUA_INEXISTENTE:
							qtEscolasAbastecimentoAguaInexistente++;
							break;
					
					}
				}
				clAbastecimentoAgua = clAbastecimentoAgua.substring(0, clAbastecimentoAgua.length() - 2);
				rsm.setValueToField("CL_ABASTECIMENTO_AGUA", clAbastecimentoAgua);
				
				String clAbastecimentoEnergia = "";
				ResultSetMap rsmAbastecimentoEnergia = getAbastecimentoEnergia(rsm.getInt("cd_instituicao"));
				while(rsmAbastecimentoEnergia.next()){
					clAbastecimentoEnergia += rsmAbastecimentoEnergia.getString("nm_abastecimento_energia") + ", ";
					switch(rsmAbastecimentoEnergia.getString("id_abastecimento_energia")){
						case InstituicaoEducacensoServices.TP_ABASTECIMENTO_ENERGIA_REDE_PUBLICA:
							qtEscolasAbastecimentoEnergiaRedePublica++;
							break;
						case InstituicaoEducacensoServices.TP_ABASTECIMENTO_ENERGIA_GERADOR:
							qtEscolasAbastecimentoEnergiaGerador++;
							break;
						case InstituicaoEducacensoServices.TP_ABASTECIMENTO_ENERGIA_OUTRAS:
							qtEscolasAbastecimentoEnergiaOutros++;
							break;
						case InstituicaoEducacensoServices.TP_ABASTECIMENTO_ENERGIA_INEXISTENTE:
							qtEscolasAbastecimentoEnergiaInexistente++;
							break;
					}
				}
				clAbastecimentoEnergia = clAbastecimentoEnergia.substring(0, clAbastecimentoEnergia.length() - 2);
				rsm.setValueToField("CL_ABASTECIMENTO_ENERGIA", clAbastecimentoEnergia);
				
				String clEsgotoSanitario = "";
				ResultSetMap rsmEsgotoSanitario = getEsgotoSanitario(rsm.getInt("cd_instituicao"));
				while(rsmEsgotoSanitario.next()){
					clEsgotoSanitario += rsmEsgotoSanitario.getString("nm_esgoto_sanitario") + ", ";
					switch(rsmEsgotoSanitario.getString("id_esgoto_sanitario")){
						case InstituicaoEducacensoServices.TP_ESGOTO_SANITARIO_REDE_PUBLICA:
							qtEscolasEsgotoSanitarioRedePublica++;
							break;
						case InstituicaoEducacensoServices.TP_ESGOTO_SANITARIO_FOSSA:
							qtEscolasEsgotoSanitarioFossa++;
							break;
						case InstituicaoEducacensoServices.TP_ESGOTO_SANITARIO_INEXISTENTE:
							qtEscolasEsgotoSanitarioInexistente++;
							break;
					}
				}
				clEsgotoSanitario = clEsgotoSanitario.substring(0, clEsgotoSanitario.length() - 2);
				rsm.setValueToField("CL_ESGOTO_SANITARIO", clEsgotoSanitario);
				
				String clDestinacaoLixo = "";
				ResultSetMap rsmDestinacaoLixo = getDestinacaoLixo(rsm.getInt("cd_instituicao"));
				while(rsmDestinacaoLixo.next()){
					clDestinacaoLixo += rsmDestinacaoLixo.getString("nm_destinacao_lixo") + ", ";
					switch(rsmDestinacaoLixo.getString("id_destinacao_lixo")){
						case InstituicaoEducacensoServices.TP_DESTINACAO_LIXO_COLETA:
							qtEscolasDestinacaoLixoColeta++;
							break;
						case InstituicaoEducacensoServices.TP_DESTINACAO_LIXO_QUEIMA:
							qtEscolasDestinacaoLixoQueima++;
							break;
						case InstituicaoEducacensoServices.TP_DESTINACAO_LIXO_OUTRA_AREA:
							qtEscolasDestinacaoLixoJoga++;
							break;
						case InstituicaoEducacensoServices.TP_DESTINACAO_LIXO_RECICLA:
							qtEscolasDestinacaoLixoRecicla++;
							break;
						case InstituicaoEducacensoServices.TP_DESTINACAO_LIXO_ENTERRA:
							qtEscolasDestinacaoLixoEnterra++;
							break;
						case InstituicaoEducacensoServices.TP_DESTINACAO_LIXO_OUTROS:
							qtEscolasDestinacaoLixoOutros++;
							break;
					}
				}
				clDestinacaoLixo = clDestinacaoLixo.substring(0, clDestinacaoLixo.length() - 2);
				rsm.setValueToField("CL_DESTINACAO_LIXO", clDestinacaoLixo);
				
			}
			rsm.beforeFirst();

			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "Abastecimento de Água\n\tEscolas com Rede Pública: " + qtEscolasAbastecimentoAguaRedePublica + "\n"
					+ "\tEscolas com Poço Artesiano: " + qtEscolasAbastecimentoAguaPocoArtesiano + "\n"
					+ "\tEscolas com Cacimba/cisterna/poço: " + qtEscolasAbastecimentoAguaCacimba + "\n"
					+ "\tEscolas com Fonte/rio/igarapé/riacho/córrego: " + qtEscolasAbastecimentoAguaFonte + "\n"
					+ "\tEscolas com Inexistente: " + qtEscolasAbastecimentoAguaInexistente + "\n"
					+ "Abastecimento de Energia\n\tEscolas com Rede Pública: " + qtEscolasAbastecimentoEnergiaRedePublica + "\n"
					+ "\tEscolas com Gerador: " + qtEscolasAbastecimentoEnergiaGerador + "\n"
					+ "\tEscolas com Outras (energias alternativas): " + qtEscolasAbastecimentoEnergiaOutros + "\n"
					+ "\tEscolas com Inexistente: " + qtEscolasAbastecimentoEnergiaInexistente + "\n"
					+ "Esgoto Sanitário\n\tEscolas com Rede Pública: " + qtEscolasEsgotoSanitarioRedePublica + "\n"
					+ "\tEscolas com Fossa: " + qtEscolasEsgotoSanitarioFossa + "\n"
					+ "\tEscolas com Inexistente: " + qtEscolasEsgotoSanitarioInexistente + "\n"
					+ "Destinação de Lixo\n\tEscolas com Coleta Periódica: " + qtEscolasDestinacaoLixoColeta + "\n"
					+ "\tEscolas com Queima: " + qtEscolasDestinacaoLixoQueima + "\n"
					+ "\tEscolas com Jogo em outra área: " + qtEscolasDestinacaoLixoJoga + "\n"
					+ "\tEscolas com Recicla: " + qtEscolasDestinacaoLixoRecicla + "\n"
					+ "\tEscolas com Enterra: " + qtEscolasDestinacaoLixoEnterra + "\n"
					+ "\tEscolas com Outros: " + qtEscolasDestinacaoLixoOutros);
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
	
	public static Result getEscolasComputadores(int cdPeriodoLetivo) {
		return getEscolasComputadores(cdPeriodoLetivo, null);
	}
	

	/**
	 * Busca a informação de computadores cadastrados de todas as escolas
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static Result getEscolasComputadores(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT A.cd_instituicao, C.nm_pessoa AS NM_INSTITUICAO, D.QT_COMPUTADOR_ADMINISTRATIVO, D.QT_COMPUTADOR_ALUNO, E.NR_FUNCIONARIOS, IP.cd_periodo_letivo FROM acd_instituicao A, grl_pessoa C, acd_instituicao_educacenso D, acd_instituicao_periodo IP, grl_pessoa_juridica E "
					+ "						   WHERE A.cd_instituicao = C.cd_pessoa "
					+ "							 AND A.cd_instituicao = D.cd_instituicao"
					+ "							 AND A.cd_instituicao <> " + cdSecretaria
					+ "							 AND A.cd_instituicao = E.cd_pessoa"
					+ "							 AND A.cd_instituicao = IP.cd_instituicao"
					+ "							 AND D.cd_periodo_letivo = IP.cd_periodo_letivo"
					+ "							 AND (IP.cd_periodo_letivo = "+cdPeriodoLetivo+" OR IP.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")"
					+ "							 AND D.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "						  ORDER BY C.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int qtEscolasComComputadoresAdministrativo = 0;
			int qtEscolasSemComputadoresAdministrativo = 0;
			int qtEscolasComComputadoresParaAlunos = 0;
			int qtEscolasSemComputadoresParaAlunos = 0;
			int qtEscolasComComputadoresAdministrativoEAlunos = 0;
			int qtEscolasSemComputadoresAdministrativoEAlunos = 0;
			while(rsm.next()){
				rsm.setValueToField("QT_ALUNOS", getAlunosOf(rsm.getInt("cd_instituicao"), rsm.getInt("cd_periodo_letivo")).size());
				
				if(rsm.getInt("QT_COMPUTADOR_ADMINISTRATIVO") > 0){
					qtEscolasComComputadoresAdministrativo++;
					if(rsm.getInt("QT_COMPUTADOR_ALUNO") > 0){
						qtEscolasComComputadoresParaAlunos++;
						qtEscolasComComputadoresAdministrativoEAlunos++;
					}
					else{
						qtEscolasSemComputadoresParaAlunos++;
					}
				}
				else{
					qtEscolasSemComputadoresAdministrativo++;
					if(rsm.getInt("QT_COMPUTADOR_ALUNO") > 0){
						qtEscolasComComputadoresParaAlunos++;
					}
					else{
						qtEscolasSemComputadoresParaAlunos++;
						qtEscolasSemComputadoresAdministrativoEAlunos++;
					}
				}
				
			}
			rsm.beforeFirst();

			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "Número de Escolas com Computadores Administrativos: " + qtEscolasComComputadoresAdministrativo + "\n"
					+ "Número de Escolas sem Computadores Administrativos: " + qtEscolasSemComputadoresAdministrativo + "\n"
					+ "Número de Escolas com Computadores para alunos: " + qtEscolasComComputadoresParaAlunos + "\n"
					+ "Número de Escolas sem Computadores para alunos: " + qtEscolasSemComputadoresParaAlunos + "\n"
					+ "Número de Escolas com Computadores Administrativos e Computadores para alunos: " + qtEscolasComComputadoresAdministrativoEAlunos + "\n"
					+ "Número de Escolas sem Computadores Administrativos e Computadores para alunos: " + qtEscolasSemComputadoresAdministrativoEAlunos);
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
	 * Método que disponibiliza a carta dos concluintes, que basicamente são os códigos para que o aluno possa se cadastrar no sistema
	 * de matrículas do Estado. Para alunos que irão para o Ensino Médio
	 * @param cdInstituicao
	 * @return
	 */
	public static ResultSetMap getCartaConcluintes(int cdInstituicao) {
		return getCartaConcluintes(cdInstituicao, null);
	}

	public static ResultSetMap getCartaConcluintes(int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdPeriodoLetivo = 0;
			ResultSetMap rsmPeriodo = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao((cdInstituicao > 0 ? cdInstituicao : ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0)), connect);
			if(rsmPeriodo.next())
				cdPeriodoLetivo = rsmPeriodo.getInt("cd_periodo_letivo");
			
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT D.nm_pessoa AS nm_instituicao, B.nm_pessoa AS nm_aluno, A.nr_codigo_estadual "
					+ "														FROM acd_aluno A, grl_pessoa B, acd_matricula M, acd_turma C, grl_pessoa D, acd_instituicao_periodo E "
					+ "													WHERE A.cd_aluno = B.cd_pessoa "
					+ "													  AND A.cd_aluno = M.cd_aluno "
					+ "													  AND M.cd_turma = C.cd_turma "
					+ "													  AND C.cd_instituicao = D.cd_pessoa "
					+ "													  AND M.cd_periodo_letivo = E.cd_periodo_letivo "
					+ "													  AND M.st_matricula IN (" + MatriculaServices.ST_ATIVA + ")"
					+ "													  AND " + (cdInstituicao == ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0) ? "E.cd_periodo_letivo_superior" : "E.cd_periodo_letivo") + " = " + cdPeriodoLetivo
					+ "													  AND M.cd_curso NOT IN("+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)+")" 
					+ " 												  AND nr_codigo_estadual IS NOT NULL").executeQuery());
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_INSTITUICAO");
			fields.add("NM_ALUNO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			return rsm;
		
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
	 * Método que disponibiliza os códigos de transferência (código do aluno + código da matrícula) dos alunos de cada instituição, ou da rede inteira
	 * @param cdInstituicao
	 * @return
	 */
	public static ResultSetMap getCodigosTransferencia(int cdInstituicao) {
		return getCodigosTransferencia(cdInstituicao, null);
	}

	public static ResultSetMap getCodigosTransferencia(int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdPeriodoLetivo = 0;
			ResultSetMap rsmPeriodo = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdInstituicao, connect);
			if(rsmPeriodo.next())
				cdPeriodoLetivo = rsmPeriodo.getInt("cd_periodo_letivo");
			
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT B.nm_pessoa AS NM_ALUNO, M.NR_MATRICULA, F.NM_MAE, F.dt_nascimento, ('' || A.cd_aluno || M.cd_matricula) AS CODIGO_TRANSFERENCIA, D.nm_pessoa AS NM_INSTITUICAO "
					+ "														FROM acd_aluno A, grl_pessoa B, acd_matricula M, acd_turma C, grl_pessoa D, acd_instituicao_periodo E, grl_pessoa_fisica F "
					+ "													WHERE A.cd_aluno = B.cd_pessoa "
					+ "													  AND A.cd_aluno = M.cd_aluno "
					+ "													  AND M.cd_turma = C.cd_turma "
					+ "													  AND C.cd_instituicao = D.cd_pessoa "
					+ "													  AND M.cd_periodo_letivo = E.cd_periodo_letivo "
					+ "													  AND A.cd_aluno = F.cd_pessoa "
					+ "													  AND M.st_matricula = " + MatriculaServices.ST_EM_TRANSFERENCIA
					+ "													  AND E.cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery());
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_ALUNO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			return rsm;
		
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
	
	public static ResultSetMap getAllAlimentacaoEscolar(int cdPeriodoLetivo) {
		return getAllAlimentacaoEscolar(cdPeriodoLetivo, null);
	}


	/**
	 * Busca a informação de alimentação escolar de todas as escolas
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAllAlimentacaoEscolar(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			int quantSim = 0;
			int quantNao = 0;
			pstmt = connect.prepareStatement("SELECT A.lg_alimentacao_escolar FROM acd_instituicao_educacenso A, acd_instituicao_periodo B WHERE A.cd_instituicao = B.cd_instituicao AND (B.cd_periodo_letivo = "+cdPeriodoLetivo+" OR B.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = B.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(rsm.getInt("LG_ALIMENTACAO_ESCOLAR") == 1){
					quantSim++;
				}
				else{
					quantNao++;
				}
			}
			rsm.beforeFirst();
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantSim);
			register.put("NM_LG_ALIMENTACAO_ESCOLAR", "Sim");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantNao);
			register.put("NM_LG_ALIMENTACAO_ESCOLAR", "Não");
			rsmFinal.addRegister(register);
			
			rsmFinal.beforeFirst();
			
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getAllAguaFiltrada(int cdPeriodoLetivo) {
		return getAllAguaFiltrada(cdPeriodoLetivo, null);
	}


	/**
	 * Busca a informação de agua filtrada de todas as escolas
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAllAguaFiltrada(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			int quantSim = 0;
			int quantNao = 0;
			pstmt = connect.prepareStatement("SELECT A.ST_AGUA_CONSUMIDA FROM acd_instituicao_educacenso A, acd_instituicao_periodo B WHERE A.cd_instituicao = B.cd_instituicao AND (B.cd_periodo_letivo = "+cdPeriodoLetivo+" OR B.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = B.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(rsm.getInt("ST_AGUA_CONSUMIDA") == 1){
					quantSim++;
				}
				else{
					quantNao++;
				}
			}
			rsm.beforeFirst();
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantSim);
			register.put("NM_ST_AGUA_CONSUMIDA", "Sim");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantNao);
			register.put("NM_ST_AGUA_CONSUMIDA", "Não");
			rsmFinal.addRegister(register);
			
			rsmFinal.beforeFirst();
			
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static Result getAllServicos(int cdPeriodoLetivo) {
		return getAllServicos(cdPeriodoLetivo, null);
	}

	public static Result getAllServicos(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total de escolas por abastecimento de agua
			HashMap<String, Integer> register = new HashMap<String, Integer>();
			pstmt = connect.prepareStatement("SELECT B.cd_abastecimento_agua, C.nm_abastecimento_agua FROM acd_instituicao_educacenso A, acd_instituicao_abastecimento_agua B, acd_abastecimento_agua C, acd_instituicao_periodo D WHERE A.cd_instituicao = B.cd_instituicao AND  A.cd_periodo_letivo = B.cd_periodo_letivo AND B.cd_abastecimento_agua = C.cd_abastecimento_agua AND A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2 ORDER BY C.cd_abastecimento_agua");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(register.containsKey(rsm.getString("NM_ABASTECIMENTO_AGUA"))){
					register.put(rsm.getString("NM_ABASTECIMENTO_AGUA"), (register.get(rsm.getString("NM_ABASTECIMENTO_AGUA")) + 1));
				}
				else{
					register.put(rsm.getString("NM_ABASTECIMENTO_AGUA"), 1);
				}
			}
			rsm.beforeFirst();
			
			ResultSetMap rsmAbastecimentoAgua = new ResultSetMap();
			for(String key : register.keySet()){
				HashMap<String, Object> registerAbastecimentoAgua = new HashMap<String, Object>();
				registerAbastecimentoAgua.put("NM_ABASTECIMENTO_AGUA", key);
				registerAbastecimentoAgua.put("NR_INSTITUICOES", register.get(key));
				rsmAbastecimentoAgua.addRegister(registerAbastecimentoAgua);
			}
			rsmAbastecimentoAgua.beforeFirst();
			
			
			//total de escolas por abastecimento de energia
			register = new HashMap<String, Integer>();
			pstmt = connect.prepareStatement("SELECT B.cd_abastecimento_energia, C.nm_abastecimento_energia FROM acd_instituicao_educacenso A, acd_instituicao_abastecimento_energia B, acd_abastecimento_energia C, acd_instituicao_periodo D  WHERE A.cd_instituicao = B.cd_instituicao AND  A.cd_periodo_letivo = B.cd_periodo_letivo AND B.cd_abastecimento_energia = C.cd_abastecimento_energia AND A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2 ORDER BY C.cd_abastecimento_energia");
			rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(register.containsKey(rsm.getString("NM_ABASTECIMENTO_ENERGIA"))){
					register.put(rsm.getString("NM_ABASTECIMENTO_ENERGIA"), (register.get(rsm.getString("NM_ABASTECIMENTO_ENERGIA")) + 1));
				}
				else{
					register.put(rsm.getString("NM_ABASTECIMENTO_ENERGIA"), 1);
				}
			}
			rsm.beforeFirst();
			
			ResultSetMap rsmAbastecimentoEnergia = new ResultSetMap();
			for(String key : register.keySet()){
				HashMap<String, Object> registerAbastecimentoEnergia = new HashMap<String, Object>();
				registerAbastecimentoEnergia.put("NM_ABASTECIMENTO_ENERGIA", key);
				registerAbastecimentoEnergia.put("NR_INSTITUICOES", register.get(key));
				rsmAbastecimentoEnergia.addRegister(registerAbastecimentoEnergia);
			}
			rsmAbastecimentoEnergia.beforeFirst();
			
			//total de escolas por esgoto sanitário
			register = new HashMap<String, Integer>();
			pstmt = connect.prepareStatement("SELECT B.cd_esgoto_sanitario, C.nm_esgoto_sanitario FROM acd_instituicao_educacenso A, acd_instituicao_esgoto_sanitario B, acd_esgoto_sanitario C, acd_instituicao_periodo D WHERE A.cd_instituicao = B.cd_instituicao AND  A.cd_periodo_letivo = B.cd_periodo_letivo AND B.cd_esgoto_sanitario = C.cd_esgoto_sanitario AND A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2 ORDER BY C.cd_esgoto_sanitario");
			rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(register.containsKey(rsm.getString("NM_ESGOTO_SANITARIO"))){
					register.put(rsm.getString("NM_ESGOTO_SANITARIO"), (register.get(rsm.getString("NM_ESGOTO_SANITARIO")) + 1));
				}
				else{
					register.put(rsm.getString("NM_ESGOTO_SANITARIO"), 1);
				}
			}
			rsm.beforeFirst();
			
			ResultSetMap rsmEsgotoSanitario = new ResultSetMap();
			for(String key : register.keySet()){
				HashMap<String, Object> registerEsgotoSanitario = new HashMap<String, Object>();
				registerEsgotoSanitario.put("NM_ESGOTO_SANITARIO", key);
				registerEsgotoSanitario.put("NR_INSTITUICOES", register.get(key));
				rsmEsgotoSanitario.addRegister(registerEsgotoSanitario);
			}
			rsmEsgotoSanitario.beforeFirst();
			
			
			//total de escolas por destinação de lixo
			register = new HashMap<String, Integer>();
			pstmt = connect.prepareStatement("SELECT B.cd_destinacao_lixo, C.nm_destinacao_lixo FROM acd_instituicao_educacenso A, acd_instituicao_destinacao_lixo B, acd_destinacao_lixo C, acd_instituicao_periodo D WHERE A.cd_instituicao = B.cd_instituicao AND A.cd_periodo_letivo = B.cd_periodo_letivo AND B.cd_destinacao_lixo = C.cd_destinacao_lixo AND A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2 ORDER BY C.cd_destinacao_lixo");
			rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(register.containsKey(rsm.getString("NM_DESTINACAO_LIXO"))){
					register.put(rsm.getString("NM_DESTINACAO_LIXO"), (register.get(rsm.getString("NM_DESTINACAO_LIXO")) + 1));
				}
				else{
					register.put(rsm.getString("NM_DESTINACAO_LIXO"), 1);
				}
			}
			rsm.beforeFirst();
			
			ResultSetMap rsmDestinacaoLixo = new ResultSetMap();
			for(String key : register.keySet()){
				HashMap<String, Object> registerDestinacaoLixo = new HashMap<String, Object>();
				registerDestinacaoLixo.put("NM_DESTINACAO_LIXO", key);
				registerDestinacaoLixo.put("NR_INSTITUICOES", register.get(key));
				rsmDestinacaoLixo.addRegister(registerDestinacaoLixo);
			}
			rsmDestinacaoLixo.beforeFirst();
			
			
			Result result = new Result(1);
			result.addObject("RSM_POR_ABASTECIMENTO_AGUA", rsmAbastecimentoAgua);
			result.addObject("RSM_POR_ABASTECIMENTO_ENERGIA", rsmAbastecimentoEnergia);
			result.addObject("RSM_POR_ESGOTO_SANITARIO", rsmEsgotoSanitario);
			result.addObject("RSM_POR_DESTINACAO_LIXO", rsmDestinacaoLixo);
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	
	public static ResultSetMap getAllDependenciasDashboard(int cdPeriodoLetivo) {
		return getAllDependenciasDashboard(cdPeriodoLetivo, null);
	}


	/**
	 * Busca a informação de dependencias de todas as escolas
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAllDependenciasDashboard(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			HashMap<String, Integer> register = new HashMap<String, Integer>();
			pstmt = connect.prepareStatement("SELECT C.nm_tipo_dependencia FROM acd_instituicao_educacenso A, acd_instituicao_dependencia B, acd_tipo_dependencia C, acd_instituicao_periodo D WHERE A.cd_instituicao = B.cd_instituicao AND A.cd_periodo_letivo = B.cd_periodo_letivo AND B.cd_tipo_dependencia = C.cd_tipo_dependencia AND A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2 " + "  AND C.st_tipo_dependencia = " + TipoDependenciaServices.ST_ATIVADO + " ORDER BY C.nm_tipo_dependencia");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(register.containsKey(Util.limparAcentos(rsm.getString("NM_TIPO_DEPENDENCIA")))){
					register.put(Util.limparAcentos(rsm.getString("NM_TIPO_DEPENDENCIA")), (register.get(Util.limparAcentos(rsm.getString("NM_TIPO_DEPENDENCIA"))) + 1));
				}
				else{
					register.put(Util.limparAcentos(rsm.getString("NM_TIPO_DEPENDENCIA")), 1);
				}
			}
			rsm.beforeFirst();
			
			
			
			ResultSetMap rsmFinal = new ResultSetMap();
			for(String key : register.keySet()){
				HashMap<String, Object> registerDependencia = new HashMap<String, Object>();
				registerDependencia.put("NM_TIPO_DEPENDENCIA", key);
				registerDependencia.put("NR_INSTITUICOES", register.get(key));
				rsmFinal.addRegister(registerDependencia);
			}
			rsmFinal.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_TIPO_DEPENDENCIA DESC");
			rsmFinal.orderBy(fields);
			
			rsmFinal.beforeFirst();
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getAllFormaOcupacao(int cdPeriodoLetivo) {
		return getAllFormaOcupacao(cdPeriodoLetivo, null);
	}


	/**
	 * Busca a informação de forma de ocupação de todas as escolas
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAllFormaOcupacao(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			int quantProprio = 0;
			int quantAlugado = 0;
			int quantCedido = 0;
			int quantInexistente = 0;
			pstmt = connect.prepareStatement("SELECT A.TP_FORMA_OCUPACAO FROM acd_instituicao_educacenso A, acd_instituicao_periodo D WHERE A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(rsm.getInt("TP_FORMA_OCUPACAO") == InstituicaoEducacensoServices.TP_OCUPACAO_PROPRIO){
					quantProprio++;
				}
				else if(rsm.getInt("TP_FORMA_OCUPACAO") == InstituicaoEducacensoServices.TP_OCUPACAO_ALUGADO){
					quantAlugado++;
				}
				else if(rsm.getInt("TP_FORMA_OCUPACAO") == InstituicaoEducacensoServices.TP_OCUPACAO_CEDIDO){
					quantCedido++;
				}
				else{
					quantInexistente++;
				}
			}
			rsm.beforeFirst();
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantProprio);
			register.put("NM_TP_FORMA_OCUPACAO", "Próprio");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantAlugado);
			register.put("NM_TP_FORMA_OCUPACAO", "Alugado");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantCedido);
			register.put("NM_TP_FORMA_OCUPACAO", "Cedido");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantInexistente);
			register.put("NM_TP_FORMA_OCUPACAO", "Inexistente");
			rsmFinal.addRegister(register);
			
			rsmFinal.beforeFirst();
			
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getAllRegulamentacao(int cdPeriodoLetivo) {
		return getAllRegulamentacao(cdPeriodoLetivo, null);
	}


	/**
	 * Busca a informação de regulamentação de todas as escolas
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAllRegulamentacao(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			int quantNao = 0;
			int quantSim = 0;
			int quantTramitando = 0;
			int quantInexistente = 0;
			pstmt = connect.prepareStatement("SELECT A.ST_REGULAMENTACAO FROM acd_instituicao_educacenso A, acd_instituicao_periodo D WHERE A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(rsm.getInt("ST_REGULAMENTACAO") == InstituicaoEducacensoServices.ST_REGULAMENTACAO_NAO){
					quantNao++;
				}
				else if(rsm.getInt("ST_REGULAMENTACAO") == InstituicaoEducacensoServices.ST_REGULAMENTACAO_SIM){
					quantSim++;
				}
				else if(rsm.getInt("ST_REGULAMENTACAO") == InstituicaoEducacensoServices.ST_REGULAMENTACAO_TRAMITANDO){
					quantTramitando++;
				}
				else{
					quantInexistente++;
				}
			}
			rsm.beforeFirst();
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantNao);
			register.put("NM_LG_REGULAMENTACAO", "Não");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantSim);
			register.put("NM_LG_REGULAMENTACAO", "Sim");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantTramitando);
			register.put("NM_LG_REGULAMENTACAO", "Tramitando");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantInexistente);
			register.put("NM_LG_REGULAMENTACAO", "Inexistente");
			rsmFinal.addRegister(register);
			
			rsmFinal.beforeFirst();
			
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getAllLocaisFuncionamento(int cdPeriodoLetivo) {
		return getAllLocaisFuncionamento(cdPeriodoLetivo, null);
	}


	/**
	 * Busca a informação de locais de funcionamento de todas as escolas
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAllLocaisFuncionamento(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			HashMap<String, Integer> register = new HashMap<String, Integer>();
			pstmt = connect.prepareStatement("SELECT C.nm_local_funcionamento FROM acd_instituicao_educacenso A, acd_instituicao_local_funcionamento B, acd_local_funcionamento C, acd_instituicao_periodo D WHERE A.cd_instituicao = B.cd_instituicao AND A.cd_periodo_letivo = B.cd_periodo_letivo AND B.cd_local_funcionamento = C.cd_local_funcionamento AND A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2 ORDER BY C.nm_local_funcionamento");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(register.containsKey(Util.limparAcentos(rsm.getString("NM_LOCAL_FUNCIONAMENTO")))){
					register.put(Util.limparAcentos(rsm.getString("NM_LOCAL_FUNCIONAMENTO")), (register.get(Util.limparAcentos(rsm.getString("NM_LOCAL_FUNCIONAMENTO"))) + 1));
				}
				else{
					register.put(Util.limparAcentos(rsm.getString("NM_LOCAL_FUNCIONAMENTO")), 1);
				}
			}
			rsm.beforeFirst();
			
			
			
			ResultSetMap rsmFinal = new ResultSetMap();
			for(String key : register.keySet()){
				HashMap<String, Object> registerEquipamento = new HashMap<String, Object>();
				registerEquipamento.put("NM_LOCAL_FUNCIONAMENTO", key);
				registerEquipamento.put("NR_INSTITUICOES", register.get(key));
				rsmFinal.addRegister(registerEquipamento);
			}
			rsmFinal.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_LOCAL_FUNCIONAMENTO DESC");
			rsmFinal.orderBy(fields);
			
			rsmFinal.beforeFirst();
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getAllEquipamentosDashboard(int cdPeriodoLetivo) {
		return getAllEquipamentosDashboard(cdPeriodoLetivo, null);
	}


	/**
	 * Busca a informação de equipamentos de todas as escolas
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAllEquipamentosDashboard(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			HashMap<String, Integer> register = new HashMap<String, Integer>();
			pstmt = connect.prepareStatement("SELECT C.nm_tipo_equipamento FROM acd_instituicao_educacenso A, acd_instituicao_tipo_equipamento B, acd_tipo_equipamento C, acd_instituicao_periodo D WHERE A.cd_instituicao = B.cd_instituicao AND A.cd_periodo_letivo = B.cd_periodo_letivo AND B.cd_tipo_equipamento = C.cd_tipo_equipamento AND A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2 AND C.st_tipo_equipamento = "+TipoEquipamentoServices.ST_ATIVADO+" ORDER BY C.nm_tipo_equipamento");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(register.containsKey(Util.limparAcentos(rsm.getString("NM_TIPO_EQUIPAMENTO")))){
					register.put(Util.limparAcentos(rsm.getString("NM_TIPO_EQUIPAMENTO")), (register.get(Util.limparAcentos(rsm.getString("NM_TIPO_EQUIPAMENTO"))) + 1));
				}
				else{
					register.put(Util.limparAcentos(rsm.getString("NM_TIPO_EQUIPAMENTO")), 1);
				}
			}
			rsm.beforeFirst();
			
			
			
			ResultSetMap rsmFinal = new ResultSetMap();
			for(String key : register.keySet()){
				HashMap<String, Object> registerEquipamento = new HashMap<String, Object>();
				registerEquipamento.put("NM_TIPO_EQUIPAMENTO", key);
				registerEquipamento.put("NR_INSTITUICOES", register.get(key));
				rsmFinal.addRegister(registerEquipamento);
			}
			rsmFinal.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_TIPO_EQUIPAMENTO DESC");
			rsmFinal.orderBy(fields);
			
			rsmFinal.beforeFirst();
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	
	public static ResultSetMap getAllInternet(int cdPeriodoLetivo) {
		return getAllInternet(cdPeriodoLetivo, null);
	}


	/**
	 * Busca a informação de internet de todas as escolas
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAllInternet(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			int quantSim = 0;
			int quantNao = 0;
			pstmt = connect.prepareStatement("SELECT A.LG_INTERNET FROM acd_instituicao_educacenso A, acd_instituicao_periodo D WHERE A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(rsm.getInt("LG_INTERNET") == 1){
					quantSim++;
				}
				else{
					quantNao++;
				}
			}
			rsm.beforeFirst();
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantSim);
			register.put("NM_LG_INTERNET", "Sim");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantNao);
			register.put("NM_LG_INTERNET", "Não");
			rsmFinal.addRegister(register);
			
			rsmFinal.beforeFirst();
			
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	
	public static ResultSetMap getAllBandaLarga(int cdPeriodoLetivo) {
		return getAllBandaLarga(cdPeriodoLetivo, null);
	}

	public static ResultSetMap getAllBandaLarga(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			int quantSim = 0;
			int quantNao = 0;
			pstmt = connect.prepareStatement("SELECT A.LG_BANDA_LARGA FROM acd_instituicao_educacenso A, acd_instituicao_periodo D WHERE A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(rsm.getInt("LG_BANDA_LARGA") == 1){
					quantSim++;
				}
				else{
					quantNao++;
				}
			}
			rsm.beforeFirst();
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantSim);
			register.put("NM_LG_BANDA_LARGA", "Sim");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantNao);
			register.put("NM_LG_BANDA_LARGA", "Não");
			rsmFinal.addRegister(register);
			
			rsmFinal.beforeFirst();
			
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getAllComputadores(int cdPeriodoLetivo) {
		return getAllComputadores(cdPeriodoLetivo, null);
	}

	public static ResultSetMap getAllComputadores(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			int quantAdministrativo = 0;
			int quantAluno = 0;
			pstmt = connect.prepareStatement("SELECT A.QT_COMPUTADOR_ADMINISTRATIVO, A.QT_COMPUTADOR_ALUNO FROM acd_instituicao_educacenso A, acd_instituicao_periodo D WHERE A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				quantAdministrativo += rsm.getInt("QT_COMPUTADOR_ADMINISTRATIVO");
				quantAluno += rsm.getInt("QT_COMPUTADOR_ALUNO");
			}
			rsm.beforeFirst();
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("QT_COMPUTADORES", quantAdministrativo);
			register.put("NM_TP_COMPUTADOR", "Computadores administrativos");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("QT_COMPUTADORES", quantAluno);
			register.put("NM_TP_COMPUTADOR", "Computadores para alunos");
			rsmFinal.addRegister(register);
			
			rsmFinal.beforeFirst();
			
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getAllDependenciaPortadoresDeficiencia(int cdPeriodoLetivo) {
		return getAllDependenciaPortadoresDeficiencia(cdPeriodoLetivo, null);
	}

	public static ResultSetMap getAllDependenciaPortadoresDeficiencia(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int qtSim = 0;
			int qtNao = 0;
			pstmt = connect.prepareStatement("SELECT A.cd_instituicao FROM acd_instituicao_educacenso A, acd_instituicao_periodo D "
					+ "							WHERE A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ " 						  AND A.cd_instituicao <> 2");
			ResultSetMap rsmInstituicaoAtivas = new ResultSetMap(pstmt.executeQuery());
			while(rsmInstituicaoAtivas.next()){
				pstmt = connect.prepareStatement("SELECT C.nm_tipo_dependencia FROM acd_instituicao_dependencia B, "
						+ "															acd_tipo_dependencia C "
						+ "							WHERE B.cd_tipo_dependencia = C.cd_tipo_dependencia "
						+ " 						  AND B.cd_instituicao = " + rsmInstituicaoAtivas.getInt("cd_instituicao")
						+ "							  AND B.cd_periodo_letivo = " + cdPeriodoLetivo
						+ "							  AND C.cd_tipo_dependencia IN ("+InstituicaoEducacensoServices.TP_DEPENDENCIA_INSTITUICAO_DEPENDENCIAS_ALUNOS_DEFICIENCIA+")"
						+ "							  AND C.st_tipo_dependencia = " + TipoDependenciaServices.ST_ATIVADO
						+ "							ORDER BY C.nm_tipo_dependencia");
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				if(rsm.next()){
					qtSim++;
				}
				else{
					qtNao++;
				}
			}
			rsmInstituicaoAtivas.beforeFirst();
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> registerDependencia = new HashMap<String, Object>();
			registerDependencia.put("NM_LG_DEPENDENCIA_PORTADORES_DEFICIENCIA", "Sim");
			registerDependencia.put("NR_INSTITUICOES", qtSim);
			rsmFinal.addRegister(registerDependencia);
			
			registerDependencia = new HashMap<String, Object>();
			registerDependencia.put("NM_LG_DEPENDENCIA_PORTADORES_DEFICIENCIA", "Não");
			registerDependencia.put("NR_INSTITUICOES", qtNao);
			rsmFinal.addRegister(registerDependencia);
			
			rsmFinal.beforeFirst();
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getAllDependenciaBanheiroPortadoresDeficiencia(int cdPeriodoLetivo) {
		return getAllDependenciaBanheiroPortadoresDeficiencia(cdPeriodoLetivo, null);
	}

	public static ResultSetMap getAllDependenciaBanheiroPortadoresDeficiencia(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int qtSim = 0;
			int qtNao = 0;
			pstmt = connect.prepareStatement("SELECT A.cd_instituicao FROM acd_instituicao_educacenso A, acd_instituicao_periodo D "
					+ "							WHERE A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ " 						  AND A.cd_instituicao <> 2");
			ResultSetMap rsmInstituicaoAtivas = new ResultSetMap(pstmt.executeQuery());
			while(rsmInstituicaoAtivas.next()){
				pstmt = connect.prepareStatement("SELECT C.nm_tipo_dependencia FROM acd_instituicao_dependencia B, "
						+ "															acd_tipo_dependencia C "
						+ "							WHERE B.cd_tipo_dependencia = C.cd_tipo_dependencia "
						+ " 						  AND B.cd_instituicao = " + rsmInstituicaoAtivas.getInt("cd_instituicao")
						+ " 						  AND B.cd_periodo_letivo = " + cdPeriodoLetivo
						+ "							  AND C.cd_tipo_dependencia IN ("+InstituicaoEducacensoServices.TP_DEPENDENCIA_INSTITUICAO_BANHEIRO_ALUNOS_DEFICIENCIA+")"
						+ "							  AND C.st_tipo_dependencia = " + TipoDependenciaServices.ST_ATIVADO
						+ "							ORDER BY C.nm_tipo_dependencia");
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				if(rsm.next()){
					qtSim++;
				}
				else{
					qtNao++;
				}
			}
			rsmInstituicaoAtivas.beforeFirst();
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> registerDependencia = new HashMap<String, Object>();
			registerDependencia.put("NM_LG_DEPENDENCIA_BANHEIRO_PORTADORES_DEFICIENCIA", "Sim");
			registerDependencia.put("NR_INSTITUICOES", qtSim);
			rsmFinal.addRegister(registerDependencia);
			
			registerDependencia = new HashMap<String, Object>();
			registerDependencia.put("NM_LG_DEPENDENCIA_BANHEIRO_PORTADORES_DEFICIENCIA", "Não");
			registerDependencia.put("NR_INSTITUICOES", qtNao);
			rsmFinal.addRegister(registerDependencia);
			
			rsmFinal.beforeFirst();
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getAllLocalizacaoDiferenciada(int cdPeriodoLetivo) {
		return getAllLocalizacaoDiferenciada(cdPeriodoLetivo, null);
	}

	public static ResultSetMap getAllLocalizacaoDiferenciada(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			int quantAssentamentos = 0;
			int quantRemQuilombola = 0;
			int quantUsoSustentavelQuilombo = 0;
			int quantNaoSeAplica = 0;
			int quantInexistente = 0;
			pstmt = connect.prepareStatement("SELECT A.TP_LOCALIZACAO_DIFERENCIADA FROM acd_instituicao_educacenso A, acd_instituicao_periodo D WHERE A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(rsm.getInt("TP_LOCALIZACAO_DIFERENCIADA") == Integer.parseInt(InstituicaoEducacensoServices.TP_LOCALIZACAO_DIFERENCIADA_ASSENTAMENTO)){
					quantAssentamentos++;
				}
				else if(rsm.getInt("TP_LOCALIZACAO_DIFERENCIADA") == Integer.parseInt(InstituicaoEducacensoServices.TP_LOCALIZACAO_DIFERENCIADA_QUILOMBOS)){
					quantRemQuilombola++;
				}
				else if(rsm.getInt("TP_LOCALIZACAO_DIFERENCIADA") == Integer.parseInt(InstituicaoEducacensoServices.TP_LOCALIZACAO_DIFERENCIADA_USO_SUSTENTAVEL_QUILOMBOS)){
					quantUsoSustentavelQuilombo++;
				}
				else if(rsm.getInt("TP_LOCALIZACAO_DIFERENCIADA") == Integer.parseInt(InstituicaoEducacensoServices.TP_LOCALIZACAO_DIFERENCIADA_NAO_APLICA)){
					quantNaoSeAplica++;
				}
				else{
					quantInexistente++;
				}
			}
			rsm.beforeFirst();
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantAssentamentos);
			register.put("NM_TP_LOCALIZACAO_DIFERENCIADA", "Assentamentos");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantRemQuilombola);
			register.put("NM_TP_LOCALIZACAO_DIFERENCIADA", "Remanescentes Quilombolas");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantUsoSustentavelQuilombo);
			register.put("NM_TP_LOCALIZACAO_DIFERENCIADA", "Uso Sustentável de Quilombos");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantNaoSeAplica);
			register.put("NM_TP_LOCALIZACAO_DIFERENCIADA", "Não se aplica");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantInexistente);
			register.put("NM_TP_LOCALIZACAO_DIFERENCIADA", "Inexistente");
			rsmFinal.addRegister(register);
			
			rsmFinal.beforeFirst();
			
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getAllMateriaisEspecificos(int cdPeriodoLetivo) {
		return getAllMateriaisEspecificos(cdPeriodoLetivo, null);
	}

	public static ResultSetMap getAllMateriaisEspecificos(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			int quantQuilombola = 0;
			int quantNaoSeAplica = 0;
			int quantInexistente = 0;
			pstmt = connect.prepareStatement("SELECT A.TP_MATERIAL_ESPECIFICO FROM acd_instituicao_educacenso A, acd_instituicao_periodo D WHERE A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(rsm.getInt("TP_MATERIAL_ESPECIFICO") == InstituicaoEducacensoServices.TP_MATERIAL_ESPECIFICO_QUILOMBOLAS){
					quantQuilombola++;
				}
				else if(rsm.getInt("TP_MATERIAL_ESPECIFICO") == InstituicaoEducacensoServices.TP_MATERIAL_ESPECIFICO_NAO_SE_APLICA){
					quantNaoSeAplica++;
				}
				else{
					quantInexistente++;
				}
			}
			rsm.beforeFirst();
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantQuilombola);
			register.put("NM_TP_MATERIAIS_ESPECIFICOS", "Quilombola");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantNaoSeAplica);
			register.put("NM_TP_MATERIAIS_ESPECIFICOS", "Não se aplica");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantInexistente);
			register.put("NM_TP_MATERIAIS_ESPECIFICOS", "Inexistente");
			rsmFinal.addRegister(register);
			
			rsmFinal.beforeFirst();
			
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getAllBrasilAlfabetizado(int cdPeriodoLetivo) {
		return getAllBrasilAlfabetizado(cdPeriodoLetivo, null);
	}

	public static ResultSetMap getAllBrasilAlfabetizado(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			int quantSim = 0;
			int quantNao = 0;
			pstmt = connect.prepareStatement("SELECT A.LG_BRASIL_ALFABETIZADO FROM acd_instituicao_educacenso A, acd_instituicao_periodo D WHERE A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(rsm.getInt("LG_BRASIL_ALFABETIZADO") == 1){
					quantSim++;
				}
				else{
					quantNao++;
				}
			}
			rsm.beforeFirst();
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantSim);
			register.put("NM_LG_BRASIL_ALFABETIZADO", "Sim");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantNao);
			register.put("NM_LG_BRASIL_ALFABETIZADO", "Não");
			rsmFinal.addRegister(register);
			
			rsmFinal.beforeFirst();
			
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getAllAbreNosFinsDeSemana(int cdPeriodoLetivo) {
		return getAllAbreNosFinsDeSemana(cdPeriodoLetivo, null);
	}

	public static ResultSetMap getAllAbreNosFinsDeSemana(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			int quantSim = 0;
			int quantNao = 0;
			pstmt = connect.prepareStatement("SELECT A.LG_ESPACO_COMUNIDADE FROM acd_instituicao_educacenso A, acd_instituicao_periodo D WHERE A.cd_instituicao = D.cd_instituicao AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND A.cd_periodo_letivo = D.cd_periodo_letivo AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + " AND A.cd_instituicao <> 2");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(rsm.getInt("LG_ESPACO_COMUNIDADE") == 1){
					quantSim++;
				}
				else{
					quantNao++;
				}
			}
			rsm.beforeFirst();
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantSim);
			register.put("NM_LG_ABRE_NOS_FINAIS_SEMANA", "Sim");
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NR_INSTITUICOES", quantNao);
			register.put("NM_LG_ABRE_NOS_FINAIS_SEMANA", "Não");
			rsmFinal.addRegister(register);
			
			rsmFinal.beforeFirst();
			
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getRendimentoEscolarByModalidade(int cdPeriodoLetivo, int tpZona, int lgUnirFundamental, int cdInstituicao) {
		return getRendimentoEscolarByModalidade(cdPeriodoLetivo, tpZona, lgUnirFundamental, cdInstituicao, null);
	}

	public static ResultSetMap getRendimentoEscolarByModalidade(int cdPeriodoLetivo, int tpZona, int lgUnirFundamental, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			int cdCurso = 0;
			int nrOrdem = -1;
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			pstmt = connect.prepareStatement("SELECT CUR.nr_idade, PS.nm_produto_servico, M.cd_curso, M.st_aluno_censo, CUR.nr_ordem, COUNT(M.st_aluno_censo) AS qt_rendimento FROM acd_instituicao_educacenso A, acd_instituicao_periodo B, acd_matricula M, acd_curso_etapa CE, acd_tipo_etapa TE, grl_produto_servico PS, acd_curso CUR "
					+ "								WHERE A.cd_instituicao = B.cd_instituicao "
					+ "								  AND (B.cd_periodo_letivo = "+cdPeriodoLetivo+" OR B.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") "
					+ "								  AND A.cd_periodo_letivo = B.cd_periodo_letivo "
					+ "								  AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE 
					+ " 							  AND A.cd_instituicao <> 2"
					+ "								  AND B.cd_periodo_letivo = M.cd_periodo_letivo"
					+ "								  AND M.st_matricula IN ("+MatriculaServices.ST_ATIVA+", "+MatriculaServices.ST_CONCLUIDA+", "+MatriculaServices.ST_DESISTENTE+", "+MatriculaServices.ST_EVADIDO+", "+MatriculaServices.ST_EM_TRANSFERENCIA + (cdInstituicao != cdSecretaria ? ", " + MatriculaServices.ST_TRANSFERIDO : "") + ")"
					+ "								  AND M.cd_curso NOT IN ("+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)+")"
					+ "								  AND M.cd_curso = CE.cd_curso "
					+ "								  AND CE.cd_etapa = TE.cd_etapa "
					+ "								  AND M.cd_curso = PS.cd_produto_servico "
					+ "								  AND M.cd_curso = CUR.cd_curso"
					+ (tpZona > 0 ? "				  AND A.tp_localizacao = " + tpZona : "")	
					+ (cdInstituicao != cdSecretaria ? " AND A.cd_instituicao = " + cdInstituicao : "")
					+ " 							  AND CUR.lg_multi = 0"				
					+ "								GROUP BY 1, 2, 3, 4, 5"
					+ "								ORDER BY 1, 2, 3, 4, 5");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(cdCurso > 0 && ((lgUnirFundamental <= 0 && rsm.getInt("cd_curso") != cdCurso) 
						        || (lgUnirFundamental > 0 && 
						        		((nrOrdem > -1 && nrOrdem < 9 && nrOrdem != rsm.getInt("nr_ordem"))
						        	   || ((nrOrdem <= -1 || nrOrdem >= 9) && rsm.getInt("cd_curso") != cdCurso))
						        	)
						          )
				  ){
					
					Curso curso = CursoDAO.get(cdCurso, connect);
					if(CursoServices.isCreche(cdCurso, connect)){
						register.put("TP_MODALIDADE", 0);
					}
					else if(CursoServices.isEducacaoInfantil(cdCurso, connect)){
						register.put("TP_MODALIDADE", 1);
					}
					else if(CursoServices.isEja(cdCurso, connect)){
						register.put("TP_MODALIDADE", 4);
					}
					else{
						if(curso.getNrOrdem() >= 0 && curso.getNrOrdem() <= 4){
							register.put("TP_MODALIDADE", 2);
						}
						else if(curso.getNrOrdem() >= 5 && curso.getNrOrdem() <= 8){
							register.put("TP_MODALIDADE", 3);
						}
						else{
							register.put("TP_MODALIDADE", -1);
						}
					}
					
					
					register.put("CD_CURSO", cdCurso);
					if(lgUnirFundamental > 0 && nrOrdem > -1 && nrOrdem < 9 && !CursoServices.isEja(cdCurso, connect)){
						ResultSetMap rsmCursoEquivalente = new ResultSetMap(connect.prepareStatement("SELECT B.nm_produto_servico FROM acd_curso A, grl_produto_servico B WHERE A.cd_curso = B.cd_produto_servico AND A.lg_referencia = 1 AND A.nr_ordem = " + nrOrdem).executeQuery());
						String nmCurso = null;
						if(rsmCursoEquivalente.next()){
							nmCurso = rsmCursoEquivalente.getString("nm_produto_servico");
						}
						register.put("NM_CURSO", nmCurso);
					}
					else{
						register.put("NM_CURSO", curso.getNmProdutoServico());
					}
					register.put("qtAbandono", (Integer.parseInt(String.valueOf((register.get("qtDesistente") != null ? register.get("qtDesistente") : 0))) + Integer.parseInt(String.valueOf((register.get("qtEvadido") != null ? register.get("qtEvadido") : 0)))));
					rsmFinal.addRegister(register);
					
					register = new HashMap<String, Object>();
					cdCurso = rsm.getInt("cd_curso");
					nrOrdem = rsm.getInt("nr_ordem");
					register.put("qt" + MatriculaServices.situacaoAlunoCenso[rsm.getInt("st_aluno_censo")], rsm.getInt("qt_rendimento"));
				}
				else{
					cdCurso = rsm.getInt("cd_curso");
					nrOrdem = rsm.getInt("nr_ordem");
					if(register.get("qt" + MatriculaServices.situacaoAlunoCenso[rsm.getInt("st_aluno_censo")]) == null)
						register.put("qt" + MatriculaServices.situacaoAlunoCenso[rsm.getInt("st_aluno_censo")], rsm.getInt("qt_rendimento"));
					else
						register.put("qt" + MatriculaServices.situacaoAlunoCenso[rsm.getInt("st_aluno_censo")], Integer.parseInt(String.valueOf(register.get("qt" + MatriculaServices.situacaoAlunoCenso[rsm.getInt("st_aluno_censo")]))) + rsm.getInt("qt_rendimento"));
				}
			}
			rsmFinal.beforeFirst();
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static Result getAllMatriculasInstituicao(int cdModalidade){
		return getAllMatriculasInstituicao(cdModalidade, null);
	}
	
	public static Result getAllMatriculasInstituicao(int cdModalidade, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		PreparedStatement pstmt;
		
		try {
			pstmt = connect.prepareStatement(
				"SELECT A.* FROM alm_circulo_modalidade A " + 
				"JOIN alm_circulo_modalidade B ON (A.cd_modalidade = B.cd_modalidade) "
			);
			
			ResultSet rs = pstmt.executeQuery();

			ResultSetMap rsmInstituicoes = new ResultSetMap();
			
			return new Result(0);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static int getPeriodoLetivoCorrespondente(int cdInstituicao, int cdPeriodoLetivoSuperior){
		return getPeriodoLetivoCorrespondente(cdInstituicao, cdPeriodoLetivoSuperior, null);
	}
	
	public static int getPeriodoLetivoCorrespondente(int cdInstituicao, int cdPeriodoLetivoSuperior, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		PreparedStatement pstmt;
		
		try {
			pstmt = connect.prepareStatement(
				"SELECT * FROM acd_instituicao_periodo " + 
				"	WHERE cd_instituicao = " + cdInstituicao + 
				"	  AND cd_periodo_letivo_superior = " + cdPeriodoLetivoSuperior	
			);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()){
				return rsm.getInt("cd_periodo_letivo");
			}
			else{
				return 0;
			}
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoServices.getPeriodoLetivoCorrespondente: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
	 * Método que exibe um resumo da instituição na tela inicial do módulo da escola (versão angular)
	 * @param cdInstituicao
	 * @return
	 */
	public static Result getResumo(int cdInstituicao){
		return getResumo(cdInstituicao, null);
	}
	
	public static Result getResumo(int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		int cdPeriodoLetivoRecente = 0;
		ResultSetMap rsmPeriodoLetivoRecente = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdInstituicao, connect);
		if(rsmPeriodoLetivoRecente.next()){
			cdPeriodoLetivoRecente = rsmPeriodoLetivoRecente.getInt("cd_periodo_letivo");
		}
		
		
		PreparedStatement pstmt;
		
		try {
			pstmt = connect.prepareStatement(
				"SELECT INS.cd_instituicao, INS.nr_inep, INS_P.nm_pessoa AS NM_INSTITUICAO, INS_E.TP_LOCALIZACAO, DIR_P.nm_pessoa AS NM_DIRETOR, COR_P.nm_pessoa AS NM_COORDENADOR, VIC_P.nm_pessoa AS NM_VICE_DIRETOR, SEC_P.nm_pessoa AS NM_SECRETARIO, TES_P.nm_pessoa AS NM_TESOUREIRO, GES_P.nm_pessoa AS NM_GESTOR "+ 
				"  FROM acd_instituicao INS " +
				" JOIN acd_instituicao_educacenso INS_E ON (INS.cd_instituicao = INS_E.cd_instituicao"
				+ "												AND INS_E.cd_periodo_letivo = "+cdPeriodoLetivoRecente+") " +
				" JOIN grl_pessoa INS_P ON (INS.cd_instituicao = INS_P.cd_pessoa) " +
				" LEFT OUTER JOIN grl_pessoa DIR_P ON (INS.cd_diretor = DIR_P.cd_pessoa) " + 
				" LEFT OUTER JOIN grl_pessoa COR_P ON (INS.cd_coordenador = COR_P.cd_pessoa) " + 
				" LEFT OUTER JOIN grl_pessoa VIC_P ON (INS.cd_vice_diretor = VIC_P.cd_pessoa) " + 
				" LEFT OUTER JOIN grl_pessoa SEC_P ON (INS.cd_secretario = SEC_P.cd_pessoa) " + 
				" LEFT OUTER JOIN grl_pessoa TES_P ON (INS.cd_tesoureiro = TES_P.cd_pessoa) " +
				" LEFT OUTER JOIN grl_pessoa GES_P ON (INS.cd_administrador = GES_P.cd_pessoa)" + 
				"	WHERE INS.cd_instituicao = " + cdInstituicao + 
				"     AND INS_E.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
			);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()){
				
				rsm.setValueToField("NM_TP_LOCALIZACAO", InstituicaoEducacensoServices.tipoLocalizacao[(rsm.getInt("TP_LOCALIZACAO")-1)]);
				
				//Faz um somatório das turmas do periodo letivo recente
				ResultSetMap rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT COUNT(cd_turma) AS NR_TURMAS FROM acd_turma WHERE cd_periodo_letivo = " + cdPeriodoLetivoRecente + " AND st_turma = " + TurmaServices.ST_ATIVO).executeQuery());
				if(rsmTurmas.next())
					rsm.setValueToField("NR_TURMAS", rsmTurmas.getInt("NR_TURMAS"));
				
				//Faz um somatório das matrículas do periodo letivo recente
				ResultSetMap rsmMatriculas = new ResultSetMap(connect.prepareStatement("SELECT COUNT(cd_matricula) AS NR_MATRICULAS FROM acd_matricula WHERE cd_periodo_letivo = " + cdPeriodoLetivoRecente + " AND st_matricula = " + MatriculaServices.ST_ATIVA).executeQuery());
				if(rsmMatriculas.next())
					rsm.setValueToField("NR_MATRICULAS", rsmMatriculas.getInt("NR_MATRICULAS"));
				
				//Faz um somatório dos alunos matriculados (pode ser divergente em relação às matrículas pois um aluno pode ter mais de uma matrícula) do periodo letivo recente
				ResultSetMap rsmAlunos = new ResultSetMap(connect.prepareStatement("SELECT COUNT(DISTINCT(cd_aluno)) AS NR_ALUNOS FROM acd_matricula WHERE cd_periodo_letivo = " + cdPeriodoLetivoRecente + " AND st_matricula = " + MatriculaServices.ST_ATIVA).executeQuery());
				if(rsmAlunos.next())
					rsm.setValueToField("NR_ALUNOS", rsmAlunos.getInt("NR_ALUNOS"));
				
				//Faz um somatório dos professores enturmados do periodo letivo recente
				ResultSetMap rsmProfessores = getAllProfessoresLotados(cdInstituicao, cdPeriodoLetivoRecente);
				if(rsmProfessores.next())
					rsm.setValueToField("NR_PROFESSORES", rsmProfessores.getInt("NR_PROFESSORES"));
				
				//Busca o periodo recente, se ele for o atual mostra apenas ele, se não for significa que está em periodo de preparação do próximo periodo letivo, mostrando ambos na tela
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivoRecente, connect);
				if(instituicaoPeriodo.getStPeriodoLetivo() == InstituicaoPeriodoServices.ST_ATUAL){
					rsm.setValueToField("NM_PERIODO_LETIVO", instituicaoPeriodo.getNmPeriodoLetivo());
				}
				else{
					rsm.setValueToField("NM_PERIODO_LETIVO", (Integer.parseInt(instituicaoPeriodo.getNmPeriodoLetivo())-1) + "/" + instituicaoPeriodo.getNmPeriodoLetivo());
				}
				
				String nmCursosOferecidos = "<br />";
				ResultSetMap rsmCursosOferecidos = getCursosByInstituicao(cdInstituicao);
				while(rsmCursosOferecidos.next()){
					nmCursosOferecidos += rsmCursosOferecidos.getString("NM_CURSO") + ",<br />";
				}
				rsm.setValueToField("CONJUNTO_ETAPAS", nmCursosOferecidos.substring(0, nmCursosOferecidos.length()-2));
				
				rsm.setValueToField("NR_SALAS", getAllSalas(cdInstituicao).size());
				
				HashMap<String, Object> register = rsm.getRegister();
				
				return new Result(1, "Resumo realizado com sucesso", "INSTITUICAO_RESUMO", register);
			}
			else{
				return new Result(-1, "Erro ao buscar resumo");
			}
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoServices.getResumo: " + e);
			return new Result(-1, "Erro ao buscar resumo");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getResumoRede(){
		return getResumoRede(null);
	}
	
	public static Result getResumoRede(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
		
		int cdPeriodoLetivoRecente = 0;
		ResultSetMap rsmPeriodoLetivoRecente = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdSecretaria, connect);
		if(rsmPeriodoLetivoRecente.next()){
			cdPeriodoLetivoRecente = rsmPeriodoLetivoRecente.getInt("cd_periodo_letivo");
		}
		
		
		PreparedStatement pstmt;
		
		try {
			pstmt = connect.prepareStatement(
				"SELECT INS.cd_instituicao, IP.cd_periodo_letivo "+ 
				"  FROM acd_instituicao INS " +
				" JOIN acd_instituicao_periodo IP ON (INS.cd_instituicao = IP.cd_instituicao) " + 
				"	WHERE IP.cd_periodo_letivo_superior = " + cdPeriodoLetivoRecente
			);
			
			int nrInstituicoes = 0;
			int nrAlunos = 0;
			int nrProfessores = 0;
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			nrInstituicoes += rsm.size();
			while(rsm.next()){
				//Faz um somatório dos professores enturmados do periodo letivo recente
				ResultSetMap rsmProfessores = getAllProfessoresLotados(rsm.getInt("cd_instituicao"), rsm.getInt("cd_periodo_letivo"));
				nrProfessores += rsmProfessores.size();
			}
			
			//Faz um somatório dos alunos matriculados (pode ser divergente em relação às matrículas pois um aluno pode ter mais de uma matrícula) do periodo letivo recente
			ResultSetMap rsmAlunos = new ResultSetMap(connect.prepareStatement("SELECT COUNT(DISTINCT(M.cd_aluno)) AS NR_ALUNOS FROM acd_matricula M, acd_instituicao_periodo IP WHERE M.cd_periodo_letivo = IP.cd_periodo_letivo AND IP.cd_periodo_letivo_superior = " + cdPeriodoLetivoRecente + " AND st_matricula = " + MatriculaServices.ST_ATIVA).executeQuery());
			if(rsmAlunos.next())
			nrAlunos += rsmAlunos.getInt("NR_ALUNOS");
			
			Result result = new Result(1, "Busca realizada com sucesso");
			result.addObject("NR_INSTITUICOES", nrInstituicoes);
			result.addObject("NR_ALUNOS", nrAlunos);
			result.addObject("NR_PROFESSORES", nrProfessores);
			
			return result;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoServices.getResumo: " + e);
			return new Result(-1, "Erro ao buscar resumo");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ValidatorResult validate(Instituicao instituicao, int idGrupo){
		return validate(instituicao, null, null, 0, idGrupo, null);
	}
	
	public static ValidatorResult validate(Instituicao instituicao, int idGrupo, Connection connect){
		return validate(instituicao, null, null, 0, idGrupo, connect);
	}
	
	public static ValidatorResult validate(Instituicao instituicao, PessoaEndereco endereco, Ponto pontoLocalizacao, int cdUsuario, int idGrupo){
		return validate(instituicao, endereco, pontoLocalizacao, cdUsuario, idGrupo, null);
	}
	
	public static ValidatorResult validate(Instituicao instituicao, PessoaEndereco endereco, Ponto pontoLocalizacao, int cdUsuario, int idGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(instituicao == null){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new ValidatorResult(ValidatorResult.ERROR, "Instituição não encontrada");
			}
			
			ValidatorResult result = new ValidatorResult(ValidatorResult.VALID, "Instituição passou pela validação");
			HashMap<Integer, Validator> listValidator = getListValidation();
			
			//GESTOR - Validações de Gestor
			PessoaFisica gestor = PessoaFisicaDAO.get(instituicao.getCdAdministrador(), connect); 
			validateGestor(listValidator, gestor, connect);
			
			//ENDERECO - Validações de Endereço
			validateEndereco(listValidator, endereco, connect);
			
						
			//EMAIL - Saber se existe email e se o mesmo é válido
			if(instituicao.getNmEmail()!= null && (!Util.isEmail(instituicao.getNmEmail()) || instituicao.getNmEmail().length() > 50)){
				listValidator.get(VALIDATE_EMAIL).add(ValidatorResult.ERROR, "Instituição com email inválido");
			}
			else if(instituicao.getNmEmail()== null){
				listValidator.get(VALIDATE_EMAIL).add(ValidatorResult.ALERT, "Instituição não possui Email");
			}
			
			
			//VERIFICAR SE EXISTE TELEFONE
			if((instituicao.getNrTelefone1() == null || instituicao.getNrTelefone1().equals("")) && 
				(instituicao.getNrTelefone2() == null || instituicao.getNrTelefone2().equals("")) && 
				(instituicao.getNrCelular() == null || instituicao.getNrCelular().equals("")) && 
				(instituicao.getNrFax() == null || instituicao.getNrFax().equals(""))){
				listValidator.get(VALIDATE_SEM_TELEFONE).add(ValidatorResult.ALERT, "Nenhum telefone cadastrado para a Instituição", GRUPO_VALIDACAO_INSERT);
				listValidator.get(VALIDATE_SEM_TELEFONE).add(ValidatorResult.ALERT, "Nenhum telefone cadastrado para a Instituição", GRUPO_VALIDACAO_UPDATE);
			}
			
			
			//TELEFONE 1 - Verificar se o telefone 1 é válido
			if(instituicao.getNrTelefone1() != null && !instituicao.getNrTelefone1().equals("") && (!Util.isNumber(instituicao.getNrTelefone1()) || instituicao.getNrTelefone1().length() < 7 || instituicao.getNrTelefone1().length() > 12)){
				listValidator.get(VALIDATE_TELEFONE_1).add(ValidatorResult.ERROR, "O primeiro telefone possui dígitos inválidos ou está incompleto");
			}
			
			
			//TELEFONE 2 - Verificar se o telefone 2 é válido
			if(instituicao.getNrTelefone2() != null && !instituicao.getNrTelefone2().equals("") && (!Util.isNumber(instituicao.getNrTelefone2()) || instituicao.getNrTelefone2().length() < 7 || instituicao.getNrTelefone2().length() > 12)){
				listValidator.get(VALIDATE_TELEFONE_2).add(ValidatorResult.ERROR, "O segundo telefone possui dígitos inválidos ou está incompleto");
			}
			
			
			//CELULAR - Verificar se o celular é válido
			if(instituicao.getNrCelular() != null && !instituicao.getNrCelular().equals("") && (!Util.isNumber(instituicao.getNrCelular()) || instituicao.getNrCelular().length() < 7 || instituicao.getNrCelular().length() > 12)){
				listValidator.get(VALIDATE_TELEFONE_CELULAR).add(ValidatorResult.ERROR, "O celular possui dígitos inválidos ou está incompleto");
			}
			
			
			//FAX - Verificar se o fax é válido
			if(instituicao.getNrFax() != null && !instituicao.getNrFax().equals("") && (!Util.isNumber(instituicao.getNrFax()) || instituicao.getNrFax().length() < 7 || instituicao.getNrFax().length() > 12)){
				listValidator.get(VALIDATE_TELEFONE_FAX).add(ValidatorResult.ERROR, "O fax possui dígitos inválidos ou está incompleto");
			}
			
			if(pontoLocalizacao.getVlLatitude() != 0 || pontoLocalizacao.getVlLongitude() != 0){
				if(pontoLocalizacao.getVlLatitude() == 0){
					listValidator.get(VALIDATE_LATITUDE).add(ValidatorResult.ERROR, "Faltando cadastro de Latitude da Escola", GRUPO_VALIDACAO_EDUCACENSO);
					listValidator.get(VALIDATE_LATITUDE).add(ValidatorResult.ALERT, "Faltando cadastro de Latitude da Escola", ValidatorResult.STANDARD);
				}
				if(pontoLocalizacao.getVlLongitude() == 0){
					listValidator.get(VALIDATE_LONGITUDE).add(ValidatorResult.ERROR, "Faltando cadastro de Longitude da Escola", GRUPO_VALIDACAO_EDUCACENSO);
					listValidator.get(VALIDATE_LONGITUDE).add(ValidatorResult.ALERT, "Faltando cadastro de Longitude da Escola", ValidatorResult.STANDARD);
				}
			}
			
			//PERIODO LETIVO - Verificar se existe data inicial e final no periodo letivo
			if(instituicao.getCdInstituicao() > 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(instituicao.getCdInstituicao(), connect);
				if(rsmPeriodoAtual.next()){
					InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect);
					if(instituicaoPeriodo.getDtInicial() == null){
						listValidator.get(VALIDATE_PERIODO_LETIVO_DATA_INICIAL).add(ValidatorResult.ERROR, "Não há registro da data inicial do período letivo vigente", GRUPO_VALIDACAO_EDUCACENSO);
						listValidator.get(VALIDATE_PERIODO_LETIVO_DATA_INICIAL).add(ValidatorResult.ALERT, "Não há registro da data inicial do período letivo vigente", GRUPO_VALIDACAO_UPDATE);
					}
					
					if(instituicaoPeriodo.getDtFinal() == null){
						listValidator.get(VALIDATE_PERIODO_LETIVO_DATA_FINAL).add(ValidatorResult.ERROR, "Não há registro da data final do período letivo vigente", GRUPO_VALIDACAO_EDUCACENSO);
						listValidator.get(VALIDATE_PERIODO_LETIVO_DATA_FINAL).add(ValidatorResult.ALERT, "Não há registro da data final do período letivo vigente", GRUPO_VALIDACAO_UPDATE);
					}
				}
			}
			
			//Faz a verificação das validações a partir do grupo que chamou
			result.addListValidator(listValidator);
			result.verify(idGrupo);
			
			//RETORNO
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoServices.validate: " + e);
			return new ValidatorResult(ValidatorResult.ERROR, "Erro em InstituicaoServices.validate");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static void validateGestor(HashMap<Integer, Validator> listValidator, PessoaFisica gestor, Connection connect){
		boolean isConnectionNull = connect==null;
		
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//GESTOR - Verifica se existe um gestor
			if(gestor == null){
				listValidator.get(VALIDATE_GESTOR).add(ValidatorResult.ERROR, "Não há gestor cadastrado na Instituição", GRUPO_VALIDACAO_EDUCACENSO);
				listValidator.get(VALIDATE_GESTOR).add(ValidatorResult.ALERT, "Não há gestor cadastrado na Instituição", ValidatorResult.STANDARD);
				return;
			}
			
			//NOME
			if(gestor.getNmPessoa() == null || gestor.getNmPessoa().trim().equals("") || gestor.getNmPessoa().length() > 100){
				listValidator.get(VALIDATE_GESTOR_NOME).add(ValidatorResult.ERROR, "O gestor não possui Nome", GRUPO_VALIDACAO_EDUCACENSO);
				listValidator.get(VALIDATE_GESTOR_NOME).add(ValidatorResult.ALERT, "O gestor não possui Nome", ValidatorResult.STANDARD);
			}
			
			
			//CPF
			if(gestor.getNrCpf() == null || gestor.getNrCpf().trim().equals("") || !Util.isCpfValido(gestor.getNrCpf())){
				listValidator.get(VALIDATE_GESTOR_CPF).add(ValidatorResult.ERROR, "O gestor não possui CPF, ou o mesmo é inválido", GRUPO_VALIDACAO_EDUCACENSO);
				listValidator.get(VALIDATE_GESTOR_CPF).add(ValidatorResult.ALERT, "O gestor não possui CPF, ou o mesmo é inválido", ValidatorResult.STANDARD);
			}
			
			
			//EMAIL
			if(gestor.getNmEmail() == null || gestor.getNmEmail().trim().equals("") || !Util.isEmail(gestor.getNmEmail())){
				listValidator.get(VALIDATE_GESTOR_EMAIL).add(ValidatorResult.ERROR, "O gestor não possui Email, ou o mesmo é inválido", GRUPO_VALIDACAO_EDUCACENSO);
				listValidator.get(VALIDATE_GESTOR_EMAIL).add(ValidatorResult.ALERT, "O gestor não possui Email, ou o mesmo é inválido", ValidatorResult.STANDARD);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoServices.validateGestor: " + e);
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
				listValidator.get(VALIDATE_ENDERECO).add(ValidatorResult.ERROR, "Não há endereço cadastrado na Instituição");
				return;
			}
			
			//CEP - Verificar se existe um CEP cadastrado e se o mesmo é válido
			ResultSetMap rsmLogradouro = LogradouroServices.getEnderecoByCep(endereco.getNrCep(), connect);
			if(endereco.getNrCep() == null || endereco.getNrCep().trim().equals("") || endereco.getNrCep().equals("45000000") || endereco.getNrCep().length() != 8 || rsmLogradouro == null  || rsmLogradouro.size() == 0){
				listValidator.get(VALIDATE_ENDERECO_CEP).add(ValidatorResult.ERROR, "O endereço não possui CEP, ou o mesmo é inválido");
			}
			
			
			//LOGRADOURO - Verificar se existe logradouro cadastrado
			if(endereco != null && (endereco.getNmLogradouro() == null || endereco.getNmLogradouro().trim().equals("") || endereco.getNmLogradouro().trim().length() > 100)){
				listValidator.get(VALIDATE_ENDERECO_LOGRADOURO).add(ValidatorResult.ERROR, "O endereço não possui logradouro ou o mesmo é inválido");
			}
			
			
			//NUMERO - Verificar se existe número de endereço cadastrado
			if(endereco != null && (endereco.getNrEndereco() == null || endereco.getNrEndereco().trim().equals("") || endereco.getNrEndereco().trim().length() > 10)){
				listValidator.get(VALIDATE_ENDERECO_NUMERO).add(ValidatorResult.ERROR, "O endereço não possui número ou o mesmo é inválido");
			}
			
			
			//BAIRRO - Verificar se existe bairro cadastrado
			if(endereco != null && (endereco.getNmBairro() == null || endereco.getNmBairro().trim().equals("") || endereco.getNmBairro().length() > 50)){
				listValidator.get(VALIDATE_ENDERECO_BAIRRO).add(ValidatorResult.ERROR, "O endereço não possui bairro");
			}
			
			//CIDADE - Verificar se existe cidade cadastrada
			if(endereco != null && (endereco.getCdCidade() == 0)){
				listValidator.get(VALIDATE_ENDERECO_CIDADE).add(ValidatorResult.ERROR, "O endereço não possui cidade");
			}
			
			//ZONA - Verificar se existe zona cadastrada
			if(endereco != null && (endereco.getTpZona() == 0)){
				listValidator.get(VALIDATE_ENDERECO_ZONA).add(ValidatorResult.ERROR, "O endereço não possui zona", GRUPO_VALIDACAO_EDUCACENSO);
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
		
		list.put(VALIDATE_EMAIL, new Validator(VALIDATE_EMAIL, ValidatorResult.VALID));
		list.put(VALIDATE_SEM_TELEFONE, new Validator(VALIDATE_SEM_TELEFONE, ValidatorResult.VALID));
		list.put(VALIDATE_TELEFONE_1, new Validator(VALIDATE_TELEFONE_1, ValidatorResult.VALID));
		list.put(VALIDATE_TELEFONE_2, new Validator(VALIDATE_TELEFONE_2, ValidatorResult.VALID));
		list.put(VALIDATE_TELEFONE_CELULAR, new Validator(VALIDATE_TELEFONE_CELULAR, ValidatorResult.VALID));
		list.put(VALIDATE_TELEFONE_FAX, new Validator(VALIDATE_TELEFONE_FAX, ValidatorResult.VALID));
		list.put(VALIDATE_LATITUDE, new Validator(VALIDATE_LATITUDE, ValidatorResult.VALID));
		list.put(VALIDATE_LONGITUDE, new Validator(VALIDATE_LONGITUDE, ValidatorResult.VALID));
		list.put(VALIDATE_PERIODO_LETIVO_DATA_INICIAL, new Validator(VALIDATE_PERIODO_LETIVO_DATA_INICIAL, ValidatorResult.VALID));
		list.put(VALIDATE_PERIODO_LETIVO_DATA_FINAL, new Validator(VALIDATE_PERIODO_LETIVO_DATA_FINAL, ValidatorResult.VALID));
		list.put(VALIDATE_GESTOR, new Validator(VALIDATE_GESTOR, ValidatorResult.VALID));
		list.put(VALIDATE_GESTOR_NOME, new Validator(VALIDATE_GESTOR_NOME, ValidatorResult.VALID));
		list.put(VALIDATE_GESTOR_CPF, new Validator(VALIDATE_GESTOR_CPF, ValidatorResult.VALID));
		list.put(VALIDATE_GESTOR_EMAIL, new Validator(VALIDATE_GESTOR_EMAIL, ValidatorResult.VALID));
		list.put(VALIDATE_ENDERECO, new Validator(VALIDATE_ENDERECO, ValidatorResult.VALID));
		list.put(VALIDATE_ENDERECO_CEP, new Validator(VALIDATE_ENDERECO_CEP, ValidatorResult.VALID));
		list.put(VALIDATE_ENDERECO_LOGRADOURO, new Validator(VALIDATE_ENDERECO_LOGRADOURO, ValidatorResult.VALID));
		list.put(VALIDATE_ENDERECO_NUMERO, new Validator(VALIDATE_ENDERECO_NUMERO, ValidatorResult.VALID));
		list.put(VALIDATE_ENDERECO_BAIRRO, new Validator(VALIDATE_ENDERECO_BAIRRO, ValidatorResult.VALID));
		list.put(VALIDATE_ENDERECO_CIDADE, new Validator(VALIDATE_ENDERECO_CIDADE, ValidatorResult.VALID));
		list.put(VALIDATE_ENDERECO_ZONA, new Validator(VALIDATE_ENDERECO_ZONA, ValidatorResult.VALID));
		
		return list;
	}
	
	
	/**
	 * Busca os distritos que possuem instituições ativas
	 * @return
	 */
	public static ResultSetMap getDistritos() {
		return getDistritos(null);
	}

	public static ResultSetMap getDistritos(Connection connection) {
		boolean isConnectioNull = connection==null;
		try {
			if (isConnectioNull)
				connection = Conexao.conectar();
			
			int cdPeriodoLetivo = 0;
			
			ResultSetMap rsmPeriodo = InstituicaoServices.getPeriodoLetivoRecente(ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0));
			if(rsmPeriodo.next())
				cdPeriodoLetivo = rsmPeriodo.getInt("cd_periodo_letivo");
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT G.*"
															+ " FROM acd_instituicao A"
															+ " JOIN grl_pessoa B ON ( A.cd_instituicao = B.cd_pessoa )"
															+ " JOIN acd_instituicao_periodo IP ON ( A.cd_instituicao = IP.cd_instituicao AND IP.cd_periodo_letivo_superior >= ? )"
															+ " JOIN acd_instituicao_educacenso C ON ( A.cd_instituicao = C.cd_instituicao AND C.cd_periodo_letivo = IP.cd_periodo_letivo )" 
															+ " LEFT OUTER JOIN grl_pessoa_endereco D ON (B.cd_pessoa = D.cd_pessoa AND D.lg_principal = 1)"
															+ " LEFT OUTER JOIN grl_logradouro_cep E ON (D.nr_cep = E.nr_cep)"
															+ " LEFT OUTER JOIN grl_logradouro F ON (E.cd_logradouro = F.cd_logradouro)"
															+ " LEFT OUTER JOIN grl_distrito 	G ON (F.cd_distrito = G.cd_distrito)"
															+ "  GROUP BY G.cd_cidade, G.cd_distrito"
															+ "  ORDER BY nm_distrito");
			pstmt.setInt(1, cdPeriodoLetivo);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectioNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Busca as instituicoes por distrito
	 * @param cdDistrito
	 * @return
	 */
	public static ResultSetMap getByDistrito(int cdDistrito) {
		return getByDistrito(cdDistrito, null);
	}

	public static ResultSetMap getByDistrito(int cdDistrito, Connection connection) {
		boolean isConnectioNull = connection==null;
		try {
			if (isConnectioNull)
				connection = Conexao.conectar();
			
			int cdPeriodoLetivo = 0;
			
			ResultSetMap rsmPeriodo = InstituicaoServices.getPeriodoLetivoRecente(ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0));
			if(rsmPeriodo.next())
				cdPeriodoLetivo = rsmPeriodo.getInt("cd_periodo_letivo");
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.*, B.nm_pessoa AS nm_instituicao, "
														    + "B.nm_pessoa AS nm_fantasia, B.nr_telefone1, B.nr_telefone2, B.nr_fax, " 
													        + "B.nm_email, B.nm_url, B.nm_pessoa as nm_pessoa, C.cd_periodo_letivo, C.st_instituicao_publica " 
															+ " FROM acd_instituicao A"
															+ " JOIN grl_pessoa B ON ( A.cd_instituicao = B.cd_pessoa )"
															+ " JOIN acd_instituicao_periodo IP ON ( A.cd_instituicao = IP.cd_instituicao AND IP.cd_periodo_letivo_superior >= ? )"
															+ " JOIN acd_instituicao_educacenso C ON ( A.cd_instituicao = C.cd_instituicao AND C.cd_periodo_letivo = IP.cd_periodo_letivo )" 
															+ " LEFT OUTER JOIN grl_pessoa_endereco D ON (B.cd_pessoa = D.cd_pessoa AND D.lg_principal = 1)"
															+ " LEFT OUTER JOIN grl_logradouro_cep E ON (D.nr_cep = E.nr_cep)"
															+ " LEFT OUTER JOIN grl_logradouro F ON (E.cd_logradouro = F.cd_logradouro)"
															+ " WHERE F.cd_distrito = ?");
			pstmt.setInt(1, cdPeriodoLetivo);
			pstmt.setInt(2, cdDistrito);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectioNull)
				Conexao.desconectar(connection);
		}
	}
	
	
}