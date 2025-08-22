package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class BoatVeiculoDAO{

	public static int insert(BoatVeiculo objeto) {
		return insert(objeto, null);
	}

	public static int insert(BoatVeiculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_boat_veiculo");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_boat");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdBoat()));
			int code = Conexao.getSequenceCode("mob_boat_veiculo", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdBoatVeiculo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_boat_veiculo (cd_boat_veiculo,"+
			                                  "cd_boat,"+
			                                  "nm_proprietario,"+
			                                  "tp_documento,"+
			                                  "nr_documento,"+
			                                  "nm_condutor,"+
			                                  "nr_cnh_condutor,"+
			                                  "uf_cnh_condutor,"+
			                                  "nr_placa,"+
			                                  "txt_declaracao_condutor,"+
			                                  "cd_especie,"+
			                                  "cd_marca,"+
			                                  "cd_cidade,"+
			                                  "cd_cor,"+
			                                  "cd_tipo,"+
			                                  "ds_endereco,"+
			                                  "nr_telefone,"+
			                                  "tp_categoria_cnh,"+
			                                  "tp_sexo,"+
			                                  "nr_idade,"+
			                                  "cd_categoria,"+
			                                  "nr_documento_condutor,"+
			                                  "dt_vencimento_cnh,"+
			                                  "dt_primeira_habilitacao,"+
			                                  "cd_veiculo,"+
			                                  "lg_condutor_evadiu,"+
			                                  "nr_telefone_proprietario,"+
			                                  "nr_chassi,"+
			                                  "lg_busca_detran,"+
			                                  "nr_renavam,"+
			                                  "nr_ano_fabricacao,"+
			                                  "lg_dano_dianteira_direita,"+
			                                  "lg_dano_dianteira_esquerda,"+
			                                  "lg_dano_lateral_direita,"+
			                                  "lg_dano_lateral_esquerda,"+
			                                  "lg_dano_traseira_direita,"+
			                                  "lg_dano_traseira_esquerda,"+
			                                  "nr_rg_condutor,"+
			                                  "nm_orgao_rg_condutor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdBoat()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdBoat());
			pstmt.setString(3,objeto.getNmProprietario());
			pstmt.setInt(4,objeto.getTpDocumento());
			pstmt.setString(5,objeto.getNrDocumento());
			pstmt.setString(6,objeto.getNmCondutor());
			pstmt.setString(7,objeto.getNrCnhCondutor());
			pstmt.setString(8,objeto.getUfCnhCondutor());
			pstmt.setString(9,objeto.getNrPlaca());
			pstmt.setString(10,objeto.getTxtDeclaracaoCondutor());
			pstmt.setInt(11,objeto.getCdEspecie());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdMarca());
			pstmt.setInt(13,objeto.getCdCidade());
			if(objeto.getCdCor()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdCor());
			if(objeto.getCdTipo()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdTipo());
			pstmt.setString(16,objeto.getDsEndereco());
			pstmt.setString(17,objeto.getNrTelefone());
			pstmt.setInt(18,objeto.getTpCategoriaCnh());
			pstmt.setInt(19,objeto.getTpSexo());
			pstmt.setInt(20,objeto.getNrIdade());
			if(objeto.getCdCategoria()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdCategoria());
			pstmt.setString(22,objeto.getNrDocumentoCondutor());
			if(objeto.getDtVencimentoCnh()==null)
				pstmt.setNull(23, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(23,new Timestamp(objeto.getDtVencimentoCnh().getTimeInMillis()));
			if(objeto.getDtPrimeiraHabilitacao()==null)
				pstmt.setNull(24, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(24,new Timestamp(objeto.getDtPrimeiraHabilitacao().getTimeInMillis()));
			if(objeto.getCdVeiculo() == 0)
				pstmt.setNull(25,Types.INTEGER);
			else
				pstmt.setInt(25,objeto.getCdVeiculo());
			pstmt.setInt(26,objeto.getLgCondutorEvadiu());
			pstmt.setString(27,objeto.getNrTelefoneProprietario());
			pstmt.setString(28,objeto.getNrChassi());
			pstmt.setInt(29,objeto.getLgBuscaDetran());
			pstmt.setString(30,objeto.getNrRenavam());
			pstmt.setString(31,objeto.getNrAnoFabricacao());
			pstmt.setInt(32,objeto.getLgDanoDianteiraDireita());
			pstmt.setInt(33,objeto.getLgDanoDianteiraEsquerda());
			pstmt.setInt(34,objeto.getLgDanoLateralDireita());
			pstmt.setInt(35,objeto.getLgDanoLateralEsquerda());
			pstmt.setInt(36,objeto.getLgDanoTraseiraDireita());
			pstmt.setInt(37,objeto.getLgDanoTraseiraEsquerda());
			pstmt.setString(38,objeto.getNrRgCondutor());
			pstmt.setString(39,objeto.getNmOrgaoRgCondutor());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatVeiculoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVeiculoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BoatVeiculo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(BoatVeiculo objeto, int cdBoatVeiculoOld, int cdBoatOld) {
		return update(objeto, cdBoatVeiculoOld, cdBoatOld, null);
	}

	public static int update(BoatVeiculo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(BoatVeiculo objeto, int cdBoatVeiculoOld, int cdBoatOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_boat_veiculo SET cd_boat_veiculo=?,"+
												      		   "cd_boat=?,"+
												      		   "nm_proprietario=?,"+
												      		   "tp_documento=?,"+
												      		   "nr_documento=?,"+
												      		   "nm_condutor=?,"+
												      		   "nr_cnh_condutor=?,"+
												      		   "uf_cnh_condutor=?,"+
												      		   "nr_placa=?,"+
												      		   "txt_declaracao_condutor=?,"+
												      		   "cd_especie=?,"+
												      		   "cd_marca=?,"+
												      		   "cd_cidade=?,"+
												      		   "cd_cor=?,"+
												      		   "cd_tipo=?,"+
												      		   "ds_endereco=?,"+
												      		   "nr_telefone=?,"+
												      		   "tp_categoria_cnh=?,"+
												      		   "tp_sexo=?,"+
												      		   "nr_idade=?,"+
												      		   "cd_categoria=?,"+
												      		   "nr_documento_condutor=?,"+
												      		   "dt_vencimento_cnh=?,"+
												      		   "dt_primeira_habilitacao=?,"+
												      		   "cd_veiculo=?,"+
												      		   "lg_condutor_evadiu=?,"+
												      		   "nr_telefone_proprietario=?,"+
												      		   "nr_chassi=?,"+
												      		   "lg_busca_detran=?,"+
												      		   "nr_renavam=?,"+
												      		   "nr_ano_fabricacao=?,"+
												      		   "lg_dano_dianteira_direita=?,"+
												      		   "lg_dano_dianteira_esquerda=?,"+
												      		   "lg_dano_lateral_direita=?,"+
												      		   "lg_dano_lateral_esquerda=?,"+
												      		   "lg_dano_traseira_direita=?,"+
												      		   "lg_dano_traseira_esquerda=?,"+
												      		   "nr_rg_condutor=?,"+
												      		   "nm_orgao_rg_condutor=? WHERE cd_boat_veiculo=? AND cd_boat=?");
			pstmt.setInt(1,objeto.getCdBoatVeiculo());
			pstmt.setInt(2,objeto.getCdBoat());
			pstmt.setString(3,objeto.getNmProprietario());
			pstmt.setInt(4,objeto.getTpDocumento());
			pstmt.setString(5,objeto.getNrDocumento());
			pstmt.setString(6,objeto.getNmCondutor());
			pstmt.setString(7,objeto.getNrCnhCondutor());
			pstmt.setString(8,objeto.getUfCnhCondutor());
			pstmt.setString(9,objeto.getNrPlaca());
			pstmt.setString(10,objeto.getTxtDeclaracaoCondutor());
			pstmt.setInt(11,objeto.getCdEspecie());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdMarca());
			pstmt.setInt(13,objeto.getCdCidade());
			if(objeto.getCdCor()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdCor());
			if(objeto.getCdTipo()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdTipo());
			pstmt.setString(16,objeto.getDsEndereco());
			pstmt.setString(17,objeto.getNrTelefone());
			pstmt.setInt(18,objeto.getTpCategoriaCnh());
			pstmt.setInt(19,objeto.getTpSexo());
			pstmt.setInt(20,objeto.getNrIdade());
			if(objeto.getCdCategoria()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdCategoria());
			pstmt.setString(22,objeto.getNrDocumentoCondutor());
			if(objeto.getDtVencimentoCnh()==null)
				pstmt.setNull(23, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(23,new Timestamp(objeto.getDtVencimentoCnh().getTimeInMillis()));
			if(objeto.getDtPrimeiraHabilitacao()==null)
				pstmt.setNull(24, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(24,new Timestamp(objeto.getDtPrimeiraHabilitacao().getTimeInMillis()));
			if(objeto.getCdVeiculo() == 0)
				pstmt.setNull(25,Types.INTEGER);
			else
				pstmt.setInt(25,objeto.getCdVeiculo());
			pstmt.setInt(26,objeto.getLgCondutorEvadiu());
			pstmt.setString(27,objeto.getNrTelefoneProprietario());
			pstmt.setString(28,objeto.getNrChassi());
			pstmt.setInt(29,objeto.getLgBuscaDetran());
			pstmt.setString(30,objeto.getNrRenavam());
			pstmt.setString(31,objeto.getNrAnoFabricacao());
			pstmt.setInt(32,objeto.getLgDanoDianteiraDireita());
			pstmt.setInt(33,objeto.getLgDanoDianteiraEsquerda());
			pstmt.setInt(34,objeto.getLgDanoLateralDireita());
			pstmt.setInt(35,objeto.getLgDanoLateralEsquerda());
			pstmt.setInt(36,objeto.getLgDanoTraseiraDireita());
			pstmt.setInt(37,objeto.getLgDanoTraseiraEsquerda());
			pstmt.setString(38,objeto.getNrRgCondutor());
			pstmt.setString(39,objeto.getNmOrgaoRgCondutor());
			pstmt.setInt(40, cdBoatVeiculoOld!=0 ? cdBoatVeiculoOld : objeto.getCdBoatVeiculo());
			pstmt.setInt(41, cdBoatOld!=0 ? cdBoatOld : objeto.getCdBoat());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatVeiculoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVeiculoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBoatVeiculo, int cdBoat) {
		return delete(cdBoatVeiculo, cdBoat, null);
	}

	public static int delete(int cdBoatVeiculo, int cdBoat, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_boat_veiculo WHERE cd_boat_veiculo=? AND cd_boat=?");
			pstmt.setInt(1, cdBoatVeiculo);
			pstmt.setInt(2, cdBoat);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatVeiculoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVeiculoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BoatVeiculo get(int cdBoatVeiculo, int cdBoat) {
		return get(cdBoatVeiculo, cdBoat, null);
	}

	public static BoatVeiculo get(int cdBoatVeiculo, int cdBoat, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_veiculo WHERE cd_boat_veiculo=? AND cd_boat=?");
			pstmt.setInt(1, cdBoatVeiculo);
			pstmt.setInt(2, cdBoat);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BoatVeiculo(rs.getInt("cd_boat_veiculo"),
						rs.getInt("cd_boat"),
						rs.getString("nm_proprietario"),
						rs.getInt("tp_documento"),
						rs.getString("nr_documento"),
						rs.getString("nm_condutor"),
						rs.getString("nr_cnh_condutor"),
						rs.getString("uf_cnh_condutor"),
						rs.getString("nr_placa"),
						rs.getString("txt_declaracao_condutor"),
						rs.getInt("cd_especie"),
						rs.getInt("cd_marca"),
						rs.getInt("cd_cidade"),
						rs.getInt("cd_cor"),
						rs.getInt("cd_tipo"),
						rs.getString("ds_endereco"),
						rs.getString("nr_telefone"),
						rs.getInt("tp_categoria_cnh"),
						rs.getInt("tp_sexo"),
						rs.getInt("nr_idade"),
						rs.getInt("cd_categoria"),
						rs.getString("nr_documento_condutor"),
						(rs.getTimestamp("dt_vencimento_cnh")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vencimento_cnh").getTime()),
						(rs.getTimestamp("dt_primeira_habilitacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_primeira_habilitacao").getTime()),
						rs.getInt("cd_veiculo"),
						rs.getInt("lg_condutor_evadiu"),
						rs.getString("nr_telefone_proprietario"),
						rs.getString("nr_chassi"),
						rs.getInt("lg_busca_detran"),
						rs.getString("nr_renavam"),
						rs.getString("nr_ano_fabricacao"),
						rs.getInt("lg_dano_dianteira_direita"),
						rs.getInt("lg_dano_dianteira_esquerda"),
						rs.getInt("lg_dano_lateral_direita"),
						rs.getInt("lg_dano_lateral_esquerda"),
						rs.getInt("lg_dano_traseira_direita"),
						rs.getInt("lg_dano_traseira_esquerda"),
						rs.getString("nr_rg_condutor"),
						rs.getString("nm_orgao_rg_condutor"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatVeiculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVeiculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_veiculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatVeiculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVeiculoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<BoatVeiculo> getList() {
		return getList(null);
	}

	public static ArrayList<BoatVeiculo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<BoatVeiculo> list = new ArrayList<BoatVeiculo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				BoatVeiculo obj = BoatVeiculoDAO.get(rsm.getInt("cd_boat_veiculo"), rsm.getInt("cd_boat"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVeiculoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_boat_veiculo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}