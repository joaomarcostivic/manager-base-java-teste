package com.tivic.manager.fta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.agd.Agendamento;
import com.tivic.manager.agd.AgendamentoDAO;
import com.tivic.manager.agd.AgendamentoServices;
import com.tivic.manager.bpm.Marca;
import com.tivic.manager.bpm.MarcaDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;


import sol.dao.ResultSetMap;

public class VeiculoCheckupServices {
	public static final String[] tipoOrigem = {"Viagem", "Ocorrência", "Padrão de Manutenção", "Padrão de Troca", "Usuário"};

	public static final int TP_ORIGEM_VIAGEM = 0;
	public static final int TP_ORIGEM_OCORRENCIA = 1;
	public static final int TP_ORIGEM_PADRAO_MANUTENCAO = 2;
	public static final int TP_ORIGEM_PADRAO_TROCA = 3;
	public static final int TP_ORIGEM_USUARIO = 4;

	public static final String[] situacaoCheckup = {"Aberto", "Verificando", "Finalizado"};

	public static final int ST_CHECKUP_ABERTO = 0;
	public static final int ST_CHECKUP_VERIFICANDO = 1;
	public static final int ST_CHECKUP_FINALIZADO = 2;

	public static int save(VeiculoCheckup checkup, ArrayList<VeiculoCheckupItem> itens){
		return save(checkup, itens, null);
	}
	public static int save(VeiculoCheckup checkup, ArrayList<VeiculoCheckupItem> itens, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(checkup==null)
				return -1;

			int retorno;
			if(checkup.getCdCheckup()==0){
				retorno = VeiculoCheckupDAO.insert(checkup, connect);
				checkup.setCdCheckup(retorno);
			}
			else
				retorno = VeiculoCheckupDAO.update(checkup, connect);


			//salvando itens
			for(int i=0; itens!=null && i<itens.size(); i++){
				if(retorno>0){
					VeiculoCheckupItem item = (VeiculoCheckupItem)itens.get(i);
					if(VeiculoCheckupItemDAO.get(item.getCdCheckupItem(), connect)==null){
						item.setCdCheckup(checkup.getCdCheckup());
						retorno = VeiculoCheckupItemDAO.insert(item, connect);
					}
					else
						retorno = VeiculoCheckupItemDAO.update(item, connect);
				}
				else break;
			}

			if(retorno>0){
				Veiculo veiculo = VeiculoDAO.get(checkup.getCdVeiculo(), connect);
				ModeloVeiculo modelo = ModeloVeiculoDAO.get(veiculo.getCdModelo(), connect);
				Marca marca = MarcaDAO.get(modelo.getCdMarca(), connect);

				Agendamento agendamento = AgendamentoDAO.get(checkup.getCdAgendamento(), connect);

				if(agendamento==null){
					agendamento = new Agendamento(0, //int cdAgendamento,
															  "FAZER CHECKUP VEICULO "+marca.getNmMarca()+" "+modelo.getNmModelo()+" "+veiculo.getNrPlaca(), //String nmAgendamento,
															  null, //String nmLocal,
															  checkup.getDtCheckup(), //GregorianCalendar dtInicial,
															  checkup.getDtPrazoConclusao(), //GregorianCalendar dtFinal,
															  AgendamentoServices.ST_PENDENTE, //int stAgendamento,
															  "VEICULO: "+marca.getNmMarca()+" "+modelo.getNmModelo()+"\n" +
															    "PLACA: "+veiculo.getNrPlaca()+"\n"+
															    "ORIGEM: "+tipoOrigem[checkup.getTpOrigem()]+"\n"+
															    "OBSERVAÇÕES: "+checkup.getTxtObservacao(), //String txtAgendamento,
															  1, //int lgLembrete,
															  1, //int qtTempoLembrete,
															  AgendamentoServices.TMP_HORA, //int tpUnidadeTempoLembrete,
															  0, //int lgAnexos,
															  new GregorianCalendar(), //GregorianCalendar dtCadastro,
															  0, //int cdRecorrencia,
															  null, //String idAgendamento,
															  0, //int nrRecorrencia,
															  ParametroServices.getValorOfParametroAsInteger("CD_TIPO_AGENDAMENTO_CHECKUP", 0, 0, connect), //int cdTipoAgendamento,
															  1, //int lgOriginal,
															  0 /*int cdAgenda*/,
															  0 /*cdMailing*/,
															  0 /*cdDocumento*/,
															  null /*dtLemebrete*/);

					HashMap<String, Object> r = AgendamentoServices.insert(agendamento, 0, checkup.getCdUsuario(), checkup.getCdUsuarioResponsavel(), null, null, connect);
					retorno = (r==null)?-2:((Agendamento)r.get("agendamentoDefault")).getCdAgendamento();

					if(retorno>0){
						checkup.setCdAgendamento(retorno);
						retorno = VeiculoCheckupDAO.update(checkup, connect);
					}
				}
				else {
					agendamento.setTxtAgendamento("VEICULO: "+marca.getNmMarca()+" "+modelo.getNmModelo()+"\n" +
															    "PLACA: "+veiculo.getNrPlaca()+"\n"+
															    "ORIGEM: "+tipoOrigem[checkup.getTpOrigem()]+"\n"+
															    "OBSERVAÇÕES: "+checkup.getTxtObservacao());
					HashMap<String, Object> r = AgendamentoServices.update(agendamento, 0, checkup.getCdUsuarioResponsavel(), null, null, connect);
					retorno = (r==null)?-2:((Agendamento)r.get("agendamentoDefault")).getCdAgendamento();
				}
			}

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno>0?checkup.getCdCheckup():retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupServices.save: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCheckup) {
		return delete(cdCheckup, null);
	}

	public static int delete(int cdCheckup, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdCheckup<=0)
				return -1;

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_veiculo_checkup_item WHERE cd_checkup=?");
			pstmt.setInt(1, cdCheckup);
			pstmt.executeUpdate();

			VeiculoCheckup checkup = VeiculoCheckupDAO.get(cdCheckup, connect);
			if(VeiculoCheckupDAO.delete(cdCheckup, connect)<=0){
				Conexao.rollback(connect);
				return -2;
			}
			if(checkup.getCdAgendamento()>0 && AgendamentoServices.delete(checkup.getCdAgendamento(), connect)<=0){
				Conexao.rollback(connect);
				return -3;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupServices.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -3;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getItens(int cdCheckup) {
		return getItens(cdCheckup, null);
	}

	public static ResultSetMap getItens(int cdCheckup, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, C.nm_componente " +
				      "FROM fta_veiculo_checkup_item A " +
				      "LEFT OUTER JOIN fta_componente_veiculo B ON (B.cd_componente = A.cd_componente) " +
				      "LEFT OUTER JOIN bpm_componente_referencia C ON (C.cd_componente = A.cd_componente) " +
				      "WHERE A.cd_checkup = ? " +
				      "ORDER BY cd_item");
			pstmt.setInt(1, cdCheckup);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupServices.getItens: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}