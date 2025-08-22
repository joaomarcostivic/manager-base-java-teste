package com.tivic.manager.str;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class ProcessoRequerenteDAO{

	public static int insert(ProcessoRequerente objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProcessoRequerente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_requerente");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_processo");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdProcesso()));
			int code = Conexao.getSequenceCode("str_processo_requerente", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRequerente(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO str_processo_requerente (cd_requerente,"+
			                                  "cd_processo,"+
			                                  "tp_requerente,"+
			                                  "nm_requerente,"+
			                                  "ds_endereco_requerente,"+
			                                  "nr_endereco_requerente,"+
			                                  "ds_complemento_endereco_requerente,"+
			                                  "nm_cidade_requerente,"+
			                                  "nr_telefone1_requerente,"+
			                                  "nr_telefone2_requerente,"+
			                                  "nr_cpf_requerente,"+
			                                  "nr_cnpj_requerente,"+
			                                  "nr_rg_requerente,"+
			                                  "nr_cnh_requerente,"+
			                                  "uf_cnh_requerente,"+
			                                  "nm_condutor,"+
			                                  "ds_endereco_condutor,"+
			                                  "nr_endereco_condutor,"+
			                                  "ds_complemento_endereco_condutor,"+
			                                  "nm_cidade_condutor,"+
			                                  "nr_telefone1_condutor,"+
			                                  "nr_telefone2_condutor,"+
			                                  "nr_cpf_condutor,"+
			                                  "nr_cnpj_condutor,"+
			                                  "nr_rg_condutor,"+
			                                  "nr_cnh_condutor,"+
			                                  "uf_cnh_condutor,"+
			                                  "txt_requisicao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProcesso());
			pstmt.setInt(3,objeto.getTpRequerente());
			pstmt.setString(4,objeto.getNmRequerente());
			pstmt.setString(5,objeto.getDsEnderecoRequerente());
			pstmt.setString(6,objeto.getNrEnderecoRequerente());
			pstmt.setString(7,objeto.getDsComplementoEnderecoRequerente());
			pstmt.setString(8,objeto.getNmCidadeRequerente());
			pstmt.setString(9,objeto.getNrTelefone1Requerente());
			pstmt.setString(10,objeto.getNrTelefone2Requerente());
			pstmt.setString(11,objeto.getNrCpfRequerente());
			pstmt.setString(12,objeto.getNrCnpjRequerente());
			pstmt.setString(13,objeto.getNrRgRequerente());
			pstmt.setString(14,objeto.getNrCnhRequerente());
			pstmt.setString(15,objeto.getUfCnhRequerente());
			pstmt.setString(16,objeto.getNmCondutor());
			pstmt.setString(17,objeto.getDsEnderecoCondutor());
			pstmt.setString(18,objeto.getNrEnderecoCondutor());
			pstmt.setString(19,objeto.getDsComplementoEnderecoCondutor());
			pstmt.setString(20,objeto.getNmCidadeCondutor());
			pstmt.setString(21,objeto.getNrTelefone1Condutor());
			pstmt.setString(22,objeto.getNrTelefone2Condutor());
			pstmt.setString(23,objeto.getNrCpfCondutor());
			pstmt.setString(24,objeto.getNrCnpjCondutor());
			pstmt.setString(25,objeto.getNrRgCondutor());
			pstmt.setString(26,objeto.getNrCnhCondutor());
			pstmt.setString(27,objeto.getUfCnhCondutor());
			pstmt.setString(28,objeto.getTxtRequisicao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoRequerenteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoRequerenteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProcessoRequerente objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ProcessoRequerente objeto, int cdRequerenteOld, int cdProcessoOld) {
		return update(objeto, cdRequerenteOld, cdProcessoOld, null);
	}

	public static int update(ProcessoRequerente objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ProcessoRequerente objeto, int cdRequerenteOld, int cdProcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE str_processo_requerente SET cd_requerente=?,"+
												      		   "cd_processo=?,"+
												      		   "tp_requerente=?,"+
												      		   "nm_requerente=?,"+
												      		   "ds_endereco_requerente=?,"+
												      		   "nr_endereco_requerente=?,"+
												      		   "ds_complemento_endereco_requerente=?,"+
												      		   "nm_cidade_requerente=?,"+
												      		   "nr_telefone1_requerente=?,"+
												      		   "nr_telefone2_requerente=?,"+
												      		   "nr_cpf_requerente=?,"+
												      		   "nr_cnpj_requerente=?,"+
												      		   "nr_rg_requerente=?,"+
												      		   "nr_cnh_requerente=?,"+
												      		   "uf_cnh_requerente=?,"+
												      		   "nm_condutor=?,"+
												      		   "ds_endereco_condutor=?,"+
												      		   "nr_endereco_condutor=?,"+
												      		   "ds_complemento_endereco_condutor=?,"+
												      		   "nm_cidade_condutor=?,"+
												      		   "nr_telefone1_condutor=?,"+
												      		   "nr_telefone2_condutor=?,"+
												      		   "nr_cpf_condutor=?,"+
												      		   "nr_cnpj_condutor=?,"+
												      		   "nr_rg_condutor=?,"+
												      		   "nr_cnh_condutor=?,"+
												      		   "uf_cnh_condutor=?,"+
												      		   "txt_requisicao=? WHERE cd_requerente=? AND cd_processo=?");
			pstmt.setInt(1,objeto.getCdRequerente());
			pstmt.setInt(2,objeto.getCdProcesso());
			pstmt.setInt(3,objeto.getTpRequerente());
			pstmt.setString(4,objeto.getNmRequerente());
			pstmt.setString(5,objeto.getDsEnderecoRequerente());
			pstmt.setString(6,objeto.getNrEnderecoRequerente());
			pstmt.setString(7,objeto.getDsComplementoEnderecoRequerente());
			pstmt.setString(8,objeto.getNmCidadeRequerente());
			pstmt.setString(9,objeto.getNrTelefone1Requerente());
			pstmt.setString(10,objeto.getNrTelefone2Requerente());
			pstmt.setString(11,objeto.getNrCpfRequerente());
			pstmt.setString(12,objeto.getNrCnpjRequerente());
			pstmt.setString(13,objeto.getNrRgRequerente());
			pstmt.setString(14,objeto.getNrCnhRequerente());
			pstmt.setString(15,objeto.getUfCnhRequerente());
			pstmt.setString(16,objeto.getNmCondutor());
			pstmt.setString(17,objeto.getDsEnderecoCondutor());
			pstmt.setString(18,objeto.getNrEnderecoCondutor());
			pstmt.setString(19,objeto.getDsComplementoEnderecoCondutor());
			pstmt.setString(20,objeto.getNmCidadeCondutor());
			pstmt.setString(21,objeto.getNrTelefone1Condutor());
			pstmt.setString(22,objeto.getNrTelefone2Condutor());
			pstmt.setString(23,objeto.getNrCpfCondutor());
			pstmt.setString(24,objeto.getNrCnpjCondutor());
			pstmt.setString(25,objeto.getNrRgCondutor());
			pstmt.setString(26,objeto.getNrCnhCondutor());
			pstmt.setString(27,objeto.getUfCnhCondutor());
			pstmt.setString(28,objeto.getTxtRequisicao());
			pstmt.setInt(29, cdRequerenteOld!=0 ? cdRequerenteOld : objeto.getCdRequerente());
			pstmt.setInt(30, cdProcessoOld!=0 ? cdProcessoOld : objeto.getCdProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoRequerenteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoRequerenteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRequerente, int cdProcesso) {
		return delete(cdRequerente, cdProcesso, null);
	}

	public static int delete(int cdRequerente, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM str_processo_requerente WHERE cd_requerente=? AND cd_processo=?");
			pstmt.setInt(1, cdRequerente);
			pstmt.setInt(2, cdProcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoRequerenteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoRequerenteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProcessoRequerente get(int cdRequerente, int cdProcesso) {
		return get(cdRequerente, cdProcesso, null);
	}

	public static ProcessoRequerente get(int cdRequerente, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM str_processo_requerente WHERE cd_requerente=? AND cd_processo=?");
			pstmt.setInt(1, cdRequerente);
			pstmt.setInt(2, cdProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProcessoRequerente(rs.getInt("cd_requerente"),
						rs.getInt("cd_processo"),
						rs.getInt("tp_requerente"),
						rs.getString("nm_requerente"),
						rs.getString("ds_endereco_requerente"),
						rs.getString("nr_endereco_requerente"),
						rs.getString("ds_complemento_endereco_requerente"),
						rs.getString("nm_cidade_requerente"),
						rs.getString("nr_telefone1_requerente"),
						rs.getString("nr_telefone2_requerente"),
						rs.getString("nr_cpf_requerente"),
						rs.getString("nr_cnpj_requerente"),
						rs.getString("nr_rg_requerente"),
						rs.getString("nr_cnh_requerente"),
						rs.getString("uf_cnh_requerente"),
						rs.getString("nm_condutor"),
						rs.getString("ds_endereco_condutor"),
						rs.getString("nr_endereco_condutor"),
						rs.getString("ds_complemento_endereco_condutor"),
						rs.getString("nm_cidade_condutor"),
						rs.getString("nr_telefone1_condutor"),
						rs.getString("nr_telefone2_condutor"),
						rs.getString("nr_cpf_condutor"),
						rs.getString("nr_cnpj_condutor"),
						rs.getString("nr_rg_condutor"),
						rs.getString("nr_cnh_condutor"),
						rs.getString("uf_cnh_condutor"),
						rs.getString("txt_requisicao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoRequerenteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoRequerenteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM str_processo_requerente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoRequerenteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoRequerenteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProcessoRequerente> getList() {
		return getList(null);
	}

	public static ArrayList<ProcessoRequerente> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProcessoRequerente> list = new ArrayList<ProcessoRequerente>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProcessoRequerente obj = ProcessoRequerenteDAO.get(rsm.getInt("cd_requerente"), rsm.getInt("cd_processo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoRequerenteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM str_processo_requerente", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
