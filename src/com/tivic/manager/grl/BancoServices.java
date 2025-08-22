package com.tivic.manager.grl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.adm.ContaCarteira;
import com.tivic.manager.adm.ContaCarteiraDAO;
import com.tivic.manager.adm.ContaCarteiraServices;
import com.tivic.manager.adm.ContaReceberServices;
import com.tivic.manager.adm.MovimentoConta;
import com.tivic.manager.adm.MovimentoContaDAO;
import com.tivic.manager.adm.MovimentoContaReceber;
import com.tivic.manager.adm.MovimentoContaReceberServices;
import com.tivic.manager.adm.MovimentoContaServices;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;


public class BancoServices {

	public static Result save(Banco banco){
		return save(banco, null);
	}
	
	public static Result save(Banco banco, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(banco==null)
				return new Result(-1, "Erro ao salvar. Banco é nulo");
			
			int retorno;
			if(banco.getCdBanco()==0)	{
				retorno = BancoDAO.insert(banco, connect);
				banco.setCdBanco(retorno);
			}
			else 
				retorno = BancoDAO.update(banco, connect);
			
			if(retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BANCO", banco);
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdBanco){
		return remove(cdBanco, false, null);
	}
	
	public static Result remove(int cdBanco, boolean cascade){
		return remove(cdBanco, cascade, null);
	}
	
	public static Result remove(int cdBanco, boolean cascade, Connection connect){
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
				retorno = BancoDAO.delete(cdBanco, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este banco está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Banco excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir banco!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		String nmBanco = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("NM_BANCO")) {
				nmBanco =	Util.limparTexto(criterios.get(i).getValue());
				nmBanco = nmBanco.trim();
				criterios.remove(i);
				i--;
			}
		}
		return Search.find(
 				    "SELECT * FROM grl_banco "+
 					"WHERE 1=1 "+
 					(!nmBanco.equals("") ?
							" AND TRANSLATE (nm_banco, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', "+
							"					'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmBanco)+"%' "
							: ""),
		            "ORDER BY nm_banco",
		           criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	
	public static void init()	{
		
		/*
		 * BANCOS
		 */
		ArrayList<Banco> bancos = new ArrayList<Banco>();
		bancos.add(new Banco(0, "001", "Banco do Brasil S/A", "BB", "www.bb.com.br"));
		bancos.add(new Banco(0, "004", "Banco do Nordeste do Brasil", "BNB", ""));
		bancos.add(new Banco(0, "104", "Caixa Economica Federal", "CEF", "www.caixa.gov.br"));
		bancos.add(new Banco(0, "237", "Banco Bradesco S/A", "BRD", "www.bradesco.com.br"));
		bancos.add(new Banco(0, "356", "Banco Real S/A", "", ""));
		bancos.add(new Banco(0, "341", "Itaú Unibanco", "", ""));
		bancos.add(new Banco(0, "389", "Banco Mercantil do Brasil", "", ""));
		bancos.add(new Banco(0, "033", "Banco Santander", "", ""));
		bancos.add(new Banco(0, "399", "HSBC", "", ""));
		//
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_banco WHERE nr_banco = ?");
			for(int i=0; i<bancos.size(); i++)	{
				pstmt.setString(1, bancos.get(i).getNrBanco());
				if(!pstmt.executeQuery().next())
					BancoDAO.insert(bancos.get(i), connect);
			}
			System.out.println("Inicialização de Bancos concluida!");
			return;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static String gerarRemessa(int cdConta, int cdContaCarteira)	{
		Connection connect = Conexao.conectar();
		try {
			ContaCarteira carteira = ContaCarteiraDAO.get(cdContaCarteira, cdConta, connect);
			String arquivo = "";
			if(carteira.getTpArquivoEdi()==ContaCarteiraServices._CNAB400)
				arquivo = gerarCnab400(cdConta, cdContaCarteira, connect);
			else
				arquivo = gerarCnab240(cdConta, cdContaCarteira, connect);
			System.out.println(arquivo);
			return arquivo;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return "";
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	private static String gerarCnab240(int cdConta, int cdContaCarteira, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			/********************************************************************************************************
			 * HEADER DO ARQUIVO
			 * ******************************************************************************************************/
			ResultSet rsCarteira = connect.prepareStatement(
										"SELECT A.*, B.*, C.*, D.*, E.*, " +
										"       F.nm_razao_social AS nm_empresa, F.nr_cnpj " +
										"FROM adm_conta_carteira A, adm_conta_financeira B, grl_agencia C, " +
										"     grl_banco D, grl_empresa E, grl_pessoa_juridica F " +
										"WHERE A.cd_conta          = " + cdConta +
										"  AND A.cd_conta_carteira = " + cdContaCarteira +
										"  AND A.cd_conta          = B.cd_conta " +
										"  AND B.cd_agencia        = C.cd_agencia " +
										"  AND C.cd_banco          = D.cd_banco " +
										"  AND B.cd_empresa        = E.cd_empresa " +
										"  AND E.cd_empresa        = F.cd_pessoa").executeQuery();
			if(!rsCarteira.next())	{
				return "";
			}
			GregorianCalendar dtCriacao = new GregorianCalendar();
			String nrCnpj  	  = rsCarteira.getString("nr_cnpj");
			String nrConvenio = rsCarteira.getString("nr_cedente");
			String nrAgencia  = rsCarteira.getString("nr_agencia");
			String dvAgencia  = "";
			if(nrAgencia.indexOf("-")>=0)	{
				dvAgencia = nrAgencia.substring(nrAgencia.indexOf("-")+1);
				nrAgencia = nrAgencia.substring(0,nrAgencia.indexOf("-"));
			}
			String nrConta		  = rsCarteira.getString("nr_conta");
			String dvConta		  = rsCarteira.getString("nr_dv");
			String dvAgenciaConta = "";
			String nmEmpresa	  = rsCarteira.getString("nm_empresa");
			String nmBanco		  = rsCarteira.getString("nm_banco");
			String nrBanco		  = rsCarteira.getString("nr_banco");
			int tpArquivo	=	1,
				nrRemessa	=	1,
				nrLayout	= 	82,
				nrDensidade	=	1600;
			String stream = getHeaderCnab240(nrBanco,nrCnpj,nrConvenio,
											 nrAgencia,dvAgencia,nrConta,dvConta,
											 dvAgenciaConta,nmEmpresa,nmBanco,
											 nrRemessa, nrLayout, nrDensidade,
											 tpArquivo,dtCriacao); // Reservado/Brancos para Banco
			/********************************************************************************************************
			 * REGISTROS
			 * ******************************************************************************************************/
			String sql = "SELECT * " +
						 "FROM grl_arquivo_registro A " +
						 "LEFT OUTER JOIN grl_arquivo_tipo_registro B ON (A.cd_tipo_registro = B.cd_tipo_registro) "+
						 "JOIN adm_conta_receber C ON (A.cd_conta_receber = C.cd_conta_receber) "+
						 "JOIN grl_pessoa        D ON (C.cd_pessoa = D.cd_pessoa) " +
						 "WHERE C.cd_conta          = "+cdConta+
						 "  AND C.cd_conta_carteira = "+cdContaCarteira+
						 "  AND A.cd_arquivo IS NULL " +
						 "ORDER BY id_tipo_registro";
			ResultSet rsRegistros = connect.prepareStatement(sql).executeQuery();
			float vlTotalSimples    = 0;
			float vlTotalVinculada  = 0;
			float vlTotalCaucionada = 0;
			float vlTotalDescontada = 0;
			int nrLote 		 = 1;
			int qtSimples 	 = 0;
			int qtVinculada	 = 0;
			int qtCaucionada = 0;
			int qtDescontada = 0;
			/**********************************************************************************************************
			 * HEADER DO LOTE
			 * *******************************************************************************************************/
			// LOTES
			String dsMensagem1	= rsCarteira.getString("txt_mensagem");
			String dsMensagem2	= "";
			stream += getHeaderLoteCobranca(nrBanco,nrCnpj,nrConvenio,
											 nrAgencia,dvAgencia,nrConta,dvConta,
											 dvAgenciaConta,nmEmpresa,dsMensagem1,dsMensagem2,
											 nrRemessa, nrLote, dtCriacao);
			int nrSeqLote = 0;
			while(rsRegistros.next())	{
				nrSeqLote++;
				qtSimples++;
				vlTotalSimples += rsRegistros.getFloat("vl_conta");
				/**********************************************************************************************************
				 * SEGMENTOS P - Dados do Título
				 * *******************************************************************************************************/
				int tpDocumento 	   = 1;
				int tpEmissaoBloqueto  = 2; // 1-Banco Emite;  2-Cliente Emite;    3-Banco Pré-emite e Cliente Complementa
											// 4-Banco Reemite;5-Banco Não Reemite 7-Banco Emitente - Aberta
											// 8-Banco Emitente - Auto-envelopável
 				int tpDistribuicao	   = 2; // 1-Banco Distribui;    2-Cliente Distribui;
 				                            // 3-Banco envia e-mail; 4-Banco envia SMS
				int tpEspecieTitulo	   = rsRegistros.getInt("tp_documento");
				int tpJurosMora		   = 2; // 1-Valor por Dia; 2-Taxa Mensal; 3-Isento
				int cdContaReceber     = rsRegistros.getInt("cd_conta_receber");
				int tpProtesto         = 3; // 1-Protestar Dias Corridos;  2-Protestar Dias Úteis
											// 3-Não Protestar;            4-Protestar Fim Falimentar - Dias Úteis
				 							// 5-Protestar Fim Falimentar - Dias Corridos; 9-Cancelamento Protesto Automático
				int nrDiasProtesto	   = rsCarteira.getInt("qt_dias_protesto");
				if(nrDiasProtesto>0)
					tpProtesto = 1;
				int tpBaixa 		   = 2; // 1-Baixar/Devolver; 2-Não Baixar/Não  Devolver
											// 3-Cancelar Prazo para Baixa / Devolução
				int nrDiasBaixa		   = rsCarteira.getInt("qt_dias_devolucao");
				if(nrDiasBaixa>0)
					tpBaixa = 1;
				if(nrDiasBaixa<nrDiasProtesto && tpProtesto!=3)
					throw new Exception("Número de dias para baixa menor do que número de dias para protesto!");

				int cdContrato    	 = rsRegistros.getInt("cd_contrato");
				String nrNossoNumero = rsRegistros.getString("id_conta_receber");
				String nrDocumento 	 = rsRegistros.getString("nr_documento");
				String tpAceite		 = rsCarteira.getString("nm_aceite")!=null&&rsCarteira.getString("nm_aceite").equals("1")?"A":"N";
				String cdMovimento 	 = rsRegistros.getString("id_tipo_registro");
				if(cdMovimento==null)
					cdMovimento = "01";
				else if(cdMovimento.length()>6)
					cdMovimento = cdMovimento.substring(7);
				GregorianCalendar dtVencimento  = Util.convTimestampToCalendar(rsRegistros.getTimestamp("dt_vencimento"));
				GregorianCalendar dtEmissao 	= Util.convTimestampToCalendar(rsRegistros.getTimestamp("dt_emissao"));
				// Prazo pra aplicação da multa por mora
				int qtIsencaoJurosMora = rsCarteira.getInt("qt_dias_multa");
				GregorianCalendar dtJurosMora	= (GregorianCalendar)dtVencimento.clone();
				if(qtIsencaoJurosMora>0)
					dtJurosMora.set(Calendar.DATE, qtIsencaoJurosMora);
				// Prazo para pagamento com desconto
				GregorianCalendar dtPagamentoComDesconto = (GregorianCalendar)dtVencimento.clone();
				if(rsCarteira.getInt("qt_dias_desconto")>0)
					dtPagamentoComDesconto.set(Calendar.DATE, rsCarteira.getInt("qt_dias_desconto")*-1);
				float vlConta      = rsRegistros.getFloat("vl_conta");
				float prJurosMora  = rsCarteira.getFloat("pr_juros");
				float vlDescontoAdimplencia = rsCarteira.getFloat("pr_desconto_adimplencia") * vlConta / 100;
				float vlAbatimento = rsRegistros.getFloat("vl_abatimento");
				float vlIOF		   = 0;
				int tpDesconto = 1; // 1-Valor Fixo Até a Data Informada;  2-Percentual Até a Data Informada
									// 3-Valor por Antecipação Dia Corrido 4-Valor por Antecipação Dia Útil
									// 5-Percentual Sobre o Valor Nominal Dia Corrido
									// 6-Percentual Sobre o Valor Nominal Dia Útil; 7-Cancelamento de Desconto

				int tpCarteira = 1; // (1-Simples,2-Vinculada,3-Caucionada,4-Descontada,5-Vendor);
				int tpCadastramento = 1; // 1-Com Cadastramento (Cobrança Registrada)
										 // 2-Sem Cadastramento (Cobrança sem Registro) Obs.: Destina-se somente para emissão de bloqueto pelo banco
										 // 3-Com Cadastramento / Recusa do Débito Automático

				/**********************************************************************************************************
				 * SEGMENTOS Q - Dados do Sacado
				 * *******************************************************************************************************/
				String segmentoP = getSegmentoP(nrBanco, nrLote, nrSeqLote, cdMovimento,
												nrAgencia, dvAgencia, dvAgenciaConta, nrConta,
												dvConta, nrNossoNumero, cdContrato,
												tpDocumento, tpEmissaoBloqueto, tpDistribuicao, nrDocumento,
												dtVencimento, dtEmissao,
												dtPagamentoComDesconto, dtJurosMora,
												vlConta, vlDescontoAdimplencia, vlIOF, vlAbatimento, cdContaReceber,
												tpEspecieTitulo, tpBaixa, tpAceite, tpJurosMora, prJurosMora,
												qtIsencaoJurosMora, tpProtesto, nrDiasProtesto, nrDiasBaixa,
												tpCarteira, tpCadastramento, tpDesconto);
				/* SEGMENTO P - Dados do Sacado*/
				ResultSet rsAux;
				String nrCpfCnpjSacado = "";
				String nmCliente	= rsRegistros.getString("nm_pessoa");
				int tpInscSacado = 0; // 0-Isento / Não Informado; 1-CPF
				 				 	  // 2-CGC / CNPJ; 3-PIS / PASEP; 9-Outros
				if(rsRegistros.getInt("gn_pessoa")==PessoaServices.TP_FISICA)	{
					rsAux = connect.prepareStatement("SELECT * FROM grl_pessoa_fisica " +
							                         "WHERE cd_pessoa = "+rsRegistros.getInt("cd_pessoa")).executeQuery();
					if(rsAux.next())	{
						tpInscSacado = 1; // CPF
						nrCpfCnpjSacado = rsAux.getString("nr_cpf");
					}
				}
				else	{
					rsAux = connect.prepareStatement("SELECT * FROM grl_pessoa_juridica " +
							                         "WHERE cd_pessoa = "+rsRegistros.getInt("cd_pessoa")).executeQuery();
					if(rsAux.next())	{
						tpInscSacado = 2; // CNPJ
						nrCpfCnpjSacado = rsAux.getString("nr_cnpj");
					}
				}
				// Dados do endereço
				String dsEndereco = "";
				String nmBairro	  = "";
				String nrCep      = "";
				String nmCidade   = "";
				String sgUf       = "";
				rsAux = connect.prepareStatement("SELECT A.*, B.nm_cidade, C.sg_estado " +
						                         "FROM grl_pessoa_endereco A " +
						                         "LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade) " +
						                         "LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado) " +
											     "WHERE A.cd_pessoa = "+rsRegistros.getInt("cd_pessoa")+
											     "ORDER BY lg_cobranca DESC ").executeQuery();
				if(rsAux.next())	{
					dsEndereco = rsAux.getString("nm_logradouro");
					if(rsAux.getString("nr_endereco")!=null)
						dsEndereco += ", "+rsAux.getString("nr_endereco");
					if(rsAux.getString("nm_complemento")!=null)
						dsEndereco += ", "+rsAux.getString("nm_complemento");
					nmBairro = rsAux.getString("nm_bairro");
					nrCep    = rsAux.getString("nr_cep");
					nmCidade = rsAux.getString("nm_cidade");
					sgUf 	 = rsAux.getString("sg_estado");
				}
				// Dados do avalista
				int tpInscAvalista = 0; // Não informado
				String nrCpfCnpjAvalista="";
				String nmAvalista="";
				String segmentoQ = getSegmentoQ(nrBanco, nrLote, nrSeqLote,
												cdMovimento, tpInscSacado, nrCpfCnpjSacado, nmCliente,
												dsEndereco, nmBairro, nrCep, nmCidade, sgUf,
												tpInscAvalista, nrCpfCnpjAvalista, nmAvalista);
				stream += segmentoP+segmentoQ;
			}
			/**********************************************************************************************************
			 * TRAILER DO LOTE
			 * *******************************************************************************************************/
			stream += Util.fill(nrBanco, 3, '0', 'E') + // Código do Banco na Compensação
			        	 Util.fillNum(nrLote, 4) + // Lote Serviço
			        	 "5" + // Tipo de Registro (0-H Arquivo,1-H Lote,2-Reg's Iniciais Lote
			    	       	   //	               3-Detalhe,4-Reg's Finais Lote,5-Tr Lote,9-Tr de Arquivo)
			        	 Util.fillAlpha("", 9)+ // Reservado/Brancos
			        	 Util.fillNum(nrSeqLote, 6)+ // Quantidades de Registro no Lote
			        	 Util.fillNum(qtSimples, 6)+ // Totalização da Cobrança Simples (Quantidade)
			        	 Util.fillNum(Math.round(vlTotalSimples), 17)+ // Totalização da Cobrança Simples (Valor)
			        	 Util.fillNum(qtVinculada, 6)+ // Totalização da Cobrança Vinculada (Quantidade)
			        	 Util.fillNum(Math.round(vlTotalVinculada), 17)+ // Totalização da Cobrança Vinculada (Valor)
			        	 Util.fillNum(qtCaucionada, 6)+ // Totalização da Cobrança Caucionada (Quantidade)
			        	 Util.fillNum(Math.round(vlTotalCaucionada), 17)+ // Totalização da Cobrança Caucionada (Valor)
			        	 Util.fillNum(qtDescontada, 6)+ // Totalização da Cobrança Descontada (Quantidade)
			        	 Util.fillNum(Math.round(vlTotalDescontada), 17)+ // Totalização da Cobrança Descontada (Valor)
			        	 Util.fillAlpha("", 8)+ // Número do Aviso de Lançamento
			        	 Util.fillAlpha("", 117)+
			        	 "\n"; // Uso Exclusivo FEBRABAN/CNAB
			/**********************************************************************************************************
			 * TRAILER DO ARQUIVO
			 * *******************************************************************************************************/
			// Trailer
			int qtLotes=0, qtRegistros=0, qtContas=0;
			stream += Util.fill(nrBanco, 3, '0', 'E') + // Código do Banco na Compensação
			             "9999" + // Lote Serviço
			             "9" + // Tipo de Registro (0-H Arquivo,1-H Lote,2-Reg's Iniciais Lote
		                	   //	                3-Detalhe,4-Reg's Finais Lote,5-Tr Lote,9-Tr de Arquivo
			             Util.fillAlpha("", 9)+ // Reservado/Brancos
			             Util.fillNum(qtLotes, 6)+ // Quantidades de Lotes do arquivo
			             Util.fillNum(qtRegistros, 6)+ // Quantidade de registros do arquivo
			             Util.fillNum(qtContas, 6)+ // Qtde de Contas p/ Conc. (Lotes)
			             Util.fillAlpha("", 205)+"\n";
			return stream;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return "";
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	private static String gerarCnab400(int cdConta, int cdContaCarteira, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			/********************************************************************************************************
			 * HEADER DO ARQUIVO
			 * ******************************************************************************************************/
			ResultSet rsCarteira = connect.prepareStatement(
										"SELECT A.*, B.*, C.*, D.*, E.*, " +
										"       F.nm_razao_social AS nm_empresa, F.nr_cnpj " +
										"FROM adm_conta_carteira A, adm_conta_financeira B, grl_agencia C, " +
										"     grl_banco D, grl_empresa E, grl_pessoa_juridica F " +
										"WHERE A.cd_conta          = " + cdConta +
										"  AND A.cd_conta_carteira = " + cdContaCarteira +
										"  AND A.cd_conta          = B.cd_conta " +
										"  AND B.cd_agencia        = C.cd_agencia " +
										"  AND C.cd_banco          = D.cd_banco " +
										"  AND B.cd_empresa        = E.cd_empresa " +
										"  AND E.cd_empresa        = F.cd_pessoa").executeQuery();
			if(!rsCarteira.next())	{
				return "";
			}
			GregorianCalendar dtCriacao = new GregorianCalendar();
			String nrCedente  = rsCarteira.getString("nr_cedente");
			//String nrConvenio = rsCarteira.getString("nr_convenio");
			String nrCarteira = rsCarteira.getString("nr_carteira");
			String nrServico  = rsCarteira.getString("nr_servico");
			String nrAgencia  = rsCarteira.getString("nr_agencia");
			if(nrAgencia.indexOf("-")>=0)
				nrAgencia = nrAgencia.substring(0,nrAgencia.indexOf("-"));
			String nrConta		  = rsCarteira.getString("nr_conta");
			String dvConta		  = rsCarteira.getString("nr_dv");
			String nmEmpresa	  = rsCarteira.getString("nm_empresa");
			String nmBanco		  = rsCarteira.getString("nm_banco");
			String nrBanco		  = rsCarteira.getString("nr_banco");
			String nmAceite       = rsCarteira.getString("nm_aceite");
			nmAceite = nmAceite.equalsIgnoreCase("sim") ? "A" : "N";
			int nrRemessa =	1;
			String stream = getHeaderCnab400(nrBanco, nrCedente,
											 nrServico, nmEmpresa, nmBanco,
											 nrRemessa, dtCriacao);
			/********************************************************************************************************
			 * REGISTROS
			 * ******************************************************************************************************/
			String sql = "SELECT * " +
						 "FROM grl_arquivo_registro A " +
						 "LEFT OUTER JOIN grl_arquivo_tipo_registro B ON (A.cd_tipo_registro = B.cd_tipo_registro) "+
						 "JOIN adm_conta_receber C ON (A.cd_conta_receber = C.cd_conta_receber) "+
						 "JOIN grl_pessoa        D ON (C.cd_pessoa = D.cd_pessoa) " +
						 "LEFT OUTER JOIN adm_tipo_documento E ON (C.cd_tipo_documento = E.cd_tipo_documento) " +
						 "WHERE C.cd_conta          = "+cdConta+
						 "  AND C.cd_conta_carteira = "+cdContaCarteira+
						 "  AND A.cd_arquivo IS NULL " +
						 "ORDER BY id_tipo_registro";
			ResultSet rsRegistros = connect.prepareStatement(sql).executeQuery();
			int nrSequencial = 0;
			while(rsRegistros.next())	{
				nrSequencial++;
				/**********************************************************************************************************
				 * DADOS DO TÍTULO
				 * *******************************************************************************************************/
				String idTipoDocumento = rsRegistros.getString("id_tipo_documento");
				int tpEmissaoBloqueto  = 2; // 1-Banco Emite;  2-Cliente Emite;
				int tpProtesto         = 0;
				int nrDiasProtesto	   = rsCarteira.getInt("qt_dias_protesto");
				if(nrDiasProtesto>0)
					tpProtesto = 6;
				int nrDiasBaixa		   = rsCarteira.getInt("qt_dias_devolucao");
				if(nrDiasBaixa<nrDiasProtesto && tpProtesto!=3)
					throw new Exception("Número de dias para baixa menor do que número de dias para protesto!");

				String nrNossoNumero = rsRegistros.getString("id_conta_receber")==null ? "" : rsRegistros.getString("id_conta_receber").replaceAll("[\\D]", "");
				String dvNossoNumero = ContaReceberServices.getDvNossoNumero(nrNossoNumero, rsCarteira.getInt("tp_digito"), rsCarteira.getInt("nr_base_inicial"), rsCarteira.getInt("nr_base_final"));
				String nrDocumento 	 = rsRegistros.getString("nr_documento");
				String tpAceite		 = rsCarteira.getString("nm_aceite")!=null&&rsCarteira.getString("nm_aceite").equals("1")?"A":"N";
				String cdMovimento 	 = rsRegistros.getString("id_tipo_registro");
				int cdContaReceber   = rsRegistros.getInt("cd_conta_receber");
				if(cdMovimento==null)
					cdMovimento = "01";
				else if(cdMovimento.length()>6)
					cdMovimento = cdMovimento.substring(7);
				GregorianCalendar dtVencimento  = Util.convTimestampToCalendar(rsRegistros.getTimestamp("dt_vencimento"));
				GregorianCalendar dtEmissao 	= Util.convTimestampToCalendar(rsRegistros.getTimestamp("dt_emissao"));
				// Prazo pra aplicação da multa por mora
				int qtIsencaoJurosMora = rsCarteira.getInt("qt_dias_multa");
				GregorianCalendar dtJurosMora	= (GregorianCalendar)dtVencimento.clone();
				if(qtIsencaoJurosMora>0)
					dtJurosMora.set(Calendar.DATE, qtIsencaoJurosMora);
				// Prazo para pagamento com desconto
				GregorianCalendar dtPagamentoComDesconto = (GregorianCalendar)dtVencimento.clone();
				if(rsCarteira.getInt("qt_dias_desconto")>0)
					dtPagamentoComDesconto.set(Calendar.DATE, rsCarteira.getInt("qt_dias_desconto")*-1);
				float vlConta      = rsRegistros.getFloat("vl_conta");
				float prJurosMora  = rsCarteira.getFloat("pr_juros");
				float vlDescontoAdimplencia = rsCarteira.getFloat("pr_desconto_adimplencia") * vlConta / 100;
				float vlAbatimento = rsRegistros.getFloat("vl_abatimento");
				float vlIOF		   = 0;
				/**********************************************************************************************************
				 * DADOS DO SACADO
				 * *******************************************************************************************************/
				/* SEGMENTO P - Dados do Sacado*/
				ResultSet rsAux;
				String nrCpfCnpjSacado = "";
				String nmCliente	= rsRegistros.getString("nm_pessoa");
				int tpInscSacado = 0; // 0-Isento / Não Informado; 1-CPF
				 				 	  // 2-CGC / CNPJ; 3-PIS / PASEP; 9-Outros
				if(rsRegistros.getInt("gn_pessoa")==PessoaServices.TP_FISICA)	{
					rsAux = connect.prepareStatement("SELECT * FROM grl_pessoa_fisica " +
							                         "WHERE cd_pessoa = "+rsRegistros.getInt("cd_pessoa")).executeQuery();
					if(rsAux.next())	{
						tpInscSacado = 1; // CPF
						nrCpfCnpjSacado = rsAux.getString("nr_cpf");
					}
				}
				else	{
					rsAux = connect.prepareStatement("SELECT * FROM grl_pessoa_juridica " +
							                         "WHERE cd_pessoa = "+rsRegistros.getInt("cd_pessoa")).executeQuery();
					if(rsAux.next())	{
						tpInscSacado = 2; // CNPJ
						nrCpfCnpjSacado = rsAux.getString("nr_cnpj");
					}
				}
				// Dados do endereço
				String dsEndereco = "";
				String nrCep      = "";
				rsAux = connect.prepareStatement("SELECT A.*, B.nm_cidade, C.sg_estado " +
						                         "FROM grl_pessoa_endereco A " +
						                         "LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade) " +
						                         "LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado) " +
											     "WHERE A.cd_pessoa = "+rsRegistros.getInt("cd_pessoa")+
											     "ORDER BY lg_cobranca DESC ").executeQuery();
				if(rsAux.next())	{
					dsEndereco = rsAux.getString("nm_logradouro");
					if(rsAux.getString("nr_endereco")!=null)
						dsEndereco += (dsEndereco.equals("")?"":", ")+rsAux.getString("nr_endereco");
					if(rsAux.getString("nm_complemento")!=null)
						dsEndereco += (dsEndereco.equals("")?"":", ")+rsAux.getString("nm_complemento");
					if(rsAux.getString("nm_bairro")!=null)
						dsEndereco += (dsEndereco.equals("")?"":", ")+rsAux.getString("nm_bairro");
					if(rsAux.getString("nm_cidade")!=null)
						dsEndereco += (dsEndereco.equals("")?"":", ")+rsAux.getString("nm_cidade");
					if(rsAux.getString("nm_cidade")!=null)
						dsEndereco += (dsEndereco.equals("")?"":"-")+rsAux.getString("sg_estado");
					nrCep    = rsAux.getString("nr_cep");
				}
				// Dados do avalista
				String nmAvalista="";

				String registro = "1" + 						// Identificação do Registro
				                  Util.fillNum(0, 5)+ 			// Código da Agência do Sacado Exclusivo para Débito em Conta
				                  Util.fillAlpha("", 1)+ 		// Dígito da Agência do Sacado
				                  Util.fillNum(0, 5)+ 			// Razão da Conta do Sacado
				                  Util.fillNum(0, 7)+ 			// Conta-Corrente
				                  Util.fillAlpha("", 1)+ 		// Dígito da Conta do Sacado
				                  // Identificação do cedente junto ao banco
				                  "0"+ 							// Zero
				                  Util.fillAlpha(nrCarteira, 3)+// Número da Carteira
				                  Util.fillAlpha(nrAgencia, 5)+ // Número da Agência
				                  Util.fillAlpha(nrConta, 7)+   // Número da Conta
				                  Util.fillAlpha(dvConta, 1)+   // Número da Conta
				                  Util.fillAlpha(String.valueOf(cdContaReceber), 25)+       // Nº Controle do Participante
				                  Util.fillAlpha(nrBanco, 3)+   // Nº do Banco 237
				                  Util.fillNum(0, 5)+           // Zeros
				                  Util.fillAlpha(nrNossoNumero, 11)+ // Nosso Número
				                  Util.fillAlpha(dvNossoNumero, 1)+  // Zeros
				                  Util.fillNum(Math.round(vlDescontoAdimplencia * 100), 10)+ // Valor Desconto Bonificação Dia
				                  tpEmissaoBloqueto+            // Cliente Emite
				                  "N"+ 							// Não registra na cobrança
				                  Util.fillAlpha("", 10)+       // Brancos
				                  " "+ 							// Rateiro (a empresa não participa)
				                  "2"+ 							// Não emite aviso
				                  Util.fillAlpha("", 2)+        // Brancos
				                  Util.fillAlpha(cdMovimento, 2)+ 				// Código da Ocorrência
				                  Util.fillAlpha(nrDocumento, 10)+ 				// Número do documento
				              	  Util.formatDate(dtVencimento, "ddMMyy")+ 		// Data de Vencimento
				              	  Util.fillNum(Math.round(vlConta * 100), 13)+ 	// Valor do título (2 casas decimais)
				                  Util.fillNum(0, 3)+ 							// Nº do Banco na Câmara de Compensação
				                  Util.fillNum(0, 5)+ 							// Código da Agência Depositária
				                  Util.fillAlpha(idTipoDocumento, 2)+           // Espécie de Título
				              	  tpAceite+ 									// Aceite
				              	  Util.formatDate(dtEmissao, "ddMMyy")+			// Data de emissão
				              	  Util.fillNum(tpProtesto, 2)+ 					// 1ª Instrução: Tipo de Protesto
				              	  Util.fillNum(nrDiasProtesto, 2)+ 				// 2ª Instrução: Dias para o protesto
				  		          Util.fillNum(Math.round(prJurosMora * vlConta / 30), 13)+  // Valor a ser cobrado por Dia de Atraso
				              	  Util.formatDate(dtPagamentoComDesconto, "ddMMyy")+		 // Data limite para desconto
				  		          Util.fillNum(Math.round(vlDescontoAdimplencia * 100), 13)+ // Valor Desconto
				  		          Util.fillNum(Math.round(vlIOF * 100), 13)+ 				 // Valor IOF
				  		          Util.fillNum(Math.round(vlAbatimento * 100), 13)+ 		 // Valor Abatimento
				  		          Util.fillNum(tpInscSacado, 2)+ 		// Tipo de Inscrição do Sacado
				  		          Util.fill(nrCpfCnpjSacado, 14, '0', 'E')+ 	// CNPJ/CPF
				  		          Util.fillAlpha(nmCliente, 40)+ 		// Nome do Sacado
				  		          Util.fillAlpha(dsEndereco, 40)+ 		// Endereço completo
				  		          Util.fillAlpha("", 12)+ 			// Mensagem
				  		          Util.fillAlpha(nrCep, 8)+ 		// CEP
				  		          Util.fillAlpha(nmAvalista, 60)+ 	// Avalista / Mensagem
				  		          Util.fillNum(nrSequencial, 6)+ 	// Valor Abatimento
				                  "\n";
				stream += registro;
			}
			/**********************************************************************************************************
			 * TRAILER DO ARQUIVO
			 * *******************************************************************************************************/
			// Trailer
			stream += "9" + // Identificação do Registro
			           Util.fillAlpha("", 393)+ // Reservado/Brancos
			           Util.fillNum(nrSequencial+1, 6)+"\n";
			return stream;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return "";
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	private static String getHeaderCnab400(String nrBanco, String nrCedente,
			String nrServico, String nmEmpresa, String nmBanco,
			int nrRemessa, GregorianCalendar dtCriacao)
	{
		nrCedente = nrCedente!=null ? nrCedente.replaceAll("\\-", "") : "";
		return "0"+ // Identificação do Registro
		       "1"+ // Identificação do Arquivo Remessa
		       "REMESSA"+ // Literal Remessa
		       Util.fill(nrServico, 2, '0', 'E')+   	 // Código de Serviço
		       Util.fill("COBRANÇA", 15, ' ', 'D')+ 	 // Descrição do Serviço
		       Util.fill(nrCedente, 20, '0', 'E')+ 	 // Código da Empresa
		       Util.fillAlpha(nmEmpresa, 30)+       	 // Nome da Empresa
		       Util.fill(nrBanco, 3, '0', 'E') +    	 // Código do Banco na Compensação
		       Util.fillAlpha(nmBanco, 15)+         	 // Nome do Banco
		       Util.formatDateTime(dtCriacao, "ddMMyy")+ // Data e Hora da Criação do arquivo
		       Util.fillAlpha("", 8)+                    // Em Branco
		       "MX"+ 									 // Identificação do Sistema
		       Util.fillNum(nrRemessa, 7)+               // Nº Seqüencial do Arquivo
		       Util.fillAlpha("", 277)+        			 // Em Branco
		       Util.fillNum(1, 6)+"\n";                       // Nº Seqüencial do Registro de Um em Um
	}


	private static String getHeaderCnab240(String nrBanco, String nrCnpj, String nrConvenio,
			String nrAgencia, String dvAgencia, String nrConta, String dvConta,
			String dvAgenciaConta, String nmEmpresa, String nmBanco,
			int nrRemessa, int nrLayout, int nrDensidade, int tpArquivo,
			GregorianCalendar dtCriacao)
	{
		return Util.fill(nrBanco, 3, '0', 'E') + // Código do Banco na Compensação
		        "0000" + // Lote Serviço
		        "0" + // Tipo de Registro (0-H Arquivo,1-H Lote,2-Reg's Iniciais Lote
		        	  //	               3-Detalhe,4-Reg's Finais Lote,5-Tr Lote,9-Tr de Arquivo
		        Util.fillAlpha("", 9)+ // Reservado/Brancos
		        "2"+ // Tipo de Inscrição (0-Isento, 1-CPF, 2-CNPJ, 3-PIS/PASEP, 9-Outros)
		        Util.fill(nrCnpj, 14, '0', 'E')+ // Número de Inscrição da Empresa (Num)
		        Util.fillAlpha(nrConvenio, 20)+     // Número do Convênio
		        Util.fill(nrAgencia, 5, '0', 'E')+  // Agência Mantenedora da Conta (Num)
		        Util.fillAlpha(dvAgencia, 1)+       // Dígito verificador da agência
		        Util.fill(nrConta, 12, '0', 'D')+   // Número da Conta Corrente (Num)
		        Util.fillAlpha(dvConta, 1)+         // Dígito verificador da conta
		        Util.fillAlpha(dvAgenciaConta, 1)+  // Dígito verificador da agência/conta
		        Util.fillAlpha(nmEmpresa, 30)+      // Nome da Empresa
		        Util.fillAlpha(nmBanco, 30)+        // Nome da Empresa
		        Util.fillAlpha("", 10)+             // Reservado/Brancos
		        tpArquivo+                          // Tipo de Arquivo (1-Remessa, 2-Retorno)
		        Util.formatDateTime(dtCriacao, "ddMMyyyyHHmmss")+ // Data e Hora da Criação do arquivo
		        Util.fillNum(nrRemessa, 6)+         // Número da Remessa
		        Util.fillNum(nrLayout, 3)+          // Versão do layout
		        Util.fillNum(nrDensidade, 5)+ // Densidade de gravação do Arquivo
		        Util.fillAlpha("", 20)+ // Reservado/Brancos para Banco
		        Util.fillAlpha("", 20)+ // Reservado/Brancos para a Empresa
		        Util.fillAlpha("", 29)+"\n";
	}

	private static String getHeaderLoteCobranca(String nrBanco, String nrCnpj, String nrConvenio,
			String nrAgencia, String dvAgencia, String nrConta, String dvConta,
			String dvAgenciaConta, String nmEmpresa, String dsMensagem1, String dsMensagem2,
			int nrRemessa, int nrLote, GregorianCalendar dtCriacao)
	{
		return Util.fill(nrBanco, 3, '0', 'E') + // Código do Banco na Compensação
		    	Util.fillNum(nrLote, 4) + // Lote Serviço
		    	"1" + // Tipo de Registro (0-H Arquivo,1-H Lote,2-Reg's Iniciais Lote
			      	  //	               3-Detalhe,4-Reg's Finais Lote,5-Tr Lote,9-Tr de Arquivo)
		    	"R"+ // Tipo de Operação (R-Arquivo Remessa,T-Arquivo Retorno)
		    	"01"+ // Tipo de Serviço (01-Cobrança)
		    	"  "+ // Reservado/Brancos (2)
		    	"041"+ // Número da Versão do Layout do lote
		    	" "+ // Reservado/Brancos (1)
		        "2"+ // Tipo de Inscrição (0-Isento, 1-CPF, 2-CNPJ, 3-PIS/PASEP, 9-Outros)
		        Util.fill(nrCnpj, 15, '0', 'E')+ // Número de Inscrição da Empresa (Num)
		        Util.fillAlpha(nrConvenio, 20)+ // Número do Convênio
		        Util.fill(nrAgencia, 5, '0', 'D')+ // Agência Mantenedora da Conta (Num)
		        Util.fillAlpha(dvAgencia, 1)+ // Dígito verificador da agência
		        Util.fill(nrConta, 12, '0', 'D')+ // Número da Conta Corrente (Num)
		        Util.fillAlpha(dvConta, 1)+ // Dígito verificador da conta
		        Util.fillAlpha(dvAgenciaConta, 1)+ // Dígito verificador da agência/conta
		        Util.fillAlpha(nmEmpresa, 30)+ // Nome da Empresa
		        Util.fillAlpha(dsMensagem1, 40)+ // Mensagem 1
		        Util.fillAlpha(dsMensagem2, 40)+ // Mensagem 2
		        Util.fillNum(nrRemessa, 8)+ // Número da Remessa
		        Util.formatDateTime(dtCriacao, "ddMMyyyy")+ // Data de Gravação da Remessa/Retorno
		        "00000000"+ // Data de Crédito, somente no arquivo de retorno
		        Util.fillAlpha("", 33)+"\n";
	}

	private static String getSegmentoP(String nrBanco, int nrLote, int nrSeqLote, String cdMovimento,
			String nrAgencia, String dvAgencia, String dvAgenciaConta, String nrConta,
			String dvConta, String nrNossoNumero, int nrContrato,
			int tpDocumento, int tpEmissaoBloqueto, int tpDistribuicao, String nrDocumento,
			GregorianCalendar dtVencimento, GregorianCalendar dtEmissao,
			GregorianCalendar dtPagamentoComDesconto, GregorianCalendar dtJurosMora,
			float vlConta, float vlDescontoAdimplencia, float vlIOF, float vlAbatimento, int cdContaReceber,
			int tpEspecieTitulo, int tpBaixa, String tpAceite, int tpJurosMora, float prJurosMora,
			int qtIsencaoJurosMora, int tpProtesto, int nrDiasProtesto, int nrDiasBaixa,
			int tpCarteira, int tpCadastramento, int tpDesconto)
	{
		return Util.fill(nrBanco, 3, '0', 'E') + // Código do Banco na Compensação
		    	Util.fillNum(nrLote, 4) + // Lote Serviço
		    	"3" + // Tipo de Registro (0-H Arquivo,1-H Lote,2-Reg's Iniciais Lote
			      	  //	               3-Detalhe,4-Reg's Finais Lote,5-Tr Lote,9-Tr de Arquivo)
		    	Util.fillNum(nrSeqLote, 5)+ // Nº Sequencial do Registro no Lote
		    	"P"+ // Cód. Segmento do Registro Detalhe (P-Detalhe do Título)
		    	" "+ // Uso Exclusivo FEBRABAN/CNAB (1)
		        Util.fill(cdMovimento, 2, '0', 'E')+ // Código de Movimento Remessa (C004)
		        Util.fill(nrAgencia, 5, '0', 'D')+ // Agência Mantenedora da Conta (Num)
		        Util.fillAlpha(dvAgencia, 1)+ // Dígito verificador da agência
		        Util.fill(nrConta, 12, '0', 'D')+ // Número da Conta Corrente (Num)
		        Util.fillAlpha(dvConta, 1)+ // Dígito verificador da conta
		        Util.fillAlpha(dvAgenciaConta, 1)+ // Dígito verificador da agência/conta
		        Util.fillAlpha(nrNossoNumero, 20)+ // Identificação do Título no Banco
		        tpCarteira+ // Código da Carteira
		        tpCadastramento+ // Forma de Cadastramento do Título no Banco
		        tpDocumento+ // Tipo de Documento (1-Tradicional, 2-Escritural)
		        tpEmissaoBloqueto + // Identificação da Emissão do Bloqueto (2-Cliente Emite)
		        tpDistribuicao + // Identificação da Distribuição (2-Cliente Distribui)
		        Util.fillAlpha(nrDocumento, 15)+ // Número do Documento de Cobrança
		        (dtVencimento==null?"99999999":Util.formatDateTime(dtVencimento, "ddMMyyyy"))+ // Data de Vencimento do título (99999999-Contra Apresentação)
		        Util.fillNum(Math.round(vlConta * 100), 15)+ // Valor do título (2 casas decimais)
		        Util.fill("", 5, '0', 'D')+ // Agência Cobradora (Num) Obs: opcional, na ausência será substituida pelo CEP
		        Util.fillAlpha("", 1)+ // Dígito verificador da agência cobrador
		        Util.fillNum(tpEspecieTitulo, 2)+ // Espécie do Título
		        tpAceite+ // Tipo de Aceite (A-Aceito,N-Não Aceito)
		        Util.formatDateTime(dtEmissao, "ddMMyyyy")+ // Data de Emissão
		        tpJurosMora+ // Tipo de Juros de Mora (1-Valor por Dia,2-Taxa Mensal,3-Isento)
		        (qtIsencaoJurosMora==0?"00000000":Util.formatDateTime(dtJurosMora, "ddMMyyyy"))+ // Data de Vencimento do título (99999999-Contra Apresentação)
		        Util.fillNum(Math.round(prJurosMora * 100), 15)+ // Juros de Mora por Dia/Taxa
		        tpDesconto+ // Código do Desconto 1 (1-fixo até a data informada)
		        Util.formatDateTime(dtPagamentoComDesconto, "ddMMyyyy")+ // Data do Desconto 1
		        Util.fillNum(Math.round(vlDescontoAdimplencia * 100), 15)+ // Valor/Percentual a ser Concedido
		        Util.fillNum(Math.round(vlIOF * 100), 15)+ // Valor do IOF a ser Recolhido
		        Util.fillNum(Math.round(vlAbatimento * 100), 15)+ // Valor Abatimento
		        Util.fillNum(cdContaReceber, 25)+ // Identificação do Título na Empresa
		        tpProtesto + // Código para Protesto (3-Não Protestar)
		        Util.fillNum(nrDiasProtesto, 2)+ // Número de dias para protesto
		        tpBaixa + // Código para Baixa/Devolução (1-Baixar/Devolver,2-Não Baixar/Não  Devolver)
		        Util.fillNum(nrDiasBaixa, 3)+ // Número de Dias para Baixa/Devolução
		        "09"+ // Tipo de Moeda (Real)
		        Util.fillNum(nrContrato, 10)+ // Nº do Contrato da Operação de Créd.
		        " \n"; // Uso Exclusivo FEBRABAN/CNAB (1);
	}
	private static String getSegmentoQ(String nrBanco, int nrLote, int nrSeqLote,
			String cdMovimento, int tpInscSacado, String nrCpfCnpjSacado, String nmCliente,
			String dsEndereco, String nmBairro, String nrCep, String nmCidade, String sgUf,
			int tpInscAvalista, String nrCpfCnpjAvalista, String nmAvalista)
	{
		return Util.fill(nrBanco, 3, '0', 'E') + // Código do Banco na Compensação
		    	Util.fillNum(nrLote, 4) + // Lote Serviço
		    	"3"+ // Tipo de Registro (0-H Arquivo,1-H Lote,2-Reg's Iniciais Lote
			      	 //	               3-Detalhe,4-Reg's Finais Lote,5-Tr Lote,9-Tr de Arquivo)
		    	Util.fillNum(nrSeqLote, 5)+ // Nº Sequencial do Registro no Lote
		    	"Q"+ // Cód. Segmento do Registro Detalhe (P-Detalhe do Sacado)
		    	" "+ // Uso Exclusivo FEBRABAN/CNAB (1)
		        Util.fill(cdMovimento, 2, '0', 'E')+ // Código de Movimento Remessa (C004)
		        tpInscSacado+ // Tipo de Inscrição (0-Isento, 1-CPF, 2-CNPJ, 3-PIS/PASEP, 9-Outros)
		        Util.fill(nrCpfCnpjSacado, 15, '0', 'E')+ // Número de Inscrição do Sacado
		        Util.fillAlpha(nmCliente, 40)+ // Nome do Sacado
		        Util.fillAlpha(dsEndereco, 40)+ // Endereço do Sacado
		        Util.fillAlpha(nmBairro, 15)+ // Bairro
		        Util.fill(nrCep, 8, '0', 'E')+ // CEP
		        Util.fillAlpha(nmCidade, 15)+ // Nome do Sacado
		        Util.fillAlpha(sgUf, 2)+ // Endereço do Sacado
		        tpInscAvalista+ // Tipo de Inscrição (0-Isento, 1-CPF, 2-CNPJ, 3-PIS/PASEP, 9-Outros)
		        Util.fill(nrCpfCnpjAvalista, 15, '0', 'E')+ // Número de Inscrição do Sacado
		        Util.fillAlpha(nmAvalista, 40)+ // Nome do Sacador/Avalista
		        Util.fillAlpha("", 20)+ // Cód. Bco. Corresp. na Compensação
		        Util.fillAlpha("", 3)+ // Nosso Nº no Banco Correspondente
		        Util.fillAlpha("", 8)+
		        "\n"; // Uso Exclusivo FEBRABAN/CNAB
	}

	public static ResultSetMap[] processaCnab240(byte[] blbFile, String nmArquivo, int cdUsuario)	{
		Connection connect = Conexao.conectar();
		ResultSetMap rsmRetorno = new ResultSetMap();
		ResultSetMap rsmLog 	= new ResultSetMap();
		try	{
			// Lendo cabeçalho
			BufferedReader reader = blbFile==null ? null : new BufferedReader(new InputStreamReader(new ByteArrayInputStream(blbFile)));
			String txtLinha = null, nrConta="", nrDv="", nrRemessa="0";
			GregorianCalendar dtGeracao = new GregorianCalendar();
			HashMap<String,Object> register = new HashMap<String,Object>();
			if(reader!=null && (txtLinha = reader.readLine())!=null)	{
				String tpRegistro = txtLinha.substring(7, 8);
				//	Tipo de Registro (0-H Arquivo,1-H Lote,2-Reg's Iniciais Lote
          	  	//	                  3-Detalhe,4-Reg's Finais Lote,5-Tr Lote,9-Tr de Arquivo
				if(tpRegistro.equals("0"))	{ // Header
					register = new HashMap<String,Object>();
					register.put("TP_REGISTRO", tpRegistro);
					register.put("NR_BANCO", txtLinha.substring(0, 3));
					register.put("NR_CNPJ", txtLinha.substring(18, 32));
					register.put("NR_CONVENIO", txtLinha.substring(32, 52));
					register.put("NR_AGENCIA", txtLinha.substring(52, 57)+"-"+txtLinha.substring(57, 58));
					nrConta = txtLinha.substring(58, 70);
					if(((String)register.get("NR_BANCO")).equals("104"))
						nrConta = String.valueOf(Integer.parseInt(nrConta.substring(3)));
					else
						nrConta = String.valueOf(Integer.parseInt(nrConta));
					register.put("NR_CONTA", nrConta);
					nrDv = txtLinha.substring(70, 71).trim();
					register.put("NR_DV_CONTA", nrDv);
					register.put("NR_DV_AGENCIA_CONTA", txtLinha.substring(71, 72));
					register.put("NM_EMPRESA", txtLinha.substring(72, 102));
					register.put("NM_BANCO", txtLinha.substring(102, 132));
					register.put("TP_ARQUIVO", txtLinha.substring(142, 143));
					dtGeracao = Util.stringToCalendar(txtLinha.substring(143, 145)+"/"+txtLinha.substring(145, 147)+"/"+txtLinha.substring(147, 151));
					register.put("DT_GERACAO", dtGeracao);
					register.put("NR_REMESSA", txtLinha.substring(157, 163));
					nrRemessa = txtLinha.substring(157, 163);
					if(((String)register.get("NR_DV_CONTA")).trim().equals(""))	{
						nrDv = (String)register.get("NR_DV_AGENCIA_CONTA");
						register.put("NR_DV_CONTA", register.get("NR_DV_AGENCIA_CONTA"));
					}
				}
			}
			// Buscando conta
			int cdConta = 0, cdEmpresa = 0;
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_conta_financeira " +
					                         				   "WHERE nr_conta = ? "+
					                         				   "  AND nr_dv    = ?");
			pstmt.setString(1, nrConta);
			pstmt.setString(2, nrDv);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())	{
				cdConta   = rs.getInt("cd_conta");
				cdEmpresa = rs.getInt("cd_empresa");
			}
			else	{
				HashMap<String,Object> regLog = new HashMap<String,Object>();
				regLog.put("DS_MENSAGEM", "Conta bancária não localizada! ["+nrConta+"-"+nrDv+"]");
				regLog.put("CD_MENSAGEM", new Integer(-20));
				rsmLog.addRegister(regLog);
				// Grava as informações do cabeçalho do arquivo
				rsmRetorno.addRegister(register);
				reader.close();
				return new ResultSetMap[] {rsmRetorno, rsmLog};
			}
			// Salvando arquivo
			TipoArquivo tipoArqRetorno = TipoArquivoServices.getTipoArquivoById("CNAB_240_T", "CNAB 240 - Retorno", connect);
			ArquivoEdi arquivoEdi = new ArquivoEdi(0 /*cdArquivo*/, nmArquivo, new GregorianCalendar(), "CNAB 240 - Remessa "+nrRemessa,
												   blbFile,tipoArqRetorno.getCdTipoArquivo(), Integer.parseInt(nrRemessa), dtGeracao,
												   cdConta, 1 /*stArquivo*/);
			arquivoEdi.setCdArquivo(ArquivoEdiServices.insert(arquivoEdi, connect));
			register.put("CD_ARQUIVO", new Integer(arquivoEdi.getCdArquivo()));
			rsmRetorno.addRegister(register);
			if(arquivoEdi.getCdArquivo()<=0)	{
				HashMap<String,Object> regLog = new HashMap<String,Object>();
				regLog.put("DS_MENSAGEM", "Não foi possível salvar arquivo!");
				regLog.put("CD_MENSAGEM", new Integer(-30));
				rsmLog.addRegister(regLog);
				reader.close();
				return new ResultSetMap[] {rsmRetorno, rsmLog};
			}
			// Inicializando variáveis
			HashMap<GregorianCalendar,MovimentoConta> movimentos = new HashMap<GregorianCalendar,MovimentoConta>();
			int qtDetalhes = 0;
			String tpRegistro = null;
			while(reader!=null && (txtLinha = reader.readLine())!=null)	{
				if(txtLinha.trim().equals(""))
					continue;
				tpRegistro = txtLinha.substring(7, 8);
				//	Tipo de Registro (0-H Arquivo,1-H Lote,2-Reg's Iniciais Lote
          	  	//	                  3-Detalhe,4-Reg's Finais Lote,5-Tr Lote,9-Tr de Arquivo
				if(tpRegistro.equals("3"))	{ // Detalhes
					// Segmentos
					String idSegmento 	 = txtLinha.substring(13, 14);
					String cdMovimento 	 = txtLinha.substring(15, 17);

					if(idSegmento.equals("T"))	{ // Segmento T - Dados do Título
						String dsMovimento = "Desconhecido";
						for(int i=0; i<ArquivoTipoRegistroServices.cnab240T.length; i++)
							if(ArquivoTipoRegistroServices.cnab240T[i][0].equals(cdMovimento))
								dsMovimento = ArquivoTipoRegistroServices.cnab240T[i][0]+"-"+ArquivoTipoRegistroServices.cnab240T[i][1];
						register = new HashMap<String,Object>();
						register.put("TP_REGISTRO", new Integer(tpRegistro));
						register.put("ID_SEGMENTO", idSegmento);
						register.put("CD_MOVIMENTO", new Integer(cdMovimento));
						register.put("CL_MOVIMENTO", dsMovimento);
						register.put("NR_NOSSO_NUMERO", txtLinha.substring(37, 57).trim());
						register.put("NR_CARTEIRA", txtLinha.substring(57, 58).trim());
						register.put("DS_CARTEIRA", ContaCarteiraServices.tipoCarteira[Integer.parseInt(txtLinha.substring(57, 58).trim())]);
						register.put("NR_DOCUMENTO", txtLinha.substring(58, 73).trim());
						register.put("DT_VENCIMENTO", Util.stringToCalendar(txtLinha.substring(73, 75)+"/"+txtLinha.substring(75, 77)+"/"+txtLinha.substring(77, 81)));
						register.put("VL_TITULO", new Float(txtLinha.substring(81, 94)+"."+txtLinha.substring(94, 96)));
						register.put("ID_TITULO", txtLinha.substring(105, 130).trim());
						register.put("VL_TARIFA", new Float(txtLinha.substring(198, 211)+"."+txtLinha.substring(211, 213)));
						register.put("DS_MOTIVO", txtLinha.substring(213, 223).trim());
					}
					if(idSegmento.equals("U"))	{ // Segmento U - Dados do Pagamento
						register.put("VL_ACRESCIMO", new Float(txtLinha.substring(17, 30)+"."+txtLinha.substring(30, 32)));
						register.put("VL_DESCONTO", new Float(txtLinha.substring(32, 45)+"."+txtLinha.substring(45, 47)));
						register.put("VL_ABATIMENTO", new Float(txtLinha.substring(47, 60)+"."+txtLinha.substring(60, 62)));
						register.put("VL_IOF", new Float(txtLinha.substring(62, 75)+"."+txtLinha.substring(75, 77)));
						register.put("VL_PAGO", new Float(txtLinha.substring(77, 90)+"."+txtLinha.substring(90, 92)));
						register.put("VL_LIQUIDO", new Float(txtLinha.substring(92, 105)+"."+txtLinha.substring(105, 107)));
						register.put("VL_OUTRAS_DESPESAS", new Float(txtLinha.substring(107, 120)+"."+txtLinha.substring(120, 122)));
						register.put("VL_OUTROS_CREDITOS", new Float(txtLinha.substring(122, 135)+"."+txtLinha.substring(135, 137)));
						register.put("DT_OCORRENCIA", Util.stringToCalendar(txtLinha.substring(137, 139)+"/"+txtLinha.substring(139, 141)+"/"+txtLinha.substring(141, 145)));
						register.put("DT_CREDITO", Util.stringToCalendar(txtLinha.substring(145, 147)+"/"+txtLinha.substring(147, 149)+"/"+txtLinha.substring(149, 153)));
						rsmRetorno.addRegister(register);
						qtDetalhes++;
						rsmRetorno.last();
						HashMap<String,Object> regLog = processaBaixa(rsmRetorno, cdEmpresa, cdConta, cdUsuario, arquivoEdi.getCdArquivo(),
									                   				  	movimentos, connect);
						rsmLog.addRegister(regLog);
					}
				}
			}
			// TRAILER
			if(txtLinha!=null && tpRegistro!=null && tpRegistro.equals("9"))	{
				register = new HashMap<String,Object>();
				register.put("TP_REGISTRO", tpRegistro);
				register.put("QT_LOTES", txtLinha.substring(17, 23));
				register.put("QT_REGISTROS", new Integer(qtDetalhes));
				rsmRetorno.addRegister(register);
			}
			if(reader!=null)
				reader.close();
			return new ResultSetMap[] {rsmRetorno, rsmLog};
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BancoServices.processaCnab240: " +  e);
			// Log do erro
			HashMap<String,Object> regLog = new HashMap<String,Object>();
			regLog.put("DS_MENSAGEM", e.getMessage());
			regLog.put("CD_MENSAGEM", new Integer(-1));
			rsmLog.addRegister(regLog);
			return new ResultSetMap[] {null, rsmLog};
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap[] processaCnab400(byte[] blbFile, String nmArquivo, int cdUsuario)	{
		Connection connect = Conexao.conectar();
		ResultSetMap rsmRetorno = new ResultSetMap();
		ResultSetMap rsmLog 	= new ResultSetMap();
		try	{
			// Lendo cabeçalho
			BufferedReader reader = blbFile==null ? null : new BufferedReader(new InputStreamReader(new ByteArrayInputStream(blbFile)));
			String txtLinha = null, nrConta="", nrDv="", nrRemessa="0", nrAgencia="";
			int cdConta = 0, cdEmpresa = 0;
			GregorianCalendar dtGeracao = new GregorianCalendar();
			HashMap<String,Object> register = new HashMap<String,Object>();
			if(reader!=null && (txtLinha = reader.readLine())!=null)	{
				String tpRegistro = txtLinha.substring(0, 1);
				//	Tipo de Registro (0-H Arquivo,1-H Lote,2-Reg's Iniciais Lote
          	  	//	                  3-Detalhe,4-Reg's Finais Lote,5-Tr Lote,9-Tr de Arquivo
				if(tpRegistro.equals("0"))	{ // Header
					register = new HashMap<String,Object>();
					register.put("TP_REGISTRO", tpRegistro);
					register.put("NR_BANCO", txtLinha.substring(76, 78));
					register.put("NR_CNPJ", "");
					register.put("NR_CONVENIO", "");
					register.put("NR_AGENCIA", "");
					register.put("NR_DV_CONTA", nrDv);
					register.put("NR_DV_AGENCIA_CONTA", "");
					register.put("NM_EMPRESA", txtLinha.substring(46, 75));
					register.put("NM_BANCO", txtLinha.substring(79, 93));
					register.put("TP_ARQUIVO", "");
					dtGeracao = Util.stringToCalendar(txtLinha.substring(94, 96)+"/"+Integer.parseInt(txtLinha.substring(96, 98))+"/20"+txtLinha.substring(98, 100));
					register.put("DT_GERACAO", dtGeracao);
					register.put("NR_REMESSA", txtLinha.substring(394, 400));
					nrRemessa = txtLinha.substring(394, 399);
				}
			}
			HashMap<String, Object> registerHeader = register;
			ResultSetMap rsmHeader = new ResultSetMap();
			if (registerHeader!=null)
				rsmHeader.addRegister(registerHeader);
			rsmHeader.beforeFirst();

			// Inicializando variáveis
			HashMap<GregorianCalendar,MovimentoConta> movimentos = new HashMap<GregorianCalendar,MovimentoConta>();
			int qtDetalhes = 0;
			String tpRegistro = null;

			TipoArquivo tipoArqRetorno = TipoArquivoServices.getTipoArquivoById("CNAB_400", "CNAB 400 - Retorno", connect);
			ArquivoEdi arquivoEdi = new ArquivoEdi(0 /*cdArquivo*/, nmArquivo, new GregorianCalendar(), "CNAB 400 - Retorno "+nrRemessa,
												   blbFile,tipoArqRetorno.getCdTipoArquivo(), Integer.parseInt(nrRemessa), dtGeracao,
												   cdConta, 1 /*stArquivo*/);
			arquivoEdi.setCdArquivo(ArquivoEdiServices.insert(arquivoEdi, connect));
			register.put("CD_ARQUIVO", new Integer(arquivoEdi.getCdArquivo()));
			rsmRetorno.addRegister(register);
			if(arquivoEdi.getCdArquivo()<=0)	{
				HashMap<String,Object> regLog = new HashMap<String,Object>();
				regLog.put("DS_MENSAGEM", "Não foi possível salvar arquivo!");
				regLog.put("CD_MENSAGEM", new Integer(-30));
				rsmLog.addRegister(regLog);
				reader.close();
				return new ResultSetMap[] {rsmRetorno, rsmLog};
			}

			while(reader!=null && (txtLinha = reader.readLine())!=null)	{
				if(txtLinha.trim().equals(""))
					continue;
				tpRegistro = txtLinha.substring(0, 1);
				if (tpRegistro.equals("1")) {
					if (registerHeader!=null && nrConta.equals("")) {
						String nrIdentificacao = txtLinha.substring(20, 37);
						nrAgencia = nrIdentificacao.substring(4, 9);
						nrConta = nrIdentificacao.substring(9, 16);
						String nrContaOld = nrConta.substring(0);
						for (int i=0; i<nrConta.length(); i++)
							if (nrConta.charAt(i) == '0') {
								nrConta = nrConta.substring(1);
								i--;
							}
							else
								break;
						nrDv = nrIdentificacao.substring(16, 17);
						PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_conta_financeira " +
								                         				   "WHERE nr_conta LIKE ? "+
								                         				   "  AND nr_dv    = ?");
						pstmt.setString(1, "%" + nrConta);
						pstmt.setString(2, nrDv);
						ResultSet rs = pstmt.executeQuery();
						if(rs.next())	{
							cdConta   = rs.getInt("cd_conta");
							cdEmpresa = rs.getInt("cd_empresa");
							register.put("NR_CONVENIO", nrContaOld + nrDv);
							register.put("NR_AGENCIA", nrAgencia);
							register.put("NR_CONTA", nrConta);
							register.put("NR_DV_CONTA", nrDv);
						}
						else	{
							HashMap<String,Object> regLog = new HashMap<String,Object>();
							regLog.put("DS_MENSAGEM", "Conta bancária não localizada! ["+nrConta+"-"+nrDv+"]");
							regLog.put("CD_MENSAGEM", new Integer(-20));
							rsmLog.addRegister(regLog);
							// Grava as informações do cabeçalho do arquivo
							rsmRetorno.addRegister(register);
							return new ResultSetMap[] {rsmRetorno, rsmLog, rsmHeader};
						}
					}
					String dsMovimento = "Desconhecido";
					String cdMovimento 	 = txtLinha.substring(108, 110);
					for(int i=0; i<ArquivoTipoRegistroServices.cnab400T.length; i++)
						if(ArquivoTipoRegistroServices.cnab400T[i][0].equals(cdMovimento))
							dsMovimento = ArquivoTipoRegistroServices.cnab400T[i][0]+"-"+ArquivoTipoRegistroServices.cnab400T[i][1];
					register = new HashMap<String,Object>();
					register.put("TP_REGISTRO", new Integer(tpRegistro));
					register.put("CD_MOVIMENTO", new Integer(cdMovimento));
					register.put("CL_MOVIMENTO", dsMovimento);
					register.put("NR_NOSSO_NUMERO", txtLinha.substring(70, 82).trim());
					register.put("NR_CARTEIRA", txtLinha.substring(107, 108).trim());
					register.put("DS_CARTEIRA", "");
					register.put("NR_DOCUMENTO", txtLinha.substring(116, 126).trim());
					register.put("DT_VENCIMENTO", Util.stringToCalendar(txtLinha.substring(146, 148)+"/"+txtLinha.substring(148, 150)+"/20"+txtLinha.substring(150, 152)));
					register.put("VL_TITULO", new Float(txtLinha.substring(152, 163)+"."+txtLinha.substring(163, 165)));
					register.put("ID_TITULO", txtLinha.substring(70, 82).trim());
					register.put("VL_TARIFA", new Float(txtLinha.substring(279, 290)+"."+txtLinha.substring(290, 292)));
					register.put("DS_MOTIVO", "");
					register.put("VL_ACRESCIMO", new Float(txtLinha.substring(266, 277)+"."+txtLinha.substring(277, 279)));
					register.put("VL_DESCONTO", new Float(txtLinha.substring(240, 251)+"."+txtLinha.substring(251, 253)));
					register.put("VL_ABATIMENTO", new Float(txtLinha.substring(227, 238)+"."+txtLinha.substring(238, 240)));
					register.put("VL_IOF", new Float(txtLinha.substring(214, 225)+"."+txtLinha.substring(225, 227)));
					register.put("VL_PAGO", new Float(txtLinha.substring(253, 264)+"."+txtLinha.substring(264, 266)));
					register.put("VL_LIQUIDO", new Float(txtLinha.substring(253, 264)+"."+txtLinha.substring(264, 266)));
					register.put("VL_OUTRAS_DESPESAS", 0);
					register.put("VL_OUTROS_CREDITOS", new Float(txtLinha.substring(279, 290)+"."+txtLinha.substring(290, 292)));
					register.put("DT_OCORRENCIA", Util.stringToCalendar(txtLinha.substring(110, 112)+"/"+txtLinha.substring(112, 114)+"/20"+txtLinha.substring(114, 116)));
					register.put("DT_CREDITO", txtLinha.substring(295, 301).trim().equals("") ? null : Util.stringToCalendar(txtLinha.substring(295, 297)+"/"+txtLinha.substring(297, 299)+"/20"+txtLinha.substring(299, 301)));
					rsmRetorno.addRegister(register);
					qtDetalhes++;
					rsmRetorno.last();
					HashMap<String,Object> regLog = processaBaixa(rsmRetorno, cdEmpresa, cdConta, cdUsuario, arquivoEdi.getCdArquivo(),
           				  	movimentos, connect);
					rsmLog.addRegister(regLog);
				}
			}
			// TRAILER
			if(txtLinha!=null && tpRegistro!=null && tpRegistro.equals("9"))	{
				register = new HashMap<String,Object>();
				register.put("TP_REGISTRO", tpRegistro);
				register.put("QT_LOTES", txtLinha.substring(17, 23));
				register.put("QT_REGISTROS", new Integer(qtDetalhes));
				rsmRetorno.addRegister(register);
			}
			if(reader!=null)
				reader.close();
			return new ResultSetMap[] {rsmRetorno, rsmLog, rsmHeader};
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BancoServices.processaCnab240: " +  e);
			// Log do erro
			HashMap<String,Object> regLog = new HashMap<String,Object>();
			regLog.put("DS_MENSAGEM", e.getMessage());
			regLog.put("CD_MENSAGEM", new Integer(-1));
			rsmLog.addRegister(regLog);
			return new ResultSetMap[] {null, rsmLog};
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	private static HashMap<String,Object> processaBaixa(ResultSetMap rsm, int cdEmpresa, int cdConta, int cdUsuario,
			int cdArquivo, HashMap<GregorianCalendar, MovimentoConta> movimentos, Connection connect)
	{
		HashMap<String,Object> regLog = new HashMap<String,Object>();
		boolean isConnectionNull = connect==null;
		try	{
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			GregorianCalendar dtMovimento = (GregorianCalendar)rsm.getObject("DT_OCORRENCIA");

			int cdContaReceber = ContaReceberServices.getCdContaReceberOfNossoNumero(cdEmpresa, rsm.getString("NR_NOSSO_NUMERO"), connect);
			if(cdContaReceber <= 0)	{
				regLog.put("DS_MENSAGEM", "Boleto não localizado: "+rsm.getString("NR_NOSSO_NUMERO"));
				regLog.put("CD_MENSAGEM", new Integer(-30));
				if (isConnectionNull)
					Conexao.rollback(connect);
				return regLog;
			}
			System.out.println("Encontrei!" + rsm.getString("CL_MOVIMENTO", ""));

			if (rsm.getString("CL_MOVIMENTO", "").startsWith("06")) {
				// Buscando ou Criando lançamento na conta
				MovimentoConta movimentoConta = (MovimentoConta)movimentos.get(dtMovimento);
				float vlMovimento = rsm.getFloat("VL_LIQUIDO");
				if(movimentoConta != null)
					vlMovimento += movimentoConta.getVlMovimento();
				else	{
					movimentoConta = new MovimentoConta(0 /*cdMovimentoConta*/,
							cdConta,
							0 /*cdContaOrigem*/,
							0 /*cdMovimentoOrigem*/,
							cdUsuario,
							0 /*cdCheque*/,
							0 /*cdViagem*/,
							dtMovimento,
							vlMovimento,
							null /*nrDocumento*/,
							MovimentoContaServices.CREDITO,
							MovimentoContaServices.toPAGAMENTO /*tpOrigem - Pagamento*/,
							MovimentoContaServices.ST_NAO_COMPENSADO,
							"Crédito",
							null /*dtDeposito*/,
							null /*idExtrato*/,
							ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_DINHEIRO", 0),
							0 /*cdFechamento*/,0/*cdTurno*/);
					movimentoConta.setCdMovimentoConta(MovimentoContaDAO.insert(movimentoConta, connect));
				}
				if(movimentoConta.getCdMovimentoConta()<=0)	{
					if (isConnectionNull)
						Conexao.rollback(connect);
					regLog = new HashMap<String,Object>();
					regLog.put("DS_MENSAGEM", "Não foi possível lançar o crédito na conta!");
					regLog.put("CD_MENSAGEM", new Integer(-40));
					return regLog;
				}
				movimentos.put(dtMovimento, movimentoConta);
				// Verificando se já não existe pagamento
				PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
						                                           "FROM adm_movimento_conta_receber A, adm_movimento_conta B " +
						                                           "WHERE A.cd_conta         = B.cd_conta " +
						                                           "  AND B.dt_movimento     = ? " +
						                                           "  AND A.cd_conta_receber = "+cdContaReceber);
				pstmt.setTimestamp(1, new Timestamp(movimentoConta.getDtMovimento().getTimeInMillis()));
				if(pstmt.executeQuery().next())	{
					if (isConnectionNull)
						Conexao.rollback(connect);
					regLog = new HashMap<String,Object>();
					regLog.put("DS_MENSAGEM", "Lançamento da liquidação do boleto em duplicidade!");
					regLog.put("CD_MENSAGEM", new Integer(-40));
					return regLog;
				}
				// Registrando Liquidação no arquivo
				ArquivoTipoRegistro tipoRegistro = ArquivoTipoRegistroServices.getTipoRegistroById(ArquivoTipoRegistroServices.prefixCnab240T, "02", ArquivoTipoRegistroServices.cnab240T, connect);
				ArquivoRegistro registro = new ArquivoRegistro(0,cdArquivo,cdContaReceber,0/*cdTipoErro*/,tipoRegistro.getCdTipoRegistro(), 0/*cdContaPagar*/);
				/*registro.setCdRegistro(ArquivoRegistroServices.insert(registro, connect));
				if(registro.getCdRegistro()<=0)	{
					if (isConnectionNull)
						Conexao.rollback(connect);
					regLog = new HashMap<String,Object>();
					regLog.put("DS_MENSAGEM", "Não foi possível gravar registro da liquidação!");
					regLog.put("CD_MENSAGEM", new Integer(-40));
					return regLog;
				}*/
				// Lançando recebimento
				MovimentoContaReceber recebimento = new MovimentoContaReceber(cdConta,
																	movimentoConta.getCdMovimentoConta(),
																	cdContaReceber,
																	rsm.getFloat("VL_PAGO"),
																	rsm.getFloat("VL_ACRESCIMO"),
																	0 /*vlMulta*/,
																	rsm.getFloat("VL_ABATIMENTO")+rsm.getFloat("VL_DESCONTO"),
																	rsm.getFloat("VL_TARIFA"),
																	cdArquivo,
																	registro.getCdRegistro());
				int r = MovimentoContaReceberServices.insert(recebimento, dtMovimento, connect);
				if(r>0)	{
					if (isConnectionNull)
						connect.commit();
					regLog = new HashMap<String,Object>();
					regLog.put("DS_MENSAGEM", "Liquidação efetuada com sucesso!");
					regLog.put("CD_MENSAGEM", new Integer(0));
				}
				else	{
					if (isConnectionNull)
						Conexao.rollback(connect);
					regLog = new HashMap<String,Object>();
					regLog.put("DS_MENSAGEM", "Não foi possível lançar recebimento!");
					regLog.put("CD_MENSAGEM", new Integer(0));
				}
			}
			return regLog;
		}
		catch(Exception e)	{
			if (isConnectionNull)
				Conexao.rollback(connect);
			regLog = new HashMap<String,Object>();
			regLog.put("DS_MENSAGEM", e.getMessage());
			regLog.put("CD_MENSAGEM", new Integer(-40));
			e.printStackTrace(System.out);
			return regLog;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Banco getFromRsm(ResultSetMap rsm){
		try {
			return new Banco(rsm.getInt("cd_banco"),
							 rsm.getString("nr_banco"),
							 rsm.getString("nm_banco"),
							 rsm.getString("id_banco"),
							 rsm.getString("nm_url"));
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BancoServices.getFromRsm: " + e);
			return null;
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_banco ORDER BY nm_banco");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BancoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
