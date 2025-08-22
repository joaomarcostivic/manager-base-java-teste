package com.tivic.manager.str;

import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class AitTransporteServices {

	public static Result sync(ArrayList<AitTransporte> aits) {
		return sync(aits, null);
	}

	public static Result sync(ArrayList<AitTransporte> aits, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
					
			if(aits.size()>1) {
				for (AitTransporte ait : aits) {
					for (AitTransporte aitCheck : aits) {
						if(ait!=aitCheck && ait.getNrAit()==aitCheck.getNrAit()) {
							aits.remove(ait);
							break;
						}
					}
				}
			}
			
			ArrayList<AitTransporte> aitsRetorno = new ArrayList<AitTransporte>();
			ArrayList<AitTransporte> aitsDuplicadas = new ArrayList<AitTransporte>();
			ArrayList<AitTransporte> aitsErro = new ArrayList<AitTransporte>();
			
			int retorno = 0;
			for (AitTransporte ait: aits) {
				
				Result r = sync(ait, connect);
				retorno = r.getCode();
				
				if(r.getCode()<=0) {
					aitsErro.add(ait);
					continue;
				}
				else if(r.getCode()==2) {
					aitsDuplicadas.add(ait);
					continue;
				}
				else {
					ait.setCdAit(r.getCode());
					aitsRetorno.add(ait);
				}
			}			
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			Result r = new Result(retorno, retorno>0 ? "Sincronizado " + (aits.size() == aitsRetorno.size() ? " com sucesso." : " parcialmente.") : "Erro ao sincronizar AITs de Transporte.");
			
//			r.addObject("AITS", aitsRetorno);
//			r.addObject("AITS_ERRO", aitsErro);
//			r.addObject("AITS_DUPLICADAS", aitsDuplicadas);
			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar AITs");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result sync(AitTransporte ait, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			
			if(lgBaseAntiga){
				System.out.println("\n["+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] Recebendo AIT de Transporte...");
				System.out.println("\tNr. AIT: "+ait.getNrAit());
				System.out.println("\tAgente: "+AgenteDAO.get(ait.getCdAgente(), connect).getNmAgente());
				System.out.println("\tInfracao: "+InfracaoTransporteDAO.get(ait.getCdInfracao(), connect).getNrInfracao());
				System.out.println("\tLocalizacao: ["+ait.getVlLatitude()+", "+ait.getVlLongitude()+"]");			
			}
			else{
				System.out.println("\n["+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] Recebendo AIT de Transporte...");
				System.out.println("\tNr. AIT: "+ait.getNrAit());
				System.out.println("\tAgente: "+com.tivic.manager.mob.AgenteDAO.get(ait.getCdAgente(), connect).getNmAgente());
				System.out.println("\tInfracao: "+com.tivic.manager.mob.InfracaoTransporteDAO.get(ait.getCdInfracao(), connect).getNrInfracao());
				System.out.println("\tLocalizacao: ["+ait.getVlLatitude()+", "+ait.getVlLongitude()+"]");											
			}
			
			int retorno = 0;
			
			ResultSet rs = connect.prepareStatement(
					lgBaseAntiga ? 
						"SELECT * FROM AIT_TRANSPORTE WHERE NR_AIT = '" + ait.getNrAit() + "'": 
						"SELECT * FROM mob_ait_transporte WHERE nr_ait = '" + ait.getNrAit() + "'"
				).executeQuery();
				
			if(rs.next()) {
				retorno = 2;
				System.out.println("Diagnostico: AIT Transporte Duplicada...");
			}
			else {
				if(lgBaseAntiga){				
				    retorno = insert(ait, connect);
				}
				else{
					com.tivic.manager.mob.AitTransporte aitTransporteMob = new com.tivic.manager.mob.AitTransporte();
					
					aitTransporteMob.setCdAit(ait.getCdAit());
					aitTransporteMob.setCdInfracao(ait.getCdInfracao());
					aitTransporteMob.setDsProvidencia(ait.getDsProvidencia());
					aitTransporteMob.setDtPrazo(ait.getDtPrazo());
					aitTransporteMob.setStAit(ait.getStAit());
					aitTransporteMob.setCdAgente(ait.getCdAgente());
					aitTransporteMob.setNmPreposto(ait.getNmPreposto());
					aitTransporteMob.setNrAit(ait.getNrAit());
					aitTransporteMob.setDsObservacao(ait.getDsObservacao());
					aitTransporteMob.setDtInfracao(ait.getDtInfracao());
					aitTransporteMob.setDtEmissaoNip(ait.getDtEmissaoNip());
					aitTransporteMob.setNrViaNip(ait.getNrViaNip());
					aitTransporteMob.setDtNotificacaoInicial(ait.getDtEmissao());
					aitTransporteMob.setVlLongitude(ait.getVlLongitude());
					aitTransporteMob.setVlLatitude(ait.getVlLatitude());
					aitTransporteMob.setCdCidade(ait.getCdCidade());
					aitTransporteMob.setCdEquipamento(ait.getCdEquipamento());
					aitTransporteMob.setNrPonto(ait.getNrPonto());
					aitTransporteMob.setDsLocalInfracao(ait.getDsLocalInfracao());
					aitTransporteMob.setLgReincidencia(ait.getLgReincidencia());
					aitTransporteMob.setNmTestemunha1(ait.getNmTestemunha1());
					aitTransporteMob.setNrRgTestemunha1(ait.getNrRgTestemunha1());
					aitTransporteMob.setNmEnderecoTestemunha1(ait.getNmEnderecoTestemunha1());
					aitTransporteMob.setNmTestemunha2(ait.getNmTestemunha2());
					aitTransporteMob.setNrRgTestemunha2(ait.getNrRgTestemunha2());
					aitTransporteMob.setNmEnderecoTestemunha2(ait.getNmEnderecoTestemunha2());
					aitTransporteMob.setTpAit(ait.getTpAit());
					aitTransporteMob.setCdTalao(ait.getCdTalao());					
					if(ait.getNmVeiculoPrefixo() != null)
					aitTransporteMob.setCdConcessaoVeiculo(Integer.parseInt(ait.getNmVeiculoPrefixo()));
					if(ait.getNmLinhaPrefixo() != null)
					aitTransporteMob.setCdLinha(Integer.parseInt(ait.getNmLinhaPrefixo()));
					aitTransporteMob.setCdConcessao(ait.getCdPermissionario());
					aitTransporteMob.setCdMotivo(ait.getCdMotivo());
					
					
					retorno = com.tivic.manager.mob.AitTransporteServices.inserirAitTalonarioEletronico(aitTransporteMob, connect);					
				}
				
				if(retorno > 0) {
					System.out.println("Diagnostico: AIT Transporte Recebida...");
				}
				else {
					System.out.println("Diagnostico: Erro ao inserir...");
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, retorno>0 ? "Sincronizado com sucesso." : "Erro ao sincronizar AITs de Transporte.");
		}
		catch(Exception e) {
			System.out.println("Diagnostico: Erro na sincronizacao...");
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar AITs de Transporte");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static int insert(AitTransporte objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			int code = 1;
			ResultSet rs = connect.prepareStatement(lgBaseAntiga ? "SELECT MAX(COD_AIT) as maxCode FROM AIT_TRANSPORTE" : 
				"SELECT MAX(CD_AIT) as maxCode FROM STR_AIT_TRANSPORTE").executeQuery();
			if(rs.next())
				code = rs.getInt("maxCode") + 1;
			objeto.setCdAit(code);
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO AIT_TRANSPORTE (COD_AIT,"+
			                                  "NM_EMPRESA,"+
			                                  "COD_INFRACAO,"+
			                                  "DS_PROVIDENCIA,"+
			                                  "DT_PRAZO,"+
			                                  "COD_AGENTE,"+
			                                  "NM_TESTEMUNHA1,"+
			                                  "NR_RG_TESTEMUNHA1,"+
			                                  "NM_ENDERECO_TESTEMUNHA1,"+
			                                  "NM_TESTEMUNHA2,"+
			                                  "NR_RG_TESTEMUNHA2,"+
			                                  "NM_ENDERECO_TESTEMUNHA2,"+
			                                  "NM_PREPOSTO,"+
			                                  "NR_AIT,"+
			                                  "DS_OBSERVACAO,"+
			                                  "DT_INFRACAO,"+
			                                  "NR_PROCESSO1,"+
			                                  "NR_PROCESSO2,"+
			                                  "DT_PROCESSO1,"+
			                                  "DT_PROCESSO2,"+
			                                  "ST_PROCESSO1,"+
			                                  "ST_PROCESSO2,"+
			                                  "DT_NOTIFICACAO1,"+
			                                  "DT_NOTIFICACAO2,"+
			                                  "DT_LIMITE,"+
			                                  "DT_EMISSAO_NIP,"+
			                                  "NR_VIA_NIP,"+
			                                  "ST_AIT,"+
			                                  "CD_PERMISSIONARIO,"+
			                                  "CD_MOTIVO,"+
			                                  "CD_USUARIO_CANCELAMENTO,"+
			                                  "CD_OCORRENCIA, "+ 
			                                  "TP_AIT, "+ 
			                                  "COD_TALAO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmEmpresa());
			pstmt.setInt(3,objeto.getCdInfracao());
			pstmt.setString(4,objeto.getDsProvidencia());
			if(objeto.getDtPrazo()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtPrazo().getTimeInMillis()));
			pstmt.setInt(6,objeto.getCdAgente());
			pstmt.setString(7,objeto.getNmTestemunha1());
			pstmt.setString(8,objeto.getNrRgTestemunha1());
			pstmt.setString(9,objeto.getNmEnderecoTestemunha1());
			pstmt.setString(10,objeto.getNmTestemunha2());
			pstmt.setString(11,objeto.getNrRgTestemunha2());
			pstmt.setString(12,objeto.getNmEnderecoTestemunha2());
			pstmt.setString(13,objeto.getNmPreposto());
			pstmt.setString(14,objeto.getNrAit());
			pstmt.setString(15,objeto.getDsObservacao());
			if(objeto.getDtInfracao()==null)
				pstmt.setNull(16, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(16,new Timestamp(objeto.getDtInfracao().getTimeInMillis()));
			pstmt.setString(17,objeto.getNrProcesso1());
			pstmt.setString(18,objeto.getNrProcesso2());
			if(objeto.getDtProcesso1()==null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19,new Timestamp(objeto.getDtProcesso1().getTimeInMillis()));
			if(objeto.getDtProcesso2()==null)
				pstmt.setNull(20, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(20,new Timestamp(objeto.getDtProcesso2().getTimeInMillis()));
			pstmt.setInt(21,objeto.getStProcesso1());
			pstmt.setInt(22,objeto.getStProcesso2());
			if(objeto.getDtNotificacao1()==null)
				pstmt.setNull(23, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(23,new Timestamp(objeto.getDtNotificacao1().getTimeInMillis()));
			if(objeto.getDtNotificacao2()==null)
				pstmt.setNull(24, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(24,new Timestamp(objeto.getDtNotificacao2().getTimeInMillis()));
			if(objeto.getDtLimite()==null)
				pstmt.setNull(25, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(25,new Timestamp(objeto.getDtLimite().getTimeInMillis()));
			if(objeto.getDtEmissaoNip()==null)
				pstmt.setNull(26, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(26,new Timestamp(objeto.getDtEmissaoNip().getTimeInMillis()));
			pstmt.setInt(27,objeto.getNrViaNip());
			pstmt.setInt(28,objeto.getStAit());
			
			pstmt.setInt(29,objeto.getCdPermissionario());
			
			if(objeto.getCdMotivo()<=0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30,objeto.getCdMotivo());
			
			if(objeto.getCdUsuarioCancelamento()<=0)
				pstmt.setNull(31, Types.INTEGER);
			else
				pstmt.setInt(31,objeto.getCdUsuarioCancelamento());
			
			if(objeto.getCdOcorrencia()<=0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdOcorrencia());

			if(objeto.getTpAit()<=0)
				pstmt.setNull(33, Types.INTEGER);
			else
				pstmt.setInt(33,objeto.getTpAit());
			
			if(objeto.getCdTalao()<=0)
				pstmt.setNull(34, Types.INTEGER);
			else
				pstmt.setInt(34,objeto.getCdTalao());
			
//			pstmt.setDouble(34,objeto.getVlLongitude());
//			pstmt.setDouble(35,objeto.getVlLatitude());
//			pstmt.setInt(36,objeto.getCdCidade());
//			pstmt.setInt(37,objeto.getCdEquipamento());
//			pstmt.setString(38,objeto.getNmLinhaPrefixo());
//			pstmt.setString(39,objeto.getNmVeiculoPrefixo());
//			pstmt.setString(40,objeto.getNrPonto());
//			pstmt.setString(41,objeto.getDsLocalInfracao());
//			pstmt.setInt(42,objeto.getLgReincidencia());
			pstmt.executeUpdate();
			
			
			//INSERIR IMAGENS AIT TRANSPORTE
			if(objeto.getImagens()!=null && objeto.getImagens().size()>0) {
				
				code = AitTransporteImagemServices.save(objeto.getCdAit(), objeto.getImagens(), connect).getCode();
			}
			
			
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result save(AitTransporte aitTransporte){
		return save(aitTransporte, null);
	}

	public static Result save(AitTransporte aitTransporte, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(aitTransporte==null)
				return new Result(-1, "Erro ao salvar. AitTransporte é nulo");

			int retorno;
			if(aitTransporte.getCdAit()==0){
				retorno = AitTransporteDAO.insert(aitTransporte, connect);
				aitTransporte.setCdAit(retorno);
			}
			else {
				retorno = AitTransporteDAO.update(aitTransporte, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AITTRANSPORTE", aitTransporte);
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
	public static Result remove(int cdAit){
		return remove(cdAit, false, null);
	}
	public static Result remove(int cdAit, boolean cascade){
		return remove(cdAit, cascade, null);
	}
	public static Result remove(int cdAit, boolean cascade, Connection connect){
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
			retorno = AitTransporteDAO.delete(cdAit, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM AIT_TRANSPORTE");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteServices.getAll: " + e);
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
		return Search.find("SELECT * FROM AIT_TRANSPORTE", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
