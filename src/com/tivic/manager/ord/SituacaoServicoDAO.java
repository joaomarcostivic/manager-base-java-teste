package com.tivic.manager.ord;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class SituacaoServicoDAO{

	public static int insert(SituacaoServico objeto) {
		return insert(objeto, null);
	}

	public static int insert(SituacaoServico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ord_situacao_servico", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdSituacaoServico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ord_situacao_servico (cd_situacao_servico,"+
			                                  "nm_situacao_servico,"+
			                                  "txt_situacao_servico,"+
			                                  "nr_ordem,"+
			                                  "lg_altera,"+
			                                  "lg_exclui,"+
			                                  "lg_cancela,"+
			                                  "st_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmSituacaoServico());
			pstmt.setString(3,objeto.getTxtSituacaoServico());
			pstmt.setInt(4,objeto.getNrOrdem());
			pstmt.setInt(5,objeto.getLgAltera());
			pstmt.setInt(6,objeto.getLgExclui());
			pstmt.setInt(7,objeto.getLgCancela());
			pstmt.setInt(8,objeto.getStCadastro());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoServicoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoServicoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(SituacaoServico objeto) {
		return update(objeto, 0, null);
	}

	public static int update(SituacaoServico objeto, int cdSituacaoServicoOld) {
		return update(objeto, cdSituacaoServicoOld, null);
	}

	public static int update(SituacaoServico objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(SituacaoServico objeto, int cdSituacaoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ord_situacao_servico SET cd_situacao_servico=?,"+
												      		   "nm_situacao_servico=?,"+
												      		   "txt_situacao_servico=?,"+
												      		   "nr_ordem=?,"+
												      		   "lg_altera=?,"+
												      		   "lg_exclui=?,"+
												      		   "lg_cancela=?,"+
												      		   "st_cadastro=? WHERE cd_situacao_servico=?");
			pstmt.setInt(1,objeto.getCdSituacaoServico());
			pstmt.setString(2,objeto.getNmSituacaoServico());
			pstmt.setString(3,objeto.getTxtSituacaoServico());
			pstmt.setInt(4,objeto.getNrOrdem());
			pstmt.setInt(5,objeto.getLgAltera());
			pstmt.setInt(6,objeto.getLgExclui());
			pstmt.setInt(7,objeto.getLgCancela());
			pstmt.setInt(8,objeto.getStCadastro());
			pstmt.setInt(9, cdSituacaoServicoOld!=0 ? cdSituacaoServicoOld : objeto.getCdSituacaoServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoServicoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoServicoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdSituacaoServico) {
		return delete(cdSituacaoServico, null);
	}

	public static int delete(int cdSituacaoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ord_situacao_servico WHERE cd_situacao_servico=?");
			pstmt.setInt(1, cdSituacaoServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoServicoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoServicoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static SituacaoServico get(int cdSituacaoServico) {
		return get(cdSituacaoServico, null);
	}

	public static SituacaoServico get(int cdSituacaoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ord_situacao_servico WHERE cd_situacao_servico=?");
			pstmt.setInt(1, cdSituacaoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new SituacaoServico(rs.getInt("cd_situacao_servico"),
						rs.getString("nm_situacao_servico"),
						rs.getString("txt_situacao_servico"),
						rs.getInt("nr_ordem"),
						rs.getInt("lg_altera"),
						rs.getInt("lg_exclui"),
						rs.getInt("lg_cancela"),
						rs.getInt("st_cadastro"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoServicoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoServicoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_situacao_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoServicoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoServicoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<SituacaoServico> getList() {
		return getList(null);
	}

	public static ArrayList<SituacaoServico> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<SituacaoServico> list = new ArrayList<SituacaoServico>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				SituacaoServico obj = SituacaoServicoDAO.get(rsm.getInt("cd_situacao_servico"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoServicoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ord_situacao_servico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}