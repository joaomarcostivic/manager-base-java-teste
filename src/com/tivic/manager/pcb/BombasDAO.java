package com.tivic.manager.pcb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class BombasDAO{

	public static int insert(Bombas objeto) {
		return insert(objeto, null);
	}

	public static int insert(Bombas objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("pcb_bombas", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdBomba(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO pcb_bombas (cd_bomba,"+
			                                  "nr_ilha,"+
			                                  "nm_bomba,"+
			                                  "id_bomba,"+
			                                  "nr_ordem,"+
			                                  "tp_bomba,"+
			                                  "st_situacao,"+
			                                  "dt_fabricacao,"+
			                                  "nm_modelo,"+
			                                  "dt_cadastro,"+
			                                  "dt_ultima_alteracao,"+
			                                  "dt_instalacao,"+
			                                  "dt_baixa,"+
			                                  "txt_observacao,"+
			                                  "cd_fabricante) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getNrIlha());
			pstmt.setString(3,objeto.getNmBomba());
			pstmt.setString(4,objeto.getIdBomba());
			pstmt.setInt(5,objeto.getNrOrdem());
			pstmt.setInt(6,objeto.getTpBomba());
			pstmt.setInt(7,objeto.getStSituacao());
			if(objeto.getDtFabricacao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtFabricacao().getTimeInMillis()));
			pstmt.setString(9,objeto.getNmModelo());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtUltimaAlteracao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtUltimaAlteracao().getTimeInMillis()));
			if(objeto.getDtInstalacao()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtInstalacao().getTimeInMillis()));
			if(objeto.getDtBaixa()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtBaixa().getTimeInMillis()));
			pstmt.setString(14,objeto.getTxtObservacao());
			if(objeto.getCdFabricante()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdFabricante());
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

	public static int update(Bombas objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Bombas objeto, int cdBombaOld) {
		return update(objeto, cdBombaOld, null);
	}

	public static int update(Bombas objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Bombas objeto, int cdBombaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE pcb_bombas SET cd_bomba=?,"+
												      		   "nr_ilha=?,"+
												      		   "nm_bomba=?,"+
												      		   "id_bomba=?,"+
												      		   "nr_ordem=?,"+
												      		   "tp_bomba=?,"+
												      		   "st_situacao=?,"+
												      		   "dt_fabricacao=?,"+
												      		   "nm_modelo=?,"+
												      		   "dt_cadastro=?,"+
												      		   "dt_ultima_alteracao=?,"+
												      		   "dt_instalacao=?,"+
												      		   "dt_baixa=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_fabricante=? WHERE cd_bomba=?");
			pstmt.setInt(1,objeto.getCdBomba());
			pstmt.setInt(2,objeto.getNrIlha());
			pstmt.setString(3,objeto.getNmBomba());
			pstmt.setString(4,objeto.getIdBomba());
			pstmt.setInt(5,objeto.getNrOrdem());
			pstmt.setInt(6,objeto.getTpBomba());
			pstmt.setInt(7,objeto.getStSituacao());
			if(objeto.getDtFabricacao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtFabricacao().getTimeInMillis()));
			pstmt.setString(9,objeto.getNmModelo());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtUltimaAlteracao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtUltimaAlteracao().getTimeInMillis()));
			if(objeto.getDtInstalacao()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtInstalacao().getTimeInMillis()));
			if(objeto.getDtBaixa()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtBaixa().getTimeInMillis()));
			pstmt.setString(14,objeto.getTxtObservacao());
			if(objeto.getCdFabricante()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdFabricante());
			pstmt.setInt(16, cdBombaOld!=0 ? cdBombaOld : objeto.getCdBomba());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BombasDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BombasDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBomba) {
		return delete(cdBomba, null);
	}

	public static int delete(int cdBomba, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM pcb_bombas WHERE cd_bomba=?");
			pstmt.setInt(1, cdBomba);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BombasDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BombasDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Bombas get(int cdBomba) {
		return get(cdBomba, null);
	}

	public static Bombas get(int cdBomba, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM pcb_bombas WHERE cd_bomba=?");
			pstmt.setInt(1, cdBomba);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Bombas(rs.getInt("cd_bomba"),
						rs.getInt("nr_ilha"),
						rs.getString("nm_bomba"),
						rs.getString("id_bomba"),
						rs.getInt("nr_ordem"),
						rs.getInt("tp_bomba"),
						rs.getInt("st_situacao"),
						(rs.getTimestamp("dt_fabricacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_fabricacao").getTime()),
						rs.getString("nm_modelo"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						(rs.getTimestamp("dt_ultima_alteracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ultima_alteracao").getTime()),
						(rs.getTimestamp("dt_instalacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_instalacao").getTime()),
						(rs.getTimestamp("dt_baixa")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_baixa").getTime()),
						rs.getString("txt_observacao"),
						rs.getInt("cd_fabricante"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BombasDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BombasDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM pcb_bombas");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BombasDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BombasDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM pcb_bombas B", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	
	public static Bombas insertObjeto(Bombas objeto) {
		return insertObjeto(objeto, null);
	}

	public static Bombas insertObjeto(Bombas objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("pcb_bombas", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return null;
			}
			objeto.setCdBomba(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO pcb_bombas (cd_bomba,"+
			                                  "nr_ilha,"+
			                                  "nm_bomba,"+
			                                  "id_bomba,"+
			                                  "nr_ordem,"+
			                                  "tp_bomba,"+
			                                  "st_situacao,"+
			                                  "dt_fabricacao,"+
			                                  "nm_modelo,"+
			                                  "dt_cadastro,"+
			                                  "dt_ultima_alteracao,"+
			                                  "dt_instalacao,"+
			                                  "dt_baixa,"+
			                                  "txt_observacao,"+
			                                  "cd_fabricante) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getNrIlha());
			pstmt.setString(3,objeto.getNmBomba());
			pstmt.setString(4,objeto.getIdBomba());
			pstmt.setInt(5,objeto.getNrOrdem());
			pstmt.setInt(6,objeto.getTpBomba());
			pstmt.setInt(7,objeto.getStSituacao());
			if(objeto.getDtFabricacao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtFabricacao().getTimeInMillis()));
			pstmt.setString(9,objeto.getNmModelo());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtUltimaAlteracao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtUltimaAlteracao().getTimeInMillis()));
			if(objeto.getDtInstalacao()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtInstalacao().getTimeInMillis()));
			if(objeto.getDtBaixa()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtBaixa().getTimeInMillis()));
			pstmt.setString(14,objeto.getTxtObservacao());
			if(objeto.getCdFabricante()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdFabricante());
			pstmt.executeUpdate();
			return objeto;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BombasDAO.insertObjeto: " + sqlExpt);
			return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BombasDAO.insertObjeto: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	
	public static Bombas updateObjeto(Bombas objeto) {
		return updateObjeto(objeto, 0, null);
	}

	public static Bombas updateObjeto(Bombas objeto, int cdBombaOld) {
		return updateObjeto(objeto, cdBombaOld, null);
	}

	public static Bombas updateObjeto(Bombas objeto, Connection connect) {
		return updateObjeto(objeto, 0, connect);
	}

	public static Bombas updateObjeto(Bombas objeto, int cdBombaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE pcb_bombas SET cd_bomba=?,"+
												      		   "nr_ilha=?,"+
												      		   "nm_bomba=?,"+
												      		   "id_bomba=?,"+
												      		   "nr_ordem=?,"+
												      		   "tp_bomba=?,"+
												      		   "st_situacao=?,"+
												      		   "dt_fabricacao=?,"+
												      		   "nm_modelo=?,"+
												      		   "dt_cadastro=?,"+
												      		   "dt_ultima_alteracao=?,"+
												      		   "dt_instalacao=?,"+
												      		   "dt_baixa=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_fabricante=? WHERE cd_bomba=?");
			pstmt.setInt(1,objeto.getCdBomba());
			pstmt.setInt(2,objeto.getNrIlha());
			pstmt.setString(3,objeto.getNmBomba());
			pstmt.setString(4,objeto.getIdBomba());
			pstmt.setInt(5,objeto.getNrOrdem());
			pstmt.setInt(6,objeto.getTpBomba());
			pstmt.setInt(7,objeto.getStSituacao());
			if(objeto.getDtFabricacao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtFabricacao().getTimeInMillis()));
			pstmt.setString(9,objeto.getNmModelo());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtUltimaAlteracao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtUltimaAlteracao().getTimeInMillis()));
			if(objeto.getDtInstalacao()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtInstalacao().getTimeInMillis()));
			if(objeto.getDtBaixa()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtBaixa().getTimeInMillis()));
			pstmt.setString(14,objeto.getTxtObservacao());
			if(objeto.getCdFabricante()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdFabricante());
			pstmt.setInt(16, cdBombaOld!=0 ? cdBombaOld : objeto.getCdBomba());
			pstmt.executeUpdate();
			return objeto;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BombasDAO.update: " + sqlExpt);
			return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BombasDAO.update: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

}
