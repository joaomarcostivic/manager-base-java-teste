package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class BoatVitimaDAO{

	public static int insert(BoatVitima objeto) {
		return insert(objeto, null);
	}

	public static int insert(BoatVitima objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_boat_vitima (cd_boat,"+
			                                  "cd_pessoa,"+
			                                  "tp_classificacao,"+
			                                  "tp_natureza_ferimento,"+
			                                  "txt_observacao,"+
			                                  "nm_conducao,"+
			                                  "cd_veiculo,"+
			                                  "qt_idade,"+
			                                  "nr_rg,"+
			                                  "sg_orgao_rg,"+
			                                  "txt_endereco,"+
			                                  "txt_atendente,"+
			                                  "tp_sexo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdBoat()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdBoat());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getTpClassificacao());
			pstmt.setInt(4,objeto.getTpNaturezaFerimento());
			pstmt.setString(5,objeto.getTxtObservacao());
			pstmt.setString(6,objeto.getNmConducao());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdVeiculo());
			pstmt.setInt(8,objeto.getQtIdade());
			pstmt.setString(9,objeto.getNrRg());
			pstmt.setString(10,objeto.getSgOrgaoRg());
			pstmt.setString(11,objeto.getTxtEndereco());
			pstmt.setString(12,objeto.getTxtAtendente());
			pstmt.setInt(13,objeto.getTpSexo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatVitimaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVitimaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BoatVitima objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(BoatVitima objeto, int cdBoatOld, int cdPessoaOld) {
		return update(objeto, cdBoatOld, cdPessoaOld, null);
	}

	public static int update(BoatVitima objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(BoatVitima objeto, int cdBoatOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_boat_vitima SET cd_boat=?,"+
												      		   "cd_pessoa=?,"+
												      		   "tp_classificacao=?,"+
												      		   "tp_natureza_ferimento=?,"+
												      		   "txt_observacao=?,"+
												      		   "nm_conducao=?,"+
												      		   "cd_veiculo=?,"+
												      		   "qt_idade=?,"+
												      		   "nr_rg=?,"+
												      		   "sg_orgao_rg=?,"+
												      		   "txt_endereco=?,"+
												      		   "txt_atendente=?,"+
												      		   "tp_sexo=? WHERE cd_boat=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdBoat());
			pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getTpClassificacao());
			pstmt.setInt(4,objeto.getTpNaturezaFerimento());
			pstmt.setString(5,objeto.getTxtObservacao());
			pstmt.setString(6,objeto.getNmConducao());
			if(objeto.getCdBoatVeiculo()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdVeiculo());
			pstmt.setInt(8,objeto.getQtIdade());
			pstmt.setString(9,objeto.getNrRg());
			pstmt.setString(10,objeto.getSgOrgaoRg());
			pstmt.setString(11,objeto.getTxtEndereco());
			pstmt.setString(12,objeto.getTxtAtendente());
			pstmt.setInt(13,objeto.getTpSexo());
			pstmt.setInt(14, cdBoatOld!=0 ? cdBoatOld : objeto.getCdBoat());
			pstmt.setInt(15, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatVitimaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVitimaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBoat, int cdPessoa) {
		return delete(cdBoat, cdPessoa, null);
	}

	public static int delete(int cdBoat, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_boat_vitima WHERE cd_boat=? AND cd_pessoa=?");
			pstmt.setInt(1, cdBoat);
			pstmt.setInt(2, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatVitimaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVitimaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BoatVitima get(int cdBoat, int cdPessoa) {
		return get(cdBoat, cdPessoa, null);
	}

	public static BoatVitima get(int cdBoat, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_vitima WHERE cd_boat=? AND cd_pessoa=?");
			pstmt.setInt(1, cdBoat);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BoatVitima(rs.getInt("cd_boat"),
						rs.getInt("cd_veiculo"),
						rs.getInt("cd_pessoa"),
						rs.getInt("tp_classificacao"),
						rs.getInt("tp_natureza_ferimento"),
						rs.getString("txt_observacao"),
						rs.getString("nm_conducao"),
						rs.getInt("cd_veiculo"),
						rs.getInt("qt_idade"),
						rs.getString("nr_rg"),
						rs.getString("sg_orgao_rg"),
						rs.getString("txt_endereco"),
						rs.getString("txt_atendente"),
						rs.getInt("tp_sexo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatVitimaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVitimaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_vitima");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatVitimaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVitimaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<BoatVitima> getList() {
		return getList(null);
	}

	public static ArrayList<BoatVitima> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<BoatVitima> list = new ArrayList<BoatVitima>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				BoatVitima obj = BoatVitimaDAO.get(rsm.getInt("cd_boat"), rsm.getInt("cd_pessoa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVitimaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_boat_vitima", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
