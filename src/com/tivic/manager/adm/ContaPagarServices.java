package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.tivic.manager.alm.DocumentoEntrada;
import com.tivic.manager.alm.DocumentoEntradaDAO;
import com.tivic.manager.alm.DocumentoEntradaServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.FeriadoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaContaBancaria;
import com.tivic.manager.grl.PessoaContaBancariaDAO;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.prc.Processo;
import com.tivic.manager.prc.ProcessoAndamento;
import com.tivic.manager.prc.ProcessoAndamentoServices;
import com.tivic.manager.prc.ProcessoArquivo;
import com.tivic.manager.prc.ProcessoDAO;
import com.tivic.manager.prc.ProcessoFinanceiro;
import com.tivic.manager.prc.ProcessoFinanceiroDAO;
import com.tivic.manager.prc.ProcessoFinanceiroServices;
import com.tivic.manager.prc.ServicoRecorte;
import com.tivic.manager.prc.ServicoRecorteDAO;
import com.tivic.manager.prc.TipoAndamento;
import com.tivic.manager.prc.TipoAndamentoDAO;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.ComplianceManager;
import com.tivic.manager.util.Recursos;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ContaPagarServices {

	public static final String[] tipoFrequencia = {"Apenas uma vez","Quantidade fixa", "Diário", "Semanal",
        										   "Semanas Alternadas","Duas vezes por mês", "A cada 4 semanas",
        										   "Mensal", "Meses alternados", "Trimestral", "Duas vezes por ano",
        										   "Anual", "Anos alternados"};
/**
 * Situação das contas antes de 30/01/2015
 *	public static final String[] situacaoContaPagar = {"Em aberto","Paga","Renegociada","Cancelada","Perda"};
 * 
 */
	// todas perdas (4) para (5)
	// todas negociadas (2) para (4)
	// todas canceladas (3) para (2)
	//
	public static final String[] situacaoContaPagar = {"Em aberto","Paga","Cancelada","Prorrogada","Renegociada","Perda"};

	public static final int ST_EM_ABERTO  = 0;
	public static final int ST_PAGA       = 1;
	public static final int ST_CANCELADA  = 2;
	public static final int ST_PRORROGADA = 3;
	public static final int ST_NEGOCIADA  = 4;
	public static final int ST_PERDA      = 5;

    public static final int UNICA_VEZ  	          = 0;
	public static final int QUANTIDADE_FIXA       = 1;
	public static final int DIARIO    	          = 2;
	public static final int SEMANAL               = 3;
	public static final int SEMANAS_ALTERNADAS    = 4;
	public static final int DUAS_VEZES_MES 	      = 5;
	public static final int A_CADA_QUATRO_SEMANAS = 6;
	public static final int MENSAL                = 7;
	public static final int MESES_ALTERNADOS 	  = 8;
	public static final int TRIMESTRAL            = 9;
	public static final int A_CADA_QUATRO_MESES   = 10;
	public static final int DUAS_VEZES_ANO        = 11;
	public static final int ANUAL                 = 12;
	public static final int ANOS_ALTERNADOS       = 13;

	public static final int OF_CONTRATO          = 0;
	public static final int OF_DOCUMENTO_ENTRADA = 1;
	public static final int OF_OUTRA_CONTA       = 2;

	/* codificação de erros retornados por rotinas relacionadas a manutenção de contas a pagar */
	public static final int ERR_NOT_CATEG_DESPESAS_DEFAULT = -2;

	/**
	 * @author ddk/Álvaro
	 * @param contaPagar
	 * @return
	 */
	public static Result save(ContaPagar contaPagar){
		return save(contaPagar, true, false, false, null, null, null);
	}
	public static Result save(ContaPagar contaPagar, AuthData authData){
		return save(contaPagar, true, false, false, null, authData, null);
	}
	/**
	 * @author Alvaro
	 * @param contaPagar
	 * @param ignorarDuplicidade
	 * @param repetirEmissao
	 * @param atualizarProximas
	 * @param categorias
	 * @param arquivos
	 * @param connect
	 * @since 30/03/2015
	 * @return
	 */
	public static Result save(ContaPagar contaPagar, boolean ignorarDuplicidade, boolean repetirEmissao, boolean atualizarProximas,
			ArrayList<ContaPagarCategoria> categorias, Connection connect){
		return save(contaPagar, ignorarDuplicidade, repetirEmissao, atualizarProximas, categorias, null, connect);
	}
	
	public static Result save(ContaPagar contaPagar, boolean ignorarDuplicidade, boolean repetirEmissao, boolean atualizarProximas,
			ArrayList<ContaPagarCategoria> categorias, AuthData authData, Connection connect){
		
		return save(contaPagar, ignorarDuplicidade, repetirEmissao, atualizarProximas, categorias, false, authData, connect);
	}
	
	public static Result save(ContaPagar contaPagar, boolean ignorarDuplicidade, boolean repetirEmissao, boolean atualizarProximas,
								ArrayList<ContaPagarCategoria> categorias, boolean lgRevisarClassificacoes, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(contaPagar==null)
				return new Result(-1, "Erro ao salvar. ContaPagar é nulo");
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.nr_documento", contaPagar.getNrDocumento(), ItemComparator.EQUAL, Types.VARCHAR));
			criterios.add(new ItemComparator("A.vl_conta", String.valueOf(contaPagar.getVlConta()), ItemComparator.EQUAL, Types.VARCHAR));
			criterios.add(new ItemComparator("A.cd_conta_pagar", String.valueOf(contaPagar.getCdContaPagar()), ItemComparator.DIFFERENT, Types.INTEGER));
			
			ResultSetMap rsmContaPagar = ContaPagarServices.find(criterios, connect);
			
			
			lgRevisarClassificacoes = !lgRevisarClassificacoes ? contaPagar.getTpFrequencia()>UNICA_VEZ : lgRevisarClassificacoes;
			
			if( contaPagar.getCdContaPagar()==0 && ParametroServices.getValorOfParametroAsInteger("LG_NUM_DOC_CONTA_PAGAR_AUTOMATICO", 0) > 0 )
				contaPagar.setNrDocumento( gerarNrDocumento( contaPagar.getCdEmpresa(), connect ) );
			//Verifica a Duplicidade de conta
			if( contaPagar.getCdContaPagar() <= 0 && isContaDuplicada(contaPagar, connect) )	{
				com.tivic.manager.util.Util.registerLog(new Exception("Conta a pagar duplicada!"));
				return new Result(-23, "Conta a pagar duplicada", "CONTAPAGAR", contaPagar);
			}
						
			int retorno;
			int tpAcao = ComplianceManager.TP_ACAO_ANY;
			if(contaPagar.getCdContaPagar()==0){
				contaPagar.setDtDigitacao(new GregorianCalendar());
				contaPagar.setDtVencimentoOriginal(contaPagar.getDtVencimento());
				retorno = ContaPagarDAO.insert(contaPagar, connect);
				contaPagar.setCdContaPagar(retorno);
				tpAcao = ComplianceManager.TP_ACAO_INSERT;
			}else {
				retorno = ContaPagarDAO.update(contaPagar, connect);
				tpAcao = ComplianceManager.TP_ACAO_UPDATE;
			}
			
			/**
			 * Gravando Categorias
			 */
			String idParam = contaPagar.getCdDocumentoEntrada()>0 ? "CD_CATEGORIA_ENTRADAS_DEFAULT" : "CD_CATEGORIA_DESPESAS_DEFAULT";
			int cdCategoriaDefault = ParametroServices.getValorOfParametroAsInteger(idParam, 0, contaPagar.getCdEmpresa(), connect);
			if( categorias == null ){
				/*
				 *  Criando categoria default
				 */
				categorias = new ArrayList<ContaPagarCategoria>();
				if (cdCategoriaDefault > 0){
					categorias.add(new ContaPagarCategoria(0, cdCategoriaDefault, contaPagar.getVlConta() - contaPagar.getVlAbatimento() + contaPagar.getVlAcrescimo(), 0));
					
				}else{
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-25, "Classificação padrão não está configurada!", "CONTAPAGAR", contaPagar);
				}
				
			}
			Double vlAClassificar = contaPagar.getVlConta(), vlClassificado = 0.0d;
			for( ContaPagarCategoria categoria : categorias ){
				vlClassificado += categoria.getVlContaCategoria();
				vlAClassificar -= categoria.getVlContaCategoria();
				categoria.setCdContaPagar( contaPagar.getCdContaPagar() );
				Result resultCategoria = ContaPagarCategoriaServices.save(categoria, authData, connect);
				if( resultCategoria.getCode() <= 0  ){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-26, "Conta a pagar não classificada corretamente!", "CONTAPAGAR", contaPagar);
				}
				
			}
			
			if(vlAClassificar > 0.01){
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-26,
						"Conta sem a devida classificação! Valor da Conta: "+Util.formatNumber(contaPagar.getVlConta())+", Valor classificado: "+Util.formatNumber(vlClassificado),
						"CONTAPAGAR", contaPagar); 
			}
			
			/**
			 * Gerando Repetições
			 */
			
			if(contaPagar.getTpFrequencia()>UNICA_VEZ && contaPagar.getCdContaOrigem()<=0 && tpAcao==ComplianceManager.TP_ACAO_INSERT)	{
				Result result = gerarParcelasOutraConta(contaPagar, repetirEmissao, connect);
			
				if( contaPagar.getCdContaPagar() > 0 && StringUtils.right(contaPagar.getNrDocumento(), 4 ).equals("-001") ){
					String nrDocumento = contaPagar.getNrDocumento().substring( 0, contaPagar.getNrDocumento().length() - 4 );
					contaPagar.setNrDocumento( nrDocumento );
				}
				ContaPagarDAO.update(contaPagar, connect);
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					com.tivic.manager.util.Util.registerLog(new Exception(result.getMessage()));
					return result;
				}
				
				/*
				 * Atualiza próximas
				 */
				//XXX:
				System.out.println("atualizarProximas: "+atualizarProximas);
				if(atualizarProximas)	{
					PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_conta_pagar " +
							                                   			  "WHERE cd_conta_origem IN ("+contaPagar.getCdContaPagar()+","+contaPagar.getCdContaOrigem()+") " +
							                                   			  "  AND dt_vencimento > ?");
					pstmt.setTimestamp(1, new Timestamp(contaPagar.getDtVencimento().getTimeInMillis()));
					ResultSet rs = pstmt.executeQuery();
					while(rs.next()){
						ContaPagar conta = ContaPagarDAO.get(rs.getInt("cd_conta_pagar"), connect);
						conta.setCdTipoDocumento(contaPagar.getCdTipoDocumento());
						conta.setCdPessoa(contaPagar.getCdPessoa());
						if(contaPagar.getTpFrequencia()==QUANTIDADE_FIXA)
							conta.setDtEmissao(contaPagar.getDtEmissao());
						conta.setVlBaseAutorizacao(contaPagar.getVlBaseAutorizacao());
						conta.setVlConta(contaPagar.getVlConta());
						conta.setVlAbatimento(contaPagar.getVlAbatimento());
						conta.setVlAcrescimo(contaPagar.getVlAcrescimo());
						conta.setCdConta(contaPagar.getCdConta());
						conta.setCdContaBancaria(contaPagar.getCdContaBancaria());
						conta.setQtParcelas(contaPagar.getQtParcelas());
						conta.setTxtObservacao(contaPagar.getTxtObservacao());
						conta.setDsHistorico(contaPagar.getDsHistorico());
						conta.setNrDocumento(contaPagar.getNrDocumento());
						// Categorias
						connect.prepareStatement("DELETE FROM adm_conta_pagar_categoria WHERE cd_conta_pagar = "+conta.getCdContaPagar()).executeUpdate();
						for(int i=0; i<categorias.size(); i++)
							categorias.get(i).setCdContaPagar(conta.getCdContaPagar());
						if(update(conta, categorias, false, authData, connect).getCode() < 1)	{
							if (isConnectionNull)
								Conexao.rollback(connect);
							Exception e = new Exception("Nao foi possivel atualizar proximas contas!");
							com.tivic.manager.util.Util.registerLog(e);
							return new Result(-1, e.getMessage(), e);
						}
					}
				}
			}
			
			/**
			 * Altera a base de autorização para parcelas subsequentes
			 */
			if( contaPagar.getCdContaOrigem() > 0 && contaPagar.getVlBaseAutorizacao() > 0 ){
				connect.prepareStatement(" UPDATE ADM_CONTA_PAGAR SET VL_BASE_AUTORIZACAO = "+contaPagar.getVlBaseAutorizacao()+
										 " WHERE CD_CONTA_ORIGEM =  "+contaPagar.getCdContaOrigem()+
										 " AND DT_DIGITACAO > '"+Util.formatDate(contaPagar.getDtDigitacao(), "yyyy-MM-dd HH:mm:ss.SSS")+"'"
										).executeUpdate();
			}
			
			/** **************************************************
			 * reclassifica a(s) conta(s).
			 * 
			 * tenta resolver o erro de classificação dos métodos que geram parcelas
			 * 
			 * @since 06/12/2017
			 * @author mauricio
			 * @comment pure XGH
			 */
			if(lgRevisarClassificacoes && atualizarProximas && tpAcao==ComplianceManager.TP_ACAO_INSERT) {
				/*
				 * 1. listar conta principal e derivadas (parcelas)
				 * 2. para cada conta de (1.)
				 * 2.1. limpar classificações
				 * 2.2. salvar classificações
				 */
				
				//1.
				ArrayList<Integer> listContas = new ArrayList<>();
				listContas.add(contaPagar.getCdContaPagar());
				
				PreparedStatement ps = connect.prepareStatement("SELECT cd_conta_pagar FROM adm_conta_pagar WHERE cd_conta_origem = "+contaPagar.getCdContaPagar());
				ResultSetMap rsmAux = new ResultSetMap(ps.executeQuery());
				while(rsmAux.next()) {
					listContas.add(rsmAux.getInt("cd_conta_pagar"));
				}
				
				//2.
				for (Integer cdConta : listContas) {
					//2.1.
					if(ContaPagarCategoriaServices.deleteAll(cdConta, connect) < 0) {
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-66, "Erro ao revisar categorias");
					}
										
					//2.2.
					if(categorias == null) { // default
						categorias = new ArrayList<ContaPagarCategoria>();
						if (cdCategoriaDefault > 0){
							categorias.add(new ContaPagarCategoria(0, cdCategoriaDefault, contaPagar.getVlConta() - contaPagar.getVlAbatimento() + contaPagar.getVlAcrescimo(), 0));
							
						} else {
							if (isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-25, "Classificação padrão não está configurada!", "CONTAPAGAR", contaPagar);
						}
					}
					for(ContaPagarCategoria categoria : categorias) {
						vlClassificado += categoria.getVlContaCategoria();
						vlAClassificar -= categoria.getVlContaCategoria();
						
						categoria.setCdContaPagar(cdConta);
						categoria.setCdContaPagarCategoria(0);
						
						Result resultCategoria = ContaPagarCategoriaServices.save(categoria, authData, connect);
						if(resultCategoria.getCode() <= 0){
							if (isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-26, "Conta a pagar não classificada corretamente!", "CONTAPAGAR", contaPagar);
						}
					}
				}
			}/** ************************************************** */
			
			
			if(contaPagar.getCdContaPagar()<=0)	{
				if (isConnectionNull)
					Conexao.rollback(connect);
				Exception e = new Exception("Nao foi possível salvar a conta a pagar!");
				com.tivic.manager.util.Util.registerLog(e);
				return new Result(contaPagar.getCdContaPagar(), e.getMessage(), e);
			}
			
			//COMPLIANCE
			ComplianceManager.process(contaPagar, authData, tpAcao, connect);
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONTAPAGAR", contaPagar);
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
	 * @author Alvaro
	 * @param contaPagar
	 * @return true or false
	 */
	private static boolean isContaDuplicada(ContaPagar contaPagar, Connection connect){
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			String sql = "SELECT * FROM adm_conta_pagar " +
	   			  "WHERE cd_empresa = "+contaPagar.getCdEmpresa()+
	   			  "  AND cd_pessoa  = "+contaPagar.getCdPessoa()+
	   			  "  AND vl_conta   = "+contaPagar.getVlConta()+
	   			  (nrDocumentoIsNotNull(contaPagar) ? "  AND nr_documento "+
	   				(Util.getConfManager().getIdOfDbUsed().equals("FB") ? " like " : " ilike ") +
	   				" '"+ contaPagar.getNrDocumento() + "'" : "" )+
	   			  "  AND dt_vencimento = ?";
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(contaPagar.getDtVencimento().getTimeInMillis()));
			
			if(pstmt.executeQuery().next())	{
				com.tivic.manager.util.Util.registerLog(new Exception("Conta a pagar duplicada!"));
				return true;
			}
			return false;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return false;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * @author Álvaro
	 * @param cdContaFinanceira
	 * @return seguinte padrão: 000 00 00 00, sendo os 3 primeiros dígitos 
	 *  sequenciais representando a quantidade de contas lançada no dia,
	 *  seguidos de 2 dígitos para o dia, 2 dígitos para o mês e 2 dígitos para o ano. Ex.: Oitava conta lançada dia 25/01/2017, 008250117.
	 */
	private static String gerarNrDocumento( int cdEmpresa, Connection connect ){
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			GregorianCalendar today = new GregorianCalendar();
			String nrDocumento  = "001"+Util.formatDate(today, "ddMMYY");
			ResultSet rs = connect.prepareStatement(" SELECT COUNT(cd_conta_pagar) as QT_CONTAS , MAX(NR_DOCUMENTO) as NR_DOCUMENTO FROM ADM_CONTA_PAGAR "+
													" WHERE DT_DIGITACAO BETWEEN "+
													"         '"+( Util.formatDate(today, "yyyy-MM-dd")+" 00:00:00'" )+
													" AND     '"+( Util.formatDate(today, "yyyy-MM-dd")+" 23:59:59'" )+
													" AND CD_EMPRESA = "+cdEmpresa
					).executeQuery();
			
			rs.next();
			if( rs.getInt("QT_CONTAS") > 0 ){
				int qtContasDia = Integer.parseInt( rs.getString("NR_DOCUMENTO").substring(0, 3) )+1;
				nrDocumento = Util.fillNum( qtContasDia, 3)+Util.formatDate(today, "ddMMYY");
			}
			
			rs.close();
			return nrDocumento;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Verifica se o nrDocumento é nulo, vazio ou preenchido só com espaços em branco
	 * 
	 * @param objeto conta a ser verificada
	 * @return retorna true se o nrDocumento estiver preenchido corretamente, e false caso contrário
	 * @author Luiz Romario Filho
	 * @since 29/08/2014
	 */
	private static boolean nrDocumentoIsNotNull(ContaPagar objeto) {
		return objeto.getNrDocumento() != null && !"".equalsIgnoreCase(objeto.getNrDocumento().trim());
	}
	
	/**
	 * @author Álvaro
	 * @param cdContaPagar
	 * @return
	 */
	public static Result autorizarPagamento(int[] cdContasPagar){
		return autorizarPagamento(cdContasPagar, null);
	}
	public static Result autorizarPagamento(int[] contasPagar, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			ArrayList<ContaPagar> contas = new ArrayList<ContaPagar>();
			for( int i=0;i<contasPagar.length;i++ ){
				int retorno = setAutorizacaoPagamento(contasPagar[i], connect);
				if( retorno < 0 ){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result( retorno,"Erro ao autorizar contas!");
				}
				ContaPagar conta = ContaPagarDAO.get(contasPagar[i]);
				conta.setLgAutorizado(1);
				contas.add(conta);
			}
			connect.commit();
			return new Result( 2,"Conta Autorizada com Sucesso!", "CONTAPAGAR", contas );
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
	 * @author Álvaro
	 * @param cdContaPagar
	 * @return
	 */
	public static Result autorizarPagamento(int cdContaPagar, AuthData auth) {
		int retorno = setAutorizacaoPagamento(cdContaPagar);
		ContaPagar conta = ContaPagarDAO.get(cdContaPagar);
		
		if(retorno>0)
			ComplianceManager.process(conta, auth, ComplianceManager.TP_ACAO_UPDATE, Conexao.conectar());
		
		return new Result( retorno, retorno>0?"Conta Autorizada com Sucesso!":"Erro ao autorizar pagamento da conta", "CONTAPAGAR", conta );
	}
	
	public static Result remove( int contas[] ){
		return remove( contas, null );
	}
	public static Result remove(int contas[], Connection connect){
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			Result r;
			for( int i=0;i<contas.length;i++ ){
				r = remove(contas[i], true, connect);
				if( r.getCode() <= 0 ){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao excluir contas!");
				}
			}
			connect.commit();
			return new Result(1, "Contas a Pagar excluídas com sucesso!");
			
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
	 * @author ddk/Álvaro
	 * @param contaPagar
	 * @return
	 */
	public static Result remove(int cdContaPagar){
		return remove(cdContaPagar, true, null);
	}
	/**
	 * @author ddk/Álvaro
	 * @param contaPagar
	 * @param cascade
	 * @return
	 */
	public static Result remove(int cdContaPagar, boolean cascade){
		return remove(cdContaPagar, cascade, null);
	}
	/**
	 * @author ddk/Álvaro
	 * @param contaPagar
	 * @return
	 */
	public static Result remove(int cdContaPagar, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			
			/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
			if(cascade){
				Result result = new Result(1);
				/**
				 * Verifica Movimentação da conta
				 */
				ResultSet rs = connect.prepareStatement("SELECT * FROM adm_movimento_conta_pagar WHERE cd_conta_pagar = "+cdContaPagar).executeQuery();
				if(rs.next())
					return new Result(-1, "Já existe um pagamento informado para essa conta, é mais seguro que você exclua antes esse pagamento!");
				
				// Tenta excluir parcelas
				rs = connect.prepareStatement("SELECT * FROM adm_conta_pagar WHERE cd_conta_origem = "+cdContaPagar).executeQuery();
				while(rs.next())	{

					if ((result=delete(rs.getInt("cd_conta_pagar"), cascade, connect)).getCode() < 1)	{
						if (isConnectionNull)
							Conexao.rollback(connect);
						return result;
					}
				}
				PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_factoring WHERE cd_conta_pagar = "+cdContaPagar);
				int ret = pstmt.executeUpdate();
				if(ret < 0){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar contas factoring!");
				}
				
				// tenta apagar os eventos relacionados à Conta a Pagar
				if (ContaPagarEventoServices.deleteAll(cdContaPagar, connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Não foi possível excluir todos os eventos financeiros ligados a essa conta a pagar!");
				}

				// tenta apagar as classificações financeiras relacionadas à Conta a Pagar
				if (ContaPagarCategoriaServices.deleteAll(cdContaPagar, connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Não foi possível excluir todas as categorias ligadas a essa conta a pagar!");
				}
				// tenta apagar arquivos de conta a pagar
				if (ContaPagarArquivoServices.removeAll(cdContaPagar, connect).getCode() <= 0)
					throw new Exception("Erro ao tentar excluir arquivos da conta a pagar!");
				
				//Cancelando faturamento de processo financeiro
				if( ProcessoFinanceiroServices.getByContaPagar(cdContaPagar, connect) != null ){
					Result r = ProcessoFinanceiroServices.cancelarFaturamentoContaPagar(cdContaPagar, connect);
					if (r.getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(r.getCode(), r.getMessage());
					}
				}
				
				retorno = 1;
			}
			
			if(!cascade || retorno>0)
				retorno = ContaPagarDAO.delete(cdContaPagar, connect);
			
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
	/**
	 * @author ddk/Álvaro
	 * @param contaPagar
	 * @return
	 */
    public static ResultSetMap getAll() {
		return getAll(null);
	}
    /**
	 * @author ddk/Álvaro
	 * @param contaPagar
	 * @return
	 */
	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_pagar");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
	 * @author Álvaro
	 * @param cdContaPagar
	 * @return
	 */
	public static ResultSetMap getArquivos( ContaPagar conta){
		return getArquivos( conta, null);
	}
	/**
	 * @author Álvaro
	 * @param cdContaPagar
	 * @return
	 */
	public static ResultSetMap getArquivos( ContaPagar conta, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_arquivo WHERE cd_arquivo = "+conta.getCdArquivo());
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.getArquivos: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.getArquivos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int setVencimentoToEmissao(int cdContaOrigem) {
		return setVencimentoToEmissao(cdContaOrigem, null);
	}

	
	public static int setVencimentoToEmissao(int cdContaOrigem, Connection connection) {
		boolean isConnectionNull = true;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement("UPDATE adm_conta_pagar " +
					"SET dt_emissao = dt_vencimento " +
					"WHERE cd_conta_origem = ? " +
					"  AND NOT cd_conta_pagar IN (SELECT cd_conta_pagar " +
					"							  FROM adm_movimento_conta_pagar)");
			pstmt.setInt(1, cdContaOrigem);
			pstmt.execute();

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
	
	public static Result savePagamentoAvulso(ContaPagar contaPagar, int cdCategoriaEconomica, int cdFormaPagamento, int cdUsuario, int cdContaReceberPermuta){
		return savePagamentoAvulso(contaPagar, cdCategoriaEconomica, cdFormaPagamento, cdUsuario, cdContaReceberPermuta, 0);
	}
	
	public static Result savePagamentoAvulso(ContaPagar contaPagar, int cdCategoriaEconomica, int cdFormaPagamento, int cdUsuario, int cdContaReceberPermuta, int cdCentroCusto) {
		
				Result result = insertPagamentoAvulso(contaPagar.getCdEmpresa(), contaPagar.getCdConta(), contaPagar.getCdPessoa(),contaPagar.getCdTipoDocumento()
				, contaPagar.getVlConta(), contaPagar.getDsHistorico(), contaPagar.getDtPagamento()
				, cdCategoriaEconomica, cdFormaPagamento, cdUsuario, cdContaReceberPermuta
				, contaPagar.getCdTurno(), cdCentroCusto, contaPagar.getNrDocumento());
				if( result.getCode() > 1 ){
					result.setDetail("");
					result.setMessage("Pagamento avulso realizado com sucesso.");
					HashMap<String, Object> objects = new HashMap<String, Object>();
					objects.put("CONTAPAGAR",contaPagar);
					result.setObjects(objects);
				}
				return result;
	}
	
	public static Result insertPagamentoAvulso(int cdEmpresa, int cdConta, int cdPessoa, int cdTipoDocumento, Double vlConta, String dsHistorico,
			GregorianCalendar dtPagamento, int cdCategoriaEconomica, int cdFormaPagamento, int cdUsuario,
			int cdContaReceberPermuta, int cdTurno) {
		return insertPagamentoAvulso(cdEmpresa, cdConta, cdPessoa, cdTipoDocumento, vlConta, dsHistorico, dtPagamento, cdCategoriaEconomica, cdFormaPagamento,
				cdUsuario, cdContaReceberPermuta, cdTurno, 0, "", null);
	}
	
	public static Result insertPagamentoAvulso(int cdEmpresa, int cdConta, int cdPessoa, int cdTipoDocumento, Double vlConta, String dsHistorico,
			GregorianCalendar dtPagamento, int cdCategoriaEconomica, int cdFormaPagamento, int cdUsuario,
			int cdContaReceberPermuta, int cdTurno, int cdCentroCusto, String nrDocumento) {
		return insertPagamentoAvulso(cdEmpresa, cdConta, cdPessoa, cdTipoDocumento, vlConta, dsHistorico, dtPagamento, cdCategoriaEconomica, cdFormaPagamento,
				cdUsuario, cdContaReceberPermuta, cdTurno, cdCentroCusto, nrDocumento, null);
	}
	public static Result insertPagamentoAvulso(int cdEmpresa, int cdConta, int cdPessoa, int cdTipoDocumento, Double vlConta, String dsHistorico,
			GregorianCalendar dtPagamento, int cdCategoriaEconomica, int cdFormaPagamento, int cdUsuario,
			int cdContaReceberPermuta, int cdTurno, int cdCentroCusto) {
		return insertPagamentoAvulso(cdEmpresa, cdConta, cdPessoa, cdTipoDocumento, vlConta, dsHistorico, dtPagamento, cdCategoriaEconomica, cdFormaPagamento,
				cdUsuario, cdContaReceberPermuta, cdTurno, cdCentroCusto, "", null);
	}
	
	public static Result insertPagamentoAvulso(int cdEmpresa, int cdConta, int cdPessoa, int cdTipoDocumento, Double vlConta, String dsHistorico,
											GregorianCalendar dtPagamento, int cdCategoriaEconomica, int cdFormaPagamento, int cdUsuario,
											int cdContaReceberPermuta, int cdTurno, int cdCentroCusto, String nrDocumento, Connection connection) {
		boolean isConnectionNull = true;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			dsHistorico = dsHistorico!=null && dsHistorico.trim().length()>100 ? dsHistorico.substring(0, 100) : dsHistorico;

			ContaPagar contaPagar = new ContaPagar(0/*cdContaPagar*/,0/*cdContrato*/,cdPessoa,cdEmpresa,0/*cdContaOrigem*/,0/*cdDocumentoEntrada*/,
												   cdConta,0/*cdContaBancaria*/,dtPagamento/*dtVencimento*/,dtPagamento /*dtEmissao*/,
												   dtPagamento,dtPagamento /*dtAutorizacao*/,nrDocumento/*nrDocumento*/,"" /*nrReferencia*/,
												   1 /*nrParcela*/,cdTipoDocumento,vlConta,0 /*vlAbatimento*/,0 /*vlAcrescimo*/,
												   0 /*vlPago*/,dsHistorico,ST_EM_ABERTO,1 /*lgAutorizado*/,0 /*tpFrequencia*/,
												   1 /*qtParcelas*/,vlConta /*vlBaseAutorizacao*/,0 /*cdViagem*/,
												   0 /*cdManutencao*/,"" /*txtObservacao*/,new GregorianCalendar(),dtPagamento, cdTurno);

			ArrayList<ContaPagarCategoria> cats = new ArrayList<ContaPagarCategoria>();
			cats.add(new ContaPagarCategoria(contaPagar.getCdContaPagar(), cdCategoriaEconomica, vlConta, cdCentroCusto));

			Result result = insert(contaPagar, true, false, cats, connection);

			contaPagar.setCdContaPagar(result.getCode());
			if(contaPagar.getCdContaPagar() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}

			Result retPagamento = setPagamentoResumido(contaPagar.getCdContaPagar(), cdConta, 0.0d /*vlMulta*/, 0 /*cdCategoriaMulta*/, 0.0d /*vlJuros*/,
					                                   0 /*cdCategoriaJuros*/, 0.0d /*vlDesconto*/, 0 /*cdCategoriaDesconto*/, new Double(vlConta), cdFormaPagamento,
					                                   0/*cdCheque*/, dsHistorico, cdUsuario, dtPagamento, -1, 0, cdTurno, null, connection);

			if(retPagamento.getCode() <= 0)	{
				if(isConnectionNull)
					Conexao.rollback(connection);
				retPagamento.setMessage("Não foi possível salvar o pagamento resumido! "+retPagamento.getMessage());
				return retPagamento;
			}

			if(cdContaReceberPermuta>0)	{
				TituloCredito tituloCredito = new TituloCredito(1 /*cdTituloCredito*/, 0 /*cdInstituicaoFinanceira*/, 0 /*cdAlinea*/,
																"" /*nrDocumento*/, "" /*nrDocumentoEmissor*/, 0 /*tpDocumentoEmissor*/,
																"" /*nmEmissor*/, 0.0 /*vlTitulo*/, 0 /*tpEmissoa*/, "" /*nrAgencia*/,
																null /*dtVencimento*/, null /*dtCredito*/, 0 /*stTitulo*/, "" /*dsObservacao*/,
																0 /*cdTipoDocumento*/, 0 /*tpCirculacao*/, 0 /*cdConta*/, cdContaReceberPermuta, 0, 0, "");
				Result retBaixaResumida = ContaReceberServices.setBaixaResumida(cdContaReceberPermuta, cdConta, 0.0 /*vlMulta*/, 0/*cdCategoriaMulta*/, 
						                                                        0.0/*vlJuros*/, 0/*cdCategoriaJuros*/, 0.0/*vlDesconto*/, 0/*cdCategoriaDesconto*/,  
						                                                        new Double(vlConta), cdFormaPagamento, tituloCredito, cdUsuario, dtPagamento, dtPagamento, 
						                                                        "Pagamento com permuta", connection);
				if(retBaixaResumida.getCode() <= 0)	{
					if(isConnectionNull)
						Conexao.rollback(connection);
					retBaixaResumida.setMessage("Não foi possível salvar conta a receber gerada por permuta. "+retBaixaResumida.getMessage());
					return retBaixaResumida;
				}
			}
			if(isConnectionNull)
				connection.commit();

			return new Result(contaPagar.getCdContaPagar());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar salvar conta avulsa!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result insert(ContaPagar objeto, boolean ignorarDuplicidade, boolean repetirEmissao) {
		return insert(objeto, ignorarDuplicidade, repetirEmissao, (Connection)null);
	}

	public static Result insert(ContaPagar objeto, boolean ignorarDuplicidade, boolean repetirEmissao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			String idParam = objeto.getCdDocumentoEntrada()>0 ? "CD_CATEGORIA_ENTRADAS_DEFAULT" : "CD_CATEGORIA_DESPESAS_DEFAULT";
			int cdCategoriaDefault = ParametroServices.getValorOfParametroAsInteger(idParam, 0, objeto.getCdEmpresa(), connection);
			if (cdCategoriaDefault <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				Exception e = new Exception("Conta a pagar não classificada corretamente!");
				com.tivic.manager.util.Util.registerLog(e); // Importante para o PDV
				return new Result(ERR_NOT_CATEG_DESPESAS_DEFAULT, e.getMessage(), e);
			}

			ArrayList<ContaPagarCategoria> categorias = new ArrayList<ContaPagarCategoria>();
			if (cdCategoriaDefault > 0)
				categorias.add(new ContaPagarCategoria(0, cdCategoriaDefault, objeto.getVlConta() - objeto.getVlAbatimento() + objeto.getVlAcrescimo(), 0));

			Result result = insert(objeto, ignorarDuplicidade, repetirEmissao, categorias, connection);
			if (result.getCode() <= 0) {
				Exception e = new Exception("Conta a pagar não foi gerada corretamente por: ContaPagarServices.insert(objeto, ignorarDuplicidade, categorias)");
				com.tivic.manager.util.Util.registerLog(e);
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
			return new Result(-1, "Não foi possível inserir conta a pagar!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result insert(ContaPagar objeto, boolean ignorarDuplicidade, boolean repetirEmissao, ArrayList<ContaPagarCategoria> categorias) {
		return insert(objeto, ignorarDuplicidade, repetirEmissao, categorias, null, (Connection)null);
	}
	
	public static Result insert(ContaPagar objeto, boolean ignorarDuplicidade, boolean repetirEmissao, ArrayList<ContaPagarCategoria> categorias, AuthData authData) {
		return insert(objeto, ignorarDuplicidade, repetirEmissao, categorias, authData, (Connection)null);
	}
	
	public static Result insert(ContaPagar objeto, boolean ignorarDuplicidade, boolean repetirEmissao, ArrayList<ContaPagarCategoria> categorias, Connection connection) {
		return insert(objeto, ignorarDuplicidade, repetirEmissao, categorias, null, connection);
	}

	public static Result insert(ContaPagar objeto, boolean ignorarDuplicidade, boolean repetirEmissao, ArrayList<ContaPagarCategoria> categorias, 
			AuthData authData, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			if(!ignorarDuplicidade)	{
				PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM adm_conta_pagar " +
						                                   			  "WHERE cd_empresa = "+objeto.getCdEmpresa()+
						                                   			  "  AND cd_pessoa  = "+objeto.getCdPessoa()+
						                                   			  "  AND vl_conta   = "+objeto.getVlConta()+
						                                   			  "  AND dt_vencimento = ?");
				pstmt.setTimestamp(1, new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
				if(pstmt.executeQuery().next())	{
					Exception e = new Exception("Conta a pagar duplicada!");
					com.tivic.manager.util.Util.registerLog(e);
					return new Result(-23, e.getMessage(), e);
				}

			}
			objeto.setDtDigitacao(new GregorianCalendar());
			objeto.setDtVencimentoOriginal(objeto.getDtVencimento());
			objeto.setCdContaPagar(ContaPagarDAO.insert(objeto, connection));

			if(objeto.getCdContaPagar()<=0)	{
				if (isConnectionNull)
					Conexao.rollback(connection);
				Exception e = new Exception("Nao foi possível salvar a conta a pagar!");
				com.tivic.manager.util.Util.registerLog(e);
				return new Result(objeto.getCdContaPagar(), e.getMessage(), e);
			}
			
//			System.out.println("cat: "+objeto);
//			System.out.println("categorias: "+categorias);
			Double vlAClassificar = objeto.getVlConta(), vlClassificado = 0.0d;
			for(int i=0; categorias!=null && i<categorias.size(); i++)	{
				vlClassificado += categorias.get(i).getVlContaCategoria();
				vlAClassificar -= categorias.get(i).getVlContaCategoria();
				categorias.get(i).setCdContaPagar(objeto.getCdContaPagar());
				int ret = ContaPagarCategoriaDAO.insert(categorias.get(i), connection);
				if (ret <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					Exception e = new Exception("Nao foi possivel salvar classificacao da conta a pagar em categorias!");
					com.tivic.manager.util.Util.registerLog(e);
					return new Result(-1, e.getMessage(), e);
				}
			}
			if (vlAClassificar > 0.01)	{
				if (isConnectionNull)
					Conexao.rollback(connection);
				Exception e = new Exception("Conta sem a devida classificação! Valor da Conta: "+Util.formatNumber(objeto.getVlConta())+", Valor classificado: "+Util.formatNumber(vlClassificado));
				com.tivic.manager.util.Util.registerLog(e);
				return new Result(-10, e.getMessage(), e);
			}

			if(objeto.getTpFrequencia()>UNICA_VEZ && objeto.getCdContaOrigem()<=0)	{
				Result result = gerarParcelasOutraConta(objeto, repetirEmissao, connection);
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					com.tivic.manager.util.Util.registerLog(new Exception(result.getMessage()));
					return result;
				}
			}
			
			//COMPLIANCE
			ComplianceManager.process(objeto, authData, ComplianceManager.TP_ACAO_INSERT, connection);

			if (isConnectionNull)
				connection.commit();
			
			return new Result(objeto.getCdContaPagar(), "Incluído com sucesso!", "rsm", getAsResultSet(objeto.getCdContaPagar()));
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			System.err.println("Erro! ContaPagarServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar incluir conta à pagar!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result update(ContaPagar objeto, boolean updateNext) {
		return update(objeto, updateNext, null);
	}
	
	public static Result update(ContaPagar objeto, boolean updateNext, AuthData authData) {
		boolean isConnectionNull = true;
		Connection connection = Conexao.conectar();
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			String idParam = objeto.getCdDocumentoEntrada()>0 ? "CD_CATEGORIA_ENTRADAS_DEFAULT" : "CD_CATEGORIA_DESPESAS_DEFAULT";
			int cdCategoriaDefault = ParametroServices.getValorOfParametroAsInteger(idParam, 0, objeto.getCdEmpresa(), connection);
			if (cdCategoriaDefault <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				Exception e = new Exception("Conta a pagar não classificada corretamente!");
				com.tivic.manager.util.Util.registerLog(e); // Importante para o PDV
				return new Result(ERR_NOT_CATEG_DESPESAS_DEFAULT, e.getMessage(), e);
			}

			ArrayList<ContaPagarCategoria> categorias = new ArrayList<ContaPagarCategoria>();
			if (cdCategoriaDefault > 0)
				categorias.add(new ContaPagarCategoria(0, cdCategoriaDefault, objeto.getVlConta() - objeto.getVlAbatimento() + objeto.getVlAcrescimo(), 0));

			Result result = update(objeto, categorias, updateNext, authData, connection);
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
			return new Result(-1, "Não foi possível inserir conta a pagar!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result update(ContaPagar objeto, ArrayList<ContaPagarCategoria> categorias, boolean updateNext) {
		return update(objeto, categorias, updateNext, null, null);
	}
	
	public static Result update(ContaPagar objeto, ArrayList<ContaPagarCategoria> categorias, boolean updateNext, AuthData authData) {
		return update(objeto, categorias, updateNext, authData, null);
	}

	public static Result update(ContaPagar objeto, ArrayList<ContaPagarCategoria> categorias, boolean updateNext, AuthData authData, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			/*
			 * Tentando atualizar
			 */
			if (ContaPagarDAO.update(objeto, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				Exception e = new Exception("Não foi possível atualizar conta à pagar!");
				com.tivic.manager.util.Util.registerLog(e);
				return new Result(-1, e.getMessage(), e);
			}
			//COMPLIANCE
			ComplianceManager.process(objeto, authData, ComplianceManager.TP_ACAO_UPDATE, connection);
			
			/*
			 * Gera repetições && objeto.getCdContaOrigem()<=0
			 */
			if (objeto.getTpFrequencia()>UNICA_VEZ )	{
				Result result = gerarParcelasOutraConta(objeto, true, connection);
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					com.tivic.manager.util.Util.registerLog(new Exception(result.getMessage()));
					return result;
				}
				/*
				 * Atualiza próximas
				 */
				if(updateNext)	{
					PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM adm_conta_pagar " +
							                                   			  "WHERE cd_conta_origem IN ("+objeto.getCdContaPagar()+","+objeto.getCdContaOrigem()+") " +
							                                   			  "  AND dt_vencimento > ?");
					pstmt.setTimestamp(1, new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
					ResultSet rs = pstmt.executeQuery();
					while(rs.next()){
						ContaPagar conta = ContaPagarDAO.get(rs.getInt("cd_conta_pagar"), connection);
						conta.setCdTipoDocumento(objeto.getCdTipoDocumento());
						conta.setCdPessoa(objeto.getCdPessoa());
						if(objeto.getTpFrequencia()==QUANTIDADE_FIXA)
							conta.setDtEmissao(objeto.getDtEmissao());
						conta.setVlBaseAutorizacao(objeto.getVlBaseAutorizacao());
						conta.setVlConta(objeto.getVlConta());
						conta.setVlAbatimento(objeto.getVlAbatimento());
						conta.setVlAcrescimo(objeto.getVlAcrescimo());
						conta.setCdConta(objeto.getCdConta());
						conta.setCdContaBancaria(objeto.getCdContaBancaria());
						conta.setQtParcelas(objeto.getQtParcelas());
						conta.setTxtObservacao(objeto.getTxtObservacao());
						conta.setDsHistorico(objeto.getDsHistorico());
						conta.setNrDocumento(objeto.getNrDocumento());
						// Categorias
						connection.prepareStatement("DELETE FROM adm_conta_pagar_categoria WHERE cd_conta_pagar = "+conta.getCdContaPagar()).executeUpdate();
						for(int i=0; i<categorias.size(); i++)
							categorias.get(i).setCdContaPagar(conta.getCdContaPagar());
						if(update(conta, categorias, false, authData, connection).getCode() < 1)	{
							if (isConnectionNull)
								Conexao.rollback(connection);
							Exception e = new Exception("Nao foi possivel atualizar proximas contas!");
							com.tivic.manager.util.Util.registerLog(e);
							return new Result(-1, e.getMessage(), e);
						}
					}
				}
			}
			/*
			 *  Gravando categorias
			 */
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM adm_conta_pagar_categoria " +
					                                              "WHERE cd_conta_pagar         = "+objeto.getCdContaPagar()+
					                                              "  AND cd_categoria_economica = ?");
			for(int i=0; categorias!=null && i<categorias.size(); i++)	{
				int ret = 1;
				categorias.get(i).setCdContaPagar(objeto.getCdContaPagar());
				pstmt.setInt(1, categorias.get(i).getCdCategoriaEconomica());
				if(categorias.size() > 1){
					if(pstmt.executeQuery().next())
						ret = ContaPagarCategoriaDAO.update(categorias.get(i), connection);
					else
						ret = ContaPagarCategoriaDAO.insert(categorias.get(i), connection);
				}
				else{
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_conta_pagar", Integer.toString(categorias.get(i).getCdContaPagar()), ItemComparator.EQUAL, Types.INTEGER));
					
					ResultSetMap rsm = ContaPagarCategoriaDAO.find(criterios, connection);
					rsm.next();
					
					if(ContaPagarCategoriaDAO.delete(categorias.get(i).getCdContaPagar(), rsm.getInt("cd_categoria_economica")) <= 0){
						if (isConnectionNull)
							Conexao.rollback(connection);
						Exception e = new Exception("Erro ao deletar a conta de categoria da Conta a Pagar!");
						com.tivic.manager.util.Util.registerLog(e);
						return new Result(-1, e.getMessage(), e);
					}
					
					ret = ContaPagarCategoriaDAO.insert(categorias.get(i), connection);
				}
				if (ret <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					com.tivic.manager.util.Util.registerLog(new Exception("Conta a pagar não classificada corretamente! ERRO: "+ret));
					return new Result(-20, "Conta a pagar não classificada corretamente! ERRO: "+ret, new Exception("Conta a pagar não classificada corretamente! ERRO: "+ret));
				}
			}
			if (isConnectionNull)
				connection.commit();

			return new Result(1, "Atualizado com sucesso!", "rsm", getAsResultSet(objeto.getCdContaPagar()));
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar salvar conta à pagar!", e);
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	public static Result lancarAndamentoProcessoFinanceiro(int cdEventoFinanceiro, int cdProcesso, int cdUsuario,int cdArquivo, ProcessoArquivo prcArquivo){
		return lancarAndamentoProcessoFinanceiro(cdEventoFinanceiro, cdProcesso, cdUsuario,cdArquivo, prcArquivo, null);
	}
	public static Result lancarAndamentoProcessoFinanceiro( int cdEventoFinanceiro, int cdProcesso, int cdUsuario,int cdArquivo, ProcessoArquivo prcArquivo, Connection connect ){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ProcessoFinanceiro prcFin = ProcessoFinanceiroDAO.get(cdEventoFinanceiro, cdProcesso);
			int cdTipoAndamento = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_ANDAMENTO_PROCESSO_FINANCEIRO", 0);
			if( cdTipoAndamento == 0 )
				return new Result(-1, "Tipo de Andamento não configurado!");
			if( cdArquivo == 0 )
				return new Result(-2, "Arquivo não informado!");
			
			TipoAndamento tpAndamento = TipoAndamentoDAO.get(cdTipoAndamento);
			
			ProcessoAndamento prcAndamento = new ProcessoAndamento(0/*cdAndamento*/, cdProcesso/*cdProcesso*/, cdTipoAndamento/*cdTipoAndamento*/,
			 												new GregorianCalendar()/*dtAndamento*/, new GregorianCalendar()/*dtLancamento*/,
			 												tpAndamento.getNmTipoAndamento()/*txtAndamento*/, 0/*stAndamento*/, 0/*tpInstancia*/,
			 												""/*txtAta*/, cdUsuario/*cdUsuario*/, null/*dtAlteracao*/, 0/*tpOrigem*/, null/*blbAta*/,
			 												0/*tpVisibilidade*/, 0/*tpEventoFinanceiro*/,
			 												prcFin.getVlEventoFinanceiro()/*vlEventoFinanceiro*/,
			 												prcFin.getCdContaPagar()/*cdContaPagar*/, 0/*cdContaReceber*/,
			 												null/*dtAtualizacaoEdi*/, 0/*stAtualizacaoEdi*/, null/*txtPublicacao*/,0 /*cdDocumento*/, 
			 												0 /*cdOrigemAndamento*/, 0 /*cdRecorte*/);
			Arquivo arquivo = ArquivoDAO.get(cdArquivo);
			prcArquivo.setCdProcesso(cdProcesso);
			prcArquivo.setDtArquivamento(arquivo.getDtArquivamento());
			prcArquivo.setBlbArquivo(arquivo.getBlbArquivo());
			
			Result result = ProcessoAndamentoServices.save(prcAndamento, prcArquivo, null, null, null, connect);
			if( result.getCode() < 0 ){
				connect.rollback();
				return result;
			}
			
			prcFin.setCdAndamento( ((ProcessoAndamento)result.getObjects().get("ANDAMENTO")).getCdAndamento() );
			
			result = ProcessoFinanceiroServices.save(prcFin, connect);
			if( result.getCode() < 0 ){
				connect.rollback();
				return result;
			}
			connect.commit();
			return new Result(1, "Andamento adicionado com sucesso!" );
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao Lançar andamento em Processo Financeiro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			int qtLimite = 0;
			int tpRelatorio = 0;
			String ordenacao = "ASC";
			Boolean orderByPrioritaria = false;
			for (int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					i--;
					continue;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("tpRelatorio")) {
					tpRelatorio = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					continue;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("A.st_conta") && 
						criterios.get(i).getValue().equals("-1")) {
					criterios.remove(i);
					continue;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("A.dt_vencimento_final") ) {
					criterios.get(i).setColumn("A.dt_vencimento");
					criterios.get(i).setValue( criterios.get(i).getValue().substring(0, 10)+" 23:59:59"  );
					continue;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("ordenacao")) {
					ordenacao = criterios.get(i).getValue();
					criterios.remove(i);
					i--;
					if( ordenacao.equals("ASC") || !ordenacao.equals("DESC")  )
						ordenacao = "ASC";
					continue;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("orderbyprioritaria")) {
					orderByPrioritaria = true;
					criterios.remove(i);
					i--;
					continue;
				}
			}
			
			String[] limitSkip = Util.getLimitAndSkip(qtLimite, 0);
			String nmPessoa = "";
			String dsHistorico = "";
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("C.NM_PESSOA")) {
					nmPessoa =	Util.limparTexto(criterios.get(i).getValue());
					nmPessoa = nmPessoa.trim();
					criterios.remove(i);
					i--;
				} else
					if (criterios.get(i).getColumn().equalsIgnoreCase("A.DS_HISTORICO")) {
						dsHistorico =	Util.limparTexto(criterios.get(i).getValue());
						dsHistorico = dsHistorico.trim();
						criterios.remove(i);
						i--;
					}
			}
			ResultSetMap rsm = Search.find("SELECT DISTINCT(A.cd_conta_pagar), "+limitSkip[0]+" A.*, H.nm_razao_social AS nm_empresa, I.nm_pessoa AS nm_fantasia, C.nm_pessoa, 		   " +
					           "       C.nm_pessoa AS nm_favorecido, C.cd_pessoa as cd_favorecido, C.st_cadastro,                      		   " +
					           "       D.nr_conta, D.nr_dv, D.nm_conta, E.nr_agencia,                                                  		   " +
					           "       F.nr_banco, F.nm_banco, G.nm_tipo_documento, G.sg_tipo_documento, J.nm_arquivo, L.*,        		       " +
					           //" 	   L.cd_evento_financeiro, L.cd_processo, L.cd_andamento,												   " +
					           " 	   ((A.vl_conta+A.vl_acrescimo-A.vl_abatimento)-A.vl_pago) as vl_apagar, 						           " +
					           " 	   M.*, M.cd_conta_bancaria AS cd_conta_bancaria_favorecido, M.cd_banco as cd_banco_favorecido,            " +
					           " 	   M.nr_conta AS nr_conta_favorecido, M.nr_dv as nr_dv_favorecido, M.nr_agencia as nr_agencia_favorecido,  " +
					           "       M.nr_agencia as nr_agencia_favorecido, M.nr_conta as nr_conta_favorecido, M.nr_dv as nr_dv_favorecido,  " +
					           "       M.tp_operacao as tp_operacao_favorecido,  															   " +
					           "       N.*, N.nr_banco as nr_banco_favorecido, N.nm_banco as nm_banco_favorecido, 						       " +
					           "       P.nm_pessoa as nm_usuario,																			   " +
					           " CASE WHEN cd_forma_pagamento_preferencial is null THEN 0 END                                                  " +
					           " FROM adm_conta_pagar A                  															           " +
					           " LEFT OUTER JOIN grl_empresa                 B   ON (A.cd_empresa = B.cd_empresa)                              " +
					           " LEFT OUTER JOIN grl_pessoa                  C   ON (A.cd_pessoa  = C.cd_pessoa)                               " +
					           " LEFT OUTER JOIN grl_pessoa_juridica         H   ON (B.cd_empresa  = H.cd_pessoa)                              " +
					           " LEFT OUTER JOIN grl_pessoa                  I   ON (H.cd_pessoa  = I.cd_pessoa)                               " +
					           " LEFT OUTER JOIN adm_conta_financeira        D   ON (A.cd_conta = D.cd_conta)                                  " +
					           " LEFT OUTER JOIN grl_agencia                 E   ON (D.cd_agencia = E.cd_agencia)                              " +
					           " LEFT OUTER JOIN grl_banco                   F   ON (E.cd_banco = F.cd_banco)                                  " +
					           " LEFT OUTER JOIN adm_tipo_documento          G   ON (A.cd_tipo_documento = G.cd_tipo_documento)                " +
					           " LEFT OUTER JOIN grl_arquivo                 J   ON (A.cd_arquivo = J.cd_arquivo)                              " +
					           " LEFT OUTER JOIN adm_forma_pagamento         L   ON (A.cd_forma_pagamento_preferencial = L.cd_forma_pagamento) " +
					           " LEFT OUTER JOIN grl_pessoa_conta_bancaria   M   ON (A.cd_conta_favorecido = M.cd_conta_bancaria 			   " + 
							   "														AND A.cd_pessoa =  M.cd_pessoa ) 					   " +
							   " LEFT OUTER JOIN grl_banco                   N   ON (M.cd_banco =  N.cd_banco ) 							   " +
							   " LEFT OUTER JOIN seg_usuario                 O   ON (A.cd_usuario =  O.cd_usuario ) 						   " +
							   " LEFT OUTER JOIN grl_pessoa                  P   ON (O.cd_pessoa =  P.cd_pessoa ) 							   " +
					           //" LEFT OUTER JOIN prc_processo_financeiro     L   ON (A.cd_conta_pagar = L.cd_conta_pagar) 					   " +
				((tpRelatorio == 0) ? "" : "JOIN adm_conta_factoring         K   ON (A.cd_conta_pagar = K.cd_conta_pagar) ") +
				" WHERE 1=1 "+ 
				(!nmPessoa.equals("") ?
								"AND TRANSLATE (C.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', " + 
								"				'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmPessoa)+"%' "
								: "") +
				(!dsHistorico.equals("") ?
						"AND TRANSLATE (A.ds_historico, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', " + 
						"				'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(dsHistorico)+"%' "
						: ""),
				 " ORDER BY "+
						 (( orderByPrioritaria )?" A.LG_PRIORITARIA DESC, ":"")
						 +" A.DT_VENCIMENTO "+ordenacao+" "+limitSkip[1], criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
			while (rsm.next()) {
				connect = Conexao.conectar();
				PreparedStatement pstmt;
				pstmt = connect.prepareStatement(
						"SELECT A.*,"
						+ " S.nm_centro_custo "
						+ " FROM adm_conta_pagar A "
						+ " LEFT OUTER JOIN adm_conta_pagar_categoria   Q   ON (A.cd_conta_pagar = Q.cd_conta_pagar)"
						+ " LEFT OUTER JOIN adm_categoria_economica     R   ON (Q.cd_categoria_economica = R.cd_categoria_economica)"
						+ " LEFT OUTER JOIN ctb_centro_custo            S   ON (Q.cd_centro_custo = S.cd_centro_custo)"
						+ " WHERE A.cd_conta_pagar=?");
				pstmt.setInt(1, rsm.getInt("cd_conta_pagar"));
				rsm.setValueToField("NM_CENTRO_CUSTO", Util.join(new ResultSetMap(pstmt.executeQuery()), "nm_centro_custo"));
				
				Conexao.desconectar(connect);
			}
			rsm.beforeFirst();
			
			return rsm;
		
		}	
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getCustasAguardandoReembolso(ArrayList<sol.dao.ItemComparator> criterios) {
		return getCustasAguardandoReembolso( criterios, null );
	}
	
	public static ResultSetMap getCustasAguardandoReembolso( ArrayList<sol.dao.ItemComparator> criterios, Connection connect ) {
		boolean isConnectionNull = connect==null;
		try{
			if( isConnectionNull ){
				connect = isConnectionNull ? Conexao.conectar() : connect;
				connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			}
			
			int cdCategoriaCustas = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_CUSTAS_REEMBOLSAVEIS", 0, 0, connect);
			return Search.find( " SELECT DISTINCT A.CD_CONTA_PAGAR, * FROM ADM_CONTA_PAGAR A "+
								" JOIN ADM_CONTA_PAGAR_CATEGORIA B ON ( A.CD_CONTA_PAGAR = B.CD_CONTA_PAGAR "+
								"											AND "+
								"										B.CD_CATEGORIA_ECONOMICA = "+cdCategoriaCustas+" ) "+
								" JOIN PRC_PROCESSO_FINANCEIRO       C ON ( A.CD_CONTA_PAGAR = C.CD_CONTA_PAGAR )  "+
								" JOIN PRC_PROCESSO                  D ON ( C.CD_PROCESSO = D.CD_PROCESSO       )  "+
								" LEFT JOIN PRC_PROCESSO_ANDAMENTO   E ON ( C.CD_ANDAMENTO = E.CD_ANDAMENTO     )  "+
								" LEFT JOIN AGD_GRUPO                F ON ( D.CD_GRUPO_TRABALHO = F.CD_GRUPO   )  "+
								" LEFT JOIN GRL_PESSOA               G ON ( A.CD_PESSOA = G.CD_PESSOA   )  "+
								" WHERE ST_CONTA = "+ST_PAGA
					, null, connect, isConnectionNull);
		
		}catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findRelatorioGroupFavorecido( ArrayList<sol.dao.ItemComparator> criterios){
		return findRelatorioGroupFavorecido(criterios, null);
	}
	
	public static ResultSetMap findRelatorioGroupFavorecido( ArrayList<sol.dao.ItemComparator> criterios, Connection connect ){
		boolean isConnectionNull = connect==null;
		try{
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			int qtLimite = 0;
			int tpRelatorio = 0;
			String ordenacao = "ASC";
			Boolean orderByPrioritaria = false;
			for (int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					i--;
					continue;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("tpRelatorio")) {
					tpRelatorio = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					continue;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("st_conta") && 
						Integer.parseInt(criterios.get(i).getValue()) == -1) {
					criterios.remove(i);
					continue;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("A.dt_vencimento_final") ) {
					criterios.get(i).setColumn("A.dt_vencimento");
					criterios.get(i).setValue( criterios.get(i).getValue().substring(0, 10)+" 23:59:59"  );
					continue;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("ordenacao")) {
					ordenacao = criterios.get(i).getValue();
					criterios.remove(i);
					i--;
					if( ordenacao.equals("ASC") || !ordenacao.equals("DESC")  )
						ordenacao = "ASC";
					continue;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("orderbyprioritaria")) {
					orderByPrioritaria = true;
					criterios.remove(i);
					i--;
					continue;
				}
			}
			
			
			String[] limitSkip = Util.getLimitAndSkip(qtLimite, 0);
			ResultSetMap rsm = Search.find("SELECT "+limitSkip[0]+" A.*, H.nm_razao_social AS nm_empresa, I.nm_pessoa AS nm_fantasia, C.nm_pessoa, " +
					           "       C.nm_pessoa AS nm_favorecido, C.cd_pessoa as cd_favorecido, C.st_cadastro, D.nr_conta, D.nr_dv, D.nm_conta, E.nr_agencia, " +
					           "       F.nr_banco, F.nm_banco, G.nm_tipo_documento, G.sg_tipo_documento, J.nm_arquivo,"+
					           " 	   ((A.vl_conta+A.vl_acrescimo-A.vl_abatimento)-A.vl_pago) as vl_apagar, L.*,"+
					           "       N.nm_pessoa as nm_usuario "+
					           "FROM adm_conta_pagar A " +
					           "LEFT OUTER JOIN grl_empresa               B   ON (A.cd_empresa = B.cd_empresa) " +
					           "LEFT OUTER JOIN grl_pessoa                C   ON (A.cd_pessoa  = C.cd_pessoa) " +
					           "LEFT OUTER JOIN grl_pessoa_juridica       H   ON (B.cd_empresa  = H.cd_pessoa) " +
					           "LEFT OUTER JOIN grl_pessoa                I   ON (H.cd_pessoa  = I.cd_pessoa) " +
					           "LEFT OUTER JOIN adm_conta_financeira      D   ON (A.cd_conta = D.cd_conta) " +
					           "LEFT OUTER JOIN grl_agencia               E   ON (D.cd_agencia = E.cd_agencia)" +
					           "LEFT OUTER JOIN grl_banco                 F   ON (E.cd_banco = F.cd_banco) "+
					           "LEFT OUTER JOIN adm_tipo_documento        G   ON (A.cd_tipo_documento = G.cd_tipo_documento) " +
					           "LEFT OUTER JOIN grl_arquivo               J   ON (A.cd_arquivo = J.cd_arquivo) " +
					           "LEFT OUTER JOIN adm_forma_pagamento       L   ON (A.cd_forma_pagamento_preferencial = L.cd_forma_pagamento) " +					           
				((tpRelatorio == 0) ? "" : "JOIN adm_conta_factoring      K   ON (A.cd_conta_pagar = K.cd_conta_pagar) ")+
							   "LEFT OUTER JOIN seg_usuario             M   ON (A.cd_usuario = M.cd_usuario) "+
						       "LEFT OUTER JOIN grl_pessoa              N   ON (M.cd_pessoa = N.cd_pessoa) ",
				 " ORDER BY CD_FAVORECIDO "+limitSkip[1], criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			ResultSetMap rsmFavorecido = new ResultSetMap();
			int cdFavorecidoAtual = 0;
			rsm.beforeFirst();
			while( rsm.next() ){
				if( cdFavorecidoAtual != rsm.getInt("CD_FAVORECIDO") ){
					cdFavorecidoAtual =  rsm.getInt("CD_FAVORECIDO");
					HashMap<String, Object> reg = new HashMap<String, Object>();
					reg.put("NM_FAVORECIDO", rsm.getString("NM_FAVORECIDO") );
					reg.put("CONTAS", new ArrayList<HashMap<String, Object>>() );
					rsmFavorecido.addRegister(reg);
					rsmFavorecido.last();
				}
				((ArrayList<HashMap<String, Object>>) rsmFavorecido.getRegister().get("CONTAS")).add(rsm.getRegister());
			}
			
			return rsmFavorecido;
		}catch(Exception e){
			Util.registerLog(e);
			System.err.println("Erro! ContaPagarServices.insert: " +  e);
			return null;
		}finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findGroupCategorias(ArrayList<sol.dao.ItemComparator> criterios) {
		return findGroupCategorias(criterios, null);
	}

	public static ResultSetMap findGroupCategorias(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull)
				connect = Conexao.conectar();
			int qtLimite = 0;
			int tpRelatorio = 0;
			String ordenacao = "ASC";
			for (int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					break;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("ordenacao")) {
					ordenacao = criterios.get(i).getValue();
					criterios.remove(i);
					if( ordenacao.equals("ASC") || !ordenacao.equals("DESC")  )
						ordenacao = "ASC";
					break;
				}
			}
			String[] limitSkip = Util.getLimitAndSkip(qtLimite, 0);
			
			return Search.find("SELECT "+limitSkip[0]+" A.*, H.vl_conta_categoria, H2.*, I.*, C.nm_pessoa,   "+
					           "       C.nm_pessoa AS nm_favorecido, E.nm_pessoa as nm_fantasia, F.nm_conta,  "+
					           "       G.nm_tipo_documento, G.sg_tipo_documento, "+
					           " 	   ((A.vl_conta+A.vl_acrescimo-A.vl_abatimento)-A.vl_pago) as vl_apagar, "+
					           " 	   K.nm_pessoa as nm_usuario "+
					           "FROM adm_conta_pagar A " +
					           "JOIN grl_empresa                             B   ON (A.cd_empresa = B.cd_empresa) " +
					           "LEFT OUTER JOIN grl_pessoa                   C   ON (A.cd_pessoa  = C.cd_pessoa) " +
					           "LEFT OUTER JOIN grl_pessoa_juridica          D   ON (B.cd_empresa  = D.cd_pessoa) " +
					           "LEFT OUTER JOIN grl_pessoa                   E   ON (D.cd_pessoa  = E.cd_pessoa) " +
					           "LEFT OUTER JOIN adm_conta_financeira         F   ON (A.cd_conta = F.cd_conta) " +
					           "LEFT OUTER JOIN adm_tipo_documento           G   ON (A.cd_tipo_documento = G.cd_tipo_documento) " +
					           "LEFT OUTER JOIN adm_conta_pagar_categoria    H   ON (A.cd_conta_pagar = H.cd_conta_pagar) "+
					           "LEFT OUTER JOIN adm_categoria_economica      H2   ON (H.cd_categoria_economica = H2.cd_categoria_economica) "+
					           "LEFT OUTER JOIN ctb_centro_custo             I   ON (H.cd_centro_custo = I.cd_centro_custo) "+
					           "LEFT OUTER JOIN seg_usuario             J   ON (A.cd_usuario = J.cd_usuario) "+
					           "LEFT OUTER JOIN grl_pessoa              K   ON (J.cd_pessoa = k.cd_pessoa) ",
					           " ORDER BY A.cd_conta_pagar,  A.dt_vencimento "+ordenacao+" "+limitSkip[1],
					           criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result findGroupCategoriasMensal(ArrayList<sol.dao.ItemComparator> criterios) {
		return findGroupCategoriasMensal(criterios, null);
	}

	public static Result findGroupCategoriasMensal(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ArrayList<sol.dao.ItemComparator> criteriosCopy = (ArrayList<sol.dao.ItemComparator>)criterios.clone();
			
			int qtLimite = 0;
			String ordenacao = "ASC";
			String clTipoPeriodo = "A.DT_VENCIMENTO";
			String clTipoPeriodoColumn = "DT_VENCIMENTO";
			int year = new GregorianCalendar().get(Calendar.YEAR); //2017;
			for (int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					break;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("ordenacao")) {
					ordenacao = criterios.get(i).getValue();
					criterios.remove(i);
					if( ordenacao.equals("ASC") || !ordenacao.equals("DESC")  )
						ordenacao = "ASC";
					break;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("clTipoPeriodo")) {
					clTipoPeriodo = criterios.get(i).getValue();
					clTipoPeriodoColumn = clTipoPeriodo.substring(2);
					criterios.remove(i);
					break;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("year")) {
					year = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					break;
				}
			}
			String[] limitSkip = Util.getLimitAndSkip(qtLimite, 0);
			
			GregorianCalendar dtInicio = new GregorianCalendar();
			dtInicio.set(Calendar.DAY_OF_MONTH, 1);
			dtInicio.set(Calendar.MONTH, 0);
			dtInicio.set(Calendar.YEAR, year);
			criterios.add(new ItemComparator(clTipoPeriodo, Util.formatDateTime(dtInicio, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			
			GregorianCalendar dtFinl = new GregorianCalendar();
			dtFinl.set(Calendar.DAY_OF_MONTH, 31);
			dtFinl.set(Calendar.MONTH, 11);
			dtFinl.set(Calendar.YEAR, year);
			criterios.add(new ItemComparator(clTipoPeriodo, Util.formatDateTime(dtFinl, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			
			ResultSetMap rsm = Search.find("SELECT "+limitSkip[0]+" A.*, H.*, H2.*, H3.nm_categoria_economica AS nm_categoria_superior, H3.nr_categoria_economica AS nr_categoria_superior  "+
					           "FROM adm_conta_pagar A " +
					           "LEFT OUTER JOIN adm_conta_pagar_categoria    H   ON (A.cd_conta_pagar = H.cd_conta_pagar) "+
					           "LEFT OUTER JOIN adm_categoria_economica      H2   ON (H.cd_categoria_economica = H2.cd_categoria_economica) "+
					           "LEFT OUTER JOIN adm_categoria_economica      H3   ON (H2.cd_categoria_superior = H3.cd_categoria_economica) ", 
					           "ORDER BY H3.nm_categoria_economica, H2.nm_categoria_economica, "+clTipoPeriodo+" "+limitSkip[1],
					           criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			int mes = 0;
			float vlMes = 0;
			float vlTotal = 0;
			int cdCategoriaSuperior = 0;
			float vlTotalCategoriaSuperior = 0;
			CategoriaEconomica categoriaEconomica = null;
			HashMap<Integer, Float> registroVlCategoriaSuperior = new HashMap<Integer, Float>();
			int contMeses = 0;
			int x = 0;
			while(rsm.next()){
				GregorianCalendar dtPeriodo = rsm.getGregorianCalendar(clTipoPeriodoColumn);
				if(x == 0){
					mes = dtPeriodo.get(Calendar.MONTH);
					categoriaEconomica = CategoriaEconomicaDAO.get(rsm.getInt("cd_categoria_economica"), connect);
				}
				if(dtPeriodo.get(Calendar.MONTH) == mes && rsm.getInt("cd_categoria_economica") == categoriaEconomica.getCdCategoriaEconomica()){
					vlMes += rsm.getFloat("vl_conta_categoria");
				}
				else{
					vlTotal += vlMes;
					if(vlMes > 0){
						contMeses++;
					}
					if(mes < 11 && rsm.getInt("cd_categoria_economica") == categoriaEconomica.getCdCategoriaEconomica()){
						register.put("VL_" + Util.limparAcentos(Util.meses[mes].toUpperCase()).substring(0, 3), vlMes);
						mes = dtPeriodo.get(Calendar.MONTH);
					}
					else{
						if(cdCategoriaSuperior == 0){
							cdCategoriaSuperior = categoriaEconomica.getCdCategoriaSuperior();
							vlTotalCategoriaSuperior += vlTotal;
						}
						else{
							if(cdCategoriaSuperior != categoriaEconomica.getCdCategoriaSuperior()){
								registroVlCategoriaSuperior.put(cdCategoriaSuperior, vlTotalCategoriaSuperior);
								cdCategoriaSuperior = categoriaEconomica.getCdCategoriaSuperior();
								vlTotalCategoriaSuperior = vlTotal;
							}
							else{
								vlTotalCategoriaSuperior += vlTotal;
							}
						}
						
						register.put("VL_" + Util.limparAcentos(Util.meses[mes].toUpperCase()).substring(0, 3), vlMes);
						register.put("VL_TOTAL", vlTotal);
						
						
						register.put("VL_MEDIA_ATUAL", (contMeses > 0 ? (vlTotal / contMeses) : 0));
						register.put("VL_MEDIA_ANTERIOR", getGroupCategoriasMensalMediaAnterior((ArrayList<ItemComparator>)criteriosCopy.clone(), categoriaEconomica.getCdCategoriaEconomica(), connect));
						register.put("CD_CATEGORIA_ECONOMICA", categoriaEconomica.getCdCategoriaEconomica());
						register.put("NM_CATEGORIA_ECONOMICA", categoriaEconomica.getNmCategoriaEconomica());
						register.put("NR_CATEGORIA_ECONOMICA", categoriaEconomica.getNrCategoriaEconomica());
						
						CategoriaEconomica categoriaSuperior = CategoriaEconomicaDAO.get(categoriaEconomica.getCdCategoriaSuperior(), connect);
						if(categoriaSuperior != null){
							register.put("CD_CATEGORIA_SUPERIOR", categoriaSuperior.getCdCategoriaEconomica());
							register.put("NM_CATEGORIA_SUPERIOR", categoriaSuperior.getNmCategoriaEconomica());
							register.put("NR_CATEGORIA_SUPERIOR", categoriaSuperior.getNrCategoriaEconomica());
						}
						else{
							register.put("CD_CATEGORIA_SUPERIOR", categoriaEconomica.getCdCategoriaEconomica());
							register.put("NM_CATEGORIA_SUPERIOR", categoriaEconomica.getNmCategoriaEconomica());
							register.put("NR_CATEGORIA_SUPERIOR", categoriaEconomica.getNrCategoriaEconomica());
						}
						rsmFinal.addRegister(register);
						register = new HashMap<String, Object>();
						mes = dtPeriodo.get(Calendar.MONTH);
						categoriaEconomica = CategoriaEconomicaDAO.get(rsm.getInt("cd_categoria_economica"), connect);
						contMeses = 0;
						vlTotal = 0;
					}
					
					vlMes = rsm.getFloat("vl_conta_categoria");
				}
				x++;
			}
			rsmFinal.beforeFirst();
			
			if(mes < 11 && vlMes > 0){
				vlTotal += vlMes;
				contMeses++;
				register.put("VL_" + Util.limparAcentos(Util.meses[mes].toUpperCase()).substring(0, 3), vlMes);
				register.put("VL_TOTAL", vlTotal);
				
				
				register.put("VL_MEDIA_ATUAL", (contMeses > 0 ? (vlTotal / contMeses) : 0));
				register.put("VL_MEDIA_ANTERIOR", getGroupCategoriasMensalMediaAnterior((ArrayList<ItemComparator>)criteriosCopy.clone(), categoriaEconomica.getCdCategoriaEconomica(), connect));
				register.put("CD_CATEGORIA_ECONOMICA", categoriaEconomica.getCdCategoriaEconomica());
				register.put("NM_CATEGORIA_ECONOMICA", categoriaEconomica.getNmCategoriaEconomica());
				register.put("NR_CATEGORIA_ECONOMICA", categoriaEconomica.getNrCategoriaEconomica());
				
				CategoriaEconomica categoriaSuperior = CategoriaEconomicaDAO.get(categoriaEconomica.getCdCategoriaSuperior(), connect);
				if(categoriaSuperior != null){
					register.put("CD_CATEGORIA_SUPERIOR", categoriaSuperior.getCdCategoriaEconomica());
					register.put("NM_CATEGORIA_SUPERIOR", categoriaSuperior.getNmCategoriaEconomica());
					register.put("NR_CATEGORIA_SUPERIOR", categoriaSuperior.getNrCategoriaEconomica());
				}
				else{
					register.put("CD_CATEGORIA_SUPERIOR", categoriaEconomica.getCdCategoriaEconomica());
					register.put("NM_CATEGORIA_SUPERIOR", categoriaEconomica.getNmCategoriaEconomica());
					register.put("NR_CATEGORIA_SUPERIOR", categoriaEconomica.getNrCategoriaEconomica());
				}
				rsmFinal.addRegister(register);
			}
			
			registroVlCategoriaSuperior.put(cdCategoriaSuperior, vlTotalCategoriaSuperior);
			cdCategoriaSuperior = (categoriaEconomica!=null ? categoriaEconomica.getCdCategoriaSuperior() : 0);
			float vlTotalJaneiro = 0;
			float vlTotalFevereiro = 0;
			float vlTotalMarco = 0;
			float vlTotalAbril = 0;
			float vlTotalMaio = 0;
			float vlTotalJunho = 0;
			float vlTotalJulho = 0;
			float vlTotalAgosto = 0;
			float vlTotalSetembro = 0;
			float vlTotalOutubro = 0;
			float vlTotalNovembro = 0;
			float vlTotalDezembro = 0;
			while(rsmFinal.next()){
				vlTotalCategoriaSuperior = (registroVlCategoriaSuperior != null && registroVlCategoriaSuperior.get(rsmFinal.getInt("CD_CATEGORIA_SUPERIOR")) != null ? registroVlCategoriaSuperior.get(rsmFinal.getInt("CD_CATEGORIA_SUPERIOR")) : 1);
				float vlTotalCategoria = rsmFinal.getFloat("VL_TOTAL");
				rsmFinal.setValueToField("VL_PORCENTAGEM", (vlTotalCategoria/vlTotalCategoriaSuperior*100));
				
				
				if(rsmFinal.getString("VL_JAN") == null){
					rsmFinal.setValueToField("VL_JAN", 0);
				}
				else{
					vlTotalJaneiro += rsmFinal.getFloat("VL_JAN");
				}
				
				if(rsmFinal.getString("VL_FEV") == null){
					rsmFinal.setValueToField("VL_FEV", 0);
				}
				else{
					vlTotalFevereiro += rsmFinal.getFloat("VL_FEV");
				}
				
				if(rsmFinal.getString("VL_MAR") == null){
					rsmFinal.setValueToField("VL_MAR", 0);
				}
				else{
					vlTotalMarco += rsmFinal.getFloat("VL_MAR");
				}
				
				if(rsmFinal.getString("VL_ABR") == null){
					rsmFinal.setValueToField("VL_ABR", 0);
				}
				else{
					vlTotalAbril += rsmFinal.getFloat("VL_ABR");
				}
				
				if(rsmFinal.getString("VL_MAI") == null){
					rsmFinal.setValueToField("VL_MAI", 0);
				}
				else{
					vlTotalMaio += rsmFinal.getFloat("VL_MAI");
				}
				
				if(rsmFinal.getString("VL_JUN") == null){
					rsmFinal.setValueToField("VL_JUN", 0);
				}
				else{
					vlTotalJunho += rsmFinal.getFloat("VL_JUN");
				}
				
				if(rsmFinal.getString("VL_JUL") == null){
					rsmFinal.setValueToField("VL_JUL", 0);
				}
				else{
					vlTotalJulho += rsmFinal.getFloat("VL_JUL");
				}
				
				if(rsmFinal.getString("VL_AGO") == null){
					rsmFinal.setValueToField("VL_AGO", 0);
				}
				else{
					vlTotalAgosto += rsmFinal.getFloat("VL_AGO");
				}
				
				if(rsmFinal.getString("VL_SET") == null){
					rsmFinal.setValueToField("VL_SET", 0);
				}
				else{
					vlTotalSetembro += rsmFinal.getFloat("VL_SET");
				}
				
				if(rsmFinal.getString("VL_OUT") == null){
					rsmFinal.setValueToField("VL_OUT", 0);
				}
				else{
					vlTotalOutubro += rsmFinal.getFloat("VL_OUT");
				}
				
				if(rsmFinal.getString("VL_NOV") == null){
					rsmFinal.setValueToField("VL_NOV", 0);
				}
				else{
					vlTotalNovembro += rsmFinal.getFloat("VL_NOV");
				}
				
				if(rsmFinal.getString("VL_DEZ") == null){
					rsmFinal.setValueToField("VL_DEZ", 0);
				}
				else{
					vlTotalDezembro += rsmFinal.getFloat("VL_DEZ");
				}
				
				
			}
			rsmFinal.beforeFirst();
			
			Result result = new Result(1);
			result.addObject("rsm", rsmFinal);
			result.addObject("VL_TOTAL_JANEIRO", vlTotalJaneiro);
			result.addObject("VL_TOTAL_FEVEREIRO", vlTotalFevereiro);
			result.addObject("VL_TOTAL_MARCO", vlTotalMarco);
			result.addObject("VL_TOTAL_ABRIL", vlTotalAbril);
			result.addObject("VL_TOTAL_MAIO", vlTotalMaio);
			result.addObject("VL_TOTAL_JUNHO", vlTotalJunho);
			result.addObject("VL_TOTAL_JULHO", vlTotalJulho);
			result.addObject("VL_TOTAL_AGOSTO", vlTotalAgosto);
			result.addObject("VL_TOTAL_SETEMBRO", vlTotalSetembro);
			result.addObject("VL_TOTAL_OUTUBRO", vlTotalOutubro);
			result.addObject("VL_TOTAL_NOVEMBRO", vlTotalNovembro);
			result.addObject("VL_TOTAL_DEZEMBRO", vlTotalDezembro);
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static float getGroupCategoriasMensalMediaAnterior(ArrayList<ItemComparator> criterios, int cdCategoriaEconomica) {
		return getGroupCategoriasMensalMediaAnterior(criterios, cdCategoriaEconomica, null);
	}

	public static float getGroupCategoriasMensalMediaAnterior(ArrayList<ItemComparator> criterios, int cdCategoriaEconomica, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			int qtLimite = 0;
			String ordenacao = "ASC";
			String clTipoPeriodo = "A.DT_VENCIMENTO";
			String clTipoPeriodoColumn = "DT_VENCIMENTO";
			int year = 2017;
			for (int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					break;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("ordenacao")) {
					ordenacao = criterios.get(i).getValue();
					criterios.remove(i);
					if( ordenacao.equals("ASC") || !ordenacao.equals("DESC")  )
						ordenacao = "ASC";
					break;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("clTipoPeriodo")) {
					clTipoPeriodo = criterios.get(i).getValue();
					clTipoPeriodoColumn = clTipoPeriodo.substring(2);
					criterios.remove(i);
					break;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("year")) {
					year = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					break;
				}
			}
			String[] limitSkip = Util.getLimitAndSkip(qtLimite, 0);
			
			GregorianCalendar dtInicio = new GregorianCalendar();
			dtInicio.set(Calendar.DAY_OF_MONTH, 1);
			dtInicio.set(Calendar.MONTH, 0);
			dtInicio.set(Calendar.YEAR, (year-1));
			criterios.add(new ItemComparator(clTipoPeriodo, Util.formatDateTime(dtInicio, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			
			GregorianCalendar dtFinl = new GregorianCalendar();
			dtFinl.set(Calendar.DAY_OF_MONTH, 31);
			dtFinl.set(Calendar.MONTH, 11);
			dtFinl.set(Calendar.YEAR, (year-1));
			criterios.add(new ItemComparator(clTipoPeriodo, Util.formatDateTime(dtFinl, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
						
			ResultSetMap rsm = Search.find("SELECT "+limitSkip[0]+" A.*, H.*, H2.*, H3.nm_categoria_economica AS nm_categoria_superior, H3.nr_categoria_economica AS nr_categoria_superior  "+
					           "FROM adm_conta_pagar A " +
					           "LEFT OUTER JOIN adm_conta_pagar_categoria    H   ON (A.cd_conta_pagar = H.cd_conta_pagar) "+
					           "LEFT OUTER JOIN adm_categoria_economica      H2   ON (H.cd_categoria_economica = H2.cd_categoria_economica) "+
					           "LEFT OUTER JOIN adm_categoria_economica      H3   ON (H2.cd_categoria_superior = H3.cd_categoria_economica) "+
					           "LEFT OUTER JOIN ctb_centro_custo             H4   ON (H.cd_centro_custo = H4.cd_centro_custo) "+
					           " WHERE H.cd_categoria_economica = " + cdCategoriaEconomica, 
					           "ORDER BY H3.nm_categoria_economica, H2.nm_categoria_economica, "+clTipoPeriodo+" "+limitSkip[1],
					           criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			
			int mes = 0;
			float vlMes = 0;
			float vlTotal = 0;
			float vlMediaTotal = 0;
			CategoriaEconomica categoriaEconomica = null;
			int contMeses = 0;
			int x = 0;
			while(rsm.next()){
				GregorianCalendar dtPeriodo = rsm.getGregorianCalendar(clTipoPeriodoColumn);
				if(x == 0){
					mes = dtPeriodo.get(Calendar.MONTH);
					categoriaEconomica = CategoriaEconomicaDAO.get(rsm.getInt("cd_categoria_economica"), connect);
				}
				if(dtPeriodo.get(Calendar.MONTH) == mes){
					vlMes += rsm.getFloat("vl_conta_categoria");
				}
				else{
					vlTotal += vlMes;
					if(vlMes > 0){
						contMeses++;
					}
					
					if(mes < 11){
						mes = dtPeriodo.get(Calendar.MONTH);
					}
										
					vlMes = rsm.getFloat("vl_conta_categoria");
				}
				x++;
			}
			vlTotal += vlMes;
			if(vlMes > 0){
				contMeses++;
			}
			
			if(contMeses > 0)
				vlMediaTotal = vlTotal / contMeses;
			
			return vlMediaTotal;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return 0;
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findContaPagarOfContrato(ArrayList<sol.dao.ItemComparator> criterios) {
		return findContaPagarOfContrato(criterios, null);
	}
	public static ResultSetMap findContaPagarOfContrato(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		ResultSetMap rsm = find(criterios, connect);
		return rsm;
	}
	
	public static Result gerarRelatorioContasPagar(ArrayList<sol.dao.ItemComparator> criterios){
		return gerarRelatorioContasPagar(criterios, null);
	}
	public static Result gerarRelatorioContasPagar(ArrayList<sol.dao.ItemComparator> criterios, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ResultSetMap rsmContasPagar = find( criterios, connect );
			
			Double vlContaTotal = 0.0, descontoTotal=0.0, acrescimoTotal=0.0, pagoTotal=0.0, apagar=0.0;
			while( rsmContasPagar.next() ){
				vlContaTotal += rsmContasPagar.getDouble("VL_CONTA");
				acrescimoTotal += rsmContasPagar.getDouble("VL_ACRESCIMO");
				descontoTotal += rsmContasPagar.getDouble("VL_ABATIMENTO");
				pagoTotal += rsmContasPagar.getDouble("VL_PAGO");
				if( rsmContasPagar.getInt("ST_CONTA") == ST_EM_ABERTO ){
					apagar += rsmContasPagar.getDouble("VL_CONTA") - rsmContasPagar.getDouble("VL_PAGO") + rsmContasPagar.getDouble("VL_ACRESCIMO") - rsmContasPagar.getDouble("VL_ABATIMENTO");
				}
			}
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("VL_CONTA", vlContaTotal);
			params.put("VL_ACRESCIMO", acrescimoTotal);
			params.put("VL_ABATIMENTO", descontoTotal);
			params.put("VL_PAGO", pagoTotal);
			params.put("VL_APAGAR", apagar);
			
			register.put("NM_PESSOA", "TOTAL");
			register.put("VL_CONTA", vlContaTotal);
			register.put("VL_ACRESCIMO", acrescimoTotal);
			register.put("VL_ABATIMENTO", descontoTotal);
			register.put("VL_PAGO", pagoTotal);
			register.put("VL_APAGAR", apagar);
			register.put("LG_AUTORIZADO", null);
			rsmContasPagar.addRegister(register);
			
			Result result = new Result(1);
			result.addObject("rsm", rsmContasPagar);
			result.addObject("params", params);
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar gerar relatório de contas a pagar!", e);
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result gerarParcelasOutraConta(ContaPagar conta, boolean repetirEmissao, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			if(conta.getCdContrato()>0 || conta.getCdDocumentoEntrada()>0 || conta.getCdManutencao()>0 || conta.getCdViagem()>0)
				return new Result(1);
			//
			int[] parcelas = new int[] {0, conta.getQtParcelas(), 365, 52, 24, 24, 12, 12, 6, 4, 3, 2, 1, 1};
			int qtParcelas = parcelas[conta.getTpFrequencia()];
			//int qtParcelas = (conta.getTpFrequencia()!= UNICA_VEZ)?conta.getQtParcelas()-1:0;
			GregorianCalendar dtVencimento = conta.getDtVencimento();
			// Contando a quantidade de contas que já existem geradas
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT MAX(nr_parcela) AS nr_parcela, MAX(dt_vencimento) AS dt_vencimento " +
						                                                 "FROM adm_conta_pagar " +
											                             "WHERE cd_conta_origem IN ("+conta.getCdContaOrigem()+","+conta.getCdContaPagar()+")"+
											                             (conta.getTpFrequencia()==QUANTIDADE_FIXA ? "" : "  AND st_conta = "+ST_EM_ABERTO)).executeQuery());
			if(rsm.next())	{
				qtParcelas -= rsm.getInt("nr_parcela");
				if(rsm.getGregorianCalendar("dt_vencimento")!=null)
					dtVencimento = rsm.getGregorianCalendar("dt_vencimento");
			}

			return gerarParcelas(conta.getCdContaPagar(), OF_OUTRA_CONTA,  conta.getCdEmpresa(), conta.getCdPessoa(), conta.getCdTipoDocumento(),
								 conta.getCdConta(), qtParcelas, conta.getVlConta(), 0 /*prDesconto*/,
								 0.0/*vlAdesao*/,	getProximoVencimento(dtVencimento, conta.getTpFrequencia(), 0),
								 conta.getDtVencimento().get(Calendar.DAY_OF_MONTH), conta.getTpFrequencia(),
								 conta.getNrDocumento(), conta.getDsHistorico(), 0, repetirEmissao, connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar gerar contas a partir de outra conta!", e);
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result gerarParcelas(int cdForeignKey, int tpForeignKey, int cdEmpresa, int cdPessoa,
			int cdTipoDocumento, int cdConta, int qtParcelas, Double vlParcela, float prDesconto,
			Double vlAdesao, GregorianCalendar dtVencimentoPrimeira, int nrDiaVencimento, int tpFrequencia,
			String prefixDocumento, String dsHistorico, int cdCategoriaParcelas) {
		return gerarParcelas(cdForeignKey, tpForeignKey, cdEmpresa, cdPessoa, cdTipoDocumento, cdConta,
				qtParcelas, vlParcela, prDesconto, vlAdesao, dtVencimentoPrimeira, nrDiaVencimento, tpFrequencia,
				prefixDocumento, dsHistorico, cdCategoriaParcelas, false, null);
	}

	public static Result gerarParcelas(int cdForeignKey, int tpForeignKey, int cdEmpresa, int cdPessoa,
			int cdTipoDocumento, int cdConta, int qtParcelas, Double vlParcela, float prDesconto,
			Double vlAdesao, GregorianCalendar dtVencimentoPrimeira, int nrDiaVencimento, int tpFrequencia,
			String prefixDocumento, String dsHistorico) {
		return gerarParcelas(cdForeignKey, tpForeignKey, cdEmpresa, cdPessoa, cdTipoDocumento, cdConta,
				qtParcelas, vlParcela, prDesconto, vlAdesao, dtVencimentoPrimeira, nrDiaVencimento, tpFrequencia,
				prefixDocumento, dsHistorico, 0, false, null);
	}

	public static Result gerarParcelas(int cdForeignKey, int tpForeignKey, int cdEmpresa, int cdPessoa,
			int cdTipoDocumento, int cdConta, int qtParcelas, Double vlParcela, float prDesconto,
			Double vlAdesao, GregorianCalendar dtVencimentoPrimeira, int nrDiaVencimento, int tpFrequencia,
			String prefixDocumento, String dsHistorico, int cdCategoriaParcelas, boolean repetirEmissao, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			boolean lgIgnorarDiasNaoUteis = ParametroServices.getValorOfParametroAsInteger("LG_IGNORAR_DIA_NAO_UTIL", 0, cdEmpresa, connect)==1;
			ArrayList<ContaPagar> contas = new ArrayList<ContaPagar>();
			/*
			 *  Buscando informações no Contrato
			 */
			int nrParcelaInicial = 0;
			if(nrDiaVencimento<=0 && dtVencimentoPrimeira!=null)
				nrDiaVencimento = dtVencimentoPrimeira.get(Calendar.DATE);
			GregorianCalendar dtVencimento = dtVencimentoPrimeira;
			GregorianCalendar dtEmissao    = new GregorianCalendar();
			dtEmissao = new GregorianCalendar(dtEmissao.get(Calendar.YEAR), dtEmissao.get(Calendar.MONTH), dtEmissao.get(Calendar.DAY_OF_MONTH));

			ResultSetMap rsmContaPagarCategoria = new ResultSetMap();
			String txtObservacao = "", nrDocumento = null;
			float vlBaseAutorizacao = 0, vlAbatimento = 0, vlAcrescimo = 0;
			int lgPagamentoSemAutorizacao = ParametroServices.getValorOfParametroAsInteger("LG_PAGAMENTO_SEM_AUTORIZACAO", 0, cdEmpresa, connect);
			int lgAutorizado    = lgPagamentoSemAutorizacao,
			    cdContaBancaria = 0;
			GregorianCalendar dtAutorizacao = null;
			ArrayList<ContaPagarCategoria> categorias = null;
			if(tpForeignKey==OF_OUTRA_CONTA)	{
				ContaPagar contaPagar = ContaPagarDAO.get(cdForeignKey, connect);
				dtEmissao 		  = contaPagar.getDtEmissao();
				txtObservacao 	  = contaPagar.getTxtObservacao();
				vlBaseAutorizacao = contaPagar.getVlBaseAutorizacao().floatValue();
				vlAbatimento      = contaPagar.getVlAbatimento().floatValue();
				vlAcrescimo		  = contaPagar.getVlAcrescimo().floatValue();
				lgAutorizado 	  = vlParcela <= vlBaseAutorizacao ? 1 : lgPagamentoSemAutorizacao;
				cdContaBancaria   = contaPagar.getCdContaBancaria();
				nrParcelaInicial  = contaPagar.getNrParcela()+1;
				if(lgAutorizado == 1)
					dtAutorizacao = new GregorianCalendar();
				rsmContaPagarCategoria = ContaPagarCategoriaServices.getCategoriaOfContaPagar(cdForeignKey, connect);
				
				categorias = new ArrayList<ContaPagarCategoria>();
				while(rsmContaPagarCategoria.next())
					categorias.add(new ContaPagarCategoria(0, rsmContaPagarCategoria.getInt("cd_categoria_economica"), rsmContaPagarCategoria.getDouble("vl_conta_categoria"), rsmContaPagarCategoria.getInt("cd_centro_custo"), 0));
				if(!repetirEmissao)
					dtEmissao.add(Calendar.MONTH, 1);
			}

			if(tpForeignKey==OF_DOCUMENTO_ENTRADA && cdForeignKey>0)	{
				DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(cdForeignKey, connect);
				categorias 		  = DocumentoEntradaServices.getClassificacaoEmCategorias(docEntrada, connect);
				if (categorias!=null && categorias.size() == 1) 
					categorias.set(0, new ContaPagarCategoria(categorias.get(0).getCdContaPagar(), categorias.get(0).getCdCategoriaEconomica(), vlParcela, categorias.get(0).getCdCentroCusto(), categorias.get(0).getCdCategoriaEconomica()));
				
				nrParcelaInicial  = 1;
			}
			float vlTotalCategorias = 0;
			for (int i=0; categorias!=null && i<categorias.size(); i++)
				vlTotalCategorias += categorias.get(i).getVlContaCategoria();

			if(cdCategoriaParcelas <= 0) 
				cdCategoriaParcelas = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_DESPESAS_DEFAULT", 0, cdEmpresa, connect);
			
			
			/* Gerar parcelas */
			for(int i=nrParcelaInicial; i<=qtParcelas; i++)	{
				Double vlConta = vlParcela;
				if(i==0 && vlAdesao>0)
					vlConta = vlAdesao;
				else if(i==0)
					continue;
				/*
				 *  Registra divisão em categorias
				 */
				ArrayList<ContaPagarCategoria> contaCategorias = new ArrayList<ContaPagarCategoria>();
				for(int x=0; categorias!=null && x<categorias.size(); x++)	{
					Double vlCategoria = vlParcela<=0 || vlTotalCategorias<=0 ? 0 : categorias.get(x).getVlContaCategoria() / vlTotalCategorias * vlConta;
					contaCategorias.add(new ContaPagarCategoria(0/*cdContaPagar*/,
						categorias.get(x).getCdCategoriaEconomica(),
						vlCategoria, categorias.get(x).getCdCentroCusto()));
				}

				if ((categorias == null || categorias.size() <= 0) && cdCategoriaParcelas > 0 && i > 0)
					contaCategorias.add(new ContaPagarCategoria(0/*cdContaPagar*/, cdCategoriaParcelas, vlParcela, 0));
				/*
				 *  INCREMENTO DA DATA DE VENCIMENTO
				 *  	- Clona para que mudanças de datas por causa de dias não úteis não sejam replicados
				 */
				GregorianCalendar venc = (GregorianCalendar)dtVencimento.clone();
				if(!lgIgnorarDiasNaoUteis)
					while(venc.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY || venc.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || FeriadoServices.isFeriado(venc, connect))
						venc.add(Calendar.DATE, 1);
				//
				nrDocumento = prefixDocumento!=null ? prefixDocumento+"-"+(new DecimalFormat("000").format(i)) : nrDocumento;
				//String idContaPagar = nrDocumento;
				String nrReferencia = "";
				if(tpFrequencia == QUANTIDADE_FIXA)
					nrReferencia = i+"/"+qtParcelas;
				else if(tpFrequencia == MENSAL)	{
					int nrMes = dtVencimento.get(Calendar.MONTH);
					int nrAno = dtVencimento.get(Calendar.YEAR);
					if(dtVencimento.get(Calendar.DAY_OF_MONTH)<28)	{
						nrMes = nrMes==0 ? 11 : nrMes-1;
						nrAno = nrMes==0 ? nrAno-1 : nrAno;
					}
					nrReferencia = Recursos.siglaMeses[nrMes]+"/"+nrAno;
				}
				int nrParcela = tpFrequencia != QUANTIDADE_FIXA && tpForeignKey==OF_OUTRA_CONTA ? 0 : i;
				int qtParc 	  = tpFrequencia != QUANTIDADE_FIXA && tpForeignKey==OF_OUTRA_CONTA ? 0 : qtParcelas;

				ContaPagar conta = new ContaPagar(0,(tpForeignKey==OF_CONTRATO ? cdForeignKey : 0) /*cdContrato*/, cdPessoa,
												  cdEmpresa, (tpForeignKey==OF_OUTRA_CONTA ? cdForeignKey : 0)  /*cdContaOrigem*/,
												  (tpForeignKey==OF_DOCUMENTO_ENTRADA ? cdForeignKey : 0) /*cdDocumentoEntrada*/,
												  cdConta, cdContaBancaria, venc, dtEmissao, null /*dtPagamento*/, dtAutorizacao,
												  nrDocumento, nrReferencia, nrParcela, cdTipoDocumento, vlConta, vlAbatimento,
												  vlAcrescimo, 0 /*vlPago*/, dsHistorico, ST_EM_ABERTO, lgAutorizado, tpFrequencia,
												  qtParc, vlBaseAutorizacao, 0 /*cdViagem*/, 0 /*cdManutencao*/, txtObservacao,
												  new GregorianCalendar(), venc, 0/*cdTurno*/);
				
				Result result = insert(conta, true, false, (categorias == null || categorias.size() <= 1 ? (contaCategorias == null || contaCategorias.size() <= 0 ? null : contaCategorias) : categorias), connect);
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					Util.registerLog(new Exception("Nao foi possivel salvar a parcela "+i+"/"+qtParcelas+"\nMensagem: "+result.getMessage()));
					return new Result(-1, "Nao foi possivel salvar a parcela "+i+"/"+qtParcelas+"\nMensagem: "+result.getMessage());
				}
				conta.setCdContaPagar(result.getCode());
				contas.add(conta);

				dtVencimento = getProximoVencimento(dtVencimento, tpFrequencia, nrDiaVencimento);

				if(!repetirEmissao)
					dtEmissao = getProximoVencimento(dtEmissao, tpFrequencia, dtEmissao.get(Calendar.DAY_OF_MONTH));

			}
			if (isConnectionNull)
				connect.commit();
			Result result = new Result(1);
			result.addObject("contas", contas);
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar gerar outras parcelas!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GregorianCalendar getProximoVencimento(GregorianCalendar dtVencimento, int tpFrequencia, int nrDia)	{
		dtVencimento = (GregorianCalendar)dtVencimento.clone();
		switch(tpFrequencia)	{
	    	case QUANTIDADE_FIXA:
	    		dtVencimento.add(Calendar.MONTH, 1);
	    		break;
	    	case DIARIO:
	    		dtVencimento.add(Calendar.DATE, 1);
	    		break;
	    	case SEMANAL:
	    		dtVencimento.add(Calendar.DATE, 7);
	    		break;
	    	case SEMANAS_ALTERNADAS:
	    		dtVencimento.add(Calendar.DATE, 14);
	    		break;
	    	case DUAS_VEZES_MES:
	    		dtVencimento.add(Calendar.DATE, 14);
	    		break;
	    	case A_CADA_QUATRO_SEMANAS:
	    		dtVencimento.add(Calendar.MONTH, 1);
	    		break;
	    	case MENSAL:
	    		dtVencimento.add(Calendar.MONTH, 1);
	    		break;
	    	case MESES_ALTERNADOS:
	    		dtVencimento.add(Calendar.MONTH, 2);
	    		break;
	    	case TRIMESTRAL:
	    		dtVencimento.add(Calendar.MONTH, 3);
	    		break;
	    	case A_CADA_QUATRO_MESES:
	    		dtVencimento.add(Calendar.MONTH, 4);
	    		break;
	    	case DUAS_VEZES_ANO:
	    		dtVencimento.add(Calendar.MONTH, 6);
	    		break;
	    	case ANUAL:
	    		dtVencimento.add(Calendar.YEAR, 1);
	    		break;
	    	case ANOS_ALTERNADOS:
	    		dtVencimento.add(Calendar.YEAR, 2);
	    		break;
		}
		if(nrDia>0 && dtVencimento.get(Calendar.DATE)!=nrDia && nrDia<=dtVencimento.getActualMaximum(Calendar.DATE))
			dtVencimento.set(Calendar.DATE, nrDia);
		return dtVencimento;
	}
	
	public static Result get(int cdContaPagar){
		Connection connect = Conexao.conectar();
		try	{
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add( new ItemComparator("A.CD_CONTA_PAGAR", String.valueOf( cdContaPagar ), ItemComparator.EQUAL, Types.INTEGER) );
			ResultSetMap rsm = find( criterios );
			if( rsm.next() )
				return new Result(1, "Conta Recuperada com Sucesso", "CONTAPAGAR", rsm.getRegister());
			else
				return new Result( -1, "Conta não encontrada", "CONTAPAGAR", null);
				
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getAsResultSet(int cdContaPagar) {
		return getAsResultSet(cdContaPagar, null);
	}

	public static ResultSetMap getAsResultSet(int cdContaPagar, Connection connect) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_conta_pagar", Integer.toString(cdContaPagar), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, connect);
	}

	public static ResultSetMap getNextConta(int cdContaPagar, GregorianCalendar dtVencimentoAtual) {
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_conta_pagar A " +
														 	   "WHERE A.cd_conta_origem = "+cdContaPagar+
														 	   "  AND A.dt_vencimento   = (SELECT MIN(dt_vencimento) FROM adm_conta_pagar X " +
														 	   "                           WHERE X.dt_vencimento   > ? " +
														 	   "                             AND X.cd_conta_origem = "+cdContaPagar+")");

			pstmt.setTimestamp(1, new Timestamp(dtVencimentoAtual.getTimeInMillis()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return getAsResultSet(rs.getInt("cd_conta_pagar"), connect);
			else
				return new ResultSetMap();
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
		
	public static Result delete(int cdContaPagar){
		return delete(cdContaPagar, true, null);
	}

	public static Result delete(int cdContaPagar, boolean deleteChild){
		return delete(cdContaPagar, deleteChild, null);
	}

	public static Result delete(int cdContaPagar, boolean deleteChild, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			ResultSet rs = connect.prepareStatement("SELECT * FROM adm_movimento_conta_pagar WHERE cd_conta_pagar = "+cdContaPagar).executeQuery();
			if(rs.next())
				return new Result(-1, "Já existe um pagamento informado para essa conta, é mais seguro que você exclua antes esse pagamento!");
			//
			Result result = new Result(1);
			// Tenta excluir
			if(deleteChild)	{
				rs = connect.prepareStatement("SELECT * FROM adm_conta_pagar WHERE cd_conta_origem = "+cdContaPagar).executeQuery();
				while(rs.next())	{

					if ((result=delete(rs.getInt("cd_conta_pagar"), deleteChild, connect)).getCode() < 1)	{
						if (isConnectionNull)
							Conexao.rollback(connect);
						return result;
					}
				}
				PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_factoring WHERE cd_conta_pagar = "+cdContaPagar);
				int ret = pstmt.executeUpdate();
				if(ret < 0){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar contas factoring!");
				}
			}
			// tenta apagar os eventos relacionados à Conta a Pagar
			if (ContaPagarEventoServices.deleteAll(cdContaPagar, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Não foi possível excluir todos os eventos financeiros ligados a essa conta a pagar!");
			}

			// tenta apagar as classificações financeiras relacionadas à Conta a Pagar
			if (ContaPagarCategoriaServices.deleteAll(cdContaPagar, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Não foi possível excluir todas as categorias ligadas a essa conta a pagar!");
			}

			// Excluindo
			try {
				connect.prepareStatement("UPDATE prc_processo_financeiro SET cd_conta_pagar = NULL WHERE cd_conta_pagar = "+cdContaPagar).executeUpdate();
			}
			catch(Exception e) {
				e.printStackTrace(System.out);
			}
			
			// tenta apagar Conta a Pagar
			if ((result = new Result(ContaPagarDAO.delete(cdContaPagar, connect))).getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				result.setMessage("Erro ao tentar excluir conta a pagar! Verifica se não existe registro de pagamento para essa conta!");
				return result;
			}

			if (isConnectionNull)
				connect.commit();

			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar excluir conta a pagar!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result setPagamentoResumido(int cdContaPagar, int cdConta,
			Double vlMulta, int cdCategoriaMulta, Double vlJuros, int cdCategoriaJuros, Double vlDesconto, int cdCategoriaDesconto,
			Double vlPago, int cdFormaPagamento, int cdCheque, String dsHistorico, int cdUsuario, GregorianCalendar dtPagamento)
	{
		return setPagamentoResumido(cdContaPagar, cdConta, vlMulta, cdCategoriaMulta, vlJuros, cdCategoriaJuros,
				                    vlDesconto, cdCategoriaDesconto, vlPago, cdFormaPagamento, cdCheque, dsHistorico, cdUsuario, dtPagamento, -1, 0, 0/*cdTurno*/, null, null);
	}
	
	public static Result setPagamentoResumido(int cdContaPagar, int cdConta,
			Double vlMulta, int cdCategoriaMulta, Double vlJuros, int cdCategoriaJuros, Double vlDesconto, int cdCategoriaDesconto,
			Double vlPago, int cdFormaPagamento, int cdCheque, String dsHistorico, int cdUsuario, GregorianCalendar dtPagamento, int stCheque, int cdTurno)
	{
		return setPagamentoResumido(cdContaPagar, cdConta, vlMulta, cdCategoriaMulta, vlJuros, cdCategoriaJuros,
				                    vlDesconto, cdCategoriaDesconto, vlPago, cdFormaPagamento, cdCheque, dsHistorico, cdUsuario, dtPagamento, stCheque, 0, cdTurno, null, null);
	}
	
	public static Result setPagamentoResumido(int cdContaPagar, int cdConta,
			Double vlMulta, int cdCategoriaMulta, Double vlJuros, int cdCategoriaJuros, Double vlDesconto, int cdCategoriaDesconto,
			Double vlPago, int cdFormaPagamento, int cdCheque, String dsHistorico, int cdUsuario, GregorianCalendar dtPagamento, int stCheque, int cdTurno, AuthData authData)
	{
		return setPagamentoResumido(cdContaPagar, cdConta, vlMulta, cdCategoriaMulta, vlJuros, cdCategoriaJuros,
				                    vlDesconto, cdCategoriaDesconto, vlPago, cdFormaPagamento, cdCheque, dsHistorico, cdUsuario, dtPagamento, stCheque, 0, cdTurno, authData, null);
	}

	public static Result setPagamentoResumido(int cdContaPagar, int cdConta,
			Double vlMulta, int cdCategoriaMulta, Double vlJuros, int cdCategoriaJuros, Double vlDesconto, int cdCategoriaDesconto,
			Double vlPago, int cdFormaPagamento, int cdCheque, String dsHistorico, int cdUsuario, GregorianCalendar dtPagamento, int stCheque,
			int cdTituloCredito, int cdTurno, AuthData authData, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			/*
			 *  Verifica a Forma de Pagamento
			 */
			FormaPagamento formaPagamento = FormaPagamentoDAO.get(cdFormaPagamento, connect);
			if(formaPagamento!=null && formaPagamento.getTpFormaPagamento()==FormaPagamentoServices.TITULO_CREDITO &&
					formaPagamento.getNmFormaPagamento().toLowerCase().indexOf("cheque")>=0 && (cdCheque<=0))	{
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-10, "Pagamento em título de crédito só é permitido com a informação do cheque!");
			}
			/*
			 *  Verifica classificação de Multa, Juros e Desconto
			 */
			if((vlMulta>0 && cdCategoriaMulta<=0) || (vlJuros>0 && cdCategoriaJuros<=0) || (vlDesconto>0 && cdCategoriaDesconto<=0))	{
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-20, "Classificação dos juros/multa ou desconto não informada!");
			}
			
			/*
			 *  Instancia conta a pagar
			 */
			ContaPagar conta = ContaPagarDAO.get(cdContaPagar, connect);
			String nrDocumento = conta.getNrDocumento();
			if(cdCheque > 0)
				nrDocumento = ChequeDAO.get(cdCheque, connect).getNrCheque();
			/*
			 *  Cria o movimento de conta
			 */
			int stMovimento = MovimentoContaServices.ST_COMPENSADO;
			if(dtPagamento.after(new GregorianCalendar()) || cdCheque>0)
				stMovimento = MovimentoContaServices.ST_NAO_COMPENSADO;
			MovimentoConta movimento = new MovimentoConta(0 /*cdMovimentoConta*/,cdConta,0 /*cdContaOrigem*/,0 /*cdMovimentoOrigem*/,
														  cdUsuario,cdCheque,0 /*cdViagem*/,dtPagamento,vlPago,nrDocumento,
														  MovimentoContaServices.DEBITO, MovimentoContaServices.toPAGAMENTO /*tpOrigem*/, stMovimento,dsHistorico,
														  null /*dtDeposito*/,null /*idExtrato*/,cdFormaPagamento,0 /*cdFechamento*/, cdTurno);
			ArrayList<MovimentoContaPagar> movimentoContaPagar = new ArrayList<MovimentoContaPagar>();
			/*
			 *  Cria o pagamento
			 */
			movimentoContaPagar.add(new MovimentoContaPagar(cdConta, 0 /*cdMovimentoConta)*/, cdContaPagar, vlPago, vlMulta,vlJuros,vlDesconto));
			/*
			 *  Cria a classificação em categorias do movimento na conta
			 */
			Double vlTotalClassificado = 0.0;
			ArrayList<MovimentoContaCategoria> movimentoCategoria = new ArrayList<MovimentoContaCategoria>();
			ResultSetMap rsmCategorias = ContaPagarCategoriaServices.getCategoriaOfContaPagar(cdContaPagar, connect);
			while(rsmCategorias.next()){
				Double vlMovimentoCategoria = rsmCategorias.getDouble("vl_conta_categoria") / (conta.getVlConta()+conta.getVlAcrescimo()-conta.getVlAbatimento()) * (vlPago-vlMulta-vlJuros+vlDesconto);
				vlTotalClassificado += vlMovimentoCategoria;
				movimentoCategoria.add(new MovimentoContaCategoria(cdConta, 0/*cdMovimentoConta*/, rsmCategorias.getInt("cd_categoria_economica"),
							                                       vlMovimentoCategoria, 0 /*cdMovimentoContaCategoria*/, cdContaPagar, 0 /*cdContaReceber*/,
							                                       MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /*tpMovimento*/, rsmCategorias.getInt("cd_centro_custo")));
			}

			if(vlMulta > 0)
				movimentoCategoria.add(new MovimentoContaCategoria(cdConta,0/*cdMovimentoConta*/,cdCategoriaMulta,vlMulta,0 /*cdMovimentoContaCategoria*/,
						                                           cdContaPagar,0 /*cdContaReceber*/,MovimentoContaCategoriaServices.TP_MULTA /*tpMovimento*/, rsmCategorias.getInt("cd_centro_custo")));
			if(vlJuros > 0)
				movimentoCategoria.add(new MovimentoContaCategoria(cdConta,0/*cdMovimentoConta*/,cdCategoriaJuros,vlJuros,0 /*cdMovimentoContaCategoria*/,
																   cdContaPagar,0 /*cdContaReceber*/,MovimentoContaCategoriaServices.TP_JUROS /*tpMovimento*/, rsmCategorias.getInt("cd_centro_custo")));
			if(vlDesconto > 0)
				movimentoCategoria.add(new MovimentoContaCategoria(cdConta,0/*cdMovimentoConta*/,cdCategoriaDesconto,vlDesconto,0 /*cdMovimentoContaCategoria*/,
						                                           cdContaPagar,0 /*cdContaReceber*/,MovimentoContaCategoriaServices.TP_DESCONTO /*tpMovimento*/, rsmCategorias.getInt("cd_centro_custo")));

			if (vlTotalClassificado+0.01 < (vlPago-vlMulta-vlJuros+vlDesconto))	{
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-30, "Conta não classificada corretamente! [Valor da Conta: "+(vlPago-vlMulta-vlJuros+vlDesconto)+", Valor Classificado: "+vlTotalClassificado+"]");
			}
			ArrayList<MovimentoContaTituloCredito> titulos = new ArrayList<MovimentoContaTituloCredito>();  
			if(cdTituloCredito > 0)
				titulos.add(new MovimentoContaTituloCredito(cdTituloCredito,0/*cdMovimentoConta*/,cdConta));
			/*
			 *  Chama o método que faz o lançamento da conta e outras operações
			 */
			Result result = MovimentoContaServices.insert(movimento, movimentoContaPagar, movimentoCategoria, titulos, -1/*stCheque*/, connect);
			if(result.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				com.tivic.manager.util.Util.registerLog(new Exception("Pagamento Resumido: Nao foi possivel salvar o movimento!"));
				result.setMessage("Pagamento Resumido: Nao foi possivel salvar o movimento! "+result.getMessage());
				return result;
			}

			/*
			 *  Altera situação do cheque
			 */
			if(cdCheque > 0 && stCheque >= 0)	{
				result = ChequeServices.verificaSituacaoCheque(cdCheque, stCheque, connect);
				if(result.getCode() <= 0) {
					if(isConnectionNull)
						Conexao.rollback(connect);
					com.tivic.manager.util.Util.registerLog(new Exception("Nao foi possivel alterar a situacao do cheque!"));
					return new Result(-50, "Nao foi possivel alterar a situacaoo do cheque!");
				}
			}
			
			//COMPLIANCE
			for (MovimentoContaPagar mcp : movimentoContaPagar) {
				ComplianceManager.process(mcp, authData, ComplianceManager.TP_ACAO_INSERT, connect);
			}

			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Pagamento Realizado com Sucesso!", "MOVIMENTOCONTAPAGAR", movimentoContaPagar);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar lançar pagamento resumido!", e);
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result setAutorizacaoPagamento(ArrayList<HashMap<String, Object>> listaContaPagar)
	{
		Connection connect = null;
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			Result resultado = new Result(1, "Contas autorizadas com sucesso!");
			
			for(HashMap<String, Object> register: listaContaPagar){
				int cdContaPagar = (Integer)register.get("cdContaPagar");
				int retorno = setAutorizacaoPagamento(cdContaPagar, connect);
				if( retorno <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					ContaPagar contaPagar = ContaPagarDAO.get(cdContaPagar);
					return new Result(-1, "Erro ao autorizar conta de Nro. " + contaPagar.getNrDocumento());
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return resultado;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.setAutorizacaoPagamento: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao autorizar pagamentos");
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int setAutorizacaoPagamento(int cdContaPagar)	{
		return setAutorizacaoPagamento(cdContaPagar, null);
	}

	public static int setAutorizacaoPagamento(int cdContaPagar, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"UPDATE adm_conta_pagar SET lg_autorizado = 1, dt_autorizacao = ? " +
					"WHERE cd_conta_pagar = "+cdContaPagar);
			pstmt.setTimestamp(1, new Timestamp(new GregorianCalendar().getTimeInMillis()));
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.setAutorizacaoPagamento: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * @author Alvaro
	 * @param cdContaPagar
	 *  Método de envelope para setCancelada
	 */
	public static Result cancelarConta(int cdContaPagar) {
		return cancelarConta(cdContaPagar, null);
	}

	public static Result cancelarConta(int cdContaPagar,  Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		String mensagem = "";
		try {
			HashMap<String, Object> retorno = setCancelada(cdContaPagar, connect) ;
			int code = (Integer)retorno.get("ErrorCode");
			return new Result( code, (code>0)?"Canceladmento efetuado com sucesso!":(String)retorno.get("ErrorMsg"),"CONTAPAGAR", ContaPagarDAO.get(cdContaPagar)  );
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			mensagem = "Erro! ContaPagarServices.cancelar: " +  e;
			System.err.println(mensagem);
			return new Result(-1, "Erro ao tentar cancelar conta.");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static HashMap<String,Object> setCancelada(int cdContaPagar) {
		return setCancelada(cdContaPagar, null);
	}

	public static HashMap<String,Object> setCancelada(int cdContaPagar, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		String mensagem = "";
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		try {
			ResultSet rs = connect.prepareStatement("SELECT * FROM adm_movimento_conta_pagar " +
													"WHERE cd_conta_pagar = "+cdContaPagar).executeQuery();
			if(rs.next()) {
				mensagem = "Erro! ContaPagarServices.cancelar: parcela tem movimentos relacionados";
				System.err.println(mensagem);
				retorno.put("ErrorCode", new Integer(-1));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}
			connect.prepareStatement("UPDATE adm_conta_pagar SET st_conta="+ST_CANCELADA+" WHERE cd_conta_pagar="+cdContaPagar).executeUpdate();
			retorno.put("ErrorCode", new Integer(1));
			retorno.put("ErrorMsg", "");
			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			mensagem = "Erro! ContaPagarServices.cancelar: " +  e;
			System.err.println(mensagem);
			retorno.put("ErrorCode", new Integer(-1));
			retorno.put("ErrorMsg", mensagem);
			return retorno;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * @category SICOE
	 * @param criterios
	 * @return
	 */
	public static ResultSetMap findWithComissao(ArrayList<ItemComparator> criterios) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try	{
			String sql = "SELECT A.*, B.nm_empresa, C.nm_pessoa, D.*, E.nr_banco, E.nm_banco, "+
						 "       F.cd_comissao, F.vl_comissao, F.tp_comissao, F.vl_pago AS vl_pago_comissao, F.pr_aplicacao, "+
						 "       G.cd_contrato_emprestimo, G.dt_contrato, G.qt_parcelas AS qt_parcelas_contrato, "+
						 "       G.dt_pagamento, G.vl_financiado, G.vl_liberado, "+
						 "       H.nm_produto, I.nm_pessoa AS nm_contratante, "+
						 "       J.nm_pessoa AS nm_parceiro "+
			             "FROM adm_conta_pagar A "+
			             "JOIN grl_empresa B ON (A.cd_empresa = B.cd_empresa) "+
			             "JOIN grl_pessoa  C ON (A.cd_pessoa  = C.cd_pessoa) "+
			             "LEFT OUTER JOIN grl_pessoa_conta_bancaria D ON (A.cd_pessoa = D.cd_pessoa "+
			             "                                            AND A.cd_conta_bancaria = D.cd_conta_bancaria) "+
			             "LEFT OUTER JOIN grl_banco                 E ON (D.cd_banco = E.cd_banco) "+
			             "JOIN sce_contrato_comissao F ON (A.cd_conta_pagar = F.cd_conta_pagar) "+
			             "JOIN sce_contrato G ON (F.cd_contrato_emprestimo = G.cd_contrato_emprestimo) "+
			             "JOIN sce_produto  H ON (G.cd_produto = H.cd_produto ) "+
			             "JOIN grl_pessoa   I ON (G.cd_contratante = I.cd_pessoa)"+
			             "JOIN grl_pessoa   J ON (H.cd_instituicao_financeira = J.cd_pessoa)";
			ResultSetMap rsm = Search.find(sql, "ORDER BY A.dt_vencimento, B.nm_empresa, C.nm_pessoa", criterios, Conexao.conectar(), true);
			int cdContaPagar = 0;
			while(rsm.next())
				if(rsm.getDouble("vl_abatimento")>0 && cdContaPagar!=rsm.getInt("cd_conta_pagar")){
					cdContaPagar = rsm.getInt("cd_conta_pagar");
					pstmt = connect.prepareStatement("SELECT * FROM sce_adiantamento_pagamento A, sce_adiantamento B "+
							                         "WHERE A.cd_conta_pagar = ? "+
							                         "  AND A.cd_adiantamento = B.cd_adiantamento");
					pstmt.setInt(1, rsm.getInt("cd_conta_pagar"));
					ResultSet rs = pstmt.executeQuery();
					while(rs.next()){
						HashMap<String,Object> register = new HashMap<String,Object>();
						register.put("CD_CONTA_PAGAR", new Integer(rsm.getInt("cd_conta_pagar")));
						register.put("CD_ADIANTAMENTO", new Integer(rs.getInt("cd_adiantamento")));
						register.put("NM_EMPRESA", rsm.getString("NM_EMPRESA"));
						register.put("NM_PESSOA", rsm.getString("NM_PESSOA"));
						register.put("NM_PARCEIRO", "");
						register.put("NM_PRODUTO", "");
						register.put("TP_COMISSAO", new Integer(-1));
						register.put("VL_PAGO_COMISSAO", new Float(rs.getDouble("VL_PAGO")*(-1)));
						register.put("DT_CONTRATO", rs.getTimestamp("DT_ADIANTAMENTO"));
						register.put("VL_LIBERADO", new Float(rs.getDouble("VL_ADIANTAMENTO")));
						register.put("PR_APLICACAO", new Float(0));
						register.put("QT_PARCELAS_CONTRATO", new Integer(rs.getInt("QT_PARCELAS")));
						register.put("NM_CONTRATANTE", "Desconto de Adiantamento");
						rsm.addRegister(register);
					}
				}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.findWithComissao: " +  e);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/**
	 * @category SICOE
	 */
	public static int setPagamento(int cdContaPagar, GregorianCalendar dtPagamento, float vlPago) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try	{
			// Confirmando pagamento da conta
			connect.setAutoCommit(false);
			pstmt = connect.prepareStatement(
					"UPDATE adm_conta_pagar SET st_conta = 1, dt_pagamento = ?, vl_pago = "+vlPago+
					" WHERE cd_conta_pagar = "+cdContaPagar);
			pstmt.setTimestamp(1, new Timestamp(dtPagamento.getTimeInMillis()));
			pstmt.executeUpdate();
			// Confirmando o dia de pagamento dos agentes
			pstmt = connect.prepareStatement(
					"UPDATE sce_contrato " +
					"SET dt_pagamento_agente = (SELECT MAX(A.dt_pagamento) " +
					"                           FROM adm_conta_pagar A, sce_contrato_comissao B "+
                    "                           WHERE A.cd_conta_pagar = B.cd_conta_pagar "+
                    "                             AND B.cd_contrato_emprestimo = sce_contrato.cd_contrato_emprestimo) "+
                    "WHERE cd_contrato_emprestimo IN (SELECT cd_contrato_emprestimo FROM sce_contrato_comissao CC " +
                    "                                 WHERE CC.cd_conta_pagar = "+cdContaPagar+")");
			pstmt.executeUpdate();
			// Atualiza situação das comissões
			pstmt = connect.prepareStatement(
					"UPDATE sce_contrato_comissao SET dt_pagamento = ?, cd_situacao = ? "+
					"WHERE cd_conta_pagar = ? ");
			pstmt.setTimestamp(1, new Timestamp(dtPagamento.getTimeInMillis()));
			if (ParametroServices.getValorOfParametro("CD_COMISSAO_PAGA")==null)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2, new Integer(ParametroServices.getValorOfParametro("CD_COMISSAO_PAGA")).intValue());
			pstmt.setInt(3, cdContaPagar);
			pstmt.executeUpdate();
			connect.commit();
			return 1;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.setContaPagar: " +  e);
			return -1;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/**
	 * @category SICOE
	 */
	public static int setPagamento(String lista, GregorianCalendar dtPagamento) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try	{
			connect.setAutoCommit(false);
			pstmt = connect.prepareStatement(
					"UPDATE adm_conta_pagar SET st_conta = 1, dt_pagamento = ?, vl_pago = vl_conta "+
					"WHERE cd_conta_pagar IN ("+lista+")");
			pstmt.setTimestamp(1, new Timestamp(dtPagamento.getTimeInMillis()));
			pstmt.executeUpdate();
			// Atualiza situação das comissões
			pstmt = connect.prepareStatement(
					"UPDATE sce_contrato_comissao SET dt_pagamento = ?, cd_situacao = ? "+
					"WHERE cd_conta_pagar IN ("+lista+")");
			pstmt.setTimestamp(1, new Timestamp(dtPagamento.getTimeInMillis()));
			if (ParametroServices.getValorOfParametro("CD_COMISSAO_PAGA")==null)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2, new Integer(ParametroServices.getValorOfParametro("CD_COMISSAO_PAGA")).intValue());
			pstmt.executeUpdate();
			connect.commit();
			return 1;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.setContaPagar: " +  e);
			return -1;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/**
	 * @category SICOE
	 */
	public static int atualizaValoresConta(int cdContaPagar, Connection connect) {
		boolean isConnectionNull = connect==null;
		PreparedStatement pstmt;
		try	{
			Double vlConta = 0.0;
			Double vlAbatimento = 0.0;
			// Somando comissões
			ResultSet rs = connect.prepareStatement("SELECT SUM(vl_pago) FROM sce_contrato_comissao "+
													"WHERE cd_conta_pagar = "+cdContaPagar+
													"  AND vl_pago > 0").executeQuery();
			if(rs.next())
				vlConta = rs.getDouble(1);
			// Somando estornos
			rs = connect.prepareStatement("SELECT SUM(vl_pago) FROM sce_contrato_comissao "+
										  "WHERE cd_conta_pagar = "+cdContaPagar+
										  "  AND vl_pago < 0").executeQuery();
			if(rs.next())
				vlAbatimento = (rs.getDouble(1) * -1);
			// Somando Adiantamentos
			rs = connect.prepareStatement("SELECT SUM(vl_pago) FROM sce_adiantamento_pagamento "+
					 					  "WHERE cd_conta_pagar = "+cdContaPagar).executeQuery();
			if(rs.next())
				vlAbatimento += rs.getDouble(1);
			// Atualizando valor da conta
			if(vlConta==0 && vlAbatimento==0)	{
				pstmt = connect.prepareStatement("DELETE FROM adm_conta_pagar WHERE cd_conta_pagar = "+cdContaPagar);
			}
			else	{
				pstmt = connect.prepareStatement(
						 "UPDATE adm_conta_pagar SET vl_conta = "+vlConta+", vl_abatimento = "+vlAbatimento+
						 " WHERE cd_conta_pagar = "+cdContaPagar);
			}
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.setContaPagar: " +  e);
			return -1;
		}finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * @category SICOE
	 */
	public static int atualizaValoresConta() {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try	{
			// Atualizando valores das comissões
			pstmt = connect.prepareStatement(
				"SELECT cd_comissao, pr_aplicacao, vl_liberado, vl_comissao, A.vl_pago "+
				"FROM sce_contrato_comissao A, sce_contrato B, adm_conta_pagar C, sce_produto D, "+
				"     grl_pessoa E, grl_empresa F "+
				"WHERE A.cd_contrato_emprestimo = B.cd_contrato_emprestimo "+
				"  AND A.cd_conta_pagar = C.cd_conta_pagar "+
				"  AND C.st_conta = 0 "+
				"  AND B.cd_produto = D.cd_produto "+
				"  AND B.cd_empresa = F.cd_empresa "+
				"  AND D.nm_produto LIKE \'INSS%\'"+
				"  AND D.cd_instituicao_financeira = E.cd_pessoa "+
				"  AND E.nm_pessoa IN (\'GE\',\'BMC\') "+
				"  AND F.nm_empresa IN (\'MACEIO\',\'SALVADOR\',\'V. DA CONQUISTA\',\'ILHEUS\') "+
				"  AND A.tp_comissao = 2 "+
				"  AND B.qt_parcelas = 36 ");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next())	{
				pstmt = connect.prepareStatement(
						"UPDATE sce_contrato_comissao SET vl_pago = ? "+
						"WHERE cd_comissao = ?");
				pstmt.setDouble(1, rsm.getDouble("pr_aplicacao") * rsm.getDouble("vl_liberado") / 100);
				pstmt.setInt(2, rsm.getInt("cd_comissao"));
				pstmt.executeUpdate();
			}
			// Somando comissões
			pstmt = connect.prepareStatement(
					 "SELECT cd_conta_pagar FROM adm_conta_pagar "+
					 "WHERE st_conta = 0 ");
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())	{
				atualizaValoresConta(rs.getInt(1), connect);
			}
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.setContaPagar: " +  e);
			return -1;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/**
	 * @category SICOE
	 */
	public static int setAutorizacao(String lista, int lgAutorizacao) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try	{
			GregorianCalendar dt = new GregorianCalendar();
			dt = new GregorianCalendar(dt.get(Calendar.YEAR), dt.get(Calendar.MONTH), dt.get(Calendar.DAY_OF_MONTH));
			connect.setAutoCommit(false);
			pstmt = connect.prepareStatement(
					"UPDATE adm_conta_pagar SET lg_autorizado = ?, dt_autorizacao = ? "+
					"WHERE cd_conta_pagar IN ("+lista+")");
			pstmt.setInt(1, lgAutorizacao);
			pstmt.setTimestamp(2, new Timestamp(dt.getTimeInMillis()));
			pstmt.executeUpdate();
			connect.commit();
			return 1;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.setContaPagar: " +  e);
			return -1;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/**
	 * @category SICOE
	 */
	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy) {
		String group  = "";
		String fields = " A.*, B.nm_empresa, C.nm_pessoa, D.*, E.nr_banco, E.nm_banco ";

		for(int i=0; i<groupBy.size();i++)	{
			String nmToGroup = (String)groupBy.get(i);
			String nmToField = (String)groupBy.get(i);
			if(nmToGroup.toUpperCase().indexOf("AS")>=0)	{
				nmToGroup = nmToGroup.substring(0, nmToGroup.toUpperCase().indexOf("AS"));
				nmToField = nmToField.substring(nmToField.toUpperCase().indexOf("AS")+3, nmToField.length()).trim();
			}
			//
			if(nmToField.equals("NM_EMPRESA"))	{
				nmToField = " A.cd_empresa, "+nmToGroup+" AS "+nmToField;
				nmToGroup = " A.cd_empresa, "+nmToGroup;
			}
			else if(nmToField.equals("NM_PESSOA"))	{
				nmToField = " A.cd_pessoa, "+nmToGroup+" AS "+nmToField;
				nmToGroup = " A.cd_pessoa, "+nmToGroup;
			}
			else if(nmToField.equals("NM_BANCO"))	{
				nmToField = " E.cd_banco, nr_banco, "+nmToGroup+" AS "+nmToField;
				nmToGroup = " E.cd_banco, E.nr_banco, "+nmToGroup;
			}
			else
				nmToField = nmToGroup+" AS "+nmToField;
			if(i==0)	{
				group  = "GROUP BY "+nmToGroup;
				fields = nmToField;
			}
			else	{
				fields = fields+", "+nmToField;
				group  = group+", "+nmToGroup;
			}
		}
		fields = groupBy.size()==0 ? fields : fields + ", COUNT(*) AS QT_CONTA, SUM(vl_conta) AS vl_conta,  "+
		                                               "  SUM(vl_abatimento) AS vl_abatimento ";


		String sql = "SELECT "+fields+" "+
		             "FROM adm_conta_pagar A "+
		             "JOIN grl_empresa B ON (A.cd_empresa = B.cd_empresa) "+
	    	         "JOIN grl_pessoa  C ON (A.cd_pessoa  = C.cd_pessoa) "+
	        	     "LEFT OUTER JOIN grl_pessoa_conta_bancaria D ON (A.cd_pessoa = D.cd_pessoa "+
	            	 "                                            AND A.cd_conta_bancaria = D.cd_conta_bancaria) "+
		             "LEFT OUTER JOIN grl_banco                 E ON (D.cd_banco = E.cd_banco) ";
		return Search.find(sql, group, criterios, Conexao.conectar(), true);
	}
	
	public static Result getReciboPagamento(int cdConta, int cdMovimentoConta, int cdContaPagar){
		try{
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("TP_RECIBO", "PAGAR");
			
			
			MovimentoContaPagar movContaPagar = MovimentoContaPagarDAO.get( cdConta, cdMovimentoConta, cdContaPagar);
			MovimentoConta movConta = MovimentoContaDAO.get(cdMovimentoConta, cdConta);
			ContaPagar conta = ContaPagarDAO.get(cdContaPagar);
			Pessoa cliente = PessoaDAO.get( conta.getCdPessoa() );
			PessoaEndereco clienteEndereco = PessoaEnderecoServices.getEnderecoPrincipal(cliente.getCdPessoa());
			
			Pessoa empresa = PessoaJuridicaDAO.get(conta.getCdEmpresa());
			
			String nrDocumento = ( ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_CHEQUE", 0) == movConta.getCdFormaPagamento() )?movConta.getNrDocumento():null;
			String nmFavorecido = "";
			String nrIdentidade = "";
			String endereco = clienteEndereco.getNmLogradouro();
			if( PessoaFisicaDAO.get( cliente.getCdPessoa() ) != null ){
				PessoaFisica clientePessoa =  PessoaFisicaDAO.get( cliente.getCdPessoa() );
				nmFavorecido = clientePessoa.getNmPessoa();
				nrIdentidade = "CPF: "+Util.formatCpf( clientePessoa.getNrCpf() );
			}else{
				PessoaJuridica clienteJur =  PessoaJuridicaDAO.get( cliente.getCdPessoa() );
				nmFavorecido = clienteJur.getNmPessoa();
				nrIdentidade = "CNPJ: "+Util.formatCnpj( clienteJur.getNrCnpj() );
			}
			
			ContaFinanceira contaFin = ContaFinanceiraDAO.get( movConta.getCdConta() );
			ChequeDAO.get( movConta.getCdFormaPagamento() );
			
			register.put("NM_SACADO", empresa.getNmPessoa());
			register.put("ENDEREÇO", endereco );
			register.put("VL_EXTENSO", Util.capitular( Util.formatExtenso( movContaPagar.getVlPago().floatValue() , true)) );
			register.put("DS_REFERENTE", movConta.getDsHistorico() );
			register.put("NM_FAVORECIDO", nmFavorecido );
			register.put("NR_IDENTIDADE", nrIdentidade );
			register.put("NR_RECIBO", conta.getNrDocumento());
			register.put("VL_RECEBIDO",  movContaPagar.getVlPago() );
			
			String dsExtenso1 = new DecimalFormat("###,###,##0.00", 
								new DecimalFormatSymbols(new Locale("pt","BR"))
							).format(movContaPagar.getVlPago())+" ( ";
			dsExtenso1 += Util.capitular( Util.formatExtenso( movContaPagar.getVlPago().floatValue() , true));
			String dsExtenso2 = Util.fill("", 81, '*', 'D')+" ).";
			if( dsExtenso1.length() > 63 ){
				dsExtenso2 = Util.fill( dsExtenso1.substring(59), 81, '*', 'D')+" ).";
				dsExtenso1 = dsExtenso1.substring(0, 59);
			}else{
				dsExtenso1 = Util.fill( dsExtenso1, 63, '*', 'D');
			}
			
			register.put("DS_EXTENSO_1", dsExtenso1.toUpperCase()  );
			register.put("DS_EXTENSO_2", dsExtenso2.toUpperCase()  );

			if( contaFin.getTpConta() == ContaFinanceiraServices.TP_CONTA_BANCARIA )
				register.put("NR_CONTA", contaFin.getNrConta()+" - "+contaFin.getNrDv() );
			else
				register.put("NR_CONTA", contaFin.getNrConta() );
			
			register.put("NR_CHEQUE", nrDocumento  );
			register.put("DT_MOVIMENTO", Util.formatDate(movConta.getDtMovimento(), "dd/MM/yyyy") );
			
			ResultSetMap rsmRecibo = new ResultSetMap();
			rsmRecibo.addRegister(register);
			Result result = new Result(1);
			result.addObject("rsm", rsmRecibo);
			result.addObject("params", params);
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return new Result(-1, e.getMessage());
		}
	}
	
	public static ResultSetMap getTotalDespesasMensais( ArrayList<ItemComparator> criterios ) {
		return getTotalDespesasMensais(criterios, null);
	}
	
	public static ResultSetMap getTotalDespesasMensais( ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ResultSetMap rsm = Search.find(
					" SELECT SUM( vl_conta ) as vl_conta, cd_empresa, "+
					" CAST( year AS INTEGER), CAST( month AS INTEGER) "+
					" FROM (  										"+
					"	SELECT  vl_conta, cd_empresa,  				"+
					"	EXTRACT( month FROM dt_emissao) as month, 	"+ 
					"	EXTRACT( year FROM dt_emissao) as year    	"+
					"	FROM adm_conta_pagar 						"+
					"	WHERE dt_emissao IS NOT NULL				"+
					"	AND   ST_CONTA NOT IN ( "+ST_CANCELADA+","+ST_PERDA+" )	"+
					" ) AS tmp      "+
					" WHERE 1=1 	",	
					" GROUP BY tmp.month,  tmp.cd_empresa, tmp.year "+
					" ORDER BY cd_empresa, year, month ASC",
					criterios, connect, isConnectionNull);
			return rsm;
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getLogCompliance(int cdConta, boolean lgDelete) {
		return getLogCompliance(cdConta, lgDelete, null);
	}
	
	public static ResultSetMap getLogCompliance(int cdConta, boolean lgDelete, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			ResultSetMap rsm = ComplianceManager
					.search(" SELECT * FROM adm_conta_pagar "
						+ " WHERE 1=1"
						+ (lgDelete ? 
						  " AND tp_acao_compliance="+ComplianceManager.TP_ACAO_DELETE	
						  :
						  " AND cd_conta_pagar="+cdConta)
						+ " ORDER BY dt_compliance DESC ");
			
			while(rsm.next()) {
				if(rsm.getPointer()==0 && !lgDelete)
					rsm.setValueToField("tp_versao_compliance", "ATUAL");
				else
					rsm.setValueToField("tp_versao_compliance", "ANTIGA");
				
				rsm.setValueToField("nm_tp_acao", ComplianceManager.tipoAcao[rsm.getInt("tp_acao_compliance")].toUpperCase());
				
				if(rsm.getInt("cd_usuario_compliance", 0) > 0) {
					Usuario usuario = UsuarioDAO.get(rsm.getInt("cd_usuario_compliance"), connect);
					Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);
					rsm.setValueToField("nm_usuario_compliance", pessoa.getNmPessoa());
				}
				
				//tomador/prestador
				if(rsm.getInt("cd_pessoa", 0) > 0) {
					Pessoa pessoa = PessoaDAO.get(rsm.getInt("cd_pessoa"), connect);
					rsm.setValueToField("nm_pessoa", pessoa.getNmPessoa());
				}
				
				rsm.setValueToField("nm_st_conta", situacaoContaPagar[rsm.getInt("st_conta")].toUpperCase());
				
				
				
				if(lgDelete) {
					ResultSetMap rsmDetalhes = new ResultSetMap();
					HashMap<String, Object> regAtual = (HashMap<String, Object>)rsm.getRegister().clone();
					regAtual.put("TP_ITEM_COMPLIANCE", " ");
					rsmDetalhes.addRegister(regAtual);
					rsm.setValueToField("RSM_DETALHE", rsmDetalhes);
					
				}
			}
			rsm.beforeFirst();

			
			if(!lgDelete) {
				while(rsm.next()) {
					ResultSetMap rsmDetalhes = new ResultSetMap();
					HashMap<String, Object> regAtual = (HashMap<String, Object>)rsm.getRegister().clone();
					regAtual.put("TP_ITEM_COMPLIANCE", "PARA");
					rsmDetalhes.addRegister(regAtual);
					
					if(rsm.next()) { //como a ordem é decrescente, o próximo registro representa a versão anterior
						HashMap<String, Object> regAnterior = (HashMap<String, Object>)rsm.getRegister().clone();
						regAnterior.put("TP_ITEM_COMPLIANCE", "DE");
						rsmDetalhes.addRegister(regAnterior);
						rsm.previous();
					}
					
					ArrayList<String> fields = new ArrayList<>();
					fields.add("TP_ITEM_COMPLIANCE");
					rsmDetalhes.orderBy(fields);
					rsm.setValueToField("RSM_DETALHE", rsmDetalhes);
					
				}
				rsm.beforeFirst();
			}
			
			return rsm;
		}
		catch (Exception e) {
			System.out.println("Erro ao buscar log: "+e.getMessage());
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}