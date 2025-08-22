package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class CorreiosEtiquetaDAO{

	public static int insert(CorreiosEtiqueta objeto) {
		return insert(objeto, null);
	}

	public static int insert(CorreiosEtiqueta objeto, Connection connect){
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = lgBaseAntiga == true ?  Conexao.getSequenceCode("stt_correios_etiqueta", connect) : Conexao.getSequenceCode("mob_correios_etiqueta", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEtiqueta(code);
			
			String insertBaseNova = "INSERT INTO mob_correios_etiqueta (cd_etiqueta,"+
                    				"cd_lote,"+
                    				"nr_etiqueta,"+
                    				"dt_envio,"+
                    				"sg_servico,"+
                    				"cd_ait,"+
                    				"tp_status,"+
                    				"nr_movimento,"+
                    				"nr_digito_verificador," +
                    				"cd_lote_impressao," +
                    				"st_aviso_recebimento,"+
                    				"dt_aviso_recebimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			String insertBaseAntiga = "INSERT INTO stt_correios_etiqueta (cd_etiqueta,"+
									  "cd_lote,"+
									  "nr_etiqueta,"+
									  "dt_envio,"+
									  "cd_ait,"+
									  "tp_status,"+
									  "nr_movimento,"+
									  "sg_servico," +
									  "cd_lote_impressao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			String sqlInsert = lgBaseAntiga == true ? insertBaseAntiga : insertBaseNova;

			PreparedStatement pstmt = connect.prepareStatement(sqlInsert);
			pstmt.setInt(1, code);
			if(objeto.getCdLote()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLote());
			pstmt.setInt(3,objeto.getNrEtiqueta());
			if(objeto.getDtEnvio()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEnvio().getTimeInMillis()));

			if (lgBaseAntiga) {
				pstmt.setInt(5,objeto.getCdAit());
				pstmt.setInt(6,objeto.getTpStatus());
				pstmt.setInt(7,objeto.getNrMovimento());
				pstmt.setString(8,objeto.getSgServico());
				pstmt.setInt(9,objeto.getCdLoteImpressao());
			}
			else {
				pstmt.setString(5,objeto.getSgServico());
				if(objeto.getCdAit()==0)
					pstmt.setNull(6, Types.INTEGER);
				else
					pstmt.setInt(6,objeto.getCdAit());
				pstmt.setInt(7,objeto.getTpStatus());
				pstmt.setInt(8,objeto.getNrMovimento());
				pstmt.setInt(9,objeto.getNrDigitoVerificador());
				if(objeto.getCdLoteImpressao()==0)
					pstmt.setNull(10, Types.INTEGER);
				else
					pstmt.setInt(10,objeto.getCdLoteImpressao());
				pstmt.setInt(11,objeto.getStAvisoRecebimento());
				if(objeto.getDtAvisoRecebimento()==null)
					pstmt.setNull(12, Types.TIMESTAMP);
				else
					pstmt.setTimestamp(12, new Timestamp(objeto.getDtAvisoRecebimento().getTimeInMillis()));
			}

			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CorreiosEtiquetaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CorreiosEtiquetaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CorreiosEtiqueta objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CorreiosEtiqueta objeto, int cdEtiquetaOld) {
		return update(objeto, cdEtiquetaOld, null);
	}

	public static int update(CorreiosEtiqueta objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CorreiosEtiqueta objeto, int cdEtiquetaOld, Connection connect){
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			String updateBaseNova = "UPDATE mob_correios_etiqueta SET cd_etiqueta=?,"+
		      		   "cd_lote=?,"+
		      		   "nr_etiqueta=?,"+
		      		   "dt_envio=?,"+
		      		   "sg_servico=?,"+
		      		   "cd_ait=?,"+
		      		   "tp_status=?,"+
		      		   "nr_movimento=?,"+
		      		   "nr_digito_verificador=?," +
		      		   "cd_lote_impressao=?," +
		      		   "st_aviso_recebimento=?," + 
		      		   "dt_aviso_recebimento=? WHERE cd_etiqueta=?";

			String updateBaseAntiga = "UPDATE stt_correios_etiqueta SET cd_etiqueta=?,"+
		      		   "cd_lote=?,"+
		      		   "nr_etiqueta=?,"+
		      		   "dt_envio=?,"+
		      		   "cd_ait=?,"+
		      		   "tp_status=?,"+
		      		   "nr_movimento=?,"+
					   "sg_servico=?," +
					   "cd_lote_impressao=? WHERE cd_etiqueta=?";
			
			String sqlUpdate = lgBaseAntiga == true ? updateBaseAntiga : updateBaseNova;
			
			PreparedStatement pstmt = connect.prepareStatement(sqlUpdate);
			pstmt.setInt(1,objeto.getCdEtiqueta());
			if(objeto.getCdLote()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLote());
			pstmt.setInt(3,objeto.getNrEtiqueta());
			if(objeto.getDtEnvio()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEnvio().getTimeInMillis()));
			if (lgBaseAntiga) {
				pstmt.setInt(5,objeto.getCdAit());
				pstmt.setInt(6,objeto.getTpStatus());
				pstmt.setInt(7,objeto.getNrMovimento());
				pstmt.setString(8,objeto.getSgServico());
				pstmt.setInt(9, objeto.getCdLoteImpressao());
				pstmt.setInt(10, objeto.getCdEtiqueta());
			}
			else {
				pstmt.setString(5,objeto.getSgServico());
				if(objeto.getCdAit()<=0)
					pstmt.setNull(6, Types.INTEGER);
				else
					pstmt.setInt(6,objeto.getCdAit());
				if(objeto.getTpStatus()<=0)
					pstmt.setNull(7, Types.INTEGER);
				else
					pstmt.setInt(7,objeto.getTpStatus());
				pstmt.setInt(8,objeto.getNrMovimento());
				pstmt.setInt(9,objeto.getNrDigitoVerificador());
				if(objeto.getCdLoteImpressao()<=0)
					pstmt.setNull(10, Types.INTEGER);
				else
					pstmt.setInt(10,objeto.getCdLoteImpressao());
				pstmt.setInt(11,objeto.getStAvisoRecebimento());
				if(objeto.getDtAvisoRecebimento()==null)
					pstmt.setNull(12, Types.TIMESTAMP);
				else
					pstmt.setTimestamp(12, new Timestamp(objeto.getDtAvisoRecebimento().getTimeInMillis()));
				pstmt.setInt(13, cdEtiquetaOld!=0 ? cdEtiquetaOld : objeto.getCdEtiqueta());
				
			}
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CorreiosEtiquetaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CorreiosEtiquetaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEtiqueta) {
		return delete(cdEtiqueta, null);
	}

	public static int delete(int cdEtiqueta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_correios_etiqueta WHERE cd_etiqueta=?");
			pstmt.setInt(1, cdEtiqueta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CorreiosEtiquetaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CorreiosEtiquetaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CorreiosEtiqueta get(int cdEtiqueta) {
		return get(cdEtiqueta, null);
	}

	public static CorreiosEtiqueta get(int cdEtiqueta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_correios_etiqueta WHERE cd_etiqueta=?");
			pstmt.setInt(1, cdEtiqueta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CorreiosEtiqueta(rs.getInt("cd_etiqueta"),
						rs.getInt("cd_lote"),
						rs.getInt("nr_etiqueta"),
						(rs.getTimestamp("dt_envio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_envio").getTime()),
						rs.getString("sg_servico"),
						rs.getInt("cd_ait"),
						rs.getInt("tp_status"),
						rs.getInt("nr_movimento"),
						rs.getInt("nr_digito_verificador"),
						rs.getInt("cd_lote_impressao"),
						rs.getInt("st_aviso_recebimento"),
						(rs.getTimestamp("dt_aviso_recebimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_aviso_recebimento").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CorreiosEtiquetaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CorreiosEtiquetaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_correios_etiqueta");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CorreiosEtiquetaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CorreiosEtiquetaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CorreiosEtiqueta> getList() {
		return getList(null);
	}

	public static ArrayList<CorreiosEtiqueta> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CorreiosEtiqueta> list = new ArrayList<CorreiosEtiqueta>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CorreiosEtiqueta obj = CorreiosEtiquetaDAO.get(rsm.getInt("cd_etiqueta"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CorreiosEtiquetaDAO.getList: " + e);
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
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");	
		if (lgBaseAntiga) {
			return Search.find("SELECT * FROM stt_correios_etiqueta", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		return Search.find("SELECT * FROM mob_correios_etiqueta A LEFT OUTER JOIN mob_ait B ON (A.cd_ait = B.cd_ait)", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
