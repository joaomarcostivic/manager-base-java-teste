package com.tivic.manager.ptc.fici;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ApresentacaoCondutorDAO{

	public static int insert(ApresentacaoCondutor objeto) {
		return insert(objeto, null);
	}

	public static int insert(ApresentacaoCondutor objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ptc_apresentacao_condutor", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdApresentacaoCondutor(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_apresentacao_condutor (cd_apresentacao_condutor,"+
			                                  "cd_documento,"+
			                                  "nr_cnh,"+
			                                  "uf_cnh,"+
			                                  "tp_categoria_cnh,"+
			                                  "nm_condutor,"+
			                                  "ds_endereco,"+
			                                  "ds_complemento_endereco,"+
			                                  "nm_cidade,"+
			                                  "nr_telefone1,"+
			                                  "nr_telefone2,"+
			                                  "nr_cpf_cnpj,"+
			                                  "nr_rg,"+
			                                  "tp_modelo_cnh,"+
			                                  "id_pais_cnh,"+
			                                  "sg_orgao_rg,"+
			                                  "cd_estado_rg,"+
			                                  "nr_endereco) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getCdDocumento());
			if(objeto.getNrCnh() == null)
				pstmt.setNull(3, Types.CHAR);
			else
				pstmt.setString(3,objeto.getNrCnh());
			if(objeto.getUfCnh() == null)
				pstmt.setNull(4, Types.CHAR);
			else
				pstmt.setString(4,objeto.getUfCnh());
			if(objeto.getTpCategoriaCnh() == null) {
				pstmt.setNull(5, Types.INTEGER);
			} else
				pstmt.setInt(5, objeto.getTpCategoriaCnh());
			if(objeto.getNmCondutor() == null)
				pstmt.setNull(6, Types.CHAR);
			else
				pstmt.setString(6, objeto.getNmCondutor());
			if(objeto.getDsEndereco() == null)
				pstmt.setNull(7, Types.CHAR);
			else
				pstmt.setString(7, objeto.getDsEndereco());
			if(objeto.getDsComplementoEndereco() == null)
				pstmt.setNull(8, Types.CHAR);
			else
				pstmt.setString(8, objeto.getDsComplementoEndereco());
			if(objeto.getNmCidade() == null)
				pstmt.setNull(9, Types.CHAR);
			else
				pstmt.setString(9, objeto.getNmCidade());
			if(objeto.getNrTelefone1() == null)
				pstmt.setNull(10, Types.CHAR);
			else
				pstmt.setString(10, objeto.getNrTelefone1());
			if(objeto.getNrTelefone2() == null)
				pstmt.setNull(11, Types.CHAR);
			else
				pstmt.setString(11, objeto.getNrTelefone2());
			if(objeto.getNrCpfCnpj() == null)
				pstmt.setNull(12, Types.CHAR);
			else
				pstmt.setString(12, objeto.getNrCpfCnpj());
			if(objeto.getNrRg() == null)
				pstmt.setNull(13, Types.CHAR);
			else
				pstmt.setString(13, objeto.getNrRg());
			if(objeto.getTpModeloCnh() == 0)
				pstmt.setNull(14, Types.INTEGER);
			else 
				pstmt.setInt(14,objeto.getTpModeloCnh());
			if(objeto.getIdPaisCnh() == null)
				pstmt.setNull(15, Types.CHAR);
			else
				pstmt.setString(15, objeto.getIdPaisCnh());
			if(objeto.getSgOrgaoRg() == null)
				pstmt.setNull(16, Types.CHAR);
			else
				pstmt.setString(16, objeto.getSgOrgaoRg());
			if(objeto.getCdEstadoRg() == 0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17, objeto.getCdEstadoRg());
			if(objeto.getNrEndereco() == null)
				pstmt.setNull(18, Types.CHAR);
			else
				pstmt.setString(18, objeto.getNrEndereco());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ApresentacaoCondutorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ApresentacaoCondutorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull) 
				Conexao.desconectar(connect);
		}
	}

	public static int update(ApresentacaoCondutor objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ApresentacaoCondutor objeto, int cdApresentacaoCondutorOld) {
		return update(objeto, cdApresentacaoCondutorOld, null);
	}

	public static int update(ApresentacaoCondutor objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ApresentacaoCondutor objeto, int cdApresentacaoCondutorOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_apresentacao_condutor SET cd_apresentacao_condutor=?,"+
												      		   "cd_documento=?,"+
												      		   "nr_cnh=?,"+
												      		   "uf_cnh=?,"+
												      		   "tp_categoria_cnh=?,"+
												      		   "nm_condutor=?,"+
												      		   "ds_endereco=?,"+
												      		   "ds_complemento_endereco=?,"+
												      		   "nm_cidade=?,"+
												      		   "nr_telefone1=?,"+
												      		   "nr_telefone2=?,"+
												      		   "nr_cpf_cnpj=?,"+
												      		   "nr_rg=?,"+
												      		   "tp_modelo_cnh=?,"+
												      		   "id_pais_cnh=?,"+
												      		   "sg_orgao_rg=?,"+
												      		   "cd_estado_rg=?,"+
												      		   "nr_endereco=? WHERE cd_apresentacao_condutor=?");
			pstmt.setInt(1,objeto.getCdApresentacaoCondutor());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDocumento());
			pstmt.setString(3,objeto.getNrCnh());
			pstmt.setString(4,objeto.getUfCnh());
			if(objeto.getTpCategoriaCnh() == null)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getTpCategoriaCnh());
			pstmt.setString(6,objeto.getNmCondutor());
			pstmt.setString(7,objeto.getDsEndereco());
			pstmt.setString(8,objeto.getDsComplementoEndereco());
			pstmt.setString(9,objeto.getNmCidade());
			pstmt.setString(10,objeto.getNrTelefone1());
			pstmt.setString(11,objeto.getNrTelefone2());
			pstmt.setString(12,objeto.getNrCpfCnpj());
			pstmt.setString(13,objeto.getNrRg());
			if(objeto.getTpModeloCnh() == 0)
				pstmt.setNull(14, Types.INTEGER);
			else 
				pstmt.setInt(14,objeto.getTpModeloCnh());
			pstmt.setString(15,objeto.getIdPaisCnh());
			pstmt.setString(16,objeto.getSgOrgaoRg());
			pstmt.setInt(17,objeto.getCdEstadoRg());
			pstmt.setString(18, objeto.getNrEndereco());
			pstmt.setInt(19, cdApresentacaoCondutorOld!=0 ? cdApresentacaoCondutorOld : objeto.getCdApresentacaoCondutor());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ApresentacaoCondutorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ApresentacaoCondutorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdApresentacaoCondutor) {
		return delete(cdApresentacaoCondutor, null);
	}

	public static int delete(int cdApresentacaoCondutor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_apresentacao_condutor WHERE cd_apresentacao_condutor=?");
			pstmt.setInt(1, cdApresentacaoCondutor);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ApresentacaoCondutorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ApresentacaoCondutorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ApresentacaoCondutor get(int cdApresentacaoCondutor) {
		return get(cdApresentacaoCondutor, null);
	}

	public static ApresentacaoCondutor get(int cdApresentacaoCondutor, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_apresentacao_condutor WHERE cd_apresentacao_condutor=?");
			pstmt.setInt(1, cdApresentacaoCondutor);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ApresentacaoCondutor(rs.getInt("cd_apresentacao_condutor"),
						rs.getInt("cd_documento"),
						rs.getString("nr_cnh"),
						rs.getString("uf_cnh"),
						rs.getInt("tp_categoria_cnh"),
						rs.getString("nm_condutor"),
						rs.getString("ds_endereco"),
						rs.getString("ds_complemento_endereco"),
						rs.getString("nm_cidade"),
						rs.getString("nr_telefone1"),
						rs.getString("nr_telefone2"),
						rs.getString("nr_cpf_cnpj"),
						rs.getString("nr_rg"),
						rs.getInt("tp_modelo_cnh"),
						rs.getString("id_pais_cnh"),
						rs.getString("sg_orgao_rg"),
						rs.getInt("cd_estado_rg"),
						rs.getString("nr_endereco"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ApresentacaoCondutorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ApresentacaoCondutorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_apresentacao_condutor");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ApresentacaoCondutorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ApresentacaoCondutorDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ApresentacaoCondutor> getList() {
		return getList(null);
	}

	public static ArrayList<ApresentacaoCondutor> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ApresentacaoCondutor> list = new ArrayList<ApresentacaoCondutor>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ApresentacaoCondutor obj = ApresentacaoCondutorDAO.get(rsm.getInt("cd_apresentacao_condutor"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ApresentacaoCondutorDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ptc_apresentacao_condutor", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
