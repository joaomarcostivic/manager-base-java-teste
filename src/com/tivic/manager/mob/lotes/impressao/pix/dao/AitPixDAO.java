package com.tivic.manager.mob.lotes.impressao.pix.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.mob.lotes.impressao.pix.model.AitPix;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class AitPixDAO {
	
	public static int insert(AitPix objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitPix objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_ait_pix", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPix(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ait_pix (cd_pix,"
							                                  +"cd_ait,"
							                                  +"cd_cobranca,"
							                                  + "txt_txid,"
							                                  + "st_cobranca,"
							                                  + "dt_criacao,"
							                                  + "dt_vencimento,"
							                                  + "vl_cobranca,"
							                                  + "txt_observacao,"
							                                  + "tp_cobranca,"
							                                  + "nr_codigo_guia_recebimento,"
							                                  + "nm_devedor,"
							                                  + "nr_cpf_cnpj,"
							                                  + "nm_localizacao,"
							                                  + "ds_qr_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2, objeto.getCdAit()); 
			pstmt.setInt(3, objeto.getCdCobranca());
			pstmt.setString(4, objeto.getTxtTxid()); 
			pstmt.setInt(5, objeto.getStCobranca()); 
			if (objeto.getDtCriacao() == null)
			    pstmt.setNull(6, Types.TIMESTAMP);
			else
			    pstmt.setTimestamp(6, new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if (objeto.getDtVencimento() == null)
			    pstmt.setNull(7, Types.TIMESTAMP);
			else
			    pstmt.setTimestamp(7, new Timestamp(objeto.getDtVencimento().getTimeInMillis())); 
			pstmt.setDouble(8, objeto.getVlCobranca());
			pstmt.setString(9, objeto.getTxtObservacao()); 
			pstmt.setInt(10, objeto.getTpCobranca()); 
			pstmt.setString(11, objeto.getNrCodigoGuiaRecebimento()); 
			pstmt.setString(12, objeto.getNmDevedor()); 
			pstmt.setString(13, objeto.getNrCpfCnpj()); 
			pstmt.setString(14, objeto.getNmLocalizacao()); 
			pstmt.setString(15, objeto.getDsQrCode()); 
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPixDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPixDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitPix objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AitPix objeto, int cdPixOld) {
		return update(objeto, cdPixOld, null);
	}

	public static int update(AitPix objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AitPix objeto, int cdPixOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_pix SET cd_pix=?,"
																+ "cd_ait?,"
											                    + "cd_cobranca?,"
											                    + "txt_txid?,"
											                    + "st_cobranca?,"
											                    + "dt_criacao?,"
											                    + "dt_vencimento?,"
											                    + "vl_cobranca?,"
											                    + "txt_observacao?,"
											                    + "tp_cobranca?,"
											                    + "nr_codigo_guia_recebimento?,"
											                    + "nm_devedor?,"
											                    + "nr_cpf_cnpj?,"
											                    + "nm_localizacao? WHERE cd_pix=?");
			pstmt.setInt(1, objeto.getCdPix());
			pstmt.setInt(2, objeto.getCdAit()); 
			pstmt.setInt(3, objeto.getCdCobranca());
			pstmt.setString(4, objeto.getTxtTxid()); 
			pstmt.setInt(5, objeto.getStCobranca()); 
			if (objeto.getDtCriacao() == null)
			    pstmt.setNull(6, Types.TIMESTAMP);
			else
			    pstmt.setTimestamp(6, new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if (objeto.getDtVencimento() == null)
			    pstmt.setNull(7, Types.TIMESTAMP);
			else
			    pstmt.setTimestamp(7, new Timestamp(objeto.getDtVencimento().getTimeInMillis())); 
			pstmt.setDouble(8, objeto.getVlCobranca());
			pstmt.setString(9, objeto.getTxtObservacao()); 
			pstmt.setInt(10, objeto.getTpCobranca()); 
			pstmt.setString(11, objeto.getNrCodigoGuiaRecebimento()); 
			pstmt.setString(12, objeto.getNmDevedor()); 
			pstmt.setString(13, objeto.getNrCpfCnpj()); 
			pstmt.setString(14, objeto.getNmLocalizacao()); 
			pstmt.setString(15, objeto.getDsQrCode()); 
			pstmt.setInt(16, cdPixOld!=0 ? cdPixOld : objeto.getCdPix());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPixDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPixDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPix) {
		return delete(cdPix, null);
	}

	public static int delete(int cdPix, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ait_pix WHERE cd_pix=?");
			pstmt.setInt(1, cdPix);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPixDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPixDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitPix get(int cdPix) {
		return get(cdPix, null);
	}

	public static AitPix get(int cdPix, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_pix WHERE cd_pix=?");
			pstmt.setInt(1, cdPix);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitPix(
						rs.getInt("cd_pix"),
						rs.getInt("cd_ait"),
						rs.getInt("cd_cobranca"),
						rs.getString("txt_txid"),
						rs.getInt("st_cobranca"),
						(rs.getTimestamp("dt_criacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_criacao").getTime()),
						(rs.getTimestamp("dt_vencimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vencimento").getTime()),
						rs.getDouble("vl_cobranca"),
						rs.getString("txt_observacao"),
						rs.getInt("tp_cobranca"),
						rs.getString("nr_codigo_guia_recebimento"),
						rs.getString("nm_devedor"),
						rs.getString("nr_cpf_cnpj"),
						rs.getString("nm_localizacao"),
						rs.getString("ds_qr_code"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPixDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPixDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_pix");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPixDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPixDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_ait_pix", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
