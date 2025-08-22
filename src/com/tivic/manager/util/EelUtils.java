package com.tivic.manager.util;

import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.Setor;
import com.tivic.manager.grl.SetorDAO;
import com.tivic.manager.grl.SetorServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.DocumentoPessoa;
import com.tivic.manager.ptc.TipoDocumento;
import com.tivic.manager.ptc.TipoDocumentoDAO;
import com.tivic.manager.seg.UsuarioDAO;

import sol.dao.ResultSetMap;
import sol.util.Result;

//=======================================//
//TODO: PADRONIZAR NOMECLATURAS DA CLASSE//
//=======================================//
public class EelUtils {
	
	public static int TP_EXTERNO = 0;
	public static int TP_INTERNO = 1;
	public static String[] internoExterno = {"E", "I"};
 	
	
	/*
	 * CONEXAO
	 */
	public static Connection conectarEelSqlServer() {
		try {
			String driver = Util.getConfManager().getProps().getProperty("EEL_SQLSERVER_DRIVER");
			String dbPath = Util.getConfManager().getProps().getProperty("EEL_SQLSERVER_DBPATH");
			String login = Util.getConfManager().getProps().getProperty("EEL_SQLSERVER_LOGIN");
			String pass = Util.getConfManager().getProps().getProperty("EEL_SQLSERVER_PASS");

			Class.forName(driver).newInstance();
	  		DriverManager.setLoginTimeout(1200);
			return DriverManager.getConnection(dbPath, login, pass);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public static void desconectarEelSqlServer(Connection connect){
		try	{
			connect.close();
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
		}
	}
	
	/**
	 * Busca assuntos de documento 
	 * 
	 * @return
	 */
	public static ResultSetMap getAllAssuntos() {
		return getAllAssuntos(null);
	}

	public static ResultSetMap getAllAssuntos(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEelSqlServer();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("use CONT_VIT_CONQUISTA_PREFEITURA; "
					+ " SELECT codigo_ass AS cd_assunto, nome_ass AS nm_assunto FROM pr_assuntos ORDER BY nm_assunto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.getAllAssuntos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}
	
	/**
	 * Busca tipos de documentos
	 * 
	 * @return
	 */
	public static ResultSetMap getAllTiposDocumento() {
		return getAllTiposDocumento(null);
	}

	public static ResultSetMap getAllTiposDocumento(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEelSqlServer();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("use CONT_VIT_CONQUISTA_PREFEITURA; "
					+ " SELECT codigo_tdc AS nr_tipo_documento, nome AS nm_tipo_documento FROM pr_tiposdocs ORDER BY nm_tipo_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.getAllTiposDocumento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}
	
	/**
	 * Busca locais
	 * 
	 * @return
	 */
	public static ResultSetMap getAllSetores() {
		return getAllSetores(null);
	}

	public static ResultSetMap getAllSetores(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEelSqlServer();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("use CONT_VIT_CONQUISTA_PREFEITURA; "
					+ " SELECT codigo_local AS nr_setor, nome_local AS nm_setor, (COALESCE(codigo_local,'')+ ' - '+COALESCE(nome_local,'')) AS ds_setor "
					+ "FROM gg_local ORDER BY nm_setor");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.getAllSetores: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}
	
	/**
	 * Busca situações de documentos (equivalente a ptc_fase)
	 * 
	 * @return
	 */
	public static ResultSetMap getAllSituacoes() {
		return getAllSituacoes(null);
	}

	public static ResultSetMap getAllSituacoes(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEelSqlServer();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("use CONT_VIT_CONQUISTA_PREFEITURA; "
					+ " SELECT codigo_sit AS cd_fase, nome AS nm_fase FROM pr_situacao ORDER BY nm_fase");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.getAllSituacoes: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}
	
	public static ResultSetMap getDocumentos(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return getDocumentos(null, null, dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap getDocumentos(String nrDocumentoExterno, String ano, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return getDocumentos(nrDocumentoExterno, ano, dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap getDocumentos(String nrDocumentoExterno, String ano, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEelSqlServer();
		PreparedStatement pstmt;
		
		try {
			String lstSetores = SetorServices.getListSetoresEel();
			
			pstmt = connect.prepareStatement("use CONT_VIT_CONQUISTA_PREFEITURA; "
					+ " SELECT  A.controle AS cd_documento,"
					+ "		   	A.codigo_g_requerente AS cd_pessoa_usuario,"
					+ "			A.codigo_g_interessada AS cd_solicitante,"
					+ "		   	A.ano AS nr_ano,"
					+ "		   	A.data_registro AS dt_protocolo,"
					+ "			A.codigo_tdc AS cd_tipo_documento,"
					+ "			A.codigo_ass AS cd_assunto,"
					+ "			A.codigo_local_origem AS cd_setor,"
					+ "			A.observacao AS txt_observacao,"
					+ "			A.codigo_docproc AS nr_documento,"
					+ "			A.tipo AS tp_documento_externo,"
					+ "			(case A.interno_externo when 'E' then 0 when 'I' then 1 end) as tp_interno_externo, "
					+ "			C.nome_g AS nm_solicitante, "
					+ "			D.nome AS nm_tipo_documento,"
					+ "			E.nome_local AS nm_setor,"
					+ "			F.controle_princ AS cd_documento_superior,"
					+ "			G.controle_sec AS cd_documento_anexo"
					+ "	FROM pr_protocolo A"
					+ " JOIN pr_andamento B ON (A.controle = B.controle AND A.codigo_emp=B.codigo_emp AND A.codigo_fil=B.codigo_fil)"
					+ " LEFT OUTER JOIN gg_geral C ON (A.codigo_g_requerente = C.codigo_g)"
					+ " LEFT OUTER JOIN pr_tiposdocs D ON (A.codigo_tdc = D.codigo_tdc)"
					+ " LEFT OUTER JOIN gg_local E ON (A.codigo_local_origem = E.codigo_local)"
					+ " LEFT OUTER JOIN pr_anexaapensa F ON (A.controle = F.controle_sec)"
					+ " LEFT OUTER JOIN pr_anexaapensa G ON (A.controle = G.controle_princ)"
					+ " WHERE A.codigo_emp='001' AND A.codigo_fil='001'"
					+ (nrDocumentoExterno!=null && ano!=null ? " AND A.controle='"+nrDocumentoExterno+"' AND A.ano='"+ano+"'" : 
						" AND EXISTS ("
						+ " 			SELECT controle FROM pr_andamento WHERE "
						+ "					B.codigo_local_origem IN ("+lstSetores+")"
						+ "					OR "
						+ "					B.codigo_local_destino IN ("+lstSetores+")"
						+ " )"
						+ (dtInicial!=null ? " AND B.data_remessa >='"+Util.convCalendarStringSql(dtInicial)+"'" : "")
						+ (dtFinal!=null ? " AND B.data_remessa <='"+Util.convCalendarStringSql(dtFinal)+"'" : "")));
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.getDocumentos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}
	
	public static ResultSetMap getDocumento(String controle) {
		return getDocumento(controle, null);
	}
	
	public static ResultSetMap getDocumento(String controle, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEelSqlServer();
		PreparedStatement pstmt;
		
		
		try {
			pstmt = connect.prepareStatement("use CONT_VIT_CONQUISTA_PREFEITURA; "
					+ " SELECT  A.controle AS cd_documento,"
					+ "		   	A.codigo_g_requerente AS cd_pessoa_usuario,"
					+ "			A.codigo_g_interessada AS cd_solicitante,"
					+ "		   	A.ano AS nr_ano,"
					+ "		   	A.data_registro AS dt_protocolo,"
					+ "			A.codigo_tdc AS cd_tipo_documento,"
					+ "			A.codigo_ass AS cd_assunto,"
					+ "			A.codigo_local_origem AS cd_setor,"
					+ "			A.observacao AS txt_observacao,"
					+ "			A.codigo_docproc AS nr_documento,"
					+ "			C.nome_g AS nm_solicitante, "
					+ "			D.nome AS nm_tipo_documento,"
					+ "			E.nome_local AS nm_setor"
					+ "	FROM pr_protocolo A"
					+ " JOIN pr_andamento B ON (A.controle = B.controle AND A.codigo_emp=B.codigo_emp AND A.codigo_fil=B.codigo_fil)"
					+ " LEFT OUTER JOIN gg_geral C ON (A.codigo_g_requerente = C.codigo_g)"
					+ " LEFT OUTER JOIN pr_tiposdocs D ON (A.codigo_tdc = D.codigo_tdc)"
					+ " LEFT OUTER JOIN gg_local E ON (A.codigo_local_origem = E.codigo_local)"
					+ " WHERE A.codigo_emp='001' AND A.codigo_fil='001'"
					+ " AND A.controle = '"+controle+"'");
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.getDocumento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}
	
	public static ResultSetMap getTramitacao(String cdDocumentoExterno, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return getTramitacao(cdDocumentoExterno, dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap getTramitacao(String cdDocumentoExterno, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEelSqlServer();
		PreparedStatement pstmt;
		try {
			String lstSetores = SetorServices.getListSetoresEel();
			
			pstmt = connect.prepareStatement("use CONT_VIT_CONQUISTA_PREFEITURA; "
					+ " SELECT	A.codigo_local_origem AS cd_setor_origem,"
					+ " 		A.codigo_g_despachou AS cd_pessoa_origem,"
					+ "			A.codigo_local_destino AS cd_setor_destino,"
					+ "			A.codigo_g_recebeu AS cd_pessoa_destino,"
					+ "			A.data_remessa AS dt_envio,"
					+ "			A.codigo_rem AS nr_remessa,"
					+ "			A.data_recebimento AS dt_recebimento,"
					+ "			A.texto_despacho AS txt_tramitacao,"
					+ "			A.controle AS cd_documento_externo,"
					+ "			A.sequencia_tramite AS cd_tramitacao,"
					+ "			A.codigo_sit AS cd_fase,"
					+ "			B.nome_local AS nm_local_origem,"
					+ "			C.nome_local AS nm_local_destino"
					+ "	FROM pr_andamento A"
					+ " LEFT OUTER JOIN gg_local B ON (A.codigo_local_origem = B.codigo_local)"
					+ " LEFT OUTER JOIN gg_local C ON (A.codigo_local_destino = C.codigo_local)"
					+ " WHERE A.codigo_emp='001' AND A.codigo_fil='001'"
					+ (cdDocumentoExterno==null ? 
						  (dtInicial!=null ? " AND A.data_remessa >='"+Util.convCalendarStringSql(dtInicial)+"'" : "")
						+ (dtFinal!=null ? " AND A.data_remessa <='"+Util.convCalendarStringSql(dtFinal)+"'" : "")
						+ " AND EXISTS ("
						+ " 			SELECT controle FROM pr_andamento WHERE "
						+ "					A.codigo_local_origem IN ("+lstSetores+")"
						+ "					OR "
						+ "					A.codigo_local_destino IN ("+lstSetores+")"
						+ " )" : 
						" AND A.controle='"+cdDocumentoExterno+"'")
					+ " ORDER BY A.sequencia_tramite");
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.getTramitacao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}
	
	public static ResultSet getUltimaTramitacao(String nrControle) {
		return getUltimaTramitacao(nrControle, null);
	}

	public static ResultSet getUltimaTramitacao(String nrControle, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEelSqlServer();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("use CONT_VIT_CONQUISTA_PREFEITURA; "
					+ " SELECT A.*, B.nome_local AS nm_local_origem, C.nome_local AS nm_local_destino "
					+ " FROM pr_andamento A"
					+ " LEFT OUTER JOIN gg_local B ON (A.codigo_local_origem = B.codigo_local)"
					+ " LEFT OUTER JOIN gg_local C ON (A.codigo_local_destino = C.codigo_local)"
					+ " WHERE A.codigo_emp='001'"
					+ " AND A.codigo_fil='001'"
					+ " AND A.controle='"+nrControle+"'"
					+ " AND A.sequencia_tramite = (select max(sequencia_tramite) from pr_andamento "
					+ "								where controle='"+nrControle+"'"
							+ "						and codigo_emp='001' and codigo_fil='001')");
			
			return pstmt.executeQuery();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.getUltimaTramitacao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}
	
	public static ResultSet getAndamentos(String nrControle) {
		return getAndamentos(nrControle, null);
	}

	public static ResultSet getAndamentos(String nrControle, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEelSqlServer();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("use CONT_VIT_CONQUISTA_PREFEITURA; "
					+ " SELECT	A.codigo_local_origem AS cd_setor_origem,"
					+ " 		A.codigo_g_despachou AS cd_pessoa_origem,"
					+ "			A.codigo_local_destino AS cd_setor_destino,"
					+ "			A.codigo_g_recebeu AS cd_pessoa_destino,"
					+ "			A.data_remessa AS dt_envio,"
					+ "			A.data_recebimento AS dt_recebimento,"
					+ "			A.obs_recebimento AS txt_tramitacao,"
					+ "			A.controle AS cd_documento_externo,"
					+ "			A.sequencia_tramite AS cd_tramitacao,"
					+ "			A.codigo_sit AS cd_fase,"
					+ "			B.nome_local AS nm_local_origem,"
					+ "			C.nome_local AS nm_local_destino"
					+ "	FROM pr_andamento A"
					+ " LEFT OUTER JOIN gg_local B ON (A.codigo_local_origem = B.codigo_local)"
					+ " LEFT OUTER JOIN gg_local C ON (A.codigo_local_destino = C.codigo_local)"
					+ " WHERE A.codigo_emp='001' AND A.codigo_fil='001'"
					+ " AND A.controle='"+nrControle+"'"
					+ " ORDER BY A.sequencia_tramite");
			
			return pstmt.executeQuery();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.getAndamentos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}
	
	public static String getNrProtocoloByControle(String nrControle) {
		return getNrProtocoloByControle(nrControle);
	}

	public static String getNrProtocoloByControle(String nrControle, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEelSqlServer();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("use CONT_VIT_CONQUISTA_PREFEITURA; "
					+ " SELECT codigo_docproc AS nr_protocolo_externo FROM pr_protocolo"
					+ " WHERE controle='"+nrControle+"'");
			ResultSet rs = pstmt.executeQuery();
			String nrProtocoloExterno = null;
			
			if(rs.next()) {
				nrProtocoloExterno = rs.getString("nr_protocolo_externo");
			}
			
			return nrProtocoloExterno;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.getNrProtocoloByControle: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}
	
	public static ResultSetMap getGeral(String codigoGeral) {
		return getGeral(codigoGeral, null);
	}

	public static ResultSetMap getGeral(String codigoGeral, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEelSqlServer();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("use CONT_VIT_CONQUISTA_PREFEITURA; "
					+ " SELECT A.codigo_g AS cd_pessoa, A.nome_g AS nm_pessoa"
					+ " FROM gg_geral A"
					+ " WHERE codigo_emp='001'"
					+ " AND A.codigo_g='"+codigoGeral+"'");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.getGeral: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}
	
	
	public static ResultSetMap getSetoresUsados() {
		return getSetoresUsados(null);
	}
	
	public static ResultSetMap getSetoresUsados(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEelSqlServer();
		PreparedStatement pstmt;
		
		
		try {
			
			pstmt = connect.prepareStatement("use CONT_VIT_CONQUISTA_PREFEITURA; "
					+ " SELECT 	A.codigo_local AS cd_setor_externo, "
					+ " 		A.nome_Local AS nm_setor_externo, "
					+ "			A.sigla_local AS sg_setor_externo"
					+ " FROM gg_local A"
					+ " WHERE A.codigo_emp='001' AND "
					+ " A.codigo_local IN (SELECT codigo_local_origem FROM pr_protocolo) OR"
					+ " A.codigo_local IN (SELECT codigo_local_destino FROM pr_andamento) OR"
					+ " A.codigo_local IN (SELECT codigo_local_origem FROM pr_andamento)");
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.getDocumentos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}
	
	public static String[] getRequerenteByControle(String nrControle) {
		return getRequerenteByControle(nrControle, null);
	}

	public static String[] getRequerenteByControle(String nrControle, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEelSqlServer();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("use CONT_VIT_CONQUISTA_PREFEITURA; "
					+ " SELECT A.codigo_g_requerente AS cd_solicitante, B.nome_g AS nm_solicitante "
					+ " FROM pr_protocolo A"
					+ " LEFT OUTER JOIN gg_geral B ON (A.codigo_g_requerente = B.codigo_g)"
					+ " WHERE A.controle='"+nrControle+"'");
			ResultSet rs = pstmt.executeQuery();
			String[] solicitante = {null, null};
			
			if(rs.next()) {
				solicitante[0] = rs.getString("cd_solicitante");
				solicitante[1] = rs.getString("nm_solicitante");
			}
			
			return solicitante;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.getRequerenteByControle: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}
	
	public static Result deleteProtocolo(String controle) {
		return deleteProtocolo(controle, null);
	}

	public static Result deleteProtocolo(String controle, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEelSqlServer();
		PreparedStatement pstmt;
		try {
			int retorno = 0;
			
			pstmt = connect.prepareStatement(//"use CONT_VIT_CONQUISTA_PREFEITURA; "
					  " DELETE FROM pr_anexaapensa" 
					+ " WHERE codigo_emp=?"
					+ " AND codigo_fil=?"
					+ " AND controle_sec=?");
			pstmt.setString(1, "001");
			pstmt.setString(2, "001");
			pstmt.setString(3, controle);
			
			retorno = pstmt.executeUpdate();
			if(retorno<0) {
				return new Result(-6, "EelUtils.deleteProtocolo: Erro ao excluir pr_anexaapensa");
			}
			
			String[] tabelas = {"pr_processos", "pr_andamento", "pr_documentos", "pr_protocolo"};
			
			for(int i=0; i<tabelas.length; i++) {
				pstmt = connect.prepareStatement(//"use CONT_VIT_CONQUISTA_PREFEITURA; "
						  " DELETE FROM "+tabelas[i] 
						+ " WHERE codigo_emp=?"
						+ " AND codigo_fil=?"
						+ " AND controle=?");
				pstmt.setString(1, "001");
				pstmt.setString(2, "001");
				pstmt.setString(3, controle);
				
				retorno = pstmt.executeUpdate();
				if(retorno<0) {
					return new Result((i+1)*(-1), "EelUtils.deleteProtocolo: Erro ao excluir "+tabelas[i]);
				}
				
			}
			
			if(retorno<0) {
				return new Result(-5, "EelUtils.deleteProtocolo: Erro ao excluir.");
			}
			
			
			return new Result(1, "Excluído da E&L.");
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("EelUtils.deleteProtocolo: Erro ao excluír registro " + sqlExpt);
			return new Result(-1, "EelUtils.deleteProtocolo: Erro ao excluír registro");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.getAllAssuntos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}
	
	public static ResultSetMap findProtocolo(String codigo_docproc, String codigo_tdc, String codigo_local_origem, String ano, int nrRegistros,
			String data_registro_inicial, String data_registro_final, String detalhamento) {
		return findProtocolo(codigo_docproc, codigo_tdc, codigo_local_origem, ano, nrRegistros, 
				data_registro_inicial, data_registro_final, detalhamento,null);
	}
	
	public static ResultSetMap findProtocolo(String codigo_docproc, String codigo_tdc, 
			String codigo_local_origem, String ano, int nrRegistros, 
			String data_registro_inicial, String data_registro_final, String detalhamento,
			Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEelSqlServer();
		PreparedStatement pstmt;
		
//		System.out.println("codigo_docproc: "+codigo_docproc);
//		System.out.println("codigo_tdc: "+codigo_tdc);
//		System.out.println("codigo_local_origem: "+codigo_local_origem);
//		System.out.println("ano: "+ano);
		
		LogUtils.debug("::::::detalhamento: "+detalhamento);
		
		try {
			pstmt = connect.prepareStatement(//"use CONT_VIT_CONQUISTA_PREFEITURA; "
					  " SELECT TOP "+nrRegistros
					+ " A.controle AS nr_documento_externo,"
					+ "		   	A.codigo_g_requerente AS cd_solicitante,"
					+ "		   	A.ano AS nr_ano,"
					+ "		   	A.data_registro AS dt_protocolo,"
					+ "			A.codigo_tdc AS cd_tipo_documento,"
					+ "			A.codigo_ass AS cd_assunto,"
					+ "			A.codigo_local_origem AS cd_setor,"
					+ "			A.observacao AS txt_observacao,"
					+ "			A.codigo_docproc AS nr_documento,"
					+ "			C.nome_g AS nm_solicitante, "
					+ "			D.nome AS nm_tipo_documento,"
					+ "			E.nome_local AS nm_setor"
					+ "	FROM pr_protocolo A"
					+ " LEFT OUTER JOIN gg_geral C ON (A.codigo_g_requerente = C.codigo_g)"
					+ " LEFT OUTER JOIN pr_tiposdocs D ON (A.codigo_tdc = D.codigo_tdc)"
					+ " LEFT OUTER JOIN gg_local E ON (A.codigo_local_origem = E.codigo_local)"
					+ " WHERE A.codigo_emp='001' AND A.codigo_fil='001'"
					+ (codigo_docproc!=null ? " AND A.codigo_docproc LIKE '%"+codigo_docproc+"'" : "")
					+ (codigo_tdc!=null ? " AND A.codigo_tdc='"+codigo_tdc+"'" : "")
					+ (codigo_local_origem!=null ? " AND A.codigo_local_origem='"+codigo_local_origem+"'" : "")
					+ (ano!=null ? " AND A.ano='"+ano+"'" : "")
					+ (data_registro_inicial!=null ? " AND A.data_registro>='"+data_registro_inicial+"'" : "")
					+ (data_registro_final!=null ? " AND A.data_registro<='"+data_registro_final+"'" : "")
					+ (detalhamento!=null ? " AND A.observacao LIKE '%"+detalhamento+"%'" : "")
					+ " ORDER BY A.data_registro DESC"
//					+ codigo_docproc!=null ? " AND REPLACE(REPLACE(REPLACE(A.codigo_docproc, '.', ''), '-', ''), '/', '') LIKE \'%"+codigo_docproc+"%\'" : ""
			);	
			
			LogUtils.debug("::::::\n"
					+ " SELECT TOP "+nrRegistros
					+ " A.controle AS nr_documento_externo,"
					+ "		   	A.codigo_g_requerente AS cd_solicitante,"
					+ "		   	A.ano AS nr_ano,"
					+ "		   	A.data_registro AS dt_protocolo,"
					+ "			A.codigo_tdc AS cd_tipo_documento,"
					+ "			A.codigo_ass AS cd_assunto,"
					+ "			A.codigo_local_origem AS cd_setor,"
					+ "			A.observacao AS txt_observacao,"
					+ "			A.codigo_docproc AS nr_documento,"
					+ "			C.nome_g AS nm_solicitante, "
					+ "			D.nome AS nm_tipo_documento,"
					+ "			E.nome_local AS nm_setor"
					+ "	FROM pr_protocolo A"
					+ " LEFT OUTER JOIN gg_geral C ON (A.codigo_g_requerente = C.codigo_g)"
					+ " LEFT OUTER JOIN pr_tiposdocs D ON (A.codigo_tdc = D.codigo_tdc)"
					+ " LEFT OUTER JOIN gg_local E ON (A.codigo_local_origem = E.codigo_local)"
					+ " WHERE A.codigo_emp='001' AND A.codigo_fil='001'"
					+ (codigo_docproc!=null ? " AND A.codigo_docproc LIKE '%"+codigo_docproc+"'" : "")
					+ (codigo_tdc!=null ? " AND A.codigo_tdc='"+codigo_tdc+"'" : "")
					+ (codigo_local_origem!=null ? " AND A.codigo_local_origem='"+codigo_local_origem+"'" : "")
					+ (ano!=null ? " AND A.ano='"+ano+"'" : "")
					+ (data_registro_inicial!=null ? " AND A.data_registro>='"+data_registro_inicial+"'" : "")
					+ (data_registro_final!=null ? " AND A.data_registro<='"+data_registro_final+"'" : "")
					+ (detalhamento!=null ? " AND A.observacao LIKE '%"+detalhamento+"%'" : "")
					+ " ORDER BY A.data_registro DESC");
			
//			LogUtils.debug("EelUtils.findProtocolo");
//			LogUtils.debug("use CONT_VIT_CONQUISTA_PREFEITURA; "
//					+ " SELECT  A.controle AS nr_documento_externo,"
//					+ "		   	A.codigo_g_requerente AS cd_solicitante,"
//					+ "		   	A.ano AS nr_ano,"
//					+ "		   	A.data_registro AS dt_protocolo,"
//					+ "			A.codigo_tdc AS cd_tipo_documento,"
//					+ "			A.codigo_ass AS cd_assunto,"
//					+ "			A.codigo_local_origem AS cd_setor,"
//					+ "			A.observacao AS txt_observacao,"
//					+ "			A.codigo_docproc AS nr_documento,"
//					+ "			C.nome_g AS nm_solicitante, "
//					+ "			D.nome AS nm_tipo_documento,"
//					+ "			E.nome_local AS nm_setor"
//					+ "	FROM pr_protocolo A"
//					+ " LEFT OUTER JOIN gg_geral C ON (A.codigo_g_requerente = C.codigo_g)"
//					+ " LEFT OUTER JOIN pr_tiposdocs D ON (A.codigo_tdc = D.codigo_tdc)"
//					+ " LEFT OUTER JOIN gg_local E ON (A.codigo_local_origem = E.codigo_local)"
//					+ " WHERE A.codigo_emp='001' AND A.codigo_fil='001'"
//					+ (codigo_docproc!=null ? " AND A.codigo_docproc LIKE '%"+codigo_docproc+"'" : "")
//					+ (codigo_tdc!=null ? " AND A.codigo_tdc='"+codigo_tdc+"'" : "")
//					+ (codigo_local_origem!=null ? " AND A.codigo_local_origem='"+codigo_local_origem+"'" : "")
//					+ (ano!=null ? " AND A.ano='"+ano+"'" : ""));
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()) {
				ResultSet rs = getUltimaTramitacao(rsm.getString("nr_documento_externo"), connect);
				int cdSituacaoDocumento = 0;
				String nmSituacaoDocumento = "";
				
				String nmLocalOrigem = null;
				String nmLocalDestino = null;
				
				if(rs.next()) {
					if(rs.getDate("data_recebimento")==null) {
						cdSituacaoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_TRAMITANDO", 0);
						nmSituacaoDocumento = "TRAMITANDO";
					}
					else {
						cdSituacaoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_EM_ABERTO", 0);
						nmSituacaoDocumento = "RECEBIDO/EM ANÁLISE";
					}
					
					nmLocalOrigem = rs.getString("nm_local_origem");
					nmLocalDestino = rs.getString("nm_local_destino");
				}
				
				rsm.setValueToField("cd_situacao_documento", cdSituacaoDocumento);
				rsm.setValueToField("nm_situacao_documento", nmSituacaoDocumento);
				rsm.setValueToField("nm_local_origem", nmLocalOrigem);
				rsm.setValueToField("nm_local_destino", nmLocalDestino);
				
				String txtObservacao = "";
				if(rsm.getObject("txt_observacao")!=null) {
					Clob clobTxtObservacao = (Clob)rsm.getObject("txt_observacao");
					Reader r = clobTxtObservacao.getCharacterStream();
					
					int c = 0;
					while((c = r.read())!=-1) {
						txtObservacao+=(char)c;
					}
					r.close();
				}
				
				rsm.setValueToField("ds_assunto", txtObservacao);
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.findProtocolo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}

	/**
	 * 
	 * @param cdDocPrinc
	 * @param cdDocSec
	 * @return
	 */
	public static Result vincularDocumentoEel(String cdDocPrinc, String cdDocSec) {
		return vincularDocumentoEel(cdDocPrinc, cdDocSec, null);
	}

	public static Result vincularDocumentoEel(String cdDocPrinc, String cdDocSec, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull) {
			connect = conectarEelSqlServer();
		}
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("INSERT INTO pr_anexaapensa (codigo_emp, codigo_fil, controle_princ, controle_sec, data, tipo_anexa_apensa)"
					+ " VALUES(?, ?, ?, ?, ?, ?)");
			pstmt.setString(1, "001");
			pstmt.setString(2, "001");
			pstmt.setString(3, cdDocPrinc);
			pstmt.setString(4, cdDocSec);
			pstmt.setDate(5, new Date(new GregorianCalendar().getTimeInMillis()));
			pstmt.setString(6, "X");

			Result result = new Result(pstmt.executeUpdate());
			if(result.getCode()>0)
				result.setMessage("Documentos vinculados com sucesso.");
			else 
				result.setMessage("EelUtils.vincularDocumentoEel: Erro ao vincular documentos.");
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.vincularDocumentoEel: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}
	
	
	public static Result save(Documento documento, DocumentoPessoa solicitante, boolean hasNumero) {
		return save(documento, solicitante, hasNumero, null);
	}

	public static Result save(Documento documento, DocumentoPessoa solicitante, boolean hasNumero, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		Connection connExt = conectarEelSqlServer();
		
		if(connect==null) {
			return new Result(-1, "Não existe conexão com o banco de dados.");
		}

		if(connExt==null) {
			return new Result(-2, "Erro ao conectar com o banco de dados da E&L");
		}
		
		PreparedStatement pstmt;
		CallableStatement cstmt = null;
		ResultSet rsUltimaTramitacao = null;
		String nrControle = null;
		String prefixo = "";
		String sufixo = "";
		
		try {
			LogUtils.debug("::::::EelUtils.save");
			LogUtils.debug("::::::Documento: "+documento);
			
			/*
			 * VALIDAÇÕES
			 */
			connExt.setAutoCommit(false);
			
			TipoDocumento tpDocumento = TipoDocumentoDAO.get(documento.getCdTipoDocumento(), connect);
			String cdTipoDocExterno = tpDocumento.getNrExterno();//"0001";
			if(cdTipoDocExterno==null) {
				return new Result(-3, "Tipo de Documento equivalente não encontrado no banco da E&L.");
			}

			//TipoDocumento tpDocProcesso = TipoDocumentoDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_PROCESSO", 0, 0, connect), connect);
			
			Setor setor = SetorDAO.get(documento.getCdSetor(), connect);
			String cdLocalExterno = (setor!=null ? setor.getNrSetorExterno() : null);//"0000062";
			if(cdLocalExterno==null) {
				return new Result(-3, "Setor equivalente não encontrado no banco da E&L.");
			}
			
			int cdRequerente = UsuarioDAO.get(documento.getCdUsuario(), connect).getCdPessoa();
			pstmt = connect.prepareStatement(
					"SELECT * FROM grl_pessoa_externa WHERE cd_pessoa = "+cdRequerente);
			ResultSet rs = pstmt.executeQuery();
			String cdPessoaRequerente = null;
			if(rs.next()) { //já existe a paridade
				cdPessoaRequerente = rs.getString("cd_pessoa_externa");//"0000368";
			}
			else {
				Pessoa p = PessoaDAO.get(cdRequerente, connect);
				PessoaFisica pf = PessoaFisicaDAO.get(cdRequerente, connect);
				pstmt = connExt.prepareStatement("USE [CONT_VIT_CONQUISTA_PREFEITURA];"
						+ " SELECT codigo_g AS cd_pessoa_externa FROM gg_geral"
						+ " WHERE nome_g LIKE '%"+p.getNmPessoa()+"%'"
								+ " OR cpf_g = '"+(pf!=null ? pf.getNrCpf() : "")+"'");
				
				rs = pstmt.executeQuery();
				if(rs.next()) {
					cdPessoaRequerente = rs.getString("cd_pessoa_externa");
					
					if(cdPessoaRequerente!=null) {
						pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_externa(cd_pessoa, cd_pessoa_externa)"
								+ " VALUES (?,?)");
						pstmt.setInt(1, cdRequerente);
						pstmt.setString(2, cdPessoaRequerente);
						
						pstmt.executeUpdate();
					}
				}
			}
			
			if(cdPessoaRequerente==null) {
				cdPessoaRequerente = "0000000"; //NAO LOCALIZADO
				//return new Result(-3, "Usuário de cadastro equivalente não encontrado no banco da E&L.");				
			}
			
			pstmt = connect.prepareStatement(
					"SELECT * FROM grl_pessoa_externa WHERE cd_pessoa = "+solicitante.getCdPessoa());
			rs = pstmt.executeQuery();
			String cdPessoaContato = null;
			if(rs.next()) {
				cdPessoaContato = rs.getString("cd_pessoa_externa");
			}
			else {
				Pessoa p = PessoaDAO.get(solicitante.getCdPessoa(), connect);
				PessoaFisica pf = PessoaFisicaDAO.get(solicitante.getCdPessoa(), connect);
				pstmt = connExt.prepareStatement("USE [CONT_VIT_CONQUISTA_PREFEITURA];"
						+ " SELECT codigo_g AS cd_pessoa_externa FROM gg_geral"
						+ " WHERE 1=1 AND "
						+ " nome_g LIKE '%"+p.getNmPessoa()+"%'"
								+ " OR cpf_g = '"+(pf!=null ? pf.getNrCpf() : "")+"'");
				
				rs = pstmt.executeQuery();
				if(rs.next()) {
					cdPessoaContato = rs.getString("cd_pessoa_externa");
					
					if(cdPessoaContato!=null) {
						pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_externa(cd_pessoa, cd_pessoa_externa)"
								+ " VALUES (?,?)");
						pstmt.setInt(1, solicitante.getCdPessoa());
						pstmt.setString(2, cdPessoaContato);
						
						pstmt.executeUpdate();
					}
				}
			}
			
			if(cdPessoaContato==null) {
				cdPessoaContato = "0000000"; //NAO LOCALIZADO
				//return new Result(-4, "Requerente equivalente não encontrado no banco da E&L.");				
			}
			
			/*
			 * INSERE O DOCUMENTO
			 */
			LogUtils.debug("::::::internoExterno[documento.getTpInternoExterno()]: "+internoExterno[documento.getTpInternoExterno()]);
			cstmt = connExt.prepareCall(//"{call USE [CONT_VIT_CONQUISTA_PREFEITURA]; "
					  "{call dbo.func_wb_grava_protocolo_trb_conquista"
					+ "		@strEmpresa=?, "
					+ "		@strFilial=?,"
					+ " 	@strTipo=?,"
					+ "		@strInternoExterno=?,"
					+ "		@strEspecie=?,"//cd_tipo_documento <--> pr_tiposdocs 
					+ "		@strOrigem=?,"//cd_setor_origem  <--> gg_local
					+ "		@strContato=?,"//cd_pessoa (ptc_documento_pessoa) <--> gg_geral
					+ "		@strRequerente=?,"//cd_pessoa (ptc_documento_pessoa) <--> gg_geral
					+ "		@strAssunto=?,"//documento.nrAssunto <--> pr_assunto
					+ "		@strDetalhamento=?,"
					+ "		@strControle=?; }");
//					+ "GO");
			
			cstmt.setString(1,"001");
			cstmt.setString(2, "001");
			cstmt.setString(3, Integer.toString(documento.getTpDocumentoExterno()));  //'0'=doc '1'=proc
			cstmt.setString(4, internoExterno[documento.getTpInternoExterno()]);
			cstmt.setString(5, cdTipoDocExterno);
			cstmt.setString(6, (documento.getTpInternoExterno()==TP_INTERNO ? cdLocalExterno : cdPessoaContato)); //Interno: gg_local; Externo: gg_geral
			cstmt.setString(7, cdPessoaContato);
			cstmt.setString(8, cdPessoaRequerente);
			cstmt.setString(9, documento.getNrAssunto());
			cstmt.setString(10, (documento.getTxtDocumento()!=null ? documento.getTxtDocumento() : documento.getDsAssunto()));
			
			cstmt.registerOutParameter(11, Types.VARCHAR);
			
			cstmt.execute();
			
			nrControle = cstmt.getString(11);
			
			LogUtils.debug("::::::nrControle: "+nrControle);
			
			/*
			 * ATUALIZAR DADOS DE TRAMITACÃO
			 */
			rsUltimaTramitacao = getUltimaTramitacao(nrControle, connExt);
			if(rsUltimaTramitacao.next()) {
				if(rsUltimaTramitacao.getString("codigo_local_origem").equalsIgnoreCase(cdLocalExterno)) {
					pstmt = connExt.prepareStatement("UPDATE pr_andamento SET codigo_local_origem=?, codigo_local_destino=?, data_recebimento=?, estado=?"
													+ "	WHERE codigo_emp='001' AND codigo_fil='001'"
													+ " AND sequencia_tramite=1"
													+ " AND controle=?");
					pstmt.setString(1, cdLocalExterno);
					pstmt.setString(2, cdLocalExterno);
					pstmt.setDate(3, new Date(new GregorianCalendar().getTimeInMillis()));
					pstmt.setString(4, "F");
					pstmt.setString(5, nrControle);
					
					if(pstmt.executeUpdate()<0) {
						connExt.rollback();
						return new Result(-4, "Erro ao gravar documento no banco E&L.");
					}
				}
			}
			
			/*
			 * ATUALIZAR NUMERO DO DOCUMENTO
			 */
			pstmt = null;
			String nrDocumento = documento.getNrDocumento();
			LogUtils.debug("::::::hasNumero: "+hasNumero);
			if(hasNumero) {
				int index = nrDocumento.indexOf("/");
				prefixo = nrDocumento;
				if(index>=0) {
					prefixo = new String(nrDocumento).substring(0, index);
					sufixo = new String(nrDocumento).substring(index);
				}
				
				//prefixo = new String(String.format ("%015d", Integer.parseInt(prefixo)));
				
				pstmt = connExt.prepareStatement("UPDATE pr_protocolo SET codigo_docproc=?"
											   + " WHERE controle=?");
				pstmt.setString(1, prefixo);
				pstmt.setString(2, nrControle);
				if(pstmt.executeUpdate()<0) {
					connExt.rollback();
					return new Result(-5, "Erro ao gravar documento no banco E&L.");
				}
			}
			else {
				pstmt = connExt.prepareStatement("SELECT codigo_docproc, ano"
						+ " FROM pr_protocolo"
						+ " WHERE controle=?");
				pstmt.setString(1, nrControle);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					documento.setNrDocumento(rs.getString("codigo_docproc")+"/"+rs.getString("ano"));
					documento.setNrProtocoloExterno(rs.getString("codigo_docproc")+"/"+rs.getString("ano"));
					
					LogUtils.debug("rs.getString('ano'): "+rs.getString("ano"));
					
					if(DocumentoDAO.update(documento, connect)<=0) {
						connect.rollback();
						return new Result(-1, "Erro ao atualizar número do documento");
					}
				}
			}
			
			LogUtils.debug("::::::nrControle: "+nrControle);
			LogUtils.debug("::::::prefixo+sufixo: "+prefixo+sufixo);
			LogUtils.debug("::::::documento.getNrDocumento(): "+documento.getNrDocumento());
			
			Result result = new Result(1, "Documento salvo com sucesso no banco da E&L!", "NR_DOCUMENTO_EXTERNO", nrControle);
			result.addObject("NR_PROTOCOLO_EXTERNO", documento.getNrDocumento());
			
			connExt.commit();
			if(isConnectionNull)
				connect.commit();
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.save: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
			desconectarEelSqlServer(connExt);
		}
	}
	
	public static Result deleteUltimoAndamento(String controle) {
		return deleteUltimoAndamento(controle, null);
	}

	public static Result deleteUltimoAndamento(String controle, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = conectarEelSqlServer();
		try {
			
			ResultSet rsUltimoAndamento = getUltimaTramitacao(controle, connect);
			String sequenciaTramite = null;
			if(rsUltimoAndamento.next())
				sequenciaTramite = rsUltimoAndamento.getString("sequencia_tramite");
			
			PreparedStatement pstmt = connect.prepareStatement(
					"DELETE FROM pr_andamento "
					+ " WHERE controle=?"
					+ " AND sequencia_tramite=?");
			pstmt.setString(1, controle);
			pstmt.setString(2, sequenciaTramite);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<0) {
				return new Result(-2, "EelUtils.deleteUltimoAndamento: Erro ao excluir.");
			}
			
			return new Result(1, "Excluído da E&L.");
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("EelUtils.deleteUltimoAndamento: Erro ao excluír registro " + sqlExpt);
			return new Result(-1, "EelUtils.deleteUltimoAndamento: Erro ao excluír registro");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EelUtils.deleteUltimoAndamento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				desconectarEelSqlServer(connect);
		}
	}
}
