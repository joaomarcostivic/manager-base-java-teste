package com.tivic.manager.ord;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class OrdemServicoItemDefeitoDAO{

	public static int insert(OrdemServicoItemDefeito objeto) {
		return insert(objeto, null);
	}

	public static int insert(OrdemServicoItemDefeito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ord_ordem_servico_item_defeito (cd_ordem_servico_item,"+
			                                  "cd_defeito,"+
			                                  "cd_ordem_servico,"+
			                                  "nr_horas_previsao_reparo,"+
			                                  "dt_analise,"+
			                                  "txt_observacao,"+
			                                  "lg_relato_cliente) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdOrdemServicoItem()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdOrdemServicoItem());
			if(objeto.getCdDefeito()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDefeito());
			if(objeto.getCdOrdemServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOrdemServico());
			pstmt.setInt(4,objeto.getNrHorasPrevisaoReparo());
			if(objeto.getDtAnalise()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtAnalise().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.setInt(7,objeto.getLgRelatoCliente());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDefeitoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDefeitoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OrdemServicoItemDefeito objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(OrdemServicoItemDefeito objeto, int cdOrdemServicoItemOld, int cdDefeitoOld, int cdOrdemServicoOld) {
		return update(objeto, cdOrdemServicoItemOld, cdDefeitoOld, cdOrdemServicoOld, null);
	}

	public static int update(OrdemServicoItemDefeito objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(OrdemServicoItemDefeito objeto, int cdOrdemServicoItemOld, int cdDefeitoOld, int cdOrdemServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ord_ordem_servico_item_defeito SET cd_ordem_servico_item=?,"+
												      		   "cd_defeito=?,"+
												      		   "cd_ordem_servico=?,"+
												      		   "nr_horas_previsao_reparo=?,"+
												      		   "dt_analise=?,"+
												      		   "txt_observacao=?,"+
												      		   "lg_relato_cliente=? WHERE cd_ordem_servico_item=? AND cd_defeito=? AND cd_ordem_servico=?");
			pstmt.setInt(1,objeto.getCdOrdemServicoItem());
			pstmt.setInt(2,objeto.getCdDefeito());
			pstmt.setInt(3,objeto.getCdOrdemServico());
			pstmt.setInt(4,objeto.getNrHorasPrevisaoReparo());
			if(objeto.getDtAnalise()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtAnalise().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.setInt(7,objeto.getLgRelatoCliente());
			pstmt.setInt(8, cdOrdemServicoItemOld!=0 ? cdOrdemServicoItemOld : objeto.getCdOrdemServicoItem());
			pstmt.setInt(9, cdDefeitoOld!=0 ? cdDefeitoOld : objeto.getCdDefeito());
			pstmt.setInt(10, cdOrdemServicoOld!=0 ? cdOrdemServicoOld : objeto.getCdOrdemServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDefeitoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDefeitoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrdemServicoItem, int cdDefeito, int cdOrdemServico) {
		return delete(cdOrdemServicoItem, cdDefeito, cdOrdemServico, null);
	}

	public static int delete(int cdOrdemServicoItem, int cdDefeito, int cdOrdemServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ord_ordem_servico_item_defeito WHERE cd_ordem_servico_item=? AND cd_defeito=? AND cd_ordem_servico=?");
			pstmt.setInt(1, cdOrdemServicoItem);
			pstmt.setInt(2, cdDefeito);
			pstmt.setInt(3, cdOrdemServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDefeitoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDefeitoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OrdemServicoItemDefeito get(int cdOrdemServicoItem, int cdDefeito, int cdOrdemServico) {
		return get(cdOrdemServicoItem, cdDefeito, cdOrdemServico, null);
	}

	public static OrdemServicoItemDefeito get(int cdOrdemServicoItem, int cdDefeito, int cdOrdemServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ord_ordem_servico_item_defeito WHERE cd_ordem_servico_item=? AND cd_defeito=? AND cd_ordem_servico=?");
			pstmt.setInt(1, cdOrdemServicoItem);
			pstmt.setInt(2, cdDefeito);
			pstmt.setInt(3, cdOrdemServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OrdemServicoItemDefeito(rs.getInt("cd_ordem_servico_item"),
						rs.getInt("cd_defeito"),
						rs.getInt("cd_ordem_servico"),
						rs.getInt("nr_horas_previsao_reparo"),
						(rs.getTimestamp("dt_analise")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_analise").getTime()),
						rs.getString("txt_observacao"),
						rs.getInt("lg_relato_cliente"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDefeitoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDefeitoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_ordem_servico_item_defeito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDefeitoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDefeitoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OrdemServicoItemDefeito> getList() {
		return getList(null);
	}

	public static ArrayList<OrdemServicoItemDefeito> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OrdemServicoItemDefeito> list = new ArrayList<OrdemServicoItemDefeito>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OrdemServicoItemDefeito obj = OrdemServicoItemDefeitoDAO.get(rsm.getInt("cd_ordem_servico_item"), rsm.getInt("cd_defeito"), rsm.getInt("cd_ordem_servico"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoItemDefeitoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ord_ordem_servico_item_defeito", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
