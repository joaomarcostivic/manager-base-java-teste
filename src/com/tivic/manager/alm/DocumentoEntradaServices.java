package com.tivic.manager.alm;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import com.tivic.manager.acd.Turma;
import com.tivic.manager.acd.TurmaDAO;
import com.tivic.manager.acd.TurmaServices;
import com.tivic.manager.adm.ContaPagar;
import com.tivic.manager.adm.ContaPagarCategoria;
import com.tivic.manager.adm.ContaPagarServices;
import com.tivic.manager.adm.EntradaEventoFinanceiro;
import com.tivic.manager.adm.EntradaEventoFinanceiroDAO;
import com.tivic.manager.adm.EntradaItemAliquota;
import com.tivic.manager.adm.EntradaItemAliquotaDAO;
import com.tivic.manager.adm.EntradaItemAliquotaServices;
import com.tivic.manager.adm.EntradaTributo;
import com.tivic.manager.adm.OrdemCompraServices;
import com.tivic.manager.adm.TabelaPreco;
import com.tivic.manager.adm.TabelaPrecoDAO;
import com.tivic.manager.adm.TabelaPrecoServices;
import com.tivic.manager.adm.TipoDocumentoDAO;
import com.tivic.manager.adm.Tributo;
import com.tivic.manager.adm.TributoAliquotaServices;
import com.tivic.manager.adm.TributoDAO;
import com.tivic.manager.adm.TributoServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.crt.PessoaServices;
import com.tivic.manager.fsc.NfeServices;
import com.tivic.manager.fsc.NotaFiscal;
import com.tivic.manager.fsc.NotaFiscalDAO;
import com.tivic.manager.fsc.NotaFiscalDocVinculadoDAO;
import com.tivic.manager.fsc.NotaFiscalDocVinculadoServices;
import com.tivic.manager.fsc.NotaFiscalServices;
import com.tivic.manager.fta.Rota;
import com.tivic.manager.fta.RotaDAO;
import com.tivic.manager.fta.TipoRota;
import com.tivic.manager.fta.TipoRotaDAO;
import com.tivic.manager.fta.Veiculo;
import com.tivic.manager.fta.VeiculoDAO;
import com.tivic.manager.fta.Viagem;
import com.tivic.manager.fta.ViagemDAO;
import com.tivic.manager.fta.ViagemServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.NumeracaoDocumentoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.ProdutoServico;
import com.tivic.manager.grl.ProdutoServicoDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresaServices;
import com.tivic.manager.grl.ProdutoServicoServices;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class DocumentoEntradaServices {
	/* Situações - Entrada */
	public static final int ST_EM_ABERTO = 0;
	public static final int ST_LIBERADO  = 1;
	public static final int ST_CANCELADO = 2;

	public static final String[] situacoes = {"Em aberto", "Liberado", "Cancelado"};

	/* tipos de entradas */
	public static final int ENT_COMPRA             = 0;
	public static final int ENT_CONSIGNACAO        = 1;
	public static final int ENT_TRANSFERENCIA 	   = 2;
	public static final int ENT_DEVOLUCAO          = 3;
	public static final int ENT_ACERTO_CONSIGNACAO = 4;
	public static final int ENT_AJUSTE             = 5;
	public static final int ENT_ENTRADA_DIRETA     = 6;

	public static String[] tiposEntrada = {"Compra","Consignação","Transferência","Devolução (do Cliente)","Acerto de Consignação","Ajuste", "Entrega Direta"};
	
	public static final int ENT_EXTA          = 0;
	public static final int ENT_DIRETA        = 1;
	public static final int ENT_INDIRETA 	  = 2;

	public static String[] tiposEntradaCae = {"Compra","Consignação","Transferência","Devolução (do Cliente)","Acerto de Consignação","Ajuste", "Entrega Direta"};

	/* tipo de documento */
	public static final int TP_NOTA_FISCAL     = 0;
	public static final int TP_CUPOM_FISCAL    = 1;
	public static final int TP_DOC_NAO_FISCAL  = 2;
	public static final int TP_NOTA_ELETRONICA = 3;

	public static String[] tiposDocumentoEntrada = {"Nota Fiscal", "Cupom Fiscal", "Não Fiscal", "NF-e"};

	/* tipo de movimento */
	public static final int MOV_ESTOQUE_NAO_CONSIGNADO = 0;
	public static final int MOV_ESTOQUE_CONSIGNADO     = 1;
	public static final int MOV_AMBOS_TIPO_ESTOQUE     = 2;
	public static final int MOV_NENHUM                 = 3;

	public static final String[] tiposMovimento = {"Normal","Consignado","Ambos","Nenhum"};

	/* tipos de fretes */
	public static final int FRT_CIF               = 0;
	public static final int FRT_FOB               = 1;
	public static final int FRT_FOB_TRANSPORTADOR = 2;

	public static String[] tiposFrete = {"Emitente", "Destinatário", "Destinatário (pago ao transp.)"};

	public static String[] tiposViasTransporte = {"Aéreo", "Ferroviário", "Hidroviário", "Marítimo", "Rodoviário"};

	/* codificação de erros retornados por rotinas relacionadas a documentos de entrada */
	public static final int ERR_EXISTENCIA_CONTAS_PAGAR  = -2;
	public static final int ERR_VALOR_TOTAL              = -3;
	public static final int ERR_FRETE_EMITENTE           = -4;
	public static final int ERR_VALOR_FRETE_INVALIDO     = -5;
	public static final int ERR_NOT_TIPO_DOCUMENTO_FRETE = -6;
	public static final int ERR_VALOR_FRETE_GERADO       = -7;
	public static final int ERR_QTD_ENTRADA_SUPERIOR     = -8;
	public static final int ERR_DOC_BALANCO              = -9;

	public static final int RET_SEM_RETORNO              = -100;
	
	public static Result saveEntrega(DocumentoEntrada documentoEntrada){
		return saveEntrega(documentoEntrada, 1, null);
	}
	
	public static Result saveEntrega(DocumentoEntrada documentoEntrada, int lgOrdemCompra){
		return saveEntrega(documentoEntrada, lgOrdemCompra, null);
	}
	
	public static Result saveEntrega(DocumentoEntrada documentoEntrada, int lgOrdemCompra, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(documentoEntrada==null)
				return new Result(-1, "Erro ao salvar. DocumentoEntrada é nulo");

			if(lgOrdemCompra>0) {
				ResultSetMap rsmOrdemCompra = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_ordem_compra WHERE cd_fornecedor = " + documentoEntrada.getCdFornecedor() + " AND (st_ordem_compra = " + OrdemCompraServices.ST_LIBERADA + " OR st_ordem_compra = " + OrdemCompraServices.ST_PENDENTE + ")").executeQuery());
				if(rsmOrdemCompra.size()==0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Nenhum contrato de compra (liberado ou pendente) identificado para esse fornecedor");
				}
				
				if(rsmOrdemCompra.next()){
					if(rsmOrdemCompra.getGregorianCalendar("dt_limite_entrega") != null && rsmOrdemCompra.getGregorianCalendar("dt_limite_entrega").before(documentoEntrada.getDtDocumentoEntrada())){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "A data limite de entrega do contrato foi ultrapassada");
					}
				}
			}
			
			if(documentoEntrada.getCdDocumentoEntrada() == 0){
				if(documentoEntrada.getTpEntrada() != ENT_ENTRADA_DIRETA)
					NumeracaoDocumentoServices.getProximoNumero("ENTREGA", new GregorianCalendar().get(Calendar.YEAR), documentoEntrada.getCdEmpresa(), connect);
				else 
					NumeracaoDocumentoServices.getProximoNumero("ENTREGA_DIRETA", new GregorianCalendar().get(Calendar.YEAR), documentoEntrada.getCdEmpresa(), connect);
			}
			
			Result resultado = save(documentoEntrada, connect);
			
			int retorno = resultado.getCode();
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?resultado.getMessage():"Salvo com sucesso...", "DOCUMENTOENTRADA", documentoEntrada);
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
	
	public static Result save(DocumentoEntrada documentoEntrada){
		return save(documentoEntrada, null);
	}

	public static Result save(DocumentoEntrada documentoEntrada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(documentoEntrada==null)
				return new Result(-1, "Erro ao salvar. DocumentoEntrada é nulo");

			int retorno;
			if(documentoEntrada.getCdDocumentoEntrada()==0){
				retorno = DocumentoEntradaDAO.insert(documentoEntrada, connect);
				documentoEntrada.setCdDocumentoEntrada(retorno);
			}
			else {
				retorno = DocumentoEntradaDAO.update(documentoEntrada, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "DOCUMENTOENTRADA", documentoEntrada);
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
	
	public static int copyDocumentoEntrada(int cdDocumentoEntrada, int cdEmpresa, int cdLocalArmazenamento) {
		return copyDocumentoEntrada(cdDocumentoEntrada, cdEmpresa, cdLocalArmazenamento, null);
	}

	public static int copyDocumentoEntrada(int cdDocumentoEntrada, int cdEmpresa, int cdLocalArmazenamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		Connection conOrigem = null;
		try {
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			conOrigem = connection;
			/* inclusao do novo documento de entrada */
			DocumentoEntrada documento = DocumentoEntradaDAO.get(cdDocumentoEntrada, conOrigem);
			int oldSituacaoDoc = documento.getStDocumentoEntrada();
			documento.setCdDocumentoEntrada(0);
			documento.setCdEmpresa(cdEmpresa);
			documento.setStDocumentoEntrada(ST_EM_ABERTO);
			int cdNewDocumentoEntrada = 0;
			if ((cdNewDocumentoEntrada = DocumentoEntradaDAO.insert(documento, connection)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			/* inclusao dos itens */
			ResultSetMap rsmItens = getAllItens(cdDocumentoEntrada, conOrigem);
			while (rsmItens.next()) {
				DocumentoEntradaItem item = new DocumentoEntradaItem(cdNewDocumentoEntrada, rsmItens.getInt("cd_produto_servico"), cdEmpresa,
						                                             rsmItens.getFloat("qt_entrada"), rsmItens.getFloat("vl_unitario"), rsmItens.getFloat("vl_acrescimo"),
						                                             rsmItens.getFloat("vl_desconto"), rsmItens.getInt("cd_unidade_medida"), rsmItens.getGregorianCalendar("dt_entrega_prevista"), 
						                                             0, 0, rsmItens.getInt("cd_item"), 0, 0, 0);
				if (DocumentoEntradaItemServices.insert(item, 0, false, connection).getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

				/* inclusao de aliquotas */
				ResultSetMap rsmAliquotas = DocumentoEntradaItemServices.getAllAliquotas(cdDocumentoEntrada, item.getCdProdutoServico(), connection);
				while (rsmAliquotas.next()) {
					EntradaItemAliquota aliquota = new EntradaItemAliquota(rsmAliquotas.getInt("cd_produto_servico"),
							cdNewDocumentoEntrada, cdEmpresa, rsmAliquotas.getInt("cd_tributo_aliquota"),
							rsmAliquotas.getInt("cd_tributo"), rsmAliquotas.getInt("cd_item"), rsmAliquotas.getFloat("pr_aliquota"), rsmAliquotas.getFloat("vl_base_calculo"));
					if (EntradaItemAliquotaDAO.insert(aliquota, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
			}

			/* executa a liberacao da entrada */
			if (oldSituacaoDoc == ST_LIBERADO) {
				int codeLib = liberarEntrada(cdNewDocumentoEntrada, cdLocalArmazenamento, connection).getCode();
				if (codeLib <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return codeLib;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap findEntrega(ArrayList<ItemComparator> criterios) {
		return findEntrega(criterios, null);
	}

	public static ResultSetMap findEntrega(ArrayList<ItemComparator> criterios, Connection connect) {
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		int qtLimite = 0;
		// correção devido a data salva no banco como timestamp completo, a comparação nunca encontra se não adicionar hora minuto e segundo
		for (ItemComparator itemComparator : criterios) {			
			if(itemComparator.getColumn().equals("dt_documento_entrada")){
				ItemComparator i = new ItemComparator();
				i.setColumn("dt_documento_entrada");
				i.setValue("'" + formatDate(itemComparator) + " 00:00:00'");
				i.setTypeComparation(ItemComparator.GREATER_EQUAL);
				crt.add(i);
				i = new ItemComparator();
				i.setColumn("dt_documento_entrada");
				i.setValue("'" + formatDate(itemComparator) + " 23:59:59'");
				i.setTypeComparation(ItemComparator.MINOR_EQUAL);
				crt.add(i);
				continue;
			}
			
			if (itemComparator.getColumn().equalsIgnoreCase("qtLimite"))
				qtLimite = Integer.parseInt(itemComparator.getValue());
			else
				crt.add(itemComparator);
		}
		String[] sqlLimt = Util.getLimitAndSkip(1, 0);
		try {
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
			ResultSetMap rsm = Search.find(
					"SELECT "+sqlLimit[0]+ 
					" A.*, B.nm_pessoa AS nm_fornecedor, B.nr_telefone1, A.tp_entrada " +
				    "FROM alm_documento_entrada A " +
				    "LEFT OUTER JOIN grl_pessoa B ON (A.cd_fornecedor = B.cd_pessoa) " +
				    "WHERE 1 = 1", "ORDER BY NR_DOCUMENTO_ENTRADA DESC "+sqlLimit[1], crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			while(rsm.next()){
				ResultSetMap rsmEndereco = PessoaEnderecoServices.getAllEnderecoByPessoa(rsm.getInt("CD_FORNECEDOR"), connect);
				
				if(rsmEndereco.size() > 0){
					HashMap<String, Object> map = rsmEndereco.getLines().get(0);
					
					for(Entry<String, Object> entry : map.entrySet()){
						rsm.setValueToField(entry.getKey(), entry.getValue());
						System.out.println(entry.getKey() + " -> " + entry.getValue());
					}
				}
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static ResultSetMap find(int cdEmpresa, ArrayList<ItemComparator> criterios) {
		criterios = criterios==null ? new ArrayList<ItemComparator>() : criterios;
		criterios.add(new ItemComparator("A.CD_EMPRESA", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		int qtLimite = 0;
		// correção devido a data salva no banco como timestamp completo, a comparação nunca encontra se não adicionar hora minuto e segundo
		for (ItemComparator itemComparator : criterios) {			
			if(itemComparator.getColumn().equals("dt_documento_entrada")){
				ItemComparator i = new ItemComparator();
				i.setColumn("dt_documento_entrada");
				i.setValue("'" + formatDate(itemComparator) + " 00:00:00'");
				i.setTypeComparation(ItemComparator.GREATER_EQUAL);
				crt.add(i);
				i = new ItemComparator();
				i.setColumn("dt_documento_entrada");
				i.setValue("'" + formatDate(itemComparator) + " 23:59:59'");
				i.setTypeComparation(ItemComparator.MINOR_EQUAL);
				crt.add(i);
				continue;
			}
			
			if (itemComparator.getColumn().equalsIgnoreCase("qtLimite"))
				qtLimite = Integer.parseInt(itemComparator.getValue());
			else
				crt.add(itemComparator);
		}
		String[] sqlLimt = Util.getLimitAndSkip(1, 0);
		String almEntradaDeclaracaoImportacao = "alm_entrada_declaracao_importacao";
		if( Util.getCurrentDbName() == "Firebird" )
			almEntradaDeclaracaoImportacao = "alm_entrada_declaracao_import";
		
		try {
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
			ResultSetMap rsm = Search.find(
					"SELECT "+sqlLimit[0]+" A.*, B.nm_pessoa AS nm_fornecedor, " +
					"       C.nm_pessoa AS nm_transportadora, E.nr_cnpj AS nr_cnpj_transportadora, " +
				    "       D.nm_natureza_operacao, D.nr_codigo_fiscal, G.nm_pessoa AS nm_digitador, " +
				    "      (SELECT SUM(E.qt_entrada * vl_unitario + vl_acrescimo - vl_desconto) FROM alm_documento_entrada_item E " +
				    "       WHERE E.cd_documento_entrada = A.cd_documento_entrada) AS vl_total_entrada, " +
				    "      (SELECT SUM(F.vl_desconto) " +
				    "       FROM alm_documento_entrada_item F " +
				    "       WHERE F.cd_documento_entrada = A.cd_documento_entrada) AS vl_total_descontos, " +
				    "      (SELECT SUM(G.vl_acrescimo) " +
				    "       FROM alm_documento_entrada_item G " +
				    "       WHERE G.cd_documento_entrada = A.cd_documento_entrada) AS vl_total_acrescimos, " +
				    "      (SELECT H.vl_base_calculo " +
				    "       FROM adm_entrada_tributo H " +
				    "       WHERE H.cd_documento_entrada = A.cd_documento_entrada AND H.cd_tributo = (SELECT "+sqlLimt[0]+" I.cd_tributo FROM adm_tributo I WHERE I.id_tributo = 'ICMS' "+sqlLimt[1]+" )) AS vl_base_calculo_icms, " +
				    "      (SELECT H.vl_tributo " +
				    "       FROM adm_entrada_tributo H " +
				    "       WHERE H.cd_documento_entrada = A.cd_documento_entrada AND H.cd_tributo = (SELECT "+sqlLimt[0]+" I.cd_tributo FROM adm_tributo I WHERE I.id_tributo = 'ICMS' "+sqlLimt[1]+" )) AS vl_icms, " +
				    "      (SELECT H.vl_retido " +
				    "       FROM adm_entrada_tributo H " +
				    "       WHERE H.cd_documento_entrada = A.cd_documento_entrada AND H.cd_tributo = (SELECT "+sqlLimt[0]+" I.cd_tributo FROM adm_tributo I WHERE I.id_tributo = 'ICMS' "+sqlLimt[1]+" )) AS vl_icms_substituto, " +
				    "      (SELECT H.vl_base_retencao " +
				    "       FROM adm_entrada_tributo H " +
				    "       WHERE H.cd_documento_entrada = A.cd_documento_entrada AND H.cd_tributo = (SELECT "+sqlLimt[0]+" I.cd_tributo FROM adm_tributo I WHERE I.id_tributo = 'ICMS' "+sqlLimt[1]+" )) AS vl_base_calculo_icms_substituto, " +
					"      (SELECT H.vl_tributo " +
					"       FROM adm_entrada_tributo H " +
					"       WHERE H.cd_documento_entrada = A.cd_documento_entrada AND H.cd_tributo = (SELECT "+sqlLimt[0]+" I.cd_tributo FROM adm_tributo I WHERE I.id_tributo = 'IPI' "+sqlLimt[1]+" )) AS vl_ipi," + 
					"       H.* " +
				    "FROM alm_documento_entrada A " +
				    "LEFT OUTER JOIN grl_pessoa B ON (A.cd_fornecedor = B.cd_pessoa) " +
				    "LEFT OUTER JOIN grl_pessoa C ON (A.cd_transportadora = C.cd_pessoa ) " +
				    "LEFT OUTER JOIN grl_pessoa_juridica E ON (C.cd_pessoa = E.cd_pessoa) " +
				    "LEFT OUTER JOIN adm_natureza_operacao D ON (A.cd_natureza_operacao = D.cd_natureza_operacao) " +
				    "LEFT OUTER JOIN seg_usuario F ON (A.cd_digitador = F.cd_usuario) " +
				    "LEFT OUTER JOIN grl_pessoa G ON (F.cd_pessoa = G.cd_pessoa) " +
				    "LEFT OUTER JOIN "+almEntradaDeclaracaoImportacao+" H ON (A.cd_documento_entrada = H.cd_documento_entrada) " +
				    //"LEFT OUTER JOIN adm_entrada_tributo H ON (H.cd_documento_entrada = A.cd_documento_entrada) " +
				    "WHERE 1 = 1", "ORDER BY DT_DOCUMENTO_ENTRADA DESC "+sqlLimit[1], crt, connect!=null ? connect : Conexao.conectar(), connect==null);
//			ArrayList<String> fields = new ArrayList<String>();
//			fields.add("dt_documento_entrada DESC");
//			rsm.orderBy(fields);
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	/**
	 * Metodo usado para busca dos documentos de entrada e itens para o DocumentoEntradaEscola
	 * Utiliza a estrutura de hierarquia, onde o rsm principal contem o fornecedor e cada um contem um rsm com o conjunto de produtos
	 * Não vem ordenado, não é possivel da forma como esta pois não da para colocar o codigo da instituição no find (por conta do agrupamento)
	 * @param criterios
	 * @return
	 */
	public static ResultSetMap findMerenda(ArrayList<ItemComparator> criterios) {
		return findMerenda(criterios, null);
	}

	@SuppressWarnings("unchecked")
	public static ResultSetMap findMerenda(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			int cdInstituicao = 0;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("CD_INSTITUICAO")) {
					cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
				}
			}
			
			ResultSetMap rsm = new ResultSetMap();
			
			PreparedStatement pstmt = connect.prepareStatement(
					"select A.cd_documento_entrada, A.cd_fornecedor, C.nm_pessoa as nm_fornecedor, D.cd_produto_servico as cd_produto, D.nm_produto_servico as nm_produto, B.qt_entrada, B.dt_entrega_prevista" +
					" from alm_documento_entrada A" +
					" join alm_documento_entrada_item B on (A.cd_documento_entrada = B.cd_documento_entrada)" +
					" left outer join grl_pessoa C on (A.cd_fornecedor = C.cd_pessoa)" +
					" left outer join grl_produto_servico D on (B.cd_produto_servico = D.cd_produto_servico)" +
					" where A.cd_empresa = " + cdInstituicao);
			
			//Hash usado para relacionar um fornecedor a um conjunto de produtos
			HashMap<Integer, HashMap<String, Object>> hashPrincipal = new HashMap<Integer, HashMap<String,Object>>();
			
			ResultSetMap rsmDocumentoEntrada = new ResultSetMap(pstmt.executeQuery());
			
			while(rsmDocumentoEntrada.next()){
				
				//Faz a inserção inicial do fornecedor no hashPrincipal
				if(!hashPrincipal.containsKey(rsmDocumentoEntrada.getInt("CD_FORNECEDOR"))){
					HashMap<String, Object> registerFornecedor = new HashMap<String, Object>();
					registerFornecedor.put("CD_FORNECEDOR", rsmDocumentoEntrada.getInt("CD_FORNECEDOR"));
					registerFornecedor.put("NM_FORNECEDOR", rsmDocumentoEntrada.getString("NM_FORNECEDOR"));
					registerFornecedor.put("NM_IDENTIFICADOR", rsmDocumentoEntrada.getString("NM_FORNECEDOR"));
					registerFornecedor.put("CD_DOCUMENTO_ENTRADA", rsmDocumentoEntrada.getInt("CD_DOCUMENTO_ENTRADA"));
					registerFornecedor.put("PRODUTOS", new ResultSetMap().getLines());
					
					hashPrincipal.put(rsmDocumentoEntrada.getInt("CD_FORNECEDOR"), registerFornecedor);
				}
				
				//Utiliza o hash daquela instituição (com o bloco anterior, é garantido que sempre haverá um hash correspondente)
				HashMap<String, Object> registerFornecedor = hashPrincipal.get(rsmDocumentoEntrada.getInt("CD_FORNECEDOR"));
				
				HashMap<String, Object> registerProduto = new HashMap<String, Object>();
				
				registerProduto.put("CD_PRODUTO", rsmDocumentoEntrada.getInt("CD_PRODUTO"));
				registerProduto.put("NM_PRODUTO", rsmDocumentoEntrada.getString("NM_PRODUTO"));
				registerProduto.put("NM_IDENTIFICADOR", rsmDocumentoEntrada.getString("NM_PRODUTO"));
				registerProduto.put("QT_ENTRADA", rsmDocumentoEntrada.getInt("QT_ENTRADA"));
				registerProduto.put("DT_ENTREGA_PREVISTA", rsmDocumentoEntrada.getDateFormat("DT_ENTREGA_PREVISTA", "dd/MM/yyyy"));
				registerProduto.put("CD_DOCUMENTO_ENTRADA", rsmDocumentoEntrada.getInt("CD_DOCUMENTO_ENTRADA"));
				
				ArrayList<HashMap<String, Object>> produtos = (ArrayList<HashMap<String, Object>>)registerFornecedor.get("PRODUTOS");
				produtos.add(registerProduto);
				
				registerFornecedor.put("PRODUTOS", produtos);
			}
			
			for(Integer cdDocumentoEntradaHash : hashPrincipal.keySet()){
				rsm.addRegister(hashPrincipal.get(cdDocumentoEntradaHash));
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
	
	/**
	 * Método para converter o formato nacional de datas para o formato sql de datas 
	 * @param
	 * @return todas as entradas diretas da semana nas escolas
	 */
	public static ResultSetMap getEntradaDiretaPod() {
		return getEntradaDiretaPod(null);
	}

	public static ResultSetMap getEntradaDiretaPod(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_pessoa as nm_instituicao, C.nm_pessoa as nm_fornecedor FROM alm_documento_entrada A" +
											" JOIN grl_pessoa B ON (A.cd_empresa = B.cd_pessoa)" +
											" left outer join grl_pessoa C on (A.cd_fornecedor = C.cd_pessoa)");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioServices.getEntradaDiretaPod: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioServices.getEntradaDiretaPod: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método para converter o formato nacional de datas para o formato sql de datas
	 * 
	 * @author Luiz Romario Filho
	 * @param itemComparator
	 * @return
	 */
	private static String formatDate(ItemComparator itemComparator) {
		String value = itemComparator.getValue();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = format.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		return newFormat.format(date);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupByFields,
			ArrayList<String> orderByFields) {
		return findCompleto(criterios, groupByFields, orderByFields, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupByFields,
			ArrayList<String> orderByFields, Connection connection) {
		
		int cdTributoICMS        = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0, 0, connection);
		String fields = "A.cd_documento_entrada, A.cd_transportadora, A.cd_empresa, A.cd_fornecedor, A.dt_documento_entrada, " +
						"A.st_documento_entrada, A.nr_documento_entrada, A.tp_documento_entrada, A.tp_entrada, A.tp_frete, tp_movimento_estoque, " +
						"A.cd_digitador, K.nm_razao_social AS nm_empresa, L.nr_codigo_fiscal, A.vl_total_documento, A.vl_acrescimo, A.vl_desconto, A.vl_frete, A.vl_seguro, " +
						"H.nm_pessoa AS nm_fornecedor, O.nm_pessoa AS nm_digitador, J.nm_pessoa AS nm_transportadora "+
						(groupByFields.size()==0 ? ", (SELECT SUM(vl_base_calculo * pr_aliquota / 100) FROM adm_entrada_item_aliquota EIA " +
								                   "   WHERE EIA.cd_documento_entrada = A.cd_documento_entrada" +
								                   "     AND EIA.cd_tributo           = "+cdTributoICMS+") AS vl_icms ": "");
		String groups = "";
		String [] retorno = Util.getFieldsAndGroupBy(groupByFields, fields, groups,
					        "SUM(A.vl_total_documento + A.vl_acrescimo - A.vl_desconto) AS vl_documento");
		fields = retorno[0];
		groups = retorno[1];

		ResultSetMap rsm = Search.find("SELECT " + fields + " " +
									   "FROM alm_documento_entrada  A " +
									   "LEFT OUTER JOIN grl_pessoa  H ON (A.cd_fornecedor     = H.cd_pessoa) " +
									   "LEFT OUTER JOIN seg_usuario I ON (A.cd_digitador      = I.cd_usuario) " +
									   "LEFT OUTER JOIN grl_pessoa  O ON (I.cd_pessoa         = O.cd_pessoa) " +
									   "LEFT OUTER JOIN grl_pessoa  J ON (A.cd_transportadora = J.cd_pessoa) " +
									   "LEFT OUTER JOIN grl_empresa N ON (A.cd_empresa        = N.cd_empresa) " +
									   "JOIN grl_pessoa_juridica    K ON (N.cd_empresa        = K.cd_pessoa) " +
									   "JOIN adm_natureza_operacao  L ON (A.cd_natureza_operacao = L.cd_natureza_operacao) " +
									   "WHERE 1 = 1 ", groups, criterios,
									    connection!=null ? connection : Conexao.conectar(), connection==null);
		if (orderByFields != null && orderByFields.size()>0) {
			rsm.orderBy(orderByFields);
		}
		return rsm;
	}

	public static ResultSetMap getAllItens(int cdDocumentoEntrada) {
		return getAllItens(cdDocumentoEntrada, null);
	}
	
	public static ResultSetMap getAllItens(int cdDocumentoEntrada, boolean hasTributos) {
		return getAllItens(cdDocumentoEntrada, hasTributos, null);
	}

	public static ResultSetMap getAllItens(int cdDocumentoEntrada, Connection connect) {
		return getAllItens(cdDocumentoEntrada, false, connect);
	}
	public static ResultSetMap getAllItens(int cdDocumentoEntrada, boolean hasTributos, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(cdDocumentoEntrada, connect);
			int cdTabelaPreco = 0;
			
			int cdTributoICMS    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0, 0, connect);
			int cdTributoIPI 	 = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_IPI", 0, 0, connect);
			int cdTributoII      = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_II", 0, 0, connect);
			int cdTributoPIS 	 = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0, 0, connect);
			int cdTributoCOFINS  = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0, 0, connect);
			int cdTipoOperacaoVarejo = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, docEntrada.getCdEmpresa(), connect);
			if(cdTipoOperacaoVarejo>0)	{
				com.tivic.manager.adm.TipoOperacao tipoOperacao = com.tivic.manager.adm.TipoOperacaoDAO.get(cdTipoOperacaoVarejo, connect);
				cdTabelaPreco                                   = tipoOperacao!=null ? tipoOperacao.getCdTabelaPreco() : 0;
			}
			
			pstmt = connect.prepareStatement(
					 "SELECT A.*, G.sg_unidade_medida, G.nr_precisao_medida, C.nm_produto_servico, L.cd_ncm, L.nr_ncm, C.tp_produto_servico, txt_especificacao, txt_dado_tecnico, " +
					 "       H.nm_pessoa AS nm_fabricante, I.vl_preco, C.nr_referencia," +
					 "       C.id_produto_servico, " +
					 /* somando impostos diretos*/
					 "       (SELECT SUM(H.vl_base_calculo * pr_aliquota / 100) FROM adm_entrada_item_aliquota H " +
					 "		  WHERE H.cd_produto_servico = A.cd_produto_servico " +
					 "		    AND H.cd_documento_entrada = A.cd_documento_entrada " +
					 "			AND cd_empresa = A.cd_empresa) AS vl_tributos, " +
					 "       (SELECT SUM(H.vl_base_calculo * pr_aliquota / 100) " +
					 "		  FROM adm_entrada_item_aliquota H, adm_tributo H1 " +
					 "		  WHERE H.cd_tributo           = H1.cd_tributo " +
					 "          AND H1.tp_cobranca         = "+TributoServices._INCIDENCIA_DIRETA+
					 "          AND H.cd_produto_servico   = A.cd_produto_servico " +
					 "			AND H.cd_documento_entrada = A.cd_documento_entrada " +
					 "			AND cd_empresa             = A.cd_empresa) AS vl_tributos_direto, "+
					 "     C.sg_produto_servico, B.st_produto_empresa, C.cd_classificacao_fiscal, E.nm_classificacao_fiscal, " +
					 "     G.nm_unidade_medida, B.id_reduzido, N.nr_codigo_fiscal, " +
					 " 		P.cd_natureza_operacao AS cd_natureza_operacao_item, P.nm_natureza_operacao AS nm_natureza_operacao_item, " +
					 "		P.nr_codigo_fiscal AS nr_codigo_fiscal_item, CEI.qt_recebida, CEI.qt_recebida AS qt_comprada, CEI.cd_ordem_compra " + 
					 "FROM alm_documento_entrada_item A " +
					 "JOIN grl_produto_servico_empresa         B ON (A.cd_produto_servico = B.cd_produto_servico AND A.cd_empresa = B.cd_empresa) " +
					 "JOIN grl_produto_servico                 C ON (B.cd_produto_servico = C.cd_produto_servico) " +
					 "LEFT OUTER JOIN grl_produto              D ON (C.cd_produto_servico = D.cd_produto_servico) " +
					 "LEFT OUTER JOIN adm_classificacao_fiscal E ON (C.cd_classificacao_fiscal = E.cd_classificacao_fiscal) " +
					 "LEFT OUTER JOIN grl_unidade_medida       G ON (A.cd_unidade_medida = G.cd_unidade_medida) " +
					 "LEFT OUTER JOIN grl_pessoa               H ON (C.cd_fabricante = H.cd_pessoa) " +
					 "LEFT OUTER JOIN adm_produto_servico_preco I ON (A.cd_produto_servico = I.cd_produto_servico " +
				     "                                            AND I.cd_tabela_preco    = " +cdTabelaPreco+
				     "                                            AND I.dt_termino_validade IS NULL) "+	
				     "LEFT OUTER JOIN grl_ncm                   L ON (C.cd_ncm = L.cd_ncm) " +
					 "LEFT OUTER JOIN alm_documento_entrada     M ON (M.cd_documento_entrada = A.cd_documento_entrada) " +
					 "LEFT OUTER JOIN adm_natureza_operacao     N ON (N.cd_natureza_operacao = M.cd_natureza_operacao) " +
					 "LEFT OUTER JOIN adm_natureza_operacao     P ON (P.cd_natureza_operacao = A.cd_natureza_operacao) " +
					 "LEFT OUTER JOIN adm_compra_entrada_item   CEI ON (A.cd_documento_entrada = CEI.cd_documento_entrada"+
					 "												  AND A.cd_item = CEI.cd_item"+
					 "												  AND A.cd_produto_servico = CEI.cd_produto_servico"+
					 "												  AND A.cd_empresa = CEI.cd_empresa) " +
				     " WHERE A.cd_documento_entrada = "+cdDocumentoEntrada);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()) {
				String dsLocal = "";
				ResultSetMap rsmLocais = DocumentoEntradaItemServices.getAllLocaisArmazenamento(cdDocumentoEntrada, rsm.getInt("cd_produto_servico"), rsm.getInt("cdEmpresa"), connect);
				while(rsmLocais.next()) {
					String qtEntrada = new DecimalFormat("#,##0.0000", new java.text.DecimalFormatSymbols(new java.util.Locale("pt", "BR"))).format(rsmLocais.getFloat("qt_entrada")); 
					dsLocal += (dsLocal.equals("") ? "" : ", ") +rsmLocais.getString("nm_local_armazenamento")+(rsmLocais.size()>1 ? ": "+qtEntrada : "");
				}
				rsm.setValueToField("NM_LOCAL_ARMAZENAMENTO", dsLocal);
				String stTributaria = "000"; // TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL 
				switch(rsm.getInt("st_tributaria")) {
					case TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA:
						stTributaria = "010"; 
						break;  
					case TributoAliquotaServices.ST_SEM_TRIBUTACAO:
						stTributaria = "041"; 
						break;  
					case TributoAliquotaServices.ST_ISENTO:
						stTributaria = "040"; 
						break;  
				}
				rsm.setValueToField("DS_CST", stTributaria);
				
				//Verificar se o produto do item esta tributado com o Imposto sobre Importacao
				ResultSetMap rsmTributos = ProdutoServicoServices.getAllTributos(rsm.getInt("cd_produto_servico"), connect);
				boolean hasII = false;
				while(rsmTributos.next())
					if(rsmTributos.getString("id_tributo").equals("II")){
						hasII = true;
						break;
					}
				if(hasII)
					rsm.setValueToField("hasII", 1);
				else
					rsm.setValueToField("hasII", 0);
				
				rsm.setValueToField("VL_UNITARIO", (Util.roundFloat(rsm.getFloat("VL_UNITARIO"), (rsm.getInt("qt_precisao_custo") > 0 ? rsm.getInt("qt_precisao_custo") : 2))));
				rsm.setValueToField("QT_ENTRADA", (Util.roundFloat(rsm.getFloat("QT_ENTRADA"), (rsm.getInt("nr_precisao_medida") > 0 ? rsm.getInt("nr_precisao_medida") : 2))));
				rsm.setValueToField("VL_ACRESCIMO", (Util.roundFloat(rsm.getFloat("VL_ACRESCIMO"), (rsm.getInt("qt_precisao_custo") > 0 ? rsm.getInt("qt_precisao_custo") : 2))));
				rsm.setValueToField("VL_DESCONTO", (Util.roundFloat(rsm.getFloat("VL_DESCONTO"), (rsm.getInt("qt_precisao_custo") > 0 ? rsm.getInt("qt_precisao_custo") : 2))));
				rsm.setValueToField("VL_TOTAL", Util.roundFloat(rsm.getFloat("VL_UNITARIO") * rsm.getFloat("QT_ENTRADA"), (rsm.getInt("qt_precisao_custo") > 0 ? rsm.getInt("qt_precisao_custo") : 2)));
				rsm.setValueToField("VL_LIQUIDO", Util.roundFloat(rsm.getFloat("VL_UNITARIO") * rsm.getFloat("QT_ENTRADA") + rsm.getFloat("VL_ACRESCIMO") - rsm.getFloat("VL_DESCONTO"), (rsm.getInt("qt_precisao_custo") > 0 ? rsm.getInt("qt_precisao_custo") : 2)));
				
			}
			rsm.beforeFirst();
			while(rsm.next() && hasTributos){
				//Total Tributos
				ResultSetMap rsm2 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_total_tributos, SUM(I.vl_base_calculo) AS vl_total_base_calculo FROM adm_entrada_item_aliquota I " +
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "			AND I.cd_produto_servico = " +rsm.getInt("cd_produto_servico") +
																		 "          AND I.cd_documento_entrada = " +rsm.getInt("cd_documento_entrada") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa")).executeQuery());
				while(rsm2.next()){
					rsm.setValueToField("vl_total_tributos", rsm2.getFloat("vl_total_tributos"));
					rsm.setValueToField("vl_total_base_calculo", rsm2.getFloat("vl_total_base_calculo"));
				}
				
				//Total Tributos do Documento
				ResultSetMap rsm3 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_total_tributos_documento, SUM(I.vl_base_calculo) AS vl_total_base_calculo_documento FROM adm_entrada_item_aliquota I " +
																		 "        WHERE I.cd_documento_entrada = " +rsm.getInt("cd_documento_entrada") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa")).executeQuery());
				while(rsm3.next()){
					rsm.setValueToField("vl_total_tributos_documento", rsm3.getFloat("vl_total_tributos_documento"));
					rsm.setValueToField("vl_total_base_calculo_documento", rsm3.getFloat("vl_total_base_calculo_documento"));
				}
				
				//Total Tributo ICMS
				PreparedStatement pstmt4 = connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, I.pr_aliquota, SUM(I.vl_base_calculo) AS vl_base_icms, P.st_tributaria, Q.cd_situacao_tributaria, Q.nm_situacao_tributaria, Q.nr_situacao_tributaria, Q.lg_substituicao FROM adm_entrada_item_aliquota I " +
						 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
						 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
						 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " + 
						 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
						 "			AND I.cd_produto_servico = " +rsm.getInt("cd_produto_servico") +
						 "          AND I.cd_documento_entrada = " +rsm.getInt("cd_documento_entrada") +
						 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
						 "          AND P.cd_tributo           = "+cdTributoICMS + 
						 "		  GROUP BY P.st_tributaria, Q.cd_situacao_tributaria, Q.nm_situacao_tributaria, Q.nr_situacao_tributaria, I.pr_aliquota, Q.lg_substituicao");
				
				ResultSetMap rsm4 = new ResultSetMap(pstmt4.executeQuery());
				
				if(rsm4.next()){
					rsm.setValueToField("vl_icms", rsm4.getFloat("vl_icms"));
					rsm.setValueToField("vl_base_icms", rsm4.getFloat("vl_base_icms"));
					rsm.setValueToField("pr_icms", rsm4.getFloat("pr_aliquota"));
					rsm.setValueToField("st_tributaria_icms", rsm4.getInt("st_tributaria"));
					rsm.setValueToField("lg_substituicao_icms", rsm4.getInt("lg_substituicao"));
					rsm.setValueToField("cd_tributo_icms", cdTributoICMS);
					rsm.setValueToField("cd_situacao_tributaria_icms", rsm4.getString("cd_situacao_tributaria"));
					rsm.setValueToField("nm_situacao_tributaria_icms", rsm4.getString("nm_situacao_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria_icms", rsm4.getString("nr_situacao_tributaria"));
				}
				else{
					rsm.setValueToField("vl_icms", 0);
					rsm.setValueToField("vl_base_icms", 0);
					rsm.setValueToField("pr_icms", 0);
					rsm.setValueToField("st_tributaria", 0);
					rsm.setValueToField("cd_tributo_icms", 0);
					rsm.setValueToField("cd_situacao_tributaria_icms", 0);
					rsm.setValueToField("nm_situacao_tributaria_icms", "");
					rsm.setValueToField("nr_situacao_tributaria", "");
					rsm.setValueToField("nr_situacao_tributaria_icms", "");
				}
				if(rsm4.next()){
					rsm.setValueToField("ErroAliq", 1);
				}
				
				//Total Tributo ICMS Documento
				ResultSetMap rsm5 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, SUM(I.vl_base_calculo) AS vl_base_icms, P.st_tributaria, Q.nr_situacao_tributaria FROM adm_entrada_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_documento_entrada = " +rsm.getInt("cd_documento_entrada") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "          AND P.cd_tributo           = "+cdTributoICMS + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria").executeQuery());
				
				while(rsm5.next()){
					if(rsm5.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
						rsm.setValueToField("vl_icms_documento", rsm5.getFloat("vl_icms"));
						rsm.setValueToField("vl_base_icms_documento", rsm5.getFloat("vl_base_icms"));
						rsm.setValueToField("st_tributaria_documento", rsm5.getInt("st_tributaria"));
					}
					if(rsm5.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
						rsm.setValueToField("vl_icms_substituto_documento", rsm5.getFloat("vl_icms"));
						rsm.setValueToField("vl_base_icms_substituto_documento", rsm5.getFloat("vl_base_icms"));
						rsm.setValueToField("st_tributaria_substituto_documento", rsm5.getInt("st_tributaria"));
					}
				}
				
				//Total Tributo IPI
				ResultSetMap rsm6 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_ipi, I.pr_aliquota, SUM(I.vl_base_calculo) AS vl_base_ipi, P.st_tributaria, Q.cd_situacao_tributaria, Q.nm_situacao_tributaria, Q.nr_situacao_tributaria FROM adm_entrada_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "			AND I.cd_produto_servico = " +rsm.getInt("cd_produto_servico") +
																		 "          AND I.cd_documento_entrada = " +rsm.getInt("cd_documento_entrada") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "          AND P.cd_tributo           = "+cdTributoIPI + 
																		 "		  GROUP BY P.st_tributaria, Q.cd_situacao_tributaria, Q.nm_situacao_tributaria, Q.nr_situacao_tributaria, I.pr_aliquota").executeQuery());
				if(rsm6.next()){
					rsm.setValueToField("vl_ipi", rsm6.getFloat("vl_ipi"));
					rsm.setValueToField("vl_base_ipi", rsm6.getFloat("vl_base_ipi"));
					rsm.setValueToField("pr_ipi", rsm6.getFloat("pr_aliquota"));
					rsm.setValueToField("st_tributaria", rsm6.getInt("st_tributaria"));
					rsm.setValueToField("cd_tributo_ipi", cdTributoIPI);
					rsm.setValueToField("cd_situacao_tributaria_ipi", rsm6.getString("cd_situacao_tributaria"));
					rsm.setValueToField("nm_situacao_tributaria_ipi", rsm6.getString("nm_situacao_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria", rsm6.getString("nr_situacao_tributaria"));
				}
				else{
					rsm.setValueToField("vl_ipi", 0);
					rsm.setValueToField("vl_base_ipi", 0);
					rsm.setValueToField("pr_ipi", 0);
					rsm.setValueToField("st_tributaria", 0);
					rsm.setValueToField("cd_tributo_ipi", 0);
					rsm.setValueToField("cd_situacao_tributaria_ipi", 0);
					rsm.setValueToField("nm_situacao_tributaria_ipi", "");
					rsm.setValueToField("nr_situacao_tributaria", "");
				}
				if(rsm6.next()){
					rsm.setValueToField("ErroAliq", 2);
				}
				
				//Total Tributo IPI do Documento
				ResultSetMap rsm7 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_ipi, SUM(I.vl_base_calculo) AS vl_base_ipi, P.st_tributaria, Q.nr_situacao_tributaria FROM adm_entrada_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_documento_entrada = " +rsm.getInt("cd_documento_entrada") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "			AND P.cd_tributo           = "+cdTributoIPI + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria").executeQuery());
				while(rsm7.next()){
					if(rsm7.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
						rsm.setValueToField("vl_ipi_documento", rsm7.getFloat("vl_ipi"));
						rsm.setValueToField("vl_base_ipi_documento", rsm7.getFloat("vl_base_ipi"));
						rsm.setValueToField("st_tributaria_documento", rsm7.getInt("st_tributaria"));
					}
					if(rsm7.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
						rsm.setValueToField("vl_ipi_substituto_documento", rsm7.getFloat("vl_ipi"));
						rsm.setValueToField("vl_base_ipi_substituto_documento", rsm7.getFloat("vl_base_ipi"));
						rsm.setValueToField("st_tributaria_substituto_documento", rsm7.getInt("st_tributaria"));
					}
				}
				
				//Total Tributo II
				ResultSetMap rsm8 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_ii,I.pr_aliquota, SUM(I.vl_base_calculo) AS vl_base_ii, P.st_tributaria, Q.cd_situacao_tributaria, Q.nm_situacao_tributaria, Q.nr_situacao_tributaria FROM adm_entrada_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "			AND I.cd_produto_servico = " +rsm.getInt("cd_produto_servico") +
																		 "          AND I.cd_documento_entrada = " +rsm.getInt("cd_documento_entrada") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "          AND P.cd_tributo           = "+cdTributoII + 
																		 "		  GROUP BY P.st_tributaria, Q.cd_situacao_tributaria, Q.nm_situacao_tributaria, Q.nr_situacao_tributaria,I.pr_aliquota").executeQuery());
				if(rsm8.next()){
					rsm.setValueToField("vl_ii", rsm8.getFloat("vl_ii"));
					rsm.setValueToField("vl_base_ii", rsm8.getFloat("vl_base_ii"));
					rsm.setValueToField("pr_ii", rsm8.getFloat("pr_aliquota"));
					rsm.setValueToField("st_tributaria", rsm8.getInt("st_tributaria"));
					rsm.setValueToField("cd_tributo_ii", cdTributoII);
					rsm.setValueToField("cd_situacao_tributaria_ii", rsm8.getString("cd_situacao_tributaria"));
					rsm.setValueToField("nm_situacao_tributaria_ii", rsm8.getString("nm_situacao_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria", rsm8.getString("nr_situacao_tributaria"));
				}
				else{
					rsm.setValueToField("vl_ii", 0);
					rsm.setValueToField("vl_base_ii", 0);
					rsm.setValueToField("pr_ii", 0);
					rsm.setValueToField("st_tributaria", 0);
					rsm.setValueToField("cd_tributo_ii", 0);
					rsm.setValueToField("cd_situacao_tributaria_ii", 0);
					rsm.setValueToField("nm_situacao_tributaria_ii", "");
					rsm.setValueToField("nr_situacao_tributaria", "");
				}
				if(rsm8.next()){
					rsm.setValueToField("ErroAliq", 3);
				}
				
				//Total Tributo II do Documento
				ResultSetMap rsm9 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_ii, SUM(I.vl_base_calculo) AS vl_base_ii, P.st_tributaria, Q.nr_situacao_tributaria FROM adm_entrada_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_documento_entrada = " +rsm.getInt("cd_documento_entrada") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "			AND P.cd_tributo           = "+cdTributoII + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria").executeQuery());
				while(rsm9.next()){
					if(rsm9.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
						rsm.setValueToField("vl_ii_documento", rsm9.getFloat("vl_ii"));
						rsm.setValueToField("vl_base_ii_documento", rsm9.getFloat("vl_base_ii"));
						rsm.setValueToField("st_tributaria_documento", rsm9.getInt("st_tributaria"));
					}
					if(rsm9.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
						rsm.setValueToField("vl_ii_substituto_documento", rsm9.getFloat("vl_ii"));
						rsm.setValueToField("vl_base_ii_substituto_documento", rsm9.getFloat("vl_base_ii"));
						rsm.setValueToField("st_tributaria_substituto_documento", rsm9.getInt("st_tributaria"));
					}
				}
				
				//Total Tributo PIS
				ResultSetMap rsm10 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_pis, I.pr_aliquota, SUM(I.vl_base_calculo) AS vl_base_pis, P.st_tributaria, Q.cd_situacao_tributaria, Q.nm_situacao_tributaria, Q.nr_situacao_tributaria FROM adm_entrada_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "			AND I.cd_produto_servico = " +rsm.getInt("cd_produto_servico") +
																		 "          AND I.cd_documento_entrada = " +rsm.getInt("cd_documento_entrada") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "          AND P.cd_tributo           = "+cdTributoPIS + 
																		 "		  GROUP BY P.st_tributaria, Q.cd_situacao_tributaria, Q.nm_situacao_tributaria, Q.nr_situacao_tributaria, I.pr_aliquota").executeQuery());
				if(rsm10.next()){
					rsm.setValueToField("vl_pis", rsm10.getFloat("vl_pis"));
					rsm.setValueToField("vl_base_pis", rsm10.getFloat("vl_base_pis"));
					rsm.setValueToField("pr_pis", rsm10.getFloat("pr_aliquota"));
					rsm.setValueToField("st_tributaria", rsm10.getInt("st_tributaria"));
					rsm.setValueToField("cd_tributo_pis", cdTributoPIS);
					rsm.setValueToField("cd_situacao_tributaria_pis", rsm10.getString("cd_situacao_tributaria"));
					rsm.setValueToField("nm_situacao_tributaria_pis", rsm10.getString("nm_situacao_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria", rsm10.getString("nr_situacao_tributaria"));
				}
				else{
					rsm.setValueToField("vl_pis", 0);
					rsm.setValueToField("vl_base_pis", 0);
					rsm.setValueToField("pr_pis", 0);
					rsm.setValueToField("st_tributaria", 0);
					rsm.setValueToField("cd_tributo_pis", 0);
					rsm.setValueToField("cd_situacao_tributaria_pis", 0);
					rsm.setValueToField("nm_situacao_tributaria_pis", "");
					rsm.setValueToField("nr_situacao_tributaria", "");
				}
				if(rsm10.next()){
					rsm.setValueToField("ErroAliq", 4);
				}
				
				//Total Tributo PIS do Documento
				ResultSetMap rsm11 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_pis, SUM(I.vl_base_calculo) AS vl_base_pis, P.st_tributaria, Q.nr_situacao_tributaria FROM adm_entrada_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_documento_entrada = " +rsm.getInt("cd_documento_entrada") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "			AND P.cd_tributo           = "+cdTributoPIS + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria").executeQuery());
				while(rsm11.next()){
					if(rsm11.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
						rsm.setValueToField("vl_pis_documento", rsm11.getFloat("vl_pis"));
						rsm.setValueToField("vl_base_pis_documento", rsm11.getFloat("vl_base_pis"));
						rsm.setValueToField("st_tributaria_documento", rsm11.getInt("st_tributaria"));
					}
					if(rsm11.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
						rsm.setValueToField("vl_pis_substituto_documento", rsm11.getFloat("vl_pis"));
						rsm.setValueToField("vl_base_pis_substituto_documento", rsm11.getFloat("vl_base_pis"));
						rsm.setValueToField("st_tributaria_substituto_documento", rsm11.getInt("st_tributaria"));
					}
				}
				
				//Total Tributo COFINS
				ResultSetMap rsm12 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_cofins, I.pr_aliquota, SUM(I.vl_base_calculo) AS vl_base_cofins, P.st_tributaria, Q.cd_situacao_tributaria, Q.nm_situacao_tributaria, Q.nr_situacao_tributaria FROM adm_entrada_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_item =  " + rsm.getInt("cd_item") +
																		 "			AND I.cd_produto_servico = " +rsm.getInt("cd_produto_servico") +
																		 "          AND I.cd_documento_entrada = " +rsm.getInt("cd_documento_entrada") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "          AND P.cd_tributo           = "+cdTributoCOFINS + 
																		 "		  GROUP BY P.st_tributaria, Q.cd_situacao_tributaria, Q.nm_situacao_tributaria, Q.nr_situacao_tributaria, I.pr_aliquota").executeQuery());
				if(rsm12.next()){
					rsm.setValueToField("vl_cofins", rsm12.getFloat("vl_cofins"));
					rsm.setValueToField("vl_base_cofins", rsm12.getFloat("vl_base_cofins"));
					rsm.setValueToField("pr_cofins", rsm12.getFloat("pr_aliquota"));
					rsm.setValueToField("st_tributaria", rsm12.getInt("st_tributaria"));
					rsm.setValueToField("cd_tributo_cofins", cdTributoCOFINS);
					rsm.setValueToField("cd_situacao_tributaria_cofins", rsm12.getString("cd_situacao_tributaria"));
					rsm.setValueToField("nm_situacao_tributaria_cofins", rsm12.getString("nm_situacao_tributaria"));
					rsm.setValueToField("nr_situacao_tributaria", rsm12.getString("nr_situacao_tributaria"));
				}
				else{
					rsm.setValueToField("vl_cofins", 0);
					rsm.setValueToField("vl_base_cofins", 0);
					rsm.setValueToField("pr_cofins", 0);
					rsm.setValueToField("st_tributaria", 0);
					rsm.setValueToField("cd_tributo_cofins", 0);
					rsm.setValueToField("cd_situacao_tributaria_cofins", 0);
					rsm.setValueToField("nm_situacao_tributaria_cofins", "");
					rsm.setValueToField("nr_situacao_tributaria", "");
				}
				if(rsm12.next()){
					rsm.setValueToField("ErroAliq", 4);
				}
				
				//Total Tributo COFINS do Documento
				ResultSetMap rsm13 = new ResultSetMap(connect.prepareStatement("SELECT SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_cofins, SUM(I.vl_base_calculo) AS vl_base_cofins, P.st_tributaria, Q.nr_situacao_tributaria FROM adm_entrada_item_aliquota I  " +
																		 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																		 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																		 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria AND Q.cd_tributo = P.cd_tributo) " +
																		 "        WHERE I.cd_documento_entrada = " +rsm.getInt("cd_documento_entrada") +
																		 "          AND I.cd_empresa = " + rsm.getInt("cd_empresa") + 
																		 "			AND P.cd_tributo           = "+cdTributoCOFINS + 
																		 "		  GROUP BY P.st_tributaria, Q.nr_situacao_tributaria").executeQuery());
				while(rsm13.next()){
					if(rsm13.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
						rsm.setValueToField("vl_cofins_documento", rsm13.getFloat("vl_cofins"));
						rsm.setValueToField("vl_base_cofins_documento", rsm13.getFloat("vl_base_cofins"));
						rsm.setValueToField("st_tributaria_documento", rsm13.getInt("st_tributaria"));
					}
					if(rsm13.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
						rsm.setValueToField("vl_cofins_substituto_documento", rsm13.getFloat("vl_cofins"));
						rsm.setValueToField("vl_base_cofins_substituto_documento", rsm13.getFloat("vl_base_cofins"));
						rsm.setValueToField("st_tributaria_substituto_documento", rsm13.getInt("st_tributaria"));
					}
				}
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
				Conexao.desconectar(connect);
		}
	}

	public static int cancelarEntrada(int cdDocumentoEntrada) {
		return cancelarEntrada(cdDocumentoEntrada, null);
	}

	public static int cancelarEntrada(int cdDocumentoEntrada, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM alm_balanco_doc_entrada " +
					"WHERE cd_documento_entrada = ?");
			pstmt.setInt(1, cdDocumentoEntrada);
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return ERR_DOC_BALANCO;
			}

			DocumentoEntrada documento = DocumentoEntradaDAO.get(cdDocumentoEntrada, connection);
			if (documento==null || documento.getStDocumentoEntrada()!=ST_EM_ABERTO)
				return -1;
			else {
				documento.setStDocumentoEntrada(ST_CANCELADO);
				return DocumentoEntradaDAO.update(documento, connection)>0 ? 1 : -1;
			}
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

	public static Result faturarFrete(int cdDocumentoEntrada, GregorianCalendar dtVencimento, GregorianCalendar dtEmissao, int cdConta) {
		return faturarFrete(cdDocumentoEntrada, dtVencimento, dtEmissao, cdConta, null);
	}

	public static Result faturarFrete(int cdDocumentoEntrada, GregorianCalendar dtVencimento,
			GregorianCalendar dtEmissao, int cdConta, Connection connection)
	{
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(cdDocumentoEntrada, connection);
			int tpFrete          = docEntrada.getTpFrete();
			float vlFrete        = docEntrada.getVlFrete();
			int cdTransportadora = docEntrada.getCdTransportadora();
			// Verificações
			if (tpFrete == FRT_CIF)
				return new Result(ERR_FRETE_EMITENTE, "Foi informado que o frete é por conta do emitente (CIF), não é permitido o faturamento!");

			if (vlFrete <= 0)
				return new Result(ERR_FRETE_EMITENTE, "Valor do frete não informado!");
			// Verifica o tipo de documento
			int cdTipoDocFrete = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_FRETE", 0, 0, connection);
			if (cdTipoDocFrete <= 0)
				return new Result(ERR_NOT_TIPO_DOCUMENTO_FRETE, "Peça ao administrador que informe o tipo de documento padrão para frete!");
			//
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM adm_conta_pagar A " +
																	"WHERE A.cd_documento_entrada = " +cdDocumentoEntrada+
																	"  AND A.cd_pessoa = " +cdTransportadora+
																	"  AND A.cd_tipo_documento = " +cdTipoDocFrete+
																	"  AND A.vl_conta = "+vlFrete);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				return new Result(ERR_VALOR_FRETE_GERADO, "O faturamento do frete já foi realizado!");
			// gera a conta a pagar
			ContaPagar contaPagar = new ContaPagar(0 /*cdContaPagar*/, 0 /*cdContrato*/, cdTransportadora /*cdPessoa*/, docEntrada.getCdEmpresa(),
					                               0 /*cdContaOrigem*/, cdDocumentoEntrada, cdConta /*cdConta*/, 0 /*cdContaBancaria*/,
					                               dtVencimento /*dtVencimento*/, dtEmissao /*dtEmissao*/, null /*dtPagamento*/,
					                               null /*dtAutorizacao*/, docEntrada.getNrDocumentoEntrada() + "-FRT" /*nrDocumento*/,
					                               docEntrada.getNrDocumentoEntrada() /*nrReferencia*/, -1 /*nrParcela*/,
					                               cdTipoDocFrete /*cdTipoDocumento*/, vlFrete /*vlConta*/, 0 /*vlAbatimento*/,
					                               0 /*vlAcrescimo*/, 0 /*vlPago*/, "Frete - Documento de Entrada " + docEntrada.getNrDocumentoEntrada() /*dsHistorico*/,
					                               ContaPagarServices.ST_EM_ABERTO /*stConta*/, 0 /*lgAutorizado*/, ContaPagarServices.UNICA_VEZ /*tpFrequencia*/,
					                               1 /*qtParcelas*/, 0 /*vlBaseAutorizacao*/, 0 /*cdViagem*/, 0 /*cdManutencao*/, null /*txtObservacao*/,
					                               new GregorianCalendar(), dtVencimento, 0/*cdTurno*/);
			ArrayList<ContaPagarCategoria> categorias = new ArrayList<ContaPagarCategoria>();
			int cdCategDespFretes = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_DESPESAS_FRETES", 0, docEntrada.getCdEmpresa(), connection);
			int cdCategDesp = cdCategDespFretes>=0 ? cdCategDespFretes : ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_DESPESAS_DEFAULT", 0, docEntrada.getCdEmpresa(), connection);
			if (cdCategDesp > 0)
				categorias.add(new ContaPagarCategoria(0 /*cdContaPagar*/, cdCategDesp /*cdClassificacaoEconomica*/, vlFrete /*vlContaCategoria*/, 0/*cdCentroCusto*/));
			Result result = ContaPagarServices.insert(contaPagar, true, true, categorias, connection);
			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				result.setMessage("Erro ao tentar lançar conta a pagar do faturamento do frete: "+result.getMessage());
				return result;
			}

			if (isConnectionNull)
				connection.commit();
			contaPagar.setCdContaPagar(result.getCode());
			result.addObject("nmTipoDocFrete", TipoDocumentoDAO.get(cdTipoDocFrete, connection).getNmTipoDocumento());
			result.addObject("contaPagar", contaPagar);
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}


	public static Result liberarEntrada(int cdDocumentoEntrada) {
		return liberarEntrada(cdDocumentoEntrada, 0, null);
	}

	public static Result liberarEntrada(int cdDocumentoEntrada, int cdLocalArmazenamento) {
		return liberarEntrada(cdDocumentoEntrada, cdLocalArmazenamento, null);
	}

	public static Result liberarEntrada(int cdDocumentoEntrada, int cdLocalArmazenamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			if (cdLocalArmazenamento > 0) {
				DocumentoEntrada documento = DocumentoEntradaDAO.get(cdDocumentoEntrada, connection);
				PreparedStatement pstmt = connection.prepareStatement("SELECT SUM(qt_entrada) AS qt_entrada, " +
						                                              "       SUM(qt_entrada_consignada) AS qt_entrada_consignada " +
						                                              "FROM alm_entrada_local_item " +
						                                              "WHERE cd_documento_entrada = "+cdDocumentoEntrada+
						                                              "  AND cd_empresa           = "+documento.getCdEmpresa()+
						                                              "  AND cd_produto_servico   = ? " +
						                                              "  AND cd_item              = ? ");
				//
				ResultSet rs = connection.prepareStatement("SELECT cd_empresa, cd_produto_servico, cd_item, qt_entrada " +
														   "FROM alm_documento_entrada_item " +
														   "WHERE cd_documento_entrada = "+cdDocumentoEntrada).executeQuery();
				while (rs.next())	{
					float qtEntrada    = documento.getTpMovimentoEstoque()==MOV_ESTOQUE_NAO_CONSIGNADO || documento.getTpMovimentoEstoque()==MOV_AMBOS_TIPO_ESTOQUE ? rs.getFloat("qt_entrada") : 0; 
					float qtConsignada = documento.getTpMovimentoEstoque()==MOV_ESTOQUE_CONSIGNADO ? rs.getFloat("qt_entrada") : 0;
					//
					pstmt.setInt(1, rs.getInt("cd_produto_servico"));
					pstmt.setInt(2, rs.getInt("cd_item"));
					ResultSet rsTemp = pstmt.executeQuery();
					if(rsTemp.next())	{
						qtEntrada    -= rsTemp.getFloat("qt_entrada");
						qtConsignada -= rsTemp.getFloat("qt_entrada_consignada");
					}
					//
					if(qtEntrada>0 || qtConsignada>0)	{
						EntradaLocalItem entradaLocal = new EntradaLocalItem(rs.getInt("cd_produto_servico"),cdDocumentoEntrada,rs.getInt("cd_empresa"),cdLocalArmazenamento,
							   			                                 qtEntrada, qtConsignada, 0 /*cdLocalArmazenamentoItem*/, rs.getInt("cd_item"));
						if(EntradaLocalItemDAO.insert(entradaLocal, connection) <= 0)
							return new Result(-1, "Erro ao tentar registrar a entrada no local de armazenamento!");
					}
				}
			}

			Result objResult = liberarEntrada(cdDocumentoEntrada, connection);
			if (objResult.getCode()<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return objResult;
			}

			if (isConnectionNull)
				connection.commit();

			return objResult;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar liberar entrada!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result liberarEntrada(int cdDocumentoEntrada, Connection connection) {
		boolean isConnectionNull = connection==null;
		ResultSetMap rsmTabPrecos = new ResultSetMap();
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			DocumentoEntrada documento = DocumentoEntradaDAO.get(cdDocumentoEntrada, connection);
			float vlTotalProduto = 0;
			if (documento==null || documento.getStDocumentoEntrada()!=ST_EM_ABERTO)
				return new Result(-1, "Documento não está em conferência!");
			boolean lgRatearDesconto = ParametroServices.getValorOfParametroAsInteger("LG_RATEIA_DESCONTO_ENTRADA", 0, documento.getCdEmpresa())==1;
			/*
			 * Confere TOTAIS
			 */
			ResultSetMap rsmItens = getAllItens(cdDocumentoEntrada, connection);
			if(rsmItens.size()==0)
				new Result(-1, "Nota de entrada sem nenhum item, não é permitido liberar!");
			rsmItens.beforeFirst();
			float vlTributosDiretos = 0;
			while (rsmItens.next()) {
//				vlTributosDiretos += rsmItens.getFloat("vl_tributos_direto");
				vlTotalProduto    += (rsmItens.getFloat("qt_entrada") * rsmItens.getFloat("vl_unitario")) + rsmItens.getFloat("vl_acrescimo") -
								     rsmItens.getFloat("vl_desconto");
			}
			// 
			if(vlTributosDiretos == 0) {
				ResultSetMap rsmTributos = new ResultSetMap(connection.prepareStatement("SELECT * FROM adm_entrada_tributo A, adm_tributo B " +
	                                                                                    "WHERE A.cd_tributo           = B.cd_tributo " +
	                                                                                    "  AND A.cd_documento_entrada = "+cdDocumentoEntrada).executeQuery());
				while(rsmTributos.next())	{
					if(rsmTributos.getInt("tp_cobranca")==1/*Direta*/)
						vlTributosDiretos += rsmTributos.getFloat("vl_tributo");
					//
					vlTributosDiretos += rsmTributos.getFloat("vl_retido");
				}
			}
			//
			vlTotalProduto += documento.getTpFrete() == FRT_FOB ? documento.getVlFrete() : 0;
			vlTotalProduto += documento.getVlSeguro();
			vlTotalProduto  = Util.roundFloat(vlTotalProduto, 2)  + vlTributosDiretos;
			float vlFinalDocumento1 = documento.getVlTotalDocumento();
			float vlFinalDocumento2 = documento.getVlTotalDocumento() + documento.getVlDesconto();
			float vlFinalDocumento3 = documento.getVlTotalDocumento() + documento.getVlDesconto() - documento.getVlAcrescimo();
			float vlFinalDocumento4 = documento.getVlTotalDocumento() - documento.getVlAcrescimo();
			if (Math.abs(vlTotalProduto-vlFinalDocumento1)>0.1 && Math.abs(vlTotalProduto-vlFinalDocumento2)>0.1 &&
				Math.abs(vlTotalProduto-vlFinalDocumento3)>0.1	&& Math.abs(vlTotalProduto-vlFinalDocumento4)>0.1) 
			{
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro na conferência dos valores: Valor do Documento: ["+vlFinalDocumento1+","+vlFinalDocumento2+","+
						              vlFinalDocumento3+","+vlFinalDocumento4+". Soma dos Itens: "+vlTotalProduto+
						              ", vlTributosDiretos: "+vlTributosDiretos+"]");
			}
			/*
			 * Verifica quantidades
			 */
			PreparedStatement pstmtEntradaLocal = connection.prepareStatement("SELECT SUM(qt_entrada) AS qt_entrada, " +
														                    "         SUM(qt_entrada_consignada) AS qt_entrada_consignada " +
														                    "FROM alm_entrada_local_item " +
														                    "WHERE cd_produto_servico   = ? " +
														                    "  AND cd_item              = ? " +
														                    "  AND cd_empresa           = "+documento.getCdEmpresa()+
														                    "  AND cd_documento_entrada = "+cdDocumentoEntrada); 
			rsmItens.beforeFirst();
			while (rsmItens.next()) {
				pstmtEntradaLocal.setInt(1, rsmItens.getInt("cd_produto_servico"));
				pstmtEntradaLocal.setInt(2, rsmItens.getInt("cd_item"));
				ResultSetMap rsTemp = new ResultSetMap(pstmtEntradaLocal.executeQuery());
				rsTemp.next();
				float qtEntrada       = rsmItens.getFloat("qt_entrada");
				float qtLocaisEntrada = rsTemp.getFloat("qt_entrada") + rsTemp.getFloat("qt_entrada_consignada");
				
				if (Util.roundFloat(qtLocaisEntrada, 2) != Util.roundFloat(qtEntrada, 2)) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					ProdutoServico produto = ProdutoServicoDAO.get(rsmItens.getInt("cd_produto_servico"), connection); 
					return new Result(-1, "Produto: "+(produto!=null? produto.getNmProdutoServico() : "" )+
							              ", Item: "+rsmItens.getInt("cd_item")+
							              "\n Quantidade do local de armazenamento ["+qtLocaisEntrada+"], superior a do item ["+qtEntrada+"]!");
				}
			}
			rsmItens.beforeFirst();
			/*
			 * Verifica se ja existe um documento com esse numero do mesmo fornecedor
			 */
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("st_documento_entrada", "1", ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_fornecedor", "" + documento.getCdFornecedor(), ItemComparator.EQUAL, Types.VARCHAR));
			criterios.add(new ItemComparator("nr_documento_entrada", documento.getNrDocumentoEntrada(), ItemComparator.LIKE_END, Types.VARCHAR));
			criterios.add(new ItemComparator("cd_empresa", String.valueOf(documento.getCdEmpresa()), ItemComparator.EQUAL, Types.INTEGER));
			//
			ResultSetMap rsmDocumentoExistentes = DocumentoEntradaDAO.find(criterios);
			if(rsmDocumentoExistentes.next()){
				
				String nrDocExistente = Util.eliminarZeroAEsquerda(rsmDocumentoExistentes.getString("nr_documento_entrada"));
				String nrDocPassado   = Util.eliminarZeroAEsquerda(documento.getNrDocumentoEntrada());
				if(nrDocExistente.equals(nrDocPassado)){
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Não podem haver dois documentos com mesmo número e mesmo fornecedor no sistema.");
				}
			}
			
			/*
			 * Verifica se a data de emissão é menor ou igual a data do documento
			 */
			
			GregorianCalendar dtDocEntrada = (GregorianCalendar)documento.getDtDocumentoEntrada().clone();
			dtDocEntrada.set(Calendar.HOUR_OF_DAY, 23);
			dtDocEntrada.set(Calendar.MINUTE, 59);
			dtDocEntrada.set(Calendar.SECOND, 59);
			if(dtDocEntrada.before(documento.getDtEmissao())){
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "A data de emissão "+Util.formatDate(documento.getDtEmissao(), "dd/MM/yyyy HH:mm:ss")+" deve ser menor ou igual a data do documento "+Util.formatDate(dtDocEntrada, "dd/MM/yyyy HH:mm:ss")+".");
			}
			
			/*
			 * Verifica se é uma importação, e caso seja, se o valor das adições e dos itens estão de acordo
			 */
			ArrayList<ItemComparator> criteriosImportacao = new ArrayList<ItemComparator>();
			criteriosImportacao.add(new ItemComparator("cd_documento_entrada", "" + documento.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmAdicoes = EntradaAdicaoServices.find(criteriosImportacao, connection);
			while(rsmAdicoes.next()){
				int cdNcm = rsmAdicoes.getInt("cd_ncm");
				float vlTotal = rsmAdicoes.getFloat("vl_total");
				float vlTotalItens = 0;
				float qtItens = rsmAdicoes.getFloat("qt_itens");
				float qtTotalItens = 0;
				String nrNcm = rsmAdicoes.getString("nr_ncm");
				while(rsmItens.next()){
					if(rsmItens.getInt("cd_ncm") == cdNcm){
						qtTotalItens++;
						vlTotalItens += Util.roundFloat(Util.roundFloat(rsmItens.getFloat("vl_unitario"), 2) * Util.roundFloat(rsmItens.getFloat("qt_entrada"), 2) + Util.roundFloat(rsmItens.getFloat("vl_acrescimo"), 2) - Util.roundFloat(rsmItens.getFloat("vl_desconto"), 2), 2);
					}
				}
				rsmItens.beforeFirst();
				if(!Util.formatNumber(vlTotal, 2).equals(Util.formatNumber(vlTotalItens, 2))){
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "O valor total dos itens (R$ "+Util.formatNumber(vlTotalItens, 2)+") do NCM Nr. " + nrNcm + " difere do total da adição (R$ " + Util.formatNumber(vlTotal, 2) + ")");
				}
				
				if(!Util.formatNumber(qtItens, 2).equals(Util.formatNumber(qtTotalItens, 2))){
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Quantidade de itens ("+Util.formatNumber(qtItens, 2)+") do NCM Nr. " + nrNcm + " difere do total informado na adição ("+Util.formatNumber(qtTotalItens, 2)+")");
				}
			}
			rsmAdicoes.beforeFirst();
			
			/*
			 * Atualiza SITUAÇÃO do documento de entrada
			 */
			documento.setStDocumentoEntrada(ST_LIBERADO);
			if ((DocumentoEntradaDAO.update(documento, connection)>0 ? 1 : -1) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao tentar atualizar dados do documento de entrada!");
			}
			/*
			 * Atualiza preços e custos médios, e aplica regras de preço
			 */
			if (documento.getTpEntrada() != ENT_ACERTO_CONSIGNACAO) {
				if (documento.getTpEntrada() == ENT_COMPRA || documento.getTpEntrada() == ENT_CONSIGNACAO) {
					ResultSet rs = connection.prepareStatement("SELECT SUM(vl_unitario * qt_entrada) AS vl_total_item," +
							                                   "       SUM(vl_desconto) AS vl_total_desconto," +
							                                   "       SUM(vl_acrescimo) AS vl_total_acrescimo  " +
							                                   "FROM alm_documento_entrada_item " +
							                                   "WHERE cd_documento_entrada = "+cdDocumentoEntrada).executeQuery();
					boolean lgDescontoExistente = rs.next() && rs.getFloat("vl_total_desconto")>0;
					float vlTotalItens = rs.getFloat("vl_total_item") - rs.getFloat("vl_total_desconto") + rs.getFloat("vl_total_acrescimo");  
					/* atualizacao de ultimo custo e preco medio */
					int[] codeItens = new int[rsmItens.size()];
					rsmItens.beforeFirst();
					while (rsmItens.next())
						codeItens[rsmItens.getPosition()] = rsmItens.getInt("cd_produto_servico");
					long tempoinicio = System.currentTimeMillis();
					ResultSetMap rsmPrecosItens = ProdutoEstoqueServices.findProduto(documento.getCdEmpresa(), 0, codeItens, null, connection);
					while (rsmPrecosItens!=null && rsmPrecosItens.next()) {
						int cdProdutoServico = rsmPrecosItens.getInt("cd_produto_servico");
						float vlPrecoMedio   = rsmPrecosItens.getFloat("vl_preco_medio");
						float vlCustoMedio   = rsmPrecosItens.getFloat("vl_custo_medio");
						float qtEstoque      = rsmPrecosItens.getFloat("QT_ESTOQUE") + rsmPrecosItens.getFloat("QT_ESTOQUE_CONSIGNADO");

						if (rsmItens.locate("cd_produto_servico", cdProdutoServico, false)) {
							float vlTotalItem = (rsmItens.getFloat("qt_entrada") * rsmItens.getFloat("vl_unitario")) + rsmItens.getFloat("vl_acrescimo") -
												 rsmItens.getFloat("vl_desconto");
							float vlTotalDocumento   = documento.getVlTotalDocumento() + documento.getVlAcrescimo() - documento.getVlDesconto();
							float vlFreteItem        = vlTotalDocumento == 0 ? 0 : documento.getTpFrete() == DocumentoEntradaServices.FRT_CIF ? 0 :
												        (vlTotalItem * documento.getVlFrete()) / vlTotalDocumento;
							float vlSeguroItem       = vlTotalDocumento == 0 ? 0 : (vlTotalItem * documento.getVlSeguro()) / vlTotalDocumento;
							float vlFreteUnitario    = documento.getTpFrete() == DocumentoEntradaServices.FRT_CIF ? 0 : vlFreteItem / rsmItens.getFloat("qt_entrada");
							float vlSeguroUnitario   = vlSeguroItem / rsmItens.getFloat("qt_entrada");
							float vlTributosUnitario = rsmItens.getFloat("vl_tributos_direto") / rsmItens.getFloat("qt_entrada");
							float vlNewUltimoCusto   = vlTotalItem/rsmItens.getFloat("qt_entrada") + vlFreteUnitario + vlSeguroUnitario + vlTributosUnitario;
							float vlNewPrecoMedio    = ((vlPrecoMedio * qtEstoque) + vlTotalItem) / (qtEstoque + rsmItens.getFloat("qt_entrada"));
							float vlNewCustoMedio    = ((vlCustoMedio * qtEstoque) + vlTotalItem + vlFreteItem + vlSeguroItem + rsmItens.getFloat("vl_tributos_direto")) /
											            (qtEstoque + rsmItens.getFloat("qt_entrada"));
							// Atribuindo o valor do desconto rateado
							float vlDescontoGeral    = (vlTotalItem / vlTotalItens) * documento.getVlDesconto();
										
							connection.prepareStatement("UPDATE alm_documento_entrada_item " +
									                    "SET vl_desconto_geral = "+vlDescontoGeral+
									                    (!lgDescontoExistente && lgRatearDesconto ? ", vl_Desconto = "+vlDescontoGeral : "")+
									                    " WHERE cd_documento_entrada = "+cdDocumentoEntrada+
									                    "   AND cd_produto_servico   = "+rsmItens.getInt("cd_produto_servico")+
									                    "   AND cd_empresa           = "+rsmItens.getInt("cd_empresa")+
									                    "   AND cd_item              = "+rsmItens.getInt("cd_item")).executeUpdate();

							ProdutoServicoEmpresa confProdutoServico = ProdutoServicoEmpresaDAO.get(documento.getCdEmpresa(), cdProdutoServico, connection);
							if (confProdutoServico == null) {
								confProdutoServico = new ProdutoServicoEmpresa(documento.getCdEmpresa(),cdProdutoServico,0 /*cdUnidadeMedida*/,"" /*idReduzido*/,
																			   0 /*vlPrecoMedio*/, 0 /*vlCustoMedio*/, 0 /*vlUltimoCusto*/,
																			   0 /*qtIdeal*/, 0 /*qtMinima*/, 0 /*qtMaxima*/, 0 /*dtDiasEstoque*/,
																			   0 /*qtPrecisaoCusto*/, 0 /*qtPrecisaoUnidade*/, 0 /*qtDiasGarantia*/,
																			   ProdutoServicoEmpresaServices.TP_MANUAL /*tpReabastecimento*/,
																			   ProdutoServicoEmpresaServices.CTL_QUANTIDADE /*tpControleEstoque*/,
																			   ProdutoServicoEmpresaServices.TRANSP_COMUM /*tpTransporte*/,
																			   ProdutoServicoEmpresaServices.ST_ATIVO /*stAtivo*/, null /*dtDesativacao*/,
																			   "" /*nrOrdem*/, 0 /*lgEstoqueNegativo*/);
								if (ProdutoServicoEmpresaDAO.insert(confProdutoServico, connection) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return new Result(-1, "Erro ao tentar salvar(incluir) dados do produto na empresa!");
								}
							}
							
							confProdutoServico.setVlUltimoCusto(vlNewUltimoCusto);
							confProdutoServico.setVlCustoMedio(vlNewCustoMedio);
							confProdutoServico.setVlPrecoMedio(vlNewPrecoMedio);
							if (ProdutoServicoEmpresaDAO.update(confProdutoServico, connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return new Result(-1, "Erro ao tentar salvar(alterar) dados do produto na empresa!");
							}
						}
					}
					/* *
					 * Aplicacao de regras de tabelas de precos aplicaveis aos itens 
					 * */
					PreparedStatement pstmt = connection.prepareStatement("SELECT cd_tabela_preco, nm_tabela_preco FROM adm_tabela_preco " +
						 	                                              "WHERE cd_empresa = " +documento.getCdEmpresa()+
																		  "  AND lg_ativo   = 1");
					rs = pstmt.executeQuery();
					while (rs.next()) {
						HashMap<String, Object> regTabPreco = new HashMap<String, Object>();
						regTabPreco = new HashMap<String, Object>();
						regTabPreco.put("CD_TABELA_PRECO", rs.getInt("cd_tabela_preco"));
						regTabPreco.put("NM_TABELA_PRECO", rs.getString("nm_tabela_preco"));
						rsmTabPrecos.addRegister(regTabPreco);
						//
						ResultSetMap rsmItensAndRegras = getAllRegras(cdDocumentoEntrada, rs.getInt("cd_tabela_preco"), connection);
						ArrayList<Integer> listCodesRegras = new ArrayList<Integer>();
						while (rsmItensAndRegras.next()) {
							ResultSetMap rsmRegras = (ResultSetMap)rsmItensAndRegras.getObject("REGRAS");
							while (rsmRegras.next())
								if (listCodesRegras.indexOf(rsmRegras.getInt("cd_regra")) == -1)
									listCodesRegras.add(rsmRegras.getInt("cd_regra"));
						}
						int[] codesRegras = new int[listCodesRegras.size()];
						for (int i=0; i<listCodesRegras.size(); i++)
							codesRegras[i] = listCodesRegras.get(i);
						ResultSetMap rsmPrecos = (ResultSetMap)TabelaPrecoServices.aplicarRegras(rs.getInt("cd_tabela_preco"), codesRegras, codeItens, connection).getObjects().get("rsmPrecos");
						if (rsmPrecos == null) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar aplicar regras de preço.");
						}
						if (regTabPreco != null)
							regTabPreco.put("PRECOS", rsmPrecos);
					}
				}
			}
			//:TODO Verificar função para que não apague os registros de Entrada Local Item já cadastrados
//			recalcTributos(cdDocumentoEntrada, connection);
			//
			
			
			//:TODO Verificar se não há problema em liberar o documento de saida origem para essa entrada
			DocumentoSaida documentoSaidaOrigem = DocumentoSaidaDAO.get(documento.getCdDocumentoSaidaOrigem(), connection);
			if(documentoSaidaOrigem != null && documentoSaidaOrigem.getStDocumentoSaida() == DocumentoSaidaServices.ST_EM_CONFERENCIA){
				Result resultado = DocumentoSaidaServices.liberarSaida(documentoSaidaOrigem.getCdDocumentoSaida(), connection);
				if(resultado.getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return resultado;
				}
			}
			
			if (isConnectionNull)
				connection.commit();

			rsmTabPrecos.beforeFirst();
			return new Result(1, "Entrada liberada com sucesso!", "tabPrecos", rsmTabPrecos);
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar liberar entrada!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result liberarEntradaBalanco(int cdDocumentoEntrada, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			int retorno = 0;
			int cdLocalArmazenamento = ParametroServices.getValorOfParametroAsInteger("CD_LOCAL_ARMAZENAMENTO_DEFAULT_CAE", 0, 0, connection);
			
			DocumentoEntrada docEntrada  = DocumentoEntradaDAO.get(cdDocumentoEntrada, connection);
			ResultSetMap     rsmDocItens = DocumentoEntradaServices.getAllItens(cdDocumentoEntrada, connection);
			
			docEntrada.setStDocumentoEntrada(DocumentoEntradaServices.ST_LIBERADO);
			retorno = DocumentoEntradaDAO.update(docEntrada, connection);

			if (isConnectionNull)
				connection.commit();
			
			return new Result(retorno, "Documento de Entrada liberado com sucesso!");
		}
	    catch(Exception pce)	{
	    	pce.printStackTrace(System.out);
	    	return new Result(-1, "Erro ao tentar carregar ", pce);
	    }
	}

	public static Result lancarFaturamento(int cdDocumentoEntrada, HashMap<String, Object> hashConfig) {
		return lancarFaturamento(cdDocumentoEntrada, hashConfig, null);
	}

	public static Result lancarFaturamento(int cdDocumentoEntrada, HashMap<String, Object> hashConfig, Connection connection) {
		int cdTipoDocumento    = hashConfig==null || hashConfig.get("cdTipoDocumento")==null ? 0 : (Integer)hashConfig.get("cdTipoDocumento");
		int cdConta            = hashConfig==null || hashConfig.get("cdConta")==null ? 0 : (Integer)hashConfig.get("cdConta");
		int qtParcelas         = hashConfig==null || hashConfig.get("qtParcelas")==null ? 0 : (Integer)hashConfig.get("qtParcelas");
		String prefixDocumento = hashConfig==null || hashConfig.get("prefixDocumento")==null ? "" : hashConfig.get("prefixDocumento").toString();
		GregorianCalendar dtVencimentoPrimeira = hashConfig==null || hashConfig.get("dtVencimentoPrimeira")==null ? new GregorianCalendar() :
			                                     (GregorianCalendar)hashConfig.get("dtVencimentoPrimeira");
		int nrDiaVencimento    = hashConfig==null || hashConfig.get("nrDiaVencimento")==null ? 0 : (Integer)hashConfig.get("nrDiaVencimento");
		String dsHistorico     = hashConfig==null || hashConfig.get("dsHistorico") == null ? "Faturamento NF Entrada" : hashConfig.get("dsHistorico").toString();
		float vlTotal          = hashConfig == null || hashConfig.get("vlConta") == null ? 0 : (Float)hashConfig.get("vlConta");
		return lancarFaturamento(cdDocumentoEntrada, cdTipoDocumento, cdConta, qtParcelas, prefixDocumento, 
				                 dtVencimentoPrimeira, nrDiaVencimento, dsHistorico, vlTotal, connection);
	}
	
	public static Result lancarFaturamento(int cdDocumentoEntrada, int cdTipoDocumento, int cdConta, int qtParcelas, String prefixDocumento, 
			                               GregorianCalendar dtVencimentoPrimeira, int nrDiaVencimento, String dsHistorico, float vlTotal, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(cdDocumentoEntrada, connection);
			dsHistorico  = (dsHistorico==null ? "Faturamento NF Entrada" : dsHistorico) + " " + docEntrada.getNrDocumentoEntrada();

			float vlParcela        = qtParcelas <= 0 ? 0 : vlTotal/qtParcelas;
			
			Result resultContas = ContaPagarServices.gerarParcelas(cdDocumentoEntrada /*cdForeignKey*/, ContaPagarServices.OF_DOCUMENTO_ENTRADA /*tpForeignKey*/,
															 docEntrada.getCdEmpresa(), docEntrada.getCdFornecedor() /*cdPessoa*/, cdTipoDocumento,
															 cdConta, qtParcelas, new Double(vlParcela), 0 /*prDesconto*/, 0.0 /*vlAdesao*/, dtVencimentoPrimeira,
															 nrDiaVencimento, ContaPagarServices.QUANTIDADE_FIXA /*tpFrequencia*/, prefixDocumento,
															 dsHistorico, 0, /*cdCategoriaParcelas*/ true /*returnContas*/, connection);

			if (resultContas.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return resultContas;
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1, "Faturamento concluído com êxito.", "contas", resultContas.getObjects().get("contas"));
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return new Result(-1, "Erros reportados ao registrar faturamento de Documento de Entrada. Anote a mensagem de erro abaixo e " +
					              "entre em contato com o suporte técnico:\n" +e.getLocalizedMessage(), e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static String getProximoNrDocumento(int cdEmpresa) {
		return getProximoNrDocumento(cdEmpresa, null);
	}

	public static String getProximoNrDocumento(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrDocumento = 0;

			if ((nrDocumento = NumeracaoDocumentoServices.getProximoNumero("DOCUMENTO_ENTRADA", nrAno, cdEmpresa, connection)) <= 0)
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
	
	public static String getProximoNrDocumento2(int cdEmpresa, boolean adiciona) {
		return getProximoNrDocumento2(cdEmpresa, adiciona, null);
	}

	public static String getProximoNrDocumento2(int cdEmpresa, boolean adiciona, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrDocumento = 0;

			if(adiciona){
				if ((nrDocumento = NumeracaoDocumentoServices.getProximoNumero("DOCUMENTO_ENTRADA", nrAno, cdEmpresa, connection)) <= 0)
					return null;
			}
			else{
				if ((nrDocumento = NumeracaoDocumentoServices.getProximoNumero2("DOCUMENTO_ENTRADA", nrAno, cdEmpresa, connection)) <= 0)
					return null;
			}
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

	public static ResultSetMap getAllTributos(int cdDocumentoEntrada) {
		return getAllTributos(cdDocumentoEntrada, null);
	}

	public static ResultSetMap getAllTributos(int cdDocumentoEntrada, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT SUM((A.pr_aliquota/100) * A.vl_base_calculo) AS vl_tributo, " +
					"       SUM(A.vl_base_calculo) AS vl_base_calculo, C.nm_tributo, C.id_tributo, " +
					"       A.cd_tributo " +
					"FROM adm_entrada_item_aliquota A, adm_tributo_aliquota B, adm_tributo C " +
					"WHERE A.cd_tributo_aliquota = B.cd_tributo_aliquota " +
					"  AND A.cd_tributo = B.cd_tributo " +
					"  AND B.cd_tributo = C.cd_tributo " +
					"  AND cd_documento_entrada = ? " +
					"GROUP BY A.cd_tributo, C.nm_tributo, C.id_tributo");
			pstmt.setInt(1, cdDocumentoEntrada);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaServices.getAllTributos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllContasPagar(int cdDocumentoEntrada) {
		return getAllContasPagar(cdDocumentoEntrada, null);
	}

	public static ResultSetMap getAllContasPagar(int cdDocumentoEntrada, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_documento_entrada", Integer.toString(cdDocumentoEntrada), ItemComparator.EQUAL, Types.INTEGER));
			return ContaPagarServices.find(criterios);
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

	public static Result deleteByViagem(int cdViagem) {
		return deleteByViagem(cdViagem, null);
	}
	
	public static Result deleteByViagem(int cdViagem, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_viagem", "" + cdViagem, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmDocEntrada = DocumentoEntradaDAO.find(criterios, connect);
			while(rsmDocEntrada.next()){
				Result resultado = delete(rsmDocEntrada.getInt("cd_documento_entrada"));
				if(resultado.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar documento de entrada!");
				}
			}
			
			if (isConnectionNull)
				connect.commit();

			return new Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Falha ao tentar excluir entrada!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdDocumentoEntrada) {
		return delete(cdDocumentoEntrada);
	}
	
	public static Result delete(int cdDocumentoEntrada) {
		return delete(cdDocumentoEntrada, null);
	}

	public static Result delete(int cdDocumentoEntrada, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM alm_balanco_doc_entrada " +
																  "WHERE cd_documento_entrada = "+cdDocumentoEntrada);
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(ERR_DOC_BALANCO, "Essa entrada não pode ser excluida por fazer parte de um balanco!");
			}

			DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(cdDocumentoEntrada, connection);
			int cdEmpresa = docEntrada.getCdEmpresa();

			/* verifica se existem faturas (contas a pagar) geradas a partir deste documento de entrada */
			pstmt = connection.prepareStatement("SELECT A.cd_conta_pagar FROM adm_conta_pagar A " +
												"WHERE A.cd_documento_entrada = "+cdDocumentoEntrada);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(ERR_EXISTENCIA_CONTAS_PAGAR, "Existem contas a pagar relacionadas a essa entrada, não é possível exclui-la!");
			}

			/*Exclusão das tributações das entradas*/
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_documento_entrada", "" + docEntrada.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_empresa", "" + docEntrada.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsmEntradaItemAliquota = EntradaItemAliquotaDAO.find(criterios);
			while(rsmEntradaItemAliquota.next()){
				if(EntradaItemAliquotaDAO.delete(rsmEntradaItemAliquota.getInt("cd_produto_servico"), rsmEntradaItemAliquota.getInt("cd_documento_entrada"), rsmEntradaItemAliquota.getInt("cd_empresa"), rsmEntradaItemAliquota.getInt("cd_tributo_aliquota"), rsmEntradaItemAliquota.getInt("cd_tributo"), rsmEntradaItemAliquota.getInt("cd_item"), connection) < 0){
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao deletar Entrada Item Aliquota");
				}
			}
			
			/*Exclusão da Nota Fiscal Eletronica vinculada (caso haja)*/
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_documento_entrada", "" + docEntrada.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmNotaFiscalDocVinculado = NotaFiscalDocVinculadoDAO.find(criterios, connection);
			while(rsmNotaFiscalDocVinculado.next()){
				if(NotaFiscalServices.delete(rsmNotaFiscalDocVinculado.getInt("cd_nota_fiscal")).getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao deletar Nota Fiscal Eletronica");
				}
			}
			
			/* exclusão dos itens */
			rs = connection.prepareStatement("SELECT cd_produto_servico, cd_item FROM alm_documento_entrada_item A " +
					                         "WHERE A.cd_documento_entrada = "+cdDocumentoEntrada).executeQuery();
			while (rs.next()) {
				Result result = DocumentoEntradaItemServices.delete(cdDocumentoEntrada, rs.getInt("cd_produto_servico"), cdEmpresa, rs.getInt("cd_item"), connection);
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					result.setMessage("Exclusão de entrada: "+result.getMessage());
					return result;
				}
			}
			
			/* exclusão dos tributos */
			connection.prepareStatement("DELETE FROM adm_entrada_tributo A " +
					                    "WHERE A.cd_documento_entrada = "+cdDocumentoEntrada).executeUpdate();
			
			
			
			/* exclusão das importacoes*/
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_documento_entrada", "" + cdDocumentoEntrada, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmAdicoes = EntradaAdicaoDAO.find(criterios, connection);
			while(rsmAdicoes.next()){
				if(EntradaAdicaoDAO.delete(rsmAdicoes.getInt("cd_entrada_adicao"), rsmAdicoes.getInt("cd_entrada_declaracao_importacao"), rsmAdicoes.getInt("cd_documento_entrada"), connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao deletar importações");
				}
			}
			ResultSetMap rsmImportacoes = EntradaDeclaracaoImportacaoDAO.find(criterios, connection);
			while(rsmImportacoes.next()){
				if(EntradaDeclaracaoImportacaoDAO.delete(rsmImportacoes.getInt("cd_entrada_declaracao_importacao"), rsmImportacoes.getInt("cd_documento_entrada"), connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao deletar importações");
				}
			}
			
			/* exclusão de eventos */
			ResultSetMap rsmEventos = EntradaEventoFinanceiroDAO.find(criterios, connection);
			while(rsmEventos.next()){
				if(EntradaEventoFinanceiroDAO.delete(rsmEventos.getInt("cd_documento_entrada"), rsmEventos.getInt("cd_evento_financeiro"), connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao deletar eventos");
				}
			}
			
			// Excluindo entrada
			if (DocumentoEntradaDAO.delete(cdDocumentoEntrada, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Falha ao tentar excluir entrada!");
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1, "Registro removido com sucesso");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Falha ao tentar excluir entrada!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAsResultSet(int cdDocumentoEntrada) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_documento_entrada", Integer.toString(cdDocumentoEntrada), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios);
	}

	public static ResultSetMap getAllRegras(int cdDocumentoEntrada, int cdTabelaPreco) {
		return getAllRegras(cdDocumentoEntrada, cdTabelaPreco, null);
	}

	public static ResultSetMap getAllRegras(int cdDocumentoEntrada, int cdTabelaPreco, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			TabelaPreco tabPreco = TabelaPrecoDAO.get(cdTabelaPreco, connect);
			int cdEmpresa = tabPreco.getCdEmpresa();
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.qt_entrada, A.vl_unitario, A.vl_acrescimo, A.vl_desconto, " +
					"       B.vl_preco_medio, B.vl_custo_medio, B.vl_ultimo_custo, C.cd_produto_servico, C.nm_produto_servico, " +
					"       C.tp_produto_servico, D.cd_grupo, L.vl_preco, " +
					"(SELECT MAX(G.cd_fornecedor) FROM alm_documento_entrada G, alm_documento_entrada_item H " +
					" WHERE G.cd_empresa = " +cdEmpresa+
					"   AND (G.tp_entrada = "+DocumentoEntradaServices.ENT_COMPRA+" OR G.tp_entrada = "+DocumentoEntradaServices.ENT_CONSIGNACAO+") " +
					"   AND G.cd_empresa = H.cd_empresa " +
					"   AND G.cd_documento_entrada = H.cd_documento_entrada " +
					"   AND NOT G.cd_fornecedor IS NULL " +
					"   AND G.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
					"   AND H.cd_produto_servico = C.cd_produto_servico " +
					"   AND G.dt_documento_entrada = (SELECT MAX(I.dt_documento_entrada) FROM alm_documento_entrada I, alm_documento_entrada_item J " +
					"								  WHERE I.cd_empresa = " +cdEmpresa+
					"   								AND (I.tp_entrada = "+DocumentoEntradaServices.ENT_COMPRA+" OR I.tp_entrada = "+DocumentoEntradaServices.ENT_CONSIGNACAO+") " +
					"   								AND I.cd_empresa = J.cd_empresa " +
					"   								AND I.cd_documento_entrada = J.cd_documento_entrada " +
					"   								AND NOT I.cd_fornecedor IS NULL " +
					"   								AND I.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
					"   								AND J.cd_produto_servico = C.cd_produto_servico)) AS cd_fornecedor " +
					"FROM alm_documento_entrada_item A, grl_produto_servico_empresa B, grl_produto_servico C " +
					"LEFT OUTER JOIN alm_produto_grupo D ON (C.cd_produto_servico = D.cd_produto_servico " +
					"									 AND D.cd_empresa = "+cdEmpresa+
					"                                    AND D.lg_principal = 1) " +
					"LEFT OUTER JOIN adm_produto_servico_preco L ON (L.cd_tabela_preco = "+cdTabelaPreco+
					"                                            AND L.cd_produto_servico = C.cd_produto_servico " +
					"                                            AND L.dt_termino_validade IS NULL) " +
					"WHERE A.cd_produto_servico   = B.cd_produto_servico " +
					"  AND A.cd_empresa           = B.cd_empresa " +
					"  AND B.cd_produto_servico   = C.cd_produto_servico " +
					"  AND A.cd_documento_entrada = "+cdDocumentoEntrada);

			ResultSetMap rsmItens = new ResultSetMap(pstmt.executeQuery());

			while (rsmItens.next()) {
				int cdProdutoServico = rsmItens.getInt("cd_produto_servico");
				int cdGrupo = rsmItens.getInt("cd_grupo");
				int cdFornecedor = rsmItens.getInt("cd_fornecedor");
				pstmt = connect.prepareStatement("SELECT A.cd_regra, A.cd_tabela_preco, A.cd_tabela_preco_base, " +
						"A.cd_produto_servico, A.cd_fornecedor, A.cd_grupo, A.pr_desconto, A.pr_margem_lucro, " +
						"A.pr_margem_minima, A.pr_margem_maxima, A.lg_incluir_impostos, A.lg_preco_minimo, A.tp_aproximacao, " +
						"A.nr_prioridade, A.tp_valor_base, B.nm_grupo, C.nm_produto_servico, " +
						"D.nm_tabela_preco AS nm_tabela_preco_base, G.vl_preco AS vl_preco_base, " +
						"E.nm_pessoa AS nm_fornecedor " +
						"FROM adm_tabela_preco_regra A " +
						"LEFT OUTER JOIN alm_grupo B ON (A.cd_grupo = B.cd_grupo) " +
						"LEFT OUTER JOIN grl_produto_servico C ON (A.cd_produto_servico = C.cd_produto_servico) " +
						"LEFT OUTER JOIN adm_tabela_preco D ON (A.cd_tabela_preco_base = D.cd_tabela_preco) " +
						"LEFT OUTER JOIN grl_pessoa E ON (A.cd_fornecedor = E.cd_pessoa) " +
						"LEFT OUTER JOIN adm_produto_servico_preco G ON (G.dt_termino_validade IS NULL AND " +
						"												 G.cd_tabela_preco = A.cd_tabela_preco_base AND " +
						"												 G.cd_produto_servico = ?) " +
						"WHERE (A.cd_produto_servico IS NULL OR A.cd_produto_servico = ?) " +
						"  AND (A.cd_grupo IS NULL" + (cdGrupo>0 ? " OR A.cd_grupo = ?" : "") + ") " +
						"  AND (A.cd_fornecedor IS NULL" + (cdFornecedor>0 ? " OR A.cd_fornecedor = ?" : "") + ") " +
						"  AND (A.cd_tabela_preco_base IS NULL OR EXISTS (SELECT F.cd_produto_servico " +
						"												  FROM adm_produto_servico_preco F " +
						"												  WHERE F.dt_termino_validade IS NULL " +
						"													AND F.cd_tabela_preco = A.cd_tabela_preco_base " +
						"													AND F.cd_produto_servico = ?)) " +
						"  AND A.cd_tabela_preco = ?");
				int i = 1;
				pstmt.setInt(i++, cdProdutoServico);
				pstmt.setInt(i++, cdProdutoServico);
				if (cdGrupo > 0)
					pstmt.setInt(i++, cdGrupo);
				if (cdFornecedor > 0)
					pstmt.setInt(i++, cdFornecedor);
				pstmt.setInt(i++, cdProdutoServico);
				pstmt.setInt(i++, cdTabelaPreco);
				ResultSetMap rsmRegras = new ResultSetMap(pstmt.executeQuery());
				ArrayList<String> fields = new ArrayList<String>();
				fields.add("nr_prioridade");
				rsmRegras.orderBy(fields);

				rsmItens.getRegister().put("REGRAS", rsmRegras);
			}

			rsmItens.beforeFirst();
			return rsmItens;
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

	public static int updateTotaisDocumentoEntrada(int cdDocumentoEntrada, float vlTotalItens, float vlAcrescimo, float vlDesconto, float vlTotalDocumento) {
		return updateTotaisDocumentoEntrada(cdDocumentoEntrada, vlTotalItens, vlAcrescimo, vlDesconto, vlTotalDocumento, null);
	}

	public static int updateTotaisDocumentoEntrada(int cdDocumentoEntrada, float vlTotalItens, float vlAcrescimo, float vlDesconto, float vlTotalDocumento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(cdDocumentoEntrada, connection);
			docEntrada.setVlTotalItens(vlTotalItens);
			docEntrada.setVlDesconto(vlDesconto);
			docEntrada.setVlAcrescimo(vlAcrescimo);
			docEntrada.setVlTotalDocumento(vlTotalDocumento);
			return DocumentoEntradaDAO.update(docEntrada, connection);
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

	public static int update(DocumentoEntrada docEntrada) {
		return update(docEntrada, 0, 0, 0, 0, 0, null).getCode();
	}

	public static Result update(DocumentoEntrada docEntrada, float vlBaseCalculoIcms, float vlIcms, float vlBaseCalculoIcmsSubstituto, float vlIcmsSubstituto, float vlIpi, EntradaDeclaracaoImportacao entradaDI) {
		return update(docEntrada, vlBaseCalculoIcms, vlIcms, vlBaseCalculoIcmsSubstituto, vlIcmsSubstituto, vlIpi, entradaDI, null);
	}
	
	public static Result update(DocumentoEntrada docEntrada, float vlBaseCalculoIcms, float vlIcms, float vlBaseCalculoIcmsSubstituto, float vlIcmsSubstituto, float vlIpi, EntradaDeclaracaoImportacao entradaDI, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM alm_balanco_doc_entrada " +
															   "WHERE cd_documento_entrada = "+docEntrada.getCdDocumentoEntrada());
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ERR_DOC_BALANCO);
			}
			// EXCLUI TODOS OS TRIBUTOS E LANÇA NOVAMENTE
			connect.prepareStatement("DELETE FROM adm_entrada_tributo " +
					                 "WHERE cd_documento_entrada = "+docEntrada.getCdDocumentoEntrada()).execute();
			connect.setAutoCommit(!isConnectionNull);
			/*
			 * Gravando informação do ICMS (Incluindo o substituto
			 */
			if(vlIcms > 0 || vlIcmsSubstituto > 0){
				int cdTributo        = TributoServices.getCdTributoById("ICMS", connect);
				float vlBaseCalculo  = vlBaseCalculoIcms;
				float prAliquota     = 0;
				float vlBaseRetencao = vlBaseCalculoIcmsSubstituto;
				EntradaTributo entTributo = new EntradaTributo(docEntrada.getCdDocumentoEntrada(), cdTributo, vlBaseCalculo, prAliquota, vlIcms, vlBaseRetencao, vlIcmsSubstituto);
				if(com.tivic.manager.adm.EntradaTributoDAO.insert(entTributo, connect) <= 0){
					if (isConnectionNull){
						connect.rollback();
					}
					return new Result(-1);
				}
			}
			/*
			 * Gravando informação do IPI
			 */
			if(vlIpi > 0){
				int cdTributo       = TributoServices.getCdTributoById("IPI", connect);
				float vlBaseCalculo = docEntrada.getVlTotalItens();
				float prAliquota    = 0;
				
				if(com.tivic.manager.adm.EntradaTributoDAO.insert(new EntradaTributo(docEntrada.getCdDocumentoEntrada(), cdTributo, vlBaseCalculo, prAliquota, vlIpi, 0, 0), connect) <= 0){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1);
				}
			}
			
			/*
			 * Atualiza dados de Importacao
			 */
			int cdNaturezaOperacaoImportacao = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_ENTRADA_IMPORTACAO_DEFAULT", 0);
			if(cdNaturezaOperacaoImportacao == docEntrada.getCdNaturezaOperacao()){
				if(entradaDI != null){
					if(entradaDI.getCdEntradaDeclaracaoImportacao() > 0){
						if(EntradaDeclaracaoImportacaoDAO.update(entradaDI, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1);
						}
					}
					else{
						if(EntradaDeclaracaoImportacaoDAO.insert(entradaDI, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1);
						}
					}
				}
			}
			int ret = DocumentoEntradaDAO.update(docEntrada, connect);
			
			if(ret <= 0 && isConnectionNull) {
				Conexao.rollback(connect);
				return new Result(-1);
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(ret);
		}
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int insert(DocumentoEntrada docEntrada) {
		return insert(docEntrada, 0, 0, 0, 0, 0, 0, 0, 0, 0, null, null).getCode();
	}
	
	public static Result insert(DocumentoEntrada docEntrada, float vlBaseCalculoIcms, float vlIcms, float vlBaseCalculoIcmsSubstituto, float vlIcmsSubstituto, float vlIpi, float vlCapatazia, float vlSiscomex, float vlArmazenagem, float vlAfrmm, EntradaDeclaracaoImportacao entradaDI) {
		return insert(docEntrada, vlBaseCalculoIcms, vlIcms, vlBaseCalculoIcmsSubstituto, vlIcmsSubstituto, vlIpi, vlCapatazia, vlSiscomex, vlArmazenagem, vlAfrmm, entradaDI, null);
	}
	
	public static Result insert(DocumentoEntrada docEntrada, float vlBaseCalculoIcms, float vlIcms, float vlBaseCalculoIcmsSubstituto, float vlIcmsSubstituto, float vlIpi, float vlCapatazia, float vlSiscomex, float vlArmazenagem, float vlAfrmm, EntradaDeclaracaoImportacao entradaDI, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			if(isConnectionNull){
				connect.setAutoCommit(false);
			}
			
			int cdDocumentoEntrada = DocumentoEntradaDAO.insert(docEntrada, connect);
			
			if(cdDocumentoEntrada <= 0){
				if (isConnectionNull)
					connect.rollback();
				return new Result(-1);
			}
			
			docEntrada.setCdDocumentoEntrada(cdDocumentoEntrada);
			
			/*
			 * Gravando informação do ICMS (Incluindo o substituto
			 */
			if(vlIcms > 0 || vlIcmsSubstituto > 0){
				int cdTributo        = TributoServices.getCdTributoById("ICMS", connect);
				float vlBaseCalculo  = vlBaseCalculoIcms;
				float prAliquota     = 0;
				float vlBaseRetencao = vlBaseCalculoIcmsSubstituto;
				EntradaTributo entTributo = new EntradaTributo(docEntrada.getCdDocumentoEntrada(), cdTributo, vlBaseCalculo, prAliquota, vlIcms, vlBaseRetencao, vlIcmsSubstituto);
				if(com.tivic.manager.adm.EntradaTributoDAO.insert(entTributo, connect) <= 0){
					if (isConnectionNull){
						connect.rollback();
					}
					return new Result(-1);
				}
			}
			/*
			 * Gravando informação do IPI
			 */
			if(vlIpi > 0){
				int cdTributo       = TributoServices.getCdTributoById("IPI", connect);
				float vlBaseCalculo = docEntrada.getVlTotalItens();
				float prAliquota    = 0;
				
				if(com.tivic.manager.adm.EntradaTributoDAO.insert(new EntradaTributo(docEntrada.getCdDocumentoEntrada(), cdTributo, vlBaseCalculo, prAliquota, vlIpi, 0, 0), connect) <= 0){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1);
				}
			}
			/*
			 * Adiciona informações de Importação
			 */
			int cdNaturezaOperacaoImportacao = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_ENTRADA_IMPORTACAO_DEFAULT", 0);
			if(cdNaturezaOperacaoImportacao == docEntrada.getCdNaturezaOperacao()){
				if(adicionarDadosImportacao(cdDocumentoEntrada, vlCapatazia, vlSiscomex, vlArmazenagem, vlAfrmm, docEntrada.getQtVolumes(), docEntrada.getVlPesoBruto(), docEntrada.getVlPesoLiquido(), connect).getCode() < 0){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar eventos de importação");
				}
				if(entradaDI != null){
					entradaDI.setCdDocumentoEntrada(cdDocumentoEntrada);
					if(EntradaDeclaracaoImportacaoDAO.insert(entradaDI, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao gravar DI");
					}
				}
			}
			
			if(isConnectionNull){
				connect.commit();
			}
			
			Result resultado = new Result(cdDocumentoEntrada);
			
			resultado.addObject("rsmDocumentoEntrada", getAsResultSet(cdDocumentoEntrada));
			
			return resultado;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result adicionarDadosImportacao(int cdDocumentoEntrada, float vlCapatazia, float vlSiscomex, float vlArmazenagem, float vlAfrmm, float qtVolume, float vlPesoBruto, float vlPesoLiquido){
		return adicionarDadosImportacao(cdDocumentoEntrada, vlCapatazia, vlSiscomex, vlArmazenagem, vlAfrmm, qtVolume, vlPesoBruto, vlPesoLiquido, null);
	}
	
	public static Result adicionarDadosImportacao(int cdDocumentoEntrada, float vlCapatazia, float vlSiscomex, float vlArmazenagem, float vlAfrmm, float qtVolume, float vlPesoBruto, float vlPesoLiquido, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(cdDocumentoEntrada, connect);
			docEntrada.setQtVolumes(qtVolume);
			docEntrada.setVlPesoBruto(vlPesoBruto);
			docEntrada.setVlPesoLiquido(vlPesoLiquido);
			if(DocumentoEntradaDAO.update(docEntrada, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar documento de entrada");
			}
			//Inserindo Capatazia
			int cdEventoFinanceiroCapatazia = ParametroServices.getValorOfParametroAsInteger("CD_EVENTO_FINANCEIRO_CAPATAZIA", 0);
			if(cdEventoFinanceiroCapatazia > 0 && vlCapatazia > 0){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_documento_entrada", "" + cdDocumentoEntrada, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_evento_financeiro", "" + cdEventoFinanceiroCapatazia, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsm = EntradaEventoFinanceiroDAO.find(criterios, connect);
				if(rsm.next()){
					EntradaEventoFinanceiro entradaEventoFinanceiro = EntradaEventoFinanceiroDAO.get(cdDocumentoEntrada, cdEventoFinanceiroCapatazia, connect);
					entradaEventoFinanceiro.setVlEventoFinanceiro(vlCapatazia);
					if(EntradaEventoFinanceiroDAO.update(entradaEventoFinanceiro, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar evento capatazia");
					}
				}
				else{
					EntradaEventoFinanceiro entradaEventoFinanceiro = new EntradaEventoFinanceiro(cdDocumentoEntrada, cdEventoFinanceiroCapatazia, vlCapatazia, 1, 1, 1);
					if(EntradaEventoFinanceiroDAO.insert(entradaEventoFinanceiro, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar evento capatazia");
					}
				}
			}
			//Inserindo Taxa SISCOMEX
			int cdEventoFinanceiroSiscomex = ParametroServices.getValorOfParametroAsInteger("CD_EVENTO_FINANCEIRO_TAXA_SISCOMEX", 0);
			if(cdEventoFinanceiroSiscomex > 0 && vlSiscomex > 0){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_documento_entrada", "" + cdDocumentoEntrada, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_evento_financeiro", "" + cdEventoFinanceiroSiscomex, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsm = EntradaEventoFinanceiroDAO.find(criterios, connect);
				if(rsm.next()){
					EntradaEventoFinanceiro entradaEventoFinanceiro = EntradaEventoFinanceiroDAO.get(cdDocumentoEntrada, cdEventoFinanceiroSiscomex, connect);
					entradaEventoFinanceiro.setVlEventoFinanceiro(vlSiscomex);
					if(EntradaEventoFinanceiroDAO.update(entradaEventoFinanceiro, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar evento taxa siscomex");
					}
				}
				else{
					EntradaEventoFinanceiro entradaEventoFinanceiro = new EntradaEventoFinanceiro(cdDocumentoEntrada, cdEventoFinanceiroSiscomex, vlSiscomex, 1, 1, 1);
					if(EntradaEventoFinanceiroDAO.insert(entradaEventoFinanceiro, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar evento taxa siscomex");
					}
				}
			}
			//Inserindo Armazenagem
			int cdEventoFinanceiroArmazenagem = ParametroServices.getValorOfParametroAsInteger("CD_EVENTO_FINANCEIRO_ARMAZENAGEM", 0);
			if(cdEventoFinanceiroArmazenagem > 0 && vlArmazenagem > 0){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_documento_entrada", "" + cdDocumentoEntrada, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_evento_financeiro", "" + cdEventoFinanceiroArmazenagem, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsm = EntradaEventoFinanceiroDAO.find(criterios, connect);
				if(rsm.next()){
					EntradaEventoFinanceiro entradaEventoFinanceiro = EntradaEventoFinanceiroDAO.get(cdDocumentoEntrada, cdEventoFinanceiroArmazenagem, connect);
					entradaEventoFinanceiro.setVlEventoFinanceiro(vlArmazenagem);
					if(EntradaEventoFinanceiroDAO.update(entradaEventoFinanceiro, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar evento armazenagem");
					}
				}
				else{
					EntradaEventoFinanceiro entradaEventoFinanceiro = new EntradaEventoFinanceiro(cdDocumentoEntrada, cdEventoFinanceiroArmazenagem, vlArmazenagem, 1, 1, 1);
					if(EntradaEventoFinanceiroDAO.insert(entradaEventoFinanceiro, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar evento armazenagem");
					}
				}
			}
			//Inserindo Afrmm
			int cdEventoFinanceiroAfrmm = ParametroServices.getValorOfParametroAsInteger("CD_EVENTO_FINANCEIRO_AFRMM", 0);
			if(cdEventoFinanceiroAfrmm > 0 && vlAfrmm > 0){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_documento_entrada", "" + cdDocumentoEntrada, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_evento_financeiro", "" + cdEventoFinanceiroAfrmm, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsm = EntradaEventoFinanceiroDAO.find(criterios, connect);
				if(rsm.next()){
					EntradaEventoFinanceiro entradaEventoFinanceiro = EntradaEventoFinanceiroDAO.get(cdDocumentoEntrada, cdEventoFinanceiroAfrmm, connect);
					entradaEventoFinanceiro.setVlEventoFinanceiro(vlAfrmm);
					if(EntradaEventoFinanceiroDAO.update(entradaEventoFinanceiro, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar evento afrmm");
					}
				}
				else{
					EntradaEventoFinanceiro entradaEventoFinanceiro = new EntradaEventoFinanceiro(cdDocumentoEntrada, cdEventoFinanceiroAfrmm, vlAfrmm, 1, 1, 1);
					if(EntradaEventoFinanceiroDAO.insert(entradaEventoFinanceiro, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar evento afrmm");
					}
				}
			}
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1);
		}
		catch(Exception e) {
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ArrayList<ContaPagarCategoria> getClassificacaoEmCategorias(DocumentoEntrada documentoEntrada, Connection connect) {
		boolean isConnectionNull = connect==null;
		ArrayList<ContaPagarCategoria> result = new ArrayList<ContaPagarCategoria>();
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			/*
			 * Sumariza por grupo
			 */
			ResultSetMap rsmPorGrupo = new ResultSetMap(connect.prepareStatement(
							"SELECT C.cd_grupo, C.cd_grupo_superior, C.cd_categoria_despesa, " +
							"       SUM((TRUNC(qt_entrada * vl_unitario * 100) / 100) + vl_acrescimo - vl_desconto) AS vl_total_grupo " +
		                    "FROM alm_documento_entrada_item A " +
		                    "LEFT OUTER JOIN alm_produto_grupo B ON (A.cd_produto_servico = B.cd_produto_servico" +
		                    "                                    AND B.lg_principal       = 1" +
		                    "                                    AND B.cd_empresa         = "+documentoEntrada.getCdEmpresa()+") " +
		                    "LEFT OUTER JOIN alm_grupo         C ON (B.cd_grupo           = C.cd_grupo) " +
		                    "WHERE A.cd_documento_entrada = "+documentoEntrada.getCdDocumentoEntrada()+
		                    " GROUP BY C.cd_grupo, C.cd_grupo_superior, C.cd_categoria_despesa").executeQuery());
			while(rsmPorGrupo.next())	{
				int cdCategoria 	= rsmPorGrupo.getInt("cd_categoria_despesa");
				int cdGrupoSuperior = rsmPorGrupo.getInt("cd_grupo_superior");
				while(cdCategoria <= 0 && cdGrupoSuperior>0)	{
					ResultSet rs = connect.prepareStatement("SELECT cd_categoria_despesa, cd_grupo_superior " +
							                                "FROM alm_grupo " +
							                                "WHERE cd_grupo = "+cdGrupoSuperior).executeQuery();
					if(rs.next())	{
						cdGrupoSuperior = rs.getInt("cd_grupo_superior");
						cdCategoria 	= rs.getInt("cd_categoria_despesa");
					}
					else
						break;
				}
				/*
				 * Se não encontrou a categoria nem no grupo nem no grupo superior, busca a categoria default
				 */
				if(cdCategoria <= 0)	{
					cdCategoria = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ENTRADAS_DEFAULT", 0, documentoEntrada.getCdEmpresa());
					if(cdCategoria <=0)	{
						com.tivic.manager.util.Util.registerLog(new Exception("Erro: Categoria padrão não informada!"));
						return null;
					}
				}
				/*
				 * Verifica se a categoria já não está na lista, não estando inclui, se já estiver acrescenta o valor
				 */
				boolean found = false;
				for(int i=0; i<result.size(); i++)	{
					found = result.get(i).getCdCategoriaEconomica()==cdCategoria;
					if(found)	{
						result.get(i).setVlContaCategoria(result.get(i).getVlContaCategoria() + rsmPorGrupo.getFloat("vl_total_grupo"));
						break;
					}
				}
				if(!found)
					result.add(new ContaPagarCategoria(0 /*cdContaPagar*/, cdCategoria, rsmPorGrupo.getFloat("vl_total_grupo"), 0/*cdCentroCusto*/));
			}
			return result;
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

	public static Result retornaSituacao(int cdDocumentoEntrada) {
		return retornaSituacao(cdDocumentoEntrada, null);
	}
	
	public static Result retornaSituacao(int cdDocumentoEntrada, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;		
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			
			int ret = connect.prepareStatement("UPDATE alm_documento_entrada SET st_documento_entrada = "+ST_EM_ABERTO+
                    						" WHERE cd_documento_entrada = "+cdDocumentoEntrada).executeUpdate();
			
			if(ret < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1);
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(ret);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar voltar situação para Em Aberto!", e);
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result recalcTributos(int cdDocumentoEntrada) {
		return recalcTributos(cdDocumentoEntrada, null);
	}

	public static Result recalcTributos(int cdDocumentoEntrada, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			ResultSet rs = connect.prepareStatement("SELECT * FROM alm_documento_entrada_item WHERE cd_documento_entrada = "+cdDocumentoEntrada).executeQuery();
			while(rs.next())	{
				DocumentoEntradaItem item = new DocumentoEntradaItem(rs.getInt("cd_documento_entrada"), rs.getInt("cd_produto_servico"),
																	 rs.getInt("cd_empresa"), rs.getFloat("qt_entrada"), rs.getFloat("vl_unitario"),
																	 rs.getFloat("vl_acrescimo"), rs.getFloat("vl_desconto"), rs.getInt("cd_unidade_medida"),
																	 (rs.getTimestamp("dt_entrega_prevista")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_entrega_prevista").getTime()),
																	 rs.getInt("cd_natureza_operacao"), rs.getInt("cd_adicao"), rs.getInt("cd_item"), rs.getFloat("vl_vucv"),rs.getFloat("vl_desconto_geral"), rs.getInt("cd_tipo_credito"));

				EntradaItemAliquotaServices.calcTributos(item, connect);
			}
			if(isConnectionNull)
				connect.commit();
			return new Result(1); 
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar recalcular tributos!", e);
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
		
	public static Result entradaFromTransferencia(int cdDocumentoSaida, int cdEmpresaDestino, int cdLocalDestino, int cdDigitador) {
		// Se igual a 0, entao pega o primeiro cd_local_armazendo cadastrado para a empresa.
		if (cdLocalDestino == 0){			
			ResultSetMap rsmLocal = LocalArmazenamentoServices.getAll(cdEmpresaDestino);
			if (rsmLocal.next())
				cdLocalDestino = rsmLocal.getInt("cd_local_armazenamento");
			
		}
		return entradaFromTransferencia(cdDocumentoSaida, cdEmpresaDestino, cdLocalDestino, cdDigitador, null);
	}

	public static Result entradaFromTransferencia(int cdDocumentoSaida, int cdEmpresaDestino, int cdLocalDestino, int cdDigitador, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			/*
			 * INSERE O DOCUMENTO DE SAÍDA
			 */
			DocumentoSaida transf = DocumentoSaidaDAO.get(cdDocumentoSaida, connect);
			if (transf == null)
				return new Result(-1, "Transferência não localizada!");
			
			if (transf.getTpSaida()!=DocumentoSaidaServices.SAI_TRANSFERENCIA || transf.getCdCliente()!=cdEmpresaDestino)
				return new Result(-1, "Saída não é uma transferência ou o destinatário não é a empresa selecionada!");
			
			/*
			 * Verifica se a entrada já foi gerada
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM alm_documento_entrada WHERE cd_documento_saida_origem = "+cdDocumentoSaida).executeQuery();
			if (rs.next())
				return new Result(-1, "A entrada referente a essa transferência já foi gerada!");
			
			/*
			 * INSERINDO ENTRADA
			 */
			DocumentoEntrada docEntrada = new DocumentoEntrada(0 /*cdDocumentoEntrada*/, cdEmpresaDestino, transf.getCdTransportadora(),
											                    transf.getCdEmpresa() /*cdFornecedor*/, transf.getDtEmissao(), transf.getDtDocumentoSaida(),
											                    DocumentoEntradaServices.ST_EM_ABERTO /*stDocumentoEntrada*/, transf.getVlDesconto(),
											                    transf.getVlAcrescimo(), transf.getNrDocumentoSaida() /*nrDocumentoEntrada*/,
											                    DocumentoEntradaServices.TP_DOC_NAO_FISCAL /*tpDocumentoEntrada*/, transf.getNrConhecimento(),
											                    DocumentoEntradaServices.ENT_TRANSFERENCIA/*tpEntrada*/, transf.getTxtObservacao(),
											                    transf.getCdNaturezaOperacao(),transf.getTpFrete(),transf.getNrPlacaVeiculo(),transf.getSgPlacaVeiculo(),
											                    transf.getQtVolumes(),transf.getDtSaidaTransportadora(),transf.getDsViaTransporte(),
											                    transf.getTxtCorpoNotaFiscal(),transf.getVlPesoBruto(),transf.getVlPesoLiquido(),
											                    transf.getDsEspecieVolumes(),transf.getDsMarcaVolumes(),transf.getNrVolumes(),
											                    transf.getTpMovimentoEstoque(),transf.getCdMoeda(), 0 /*cdTabelaPreco*/, transf.getVlTotalDocumento(),
											                    cdDocumentoSaida /*cdDocumentoSaidaOrigem*/,transf.getVlFrete(),transf.getVlSeguro(),
											                    cdDigitador,transf.getVlTotalItens(),1 /*nrSerie*/);
			int cdDocumentoEntrada = DocumentoEntradaDAO.insert(docEntrada, connect);
			if (cdDocumentoEntrada <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Falha ao criar documento de entrada a partir da transferência!");
			}
			/*
			 * INCLUINDO ITENS e ENTRADA POR LOCAL
			 */
			ResultSetMap rsmItens = DocumentoSaidaServices.getAllItens(cdDocumentoSaida, connect);
			while(rsmItens.next())	{
				int cdProdutoServico = rsmItens.getInt("cd_produto_servico");
				// Verifica se o item existe na tabela ProdutoServicoEmpresa
				if (ProdutoServicoEmpresaDAO.get(cdEmpresaDestino, cdProdutoServico, connect) == null) {
					// Pegando a referência da empresa de origem
					ProdutoServicoEmpresa prodEmp = ProdutoServicoEmpresaDAO.get(transf.getCdEmpresa(), cdProdutoServico, connect);
					String idReduzido = prodEmp!=null ? prodEmp.getIdReduzido() : "";
					// Salvando os dados do produto na empresa
					ProdutoServicoEmpresaDAO.insert(new ProdutoServicoEmpresa(cdEmpresaDestino, cdProdutoServico, rsmItens.getInt("cd_unidade_medida"),
																			  idReduzido,0 /*vlPrecoMedio*/,0 /*vlCustoMedio*/,0 /*vlUltimoCusto*/,
																			  0 /*qtIdeal*/,0 /*qtMinima*/,0 /*qtMaxima*/,0 /*qtDiasEstoque*/,
																			  0 /*qtPrecisaoCusto*/,0 /*qtPrecisaoUnidade*/,0 /*qtDiasGarantia*/,
																			  0 /*tpReabastecimento*/,0 /*tpControleEstoque*/,0 /*tpTransporte*/,
																			  ProdutoServicoEmpresaServices.ST_ATIVO /*stProdutoEmpresa*/,
																			  null /*dtDesativacao*/,"" /*nrOrdem*/, 0 /*lgEstoqueNegativo*/), connect);
				}
				// ITENS
				float qtSaida = rsmItens.getFloat("qt_saida");
				if (DocumentoEntradaItemServices.insert(new DocumentoEntradaItem(cdDocumentoEntrada, cdProdutoServico,
																			cdEmpresaDestino, qtSaida /*qtEntrada*/,
																			rsmItens.getFloat("vl_unitario"),rsmItens.getFloat("vl_acrescimo"), rsmItens.getFloat("vl_desconto"), 0 /*cdUnidadeMedida*/, 
																			null, 0 /*cdNaturezaOperacao*/, 0 /*cdAdicao*/, 0 /*cdItem*/, 0 /*vlVucv*/, rsmItens.getFloat("vl_desconto"), 0 /*cdTipoCredito*/), 
																			0, true, connect).getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Falha ao incluir item na entrada!");
				}
				// Locais
				if (EntradaLocalItemDAO.insert(new EntradaLocalItem(cdProdutoServico,cdDocumentoEntrada, cdEmpresaDestino, cdLocalDestino /*cdLocalArmazenamento*/,
						                                            transf.getTpMovimentoEstoque() == MOV_ESTOQUE_NAO_CONSIGNADO ? qtSaida : 0 /*qtEntrada*/,
						                                            transf.getTpMovimentoEstoque() == MOV_ESTOQUE_CONSIGNADO ? qtSaida : 0 /*qtEntradaConsignada*/,
						                                            0 /*cdReferencia*/, 1 /*cdItem*/), connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Falha ao registrar saida no local de armazenamento!");
				}
			}

			if (isConnectionNull)
				connect.commit();

			return new Result(cdDocumentoEntrada); 
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar criar entrada a partir da transferência!", e);
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo que a partir de uma nota de remessa e varias notas de venda, gera uma nota de entrada de retorno para os produtos não vendidos
	 * @since 24/07/2014
	 * @author Gabriel
	 * @param codigosDocumentoRemessa Array com o conjunto de codigos de remessa
	 * @param codigosDocumentoVenda Array com o conjunto de codigos de venda
	 * @param cdEmpresa Codigo da empresa a que pertence as notas
	 * @param cdDigitador Codigo do usuário que está registrando as notas
	 * @return
	 */
	public static Result entradaFromVendaExterna(ArrayList<Integer> codigosDocumentoRemessa, ArrayList<Integer> codigosDocumentoVenda, int cdLocalArmazenamento) {
		return entradaFromVendaExterna(codigosDocumentoRemessa, codigosDocumentoVenda, cdLocalArmazenamento, null);
	}

	public static Result entradaFromVendaExterna(ArrayList<Integer> codigosDocumentoRemessa, ArrayList<Integer> codigosDocumentoSaida, int cdLocalArmazenamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//Verificação em relação a conclusão dos documentos que farão parte da venda externa, para a contabilização ou não de estoque
			for(int cdDocumentoRemessa : codigosDocumentoRemessa){
				DocumentoSaida documentoRemessa = DocumentoSaidaDAO.get(cdDocumentoRemessa, connect);
				if(ParametroServices.getValorOfParametroAsInteger("LG_CONTABILIZACAO_ESTOQUE_VENDA_EXTERNA", 0, documentoRemessa.getCdEmpresa())==1){
					if(documentoRemessa.getStDocumentoSaida() != DocumentoSaidaServices.ST_CONCLUIDO){
						return new Result(-1, "A contabilização de estoque para venda externa está ativa. Documento de Remessa não foi liberado. Favor libera-lo antes de finalizar a viagem!");
					}
				}
				else{
					if(documentoRemessa.getStDocumentoSaida() == DocumentoSaidaServices.ST_CONCLUIDO){
						return new Result(-1, "A contabilização de estoque para venda externa não está ativa. Documento de Remessa foi liberado. Favor coloca-lo em conferência antes de finalizar a viagem!");
					}
				}
			}
			for(int cdDocumentoVenda : codigosDocumentoSaida){
				DocumentoSaida documentoVenda = DocumentoSaidaDAO.get(cdDocumentoVenda, connect);
				if(ParametroServices.getValorOfParametroAsInteger("LG_CONTABILIZACAO_ESTOQUE_VENDA_EXTERNA", 0, documentoVenda.getCdEmpresa())==1){
					if(documentoVenda.getStDocumentoSaida() != DocumentoSaidaServices.ST_CONCLUIDO){
						return new Result(-1, "A contabilização de estoque para venda externa está ativa. Documento de Venda Nº "+documentoVenda.getNrDocumentoSaida()+" não foi liberado. Favor libera-lo antes de finalizar a viagem!");
					}
				}
				else{
					if(documentoVenda.getStDocumentoSaida() == DocumentoSaidaServices.ST_CONCLUIDO){
						return new Result(-1, "A contabilização de estoque para venda externa não está ativa. Documento de Venda Nº "+documentoVenda.getNrDocumentoSaida()+" foi liberado. Favor coloca-lo em conferência antes de finalizar a viagem!");
					}
				}
			}
			
			//Busca o primeiro documento de remessa passado para pegar o codigo de empresa e digitador
			DocumentoSaida documentoRemessaPrimeiro = DocumentoSaidaDAO.get(codigosDocumentoRemessa.get(0), connect);
			int cdEmpresa   = documentoRemessaPrimeiro.getCdEmpresa();
			int cdDigitador = documentoRemessaPrimeiro.getCdDigitador();
			int cdNaturezaOperacaoRetornoNormal = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_VENDA_EXTERNA_RETORNO_NORMAL", 0);
			int cdNaturezaOperacaoRetornoSubstituicao = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_VENDA_EXTERNA_RETORNO_SUBSTITUICAO", 0);
			int cdMoeda = ParametroServices.getValorOfParametroAsInteger("CD_MOEDA_DEFAULT", 0);
			int cdTransportadora = documentoRemessaPrimeiro.getCdTransportadora();
			/*
			 * INSERINDO ENTRADA
			 */
			DocumentoEntrada docEntrada = new DocumentoEntrada(0 /*cdDocumentoEntrada*/, cdEmpresa, cdTransportadora, cdEmpresa, Util.getDataAtual(), Util.getDataAtual(),   
																DocumentoEntradaServices.ST_EM_ABERTO /*stDocumentoEntrada*/,
																DocumentoEntradaServices.TP_NOTA_FISCAL /*tpDocumentoEntrada*/, 
											                    DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO/*tpEntrada*/, cdNaturezaOperacaoRetornoNormal,
											                    DocumentoEntradaServices.MOV_ESTOQUE_CONSIGNADO,
											                    cdMoeda, cdDigitador,1 /*nrSerie*/, documentoRemessaPrimeiro.getCdViagem());
			int cdDocumentoEntrada = DocumentoEntradaDAO.insert(docEntrada, connect);
			if (cdDocumentoEntrada <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Falha ao criar documento de entrada a partir da venda externa!");
			}
			
			//Valor total de produtos que será do documento de retorno
			float vlTotalProduto   = 0;
			
			//Array de codigos de produto de servico ja registrados
			ArrayList<Integer> codigosProdutoServico = new ArrayList<Integer>();
			
			//Itera sobre os documentos de remessa para criar o documento de retorno inicial que será igual, em relação aos itens e total, aos documentos de remessa
			for(int cdDocumentoRemessa : codigosDocumentoRemessa){
				//Busca todos os itens do documento de remessa
				ResultSetMap rsmItens = DocumentoSaidaServices.getAllItens(cdDocumentoRemessa, connect);
				while(rsmItens.next())	{
					//Busca o codigo do produto e a quantidade de saida do item em questao
					int cdProdutoServico = rsmItens.getInt("cd_produto_servico");
					float qtSaidaRemessa  = rsmItens.getFloat("qt_saida");
					
					//Caso o codigo ja esteja no array, será alterado apenas a quantidade, sendo acrescentado no item do documento de retorno
					if(codigosProdutoServico.contains(cdProdutoServico)){
						//Busca o item deste produto que ja existe no retorno
						DocumentoEntradaItem item = DocumentoEntradaItemDAO.get(cdDocumentoEntrada, cdProdutoServico, cdEmpresa, rsmItens.getInt("cd_item"), connect);
						
						//Retira o valor do total de produtos para a atualização da quantidade de entrada
						vlTotalProduto -= (item.getVlUnitario() * item.getQtEntrada() + item.getVlAcrescimo() - item.getVlDesconto());
						
						//Atualiza a quantidade de entrada
						item.setQtEntrada(item.getQtEntrada() + qtSaidaRemessa);
						
						//Atualiza o item
						if(DocumentoEntradaItemDAO.update(item, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar item de retorno");
						}
						
						//Coloca o valor do total de produtos novamente com a quantidade nova
						vlTotalProduto += (item.getVlUnitario() * item.getQtEntrada() + item.getVlAcrescimo() - item.getVlDesconto());
					}
					//Caso o codigo nao esteja no array, o item com esse produto será criado com as informações do item do documento de remessa
					else{
						
						//Lista alguns valores do item do documento de remessa para inserir no documento de retorno
						float vlUnitario      = rsmItens.getFloat("vl_unitario");
						float vlAcrescimo     = rsmItens.getFloat("vl_acrescimo");
						float vlDesconto      = rsmItens.getFloat("vl_desconto");
						int cdUnidadeMedida   = rsmItens.getInt("cd_unidade_medida");
						int cdNaturezaOperacaoItem = cdNaturezaOperacaoRetornoNormal;
						
						if(rsmItens.getInt("cd_classificacao_fiscal") == ParametroServices.getValorOfParametroAsInteger("CD_SUBSTITUICAO_TRIBUTARIA", 0)){
							cdNaturezaOperacaoItem = cdNaturezaOperacaoRetornoSubstituicao;
						}
						
						//Insere o documento de retorno com as informações da remessa
						if (DocumentoEntradaItemServices.insert(new DocumentoEntradaItem(cdDocumentoEntrada, cdProdutoServico,
														cdEmpresa, qtSaidaRemessa /*qtEntrada*/,
														vlUnitario,vlAcrescimo, vlDesconto,cdUnidadeMedida,
														null /*dtEntregaPrevista*/, cdNaturezaOperacaoItem/*cdNaturezaOperacao*/, 0/*cdAdicao*/, 0/*cdItem*/, 0/*vlVucv*/ , 0/*vlDescontoGeral*/, 0/*cdTipoCredito*/), cdLocalArmazenamento, true, connect).getCode() <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Falha ao incluir item na entrada!");
						}
						
						//Acrescenta o valor do item no valor total de produtos do retorno
						vlTotalProduto += rsmItens.getFloat("vl_unitario") * rsmItens.getFloat("qt_saida") + rsmItens.getFloat("vl_acrescimo") - rsmItens.getFloat("vl_desconto");
						
						//Acrescenta no array de codigos de produto ja cadastrado o produto do novo item
						codigosProdutoServico.add(cdProdutoServico);
					}
					
				}
			}
			
			//Itera sobre as notas de venda, para retirar os itens e quantidades que foram vendidos
			for(int cdDocumentoVenda : codigosDocumentoSaida){
				//A cada venda, um documento de entrada irá ser criada para balancear o estoque
				DocumentoEntrada docEntradaRetSup = new DocumentoEntrada(0 /*cdDocumentoEntrada*/, cdEmpresa, cdTransportadora, cdEmpresa, Util.getDataAtual(), Util.getDataAtual(),
										DocumentoEntradaServices.ST_EM_ABERTO /*stDocumentoEntrada*/,
										DocumentoEntradaServices.TP_DOC_NAO_FISCAL /*tpDocumentoEntrada*/, 
					                    DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO/*tpEntrada*/, cdNaturezaOperacaoRetornoNormal,
					                    DocumentoEntradaServices.MOV_ESTOQUE_CONSIGNADO,
					                    cdMoeda, cdDigitador,1 /*nrSerie*/, documentoRemessaPrimeiro.getCdViagem());
				int cdDocumentoEntradaRetSup = DocumentoEntradaDAO.insert(docEntradaRetSup, connect);
				docEntradaRetSup.setCdDocumentoEntrada(cdDocumentoEntradaRetSup);
				if (cdDocumentoEntradaRetSup <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Falha ao criar documento de entrada a partir da venda externa!");
				}
				float vlTotalItemSuplementar = 0;
				
				//Busca todos os itens do documento de venda
				ResultSetMap rsmItens = DocumentoSaidaServices.getAllItens(cdDocumentoVenda, connect);
				while(rsmItens.next())	{
					int cdProdutoServico = rsmItens.getInt("cd_produto_servico");
					float qtSaida = rsmItens.getFloat("qt_saida");
					//Busca o item do documento de retorno referente ao produto serviço que foi vendido
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_documento_entrada", "" + cdDocumentoEntrada, ItemComparator.IN, Types.INTEGER));
					criterios.add(new ItemComparator("cd_produto_servico", "" + cdProdutoServico, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_empresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmItem = DocumentoEntradaItemDAO.find(criterios, connect);
					DocumentoEntradaItem item = null;
					if(rsmItem.next()){
						item = DocumentoEntradaItemDAO.get(cdDocumentoEntrada, cdProdutoServico, cdEmpresa, rsmItem.getInt("cd_item"), connect);
					}
					//Testa caso haja um produto que não tenha na nota de remessa ser vendido nessa viagem
					else{
						ProdutoServico produtoServico = ProdutoServicoDAO.get(cdProdutoServico, connect);
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro: Produto "+produtoServico.getNmProdutoServico()+" não está presente na nota de remessa.");
					}
					//Retira o valor do total de produtos para a atualização da quantidade de entrada
					vlTotalProduto -= (item.getVlUnitario() * item.getQtEntrada() + item.getVlAcrescimo() - item.getVlDesconto());
					
					//Atualiza a quantidade de entrada
					item.setQtEntrada(item.getQtEntrada() - qtSaida);
					
					//Coloca o valor do total de produtos novamente com a quantidade nova
					vlTotalProduto += (item.getVlUnitario() * item.getQtEntrada() + item.getVlAcrescimo() - item.getVlDesconto());
					
					//Atualiza o item
					if(DocumentoEntradaItemDAO.update(item, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar item de retorno");
					}
					
					//Atualiza a entrada local item
					ArrayList<ItemComparator> criteriosEntradaLocalItem = new ArrayList<ItemComparator>();
					criteriosEntradaLocalItem.add(new ItemComparator("cd_produto_servico", "" + cdProdutoServico, ItemComparator.EQUAL, Types.INTEGER));
					criteriosEntradaLocalItem.add(new ItemComparator("cd_documento_entrada", "" + cdDocumentoEntrada, ItemComparator.EQUAL, Types.INTEGER));
					criteriosEntradaLocalItem.add(new ItemComparator("cd_empresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
					criteriosEntradaLocalItem.add(new ItemComparator("cd_local_armazenamento", "" + cdLocalArmazenamento, ItemComparator.EQUAL, Types.INTEGER));
					criteriosEntradaLocalItem.add(new ItemComparator("cd_item", "" + item.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmEntradaLocalItem = EntradaLocalItemDAO.find(criteriosEntradaLocalItem, connect);
					while(rsmEntradaLocalItem.next()){
						EntradaLocalItem entradaLocalItem = EntradaLocalItemDAO.get(cdProdutoServico, cdDocumentoEntrada, cdEmpresa, cdLocalArmazenamento, rsmEntradaLocalItem.getInt("cd_entrada_local_item"), item.getCdItem(), connect);
						entradaLocalItem.setQtEntradaConsignada(entradaLocalItem.getQtEntradaConsignada() - qtSaida);
						if(EntradaLocalItemDAO.update(entradaLocalItem, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar local item de retorno");
						}
					}
					
					//Lista alguns valores do item do documento de retorno suplementar
					float vlUnitario      = item.getVlUnitario();
					float vlAcrescimo     = item.getVlAcrescimo();
					float vlDesconto      = item.getVlDesconto();
					int cdUnidadeMedida   = item.getCdUnidadeMedida();
					int cdNaturezaOperacaoItem = cdNaturezaOperacaoRetornoNormal;
					
					if(rsmItens.getInt("cd_classificacao_fiscal") == ParametroServices.getValorOfParametroAsInteger("CD_SUBSTITUICAO_TRIBUTARIA", 0)){
						cdNaturezaOperacaoItem = cdNaturezaOperacaoRetornoSubstituicao;
					}
					
					
					//Insere o item documento de retorno suplementar
					if (DocumentoEntradaItemServices.insert(new DocumentoEntradaItem(cdDocumentoEntradaRetSup, cdProdutoServico,
													cdEmpresa, qtSaida /*qtEntrada*/,
													vlUnitario,vlAcrescimo, vlDesconto,cdUnidadeMedida,
													null /*dtEntregaPrevista*/, cdNaturezaOperacaoItem/*cdNaturezaOperacao*/, 0/*cdAdicao*/, 0/*cdItem*/, 0/*vlDescontoGeral*/, 0/*vlVucv*/, 0/*cdTipoCredito*/), cdLocalArmazenamento, true, connect).getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Falha ao incluir item na entrada!");
					}
					
					//Soma o total de itens suplementar
					vlTotalItemSuplementar += (vlUnitario * qtSaida + vlAcrescimo - vlDesconto);
					
					//Caso a quantidade do item seja de 0, significa que todos os produtos desse item foram vendidos, logo não será lançado no documento de retorno
					if(item.getQtEntrada() == 0){
						if(DocumentoEntradaItemDAO.delete(cdDocumentoEntrada, cdProdutoServico, cdEmpresa, item.getCdItem(), connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao deletar produto do retorno.");
						}
					}
					//Caso a quantidade do item seja menor do que 0, significa que foi vendido uma quantidade maior do que a mandada na remessa, logo alguma informação deve estar
					//errada
					else if(item.getQtEntrada() < 0){
						
						ProdutoServico produtoServico = ProdutoServicoDAO.get(item.getCdProdutoServico(), connect);
						
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro: Produto "+produtoServico.getNmProdutoServico()+" vendido em quantidade maior do que foi mandado.");
					}
				}
				
				//Deleta o documento de entrada suplementar caso ele não tenha nenhum item
				if(getAllItens(cdDocumentoEntradaRetSup, connect).size() < 0){
					if(delete(cdDocumentoEntradaRetSup, connect).getCode() <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao deletar documento de entrada suplementar");
					}
				}
				//Libera o documento caso contrario
				else{
					docEntradaRetSup.setVlTotalDocumento(vlTotalItemSuplementar);
					docEntradaRetSup.setVlTotalItens(vlTotalItemSuplementar);
					if(DocumentoEntradaDAO.update(docEntradaRetSup, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar documento de entrada suplementar");
					}
					
					Result resultado = liberarEntrada(cdDocumentoEntradaRetSup, connect);
					if(resultado.getCode() <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao liberar documento de entrada suplementar");
					}
				}
			}
			//Caso o valor total de produto seja 0, significa que todos os produtos foram vendidos, e não será necessário uma nota de retorno
			if(vlTotalProduto == 0){
				if(delete(cdDocumentoEntrada, connect).getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao deletar nota de retorno.");
				}
				
				return new Result(RET_SEM_RETORNO, "Não será necessário nota de retorno. Todos os produtos foram vendidos");
			}
			else if(vlTotalProduto < 0){
				
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao calcular do total da nota de retorno.");
			}
			else{
				//Atualiza os valores do documento de entrada
				docEntrada.setVlTotalDocumento(vlTotalProduto);
				docEntrada.setVlTotalItens(vlTotalProduto);
				
				if(DocumentoEntradaDAO.update(docEntrada, connect) < 0){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Falha ao incluir documento de retorno!");
				}
//				Result resultado = liberarEntrada(cdDocumentoEntrada, connect);
//				//Libera o documento de retorno
//				if(resultado.getCode() <= 0){
//					System.out.println("Retorno: " + resultado.getMessage());
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(-1, "Erro ao liberar documento de entrada de retorno");
//				}
			}
			
			
			//Lança a nota fiscal da nota de retorno
			ResultSetMap rsmDocVinculado = NotaFiscalDocVinculadoServices.findByDocSaida(documentoRemessaPrimeiro.getCdDocumentoSaida());
			NotaFiscal nota = null;
			boolean achou = false;
			//Percorre as notas de remessa e procura uma que tenha nota fiscal eletronica
			while(rsmDocVinculado.next()){
				nota = NotaFiscalDAO.get(rsmDocVinculado.getInt("cd_nota_fiscal"));
				if(nota != null){
					achou = true;
					NotaFiscalServices.fromDocEntradaToNF(cdDocumentoEntrada, NotaFiscalServices.NO_PRECO, nota.getPrDesconto(), connect);
					break;
				}
			}
			
			//Caso não ache, faz a nota fiscal eletronica sem desconto
			if(!achou){
				NotaFiscalServices.fromDocEntradaToNF(cdDocumentoEntrada, 0, 0, connect);
			}
			
			Viagem viagem = ViagemDAO.get(documentoRemessaPrimeiro.getCdViagem(), connect);
			viagem.setStViagem(ViagemServices.ST_CHEGADA);
			if(ViagemDAO.update(viagem, connect) < 0){
				return new Result(-1, "Falha ao atualizar a viagem");
			}
			
			if (isConnectionNull)
				connect.commit();
			return new Result(cdDocumentoEntrada); 
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar criar entrada a partir da transferência!", e);
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static int insertEventoFinanceiro(int cdDocumentoEntrada, int cdEventoFinanceiro, float vlEventoFinanceiro, int lgCusto) {
		return insertEventoFinanceiro(cdDocumentoEntrada, cdEventoFinanceiro, vlEventoFinanceiro, lgCusto, null);
	}

	public static int insertEventoFinanceiro(int cdDocumentoEntrada, int cdEventoFinanceiro, float vlEventoFinanceiro, int lgCusto, Connection connection)	{
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("INSERT INTO adm_entrada_evento_financeiro (cd_documento_entrada,"+
											                    "cd_evento_financeiro,"+
											                    "vl_evento_financeiro,"+
											                    "lg_custo) VALUES (?, ?, ?, ?)");
			
			pstmt.setInt  (1, cdDocumentoEntrada);
			pstmt.setInt  (2, cdEventoFinanceiro);
			pstmt.setFloat(3, vlEventoFinanceiro);
			pstmt.setInt(4, lgCusto);
			
			pstmt.executeUpdate();
			
			if (isConnectionNull)
				connection.commit();
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static int deleteEventoFinanceiro(int cdDocumentoEntrada, int cdEventoFinanceiro) {
		return deleteEventoFinanceiro(cdDocumentoEntrada, cdEventoFinanceiro, null);
	}

	public static int deleteEventoFinanceiro(int cdDocumentoEntrada, int cdEventoFinanceiro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_entrada_evento_financeiro WHERE cd_documento_entrada=? AND cd_evento_financeiro=?");
			pstmt.setInt(1, cdDocumentoEntrada);
			pstmt.setInt(2, cdEventoFinanceiro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaServices.deleteEventoFinanceiro: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaServices.deleteEventoFinanceiro: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int updateEventoFinanceiro(int cdDocumentoEntrada, int cdEventoFinanceiro, float vlEventoFinanceiro, int lgCusto) {
		return updateEventoFinanceiro(cdDocumentoEntrada, cdEventoFinanceiro, vlEventoFinanceiro, lgCusto, null);
	}

	public static int updateEventoFinanceiro(int cdDocumentoEntrada, int cdEventoFinanceiro, float vlEventoFinanceiro, int lgCusto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_entrada_evento_financeiro SET vl_evento_financeiro=?,"+
												      		   "lg_custo=?,"+
												      		   " WHERE cd_documento_entrada=? AND cd_evento_financeiro=?");
			pstmt.setInt(1,cdDocumentoEntrada);
			pstmt.setInt(2,cdEventoFinanceiro);
			pstmt.setFloat(3,vlEventoFinanceiro);
			pstmt.setInt(4,lgCusto);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaServices.updateEventoFinanceiro: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaServices.updateEventoFinanceiro:: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllEventoFinanceiro(int cdDocumentoEntrada) {
		return getAllEventoFinanceiro(cdDocumentoEntrada, null);
	}

	public static ResultSetMap getAllEventoFinanceiro(int cdDocumentoEntrada, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					 "SELECT B.*, A.nm_evento_financeiro FROM adm_evento_financeiro A " +
					 " JOIN adm_entrada_evento_financeiro  B ON (A.cd_evento_financeiro = B.cd_evento_financeiro AND B.cd_documento_entrada = "+cdDocumentoEntrada+") ");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("CL_FISCAL", (rsm.getInt("LG_FISCAL") == 1 ? "Sim" : "Não"));
				rsm.setValueToField("CL_DESPESA_ACESSORIA", (rsm.getInt("LG_DESPESA_ACESSORIA") == 1 ? "Sim" : "Não"));
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
				Conexao.desconectar(connect);
		}
	}
	
	private static byte[] correcaoXML(byte[] fileNFE) {
		try{
			String texto = new String(fileNFE, "utf-8");
			texto = NfeServices.removeAcentos(texto);
			while(texto.charAt(0) != '<')
				texto = texto.substring(1);
			return texto.getBytes();
		}
		catch(Exception e){e.printStackTrace();return null;}
	}

	
	public static Result importFromNFE(int cdEmpresa, byte[] fileNFE) {
		try {
			Connection connect = Conexao.conectar();
			if(connect==null)
				System.out.println("Conexão nula");
			System.out.println("loadFromXML");
			fileNFE = correcaoXML(fileNFE);
			Result resultado = new Result(1);
			SAXBuilder saxObj = new SAXBuilder();
			Document doc      = saxObj.build(new InputStreamReader(new ByteArrayInputStream(fileNFE)));
			Namespace ns      = Namespace.getNamespace("http://www.portalfiscal.inf.br/nfe");
	    	Element infNFe    = (Element)doc.getRootElement().getChild("NFe", ns).getChild("infNFe", ns);
	    	// String idNfe        = infNFe.getAttributeValue("Id");     
            // String versaoNfe    = infNFe.getAttributeValue("versao");
	    	
	    	
	    	PreparedStatement pesqEmissor  				 = connect.prepareStatement("SELECT * FROM grl_pessoa_juridica WHERE nr_cnpj = ?");
	    	PreparedStatement pesqNota  				 = connect.prepareStatement("SELECT * FROM alm_documento_entrada WHERE nr_documento_entrada = ? AND cd_fornecedor = ? AND cd_empresa = ?");
	    	PreparedStatement pesqNatOper  			 	 = connect.prepareStatement("SELECT * FROM adm_natureza_operacao WHERE nr_codigo_fiscal = ?");
	    	PreparedStatement pesqCidade  			     = connect.prepareStatement("SELECT * FROM grl_cidade WHERE cd_cidade = ?");

	    	
	    	PreparedStatement insertCidade  			 = connect.prepareStatement("INSERT into grl_cidade (cd_cidade, nm_cidade) VALUES (?, ?)");
	    	
	    	// Emissor
	    	Element emissor = (Element) infNFe.getChild("emit", ns);
	    	int cdEmissor = 0;
	    	if(emissor!=null)	{
	    		pesqEmissor.setString(1, emissor.getChild("CNPJ", ns).getValue());
				ResultSet rsE = pesqEmissor.executeQuery();
				if(rsE.next())
					cdEmissor = rsE.getInt("cd_pessoa");
				else{					
					String nrCnpj              = (emissor.getChild("CNPJ", ns) != null  ? emissor.getChild("CNPJ", ns).getValue() : "");
					String nome                = (emissor.getChild("xNome", ns) != null ? emissor.getChild("xNome", ns).getValue() : "");
					String nomeFantasia        = (emissor.getChild("xFant", ns) != null ? emissor.getChild("xFant", ns).getValue() : "");
					String nrInscricaoEstadual = (emissor.getChild("IE", ns) != null    ? emissor.getChild("IE", ns).getValue() : "");
					// String CRT                 = emissor.getChild("CRT", ns).getValue();
					
					
					String nmLogradouro = "";
					String numero = "";
					String nmBairro = "";
					int cdMunicipio = 0;
					String nmMunicipio = "";
					// String uf           = emissor.getChild("enderEmit", ns).getChild("UF", ns).getValue();
					String cep = "";
					int cdPais          = 1;
					// String nmPais       = "BRASIL";
					String telefone = emissor.getChild("enderEmit", ns).getChild("fone", ns).getValue();
					
					if(emissor.getChild("enderEmit", ns) != null){
						nmLogradouro = (emissor.getChild("enderEmit", ns).getChild("xLgr", ns) != null ? emissor.getChild("enderEmit", ns).getChild("xLgr", ns).getValue() : "");
						numero       = (emissor.getChild("enderEmit", ns).getChild("nro", ns) != null ? emissor.getChild("enderEmit", ns).getChild("nro", ns).getValue() : "");
						nmBairro     = (emissor.getChild("enderEmit", ns).getChild("xBairro", ns) != null ? emissor.getChild("enderEmit", ns).getChild("xBairro", ns).getValue() : "");
						cdMunicipio  = Integer.parseInt((emissor.getChild("enderEmit", ns).getChild("cMun", ns) != null ? emissor.getChild("enderEmit", ns).getChild("cMun", ns).getValue() : "0"));
						nmMunicipio  = (emissor.getChild("enderEmit", ns).getChild("xMun", ns) != null ? emissor.getChild("enderEmit", ns).getChild("xMun", ns).getValue() : "");
						// String uf           = emissor.getChild("enderEmit", ns).getChild("UF", ns).getValue();
						cep          = (emissor.getChild("enderEmit", ns).getChild("CEP", ns) != null ? emissor.getChild("enderEmit", ns).getChild("CEP", ns).getValue() : "");
						// String nmPais       = "BRASIL";
						telefone     = (emissor.getChild("enderEmit", ns).getChild("fone", ns) != null ? emissor.getChild("enderEmit", ns).getChild("fone", ns).getValue() : "");
					}
					
					com.tivic.manager.grl.PessoaJuridica pj = new com.tivic.manager.grl.PessoaJuridica(0, 0, cdPais, nomeFantasia, null, null, null, null, null, null, PessoaServices.JURIDICA, null, 0, null, null, null, 0, null, 0, 0, null, nrCnpj, nome, nrInscricaoEstadual, null, 0, null, 0, 0, null);
					cdEmissor = com.tivic.manager.grl.PessoaJuridicaDAO.insert(pj, connect);
					if(cdEmissor <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Fornecedor!");
					}
					
					pesqCidade.setInt(1, cdMunicipio);
					ResultSet rsC = pesqCidade.executeQuery();
					
					if(!rsC.next()){
						insertCidade.setInt(1, cdMunicipio);
						insertCidade.setString(2, nmMunicipio);
						if(insertCidade.executeUpdate() <= 0){
							Conexao.rollback(connect);
							return new Result(-1, "Erro ao cadastrar cidade do Fornecedor!");
						}
					}
					
					int cdEnderecoEmissor = com.tivic.manager.grl.PessoaEnderecoDAO.insert(new PessoaEndereco(0, cdEmissor, null, 0, 0, 0, 0, cdMunicipio, nmLogradouro, nmBairro, cep, numero, null, telefone, null, 0, 0), connect);
					
					if(cdEnderecoEmissor <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar Endereco do Fornecedor!");
					}
				}
				
			}
	    		    	
	    	float vProd = 0, vFrete = 0, vSeg = 0, vDesc = 0, vNF = 0;
	    	
	    	// Total da NFe
            Element total   = (Element) infNFe.getChild("total", ns);
            Element ICMSTot = (Element) total.getChild("ICMSTot", ns);
            if(ICMSTot!=null)	{
            	vProd  = Float.parseFloat(ICMSTot.getChild("vProd", ns).getValue());
    	    	vFrete = Float.parseFloat(ICMSTot.getChild("vFrete", ns).getValue());
    	    	vSeg   = Float.parseFloat(ICMSTot.getChild("vSeg", ns).getValue());
    	    	vDesc  = Float.parseFloat(ICMSTot.getChild("vDesc", ns).getValue());
    	    	vNF    = Float.parseFloat(ICMSTot.getChild("vNF", ns).getValue());
            }
	    	
	    	// Identificação da NFe
            Element ide  = (Element) infNFe.getChild("ide", ns);
            int cdNota = 0;
            String numeroNota = ide.getChild("nNF", ns).getValue();
            int nrSerie       = 1;
            try {
            	String value = ide.getChild("nSerie", ns) != null ? ide.getChild("nSerie", ns).getValue() : "";
				if(!"".equals(value.trim()))
						nrSerie = Integer.valueOf(value);
            }
            catch(Exception e){
            	e.printStackTrace();
            }

            int codNaturezaOperacao = 0;
            String nr_codigo_fiscal = ide.getChild("natOp", ns).getValue();
            //Codigo para buscar o numero de codigo fiscal da nota
            pesqNatOper.setString(1, nr_codigo_fiscal);
            ResultSet rsNO = pesqNatOper.executeQuery();
            
            //buscar data de emissao
            String data = ide.getChild("dEmi", ns) != null ? ide.getChild("dEmi", ns).getValue() : "";
            GregorianCalendar dtEmissao = new GregorianCalendar();
			if(!"".equals(data))
				dtEmissao = convStringToCalendar(data);
            
            //buscar data do documento de entrada
            //String data2 = String.valueOf(ide.getChild("dSaiEnt", ns));
            //System.out.println("data2: " + data2);
            GregorianCalendar dtDocEnt = Util.getDataAtual();
            
            if(rsNO.next())
            	codNaturezaOperacao = rsNO.getInt("cd_natureza_operacao");
            
            if(ide!=null)	{
            	pesqNota.setString(1, numeroNota);
            	pesqNota.setInt(2, cdEmissor);
            	pesqNota.setInt(3, cdEmpresa);
            	ResultSet rsN = pesqNota.executeQuery();
				if(rsN.next())
					cdNota = rsN.getInt("cd_documento_entrada");
				else	{
					
					com.tivic.manager.alm.DocumentoEntrada docEnt = new com.tivic.manager.alm.DocumentoEntrada(0, cdEmpresa, 0, cdEmissor, dtEmissao, dtDocEnt, 0, vDesc, 0, numeroNota, DocumentoEntradaServices.TP_NOTA_FISCAL, null, DocumentoEntradaServices.ENT_COMPRA, null, codNaturezaOperacao, 0, null, null, 0, null, null, null, 0, 0, null, null, null, 0, 0, 0, vProd, 0, vFrete, vSeg, 0, vProd, nrSerie);
					cdNota = com.tivic.manager.alm.DocumentoEntradaDAO.insert(docEnt, connect);
					if(cdNota <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar o Documento de Entrada!");
					}
				}
								
            }
            
            ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
    		criterios.add(new ItemComparator("A.CD_DOCUMENTO_ENTRADA", "" + cdNota, ItemComparator.EQUAL, Types.INTEGER));
    		ResultSetMap rsmDocEntrada = DocumentoEntradaServices.find(criterios);
    		resultado.addObject("rsmDoc", rsmDocEntrada.getLines());
    		
    		
            // Destinatário
//			Element dest  = (Element) infNFe.getChild("dest", ns);
//			if(dest!=null)	{
//				// String nrCpfCnpjDest = dest.getChild("CNPJ", ns).getText();
//			}
			
    		List<?> itens = infNFe.getChildren("det", ns);
			/*
			 * IMPORTAÇÃO
			 */
			/***********************
			 * Importando produtos
			 ***********************/
			
			PreparedStatement pesqProdutoServicoCd = connect.prepareStatement("SELECT A.*, B.* FROM grl_produto_servico A, grl_produto_servico_empresa B " +
															                  "WHERE A.cd_produto_servico = ? "+
																			  "AND A.cd_produto_servico = B.cd_produto_servico  ");
			PreparedStatement pesqNcm  			 = connect.prepareStatement("SELECT * FROM grl_ncm WHERE nr_ncm = ?");
			PreparedStatement pesqUnidade  		 = connect.prepareStatement("SELECT * FROM grl_unidade_medida WHERE UPPER(sg_unidade_medida) = ?");
			PreparedStatement pesqGrupo    		 = connect.prepareStatement("SELECT * FROM alm_grupo WHERE id_grupo = ?");
			
			ResultSetMap rsmProd = new ResultSetMap();
			for(int i=0; i<itens.size(); i++)	{
				
				Element xlmProduto   = ((Element)itens.get(i)).getChild("prod", ns);
				//
				String idReduzido 	  = xlmProduto.getChildText("cProd", ns);
				String nrCodigoBarras = xlmProduto.getChildText("cEAN", ns);
				String nmProduto      = xlmProduto.getChildText("xProd", ns);
				nmProduto = nmProduto!=null ? nmProduto.replaceAll("\"", "") : "";
				nmProduto = nmProduto!=null ? nmProduto.replaceAll("&quot;", "") : "";
				nmProduto = nmProduto!=null ? nmProduto.replaceAll("\'", "") : "";
				
				String nrNcm 		  = xlmProduto.getChildText("NCM", ns);
				//String nrCFOP 		= xlmProduto.getChildText("CFOP", ns);
				String sgUnidade      = xlmProduto.getChildText("uCom", ns);
				String qtEntrada      = xlmProduto.getChildText("qCom", ns);
				String vlUnitario     = xlmProduto.getChildText("vUnCom", ns);
				String idGrupo        = "";
				if(idReduzido!=null)	{
					idReduzido = idReduzido.replaceAll("\\.", "-");
					if(idReduzido.indexOf('-')>0)
						idGrupo = idReduzido.substring(idReduzido.indexOf('-')+1);
					// idReduzido = idReduzido.replaceAll("-", "");
				}
				// Unidade de Medida
				int cdUnidadeMedida = 0;
				String nmUnidadeMedida = "";
				if (sgUnidade!=null)	{
					pesqUnidade.setString(1, sgUnidade.toUpperCase());
					ResultSet rsT = pesqUnidade.executeQuery();
					if(rsT.next()){
						cdUnidadeMedida = rsT.getInt("cd_unidade_medida");
						nmUnidadeMedida = rsT.getString("nm_unidade_medida");
					}
					else{
						com.tivic.manager.grl.UnidadeMedida unidadeMedida = new com.tivic.manager.grl.UnidadeMedida(0, sgUnidade, sgUnidade, null, 0, 1);
						cdUnidadeMedida = com.tivic.manager.grl.UnidadeMedidaDAO.insert(unidadeMedida, connect);
						nmUnidadeMedida = sgUnidade;
						if(cdUnidadeMedida <= 0){
							Conexao.rollback(connect);
							return new Result(-1, "Erro ao cadastrar o Unidade de Medida!");
						}
					}
				}
				// Grupo
				int cdGrupo = 0;
				String nmGrupo = "";
				if (!idGrupo.equals(""))	{
					pesqGrupo.setString(1, idGrupo);
					ResultSet rsT = pesqGrupo.executeQuery();
					if(rsT.next()){
						cdGrupo = rsT.getInt("cd_grupo");
						nmGrupo = rsT.getString("nm_grupo");
					}
					else{
						com.tivic.manager.alm.Grupo grupo = new com.tivic.manager.alm.Grupo(0,0,0,0,"GRUPO "+idGrupo,0,0,0,1,idGrupo);
						cdGrupo = com.tivic.manager.alm.GrupoDAO.insert(grupo, connect);
						nmGrupo = "GRUPO "+idGrupo;
						if(cdGrupo <= 0){
							Conexao.rollback(connect);
							return new Result(-1, "Erro ao cadastrar o Grupo!");
						}
					}
						
				}
				// NCM
				int cdNcm = 0;
				String nmNcm = "";
				if (nrNcm!=null)	{
					pesqNcm.setString(1, nrNcm.toUpperCase());
					ResultSet rsN = pesqNcm.executeQuery();
					if(rsN.next()){
						cdNcm = rsN.getInt("cd_ncm");
						nmNcm = rsN.getString("nm_ncm");
					}
					else	{
						com.tivic.manager.grl.Ncm ncm = new com.tivic.manager.grl.Ncm(0, nrNcm, cdUnidadeMedida, nrNcm);
						cdNcm = com.tivic.manager.grl.NcmDAO.insert(ncm, connect);
						nmNcm = nrNcm;
						if(cdNcm <= 0){
							Conexao.rollback(connect);
							return new Result(-1, "Erro ao cadastrar o NCM!");
						}
					}
						
				}
				ResultSetMap rsProd     = null;
				ResultSet    rsProdCd   = null;
				ResultSetMap rsProdForn = null;
				PreparedStatement pesqProdutoServico = connect.prepareStatement("SELECT * FROM grl_produto_servico A, grl_produto_servico_empresa B " +
														                        "WHERE " + (!nrCodigoBarras.equals("") || nrCodigoBarras != null || !idReduzido.equals("") || idReduzido != null ? "("+(!nrCodigoBarras.equals("") && nrCodigoBarras != null ? "id_produto_servico = '"+nrCodigoBarras+"' OR " : "" ) + (!idReduzido.equals("") && idReduzido != null ? " id_reduzido = '"+idReduzido+"'" : "")+") "+
																				"  AND " : "") +  "A.cd_produto_servico = B.cd_produto_servico ");
				
				PreparedStatement pesqProdutoFornecedor = connect.prepareStatement("SELECT * FROM adm_produto_fornecedor " +
																				   "WHERE id_produto = ? AND cd_fornecedor = ?");
				pesqProdutoFornecedor.setString(1, idReduzido);
				pesqProdutoFornecedor.setInt(2, cdEmissor);
				
				rsProd = new ResultSetMap(pesqProdutoServico.executeQuery());
				rsProdForn = new ResultSetMap(pesqProdutoFornecedor.executeQuery());
				// Incluindo
				if(!rsProdForn.next())	{
					if(!rsProd.next()){//Cairá aqui quando não houver nem referencia ao id em produto_fornecedor, nem um produto cadastrado com esse id e código de barras
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("CD_PRODUTO_SERVICO", 0);
						register.put("CD_NCM", cdNcm);
						register.put("NM_NCM", nmNcm);
						register.put("NM_PRODUTO_SERVICO", nmProduto);
						register.put("NM_PRODUTO_SERVICO_ANTIGO", nmProduto);
						register.put("CD_GRUPO", cdGrupo);
						register.put("NM_GRUPO", nmGrupo);
						register.put("CD_UNIDADE_MEDIDA", cdUnidadeMedida);
						register.put("NM_UNIDADE_MEDIDA", nmUnidadeMedida);
						register.put("ID_REDUZIDO", idReduzido);
						register.put("ID_REDUZIDO_ANTIGO", idReduzido);
						register.put("ID_PRODUTO_FORNECEDOR", "");
						register.put("NR_CODIGO_BARRAS", nrCodigoBarras);
						register.put("VL_UNITARIO", vlUnitario);
						register.put("QT_PRECISAO_CUSTO", 2);
						register.put("QT_ENTRADA", qtEntrada);
						register.put("CL_VALIDA", 0);
						rsmProd.addRegister(register);
					}
					else{//Cairá aqui quando não haja uma referencia desse produto em produto_fornecedor, mas ele esta cadastrado no sistema
						int cdProdutoServico = rsProd.getInt("cd_produto_servico");
						
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("CD_PRODUTO_SERVICO", cdProdutoServico);
						register.put("CD_NCM", cdNcm);
						register.put("NM_NCM", nmNcm);
						register.put("NM_PRODUTO_SERVICO", rsProd.getString("nm_produto_servico"));
						register.put("NM_PRODUTO_SERVICO_ANTIGO", nmProduto);
						register.put("CD_GRUPO", cdGrupo);
						register.put("NM_GRUPO", nmGrupo);
						register.put("CD_UNIDADE_MEDIDA", cdUnidadeMedida);
						register.put("NM_UNIDADE_MEDIDA", nmUnidadeMedida);
						register.put("ID_REDUZIDO", rsProd.getString("id_reduzido"));
						register.put("ID_REDUZIDO_ANTIGO", rsProd.getString("id_reduzido"));
						register.put("ID_PRODUTO_FORNECEDOR", "");
						register.put("NR_CODIGO_BARRAS", rsProd.getString("id_produto_servico"));
						register.put("VL_UNITARIO", vlUnitario);
						register.put("QT_PRECISAO_CUSTO", 2);
						register.put("QT_ENTRADA", qtEntrada);
						register.put("CL_VALIDA", 1);
						rsmProd.addRegister(register);
					}
				}
				else{
					if(!rsProd.next()){//Cairá aqui quando houver referencia a esse produto em produto_fornecedor mas ele nao tiver sido cadastrado no sistema (Ele foi relacionado com algum outro produto do banco)
						int cdProdutoServico = rsProdForn.getInt("cd_produto_servico");
						pesqProdutoServicoCd.setInt(1, cdProdutoServico);
						rsProdCd = pesqProdutoServicoCd.executeQuery();
						if(rsProdCd.next()){
							HashMap<String, Object> register = new HashMap<String, Object>();
							register.put("CD_PRODUTO_SERVICO", cdProdutoServico);
							register.put("CD_NCM", cdNcm);
							register.put("NM_NCM", nmNcm);
							register.put("NM_PRODUTO_SERVICO", rsProdCd.getString("nm_produto_servico"));
							register.put("NM_PRODUTO_SERVICO_ANTIGO", nmProduto);
							register.put("CD_GRUPO", cdGrupo);
							register.put("NM_GRUPO", nmGrupo);
							register.put("CD_UNIDADE_MEDIDA", cdUnidadeMedida);
							register.put("NM_UNIDADE_MEDIDA", nmUnidadeMedida);
							register.put("ID_REDUZIDO", rsProdCd.getString("id_reduzido"));
							register.put("ID_REDUZIDO_ANTIGO", idReduzido);
							register.put("ID_PRODUTO_FORNECEDOR", rsProdForn.getString("id_produto"));
							register.put("NR_CODIGO_BARRAS", rsProdCd.getString("id_produto_servico"));
							register.put("VL_UNITARIO", vlUnitario);
							register.put("QT_PRECISAO_CUSTO", 2);
							register.put("QT_ENTRADA", qtEntrada);
							register.put("CL_VALIDA", 1);
							rsmProd.addRegister(register);
						}
						
					}
					
					else{//Cairá aqui quando ele já estiver cadastrado e seu id também eh referenciado em produto_fornecedor
						int cdProdutoServico = rsProd.getInt("cd_produto_servico");
						
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("CD_PRODUTO_SERVICO", cdProdutoServico);
						register.put("CD_NCM", cdNcm);
						register.put("NM_NCM", nmNcm);
						register.put("NM_PRODUTO_SERVICO", rsProd.getString("nm_produto_servico"));
						register.put("NM_PRODUTO_SERVICO_ANTIGO", nmProduto);
						register.put("CD_GRUPO", cdGrupo);
						register.put("NM_GRUPO", nmGrupo);
						register.put("CD_UNIDADE_MEDIDA", cdUnidadeMedida);
						register.put("NM_UNIDADE_MEDIDA", nmUnidadeMedida);
						register.put("ID_REDUZIDO", rsProd.getString("id_reduzido"));
						register.put("ID_REDUZIDO_ANTIGO", rsProd.getString("id_reduzido"));
						register.put("ID_PRODUTO_FORNECEDOR", rsProdForn.getString("id_produto"));
						register.put("NR_CODIGO_BARRAS", rsProd.getString("id_produto_servico"));
						register.put("VL_UNITARIO", vlUnitario);
						register.put("QT_PRECISAO_CUSTO", 2);
						register.put("QT_ENTRADA", qtEntrada);
						register.put("CL_VALIDA", 1);
						rsmProd.addRegister(register);
						
					}
					
				}
			}
			
			
			
			resultado.addObject("rsmProd", rsmProd);
			
			return resultado;
	    }
	    catch(Exception pce)	{
	    	pce.printStackTrace(System.out);
	    	return new Result(-1, "Erro ao tentar carregar ", pce);
	    }
	}
	
	
	public static GregorianCalendar convStringToCalendar(String data) {
    	return convStringToCalendar(data, null);
    }

    public static GregorianCalendar convStringToCalendar(String data, GregorianCalendar defaultDate) {
		try {
	        if(data==null || data.trim().equals(""))
	        	return null;
	        StringTokenizer token = new StringTokenizer(data, " ");
	        String d = token.nextToken(), h = "";
	        int hora =0, min = 0, sec = 0, millisec=0;
	        if(token.hasMoreTokens())	{
	        	h	=	token.nextToken();
				StringTokenizer token2	= new StringTokenizer(h, ":");
				hora =	Integer.parseInt(token2.nextToken().trim());
				min  =	Integer.parseInt(token2.nextToken().trim());
				String token3 = token2.hasMoreTokens() ? token2.nextToken() : null;
				sec  =	token3==null ? 0 : Integer.parseInt(token3.trim());
				token3 = token2.hasMoreTokens() ? token2.nextToken() : null;
				millisec  =	token3==null ? 0 : Integer.parseInt(token3.trim());
			}
        	token = new StringTokenizer(d, "-");
        	int ano = Integer.parseInt(token.nextToken());
        	int mes = Integer.parseInt(token.nextToken());
        	int dia = Integer.parseInt(token.nextToken());

	        GregorianCalendar date = new GregorianCalendar(ano, mes-1, dia, hora, min, sec);
	        date.set(Calendar.MILLISECOND, millisec);
	        return date;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return defaultDate;
		}
	}
    
    public static float getValorAllItens(int cdDocumentoEntrada) {
		return getValorAllItens(cdDocumentoEntrada, null);
	}

	public static float getValorAllItens(int cdDocumentoEntrada, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			float valorOfProdutos = 0;
			
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					 "SELECT A.* FROM alm_documento_entrada_item A " +
					 "WHERE A.cd_documento_entrada = ?");
			pstmt.setInt(1, cdDocumentoEntrada);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				float qtEntrada = rsm.getFloat("qt_entrada");
				float vlUnitario = rsm.getFloat("vl_unitario");
				float vlAcrescimo = rsm.getFloat("vl_acrescimo");
				float vlDesconto = rsm.getFloat("vl_desconto");
				
				valorOfProdutos += (qtEntrada * vlUnitario) + vlAcrescimo - vlDesconto;
				
			}
			
			return valorOfProdutos;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaServices.getValorAllItens: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static float getValorDocumento(int cdDocumentoEntrada) {
		return getValorDocumento(cdDocumentoEntrada, null);
	}

	public static float getValorDocumento(int cdDocumentoEntrada, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			float valorTotal = getValorAllItens(cdDocumentoEntrada, connect);
			
			DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(cdDocumentoEntrada, connect);
			
			valorTotal += docEntrada.getVlAcrescimo() - docEntrada.getVlDesconto() + docEntrada.getVlFrete() + docEntrada.getVlSeguro();
			
			//Itera sobre os tributos da nota para saber o que é imposto direto e qual é de icms por substituicao tributaria
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_documento_saida", "" + cdDocumentoEntrada, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = EntradaItemAliquotaServices.find(criterios, connect);
			rsm.beforeFirst();
			float vlICMSST = 0;
			float vlICMS = 0;
			float vlPIS = 0;
			float vlCOFINS = 0;
			float vlImpostosDiretos = 0;
			while(rsm.next()){
				Tributo tributo = TributoDAO.get(rsm.getInt("cd_tributo"), connect);
				if(tributo.getIdTributo().equals("ICMS")){
					vlICMSST += rsm.getFloat("vl_retido");
					vlICMS += rsm.getFloat("vl_tributo");	
				}
				else if(tributo.getIdTributo().equals("IPI")){
					vlImpostosDiretos += rsm.getFloat("vl_tributo");					
				}
				else if(tributo.getIdTributo().equals("II")){
					vlImpostosDiretos += rsm.getFloat("vl_tributo");
				}
				else if(tributo.getIdTributo().equals("PIS")){
					vlPIS += rsm.getFloat("vl_tributo");					
				}
				else if(tributo.getIdTributo().equals("COFINS")){
					vlCOFINS += rsm.getFloat("vl_tributo");					
				}
			}
			
			valorTotal += vlICMSST + vlImpostosDiretos;
			
			return valorTotal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaServices.getValorAllItens: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public Result gerarRelatorioCompraProduto(int cdEmpresa, GregorianCalendar dtInicial,GregorianCalendar dtFinal, boolean lgCombustivel){
		
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.dt_documento_entrada", Util.formatDate(dtInicial, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.DATE));
			criterios.add(new ItemComparator("A.dt_documento_entrada", Util.formatDate(dtFinal, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.DATE));
			criterios.add(new ItemComparator("lgCombustivel", "" + lgCombustivel, ItemComparator.EQUAL, Types.BOOLEAN));
			criterios.add(new ItemComparator("isNotCombustivel", "1", ItemComparator.EQUAL, Types.INTEGER));
			ArrayList<String> groupByFields = new ArrayList<String>();
			groupByFields.add("B.cd_produto_servico");
			groupByFields.add("D.nm_produto_servico");
			groupByFields.add("id_reduzido");
			groupByFields.add("C.vl_ultimo_custo");
			groupByFields.add("H.nm_pessoa");
			groupByFields.add("E.sg_unidade_medida");
			groupByFields.add("R.id_classificacao_fiscal");
			groupByFields.add("R.cd_classificacao_fiscal");
			groupByFields.add("S.nr_codigo_fiscal");
			groupByFields.add("S.cd_natureza_operacao");
			groupByFields.add("A.cd_documento_entrada");
			groupByFields.add("A.nr_documento_entrada");
			groupByFields.add("A.vl_total_documento");
			groupByFields.add("A.dt_emissao");
			groupByFields.add("A.dt_documento_entrada");
			groupByFields.add("A.cd_documento_entrada");
			groupByFields.add("U.vl_preco");
			groupByFields.add("B.vl_unitario");
			groupByFields.add("B.vl_acrescimo");
			groupByFields.add("B.vl_desconto");
			groupByFields.add("X.pr_aliquota");
			groupByFields.add("X.vl_base_calculo");
			groupByFields.add("Z.st_tributaria");
			groupByFields.add("U.vl_preco");
			groupByFields.add("U.vl_preco");
			groupByFields.add("DLA.nm_local_armazenamento");
			groupByFields.add("LA.nm_local_armazenamento");
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("A.cd_documento_entrada");
			ResultSetMap rsm = DocumentoEntradaItemServices.findCompletoRelatorio(criterios, groupByFields, orderBy);
			while(rsm.next()){
				rsm.setValueToField("ID_REDUZIDO", rsm.getString("ID_REDUZIDO"));
				rsm.setValueToField("NM_PRODUTO_SERVICO", rsm.getString("NM_PRODUTO_SERVICO"));
				rsm.setValueToField("NM_FORNECEDOR", rsm.getString("NM_PESSOA"));
				rsm.setValueToField("SG_UNIDADE", rsm.getString("sg_unidade_medida"));
				rsm.setValueToField("ID_CLASSIFICACAO_FISCAL", rsm.getString("id_classificacao_fiscal"));
				rsm.setValueToField("NR_CFOP", rsm.getString("nr_codigo_fiscal"));
				rsm.setValueToField("QT_ENTRADA", rsm.getFloat("qt_entradas"));
				rsm.setValueToField("VL_UNITARIO", rsm.getFloat("vl_unitario"));
				rsm.setValueToField("VL_TOTAL_CUSTO", (rsm.getFloat("qt_entradas") * rsm.getFloat("vl_unitario")));
				rsm.setValueToField("VL_TOTAL_VENDA", (rsm.getFloat("qt_entradas") * rsm.getFloat("vl_preco")));
				rsm.setValueToField("PR_MARGEM", (((rsm.getFloat("VL_TOTAL_VENDA") / rsm.getFloat("QT_ENTRADA") - rsm.getFloat("VL_UNITARIO")) / rsm.getFloat("VL_UNITARIO")) * 100));
				rsm.setValueToField("DT_MOVIMENTO", ((rsm.getGregorianCalendar("dt_documento_entrada") != null) ? Util.convCalendarString(rsm.getGregorianCalendar("dt_documento_entrada")) : ""));
				rsm.setValueToField("DT_EMISSAO", ((rsm.getGregorianCalendar("dt_emissao") != null) ? Util.convCalendarString(rsm.getGregorianCalendar("dt_emissao")) : ""));
				rsm.setValueToField("NR_NOTA_FISCAl", rsm.getString("nr_documento_entrada"));				
				rsm.setValueToField("VL_MERCADORIAS", rsm.getFloat("vl_total_produto"));				
				rsm.setValueToField("VL_TOTAL_NOTA", rsm.getFloat("vl_total_documento"));
				rsm.setValueToField("LG_CONTA_PAGAR", (rsm.getString("cd_conta_pagar") != null && !rsm.getString("cd_conta_pagar").equals("0")));
				if(rsm.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
					rsm.setValueToField("VL_ICMS_ST", (rsm.getFloat("pr_aliquota") == 0 || rsm.getFloat("vl_base_calculo") == 0) ? 0.0 : (rsm.getFloat("pr_aliquota") * rsm.getFloat("vl_base_calculo") / 100));
					rsm.setValueToField("VL_PRECO_RET", rsm.getFloat("vl_base_calculo") == 0 ? 0.0 : (rsm.getFloat("vl_base_calculo") / (rsm.getFloat("qt_entradas") == 0 ? 1 : rsm.getFloat("qt_entradas"))));
				}
				else{
					rsm.setValueToField("VL_ICMS_ST", 0.0);
					rsm.setValueToField("VL_PRECO_RET", 0.0);
				}
				rsm.setValueToField("NM_LOCAL_ARMAZENAMENTO_PRODUTO_EMPRESA", rsm.getString("nm_local_armazenamento_produto_empresa"));	
				
			}
//			System.out.println("rsm = " + rsm);
			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
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
	
	public Result gerarRelatorioCompraProdutoResumido( ArrayList<ItemComparator> criterios, int cdEmpresa){
		
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			criterios.add(new ItemComparator("A.cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = Search.find(
					"SELECT B.qt_entrada, C.vl_custo_medio, C.id_reduzido, D.nm_produto_servico, D2.qt_embalagem, E.sg_unidade_medida, "+
					" E2.sg_unidade_medida as sg_unidade_medida_embalagem, G.nm_grupo, G2.nm_grupo as nm_grupo_superior " +
					" FROM alm_documento_entrada                A " +
					" JOIN alm_documento_entrada_item           B ON (A.cd_documento_entrada = B.cd_documento_entrada) " +
					" JOIN grl_produto_servico_empresa   	   C ON (B.cd_produto_servico = C.cd_produto_servico " +
					"										     AND B.cd_empresa = C.cd_empresa) " +
					" JOIN grl_produto_servico           	   D ON (C.cd_produto_servico = D.cd_produto_servico) " +
					" LEFT OUTER JOIN grl_produto           	   D2 ON (C.cd_produto_servico = D2.cd_produto_servico) " +
					" LEFT OUTER JOIN grl_unidade_medida 	   E ON (B.cd_unidade_medida = E.cd_unidade_medida) " +
					" LEFT OUTER JOIN grl_unidade_medida 	   E2 ON (C.cd_unidade_medida = E2.cd_unidade_medida) " +
					" LEFT OUTER JOIN alm_produto_grupo         F ON (F.cd_produto_servico = C.cd_produto_servico " +
					"								      	     AND F.cd_empresa         = C.cd_empresa " +
					"								    	     AND F.lg_principal       = 1) " +
					" LEFT OUTER JOIN alm_grupo                 G ON (F.cd_grupo = G.cd_grupo) " +
					" LEFT OUTER JOIN alm_grupo                 G2 ON (G.cd_grupo_superior = G2.cd_grupo) " +
					" WHERE 1 = 1 ",
					" GROUP BY B.qt_entrada, C.vl_custo_medio, C.id_reduzido, D.nm_produto_servico, D2.qt_embalagem, E.sg_unidade_medida, E2.sg_unidade_medida, "+
					" G.nm_grupo, G2.nm_grupo, G.cd_grupo, G2.cd_grupo ORDER BY G2.cd_grupo, G.cd_grupo ",
					criterios, connection, false);
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
	
	
	public Result gerarRelatorioCompraCombustivel(int cdEmpresa, GregorianCalendar dtInicial,GregorianCalendar dtFinal ){
		return gerarRelatorioCompraCombustivel( cdEmpresa, dtInicial, dtFinal, null);
	}
	public Result gerarRelatorioCompraCombustivel(int cdEmpresa, GregorianCalendar dtInicial,GregorianCalendar dtFinal, ArrayList<ItemComparator> criterios){
		
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			if( criterios == null )
				criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.dt_documento_entrada", Util.formatDate(dtInicial, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.DATE));
			criterios.add(new ItemComparator("A.dt_documento_entrada", Util.formatDate(dtFinal, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.DATE));
			criterios.add(new ItemComparator("lgCombustivel", "true", ItemComparator.EQUAL, Types.BOOLEAN));
			ArrayList<String> groupByFields = new ArrayList<String>();
			groupByFields.add("B.cd_produto_servico");
			groupByFields.add("D.nm_produto_servico");
			groupByFields.add("id_reduzido");
			groupByFields.add("C.vl_ultimo_custo");
			groupByFields.add("H.nm_pessoa");
			groupByFields.add("E.sg_unidade_medida");
			groupByFields.add("R.id_classificacao_fiscal");
			groupByFields.add("R.cd_classificacao_fiscal");
			groupByFields.add("S.nr_codigo_fiscal");
			groupByFields.add("S.cd_natureza_operacao");
			groupByFields.add("A.cd_documento_entrada");
			groupByFields.add("A.nr_documento_entrada");
			groupByFields.add("A.vl_total_documento");
			groupByFields.add("A.dt_emissao");
			groupByFields.add("A.dt_documento_entrada");
			groupByFields.add("U.vl_preco");
			groupByFields.add("B.vl_unitario");
			groupByFields.add("B.vl_acrescimo");
			groupByFields.add("B.vl_desconto");
			groupByFields.add("X.pr_aliquota");
			groupByFields.add("X.vl_base_calculo");
			groupByFields.add("Z.st_tributaria");
			groupByFields.add("LA.nm_local_armazenamento");
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("D.nm_produto_servico");
			ResultSetMap rsm = DocumentoEntradaItemServices.findCompletoRelatorio(criterios, groupByFields, orderBy);
			rsm.beforeFirst();
			while(rsm.next()){
				
				rsm.setValueToField("DT_MOVIMENTACAO", ((rsm.getGregorianCalendar("dt_documento_entrada") != null) ? Util.convCalendarString(rsm.getGregorianCalendar("dt_documento_entrada")) : ""));
				rsm.setValueToField("NM_FORNECEDOR", rsm.getString("NM_PESSOA"));
				rsm.setValueToField("NR_NOTA_FISCAl", rsm.getString("nr_documento_entrada"));		
				rsm.setValueToField("DT_EMISSAO", ((rsm.getGregorianCalendar("dt_emissao") != null) ? Util.convCalendarString(rsm.getGregorianCalendar("dt_emissao")) : ""));
				rsm.setValueToField("VL_CUSTO", rsm.getFloat("vl_ultimo_custo"));
				rsm.setValueToField("QT_ENTRADA", rsm.getFloat("qt_entradas"));	
				rsm.setValueToField("NM_LOCAL_ARMAZENAMENTO", rsm.getString("nm_local_armazenamento"));	
				rsm.setValueToField("VL_TOTAL", (rsm.getFloat("qt_entradas") * rsm.getFloat("vl_ultimo_custo")));
				
				rsm.setValueToField("NM_TOTAL", "Total de " + rsm.getString("nm_produto_servico"));
				rsm.setValueToField("NM_COMBUSTIVEL", rsm.getString("nm_produto_servico"));
				
				if(rsm.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA)
					rsm.setValueToField("VL_PRECO_RET", rsm.getFloat("vl_base_calculo") == 0 || rsm.getFloat("qt_entradas") == 0 ? 0.0 : (rsm.getFloat("vl_base_calculo") / (rsm.getFloat("qt_entradas") == 0 ? 1 : rsm.getFloat("qt_entradas"))));
				else
					rsm.setValueToField("VL_PRECO_RET", 0.0);
			}
			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
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
	
	public static ResultSetMap findCompletoByNfe(int cdNotaFiscal) {
		return findCompletoByNfe(cdNotaFiscal, null);
	}

	public static ResultSetMap findCompletoByNfe(int cdNotaFiscal, Connection connection)
	{
		
		/*
		 * Incluido para acrescentar ao relatório informações de faturamento
		 */
		return Search.find("SELECT A.*, H.nm_pessoa AS nm_cliente, J.nm_pessoa AS nm_transportadora, " +
	                       "       N.*, K.nr_cnpj " +
						   "FROM alm_documento_entrada A " +
						   "JOIN fsc_nota_fiscal_doc_vinculado  B ON (A.cd_documento_entrada = B.cd_documento_entrada AND B.cd_nota_fiscal = " +cdNotaFiscal+ ")  " +
						   "LEFT OUTER JOIN grl_pessoa          H ON (A.cd_fornecedor = H.cd_pessoa) " +
						   "LEFT OUTER JOIN grl_pessoa          J ON (A.cd_transportadora = J.cd_pessoa) " +
						   "LEFT OUTER JOIN grl_empresa         N ON (A.cd_empresa = N.cd_empresa) " +
						   "LEFT OUTER JOIN grl_pessoa_juridica K ON (N.cd_empresa  = K.cd_pessoa)" +
						   "WHERE 1 = 1 " +
						   "ORDER BY dt_documento_entrada ", new ArrayList<ItemComparator>(), connection!=null ? connection : Conexao.conectar());
	}
	
	public static ResultSetMap findCompletoByViagem(int cdViagem) {
		return findCompletoByViagem(cdViagem, null);
	}

	public static ResultSetMap findCompletoByViagem(int cdViagem, Connection connection)
	{
		
		return Search.find("  SELECT A.*, H.nm_pessoa AS nm_fornecedor, J.nm_pessoa AS nm_transportadora, " +
	                       "       N.*, K.nr_cnpj, " +
	                       "	  (SELECT SUM(E.qt_entrada * vl_unitario + vl_acrescimo - vl_desconto) FROM alm_documento_entrada_item E " +
						   "       WHERE E.cd_documento_entrada = A.cd_documento_entrada) AS vl_total_liquido " +
						   "	FROM alm_documento_entrada A " +
						   "	LEFT OUTER JOIN grl_pessoa          H ON (A.cd_fornecedor = H.cd_pessoa) " +
						   "	LEFT OUTER JOIN grl_pessoa          J ON (A.cd_transportadora = J.cd_pessoa) " +
						   "	LEFT OUTER JOIN grl_empresa         N ON (A.cd_empresa = N.cd_empresa) " +
						   "	LEFT OUTER JOIN grl_pessoa_juridica K ON (N.cd_empresa  = K.cd_pessoa)" +
						   "  WHERE 1 = 1 AND A.cd_viagem = " + cdViagem + 
						   "    AND A.tp_documento_entrada = " + DocumentoEntradaServices.TP_NOTA_FISCAL +
						   "  ORDER BY dt_documento_entrada ", new ArrayList<ItemComparator>(), connection!=null ? connection : Conexao.conectar());
	}
	
	/**
	 * Correção do estoque da loja que a rotina de vendas externas causou
	 * @return
	 */
	public static Result correcaoEstoqueViagem() {
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			//Busca documentos de remessa sem viagem e cadastra uma viagem
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("tp_documento_saida", "" + DocumentoSaidaServices.TP_NOTA_REMESSA, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_viagem", "", ItemComparator.ISNULL, Types.INTEGER));
			ResultSetMap rsmRemessaSemViagem = DocumentoSaidaDAO.find(criterios, connect);
			while(rsmRemessaSemViagem.next()){
				int cdTipoRota      = 1;
				int cdCidadeOrigem  = 1;
				int cdCidadeDestino = 1;
				int cdVeiculo       = 1;
				
				TipoRota tipoRota = TipoRotaDAO.get(cdTipoRota, connect);
				if(tipoRota == null)
					break;
				
				Cidade cidadeOrigem = CidadeDAO.get(cdCidadeOrigem, connect);
				if(cidadeOrigem == null)
					break;
				
				Cidade cidadeDestino = CidadeDAO.get(cdCidadeDestino, connect);
				if(cidadeDestino == null)
					break;
				
				Veiculo veiculo = VeiculoDAO.get(cdVeiculo, connect);
				if(veiculo == null)
					break;
				
				Rota rota = new Rota(0, cdTipoRota, cdCidadeOrigem, cdCidadeDestino, 0, 0, "VITÓRIA DA CONQUISTA", "VITÓRIA DA CONQUISTA", 0, 0, 0, 0, "DE VITÓRIA DA CONQUISTA PARA VITÓRIA DA CONQUISTA", 0);
				int cdRota = RotaDAO.insert(rota, connect);
				if(cdRota < 0)
					break;
				
				Viagem viagem = new Viagem(0, cdRota, cdVeiculo, 0, Util.getDataAtual(), Util.getDataAtual(), null, 0, Util.getDataAtual(), 0);
				int cdViagem = ViagemDAO.insert(viagem, connect);
				if(cdViagem < 0)
					break;
				
				DocumentoSaida documentoSaidaRemessaSemViagem = DocumentoSaidaDAO.get(rsmRemessaSemViagem.getInt("cd_documento_saida"), connect);
				documentoSaidaRemessaSemViagem.setCdViagem(cdViagem);
				if(DocumentoSaidaDAO.update(documentoSaidaRemessaSemViagem, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar documento de remessa sem viagem");
				}
					
			}
			
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("tp_documento_saida", "" + DocumentoSaidaServices.TP_NOTA_REMESSA, ItemComparator.EQUAL, Types.INTEGER));
			
			//Busca das remessas cadastradas
			ResultSetMap rsmRemessas = DocumentoSaidaServices.find(criterios, connect);
			while(rsmRemessas.next()){
				//Apenas arrumará as remessas que tenham viagem cadastrada
				DocumentoSaida documentoRemessa = DocumentoSaidaDAO.get(rsmRemessas.getInt("cd_documento_saida"), connect);
				if(documentoRemessa.getCdViagem() > 0){
					
					//Atualiza as informações do documento, tornando-o consignado
					documentoRemessa.setTpSaida(DocumentoSaidaServices.SAI_VENDA_CONSIGNACAO);
					documentoRemessa.setTpMovimentoEstoque(DocumentoSaidaServices.MOV_ESTOQUE_CONSIGNADO);
					if(DocumentoSaidaDAO.update(documentoRemessa, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar documento de saida");
					}
					
					//Busca os registros de estoque de cada item da remessa, para converte-los para estoque consignado
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_documento_saida", "" + documentoRemessa.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_empresa", "" + documentoRemessa.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmSaidaLocalItem = SaidaLocalItemDAO.find(criterios, connect);
					while(rsmSaidaLocalItem.next()){
						SaidaLocalItem saidaLocalItem = SaidaLocalItemDAO.get(rsmSaidaLocalItem.getInt("cd_saida"), rsmSaidaLocalItem.getInt("cd_produto_servico"), rsmSaidaLocalItem.getInt("cd_documento_saida"), rsmSaidaLocalItem.getInt("cd_empresa"), rsmSaidaLocalItem.getInt("cd_local_armazenamento"), rsmSaidaLocalItem.getInt("cd_item"), connect);
						//Transfere a quantidade de saida para saida consignada
						if(saidaLocalItem.getQtSaida() > 0){
							saidaLocalItem.setQtSaidaConsignada(saidaLocalItem.getQtSaida());
							saidaLocalItem.setQtSaida(0);
						}
						if(SaidaLocalItemDAO.update(saidaLocalItem, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar saida local item");
						}
					}
					
					//Busca as notas de retorno vinculadas a essa viagem para torna-la acerto de consignação
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_viagem", "" + documentoRemessa.getCdViagem(), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmDocumentoEntrada = DocumentoEntradaDAO.find(criterios, connect);
					while(rsmDocumentoEntrada.next()){
						DocumentoEntrada documentoRetorno = DocumentoEntradaDAO.get(rsmDocumentoEntrada.getInt("cd_documento_entrada"), connect);
						
						//Atualiza o documento de retorno
						documentoRetorno.setTpEntrada(ENT_ACERTO_CONSIGNACAO);
						documentoRetorno.setTpMovimentoEstoque(MOV_ESTOQUE_CONSIGNADO);
						if(DocumentoEntradaDAO.update(documentoRetorno, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar documento de entrada");
						}
						
						//Busca os registros de estoque de cada item do retorno, para converte-los para estoque consignado
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_documento_entrada", "" + documentoRetorno.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_empresa", "" + documentoRetorno.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmEntradaLocalItem = EntradaLocalItemDAO.find(criterios, connect);
						while(rsmEntradaLocalItem.next()){
							EntradaLocalItem entradaLocalItem = EntradaLocalItemDAO.get(rsmEntradaLocalItem.getInt("cd_produto_servico"), rsmEntradaLocalItem.getInt("cd_documento_entrada"), rsmEntradaLocalItem.getInt("cd_empresa"), rsmEntradaLocalItem.getInt("cd_local_armazenamento"), rsmEntradaLocalItem.getInt("cd_entrada_local_item"), rsmEntradaLocalItem.getInt("cd_item"), connect);
							//Transfere a quantidade de entrada para saida consignada
							if(entradaLocalItem.getQtEntrada() > 0){
								entradaLocalItem.setQtEntradaConsignada(entradaLocalItem.getQtEntrada());
								entradaLocalItem.setQtEntrada(0);
							}
							if(EntradaLocalItemDAO.update(entradaLocalItem, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao atualizar entrada local item");
							}
						}
					}
					
					
					//Busca as notas de venda para fazer as notas de entrada que equilibram o controle de estoque
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("tp_documento_saida", "" + DocumentoSaidaServices.TP_NOTA_REMESSA, ItemComparator.DIFFERENT, Types.INTEGER));
					criterios.add(new ItemComparator("cd_viagem", "" + documentoRemessa.getCdViagem(), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmDocumentoVenda = DocumentoSaidaDAO.find(criterios, connect);
					while(rsmDocumentoVenda.next()){
						
						DocumentoSaida documentoSaida = DocumentoSaidaDAO.get(rsmDocumentoVenda.getInt("cd_documento_saida"), connect);
						
						int cdEmpresa   = documentoSaida.getCdEmpresa();
						int cdDigitador = documentoSaida.getCdDigitador();
						int cdMoeda = ParametroServices.getValorOfParametroAsInteger("CD_MOEDA_DEFAULT", 0);
						int cdTransportadora = documentoSaida.getCdTransportadora();
						
						//A cada venda, um documento de entrada irá ser criada para balancear o estoque
						DocumentoEntrada docEntradaRetSup = new DocumentoEntrada(0 /*cdDocumentoEntrada*/, cdEmpresa, cdTransportadora, cdEmpresa, Util.getDataAtual(), Util.getDataAtual(),
												DocumentoEntradaServices.ST_EM_ABERTO /*stDocumentoEntrada*/,
												DocumentoEntradaServices.TP_DOC_NAO_FISCAL /*tpDocumentoEntrada*/, 
							                    DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO/*tpEntrada*/, 0,
							                    DocumentoEntradaServices.MOV_ESTOQUE_CONSIGNADO,
							                    cdMoeda, cdDigitador,1 /*nrSerie*/, documentoSaida.getCdViagem());
						
						int cdDocumentoEntradaRetSup = DocumentoEntradaDAO.insert(docEntradaRetSup, connect);
						docEntradaRetSup.setCdDocumentoEntrada(cdDocumentoEntradaRetSup);
						if (cdDocumentoEntradaRetSup <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Falha ao criar documento de entrada a partir da venda externa!");
						}
						float vlTotalItemSuplementar = 0;
						
						//Busca todos os itens do documento de venda
						ResultSetMap rsmItens = DocumentoSaidaServices.getAllItens(documentoSaida.getCdDocumentoSaida(), connect);
						while(rsmItens.next())	{
							int cdProdutoServico = rsmItens.getInt("cd_produto_servico");
							float qtSaida = rsmItens.getFloat("qt_saida");
							
							DocumentoSaidaItem item = DocumentoSaidaItemDAO.get(rsmItens.getInt("cd_documento_saida"), rsmItens.getInt("cd_produto_servico"), rsmItens.getInt("cd_empresa"), rsmItens.getInt("cd_item"), connect);
							
							//Lista alguns valores do item do documento de retorno suplementar
							float vlUnitario      = item.getVlUnitario();
							float vlAcrescimo     = item.getVlAcrescimo();
							float vlDesconto      = item.getVlDesconto();
							int cdUnidadeMedida   = item.getCdUnidadeMedida();
							
							//Busca o local de armazenamento pela saida do item
							int cdLocalArmazenamento = 0;
							criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cd_produto_servico", "" + item.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("cd_documento_saida", "" + documentoSaida.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("cd_empresa", "" + documentoSaida.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("cd_item", "" + item.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
							ResultSetMap rsmSaidaLocalItemVenda = SaidaLocalItemDAO.find(criterios, connect);
							if(rsmSaidaLocalItemVenda.next()){
								cdLocalArmazenamento = rsmSaidaLocalItemVenda.getInt("cd_local_armazenamento");
							}
							
							//Insere o item documento de retorno suplementar
							Result resultado = DocumentoEntradaItemServices.insert(new DocumentoEntradaItem(cdDocumentoEntradaRetSup, cdProdutoServico,
																					cdEmpresa, qtSaida /*qtEntrada*/,
																					vlUnitario,vlAcrescimo, vlDesconto, cdUnidadeMedida,
																					null /*dtEntregaPrevista*/, 0/*cdNaturezaOperacao*/, 0/*cdAdicao*/, 0/*cdItem*/, 0 /*vlVucv*/, 0 /*vlDescontoGeral*/, 0 /*cdTipoCredito*/), cdLocalArmazenamento, true, connect);
							if (resultado.getCode() <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Falha ao incluir item na entrada!");
							}
							
							//Soma o total de itens suplementar
							vlTotalItemSuplementar += (vlUnitario * qtSaida + vlAcrescimo - vlDesconto);
							
						}
						
						
						docEntradaRetSup.setVlTotalDocumento(vlTotalItemSuplementar);
						docEntradaRetSup.setVlTotalItens(vlTotalItemSuplementar);
						if(DocumentoEntradaDAO.update(docEntradaRetSup, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar documento de entrada suplementar");
						}
						
						Result resultado = liberarEntrada(cdDocumentoEntradaRetSup, connect);
						if(resultado.getCode() <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao liberar documento de entrada suplementar");
						}
					}
				}
			}
			
			if (isConnectionNull)
				connect.commit();

			return new Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Falha ao tentar excluir entrada!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo para buscar a soma de descontos dos itens do documento de entrada passado
	 * @param cdDocumentoSaida
	 * @return
	 */
	public static float getValorDescontosAllItens(int cdDocumentoSaida) {
		return getValorDescontosAllItens(cdDocumentoSaida, null);
	}
	public static float getValorDescontosAllItens(int cdDocumentoSaida, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			float valorOfProdutos = 0;
			
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					 "SELECT A.* FROM alm_documento_entrada_item A " +
					 "WHERE A.cd_documento_entrada = ?");
			pstmt.setInt(1, cdDocumentoSaida);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				float vlDesconto = rsm.getFloat("vl_desconto");
				
				valorOfProdutos += Util.roundFloat(vlDesconto, 2);
				
			}
			
			return valorOfProdutos;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaServices.getValorAllItens: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String getProximoNrDocumentoEntrega(int cdEmpresa) {
		return getProximoNrDocumentoEntrega(cdEmpresa, null, null);
	}
	
	public static String getProximoNrDocumentoEntrega(int cdEmpresa, String nmDocumento) {
		return getProximoNrDocumentoEntrega(cdEmpresa, nmDocumento, null);
	}

	public static String getProximoNrDocumentoEntrega(int cdEmpresa, String nmDocumento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrDocumento = 0;
			nmDocumento = nmDocumento != null ? nmDocumento : "ENTREGA";
			
			if ((nrDocumento = NumeracaoDocumentoServices.getProximoNumero2(nmDocumento, nrAno, cdEmpresa, connection)) <= 0)
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
	 * Correção das notas de entrada de retorno para viagens
	 * @return
	 */
	public static void correcaoNotasViagem() {
//		Connection connect = Conexao.conectar();
//		boolean isConnectionNull = true;
//		try {
//			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
//			
//			PreparedStatement connectDoc5 = connect.prepareStatement("update alm_documento_entrada set tp_documento_entrada = 0 where cd_documento_entrada = 5");
//			
//			connectDoc5.executeUpdate();
//			
//			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
//			criterios.add(new ItemComparator("vl_unitario", "", ItemComparator.ISNULL, Types.INTEGER));
//			ResultSetMap rsmItemSemValorUnitario = DocumentoSaidaItemDAO.find(criterios, connect);
//			while(rsmItemSemValorUnitario.next()){
//				DocumentoSaida documentoSaidaVenda = DocumentoSaidaDAO.get(rsmItemSemValorUnitario.getInt("cd_documento_saida"), connect);
//				float vlTotalItens = 0;
//				if(documentoSaidaVenda != null){
//					criterios = new ArrayList<ItemComparator>();
//					criterios.add(new ItemComparator("cd_viagem", "" + documentoSaidaVenda.getCdViagem(), ItemComparator.EQUAL, Types.INTEGER));
//					criterios.add(new ItemComparator("tp_documento_saida", "" + DocumentoSaidaServices.TP_NOTA_REMESSA, ItemComparator.EQUAL, Types.INTEGER));
//					DocumentoSaida documentoSaidaRemessa = null;
//					ResultSetMap rsmDocumentoRemessa = DocumentoSaidaDAO.find(criterios, connect);
//					while(rsmDocumentoRemessa.next()){
//						documentoSaidaRemessa = DocumentoSaidaDAO.get(rsmDocumentoRemessa.getInt("cd_documento_saida"), connect);
//						if(documentoSaidaRemessa != null){
//							criterios = new ArrayList<ItemComparator>();
//							criterios.add(new ItemComparator("cd_documento_saida", "" + documentoSaidaRemessa.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
//							criterios.add(new ItemComparator("cd_produto_servico", "" + rsmItemSemValorUnitario.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
//							criterios.add(new ItemComparator("cd_empresa", "" + documentoSaidaRemessa.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
//							
//							ResultSetMap rsmDocumentoSaidaItem = DocumentoSaidaItemDAO.find(criterios, connect);
//							if(rsmDocumentoSaidaItem.next()){
//								DocumentoSaidaItem documentoSaidaVendaItem = DocumentoSaidaItemDAO.get(rsmDocumentoSaidaItem.getInt("cd_documento_saida"), rsmDocumentoSaidaItem.getInt("cd_produto_servico"), rsmDocumentoSaidaItem.getInt("cd_empresa"), rsmDocumentoSaidaItem.getInt("cd_item"), connect);
//								DocumentoSaidaItem documentoSaidaVendaItemSemValorUnitario = DocumentoSaidaItemDAO.get(rsmItemSemValorUnitario.getInt("cd_documento_saida"), rsmItemSemValorUnitario.getInt("cd_produto_servico"), rsmItemSemValorUnitario.getInt("cd_empresa"), rsmItemSemValorUnitario.getInt("cd_item"), connect);
//								if(documentoSaidaVendaItem != null && documentoSaidaVendaItemSemValorUnitario != null){
//									documentoSaidaVendaItemSemValorUnitario.setVlUnitario(documentoSaidaVendaItem.getVlUnitario());
//									vlTotalItens += (documentoSaidaVendaItemSemValorUnitario.getVlUnitario() * documentoSaidaVendaItemSemValorUnitario.getQtSaida() + documentoSaidaVendaItemSemValorUnitario.getVlAcrescimo() - documentoSaidaVendaItemSemValorUnitario.getVlDesconto());
//									if(DocumentoSaidaItemDAO.update(documentoSaidaVendaItemSemValorUnitario, connect) < 0){
//										System.out.println("Erro ao atualizar documento de saida item");
//										Conexao.rollback(connect);
//										return new Result(-1);
//									}
//								}
//								break;
//							}
//							
//						}
//					}
//				}
//				
//				documentoSaidaVenda.setVlTotalItens(vlTotalItens);
//				documentoSaidaVenda.setVlTotalDocumento(vlTotalItens + documentoSaidaVenda.getVlAcrescimo() - documentoSaidaVenda.getVlDesconto());
//				
//				if(DocumentoSaidaDAO.update(documentoSaidaVenda, connect) < 0){
//					System.out.println("Erro ao atualizar documento de saida de venda");
//					Conexao.rollback(connect);
//					return new Result(-1);
//				}
//			}
//			
//			criterios = new ArrayList<ItemComparator>();
//			criterios.add(new ItemComparator("tp_documento_entrada", "" + DocumentoEntradaServices.TP_DOC_NAO_FISCAL, ItemComparator.EQUAL, Types.INTEGER));
//			criterios.add(new ItemComparator("tp_entrada", "" + DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO, ItemComparator.EQUAL, Types.INTEGER));
//			criterios.add(new ItemComparator("cd_viagem", "", ItemComparator.NOTISNULL, Types.INTEGER));
//			
//			ResultSetMap rsmRetornoAcerto = DocumentoEntradaDAO.find(criterios, connect);
////			System.out.println("rsmRetornoAcerto = " + rsmRetornoAcerto);
//			while(rsmRetornoAcerto.next()){
//			
//				Result resultado = DocumentoEntradaServices.retornaSituacao(rsmRetornoAcerto.getInt("cd_documento_entrada"), connect);
//				
//				if(resultado.getCode() < 0){
//					System.out.println(resultado.getMessage());
//					connect.rollback();
//					return resultado;
//				}
//				resultado = DocumentoEntradaServices.delete(rsmRetornoAcerto.getInt("cd_documento_entrada"), connect);
//				if(resultado.getCode() < 0){
//					System.out.println("Documento Entrada " + rsmRetornoAcerto.getInt("cd_documento_entrada") + ": " + resultado.getMessage());
//					connect.rollback();
//					return resultado;
//				}
//			}	
//				
//				
//			ResultSetMap rsmViagens = ViagemDAO.getAll(connect);
////			System.out.println("rsmRemessas = " + rsmRemessas);
//			
//			while(rsmViagens.next()){	
//				int cdViagem = rsmViagens.getInt("cd_viagem");
////				System.out.println();
////				System.out.println("documentoRemessa = " + documentoRemessa);
//			
//				//Busca as notas de venda para fazer as notas de entrada que equilibram o controle de estoque
//				criterios = new ArrayList<ItemComparator>();
//				criterios.add(new ItemComparator("tp_documento_saida", "" + DocumentoSaidaServices.TP_NOTA_REMESSA, ItemComparator.DIFFERENT, Types.INTEGER));
//				criterios.add(new ItemComparator("cd_viagem", "" + cdViagem, ItemComparator.EQUAL, Types.INTEGER));
//				ResultSetMap rsmDocumentoVenda = DocumentoSaidaDAO.find(criterios, connect);
////					System.out.println();
////					System.out.println("rsmDocumentoVenda = " + rsmDocumentoVenda);
//				
//				while(rsmDocumentoVenda.next()){
//					
//					DocumentoSaida documentoSaida = DocumentoSaidaDAO.get(rsmDocumentoVenda.getInt("cd_documento_saida"), connect);
////						System.out.println();
////						System.out.println("documento Venda = " + documentoSaida);
////						System.out.println();
//					int cdEmpresa   = documentoSaida.getCdEmpresa();
//					int cdDigitador = documentoSaida.getCdDigitador();
//					int cdMoeda = ParametroServices.getValorOfParametroAsInteger("CD_MOEDA_DEFAULT", 0);
//					int cdTransportadora = documentoSaida.getCdTransportadora();
//					
//					//A cada venda, um documento de entrada irá ser criada para balancear o estoque
//					DocumentoEntrada docEntradaRetSup = new DocumentoEntrada(0 /*cdDocumentoEntrada*/, cdEmpresa, cdTransportadora, cdEmpresa, 
//											DocumentoEntradaServices.ST_EM_ABERTO /*stDocumentoEntrada*/,
//											DocumentoEntradaServices.TP_DOC_NAO_FISCAL /*tpDocumentoEntrada*/, 
//						                    DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO/*tpEntrada*/, 0,
//						                    DocumentoEntradaServices.MOV_ESTOQUE_CONSIGNADO,
//						                    cdMoeda, cdDigitador,1 /*nrSerie*/, documentoSaida.getCdViagem());
//					
//					int cdDocumentoEntradaRetSup = DocumentoEntradaDAO.insert(docEntradaRetSup, connect);
//					docEntradaRetSup.setCdDocumentoEntrada(cdDocumentoEntradaRetSup);
//					if (cdDocumentoEntradaRetSup <= 0) {
//						if (isConnectionNull)
//							Conexao.rollback(connect);
//						return new Result(-1, "Falha ao criar documento de entrada a partir da venda externa!");
//					}
//					float vlTotalItemSuplementar = 0;
//					
//					//Busca todos os itens do documento de venda
//					ResultSetMap rsmItens = DocumentoSaidaServices.getAllItens(documentoSaida.getCdDocumentoSaida(), connect);
////						System.out.println();
////						System.out.println("rsmItensVenda = " + rsmItens);
////						System.out.println();
//					while(rsmItens.next())	{
//						int cdProdutoServico = rsmItens.getInt("cd_produto_servico");
//						float qtSaida = rsmItens.getFloat("qt_saida");
//						
//						DocumentoSaidaItem item = DocumentoSaidaItemDAO.get(rsmItens.getInt("cd_documento_saida"), rsmItens.getInt("cd_produto_servico"), rsmItens.getInt("cd_empresa"), rsmItens.getInt("cd_item"), connect);
//						
//						//Lista alguns valores do item do documento de retorno suplementar
//						float vlUnitario      = item.getVlUnitario();
//						float vlAcrescimo     = item.getVlAcrescimo();
//						float vlDesconto      = item.getVlDesconto();
//						int cdUnidadeMedida   = item.getCdUnidadeMedida();
//						
//						//Busca o local de armazenamento pela saida do item
//						int cdLocalArmazenamento = 0;
//						criterios = new ArrayList<ItemComparator>();
//						criterios.add(new ItemComparator("cd_produto_servico", "" + item.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
//						criterios.add(new ItemComparator("cd_documento_saida", "" + documentoSaida.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
//						criterios.add(new ItemComparator("cd_empresa", "" + documentoSaida.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
//						criterios.add(new ItemComparator("cd_item", "" + item.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
//						ResultSetMap rsmSaidaLocalItemVenda = SaidaLocalItemDAO.find(criterios, connect);
//						if(rsmSaidaLocalItemVenda.next()){
//							cdLocalArmazenamento = rsmSaidaLocalItemVenda.getInt("cd_local_armazenamento");
//						}
//						
//						//Insere o item documento de retorno suplementar
//						Result resultado = DocumentoEntradaItemServices.insert(new DocumentoEntradaItem(cdDocumentoEntradaRetSup, cdProdutoServico,
//																				cdEmpresa, 0/*cdItem*/, qtSaida /*qtEntrada*/,
//																				vlUnitario,vlAcrescimo, vlDesconto,cdUnidadeMedida,
//																				null /*dtEntregaPrevista*/), cdLocalArmazenamento, true, connect);
//						if (resultado.getCode() <= 0) {
//							if (isConnectionNull)
//								Conexao.rollback(connect);
//							return new Result(-1, "Falha ao incluir item na entrada!");
//						}
//						
//						//Soma o total de itens suplementar
//						vlTotalItemSuplementar += (vlUnitario * qtSaida + vlAcrescimo - vlDesconto);
////							System.out.println("vlUnitario 	= " + vlUnitario);
////							System.out.println("qtSaida 	= " + qtSaida);
////							System.out.println("vlAcrescimo = " + vlAcrescimo);
////							System.out.println("vlDesconto  = " + vlDesconto);
////							System.out.println("vlTotalItemSuplementar = " + vlTotalItemSuplementar);
//						
//					}
//					
//					
//					docEntradaRetSup.setVlTotalDocumento(vlTotalItemSuplementar);
//					docEntradaRetSup.setVlTotalItens(vlTotalItemSuplementar);
//					if(DocumentoEntradaDAO.update(docEntradaRetSup, connect) < 0){
//						if(isConnectionNull)
//							Conexao.rollback(connect);
//						return new Result(-1, "Erro ao atualizar documento de entrada suplementar");
//					}
//					
//					Result resultado = liberarEntrada(cdDocumentoEntradaRetSup, connect);
//					if(resultado.getCode() <= 0){
//						if(isConnectionNull)
//							Conexao.rollback(connect);
//						return new Result(-1, "Erro ao liberar documento de entrada suplementar");
//					}
//				}
//				
//			}
//			
//			connect.commit();
//
//			return new Result(1);
//		}
//		catch(Exception e){
//			e.printStackTrace(System.out);
//			if (isConnectionNull)
//				Conexao.rollback(connect);
//			return new Result(-1, "Falha ao tentar excluir entrada!", e);
//		}
//		finally{
//			if (isConnectionNull)
//				Conexao.desconectar(connect);
//		}
	}
}