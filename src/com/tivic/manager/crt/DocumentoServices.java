package com.tivic.manager.crt;

import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.ItemComparator;
import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.*;


public class DocumentoServices	{

	public static final int ST_PARADO = 0;
	public static final int ST_TRAMITANDO = 1;
	public static final int ST_ARQUIVADO = 2;
	public static final int ST_SETOR_EXTERNO = 3;

	public static ResultSetMap insertMovimento(ArrayList<Integer> docs, int cdEmpresaDestino, int cdPessoaDestino, String nmLocalDestino)	{
		Connection connect = Conexao.conectar();
		ResultSetMap rsmLog = new ResultSetMap();
		try	{
			for(int i=0; i<docs.size(); i++){
				int ret = insertMovimento(((Integer)docs.get(i)).intValue(), cdEmpresaDestino, cdPessoaDestino, nmLocalDestino, connect);
				if (ret <= 0)	{
					HashMap<String,Object> register = new HashMap<String,Object>();
					register.put("CD_ERRO", new Integer(ret));
					register.put("CD_DOCUMENTO", docs.get(i));
					register.put("DS_ERRO", ret==-10?"Usuario destino não registrado na empresa informada":"Erro desconhecido");
					rsmLog.addRegister(register);
				}
			}
			return rsmLog;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.insertMovimento: " + e);
			return rsmLog;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static int insertMovimento(int cdDocumento, int cdEmpresaDestino, int cdPessoaDestino, String nmLocalDestino, Connection connect)	{
		boolean isConnectionNull = (connect==null);
		if(isConnectionNull)
			connect = Conexao.conectar();
		try	{
			int code = Conexao.getSequenceCode("PTC_TRAMITACAO_DOCUMENTO", connect);
			connect.setAutoCommit(false);
			if(cdPessoaDestino>0 && !connect.prepareStatement("SELECT * FROM seg_usuario_empresa " +
															  "WHERE cd_empresa = "+cdEmpresaDestino+
															  "  AND cd_usuario = "+cdPessoaDestino).executeQuery().next())	{
				return -10;
			}
			//
			PreparedStatement pstmt = connect.prepareStatement(
					"INSERT INTO ptc_tramitacao_documento (cd_documento,cd_tramitacao,dt_tramitacao,cd_pessoa_destino," +
					"cd_empresa_destino,st_tramitacao,nm_local_destino,lg_liberado)VALUES (?,?,?,?,?,?,?,1)");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, code);
			pstmt.setTimestamp(3, new Timestamp((new GregorianCalendar()).getTimeInMillis()));
			if(cdPessoaDestino > 0)
				pstmt.setInt(4, cdPessoaDestino);
			else
				pstmt.setNull(4, 4);
			if(cdEmpresaDestino > 0){
				pstmt.setInt(5, cdEmpresaDestino);
				pstmt.setInt(6, 0);
			}
			else	{
				pstmt.setNull(5, 4);
				pstmt.setInt(6, 1);
			}
			pstmt.setString(7, nmLocalDestino);
			pstmt.executeUpdate();
			pstmt = connect.prepareStatement("UPDATE ptc_documento SET st_documento = " + (cdEmpresaDestino <= 0 ? 3 : 1) + " WHERE cd_documento = " + cdDocumento);
			pstmt.executeUpdate();
			connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			Conexao.rollback(connect);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.insertMovimento: " + sqlExpt);
			return -1 * sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.insertMovimento: " + e);
			return -1;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap setRecebimento(ArrayList<Integer> docs, ArrayList<Integer> trs, int cdPessoaAtual)	{
		Connection connect = Conexao.conectar();
		ResultSetMap rsmLog = new ResultSetMap();
		try	{
			for(int i=0; i<docs.size(); i++){
				int ret = setRecebimento(((Integer)docs.get(i)).intValue(), ((Integer)trs.get(i)).intValue(), cdPessoaAtual, connect);
				if (ret <= 0)	{
					HashMap<String,Object> register = new HashMap<String,Object>();
					register.put("CD_ERRO", new Integer(ret));
					register.put("CD_DOCUMENTO", docs.get(i));
					register.put("DS_ERRO", "Erro desconhecido");
					rsmLog.addRegister(register);
				}
			}
			return rsmLog;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.insertMovimento: " + e);
			return rsmLog;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static int setRecebimento(int cdDocumento, int cdTramitacao, int cdPessoaAtual, Connection connect)	{
		boolean isConnectionNull = (connect==null);
		if(isConnectionNull)
			connect = Conexao.conectar();
		try		{
			int cdEmpresaAtual = 0;
			connect.setAutoCommit(false);
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_empresa_destino, cd_pessoa_destino FROM ptc_tramitacao_documento WHERE" +
					" cd_documento = "
					+ cdDocumento + "  AND cd_tramitacao = " + cdTramitacao);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				cdEmpresaAtual = rs.getInt("cd_empresa_destino");

			pstmt = connect.prepareStatement("UPDATE ptc_tramitacao_documento SET dt_recebimento=?, st_tramitacao = ? " +
					                         "WHERE cd_documento = ? " +
					                         "   AND cd_tramitacao = ? ");
			pstmt.setTimestamp(1, new Timestamp((new GregorianCalendar()).getTimeInMillis()));
			pstmt.setInt(2, 1);
			pstmt.setInt(3, cdDocumento);
			pstmt.setInt(4, cdTramitacao);
			pstmt.executeUpdate();
			pstmt = connect.prepareStatement("UPDATE ptc_documento SET st_documento = 0   ,cd_pessoa_atual  = " + cdPessoaAtual + "   ,cd_empresa_atual = " + cdEmpresaAtual + " WHERE cd_documento = " + cdDocumento);
			pstmt.executeUpdate();
			connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			Conexao.rollback(connect);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.setRecebimento: " + sqlExpt);
			return -1 * sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.setRecebimento: " + e);
			return -1;
		}
		finally		{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap setArquivamento(ArrayList<Integer> docs)	{
		Connection connect = Conexao.conectar();
		ResultSetMap rsmLog = new ResultSetMap();
		try	{
			for(int i=0; i<docs.size(); i++){
				int ret = setArquivamento(((Integer)docs.get(i)).intValue(), connect);
				if (ret <= 0)	{
					HashMap<String,Object> register = new HashMap<String,Object>();
					register.put("CD_ERRO", new Integer(ret));
					register.put("CD_DOCUMENTO", docs.get(i));
					register.put("DS_ERRO", "Erro desconhecido");
					rsmLog.addRegister(register);
				}
			}
			return rsmLog;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.setArquivamento: " + e);
			return rsmLog;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static int setArquivamento(int cdDocumento, Connection connect)	{
		boolean isConectionNull = (connect==null);
		if(isConectionNull)
			connect = Conexao.conectar();
		try		{
			connect.setAutoCommit(false);
			PreparedStatement pstmt = connect.prepareStatement(
					"UPDATE ptc_documento SET st_documento = " + ST_ARQUIVADO +
					"                        ,dt_arquivamento = ? "+
					"WHERE cd_documento = " + cdDocumento +
					"  AND st_documento = 0");
			pstmt.setTimestamp(1, new Timestamp(new GregorianCalendar().getTimeInMillis()));
			if(pstmt.executeUpdate() > 0)	{
				int cdSituacaoProposta = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_ARQUIVADO", 0);
				if(cdSituacaoProposta > 0)
					connect.prepareStatement("UPDATE sce_contrato SET cd_situacao = " + cdSituacaoProposta +
							                 "WHERE cd_documento = " + cdDocumento).executeUpdate();
			}
			else
				return -10;
			connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			Conexao.rollback(connect);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.setRecebimento: " + sqlExpt);
			return -1 * sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.setRecebimento: " + e);
			return -1;
		}
		finally		{
			if(isConectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios)	{
		String sqlBloqueados = "JOIN sce_contrato_comissao CC ON (CC.cd_contrato_emprestimo = F.cd_contrato_emprestimo) " +
				               "WHERE CC.cd_nota_fiscal IS NULL "+
					           "  AND CC.cd_conta_pagar IS NULL " +
					           "  AND CC.vl_pago > 0 " +
					           "  AND CC.cd_pessoa IS NOT NULL "+
					           "  AND F.cd_contrato_emprestimo IN (SELECT X.cd_contrato_emprestimo "+
					           "                                   FROM sce_contrato_comissao X, adm_nota_fiscal Y, sce_fechamento W "+
					           "                                   WHERE Y.cd_fechamento = W.cd_fechamento" +
					           "                                     AND W.cd_empresa IS NOT NULL " +
					           "                                     AND Y.cd_nota_fiscal = X.cd_nota_fiscal) ";
		String sql = "SELECT A.cd_documento, A.dt_documento, A.nm_local_origem, A.nm_local_destino, " +
					 "       A.lg_confidencial, A.id_documento, A.cd_pessoa_atual, A.cd_pessoa_emissor, " +
					 "       A.cd_empresa_emissora, A.cd_empresa_atual, A.st_documento, A.txt_documento,  " +
					 "       B.nm_pessoa AS nm_pessoa_emissor, C.nm_pessoa AS nm_pessoa_atual,     " +
					 "       D.nm_empresa AS nm_empresa_emissora, E.nm_empresa AS nm_empresa_atual,       " +
					 "       F.qt_parcelas, F.vl_financiado, F.cd_agente, G.nm_pessoa AS nm_contratante, G.nr_cpf_cnpj, " +
					 "       H.nm_pessoa AS nm_agente, J.nm_pessoa AS nm_colaborador, J.nr_cpf_cnpj AS nr_cpf_cnpj_colaborador, " +
					 "       L.nm_produto, M.cd_pessoa AS cd_parceiro, M.nm_pessoa AS nm_parceiro " +
					 "FROM ptc_documento A " +
					 "LEFT OUTER JOIN grl_pessoa   B ON (A.cd_pessoa_emissor   = B.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_pessoa   C ON (A.cd_pessoa_atual     = C.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_empresa  D ON (A.cd_empresa_emissora = D.cd_empresa) " +
					 "LEFT OUTER JOIN grl_empresa  E ON (A.cd_empresa_atual    = E.cd_empresa) " +
					 "LEFT OUTER JOIN sce_contrato F ON (A.cd_documento   = F.cd_documento) " +
					 "LEFT OUTER JOIN grl_pessoa   G ON (F.cd_contratante = G.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_pessoa   H ON (F.cd_agente      = H.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_pessoa_empresa I ON (A.cd_documento = I.cd_documento) " +
					 "LEFT OUTER JOIN grl_pessoa   J ON (I.cd_pessoa = J.cd_pessoa) " +
					 "LEFT OUTER JOIN sce_produto  L ON (F.cd_produto = L.cd_produto) " +
					 "LEFT OUTER JOIN grl_pessoa   M ON (L.cd_instituicao_financeira = M.cd_pessoa) ";
		for(int i=0; i<criterios.size(); i++)
			if(criterios.get(i).getColumn().toUpperCase().equals("LG_BLOQUEADO"))	{
				criterios.remove(i);
				sql += sqlBloqueados;
				break;
			}

		return Search.find(sql, criterios, true, Conexao.conectar(), true);
	}

	public static ResultSetMap findPendencia(int cdAtendente)	{
		String sql = "SELECT A.cd_documento, A.dt_documento, A.nm_local_origem, A.nm_local_destino, " +
					 "       A.lg_confidencial, A.id_documento, A.cd_pessoa_atual, A.cd_pessoa_emissor, " +
					 "       A.cd_empresa_emissora, A.cd_empresa_atual, A.st_documento, A.txt_documento,  " +
					 "       B.nm_pessoa AS nm_pessoa_emissor, C.nm_pessoa AS nm_pessoa_atual,     " +
					 "       D.nm_empresa AS nm_empresa_emissora, E.nm_empresa AS nm_empresa_atual,       " +
					 "       F.qt_parcelas, F.vl_financiado, F.cd_agente, G.nm_pessoa AS nm_contratante, G.nr_cpf_cnpj, " +
					 "       H.nm_pessoa AS nm_agente, J.nm_pessoa AS nm_colaborador, J.nr_cpf_cnpj AS nr_cpf_cnpj_colaborador, " +
					 "       L.nm_produto, M.cd_pessoa AS cd_parceiro, M.nm_pessoa AS nm_parceiro " +
					 "FROM ptc_documento A " +
					 "LEFT OUTER JOIN grl_pessoa   B ON (A.cd_pessoa_emissor   = B.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_pessoa   C ON (A.cd_pessoa_atual     = C.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_empresa  D ON (A.cd_empresa_emissora = D.cd_empresa) " +
					 "LEFT OUTER JOIN grl_empresa  E ON (A.cd_empresa_atual    = E.cd_empresa) " +
					 "LEFT OUTER JOIN sce_contrato F ON (A.cd_documento   = F.cd_documento) " +
					 "LEFT OUTER JOIN grl_pessoa   G ON (F.cd_contratante = G.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_pessoa   H ON (F.cd_agente      = H.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_pessoa_empresa I ON (A.cd_documento = I.cd_documento) " +
					 "LEFT OUTER JOIN grl_pessoa   J ON (I.cd_pessoa = J.cd_pessoa) " +
					 "LEFT OUTER JOIN sce_produto  L ON (F.cd_produto = L.cd_produto) " +
					 "LEFT OUTER JOIN grl_pessoa   M ON (L.cd_instituicao_financeira = M.cd_pessoa) " +
					 "WHERE A.cd_pessoa_atual = F.cd_agente " +
					 "  AND F.cd_atendente    = "+cdAtendente;
		return Search.find(sql, new ArrayList<ItemComparator>(), true, Conexao.conectar(), true);
	}

	public static ResultSetMap findDocumentoEnviado(int cdPessoa)	{
		Connection connect = Conexao.conectar();
		try	{
			String sql = "SELECT A.cd_documento, A.dt_documento, A.nm_local_origem, " +
					     "       A.lg_confidencial, A.id_documento, A.cd_pessoa_atual, A.cd_pessoa_emissor, " +
					     "       A.cd_empresa_emissora, A.cd_empresa_atual, A.st_documento, A.txt_documento, " +
					     "       B.nm_pessoa AS nm_pessoa_destino, D.nm_empresa AS nm_empresa_destino, " +
					     "       F.qt_parcelas, F.vl_financiado, G.nm_pessoa AS nm_contratante, G.nr_cpf_cnpj, " +
					     "       H.nm_pessoa AS nm_agente, J.nm_pessoa AS nm_colaborador, J.nr_cpf_cnpj AS nr_cpf_cnpj_colaborador, " +
					     "       L.nm_produto, M.nm_pessoa AS nm_parceiro, M.cd_pessoa AS cd_parceiro, " +
					     "       TD.dt_tramitacao, TD.cd_tramitacao, TD.nm_local_destino, TD.dt_recebimento " +
					     "FROM ptc_documento  A " +
					     "JOIN ptc_tramitacao_documento TD ON (A.cd_documento  = TD.cd_documento       " +
						 "                                 AND TD.st_tramitacao = 0) " +
						 "LEFT OUTER JOIN grl_pessoa     B ON (TD.cd_pessoa_destino  = B.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_empresa  D ON (TD.cd_empresa_destino = D.cd_empresa) " +
						 "LEFT OUTER JOIN sce_contrato F ON (A.cd_documento   = F.cd_documento) " +
						 "LEFT OUTER JOIN grl_pessoa   G ON (F.cd_contratante = G.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_pessoa   H ON (F.cd_agente      = H.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_pessoa_empresa I ON (A.cd_documento = I.cd_documento) " +
						 "LEFT OUTER JOIN grl_pessoa   J ON (I.cd_pessoa = J.cd_pessoa) " +
						 "LEFT OUTER JOIN sce_produto  L ON (F.cd_produto = L.cd_produto) " +
						 "LEFT OUTER JOIN grl_pessoa   M ON (L.cd_instituicao_financeira = M.cd_pessoa) " +
						 "WHERE A.cd_pessoa_atual = " + cdPessoa +
						 "  AND A.st_documento    = 1";
			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.findDocumentoEnviado: " + sqlExpt);
			return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.findDocumentoEnviado: " + e);
			return null;
		}
		finally		{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findDocumentoChegando(int cdEmpresa, int cdPessoa, ArrayList<ItemComparator> criterios)	{
		try		{
			String sql = "SELECT A.cd_documento, A.dt_documento, A.nm_local_origem, " +
					     "       A.lg_confidencial, A.id_documento, A.cd_pessoa_atual, A.cd_pessoa_emissor, " +
					     "       A.cd_empresa_emissora, A.cd_empresa_atual, A.st_documento, A.txt_documento, " +
					     "       B.nm_pessoa AS nm_pessoa_atual, D.nm_empresa AS nm_empresa_atual, F.cd_agente, " +
					     "       F.qt_parcelas, F.vl_financiado, G.nm_pessoa AS nm_contratante, G.nr_cpf_cnpj," +
					     "       H.nm_pessoa AS nm_agente, J.nm_pessoa AS nm_colaborador, " +
					     "       J.nr_cpf_cnpj AS nr_cpf_cnpj_colaborador, L.nm_produto, " +
					     "       M.nm_pessoa AS nm_parceiro, M.cd_pessoa AS cd_parceiro, TD.dt_tramitacao, " +
					     "       TD.cd_tramitacao, TD.nm_local_destino, TD.dt_recebimento FROM ptc_documento A " +
						 "JOIN ptc_tramitacao_documento TD ON (A.cd_documento   = TD.cd_documento         " +
						 "                                 AND TD.st_tramitacao = 0   " +
						 "                                 AND (TD.cd_pessoa_destino  = "+ cdPessoa +
						 "                                   OR TD.cd_pessoa_destino IS NULL) " +
						 "                                 AND  TD.cd_empresa_destino = " + cdEmpresa + ")" +
						 "LEFT OUTER JOIN grl_pessoa   B ON (A.cd_pessoa_atual  = B.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_empresa  D ON (A.cd_empresa_atual = D.cd_empresa) " +
						 "LEFT OUTER JOIN sce_contrato F ON (A.cd_documento   = F.cd_documento) " +
						 "LEFT OUTER JOIN grl_pessoa   G ON (F.cd_contratante = G.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_pessoa   H ON (F.cd_agente      = H.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_pessoa_empresa I ON (A.cd_documento = I.cd_documento) " +
						 "LEFT OUTER JOIN grl_pessoa   J ON (I.cd_pessoa = J.cd_pessoa) " +
						 "LEFT OUTER JOIN sce_produto  L ON (F.cd_produto = L.cd_produto) " +
						 "LEFT OUTER JOIN grl_pessoa   M ON (L.cd_instituicao_financeira = M.cd_pessoa) ";
			return Search.find(sql, criterios, true, Conexao.conectar(), true);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.findDocumentoChegando: " + e);
			return null;
		}
	}

	public static ResultSetMap findDocumentoChegandoMatriz(int cdPessoa, ArrayList<ItemComparator> criterios)	{
		Connection connect = Conexao.conectar();
		try		{
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT cd_empresa FROM grl_empresa WHERE lg_matriz = 1").executeQuery());
			String matrizes = "";
			while(rsm.next())
				matrizes += (matrizes.equals("") ? "" : "," )+ rsm.getInt("cd_empresa");

			String sql = "SELECT A.cd_documento, A.dt_documento, A.nm_local_origem, " +
					     "       A.lg_confidencial, A.id_documento, A.cd_pessoa_atual, A.cd_pessoa_emissor, " +
					     "       A.cd_empresa_emissora, A.cd_empresa_atual, A.st_documento, A.txt_documento, " +
					     "       B.nm_pessoa AS nm_pessoa_atual, D.nm_empresa AS nm_empresa_atual, F.cd_agente, " +
					     "       F.qt_parcelas, F.vl_financiado, G.nm_pessoa AS nm_contratante, G.nr_cpf_cnpj," +
					     "       H.nm_pessoa AS nm_agente, J.nm_pessoa AS nm_colaborador, " +
					     "       J.nr_cpf_cnpj AS nr_cpf_cnpj_colaborador, L.nm_produto, " +
					     "       M.nm_pessoa AS nm_parceiro, M.cd_pessoa AS cd_parceiro, TD.dt_tramitacao, " +
					     "       TD.cd_tramitacao, TD.nm_local_destino, TD.dt_recebimento " +
					     "FROM ptc_documento A " +
						 "JOIN ptc_tramitacao_documento TD ON (A.cd_documento   = TD.cd_documento         " +
						 "                                 AND TD.st_tramitacao = 0   " +
						 "                                 AND (TD.cd_pessoa_destino  = "+ cdPessoa +
						 "                                   OR TD.cd_pessoa_destino IS NULL) " +
						 "                                 AND  TD.cd_empresa_destino IN ("+matrizes+"))" +
						 "LEFT OUTER JOIN grl_pessoa   B ON (A.cd_pessoa_atual  = B.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_empresa  D ON (A.cd_empresa_atual = D.cd_empresa) " +
						 "LEFT OUTER JOIN sce_contrato F ON (A.cd_documento   = F.cd_documento) " +
						 "LEFT OUTER JOIN grl_pessoa   G ON (F.cd_contratante = G.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_pessoa   H ON (F.cd_agente      = H.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_pessoa_empresa I ON (A.cd_documento = I.cd_documento) " +
						 "LEFT OUTER JOIN grl_pessoa   J ON (I.cd_pessoa = J.cd_pessoa) " +
						 "LEFT OUTER JOIN sce_produto  L ON (F.cd_produto = L.cd_produto) " +
						 "LEFT OUTER JOIN grl_pessoa   M ON (L.cd_instituicao_financeira = M.cd_pessoa) ";
			return Search.find(sql, criterios, true, connect, true);
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.findDocumentoChegandoMatriz: " + sqlExpt);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findTramitacao(ArrayList<ItemComparator> criterios)	{
		String sql = "SELECT A.*, B.nm_pessoa AS nm_pessoa_destino, C.nm_empresa AS nm_empresa_destino" +
		             " FROM ptc_tramitacao_documento A " +
		             "LEFT OUTER JOIN grl_pessoa   B ON (A.cd_pessoa_destino  = B.cd_pessoa) " +
		             "LEFT OUTER JOIN grl_empresa  C ON (A.cd_empresa_destino = C.cd_empresa) ";
		return Search.find(sql, criterios, true, Conexao.conectar(), true);
	}

	public static ResultSetMap setRecebimentoAgente(ArrayList<Integer> docs, int cdEmpresaAtual, int cdPessoaAtual)	{
		Connection connect = Conexao.conectar();
		ResultSetMap rsmLog = new ResultSetMap();
		try	{
			for(int i=0; i<docs.size(); i++){
				int cdDocumento = docs.get(i).intValue();
				int cdTramitacao = insertMovimento(cdDocumento, cdEmpresaAtual, cdPessoaAtual, "", connect);
				int ret = cdTramitacao;
				if(cdTramitacao>0)
					ret = setRecebimento(cdDocumento, cdTramitacao, cdPessoaAtual, connect);
				if (ret <= 0)	{
					HashMap<String,Object> register = new HashMap<String,Object>();
					register.put("CD_ERRO", new Integer(ret));
					register.put("CD_DOCUMENTO", docs.get(i));
					register.put("DS_ERRO", ret==-10?"Usuario destino não registrado na empresa informada":"Erro desconhecido");
					rsmLog.addRegister(register);
					System.out.println("DocumentoServices.setRecebimentoAgente: "+(ret==-10?"Usuario destino não registrado na empresa informada":"Erro desconhecido"));
				}
			}
			return rsmLog;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.setRecebimentoAgente: " + e);
			return rsmLog;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static int setRecebimentoAgente(int cdDocumento, int cdEmpresaAtual, int cdPessoaAtual)	{
		Connection connect = Conexao.conectar();
		try	{
			int cdTramitacao = insertMovimento(cdDocumento, cdEmpresaAtual, cdPessoaAtual, "", connect);
			if(cdTramitacao>0)
				return setRecebimento(cdDocumento, cdTramitacao, cdPessoaAtual, connect);
			return -1;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.setRecebimentoAgente: " + e);
			return -1;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

}
