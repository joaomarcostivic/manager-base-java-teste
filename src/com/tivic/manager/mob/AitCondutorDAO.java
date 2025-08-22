package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class AitCondutorDAO{

	public static int insert(AitCondutor objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitCondutor objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_ait_condutor");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_ait");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdAit()));
			int code = Conexao.getSequenceCode("mob_ait_condutor", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAitCondutor(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ait_condutor (cd_ait_condutor,"+
			                                  "cd_ait,"+
			                                  "tp_documento,"+
			                                  "nr_documento,"+
			                                  "nm_condutor,"+
			                                  "nr_cnh_condutor,"+
			                                  "uf_cnh_condutor,"+
			                                  "ds_endereco_condutor,"+
			                                  "tp_cnh_condutor,"+
			                                  "ds_bairro_condutor,"+
			                                  "nr_imovel_condutor,"+
			                                  "ds_complemento_condutor,"+
			                                  "cd_cidade_condutor,"+
			                                  "nr_cep_condutor,"+
			                                  "nr_cpf_condutor,"+
			                                  "cd_condutor,"+
			                                  "cd_endereco_condutor,"+
			                                  "nm_condutor_autuacao,"+
			                                  "nr_cnh_autuacao,"+
			                                  "nr_documento_autuacao,"+
			                                  "cd_pessoa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdAit()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAit());
			pstmt.setInt(3,objeto.getTpDocumento());
			pstmt.setString(4,objeto.getNrDocumento());
			pstmt.setString(5,objeto.getNmCondutor());
			pstmt.setString(6,objeto.getNrCnhCondutor());
			pstmt.setString(7,objeto.getUfCnhCondutor());
			pstmt.setString(8,objeto.getDsEnderecoCondutor());
			pstmt.setInt(9,objeto.getTpCnhCondutor());
			pstmt.setString(10,objeto.getDsBairroCondutor());
			pstmt.setString(11,objeto.getNrImovelCondutor());
			pstmt.setString(12,objeto.getDsComplementoCondutor());
			pstmt.setInt(13,objeto.getCdCidadeCondutor());
			pstmt.setString(14,objeto.getNrCepCondutor());
			pstmt.setString(15,objeto.getNrCpfCondutor());
			pstmt.setInt(16,objeto.getCdCondutor());
			pstmt.setInt(17,objeto.getCdEnderecoCondutor());
			pstmt.setString(18,objeto.getNmCondutorAutuacao());
			pstmt.setString(19,objeto.getNrCnhAutuacao());
			pstmt.setString(20,objeto.getNrDocumentoAutuacao());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdPessoa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitCondutorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitCondutorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitCondutor objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AitCondutor objeto, int cdAitCondutorOld, int cdAitOld) {
		return update(objeto, cdAitCondutorOld, cdAitOld, null);
	}

	public static int update(AitCondutor objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AitCondutor objeto, int cdAitCondutorOld, int cdAitOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_condutor SET cd_ait_condutor=?,"+
												      		   "cd_ait=?,"+
												      		   "tp_documento=?,"+
												      		   "nr_documento=?,"+
												      		   "nm_condutor=?,"+
												      		   "nr_cnh_condutor=?,"+
												      		   "uf_cnh_condutor=?,"+
												      		   "ds_endereco_condutor=?,"+
												      		   "tp_cnh_condutor=?,"+
												      		   "ds_bairro_condutor=?,"+
												      		   "nr_imovel_condutor=?,"+
												      		   "ds_complemento_condutor=?,"+
												      		   "cd_cidade_condutor=?,"+
												      		   "nr_cep_condutor=?,"+
												      		   "nr_cpf_condutor=?,"+
												      		   "cd_condutor=?,"+
												      		   "cd_endereco_condutor=?,"+
												      		   "nm_condutor_autuacao=?,"+
												      		   "nr_cnh_autuacao=?,"+
												      		   "nr_documento_autuacao=?,"+
												      		   "cd_pessoa=? WHERE cd_ait_condutor=? AND cd_ait=?");
			pstmt.setInt(1,objeto.getCdAitCondutor());
			pstmt.setInt(2,objeto.getCdAit());
			pstmt.setInt(3,objeto.getTpDocumento());
			pstmt.setString(4,objeto.getNrDocumento());
			pstmt.setString(5,objeto.getNmCondutor());
			pstmt.setString(6,objeto.getNrCnhCondutor());
			pstmt.setString(7,objeto.getUfCnhCondutor());
			pstmt.setString(8,objeto.getDsEnderecoCondutor());
			pstmt.setInt(9,objeto.getTpCnhCondutor());
			pstmt.setString(10,objeto.getDsBairroCondutor());
			pstmt.setString(11,objeto.getNrImovelCondutor());
			pstmt.setString(12,objeto.getDsComplementoCondutor());
			pstmt.setInt(13,objeto.getCdCidadeCondutor());
			pstmt.setString(14,objeto.getNrCepCondutor());
			pstmt.setString(15,objeto.getNrCpfCondutor());
			pstmt.setInt(16,objeto.getCdCondutor());
			pstmt.setInt(17,objeto.getCdEnderecoCondutor());
			pstmt.setString(18,objeto.getNmCondutorAutuacao());
			pstmt.setString(19,objeto.getNrCnhAutuacao());
			pstmt.setString(20,objeto.getNrDocumentoAutuacao());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdPessoa());
			pstmt.setInt(22, cdAitCondutorOld!=0 ? cdAitCondutorOld : objeto.getCdAitCondutor());
			pstmt.setInt(23, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitCondutorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitCondutorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAitCondutor, int cdAit) {
		return delete(cdAitCondutor, cdAit, null);
	}

	public static int delete(int cdAitCondutor, int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ait_condutor WHERE cd_ait_condutor=? AND cd_ait=?");
			pstmt.setInt(1, cdAitCondutor);
			pstmt.setInt(2, cdAit);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitCondutorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitCondutorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitCondutor get(int cdAitCondutor, int cdAit) {
		return get(cdAitCondutor, cdAit, null);
	}

	public static AitCondutor get(int cdAitCondutor, int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_condutor WHERE cd_ait_condutor=? AND cd_ait=?");
			pstmt.setInt(1, cdAitCondutor);
			pstmt.setInt(2, cdAit);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitCondutor(rs.getInt("cd_ait_condutor"),
						rs.getInt("cd_ait"),
						rs.getInt("tp_documento"),
						rs.getString("nr_documento"),
						rs.getString("nm_condutor"),
						rs.getString("nr_cnh_condutor"),
						rs.getString("uf_cnh_condutor"),
						rs.getString("ds_endereco_condutor"),
						rs.getInt("tp_cnh_condutor"),
						rs.getString("ds_bairro_condutor"),
						rs.getString("nr_imovel_condutor"),
						rs.getString("ds_complemento_condutor"),
						rs.getInt("cd_cidade_condutor"),
						rs.getString("nr_cep_condutor"),
						rs.getString("nr_cpf_condutor"),
						rs.getInt("cd_condutor"),
						rs.getInt("cd_endereco_condutor"),
						rs.getString("nm_condutor_autuacao"),
						rs.getString("nr_cnh_autuacao"),
						rs.getString("nr_documento_autuacao"),
						rs.getInt("cd_pessoa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitCondutorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitCondutorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_condutor");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitCondutorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitCondutorDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AitCondutor> getList() {
		return getList(null);
	}

	public static ArrayList<AitCondutor> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitCondutor> list = new ArrayList<AitCondutor>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AitCondutor obj = AitCondutorDAO.get(rsm.getInt("cd_ait_condutor"), rsm.getInt("cd_ait"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitCondutorDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_ait_condutor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
