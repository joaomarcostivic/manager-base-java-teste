package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class MovimentoContaDAO{

	public static int insert(MovimentoConta objeto) {
		return insert(objeto, null);
	}

	public static int insert(MovimentoConta objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_movimento_conta");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_conta");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdConta()));
			int code = Conexao.getSequenceCode("adm_movimento_conta", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMovimentoConta(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_movimento_conta (cd_movimento_conta,"+
			                                  "cd_conta,"+
			                                  "cd_conta_origem,"+
			                                  "cd_movimento_origem,"+
			                                  "cd_usuario,"+
			                                  "cd_cheque,"+
			                                  "cd_viagem,"+
			                                  "dt_movimento,"+
			                                  "vl_movimento,"+
			                                  "nr_documento,"+
			                                  "tp_movimento,"+
			                                  "tp_origem,"+
			                                  "st_movimento,"+
			                                  "ds_historico,"+
			                                  "dt_deposito,"+
			                                  "id_extrato,"+
			                                  "cd_forma_pagamento,"+
			                                  "cd_fechamento,"+
			                                  "cd_turno,"+
			                                  "cd_plano_pagamento,"+
			                                  "nr_doc_conta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdConta()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConta());
			if(objeto.getCdContaOrigem()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaOrigem());
			if(objeto.getCdMovimentoOrigem()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdMovimentoOrigem());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdUsuario());
			if(objeto.getCdCheque()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCheque());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdViagem());
			if(objeto.getDtMovimento()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtMovimento().getTimeInMillis()));
			pstmt.setDouble(9,objeto.getVlMovimento());
			pstmt.setString(10,objeto.getNrDocumento());
			pstmt.setInt(11,objeto.getTpMovimento());
			pstmt.setInt(12,objeto.getTpOrigem());
			pstmt.setInt(13,objeto.getStMovimento());
			pstmt.setString(14,objeto.getDsHistorico());
			if(objeto.getDtDeposito()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtDeposito().getTimeInMillis()));
			pstmt.setString(16,objeto.getIdExtrato());
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdFormaPagamento());
			if(objeto.getCdFechamento()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdFechamento());
			if(objeto.getCdTurno()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdTurno());
			pstmt.setInt(20,objeto.getCdPlanoPagamento());
			pstmt.setString(21,objeto.getNrDocConta());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MovimentoConta objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MovimentoConta objeto, int cdMovimentoContaOld, int cdContaOld) {
		return update(objeto, cdMovimentoContaOld, cdContaOld, null);
	}

	public static int update(MovimentoConta objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MovimentoConta objeto, int cdMovimentoContaOld, int cdContaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_movimento_conta SET cd_movimento_conta=?,"+
												      		   "cd_conta=?,"+
												      		   "cd_conta_origem=?,"+
												      		   "cd_movimento_origem=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_cheque=?,"+
												      		   "cd_viagem=?,"+
												      		   "dt_movimento=?,"+
												      		   "vl_movimento=?,"+
												      		   "nr_documento=?,"+
												      		   "tp_movimento=?,"+
												      		   "tp_origem=?,"+
												      		   "st_movimento=?,"+
												      		   "ds_historico=?,"+
												      		   "dt_deposito=?,"+
												      		   "id_extrato=?,"+
												      		   "cd_forma_pagamento=?,"+
												      		   "cd_fechamento=?,"+
												      		   "cd_turno=?,"+
												      		   "cd_plano_pagamento=?,"+
												      		   "nr_doc_conta=? WHERE cd_movimento_conta=? AND cd_conta=?");
			pstmt.setInt(1,objeto.getCdMovimentoConta());
			pstmt.setInt(2,objeto.getCdConta());
			if(objeto.getCdContaOrigem()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaOrigem());
			if(objeto.getCdMovimentoOrigem()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdMovimentoOrigem());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdUsuario());
			if(objeto.getCdCheque()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCheque());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdViagem());
			if(objeto.getDtMovimento()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtMovimento().getTimeInMillis()));
			pstmt.setDouble(9,objeto.getVlMovimento());
			pstmt.setString(10,objeto.getNrDocumento());
			pstmt.setInt(11,objeto.getTpMovimento());
			pstmt.setInt(12,objeto.getTpOrigem());
			pstmt.setInt(13,objeto.getStMovimento());
			pstmt.setString(14,objeto.getDsHistorico());
			if(objeto.getDtDeposito()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtDeposito().getTimeInMillis()));
			pstmt.setString(16,objeto.getIdExtrato());
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdFormaPagamento());
			if(objeto.getCdFechamento()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdFechamento());
			if(objeto.getCdTurno()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdTurno());
			pstmt.setInt(20,objeto.getCdPlanoPagamento());
			pstmt.setString(21,objeto.getNrDocConta());
			pstmt.setInt(22, cdMovimentoContaOld!=0 ? cdMovimentoContaOld : objeto.getCdMovimentoConta());
			pstmt.setInt(23, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMovimentoConta, int cdConta) {
		return delete(cdMovimentoConta, cdConta, null);
	}

	public static int delete(int cdMovimentoConta, int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_movimento_conta WHERE cd_movimento_conta=? AND cd_conta=?");
			pstmt.setInt(1, cdMovimentoConta);
			pstmt.setInt(2, cdConta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MovimentoConta get(int cdMovimentoConta, int cdConta) {
		return get(cdMovimentoConta, cdConta, null);
	}

	public static MovimentoConta get(int cdMovimentoConta, int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_conta WHERE cd_movimento_conta=? AND cd_conta=?");
			pstmt.setInt(1, cdMovimentoConta);
			pstmt.setInt(2, cdConta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MovimentoConta(rs.getInt("cd_movimento_conta"),
						rs.getInt("cd_conta"),
						rs.getInt("cd_conta_origem"),
						rs.getInt("cd_movimento_origem"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_cheque"),
						rs.getInt("cd_viagem"),
						(rs.getTimestamp("dt_movimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_movimento").getTime()),
						rs.getDouble("vl_movimento"),
						rs.getString("nr_documento"),
						rs.getInt("tp_movimento"),
						rs.getInt("tp_origem"),
						rs.getInt("st_movimento"),
						rs.getString("ds_historico"),
						(rs.getTimestamp("dt_deposito")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_deposito").getTime()),
						rs.getString("id_extrato"),
						rs.getInt("cd_forma_pagamento"),
						rs.getInt("cd_fechamento"),
						rs.getInt("cd_turno"),
						rs.getInt("cd_plano_pagamento"),
						rs.getString("nr_doc_conta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_conta");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MovimentoConta> getList() {
		return getList(null);
	}

	public static ArrayList<MovimentoConta> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MovimentoConta> list = new ArrayList<MovimentoConta>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MovimentoConta obj = MovimentoContaDAO.get(rsm.getInt("cd_movimento_conta"), rsm.getInt("cd_conta"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_movimento_conta", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}