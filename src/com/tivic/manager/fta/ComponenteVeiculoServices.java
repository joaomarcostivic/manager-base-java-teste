package com.tivic.manager.fta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.manager.bpm.Marca;
import com.tivic.manager.bpm.MarcaDAO;
import com.tivic.sol.connection.Conexao;


public class ComponenteVeiculoServices {
	public static int save(ComponenteVeiculo componente){
		return save(componente, null);
	}
	public static int save(ComponenteVeiculo componente, Connection connect){
		return save(componente, 0, connect);
	}
	public static int save(ComponenteVeiculo componente, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(componente==null){
				return -1;
			}
			
			int retorno;
			if(componente.getCdComponente()==0){
				retorno = ComponenteVeiculoDAO.insert(componente, connect);
			}
			else{
				retorno = ComponenteVeiculoDAO.update(componente, connect);
				retorno = retorno>0?componente.getCdComponente():retorno;
			}
			
			//agendar checkups
			if(retorno>0){
				Veiculo veiculo = VeiculoDAO.get(componente.getCdReferencia(), connect);
				ModeloVeiculo modelo = ModeloVeiculoDAO.get(veiculo.getCdModelo(), connect);
				Marca marca = MarcaDAO.get(modelo.getCdMarca(), connect);
				
				VeiculoCheckup checkup = null;
				ArrayList<VeiculoCheckupItem> itens = new ArrayList<VeiculoCheckupItem>();
				if(componente.getDtValidade()!=null){
					checkup = new VeiculoCheckup(0, //int cdCheckup,
												 0, //int cdTipoCheckup,
												 veiculo.getCdVeiculo(), //int cdVeiculo,
												 componente.getDtValidade(), //GregorianCalendar dtCheckup,
												 "VEICULO: "+marca.getNmMarca()+" "+modelo.getNmModelo()+"\n" +
												 "Validade vencida "+componente.getNmComponente(), //String txtObservacao,
												 0, //int cdViagem,
												 0, //int cdAgendamento,
												 null, //String txtDiagnostico,
												 VeiculoCheckupServices.ST_CHECKUP_ABERTO, //int stCheckup,
												 VeiculoCheckupServices.TP_ORIGEM_PADRAO_TROCA, //int tpOrigem,
												 componente.getDtValidade(), //GregorianCalendar dtPrazoConclusao,
												 cdUsuario,
												 cdUsuario); //int cdUsuarioResponsavel
					itens.add(new VeiculoCheckupItem(0, //int cdCheckupItem,
													 0, //int cdTipoCheckup,
													 0, //int cdItem,
													 0, //int cdCheckup,
													 componente.getCdComponente(), //int cdComponente,
													 0, //float vlItem,
													 "Trocar Peça/Acessório\nValidade Vencida", //String txtObservacao,
													 null, //String txtDiagnostico,
													 VeiculoCheckupItemServices.ST_VERIFICANDO));// int stCheckupItem
				}
				
				if(checkup!=null){
					
				}
			}
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteVeiculoServices.save: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteVeiculoServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -2;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int delete(int cdComponente) {
		return delete(cdComponente, null);
	}

	public static int delete(int cdComponente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdComponente<=0)
				return -1;
			
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//checkup
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_veiculo_checkup_item WHERE cd_componente=?");
			pstmt.setInt(1, cdComponente);
			pstmt.executeUpdate();
			
			//ocorrencia
			pstmt = connect.prepareStatement("DELETE FROM fta_ocorrencia WHERE cd_componente=?");
			pstmt.setInt(1, cdComponente);
			pstmt.executeUpdate();
			
			//manutencao
			pstmt = connect.prepareStatement("DELETE FROM fta_manutencao WHERE cd_componente=?");
			pstmt.setInt(1, cdComponente);
			pstmt.executeUpdate();
			
			if(ComponenteVeiculoDAO.delete(cdComponente, connect)<=0){
				Conexao.rollback(connect);
				return -2;
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
}
