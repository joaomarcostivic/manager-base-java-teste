package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class ProcessoFinanceiroDAO{

	public static int insert(ProcessoFinanceiro objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProcessoFinanceiro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "CD_EVENTO_FINANCEIRO");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "CD_PROCESSO");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdProcesso()));
			int code = Conexao.getSequenceCode("PRC_PROCESSO_FINANCEIRO", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEventoFinanceiro(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_PROCESSO_FINANCEIRO (CD_PROCESSO,"+
			                                  "CD_EVENTO_FINANCEIRO,"+
			                                  "CD_PRODUTO_SERVICO,"+
			                                  "CD_ANDAMENTO,"+
			                                  "CD_PESSOA,"+
			                                  "TP_NATUREZA_EVENTO,"+
			                                  "DT_EVENTO_FINANCEIRO,"+
			                                  "VL_EVENTO_FINANCEIRO,"+
			                                  "DT_LANCAMENTO,"+
			                                  "CD_CONTA_PAGAR,"+
			                                  "CD_CONTA_RECEBER,"+
			                                  "CD_USUARIO,"+
			                                  "CD_ARQUIVO,"+
			                                  "DT_REVISAO,"+
			                                  "CD_USUARIO_REVISAO,"+
			                                  "CD_AGENDA_ITEM,"+
			                                  "CD_REGRA_FATURAMENTO,"+
			                                  "CD_EVENTO_FINANCEIRO_ORIGEM,"+
			                                  "TP_SEGMENTO,"+
			                                  "TP_INSTANCIA,"+
			                                  "CD_ESTADO,"+
			                                  "NR_REFERENCIA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdProcesso());
			pstmt.setInt(2, code);
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProdutoServico());
			if(objeto.getCdAndamento()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdAndamento());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPessoa());
			pstmt.setInt(6,objeto.getTpNaturezaEvento());
			if(objeto.getDtEventoFinanceiro()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtEventoFinanceiro().getTimeInMillis()));
			pstmt.setDouble(8,objeto.getVlEventoFinanceiro());
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdContaPagar());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdContaReceber());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdUsuario());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdArquivo());
			if(objeto.getDtRevisao()==null)
				pstmt.setNull(14, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(14,new Timestamp(objeto.getDtRevisao().getTimeInMillis()));
			pstmt.setInt(15,objeto.getCdUsuarioRevisao());
			if(objeto.getCdAgendaItem()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdAgendaItem());
			if(objeto.getCdRegraFaturamento()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdRegraFaturamento());
			if(objeto.getCdEventoFinanceiroOrigem()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdEventoFinanceiroOrigem());
			pstmt.setInt(19,objeto.getTpSegmento());
			pstmt.setInt(20,objeto.getTpInstancia());
			pstmt.setInt(21,objeto.getCdEstado());
			pstmt.setString(22,objeto.getNrReferencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoFinanceiroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoFinanceiroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProcessoFinanceiro objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ProcessoFinanceiro objeto, int cdEventoFinanceiroOld, int cdProcessoOld) {
		return update(objeto, cdEventoFinanceiroOld, cdProcessoOld, null);
	}

	public static int update(ProcessoFinanceiro objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ProcessoFinanceiro objeto, int cdEventoFinanceiroOld, int cdProcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_PROCESSO_FINANCEIRO SET CD_PROCESSO=?,"+
												      		   "CD_EVENTO_FINANCEIRO=?,"+
												      		   "CD_PRODUTO_SERVICO=?,"+
												      		   "CD_ANDAMENTO=?,"+
												      		   "CD_PESSOA=?,"+
												      		   "TP_NATUREZA_EVENTO=?,"+
												      		   "DT_EVENTO_FINANCEIRO=?,"+
												      		   "VL_EVENTO_FINANCEIRO=?,"+
												      		   "DT_LANCAMENTO=?,"+
												      		   "CD_CONTA_PAGAR=?,"+
												      		   "CD_CONTA_RECEBER=?,"+
												      		   "CD_USUARIO=?,"+
												      		   "CD_ARQUIVO=?,"+
												      		   "DT_REVISAO=?,"+
												      		   "CD_USUARIO_REVISAO=?,"+
												      		   "CD_AGENDA_ITEM=?,"+
												      		   "CD_REGRA_FATURAMENTO=?,"+
												      		   "CD_EVENTO_FINANCEIRO_ORIGEM=?,"+
												      		   "TP_SEGMENTO=?,"+
												      		   "TP_INSTANCIA=?,"+
												      		   "CD_ESTADO=?,"+
												      		   "NR_REFERENCIA=? WHERE CD_EVENTO_FINANCEIRO=? AND CD_PROCESSO=?");
			pstmt.setInt(1,objeto.getCdProcesso());
			pstmt.setInt(2,objeto.getCdEventoFinanceiro());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProdutoServico());
			if(objeto.getCdAndamento()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdAndamento());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPessoa());
			pstmt.setInt(6,objeto.getTpNaturezaEvento());
			if(objeto.getDtEventoFinanceiro()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtEventoFinanceiro().getTimeInMillis()));
			pstmt.setDouble(8,objeto.getVlEventoFinanceiro());
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdContaPagar());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdContaReceber());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdUsuario());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdArquivo());
			if(objeto.getDtRevisao()==null)
				pstmt.setNull(14, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(14,new Timestamp(objeto.getDtRevisao().getTimeInMillis()));
			if(objeto.getCdUsuarioRevisao()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdUsuarioRevisao());
			if(objeto.getCdAgendaItem()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdAgendaItem());
			if(objeto.getCdRegraFaturamento()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdRegraFaturamento());
			if(objeto.getCdEventoFinanceiroOrigem()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdEventoFinanceiroOrigem());
			pstmt.setInt(19,objeto.getTpSegmento());
			pstmt.setInt(20,objeto.getTpInstancia());
			pstmt.setInt(21,objeto.getCdEstado());
			pstmt.setString(22,objeto.getNrReferencia());
			pstmt.setInt(23, cdEventoFinanceiroOld!=0 ? cdEventoFinanceiroOld : objeto.getCdEventoFinanceiro());
			pstmt.setInt(24, cdProcessoOld!=0 ? cdProcessoOld : objeto.getCdProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoFinanceiroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoFinanceiroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEventoFinanceiro, int cdProcesso) {
		return delete(cdEventoFinanceiro, cdProcesso, null);
	}

	public static int delete(int cdEventoFinanceiro, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_PROCESSO_FINANCEIRO WHERE CD_EVENTO_FINANCEIRO=? AND CD_PROCESSO=?");
			pstmt.setInt(1, cdEventoFinanceiro);
			pstmt.setInt(2, cdProcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoFinanceiroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoFinanceiroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProcessoFinanceiro get(int cdEventoFinanceiro, int cdProcesso) {
		return get(cdEventoFinanceiro, cdProcesso, null);
	}

	public static ProcessoFinanceiro get(int cdEventoFinanceiro, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_PROCESSO_FINANCEIRO WHERE CD_EVENTO_FINANCEIRO=? AND CD_PROCESSO=?");
			pstmt.setInt(1, cdEventoFinanceiro);
			pstmt.setInt(2, cdProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProcessoFinanceiro(rs.getInt("CD_PROCESSO"),
						rs.getInt("CD_EVENTO_FINANCEIRO"),
						rs.getInt("CD_PRODUTO_SERVICO"),
						rs.getInt("CD_ANDAMENTO"),
						rs.getInt("CD_PESSOA"),
						rs.getInt("TP_NATUREZA_EVENTO"),
						(rs.getTimestamp("DT_EVENTO_FINANCEIRO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_EVENTO_FINANCEIRO").getTime()),
						rs.getDouble("VL_EVENTO_FINANCEIRO"),
						(rs.getTimestamp("DT_LANCAMENTO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_LANCAMENTO").getTime()),
						rs.getInt("CD_CONTA_PAGAR"),
						rs.getInt("CD_CONTA_RECEBER"),
						rs.getInt("CD_USUARIO"),
						rs.getInt("CD_ARQUIVO"),
						(rs.getTimestamp("DT_REVISAO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_REVISAO").getTime()),
						rs.getInt("CD_USUARIO_REVISAO"),
						rs.getInt("CD_AGENDA_ITEM"),
						rs.getInt("CD_REGRA_FATURAMENTO"),
						rs.getInt("CD_EVENTO_FINANCEIRO_ORIGEM"),
						rs.getInt("TP_SEGMENTO"),
						rs.getInt("TP_INSTANCIA"),
						rs.getInt("CD_ESTADO"),
						rs.getString("NR_REFERENCIA"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoFinanceiroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoFinanceiroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_PROCESSO_FINANCEIRO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoFinanceiroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoFinanceiroDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProcessoFinanceiro> getList() {
		return getList(null);
	}

	public static ArrayList<ProcessoFinanceiro> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProcessoFinanceiro> list = new ArrayList<ProcessoFinanceiro>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProcessoFinanceiro obj = ProcessoFinanceiroDAO.get(rsm.getInt("CD_EVENTO_FINANCEIRO"), rsm.getInt("CD_PROCESSO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoFinanceiroDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_PROCESSO_FINANCEIRO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
