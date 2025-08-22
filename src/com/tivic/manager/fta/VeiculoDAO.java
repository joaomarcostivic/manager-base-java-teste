package com.tivic.manager.fta;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class VeiculoDAO{

	public static int insert(Veiculo objeto) {
		return insert(objeto, null);
	}

	public static int insert(Veiculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("bpm_referencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdVeiculo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_veiculo (cd_veiculo,"+
			                                  "cd_proprietario,"+
			                                  "cd_modelo,"+
			                                  "cd_tipo_veiculo,"+
			                                  "cd_reboque,"+
			                                  "nr_placa,"+
			                                  "nr_ano_fabricacao,"+
			                                  "nr_ano_modelo,"+
			                                  "nm_cor,"+
			                                  "nr_portas,"+
			                                  "nr_chassi,"+
			                                  "nr_renavam,"+
			                                  "nr_hodometro_inicial,"+
			                                  "tp_combustivel,"+
			                                  "nr_capacidade,"+
			                                  "nr_potencia,"+
			                                  "nr_cilindrada,"+
			                                  "nr_tabela_referencia,"+
			                                  "txt_observacao,"+
			                                  "qt_capacidade_tanque,"+
			                                  "qt_consumo_urbano,"+
			                                  "qt_consumo_rodoviario,"+
			                                  "tp_eixo_dianteiro,"+
			                                  "tp_eixo_traseiro,"+
			                                  "qt_eixos_dianteiros,"+
			                                  "qt_eixos_traseiros,"+
			                                  "st_veiculo,"+
			                                  "qt_hodometro_atual,"+
			                                  "cd_cor,"+
			                                  "cd_tipo,"+
			                                  "cd_categoria,"+
			                                  "cd_marca,"+
			                                  "cd_especie,"+
			                                  "cd_endereco,"+
			                                  "cd_cidade,"+
			                                  "cd_frota,"+
			                                  "tp_adaptacao,"+
			                                  "tp_utilizacao,"+
			                                  "cd_marca_carroceria,"+
			                                  "nr_ano_chassi,"+
			                                  "nr_ano_carroceria,"+
			                                  "nm_prefixo,"+
			                                  "nr_ano_licenciamento,"+
			                                  "qt_entre_eixos,"+
			                                  "qt_comprimento_interno,"+
			                                  "qt_largura_interna,"+
			                                  "qt_largura_corredor,"+
			                                  "qt_area_corredor,"+
			                                  "qt_max_em_pe,"+
			                                  "nr_licenciamento,"+
			                                  "cd_plano_vistoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdProprietario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProprietario());
			if(objeto.getCdModelo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdModelo());
			if(objeto.getCdTipoVeiculo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoVeiculo());
			if(objeto.getCdReboque()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdReboque());
			pstmt.setString(6,objeto.getNrPlaca());
			pstmt.setString(7,objeto.getNrAnoFabricacao());
			pstmt.setString(8,objeto.getNrAnoModelo());
			pstmt.setString(9,objeto.getNmCor());
			pstmt.setInt(10,objeto.getNrPortas());
			pstmt.setString(11,objeto.getNrChassi());
			pstmt.setString(12,objeto.getNrRenavam());
			pstmt.setInt(13,objeto.getNrHodometroInicial());
			pstmt.setInt(14,objeto.getTpCombustivel());
			pstmt.setString(15,objeto.getNrCapacidade());
			pstmt.setInt(16,objeto.getNrPotencia());
			pstmt.setInt(17,objeto.getNrCilindrada());
			pstmt.setString(18,objeto.getNrTabelaReferencia());
			pstmt.setString(19,objeto.getTxtObservacao());
			pstmt.setInt(20,objeto.getQtCapacidadeTanque());
			pstmt.setFloat(21,objeto.getQtConsumoUrbano());
			pstmt.setFloat(22,objeto.getQtConsumoRodoviario());
			pstmt.setInt(23,objeto.getTpEixoDianteiro());
			pstmt.setInt(24,objeto.getTpEixoTraseiro());
			pstmt.setInt(25,objeto.getQtEixosDianteiros());
			pstmt.setInt(26,objeto.getQtEixosTraseiros());
			pstmt.setInt(27,objeto.getStVeiculo());
			pstmt.setFloat(28,objeto.getQtHodometroAtual());
			if(objeto.getCdCor()==0)
				pstmt.setNull(29, Types.INTEGER);
			else
				pstmt.setInt(29,objeto.getCdCor());
			pstmt.setInt(30,objeto.getCdTipo());
			if(objeto.getCdCategoria()==0)
				pstmt.setNull(31, Types.INTEGER);
			else
				pstmt.setInt(31,objeto.getCdCategoria());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdMarca());
			if(objeto.getCdEspecie()==0)
				pstmt.setNull(33, Types.INTEGER);
			else
				pstmt.setInt(33,objeto.getCdEspecie());
			if(objeto.getCdEndereco()==0)
				pstmt.setNull(34, Types.INTEGER);
			else
				pstmt.setInt(34,objeto.getCdEndereco());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(35, Types.INTEGER);
			else
				pstmt.setInt(35,objeto.getCdCidade());
			if(objeto.getCdFrota()==0)
				pstmt.setNull(36, Types.INTEGER);
			else
				pstmt.setInt(36,objeto.getCdFrota());
			pstmt.setInt(37,objeto.getTpAdaptacao());
			pstmt.setInt(38,objeto.getTpUtilizacao());
			if(objeto.getCdMarcaCarroceria()==0)
				pstmt.setNull(39, Types.INTEGER);
			else
				pstmt.setInt(39,objeto.getCdMarcaCarroceria());
			pstmt.setString(40,objeto.getNrAnoChassi());
			pstmt.setString(41,objeto.getNrAnoCarroceria());
			pstmt.setString(42,objeto.getNmPrefixo());
			pstmt.setString(43,objeto.getNrAnoLicenciamento());
			pstmt.setFloat(44,objeto.getQtEntreEixos());
			pstmt.setFloat(45,objeto.getQtComprimentoInterno());
			pstmt.setFloat(46,objeto.getQtLarguraInterna());
			pstmt.setFloat(47,objeto.getQtLarguraCorredor());
			pstmt.setFloat(48,objeto.getQtAreaCorredor());
			pstmt.setInt(49,objeto.getQtMaxEmPe());
			pstmt.setString(50, objeto.getNrLicenciamento());
			if(objeto.getCdPlanoVistoria()==0)
				pstmt.setNull(51, Types.INTEGER);
			else
				pstmt.setInt(51,objeto.getCdPlanoVistoria());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Veiculo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Veiculo objeto, int cdVeiculoOld) {
		return update(objeto, cdVeiculoOld, null);
	}

	public static int update(Veiculo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Veiculo objeto, int cdVeiculoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			pstmt = connect.prepareStatement("UPDATE fta_veiculo SET cd_veiculo=?,"+
												      		   "cd_proprietario=?,"+
												      		   "cd_modelo=?,"+
												      		   "cd_tipo_veiculo=?,"+
												      		   "cd_reboque=?,"+
												      		   "nr_placa=?,"+
												      		   "nr_ano_fabricacao=?,"+
												      		   "nr_ano_modelo=?,"+
												      		   "nm_cor=?,"+
												      		   "nr_portas=?,"+
												      		   "nr_chassi=?,"+
												      		   "nr_renavam=?,"+
												      		   "nr_hodometro_inicial=?,"+
												      		   "tp_combustivel=?,"+
												      		   "nr_capacidade=?,"+
												      		   "nr_potencia=?,"+
												      		   "nr_cilindrada=?,"+
												      		   "nr_tabela_referencia=?,"+
												      		   "txt_observacao=?,"+
												      		   "qt_capacidade_tanque=?,"+
												      		   "qt_consumo_urbano=?,"+
												      		   "qt_consumo_rodoviario=?,"+
												      		   "tp_eixo_dianteiro=?,"+
												      		   "tp_eixo_traseiro=?,"+
												      		   "qt_eixos_dianteiros=?,"+
												      		   "qt_eixos_traseiros=?,"+
												      		   "st_veiculo=?,"+
												      		   "qt_hodometro_atual=?,"+
												      		   "cd_cor=?,"+
												      		   "cd_tipo=?,"+
												      		   "cd_categoria=?,"+
												      		   "cd_marca=?,"+
												      		   "cd_especie=?,"+
												      		   "cd_endereco=?,"+
												      		   "cd_cidade=?,"+
												      		   "cd_frota=?,"+
												      		   "tp_adaptacao=?,"+
												      		   "tp_utilizacao=?,"+
												      		   "cd_marca_carroceria=?,"+
												      		   "nr_ano_chassi=?,"+
												      		   "nr_ano_carroceria=?,"+
												      		   "nm_prefixo=?,"+
												      		   "nr_ano_licenciamento=?,"+
												      		   "qt_entre_eixos=?,"+
												      		   "qt_comprimento_interno=?,"+
												      		   "qt_largura_interna=?,"+
												      		   "qt_largura_corredor=?,"+
												      		   "qt_area_corredor=?,"+
												      		   "qt_max_em_pe=?, "+
												      		   "nr_licenciamento=?, "+
												      		   "cd_plano_vistoria=? WHERE cd_veiculo=?");
			pstmt.setInt(1,objeto.getCdVeiculo());
			if(objeto.getCdProprietario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProprietario());
			if(objeto.getCdModelo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdModelo());
			if(objeto.getCdTipoVeiculo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoVeiculo());
			if(objeto.getCdReboque()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdReboque());
			pstmt.setString(6,objeto.getNrPlaca());
			pstmt.setString(7,objeto.getNrAnoFabricacao());
			pstmt.setString(8,objeto.getNrAnoModelo());
			pstmt.setString(9,objeto.getNmCor());
			pstmt.setInt(10,objeto.getNrPortas());
			pstmt.setString(11,objeto.getNrChassi());
			pstmt.setString(12,objeto.getNrRenavam());
			pstmt.setInt(13,objeto.getNrHodometroInicial());
			pstmt.setInt(14,objeto.getTpCombustivel());
			pstmt.setString(15,objeto.getNrCapacidade());
			pstmt.setInt(16,objeto.getNrPotencia());
			pstmt.setInt(17,objeto.getNrCilindrada());
			pstmt.setString(18,objeto.getNrTabelaReferencia());
			pstmt.setString(19,objeto.getTxtObservacao());
			pstmt.setInt(20,objeto.getQtCapacidadeTanque());
			pstmt.setFloat(21,objeto.getQtConsumoUrbano());
			pstmt.setFloat(22,objeto.getQtConsumoRodoviario());
			pstmt.setInt(23,objeto.getTpEixoDianteiro());
			pstmt.setInt(24,objeto.getTpEixoTraseiro());
			pstmt.setInt(25,objeto.getQtEixosDianteiros());
			pstmt.setInt(26,objeto.getQtEixosTraseiros());
			pstmt.setInt(27,objeto.getStVeiculo());
			pstmt.setFloat(28,objeto.getQtHodometroAtual());
			if(objeto.getCdCor()==0)
				pstmt.setNull(29, Types.INTEGER);
			else
				pstmt.setInt(29,objeto.getCdCor());
			pstmt.setInt(30,objeto.getCdTipo());
			if(objeto.getCdCategoria()==0)
				pstmt.setNull(31, Types.INTEGER);
			else
				pstmt.setInt(31,objeto.getCdCategoria());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdMarca());
			if(objeto.getCdEspecie()==0)
				pstmt.setNull(33, Types.INTEGER);
			else
				pstmt.setInt(33,objeto.getCdEspecie());
			if(objeto.getCdEndereco()==0)
				pstmt.setNull(34, Types.INTEGER);
			else
				pstmt.setInt(34,objeto.getCdEndereco());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(35, Types.INTEGER);
			else
				pstmt.setInt(35,objeto.getCdCidade());
			if(objeto.getCdFrota()==0)
				pstmt.setNull(36, Types.INTEGER);
			else
				pstmt.setInt(36,objeto.getCdFrota());
			pstmt.setInt(37,objeto.getTpAdaptacao());
			pstmt.setInt(38,objeto.getTpUtilizacao());
			if(objeto.getCdMarcaCarroceria()==0)
				pstmt.setNull(39, Types.INTEGER);
			else
				pstmt.setInt(39,objeto.getCdMarcaCarroceria());
			pstmt.setString(40,objeto.getNrAnoChassi());
			pstmt.setString(41,objeto.getNrAnoCarroceria());
			pstmt.setString(42,objeto.getNmPrefixo());
			pstmt.setString(43,objeto.getNrAnoLicenciamento());
			pstmt.setFloat(44,objeto.getQtEntreEixos());
			pstmt.setFloat(45,objeto.getQtComprimentoInterno());
			pstmt.setFloat(46,objeto.getQtLarguraInterna());
			pstmt.setFloat(47,objeto.getQtLarguraCorredor());
			pstmt.setFloat(48,objeto.getQtAreaCorredor());
			pstmt.setInt(49,objeto.getQtMaxEmPe());
			pstmt.setString(50, objeto.getNrLicenciamento());
			if(objeto.getCdPlanoVistoria()==0)
				pstmt.setNull(51, Types.INTEGER);
			else
				pstmt.setInt(51,objeto.getCdPlanoVistoria());
			pstmt.setInt(52, cdVeiculoOld!=0 ? cdVeiculoOld : objeto.getCdVeiculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdVeiculo) {
		return delete(cdVeiculo, null);
	}

	public static int delete(int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_veiculo WHERE cd_veiculo=?");
			pstmt.setInt(1, cdVeiculo);
			pstmt.executeUpdate();
			if (com.tivic.manager.bpm.ReferenciaDAO.delete(cdVeiculo, connect)<=0) {
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
			System.err.println("Erro! VeiculoDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Veiculo get(int cdVeiculo) {
		return get(cdVeiculo, null);
	}

	public static Veiculo get(int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement(""
					+ "SELECT * FROM fta_veiculo A "
					+ "LEFT OUTER JOIN bpm_referencia B ON (A.cd_veiculo=B.cd_referencia) "
					+ "WHERE A.cd_veiculo=? ");
			pstmt.setInt(1, cdVeiculo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Veiculo(rs.getInt("cd_veiculo"),
						rs.getInt("cd_proprietario"),
						rs.getInt("cd_modelo"),
						rs.getInt("cd_tipo_veiculo"),
						rs.getInt("cd_reboque"),
						rs.getString("nr_placa"),
						rs.getString("nr_ano_fabricacao"),
						rs.getString("nr_ano_modelo"),
						rs.getString("nm_cor"),
						rs.getInt("nr_portas"),
						rs.getString("nr_chassi"),
						rs.getString("nr_renavam"),
						rs.getInt("nr_hodometro_inicial"),
						rs.getInt("tp_combustivel"),
						rs.getString("nr_capacidade"),
						rs.getInt("nr_potencia"),
						rs.getInt("nr_cilindrada"),
						rs.getString("nr_tabela_referencia"),
						rs.getString("txt_observacao"),
						rs.getInt("qt_capacidade_tanque"),
						rs.getFloat("qt_consumo_urbano"),
						rs.getFloat("qt_consumo_rodoviario"),
						rs.getInt("tp_eixo_dianteiro"),
						rs.getInt("tp_eixo_traseiro"),
						rs.getInt("qt_eixos_dianteiros"),
						rs.getInt("qt_eixos_traseiros"),
						rs.getInt("st_veiculo"),
						rs.getFloat("qt_hodometro_atual"),
						rs.getInt("cd_cor"),
						rs.getInt("cd_tipo"),
						rs.getInt("cd_categoria"),
						rs.getInt("cd_marca"),
						rs.getInt("cd_especie"),
						rs.getInt("cd_endereco"),
						rs.getInt("cd_cidade"),
						rs.getInt("cd_frota"),
						rs.getInt("tp_adaptacao"),
						rs.getInt("tp_utilizacao"),
						rs.getInt("cd_marca_carroceria"),
						rs.getString("nr_ano_chassi"),
						rs.getString("nr_ano_carroceria"),
						rs.getString("nm_prefixo"),
						rs.getString("nr_ano_licenciamento"),
						rs.getFloat("qt_entre_eixos"),
						rs.getFloat("qt_comprimento_interno"),
						rs.getFloat("qt_largura_interna"),
						rs.getFloat("qt_largura_corredor"),
						rs.getFloat("qt_area_corredor"),
						rs.getInt("qt_max_em_pe"),
						rs.getString("nr_licenciamento"),
						rs.getInt("cd_plano_vistoria"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_veiculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Veiculo> getList() {
		return getList(null);
	}

	public static ArrayList<Veiculo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Veiculo> list = new ArrayList<Veiculo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Veiculo obj = VeiculoDAO.get(rsm.getInt("cd_veiculo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM fta_veiculo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
