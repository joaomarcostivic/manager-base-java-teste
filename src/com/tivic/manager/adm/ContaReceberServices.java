package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.tivic.manager.alm.DocumentoSaida;
import com.tivic.manager.alm.DocumentoSaidaDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.ctb.CentroCusto;
import com.tivic.manager.ctb.CentroCustoDAO;
import com.tivic.manager.egov.DAMServices;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.ArquivoRegistro;
import com.tivic.manager.grl.ArquivoRegistroDAO;
import com.tivic.manager.grl.ArquivoRegistroServices;
import com.tivic.manager.grl.ArquivoTipoRegistro;
import com.tivic.manager.grl.ArquivoTipoRegistroServices;
import com.tivic.manager.grl.BancoDAO;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaDAO;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.FeriadoServices;
import com.tivic.manager.grl.NumeracaoDocumentoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.prc.Processo;
import com.tivic.manager.prc.ProcessoDAO;
import com.tivic.manager.prc.ProcessoFinanceiroServices;
import com.tivic.sol.report.ReportServices;
import com.tivic.manager.ptc.DocumentoConta;
import com.tivic.manager.ptc.DocumentoContaDAO;
import com.tivic.manager.ptc.DocumentoContaServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.ComplianceManager;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Recursos;
import com.tivic.manager.util.Util;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.mail.SMTPClient;
import sol.util.DateServices;
import sol.util.Result;

public class ContaReceberServices {

	public static final int VENC_ADESAO_PARCELA1_DISTINTOS = 0;
	public static final int VENC_ADESAO_PARCELA1_IGUAIS    = 1;

	public static final int ADESAO_VENC_PRIMEIRA        = 0;
	public static final int ADESAO_VENC_ASSINATURA      = 1;
	public static final int ADESAO_VENC_INICIO_VIGENCIA = 2;

	public static final String[] tipoFrequencia = {"Apenas uma vez","Quantidade fixa", "Diário", "Semanal",
												   "Semanas Alternadas","Duas vezes por mês", "A cada 4 semanas",
												   "Mensal", "Meses alternados", "Trimestral", "Duas vezes por ano",
												   "Anual", "Anos alternados"};

	public static final String[] situacaoContaReceber = {"Em aberto","Recebida","Renegociada","Cancelada","Perda","Prorrogada","Refaturada"};

	public static final int ST_EM_ABERTO  = 0;
	public static final int ST_RECEBIDA   = 1;
	public static final int ST_NEGOCIADA  = 2;
	public static final int ST_CANCELADA  = 3;
	public static final int ST_PERDA  	  = 4;
	public static final int ST_PRORROGADA = 5;
	public static final int ST_REFATURADA = 6;
		
	public static int UNICA_VEZ  	        = 0;
	public static int QUANTIDADE_FIXA       = 1;
	public static int DIARIO    	        = 2;
	public static int SEMANAL               = 3;
	public static int SEMANAS_ALTERNADAS    = 4;
	public static int DUAS_VEZES_MES 	    = 5;
	public static int A_CADA_QUATRO_SEMANAS = 6;
	public static int MENSAL                = 7;
	public static int MESES_ALTERNADOS 	    = 8;
	public static int TRIMESTRAL            = 9;
	public static int A_CADA_QUATRO_MESES   = 10;
	public static int DUAS_VEZES_ANO        = 11;
	public static int ANUAL                 = 12;
	public static int ANOS_ALTERNADOS       = 13;

	public static final int OF_CONTRATO          = 0;
	public static final int OF_DOCUMENTO_SAIDA   = 1;
	public static final int OF_OUTRA_CONTA       = 2;
	public static final int OF_NEGOCIACAO	     = 3;

	public static final int TP_TAXA_ADESAO = 0;
	public static final int TP_PARCELA = 1;
	public static final int TP_MANUTENCAO          = 2;
	public static final int TP_RESCISAO_CONTRATUAL = 3;
	public static final int TP_NEGOCIACAO = 4;
	public static final int TP_OUTRO = 5;

	public static final String[] tiposContaReceber = {"Taxa de Adesão","Parcela","Manutenção","Rescisão contratual","Negociação","Outro"};

	public static final int ERR_CLASSIFICACAO = -30;
	public static final int ERR_MOVIMENTO     = -40;
	
	
	public static Result save(ContaReceber contaReceber){
		return save(contaReceber, null, null, null, null);
	}
	
	/**
	 * @author ddk/Alvaro
	 * @param contaReceber
	 * @param TituloCredito
	 * @return
	 **/
	public static Result save(ContaReceber contaReceber, TituloCredito titulo){
		return save(contaReceber, titulo, null, null, null);
	}

	/**
	 * @author ddk/Alvaro
	 * @param contaReceber
	 * @param TituloCredito
	 * @param ContaReceberCategorias ArrayList
	 * @return
	 **/
	public static Result save(ContaReceber contaReceber, TituloCredito titulo, ArrayList<ContaReceberCategoria> categorias){
		return save(contaReceber, titulo, categorias, null, null);
	}
	
	public static Result save(ContaReceber contaReceber, TituloCredito titulo, ArrayList<ContaReceberCategoria> categorias, AuthData authData){
		return save(contaReceber, titulo, categorias, authData, null);
	}
	
	
	/**
	 * @author ddk/Alvaro
	 * @param contaReceber
	 * @param TituloCredito
	 * @param ContaReceberCategorias ArrayList
	 * @param Connection
	 * @return
	 **/
	public static Result save(ContaReceber contaReceber, TituloCredito titulo, ArrayList<ContaReceberCategoria> categorias, Connection connect){
		return save(contaReceber, titulo, categorias, null, connect);
	}
	
	public static Result save(ContaReceber contaReceber, TituloCredito titulo, ArrayList<ContaReceberCategoria> categorias, 
			AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(contaReceber==null)
				return new Result(-1, "Erro ao salvar. ContaReceber é nulo");
			
			if( contaReceber.getCdContaReceber() <= 0 && ParametroServices.getValorOfParametroAsInteger("LG_NUM_DOC_CONTA_RECEBER_AUTOMATICO", 0) > 0 )
				contaReceber.setNrDocumento( gerarNrDocumento( contaReceber.getCdEmpresa(), connect ) );
			//Verifica a Duplicidade de conta
			if( contaReceber.getCdContaReceber() <= 0 && isContaDuplicada(contaReceber, connect) )	{
				com.tivic.manager.util.Util.registerLog(new Exception("Conta a receber duplicada!"));
				return new Result(-23, "Conta a receber duplicada", "CONTARECEBER", contaReceber);
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.nr_documento", contaReceber.getNrDocumento(), ItemComparator.EQUAL, Types.VARCHAR));
			criterios.add(new ItemComparator("A.vl_conta", String.valueOf(contaReceber.getVlConta()), ItemComparator.EQUAL, Types.VARCHAR));
			criterios.add(new ItemComparator("A.cd_conta_receber", String.valueOf(contaReceber.getCdContaReceber()), ItemComparator.DIFFERENT, Types.INTEGER));
			
			ResultSetMap rsmContaReceber = ContaReceberServices.find(criterios, connect);
			
			if(rsmContaReceber.size()>0) {
				return new Result(-1, "Já existe uma conta com este número e valor!");
			}
			
			/*
			 *  Criando registro da conta para enviar no arquivo e gerando nosso numero
			 */
			boolean registrarCobranca = gerarNossoNumeroAndRegistro(contaReceber, connect);
			
			int retorno;
			int tpAcao = ComplianceManager.TP_ACAO_ANY;
			if(contaReceber.getCdContaReceber()==0){
				contaReceber.setDtDigitacao(new GregorianCalendar());
				retorno = ContaReceberDAO.insert(contaReceber, connect);
				contaReceber.setCdContaReceber(retorno);
				tpAcao = ComplianceManager.TP_ACAO_INSERT;				
			}
			else {
				retorno = ContaReceberDAO.update(contaReceber, connect);
				tpAcao = ComplianceManager.TP_ACAO_UPDATE;
			}
			
			/*
			 *  Titulo de credito
			 */
			if(retorno > 0 && titulo != null ){
				TituloCredito tituloExistente;
				if( ( tituloExistente = TituloCreditoServices.getByContaReceber( contaReceber.getCdContaReceber() ) ) != null ){
					titulo.setCdTituloCredito( tituloExistente.getCdTituloCredito() );
				}
				
				titulo.setCdConta(contaReceber.getCdConta());
				titulo.setCdContaReceber(contaReceber.getCdContaReceber());
				Result resultTitulo = TituloCreditoServices.save( titulo, connect );
				if( resultTitulo.getCode() <= 0  ){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return resultTitulo;
				}
			}
			
			/*
			 *  Registrando cobranca
			 */
			if(registrarCobranca) {
				if (registrarCobranca(contaReceber, false, connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					Exception e = new Exception("Erro ao registrar cobranÃ§a!");
					com.tivic.manager.util.Util.registerLog(e);
					e.printStackTrace(System.out);
					return new Result(-40, e.getMessage(), e);
				}
			}
			
			/*
			 * Gerando repeticoes
			 */
			if(contaReceber.getTpFrequencia()>UNICA_VEZ && contaReceber.getCdContaOrigem()<=0)	{
				Result result = gerarParcelasOutraConta(contaReceber, connect);
				
				if( contaReceber.getCdContaReceber() > 0 && StringUtils.right(contaReceber.getNrDocumento(), 4 ).equals("-001") ){
					String nrDocumento = contaReceber.getNrDocumento().substring( 0, contaReceber.getNrDocumento().length() - 4 );
					contaReceber.setNrDocumento( nrDocumento );
				}
				String idContaReceber = contaReceber.getNrDocumento().replaceAll("[/]","").replaceAll("[-]","");
				contaReceber.setIdContaReceber(idContaReceber);
				contaReceber.setNrDocumento( contaReceber.getNrDocumento()+"-"+Util.fillNum(contaReceber.getNrParcela(), 3) );
				ContaReceberDAO.update(contaReceber, connect);
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
				//COMPLIANCE
				ComplianceManager.process(contaReceber, authData, ComplianceManager.TP_ACAO_UPDATE, connect);
			}
			
			
			/**
			 * Gravando Categorias
			 */
			if( categorias == null ){
				/*
				 *  Criando categoria default
				 */
				String idParam = contaReceber.getCdDocumentoSaida()>0 ? "CD_CATEGORIA_SAIDAS_DEFAULT" : "CD_CATEGORIA_RECEITAS_DEFAULT";
				int cdCategoriaDefault = ParametroServices.getValorOfParametroAsInteger(idParam, 0, contaReceber.getCdEmpresa(), connect);
				categorias = new ArrayList<ContaReceberCategoria>();
				if (cdCategoriaDefault > 0){
					categorias.add(new ContaReceberCategoria(0, cdCategoriaDefault,
									contaReceber.getVlConta() -
									contaReceber.getVlAbatimento() +
									contaReceber.getVlAcrescimo(), 0));
				}else{
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-25, "Classificaï¿½ï¿½o padrÃ£o nÃ£o estÃ¡ configurada! Cadastre ao menos uma Classificaï¿½ï¿½o", "CONTARECEBER", contaReceber);
				}
				
			}
			Double vlConta =  contaReceber.getVlConta();
			Double vlAClassificar = vlConta, vlClassificado = 0.0d;
			for( ContaReceberCategoria categoria : categorias ){
				vlClassificado += categoria.getVlContaCategoria();
				vlAClassificar -= categoria.getVlContaCategoria();
				categoria.setCdContaReceber( contaReceber.getCdContaReceber() );
				Result resultCategoria = ContaReceberCategoriaServices.save(categoria, authData, connect);
				if( resultCategoria.getCode() <= 0  ){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-26, "Conta a receber nÃ£o classificada corretamente!", "CONTARECEBER", contaReceber);
				}
			}
			if(vlAClassificar > 0.01){
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-26,
						"Conta sem a devida classificaï¿½ï¿½o! Valor da Conta: "+Util.formatNumber(contaReceber.getVlConta())+", Valor classificado: "+Util.formatNumber(vlClassificado),
						"CONTARECEBER", contaReceber); 
			}
			

			//COMPLIANCE
			ComplianceManager.process(contaReceber, authData, tpAcao, connect);
			
			if(retorno<=0 )
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONTARECEBER", contaReceber);
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
	 * @param contaReceber
	 * @return true or false
	 */
	private static boolean isContaDuplicada(ContaReceber contaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			String sql = "SELECT * FROM adm_conta_receber " +
	   			  "WHERE cd_empresa = "+contaReceber.getCdEmpresa()+
	   			  "  AND cd_pessoa  = "+contaReceber.getCdPessoa()+
	   			  "  AND vl_conta   = "+contaReceber.getVlConta()+
	   			  (nrDocumentoIsNotNull(contaReceber) ? "  AND nr_documento "+
	   					  (Util.getConfManager().getIdOfDbUsed().equals("FB") ? " like " : " ilike ") + 
	   					  " '"+ contaReceber.getNrDocumento() + "'" : "" )+
	   			  "  AND dt_vencimento = ?";
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(contaReceber.getDtVencimento().getTimeInMillis()));
			
			if(pstmt.executeQuery().next())	{
				com.tivic.manager.util.Util.registerLog(new Exception("Conta a receber duplicada!"));
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
			ResultSet rs = connect.prepareStatement(" SELECT COUNT(cd_conta_receber) as QT_CONTAS , MAX(NR_DOCUMENTO) as NR_DOCUMENTO FROM ADM_CONTA_RECEBER "+
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
	
	
	public static void baixaPolimedic()	{
		Connection connect = Conexao.conectar();
		try	{
			ResultSet rs = connect.prepareStatement("SELECT * FROM adm_conta_receber WHERE st_conta = 0 AND dt_vencimento < \'01/10/2010\'").executeQuery();
			while(rs.next())	{
				setBaixaResumida(rs.getInt("cd_conta_receber"), rs.getInt("cd_conta"), 0.0, 0, 0.0, 0, 0.0, 0, rs.getDouble("vl_conta"),
						         3, null, 1, null, Util.convTimestampToCalendar(rs.getTimestamp("dt_vencimento")), null, connect);
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static final int createTiposDocumentosDefault() {
		Connection connection = null;
		try {
			connection = Conexao.conectar();
			connection.setAutoCommit(false);
			/*
			for (int i=0; i<_tipoDocumentoConta.length; i++) {
				if (TipoDocumentoDAO.insert(new TipoDocumento(0, _tipoDocumentoConta[i], _siglaDocumentoConta[i], i+"", 1), connection) <= 0) {
					Conexao.rollback(connection);
					return -1;
				}
			}
			*/
			connection.commit();
			return 2;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return -1;
		}
		finally {
			Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap findVencidasGroupCliente(ArrayList<ItemComparator> criterios) {
		return findVencidasGroupCliente(criterios, null);
	}

	public static ResultSetMap findVencidasGroupCliente(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			if( isConnectionNull )
				connect = Conexao.conectar();
			if( criterios == null )
				criterios = new ArrayList<ItemComparator>();
			
			int tpAgrupamento = 0;
			String nmPessoa = null;
			for (int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("tpAgrupamento")) {
					tpAgrupamento = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("nmPessoa")) {
					nmPessoa = criterios.get(i).getValue();
					criterios.remove(i);
				}
			}
			ResultSetMap rsm = new ResultSetMap();
			ResultSetMap rsmContas = Search.find(
					" SELECT * " +
					" FROM ADM_CONTA_RECEBER A "+
					" LEFT JOIN GRL_PESSOA B ON ( A.CD_PESSOA = B.CD_PESSOA )"+
					" LEFT JOIN ADM_TIPO_DOCUMENTO C ON ( A.CD_TIPO_DOCUMENTO = C.CD_TIPO_DOCUMENTO )"+
					" WHERE A.ST_CONTA = "+ST_EM_ABERTO+
					"   AND A.DT_VENCIMENTO <= '" +Util.formatDate( new GregorianCalendar(), "yyyy-MM-dd HH:mm:ss")+"' "+
					(nmPessoa != null ? " AND TRANSLATE(B.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE TRANSLATE('%"+nmPessoa+"%', 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') ": ""),
					" ORDER BY A.CD_PESSOA, A.CD_CONTA_RECEBER ",
			        criterios, connect, false);
			int cdPessoaAtual = 0;
			int cdContaReceber = 0;
			Double vlTotal = 0.0;
			Double vlTotalAreceber = 0.0;
			Double vlTotalRecebido = 0.0;
			rsmContas.beforeFirst();
			while( rsmContas.next() ){
				if( cdPessoaAtual != rsmContas.getInt("CD_PESSOA")){
					if( cdPessoaAtual > 0){
						rsm.setValueToField("VL_CONTA", vlTotal);
						rsm.setValueToField("VL_ARECEBER", vlTotalAreceber);
						rsm.setValueToField("VL_RECEBIDO", vlTotalRecebido);
						vlTotal = 0.0;
						vlTotalRecebido = 0.0;
						vlTotalAreceber = 0.0;
					}
					HashMap<String, Object> reg = new HashMap<String, Object>();
					reg.put("NM_SACADO", rsmContas.getString("NM_PESSOA"));
					cdPessoaAtual = rsmContas.getInt("CD_PESSOA");
					reg.put("CONTAS", new ArrayList<HashMap<String, Object>>());
					rsm.addRegister(reg);
					rsm.last();
				}
				rsmContas.setValueToField("VL_ARECEBER", rsmContas.getDouble("VL_CONTA") - rsmContas.getDouble("VL_RECEBIDO") );

				vlTotal += rsmContas.getDouble("VL_CONTA");
				vlTotalRecebido += rsmContas.getDouble("VL_RECEBIDO");
				vlTotalAreceber += rsmContas.getDouble("VL_ARECEBER");
				rsmContas.setValueToField("QT_DIAS_ATRASO", Util.getQuantidadeDias(rsmContas.getGregorianCalendar("DT_VENCIMENTO"), new GregorianCalendar())  );
				ResultSetMap rsmCategoria = ContaReceberCategoriaServices.getCategoriaOfContaReceber(rsmContas.getInt("cd_conta_receber"), connect);
				HashMap<Integer, Float> registroCentroCusto = new HashMap<Integer, Float>();
				while(rsmCategoria.next()){
					if(!registroCentroCusto.containsKey(rsmCategoria.getInt("cd_centro_custo"))){
						registroCentroCusto.put(new Integer(rsmCategoria.getInt("cd_centro_custo")), new Float(0));
					}
					
					registroCentroCusto.put(new Integer(rsmCategoria.getInt("cd_centro_custo")), registroCentroCusto.get(new Integer(rsmCategoria.getInt("cd_centro_custo"))) + rsmCategoria.getFloat("vl_conta_categoria"));
				}
				rsmCategoria.beforeFirst();
				
				String clCentroCusto = "";
				if(registroCentroCusto.keySet().size() > 1){
					for(Integer cdCentroCustoRegistro : registroCentroCusto.keySet()){
						CentroCusto centroCusto = CentroCustoDAO.get(cdCentroCustoRegistro, connect);
						clCentroCusto += (centroCusto.getIdCentroCusto() != null ? centroCusto.getIdCentroCusto() + " - " : "") + centroCusto.getNmCentroCusto() + ": " + Util.formatNumber(registroCentroCusto.get(cdCentroCustoRegistro), "#,##") + ", ";
					}
					
					clCentroCusto = clCentroCusto.substring(0, clCentroCusto.length() - 2);
				}
				else{
					for(Integer cdCentroCustoRegistro : registroCentroCusto.keySet()){
						CentroCusto centroCusto = CentroCustoDAO.get(cdCentroCustoRegistro, connect);
						clCentroCusto = (centroCusto.getIdCentroCusto() != null ? centroCusto.getIdCentroCusto() + " - " : "") + centroCusto.getNmCentroCusto();
					}
				}
				rsmContas.setValueToField("CL_CENTRO_CUSTO", clCentroCusto);
				
				((ArrayList<HashMap<String, Object>>) rsm.getObject("CONTAS")).add( rsmContas.getRegister() );
			}
			
			rsm.setValueToField("VL_CONTA", vlTotal);
			rsm.setValueToField("VL_RECEBIDO", vlTotalRecebido);
			rsm.setValueToField("VL_ARECEBER", vlTotalAreceber);
			return rsm;
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

	public static ResultSetMap getAllEventosFinanceiros(int cdContaReceber) {
		return getAllEventosFinanceiros(cdContaReceber, null);
	}

	public static ResultSetMap getAllEventosFinanceiros(int cdContaReceber, Connection connect) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_conta_receber", Integer.toString(cdContaReceber), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.st_evento", Integer.toString(ContaReceberEventoServices.ST_ATIVO), ItemComparator.EQUAL, Types.INTEGER));
		return Search.find("SELECT A.*, B.id_evento_financeiro, B.nm_evento_financeiro, nm_pessoa " +
				           "FROM adm_conta_receber_evento A " +
				           "JOIN adm_evento_financeiro B ON (A.cd_evento_financeiro = B.cd_evento_financeiro) " +
				           "LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa) ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static Result get(int cdContaReceber){
		Connection connect = Conexao.conectar();
		try	{
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add( new ItemComparator("A.CD_CONTA_RECEBER", String.valueOf( cdContaReceber ), ItemComparator.EQUAL, Types.INTEGER) );
			ResultSetMap rsm = findParcelas( criterios );
			if( rsm.next() )
				return new Result(1, "Conta Recuperada com Sucesso", "CONTARECEBER", rsm.getRegister());
			else
				return new Result( -1, "Conta nÃ£o encontrada", "CONTARECEBER", null);
				
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAsResultSet(int cdContaReceber) {
		return getAsResultSet(cdContaReceber, null);
	}

	public static ResultSetMap getAsResultSet(int cdContaReceber, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();
//			return new ResultSetMap(connect.prepareStatement(
//				   "SELECT A.*, A.cd_titulo_credito AS cd_titulo_credito_conta_receber, J.nm_razao_social AS nm_empresa, J.nr_cnpj AS nr_cnpj_empresa, " +
//		           "       K.nm_pessoa AS nm_fantasia, C.nm_pessoa, " +
//				   "       C2.nm_razao_social, D.nr_conta, D.nr_dv, D.nm_conta, D.tp_operacao, E.nr_agencia, " +
//				   "       F.nr_banco, F.nm_banco, G.nr_carteira, G.nm_carteira, G.sg_carteira, G.tp_digito, " +
//				   "       G.nr_digitos_inicio, G.qt_digitos_numero, G.nm_moeda, G.nm_aceite, G.nr_cedente, " +
//				   "       G.nm_local_pagamento, G.txt_mensagem, G.txt_campo_livre, G.pr_juros AS pr_juros_carteira, " +
//				   "       G.pr_multa AS pr_multa_carteira, G.nr_base_inicial, G.nr_base_final, G.nr_convenio, G.nr_servico, " +
//				   "       C1.nr_cpf, C2.nr_cnpj, " +
//				   "       H.nm_tipo_documento, H.sg_tipo_documento, " +
//				   "       I.cd_titulo_credito AS cd_titulo_credito_trabalho, I.cd_instituicao_financeira, I.cd_alinea, I.nr_documento_emissor," +
//				   "       I.tp_documento_emissor, I.nm_emissor, I.tp_emissao, I.nr_agencia AS nr_agencia_titulo_credito," +
//				   "       I.st_titulo, I.ds_observacao, I.dt_credito, I.tp_circulacao, I.cd_conta AS cd_conta_titulo_credito, " +
//				   "       L.nr_banco AS nr_instituicao_financeira, L.nm_banco AS nm_instituicao_financeira  "+
//		           "FROM adm_conta_receber A " +
//		           "LEFT OUTER JOIN grl_empresa          B ON (A.cd_empresa = B.cd_empresa) " +
//		           "LEFT OUTER JOIN grl_pessoa_juridica  J ON (B.cd_empresa  = J.cd_pessoa) " +
//		           "LEFT OUTER JOIN grl_pessoa           K ON (J.cd_pessoa  = K.cd_pessoa) " +
//		           "LEFT OUTER JOIN grl_pessoa           C ON (A.cd_pessoa  = C.cd_pessoa) "+
//		           "LEFT OUTER JOIN grl_pessoa_fisica   C1 ON (A.cd_pessoa = C1.cd_pessoa) "+
//		           "LEFT OUTER JOIN grl_pessoa_juridica C2 ON (A.cd_pessoa = C2.cd_pessoa) "+
//		           "LEFT OUTER JOIN adm_conta_financeira D ON (A.cd_conta = D.cd_conta) " +
//		           "LEFT OUTER JOIN grl_agencia          E ON (D.cd_agencia = E.cd_agencia)" +
//		           "LEFT OUTER JOIN grl_banco            F ON (E.cd_banco = F.cd_banco) "+
//		           "LEFT OUTER JOIN adm_conta_carteira   G ON (A.cd_conta          = G.cd_conta " +
//		           "                                       AND A.cd_conta_carteira = G.cd_conta_carteira) " +
//		           "LEFT OUTER JOIN adm_tipo_documento   H ON (A.cd_tipo_documento = H.cd_tipo_documento) " +
//		           "LEFT OUTER JOIN adm_titulo_credito   I ON (A.cd_conta_receber = I.cd_conta_receber) "+
//		           "LEFT OUTER JOIN grl_banco            L ON (I.cd_instituicao_financeira = L.cd_banco) " +
//		           "WHERE A.cd_conta_receber = " +cdContaReceber+
//		           " ORDER BY A.dt_vencimento").executeQuery());
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_conta_receber", "" + cdContaReceber, ItemComparator.EQUAL, Types.INTEGER));
			return find(criterios);
			
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

	public static Result insert(ContaReceber objeto, TituloCredito tituloCredito, boolean ignorarDuplicidade) {
		return insert(objeto, tituloCredito, ignorarDuplicidade, (Connection)null);
	}

	public static Result insert(ContaReceber objeto, TituloCredito tituloCredito, boolean ignorarDuplicidade, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			/*
			 *  Criando categoria default
			 */
			String idParam = objeto.getCdDocumentoSaida()>0 ? "CD_CATEGORIA_SAIDAS_DEFAULT" : "CD_CATEGORIA_RECEITAS_DEFAULT";
			int cdCategoriaDefault = ParametroServices.getValorOfParametroAsInteger(idParam, 0, objeto.getCdEmpresa(), connection);
			ArrayList<ContaReceberCategoria> categorias = new ArrayList<ContaReceberCategoria>();
			if (cdCategoriaDefault > 0)
				categorias.add(new ContaReceberCategoria(0, cdCategoriaDefault, objeto.getVlConta() - objeto.getVlAbatimento() + objeto.getVlAcrescimo(), 0));

			if(objeto.getStConta()==99)
				objeto.setStConta(ST_EM_ABERTO);
			Result ret = insert(objeto, categorias, tituloCredito, ignorarDuplicidade, connection);
			if (ret.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return ret;
			}

			if (isConnectionNull)
				connection.commit();

			return ret;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar incluir conta a receber!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result insert(ContaReceber objeto, boolean ignorarDuplicidade, ArrayList<HashMap<String,Object>> registros) {
		return insert(objeto, null, null, ignorarDuplicidade, registros, null);
	}
	
	public static Result insert(ContaReceber objeto, TituloCredito titulo, boolean ignorarDuplicidade, ArrayList<HashMap<String,Object>> registros) {
		return insert(objeto, null, titulo, ignorarDuplicidade, registros, null);
	}
	
	public static Result insert(ContaReceber objeto, ArrayList<ContaReceberCategoria> categorias, boolean ignorarDuplicidade, ArrayList<HashMap<String,Object>> registros) {
		return insert(objeto, categorias, null, ignorarDuplicidade, registros, null);
	}

	public static Result insert(ContaReceber objeto, ArrayList<ContaReceberCategoria> categorias, TituloCredito titulo, boolean ignorarDuplicidade, ArrayList<HashMap<String,Object>> registros, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			Result resultado = insert(objeto, categorias, titulo, ignorarDuplicidade, connection);
			int codContaReceber = resultado.getCode();
			if(codContaReceber <= 0){
				return new Result(-1, "Erro: " + resultado.getMessage());
			}
			Result retorno = refaturarContas(registros, codContaReceber, connection);
			if (isConnectionNull)
				connection.commit();
			return retorno;
		
		}
		catch(Exception e){
			if (isConnectionNull)
				Conexao.rollback(connection);
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir a conta a receber!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
		
	}
	
	public static Result insert(ContaReceber objeto, TituloCredito titulo, boolean ignorarDuplicidade, int cdContaPagar, int cdContaBancaria, boolean lgPermitirBloqueado) {
		return insert(objeto, titulo, ignorarDuplicidade, cdContaPagar, cdContaBancaria, lgPermitirBloqueado, null);
	}
	
	public static Result insert(ContaReceber objeto, TituloCredito titulo, boolean ignorarDuplicidade, int cdContaPagar, int cdContaBancaria) {
		return insert(objeto, titulo, ignorarDuplicidade, cdContaPagar, cdContaBancaria, false, null);
	}
	
	public static Result insert(ContaReceber objeto, TituloCredito titulo, boolean ignorarDuplicidade, int cdContaPagar, int cdContaBancaria, Connection connection) {
		return insert(objeto, titulo, ignorarDuplicidade, cdContaPagar, cdContaBancaria, false, connection);
	}
	
	public static Result insert(ContaReceber objeto, TituloCredito titulo, boolean ignorarDuplicidade, int cdContaPagar, int cdContaBancaria, boolean lgPermitirBloqueado, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			//Incluir Categoria
			int cdCategoriaEconomica = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_CHEQUE_A_PRAZO", 0, objeto.getCdEmpresa(), connection);
			if(cdCategoriaEconomica <= 0){
				Conexao.rollback(connection);
				return new Result(-1, "Parametro para cheques a prazo do modulo de factoring, nÃ£o configurado!");
			}
			int cdCentroCusto = 0;
			ArrayList<ContaReceberCategoria> categorias = new ArrayList<ContaReceberCategoria>();
			ContaReceberCategoria contaCat = new ContaReceberCategoria(objeto.getCdContaReceber(), cdCategoriaEconomica, objeto.getVlConta(), cdCentroCusto);
			categorias.add(contaCat);
			
			ContaPagar contaPagar = ContaPagarDAO.get(cdContaPagar, connection);
			if(contaPagar == null){
				Conexao.rollback(connection);
				return new Result(-1, "Nenhuma conta a pagar foi carregada!");
			}
			
			Pessoa pessoa = PessoaDAO.get(contaPagar.getCdPessoa(), connection);
			if(pessoa == null){
				Conexao.rollback(connection);
				return new Result(-1, "Nenhuma pessoa foi carregada!");
			}
			Cliente cliente = ClienteDAO.get(objeto.getCdEmpresa(), pessoa.getCdPessoa(), connection);
			if(cliente == null){
				Conexao.rollback(connection);
				return new Result(-1, "Nenhum cliente foi carregado!");
			}
			
			//Incluir Adm_Conta_Factoring
			int qtDias = Util.getQuantidadeDiasUteis(objeto.getDtVencimento(), Util.getDataAtual(), connection);
			Double prJuros = new Double(cliente.getPrTaxaPadraoFactoring());
			if(prJuros == 0)
				prJuros = new Double(ParametroServices.getValorOfParametroAsFloat("VL_TAXA_PADRAO", 0, objeto.getCdEmpresa(), connection));
			if(prJuros == 0){
				Conexao.rollback(connection);
				return new Result(-1, "Parametro de Valor de Taxa PadrÃ£o nÃ£o configurado!");
			}
			Double vlCheque = objeto.getVlConta();
			Double vlDesconto = (vlCheque * prJuros / 100) / 30 * qtDias;
			//Incluir conta a receber
			Result resultado = insert(objeto, categorias, titulo, ignorarDuplicidade, connection);
			int codContaReceber = resultado.getCode();
			if(codContaReceber <= 0){
				Conexao.rollback(connection);
				return new Result(-1, "Erro ao inserir Conta a Receber: " + resultado.getMessage());
			}
//			TituloCredito tituloCredito = (TituloCredito) resultado.getObjects().get("titulo");
//			ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber, connection);
//			contaReceber.setCd
			//Incluir conta factoring
			ContaFactoring contaFac = new ContaFactoring(0, cdContaPagar, objeto.getCdContaReceber(), qtDias, prJuros.floatValue(), vlDesconto.floatValue());
			if(ContaFactoringDAO.insert(contaFac, connection) <= 0){
				Conexao.rollback(connection);
				return new Result(-1, "Erro ao relacionar Conta a Receber com Conta a Pagar!");
			}
			
			//Atualiza os valores de desconto e conta de Conta Pagar
			if(ContaFactoringServices.atualizarValoresContaPagar(cdContaPagar, connection).getCode() <= 0){
				Conexao.rollback(connection);
				return new Result(-1, "Erro ao atualizar Conta Pagar!");
			}
			
			//Valida a nota conta a receber para saber se ela nao ultrapssou nenhum limite configurado
			Result resultVerificacao = ContaFactoringServices.verificarPagamento(cdContaPagar, objeto.getCdContaReceber(), cdContaBancaria, lgPermitirBloqueado, connection);
			
			if(resultVerificacao.getCode() == -1){
				Conexao.rollback(connection);
				return resultVerificacao;
			}
			
			if(isConnectionNull)
				connection.commit();
			
			return new Result(1, "Cheque cadastrado com sucesso!");
		
		}
		catch(Exception e){
			if (isConnectionNull)
				Conexao.rollback(connection);
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir a conta a receber!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
		
	}
	
	public static Result insert(ContaReceber objeto, ArrayList<ContaReceberCategoria> categorias, boolean ignorarDuplicidade) {
		return insert(objeto, categorias, null, ignorarDuplicidade, null);
	}

	public static Result insert(ContaReceber objeto, ArrayList<ContaReceberCategoria> categorias, TituloCredito tituloCredito, boolean ignorarDuplicidade) {
		return insert(objeto, categorias, tituloCredito, ignorarDuplicidade, null);
	}
	
	public static Result insert(ContaReceber objeto, ArrayList<ContaReceberCategoria> categorias, TituloCredito tituloCredito, boolean ignorarDuplicidade, Connection connection) {
		return insert(objeto, categorias, tituloCredito, ignorarDuplicidade, (AuthData)null, connection);
	}

	public static Result insert(ContaReceber objeto, ArrayList<ContaReceberCategoria> categorias, TituloCredito tituloCredito, boolean ignorarDuplicidade, 
			AuthData authData, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			if(!ignorarDuplicidade)	{
				String sql = "SELECT * FROM adm_conta_receber " +
	                   			  "WHERE cd_empresa = "+objeto.getCdEmpresa()+
	                   			  "  AND cd_pessoa  = "+objeto.getCdPessoa()+
	                   			  "  AND vl_conta   = "+objeto.getVlConta()+
	                   			  (nrDocumentoIsNotNull(objeto) ? "  AND nr_documento " + 
	                   					  (Util.getConfManager().getIdOfDbUsed().equals("FB") ? " like " : " ilike ") +
	                   					  " '"+ objeto.getNrDocumento() + "'" : "" )+
	                   			  "  AND dt_vencimento = ?";
				PreparedStatement pstmt = connection.prepareStatement(sql);
				pstmt.setTimestamp(1, new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
				
				if(pstmt.executeQuery().next())	{
					com.tivic.manager.util.Util.registerLog(new Exception("Conta a receber duplicada!"));
					return new Result(-23, "Erro ao tentar incluir conta: Conta duplicada!", new Exception("Conta a receber duplicada!"));
				}

			}
			/*
			 *  Criando registro da conta para enviar no arquivo e gerando nosso numero
			 */
			boolean registrarCobranca = gerarNossoNumeroAndRegistro(objeto, connection);
			/*
			 *  INCLUINDO
			 */
			objeto.setDtDigitacao(new GregorianCalendar());
			objeto.setDtVencimentoOriginal(objeto.getDtVencimento());
			objeto.setCdContaReceber(ContaReceberDAO.insert(objeto, connection));
			if (objeto.getCdContaReceber() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				com.tivic.manager.util.Util.registerLog(new Exception("Erro ao incluir conta a receber!"));
				return new Result(-10, "Erro ao incluir conta a receber!");
			}
			//COMPLIANCE
			ComplianceManager.process(objeto, authData, ComplianceManager.TP_ACAO_INSERT, connection);
			
			/*
			 *  Gravando categorias
			 */
			for(int i=0; categorias!=null && i<categorias.size(); i++)	{
				categorias.get(i).setCdContaReceber(objeto.getCdContaReceber());
				int ret = ContaReceberCategoriaDAO.insert((ContaReceberCategoria)categorias.get(i), connection);
				if (ret <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					com.tivic.manager.util.Util.registerLog(new Exception("Conta a receber nÃ£o classificada corretamente! ERRO: "+ret));
					return new Result(-20, "NÃ£o foi possÃ­vel gravar classificaï¿½ï¿½o da conta! [ERRO: "+ret+"]");
				}
			}
			/*
			 *  Gerando repeticoes
			 */
			if(objeto.getTpFrequencia()>UNICA_VEZ)	{
				Result result = gerarParcelasOutraConta(objeto, connection);
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					com.tivic.manager.util.Util.registerLog(new Exception("Erro ao tentar gerar outras parcelas!"));
					return result;
				}
			}
			/*
			 *  Registrando cobranca
			 */
			if(registrarCobranca) {
				if (registrarCobranca(objeto, false, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					Exception e = new Exception("Erro ao registrar cobranÃ§a!");
					com.tivic.manager.util.Util.registerLog(e);
					e.printStackTrace(System.out);
					return new Result(-40, e.getMessage(), e);
				}
			}
			/*
			 *  Criando titulo de credito
			 */
			if(tituloCredito!=null)	{
				tituloCredito.setCdConta(objeto.getCdConta());
				tituloCredito.setCdContaReceber(objeto.getCdContaReceber());
				int cdTituloCredito = 0;
				if((cdTituloCredito = TituloCreditoServices.insert(tituloCredito, connection)) <= 0)	{
					if (isConnectionNull)
						Conexao.rollback(connection);
					Exception e = new Exception("Erro ao gravar tï¿½tulo de crï¿½dito!");
					com.tivic.manager.util.Util.registerLog(e);
					e.printStackTrace(System.out);
					return new Result(-50, e.getMessage(), e);
				}
				tituloCredito.setCdTituloCredito(cdTituloCredito);
				if(ContaReceberDAO.update(objeto, connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao atualizar título de crédito");
				}
			}
			
			if (isConnectionNull)
				connection.commit();

			Result result = new Result(objeto.getCdContaReceber(), "Conta a receber incluida com sucesso!",
                    					"rsm", getAsResultSet(objeto.getCdContaReceber()));
			
			result.addObject("titulo", tituloCredito);
			
			return result;
		}
		catch(Exception e){
			if (isConnectionNull)
				Conexao.rollback(connection);
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir a conta a receber!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	/**
	 * Verifica se o nrDocumento Ã© nulo, vazio ou preenchido so com espacos em branco
	 * 
	 * @param objeto conta a ser verificada
	 * @return retorna true se o nrDocumento estiver preenchido corretamente, e false caso contrario
	 * @author Luiz Romario Filho
	 * @since 29/08/2014
	 */
	private static boolean nrDocumentoIsNotNull(ContaReceber objeto) {
		return objeto.getNrDocumento() != null && !"".equalsIgnoreCase(objeto.getNrDocumento().trim());
	}

	/*
	 * @category Factoring
	 */
	public static Result update(ContaReceber objeto, TituloCredito titulo, int cdContaPagar, int cdContaBancaria, boolean lgAutorizarBloqueado) {
		return update(objeto, titulo, cdContaPagar, cdContaBancaria, lgAutorizarBloqueado, null);
	}
	
	public static Result update(ContaReceber objeto, TituloCredito titulo, int cdContaPagar, int cdContaBancaria) {
		return update(objeto, titulo, cdContaPagar, cdContaBancaria, false, null);
	}
	
	public static Result update(ContaReceber objeto, TituloCredito titulo, int cdContaPagar, int cdContaBancaria, Connection connection) {
		return update(objeto, titulo, cdContaPagar, cdContaBancaria, false, connection);
	}
	
	public static Result update(ContaReceber objeto, TituloCredito titulo, int cdContaPagar, int cdContaBancaria, boolean lgAutorizarBloqueado, Connection connection) {
		return update(objeto, titulo, cdContaPagar, cdContaBancaria, lgAutorizarBloqueado, null, connection);
	}
	
	public static Result update(ContaReceber objeto, TituloCredito titulo, int cdContaPagar, int cdContaBancaria, boolean lgAutorizarBloqueado, 
			AuthData authData, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			int qtDias       = Util.getQuantidadeDiasUteis(objeto.getDtVencimento(), Util.getDataAtual(), connection);
			Double prJuros    = new Double(ParametroServices.getValorOfParametroAsFloat("VL_TAXA_PADRAO", 0, objeto.getCdEmpresa(), connection));
			Double vlCheque   = objeto.getVlConta();
			Double vlDesconto =  (vlCheque * prJuros / 100) / 30 * qtDias;
			objeto.setVlAbatimento(vlDesconto);
			
			ArrayList<ContaReceberCategoria> categorias = new ArrayList<ContaReceberCategoria>();
			ResultSetMap rsmCats = ContaReceberCategoriaServices.getCategoriaOfContaReceber(objeto.getCdContaReceber(), connection);
			if(rsmCats.next() && rsmCats.size()==1) {
				for(int i = 0; i < categorias.size(); i++){
					if(ContaReceberCategoriaDAO.delete(rsmCats.getInt("cd_conta_receber"), rsmCats.getInt("cd_categoria_economica"), connection) <= 0){
						Conexao.rollback(connection);
						return new Result(-1, "Erro ao limpar categorias economicas da conta!");
					}
				}
			}
			int cdCategoriaEconomica = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_CHEQUE_A_PRAZO", 0, objeto.getCdEmpresa(), connection);
			categorias.add(new ContaReceberCategoria(objeto.getCdContaReceber(), cdCategoriaEconomica, objeto.getVlConta(), 0));

			
			//Atualiza ContaReceber
			Result resultado    = update(objeto, categorias, titulo, connection);
			int codContaReceber = resultado.getCode();
			if(codContaReceber <= 0)	{
				Conexao.rollback(connection);
				return new Result(-1, "Erro ao inserir Conta a Receber: " + resultado.getMessage());
			}
			
			//Atualizar Adm_Conta_Factoring
			//Procurar conta Factoring levando em consideracao que uma conta a receber so pode participar de um Factorig
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_conta_receber_antecipada", Integer.toString(objeto.getCdContaReceber()), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_conta_pagar", Integer.toString(cdContaPagar), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmContaFac = ContaFactoringDAO.find(criterios, connection);
			int cdContaFactoring = 0;
			if(rsmContaFac.next()){
				cdContaFactoring = rsmContaFac.getInt("cd_conta_factoring");
			}
			if(cdContaFactoring == 0){
				Conexao.rollback(connection);
				new Result(-1, "Erro ao buscar conta factoring!");
			}
			ContaFactoring contaFac = ContaFactoringDAO.get(cdContaFactoring, connection);
			contaFac.setPrJuros(prJuros.floatValue());
			contaFac.setVlDesconto(vlDesconto.floatValue());
			contaFac.setQtDias(qtDias);
			if(ContaFactoringDAO.update(contaFac, connection) <= 0){
				Conexao.rollback(connection);
				return new Result(-1, "Erro atualizar relacionamento Conta a Receber com Conta a Pagar: " + resultado.getMessage());
			}
			
			//Atualiza os valores de desconto e conta de Conta Pagar
			if(ContaFactoringServices.atualizarValoresContaPagar(cdContaPagar, connection).getCode() <= 0){
				Conexao.rollback(connection);
				return new Result(-1, "Erro ao atualizar Conta Pagar!");
			}
			
			//Valida a nota conta a receber para saber se ela nao ultrapssou nenhum limite configurado
			Result resultVerificacao = ContaFactoringServices.verificarPagamento(cdContaPagar, objeto.getCdContaReceber(), cdContaBancaria, lgAutorizarBloqueado, connection);
			
			if(resultVerificacao.getCode() == -1){
				Conexao.rollback(connection);
				return resultVerificacao;
			}
			

			//COMPLIANCE
			ComplianceManager.process(objeto, authData, ComplianceManager.TP_ACAO_UPDATE, connection);
			
			if(isConnectionNull)
				connection.commit();
			
			return new Result(1, "Cheque atualizado com sucesso!");
		
		}
		catch(Exception e){
			if (isConnectionNull)
				Conexao.rollback(connection);
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar atualizar a conta a receber!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
		
	}
	
	
	/*
	 * @category PDV
	 * Usado pelo PDV por favor nao alterar
	 */
	public static Result update(ContaReceber objeto, TituloCredito tituloCredito) {
		Connection connect = Conexao.conectar();
		try	{
			ArrayList<ContaReceberCategoria> categorias = new ArrayList<ContaReceberCategoria>();
			ResultSetMap rsmCats = ContaReceberCategoriaServices.getCategoriaOfContaReceber(objeto.getCdContaReceber(), connect);
			if(rsmCats.next() && rsmCats.size()==1) {
				categorias.add(new ContaReceberCategoria(objeto.getCdContaReceber(), rsmCats.getInt("cd_categoria_economica"),
			                                             objeto.getVlConta(), rsmCats.getInt("cd_centro_custo")));
			}
			//
			return update(objeto, categorias, tituloCredito, null);
		}
		catch(Exception e){
			Conexao.rollback(connect);
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar atualizar a conta a receber!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static Result update(ContaReceber objeto, ArrayList<ContaReceberCategoria> categorias, TituloCredito tituloCredito) {
		return update(objeto, categorias, tituloCredito, null);
	}

	public static Result update(ContaReceber objeto, ArrayList<ContaReceberCategoria> categorias, TituloCredito tituloCredito, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			/*
			 *  Criando registro da conta para enviar no arquivo e gerando nosso numero
			 */
			boolean registrarCobranca = gerarNossoNumeroAndRegistro(objeto, connection);
			/*
			 * Tentando atualizar
			 */
			if(objeto.getStConta()==99)
				objeto.setStConta(ST_EM_ABERTO);
			int r = ContaReceberDAO.update(objeto, connection);
			if (r <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao atualizar Conta a Receber!");
			}
			/*
			 * Gerando repeticoes
			 */
			if(objeto.getTpFrequencia()>UNICA_VEZ)	{
				Result result = gerarParcelasOutraConta(objeto, connection);
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return result;
				}
			}
			/*
			 * Registrando cobranca
			 */
			if(registrarCobranca)
				if (registrarCobranca(objeto, true, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					Exception e = new Exception("Erro ao registrar a cobranca da Conta a Receber!");
					com.tivic.manager.util.Util.registerLog(e);
					return new Result(-1, e.getMessage(), e);
				}
			/*
			 *  Gravando categorias
			 */
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM adm_conta_receber_categoria " +
					                                              "WHERE cd_conta_receber       = "+objeto.getCdContaReceber()+
					                                              "  AND cd_categoria_economica = ?");
			for(int i=0; categorias!=null && i<categorias.size(); i++)	{
				int ret = 1;
				categorias.get(i).setCdContaReceber(objeto.getCdContaReceber());
				pstmt.setInt(1, categorias.get(i).getCdCategoriaEconomica());
				//Se houver apenas uma categoria economica a ser cadastrada, significa que sera automaticamente substituida
				if(categorias.size() > 1){
					if(pstmt.executeQuery().next())
						ret = ContaReceberCategoriaDAO.update(categorias.get(i), connection);
					else
						ret = ContaReceberCategoriaDAO.insert(categorias.get(i), connection);
				}
				else{
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_conta_receber", Integer.toString(categorias.get(i).getCdContaReceber()), ItemComparator.EQUAL, Types.INTEGER));
					
					ResultSetMap rsm = ContaReceberCategoriaDAO.find(criterios, connection);
					rsm.next();
					
					if(ContaReceberCategoriaDAO.delete(categorias.get(i).getCdContaReceber(), rsm.getInt("cd_categoria_economica"), connection) <= 0){
						if (isConnectionNull)
							Conexao.rollback(connection);
						Exception e = new Exception("Erro ao deletar a conta de categoria da Conta a Receber!");
						com.tivic.manager.util.Util.registerLog(e);
						pstmt.close();
						return new Result(-1, e.getMessage(), e);
					}
					
					ret = ContaReceberCategoriaDAO.insert(categorias.get(i), connection);
				}
				if (ret <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					Exception e = new Exception("Conta a receber nÃ£o classificada corretamente! ERRO: "+ret);
					com.tivic.manager.util.Util.registerLog(e);
					pstmt.close();
					return new Result(-20, e.getMessage(), e);
				}
			}
			/*
			 *  Criando titulo de credito
			 */
			if(tituloCredito!=null)	{
				tituloCredito.setDtVencimento(objeto.getDtVencimento());
				tituloCredito.setCdConta(objeto.getCdConta());
				tituloCredito.setCdContaReceber(objeto.getCdContaReceber());
				int ret = 0;
				if(tituloCredito.getCdTituloCredito()>0) {
					ret = TituloCreditoServices.update(tituloCredito, connection);
				}
				else	{
					ResultSet rs = connection.prepareStatement("SELECT * " +
							"FROM adm_titulo_credito " +
							"WHERE cd_conta_receber = "+objeto.getCdContaReceber()).executeQuery();
					
					if(rs.next())	{
						tituloCredito.setCdTituloCredito(rs.getInt("cd_titulo_credito"));
						ret = TituloCreditoServices.update(tituloCredito, connection);
					}
					else
						ret = TituloCreditoServices.insert(tituloCredito, connection);
				}

				if(ret<=0)	{
					if (isConnectionNull)
						Conexao.rollback(connection);
					Exception e = new Exception("Erro ao gravar tï¿½tulo de crï¿½dito!");
					com.tivic.manager.util.Util.registerLog(e);
					return new Result(-50, e.getMessage(), e);
				}
				if(ContaReceberDAO.update(objeto, connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao atualizar titulo de crï¿½dito");
				}
			}

			if (isConnectionNull) {
				connection.commit();
			}

			return new Result(r, "Conta a receber atualizada com sucesso!", "rsm", getAsResultSet(objeto.getCdContaReceber()));
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao atualizar a conta a receber!", e);
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	private static int registrarCobranca(ContaReceber conta, boolean verificaExistencia, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			// Verificando existencia de registro
			String prefix = ArquivoTipoRegistroServices.prefixCnab240R;
			if(verificaExistencia)	{
				ResultSetMap rsm = ArquivoRegistroServices.getRegistrosOfContaReceber(conta.getCdContaReceber(), connect);
				while(rsm.next())
					if((prefix+ArquivoTipoRegistroServices.cnab240R[0][0]).equals(rsm.getString("id_tipo_registro"))) {
						if (isConnectionNull)
							connect.commit();
						return 1;
					}
			}
			// Verifica existencia
			ArquivoTipoRegistro tipo = ArquivoTipoRegistroServices.getTipoRegistroById(prefix, "01", ArquivoTipoRegistroServices.cnab240R, connect);
			//
			ArquivoRegistro registro = new ArquivoRegistro(0, 0, conta.getCdContaReceber(),
														   0, tipo.getCdTipoRegistro(), 0);
			int code = ArquivoRegistroDAO.insert(registro, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberServices.registrarCobranca: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}
	/**
	 * metodo utilizado pelo pdv
	 * @param criterios
	 * @param connection
	 * @return
	 */
	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connection) {
		boolean isConnectionNull = connection==null;
		boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
		if(!lgBaseAntiga)
			try{
				int notNegociacao = 0;
				int cdConvenio 	  = 0;
				int qtLimite      = 0;
				int isFactoring	  = 0;
				int cdData   	  = 0;
				int isPDV         = 0;
				String complemento = "";
				String ordenacao = "ASC";
				Boolean orderByPrioritaria = false;
				for (int i=0; criterios!=null && i<criterios.size(); i++)
					if (criterios.get(i).getColumn().equalsIgnoreCase("notNegociacao")) {
						notNegociacao = Integer.parseInt(criterios.get(i).getValue());
						criterios.remove(i);
						i--;
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("cdConvenio")) {
						cdConvenio = Integer.parseInt(criterios.get(i).getValue());
						criterios.remove(i);
						i--;
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
						qtLimite = Integer.parseInt(criterios.get(i).getValue());
						criterios.remove(i);
						i--;
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("st_conta") && 
							Integer.parseInt(criterios.get(i).getValue()) == -1) {
						criterios.remove(i);
						i--;
						continue;
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("isFactoring")) {
						isFactoring = Integer.parseInt(criterios.get(i).getValue());
						criterios.remove(i);
						i--;
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("A.dt_vencimento_final") ) {
						criterios.get(i).setColumn("A.dt_vencimento");
						criterios.get(i).setValue( criterios.get(i).getValue().substring(0, 10)+" 23:59:59"  );
						continue;
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("A.dt_emissao_final") ) {
						criterios.get(i).setColumn("A.dt_emissao");
						criterios.get(i).setValue( criterios.get(i).getValue().substring(0, 10)+" 23:59:59"  );
						continue;
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("COMPLEMENTO")) {
						complemento = criterios.get(i).getValue();
						criterios.remove(i);
						i--;
					}else if (criterios.get(i).getColumn().equalsIgnoreCase("VENCIDAS")) {
						cdData = Integer.parseInt(criterios.get(i).getValue());
						criterios.remove(i);
						i--;
					}else if (criterios.get(i).getColumn().equalsIgnoreCase("isPDV")) {
						isPDV = Integer.parseInt(criterios.get(i).getValue());
						criterios.remove(i);
						i--;
					}else if (criterios.get(i).getColumn().equalsIgnoreCase("ordenacao")) {
						ordenacao = criterios.get(i).getValue();
						criterios.remove(i);
						i--;
						if( ordenacao.equals("ASC") || !ordenacao.equals("DESC")  )
							ordenacao = "ASC";
					}else if (criterios.get(i).getColumn().equalsIgnoreCase("orderbyprioritaria")) {
						orderByPrioritaria = true;
						criterios.remove(i);
						i--;
						
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
				//			
				ResultSetMap rsm = Search.find(
						   "SELECT "+limitSkip[0]+" DISTINCT (A.cd_conta_receber), A.*, A.cd_conta AS cd_conta, J.nm_razao_social AS nm_empresa, J.nr_cnpj AS nr_cnpj_empresa, " +
						   "	   A2.nm_pessoa AS nm_usuario, " +
						   "	   A3.nm_logradouro, A3.nr_cep, A3.nr_endereco," +
				           "       K.nm_pessoa AS nm_fantasia, C.nm_pessoa, " +
						   "       C2.nm_razao_social, D.nr_conta, D.nr_dv, D.nm_conta, D.tp_operacao, E.nr_agencia, " +
						   "       F.nr_banco, F.nm_banco, G.nr_carteira, G.nm_carteira, G.sg_carteira, G.tp_digito, " +
						   "       G.nr_digitos_inicio, G.qt_digitos_numero, G.nm_moeda, G.nm_aceite, G.nr_cedente, " +
						   "       G.nm_local_pagamento, G.txt_mensagem, G.txt_campo_livre, G.pr_juros AS pr_juros_carteira, " +
						   "       G.pr_multa AS pr_multa_carteira, G.nr_base_inicial, G.nr_base_final, G.nr_convenio, G.nr_servico, " +
						   "       C1.nr_cpf, C2.nr_cnpj, C.nr_telefone1, C.nr_telefone2, C.nr_celular, " +
						   "       H.nm_tipo_documento, H.sg_tipo_documento, " +
			    ((isPDV == 1) ? "       I.cd_titulo_credito, I.cd_instituicao_financeira, I.cd_alinea, I.nr_documento_emissor, " +
						   		"       I.tp_documento_emissor, I.nm_emissor, I.tp_emissao, I.nr_agencia AS nr_agencia_titulo_credito, " +
						   		"       I.st_titulo, I.ds_observacao, I.dt_credito, I.tp_circulacao, I.cd_conta AS cd_conta_titulo_credito, " +
						   		"       L.nr_banco AS nr_instituicao_financeira, L.nm_banco AS nm_instituicao_financeira, " : "") +
						   "	   PG.vl_pagamento AS vl_plano_pagto "+
				((isFactoring == 1) ? ", CPP.nm_pessoa AS nm_cliente " : "") +
	//						" , PRC.nr_processo " +
				           "FROM adm_conta_receber A " +
				           "LEFT OUTER JOIN seg_usuario A1 ON (A.cd_usuario = A1.cd_usuario) "+
				           "LEFT OUTER JOIN grl_pessoa A2 ON (A1.cd_pessoa = A2.cd_pessoa) "+
				           "LEFT OUTER JOIN grl_pessoa_endereco A3 ON (A1.cd_pessoa = A3.cd_pessoa) "+
				           "LEFT OUTER JOIN grl_empresa          B ON (A.cd_empresa = B.cd_empresa) " +
				           "LEFT OUTER JOIN grl_pessoa_juridica  J ON (B.cd_empresa  = J.cd_pessoa) " +
				           "LEFT OUTER JOIN grl_pessoa           K ON (J.cd_pessoa  = K.cd_pessoa) " +
				           "LEFT OUTER JOIN grl_pessoa           C ON (A.cd_pessoa  = C.cd_pessoa) "+
				           "LEFT OUTER JOIN grl_pessoa_fisica   C1 ON (A.cd_pessoa = C1.cd_pessoa) "+
				           "LEFT OUTER JOIN grl_pessoa_juridica C2 ON (A.cd_pessoa = C2.cd_pessoa) "+
				           "LEFT OUTER JOIN adm_conta_financeira D ON (A.cd_conta = D.cd_conta) " +
				           "LEFT OUTER JOIN grl_agencia          E ON (D.cd_agencia = E.cd_agencia) " +
				           "LEFT OUTER JOIN grl_banco            F ON (E.cd_banco = F.cd_banco) "+
				           "LEFT OUTER JOIN adm_conta_carteira   G ON (A.cd_conta          = G.cd_conta " +
				           "                                       AND A.cd_conta_carteira = G.cd_conta_carteira) " +
				           "LEFT OUTER JOIN adm_tipo_documento   H ON (A.cd_tipo_documento = H.cd_tipo_documento) " +
				((isPDV == 1) ? "LEFT OUTER JOIN adm_titulo_credito   I ON (A.cd_conta_receber = I.cd_conta_receber) "+
				           		"LEFT OUTER JOIN grl_banco            L ON (I.cd_instituicao_financeira = L.cd_banco) " : "") +
				           "LEFT OUTER JOIN adm_plano_pagto_documento_saida      PG ON (A.cd_documento_saida = PG.cd_documento_saida " +
				           "													    AND A.cd_plano_pagamento = PG.cd_plano_pagamento " +
				           "													    AND A.cd_forma_pagamento = PG.cd_forma_pagamento) " +
				((isFactoring == 1) ? "JOIN adm_conta_factoring  M   ON (A.cd_conta_receber = M.cd_conta_receber_antecipada) " +
						   			  "JOIN adm_conta_pagar		 CP  ON (M.cd_conta_pagar = CP.cd_conta_pagar) " +
						   			  "JOIN grl_pessoa           CPP ON (CP.cd_pessoa = CPP.cd_pessoa) " : "") +
				           (cdConvenio<=0 ? "" : "LEFT OUTER JOIN adm_contrato         M ON (A.cd_contrato = M.cd_contrato) ") +
	//			           " LEFT OUTER JOIN prc_processo_financeiro PF ON (A.cd_conta_receber = PF.cd_conta_receber) "+
	//			           " LEFT OUTER JOIN prc_processo PRC ON (PF.cd_processo = PRC.cd_processo) "+
	//tratado mais abaixo para evitar duplicidade numa conta com mais de um centro de custo
	//			           "LEFT OUTER JOIN adm_conta_receber_categoria  N   ON (A.cd_conta_receber = N.cd_conta_receber) "+
	//			           "LEFT OUTER JOIN adm_categoria_economica      O   ON (N.cd_categoria_economica = O.cd_categoria_economica) "+
	//			           "LEFT OUTER JOIN ctb_centro_custo             P   ON (N.cd_centro_custo = P.cd_centro_custo) " +
				           "WHERE 1=1 " + complemento +
				           (!nmPessoa.equals("") ?
				        	"AND TRANSLATE (C.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', " + 
				        	"				'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmPessoa)+"%' "
				        	: "") +
				           (!dsHistorico.equals("") ?
									"AND TRANSLATE (A.ds_historico, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', " + 
									"				'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(dsHistorico)+"%' "
									: "")+
				           (cdData>0?" AND A.dt_vencimento <= \'" + Util.getDataAtual().getTimeInMillis()+"\'":"")+
				           (cdConvenio<=0 ? "" : " AND (A.cd_contrato = " + cdConvenio + " OR M.cd_convenio = " + cdConvenio + ") ") +
				           (notNegociacao<=0 ? "" : " AND (A.cd_negociacao IS NULL OR A.cd_negociacao <> " + notNegociacao + ") "),
				           " ORDER BY "+
				           (( orderByPrioritaria )?" A.LG_PRIORITARIA DESC, ":"")
				           + " A.DT_VENCIMENTO "+ordenacao+" "+limitSkip[1], criterios,
				           connection==null ? Conexao.conectar() : connection, connection==null);
	
				//centro de custo
				while(rsm.next()){
					PreparedStatement pstmt;
					pstmt = connection.prepareStatement(
							"SELECT A.*,"
							+ " P.nm_centro_custo "
							+ " FROM adm_conta_receber A "
							+ " LEFT OUTER JOIN adm_conta_receber_categoria N   ON (A.cd_conta_receber = N.cd_conta_receber)"
							+ " LEFT OUTER JOIN adm_categoria_economica     O   ON (N.cd_categoria_economica = O.cd_categoria_economica)"
							+ " LEFT OUTER JOIN ctb_centro_custo            P   ON (N.cd_centro_custo = P.cd_centro_custo)"
							+ " WHERE A.cd_conta_receber=?");
					pstmt.setInt(1, rsm.getInt("cd_conta_receber"));
					rsm.setValueToField("NM_CENTRO_CUSTO", Util.join(new ResultSetMap(pstmt.executeQuery()), "nm_centro_custo"));
							
					//Buscando valores do Titulo de Credito vinculado a Conta a Receber
					ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
					crt.add(new ItemComparator("cd_conta_receber", rsm.getString("cd_conta_receber"), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmTituloCredito = TituloCreditoDAO.find(crt, connection);
					if(rsmTituloCredito.next()){
						HashMap<String, Object> register = rsmTituloCredito.getRegister();
						for(String chave : register.keySet()){
							String chaveOriginal = chave;
							if(chave.equals("NR_AGENCIA"))
								chave = "NR_AGENCIA_TITULO_CREDITO";
							if(chave.equals("DT_VENCIMENTO"))
								chave = "DT_VENCIMENTO_TITULO_CREDITO";
							if(chave.equals("NR_DOCUMENTO"))
								chave = "NR_DOCUMENTO_TITULO_CREDITO";
							if(chave.equals("CD_CONTA"))
								chave = "CD_CONTA_TITULO_CREDITO";
							rsm.setValueToField(chave, register.get(chaveOriginal));
						}
						
						//Buscando registros do banco ligado ao titulo de credito
						crt = new ArrayList<ItemComparator>();
						crt.add(new ItemComparator("cd_banco", rsm.getString("cd_instituicao_financeira"), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmBanco = BancoDAO.find(crt, connection);
						if(rsmBanco.next()){
							register = rsmBanco.getRegister();
							for(String chave : register.keySet()){
								String chaveOriginal = chave;
								if(chave.equals("NR_BANCO"))
									chave = "NR_INSTITUICAO_FINANCEIRA";
								else if(chave.equals("nm_banco"))
									chave = "NM_INSTITUICAO_FINANCEIRA";
								rsm.setValueToField(chave, register.get(chaveOriginal));
							}
						}
					}
					
					if(rsm.getInt("cd_pessoa") > 0){
						Result resultado = ClienteProgramaFaturaServices.getProgramaFaturaVigente(rsm.getInt("cd_empresa"), rsm.getInt("cd_pessoa"), connection);
						ProgramaFatura programaFatura = (ProgramaFatura) resultado.getObjects().get("programaFatura");
						if(programaFatura != null){
							//Usado pelo refaturamento para calcular o novo dia de vencimento
							GregorianCalendar dtVencimento = Util.getDataAtual();
							int qtDiasCarencia = programaFatura.getQtDiasCarencia();
							dtVencimento.add(Calendar.DAY_OF_MONTH, qtDiasCarencia);
							rsm.setValueToField("DT_VENCIMENTO_REFATURAMENTO", Util.formatDate(dtVencimento, "dd/MM/yyyy"));
						}
						else
							rsm.setValueToField("DT_VENCIMENTO_REFATURAMENTO", Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"));
					}
					// Seta juros e multas no dataset usado no pdv
					if(isFactoring==1){
						Object result[] = calcJurosMultaCheque(rsm.getInt("cd_conta_receber"), Util.getDataAtual(), 1);
						if((Double)result[0] == -10 && (Double)result[1] == -10){
							return null;
						}
						rsm.setValueToField("VL_MULTA", result[0]);
						rsm.setValueToField("CD_CATEGORIA_MULTA", result[4]);
						rsm.setValueToField("NM_CATEGORIA_MULTA", result[5]);
						rsm.setValueToField("VL_JUROS", result[1]);
						rsm.setValueToField("CD_CATEGORIA_JUROS", result[2]);
						rsm.setValueToField("NM_CATEGORIA_JUROS", result[3]);
					}
					else {
						Object[] valores = calcJurosMulta(rsm.getInt("cd_conta_receber"));
						Double vlMulta = Double.parseDouble(valores[0].toString());
						Double vlJuros = Double.parseDouble(valores[1].toString());
						
						rsm.setValueToField("VL_MULTA", vlMulta);
						rsm.setValueToField("VL_JUROS", vlJuros);
					}
					
					//PROCESSO
					String nrProcesso = "";
					PreparedStatement ps = connection.prepareStatement(
							"SELECT cd_processo FROM prc_processo_financeiro WHERE cd_conta_receber = "+rsm.getInt("cd_conta_receber"));
					ResultSetMap rsmProcesso = new ResultSetMap(ps.executeQuery());
					while(rsmProcesso.next()) {
						Processo processo = ProcessoDAO.get(rsmProcesso.getInt("cd_processo"), connection);
						if(processo.getNrProcesso()!=null && !processo.getNrProcesso().equals(""))
							nrProcesso += processo.getNrProcesso();
						else
							nrProcesso += processo.getNrInterno()!=null?processo.getNrInterno():"";
						
						if(rsmProcesso.hasMore())
							nrProcesso += ", ";
					}
					rsm.setValueToField("NR_PROCESSO", nrProcesso);
				}
				rsm.beforeFirst();
				return rsm; 
			}catch(Exception e){
				Util.registerLog(e);
				e.printStackTrace(System.out);
				return null;
			}finally	{
				if(isConnectionNull)
					Conexao.desconectar(connection);
			}
		else
			try{
				int notNegociacao = 0;
				int cdConvenio 	  = 0;
				int qtLimite      = 0;
				int isFactoring	  = 0;
				int cdData   	  = 0;
				int isPDV         = 0;
				String complemento = "";
				String ordenacao = "ASC";
				Boolean orderByPrioritaria = false;
				for (int i=0; criterios!=null && i<criterios.size(); i++)
					if (criterios.get(i).getColumn().equalsIgnoreCase("notNegociacao")) {
						notNegociacao = Integer.parseInt(criterios.get(i).getValue());
						criterios.remove(i);
						i--;
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("cdConvenio")) {
						cdConvenio = Integer.parseInt(criterios.get(i).getValue());
						criterios.remove(i);
						i--;
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
						qtLimite = Integer.parseInt(criterios.get(i).getValue());
						criterios.remove(i);
						i--;
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("st_conta") && 
							Integer.parseInt(criterios.get(i).getValue()) == -1) {
						criterios.remove(i);
						i--;
						continue;
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("isFactoring")) {
						isFactoring = Integer.parseInt(criterios.get(i).getValue());
						criterios.remove(i);
						i--;
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("A.dt_vencimento_final") ) {
						criterios.get(i).setColumn("A.dt_vencimento");
						criterios.get(i).setValue( criterios.get(i).getValue().substring(0, 10)+" 23:59:59"  );
						continue;
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("A.dt_emissao_final") ) {
						criterios.get(i).setColumn("A.dt_emissao");
						criterios.get(i).setValue( criterios.get(i).getValue().substring(0, 10)+" 23:59:59"  );
						continue;
					}
					else if (criterios.get(i).getColumn().equalsIgnoreCase("COMPLEMENTO")) {
						complemento = criterios.get(i).getValue();
						criterios.remove(i);
						i--;
					}else if (criterios.get(i).getColumn().equalsIgnoreCase("VENCIDAS")) {
						cdData = Integer.parseInt(criterios.get(i).getValue());
						criterios.remove(i);
						i--;
					}else if (criterios.get(i).getColumn().equalsIgnoreCase("isPDV")) {
						isPDV = Integer.parseInt(criterios.get(i).getValue());
						criterios.remove(i);
						i--;
					}else if (criterios.get(i).getColumn().equalsIgnoreCase("ordenacao")) {
						ordenacao = criterios.get(i).getValue();
						criterios.remove(i);
						i--;
						if( ordenacao.equals("ASC") || !ordenacao.equals("DESC")  )
							ordenacao = "ASC";
					}else if (criterios.get(i).getColumn().equalsIgnoreCase("orderbyprioritaria")) {
						orderByPrioritaria = true;
						criterios.remove(i);
						i--;
						
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
				//			
				ResultSetMap rsm = Search.find(
						   "SELECT "+limitSkip[0]+" DISTINCT (A.cd_conta_receber), A.*, A.cd_conta AS cd_conta, J.nm_razao_social AS nm_empresa, J.nr_cnpj AS nr_cnpj_empresa, " +
						   "	   A2.nm_pessoa AS nm_usuario, " +
						   "	   EN.nm_logradouro, EN.nr_cep, EN.nr_endereco, MUN.nm_municipio, MUN.nm_uf, BAI.nm_bairro, " +
				           "       K.nm_pessoa AS nm_fantasia, C.nm_pessoa, " +
						   "       C2.nm_razao_social, D.nr_conta, D.nr_dv, D.nm_conta, D.tp_operacao, E.nr_agencia, " +
						   "       F.nr_banco, F.nm_banco, G.nr_carteira, G.nm_carteira, G.sg_carteira, G.tp_digito, " +
						   "       G.nr_digitos_inicio, G.qt_digitos_numero, G.nm_moeda, G.nm_aceite, G.nr_cedente, " +
						   "       G.nm_local_pagamento, G.txt_mensagem, G.txt_campo_livre, G.pr_juros AS pr_juros_carteira, " +
						   "       G.pr_multa AS pr_multa_carteira, G.nr_base_inicial, G.nr_base_final, G.nr_convenio, G.nr_servico, " +
						   "       C1.nr_cpf, C2.nr_cnpj, C.nr_telefone1, C.nr_telefone2, C.nr_celular, " +
						   "       H.nm_tipo_documento, H.sg_tipo_documento, O.cd_registro " +
			    ((isPDV == 1) ? ",       I.cd_titulo_credito, I.cd_instituicao_financeira, I.cd_alinea, I.nr_documento_emissor, " +
						   		"       I.tp_documento_emissor, I.nm_emissor, I.tp_emissao, I.nr_agencia AS nr_agencia_titulo_credito, " +
						   		"       I.st_titulo, I.ds_observacao, I.dt_credito, I.tp_circulacao, I.cd_conta AS cd_conta_titulo_credito, " +
						   		"       L.nr_banco AS nr_instituicao_financeira, L.nm_banco AS nm_instituicao_financeira, " : "") +
//						   "	   PG.vl_pagamento AS vl_plano_pagto "+
				((isFactoring == 1) ? ", CPP.nm_pessoa AS nm_cliente " : "") +
//							" , PRC.nr_processo " +
				           "FROM adm_conta_receber A " +
				           "LEFT OUTER JOIN grl_pessoa A2 ON (A.cd_pessoa = A2.cd_pessoa) "+
				           "LEFT OUTER JOIN grl_pessoa_endereco EN ON (A.cd_pessoa = EN.cd_pessoa) "+
				           "LEFT OUTER JOIN municipio MUN ON (EN.cd_municipio = MUN.cod_municipio) "+
				           "LEFT OUTER JOIN bairro BAI ON (EN.cd_bairro = BAI.cod_bairro) "+
				           "LEFT OUTER JOIN grl_empresa          B ON (A.cd_empresa = B.cd_empresa) " +
				           "LEFT OUTER JOIN grl_pessoa_juridica  J ON (B.cd_empresa  = J.cd_pessoa) " +
				           "LEFT OUTER JOIN grl_pessoa           K ON (J.cd_pessoa  = K.cd_pessoa) " +
				           "LEFT OUTER JOIN grl_pessoa           C ON (A.cd_pessoa  = C.cd_pessoa) "+
				           "LEFT OUTER JOIN grl_pessoa_fisica   C1 ON (A.cd_pessoa = C1.cd_pessoa) "+
				           "LEFT OUTER JOIN grl_pessoa_juridica C2 ON (A.cd_pessoa = C2.cd_pessoa) "+
				           "LEFT OUTER JOIN adm_conta_financeira D ON (A.cd_conta = D.cd_conta) " +
				           "LEFT OUTER JOIN grl_agencia          E ON (D.cd_agencia = E.cd_agencia) " +
				           "LEFT OUTER JOIN grl_banco            F ON (E.cd_banco = F.cd_banco) "+
				           "LEFT OUTER JOIN adm_conta_carteira   G ON (A.cd_conta          = G.cd_conta " +
				           "                                       AND A.cd_conta_carteira = G.cd_conta_carteira) " +
				           "LEFT OUTER JOIN adm_tipo_documento   H ON (A.cd_tipo_documento = H.cd_tipo_documento) " +
				           "LEFT OUTER JOIN grl_arquivo   N ON (A.cd_arquivo = N.cd_arquivo) " +
				           "LEFT OUTER JOIN grl_arquivo_registro O ON (A.cd_conta_receber = O.cd_conta_receber) " +
				((isPDV == 1) ? "LEFT OUTER JOIN adm_titulo_credito   I ON (A.cd_conta_receber = I.cd_conta_receber) "+
				           		"LEFT OUTER JOIN grl_banco            L ON (I.cd_instituicao_financeira = L.cd_banco) " : "") +
//				           "LEFT OUTER JOIN adm_plano_pagto_documento_saida      PG ON (A.cd_documento_saida = PG.cd_documento_saida " +
//				           "													    AND A.cd_plano_pagamento = PG.cd_plano_pagamento " +
//				           "													    AND A.cd_forma_pagamento = PG.cd_forma_pagamento) " +
				((isFactoring == 1) ? "JOIN adm_conta_factoring  M   ON (A.cd_conta_receber = M.cd_conta_receber_antecipada) " +
						   			  "JOIN adm_conta_pagar		 CP  ON (M.cd_conta_pagar = CP.cd_conta_pagar) " +
						   			  "JOIN grl_pessoa           CPP ON (CP.cd_pessoa = CPP.cd_pessoa) " : "") +
				           (cdConvenio<=0 ? "" : "LEFT OUTER JOIN adm_contrato         M ON (A.cd_contrato = M.cd_contrato) ") +
//				           " LEFT OUTER JOIN prc_processo_financeiro PF ON (A.cd_conta_receber = PF.cd_conta_receber) "+
//				           " LEFT OUTER JOIN prc_processo PRC ON (PF.cd_processo = PRC.cd_processo) "+
	//tratado mais abaixo para evitar duplicidade numa conta com mais de um centro de custo
//				           "LEFT OUTER JOIN adm_conta_receber_categoria  N   ON (A.cd_conta_receber = N.cd_conta_receber) "+
//				           "LEFT OUTER JOIN adm_categoria_economica      O   ON (N.cd_categoria_economica = O.cd_categoria_economica) "+
//				           "LEFT OUTER JOIN ctb_centro_custo             P   ON (N.cd_centro_custo = P.cd_centro_custo) " +
				           "WHERE 1=1 " + complemento +
				           (!nmPessoa.equals("") ?
				        	"AND TRANSLATE (C.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', " + 
				        	"				'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmPessoa)+"%' "
				        	: "") +
				           (!dsHistorico.equals("") ?
									"AND TRANSLATE (A.ds_historico, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', " + 
									"				'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(dsHistorico)+"%' "
									: "")+
				           (cdData>0?" AND A.dt_vencimento <= \'" + Util.getDataAtual().getTimeInMillis()+"\'":"")+
				           (cdConvenio<=0 ? "" : " AND (A.cd_contrato = " + cdConvenio + " OR M.cd_convenio = " + cdConvenio + ") ") +
				           (notNegociacao<=0 ? "" : " AND (A.cd_negociacao IS NULL OR A.cd_negociacao <> " + notNegociacao + ") "),
				           " ORDER BY "+
				           (( orderByPrioritaria )?" A.LG_PRIORITARIA DESC, ":"")
				           + " A.DT_VENCIMENTO "+ordenacao+" "+limitSkip[1], criterios,
				           connection==null ? Conexao.conectar() : connection, connection==null);

				//centro de custo
				
//				while(rsm.next()){
//					PreparedStatement pstmt;
//					pstmt = connection.prepareStatement(
//							"SELECT A.*,"
//							+ " P.nm_centro_custo "
//							+ " FROM adm_conta_receber A "
//							+ " LEFT OUTER JOIN adm_conta_receber_categoria N   ON (A.cd_conta_receber = N.cd_conta_receber)"
//							+ " LEFT OUTER JOIN adm_categoria_economica     O   ON (N.cd_categoria_economica = O.cd_categoria_economica)"
//							+ " LEFT OUTER JOIN ctb_centro_custo            P   ON (N.cd_centro_custo = P.cd_centro_custo)"
//							+ " WHERE A.cd_conta_receber=?");
//					pstmt.setInt(1, rsm.getInt("cd_conta_receber"));
//					rsm.setValueToField("NM_CENTRO_CUSTO", Util.join(new ResultSetMap(pstmt.executeQuery()), "nm_centro_custo"));
//							
//					//Buscando valores do Titulo de Credito vinculado a Conta a Receber
//					ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
//					crt.add(new ItemComparator("cd_conta_receber", rsm.getString("cd_conta_receber"), ItemComparator.EQUAL, Types.INTEGER));
//					ResultSetMap rsmTituloCredito = TituloCreditoDAO.find(crt, connection);
//					if(rsmTituloCredito.next()){
//						HashMap<String, Object> register = rsmTituloCredito.getRegister();
//						for(String chave : register.keySet()){
//							String chaveOriginal = chave;
//							if(chave.equals("NR_AGENCIA"))
//								chave = "NR_AGENCIA_TITULO_CREDITO";
//							if(chave.equals("DT_VENCIMENTO"))
//								chave = "DT_VENCIMENTO_TITULO_CREDITO";
//							if(chave.equals("NR_DOCUMENTO"))
//								chave = "NR_DOCUMENTO_TITULO_CREDITO";
//							if(chave.equals("CD_CONTA"))
//								chave = "CD_CONTA_TITULO_CREDITO";
//							rsm.setValueToField(chave, register.get(chaveOriginal));
//						}
//						
//						//Buscando registros do banco ligado ao titulo de credito
//						crt = new ArrayList<ItemComparator>();
//						crt.add(new ItemComparator("cd_banco", rsm.getString("cd_instituicao_financeira"), ItemComparator.EQUAL, Types.INTEGER));
//						ResultSetMap rsmBanco = BancoDAO.find(crt, connection);
//						if(rsmBanco.next()){
//							register = rsmBanco.getRegister();
//							for(String chave : register.keySet()){
//								String chaveOriginal = chave;
//								if(chave.equals("NR_BANCO"))
//									chave = "NR_INSTITUICAO_FINANCEIRA";
//								else if(chave.equals("nm_banco"))
//									chave = "NM_INSTITUICAO_FINANCEIRA";
//								rsm.setValueToField(chave, register.get(chaveOriginal));
//							}
//						}
//					}
//					
//					if(rsm.getInt("cd_pessoa") > 0){
//						Result resultado = ClienteProgramaFaturaServices.getProgramaFaturaVigente(rsm.getInt("cd_empresa"), rsm.getInt("cd_pessoa"), connection);
//						ProgramaFatura programaFatura = (ProgramaFatura) resultado.getObjects().get("programaFatura");
//						if(programaFatura != null){
//							//Usado pelo refaturamento para calcular o novo dia de vencimento
//							GregorianCalendar dtVencimento = Util.getDataAtual();
//							int qtDiasCarencia = programaFatura.getQtDiasCarencia();
//							dtVencimento.add(Calendar.DAY_OF_MONTH, qtDiasCarencia);
//							rsm.setValueToField("DT_VENCIMENTO_REFATURAMENTO", Util.formatDate(dtVencimento, "dd/MM/yyyy"));
//						}
//						else
//							rsm.setValueToField("DT_VENCIMENTO_REFATURAMENTO", Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"));
//					}
//					// Seta juros e multas no dataset usado no pdv
//					if(isFactoring==1){
//						Object result[] = calcJurosMultaCheque(rsm.getInt("cd_conta_receber"), Util.getDataAtual(), 1);
//						if((Double)result[0] == -10 && (Double)result[1] == -10){
//							return null;
//						}
//						rsm.setValueToField("VL_MULTA", result[0]);
//						rsm.setValueToField("CD_CATEGORIA_MULTA", result[4]);
//						rsm.setValueToField("NM_CATEGORIA_MULTA", result[5]);
//						rsm.setValueToField("VL_JUROS", result[1]);
//						rsm.setValueToField("CD_CATEGORIA_JUROS", result[2]);
//						rsm.setValueToField("NM_CATEGORIA_JUROS", result[3]);
//					}
//					else {
//						Object[] valores = calcJurosMulta(rsm.getInt("cd_conta_receber"));
//						Double vlMulta = Double.parseDouble(valores[0].toString());
//						Double vlJuros = Double.parseDouble(valores[1].toString());
//						
//						rsm.setValueToField("VL_MULTA", vlMulta);
//						rsm.setValueToField("VL_JUROS", vlJuros);
//					}
//					
//					//PROCESSO
//					String nrProcesso = "";
//					PreparedStatement ps = connection.prepareStatement(
//							"SELECT cd_processo FROM prc_processo_financeiro WHERE cd_conta_receber = "+rsm.getInt("cd_conta_receber"));
//					ResultSetMap rsmProcesso = new ResultSetMap(ps.executeQuery());
//					while(rsmProcesso.next()) {
//						Processo processo = ProcessoDAO.get(rsmProcesso.getInt("cd_processo"), connection);
//						if(processo.getNrProcesso()!=null && !processo.getNrProcesso().equals(""))
//							nrProcesso += processo.getNrProcesso();
//						else
//							nrProcesso += processo.getNrInterno()!=null?processo.getNrInterno():"";
//						
//						if(rsmProcesso.hasMore())
//							nrProcesso += ", ";
//					}
//					rsm.setValueToField("NR_PROCESSO", nrProcesso);
//				}
				rsm.beforeFirst();
				return rsm; 
			}catch(Exception e){
				Util.registerLog(e);
				e.printStackTrace(System.out);
				return null;
			}finally	{
				if(isConnectionNull)
					Conexao.desconectar(connection);
			}
	}
	public static ResultSetMap findContas(ArrayList<sol.dao.ItemComparator> criterios) {
		return findContas(criterios, null);
	}
	
	public static ResultSetMap findContas(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		ResultSetMap rsm = new ResultSetMap();
		String orderBy = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
				orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}
		rsm = Search.find(" SELECT A.*, B.*"
				+ " FROM adm_conta_carteira A"
				+ " LEFT OUTER JOIN adm_conta_financeira B ON B.cd_conta = A.cd_conta"
				+(orderBy != "" ? orderBy : ""), criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		return rsm;
	}
	public static ResultSetMap getCodigo(ArrayList<sol.dao.ItemComparator> criterios) {
		return getCodigo(criterios, null);
	}
	
	public static ResultSetMap getCodigo(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();
			int isFactoring	  = 0;
			for (int i=0; criterios!=null && i<criterios.size(); i++)
				if (criterios.get(i).getColumn().equalsIgnoreCase("isFactoring")) 
					isFactoring = Integer.parseInt(criterios.get(i).getValue());
		ResultSetMap rsm = find(criterios, connect);
		while(rsm.next())	{
			Double vlAReceber = rsm.getDouble("VL_CONTA") - rsm.getDouble("VL_ABATIMENTO") + rsm.getDouble("VL_ACRESCIMO") - rsm.getDouble("VL_RECEBIDO");
//			Double vlAReceber = 10.0;
			if(vlAReceber>0) {
				// Tratando nosso numeros
				String nrNossoNumero = rsm.getString("id_conta_receber")==null ? "" : rsm.getString("id_conta_receber").replaceAll("[\\D]", "");

				String txtCampoLivre  = rsm.getString("txt_campo_livre");
				String nrCodigoBarras = getCodigoBarras(rsm.getString("NR_BANCO"), rsm.getString("NR_AGENCIA"),
														rsm.getString("NR_CONTA"),rsm.getInt("TP_OPERACAO"),rsm.getString("NR_CARTEIRA"),
														rsm.getString("NR_SERVICO"),
														rsm.getString("NR_CEDENTE"), rsm.getString("NR_CONVENIO"),
														nrNossoNumero,
														vlAReceber, rsm.getGregorianCalendar("dt_vencimento"),
														rsm.getGregorianCalendar("dt_emissao"),
														txtCampoLivre);
				rsm.setValueToField("NR_CODIGO_BARRAS", nrCodigoBarras);
				rsm.setValueToField("NR_LINHA_DIGITAVEL", getLinhaDigitavel(nrCodigoBarras));
				rsm.setValueToField("NR_NOSSO_NUMERO", nrNossoNumero.equals("")?"":nrNossoNumero+(rsm.getInt("tp_digito")!=5?"-":"")+getDvNossoNumero(nrNossoNumero, rsm.getInt("tp_digito"), rsm.getInt("nr_base_inicial"), rsm.getInt("nr_base_final")));
				rsm.setValueToField("NR_DV_BANCO", rsm.getString("NR_BANCO")!=null ? Util.getDvMod11(rsm.getString("NR_BANCO").replaceAll("[\\D]", "").trim(), "0", "0", 9, 2, false): "");
				if(isFactoring==1){
					Object result[] = calcJurosMultaCheque(rsm.getInt("cd_conta_receber"), Util.getDataAtual(), 1);
					if((Double)result[0] == -10 && (Double)result[1] == -10){
						return null;
					}
					rsm.setValueToField("VL_MULTA", result[0]);
					rsm.setValueToField("CD_CATEGORIA_MULTA", result[4]);
					rsm.setValueToField("NM_CATEGORIA_MULTA", result[5]);
					rsm.setValueToField("VL_JUROS", result[1]);
					rsm.setValueToField("CD_CATEGORIA_JUROS", result[2]);
					rsm.setValueToField("NM_CATEGORIA_JUROS", result[3]);
				}
				else {
					Object[] valores = calcJurosMulta(rsm.getInt("cd_conta_receber"));
					Double vlMulta = Double.parseDouble(valores[0].toString());
					Double vlJuros = Double.parseDouble(valores[1].toString());
					
					rsm.setValueToField("VL_MULTA", vlMulta);
					rsm.setValueToField("VL_JUROS", vlJuros);
				}
				// Codigo de Barras Atualizado
				String nrCodigoBarrasAtual = nrCodigoBarras;
				if(rsm.getGregorianCalendar("dt_vencimento")!=null && rsm.getGregorianCalendar("dt_vencimento").before(new GregorianCalendar())) {
					nrCodigoBarrasAtual = getCodigoBarras(rsm.getString("NR_BANCO"), rsm.getString("NR_AGENCIA"),
															 rsm.getString("NR_CONTA"),rsm.getInt("TP_OPERACAO"),rsm.getString("NR_CARTEIRA"),
															 rsm.getString("NR_SERVICO"),
															 rsm.getString("NR_CEDENTE"), rsm.getString("NR_CONVENIO"),
															 nrNossoNumero,
															 vlAReceber + rsm.getDouble("VL_MULTA")+rsm.getDouble("VL_JUROS"), 
															 new GregorianCalendar(),
															 rsm.getGregorianCalendar("dt_emissao"),
															 txtCampoLivre);
				}
				rsm.setValueToField("NR_CODIGO_BARRAS_ATUAL", nrCodigoBarrasAtual);
				rsm.setValueToField("NR_LINHA_DIGITAVEL_ATUAL", getLinhaDigitavel(nrCodigoBarrasAtual));
			}
		}
		return rsm;
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveArquivo(Arquivo arquivo) {
		return saveArquivo(arquivo, null);
	}
	
	public static Result saveArquivo(Arquivo arquivo, Connection connect) {
		Result result;
		try {
			arquivo.setDtCriacao(new GregorianCalendar());
			arquivo.setDtArquivamento(new GregorianCalendar());
			
			int code = ArquivoDAO.insert(arquivo, connect);
			
//			Result resposta = atualizarConta(code, CD_CONTA_RECEBER, connect);
			
			result = new Result(code);
			return result;
		} catch(Exception e){
			e.printStackTrace(System.out);
			result = new Result(-1, "Erro ao tentar salvar arquivo!");
			return result;
		}
	}
	
	public static Result saveRegistro(int CD_ARQUIVO, int CD_CONTA_RECEBER) {
		return saveRegistro(CD_ARQUIVO, CD_CONTA_RECEBER, null);
	}
	
	public static Result saveRegistro(int CD_ARQUIVO, int CD_CONTA_RECEBER, Connection connect) {
		boolean isConnectionNull = connect==null;
		Result result;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_arquivo_registro", connect);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_arquivo_registro ("+
																	"cd_registro,"+
																	"cd_arquivo,"+
																	"cd_conta_receber)"+
																	" VALUES (?, ?, ?)"
					);
			
			pstmt.setInt(1, code);
			pstmt.setInt(2, CD_ARQUIVO);
			pstmt.setInt(3, CD_CONTA_RECEBER);
			pstmt.execute();
			
			result = new Result(code);
			return result;
		} catch(Exception e){
			e.printStackTrace(System.out);
			result = new Result(-1, "Erro ao tentar salvar arquivo!");
			return result;
		}
	}
	
	public static Result atualizarConta (int code, int CD_CONTA_RECEBER, Connection connect) {
		boolean isConnectionNull = connect==null;
		Result result;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_receber SET cd_arquivo=? "+
		      		   "WHERE cd_conta_receber=?");
			pstmt.setInt(1, code);
			pstmt.setInt(2, CD_CONTA_RECEBER);
			pstmt.executeUpdate();
			return result = new Result(1, "Atualização feita com sucesso!");
		}catch(Exception e){
			e.printStackTrace(System.out);
			result = new Result(-1, "Erro ao tentar atualizar!");
			return result;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	
	}
	public static ResultSetMap findParcelas(ArrayList<sol.dao.ItemComparator> criterios) {
		return findParcelas(criterios, null);
	}
	
	public static ResultSetMap findParcelas(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();
			int isFactoring	  = 0;
			for (int i=0; criterios!=null && i<criterios.size(); i++)
				if (criterios.get(i).getColumn().equalsIgnoreCase("isFactoring")) 
					isFactoring = Integer.parseInt(criterios.get(i).getValue());	
			ResultSetMap rsm = find(criterios, connect);
			while(rsm.next())	{
				Double vlAReceber = rsm.getDouble("VL_CONTA") - rsm.getDouble("VL_ABATIMENTO") + rsm.getDouble("VL_ACRESCIMO") - rsm.getDouble("VL_RECEBIDO");
				if(vlAReceber>0)	{
					if(rsm.getString("id_conta_receber")==null || rsm.getString("id_conta_receber").equals(""))	{
						ContaReceber conta = new ContaReceber(rsm.getInt("cd_conta_receber"),rsm.getInt("cd_pessoa"),
																rsm.getInt("cd_empresa"),rsm.getInt("cd_contrato"),
																rsm.getInt("cd_conta_origem"),rsm.getInt("cd_documento_saida"),
																rsm.getInt("cd_conta_carteira"),rsm.getInt("cd_conta"),
																rsm.getInt("cd_frete"),rsm.getString("nr_documento"),
																rsm.getString("id_conta_receber"),rsm.getInt("nr_parcela"),
																rsm.getString("nr_referencia"),rsm.getInt("cd_tipo_documento"),
																rsm.getString("ds_historico"),
																(rsm.getTimestamp("dt_vencimento")==null)?null:com.tivic.manager.util.Util.longToCalendar(rsm.getTimestamp("dt_vencimento").getTime()),
																(rsm.getTimestamp("dt_emissao")==null)?null:Util.longToCalendar(rsm.getTimestamp("dt_emissao").getTime()),
																(rsm.getTimestamp("dt_recebimento")==null)?null:Util.longToCalendar(rsm.getTimestamp("dt_recebimento").getTime()),
																(rsm.getTimestamp("dt_prorrogacao")==null)?null:Util.longToCalendar(rsm.getTimestamp("dt_prorrogacao").getTime()),
																rsm.getDouble("vl_conta"),rsm.getDouble("vl_abatimento"),
																rsm.getDouble("vl_acrescimo"),rsm.getDouble("vl_recebido"),
																rsm.getInt("st_conta"),rsm.getInt("tp_frequencia"),
																rsm.getInt("qt_parcelas"),rsm.getInt("tp_conta_receber"),
																rsm.getInt("cd_negociacao"),rsm.getString("txt_observacao"),
																rsm.getInt("cd_plano_pagamento"),rsm.getInt("cd_forma_pagamento"),
																(rsm.getTimestamp("dt_digitacao")==null)?null:com.tivic.manager.util.Util.longToCalendar(rsm.getTimestamp("dt_digitacao").getTime()),
																(rsm.getTimestamp("dt_vencimento_original")==null)?null:com.tivic.manager.util.Util.longToCalendar(rsm.getTimestamp("dt_vencimento_original").getTime()),
																rsm.getInt("cd_turno"),
																rsm.getDouble("pr_juros"),
																rsm.getDouble("pr_multa"),
																rsm.getInt("lg_protesto"));
						gerarNossoNumeroAndRegistro(conta, rsm.getString("nr_digitos_inicio"), rsm.getInt("qt_digitos_numero"), rsm.getInt("tp_cobranca"), connect);
						connect.prepareStatement(" UPDATE adm_conta_receber " +
												 " SET id_conta_receber = \'"+conta.getIdContaReceber()+"\' " +
								                 " WHERE cd_conta_receber = "+conta.getCdContaReceber()).executeUpdate();
						rsm.setValueToField("ID_CONTA_RECEBER", conta.getIdContaReceber());
					}
					// Tratando nosso numeros
					String nrNossoNumero = rsm.getString("id_conta_receber")==null ? "" : rsm.getString("id_conta_receber").replaceAll("[\\D]", "");

					String txtCampoLivre  = rsm.getString("txt_campo_livre");
					String nrCodigoBarras = getCodigoBarras(rsm.getString("NR_BANCO"), rsm.getString("NR_AGENCIA"),
															rsm.getString("NR_CONTA"),rsm.getInt("TP_OPERACAO"),rsm.getString("NR_CARTEIRA"),
															rsm.getString("NR_SERVICO"),
															rsm.getString("NR_CEDENTE"), rsm.getString("NR_CONVENIO"),
															nrNossoNumero,
															vlAReceber, rsm.getGregorianCalendar("dt_vencimento"),
															rsm.getGregorianCalendar("dt_emissao"),
															txtCampoLivre);
					rsm.setValueToField("NR_CODIGO_BARRAS", nrCodigoBarras);
					rsm.setValueToField("NR_LINHA_DIGITAVEL", getLinhaDigitavel(nrCodigoBarras));
					rsm.setValueToField("NR_NOSSO_NUMERO", nrNossoNumero.equals("")?"":nrNossoNumero+(rsm.getInt("tp_digito")!=5?"-":"")+getDvNossoNumero(nrNossoNumero, rsm.getInt("tp_digito"), rsm.getInt("nr_base_inicial"), rsm.getInt("nr_base_final")));
					rsm.setValueToField("NR_DV_BANCO", rsm.getString("NR_BANCO")!=null ? Util.getDvMod11(rsm.getString("NR_BANCO").replaceAll("[\\D]", "").trim(), "0", "0", 9, 2, false): "");
					if(isFactoring==1){
						Object result[] = calcJurosMultaCheque(rsm.getInt("cd_conta_receber"), Util.getDataAtual(), 1);
						if((Double)result[0] == -10 && (Double)result[1] == -10){
							return null;
						}
						rsm.setValueToField("VL_MULTA", result[0]);
						rsm.setValueToField("CD_CATEGORIA_MULTA", result[4]);
						rsm.setValueToField("NM_CATEGORIA_MULTA", result[5]);
						rsm.setValueToField("VL_JUROS", result[1]);
						rsm.setValueToField("CD_CATEGORIA_JUROS", result[2]);
						rsm.setValueToField("NM_CATEGORIA_JUROS", result[3]);
					}
					else {
						Object[] valores = calcJurosMulta(rsm.getInt("cd_conta_receber"));
						Double vlMulta = Double.parseDouble(valores[0].toString());
						Double vlJuros = Double.parseDouble(valores[1].toString());
						
						rsm.setValueToField("VL_MULTA", vlMulta);
						rsm.setValueToField("VL_JUROS", vlJuros);
					}
					// Codigo de Barras Atualizado
					String nrCodigoBarrasAtual = nrCodigoBarras;
					if(rsm.getGregorianCalendar("dt_vencimento")!=null && rsm.getGregorianCalendar("dt_vencimento").before(new GregorianCalendar())) {
						nrCodigoBarrasAtual = getCodigoBarras(rsm.getString("NR_BANCO"), rsm.getString("NR_AGENCIA"),
																 rsm.getString("NR_CONTA"),rsm.getInt("TP_OPERACAO"),rsm.getString("NR_CARTEIRA"),
																 rsm.getString("NR_SERVICO"),
																 rsm.getString("NR_CEDENTE"), rsm.getString("NR_CONVENIO"),
																 nrNossoNumero,
																 vlAReceber + rsm.getDouble("VL_MULTA")+rsm.getDouble("VL_JUROS"), 
																 new GregorianCalendar(),
																 rsm.getGregorianCalendar("dt_emissao"),
																 txtCampoLivre);
					}
					rsm.setValueToField("NR_CODIGO_BARRAS_ATUAL", nrCodigoBarrasAtual);
					rsm.setValueToField("NR_LINHA_DIGITAVEL_ATUAL", getLinhaDigitavel(nrCodigoBarrasAtual));
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findByCategorias(ArrayList<sol.dao.ItemComparator> criterios) {
		return findByCategorias(criterios, null);
	}

	public static ResultSetMap findByCategorias(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			int qtLimite = 0;
			String ordenacao = "ASC";
			for (int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					//break;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("ordenacao")) {
					ordenacao = criterios.get(i).getValue();
					criterios.remove(i);
					if( ordenacao.equals("ASC") || !ordenacao.equals("DESC")  )
						ordenacao = "ASC";
					//break;
				}
			}
			String[] limitSkip = Util.getLimitAndSkip(qtLimite, 0);
			
			ResultSetMap rsm = Search.find(
							   "SELECT "+limitSkip[0]+" A.*, H.vl_conta_categoria, H2.*, I.*, C.nm_pessoa,   "+
					           "       C.nm_pessoa AS nm_favorecido, F.nm_conta, "+
					           "       G.nm_tipo_documento, G.sg_tipo_documento, "+
					           " 	   ((A.vl_conta+A.vl_acrescimo-A.vl_abatimento)-A.vl_recebido) as vl_areceber "+
					           "FROM adm_conta_receber 						 A " +
					           "JOIN grl_empresa                             B   ON (A.cd_empresa = B.cd_empresa) " +
					           "LEFT OUTER JOIN grl_pessoa                   C   ON (A.cd_pessoa  = C.cd_pessoa) " +
					           "LEFT OUTER JOIN grl_pessoa_juridica          D   ON (B.cd_empresa  = D.cd_pessoa) " +
					           "LEFT OUTER JOIN grl_pessoa                   E   ON (D.cd_pessoa  = E.cd_pessoa) " +
					           "LEFT OUTER JOIN adm_conta_financeira         F   ON (A.cd_conta = F.cd_conta) " +
					           "LEFT OUTER JOIN adm_tipo_documento           G   ON (A.cd_tipo_documento = G.cd_tipo_documento) " +
					           "LEFT OUTER JOIN adm_conta_receber_categoria  H   ON (A.cd_conta_receber = H.cd_conta_receber) "+
					           "LEFT OUTER JOIN adm_categoria_economica      H2  ON (H.cd_categoria_economica = H2.cd_categoria_economica) "+
					           "LEFT OUTER JOIN ctb_centro_custo             I   ON (H.cd_centro_custo = I.cd_centro_custo) ", 
					           " ORDER BY A.cd_conta_receber,  A.dt_vencimento "+ordenacao+" "+limitSkip[1], 
					           criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			while(rsm.next()) {
				//PROCESSO
				String nrProcesso = "";
				PreparedStatement ps = connect.prepareStatement(
						"SELECT cd_processo FROM prc_processo_financeiro WHERE cd_conta_receber = "+rsm.getInt("cd_conta_receber"));
				ResultSetMap rsmProcesso = new ResultSetMap(ps.executeQuery());
				while(rsmProcesso.next()) {
					Processo processo = ProcessoDAO.get(rsmProcesso.getInt("cd_processo"), connect);
					nrProcesso += processo.getNrProcesso();
					if(rsmProcesso.hasMore())
						nrProcesso += ", ";
				}
				rsm.setValueToField("NR_PROCESSO", nrProcesso);
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("ContaReceberServices.findByCategorias");
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static ResultSetMap findByCliente(ArrayList<sol.dao.ItemComparator> criterios){
		return findByCliente(criterios, null);
	}
	
	public static ResultSetMap findByCliente(ArrayList<sol.dao.ItemComparator> criterios, Connection connection) {
		int notNegociacao 	 = 0;
		int cdConvenio 	  	 = 0;
		int qtLimite      	 = 0;
		int isFactoring	  	 = 0;
		int isGroupByCliente = 0;
		for (int i=0; criterios!=null && i<criterios.size(); i++)
			if (criterios.get(i).getColumn().equalsIgnoreCase("notNegociacao")) {
				notNegociacao = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("cdConvenio")) {
				cdConvenio = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
				qtLimite = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("isFactoring")) {
				isFactoring = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("isGroupByCliente")) {
				isGroupByCliente = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
			}
		String[] limitSkip = Util.getLimitAndSkip(qtLimite, 0);
		return Search.find("SELECT C.cd_pessoa, C.nm_pessoa, " + (isFactoring == 1 ? (isGroupByCliente == 1 ? "CPP.nm_pessoa AS nm_cliente, " : "") : "") +
				            "      SUM(A.vl_conta) AS vl_conta, " +
							"      SUM(A.vl_recebido) AS vl_recebido, " +
							"	   SUM(A.vl_acrescimo) AS vl_acrescimo,  " +
							"	   SUM(A.vl_abatimento) AS vl_abatimento, " +
							"      COUNT(*) AS qt_conta "+
						   "FROM adm_conta_receber A " +
				           "LEFT OUTER JOIN grl_pessoa           C ON (A.cd_pessoa  = C.cd_pessoa) "+
				           (isFactoring == 1 ? "JOIN adm_conta_factoring  M ON (A.cd_conta_receber = M.cd_conta_receber_antecipada) " +
				        		   			   "JOIN adm_conta_pagar		 CP  ON (M.cd_conta_pagar = CP.cd_conta_pagar) " +
				        		   			   "JOIN grl_pessoa           CPP ON (CP.cd_pessoa = CPP.cd_pessoa) "  : "") +
				           (cdConvenio>0     ? "LEFT OUTER JOIN adm_contrato         M ON (A.cd_contrato = M.cd_contrato) " : "") +
				           "LEFT OUTER JOIN adm_titulo_credito   I ON (A.cd_conta_receber = I.cd_conta_receber) " + 
				           "WHERE 1=1 " +
				           (cdConvenio<=0    ? "" : " AND (A.cd_contrato = " + cdConvenio + " OR M.cd_convenio = " + cdConvenio + ") ") +
				           (notNegociacao<=0 ? "" : " AND (A.cd_negociacao IS NULL OR A.cd_negociacao <> " + notNegociacao + ") "),
				           " GROUP BY C.cd_pessoa, C.nm_pessoa " + (isFactoring == 1 ? (isGroupByCliente == 1 ? "CPP.nm_pessoa, " : "") : "") +
				           " ORDER BY C.nm_pessoa " +limitSkip[1], criterios,
				           connection==null ? Conexao.conectar() : connection, connection==null, true);
	}

	public static Result gerarParcelasOutraConta(ContaReceber conta, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if(conta.getCdContrato()>0 || conta.getCdDocumentoSaida()>0 || conta.getCdFrete()>0 || conta.getCdNegociacao()>0)
				return new Result(1, "");

			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			int[] parcelas = new int[] {0, conta.getQtParcelas()-1, 365, 52, 24, 24, 12, 12, 6, 4, 3, 2, 1, 1};
			int qtParcelas = parcelas[conta.getTpFrequencia()];
			//int qtParcelas = (conta.getTpFrequencia()!= UNICA_VEZ)?conta.getQtParcelas()-1:0;
			GregorianCalendar dtVencimento = conta.getDtVencimento();
			// Contando a quantidade de contas que ja existem geradas
			String sql = "SELECT count(*) AS qt_contas, MAX(dt_vencimento) AS dt_vencimento " +
					                                                "FROM adm_conta_receber " +
										                            "WHERE cd_conta_origem IN ("+conta.getCdContaOrigem()+","+conta.getCdContaReceber()+") "+
										                            (conta.getTpFrequencia()==QUANTIDADE_FIXA?"":"  AND st_conta = "+ST_EM_ABERTO);
			PreparedStatement pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next())	{
				qtParcelas -= rsm.getInt("qt_contas");
				if(rsm.getGregorianCalendar("dt_vencimento")!=null)
					dtVencimento = rsm.getGregorianCalendar("dt_vencimento");
			}

			Result result = gerarParcelas(conta.getCdContaReceber(), OF_OUTRA_CONTA,  conta.getCdEmpresa(), conta.getCdPessoa(), conta.getCdTipoDocumento(),
											conta.getCdConta(), conta.getCdContaCarteira(), qtParcelas, conta.getVlConta(), ContratoServices.DESC_PERCENTUAL,
											0.0 /*prDesconto*/, 0.0 /*vlDesconto*/, 0.0/*vlAdesao*/, ContaPagarServices.getProximoVencimento(dtVencimento, conta.getTpFrequencia(), 0),
											conta.getDtVencimento().get(Calendar.DAY_OF_MONTH),	conta.getTpFrequencia(), conta.getNrDocumento(), conta.getDsHistorico(),
											-1/*tpAmortizacao*/, 0.0/*prJuros*/, 0.0/*vlTotal*/, 0.0/*vlIof*/, 0/*cdCategoriaAdesao*/, 0/*cdCategoriaParcelas*/,
											-1/*tpContaReceber*/, 0/*cdNegociacao*/, null, false, connect);

			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}

			if (isConnectionNull)
				connect.commit();

			return result;
		}
		catch(Exception e){
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar gerar outras contas!", e);
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/* Chamada em documento_saida.jsp */
	public static Result gerarParcelas(int cdForeignKey, int tpForeignKey, int cdEmpresa, int cdPessoa,
			int cdTipoDocumento, int cdConta, int cdContaCarteira, int qtParcelas, Double vlParcela, Double prDesconto,
			Double vlAdesao, GregorianCalendar dtVencimentoPrimeira, int nrDiaVencimento, int tpFrequencia,
			String prefixDocumento, String dsHistorico, int tpAmortizacao, Double prJuros, Double vlTotal, Double vlIofPorParcela,
			int cdCategoriaAdesao, int cdCategoriaParcelas, int tpContaReceber, int cdNegociacao, ArrayList<HashMap<String, Object>> dadosParcelas,
			boolean simulateParcelas) {
		return gerarParcelas(cdForeignKey, tpForeignKey, cdEmpresa, cdPessoa, cdTipoDocumento, cdConta, cdContaCarteira,
				qtParcelas, vlParcela, ContratoServices.DESC_PERCENTUAL, prDesconto, 0.0, vlAdesao, dtVencimentoPrimeira, nrDiaVencimento, tpFrequencia, prefixDocumento,
				dsHistorico, tpAmortizacao, prJuros, vlTotal, vlIofPorParcela, cdCategoriaAdesao, cdCategoriaParcelas,
				tpContaReceber, cdNegociacao, dadosParcelas, simulateParcelas, null);
	}

	public static Result gerarParcelas(int cdForeignKey, int tpForeignKey, int cdEmpresa, int cdPessoa,
			int cdTipoDocumento, int cdConta, int cdContaCarteira, int qtParcelas, Double vlParcela, int tpDesconto, Double prDesconto,
			Double vlDesconto, Double vlAdesao, GregorianCalendar dtVencimentoPrimeira, int nrDiaVencimento, int tpFrequencia,
			String prefixDocumento, String dsHistorico, int tpAmortizacao, Double prJuros, Double vlTotal, Double vlIofPorParcela,
			int cdCategoriaAdesao, int cdCategoriaParcelas, int tpContaReceber, int cdNegociacao, ArrayList<HashMap<String, Object>> dadosParcelas,
			boolean simulateParcelas, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull && !simulateParcelas ? false : connection.getAutoCommit());

			ArrayList<ContaReceber> contas = new ArrayList<ContaReceber>();
			int tpVencAdesao = ParametroServices.getValorOfParametroAsInteger("TP_VENCIMENTO_ADESAO", VENC_ADESAO_PARCELA1_DISTINTOS, 0 /*cdEmpresa*/, connection);
			int tpConfigVencDefault = ParametroServices.getValorOfParametroAsInteger("TP_VENCIMENTO_PARCELAS_CONTRATO", VENC_ADESAO_PARCELA1_DISTINTOS, 0 /*cdEmpresa*/, connection);
			boolean lgIgnorarDiasNaoUteis = ParametroServices.getValorOfParametroAsInteger("LG_IGNORAR_DIA_NAO_UTIL", 0, cdEmpresa, connection)==1;
			int cdContrato = tpForeignKey==OF_CONTRATO ? cdForeignKey : 0;
			Contrato contrato = cdContrato<=0 ? null : ContratoDAO.get(cdContrato, connection);

			// Buscando informacoes no Contrato
			GregorianCalendar dtVencimento = dtVencimentoPrimeira;
			GregorianCalendar dtEmissao = new GregorianCalendar();
			dtEmissao = new GregorianCalendar(dtEmissao.get(Calendar.YEAR), dtEmissao.get(Calendar.MONTH), dtEmissao.get(Calendar.DAY_OF_MONTH));
			Double vlSaldoDevedor = vlTotal;
			int cdEventoCapital = simulateParcelas ? 0 : ParametroServices.getValorOfParametroAsInteger("CD_EVENTO_FINANCEIRO_CAPITAL", 0);
			int cdEventoJuros   = simulateParcelas ? 0 : ParametroServices.getValorOfParametroAsInteger("CD_EVENTO_FINANCEIRO_JUROS", 0);
			int cdEventoIof     = simulateParcelas ? 0 : ParametroServices.getValorOfParametroAsInteger("CD_EVENTO_FINANCEIRO_IOF", 0);
			if(cdCategoriaParcelas<=0)
				cdCategoriaParcelas = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_RECEITAS_DEFAULT", 0, cdEmpresa);
			if(nrDiaVencimento<=0 && dtVencimentoPrimeira!=null)
				nrDiaVencimento = dtVencimentoPrimeira.get(Calendar.DATE);
			/*
			 *  CATEGORIAS ECONOMICAS
			 *  	Se a conta estiver sendo gerar a partir de uma conta pega a classificacao da conta original
			 */
			ResultSetMap rsmCategorias = new ResultSetMap();
			int nrParcelaInicial = 0;
			if(tpForeignKey==OF_OUTRA_CONTA)	{
				ContaReceber conta = ContaReceberDAO.get(cdForeignKey, connection);
				dtEmissao          = conta.getDtEmissao();
				rsmCategorias      = ContaReceberCategoriaServices.getCategoriaOfContaReceber(cdForeignKey, connection);
				nrParcelaInicial   = conta.getNrParcela();
			}
			if(cdCategoriaAdesao<=0)
				cdCategoriaAdesao = cdCategoriaParcelas;
			for(int i=nrParcelaInicial; i<=qtParcelas; i++)	{
				Double vlConta = vlParcela;
				for (int j=0; dadosParcelas!=null && j<dadosParcelas.size(); j++)
					if (dadosParcelas.get(j).get("nrParcela")!=null && ((Integer)dadosParcelas.get(j).get("nrParcela")).intValue() == i) {
						vlConta = ((Double)dadosParcelas.get(j).get("vlParcela"));
						break;
					}
				/*
				 *  Verifica se ï¿½ a entrada (adesao), se o valor da adesao for zero nao cria a parcela '0'
				 */
				if(i==0 && vlAdesao>0)	{
					vlConta = vlAdesao;
					for (int j=0; dadosParcelas!=null && j<dadosParcelas.size(); j++)
						if (dadosParcelas.get(j).get("nrParcela")!=null && ((Integer)dadosParcelas.get(j).get("nrParcela")).intValue() == i) {
							vlConta = ((Double)dadosParcelas.get(j).get("vlParcela"));
							break;
						}
					if (cdCategoriaAdesao>0) {
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("CD_CATEGORIA_ECONOMICA", cdCategoriaAdesao);
						register.put("VL_CONTA_CATEGORIA", vlConta);
						rsmCategorias.addRegister(register);
					}
				}
				else if(i==0)
					continue;
				/*
				 *  Registra divisao em categorias
				 */
				if (i>0 && cdCategoriaParcelas>0) {
					rsmCategorias = new ResultSetMap();
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("CD_CATEGORIA_ECONOMICA", cdCategoriaParcelas);
					register.put("VL_CONTA_CATEGORIA", vlParcela);
					rsmCategorias.addRegister(register);
				}
				/*
				 *  VERIFICA DIAS NAO ï¿½TEIS
				 *  	Clona a data para que mudancas nela por causa de dias nao uteis nao sejam replicadas
				 */
				GregorianCalendar venc = i!=0 
											?(GregorianCalendar)dtVencimento.clone() 
											: tpVencAdesao==ADESAO_VENC_PRIMEIRA
												?(GregorianCalendar)dtVencimento.clone() 
												: tpVencAdesao==ADESAO_VENC_ASSINATURA 
													?(GregorianCalendar)contrato.getDtAssinatura() 
													:(GregorianCalendar)contrato.getDtInicioVigencia();
				venc = venc==null ? (GregorianCalendar)dtVencimento.clone() : venc;
				if(!lgIgnorarDiasNaoUteis){
					while(venc.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY 
							|| venc.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY 
							|| FeriadoServices.isFeriado(venc, connection) 
					){
						venc.add(Calendar.DATE, 1);
					}
				}
				/*
				 *  Calcula numero do documento
				 */
				String nrDocumento    = prefixDocumento!=null ? prefixDocumento+"-"+(new DecimalFormat("000").format(i+1)) : null;
				String idContaReceber = prefixDocumento!=null ? prefixDocumento.replaceAll("[/]","").replaceAll("[-]","")+(new DecimalFormat("000").format(i+1)) : null;
				String nrReferencia   = "";
				if(tpFrequencia == QUANTIDADE_FIXA)
					nrReferencia = (i+1)+"/"+(qtParcelas+1);
				else if(tpFrequencia == MENSAL)	{
					int nrMes = dtVencimento.get(Calendar.MONTH);
					int nrAno = dtVencimento.get(Calendar.YEAR);
					if(dtVencimento.get(Calendar.DAY_OF_MONTH)<28)	{
						nrMes = nrMes==0 ? 11 : nrMes-1;
						nrAno = nrMes==0 ? nrAno-1 : nrAno;
					}
					nrReferencia = Recursos.siglaMeses[nrMes]+"/"+nrAno;
				}
				/*
				 *  Calcula valor de amortizacao e juros quando for tabela PRICE ou SAC
				 */
				Double vlAmortizacao = vlConta;
				Double vlJuros = 0.0;
				if(tpAmortizacao == ContratoServices.PRICE)	{
					vlJuros = vlSaldoDevedor * prJuros / 100;
					vlAmortizacao = vlConta - vlJuros - vlIofPorParcela;
				}
				vlSaldoDevedor -= vlAmortizacao;
				/*
				 *  Cria conta a receber
				 */
				int nrParcela = tpFrequencia != QUANTIDADE_FIXA && tpForeignKey==OF_OUTRA_CONTA ? 0 : i;
				int qtParc = tpFrequencia != QUANTIDADE_FIXA && tpForeignKey==OF_OUTRA_CONTA ? 0 : qtParcelas+1;
				ContaReceber conta = new ContaReceber(0,cdPessoa, cdEmpresa,
						                              (tpForeignKey==OF_CONTRATO ? cdForeignKey : 0) /*cdContrato*/,
						                              (tpForeignKey==OF_OUTRA_CONTA ? cdForeignKey : 0)  /*cdContaOrigem*/,
						                              (tpForeignKey==OF_DOCUMENTO_SAIDA ? cdForeignKey : 0) /*cdDocumentoSaida*/,
						                              cdContaCarteira,cdConta,0 /*cdFrete*/,
						                              nrDocumento==null ? "" : nrDocumento.trim().length()>15 ? nrDocumento.trim().substring(0, 15) : nrDocumento.trim(),
						                              idContaReceber,nrParcela,
						                              nrReferencia==null ? "" : nrReferencia.trim().length()>15 ? nrReferencia.trim().substring(0, 15) : nrReferencia.trim(),
						                              cdTipoDocumento,dsHistorico,
						                              venc, dtEmissao, null /*dtRecebimento*/, null /*dtProrrogacao*/, vlConta,
						                              i<=0 ? 0 : tpDesconto==ContratoServices.DESC_PERCENTUAL ? (prDesconto / (double)100) * vlParcela :
						                              vlDesconto/qtParcelas /*vlAbatimento*/,0.0d /*vlAcrescimo*/,0.0d /*vlRecebido*/,ST_EM_ABERTO,tpFrequencia,
						                              qtParc,tpContaReceber!=-1 ? tpContaReceber : i==0 ? TP_TAXA_ADESAO : TP_PARCELA /*tpContaReceber*/,
						                              cdNegociacao /*cdNegociacao*/,null /*txtObservacao*/,0 /*cdPlanoPagamento*/,0 /*cdFormaPagamento*/,
						                              new GregorianCalendar(),venc, 0/*cdTurno*/, 0.0d/*prJuros*/, 0.0d/*prMulta*/, 0/*lgProtesto*/);

				/*
				 *  Gera o nosso numero e se for carteira com registro cria o registro para ser enviado na proxima remessa
				 */
				boolean registrarCobranca = simulateParcelas ? false : gerarNossoNumeroAndRegistro(conta, connection);
				int cdContaReceber = simulateParcelas ? 0 : ContaReceberDAO.insert(conta, connection);
				if (cdContaReceber <= 0 && !simulateParcelas) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar incluir conta gerada a partir da conta inicial! [ERRO:"+cdContaReceber+"]");
				}
				conta.setCdContaReceber(cdContaReceber);
				contas.add(conta);

				/*
				 *  Salva categorias
				 */
				if (!simulateParcelas) {
					rsmCategorias.beforeFirst();
					while(rsmCategorias.next())	{
						int ret = ContaReceberCategoriaDAO.insert(new ContaReceberCategoria(conta.getCdContaReceber(),
								                                  rsmCategorias.getInt("cd_categoria_economica"), rsmCategorias.getDouble("vl_conta_categoria"), rsmCategorias.getInt("cd_centro_custo")), connection);
						if (ret <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao salvar classificaï¿½ï¿½o da conta gerada! [ERRO: "+ret+"]");
						}
					}
					/*
					 *  se for carteira com registro cria o registro para ser enviado na proxima remessa
					 */
					if(registrarCobranca)	{
						int ret = registrarCobranca(conta, false, connection);
						if (ret <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao gerar registro de cobranï¿½a na conta gerada! [ERRO: "+ret+"]");
						}
					}
					/*
					 *  Salva a composicao da conta (Eventos Financeiros)
					 */
					if(tpAmortizacao > 0)	{
						int ret = ContaReceberEventoDAO.insert(new ContaReceberEvento(conta.getCdContaReceber(),cdEventoCapital /*cdEventoFinanceiro*/,
							    vlAmortizacao.floatValue() /*vlEventoFinanceiro*/,0 /*cdContrato*/,0 /*cdPessoa*/,
							    0 /*cdContaReceberEvento*/,ContaReceberEventoServices.ST_ATIVO /*stEvento*/), connection);
						// Capital
						if (ret <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao salvar composição de conta gerada(Capital)! [ERRO: "+ret+"]");
						}
						// Juros
						if(vlJuros>0)	{
							if (ContaReceberEventoDAO.insert(new ContaReceberEvento(conta.getCdContaReceber(),
									cdEventoJuros /*cdEventoFinanceiro*/,
									vlJuros.floatValue() /*vlEventoFinanceiro*/,
									0 /*cdContrato*/,
									0 /*cdPessoa*/,
									0 /*cdContaReceberEvento*/,
									ContaReceberEventoServices.ST_ATIVO /*stEvento*/), connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return new Result(-1, "Erro ao salvar composição de conta gerada(Juros)! [ERRO: "+ret+"]");
							}
						}
						// IOF
						if(vlIofPorParcela>0)
							if (ContaReceberEventoDAO.insert(new ContaReceberEvento(conta.getCdContaReceber(),
									cdEventoIof /*cdEventoFinanceiro*/,
									vlIofPorParcela.floatValue() /*vlEventoFinanceiro*/,
									0 /*cdContrato*/,
									0 /*cdPessoa*/,
									0 /*cdContaReceberEvento*/,
									ContaReceberEventoServices.ST_ATIVO /*stEvento*/), connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return new Result(-1, "Erro ao salvar composiÃ§Ã£o de conta gerada(IOF)! [ERRO: "+ret+"]");

							}
					}
				}

				/*
				 *  Calcula o proximo vencimento
				 */
				dtVencimento = (tpConfigVencDefault==VENC_ADESAO_PARCELA1_IGUAIS || tpVencAdesao!=ADESAO_VENC_PRIMEIRA) && i==0 ?
						(GregorianCalendar)dtVencimentoPrimeira.clone() : ContaPagarServices.getProximoVencimento(dtVencimento, tpFrequencia, nrDiaVencimento);
			}

			if (!simulateParcelas && isConnectionNull)
				connection.commit();
			Result result = new Result(1);
			result.addObject("contas", contas);
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (simulateParcelas && isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar gerar contas a receber!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static String getCodigoBarras(int cdConta, int cdContaCarteira, String nrNossoNumero, Double vlDocumento,
			GregorianCalendar dtVencimento, GregorianCalendar dtEmissao)	{
		return getCodigoBarras(cdConta, cdContaCarteira, nrNossoNumero, vlDocumento, dtVencimento, dtEmissao, null);
	}

	public static String getCodigoBarras(int cdConta, int cdContaCarteira, String nrNossoNumero, Double vlDocumento, GregorianCalendar dtVencimento, GregorianCalendar dtEmissao, Connection connection)	{
		boolean isConnectionNull = connection==null;
		try	{
			connection = isConnectionNull ? Conexao.conectar() : connection;
			ResultSet rs = connection.prepareStatement("SELECT A.*, B.tp_operacao, C.nr_agencia, D.nr_banco " +
					                                "FROM adm_conta_carteira A " +
					                                "JOIN adm_conta_financeira B ON (B.cd_conta = "+cdConta+")" +
					                                "LEFT OUTER JOIN grl_agencia C ON (B.cd_agencia = C.cd_agencia) " +
					                                "LEFT OUTER JOIN grl_banco   D ON (C.cd_banco = D.cd_banco) " +
					                                "WHERE A.cd_conta          = "+cdConta+
					                                "  AND A.cd_conta_carteira = "+cdContaCarteira).executeQuery();

			if(rs.next())	{
				return getCodigoBarras(rs.getString("nr_banco"), rs.getString("nr_agencia"), rs.getString("nr_conta"), rs.getInt("tp_operacao"),
						               rs.getString("nr_carteira"), rs.getString("nr_servico"), rs.getString("nr_cedente"), rs.getString("nr_convenio"),
						               nrNossoNumero, vlDocumento, dtVencimento, dtEmissao, rs.getString("txt_campo_livre"));
			}
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
		return "";
	}

	public static String getCodigoBarras(String nrBanco, String nrAgencia, String nrConta, int tpOperacaoConta, String nrCarteira, String nrServico,
			String nrCedente, String nrConvenio, String nrNossoNumero,
			Double vlDocumento, GregorianCalendar dtVencimento, GregorianCalendar dtEmissao, String txtCampoLivre)
	{
		try	{
			if (dtVencimento==null)
				return "";
			String cdMoeda = "9";
			nrNossoNumero = nrNossoNumero==null? "" : nrNossoNumero.replaceAll("[\\D]","").trim();
			nrCarteira    = nrCarteira==null   ? "" : nrCarteira.replaceAll("[\\D]","").trim();
			nrAgencia     = nrAgencia==null    ? "" : nrAgencia.replaceAll("[\\D]","").trim();
			nrCedente     = nrCedente==null    ? "" : nrCedente.replaceAll("[\\D]","").trim();
			nrBanco       = nrBanco==null      ? "" : Util.fill(nrBanco.replaceAll("[\\D]","").trim(), 3, '0', 'E');
			int vlFatorVencimento = Math.round((dtVencimento.getTimeInMillis() - (new GregorianCalendar(1997, GregorianCalendar.OCTOBER, 7)).getTimeInMillis()) / (24*60*60*1000));
			String dsCampoLivre = "";
			ResultSetMap rsmCampoLivre = ContaCarteiraServices.getCampoLivre(txtCampoLivre);

			while(rsmCampoLivre.next())	{
				int nrInicio  = rsmCampoLivre.getInt("nr_inicio_campo");
				int qtDigitos = rsmCampoLivre.getInt("qt_digitos");
				String dsInformacao = "";
				switch(rsmCampoLivre.getInt("tp_campo"))	{
					case ContaCarteiraServices._NR_AGENCIA:
						dsInformacao = nrAgencia;
						break;
					case ContaCarteiraServices._NR_CARTEIRA:
						dsInformacao = nrCarteira;
						break;
					case ContaCarteiraServices._NR_CEDENTE:
						dsInformacao = nrCedente;
						break;
					case ContaCarteiraServices._NOSSO_NUMERO:
						dsInformacao = nrNossoNumero;
						break;
					case ContaCarteiraServices._FIXO:
						dsInformacao = String.valueOf(qtDigitos);
						qtDigitos = dsInformacao.length();
						break;
					case ContaCarteiraServices._NR_CONTA:
						dsInformacao = nrConta;
						break;
					case ContaCarteiraServices._ANO_EMISSAO:
						dsInformacao = Util.formatDate(dtEmissao, Util.fill("", qtDigitos, 'y', 'E'));
						break;
					case ContaCarteiraServices._NR_SERVICO:
						dsInformacao = nrServico;
						break;
					case ContaCarteiraServices._NR_CONVENIO:
						dsInformacao = nrConvenio;
						break;
					case ContaCarteiraServices._DV_CAMPO_LIVRE:
						dsInformacao = Util.getDvMod11(dsCampoLivre, "0", "0", 9, 2, false);
						qtDigitos 	 = 1;
						break;
				}
				if(dsInformacao.length()>0 && nrInicio>0 && dsInformacao.length()>nrInicio)
					dsCampoLivre += Util.fill(dsInformacao.substring(nrInicio), qtDigitos, '0','E');
				else
					dsCampoLivre += Util.fill(dsInformacao, qtDigitos, '0','E');
			}

			// Montando codigo de barras
			String nrCodigoBarras = nrBanco + cdMoeda + Util.fillNum(vlFatorVencimento, 4) +
		                            Util.fillNum(Math.round(vlDocumento.floatValue()*100), 10) + dsCampoLivre;
			// Calculo DIV 11
			int nrDv = 0;
			try	{
				nrDv = 11-Integer.parseInt(Util.getDvMod11(nrCodigoBarras, "1", "1", 9, 2, true));
			}
			catch(Exception e)	{
			}

			if(nrDv>=10 || nrDv==0)
				nrDv = 1;
			// Retorno
			if(nrCodigoBarras.length()==43)
				return nrCodigoBarras.substring(0, 4)+nrDv+nrCodigoBarras.substring(4, 43);
			else
				return "";
		}
		catch(Exception e)	{
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return "";
		}
	}

	public static String getLinhaDigitavel(String cb)	{
		if(cb.length()<44)
			return "";
		String dsCampo1 = cb.substring(0,4)+cb.substring(19,20)+"."+cb.substring(20,24)+Util.getDvMod10(cb.substring(0,4)+cb.substring(19,24));
		String dsCampo2 = cb.substring(24,29)+"."+cb.substring(29,34)+Util.getDvMod10(cb.substring(24,34));
		String dsCampo3 = cb.substring(34,39)+"."+cb.substring(39,44)+Util.getDvMod10(cb.substring(34,44));
		// Retorno
		return dsCampo1+" "+dsCampo2+" "+dsCampo3+" "+cb.substring(4,5)+" "+(cb.substring(5,19));
	}

	public static String getDvNossoNumero(String nrNossoNumero, int tpDigito, int nrBaseInicial, int nrBaseFinal)	{
		if(nrNossoNumero==null)
			return "";
		String dv = "";
		try	{
			if(tpDigito==ContaCarteiraServices._MOD10)
				dv = String.valueOf(Util.getDvMod10(nrNossoNumero));
			else if(tpDigito==ContaCarteiraServices._MOD11_V1)
				dv = String.valueOf(nrBaseInicial+nrBaseFinal<=0 ? Util.getDvMod11(nrNossoNumero) : Util.getDvMod11(nrNossoNumero, nrBaseInicial, nrBaseFinal));
			else if(tpDigito==ContaCarteiraServices._MOD11_V2)
				dv = String.valueOf(nrBaseInicial+nrBaseFinal<=0 ? Util.getDvMod11(nrNossoNumero, "0", "0", 9, 2, true) : Util.getDvMod11(nrNossoNumero, "0", "0", nrBaseInicial, nrBaseFinal, false));
			else if(tpDigito==ContaCarteiraServices._DV_NONE)
				dv = "";
			else
				dv = Util.getDvMod11(nrNossoNumero)+Util.getDvMod10(nrNossoNumero)+Util.getDvMod11(nrNossoNumero);
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return "";
		}
		return dv;
	}

	public static int getCdContaReceberOfNossoNumero(int cdEmpresa, String nrNossoNumero, Connection connect)	{
		boolean isConnNull = connect==null;
		if(isConnNull)
			connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_conta_receber FROM adm_conta_receber " +
					                                           "WHERE id_conta_receber = ? " +
					                                           "  AND cd_empresa = "+cdEmpresa);
			pstmt.setString(1, nrNossoNumero);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return rs.getInt("cd_conta_receber");
			else
				return 0;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberServices.processLiquidacao: " +  e);
			return -1;
		}
		finally{
			if(isConnNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaCarteira getContaCarteiraPdv(int cdEmpresa)	{
		return getContaCarteiraPdv(cdEmpresa, null);
	}

	public static ContaCarteira getContaCarteiraPdv(int cdEmpresa, Connection connection)	{
		boolean isConnectionNull = connection==null;
		try	{
			if (isConnectionNull)
				connection = Conexao.conectar();
			String valor = ParametroServices.getValorOfParametro("CD_CARTEIRA_PDV", cdEmpresa, connection);
			if (valor!=null && !valor.trim().equals("") && valor.trim().length()>1) {
				int cdConta = Integer.parseInt(valor.trim().substring(0, valor.trim().indexOf("-")));
				int cdContaCarteira = Integer.parseInt(valor.trim().substring(valor.trim().indexOf("-") + 1));
				return ContaCarteiraDAO.get(cdContaCarteira, cdConta, connection);
			}
			else
				return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberServices.getContaCarteiraPdv: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	
	public static int delete(ArrayList<Integer> codigos){
		int retorno = 1;
		try{
			Connection connect = Conexao.conectar();
			connect.setAutoCommit(false);
			for(int i = 0; i < codigos.size(); i++){
				retorno = delete(codigos.get(i), true, false, connect);
			}
			connect.commit();
			return retorno;
		}
		
		catch(Exception e){System.out.println("Erro em delete: " + e); return -1;}
	}
	
	public static Result remove( Integer contas[] ){
		return remove( contas, null );
	}
	
	public static Result remove(Integer contas[], Connection connect){
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			Result r;
			for( int i=0;i<contas.length;i++ ){
				r = remove(contas[i], true, true, connect);
				if( r.getCode() <= 0 ){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao excluir contas!");
				}
			}
			connect.commit();
			return new Result(1, "Contas a Receber excluï¿½das com sucesso!");
			
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
	
	public static Result remove(int cdContaReceber){
		return remove(cdContaReceber, false, false, null);
	}
	
	public static Result remove(int cdContaReceber, boolean cascade){
		return remove(cdContaReceber, cascade, false, null);
	}
	
	public static Result remove(int cdContaReceber, boolean deleteChild, boolean deleteNegociacao){
		int retorno = delete(cdContaReceber, deleteChild, deleteNegociacao);
		return new Result(retorno, (retorno<=0)?"Erro ao excluir...":"Conta excluida com sucesso...");
	}

	public static Result remove(int cdContaReceber, boolean cascade, boolean deleteNegociacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 1;
			
			ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber, connect);
			if (deleteNegociacao && contaReceber.getCdNegociacao()>0) {
				int ret = ContratoNegociacaoServices.delete(contaReceber.getCdContrato(), contaReceber.getCdNegociacao(), connect);
				if (ret<0)
					throw new Exception("Erro ao exluir negociação geradora da conta atual!");
				else {
					if (isConnectionNull)
						connect.commit();
					return new Result(1);
				}
			}
			
			//Verifica se a conta possui movimentacoes
			ResultSetMap movimentoContaReceber = MovimentoContaReceberServices.getRecebimentoOfContaReceber( contaReceber.getCdContaReceber(), connect);
			if( movimentoContaReceber.next() ){
				return new Result(-3, "Conta não pode ser excluída, existem movimentações associadas!");
			}
			// Tenta excluir parcelas
			if(cascade)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM adm_conta_receber WHERE cd_conta_origem = "+cdContaReceber).executeQuery();
				while(rs.next())
					if (delete(rs.getInt("cd_conta_receber"), cascade, false, connect) < 1)
						throw new Exception("Erro ao tentar excluir contas geradas pela conta atual!");
			}
			// tenta apagar os eventos relacionados a Conta a Receber
			if (ContaReceberEventoServices.deleteAll(cdContaReceber, connect) <= 0)
				throw new Exception("Erro ao tentar excluir eventos!");

			// Tenta apagar as classificacoes financeiras relacionadas a Conta a Receber
			if (ContaReceberCategoriaServices.deleteAll(cdContaReceber, connect) <= 0)
				throw new Exception("Erro ao tentar excluir a classificação em categorias!");

			// tenta apagar os arquivos de registro relacionados a conta a receber
			if (ArquivoRegistroServices.deleteAllOfContaReceber(cdContaReceber, connect) <= 0)
				throw new Exception("Erro ao tentar excluir os registros em arquivos!");

			// tenta apagar os titulos de credito registro relacionados a conta a receber
			if (TituloCreditoServices.deleteAllOfContaReceber(cdContaReceber, connect) < 0)
				throw new Exception("Erro ao tentar excluir títulos de crédito!");
			
			// tenta apagar as contas factoring relacionados a conta a receber
			if (ContaFactoringServices.deleteAllOfContaReceber(cdContaReceber, connect) < 0)
				throw new Exception("Erro ao tentar excluir contas factoring!");
			
			//Cancelando faturamento de processo financeiro
			if( ProcessoFinanceiroServices.getByContaReceber(cdContaReceber, connect) != null ){
				Result r = ProcessoFinanceiroServices.cancelarFaturamentoContaReceber(cdContaReceber, connect);
				if (r.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(r.getCode(), r.getMessage());
				}
			}
			
			// tenta apagar arquivos de Conta a Receber
			if (ContaReceberArquivoServices.removeAll(cdContaReceber, connect).getCode() <= 0)
				throw new Exception("Erro ao tentar excluir arquivos da conta a receber!");
						
			// tenta apagar Conta a Receber
			if (ContaReceberDAO.delete(cdContaReceber, connect) <= 0)
				throw new Exception("Erro ao tentar excluir conta a receber!");
			
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro esta vinculado a outros e não pode ser excluído!");
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
	
	
	// Exclui a conta a receber e TODOS os eventos financeiros relacionados
	public static int delete(int cdContaReceber){
		return delete(cdContaReceber, true, false, null);
	}

	public static int delete(int cdContaReceber, boolean deleteChild) {
		return delete(cdContaReceber, deleteChild, false, null);
	}

	// Exclui a conta a receber e TODOS os eventos financeiros relacionados
	public static int delete(int cdContaReceber, boolean deleteChild, boolean deleteNegociacao){
		return delete(cdContaReceber, deleteChild, deleteNegociacao, null);
	}

	public static int delete(int cdContaReceber, boolean deleteChild, boolean deleteNegociacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber, connect);
			if (deleteNegociacao && contaReceber.getCdNegociacao()>0) {
				int ret = ContratoNegociacaoServices.delete(contaReceber.getCdContrato(), contaReceber.getCdNegociacao(), connect);
				if (ret<0)
					throw new Exception("Erro ao exluir negociaï¿½ï¿½o geradora da conta atual!");
				else {
					if (isConnectionNull)
						connect.commit();
					return 1;
				}
			}

			// Tenta excluir
			if(deleteChild)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM adm_conta_receber WHERE cd_conta_origem = "+cdContaReceber).executeQuery();
				while(rs.next())
					if (delete(rs.getInt("cd_conta_receber"), deleteChild, false, connect) < 1)
						throw new Exception("Erro ao tentar excluir contas geradas pela conta atual!");
			}
			// tenta apagar os eventos relacionados ï¿½ Conta a Receber
			if (ContaReceberEventoServices.deleteAll(cdContaReceber, connect) <= 0)
				throw new Exception("Erro ao tentar excluir eventos!");

			// Tenta apagar as classificaï¿½ï¿½es financeiras relacionadas ï¿½ Conta a Receber
			if (ContaReceberCategoriaServices.deleteAll(cdContaReceber, connect) <= 0)
				throw new Exception("Erro ao tentar excluir a classificaï¿½ï¿½o em categorias!");

			// tenta apagar os arquivos de registro relacionados ï¿½ conta a receber
			if (ArquivoRegistroServices.deleteAllOfContaReceber(cdContaReceber, connect) <= 0)
				throw new Exception("Erro ao tentar excluir os registros em arquivos!");

			// tenta apagar os tï¿½tulos de crï¿½dito registro relacionados ï¿½ conta a receber
			if (TituloCreditoServices.deleteAllOfContaReceber(cdContaReceber, connect) < 0)
				throw new Exception("Erro ao tentar excluir tï¿½tulos de crï¿½dito!");
			
			// tenta apagar as contas factoring relacionados ï¿½ conta a receber
			if (ContaFactoringServices.deleteAllOfContaReceber(cdContaReceber, connect) < 0)
				throw new Exception("Erro ao tentar excluir contas factoring!");
			// tenta apagar Conta a Receber
			if (ContaReceberDAO.delete(cdContaReceber, connect) <= 0)
				throw new Exception("Erro ao tentar excluir conta a receber!");
			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			System.err.println("Erro! ContaReceberServices.delete: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static HashMap<String,Object> setCancelada(int cdContaReceber) {
		return setCancelada(cdContaReceber, null);
	}

	public static HashMap<String,Object> setCancelada(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		String mensagem = "";
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM adm_movimento_conta_receber " +
	                "WHERE cd_conta_receber = ?");
			pstmt.setInt(1, cdContaReceber);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				mensagem = "Erro! ContaReceberServices.cancelar: parcela tem movimentos relacionados";
				System.err.println(mensagem);
				retorno.put("ErrorCode", new Integer(-1));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}

			pstmt = connect.prepareStatement("UPDATE adm_conta_receber " +
					"SET st_conta=? " +
					"WHERE cd_conta_receber=?");
			pstmt.setInt(1,ST_CANCELADA);
			pstmt.setInt(2,cdContaReceber);
			pstmt.execute();

			if (TituloCreditoServices.setSituacaoTitulosOfConta(cdContaReceber, ST_CANCELADA, null, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				retorno.put("ErrorCode", new Integer(-2));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}

			if (isConnectionNull)
				connect.commit();

			retorno.put("ErrorCode", new Integer(1));
			retorno.put("ErrorMsg", "");
			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			mensagem = "Erro! ContaReceberServices.cancelar: " +  e;
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
	
	public static HashMap<String,Object> setDevolvido(int cdContaReceber) {
		return setDevolvido(cdContaReceber, null);
	}

	public static HashMap<String,Object> setDevolvido(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		String mensagem = "";
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			float vlMulta = 0;
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM adm_movimento_conta_receber " +
	                "WHERE cd_conta_receber = ?");
			pstmt.setInt(1, cdContaReceber);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				mensagem = "Erro! ContaReceberServices.devolver: parcela tem movimentos relacionados";
				System.err.println(mensagem);
				retorno.put("ErrorCode", new Integer(-1));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}
			ContaReceber conta = ContaReceberDAO.get(cdContaReceber, connect);

			pstmt = connect.prepareStatement("UPDATE adm_conta_receber " +
					"SET st_conta=? " +
					"WHERE cd_conta_receber=?");
			conta.setStConta(ST_EM_ABERTO);
			pstmt.setInt(1,conta.getStConta());
			pstmt.setInt(2,cdContaReceber);
			pstmt.execute();

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_conta_receber", Integer.toString(conta.getCdContaReceber()), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmTitulo = TituloCreditoDAO.find(criterios, connect);
			TituloCredito titulo = null;
			if(rsmTitulo.next()){
				titulo = TituloCreditoDAO.get(rsmTitulo.getInt("cd_titulo_credito"), connect);
			}
			if(titulo != null){
				if (TituloCreditoServices.setSituacaoTitulosOfConta(cdContaReceber, ((titulo.getStTitulo() == TituloCreditoServices.stDEVOLVIDA_1X)?8/*stDevolvido2x*/:7/*stDevolvido1x*/), null, connect) <= 0) {
					mensagem = "Erro! ContaReceberServices.devolver: Erro ao mudar situacao do titulo da conta";
					if(isConnectionNull)
						Conexao.rollback(connect);
					retorno.put("ErrorCode", new Integer(-2));
					retorno.put("ErrorMsg", mensagem);
					return retorno;
				}
			}
			if (isConnectionNull)
				connect.commit();
			
			//Busca da Conta a pagar relacionada a essa conta a receber
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_conta_receber_antecipada", Integer.toString(conta.getCdContaReceber()), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmContaFac = ContaFactoringDAO.find(criterios, connect);
			if(rsmContaFac.next()){
				ContaPagar contaPag = ContaPagarDAO.get(rsmContaFac.getInt("cd_conta_pagar"), connect);
				//Bloqueia o cliente caso a situaï¿½ï¿½o de titulo anterior for devolvida 1x, e se o parametro de bloqueio de cliente apos duas devoluï¿½ï¿½es estiver ativado
				Pessoa cliente = PessoaDAO.get(contaPag.getCdPessoa(), connect);
				if(titulo.getStTitulo() == TituloCreditoServices.stDEVOLVIDA_1X && ParametroServices.getValorOfParametroAsInteger("LG_BLOQUEAR_CLIENTE_DEVOLUCAO", 0, contaPag.getCdEmpresa())==1){
					cliente.setStCadastro(PessoaServices.ST_BLOQUEADO);
					if(PessoaDAO.update(cliente, connect) <=0){
						mensagem = "Erro! ContaReceberServices.devolver: Erro ao atualizar status do cliente";
						Conexao.rollback(connect);
						retorno.put("ErrorCode", new Integer(-2));
						retorno.put("ErrorMsg", mensagem);
						return retorno;
					}
				}
				
				Cliente clienteFac = ClienteDAO.get(contaPag.getCdEmpresa(), cliente.getCdPessoa());
				if(clienteFac == null){
					mensagem = "Erro! ContaReceberServices.devolver: Erro ao ao buscar cliente";
					Conexao.rollback(connect);
					retorno.put("ErrorCode", new Integer(-2));
					retorno.put("ErrorMsg", mensagem);
					return retorno;
				}
				vlMulta = clienteFac.getVlTaxaDevolucaoFactoring();
				if(vlMulta == 0)
					vlMulta = ParametroServices.getValorOfParametroAsFloat("VL_TAXA_DEVOLUCAO", 0, clienteFac.getCdEmpresa());
				
			}
			
			//Bloqueia o emitente caso a situaï¿½ï¿½o de titulo anterior for devolvida 1x, e se o parametro de bloqueio de emitente apos duas devoluï¿½ï¿½es estiver ativado
			if(titulo.getStTitulo() == TituloCreditoServices.stDEVOLVIDA_1X && ParametroServices.getValorOfParametroAsInteger("LG_BLOQUEAR_EMITENTE_DEVOLUCAO", 0, conta.getCdEmpresa())==1){
				Pessoa emitente = PessoaDAO.get(conta.getCdPessoa(), connect);
				emitente.setStCadastro(PessoaServices.ST_BLOQUEADO);
				if(PessoaDAO.update(emitente, connect) <=0){
					mensagem = "Erro! ContaReceberServices.devolver: Erro ao atualizar status do emitente";
					Conexao.rollback(connect);
					retorno.put("ErrorCode", new Integer(-2));
					retorno.put("ErrorMsg", mensagem);
					return retorno;
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			retorno.put("vlMulta", vlMulta);
			retorno.put("vez", (titulo.getStTitulo() == TituloCreditoServices.stDEVOLVIDA_1X ? TituloCreditoServices.stDEVOLVIDA_2X : TituloCreditoServices.stDEVOLVIDA_1X));
			retorno.put("ErrorCode", new Integer(1));
			retorno.put("ErrorMsg", "");
			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			mensagem = "Erro! ContaReceberServices.devolvido: " +  e;
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

	public static HashMap<String,Object> setPerda(int cdContaReceber) {
		return setPerda(cdContaReceber, null);
	}

	public static HashMap<String,Object> setPerda(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		String mensagem = "";
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM adm_movimento_conta_receber " +
	                "WHERE cd_conta_receber = ?");
			pstmt.setInt(1, cdContaReceber);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				mensagem = "Erro! ContaReceberServices.perda: parcela tem movimentos relacionados";
				System.err.println(mensagem);
				retorno.put("ErrorCode", new Integer(-1));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}
			ContaReceber conta = ContaReceberDAO.get(cdContaReceber, connect);

			
			pstmt = connect.prepareStatement("UPDATE adm_conta_receber " +
					"SET st_conta=? " +
					"WHERE cd_conta_receber=?");
			conta.setStConta(ST_PERDA);
			pstmt.setInt(1,conta.getStConta());
			pstmt.setInt(2,cdContaReceber);
			pstmt.execute();

			if (TituloCreditoServices.setSituacaoTitulosOfConta(cdContaReceber, ST_PERDA, null, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				retorno.put("ErrorCode", new Integer(-2));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}

			if (isConnectionNull)
				connect.commit();
			
			retorno.put("ErrorCode", new Integer(1));
			retorno.put("ErrorMsg", "");
			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			mensagem = "Erro! ContaReceberServices.devolvido: " +  e;
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
	
	public static HashMap<String,Object> setRecuperar(int cdContaReceber) {
		return setRecuperar(cdContaReceber, null);
	}

	public static HashMap<String,Object> setRecuperar(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		String mensagem = "";
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM adm_movimento_conta_receber " +
	                "WHERE cd_conta_receber = ?");
			pstmt.setInt(1, cdContaReceber);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				mensagem = "Erro! ContaReceberServices.perda: parcela tem movimentos relacionados";
				System.err.println(mensagem);
				retorno.put("ErrorCode", new Integer(-1));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}
			ContaReceber conta = ContaReceberDAO.get(cdContaReceber, connect);

			
			pstmt = connect.prepareStatement("UPDATE adm_conta_receber " +
					"SET st_conta=? " +
					"WHERE cd_conta_receber=?");
			conta.setStConta(ST_EM_ABERTO);
			pstmt.setInt(1,conta.getStConta());
			pstmt.setInt(2,cdContaReceber);
			pstmt.execute();

			if (TituloCreditoServices.setSituacaoTitulosOfConta(cdContaReceber, ST_EM_ABERTO, null, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				retorno.put("ErrorCode", new Integer(-2));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}

			if (isConnectionNull)
				connect.commit();
			
			retorno.put("ErrorCode", new Integer(1));
			retorno.put("ErrorMsg", "");
			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			mensagem = "Erro! ContaReceberServices.devolvido: " +  e;
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
	
	public static HashMap<String,Object> setCompensado(int cdContaReceber) {
		return setCompensado(cdContaReceber, null);
	}

	public static HashMap<String,Object> setCompensado(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		String mensagem = "";
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			if (TituloCreditoServices.setSituacaoTitulosOfConta(cdContaReceber, 9/*stDESCONTADO*/, null, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				retorno.put("ErrorCode", new Integer(-2));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}

			if (isConnectionNull)
				connect.commit();
			
			retorno.put("ErrorCode", new Integer(1));
			retorno.put("ErrorMsg", "");
			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			mensagem = "Erro! ContaReceberServices.devolvido: " +  e;
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
	
	public static HashMap<String,Object> setProrrogado(int cdContaReceber, GregorianCalendar dtNovoVencimento) {
		return setProrrogado(cdContaReceber, dtNovoVencimento, null);
	}

	public static HashMap<String,Object> setProrrogado(int cdContaReceber, GregorianCalendar dtNovoVencimento, Connection connect){
		boolean isConnectionNull = connect==null;
		String mensagem = "";
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			// Busca da conta a receber
			ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber, connect);
			if(contaReceber==null){
				mensagem = "Erro: Conta a Receber nao identificada!";
				System.out.println(mensagem);
				Conexao.rollback(connect);
				retorno.put("ErrorCode", new Integer(-1));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}
			//Busca do Titulo de Credito relacionado a conta a receber
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_conta_receber", Integer.toString(cdContaReceber), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmTitulo = TituloCreditoDAO.find(criterios, connect);
			TituloCredito titulo = null;
			if(rsmTitulo.next()){
				titulo = TituloCreditoDAO.get(rsmTitulo.getInt("cd_titulo_credito"), connect);
			}
			if(titulo == null){
				mensagem = "Erro: Titulo nao identificado!";
				System.out.println(mensagem);
				Conexao.rollback(connect);
				retorno.put("ErrorCode", new Integer(-1));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}
			//Busca da Conta Factoring que relaciona essa conta a receber ï¿½ uma conta a pagar
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_conta_receber_antecipada", Integer.toString(cdContaReceber), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmContaFac = ContaFactoringDAO.find(criterios, connect);
			int cdContaFactoring = 0;
			if(rsmContaFac.next()){
				cdContaFactoring = rsmContaFac.getInt("cd_conta_factoring");
			}
			ContaFactoring contaFac = ContaFactoringDAO.get(cdContaFactoring, connect);
			if(contaFac == null){
				mensagem = "Erro: Conta Factoring nao identificada!";
				Conexao.rollback(connect);
				System.out.println(mensagem);
				retorno.put("ErrorCode", new Integer(-1));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}
			
			//Busca da Conta a pagar relacionada a conta factoring
			ContaPagar contaPagar = ContaPagarDAO.get(contaFac.getCdContaPagar(), connect);
			if(contaPagar == null){
				mensagem = "Erro: Conta a Pagar nao identificada!";
				Conexao.rollback(connect);
				System.out.println(mensagem);
				retorno.put("ErrorCode", new Integer(-1));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}
			//Busca do Cliente relacionado a troca do cheque
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("findCliente", "1", ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_pessoa", Integer.toString(contaPagar.getCdPessoa()), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmCliente = PessoaServices.find(criterios);
			Cliente cliente = null;
			if(rsmCliente.next()){
				cliente = ClienteDAO.get(rsmCliente.getInt("cd_empresa_trabalho"), rsmCliente.getInt("cd_pessoa"), connect);
			}
			if(cliente == null){
				mensagem = "Erro: Cliente nao identificado!";
				Conexao.rollback(connect);
				System.out.println(mensagem);
				Conexao.rollback(connect);
				retorno.put("ErrorCode", new Integer(-1));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}
			float prProrrogacao = cliente.getPrTaxaProrrogacaoFactoring();
			if(prProrrogacao == 0)
				prProrrogacao = ParametroServices.getValorOfParametroAsFloat("VL_TAXA_PRORROGACAO", 0, cliente.getCdEmpresa());
			if(prProrrogacao == 0){
				mensagem = "Erro: Parametro de Taxa de Juros nÃ£o configurado!";
				Conexao.rollback(connect);
				System.out.println(mensagem);
				Conexao.rollback(connect);
				retorno.put("ErrorCode", new Integer(-1));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}
			Double vlMulta;
			// Jï¿½ verifica se venceu em dia nï¿½o ï¿½til
			GregorianCalendar dtVencimento = contaReceber.getDtVencimento();
			while(dtVencimento.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || dtVencimento.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY || FeriadoServices.isFeriado(dtVencimento))
				dtVencimento.add(Calendar.DATE, 1);
			
			// Realiza cï¿½lculos identificando a multa pela diferenca de dias entre a data de vencimento atual e a nova
			Double vlAReceber  = contaReceber.getVlConta() - contaReceber.getVlAbatimento() + contaReceber.getVlAcrescimo();
			int diasAte = Math.round((dtNovoVencimento.getTimeInMillis() - dtVencimento.getTimeInMillis()) / 1000 / 3600 / 24);
			if(diasAte > 0)
				vlMulta = vlAReceber * prProrrogacao / 100 / 30 * diasAte;
			else{
				mensagem = "Erro: Nova data de vencimento Ã© menor do que a anterior!";
				Conexao.rollback(connect);
				System.out.println(mensagem);
				Conexao.rollback(connect);
				retorno.put("ErrorCode", new Integer(-1));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}
				
			//Incluir valor da multa nos acrescimos da conta a receber
			//Ao dar baixa na conta a receber incluir o valor do acrescimo e a categoria de muta por prorrogaï¿½ï¿½o
			//Alterar data de vencimento
			contaReceber.setDtVencimento(dtNovoVencimento);
			contaReceber.setVlAcrescimo(vlMulta);
			if(ContaReceberDAO.update(contaReceber, connect) <= 0){
				mensagem = "Erro: Ao atualizar conta a receber!";
				Conexao.rollback(connect);
				System.out.println(mensagem);
				Conexao.rollback(connect);
				retorno.put("ErrorCode", new Integer(-1));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}
						
			if (TituloCreditoServices.setSituacaoTitulosOfConta(cdContaReceber, ContaReceberServices.ST_EM_ABERTO, null, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				retorno.put("ErrorCode", new Integer(-2));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}

			if (isConnectionNull)
				connect.commit();
			
			retorno.put("vlAcrescimo", vlMulta);
			retorno.put("dtVencimento", Util.convCalendarString(dtNovoVencimento));
			retorno.put("ErrorCode", new Integer(1));
			retorno.put("ErrorMsg", "");
			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			mensagem = "Erro! ContaReceberServices.devolvido: " +  e;
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
	
	
	public static HashMap<String,Object> setResgatado(int cdContaReceber) {
		return setResgatado(cdContaReceber, null);
	}

	public static HashMap<String,Object> setResgatado(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		String mensagem = "";
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM adm_movimento_conta_receber " +
	                "WHERE cd_conta_receber = ?");
			pstmt.setInt(1, cdContaReceber);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				mensagem = "Erro! ContaReceberServices.perda: parcela tem movimentos relacionados";
				System.err.println(mensagem);
				retorno.put("ErrorCode", new Integer(-1));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}
			ContaReceber conta = ContaReceberDAO.get(cdContaReceber, connect);
			pstmt = connect.prepareStatement("UPDATE adm_conta_receber " +
					"SET st_conta=? " +
					"WHERE cd_conta_receber=?");
			conta.setStConta(ST_RECEBIDA);
			pstmt.setInt(1,conta.getStConta());
			pstmt.setInt(2,cdContaReceber);
			pstmt.execute();
			//
			if (TituloCreditoServices.setSituacaoTitulosOfConta(cdContaReceber, 10/*stRESGATADO*/, null, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				retorno.put("ErrorCode", new Integer(-2));
				retorno.put("ErrorMsg", mensagem);
				return retorno;
			}

			if (isConnectionNull)
				connect.commit();
			
			retorno.put("ErrorCode", new Integer(1));
			retorno.put("ErrorMsg", "");
			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			mensagem = "Erro! ContaReceberServices.devolvido: " +  e;
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
	
	public static ResultSetMap atualizarRegistroContaReceber(int cdContaReceber, int cdMovimentoConta, int cdEmpresa){
		Connection connect = null;
		
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			ContaReceber conta = ContaReceberDAO.get(cdContaReceber);
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_conta_receber " +
	                                                                     "WHERE cd_conta_receber = "+cdContaReceber).executeQuery());
			
			rsm.next();
			rsm.setValueToField("NM_PESSOA", ((conta.getCdPessoa() > 0) ? PessoaDAO.get(conta.getCdPessoa()).getNmPessoa() : ""));
			rsm.setValueToField("DT_DEPOSITO", (conta.getDtRecebimento() != null) ? conta.getDtRecebimento() : Util.getDataAtual());
			rsm.setValueToField("DT_MOVIMENTO", (conta.getDtRecebimento() != null) ? conta.getDtRecebimento() : Util.getDataAtual());
			rsm.setValueToField("NM_TIPO_DOCUMENTO", TipoDocumentoDAO.get(conta.getCdTipoDocumento()).getNmTipoDocumento());
			
			// Categorias
			int cdCategoriaMulta    = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_MULTA_RECEBIDA", 0, cdEmpresa);
			int cdCategoriaJuros    = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_JUROS_RECEBIDO", 0, cdEmpresa);
			int cdCategoriaDesconto = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_DESCONTO_CONCEDIDO", 0, cdEmpresa);
			String dsCategoriaMulta = "";
			String nrCategoriaMulta = "";
			if(cdCategoriaMulta>0)	{
				CategoriaEconomica categoria = CategoriaEconomicaDAO.get(cdCategoriaMulta);
				dsCategoriaMulta = categoria==null ? "" : categoria.getNmCategoriaEconomica();
				nrCategoriaMulta = categoria==null ? "" : categoria.getNrCategoriaEconomica();
			}
			String dsCategoriaJuros = "";
			String nrCategoriaJuros = "";
			if(cdCategoriaJuros>0)	{
				CategoriaEconomica categoria = CategoriaEconomicaDAO.get(cdCategoriaJuros);
				dsCategoriaJuros = categoria==null ? "" : categoria.getNmCategoriaEconomica();
				nrCategoriaJuros = categoria==null ? "" : categoria.getNrCategoriaEconomica();
			}
			String dsCategoriaDesconto = "";
			String nrCategoriaDesconto = "";
			if(cdCategoriaDesconto>0)	{
				CategoriaEconomica categoria = CategoriaEconomicaDAO.get(cdCategoriaDesconto);
				dsCategoriaDesconto = categoria==null ? "" : categoria.getNmCategoriaEconomica();
				nrCategoriaDesconto = categoria==null ? "" : categoria.getNrCategoriaEconomica();
			}
			
			Object[] valores = calcJurosMulta(cdContaReceber);
			float vlMulta = Float.parseFloat(valores[0].toString());
			float vlJuros = Float.parseFloat(valores[1].toString());
			
			rsm.setValueToField("VL_MULTA", vlMulta);
			rsm.setValueToField("NM_CATEGORIA_MULTA", dsCategoriaMulta);
			rsm.setValueToField("NR_CATEGORIA_MULTA", nrCategoriaMulta);
			rsm.setValueToField("CD_CATEGORIA_MULTA", cdCategoriaMulta);
			rsm.setValueToField("VL_JUROS", vlJuros);
			rsm.setValueToField("NM_CATEGORIA_JUROS", dsCategoriaJuros);
			rsm.setValueToField("NR_CATEGORIA_JUROS", nrCategoriaJuros);
			rsm.setValueToField("CD_CATEGORIA_JUROS", cdCategoriaJuros);
			rsm.setValueToField("VL_DESCONTO", conta.getVlAbatimento());
			rsm.setValueToField("NM_CATEGORIA_DESCONTO", dsCategoriaDesconto);
			rsm.setValueToField("NR_CATEGORIA_DESCONTO", nrCategoriaDesconto);
			rsm.setValueToField("CD_CATEGORIA_DESCONTO", cdCategoriaDesconto);
			rsm.setValueToField("VL_ARECEBER", (conta.getVlConta() - conta.getVlAbatimento() + conta.getVlAcrescimo() - conta.getVlRecebido()));
			
			rsm.beforeFirst();
			return rsm;
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
	 * @author Alvaro
	 * @c
	 * @param cdContaReceber
	 * @param cdConta
	 * @param vlMulta
	 * @param cdCategoriaMulta
	 * @param vlJuros
	 * @param cdCategoriaJuros
	 * @param vlDesconto
	 * @param cdCategoriaDesconto
	 * @param vlRecebido
	 * @param cdFormaPagamento
	 * @param tituloCredito
	 * @param cdUsuario
	 * @param dtDeposito
	 * @param dtMovimento
	 * @param dsHistorico
	 * @return Result
	 * 
	 * Mï¿½todo criado para encapsular o comportamento de setBaixaResumida.
	 * Criado com a finalidade de evitar a chamada incorreta feita pelo FLEX
	 * e formatar o objeto Result no padrï¿½o de resposta esperado.
	 */
	public static Result lancarRecebimento(int cdContaReceber, int cdConta,
			Double vlMulta, int cdCategoriaMulta, Double vlJuros, int cdCategoriaJuros, Double vlDesconto, int cdCategoriaDesconto,
			Double vlRecebido, int cdFormaPagamento, TituloCredito tituloCredito, int cdUsuario, GregorianCalendar dtDeposito,
			GregorianCalendar dtMovimento, String dsHistorico) {
		
		return lancarRecebimento(cdContaReceber, cdConta, vlMulta, cdCategoriaMulta, vlJuros, cdCategoriaJuros, vlDesconto, 
				cdCategoriaDesconto, vlRecebido, cdFormaPagamento, tituloCredito, cdUsuario, dtDeposito, dtMovimento, dsHistorico, null);
	}
	
	public static Result lancarRecebimento(int cdContaReceber, int cdConta,
			Double vlMulta, int cdCategoriaMulta, Double vlJuros, int cdCategoriaJuros, Double vlDesconto, int cdCategoriaDesconto,
			Double vlRecebido, int cdFormaPagamento, TituloCredito tituloCredito, int cdUsuario, GregorianCalendar dtDeposito,
			GregorianCalendar dtMovimento, String dsHistorico, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try{
			
			return setBaixaResumida(cdContaReceber, cdConta, vlMulta, cdCategoriaMulta, vlJuros, cdCategoriaJuros,
		                vlDesconto, cdCategoriaDesconto, vlRecebido, cdFormaPagamento, tituloCredito, cdUsuario, dtDeposito, dtMovimento, dsHistorico, connect);
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar lanï¿½ar recebimento resumido!", e);
		}finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result setBaixaResumida(int cdContaReceber, int cdConta,
			Double vlMulta, int cdCategoriaMulta, Double vlJuros, int cdCategoriaJuros, Double vlDesconto, int cdCategoriaDesconto,
			Double vlRecebido, int cdFormaPagamento, TituloCredito tituloCredito, int cdUsuario, GregorianCalendar dtDeposito,
			GregorianCalendar dtMovimento, String dsHistorico)
	{
		return setBaixaResumida(cdContaReceber, cdConta, vlMulta, cdCategoriaMulta, vlJuros, cdCategoriaJuros,
				                vlDesconto, cdCategoriaDesconto, vlRecebido, cdFormaPagamento, tituloCredito, cdUsuario, dtDeposito, dtMovimento, dsHistorico, null);
	}

	public static Result setBaixaResumida(int cdContaReceber, int cdConta,
			Double vlMulta, int cdCategoriaMulta, Double vlJuros, int cdCategoriaJuros, Double vlDesconto, int cdCategoriaDesconto,
			Double vlRecebido, int cdFormaPagamento, TituloCredito tituloCredito, int cdUsuario, GregorianCalendar dtDeposito,
			GregorianCalendar dtMovimento,
			String dsHistorico, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			/*
			 *  Verifica a Forma de Pagamento
			 */
			FormaPagamento formaPagamento = FormaPagamentoDAO.get(cdFormaPagamento, connect);
			if(formaPagamento.getTpFormaPagamento()==FormaPagamentoServices.TITULO_CREDITO && (tituloCredito==null))
				return new Result(-10, "Recebimento em tï¿½tulo de crï¿½dito sï¿½o permitido com todas as informaï¿½ï¿½es ao tï¿½tulo!");

			/*
			 *  Pagamento com tï¿½tulo de crï¿½dito
			 */
			ArrayList<MovimentoContaTituloCredito> titulos = new ArrayList<MovimentoContaTituloCredito>();

			if(tituloCredito!=null && tituloCredito.getCdTituloCredito()<=0)	{
				System.out.println("Novo titulo");
				int cdPessoa = 0;
				ContaFinanceira conta = ContaFinanceiraDAO.get(cdConta, connect);
				String sql = "SELECT * FROM grl_pessoa A " +
							 "LEFT OUTER JOIN grl_pessoa_fisica B ON (A.cd_pessoa = B.cd_pessoa) " +
							 "WHERE nr_cpf = \'"+tituloCredito.getNrDocumentoEmissor()+"\'";
				if (tituloCredito.getTpDocumentoEmissor()==TituloCreditoServices.tdocCNPJ) 	{
					sql = "SELECT * FROM grl_pessoa A " +
						  "LEFT OUTER JOIN grl_pessoa_juridica C ON (A.cd_pessoa = C.cd_pessoa) "+
						  "WHERE nr_cnpj = \'"+tituloCredito.getNrDocumentoEmissor()+"\'";
				}
				ResultSet rs = connect.prepareStatement(sql).executeQuery();
				if(rs.next())
					cdPessoa = rs.getInt("cd_pessoa");
				/**
				 * 
				 */
				ContaReceber contaReceber = new ContaReceber(0, cdPessoa, conta.getCdEmpresa(), 0 /*cdContrato*/, 0 /*cdContaOrigem*/,
															 0 /*cdDocumentoSaida*/, 0 /*cdContaCarteira*/, cdConta, 0 /*cdFrete*/,
															 tituloCredito.getNrDocumento(), null, 1 /*nrParcela*/, "1/1" /*nrReferencia*/,
															 tituloCredito.getCdTipoDocumento(), "Pagamento em tï¿½tulo de crï¿½dito",
															 tituloCredito.getDtVencimento(), tituloCredito.getDtCredito(), null /*dtRecebimento*/,
															 null /*dtProrrogacao*/, tituloCredito.getVlTitulo(), 0.0d /*vlAbatimento*/,
															 0.0d /*vlAcrescimo*/, 0.0d /*vlRecebido*/, ST_EM_ABERTO, UNICA_VEZ, 1 /*qtParcelas*/,
															 TP_PARCELA, 0 /*cdNegociacao*/, null /*txtObservacao*/, 0 /*cdPlanoPagamento*/,
															 0 /*cdFormaPagamento*/, new GregorianCalendar(), tituloCredito.getDtVencimento(), 0/*cdTurno*/,
															 0.0d/*prJuros*/, 0.0d/*prMulta*/, 0/*lgProtesto*/);
				ArrayList<ContaReceberCategoria> categorias = new ArrayList<ContaReceberCategoria>();
				ResultSetMap rsm = ContaReceberCategoriaServices.getCategoriaOfContaReceber(cdContaReceber, connect);
				Double vlTotal = 0.0;
				while(rsm.next())
					vlTotal += rsm.getDouble("vl_conta_categoria");
				rsm.beforeFirst();
				while(rsm.next())
					categorias.add(new ContaReceberCategoria(0 /*cdContaReceber*/,
							                                 rsm.getInt("cd_categoria_economica"), tituloCredito.getVlTitulo() / vlTotal * rsm.getDouble("vl_conta_categoria"), rsm.getInt("cd_centro_custo")));
				Result ret = insert(contaReceber, categorias, tituloCredito, true, connect);
				contaReceber.setCdContaReceber(ret.getCode());
				if(contaReceber.getCdContaReceber() < 0)
					return new Result(-11, "Nï¿½o foi possï¿½vel criar a conta a receber do tï¿½tulo de crï¿½dito!");

				titulos.add(new MovimentoContaTituloCredito(tituloCredito.getCdTituloCredito(), 0, cdConta));
			}
			/*
			 *  Verifica classificaï¿½ï¿½o de Multa, Juros e Desconto
			 */
			if((vlMulta>0 && cdCategoriaMulta<=0) || (vlJuros>0 && cdCategoriaJuros<=0) || (vlDesconto>0 && cdCategoriaDesconto<=0))
				return new Result(-20, "Classificaï¿½ï¿½o dos juros/multa ou desconto inexistente!");
			/*
			 *  Instancia a conta a receber
			 */
			ContaReceber conta = ContaReceberDAO.get(cdContaReceber, connect);
			
			/*
			 *  Cria o movimento de conta
			 */
			int stMovimento = MovimentoContaServices.ST_COMPENSADO;
			if(dtDeposito!=null && dtMovimento.after(new GregorianCalendar()))
				stMovimento = MovimentoContaServices.ST_NAO_COMPENSADO;
			MovimentoConta movimento = new MovimentoConta(0 /*cdMovimentoConta*/, cdConta, 0 /*cdContaOrigem*/, 0 /*cdMovimentoOrigem*/,
															cdUsuario, 0 /*cdCheque*/, 0 /*cdViagem*/,
															dtMovimento, vlRecebido, conta.getNrDocumento()/*nrDocumento*/,
															MovimentoContaServices.CREDITO, 0 /*tpOrigem*/,
															stMovimento,
															dsHistorico, dtDeposito, null /*idExtrato*/,
															cdFormaPagamento, 0 /*cdFechamento*/,0/*cdTurno*/);
			ArrayList<MovimentoContaReceber> movimentoContaReceber = new ArrayList<MovimentoContaReceber>();
			/*
			 *  Cria o recebimento
			 */
			movimentoContaReceber.add(new MovimentoContaReceber(cdConta, 0 /*cdMovimentoConta)*/,
																		  cdContaReceber, vlRecebido,
																		  vlJuros,vlMulta,vlDesconto,
																		  0 /*vlTarifaCobranca*/,0 /*cdArquivo*/,
																		  0 /*cdRegistro*/));
			/*
			 *  Cria a classificaï¿½ï¿½o em categorias do movimento na conta
			 */
			Double vlTotalClassificado = 0.0;
			ArrayList<MovimentoContaCategoria> movimentoCategoria = new ArrayList<MovimentoContaCategoria>();
			ResultSetMap rsmCategorias = ContaReceberCategoriaServices.getCategoriaOfContaReceber(cdContaReceber, connect);
			while(rsmCategorias.next()){
				
				Double vlMovimentoCategoria = rsmCategorias.getDouble("vl_conta_categoria") / (conta.getVlConta() - conta.getVlAbatimento() + conta.getVlAcrescimo()) * 
						                                                                    (vlRecebido - vlMulta - vlJuros + vlDesconto);
				vlTotalClassificado += vlMovimentoCategoria;
				
				movimentoCategoria.add(new MovimentoContaCategoria(cdConta, 0/*cdMovimentoConta*/, rsmCategorias.getInt("cd_categoria_economica"),
																   vlMovimentoCategoria.floatValue(), 0 /*cdMovimentoContaCategoria*/, 0 /*cdContaPagar*/,
																   cdContaReceber, MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /*tpMovimento*/, rsmCategorias.getInt("cd_centro_custo")));
			}
			if(vlMulta > 0)
				movimentoCategoria.add(new MovimentoContaCategoria(cdConta, 0/*cdMovimentoConta*/, cdCategoriaMulta, vlMulta.floatValue(),
																   0 /*cdMovimentoContaCategoria*/, 0 /*cdContaPagar*/, cdContaReceber,
																   MovimentoContaCategoriaServices.TP_MULTA /*tpMovimento*/, (rsmCategorias.getPosition() > 0 ? rsmCategorias.getInt("cd_centro_custo") : 0)));
			if(vlJuros > 0)
				movimentoCategoria.add(new MovimentoContaCategoria(cdConta, 0/*cdMovimentoConta*/, cdCategoriaJuros, vlJuros.floatValue(),
						                                           0 /*cdMovimentoContaCategoria*/, 0 /*cdContaPagar*/, cdContaReceber,
						                                           MovimentoContaCategoriaServices.TP_JUROS /*tpMovimento*/, (rsmCategorias.getPosition() > 0 ? rsmCategorias.getInt("cd_centro_custo") : 0)));
			if(vlDesconto > 0)
				movimentoCategoria.add(new MovimentoContaCategoria(cdConta, 0/*cdMovimentoConta*/, cdCategoriaDesconto, vlDesconto.floatValue(),
						                                           0 /*cdMovimentoContaCategoria*/, 0 /*cdContaPagar*/, cdContaReceber,
						                                           MovimentoContaCategoriaServices.TP_DESCONTO /*tpMovimento*/, (rsmCategorias.getPosition() > 0 ? rsmCategorias.getInt("cd_centro_custo") : 0)));
			
			if (vlTotalClassificado+0.01 < (vlRecebido-vlMulta-vlJuros+vlDesconto))	{
				return new Result(-30, "setBaixaResumida: Conta nï¿½o classificada correntamente! [Valor Total Classificado: "+vlTotalClassificado+", Valor da Conta: "+(vlRecebido-vlMulta-vlJuros+vlDesconto)+"]");
			}
			/*
			 *  Chama o mï¿½todo que faz o lanï¿½amento da conta, o tï¿½tulo de crï¿½dito,
			 */
			
			Result resultadoMovimento = MovimentoContaServices.insert(movimento, movimentoContaReceber, movimentoCategoria, titulos, 0, connect);
			int cdMovimentoConta = resultadoMovimento.getCode();
			if(cdMovimentoConta<=0)	{
				if(isConnectionNull)
					Conexao.rollback(connect);
				com.tivic.manager.util.Util.registerLog(new Exception("setBaixaResumida: não foi possível gravar o movimento!"));
				return new Result(-40, "setBaixaResumida 2: nï¿½o foi possï¿½vel gravar o movimento: " + resultadoMovimento.getMessage());
			}
			
			AuthData auth = new AuthData();
			auth.setUsuario(UsuarioDAO.get(cdUsuario, connect));
			ComplianceManager.process(ContaReceberDAO.get(conta.getCdContaReceber(), connect), auth, ComplianceManager.TP_ACAO_UPDATE, Conexao.conectar());

			if (isConnectionNull)
				connect.commit();

			return new Result(1, "Recebimento efetuado com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar lanï¿½ar recebimento resumido!", e);
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static boolean gerarNossoNumeroAndRegistro(ContaReceber conta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			connect = isConnectionNull ? Conexao.conectar() : connect;
			ContaCarteira carteira = ContaCarteiraDAO.get(conta.getCdContaCarteira(), conta.getCdConta(), connect);
			if(carteira==null)	{
				return false;
			}
			return gerarNossoNumeroAndRegistro(conta, carteira.getNrDigitosInicio(), carteira.getQtDigitosNumero(),
					carteira.getTpCobranca(), connect);
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			return false;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	private static boolean gerarNossoNumeroAndRegistro(ContaReceber conta, String nrDigitosInicio, int qtDigitosNumero, int tpCobranca, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			connect = isConnectionNull ? Conexao.conectar() : connect;
			if(conta.getIdContaReceber()==null || conta.getIdContaReceber().equals("") || conta.getIdContaReceber().length()<qtDigitosNumero)	{
				String prefix  = nrDigitosInicio!=null ? nrDigitosInicio.trim() : "";
				String idContaReceber  = "";
				if(conta.getCdContrato()>0)	{
					Contrato contrato = ContratoDAO.get(conta.getCdContrato(), connect);
					idContaReceber    = contrato.getNrContrato().replace("/", "").replace("-", "")+Util.fillNum(conta.getNrParcela(), 3);
				}
				else if(conta.getCdDocumentoSaida()>0)	{
					DocumentoSaida docSaida = DocumentoSaidaDAO.get(conta.getCdDocumentoSaida(), connect);
					idContaReceber = docSaida.getNrDocumentoSaida().replace("/", "").replace("-", "")+Util.fillNum(conta.getNrParcela(), 3);
				}
				else	{
					// Autoincremental
					String idTable = "grl_conta_carteira_"+conta.getCdConta()+"_"+conta.getCdContaCarteira();
					idContaReceber = String.valueOf(Conexao.getSequenceCode(idTable, connect));
				}
				// Complementa nosso nï¿½mero deixando do tamanho padrï¿½o
				idContaReceber = prefix+Util.fill(idContaReceber, qtDigitosNumero-prefix.length(), '0', 'E');
				conta.setIdContaReceber(idContaReceber);
			}
			return (tpCobranca==1);
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			return false;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Object[] calcJurosMulta(int cdContaReceber)	{
		return calcJurosMulta(cdContaReceber, Util.getDataAtual(), null);
	}
	public static ResultSetMap calcJurosMulta( Object[] args, int cdContaReceber, GregorianCalendar dtPagamento)	{
		ResultSetMap rsm = new ResultSetMap();
		try{
			Object[] result = calcJurosMulta(cdContaReceber, dtPagamento, null);
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("MULTA", result[0]);
			register.put("JUROS", result[1]);
			rsm.addRegister(register);
		}catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
		}
		return rsm;
	}
	public static Object[] calcJurosMulta(int cdContaReceber, GregorianCalendar dtPagamento)	{
		return calcJurosMulta(cdContaReceber, dtPagamento, null);
	}
	public static Object[] calcJurosMulta(int cdContaReceber, GregorianCalendar dtPagamento, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try	{
			if(connect==null)
				connect = Conexao.conectar();
			ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber, connect);
			if(contaReceber==null)
				return new Object[] {-10, -10};
			float prJuros = 0, vlJuros = 0;
			float prMulta = 0, vlMulta = 0;
			GregorianCalendar dtVencimento = contaReceber.getDtVencimento();
			
			int diasAte = Math.round((dtPagamento.getTimeInMillis() - dtVencimento.getTimeInMillis()) / 1000 / 3600 / 24);
			if( contaReceber.getCdContaCarteira() > 0 && contaReceber.getCdConta() > 0 ){
				ContaCarteira contaCarteira = ContaCarteiraDAO.get(contaReceber.getCdContaCarteira(), contaReceber.getCdConta());
				if( diasAte <= contaCarteira.getQtDiasMulta()){
					return new Object[] {0, 0};
				}
				diasAte = diasAte-contaCarteira.getQtDiasMulta();
			}
			
			int cdCategoriaJuros = 0;
			String nmCategoriaJuros = null;
			if(contaReceber.getPrJuros() > 0){
				prJuros = contaReceber.getPrJuros().floatValue();
				cdCategoriaJuros = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_JUROS_PROGRAMA_FATURA", 0);
				if(cdCategoriaJuros > 0)
					nmCategoriaJuros = CategoriaEconomicaDAO.get(cdCategoriaJuros, connect).getNmCategoriaEconomica();
			}
			else if(prJuros==0 && prMulta==0)	{
				ContaCarteira carteira = ContaCarteiraDAO.get(contaReceber.getCdContaCarteira(), contaReceber.getCdConta(), connect);
				if(carteira!=null){
					prJuros = carteira.getPrJuros();
					prMulta = carteira.getPrMulta();
				}
			}
			else if(contaReceber.getCdContrato()>0)	{
				Contrato contrato = ContratoDAO.get(contaReceber.getCdContrato(), connect);
				prJuros = contrato.getPrJurosMora();
				prMulta = contrato.getPrMultaMora();
			}
			
			// Verifica se o percentual de juros ï¿½ a.m. ou a.d., se nï¿½o transforma e em juros ao dia
			if(prJuros >= 1)
				prJuros = prJuros / 30;
			
			// Jï¿½ verifica se venceu em dia nï¿½o ï¿½til
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			if(!lgBaseAntiga)
			while(dtVencimento.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || dtVencimento.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY || FeriadoServices.isFeriado(dtVencimento))
				dtVencimento.add(Calendar.DATE, 1);
			else
				while(dtVencimento.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || dtVencimento.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
					dtVencimento.add(Calendar.DATE, 1);
			// Realiza cï¿½lculos identificando os valores Jï¿½ pago
			Double vlAReceber  = contaReceber.getVlConta() - contaReceber.getVlAbatimento() + contaReceber.getVlAcrescimo();
			
			if(diasAte > 0)
				vlMulta = vlAReceber.floatValue() * prMulta / 100;
			ResultSetMap rsm  = MovimentoContaReceberServices.getRecebimentoOfContaReceber(cdContaReceber, connect);
			while(rsm.next())	{
				diasAte  = Math.round((rsm.getGregorianCalendar("dt_movimento").getTimeInMillis() - dtVencimento.getTimeInMillis()) / 1000 / 3600 / 24);
				dtVencimento = rsm.getGregorianCalendar("dt_movimento");
				vlJuros    += ((vlAReceber * prJuros * diasAte) / 100) - rsm.getDouble("vl_juros");
				vlAReceber -= rsm.getDouble("vl_recebido");
				vlMulta    -= rsm.getDouble("vl_multa");
			}
			/*
			 *  Calcula juros do valor ainda a receber, como os juros de cada pagamento Jï¿½ foram sendo calculado a cada pagamento e a
			 *  	data de vencimento
			 */
			diasAte = Math.round((dtPagamento.getTimeInMillis() - dtVencimento.getTimeInMillis()) / 1000 / 3600 / 24);
			vlJuros    += ((vlAReceber * prJuros * diasAte) / 100);
			vlMulta = vlMulta<0 ? 0 : vlMulta;
			vlJuros = vlJuros<0 ? 0 : vlJuros;
			return new Object[] {vlMulta, vlJuros, cdCategoriaJuros, nmCategoriaJuros};
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Object[] {-10, -10};
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Object[] calcJurosMultaCheque(int cdContaReceber)	{
		return calcJurosMultaCheque(cdContaReceber, Util.getDataAtual(), 0, null);
	}
	public static Object[] calcJurosMultaCheque(int cdContaReceber, GregorianCalendar dtPagamento, int isResgate)	{
		return calcJurosMultaCheque(cdContaReceber, dtPagamento, isResgate, null);
	}
	public static Object[] calcJurosMultaCheque(int cdContaReceber, GregorianCalendar dtPagamento, int isResgate, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try	{
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			//Busca da conta a receber
			ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber, connect);
			if(contaReceber==null){
				System.out.println("Erro: Conta a Receber nao identificada!");
				Conexao.rollback(connect);
				return new Object[] {-10, -10, -1, null, -1, "Erro: Conta a Receber nao identificada!"};
			}
			//Busca do Titulo de Credito relacionado a conta a receber
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_conta_receber", Integer.toString(cdContaReceber), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmTitulo = TituloCreditoDAO.find(criterios, connect);
			TituloCredito titulo = null;
			if(rsmTitulo.next()){
				titulo = TituloCreditoDAO.get(rsmTitulo.getInt("cd_titulo_credito"), connect);
			}
			if(titulo == null){
				System.out.println("Erro: Titulo nao identificado!");
				Conexao.rollback(connect);
				return new Object[] {-10, -10, -1, null, -1, "Erro: Titulo nao identificado!"};
			}
			//Busca da Conta Factoring que relaciona essa conta a receber ï¿½ uma conta a pagar
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_conta_receber_antecipada", Integer.toString(cdContaReceber), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmContaFac = ContaFactoringDAO.find(criterios, connect);
			int cdContaFactoring = 0;
			if(rsmContaFac.next()){
				cdContaFactoring = rsmContaFac.getInt("cd_conta_factoring");
			}
			ContaFactoring contaFac = ContaFactoringDAO.get(cdContaFactoring, connect);
			if(contaFac == null){
				Conexao.rollback(connect);
				System.out.println("Erro: Conta Factoring nao identificada!");
				return new Object[] {-10, -10, -1, null, -1, "Erro: Conta Factoring nao identificada!"};
			}
			
			//Busca da Conta a pagar relacionada a conta factoring
			ContaPagar contaPagar = ContaPagarDAO.get(contaFac.getCdContaPagar(), connect);
			if(contaPagar == null){
				Conexao.rollback(connect);
				System.out.println("Erro: Conta a Pagar nao identificada!");
				return new Object[] {-10, -10, -1, null, -1, "Erro: Conta a Pagar nao identificada!"};
			}
			//Busca do Cliente relacionado a troca do cheque
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("findCliente", "1", ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_pessoa", Integer.toString(contaPagar.getCdPessoa()), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmCliente = PessoaServices.find(criterios);
			Cliente cliente = null;
			if(rsmCliente.next()){
				cliente = ClienteDAO.get(rsmCliente.getInt("cd_empresa_trabalho"), rsmCliente.getInt("cd_pessoa"), connect);
			}
			if(cliente == null){
				Conexao.rollback(connect);
				System.out.println("Erro: Cliente nao identificado!");
				return new Object[] {-10, -10, -1, null, -1, "Erro: Cliente nao identificado!"};
			}
			float prJuros = cliente.getPrTaxaJurosFactoring();
			if(prJuros == 0)
				prJuros = ParametroServices.getValorOfParametroAsFloat("VL_TAXA_JUROS", 0, cliente.getCdEmpresa());
			if(prJuros == 0){
				Conexao.rollback(connect);
				System.out.println("Erro: Parametro de Taxa de Juros nï¿½o configurado!");
				return new Object[] {-10, -10, -1, null, -1, "Erro: Parametro de Taxa de Juros nï¿½o configurado!"};
			}
			
			Double vlJuros = 0.0;
			Double vlMulta = 0.0;
			
			
			// Jï¿½ verifica se venceu em dia nï¿½o ï¿½til
			GregorianCalendar dtVencimento = contaReceber.getDtVencimento();
			while(dtVencimento.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || dtVencimento.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY || FeriadoServices.isFeriado(dtVencimento))
				dtVencimento.add(Calendar.DATE, 1);
			// Realiza cï¿½lculos identificando se hï¿½ atraso no pagamento e se eh um resgate
			Double vlAReceber  = contaReceber.getVlConta() - contaReceber.getVlAbatimento() + contaReceber.getVlAcrescimo();
			int diasAte = Math.round((dtPagamento.getTimeInMillis() - dtVencimento.getTimeInMillis()) / 1000 / 3600 / 24);
			if(diasAte > 0 && isResgate == 1)
				vlJuros = vlAReceber * prJuros / 100 / 30 * diasAte;
			//Verifica se ha multa relacionada (caso se resgate, e tenha havido devolucao)
			if(isResgate == 1 && (titulo.getStTitulo() == TituloCreditoServices.stDEVOLVIDA_1X || titulo.getStTitulo() == TituloCreditoServices.stDEVOLVIDA_2X)){
				vlMulta = new Double(cliente.getVlTaxaDevolucaoFactoring());
				if(vlMulta == 0)
					vlMulta = new Double(ParametroServices.getValorOfParametroAsFloat("VL_TAXA_DEVOLUCAO", 0, cliente.getCdEmpresa()));
			}
	
			//Valor de multa por prorrogaï¿½ï¿½o
//			if(contaReceber.getVlAcrescimo() > 0){
//				vlMulta += contaReceber.getVlAcrescimo();
//			}
			
			//Faz a busca pelos parametros configurados para categoria de Juros por Atraso
			int cdCategoriaJuros = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_JUROS_ATRASO", 0, cliente.getCdEmpresa(), connect);
			if(cdCategoriaJuros == 0){
				Conexao.rollback(connect);
				System.out.println("Erro: Parametro de Categoria Economica de Juros de Atraso nao configurado!");
				return new Object[] {-10, -10, -1, null, -1, "Erro: Parametro de Categoria Economica de Juros de Atraso nao configurado!"};
			}
			//Faz a busca pelos parametros configurados para categoria de Multa por Devolucao
			int cdCategoriaMulta = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_MULTA_DEVOLUCAO", 0, cliente.getCdEmpresa(), connect);
			if(cdCategoriaMulta == 0){
				Conexao.rollback(connect);
				System.out.println("Erro: Parametro de Categoria Economica de Multa por devolucao nao configurado!");
				return new Object[] {-10, -10, -1, null, -1, "Erro: Parametro de Categoria Economica de Multa por devolucao nao configurado!"};
			}
			
			if(isConnectionNull)
				connect.commit();
			
			CategoriaEconomica categoriaJuros = CategoriaEconomicaDAO.get(cdCategoriaJuros, connect);
			CategoriaEconomica categoriaMulta = CategoriaEconomicaDAO.get(cdCategoriaMulta, connect);
			String strCategoriaJuros = categoriaJuros.getNmCategoriaEconomica();
			String strCategoriaMulta = categoriaMulta.getNmCategoriaEconomica();
			return new Object[] {vlMulta, vlJuros, cdCategoriaJuros, strCategoriaJuros, cdCategoriaMulta, strCategoriaMulta};
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Object[] {-10, -10, -1, null, -1, null};
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int setSituacaoContaReceber(int cdContaReceber, int stConta) {
		return setSituacaoContaReceber(cdContaReceber, stConta, null);
	}

	public static int setSituacaoContaReceber(int cdContaReceber, int stConta, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			ContaReceber conta = ContaReceberDAO.get(cdContaReceber, connection);
			if (conta == null)
				return -1;

			int situacaoAtual = conta.getStConta();
			if (stConta == situacaoAtual)
				return 1;
			else {
				conta.setStConta(stConta);
				if (ContaReceberDAO.update(conta, connection) <= 0) {
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
			System.err.println("Erro! ContaReceberServices.setSituacaoContaReceber: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static String remontarLinhaDigitavel(String nrLinhaDigitavel, GregorianCalendar dtVencimento)	{
		int vlFatorVencimento = Math.round((dtVencimento.getTimeInMillis() - (new GregorianCalendar(1997, GregorianCalendar.OCTOBER, 7)).getTimeInMillis()) / (24*60*60*1000));
		nrLinhaDigitavel = nrLinhaDigitavel.replaceAll("[ ]","").replaceAll("[\\.]","");
		String nrCodigoBarras = nrLinhaDigitavel.substring(0, 4)+  /*00-04*/
								Util.fillNum(vlFatorVencimento, 4)+ // Nova data de vencimento
								nrLinhaDigitavel.substring(37)  +  /*05-19*/
		                        nrLinhaDigitavel.substring(4, 9)+  /*20-23*/
		                        nrLinhaDigitavel.substring(10, 20)+/*24-34*/
		                        nrLinhaDigitavel.substring(21, 31) /*35-44*/;
		int nrDv = 0;
		try	{
			nrDv = 11-Integer.parseInt(Util.getDvMod11(nrCodigoBarras, "1", "1", 9, 2, true));
		}
		catch(Exception e)	{
		}

		if(nrDv>=10 || nrDv==0)
			nrDv = 1;
		// Retorno
		if(nrCodigoBarras.length()==43)
			return nrCodigoBarras.substring(0, 4)+nrDv+nrCodigoBarras.substring(4, 43);
		else
			return "";
	}
	
	public static Result refaturarContas(ArrayList<HashMap<String,Object>> registros, int cdContaReceberNova) {
		return refaturarContas(registros, cdContaReceberNova, null);
	}

	
	public static Result refaturarContas(ArrayList<HashMap<String,Object>> registros, int cdContaReceberNova, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			int i = 0;
			Result resultado = new Result(1, "Refaturamento realizado com sucesso!");
			ResultSetMap rs = new ResultSetMap();
			ContaReceber contaNova = null;
			contaNova = ContaReceberDAO.get(cdContaReceberNova, connection);
			Double vlConta = 0.0d;
			Double vlAbatimento = 0.0d;
			Double vlAcrescimo = 0.0d;
			Double vlRecebido = 0.0d;
			for(i = 0; i < registros.size(); i++){
				int cdContaReceber    = registros.get(i)==null || registros.get(i).get("cdContaReceber")==null ? 0 : (Integer) registros.get(i).get("cdContaReceber");
				ContaReceber conta = ContaReceberDAO.get(cdContaReceber);
				vlConta      += conta.getVlConta();
				vlAbatimento += conta.getVlAbatimento();
				vlAcrescimo  += conta.getVlAcrescimo();
				vlRecebido   += conta.getVlRecebido();
				
				conta.setStConta(6);//Mudar ela para refaturada
				
				if(ContaReceberDAO.update(conta, connection) < 0){
					connection.rollback();
					System.out.println("Erro refaturarContas!");
					return new Result(-1, "Erro ao refaturar as contas!");
				}
			}
			
			contaNova.setVlConta(vlConta);
			contaNova.setVlAbatimento(vlAbatimento);
			contaNova.setVlAcrescimo(vlAcrescimo);
			contaNova.setVlRecebido(vlRecebido);
			
			if(ContaReceberDAO.update(contaNova, connection) < 0){
				connection.rollback();
				System.out.println("Erro refaturarContas!");
				return new Result(-1, "Erro ao refaturar as contas!");
			}
			resultado.addObject("resultado", rs);
			
			if(i == 0){
				return new Result(-1, "Nenhuma conta foi selecionada!");
			}
			
			if(isConnectionNull)
				connection.commit();
			
			return resultado;
			
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberServices.setSituacaoContaReceber: " + e);
			return new Result(-1, "Erro em refaturarContas: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public Result gerarRelatorioGeralReceberResumido(int cdEmpresa, int tpData, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdTipoDocumento, 
			int cdContrato, int cdConvenio, int stConta, int cdCliente, String dsHistorico, String nrDocumento){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			if(dtInicial!=null) {
				dtInicial.set(Calendar.HOUR, 0);
				dtInicial.set(Calendar.MINUTE, 0);
				dtInicial.set(Calendar.SECOND, 0);
				dtInicial.set(Calendar.MILLISECOND, 0);
			}
			if(dtFinal!=null) {
				dtFinal.set(Calendar.HOUR, 23);
				dtFinal.set(Calendar.MINUTE, 59);
				dtFinal.set(Calendar.SECOND, 59);
				dtFinal.set(Calendar.MILLISECOND, 59);
			}
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			// Data de Emisso menor do que o final do perodo
			criterios.add(new ItemComparator("A.dt_emissao", Util.formatDate(dtFinal, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.DATE));
			//
			if(cdTipoDocumento > 0)
				criterios.add(new ItemComparator("A.cd_tipo_documento", String.valueOf(cdTipoDocumento), ItemComparator.EQUAL, Types.INTEGER));
			//
			if(cdContrato > 0)
				criterios.add(new ItemComparator("A.cd_contrato", String.valueOf(cdContrato), ItemComparator.EQUAL, Types.INTEGER));
			//
			if(cdConvenio > 0)
				criterios.add(new ItemComparator("cdConvenio", String.valueOf(cdConvenio), ItemComparator.EQUAL, Types.INTEGER));
			//
			if(stConta >= 0)
				criterios.add(new ItemComparator("A.st_conta", String.valueOf(stConta), ItemComparator.EQUAL, Types.INTEGER));
			if(cdCliente > 0)
				criterios.add(new ItemComparator("A.cd_pessoa", String.valueOf(cdCliente), ItemComparator.EQUAL, Types.INTEGER));
			if(nrDocumento!=null && !nrDocumento.trim().equals(""))
				criterios.add(new ItemComparator("A.nr_documento", nrDocumento, ItemComparator.EQUAL, Types.VARCHAR));
			if(dsHistorico!=null && !dsHistorico.trim().equals(""))
				criterios.add(new ItemComparator("A.ds_historico", dsHistorico, ItemComparator.LIKE_ANY, Types.VARCHAR));
			ResultSetMap rsm = findByCliente(criterios, connection);
			//
			PreparedStatement pstmtValorEmNotas = connection.prepareStatement("SELECT SUM(vl_conta + vl_acrescimo - vl_abatimento) AS vl_total "+ 
																			  "FROM adm_conta_receber A " + 
																			  "WHERE A.cd_empresa    = "+cdEmpresa+
																			  "  AND A.cd_pessoa     = ? " +
																			  "  AND A.dt_emissao    < ? ");
			// 
			PreparedStatement pstmtUltRecebimento = connection.prepareStatement("SELECT MAX(dt_movimento) AS dt_ultimo_recebimento " + 
																			    "FROM adm_movimento_conta         A " + 
																			    "JOIN adm_movimento_conta_receber B ON (A.cd_conta           = B.cd_conta " +
																			    "                                   AND A.cd_movimento_conta = B.cd_movimento_conta) " + 
																			    "JOIN adm_conta_receber           C ON(B.cd_conta_receber   = C.cd_conta_receber) " + 
																			    "WHERE C.cd_empresa    = "+cdEmpresa+
																			    "  AND C.cd_pessoa     = ? " +
																			    "  AND A.dt_movimento <= ? ");
			PreparedStatement pstmtMaiorAtraso = connection.prepareStatement(
                              "SELECT MIN(dt_vencimento) AS dt_vencimento "+ 
							  "FROM adm_conta_receber A  " + 
							  "WHERE A.cd_empresa = "+cdEmpresa+
							  "  AND A.cd_pessoa  = ? " +
							  "  AND (vl_conta + vl_acrescimo - vl_abatimento) > COALESCE((SELECT SUM(vl_recebido - vl_juros - vl_multa + vl_desconto) " +
							  "                                                            FROM adm_movimento_conta_receber MCR " +
							  "                                                            JOIN adm_movimento_conta MC ON (MCR.cd_conta = MC.cd_conta " +
							  "                                                                                        AND MCR.cd_movimento_conta = MC.cd_movimento_conta) " +
							  "                                                            WHERE MCR.cd_conta_receber = A.cd_conta_receber " +
							  "                                                              AND MC.dt_movimento < ?), 0)+0.1 ");
			PreparedStatement pstmtCompras = connection.prepareStatement("SELECT MIN(dt_documento_saida) AS dt_primeira_compra, MAX(dt_documento_saida) AS dt_ultima_compra "+ 
																		 "FROM alm_documento_saida A  " + 
																		 "WHERE A.cd_empresa = "+cdEmpresa+
																		 "  AND A.cd_cliente  = ? ");
			//
			PreparedStatement pstmtCreditos = connection.prepareStatement("SELECT SUM(B.vl_recebido - B.vl_juros - B.vl_multa + B.vl_desconto) AS vl_total," +
					                                                      "       SUM(B.vl_juros) AS vl_juros " + 
																		  "FROM adm_movimento_conta         A " + 
																		  "JOIN adm_movimento_conta_receber B ON (A.cd_movimento_conta = B.cd_movimento_conta " +
																		  "                                   AND A.cd_conta           = B.cd_conta) " + 
																		  "JOIN adm_conta_receber           C ON (B.cd_conta_receber = C.cd_conta_receber) " + 
																		  "WHERE A.dt_movimento >= ? " +
																		  "  AND A.dt_movimento <= ? " +
																		  "  AND C.cd_pessoa     = ? " +
																		  "  AND C.cd_empresa    = "+cdEmpresa);
			//
			int contClientes = 0;
			while(rsm.next())	{
				rsm.setValueToField("NM_CLIENTE", rsm.getString("NM_PESSOA"));
				rsm.setValueToField("CD_CLIENTE", rsm.getInt("CD_PESSOA"));
				rsm.setValueToField("NR_CLS", "CLI"); // Valor fixo por enquanto
				// NOTAS GERAL
				GregorianCalendar dtFinalGeral = new GregorianCalendar();
				dtFinalGeral.add(Calendar.YEAR, 20);
				pstmtValorEmNotas.setInt(1, rsm.getInt("cd_pessoa"));
				pstmtValorEmNotas.setTimestamp(2, new Timestamp(dtFinalGeral.getTimeInMillis()));
				ResultSet rsTemp = pstmtValorEmNotas.executeQuery();
				if(rsTemp.next())
					rsm.setValueToField("Vl_TOTAL_GERAL", rsTemp.getDouble("vl_total"));
				// CREDITOS GERAL
				pstmtCreditos.setTimestamp(1, new Timestamp(0));
				pstmtCreditos.setTimestamp(2, new Timestamp(dtFinalGeral.getTimeInMillis()));
				pstmtCreditos.setInt(3, rsm.getInt("cd_pessoa"));
				rsTemp = pstmtCreditos.executeQuery();
				if(rsTemp.next())
					rsm.setValueToField("Vl_TOTAL_PAGO", rsTemp.getDouble("vl_total"));
				// NOTAS EM ABERTO
				Double vlEmNotas = 0.0;
				pstmtValorEmNotas.setInt(1, rsm.getInt("cd_pessoa"));
				pstmtValorEmNotas.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
				rsTemp = pstmtValorEmNotas.executeQuery();
				if(rsTemp.next())
					vlEmNotas = rsTemp.getDouble("vl_total");
				// CREDITOS AT O INCIO DO PERODO
				pstmtCreditos.setTimestamp(1, new Timestamp(0));
				pstmtCreditos.setTimestamp(2, new Timestamp(dtInicial.getTimeInMillis()));
				pstmtCreditos.setInt(3, rsm.getInt("cd_pessoa"));
				rsTemp = pstmtCreditos.executeQuery();
				if(rsTemp.next())
					vlEmNotas -= rsTemp.getDouble("vl_total");
				// CREDITOS NO PERODO
				pstmtCreditos.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
				pstmtCreditos.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
				pstmtCreditos.setInt(3, rsm.getInt("cd_pessoa"));
				rsTemp = pstmtCreditos.executeQuery();
				if(rsTemp.next())	{
					rsm.setValueToField("Vl_CRED", rsTemp.getDouble("VL_TOTAL"));
					rsm.setValueToField("Vl_JUROS", rsTemp.getDouble("VL_JUROS"));
				}
				//
				rsm.setValueToField("VL_HAVER", 0);
				rsm.setValueToField("Vl_NOTAS", vlEmNotas);
				rsm.setValueToField("Vl_DIFERENCA", rsm.getDouble("VL_ABATIMENTO") - rsm.getDouble("VL_ACRESCIMO"));
				rsm.setValueToField("VL_SALDO", vlEmNotas - rsm.getDouble("vl_cred") + rsm.getDouble("vl_juros"));
				rsm.setValueToField("QT_DIAS_ATRASO", 0);
				
				// Buscando DATA DO LTIMO RECEBIMENTO
				pstmtUltRecebimento.setInt(1, rsm.getInt("CD_PESSOA"));
				pstmtUltRecebimento.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
				ResultSetMap rsmTemp = new ResultSetMap(pstmtUltRecebimento.executeQuery());
				if(rsmTemp.next())
					rsm.setValueToField("DT_ULTIMO_RECEBIMENTO", rsmTemp.getDateFormat("dt_ultimo_recebimento", "dd/MM/yyyy"));
				// Conta mais Atrasada
				pstmtMaiorAtraso.setInt(1, rsm.getInt("cd_pessoa"));
				pstmtMaiorAtraso.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
				rsmTemp = new ResultSetMap(pstmtMaiorAtraso.executeQuery());
				if(rsmTemp.next() && rsmTemp.getObject("DT_VENCIMENTO")!=null)	{
					GregorianCalendar dtUltimaAberto = rsmTemp.getGregorianCalendar("DT_VENCIMENTO");
					int qtDias = Math.round((new GregorianCalendar().getTimeInMillis() - dtUltimaAberto.getTimeInMillis()) / 1000 / 60 / 60 / 24);
					rsm.setValueToField("QT_DIAS_ATRASO", qtDias);
				}
				// COMPRA MAIS ANTIGA E MAIS NOVA
				pstmtCompras.setInt(1, rsm.getInt("CD_PESSOA"));
				rsmTemp = new ResultSetMap(pstmtCompras.executeQuery());
				if(rsmTemp.next()) {
					rsm.setValueToField("DT_PRIMEIRA_COMPRA", rsmTemp.getDateFormat("dt_primeira_compra", "dd/MM/yyyy"));
					rsm.setValueToField("DT_ULTIMA_COMPRA", rsmTemp.getDateFormat("dt_ultima_compra", "dd/MM/yyyy"));
				}
				rsm.setValueToField("VL_DEB", 0);
				
				contClientes++;
			}
			
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_CLIENTE");
			//fields.add("DS_GRUPO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("nmTotal", "Total " + contClientes + " cliente(s)");
			param.put("dtInicial", Util.convCalendarString(dtInicial));
			param.put("dtFinal", Util.convCalendarString(dtFinal));
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
	 * Metodo que busca uma conta a receber a partir do seu cï¿½digo e empresa
	 * @author Gabriel
	 * @since 25/08/2014
	 * @param cdContaReceber
	 * @param cdEmpresa
	 * @return
	 */
	public static ResultSetMap getContaReceberCustodia(int cdContaReceber, int cdEmpresa){
		return getContaReceberCustodia(cdContaReceber, cdEmpresa, null);
	}
	
	public static ResultSetMap getContaReceberCustodia(int cdContaReceber, int cdEmpresa, Connection connection){
		boolean isConnectionNull = true;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_conta_receber", Integer.toString(cdContaReceber), ItemComparator.EQUAL, Types.INTEGER));
			return find(criterios, connection);
		}
		
		catch(Exception e) {
			e.printStackTrace();
			Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result findCustodia(int cdEmpresa, int tpData, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdCliente){
		return findCustodia(cdEmpresa, tpData, dtInicial, dtFinal, cdCliente, null, null);
	}
	
	public static Result findCustodia(int cdEmpresa, int tpData, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdCliente, Connection connection){
		return findCustodia(cdEmpresa, tpData, dtInicial, dtFinal, cdCliente, null, connection);
	}
	
	public static Result findCustodia(int cdEmpresa, int tpData, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdCliente, ArrayList<ItemComparator> criterios){
		return findCustodia(cdEmpresa, tpData, dtInicial, dtFinal, cdCliente, criterios, null);
	}
	
	public static Result findCustodia(int cdEmpresa, int tpData, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdCliente, ArrayList<ItemComparator> criterios, Connection connection){
		boolean isConnectionNull = true;
		try {
			if(dtInicial!=null) {
				dtInicial.set(Calendar.HOUR, 0);
				dtInicial.set(Calendar.MINUTE, 0);
				dtInicial.set(Calendar.SECOND, 0);
				dtInicial.set(Calendar.MILLISECOND, 0);
			}
			if(dtFinal!=null) {
				dtFinal.set(Calendar.HOUR_OF_DAY, 23);
				dtFinal.set(Calendar.MINUTE, 59);
				dtFinal.set(Calendar.SECOND, 59);
			}
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			if(criterios == null)
				criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			// 
			String tpDataStr = "";
			if(tpData==0)
				tpDataStr = "A.dt_vencimento";
			else if(tpData==1)
				tpDataStr = "A.dt_emissao";
			else if(tpData==2)
				tpDataStr = "A.dt_recebimento";
			if(dtInicial != null)
				criterios.add(new ItemComparator(tpDataStr, Util.formatDate(dtInicial, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.DATE));
			if(dtFinal != null)
				criterios.add(new ItemComparator(tpDataStr,new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Util.convCalendarToTimestamp(dtFinal)), ItemComparator.MINOR_EQUAL, Types.DATE));
			//
			if(cdCliente > 0)
				criterios.add(new ItemComparator("A.cd_pessoa", String.valueOf(cdCliente), ItemComparator.EQUAL, Types.INTEGER));
			
			criterios.add(new ItemComparator("st_conta", "0", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmTiposDocumentoCheque = ParametroServices.getValoresOfParametro("TP_DOCUMENTO_CHEQUE");
			String complemento = " AND (";
			if(rsmTiposDocumentoCheque != null && rsmTiposDocumentoCheque.size() > 0){
				int cdTipoDocumento = Integer.parseInt(rsmTiposDocumentoCheque.getString("vl_real"));
				complemento += "A.cd_tipo_documento = " + cdTipoDocumento + " OR ";
			}
			while (rsmTiposDocumentoCheque != null && rsmTiposDocumentoCheque.next()) {
				int cdTipoDocumento = Integer.parseInt(rsmTiposDocumentoCheque.getString("vl_real"));
				complemento += "A.cd_tipo_documento = " + cdTipoDocumento + " OR ";
			}
			if(rsmTiposDocumentoCheque.size() > 0){
				complemento = complemento.substring(0, complemento.length() - 4);
				complemento += ")";
			}
			else
				complemento += "A.cd_tipo_documento = -1)";
			criterios.add(new ItemComparator("COMPLEMENTO", complemento, ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsm = find(criterios, connection);
			//
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			
			if (isConnectionNull)
				connection.commit();
			return result;
		}
		catch(Exception e) {
			e.printStackTrace();
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	/**
	 * Rotina para geraï¿½ï¿½o do documento de custodia a partir de uma lista de contas a receber
	 * Da baixa nos cheques que serï¿½o trocados, e adiciona uma conta a pagar com uma porcentagem de acrescimo
	 * @author Gabriel
	 * @since 25/08/2014
	 * @param cdEmpresa
	 * @param tpData
	 * @param dtInicial
	 * @param dtFinal
	 * @param dtTroca
	 * @param cdDestinatario ï¿½ a pessoa para quem os cheques serï¿½o repassados, ele seirï¿½o o cliente que fairï¿½o o pagamento da conta a receber que seirï¿½o recebida imediatamente
	 * @param prTaxa Taxa de desconto dado no recebimento imediato
	 * @param cdUsuario
	 * @param valido Usado para saber se desaja-se realmente fazer a custï¿½dia ou apenas mostrar um documento de pirï¿½o visualizaï¿½ï¿½o
	 * @param listaContaReceber Serï¿½o os cheques que irï¿½o ser trocados por um recebimento imediato
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Result gerarRelatorioCustodia(int cdEmpresa, int tpData, GregorianCalendar dtInicial, GregorianCalendar dtFinal, GregorianCalendar dtTroca, int cdDestinatario, int cdCliente, float prTaxa, 
												int cdUsuario, int valido, boolean lgTodos, ArrayList<Integer> listaContaReceber){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			HashMap<String, Object> dadosCustodia = new HashMap<String, Object>();
			//Parametros usados no documento da custï¿½dia
			HashMap<String, Object> param = new HashMap<String, Object>();
			dadosCustodia.put("DT_TROCA", Util.convCalendarString(dtTroca) );
			dadosCustodia.put("PR_TAXA", prTaxa );
			dadosCustodia.put("CD_CONTA_RECEBER", 0 );
			param.put("dtTroca", Util.convCalendarString(dtTroca));
			param.put("prTaxa", prTaxa);
			param.put("valido", valido);
			//Busca o destinatï¿½rio e suas informaï¿½ï¿½es de nome, cpf/cnpj, inscriï¿½ï¿½o estadual e endereï¿½o para serem colocadas no endereï¿½o
			Pessoa pessoa = PessoaDAO.get(cdDestinatario, connection);
			if(pessoa != null){
				PessoaFisica pessoaFisica     = PessoaFisicaDAO.get(cdDestinatario, connection);
				PessoaJuridica pessoaJuridica = PessoaJuridicaDAO.get(cdDestinatario, connection);
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_pessoa", "" + cdDestinatario, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmEnderecoDestinatario = PessoaEnderecoDAO.find(criterios, connection);
				if(rsmEnderecoDestinatario.next()){
					param.put("nmLogradouro", rsmEnderecoDestinatario.getString("nm_logradouro"));
					param.put("nrEndereco", rsmEnderecoDestinatario.getString("nr_endereco"));
					param.put("nmBairro", rsmEnderecoDestinatario.getString("nm_bairro"));
					Cidade cidade = CidadeDAO.get(rsmEnderecoDestinatario.getInt("cd_cidade"), connection);
					if(cidade != null){
						param.put("nmCidade", cidade.getNmCidade());
						Estado estado = EstadoDAO.get(cidade.getCdEstado(), connection);
						if(estado != null){
							param.put("sgUf", estado.getSgEstado());
						}
					}
				}
				param.put("nmDestinatario", pessoa.getNmPessoa());
				dadosCustodia.put("NM_DESTINATARIO" , pessoa.getNmPessoa());
				if(pessoaFisica != null){
					param.put("nrCpf", pessoaFisica.getNrCpf());
				}
				else if (pessoaJuridica != null){
					param.put("nrCnpj", pessoaJuridica.getNrCnpj());
					param.put("nrInscricaoEstadual", pessoaJuridica.getNrInscricaoEstadual());
				}
			}
			ResultSetMap rsm = new ResultSetMap();
			//Faz a soma dos cheques
			float vlTotal = 0;
			
			//Itera sobre os cheques que serï¿½o trocados
			//Caso a pesquisa nï¿½o tenha selecionada a imagem de "Todos", apenas os selecionados entrarï¿½o no RSM
			if(!lgTodos)
				for(int i = 0; i < listaContaReceber.size(); i++){
					//Busca um rsm do cheque que seirï¿½o dado baixa
					ResultSetMap rsmContaReceberCustodia = getContaReceberCustodia(listaContaReceber.get(i) == null || listaContaReceber.get(i).equals("") || listaContaReceber.get(i).equals("null") ? 0 : (Integer)listaContaReceber.get(i), cdEmpresa, connection);
					rsmContaReceberCustodia.beforeFirst();
					//Configura um registro para jogar as informaï¿½ï¿½es no relatï¿½rio
					if(rsmContaReceberCustodia.next()){
						HashMap<String, Object> register = new HashMap<String, Object>( dadosCustodia );
						register.put("NM_CLIENTE", rsmContaReceberCustodia.getString("NM_PESSOA"));
						register.put("NM_EMISSOR", rsmContaReceberCustodia.getString("NM_EMISSOR"));
						register.put("NR_DOCUMENTO", rsmContaReceberCustodia.getString("NR_DOCUMENTO"));
						register.put("NR_AGENCIA", rsmContaReceberCustodia.getString("NR_AGENCIA_TITULO_CREDITO"));
						register.put("NM_BANCO", rsmContaReceberCustodia.getString("NM_INSTITUICAO_FINANCEIRA"));
						Double vlCheque = rsmContaReceberCustodia.getDouble("VL_CONTA") - (rsmContaReceberCustodia.getDouble("VL_RECEBIDO") + rsmContaReceberCustodia.getDouble("VL_ABATIMENTO") - rsmContaReceberCustodia.getDouble("VL_ACRESCIMO"));
						vlTotal += vlCheque;
						register.put("VL_CHEQUE", (vlCheque < 0 ? 0 : vlCheque));
						int qtDias = Util.getQuantidadeDias( dtTroca, Util.convTimestampToCalendar(rsmContaReceberCustodia.getTimestamp("DT_VENCIMENTO")))+1;
						register.put("QT_DIAS", qtDias);
						register.put("DT_VENCIMENTO", (Util.convTimestampToCalendar(rsmContaReceberCustodia.getTimestamp("DT_VENCIMENTO")) == null || Util.convTimestampToCalendar(rsmContaReceberCustodia.getTimestamp("DT_VENCIMENTO")).equals("") || Util.convTimestampToCalendar(rsmContaReceberCustodia.getTimestamp("DT_VENCIMENTO")).equals("null")  ? "" : Util.formatDate(Util.convTimestampToCalendar(rsmContaReceberCustodia.getTimestamp("DT_VENCIMENTO")), "dd/MM/yyyy")));
						
						Double vlJuros = ((((double)qtDias / (double)30) * prTaxa) / 100) * (vlCheque < 0 ? 0 : vlCheque);
						Double vlReceber = (vlCheque < 0 ? 0 : vlCheque) - vlJuros;
						
						register.put("VL_RECEBER_CHEQUE", vlReceber);
						register.put("VL_JUROS", vlJuros);
						
						rsm.addRegister(register);
					}
				}
			//Caso a pesquisa tenha selecionada a imagem de "Todos", apenas os nï¿½o selecionados nï¿½o entrarï¿½o no RSM, por isso o find seirï¿½o refeito, e os codigos
			//dos nï¿½o selecionados serï¿½o retirados
			else{
				//Coloca no criterios os codigos que nï¿½o devem ser buscados
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				ArrayList<Integer> codigos = new ArrayList<Integer>();
				for(int i = 0; i < listaContaReceber.size(); i++){
					codigos.add((Integer)listaContaReceber.get(i));
				}
				listaContaReceber = new ArrayList<Integer>();
				if(codigos.size() > 0)
					criterios.add(new ItemComparator("A.cd_conta_receber", codigos.toString().substring(1, codigos.toString().length()-1), ItemComparator.NOTIN, Types.INTEGER));
				
				rsm = (ResultSetMap)findCustodia(cdEmpresa, tpData, dtInicial, dtFinal, cdCliente, criterios, connection).getObjects().get("rsm");
				while(rsm.next()){
					rsm.setValueToField("NM_CLIENTE", rsm.getString("NM_PESSOA"));
					rsm.setValueToField("NM_EMITENTE", rsm.getString("NM_EMISSOR"));
					rsm.setValueToField("NR_CHEQUE", rsm.getString("NR_DOCUMENTO"));
					rsm.setValueToField("NR_AGENCIA", rsm.getString("NR_AGENCIA_TITULO_CREDITO"));
					rsm.setValueToField("NM_BANCO", rsm.getString("NM_INSTITUICAO_FINANCEIRA"));
					Double vlCheque = rsm.getDouble("VL_CONTA") - (rsm.getDouble("VL_RECEBIDO") + rsm.getDouble("VL_ABATIMENTO") - rsm.getDouble("VL_ACRESCIMO"));
					vlTotal += vlCheque;
					rsm.setValueToField("VL_CHEQUE", (vlCheque < 0 ? 0 : vlCheque));
					int qtDias = Util.getQuantidadeDiasUteis(Util.convTimestampToCalendar(rsm.getTimestamp("DT_VENCIMENTO")), Util.getDataAtual(), connection);
					rsm.setValueToField("NR_DIAS", qtDias);
					rsm.setValueToField("DT_VENCIMENTO", (Util.convTimestampToCalendar(rsm.getTimestamp("DT_VENCIMENTO")) == null || Util.convTimestampToCalendar(rsm.getTimestamp("DT_VENCIMENTO")).equals("") || Util.convTimestampToCalendar(rsm.getTimestamp("DT_VENCIMENTO")).equals("null")  ? "" : Util.formatDate(Util.convTimestampToCalendar(rsm.getTimestamp("DT_VENCIMENTO")), "dd/MM/yyyy")));
					
					//Coloca os cï¿½digos usados no relatï¿½rio para que seja dada a baixa caso imprima
					listaContaReceber.add((Integer) rsm.getInt("cd_conta_receber"));
				}
			}
			rsm.beforeFirst();
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			
			result.addObject("params", param);
			
			//Caso essa vairavel seja 1, entï¿½o o sistema iirï¿½o gerar o recebimento e dar baixa nos cheques
			if(valido==1){
				//Conta carteira padrï¿½o cadastrada para a troca de cheque
				int cdContaCarteiraFac    = ParametroServices.getValorOfParametroAsInteger("CD_CARTEIRA", 0, cdEmpresa);
				if(cdContaCarteiraFac <= 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Parametro de conta carteira para custï¿½dia nï¿½o configurado!");
				}
				//Conta padrï¿½o cadastrada para a troca de cheque
				int cdConta 		      = ParametroServices.getValorOfParametroAsInteger("CD_CONTA", 0, cdEmpresa);
				if(cdConta <= 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Parametro de conta para custï¿½dia nï¿½o configurado!");
				}
				//Calculo do abatimento que o recebimento imediato iirï¿½o sofrer
				float vlAbatimento		  = vlTotal * (prTaxa / 100);
				//Forma de pagamento padrï¿½o para o recebimento imediato - DINHEIRO
				int cdFormaPagamento      = ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_DINHEIRO", 0);
				if(cdFormaPagamento <= 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Parametro de forma de pagamento dinheiro nï¿½o configurado!");
				}
				//Realiza a baixa automï¿½tica dos cheques no sistema
				Result resultado = setBaixaResumidaCustodia(cdEmpresa, cdDestinatario, cdConta, cdContaCarteiraFac, vlTotal, cdFormaPagamento, 0, null, cdUsuario, dtTroca, 0/*stCheque*/, 0, listaContaReceber, vlAbatimento, prTaxa, connection);
				if(resultado.getCode() < 0){
					int code = resultado.getCode();
					
					if(isConnectionNull)
						Conexao.rollback(connection);
					//Caso haja erro no movimento ou na classificaï¿½ï¿½o, o relatï¿½rio a ser mostrado seirï¿½o outro
					//Mostrando as diferenï¿½as de valores que precisarï¿½o ser corrigidas antes do cheque ser dado baixa
					if(code == ERR_MOVIMENTO || code == ERR_CLASSIFICACAO){
						
						String listaCodigos = null;
						for(Integer cdContaReceber : listaContaReceber){
							if(listaCodigos == null)
								listaCodigos = new String(cdContaReceber+"");
							else
								listaCodigos += ", " + cdContaReceber;
						}
						
						ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("A.cd_conta_receber", listaCodigos, ItemComparator.IN , Types.INTEGER));
						if(code == ERR_CLASSIFICACAO){
							criterios.add(new ItemComparator("lgconferencia", "1", ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("lgcategoria", "1", ItemComparator.EQUAL, Types.INTEGER));
						}
						if(code == ERR_MOVIMENTO){
							criterios.add(new ItemComparator("lgconferencia", "1", ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("lgcategoria", "1", ItemComparator.EQUAL, Types.INTEGER));
						}
						System.out.println("resultado.getCode() = " + resultado.getCode());
						System.out.println("resultado.getMessage() = " + resultado.getMessage());
						
						resultado = gerarRelatorioContasReceber(criterios, 0);
						//UTILIZAR A CONSTANTE NO LUGAR CORRETO.
						resultado.setCode(ReportServices.DESVIO_RELATORIO);
						param = (HashMap<String, Object>) resultado.getObjects().get("params");
						
						if(code == ERR_CLASSIFICACAO)
							param.put("nmTitulo", "ERRO NA CONFERï¿½NCIADAS CATEGORIAS");
						if(code == ERR_MOVIMENTO)
							param.put("nmTitulo", "ERRO NA CONFERï¿½NCIADAS MOVIMENTAï¿½ï¿½ES");
						System.out.println("param - " + param);
						resultado.addObject("params", param);
						resultado.addObject("novoJasper", "relatorio_contas_receber_custodia");
						resultado.addObject("novoModulo", "adm");
						
					}
					return resultado;
				}
			}
			
			if(isConnectionNull)
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
	 * @author Alvaro
	 * @since 28/10/2014
	 * @param criterios
	 * @return Result
	 */
	public static Result gerarRelatorioCustodia(ArrayList<ItemComparator> criterios){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			HashMap<String, Object> param = new HashMap<String, Object>();
			ResultSetMap rsm = Search.find("SELECT A.CD_CONTA_RECEBER, A.VL_CONTA, A.VL_ABATIMENTO, A.VL_RECEBIDO, "+ 
					" C.nm_pessoa as NM_DESTINATARIO,  "+
					" C2.NR_CPF, C3.NR_CNPJ, C3.NR_INSCRICAO_ESTADUAL, C3.NM_RAZAO_SOCIAL, "+
					" C4.NM_BAIRRO, C5.NM_CIDADE, C6.SG_ESTADO,  "+
					" C4.NM_LOGRADOURO, C4.NR_ENDERECO, "+
					" B.PR_JUROS, B.QT_DIAS, "+
					" D.DT_RECEBIMENTO AS DT_TROCA, "+
					" D.VL_RECEBIDO AS VL_RECEBIDO_CHEQUE, "+
					" D.VL_CONTA + D.VL_ACRESCIMO - D.VL_ABATIMENTO  as VL_RECEBER, "+
					" D2.VL_TITULO AS VL_CHEQUE, "+
					" D2.DT_VENCIMENTO, D2.NM_EMISSOR, D2.NR_AGENCIA, D2.NR_DOCUMENTO, "+
					" D3.NM_BANCO, D4.NM_PESSOA as NM_CLIENTE "+
					" FROM adm_conta_receber A "+
					" INNER JOIN adm_conta_factoring B ON (A.cd_conta_receber = B.cd_conta_receber ) "+
					" INNER JOIN grl_pessoa C ON ( A.cd_pessoa = C.cd_pessoa ) "+
					" INNER JOIN adm_conta_receber D ON ( B.cd_conta_receber_antecipada = D.cd_conta_receber ) "+
					" INNER JOIN adm_titulo_credito D2 ON ( B.cd_conta_receber_antecipada = D2.cd_conta_receber ) "+
					" LEFT OUTER JOIN grl_banco D3 ON ( D2.cd_instituicao_financeira = D3.cd_banco ) "+
					" LEFT OUTER JOIN grl_pessoa D4 ON ( D.cd_pessoa = D4.cd_pessoa ) "+
					" LEFT OUTER JOIN grl_pessoa_fisica C2 ON ( A.cd_pessoa = C2.cd_pessoa ) "+
					" LEFT OUTER JOIN grl_pessoa_juridica C3 ON ( A.cd_pessoa = C3.cd_pessoa ) "+
					" LEFT OUTER JOIN grl_pessoa_endereco C4 ON ( C.cd_pessoa = C4.cd_pessoa AND C4.lg_principal = 1 ) "+
					" LEFT OUTER JOIN grl_cidade C5 ON ( C4.cd_cidade = C5.cd_cidade ) "+
					" LEFT OUTER JOIN grl_estado C6 ON ( C5.cd_estado = C6.cd_estado ) "+
					" WHERE 1=1 ",
					" ORDER BY A.CD_CONTA_RECEBER, NM_CLIENTE, NM_EMISSOR ", criterios, connection,  isConnectionNull); 
					
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			
			if(isConnectionNull)
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
	
	public static Result gerarRecebimentos(int cdEmpresa, int cdUsuario, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			int cdContaPadraoRecebimento = ParametroServices.getValorOfParametroAsInteger("CD_CONTA_PADRAO_RECEBIMENTO", 0, cdEmpresa);
			int cdTipoDocumentoCheque    = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_CHEQUE", 0);
			int cdFormaPagamento         = ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_DINHEIRO", 0);
			
			if(dtFinal.before(dtInicial))
				return new Result(-1, "A data final nï¿½o pode ser menor do que a data inicial!");
			if(cdContaPadraoRecebimento == 0)
				return new Result(-1, "Configure o parametro de Conta Padrï¿½o de Recebimento no modulo Financeiro!");
			if(cdFormaPagamento == 0)
				return new Result(-1, "Configure o parametro de Forma de Pagamento em Dinheiro no modulo Financeiro!");
			if(cdTipoDocumentoCheque == 0)
				return new Result(-1, "Configure o parametro de tipo de documento Cheque no modulo Financeiro!");
			
			GregorianCalendar dtMovimento = (GregorianCalendar)dtInicial.clone();
			ArrayList<Result> resultados = new ArrayList<Result>();
			while(dtFinal.after(dtMovimento)){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("A.dt_vencimento", Util.formatDateTime(dtMovimento, "dd/MM/yyyy"), ItemComparator.EQUAL, Types.TIMESTAMP));
				criterios.add(new ItemComparator("H.cd_tipo_documento", Integer.toString(cdTipoDocumentoCheque), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("A.st_conta", Integer.toString(ContaReceberServices.ST_EM_ABERTO), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmContasEmpresa = ContaReceberServices.find(criterios);
				ArrayList<Integer> codigosContaReceber = new ArrayList<Integer>();
				float vlMovimento = 0;
				int cdConta = cdContaPadraoRecebimento;
				while(rsmContasEmpresa.next()){
					Double vlAReceber = rsmContasEmpresa.getDouble("vl_conta") - rsmContasEmpresa.getDouble("vl_recebido") - rsmContasEmpresa.getDouble("vl_abatimento") + rsmContasEmpresa.getDouble("vl_acrescimo");
//					ContaFinanceira contaTitulo = ContaFinanceiraDAO.get(rsmContasEmpresa.getInt("cd_conta_titulo_credito"), connection);
//					ContaFinanceira conta 		= ContaFinanceiraDAO.get(rsmContasEmpresa.getInt("cd_conta"), connection);
//					if(contaTitulo.getTpConta() == ContaFinanceiraServices.TP_CONTA_BANCARIA)
//						cdConta = rsmContasEmpresa.getInt("cd_conta_titulo_credito");
//					else if(conta.getTpConta() == ContaFinanceiraServices.TP_CONTA_BANCARIA){
//						cdConta = rsmContasEmpresa.getInt("cd_conta");
//					}
//					else
					if(vlAReceber > 0){
						vlMovimento += vlAReceber;
						codigosContaReceber.add(rsmContasEmpresa.getInt("cd_conta_receber"));
					}
				}
				if(vlMovimento > 0){
					Result resultadoMov = setRecebimentoResumido(codigosContaReceber, cdConta, cdFormaPagamento, cdUsuario, new GregorianCalendar(), -1, connection);
					resultados.add(resultadoMov);
				}
				dtMovimento.add(Calendar.DAY_OF_MONTH, 1);
			}
			
//			if (isConnectionNull)
//				connection.commit();
			
			Result resultadoFinal = new Result(1, "Operaï¿½ï¿½o realizada com sucesso!");
			
			resultadoFinal.addObject("RESULTADOS", resultados);
			
			return resultadoFinal;
			
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
	
	public static Result gerarRelatorioContasReceber(ArrayList<sol.dao.ItemComparator> criterios, int tpRelatorio){
		return gerarRelatorioContasReceber(criterios, tpRelatorio, null);
	}
	public static Result gerarRelatorioContasReceber(ArrayList<sol.dao.ItemComparator> criterios, int tpRelatorio, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSetMap rsmContasReceberGroupClientes = null;
			boolean lgCategoria = false, lgConferencia = false;
			for(int i=0; i<criterios.size();i++){
				if(criterios.get(i).getColumn().equalsIgnoreCase("lgcategoria"))	{
					criterios.remove(i);
					lgCategoria = true;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("lgconferencia"))	{
					criterios.remove(i);
					lgConferencia = true;
				}
			}
			ResultSetMap rsmContasReceber = find( criterios, connect );
			HashMap<String, Object> paramns = new HashMap<String, Object>();
			/**
			 * Preenche o valor de NM_PESSOA com NM_FANTASIA quando aquele for nulo, 
			 * pois no momento da ordenaï¿½ï¿½o ou da recuperaï¿½ï¿½o via register.getString("NM_PESSOA")
			 * sï¿½o lanï¿½adas excessï¿½es caso NM_PESSOA seja nulo 
			 */
			while( rsmContasReceber.next() ){
				if( rsmContasReceber.getObject("NM_PESSOA") == null )
					rsmContasReceber.setValueToField("NM_PESSOA", rsmContasReceber.getString("NM_FANTASIA"));
			}
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("NR_DOCUMENTO");
			rsmContasReceber.orderBy(orderBy, true);
			rsmContasReceber.next();
			
			/* Relatï¿½rio Agrupado por Clientes */
			if( tpRelatorio == 1 ){
				paramns.put("ORDERBY_PESSOA", 1);
				rsmContasReceberGroupClientes = new ResultSetMap();
				rsmContasReceber.beforeFirst();
				while( rsmContasReceber.next() ){
					HashMap<String, Object> newRegister = rsmContasReceber.getRegister();
					if( rsmContasReceberGroupClientes.size() == 0 
						|| !rsmContasReceberGroupClientes.getString("NM_PESSOA").equals(  newRegister.get("NM_PESSOA") ) ){
						rsmContasReceberGroupClientes.addRegister( newRegister );
						rsmContasReceberGroupClientes.next();
						
					}else{
						rsmContasReceberGroupClientes.setValueToField("VL_CONTA",  rsmContasReceberGroupClientes.getDouble("VL_CONTA") + (Double) newRegister.get("VL_CONTA") );
						rsmContasReceberGroupClientes.setValueToField("VL_ACRESCIMO", rsmContasReceberGroupClientes.getDouble("VL_ACRESCIMO") + (Double) newRegister.get("VL_ACRESCIMO") );
						rsmContasReceberGroupClientes.setValueToField("VL_ABATIMENTO", rsmContasReceberGroupClientes.getDouble("VL_ABATIMENTO") + (Double) newRegister.get("VL_ABATIMENTO") );
						rsmContasReceberGroupClientes.setValueToField("VL_RECEBIDO", rsmContasReceberGroupClientes.getDouble("VL_RECEBIDO") + (Double) newRegister.get("VL_RECEBIDO") );
					}
				}
				rsmContasReceber = rsmContasReceberGroupClientes;
			}
			
			/*Totalizaï¿½ï¿½o de valores*/
			Double vlContaTotal = 0.0, descontoTotal=0.0, acrescimoTotal=0.0, recebidoTotal=0.0, areceber=0.0;
			rsmContasReceber.beforeFirst();
			while( rsmContasReceber.next() ){
				vlContaTotal += rsmContasReceber.getDouble("VL_CONTA");
				acrescimoTotal += rsmContasReceber.getDouble("VL_ACRESCIMO");
				descontoTotal += rsmContasReceber.getDouble("VL_ABATIMENTO");
				recebidoTotal += rsmContasReceber.getDouble("VL_RECEBIDO");
				if( rsmContasReceber.getInt("ST_CONTA") == ST_EM_ABERTO ){
					areceber += rsmContasReceber.getDouble("VL_CONTA") - rsmContasReceber.getDouble("VL_RECEBIDO") + rsmContasReceber.getDouble("VL_ACRESCIMO") - rsmContasReceber.getDouble("VL_ABATIMENTO");
				}
			}
			rsmContasReceber.beforeFirst();
			PreparedStatement pstmt = connect.prepareStatement(
					   "	SELECT A.*, B.* FROM adm_conta_receber_categoria A " +
			           "		JOIN adm_categoria_economica B ON (A.cd_categoria_economica = B.cd_categoria_economica)" +
			           "	WHERE A.cd_conta_receber = ?");
			
			
			while(lgCategoria && rsmContasReceber.next()){
				String dsCategoria = "", dsAlerta = "";
				Double vlAClassificar = rsmContasReceber.getDouble("vl_conta")-rsmContasReceber.getDouble("vl_abatimento")+rsmContasReceber.getDouble("vl_acrescimo");
				pstmt.setInt(1, rsmContasReceber.getInt("cd_conta_receber"));
				ResultSetMap rsmCategoriasConta = new ResultSetMap(pstmt.executeQuery());
				while(rsmCategoriasConta.next())	{
					dsCategoria    += (dsCategoria.equals("") ? "" : ", ")+rsmCategoriasConta.getString("nr_categoria_economica")+"-"+
					                                                       rsmCategoriasConta.getString("nm_categoria_economica");
					int tpCategoria = rsmCategoriasConta.getInt("tp_categoria_economica");
					if(tpCategoria == CategoriaEconomicaServices.TP_DESPESA || tpCategoria == CategoriaEconomicaServices.TP_DEDUCAO_RECEITA)
						vlAClassificar += rsmCategoriasConta.getDouble("vl_conta_categoria");
					else
						vlAClassificar -= rsmCategoriasConta.getDouble("vl_conta_categoria");
					// Verificando se alguma categoria de despesa foi usada
					if(lgConferencia)	{
						if((tpCategoria!=CategoriaEconomicaServices.TP_RECEITA && tpCategoria!=CategoriaEconomicaServices.TP_DEDUCAO_RECEITA) &&
							rsmCategoriasConta.getDouble("vl_conta_categoria")>rsmContasReceber.getDouble("vl_abatimento"))
							dsAlerta += (dsAlerta.equals("") ? "" : ", ")+"Categoria de despesa";
					}
				}
				if(lgConferencia && (vlAClassificar>0.009 || vlAClassificar<0) && !Util.formatNumber(Math.abs(vlAClassificar)).equals("0,00"))
					dsAlerta += (dsAlerta.equals("") ? "" : ", ")+"Diferenï¿½a na classificacao: "+Util.formatNumber(Math.abs(vlAClassificar));
				
				if(dsAlerta.equals(""))
					dsAlerta = "OK";
				paramns.put("LABEL_CATEGORIA", "Categoria Econï¿½mica");
				rsmContasReceber.setValueToField("DS_CATEGORIA_ECONOMICA", dsCategoria);
				paramns.put("LABEL_ALERTA", "Alerta");
				rsmContasReceber.setValueToField("DS_ALERTA", dsAlerta);
			}
			rsmContasReceber.beforeFirst();
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NM_PESSOA", "TOTAL");
			register.put("VL_CONTA", vlContaTotal);
			register.put("VL_ACRESCIMO", acrescimoTotal);
			register.put("VL_ABATIMENTO", descontoTotal);
			register.put("VL_RECEBIDO", recebidoTotal);
			rsmContasReceber.addRegister(register);
			/**************************************************/
			
			paramns.put("VL_CONTA", vlContaTotal);
			paramns.put("VL_ACRESCIMO", acrescimoTotal);
			paramns.put("VL_ABATIMENTO", descontoTotal);
			paramns.put("VL_ARECEBER", areceber);
			
			Result result = new Result(1);
			result.addObject("rsm", rsmContasReceber);
			result.addObject("params",paramns );
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar gerar relatï¿½rio de contas a receber!", e);
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result setRecebimentoResumido(ArrayList<Integer> codigosConta, int cdConta, int cdFormaPagamento, int cdUsuario, GregorianCalendar dtRecebimento, int stCheque, Connection connect){
		return setRecebimentoResumido(codigosConta, cdConta, cdFormaPagamento, 0, null, cdUsuario, dtRecebimento, stCheque, 0, 0, connect);
	}
	
	
	public static Result setRecebimentoResumido(ArrayList<Integer> codigosConta, int cdConta, int cdFormaPagamento, int cdCheque, String dsHistorico, int cdUsuario, GregorianCalendar dtRecebimento, int stCheque,
			int cdTituloCredito, int cdTurno, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			/*
			 *  Verifica a Forma de Pagamento
			 */
			FormaPagamento formaPagamento = FormaPagamentoDAO.get(cdFormaPagamento, connect);
			if(formaPagamento.getTpFormaPagamento()==FormaPagamentoServices.TITULO_CREDITO &&
					formaPagamento.getNmFormaPagamento().toLowerCase().indexOf("cheque")>=0 && (cdCheque<=0))	{
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-10, "Recebimento em tï¿½tulo de crï¿½dito sï¿½o ï¿½ permitido com a informaï¿½ï¿½o do cheque!");
			}
			
			String nrDocumento = "";
			if(cdCheque > 0)
				nrDocumento = ChequeDAO.get(cdCheque, connect).getNrCheque();
			/*
			 *  Cria o movimento de conta
			 */
			int stMovimento = MovimentoContaServices.ST_COMPENSADO;
			if(dtRecebimento.after(new GregorianCalendar()) || cdCheque>0)
				stMovimento = MovimentoContaServices.ST_NAO_COMPENSADO;
			ArrayList<MovimentoContaReceber> movimentoContaReceber = new ArrayList<MovimentoContaReceber>();
			ArrayList<MovimentoContaCategoria> movimentoCategoria = new ArrayList<MovimentoContaCategoria>();
			ArrayList<MovimentoContaTituloCredito> titulos = new ArrayList<MovimentoContaTituloCredito>();
			ResultSetMap registrosErrados = new ResultSetMap();
			float vlTotalAReceber = 0;
			for(int i = 0; i < codigosConta.size(); i++){
				int cdContaReceber = codigosConta.get(i);
				/*
				 *  Instancia conta a receber
				 */
				ContaReceber conta = ContaReceberDAO.get(cdContaReceber, connect);
				Double vlAReceber = conta.getVlConta() - conta.getVlRecebido() - conta.getVlAbatimento() + conta.getVlAcrescimo();
				vlTotalAReceber += vlAReceber;
				/*
				 *  Cria o pagamento
				 */
				ArrayList<MovimentoContaReceber> movimentoContaReceberTemp = new ArrayList<MovimentoContaReceber>();
				ArrayList<MovimentoContaCategoria> movimentoCategoriaTemp = new ArrayList<MovimentoContaCategoria>();
				
				
				movimentoContaReceberTemp.add(new MovimentoContaReceber(cdConta, 0 /*cdMovimentoConta)*/, cdContaReceber, vlAReceber.floatValue(), 0, 0, 0, 0, 0, 0));
				
				/*
				 *  Cria a classificaï¿½ï¿½o em categorias do movimento na conta
				 */
				float vlTotalClassificado = 0;
				ResultSetMap rsmCategorias = ContaReceberCategoriaServices.getCategoriaOfContaReceber(cdContaReceber, connect);
				float vlCategoriaNaoDespesa = 0;
				float vlCategoriaDespesa    = 0;
				while(rsmCategorias.next()){ 
					if(rsmCategorias.getDouble("tp_categoria_economica") != CategoriaEconomicaServices.TP_DESPESA)
						vlCategoriaNaoDespesa += rsmCategorias.getDouble("vl_conta_categoria");
					else
						vlCategoriaDespesa += rsmCategorias.getDouble("vl_conta_categoria");
					
					Double vlMovimentoCategoria = rsmCategorias.getDouble("vl_conta_categoria") / (conta.getVlConta() - conta.getVlAbatimento()) * vlAReceber;
					vlTotalClassificado += vlMovimentoCategoria;
					movimentoCategoriaTemp.add(new MovimentoContaCategoria(cdConta, 0/*cdMovimentoConta*/, rsmCategorias.getInt("cd_categoria_economica"),
								                                       vlMovimentoCategoria.floatValue(), 0 /*cdMovimentoContaCategoria*/, 0, cdContaReceber/*cdContaReceber*/,
								                                       (rsmCategorias.getInt("tp_categoria_economica") == CategoriaEconomicaServices.TP_DESPESA ? MovimentoContaCategoriaServices.TP_DESCONTO : MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO)/*tpMovimento*/, rsmCategorias.getInt("cd_centro_custo")));
				}
								
				if (vlTotalClassificado+0.01 < vlAReceber || 
					(vlCategoriaNaoDespesa != (conta.getVlConta() + conta.getVlAcrescimo())) || 
					(vlCategoriaDespesa != (conta.getVlAbatimento()))){
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("ERRO", "Conta a receber com Nï¿½o " + conta.getNrDocumento() + " com erro na soma do valor das suas categorias!");
						registrosErrados.addRegister(register);
						vlTotalAReceber -= vlAReceber;
				}
				
				else{
					for(int j = 0; j < movimentoCategoriaTemp.size(); j++){
						movimentoCategoria.add(movimentoCategoriaTemp.get(j));
					}
					for(int j = 0; j < movimentoContaReceberTemp.size(); j++){
						movimentoContaReceber.add(movimentoContaReceberTemp.get(j));
					}
					if(cdTituloCredito > 0)
						titulos.add(new MovimentoContaTituloCredito(cdTituloCredito,0/*cdMovimentoConta*/,cdConta));
				}
				
			}
			MovimentoConta movimento = new MovimentoConta(0 /*cdMovimentoConta*/,cdConta,0 /*cdContaOrigem*/,0 /*cdMovimentoOrigem*/,
					  cdUsuario,cdCheque,0 /*cdViagem*/,dtRecebimento,vlTotalAReceber,nrDocumento,
					  MovimentoContaServices.CREDITO, MovimentoContaServices.toCHEQUE /*tpOrigem*/, stMovimento,dsHistorico,
					  null /*dtDeposito*/,null /*idExtrato*/,cdFormaPagamento,0 /*cdFechamento*/, cdTurno);

			/*
			 *  Chama o mï¿½todo que faz o lanï¿½amento da conta e outras operaï¿½ï¿½es
			 */
			Result result = MovimentoContaServices.insert(movimento, movimentoContaReceber, movimentoCategoria, titulos, -1/*stCheque*/, connect);
			if(result.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				com.tivic.manager.util.Util.registerLog(new Exception("Recebimento Resumido: Nao foi possivel salvar o movimento!"));
				result.setMessage("Recebimento Resumido: Nao foi possivel salvar o movimento! "+result.getMessage());
				return result;
			}

			/*
			 *  Altera situaï¿½ï¿½o do cheque
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


			Result resultado = new Result(1, "Contas lanï¿½adas com sucesso!");
			if(registrosErrados.getLines().size() > 0){
				resultado.setMessage("Contas com erros!");
				resultado.addObject("ERRO", registrosErrados);
			}
			return resultado;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar lanï¿½ar recebimento resumido!", e);
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	/**
	 * Metodo para fazer a baixa de uma lista de contas a receber
	 * @since 25/08/2014
	 * @author Gabriel
	 * @param cdEmpresa
	 * @param cdDestinatario
	 * @param cdConta
	 * @param cdContaCarteira
	 * @param vlPago
	 * @param cdFormaPagamento
	 * @param cdCheque
	 * @param dsHistorico
	 * @param cdUsuario
	 * @param dtRecebimento
	 * @param stCheque
	 * @param cdTurno
	 * @param listaContaReceber
	 * @param vlAbatimento
	 * @param prTaxa
	 * @return
	 */
	public static Result setBaixaResumidaCustodia(int cdEmpresa, int cdDestinatario, int cdConta, int cdContaCarteira, float vlPago, int cdFormaPagamento, int cdCheque, String dsHistorico, int cdUsuario, GregorianCalendar dtRecebimento, 
										  int stCheque, int cdTurno, ArrayList<Integer> listaContaReceber, float vlAbatimento, float prTaxa)
	{
		return setBaixaResumidaCustodia(cdEmpresa, cdDestinatario, cdConta,cdContaCarteira, vlPago, cdFormaPagamento, cdCheque, dsHistorico, cdUsuario, dtRecebimento, stCheque, cdTurno, listaContaReceber, vlAbatimento, prTaxa, null);
	}
	
	public static Result setBaixaResumidaCustodia(int cdEmpresa, int cdDestinatario, int cdConta, int cdContaCarteira, float vlPago, int cdFormaPagamento, int cdCheque, String dsHistorico, int cdUsuario, GregorianCalendar dtRecebimento, 
										  int stCheque, int cdTurno, ArrayList<Integer> listaContaReceber, float vlAbatimento, float prTaxa, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			Result result = new Result(1, "Sucesso!");
			
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			//Lista dos movimentos de conta a receber
			ArrayList<MovimentoContaReceber> movimentoContaReceber = new ArrayList<MovimentoContaReceber>();
			//Lista dos movimentos de categoria
			ArrayList<MovimentoContaCategoria> movimentoCategoria = new ArrayList<MovimentoContaCategoria>();
			//Valor recebido total
			float vlRecebidoTotal = 0;
			//Parametro de Forma de Pagamento para compensaï¿½ï¿½o de cheque
			int cdFormaPagamentoCompensacao = ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_COMPENSACAO", 0);
			if(cdFormaPagamentoCompensacao <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Parametro de forma de pagamento para compensaï¿½ï¿½o nï¿½o cadastrado!");
			}
			for(int i = 0; i < listaContaReceber.size(); i++){
				int cdContaReceber 	  = listaContaReceber.get(i)==null || listaContaReceber.get(i)==null ? 0 : (Integer) listaContaReceber.get(i);
				ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber, connect);
				Double vlRecebido = (contaReceber.getVlConta() - (contaReceber.getVlRecebido() + contaReceber.getVlAbatimento() - contaReceber.getVlAcrescimo()));
				Double vlDesconto = vlRecebido * (prTaxa / 100);
				Double vlRecebidoFinal = vlRecebido-vlDesconto;
				vlRecebidoTotal += vlRecebidoFinal;
				
				/*
				 *  Cria o recebimento
				 */
				
				movimentoContaReceber.add(new MovimentoContaReceber(cdConta, 0 /*cdMovimentoConta)*/,
																			  cdContaReceber, vlRecebidoFinal.floatValue(),
																			  0/*vlJuros*/,0/*vlMulta*/,vlDesconto.floatValue()/*vlDesconto*/,
																			  0 /*vlTarifaCobranca*/,0 /*cdArquivo*/,
																			  0 /*cdRegistro*/));
				/*
				 *  Cria a classificaï¿½ï¿½o em categorias do movimento na conta
				 */
				float vlTotalClassificado = 0;
				
				ResultSetMap rsmCategorias = ContaReceberCategoriaServices.getCategoriaOfContaReceber(cdContaReceber, connect);
				if(rsmCategorias.size()==0){
					int cdCategoriaAReceberRepasseCheque = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_CHEQUE_A_PRAZO", 0, cdEmpresa);
					if(ContaReceberCategoriaDAO.insert(new ContaReceberCategoria(cdContaReceber, cdCategoriaAReceberRepasseCheque, vlRecebidoFinal.floatValue(), 0), connect) <=0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao inserir categoria da conta a receber!");
					}
				}
				//Faz comparaï¿½ï¿½es para saber se o valor das categorias estï¿½o correto
				rsmCategorias = ContaReceberCategoriaServices.getCategoriaOfContaReceber(cdContaReceber, connect);
				rsmCategorias.beforeFirst();
				while(rsmCategorias.next()){
					Double vlMovimentoCategoria = rsmCategorias.getDouble("vl_conta_categoria") / (contaReceber.getVlConta() - contaReceber.getVlAbatimento() + contaReceber.getVlAcrescimo()) * 
							                                                                    vlRecebidoFinal;
					if(rsmCategorias.getInt("tp_categoria_economica") == 0)
						vlTotalClassificado += vlMovimentoCategoria;
					else
						vlTotalClassificado -= vlMovimentoCategoria;
					movimentoCategoria.add(new MovimentoContaCategoria(cdConta, 0/*cdMovimentoConta*/, rsmCategorias.getInt("cd_categoria_economica"),
																	   vlMovimentoCategoria.floatValue(), 0 /*cdMovimentoContaCategoria*/, 0 /*cdContaReceberPrincipal*/,
																	   cdContaReceber, MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /*tpMovimento*/, rsmCategorias.getInt("cd_centro_custo")));
				}
				if (vlTotalClassificado+0.01 < vlRecebidoFinal)	{
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(ERR_CLASSIFICACAO, "setBaixaResumida Custodia: Conta nï¿½o classificada correntamente! [Valor Total Classificado: "+vlTotalClassificado+", Valor da Conta: "+(vlRecebidoFinal)+"]");
				}
				
			}
			//Tira o valor da taxa do recebimento final
//			float vlRecebidoFinal = vlRecebidoTotal * (1 - (prTaxa / 100));
			//Busca o nï¿½mero do documento
			String nrDocumento  = getProximoNrDocumentoCustodia(cdEmpresa);
			//Busca o tipo de documento parametrizado para custï¿½dia
			int cdTipoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_CUSTODIA", 0);
			if(cdTipoDocumento <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Parametro de tipo de documento para custï¿½dia nï¿½o configurado!");
			}
			//Busca o tipo de conta a receber parametrizado para custï¿½dia
			int tpContaReceber  = ParametroServices.getValorOfParametroAsInteger("TP_CONTA_RECEBER_CUSTODIA", 0, cdEmpresa);
			if(tpContaReceber <= -1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Parametro de tipo de documento para custï¿½dia nï¿½o configurado!");
			}
			
			//Faz o recebimento imediato a partir das informaï¿½ï¿½es passadas
			int cdContaReceberPrincipal = ContaReceberDAO.insert(new ContaReceber(0, cdDestinatario, cdEmpresa, 0, 0, 0, 
																cdContaCarteira, cdConta, 0, nrDocumento, null, 1, null,
																cdTipoDocumento, null, dtRecebimento, dtRecebimento,
																dtRecebimento, null, Double.valueOf( Float.toString(vlPago) ),
																Double.valueOf( Float.toString(vlAbatimento)), 0.0d,
																Double.valueOf( Float.toString(vlPago - vlAbatimento)),
																ContaReceberServices.ST_RECEBIDA, 0, 1, tpContaReceber, 0,
																null, 0, 0, null, dtRecebimento, 0,
																0.0d/*prJuros*/, 0.0d/*prMulta*/, 0/*lgProtesto*/), connect);
			if(cdContaReceberPrincipal < 0){
				Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir conta a receber!");
			}
			//Parametro para a categoria de custï¿½dia 
			int cdCategoriaCustodia = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_CUSTODIA", 0, cdEmpresa);
			if(cdCategoriaCustodia <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Parametro de categoria economica de custï¿½dia nï¿½o cadastrado!");
			}
			//Faz o cadastro da categoria para a conta do recebimento imediato
			if(ContaReceberCategoriaDAO.insert(new ContaReceberCategoria(cdContaReceberPrincipal, cdCategoriaCustodia, vlRecebidoTotal, 0), connect) <=0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir categoria de custodia da conta a receber principal!");
			}
			
			//Parametro para desconto do cheque a vista de custodia nï¿½o cadastrado
			int cdCategoriaDescontoChequeAVista = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_DESCONTO_CUSTODIA", 0, cdEmpresa);
			if(cdCategoriaDescontoChequeAVista <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Parametro de categoria economica de custï¿½dia nï¿½o cadastrado!");
			}
			//Cadastra a categoria de desconto do recebimento imediato
			if(ContaReceberCategoriaDAO.insert(new ContaReceberCategoria(cdContaReceberPrincipal, cdCategoriaDescontoChequeAVista, vlAbatimento, 0), connect) <=0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir categoria de desconto da conta a receber principal!");
			}
			
			
			/*
			 *  Cria o movimento de conta
			 */
			int stMovimento = MovimentoContaServices.ST_COMPENSADO;
			MovimentoConta movimento = new MovimentoConta(0 /*cdMovimentoConta*/, cdConta, 0 /*cdContaOrigem*/, 0 /*cdMovimentoOrigem*/,
															cdUsuario, 0 /*cdCheque*/, 0 /*cdViagem*/,
															Util.getDataAtual(), vlRecebidoTotal,  null /*nrDocumento*/,
															MovimentoContaServices.CREDITO, 0 /*tpOrigem*/,
															stMovimento,
															dsHistorico, Util.getDataAtual(), null /*idExtrato*/,
															cdFormaPagamentoCompensacao, 0 /*cdFechamento*/,0/*cdTurno*/);
			/*
			 *  Chama o mï¿½todo que faz o lanï¿½amento da conta
			 */
			Result resultadoMovimento = MovimentoContaServices.insert(movimento, movimentoContaReceber, movimentoCategoria, new ArrayList<MovimentoContaTituloCredito>(), 0, connect);
			int cdMovimentoConta = resultadoMovimento.getCode();
			if(cdMovimentoConta<=0)	{
				if(isConnectionNull)
					Conexao.rollback(connect);
				com.tivic.manager.util.Util.registerLog(new Exception("setBaixaResumida: nï¿½o foi possï¿½vel gravar o movimento!"));
				resultadoMovimento.setCode(ERR_MOVIMENTO);
				resultadoMovimento.setMessage("setBaixaResumida: nï¿½o foi possï¿½vel gravar o movimento: " + resultadoMovimento.getMessage());
				return resultadoMovimento;
			}
			//Faz uma outra iteraï¿½ï¿½o dos cheques a serem trocados para que sejam dados como recebidos 
			//e feito cadastro no registro de conta factoring, ligando esses cheques ao cheque de recebimento imediato
			for(int i = 0; i < listaContaReceber.size(); i++){
				int cdContaReceber 	  = listaContaReceber.get(i)==null || listaContaReceber.get(i)==null ? 0 : (Integer) listaContaReceber.get(i);
				ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber, connect);
				Double vlAbatimentoFinal = contaReceber.getVlConta() * (prTaxa / 100);
				contaReceber.setStConta(ContaReceberServices.ST_RECEBIDA);
				contaReceber.setDtRecebimento(Util.getDataAtual());
				TituloCreditoServices.setSituacaoTitulosOfConta(contaReceber.getCdContaReceber(), ContaReceberServices.ST_RECEBIDA, Util.getDataAtual(), connect);
				if(ContaReceberDAO.update(contaReceber, connect) <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar conta a receber!");
				}
				
				int qtDias = Util.getQuantidadeDias(dtRecebimento, contaReceber.getDtVencimento())+1;
				ContaFactoring contaFac = new ContaFactoring(0, cdContaReceber, qtDias, prTaxa, vlAbatimentoFinal.floatValue(), cdContaReceberPrincipal);
				if(ContaFactoringDAO.insert(contaFac, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir conta factoring!");
				}
			}
			
			if (isConnectionNull)
				connect.commit();
						
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar lanï¿½ar pagamento resumido!", e);
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	/**
	 * Rotina que busca o prï¿½ximo nï¿½mero do documento de custï¿½dia, calculado automaticamente
	 * @param cdEmpresa
	 * @return
	 */
	public static String getProximoNrDocumentoCustodia(int cdEmpresa) {
		return getProximoNrDocumentoCustodia(cdEmpresa, null);
	}

	public static String getProximoNrDocumentoCustodia(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrDocumento = 0;

			if ((nrDocumento = NumeracaoDocumentoServices.getProximoNumero("CONTA_RECEBER", nrAno, cdEmpresa, connection)) <= 0)
				return null;

			return new DecimalFormat("000000").format(nrDocumento) + "/" + nrAno;
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Metodo que, caso o parametro adm/Gerar baixa automï¿½tica de cheques esteja ativo, iirï¿½o 
	 * buscar as contas abertas dos clientes, e para aqueles que tï¿½m registros de perfil de cobranï¿½a,
	 * iirï¿½o analisar os nï¿½veis de cobranï¿½a e os dias de vencimento passados, mandando emails baseado nos nï¿½veis
	 * para cobrar esses clientes dessas contas
	 * @author Gabriel
	 * @since 25/08/2014
	 */
	public static void setBaixaAutomatica(){
		boolean isConnectionNull = true;
		Connection connect = null;
		try {
			System.out.println("inicio da baixa automatica...");
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			GregorianCalendar dtFinal = Util.getDataAtual();
			dtFinal.set(Calendar.HOUR_OF_DAY, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			ResultSetMap rsmEmpresas = EmpresaServices.getAll();
			
			while(rsmEmpresas.next()){
				ResultSetMap rsmFalhas = new ResultSetMap();
				Empresa empresa = EmpresaDAO.get(rsmEmpresas.getInt("cd_empresa"), connect);
				int cdEmpresa = rsmEmpresas.getInt("cd_empresa");
				int lgBaixaAutomaticaCheques = ParametroServices.getValorOfParametroAsInteger("LG_BAIXA_AUTOMATICA_CHEQUES", 0, cdEmpresa, connect);
				if(lgBaixaAutomaticaCheques==1){
					Result resultado = findCustodia(cdEmpresa, 0, null, dtFinal, 0, connect);
					
					ResultSetMap rsmCheques = (ResultSetMap) resultado.getObjects().get("rsm");
					while(rsmCheques.next()){
						System.out.println("Conta: " + rsmCheques.getInt("cd_conta_receber") + ", Situacao: " + rsmCheques.getInt("st_conta"));
						int cdConta     = ParametroServices.getValorOfParametroAsInteger("CD_CONTA", 0, cdEmpresa, connect);
						if(cdConta > 0){
							int cdUsuario   = ParametroServices.getValorOfParametroAsInteger("CD_USUARIO_PADRAO_BAIXA_AUTOMATICA", 0, cdEmpresa, connect);
							int cdFormaPagamento =  ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_DINHEIRO", 0, 0, connect);
							GregorianCalendar dtRecebimento = rsmCheques.getGregorianCalendar("dt_vencimento");
							while(dtRecebimento.get(Calendar.DAY_OF_WEEK)==1 || dtRecebimento.get(Calendar.DAY_OF_WEEK)==7 || com.tivic.manager.grl.FeriadoServices.isFeriado(dtRecebimento, connect)){
								dtRecebimento.add(Calendar.DAY_OF_MONTH, 1);
							}
							Result resultadoBaixa = setBaixaResumida(rsmCheques.getInt("cd_conta_receber"), cdConta, 0.0, 0, 0.0, 0, 0.0, 0, (rsmCheques.getDouble("vl_conta") - rsmCheques.getDouble("vl_abatimento") + rsmCheques.getDouble("vl_acrescimo") - rsmCheques.getDouble("vl_recebido")), cdFormaPagamento, null, cdUsuario, dtRecebimento, dtRecebimento, null);
							if(resultadoBaixa.getCode() < 0){
								Connection connectAux = Conexao.conectar();
								connectAux.setAutoCommit(false);
								int result = ContaReceberCategoriaServices.deleteAll(rsmCheques.getInt("cd_conta_receber"), connectAux);
								if(result > 0){
									if(rsmCheques.getDouble("vl_abatimento") > 0){
										int cdCategoriaDescontoConcedido = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_DESCONTO_CONCEDIDO", 0, cdEmpresa);
										if(cdCategoriaDescontoConcedido > 0){
											ContaReceberCategoriaDAO.insert(new ContaReceberCategoria(rsmCheques.getInt("cd_conta_receber"), cdCategoriaDescontoConcedido, rsmCheques.getDouble("vl_abatimento"), 0), connectAux);
										}
										
									}
									else if(rsmCheques.getDouble("vl_abatimento") < 0){
										Double vlAbatimento = rsmCheques.getDouble("vl_abatimento") * -1;
										int cdCategoriaDescontoConcedido = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_DESCONTO_CONCEDIDO", 0, cdEmpresa);
										if(cdCategoriaDescontoConcedido > 0){
											ContaReceberCategoriaDAO.insert(new ContaReceberCategoria(rsmCheques.getInt("cd_conta_receber"), cdCategoriaDescontoConcedido, vlAbatimento, 0), connectAux);
										}
										ContaReceber contaRec = ContaReceberDAO.get(rsmCheques.getInt("cd_conta_receber"), connectAux);
										contaRec.setVlAbatimento(vlAbatimento);
										ContaReceberDAO.update(contaRec, connectAux);
									}
									int cdCategoriaReceitaPadrao     = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_RECEITAS_DEFAULT", 0, cdEmpresa);
									if(cdCategoriaReceitaPadrao > 0){
										ContaReceberCategoriaDAO.insert(new ContaReceberCategoria(rsmCheques.getInt("cd_conta_receber"), cdCategoriaReceitaPadrao, (rsmCheques.getDouble("vl_conta") + rsmCheques.getDouble("vl_acrescimo")), 0), connectAux);
									}
								}
								resultadoBaixa = setBaixaResumida(rsmCheques.getInt("cd_conta_receber"), cdConta, 0.0, 0, 0.0, 0, 0.0, 0, (rsmCheques.getDouble("vl_conta") - rsmCheques.getDouble("vl_abatimento") + rsmCheques.getDouble("vl_acrescimo") - rsmCheques.getDouble("vl_recebido")), cdFormaPagamento, null, cdUsuario, dtRecebimento, dtRecebimento, null, connectAux);
								if(resultadoBaixa.getCode() < 0){
									HashMap<String, Object> register = new HashMap<String, Object>();
									register.put("DS_ERRO", resultadoBaixa.getMessage());
									register.put("NR_CHEQUE", rsmCheques.getString("nr_documento"));
									rsmFalhas.addRegister(register);
									Conexao.rollback(connectAux);
								}
								else{
									System.out.println("cheque baixado automaticamente: " + rsmCheques.getInt("cd_conta_receber"));
									connectAux.commit();
								}
								Conexao.desconectar(connectAux);	
							}
							else
								System.out.println("cheque baixado automaticamente: " + rsmCheques.getInt("cd_conta_receber"));
						}
					}
//					System.out.println(rsmCheques.size() + " cheque(s) baixado(s) automaticamente da empresa " + empresa.getNmRazaoSocial());
//					System.out.println(rsmFalhas.size()  + " cheque(s) baixado(s) falhou(ram) ");
					
				}
				String email = empresa.getNmEmail();
				HashMap<String, Object> params = new HashMap<String, Object>();
				if(email == null || email.equals("")){
					System.out.println("A empresa nï¿½o possui um email cadastrado para que se envie o email!");
				}
				else if(!Util.isEmail(email)){
					System.out.println("A empresa nï¿½o possui um email vï¿½lido!");
				}
				else if(rsmFalhas.size() > 0){
					JRBeanCollectionDataSource jrRS = new JRBeanCollectionDataSource(rsmFalhas.getLines());
//					String path = ParametroServices.getValorOfParametroAsString("NM_PATH_RELATORIO_FALHAS_BAIXA_AUTOMATICA", "C://TIVIC//manager//web//adm");
//					byte[] bytes = JasperRunManager.runReportToPdf(path+"//relatorio_falhas_baixa_automatica.jasper", params, jrRS);
					byte[] bytes = JasperRunManager.runReportToPdf(ReportServices.getJasperReport("relatorio_falhas_baixa_automatica"), params, jrRS);
					String assunto = "Sistema Manager - Baixa automï¿½tica de cheques - Falha de " + rsmFalhas.size() + " cheque(s)"; 
					String texto = "";//Assinatura da empresa ao desenvolvedor
					HashMap<String, Object> arquivo = new HashMap<String, Object>();
					arquivo.put("BLB_ARQUIVO", bytes);
					GregorianCalendar dataHoje = Util.getDataAtual();
					arquivo.put("NM_ARQUIVO", "Relatï¿½rio de Falhas na Baixa automï¿½tica do dia "+Util.formatDate(dataHoje, "dd/MM/yyyy")+".pdf");
					ArrayList<HashMap<String, Object>> anexos = new ArrayList<HashMap<String, Object>>();
					anexos.add(arquivo);
					boolean erro = false;
					if(ParametroServices.getValorOfParametroAsString("NM_SERVIDOR_SMTP", "").equals("")){
						System.out.println("Nome do Servidor SMTP nï¿½o configurado nos parametros!");
						erro = true;
					}
					if(ParametroServices.getValorOfParametroAsString("NR_PORTA_SMTP", "").equals("")){
						System.out.println("Nï¿½mero da porta do Servidor SMTP nï¿½o configurado nos parametros!");
						erro = true;
					}
					if(ParametroServices.getValorOfParametroAsString("NM_LOGIN_SERVIDOR_SMTP", "").equals("")){
						System.out.println("Login do Servidor SMTP nï¿½o configurado nos parametros!");
						erro = true;
					}
					if(ParametroServices.getValorOfParametroAsString("NM_SENHA_SERVIDOR_SMTP", "").equals("")){
						System.out.println("Senha do Servidor SMTP nï¿½o configurado nos parametros!");
						erro = true;
					}
					if(ParametroServices.getValorOfParametroAsString("NM_EMAIL_REMETENTE_SMTP", "").equals("")){
						System.out.println("Remetente SMTP nï¿½o configurado nos parametros!");
						erro = true;
					}
					if(!erro){
						SMTPClient cliente = new SMTPClient(ParametroServices.getValorOfParametroAsString("NM_SERVIDOR_SMTP", ""), 
								ParametroServices.getValorOfParametroAsInteger("NR_PORTA_SMTP", 0), 
								ParametroServices.getValorOfParametroAsString("NM_LOGIN_SERVIDOR_SMTP", ""), 
								ParametroServices.getValorOfParametroAsString("NM_SENHA_SERVIDOR_SMTP", ""), 
								ParametroServices.getValorOfParametroAsInteger("LG_AUTENTICACAO_SMTP", 0)==1, 
								ParametroServices.getValorOfParametroAsInteger("LG_SSL_SMTP", 0)==1, ParametroServices.getValorOfParametroAsString("NM_TRANSPORT_SMTP", "smtp"));
						if(ParametroServices.getValorOfParametroAsInteger("LG_DEBUG_SMTP", 0) == 1){
							cliente.setDebug(true);	
						}
						if(cliente.connect()) {
							@SuppressWarnings("unchecked")
							HashMap<String, Object>[] attachments = new HashMap[anexos.size()];
							
							for (int i = 0; i < anexos.size(); i++) {
								HashMap<String, Object> register = (HashMap<String, Object>)anexos.get(i); 
								register.put("BYTES", register.get("BLB_ARQUIVO"));
								register.put("NAME", register.get("NM_ARQUIVO"));
								attachments[i] = register;
							}
							String[] to = {email};
							cliente.send(ParametroServices.getValorOfParametroAsString("NM_EMAIL_REMETENTE_SMTP", ""), new String[] {ParametroServices.getValorOfParametroAsString("NM_EMAIL_REMETENTE_SMTP", "")}, to, null, null, 
									assunto, texto, null, null, attachments);
						}
						else{
							System.out.println("Erro de conexï¿½o!");
						}
						
						cliente.disconnect();
					}
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			System.out.println("fim da baixa automatica...");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
			
		}
	}
	/**
	 * @author ï¿½lvaro
	 * @param cdContaReceber
	 * @param connect
	 * @return 
	 */
	public static ResultSetMap getDemonstrativoEntradasPorPeriodo(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal,Integer nivelDetalhamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			/**
			 * SQL retorna o total a receber por formas de pagamento ( Visa Crï¿½dito, Hipercard, etc. )
			 */
			String sqlRecebiveisEntradas = "SELECT CAST(COUNT( A.cd_conta_receber ) as INTEGER) as qt_itens, SUM( A.vl_conta ) as vl_total,"+
										" B.nm_forma_pagamento, "+
										"CASE B.tp_forma_pagamento  "+
										"	WHEN 0 THEN 'Espï¿½cie' "+
										"	WHEN 1 THEN 'TEF' "+
										"	WHEN 2 THEN 'Tï¿½tulo de Crï¿½dito' "+
										"END as tp_forma_pagamento "+
										"FROM adm_conta_receber A  "+
										"JOIN adm_forma_pagamento B ON ( A.cd_forma_pagamento = B.cd_forma_pagamento ) "+ 
										"WHERE A.cd_empresa = ?  "+
										"AND ( A.dt_emissao >= ? AND A.dt_emissao <= ? ) "+ 
										"AND A.st_conta =   " + ContaReceberServices.ST_EM_ABERTO +
										"AND B.tp_forma_pagamento <> " + FormaPagamentoServices.MOEDA_CORRENTE +
										"GROUP BY B.cd_forma_pagamento, B.nm_forma_pagamento "+
										"ORDER BY B.tp_forma_pagamento, B.nm_forma_pagamento";
			/**
			 * SQL retorna o total a receber por tipo de formas de pagamento( TEF, Tï¿½tulo de Crï¿½dito, Espï¿½cie )
			 * baseado no resultado da query sqlRecebiveisEntradas
			 */
			String sqlTotalTpDoc = "SELECT SUM( A.qt_itens ) as qt_itens, "+
												" SUM( A.vl_total ) as vl_total, A.tp_forma_pagamento FROM  ( "+
													sqlRecebiveisEntradas +
												" ) as A GROUP BY A.tp_forma_pagamento ORDER BY A.tp_forma_pagamento";
			PreparedStatement pstmt = connect.prepareStatement(sqlRecebiveisEntradas);
			pstmt.setInt(1, cdEmpresa);
			pstmt.setTimestamp(2, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(3, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsmRecebiveisEntradas = new ResultSetMap(pstmt.executeQuery());
			
			pstmt = connect.prepareStatement(sqlTotalTpDoc);
			pstmt.setInt(1, cdEmpresa);
			pstmt.setTimestamp(2, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(3, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsmTotalTpFormasPagamento = new ResultSetMap(pstmt.executeQuery());
			int nrCategoria;
			while(  rsmTotalTpFormasPagamento.next() ){
				int i=1;
				while( rsmRecebiveisEntradas.next() ){
					if( rsmTotalTpFormasPagamento.getString("TP_FORMA_PAGAMENTO").equals( rsmRecebiveisEntradas.getString("TP_FORMA_PAGAMENTO") ) ){
						if(i==1){
							HashMap<String, Object> newTpDoc = new HashMap<String, Object>();
							newTpDoc.put("PORCENTAGEM", "100");
							newTpDoc.put("NR_NIVEL", 4);
							newTpDoc.put("TP_FORMA_PAGAMENTO", rsmRecebiveisEntradas.getString("TP_FORMA_PAGAMENTO")  );
							newTpDoc.put("QT_ITENS", rsmTotalTpFormasPagamento.getInt("QT_ITENS") );
							newTpDoc.put("VL_TOTAL", rsmTotalTpFormasPagamento.getDouble("VL_TOTAL") );
							newTpDoc.put("NR_CATEGORIA",  "4.1.1." + String.format("%03d", rsmTotalTpFormasPagamento.getPointer()+1));
							rsmRecebiveisEntradas.getLines().add( rsmRecebiveisEntradas.getPointer(), newTpDoc);
							rsmRecebiveisEntradas.goTo( rsmRecebiveisEntradas.getPointer()+1 );
						}
						Double vlTotal = rsmTotalTpFormasPagamento.getDouble("VL_TOTAL");
						Double vlItem = rsmRecebiveisEntradas.getDouble("VL_TOTAL");
						Double porcentagem = (vlItem*100/vlTotal);
						rsmRecebiveisEntradas.setValueToField("NR_NIVEL", 5);
						rsmRecebiveisEntradas.setValueToField("PORCENTAGEM", porcentagem.toString());
						nrCategoria = rsmTotalTpFormasPagamento.getPosition()+1;
						rsmRecebiveisEntradas.setValueToField("NR_CATEGORIA", "4.1.1." + (String.format("%03d",  nrCategoria )+"."+ String.format("%04d", i )));
						i++;
					}
				}
				rsmRecebiveisEntradas.beforeFirst();
			}
			
			if( nivelDetalhamento != null  ){
				while( rsmRecebiveisEntradas.next() ){
					if( rsmRecebiveisEntradas.getInt("NR_NIVEL") > nivelDetalhamento ){
						rsmRecebiveisEntradas.deleteRow();
						rsmRecebiveisEntradas.previous();
					}
				}
			}
			if(rsmRecebiveisEntradas.next()){
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NR_CATEGORIA", "4");
				register.put("TP_FORMA_PAGAMENTO", "RECEBï¿½VEIS - ENTRADA");
				register.put("NR_NIVEL", 1);
				rsmRecebiveisEntradas.getLines().add( 0, register);
				
				register = new HashMap<String, Object>();
				register.put("NR_CATEGORIA", "4.1");
				register.put("TP_FORMA_PAGAMENTO", "DOCUMENTOS A RECEBER NO PERï¿½ODO");
				register.put("NR_NIVEL", 2);
				rsmRecebiveisEntradas.getLines().add( 1, register);
				
				register = new HashMap<String, Object>();
				register.put("NR_CATEGORIA", "4.1.1");
				register.put("TP_FORMA_PAGAMENTO", "RECEBï¿½VEIS Nï¿½O LIQUIDADOS");
				register.put("NR_NIVEL", 3);
				rsmRecebiveisEntradas.getLines().add( 2, register);
			}
			rsmRecebiveisEntradas.beforeFirst();
			while(rsmRecebiveisEntradas.next()){
				
				
			}
			rsmRecebiveisEntradas.beforeFirst();
			return rsmRecebiveisEntradas;
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
	 * Retorna RECEBï¿½VEIS que saï¿½ram da lista ï¿½ receber
	 * 
	 * @param cdEmpresa
	 * @param dtInicial
	 * @param dtFinal
	 * @param nivelDetalhamento
	 * @param connect
	 * @return
	 * @author Luiz Romario Filho
	 */
	public static ResultSetMap getDemonstrativoSaidasPorPeriodo(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal,Integer nivelDetalhamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			/**
			 * SQL retorna o total a receber por formas de pagamento ( Visa Crï¿½dito, Hipercard, etc. )
			 */
			String sqlRecebiveisSaida = "SELECT CAST(COUNT( A.cd_conta_receber ) as INTEGER) as qt_itens, SUM( A.vl_conta ) as vl_total,"+
										" B.nm_forma_pagamento, "+
										"CASE B.tp_forma_pagamento  "+
										"	WHEN 0 THEN 'Espï¿½cie' "+
										"	WHEN 1 THEN 'TEF' "+
										"	WHEN 2 THEN 'Tï¿½tulo de Crï¿½dito' "+
										"END as tp_forma_pagamento "+
										"FROM adm_conta_receber A  "+
										"JOIN adm_forma_pagamento B ON ( A.cd_forma_pagamento = B.cd_forma_pagamento ) "+ 
										"WHERE A.cd_empresa = ?  "+
										"AND ( A.dt_recebimento >= ? AND A.dt_recebimento <= ? ) "+ 
										"AND A.st_conta =  " + ContaReceberServices.ST_RECEBIDA +
										"AND B.tp_forma_pagamento <> " + FormaPagamentoServices.MOEDA_CORRENTE +
										"GROUP BY B.cd_forma_pagamento, B.nm_forma_pagamento "+
										"ORDER BY B.tp_forma_pagamento, B.nm_forma_pagamento";
			/**
			 * SQL retorna o total a receber por tipo de formas de pagamento( TEF, Tï¿½tulo de Crï¿½dito, Espï¿½cie )
			 * baseado no resultado da query sqlRecebiveisSaida
			 */
			String sqlTotalTpDoc = "SELECT SUM( A.qt_itens ) as qt_itens, "+
												" SUM( A.vl_total ) as vl_total, A.tp_forma_pagamento FROM  ( "+
													sqlRecebiveisSaida +
												" ) as A GROUP BY A.tp_forma_pagamento ORDER BY A.tp_forma_pagamento";
			PreparedStatement pstmt = connect.prepareStatement(sqlRecebiveisSaida);
			pstmt.setInt(1, cdEmpresa);
			pstmt.setTimestamp(2, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(3, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsmRecebiveisSaidas = new ResultSetMap(pstmt.executeQuery());
			
			pstmt = connect.prepareStatement(sqlTotalTpDoc);
			pstmt.setInt(1, cdEmpresa);
			pstmt.setTimestamp(2, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(3, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsmTotalTpFormasPagamento = new ResultSetMap(pstmt.executeQuery());
			
			while(  rsmTotalTpFormasPagamento.next() ){
				int i=1;
				while( rsmRecebiveisSaidas.next() ){
					if( rsmTotalTpFormasPagamento.getString("TP_FORMA_PAGAMENTO").equals( rsmRecebiveisSaidas.getString("TP_FORMA_PAGAMENTO") ) ){
						if(i==1){
							HashMap<String, Object> newTpDoc = new HashMap<String, Object>();
							newTpDoc.put("PORCENTAGEM", "100");
							newTpDoc.put("NR_NIVEL", 4);
							newTpDoc.put("TP_FORMA_PAGAMENTO", rsmRecebiveisSaidas.getString("TP_FORMA_PAGAMENTO")  );
							newTpDoc.put("QT_ITENS", rsmTotalTpFormasPagamento.getInt("QT_ITENS") );
							newTpDoc.put("VL_TOTAL", rsmTotalTpFormasPagamento.getDouble("VL_TOTAL") );
							newTpDoc.put("NR_CATEGORIA", "5.1.1." + String.format("%03d", rsmTotalTpFormasPagamento.getPointer()+1));
							rsmRecebiveisSaidas.getLines().add( rsmRecebiveisSaidas.getPointer(), newTpDoc);
							rsmRecebiveisSaidas.goTo( rsmRecebiveisSaidas.getPointer()+1 );
						}
						Double vlTotal = rsmTotalTpFormasPagamento.getDouble("VL_TOTAL");
						Double vlItem = rsmRecebiveisSaidas.getDouble("VL_TOTAL");
						Double porcentagem = (vlItem*100/vlTotal);
						rsmRecebiveisSaidas.setValueToField("NR_NIVEL", 5);
						rsmRecebiveisSaidas.setValueToField("PORCENTAGEM", porcentagem.toString());
						int nrCategoria = rsmTotalTpFormasPagamento.getPosition()+1;
						rsmRecebiveisSaidas.setValueToField("NR_CATEGORIA",  "5.1.1." + String.format("%03d", nrCategoria) +"."+String.format("%04d", i) );
						i++;
					}
				}
				rsmRecebiveisSaidas.beforeFirst();
			}
			
			if( nivelDetalhamento != null  ){
				while( rsmRecebiveisSaidas.next() ){
					if( rsmRecebiveisSaidas.getInt("NR_NIVEL") > nivelDetalhamento ){
						rsmRecebiveisSaidas.deleteRow();
						rsmRecebiveisSaidas.previous();
					}
				}
			}
			
			if(rsmRecebiveisSaidas.next()){
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NR_CATEGORIA", "5");
				register.put("TP_FORMA_PAGAMENTO", "RECEBï¿½VEIS - SAï¿½DA");
				register.put("NR_NIVEL", 1);
				rsmRecebiveisSaidas.getLines().add( 0, register);
				
				register = new HashMap<String, Object>();
				register.put("NR_CATEGORIA", "5.1");
				register.put("TP_FORMA_PAGAMENTO", "RECEBIMENTOS DO PERï¿½ODO");
				register.put("NR_NIVEL",2);
				rsmRecebiveisSaidas.getLines().add( 1, register);
				
				register = new HashMap<String, Object>();
				register.put("NR_CATEGORIA", "5.1.1");
				register.put("TP_FORMA_PAGAMENTO", "RECEBï¿½VEIS LIQUIDADOS");
				register.put("NR_NIVEL", 3);
				rsmRecebiveisSaidas.getLines().add( 2, register);
			}
			return rsmRecebiveisSaidas;
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
	 * Cï¿½lcula os RECEBï¿½VEIS em aberto
	 * 
	 * @param cdEmpresa
	 * @param dtInicial
	 * @param dtFinal
	 * @param nivelDetalhamento
	 * @param connect
	 * @return
	 * @author Luiz Romario Filho
	 */
	public static ResultSetMap getDreContasReceber(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			/**
			 * SQL retorna o total a receber por formas de pagamento ( Visa Crï¿½dito, Hipercard, etc. )
			 */
			String sqlRecebiveisEntradas = "SELECT CAST(COUNT( A.cd_conta_receber ) as INTEGER) as qt_itens, SUM( A.vl_conta ) as vl_total,"+
										" B.nm_forma_pagamento, "+
										"CASE B.tp_forma_pagamento  "+
										"	WHEN 0 THEN 'Espï¿½cie' "+
										"	WHEN 1 THEN 'TEF' "+
										"	WHEN 2 THEN 'Tï¿½tulo de Crï¿½dito' "+
										"END as tp_forma_pagamento "+
										"FROM adm_conta_receber A  "+
										"JOIN adm_forma_pagamento B ON ( A.cd_forma_pagamento = B.cd_forma_pagamento ) "+ 
										"WHERE A.cd_empresa = ?  "+
										"AND A.st_conta =   " + ContaReceberServices.ST_EM_ABERTO +
										"AND B.tp_forma_pagamento <> " + FormaPagamentoServices.MOEDA_CORRENTE +
										"GROUP BY B.cd_forma_pagamento, B.nm_forma_pagamento "+
										"ORDER BY B.tp_forma_pagamento, B.nm_forma_pagamento";
			/**
			 * SQL retorna o total a receber por tipo de formas de pagamento( TEF, Tï¿½tulo de Crï¿½dito, Espï¿½cie )
			 * baseado no resultado da query sqlRecebiveisEntradas
			 */
			String sqlTotalTpDoc = "SELECT SUM( A.qt_itens ) as qt_itens, "+
												" SUM( A.vl_total ) as vl_total, A.tp_forma_pagamento FROM  ( "+
													sqlRecebiveisEntradas +
												" ) as A GROUP BY A.tp_forma_pagamento ORDER BY A.tp_forma_pagamento";
			PreparedStatement pstmt = connect.prepareStatement(sqlRecebiveisEntradas);
			pstmt.setInt(1, cdEmpresa);
			ResultSetMap rsmContasReceber = new ResultSetMap(pstmt.executeQuery());
			
			pstmt = connect.prepareStatement(sqlTotalTpDoc);
			pstmt.setInt(1, cdEmpresa);
				ResultSetMap rsmTotalTpFormasPagamento = new ResultSetMap(pstmt.executeQuery());
			int nrCategoria;
			while(  rsmTotalTpFormasPagamento.next() ){
				int i=1;
				while( rsmContasReceber.next() ){
					if( rsmTotalTpFormasPagamento.getString("TP_FORMA_PAGAMENTO").equals( rsmContasReceber.getString("TP_FORMA_PAGAMENTO") ) ){
						if(i==1){
							HashMap<String, Object> newTpDoc = new HashMap<String, Object>();
							newTpDoc.put("PORCENTAGEM", "100");
							newTpDoc.put("NR_NIVEL", 4);
							newTpDoc.put("TP_FORMA_PAGAMENTO", rsmContasReceber.getString("TP_FORMA_PAGAMENTO")  );
							newTpDoc.put("QT_ITENS", rsmTotalTpFormasPagamento.getInt("QT_ITENS") );
							newTpDoc.put("VL_TOTAL", rsmTotalTpFormasPagamento.getDouble("VL_TOTAL") );
							newTpDoc.put("NR_CATEGORIA",  "8.1.1." + String.format("%03d", rsmTotalTpFormasPagamento.getPointer()+1));
							rsmContasReceber.getLines().add( rsmContasReceber.getPointer(), newTpDoc);
							rsmContasReceber.goTo( rsmContasReceber.getPointer()+1 );
						}
						Double vlTotal = rsmTotalTpFormasPagamento.getDouble("VL_TOTAL");
						Double vlItem = rsmContasReceber.getDouble("VL_TOTAL");
						Double porcentagem = (vlItem*100/vlTotal);
						rsmContasReceber.setValueToField("NR_NIVEL", 5);
						rsmContasReceber.setValueToField("PORCENTAGEM", porcentagem.toString());
						nrCategoria = rsmTotalTpFormasPagamento.getPosition()+1;
						rsmContasReceber.setValueToField("NR_CATEGORIA", "8.1.1." + (String.format("%03d",  nrCategoria )+"."+ String.format("%04d", i )));
						i++;
					}
				}
				rsmContasReceber.beforeFirst();
			}
			
			if(rsmContasReceber.next()){
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NR_CATEGORIA", "8.1");
				register.put("TP_FORMA_PAGAMENTO", "CONTAS A RECEBER");
				register.put("NR_NIVEL", 2);
				rsmContasReceber.getLines().add( 0, register);
				
				register = new HashMap<String, Object>();
				register.put("NR_CATEGORIA", "8.1.1");
				register.put("TP_FORMA_PAGAMENTO", "RECEBï¿½VEIS ACUMULADO");
				register.put("NR_NIVEL", 3);
				rsmContasReceber.getLines().add( 1, register);
			}
			rsmContasReceber.beforeFirst();
			return rsmContasReceber;
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
	 * verificaTituloVencido      verifica se o cliente tem alguma conta vencida (cheque, promissï¿½ria ou boleto) atï¿½ a data atual e avisa antes da venda no PDV.
	 * 
	 * @param  cdEmpresa       // cï¿½digo da empresa 
	 * @param  cdCliente       // cï¿½digo do cliente
	 * @return 0               // Cliente com faturas em vencidas
	 * @return 1               // Cliente sem faturas vencidas
	 * 
	 * @author Joao Marlon Souto Ferraz
	 * */
	public static ResultSetMap verificaTituloVencido(int cdEmpresa, int cdCliente, int stConta){
		return verificaTituloVencido(cdEmpresa, cdCliente, stConta, null);
	}
	public static ResultSetMap verificaTituloVencido(int cdEmpresa, int cdCliente, int stConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;	
			PreparedStatement pstmtContasReceber = connect.prepareStatement("SELECT * FROM adm_conta_receber A " +																            
																            "WHERE cd_empresa     = ? " +
																            "   AND cd_pessoa     = ? " +
																            "   AND dt_vencimento < ? " +
																            "   AND st_conta      = " + ContaReceberServices.ST_EM_ABERTO);
			pstmtContasReceber.setInt(1, cdEmpresa);
			pstmtContasReceber.setInt(2, cdCliente);
			pstmtContasReceber.setTimestamp(3, new Timestamp(Util.getDataAtual().getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmtContasReceber.executeQuery());
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! CobrancaService.verificaTituloVencido: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getReciboRecebimento(int cdConta, int cdMovimentoConta, int cdContaReceber){
		try{
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("TP_RECIBO", "RECEBER");
			
			
			MovimentoContaReceber movContaReceber = MovimentoContaReceberDAO.get( cdConta, cdMovimentoConta, cdContaReceber);
			MovimentoConta movConta = MovimentoContaDAO.get(cdMovimentoConta, cdConta);
			ContaReceber conta = ContaReceberDAO.get(cdContaReceber);
			Empresa empresa = EmpresaDAO.get( conta.getCdEmpresa() );
			Pessoa cliente = PessoaDAO.get( conta.getCdPessoa() );
			PessoaEndereco clienteEndereco = PessoaEnderecoServices.getEnderecoPrincipal(cliente.getCdPessoa());
			String nmSacado = "";
			String nrIdentidade = "";
			String nrDocumento = ( ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_CHEQUE", 0) == movConta.getCdFormaPagamento() )?movConta.getNrDocumento():null;
			String endereco = Util.formatEndereco("", clienteEndereco.getNmLogradouro(),
					clienteEndereco.getNrEndereco(), "", clienteEndereco.getNmBairro(), "", "", "",null);
			if( PessoaFisicaDAO.get( cliente.getCdPessoa() ) != null ){
				PessoaFisica clientePessoa =  PessoaFisicaDAO.get( cliente.getCdPessoa() );
				nmSacado = clientePessoa.getNmPessoa();
				nrIdentidade = "CPF :"+Util.formatCpf( clientePessoa.getNrCpf() );
			}else{
				PessoaJuridica clienteJur =  PessoaJuridicaDAO.get( cliente.getCdPessoa() );
				nmSacado = clienteJur.getNmPessoa();
				nrIdentidade = "CNPJ :"+Util.formatCnpj( clienteJur.getNrCnpj() );
			}
			
			ContaFinanceira contaFin = ContaFinanceiraDAO.get( movConta.getCdConta() );
			ChequeDAO.get( movConta.getCdFormaPagamento() );
			register.put("NM_SACADO", nmSacado);
			register.put("NR_IDENTIDADE", nrIdentidade );
			register.put("ENDERECO", endereco);
			register.put("VL_EXTENSO", Util.capitular( Util.formatExtenso( movContaReceber.getVlRecebido() , true)) );
			register.put("DS_REFERENTE", movConta.getDsHistorico() );
			register.put("NM_FAVORECIDO", empresa.getNmPessoa());
			register.put("NR_RECIBO", conta.getNrDocumento());
			register.put("VL_RECEBIDO",  movContaReceber.getVlRecebido() );
			
			String dsExtenso1 = new DecimalFormat("###,###,##0.00", 
					new DecimalFormatSymbols(new Locale("pt","BR"))
				).format(movContaReceber.getVlRecebido())+" ( ";
			dsExtenso1 += Util.capitular( Util.formatExtenso( movContaReceber.getVlRecebido().floatValue() , true));
			String dsExtenso2 = Util.fill("", 81, '*', 'D')+" ).";
			if( dsExtenso1.length() > 63 ){
				dsExtenso2 = Util.fill( dsExtenso1.substring(59), 81, '*', 'D')+" ).";
				dsExtenso1 = dsExtenso1.substring(0, 59);
			}else{
				dsExtenso1 = Util.fill( dsExtenso1, 63, '*', 'D');
			}
			
			register.put("DS_EXTENSO_1", dsExtenso1.toUpperCase()  );
			register.put("DS_EXTENSO_2", dsExtenso2.toUpperCase()  );
			
			register.put("NR_CONTA", String.valueOf( contaFin.getNrConta() ));
			register.put("NR_CHEQUE", nrDocumento  );
			register.put("DT_MOVIMENTO", Util.formatDate(movConta.getDtMovimento(), "dd/MM/yyyy") );
			
			ResultSetMap rsmRecibo = new ResultSetMap();
			rsmRecibo.addRegister(register);
			Result result = new Result(1);
			result.addObject("rsm", rsmRecibo);
			result.addObject("params", params);
			return result;
		}catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao gerar Recibo.");
		}
	}
	
	public static void correcaoContaReceber() {
		Connection connect = Conexao.conectar();
		try	{
			connect.setAutoCommit(false);
			
			ResultSetMap rsmContaReceber = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_conta_receber").executeQuery());
			
			ResultSetMap rsmMovContaReceber = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_movimento_conta_receber").executeQuery());
			
			ResultSetMap rsmMovConta = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_movimento_conta").executeQuery());
			
			HashMap<ArrayList<Integer>, ArrayList<Double>> registrosCR = new HashMap<ArrayList<Integer>, ArrayList<Double>>();
			HashMap<ArrayList<Integer>, ArrayList<Double>> registrosMCR = new HashMap<ArrayList<Integer>, ArrayList<Double>>();
			HashMap<ArrayList<Integer>, ArrayList<Double>> registrosM = new HashMap<ArrayList<Integer>, ArrayList<Double>>();
			
			while(rsmContaReceber.next()){
				Double vlConta      = Util.roundDouble(rsmContaReceber.getDouble("vl_conta"), 2);
				Double vlAbatimento = Util.roundDouble(rsmContaReceber.getDouble("vl_abatimento"), 2);
				Double vlAcrescimo  = Util.roundDouble(rsmContaReceber.getDouble("vl_acrescimo"), 2);
				Double vlRecebido   = Util.roundDouble(rsmContaReceber.getDouble("vl_recebido"), 2);
				
				ArrayList<Integer> codigos = new ArrayList<Integer>();
				codigos.add(rsmContaReceber.getInt("cd_conta_receber"));
				ArrayList<Double> valores = new ArrayList<Double>();
				valores.add(vlConta);
				valores.add(vlAbatimento);
				valores.add(vlAcrescimo);
				valores.add(vlRecebido);
				registrosCR.put(codigos, valores);
				
			}
			
			while(rsmMovContaReceber.next()){
				Double vlJurosB    = Util.roundDouble(rsmMovContaReceber.getDouble("vl_juros"), 2);
				Double vlMultaB    = Util.roundDouble(rsmMovContaReceber.getDouble("vl_multa"), 2);
				Double vlDescontoB = Util.roundDouble(rsmMovContaReceber.getDouble("vl_desconto"), 2);
				Double vlRecebidoB = Util.roundDouble(rsmMovContaReceber.getDouble("vl_recebido"), 2);
				
				ArrayList<Integer> codigos = new ArrayList<Integer>();
				codigos.add(rsmMovContaReceber.getInt("cd_conta"));
				codigos.add(rsmMovContaReceber.getInt("cd_movimento_conta"));
				codigos.add(rsmMovContaReceber.getInt("cd_conta_receber"));
				ArrayList<Double> valores = new ArrayList<Double>();
				valores.add(vlJurosB);
				valores.add(vlMultaB);
				valores.add(vlDescontoB);
				valores.add(vlRecebidoB);
				registrosMCR.put(codigos, valores);
					
			}
			
			while(rsmMovConta.next()){
				Double vlMovimento = Util.roundDouble(rsmMovConta.getDouble("vl_movimento"), 2);
				
				ArrayList<Integer> codigos = new ArrayList<Integer>();
				codigos.add(rsmMovConta.getInt("cd_movimento_conta"));
				codigos.add(rsmMovConta.getInt("cd_conta"));
				ArrayList<Double> valores = new ArrayList<Double>();
				valores.add(vlMovimento);
				registrosM.put(codigos, valores);
				
			}
			
			
			connect.prepareStatement("UPDATE adm_conta_receber SET vl_conta = 0").executeUpdate();
			connect.prepareStatement("UPDATE adm_conta_receber SET vl_abatimento = 0").executeUpdate();
			connect.prepareStatement("UPDATE adm_conta_receber SET vl_acrescimo = 0").executeUpdate();
			connect.prepareStatement("UPDATE adm_conta_receber SET vl_recebido = 0").executeUpdate();
			
			
			connect.prepareStatement("UPDATE adm_movimento_conta_receber SET vl_recebido = 0").executeUpdate();
			connect.prepareStatement("UPDATE adm_movimento_conta_receber SET vl_juros = 0").executeUpdate();
			connect.prepareStatement("UPDATE adm_movimento_conta_receber SET vl_multa = 0").executeUpdate();
			connect.prepareStatement("UPDATE adm_movimento_conta_receber SET vl_desconto = 0").executeUpdate();
			
			connect.prepareStatement("UPDATE adm_movimento_conta A SET vl_movimento = 0").executeUpdate();
			
			
			
			connect.prepareStatement("ALTER TABLE adm_conta_receber ALTER COLUMN vl_conta TYPE numeric").executeUpdate();
			connect.prepareStatement("ALTER TABLE adm_conta_receber ALTER COLUMN vl_abatimento TYPE numeric").executeUpdate();
			connect.prepareStatement("ALTER TABLE adm_conta_receber ALTER COLUMN vl_acrescimo TYPE numeric").executeUpdate();
			connect.prepareStatement("ALTER TABLE adm_conta_receber ALTER COLUMN vl_recebido TYPE numeric").executeUpdate();
			
			
			connect.prepareStatement("ALTER TABLE adm_movimento_conta_receber ALTER COLUMN vl_recebido TYPE numeric").executeUpdate();
			connect.prepareStatement("ALTER TABLE adm_movimento_conta_receber ALTER COLUMN vl_juros TYPE numeric").executeUpdate();
			connect.prepareStatement("ALTER TABLE adm_movimento_conta_receber ALTER COLUMN vl_multa TYPE numeric").executeUpdate();
			connect.prepareStatement("ALTER TABLE adm_movimento_conta_receber ALTER COLUMN vl_desconto TYPE numeric").executeUpdate();
			
			connect.prepareStatement("ALTER TABLE adm_movimento_conta ALTER COLUMN vl_movimento TYPE numeric").executeUpdate();
			
			
			
			for(ArrayList<Integer> chaves : registrosCR.keySet()){
				ArrayList<Double> valores = registrosCR.get(chaves);
				
				ContaReceber contaReceber = ContaReceberDAO.get(chaves.get(0), connect);
				if(contaReceber != null){
					contaReceber.setVlConta(valores.get(0));
					contaReceber.setVlAbatimento(valores.get(1));
					contaReceber.setVlAcrescimo(valores.get(2));
					contaReceber.setVlRecebido(valores.get(3));
					
					if(ContaReceberDAO.update(contaReceber, connect) < 0){
						Conexao.rollback(connect);
						System.out.println("Erro ao atualizar conta");
						return;
					}
				}
				
			}
			
			
			for(ArrayList<Integer> chaves : registrosMCR.keySet()){
				ArrayList<Double> valores = registrosMCR.get(chaves);
				MovimentoContaReceber movimentoContaReceber = MovimentoContaReceberDAO.get(chaves.get(0), chaves.get(1), chaves.get(2), connect);
				if(movimentoContaReceber != null){
					movimentoContaReceber.setVlDesconto(valores.get(0));
					movimentoContaReceber.setVlJuros(valores.get(1));
					movimentoContaReceber.setVlMulta(valores.get(2));
					movimentoContaReceber.setVlRecebido(valores.get(3));
					
					if(MovimentoContaReceberDAO.update(movimentoContaReceber, connect) < 0){
						Conexao.rollback(connect);
						System.out.println("Erro ao atualizar movimento conta receber");
						return;
					}
				}
			}
			
			for(ArrayList<Integer> chaves : registrosM.keySet()){
				ArrayList<Double> valores = registrosM.get(chaves);
				MovimentoConta movimentoConta = MovimentoContaDAO.get(chaves.get(0), chaves.get(1), connect);
				if(movimentoConta != null){
					movimentoConta.setVlMovimento(valores.get(0));
					if(MovimentoContaDAO.update(movimentoConta, connect) < 0){
						Conexao.rollback(connect);
						System.out.println("Erro ao atualizar movimento de conta");
						return;
					}
				}
			}
			
			connect.commit();
			
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * PROCON - MÃ©todo para pegar todas as contas com situacao em aberto
	 * obs.: Faz integracao com o banco da E&L
	 */
	public static ResultSetMap getDocumentoConta(int stConta, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdPessoa) {
		return getDocumentoConta(stConta, dtInicial, dtFinal, cdPessoa, null);
	}

	public static ResultSetMap getDocumentoConta(int stConta, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			System.out.println("entrou!");
			
			// Verificar as taxas pagas na E&L
			//ResultSetMap verificados = DAMServices.verificarTaxasEL();
			
			System.out.println("aqui!");
			
			pstmt = connect.prepareStatement("SELECT A.cd_documento, A.cd_documento_conta, A.cd_conta_receber, A.dt_pagamento as dt_recebimento," +
											" B.vl_conta, B.st_conta, B.dt_emissao, B.dt_vencimento,"+
											" C.nr_documento, D.nm_pessoa, E.nr_cnpj"+
											" from ptc_documento_conta A" +
											" join adm_conta_receber B ON (A.cd_conta_receber = B.cd_conta_receber)" +
											" join ptc_documento C ON (A.cd_documento = C.cd_documento)" +
											" left outer join grl_pessoa D ON (D.cd_pessoa = B.cd_pessoa)" +
											" left outer join grl_pessoa_juridica E ON (E.cd_pessoa = B.cd_pessoa)" +
											" WHERE A.st_documento_conta = " + DocumentoConta.ST_ATIVO + 
											(stConta!=0?" AND B.st_conta = "+stConta : "") +
											(cdPessoa!=0?" AND B.cd_pessoa = "+cdPessoa : "") +
											" AND B.dt_emissao >= '" + new Timestamp(dtInicial.getTimeInMillis()) + "' " +
											" AND B.dt_emissao <= '" + new Timestamp(dtFinal.getTimeInMillis()) + "' " +
											" ORDER BY " + (stConta==ST_RECEBIDA? "A.dt_pagamento" : "B.dt_emissao"));
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				rsm.setValueToField("NM_ST_CONTA", situacaoContaReceber[rsm.getInt("ST_CONTA")]);
			}
			
			rsm.beforeFirst();
			
			return rsm;
		}
		
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberServices.getDocumentoConta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberServices.getDocumentoConta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * PROCON - MÃ©todo para calcularAcrescimo na conta e cancelar conta no banco E&L
	 * obs.: Faz integracao com o banco da E&L
	 */
	public static Result calcularAcrescimo(int cdDocumentoConta, int cdDocumento){
		return calcularAcrescimo(cdDocumentoConta, cdDocumento, null);
	}

	public static Result calcularAcrescimo(int cdDocumentoConta, int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cdDocumentoConta==0 || cdDocumento==0)
				return new Result(-1, "Erro ao calcular acrÃ©scimo.");

			int retorno = 0;
			ContaReceber contaReceber = null;
			
			DocumentoConta documentoConta = DocumentoContaDAO.get(cdDocumento, cdDocumentoConta, connect);
			
			if(documentoConta.getNrDocumento()==null || documentoConta.getNrDocumento()=="")
				return new Result(-1, "Nenhuma multa gerada anteriormente com essa conta.");
			
			if(documentoConta.getCdContaReceber()!=0) {
				contaReceber = ContaReceberDAO.get(documentoConta.getCdContaReceber(), connect);
				contaReceber.setStConta(ST_NEGOCIADA);
				
				// Calculo do acrescimo
				int meses = DateServices.countMonthsBetween(contaReceber.getDtVencimentoOriginal(), contaReceber.getDtVencimento());
				Double acrescimo = contaReceber.getVlConta() * (meses * 0.01);
				contaReceber.setVlAcrescimo(acrescimo);
				
				// Savar acrescimo
				retorno = ContaReceberDAO.update(contaReceber, connect);
			}
			
			Result result = null;
			if(retorno>0){
				result = DocumentoContaServices.setInativo(cdDocumento, cdDocumentoConta, true, false, connect);
				
				System.out.println("resultFinal = " + result.getCode());
			}
			
			if(result==null || result.getCode()<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao calcular...":"Calculado com sucesso...", "CONTARECEBER", contaReceber);
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
	
	public static Result confirmarPagamentoInscricao(ResultSetMap rsmBoletos) {
		return confirmarPagamentoInscricao(rsmBoletos, null);
	}
	public static Result confirmarPagamentoInscricao(ResultSetMap rsmBoletos, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			String ids 	= "";
			int counter = 1;
			int confirmados = 0;
			
			while(rsmBoletos.next()){				
				if(counter != rsmBoletos.size()){
					ids += "'" + rsmBoletos.getString("NR_REFERENCIA") + "', ";
				} else {
					ids += "'" + rsmBoletos.getString("NR_REFERENCIA") + "'";
				}
				counter++;
			}
			
			criterios.add(new ItemComparator("A.ID_CONTA_RECEBER", ids, ItemComparator.IN, Types.VARCHAR));
            
			ResultSetMap rsmContasReceber = find(criterios, connect);
			
			if(rsmContasReceber.size() != 0){
				while(rsmContasReceber.next()){
					ContaReceber contaReceber = new ContaReceber();
					contaReceber = (ContaReceber) ContaReceberDAO.get(rsmContasReceber.getInt("CD_CONTA_RECEBER"), connect).clone();
					if(contaReceber!= null && contaReceber.getCdConta() > 0){
						contaReceber.setCdContaReceber(rsmContasReceber.getInt("CD_CONTA_RECEBER"));
						contaReceber.setStConta(1);
						retorno = ContaReceberDAO.update(contaReceber, connect);
						if(retorno > 0){
							confirmados++;
						}
					}
				}
			} else {
				return new Result(1, "Nenhum pagamento a confirmar neste arquivo.");
			}
			
			connect.commit();
			return new Result(retorno, (retorno<=0)?"Erro ao confirmar...": confirmados + " pagamentos confirmados com sucesso...");
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
	
	public static ResultSetMap getTotalReceitasMensais( ArrayList<ItemComparator> criterios ) {
		return getTotalReceitasMensais(criterios, null);
	}
	
	public static ResultSetMap getTotalReceitasMensais( ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ResultSetMap rsm = Search.find(
					" SELECT SUM( vl_conta ) as vl_conta, cd_empresa, "+
					" CAST( year AS INTEGER), CAST( month AS INTEGER) "+
					" FROM (  												"+
					"	SELECT  vl_conta, cd_empresa,  						"+
					"	EXTRACT( month FROM dt_emissao) as month, 	"+ 
					"	EXTRACT( year FROM dt_emissao) as year    	"+
					"	FROM adm_conta_receber 								"+
					"	WHERE dt_emissao IS NOT NULL						"+
					"	AND   ST_CONTA NOT IN ( "+ST_CANCELADA+","+ST_PERDA+" )  	"+
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
	
	@Deprecated
	public static Result findGroupCategoriasMensal(ArrayList<sol.dao.ItemComparator> criterios) {
		return findGroupCategoriasMensal(criterios, null);
	}

	@Deprecated
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
			dtInicio.set(Calendar.YEAR, year);
			dtInicio.set(Calendar.HOUR, 0);
			dtInicio.set(Calendar.MINUTE, 0);
			dtInicio.set(Calendar.SECOND, 0);
			criterios.add(new ItemComparator(clTipoPeriodo, Util.formatDateTime(dtInicio, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			
			GregorianCalendar dtFinal = new GregorianCalendar();
			dtFinal.set(Calendar.DAY_OF_MONTH, 31);
			dtFinal.set(Calendar.MONTH, 11);
			dtFinal.set(Calendar.YEAR, year);
			dtFinal.set(Calendar.HOUR, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			criterios.add(new ItemComparator(clTipoPeriodo, Util.formatDateTime(dtFinal, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			
			ResultSetMap rsm = Search.find("SELECT "+limitSkip[0]+" * "+
					           "FROM adm_conta_receber A " +
					           "LEFT OUTER JOIN adm_conta_receber_categoria    H    ON (A.cd_conta_receber = H.cd_conta_receber) "+
					           "LEFT OUTER JOIN adm_categoria_economica        H2   ON (H.cd_categoria_economica = H2.cd_categoria_economica) "+
					           "LEFT OUTER JOIN ctb_centro_custo               H3   ON (H.cd_centro_custo = H3.cd_centro_custo) "+
					           " WHERE H3.NM_CENTRO_CUSTO NOT LIKE 'CONTENCIOSO DE MASSA%' "+ 
					           "   AND H2.nm_categoria_economica NOT LIKE 'Tributos retidos%'",
					           "ORDER BY H3.nm_centro_custo, "+clTipoPeriodo+" "+limitSkip[1],
					           criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			int mes = 0;
			float vlMes = 0;
			float vlTotal = 0;
			float vlCompleto = 0;
			int contMeses = 0;
			int x = 0;
			CentroCusto centroCusto = null;
			
			while(rsm.next()){ 
				
				GregorianCalendar dtPeriodo = rsm.getGregorianCalendar(clTipoPeriodoColumn);
				if(x == 0){ // primeiro mês
					mes = dtPeriodo.get(Calendar.MONTH);
					centroCusto = CentroCustoDAO.get(rsm.getInt("cd_centro_custo"), connect);
				}
				
				if(dtPeriodo.get(Calendar.MONTH) == mes && rsm.getInt("cd_centro_custo") == centroCusto.getCdCentroCusto()){
					vlMes += rsm.getFloat("vl_conta_categoria");
				}
				else{
					vlTotal += vlMes;
					
					if(vlMes > 0){
						contMeses++;
					}
					if(mes < 11 && rsm.getInt("cd_centro_custo") == centroCusto.getCdCentroCusto()){
						register.put("VL_" + Util.limparAcentos(Util.meses[mes].toUpperCase()).substring(0, 3), vlMes);
						mes = dtPeriodo.get(Calendar.MONTH);
					}
					else{
						register.put("VL_" + Util.limparAcentos(Util.meses[mes].toUpperCase()).substring(0, 3), vlMes);
						register.put("VL_TOTAL", vlTotal);
						register.put("VL_MEDIA_ATUAL", (vlTotal / contMeses));
						register.put("VL_MEDIA_ANTERIOR", getGroupCentroCustoMensalMediaAnterior((ArrayList<ItemComparator>)criteriosCopy.clone(), rsm.getInt("cd_centro_custo"), connect));
						register.put("NM_CENTRO_CUSTO_CLIENTE", centroCusto.getNmCentroCusto());
						register.put("NM_CENTRO_CUSTO_SUPERIOR", "Ações Estratégicas");
						rsmFinal.addRegister(register);
						register = new HashMap<String, Object>();
						mes = dtPeriodo.get(Calendar.MONTH);
						vlCompleto += vlTotal;
						centroCusto = CentroCustoDAO.get(rsm.getInt("cd_centro_custo"), connect);
						contMeses = 0;
						vlTotal = 0;
					}
					
					vlMes = rsm.getFloat("vl_conta_categoria");
				}
				//System.out.println("\t\t"+centroCusto.getNmCentroCusto()+": "+vlMes+"\t["+Util.meses[mes]+"]");
				x++;
			}
			rsmFinal.beforeFirst();
			if(mes < 11 && vlMes > 0){
				vlTotal += vlMes;
				contMeses++;
				register.put("VL_" + Util.limparAcentos(Util.meses[mes].toUpperCase()).substring(0, 3), vlMes);
				register.put("VL_TOTAL", vlTotal);
				register.put("VL_MEDIA_ATUAL", (vlTotal / contMeses));
				register.put("VL_MEDIA_ANTERIOR", getGroupCentroCustoMensalMediaAnterior((ArrayList<ItemComparator>)criteriosCopy.clone(), rsm.getInt("cd_centro_custo"), connect));
				register.put("NM_CENTRO_CUSTO_CLIENTE", centroCusto.getNmCentroCusto());
				register.put("NM_CENTRO_CUSTO_SUPERIOR", "Ações Estratégicas");
				rsmFinal.addRegister(register);
				vlCompleto += vlTotal;
			}
			
			rsm = Search.find("SELECT "+limitSkip[0]+" * "+
					           "FROM adm_conta_receber A " +
					           "LEFT OUTER JOIN adm_conta_receber_categoria    H    ON (A.cd_conta_receber = H.cd_conta_receber) "+
					           "LEFT OUTER JOIN adm_categoria_economica        H4   ON (H.cd_categoria_economica = H4.cd_categoria_economica) "+
					           "LEFT OUTER JOIN grl_pessoa                     H2   ON (A.cd_pessoa = H2.cd_pessoa) "+
					           "LEFT OUTER JOIN ctb_centro_custo               H3   ON (H.cd_centro_custo = H3.cd_centro_custo) "+
					           " WHERE H3.NM_CENTRO_CUSTO LIKE 'CONTENCIOSO DE MASSA%' " + 
					           "   AND H4.nm_categoria_economica NOT LIKE 'Tributos retidos%'",
					           "ORDER BY H2.nm_pessoa, H2.cd_pessoa, "+clTipoPeriodo+" "+limitSkip[1],
					           criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			
			register = new HashMap<String, Object>();
			mes = 0;
			vlMes = 0;
			vlTotal = 0;
			contMeses = 0;
			x = 0;
			Pessoa pessoa = null;
			while(rsm.next()){
				GregorianCalendar dtPeriodo = rsm.getGregorianCalendar(clTipoPeriodoColumn);
				if(x == 0){
					mes = dtPeriodo.get(Calendar.MONTH);
					pessoa = PessoaDAO.get(rsm.getInt("cd_pessoa"), connect);
				}
				if(dtPeriodo.get(Calendar.MONTH) == mes && rsm.getInt("cd_pessoa") == pessoa.getCdPessoa()){
					vlMes += rsm.getFloat("vl_conta_categoria");
				}
				else{
					vlTotal += vlMes;
					
					if(vlMes > 0){
						contMeses++;
					}
					if(mes < 11 && rsm.getInt("cd_pessoa") == pessoa.getCdPessoa()){
						register.put("VL_" + Util.limparAcentos(Util.meses[mes].toUpperCase()).substring(0, 3), vlMes);
						mes = dtPeriodo.get(Calendar.MONTH);
					}
					else{
												
						register.put("VL_" + Util.limparAcentos(Util.meses[mes].toUpperCase()).substring(0, 3), vlMes);
						register.put("VL_TOTAL", vlTotal);
						register.put("VL_MEDIA_ATUAL", (vlTotal / contMeses));
						register.put("VL_MEDIA_ANTERIOR", getGroupClienteMensalMediaAnterior((ArrayList<ItemComparator>)criteriosCopy.clone(), rsm.getInt("cd_pessoa"), connect));
						register.put("NM_CENTRO_CUSTO_CLIENTE", pessoa.getNmPessoa());
						register.put("NM_CENTRO_CUSTO_SUPERIOR", "Contencioso de Massa");
						rsmFinal.addRegister(register);
						register = new HashMap<String, Object>();
						mes = dtPeriodo.get(Calendar.MONTH);
						vlCompleto += vlTotal;
						pessoa = PessoaDAO.get(rsm.getInt("cd_pessoa"), connect);
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
				register.put("VL_MEDIA_ATUAL", (vlTotal / contMeses));
				register.put("VL_MEDIA_ANTERIOR", getGroupCentroCustoMensalMediaAnterior((ArrayList<ItemComparator>)criteriosCopy.clone(), rsm.getInt("cd_centro_custo"), connect));
				register.put("NM_CENTRO_CUSTO_CLIENTE", (centroCusto != null ? centroCusto.getNmCentroCusto() : ""));
				register.put("NM_CENTRO_CUSTO_SUPERIOR", "Ações Estratégicas");
				rsmFinal.addRegister(register);
				vlCompleto += vlTotal;
			}
			
			
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
				float vlTotalCategoria = rsmFinal.getFloat("VL_TOTAL");
				rsmFinal.setValueToField("VL_PORCENTAGEM", (vlTotalCategoria/vlCompleto*100));
				
				
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
	
	public static Result findGroupCategoriasMensal2(ArrayList<sol.dao.ItemComparator> criterios) {
		return findGroupCategoriasMensal2(criterios, null);
	}

	public static Result findGroupCategoriasMensal2(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ArrayList<sol.dao.ItemComparator> crtMediaAnterior = (ArrayList<sol.dao.ItemComparator>)criterios.clone();
			
			String ordenacao 			= "ASC";
			String clTipoPeriodo 		= "A.DT_VENCIMENTO";
			String clTipoPeriodoColumn 	= "DT_VENCIMENTO";
			int year 					= new GregorianCalendar().get(Calendar.YEAR);
			int cdCentroCusto			= 0;
			for (int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("ordenacao")) {
					ordenacao = criterios.get(i).getValue();
					criterios.remove(i);
					i--;
					if( ordenacao.equals("ASC") || !ordenacao.equals("DESC")  )
						ordenacao = "ASC";
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("clTipoPeriodo")) {
					clTipoPeriodo = criterios.get(i).getValue();
					clTipoPeriodoColumn = clTipoPeriodo.substring(2);
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("year")) {
					year = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("H.cd_centro_custo")) {
					cdCentroCusto = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					i--;
				}
				
			}
			
			ResultSetMap rsm = new ResultSetMap();
			Double vlTotalGeral = 0.0;
			
			GregorianCalendar dtInicio = Util.getPrimeiroDiaMes(Calendar.JANUARY, year);
			dtInicio.set(Calendar.HOUR, 0);
			dtInicio.set(Calendar.MINUTE, 0);
			dtInicio.set(Calendar.SECOND, 0);
			criterios.add(new ItemComparator(clTipoPeriodo, Util.formatDateTime(dtInicio, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			
			GregorianCalendar dtFinal = Util.getUltimoDiaMes(Calendar.DECEMBER, year);
			dtFinal.set(Calendar.HOUR, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			criterios.add(new ItemComparator(clTipoPeriodo, Util.formatDateTime(dtFinal, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			
			Double[] vlTotalMensal = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
			
			// contas e centros de custo
			ResultSetMap rsmContas = Search.find(
						 " SELECT A.cd_conta_receber, H.cd_centro_custo "
					   + " FROM adm_conta_receber A"
					   + " LEFT OUTER JOIN adm_conta_receber_categoria H ON (A.cd_conta_receber = H.cd_conta_receber)"
					   + " WHERE 1=1", 
					     " GROUP BY A.cd_conta_receber, H.cd_centro_custo "
					   + " ORDER BY A.cd_conta_receber", 
					     criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			// nomes dos centros de custo
			ResultSetMap rsmCentroCusto = new ResultSetMap(connect.prepareStatement(
					  " SELECT A.cd_centro_custo, A.nm_centro_custo "
					+ " FROM ctb_centro_custo A"
					+ " WHERE A.cd_centro_custo IN ("+Util.join(rsmContas, "cd_centro_custo")+")"
					+ (cdCentroCusto>0 ? " AND A.cd_centro_custo="+cdCentroCusto : "")
					+ " ORDER BY A.nm_centro_custo")
					.executeQuery());
			
			while(rsmCentroCusto.next()) {
				HashMap<String, Object> register = new HashMap<>();
				register.put("NM_CENTRO_CUSTO_CLIENTE", rsmCentroCusto.getString("nm_centro_custo"));
				register.put("CD_CENTRO_CUSTO_CLIENTE", rsmCentroCusto.getInt("cd_centro_custo"));
				Double vlCentroCusto = 0.0;
				
				GregorianCalendar dtAtual = (GregorianCalendar) dtInicio.clone();
				while(Util.compareDates(dtAtual, dtFinal)<=0) { 
					
					//calculo do vl_mes
					GregorianCalendar dtI = Util.getPrimeiroDiaMes(dtAtual.get(Calendar.MONTH), year);
					dtI.set(Calendar.HOUR, 0);
					dtI.set(Calendar.MINUTE, 0);
					dtI.set(Calendar.SECOND, 0);
					
					GregorianCalendar dtF = Util.getUltimoDiaMes(dtAtual.get(Calendar.MONTH), year);
					dtF.set(Calendar.HOUR, 23);
					dtF.set(Calendar.MINUTE, 59);
					dtF.set(Calendar.SECOND, 59);
					
					ArrayList<sol.dao.ItemComparator> crt = (ArrayList<sol.dao.ItemComparator>)criterios.clone();
					crt.add(new ItemComparator(clTipoPeriodo, Util.formatDateTime(dtI, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
					crt.add(new ItemComparator(clTipoPeriodo, Util.formatDateTime(dtF, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
					crt.add(new ItemComparator("H.cd_centro_custo", Integer.toString(rsmCentroCusto.getInt("cd_centro_custo")), ItemComparator.EQUAL, Types.INTEGER));
					
					// contas do período
					rsmContas = Search.find( 
							 " SELECT A.cd_conta_receber, H.cd_centro_custo "
						   + " FROM adm_conta_receber A"
						   + " LEFT OUTER JOIN adm_conta_receber_categoria H ON (A.cd_conta_receber = H.cd_conta_receber)"
						   + " WHERE 1=1", 
						     " GROUP BY A.cd_conta_receber, H.cd_centro_custo "
						   + " ORDER BY A.cd_conta_receber", 
						   crt, connect!=null ? connect : Conexao.conectar(), connect==null);
					
					if(!rsmContas.next()) {
						Double vlMes = 0.0;
						register.put("VL_" + Util.limparAcentos(Util.meses[dtAtual.get(Calendar.MONTH)].toUpperCase()).substring(0, 3), vlMes);
						
						vlTotalMensal[dtAtual.get(Calendar.MONTH)] += vlMes;
						
						dtAtual.add(Calendar.MONTH, 1);
						
						continue;
					}
					
					Double vlClassReceita = 0.0;
					Double vlClassDespesa = 0.0;
					
					ResultSetMap rsmClassReceita = new ResultSetMap(connect.prepareStatement(
							" SELECT A.* "
							+ " FROM adm_conta_receber_categoria A"
							+ " LEFT OUTER JOIN adm_categoria_economica B ON (A.cd_categoria_economica = B.cd_categoria_economica)"
							+ " WHERE A.cd_conta_receber IN ("+Util.join(rsmContas, "cd_conta_receber")+")"
							+ " AND B.tp_categoria_economica="+CategoriaEconomicaServices.TP_RECEITA
							+ " AND A.cd_centro_custo="+Integer.toString(rsmCentroCusto.getInt("cd_centro_custo"))).executeQuery());
					while(rsmClassReceita.next()) {
						vlClassReceita += rsmClassReceita.getDouble("vl_conta_categoria");
					}
					
					Double vlMes = vlClassReceita-vlClassDespesa;
					register.put("VL_" + Util.limparAcentos(Util.meses[dtAtual.get(Calendar.MONTH)].toUpperCase()).substring(0, 3), vlMes);
					
					vlTotalGeral += vlMes;
					vlCentroCusto += vlMes;				
					vlTotalMensal[dtAtual.get(Calendar.MONTH)] += vlMes;
					
					dtAtual.add(Calendar.MONTH, 1);
				}
				register.put("VL_TOTAL", vlCentroCusto);
				rsm.addRegister(register);
			}
			
			while(rsm.next()) {
				rsm.setValueToField("VL_PORCENTAGEM", rsm.getDouble("VL_TOTAL")/vlTotalGeral*100);
				rsm.setValueToField("VL_MEDIA_ATUAL", rsm.getDouble("VL_TOTAL")/12);
				rsm.setValueToField("VL_MEDIA_ANTERIOR", getGroupCentroCustoMensalMediaAnterior(crtMediaAnterior, rsm.getInt("CD_CENTRO_CUSTO_CLIENTE"), connect));
			} rsm.beforeFirst();
			
			Result result = new Result(1);
			result.addObject("rsm", rsm);
			
			result.addObject("VL_TOTAL_JANEIRO",   vlTotalMensal[Calendar.JANUARY]);
			result.addObject("VL_TOTAL_FEVEREIRO", vlTotalMensal[Calendar.FEBRUARY]);
			result.addObject("VL_TOTAL_MARCO", 	   vlTotalMensal[Calendar.MARCH]);
			result.addObject("VL_TOTAL_ABRIL", 	   vlTotalMensal[Calendar.APRIL]);
			result.addObject("VL_TOTAL_MAIO",      vlTotalMensal[Calendar.MAY]);
			result.addObject("VL_TOTAL_JUNHO",     vlTotalMensal[Calendar.JUNE]);
			result.addObject("VL_TOTAL_JULHO",     vlTotalMensal[Calendar.JULY]);
			result.addObject("VL_TOTAL_AGOSTO",    vlTotalMensal[Calendar.AUGUST]);
			result.addObject("VL_TOTAL_SETEMBRO",  vlTotalMensal[Calendar.SEPTEMBER]);
			result.addObject("VL_TOTAL_OUTUBRO",   vlTotalMensal[Calendar.OCTOBER]);
			result.addObject("VL_TOTAL_NOVEMBRO",  vlTotalMensal[Calendar.NOVEMBER]);
			result.addObject("VL_TOTAL_DEZEMBRO",  vlTotalMensal[Calendar.DECEMBER]);
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
	
	public static float getGroupCentroCustoMensalMediaAnterior(ArrayList<ItemComparator> criterios, int cdCentroCusto) {
		return getGroupCentroCustoMensalMediaAnterior(criterios, cdCentroCusto, null);
	}

	public static float getGroupCentroCustoMensalMediaAnterior(ArrayList<ItemComparator> criterios, int cdCentroCusto, Connection connect) {
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
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("ordenacao")) {
					ordenacao = criterios.get(i).getValue();
					criterios.remove(i);
					i--;
					if( ordenacao.equals("ASC") || !ordenacao.equals("DESC")  )
						ordenacao = "ASC";
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("clTipoPeriodo")) {
					clTipoPeriodo = criterios.get(i).getValue();
					clTipoPeriodoColumn = clTipoPeriodo.substring(2);
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("year")) {
					year = Integer.parseInt(criterios.get(i).getValue())-1;
					criterios.remove(i);
					i--;
				}
			}
			String[] limitSkip = Util.getLimitAndSkip(qtLimite, 0);
			
			GregorianCalendar dtInicio = new GregorianCalendar();
			dtInicio.set(Calendar.DAY_OF_MONTH, 1);
			dtInicio.set(Calendar.MONTH, 0);
			dtInicio.set(Calendar.YEAR, (year-1));
			dtInicio.set(Calendar.HOUR, 0);
			dtInicio.set(Calendar.MINUTE, 0);
			dtInicio.set(Calendar.SECOND, 0);
			criterios.add(new ItemComparator(clTipoPeriodo, Util.formatDateTime(dtInicio, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			
			GregorianCalendar dtFinl = new GregorianCalendar();
			dtFinl.set(Calendar.DAY_OF_MONTH, 31);
			dtFinl.set(Calendar.MONTH, 11);
			dtFinl.set(Calendar.YEAR, (year-1));
			dtFinl.set(Calendar.HOUR, 0);
			dtFinl.set(Calendar.MINUTE, 0);
			dtFinl.set(Calendar.SECOND, 0);
			criterios.add(new ItemComparator(clTipoPeriodo, Util.formatDateTime(dtFinl, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			
			ResultSetMap rsm = Search.find("SELECT "+limitSkip[0]+" * "+
					           "FROM adm_conta_receber A " +
					           "LEFT OUTER JOIN adm_conta_receber_categoria    H    ON (A.cd_conta_receber = H.cd_conta_receber) "+
					           "LEFT OUTER JOIN adm_categoria_economica        H2   ON (H.cd_categoria_economica = H2.cd_categoria_economica) "+
					           "LEFT OUTER JOIN ctb_centro_custo               H3   ON (H.cd_centro_custo = H3.cd_centro_custo) "+
					           " WHERE H3.NM_CENTRO_CUSTO NOT LIKE 'CONTENCIOSO DE MASSA%'",
					           "ORDER BY H3.nm_centro_custo, "+clTipoPeriodo+" "+limitSkip[1],
					           criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			
			int mes = 0;
			float vlMes = 0;
			float vlTotal = 0;
			float vlMediaTotal = 0;
			int contMeses = 0;
			int x = 0;
			CentroCusto centroCusto = null;
			while(rsm.next()){
				GregorianCalendar dtPeriodo = rsm.getGregorianCalendar(clTipoPeriodoColumn);
				if(x == 0){
					mes = dtPeriodo.get(Calendar.MONTH);
					centroCusto = CentroCustoDAO.get(rsm.getInt("cd_centro_custo"), connect);
				}
				if(dtPeriodo.get(Calendar.MONTH) == mes && rsm.getInt("cd_centro_custo") == centroCusto.getCdCentroCusto()){
					vlMes += rsm.getFloat("vl_conta_categoria");
				}
				else{
					vlTotal += vlMes;
					
					if(vlMes > 0){
						contMeses++;
					}
					if(mes < 11 && rsm.getInt("cd_centro_custo") == centroCusto.getCdCentroCusto()){
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
	
	public static float getGroupClienteMensalMediaAnterior(ArrayList<ItemComparator> criterios, int cdPessoa) {
		return getGroupClienteMensalMediaAnterior(criterios, cdPessoa, null);
	}

	public static float getGroupClienteMensalMediaAnterior(ArrayList<ItemComparator> criterios, int cdPessoa, Connection connect) {
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
			dtInicio.set(Calendar.HOUR, 0);
			dtInicio.set(Calendar.MINUTE, 0);
			dtInicio.set(Calendar.SECOND, 0);
			criterios.add(new ItemComparator(clTipoPeriodo, Util.formatDateTime(dtInicio, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			
			GregorianCalendar dtFinal = new GregorianCalendar();
			dtFinal.set(Calendar.DAY_OF_MONTH, 31);
			dtFinal.set(Calendar.MONTH, 11);
			dtFinal.set(Calendar.YEAR, (year-1));
			dtFinal.set(Calendar.HOUR, 0);
			dtFinal.set(Calendar.MINUTE, 0);
			dtFinal.set(Calendar.SECOND, 0);
			criterios.add(new ItemComparator(clTipoPeriodo, Util.formatDateTime(dtFinal, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			
			ResultSetMap rsm = Search.find("SELECT "+limitSkip[0]+" * "+
									           "FROM adm_conta_receber A " +
									           "LEFT OUTER JOIN adm_conta_receber_categoria    H    ON (A.cd_conta_receber = H.cd_conta_receber) "+
									           "LEFT OUTER JOIN grl_pessoa                     H3   ON (A.cd_pessoa = H3.cd_pessoa) "+
									           "LEFT OUTER JOIN ctb_centro_custo               H4   ON (H.cd_centro_custo = H4.cd_centro_custo) "+
									           " WHERE H4.NM_CENTRO_CUSTO LIKE 'CONTENCIOSO DE MASSA%'",
									           "ORDER BY H3.nm_pessoa, H3.cd_pessoa, "+clTipoPeriodo+" "+limitSkip[1],
									           criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			int mes = 0;
			float vlMes = 0;
			float vlTotal = 0;
			float vlMediaTotal = 0;
			int contMeses = 0;
			int x = 0;
			CentroCusto centroCusto = null;
			while(rsm.next()){
				GregorianCalendar dtPeriodo = rsm.getGregorianCalendar(clTipoPeriodoColumn);
				if(x == 0){
					mes = dtPeriodo.get(Calendar.MONTH);
					centroCusto = CentroCustoDAO.get(rsm.getInt("cd_centro_custo"), connect);
				}
				if(dtPeriodo.get(Calendar.MONTH) == mes && rsm.getInt("cd_centro_custo") == centroCusto.getCdCentroCusto()){
					vlMes += rsm.getFloat("vl_conta_categoria");
				}
				else{
					vlTotal += vlMes;
					
					if(vlMes > 0){
						contMeses++;
					}
					if(mes < 11 && rsm.getInt("cd_centro_custo") == centroCusto.getCdCentroCusto()){
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
					.search(" SELECT * FROM adm_conta_receber "
						+ " WHERE 1=1"
						+ (lgDelete ? 
						  " AND tp_acao_compliance="+ComplianceManager.TP_ACAO_DELETE	
						  :
						  " AND cd_conta_receber="+cdConta)
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
				
				rsm.setValueToField("nm_st_conta", situacaoContaReceber[rsm.getInt("st_conta")].toUpperCase());
				
				
				
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
