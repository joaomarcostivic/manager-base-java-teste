package com.tivic.manager.str;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class AitDAO{

	public static int insert(Ait objeto) {
		return insert(objeto, null);
	}

	public static int insert(Ait objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("str_ait", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAit(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO str_ait (cd_ait,"+
			                                  "cd_infracao,"+
			                                  "cd_agente,"+
			                                  "cd_usuario,"+
			                                  "cd_especie,"+
			                                  "cd_marca,"+
			                                  "cd_cor,"+
			                                  "cd_tipo,"+
			                                  "dt_infracao,"+
			                                  "cd_categoria,"+
			                                  "ds_observacao,"+
			                                  "ds_local_infracao,"+
			                                  "nr_renavan,"+
			                                  "ds_ano_fabricacao,"+
			                                  "ds_ano_modelo,"+
			                                  "nm_proprietario,"+
			                                  "tp_documento,"+
			                                  "nr_documento,"+
			                                  "nr_ait,"+
			                                  "nm_condutor,"+
			                                  "nr_cnh_condutor,"+
			                                  "uf_cnh_condutor,"+
			                                  "vl_velocidade_permitida,"+
			                                  "vl_velocidade_aferida,"+
			                                  "vl_velocidade_penalidade,"+
			                                  "nr_placa,"+
			                                  "ds_ponto_referencia,"+
			                                  "lg_auto_assinado,"+
			                                  "vl_longitude,"+
			                                  "vl_latitude,"+
			                                  "cd_cidade, "+ 
			                                  "cd_equipamento, "+ 
			                                  "tp_cnh_condutor,"+
			                                  "tp_convenio,"+
			                                  "st_ait,"+
			                                  "cd_logradouro_infracao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdInfracao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInfracao());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAgente());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getCdEspecie()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEspecie());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdMarca());
			if(objeto.getCdCor()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCor());
			if(objeto.getCdTipo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdTipo());
			if(objeto.getDtInfracao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtInfracao().getTimeInMillis()));
			if(objeto.getCdCategoria()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdCategoria());
			pstmt.setString(11,objeto.getDsObservacao());
			pstmt.setString(12,objeto.getDsLocalInfracao());
			pstmt.setLong(13, Long.valueOf(objeto.getNrRenavan()));
			pstmt.setString(14,objeto.getDsAnoFabricacao());
			pstmt.setString(15,objeto.getDsAnoModelo());
			pstmt.setString(16,objeto.getNmProprietario());
			pstmt.setInt(17,objeto.getTpDocumento());
			pstmt.setString(18,objeto.getNrDocumento());
			pstmt.setInt(19,objeto.getNrAit());
			pstmt.setString(20,objeto.getNmCondutor());
			pstmt.setString(21,objeto.getNrCnhCondutor());
			pstmt.setString(22,objeto.getUfCnhCondutor());
			pstmt.setFloat(23,objeto.getVlVelocidadePermitida());
			pstmt.setFloat(24,objeto.getVlVelocidadeAferida());
			pstmt.setFloat(25,objeto.getVlVelocidadePenalidade());
			pstmt.setString(26,objeto.getNrPlaca());
			pstmt.setString(27,objeto.getDsPontoReferencia());
			pstmt.setInt(28,objeto.getLgAutoAssinado());
			pstmt.setFloat(29,objeto.getVlLongitude());
			pstmt.setFloat(30,objeto.getVlLatitude());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(31, Types.INTEGER);
			else
				pstmt.setInt(31,objeto.getCdCidade());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdEquipamento());

			pstmt.setInt(33,objeto.getTpCnhCondutor());

			pstmt.setInt(34,objeto.getTpConvenio());
			
			pstmt.setInt(35,objeto.getStAit());
			
			if(objeto.getCdLogradouroInfracao()==0)
				pstmt.setNull(36, Types.INTEGER);
			else
				pstmt.setInt(36,objeto.getCdLogradouroInfracao());
			
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Ait objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Ait objeto, int cdAitOld) {
		return update(objeto, cdAitOld, null);
	}

	public static int update(Ait objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Ait objeto, int cdAitOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE str_ait SET cd_ait=?,"+
												      		   "cd_infracao=?,"+
												      		   "cd_agente=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_especie=?,"+
												      		   "cd_marca=?,"+
												      		   "cd_cor=?,"+
												      		   "cd_tipo=?,"+
												      		   "dt_infracao=?,"+
												      		   "cd_categoria=?,"+
												      		   "ds_observacao=?,"+
												      		   "ds_local_infracao=?,"+
												      		   "nr_renavan=?,"+
												      		   "ds_ano_fabricacao=?,"+
												      		   "ds_ano_modelo=?,"+
												      		   "nm_proprietario=?,"+
												      		   "tp_documento=?,"+
												      		   "nr_documento=?,"+
												      		   "nr_ait=?,"+
												      		   "nm_condutor=?,"+
												      		   "nr_cnh_condutor=?,"+
												      		   "uf_cnh_condutor=?,"+
												      		   "vl_velocidade_permitida=?,"+
												      		   "vl_velocidade_aferida=?,"+
												      		   "vl_velocidade_penalidade=?,"+
												      		   "nr_placa=?,"+
												      		   "ds_ponto_referencia=?,"+
												      		   "lg_auto_assinado=?,"+
												      		   "vl_longitude=?,"+
												      		   "vl_latitude=?,"+
												      		   "cd_cidade=?,"+
												      		   "cd_equipamento=?,"+
												      		   "tp_cnh_condutor=?,"+
												      		   "tp_convenio=?,"+
												      		   "cd_logradouro_infracao=? WHERE cd_ait=?");
			pstmt.setInt(1,objeto.getCdAit());
			if(objeto.getCdInfracao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInfracao());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAgente());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getCdEspecie()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEspecie());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdMarca());
			if(objeto.getCdCor()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCor());
			if(objeto.getCdTipo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdTipo());
			if(objeto.getDtInfracao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtInfracao().getTimeInMillis()));
			if(objeto.getCdCategoria()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdCategoria());
			pstmt.setString(11,objeto.getDsObservacao());
			pstmt.setString(12,objeto.getDsLocalInfracao());
			pstmt.setLong(13,Long.valueOf(objeto.getNrRenavan()));
			pstmt.setString(14,objeto.getDsAnoFabricacao());
			pstmt.setString(15,objeto.getDsAnoModelo());
			pstmt.setString(16,objeto.getNmProprietario());
			pstmt.setInt(17,objeto.getTpDocumento());
			pstmt.setString(18,objeto.getNrDocumento());
			pstmt.setInt(19,objeto.getNrAit());
			pstmt.setString(20,objeto.getNmCondutor());
			pstmt.setString(21,objeto.getNrCnhCondutor());
			pstmt.setString(22,objeto.getUfCnhCondutor());
			pstmt.setFloat(23,objeto.getVlVelocidadePermitida());
			pstmt.setFloat(24,objeto.getVlVelocidadeAferida());
			pstmt.setFloat(25,objeto.getVlVelocidadePenalidade());
			pstmt.setString(26,objeto.getNrPlaca());
			pstmt.setString(27,objeto.getDsPontoReferencia());
			pstmt.setInt(28,objeto.getLgAutoAssinado());
			pstmt.setFloat(29,objeto.getVlLongitude());
			pstmt.setFloat(30,objeto.getVlLatitude());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(31, Types.INTEGER);
			else
				pstmt.setInt(31,objeto.getCdCidade());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdEquipamento());
			pstmt.setInt(33,objeto.getTpCnhCondutor());
			pstmt.setInt(34,objeto.getTpConvenio());

			if(objeto.getCdLogradouroInfracao()==0)
				pstmt.setNull(35, Types.INTEGER);
			else
				pstmt.setInt(35,objeto.getCdLogradouroInfracao());
			
			pstmt.setInt(35, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	//Update Base Antiga
	
	public static int updateBaseAntiga(Ait objeto) {
		return updateBaseAntiga(objeto, 0, null);
	}

	public static int updateBaseAntiga(Ait objeto, int cdAitOld) {
		return updateBaseAntiga(objeto, cdAitOld, null);
	}

	public static int updateBaseAntiga(Ait objeto, Connection connect) {
		return updateBaseAntiga(objeto, 0, connect);
	}

	public static int updateBaseAntiga(Ait objeto, int cdAitOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ait SET codigo_ait=?,"
												      		  		+ " cod_infracao=?,"
												      		  		+ " cod_agente=?,"
												      		  		+ " cod_usuario=?,"
												      		  		+ " cod_especie=?, "
												      		  		+ " cod_marca=?, "
												      		  		+ " cod_cor=?, "
												      		  		+ " cod_tipo=?, "
												      		  		+ " dt_infracao=?,"
												      		  		+ " cod_categoria=?, "
												      		  		+ " ds_observacao=?,"
												      		  		+ " ds_local_infracao=?,"
												      		  		+ " cd_renavan=?, "
												      		  		+ " ds_ano_fabricacao=?, "
												      		  		+ " ds_ano_modelo=?, "
												      		  		+ " nm_proprietario=?, "
												      		  		+ " tp_documento=?, "
												      		  		+ " nr_documento=?, "
												      		  		+ " nr_ait=?,"
												      		  		+ " nm_condutor=?, "
												      		  		+ " nr_cnh_condutor=?, "
												      		  		+ " uf_cnh_condutor=?, "
												      		  		+ " vl_velocidade_permitida=?,"
												      		  		+ " vl_velocidade_aferida=?,"
												      		  		+ " vl_velocidade_penalidade=?,"
												      		  		+ " nr_placa=?,"
												      		  		+ " ds_ponto_referencia=?,"
												      		  		+ " lg_auto_assinado=?,"
												      		  		+ " vl_longitude=?,"
												      		  		+ " vl_latitude=?,"
												      		  		+ " cod_municipio=?,"
												      		  		+ " cd_equipamento=?,"
												      		  		+ " tp_cnh_condutor=?, "
												      		  		+ " tp_convenio=?, "
												      		  		+ " dt_prazo_defesa=? "
												      		  		+ "		WHERE codigo_ait=?"
												      		   );
												      		   									      		   
			pstmt.setInt(1,objeto.getCdAit());
			if(objeto.getCdInfracao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInfracao());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAgente());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getCdEspecie()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEspecie());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdMarca());
			if(objeto.getCdCor()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCor());
			if(objeto.getCdTipo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdTipo());
			if(objeto.getDtInfracao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtInfracao().getTimeInMillis()));
			if(objeto.getCdCategoria()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdCategoria());
			pstmt.setString(11,objeto.getDsObservacao());
			pstmt.setString(12,objeto.getDsLocalInfracao());
			if(objeto.getNrRenavan() == null)
				pstmt.setLong(13, Types.BIGINT);	
			else
				pstmt.setLong(13,Long.valueOf(objeto.getNrRenavan()));	
			pstmt.setString(14,objeto.getDsAnoFabricacao());
			pstmt.setString(15,objeto.getDsAnoModelo());
			pstmt.setString(16,objeto.getNmProprietario());
			pstmt.setInt(17,objeto.getTpDocumento());
			pstmt.setString(18,objeto.getNrDocumento());
			pstmt.setInt(19,objeto.getNrAit());
			pstmt.setString(20,objeto.getNmCondutor());
			pstmt.setString(21,objeto.getNrCnhCondutor());
			pstmt.setString(22,objeto.getUfCnhCondutor());
			pstmt.setFloat(23,objeto.getVlVelocidadePermitida());
			pstmt.setFloat(24,objeto.getVlVelocidadeAferida());
			pstmt.setFloat(25,objeto.getVlVelocidadePenalidade());
			pstmt.setString(26,objeto.getNrPlaca());
			pstmt.setString(27,objeto.getDsPontoReferencia());
			pstmt.setInt(28,objeto.getLgAutoAssinado());
			pstmt.setFloat(29,objeto.getVlLongitude());
			pstmt.setFloat(30,objeto.getVlLatitude());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(31, Types.INTEGER);
			else
				pstmt.setInt(31,objeto.getCdCidade());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdEquipamento());
			pstmt.setInt(33,objeto.getTpCnhCondutor());
			pstmt.setInt(34,objeto.getTpConvenio());
			if(objeto.getDtPrazoDefesa()  ==null)
				pstmt.setNull(35, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(35,new Timestamp(objeto.getDtPrazoDefesa().getTimeInMillis()));
			pstmt.setInt(36, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	//*******************

	public static int delete(int cdAit) {
		return delete(cdAit, null);
	}

	public static int delete(int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM str_ait WHERE cd_ait=?");
			pstmt.setInt(1, cdAit);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Ait get(int cdAit) {
		return get(cdAit, null);
	}

	public static Ait get(int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM str_ait WHERE cd_ait=?");
			pstmt.setInt(1, cdAit);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Ait(rs.getInt("cd_ait"),
						rs.getInt("cd_infracao"),
						rs.getInt("cd_agente"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_especie"),
						rs.getInt("cd_marca"),
						rs.getInt("cd_cor"),
						rs.getInt("cd_tipo"),
						(rs.getTimestamp("dt_infracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_infracao").getTime()),
						rs.getInt("cd_categoria"),
						rs.getString("ds_observacao"),
						rs.getString("ds_local_infracao"),
						rs.getBigDecimal("nr_renavan").toString(),
						rs.getString("ds_ano_fabricacao"),
						rs.getString("ds_ano_modelo"),
						rs.getString("nm_proprietario"),
						rs.getInt("tp_documento"),
						rs.getString("nr_documento"),
						rs.getInt("nr_ait"),
						rs.getString("nm_condutor"),
						rs.getString("nr_cnh_condutor"),
						rs.getString("uf_cnh_condutor"),
						rs.getFloat("vl_velocidade_permitida"),
						rs.getFloat("vl_velocidade_aferida"),
						rs.getFloat("vl_velocidade_penalidade"),
						rs.getString("nr_placa"),
						rs.getString("ds_ponto_referencia"),
						rs.getInt("lg_auto_assinado"),
						rs.getFloat("vl_longitude"),
						rs.getFloat("vl_latitude"),
						rs.getInt("cd_cidade"),
						rs.getInt("cd_equipamento"),
						rs.getInt("tp_cnh_condutor"),
						rs.getInt("tp_convenio"),
						rs.getInt("st_ait"),
						(rs.getTimestamp("dt_prazo_defesa")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_prazo_defesa").getTime()),
						rs.getInt("cd_logradouro_infracao"),
						null,
						null, 
						null);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	//Teste base antiga
	
	
	public static Ait getBaseOld(int cdAit) {
		return getBaseOld(cdAit, null);
	}

	public static Ait getBaseOld(int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ait WHERE codigo_ait=?");
			pstmt.setInt(1, cdAit);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Ait(rs.getInt("codigo_ait"),
						rs.getInt("cod_infracao"),
						rs.getInt("cod_agente"),
						rs.getInt("cod_usuario"),
						rs.getInt("cod_especie"),
						rs.getInt("cod_marca"),
						rs.getInt("cod_cor"),
						rs.getInt("cod_tipo"),
						(rs.getTimestamp("dt_infracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_infracao").getTime()),
						rs.getInt("cod_categoria"),
						rs.getString("ds_observacao"),
						rs.getString("ds_local_infracao"),
						(rs.getBigDecimal("cd_renavan")==null)? null:rs.getBigDecimal("cd_renavan").toString(),
						rs.getString("ds_ano_fabricacao"),
						rs.getString("ds_ano_modelo"),
						rs.getString("nm_proprietario"),
						rs.getInt("tp_documento"),
						rs.getString("nr_documento"),
						Integer.parseInt(rs.getString("nr_ait").replaceAll("[^\\d]", "")),
						rs.getString("nm_condutor"),
						rs.getString("nr_cnh_condutor"),
						rs.getString("uf_cnh_condutor"),
						rs.getFloat("vl_velocidade_permitida"),
						rs.getFloat("vl_velocidade_aferida"),
						rs.getFloat("vl_velocidade_penalidade"),
						rs.getString("nr_placa"),
						rs.getString("ds_ponto_referencia"),
						rs.getInt("lg_auto_assinado"),
						rs.getFloat("vl_longitude"),
						rs.getFloat("vl_latitude"),
						rs.getInt("cod_municipio"),
						rs.getInt("cd_equipamento"),
						rs.getInt("tp_cnh_condutor"),
						rs.getInt("tp_convenio"),
						rs.getInt("st_ait"),
						(rs.getTimestamp("dt_prazo_defesa")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_prazo_defesa").getTime()),
						0,
						null,
						null, 
						null);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	//----------------

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM str_ait");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Ait> getList() {
		return getList(null);
	}

	public static ArrayList<Ait> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Ait> list = new ArrayList<Ait>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Ait obj = AitDAO.get(rsm.getInt("cd_ait"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitDAO.getList: " + e);
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
		return Search.find("SELECT * FROM str_ait", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
