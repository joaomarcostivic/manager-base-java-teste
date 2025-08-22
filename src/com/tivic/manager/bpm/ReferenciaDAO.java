package com.tivic.manager.bpm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ReferenciaDAO{

	public static int insert(Referencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(Referencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("bpm_referencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdReferencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO bpm_referencia (cd_referencia,"+
			                                  "cd_bem,"+
			                                  "cd_setor,"+
			                                  "cd_documento_entrada,"+
			                                  "cd_empresa,"+
			                                  "cd_marca,"+
			                                  "dt_aquisicao,"+
			                                  "dt_garantia,"+
			                                  "dt_validade,"+
			                                  "dt_baixa,"+
			                                  "nr_serie,"+
			                                  "nr_tombo,"+
			                                  "st_referencia,"+
			                                  "nm_modelo,"+
			                                  "dt_incorporacao,"+
			                                  "qt_capacidade,"+
			                                  "lg_producao,"+
			                                  "id_referencia," +
			                                  "cd_local_armazenamento,"+
			                                  "tp_referencia,"+
			                                  "txt_versao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdBem()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdBem());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSetor());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdDocumentoEntrada());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdMarca());
			if(objeto.getDtAquisicao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtAquisicao().getTimeInMillis()));
			if(objeto.getDtGarantia()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtGarantia().getTimeInMillis()));
			if(objeto.getDtValidade()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtValidade().getTimeInMillis()));
			if(objeto.getDtBaixa()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtBaixa().getTimeInMillis()));
			pstmt.setString(11,objeto.getNrSerie());
			pstmt.setString(12,objeto.getNrTombo());
			pstmt.setInt(13,objeto.getStReferencia());
			pstmt.setString(14,objeto.getNmModelo());
			if(objeto.getDtIncorporacao()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtIncorporacao().getTimeInMillis()));
			pstmt.setFloat(16,objeto.getQtCapacidade());
			pstmt.setInt(17,objeto.getLgProducao());
			pstmt.setString(18,objeto.getIdReferencia());
			if(objeto.getCdLocalArmazenamento()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdLocalArmazenamento());
			pstmt.setString(20,objeto.getTpReferencia());
			pstmt.setString(21,objeto.getTxtVersao());
			pstmt.executeUpdate();
			System.out.println("CD_MARCA: " + objeto.getCdMarca());
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Referencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Referencia objeto, int cdReferenciaOld) {
		return update(objeto, cdReferenciaOld, null);
	}

	public static int update(Referencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Referencia objeto, int cdReferenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE bpm_referencia SET cd_referencia=?,"+
												      		   "cd_bem=?,"+
												      		   "cd_setor=?,"+
												      		   "cd_documento_entrada=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_marca=?,"+
												      		   "dt_aquisicao=?,"+
												      		   "dt_garantia=?,"+
												      		   "dt_validade=?,"+
												      		   "dt_baixa=?,"+
												      		   "nr_serie=?,"+
												      		   "nr_tombo=?,"+
												      		   "st_referencia=?,"+
												      		   "nm_modelo=?,"+
												      		   "dt_incorporacao=?,"+
												      		   "qt_capacidade=?,"+
												      		   "lg_producao=?,"+
												      		   "id_referencia=?," +
												      		   "cd_local_armazenamento=?," +
												      		   "tp_referencia=?," +
												      		   "txt_versao=? WHERE cd_referencia=?");
			pstmt.setInt(1,objeto.getCdReferencia());
			if(objeto.getCdBem()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdBem());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSetor());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdDocumentoEntrada());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdMarca());
			if(objeto.getDtAquisicao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtAquisicao().getTimeInMillis()));
			if(objeto.getDtGarantia()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtGarantia().getTimeInMillis()));
			if(objeto.getDtValidade()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtValidade().getTimeInMillis()));
			if(objeto.getDtBaixa()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtBaixa().getTimeInMillis()));
			pstmt.setString(11,objeto.getNrSerie());
			pstmt.setString(12,objeto.getNrTombo());
			pstmt.setInt(13,objeto.getStReferencia());
			pstmt.setString(14,objeto.getNmModelo());
			if(objeto.getDtIncorporacao()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtIncorporacao().getTimeInMillis()));
			pstmt.setFloat(16,objeto.getQtCapacidade());
			pstmt.setInt(17,objeto.getLgProducao());
			pstmt.setString(18,objeto.getIdReferencia());
			if(objeto.getCdLocalArmazenamento()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdLocalArmazenamento());
			pstmt.setString(20, objeto.getTpReferencia());
			pstmt.setString(21, objeto.getTxtVersao());
			pstmt.setInt(22, cdReferenciaOld!=0 ? cdReferenciaOld : objeto.getCdReferencia());
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

	public static int delete(int cdReferencia) {
		return delete(cdReferencia, null);
	}

	public static int delete(int cdReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM bpm_referencia WHERE cd_referencia=?");
			pstmt.setInt(1, cdReferencia);
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

	public static Referencia get(int cdReferencia) {
		return get(cdReferencia, null);
	}

	public static Referencia get(int cdReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM bpm_referencia WHERE cd_referencia=?");
			pstmt.setInt(1, cdReferencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Referencia(rs.getInt("cd_referencia"),
						rs.getInt("cd_bem"),
						rs.getInt("cd_setor"),
						rs.getInt("cd_documento_entrada"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_marca"),
						(rs.getTimestamp("dt_aquisicao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_aquisicao").getTime()),
						(rs.getTimestamp("dt_garantia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_garantia").getTime()),
						(rs.getTimestamp("dt_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_validade").getTime()),
						(rs.getTimestamp("dt_baixa")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_baixa").getTime()),
						rs.getString("nr_serie"),
						rs.getString("nr_tombo"),
						rs.getInt("st_referencia"),
						rs.getString("nm_modelo"),
						(rs.getTimestamp("dt_incorporacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_incorporacao").getTime()),
						rs.getFloat("qt_capacidade"),
						rs.getInt("lg_producao"),
						rs.getString("id_referencia"),
						rs.getInt("cd_local_armazenamento"),
						rs.getString("tp_referencia"),
						rs.getString("txt_versao"));
			}
			else{
				return null;
			}
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
			pstmt = connect.prepareStatement("SELECT * FROM bpm_referencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM bpm_referencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
