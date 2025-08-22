package com.tivic.manager.ptc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ModeloObservacaoDAO{

	public static int insert(ModeloObservacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(ModeloObservacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ptc_modelo_observacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdObservacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_modelo_observacao (cd_observacao,"+
			                                  "cd_tipo_documento,"+
			                                  "nm_observacao,"+
			                                  "txt_observacao) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoDocumento());
			pstmt.setString(3,objeto.getNmObservacao());
			pstmt.setString(4,objeto.getTxtObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloObservacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloObservacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ModeloObservacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ModeloObservacao objeto, int cdObservacaoOld) {
		return update(objeto, cdObservacaoOld, null);
	}

	public static int update(ModeloObservacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ModeloObservacao objeto, int cdObservacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_modelo_observacao SET cd_observacao=?,"+
												      		   "cd_tipo_documento=?,"+
												      		   "nm_observacao=?,"+
												      		   "txt_observacao=? WHERE cd_observacao=?");
			pstmt.setInt(1,objeto.getCdObservacao());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoDocumento());
			pstmt.setString(3,objeto.getNmObservacao());
			pstmt.setString(4,objeto.getTxtObservacao());
			pstmt.setInt(5, cdObservacaoOld!=0 ? cdObservacaoOld : objeto.getCdObservacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloObservacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloObservacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdObservacao) {
		return delete(cdObservacao, null);
	}

	public static int delete(int cdObservacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_modelo_observacao WHERE cd_observacao=?");
			pstmt.setInt(1, cdObservacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloObservacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloObservacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ModeloObservacao get(int cdObservacao) {
		return get(cdObservacao, null);
	}

	public static ModeloObservacao get(int cdObservacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_modelo_observacao WHERE cd_observacao=?");
			pstmt.setInt(1, cdObservacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ModeloObservacao(rs.getInt("cd_observacao"),
						rs.getInt("cd_tipo_documento"),
						rs.getString("nm_observacao"),
						rs.getString("txt_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloObservacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloObservacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_modelo_observacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloObservacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloObservacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ModeloObservacao> getList() {
		return getList(null);
	}

	public static ArrayList<ModeloObservacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ModeloObservacao> list = new ArrayList<ModeloObservacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ModeloObservacao obj = ModeloObservacaoDAO.get(rsm.getInt("cd_observacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloObservacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ptc_modelo_observacao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}