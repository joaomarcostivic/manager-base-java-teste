package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class NotaFiscalHistoricoDAO{

	public static int insert(NotaFiscalHistorico objeto) {
		return insert(objeto, null);
	}

	public static int insert(NotaFiscalHistorico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			@SuppressWarnings("unchecked")
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_nota_fiscal");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdNotaFiscal()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_historico");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("fsc_nota_fiscal_historico", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdHistorico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_nota_fiscal_historico (cd_nota_fiscal,"+
			                                  "cd_historico,"+
			                                  "tp_ambiente,"+
			                                  "nr_versao,"+
			                                  "dt_envio,"+
			                                  "dt_recebimento,"+
			                                  "nr_chave,"+
			                                  "nr_protocolo,"+
			                                  "nr_digito,"+
			                                  "tp_status,"+
			                                  "ds_mensagem,"+
			                                  "id_transacao,"+
			                                  "txt_xml," +
			                                  "tp_historico) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdNotaFiscal()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdNotaFiscal());
			pstmt.setInt(2, code);
			pstmt.setInt(3,objeto.getTpAmbiente());
			pstmt.setString(4,objeto.getNrVersao());
			if(objeto.getDtEnvio()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtEnvio().getTimeInMillis()));
			if(objeto.getDtRecebimento()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtRecebimento().getTimeInMillis()));
			pstmt.setString(7,objeto.getNrChave());
			pstmt.setString(8,objeto.getNrProtocolo());
			pstmt.setString(9,objeto.getNrDigito());
			pstmt.setString(10,objeto.getTpStatus());
			pstmt.setString(11,objeto.getDsMensagem());
			pstmt.setString(12,objeto.getIdTransacao());
			pstmt.setString(13,objeto.getTxtXml());
			pstmt.setInt(14,objeto.getTpHistorico());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalHistoricoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalHistoricoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(NotaFiscalHistorico objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(NotaFiscalHistorico objeto, int cdNotaFiscalOld, int cdHistoricoOld) {
		return update(objeto, cdNotaFiscalOld, cdHistoricoOld, null);
	}

	public static int update(NotaFiscalHistorico objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(NotaFiscalHistorico objeto, int cdNotaFiscalOld, int cdHistoricoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_nota_fiscal_historico SET cd_nota_fiscal=?,"+
												      		   "cd_historico=?,"+
												      		   "tp_ambiente=?,"+
												      		   "nr_versao=?,"+
												      		   "dt_envio=?,"+
												      		   "dt_recebimento=?,"+
												      		   "nr_chave=?,"+
												      		   "nr_protocolo=?,"+
												      		   "nr_digito=?,"+
												      		   "tp_status=?,"+
												      		   "ds_mensagem=?,"+
												      		   "id_transacao=?,"+
												      		   "txt_xml=?," +
												      		   "tp_historico=? WHERE cd_nota_fiscal=? AND cd_historico=?");
			pstmt.setInt(1,objeto.getCdNotaFiscal());
			pstmt.setInt(2,objeto.getCdHistorico());
			pstmt.setInt(3,objeto.getTpAmbiente());
			pstmt.setString(4,objeto.getNrVersao());
			if(objeto.getDtEnvio()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtEnvio().getTimeInMillis()));
			if(objeto.getDtRecebimento()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtRecebimento().getTimeInMillis()));
			pstmt.setString(7,objeto.getNrChave());
			pstmt.setString(8,objeto.getNrProtocolo());
			pstmt.setString(9,objeto.getNrDigito());
			pstmt.setString(10,objeto.getTpStatus());
			pstmt.setString(11,objeto.getDsMensagem());
			pstmt.setString(12,objeto.getIdTransacao());
			pstmt.setString(13,objeto.getTxtXml());
			pstmt.setInt(14,objeto.getTpHistorico());
			pstmt.setInt(15, cdNotaFiscalOld!=0 ? cdNotaFiscalOld : objeto.getCdNotaFiscal());
			pstmt.setInt(16, cdHistoricoOld!=0 ? cdHistoricoOld : objeto.getCdHistorico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalHistoricoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalHistoricoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdNotaFiscal, int cdHistorico) {
		return delete(cdNotaFiscal, cdHistorico, null);
	}

	public static int delete(int cdNotaFiscal, int cdHistorico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fsc_nota_fiscal_historico WHERE cd_nota_fiscal=? AND cd_historico=?");
			pstmt.setInt(1, cdNotaFiscal);
			pstmt.setInt(2, cdHistorico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalHistoricoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalHistoricoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static NotaFiscalHistorico get(int cdNotaFiscal, int cdHistorico) {
		return get(cdNotaFiscal, cdHistorico, null);
	}

	public static NotaFiscalHistorico get(int cdNotaFiscal, int cdHistorico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_historico WHERE cd_nota_fiscal=? AND cd_historico=?");
			pstmt.setInt(1, cdNotaFiscal);
			pstmt.setInt(2, cdHistorico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NotaFiscalHistorico(rs.getInt("cd_nota_fiscal"),
						rs.getInt("cd_historico"),
						rs.getInt("tp_ambiente"),
						rs.getString("nr_versao"),
						(rs.getTimestamp("dt_envio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_envio").getTime()),
						(rs.getTimestamp("dt_recebimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_recebimento").getTime()),
						rs.getString("nr_chave"),
						rs.getString("nr_protocolo"),
						rs.getString("nr_digito"),
						rs.getString("tp_status"),
						rs.getString("ds_mensagem"),
						rs.getString("id_transacao"),
						rs.getString("txt_xml"),
						rs.getInt("tp_historico"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalHistoricoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalHistoricoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_historico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalHistoricoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalHistoricoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM fsc_nota_fiscal_historico", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
