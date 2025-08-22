package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class AgenteDAO{

	public static int insert(Agente objeto) {
		return insert(objeto, null);
	}

	public static int insert(Agente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_agente", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAgente(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_agente (cd_agente,"+
			                                  "cd_usuario,"+
			                                  "nm_agente,"+
			                                  "ds_endereco,"+
			                                  "nm_bairro,"+
			                                  "nr_cep,"+
			                                  "cd_cidade,"+
			                                  "nr_matricula,"+
			                                  "tp_agente,"+
			                                  "cd_orgao,"+
			                                  "st_agente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setString(3,objeto.getNmAgente());
			pstmt.setString(4,objeto.getDsEndereco());
			pstmt.setString(5,objeto.getNmBairro());
			pstmt.setString(6,objeto.getNrCep());
			pstmt.setInt(7,objeto.getCdCidade());
			pstmt.setString(8,objeto.getNrMatricula());
			pstmt.setInt(9,objeto.getTpAgente());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdOrgao());
			if(objeto.getStAgente()==0) 
				pstmt.setNull(11, Types.SMALLINT);
			else
				pstmt.setInt(11, objeto.getStAgente());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Agente objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Agente objeto, int cdAgente) {
		return update(objeto, cdAgente, null);
	}

	public static int update(Agente objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Agente objeto, int cdAgente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_agente SET cd_agente=?,"+
												      		   "cd_usuario=?,"+
												      		   "nm_agente=?,"+
												      		   "ds_endereco=?,"+
												      		   "nm_bairro=?,"+
												      		   "nr_cep=?,"+
												      		   "cd_cidade=?,"+
												      		   "nr_matricula=?,"+
												      		   "tp_agente=?, "+
												      		   "cd_orgao=?, "+
												      		   "st_agente=? WHERE cd_agente=?");
			pstmt.setInt(1,objeto.getCdAgente());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setString(3,objeto.getNmAgente());
			pstmt.setString(4,objeto.getDsEndereco());
			pstmt.setString(5,objeto.getNmBairro());
			pstmt.setString(6,objeto.getNrCep());
			pstmt.setInt(7,objeto.getCdCidade());
			pstmt.setString(8,objeto.getNrMatricula());
			pstmt.setInt(9,objeto.getTpAgente());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdOrgao());
			if(objeto.getStAgente()==0) 
				pstmt.setNull(11, Types.SMALLINT);
			else
				pstmt.setInt(11, objeto.getStAgente());
			pstmt.setInt(12, cdAgente!=0 ? cdAgente : objeto.getCdAgente());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAgente) {
		return delete(cdAgente, null);
	}

	public static int delete(int cdAgente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_agente WHERE cd_agente=?");
			pstmt.setInt(1, cdAgente);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Agente get(int cdAgente) {
		return get(cdAgente, null);
	}

	public static Agente get(int cdAgente, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); //Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			if(lgBaseAntiga) {
				pstmt = connect.prepareStatement("SELECT * FROM agente WHERE cod_agente=?");
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM mob_agente WHERE cd_agente=?");
			}
			pstmt.setInt(1, cdAgente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Agente( lgBaseAntiga ? rs.getInt("cod_agente"): rs.getInt("cd_agente"),
						lgBaseAntiga ? rs.getInt("cod_usuario"): rs.getInt("cd_usuario"),
						rs.getString("nm_agente"),
						rs.getString("ds_endereco"),
						lgBaseAntiga ? rs.getString("bairro"): rs.getString("nm_bairro"),
						rs.getString("nr_cep"),
						lgBaseAntiga ? rs.getInt("cod_municipio"): rs.getInt("cd_cidade"),
						rs.getString("nr_matricula"),
						rs.getInt("tp_agente"),
						lgBaseAntiga ? 0 : rs.getInt("cd_orgao"),
						rs.getInt("st_agente"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Agente getByCdUsuario(int cdUsuario) {
	    return getByCdUsuario(cdUsuario, null);
	}

	public static Agente getByCdUsuario(int cdUsuario, Connection connect) {
	    boolean isConnectionNull = connect == null;
	    if (isConnectionNull)
	        connect = Conexao.conectar();
	    PreparedStatement pstmt;
	    ResultSet rs;
	    try {
	        pstmt = connect.prepareStatement("SELECT * FROM mob_agente WHERE cd_usuario=?");
	        pstmt.setInt(1, cdUsuario);
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	        	return new Agente(
	        		    rs.getInt("cd_agente"),
	        		    rs.getInt("cd_usuario"),
	        		    rs.getString("nm_agente"),
	        		    rs.getString("ds_endereco"),
	        		    rs.getString("nm_bairro"),
	        		    rs.getString("nr_cep"),
	        		    rs.getInt("cd_cidade"),
	        		    rs.getString("nr_matricula"),
	        		    rs.getInt("tp_agente"),
	        		    rs.getInt("cd_orgao"),
	        		    rs.getInt("st_agente")
	        		);
	        } else {
	            return null;
	        }
	    } catch (SQLException sqlExpt) {
	        sqlExpt.printStackTrace(System.out);
	        System.err.println("Erro! AgenteDAO.getByCdUsuario: " + sqlExpt);
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace(System.out);
	        System.err.println("Erro! AgenteDAO.getByCdUsuario: " + e);
	        return null;
	    } finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_agente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Agente> getList() {
		return getList(null);
	}

	public static ArrayList<Agente> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Agente> list = new ArrayList<Agente>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Agente obj = AgenteDAO.get(rsm.getInt("cd_agente"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_agente", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
