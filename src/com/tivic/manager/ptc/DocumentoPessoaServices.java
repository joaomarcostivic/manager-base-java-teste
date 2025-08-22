package com.tivic.manager.ptc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.json.JSONArray;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class DocumentoPessoaServices {
	public static Result save(DocumentoPessoa pessoa){
		return save(pessoa, null);
	}
	
	public static Result save(DocumentoPessoa pessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(pessoa==null)
				return new Result(-2, "Erro ao salvar. Pessoa é nula");
			
			int retorno = 0;
			
			// Verifica se já existe pessoa no documento
			DocumentoPessoa p = DocumentoPessoaDAO.get(pessoa.getCdDocumento(), pessoa.getCdPessoa(), connect);
			if(p==null) {
				retorno = DocumentoPessoaDAO.insert(pessoa, connect);
			}
			else {
				retorno = DocumentoPessoaDAO.update(pessoa, connect);
			}
			
//			ResultSet rs = connect.prepareStatement("SELECT A.* FROM ptc_documento_pessoa A " +
//					                                "WHERE A.cd_documento = " +pessoa.getCdDocumento()+
//					   								"  AND A.cd_pessoa ="+pessoa.getCdPessoa()).executeQuery();
//			if(rs.next())
//				return new Result(-3, "Já existe esta pessoa no documento.");
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PESSOA", pessoa);
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
	
	public static Result remove(int cdDocumento, int cdPessoa){
		return remove(cdDocumento, cdPessoa, false, null);
	}
	
	public static Result remove(int cdDocumento, int cdPessoa, boolean cascade){
		return remove(cdDocumento, cdPessoa, cascade, null);
	}
	
	public static Result remove(int cdDocumento, int cdPessoa, boolean cascade, Connection connect){
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
				retorno = DocumentoPessoaDAO.delete(cdDocumento, cdPessoa, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este pessoa está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Pessoa excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir pessoa!");
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
		return DocumentoPessoaDAO.find(criterios, connect);
		//return Search.find("SELECT * FROM PRC_JUIZO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}


	public static ResultSetMap getAllByDocumento(int cdDocumento) {
		Connection connect  = Conexao.conectar();
		try {
			String sql =
					"SELECT A.*, B.nm_pessoa FROM ptc_documento_pessoa A, grl_pessoa B " +
                            "WHERE A.cd_pessoa = B.cd_pessoa " +
                            "  AND A.cd_documento = " + cdDocumento;
				return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	/**
	 * Metódo busca todas as informacoes da partes ligadas ao documento
	 * @param cdDocumento
	 * @return
	 */
	public static ResultSetMap getAllByDocumentoCompleto(int cdDocumento) {
		return getAllByDocumentoCompleto(cdDocumento, 0, null);
	}
	
	public static ResultSetMap getAllByDocumentoCompleto(int cdDocumento, int cdTipoCorrespodencia) {
		return getAllByDocumentoCompleto(cdDocumento, cdTipoCorrespodencia, null);
	}
	
	public static ResultSetMap getAllByDocumentoCompleto(int cdDocumento, Connection connect) {
		return getAllByDocumentoCompleto(cdDocumento, 0, connect);
	}
	
	public static ResultSetMap getAllByDocumentoCompleto(int cdDocumento, int cdTipoCorrespondencia, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			String sql = " SELECT A.*, B.cd_pessoa, B.nm_pessoa, B.gn_pessoa, B.nr_telefone1, C.nr_cpf, C.nr_rg, D.nr_cnpj, P.*, " +
						 " P.nm_logradouro AS nm_logradouro_nodne, P.nm_complemento AS nm_complemento_nodne, P.nm_bairro AS nm_bairro_nodne, P.nr_cep AS nr_cep_nodne, P.nr_endereco as nr_endereco_nodne, " +
						 " P1.nm_logradouro AS nm_logradouro_principal, P1.nm_complemento AS nm_complemento_principal, P1.nm_bairro AS nm_bairro_principal, P1.nr_cep AS nr_cep_principal, P1.nr_endereco as nr_endereco_principal, " +
						 " C1.nm_cidade AS nm_cidade_nodne, " +
						 " C2.nm_cidade AS nm_cidade_principal, " +
						 " ED.sg_estado AS sg_estado_nodne, ED.nm_estado as nm_estado_nodne, " +
						 " EP.sg_estado AS sg_estado_principal, EP.nm_estado as nm_estado_principal, " +
						 " E3.*, E4.*, E5.NM_CIDADE, E6.*, E7.*, E9.*, E10.*, E11.* " +
						 " FROM ptc_documento_pessoa A" +
						 " LEFT OUTER JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa)" +
						 " LEFT OUTER JOIN grl_pessoa_fisica C ON (A.cd_pessoa = C.cd_pessoa)" +
						 " LEFT OUTER JOIN grl_pessoa_juridica D ON (A.cd_pessoa = D.cd_pessoa)" +
						 " LEFT OUTER JOIN grl_pessoa_endereco P ON (A.cd_pessoa = P.cd_pessoa AND "+(cdTipoCorrespondencia != 0 ? (cdTipoCorrespondencia <= 0 ? "P.lg_principal = 1" : "P.cd_tipo_endereco = " + cdTipoCorrespondencia) : "P.lg_principal = 1")+") " +
						 " LEFT OUTER JOIN grl_pessoa_endereco P1 ON (A.cd_pessoa = P1.cd_pessoa AND P1.lg_principal = 1 ) " +
						 " LEFT OUTER JOIN grl_logradouro E3 ON (P.cd_logradouro = E3.cd_logradouro)" +
						 " LEFT OUTER JOIN grl_tipo_logradouro E4 ON (E3.cd_tipo_logradouro = E4.cd_tipo_logradouro)" +
						 " LEFT OUTER JOIN grl_cidade E5 ON (P.cd_cidade = E5.cd_cidade)" +
						 " LEFT OUTER JOIN grl_cidade C1 ON (P.cd_cidade = C1.cd_cidade) " +
						 " LEFT OUTER JOIN grl_cidade C2 ON (P1.cd_cidade = C2.cd_cidade) " +
						 " LEFT OUTER JOIN grl_distrito E6 ON (E3.cd_distrito = E6.cd_distrito" +
						 "                                     AND E5.cd_cidade = E6.cd_cidade)" +
						 " LEFT OUTER JOIN grl_estado E7 ON (E5.cd_estado = E7.cd_estado)" +
						 " LEFT OUTER JOIN grl_estado ED ON (C1.cd_estado = ED.cd_estado) " +
						 " LEFT OUTER JOIN grl_estado EP ON (C2.cd_estado = EP.cd_estado) " +
						 " LEFT OUTER JOIN grl_bairro E8 ON (E5.cd_cidade = E8.cd_cidade" +
						 "                                   AND E6.cd_distrito = E8.cd_distrito)" +
						 " LEFT OUTER JOIN grl_logradouro_cep E9 ON (E3.cd_logradouro = E9.cd_logradouro)" +
						 " LEFT OUTER JOIN grl_logradouro_bairro E10 ON (E3.cd_logradouro = E10.cd_logradouro)" +
						 " LEFT OUTER JOIN grl_tipo_endereco E11 ON (P.cd_tipo_endereco = E11.cd_tipo_endereco)" +
						 
                         " WHERE A.cd_pessoa = B.cd_pessoa" +
                         "  AND A.cd_documento = " + cdDocumento;
			
//			LogUtils.log("SQL: \n" + sql, LogUtils.VERBOSE_LEVEL_INFO);
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
			
			while(rsm.next()){
				if(rsm.getString("NM_LOGRADOURO") == null || rsm.getString("NM_LOGRADOURO").equals("")){
					rsm.setValueToField("NM_LOGRADOURO", rsm.getString("NM_LOGRADOURO_NODNE") != null && !rsm.getString("NM_LOGRADOURO_NODNE").equals("") ? rsm.getString("NM_LOGRADOURO_NODNE") : rsm.getString("NM_LOGRADOURO_PRINCIPAL"));
				}
				if(rsm.getString("NM_COMPLEMENTO") == null || rsm.getString("NM_COMPLEMENTO").equals("")){
					rsm.setValueToField("NM_COMPLEMENTO", rsm.getString("NM_COMPLEMENTO_NODNE") != null && !rsm.getString("NM_COMPLEMENTO_NODNE").equals("") ? rsm.getString("NM_COMPLEMENTO_NODNE") : rsm.getString("NM_COMPLEMENTO_PRINCIPAL"));
				}
				if(rsm.getString("NM_BAIRRO") == null || rsm.getString("NM_BAIRRO").equals("")){
					rsm.setValueToField("NM_BAIRRO", rsm.getString("NM_BAIRRO_NODNE") != null && !rsm.getString("NM_BAIRRO_NODNE").equals("") ? rsm.getString("NM_BAIRRO_NODNE") : rsm.getString("NM_BAIRRO_PRINCIPAL"));
				}
				if(rsm.getString("NR_CEP") == null || rsm.getString("NR_CEP").equals("")){
					rsm.setValueToField("NR_CEP", rsm.getString("NR_CEP_NODNE") != null && !rsm.getString("NR_CEP_NODNE").equals("") ? rsm.getString("NR_CEP_NODNE") : rsm.getString("NR_CEP_PRINCIPAL"));
				}
				if(rsm.getString("NM_CIDADE") == null || rsm.getString("NM_CIDADE").equals("")){
					rsm.setValueToField("NM_CIDADE", rsm.getString("NM_CIDADE_NODNE") != null && !rsm.getString("NM_CIDADE_NODNE").equals("") ? rsm.getString("NM_CIDADE_NODNE") : rsm.getString("NM_CIDADE_PRINCIPAL"));
				}
				if(rsm.getString("SG_ESTADO") == null || rsm.getString("SG_ESTADO").equals("")){
					rsm.setValueToField("SG_ESTADO", rsm.getString("SG_ESTADO_NODNE") != null && !rsm.getString("SG_ESTADO_NODNE").equals("") ? rsm.getString("SG_ESTADO_NODNE") : rsm.getString("SG_ESTADO_PRINCIPAL"));
				}
				if(rsm.getString("NM_ESTADO") == null || rsm.getString("NM_ESTADO").equals("")){
					rsm.setValueToField("NM_ESTADO", rsm.getString("NM_ESTADO_NODNE") != null && !rsm.getString("NM_ESTADO_NODNE").equals("") ? rsm.getString("NM_ESTADO_NODNE") : rsm.getString("NM_ESTADO_PRINCIPAL"));
				}
				if(rsm.getString("NR_ENDERECO") == null || rsm.getString("NR_ENDERECO").equals("")){
					rsm.setValueToField("NR_ENDERECO", rsm.getString("NR_ENDERECO_NODNE") != null && !rsm.getString("NR_ENDERECO_NODNE").equals("") ? rsm.getString("NR_ENDERECO_NODNE") : rsm.getString("NR_ENDERECO_PRINCIPAL"));
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
	
	
	/**
	 * Metódo busca as pessoas mais citadas (por data inicial e final, documento superior e qualificação)
	 * @param cdMes, cdDocumentoSuperior, nmQualificacao
	 * @return
	 */
	
	public static ResultSetMap getRanking(int cdTipoDocumento, String nmQualificacao, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int nrRegistros) {
		return getRanking(cdTipoDocumento, nmQualificacao, dtInicial, dtFinal, nrRegistros, null);
	}
	
	public static ResultSetMap getRanking(int cdTipoDocumento, String nmQualificacao, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int nrRegistros, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			if(dtInicial == null && dtFinal == null) {
				// Cálculo de primeiro e ultimo dia do mês corrente
				GregorianCalendar d = new GregorianCalendar();
				dtInicial = new GregorianCalendar();
				dtFinal = new GregorianCalendar();
				
				dtInicial.set(d.get(Calendar.YEAR), d.get(Calendar.MONTH), 1);			
				dtFinal.set(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.getMaximum(Calendar.MONTH));		
			
			} else {
				
				dtInicial.set(dtInicial.get(Calendar.YEAR), dtInicial.get(Calendar.MONTH), dtInicial.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
				dtFinal.set(dtFinal.get(Calendar.YEAR), dtFinal.get(Calendar.MONTH), dtFinal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
			}
			
			String sql = new String();			
			if(!nmQualificacao.equals("Atendente")){
				sql = "SELECT " +
						" count(A.cd_pessoa) as qt_pessoa, A.cd_pessoa, D.nm_pessoa, " +
						" (SELECT COUNT(*) FROM ptc_documento B1 WHERE B1.dt_protocolo >= '" + new Timestamp(dtInicial.getTimeInMillis()) + "' AND B1.dt_protocolo <= '" + new Timestamp(dtFinal.getTimeInMillis()) + "' " + (nrRegistros > 0 ? "LIMIT " + nrRegistros : "") + ") AS TOTAL_DOCUMENTOS " +
						" FROM ptc_documento_pessoa A" +
						" JOIN ptc_documento B ON (B.cd_documento = A.cd_documento)" +
//						" LEFT OUTER JOIN gpn_tipo_documento C ON (B.cd_tipo_documento = C.cd_tipo_documento_superior)" +
						" LEFT OUTER JOIN grl_pessoa D ON (A.cd_pessoa = D.cd_pessoa)" +
						" WHERE B.dt_protocolo >= '" + new Timestamp(dtInicial.getTimeInMillis()) + "' AND B.dt_protocolo <= '" + new Timestamp(dtFinal.getTimeInMillis()) + "'" +
						" AND B.cd_tipo_documento = " + cdTipoDocumento +
						" AND A.nm_qualificacao ILIKE '" + nmQualificacao + "'" +
						" GROUP BY A.cd_pessoa, D.nm_pessoa" + 
						" ORDER BY qt_pessoa DESC " +
						(nrRegistros > 0 ? "LIMIT " + nrRegistros : "");
			} else {
				sql = "SELECT " +
						"  A.cd_usuario, A.nm_login, B.cd_pessoa, B.nm_pessoa, " +
						" (SELECT COUNT(*) FROM ptc_documento WHERE dt_protocolo >= '" + new Timestamp(dtInicial.getTimeInMillis()) + "' AND dt_protocolo <= '" + new Timestamp(dtFinal.getTimeInMillis()) + "' " + (nrRegistros > 0 ? "LIMIT " + nrRegistros : "") + ") AS qt_total_atendimentos, " +
						" (SELECT COUNT(*) FROM ptc_documento WHERE cd_usuario = A.cd_usuario AND dt_protocolo >= '" + new Timestamp(dtInicial.getTimeInMillis()) + "' AND dt_protocolo <= '" + new Timestamp(dtFinal.getTimeInMillis()) + "' " + (nrRegistros > 0 ? "LIMIT " + nrRegistros : "") + ") AS qt_atendimentos " +
						"  FROM seg_usuario A " +
						"  JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa) " +
						" WHERE A.tp_usuario != 0 "+
						" ORDER BY qt_atendimentos DESC " +
						(nrRegistros > 0 ? "LIMIT " + nrRegistros : "");
			}
			
			System.out.println(sql);
			
			ResultSet rs = connect.prepareStatement(sql).executeQuery();
			ResultSetMap rsm = new ResultSetMap();

			while(rs.next()){
				if(nmQualificacao.equals("Atendente")){
					
					if(rs.getInt("QT_ATENDIMENTOS") > 1){
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("CD_USUARIO", rs.getInt("CD_USUARIO"));
						register.put("NM_LOGIN", rs.getString("NM_LOGIN"));
						register.put("CD_PESSOA", rs.getInt("CD_PESSOA"));
						register.put("NM_PESSOA", rs.getString("NM_PESSOA"));
						register.put("QT_TOTAL_ATENDIMENTOS", rs.getInt("QT_TOTAL_ATENDIMENTOS"));
						register.put("QT_ATENDIMENTOS", rs.getInt("QT_ATENDIMENTOS"));
						rsm.addRegister(register);
					}
					
				} else {
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("QT_PESSOA", rs.getInt("QT_PESSOA"));
					register.put("CD_PESSOA", rs.getString("CD_PESSOA"));
					register.put("NM_PESSOA", rs.getString("NM_PESSOA"));
					register.put("TOTAL_DOCUMENTOS", rs.getString("TOTAL_DOCUMENTOS"));
					rsm.addRegister(register);
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
	
	public static String getRankingJSON(String tipo, String qualificacao, String periodo){
		try {
			GregorianCalendar dateInicial = Util.convStringToCalendar(periodo);
			GregorianCalendar dateFinal   = Util.getUltimoDiaMes(dateInicial.get(GregorianCalendar.MONTH), dateInicial.get(GregorianCalendar.YEAR));
			ResultSetMap rsm = getRanking(5, qualificacao, dateInicial, dateFinal, 10);
			ArrayList<HashMap<String, Object>> ranking = new ArrayList<HashMap<String, Object>>();
	
			JSONArray JSONArray = new JSONArray();
			while(rsm.next()){
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("QT_RECLAMACOES_PERIODO", rsm.getString("TOTAL_DOCUMENTOS"));
				register.put("RECLAMADO", rsm.getString("NM_PESSOA"));
				register.put("QT_RECLAMANTES", rsm.getString("QT_PESSOA"));
				ranking.add(register);
			}
			JSONArray.put(ranking);
			
			
			return JSONArray.toString(1);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}	
	}
	
	public static int updateRelatorJari(DocumentoPessoa relator) {
		return updateRelatorJari(relator, null);
	}
	
	public static int updateRelatorJari(DocumentoPessoa relator, Connection conn) {
		boolean isConnNull = (conn == null);
		
		if(isConnNull)
			conn = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement("UPDATE PTC_DOCUMENTO_PESSOA SET cd_pessoa = ? WHERE cd_documento = ? AND nm_qualificacao = ?");
			pstmt.setInt(1, relator.getCdPessoa());
			pstmt.setInt(2, relator.getCdDocumento());
			pstmt.setString(3, "RELATOR JARI");
			
			int res = pstmt.executeUpdate();
			
			if(res <= 0) {
				pstmt = conn.prepareStatement("INSERT INTO PTC_DOCUMENTO_PESSOA (cd_pessoa, cd_documento, nm_qualificacao) VALUES (?, ?, ?)");
				pstmt.setInt(1, relator.getCdPessoa());
				pstmt.setInt(2, relator.getCdDocumento());
				pstmt.setString(3, "RELATOR JARI");
				
				res = pstmt.executeUpdate();
			}
			
			return res;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return -1;
		} finally {
			if(isConnNull)
				Conexao.desconectar(conn);
		}
	}
}
