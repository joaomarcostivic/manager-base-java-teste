package com.tivic.manager.pcb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class TanqueDAO{

	public static int insert(Tanque objeto) {
		return insert(objeto, null);
	}

	public static int insert(Tanque objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.alm.LocalArmazenamentoDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLocalArmazenamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO pcb_tanque (cd_tanque,"+
			                                  "qt_lastro,"+
			                                  "st_tanque,"+
			                                  "dt_cadastro,"+
			                                  "dt_instalacao,"+
			                                  "txt_observacao,"+
			                                  "cd_tipo_tanque,"+
			                                  "cd_produto_servico) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdLocalArmazenamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdLocalArmazenamento());
			pstmt.setFloat(2,objeto.getQtLastro());
			pstmt.setInt(3,objeto.getStTanque());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtInstalacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInstalacao().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtObservacao());
			if(objeto.getCdTipoTanque()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoTanque());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdProdutoServico());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TanqueDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TanqueDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Tanque objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Tanque objeto, int cdTanqueOld) {
		return update(objeto, cdTanqueOld, null);
	}

	public static int update(Tanque objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Tanque objeto, int cdTanqueOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			Tanque objetoTemp = get(objeto.getCdLocalArmazenamento(), connect);
			if (objetoTemp == null) 
				pstmt = connect.prepareStatement("INSERT INTO pcb_tanque (cd_tanque,"+
			                                  "qt_lastro,"+
			                                  "st_tanque,"+
			                                  "dt_cadastro,"+
			                                  "dt_instalacao,"+
			                                  "txt_observacao,"+
			                                  "cd_tipo_tanque,"+
			                                  "cd_produto_servico) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE pcb_tanque SET cd_tanque=?,"+
												      		   "qt_lastro=?,"+
												      		   "st_tanque=?,"+
												      		   "dt_cadastro=?,"+
												      		   "dt_instalacao=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_tipo_tanque=?,"+
												      		   "cd_produto_servico=? WHERE cd_tanque=?");
			pstmt.setInt(1,objeto.getCdLocalArmazenamento());
			pstmt.setFloat(2,objeto.getQtLastro());
			pstmt.setInt(3,objeto.getStTanque());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtInstalacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInstalacao().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtObservacao());
			if(objeto.getCdTipoTanque()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoTanque());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdProdutoServico());
			if (objetoTemp != null) {
				pstmt.setInt(9, cdTanqueOld!=0 ? cdTanqueOld : objeto.getCdLocalArmazenamento());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.alm.LocalArmazenamentoDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			
			if(objetoTemp.getCdProdutoServico() != objeto.getCdProdutoServico())
				return -1;
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTanque) {
		return delete(cdTanque, null);
	}

	public static int delete(int cdTanque, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM pcb_tanque WHERE cd_tanque=?");
			pstmt.setInt(1, cdTanque);
			pstmt.executeUpdate();
			if (com.tivic.manager.alm.LocalArmazenamentoDAO.delete(cdTanque, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TanqueDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TanqueDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Tanque get(int cdTanque) {
		return get(cdTanque, null);
	}

	public static Tanque get(int cdTanque, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM pcb_tanque A, alm_local_armazenamento B WHERE A.cd_tanque=B.cd_local_armazenamento AND A.cd_tanque=?");
			pstmt.setInt(1, cdTanque);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Tanque(rs.getInt("cd_local_armazenamento"),
						rs.getInt("cd_setor"),
						rs.getInt("cd_nivel_local"),
						rs.getInt("cd_responsavel"),
						rs.getString("nm_local_armazenamento"),
						rs.getString("id_local_armazenamento"),
						rs.getInt("cd_local_armazenamento_superior"),
						rs.getInt("cd_empresa"),
						rs.getFloat("qt_lastro"),
						rs.getInt("st_tanque"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						(rs.getTimestamp("dt_instalacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_instalacao").getTime()),
						rs.getString("txt_observacao"),
						rs.getInt("cd_tipo_tanque"),
						rs.getInt("cd_produto_servico"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TanqueDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TanqueDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM pcb_tanque");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TanqueDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TanqueDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM pcb_tanque", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getAllTanques(int cdEmpresa) {
		
		return getAllTanques(cdEmpresa, null);
	}

	public static ResultSetMap getAllTanques(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String sql = "SELECT T.*, LA.*, LA.nm_local_armazenamento AS nm_tanque, " +
					     "       PS.nm_produto_servico as nm_combustivel, TT.nm_tipo_tanque " +
					 	 "FROM pcb_tanque T " +
					 	 "JOIN alm_local_armazenamento LA ON(T.cd_tanque          = LA.cd_local_armazenamento) " +
					 	 "JOIN grl_produto_servico     PS ON(T.cd_produto_servico = PS.cd_produto_servico) " +
					 	 "JOIN pcb_tipo_tanque         TT ON(TT.cd_tipo_tanque    = T.cd_tipo_tanque) " +
					 	 "WHERE LA.cd_empresa = "+cdEmpresa;
			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
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
	
	public static ResultSetMap findTanque(ArrayList<ItemComparator> criterios) {
		return findTanque(criterios,null);
	}
	
	public static ResultSetMap findTanque(ArrayList<ItemComparator> criterios, Connection connect) {
		String sql = "SELECT T.*, LA.nm_local_armazenamento AS nm_tanque, PS.nm_produto_servico as nm_combustivel, TT.nm_tipo_tanque ,TT.qt_capacidade as qt_tanque_capacidade, LA.* " +
					 "FROM pcb_tanque T " +
				 	 "JOIN alm_local_armazenamento LA ON(T.cd_tanque          = LA.cd_local_armazenamento) " +
				 	 "JOIN grl_produto_servico     PS ON(T.cd_produto_servico = PS.cd_produto_servico) " +
				 	 "JOIN pcb_tipo_tanque         TT ON(TT.cd_tipo_tanque    = T.cd_tipo_tanque) " +
				 	 "WHERE 1=1 ";
		
		return Search.find(sql, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
