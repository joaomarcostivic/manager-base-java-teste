package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class ServicoRecorteDAO{

	public static int insert(ServicoRecorte objeto) {
		return insert(objeto, null);
	}

	public static int insert(ServicoRecorte objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("prc_servico_recorte", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdServico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_servico_recorte (cd_servico,"+
			                                  "nm_servico,"+
			                                  "tp_servico,"+
			                                  "id_token,"+
			                                  "id_cliente,"+
			                                  "nm_senha,"+
			                                  "ds_url,"+
			                                  "txt_formato,"+
			                                  "vl_timeout,"+
			                                  "dt_ultima_busca,"+
			                                  "lg_estado,"+
			                                  "st_servico,"+
			                                  "lg_confirmar_leitura) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmServico());
			pstmt.setInt(3,objeto.getTpServico());
			pstmt.setString(4,objeto.getIdToken());
			pstmt.setString(5,objeto.getIdCliente());
			pstmt.setString(6,objeto.getNmSenha());
			pstmt.setString(7,objeto.getDsUrl());
			pstmt.setString(8,objeto.getTxtFormato());
			pstmt.setInt(9,objeto.getVlTimeout());
			if(objeto.getDtUltimaBusca()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtUltimaBusca().getTimeInMillis()));
			pstmt.setInt(11,objeto.getLgEstado());
			pstmt.setInt(12,objeto.getStServico());
			pstmt.setInt(13,objeto.getLgConfirmarLeitura());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ServicoRecorte objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ServicoRecorte objeto, int cdServicoOld) {
		return update(objeto, cdServicoOld, null);
	}

	public static int update(ServicoRecorte objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ServicoRecorte objeto, int cdServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_servico_recorte SET cd_servico=?,"+
												      		   "nm_servico=?,"+
												      		   "tp_servico=?,"+
												      		   "id_token=?,"+
												      		   "id_cliente=?,"+
												      		   "nm_senha=?,"+
												      		   "ds_url=?,"+
												      		   "txt_formato=?,"+
												      		   "vl_timeout=?,"+
												      		   "dt_ultima_busca=?,"+
												      		   "lg_estado=?,"+
												      		   "st_servico=?,"+
												      		   "lg_confirmar_leitura=? WHERE cd_servico=?");
			pstmt.setInt(1,objeto.getCdServico());
			pstmt.setString(2,objeto.getNmServico());
			pstmt.setInt(3,objeto.getTpServico());
			pstmt.setString(4,objeto.getIdToken());
			pstmt.setString(5,objeto.getIdCliente());
			pstmt.setString(6,objeto.getNmSenha());
			pstmt.setString(7,objeto.getDsUrl());
			pstmt.setString(8,objeto.getTxtFormato());
			pstmt.setInt(9,objeto.getVlTimeout());
			if(objeto.getDtUltimaBusca()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtUltimaBusca().getTimeInMillis()));
			pstmt.setInt(11,objeto.getLgEstado());
			pstmt.setInt(12,objeto.getStServico());
			pstmt.setInt(13,objeto.getLgConfirmarLeitura());
			pstmt.setInt(14, cdServicoOld!=0 ? cdServicoOld : objeto.getCdServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdServico) {
		return delete(cdServico, null);
	}

	public static int delete(int cdServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_servico_recorte WHERE cd_servico=?");
			pstmt.setInt(1, cdServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ServicoRecorte get(int cdServico) {
		return get(cdServico, null);
	}

	public static ServicoRecorte get(int cdServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_servico_recorte WHERE cd_servico=?");
			pstmt.setInt(1, cdServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ServicoRecorte(rs.getInt("cd_servico"),
						rs.getString("nm_servico"),
						rs.getInt("tp_servico"),
						rs.getString("id_token"),
						rs.getString("id_cliente"),
						rs.getString("nm_senha"),
						rs.getString("ds_url"),
						rs.getString("txt_formato"),
						rs.getInt("vl_timeout"),
						(rs.getTimestamp("dt_ultima_busca")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ultima_busca").getTime()),
						rs.getInt("lg_estado"),
						rs.getInt("st_servico"),
						rs.getInt("lg_confirmar_leitura"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_servico_recorte");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ServicoRecorte> getList() {
		return getList(null);
	}

	public static ArrayList<ServicoRecorte> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ServicoRecorte> list = new ArrayList<ServicoRecorte>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ServicoRecorte obj = ServicoRecorteDAO.get(rsm.getInt("cd_servico"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_servico_recorte", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}