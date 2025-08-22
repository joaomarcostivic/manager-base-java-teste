package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class AitProprietarioDAO{

	public static int insert(AitProprietario objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitProprietario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_ait_proprietario");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_ait");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdAit()));
			int code = Conexao.getSequenceCode("mob_ait_proprietario", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAitProprietario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ait_proprietario (cd_ait_proprietario,"+
			                                  "cd_ait,"+
			                                  "nm_proprietario,"+
			                                  "ds_logradouro,"+
			                                  "ds_nr_imovel,"+
			                                  "nr_cep,"+
			                                  "nr_ddd,"+
			                                  "nr_telefone,"+
			                                  "nr_cpf_proprietario,"+
			                                  "tp_pessoa_proprietario,"+
			                                  "nr_cpf_cnpj_proprietario,"+
			                                  "nm_complemento,"+
			                                  "cd_endereco,"+
			                                  "cd_proprietario,"+
			                                  "nm_proprietario_autuacao,"+
			                                  "cd_pessoa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdAit()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAit());
			pstmt.setString(3,objeto.getNmProprietario());
			pstmt.setString(4,objeto.getDsLogradouro());
			pstmt.setString(5,objeto.getDsNrImovel());
			pstmt.setString(6,objeto.getNrCep());
			pstmt.setString(7,objeto.getNrDdd());
			pstmt.setString(8,objeto.getNrTelefone());
			pstmt.setString(9,objeto.getNrCpfProprietario());
			pstmt.setInt(10,objeto.getTpPessoaProprietario());
			pstmt.setString(11,objeto.getNrCpfCnpjProprietario());
			pstmt.setString(12,objeto.getNmComplemento());
			pstmt.setInt(13,objeto.getCdEndereco());
			pstmt.setInt(14,objeto.getCdProprietario());
			pstmt.setString(15,objeto.getNmProprietarioAutuacao());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdPessoa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitProprietarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitProprietarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitProprietario objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AitProprietario objeto, int cdAitProprietarioOld, int cdAitOld) {
		return update(objeto, cdAitProprietarioOld, cdAitOld, null);
	}

	public static int update(AitProprietario objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AitProprietario objeto, int cdAitProprietarioOld, int cdAitOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_proprietario SET cd_ait_proprietario=?,"+
												      		   "cd_ait=?,"+
												      		   "nm_proprietario=?,"+
												      		   "ds_logradouro=?,"+
												      		   "ds_nr_imovel=?,"+
												      		   "nr_cep=?,"+
												      		   "nr_ddd=?,"+
												      		   "nr_telefone=?,"+
												      		   "nr_cpf_proprietario=?,"+
												      		   "tp_pessoa_proprietario=?,"+
												      		   "nr_cpf_cnpj_proprietario=?,"+
												      		   "nm_complemento=?,"+
												      		   "cd_endereco=?,"+
												      		   "cd_proprietario=?,"+
												      		   "nm_proprietario_autuacao=?,"+
												      		   "cd_pessoa=? WHERE cd_ait_proprietario=? AND cd_ait=?");
			pstmt.setInt(1,objeto.getCdAitProprietario());
			pstmt.setInt(2,objeto.getCdAit());
			pstmt.setString(3,objeto.getNmProprietario());
			pstmt.setString(4,objeto.getDsLogradouro());
			pstmt.setString(5,objeto.getDsNrImovel());
			pstmt.setString(6,objeto.getNrCep());
			pstmt.setString(7,objeto.getNrDdd());
			pstmt.setString(8,objeto.getNrTelefone());
			pstmt.setString(9,objeto.getNrCpfProprietario());
			pstmt.setInt(10,objeto.getTpPessoaProprietario());
			pstmt.setString(11,objeto.getNrCpfCnpjProprietario());
			pstmt.setString(12,objeto.getNmComplemento());
			pstmt.setInt(13,objeto.getCdEndereco());
			pstmt.setInt(14,objeto.getCdProprietario());
			pstmt.setString(15,objeto.getNmProprietarioAutuacao());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdPessoa());
			pstmt.setInt(17, cdAitProprietarioOld!=0 ? cdAitProprietarioOld : objeto.getCdAitProprietario());
			pstmt.setInt(18, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitProprietarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitProprietarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAitProprietario, int cdAit) {
		return delete(cdAitProprietario, cdAit, null);
	}

	public static int delete(int cdAitProprietario, int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ait_proprietario WHERE cd_ait_proprietario=? AND cd_ait=?");
			pstmt.setInt(1, cdAitProprietario);
			pstmt.setInt(2, cdAit);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitProprietarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitProprietarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitProprietario get(int cdAitProprietario, int cdAit) {
		return get(cdAitProprietario, cdAit, null);
	}

	public static AitProprietario get(int cdAitProprietario, int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_proprietario WHERE cd_ait_proprietario=? AND cd_ait=?");
			pstmt.setInt(1, cdAitProprietario);
			pstmt.setInt(2, cdAit);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitProprietario(rs.getInt("cd_ait_proprietario"),
						rs.getInt("cd_ait"),
						rs.getString("nm_proprietario"),
						rs.getString("ds_logradouro"),
						rs.getString("ds_nr_imovel"),
						rs.getString("nr_cep"),
						rs.getString("nr_ddd"),
						rs.getString("nr_telefone"),
						rs.getString("nr_cpf_proprietario"),
						rs.getInt("tp_pessoa_proprietario"),
						rs.getString("nr_cpf_cnpj_proprietario"),
						rs.getString("nm_complemento"),
						rs.getInt("cd_endereco"),
						rs.getInt("cd_proprietario"),
						rs.getString("nm_proprietario_autuacao"),
						rs.getInt("cd_pessoa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitProprietarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitProprietarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_proprietario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitProprietarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitProprietarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AitProprietario> getList() {
		return getList(null);
	}

	public static ArrayList<AitProprietario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitProprietario> list = new ArrayList<AitProprietario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AitProprietario obj = AitProprietarioDAO.get(rsm.getInt("cd_ait_proprietario"), rsm.getInt("cd_ait"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitProprietarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_ait_proprietario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
