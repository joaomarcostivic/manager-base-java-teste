package com.tivic.manager.crm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;

import sol.dao.ResultSetMap;

@DestinationConfig(enabled = false)
public class CentralAtendimentoServices {

	public static ArrayList<SessaoAtendimento> sessoesAtendimento = new ArrayList<SessaoAtendimento>();

	public static int save(int cdCental, int cdEmpresa, String nmCentral, String dsCentral, String idCentral, String txtMensagemInicial){
		return save(cdCental, cdEmpresa, nmCentral, dsCentral, idCentral, txtMensagemInicial, null);
	}
	public static int save(int cdCental, int cdEmpresa, String nmCentral, String dsCentral, String idCentral, String txtMensagemInicial, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			CentralAtendimento central = new CentralAtendimento(cdCental, cdEmpresa, nmCentral, dsCentral, idCentral, txtMensagemInicial);

			int retorno;
			if(cdCental==0)
				retorno = CentralAtendimentoDAO.insert(central, connect);
			else {
				retorno = CentralAtendimentoDAO.update(central, connect);
				retorno = retorno>0?cdCental:retorno;
			}
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CentralAtendimentoServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCentral) {
		return delete(cdCentral, null);
	}

	public static int delete(int cdCentral, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdCentral<=0)
				return -1;

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			// Atendentes
			connect.prepareStatement("DELETE FROM crm_atendente WHERE cd_central="+cdCentral).executeUpdate();

			if(CentralAtendimentoDAO.delete(cdCentral)<=0){
				Conexao.rollback(connect);
				return -2;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CentralAtendimentoServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -3;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getCentralAtendimentoByEmpresa(int cdEmpresa) {
		return getCentralAtendimentoByEmpresa(cdEmpresa, null);
	}

	public static ResultSetMap getCentralAtendimentoByEmpresa(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("SELECT *, " +
					  "(SELECT COUNT(*) FROM crm_atendente B WHERE B.cd_central = A.cd_central) as QT_ATENDENTES " +
				      "FROM crm_central_atendimento A " +
				      "WHERE cd_empresa = ? ORDER BY A.NM_CENTRAL");
			pstmt.setInt(1, cdEmpresa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getCentralAtendimentoByUsuario(int cdUsuario) {
		return getCentralAtendimentoByUsuario(cdUsuario, null);
	}

	public static ResultSetMap getCentralAtendimentoByUsuario(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("SELECT B.* " +
				      "FROM crm_atendente A " +
				      "JOIN crm_central_atendimento B ON (A.cd_central=B.cd_central) "+
				      "JOIN seg_usuario C ON (C.cd_usuario=A.cd_usuario) "+
				      "WHERE A.cd_usuario   = "+cdUsuario+
				      "  AND A.st_atendente = 1");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAtendentes(int cdCentral) {
		return getAtendentes(cdCentral, false, null);
	}

	public static ResultSetMap getAtendentes(int cdCentral, boolean lgAgenteVendas) {
		return getAtendentes(cdCentral, lgAgenteVendas, null);
	}

	public static ResultSetMap getAtendentes(int cdCentral, boolean lgAgenteVendas, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int cdVinculoAgente = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_AGENTE_CREDITO", 0);
			String sqlAdicional = "";
			if(lgAgenteVendas){
				sqlAdicional = " AND EXISTS (SELECT * FROM crm_central_atendimento CA, grl_pessoa_empresa PE " +
							   "             WHERE CA.cd_central = " +cdCentral+
							   "               AND CA.cd_empresa = PE.cd_empresa "+
							   "               AND PE.cd_pessoa  = C.cd_pessoa " +
							   "               AND PE.cd_vinculo = "+cdVinculoAgente+")";
			}

			return new ResultSetMap(connect.prepareStatement("SELECT A.*, C.* " +
													      	 "FROM crm_atendente A " +
													      	 "LEFT OUTER JOIN seg_usuario B ON (A.CD_USUARIO=B.CD_USUARIO) "+
													      	 "LEFT OUTER JOIN grl_pessoa  C ON (B.CD_PESSOA=C.CD_PESSOA) "+
													      	 "WHERE cd_central   = " +cdCentral+
													      	 "  AND st_atendente = 1 "+
													      	sqlAdicional+
													      	 "ORDER BY C.nm_pessoa").executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int addSessaoAtendimento(Atendente atendente){
		boolean add = true;
		for(int i=0; i<sessoesAtendimento.size(); i++){
			if(sessoesAtendimento.get(i).getAtendente().getCdCentral() == atendente.getCdCentral() &&
			   sessoesAtendimento.get(i).getAtendente().getCdUsuario() == atendente.getCdUsuario()) {
				add = false;
			}
		}
		if(add){
			sessoesAtendimento.add(new SessaoAtendimento(atendente));
			return 1;
		}
		else
			return 0;
	}

	public static int removeSessaoAtendimento(Atendente atendente){
		for(int i=0; i<sessoesAtendimento.size(); i++){
			if(sessoesAtendimento.get(i).getAtendente().getCdCentral() == atendente.getCdCentral() &&
			   sessoesAtendimento.get(i).getAtendente().getCdUsuario() == atendente.getCdUsuario()) {
				sessoesAtendimento.remove(i);
				return 1;
			}
		}
		return 0;
	}

	public static SessaoAtendimento getSessaoAtendimentoDisponivel(int cdCentral){
		SessaoAtendimento sessao = null;
		for(int i=0; i<sessoesAtendimento.size(); i++){
			if(sessoesAtendimento.get(i).getAtendente().getCdCentral()==cdCentral){
				if(i==0)
					sessao = sessoesAtendimento.get(i);
				else if(sessoesAtendimento.get(i).getQtAtendimentos() < sessao.getQtAtendimentos())
					sessao = sessoesAtendimento.get(i);
			}
		}
		return sessao;
	}

	public static HashMap<String,Object> startAtendimento(int cdCentral, String nmAtendido){
		HashMap<String, Object> retorno = new HashMap<String, Object>();
		SessaoAtendimento sessao = getSessaoAtendimentoDisponivel(cdCentral);
		if(sessao!=null){
			String idAtendimento = String.valueOf(System.currentTimeMillis());
			sessao.addAtendimento(new SalaAtendimento(idAtendimento, "Iniciando atendimento "+idAtendimento, nmAtendido, ""));
			retorno.put("SESSAO_ATENDIMENTO", sessao);
			retorno.put("ID_ATENDIMENTO", idAtendimento);
		}
		return retorno;
	}

	public static int finishAtendimento(String idAtendimento){
		for(int i=0; i<sessoesAtendimento.size(); i++){
			for(int j=0; j<sessoesAtendimento.get(i).getQtAtendimentos(); j++){
				if(sessoesAtendimento.get(i).getAtendimento(j).getIdAtendimento().equals(idAtendimento)){
					return sessoesAtendimento.get(i).removeAtendimento(j);
				}
			}
		}
		return 0;
	}

	public static String getXMPPServer(String remoteIP){
		String nmServidorXMPPInterno = ParametroServices.getValorOfParametro("NM_SERVIDOR_XMPP_INTERNO");
		String nmServidorXMPPExterno = ParametroServices.getValorOfParametro("NM_SERVIDOR_XMPP_EXTERNO");

		String[] servers = {remoteIP, nmServidorXMPPInterno, nmServidorXMPPExterno};
		try{
			for(int i=0; i<servers.length; i++){
				System.out.println("Tentando conexao com "+ servers[i]);
				try {

					SocketAddress sockaddr = new InetSocketAddress(servers[i], 5222);
					Socket sock = new Socket();
					sock.connect(sockaddr, 2500);

					if(sock.isConnected()){
						sock.close();
						if(i==0)
							return "localhost";
						else
							return servers[i];
					}
				}
				catch(IOException e) {
					//System.out.println("Conexao falhou: "+ e);
		        }
				//System.out.println("Conexao falhou...");
			}
			return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CentralAtendimentoServices.getXMPPServer: " +  e);
			return  null;
		}
	}
}
