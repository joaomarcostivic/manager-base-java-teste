package com.tivic.manager.crm;

import java.sql.Connection;
import java.util.GregorianCalendar;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.OcorrenciaArquivo;
import com.tivic.manager.grl.OcorrenciaArquivoDAO;

@DestinationConfig(enabled = false)
public class OcorrenciaServices {

	public static int insertArquivo(byte[] blbArquivo, String nmArquivo, int cdOcorrencia, String txtObservacao) {
		return insertArquivo(blbArquivo, nmArquivo, cdOcorrencia, txtObservacao, null);
	}

	public static int insertArquivo(byte[] blbArquivo, String nmArquivo, int cdOcorrencia, String txtObservacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(cdOcorrencia<=0)
				return -1;
			
			Ocorrencia ocorrencia = OcorrenciaDAO.get(cdOcorrencia, connect);
			if(ocorrencia==null)
				return -2;
			
			int retorno;
			Arquivo arquivo = new Arquivo(0, nmArquivo, new GregorianCalendar(), nmArquivo, blbArquivo, 0, null/*dtCriacao*/, 0/*cdUsuario*/,0/*stArquivo*/, null /*dtInicial*/, null/*dtFinal*/, 0/*cdAssinatura*/, null/*txtOcr*/, 0, null, null);
			if((retorno = ArquivoDAO.insert(arquivo, connect)) >0){
				OcorrenciaArquivo ocorrenciaArquivo = new OcorrenciaArquivo(cdOcorrencia, retorno, txtObservacao);
				retorno = OcorrenciaArquivoDAO.insert(ocorrenciaArquivo, connect);
			}
			
			if(retorno<0){
				Conexao.rollback(connect);
				return -3;
			}
			else if (isConnectionNull)
				connect.commit();
			
			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaServices.insertArquivo: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int deleteArquivo(int cdOcorrencia, int cdArquivo) {
		return deleteArquivo(cdOcorrencia, cdArquivo, null);
	}

	public static int deleteArquivo(int cdOcorrencia, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(cdOcorrencia<=0)
				return -1;
			if(cdArquivo<=0)
				return -2;
			
			int retorno;
			if((retorno = OcorrenciaArquivoDAO.delete(cdOcorrencia, cdArquivo, connect)) >0)
				retorno = ArquivoDAO.delete(cdArquivo, connect);
			
			if(retorno<0){
				Conexao.rollback(connect);
				return -3;
			}
			else if (isConnectionNull)
				connect.commit();
			
			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaServices.deleteArquivo: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
