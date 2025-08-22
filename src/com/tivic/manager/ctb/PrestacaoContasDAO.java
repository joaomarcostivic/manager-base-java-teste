package com.tivic.manager.ctb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class PrestacaoContasDAO{

	public static int insert(PrestacaoContas objeto) {
		return insert(objeto, null);
	}

	public static int insert(PrestacaoContas objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ctb_prestacao_contas", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPrestacaoContas(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_prestacao_contas (cd_prestacao_contas,"+
			                                  "cd_prestacao_contas_consolidada,"+
			                                  "vl_saldo_anterior,"+
			                                  "vl_recebido,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "cd_unidade_executora,"+
			                                  "cd_instituicao,"+
			                                  "cd_exercicio,"+
			                                  "vl_devolvido,"+
			                                  "vl_rendimento,"+
			                                  "vl_recurso_proprio,"+
			                                  "vl_total_receita,"+
			                                  "vl_total_despesa,"+
			                                  "tp_destinacao_saldo,"+
			                                  "dt_prestacao_contas,"+
			                                  "st_prestacao_contas,"+
			                                  "cd_responsavel,"+
			                                  "cd_supervisor,"+
			                                  "cd_programa,"+
			                                  "txt_parecer) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPrestacaoContasConsolidada()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPrestacaoContasConsolidada());
			pstmt.setDouble(3,objeto.getVlSaldoAnterior());
			pstmt.setDouble(4,objeto.getVlRecebido());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			if(objeto.getCdUnidadeExecutora()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdUnidadeExecutora());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdInstituicao());
			if(objeto.getCdExercicio()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdExercicio());
			pstmt.setDouble(10,objeto.getVlDevolvido());
			pstmt.setDouble(11,objeto.getVlRendimento());
			pstmt.setDouble(12,objeto.getVlRecursoProprio());
			pstmt.setDouble(13,objeto.getVlTotalReceita());
			pstmt.setDouble(14,objeto.getVlTotalDespesa());
			pstmt.setInt(15,objeto.getTpDestinacaoSaldo());
			if(objeto.getDtPrestacaoContas()==null)
				pstmt.setNull(16, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(16,new Timestamp(objeto.getDtPrestacaoContas().getTimeInMillis()));
			pstmt.setInt(17,objeto.getStPrestacaoContas());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdResponsavel());
			if(objeto.getCdSupervisor()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdSupervisor());
			if(objeto.getCdPrograma()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdPrograma());
			pstmt.setString(21,objeto.getTxtParecer());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PrestacaoContasDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PrestacaoContasDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PrestacaoContas objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PrestacaoContas objeto, int cdPrestacaoContasOld) {
		return update(objeto, cdPrestacaoContasOld, null);
	}

	public static int update(PrestacaoContas objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PrestacaoContas objeto, int cdPrestacaoContasOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_prestacao_contas SET cd_prestacao_contas=?,"+
												      		   "cd_prestacao_contas_consolidada=?,"+
												      		   "vl_saldo_anterior=?,"+
												      		   "vl_recebido=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=?,"+
												      		   "cd_unidade_executora=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_exercicio=?,"+
												      		   "vl_devolvido=?,"+
												      		   "vl_rendimento=?,"+
												      		   "vl_recurso_proprio=?,"+
												      		   "vl_total_receita=?,"+
												      		   "vl_total_despesa=?,"+
												      		   "tp_destinacao_saldo=?,"+
												      		   "dt_prestacao_contas=?,"+
												      		   "st_prestacao_contas=?,"+
												      		   "cd_responsavel=?,"+
												      		   "cd_supervisor=?,"+
												      		   "cd_programa=?,"+
												      		   "txt_parecer=? WHERE cd_prestacao_contas=?");
			pstmt.setInt(1,objeto.getCdPrestacaoContas());
			if(objeto.getCdPrestacaoContasConsolidada()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPrestacaoContasConsolidada());
			pstmt.setDouble(3,objeto.getVlSaldoAnterior());
			pstmt.setDouble(4,objeto.getVlRecebido());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			if(objeto.getCdUnidadeExecutora()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdUnidadeExecutora());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdInstituicao());
			if(objeto.getCdExercicio()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdExercicio());
			pstmt.setDouble(10,objeto.getVlDevolvido());
			pstmt.setDouble(11,objeto.getVlRendimento());
			pstmt.setDouble(12,objeto.getVlRecursoProprio());
			pstmt.setDouble(13,objeto.getVlTotalReceita());
			pstmt.setDouble(14,objeto.getVlTotalDespesa());
			pstmt.setInt(15,objeto.getTpDestinacaoSaldo());
			if(objeto.getDtPrestacaoContas()==null)
				pstmt.setNull(16, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(16,new Timestamp(objeto.getDtPrestacaoContas().getTimeInMillis()));
			pstmt.setInt(17,objeto.getStPrestacaoContas());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdResponsavel());
			if(objeto.getCdSupervisor()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdSupervisor());
			if(objeto.getCdPrograma()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdPrograma());
			pstmt.setString(21,objeto.getTxtParecer());
			pstmt.setInt(22, cdPrestacaoContasOld!=0 ? cdPrestacaoContasOld : objeto.getCdPrestacaoContas());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PrestacaoContasDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PrestacaoContasDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPrestacaoContas) {
		return delete(cdPrestacaoContas, null);
	}

	public static int delete(int cdPrestacaoContas, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_prestacao_contas WHERE cd_prestacao_contas=?");
			pstmt.setInt(1, cdPrestacaoContas);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PrestacaoContasDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PrestacaoContasDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PrestacaoContas get(int cdPrestacaoContas) {
		return get(cdPrestacaoContas, null);
	}

	public static PrestacaoContas get(int cdPrestacaoContas, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_prestacao_contas WHERE cd_prestacao_contas=?");
			pstmt.setInt(1, cdPrestacaoContas);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PrestacaoContas(rs.getInt("cd_prestacao_contas"),
						rs.getInt("cd_prestacao_contas_consolidada"),
						rs.getDouble("vl_saldo_anterior"),
						rs.getDouble("vl_recebido"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getInt("cd_unidade_executora"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_exercicio"),
						rs.getDouble("vl_devolvido"),
						rs.getDouble("vl_rendimento"),
						rs.getDouble("vl_recurso_proprio"),
						rs.getDouble("vl_total_receita"),
						rs.getDouble("vl_total_despesa"),
						rs.getInt("tp_destinacao_saldo"),
						(rs.getTimestamp("dt_prestacao_contas")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_prestacao_contas").getTime()),
						rs.getInt("st_prestacao_contas"),
						rs.getInt("cd_responsavel"),
						rs.getInt("cd_supervisor"),
						rs.getInt("cd_programa"),
						rs.getString("txt_parecer"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PrestacaoContasDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PrestacaoContasDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_prestacao_contas");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PrestacaoContasDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PrestacaoContasDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PrestacaoContas> getList() {
		return getList(null);
	}

	public static ArrayList<PrestacaoContas> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PrestacaoContas> list = new ArrayList<PrestacaoContas>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PrestacaoContas obj = PrestacaoContasDAO.get(rsm.getInt("cd_prestacao_contas"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PrestacaoContasDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ctb_prestacao_contas", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
