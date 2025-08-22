package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.trrav.TrravReport;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TrravServices {

	public static Result save(Trrav trrav){
		return save(trrav, null, null);
	}

	public static Result save(Trrav trrav, AuthData authData){
		return save(trrav, authData, null);
	}

	public static Result save(Trrav trrav, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(trrav==null)
				return new Result(-1, "Erro ao salvar. Trrav é nulo");

			int retorno;
			if(trrav.getCdTrrav()==0){
				retorno = TrravDAO.insert(trrav, connect);
				trrav.setCdTrrav(retorno);
			}
			else {
				retorno = TrravDAO.update(trrav, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TRRAV", trrav);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result sync(ArrayList<Trrav> trravs) {
		return sync(trravs, null);
	}
	
	public static Result sync(ArrayList<Trrav> trravs, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			// Retira trravs duplicadas
			if(trravs.size()>1) {
				for (Trrav trrav : trravs) {
					for (Trrav trravCheck : trravs) {
						if(trrav!=trravCheck && trrav.getNrTrrav()==trravCheck.getNrTrrav()) {
							trravs.remove(trrav);
							break;
						}
					}
				}
			}
			
			ArrayList<Trrav> trravsRetorno = new ArrayList<Trrav>();
			ArrayList<Trrav> trravsDuplicadas = new ArrayList<Trrav>();
			ArrayList<Trrav> trravsErro = new ArrayList<Trrav>();
			
			int retorno = 0;
			for (Trrav trrav: trravs) {
				
				Result r = sync(trrav, connect);
				retorno = r.getCode();
				
				if(r.getCode()<=0) {
					trravsErro.add(trrav);
					continue;
				}
				else if(r.getCode()==2) {
					trravsDuplicadas.add(trrav);
					continue;
				}
				else {
					trrav.setCdTrrav(r.getCode());
					trravsRetorno.add(trrav);
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			Result r = new Result(retorno, retorno>0 ? "Sincronizado " + (trravs.size() == trravs.size() ? " com sucesso." : " parcialmente.") : "Erro ao sincronizar TRRAVs.");
			
//			r.addObject("AITS", aitsRetorno);
//			r.addObject("AITS_ERRO", aitsErro);
//			r.addObject("AITS_DUPLICADAS", aitsDuplicadas);
			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar TRRAVs");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result sync(Trrav trrav, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			System.out.println("\n["+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] Recebendo TRRAV...");
			System.out.println("\tNr. TRRAV: "+trrav.getNrTrrav());
			System.out.println("\tAgente: "+AgenteDAO.get(trrav.getCdAgente(), connect).getNmAgente());
			System.out.println("\tLocalizacao: ["+trrav.getVlLatitude()+", "+trrav.getVlLongitude()+"]");
			
			int retorno = 0;
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			ResultSet rs = connect.prepareStatement(
					lgBaseAntiga ? 
						"SELECT * FROM MOB_TRRAV WHERE NR_TRRAV = " + trrav.getNrTrrav(): 
						"SELECT * FROM mob_trrav WHERE nr_trrav = " + trrav.getNrTrrav()
				).executeQuery();
				
			if(rs.next()) {
				retorno = 2;
				System.out.println("Diagnostico: TRRAV Duplicada...");
			}
			else {
				retorno = insert(trrav, connect);
				
				if(retorno > 0) {
					System.out.println("Diagnostico: TRRAV Recebida...");
				}
				else {
					System.out.println("Diagnostico: Erro ao inserir...");
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, retorno>0 ? "Sincronizado com sucesso." : "Erro ao sincronizar TRRAVs.");
		}
		catch(Exception e) {
			System.out.println("Diagnostico: Erro na sincronizacao...");
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar TRRAVs");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int insert(Trrav objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			int code = 1;
			ResultSet rs = connect.prepareStatement(lgBaseAntiga ? "SELECT MAX(cd_trrav) as maxCode FROM mob_trrav" : 
				"SELECT MAX(cd_trrav) as maxCode FROM mob_trrav").executeQuery();
			if(rs.next())
				code = rs.getInt("maxCode") + 1;
			objeto.setCdTrrav(code);
			
			String sql = "INSERT INTO mob_trrav (cd_trrav,"+
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
                    "ds_motivo_recolhimento,"+
                    "nm_patio_destino) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			if(lgBaseAntiga) {
				sql = "INSERT INTO mob_trrav (cd_trrav,"+
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
                        "ds_motivo_recolhimento,"+
                        "nm_patio_destino) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			}
						
			PreparedStatement pstmt = connect.prepareStatement(sql);
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

			if(pstmt.executeUpdate() < 0) {
				if(isConnectionNull) {
					Conexao.rollback(connect);
				}
				System.out.println("Erro ao inserir TRRAV");
				return -1;
			}
			
			objeto.setCdTrrav(code);
				
			// jeito curioso de validação --@mauriciocordeiro
			int codeDocumentacao = 1, codeExame = 1, codeAitVinculadas = 1, codeImagem=1;
						
			//INSERIR IMAGENS
			if(objeto.getImagens()!=null && objeto.getImagens().size()>0) {
				codeImagem = TrravImagemServices.save(objeto.getCdTrrav(), objeto.getImagens(), connect).getCode();
			}
			
			//AIT VINCULADA
			if(objeto.getAitvinculadas()!=null && objeto.getAitvinculadas().size()>0) {				
				// Utiliza as chaves candidatas (nr_ait e nr_trrav) para garantir que o vínculo esteja correto
				for (TrravAit obj : objeto.getAitvinculadas()) {
					obj.setCdAit(AitServices.getById(obj.getNrAit(), connect).getCdAit());
					obj.setCdTrrav(objeto.getCdTrrav());
				}
				
				codeAitVinculadas = TrravAitServices.save(objeto.getCdTrrav(), objeto.getAitvinculadas(), connect).getCode();
				
			}
			else if(objeto.getAitvinculadas().size()==0){
				codeAitVinculadas = 1;
			}
									
			code = codeDocumentacao * codeExame * codeAitVinculadas * codeImagem;

			if(code<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravServices.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(Trrav trrav) {
		return remove(trrav.getCdTrrav());
	}
	public static Result remove(int cdTrrav){
		return remove(cdTrrav, false, null, null);
	}
	public static Result remove(int cdTrrav, boolean cascade){
		return remove(cdTrrav, cascade, null, null);
	}
	public static Result remove(int cdTrrav, boolean cascade, AuthData authData){
		return remove(cdTrrav, cascade, authData, null);
	}
	public static Result remove(int cdTrrav, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = TrravDAO.delete(cdTrrav, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
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
			System.err.println("Erro! TrravServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_trrav", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getAllByAit(int cdAit, Connection connect) {
		boolean isConnectionNull = connect==null;
		PreparedStatement pstmt;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			pstmt = connect.prepareStatement("SELECT cd_trrav FROM mob_trrav_ait WHERE cd_ait = "+cdAit);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			String listaRrd = Util.join(rsm, "cd_trrav", true);
			
			Criterios crt = new Criterios();
			crt.add("cd_trrav", listaRrd, ItemComparator.IN, Types.VARCHAR);
						
			return find(crt, connect);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravServices.getAllByAit: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	/**
	 * STATS
	 */
	@Deprecated
	public static Result statsQtTrrav() {
		GregorianCalendar hoje = new GregorianCalendar();
		GregorianCalendar dtInicial = new GregorianCalendar(hoje.get(Calendar.YEAR)-1, Calendar.JANUARY, 1, 0, 0, 0);
		GregorianCalendar dtFinal = new GregorianCalendar(hoje.get(Calendar.YEAR), Calendar.JANUARY, 31, 0, 0, 0);
		
		return statsQtTrrav(dtInicial, dtFinal, null);
	}
	
	public static Result statsQtTrrav(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsQtTrrav(dtInicial, dtFinal, null);
	}
	
	public static Result statsQtTrrav(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connection) {
	
		boolean isConnectionNull = connection == null;
		
		try {			
			if (isConnectionNull) 
				connection = Conexao.conectar();
			
			Result result = new Result(1, "");
			
			PreparedStatement ps = connection.prepareStatement(
					" SELECT COUNT(*) AS qt_trrav"
					+ " FROM mob_trrav"
					+ " WHERE dt_ocorrencia BETWEEN ? AND ?");
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));	
	
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				result.addObject("QT_TRRAV", rs.getInt("qt_trrav"));
			}
			
			return result;
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public byte[] imprimirTrrav(int cdTrrav) throws ValidationException, Exception {
	    if (cdTrrav <= 0) {
	        throw new ValidationException("O TRRAV com o código " + cdTrrav + " não foi encontrado.");
	    }
	    byte[] trrav = new TrravReport().gerar(cdTrrav);
	    return trrav;
	}

}