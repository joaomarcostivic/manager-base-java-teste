package com.tivic.manager.grl;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.GregorianCalendar;

import javax.servlet.ServletRequest;

import com.tivic.manager.adm.*;
import com.tivic.manager.alm.*;
import com.tivic.manager.bpm.Bem;
import com.tivic.manager.bpm.BemDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.RequestUtilities;
import sol.util.Result;

public class ProdutoServicoEmpresaServices {

	/* Situações (st_produto_empresa)*/
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO   = 1;

	public static String[] situacoesProduto = {"Inativo", "Ativo"};
	
	/* tipo de reabastecimento */
	public static final int TP_MANUAL     = 0;
	public static final int TP_QTD_MINIMA = 1;
	public static final int TP_AUTOMATICA = 2;
	
	public static String[] tiposAbastecimento = {"Manual", "Quantidade Mínima", "Automática"};

	/*tipo de controle de estoque */
	public static final int CTL_QUANTIDADE = 0;
	public static final int CTL_LOTE_COR   = 1;
	public static final int CTL_INDIVIDUAL = 2;

	public static String[] tiposControleEstoque = {"Quantidade","Lote/Cor","Individual"};

	/* tipo de transporte */
	public static final int TRANSP_COMUM       = 0;
	public static final int TRANSP_CONTROLADO  = 1;
	public static final int TRANSP_CARRO_FORTE = 2;
	public static final int TRANSP_TANQUE      = 3;

	/* erros gerados por rotinas da classe */
	public static final int ERR_ID_REDUZIDO_EM_USO 		= -2;
	public static final int ERR_ID_CODIGO_BARRAS_EM_USO = -3;
	/*tipo de Origem*/
	public static final int NACIONAL       				          = 0;
	public static final int ESTRANGEIRA_IMPORT_DIRETA    		  = 1;
	public static final int ESTRANGEIRA_ADQUIRIDA_MERCADO_INTERNO = 2;
	
	public static String[] tiposOrigem = {"Nacional", "Estrangeira - Importação Direta", "Estrangeira - Adquirida no Mercado Interno"};
	
	private static HashMap<String,String> idReduzidos = new HashMap<String,String>(); 

	public static int insert(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa) {
		return insert(produtoServico, produtoServicoEmpresa, 0, (Connection)null);
	}

	public static int insert(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, Connection connection) {
		return insert(produtoServico, produtoServicoEmpresa, 0, 0, null, null, connection);
	}

	public static int insert(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, int cdGrupo2, ArrayList<FormularioAtributoValor> atributos) {
		return insert(produtoServico, produtoServicoEmpresa, cdGrupo, cdGrupo2, atributos, null, null);
	}

	public static Result insertDna(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, int cdGrupo2, ArrayList<FormularioAtributoValor> atributos, HashMap<String, Object> parametros) {
		
		Connection connect = Conexao.conectar();
		
		try{
		
			connect.setAutoCommit(false);
			
			if(produtoServico.getIdProdutoServico() != null && !produtoServico.getIdProdutoServico().trim().equals("")){
				ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_produto_servico WHERE id_produto_servico = '"+produtoServico.getIdProdutoServico()+"'").executeQuery());
				if(rsm.next()){
					Conexao.rollback(connect);
					return new Result(ERR_ID_CODIGO_BARRAS_EM_USO, "Código de Barras já existe!");
				}
				else{
					rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_produto_codigo WHERE id_produto_servico = '"+produtoServico.getIdProdutoServico()+"' AND lg_codigo_barras = 1").executeQuery());
					if(rsm.next()){
						Conexao.rollback(connect);
						return new Result(ERR_ID_CODIGO_BARRAS_EM_USO, "Código de Barras já existe!");
					}
				}
			}
			int code = insert(produtoServico, produtoServicoEmpresa, cdGrupo, cdGrupo2, atributos, null, parametros, connect);
			if(code < 0){
				Conexao.rollback(connect);
			}
			else{
				connect.commit();
			}
			Result resultado = new Result(code);
			resultado.addObject("dtCadastro", Util.convCalendarString(produtoServicoEmpresa.getDtCadastro()));
			resultado.addObject("dtUltimaAlteracao", Util.convCalendarString(produtoServicoEmpresa.getDtUltimaAlteracao()));
			return resultado;
		}
		catch(Exception e){
			e.printStackTrace();
			Conexao.rollback(connect);
			return new Result(-1);
		}
		
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static Result save(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo){
		return save(produtoServico, produtoServicoEmpresa, cdGrupo, null, null);
	}

	public static Result save(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, ProdutoServicoParametro produtoServicoParametro){
		return save(produtoServico, produtoServicoEmpresa, 0/*cdGrupo*/, produtoServicoParametro, null);
	}

	public static Result save(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, ProdutoServicoParametro produtoServicoParametro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(produtoServicoEmpresa==null)
				return new Result(-1, "Erro ao salvar. ProdutoServicoEmpresa é nulo");

			int retorno;
			ProdutoServicoEmpresa objTmp  = ProdutoServicoEmpresaDAO.get( produtoServicoEmpresa.getCdEmpresa(),
																		  produtoServicoEmpresa.getCdProdutoServico(), connect);
			if(objTmp==null){
				retorno = insert(produtoServico, produtoServicoEmpresa, cdGrupo, connect);
			}else {
				retorno = update(produtoServico, produtoServicoEmpresa, cdGrupo, connect);
			}
			
			if( produtoServicoParametro != null ){
				produtoServicoParametro.setCdEmpresa( produtoServicoEmpresa.getCdEmpresa());
				produtoServicoParametro.setCdProdutoServico( produtoServicoEmpresa.getCdProdutoServico() );
				Result r = ProdutoServicoParametroServices.save(produtoServicoParametro, null, connect);
				if( r.getCode()<=0 ){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao salvar parametros do produto/serviço");
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PRODUTOSERVICOEMPRESA", produtoServicoEmpresa);
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
	
	public static int insert(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, Bem bemPatrimonial) {
		return insert(produtoServico, produtoServicoEmpresa, cdGrupo, 0, null, bemPatrimonial, null);
	}

	public static int insert(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, Connection connect) {
		return insert(produtoServico, produtoServicoEmpresa, cdGrupo, 0, null, null, connect);
	}
	
	/**
	 * Salva um produto quando este mesmo é um bem patrimonial
	 * 
	 * @param produtoServico
	 * @param produtoServicoEmpresa
	 * @param cdGrupo
	 * @param cdGrupo2
	 * @param atributos
	 * @param bemPatrimonial
	 * @category bpm
	 * @author Luiz Romario Filho
	 * @since 16/03/2015
	 * @return
	 * 
	 */
	public static int insert(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, int cdGrupo2, ArrayList<FormularioAtributoValor> atributos, Bem bemPatrimonial){
		return insert(produtoServico, produtoServicoEmpresa, cdGrupo, cdGrupo2, atributos, bemPatrimonial, null, null);
	}
	public static int insert(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, int cdGrupo2, ArrayList<FormularioAtributoValor> atributos, Bem bemPatrimonial, Connection connection){
		return insert(produtoServico, produtoServicoEmpresa, cdGrupo, cdGrupo2, atributos, bemPatrimonial, null, connection);
	}
	
	public static int insert(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, int cdGrupo2, ArrayList<FormularioAtributoValor> atributos, Bem bemPatrimonial, HashMap<String, Object> parametros, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			boolean lgPermiteRepetirID = ParametroServices.getValorOfParametroAsInteger("LG_PERMITE_ID_REPETIDO", 1, produtoServicoEmpresa.getCdEmpresa())==1;
			/* verifica se o id informado (se informado) já está sendo usado */
			if(!lgPermiteRepetirID)	{
				produtoServicoEmpresa.setIdReduzido(produtoServicoEmpresa.getIdReduzido()==null ? "" : produtoServicoEmpresa.getIdReduzido().trim());
				if (produtoServicoEmpresa.getIdReduzido()!=null && !produtoServicoEmpresa.getIdReduzido().equals("")) {
					PreparedStatement pstmt = connection.prepareStatement("SELECT cd_produto_servico " +
							                                              "FROM grl_produto_servico_empresa A " +
							                                              "WHERE A.cd_empresa        = ? " +
																		  "  AND TRIM(A.id_reduzido) = ?");
					pstmt.setInt(1, produtoServicoEmpresa.getCdEmpresa());
					pstmt.setString(2, produtoServicoEmpresa.getIdReduzido());
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return ERR_ID_REDUZIDO_EM_USO;
					}
				}
			}
			
			PreparedStatement pstmtCodBarras = connection.prepareStatement("SELECT cd_produto_servico " +
														                   "	FROM grl_produto_servico  " +
														                   "	WHERE TRIM(id_produto_servico) = ? " +
														                   "    	  AND TRIM(id_produto_servico) <> '' " +
														                   "		  AND TRIM(id_produto_servico) IS NOT NULL");
			pstmtCodBarras.setString(1, produtoServico.getIdProdutoServico());
			ResultSet rs = pstmtCodBarras.executeQuery();
			if (rs.next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return ERR_ID_CODIGO_BARRAS_EM_USO;
			}
			
			//
			int cdProdutoServico = produtoServico instanceof Produto ? ProdutoDAO.insert((Produto)produtoServico, connection) :
				ProdutoServicoDAO.insert(produtoServico, connection);
			if (cdProdutoServico <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			// Salvando os dados em bem
			if (bemPatrimonial != null) {
				PreparedStatement pstmt = connection.prepareStatement("INSERT INTO bpm_bem (cd_bem,cd_classificacao,pr_depreciacao) VALUES (?, ?, ?)");
				pstmt.setInt(1, cdProdutoServico);
				if(bemPatrimonial.getCdClassificacao()==0)
					pstmt.setNull(2, Types.INTEGER);
				else
					pstmt.setInt(2, bemPatrimonial.getCdClassificacao());
				pstmt.setFloat(3, bemPatrimonial.getPrDepreciacao());
				pstmt.execute();
			}

			produtoServicoEmpresa.setCdProdutoServico(cdProdutoServico);
			produtoServicoEmpresa.setDtUltimaAlteracao(Util.getDataAtual());
			produtoServicoEmpresa.setDtCadastro(Util.getDataAtual());
			
			if (ProdutoServicoEmpresaDAO.insert(produtoServicoEmpresa, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (cdGrupo > 0) {
				if (ProdutoGrupoDAO.insert(new ProdutoGrupo(cdProdutoServico, cdGrupo, produtoServicoEmpresa.getCdEmpresa(), 1), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (cdGrupo2 > 0) {
				if (ProdutoGrupoDAO.insert(new ProdutoGrupo(cdProdutoServico, cdGrupo2, produtoServicoEmpresa.getCdEmpresa(), 0), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}
			
			if (atributos != null) {
				/* configura os atributos do produto */
				for (int i=0; i<atributos.size(); i++) {
					FormularioAtributoValor atributo = atributos.get(i);
					atributo.setCdProdutoServico(cdProdutoServico);
					if (FormularioAtributoValorDAO.insert(atributo, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
			}

			/*
			 * Parametros 
			 */
			if(parametros != null){
				if(ProdutoServicoParametroDAO.insert(new ProdutoServicoParametro(produtoServicoEmpresa.getCdEmpresa(), cdProdutoServico, (Integer)parametros.get("lgVerificarEstoqueNaVenda"), (Integer)parametros.get("lgBloqueiaVenda"), (Integer)parametros.get("lgPermiteDesconto"), (Integer)parametros.get("lgFazEntrega"), (Integer)parametros.get("lgNaoControlaEstoque"), (Integer)parametros.get("lgImprimeNaTabelaPreco"), (Integer)parametros.get("lgProdutoUsoConsumo")), connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}
			
			if (isConnectionNull)
				connection.commit();

			return cdProdutoServico;
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

	public static int insertNFe(ArrayList<HashMap<String,Object>> registros, int cdEmpresa, int cdDocumentoEntrada, int cdFornecedor, int cdLocalArmazenamento) {
		for(int i = 0; i < registros.size(); i++)	{
			//
			ProdutoServico 		  produtoServico           = registros.get(i)==null || registros.get(i).get("produto")          ==null ? null : (ProdutoServico)registros.get(i).get("produto");
			ProdutoServicoEmpresa produtoServicoEmpresa    = registros.get(i)==null || registros.get(i).get("produtoEmpresa")   ==null ? null : (ProdutoServicoEmpresa)registros.get(i).get("produtoEmpresa");
			int 				  cdGrupo                  = registros.get(i)==null || registros.get(i).get("cdGrupo")          ==null ? 0    : (Integer)registros.get(i).get("cdGrupo");
			float 				  qtEntrada 			   = registros.get(i)==null || registros.get(i).get("qtEntrada")        ==null ? 0 	  : (Float)registros.get(i).get("qtEntrada");
			float 				  vlUnitario  		       = registros.get(i)==null || registros.get(i).get("vlUnitario")       ==null ? 0 	  : (Float)registros.get(i).get("vlUnitario");
			int 				  cdUnidadeMedida   	   = registros.get(i)==null || registros.get(i).get("cdUnidadeMedida")  ==null ? 0    : (Integer)registros.get(i).get("cdUnidadeMedida");
			String 				  idReduzidoAntigo		   = registros.get(i)==null || registros.get(i).get("idReduzidoAntigo") ==null ? null : (String)registros.get(i).get("idReduzidoAntigo");
			
			if(insertNFe(produtoServico, produtoServicoEmpresa, cdGrupo, cdEmpresa, cdDocumentoEntrada, qtEntrada, vlUnitario, cdUnidadeMedida, idReduzidoAntigo, cdFornecedor, cdLocalArmazenamento) == -1)
				return -1;
		}
		
		return 1;
	}
	
	public static int insertNFe(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, int cdEmpresa, int cdDocumentoEntrada, float qtEntrada, float vlUnitario, int cdUnidadeMedida, String idReduzidoAntigo, int cdFornecedor, int cdLocalArmazenamento) {
		return insertNFe(produtoServico, produtoServicoEmpresa, cdGrupo, cdEmpresa, cdDocumentoEntrada, qtEntrada, vlUnitario, cdUnidadeMedida, idReduzidoAntigo, cdFornecedor, cdLocalArmazenamento, null);
	}

	public static int insertNFe(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, int cdEmpresa, int cdDocumentoEntrada, float qtEntrada, float vlUnitario, int cdUnidadeMedida, String idReduzidoAntigo, int cdFornecedor, int cdLocalArmazenamento, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			int cdProdutoServico = 0;
			if(produtoServico.getCdProdutoServico() <= 0){
				cdProdutoServico = produtoServico instanceof Produto ? ProdutoDAO.insert((Produto)produtoServico, connection) :
					ProdutoServicoDAO.insert(produtoServico, connection);
				if (cdProdutoServico <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

				produtoServicoEmpresa.setCdProdutoServico(cdProdutoServico);
				if (ProdutoServicoEmpresaDAO.insert(produtoServicoEmpresa, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
				
			}
			
			else{
				cdProdutoServico = produtoServico.getCdProdutoServico();
			}
			if(cdFornecedor > 0 && cdProdutoServico > 0){
				
				ArrayList<ItemComparator> crt= new java.util.ArrayList<ItemComparator>();
				
				ItemComparator item1= new ItemComparator("cd_fornecedor", "" + cdFornecedor, ItemComparator.EQUAL, Types.INTEGER);
				ItemComparator item2= new ItemComparator("cd_empresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER);
				ItemComparator item3= new ItemComparator("cd_produto_servico", "" + cdProdutoServico, ItemComparator.EQUAL, Types.INTEGER);
			    crt.add(item1);
			    crt.add(item2);
			    crt.add(item3);
			    ResultSetMap rsm = com.tivic.manager.adm.ProdutoFornecedorDAO.find(crt);
			    if(rsm.getLines().size() == 0){
			    	if(com.tivic.manager.adm.ProdutoFornecedorServices.insert(new ProdutoFornecedor(cdFornecedor, cdEmpresa, cdProdutoServico, 0, 0, 0, 0, idReduzidoAntigo, 0, 0, cdUnidadeMedida), connection) <= 0){
//						if (isConnectionNull)
//							Conexao.rollback(connection);
//						return -1;
					}
			    }
				
			}
			
			if (cdGrupo > 0) {

				ArrayList<ItemComparator> crt= new java.util.ArrayList<ItemComparator>();
				
				ItemComparator item1= new ItemComparator("cd_grupo", "" + cdGrupo, ItemComparator.EQUAL, Types.INTEGER);
				ItemComparator item2= new ItemComparator("cd_empresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER);
				ItemComparator item3= new ItemComparator("cd_produto_servico", "" + cdProdutoServico, ItemComparator.EQUAL, Types.INTEGER);
			    crt.add(item1);
			    crt.add(item2);
			    crt.add(item3);
			    ResultSetMap rsm = com.tivic.manager.alm.ProdutoGrupoDAO.find(crt);
			    if(rsm.getLines().size() == 0){
					if (ProdutoGrupoDAO.insert(new ProdutoGrupo(cdProdutoServico, cdGrupo, produtoServicoEmpresa.getCdEmpresa(), 1), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
			    }
			}
			if(cdDocumentoEntrada > 0 && cdEmpresa > 0 && cdProdutoServico > 0){
				
				
				ArrayList<ItemComparator> crt= new java.util.ArrayList<ItemComparator>();
				
				ItemComparator item1= new ItemComparator("cd_documento_entrada", "" + cdDocumentoEntrada, ItemComparator.EQUAL, Types.INTEGER);
				ItemComparator item2= new ItemComparator("cd_empresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER);
				ItemComparator item3= new ItemComparator("cd_produto_servico", "" + cdProdutoServico, ItemComparator.EQUAL, Types.INTEGER);
			    crt.add(item1);
			    crt.add(item2);
			    crt.add(item3);
			    ResultSetMap rsm = com.tivic.manager.alm.DocumentoEntradaItemDAO.find(crt);
			    if(rsm.getLines().size() == 0){
			    	Result resultado = DocumentoEntradaItemServices.insert(new DocumentoEntradaItem(cdDocumentoEntrada, cdProdutoServico, cdEmpresa, qtEntrada, vlUnitario, (float)0, (float)0, cdUnidadeMedida, null, 0/*cdItem*/, 0, 0, 0, 0, 0), connection);
			    	int cdItem = resultado.getCode();
			    	if(cdItem < 0){
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
			    	else{
			    		if(EntradaLocalItemDAO.insert(new EntradaLocalItem(cdProdutoServico, cdDocumentoEntrada, cdEmpresa, cdLocalArmazenamento, qtEntrada, 0/*qtConsignada*/, 0/*cdLocalArmazenamentoItem*/, cdItem), connection) < 0){
			    			if (isConnectionNull)
								Conexao.rollback(connection);
							return -1;
			    		}
			    	}
			    }
			}
			if (isConnectionNull)
				connection.commit();
			
			return cdProdutoServico;
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
	
	public static int delete(int cdEmpresa, int cdProdutoServico) {
		return delete(cdEmpresa, cdProdutoServico, null);
	}

	public static int delete(int cdEmpresa, int cdProdutoServico, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			connection.prepareStatement("DELETE FROM alm_produto_estoque " +
										"WHERE cd_produto_servico = " +cdProdutoServico+
										"  AND cd_empresa         = " +cdEmpresa).execute();

			connection.prepareStatement("DELETE FROM GRL_FORMULARIO_ATRIBUTO_VALOR " +
										"WHERE cd_produto_servico = " +cdProdutoServico+
										"  AND cd_empresa         = " +cdEmpresa).execute();

			connection.prepareStatement("DELETE FROM alm_produto_grupo " +
										"WHERE cd_produto_servico = " +cdProdutoServico+
										"  AND cd_empresa         = " +cdEmpresa).execute();
			
			connection.prepareStatement("DELETE FROM grl_produto_similar " +
										" WHERE cd_produto_servico = " +cdProdutoServico+
										" OR cd_similar = " + cdProdutoServico).execute();
			
			connection.prepareStatement("DELETE FROM grl_produto_servico_parametro " +
										" WHERE cd_produto_servico = " +cdProdutoServico+
										" 	AND cd_empresa         = " +cdEmpresa).execute();
			connection.prepareStatement("DELETE FROM adm_produto_servico_preco " +
					" WHERE cd_produto_servico = " +cdProdutoServico).execute();
			
			connection.prepareStatement("DELETE FROM grl_produto_codigo " +
					" WHERE cd_produto_servico = " +cdProdutoServico).execute();
			
			if (ProdutoServicoEmpresaDAO.delete(cdEmpresa, cdProdutoServico, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (ProdutoDAO.delete(cdProdutoServico, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
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

	public static int copyProdutos(int cdEmpresa, int[] cdsProdutosServicos, int cdEmpresaSource) {
		return copyProdutos(cdEmpresa, cdsProdutosServicos, cdEmpresaSource, null);
	}

	public static int copyProdutos(int cdEmpresa, int[] cdsProdutosServicos, int cdEmpresaSource, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_produto_servico " +
					"FROM grl_produto_servico_empresa " +
					"WHERE cd_produto_servico = ? " +
					"  AND cd_empresa = ? ");

			for (int i=0; cdsProdutosServicos!=null && i<cdsProdutosServicos.length; i++){
				pstmt.setInt(1, cdsProdutosServicos[i]);
				pstmt.setInt(2, cdEmpresa);
				ResultSet rs = pstmt.executeQuery();
				if (!rs.next()) {
					ProdutoServicoEmpresa prodEmpresa = null;
					if (cdEmpresaSource>0) {
						prodEmpresa = ProdutoServicoEmpresaDAO.get(cdEmpresaSource, cdsProdutosServicos[i], connection);
						if (prodEmpresa!=null)
							prodEmpresa.setCdEmpresa(cdEmpresa);
					}

					if (prodEmpresa!=null)
						prodEmpresa = new ProdutoServicoEmpresa(cdEmpresa,cdsProdutosServicos[i],0 /*cdUnidadeMedida*/,"" /*idReduzido*/,0 /*vlPrecoMedio*/,
																0 /*vlCustoMedio*/, 0 /*vlUltimoCusto*/,0 /*qtIdeal*/, 0 /*qtMinima*/, 0 /*qtMaxima*/, 0 /*qtDiasEstoque*/,
																0 /*qtPrecisaoCusto*/, 0 /*qtPrecisaoUnidade*/, 0 /*qtDiasGarantia*/, TP_MANUAL /*tpReabastecimento*/,
																CTL_QUANTIDADE /*tpControleEstoque*/, TRANSP_COMUM /*tpTransporte*/, ST_ATIVO /*stProdutoEmpresa*/,
																null /*dtDesativacao*/, "" /*nrOrdem*/, 0 /*lgEstoqueNegativo*/);


					if (ProdutoServicoEmpresaDAO.insert(prodEmpresa, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
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

	public static ResultSetMap findProdutosOfEmpresa(ArrayList<ItemComparator> criterios, int cdTipoOperacao) {
		return findProdutosOfEmpresa(criterios, cdTipoOperacao, null);
	}

	public static ResultSetMap findProdutosOfEmpresa(ArrayList<ItemComparator> criterios) {
		return findProdutosOfEmpresa(criterios, 0/*cdTipoOperacao*/, null);
	}

	public static ResultSetMap findProdutosOfEmpresa(ArrayList<ItemComparator> criterios, Connection connect) {
		return findProdutosOfEmpresa(criterios, 0/*cdTipoOperacao*/, connect);
	}

	public static ResultSetMap findProdutosOfEmpresa(ArrayList<ItemComparator> criterios, int cdTipoOperacao, Connection connect) {
		boolean isConnectionNull = connect == null;
		ResultSetMap rsm = null;
		try	{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			int cdEmpresa 	  = 0;
			int cdTabelaPreco = 0;
			String nmFabricanteFornecedor = "",  idProdutoServicoReduzido = "", id_produto_unico = "";
			boolean notFindSemPreco = false;
			int cdCliente = 0;
			String idProdutoServico = null;
			boolean hasCombustivel = false;
			boolean lgControlaEstoque = true;
			
			for (int i = 0; criterios != null && i < criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("cd_tipo_operacao"))
					cdTipoOperacao = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().toLowerCase().indexOf("cd_empresa") >= 0)
					cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().toLowerCase().indexOf("cd_tabela_preco") >= 0)
					cdTabelaPreco = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().toLowerCase().indexOf("nm_fabricante_fornecedor") >= 0)
					nmFabricanteFornecedor = criterios.get(i).getValue();
				else if (criterios.get(i).getColumn().toLowerCase().indexOf("id_produto_servico_reduzido") >= 0)
					idProdutoServicoReduzido = criterios.get(i).getValue();
				else if (criterios.get(i).getColumn().toLowerCase().indexOf("id_produto_unico") >= 0)
					id_produto_unico = criterios.get(i).getValue();
				else if (criterios.get(i).getColumn().equals("notFindSemPreco"))
					notFindSemPreco = true;
				else if (criterios.get(i).getColumn().equals("cdCliente"))
					cdCliente = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equals("id_produto_servico"))
					idProdutoServico = criterios.get(i).getValue();
				else if (criterios.get(i).getColumn().equals("cd_grupo_combustivel"))
					hasCombustivel = true;
				else if (criterios.get(i).getColumn().equals("M.LG_NAO_CONTROLA_ESTOQUE"))
					lgControlaEstoque = false;
				else
					crt.add(criterios.get(i));
			}
			String codigosCombustivel = com.tivic.manager.alm.GrupoServices.getAllCombustivel(cdEmpresa, connect);
			// Descobrindo tabela de preço do varejo
			int cdTabelaPrecoVarejo  = 0, cdTipoOperacaoVarejo  = 0;
			int cdTabelaPrecoAtacado = 0, cdTipoOperacaoAtacado = 0;
			
			if(cdTabelaPreco <= 0)	{
				cdTipoOperacaoVarejo = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, cdEmpresa);
				if(cdTipoOperacaoVarejo>0){
					TipoOperacao tipoOperacao = TipoOperacaoDAO.get(cdTipoOperacaoVarejo, connect);
					cdTabelaPrecoVarejo = tipoOperacao!=null ? tipoOperacao.getCdTabelaPreco() : 0;
				}
				// Descobrindo tabela de preço do atacado
				cdTipoOperacaoAtacado = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_ATACADO", 0, cdEmpresa);
				if(cdTipoOperacaoAtacado>0)	{
					TipoOperacao tipoOperacao = TipoOperacaoDAO.get(cdTipoOperacaoAtacado, connect);
					cdTabelaPrecoAtacado = tipoOperacao!=null ? tipoOperacao.getCdTabelaPreco() : 0;
				}
			}
			String sql = "SELECT B.cd_produto_servico, B.nr_referencia," +
			" A.cd_empresa, A.cd_unidade_medida, A.id_reduzido, A.vl_preco_medio, A.vl_custo_medio, A.vl_ultimo_custo, " +
			" A.qt_ideal, A.qt_minima, A.qt_maxima, A.qt_dias_estoque, A.qt_precisao_custo, A.qt_precisao_unidade, A.qt_dias_garantia, " +
			" A.tp_reabastecimento, A.tp_controle_estoque, A.tp_transporte, A.st_produto_empresa, A.dt_desativacao, A.nr_ordem, " +
			" A.lg_estoque_negativo, " +
			"	B.nm_produto_servico, B.txt_produto_servico, B.txt_dado_tecnico, B.txt_especificacao, " +
			"	B.tp_produto_servico, B.id_produto_servico, B.sg_produto_servico, " +
			"	B.cd_classificacao_fiscal, B.cd_ncm, B.nr_referencia, " +
			"	C.qt_embalagem, C.vl_peso_unitario, C.vl_peso_unitario_embalagem, " +
			"	D.nm_classificacao_fiscal, D.id_classificacao_fiscal, " +
			"	E.nm_unidade_medida, E.sg_unidade_medida, E.nr_precisao_medida, I.nm_grupo, I.cd_grupo, " +
			"	J.nm_pessoa AS nm_fabricante, " +
			"	K.nm_ncm " +
			(cdTipoOperacao > 0 || cdTabelaPreco>0 ? ", G.vl_preco, G.cd_tabela_preco ":"") +
			(cdTabelaPrecoVarejo>0 ? ",(SELECT vl_preco FROM adm_produto_servico_preco VRJ " +
			             			 "  WHERE VRJ.cd_produto_servico = A.cd_produto_servico " +
			             			 "    AND dt_termino_validade IS NULL " +
			             			 "    AND VRJ.cd_tabela_preco    = "+cdTabelaPrecoVarejo+") AS vl_preco_"+cdTipoOperacaoVarejo : "")+
			(cdTabelaPrecoAtacado>0 ? ",(SELECT vl_preco FROM adm_produto_servico_preco ATD " +
									  "  WHERE ATD.cd_produto_servico = A.cd_produto_servico " +
						 			  "    AND dt_termino_validade IS NULL " +
						 			  "    AND ATD.cd_tabela_preco    = "+cdTabelaPrecoAtacado+") AS vl_preco_"+cdTipoOperacaoAtacado : "")+
			" FROM grl_produto_servico B " +
			"	LEFT JOIN grl_produto C ON (B.cd_produto_servico = C.cd_produto_servico) " +
			"	LEFT JOIN grl_produto_servico_empresa A ON (" + (cdEmpresa>0 ? " A.cd_empresa = " + cdEmpresa + " AND " : "") + "B.cd_produto_servico = A.cd_produto_servico) " +
			"	LEFT OUTER JOIN adm_classificacao_fiscal 	    D ON (B.cd_classificacao_fiscal = D.cd_classificacao_fiscal) " +
			"	LEFT OUTER JOIN grl_unidade_medida 			    E ON (A.cd_unidade_medida = E.cd_unidade_medida) " +
			"	LEFT OUTER JOIN grl_pessoa 					    J ON (B.cd_fabricante = J.cd_pessoa) " +
			"	LEFT OUTER JOIN grl_ncm 					    K ON (B.cd_ncm = K.cd_ncm) " +
			"	LEFT OUTER JOIN grl_produto_servico_parametro	M ON (A.cd_empresa = M.cd_empresa AND A.cd_produto_servico = M.cd_produto_servico ) " +
			(cdTipoOperacao > 0 ? (!notFindSemPreco ? "LEFT OUTER " : "") + "JOIN adm_tipo_operacao 		 F ON (F.cd_tipo_operacao = " + cdTipoOperacao + ") "+
					              (!notFindSemPreco ? "LEFT OUTER " : "") + "JOIN adm_produto_servico_preco G ON (A.cd_produto_servico = G.cd_produto_servico " +
					              "                                            AND F.cd_tabela_preco    = G.cd_tabela_preco " +
					              "                                            AND G.dt_termino_validade IS NULL) " : "")+
			(cdTabelaPreco>0 && cdTipoOperacao<=0 ?
			(!notFindSemPreco ? "LEFT OUTER " : "") + "JOIN adm_produto_servico_preco G ON (A.cd_produto_servico = G.cd_produto_servico " +
			                                          "                                 AND G.cd_tabela_preco    = " +cdTabelaPreco+
			                                          "                                 AND G.dt_termino_validade IS NULL) " : "")+
			// Se não tiver definido a tabela de preço
			"LEFT OUTER JOIN alm_produto_grupo 				H ON (A.cd_produto_servico = H.cd_produto_servico " +
			(cdEmpresa > 0 ? "                 				  AND H.cd_empresa = " + cdEmpresa : "") +
			"                                  				  AND H.lg_principal = 1) " +
			"LEFT OUTER JOIN alm_grupo 						I ON (H.cd_grupo = I.cd_grupo) " +
			"LEFT OUTER JOIN ecm_foto_produto_empresa 		L ON (A.cd_produto_servico = L.cd_produto_servico " +
			"										    	  AND A.cd_empresa = L.cd_empresa AND L.nr_ordem = 1) " +
			"WHERE (A.st_produto_empresa = " + ST_ATIVO + " OR A.st_produto_empresa IS NULL) " +
			( lgControlaEstoque ?" AND ( M.LG_NAO_CONTROLA_ESTOQUE = 0 OR M.LG_NAO_CONTROLA_ESTOQUE IS NULL  )  ":"" )+
			(!idProdutoServicoReduzido.equals("") ? " AND (B.id_produto_servico = \'"+idProdutoServicoReduzido+"\' OR B.nr_referencia LIKE \'"+idProdutoServicoReduzido+"%\' OR A.id_reduzido LIKE \'"+idProdutoServicoReduzido+"%\') " +
											        "  OR  B.cd_produto_servico =  (SELECT PC.cd_produto_servico FROM grl_produto_codigo PC " +
											        "				                 WHERE PC.id_produto_servico = \'"+idProdutoServicoReduzido+"\'" +
											        "                                   OR PC.id_reduzido     LIKE \'"+idProdutoServicoReduzido+"\')": "")+
			(!id_produto_unico.equals("") ? " AND (B.id_produto_servico = \'"+id_produto_unico+"\' OR B.nr_referencia LIKE \'"+id_produto_unico+"%\' OR A.id_reduzido LIKE \'"+id_produto_unico+"\') " +
											"  OR  B.cd_produto_servico =  (SELECT PC.cd_produto_servico FROM grl_produto_codigo PC " +
									        "				                 WHERE PC.id_produto_servico = \'"+id_produto_unico+"\'" +
									        "                                   OR PC.id_reduzido     LIKE \'"+id_produto_unico+"\')": "")+
			// SQL para pesquisa produto e fabricante ao mesmo tempo, usado no PDV
			(!nmFabricanteFornecedor.equals("")?" AND (UPPER(J.nm_pessoa) LIKE \'%"+nmFabricanteFornecedor+"%\' "+
			                                    "  OR EXISTS (SELECT * FROM adm_produto_fornecedor FORN, grl_pessoa PES "+
			             						"             WHERE FORN.cd_produto_servico = A.cd_produto_servico "+
			             						"               AND FORN.cd_fornecedor      = PES.cd_pessoa "+
			             						"               AND UPPER(PES.nm_pessoa) LIKE \'%"+nmFabricanteFornecedor+"%\' ":"") + 
			(idProdutoServico != null ? " AND (EXISTS (SELECT * FROM grl_produto_codigo GPC "
				+ "															WHERE GPC.cd_produto_servico = B.cd_produto_servico "
				+ "															  AND GPC.id_produto_servico LIKE '"+idProdutoServico+"' "
				+ "															  AND GPC.lg_codigo_barras = 1) "
				+ "													OR (B.id_produto_servico LIKE '"+idProdutoServico+"'))" : " ") + 
			(hasCombustivel ? "AND I.cd_grupo IN " + codigosCombustivel +" " : " ");
			rsm = Search.find(
				sql,
				"ORDER BY B.nm_produto_servico", crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			
				//Verifica se o produto eh sujeito ao Imposto sobre Importacao para que se possa acrescentar o campo VUCV ao cadastrar novo produto em documento de entrada
				while(rsm.next()){
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
					if(cdTipoOperacao > 0){
						TipoOperacao tipoOperacao = TipoOperacaoDAO.get(cdTipoOperacao, connect);
						cdTabelaPreco = (cdTabelaPreco <= 0 ? (tipoOperacao!=null ? tipoOperacao.getCdTabelaPreco() : 0) : cdTabelaPreco);
						ResultSetMap rsmTabelaPromocional = DocumentoSaidaItemServices.getTabelaPromocional(cdTabelaPreco, rsm.getInt("cd_produto_servico"), rsm.getInt("cd_grupo"), cdCliente, connect);
						if(rsmTabelaPromocional.next()){
							rsm.setValueToField("CD_TABELA_PRECO", rsmTabelaPromocional.getInt("cd_tabela_preco"));
							rsm.setValueToField("VL_PRECO", rsmTabelaPromocional.getDouble("vl_preco"));
						}
					}
					
					if(idProdutoServico != null)
						rsm.setValueToField("id_produto_servico", idProdutoServico);
					
					Result registroEstoqueAtual = ProdutoEstoqueServices.getEstoqueAtual(criterios, connect);
					
					if(registroEstoqueAtual.getObjects().get("QT_ESTOQUE") != null)
						rsm.setValueToField("QT_ESTOQUE", registroEstoqueAtual.getObjects().get("QT_ESTOQUE"));
					else
						rsm.setValueToField("QT_ESTOQUE", 0);
					if(registroEstoqueAtual.getObjects().get("QT_ESTOQUE_CONSIGNADO") != null)
						rsm.setValueToField("QT_ESTOQUE_CONSIGNADO", registroEstoqueAtual.getObjects().get("QT_ESTOQUE_CONSIGNADO"));
					else
						rsm.setValueToField("QT_ESTOQUE_CONSIGNADO", 0);
					
					rsm.setValueToField("QT_ESTOQUE", ((double)registroEstoqueAtual.getObjects().get("QT_ESTOQUE") + (double)registroEstoqueAtual.getObjects().get("QT_ESTOQUE_CONSIGNADO")));
					
					
				}
				rsm.beforeFirst();
			
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
		}
		finally{
			Conexao.desconectar(connect);
		}
		
		return rsm;
	}

	public static ResultSetMap findProdutosOfEmpresa(int cdEmpresa, String idReduzido, int cdTabelaPreco) {
		Connection connect = Conexao.conectar();
		try	{
			String sql = 
				"SELECT B.cd_produto_servico, " +
				" A.cd_empresa, A.cd_unidade_medida, A.id_reduzido, A.vl_preco_medio, A.vl_custo_medio, A.vl_ultimo_custo, " +
				" A.qt_ideal, A.qt_minima, A.qt_maxima, A.qt_dias_estoque, A.qt_precisao_custo, A.qt_precisao_unidade, A.qt_dias_garantia, " +
				" A.tp_reabastecimento, A.tp_controle_estoque, A.tp_transporte, A.st_produto_empresa, A.dt_desativacao, A.nr_ordem, " +
				" A.lg_estoque_negativo, " +
			    "	B.nm_produto_servico, B.txt_produto_servico, B.txt_dado_tecnico, B.txt_especificacao, " +
				"	B.tp_produto_servico, B.id_produto_servico, B.sg_produto_servico, B.nr_referencia, " +
				"	B.cd_classificacao_fiscal, " +
				"	C.qt_embalagem, C.vl_peso_unitario, C.vl_peso_unitario_embalagem, " +
				"	D.nm_classificacao_fiscal, D.id_classificacao_fiscal, " +
				"	E.nm_unidade_medida, E.sg_unidade_medida, I.nm_grupo, I.cd_grupo, " +
				"	J.nm_pessoa AS nm_fabricante, G.vl_preco, G.cd_tabela_preco "+
				" FROM grl_produto_servico B " +
				"	LEFT OUTER JOIN grl_produto C ON (B.cd_produto_servico = C.cd_produto_servico) " +
				"	LEFT OUTER JOIN grl_produto_servico_empresa A ON (" + (cdEmpresa>0 ? " A.cd_empresa = " + cdEmpresa + " AND " : "") + "B.cd_produto_servico = A.cd_produto_servico) " +
				"	LEFT OUTER JOIN adm_classificacao_fiscal 	D ON (B.cd_classificacao_fiscal = D.cd_classificacao_fiscal) " +
				"	LEFT OUTER JOIN grl_unidade_medida 			E ON (A.cd_unidade_medida = E.cd_unidade_medida) " +
				"	LEFT OUTER JOIN grl_pessoa 					J ON (B.cd_fabricante = J.cd_pessoa) " +
				"   LEFT OUTER JOIN adm_produto_servico_preco G ON (A.cd_produto_servico = G.cd_produto_servico " +
				"                                               AND G.cd_tabela_preco    = " +cdTabelaPreco+
				"                                               AND G.dt_termino_validade IS NULL) "+
	            // Se não tiver definido a tabela de preço
				"LEFT OUTER JOIN alm_produto_grupo 				H ON (A.cd_produto_servico = H.cd_produto_servico " +
				(cdEmpresa > 0 ? "                 				  AND H.cd_empresa = " + cdEmpresa : "") +
				"                                  				  AND H.lg_principal = 1) " +
				"LEFT OUTER JOIN alm_grupo 						I ON (H.cd_grupo = I.cd_grupo) " +
				"WHERE (A.st_produto_empresa = " + ST_ATIVO +"    OR A.st_produto_empresa IS NULL) " +
				"  AND (A.id_reduzido LIKE \'"+idReduzido+"_\' OR A.id_reduzido = \'"+idReduzido+"\')"+
				"ORDER BY B.nm_produto_servico ";
			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static ResultSetMap findProdutosOfEmpresa(int cdEmpresa, String nrReferencia, int cdTabelaPreco, String nula) {
		Connection connect = Conexao.conectar();
		try	{
			String sql = 
				"SELECT B.cd_produto_servico, " +
				" A.cd_empresa, A.cd_unidade_medida, A.id_reduzido, A.vl_preco_medio, A.vl_custo_medio, A.vl_ultimo_custo, " +
				" A.qt_ideal, A.qt_minima, A.qt_maxima, A.qt_dias_estoque, A.qt_precisao_custo, A.qt_precisao_unidade, A.qt_dias_garantia, " +
				" A.tp_reabastecimento, A.tp_controle_estoque, A.tp_transporte, A.st_produto_empresa, A.dt_desativacao, A.nr_ordem, " +
				" A.lg_estoque_negativo, " +
			    "	B.nm_produto_servico, B.txt_produto_servico, B.txt_dado_tecnico, B.txt_especificacao, " +
				"	B.tp_produto_servico, B.id_produto_servico, B.sg_produto_servico, B.nr_referencia, " +
				"	B.cd_classificacao_fiscal, " +
				"	C.qt_embalagem, C.vl_peso_unitario, C.vl_peso_unitario_embalagem, " +
				"	D.nm_classificacao_fiscal, D.id_classificacao_fiscal, " +
				"	E.nm_unidade_medida, E.sg_unidade_medida, I.nm_grupo, I.cd_grupo, " +
				"	J.nm_pessoa AS nm_fabricante, G.vl_preco, G.cd_tabela_preco "+
				" FROM grl_produto_servico B " +
				"	LEFT OUTER JOIN grl_produto C ON (B.cd_produto_servico = C.cd_produto_servico) " +
				"	LEFT OUTER JOIN grl_produto_servico_empresa A ON (" + (cdEmpresa>0 ? " A.cd_empresa = " + cdEmpresa + " AND " : "") + "B.cd_produto_servico = A.cd_produto_servico) " +
				"	LEFT OUTER JOIN adm_classificacao_fiscal 	D ON (B.cd_classificacao_fiscal = D.cd_classificacao_fiscal) " +
				"	LEFT OUTER JOIN grl_unidade_medida 			E ON (A.cd_unidade_medida = E.cd_unidade_medida) " +
				"	LEFT OUTER JOIN grl_pessoa 					J ON (B.cd_fabricante = J.cd_pessoa) " +
				"   LEFT OUTER JOIN adm_produto_servico_preco G ON (A.cd_produto_servico = G.cd_produto_servico " +
				"                                               AND G.cd_tabela_preco    = " +cdTabelaPreco+
				"                                               AND G.dt_termino_validade IS NULL) "+
	            // Se não tiver definido a tabela de preço
				"LEFT OUTER JOIN alm_produto_grupo 				H ON (A.cd_produto_servico = H.cd_produto_servico " +
				(cdEmpresa > 0 ? "                 				  AND H.cd_empresa = " + cdEmpresa : "") +
				"                                  				  AND H.lg_principal = 1) " +
				"LEFT OUTER JOIN alm_grupo 						I ON (H.cd_grupo = I.cd_grupo) " +
				"WHERE (A.st_produto_empresa = " + ST_ATIVO +"    OR A.st_produto_empresa IS NULL) " +
				"  AND (B.nr_referencia LIKE \'"+nrReferencia+"_\' OR B.nr_referencia = \'"+nrReferencia+"\')"+
				"ORDER BY B.nm_produto_servico ";
			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
	}

	public static String exportToNFE(int cdEmpresa, int cdTabelaPreco)	{
		Connection connect = Conexao.conectar();
		try	{
			int cdTributoICMS = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0);
			int cdNaturezaOperacao = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_VAREJO", 0, cdEmpresa);
			int cdEstado = 0;
			ResultSet rs = connect.prepareStatement("SELECT * FROM grl_estado WHERE sg_estado = \'BA\'").executeQuery();
			if(rs.next())
				cdEstado = rs.getInt("cd_estado");
			//
			String sql = "SELECT B.cd_produto_servico, "+
						 "	A.cd_empresa, A.cd_unidade_medida, A.id_reduzido, A.vl_preco_medio, A.vl_custo_medio, A.vl_ultimo_custo, "+
						 "	A.qt_ideal, A.qt_minima, A.qt_maxima, A.qt_dias_estoque, A.qt_precisao_custo, A.qt_precisao_unidade, A.qt_dias_garantia, "+
						 "	A.tp_reabastecimento, A.tp_controle_estoque, A.tp_transporte, A.st_produto_empresa, A.dt_desativacao, A.nr_ordem,  "+
						 "	A.lg_estoque_negativo,  "+
						 "	B.nm_produto_servico, B.txt_produto_servico, "+
						 "	B.tp_produto_servico, B.id_produto_servico, B.sg_produto_servico, "+
						 "	B.cd_classificacao_fiscal,  "+
						 "	C.qt_embalagem, C.vl_peso_unitario, C.vl_peso_unitario_embalagem, "+
						 "	D.nm_classificacao_fiscal, D.id_classificacao_fiscal,  "+
						 "	E.nm_unidade_medida, E.sg_unidade_medida, G.vl_preco, G.cd_tabela_preco "+
						 "FROM grl_produto_servico B  "+
						 "LEFT OUTER JOIN grl_produto                 C ON (B.cd_produto_servico = C.cd_produto_servico) "+
						 "LEFT OUTER JOIN grl_produto_servico_empresa A ON (A.cd_empresa = 2 AND B.cd_produto_servico = A.cd_produto_servico) "+
						 "LEFT OUTER JOIN adm_classificacao_fiscal    D ON (B.cd_classificacao_fiscal = D.cd_classificacao_fiscal)  "+
						 "LEFT OUTER JOIN grl_unidade_medida 	    E ON (A.cd_unidade_medida = E.cd_unidade_medida)  "+
						 "JOIN adm_produto_servico_preco G ON (A.cd_produto_servico = G.cd_produto_servico  "+
						 "                                 AND G.cd_tabela_preco    = 1 "+
					     "					 			   AND G.dt_termino_validade IS NULL) "+
						 "WHERE (A.st_produto_empresa =  1 OR A.st_produto_empresa IS NULL) " +
						 "ORDER BY B.nm_produto_servico";
			rs = connect.prepareStatement(sql).executeQuery();
			int i = 0;
			while(rs.next())	{
				i++;
				String txtArquivo = "<sistema versao=\"1.02\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">";
				txtArquivo += "<det><prod>";
				txtArquivo += "<cProd>"+rs.getString("id_reduzido").trim()+"</cProd>";
				txtArquivo += "<xProd>"+Util.limparTexto(rs.getString("nm_produto_servico")).trim()+"</xProd>";
				txtArquivo += "<uCom>"+rs.getString("sg_unidade_medida").trim()+"</uCom>";
				txtArquivo += "<uTrib>"+rs.getString("sg_unidade_medida").trim()+"</uTrib>";
				txtArquivo += "<qTrib>"+Util.formatNumber(rs.getFloat("vl_preco"), "0.0000").replace(',', '.')+"</qTrib>";
				txtArquivo += "<vUnCom>"+Util.formatNumber(rs.getFloat("vl_preco"), "0.0000").replace(',', '.')+"</vUnCom>";
				txtArquivo += "<vUnTrib>"+Util.formatNumber(rs.getFloat("vl_preco"), "0.0000").replace(',', '.')+"</vUnTrib>";
				txtArquivo += "</prod>";
				// IMPOSTOS
				int cdClassificacaoFiscal = rs.getInt("cd_classificacao_fiscal");
				/*
				 *  Calcula base de cálculo
				 */
				ResultSetMap rsm = TributoServices.calcTributos(TributoAliquotaServices.OP_VENDA, cdNaturezaOperacao,
						                                        cdClassificacaoFiscal, rs.getInt("cd_produto_servico"),
						                                        0 /*cdPais*/, cdEstado, 0 /*cdCidade*/,
						                                        0/*vlBaseCalculo*/, connect);
				txtArquivo += "<imposto>";
				if(rsm.locate("cd_tributo", cdTributoICMS))	{
					txtArquivo += "<ICMS>";
					txtArquivo += "<orig>0</orig>"; // Origem
					// Situação tributária
					if(rsm.getInt("st_tributaria")==TributoAliquotaServices.ST_ISENTO)
						txtArquivo += "<CST>40</CST>"; // Isento
					else if(rsm.getInt("st_tributaria")==TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL)	{
						txtArquivo += "<CST>00</CST>";
						txtArquivo += "<modBC>0</modBC>";
						txtArquivo += "<pICMS>"+Util.formatNumber(rsm.getFloat("pr_aliquota"), "0.00").replace(',', '.')+"</pICMS>";
					}
					txtArquivo += "</ICMS>";
				}
				txtArquivo += "</imposto>";
				//
				txtArquivo += "</det>";
				txtArquivo += "</sistema>";
				com.tivic.manager.util.Util.printInFile("c:/TIVIC/PRODUTO-"+Util.formatDateTime(new GregorianCalendar(),"yyyyMMdd")+"-"+rs.getString("id_reduzido").trim()+".xml", txtArquivo);
			}
			return i+" exportados com sucesso!";
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static HashMap<String,Object> getAtributos(int cdProdutoServico, int cdEmpresa, int cdGrupo) {
		return getAtributos(cdProdutoServico, cdEmpresa, cdGrupo, null);
	}

	public static HashMap<String,Object> getAtributos(int cdProdutoServico, int cdEmpresa, int cdGrupo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			com.tivic.manager.alm.Grupo grupo = cdGrupo>0 ? com.tivic.manager.alm.GrupoDAO.get(cdGrupo, connect) : null;
			int cdFormulario = grupo==null ? 0 : grupo.getCdFormulario();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.tp_dado, C.nm_pessoa " +
					"FROM grl_formulario_atributo_valor A " +
					"JOIN grl_formulario_atributo B ON (A.cd_formulario_atributo = B.cd_formulario_atributo) " +
					"LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa_valor = C.cd_pessoa) " +
					"WHERE A.cd_produto_servico = ? " +
					(cdFormulario > 0 ? "  AND A.cd_formulario_atributo IN (SELECT cd_formulario_atributo " +
					"									FROM grl_formulario_atributo " +
					"									WHERE cd_formulario = ?)" : ""));
			pstmt.setInt(1, cdProdutoServico);
			if (cdFormulario > 0)
				pstmt.setInt(2, cdFormulario);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			HashMap<String,Object> atributos = new HashMap<String,Object>();
			while (rsm!=null && rsm.next()) {
				int cdFormularioAtributo = rsm.getInt("cd_formulario_atributo");
				int tpDado = rsm.getInt("tp_dado");
				Object atributoValor = tpDado==FormularioAtributoServices.TP_OPCOES ? (Object)new Integer(rsm.getInt("cd_opcao")) :
									   tpDado==FormularioAtributoServices.TP_PESSOA ? (Object)new Integer(rsm.getInt("cd_pessoa_valor")) : (Object)rsm.getString("txt_atributo_valor");
				atributos.put("ATRIBUTO_" + cdFormularioAtributo, atributoValor);
				if (tpDado==FormularioAtributoServices.TP_PESSOA)
					atributos.put("ATRIBUTO_PESSOA_VALOR_" + cdFormularioAtributo, rsm.getString("nm_pessoa"));
			}
			return atributos;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoEmpresaServices.getAtributos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa) {
		return update(produtoServico, produtoServicoEmpresa, 0, 0, null, null, null);
	}

	public static int update(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, ArrayList<FormularioAtributoValor> atributos) {
		return update(produtoServico, produtoServicoEmpresa, 0, 0, atributos, null, null);
	}

	public static int update(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, int cdGrupo2) {
		return update(produtoServico, produtoServicoEmpresa, cdGrupo, cdGrupo2, null, null, null);
	}

	public static int update(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, Connection connect) {
		return update(produtoServico, produtoServicoEmpresa, cdGrupo, 0, null, null, connect);
	}
	
	public static Result updateDna(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, int cdGrupo2, ArrayList<FormularioAtributoValor> atributos, HashMap<String, Object> parametros, byte[] imagem) {
		
		
		Connection connect = Conexao.conectar();
		
		try{
		
			connect.setAutoCommit(false);
			
			if(produtoServico.getIdProdutoServico() != null && !produtoServico.getIdProdutoServico().trim().equals("")){
				ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_produto_servico WHERE id_produto_servico = '"+produtoServico.getIdProdutoServico()+"' AND cd_produto_servico <> " + produtoServico.getCdProdutoServico()).executeQuery());
				if(rsm.next()){
					Conexao.rollback(connect);
					return new Result(ERR_ID_CODIGO_BARRAS_EM_USO, "Código de Barras já existe!");
				}
				else{
					rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_produto_codigo WHERE id_produto_servico = '"+produtoServico.getIdProdutoServico()+"' AND cd_produto_servico <> " + produtoServico.getCdProdutoServico() + " AND lg_codigo_barras = 1").executeQuery());
					if(rsm.next()){
						Conexao.rollback(connect);
						return new Result(ERR_ID_CODIGO_BARRAS_EM_USO, "Código de Barras já existe!");
					}
				}
			}
			
			int code = update(produtoServico, produtoServicoEmpresa, cdGrupo, cdGrupo2, atributos, null, parametros, connect);
			if(code < 0){
				Conexao.rollback(connect);
			}
			else{
				connect.commit();
			}
			Result resultado = new Result(code);
			if(produtoServicoEmpresa.getDtCadastro() != null)
				resultado.addObject("dtCadastro", Util.convCalendarString(produtoServicoEmpresa.getDtCadastro()));
			if(produtoServicoEmpresa.getDtUltimaAlteracao() != null)
				resultado.addObject("dtUltimaAlteracao", Util.convCalendarString(produtoServicoEmpresa.getDtUltimaAlteracao()));
			return resultado;
		}
		catch(Exception e){
			e.printStackTrace();
			Conexao.rollback(connect);
			return new Result(-1);
		}
		
		finally{
			Conexao.desconectar(connect);
		}
		
	}

	public static int update(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, int cdGrupo2, ArrayList<FormularioAtributoValor> atributos) {
		return update(produtoServico, produtoServicoEmpresa, cdGrupo, cdGrupo2, atributos, null, null);
	}

	public static int update(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, Bem bemPatrimonial) {
		return update(produtoServico, produtoServicoEmpresa, cdGrupo, 0, null, bemPatrimonial, null);
	}

	public static int update(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, int cdGrupo2, ArrayList<FormularioAtributoValor> atributos, Bem bemPatrimonial, Connection connection){
		return update(produtoServico, produtoServicoEmpresa, cdGrupo, 0, null, bemPatrimonial, null, null);
	}
	
	public static int update(ProdutoServico produtoServico, ProdutoServicoEmpresa produtoServicoEmpresa, int cdGrupo, int cdGrupo2, ArrayList<FormularioAtributoValor> atributos, Bem bemPatrimonial, HashMap<String, Object> parametros, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int cdRetorno = 0;
			if (bemPatrimonial != null) {
				cdRetorno = BemDAO.update(bemPatrimonial, connection);
				if (cdRetorno <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			PreparedStatement pstmtCodBarras = connection.prepareStatement("SELECT cd_produto_servico " +
														                   "	FROM grl_produto_servico  " +
														                   "	WHERE TRIM(id_produto_servico) = ?" +
														                   "		AND cd_produto_servico <> ?" +
														                   "		AND TRIM(id_produto_servico) <> ''" +
														                   "		AND TRIM(id_produto_servico) IS NOT NULL");
			PreparedStatement pstmtProduto = connection.prepareStatement("SELECT * FROM grl_produto_servico  " +
																           "	WHERE cd_produto_servico = ?");
				
			pstmtCodBarras.setString(1, produtoServico.getIdProdutoServico());
			pstmtCodBarras.setInt(2, produtoServico.getCdProdutoServico());
			ResultSet rs = pstmtCodBarras.executeQuery();
			if (rs.next()) {
				pstmtProduto.setInt(1, produtoServico.getCdProdutoServico());
				ResultSetMap rsmProd = new ResultSetMap(pstmtProduto.executeQuery());
				if(rsmProd.next()){
					if(!rsmProd.getString("id_produto_servico").equals(produtoServico.getIdProdutoServico())){
						if (isConnectionNull)
							Conexao.rollback(connection);
						return ERR_ID_CODIGO_BARRAS_EM_USO;
					}
				}
			}
			
			ProdutoServicoEmpresa prodServEmpTemp = ProdutoServicoEmpresaDAO.get(produtoServicoEmpresa.getCdEmpresa(), produtoServicoEmpresa.getCdProdutoServico(), connection);
			if (prodServEmpTemp != null) {
				produtoServicoEmpresa.setVlCustoMedio(prodServEmpTemp.getVlCustoMedio());
				produtoServicoEmpresa.setVlPrecoMedio(prodServEmpTemp.getVlPrecoMedio());
				produtoServicoEmpresa.setVlUltimoCusto(prodServEmpTemp.getVlUltimoCusto());
			}

			cdRetorno = produtoServico instanceof Produto ? ProdutoDAO.update(((Produto)produtoServico), connection) : ProdutoServicoDAO.update(produtoServico, connection);
			if (cdRetorno <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (ProdutoServicoEmpresaDAO.get(produtoServicoEmpresa.getCdEmpresa(), produtoServicoEmpresa.getCdProdutoServico(), connection)==null) {
				produtoServicoEmpresa.setDtUltimaAlteracao(Util.getDataAtual());
				produtoServicoEmpresa.setDtCadastro(Util.getDataAtual());
				if (ProdutoServicoEmpresaDAO.insert(produtoServicoEmpresa, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}
			else	{
				produtoServicoEmpresa.setDtUltimaAlteracao(Util.getDataAtual());
				if (ProdutoServicoEmpresaDAO.update(produtoServicoEmpresa, connection) <= 0) {

					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}
			
			// Grupos
			if(cdGrupo > 0 || cdGrupo2 > 0)	{
				connection.prepareStatement("DELETE FROM alm_produto_grupo " +
                                            "WHERE cd_produto_servico = "+produtoServico.getCdProdutoServico()+
                                            "  AND cd_empresa         = "+produtoServicoEmpresa.getCdEmpresa()).executeUpdate();
			}
			if (cdGrupo > 0) {
				if (ProdutoGrupoDAO.insert(new ProdutoGrupo(produtoServico.getCdProdutoServico(), cdGrupo, produtoServicoEmpresa.getCdEmpresa(), 1), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}
			if (cdGrupo2 > 0) {
				if (ProdutoGrupoDAO.insert(new ProdutoGrupo(produtoServico.getCdProdutoServico(), cdGrupo2, produtoServicoEmpresa.getCdEmpresa(), 0), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}
			// Grupo Principal definindo o formulário
			com.tivic.manager.alm.Grupo grupo = cdGrupo==0 ? null : com.tivic.manager.alm.GrupoDAO.get(cdGrupo, connection);
			int cdFormulario = grupo==null ? 0 : grupo.getCdFormulario();
			if (atributos != null) {
				/* remove atributos configurados anteriormente para o produto */
				PreparedStatement pstmt = connection.prepareStatement("DELETE FROM grl_formulario_atributo_valor " +
						"WHERE cd_produto_servico = ? " +
						(cdFormulario>0 ? "  AND cd_formulario_atributo IN (SELECT cd_formulario_atributo FROM grl_formulario_atributo WHERE cd_formulario = ?)" : ""));
				pstmt.setInt(1, produtoServicoEmpresa.getCdProdutoServico());
				if (cdFormulario > 0)
					pstmt.setInt(2, cdFormulario);
				pstmt.execute();

				/* configura os atributos do produto */
				for (int i=0; i<atributos.size(); i++) {
					FormularioAtributoValor atributo = (FormularioAtributoValor)atributos.get(i);
					if (FormularioAtributoValorDAO.insert(atributo, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
			}
			
			/*
			 * Parametros 
			 */
			if(parametros != null){
				ProdutoServicoParametro produtoParametro = ProdutoServicoParametroDAO.get(produtoServicoEmpresa.getCdEmpresa(), produtoServicoEmpresa.getCdProdutoServico(), connection);
				if(produtoParametro != null){
					produtoParametro.setLgBloqueiaVenda((Integer)parametros.get("lgBloqueiaVenda"));
					produtoParametro.setLgFazEntrega((Integer)parametros.get("lgFazEntrega"));
					produtoParametro.setLgImprimeNaTabelaPreco((Integer)parametros.get("lgImprimeNaTabelaPreco"));
					produtoParametro.setLgNaoControlaEstoque((Integer)parametros.get("lgNaoControlaEstoque"));
					produtoParametro.setLgPermiteDesconto((Integer)parametros.get("lgPermiteDesconto"));
					produtoParametro.setLgProdutoUsoConsumo((Integer)parametros.get("lgProdutoUsoConsumo"));
					produtoParametro.setLgVerificarEstoqueNaVenda((Integer)parametros.get("lgVerificarEstoqueNaVenda"));
					if(ProdutoServicoParametroDAO.update(new ProdutoServicoParametro(produtoServicoEmpresa.getCdEmpresa(), produtoServicoEmpresa.getCdProdutoServico(), (Integer)parametros.get("lgVerificarEstoqueNaVenda"), (Integer)parametros.get("lgBloqueiaVenda"), (Integer)parametros.get("lgPermiteDesconto"), (Integer)parametros.get("lgFazEntrega"), (Integer)parametros.get("lgNaoControlaEstoque"), (Integer)parametros.get("lgImprimeNaTabelaPreco"), (Integer)parametros.get("lgProdutoUsoConsumo")), connection) < 0){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
				else{
					if(ProdutoServicoParametroDAO.insert(new ProdutoServicoParametro(produtoServicoEmpresa.getCdEmpresa(), produtoServicoEmpresa.getCdProdutoServico(), (Integer)parametros.get("lgVerificarEstoqueNaVenda"), (Integer)parametros.get("lgBloqueiaVenda"), (Integer)parametros.get("lgPermiteDesconto"), (Integer)parametros.get("lgFazEntrega"), (Integer)parametros.get("lgNaoControlaEstoque"), (Integer)parametros.get("lgImprimeNaTabelaPreco"), (Integer)parametros.get("lgProdutoUsoConsumo")), connection) < 0){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
			}

			if (isConnectionNull)
				connection.commit();
			return cdRetorno;
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

	public static ResultSetMap getUnidadeMedidaOf(int cdProdutoServico, int cdEmpresa) {
		return getUnidadeMedidaOf(cdProdutoServico, cdEmpresa, null);
	}

	public static ResultSetMap getUnidadeMedidaOf(int cdProdutoServico, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT B.* " +
					"FROM grl_produto_servico_empresa A, grl_unidade_medida B " +
					"WHERE A.cd_produto_servico = " +cdProdutoServico+
					"  AND A.cd_empresa         = "+cdEmpresa+
					"  AND (A.cd_unidade_medida = B.cd_unidade_medida "+
					"   OR A.cd_unidade_medida IN (SELECT cd_unidade_origem FROM grl_unidade_conversao "+
					"                              WHERE cd_unidade_destino = B.cd_unidade_medida) "+
					"   OR A.cd_unidade_medida IN (SELECT cd_unidade_destino FROM grl_unidade_conversao "+
					"                              WHERE cd_unidade_origem = B.cd_unidade_medida)) " +
					"ORDER BY nm_unidade_medida");
			return new ResultSetMap(pstmt.executeQuery());
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

	public static boolean isIdReduzidoByGrupo(int cdEmpresa) {
		try {
			// Mascara para definir o tamanho e a máscara do ID Reduzido
			String idReduzidoMask = ParametroServices.getValorOfParametro("DS_ID_REDUZIDO_MASK", "", cdEmpresa, null);
			return idReduzidoMask!=null && idReduzidoMask.indexOf("G")>=0;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return false;
		}
	}

	public static String getNextIdReduzido(int cdEmpresa) {
		return getNextIdReduzido(cdEmpresa, 0, null);
	}

	public static String getNextIdReduzido(int cdEmpresa, Connection connect) {
		return getNextIdReduzido(cdEmpresa, 0, connect);
	}

	public static String getNextIdReduzido(int cdEmpresa, int cdGrupo) {
		return getNextIdReduzido(cdEmpresa, cdGrupo, null);
	}

	public static String getNextIdReduzido(int cdEmpresa, int cdGrupo, Connection connect) {
		boolean isConnectionNull = (connect==null);
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			// Mascara para definir o tamanho e a máscara do ID Reduzido
			String idReduzidoMask = ParametroServices.getValorOfParametro("DS_ID_REDUZIDO_MASK", "", cdEmpresa, connect);
			idReduzidoMask 	      = idReduzidoMask!=null ? idReduzidoMask.toUpperCase() : "";
			int length 		      = idReduzidoMask.length()>0 ? idReduzidoMask.length() : 6;
			boolean isByGrupo     = idReduzidoMask.indexOf("G")>=0;
			boolean hasSeparator  = isByGrupo && idReduzidoMask.indexOf("-")>=0;
 			if(isByGrupo && cdGrupo<=0)
				return "";
			//
			ResultSet rs = null;
			if(hasSeparator) {
				rs = connect.prepareStatement(
						"SELECT MAX(CAST(SUBSTRING(id_reduzido, 1, POSITION('-' IN id_reduzido)-1) as INTEGER)) " +
						"FROM grl_produto_servico_empresa A " +
						"JOIN alm_produto_grupo B ON (A.cd_produto_servico = B.cd_produto_servico " +
						"                         AND B.cd_empresa         = "+cdEmpresa+
						"                         AND B.cd_grupo 			= "+cdGrupo+") "+
						"WHERE A.id_reduzido                  IS NOT NULL " +
						"  AND POSITION('-' IN A.id_reduzido) > 0 "+
						"  AND A.cd_empresa                   = "+cdEmpresa).executeQuery();
			}
			else {
				// try catch para contornar as situações em que contem string no banco
				try{
					rs = connect.prepareStatement(
							"SELECT MAX(CAST(id_reduzido AS INTEGER)) " +
							"FROM grl_produto_servico_empresa A " +
							(isByGrupo ? "JOIN alm_produto_grupo B ON (B.cd_produto_servico = A.cd_produto_servico " +
									     "                         AND B.cd_empresa         = "+cdEmpresa+
									     "                         AND B.cd_grupo 			= "+cdGrupo+") " : "")+
	
							"WHERE A.id_reduzido       IS NOT NULL " +
							"  AND TRIM(A.id_reduzido) <> \'\' " +
							"  AND A.cd_empresa         = "+cdEmpresa).executeQuery();
				}catch(Exception e){}
			}
			int seq = rs != null && rs.next() ? rs.getInt(1) : 1;
			String idReduzido = "";
			// Repete esse bloco até encontrar um ID não utilizado
			do {
				seq++;
				idReduzido = "";
				if(isByGrupo)	{
					com.tivic.manager.alm.Grupo grupo = com.tivic.manager.alm.GrupoDAO.get(cdGrupo, connect);
					// Tamanho do ID-GRUPO do grupo que faz parte do ID-REDUZIDO
					int lenGrupo = 0;
					for(int i=0; i<idReduzidoMask.length(); i++)
						if(idReduzidoMask.charAt(i)=='G')
							lenGrupo++;
					//
					int lenSequencial   = length - lenGrupo - (hasSeparator ? 1 : 0);
					String idGrupo 		= grupo!=null ? (grupo.getIdGrupo()!=null && !grupo.getIdGrupo().equals("") ? grupo.getIdGrupo() : String.valueOf(grupo.getCdGrupo())) : "";
					idGrupo 		    = lenGrupo>idGrupo.length() ? Util.fill(idGrupo, lenGrupo, '0', 'E') : idGrupo;
					String idSequencial = lenSequencial>String.valueOf(seq).length() ? Util.fillNum(seq, lenSequencial) : String.valueOf(seq);
					// Formando o ID-REDUZIDO
					int iS = 0, iG = 0;
					// Se o grupo estiver na primeira posição
					int qtMinima = 1;
					if(idReduzidoMask.charAt(0)=='G') {
						for(int i=0; i<lenSequencial; i++)
							qtMinima = qtMinima * 10;  
					}
					if(seq > qtMinima && (!hasSeparator || !isByGrupo)) {
						idReduzido = idSequencial;
						while(idReduzido.length() < (lenGrupo + lenSequencial))
							idReduzido = "0"+idReduzido;
					}
					else {
						for(int i=0; i<idReduzidoMask.length(); i++) {
							if(idReduzidoMask.charAt(i)=='G' && iG < idGrupo.length())	{
								idReduzido += (lenGrupo < idGrupo.length()) ? idGrupo          : idGrupo.charAt(iG);
								iG         += (lenGrupo < idGrupo.length()) ? idGrupo.length() : 1;
							}
							else if(idReduzidoMask.charAt(i)=='-') {
								idReduzido += '-';
							}
							else if(iS < idSequencial.length()) {
								//
								idReduzido += (lenSequencial < idSequencial.length()) ? idSequencial          : idSequencial.charAt(iS);
								iS         += (lenSequencial < idSequencial.length()) ? idSequencial.length() : 1;
							}
						}
					}
				}
				else
					idReduzido = com.tivic.manager.util.Util.fillNum(seq, length);
			}
			while(idReduzidos.get(cdEmpresa+":"+idReduzido)!=null);
			// Salva pra que outro usuário não use o mesmo id
			idReduzidos.put(cdEmpresa+":"+idReduzido, idReduzido);
			//
			return idReduzido;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return "";
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllGrupos(int cdProdutoServico, int cdEmpresa) {
		return getAllGrupos(cdProdutoServico, cdEmpresa, null);
	}

	public static ResultSetMap getAllGrupos(int cdProdutoServico, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.lg_principal " +
					"FROM alm_grupo A, alm_produto_grupo B " +
					"WHERE A.cd_grupo = B.cd_grupo " +
					"  AND B.cd_produto_servico = ? " +
					"  AND B.cd_empresa = ?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdEmpresa);
			return new ResultSetMap(pstmt.executeQuery());
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

	public static final int setOrdemProdutosServicos(int cdEmpresa, ServletRequest request) {
		return setOrdemProdutosServicos(cdEmpresa, request, null);
	}

	public static final int setOrdemProdutosServicos(int cdEmpresa, ServletRequest request, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int countItens = RequestUtilities.getAsInteger(request, "countItens", 0);
			for (int i=0; i<countItens; i++) {
				int cdProdutoServico = RequestUtilities.getAsInteger(request, "cdProdutoServico_" + (i+1), 0);
				String nrOrdem = RequestUtilities.getAsString(request, "nrOrdem_" + (i+1), "");
				ProdutoServicoEmpresa item = ProdutoServicoEmpresaDAO.get(cdEmpresa, cdProdutoServico, connection);
				boolean isInsert = item==null;
				item = item!=null ? item : new ProdutoServicoEmpresa(cdEmpresa,
						cdProdutoServico,
						0 /*cdUnidadeMedida*/,
						"" /*idReduzido*/,
						0 /*vlPrecoMedio*/,
						0 /*vlCustoMedio*/,
						0 /*vlUltimoCusto*/,
						0 /*qtIdeal*/,
						0 /*qtMinima*/,
						0 /*qtMaxima*/,
						0 /*qtDiasEstoque*/,
						0 /*qtPrecisaoCusto*/,
						0 /*qtPrecisaoUnidade*/,
						0 /*qtDiasGarantia*/,
						ProdutoServicoEmpresaServices.TP_MANUAL /*tpReabastecimento*/,
						ProdutoServicoEmpresaServices.CTL_QUANTIDADE /*tpControleEstoque*/,
						ProdutoServicoEmpresaServices.TRANSP_COMUM /* tpTransporte*/,
						ProdutoServicoEmpresaServices.ST_ATIVO /*stProdutoEmpresa*/,
						null /*dtDesativacao*/,
						nrOrdem,
						0 /*lgEstoqueNegativo*/);
				item.setNrOrdem(nrOrdem);
				if ((isInsert ? ProdutoServicoEmpresaDAO.insert(item, connection) : ProdutoServicoEmpresaDAO.update(item, connection)) <= 0) {
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

	public static HashMap<String, Object> updateCustosAndPrecosMedios(int cdEmpresa, int cdProdutoServico) {
		return updateCustosAndPrecosMedios(cdEmpresa, cdProdutoServico, null);
	}

	public static HashMap<String, Object> updateCustosAndPrecosMedios(int cdEmpresa, int cdProdutoServico, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : null;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_produto_servico", Integer.toString(cdProdutoServico), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = ProdutoEstoqueServices.recalcularUltimoCusto(cdEmpresa, criterios, connection);
			if (rsm==null || !rsm.next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("cdProdutoServico", cdProdutoServico);
			hash.put("cdEmpresa", cdEmpresa);
			hash.put("vlUltimoCusto", rsm.getFloat("VL_ULTIMO_CUSTO"));

			rsm = ProdutoEstoqueServices.recalcularValoresMedios(cdEmpresa, criterios, connection);
			if (rsm==null || !rsm.next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}
			hash.put("vlPrecoMedio", rsm.getFloat("VL_PRECO_MEDIO"));
			hash.put("vlCustoMedio", rsm.getFloat("VL_CUSTO_MEDIO"));

			if (isConnectionNull)
				connection.commit();

			return hash;
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

	public static Result setProdutoEstoque(int cdEmpresa, int cdProdutoServico, int cdBalanco, Double qtEstoque, String idProdutoServico) {
		return setProdutoEstoque(cdEmpresa, cdProdutoServico, cdBalanco, qtEstoque, idProdutoServico, null);
	}

	public static Result setProdutoEstoque(int cdEmpresa, int cdProdutoServico, int cdBalanco, Double qtEstoque, String idProdutoServico, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if(cdProdutoServico<=0 || qtEstoque<0)
				return new Result(-1, "Alguma informação obrigatória não foi definida: [Cód. do Produto:"+cdProdutoServico+",Estoque:"+qtEstoque+"]");
			// Conexão com o BD
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			Balanco balanco = BalancoDAO.get(cdBalanco, cdEmpresa);
			if (balanco==null)	{
				connection.rollback();
				return new Result(-1, "Não há balanco ativo!");
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_empresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_balanco", "" + cdBalanco, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_produto_servico", "" + cdProdutoServico, ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsm = com.tivic.manager.alm.BalancoProdutoServicoDAO.find(criterios, connection);
			
			if(rsm.next()){
				if(com.tivic.manager.alm.BalancoProdutoServicoDAO.update(new BalancoProdutoServico(cdBalanco, cdEmpresa, cdProdutoServico, 0.0, qtEstoque, 0), connection) <= 0){
					Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar atualizar o produto ao balanço.");
				}
			}
			else{
				if(com.tivic.manager.alm.BalancoProdutoServicoDAO.insert(new BalancoProdutoServico(cdBalanco, cdEmpresa, cdProdutoServico, 0.0, qtEstoque, 0), connection) <= 0){
					Conexao.rollback(connection);
					return new Result(-1, "Erro ao tentar inserir o produto ao balanço.");
				}
			}
			
			// Atualizando idProdutoServico
			if(idProdutoServico!=null && !idProdutoServico.equals(""))
				connection.prepareStatement("UPDATE grl_produto_servico SET id_produto_servico = \'"+idProdutoServico+"\' " +
						                    "WHERE cd_produto_servico = "+cdProdutoServico).executeUpdate();
			if (isConnectionNull)
				connection.commit();

			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar atualizar estoque!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result setProdutoPreco(int cdEmpresa, ArrayList<String> produtos, int cdTabelaPreco, float vlPreco) {
		Connection connection = Conexao.conectar();
		try	{
			connection.setAutoCommit(false);
			for(int i=0; i<produtos.size(); i++)	{
				int cdProdutoServico = Integer.parseInt(produtos.get(i));
				int ret = setProdutoPreco(cdEmpresa,cdProdutoServico,cdTabelaPreco,vlPreco,null,connection).getCode();
				if(ret<=0)	{
					Conexao.rollback(connection);
					return new Result(ret, "Erro ao tentar atualizar o produto: "+cdProdutoServico);
				}
			}
			connection.commit();
			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar atualizar lista de preço em mais de um produto!", e);
		}
		finally	{
			
		}
	}

	public static Result setProdutoPreco(int cdEmpresa, int cdProdutoServico, int cdTabelaPreco, float vlPreco, String idProdutoServico) {
		return setProdutoPreco(cdEmpresa, cdProdutoServico, cdTabelaPreco, vlPreco, idProdutoServico, null);
	}

	public static Result setProdutoPreco(int cdEmpresa, int cdProdutoServico, int cdTabelaPreco, float vlPreco, String idProdutoServico, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if(cdTabelaPreco<=0 || cdProdutoServico<=0 || vlPreco<=0)
				return new Result(-1, "Alguma informação obrigatória não foi definida: [Cód. do Produto:"+cdProdutoServico+",Tabela de Preco:"+cdTabelaPreco+",Preco:"+vlPreco+"]");
			// Conexão
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			// ARMENGO
			connection.prepareStatement("UPDATE grl_produto_servico_empresa SET qt_ideal = "+vlPreco+
                                        " WHERE cd_produto_servico = "+cdProdutoServico).executeUpdate();
			// Define o novo preço
			Result result = new Result(1);
			ProdutoServicoPreco produtoPreco = new ProdutoServicoPreco(cdTabelaPreco, cdProdutoServico, 0 /*cdProdutoServicoPreco*/, null /*dtTerminoValidade*/, vlPreco);
			result.setCode(ProdutoServicoPrecoServices.insert(produtoPreco, connection));
			if(result.getCode()<=0)	{
				if(isConnectionNull)
					Conexao.rollback(connection);
				result.setMessage("Não foi possível gravar preço na tabela de preços do produto!");
				return result;
			}
			// Atualizando idProdutoServico
			if(idProdutoServico!=null && !idProdutoServico.equals(""))
				connection.prepareStatement("UPDATE grl_produto_servico SET id_produto_servico = \'"+idProdutoServico+"\' " +
						                    "WHERE cd_produto_servico = "+cdProdutoServico).executeUpdate();
			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar atualizar preço!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/*
	 * @category PAF-ECF
	 */
	public static String gerarRelatorioProdutos(int cdEmpresa){
		return gerarRelatorioProdutos(cdEmpresa, "");
	}
	public static String gerarRelatorioProdutos(int cdEmpresa, String nmPath){	
		try{
			Util.printInFile("C:/Marlon.log", "\nInicio" );
			ItemComparator i = new ItemComparator("lgEstoque", "1", ItemComparator.EQUAL, java.sql.Types.INTEGER);
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(i);
			Util.printInFile("C:/Marlon.log", "\ncriterios: " + criterios);
			ResultSetMap rsmProdutos = com.tivic.manager.alm.ProdutoEstoqueServices.findProduto(cdEmpresa, criterios, null);
			String texto = "";		
			Util.printInFile("C:/Marlon.log", "\nrsmProdutos: " + rsmProdutos);
			/*********************************** IDENTIFICAÇÃO DO ESTABELECIMENTO USUÁRIO DO PAF-ECF E DO ECF ******************************************************************/
			Empresa   empresa 			 = com.tivic.manager.grl.EmpresaDAO.get(cdEmpresa);
			String    cnpj    			 = empresa.getNrCnpj();
	//		String    inscricaoEstadual  = empresa.getNrInscricaoEstadual();
	//		String    inscricaoMunicipal = empresa.getNrInscricaoMunicipal();
	//		String    razaoSocial 		 = empresa.getNmRazaoSocial();
	//		String    numeroFabricacao   = null;
	//		String    mfAdiciona 		 = null;
	//		String    tipoECF 			 = null;
	//		String    marca 			 = null;
	//		String    modelo 			 = null;
	//		Calendar calendario 		 = Calendar.getInstance();
	//		String   data 				 = calendario.get(Calendar.YEAR) + Util.fillNum(calendario.get(Calendar.MONTH), 2) + Util.fillNum(calendario.get(Calendar.DAY_OF_MONTH), 2);
	//		String   hora 				 = Util.fillNum(calendario.get(Calendar.HOUR_OF_DAY), 2) + Util.fillNum(calendario.get(Calendar.MINUTE), 2) + Util.fillNum(calendario.get(Calendar.SECOND), 2);				
			//Tipo de Registro
	//		texto += "E1"; 		
	//		//CNPJ
	//		texto += Util.fill(cnpj, 14, '0', 'E');		
	//		//Inscricão Estadual
	//		texto += Util.fill(inscricaoEstadual, 14, ' ', 'D');				
	//		//Inscricão Municipal
	//		texto += Util.fill(inscricaoMunicipal, 14, ' ', 'D');				
	//		//Razão Social
	//		texto += Util.fill(razaoSocial, 50, ' ', 'D');				
	//		//Número de Fabricação
	//		texto += Util.fill(numeroFabricacao, 20, ' ', 'D');				
	//		//MF Adicional
	//		texto += Util.fillAlpha(mfAdiciona, 1);		
	//		//Tipo de ECF
	//		texto += Util.fill(tipoECF, 7, ' ', 'D');		
	//		//Marca do ECF
	//		texto += Util.fill(marca, 20, ' ', 'D');		
	//		//Modelo do ECF
	//		texto += Util.fill(modelo, 20, ' ', 'D');		
	//		//Data do Estoque
	//	    texto += Util.fillAlpha(data, 8);		
	//	    //Hora do estoque
	//	    texto += Util.fillAlpha(hora, 6);	  		
	//		texto += "\n";
			/*********************************** RELAÇÃO DAS MERCADORIAS EM ESTOQUE ******************************************************************/
			int quantidadeDeRegistros = 0;		
			while(rsmProdutos.next()){			
				String codigoProdutoServico      = rsmProdutos.getString("id_produto_servico");			
				String dsProdutoServico          = (rsmProdutos.getString("nm_produto_servico") != null) ? rsmProdutos.getString("nm_produto_servico") : "";			
				String unidadeMedida 		     = rsmProdutos.getString("sg_unidade_medida");			
				//String vlUnitario                = String.valueOf(rsmProdutos.getFloat("vl_preco"));
	//			String vlUnitario                = String.valueOf(rsmProdutos.getFloat("vl_unitario"));			
	//			String nmStTributaria 		     = rsmProdutos.getString("nm_classificacao_fiscal");			
	//			String idStTributaria 		     = rsmProdutos.getString("id_classificacao_fiscal");			
				boolean estoqueNegativo 	     = ((rsmProdutos.getFloat("qt_estoque") + rsmProdutos.getFloat("qt_estoque_consignado")) < 0);			
				String mensuracao 		         = (estoqueNegativo) ? "-" : "+";			
				java.text.DecimalFormat df       = new java.text.DecimalFormat("#,###.000");			
				String quantidadeEstoque         = df.format(rsmProdutos.getFloat("qt_estoque") + rsmProdutos.getFloat("qt_estoque_consignado"));			
				String quantidadeEstoqueAuxiliar = "";
				for(int k = 0; k < quantidadeEstoque.length(); k++){
					if(!quantidadeEstoque.substring(k, k+1).equals(",") && !quantidadeEstoque.substring(k, k+1).equals(".")){
						quantidadeEstoqueAuxiliar += quantidadeEstoque.substring(k, k+1);
					}
				}		
				//Tipo de Registro
				texto += "E2"; 		
				//CNPJ
				texto += Util.fill(cnpj, 14, '0', 'E');						
				//Codigo do produto
				texto += Util.fill(codigoProdutoServico, 14, ' ', 'D');						
				//Descrissão do produto
				texto += Util.fill(dsProdutoServico.trim(), 50, ' ', 'D');			
				//Unidade de Medida do Produto
				texto += Util.fill(unidadeMedida.trim(), 6, ' ', 'D');				
				//Mensuração de Estoque
				texto += Util.fillAlpha(mensuracao, 1);			
	//			//Valor Unitários
	//			texto += Util.fill(vlUnitario, 6, '0', 'D');			
	//			//Situação Tributária
	//			texto += Util.fill(nmStTributaria, 50, ' ', 'D');
	//			//Código Situação Tributária
	//			texto += Util.fill(idStTributaria.trim(), 6, '0', 'D');						
				//Quantidade em estoque
				texto += Util.fill(quantidadeEstoqueAuxiliar, 9, '0', 'E');			
				texto += "\n";
				quantidadeDeRegistros++; 
			}
			/*********************************** Totalização do Arquivo ******************************************************************/
			//Tipo de Registro
	//		texto += "E9"; 
	//				
	//		//CNPJ
	//		texto += Util.fill(cnpj, 14, '0', 'E');
	//				
	//		//Inscrissão Estadual
	//		texto += Util.fill(inscricaoEstadual, 14, ' ', 'D');
	//				
	//		//Quantidade Total de Produtos
	//		texto += Util.fillNum(quantidadeDeRegistros, 6);
	//		
	//		texto += "\n";
			/*********************************** Assinatura Digital ******************************************************************/
			//String assinaturaDigital = getEAD();		
			//Tipo de Registro
		//	texto += "EAD";		
		//	texto += Util.fill(assinaturaDigital, 256, ' ', 'D');		
		//	texto += "\n";		
			  
			/*********************************** Gera Arquivo ******************************************************************/	    
	         return texto;
		}catch(Exception e){
			 Util.printInFile("C:/Marlon.log", "\nInicio e: " + e.toString());
			 return null;
		}
	}
	
	public static String gerarRelatorioProdutosTributados(int cdEmpresa){
		return gerarRelatorioProdutosTributados(cdEmpresa, "");
	}
	
	public static String gerarRelatorioProdutosTributados(int cdEmpresa, String nmPath){
		
		ItemComparator i = new ItemComparator("lgEstoque", "1", ItemComparator.EQUAL, java.sql.Types.INTEGER);
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(i);
		ResultSetMap rsmProdutos = com.tivic.manager.alm.ProdutoEstoqueServices.findProduto(cdEmpresa, criterios, null);
		String texto = "";				
		/*********************************** IDENTIFICAÇÃO DO ESTABELECIMENTO USUÁRIO DO PAF-ECF E DO ECF ******************************************************************/
		Empresa empresa 		  = com.tivic.manager.grl.EmpresaDAO.get(cdEmpresa);
		String cnpj 			  = empresa.getNrCnpj();
//		String inscricaoEstadual  = empresa.getNrInscricaoEstadual();
//		String inscricaoMunicipal = empresa.getNrInscricaoMunicipal();
//		String razaoSocial 		  = empresa.getNmRazaoSocial();			
//		//Tipo de Registro
//		texto += "P1"; 		
//		//CNPJ
//		texto += Util.fill(cnpj, 14, '0', 'E');		
//		//Inscricão Estadual
//		texto += Util.fill(inscricaoEstadual, 14, ' ', 'D');		
//		//Inscricão Municipal
//		texto += Util.fill(inscricaoMunicipal, 14, ' ', 'D');				
//		//Razão Social
//		texto += Util.fill(razaoSocial, 50, ' ', 'D');				
//		texto += "\n";		
		/*********************************** RELAÇÃO DAS MERCADORIAS EM ESTOQUE ******************************************************************/
		int quantidadeDeRegistros = 0;
		while(rsmProdutos.next()){				
			    String cdProdutoServico = (rsmProdutos.getString("id_produto_servico") != null && !rsmProdutos.getString("id_produto_servico").equals("") ? rsmProdutos.getString("id_produto_servico") :
			    						   rsmProdutos.getString("id_reduzido") != null && !rsmProdutos.getString("id_reduzido").equals("") ? rsmProdutos.getString("id_reduzido") : rsmProdutos.getString("nr_referencia"));
				String dsProdutoServico = rsmProdutos.getString("nm_produto_servico");
				String unidadeMedida 	= rsmProdutos.getString("sg_unidade_medida");
				String iat  			= (rsmProdutos.getInt("qt_precisao_custo") > 0) ? "A" : "T";
				String ippt 			= "T";				
				int cdTipoOperacao 		= Integer.parseInt(com.tivic.manager.grl.ParametroServices.getValorOfParametro("CD_TIPO_OPERACAO_VAREJO", cdEmpresa, null));						
				int cdTabelaPreco  		= com.tivic.manager.adm.TipoOperacaoServices.getCdTabelaPrecoOfOperacao(cdTipoOperacao);				
				ItemComparator i0  		= new ItemComparator("cd_tabela_preco", "" + cdTabelaPreco, ItemComparator.EQUAL, java.sql.Types.INTEGER);
				ItemComparator i1  		= new ItemComparator("cd_produto_servico", "" + rsmProdutos.getInt("cd_produto_servico"), ItemComparator.EQUAL, java.sql.Types.INTEGER);
				ArrayList<ItemComparator> criterios2 = new ArrayList<ItemComparator>();
				criterios2.add(i0);
				criterios2.add(i1);
				String vlValorUnitario         = "";
				String vlValorUnitarioAuxiliar = "";
				ResultSetMap rsmProdutoPreco   = com.tivic.manager.adm.ProdutoServicoPrecoDAO.find(criterios2);								
				if(rsmProdutoPreco.next()){
					vlValorUnitario = "" + rsmProdutoPreco.getFloat("vl_preco");
				}				
				for(int k = 0; k < vlValorUnitario.length(); k++){
					if(!vlValorUnitario.substring(k, k+1).equals(",") && !vlValorUnitario.substring(k, k+1).equals(".")){
						vlValorUnitarioAuxiliar += vlValorUnitario.substring(k, k+1);
					}
				}
				//
				String situacao_aliquota  = "";
				String aliquota 		  = "";			
				situacao_aliquota         = getAliquotaOfTributo(rsmProdutos.getInt("cd_produto_servico"), cdEmpresa);										
				//
				String situacaoTributaria = situacao_aliquota.substring(4);
				String st = "";							
				aliquota = situacao_aliquota.substring(0, 4);								
				//
				if(situacaoTributaria.equals("Isento")){
					st = "I";
				}else if(situacaoTributaria.equals("Tributada Integralmente")){
					st = "T";
				}else if(situacaoTributaria.equals("Substituição tributária")){
					st = "F";
				}else if(situacaoTributaria.equals("Não tributado")){
					st = "N";
				}else
					st = "S";
				//Tipo de Registro
				texto += "P2"; 						
				//CNPJ
				texto += Util.fill(cnpj, 14, '0', 'E');			
				//Codigo do produto
				texto += Util.fill(cdProdutoServico, 14, ' ', 'D');				
				//Descricão do produto
				texto += Util.fill(dsProdutoServico.trim(), 50, ' ', 'D');					
				//Unidade de Medida do Produto
				texto += Util.fill(unidadeMedida.trim(), 6, ' ', 'D');				
				//Indicador de Arrendondamento ou Truncamento
				texto += Util.fillAlpha(iat, 1);
				//Indicador de Produção Própria ou de Terceiros
				texto += Util.fillAlpha(ippt, 1);				
				//Situação Tributária
				texto += Util.fillAlpha(st, 1);				
				//Aliquota
				texto += Util.fill(aliquota, 4, '0', 'E');				
				//Valor Unitario
				texto += Util.fill(vlValorUnitarioAuxiliar, 12, '0', 'E');				
				texto += "\n";				
				quantidadeDeRegistros++;
		}
		
		/*********************************** Totalização do Arquivo ******************************************************************/
//		//Tipo de Registro
//		texto += "P9"; 		
//		//CNPJ
//		texto += Util.fill(cnpj, 14, '0', 'E');
//		//Inscricão Estadual
//		texto += Util.fill(inscricaoEstadual, 14, ' ', 'D');				
//		//Quantidade Total de Produtos
//		texto += Util.fillNum(quantidadeDeRegistros, 6);
		//
//		texto += "\n";
		/*********************************** Assinatura Digital ******************************************************************/
		//String assinaturaDigital = getEAD();
		
		//Tipo de Registro
		//texto += "EAD";
		
		//texto += Util.fill(assinaturaDigital, 256, ' ', 'D');
		
		//texto += "\n";
		
		//System.out.println(texto);
		
		/*********************************** Gera Arquivo ******************************************************************/
		try{
			if(!nmPath.equals("")){
				File file = new File(nmPath);
	            if (file.exists())
	            	file.delete();
	            FileOutputStream fos = new FileOutputStream(nmPath);
	            fos.write(texto.toString().getBytes());
	            fos.close();
	            return "Arquivo gerado em \"" + nmPath + "\"";
			}	         
			return texto;
	      }  
	      // em caso de erro apresenta mensagem abaixo  
	      catch(Exception e){
	    	  return "";
	      }  
		//return texto;
	}
	
	/**
	 * Registro E3
	 * @param cdEmpresa
	 * @return
	 */
	public static String gerarIdentificacaoEcf(int cdEmpresa, int cdReferencia){
		return gerarIdentificacaoEcf(cdEmpresa, cdReferencia, "");
	}
	
	public static String gerarIdentificacaoEcf(int cdEmpresa, int cdReferencia, String nmPath){
		Connection connect = null;
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			ItemComparator i = new ItemComparator("lgEstoque", "1", ItemComparator.EQUAL, java.sql.Types.INTEGER);
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(i);
			ResultSetMap rsmEcf = new ResultSetMap(connect.prepareStatement("SELECT A.*, B.* FROM bpm_referencia A " +
																			" JOIN bpm_marca B ON (A.cd_marca = B.cd_marca) " +
																			" WHERE A.cd_referencia = " + cdReferencia).executeQuery());
			String texto = "";				
			
			PreparedStatement pstmtUltimoDoc = connect.prepareStatement("SELECT * FROM alm_documento_saida " +
																		" WHERE cd_referencia_ecf = ? " +
																		"  AND st_documento_saida = "+DocumentoSaidaServices.ST_CONCLUIDO +
																		"  AND tp_documento_saida = " + DocumentoSaidaServices.TP_CUPOM_FISCAL +
																		" ORDER BY dt_documento_saida DESC " +
																		" LIMIT 1");
			
			while(rsmEcf.next()){				
			
				pstmtUltimoDoc.setInt(1, rsmEcf.getInt("cd_referencia"));
				ResultSetMap rsmUltimoDoc = new ResultSetMap(pstmtUltimoDoc.executeQuery());
				
				
				String nrFabricacao = rsmEcf.getString("nr_serie");
				String mfAdicional 	= "";
				String tpEcf 		= rsmEcf.getString("tp_referencia");
				String nmMarca 		= rsmEcf.getString("nm_marca");
				String nmModelo     = rsmEcf.getString("nm_modelo");
				String dtEstoque    = "";
				String hrEstoque    = "";
			
				if(rsmUltimoDoc.next()){
					dtEstoque = Util.formatDate(rsmUltimoDoc.getGregorianCalendar("dt_documento_saida"), "yyyyMMdd");
					hrEstoque = Util.formatDate(rsmUltimoDoc.getGregorianCalendar("dt_documento_saida"), "HHmmss");
				}
				
				//Tipo de Registro
				texto += "E3"; 						
				//Número de Fabricacao
				texto += Util.fill(nrFabricacao, 20, ' ', 'D');			
				//MF Adicional - Letra indicativa
				texto += Util.fill(mfAdicional, 1, ' ', 'D');				
				//Tipo de Ecf
				texto += Util.fill(tpEcf, 7, ' ', 'D');					
				//Marca do Ecf
				texto += Util.fill(nmMarca, 20, ' ', 'D');				
				//Modelo do Ecf
				texto += Util.fillAlpha(nmModelo, 20);
				//Data de Atualização do Estoque
				texto += Util.fillAlpha(dtEstoque, 8);				
				//Hora de Atualização do Estoque
				texto += Util.fillAlpha(hrEstoque, 6);
				texto += "\n";				
			}
			/*********************************** Gera Arquivo ******************************************************************/
			try{
				if(!nmPath.equals("")){
					File file = new File(nmPath);
		            if (file.exists())
		            	file.delete();
		            FileOutputStream fos = new FileOutputStream(nmPath);
		            fos.write(texto.toString().getBytes());
		            fos.close();
		           // return "Arquivo gerado em \"" + nmPath + "\"";
				}	         
				return texto;
		      }  
		      // em caso de erro apresenta mensagem abaixo  
		      catch(Exception e){
		    	  return "";
		      }  
			
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return "";
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
	 * Registro R01 a R07
	 * @param cdEmpresa
	 * @return
	 */
	public static String gerarRegistroR01aR07(int cdEmpresa, int cdReferencia, String nrMD5/*,
											  GregorianCalendar dtInicial, GregorianCalendar dtFinal*/){
		return gerarRegistroR01(cdEmpresa, cdReferencia, nrMD5, null, null, "");
	}
	
	public static String gerarRegistroR01(int cdEmpresa, int cdReferencia, String nrMD5, 
			      GregorianCalendar dtInicial, GregorianCalendar dtFinal, String nmPath){
		Connection connect = null;
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			ItemComparator i = new ItemComparator("lgEstoque", "1", ItemComparator.EQUAL, java.sql.Types.INTEGER);
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(i);
			ResultSetMap rsmEcf             = new ResultSetMap(connect.prepareStatement("SELECT A.*, B.* FROM bpm_referencia A " +
																					    " JOIN bpm_marca B ON (A.cd_marca = B.cd_marca) " +
																					    " WHERE A.cd_referencia = " + cdReferencia).executeQuery());
			String texto = "";
			//
			PreparedStatement pstmtUltimoDoc = connect.prepareStatement("SELECT * FROM alm_documento_saida " +
																		" WHERE cd_referencia_ecf = ? " +
																		"  AND st_documento_saida = "+DocumentoSaidaServices.ST_CONCLUIDO +
																		"  AND tp_documento_saida = " + DocumentoSaidaServices.TP_CUPOM_FISCAL +
																		" ORDER BY dt_documento_saida DESC " +
																		" LIMIT 1");
			//
			ResultSetMap rsmEmpresa       = new ResultSetMap(connect.prepareStatement("SELECT B.nr_cnpj, B.nr_inscricao_estadual FROM grl_pessoa A " +
																		   		      "LEFT OUTER JOIN grl_pessoa_juridica B ON (A.cd_pessoa = B.cd_pessoa) " +
																		   		      "WHERE A.cd_pessoa = " + cdEmpresa).executeQuery());
			//
			int cdDesenvolvedor           = ParametroServices.getValorOfParametroAsInteger("CD_DESENVOLVEDOR", 0, 0);
			ResultSetMap rsmDesenvolvedor = new ResultSetMap(connect.prepareStatement("SELECT B.nr_cnpj, B.nr_inscricao_estadual, B.nr_inscricao_municipal, B.nm_razao_social, " +
			   																		  " C.nm_comercial, C.nr_versao " +
			   																		  "FROM grl_pessoa A " +
			   																		  "LEFT OUTER JOIN grl_pessoa_juridica   B ON (A.cd_pessoa = B.cd_pessoa) " +
			   																		  "LEFT OUTER JOIN fsc_dados_homologacao C ON (A.cd_pessoa = C.cd_desenvolvedor) " +
			   																		  "WHERE A.cd_pessoa = " + cdDesenvolvedor).executeQuery());
			//
			while(rsmEcf.next()){				
			
				pstmtUltimoDoc.setInt(1, rsmEcf.getInt("cd_referencia"));
				ResultSetMap rsmUltimoDoc = new ResultSetMap(pstmtUltimoDoc.executeQuery());
				
				String nrFabricacao = rsmEcf.getString("nr_serie");		
				String mfAdicional 	= "";
				String tpEcf 		= rsmEcf.getString("tp_referencia");
				String nmMarca 		= rsmEcf.getString("nm_marca");
				String nmModelo     = rsmEcf.getString("nm_modelo");
				String versaoSB     = rsmEcf.getString("txt_versao");
				String dtEstoque    = "";
				String hrEstoque    = "";
				String nrSequencial = "";
				String nrCNPJEstab  = "";
				String iEEmpresa    = "";
				String nrCNPJDesenv = "";
				String iEDesenv     = "";
				String iMDesenv     = "";
				String nmRSDesenv   = "";
				String nmComercial  = "";
				String nrVersao     = "";
				//String dtInicialR   = SimpleDateFormaat(dtInicial, "yyyyMMdd");
				//String dtFinalR     = Util.formatDate(dtFinal, "yyyyMMdd");				
				//
				if(rsmUltimoDoc.next()){
					dtEstoque    = Util.formatDate(rsmUltimoDoc.getGregorianCalendar("dt_documento_saida"), "yyyyMMdd");
					hrEstoque    = Util.formatDate(rsmUltimoDoc.getGregorianCalendar("dt_documento_saida"), "HHmmss");
					nrSequencial = rsmUltimoDoc.getString("nr_documento_saida");
				}
				//
				if(rsmEmpresa.next()){
					nrCNPJEstab  = rsmEmpresa.getString("nr_cnpj");
					iEEmpresa    = rsmEcf.getString("nr_inscricao_estadual");
				}
				//
				while(rsmDesenvolvedor.next()){
					nrCNPJDesenv = rsmDesenvolvedor.getString("nr_cnpj");
					iEDesenv     = rsmDesenvolvedor.getString("nr_inscricao_estadual");
					iMDesenv     = rsmDesenvolvedor.getString("nr_inscricao_municipal");
					nmRSDesenv   = rsmDesenvolvedor.getString("nm_razao_social");
					nmComercial  = rsmDesenvolvedor.getString("nm_comercial");
					nrVersao     = rsmDesenvolvedor.getString("nr_versao");
				}
				/**
				 * R01
				 * REGISTRO TIPO R01 - IDENTIFICAÇÃO DO ECF, DO USUÁRIO, DO PAF-ECF E DA EMPRESA DESENVOLVEDORA
				 * */
				//Tipo de Registro
				texto += "R01"; 						
				//Número de Fabricacao
				texto += Util.fill(nrFabricacao, 20, ' ', 'D');			
				//MF Adicional - Letra indicativa
				texto += Util.fill(mfAdicional, 1, ' ', 'D');				
				//Tipo de Ecf
				texto += Util.fill(tpEcf, 7, ' ', 'D');					
				//Marca do Ecf
				texto += Util.fill(nmMarca, 20, ' ', 'D');				
				//Modelo do Ecf
				texto += Util.fill(nmModelo, 20, ' ', 'D');
				//Versão do SB
				texto += Util.fill(versaoSB, 10, ' ', 'D');
				//Data de Atualização do Estoque
				texto += Util.fillAlpha(dtEstoque, 8);				
				//Hora de Atualização do Estoque
				texto += Util.fillAlpha(hrEstoque, 6);
				//Número Sequencial do ECF
				texto += Util.fill(nrSequencial, 3, ' ', 'D');
				//CNPJ do usuário
				texto += Util.fill(nrCNPJEstab, 14, ' ', 'D');
				//Inscrição Estadual do usuário
				texto += Util.fill(iEEmpresa, 14, ' ', 'D');
				//CNPJ da desenvolvedora
				texto += Util.fill(nrCNPJDesenv, 14, ' ', 'D');
				//Inscrição Estadual da desenvolvedora
				texto += Util.fill(iEDesenv, 14, ' ', 'D');
				//Inscrição Municipal da desenvolvedora
				texto += Util.fill(iMDesenv, 14, ' ', 'D');
				//Denominação da empresa desenvolvedora
				texto += Util.fill(nmRSDesenv, 40, ' ', 'D');
				//Nome do PAF-ECF
				texto += Util.fill(nmComercial, 40, ' ', 'D');
				//Versão do PAF-ECF
				texto += Util.fill(nrVersao, 10, ' ', 'D');
				//Código MD-5 do PAF-ECF
				texto += Util.fill(nrMD5, 32, ' ', 'D');
				//Data Inicial
				//texto += Util.fillAlpha(dtInicialR, 8);
				//Data Final
				//texto += Util.fillAlpha(dtFinalR, 8);
				//Versão da ER-PAF-ECF
				texto += Util.fill(" 2.01", 4, ' ', 'D');
				
				//texto += "\n";				
			}

			         
				return texto;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return "";
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static String getAliquotaOfTributo(int cdProdutoServico, int cdEmpresa) {
		return getAliquotaOfTributo(cdProdutoServico, cdEmpresa, null);
	}
	public static String getAliquotaOfTributo(int cdProdutoServico, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
	
			// Descobrindo dados do endereço
			int cdEstado=0;
			ResultSet rs = connect.prepareStatement("SELECT cd_estado " +
													"FROM grl_estado " +
													"WHERE NM_ESTADO = 'BAHIA'").executeQuery();
			if(rs.next())	{
				cdEstado = rs.getInt("cd_estado");
			}
			
			/*
			 *  Calcula e soma alíquotas
			 */
			/*
			 *  Pesquisa classificacao fiscal do produto/serviço
			 */
			rs = connect.prepareStatement("SELECT * FROM grl_produto_servico " +
					                 "WHERE cd_produto_servico = " +cdProdutoServico).executeQuery();
			int cdClassificacaoFiscal = 0;
			if(rs.next())
				cdClassificacaoFiscal = rs.getInt("cd_classificacao_fiscal");
			/*
			 *  Calcula base de cálculo
			 */
			int cdNaturezaOperacao = Integer.parseInt(com.tivic.manager.grl.ParametroServices.getValorOfParametro("CD_NATUREZA_OPERACAO_VAREJO", cdEmpresa, null));
			int cdTributo = Integer.parseInt(com.tivic.manager.grl.ParametroServices.getValorOfParametro("CD_TRIBUTO_ICMS"));
			ResultSetMap rsm = TributoServices.calcTributos(TributoAliquotaServices.OP_VENDA, cdNaturezaOperacao,
					                                        cdClassificacaoFiscal, cdProdutoServico, 0, cdEstado, 0,
					                                        0, connect);
			float prAliquota = 0;
			int stTributaria = TributoAliquotaServices.ST_IGNORADA;
			if(rsm.locate("cd_tributo", cdTributo))	{
				stTributaria = rsm.getInt("st_tributaria");
				prAliquota   = rsm.getFloat("pr_aliquota");
			}
			return Util.fillNum(Math.round(prAliquota * 100), 4)+TributoAliquotaServices.situacaoTributaria[stTributaria];
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return "";
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result transfereProduto(int cdEmpresaPadrao, int cdEmpresaCliente){
		return transfereProduto(cdEmpresaPadrao, cdEmpresaCliente, null);
	}
	public static Result transfereProduto(int cdEmpresaPadrao, int cdEmpresaCliente, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {			
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());			
			if ((cdEmpresaPadrao > 0) && (cdEmpresaCliente > 0)) {
				PreparedStatement pstmt = connection.prepareStatement("INSERT INTO grl_produto_servico_empresa (cd_empresa, "+ 
																					"cd_produto_servico, cd_unidade_medida, "+
																					"vl_preco_medio,vl_custo_medio, " +
																					"vl_ultimo_custo, qt_ideal, "+
																					"qt_minima, qt_maxima, "+
																					"qt_dias_estoque, qt_precisao_custo, "+
																					"qt_precisao_unidade, qt_dias_garantia, "+
																					"tp_reabastecimento, tp_controle_estoque, "+
																					"tp_transporte,  st_produto_empresa, "+
																					"lg_estoque_negativo, id_reduzido) "	+																				
																  "  SELECT " +cdEmpresaCliente+ ", cd_produto_servico, "+
																  		     "cd_unidade_medida, vl_preco_medio, vl_custo_medio, "+
																  		     "vl_ultimo_custo, qt_ideal, qt_minima, qt_maxima, "+
																  		     "qt_dias_estoque, qt_precisao_custo, qt_precisao_unidade, "+
																  		     "qt_dias_garantia, tp_reabastecimento, tp_controle_estoque, "+
																  		     "tp_transporte, st_produto_empresa, lg_estoque_negativo, id_reduzido "+ 
												                  "  FROM grl_produto_servico_empresa A1 "+
												                  " WHERE cd_empresa = " + cdEmpresaPadrao +				  
												                  "   AND NOT EXISTS (SELECT * FROM grl_produto_servico_empresa X "+
												                  "                   WHERE A1.cd_produto_servico = X.cd_produto_servico "+
												                  "						AND X.cd_empresa = "+cdEmpresaCliente + ")");
								
				pstmt.execute();			
			}
			
			if (isConnectionNull)
				connection.commit();
			
			return new Result(1, "Produtos enviados com sucesso!");
		
		}catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro ao tentar transferir produtos: " +  e);
			return new Result(-1, "Erro ao tentar trasferir produtos");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}	
	}
	
	public static Result transfereNota(int cdEmpresa, String nrDocumentoSaida){
		return transfereNota(cdEmpresa, nrDocumentoSaida, null);
	}
	
	public static Result transfereNota(int cdEmpresa, String nrDocumentoSaida, Connection connection) {
		boolean isConnectionNull = connection==null;
		int cdDocumentoSaida = 0;
		try {			
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());			
			ResultSet rs = connection.prepareStatement("SELECT cd_documento_saida FROM alm_documento_saida " +
													   "WHERE nr_documento_saida = '" + nrDocumentoSaida+"'").executeQuery();
			if(rs.next())	{
				cdDocumentoSaida = rs.getInt("cd_documento_saida");
			}		
			PreparedStatement pstmt1 = connection.prepareStatement("INSERT INTO alm_documento_saida_item "+
																              "(cd_documento_saida, cd_produto_servico, "+
																              " cd_empresa, qt_saida, vl_unitario, vl_acrescimo, "+
																              " vl_desconto, cd_unidade_medida, cd_tabela_preco, cd_item) "+

																 "SELECT cd_documento_saida, cd_produto_servico, "+cdEmpresa+
																 		", qt_saida, vl_unitario, vl_acrescimo, vl_desconto"+
																 		", cd_unidade_medida, cd_tabela_preco, cd_item "+ 
																 "FROM alm_documento_saida_item "+ 
																 "WHERE cd_documento_saida = " + cdDocumentoSaida);								
			pstmt1.execute();
			//
			PreparedStatement pstmt2 = connection.prepareStatement("UPDATE alm_saida_local_item SET cd_empresa = "+cdEmpresa+
														           "WHERE cd_documento_saida = " + cdDocumentoSaida);
			pstmt2.execute();	
			//
			PreparedStatement pstmt3 = connection.prepareStatement("UPDATE adm_saida_item_aliquota  SET cd_empresa = "+cdEmpresa+
														           "WHERE cd_documento_saida = " + cdDocumentoSaida);
			pstmt3.execute();	
			//
			PreparedStatement pstmt4 = connection.prepareStatement("DELETE FROM alm_documento_saida_item "+
																   "WHERE cd_documento_saida = "+cdDocumentoSaida+
														           "  AND cd_empresa  <> " + cdEmpresa);
			pstmt4.execute();	
			//
			PreparedStatement pstmt5 = connection.prepareStatement("UPDATE alm_documento_saida SET cd_empresa = "+cdEmpresa+
														           "WHERE cd_documento_saida = " +cdDocumentoSaida);
			pstmt5.execute();	
			//
			if (isConnectionNull)
				connection.commit();			
			return new Result(1, "Nota Transferida com sucesso!");
		
		}catch(Exception e){			
			e.printStackTrace(System.out);
			System.err.println("Erro ao tentar transferir Nota: " +  e);
			return new Result(-1, "Erro ao tentar trasferir Nota");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}	
	}
	
	public static byte[] getBytes(int cdEmpresa, int cdProdutoServico) {
		return getBytes(cdEmpresa, cdProdutoServico, null);
	}

	public static byte[] getBytes(int cdEmpresa, int cdProdutoServico, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			ProdutoServicoEmpresa produtoEmpresa = ProdutoServicoEmpresaDAO.get(cdEmpresa, cdProdutoServico, connection);
			return produtoEmpresa==null ? null : produtoEmpresa.getImgProduto();
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
	public static void main(String args[]){
		gerarRelatorioProdutosTributados(3201);
	}
}
