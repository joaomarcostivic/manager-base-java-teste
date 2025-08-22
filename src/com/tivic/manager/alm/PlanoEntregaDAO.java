package com.tivic.manager.alm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class PlanoEntregaDAO{

	public static int insert(PlanoEntrega objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoEntrega objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("alm_plano_entrega", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPlano(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_plano_entrega (cd_plano,"+
			                                  "dt_cadastro,"+
			                                  "dt_entrega,"+
			                                  "cd_motorista,"+
			                                  "cd_supervisor,"+
			                                  "txt_observacao,"+
			                                  "id_plano,"+
			                                  "cd_veiculo,"+
			                                  "st_plano,"+
			                                  "cd_usuario,"+
			                                  "cd_viagem,"+
			                                  "nr_mes_referencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtEntrega()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtEntrega().getTimeInMillis()));
			if(objeto.getCdMotorista()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdMotorista());
			if(objeto.getCdSupervisor()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdSupervisor());
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.setString(7,objeto.getIdPlano());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdVeiculo());
			pstmt.setInt(9,objeto.getStPlano());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdUsuario());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdViagem());
			pstmt.setInt(12,objeto.getNrMesReferencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoEntrega objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PlanoEntrega objeto, int cdPlanoOld) {
		return update(objeto, cdPlanoOld, null);
	}

	public static int update(PlanoEntrega objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PlanoEntrega objeto, int cdPlanoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_plano_entrega SET cd_plano=?,"+
												      		   "dt_cadastro=?,"+
												      		   "dt_entrega=?,"+
												      		   "cd_motorista=?,"+
												      		   "cd_supervisor=?,"+
												      		   "txt_observacao=?,"+
												      		   "id_plano=?,"+
												      		   "cd_veiculo=?,"+
												      		   "st_plano=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_viagem=?,"+
												      		   "nr_mes_referencia=? WHERE cd_plano=?");
			pstmt.setInt(1,objeto.getCdPlano());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtEntrega()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtEntrega().getTimeInMillis()));
			if(objeto.getCdMotorista()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdMotorista());
			if(objeto.getCdSupervisor()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdSupervisor());
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.setString(7,objeto.getIdPlano());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdVeiculo());
			pstmt.setInt(9,objeto.getStPlano());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdUsuario());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdViagem());
			pstmt.setInt(12,objeto.getNrMesReferencia());
			pstmt.setInt(13, cdPlanoOld!=0 ? cdPlanoOld : objeto.getCdPlano());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlano) {
		return delete(cdPlano, null);
	}

	public static int delete(int cdPlano, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_plano_entrega WHERE cd_plano=?");
			pstmt.setInt(1, cdPlano);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoEntrega get(int cdPlano) {
		return get(cdPlano, null);
	}

	public static PlanoEntrega get(int cdPlano, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_plano_entrega WHERE cd_plano=?");
			pstmt.setInt(1, cdPlano);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoEntrega(rs.getInt("cd_plano"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						(rs.getTimestamp("dt_entrega")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_entrega").getTime()),
						rs.getInt("cd_motorista"),
						rs.getInt("cd_supervisor"),
						rs.getString("txt_observacao"),
						rs.getString("id_plano"),
						rs.getInt("cd_veiculo"),
						rs.getInt("st_plano"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_viagem"),
						rs.getInt("nr_mes_referencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_plano_entrega");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PlanoEntrega> getList() {
		return getList(null);
	}

	public static ArrayList<PlanoEntrega> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PlanoEntrega> list = new ArrayList<PlanoEntrega>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PlanoEntrega obj = PlanoEntregaDAO.get(rsm.getInt("cd_plano"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM alm_plano_entrega", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}