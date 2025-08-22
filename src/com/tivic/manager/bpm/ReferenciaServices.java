package com.tivic.manager.bpm;

import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.manager.alm.DocumentoSaida;
import com.tivic.manager.alm.DocumentoSaidaDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.crt.ProdutoServices;
import com.tivic.manager.grl.Produto;
import com.tivic.manager.grl.ProdutoDAO;
import com.tivic.manager.grl.Setor;
import com.tivic.manager.grl.SetorDAO;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class ReferenciaServices {

	public static final int ST_BAIXADO = 0;
	public static final int ST_ATIVO = 1;
	public static final int ST_TRANSFERENCIA = 2;
	public static final int ST_NAO_LOCALIZADO = 3;
	public static final int ST_DOADO = 4;

	public static final String[] situacaoReferencia = {"Baixado","Ativo","Transfência externa","Não localizado","Doado"};

	/**
	 * @autor Hugo Azevedo
	 * @category PDV
	 *
	 **/
	public static int findAndInsertEcf(String nmMarca, String nmModelo, String nmBem, String nrSerie, int cdEmpresa, String tpReferencia, String txtVersao,boolean allowInsert)	{
		return findAndInsertEcf(nmMarca,nmModelo,nmBem,nrSerie,cdEmpresa,tpReferencia, txtVersao,allowInsert, null);
	}
	public static int findAndInsertEcf(String nmMarca, String nmModelo, String nmBem, String nrSerie, int cdEmpresa, boolean allowInsert)	{
		return findAndInsertEcf(nmMarca,nmModelo,nmBem,nrSerie,cdEmpresa, null, null, allowInsert, null);
	}

	public static int findAndInsertEcf(String nmMarca, String nmModelo, String nmBem, String nrSerie,
											  int cdEmpresa, String tpReferencia, String txtVersao, boolean allowInsert, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			//connect.setAutoCommit(false);
			// Pesquisa / Verifica MARCA
			int cdMarca;
			ResultSet rs = connect.prepareStatement("SELECT * FROM bpm_marca " +
                    								"WHERE nm_marca = \'"+nmMarca+"\'").executeQuery();
			if(!rs.next())
				cdMarca = MarcaDAO.insert(new Marca(0, nmMarca, ""), connect);
			else
				cdMarca = rs.getInt("cd_marca");
			// Pesquisa / Verifica PRODUTO:BEM
			int cdProdutoServico = 0;
			rs = connect.prepareStatement("SELECT * FROM grl_produto_servico " +
                    					  "WHERE nm_produto_servico = \'"+nmBem+"\'").executeQuery();
			if(!rs.next())
				cdProdutoServico = BemDAO.insert(new Bem(0, 0 /*cdCategoria*/, nmBem, "" /*txtProdutoServico*/, "" /*txtEspecificacao*/, ""/*txtDadoTecnico*/,
														 "" /*txtPrazoEntrega*/, 0 /*tpProdutoServico*/, "" /*idProdutoServico*/, "ECF" /*sgProdutoServico*/,
														 0 /*cdClassificacaoFiscal*/, 0 /*cdFabricante*/, 0 /*cdMarca*/, "" /*nmModelo*/, 0/*cdNcm*/, ""/*nrRefencia*/, 0/*cdClassificacao*/,
														 0 /*prDepreciacao*/), connect);
			else{
				cdProdutoServico = rs.getInt("cd_produto_servico");
			}
			//com.tivic.manager.util.Util.registerLog(new Exception("nrSerie : "+nrSerie));
			rs = connect.prepareStatement("SELECT cd_referencia FROM bpm_referencia " +
					  					  "WHERE cd_bem   = "+cdProdutoServico+
					  					  "  AND nr_serie = \'"+nrSerie+"\'").executeQuery();

			int cdReferencia = 0;
			if(!rs.next())	{
				if(cdProdutoServico>0 && allowInsert)
					cdReferencia = ReferenciaDAO.insert(new Referencia(0,cdProdutoServico, 0 /*cdSetor*/, 0 /*cdDocumentoEntrada*/, cdEmpresa, cdMarca,
																	   null /*dtAquisicao*/, null /*dtGarantia*/, null /*dtValidade*/, null /*dtBaixa*/,
																	   nrSerie, null /*nrTombo*/, 1 /*stReferencia*/, nmModelo, new GregorianCalendar() /*dtIncorporacao*/,
																	   0 /*qtCapacidade*/, 0 /*lgProducao*/, "" /*idReferencia*/, 0/*cdLocalArmazenamento*/, tpReferencia, txtVersao), connect);
			}
			else
				cdReferencia = rs.getInt("cd_referencia");
			//connect.commit();
			return cdReferencia;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	public static int registraReducaoZ(String nrSerie, String nmModelo, String nrUsuario, String nrCRZ, String nrCOO, String nrCRO,
			   				           String dtMovimento, String dtEmissao, String hrEmissao, String vendaBrutaDiaria, String prDescontoISSQN)	{
		return registraReducaoZ(nrSerie,nmModelo,nrUsuario,nrCRZ, nrCOO, nrCRO, 
				                dtMovimento,dtEmissao,hrEmissao,vendaBrutaDiaria,prDescontoISSQN, null);
	}

	public static int registraReducaoZ(String nrSerie, String nmModelo, String nrUsuario, String nrCRZ, String nrCOO, String nrCRO,
									   String dtMovimento, String dtEmissao, String hrEmissao, String vendaBrutaDiaria, String prDescontoISSQN, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			//Pesquisa impressora
			int cdImpressora;
			ResultSet rs = connect.prepareStatement("SELECT * FROM bpm_referencia " +
                    								"WHERE nr_serie = \'"+nrSerie+"\'").executeQuery();
			if(rs.next())
				cdImpressora = rs.getInt("cd_referencia");
			
	   //connect.commit();
			return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
		
	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, false, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, boolean notIncluirBaixas) {
		return findCompleto(criterios, notIncluirBaixas, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, boolean notIncluirBaixas, Connection connect) {
		return Search.find("SELECT A.*, B.cd_classificacao, B.pr_depreciacao, C.nm_produto_servico, C.id_produto_servico, " +
				"C.txt_produto_servico, D.nm_marca, D.id_marca, E.nm_setor, E.id_setor, " +
				"(SELECT MAX(H.nm_setor) FROM bpm_referencia_movimentacao F, grl_setor H " +
				" WHERE F.cd_referencia = A.cd_referencia " +
				"   AND F.cd_setor = H.cd_setor " +
				"   AND F.dt_movimentacao = (SELECT MAX(G.dt_movimentacao) FROM bpm_referencia_movimentacao G " +
				"							 WHERE G.cd_referencia = A.cd_referencia)) AS nm_setor_atual " +
				"FROM bpm_referencia A " +
				"JOIN bpm_bem B ON (A.cd_bem = B.cd_bem) " +
				"JOIN grl_produto_servico C ON (B.cd_bem = C.cd_produto_servico) " +
				"LEFT OUTER JOIN bpm_marca D ON (A.cd_marca = D.cd_marca) " +
				"LEFT OUTER JOIN grl_setor E ON (A.cd_setor = E.cd_setor) " +
				"WHERE 1 = 1 " +
				(notIncluirBaixas ? (" AND A.st_referencia <> " + ReferenciaServices.ST_BAIXADO + " ") : ""), criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static Setor getLocalizacaoAtual(int cdReferencia) {
		return getLocalizacaoAtual(cdReferencia, null);
	}

	public static Setor getLocalizacaoAtual(int cdReferencia, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			PreparedStatement pstmt = connection.prepareStatement("SELECT MAX(dt_movimentacao) " +
					"FROM bpm_referencia_movimentacao " +
					"WHERE cd_referencia = ?");
			pstmt.setInt(1, cdReferencia);
			ResultSet rs = pstmt.executeQuery();

			int cdSetorAtual = 0;
			if (rs.next() && rs.getTimestamp(1) != null) {
				pstmt = connection.prepareStatement("SELECT cd_setor FROM bpm_referencia_movimentacao " +
						"WHERE cd_referencia = ? " +
						"AND dt_movimentacao = ?");
				pstmt.setInt(1, cdReferencia);
				pstmt.setTimestamp(2, rs.getTimestamp(1));
				rs = pstmt.executeQuery();
				cdSetorAtual = rs.next() ? rs.getInt(1) : 0;
			}

			return cdSetorAtual <= 0 ? null : SetorDAO.get(cdSetorAtual, connection);
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

	public static int update(Referencia objeto, int cdSetorAtual) {
		return update(objeto, cdSetorAtual, null);
	}

	public static int update(Referencia objeto, int cdSetorAtual, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			if (cdSetorAtual > 0) {
				PreparedStatement pstmt = connection.prepareStatement("SELECT MAX(dt_movimentacao) " +
																	  "FROM bpm_referencia_movimentacao " +
																	  "WHERE cd_referencia = "+objeto.getCdReferencia());
				ResultSet rs = pstmt.executeQuery();

				int cdSetorAnterior = 0;
				if (rs.next() && rs.getTimestamp(1) != null) {
					pstmt = connection.prepareStatement("SELECT cd_setor FROM bpm_referencia_movimentacao " +
							                            "WHERE cd_referencia = ? " +
							                            "  AND dt_movimentacao = ?");
					pstmt.setInt(1, objeto.getCdReferencia());
					pstmt.setTimestamp(2, rs.getTimestamp(1));
					rs = pstmt.executeQuery();
					cdSetorAnterior = rs.next() ? rs.getInt(1) : 0;
				}

				if (cdSetorAnterior != cdSetorAtual) {
					ReferenciaMovimentacao movimentacao = new ReferenciaMovimentacao(objeto.getCdReferencia(), 0, cdSetorAtual, new GregorianCalendar());
					if (ReferenciaMovimentacaoDAO.insert(movimentacao, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
			}

			if (ReferenciaDAO.update(objeto, connection) <= 0) {
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

	public static int insert(Referencia objeto, int cdSetorAtual) {
		return insert(objeto, cdSetorAtual, null);
	}

	public static int insert(Referencia objeto, int cdSetorAtual, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int cdReferencia = 0;
			
			if ((cdReferencia = ReferenciaDAO.insert(objeto, connection)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (cdSetorAtual > 0) {
				ReferenciaMovimentacao movimentacao = new ReferenciaMovimentacao(cdReferencia, 0, cdSetorAtual, new GregorianCalendar());
				if (ReferenciaMovimentacaoDAO.insert(movimentacao, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return cdReferencia;
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

	public static ResultSetMap getReferenciasSetor(int cdSetor) {
		return getReferenciasSetor(cdSetor, null);
	}

	public static ResultSetMap getReferenciasSetor(int cdSetor, Connection connection) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_setor", Integer.toString(cdSetor), ItemComparator.EQUAL, Types.INTEGER));
		return Search.find("SELECT A.*, B.cd_classificacao, B.pr_depreciacao, C.nm_produto_servico, C.id_produto_servico, " +
				"C.txt_produto_servico, D.nm_marca, D.id_marca, E.nm_setor, E.id_setor " +
				"FROM bpm_referencia A " +
				"JOIN bpm_bem B ON (A.cd_bem = B.cd_bem) " +
				"JOIN grl_produto_servico C ON (B.cd_bem = C.cd_produto_servico) " +
				"LEFT OUTER JOIN bpm_marca D ON (A.cd_marca = D.cd_marca) " +
				"LEFT OUTER JOIN grl_setor E ON (A.cd_setor = E.cd_setor)", criterios, true,
				connection!=null ? connection : Conexao.conectar(), connection==null);
	}

	public static ResultSetMap getReferenciaDocSaida(int cdDocumentoSaida) {
		return getReferenciaDocSaida(cdDocumentoSaida, null);
	}

	public static ResultSetMap getReferenciaDocSaida(int cdDocumentoSaida, Connection connection) {
		DocumentoSaida docSaida = DocumentoSaidaDAO.get(cdDocumentoSaida);
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_referencia", Integer.toString(docSaida.getCdReferenciaEcf()), ItemComparator.EQUAL, Types.INTEGER));
		return Search.find("SELECT A.*, B.cd_classificacao, B.pr_depreciacao, C.nm_produto_servico, C.id_produto_servico, " +
				"C.txt_produto_servico, D.nm_marca, D.id_marca, E.nm_setor, E.id_setor " +
				"FROM bpm_referencia A " +
				"JOIN bpm_bem B ON (A.cd_produto_servico = B.cd_produto_servico) " +
				"JOIN grl_produto_servico C ON (B.cd_produto_servico = C.cd_produto_servico) " +
				"LEFT OUTER JOIN bpm_marca D ON (A.cd_marca = D.cd_marca) " +
				"LEFT OUTER JOIN grl_setor E ON (A.cd_setor = E.cd_setor)", criterios, true,
				connection!=null ? connection : Conexao.conectar(), connection==null);
	}
	
	
	public static ResultSetMap getMovimentacoesReferencia(int cdReferencia) {
		return getMovimentacoesReferencia(cdReferencia, null);
	}

	public static ResultSetMap getMovimentacoesReferencia(int cdReferencia, Connection connection) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_referencia", Integer.toString(cdReferencia), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsm = Search.find("SELECT A.*, B.* " +
				"FROM bpm_referencia_movimentacao A, grl_setor B " +
				"WHERE A.cd_setor = B.cd_setor", criterios, true,
				connection!=null ? connection : Conexao.conectar(), connection==null);
		ArrayList<String> criteriosOrder = new ArrayList<String>();
		criteriosOrder.add("dt_movimentacao DESC");
		rsm.orderBy(criteriosOrder);
		return rsm;
	}

	public static int transferirReferencias(int[] cdsReferencias, int cdSetorDestino) {
		return transferirReferencias(cdsReferencias, cdSetorDestino, null);
	}

	public static int transferirReferencias(int[] cdsReferencias, int cdSetorDestino, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			for (int i=0; cdsReferencias!=null && i<cdsReferencias.length; i++){
				int cdReferencia = cdsReferencias[i];
				Setor setorAtual = getLocalizacaoAtual(cdReferencia, connection);
				if (setorAtual==null || setorAtual.getCdSetor()!=cdSetorDestino) {
					ReferenciaMovimentacao movimentacao = new ReferenciaMovimentacao(cdReferencia, 0, cdSetorDestino, new GregorianCalendar());
					if (ReferenciaMovimentacaoDAO.insert(movimentacao, connection) <= 0) {
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

	public static int baixarReferencias(int[] cdsReferencias, GregorianCalendar dtBaixa) {
		return baixarReferencias(cdsReferencias, dtBaixa, null);
	}

	public static int baixarReferencias(int[] cdsReferencias, GregorianCalendar dtBaixa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			for (int i=0; cdsReferencias!=null && i<cdsReferencias.length; i++){
				int cdReferencia = cdsReferencias[i];
				Referencia referencia = ReferenciaDAO.get(cdReferencia, connection);
				referencia.setStReferencia(ST_BAIXADO);
				referencia.setDtBaixa(dtBaixa);
				if (ReferenciaDAO.update(referencia, connection) <= 0) {
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
	public static ResultSetMap getAllEcf(){
		return getAllEcf(null);
	}
	
	public static ResultSetMap getAllEcf(Connection connection) {		
		try {		
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("st_referencia", Integer.toString(ST_ATIVO), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = Search.find("SELECT * FROM bpm_referencia ", criterios, true,
					connection!=null ? connection : Conexao.conectar(), connection==null);
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.printInFile("C:/log.log", e.toString());
			System.err.println("Erro! ReferenciaServices.getAllEcf: " + e);
			return null;
		}
	}
}