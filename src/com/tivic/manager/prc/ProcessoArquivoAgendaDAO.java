package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ProcessoArquivoAgendaDAO{

	public static int insert(ProcessoArquivoAgenda objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProcessoArquivoAgenda objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_PROCESSO_ARQUIVO_AGENDA (CD_ARQUIVO,"+
			                                  "CD_PROCESSO,"+
			                                  "CD_AGENDA_ITEM) VALUES (?, ?, ?)");
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdArquivo());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProcesso());
			if(objeto.getCdAgendaItem()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAgendaItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoAgendaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoAgendaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProcessoArquivoAgenda objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ProcessoArquivoAgenda objeto, int cdAgendaItemOld, int cdArquivoOld, int cdProcessoOld) {
		return update(objeto, cdAgendaItemOld, cdArquivoOld, cdProcessoOld, null);
	}

	public static int update(ProcessoArquivoAgenda objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ProcessoArquivoAgenda objeto, int cdAgendaItemOld, int cdArquivoOld, int cdProcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_PROCESSO_ARQUIVO_AGENDA SET CD_ARQUIVO=?,"+
												      		   "CD_PROCESSO=?,"+
												      		   "CD_AGENDA_ITEM=? WHERE CD_AGENDA_ITEM=? AND CD_ARQUIVO=? AND CD_PROCESSO=?");
			pstmt.setInt(1,objeto.getCdArquivo());
			pstmt.setInt(2,objeto.getCdProcesso());
			pstmt.setInt(3,objeto.getCdAgendaItem());
			pstmt.setInt(4, cdAgendaItemOld!=0 ? cdAgendaItemOld : objeto.getCdAgendaItem());
			pstmt.setInt(5, cdArquivoOld!=0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.setInt(6, cdProcessoOld!=0 ? cdProcessoOld : objeto.getCdProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoAgendaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoAgendaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAgendaItem, int cdArquivo, int cdProcesso) {
		return delete(cdAgendaItem, cdArquivo, cdProcesso, null);
	}

	public static int delete(int cdAgendaItem, int cdArquivo, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_PROCESSO_ARQUIVO_AGENDA WHERE CD_AGENDA_ITEM=? AND CD_ARQUIVO=? AND CD_PROCESSO=?");
			pstmt.setInt(1, cdAgendaItem);
			pstmt.setInt(2, cdArquivo);
			pstmt.setInt(3, cdProcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoAgendaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoAgendaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProcessoArquivoAgenda get(int cdAgendaItem, int cdArquivo, int cdProcesso) {
		return get(cdAgendaItem, cdArquivo, cdProcesso, null);
	}

	public static ProcessoArquivoAgenda get(int cdAgendaItem, int cdArquivo, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_PROCESSO_ARQUIVO_AGENDA WHERE CD_AGENDA_ITEM=? AND CD_ARQUIVO=? AND CD_PROCESSO=?");
			pstmt.setInt(1, cdAgendaItem);
			pstmt.setInt(2, cdArquivo);
			pstmt.setInt(3, cdProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProcessoArquivoAgenda(rs.getInt("CD_ARQUIVO"),
						rs.getInt("CD_PROCESSO"),
						rs.getInt("CD_AGENDA_ITEM"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoAgendaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoAgendaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_PROCESSO_ARQUIVO_AGENDA");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoAgendaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoAgendaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProcessoArquivoAgenda> getList() {
		return getList(null);
	}

	public static ArrayList<ProcessoArquivoAgenda> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProcessoArquivoAgenda> list = new ArrayList<ProcessoArquivoAgenda>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProcessoArquivoAgenda obj = ProcessoArquivoAgendaDAO.get(rsm.getInt("CD_AGENDA_ITEM"), rsm.getInt("CD_ARQUIVO"), rsm.getInt("CD_PROCESSO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoAgendaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_PROCESSO_ARQUIVO_AGENDA", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
