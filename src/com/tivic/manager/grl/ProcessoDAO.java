package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ProcessoDAO{

	public static int insert(Processo objeto) {
		return insert(objeto, null);
	}

	public static int insert(Processo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_processo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdProcesso(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_processo (cd_processo,"+
			                                  "nm_processo,"+
			                                  "st_processo,"+
			                                  "cd_empresa,"+
			                                  "txt_processo,"+
			                                  "cd_tipo_processo,"+
			                                  "tp_numeracao,"+
			                                  "id_prefixo_numeracao,"+
			                                  "txt_mascara_numeracao,"+
			                                  "nr_ultima_numeracao,"+
			                                  "tp_processo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmProcesso());
			pstmt.setInt(3,objeto.getStProcesso());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			pstmt.setString(5,objeto.getTxtProcesso());
			if(objeto.getCdTipoProcesso()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTipoProcesso());
			pstmt.setInt(7,objeto.getTpNumeracao());
			pstmt.setString(8,objeto.getIdPrefixoNumeracao());
			pstmt.setString(9,objeto.getTxtMascaraNumeracao());
			pstmt.setInt(10,objeto.getNrUltimaNumeracao());
			pstmt.setInt(11,objeto.getTpProcesso());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Processo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Processo objeto, int cdProcessoOld) {
		return update(objeto, cdProcessoOld, null);
	}

	public static int update(Processo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Processo objeto, int cdProcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_processo SET cd_processo=?,"+
												      		   "nm_processo=?,"+
												      		   "st_processo=?,"+
												      		   "cd_empresa=?,"+
												      		   "txt_processo=?,"+
												      		   "cd_tipo_processo=?,"+
												      		   "tp_numeracao=?,"+
												      		   "id_prefixo_numeracao=?,"+
												      		   "txt_mascara_numeracao=?,"+
												      		   "nr_ultima_numeracao=?,"+
												      		   "tp_processo=? WHERE cd_processo=?");
			pstmt.setInt(1,objeto.getCdProcesso());
			pstmt.setString(2,objeto.getNmProcesso());
			pstmt.setInt(3,objeto.getStProcesso());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			pstmt.setString(5,objeto.getTxtProcesso());
			if(objeto.getCdTipoProcesso()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTipoProcesso());
			pstmt.setInt(7,objeto.getTpNumeracao());
			pstmt.setString(8,objeto.getIdPrefixoNumeracao());
			pstmt.setString(9,objeto.getTxtMascaraNumeracao());
			pstmt.setInt(10,objeto.getNrUltimaNumeracao());
			pstmt.setInt(11,objeto.getTpProcesso());
			pstmt.setInt(12, cdProcessoOld!=0 ? cdProcessoOld : objeto.getCdProcesso());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProcesso) {
		return delete(cdProcesso, null);
	}

	public static int delete(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return connect.prepareStatement("DELETE FROM grl_processo WHERE cd_processo="+cdProcesso).executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Processo get(int cdProcesso) {
		return get(cdProcesso, null);
	}

	public static Processo get(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_processo WHERE cd_processo=?");
			pstmt.setInt(1, cdProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Processo(rs.getInt("cd_processo"),
						rs.getString("nm_processo"),
						rs.getInt("st_processo"),
						rs.getInt("cd_empresa"),
						rs.getString("txt_processo"),
						rs.getInt("cd_tipo_processo"),
						rs.getInt("tp_numeracao"),
						rs.getString("id_prefixo_numeracao"),
						rs.getString("txt_mascara_numeracao"),
						rs.getInt("nr_ultima_numeracao"),
						rs.getInt("tp_processo"));
			}
			else
				return null;
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

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_processo");
			return new ResultSetMap(pstmt.executeQuery());
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

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_processo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

