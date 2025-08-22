package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.alm.DocumentoSaida;
import com.tivic.manager.alm.DocumentoSaidaDAO;
import com.tivic.manager.alm.DocumentoSaidaItem;
import com.tivic.manager.alm.DocumentoSaidaItemServices;
import com.tivic.manager.alm.DocumentoSaidaServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.NumeracaoDocumentoServices;


import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class SolicitacaoMaterialServices {

	public static final String[] situacoes = {"Em aberto",
	    "Confirmada",
	    "Em atendimento",
		"Atendimento concluído",
	    "Cancelada"};

	public static final int ST_EM_ABERTO = 0;
	public static final int ST_CONFIRMADA = 1;
	public static final int ST_EM_ATENDIMENTO = 2;
	public static final int ST_ATENDIDA = 3;
	public static final int ST_CANCELADA = 4;

	public static String getProximoNrSolicitacao(int cdEmpresa) {
		return getProximoNrSolicitacao(cdEmpresa, null);
	}

	public static String getProximoNrSolicitacao(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrSolicitacao = 0;
			if ((nrSolicitacao = NumeracaoDocumentoServices.getProximoNumero("SOLICITACAO_MATERIAL", nrAno, cdEmpresa, connection)) <= 0)
				return null;
			return new DecimalFormat("000000").format(nrSolicitacao) + "/" + nrAno;
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

	public static ResultSetMap getAsResultSet(int cdSolicitacaoMaterial) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_solicitacao_material", Integer.toString(cdSolicitacaoMaterial), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios);
	}

	public static int cancelarSolicitacao(int cdSolicitacaoMaterial) {
		return cancelarSolicitacao(cdSolicitacaoMaterial, null);
	}

	public static int cancelarSolicitacao(int cdSolicitacaoMaterial, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			SolicitacaoMaterial solicitacao = SolicitacaoMaterialDAO.get(cdSolicitacaoMaterial, connection);
			if (solicitacao==null || solicitacao.getStSolicitacaoMaterial()!=ST_EM_ABERTO)
				return -1;
			else {
				solicitacao.setStSolicitacaoMaterial(ST_CANCELADA);
				return SolicitacaoMaterialDAO.update(solicitacao, connection)>0 ? 1 : -1;
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

	public static int concluirAtendSolicitacao(int cdSolicitacaoMaterial) {
		return concluirAtendSolicitacao(cdSolicitacaoMaterial, null);
	}

	public static int concluirAtendSolicitacao(int cdSolicitacaoMaterial, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			SolicitacaoMaterial solicitacao = SolicitacaoMaterialDAO.get(cdSolicitacaoMaterial, connection);
			if (solicitacao==null || solicitacao.getStSolicitacaoMaterial()!=ST_EM_ATENDIMENTO) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_documento_saida " +
					"FROM alm_documento_saida " +
					"WHERE cd_solicitacao_material = ? " +
					"  AND st_documento_saida = ?");
			pstmt.setInt(1, cdSolicitacaoMaterial);
			pstmt.setInt(2, DocumentoSaidaServices.ST_EM_CONFERENCIA);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (DocumentoSaidaServices.liberarSaida(rs.getInt("cd_documento_saida"), 0 /*cdLocal*/, connection).getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			solicitacao.setStSolicitacaoMaterial(ST_ATENDIDA);
			if (SolicitacaoMaterialDAO.update(solicitacao, connection) <= 0) {
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
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int confirmarSolicitacao(int cdSolicitacaoMaterial) {
		return confirmarSolicitacao(cdSolicitacaoMaterial, null);
	}

	public static int confirmarSolicitacao(int cdSolicitacaoMaterial, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			SolicitacaoMaterial solicitacao = SolicitacaoMaterialDAO.get(cdSolicitacaoMaterial, connection);
			if (solicitacao==null || solicitacao.getStSolicitacaoMaterial()!=ST_EM_ABERTO)
				return -1;
			else {
				solicitacao.setStSolicitacaoMaterial(ST_CONFIRMADA);
				return SolicitacaoMaterialDAO.update(solicitacao, connection)>0 ? 1 : -1;
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

	public static int iniciarAtendimentoSolicitacao(int cdSolicitacaoMaterial) {
		return iniciarAtendimentoSolicitacao(cdSolicitacaoMaterial, null);
	}

	public static int iniciarAtendimentoSolicitacao(int cdSolicitacaoMaterial, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			SolicitacaoMaterial solicitacao = SolicitacaoMaterialDAO.get(cdSolicitacaoMaterial, connection);
			if (solicitacao==null || solicitacao.getStSolicitacaoMaterial()!=ST_CONFIRMADA)
				return -1;
			else {
				solicitacao.setStSolicitacaoMaterial(ST_EM_ATENDIMENTO);
				return SolicitacaoMaterialDAO.update(solicitacao, connection)>0 ? 1 : -1;
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

	public static int delete(int cdSolicitacaoMaterial) {
		return delete(cdSolicitacaoMaterial, null);
	}

	public static int delete(int cdSolicitacaoMaterial, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("DELETE " +
					"FROM adm_solicitacao_material_item " +
					"WHERE cd_solicitacao_material = ?");
			pstmt.setInt(1, cdSolicitacaoMaterial);
			pstmt.execute();

			if (SolicitacaoMaterialDAO.delete(cdSolicitacaoMaterial, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		try {
			int cdProdutoServico = 0;
			for (int i=0; criterios!=null && i<criterios.size(); i++)
				if (criterios.get(i).getColumn().equalsIgnoreCase("cd_produto_servico")) {
					cdProdutoServico = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					break;
				}
			return Search.find("SELECT A.*, B.nm_setor AS nm_setor_solicitante, C.nm_pessoa AS nm_solicitante, " +
					"F.cd_documento_saida, F.st_documento_saida, F.nr_documento_saida, F.dt_documento_saida, " +
					"(SELECT COUNT(*) " +
					" FROM adm_solicitacao_material_item D " +
					" WHERE D.cd_solicitacao_material = A.cd_solicitacao_material) AS qt_itens " +
					"FROM adm_solicitacao_material A " +
					"LEFT OUTER JOIN grl_setor B ON (A.cd_setor_solicitante = B.cd_setor) " +
					"LEFT OUTER JOIN grl_pessoa C ON (A.cd_solicitante = C.cd_pessoa) " +
					"LEFT OUTER JOIN alm_documento_saida F ON (A.cd_solicitacao_material = F.cd_solicitacao_material AND" +
					"										   F.cd_documento_saida = (SELECT MAX(G.cd_documento_saida) " +
					"																		FROM alm_documento_saida G " +
					"																		WHERE G.cd_solicitacao_material = A.cd_solicitacao_material))" +
					"WHERE 1=1 " +
					(cdProdutoServico>0 ? "  AND " + cdProdutoServico + " IN (SELECT E.cd_produto_servico " +
					"			 FROM adm_solicitacao_material_item E " +
					"			 WHERE E.cd_solicitacao_material = A.cd_solicitacao_material)" : ""), criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public static ResultSetMap getAllItens(int cdDocumentoSaida) {
		return getAllItens(cdDocumentoSaida, null);
	}

	public static ResultSetMap getAllItens(int cdSolicitacaoMaterial, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			SolicitacaoMaterial solicitacao = SolicitacaoMaterialDAO.get(cdSolicitacaoMaterial, connection);
			PreparedStatement pstmt = connection.prepareStatement(
					 "SELECT A.*, B.nm_produto_servico, B.id_produto_servico, C.nm_unidade_medida, C.sg_unidade_medida, " +
					 "		 E.id_reduzido, " +
					 "		 (SELECT SUM(D.qt_saida + D.qt_saida_consignada) " +
					 "		  FROM alm_saida_local_item D, alm_documento_saida F " +
					 "		  WHERE D.cd_produto_servico = A.cd_produto_servico AND " +
					 "				D.cd_documento_saida = F.cd_documento_saida AND " +
					 "				F.cd_solicitacao_material = A.cd_solicitacao_material) AS qt_atendida " +
					 "FROM adm_solicitacao_material_item A " +
					 "JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) " +
					 "LEFT OUTER JOIN grl_unidade_medida C ON (A.cd_unidade_medida = C.cd_unidade_medida) " +
					 "LEFT OUTER JOIN grl_produto_servico_empresa E ON (B.cd_produto_servico = E.cd_produto_servico AND " +
					 "													E.cd_empresa = ?) " +
					 "WHERE A.cd_solicitacao_material = ?");
			pstmt.setInt(1, solicitacao.getCdEmpresa());
			pstmt.setInt(2, cdSolicitacaoMaterial);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialServices.getAllItens: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int setItensAtendidos(int cdSolicitacaoMaterial, ArrayList<HashMap<String, Object>> itens, boolean finalizeAtendimento) {
		return setItensAtendidos(cdSolicitacaoMaterial, itens, finalizeAtendimento, null);
	}

	public static int setItensAtendidos(int cdSolicitacaoMaterial, ArrayList<HashMap<String, Object>> itens, boolean finalizeAtendimento,
			Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			SolicitacaoMaterial solicitacao = SolicitacaoMaterialDAO.get(cdSolicitacaoMaterial, connection);
			if (!(solicitacao.getStSolicitacaoMaterial()==ST_CONFIRMADA || solicitacao.getStSolicitacaoMaterial()==ST_EM_ATENDIMENTO)) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_documento_saida, st_documento_saida " +
					"FROM alm_documento_saida " +
					"WHERE cd_solicitacao_material = ? " +
					"  AND st_documento_saida <> ?");
			pstmt.setInt(1, cdSolicitacaoMaterial);
			pstmt.setInt(2, DocumentoSaidaServices.ST_CANCELADO);
			ResultSet rs = pstmt.executeQuery();
			int cdDocumentoSaida = rs.next() ? rs.getInt("cd_documento_saida") : 0;

			if (cdDocumentoSaida > 0 && rs.getInt("st_documento_saida") == DocumentoSaidaServices.ST_CONCLUIDO) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (cdDocumentoSaida <= 0) {
				if ((cdDocumentoSaida = DocumentoSaidaDAO.insert(new DocumentoSaida(0 /*cdDocumentoSaida*/,
						0 /*cdTransportadora*/,
						solicitacao.getCdEmpresa(),
						0 /*cdCliente*/,
						new GregorianCalendar() /*dtDocumentoSaida*/,
						DocumentoSaidaServices.ST_EM_CONFERENCIA /*stDocumentoSaida*/,
						DocumentoSaidaServices.getProximoNrDocumento(solicitacao.getCdEmpresa(), connection) /*nrDocumentoSaida*/,
						DocumentoSaidaServices.TP_NOTA_REMESSA /*tpDocumentoSaida*/,
						DocumentoSaidaServices.SAI_DOACAO /*tpSaida*/,
						"" /*nrConhecimento*/,
						0 /*vlDesconto*/,
						0 /*vlAcrescimo*/,
						new GregorianCalendar() /*dtEmissao*/,
						DocumentoSaidaServices.FRT_CIF /*tpFrete*/,
						"" /*txtMensagem*/,
						"Referente à Solicitação de Materiais " + solicitacao.getIdSolicitacaoMaterial() /*txtObservacao*/,
						"" /*nrPlacaVeiculo*/,
						"" /*sgPlacaVeiculo*/,
						"" /*nrVolumes*/,
						null /*dtSaidaTranportadora*/,
						"" /*dsViaTransporte*/,
						0 /*cdNaturezaOperacao*/,
						"" /*txtNotaFiscal*/,
						0 /*vlPesoLiquido*/,
						0 /*vlPesoBruto*/,
						"" /*dsEspecieVolumes*/,
						"" /*dsMarcaVolumes*/,
						0 /*qtVolumes*/,
						DocumentoSaidaServices.MOV_ESTOQUE_NAO_CONSIGNADO /*tpMovimento*/,
						0 /*cdVendedor*/,
						0 /*cdMoeda*/,
						0 /*cdReferenciaEfc*/,
						solicitacao.getCdSolicitacaoMaterial(),
						0 /*cdTipoOperacao*/,
						0 /*vlTotalDocumento*/,
						0 /*cdContrato*/,
						0 /*vlFrete*/,
						0 /*vlSeguro*/,
						0 /*cdDigitador*/,
						0 /*cdDocumento*/,
						0 /*cdConta*/,
						0/*cdTurno*/,0/*vlTotalItens*/,1 /*nrSerie*/), connection)) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			/* remove itens incluidos anteriormente */
			pstmt = connection.prepareStatement("DELETE " +
					"FROM alm_saida_local_item " +
					"WHERE cd_documento_saida = ?");
			pstmt.setInt(1, cdDocumentoSaida);
			pstmt.execute();

			pstmt = connection.prepareStatement("DELETE " +
					"FROM alm_documento_saida_item " +
					"WHERE cd_documento_saida = ?");
			pstmt.setInt(1, cdDocumentoSaida);
			pstmt.execute();

			for (int i=0; itens!=null && i<itens.size(); i++) {
				HashMap<String, Object> item = itens.get(i);
				int cdProdutoServico = ((Integer)item.get("cdProdutoServico")).intValue();
				int cdUnidadeMedida = ((Integer)item.get("cdUnidadeMedida")).intValue();
				int cdLocal = ((Integer)item.get("cdLocal")).intValue();
				float qtAtendida = ((Float)item.get("qtAtendida")).floatValue();
				if (DocumentoSaidaItemServices.insert(new DocumentoSaidaItem(cdDocumentoSaida,
						cdProdutoServico,
						solicitacao.getCdEmpresa(),
						qtAtendida /*qtSaida*/,
						0 /*vlUnitario*/,
						0 /*vlAcrescimo*/,
						0 /*vlDesconto*/,
						null /*dtEntregaPrevista*/,
						cdUnidadeMedida,
						0 /*cdTabelaPreco*/,
						0 /*cdItem*/,
						0 /*cdBico*/), cdLocal, 0 /*cdLocalDestino*/, false /*registerTributacao*/, connection).getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (finalizeAtendimento && concluirAtendSolicitacao(cdSolicitacaoMaterial, connection) <= 0) {
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
			System.err.println("Erro! SolicitacaoMaterialServices.setItensAtendidos: " + e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
