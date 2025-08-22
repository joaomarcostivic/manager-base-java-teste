package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class RecorteDAO{

	public static int insert(Recorte objeto) {
		return insert(objeto, null);
	}

	public static int insert(Recorte objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("prc_recorte", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRecorte(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_recorte (cd_recorte,"+
			                                  "id_recorte,"+
			                                  "txt_recorte,"+
			                                  "cd_processo,"+
			                                  "dt_processamento,"+
			                                  "dt_busca,"+
			                                  "st_recorte,"+
			                                  "cd_servico,"+
			                                  "dt_publicacao,"+
			                                  "nm_diario,"+
			                                  "nr_pagina,"+
			                                  "nm_orgao,"+
			                                  "nm_juizo,"+
			                                  "txt_andamento,"+
			                                  "cd_estado,"+
			                                  "st_anterior,"+
			                                  "id_processo_recorte) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getIdRecorte());
			pstmt.setString(3,objeto.getTxtRecorte());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProcesso());
			if(objeto.getDtProcessamento()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtProcessamento().getTimeInMillis()));
			if(objeto.getDtBusca()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtBusca().getTimeInMillis()));
			pstmt.setInt(7,objeto.getStRecorte());
			if(objeto.getCdServico()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdServico());
			if(objeto.getDtPublicacao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtPublicacao().getTimeInMillis()));
			pstmt.setString(10,objeto.getNmDiario());
			pstmt.setString(11,objeto.getNrPagina());
			pstmt.setString(12,objeto.getNmOrgao());
			pstmt.setString(13,objeto.getNmJuizo());
			pstmt.setString(14,objeto.getTxtAndamento());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdEstado());
			pstmt.setInt(16,objeto.getStAnterior());
			pstmt.setString(17,objeto.getIdProcessoRecorte());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecorteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecorteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Recorte objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Recorte objeto, int cdRecorteOld) {
		return update(objeto, cdRecorteOld, null);
	}

	public static int update(Recorte objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Recorte objeto, int cdRecorteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_recorte SET cd_recorte=?,"+
												      		   "id_recorte=?,"+
												      		   "txt_recorte=?,"+
												      		   "cd_processo=?,"+
												      		   "dt_processamento=?,"+
												      		   "dt_busca=?,"+
												      		   "st_recorte=?,"+
												      		   "cd_servico=?,"+
												      		   "dt_publicacao=?,"+
												      		   "nm_diario=?,"+
												      		   "nr_pagina=?,"+
												      		   "nm_orgao=?,"+
												      		   "nm_juizo=?,"+
												      		   "txt_andamento=?,"+
												      		   "cd_estado=?,"+
												      		   "st_anterior=?,"+
												      		   "id_processo_recorte=? WHERE cd_recorte=?");
			pstmt.setInt(1,objeto.getCdRecorte());
			pstmt.setString(2,objeto.getIdRecorte());
			pstmt.setString(3,objeto.getTxtRecorte());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProcesso());
			if(objeto.getDtProcessamento()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtProcessamento().getTimeInMillis()));
			if(objeto.getDtBusca()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtBusca().getTimeInMillis()));
			pstmt.setInt(7,objeto.getStRecorte());
			if(objeto.getCdServico()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdServico());
			if(objeto.getDtPublicacao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtPublicacao().getTimeInMillis()));
			pstmt.setString(10,objeto.getNmDiario());
			pstmt.setString(11,objeto.getNrPagina());
			pstmt.setString(12,objeto.getNmOrgao());
			pstmt.setString(13,objeto.getNmJuizo());
			pstmt.setString(14,objeto.getTxtAndamento());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdEstado());
			pstmt.setInt(16,objeto.getStAnterior());
			pstmt.setString(17,objeto.getIdProcessoRecorte());
			pstmt.setInt(18, cdRecorteOld!=0 ? cdRecorteOld : objeto.getCdRecorte());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecorteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecorteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRecorte) {
		return delete(cdRecorte, null);
	}

	public static int delete(int cdRecorte, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_recorte WHERE cd_recorte=?");
			pstmt.setInt(1, cdRecorte);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecorteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecorteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Recorte get(int cdRecorte) {
		return get(cdRecorte, null);
	}

	public static Recorte get(int cdRecorte, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_recorte WHERE cd_recorte=?");
			pstmt.setInt(1, cdRecorte);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Recorte(rs.getInt("cd_recorte"),
						rs.getString("id_recorte"),
						rs.getString("txt_recorte"),
						rs.getInt("cd_processo"),
						(rs.getTimestamp("dt_processamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_processamento").getTime()),
						(rs.getTimestamp("dt_busca")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_busca").getTime()),
						rs.getInt("st_recorte"),
						rs.getInt("cd_servico"),
						(rs.getTimestamp("dt_publicacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_publicacao").getTime()),
						rs.getString("nm_diario"),
						rs.getString("nr_pagina"),
						rs.getString("nm_orgao"),
						rs.getString("nm_juizo"),
						rs.getString("txt_andamento"),
						rs.getInt("cd_estado"),
						rs.getInt("st_anterior"),
						rs.getString("id_processo_recorte"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecorteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecorteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_recorte");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecorteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecorteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Recorte> getList() {
		return getList(null);
	}

	public static ArrayList<Recorte> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Recorte> list = new ArrayList<Recorte>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Recorte obj = RecorteDAO.get(rsm.getInt("cd_recorte"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecorteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_recorte", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}