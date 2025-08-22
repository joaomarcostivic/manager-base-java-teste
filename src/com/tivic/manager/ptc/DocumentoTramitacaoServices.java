package com.tivic.manager.ptc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Setor;
import com.tivic.manager.grl.SetorDAO;

import sol.util.Result;
import sol.dao.ResultSetMap;

public class DocumentoTramitacaoServices {

	public static final String[] locaisDestinoDefault = {"Cliente", "Fornecedor/Matriz"};

	public static final int LOC_CLIENTE    = 0;
	public static final int LOC_FORNECEDOR = 1;
	public static final int LOC_OUTROS     = 2;

	public static ResultSetMap getTramitacoes(int cdDocumento) {
		return getTramitacoes(cdDocumento, null);
	}

	public static ResultSetMap getTramitacoes(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) 
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, B.nm_setor, C.nm_login AS nm_usuario_origem, D.nm_pessoa AS nm_pessoa_origem, " +
					"       E.nm_login AS nm_usuario_destino, F.nm_pessoa AS nm_pessoa_destino, " +
					"       G.nm_situacao_documento " +
					"FROM ptc_documento_tramitacao A " +
					"LEFT OUTER JOIN grl_setor              B ON (A.cd_setor_origem = B.cd_setor) " +
					"LEFT OUTER JOIN seg_usuario            C ON (A.cd_usuario_origem = C.cd_usuario) " +
					"LEFT OUTER JOIN grl_pessoa             D ON (C.cd_pessoa = D.cd_pessoa) " +
					"LEFT OUTER JOIN seg_usuario            E ON (A.cd_usuario_destino = E.cd_usuario) " +
					"LEFT OUTER JOIN grl_pessoa             F ON (E.cd_pessoa = F.cd_pessoa) " +
					"LEFT OUTER JOIN ptc_situacao_documento G ON (A.cd_situacao_documento = G.cd_situacao_documento) " +
					"WHERE a.cd_documento = "+cdDocumento+" ORDER BY a.dt_tramitacao DESC");

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

	public static DocumentoTramitacao getUltimaTramitacao(int cdDocumento) {
		return getUltimaTramitacao(cdDocumento, null);
	}

	public static DocumentoTramitacao getUltimaTramitacao(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			PreparedStatement pstmt = connect.prepareStatement(" SELECT * FROM ptc_documento_tramitacao" +
																" WHERE cd_tramitacao = (SELECT max(cd_tramitacao)" +
																							" FROM ptc_documento_tramitacao " +
																							" WHERE cd_documento = ?) " +
																" AND cd_documento = ?");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, cdDocumento);

			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return !rsm.next() ? null : new DocumentoTramitacao(
						rsm.getInt("cd_documento_tramitacao"),
						rsm.getInt("cd_documento"),
						rsm.getInt("cd_setor_destino", 0),
						rsm.getInt("cd_usuario_destino", 0),
						rsm.getInt("cd_setor_origem", 0),
						rsm.getInt("cd_usuario_origem", 0),
						rsm.getGregorianCalendar("dt_tramitacao"),
						rsm.getGregorianCalendar("dt_recebimento"),
						rsm.getString("nm_local_destino"),
						rsm.getString("txt_tramitacao"),
						rsm.getInt("cd_situacao_documento", 0),
						rsm.getInt("cd_fase", 0), 
						rsm.getString("nm_local_origem"),
						rsm.getString("nr_remessa"));
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

	public static Result insert(DocumentoTramitacao tramitacao) {
		return insert(tramitacao, null);
	}

	public static Result insert(DocumentoTramitacao tramitacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			// Verifica se já existe um andamento em aberto
			
			DocumentoTramitacao tra = getUltimaTramitacao(tramitacao.getCdDocumento(), connect);
			
			ResultSet rs = connect.prepareStatement("SELECT * FROM ptc_documento_tramitacao A " +
					                                "WHERE A.cd_documento = " +tramitacao.getCdDocumento()+
					   								"  AND dt_recebimento IS NULL"
					   								+ " AND cd_tramitacao="+tra.getCdTramitacao()).executeQuery();
			if(rs.next())
				return new Result(-1, "Documento já em tramitação!");
			
			String nmSetorOrigem = null;
			// Pesquisa o setor de origem
			rs = connect.prepareStatement("SELECT A.cd_setor_destino, B.nm_setor " +
										  "FROM ptc_documento_tramitacao A, grl_setor B " +
										  "WHERE A.cd_setor_origem = B.cd_setor " +
									      "  AND A.cd_documento = " +tramitacao.getCdDocumento()+
										  "ORDER BY dt_tramitacao DESC").executeQuery();
			int cdOldSetor = rs.next() ? rs.getInt("cd_setor_destino") : 0;
			nmSetorOrigem = cdOldSetor>0 ? rs.getString("nm_setor") : null;
			if (cdOldSetor <= 0) {
				Documento documento = DocumentoDAO.get(tramitacao.getCdDocumento(), connect);
				cdOldSetor = documento.getCdSetor();
				Setor setor = cdOldSetor<=0 ? null : SetorDAO.get(cdOldSetor, connect);
				nmSetorOrigem = setor==null ? null : setor.getNmSetor();
			}
			tramitacao.setCdSetorOrigem(cdOldSetor);
			/* Localiza a situacao de documento */
			int cdSituacaoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_TRAMITANDO", 0, 0, connect);
			if (cdSituacaoDocumento <= 0)
				return new Result(-10, "Situação de documento recebido/parado não definida!");
			tramitacao.setCdSituacaoDocumento(cdSituacaoDocumento);
			tramitacao.setDtTramitacao(new GregorianCalendar());
			if (DocumentoTramitacaoDAO.insert(tramitacao, connect)<=0)
				return new Result(-1, "Erro ao tentar inserir Tramitação!");

			Setor setor = tramitacao.getCdSetorDestino()<=0 ? null : SetorDAO.get(tramitacao.getCdSetorDestino(), connect);
			String nmSetorDestino = setor==null ? null : setor.getNmSetor();


			Result result = new Result(1);
			result.addObject("tramitacao", tramitacao);
			result.addObject("nmSetorOrigem", nmSetorOrigem);
			result.addObject("nmSetorDestino", nmSetorDestino);
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir Tramitação!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getUltimaTramitacaoAsResultSetMap(int cdDocumento) {
		return getUltimaTramitacaoAsResultSetMap(cdDocumento, null);
	}

	public static ResultSetMap getUltimaTramitacaoAsResultSetMap(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement(
					" SELECT A.*, B.nm_situacao_documento, C.nm_setor AS nm_setor_origem, D.nm_setor AS nm_setor_destino" +
					" FROM ptc_documento_tramitacao A" +
					"      INNER JOIN ptc_situacao_documento B ON (a.cd_situacao_documento = B.cd_situacao_documento)" +
					"      LEFT JOIN grl_setor C ON (A.cd_setor_origem = C.cd_setor)" +
					"      LEFT JOIN grl_setor D ON (A.cd_setor_destino = D.cd_setor)" +
					" WHERE A.dt_tramitacao = (SELECT max(dt_tramitacao)" +
					"                          FROM ptc_documento_tramitacao A" +
					"                          WHERE cd_documento = ?)" +
					" AND A.cd_documento = ?");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, cdDocumento);

			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if (isConnectionNull)
				connect.commit();

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

	public static ResultSetMap getTramitacaoAsResultSetMap(int cdDocumentoTramitacao, int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM ptc_documento_tramitacao " +
					                                           "WHERE cd_documento_tramitacao=? AND cd_documento=?");
			pstmt.setInt(1, cdDocumentoTramitacao);
			pstmt.setInt(2, cdDocumento);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			if (isConnectionNull)
				connect.commit();

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
	
	public static Result save(DocumentoTramitacao tramitacao){
		return save(tramitacao, null);
	}
	
	public static Result save(DocumentoTramitacao tramitacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tramitacao==null)
				return new Result(-1, "Erro ao salvar. Tramitação é nula");
			
			// Verifica se já existe pessoa no documento
			ResultSet rs = connect.prepareStatement("SELECT * FROM ptc_documento_tramitacao A " +
					                                "WHERE A.cd_documento = " +tramitacao.getCdDocumento()+
					   								"  AND A.cd_tramitacao ="+tramitacao.getCdTramitacao()).executeQuery();
			int retorno = 0;
			
			if(rs.next()) {
				retorno = DocumentoTramitacaoDAO.update(tramitacao, connect);
			}
			else {
				retorno = DocumentoTramitacaoDAO.insert(tramitacao, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TRAMITACAO", tramitacao);
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

}
