package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class TrravDAO{

	public static int insert(Trrav objeto) {
		return insert(objeto, null);
	}

	public static int insert(Trrav objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_trrav", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTrrav(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_trrav (cd_trrav,"+
			                                  "nr_trrav,"+
			                                  "dt_ocorrencia,"+
			                                  "cd_usuario,"+
			                                  "cd_agente,"+
			                                  "cd_cidade,"+
			                                  "ds_observacao,"+
			                                  "ds_local_ocorrencia,"+
			                                  "ds_ponto_referencia,"+
			                                  "vl_latitude,"+
			                                  "vl_longitude,"+
			                                  "cd_veiculo,"+
			                                  "nr_placa,"+
			                                  "tp_documento,"+
			                                  "nr_documento,"+
			                                  "nm_condutor,"+
			                                  "nr_cnh_condutor,"+
			                                  "nm_proprietario,"+
			                                  "cd_local_remocao,"+
			                                  "cd_motivo_remocao,"+
			                                  "txt_objetos,"+
			                                  "cd_boat,"+
			                                  "cd_tipo_remocao,"+
			                                  "nm_recebedor,"+
			                                  "rg_recebedor,"+
			                                  "dt_recebimento,"+
			                                  "cd_cidade_condutor,"+
			                                  "cd_cidade_proprietario,"+
			                                  "uf_condutor,"+
			                                  "uf_proprietario,"+
			                                  "endereco_condutor,"+
			                                  "endereco_proprietario,"+
			                                  "bairro_condutor,"+
			                                  "bairro_proprietario,"+
			                                  "vl_hodometro,"+
			                                  "nr_cnh_prorietario,"+
			                                  "cd_categoria_cnh_proprietario,"+
			                                  "lg_cnh_vencida_proprietario,"+
			                                  "cd_categoria_cnh_condutor,"+
			                                  "lg_cnh_vencida_condutor,"+
			                                  "nr_guia_exame,"+
			                                  "nm_delegacia_policia,"+
			                                  "ds_motivo_recolhimento_documentos,"+
			                                  "nm_patio_destino,"+
			                                  "ds_motivo_recolhimento,"+
			                                  "cd_meio_remocao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getNrTrrav());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAgente());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCidade());
			pstmt.setString(7,objeto.getDsObservacao());
			pstmt.setString(8,objeto.getDsLocalOcorrencia());
			pstmt.setString(9,objeto.getDsPontoReferencia());
			pstmt.setDouble(10,objeto.getVlLatitude());
			pstmt.setDouble(11,objeto.getVlLongitude());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdVeiculo());
			pstmt.setString(13,objeto.getNrPlaca());
			pstmt.setInt(14,objeto.getTpDocumento());
			pstmt.setString(15,objeto.getNrDocumento());
			pstmt.setString(16,objeto.getNmCondutor());
			pstmt.setString(17,objeto.getNrCnhCondutor());
			pstmt.setString(18,objeto.getNmProprietario());
			if(objeto.getCdLocalRemocao()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdLocalRemocao());
			if(objeto.getCdMotivoRemocao()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdMotivoRemocao());
			pstmt.setString(21,objeto.getTxtObjetos());
			if(objeto.getCdBoat()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdBoat());
			if(objeto.getCdTipoRemocao()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdTipoRemocao());
			pstmt.setString(24,objeto.getNmRecebedor());
			pstmt.setString(25,objeto.getRgRecebedor());
			if(objeto.getDtRecebimento()==null)
				pstmt.setNull(26, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(26,new Timestamp(objeto.getDtRecebimento().getTimeInMillis()));
			pstmt.setInt(27,objeto.getCdCidadeCondutor());
			pstmt.setInt(28,objeto.getCdCidadeProprietario());
			pstmt.setString(29,objeto.getUfCondutor());
			pstmt.setString(30,objeto.getUfProprietario());
			pstmt.setString(31,objeto.getEnderecoCondutor());
			pstmt.setString(32,objeto.getEnderecoProprietario());
			pstmt.setString(33,objeto.getBairroCondutor());
			pstmt.setString(34,objeto.getBairroProprietario());
			pstmt.setString(35,objeto.getVlHodometro());
			pstmt.setString(36,objeto.getNrCnhProrietario());
			pstmt.setInt(37,objeto.getCdCategoriaCnhProprietario());
			pstmt.setInt(38,objeto.getLgCnhVencidaProprietario());
			pstmt.setInt(39,objeto.getCdCategoriaCnhCondutor());
			pstmt.setInt(40,objeto.getLgCnhVencidaCondutor());
			pstmt.setString(41,objeto.getNrGuiaExame());
			pstmt.setString(42,objeto.getNmDelegaciaPolicia());
			pstmt.setString(43,objeto.getDsMotivoRecolhimentoDocumentos());
			pstmt.setString(44,objeto.getNmPatioDestino());
			pstmt.setString(45,objeto.getDsMotivoRecolhimento());
			pstmt.setInt(46,objeto.getCdMeioRemocao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Trrav objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Trrav objeto, int cdTrravOld) {
		return update(objeto, cdTrravOld, null);
	}

	public static int update(Trrav objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Trrav objeto, int cdTrravOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_trrav SET cd_trrav=?,"+
												      		   "nr_trrav=?,"+
												      		   "dt_ocorrencia=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_agente=?,"+
												      		   "cd_cidade=?,"+
												      		   "ds_observacao=?,"+
												      		   "ds_local_ocorrencia=?,"+
												      		   "ds_ponto_referencia=?,"+
												      		   "vl_latitude=?,"+
												      		   "vl_longitude=?,"+
												      		   "cd_veiculo=?,"+
												      		   "nr_placa=?,"+
												      		   "tp_documento=?,"+
												      		   "nr_documento=?,"+
												      		   "nm_condutor=?,"+
												      		   "nr_cnh_condutor=?,"+
												      		   "nm_proprietario=?,"+
												      		   "cd_local_remocao=?,"+
												      		   "cd_motivo_remocao=?,"+
												      		   "txt_objetos=?,"+
												      		   "cd_boat=?,"+
												      		   "cd_tipo_remocao=?,"+
												      		   "nm_recebedor=?,"+
												      		   "rg_recebedor=?,"+
												      		   "dt_recebimento=?,"+
												      		   "cd_cidade_condutor=?,"+
												      		   "cd_cidade_proprietario=?,"+
												      		   "uf_condutor=?,"+
												      		   "uf_proprietario=?,"+
												      		   "endereco_condutor=?,"+
												      		   "endereco_proprietario=?,"+
												      		   "bairro_condutor=?,"+
												      		   "bairro_proprietario=?,"+
												      		   "vl_hodometro=?,"+
												      		   "nr_cnh_prorietario=?,"+
												      		   "cd_categoria_cnh_proprietario=?,"+
												      		   "lg_cnh_vencida_proprietario=?,"+
												      		   "cd_categoria_cnh_condutor=?,"+
												      		   "lg_cnh_vencida_condutor=?,"+
												      		   "nr_guia_exame=?,"+
												      		   "nm_delegacia_policia=?,"+
												      		   "ds_motivo_recolhimento_documentos=?,"+
												      		   "nm_patio_destino=?,"+
												      		   "ds_motivo_recolhimento=?,"+
												      		   "cd_meio_remocao=? WHERE cd_trrav=?");
			pstmt.setInt(1,objeto.getCdTrrav());
			pstmt.setInt(2,objeto.getNrTrrav());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAgente());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCidade());
			pstmt.setString(7,objeto.getDsObservacao());
			pstmt.setString(8,objeto.getDsLocalOcorrencia());
			pstmt.setString(9,objeto.getDsPontoReferencia());
			pstmt.setDouble(10,objeto.getVlLatitude());
			pstmt.setDouble(11,objeto.getVlLongitude());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdVeiculo());
			pstmt.setString(13,objeto.getNrPlaca());
			pstmt.setInt(14,objeto.getTpDocumento());
			pstmt.setString(15,objeto.getNrDocumento());
			pstmt.setString(16,objeto.getNmCondutor());
			pstmt.setString(17,objeto.getNrCnhCondutor());
			pstmt.setString(18,objeto.getNmProprietario());
			if(objeto.getCdLocalRemocao()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdLocalRemocao());
			if(objeto.getCdMotivoRemocao()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdMotivoRemocao());
			pstmt.setString(21,objeto.getTxtObjetos());
			if(objeto.getCdBoat()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdBoat());
			if(objeto.getCdTipoRemocao()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdTipoRemocao());
			pstmt.setString(24,objeto.getNmRecebedor());
			pstmt.setString(25,objeto.getRgRecebedor());
			if(objeto.getDtRecebimento()==null)
				pstmt.setNull(26, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(26,new Timestamp(objeto.getDtRecebimento().getTimeInMillis()));
			pstmt.setInt(27,objeto.getCdCidadeCondutor());
			pstmt.setInt(28,objeto.getCdCidadeProprietario());
			pstmt.setString(29,objeto.getUfCondutor());
			pstmt.setString(30,objeto.getUfProprietario());
			pstmt.setString(31,objeto.getEnderecoCondutor());
			pstmt.setString(32,objeto.getEnderecoProprietario());
			pstmt.setString(33,objeto.getBairroCondutor());
			pstmt.setString(34,objeto.getBairroProprietario());
			pstmt.setString(35,objeto.getVlHodometro());
			pstmt.setString(36,objeto.getNrCnhProrietario());
			pstmt.setInt(37,objeto.getCdCategoriaCnhProprietario());
			pstmt.setInt(38,objeto.getLgCnhVencidaProprietario());
			pstmt.setInt(39,objeto.getCdCategoriaCnhCondutor());
			pstmt.setInt(40,objeto.getLgCnhVencidaCondutor());
			pstmt.setString(41,objeto.getNrGuiaExame());
			pstmt.setString(42,objeto.getNmDelegaciaPolicia());
			pstmt.setString(43,objeto.getDsMotivoRecolhimentoDocumentos());
			pstmt.setString(44,objeto.getNmPatioDestino());
			pstmt.setInt(45,objeto.getCdMeioRemocao());
			if(objeto.getDsMotivoRecolhimento()==null)
				pstmt.setNull(46, Types.INTEGER);
			else
				pstmt.setString(46,objeto.getDsMotivoRecolhimento());
			pstmt.setInt(47, cdTrravOld!=0 ? cdTrravOld : objeto.getCdTrrav());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTrrav) {
		return delete(cdTrrav, null);
	}

	public static int delete(int cdTrrav, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_trrav WHERE cd_trrav=?");
			pstmt.setInt(1, cdTrrav);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Trrav get(int cdTrrav) {
		return get(cdTrrav, null);
	}

	public static Trrav get(int cdTrrav, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_trrav WHERE cd_trrav=?");
			pstmt.setInt(1, cdTrrav);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Trrav(rs.getInt("cd_trrav"),
						rs.getInt("nr_trrav"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_agente"),
						rs.getInt("cd_cidade"),
						rs.getString("ds_observacao"),
						rs.getString("ds_local_ocorrencia"),
						rs.getString("ds_ponto_referencia"),
						rs.getDouble("vl_latitude"),
						rs.getDouble("vl_longitude"),
						rs.getInt("cd_veiculo"),
						rs.getString("nr_placa"),
						rs.getInt("tp_documento"),
						rs.getString("nr_documento"),
						rs.getString("nm_condutor"),
						rs.getString("nr_cnh_condutor"),
						rs.getString("nm_proprietario"),
						rs.getInt("cd_local_remocao"),
						rs.getInt("cd_motivo_remocao"),
						rs.getString("txt_objetos"),
						rs.getInt("cd_boat"),
						rs.getInt("cd_tipo_remocao"),
						rs.getString("nm_recebedor"),
						rs.getString("rg_recebedor"),
						(rs.getTimestamp("dt_recebimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_recebimento").getTime()),
						rs.getInt("cd_cidade_condutor"),
						rs.getInt("cd_cidade_proprietario"),
						rs.getString("uf_condutor"),
						rs.getString("uf_proprietario"),
						rs.getString("endereco_condutor"),
						rs.getString("endereco_proprietario"),
						rs.getString("bairro_condutor"),
						rs.getString("bairro_proprietario"),
						rs.getString("vl_hodometro"),
						rs.getString("nr_cnh_prorietario"),
						rs.getInt("cd_categoria_cnh_proprietario"),
						rs.getInt("lg_cnh_vencida_proprietario"),
						rs.getInt("cd_categoria_cnh_condutor"),
						rs.getInt("lg_cnh_vencida_condutor"),
						rs.getString("nr_guia_exame"),
						rs.getString("nm_delegacia_policia"),
						rs.getString("ds_motivo_recolhimento_documentos"),
						rs.getString("nm_patio_destino"),
						rs.getString("ds_motivo_recolhimento"),
						rs.getInt("cd_meio_remocao"),null,null,null,null);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_trrav");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Trrav> getList() {
		return getList(null);
	}

	public static ArrayList<Trrav> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Trrav> list = new ArrayList<Trrav>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Trrav obj = TrravDAO.get(rsm.getInt("cd_trrav"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_trrav", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}