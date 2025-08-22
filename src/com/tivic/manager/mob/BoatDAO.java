package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class BoatDAO{

	public static int insert(Boat objeto) {
		return insert(objeto, null);
	}

	public static int insert(Boat objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_boat", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdBoat(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_boat (cd_boat,"+
			                                  "nr_boat,"+
			                                  "dt_ocorrencia,"+
			                                  "tp_acidente,"+
			                                  "nm_tipo_acidente_outro,"+
			                                  "cd_usuario,"+
			                                  "cd_agente,"+
			                                  "cd_cidade,"+
			                                  "ds_observacao,"+
			                                  "ds_local_ocorrencia,"+
			                                  "ds_ponto_referencia,"+
			                                  "vl_latitude,"+
			                                  "vl_longitude,"+
			                                  "cd_tipo_acidente,"+
			                                  "cd_condicao_via,"+
			                                  "cd_tipo_pavimento,"+
			                                  "cd_condicao_clima,"+
			                                  "st_sinalizacao_horizontal,"+
			                                  "st_sinalizacao_vertical,"+
			                                  "st_semaforo,"+
			                                  "nm_delegacia_comunicada,"+
			                                  "dt_comunicacao,"+
			                                  "nr_ocorrencia_policial,"+
			                                  "nm_agente_policial,"+
			                                  "nr_matricula_agente_policial,"+
			                                  "txt_ocorrencia_policial,"+
			                                  "lg_policia_tecnica,"+
			                                  "nm_responsavel_policia_tecnica,"+
			                                  "lg_iml,"+
			                                  "nm_responsavel_iml,"+
			                                  "lg_pericia_realizada,"+
			                                  "nm_responsavel_pericia,"+
			                                  "lg_bombeiros,"+
			                                  "nm_responsavel_bombeiros,"+
			                                  "lg_resgate,"+
			                                  "nm_responsavel_resgate,"+
			                                  "nm_outro_orgao,"+
			                                  "nm_responsavel_outro_orgao,"+
			                                  "blb_diagrama,"+
			                                  "img_diagrama,"+
			                                  "txt_descricao_sumaria,"+
			                                  "tp_caracteristica_acidente,"+
			                                  "tp_condicao_via,"+
			                                  "tp_pavimento,"+
			                                  "tp_condicao_clima,"+
			                                  "nm_outra_condicao_via,"+
			                                  "nm_outro_pavimento,"+
			                                  "nm_outra_condicao_clima,"+
			                                  "txt_documentos_entregues,"+
			                                  "nr_protocolo,"+
			                                  "st_boat,"+
			                                  "lg_dano_patrimonio_publico,"+
			                                  "lg_dano_meio_ambiente,"+
			                                  "txt_justificativa,"+
			                                  "tp_tracado_via) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getNrBoat());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setInt(4,objeto.getTpAcidente());
			pstmt.setString(5,objeto.getNmTipoAcidenteOutro());
			if(objeto.getCdUsuario() == 0) 
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUsuario());
			
			if(objeto.getCdAgente() == 0)
				pstmt.setNull(7, Types.INTEGER);
			else 
				pstmt.setInt(7,objeto.getCdAgente());
			
			pstmt.setInt(8,objeto.getCdCidade());
			pstmt.setString(9,objeto.getDsObservacao());
			pstmt.setString(10,objeto.getDsLocalOcorrencia());
			pstmt.setString(11,objeto.getDsPontoReferencia());
			pstmt.setDouble(12,objeto.getVlLatitude());
			pstmt.setDouble(13,objeto.getVlLongitude());

			if(objeto.getCdTipoAcidente() == 0)
				pstmt.setNull(14, Types.INTEGER);
			else 
				pstmt.setInt(14, objeto.getCdTipoAcidente());

			if(objeto.getCdCondicaoVia() == 0)
				pstmt.setNull(15, Types.INTEGER);
			else 
				pstmt.setInt(15, objeto.getCdCondicaoVia());

			if(objeto.getCdTipoPavimento() == 0)
				pstmt.setNull(16, Types.INTEGER);
			else 
				pstmt.setInt(16, objeto.getCdTipoPavimento());

			if(objeto.getCdCondicaoClima() == 0)
				pstmt.setNull(17, Types.INTEGER);
			else 
				pstmt.setInt(17, objeto.getCdCondicaoClima());
			
			pstmt.setInt(18,objeto.getStSinalizacaoHorizontal());
			pstmt.setInt(19,objeto.getStSinalizacaoVertical());
			pstmt.setInt(20,objeto.getStSemaforo());
			pstmt.setString(21,objeto.getNmDelegaciaComunicada());
			if(objeto.getDtComunicacao()==null)
				pstmt.setNull(22, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(22,new Timestamp(objeto.getDtComunicacao().getTimeInMillis()));
			pstmt.setString(23,objeto.getNrOcorrenciaPolicial());
			pstmt.setString(24,objeto.getNmAgentePolicial());
			pstmt.setString(25,objeto.getNrMatriculaAgentePolicial());
			pstmt.setString(26,objeto.getTxtOcorrenciaPolicial());
			pstmt.setInt(27,objeto.getLgPoliciaTecnica());
			pstmt.setString(28,objeto.getNmResponsavelPoliciaTecnica());
			pstmt.setInt(29,objeto.getLgIml());
			pstmt.setString(30,objeto.getNmResponsavelIml());
			pstmt.setInt(31,objeto.getLgPericiaRealizada());
			pstmt.setString(32,objeto.getNmResponsavelPericia());
			pstmt.setString(33,objeto.getLgBombeiros());
			pstmt.setString(34,objeto.getNmResponsavelBombeiros());
			pstmt.setInt(35,objeto.getLgResgate());
			pstmt.setString(36,objeto.getNmResponsavelResgate());
			pstmt.setString(37,objeto.getNmOutroOrgao());
			pstmt.setString(38,objeto.getNmResponsavelOutroOrgao());
			if(objeto.getBlbDiagrama()==null)
				pstmt.setNull(39, Types.BINARY);
			else
				pstmt.setBytes(39,objeto.getBlbDiagrama());
			if(objeto.getImgDiagrama()==null)
				pstmt.setNull(40, Types.BINARY);
			else
				pstmt.setBytes(40,objeto.getImgDiagrama());
			pstmt.setString(41,objeto.getTxtDescricaoSumaria());
			pstmt.setInt(42,objeto.getTpCaracteristicaAcidente());
			pstmt.setInt(43,objeto.getTpCondicaoVia());
			pstmt.setInt(44,objeto.getTpPavimento());
			pstmt.setInt(45,objeto.getTpCondicaoClima());
			pstmt.setString(46,objeto.getNmOutraCondicaoVia());
			pstmt.setString(47,objeto.getNmOutroPavimento());
			pstmt.setString(48,objeto.getNmOutraCondicaoClima());
			pstmt.setString(49,objeto.getTxtDocumentosEntregues());
			pstmt.setString(50,objeto.getNrProtocolo());
			pstmt.setInt(51,objeto.getStBoat());
			pstmt.setInt(52,objeto.getLgDanoPatrimonioPublico());
			pstmt.setInt(53,objeto.getLgDanoMeioAmbiente());
			pstmt.setString(54,objeto.getTxtJustificativa());
			pstmt.setInt(55,objeto.getTpTracadoVia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Boat objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Boat objeto, int cdBoatOld) {
		return update(objeto, cdBoatOld, null);
	}

	public static int update(Boat objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Boat objeto, int cdBoatOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_boat SET cd_boat=?,"+
												      		   "nr_boat=?,"+
												      		   "dt_ocorrencia=?,"+
												      		   "tp_acidente=?,"+
												      		   "nm_tipo_acidente_outro=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_agente=?,"+
												      		   "cd_cidade=?,"+
												      		   "ds_observacao=?,"+
												      		   "ds_local_ocorrencia=?,"+
												      		   "ds_ponto_referencia=?,"+
												      		   "vl_latitude=?,"+
												      		   "vl_longitude=?,"+
												      		   "cd_tipo_acidente=?,"+
												      		   "cd_condicao_via=?,"+
												      		   "cd_tipo_pavimento=?,"+
												      		   "cd_condicao_clima=?,"+
												      		   "st_sinalizacao_horizontal=?,"+
												      		   "st_sinalizacao_vertical=?,"+
												      		   "st_semaforo=?,"+
												      		   "nm_delegacia_comunicada=?,"+
												      		   "dt_comunicacao=?,"+
												      		   "nr_ocorrencia_policial=?,"+
												      		   "nm_agente_policial=?,"+
												      		   "nr_matricula_agente_policial=?,"+
												      		   "txt_ocorrencia_policial=?,"+
												      		   "lg_policia_tecnica=?,"+
												      		   "nm_responsavel_policia_tecnica=?,"+
												      		   "lg_iml=?,"+
												      		   "nm_responsavel_iml=?,"+
												      		   "lg_pericia_realizada=?,"+
												      		   "nm_responsavel_pericia=?,"+
												      		   "lg_bombeiros=?,"+
												      		   "nm_responsavel_bombeiros=?,"+
												      		   "lg_resgate=?,"+
												      		   "nm_responsavel_resgate=?,"+
												      		   "nm_outro_orgao=?,"+
												      		   "nm_responsavel_outro_orgao=?,"+
												      		   "blb_diagrama=?,"+
												      		   "img_diagrama=?,"+
												      		   "txt_descricao_sumaria=?,"+
												      		   "tp_caracteristica_acidente=?,"+
												      		   "tp_condicao_via=?,"+
												      		   "tp_pavimento=?,"+
												      		   "tp_condicao_clima=?,"+
												      		   "nm_outra_condicao_via=?,"+
												      		   "nm_outro_pavimento=?,"+
												      		   "nm_outra_condicao_clima=?,"+
												      		   "txt_documentos_entregues=?,"+
												      		   "nr_protocolo=?,"+
												      		   "st_boat=?,"+
												      		   "lg_dano_patrimonio_publico=?,"+
												      		   "lg_dano_meio_ambiente=?,"+
												      		   "txt_justificativa=?,"+
												      		   "tp_tracado_via=? WHERE cd_boat=?");
			pstmt.setInt(1,objeto.getCdBoat());
			pstmt.setInt(2,objeto.getNrBoat());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setInt(4,objeto.getTpAcidente());
			pstmt.setString(5,objeto.getNmTipoAcidenteOutro());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setInt(4,objeto.getTpAcidente());
			pstmt.setString(5,objeto.getNmTipoAcidenteOutro());
			if(objeto.getCdUsuario() == 0) 
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUsuario());
			
			if(objeto.getCdAgente() == 0)
				pstmt.setNull(7, Types.INTEGER);
			else 
				pstmt.setInt(7,objeto.getCdAgente());
			
			pstmt.setInt(8,objeto.getCdCidade());
			pstmt.setString(9,objeto.getDsObservacao());
			pstmt.setString(10,objeto.getDsLocalOcorrencia());
			pstmt.setString(11,objeto.getDsPontoReferencia());
			pstmt.setDouble(12,objeto.getVlLatitude());
			pstmt.setDouble(13,objeto.getVlLongitude());

			if(objeto.getCdTipoAcidente() == 0)
				pstmt.setNull(14, Types.INTEGER);
			else 
				pstmt.setInt(14, objeto.getCdTipoAcidente());

			if(objeto.getCdCondicaoVia() == 0)
				pstmt.setNull(15, Types.INTEGER);
			else 
				pstmt.setInt(15, objeto.getCdCondicaoVia());

			if(objeto.getCdTipoPavimento() == 0)
				pstmt.setNull(16, Types.INTEGER);
			else 
				pstmt.setInt(16, objeto.getCdTipoPavimento());

			if(objeto.getCdCondicaoClima() == 0)
				pstmt.setNull(17, Types.INTEGER);
			else 
				pstmt.setInt(17, objeto.getCdCondicaoClima());
			pstmt.setInt(18,objeto.getStSinalizacaoHorizontal());
			pstmt.setInt(19,objeto.getStSinalizacaoVertical());
			pstmt.setInt(20,objeto.getStSemaforo());
			pstmt.setString(21,objeto.getNmDelegaciaComunicada());
			if(objeto.getDtComunicacao()==null)
				pstmt.setNull(22, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(22,new Timestamp(objeto.getDtComunicacao().getTimeInMillis()));
			pstmt.setString(23,objeto.getNrOcorrenciaPolicial());
			pstmt.setString(24,objeto.getNmAgentePolicial());
			pstmt.setString(25,objeto.getNrMatriculaAgentePolicial());
			pstmt.setString(26,objeto.getTxtOcorrenciaPolicial());
			pstmt.setInt(27,objeto.getLgPoliciaTecnica());
			pstmt.setString(28,objeto.getNmResponsavelPoliciaTecnica());
			pstmt.setInt(29,objeto.getLgIml());
			pstmt.setString(30,objeto.getNmResponsavelIml());
			pstmt.setInt(31,objeto.getLgPericiaRealizada());
			pstmt.setString(32,objeto.getNmResponsavelPericia());
			pstmt.setString(33,objeto.getLgBombeiros());
			pstmt.setString(34,objeto.getNmResponsavelBombeiros());
			pstmt.setInt(35,objeto.getLgResgate());
			pstmt.setString(36,objeto.getNmResponsavelResgate());
			pstmt.setString(37,objeto.getNmOutroOrgao());
			pstmt.setString(38,objeto.getNmResponsavelOutroOrgao());
			if(objeto.getBlbDiagrama()==null)
				pstmt.setNull(39, Types.BINARY);
			else
				pstmt.setBytes(39,objeto.getBlbDiagrama());
			if(objeto.getImgDiagrama()==null)
				pstmt.setNull(40, Types.BINARY);
			else
				pstmt.setBytes(40,objeto.getImgDiagrama());
			pstmt.setString(41,objeto.getTxtDescricaoSumaria());
			pstmt.setInt(42,objeto.getTpCaracteristicaAcidente());
			pstmt.setInt(43,objeto.getTpCondicaoVia());
			pstmt.setInt(44,objeto.getTpPavimento());
			pstmt.setInt(45,objeto.getTpCondicaoClima());
			pstmt.setString(46,objeto.getNmOutraCondicaoVia());
			pstmt.setString(47,objeto.getNmOutroPavimento());
			pstmt.setString(48,objeto.getNmOutraCondicaoClima());
			pstmt.setString(49,objeto.getTxtDocumentosEntregues());
			pstmt.setString(50,objeto.getNrProtocolo());
			pstmt.setInt(51,objeto.getStBoat());
			pstmt.setInt(52,objeto.getLgDanoPatrimonioPublico());
			pstmt.setInt(53,objeto.getLgDanoMeioAmbiente());
			pstmt.setString(54,objeto.getTxtJustificativa());
			pstmt.setInt(55,objeto.getTpTracadoVia());
			pstmt.setInt(56, cdBoatOld!=0 ? cdBoatOld : objeto.getCdBoat());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBoat) {
		return delete(cdBoat, null);
	}

	public static int delete(int cdBoat, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_boat WHERE cd_boat=?");
			pstmt.setInt(1, cdBoat);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Boat get(int cdBoat) {
		return get(cdBoat, null);
	}

	public static Boat get(int cdBoat, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT "
					+ "cd_boat,"  
					+ "nr_boat," 
					+ "dt_ocorrencia,"  
					+ "tp_acidente,"  
					+ "nm_tipo_acidente_outro,"  
					+ "cd_usuario,"  
					+ "cd_agente,"  
					+ "cd_cidade,"  
					+ "ds_observacao,"  
					+ "ds_local_ocorrencia,"  
					+ "ds_ponto_referencia,"  
					+ "vl_latitude,"  
					+ "vl_longitude,"  
					+ "cd_tipo_acidente,"  
					+ "cd_condicao_via,"  
					+ "cd_tipo_pavimento,"  
					+ "cd_condicao_clima,"  
					+ "st_sinalizacao_horizontal," 
					+ "st_sinalizacao_vertical,"  
					+ "st_semaforo," 
					+ "nm_delegacia_comunicada,"  
					+ "dt_comunicacao,"  
					+ "nr_ocorrencia_policial,"  
					+ "nm_agente_policial," 
					+ "nr_matricula_agente_policial,"  
					+ "txt_ocorrencia_policial," 
					+ "lg_policia_tecnica," 
					+ "nm_responsavel_policia_tecnica,"  
					+ "lg_iml," 
					+ "nm_responsavel_iml," 
					+ "lg_pericia_realizada," 
					+ "nm_responsavel_pericia," 
					+ "lg_bombeiros," 
					+ "nm_responsavel_bombeiros,"
					+ "lg_resgate," 
					+ "nm_responsavel_resgate," 
					+ "nm_outro_orgao," 
					+ "nm_responsavel_outro_orgao," 
					+ "blb_diagrama," 
					+ "img_diagrama," 
					+ "txt_descricao_sumaria," 
					+ "tp_condicao_via," 
					+ "tp_pavimento," 
					+ "tp_condicao_clima," 
					+ "nm_outra_condicao_via," 
					+ "nm_outro_pavimento," 
					+ "nm_outra_condicao_clima," 
					+ "lg_dano_patrimonio_publico," 
					+ "lg_dano_meio_ambiente," 
					+ "tp_tracado_via," 
					+ "txt_documentos_entregues," 
					+ "nr_protocolo," 
					+ "st_boat," 
					+ "txt_justificativa," 
					+ " CASE " 
					+ " WHEN tp_caracteristica_acidente IS NULL THEN '-1'" 
					+ " ELSE tp_caracteristica_acidente" 
					+ " END AS tp_caracteristica_acidente FROM mob_boat WHERE cd_boat=?");
			pstmt.setInt(1, cdBoat);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Boat(rs.getInt("cd_boat"),
						rs.getInt("nr_boat"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()),
						rs.getInt("tp_acidente"),
						rs.getString("nm_tipo_acidente_outro"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_agente"),
						rs.getInt("cd_cidade"),
						rs.getString("ds_observacao"),
						rs.getString("ds_local_ocorrencia"),
						rs.getString("ds_ponto_referencia"),
						rs.getDouble("vl_latitude"),
						rs.getDouble("vl_longitude"),
						rs.getInt("cd_tipo_acidente"),
						rs.getInt("cd_condicao_via"),
						rs.getInt("cd_tipo_pavimento"),
						rs.getInt("cd_condicao_clima"),
						rs.getInt("st_sinalizacao_horizontal"),
						rs.getInt("st_sinalizacao_vertical"),
						rs.getInt("st_semaforo"),
						rs.getString("nm_delegacia_comunicada"),
						(rs.getTimestamp("dt_comunicacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_comunicacao").getTime()),
						rs.getString("nr_ocorrencia_policial"),
						rs.getString("nm_agente_policial"),
						rs.getString("nr_matricula_agente_policial"),
						rs.getString("txt_ocorrencia_policial"),
						rs.getInt("lg_policia_tecnica"),
						rs.getString("nm_responsavel_policia_tecnica"),
						rs.getInt("lg_iml"),
						rs.getString("nm_responsavel_iml"),
						rs.getInt("lg_pericia_realizada"),
						rs.getString("nm_responsavel_pericia"),
						rs.getString("lg_bombeiros"),
						rs.getString("nm_responsavel_bombeiros"),
						rs.getInt("lg_resgate"),
						rs.getString("nm_responsavel_resgate"),
						rs.getString("nm_outro_orgao"),
						rs.getString("nm_responsavel_outro_orgao"),
						rs.getBytes("blb_diagrama")==null?null:rs.getBytes("blb_diagrama"),
						rs.getBytes("img_diagrama")==null?null:rs.getBytes("img_diagrama"),
						rs.getString("txt_descricao_sumaria"),
						rs.getInt("tp_caracteristica_acidente"),
						rs.getInt("tp_condicao_via"),
						rs.getInt("tp_pavimento"),
						rs.getInt("tp_condicao_clima"),
						rs.getInt("tp_tracado_via"),
						rs.getString("nm_outra_condicao_via"),
						rs.getString("nm_outro_pavimento"),
						rs.getString("nm_outra_condicao_clima"),
						rs.getString("txt_documentos_entregues"),
						rs.getString("nr_protocolo"),
						rs.getInt("st_boat"),
						rs.getInt("lg_dano_patrimonio_publico"),
						rs.getInt("lg_dano_meio_ambiente"),
						rs.getString("txt_justificativa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Boat> getList() {
		return getList(null);
	}

	public static ArrayList<Boat> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Boat> list = new ArrayList<Boat>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Boat obj = BoatDAO.get(rsm.getInt("cd_boat"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_boat", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
