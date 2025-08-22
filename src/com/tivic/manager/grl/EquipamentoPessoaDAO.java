package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class EquipamentoPessoaDAO{

	public static int insert(EquipamentoPessoa objeto) {
		return insert(objeto, null);
	}

	public static int insert(EquipamentoPessoa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_equipamento_pessoa");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_equipamento");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdEquipamento()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_pessoa");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdPessoa()));
			int code = Conexao.getSequenceCode("grl_equipamento_pessoa", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEquipamentoPessoa(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_equipamento_pessoa (cd_equipamento_pessoa,"+
			                                  "cd_equipamento,"+
			                                  "cd_pessoa,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "st_emprestimo,"+
			                                  "txt_observacao,"+
			                                  "cd_agendamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEquipamento());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(6,objeto.getStEmprestimo());
			pstmt.setString(7,objeto.getTxtObservacao());
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdAgendamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoPessoaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoPessoaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EquipamentoPessoa objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(EquipamentoPessoa objeto, int cdEquipamentoPessoaOld, int cdEquipamentoOld, int cdPessoaOld) {
		return update(objeto, cdEquipamentoPessoaOld, cdEquipamentoOld, cdPessoaOld, null);
	}

	public static int update(EquipamentoPessoa objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(EquipamentoPessoa objeto, int cdEquipamentoPessoaOld, int cdEquipamentoOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_equipamento_pessoa SET cd_equipamento_pessoa=?,"+
												      		   "cd_equipamento=?,"+
												      		   "cd_pessoa=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=?,"+
												      		   "st_emprestimo=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_agendamento=? WHERE cd_equipamento_pessoa=? AND cd_equipamento=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdEquipamentoPessoa());
			pstmt.setInt(2,objeto.getCdEquipamento());
			pstmt.setInt(3,objeto.getCdPessoa());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(6,objeto.getStEmprestimo());
			pstmt.setString(7,objeto.getTxtObservacao());
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdAgendamento());
			pstmt.setInt(9, cdEquipamentoPessoaOld!=0 ? cdEquipamentoPessoaOld : objeto.getCdEquipamentoPessoa());
			pstmt.setInt(10, cdEquipamentoOld!=0 ? cdEquipamentoOld : objeto.getCdEquipamento());
			pstmt.setInt(11, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoPessoaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoPessoaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEquipamentoPessoa, int cdEquipamento, int cdPessoa) {
		return delete(cdEquipamentoPessoa, cdEquipamento, cdPessoa, null);
	}

	public static int delete(int cdEquipamentoPessoa, int cdEquipamento, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_equipamento_pessoa WHERE cd_equipamento_pessoa=? AND cd_equipamento=? AND cd_pessoa=?");
			pstmt.setInt(1, cdEquipamentoPessoa);
			pstmt.setInt(2, cdEquipamento);
			pstmt.setInt(3, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoPessoaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoPessoaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EquipamentoPessoa get(int cdEquipamentoPessoa, int cdEquipamento, int cdPessoa) {
		return get(cdEquipamentoPessoa, cdEquipamento, cdPessoa, null);
	}

	public static EquipamentoPessoa get(int cdEquipamentoPessoa, int cdEquipamento, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_equipamento_pessoa WHERE cd_equipamento_pessoa=? AND cd_equipamento=? AND cd_pessoa=?");
			pstmt.setInt(1, cdEquipamentoPessoa);
			pstmt.setInt(2, cdEquipamento);
			pstmt.setInt(3, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EquipamentoPessoa(rs.getInt("cd_equipamento_pessoa"),
						rs.getInt("cd_equipamento"),
						rs.getInt("cd_pessoa"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getInt("st_emprestimo"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_agendamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoPessoaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoPessoaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_equipamento_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoPessoaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoPessoaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EquipamentoPessoa> getList() {
		return getList(null);
	}

	public static ArrayList<EquipamentoPessoa> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EquipamentoPessoa> list = new ArrayList<EquipamentoPessoa>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EquipamentoPessoa obj = EquipamentoPessoaDAO.get(rsm.getInt("cd_equipamento_pessoa"), rsm.getInt("cd_equipamento"), rsm.getInt("cd_pessoa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoPessoaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_equipamento_pessoa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
