package com.tivic.manager.wsdl.detran.sp.incluirautoinfracao;

import java.sql.Connection;
import java.util.GregorianCalendar;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitDAO;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDetranServices;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.ArquivoMovimento;
import com.tivic.manager.mob.ServicoDetranConsultaServices;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.sp.DadosRetornoSP;
import com.tivic.manager.wsdl.detran.sp.ServicoDetranObjetoSP;
import com.tivic.manager.wsdl.interfaces.DetranRegistro;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.sol.connection.Conexao;

public class IncluirAutoInfracaoRegistro extends DetranRegistro {
	
	
	public IncluirAutoInfracaoRegistro() throws Exception {
		super();
	}

	@Override
	public void registrar(ServicoDetranObjeto servicoDetranObjeto, boolean lgHomologacao) {
		Connection connection = null;
		try{
			connection = Conexao.conectar();
			connection.setAutoCommit(false);
			
			ServicoDetranObjetoSP servicoDetranObjetoSp = (ServicoDetranObjetoSP)servicoDetranObjeto;
			AitMovimento aitMovimento = servicoDetranObjetoSp.getAitMovimento();
			Ait ait =  atualizarAit(servicoDetranObjetoSp, aitMovimento, connection);
			ArquivoMovimento arquivoMovimento = gerarArquivoMovimento(servicoDetranObjetoSp, aitMovimento);
			AitMovimentoDetranServices.save(new AitDetranObject(ait, aitMovimento, arquivoMovimento), null, connection);
			connection.commit();
			
		} catch(Exception e){
			e.printStackTrace();
			Conexao.rollback(connection);
			System.out.println(e.getMessage());
		} finally{
			Conexao.desconectar(connection);
		}
	}

	private Ait atualizarAit(ServicoDetranObjetoSP servicoDetranObjetoSp, AitMovimento aitMovimento, Connection connection) throws Exception{
		DadosRetornoSP incluirAutoInfracaoDadosRetorno = (DadosRetornoSP) servicoDetranObjetoSp.getDadosRetorno();
		return incluirAutoInfracaoDadosRetorno.getCodigoRetorno() == 0 ? 
				atualizarEnvioDetran(servicoDetranObjetoSp, aitMovimento, connection) : atualizarErro(servicoDetranObjetoSp, aitMovimento, connection);
	}
	
	private Ait atualizarEnvioDetran(ServicoDetranObjetoSP servicoDetranObjetoSP, AitMovimento aitMovimento, Connection connection) throws Exception{
		aitMovimento.setLgEnviadoDetran(AitMovimentoServices.ENVIADO_AGUARDANDO);
		aitMovimento.setDtRegistroDetran(new GregorianCalendar());
		Ait ait = AitDAO.get(aitMovimento.getCdAit(), connection);
		if(ait == null)
			throw new Exception("Ait do movimento nao encontrado");
		ait.setNrErro(String.valueOf(ServicoDetranConsultaServices.CODIGO_RETORNO_SUCESSO));
		ait.setLgEnviadoDetran(AitMovimentoServices.ENVIADO_AGUARDANDO);
		ait.setDtRegistroDetran(new GregorianCalendar());
		return ait;
	}
	
	private Ait atualizarErro(ServicoDetranObjetoSP servicoDetranObjetoSP, AitMovimento aitMovimento, Connection connection) throws Exception{
		Ait ait = servicoDetranObjetoSP.getAit();
		aitMovimento.setLgEnviadoDetran(AitMovimentoServices.NAO_ENVIADO);		
		aitMovimento.setNrErro(String.valueOf(servicoDetranObjetoSP.getDadosRetorno().getCodigoRetorno()));
		ait.setLgEnviadoDetran(AitMovimentoServices.NAO_ENVIADO);
		ait.setNrErro(String.valueOf(servicoDetranObjetoSP.getDadosRetorno().getCodigoRetorno()));
		return ait;
	}
	
}
