package com.tivic.manager.fsc;

import inutNFe_v310.TInutNFe;
import inutNFe_v310.TInutNFe.InfInut;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.commons.httpclient.protocol.Protocol;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;
import br.com.javac.v100.eventocanc.TEnvEvento;
import br.com.javac.v100.eventocanc.TEvento;
import br.com.javac.v100.eventocanc.TEvento.InfEvento;
import br.com.javac.v100.eventocanc.TEvento.InfEvento.DetEvento;
import br.com.javac.v101.dpec.TDPEC;
import br.com.javac.v101.dpec.TDPEC.InfDPEC;
import br.com.javac.v101.dpec.TDPEC.InfDPEC.IdeDec;
import br.com.javac.v101.dpec.TDPEC.InfDPEC.ResNFe;
import br.com.javac.v200.cancnfe.TCancNFe;
import br.com.javac.v200.cancnfe.TCancNFe.InfCanc;
//import br.com.javac.v200.inutnfe.TInutNFe;
//import br.com.javac.v200.inutnfe.TInutNFe.InfInut;
import br.inf.portalfiscal.www.nfe.wsdl.cadconsultacadastro2.CadConsultaCadastro2Stub;
import br.inf.portalfiscal.www.nfe.wsdl.nfeautorizacao.NfeAutorizacaoStub;
import br.inf.portalfiscal.www.nfe.wsdl.nfeconsulta2.NfeConsulta2Stub;
import br.inf.portalfiscal.www.nfe.wsdl.nferetautorizacao.NfeRetAutorizacaoStub;
import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.RecepcaoEventoStub;
import br.inf.portalfiscal.www.nfe.wsdl.scerecepcaorfb.SCERecepcaoRFBStub;

import com.tivic.manager.adm.EntradaEventoFinanceiro;
import com.tivic.manager.adm.EntradaEventoFinanceiroDAO;
import com.tivic.manager.adm.NaturezaOperacao;
import com.tivic.manager.adm.NaturezaOperacaoDAO;
import com.tivic.manager.adm.SaidaItemAliquotaServices;
import com.tivic.manager.adm.TributoAliquotaServices;
import com.tivic.manager.adm.TributoServices;
import com.tivic.manager.alm.DocumentoEntrada;
import com.tivic.manager.alm.DocumentoEntradaDAO;
import com.tivic.manager.alm.DocumentoEntradaItem;
import com.tivic.manager.alm.DocumentoEntradaItemDAO;
import com.tivic.manager.alm.DocumentoSaidaItem;
import com.tivic.manager.alm.DocumentoSaidaItemDAO;
import com.tivic.manager.alm.DocumentoSaidaServices;
import com.tivic.manager.alm.EntradaDeclaracaoImportacaoServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaDAO;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.Pais;
import com.tivic.manager.grl.PaisDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.grl.ProdutoServico;
import com.tivic.manager.grl.ProdutoServicoDAO;
import com.tivic.manager.grl.ProdutoServicoServices;
import com.tivic.manager.grl.UnidadeMedida;
import com.tivic.manager.grl.UnidadeMedidaDAO;
import com.tivic.manager.seg.CertificadoServices;
import com.tivic.manager.util.Util;

import enviNFe_v310.ObjectFactory;
import enviNFe_v310.TEnderEmi;
import enviNFe_v310.TEndereco;
import enviNFe_v310.TEnviNFe;
import enviNFe_v310.TIpi;
import enviNFe_v310.TIpi.IPINT;
import enviNFe_v310.TIpi.IPITrib;
import enviNFe_v310.TNFe;
import enviNFe_v310.TNFe.InfNFe;
import enviNFe_v310.TNFe.InfNFe.AutXML;
import enviNFe_v310.TNFe.InfNFe.Dest;
import enviNFe_v310.TNFe.InfNFe.Det;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSOutr;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSQtde;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.COFINSST;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS00;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS10;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS20;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS30;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS40;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS51;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS60;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS70;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS90;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.II;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.PIS;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISAliq;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISNT;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISOutr;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISQtde;
import enviNFe_v310.TNFe.InfNFe.Det.Imposto.PISST;
import enviNFe_v310.TNFe.InfNFe.Det.Prod;
import enviNFe_v310.TNFe.InfNFe.Det.Prod.DI;
import enviNFe_v310.TNFe.InfNFe.Det.Prod.DI.Adi;
import enviNFe_v310.TNFe.InfNFe.Emit;
import enviNFe_v310.TNFe.InfNFe.Ide;
import enviNFe_v310.TNFe.InfNFe.Ide.NFref;
import enviNFe_v310.TNFe.InfNFe.InfAdic;
import enviNFe_v310.TNFe.InfNFe.Total;
import enviNFe_v310.TNFe.InfNFe.Total.ICMSTot;
import enviNFe_v310.TNFe.InfNFe.Total.RetTrib;
import enviNFe_v310.TNFe.InfNFe.Transp;
import enviNFe_v310.TNFe.InfNFe.Transp.Transporta;
import enviNFe_v310.TNFe.InfNFe.Transp.Vol;
import enviNFe_v310.TUf;
import enviNFe_v310.TUfEmi;
import enviNFe_v310.TVeiculo;

public class NfeServices {
	/*TIPO DE AMBIENTE*/
	public static final int PRODUCAO    = 1;
	public static final int HOMOLOGACAO = 2;
	
	/*ETAPA DA NOTA FISCAL*/
	public static final int CANCELAMENTO      = 1; 
	public static final int CONSULTA_CADASTRO = 2;
	public static final int ENVIO             = 3;
	public static final int INUTILIZACAO      = 4;
	public static final int EPEC      		  = 5;
	public static final int CANCELAMENTO_PROC = 6;
	
	private static ArrayList<String> listaComErrosDeValidacao = new ArrayList<String>();  
	
	private static final String INFINUT   = "infInut";  
    private static final String INFEVENTO = "infEvento";
    private static final String INFCANC   = "infCanc";
    private static final String INFDPEC   = "infDPEC";  
    private static final String NFE       = "NFe";  
  
	public static String getCdUfDefault()	{
		return getCdUfDefault(null);
	}
	
	public static String getCdUfDefault(Connection connect)	{
		return ParametroServices.getValorOfParametro("CD_UF_PADRAO", "29", connect == null ? Conexao.conectar() : connect); 
	}
	
	private static final int SSL_PORT = 443;
	
	/*TIPO DE AMBIENTE*/
	private static int tpAmbiente = -1;
	public static int getTipoAmbiente(int cdEmpresa)	{
		if(tpAmbiente==-1)
			tpAmbiente = ParametroServices.getValorOfParametroAsInteger("TP_AMBIENTE", PRODUCAO, cdEmpresa, Conexao.conectar());
		return tpAmbiente;
	}
	
	
	/**
	 * Gera Número de Identificação da NFe, a partir do Código ele busca os campos necessários e gera um ID de tamanho fixo
	 * 
	 * @param cdNotaFiscal
	 * @param connect
	 * @return result com o Número da chave e o dígito verificador
	 */
	public static Result gerarNfeId(int cdNotaFiscal, Connection connect) {  
        try {  
        	NotaFiscal nota        = NotaFiscalDAO.get(cdNotaFiscal, connect);
        	GregorianCalendar data = nota.getDtEmissao();
        	String ano = ("" + data.get(Calendar.YEAR)).substring(2);
        	String mes = (((data.get(Calendar.MONTH) + 1) < 10) ? "0" + (data.get(Calendar.MONTH) + 1) : "" + (data.get(Calendar.MONTH) + 1));
        	PessoaJuridica emitente = PessoaJuridicaDAO.get(nota.getCdEmpresa());
        	int cnf 		= (int)(Math.random() * 99999999);
        	
        	String cUF      = NfeServices.getCdUfDefault(connect);          // Código da UF do emitente do Documento Fiscal.  
            String dataAAMM = ano + mes;                             // Ano e Mês de emissão da NF-e.  
            String cnpjCpf  = Util.formatCnpj(emitente.getNrCnpj()); // CNPJ do emitente.  
            String mod      = "55";                                  // Modelo do Documento Fiscal.  
            String serie    = nota.getNrSerie();                     // Série do Documento Fiscal.  
            String nNF      = nota.getNrNotaFiscal();                // Número do Documento Fiscal.  
            String tpEmis   = String.valueOf(nota.getTpEmissao());   // Forma de emissão da NF-e  
            String cNF      = String.valueOf(cnf);                   // Código Numérico que cópia a Chave de Acesso.  
              
            StringBuilder chave = new StringBuilder();  
            chave.append(Util.fill(cUF, 2, '0', 'E'));  
            chave.append(Util.fill(dataAAMM, 4, '0', 'E'));  
            chave.append(Util.fill(cnpjCpf.replaceAll("\\D",""), 14, '0', 'E'));
            chave.append(Util.fill(mod, 2, '0', 'E'));  
            chave.append(Util.fill(serie, 3, '0', 'E'));  
            chave.append(Util.fill(nNF, 9, '0', 'E'));  
            chave.append(Util.fill(tpEmis, 1, '0', 'E'));  
            chave.append(Util.fill(cNF, 8, '0', 'E'));  
            int dv = Util.modulo11(chave.toString());
            chave.append(dv);
            
            chave.insert(0, "NFe"); 
            
            info("Chave NF-e: " + chave.toString()); 
            
            Result result = new Result(1); 
            result.addObject("nrChave", chave.toString());
            result.addObject("nrDv", dv);
            
            return result;
        } 
        catch (Exception e) {  
            error(e.toString());
            return new Result(-1, "Falha ao tentar gerar o ID da Nota Fiscal Eletrônica!", e);
        } 
        finally {
			Conexao.desconectar(connect);
		}
  	}
	  
	private static Result getNumeroRecibo(String xml)	{
		try {
			InputSource inStream = new InputSource();  
	        inStream.setCharacterStream(new StringReader(xml));  
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	        factory.setNamespaceAware(true);  
			SAXBuilder saxObj     = new SAXBuilder();
			org.jdom.Document doc = saxObj.build(inStream);
			Namespace ns          = Namespace.getNamespace("http://www.portalfiscal.inf.br/nfe");
	    	Element cStat         = (Element)doc.getRootElement().getChild("cStat", ns);
	    	
	    	if(cStat != null && cStat.getValue().equals("999"))
	    		return new Result(-1, "Erro não identificado!");
	    	
	    	Element nRec          = (Element)doc.getRootElement().getChild("infRec", ns).getChild("nRec", ns);
			Element xMotivo       = (Element)doc.getRootElement().getChild("xMotivo", ns);
			Result resultado = new Result(1, xMotivo.getValue());
			resultado.addObject("recibo", nRec.getValue());
			return resultado;
		} 
		catch (Exception ex) {
			ex.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar pegar o número do Recibo!", ex);
		}
	}
	
	private static Result getNumeroReciboDpec(String xml)	{
		try {
			InputSource inStream = new InputSource();  
	        inStream.setCharacterStream(new StringReader(xml));  
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	        factory.setNamespaceAware(true);  
			SAXBuilder saxObj     = new SAXBuilder();
			org.jdom.Document doc = saxObj.build(inStream);
			Namespace ns          = Namespace.getNamespace("http://www.portalfiscal.inf.br/nfe");
	    	Element cStat         = (Element)doc.getRootElement().getChild("infDPECReg", ns).getChild("cStat", ns);
	    	
	    	if(cStat != null && cStat.getValue().equals("999"))
	    		return new Result(-1, "Erro não identificado!");
	    	
	    	Element nRec          = (Element)doc.getRootElement().getChild("infDPECReg", ns).getChild("nRegDPEC", ns);
			Element xMotivo       = (Element)doc.getRootElement().getChild("infDPECReg", ns).getChild("xMotivo", ns);
			Result resultado = new Result(1, xMotivo.getValue());
			if(nRec != null)
				resultado.addObject("recibo", nRec.getValue());
			return resultado;
		} 
		catch (Exception ex) {
			ex.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar pegar o número do Recibo!", ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static String[] getRetornoSEFAZ(String xml){
		try {
			InputSource inStream = new InputSource();  
	        inStream.setCharacterStream(new StringReader(xml));  
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	        factory.setNamespaceAware(true);  
			SAXBuilder saxObj     = new SAXBuilder();
			org.jdom.Document doc = saxObj.build(inStream);
			Namespace ns          = Namespace.getNamespace("http://www.portalfiscal.inf.br/nfe");
			Element cStat = null;
			Element nProt = null;
			Element xMot = null;
			Element tpAmb = null;
			Element verAplic = null;
			Element chNFe = null;
			Element dhRecbto = null;
			Element digVal = null;
			List<Attribute> id = null;
			if((Element)doc.getRootElement().getChild("protNFe", ns) != null){
				cStat         = (Element)doc.getRootElement().getChild("protNFe", ns).getChild("infProt", ns).getChild("cStat", ns);
				nProt         = (Element)doc.getRootElement().getChild("protNFe", ns).getChild("infProt", ns).getChild("nProt", ns);
				xMot          = (Element)doc.getRootElement().getChild("protNFe", ns).getChild("infProt", ns).getChild("xMotivo", ns);
				tpAmb         = (Element)doc.getRootElement().getChild("protNFe", ns).getChild("infProt", ns).getChild("tpAmb", ns);
				verAplic      = (Element)doc.getRootElement().getChild("protNFe", ns).getChild("infProt", ns).getChild("verAplic", ns);
				chNFe         = (Element)doc.getRootElement().getChild("protNFe", ns).getChild("infProt", ns).getChild("chNFe", ns);
				dhRecbto      = (Element)doc.getRootElement().getChild("protNFe", ns).getChild("infProt", ns).getChild("dhRecbto", ns);
				digVal        = (Element)doc.getRootElement().getChild("protNFe", ns).getChild("infProt", ns).getChild("digVal", ns);
				id     	      = (List<Attribute>)doc.getRootElement().getChild("protNFe", ns).getChild("infProt", ns).getAttributes();
			}
			else{
				cStat         = (Element)doc.getRootElement().getChild("cStat", ns);
				xMot          = (Element)doc.getRootElement().getChild("xMotivo", ns);
			}
	    	
	    	String[] retorno = new String[9];
	    	
	    	retorno[0] = (tpAmb != null) ? tpAmb.getValue() : "0";
	    	retorno[1] = (verAplic != null) ? verAplic.getValue() : "";
	    	retorno[2] = (dhRecbto != null) ? dhRecbto.getValue() : "";
	    	retorno[3] = (chNFe != null) ? chNFe.getValue() : "";
	    	retorno[4] = (nProt != null) ? nProt.getValue() : "";
	    	retorno[5] = (digVal != null) ? digVal.getValue() : "";
	    	retorno[6] = cStat.getValue();
	    	retorno[7] = xMot.getValue();
	    	retorno[8] = (id != null && id.size() > 0) ? id.get(0).getValue() : "";
	    	return retorno;
		}
		catch (Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	/**
	 * Método que baixa todos os xml's de um período na pasta padrão dos xml's
	 * 
	 * @param criterios
	 * @author Luiz Romario Filho
	 * @since 08/05/2015
	 */
	public static Result gerarXmlNFE(ArrayList<ItemComparator> criterios){
		Connection connection = Conexao.conectar();
		Empresa empresa = EmpresaServices.getDefaultEmpresa(connection);
		int cdEmpresa = 0;
		String where = " WHERE 1 = 1 ";
		if(criterios != null){
			for (ItemComparator itemComparator : criterios) {
				if(!isNullOrBlank(itemComparator.getValue())){
					if(itemComparator.getColumn().equalsIgnoreCase("cd_empresa")){
						cdEmpresa = Integer.parseInt(itemComparator.getValue());
						where+= " AND cd_empresa = " + cdEmpresa;
					} else if(itemComparator.getColumn().equalsIgnoreCase("dt_inicio")){
						where+= " AND dt_emissao >= '" + itemComparator.getValue() + "'";
					} else if(itemComparator.getColumn().equalsIgnoreCase("dt_fim")){
						where+= " AND dt_emissao <= '" + itemComparator.getValue() + "'";
					} else if(itemComparator.getColumn().equalsIgnoreCase("tp_nota_fiscal")){
						if(Integer.parseInt(itemComparator.getValue()) > 0)
							where+= " AND tp_emissao = "+ itemComparator.getValue();
					} else if(itemComparator.getColumn().equalsIgnoreCase("st_nota_fiscal")){
						if(Integer.parseInt(itemComparator.getValue()) > 0)
							where+= " AND st_nota_fiscal = "+ itemComparator.getValue();	
					} else if(itemComparator.getColumn().equalsIgnoreCase("nr_chave_acesso")){
						where+= " AND nr_chave_acesso ILIKE '%"+ itemComparator.getValue() +"%'";	
					} else if(itemComparator.getColumn().equalsIgnoreCase("nr_cpf_cnpj_destinatario")){
						// TODO pesquisar como fazer esse join ainda... 
					} else if(itemComparator.getColumn().equalsIgnoreCase("nr_serie")){
						where+= " AND nr_serie ILIKE '%"+ itemComparator.getValue() +"%'";					
					} else if(itemComparator.getColumn().equalsIgnoreCase("nr_inicio")){
						where+= " AND nr_nota_fiscal >= '"+ itemComparator.getValue() +"'";	
					} else if(itemComparator.getColumn().equalsIgnoreCase("nr_fim")){
						where+= " AND nr_nota_fiscal <= '"+ itemComparator.getValue() +"'";	
					}
				}
			}
		}
		cdEmpresa = cdEmpresa > 0 ? cdEmpresa : empresa.getCdEmpresa();
		String path = ParametroServices.getValorOfParametro("NM_REPOSITORIO_XML", cdEmpresa, connection);
		try {
			
			String sql = "SELECT * FROM fsc_nota_fiscal ";
			sql += where;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSetMap resultSetMap = new ResultSetMap(preparedStatement.executeQuery());
			
			System.out.println(sql);
			if(resultSetMap == null || resultSetMap.getLines().isEmpty()){
				return new Result(1, "Nenhuma nota foi baixada.");
			} else {
				while(resultSetMap.next()){
					int cdNotaFiscal = resultSetMap.getInt("CD_NOTA_FISCAL");
					String nrChaveAcesso = resultSetMap.getString("NR_CHAVE_ACESSO");
					System.out.printf("Nota = %d empresa = %d \n", cdNotaFiscal, cdEmpresa);
					String xml = getXmlComProtocolo(cdNotaFiscal, (NotaFiscalHistorico) null, cdEmpresa, connection);
					File file = new File(path + "/download/");
					if(!file.exists()){
						if(!file.mkdirs()) {
							return new Result(-1, "Erro ao criar pasta para salvar xml das notas fiscais.");
						}
					}
					try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(file.getCanonicalPath() + "\\" + nrChaveAcesso +".xml")), "utf-8"))){
						writer.write(xml);
						writer.close();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						return new Result(-1, "Erro ao salvar o xml das notas fiscais.");
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						return new Result(-1, "Erro ao salvar o xml das notas fiscais.");
					} catch (IOException e) {
						e.printStackTrace();
						return new Result(-1, "Erro ao salvar o xml das notas fiscais.");		
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao pesquisar as notas fiscais.");
		}
		
		return new Result(1, "Xml's baixados com sucesso na pasta " + path);
		
	}
	
	public static Result gerarXmlNFE(int cdNotaFiscal, int cdEmpresa) {
		return gerarXmlNFE(cdNotaFiscal, cdEmpresa, -1, null);
	}
	
	public static Result gerarXmlNFE(int cdNotaFiscal, int cdEmpresa, int tpEmi) {		
		return gerarXmlNFE(cdNotaFiscal, cdEmpresa, tpEmi, null);
	}
	
	public static Result gerarXmlNFE(int cdNotaFiscal, int cdEmpresa, Connection connect) {  
		return gerarXmlNFE(cdNotaFiscal, cdEmpresa, -1, connect);
	}
	
	public static Result gerarXmlNFE(int cdNotaFiscal, int cdEmpresa, int tpEmi, Connection connect) {
		boolean isConnectionNull = connect == null;		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
            String dsValidacao    = "";
            String dsValidacao2   = "";
            float vlII 			  = 0;
            float vlIPI 		  = 0;
            float vlPIS 		  = 0;
            float vlCOFINS 		  = 0;
            double vlBaseCalculo   = 0;
        	float vlICMS          = 0;
        	float vlICMSDeson     = 0;
        	float vlBaseCalculoST = 0; 
        	float vlICMSST        = 0;  
        	float vlDescontos     = 0;
        	@SuppressWarnings("unused")
			float vlAcrescimo     = 0;
        	float vlRetPIS 	  	  = 0;
        	float vlRetCOFINS 	  = 0;
        	//
        	ResultSetMap rsNfe = new ResultSetMap(connect.prepareStatement("SELECT A.*, B.nm_natureza_operacao, "+
													   "       B.nr_codigo_fiscal AS nr_cfop, C.nm_pessoa AS nm_fantasia, C.nr_telefone1, C.nr_telefone2, C.nr_fax, C.nr_celular, " + 
												   	   "       C.nm_email, " +
													   "       I.nr_telefone1 as contato_nr_telefone1 , I.nr_telefone2 AS contato_nr_telefone2, " +
								                       "       I.nr_celular AS contato_nr_celular, I.nr_celular2 AS contato_nr_celular2, I.nr_celular3 AS contato_nr_celular3, " +
													   " 	   I.nr_celular4 AS contato_nr_celular4, " +														
													   "       D.nr_cnpj AS nr_cnpj_emissor, D.nm_razao_social, D.nr_inscricao_estadual, D.nr_inscricao_municipal, "+
													   "       E.nm_logradouro, E.nm_bairro, E.nr_endereco, E.nr_cep, E.nm_complemento, "+
													   "       F.nm_cidade, F.id_ibge, G.sg_estado, G.cd_estado, G.cd_pais "+
													   "FROM fsc_nota_fiscal A "+
													   "JOIN adm_natureza_operacao B ON (A.cd_natureza_operacao = B.cd_natureza_operacao) "+
													   "JOIN grl_pessoa            C ON (A.cd_empresa           = C.cd_pessoa) "+
													   "JOIN grl_pessoa_juridica   D ON (A.cd_empresa           = D.cd_pessoa) "+
													   "JOIN grl_pessoa_endereco   E ON (A.cd_empresa           = E.cd_pessoa  "+
													   "                             AND E.lg_principal         = 1)           "+
													   "JOIN grl_cidade            F ON (E.cd_cidade            = F.cd_cidade) "+
													   "JOIN grl_estado            G ON (F.cd_estado            = G.cd_estado) "+
													   "LEFT OUTER JOIN grl_pessoa_contato I ON (C.cd_pessoa 	 = I.cd_pessoa )" +
													   "JOIN fsc_nota_fiscal_doc_vinculado   H ON (H.cd_nota_fiscal = A.cd_nota_fiscal) "+
													   "WHERE A.cd_nota_fiscal = " + cdNotaFiscal).executeQuery());
        	if(!rsNfe.next())
            	return new Result(-1, "Nota fiscal não localizada!");
            // Atualiza endereço de destino como o principal caso não exista
            int cdEnderecoDestinario = rsNfe.getInt("cd_endereco_destinatario");
            int cdEstadoDestinatario = 0;
            int cdPaisDestinatario = 0;
            PessoaEndereco endDestinatario = PessoaEnderecoServices.getEnderecoPrincipal(rsNfe.getInt("cd_destinatario"), connect);
            if(cdEnderecoDestinario <= 0) {
            	if(endDestinatario!=null) {
            		connect.prepareStatement(" UPDATE fsc_nota_fiscal " +
            				                 " SET cd_endereco_destinatario = "+endDestinatario.getCdEndereco()+
            				                 " WHERE cd_nota_fiscal         = "+rsNfe.getInt("cd_nota_fiscal")).executeUpdate();
            		cdEnderecoDestinario = endDestinatario.getCdEndereco();
            	}
            	
            }
            
        	Cidade cidadeDest = CidadeDAO.get(endDestinatario.getCdCidade(), connect);
        	Estado estadoDest = EstadoDAO.get(cidadeDest.getCdEstado(), connect);
        	cdEstadoDestinatario = estadoDest.getCdEstado();
        	Pais paisDest = PaisDAO.get(estadoDest.getCdPais(), connect);
        	cdPaisDestinatario = paisDest.getCdPais();
            // VALIDAÇÃO - EMITENTE
	        	// CNPJ do Emissor
	        if(rsNfe.getString("nr_cnpj_emissor") == null || rsNfe.getString("nr_cnpj_emissor").trim().equals(""))
	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CNPJ do emissor";
	        	// Logradouro
	        if(rsNfe.getString("nm_logradouro")==null || rsNfe.getString("nm_logradouro").trim().equals(""))
	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Logradouro do emissor";
	        	// Número
	        if(rsNfe.getString("nr_endereco") == null || rsNfe.getString("nr_endereco").trim().equals(""))
	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Número do imÃ³vel do emissor";
	        	// Bairro
	        if(rsNfe.getString("nm_bairro")==null || rsNfe.getString("nm_bairro").trim().equals(""))
	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Bairro do emissor";
        		// Cidade
	        if(rsNfe.getString("nm_cidade")==null || rsNfe.getString("nm_cidade").trim().equals(""))
	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Cidade do emissor";
	        	// IBGE
	        if(rsNfe.getString("id_ibge")==null || rsNfe.getString("id_ibge").trim().equals(""))
	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Cod. IBGE da cidade do emissor";
	        	// Estado
	        if(rsNfe.getString("sg_estado") == null)
	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Estado do emissor";
	        	//Telefone
	        
	        String telefone_emissor_1 =rsNfe.getString("nr_telefone1"); 
            String telefone_emissor_2 =rsNfe.getString("nr_telefone2"); 
            String fax_emissor =rsNfe.getString("nr_fax"); 
            String celular_emissor =rsNfe.getString("nr_celular");
            
            String contato_telefone_emissor_1 =rsNfe.getString("contato_nr_telefone1"); 
            String contato_telefone_emissor_2 =rsNfe.getString("contato_nr_telefone2"); 
            String contato_celular_emissor =rsNfe.getString("contato_nr_celular");
            String contato_celular_emissor_2 =rsNfe.getString("contato_nr_celular2");
            String contato_celular_emissor_3 =rsNfe.getString("contato_nr_celular3");
            String contato_celular_emissor_4 =rsNfe.getString("contato_nr_celular4");
            
            
	        if(isNullOrBlank(telefone_emissor_1) 
            		&& isNullOrBlank(telefone_emissor_2)
            		&& isNullOrBlank(fax_emissor) 
            		&& isNullOrBlank(celular_emissor)
            		&& isNullOrBlank(contato_celular_emissor)
            		&& isNullOrBlank(contato_celular_emissor_2)
            		&& isNullOrBlank(contato_celular_emissor_3)
            		&& isNullOrBlank(contato_celular_emissor_4)
            		&& isNullOrBlank(contato_telefone_emissor_1)
            		&& isNullOrBlank(contato_telefone_emissor_2)
	        		){
	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Telefone do emissor";
	        }
	        	//Email
	        if(rsNfe.getString("nm_email") == null || rsNfe.getString("nm_email").trim().equals(""))//28-12-2012
	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Email do emissor";
	        	//CFOP
	        if(rsNfe.getString("nr_cfop") == null || rsNfe.getString("nr_cfop").trim().equals(""))//14-03-2013
	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CFOP do emissor";
            if(!dsValidacao.equals(""))
            	return new Result(-1, "Dados do emitente faltando: "+dsValidacao);
            
	        //Pre-Validacao 2
	    	// CNPJ do Emissor
	        if(!Util.isCNPJ(rsNfe.getString("nr_cnpj_emissor")))
	        	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CNPJ";
	        if(rsNfe.getString("nm_logradouro")!=null && rsNfe.getString("nm_logradouro").trim().length()==1)
	        	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Logradouro - quantidade de caracteres deve ser maior do que 1(um)";
	        if(!NotaFiscalServices.validaSiglaEstado(rsNfe.getString("sg_estado")))
	        	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Sigla do estado";
	        String primeiroDig = rsNfe.getString("nr_cfop").trim().substring(0, 1);
	        if(rsNfe.getInt("tp_movimento") == NotaFiscalServices.MOV_ENTRADA && (primeiroDig.equals("5") || primeiroDig.equals("6") || primeiroDig.equals("7")))
	        	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CFOP de saida para Nfe de entrada";
	        if(rsNfe.getInt("tp_movimento") == NotaFiscalServices.MOV_SAIDA && (primeiroDig.equals("1") || primeiroDig.equals("2") || primeiroDig.equals("3")))
	        	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CFOP de entrada para Nfe de saida";
	        
            if( isNullOrBlank(telefone_emissor_1) 
            		&& isNullOrBlank(telefone_emissor_2)
            		&& isNullOrBlank(fax_emissor) 
            		&& isNullOrBlank(celular_emissor)
            		&& isNullOrBlank(contato_telefone_emissor_1)
            		&& isNullOrBlank(contato_telefone_emissor_2)
            		&& isNullOrBlank(contato_celular_emissor)
            		&& isNullOrBlank(contato_celular_emissor_2)
            		&& isNullOrBlank(contato_celular_emissor_3)
            		&& isNullOrBlank(contato_celular_emissor_4)) {
	        	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Telefone do emissor";
            }
	        
	        if(!dsValidacao2.equals(""))
            	return new Result(-1, "Dados do emitente inválidos: "+dsValidacao2);
	        
	        double vlTotalProdutos = 0;
            float vlSeguros       = rsNfe.getFloat("vl_seguro"); // Usado no total de ICMS, usar assim ou são quando tiver produtos que tenham ICMS?
        	float vlOutro         = rsNfe.getFloat("vl_outras_despesas");
        	float vlFrete         = rsNfe.getFloat("vl_frete");  // Usado no total de ICMS, usar assim ou são quando tiver produtos que tenham ICMS?
        	// 
            TNFe nFe      = new TNFe();  
            InfNFe infNFe = new InfNFe();
            infNFe.setId(rsNfe.getString("nr_chave_acesso"));  
            infNFe.setVersao("3.10");
            /*
             *  Identificação da NF-e
             */
            Ide ide = new Ide();  
            ide.setCUF(NfeServices.getCdUfDefault(connect));
            ide.setCNF(rsNfe.getString("nr_chave_acesso").substring(38, 46)); // Código Numérico que cópia a Chave de Acesso - Verificar intervalo
            ide.setNatOp(rsNfe.getString("nr_cfop"));
            ide.setIndPag(rsNfe.getString("tp_pagamento"));  
            ide.setMod(rsNfe.getString("tp_modelo"));  
            ide.setSerie(rsNfe.getString("nr_serie"));
            ide.setNNF(rsNfe.getString("nr_nota_fiscal"));
            ide.setDhEmi(Util.formatDate(rsNfe.getTimestamp("dt_emissao"), "yyyy-MM-dd'T'HH:mm:ss'-03:00'"));
            ide.setDhSaiEnt(Util.formatDate(rsNfe.getTimestamp("dt_movimentacao"), "yyyy-MM-dd'T'HH:mm:ss'-03:00'"));
//            ide.setHSaiEnt(Util.formatDate(rsNfe.getTimestamp("dt_movimentacao"), "HH:mm:ss"));  
            ide.setTpNF(rsNfe.getString("tp_movimento"));
            if(rsNfe.getInt("cd_pais") != cdPaisDestinatario)
            	ide.setIdDest("3");
            else if(rsNfe.getInt("cd_estado") != cdEstadoDestinatario)
            	ide.setIdDest("2");
            else
            	ide.setIdDest("1");
            ide.setIndFinal(rsNfe.getString("lg_consumidor_final"));
            ide.setIndPres(rsNfe.getString("tp_venda_presenca"));
        	ide.setCMunFG(rsNfe.getString("id_ibge"));
            ide.setTpImp(rsNfe.getString("tp_danfe"));
            ide.setTpEmis(tpEmi == -1 ? rsNfe.getString("tp_emissao") : String.valueOf(tpEmi));
            ide.setCDV(rsNfe.getString("nr_chave_acesso").substring(46));   
            ide.setTpAmb(String.valueOf(getTipoAmbiente(cdEmpresa)));
            ide.setFinNFe(rsNfe.getString("tp_finalidade"));
            if(rsNfe.getInt("tp_finalidade") == NotaFiscalServices.NFE_DE_DEVOLUCAO){
            	if(rsNfe.getString("nr_chave_acesso_referencia") != null){
            		NFref nfRef = new NFref();
        			nfRef.setRefNFe(rsNfe.getString("nr_chave_acesso_referencia").trim());
        			ide.getNFref().add(nfRef);
            	}
            	else{
	            	ResultSetMap rsmEntradas = new ResultSetMap(connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_doc_vinculado WHERE cd_nota_fiscal = " + rsNfe.getInt("cd_nota_fiscal")).executeQuery());
	            	while(rsmEntradas.next()){
	            		DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(rsmEntradas.getInt("cd_documento_entrada"), connect);
	            		if(docEntrada != null){
		            		ResultSetMap rsmNotaFiscalRef = new ResultSetMap(connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_doc_vinculado WHERE cd_documento_saida = " + docEntrada.getCdDocumentoSaidaOrigem()).executeQuery());
		            		if(rsmNotaFiscalRef.next()){
		            			NotaFiscal notaRef = NotaFiscalDAO.get(rsmNotaFiscalRef.getInt("cd_nota_fiscal"), connect);
		            			NFref nfRef = new NFref();
		            			nfRef.setRefNFe(notaRef.getNrChaveAcesso().substring(3));
		            			ide.getNFref().add(nfRef);
		            		}
	            		}
	            	}
            	}
            }
            ide.setProcEmi("0");     // 0 - emissão de NF-e com aplicativo do contribuinte;
            ide.setVerProc("1.0");   // Versão do aplicativo emissor
            if(tpEmi == NotaFiscalServices.EMI_CONTIGENCIA_COM_SCAN){
            	ide.setDhCont(Util.formatDate(Util.getDataAtual(), "yyyy-MM-dd'T'HH:mm:ss'-03:00'"));
	            ide.setXJust("EMISSAO EM CONTINGENCIA DEVIDO A PROBLEMAS COM O ENVIO PARA SEFAZ DE ORIGEM.");
	        }
            infNFe.setIde(ide);      // Adicionando os dados de Identificação da NF-e
            /*
             *  Dados do Emissor
             */
            //
            Emit emit = new Emit();  
        	emit.setCNPJ(rsNfe.getString("nr_cnpj_emissor")); 
            emit.setXNome(rsNfe.getString("nm_razao_social").trim());  
            emit.setXFant(rsNfe.getString("nm_fantasia").trim());
            emit.setIE(rsNfe.getString("nr_inscricao_estadual") != null ? Util.limparFormatos(rsNfe.getString("nr_inscricao_estadual"), 'N') : "");
           	int crt = ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", -1);
            if(crt == -1)
            	return new Result(-1, "Tipo de Regime não configurado!");
           	emit.setCRT(String.valueOf(crt)); // 1 é Simples Nacional, 2 é Simples Nacional é excesso de sublimite de receita bruta, 3 é Regime Normal. (v2.0).        
            //
            TEnderEmi enderEmit = new TEnderEmi();   
            if(rsNfe.getString("nm_logradouro").length() < 2){
            	return new Result(-1, "Descrição do logradouro do emitente muito pequena");
            }
        	enderEmit.setXLgr(rsNfe.getString("nm_logradouro").trim());  
        	enderEmit.setNro(rsNfe.getString("nr_endereco").trim());  
        	enderEmit.setXBairro(rsNfe.getString("nm_bairro").trim()); 
        	enderEmit.setCMun(rsNfe.getString("id_ibge").trim());   
            enderEmit.setXMun(rsNfe.getString("nm_cidade"));
            enderEmit.setUF(TUfEmi.valueOf(rsNfe.getString("sg_estado")));  
            enderEmit.setCEP(rsNfe.getString("nr_cep"));  
            enderEmit.setCPais("1058");    // Fixo 
            enderEmit.setXPais("Brasil");  // Fixo
            
            String telefone_emitente_1 =rsNfe.getString("nr_telefone1"); 
            String telefone_emitente_2 =rsNfe.getString("nr_telefone2"); 
            String fax_emitente =rsNfe.getString("nr_fax"); 
            String celular_emitente =rsNfe.getString("nr_celular");
            
            String contato_telefone_emitente_1 =rsNfe.getString("contato_nr_telefone1"); 
            String contato_telefone_emitente_2 =rsNfe.getString("contato_nr_telefone2"); 
            String contato_celular_emitente =rsNfe.getString("contato_nr_celular");
            String contato_celular_emitente_2 =rsNfe.getString("contato_nr_celular2");
            String contato_celular_emitente_3 =rsNfe.getString("contato_nr_celular3");
            String contato_celular_emitente_4 =rsNfe.getString("contato_nr_celular4");
            
            if(!isNullOrBlank(telefone_emitente_1)){
            	enderEmit.setFone(Util.limparFormatos(telefone_emitente_1, 'N').trim());  
            } else if(!isNullOrBlank(telefone_emitente_2)){
            	enderEmit.setFone(Util.limparFormatos(telefone_emitente_2, 'N').trim());  
            } else if(!isNullOrBlank(fax_emitente)){
            	enderEmit.setFone(Util.limparFormatos(fax_emitente, 'N').trim());  
            } else if(!isNullOrBlank(celular_emitente)){
            	enderEmit.setFone(Util.limparFormatos(celular_emitente, 'N').trim());
			} else if(!isNullOrBlank(contato_telefone_emitente_1)){
				enderEmit.setFone(Util.limparFormatos(contato_telefone_emitente_1, 'N').trim());  
			} else if(!isNullOrBlank(contato_telefone_emitente_2)){
				enderEmit.setFone(Util.limparFormatos(contato_telefone_emitente_2, 'N').trim());  
			} else if(!isNullOrBlank(contato_celular_emitente)){
				enderEmit.setFone(Util.limparFormatos(contato_celular_emitente, 'N').trim());  
			} else if(!isNullOrBlank(contato_celular_emitente_2)){
				enderEmit.setFone(Util.limparFormatos(contato_celular_emitente_2, 'N').trim());  
			} else if(!isNullOrBlank(contato_celular_emitente_3)){
				enderEmit.setFone(Util.limparFormatos(contato_celular_emitente_3, 'N').trim());  
			} else if(!isNullOrBlank(contato_celular_emitente_4)){
				enderEmit.setFone(Util.limparFormatos(contato_celular_emitente_4, 'N').trim());  
			}  
            emit.setEnderEmit(enderEmit);  
            infNFe.setEmit(emit);          // <- Adicionando os dados do emissor é nota
            /*
             *  Dados do destinatário
             */
            ResultSet rsDest = connect.prepareStatement("SELECT gn_pessoa, nr_cnpj, nr_cpf, nm_pessoa, nr_inscricao_estadual, A.nm_email, " +
            		                                    "       nm_logradouro, nm_bairro, nm_complemento, nr_endereco, E.nr_cep, E.cd_cidade, A.nr_telefone1, A.nr_telefone2, A.nr_fax, A.nr_celular, " +
            		                                    "       nm_cidade, id_ibge, sg_estado, nm_razao_social, id_pessoa, " +
            		                                    "      H.nr_telefone1 as contato_nr_telefone1 , H.nr_telefone2 AS contato_nr_telefone2, " +
								                        "      H.nr_celular AS contato_nr_celular, H.nr_celular2 AS contato_nr_celular2, H.nr_celular3 AS contato_nr_celular3, " +
														" 	   H.nr_celular4 AS contato_nr_celular4 " +														
            		                                    "FROM grl_pessoa A " +
            		                                    "LEFT OUTER JOIN grl_pessoa_fisica    B ON (A.cd_pessoa = B.cd_pessoa) " +
            		                                    "LEFT OUTER JOIN grl_pessoa_juridica  C ON (A.cd_pessoa = C.cd_pessoa) " +
            		                                    "LEFT OUTER JOIN grl_pessoa_endereco  E ON (A.cd_pessoa = E.cd_pessoa  "+
														"                                       AND E.cd_endereco          = "+cdEnderecoDestinario+")"+
														"LEFT OUTER JOIN grl_cidade           F ON (E.cd_cidade            = F.cd_cidade) "+
														"LEFT OUTER JOIN grl_estado           G ON (F.cd_estado            = G.cd_estado) "+
														"LEFT OUTER JOIN grl_pessoa_contato H ON (A.cd_pessoa 	 = H.cd_pessoa )" +
            		                                    "WHERE A.cd_pessoa = "+rsNfe.getInt("cd_destinatario")).executeQuery();
            if(!rsDest.next())
            	return new Result(-1, "Destinatário não localizado!");
            
            
            //Verifica se é uma nota fiscal de importação: Caso o pais do destinatario seja diferente do da empresa, e seja uma nota de entrada
            Pessoa empresa = PessoaDAO.get(rsNfe.getInt("cd_empresa"), connect);
			Pessoa fornecedor = PessoaDAO.get(rsNfe.getInt("cd_destinatario"), connect);
			
			//Busca do pais e estado do emitente
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_pessoa", "" + empresa.getCdPessoa(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmPessoaEnd = PessoaEnderecoDAO.find(criterios, connect);
			int cdEstadoEmi = 0; 
			int cdPaisEmi   = 0;
			if(rsmPessoaEnd.next()){
				int cdCidade = rsmPessoaEnd.getInt("cd_cidade");
				Cidade cidade = CidadeDAO.get(cdCidade, connect);
				if(cidade == null){
					Conexao.rollback(connect);
					return new Result(-1, "Emitente sem cidade declarada!");
				}
				Estado estado = EstadoDAO.get(cidade.getCdEstado(), connect);
				if(estado == null){
					Conexao.rollback(connect);
					return new Result(-1, "Emitente sem estado declarada!");
				}
				cdEstadoEmi = estado.getCdEstado();
			}
			Estado estado = EstadoDAO.get(cdEstadoEmi, connect);
			cdPaisEmi = estado.getCdPais();
			if(cdPaisEmi == 0){
				Conexao.rollback(connect);
				return new Result(-1, "Emitente sem pais declarado!");
			}
			
			//Busca do pais do fornecedor
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_pessoa", "" + fornecedor.getCdPessoa(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
			rsmPessoaEnd = PessoaEnderecoDAO.find(criterios, connect);
			int cdEstadoForn = 0; 
			int cdPaisForn   = 0;
			if(rsmPessoaEnd.next()){
				int cdCidade = rsmPessoaEnd.getInt("cd_cidade");
				Cidade cidade = CidadeDAO.get(cdCidade, connect);
				if(cidade == null){
					Conexao.rollback(connect);
					return new Result(-1, "Fornecedor sem cidade declarada!");
				}
				estado = EstadoDAO.get(cidade.getCdEstado(), connect);
				if(estado == null){
					Conexao.rollback(connect);
					return new Result(-1, "Fornecedor sem estado declarado!");
				}
				cdEstadoForn = estado.getCdEstado();
			}
			estado = EstadoDAO.get(cdEstadoForn, connect);
			cdPaisForn = estado.getCdPais();
			if(cdPaisForn == 0){
				Conexao.rollback(connect);
				return new Result(-1, "Fornecedor sem pais declarado!");
			}
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_nota_fiscal", "" + rsNfe.getInt("cd_nota_fiscal"), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmDocVinc = NotaFiscalDocVinculadoDAO.find(criterios);
			boolean isDocEnt = false;
			if(rsmDocVinc.next()){
				isDocEnt = rsmDocVinc.getInt("cd_documento_entrada") != 0;
			}
			
			//Nota Fiscal De Importação
			if(cdPaisEmi != cdPaisForn && isDocEnt){
				 /*
	             *  Dados do destinatário
	             */
	            // PRÉ-VALIDAÇÃO
	            	// CPF / CNPJ
	            String telefone_destinatario_1 =rsDest.getString("nr_telefone1"); 
	            String telefone_destinatario_2 =rsDest.getString("nr_telefone2"); 
	            String fax_destinatario =rsDest.getString("nr_fax"); 
	            String celular_destinatario =rsDest.getString("nr_celular"); 
	            
	            String contato_telefone_destinatario_1 =rsDest.getString("contato_nr_telefone1"); 
	            String contato_telefone_destinatario_2 =rsDest.getString("contato_nr_telefone2"); 
	            String contato_celular_destinatario =rsDest.getString("contato_nr_celular");
	            String contato_celular_destinatario_2 =rsDest.getString("contato_nr_celular2");
	            String contato_celular_destinatario_3 =rsDest.getString("contato_nr_celular3");
	            String contato_celular_destinatario_4 =rsDest.getString("contato_nr_celular4");
	            
		        	//Logradouro
		        if(rsDest.getString("nm_logradouro") == null || rsDest.getString("nm_logradouro").trim().equals(""))//28-05-2013
		        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Logradouro ";
		        if(!dsValidacao.equals(""))
		        	return new Result(-1, "Dados do fornecedor faltando: "+dsValidacao);
	        
		        //
		        Dest dest = new Dest();
	            dest.setXNome(getTipoAmbiente(cdEmpresa) == PRODUCAO ? ((rsDest.getString("nm_razao_social") != null && !rsDest.getString("nm_razao_social").equals("")) ? rsDest.getString("nm_razao_social").trim() : rsDest.getString("nm_pessoa")) : "NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
//	            if(rsDest.getInt("gn_pessoa") == PessoaServices.TP_FISICA)
//	            	dest.setCPF("");//Deve ser informado vazio
//	            else
//	            	dest.setCNPJ("");//Deve ser informado vazio
	            dest.setIdEstrangeiro(rsDest.getString("id_pessoa"));
	            dest.setIndIEDest("9");
//	           	dest.setIE("");//Deve ser informado vazio
	           	if(rsDest.getString("nm_email") == null || rsDest.getString("nm_email").trim().equals("") || rsDest.getString("nm_email").trim().equals("null"))
	           		dest.setEmail(rsNfe.getString("nm_email").trim());
	           	else
	           		dest.setEmail(rsDest.getString("nm_email").trim());
	            /*
	             * ENDEREÇO DO CLIENTE
	             */
	            TEndereco enderDest = new TEndereco(); 
	            if(rsDest.getString("nm_logradouro").length() < 2){
	            	return new Result(-1, "Descrição do logradouro do destinatário muito pequena");
	            }
	        	enderDest.setXLgr(rsDest.getString("nm_logradouro").trim());
	        	if(rsDest.getString("nr_endereco") != null && !rsDest.getString("nr_endereco").equals(""))
	        		enderDest.setNro(rsDest.getString("nr_endereco").trim());  
	        	else
	        		enderDest.setNro("S/N");
	        	if(rsDest.getString("nm_bairro") != null && !rsDest.getString("nm_bairro").equals(""))
	        		enderDest.setXBairro(rsDest.getString("nm_bairro").trim());
	        	if(rsDest.getString("nm_complemento") != null && !rsDest.getString("nm_complemento").equals(""))
	        		enderDest.setXCpl(rsDest.getString("nm_complemento").trim());
	        	enderDest.setXMun("EXTERIOR");
	            enderDest.setCMun("9999999");        
	        	enderDest.setUF(TUf.valueOf("EX")); 
//	            enderDest.setCEP(rsDest.getString("nr_cep") == null ? "" : rsDest.getString("nr_cep").trim());
	            Pais pais = PaisDAO.get(cdPaisForn);
	            if(pais.getIdPais() == null || pais.getIdPais().equals(""))
	            	return new Result(-1, "o Id do pais não está cadastrado!");
	            enderDest.setCPais(pais.getIdPais());    // Fixo  
	            enderDest.setXPais(pais.getNmPais());  // Fixo
	            if(!isNullOrBlank(telefone_destinatario_1)){
	            	enderDest.setFone(Util.limparFormatos(telefone_destinatario_1, 'N').trim());  
	            } else if(!isNullOrBlank(telefone_destinatario_2)){
	            	enderDest.setFone(Util.limparFormatos(telefone_destinatario_2, 'N').trim());  
	            } else if(!isNullOrBlank(fax_destinatario)){
	            	enderDest.setFone(Util.limparFormatos(fax_destinatario, 'N').trim());  
	            } else if(!isNullOrBlank(celular_destinatario)){
	            	enderDest.setFone(Util.limparFormatos(celular_destinatario, 'N').trim());  
	            } else if(!isNullOrBlank(contato_telefone_destinatario_1)){
	            	enderDest.setFone(Util.limparFormatos(contato_telefone_destinatario_1, 'N').trim());  
				} else if(!isNullOrBlank(contato_telefone_destinatario_2)){
					enderDest.setFone(Util.limparFormatos(contato_telefone_destinatario_2, 'N').trim());  
				} else if(!isNullOrBlank(contato_celular_destinatario)){
					enderDest.setFone(Util.limparFormatos(contato_celular_destinatario, 'N').trim());  
				} else if(!isNullOrBlank(contato_celular_destinatario_2)){
					enderDest.setFone(Util.limparFormatos(contato_celular_destinatario_2, 'N').trim());  
				} else if(!isNullOrBlank(contato_celular_destinatario_3)){
					enderDest.setFone(Util.limparFormatos(contato_celular_destinatario_3, 'N').trim());  
				} else if(!isNullOrBlank(contato_celular_destinatario_4)){
					enderDest.setFone(Util.limparFormatos(contato_celular_destinatario_4, 'N').trim());  
				}  
    
	            dest.setEnderDest(enderDest);  
	            infNFe.setDest(dest);  
			}
			else{
				 /*
	             *  Dados do destinatário
	             */
	            // PRÉ-VALIDAÇÃO
	            	// CPF / CNPJ
	            if((rsDest.getString("nr_cpf") == null && rsDest.getString("nr_cnpj")==null) || (rsDest.getString("nr_cpf") != null && rsDest.getString("nr_cpf").trim().equals("") && rsDest.getString("nr_cnpj") != null && rsDest.getString("nr_cnpj").trim().equals(""))) 
	            	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CPF/CNPJ do cliente";  
	            	//Nome
	            if(rsDest.getString("nm_pessoa") == null || rsDest.getString("nm_pessoa").trim().equals(""))
		        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Nome do cliente";
	            	//Logradouro
	            if(rsDest.getString("nm_logradouro") == null || rsDest.getString("nm_logradouro").trim().equals(""))
		        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Logradouro do cliente";
		        	// Número
		    	if(rsDest.getString("nr_endereco") == null || rsDest.getString("nr_endereco").trim().equals(""))
		        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" No do imÃ³vel do cliente";
		    		// Bairro
		        if(rsDest.getString("nm_bairro") == null || rsDest.getString("nm_bairro").trim().equals(""))
		        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Bairro do cliente"; 
		        	// Cidade
		        if(rsDest.getString("nm_cidade") == null || rsDest.getString("nm_cidade").trim().equals(""))
		        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Cidade do cliente"; 
		    		// Estado
		        if(rsDest.getString("sg_estado") == null || rsDest.getString("sg_estado").trim().equals(""))
		        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Estado do cliente"; 
					// Código IBGE
		        if(rsDest.getString("id_ibge") == null || rsDest.getString("id_ibge").trim().equals(""))
		        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Cod. IBGE Cidade do cliente"; 
					// CEP
		        if(rsDest.getString("nr_cep") == null || rsDest.getString("nr_cep").trim().equals(""))
		        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CEP do cliente";
		        	// Telefone
		        String telefone_destinatario_1 =rsDest.getString("nr_telefone1"); 
	            String telefone_destinatario_2 =rsDest.getString("nr_telefone2"); 
	            String fax_destinatario =rsDest.getString("nr_fax"); 
	            String celular_destinatario =rsDest.getString("nr_celular");
	            
	            String contato_telefone_destinatario_1 =rsDest.getString("contato_nr_telefone1"); 
	            String contato_telefone_destinatario_2 =rsDest.getString("contato_nr_telefone2"); 
	            String contato_celular_destinatario =rsDest.getString("contato_nr_celular");
	            String contato_celular_destinatario_2 =rsDest.getString("contato_nr_celular2");
	            String contato_celular_destinatario_3 =rsDest.getString("contato_nr_celular3");
	            String contato_celular_destinatario_4 =rsDest.getString("contato_nr_celular4");
	            
	            if( isNullOrBlank(telefone_destinatario_1) 
	            		&& isNullOrBlank(telefone_destinatario_2)
	            		&& isNullOrBlank(fax_destinatario) 
	            		&& isNullOrBlank(celular_destinatario)
	            		&& isNullOrBlank(contato_telefone_destinatario_1)
	            		&& isNullOrBlank(contato_telefone_destinatario_2)
	            		&& isNullOrBlank(contato_celular_destinatario)
	            		&& isNullOrBlank(contato_celular_destinatario_2)
	            		&& isNullOrBlank(contato_celular_destinatario_3)
	            		&& isNullOrBlank(contato_celular_destinatario_4)) {
		        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Telefone do cliente";
	            }
		        //
		        if(!dsValidacao.equals(""))
		        	return new Result(-1, "Dados do cliente faltando: "+dsValidacao);
	        
		        //Pre-Validacao 2
		    	// CNPJ do Emissor
		        if(rsDest.getString("nr_cpf") != null && !Util.isCpfValido(rsDest.getString("nr_cpf")))
		        	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CPF";
		        if(rsDest.getString("nr_cnpj") != null && !Util.isCNPJ(rsDest.getString("nr_cnpj")))
		        	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CNPJ";
		        if(!NotaFiscalServices.validaSiglaEstado(rsDest.getString("sg_estado")))
		        	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Sigla do estado";
		        
		        if( (Util.limparFormatos(telefone_destinatario_1, 'N').trim().length() < 8 || Util.limparFormatos(telefone_destinatario_1, 'N').trim().length() > 11)
		        		&& (Util.limparFormatos(telefone_destinatario_2, 'N').trim().length() < 8 || Util.limparFormatos(telefone_destinatario_2, 'N').trim().length() > 11)
		        		&& (Util.limparFormatos(fax_destinatario, 'N').trim().length() < 8 || Util.limparFormatos(fax_destinatario, 'N').trim().length() > 11)
		        		&& (Util.limparFormatos(celular_destinatario, 'N').trim().length() < 8 || Util.limparFormatos(celular_destinatario, 'N').trim().length() > 11)
		        		&& (Util.limparFormatos(contato_telefone_destinatario_1, 'N').trim().length() < 8 || Util.limparFormatos(contato_telefone_destinatario_1, 'N').trim().length() > 11)
		        		&& (Util.limparFormatos(contato_telefone_destinatario_2, 'N').trim().length() < 8 || Util.limparFormatos(contato_telefone_destinatario_2, 'N').trim().length() > 11)
		        		&& (Util.limparFormatos(contato_celular_destinatario, 'N').trim().length() < 8 || Util.limparFormatos(contato_celular_destinatario, 'N').trim().length() > 11)
		        		&& (Util.limparFormatos(contato_celular_destinatario_2, 'N').trim().length() < 8 || Util.limparFormatos(contato_celular_destinatario_2, 'N').trim().length() > 11)
		        		&& (Util.limparFormatos(contato_celular_destinatario_3, 'N').trim().length() < 8 || Util.limparFormatos(contato_celular_destinatario_3, 'N').trim().length() > 11)
		        		&& (Util.limparFormatos(contato_celular_destinatario_4, 'N').trim().length() < 8 || Util.limparFormatos(contato_celular_destinatario_4, 'N').trim().length() > 11)
		        		) {
		        	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Telefone do destinatário";
		        }
		        if(!dsValidacao2.equals(""))
	            	return new Result(-1, "Dados do cliente inválidos: "+dsValidacao2);
		    
		        
		        
		        //
		        Dest dest = new Dest();
	            dest.setXNome(getTipoAmbiente(cdEmpresa) == PRODUCAO ? ((rsDest.getString("nm_razao_social") != null && !rsDest.getString("nm_razao_social").equals("")) ? rsDest.getString("nm_razao_social").trim() : rsDest.getString("nm_pessoa")) : "NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
	        	dest.setCPF(rsDest.getString("nr_cpf"));
	            dest.setCNPJ(rsDest.getString("nr_cnpj"));
	            dest.setIndIEDest(rsDest.getString("nr_inscricao_estadual") != null && !rsDest.getString("nr_inscricao_estadual").trim().equals("") ? "1": "9");
	            if(rsDest.getString("nr_inscricao_estadual") != null && !rsDest.getString("nr_inscricao_estadual").trim().equals(""))
	            	dest.setIE(Util.limparFormatos(rsDest.getString("nr_inscricao_estadual"), 'N').trim());
	           	if(rsDest.getString("nm_email") == null || rsDest.getString("nm_email").trim().equals("") || rsDest.getString("nm_email").trim().equals("null"))
	           		dest.setEmail(rsNfe.getString("nm_email").trim());
	           	else
	           		dest.setEmail(rsDest.getString("nm_email").trim());
	            /*
	             * ENDEREÇO DO CLIENTE
	             */
	            TEndereco enderDest = new TEndereco(); 
	            if(rsDest.getString("nm_logradouro").length() < 2){
	            	return new Result(-1, "Descrição do logradouro do destinatário muito pequena");
	            }
	        	enderDest.setXLgr(rsDest.getString("nm_logradouro").trim());  
	        	enderDest.setNro(rsDest.getString("nr_endereco").trim());  
	        	enderDest.setXBairro(rsDest.getString("nm_bairro").trim());
	        	enderDest.setXMun(rsDest.getString("nm_cidade").trim());
	            enderDest.setCMun(rsDest.getString("id_ibge").trim());//  - ID do municipio do destinatario - idMunicipio      
	        	enderDest.setUF(TUf.valueOf(rsDest.getString("sg_estado").trim())); //
	            enderDest.setCEP(rsDest.getString("nr_cep").trim());  
	            enderDest.setCPais("1058");    // Fixo  
	            enderDest.setXPais("Brasil");  // Fixo
	            if(!isNullOrBlank(telefone_destinatario_1)){
	            	enderDest.setFone(Util.limparFormatos(telefone_destinatario_1, 'N').trim());  
	            } else if(!isNullOrBlank(telefone_destinatario_2)){
	            	enderDest.setFone(Util.limparFormatos(telefone_destinatario_2, 'N').trim());  
	            } else if(!isNullOrBlank(fax_destinatario)){
	            	enderDest.setFone(Util.limparFormatos(fax_destinatario, 'N').trim());  
	            } else if(!isNullOrBlank(celular_destinatario)){
	            	enderDest.setFone(Util.limparFormatos(celular_destinatario, 'N').trim());  
	            } 
	            dest.setEnderDest(enderDest);  
	            infNFe.setDest(dest);  
			}
            
			AutXML autXml = new AutXML();
			autXml.setCNPJ("13937073000156");
			infNFe.getAutXML().add(autXml);
			
           
            /*
             *  Dados dos produtos
             */
			PreparedStatement pstmtNotaItem = connect.prepareStatement("SELECT A.*, B.tp_operacao, " +
																		" 		  B.tp_base_calculo, B.pr_reducao_base, B.tp_motivo_desoneracao, " +
																		"		  B.tp_base_calculo_substituicao, " +
																		"		  B.pr_reducao_base_substituicao, C.cd_situacao_tributaria AS cd_situacao_tributaria_original, C.nr_situacao_tributaria, C.lg_simples, " +
																		"		  C.lg_substituicao, C.lg_retido, D.id_tributo FROM  fsc_nota_fiscal_item_tributo A " +
																		"	JOIN adm_tributo_aliquota B ON (A.cd_tributo = B.cd_tributo  " +
																		"					AND A.cd_tributo_aliquota = B.cd_tributo_aliquota) " +
																		"	LEFT OUTER JOIN fsc_situacao_tributaria C ON (A.cd_situacao_tributaria = C.cd_situacao_tributaria " +
																		"						AND A.cd_tributo = C.cd_tributo) " +
																		"	JOIN adm_tributo D ON (A.cd_tributo = D.cd_tributo)" +
																		"	WHERE A.cd_nota_fiscal = ? " +
																		"	  AND A.cd_item = ?");
			
             ResultSet rsProdutos = connect.prepareStatement("SELECT A.*, D.nr_ncm, D.cd_ncm, E.sg_unidade_medida, E.nr_precisao_medida, C.id_reduzido, C.tp_origem, B.nm_produto_servico, B.txt_especificacao, " +
             		                                        "        B.cd_categoria_economica, B.cd_classificacao_fiscal, B.id_produto_servico, B.cd_fabricante, " +
             		                                        "        E.sg_unidade_medida, J.nr_codigo_fiscal AS nr_cfop, BG.cd_grupo, BE.qt_precisao_custo " +
										                    "FROM fsc_nota_fiscal_item             A " +
										                    "JOIN grl_produto_servico              B ON (A.cd_produto_servico = B.cd_produto_servico) " +
										                    "JOIN grl_produto_servico_empresa      BE ON (A.cd_produto_servico = BE.cd_produto_servico" +
										                    "												AND BE.cd_empresa = "+cdEmpresa+")  " +
										                    "LEFT OUTER JOIN alm_produto_grupo               BG ON (A.cd_produto_servico = BG.cd_produto_servico" +
										                    "												AND lg_principal = 1" +
										                    "												AND BG.cd_empresa = "+cdEmpresa+") " +
										                    "JOIN grl_produto_servico_empresa      C ON (A.cd_produto_servico = C.cd_produto_servico and C.cd_empresa = "+cdEmpresa+") " +
										                    "LEFT OUTER JOIN grl_ncm               D ON (D.cd_ncm = B.cd_ncm) "+
															"LEFT OUTER JOIN grl_unidade_medida    E ON (C.cd_unidade_medida = E.cd_unidade_medida) "+
															"LEFT OUTER JOIN adm_natureza_operacao            J ON (J.cd_natureza_operacao = A.cd_natureza_operacao) " +
															"WHERE A.cd_nota_fiscal = "+rsNfe.getInt("cd_nota_fiscal")).executeQuery();
            if(!rsProdutos.next())	
            	return new Result(-1, "Produtos não localizados");
            
            int nrItem = 1;
            HashMap<String, Integer> produtoAdicao = new HashMap<String, Integer>();
            //Por produto
            do {
            	produtoAdicao.put(rsProdutos.getString("nr_ncm"), (produtoAdicao.get(rsProdutos.getString("nr_ncm")) != null ? produtoAdicao.get(rsProdutos.getString("nr_ncm")) : 0) + 1);
            	vlDescontos += rsProdutos.getFloat("vl_desconto");
            	
            	vlAcrescimo += rsProdutos.getFloat("vl_acrescimo");
            	
            	// PRÉ-VALIDAÇÃO
            	if(rsProdutos.getString("nr_ncm") == null || rsProdutos.getString("nr_ncm").trim().equals(""))
    	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" NCM ";
            	if(rsProdutos.getString("nr_cfop") == null || rsProdutos.getString("nr_cfop").trim().equals(""))
    	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CFOP ";
            	if(rsProdutos.getString("sg_unidade_medida") == null || rsProdutos.getString("sg_unidade_medida").trim().equals(""))
    	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Unidade ";
            	if(!dsValidacao.equals(""))
                	return new Result(-1, "Dados do produto "+rsProdutos.getString("nm_produto_servico")+" faltando: "+dsValidacao);
            	
//                if(rsProdutos.getString("id_produto_servico") != null && !rsProdutos.getString("id_produto_servico").equals("") && !NotaFiscalServices.isCean(rsProdutos.getString("id_produto_servico").trim()))
//    	        	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" cÃ³digo de barras inválido";
//    	        if(!dsValidacao2.equals(""))
//                	return new Result(-1, "Dados do produto "+rsProdutos.getString("nm_produto_servico")+" inválidos: "+dsValidacao2);
                
            	//
                Det det = new Det();  
                det.setNItem(String.valueOf(nrItem));  
                det.setInfAdProd((rsProdutos.getString("txt_especificacao") != null && !rsProdutos.getString("txt_especificacao").trim().equals("")) ? rsProdutos.getString("txt_especificacao").trim() : "NENHUM DETALHE"); // Dados adicionais - Nao pode ser vazio
                String nrCodigoBarras = rsProdutos.getString("id_produto_servico");
                // Dados do produto  
                Prod prod = new Prod();
               
                DI di = new DI();
                criterios = new ArrayList<ItemComparator>();
    			criterios.add(new ItemComparator("cd_nota_fiscal", "" + rsNfe.getInt("cd_nota_fiscal"), ItemComparator.EQUAL, Types.INTEGER));
    			criterios.add(new ItemComparator("cd_item", "" + rsProdutos.getInt("cd_item"), ItemComparator.EQUAL, Types.INTEGER));
    			ResultSetMap rsmDeclaracao = DeclaracaoImportacaoDAO.find(criterios);
    			while(rsmDeclaracao.next()){
    				di = new DI();
    				DeclaracaoImportacao declaracao = DeclaracaoImportacaoDAO.get(rsmDeclaracao.getInt("cd_declaracao_importacao"));
    				estado = EstadoDAO.get(declaracao.getCdEstado());
    				if(estado == null)
    					return new Result(-1, "Faltando estado da declaração de importação!");
    				
    				if(declaracao.getCdExportador() == 0)
        	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Exportador ";
    				if(declaracao.getDtDesembaraco() == null)
        	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Data do Desembaração ";
    				if(declaracao.getDtRegistro() == null)
        	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Data do Registro ";
    				if(declaracao.getNrDeclaracaoImportacao() == null || declaracao.getNrDeclaracaoImportacao().equals(""))
        	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Número da Declaração de Importação ";
    				if(declaracao.getDsLocalDesembaraco() == null || declaracao.getDsLocalDesembaraco().equals(""))
        	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Local do Desembaração ";
    				if(!dsValidacao.equals(""))
                    	return new Result(-1, "Dados da declaração de importação faltando: "+dsValidacao);
    				
    				di.setTpViaTransp(String.valueOf(declaracao.getTpViaTransporte()));
    				di.setTpIntermedio(String.valueOf(declaracao.getTpIntermedio()));
    				if(declaracao.getTpIntermedio() != EntradaDeclaracaoImportacaoServices.TP_INTERMEDIO_PROPRIA){
    					di.setCNPJ(declaracao.getNrCnpjIntermediario());
    					di.setUFTerceiro(TUfEmi.valueOf(EstadoDAO.get(declaracao.getCdEstadoIntermediario(), connect).getSgEstado()));
    				}
    				
    				if(declaracao.getTpViaTransporte() == EntradaDeclaracaoImportacaoServices.TP_VIA_MARITIMA)
    					di.setVAFRMM(getVAfrmm(rsNfe.getInt("cd_nota_fiscal"), rsProdutos.getInt("cd_item")));
    				di.setCExportador(String.valueOf(declaracao.getCdExportador()));
    				di.setDDesemb(Util.formatDate(declaracao.getDtDesembaraco(), "yyyy-MM-dd"));
    				di.setDDI(Util.formatDate(declaracao.getDtRegistro(), "yyyy-MM-dd"));
    				di.setNDI(Util.limparFormatos(declaracao.getNrDeclaracaoImportacao().trim()));
    				di.setUFDesemb(TUfEmi.valueOf(estado.getSgEstado().trim()));
    				di.setXLocDesemb(declaracao.getDsLocalDesembaraco().trim());
    				
    				criterios = new ArrayList<ItemComparator>();
        			criterios.add(new ItemComparator("cd_declaracao_importacao", "" + declaracao.getCdDeclaracaoImportacao(), ItemComparator.EQUAL, Types.INTEGER));
        			ResultSetMap rsmAdicoes = AdicaoDAO.find(criterios, connect);
        			Adi adi = new Adi();
        			int qtAdicoes = 0;
        			while(rsmAdicoes.next()){
        				//Apenas seráo colocar no XML as adições que tem mesmo NCM que aquele produto
        				if(rsProdutos.getInt("cd_ncm") == rsmAdicoes.getInt("cd_ncm")){
	        				adi = new Adi();
	        				Adicao adicao = AdicaoDAO.get(rsmAdicoes.getInt("cd_adicao"));
	        				int cdFabricante = rsProdutos.getInt("cd_fabricante");
	        				if(cdFabricante == 0)
	        					cdFabricante = rsNfe.getInt("cd_empresa");
	        				
	        				if(adicao.getNrAdicao() == 0)
	            	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Número da Adição ";
	        				if(!dsValidacao.equals(""))
	                        	return new Result(-1, "Dados da adição de NCM "+rsProdutos.getString("nr_ncm")+" de importação faltando: "+dsValidacao);
	        				
	        				adi.setCFabricante(String.valueOf(cdFabricante));
	        				adi.setNAdicao(String.valueOf(adicao.getNrAdicao()));
	        				adi.setNSeqAdic(String.valueOf(produtoAdicao.get(rsProdutos.getString("nr_ncm"))));
//	        				String vlDesconto = Util.formatNumberSemSimbolos(adicao.getVlDesconto(), 2);
//	        				adi.setVDescDI(vlDesconto);
	        				di.getAdi().add(adi);
	        				qtAdicoes++;
        				}
        			}
        			if(qtAdicoes > 999)
        				return new Result(-1, "Número de Adições ultrapassa 999!");
        			prod.getDI().add(di);
        			
    				
    			}
    			String cProd = (rsProdutos.getString("id_reduzido") != null && !rsProdutos.getString("id_reduzido").trim().equals("") ? rsProdutos.getString("id_reduzido") : rsProdutos.getString("cd_produto_servico"));
    			prod.setCProd(cProd); 
            	prod.setNCM(rsProdutos.getString("nr_ncm")!=null ? rsProdutos.getString("nr_ncm") : "");
            	prod.setCFOP(rsProdutos.getString("nr_cfop"));
            	if(rsProdutos.getString("id_produto_servico") != null && rsProdutos.getString("id_produto_servico").trim().length() == 13 && NotaFiscalServices.isCean(rsProdutos.getString("id_produto_servico").trim()))
            		prod.setCEAN(nrCodigoBarras);
            	else
            		prod.setCEAN("");
            	prod.setXProd(rsProdutos.getString("nm_produto_servico").trim() + ((rsProdutos.getString("txt_informacao_adicional") != null && !rsProdutos.getString("txt_informacao_adicional").equals("null") && !rsProdutos.getString("txt_informacao_adicional").trim().equals("")) ? " - " + rsProdutos.getString("txt_informacao_adicional") : "" ));
            	String sgUnidadeMedida = rsProdutos.getString("sg_unidade_medida");
            	prod.setUCom(sgUnidadeMedida);
                prod.setUTrib(sgUnidadeMedida!= null ? sgUnidadeMedida.trim() : "");
                // Valor
                prod.setVUnCom(Util.formatNumberSemSimbolos(rsProdutos.getFloat("vl_unitario"), (rsProdutos.getInt("qt_precisao_custo") > 10 ? 10 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo")))));
                prod.setVUnTrib(Util.formatNumberSemSimbolos(rsProdutos.getFloat("vl_unitario"), (rsProdutos.getInt("qt_precisao_custo") > 10 ? 10 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo")))));  
                // Quantidade
                prod.setQCom(Util.formatNumberSemSimbolos(rsProdutos.getFloat("qt_tributario"), (rsProdutos.getInt("qt_precisao_custo") > 4 ? 4 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo"))))); 
                prod.setQTrib(Util.formatNumberSemSimbolos(rsProdutos.getFloat("qt_tributario"), (rsProdutos.getInt("qt_precisao_custo") > 4 ? 4 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo")))));  
               	// Total
                vlTotalProdutos += Double.parseDouble(Util.formatNumberSemSimbolos(
                		(Util.arredondar(rsProdutos.getFloat("vl_unitario"), (rsProdutos.getInt("qt_precisao_custo") > 10 ? 10 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo")))) 
                		* 
                		Util.arredondar(rsProdutos.getFloat("qt_tributario"), (rsProdutos.getInt("nr_precisao_medida") > 4 ? 4 : (rsProdutos.getInt("nr_precisao_medida") <= 0 ? 0 : rsProdutos.getInt("nr_precisao_medida"))))), 
                		(rsProdutos.getInt("qt_precisao_custo") > 2 ? 2 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo")))));
//                vlTotalProdutos = vlTotalProdutos + Float.parseFloat(Util.formatNumberSemSimbolos(
//                		(Util.arredondar(rsProdutos.getFloat("vl_unitario"), (rsProdutos.getInt("qt_precisao_custo") > 10 ? 10 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo")))) 
//                		* 
//                		Util.arredondar(rsProdutos.getFloat("qt_tributario"), (rsProdutos.getInt("nr_precisao_medida") > 4 ? 4 : (rsProdutos.getInt("nr_precisao_medida") <= 0 ? 0 : rsProdutos.getInt("nr_precisao_medida"))))), 
//                		2));
                prod.setVProd(Util.formatNumberSemSimbolos(
                		(Util.arredondar(rsProdutos.getFloat("vl_unitario"), (rsProdutos.getInt("qt_precisao_custo") > 10 ? 10 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo")))) 
                		* 
                		Util.arredondar(rsProdutos.getFloat("qt_tributario"), (rsProdutos.getInt("nr_precisao_medida") > 4 ? 4 : (rsProdutos.getInt("nr_precisao_medida") <= 0 ? 0 : rsProdutos.getInt("nr_precisao_medida"))))), 
                		(rsProdutos.getInt("qt_precisao_custo") > 2 ? 2 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo")))));
                
            	if(rsProdutos.getString("id_produto_servico") != null && rsProdutos.getString("id_produto_servico").trim().length() == 13 && NotaFiscalServices.isCean(rsProdutos.getString("id_produto_servico").trim()))
            		prod.setCEANTrib(nrCodigoBarras);
            	else
            		prod.setCEANTrib("");
                prod.setIndTot("1");  // 1 - Produto
                det.setProd(prod);
                
                pstmtNotaItem.setInt(1, rsProdutos.getInt("cd_nota_fiscal"));
                pstmtNotaItem.setInt(2, rsProdutos.getInt("cd_item"));
                
                
                ResultSetMap rsmTributos = new ResultSetMap(pstmtNotaItem.executeQuery());
                //Usado para nao permitir o programa entrar na area do do-while caso nao haja parametros, mas a tributacao nao seja obrigatoria (colocando-se
                //os tributos padroes)
                boolean hasTributo 	= true;
                //Caso haja ISSQN nao podera haver ICMS, IPI e II, porem nao esta sendo usado pois nao ha empresa que utiliza o sistema e trabalhe com sevico
                boolean hasISSQN 	= false;
                //Usados para confirmar se no produto foi declarado estes 4 impostos que sao obrigatorios, caso nao forem, o sistema tratarão de colocar os padroes
                boolean hasPIS 		= false;
                boolean hasCOFINS 	= false;
                boolean hasIPI 		= false;
                boolean hasICMS 	= false;
                //Usado para declarar o PISST caso o o COFINSST tiver sido incluido, sincronizando os valores de ambos
                boolean hasCOFINSST = false;
                double vlBaseStCofins = 0;
                float prAliquotaStCofins = 0;
                //Usado para declarar o COFINSST caso o o PISST tiver sido incluido, sincronizando os valores de ambos
                boolean hasPISST 	= false;
                double vlBaseStPis = 0;
                float prAliquotaStPis = 0;
                @SuppressWarnings("unused")
                boolean hasICMSIPIII = false;
                Imposto imposto = new Imposto();
                
                
                rsmTributos.beforeFirst();
                rsmTributos = organizarTributos(rsmTributos);
                if(!rsmTributos.next()) {
                	//Parametro que permiti ou bloqueia um produto nao ter uma tributacao cadastrada
                	if(ParametroServices.getValorOfParametroAsInteger("LG_TRIBUTACAO", -1, cdEmpresa, connect) == 1){
                		hasTributo = false;
                	}
                	else{
                		return new Result(-1, "Parametro que impede produtos sem tributação está ativado. Produto " + rsProdutos.getString("nm_produto_servico") + " sem tributação configurada!");
                	}
                }
                if(hasTributo){
                    do{
                    	if(rsmTributos.getRegister() == null)
                    		continue;
                    	
                    	if(rsmTributos.getInt("cd_situacao_tributaria_original") == 0){
                    		return new Result(-1, "Item " + rsProdutos.getString("nm_produto_servico") + " com tributo sem situação tributária definida!");
                    	}
                		//Caso seja devolucao vai incluir o valor de ICMS e valor de base de calculo
                		if(rsmTributos.getString("id_tributo").equals("ICMS") && (rsNfe.getInt("cd_natureza_operacao") == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_ENTRADA_IMPORTACAO_DEFAULT", 0))){
                			//Caso seja tambem substituicao tributaria
                			if(rsmTributos.getInt("lg_substituicao") == 1){
                				
                				if(rsmTributos.getFloat("vl_retido") > 0 && rsmTributos.getInt("lg_retido") == 0){
                					vlBaseCalculoST 	+= Util.arredondar(rsmTributos.getFloat("vl_base_retencao"), 2);
	                				vlICMSST 			+= Util.arredondar(rsmTributos.getFloat("vl_retido"), 2);
                				}
    	                	}
                			else{
                				vlBaseCalculo 	+= Util.arredondar(rsmTributos.getFloat("vl_base_calculo"), 2);
		                		vlICMS 			+= Util.arredondar(rsmTributos.getFloat("vl_tributo"), 2);
                			}
                			
                		}
                		//Vai incluir o valor de icms e base de calculo caso nao seja simples
                		else if(rsmTributos.getString("id_tributo").equals("ICMS") && rsmTributos.getInt("lg_simples") != SituacaoTributariaServices.REG_SIMPLES && rsmTributos.getInt("lg_substituicao") == 0){
                			vlBaseCalculo 	+= Util.arredondar(rsmTributos.getFloat("vl_base_calculo"), 2);
	                		vlICMS 			+= Util.arredondar(rsmTributos.getFloat("vl_tributo"), 2);
	                	}
                		//Vai incluir o valor de icms e base de calculo st caso seja simples porém seja por substituicao tributaria
                		else if(rsmTributos.getString("id_tributo").equals("ICMS") && rsmTributos.getInt("lg_substituicao") == 1 && rsmTributos.getFloat("vl_retido") > 0 && rsmTributos.getInt("lg_retido") == 0){
                			vlBaseCalculoST 	+= Util.arredondar(rsmTributos.getFloat("vl_base_retencao"), 2);
            				vlICMSST 			+= Util.arredondar(rsmTributos.getFloat("vl_retido"), 2);
	                	}
                		//ICMS
	                	if(rsmTributos.getString("id_tributo").equals("ICMS") && !hasISSQN){
	                		hasICMS = true;
	                		ICMS icms = new ICMS();  
	                		hasICMSIPIII = true;
	                		//Tributado Integralmente
	                		if(rsmTributos.getString("nr_situacao_tributaria").equals("00")){
	                			ICMS00 icms00 = new ICMS00();
	                			icms00.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms00.setCST("00");
	                			icms00.setModBC(rsmTributos.getString("tp_base_calculo"));
	                			icms00.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			icms00.setPICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			icms00.setVICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			icms.setICMS00(icms00);
	                		}
	                		//Tributada e com cobrança do ICMS por substituição tributária
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("10")){
	                			
	                			vlBaseCalculo 	+= Util.arredondar(rsmTributos.getFloat("vl_base_calculo"), 2);
		                		vlICMS 			+= Util.arredondar(rsmTributos.getFloat("vl_tributo"), 2);
	                			
	                			ICMS10 icms10 = new ICMS10();
	                			icms10.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms10.setCST("10");
	                			icms10.setModBC(rsmTributos.getString("tp_base_calculo"));
	                			icms10.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			icms10.setPICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			icms10.setVICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			if(rsmTributos.getInt("lg_substituicao") == 1){
	                				icms10.setModBCST(rsmTributos.getString("tp_base_calculo_substituicao"));
	                				if(rsmTributos.getFloat("pr_aliquota_substituicao") != 0)
	                					icms10.setPMVAST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                				if(rsmTributos.getFloat("pr_reducao_base_substituicao") != 0)
	                					icms10.setPRedBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_reducao_base_substituicao"), 2));
	                				icms10.setVBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	                				icms10.setPICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                				icms10.setVICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                    		}
	                			icms.setICMS10(icms10);
	                		}
	                		//Com redução de base de cálculo
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("20")){
	                			ICMS20 icms20 = new ICMS20();
	                			icms20.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms20.setCST("20");
	                			icms20.setModBC(rsmTributos.getString("tp_base_calculo"));
	                			icms20.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			icms20.setPICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			icms20.setVICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			icms20.setPRedBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_reducao_base_substituicao"), 2));
	                			icms.setICMS20(icms20);
	                		}
	                		//Isenta ou não tributada e com cobrança do ICMS por substituição tributária
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("30")){
	                			ICMS30 icms30 = new ICMS30();
	                			icms30.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms30.setCST("30");
	                			icms30.setModBCST(rsmTributos.getString("tp_base_calculo_substituicao"));
	                			if(rsmTributos.getInt("lg_substituicao") == 1){
	                				if(rsmTributos.getFloat("pr_credito") != 0)
	                					icms30.setPMVAST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));// Percentual da margem de valor Adicionado do ICMS ST
	                				if(rsmTributos.getFloat("pr_reducao_base_substituicao") != 0)
	                					icms30.setPRedBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_reducao_base_substituicao"), 2));
		            				icms30.setVBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
		            				icms30.setPICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
		            				icms30.setVICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                			}
	                			icms.setICMS30(icms30);
	                		}
	                		//40 - Isenta 41 - não tributada 50 - Suspensão
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("40")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("41") 
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("50")){
	                			ICMS40 icms40 = new ICMS40();
	                			icms40.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms40.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			if(rsmTributos.getInt("tp_motivo_desoneracao") == TributoAliquotaServices.DES_UTIL_MOT 
	                					|| rsmTributos.getInt("tp_motivo_desoneracao") == TributoAliquotaServices.DES_SUFRAMA 
	                					|| rsmTributos.getInt("tp_motivo_desoneracao") == TributoAliquotaServices.DES_VEN_ORG_PUB)
	                				icms40.setVICMSDeson(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			icms40.setVICMSDeson(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			icms40.setMotDesICMS(rsmTributos.getString("tp_motivo_desoneracao"));
	                			icms.setICMS40(icms40);
	                			
	                		}
	                		//Diferimento A exigência do preenchimento das informações do ICMS diferido fica a critério de cada UF.
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("51")){
	                			ICMS51 icms51 = new ICMS51();
	                			icms51.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms51.setCST("51");
	                			icms51.setModBC(rsmTributos.getString("tp_base_calculo"));
	                			icms51.setPRedBC(rsmTributos.getString("pr_reducao_base"));
	                			icms51.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			icms51.setPICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			icms51.setVICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			icms.setICMS51(icms51);
	                		}
	                		//ICMS cobrado anteriormente por substituição tributária
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("60")){
	                			ICMS60 icms60 = new ICMS60();
	                			icms60.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms60.setCST("60");
	                			if(rsmTributos.getInt("lg_substituicao") == 1  && rsmTributos.getFloat("vl_retido") > 0){
	                				icms60.setVBCSTRet(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
		                			icms60.setVICMSSTRet(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                			} 
	                			else{
	                				icms60.setVBCSTRet("0.00");
	                				icms60.setVICMSSTRet("0.00");
	                			}	                			
	                			icms.setICMS60(icms60);
	                		}
	                		//Com redução de base de cálculo e cobrança do ICMS por substituição tributária
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("70")){
	                			ICMS70 icms70 = new ICMS70();
	                			icms70.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms70.setCST("70");
	                			icms70.setModBC(rsmTributos.getString("tp_base_calculo"));
	                			icms70.setPRedBC(rsmTributos.getString("pr_reducao_base"));
	                			icms70.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			icms70.setPICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			icms70.setVICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			if(rsmTributos.getInt("lg_substituicao") == 1){
	                				icms70.setModBCST(rsmTributos.getString("tp_base_calculo_substituicao"));
	                				if(rsmTributos.getFloat("pr_aliquota_substituicao") != 0)
	                					icms70.setPMVAST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                				if(rsmTributos.getFloat("pr_reducao_base_substituicao") != 0)
	                					icms70.setPRedBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_reducao_base_substituicao"), 2));
	                				icms70.setVBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	                				icms70.setPICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                				icms70.setVICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                    		}
	                			icms.setICMS70(icms70);
	                		}
	                		//Outros
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("90")){
	                			ICMS90 icms90 = new ICMS90();
	                			icms90.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms90.setCST("90");
	                			icms90.setModBC(rsmTributos.getString("tp_base_calculo"));
//	                			icms90.setPRedBC(rsmTributos.getString("pr_reducao_base"));
	                			icms90.setPICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			icms90.setVICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			icms90.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			if(rsmTributos.getInt("lg_substituicao") == 1){
	                				icms90.setModBCST(rsmTributos.getString("tp_base_calculo_substituicao"));
	                				if(rsmTributos.getFloat("pr_aliquota_substituicao") != 0)
	                					icms90.setPMVAST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                				if(rsmTributos.getFloat("pr_reducao_base_substituicao") != 0)
	                					icms90.setPRedBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_reducao_base_substituicao"), 2));
	                				icms90.setVBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	                				icms90.setPICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                				icms90.setVICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                    		}
	                			icms.setICMS90(icms90);
	                		}
	                		
//	                		VER COM LÉO QUANDO VAI ENTRAR NESSAS DUAS
//	                		//ICMSPart
//	                		else if(rsmTributos.getString("id_tributo").equals("ICMS") && rsmTributos.getInt("lg_substituicao") == 1 && rsmTributos.getInt("lg_simples") != SituacaoTributariaServices.REG_SIMPLES){
//	                			ICMSST icmsst = new ICMSST();
//	                			vlBaseCalculoST += novaBaseCalculo;
//	                			vlICMSST		+= (rsmTributos.getFloat("pr_aliquota") / 100 * novaBaseCalculo);
//	                			icmsst.setOrig("0");
//	                			icmsst.setCST(rsmTributos.getString("nr_situacao_tributaria"));
//	                			icmsst.setVBCSTRet(Util.formatNumberSemSimbolos(novaBaseCalculo, 2));//Perguntar se o retido vai ser o proprio valor de base de calculo
//	                			icmsst.setVICMSSTRet(Util.formatNumberSemSimbolos((rsmTributos.getFloat("pr_reducao_base_substituicao") / 100 * novaBaseCalculo), 2));//Perguntar se o valor do icms retido eh o mesmo do icms
//	//                			icmsst.setVBCSTDest(value);Perguntar LÉO como eh que se acha o do destino
//	//                			icmsst.setVBCSTDest(value);Perguntar LÉO como eh que se acha o do destino
//	                			icms.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoICMSST(icmsst));; 
//	                		}
//	                		//ICMSST
//	                		else if(rsmTributos.getString("id_tributo").equals("ICMS") && rsmTributos.getInt("lg_substituicao") == 1 && rsmTributos.getInt("lg_simples") != SituacaoTributariaServices.REG_SIMPLES){
//	                			ICMSST icmsst = new ICMSST();
//	                			vlBaseCalculoST += novaBaseCalculo;
//	                			vlICMSST		+= (rsmTributos.getFloat("pr_aliquota") / 100 * novaBaseCalculo);
//	                			icmsst.setOrig("0");
//	                			icmsst.setCST(rsmTributos.getString("nr_situacao_tributaria"));
//	                			icmsst.setVBCSTRet(Util.formatNumberSemSimbolos(novaBaseCalculo, 2));//Perguntar se o retido vai ser o proprio valor de base de calculo
//	                			icmsst.setVICMSSTRet(Util.formatNumberSemSimbolos((rsmTributos.getFloat("pr_reducao_base_substituicao") / 100 * novaBaseCalculo), 2));//Perguntar se o valor do icms retido eh o mesmo do icms
//	//                			icmsst.setVBCSTDest(value);Perguntar LÉO como eh que se acha o do destino
//	//                			icmsst.setVBCSTDest(value);Perguntar LÉO como eh que se acha o do destino
//	                			icms.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoICMSST(icmsst));; 
//	                		}
	                		//ICMS - Simples Nacional 
	                		else if(rsmTributos.getString("id_tributo").equals("ICMS") && rsmTributos.getInt("lg_simples") == SituacaoTributariaServices.REG_SIMPLES){//Simples Nacional
	                			//Tributação do ICMS pelo SIMPLES NACIONAL e CSOSN=101 (v.2.0)
	                			if(rsmTributos.getString("nr_situacao_tributaria").equals("101")){
	                				ICMSSN101 icms101 = new ICMSSN101();
	                    			icms101.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                    			icms101.setCSOSN("101");
	                    			icms101.setPCredSN(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_credito"), 2));
	                    			icms101.setVCredICMSSN(Util.formatNumberSemSimbolos((rsmTributos.getFloat("pr_credito") / 100 * rsmTributos.getFloat("vl_base_calculo")), 2));
	                    			icms.setICMSSN101(icms101); 
	                    		}
	                			//102- Tributada pelo Simples Nacional sem premissão de crédito.
	                			//103 é Isenção do ICMS no Simples Nacional para faixa de receita bruta.
	                			//300 é Imune.
	                			//400 é não tributada pelo Simples Nacional (v.2.0) (v.2.0)
	                			else if(rsmTributos.getString("nr_situacao_tributaria").equals("102") 
	                			|| rsmTributos.getString("nr_situacao_tributaria").equals("103") 
	                			|| rsmTributos.getString("nr_situacao_tributaria").equals("300") 
	                			|| rsmTributos.getString("nr_situacao_tributaria").equals("400")){
	                				ICMSSN102 icms102 = new ICMSSN102();
	                    			icms102.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                    			icms102.setCSOSN(rsmTributos.getString("nr_situacao_tributaria"));
	                    			icms.setICMSSN102(icms102); 
	                    		}
	                			//201- Tributada pelo Simples Nacional com premissão de crédito. (v.2.0)
	                			else if(rsmTributos.getString("nr_situacao_tributaria").equals("201")){
	                				ICMSSN201 icms201 = new ICMSSN201();
	                    			icms201.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                    			icms201.setCSOSN("201");
	                    			if(rsmTributos.getInt("lg_substituicao") == 1){
	                    				icms201.setModBCST(rsmTributos.getString("tp_base_calculo_substituicao"));
	                    				if(rsmTributos.getFloat("pr_aliquota_substituicao") != 0)
	                    					icms201.setPMVAST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                    				if(rsmTributos.getFloat("pr_reducao_base_substituicao") != 0)
	                    					icms201.setPRedBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_reducao_base_substituicao"), 2));
	                    				icms201.setVBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	                    				icms201.setPICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                    				icms201.setVICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                        		}
	                    			icms201.setPCredSN(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_credito"), 2));
	                    			icms201.setVCredICMSSN(Util.formatNumberSemSimbolos((rsmTributos.getFloat("pr_credito") / 100 * rsmTributos.getFloat("vl_base_calculo")), 2));
	                    			icms.setICMSSN201(icms201); 
	                    		}
	                			//202 - Tributada pelo Simples Nacional sem premissão de crédito e com cobrança do ICMS por Substituição Tributária
	                			//203- Isenção do ICMS nos Simples Nacional para faixa de receita bruta e com cobrança do ICMS por Substituição Tributária (v.2.0)
	                			else if(rsmTributos.getString("nr_situacao_tributaria").equals("202")
	                			|| rsmTributos.getString("nr_situacao_tributaria").equals("203")){
	                				ICMSSN202 icms202 = new ICMSSN202();
	                				icms202.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                    			icms202.setCSOSN(rsmTributos.getString("nr_situacao_tributaria"));
	                    			if(rsmTributos.getInt("lg_substituicao") == 1){
	                    				icms202.setModBCST(rsmTributos.getString("tp_base_calculo_substituicao"));
	                    				if(rsmTributos.getFloat("pr_aliquota_substituicao") != 0)
	                    					icms202.setPMVAST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                    				if(rsmTributos.getFloat("pr_reducao_base_substituicao") != 0)
	                    					icms202.setPRedBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_reducao_base_substituicao"), 2));
	                    				icms202.setVBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	                    				icms202.setPICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                    				icms202.setVICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                        		}
	                    			icms.setICMSSN202(icms202); 
	                    		}
	                			//ICMS cobrado anteriormente por substituição tributária (substituído) ou por antecipação
	                			else if(rsmTributos.getString("nr_situacao_tributaria").equals("500")){
	                				ICMSSN500 icms500 = new ICMSSN500();
	                				icms500.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                    			icms500.setCSOSN("500");
	                    			if(rsmTributos.getInt("lg_substituicao") == 1){
	                    				icms500.setVBCSTRet(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	                        			icms500.setVICMSSTRet(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                        		}
	                    			icms.setICMSSN500(icms500); 
	                    		}
	                			//Tributação pelo ICMS 900 - Outros(v2.0)
	                			else if(rsmTributos.getString("nr_situacao_tributaria").equals("900")){
	                				ICMSSN900 icms900 = new ICMSSN900();
	                    			icms900.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                    			icms900.setCSOSN("900");
	                    			icms900.setModBC(rsmTributos.getString("tp_base_calculo"));
	                    			if(rsmTributos.getFloat("pr_reducao_base") != 0)
	                    				icms900.setPRedBC(rsmTributos.getString("pr_reducao_base"));
	                    			icms900.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                    			icms900.setPICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                    			icms900.setVICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                    			if(rsmTributos.getInt("lg_substituicao") == 1){
	                    				icms900.setModBCST(rsmTributos.getString("tp_base_calculo_substituicao"));
	                    				if(rsmTributos.getFloat("pr_aliquota_substituicao") != 0)
	                    					icms900.setPMVAST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                    				if(rsmTributos.getFloat("pr_reducao_base_substituicao") != 0)
	                    					icms900.setPRedBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_reducao_base_substituicao"), 2));
	                    				icms900.setVBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	                    				icms900.setPICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                    				icms900.setVICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                    				
	                        		}
	                    			icms900.setPCredSN(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_credito"), 2));
	                    			icms900.setVCredICMSSN(Util.formatNumberSemSimbolos((rsmTributos.getFloat("pr_credito") / 100 * rsmTributos.getFloat("vl_base_calculo")), 2));
	                    			icms.setICMSSN900(icms900); 
	                    		}
	                			
	                			
	                		}
	                		
	                		imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoICMS(icms));  
	
	                	}
	                	//IPI
	                	if(rsmTributos.getString("id_tributo").equals("IPI") && !hasISSQN){
	                		if(!hasICMS){
	                			hasICMS = true;
	                        	ICMS icms = new ICMS();  
	        	                ICMSSN102 icmssn102 = new ICMSSN102();  
	        	                icmssn102.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));       
	        	                icmssn102.setCSOSN("102");//Tabela fixa  
	        	                icms.setICMSSN102(icmssn102);
	        	                imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoICMS(icms)); 
	                        }
	                        
	                		hasIPI = true;
	                		TIpi ipi = new TIpi();
	                		vlIPI += Util.arredondar(rsmTributos.getFloat("vl_tributo"), 2);
	                		hasICMSIPIII = true;
	                		
	                		int cdGrupoCigarros = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_CIGARROS", 0, cdEmpresa);
	                		int cdGrupoBebidas = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_BEBIDAS", 0, cdEmpresa);
	                		
	                		if(rsProdutos.getInt("cd_grupo") != 0 && (rsProdutos.getInt("cd_grupo") == cdGrupoCigarros
	                				|| rsProdutos.getInt("cd_grupo") == cdGrupoBebidas)){
	                			ipi.setClEnq(rsmTributos.getString("nl_classe"));
	                		}
	//                		ipi.setCNPJProd(value);Usado para exportacao
//	                		ipi.setClEnq("0");
	                		ipi.setCEnq(rsmTributos.getString("nr_enquadramento")); 
	                  		ipi.setCSelo("0");  // Codigo do selo de controle do IPI
	                      	ipi.setQSelo("0");  // Quantidade de selo de controle do IPI
	                      	ipi.setCEnq("999"); // Código de enquadramento lega do IPI
	                		
	                		
	                		//IPITrib
	                		//00-Entrada com recuperação de crédito
	                		//49-Outras entradas
	                		//50-Saída tributada
	                		//99-Outras saídas
	                		if(rsmTributos.getString("nr_situacao_tributaria").equals("00")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("49")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("50")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("99")){
	                			IPITrib ipitrib = new IPITrib();
	                			ipitrib.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			//:TODO Verificar como esses quatro campos são preenchidos corretamente
	                			// http://www.flexdocs.com.br/guianfe/gerarNFe.detalhe.imp.IPI.html
	                			// fonte de pesquisa
	                			ipitrib.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			ipitrib.setPIPI(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			//--
	                			ipitrib.setVIPI(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			ipi.setIPITrib(ipitrib);
	                		}
	                		//IPINT
	                		//01-Entrada tributada com alíquota zero
	                		//02-Entrada isenta
	                		//03-Entrada não-tributada
	                		//04-Entrada imune
	                		//05-Entrada com suspensão
	                		//51-Saída tributada com alíquota zero
	                		//52-Saída isenta
	                		//53-Saída não-tributada
	                		//54-Saída imune
	                		//55-Saída com suspensão
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("01")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("02")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("03")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("04")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("05")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("51")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("52")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("53")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("54")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("55")){
	                			IPINT ipint = new IPINT();	
	                			ipint.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			ipi.setIPINT(ipint);
	                		}
	                		imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoIPI(ipi));
	                	}
                		//PIS
                		if(rsmTributos.getString("id_tributo").equals("PIS") && rsmTributos.getInt("lg_substituicao") != 1){
                			if(!hasICMS){
                				hasICMS = true;
	                        	ICMS icms = new ICMS();  
	        	                ICMSSN102 icmssn102 = new ICMSSN102();  
	        	                icmssn102.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));       
	        	                icmssn102.setCSOSN("102");//Tabela fixa  
	        	                icms.setICMSSN102(icmssn102);
	        	                imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoICMS(icms)); 
	                        }
                			if(!hasIPI){
                				hasIPI = true;
	                        	TIpi ipi = new TIpi();
	                      		ipi.setClEnq("0");
	                      		ipi.setCSelo("0");  // Codigo do selo de controle do IPI
	                          	ipi.setQSelo("0");  // Quantidade de selo de controle do IPI
	                          	ipi.setCEnq("999"); // Código de enquadramento lega do IPI
	                          	// NT - OK
	                          	IPINT ipiNT = new IPINT();
	                          	ipiNT.setCST("52");
	                            ipi.setIPINT(ipiNT);
	                            imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoIPI(ipi));
	                      	}
	                       
	                		hasPIS = true;
	                		vlPIS 	 += Util.arredondar(rsmTributos.getFloat("vl_tributo"), 2);
	                		vlRetPIS += Util.arredondar(rsmTributos.getFloat("vl_retido"), 2);
	                		PIS pis = new PIS();
	                		//PISAliq
	                		//01 é operação Tributível (base de cálculo = valor da operação alíquota normal (cumulativo/não cumulativo));
	                		//02 - operação Tributível (base de cálculo = valor da operação (alíquota diferenciada));
	                		if(rsmTributos.getString("nr_situacao_tributaria").equals("01")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("02")){
	                			PISAliq pisaliq = new PISAliq();
	                			pisaliq.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			pisaliq.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			pisaliq.setPPIS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			pisaliq.setVPIS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			pis.setPISAliq(pisaliq);
	                		}
	                		//PISQtde
	                		//03 - operação Tributível (base de cálculo = quantidade vendida x alíquota por unidade de produto);
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("03")){
	                			PISQtde pisqtde = new PISQtde();
	                			pisqtde.setCST("03");
	                			pisqtde.setQBCProd(rsProdutos.getString("qt_tributario"));
	//                			pisqtde.setVAliqProd(value);//valor da aliquota em reais
	                			pisqtde.setVPIS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			pis.setPISQtde(pisqtde);
	                		}
	                		//PISNT
	                		//04 - operação Tributível (tributação monofísica (alíquota zero));
	                		//06 - operação Tributível (alíquota zero);
	                		//07 - operação Isenta da Contribuição;
	                		//08 - operação Sem Incidência da Contribuição;
	                		//09 - operação com Suspensão da Contribuição;
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("04")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("06")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("07")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("08")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("09")){
	                			PISNT pisnt = new PISNT();
	                			pisnt.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			pis.setPISNT(pisnt);
	                		}
	                		//PISOutr
	                		//49 - Outras Operações de Saída;
	                		//50 - operação com Direito a Crédito - Vinculada Exclusivamente a Receita Tributada no Mercado Interno;
	                		//51 - operação com Direito a Crédito - Vinculada Exclusivamente a Receita não Tributada no Mercado Interno;
	                		//52 - operação com Direito a Crédito é Vinculada Exclusivamente a Receita de Exportação;
	                		//53 - operação com Direito a Crédito - Vinculada a Receitas Tributadas e não-Tributadas no Mercado Interno;
	                		//54 - operação com Direito a Crédito - Vinculada a Receitas Tributadas no Mercado Interno e de Exportação;
	                		//55 - operação com Direito a Crédito - Vinculada a Receitas não-Tributadas no Mercado Interno e de Exportação;
	                		//56 - operação com Direito a Crédito - Vinculada a Receitas Tributadas e não-Tributadas no Mercado Interno, e de Exportação;
	                		//60 - Crédito Presumido - operação de Aquisição Vinculada Exclusivamente a Receita Tributada no Mercado Interno;
	                		//61 - Crédito Presumido - operação de Aquisição Vinculada Exclusivamente a Receita não-Tributada no Mercado Interno;
	                		//62 - Crédito Presumido - operação de Aquisição Vinculada Exclusivamente a Receita de Exportação;
	                		//63 - Crédito Presumido - operação de Aquisição Vinculada a Receitas Tributadas e não-Tributadas no Mercado Interno;
	                		//64 - Crédito Presumido - operação de Aquisição Vinculada a Receitas Tributadas no Mercado Interno e de Exportação;
	                		//65 - Crédito Presumido - operação de Aquisição Vinculada a Receitas não-Tributadas no Mercado Interno e de Exportação;
	                		//66 - Crédito Presumido - operação de Aquisição Vinculada a Receitas Tributadas e não-Tributadas no Mercado Interno, e de Exportação;
	                		//67 - Crédito Presumido - Outras Operações;
	                		//70 - operação de Aquisição sem Direito a Crédito;
	                		//71 - operação de Aquisição com Isenção;
	                		//72 - operação de Aquisição com Suspensão;
	                		//73 - operação de Aquisição a Aliquota Zero;
	                		//74 - operação de Aquisição; sem Incidência da Contribuição;
	                		//75 - operação de Aquisição por Substituição Tributária;
	                		//98 - Outras Operações de Entrada;
	                		//99 - Outras Operações;
	                		else {
	                			PISOutr pisoutr = new PISOutr();
	                			pisoutr.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			pisoutr.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			pisoutr.setPPIS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			pisoutr.setVPIS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			pis.setPISOutr(pisoutr);
	                		}
	                		imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));  
	                	}
	                	//PISST
	                	else if(rsmTributos.getString("id_tributo").equals("PIS") && rsmTributos.getInt("lg_substituicao") == 1){
	                			hasPIS 	 = true;
	                			hasPISST = true;
	                			vlPIS 	+= Util.arredondar(rsmTributos.getFloat("vl_retido"), 2);
	                			vlBaseStPis = Util.arredondar(rsmTributos.getFloat("vl_base_retencao"), 2);
	                			prAliquotaStPis = rsmTributos.getFloat("pr_aliquota_substituicao");
	                			PIS pis = new PIS();  
	        	                PISNT pisnt = new PISNT();  
	        	                pisnt.setCST("07");
	        	                pis.setPISNT(pisnt); 
	        	                imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));
	                			PISST pisst = new PISST();
	                			pisst.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	                			if(prAliquotaStPis == 0)
		            				return new Result(-1, "Declarado PISST porém sem a aliquota configurada, valor = 0%!");
	                			pisst.setPPIS(Util.formatNumberSemSimbolos(prAliquotaStPis, 2));
	                			pisst.setVPIS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                			imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoPISST(pisst));
	                		
	                	}
	                	
	                	//COFINS
	                	if(rsmTributos.getString("id_tributo").equals("COFINS") && rsmTributos.getInt("lg_substituicao") != 1){
	                		if(!hasICMS){
                				hasICMS = true;
	                        	ICMS icms = new ICMS();  
	        	                ICMSSN102 icmssn102 = new ICMSSN102();  
	        	                icmssn102.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));       
	        	                icmssn102.setCSOSN("102");//Tabela fixa  
	        	                icms.setICMSSN102(icmssn102);
	        	                imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoICMS(icms)); 
	                        }
                			if(!hasIPI){
                				hasIPI = true;
	                        	TIpi ipi = new TIpi();
	                      		ipi.setClEnq("0");
	                      		ipi.setCSelo("0");  // Codigo do selo de controle do IPI
	                          	ipi.setQSelo("0");  // Quantidade de selo de controle do IPI
	                          	ipi.setCEnq("999"); // Código de enquadramento lega do IPI
	                          	// NT - OK
	                          	IPINT ipiNT = new IPINT();
	                          	ipiNT.setCST("52");
	                            ipi.setIPINT(ipiNT);
	                            imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoIPI(ipi));
	                      	}
 	                        if(!hasPIS){
 	                        	hasPIS = true;
	                        	PIS pis = new PIS();  
	        	                PISNT pisnt = new PISNT();  
	        	                pisnt.setCST("07");
	        	                pis.setPISNT(pisnt); 
	        	                imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));
	                        	if(hasCOFINSST){
	                        		PISST pisst = new PISST();
	                        		pisst.setVBC(Util.formatNumberSemSimbolos(vlBaseStCofins, 2));
	                        		pisst.setPPIS(Util.formatNumberSemSimbolos(prAliquotaStCofins, 2));
	                        		pisst.setVPIS(Util.formatNumberSemSimbolos((vlBaseStCofins * prAliquotaStCofins), 2));
	                        		imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoPISST(pisst));
	                        	}
	                      	}
	                        
	                		hasCOFINS = true;
	                		COFINS cofins = new COFINS();
	                		vlCOFINS 	+= Float.parseFloat(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                		vlRetCOFINS += rsmTributos.getFloat("vl_retido");
	                		//COFINSAliq
	                		//01 é operação Tributível (base de cálculo = valor da operação alíquota normal (cumulativo/não cumulativo));
	                		//02 - operação Tributível (base de cálculo = valor da operação (alíquota diferenciada));
	                		if(rsmTributos.getString("nr_situacao_tributaria").equals("01")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("02")){
	                			COFINSAliq cofinsaliq = new COFINSAliq();
	                			cofinsaliq.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			cofinsaliq.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			cofinsaliq.setPCOFINS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			cofinsaliq.setVCOFINS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			cofins.setCOFINSAliq(cofinsaliq);
	                			
	                		}
	                		//CONFINSQtde
	                		//03 - operação Tributível (base de cálculo = quantidade vendida x alíquota por unidade de produto);
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("03")){
	                			COFINSQtde cofinsqtde = new COFINSQtde();
	                			cofinsqtde.setCST("03");
	                			cofinsqtde.setQBCProd(rsProdutos.getString("qt_tributario"));
	//                			cofinsqtde.setVAliqProd(value);//valor da aliquota em reais
	                			cofinsqtde.setVCOFINS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			cofins.setCOFINSQtde(cofinsqtde);
	                		}
	                		//COFINSNT
	                		//04 - operação Tributível (tributação monofísica (alíquota zero));
	                		//06 - operação Tributível (alíquota zero);
	                		//07 - operação Isenta da Contribuição;
	                		//08 - operação Sem Incidência da Contribuição;
	                		//09 - operação com Suspensão da Contribuição;
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("04")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("06")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("07")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("08")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("09")){
	                			COFINSNT cofinsnt = new COFINSNT();
	                			cofinsnt.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			cofins.setCOFINSNT(cofinsnt);
	                		}
	                		//COFINSOutr
	                		//49 - Outras Operações de Saída;
	                		//50 - operação com Direito a Crédito - Vinculada Exclusivamente a Receita Tributada no Mercado Interno;
	                		//51 - operação com Direito a Crédito - Vinculada Exclusivamente a Receita não Tributada no Mercado Interno;
	                		//52 - operação com Direito a Crédito é Vinculada Exclusivamente a Receita de Exportação;
	                		//53 - operação com Direito a Crédito - Vinculada a Receitas Tributadas e não-Tributadas no Mercado Interno;
	                		//54 - operação com Direito a Crédito - Vinculada a Receitas Tributadas no Mercado Interno e de Exportação;
	                		//55 - operação com Direito a Crédito - Vinculada a Receitas não-Tributadas no Mercado Interno e de Exportação;
	                		//56 - operação com Direito a Crédito - Vinculada a Receitas Tributadas e não-Tributadas no Mercado Interno, e de Exportação;
	                		//60 - Crédito Presumido - operação de Aquisição Vinculada Exclusivamente a Receita Tributada no Mercado Interno;
	                		//61 - Crédito Presumido - operação de Aquisição Vinculada Exclusivamente a Receita não-Tributada no Mercado Interno;
	                		//62 - Crédito Presumido - operação de Aquisição Vinculada Exclusivamente a Receita de Exportação;
	                		//63 - Crédito Presumido - operação de Aquisição Vinculada a Receitas Tributadas e não-Tributadas no Mercado Interno;
	                		//64 - Crédito Presumido - operação de Aquisição Vinculada a Receitas Tributadas no Mercado Interno e de Exportação;
	                		//65 - Crédito Presumido - operação de Aquisição Vinculada a Receitas não-Tributadas no Mercado Interno e de Exportação;
	                		//66 - Crédito Presumido - operação de Aquisição Vinculada a Receitas Tributadas e não-Tributadas no Mercado Interno, e de Exportação;
	                		//67 - Crédito Presumido - Outras Operações;
	                		//70 - operação de Aquisição sem Direito a Crédito;
	                		//71 - operação de Aquisição com Isenção;
	                		//72 - operação de Aquisição com Suspensão;
	                		//73 - operação de Aquisição a Aliquota Zero;
	                		//74 - operação de Aquisição; sem Incidência da Contribuição;
	                		//75 - operação de Aquisição por Substituição Tributária;
	                		//98 - Outras Operações de Entrada;
	                		//99 - Outras Operações;
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("49")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("50")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("51")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("52")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("53")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("54")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("55")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("56")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("60")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("61")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("62")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("63")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("64")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("65")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("66")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("67")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("70")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("71")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("72")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("73")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("74")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("75")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("98")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("99")){
	                			COFINSOutr cofinsoutr = new COFINSOutr();
	                			cofinsoutr.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			cofinsoutr.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			cofinsoutr.setPCOFINS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			cofinsoutr.setVCOFINS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			cofins.setCOFINSOutr(cofinsoutr);
	                		}
	                		
	                		imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoCOFINS(cofins));
	                	}
	                	//COFINSST
	                	else if(rsmTributos.getString("id_tributo").equals("COFINS") && rsmTributos.getInt("lg_substituicao") == 1){
	                		hasCOFINS 	= true;
	                		hasCOFINSST = true;
	                		vlCOFINS 	+= Float.parseFloat(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                		vlBaseStCofins 	   = Util.arredondar(rsmTributos.getFloat("vl_base_retencao"), 2);
                			prAliquotaStCofins = rsmTributos.getFloat("pr_aliquota_substituicao");
                			COFINS cofins = new COFINS();  
                            COFINSNT cofinsnt = new COFINSNT();  
                            cofinsnt.setCST("07");
                            cofins.setCOFINSNT(cofinsnt);
                            imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoCOFINS(cofins));
	            			COFINSST cofinsst = new COFINSST();
	            			cofinsst.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	            			if(prAliquotaStCofins == 0)
	            				return new Result(-1, "Declarado COFINSST porém sem a aliquota configurada, valor = 0%!");
	            			cofinsst.setPCOFINS(Util.formatNumberSemSimbolos(prAliquotaStCofins, 2));
	            			cofinsst.setVCOFINS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	            			imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoCOFINSST(cofinsst));
	            		}
	                	
	//            		Nao usado no momento 31/05/2013
	//                	//Quando o ISSQN é informado, o ICMS, IPI, II não são informados, e vice-versa
	//                	if(rsmTributos.getString("id_tributo") == "ISSQN" && !hasICMSIPIII){
	//                		hasISSQN = true;
	//                		ISSQN issqn = new ISSQN();
	//                		issqn.setVBC(Util.formatNumberSemSimbolos(novaBaseCalculo, 2));
	//                		issqn.setVAliq(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));//Verificar se é em porcentagem ou real
	//                		issqn.setVISSQN(Util.formatNumberSemSimbolos((rsmTributos.getFloat("pr_aliquota") / 100 * novaBaseCalculo), 2));
	//                		issqn.setCMunFG(rsNfe.getString("id_ibge"));
	////                		issqn.setCListServ(value);//Saber se vai usar
	//                		issqn.setCSitTrib(value);//Informar o Código da tributação do ISSQN:
	////                									N é NORMAL;
	////                									R é RETIDA;
	////                									S é SUBSTITUTA;
	////                									I é ISENTA. (v.2.0)
	//                		imposto.setISSQN(issqn);
	//                	}
	                	
	                	//Grupo de Imposto de Importacao
	                	if(rsmTributos.getString("id_tributo").equals("II") && !hasISSQN){
	                		if(!hasICMS){
                				hasICMS = true;
	                        	ICMS icms = new ICMS();  
	        	                ICMSSN102 icmssn102 = new ICMSSN102();  
	        	                icmssn102.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));       
	        	                icmssn102.setCSOSN("102");//Tabela fixa  
	        	                icms.setICMSSN102(icmssn102);
	        	                imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoICMS(icms)); 
	                        }
                			if(!hasIPI){
                				hasIPI = true;
	                        	TIpi ipi = new TIpi();
	                      		ipi.setClEnq("0");
	                      		ipi.setCSelo("0");  // Codigo do selo de controle do IPI
	                          	ipi.setQSelo("0");  // Quantidade de selo de controle do IPI
	                          	ipi.setCEnq("999"); // Código de enquadramento lega do IPI
	                          	// NT - OK
	                          	IPINT ipiNT = new IPINT();
	                          	ipiNT.setCST("52");
	                            ipi.setIPINT(ipiNT);
	                            imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoIPI(ipi));
	                      	}
 	                        if(!hasPIS){
 	                        	hasPIS = true;
	                        	PIS pis = new PIS();  
	        	                PISNT pisnt = new PISNT();  
	        	                pisnt.setCST("07");
	        	                pis.setPISNT(pisnt); 
	        	                imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));
	                        	if(hasCOFINSST){
	                        		PISST pisst = new PISST();
	                        		pisst.setVBC(Util.formatNumberSemSimbolos(vlBaseStCofins, 2));
	                        		pisst.setPPIS(Util.formatNumberSemSimbolos(prAliquotaStCofins, 2));
	                        		pisst.setVPIS(Util.formatNumberSemSimbolos((vlBaseStCofins * prAliquotaStCofins), 2));
	                        		imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoPISST(pisst));
	                        	}
	                      	}
	                		if(!hasCOFINS){
	                        	hasCOFINS = true;
	                        	COFINS cofins = new COFINS();  
	                            COFINSNT cofinsnt = new COFINSNT();  
	                            cofinsnt.setCST("07");
	                            cofins.setCOFINSNT(cofinsnt);
	                            imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoCOFINS(cofins));
	                        	if(hasPISST){
	                        		COFINSST cofinsst = new COFINSST();
	                        		cofinsst.setVBC(Util.formatNumberSemSimbolos(vlBaseStPis, 2));
	                        		cofinsst.setPCOFINS(Util.formatNumberSemSimbolos(prAliquotaStPis, 2));
	                        		cofinsst.setVCOFINS(Util.formatNumberSemSimbolos((vlBaseStPis * prAliquotaStPis), 2));
	                        		imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoCOFINSST(cofinsst));
	                        	}
	                      	}
	                		vlII += Util.arredondar(rsmTributos.getFloat("vl_tributo"), 2);
	                		hasICMSIPIII = true;
	                		II ii = new II();	
	            			ii.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	            			ResultSetMap rsmAdicao = AdicaoServices.getAdicaoByNotaFiscalNcm(rsNfe.getInt("cd_nota_fiscal"), rsProdutos.getInt("cd_ncm"));
	            			if(rsmAdicao.next())
	            				ii.setVDespAdu(Util.formatNumberSemSimbolos(rsmAdicao.getFloat("vl_aduaneiro"), 2));
	            			else
	            				return new Result(-1, "O produto " + rsProdutos.getString("nm_produto_servico") + " não esta ligado a nenhuma adição!");
	            			ii.setVII(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	            			ii.setVIOF(Util.formatNumberSemSimbolos(0, 2));
	            			imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoII(ii));
	                	}
	                	
	                }while(rsmTributos.next());
                }
                if(!hasICMS){
    				hasICMS = true;
                	ICMS icms = new ICMS();  
	                ICMSSN102 icmssn102 = new ICMSSN102();  
	                icmssn102.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));       
	                icmssn102.setCSOSN("102");//Tabela fixa  
	                icms.setICMSSN102(icmssn102);
	                imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoICMS(icms)); 
                }
    			if(!hasIPI){
    				hasIPI = true;
                	TIpi ipi = new TIpi();
              		ipi.setClEnq("0");
              		ipi.setCSelo("0");  // Codigo do selo de controle do IPI
                  	ipi.setQSelo("0");  // Quantidade de selo de controle do IPI
                  	ipi.setCEnq("999"); // Código de enquadramento lega do IPI
                  	// NT - OK
                  	IPINT ipiNT = new IPINT();
                  	ipiNT.setCST("52");
                    ipi.setIPINT(ipiNT);
                    imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoIPI(ipi));
              	}
                 if(!hasPIS){
                 	hasPIS = true;
                	PIS pis = new PIS();  
	                PISNT pisnt = new PISNT();  
	                pisnt.setCST("07");
	                pis.setPISNT(pisnt); 
	                imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));
                	if(hasCOFINSST){
                		PISST pisst = new PISST();
                		pisst.setVBC(Util.formatNumberSemSimbolos(vlBaseStCofins, 2));
                		pisst.setPPIS(Util.formatNumberSemSimbolos(prAliquotaStCofins, 2));
                		pisst.setVPIS(Util.formatNumberSemSimbolos((vlBaseStCofins * prAliquotaStCofins), 2));
                		imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoPISST(pisst));
                	}
              	}
                
                if(!hasCOFINS){
                	hasCOFINS = true;
                	COFINS cofins = new COFINS();  
                    COFINSNT cofinsnt = new COFINSNT();  
                    cofinsnt.setCST("07");
                    cofins.setCOFINSNT(cofinsnt);
                    imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoCOFINS(cofins));
                	if(hasPISST){
                		COFINSST cofinsst = new COFINSST();
                		cofinsst.setVBC(Util.formatNumberSemSimbolos(vlBaseStPis, 2));
                		cofinsst.setPCOFINS(Util.formatNumberSemSimbolos(prAliquotaStPis, 2));
                		cofinsst.setVCOFINS(Util.formatNumberSemSimbolos((vlBaseStPis * prAliquotaStPis), 2));
                		imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoCOFINSST(cofinsst));
                	}
              	}
                
                det.setImposto(imposto);  // inclui impostos no detalhe  
                infNFe.getDet().add(det); // inclui detalhes do produto na nota
                //
                nrItem++;
            } while(rsProdutos.next());
            
            if(nrItem > 991)//28-12-2012
            	return new Result(-1, "Limite de itens por nota atingido!");//O maximo de itens que uma nota pode ter eh 990 (o 991 eh por que na ultima ele pode acrescentar
            																//mais 1, e nao ter nenhuma nota a mais para processar
            
            
            /*
             *  Totais da NF-e  
             */
            Total total = new Total();  
            // Total do ICMS
            ICMSTot icmstot = new ICMSTot();  
            /**
             * (+) vProd (Somatório do valor de todos os produtos da NF-e);
				(-) vDesc (Somatório do desconto de todos os produtos da NF-e);
				(+) vST (Somatório do valor do ICMS com Substituição Tributária de todos os produtos da NF-e);
				(+) vFrete (Somatório do valor do Frete de todos os produtos da NF-e);
				(+) vSeg (Somatório do valor do seguro de todos os produtos da NF-e);
				(+) vOutro (Somatório do valor de outras despesas de todos os produtos da NF-e);
				(+) vII (Somatório do valor do Imposto de Importação de todos os produtos da NF-e);
				(+) vIPI (Somatório do valor do IPI de todos os produtos da NF-e);
				(+) vServ (Somatório do valor do Serviço de todos os itens da NF-e).
             */
            if(Integer.parseInt(rsNfe.getString("nr_cfop")) == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_SUBSTITUTO", 0) ||
    		   Integer.parseInt(rsNfe.getString("nr_cfop")) == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_OUTRO_ESTADO", 0) ||
			   Integer.parseInt(rsNfe.getString("nr_cfop")) == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_SUBSTITUTO_OUTRO_ESTADO", 0) ||
			   Integer.parseInt(rsNfe.getString("nr_cfop")) == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR", 0)){
     				
            	
            	int cdTributoIcms = TributoServices.getCdTributoById("ICMS", connect);
            	
            	NotaFiscalTributo notaFiscalTributo = NotaFiscalTributoDAO.get(rsNfe.getInt("cd_nota_fiscal"), cdTributoIcms, connect);
            	
            	if(notaFiscalTributo != null){
	            	icmstot.setVBC(Util.formatNumberSemSimbolos(notaFiscalTributo.getVlBaseCalculo(), 2));
	                icmstot.setVICMS(Util.formatNumberSemSimbolos(notaFiscalTributo.getVlTributo(), 2));  
	                icmstot.setVICMSDeson(Util.formatNumberSemSimbolos(vlICMSDeson, 2)); 
	                icmstot.setVBCST(Util.formatNumberSemSimbolos(notaFiscalTributo.getVlBaseRetencao(), 2));  
	                icmstot.setVST(Util.formatNumberSemSimbolos(notaFiscalTributo.getVlRetido(), 2));
	                vlICMSST = notaFiscalTributo.getVlRetido();
            	}
            	else{
            		icmstot.setVBC(Util.formatNumberSemSimbolos(0, 2));
	                icmstot.setVICMS(Util.formatNumberSemSimbolos(0, 2));  
	                icmstot.setVICMSDeson(Util.formatNumberSemSimbolos(vlICMSDeson, 2)); 
	                icmstot.setVBCST(Util.formatNumberSemSimbolos(0, 2));  
	                icmstot.setVST(Util.formatNumberSemSimbolos(0, 2));
            	}
            }
            else{
            	icmstot.setVBC(Util.formatNumberSemSimbolos(vlBaseCalculo, 2));
                icmstot.setVICMS(Util.formatNumberSemSimbolos(vlICMS, 2));  
                icmstot.setVICMSDeson(Util.formatNumberSemSimbolos(vlICMSDeson, 2)); 
                icmstot.setVBCST(Util.formatNumberSemSimbolos(vlBaseCalculoST, 2));  
                icmstot.setVST(Util.formatNumberSemSimbolos(vlICMSST, 2));  
            }
            icmstot.setVProd(Util.formatNumberSemSimbolos(vlTotalProdutos, 2));  
            icmstot.setVFrete(Util.formatNumberSemSimbolos(vlFrete, 2));  
            icmstot.setVSeg(Util.formatNumberSemSimbolos(vlSeguros, 2));
            icmstot.setVDesc(Util.formatNumberSemSimbolos(((vlDescontos <= 0) ? 0 : vlDescontos), 2));  
            icmstot.setVII(Util.formatNumberSemSimbolos(vlII, 2));  
            icmstot.setVIPI(Util.formatNumberSemSimbolos(vlIPI, 2)); 
            icmstot.setVPIS(Util.formatNumberSemSimbolos(vlPIS, 2));  
            icmstot.setVCOFINS(Util.formatNumberSemSimbolos(vlCOFINS, 2));
            icmstot.setVOutro(Util.formatNumberSemSimbolos(vlOutro, 2));
            
            double valorTotalNota = vlTotalProdutos - vlDescontos + vlICMSST + vlFrete + vlSeguros + vlOutro + vlII + vlIPI;
          
	        if(cdPaisEmi != cdPaisForn && isDocEnt){
	        	valorTotalNota += vlICMS + vlPIS + vlCOFINS;
	        }
            icmstot.setVNF(Util.formatNumberSemSimbolos(valorTotalNota, 2));  
            total.setICMSTot(icmstot);
            
            //Grupo de retencao de tributos
            if(vlRetCOFINS != 0 || vlRetPIS != 0){
	            RetTrib rettrib = new RetTrib();
	            if(vlRetCOFINS != 0)
	            	rettrib.setVRetCOFINS(Util.formatNumberSemSimbolos(vlRetCOFINS, 2));
	            if(vlRetPIS != 0)
	            	rettrib.setVRetPIS(Util.formatNumberSemSimbolos(vlRetPIS, 2));
	            total.setRetTrib(rettrib);
            }
            
            
            infNFe.setTotal(total);
            
            Transp transp = new Transp();  
            
            transp.setModFrete(rsNfe.getString("tp_modalidade_frete"));
            
            boolean entrou = false;
            Vol vol = new Vol();
        	if(rsNfe.getString("ds_numeracao") != null && !rsNfe.getString("ds_numeracao").equals("") && !rsNfe.getString("ds_numeracao").equals("null")){
        		vol.setNVol(rsNfe.getString("ds_numeracao"));
        		entrou = true;
        	}
        	if(rsNfe.getString("qt_volume") != null && !rsNfe.getString("qt_volume").equals("") && !rsNfe.getString("qt_volume").equals("null")){
            	vol.setQVol(Util.formatNumberSemSimbolos(Float.parseFloat(rsNfe.getString("qt_volume")), 0));
	        	entrou = true;
	    	}
            if(rsNfe.getString("ds_especie") != null && !rsNfe.getString("ds_especie").equals("") && !rsNfe.getString("ds_especie").equals("null")){
            	vol.setEsp(rsNfe.getString("ds_especie"));
            	entrou = true;
        	}
            if(rsNfe.getString("ds_marca") != null && !rsNfe.getString("ds_marca").equals("") && !rsNfe.getString("ds_marca").equals("null")){
            	vol.setMarca(rsNfe.getString("ds_marca"));
            	entrou = true;
        	}
            if(rsNfe.getString("vl_peso_liquido") != null && !rsNfe.getString("vl_peso_liquido").equals("") && !rsNfe.getString("vl_peso_liquido").equals("null") && rsNfe.getFloat("vl_peso_liquido") != 0){
            	vol.setPesoL(Util.formatNumberSemSimbolos(rsNfe.getFloat("vl_peso_liquido"), 3));  
            	entrou = true;
        	}
            if(rsNfe.getString("vl_peso_bruto") != null && !rsNfe.getString("vl_peso_bruto").equals("") && !rsNfe.getString("vl_peso_bruto").equals("null") && rsNfe.getFloat("vl_peso_bruto") != 0){
            	vol.setPesoB(Util.formatNumberSemSimbolos(rsNfe.getFloat("vl_peso_bruto"), 3));
            	entrou = true;
        	}
            
            if(entrou)
            	transp.getVol().add(vol);
            //Dados de Veiculo
            if(rsNfe.getString("nr_placa") != null && !rsNfe.getString("nr_placa").equals("") && !rsNfe.getString("nr_placa").equals("null")
            && rsNfe.getString("sg_uf_veiculo") != null && !rsNfe.getString("sg_uf_veiculo").equals("") && !rsNfe.getString("sg_uf_veiculo").equals("null") && Util.isSgEstado(rsNfe.getString("sg_uf_veiculo"))){
            	if(!Util.isPlacaValida(rsNfe.getString("nr_placa")))
    	        	return new Result(-1, "Placa do veiculo invalida");
            	
	            TVeiculo veiculo = new TVeiculo();
	            veiculo.setPlaca(rsNfe.getString("nr_placa"));
	            if(rsNfe.getString("nr_rntc") != null && !rsNfe.getString("nr_rntc").equals("") && !rsNfe.getString("nr_rntc").equals("null"))
	            	veiculo.setRNTC(rsNfe.getString("nr_rntc"));
	            veiculo.setUF(TUf.valueOf(rsNfe.getString("sg_uf_veiculo").toUpperCase()));
	            transp.setVeicTransp(veiculo);
        	}
            /*
             *  Dados do Transporte
             */
            ResultSet rsTransp = connect.prepareStatement("SELECT nr_cnpj, nr_cpf, nm_pessoa, gn_pessoa, nr_inscricao_estadual, nm_email, " +
										                  "       nm_logradouro, nm_bairro, nm_complemento, nr_endereco, nr_telefone1, nr_telefone2, nr_fax, nr_celular, " +
										                  "       nm_cidade, id_ibge, sg_estado " +
										                  "FROM grl_pessoa A " +
										                  "LEFT OUTER JOIN grl_pessoa_fisica    B ON (A.cd_pessoa = B.cd_pessoa) " +
										                  "LEFT OUTER JOIN grl_pessoa_juridica  C ON (A.cd_pessoa = C.cd_pessoa) " +
										                  "LEFT OUTER JOIN grl_pessoa_endereco  E ON (A.cd_pessoa = E.cd_pessoa)"+
														  "LEFT OUTER JOIN grl_cidade           F ON (E.cd_cidade            = F.cd_cidade) "+
														  "LEFT OUTER JOIN grl_estado           G ON (F.cd_estado            = G.cd_estado) "+
										                  "WHERE A.cd_pessoa = "+rsNfe.getInt("cd_transportador")).executeQuery();
            if(rsTransp.next())	{
            	// PRÉ-VALIDAÇÃO
            	if((rsTransp.getInt("gn_pessoa") == com.tivic.manager.grl.PessoaServices.TP_JURIDICA) && (rsTransp.getString("nr_cnpj")==null || rsTransp.getString("nr_cnpj").trim().equals("") || !Util.isCNPJ(rsTransp.getString("nr_cnpj"))))
    	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CNPJ";
            	if((rsTransp.getInt("gn_pessoa") == com.tivic.manager.grl.PessoaServices.TP_FISICA) && (rsTransp.getString("nr_cpf")==null || rsTransp.getString("nr_cpf").trim().equals("") || !Util.isCpfValido(rsTransp.getString("nr_cpf"))))
    	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CPF";
            	if(rsTransp.getString("nm_cidade") == null || rsTransp.getString("nm_cidade").trim().equals(""))
    	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Nome da Cidade";
            	if(rsTransp.getString("sg_estado") == null)
    	        	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Estado";
            	if(!dsValidacao.equals(""))
                	return new Result(-1, "Dados da transportadora "+rsTransp.getString("nm_pessoa") +" faltando ou inválidos: "+dsValidacao);
               
            	// Dados da Transportadora. 
                Transporta transporta = new Transporta();  
                
                if(rsTransp.getInt("gn_pessoa") == com.tivic.manager.grl.PessoaServices.TP_JURIDICA)
                	transporta.setCNPJ(rsTransp.getString("nr_cnpj"));
                if(rsTransp.getInt("gn_pessoa") == com.tivic.manager.grl.PessoaServices.TP_FISICA)
                	transporta.setCPF(rsTransp.getString("nr_cpf"));
	            transporta.setXNome(rsTransp.getString("nm_pessoa").trim());//  "JavaC - Java Communuty"
//	            transporta.setIE(rsTransp.getString("nr_inscricao_estadual") != null ? rsTransp.getString("nr_inscricao_estadual") : "");
	            // Endereço
	            String enderTran =((rsTransp.getString("nm_logradouro") != null) ? rsTransp.getString("nm_logradouro") : "") + " " + ((rsTransp.getString("nm_complemento") != null) ? rsTransp.getString("nm_complemento").trim() : "");
	            if(!enderTran.trim().equals(""))
	            	transporta.setXEnder(enderTran.trim());//
	            transporta.setXMun(rsTransp.getString("nm_cidade")!=null? rsTransp.getString("nm_cidade").trim() : "");
	            if(!NotaFiscalServices.validaSiglaEstado(rsTransp.getString("sg_estado")))
	            	return new Result(-1, "Erro: Corrigir a sigla do estado para transporte");
//	            transporta.setUF(TUf.valueOf(rsTransp.getString("sg_estado")));
	            transp.setTransporta(transporta);  
	        }
            
            
        	infNFe.setTransp(transp);
        	// Informações Adicionais
            InfAdic infAdic = new InfAdic();
            infAdic.setInfAdFisco(rsNfe.getString("txt_informacao_fisco"));//16-01-2013
            // Documentos Vinculados
            String txtObservacao = rsNfe.getString("txt_observacao")!=null ? rsNfe.getString("txt_observacao") : "";
            int qtDocs = 0, qtLimite = txtObservacao.equals("") ? 10 : 7;
            if(txtObservacao.indexOf("CUPOM")<0 && txtObservacao.indexOf("DAV")<0) {
            	txtObservacao += (txtObservacao.equals("") || qtDocs==qtLimite? "" : "");
                ResultSetMap rsmDocVinculados = DocumentoSaidaServices.findCompletoByNfe(rsNfe.getInt("cd_nota_fiscal"));
	            while(rsmDocVinculados.next()) {
	            	if(rsmDocVinculados.getInt("tp_documento_saida")==DocumentoSaidaServices.TP_CUPOM_FISCAL) {
	            		qtDocs++;
	            		txtObservacao += (qtDocs==qtLimite ? "|" : ""); 
	            		txtObservacao += (txtObservacao.equals("") || qtDocs==qtLimite? "" : "")+"Cupom Fiscal - Modelo 2B, N. ECF: "+rsmDocVinculados.getString("nr_serie_ecf")+
	            		                                                         ", COO: "+rsmDocVinculados.getString("nr_documento_saida");
	            	}
	            	else if(rsmDocVinculados.getInt("tp_documento_saida")==DocumentoSaidaServices.TP_CUPOM_FISCAL) {
	            		qtDocs++;
	            		txtObservacao += (qtDocs==qtLimite ? "|" : ""); 
	            		txtObservacao += (txtObservacao.equals("") || qtDocs==qtLimite? "" : "")+"DAV No: "+rsmDocVinculados.getString("nr_documento_saida");
	            	}
	            }
            }
            NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);
            if(nota.getTpEmissao() == NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC){
            	txtObservacao += " DANFE impresso em contingência - DPEC regularmente recebida pela Receita Federal do Brasil.";
            }
            
            if(!txtObservacao.trim().equals(""))
            	infAdic.setInfCpl(txtObservacao.trim());
            
            //Acrescentar valor do IPBT
            PreparedStatement pstm = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_doc_vinculado A "
            		+ " LEFT JOIN alm_documento_saida B ON (B.cd_documento_saida = A.cd_documento_saida)"
            		+ " WHERE A.cd_nota_fiscal = ?"
            		+ "   AND A.cd_documento_saida IS NOT NULL "
            		+ "   AND A.cd_documento_saida <> 0 "
            		+ "   AND B.cd_empresa     = ?");
            pstm.setInt(1, cdNotaFiscal);
            pstm.setInt(2, cdEmpresa);
            
            ResultSetMap rsmDocSaida = new ResultSetMap(pstm.executeQuery());
            
            int cdDocumentoSaida = 0;
            float vlTotalDocumento = 0;
            while(rsmDocSaida.next()){
	            cdDocumentoSaida = rsmDocSaida.getInt("CD_DOCUMENTO_SAIDA");
				vlTotalDocumento = rsmDocSaida.getFloat("VL_TOTAL_DOCUMENTO");
				if(vlTotalDocumento == 0)
					vlTotalDocumento = DocumentoSaidaServices.getValorDocumento(cdDocumentoSaida, connect);
            }
            if(cdDocumentoSaida > 0 && vlTotalDocumento > 0 && rsNfe.getString("tp_movimento").equals("1")){
            	infAdic.setInfAdFisco(infAdic.getInfAdFisco() + SaidaItemAliquotaServices.getAliquotaOfDocSaida(cdDocumentoSaida, cdEmpresa, valorTotalNota));
            }
            
            if(infAdic.getInfAdFisco() == null || infAdic.getInfAdFisco().equals("")){
            	infAdic.setInfAdFisco("SEM INFORMAÇÕES DE FISCO");
            }
            	
            
            //	
            infNFe.setInfAdic(infAdic);  
            nFe.setInfNFe(infNFe);  
            TEnviNFe enviNFe = new TEnviNFe();  
            enviNFe.setVersao("3.10");  
            enviNFe.setIdLote("0000001");
            enviNFe.setIndSinc("0");
            enviNFe.getNFe().add(nFe);  
            JAXBContext context   = JAXBContext.newInstance(TEnviNFe.class);  
            Marshaller marshaller = context.createMarshaller();  
            JAXBElement<TEnviNFe> element = new ObjectFactory().createEnviNFe((enviNFe));  
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);  
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);  
            StringWriter sw = new StringWriter();  
            marshaller.marshal(element, sw);
            // 
            String xml = sw.toString();
            xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + xml;
            xml = xml.replaceAll("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\" ", "");  
            xml = xml.replaceAll("<NFe>", "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
            xml = xml.replaceAll("\r", "");
            xml = xml.replaceAll("\t", "");
            xml = xml.replaceAll("\n", "");

            xml = removeAcentos(xml);
            /*
             * Criando objeto d retorno
             */
            Result result = new Result(1, "Nota Validada com sucesso");
            result.addObject("xml", xml);
            result.addObject("nrNfe", rsNfe.getString("nr_nota_fiscal"));
            return result;
        } 
        catch (Exception e) {  
            error(e.toString());
            e.printStackTrace(System.out);            
            return new Result(-1, "Erro ao tentar gerar xml: ", e);
        }  
		
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
    }

	
	public static String getXmlComProtocolo(int cdNotaFiscal, NotaFiscalHistorico notaHistorico, int cdEmpresa) {
		return getXmlComProtocolo(cdNotaFiscal, notaHistorico, cdEmpresa, null);
	}
	
	public static String getXmlComProtocolo(int cdNotaFiscal, NotaFiscalHistorico notaHistorico, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect == null;
		if(isConnectionNull)
			connect = Conexao.conectar();
		try {
			if(notaHistorico==null) {
				ResultSet rs = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_historico " +
						                                "WHERE cd_nota_fiscal = "+cdNotaFiscal+
						                                " AND  tp_status      = \'100\' AND dt_recebimento IS NOT NULL").executeQuery();
				if(rs.next())
					notaHistorico = NotaFiscalHistoricoDAO.get(cdNotaFiscal, rs.getInt("cd_historico"), connect);
		    }
	    	String[] retornoSefaz = null;
	    	if(notaHistorico!=null){
	    		retornoSefaz = new String[9];
		        retornoSefaz[0] = String.valueOf(notaHistorico.getTpAmbiente());
		        retornoSefaz[1] = String.valueOf(notaHistorico.getNrVersao());
		        retornoSefaz[2] = String.valueOf(Util.formatDate(notaHistorico.getDtRecebimento(), "yyyy-MM-dd'T'HH:mm:ss'-03:00'"));
		        retornoSefaz[3] = String.valueOf(notaHistorico.getNrChave());
		        retornoSefaz[4] = String.valueOf(notaHistorico.getNrProtocolo());
		        retornoSefaz[5] = String.valueOf(notaHistorico.getNrDigito());
		        retornoSefaz[6] = String.valueOf(notaHistorico.getTpStatus());
		        retornoSefaz[7] = String.valueOf(notaHistorico.getDsMensagem());
		        retornoSefaz[8] = String.valueOf(notaHistorico.getIdTransacao());
	    	}
	        return getXmlComProtocolo(cdNotaFiscal, retornoSefaz, cdEmpresa, connect);
		}
        catch (Exception e) {  
            error(e.toString());
            e.printStackTrace();
            return "Erro xmlProtocolo 321: " + e;
        }  
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String getXmlComProtocolo(int cdNotaFiscal, String[] retornoSefaz, int cdEmpresa) {
		return getXmlComProtocolo(cdNotaFiscal, retornoSefaz, cdEmpresa, null);
	}
	
	/**
	 * Monta um Xml para imprimir o Danfe 
	 * 
	 * @param cdNotaFiscal
	 * @param retornoSefaz
	 * @param cdEmpresa
	 * @param connect
	 * @return
	 */
	public static String getXmlComProtocolo(int cdNotaFiscal, String[] retornoSefaz, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect == null;
		if(isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			float vlII 			  = 0;
            float vlIPI 		  = 0;
            float vlPIS 		  = 0;
            float vlCOFINS 		  = 0;
            double vlBaseCalculo   = 0;
        	float vlICMS          = 0;
        	float vlICMSDeson     = 0;
        	float vlBaseCalculoST = 0; 
        	float vlICMSST        = 0;  
        	float vlDescontos     = 0;
        	@SuppressWarnings("unused")
			float vlAcrescimo     = 0;
        	float vlRetPIS 	  	  = 0;
        	float vlRetCOFINS 	  = 0;
        	
        	
        	
			if(isConnectionNull)
				connect.setAutoCommit(false);
			
        	ResultSet rsNfe = connect.prepareStatement("SELECT A.*, B.nm_natureza_operacao, "+
														"      B.nr_codigo_fiscal AS nr_cfop, B.cd_natureza_operacao, C.nm_pessoa AS nm_fantasia, " +
														"      C.nr_telefone1, C.nr_telefone2, C.nr_fax, C.nr_celular, C.nm_email,  " +
														"      H.nr_telefone1 as contato_nr_telefone1 , H.nr_telefone2 AS contato_nr_telefone2, " +
								                        "      H.nr_celular AS contato_nr_celular, H.nr_celular2 AS contato_nr_celular2, H.nr_celular3 AS contato_nr_celular3, " +
														" 	   H.nr_celular4 AS contato_nr_celular4, " +														
														"      D.nr_cnpj AS nr_cnpj_emissor, D.nm_razao_social, D.nr_inscricao_estadual, D.nr_inscricao_municipal, "+
														"      E.nm_logradouro, E.nm_bairro, E.nr_endereco, E.nr_cep, E.nm_complemento, "+
														"      F.nm_cidade, F.id_ibge, G.sg_estado, G.cd_estado, G.cd_pais "+
														"FROM fsc_nota_fiscal A "+
														"JOIN adm_natureza_operacao B ON (A.cd_natureza_operacao = B.cd_natureza_operacao) "+
														"JOIN grl_pessoa            C ON (A.cd_empresa           = C.cd_pessoa) "+
														"JOIN grl_pessoa_juridica   D ON (A.cd_empresa           = D.cd_pessoa) "+
														"JOIN grl_pessoa_endereco   E ON (A.cd_empresa           = E.cd_pessoa  "+
														"                             AND E.lg_principal         = 1)           "+
														"LEFT OUTER JOIN grl_pessoa_contato H ON (C.cd_pessoa 	 = H.cd_pessoa )" +
														"JOIN grl_cidade            F ON (E.cd_cidade            = F.cd_cidade) "+
														"JOIN grl_estado            G ON (F.cd_estado            = G.cd_estado) "+
														"WHERE A.cd_nota_fiscal = " + cdNotaFiscal).executeQuery();
        	
            if(!rsNfe.next())
            	return "Nota fiscal não localizada!";
            double vlTotalProdutos = 0;
            float vlSeguros       = rsNfe.getFloat("vl_seguro"); // Usado no total de ICMS, usar assim ou são quando tiver produtos que tenham ICMS?
        	float vlOutro         = rsNfe.getFloat("vl_outras_despesas");
        	float vlFrete         = rsNfe.getFloat("vl_frete");  // Usado no total de ICMS, usar assim ou são quando tiver produtos que tenham ICMS?
            // Atualiza endereço de destino como o principal caso não exista
            int cdEnderecoDestinario = rsNfe.getInt("cd_endereco_destinatario");
            int cdEstadoDestinatario = 0;
            int cdPaisDestinatario = 0;
            PessoaEndereco endDestinatario = PessoaEnderecoServices.getEnderecoPrincipal(rsNfe.getInt("cd_destinatario"), connect);
            if(cdEnderecoDestinario <= 0) {
            	if(endDestinatario!=null) {
            		connect.prepareStatement(" UPDATE fsc_nota_fiscal " +
            				                 " SET cd_endereco_destinatario = "+endDestinatario.getCdEndereco()+
            				                 " WHERE cd_nota_fiscal         = "+rsNfe.getInt("cd_nota_fiscal")).executeUpdate();
            		cdEnderecoDestinario = endDestinatario.getCdEndereco();
            	}
            	
            }
            
        	Cidade cidadeDest = CidadeDAO.get(endDestinatario.getCdCidade(), connect);
        	Estado estadoDest = EstadoDAO.get(cidadeDest.getCdEstado(), connect);
        	cdEstadoDestinatario = estadoDest.getCdEstado();
        	Pais paisDes = PaisDAO.get(estadoDest.getCdPais(), connect);
        	cdPaisDestinatario = paisDes.getCdPais();
        	
        	
            
            //
            procNFe_v310.TNFe nFe = new procNFe_v310.TNFe();  
            procNFe_v310.TNFe.InfNFe infNFe = new procNFe_v310.TNFe.InfNFe();  
            infNFe.setId(rsNfe.getString("nr_chave_acesso"));  
            infNFe.setVersao("3.10");
            /*
             *  Identificação da NF-e
             */
            procNFe_v310.TNFe.InfNFe.Ide ide = new procNFe_v310.TNFe.InfNFe.Ide();  
            ide.setCUF(NfeServices.getCdUfDefault(connect));
            ide.setCNF((rsNfe.getString("nr_chave_acesso") == null) ? "" : rsNfe.getString("nr_chave_acesso").substring(38, 46)); // Código Numérico que cópia a Chave de Acesso - Verificar intervalo
//            String nmNaturezaOperacao = rsNfe.getString("nm_natureza_operacao");
            ide.setNatOp(rsNfe.getString("nr_cfop"));  
            ide.setIndPag(rsNfe.getString("tp_pagamento"));  
            ide.setMod(rsNfe.getString("tp_modelo"));  
            ide.setSerie(rsNfe.getString("nr_serie"));  
            ide.setNNF(rsNfe.getString("nr_nota_fiscal"));  
            ide.setDhEmi(Util.formatDate(rsNfe.getTimestamp("dt_emissao"), "yyyy-MM-dd'T'HH:mm:ss'-03:00'"));
            ide.setDhSaiEnt(Util.formatDate(rsNfe.getTimestamp("dt_movimentacao"), "yyyy-MM-dd'T'HH:mm:ss'-03:00'"));
//            ide.setHSaiEnt(Util.formatDate(rsNfe.getTimestamp("dt_movimentacao"), "HH:mm:ss"));  
            ide.setTpNF(rsNfe.getString("tp_movimento"));
            if(rsNfe.getInt("cd_pais") != cdPaisDestinatario)
            	ide.setIdDest("3");
            else if(rsNfe.getInt("cd_estado") != cdEstadoDestinatario)
            	ide.setIdDest("2");
            else
            	ide.setIdDest("1");            	
            ide.setIndFinal(rsNfe.getString("lg_consumidor_final"));
            ide.setIndPres(rsNfe.getString("tp_venda_presenca"));
            ide.setCMunFG(rsNfe.getString("id_ibge"));
            ide.setTpImp(rsNfe.getString("tp_danfe"));  
            ide.setTpEmis(rsNfe.getString("tp_emissao"));  
            ide.setCDV((rsNfe.getString("nr_chave_acesso") == null ) ? "" : rsNfe.getString("nr_chave_acesso").substring(46));  
            ide.setTpAmb(String.valueOf(getTipoAmbiente(cdEmpresa)));  
            ide.setFinNFe(rsNfe.getString("tp_finalidade"));
            if(rsNfe.getInt("tp_finalidade") == NotaFiscalServices.NFE_DE_DEVOLUCAO){
            	if(rsNfe.getString("nr_chave_acesso_referencia") != null){
            		procNFe_v310.TNFe.InfNFe.Ide.NFref nfRef = new procNFe_v310.TNFe.InfNFe.Ide.NFref();
        			nfRef.setRefNFe(rsNfe.getString("nr_chave_acesso_referencia").trim());
        			ide.getNFref().add(nfRef);
            	}
            	else{
	            	ResultSetMap rsmEntradas = new ResultSetMap(connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_doc_vinculado WHERE cd_nota_fiscal = " + rsNfe.getInt("cd_nota_fiscal")).executeQuery());
	            	while(rsmEntradas.next()){
	            		DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(rsmEntradas.getInt("cd_documento_entrada"), connect);
	            		if(docEntrada != null){
		            		ResultSetMap rsmNotaFiscalRef = new ResultSetMap(connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_doc_vinculado WHERE cd_documento_saida = " + docEntrada.getCdDocumentoSaidaOrigem()).executeQuery());
		            		if(rsmNotaFiscalRef.next()){
		            			NotaFiscal notaRef = NotaFiscalDAO.get(rsmNotaFiscalRef.getInt("cd_nota_fiscal"), connect);
		            			procNFe_v310.TNFe.InfNFe.Ide.NFref nfRef = new procNFe_v310.TNFe.InfNFe.Ide.NFref();
		            			nfRef.setRefNFe(notaRef.getNrChaveAcesso().substring(3));
		            			ide.getNFref().add(nfRef);
		            		}
	            		}
	            	}
            	}
            }
            ide.setProcEmi("0");     // 0 - emissão de NF-e com aplicativo do contribuinte;
            ide.setVerProc("1.0");   // Versão do aplicativo emissor
            infNFe.setIde(ide);      // Adicionando os dados de Identificação da NF-e
            /*
             *  Dados do Emissor
             */
            procNFe_v310.TNFe.InfNFe.Emit emit = new procNFe_v310.TNFe.InfNFe.Emit();  
            emit.setCNPJ(rsNfe.getString("nr_cnpj_emissor"));//
            emit.setXNome((rsNfe.getString("nm_razao_social") == null) ? "" : rsNfe.getString("nm_razao_social").trim());  
            emit.setXFant((rsNfe.getString("nm_fantasia") == null) ? "" : rsNfe.getString("nm_fantasia").trim());  
            emit.setIE(Util.limparFormatos(rsNfe.getString("nr_inscricao_estadual"), 'N'));// 
            /**
             * Este campo será
			 *	obrigatoriamente preenchido
			 *	com:
			 *	1 é Simples Nacional;
			 *	2 é Simples Nacional é excesso de sublimite de receita bruta;
			 *	3 é Regime Normal. (v2.0).
             * 
             * */
            emit.setCRT(String.valueOf(ParametroServices.getValorOfParametroAsInteger("TP_REGIME_TRIBUTARIO", 0)));        
            // Endereço do emitente
            procNFe_v310.TEnderEmi enderEmit = new procNFe_v310.TEnderEmi();
            if((rsNfe.getString("nm_logradouro") != null && !rsNfe.getString("nm_logradouro").trim().equals("") && !rsNfe.getString("nm_logradouro").trim().equals("null")))
            	enderEmit.setXLgr(rsNfe.getString("nm_logradouro").trim());  
            if((rsNfe.getString("nr_endereco") != null || !rsNfe.getString("nr_endereco").trim().equals("") && !rsNfe.getString("nr_endereco").trim().equals("null")))
            	enderEmit.setNro(rsNfe.getString("nr_endereco").trim());  
            if((rsNfe.getString("nm_bairro") != null || !rsNfe.getString("nm_bairro").trim().equals("") && !rsNfe.getString("nm_bairro").trim().equals("null")))
            	enderEmit.setXBairro(rsNfe.getString("nm_bairro").trim());
            enderEmit.setCMun(rsNfe.getString("id_ibge"));  
            enderEmit.setXMun(rsNfe.getString("nm_cidade"));
            enderEmit.setUF(procNFe_v310.TUfEmi.valueOf(rsNfe.getString("sg_estado")));  
            enderEmit.setCEP(rsNfe.getString("nr_cep"));  
            enderEmit.setCPais("1058");    // Fixo 
            enderEmit.setXPais("Brasil");  // Fixo
            
            String telefone_emitente_1 =rsNfe.getString("nr_telefone1"); 
            String telefone_emitente_2 =rsNfe.getString("nr_telefone2"); 
            String fax_emitente =rsNfe.getString("nr_fax"); 
            String celular_emitente =rsNfe.getString("nr_celular");
            
            String contato_telefone_emitente_1 =rsNfe.getString("contato_nr_telefone1"); 
            String contato_telefone_emitente_2 =rsNfe.getString("contato_nr_telefone2"); 
            String contato_celular_emitente =rsNfe.getString("contato_nr_celular");
            String contato_celular_emitente_2 =rsNfe.getString("contato_nr_celular2");
            String contato_celular_emitente_3 =rsNfe.getString("contato_nr_celular3");
            String contato_celular_emitente_4 =rsNfe.getString("contato_nr_celular4");
            
            if(!isNullOrBlank(telefone_emitente_1)){
            	enderEmit.setFone(Util.limparFormatos(telefone_emitente_1, 'N').trim());  
            } else if(!isNullOrBlank(telefone_emitente_2)){
            	enderEmit.setFone(Util.limparFormatos(telefone_emitente_2, 'N').trim());  
            } else if(!isNullOrBlank(fax_emitente)){
            	enderEmit.setFone(Util.limparFormatos(fax_emitente, 'N').trim());  
            } else if(!isNullOrBlank(celular_emitente)){
            	enderEmit.setFone(Util.limparFormatos(celular_emitente, 'N').trim());
			} else if(!isNullOrBlank(contato_telefone_emitente_1)){
				enderEmit.setFone(Util.limparFormatos(contato_telefone_emitente_1, 'N').trim());  
			} else if(!isNullOrBlank(contato_telefone_emitente_2)){
				enderEmit.setFone(Util.limparFormatos(contato_telefone_emitente_2, 'N').trim());  
			} else if(!isNullOrBlank(contato_celular_emitente)){
				enderEmit.setFone(Util.limparFormatos(contato_celular_emitente, 'N').trim());  
			} else if(!isNullOrBlank(contato_celular_emitente_2)){
				enderEmit.setFone(Util.limparFormatos(contato_celular_emitente_2, 'N').trim());  
			} else if(!isNullOrBlank(contato_celular_emitente_3)){
				enderEmit.setFone(Util.limparFormatos(contato_celular_emitente_3, 'N').trim());  
			} else if(!isNullOrBlank(contato_celular_emitente_4)){
				enderEmit.setFone(Util.limparFormatos(contato_celular_emitente_4, 'N').trim());  
			}  
            emit.setEnderEmit(enderEmit);  
            infNFe.setEmit(emit);          // Adicionando os dados do emissor é nota
            ResultSet rsDest = connect.prepareStatement("SELECT nr_cnpj, nr_cpf, nm_pessoa, nr_inscricao_estadual, A.nm_email, " +
            		                                    "       nm_logradouro, nm_bairro, nm_complemento, nr_endereco, E.nr_cep, A.nr_telefone1, A.nr_telefone2, A.nr_fax, A.nr_celular, " +
            		                                    "      H.nr_telefone1 as contato_nr_telefone1 , H.nr_telefone2 as contato_nr_telefone2, " +
								                        "      H.nr_celular as contato_nr_celular, H.nr_celular2 as contato_nr_celular2, H.nr_celular3 as contato_nr_celular3, " +
														" 	   H.nr_celular4 as contato_nr_celular4, " +
            		                                    "       nm_cidade, id_ibge, sg_estado, nm_razao_social, id_pessoa " +
            		                                    "FROM grl_pessoa A " +
            		                                    "LEFT OUTER JOIN grl_pessoa_fisica    B ON (A.cd_pessoa = B.cd_pessoa) " +
            		                                    "LEFT OUTER JOIN grl_pessoa_juridica  C ON (A.cd_pessoa = C.cd_pessoa) " +
            		                                    "LEFT OUTER JOIN grl_pessoa_endereco  E ON (A.cd_pessoa = E.cd_pessoa  "+
														"                                       AND E.cd_endereco          = "+cdEnderecoDestinario+")"+
            		                                    "LEFT OUTER JOIN grl_pessoa_contato   H ON (H.cd_pessoa = A.cd_pessoa)" +
														"LEFT OUTER JOIN grl_cidade           F ON (E.cd_cidade            = F.cd_cidade) "+
														"LEFT OUTER JOIN grl_estado           G ON (F.cd_estado            = G.cd_estado) "+
            		                                    "WHERE A.cd_pessoa = "+rsNfe.getInt("cd_destinatario")).executeQuery();
            
            // Dados do destinatário
          //Verifica se é uma nota fiscal de importação: Caso o pais do destinatario seja diferente do da empresa, e seja uma nota de entrada
            Pessoa empresa = PessoaDAO.get(rsNfe.getInt("cd_empresa"), connect);
			Pessoa fornecedor = PessoaDAO.get(rsNfe.getInt("cd_destinatario"), connect);
			
			//Busca do pais e estado do emitente
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_pessoa", "" + empresa.getCdPessoa(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmPessoaEnd = PessoaEnderecoDAO.find(criterios, connect);
			int cdEstadoEmi = 0; 
			int cdPaisEmi   = 0;
			if(rsmPessoaEnd.next()){
				int cdCidade = rsmPessoaEnd.getInt("cd_cidade");
				Cidade cidade = CidadeDAO.get(cdCidade, connect);
				Estado estado = EstadoDAO.get(cidade.getCdEstado(), connect);
				cdEstadoEmi = estado.getCdEstado();
			}
			Estado estado = EstadoDAO.get(cdEstadoEmi, connect);
			cdPaisEmi = estado.getCdPais();
			//Busca do pais do fornecedor
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_pessoa", "" + fornecedor.getCdPessoa(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
			rsmPessoaEnd = PessoaEnderecoDAO.find(criterios, connect);
			int cdEstadoForn = 0; 
			int cdPaisForn   = 0;
			if(rsmPessoaEnd.next()){
				int cdCidade = rsmPessoaEnd.getInt("cd_cidade");
				Cidade cidade = CidadeDAO.get(cdCidade, connect);
				estado = EstadoDAO.get(cidade.getCdEstado(), connect);
				cdEstadoForn = estado.getCdEstado();
			}
			estado = EstadoDAO.get(cdEstadoForn, connect);
			cdPaisForn = estado.getCdPais();
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_nota_fiscal", "" + rsNfe.getInt("cd_nota_fiscal"), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmDocVinc = NotaFiscalDocVinculadoDAO.find(criterios);
			boolean isDocEnt = false;
			if(rsmDocVinc.next()){
				isDocEnt = rsmDocVinc.getInt("cd_documento_entrada") != 0;
			}
			rsDest.next();
			
			//Nota Fiscal De Importação
			if(cdPaisEmi != cdPaisForn && isDocEnt){
				 /*
	             *  Dados do destinatário
	             */
	            //
				procNFe_v310.TNFe.InfNFe.Dest dest = new procNFe_v310.TNFe.InfNFe.Dest();
	            dest.setXNome(getTipoAmbiente(cdEmpresa) == PRODUCAO ? ((rsDest.getString("nm_razao_social") != null && !rsDest.getString("nm_razao_social").equals("")) ? rsDest.getString("nm_razao_social").trim() : rsDest.getString("nm_pessoa")) : "NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
//	        	dest.setCPF("");//Deve ser informado vazio
//	            dest.setCNPJ("");//Deve ser informado vazio
	            dest.setIdEstrangeiro(rsDest.getString("id_pessoa"));
	            dest.setIndIEDest("9");
//	           	dest.setIE("");//Deve ser informado vazio
	           	if(rsDest.getString("nm_email") == null  || rsDest.getString("nm_email").trim().equals("")  || rsDest.getString("nm_email").trim().equals("null"))
	           		dest.setEmail(rsNfe.getString("nm_email").trim());
	           	else
	           		dest.setEmail(rsDest.getString("nm_email").trim());
	            /*
	             * ENDEREÇO DO CLIENTE
	             */
	           	procNFe_v310.TEndereco enderDest = new procNFe_v310.TEndereco();    
	           	if(rsDest.getString("nm_logradouro") != null && !rsDest.getString("nm_logradouro").trim().equals("") && !rsDest.getString("nm_logradouro").trim().equals("null"))
	            	enderDest.setXLgr(rsDest.getString("nm_logradouro").trim());  
	            if(rsDest.getString("nr_endereco") != null && !rsDest.getString("nr_endereco").trim().equals("") && !rsDest.getString("nr_endereco").trim().equals("null"))
	            	enderDest.setNro((rsDest.getString("nr_endereco") == null) ? "" : rsDest.getString("nr_endereco").trim());
	            else
	            	enderDest.setNro("S/N");
	            if(rsDest.getString("nm_bairro") != null && !rsDest.getString("nm_bairro").trim().equals("") && !rsDest.getString("nm_bairro").trim().equals("null"))
	            	enderDest.setXBairro((rsDest.getString("nm_bairro") == null) ? "" : rsDest.getString("nm_bairro").trim()); 
	            if(rsDest.getString("nm_complemento") != null && !rsDest.getString("nm_complemento").trim().equals("") && !rsDest.getString("nm_complemento").trim().equals("null"))
	            	enderDest.setXCpl(rsDest.getString("nm_complemento").trim());
	        	enderDest.setXMun("EXTERIOR");
	            enderDest.setCMun("9999999"); 
	            enderDest.setUF(procNFe_v310.TUf.valueOf("EX"));
	            Pais pais = PaisDAO.get(cdPaisForn);
	            enderDest.setCPais(pais.getIdPais());    // Fixo  
	            enderDest.setXPais(pais.getNmPais());  // Fixo
	            
	            String telefone_destinatario_1 =rsDest.getString("nr_telefone1"); 
	            String telefone_destinatario_2 =rsDest.getString("nr_telefone2"); 
	            String fax_destinatario =rsDest.getString("nr_fax"); 
	            String celular_destinatario =rsDest.getString("nr_celular");
	            
	            String contato_telefone_destinatario_1 =rsDest.getString("contato_nr_telefone1"); 
	            String contato_telefone_destinatario_2 =rsDest.getString("contato_nr_telefone2"); 
	            String contato_celular_destinatario =rsDest.getString("contato_nr_celular");
	            String contato_celular_destinatario_2 =rsDest.getString("contato_nr_celular2");
	            String contato_celular_destinatario_3 =rsDest.getString("contato_nr_celular3");
	            String contato_celular_destinatario_4 =rsDest.getString("contato_nr_celular4");
	            
	            if(!isNullOrBlank(telefone_destinatario_1)){
	            	enderDest.setFone(Util.limparFormatos(telefone_destinatario_1, 'N').trim());  
	            } else if(!isNullOrBlank(telefone_destinatario_2)){
	            	enderDest.setFone(Util.limparFormatos(telefone_destinatario_2, 'N').trim());  
	            } else if(!isNullOrBlank(fax_destinatario)){
	            	enderDest.setFone(Util.limparFormatos(fax_destinatario, 'N').trim());  
	            } else if(!isNullOrBlank(celular_destinatario)){
	            	enderDest.setFone(Util.limparFormatos(celular_destinatario, 'N').trim());
				} else if(!isNullOrBlank(contato_telefone_destinatario_1)){
					enderDest.setFone(Util.limparFormatos(contato_telefone_destinatario_1, 'N').trim());  
				} else if(!isNullOrBlank(contato_telefone_destinatario_2)){
					enderDest.setFone(Util.limparFormatos(contato_telefone_destinatario_2, 'N').trim());  
				} else if(!isNullOrBlank(contato_celular_destinatario)){
					enderDest.setFone(Util.limparFormatos(contato_celular_destinatario, 'N').trim());  
				} else if(!isNullOrBlank(contato_celular_destinatario_2)){
					enderDest.setFone(Util.limparFormatos(contato_celular_destinatario_2, 'N').trim());  
				} else if(!isNullOrBlank(contato_celular_destinatario_3)){
					enderDest.setFone(Util.limparFormatos(contato_celular_destinatario_3, 'N').trim());  
				} else if(!isNullOrBlank(contato_celular_destinatario_4)){
					enderDest.setFone(Util.limparFormatos(contato_celular_destinatario_4, 'N').trim());
				}
	            dest.setEnderDest(enderDest);  
	            infNFe.setDest(dest);  
			}
			else{
				 /*
	             *  Dados do destinatário
	             */
				procNFe_v310.TNFe.InfNFe.Dest dest = new procNFe_v310.TNFe.InfNFe.Dest();
	            dest.setCPF(rsDest.getString("nr_cpf"));
	            dest.setCNPJ(rsDest.getString("nr_cnpj"));
	            if(getTipoAmbiente(cdEmpresa) == HOMOLOGACAO)
	            	dest.setXNome("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
	            else
	            	dest.setXNome(((rsDest.getString("nm_razao_social") != null) && !rsDest.getString("nm_razao_social").equals("") ? rsDest.getString("nm_razao_social").trim() : rsDest.getString("nm_pessoa")));
	            dest.setIndIEDest(rsDest.getString("nr_inscricao_estadual") != null && !rsDest.getString("nr_inscricao_estadual").trim().equals("") ? "1": "9");
	            if(rsDest.getString("nr_inscricao_estadual") != null && !rsDest.getString("nr_inscricao_estadual").trim().equals(""))
	            	dest.setIE(Util.limparFormatos(rsDest.getString("nr_inscricao_estadual"), 'N').trim());
	            String email = (rsDest.getString("nm_email") == null  || rsDest.getString("nm_email").trim().equals("")  || rsDest.getString("nm_email").trim().equals("null")) ? rsNfe.getString("nm_email") : rsDest.getString("nm_email");
	            dest.setEmail(email.trim());
	            /*
	             * ENDEREÇO DO CLIENTE
	             */
	            procNFe_v310.TEndereco enderDest = new procNFe_v310.TEndereco();  
	            if(rsDest.getString("nm_logradouro") != null && !rsDest.getString("nm_logradouro").trim().equals("") && !rsDest.getString("nm_logradouro").trim().equals("null"))
	            	enderDest.setXLgr(rsDest.getString("nm_logradouro").trim());  
	            if(rsDest.getString("nr_endereco") != null && !rsDest.getString("nr_endereco").trim().equals("") && !rsDest.getString("nr_endereco").trim().equals("null"))
	            	enderDest.setNro((rsDest.getString("nr_endereco") == null) ? "" : rsDest.getString("nr_endereco").trim());  
	            if(rsDest.getString("nm_bairro") != null && !rsDest.getString("nm_bairro").trim().equals("") && !rsDest.getString("nm_bairro").trim().equals("null"))
	            	enderDest.setXBairro((rsDest.getString("nm_bairro") == null) ? "" : rsDest.getString("nm_bairro").trim());  
	            enderDest.setCMun(rsDest.getString("id_ibge"));//
	            enderDest.setXMun((rsDest.getString("nm_cidade") == null) ? "" : rsDest.getString("nm_cidade").trim()); 
	            enderDest.setUF(procNFe_v310.TUf.valueOf(rsDest.getString("sg_estado")));//
	            enderDest.setCEP(rsDest.getString("nr_cep"));  
	            enderDest.setCPais("1058");    // Fixo  
	            enderDest.setXPais("Brasil");  // Fixo
	            
	            String telefone_destinatario_1 =rsDest.getString("nr_telefone1"); 
	            String telefone_destinatario_2 =rsDest.getString("nr_telefone2"); 
	            String fax_destinatario =rsDest.getString("nr_fax"); 
	            String celular_destinatario =rsDest.getString("nr_celular");
	            
	            String contato_telefone_destinatario_1 =rsDest.getString("contato_nr_telefone1"); 
	            String contato_telefone_destinatario_2 =rsDest.getString("contato_nr_telefone2"); 
	            String contato_celular_destinatario =rsDest.getString("contato_nr_celular");
	            String contato_celular_destinatario_2 =rsDest.getString("contato_nr_celular2");
	            String contato_celular_destinatario_3 =rsDest.getString("contato_nr_celular3");
	            String contato_celular_destinatario_4 =rsDest.getString("contato_nr_celular4");
	            
	            if(!isNullOrBlank(telefone_destinatario_1)){
	            	enderDest.setFone(Util.limparFormatos(telefone_destinatario_1, 'N').trim());  
	            } else if(!isNullOrBlank(telefone_destinatario_2)){
	            	enderDest.setFone(Util.limparFormatos(telefone_destinatario_2, 'N').trim());  
	            } else if(!isNullOrBlank(fax_destinatario)){
	            	enderDest.setFone(Util.limparFormatos(fax_destinatario, 'N').trim());  
	            } else if(!isNullOrBlank(celular_destinatario)){
	            	enderDest.setFone(Util.limparFormatos(celular_destinatario, 'N').trim());
				} else if(!isNullOrBlank(contato_telefone_destinatario_1)){
					enderDest.setFone(Util.limparFormatos(contato_telefone_destinatario_1, 'N').trim());  
				} else if(!isNullOrBlank(contato_telefone_destinatario_2)){
					enderDest.setFone(Util.limparFormatos(contato_telefone_destinatario_2, 'N').trim());  
				} else if(!isNullOrBlank(contato_celular_destinatario)){
					enderDest.setFone(Util.limparFormatos(contato_celular_destinatario, 'N').trim());  
				} else if(!isNullOrBlank(contato_celular_destinatario_2)){
					enderDest.setFone(Util.limparFormatos(contato_celular_destinatario_2, 'N').trim());  
				} else if(!isNullOrBlank(contato_celular_destinatario_3)){
					enderDest.setFone(Util.limparFormatos(contato_celular_destinatario_3, 'N').trim());  
				} else if(!isNullOrBlank(contato_celular_destinatario_4)){
					enderDest.setFone(Util.limparFormatos(contato_celular_destinatario_4, 'N').trim());
				}
	            	
	            dest.setEnderDest(enderDest);  
	            infNFe.setDest(dest);  
			}
			
			
			procNFe_v310.TNFe.InfNFe.AutXML autXml = new procNFe_v310.TNFe.InfNFe.AutXML();
			autXml.setCNPJ("13937073000156");
			infNFe.getAutXML().add(autXml);
			
			/*
             *  Dados dos produtos
             */
			PreparedStatement pstmtNotaItem = connect.prepareStatement("SELECT A.*, B.tp_operacao, " +
																		" 		  B.tp_base_calculo, B.pr_reducao_base, B.tp_motivo_desoneracao, " +
																		"		  B.tp_base_calculo_substituicao, " +
																		"		  B.pr_reducao_base_substituicao, C.cd_situacao_tributaria AS cd_situacao_tributaria_original, C.nr_situacao_tributaria, C.lg_simples, " +
																		"		  C.lg_substituicao, C.lg_retido, D.id_tributo FROM  fsc_nota_fiscal_item_tributo A " +
																		"	JOIN adm_tributo_aliquota B ON (A.cd_tributo = B.cd_tributo  " +
																		"					AND A.cd_tributo_aliquota = B.cd_tributo_aliquota) " +
																		"	JOIN fsc_situacao_tributaria C ON (A.cd_situacao_tributaria = C.cd_situacao_tributaria " +
																		"						AND A.cd_tributo = C.cd_tributo) " +
																		"	JOIN adm_tributo D ON (A.cd_tributo = D.cd_tributo)" +
																		"	WHERE A.cd_nota_fiscal = ? " +
																		"	  AND A.cd_item = ?");
			/*
             *  Dados dos produtos
             */
             ResultSetMap rsProdutos = new ResultSetMap(connect.prepareStatement("SELECT A.*, D.nr_ncm, D.cd_ncm, E.sg_unidade_medida, E.nr_precisao_medida, C.id_reduzido, C.tp_origem, C.qt_precisao_custo, B.nm_produto_servico, B.txt_especificacao, " +
             		                                        "        B.cd_categoria_economica, B.cd_classificacao_fiscal, B.id_produto_servico, B.cd_fabricante, " +
             		                                        "        E.sg_unidade_medida, J.nr_codigo_fiscal AS nr_cfop, BG.cd_grupo " +
										                    "FROM fsc_nota_fiscal_item             A " +
										                    "JOIN grl_produto_servico              B ON (A.cd_produto_servico = B.cd_produto_servico) " +
										                    "LEFT OUTER JOIN alm_produto_grupo               BG ON (A.cd_produto_servico = BG.cd_produto_servico" +
										                    "												AND lg_principal = 1 " +
										                    "												AND BG.cd_empresa = "+cdEmpresa+") " +
										                    "JOIN grl_produto_servico_empresa      C ON (A.cd_produto_servico = C.cd_produto_servico and C.cd_empresa = "+cdEmpresa+") " +
										                    "LEFT OUTER JOIN grl_ncm               D ON (D.cd_ncm = B.cd_ncm) "+
															"LEFT OUTER JOIN grl_unidade_medida    E ON (C.cd_unidade_medida = E.cd_unidade_medida) "+
															"LEFT OUTER JOIN adm_natureza_operacao            J ON (J.cd_natureza_operacao = A.cd_natureza_operacao) " +
															"WHERE A.cd_nota_fiscal = "+rsNfe.getInt("cd_nota_fiscal")).executeQuery());
            
            int nrItem = 1;
            HashMap<String, Integer> produtoAdicao = new HashMap<String, Integer>();
            //Por produto
            while(rsProdutos.next()){
            	produtoAdicao.put(rsProdutos.getString("nr_ncm"), (produtoAdicao.get(rsProdutos.getString("nr_ncm")) != null ? produtoAdicao.get(rsProdutos.getString("nr_ncm")) : 0) + 1);
            	vlDescontos += rsProdutos.getFloat("vl_desconto");
            	vlAcrescimo += rsProdutos.getFloat("vl_acrescimo");
            	
            	procNFe_v310.TNFe.InfNFe.Det det = new procNFe_v310.TNFe.InfNFe.Det();  
                det.setNItem(String.valueOf(nrItem));  
                det.setInfAdProd((rsProdutos.getString("txt_especificacao") != null && !rsProdutos.getString("txt_especificacao").trim().equals("")) ? rsProdutos.getString("txt_especificacao").trim() : "NENHUM DETALHE"); // Dados adicionais - Nao pode ser vazio
                String nrCodigoBarras = rsProdutos.getString("id_produto_servico");
                // Dados do produto  
                procNFe_v310.TNFe.InfNFe.Det.Prod prod = new procNFe_v310.TNFe.InfNFe.Det.Prod();
               
                procNFe_v310.TNFe.InfNFe.Det.Prod.DI di = new procNFe_v310.TNFe.InfNFe.Det.Prod.DI();
                criterios = new ArrayList<ItemComparator>();
    			criterios.add(new ItemComparator("cd_nota_fiscal", "" + rsNfe.getInt("cd_nota_fiscal"), ItemComparator.EQUAL, Types.INTEGER));
    			criterios.add(new ItemComparator("cd_item", "" + rsProdutos.getInt("cd_item"), ItemComparator.EQUAL, Types.INTEGER));
    			ResultSetMap rsmDeclaracao = DeclaracaoImportacaoDAO.find(criterios);
    			while(rsmDeclaracao.next()){
    				di = new procNFe_v310.TNFe.InfNFe.Det.Prod.DI();
    				DeclaracaoImportacao declaracao = DeclaracaoImportacaoDAO.get(rsmDeclaracao.getInt("cd_declaracao_importacao"));
    				estado = EstadoDAO.get(declaracao.getCdEstado());
    				
    				di.setTpViaTransp(String.valueOf(declaracao.getTpViaTransporte()));
    				di.setTpIntermedio(String.valueOf(declaracao.getTpIntermedio()));
    				if(declaracao.getTpIntermedio() != EntradaDeclaracaoImportacaoServices.TP_INTERMEDIO_PROPRIA){
    					di.setCNPJ(declaracao.getNrCnpjIntermediario());
    					di.setUFTerceiro(procNFe_v310.TUfEmi.valueOf(EstadoDAO.get(declaracao.getCdEstadoIntermediario(), connect).getSgEstado()));
    				}
    				if(declaracao.getTpViaTransporte() == EntradaDeclaracaoImportacaoServices.TP_VIA_MARITIMA)
    					di.setVAFRMM(getVAfrmm(rsNfe.getInt("cd_nota_fiscal"), rsProdutos.getInt("cd_item")));
    				di.setCExportador((declaracao.getCdExportador() > 0 ? String.valueOf(declaracao.getCdExportador()) : "9999999"));
    				di.setDDesemb((declaracao.getDtDesembaraco() != null ? Util.formatDate(declaracao.getDtDesembaraco(), "yyyy-MM-dd") : ""));
    				di.setDDI((declaracao.getDtDesembaraco() != null ? Util.formatDate(declaracao.getDtRegistro(), "yyyy-MM-dd") : ""));
    				di.setNDI((declaracao.getNrDeclaracaoImportacao() != null && !declaracao.getNrDeclaracaoImportacao().equals("") ? Util.limparFormatos(declaracao.getNrDeclaracaoImportacao().trim()) : ""));
    				di.setUFDesemb(procNFe_v310.TUfEmi.valueOf(estado.getSgEstado().trim()));
    				di.setXLocDesemb((declaracao.getNrDeclaracaoImportacao() != null && !declaracao.getNrDeclaracaoImportacao().equals("") ? declaracao.getDsLocalDesembaraco().trim() : ""));
    				
    				criterios = new ArrayList<ItemComparator>();
        			criterios.add(new ItemComparator("cd_declaracao_importacao", "" + declaracao.getCdDeclaracaoImportacao(), ItemComparator.EQUAL, Types.INTEGER));
        			ResultSetMap rsmAdicoes = AdicaoDAO.find(criterios, connect);
        			procNFe_v310.TNFe.InfNFe.Det.Prod.DI.Adi adi = new procNFe_v310.TNFe.InfNFe.Det.Prod.DI.Adi();
        			while(rsmAdicoes.next()){
        				//Apenas seráo colocar no XML as adições que tem mesmo NCM que aquele produto
        				if(rsProdutos.getInt("cd_ncm") == rsmAdicoes.getInt("cd_ncm")){
	        				adi = new procNFe_v310.TNFe.InfNFe.Det.Prod.DI.Adi();
	        				Adicao adicao = AdicaoDAO.get(rsmAdicoes.getInt("cd_adicao"));
	        				int cdFabricante = rsProdutos.getInt("cd_fabricante");
	        				if(cdFabricante == 0)
	        					cdFabricante = rsNfe.getInt("cd_empresa");
	        				
	        				adi.setCFabricante(String.valueOf(cdFabricante));
	        				adi.setNAdicao((adicao.getNrAdicao() != 0 ? String.valueOf(adicao.getNrAdicao()) : ""));
	        				adi.setNSeqAdic(String.valueOf(produtoAdicao.get(rsProdutos.getString("nr_ncm"))));
//	        				String vlDesconto = Util.formatNumberSemSimbolos(adicao.getVlDesconto(), 2);
//	        				adi.setVDescDI(vlDesconto);
	        				di.getAdi().add(adi);
	        			}
        			}
        			prod.getDI().add(di);
        			
    				
    			}
    			String cProd = (rsProdutos.getString("id_reduzido") != null && !rsProdutos.getString("id_reduzido").trim().equals("") ? rsProdutos.getString("id_reduzido") : rsProdutos.getString("cd_produto_servico"));
    			prod.setCProd(cProd); 
            	prod.setNCM(rsProdutos.getString("nr_ncm")!=null ? rsProdutos.getString("nr_ncm") : "");
            	prod.setCFOP(rsProdutos.getString("nr_cfop"));
            	if(rsProdutos.getString("id_produto_servico") != null && rsProdutos.getString("id_produto_servico").trim().length() == 13 && NotaFiscalServices.isCean(rsProdutos.getString("id_produto_servico").trim()))
            		prod.setCEAN(nrCodigoBarras);
            	else
            		prod.setCEAN("");
            	prod.setXProd(rsProdutos.getString("nm_produto_servico").trim() + ((rsProdutos.getString("txt_informacao_adicional") != null && !rsProdutos.getString("txt_informacao_adicional").equals("null") && !rsProdutos.getString("txt_informacao_adicional").trim().equals("")) ? " - " + rsProdutos.getString("txt_informacao_adicional") : "" ));
            	String sgUnidadeMedida = rsProdutos.getString("sg_unidade_medida");
            	prod.setUCom(sgUnidadeMedida);
                prod.setUTrib(sgUnidadeMedida!= null ? sgUnidadeMedida.trim() : "");
                //Valor
                prod.setVUnCom(Util.formatNumberSemSimbolos(rsProdutos.getFloat("vl_unitario"), (rsProdutos.getInt("qt_precisao_custo") > 10 ? 10 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo")))));
                prod.setVUnTrib(Util.formatNumberSemSimbolos(rsProdutos.getFloat("vl_unitario"), (rsProdutos.getInt("qt_precisao_custo") > 10 ? 10 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo")))));  
                // Quantidade
                prod.setQCom(Util.formatNumberSemSimbolos(rsProdutos.getFloat("qt_tributario"), (rsProdutos.getInt("qt_precisao_custo") > 4 ? 4 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo"))))); 
                prod.setQTrib(Util.formatNumberSemSimbolos(rsProdutos.getFloat("qt_tributario"), (rsProdutos.getInt("qt_precisao_custo") > 4 ? 4 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo")))));  
               	// Total
                vlTotalProdutos += Double.parseDouble(Util.formatNumberSemSimbolos(
                		(Util.arredondar(rsProdutos.getFloat("vl_unitario"), (rsProdutos.getInt("qt_precisao_custo") > 10 ? 10 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo")))) 
                		* 
                		Util.arredondar(rsProdutos.getFloat("qt_tributario"), (rsProdutos.getInt("nr_precisao_medida") > 4 ? 4 : (rsProdutos.getInt("nr_precisao_medida") <= 0 ? 0 : rsProdutos.getInt("nr_precisao_medida"))))), 
                		(rsProdutos.getInt("qt_precisao_custo") > 2 ? 2 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo")))));
//                vlTotalProdutos += Float.parseFloat(Util.formatNumberSemSimbolos(
//                		(Util.arredondar(rsProdutos.getFloat("vl_unitario"), (rsProdutos.getInt("qt_precisao_custo") > 10 ? 10 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo")))) 
//                		* 
//                		Util.arredondar(rsProdutos.getFloat("qt_tributario"), (rsProdutos.getInt("nr_precisao_medida") > 4 ? 4 : (rsProdutos.getInt("nr_precisao_medida") <= 0 ? 0 : rsProdutos.getInt("nr_precisao_medida"))))), 
//                		2));
            	
                prod.setVProd(Util.formatNumberSemSimbolos(
                		(Util.arredondar(rsProdutos.getFloat("vl_unitario"), (rsProdutos.getInt("qt_precisao_custo") > 10 ? 10 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo")))) 
                		* 
                		Util.arredondar(rsProdutos.getFloat("qt_tributario"), (rsProdutos.getInt("nr_precisao_medida") > 4 ? 4 : (rsProdutos.getInt("nr_precisao_medida") <= 0 ? 0 : rsProdutos.getInt("nr_precisao_medida"))))), 
                		(rsProdutos.getInt("qt_precisao_custo") > 2 ? 2 : (rsProdutos.getInt("qt_precisao_custo") <= 0 ? 2 : rsProdutos.getInt("qt_precisao_custo")))));
                
                //
                if(rsProdutos.getString("id_produto_servico") != null && rsProdutos.getString("id_produto_servico").trim().length() == 13 && NotaFiscalServices.isCean(rsProdutos.getString("id_produto_servico").trim()))
            		prod.setCEANTrib(nrCodigoBarras);
            	else
            		prod.setCEANTrib("");
                prod.setIndTot("1");  // 1 - Produto
                det.setProd(prod);
             
                pstmtNotaItem.setInt(1, rsProdutos.getInt("cd_nota_fiscal"));
                pstmtNotaItem.setInt(2, rsProdutos.getInt("cd_item"));
                ResultSetMap rsmTributos = new ResultSetMap(pstmtNotaItem.executeQuery());
                rsmTributos.beforeFirst();
                rsmTributos = organizarTributos(rsmTributos);
                
                //Usado para nao permitir o programa entrar na area do do-while caso nao haja parametros, mas a tributacao nao seja obrigatoria (colocando-se
                //os tributos padroes)
                boolean hasTributo 	= true;
                //Caso haja ISSQN nao podera haver ICMS, IPI e II, porem nao esta sendo usado pois nao ha empresa que utiliza o sistema e trabalhe com sevico
                boolean hasISSQN 	= false;
                //Usados para confirmar se no produto foi declarado estes 4 impostos que sao obrigatorios, caso nao forem, o sistema tratarão de colocar os padroes
                boolean hasPIS 		= false;
                boolean hasCOFINS 	= false;
                boolean hasIPI 		= false;
                boolean hasICMS 	= false;
                //Usado para declarar o PISST caso o o COFINSST tiver sido incluido, sincronizando os valores de ambos
                boolean hasCOFINSST = false;
                double vlBaseStCofins = 0;
                float prAliquotaStCofins = 0;
                //Usado para declarar o COFINSST caso o o PISST tiver sido incluido, sincronizando os valores de ambos
                boolean hasPISST 	= false;
                double vlBaseStPis = 0;
                float prAliquotaStPis = 0;
                @SuppressWarnings("unused")
                boolean hasICMSIPIII = false;
                procNFe_v310.TNFe.InfNFe.Det.Imposto imposto = new procNFe_v310.TNFe.InfNFe.Det.Imposto();
                if(!rsmTributos.next()){
            	//Parametro que permiti ou bloqueia um produto nao ter uma tributacao cadastrada
//            	if(ParametroServices.getValorOfParametroAsInteger("LG_TRIBUTACAO", -1, cdEmpresa, connect) == 1){
            		hasTributo = false;
//            	}
                }
                if(hasTributo){
                    do{
                    	if(rsmTributos.getRegister() == null)
                    		continue;
                    	
                    	//Caso seja devolucao vai incluir o valor de ICMS e valor de base de calculo
                    	if(rsmTributos.getString("id_tributo").equals("ICMS") && (rsNfe.getInt("cd_natureza_operacao") == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_ENTRADA_IMPORTACAO_DEFAULT", 0))){
                			//Caso seja tambem substituicao tributaria
                			if(rsmTributos.getInt("lg_substituicao") == 1){
                				if(rsmTributos.getFloat("vl_retido") > 0 && rsmTributos.getInt("lg_retido") == 0){
	                				vlBaseCalculoST 	+= Util.arredondar(rsmTributos.getFloat("vl_base_retencao"), 2);
	                				vlICMSST 			+= Util.arredondar(rsmTributos.getFloat("vl_retido"), 2);
                				}
    	                	}
                			else{
                				vlBaseCalculo 	+= Util.arredondar(rsmTributos.getFloat("vl_base_calculo"), 2);
		                		vlICMS 			+= Util.arredondar(rsmTributos.getFloat("vl_tributo"), 2);
                			}
                			
                		}
                		//Vai incluir o valor de icms e base de calculo caso nao seja simples
                		else if(rsmTributos.getString("id_tributo").equals("ICMS") && rsmTributos.getInt("lg_simples") != SituacaoTributariaServices.REG_SIMPLES && rsmTributos.getInt("lg_substituicao") == 0){
                			vlBaseCalculo 	+= Util.arredondar(rsmTributos.getFloat("vl_base_calculo"), 2);
	                		vlICMS 			+= Util.arredondar(rsmTributos.getFloat("vl_tributo"), 2);
	                	}
                		//Vai incluir o valor de icms e base de calculo st caso seja simples porém seja por substituicao tributaria
                		else if(rsmTributos.getString("id_tributo").equals("ICMS") && rsmTributos.getInt("lg_substituicao") == 1 && rsmTributos.getFloat("vl_retido") > 0 && rsmTributos.getInt("lg_retido") == 0){
                			vlBaseCalculoST 	+= Util.arredondar(rsmTributos.getFloat("vl_base_retencao"), 2);
            				vlICMSST 			+= Util.arredondar(rsmTributos.getFloat("vl_retido"), 2);
	                	}
                		if(rsmTributos.getString("id_tributo").equals("PIS") && rsmTributos.getInt("lg_substituicao") != 1){
                			if(!hasICMS){
	                			hasICMS = true;
                            	procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS icms = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS();  
                            	procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 icmssn102 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102();  
            	                icmssn102.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));       
            	                icmssn102.setCSOSN("102");//Tabela fixa  
            	                icms.setICMSSN102(icmssn102);
            	                imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoICMS(icms));; 
                            }
                			if(!hasIPI){
                				hasIPI = true;
                            	procNFe_v310.TIpi ipi = new procNFe_v310.TIpi();
                          		ipi.setClEnq("0");
                          		ipi.setCSelo("0");  // Codigo do selo de controle do IPI
                              	ipi.setQSelo("0");  // Quantidade de selo de controle do IPI
                              	ipi.setCEnq("999"); // Código de enquadramento lega do IPI
                              	
                              	// NT - OK
                              	procNFe_v310.TIpi.IPINT ipiNT = new procNFe_v310.TIpi.IPINT();
                              	ipiNT.setCST("52");
                                ipi.setIPINT(ipiNT);
                                imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoIPI(ipi));;
                          	}
                            
	                		hasPIS = true;
	                		vlPIS 	 += Util.arredondar(rsmTributos.getFloat("vl_tributo"), 2);
	                		vlRetPIS += Util.arredondar(rsmTributos.getFloat("vl_retido"), 2);
	                		procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS pis = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS();
	                		//PISAliq
	                		//01 é operação Tributível (base de cálculo = valor da operação alíquota normal (cumulativo/não cumulativo));
	                		//02 - operação Tributível (base de cálculo = valor da operação (alíquota diferenciada));
	                		if(rsmTributos.getString("nr_situacao_tributaria").equals("01")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("02")){
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISAliq pisaliq = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISAliq();
	                			pisaliq.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			pisaliq.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			pisaliq.setPPIS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			pisaliq.setVPIS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			pis.setPISAliq(pisaliq);
	                		}
	                		//PISQtde
	                		//03 - operação Tributível (base de cálculo = quantidade vendida x alíquota por unidade de produto);
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("03")){
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISQtde pisqtde = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISQtde();
	                			pisqtde.setCST("03");
	                			pisqtde.setQBCProd(rsProdutos.getString("qt_tributario"));
	//                			pisqtde.setVAliqProd(value);//valor da aliquota em reais
	                			pisqtde.setVPIS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			pis.setPISQtde(pisqtde);
	                		}
	                		//PISNT
	                		//04 - operação Tributível (tributação monofísica (alíquota zero));
	                		//06 - operação Tributível (alíquota zero);
	                		//07 - operação Isenta da Contribuição;
	                		//08 - operação Sem Incidência da Contribuição;
	                		//09 - operação com Suspensão da Contribuição;
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("04")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("06")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("07")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("08")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("09")){
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISNT pisnt = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISNT();
	                			pisnt.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			pis.setPISNT(pisnt);
	                		}
	                		//PISOutr
	                		//49 - Outras Operações de Saída;
	                		//50 - operação com Direito a Crédito - Vinculada Exclusivamente a Receita Tributada no Mercado Interno;
	                		//51 - operação com Direito a Crédito - Vinculada Exclusivamente a Receita não Tributada no Mercado Interno;
	                		//52 - operação com Direito a Crédito é Vinculada Exclusivamente a Receita de Exportação;
	                		//53 - operação com Direito a Crédito - Vinculada a Receitas Tributadas e não-Tributadas no Mercado Interno;
	                		//54 - operação com Direito a Crédito - Vinculada a Receitas Tributadas no Mercado Interno e de Exportação;
	                		//55 - operação com Direito a Crédito - Vinculada a Receitas não-Tributadas no Mercado Interno e de Exportação;
	                		//56 - operação com Direito a Crédito - Vinculada a Receitas Tributadas e não-Tributadas no Mercado Interno, e de Exportação;
	                		//60 - Crédito Presumido - operação de Aquisição Vinculada Exclusivamente a Receita Tributada no Mercado Interno;
	                		//61 - Crédito Presumido - operação de Aquisição Vinculada Exclusivamente a Receita não-Tributada no Mercado Interno;
	                		//62 - Crédito Presumido - operação de Aquisição Vinculada Exclusivamente a Receita de Exportação;
	                		//63 - Crédito Presumido - operação de Aquisição Vinculada a Receitas Tributadas e não-Tributadas no Mercado Interno;
	                		//64 - Crédito Presumido - operação de Aquisição Vinculada a Receitas Tributadas no Mercado Interno e de Exportação;
	                		//65 - Crédito Presumido - operação de Aquisição Vinculada a Receitas não-Tributadas no Mercado Interno e de Exportação;
	                		//66 - Crédito Presumido - operação de Aquisição Vinculada a Receitas Tributadas e não-Tributadas no Mercado Interno, e de Exportação;
	                		//67 - Crédito Presumido - Outras Operações;
	                		//70 - operação de Aquisição sem Direito a Crédito;
	                		//71 - operação de Aquisição com Isenção;
	                		//72 - operação de Aquisição com Suspensão;
	                		//73 - operação de Aquisição a Aliquota Zero;
	                		//74 - operação de Aquisição; sem Incidência da Contribuição;
	                		//75 - operação de Aquisição por Substituição Tributária;
	                		//98 - Outras Operações de Entrada;
	                		//99 - Outras Operações;
	                		else {
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISOutr pisoutr = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISOutr();
	                			pisoutr.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			pisoutr.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			pisoutr.setPPIS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			pisoutr.setVPIS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			pis.setPISOutr(pisoutr);
	                		}
	                		imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));
	                	}
	                	//PISST
	                	else if(rsmTributos.getString("id_tributo").equals("PIS") && rsmTributos.getInt("lg_substituicao") == 1){
	                			hasPIS 	 = true;
	                			hasPISST = true;
	                			vlPIS 	+= Util.arredondar(rsmTributos.getFloat("vl_retido"), 2);
	                			vlBaseStPis = Util.arredondar(rsmTributos.getFloat("vl_base_retencao"), 2);
	                			prAliquotaStPis = rsmTributos.getFloat("pr_aliquota_substituicao");
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS pis = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS();  
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISNT pisnt = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISNT();  
	        	                pisnt.setCST("07");
	        	                pis.setPISNT(pisnt); 
	        	                imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));;
	        	                procNFe_v310.TNFe.InfNFe.Det.Imposto.PISST pisst = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PISST();
	                			pisst.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	                			pisst.setPPIS(Util.formatNumberSemSimbolos(prAliquotaStPis, 2));
	                			pisst.setVPIS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                			imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));;
	                		
	                	}
                		//COFINS
	                	if(rsmTributos.getString("id_tributo").equals("COFINS") && rsmTributos.getInt("lg_substituicao") != 1){
	                		if(!hasICMS){
	                			hasICMS = true;
                            	procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS icms = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS();  
                            	procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 icmssn102 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102();  
            	                icmssn102.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));       
            	                icmssn102.setCSOSN("102");//Tabela fixa  
            	                icms.setICMSSN102(icmssn102);
            	                imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoICMS(icms));; 
                            }
                			if(!hasIPI){
                				hasIPI = true;
                            	procNFe_v310.TIpi ipi = new procNFe_v310.TIpi();
                          		ipi.setClEnq("0");
                          		ipi.setCSelo("0");  // Codigo do selo de controle do IPI
                              	ipi.setQSelo("0");  // Quantidade de selo de controle do IPI
                              	ipi.setCEnq("999"); // Código de enquadramento lega do IPI
                              	
                              	// NT - OK
                              	procNFe_v310.TIpi.IPINT ipiNT = new procNFe_v310.TIpi.IPINT();
                              	ipiNT.setCST("52");
                                ipi.setIPINT(ipiNT);
                                imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoIPI(ipi));;
                          	}
	                		if(!hasPIS){
	                			hasPIS = true;
                            	procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS pis = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS();  
                            	procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISNT pisnt = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISNT();  
            	                pisnt.setCST("07");
            	                pis.setPISNT(pisnt); 
            	                imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));;
                            	if(hasCOFINSST){
                            		procNFe_v310.TNFe.InfNFe.Det.Imposto.PISST pisst = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PISST();
                            		pisst.setVBC(Util.formatNumberSemSimbolos(vlBaseStCofins, 2));
                            		pisst.setPPIS(Util.formatNumberSemSimbolos(prAliquotaStCofins, 2));
                            		pisst.setVPIS(Util.formatNumberSemSimbolos((vlBaseStCofins * prAliquotaStCofins), 2));
                            		imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));;
                            	}
                          	}
                            
	                		hasCOFINS = true;
	                		procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS cofins = new procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS();
	                		vlCOFINS 	+= Float.parseFloat(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                		vlRetCOFINS += Util.arredondar(rsmTributos.getFloat("vl_retido"), 2);
	                		//COFINSAliq
	                		//01 é operação Tributível (base de cálculo = valor da operação alíquota normal (cumulativo/não cumulativo));
	                		//02 - operação Tributível (base de cálculo = valor da operação (alíquota diferenciada));
	                		if(rsmTributos.getString("nr_situacao_tributaria").equals("01")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("02")){
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq cofinsaliq = new procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq();
	                			cofinsaliq.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			cofinsaliq.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			cofinsaliq.setPCOFINS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			cofinsaliq.setVCOFINS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			cofins.setCOFINSAliq(cofinsaliq);
	                			
	                		}
	                		//CONFINSQtde
	                		//03 - operação Tributível (base de cálculo = quantidade vendida x alíquota por unidade de produto);
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("03")){
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSQtde cofinsqtde = new procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSQtde();
	                			cofinsqtde.setCST("03");
	                			cofinsqtde.setQBCProd(rsProdutos.getString("qt_tributario"));
	//                			cofinsqtde.setVAliqProd(value);//valor da aliquota em reais
	                			cofinsqtde.setVCOFINS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			cofins.setCOFINSQtde(cofinsqtde);
	                		}
	                		//COFINSNT
	                		//04 - operação Tributível (tributação monofísica (alíquota zero));
	                		//06 - operação Tributível (alíquota zero);
	                		//07 - operação Isenta da Contribuição;
	                		//08 - operação Sem Incidência da Contribuição;
	                		//09 - operação com Suspensão da Contribuição;
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("04")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("06")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("07")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("08")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("09")){
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT cofinsnt = new procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT();
	                			cofinsnt.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			cofins.setCOFINSNT(cofinsnt);
	                		}
	                		//COFINSOutr
	                		//49 - Outras Operações de Saída;
	                		//50 - operação com Direito a Crédito - Vinculada Exclusivamente a Receita Tributada no Mercado Interno;
	                		//51 - operação com Direito a Crédito - Vinculada Exclusivamente a Receita não Tributada no Mercado Interno;
	                		//52 - operação com Direito a Crédito é Vinculada Exclusivamente a Receita de Exportação;
	                		//53 - operação com Direito a Crédito - Vinculada a Receitas Tributadas e não-Tributadas no Mercado Interno;
	                		//54 - operação com Direito a Crédito - Vinculada a Receitas Tributadas no Mercado Interno e de Exportação;
	                		//55 - operação com Direito a Crédito - Vinculada a Receitas não-Tributadas no Mercado Interno e de Exportação;
	                		//56 - operação com Direito a Crédito - Vinculada a Receitas Tributadas e não-Tributadas no Mercado Interno, e de Exportação;
	                		//60 - Crédito Presumido - operação de Aquisição Vinculada Exclusivamente a Receita Tributada no Mercado Interno;
	                		//61 - Crédito Presumido - operação de Aquisição Vinculada Exclusivamente a Receita não-Tributada no Mercado Interno;
	                		//62 - Crédito Presumido - operação de Aquisição Vinculada Exclusivamente a Receita de Exportação;
	                		//63 - Crédito Presumido - operação de Aquisição Vinculada a Receitas Tributadas e não-Tributadas no Mercado Interno;
	                		//64 - Crédito Presumido - operação de Aquisição Vinculada a Receitas Tributadas no Mercado Interno e de Exportação;
	                		//65 - Crédito Presumido - operação de Aquisição Vinculada a Receitas não-Tributadas no Mercado Interno e de Exportação;
	                		//66 - Crédito Presumido - operação de Aquisição Vinculada a Receitas Tributadas e não-Tributadas no Mercado Interno, e de Exportação;
	                		//67 - Crédito Presumido - Outras Operações;
	                		//70 - operação de Aquisição sem Direito a Crédito;
	                		//71 - operação de Aquisição com Isenção;
	                		//72 - operação de Aquisição com Suspensão;
	                		//73 - operação de Aquisição a Aliquota Zero;
	                		//74 - operação de Aquisição; sem Incidência da Contribuição;
	                		//75 - operação de Aquisição por Substituição Tributária;
	                		//98 - Outras Operações de Entrada;
	                		//99 - Outras Operações;
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("49")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("50")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("51")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("52")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("53")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("54")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("55")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("56")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("60")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("61")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("62")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("63")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("64")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("65")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("66")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("67")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("70")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("71")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("72")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("73")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("74")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("75")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("98")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("99")){
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSOutr cofinsoutr = new procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSOutr();
	                			cofinsoutr.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			cofinsoutr.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			cofinsoutr.setPCOFINS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			cofinsoutr.setVCOFINS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			cofins.setCOFINSOutr(cofinsoutr);
	                		}
	                		
	                		imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoCOFINS(cofins));;
	                	}
	                	//COFINSST
	                	else if(rsmTributos.getString("id_tributo").equals("COFINS") && rsmTributos.getInt("lg_substituicao") == 1){
	                		hasCOFINS 	= true;
	                		hasCOFINSST = true;
	                		Util.arredondar(vlCOFINS 	+= rsmTributos.getFloat("vl_retido"), 2);
	                		Util.arredondar(vlBaseStCofins 	   = rsmTributos.getFloat("vl_base_retencao"), 2);
                			prAliquotaStCofins = rsmTributos.getFloat("pr_aliquota_substituicao");
                			procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS cofins = new procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS();  
                			procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT cofinsnt = new procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT();  
                            cofinsnt.setCST("07");
                            cofins.setCOFINSNT(cofinsnt);
                            imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoCOFINS(cofins));;
                            procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINSST cofinsst = new procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINSST();
	            			cofinsst.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	            			cofinsst.setPCOFINS(Util.formatNumberSemSimbolos(prAliquotaStCofins, 2));
	            			cofinsst.setVCOFINS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	            			imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoCOFINSST(cofinsst));;
	            		}
	                	
	//            		Nao usado no momento 31/05/2013
	//                	//Quando o ISSQN é informado, o ICMS, IPI, II não são informados, e vice-versa
	//                	if(rsmTributos.getString("id_tributo") == "ISSQN" && !hasICMSIPIII){
	//                		hasISSQN = true;
	//                		ISSQN issqn = new ISSQN();
	//                		issqn.setVBC(Util.formatNumberSemSimbolos(novaBaseCalculo, 2));
	//                		issqn.setVAliq(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));//Verificar se é em porcentagem ou real
	//                		issqn.setVISSQN(Util.formatNumberSemSimbolos((rsmTributos.getFloat("pr_aliquota") / 100 * novaBaseCalculo), 2));
	//                		issqn.setCMunFG(rsNfe.getString("id_ibge"));
	////                		issqn.setCListServ(value);//Saber se vai usar
	//                		issqn.setCSitTrib(value);//Informar o Código da tributação do ISSQN:
	////                									N é NORMAL;
	////                									R é RETIDA;
	////                									S é SUBSTITUTA;
	////                									I é ISENTA. (v.2.0)
	//                		imposto.setISSQN(issqn);
	//                	}
	                	//ICMS
	                	if(rsmTributos.getString("id_tributo").equals("ICMS") && !hasISSQN){
	                		hasICMS = true;
	                		procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS icms = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS();  
	                		hasICMSIPIII = true;
	                		//Tributado Integralmente
	                		if(rsmTributos.getString("nr_situacao_tributaria").equals("00")){
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS00 icms00 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS00();
	                			icms00.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms00.setCST("00");
	                			icms00.setModBC(rsmTributos.getString("tp_base_calculo"));
	                			icms00.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			icms00.setPICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			icms00.setVICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			icms.setICMS00(icms00);
	                		}
	                		//Tributada e com cobrança do ICMS por substituição tributária
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("10")){
	                			
	                			vlBaseCalculo 	+= Util.arredondar(rsmTributos.getFloat("vl_base_calculo"), 2);
		                		vlICMS 			+= Util.arredondar(rsmTributos.getFloat("vl_tributo"), 2);
	                			
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS10 icms10 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS10();
	                			icms10.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms10.setCST("10");
	                			icms10.setModBC(rsmTributos.getString("tp_base_calculo"));
	                			icms10.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			icms10.setPICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			icms10.setVICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			if(rsmTributos.getInt("lg_substituicao") == 1){
	                				icms10.setModBCST(rsmTributos.getString("tp_base_calculo_substituicao"));
	                				if(rsmTributos.getFloat("pr_aliquota_substituicao") != 0)
	                					icms10.setPMVAST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                				if(rsmTributos.getFloat("pr_reducao_base_substituicao") != 0)
	                					icms10.setPRedBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_reducao_base_substituicao"), 2));
	                				icms10.setVBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	                				icms10.setPICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                				icms10.setVICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                    		}
	                			icms.setICMS10(icms10);
	                		}
	                		//Com redução de base de cálculo
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("20")){
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS20 icms20 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS20();
	                			icms20.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms20.setCST("20");
	                			icms20.setModBC(rsmTributos.getString("tp_base_calculo"));
	                			icms20.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			icms20.setPICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			icms20.setVICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			icms20.setPRedBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_reducao_base_substituicao"), 2));
	                			icms.setICMS20(icms20);
	                		}
	                		//Isenta ou não tributada e com cobrança do ICMS por substituição tributária
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("30")){
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS30 icms30 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS30();
	                			icms30.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms30.setCST("30");
	                			icms30.setModBCST(rsmTributos.getString("tp_base_calculo_substituicao"));
	                			if(rsmTributos.getInt("lg_substituicao") == 1){
	                				if(rsmTributos.getFloat("pr_credito") != 0)
	                					icms30.setPMVAST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));// Percentual da margem de valor Adicionado do ICMS ST
	                				if(rsmTributos.getFloat("pr_reducao_base_substituicao") != 0)
	                					icms30.setPRedBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_reducao_base_substituicao"), 2));
		            				icms30.setVBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
		            				icms30.setPICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
		            				icms30.setVICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                			}
	                			icms.setICMS30(icms30);
	                		}
	                		//40 - Isenta 41 - não tributada 50 - Suspensão
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("40")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("41") 
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("50")){
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS40 icms40 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS40();
	                			icms40.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms40.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			if(rsmTributos.getInt("tp_motivo_desoneracao") == TributoAliquotaServices.DES_UTIL_MOT 
	                					|| rsmTributos.getInt("tp_motivo_desoneracao") == TributoAliquotaServices.DES_SUFRAMA 
	                					|| rsmTributos.getInt("tp_motivo_desoneracao") == TributoAliquotaServices.DES_VEN_ORG_PUB)
	                				icms40.setVICMSDeson(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			icms40.setVICMSDeson(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			icms40.setMotDesICMS(rsmTributos.getString("tp_motivo_desoneracao"));
	                			icms.setICMS40(icms40);
	                			
	                		}
	                		//Diferimento A exigência do preenchimento das informações do ICMS diferido fica a critério de cada UF.
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("51")){
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS51 icms51 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS51();
	                			icms51.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms51.setCST("51");
	                			icms51.setModBC(rsmTributos.getString("tp_base_calculo"));
	                			icms51.setPRedBC(rsmTributos.getString("pr_reducao_base"));
	                			icms51.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			icms51.setPICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			icms51.setVICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			icms.setICMS51(icms51);
	                		}
	                		//ICMS cobrado anteriormente por substituição tributária
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("60")){
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS60 icms60 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS60();
	                			icms60.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms60.setCST("60");
	                			if(rsmTributos.getInt("lg_substituicao") == 1 && rsmTributos.getFloat("vl_retido") > 0){
	                				icms60.setVBCSTRet(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
		                			icms60.setVICMSSTRet(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                			}
	                			else{
	                				icms60.setVBCSTRet("0.00");
	                				icms60.setVICMSSTRet("0.00");
	                			}
	                			icms.setICMS60(icms60);
	                		}
	                		//Com redução de base de cálculo e cobrança do ICMS por substituição tributária
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("70")){
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS70 icms70 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS70();
	                			icms70.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms70.setCST("70");
	                			icms70.setModBC(rsmTributos.getString("tp_base_calculo"));
	                			icms70.setPRedBC(rsmTributos.getString("pr_reducao_base"));
	                			icms70.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			icms70.setPICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			icms70.setVICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			if(rsmTributos.getInt("lg_substituicao") == 1){
	                				icms70.setModBCST(rsmTributos.getString("tp_base_calculo_substituicao"));
	                				if(rsmTributos.getFloat("pr_aliquota_substituicao") != 0)
	                					icms70.setPMVAST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                				if(rsmTributos.getFloat("pr_reducao_base_substituicao") != 0)
	                					icms70.setPRedBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_reducao_base_substituicao"), 2));
	                				icms70.setVBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	                				icms70.setPICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                				icms70.setVICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                    		}
	                			icms.setICMS70(icms70);
	                		}
	                		//Outros
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("90")){
	                			procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS90 icms90 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMS90();
	                			icms90.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                			icms90.setCST("90");
	                			icms90.setModBC(rsmTributos.getString("tp_base_calculo"));
//	                			icms90.setPRedBC(rsmTributos.getString("pr_reducao_base"));
	                			icms90.setPICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			icms90.setVICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			icms90.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			if(rsmTributos.getInt("lg_substituicao") == 1){
	                				icms90.setModBCST(rsmTributos.getString("tp_base_calculo_substituicao"));
	                				if(rsmTributos.getFloat("pr_aliquota_substituicao") != 0)
	                					icms90.setPMVAST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                				if(rsmTributos.getFloat("pr_reducao_base_substituicao") != 0)
	                					icms90.setPRedBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_reducao_base_substituicao"), 2));
	                				icms90.setVBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	                				icms90.setPICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                				icms90.setVICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                    		}
	                			icms.setICMS90(icms90);
	                		}
	                		
//	                		VER COM LÉO QUANDO VAI ENTRAR NESSAS DUAS
//	                		//ICMSPart
//	                		else if(rsmTributos.getString("id_tributo").equals("ICMS") && rsmTributos.getInt("lg_substituicao") == 1 && rsmTributos.getInt("lg_simples") != SituacaoTributariaServices.REG_SIMPLES){
//	                			ICMSST icmsst = new ICMSST();
//	                			vlBaseCalculoST += novaBaseCalculo;
//	                			vlICMSST		+= (rsmTributos.getFloat("pr_aliquota") / 100 * novaBaseCalculo);
//	                			icmsst.setOrig("0");
//	                			icmsst.setCST(rsmTributos.getString("nr_situacao_tributaria"));
//	                			icmsst.setVBCSTRet(Util.formatNumberSemSimbolos(novaBaseCalculo, 2));//Perguntar se o retido vai ser o proprio valor de base de calculo
//	                			icmsst.setVICMSSTRet(Util.formatNumberSemSimbolos((rsmTributos.getFloat("pr_reducao_base_substituicao") / 100 * novaBaseCalculo), 2));//Perguntar se o valor do icms retido eh o mesmo do icms
//	//                			icmsst.setVBCSTDest(value);Perguntar LÉO como eh que se acha o do destino
//	//                			icmsst.setVBCSTDest(value);Perguntar LÉO como eh que se acha o do destino
//	                			icms.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoICMSST(icmsst));; 
//	                		}
//	                		//ICMSST
//	                		else if(rsmTributos.getString("id_tributo").equals("ICMS") && rsmTributos.getInt("lg_substituicao") == 1 && rsmTributos.getInt("lg_simples") != SituacaoTributariaServices.REG_SIMPLES){
//	                			ICMSST icmsst = new ICMSST();
//	                			vlBaseCalculoST += novaBaseCalculo;
//	                			vlICMSST		+= (rsmTributos.getFloat("pr_aliquota") / 100 * novaBaseCalculo);
//	                			icmsst.setOrig("0");
//	                			icmsst.setCST(rsmTributos.getString("nr_situacao_tributaria"));
//	                			icmsst.setVBCSTRet(Util.formatNumberSemSimbolos(novaBaseCalculo, 2));//Perguntar se o retido vai ser o proprio valor de base de calculo
//	                			icmsst.setVICMSSTRet(Util.formatNumberSemSimbolos((rsmTributos.getFloat("pr_reducao_base_substituicao") / 100 * novaBaseCalculo), 2));//Perguntar se o valor do icms retido eh o mesmo do icms
//	//                			icmsst.setVBCSTDest(value);Perguntar LÉO como eh que se acha o do destino
//	//                			icmsst.setVBCSTDest(value);Perguntar LÉO como eh que se acha o do destino
//	                			icms.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoICMSST(icmsst));; 
//	                		}
	                		//ICMS - Simples Nacional 
	                		else if(rsmTributos.getString("id_tributo").equals("ICMS") && rsmTributos.getInt("lg_simples") == SituacaoTributariaServices.REG_SIMPLES){//Simples Nacional
	                			//Tributação do ICMS pelo SIMPLES NACIONAL e CSOSN=101 (v.2.0)
	                			if(rsmTributos.getString("nr_situacao_tributaria").equals("101")){
	                				procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101 icms101 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101();
	                    			icms101.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                    			icms101.setCSOSN("101");
	                    			icms101.setPCredSN(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_credito"), 2));
	                    			icms101.setVCredICMSSN(Util.formatNumberSemSimbolos((rsmTributos.getFloat("pr_credito") / 100 * rsmTributos.getFloat("vl_base_calculo")), 2));
	                    			icms.setICMSSN101(icms101); 
	                    		}
	                			//102- Tributada pelo Simples Nacional sem premissão de crédito.
	                			//103 é Isenção do ICMS no Simples Nacional para faixa de receita bruta.
	                			//300 é Imune.
	                			//400 é não tributada pelo Simples Nacional (v.2.0) (v.2.0)
	                			else if(rsmTributos.getString("nr_situacao_tributaria").equals("102") 
	                			|| rsmTributos.getString("nr_situacao_tributaria").equals("103") 
	                			|| rsmTributos.getString("nr_situacao_tributaria").equals("300") 
	                			|| rsmTributos.getString("nr_situacao_tributaria").equals("400")){
	                				procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 icms102 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102();
	                    			icms102.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                    			icms102.setCSOSN(rsmTributos.getString("nr_situacao_tributaria"));
	                    			icms.setICMSSN102(icms102); 
	                    		}
	                			//201- Tributada pelo Simples Nacional com premissão de crédito. (v.2.0)
	                			else if(rsmTributos.getString("nr_situacao_tributaria").equals("201")){
	                				procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201 icms201 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201();
	                    			icms201.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                    			icms201.setCSOSN("201");
	                    			if(rsmTributos.getInt("lg_substituicao") == 1){
	                    				icms201.setModBCST(rsmTributos.getString("tp_base_calculo_substituicao"));
	                    				if(rsmTributos.getFloat("pr_aliquota_substituicao") != 0)
	                    					icms201.setPMVAST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                    				if(rsmTributos.getFloat("pr_reducao_base_substituicao") != 0)
	                    					icms201.setPRedBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_reducao_base_substituicao"), 2));
	                    				icms201.setVBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	                    				icms201.setPICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                    				icms201.setVICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                        		}
	                    			icms201.setPCredSN(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_credito"), 2));
	                    			icms201.setVCredICMSSN(Util.formatNumberSemSimbolos((rsmTributos.getFloat("pr_credito") / 100 * rsmTributos.getFloat("vl_base_calculo")), 2));
	                    			icms.setICMSSN201(icms201); 
	                    		}
	                			//202 - Tributada pelo Simples Nacional sem premissão de crédito e com cobrança do ICMS por Substituição Tributária
	                			//203- Isenção do ICMS nos Simples Nacional para faixa de receita bruta e com cobrança do ICMS por Substituição Tributária (v.2.0)
	                			else if(rsmTributos.getString("nr_situacao_tributaria").equals("202")
	                			|| rsmTributos.getString("nr_situacao_tributaria").equals("203")){
	                				procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202 icms202 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202();
	                				icms202.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                    			icms202.setCSOSN(rsmTributos.getString("nr_situacao_tributaria"));
	                    			if(rsmTributos.getInt("lg_substituicao") == 1){
	                    				icms202.setModBCST(rsmTributos.getString("tp_base_calculo_substituicao"));
	                    				if(rsmTributos.getFloat("pr_aliquota_substituicao") != 0)
	                    					icms202.setPMVAST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                    				if(rsmTributos.getFloat("pr_reducao_base_substituicao") != 0)
	                    					icms202.setPRedBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_reducao_base_substituicao"), 2));
	                    				icms202.setVBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	                    				icms202.setPICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                    				icms202.setVICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                        		}
	                    			icms.setICMSSN202(icms202); 
	                    		}
	                			//ICMS cobrado anteriormente por substituição tributária (substituído) ou por antecipação
	                			else if(rsmTributos.getString("nr_situacao_tributaria").equals("500")){
	                				procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500 icms500 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500();
	                				icms500.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                    			icms500.setCSOSN("500");
	                    			if(rsmTributos.getInt("lg_substituicao") == 1){
	                    				icms500.setVBCSTRet(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	                        			icms500.setVICMSSTRet(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                        		}
	                    			icms.setICMSSN500(icms500); 
	                    		}
	                			//Tributação pelo ICMS 900 - Outros(v2.0)
	                			else if(rsmTributos.getString("nr_situacao_tributaria").equals("900")){
	                				procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900 icms900 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900();
	                    			icms900.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));
	                    			icms900.setCSOSN("900");
	                    			icms900.setModBC(rsmTributos.getString("tp_base_calculo"));
	                    			if(rsmTributos.getFloat("pr_reducao_base") != 0)
	                    				icms900.setPRedBC(rsmTributos.getString("pr_reducao_base"));
	                    			icms900.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                    			icms900.setPICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                    			icms900.setVICMS(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                    			if(rsmTributos.getInt("lg_substituicao") == 1){
	                    				icms900.setModBCST(rsmTributos.getString("tp_base_calculo_substituicao"));
	                    				if(rsmTributos.getFloat("pr_aliquota_substituicao") != 0)
	                    					icms900.setPMVAST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                    				if(rsmTributos.getFloat("pr_reducao_base_substituicao") != 0)
	                    					icms900.setPRedBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_reducao_base_substituicao"), 2));
	                    				icms900.setVBCST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_retencao"), 2));
	                    				icms900.setPICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota_substituicao"), 2));
	                    				icms900.setVICMSST(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_retido"), 2));
	                        		}
	                    			icms900.setPCredSN(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_credito"), 2));
	                    			icms900.setVCredICMSSN(Util.formatNumberSemSimbolos((rsmTributos.getFloat("pr_credito") / 100 * rsmTributos.getFloat("vl_base_calculo")), 2));
	                    			icms.setICMSSN900(icms900); 
	                    		}
	                			
	                			
	                		}
	                		imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoICMS(icms));;
	
	                	}
	                	//IPI
	                	if(rsmTributos.getString("id_tributo").equals("IPI") && !hasISSQN){
	                		if(!hasICMS){
	                			hasICMS = true;
                            	procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS icms = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS();  
                            	procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 icmssn102 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102();  
            	                icmssn102.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));       
            	                icmssn102.setCSOSN("102");//Tabela fixa  
            	                icms.setICMSSN102(icmssn102);
            	                imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoICMS(icms));; 
                            }
                            
	                		hasIPI = true;
	                		procNFe_v310.TIpi ipi = new procNFe_v310.TIpi();
	                		vlIPI += Util.arredondar(rsmTributos.getFloat("vl_tributo"), 2);
	                		hasICMSIPIII = true;
	                		
	                		int cdGrupoCigarros = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_CIGARROS", 0, cdEmpresa);
	                		int cdGrupoBebidas = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_BEBIDAS", 0, cdEmpresa);
	                		
	                		if(rsProdutos.getInt("cd_grupo") != 0 && (rsProdutos.getInt("cd_grupo") == cdGrupoCigarros
	                				|| rsProdutos.getInt("cd_grupo") == cdGrupoBebidas)){
	                			ipi.setClEnq(rsmTributos.getString("nl_classe"));
	                		}
	                		
//	                		ipi.setClEnq("0");
	                		ipi.setCEnq(rsmTributos.getString("nr_enquadramento")); 
	                  		ipi.setCSelo("0");  // Codigo do selo de controle do IPI
	                      	ipi.setQSelo("0");  // Quantidade de selo de controle do IPI
	                      	ipi.setCEnq("999"); // Código de enquadramento lega do IPI
	                		
	                		
	                		//IPITrib
	                		//00-Entrada com recuperação de crédito
	                		//49-Outras entradas
	                		//50-Saída tributada
	                		//99-Outras saídas
	                		if(rsmTributos.getString("nr_situacao_tributaria").equals("00")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("49")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("50")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("99")){
	                			procNFe_v310.TIpi.IPITrib ipitrib = new procNFe_v310.TIpi.IPITrib();
	                			ipitrib.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			//:TODO Verificar como esses quatro campos são preenchidos corretamente
	                			// http://www.flexdocs.com.br/guianfe/gerarNFe.detalhe.imp.IPI.html
	                			// fonte de pesquisa
	                			ipitrib.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	                			ipitrib.setPIPI(Util.formatNumberSemSimbolos(rsmTributos.getFloat("pr_aliquota"), 2));
	                			//--
	                			ipitrib.setVIPI(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	                			ipi.setIPITrib(ipitrib);
	                		}
	                		//IPINT
	                		//01-Entrada tributada com alíquota zero
	                		//02-Entrada isenta
	                		//03-Entrada não-tributada
	                		//04-Entrada imune
	                		//05-Entrada com suspensão
	                		//51-Saída tributada com alíquota zero
	                		//52-Saída isenta
	                		//53-Saída não-tributada
	                		//54-Saída imune
	                		//55-Saída com suspensão
	                		else if(rsmTributos.getString("nr_situacao_tributaria").equals("01")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("02")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("03")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("04")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("05")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("51")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("52")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("53")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("54")
	                				|| rsmTributos.getString("nr_situacao_tributaria").equals("55")){
	                			procNFe_v310.TIpi.IPINT ipint = new procNFe_v310.TIpi.IPINT();	
	                			ipint.setCST(rsmTributos.getString("nr_situacao_tributaria"));
	                			ipi.setIPINT(ipint);
	                		}
	                		imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoIPI(ipi));;
	                	}
	                	
	                	//Grupo de Imposto de Importacao
	                	if(rsmTributos.getString("id_tributo").equals("II") && !hasISSQN){
	                		if(!hasICMS){
	                			hasICMS = true;
                            	procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS icms = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS();  
                            	procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 icmssn102 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102();  
            	                icmssn102.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));       
            	                icmssn102.setCSOSN("102");//Tabela fixa  
            	                icms.setICMSSN102(icmssn102);
            	                imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoICMS(icms));; 
                            }
                			if(!hasIPI){
                				hasIPI = true;
                            	procNFe_v310.TIpi ipi = new procNFe_v310.TIpi();
                          		ipi.setClEnq("0");
                          		ipi.setCSelo("0");  // Codigo do selo de controle do IPI
                              	ipi.setQSelo("0");  // Quantidade de selo de controle do IPI
                              	ipi.setCEnq("999"); // Código de enquadramento lega do IPI
                              	
                              	// NT - OK
                              	procNFe_v310.TIpi.IPINT ipiNT = new procNFe_v310.TIpi.IPINT();
                              	ipiNT.setCST("52");
                                ipi.setIPINT(ipiNT);
                                imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoIPI(ipi));;
                          	}
	                		if(!hasPIS){
	                			hasPIS = true;
                            	procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS pis = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS();  
                            	procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISNT pisnt = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISNT();  
            	                pisnt.setCST("07");
            	                pis.setPISNT(pisnt); 
            	                imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));;
                            	if(hasCOFINSST){
                            		procNFe_v310.TNFe.InfNFe.Det.Imposto.PISST pisst = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PISST();
                            		pisst.setVBC(Util.formatNumberSemSimbolos(vlBaseStCofins, 2));
                            		pisst.setPPIS(Util.formatNumberSemSimbolos(prAliquotaStCofins, 2));
                            		pisst.setVPIS(Util.formatNumberSemSimbolos((vlBaseStCofins * prAliquotaStCofins), 2));
                            		imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));;
                            	}
                          	}
	                		if(!hasCOFINS){
	                			hasCOFINS = true;
                            	procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS cofins = new procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS();  
                            	procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT cofinsnt = new procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT();  
                                cofinsnt.setCST("07");
                                cofins.setCOFINSNT(cofinsnt);
                                imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoCOFINS(cofins));;
                            	if(hasPISST){
                            		procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINSST cofinsst = new procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINSST();
                            		cofinsst.setVBC(Util.formatNumberSemSimbolos(vlBaseStPis, 2));
                            		cofinsst.setPCOFINS(Util.formatNumberSemSimbolos(prAliquotaStPis, 2));
                            		cofinsst.setVCOFINS(Util.formatNumberSemSimbolos((vlBaseStPis * prAliquotaStPis), 2));
                            		imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoCOFINSST(cofinsst));;
                            	}
                          	}
	                		vlII += Util.arredondar(rsmTributos.getFloat("vl_tributo"), 2);
	                		hasICMSIPIII = true;
	                		procNFe_v310.TNFe.InfNFe.Det.Imposto.II ii = new procNFe_v310.TNFe.InfNFe.Det.Imposto.II();	
	            			ii.setVBC(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_base_calculo"), 2));
	            			ResultSetMap rsmAdicao = AdicaoServices.getAdicaoByNotaFiscalNcm(rsNfe.getInt("cd_nota_fiscal"), rsProdutos.getInt("cd_ncm"));
	            			if(rsmAdicao.next())
	            				ii.setVDespAdu(Util.formatNumberSemSimbolos(rsmAdicao.getFloat("vl_aduaneiro"), 2));
	            			ii.setVII(Util.formatNumberSemSimbolos(rsmTributos.getFloat("vl_tributo"), 2));
	            			ii.setVIOF(Util.formatNumberSemSimbolos(0, 2));
	            			imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoII(ii));;
	                	}
	                	
	                }while(rsmTributos.next());
                    
                    if(!hasICMS){
            			hasICMS = true;
                    	procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS icms = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS();  
                    	procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 icmssn102 = new procNFe_v310.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102();  
    	                icmssn102.setOrig(rsProdutos.getString("tp_origem") == null || rsProdutos.getString("tp_origem").equals("") ? "0" : rsProdutos.getString("tp_origem"));       
    	                icmssn102.setCSOSN("102");//Tabela fixa  
    	                icms.setICMSSN102(icmssn102);
    	                imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoICMS(icms));; 
                    }
        			if(!hasIPI){
        				hasIPI = true;
                    	procNFe_v310.TIpi ipi = new procNFe_v310.TIpi();
                  		ipi.setClEnq("0");
                  		ipi.setCSelo("0");  // Codigo do selo de controle do IPI
                      	ipi.setQSelo("0");  // Quantidade de selo de controle do IPI
                      	ipi.setCEnq("999"); // Código de enquadramento lega do IPI
                      	
                      	// NT - OK
                      	procNFe_v310.TIpi.IPINT ipiNT = new procNFe_v310.TIpi.IPINT();
                      	ipiNT.setCST("52");
                        ipi.setIPINT(ipiNT);
                        imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoIPI(ipi));;
                  	}
            		if(!hasPIS){
            			hasPIS = true;
                    	procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS pis = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS();  
                    	procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISNT pisnt = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PIS.PISNT();  
    	                pisnt.setCST("07");
    	                pis.setPISNT(pisnt); 
    	                imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));;
                    	if(hasCOFINSST){
                    		procNFe_v310.TNFe.InfNFe.Det.Imposto.PISST pisst = new procNFe_v310.TNFe.InfNFe.Det.Imposto.PISST();
                    		pisst.setVBC(Util.formatNumberSemSimbolos(vlBaseStCofins, 2));
                    		pisst.setPPIS(Util.formatNumberSemSimbolos(prAliquotaStCofins, 2));
                    		pisst.setVPIS(Util.formatNumberSemSimbolos((vlBaseStCofins * prAliquotaStCofins), 2));
                    		imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));;
                    	}
                  	}
                    
                    if(!hasCOFINS){
            			hasCOFINS = true;
                    	procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS cofins = new procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS();  
                    	procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT cofinsnt = new procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT();  
                        cofinsnt.setCST("07");
                        cofins.setCOFINSNT(cofinsnt);
                        imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoCOFINS(cofins));;
                    	if(hasPISST){
                    		procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINSST cofinsst = new procNFe_v310.TNFe.InfNFe.Det.Imposto.COFINSST();
                    		cofinsst.setVBC(Util.formatNumberSemSimbolos(vlBaseStPis, 2));
                    		cofinsst.setPCOFINS(Util.formatNumberSemSimbolos(prAliquotaStPis, 2));
                    		cofinsst.setVCOFINS(Util.formatNumberSemSimbolos((vlBaseStPis * prAliquotaStPis), 2));
                    		imposto.getContent().add(new procNFe_v310.ObjectFactory().createTNFeInfNFeDetImpostoCOFINSST(cofinsst));;
                    	}
                  	}
                }
                
                det.setImposto(imposto);  // inclui impostos no detalhe  
                infNFe.getDet().add(det); // inclui detalhes do produto na nota
                //
                nrItem++;
            } 
            /*
             *  Totais da NF-e  
             */
            procNFe_v310.TNFe.InfNFe.Total total = new procNFe_v310.TNFe.InfNFe.Total();  
            // Total do ICMS
            procNFe_v310.TNFe.InfNFe.Total.ICMSTot icmstot = new procNFe_v310.TNFe.InfNFe.Total.ICMSTot();  
            /**
             * (+) vProd (Somatório do valor de todos os produtos da NF-e);
				(-) vDesc (Somatório do desconto de todos os produtos da NF-e);
				(+) vST (Somatório do valor do ICMS com Substituição Tributária de todos os produtos da NF-e);
				(+) vFrete (Somatório do valor do Frete de todos os produtos da NF-e);
				(+) vSeg (Somatório do valor do seguro de todos os produtos da NF-e);
				(+) vOutro (Somatório do valor de outras despesas de todos os produtos da NF-e);
				(+) vII (Somatório do valor do Imposto de Importação de todos os produtos da NF-e);
				(+) vIPI (Somatório do valor do IPI de todos os produtos da NF-e);
				(+) vServ (Somatório do valor do Serviço de todos os itens da NF-e).
             */
            if(Integer.parseInt(rsNfe.getString("nr_cfop")) == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_SUBSTITUTO", 0) ||
     		   Integer.parseInt(rsNfe.getString("nr_cfop")) == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_OUTRO_ESTADO", 0) ||
 			   Integer.parseInt(rsNfe.getString("nr_cfop")) == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_SUBSTITUTO_OUTRO_ESTADO", 0) ||
 			   Integer.parseInt(rsNfe.getString("nr_cfop")) == ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR", 0)){
      				
             	
             	int cdTributoIcms = TributoServices.getCdTributoById("ICMS", connect);
             	
             	NotaFiscalTributo notaFiscalTributo = NotaFiscalTributoDAO.get(rsNfe.getInt("cd_nota_fiscal"), cdTributoIcms, connect);
             	
             	if(notaFiscalTributo != null){
 	            	icmstot.setVBC(Util.formatNumberSemSimbolos(notaFiscalTributo.getVlBaseCalculo(), 2));
 	                icmstot.setVICMS(Util.formatNumberSemSimbolos(notaFiscalTributo.getVlTributo(), 2));  
 	                icmstot.setVICMSDeson(Util.formatNumberSemSimbolos(vlICMSDeson, 2)); 
 	                icmstot.setVBCST(Util.formatNumberSemSimbolos(notaFiscalTributo.getVlBaseRetencao(), 2));  
 	                icmstot.setVST(Util.formatNumberSemSimbolos(notaFiscalTributo.getVlRetido(), 2));
 	                vlICMSST = notaFiscalTributo.getVlRetido();
             	}
             	else{
             		icmstot.setVBC(Util.formatNumberSemSimbolos(0, 2));
 	                icmstot.setVICMS(Util.formatNumberSemSimbolos(0, 2));  
 	                icmstot.setVICMSDeson(Util.formatNumberSemSimbolos(vlICMSDeson, 2)); 
 	                icmstot.setVBCST(Util.formatNumberSemSimbolos(0, 2));  
 	                icmstot.setVST(Util.formatNumberSemSimbolos(0, 2));
             	}
             }
             else{
             	icmstot.setVBC(Util.formatNumberSemSimbolos(vlBaseCalculo, 2));
                 icmstot.setVICMS(Util.formatNumberSemSimbolos(vlICMS, 2));  
                 icmstot.setVICMSDeson(Util.formatNumberSemSimbolos(vlICMSDeson, 2)); 
                 icmstot.setVBCST(Util.formatNumberSemSimbolos(vlBaseCalculoST, 2));  
                 icmstot.setVST(Util.formatNumberSemSimbolos(vlICMSST, 2));  
             }
            icmstot.setVProd(Util.formatNumberSemSimbolos(vlTotalProdutos, 2));  
            icmstot.setVFrete(Util.formatNumberSemSimbolos(vlFrete, 2));  
            icmstot.setVSeg(Util.formatNumberSemSimbolos(vlSeguros, 2));  
            icmstot.setVDesc(Util.formatNumberSemSimbolos(((vlDescontos <= 0) ? 0 : vlDescontos), 2));  
            icmstot.setVII(Util.formatNumberSemSimbolos(vlII, 2));  
            icmstot.setVIPI(Util.formatNumberSemSimbolos(vlIPI, 2)); 
            icmstot.setVPIS(Util.formatNumberSemSimbolos(vlPIS, 2));  
            icmstot.setVCOFINS(Util.formatNumberSemSimbolos(vlCOFINS, 2));
            icmstot.setVOutro(Util.formatNumberSemSimbolos(vlOutro, 2));
            double valorTotalNota = vlTotalProdutos - vlDescontos + vlICMSST + vlFrete + vlSeguros + vlOutro + vlII + vlIPI;
            
	        if(cdPaisEmi != cdPaisForn && isDocEnt){
	        	valorTotalNota += vlICMS + vlPIS + vlCOFINS;
	        }
			icmstot.setVNF(Util.formatNumberSemSimbolos(valorTotalNota, 2)); 
            total.setICMSTot(icmstot);
            
            //Grupo de retencao de tributos
            if(vlRetCOFINS != 0 || vlRetPIS != 0){
            	procNFe_v310.TNFe.InfNFe.Total.RetTrib rettrib = new procNFe_v310.TNFe.InfNFe.Total.RetTrib();
	            if(vlRetCOFINS != 0)
	            	rettrib.setVRetCOFINS(Util.formatNumberSemSimbolos(vlRetCOFINS, 2));
	            if(vlRetPIS != 0)
	            	rettrib.setVRetPIS(Util.formatNumberSemSimbolos(vlRetPIS, 2));
	            total.setRetTrib(rettrib);
            }
            
            
            infNFe.setTotal(total);
            
            procNFe_v310.TNFe.InfNFe.Transp transp = new procNFe_v310.TNFe.InfNFe.Transp();
        	
            transp.setModFrete(rsNfe.getString("tp_modalidade_frete"));
            
            //Dados de Volumes
            procNFe_v310.TNFe.InfNFe.Transp.Vol vol = new procNFe_v310.TNFe.InfNFe.Transp.Vol();
            
            boolean entrou = false;
            
        	if(rsNfe.getString("ds_numeracao") != null && !rsNfe.getString("ds_numeracao").equals("") && !rsNfe.getString("ds_numeracao").equals("null")){
        		vol.setNVol(rsNfe.getString("ds_numeracao"));
        		entrou = true;
        	}
        	if(rsNfe.getString("qt_volume") != null && !rsNfe.getString("qt_volume").equals("") && !rsNfe.getString("qt_volume").equals("null")){
        		vol.setQVol(Util.formatNumberSemSimbolos(Float.parseFloat(rsNfe.getString("qt_volume")), 0));
	        	entrou = true;
	    	}
            if(rsNfe.getString("ds_especie") != null && !rsNfe.getString("ds_especie").equals("") && !rsNfe.getString("ds_especie").equals("null")){
            	vol.setEsp(rsNfe.getString("ds_especie"));
            	entrou = true;
        	}
            if(rsNfe.getString("ds_marca") != null && !rsNfe.getString("ds_marca").equals("") && !rsNfe.getString("ds_marca").equals("null")){
            	vol.setMarca(rsNfe.getString("ds_marca"));
            	entrou = true;
        	}
            if(rsNfe.getString("vl_peso_liquido") != null && !rsNfe.getString("vl_peso_liquido").equals("") && !rsNfe.getString("vl_peso_liquido").equals("null") && rsNfe.getFloat("vl_peso_liquido") != 0){
            	vol.setPesoL(Util.formatNumberSemSimbolos(rsNfe.getFloat("vl_peso_liquido"), 3));  
            	entrou = true;
        	}
            if(rsNfe.getString("vl_peso_bruto") != null && !rsNfe.getString("vl_peso_bruto").equals("") && !rsNfe.getString("vl_peso_bruto").equals("null") && rsNfe.getFloat("vl_peso_bruto") != 0){
            	vol.setPesoB(Util.formatNumberSemSimbolos(rsNfe.getFloat("vl_peso_bruto"),3));
            	entrou = true;
        	}
            if(entrou)
            	transp.getVol().add(vol);
            //Dados do Veiculo
            if(rsNfe.getString("nr_placa") != null && !rsNfe.getString("nr_placa").equals("") && !rsNfe.getString("nr_placa").equals("null")
            && rsNfe.getString("sg_uf_veiculo") != null && !rsNfe.getString("sg_uf_veiculo").equals("") && !rsNfe.getString("sg_uf_veiculo").equals("null")){
	            procNFe_v310.TVeiculo veiculo = new procNFe_v310.TVeiculo();
	            veiculo.setPlaca(rsNfe.getString("nr_placa"));
	            veiculo.setUF(procNFe_v310.TUf.valueOf(rsNfe.getString("sg_uf_veiculo").toUpperCase()));
	            if(rsNfe.getString("nr_rntc") != null && !rsNfe.getString("nr_rntc").equals("") && !rsNfe.getString("nr_rntc").equals("null"))
	            	veiculo.setRNTC(rsNfe.getString("nr_rntc"));
	            transp.setVeicTransp(veiculo);
            }
            /*
             *  Dados do Transporte
             */
            ResultSet rsTransp = connect.prepareStatement("SELECT nr_cnpj, nr_cpf, nm_pessoa, gn_pessoa, nr_inscricao_estadual, nm_email, " +
										                  "       nm_logradouro, nm_bairro, nm_complemento, nr_endereco, nr_telefone1, " +
										                  "       nm_cidade, id_ibge, sg_estado " +
										                  "FROM grl_pessoa A " +
										                  "LEFT OUTER JOIN grl_pessoa_fisica    B ON (A.cd_pessoa = B.cd_pessoa) " +
										                  "LEFT OUTER JOIN grl_pessoa_juridica  C ON (A.cd_pessoa = C.cd_pessoa) " +
										                  "LEFT OUTER JOIN grl_pessoa_endereco  E ON (A.cd_pessoa = E.cd_pessoa)"+
														  "LEFT OUTER JOIN grl_cidade           F ON (E.cd_cidade            = F.cd_cidade) "+
														  "LEFT OUTER JOIN grl_estado           G ON (F.cd_estado            = G.cd_estado) "+
										                  "WHERE A.cd_pessoa = "+rsNfe.getInt("cd_transportador")).executeQuery();
            if(rsTransp.next()){
            	// Dados da Transportadora. 
            	procNFe_v310.TNFe.InfNFe.Transp.Transporta transporta = new procNFe_v310.TNFe.InfNFe.Transp.Transporta();
	            if(rsTransp.getInt("gn_pessoa") == com.tivic.manager.grl.PessoaServices.TP_JURIDICA)
                	transporta.setCNPJ(rsTransp.getString("nr_cnpj"));
                if(rsTransp.getInt("gn_pessoa") == com.tivic.manager.grl.PessoaServices.TP_FISICA)
                	transporta.setCPF(rsTransp.getString("nr_cpf"));
                // nome da transportadora não pode ultrapassar 39 caracteres para não desconfigurar o DANFE
	            String nomeTransportadora = rsTransp.getString("nm_pessoa");
				transporta.setXNome(
						nomeTransportadora != null ? 
								(nomeTransportadora.length() > 39 ? nomeTransportadora.trim().substring(0, 39) : nomeTransportadora.trim()) 
						: "");//  "JavaC - Java Communuty"
				if(rsTransp.getString("nr_inscricao_estadual") != null && !rsTransp.getString("nr_inscricao_estadual").trim().equals(""))
					transporta.setIE(Util.limparFormatos(rsTransp.getString("nr_inscricao_estadual"), 'N').trim());
	            // Endereço
	            String enderTran =((rsTransp.getString("nm_logradouro") != null) ? rsTransp.getString("nm_logradouro") : "") + " " + ((rsTransp.getString("nm_complemento") != null) ? rsTransp.getString("nm_complemento").trim() : "");
	            if(!enderTran.trim().equals(""))
	            	transporta.setXEnder(enderTran.trim());//
	            transporta.setXMun(rsTransp.getString("nm_cidade")!=null? rsTransp.getString("nm_cidade").trim() : "");
	            transporta.setUF(procNFe_v310.TUf.valueOf(rsTransp.getString("sg_estado")));
	            
	            transp.setTransporta(transporta);
	        }
            infNFe.setTransp(transp);
            
            // Informações Adicionais
            procNFe_v310.TNFe.InfNFe.InfAdic infAdic = new procNFe_v310.TNFe.InfNFe.InfAdic();
            infAdic.setInfAdFisco(rsNfe.getString("txt_informacao_fisco"));
            // Documentos Vinculados
            String txtObservacao = rsNfe.getString("txt_observacao")!=null ? rsNfe.getString("txt_observacao") : "";
            int qtDocs = 0, qtLimite = txtObservacao.equals("") ? 10 : 7;
            if(txtObservacao.indexOf("CUPOM")<0 && txtObservacao.indexOf("DAV")<0) {
            	txtObservacao += (txtObservacao.equals("") || qtDocs==qtLimite? "" : "");
                ResultSetMap rsmDocVinculados = DocumentoSaidaServices.findCompletoByNfe(rsNfe.getInt("cd_nota_fiscal"));
	            while(rsmDocVinculados.next()) {
	            	if(rsmDocVinculados.getInt("tp_documento_saida")==DocumentoSaidaServices.TP_CUPOM_FISCAL) {
	            		qtDocs++;
	            		txtObservacao += (qtDocs==qtLimite ? "|" : ""); 
	            		txtObservacao += (txtObservacao.equals("") || qtDocs==qtLimite? "" : "")+"Cupom Fiscal - Modelo 2B, N. ECF: "+rsmDocVinculados.getString("nr_serie_ecf")+
	            		                                                         ", COO: "+rsmDocVinculados.getString("nr_documento_saida");
	            	}
	            	else if(rsmDocVinculados.getInt("tp_documento_saida")==DocumentoSaidaServices.TP_CUPOM_FISCAL) {
	            		qtDocs++;
	            		txtObservacao += (qtDocs==qtLimite ? "|" : ""); 
	            		txtObservacao += (txtObservacao.equals("") || qtDocs==qtLimite? "" : "")+"DAV No: "+rsmDocVinculados.getString("nr_documento_saida");
	            	}
	            }
            }
            NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);
            if(nota.getTpEmissao() == NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC){
            	txtObservacao += " DANFE impresso em contingência - DPEC regularmente recebida pela Receita Federal do Brasil.";
            }
            
            if(!txtObservacao.trim().equals(""))
            	infAdic.setInfCpl(txtObservacao.trim());
           
            
            //Acrescentar valor do IPBT
            PreparedStatement pstm = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_doc_vinculado A "
            		+ " LEFT JOIN alm_documento_saida B ON (B.cd_documento_saida = A.cd_documento_saida)"
            		+ " WHERE A.cd_nota_fiscal = ?"
            		+ "   AND A.cd_documento_saida IS NOT NULL "
            		+ "   AND A.cd_documento_saida <> 0 "
            		+ "   AND B.cd_empresa     = ?");
            pstm.setInt(1, cdNotaFiscal);
            pstm.setInt(2, cdEmpresa);
            ResultSetMap rsmDocSaida = new ResultSetMap(pstm.executeQuery());
            
            int cdDocumentoSaida = 0;
            float vlTotalDocumento = 0;
            while(rsmDocSaida.next()){
	            cdDocumentoSaida = rsmDocSaida.getInt("CD_DOCUMENTO_SAIDA");
				vlTotalDocumento = rsmDocSaida.getFloat("VL_TOTAL_DOCUMENTO");
				if(vlTotalDocumento == 0)
					vlTotalDocumento = DocumentoSaidaServices.getValorDocumento(cdDocumentoSaida, connect);
            }
            if(cdDocumentoSaida > 0 && vlTotalDocumento > 0 && rsNfe.getString("tp_movimento").equals("1")){
            	infAdic.setInfAdFisco(infAdic.getInfAdFisco() + SaidaItemAliquotaServices.getAliquotaOfDocSaida(cdDocumentoSaida, cdEmpresa, valorTotalNota));
            }
            
            if(infAdic.getInfAdFisco() == null || infAdic.getInfAdFisco().equals("")){
            	infAdic.setInfAdFisco("SEM INFORMAÇÕES DE FISCO");
            }
            
            infNFe.setInfAdic(infAdic);  
            nFe.setInfNFe(infNFe);
            
            procNFe_v310.TProtNFe nfeProt         = new procNFe_v310.TProtNFe();
            procNFe_v310.TProtNFe.InfProt infProt = new procNFe_v310.TProtNFe.InfProt();
            if(retornoSefaz != null){
            	if(retornoSefaz[0] != null)
            		infProt.setTpAmb(retornoSefaz[0]);
            	if(retornoSefaz[1] != null)
            		infProt.setVerAplic(retornoSefaz[1]);
	            infProt.setChNFe(rsNfe.getString("nr_chave_acesso").substring(3));
	            if(retornoSefaz[2] != null){
		            infProt.setDhRecbto(retornoSefaz[2]);
	            }
	            if(retornoSefaz[5] != null)
	            	infProt.setDigVal(retornoSefaz[5].getBytes());
	            if(retornoSefaz[4] != null)
	            	infProt.setNProt(retornoSefaz[4]);
	            if(retornoSefaz[6] != null)
	            	infProt.setCStat(retornoSefaz[6]);
	            if(retornoSefaz[7] != null)
	            	infProt.setXMotivo(retornoSefaz[7]);
	            if(retornoSefaz[8] != null)
	            	infProt.setId(retornoSefaz[8]);
            }
            nfeProt.setInfProt(infProt);
            nfeProt.setVersao("3.10");
            procNFe_v310.TNfeProc nfeP = new procNFe_v310.TNfeProc();
            nfeP.setNFe(nFe);
            nfeP.setProtNFe(nfeProt);
            JAXBContext context   = JAXBContext.newInstance(procNFe_v310.TNfeProc.class);  
            Marshaller marshaller = context.createMarshaller();  
            JAXBElement<procNFe_v310.TNfeProc> element = new procNFe_v310.ObjectFactory().createNfeProc(nfeP);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);  
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);  
            StringWriter sw = new StringWriter();  
            marshaller.marshal(element, sw);  
            /*
             * XML
             */
            String xml = sw.toString(); 
            xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + xml;
            xml = xml.replaceAll("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\" ", "");  
            xml = xml.replaceAll("<NFe>", "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
            xml = xml.replaceAll("<nfeProc xmlns=\"http://www.portalfiscal.inf.br/nfe\">", "<nfeProc xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"3.10\">");
            xml = removeAcentos(xml);
            //Assina o xml com protoloco, por enquanto apenas para envio
            Result assinResultado = null;
            if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3){
                assinResultado = assinarNFeA3(xml, NfeServices.ENVIO, cdEmpresa);
                if(assinResultado.getCode() <= 0)
                	return "ERRO";
    		}
    		else{
    			assinResultado = assinarNFeA1(xml, NfeServices.ENVIO, cdEmpresa);
                if(assinResultado.getCode() <= 0)
                	return "ERRO";
    		}
            xml = (String) assinResultado.getObjects().get("xmlAssinado");
            
            return xml;
        } 
        catch (Exception e) {  
        	e.printStackTrace();
            error(e.toString());
            if(isConnectionNull)
            	Conexao.rollback(connect);
            
            return "Erro xmlProtocolo 1: " + e;
        }  
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Testa se uma string é null ou vazia ou se tem a string 'null'
	 * 
	 * @param value
	 * @return
	 */
	private static boolean isNullOrBlank(String value){
		return value == null || "".equalsIgnoreCase(value.trim()) || "null".equalsIgnoreCase(value.trim());
	}

	/**
	 * 
	 * @param cdNotaFiscal
	 * @return
	 * @see #getDadosPagamentoFromNfe(int, Connection)
	 */
	public static String getDadosPagamentoFromNfe(int cdNotaFiscal) {
		return getDadosPagamentoFromNfe(cdNotaFiscal, null);
	}
	
	/**
	 * Busca as contas a receber a partir de um documento de saída para preencher o parametro FATURA_DUPLICATA no DANFE
	 * Preenche o valor da conta e a data, agrupado pela forma de pagamento
	 *  
	 * @param cdNotaFiscal
	 * @param connect
	 * @return 
	 * @author Luiz Romario Filho
	 * @since 25/09/2014
	 */
	public static String getDadosPagamentoFromNfe(int cdNotaFiscal, Connection connect) {
		/*
		 * Adiciona forma/faturamento do documento de saída
		 */
		boolean isConnectionNull = connect == null;
		if(isConnectionNull)
			connect = Conexao.conectar();
		ResultSet rsFormaPagamento;
		try {
			rsFormaPagamento = connect.prepareStatement("SELECT DISTINCT ON (C.cd_conta_receber) C.cd_conta_receber, (C.vl_conta - vl_abatimento) AS vl_conta, "
														+ " N.pr_desconto, C.dt_vencimento_original, G.nm_tipo_documento "
														+ " FROM fsc_nota_fiscal N "
														+ " JOIN fsc_nota_fiscal_doc_vinculado A ON A.cd_nota_fiscal = N.cd_nota_fiscal "
														+ " JOIN alm_documento_saida B ON B.cd_documento_saida = A.cd_documento_saida "
														+ " JOIN adm_conta_receber C ON C.cd_documento_saida = B.cd_documento_saida "
														+ " JOIN adm_forma_pagamento D ON D.cd_forma_pagamento = C.cd_forma_pagamento "
														+ " JOIN adm_plano_pagamento E ON E.cd_plano_pagamento = C.cd_plano_pagamento"
														+ " JOIN adm_forma_pagamento_empresa F ON F.cd_forma_pagamento = D.cd_forma_pagamento " 
														+ " JOIN adm_tipo_documento G ON G.cd_tipo_documento = F.cd_tipo_documento "
														+ " WHERE A.cd_nota_fiscal = " + cdNotaFiscal 
														+ " GROUP BY C.cd_conta_receber, N.pr_desconto, C.dt_vencimento_original, G.nm_tipo_documento "
														+ " ORDER BY C.cd_conta_receber, nm_tipo_documento ").executeQuery();

			/*
			 * Formata o valor da conta a receber, a data e a forma de pagamento
			 * Cada forma de pagamento recebe os valores e as datas respectivas das parcelas
			 * Aplica desconto em cima do valor dos itens
			 */
			String result = "";
			Double valorConta;
			Double desconto;
			String dataVencimentoOriginal = "";
			String formaPagamento = "";
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			List<String> list = new ArrayList<String>();
			while (rsFormaPagamento.next()) {
				valorConta = rsFormaPagamento.getDouble("vl_conta");
				desconto = rsFormaPagamento.getDouble("pr_desconto");
				dataVencimentoOriginal = new SimpleDateFormat("dd/MM/yyyy").format(rsFormaPagamento.getDate("dt_vencimento_original"));
				formaPagamento = (rsFormaPagamento.getString("nm_tipo_documento") == null || "".equalsIgnoreCase(rsFormaPagamento.getString("nm_tipo_documento").trim())) ? "Dinheiro": rsFormaPagamento.getString("nm_tipo_documento");

				Locale currentLocale = Locale.getDefault();
			    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
				
				if (map.get(formaPagamento) == null)
					list = new ArrayList<String>();
				if(desconto > 0){
					valorConta = valorConta - (valorConta * (desconto / 100));
				}
				list.add(currencyFormatter.format(valorConta) + " -- " + dataVencimentoOriginal);

				map.put(formaPagamento, list);
				
				list = map.get(formaPagamento);
			}
			
			/*
			 * Concatena no retorno do método
			 * forma de pagamento : valor -- data 
			 */
			Iterator<Entry<String, List<String>>> it = map.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String, List<String>> pairs = it.next();
		        result += pairs.getKey() + " : ";
		        for (String string : pairs.getValue()) {
					result += string + "   ";
				}
		    }
		    
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			error(e.toString());
			if (isConnectionNull)
				Conexao.rollback(connect);

			return "Erro xmlProtocolo 1: " + e;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * 
	 * @param cdNotaFiscal
	 * @return
	 * @see #isCartao(int, Connection)
	 * 
	 */
	public static boolean isCartao(int cdNotaFiscal) {
		return isCartao(cdNotaFiscal, null);
	}
	
	/**
	 * Testa se o faturamento de uma nota foi feito com cartão
	 * 
	 * @param cdNotaFiscal
	 * @param connect
	 * @return
	 * @author Luiz Romario Filho
	 * @since 30/10/2014
	 */
	public static boolean isCartao(int cdNotaFiscal, Connection connect) {
		/*
		 * Adiciona forma/faturamento do documento de saída
		 */
		boolean isConnectionNull = connect == null;
		ResultSet rsFormaPagamento;
		try {
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			rsFormaPagamento = connect.prepareStatement(" SELECT F.cd_tipo_documento "
														+ " FROM fsc_nota_fiscal_doc_vinculado A "
														+ " JOIN alm_documento_saida B ON B.cd_documento_saida = A.cd_documento_saida " 
														+ "	JOIN adm_conta_receber C ON C.cd_documento_saida = B.cd_documento_saida " 
														+ " JOIN adm_forma_pagamento D ON D.cd_forma_pagamento = C.cd_forma_pagamento " 
														+ "	JOIN adm_plano_pagamento E ON E.cd_plano_pagamento = C.cd_plano_pagamento "
														+ " JOIN adm_forma_pagamento_empresa F ON F.cd_forma_pagamento = D.cd_forma_pagamento " 
														+ "	WHERE A.cd_nota_fiscal = " + cdNotaFiscal).executeQuery();
			// Busca os valores do parametro tipo cartão
			ResultSetMap rsTipoDocumentoCartao = ParametroServices.getValoresOfParametro("CD_TIPO_DOCUMENTO_CARTAO");
			List<Integer> tipos = new ArrayList<>();
			rsTipoDocumentoCartao.beforeFirst();
			while(rsTipoDocumentoCartao.next()){
				tipos.add(Integer.parseInt(rsTipoDocumentoCartao.getString("VL_REAL")));
			}
			//Busca a forma de pagamento da nota
			while(rsFormaPagamento.next()){
				int cdTipoDocumento = rsFormaPagamento.getInt("CD_TIPO_DOCUMENTO");
				// Retorna se no array de Códigos de cartões possui o Código utilizado na nota  
				return tipos.contains(cdTipoDocumento);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			error(e.toString());
			if (isConnectionNull)
				Conexao.rollback(connect);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		return false;
	}
	
	
    /* ************************************************************************************************************************************************************
     * Usado para remover os acentos do XML, para que os caracteres passem na validação
     * ************************************************************************************************************************************************************/	
	public static String removeAcentos(String str) {  
	    CharSequence cs = new StringBuilder(str == null ? "" : str);  
	    return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");  
	} 
	
	/**
	 * Metodo para validar uma Nota Fiscal Eletrônica
	 * @param cdNotaFiscal
	 * @param cdEmpresa
	 * @return
	 */
	public static Result validarNFe(int cdNotaFiscal, int cdEmpresa){
    	try{
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("cdNotaFiscal", cdNotaFiscal);
			register.put("cdEmpresa", cdEmpresa);	
			
			ArrayList<HashMap<String, Object>> criterios = new ArrayList<HashMap<String, Object>>();
			criterios.add(register);
			return validarNFe(criterios);							
    	}catch(Exception e){    		    	
    		Util.printInFile("C:/log.log", e.toString());
    		return null;
    	}
		
    }	
	
	/**
	 * Metodo de validação da nota fiscal eletronica, usado pelo WEB
	 * @param registros
	 * @return
	 */
	public static Result validarNFe(ArrayList<HashMap<String,Object>> registros)	{
		try	 {
			int qtErro = 0, qtSucesso = 0;
			Result result     = new Result(1, "Notas Validadas com sucesso!");
			ResultSetMap rsm  = new ResultSetMap();
			for(int i = 0; i < registros.size(); i++){
				int cdNotaFiscal = registros.get(i)==null || registros.get(i).get("cdNotaFiscal")==null ? 0 : (Integer) registros.get(i).get("cdNotaFiscal");
				int cdEmpresa    = registros.get(i)==null || registros.get(i).get("cdEmpresa")==null ? 0 : (Integer) registros.get(i).get("cdEmpresa");
				/*
				 * Validando nota fiscal
				 */
				
				Connection connect = null;
				Result retorno   = new Result(-1, "Erro ao atualizar nota");
				//Variavel usada para retornar o numero da nota para nulo caso o parametro de geração de numero na autorização esteja ativo
				NotaFiscal nota  = NotaFiscalDAO.get(cdNotaFiscal);
				
				//Tratamento usado apenas dentro do for pois cada nota passada irá ser tratada como uma operação atomica
				try{
					connect = Conexao.conectar();
					connect.setAutoCommit(false);
					NotaFiscal notaFiscal  = NotaFiscalDAO.get(cdNotaFiscal, connect);
					
					//Coloca o numero da nota fiscal que é a proxima da serie levando em consideracao autorizadas e canceladas e denegadas
					if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa)==1){
						String nrNotaFiscal = NotaFiscalServices.getNrNotaFiscalAtual(notaFiscal.getNrSerie(), connect);
						notaFiscal.setNrNotaFiscal(nrNotaFiscal);
						//Atualizacao da chave e do DV também pois eles dependem do numero da nota
		    			Result resultId = NotaFiscalServices.gerarNfeId(notaFiscal);
		    			notaFiscal.setNrChaveAcesso((String)resultId.getObjects().get("nrChave"));
		    			notaFiscal.setNrDv((Integer)resultId.getObjects().get("nrDv"));
						NotaFiscalDAO.update(notaFiscal, connect);
					}
					retorno = validarNFe(cdNotaFiscal, NfeServices.ENVIO, cdEmpresa, connect);
					if(retorno.getCode() <= 0){   
						qtErro++; 
						//Caso tenha algum erro na validação, a numeração não é contada pelo sistema
						if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa)==1){
							notaFiscal.setNrNotaFiscal(null);
							//Atualizacao da chave e do DV também pois eles dependem do numero da nota
			    			notaFiscal.setNrChaveAcesso(null);
			    			notaFiscal.setNrDv(0);
							NotaFiscalDAO.update(notaFiscal, connect);
						}
					}
					else { 
						qtSucesso++;
					}
					
					connect.commit();
				}
				catch(Exception e){
					if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa)==1){
						NotaFiscal notaFiscal  = NotaFiscalDAO.get(cdNotaFiscal);
						notaFiscal.setNrNotaFiscal(null);
						notaFiscal.setNrChaveAcesso(null);
						notaFiscal.setNrDv(0);
						NotaFiscalDAO.update(notaFiscal);
					}
					if(connect != null)
						Conexao.rollback(connect);
				}
				finally{
					if(connect != null)
						Conexao.desconectar(connect);
				}
				/*
				 * Registrando o LOG
				 */
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NUMERO", nota.getNrNotaFiscal());
				register.put("RESULTADO", retorno.getMessage());
				rsm.addRegister(register);
			}
			
			if((qtErro+qtSucesso) == 0)
				return new Result(-1, "Nenhuma nota foi enviada!");
			
			result.addObject("resultado", rsm);
			if(qtSucesso==0)
				result.setMessage("Nenhuma nota foi validada! [Erros: "+qtErro+"]");
			else if(qtErro==0)
				result.setMessage("Todas as notas foram validadas com sucesso!");
			else
				result.setMessage(qtSucesso+" nota(s) validada(s) com sucesso! "+qtErro+" nota(s) não foi(ram) validada(s)!");
			
			if(result.getCode()<=0){
				return new Result(-1, "Erro ao validar nfe!");
			}
			return result;
		}
		catch(Exception e) {			
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar validar NFE!", e);
		}
		
	}
	
	/**
	 * Metodo para validacao de NFe, usado pelo Nota Rapida
	 * @param cdNotaFiscal
	 * @param etapa
	 * @param cdEmpresa
	 * @return
	 */
	public static Result validarNFe(int cdNotaFiscal, int etapa, int cdEmpresa){
		return validarNFe(cdNotaFiscal, etapa, cdEmpresa, null, null);
	}
	
	public static Result validarNFe(int cdNotaFiscal, int etapa, int cdEmpresa, Connection connect){
		return validarNFe(cdNotaFiscal, etapa, cdEmpresa, null, connect);
	}
	
	public static Result validarNFe(int cdNotaFiscal, int etapa, int cdEmpresa, String txtMotivo){
		return validarNFe(cdNotaFiscal, etapa, cdEmpresa, txtMotivo, null);
	}
	
	public static Result validarNFe(int cdNotaFiscal, int etapa, int cdEmpresa, String txtMotivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {  
            /** 
             * XSD usado para Validar o XML. 
             * Use o XSD adequado para cada XML a ser validado: 
             * enviNFe_v2.00.xsd (Envio do Lote) 
             * inutNFe_v2.00.xsd (Inutilização de Número da NF-e) 
             * cancNFe_v2.00.xsd (Cancelamento da NF-e) 
             * ... 
             */  
			
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			//O xsd é o arquivo que faz a validação do xml, contém as regras para cada campo
            String xsd = null;
            String xml = null;
            String xmlSemAssinar  = null;
            Result assinResultado = null;
            Result resultado      = null;            
    		switch(etapa){
    			//Gera o xml para cancelamento
            	case NfeServices.CANCELAMENTO:
            		resultado     = gerarXmlNFECanc(cdNotaFiscal, txtMotivo, connect);
            		xmlSemAssinar = (String) resultado.getObjects().get("xml");
            		if(xmlSemAssinar == null){
            			if (isConnectionNull)
            				Conexao.rollback(connect);
            			return resultado;
            		}
            			
            		//Caso o certificado usado nos parametros for do tipo A3
            		if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3){
	                    assinResultado = assinarNFeA3(xmlSemAssinar, etapa, cdEmpresa);
	                    if(assinResultado.getCode() <= 0){
	                    	if (isConnectionNull)	
	                    		Conexao.rollback(connect);
	            			return assinResultado;
	            		}
            		}
            		else{
            			assinResultado = assinarNFeA1(xmlSemAssinar, etapa, cdEmpresa);
	                    if(assinResultado.getCode() <= 0){
	                    	if (isConnectionNull)
	                    		Conexao.rollback(connect);
	            			return assinResultado;
	            		}
            		}
            			
                    xml = (String) assinResultado.getObjects().get("xmlAssinado");
                    xsd = ParametroServices.getValorOfParametro("NM_FILE_XSD_CANCELAMENTO", "c:/TIVIC/xsd/envEventoCancNFe_v1.00.xsd", cdEmpresa);
            		break;
            	//Gera o xml para consulta de cadastro	
            	case NfeServices.CONSULTA_CADASTRO:
            		resultado     = gerarXmlNFE(cdNotaFiscal, cdEmpresa);
            		xmlSemAssinar = (String) resultado.getObjects().get("xml");
            		if(xmlSemAssinar == null){
            			if (isConnectionNull)
            				Conexao.rollback(connect);
            			return resultado;
            		}
            		//Caso o certificado usado nos parametros for do tipo A3
            		if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3){
	                    assinResultado = assinarNFeA3(xmlSemAssinar, etapa, cdEmpresa);
	                    if(assinResultado.getCode() <= 0){
	                    	if (isConnectionNull)	
	                    		Conexao.rollback(connect);
	            			return assinResultado;
	            		}
            		}
            		else{
            			assinResultado = assinarNFeA1(xmlSemAssinar, etapa, cdEmpresa);
	                    if(assinResultado.getCode() <= 0){
	                    	if (isConnectionNull)
	                    		Conexao.rollback(connect);
	            			
	            			return assinResultado;
	            		}
            		}
                    xml = (String) assinResultado.getObjects().get("xmlAssinado");
            		xsd = ParametroServices.getValorOfParametro("NM_FILE_XSD_CONSULTA", "c:/TIVIC/xsd/consCad_v2.00.xsd", cdEmpresa);  
            		break;
            	//Faz o XML para envio e autorização da nota	
            	case NfeServices.ENVIO:
            		NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal, connect);
            		
            		//Realiza teste para saber se esse cliente possui cadastro de parametro, e se possui autorização para compra
    				ResultSetMap rsmParametroValor = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_parametro_valor where cd_pessoa = " + nota.getCdDestinatario()).executeQuery());
    				PreparedStatement pstmtParametro = connect.prepareStatement("SELECT * FROM grl_parametro WHERE cd_parametro = ?");
    				if(rsmParametroValor.next()){
    					do{
    						pstmtParametro.setInt(1, rsmParametroValor.getInt("cd_parametro"));
    						ResultSetMap rsmParametroMap = new ResultSetMap(pstmtParametro.executeQuery());
    						if(rsmParametroMap.next()){    							
    							if(rsmParametroMap.getString("nm_parametro").equals("lgAutorizadoCompra") && rsmParametroValor.getString("vl_inicial").equals("0")){
    								if(isConnectionNull)
    									Conexao.rollback(connect);
    								return new Result(-1, "Cliente não possui autorização para compras!");
    							}
    							else{
    								resultado     = gerarXmlNFE(cdNotaFiscal, cdEmpresa, connect);
    		                		xmlSemAssinar = (String) resultado.getObjects().get("xml");
    		                		System.out.println("xmlSemAssinar = " + xmlSemAssinar);
    		                		if(xmlSemAssinar == null){
    		                			if (isConnectionNull)
    		                				Conexao.rollback(connect);
    		                			return resultado;
    		                		}
    		                		
    		                		if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3){
    		    	                    assinResultado = assinarNFeA3(xmlSemAssinar, etapa, cdEmpresa);    		    	                    
    		    	                    if(assinResultado.getCode() <= 0){
    		    	                    	if (isConnectionNull)
    		    	                    		Conexao.rollback(connect);
    		    	            			return assinResultado;
    		    	            		}
    		                		}
    		                		else{
    		                			assinResultado = assinarNFeA1(xmlSemAssinar, etapa, cdEmpresa);
    		    	                    if(assinResultado.getCode() <= 0){
    		    	                    	if (isConnectionNull)	
    		    	                    		Conexao.rollback(connect);
    		    	            			return assinResultado;
    		    	            		}
    		                		}
    		                        xml = (String) assinResultado.getObjects().get("xmlAssinado");
    		                        xsd = ParametroServices.getValorOfParametro("NM_FILE_XSD_ENVIO", "c:/TIVIC/xsd31/enviNFe_v3.10.xsd", cdEmpresa);  
    							}
    						}
    					}while(rsmParametroValor.next());
    				}
    				//Caso não possua nenhum parametro, entra é gerado o xml para envio de autorização
    				else{
    					resultado     = gerarXmlNFE(cdNotaFiscal, cdEmpresa, connect);
                		xmlSemAssinar = (String) resultado.getObjects().get("xml");                		
                		if(xmlSemAssinar == null){
                			if (isConnectionNull)
                				Conexao.rollback(connect);
                			return resultado;
                		}                		
                		if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3){                			
    	                    assinResultado = assinarNFeA3(xmlSemAssinar, etapa, cdEmpresa);
    	                    if(assinResultado.getCode() <= 0){
    	                    	if (isConnectionNull)
    	                    		Conexao.rollback(connect);
    	            			return assinResultado;
    	            		}
                		}
                		else{
                			assinResultado = assinarNFeA1(xmlSemAssinar, etapa, cdEmpresa);
    	                    if(assinResultado.getCode() <= 0){
    	                    	if (isConnectionNull)	
    	                    		Conexao.rollback(connect);
    	            			return assinResultado;
    	            		}
                		}
                        xml = (String) assinResultado.getObjects().get("xmlAssinado");
                        xsd = ParametroServices.getValorOfParametro("NM_FILE_XSD_ENVIO", "c:/TIVIC/xsd31/enviNFe_v3.10.xsd", cdEmpresa);  
    				}
            		
            		
            		break;
            	//Validação para gerar inutilização
            	case NfeServices.INUTILIZACAO:
            		if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3){
	                    assinResultado = assinarNFeA3(xmlSemAssinar, etapa, cdEmpresa);
	                    if(assinResultado.getCode() <= 0){
	                    	if (isConnectionNull)
	                    		Conexao.rollback(connect);
	            			return assinResultado;
	            		}
            		}
            		else{
            			assinResultado = assinarNFeA1(xmlSemAssinar, etapa, cdEmpresa);
	                    if(assinResultado.getCode() <= 0){
	                    	if (isConnectionNull)
	                    		Conexao.rollback(connect);
	            			return assinResultado;
	            		}
            		}
                    xml = (String) assinResultado.getObjects().get("xmlAssinado");
            		xsd = ParametroServices.getValorOfParametro("NM_FILE_XSD_INUTILIZACAO", "c:/TIVIC/xsd/inutNFe_v2.00.xsd", cdEmpresa);    
            		break;
            	//Gera o xml para envio via DPEC, a nota não é assinada aqui, pois a assinatura é feita em outro momento	
            	case NfeServices.EPEC:
            		resultado     = gerarXmlDpec(cdNotaFiscal, cdEmpresa, connect);            		
            		xml           = (String) resultado.getObjects().get("xml");
                    xsd = ParametroServices.getValorOfParametro("NM_FILE_XSD_ENVIO_DPEC", "c:/TIVIC/xsd/envDPEC_v1.01.xsd", cdEmpresa);  
            		break;
            }
            /** 
             * Caminho do Arquivo XML a ser validado. 
             */
    		//Verifica se houve erros na hora da validação
    		System.out.println("xml de validacao = " + xml);
    		List<String> errosValidacao = validateXml(normalizeXML(xml.toString()), xsd);
            listaComErrosDeValidacao = new ArrayList<String>();
            if(errosValidacao == null){
            	if (isConnectionNull)
            		Conexao.rollback(connect);
            	return new Result(-1, "Erro!");
            }
            if (errosValidacao.size() > 0) {  
            	String mensagensErro = "";
            	for (String msgError : errosValidacao) {  
//                    info("| Erro XML: " + msgError); 
            		System.out.println(msgError);
            		mensagensErro += msgError + " - ";
                }  
            	if (isConnectionNull)
            		Conexao.rollback(connect);
                return  new Result(-1, mensagensErro);
            }  
            else {  
//                info("| OK: XML Validado com Sucesso!");  
            	
            	//Muda o estado no banco de dados
            	if(etapa != NfeServices.CANCELAMENTO && etapa != NfeServices.EPEC){
	            	NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal, connect);
	            	nota.setStNotaFiscal(NotaFiscalServices.VALIDADA);
	            	NotaFiscalDAO.update(nota, connect);
            	}
            	Result resultado2 = new Result(1, "OK: Nota Fiscal validada com Sucesso!");
            	resultado2.addObject("xmlAssinado", xml);
            	
            	if (isConnectionNull)
    				connect.commit();
            	
            	return  resultado2;
            }  
        } 
		catch (Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
        	e.printStackTrace(System.out);  
            return new Result(-1, "Erro!");
        }  
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
	 * Corrige algumas caracteristicas do xml original
	 * @param xml
	 * @return
	 */
	public static String normalizeXML(String xml) {  
        if ((xml != null) && (!"".equals(xml))) {  
            xml = xml.replaceAll("\\r\\n", "");  
            xml = xml.replaceAll(" standalone=\"no\"", "");  
        }  
        return xml;  
    }  
  
	/**
	 * Faz a validação do XML de acordo com o seu XSD
	 * @param xml
	 * @param xsd
	 * @return
	 */
    public static List<String> validateXml(String xml, String xsd) {  
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        factory.setNamespaceAware(true);  
        factory.setValidating(true);  
        factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");  
        factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", xsd);  
        DocumentBuilder builder = null;  
        try {  
            builder = factory.newDocumentBuilder();
            
            builder.setErrorHandler(new ErrorHandler() {@Override
														public void warning(SAXParseException exception) {
															System.out.println("WARNING: " + exception.getMessage());
															listaComErrosDeValidacao.add(tratamentoRetorno(exception.getMessage()));  
																
														}
															
														@Override
														public void fatalError(SAXParseException exception) {
															System.out.println("FATAL ERROR: " + exception.getMessage());
															listaComErrosDeValidacao.add(tratamentoRetorno(exception.getMessage()));  
														}
															
														@Override
														public void error(SAXParseException exception) {
															if (isError(exception)) {  
																System.out.println("ERROR: " + exception.getMessage());
																listaComErrosDeValidacao.add(tratamentoRetorno(exception.getMessage()));
														    }
																
														} });  
        } 
        catch (ParserConfigurationException ex) {  
            error("| validateXml():"); 
            System.out.println("Erro ParserConfigurationException: " + ex.getMessage());
            return null;  
        } 
        catch (Exception ex) {  
            error("| validateXml():"); 
            System.out.println("Erro Exception: " + ex.getMessage());
            return null;  
        }   
        org.w3c.dom.Document document;  
        try {  
        	document = builder.parse(new ByteArrayInputStream(xml.getBytes()));  
            org.w3c.dom.Node rootNode  = document.getFirstChild();  
            info("| Validando Node: " + rootNode.getNodeName());  
            return getListaComErrosDeValidacao();  
        } 
        catch (Exception ex) {  
            error("| validateXml():"); 
            System.out.println("Erro Exception: " + ex.getMessage());
            return null;  
        }  
    }  
    
    /**
     * Retira coisas da mensagem para tornar a mensagem de retorno mais clara
     * @param message
     * @return
     */
    private static String tratamentoRetorno(String message) {  
        message = message.replaceAll("cvc-type.3.1.3:", "");  
        message = message.replaceAll("cvc-complex-type.2.4.a:", "");  
        message = message.replaceAll("cvc-complex-type.2.4.b:", "");  
        message = message.replaceAll("The value", "O valor");  
        message = message.replaceAll("of element", "do campo");  
        message = message.replaceAll("is not valid", "não é valido");  
        message = message.replaceAll("Invalid content was found starting with element", "Encontrado o campo");  
        message = message.replaceAll("One of", "Campo(s)");  
        message = message.replaceAll("is expected", "é obrigatorio");  
        message = message.replaceAll("\\{", "");  
        message = message.replaceAll("\\}", "");  
        message = message.replaceAll("\"", ""); 
        message = message.replaceAll("schema_reference.", "");  
        message = message.replaceAll("http://www.portalfiscal.inf.br/nfe:", "");  
        return message.trim();  
    }  
  	
    public static ArrayList<String> getListaComErrosDeValidacao() {  
        return listaComErrosDeValidacao;  
    }  
    
    private static boolean isError(SAXParseException exception) {  
        if (exception.getMessage().startsWith("cvc-pattern-valid") ||  
            exception.getMessage().startsWith("cvc-maxLength-valid") ||  
            exception.getMessage().startsWith("cvc-datatype")) {  
            return false;  
        }  
        return true;  
    } 
    
/********************************************************************************************************************************************************************/      
	
    
    /**
     * Metodo para assinar uma NFe qualquer, com certificado A1
     * OBS.: Tive de adicionar varios casts para que os metodo funcionassem    
     * */
	public static Result assinarNFeA1(String xml, int etapa, int cdEmpresa){
		
		try{
			String xmlAssinado = null;
			String caminhoDoCertificadoDoCliente = ParametroServices.getValorOfParametro("NM_FILE_CERTIFICADO", cdEmpresa);
            switch(etapa){
			
				case NfeServices.ENVIO:
					Result envioResultado = assinaEnviNFe(  
		                    xml, caminhoDoCertificadoDoCliente, cdEmpresa); 
					if(envioResultado.getCode() <= 0)
			            	return envioResultado;
					xmlAssinado = (String) envioResultado.getObjects().get("xmlAssinado"); 
					break;
	
				case NfeServices.CANCELAMENTO:
					
					Result cancResultado = assinaCancNFe(  
		                    xml, caminhoDoCertificadoDoCliente, cdEmpresa, etapa);  
					if(cancResultado.getCode() <= 0){
		            	return cancResultado;
		            }
					xmlAssinado =(String) cancResultado.getObjects().get("xmlAssinado");  
					break;
					
				case NfeServices.CANCELAMENTO_PROC:	
					Result cancResultadoProc = assinaCancNFe(  
		                    xml, caminhoDoCertificadoDoCliente, cdEmpresa, etapa);  
					if(cancResultadoProc.getCode() <= 0){
		            	return cancResultadoProc;
		            }
					xmlAssinado =(String) cancResultadoProc.getObjects().get("xmlAssinado");  
					break;
					
					
				case NfeServices.INUTILIZACAO:
					
					Result inutResultado = assinaInutNFe(  
		                    xml, caminhoDoCertificadoDoCliente, cdEmpresa);  
					if(inutResultado.getCode() <= 0){
		            	return inutResultado;
		            } 
					xmlAssinado = (String) inutResultado.getObjects().get("xmlAssinado");  
					break;
					
				case NfeServices.EPEC:
					Result dpecResultado = assinaEnvDPEC(  
		                    xml, caminhoDoCertificadoDoCliente, cdEmpresa); 
					if(dpecResultado.getCode() <= 0)
			            	return dpecResultado;
					xmlAssinado = (String) dpecResultado.getObjects().get("xmlAssinado"); 
					break;
		
	
				default:
					
					return null;
					
			
			}
		
			Result resultado = new Result (1, "Assinado com sucesso!");
			resultado.addObject("xmlAssinado", xmlAssinado);
			
			return resultado;
		}
		
		catch(Exception e) {
			System.out.println("Erro ao assinar: " + e);
    		e.printStackTrace(System.out);
			return new Result(-1, "Erro ao assinar: " + e);
		}
	}
	
	
	
	/** 
     * Assinatura do XML de Envio de Lote da NF-e utilizando Certificado 
     * Digital A1. 
     * @param xml 
     * @param certificado 
     * @return 
     * @throws Exception 
     */  
    public static Result assinaEnviNFe(String xml, String certificado, int cdEmpresa)  
            throws Exception {  
        Document document = documentFactory(xml);  
        XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");  
        ArrayList<Transform> transformList = CertificadoServices.signatureFactory(signatureFactory);  
        CertificadoServices.loadCertificates(certificado, signatureFactory, cdEmpresa);  
  
        for (int i = 0; i < ((org.w3c.dom.Document) document).getDocumentElement().getElementsByTagName(NFE).getLength(); i++) {
        	assinarNFe(signatureFactory, transformList, CertificadoServices.privateKey, CertificadoServices.keyInfo, document, i);  
        }  
  
        Result resultado = new Result(1, "Assinado com sucesso!");
        resultado.addObject("xmlAssinado", outputXML(document));
        
        return resultado; 
    }  
  
    /** 
     * Assintaruda do XML de Cancelamento da NF-e utilizando Certificado 
     * Digital A1. 
     * @param xml 
     * @param certificado 
     * @return 
     * @throws Exception 
     */  
    public static Result assinaCancNFe(String xml, String certificado, int cdEmpresa, int etapa) throws Exception {
    	String raiz = (etapa == CANCELAMENTO) ? INFEVENTO : INFCANC;
        return assinaCancelametoInutilizacao(xml, certificado, raiz, cdEmpresa);  
    }  
  
    /** 
     * Assinatura do XML de Inutilizacao de sequenciais da NF-e utilizando 
     * Certificado Digital A1. 
     * @param xml 
     * @param certificado 
     * @return 
     * @throws Exception 
     */  
    public static Result assinaInutNFe(String xml, String certificado, int cdEmpresa) throws Exception {  
        return assinaCancelametoInutilizacao(xml, certificado, INFINUT, cdEmpresa);  
    }  
    
    /** 
     * Assinatura do XML de Envio do DPEC. 
     * DPEC = emissão da NF-e com Prévio Registro da DPEC no Ambiente Nacional. 
     * @param xml 
     * @param certificado 
     * @param senha 
     * @return Xml do Envio do DPEC Assinado. 
     * @throws Exception 
     */  
    private static Result assinaEnvDPEC(String xml, String certificado, int cdEmpresa)   
            throws Exception {  
        return assinaCancelametoInutilizacao(xml, certificado, INFDPEC, cdEmpresa);  
    }  
  
    private static Result assinaCancelametoInutilizacao(String xml,  
            String certificado, String tagCancInut, int cdEmpresa)  
            throws Exception {  
        Document document = documentFactory(xml);  
        XMLSignatureFactory signatureFactory = XMLSignatureFactory  
                .getInstance("DOM");  
        ArrayList<Transform> transformList = CertificadoServices.signatureFactory(signatureFactory);  
        CertificadoServices.loadCertificates(certificado, signatureFactory, cdEmpresa);  
        NodeList elements = ((org.w3c.dom.Document) document).getElementsByTagName(tagCancInut);  
        org.w3c.dom.Element el = (org.w3c.dom.Element) elements.item(0);  
        String id = el.getAttribute("Id");  
        Reference ref = signatureFactory.newReference("#" + id,  
                signatureFactory.newDigestMethod(DigestMethod.SHA1, null),  
                transformList, null, null);  
  
        SignedInfo si = signatureFactory.newSignedInfo(signatureFactory  
                .newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,  
                        (C14NMethodParameterSpec) null), signatureFactory  
                .newSignatureMethod(SignatureMethod.RSA_SHA1, null),  
                Collections.singletonList(ref));  
  
        XMLSignature signature = signatureFactory.newXMLSignature(si, CertificadoServices.keyInfo);  
  
        DOMSignContext dsc = new DOMSignContext(CertificadoServices.privateKey, el.getParentNode());  
        signature.sign(dsc);  
  
        Result resultado = new Result(1, "Assinado com sucesso!");
        resultado.addObject("xmlAssinado", outputXML(document));
        return resultado; 
    }  
     
  
/*******************************************************************************************************************************************************/    
    
    /**
   * Metodo para assinar uma NFe qualquer, com certificado A3
   * */
	public static Result assinarNFeA3(String xml, int etapa, int cdEmpresa)	{
		try	{
			String xmlAssinado = null;
			
			switch(etapa)	{
				case NfeServices.ENVIO:
					Result envioResultado = assinaEnviNFe(xml, cdEmpresa);  
//		            info("XML EnviNFe Assinado: " + xmlEnviNFeAssinado);  
		            if(envioResultado.getCode() <= 0)
		            	return envioResultado;
		            xmlAssinado = (String) envioResultado.getObjects().get("xmlAssinado"); 
					break;
				case NfeServices.CANCELAMENTO:
					Result cancResultado = assinaCancNFe(xml, cdEmpresa, etapa); 
					if(cancResultado.getCode() <= 0){
		            	return cancResultado;
		            }
					String xmlCancNFeAssinado = (String) cancResultado.getObjects().get("xmlAssinado");  
//		            info("XML CancNFe Assinado: " + xmlCancNFeAssinado);  
		            xmlAssinado = xmlCancNFeAssinado;
					break;
				case NfeServices.CANCELAMENTO_PROC:
					Result cancResultadoProc = assinaCancNFe(xml, cdEmpresa, etapa); 
					if(cancResultadoProc.getCode() <= 0){
		            	return cancResultadoProc;
		            }
					String xmlCancProcNFeAssinado = (String) cancResultadoProc.getObjects().get("xmlAssinado");  
//		            info("XML CancNFe Assinado: " + xmlCancNFeAssinado);  
		            xmlAssinado = xmlCancProcNFeAssinado;
					break;	
				case NfeServices.INUTILIZACAO:
					Result inutResultado = assinaInutNFe(xml, cdEmpresa);
					if(inutResultado.getCode() <= 0){
		            	return inutResultado;
		            }
		            String xmlInutNFeAssinado = (String) inutResultado.getObjects().get("xmlAssinado");     
//		            info("XML InutNFe Assinado: " + xmlInutNFeAssinado);  
		            xmlAssinado = xmlInutNFeAssinado;
					break;
				case NfeServices.EPEC:
					Result dpecResultado = assinaDpecNFe(xml, cdEmpresa);  
//		            info("XML EnviNFe Assinado: " + xmlEnviNFeAssinado);  
		            if(dpecResultado.getCode() <= 0)
		            	return dpecResultado;
		            xmlAssinado = (String) dpecResultado.getObjects().get("xmlAssinado"); 
					break;
				default:
					return null;
			}
		
			Result resultado = new Result (1, "Assinado com sucesso!");
			resultado.addObject("xmlAssinado", xmlAssinado);
			
			return resultado;
		}
		catch(Exception e) {
    		e.printStackTrace(System.out);
			return new Result(-1, "Erro ao assinar: " + e);
		}
		
	}
    
    /** 
     * Assinatura do XML de Envio de Lote da NF-e utilizando Certificado 
     * Digital A3. 
     * @param xml 
     * @param certificado 
     * @return 
     * @throws Exception 
     */  
    public static Result assinaEnviNFe(String xml, int cdEmpresa){  
    	try	{
    		Document document = documentFactory(xml); 
	    	XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");  
	        ArrayList<Transform> transformList   = CertificadoServices.signatureFactory(signatureFactory);
	        //
	        if(!CertificadoServices.loadCertificates(signatureFactory, cdEmpresa))
	        	return new Result(-1, "Falha ao carregar certificado!");
	        //
	        for (int i = 0; i < document.getDocumentElement().getElementsByTagName(NFE).getLength(); i++)  
	            assinarNFe(signatureFactory, transformList, CertificadoServices.privateKey, CertificadoServices.keyInfo, document, i);  
	        //
	        Result resultado = new Result(1, "Assinado com sucesso!");
	        resultado.addObject("xmlAssinado", outputXML(document));
	        
	        return resultado;  
	    }
    	catch (Exception e) {
    		e.printStackTrace(System.out);
			return new Result(-1, "Falha ao assinar xml de envio: "+e.getMessage(), e);
		}
    }  
  
    /** 
     * Assintaruda do XML de Cancelamento da NF-e utilizando Certificado 
     * Digital A3. 
     * @param xml 
     * @param certificado 
     * @return 
     * @throws Exception 
     */  
    public static Result assinaCancNFe(String xml, int cdEmpresa, int etapa)	{  
    	String raiz = (etapa == CANCELAMENTO) ? INFEVENTO : INFCANC;
        return assinaCancelametoInutilizacao(xml, raiz, cdEmpresa);  
    }  
  
    /** 
     * Assinatura do XML de Inutilizacao de sequenciais da NF-e utilizando 
     * Certificado Digital A3. 
     * @param xml 
     * @param certificado 
     * @param senha 
     * @return 
     * @throws Exception 
     */  
    public static Result assinaInutNFe(String xml, int cdEmpresa)	{  
        return assinaCancelametoInutilizacao(xml, INFINUT, cdEmpresa);  
    }
    
    /** 
     * Assintaruda do XML de Cancelamento da NF-e utilizando Certificado 
     * Digital A3. 
     * @param xml 
     * @param certificado 
     * @return 
     * @throws Exception 
     */  
    public static Result assinaDpecNFe(String xml, int cdEmpresa)	{  
        return assinaCancelametoInutilizacao(xml, INFDPEC, cdEmpresa);  
    }  
  
    private static void assinarNFe(XMLSignatureFactory fac, ArrayList<Transform> transformList, PrivateKey privateKey,  
                                   KeyInfo ki, Document document, int indexNFe) 
    {  
    	try	{
	        NodeList elements = document.getElementsByTagName("infNFe");  
	        org.w3c.dom.Element el = (org.w3c.dom.Element) elements.item(indexNFe);  
	        String id     = el.getAttribute("Id");
	        
	        
	        Reference ref = fac.newReference("#" + id, fac.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);  
	        SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,  
	                                                                        (C14NMethodParameterSpec)null), fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),  
	                                                                        Collections.singletonList(ref));  
	        XMLSignature signature = fac.newXMLSignature(si, ki); 
	        
	        DOMSignContext dsc     = new DOMSignContext(privateKey, document.getDocumentElement().getElementsByTagName(NFE).item(indexNFe));  
	        signature.sign(dsc);
    	}
    	catch(Exception e) {
    		e.printStackTrace(System.out);
    	}
    }  
  
    private static Result assinaCancelametoInutilizacao(String xml, String tagCancInut, int cdEmpresa)	{  
    	try	{
    		Document document = documentFactory(xml);  
	    	XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");  
	    	ArrayList<Transform> transformList   = CertificadoServices.signatureFactory(signatureFactory);  
	        if(!CertificadoServices.loadCertificates(signatureFactory, cdEmpresa))
	        	return new Result(-1, "Falha ao carregar certificado!");
	        
	        NodeList elements = ((org.w3c.dom.Document) document).getElementsByTagName(tagCancInut);  
	        org.w3c.dom.Element el = (org.w3c.dom.Element) elements.item(0);  
	        String id = el.getAttribute("Id");
	        Reference ref = signatureFactory.newReference("#" + id, signatureFactory.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);  
	        SignedInfo si = signatureFactory.newSignedInfo(signatureFactory .newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null), signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null),  
	                                                       Collections.singletonList(ref));  
	        XMLSignature signature = signatureFactory.newXMLSignature(si, CertificadoServices.keyInfo);  
	        DOMSignContext dsc     = new DOMSignContext(CertificadoServices.privateKey, el.getParentNode());  
	        signature.sign(dsc);  
	        Result resultado = new Result(1, "Assinado com sucesso!");
	        resultado.addObject("xmlAssinado", outputXML(document));
	        return resultado; 
    	}
    	catch (Exception e) {
    		e.printStackTrace(System.out);
    		return new Result(-1, "Erro cancelamento-inutilizacao: " + e);
		}
    }  
  
    private static Document documentFactory(String xml) throws SAXException,  
            IOException, ParserConfigurationException {  
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        factory.setNamespaceAware(true);  
        Document document = factory.newDocumentBuilder().parse(  
                new ByteArrayInputStream(xml.getBytes()));  
        return document;  
    }  
  
    private static String outputXML(Document doc){  
    	try{
	        ByteArrayOutputStream os = new ByteArrayOutputStream();  
	        TransformerFactory tf = TransformerFactory.newInstance();  
	        Transformer trans = tf.newTransformer();  
	        trans.transform(new DOMSource(doc), new StreamResult(os));  
	        String xml = os.toString();  
	        if ((xml != null) && (!"".equals(xml))) {  
	            xml = xml.replaceAll("\\r\\n", "");  
	            xml = xml.replaceAll(" standalone=\"no\"", "");  
	        }  
	        return xml;
    	}
    	
    	catch(Exception e){
    		e.printStackTrace(System.out); 
    		return null;
    	}
    }  
  
/********************************************************************************************************************************************************************/	
    /**
     * Transmiti o NFe para o SEFAZ e retorna com a mensagem se esta aceitou o documento ou não
     * @param cdNotaFiscal
     * @param cdEmpresa
     * @param isPDV
     * @return
     */
    public static Result transmitirNFe(int cdNotaFiscal, int cdEmpresa, boolean isPDV){    			
		HashMap<String, Object> register = new HashMap<String, Object>();
		register.put("cdNotaFiscal", cdNotaFiscal);
		register.put("cdEmpresa", cdEmpresa);	
		
		ArrayList<HashMap<String, Object>> criterios = new ArrayList<HashMap<String, Object>>();
		criterios.add(register);
		return transmitirNFe(criterios);					
    }
    
    /**
     * Metodo para transmissao da nota fiscal eletronica para a SEFAz, usado pelo WEB
     * @param registros
     * @return
     */
    public static Result transmitirNFe(ArrayList<HashMap<String,Object>> registros){
    	return transmitirNFe(registros, null);
    }
    
    public static Result transmitirNFe(ArrayList<HashMap<String,Object>> registros, Connection connect){
    	boolean isConnectionNull = connect == null;
    	try{
			int i = 0;
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Result resultado = new Result(1, "Notas transmitidas com sucesso!");
			ResultSetMap rs = new ResultSetMap();
			for(i = 0; i < registros.size(); i++){
				int cdNotaFiscal    = registros.get(i)==null || registros.get(i).get("cdNotaFiscal")==null ? 0 : (Integer) registros.get(i).get("cdNotaFiscal");
				int cdEmpresa    = registros.get(i)==null || registros.get(i).get("cdEmpresa")==null ? 0 : (Integer) registros.get(i).get("cdEmpresa");				
				Result retorno = transmitirNFe(cdNotaFiscal, cdEmpresa, connect);				
				NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);
				if(retorno.getCode() <= 0){
					resultado.setMessage("Nem todas as notas foram transmitidas com sucesso");
				}
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NUMERO", nota.getNrNotaFiscal());
				register.put("RESULTADO", retorno.getMessage());
				rs.addRegister(register);
			}
			
			resultado.addObject("resultado", rs);
			
			if(i == 0){
				return new Result(-1, "Nenhuma nota foi transmitida!");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return resultado;
    	}catch(Exception e){
    		Util.printInFile("C:/log.log", "\ne: " + e.toString());
    		return null;
    	}
    	
    	finally{
    		if(isConnectionNull)
    			Conexao.desconectar(connect);
    	}
	}
    
    public static Result transmitirNFe(int cdNotaFiscal, int cdEmpresa){
    	return transmitirNFe(cdNotaFiscal, cdEmpresa, null);
    }
    
    public static Result transmitirNFe(int cdNotaFiscal, int cdEmpresa, Connection connect){
    	boolean isConnectionNull = connect == null;
    	try {
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal, connect);
			int tpEmi = nota.getTpEmissao();
			
			Result resultado =  tpEmi != NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC ? gerarXmlNFE(cdNotaFiscal, cdEmpresa, tpEmi, connect) : gerarXmlDpec(cdNotaFiscal, cdEmpresa, connect);
			String xml = (String) resultado.getObjects().get("xml"); 		  
			if(tpEmi != NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC){
			   //Caso o resultado não tenha XML significa que houve algum erro
			   if(xml == null){
				   if(isConnectionNull)
						Conexao.rollback(connect);
				   return resultado;
			   }
			   //Assinar XML
			   String xmlAssinado = "";
			   if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3){
				   xmlAssinado = (String) assinarNFeA3(xml, tpEmi==NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC ? NfeServices.EPEC : NfeServices.ENVIO, cdEmpresa).getObjects().get("xmlAssinado");
			   }
			   else{				   
				   xmlAssinado = (String) assinarNFeA1(xml, tpEmi==NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC ? NfeServices.EPEC : NfeServices.ENVIO, cdEmpresa).getObjects().get("xmlAssinado"); 
			   }
			   
			   xml = xmlAssinado;
			}
			//Validação de XML de DPEC
			else{			   
			   Result resultadoValid = validarNFe(cdNotaFiscal, EPEC, cdEmpresa, connect);
			   if(resultadoValid.getCode() <= 0){
				   if(isConnectionNull)
					   Conexao.rollback(connect);
				   return resultadoValid;
			   }
			}
		    String codigoDoEstado = getCdUfDefault(connect);
		   
		    URL url      = new URL(getTipoAmbiente(cdEmpresa) == PRODUCAO ? "https://nfe.sefaz.ba.gov.br/webservices/NfeAutorizacao/NfeAutorizacao.asmx" : 
			                                                                "https://hnfe.sefaz.ba.gov.br/webservices/NfeAutorizacao/NfeAutorizacao.asmx");//NfeRecepcao  
		   
		    if(tpEmi == NotaFiscalServices.EMI_CONTIGENCIA_SVC_AN)
			    url      = new URL(getTipoAmbiente(cdEmpresa) == PRODUCAO ? "https://www.svc.fazenda.gov.br/NfeAutorizacao/NfeAutorizacao.asmx" : 
                       													    "https://hom.svc.fazenda.gov.br/NfeAutorizacao/NfeAutorizacao.asmx");//NfeRecepcao 
		   
		    else if(tpEmi == NotaFiscalServices.EMI_CONTIGENCIA_SVC_RS)
			    url      = new URL(getTipoAmbiente(cdEmpresa) == PRODUCAO ? "https://nfe.sefazvirtual.rs.gov.br/ws/NfeAutorizacao/NFeAutorizacao.asmx" : 
                       													    "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeAutorizacao/NFeAutorizacao.asmx");//NfeRecepcao
		    else if(tpEmi == NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC)
			    url      = new URL(getTipoAmbiente(cdEmpresa) == PRODUCAO ? "https://www.nfe.fazenda.gov.br/RecepcaoEvento/RecepcaoEvento.asmx" : 
                       													    "https://hom.nfe.fazenda.gov.br/RecepcaoEvento/RecepcaoEvento.asmx");//NfeRecepcao
		    else if(tpEmi != NotaFiscalServices.EMI_NORMAL){
			    if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Sistema não permite envio de nota para o tipo de emissão " + NotaFiscalServices.tiposNotaFiscal[tpEmi]);
		    }
			   
		   
		   String configName         = ParametroServices.getValorOfParametro("NM_FILE_CERTIFICADO", "SmartCard.cfg", cdEmpresa);  
           String senhaDoCertificado = CertificadoServices.getPass(cdEmpresa);    
           String arquivoCacerts     = CertificadoServices.getCacertsFile(cdEmpresa);    
           if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3){
        	   Provider p  = new sun.security.pkcs11.SunPKCS11(configName);    
	           Security.addProvider(p);    
	           char[] pin  = senhaDoCertificado.toCharArray();    
	           KeyStore ks = KeyStore.getInstance("pkcs11");    
	           ks.load(null, pin);    
	           String alias = "";    
	           Enumeration<String> aliasesEnum = ks.aliases();    
	           while (aliasesEnum.hasMoreElements()) {    
	               alias = (String) aliasesEnum.nextElement();    
	               if (ks.isKeyEntry(alias)) break;    
	           }    
	           X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);    
	           PrivateKey privateKey = (PrivateKey) ks.getKey(alias, senhaDoCertificado.toCharArray());    
	           SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(certificate, privateKey);    
	           socketFactoryDinamico.setFileCacerts(arquivoCacerts);    
	   
	           Protocol protocol = new Protocol("https", socketFactoryDinamico, SSL_PORT);      
	           Protocol.registerProtocol("https", protocol);   
           }
           else{
        	   InputStream entrada = new FileInputStream(configName);  
        	   KeyStore ks = KeyStore.getInstance("pkcs12");  
               try {  
                   ks.load(entrada, senhaDoCertificado.toCharArray());  
               } catch (IOException e) {
            	   	if(isConnectionNull)
	   					Conexao.rollback(connect);
	   				if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa) == 1){
	   					nota.setNrNotaFiscal(null);
	   					nota.setNrChaveAcesso(null);
	   					nota.setNrDv(0);
	   					NotaFiscalDAO.update(nota);
	   				}
                   throw new Exception("Senha do Certificado Digital esta incorreta ou Certificado inválido.");  
               }  
               
               String alias = "";    
               Enumeration<String> aliasesEnum = ks.aliases();    
               while (aliasesEnum.hasMoreElements()) {    
                   alias = (String) aliasesEnum.nextElement();    
                   if (ks.isKeyEntry(alias)) break;    
               }  
               X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);  
               PrivateKey privateKey = (PrivateKey) ks.getKey(alias, senhaDoCertificado.toCharArray());  
               SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(certificate, privateKey);  
               socketFactoryDinamico.setFileCacerts(arquivoCacerts);  
     
               Protocol protocol = new Protocol("https", socketFactoryDinamico, SSL_PORT);    
               Protocol.registerProtocol("https", protocol);  
        	   
        	   
           }
           /** 
            * IMPORTANTE: O XML já deve ser assinado antes do envio. 
            * Lendo o Xml de um arquivo Gerado. 
            */  
           String xmlEnvNFe = xml.toString();
     
           System.out.println("xml de transmissao = " + xmlEnvNFe);
           
           //O historico é inserido na transmissao da nota
           String mensagem = (tpEmi == NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC ? "Envio XML por EPEC" : "Envio de XML da nota");
           int tpHistorico = (tpEmi == NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC ? NotaFiscalHistoricoServices.ENVIO_XML_EPEC : NotaFiscalHistoricoServices.ENVIO_XML_NOTA);
           NotaFiscalHistorico notaHistorico = new NotaFiscalHistorico(cdNotaFiscal, 0, 0, null, 
								Util.getDataAtual(), null, null,  
								null,  null,  null,  mensagem,  
								null, xmlEnvNFe.toString(), tpHistorico);
			
           if(NotaFiscalHistoricoDAO.insert(notaHistorico, connect) <= 0){
        	   if(isConnectionNull)
					Conexao.rollback(connect);
			   return new Result(-1, "Erro ao inserir NotaHistorico");
           }
           OMElement ome = AXIOMUtil.stringToOM(xmlEnvNFe);  
           Result res = null;

           if(tpEmi != NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC){
        	   //Na transmissao o xml de envio é gravado na nota fiscal
        	   NotaFiscal nf = NotaFiscalDAO.get(cdNotaFiscal, connect);
               nf.setTxtXml(xmlEnvNFe);
               if(NotaFiscalDAO.update(nf, connect) < 0){
            	   if(isConnectionNull)
            		   Conexao.rollback(connect);
   				   return new Result(-1, "Erro ao acrescentar Xml Ã  nota!");
               }
        	   Iterator<?> children = ome.getChildrenWithLocalName("NFe");    
	           while (children.hasNext()) {  
	               OMElement omElement = (OMElement) children.next();    
	               if ((omElement != null) && ("NFe".equals(omElement.getLocalName()))) {    
	                   omElement.addAttribute("xmlns", "http://www.portalfiscal.inf.br/nfe", null);    
	               }  
	           }  
	           NfeAutorizacaoStub.NfeDadosMsg dadosMsg = new NfeAutorizacaoStub.NfeDadosMsg();  
	           dadosMsg.setExtraElement(ome);  
	           NfeAutorizacaoStub.NfeCabecMsg nfeCabecMsg = new NfeAutorizacaoStub.NfeCabecMsg();  
	           /** 
	            * Código do Estado. 
	            */  
	           nfeCabecMsg.setCUF(codigoDoEstado);  
	           /** 
	            * Versao do XML 
	            */  
	           nfeCabecMsg.setVersaoDados("3.10");  
	           NfeAutorizacaoStub.NfeCabecMsgE nfeCabecMsgE = new NfeAutorizacaoStub.NfeCabecMsgE();  
	           nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);  
	           NfeAutorizacaoStub stub = new NfeAutorizacaoStub(url.toString());
	           NfeAutorizacaoStub.NfeAutorizacaoLoteResult result = stub.nfeAutorizacaoLote(dadosMsg, nfeCabecMsgE);
	           
	           //Numero de recebimento é gravado na nota fiscal para ser consultado na base da SEFAZ
	           res = getNumeroRecibo(result.getExtraElement().toString());
	           if(res.getCode() <= 0){
	        	   if(isConnectionNull)
						Conexao.rollback(connect);
				   return new Result(-1, res.getMessage());
	           }
	           String numeroRecibo = (String) res.getObjects().get("recibo");
	           if(numeroRecibo != null){
				   nota.setTpEmissao(tpEmi);
				   nota.setStNotaFiscal(NotaFiscalServices.TRANSMITIDA);
				   nota.setNrRecebimento(numeroRecibo);
			       NotaFiscalDAO.update(nota, connect);
			   }
	           if(isConnectionNull)
	        	   connect.commit();
	           return res;
           }
           //Transmissão via EPEC
           else{
        	   SCERecepcaoRFBStub.SceDadosMsg sceDadosMsg = new SCERecepcaoRFBStub.SceDadosMsg();  
	           sceDadosMsg.setExtraElement(ome);
	           SCERecepcaoRFBStub.SceCabecMsg sceCabecMsg = new SCERecepcaoRFBStub.SceCabecMsg();  
	           /** 
	            * Versao do XML 
	            */  
	           sceCabecMsg.setVersaoDados("1.01");  
	           SCERecepcaoRFBStub.SceCabecMsgE sceCabecMsgE = new SCERecepcaoRFBStub.SceCabecMsgE();  
	           sceCabecMsgE.setSceCabecMsg(sceCabecMsg);  
	           SCERecepcaoRFBStub stub = new SCERecepcaoRFBStub(url.toString());
	           SCERecepcaoRFBStub.SceRecepcaoDPECResult result = stub.sceRecepcaoDPEC(sceDadosMsg, sceCabecMsgE);
	           String xmlRecDpec = result.getExtraElement().toString();
	           
	           //Busca o numero de recibo de EPEC
	           res = getNumeroReciboDpec(xmlRecDpec);
	           if(res.getCode() <= 0){
	        	   if(isConnectionNull)
						Conexao.rollback(connect);
				   return new Result(-1, res.getMessage());
	           }
	           String numeroRecibo = (String) res.getObjects().get("recibo");
	           if(numeroRecibo != null){
	        	   
	        	   //É gravado o histórico de envio via EPEC com o XML de recebimento de DPEC
	        	   notaHistorico = new NotaFiscalHistorico(cdNotaFiscal, 0, 0, null, 
										Util.getDataAtual(), null, null,  
										numeroRecibo,  null,  null,  "Retorno DPEC", 
										null, xmlRecDpec, NotaFiscalHistoricoServices.RETORNO_DPEC);
					
				   if(NotaFiscalHistoricoDAO.insert(notaHistorico, connect) <= 0){
					   if(isConnectionNull)
							Conexao.rollback(connect);
					   return new Result(-1, "Erro ao inserir NotaHistorico");
				   }
	        	   
				   //É gravado o histórico dizendo que a nota foi enviada por EPEC com o XML de envio
	        	   notaHistorico = new NotaFiscalHistorico(cdNotaFiscal, 0, 0, null, 
										Util.getDataAtual(), null, null,  
										numeroRecibo,  null,  null,  "Nota Enviada via DPEC", 
										null, xmlEnvNFe.toString(), NotaFiscalHistoricoServices.ENVIO_DPEC);
					
				   if(NotaFiscalHistoricoDAO.insert(notaHistorico, connect) <= 0){
					   if(isConnectionNull)
							Conexao.rollback(connect);
					   return new Result(-1, "Erro ao inserir NotaHistorico");
				   }
				   
				   //O DPEC é considerado uma nota já autorizada, e o sistema enviará novamente essa nota quando o sistema da SEFAZ estiver funcionando
				   nota.setTpEmissao(NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC);
				   nota.setStNotaFiscal(NotaFiscalServices.AUTORIZADA);
				   nota.setNrRecebimento(numeroRecibo);
			       NotaFiscalDAO.update(nota, connect);
			       String[] argumentos = new String[9];
			       argumentos[4] = numeroRecibo;
			       String xmlDpecProtocolo = getXmlComProtocolo(cdNotaFiscal, argumentos, cdEmpresa);
				   
			       //É gravado no histórico o envio do DPEC, e o XML do DPEC de Protocolo é gravado
				   notaHistorico = new NotaFiscalHistorico(cdNotaFiscal, 0, 0, null, 
										Util.getDataAtual(), null, null,  
										numeroRecibo,  null,  null,  "Proc DPEC", 
										null, xmlDpecProtocolo, NotaFiscalHistoricoServices.PROC_DPEC);
					
				   if(NotaFiscalHistoricoDAO.insert(notaHistorico, connect) <= 0){
					   if(isConnectionNull)
							Conexao.rollback(connect);
					   return new Result(-1, "Erro ao inserir NotaHistorico");
				   }
				   
			   }
	           
	           if(isConnectionNull)
	        	   connect.commit();
	           
			   return res;
           }
                
    		
		} 
		catch (Exception e) {
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa)==1){
				NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);
				nota.setNrNotaFiscal(null);
				nota.setNrChaveAcesso(null);
				nota.setNrDv(0);
				NotaFiscalDAO.update(nota);
			}
			return new Result(-1, "Erro ao transmitir a nota para a SEFAZ");
		}
    	
    	finally{
    		if(isConnectionNull)
    			Conexao.desconectar(connect);
    	}
    	
	}

	
	
/********************************************************************************************************************************************************************/		
	
	/**
	 * Recepção do retorno da SEFAZ em relação ao NFe enviado
	 * @param cdNotaFiscal
	 * @param cdEmpresa
	 * @param isPDV
	 * @return
	 */
    public static Result retornoProcessamento(int cdNotaFiscal, int cdEmpresa, boolean isPDV){			
		HashMap<String, Object> register = new HashMap<String, Object>();
		register.put("cdNotaFiscal", cdNotaFiscal);
		register.put("cdEmpresa", cdEmpresa);	
		
		ArrayList<HashMap<String, Object>> criterios = new ArrayList<HashMap<String, Object>>();
		criterios.add(register);
		return retornoProcessamento(criterios);				
			
  	
    }
    
    /**
     * Recepção do retorno da SEFAZ em relação ao NFe enviado - Usado no WEB
     * @param registros
     * @return
     */
    public static Result retornoProcessamento(ArrayList<HashMap<String,Object>> registros){
    	return retornoProcessamento(registros, null);
    }
    
	public static Result retornoProcessamento(ArrayList<HashMap<String,Object>> registros, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int i = 0;
			Result resultado = new Result(1, "Consultas realizadas com sucesso!");
			ResultSetMap rs = new ResultSetMap();
			for(i = 0; i < registros.size(); i++){
				int cdNotaFiscal    = registros.get(i)==null || registros.get(i).get("cdNotaFiscal")==null ? 0 : (Integer) registros.get(i).get("cdNotaFiscal");
				int cdEmpresa    = registros.get(i)==null || registros.get(i).get("cdEmpresa")==null ? 0 : (Integer) registros.get(i).get("cdEmpresa");
				Result retorno = retornoProcessamento(cdNotaFiscal, cdEmpresa, connect);
				NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal, connect);
				if(retorno.getCode() <= 0){
					resultado.setMessage("Nem todas as consultas foram realizadas com sucesso");
				}
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NUMERO", nota.getNrNotaFiscal());
				register.put("RESULTADO", retorno.getMessage());
				rs.addRegister(register);
			}
			
			resultado.addObject("resultado", rs);
			
			if(i == 0){
				return new Result(-1, "Nenhuma nota foi autorizada ou denegada!");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return resultado;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro no retorno de processamento");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Retorno de processamento usado pela Nota Rapida
	 * @param cdNotaFiscal
	 * @param cdEmpresa
	 * @return
	 */
	public static Result retornoProcessamento(int cdNotaFiscal, int cdEmpresa){
		return retornoProcessamento(cdNotaFiscal, cdEmpresa, null);
	}
	
	public static Result retornoProcessamento(int cdNotaFiscal, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect == null;
		
		
		try {  
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			NotaFiscal notaFiscal = NotaFiscalDAO.get(cdNotaFiscal, connect);
			
			String codigoDoEstado = getCdUfDefault(connect);  
			
			int tpEmi = notaFiscal.getTpEmissao();
			/*BAHIA*/
			URL url      = new URL(getTipoAmbiente(cdEmpresa) == PRODUCAO ? "https://nfe.sefaz.ba.gov.br/webservices/NfeRetAutorizacao/NfeRetAutorizacao.asmx" : 
                    														"https://hnfe.sefaz.ba.gov.br/webservices/NfeRetAutorizacao/NfeRetAutorizacao.asmx");//NfeRecepcao  

			if(tpEmi == NotaFiscalServices.EMI_CONTIGENCIA_SVC_AN)
				url      = new URL(getTipoAmbiente(cdEmpresa) == PRODUCAO ? "https://www.svc.fazenda.gov.br/NfeRetAutorizacao/NfeRetAutorizacao.asmx" : 
								    										"https://hom.svc.fazenda.gov.br/NfeRetAutorizacao/NfeRetAutorizacao.asmx");//NfeRecepcao 
			
			else if(tpEmi == NotaFiscalServices.EMI_CONTIGENCIA_SVC_RS)
				url      = new URL(getTipoAmbiente(cdEmpresa) == PRODUCAO ? "https://nfe.sefazvirtual.rs.gov.br/ws/NfeRetAutorizacao/NFeRetAutorizacao.asmx" : 
								    										"https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeRetAutorizacao/NFeRetAutorizacao.asmx");//NfeRecepcao
			else if(tpEmi != NotaFiscalServices.EMI_NORMAL){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Sistema não permite envio de nota para o tipo de emissão " + NotaFiscalServices.tiposNotaFiscal[tpEmi]);
			}
	      	String numeroDoRecibo  = notaFiscal.getNrRecebimento();  
	        String configName      = ParametroServices.getValorOfParametro("NM_FILE_CERTIFICADO", "SmartCard.cfg", cdEmpresa, connect);    
            String passCertificado = CertificadoServices.getPass(cdEmpresa);  
            String arquivoCacertsGeradoTodosOsEstados = CertificadoServices.getCacertsFile(cdEmpresa);    
            if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3){
	            Provider p = new sun.security.pkcs11.SunPKCS11(configName);    
	            Security.addProvider(p);    
	            char[] pin = passCertificado.toCharArray();    
	            KeyStore ks = KeyStore.getInstance("pkcs11");    
	            ks.load(null, pin);    
	            String alias = "";    
	            Enumeration<String> aliasesEnum = ks.aliases();    
	            while (aliasesEnum.hasMoreElements()) {    
	                alias = (String) aliasesEnum.nextElement();    
	                if (ks.isKeyEntry(alias)) break;    
	            }    
	            X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);    
	            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, passCertificado.toCharArray());    
	            SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(certificate, privateKey);    
	            socketFactoryDinamico.setFileCacerts(arquivoCacertsGeradoTodosOsEstados);    
	            Protocol protocol = new Protocol("https", socketFactoryDinamico, SSL_PORT);      
	            Protocol.registerProtocol("https", protocol);  
            }
            else{
	            /** 
	            * Informações do Certificado Digital. 
	            */  
//	            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");  
//	            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());  
//	            System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");  
//		        System.setProperty("javax.net.ssl.keyStore", configName);  
//		        System.setProperty("javax.net.ssl.keyStorePassword", passCertificado);  
//		        System.setProperty("javax.net.ssl.trustStoreType", "JKS");  
//   	            System.setProperty("javax.net.ssl.trustStore", arquivoCacertsGeradoTodosOsEstados);  
            	
            	InputStream entrada = new FileInputStream(configName);  
                KeyStore ks = KeyStore.getInstance("pkcs12");  
                try {  
                    ks.load(entrada, passCertificado.toCharArray());  
                } catch (IOException e) { 
                	if(isConnectionNull)
                		Conexao.rollback(connect);
                	if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa, connect) == 1){
    	            	notaFiscal.setNrNotaFiscal(null);
    	            	notaFiscal.setNrChaveAcesso(null);
    	            	notaFiscal.setNrDv(0);
    	            	NotaFiscalDAO.update(notaFiscal);
                	}
                    throw new Exception("Senha do Certificado Digital esta incorreta ou Certificado inválido.");  
                }  
      
                /** 
                 * Resolve o problema do 403.7 Forbidden para Certificados A3 e A1  
                 * e elimina o uso das configurações: 
                 * - System.setProperty("javax.net.ssl.keyStore", "NONE"); 
                 * - System.setProperty("javax.net.ssl.keyStoreType", "PKCS11"); 
                 * - System.setProperty("javax.net.ssl.keyStoreProvider", "SunPKCS11-SmartCard"); 
                 * - System.setProperty("javax.net.ssl.trustStoreType", "JKS"); 
                 * - System.setProperty("javax.net.ssl.trustStore", arquivoCacertsGeradoTodosOsEstados); 
                 */  
                String alias = "";    
                Enumeration<String> aliasesEnum = ks.aliases();    
                while (aliasesEnum.hasMoreElements()) {    
                    alias = (String) aliasesEnum.nextElement();    
                    if (ks.isKeyEntry(alias)) break;    
                }  
                X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);  
                PrivateKey privateKey = (PrivateKey) ks.getKey(alias, passCertificado.toCharArray());  
                SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(certificate, privateKey);  
                socketFactoryDinamico.setFileCacerts(arquivoCacertsGeradoTodosOsEstados);  
      
                Protocol protocol = new Protocol("https", socketFactoryDinamico, SSL_PORT);    
                Protocol.registerProtocol("https", protocol); 
           }	
            /** 
             * Xml de Consulta. 
             */  
            StringBuilder xml = new StringBuilder();  
            String retornoProcSefaz[] = null;
            
        	xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")  
                .append("<consReciNFe versao=\"3.10\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">")  
                .append("<tpAmb>"+getTipoAmbiente(cdEmpresa)+"</tpAmb>")  
                .append("<nRec>")  
                .append(numeroDoRecibo)  
                .append("</nRec>")  
                .append("</consReciNFe>"); 
            
            OMElement ome = AXIOMUtil.stringToOM(xml.toString()); 
        	
            NfeRetAutorizacaoStub.NfeDadosMsg dadosMsg = new NfeRetAutorizacaoStub.NfeDadosMsg();  
            dadosMsg.setExtraElement(ome);
            NfeRetAutorizacaoStub.NfeCabecMsg nfeCabecMsg = new NfeRetAutorizacaoStub.NfeCabecMsg();  
            /** 
             * Código do Estado. 
             */  
            nfeCabecMsg.setCUF(codigoDoEstado);  
            /** 
             * Versão do XML 
             */  
            nfeCabecMsg.setVersaoDados("3.10");  
            NfeRetAutorizacaoStub.NfeCabecMsgE nfeCabecMsgE = new NfeRetAutorizacaoStub.NfeCabecMsgE();  
            nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);  
            NfeRetAutorizacaoStub stub = new NfeRetAutorizacaoStub(url.toString());  
            NfeRetAutorizacaoStub.NfeRetAutorizacaoLoteResult result = stub.nfeRetAutorizacaoLote(dadosMsg, nfeCabecMsgE);  
            retornoProcSefaz = getRetornoSEFAZ(result.getExtraElement().toString());
            if(retornoProcSefaz == null || retornoProcSefaz.length == 0){
            	if(isConnectionNull)
            		Conexao.rollback(connect);
            	return new Result(-1, "Problema não identificado!");
            }
			if(retornoProcSefaz[6] == null){
				if(isConnectionNull)
            		Conexao.rollback(connect);
            	if(retornoProcSefaz[7] != null){
					return new Result(-1, "Retorno da SEFAZ: " + retornoProcSefaz[7]);
				}
				return new Result(-1, "Erro no retorno da SEFAZ!");
			}
			int codigoResultado = Integer.parseInt(retornoProcSefaz[6]);
			// é registrado o histórico do envio da consulta da nota
			GregorianCalendar dataRec = Util.getDataAtual(); 
			NotaFiscalHistorico notaHistorico = new NotaFiscalHistorico(cdNotaFiscal, 0, Integer.parseInt(retornoProcSefaz[0]), retornoProcSefaz[1], 
																		Util.getDataAtual(), dataRec, retornoProcSefaz[3],  
																		retornoProcSefaz[4],  retornoProcSefaz[5],  retornoProcSefaz[6],  (codigoResultado == 100 ? "Envio do número de recibo para verificação" : retornoProcSefaz[7]),  
																		retornoProcSefaz[8], xml.toString(), NotaFiscalHistoricoServices.ENVIO_NUMERO_RECIBO);
			int code = NotaFiscalHistoricoDAO.insert(notaHistorico, connect);
			if(code <= 0){
				if(isConnectionNull)
            		Conexao.rollback(connect);
            	return new Result(-1, "Erro ao inserir NotaHistorico");
			}
			notaHistorico.setCdHistorico(code);
			if(codigoResultado == 105){//Lote em Processamento
				if(isConnectionNull)
            		connect.commit();
            	return new Result(1, "Retorno da SEFAZ: " + notaHistorico.getDsMensagem());
			}
			else if(codigoResultado == 106){//Lote não localizado - Provavel problema com Tipo de Ambiente
				if(notaFiscal.getTpEmissao() != NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC){
					notaFiscal.setStNotaFiscal(NotaFiscalServices.EM_DIGITACAO);
					notaFiscal.setNrRecebimento(null);//Retorna o numero de recebimento para nulo
					if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa) == 1){
		            	notaFiscal.setNrNotaFiscal(null);
		            	notaFiscal.setNrChaveAcesso(null);
		            	notaFiscal.setNrDv(0);
		        	}
	            
	            	NotaFiscalDAO.update(notaFiscal, connect);
	    	    }
				
				if(isConnectionNull)
					connect.commit();
				
				return new Result(-1, "Retorno da SEFAZ: " + notaHistorico.getDsMensagem());
			}
			
			else if(codigoResultado == 107){// Serviço em operação
				
				if(isConnectionNull)
					connect.commit();
				//Fazer chamada para caso o usuário queira enviar a nota em contigencia
				return new Result(1, "Retorno da SEFAZ: " + notaHistorico.getDsMensagem());
			}
			else if(codigoResultado == 108){//Serviço Paralisado Momentaneamente (curto prazo)
				if(isConnectionNull)
					connect.commit();
				//Fazer chamada para caso o usuário queira enviar a nota em contigencia
				return new Result(1, "Retorno da SEFAZ: " + notaHistorico.getDsMensagem());
			}
			else if(codigoResultado == 109){//Serviço Paralisado sem Previsão
				if(isConnectionNull)
					connect.commit();
				//Fazer chamada para caso o usuário queira enviar a nota em contigencia
				return new Result(1, "Retorno da SEFAZ: " + notaHistorico.getDsMensagem());
			}
			
			else if(codigoResultado == 110){//Uso Denegado - Numeração nao poderá mais ser usada
				notaFiscal.setStNotaFiscal(NotaFiscalServices.DENEGADA);
				notaFiscal.setDtAutorizacao(null);
				NotaFiscalDAO.update(notaFiscal);
				if(isConnectionNull)
					connect.commit();
				return new Result(1, "Retorno da SEFAZ: " + notaHistorico.getDsMensagem());
			}
			
			else if(codigoResultado == 301 || codigoResultado == 302){//Uso Denegado - Irregularidades do Emitente ou Destinatario respectivamente - Numeração nao poderá mais ser usada
				notaFiscal.setStNotaFiscal(NotaFiscalServices.DENEGADA);
				notaFiscal.setDtAutorizacao(null);
				NotaFiscalDAO.update(notaFiscal);
				if(isConnectionNull)
					connect.commit();
				return new Result(1, "Retorno da SEFAZ: " + notaHistorico.getDsMensagem());
			}
			
			else if(codigoResultado == 100){//Autorizado o uso da NF-e
				//28-12-2012	
				notaFiscal.setStNotaFiscal(NotaFiscalServices.AUTORIZADA);
				notaFiscal.setNrProtocoloAutorizacao(notaHistorico.getNrProtocolo());
				notaFiscal.setDtAutorizacao(Util.getDataAtual());
				NotaFiscalDAO.update(notaFiscal, connect);
				
				String xmlGravacao = getXmlComProtocolo(cdNotaFiscal, retornoProcSefaz, cdEmpresa, connect);
				String caminhoPadrao = ParametroServices.getValorOfParametro("NM_REPOSITORIO_XML", cdEmpresa, connect);
				
				if(caminhoPadrao == null || caminhoPadrao.trim().equals("")){
					if(isConnectionNull)
						connect.rollback();
					return new Result(-1, "Parametro de Caminho Padrão XML não esta configurado!");
				}
				
				String caminhoPasta = caminhoPadrao + "/" + dataRec.get(Calendar.YEAR) + "_" + (dataRec.get(Calendar.MONTH) + 1);
				
				FileOutputStream gravar = null;
				try{
					File arquivo = new File(notaFiscal.getNrChaveAcesso() + ".xml"); 
					
					File diretorio = new File(caminhoPasta);
					if(!diretorio.exists()){
						diretorio.mkdir();
					}
					
		    		gravar = new FileOutputStream(caminhoPasta + "/" + arquivo);
					gravar.write(xmlGravacao.getBytes());
					gravar.close();
				}
				catch(Exception ex){
					if(gravar != null) 
						gravar.close();
				}
				
				notaHistorico = new NotaFiscalHistorico(cdNotaFiscal, 0, Integer.parseInt(retornoProcSefaz[0]), retornoProcSefaz[1], 
						Util.getDataAtual(), dataRec, retornoProcSefaz[3],  
						retornoProcSefaz[4],  retornoProcSefaz[5],  retornoProcSefaz[6],  retornoProcSefaz[7],  
						retornoProcSefaz[8], xmlGravacao, NotaFiscalHistoricoServices.AUTORIZACAO_NOTA);
				if(NotaFiscalHistoricoDAO.insert(notaHistorico, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar histórico da nota!");
				}
				NotaFiscalServices.enviarEmail(cdNotaFiscal);
				
				Result res = new Result(code, "Autorizado o uso da NF-e");
				
				if(isConnectionNull)
					connect.commit();
				
				return res;
			}
			
			else if(codigoResultado == 539){//Rejeição de Duplicidade
				//Cria uma nota fiscal apenas para registro, que diz que já existe na base da SEFAZ uma nota
				//com esse Número, porém não se encontra no sistema. 
				//:TODO Desenvolver uma rotina para recuperação dos dados das notas da SEFAZ
				NotaFiscal notaNova = new NotaFiscal();
				notaNova.setNrNotaFiscal(notaFiscal.getNrNotaFiscal());
				notaNova.setStNotaFiscal(NotaFiscalServices.ST_FORA_DO_SISTEMA);
				notaNova.setCdEmpresa(notaFiscal.getCdEmpresa());
				notaNova.setCdNaturezaOperacao(notaFiscal.getCdNaturezaOperacao());
				notaNova.setCdCidade(notaFiscal.getCdCidade());
				NotaFiscalDAO.insert(notaNova, connect);
				
				if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa, connect) == 1){
	            	notaFiscal.setNrNotaFiscal(null);
	            	notaFiscal.setNrChaveAcesso(null);
	            	notaFiscal.setNrDv(0);
	            }
				if(notaFiscal.getTpEmissao() != NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC){
					notaFiscal.setStNotaFiscal(NotaFiscalServices.EM_DIGITACAO);
					notaFiscal.setNrRecebimento(null);//Retorna o numero de recebimento para nulo
					NotaFiscalDAO.update(notaFiscal, connect);
				}
				
				if(isConnectionNull)
            		connect.commit();
				return new Result(-1, "Retorno da SEFAZ: " + notaHistorico.getDsMensagem());
			}
			
			else if(codigoResultado == 999){//Erro não catalogado - retorna para situação em digitação
				
				if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa, connect) == 1){
	            	notaFiscal.setNrNotaFiscal(null);
	            	notaFiscal.setNrChaveAcesso(null);
	            	notaFiscal.setNrDv(0);
	            }
				if(notaFiscal.getTpEmissao() != NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC){
					notaFiscal.setStNotaFiscal(NotaFiscalServices.EM_DIGITACAO);
					notaFiscal.setNrRecebimento(null);//Retorna o numero de recebimento para nulo
				}
				
				NotaFiscalDAO.update(notaFiscal, connect);
				
				if(isConnectionNull)
            		connect.commit();
				
				return new Result(-1, "Retorno da SEFAZ: Erro não catalogado!");
			}
			
			else{//Codigos de Rejeição - A nota pode ser submetida novamente com a correção dos erros encontrados com a mesma numeracao - retorna para situação em digitação
				if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa, connect) == 1){
	            	notaFiscal.setNrNotaFiscal(null);
	            	notaFiscal.setNrChaveAcesso(null);
	            	notaFiscal.setNrDv(0);
	            }
				if(notaFiscal.getTpEmissao() != NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC){
					notaFiscal.setStNotaFiscal(NotaFiscalServices.EM_DIGITACAO);
					notaFiscal.setNrRecebimento(null);//Retorna o numero de recebimento para nulo
					NotaFiscalDAO.update(notaFiscal, connect);
				}
				if(isConnectionNull)
            		connect.commit();
				return new Result(-1, "Retorno da SEFAZ: " + notaHistorico.getDsMensagem());
			}
            
        } 
		catch (KeyStoreException kse){
			if(isConnectionNull)
        		Conexao.rollback(connect);
			if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa, connect)==1){
				NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);
				nota.setNrNotaFiscal(null);
				nota.setNrChaveAcesso(null);
				nota.setNrDv(0);
				NotaFiscalDAO.update(nota);
			}
		    return new Result(-1, "Problema com Chave do Certificado. Verifique se o mesmo está conectado ao computador: " + kse.getMessage());
		}
		catch (AxisFault e) {
			if(isConnectionNull)
        		Conexao.rollback(connect);
			if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa, connect)==1){
				NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);
				nota.setNrNotaFiscal(null);
				nota.setNrChaveAcesso(null);
				nota.setNrDv(0);
				NotaFiscalDAO.update(nota);
			}
	        e.printStackTrace(System.out);  
	        return new Result(-1, "Problema de Conexão com a internet: " + e.getMessage());
		}
		catch (Exception e) {
			if(isConnectionNull)
        		Conexao.rollback(connect);
			if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa, connect)==1){
				NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);
				nota.setNrNotaFiscal(null);
				nota.setNrChaveAcesso(null);
				nota.setNrDv(0);
				NotaFiscalDAO.update(nota);
			}
            e.printStackTrace(System.out);
            return new Result(-1, "Erro: " + e.getMessage());
        } 

		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
/**************************************************************************************************************************************************/
	
	public static Result gerarXmlNFECanc(int cdNotaFiscal, String txtMotivo, Connection connect) {  
        boolean isConnectionIsNull = connect == null;
		try {
			if(isConnectionIsNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			ResultSet rsNfe = connect.prepareStatement("SELECT A.*, "+
														"      B.nr_codigo_fiscal AS nr_cfop, C.nm_pessoa AS nm_fantasia, C.nr_telefone1,  "+
														"      D.nr_cnpj AS nr_cnpj_emissor, D.nm_razao_social, D.nr_inscricao_estadual, D.nr_inscricao_municipal, "+
														"      E.nm_logradouro, E.nm_bairro, E.nr_endereco, E.nr_cep, E.nm_complemento, "+
														"      F.nm_cidade, F.id_ibge AS id_ibge_cidade, G.sg_estado "+
														"FROM fsc_nota_fiscal A "+
														"JOIN adm_natureza_operacao B ON (A.cd_natureza_operacao = B.cd_natureza_operacao) "+
														"JOIN grl_pessoa            C ON (A.cd_empresa           = C.cd_pessoa) "+
														"JOIN grl_pessoa_juridica   D ON (A.cd_empresa           = D.cd_pessoa) "+
														"JOIN grl_pessoa_endereco   E ON (A.cd_empresa           = E.cd_pessoa  "+
														"                             AND E.lg_principal         = 1)           "+
														"JOIN grl_cidade            F ON (E.cd_cidade            = F.cd_cidade) "+
														"JOIN grl_estado            G ON (F.cd_estado            = G.cd_estado) "+
														"WHERE A.cd_nota_fiscal = " + cdNotaFiscal).executeQuery();
            if(!rsNfe.next()){
            	if(isConnectionIsNull)
            		Conexao.rollback(connect);
            	return new Result(-1, "Nota fiscal não localizada!");
            }
            
            TEnvEvento evnEvento = new TEnvEvento();
            TEvento evn = new TEvento();
            InfEvento infEvn = new InfEvento();
            infEvn.setCOrgao(getCdUfDefault(connect));
            infEvn.setTpAmb(String.valueOf(getTipoAmbiente(rsNfe.getInt("cd_empresa"))));
            infEvn.setCNPJ(Util.limparFormatos(rsNfe.getString("nr_cnpj_emissor")));
            infEvn.setChNFe(rsNfe.getString("nr_chave_acesso").substring(3));
            infEvn.setDhEvento(Util.formatDate(Util.getDataAtual(), "yyyy-MM-dd'T'HH:mm:ss'-03:00'"));
            infEvn.setTpEvento("110111");
            infEvn.setNSeqEvento("1");
            infEvn.setVerEvento("1.00");
            infEvn.setId("ID110111" + rsNfe.getString("nr_chave_acesso").substring(3) + "01");
            DetEvento detEvn = new DetEvento();
            detEvn.setVersao("1.00");
            detEvn.setDescEvento("Cancelamento");
            detEvn.setNProt(rsNfe.getString("nr_protocolo_autorizacao"));
            detEvn.setXJust(((getTipoAmbiente(rsNfe.getInt("cd_empresa")) == HOMOLOGACAO) ? "Teste de cancelamento como evento" : (txtMotivo == null) ? "Sem Justificativa" : txtMotivo));
            infEvn.setDetEvento(detEvn);
            evn.setInfEvento(infEvn);
            evn.setVersao("1.00");
            evnEvento.getEvento().add(evn);
            evnEvento.setIdLote("1");
            evnEvento.setVersao("1.00");
            
            JAXBContext context   = JAXBContext.newInstance(TEnvEvento.class);  
            Marshaller marshaller = context.createMarshaller();  
            JAXBElement<TEnvEvento> element = new br.com.javac.v100.eventocanc.ObjectFactory().createEnvEvento(evnEvento);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);  
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);  
            StringWriter sw = new StringWriter();  
            marshaller.marshal(element, sw);  
            
            String xml = sw.toString(); 
            xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + xml;
            xml = xml.replaceAll("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\" ", "");  
            xml = xml.replaceAll("<evento", "<evento xmlns=\"http://www.portalfiscal.inf.br/nfe\"");
            xml = removeAcentos(xml);
            Result result = new Result(1);
            result.addObject("xml", xml);
            result.addObject("nrNfe", rsNfe.getString("nr_nota_fiscal"));
            
            if(isConnectionIsNull)
            	connect.commit();
            
            return result;
        } 
        catch (Exception e) {  
            error(e.toString());
            if(isConnectionIsNull)
            	Conexao.rollback(connect);
            e.printStackTrace(System.out);
            return new Result(-1, "Erro ao tentar gerar xml de cancelamento: ", e);
        }  
		
		finally{
			if (isConnectionIsNull)
				Conexao.desconectar(connect);
		}
    }
	public static Result gerarXmlNFECancProc(int cdNotaFiscal, int cdEmpresa, String txtMotivo, String[] retorno) {  
		return gerarXmlNFECancProc(cdNotaFiscal, cdEmpresa, txtMotivo, retorno, null);
	}
	
	public static Result gerarXmlNFECancProc(int cdNotaFiscal, int cdEmpresa, String txtMotivo, String[] retorno, Connection connect) {  
        boolean isConnectionIsNull = connect == null;
		try {
			if(isConnectionIsNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			ResultSetMap rsNfe = new ResultSetMap(connect.prepareStatement("SELECT A.*, "+
														"      B.nr_codigo_fiscal AS nr_cfop, C.nm_pessoa AS nm_fantasia, C.nr_telefone1,  "+
														"      D.nr_cnpj AS nr_cnpj_emissor, D.nm_razao_social, D.nr_inscricao_estadual, D.nr_inscricao_municipal, "+
														"      E.nm_logradouro, E.nm_bairro, E.nr_endereco, E.nr_cep, E.nm_complemento, "+
														"      F.nm_cidade, F.id_ibge AS id_ibge_cidade, G.sg_estado "+
														"FROM fsc_nota_fiscal A "+
														"JOIN adm_natureza_operacao B ON (A.cd_natureza_operacao = B.cd_natureza_operacao) "+
														"JOIN grl_pessoa            C ON (A.cd_empresa           = C.cd_pessoa) "+
														"JOIN grl_pessoa_juridica   D ON (A.cd_empresa           = D.cd_pessoa) "+
														"JOIN grl_pessoa_endereco   E ON (A.cd_empresa           = E.cd_pessoa  "+
														"                             AND E.lg_principal         = 1)           "+
														"JOIN grl_cidade            F ON (E.cd_cidade            = F.cd_cidade) "+
														"JOIN grl_estado            G ON (F.cd_estado            = G.cd_estado) "+
														"WHERE A.cd_nota_fiscal = " + cdNotaFiscal).executeQuery());
            if(!rsNfe.next()){
            	if(isConnectionIsNull)
            		Conexao.rollback(connect);
            	return new Result(-1, "Nota fiscal não localizada!");
            }
            
            TCancNFe cancNfe = new TCancNFe();
            cancNfe.setVersao("2.00");
            InfCanc infCanc = new InfCanc();
            infCanc.setId("ID" + rsNfe.getString("nr_chave_acesso").substring(3));
            infCanc.setTpAmb(String.valueOf(getTipoAmbiente(rsNfe.getInt("cd_empresa"))));
            infCanc.setXServ("CANCELAR");
            infCanc.setChNFe(rsNfe.getString("nr_chave_acesso").substring(3));
            infCanc.setNProt(rsNfe.getString("nr_protocolo_autorizacao"));
            infCanc.setXJust(((getTipoAmbiente(rsNfe.getInt("cd_empresa")) == HOMOLOGACAO) ? "Teste de cancelamento como evento" : (txtMotivo == null) ? "Sem Justificativa" : txtMotivo));
            cancNfe.setInfCanc(infCanc);
            
            br.com.javac.v200.cancnfe.TRetCancNFe.InfCanc infRetCanc = new br.com.javac.v200.cancnfe.TRetCancNFe.InfCanc();
            infRetCanc.setTpAmb(String.valueOf(getTipoAmbiente(rsNfe.getInt("cd_empresa"))));
            
            JAXBContext context   = JAXBContext.newInstance(TCancNFe.class);  
            Marshaller marshaller = context.createMarshaller();  
            JAXBElement<TCancNFe> element = new br.com.javac.v200.cancnfe.ObjectFactory().createCancNFe(cancNfe);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);  
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);  
            StringWriter sw = new StringWriter();  
            marshaller.marshal(element, sw);  
            
            String xml = sw.toString(); 
            
            
            Result assinResultado = null;
            if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3){
                assinResultado = assinarNFeA3(xml, NfeServices.CANCELAMENTO_PROC, cdEmpresa);
                if(assinResultado.getCode() <= 0)
                	return new Result(-1, "ERRO");
    		}
    		else{
    			assinResultado = assinarNFeA1(xml, NfeServices.CANCELAMENTO_PROC, cdEmpresa);
                if(assinResultado.getCode() <= 0)
                	return new Result(-1, "ERRO");
    		}
            xml = (String) assinResultado.getObjects().get("xmlAssinado");
            
            
//            xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + xml;
            xml = xml.replaceAll("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\" ", "");  
            xml = xml.substring(38);  
            StringBuilder sb = new StringBuilder();  
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");  
            sb.append("<procCancNFe versao=\"2.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">");  
            sb.append(xml);
            sb.append("<retCancNFe versao=\"2.00\">");
            sb.append("<infCanc Id=\""+retorno[8]+"\">");
            sb.append("<tpAmb>");
            sb.append(retorno[0]);
            sb.append("</tpAmb>");
            sb.append("<verAplic>");
            sb.append(retorno[1]);
            sb.append("</verAplic>");
            sb.append("<cStat>");
            sb.append(retorno[6]);
            sb.append("</cStat>");
            sb.append("<xMotivo>");
            sb.append(retorno[7]);
            sb.append("</xMotivo>");
            sb.append("<cUF>");
            sb.append(getCdUfDefault(connect));
            sb.append("</cUF>");
            sb.append("<chNFe>");
            sb.append(retorno[3]);
            sb.append("</chNFe>");
            sb.append("<dhRecbto>");
            sb.append(Util.formatDate(Util.convStringToCalendar2(retorno[2]), "yyyy-MM-dd'T'HH:mm:ss'-03:00'"));
            sb.append("</dhRecbto>");
            sb.append("<nProt>");
            sb.append(retorno[4]);
            sb.append("</nProt>");
            sb.append("</infCanc>");
            sb.append("</retCancNFe>");
            sb.append("</procCancNFe>");  
            xml = sb.toString();
            
            
            xml = removeAcentos(xml);
            Result result = new Result(1);
            result.addObject("xml", xml);
            result.addObject("nrNfe", rsNfe.getString("nr_nota_fiscal"));
            
            if(isConnectionIsNull)
            	connect.commit();
            return result;
        } 
        catch (Exception e) {  
            error(e.toString());
            if(isConnectionIsNull)
            	Conexao.rollback(connect);
            e.printStackTrace(System.out);
            return new Result(-1, "Erro ao tentar gerar xml de cancelamento: ", e);
        }  
		
		finally{
			if (isConnectionIsNull)
				Conexao.desconectar(connect);
		}
    }
	
	public static Result cancelarNFe(int cdNotaFiscal, int cdEmpresa, String txtMotivo, boolean isPDV){
		HashMap<String, Object> register = new HashMap<String, Object>();
		register.put("cdNotaFiscal", cdNotaFiscal);
		register.put("cdEmpresa", cdEmpresa);	
		register.put("txtMotivo", txtMotivo);
		
		ArrayList<HashMap<String, Object>> criterios = new ArrayList<HashMap<String, Object>>();
		criterios.add(register);
		return cancelarNFe(criterios);				
	}
	
	public static Result cancelarNFe(ArrayList<HashMap<String,Object>> registros){
		try {
			
			int i = 0;
			Result resultado = new Result(1, "Cancelamentos realizados com sucesso!");
			ResultSetMap rs = new ResultSetMap();
			for(i = 0; i < registros.size(); i++){
				int cdNotaFiscal    = registros.get(i)==null || registros.get(i).get("cdNotaFiscal")==null ? 0 : (Integer) registros.get(i).get("cdNotaFiscal");
				int cdEmpresa    	= registros.get(i)==null || registros.get(i).get("cdEmpresa")==null ? 0 : (Integer) registros.get(i).get("cdEmpresa");
				String txtMotivo 	= registros.get(i)==null || registros.get(i).get("txtMotivo")==null ? null : (String) registros.get(i).get("txtMotivo");
				Result retorno = cancelarNFe(cdNotaFiscal, cdEmpresa, txtMotivo);
				NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);
				if(retorno.getCode() <= 0){
					resultado.setMessage("Nem todas os cancelamentos foram realizados com sucesso");
				}
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NUMERO", nota.getNrNotaFiscal());
				register.put("RESULTADO", retorno.getMessage());
				rs.addRegister(register);
			}
			
			resultado.addObject("resultado", rs);
			
			if(i == 0){
				return new Result(-1, "Nenhuma nota foi cancelada!");
			}
			
			return resultado;
		} 
        catch (Exception e) {  
            error(e.toString());
            e.printStackTrace(System.out);
            return new Result(-1, "Erro ao tentar cancelar NFE: ", e);
        }  
		
	}
	
	public static Result cancelarNFe(int cdNotaFiscal, int cdEmpresa, String txtMotivo){
		return cancelarNFe(cdNotaFiscal, cdEmpresa, txtMotivo, null);
	}
	
	public static Result cancelarNFe(int cdNotaFiscal, int cdEmpresa, String txtMotivo, Connection connect){
		boolean isConnectionNull = connect == null;
		try {  
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			Result resultado = validarNFe(cdNotaFiscal, NfeServices.CANCELAMENTO, cdEmpresa, txtMotivo, connect);
			if(resultado.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return resultado;
			}
			String codigoDoEstado = getCdUfDefault(connect);  
			NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal, connect);
			
            URL url = new URL(getTipoAmbiente(cdEmpresa) == PRODUCAO ? "https://nfe.sefaz.ba.gov.br/webservices/sre/recepcaoevento.asmx" : 
            	                                                       "https://hnfe.sefaz.ba.gov.br/webservices/sre/recepcaoevento.asmx");
            
            if(nota.getTpEmissao() == NotaFiscalServices.EMI_CONTIGENCIA_SVC_AN)
            	url = new URL(getTipoAmbiente(cdEmpresa) == PRODUCAO ? "https://www.svc.fazenda.gov.br/RecepcaoEvento/RecepcaoEvento.asmx" : 
                        											   "https://hom.svc.fazenda.gov.br/RecepcaoEvento/RecepcaoEvento.asmx");
            else if(nota.getTpEmissao() == NotaFiscalServices.EMI_CONTIGENCIA_SVC_RS)
            	url = new URL(getTipoAmbiente(cdEmpresa) == PRODUCAO ? "https://nfe.sefazvirtual.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx" : 
                        											   "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx");

            else if(nota.getTpEmissao() != NotaFiscalServices.EMI_NORMAL){
            	if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Sistema não permite envio de nota para o tipo de emissão " + NotaFiscalServices.tiposNotaFiscal[nota.getTpEmissao()]);
            }
            String configName = ParametroServices.getValorOfParametro("NM_FILE_CERTIFICADO", "SmartCard.cfg", cdEmpresa);    
            String senhaDoCertificado = CertificadoServices.getPass(cdEmpresa);   
            String arquivoCacertsGeradoTodosOsEstados = CertificadoServices.getCacertsFile(cdEmpresa);    
            
            if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3){
	            Provider p = new sun.security.pkcs11.SunPKCS11(configName);    
	            Security.addProvider(p);    
	            char[] pin = senhaDoCertificado.toCharArray();    
	            KeyStore ks = KeyStore.getInstance("pkcs11");    
	            ks.load(null, pin);    
	            String alias = "";    
	            Enumeration<String> aliasesEnum = ks.aliases();    
	            while (aliasesEnum.hasMoreElements()) {    
	                alias = (String) aliasesEnum.nextElement();    
	                if (ks.isKeyEntry(alias)) break;    
	            }    
	            X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);    
	            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, senhaDoCertificado.toCharArray());    
	            SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(certificate, privateKey);    
	            socketFactoryDinamico.setFileCacerts(arquivoCacertsGeradoTodosOsEstados);    
	            Protocol protocol = new Protocol("https", socketFactoryDinamico, SSL_PORT);      
	            Protocol.registerProtocol("https", protocol);   
            }
            
            else{
            	InputStream entrada = new FileInputStream(configName);  
                KeyStore ks = KeyStore.getInstance("pkcs12");  
                try {  
                    ks.load(entrada, senhaDoCertificado.toCharArray());  
                } catch (IOException e) {  
                    throw new Exception("Senha do Certificado Digital esta incorreta ou Certificado inválido.");  
                }  
      
                /** 
                 * Resolve o problema do 403.7 Forbidden para Certificados A3 e A1  
                 * e elimina o uso das configurações: 
                 * - System.setProperty("javax.net.ssl.keyStore", "NONE"); 
                 * - System.setProperty("javax.net.ssl.keyStoreType", "PKCS11"); 
                 * - System.setProperty("javax.net.ssl.keyStoreProvider", "SunPKCS11-SmartCard"); 
                 * - System.setProperty("javax.net.ssl.trustStoreType", "JKS"); 
                 * - System.setProperty("javax.net.ssl.trustStore", arquivoCacertsGeradoTodosOsEstados); 
                 */  
                String alias = "";    
                Enumeration<String> aliasesEnum = ks.aliases();    
                while (aliasesEnum.hasMoreElements()) {    
                    alias = (String) aliasesEnum.nextElement();    
                    if (ks.isKeyEntry(alias)) break;    
                }  
                X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);  
                PrivateKey privateKey = (PrivateKey) ks.getKey(alias, senhaDoCertificado.toCharArray());  
                SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(certificate, privateKey);  
                socketFactoryDinamico.setFileCacerts(arquivoCacertsGeradoTodosOsEstados);  
      
                Protocol protocol = new Protocol("https", socketFactoryDinamico, SSL_PORT);    
                Protocol.registerProtocol("https", protocol);     
            	
            	
            }   
            
            /** 
             * IMPORTANTE: O XML já deve estar assinado antes do envio. 
             * Lendo o Xml de um arquivo Gerado. 
             */  
           
            String xmlAssinado = (String)resultado.getObjects().get("xmlAssinado");
            
            System.out.println("xmlAssinado = " + xmlAssinado);
            
            NotaFiscalHistorico notaHistorico = new NotaFiscalHistorico(cdNotaFiscal, 0, 0, null, 
								Util.getDataAtual(), null, null,  
								null,  null,  null,  "Envio de Cancelamento",  
								null, xmlAssinado.toString(), NotaFiscalHistoricoServices.ENVIO_CANCELAMENTO);
			
			if(NotaFiscalHistoricoDAO.insert(notaHistorico, connect) <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir NotaHistorico");
			}
            
            OMElement ome = AXIOMUtil.stringToOM(xmlAssinado);
            
            
            RecepcaoEventoStub.NfeDadosMsg dadosMsg = new RecepcaoEventoStub.NfeDadosMsg();  
            
            dadosMsg.setExtraElement(ome);  
            RecepcaoEventoStub.NfeCabecMsg nfeCabecMsg = new RecepcaoEventoStub.NfeCabecMsg();  
            /** 
             * Codigo do Estado. 
             */  
            nfeCabecMsg.setCUF(codigoDoEstado);  
  
            /** 
             * Versao do XML 
             */  
            nfeCabecMsg.setVersaoDados("1.00"); 
            
            RecepcaoEventoStub.NfeCabecMsgE nfeCabecMsgE = new RecepcaoEventoStub.NfeCabecMsgE();  
            nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);  
  
            RecepcaoEventoStub stub = new RecepcaoEventoStub(url.toString());  
            RecepcaoEventoStub.NfeRecepcaoEventoResult result = stub.nfeRecepcaoEvento(dadosMsg, nfeCabecMsgE);  
            
            String xml = result.getExtraElement().toString();
            
            String[] rs = getRetornoCancelamento(xml);
            if(rs == null)
            	return new Result(-1, "Erro no Retorno do Cancelamento!");
            
            resultado.setMessage(rs[7]);
            if(rs[6].equals("135")){
            	int cdMotivoCancelamento = MotivoCancelamentoDAO.insert(new MotivoCancelamento(0, txtMotivo, ""), connect);
            	if(cdMotivoCancelamento <= 0){
                	if(isConnectionNull)
        				Conexao.rollback(connect);
                	return new Result(-1, "Erro ao inserir motivo de cancelamento da nota!");
                }
            	
            	nota.setStNotaFiscal(NotaFiscalServices.CANCELADA);
                nota.setCdMotivoCancelamento(cdMotivoCancelamento);
                if(NotaFiscalDAO.update(nota, connect) <= 0){
                	if(isConnectionNull)
        				Conexao.rollback(connect);
                	return new Result(-1, "Erro ao atualizar nota após Cancelamento!");
                }
                
                File arquivo = new File(nota.getNrChaveAcesso() + ".xml"); 
				FileOutputStream gravar;
				
				String caminhoPadrao = ParametroServices.getValorOfParametro("NM_REPOSITORIO_XML", cdEmpresa, connect);
				
				if(caminhoPadrao == null || caminhoPadrao.trim().equals("")){
					connect.rollback();
					return new Result(-1, "Parametro de Caminho Padrão XML não esta configurado!");
				}
                
                String caminhoPasta = caminhoPadrao + "\\" + nota.getDtAutorizacao().get(Calendar.YEAR) + "_" + (nota.getDtAutorizacao().get(Calendar.MONTH) + 1);
				
				File diretorio = new File(caminhoPasta);
				if(!diretorio.exists()){
					diretorio.mkdir();
				}	
                
                arquivo = new File("NFeCanc" + nota.getNrChaveAcesso().substring(3) + ".xml"); 
				
				gravar = new FileOutputStream(caminhoPasta + "\\" + arquivo);
				
				Result resultado2     = gerarXmlNFECancProc(cdNotaFiscal, cdEmpresa, txtMotivo, rs, connect);
        		String xmlCanc = (String) resultado2.getObjects().get("xml");
        		if(xmlCanc == null){
        			if (isConnectionNull)
        				Conexao.rollback(connect);
        			return resultado;
        		}
        		
				gravar.write(xmlCanc.getBytes());
				gravar.close();
				GregorianCalendar dataRec = Util.convStringToCalendar2(rs[2]);
				notaHistorico = new NotaFiscalHistorico(cdNotaFiscal, 0, Integer.parseInt(rs[0]), rs[1], 
									Util.getDataAtual(), dataRec, rs[3],  
									rs[4],  rs[5],  rs[6],  rs[7],  
									rs[8], xmlCanc, NotaFiscalHistoricoServices.CANCELAMENTO_HOMOLOGADO);
				
				if(NotaFiscalHistoricoDAO.insert(notaHistorico, connect) <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao inserir NotaHistorico");
				}
                
            }
            GregorianCalendar dataRec = Util.convStringToCalendar2(rs[2]);
            notaHistorico = new NotaFiscalHistorico(cdNotaFiscal, 0, Integer.parseInt(rs[0]), rs[1], 
																		Util.getDataAtual(), dataRec, rs[3],  
																		rs[4],  rs[5],  rs[6],  "Resposta da SEFAZ para pedido de cancelamento",  
																		rs[8], xml.toString(), NotaFiscalHistoricoServices.RESPOSTA_CANCELAMENTO);

			if(NotaFiscalHistoricoDAO.insert(notaHistorico, connect) <= 0){
				if(isConnectionNull)
    				Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir NotaHistorico");
			}
			
            if(isConnectionNull){
            	connect.commit();
            }
            
            return resultado;
        }
		catch (ConnectException e) {  
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);  
			return new Result(-1, "Problema de Conexão com a internet");
		} 
		catch (Exception e) {
			if(isConnectionNull)
				Conexao.rollback(connect);
            e.printStackTrace(System.out);
            return new Result(-1, "Erro cancelar: " + e);
        }  
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static String[] getRetornoCancelamento(String xml){
		
		try {
			System.out.println("xml = " + xml);
			SAXBuilder saxObj     = new SAXBuilder();
			org.jdom.Document doc = saxObj.build(new StringReader(xml));
			Namespace ns          = Namespace.getNamespace("http://www.portalfiscal.inf.br/nfe");
	    	Element cStat    	  = (Element)doc.getRootElement().getChild("retEvento", ns).getChild("infEvento", ns).getChild("cStat", ns);
	    	Element cStat2    	  = (Element)doc.getRootElement().getChild("cStat", ns);
	    	Element xEvento   	  = (Element)doc.getRootElement().getChild("retEvento", ns).getChild("infEvento", ns).getChild("xEvento", ns);
	    	Element xMot   	  	  = (Element)doc.getRootElement().getChild("retEvento", ns).getChild("infEvento", ns).getChild("xMotivo", ns);
	    	Element tpAmb   	  = (Element)doc.getRootElement().getChild("tpAmb", ns);
	    	Element verAplic   	  = (Element)doc.getRootElement().getChild("verAplic", ns);
	    	Element dhRecbto 	  = (Element)doc.getRootElement().getChild("retEvento", ns).getChild("infEvento", ns).getChild("dhRegEvento", ns);
	    	Element chNFe 	  	  = (Element)doc.getRootElement().getChild("retEvento", ns).getChild("infEvento", ns).getChild("chNFe", ns);
	    	Element nProt 	  	  = (Element)doc.getRootElement().getChild("retEvento", ns).getChild("infEvento", ns).getChild("nProt", ns);
	    	List<Attribute> id    = (List<Attribute>)doc.getRootElement().getChild("retEvento", ns).getChild("infEvento", ns).getAttributes();
	    	
	    	String[] retorno = new String[9];
	    		    	
	    	retorno[0] = (tpAmb != null) ? tpAmb.getValue() : "";
	    	retorno[1] = (verAplic != null) ? verAplic.getValue().substring(0, 9) : "";
	    	retorno[2] = (dhRecbto != null) ? dhRecbto.getValue().substring(0, 19) : "";
	    	retorno[3] = (chNFe != null) ? chNFe.getValue() : "";
	    	retorno[4] = (nProt != null) ? nProt.getValue() : "";
	    	retorno[5] = "";
	    	retorno[6] = ((cStat != null) ? cStat.getValue() : cStat2.getValue());
	    	retorno[7] = ((xEvento != null) ? xEvento.getValue() : xMot.getValue());
	    	retorno[8] = "";

	    	return retorno;
		} 
		catch (Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	/**
	 * Inutiliza as faixas de númeração não utilizadas pela empresa no mês anterior
	 * 
	 * @param cdEmpresa
	 * @return
	 * @author Luiz Romario Filho
	 * @since 12/05/2015
	 */
	public static Result inutilizacaoDeNumeracaoNFe(int cdEmpresa){
		Connection connection = Conexao.conectar();
		ArrayList<HashMap<String, Object>> lista = new ArrayList<HashMap<String, Object>>();
		Result result = null;
		try{
			result = buscarFaixasInutilizacao(cdEmpresa, connection);
			ResultSetMap rsmFinal = (ResultSetMap) result.getObjects().get("rsmFinal");
			lista = rsmFinal.getLines(); 
			String xml = null;
			String xmlRetorno = null;
			for (HashMap<String, Object> hashMap : lista) {
				hashMap.put("CNPJ_EMISSOR",getCnpjEmissor(cdEmpresa, connection));
				xml = gerarXmlNFEInutilizacao(hashMap, cdEmpresa, connection);
				List<String> list = validarXmlInutilizacao(xml,cdEmpresa, connection);
				if(list == null || list.isEmpty()){
					Result resultEnvio = enviarXmlInutilizacao(xml, cdEmpresa, connection);
					if(resultEnvio.getCode() > 0){
						xmlRetorno = (String) resultEnvio.getObjects().get("retorno");
						result = processarRetorno(xmlRetorno, connection);
					} else {
						return resultEnvio;
					}
				} else {
					String error = "";
					for (String string : list) {
						error+=string + "\n";
					}
					return new Result(-1, "Erro ao validar xml de inutilização.\n" + error);
				}
			}
			return result;
		}catch (Exception e) {  
            e.printStackTrace(System.out);
            return new Result(-1, "Erro");
        }  
	}


	/**
	 * Busca o cnpj do emissor de notas fiscais
	 * @param cdEmpresa
	 * @return
	 * @author Luiz Romario Filho
	 */
	private static String getCnpjEmissor(int cdEmpresa, Connection connection) {
		String sql = "SELECT * FROM grl_pessoa_juridica WHERE cd_pessoa = ? ";
		String cnpj = "";
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, cdEmpresa);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()){
				cnpj = rsm.getString("NR_CNPJ");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return cnpj;
	}

	private static Result processarRetorno(String xmlRetorno, Connection connection) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Envia um xml para inutlização de uma faixa de numeração de notas fiscais
	 * 
	 * @param xml
	 * @param connection
	 * @return
	 * @author Luiz Romario Filho
	 * 
	 */
	private static Result enviarXmlInutilizacao(String xml, int cdEmpresa, Connection connection) {
		URL url;
		String retorno = null;
		Result resultado = null;
		try {
			
			String configName = ParametroServices.getValorOfParametro("NM_FILE_CERTIFICADO", "SmartCard.cfg", cdEmpresa);    
            String senhaDoCertificado = CertificadoServices.getPass(cdEmpresa);   
            String arquivoCacertsGeradoTodosOsEstados = CertificadoServices.getCacertsFile(cdEmpresa);    
            
            if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3){
	            Provider p = new sun.security.pkcs11.SunPKCS11(configName);    
	            Security.addProvider(p);    
	            char[] pin = senhaDoCertificado.toCharArray();    
	            KeyStore ks = KeyStore.getInstance("pkcs11");    
	            ks.load(null, pin);    
	            String alias = "";    
	            Enumeration<String> aliasesEnum = ks.aliases();    
	            while (aliasesEnum.hasMoreElements()) {    
	                alias = (String) aliasesEnum.nextElement();    
	                if (ks.isKeyEntry(alias)) break;    
	            }    
	            X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);    
	            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, senhaDoCertificado.toCharArray());    
	            SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(certificate, privateKey);    
	            socketFactoryDinamico.setFileCacerts(arquivoCacertsGeradoTodosOsEstados);    
	            Protocol protocol = new Protocol("https", socketFactoryDinamico, SSL_PORT);      
	            Protocol.registerProtocol("https", protocol);   
            }
            
            else{
            	InputStream entrada = new FileInputStream(configName);  
                KeyStore ks = KeyStore.getInstance("pkcs12");  
                try {  
                    ks.load(entrada, senhaDoCertificado.toCharArray());  
                } catch (IOException e) {  
                    throw new Exception("Senha do Certificado Digital esta incorreta ou Certificado inválido.");  
                }  		
            }
		
			url = new URL(getTipoAmbiente(cdEmpresa) == PRODUCAO ? "https://nfe.sefaz.ba.gov.br/webservices/sre/recepcaoevento.asmx" : 
					"https://hnfe.sefaz.ba.gov.br/webservices/sre/recepcaoevento.asmx");
		
			String codigoDoEstado = getCdUfDefault(connection);  
			
			OMElement ome = AXIOMUtil.stringToOM(xml);
	        
	        RecepcaoEventoStub.NfeDadosMsg dadosMsg = new RecepcaoEventoStub.NfeDadosMsg();  
	        
	        dadosMsg.setExtraElement(ome);  
	        RecepcaoEventoStub.NfeCabecMsg nfeCabecMsg = new RecepcaoEventoStub.NfeCabecMsg();  
	        /** 
	         * Codigo do Estado. 
	         */  
	        nfeCabecMsg.setCUF(codigoDoEstado);  
	
	        /** 
	         * Versao do XML 
	         */  
	        nfeCabecMsg.setVersaoDados("1.00"); 
	        
	        RecepcaoEventoStub.NfeCabecMsgE nfeCabecMsgE = new RecepcaoEventoStub.NfeCabecMsgE();  
	        nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);  
	
	        RecepcaoEventoStub stub = new RecepcaoEventoStub(url.toString());  
	        RecepcaoEventoStub.NfeRecepcaoEventoResult result = stub.nfeRecepcaoEvento(dadosMsg, nfeCabecMsgE);  
	        
			retorno = result.getExtraElement().toString();
			resultado = new Result(1, "");
			resultado.addObject("retorno", retorno);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao montar url de envio.");
		} catch (XMLStreamException e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao processar xml de envio.");
		} catch (AxisFault e) {
			e.printStackTrace();
			return new Result(-1, "Erro no webservice de envio.");
		} catch (RemoteException e) {
			e.printStackTrace();
			return new Result(-1, "Erro remoto.");
//		} catch (KeyStoreException e) {
//			e.printStackTrace();
//			return new Result(-1,"Erro ao carregar chave do certificado.");
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (CertificateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro genérico.");
		}
		
		return resultado;
	}
	
	/**
	 * Assina um xml de inutilização
	 * @param xml
	 * @param cdEmpresa
	 * @param connection
	 * @return
	 */
	private static String assinarXmlInutilizacao(String xml, int cdEmpresa, Connection connection) {
		String xmlAssinado = "";
		if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3){
            Result assinarNFeA3 = assinarNFeA3(xml, NfeServices.INUTILIZACAO, cdEmpresa);
            if(assinarNFeA3.getCode() <= 0){
    			return null;
    		} else {
    			xmlAssinado = (String) assinarNFeA3.getObjects().get("xmlAssinado");
    		}
		} else{
			Result assinarNFeA1 = assinarNFeA1(xml, NfeServices.INUTILIZACAO, cdEmpresa);
            if(assinarNFeA1.getCode() <= 0){
            	return null;
    		} else {
    			 xmlAssinado = (String) assinarNFeA1.getObjects().get("xmlAssinado");
    		}
		}
		return xmlAssinado;
	}

	/**
	 * Valida um xml utilizando o xsd de inutilização de faixa de números de nota fiscal
	 * @param xml
	 * @param cdEmpresa
	 * @param connection
	 * @return
	 */
	private static List<String> validarXmlInutilizacao(String xml, int cdEmpresa, Connection connection) {
        String xmlAssinado = assinarXmlInutilizacao(xml, cdEmpresa, connection);
		String xsd = ParametroServices.getValorOfParametro("NM_FILE_XSD_INUTILIZACAO", "c:/TIVIC/xsd/inutNFe_v2.00.xsd", cdEmpresa, connection);
		List<String> list = validateXml(xmlAssinado, xsd);
		return list;
	}

	/**
	 * Gera xml para inutilização de uma faixa de números de nota fiscal
	 * @param hashMap
	 * @param connection
	 * @return
	 */
	private static String gerarXmlNFEInutilizacao(HashMap<String, Object> hashMap, int cdEmpresa, Connection connection) {
		TInutNFe nfe = new TInutNFe();
		
		String cdUfDefault = getCdUfDefault(connection);
		String modelo = "55";
		String ano = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2);
		String cnpjEmissor = (String) hashMap.get("CNPJ_EMISSOR");
		String serie = (String) hashMap.get("NR_SERIE");
		String nrInicial = String.valueOf(hashMap.get("NR_INICIO"));
		String nrFinal = String.valueOf(hashMap.get("NR_FIM"));
		String txtJustificativa = ParametroServices.getValorOfParametro("TXT_JUSTIFICATIVA_INUTILIZACAO","Inutilização de números da NFe", cdEmpresa, connection);
		
		
		String id = "ID" + cdUfDefault + ano + cnpjEmissor + modelo + preencheZero(serie,3) + preencheZero(nrInicial,9) + preencheZero(nrFinal,9);
		
    	InfInut infInut = new InfInut();
    	
    	infInut.setId(id);
    	infInut.setTpAmb(String.valueOf(NotaFiscalServices.getTipoAmbiente(cdEmpresa)));//Homologação
    	infInut.setXServ("INUTILIZAR");
		infInut.setCUF(cdUfDefault);
    	infInut.setAno(ano);
		infInut.setCNPJ(cnpjEmissor);//Lembrando que deve ser o mesmo do Certificado
		infInut.setMod(modelo);
    	infInut.setSerie(serie);
		infInut.setNNFIni(nrInicial); // Numero inicial que será inutilizado
		infInut.setNNFFin(nrFinal); // Numero final que será inutilizado
		infInut.setXJust(txtJustificativa);
    	
    	nfe.setVersao("3.10");
    	nfe.setInfInut(infInut);
    	
        JAXBContext context;
        StringWriter sw = new StringWriter();  
		try {
			context = JAXBContext.newInstance(TInutNFe.class);
			Marshaller marshaller = context.createMarshaller();
			JAXBElement<TInutNFe> element = new inutNFe_v310.ObjectFactory().createInutNFe(nfe);
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);  
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);  
			marshaller.marshal(element, sw);  
		} catch (JAXBException e) {
			e.printStackTrace();
		}  
        
        String xml = sw.toString(); 
        xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + xml;
        xml = xml.replaceAll("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\" ", "");  
        xml = xml.replaceAll("<NFe>", "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
        xml = removeAcentos(xml);
		return xml;
	}

	/**
	 * Preenche um número com zeros a esquerda
	 * 
	 * @param nrInicial
	 * @return
	 */
	private static String preencheZero(String nr, int total) {
		while(nr.length() < total){
			nr = "0"+ nr;
		}
		return nr;
	}

	/**
	 * Busca as faixas de números que serão inutilizados
	 * 
	 * @param cdEmpresa
	 * @param connection
	 * @return
	 */
	private static Result buscarFaixasInutilizacao(int cdEmpresa, Connection connection) {
			String sql = "SELECT CAST (nr_nota_fiscal AS INTEGER), nr_serie "
					+ " FROM fsc_nota_fiscal "
					+ " WHERE dt_emissao >= ? AND dt_emissao <= ? "
					+ "	AND cd_empresa = ? " 
					+ " ORDER BY CAST(nr_nota_fiscal AS NUMERIC) ASC";
			Result result = null;
			ResultSetMap rsmFinal = new ResultSetMap();
			try {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				
				
				pstmt.setDate(1, new Date(calendar.getTimeInMillis()));
				pstmt.setDate(2, new Date(Util.getUltimoDiaMes(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)).getTimeInMillis()));
				pstmt.setInt(3, cdEmpresa);
				
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				
				ArrayList<HashMap<String, Object>> list = processaFaixaInutilizacao(rsm);
				rsmFinal.setLines(list);
				
			} catch (SQLException e) {
				e.printStackTrace();
				return new Result(-1, "Erro ao buscar a faixa de números inutilizáveis.");
			}
			result = new Result(1, "");
			HashMap<String,Object> map = new HashMap<String, Object>();
			map.put("rsmFinal", rsmFinal);
			result.setObjects(map);
		return result;
	}

	/**
	 * Retorna uma lista de faixas a partir de um rsm
	 * 
	 * @param rsm
	 * @return
	 * @author Luiz Romario Filho
	 * @since 14/05/2015
	 */
	private static ArrayList<HashMap<String, Object>> processaFaixaInutilizacao(ResultSetMap rsm) {
		int nrNotaFiscal;
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		while(rsm.next()){
			nrNotaFiscal = rsm.getInt("NR_NOTA_FISCAL");
			if(rsm.next()){
				if(rsm.getInt("NR_NOTA_FISCAL") - nrNotaFiscal  > 1){
					HashMap<String, Object> hash = new HashMap<String, Object>();
					hash.put("NR_INICIO", nrNotaFiscal);
					hash.put("NR_FIM", rsm.getInt("NR_NOTA_FISCAL"));
					hash.put("NR_SERIE", rsm.getString("NR_SERIE"));
					list.add(hash);
				}
				rsm.previous();						
			}
		}
		return list;
	}
	
	public static String consultaCadastro(String nrCpfCnpj, String nrUfConsulta, int cdEmpresa) {  
        try {  
            /** 
             * CNPJ Sem formatacao. Caso esta formatado 
             * retornara Falha de Schema XML. 
             */                
            URL url = new URL(getTipoAmbiente(cdEmpresa) == PRODUCAO ? "https://nfe.sefaz.ba.gov.br/webservices/nfenw/CadConsultaCadastro2.asmx" : 
            	                                                       "https://hnfe.sefaz.ba.gov.br/webservices/nfenw/CadConsultaCadastro2.asmx");  
            System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
            CertificadoServices.setInformacaoCertificado(NfeServices.getCdUfDefault(), cdEmpresa);
            /** 
             * Xml de Consulta. 
             */  
            StringBuilder xml = new StringBuilder();  
            xml.append("<nfeDadosMsg>")  
                .append("<ConsCad versao=\"2.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">")   
                .append("<infCons>")  
                .append("<xServ>CONS-CAD</xServ>")  
                .append("<UF>")  
                .append("BA")  
                .append("</UF>")  
                .append("<CNPJ>")  
                .append(nrCpfCnpj)  
                .append("</CNPJ>")   
                .append("</infCons>")  
                .append("</ConsCad>")  
                .append("</nfeDadosMsg>");  
              
            XMLStreamReader dadosXML = XMLInputFactory.newInstance().createXMLStreamReader(new java.io.StringReader(xml.toString()));   
            CadConsultaCadastro2Stub.NfeDadosMsg dadosMsg = CadConsultaCadastro2Stub.NfeDadosMsg.Factory.parse(dadosXML);  
  
            CadConsultaCadastro2Stub.NfeCabecMsg nfeCabecMsg = new CadConsultaCadastro2Stub.NfeCabecMsg();  
            /** 
             * Codigo do Estado. 
             */  
            nfeCabecMsg.setCUF(nrUfConsulta);  
  
            /** 
             * Versao do XML 
             */  
            nfeCabecMsg.setVersaoDados("1.00");  
            CadConsultaCadastro2Stub.NfeCabecMsgE nfeCabecMsgE = new CadConsultaCadastro2Stub.NfeCabecMsgE();  
            nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);  
  
            CadConsultaCadastro2Stub stub = new CadConsultaCadastro2Stub(url.toString());  
            CadConsultaCadastro2Stub.ConsultaCadastro2Result result = stub.consultaCadastro2(dadosMsg, nfeCabecMsgE);  
  
            return result.getExtraElement().toString();
        } 
        catch (Exception e) {  
            e.printStackTrace(System.out);
            return "ERRO";
        }
    }
    
    /** 
     * Info. 
     * @param log 
     */  
    private static void info(String log) {  
        System.out.println("INFO: " + log);  
    }  
  
    /** 
     * Error. 
     * @param log 
     */  
    private static void error(String log) {  
        System.out.println("ERROR: " + log);  
   	}
    
    public static String gerarConsultaNFe(int cdEmpresa, String chave) {  
    	try {  
            /** 
             * 1) codigoDoEstado = Código do Estado conforme tabela IBGE. 
             * 
             * 2) url = Endereço do WebService para cada Estado. 
             *       Ver relação dos endereços em: 
             *       Para Homologação: http://hom.nfe.fazenda.gov.br/PORTAL/WebServices.aspx 
             *       Para Produção: http://www.nfe.fazenda.gov.br/portal/WebServices.aspx 
             * 
             * 3) caminhoDoCertificadoDoCliente = Caminho do Certificado do Cliente (A1). 
             * 
             * 4) senhaDoCertificadoDoCliente = Senha do Certificado A1 do Cliente. 
             * 
             * 5) arquivoCacertsGeradoParaCadaEstado = Arquivo com os Certificados necessarios para 
             * acessar o WebService. Pode ser gerado com a Classe NFeBuildCacerts. 
             * 
             * 6) Chave de Acesso da NFe; 
             */  
            String codigoDoEstado = NfeServices.getCdUfDefault();  
            /** 
             * Enderecos de Homologação do Sefaz Virtual RS 
             * para cada WebService existe um endereco Diferente. 
             */  
            
            URL url = new URL(getTipoAmbiente(cdEmpresa) == PRODUCAO ? "https://nfe.sefaz.ba.gov.br/webservices/nfenw/NfeConsulta2.asmx" : 
            														   "https://hnfe.sefaz.ba.gov.br/webservices/nfenw/NfeConsulta2.asmx");  
            
            String configName      = ParametroServices.getValorOfParametro("NM_FILE_CERTIFICADO", "SmartCard.cfg", cdEmpresa);    
            String passCertificado = CertificadoServices.getPass(cdEmpresa);  
            String arquivoCacertsGeradoTodosOsEstados = CertificadoServices.getCacertsFile(cdEmpresa);    
            
            if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3){
            	Provider p = new sun.security.pkcs11.SunPKCS11(configName);    
	            Security.addProvider(p);    
	            char[] pin = passCertificado.toCharArray();    
	            KeyStore ks = KeyStore.getInstance("pkcs11");    
	            ks.load(null, pin);    
	            String alias = "";    
	            Enumeration<String> aliasesEnum = ks.aliases();    
	            while (aliasesEnum.hasMoreElements()) {    
	                alias = (String) aliasesEnum.nextElement();    
	                if (ks.isKeyEntry(alias)) break;    
	            }    
	            X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);    
	            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, passCertificado.toCharArray());    
	            SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(certificate, privateKey);    
	            socketFactoryDinamico.setFileCacerts(arquivoCacertsGeradoTodosOsEstados);    
	            Protocol protocol = new Protocol("https", socketFactoryDinamico, SSL_PORT);      
	            Protocol.registerProtocol("https", protocol);  
            }
            else{
//	            /** 
//	            * Informações do Certificado Digital. 
//	            */  
//	            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");  
//	            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());  
//	            System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");  
//		        System.setProperty("javax.net.ssl.keyStore", configName);  
//		        System.setProperty("javax.net.ssl.keyStorePassword", passCertificado);  
//		        System.setProperty("javax.net.ssl.trustStoreType", "JKS");  
//   	            System.setProperty("javax.net.ssl.trustStore", arquivoCacertsGeradoTodosOsEstados);  
            	
            	
            	InputStream entrada = new FileInputStream(configName);  
                KeyStore ks = KeyStore.getInstance("pkcs12");  
                try {  
                    ks.load(entrada, passCertificado.toCharArray());  
                } catch (IOException e) {  
                    throw new Exception("Senha do Certificado Digital esta incorreta ou Certificado inválido.");  
                }  
      
                /** 
                 * Resolve o problema do 403.7 Forbidden para Certificados A3 e A1  
                 * e elimina o uso das configurações: 
                 * - System.setProperty("javax.net.ssl.keyStore", "NONE"); 
                 * - System.setProperty("javax.net.ssl.keyStoreType", "PKCS11"); 
                 * - System.setProperty("javax.net.ssl.keyStoreProvider", "SunPKCS11-SmartCard"); 
                 * - System.setProperty("javax.net.ssl.trustStoreType", "JKS"); 
                 * - System.setProperty("javax.net.ssl.trustStore", arquivoCacertsGeradoTodosOsEstados); 
                 */  
                String alias = "";    
                Enumeration<String> aliasesEnum = ks.aliases();    
                while (aliasesEnum.hasMoreElements()) {    
                    alias = (String) aliasesEnum.nextElement();    
                    if (ks.isKeyEntry(alias)) break;    
                }  
                X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);  
                PrivateKey privateKey = (PrivateKey) ks.getKey(alias, passCertificado.toCharArray());  
                SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(certificate, privateKey);  
                socketFactoryDinamico.setFileCacerts(arquivoCacertsGeradoTodosOsEstados);  
      
                Protocol protocol = new Protocol("https", socketFactoryDinamico, SSL_PORT);    
                Protocol.registerProtocol("https", protocol); 
            	
           }	 
  
            /** 
             * Xml de Consulta. 
             */  
            StringBuilder xml = new StringBuilder();  
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")  
                .append("<consSitNFe versao=\"2.01\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">")  
                .append("<tpAmb>"+getTipoAmbiente(cdEmpresa)+"</tpAmb>")  
                .append("<xServ>CONSULTAR</xServ>")  
                .append("<chNFe>")  
                .append(chave)  
                .append("</chNFe>")  
                .append("</consSitNFe>");  
  
            
            
           OMElement ome = AXIOMUtil.stringToOM(xml.toString());  
            NfeConsulta2Stub.NfeDadosMsg dadosMsg = new NfeConsulta2Stub.NfeDadosMsg();  
            dadosMsg.setExtraElement(ome);  
            NfeConsulta2Stub.NfeCabecMsg nfeCabecMsg = new NfeConsulta2Stub.NfeCabecMsg();  
            nfeCabecMsg.setCUF(codigoDoEstado);  
            nfeCabecMsg.setVersaoDados("3.10");  
            NfeConsulta2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeConsulta2Stub.NfeCabecMsgE();  
            nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);  
            NfeConsulta2Stub stub = new NfeConsulta2Stub(url.toString());  
            NfeConsulta2Stub.NfeConsultaNF2Result result = stub.nfeConsultaNF2(dadosMsg, nfeCabecMsgE);  
  
            return result.getExtraElement().toString();  
        } 
    	catch (Exception e) {  
            e.printStackTrace(); 
            return "Erro: " + e;
        }  
    }  

	public static String gerarAllNfeXml(int cdEmpresa){
		Connection connect = Conexao.conectar();		
		try {  
			connect.setAutoCommit(false);
			
			ResultSetMap rsmNotas = NotaFiscalDAO.getAll();
			
			while(rsmNotas.next()){
				NotaFiscal nota = NotaFiscalDAO.get(rsmNotas.getInt("cd_nota_fiscal"), connect);
				
				if(nota.getStNotaFiscal() != NotaFiscalServices.AUTORIZADA && nota.getStNotaFiscal() != NotaFiscalServices.CANCELADA)
					continue;
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_nota_fiscal", "" + nota.getCdNotaFiscal(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmHist = NotaFiscalHistoricoDAO.find(criterios, connect);
				if(rsmHist.next())
					continue;
				NfeServices.retornoProcessamento(nota.getCdNotaFiscal(), cdEmpresa);
			}
			connect.commit();
			
			return "Correto";
		} 
		catch (Exception e) {  
			Conexao.rollback(connect);
            e.printStackTrace(System.out);
            return "Erro: " + e.getMessage();
        } 

		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static Result tarefasNFe(int cdNotaFiscal, int cdEmpresa, int lgSelecionado){
    	try{			
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("cdNotaFiscal", cdNotaFiscal);
			register.put("cdEmpresa", cdEmpresa);	
			register.put("lgSelecionado", lgSelecionado);
			
			ArrayList<HashMap<String, Object>> criterios = new ArrayList<HashMap<String, Object>>();
			criterios.add(register);
			tarefasNFe(criterios);							
    	}
    	catch(Exception e){
    		Util.registerLog(e);
    	}
		return null;
    }
	
	public static Result tarefasNFe(ArrayList<HashMap<String,Object>> registros){
		try	 {
			
			//Marcam a quantidade de notas que foram transmitidas com sucesso e erros
			int qtErro = 0, qtSucesso = 0;
			Result result     = new Result(1, "Tarefas realizadas com sucesso!");
			
			//Guarda os resultados para cada nota
			ResultSetMap rsm  = new ResultSetMap();
			
			
			//Rotina para enviar as notas que foram emitidas via DPEC, para que sejam emitidas novamente no modo normal
			Connection conexao = Conexao.conectar();
			
			PreparedStatement pstmt = conexao.prepareStatement("SELECT * FROM " +
															  "		fsc_nota_fiscal " +
															  " WHERE st_nota_fiscal = " + NotaFiscalServices.AUTORIZADA +
															  " AND tp_emissao = " + NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC);
			
			ResultSetMap rsmNotaDpec = new ResultSetMap(pstmt.executeQuery());
			while(rsmNotaDpec.next()){
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("cdNotaFiscal", rsmNotaDpec.getInt("cd_nota_fiscal"));
				register.put("cdEmpresa", rsmNotaDpec.getInt("cd_empresa"));
				boolean existe = false;
				for(int i = 0; i < registros.size(); i++){
					if(registros.get(i).get("cdNotaFiscal")!=null && ((Integer)registros.get(i).get("cdNotaFiscal")) != rsmNotaDpec.getInt("cd_nota_fiscal")){
						existe = true;
						break;
					}
				}
				
				if(!existe)
					registros.add(register);
				
			}
			
			Conexao.desconectar(conexao);
			
			//-----------------------------------------------------------------------------------------------------------------
			
			//Itera sobre os registros que possuem as notas a serem autorizadas, cada uma terão um retorno diferente
			for(int i = 0; i < registros.size(); i++){
				
				Connection connect = Conexao.conectar();
				
				try{
					connect.setAutoCommit(false);
				
					int cdNotaFiscal = registros.get(i)==null || registros.get(i).get("cdNotaFiscal")==null ? 0 : (Integer) registros.get(i).get("cdNotaFiscal");
					int cdEmpresa    = registros.get(i)==null || registros.get(i).get("cdEmpresa")==null ? 0 : (Integer) registros.get(i).get("cdEmpresa");
					int lgSelecionado= registros.get(i)==null || registros.get(i).get("lgSelecionado")==null ? 0 : (Integer) registros.get(i).get("lgSelecionado");
					/*
					 * Validando nota fiscal
					 */
					NotaFiscal nota  = NotaFiscalDAO.get(cdNotaFiscal, connect);
					Result retorno   = null;
					
					
					//Coloca o numero da nota fiscal que é a proxima da serie levando em consideracao autorizadas e canceladas e denegadas
					if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa)==1){
						String nrNotaFiscal = NotaFiscalServices.getNrNotaFiscalAtual(nota.getNrSerie(), connect);
						nota.setNrNotaFiscal(nrNotaFiscal);
						//Atualizacao da chave e do DV também pois eles dependem do numero da nota
		    			Result resultId = NotaFiscalServices.gerarNfeId(nota);
		    			nota.setNrChaveAcesso((String)resultId.getObjects().get("nrChave"));
		    			nota.setNrDv((Integer)resultId.getObjects().get("nrDv"));
						NotaFiscalDAO.update(nota, connect);
					}
					
					//Apenas notas selecionadas seráo validadas, para os outros processos seráo feitos independente da seleção
					if(lgSelecionado == 1 && nota.getStNotaFiscal() == NotaFiscalServices.EM_DIGITACAO){					
						//Busca validar a nota
						retorno   = validarNFe(cdNotaFiscal, NfeServices.ENVIO, cdEmpresa, connect);
						if(retorno.getCode() <= 0){
							
							Conexao.rollback(connect);
							
							//Caso o parametro de gerar a nota na autorização estiver ativo, irá retornar o numero da nota para nulo
							if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa, connect)==1){
								nota.setNrNotaFiscal(null);
								nota.setNrChaveAcesso(null);
				            	nota.setNrDv(0);
								NotaFiscalDAO.update(nota);
							}
							
							qtErro++;
							
							/*
							 * Registrando o LOG
							 */
							HashMap<String, Object> register = new HashMap<String, Object>();
							register.put("NUMERO", nota.getNrNotaFiscal());
							register.put("RESULTADO", retorno.getMessage());
							rsm.addRegister(register);
							
							continue;
						}
					}
					
					nota  = NotaFiscalDAO.get(cdNotaFiscal, connect);
					
					/*
					 * Transmitindo nota fiscal
					 */
					if((nota.getStNotaFiscal() == NotaFiscalServices.VALIDADA) ||
					//As notas via DPEC que estejam autorizadas também seráo enviadas para que sejam retransmitidas e autorizadas novamente		
					(nota.getStNotaFiscal() == NotaFiscalServices.AUTORIZADA && nota.getTpEmissao() == NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC)){
						retorno   = transmitirNFe(cdNotaFiscal, cdEmpresa, connect);
						if(retorno.getCode() <= 0){
							//Caso o parametro de gerar a nota na autorização estiver ativo, irá retornar o numero da nota para nulo
							if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa, connect)==1){
								nota.setNrNotaFiscal(null);
								nota.setNrChaveAcesso(null);
				            	nota.setNrDv(0);
								NotaFiscalDAO.update(nota);
							}
				
							Conexao.rollback(connect);
				
							qtErro++;
							
							/*
							 * Registrando o LOG
							 */
							HashMap<String, Object> register = new HashMap<String, Object>();
							register.put("NUMERO", nota.getNrNotaFiscal());
							register.put("RESULTADO", retorno.getMessage());
							rsm.addRegister(register);
							
							continue;
						}
						else{
							//Esse tempo de espera serve para dar tempo da nota ser processada na SEFAZ para que possa ser consultada
							try{  
								  Thread.sleep(5000);  
						    }catch(Exception e){  
						          System.out.println("Deu erro!");  
						    }  
						}
						
					}
					nota  = NotaFiscalDAO.get(cdNotaFiscal, connect);
					//Envio via DPEC, não é consultado na proxima rotina
					if(nota.getTpEmissao() == NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC){
						if(nota.getStNotaFiscal() == NotaFiscalServices.AUTORIZADA){
							qtSucesso++;
						}
						
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("NUMERO", nota.getNrNotaFiscal());
						register.put("RESULTADO", "Nota Autorizada via DPEC");
						rsm.addRegister(register);
						
						connect.commit();
						
						
						continue;
					}
					nota  = NotaFiscalDAO.get(cdNotaFiscal, connect);
					/*
					 * Consultando nota fiscal
					 */
					
					if(nota.getStNotaFiscal() == NotaFiscalServices.TRANSMITIDA){
						retorno   = retornoProcessamento(cdNotaFiscal, cdEmpresa, connect);
						if(retorno.getCode() <= 0){
							
							Conexao.rollback(connect);
							
							qtErro++;
						}
						else{  
							
							connect.commit();
							
							qtSucesso++;
						}
						
						/*
						 * Registrando o LOG
						 */
						if(retorno != null){
							HashMap<String, Object> register = new HashMap<String, Object>();
							register.put("NUMERO", nota.getNrNotaFiscal());
							register.put("RESULTADO", retorno.getMessage());
							rsm.addRegister(register);
						}
						
					}
					
					
				}
				
				catch(Exception e){
					Conexao.rollback(connect);
					e.printStackTrace();
				}
				
				finally{
					Conexao.desconectar(connect);
				}
				
			}
			
			if((qtErro+qtSucesso) == 0)
				return new Result(-1, "Nenhuma nota foi enviada!");
			
			result.addObject("resultado", rsm);
			if(qtSucesso==0)
				result.setMessage("Nenhuma nota foi autorizada! [Erros: "+qtErro+"]");
			else if(qtErro==0)
				result.setMessage("Todas as notas foram autorizadas com sucesso!");
			else
				result.setMessage(qtSucesso+" nota(s) autorizada(s) com sucesso! "+qtErro+" nota(s) não foi(ram) autorizada(s)!");
			
			if(result.getCode()<=0){
				return new Result(-1, "Erro ao realizar tarefas de nfe!");
			}
			
			return result;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar realizar as tarefas de NFE!", e);
		}
		
	}
	
	public static Result gerarXmlDpec(int cdNotaFiscal, int cdEmpresa) {		
		return gerarXmlDpec(cdNotaFiscal, cdEmpresa, null);
	}
	public static Result gerarXmlDpec(int cdNotaFiscal, int cdEmpresa, Connection connect){		
		boolean isConnectionNull = connect == null;
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {  			
			ResultSet rsNfe = connect.prepareStatement("SELECT A.*, B.nm_natureza_operacao, "+
													   "       B.nr_codigo_fiscal AS nr_cfop, C.nm_pessoa AS nm_fantasia, C.nr_telefone1, C.nm_email,"+
													   "       D.nr_cnpj AS nr_cnpj_emissor, D.nm_razao_social, D.nr_inscricao_estadual, D.nr_inscricao_municipal, "+
													   "       E.nm_logradouro, E.nm_bairro, E.nr_endereco, E.nr_cep, E.nm_complemento, "+
													   "       F.nm_cidade, F.id_ibge, G.sg_estado "+
													   "FROM fsc_nota_fiscal A "+
													   "JOIN adm_natureza_operacao B ON (A.cd_natureza_operacao = B.cd_natureza_operacao) "+
													   "JOIN grl_pessoa            C ON (A.cd_empresa           = C.cd_pessoa) "+
													   "JOIN grl_pessoa_juridica   D ON (A.cd_empresa           = D.cd_pessoa) "+
													   "JOIN grl_pessoa_endereco   E ON (A.cd_empresa           = E.cd_pessoa  "+
													   "                             AND E.lg_principal         = 1)           "+
													   "JOIN grl_cidade            F ON (E.cd_cidade            = F.cd_cidade) "+
													   "JOIN grl_estado            G ON (F.cd_estado            = G.cd_estado) "+
													   "JOIN fsc_nota_fiscal_doc_vinculado   H ON (H.cd_nota_fiscal = A.cd_nota_fiscal) "+
													   "WHERE A.cd_nota_fiscal = " + cdNotaFiscal).executeQuery();
			if(!rsNfe.next()){				
				return new Result(-1, "Nota fiscal não localizada!");
			}
			
			
            /** 
             * Estrutura XML com a Declaração Prévia emissão em Contingência - DPEC. 
             */ 			
            TDPEC dpec = new TDPEC();  
            /* 
             * Versão do leiaute. 
             */             
            dpec.setVersao("1.01");  
  
            /** 
             * Tag de grupo com Informações da Declaração  
             * Prévia de emissão em Contingência 
             */             
            InfDPEC infDPEC = new InfDPEC();  
            /* 
             * Grupo de Identificação da TAG a ser assinada. 
             * Informar com a literal "DPEC" + CNPJ do emissor. 
             */              
            infDPEC.setId("DPEC" + Util.limparFormatos(rsNfe.getString("nr_cnpj_emissor"), 'N'));  
              
            /** 
             * Grupo de Identificação do Declarante, deve ser  
             * informado com os dados do emissor das NF-e  
             * emitidas em Contingência Eletrônica 
             */            
            IdeDec ideDec = new IdeDec();  
            /* 
             * Código da UF do emitente do Documento Fiscal. Utilizar a Tabela do IBGE. 
             */              
            ideDec.setCUF(getCdUfDefault(connect));  
            /* 
             * Identificação do Ambiente: 
             * 1 - Produção 
             * 2 - Homologação. 
             */  
            ideDec.setTpAmb(String.valueOf(getTipoAmbiente(cdEmpresa)));
            
            
            /* 
             * Versão do aplicativo utilizado no processo de emissão da DPEC. 
             */
//            ideDec.setVerProc("App100");            
            ideDec.setVerProc("App200");  
            /* 
             * Número do CNPJ do emitente, vedada a formatação do campo. 
             */  
            ideDec.setCNPJ(Util.limparFormatos(rsNfe.getString("nr_cnpj_emissor"), 'N'));  
            /* 
             * Número da Inscrição Estadual do emitente, vedada a formatação do campo. 
             */
            ideDec.setIE(Util.limparFormatos(rsNfe.getString("nr_inscricao_estadual"), 'N'));  
            infDPEC.setIdeDec(ideDec);  
  
            /** 
             * Resumo das NF-e emitidas no Sistema de 
             * Contingência Eletrônica (até 50 NF-e com tpEmis = "4") 
             * tpEmis "4" = Contingência DPEC - emissão em Contingência com envio da Declaração 
             * Prévia de emissão em Contingência é DPEC; 
             */              
            int cdEnderecoDestinario = rsNfe.getInt("cd_endereco_destinatario");
            int vlICMS = 0;
            ResultSet rsDest = connect.prepareStatement("SELECT gn_pessoa, nr_cnpj, nr_cpf, nm_pessoa, nr_inscricao_estadual, nm_email, " +
									                    "       nm_logradouro, nm_bairro, nm_complemento, nr_endereco, E.nr_cep, nr_telefone1, " +
									                    "       nm_cidade, id_ibge, sg_estado, nm_razao_social " +
									                    "FROM grl_pessoa A " +
									                    "LEFT OUTER JOIN grl_pessoa_fisica    B ON (A.cd_pessoa = B.cd_pessoa) " +
									                    "LEFT OUTER JOIN grl_pessoa_juridica  C ON (A.cd_pessoa = C.cd_pessoa) " +
									                    "LEFT OUTER JOIN grl_pessoa_endereco  E ON (A.cd_pessoa = E.cd_pessoa  "+
														"                                       AND E.cd_endereco          = "+cdEnderecoDestinario+")"+
														"LEFT OUTER JOIN grl_cidade           F ON (E.cd_cidade            = F.cd_cidade) "+
														"LEFT OUTER JOIN grl_estado           G ON (F.cd_estado            = G.cd_estado) "+
									                    "WHERE A.cd_pessoa = "+rsNfe.getInt("cd_destinatario")).executeQuery();
			if(!rsDest.next()){				
				return new Result(-1, "Destinatário não localizado!");
			}
            ResNFe resNFe = new ResNFe();  
            /* 
             * Chave de Acesso da NF-e emitida em Contingência Eletrônica. 
             */  
            
            
            resNFe.setChNFe(rsNfe.getString("nr_chave_acesso").substring(3));  
            /* 
             * Informar o CNPJ ou o CPF do destinatário da NF-e, 
             * em caso de destinatário ou remetente estabelecido 
             * no exterior deverá ser informado a tag CNPJ sem 
             * conteúdo. 
             */             
            if(rsDest.getString("nr_cnpj") != null && !rsDest.getString("nr_cnpj").equals(""))
            	resNFe.setCNPJ(Util.limparFormatos(rsDest.getString("nr_cnpj"), 'N'));
            else if(rsDest.getString("nr_cpf") != null && !rsDest.getString("nr_cpf").equals(""))
            	resNFe.setCPF(Util.limparFormatos(rsDest.getString("nr_cpf"), 'N'));
            /* 
             * Sigla da UF de destino da mercadoria. 
             */  
            resNFe.setUF(br.com.javac.v101.dpec.TUf.valueOf(rsDest.getString("sg_estado")));  
            /* 
             * Valor total da NF-e. 
             */  
            resNFe.setVNF(Util.formatNumberSemSimbolos(rsNfe.getDouble("vl_total_nota"), 2));  
            /* 
             * Valor Total do ICMS da operação própria. 
             */  
            resNFe.setVICMS(Util.formatNumberSemSimbolos(vlICMS, 2));  
            /* 
             * Valor Total do ICMS retido por Substituição Tributária 
             */  
            resNFe.setVST("0.00");  
              
            infDPEC.getResNFe().add(resNFe);  
              
            dpec.setInfDPEC(infDPEC);              
            JAXBContext context   = JAXBContext.newInstance(br.com.javac.v101.dpec.TDPEC.class);  
            Marshaller marshaller = context.createMarshaller();  
            JAXBElement<br.com.javac.v101.dpec.TDPEC> element = new br.com.javac.v101.dpec.ObjectFactory().createEnvDPEC(dpec);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);  
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);  
            StringWriter sw = new StringWriter();  
            marshaller.marshal(element, sw);  
            /*
             * XML
             */
            String xml = sw.toString(); 
            xml = xml.replaceAll("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\" ", "");  
            xml = removeAcentos(xml);
            //Assinar XML            
 		    if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3)
 			   xml = (String) assinarNFeA3(xml, NfeServices.EPEC, cdEmpresa).getObjects().get("xmlAssinado"); 
 		    else{
 			   xml = (String) assinarNFeA1(xml, NfeServices.EPEC, cdEmpresa).getObjects().get("xmlAssinado"); 
 		    } 		   
            
            /*
             * Criando objeto d retorno
             */
            Result result = new Result(1, "Correto");            
            result.addObject("xml", xml);            
            return result;
            
        } catch (Exception e) {
        	Util.registerLog(e);
            error(e.toString()); 
            return new Result(-1, "Erro ao gerarXmlDpec");
        }  
		
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result getDANFE(int cdEmpresa, int cdNotaFiscal, String pathServer){
		try {
			//
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		    DocumentBuilder db         = dbf.newDocumentBuilder();  
		    // Carregando XML      
		    Connection conexao         = Conexao.conectar();
		    PreparedStatement pstmt    = conexao.prepareStatement("SELECT txt_xml FROM fsc_nota_fiscal_historico A "+
		    													  "WHERE A.cd_nota_fiscal = " + cdNotaFiscal + 
		                                                          "  AND A.tp_historico   = " + NotaFiscalHistoricoServices.AUTORIZACAO_NOTA);
		    ResultSetMap rsm           = new ResultSetMap(pstmt.executeQuery());
		    pstmt                      = conexao.prepareStatement("SELECT txt_xml FROM fsc_nota_fiscal_historico A "+
													    		  "WHERE A.cd_nota_fiscal = " + cdNotaFiscal + 
		    	                                                  "  AND A.tp_historico   = " + NotaFiscalHistoricoServices.PROC_DPEC);
		    ResultSetMap rsm2 = new ResultSetMap(pstmt.executeQuery());
		    
		    NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);
		    NaturezaOperacao natOp = NaturezaOperacaoDAO.get(nota.getCdNaturezaOperacao());
		    if(rsm.next()){
			    String xmlNFE = rsm.getString("txt_xml");
			    InputSource inStream = new InputSource();  
		        inStream.setCharacterStream(new StringReader(xmlNFE));  
			    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
		        factory.setNamespaceAware(true);  
		        org.w3c.dom.Document doc      = db.parse(inStream);  
			    String recordPath             = "/nfeProc/NFe/infNFe/det";//estrutura do xml  
			    JRXmlDataSource xmlDataSource = new JRXmlDataSource(doc, recordPath);
			    
			    HashMap<String, Object> params = new HashMap<String, Object>();
			    Empresa empresa   = EmpresaDAO.get(cdEmpresa);
			    params.put("LOGO", empresa.getImgLogomarca());
			    params.put("TPEMISSAO", NotaFiscalServices.EMI_NORMAL);
			    params.put("NMCFOP", natOp.getNmNaturezaOperacao());
			    
				// GERANDO RELatéRIO				
				JasperPrint js =  JasperFillManager.fillReport(pathServer+"danfe.jasper", params, xmlDataSource);			
				// Criando relatério e exportando para o PDF 
				JasperExportManager.exportReportToPdfFile(js, pathServer +"danfe.pdf");				
		    }
		    else if (rsm2.next()){
			    String xmlNFE = rsm2.getString("txt_xml");
			    InputSource inStream = new InputSource();  
		        inStream.setCharacterStream(new StringReader(xmlNFE));  
			    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
		        factory.setNamespaceAware(true);  
		        org.w3c.dom.Document doc      = db.parse(inStream);  
			    String recordPath             = "/nfeProc/NFe/infNFe/det";//estrutura do xml  
			    JRXmlDataSource xmlDataSource = new JRXmlDataSource(doc, recordPath);
			    
			    HashMap<String, Object> params = new HashMap<String, Object>();
			    Empresa empresa   = EmpresaDAO.get(cdEmpresa);
			    params.put("LOGO", empresa.getImgLogomarca());
			    params.put("TPEMISSAO", NotaFiscalServices.EMI_CONTIGENCIA_VIA_EPEC);
			    params.put("NMCFOP", natOp.getNmNaturezaOperacao());
			    
				// GERANDO RELatéRIO				
				JasperPrint js =  JasperFillManager.fillReport(pathServer+"danfe.jasper", params, xmlDataSource);			
				// Criando relatério e exportando para o PDF 
				JasperExportManager.exportReportToPdfFile(js, pathServer +"danfe.pdf");				
		    }
		    
		    else{
		    	String xmlNFE        = (String) com.tivic.manager.fsc.NfeServices.getXmlComProtocolo(cdNotaFiscal, (NotaFiscalHistorico)null, cdEmpresa);
			    InputSource inStream = new InputSource();  
		        inStream.setCharacterStream(new StringReader(xmlNFE));  
			    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
		        factory.setNamespaceAware(true);  
		        org.w3c.dom.Document doc      = db.parse(inStream);  
			    String recordPath             = "/nfeProc/NFe/infNFe/det";//estrutura do xml  
			    JRXmlDataSource xmlDataSource = new JRXmlDataSource(doc, recordPath);
			    
			    HashMap<String, Object> params = new HashMap<String, Object>();
			    Empresa empresa   = EmpresaDAO.get(cdEmpresa);
			    params.put("LOGO", empresa.getImgLogomarca());
			    params.put("TPEMISSAO", NotaFiscalServices.EMI_NORMAL);
			    params.put("NMCFOP", natOp.getNmNaturezaOperacao());
			    
				// GERANDO RELatéRIO				
				JasperPrint js =  JasperFillManager.fillReport(pathServer+"danfe.jasper", params, xmlDataSource);			
				// Criando relatério e exportando para o PDF 
				JasperExportManager.exportReportToPdfFile(js, pathServer +"danfe.pdf");				
		    }
		    return new Result(1);
		}
		catch (Exception e){
			com.tivic.manager.util.Util.registerLog(e);				
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao processar DANFE");					
		}
	}		

	public static ResultSetMap organizarTributos(ResultSetMap rsmTributos){
		
		ResultSetMap rsmTributoRetorno = new ResultSetMap();
		rsmTributoRetorno.addRegister(null);
		rsmTributoRetorno.addRegister(null);
		rsmTributoRetorno.addRegister(null);
		rsmTributoRetorno.addRegister(null);
		rsmTributoRetorno.addRegister(null);
		
		while(rsmTributos.next()){
			if(rsmTributos.getString("id_tributo").equals("ICMS")){
				rsmTributoRetorno.getLines().set(0, rsmTributos.getRegister());
			}
			
			if(rsmTributos.getString("id_tributo").equals("IPI")){
				rsmTributoRetorno.getLines().set(1, rsmTributos.getRegister());
			}
			
			if(rsmTributos.getString("id_tributo").equals("II")){
				rsmTributoRetorno.getLines().set(2, rsmTributos.getRegister());
			}
			
			if(rsmTributos.getString("id_tributo").equals("PIS")){
				rsmTributoRetorno.getLines().set(3, rsmTributos.getRegister());
			}
			
			if(rsmTributos.getString("id_tributo").equals("COFINS")){
				rsmTributoRetorno.getLines().set(4, rsmTributos.getRegister());
			}
		}
		rsmTributoRetorno.beforeFirst();
		
		return rsmTributoRetorno;
	}
	
	public static String getVAfrmm(int cdNotaFiscal, int cdItem){
		Connection connect         = Conexao.conectar();
		try {
			NotaFiscalItem notaItem = NotaFiscalItemDAO.get(cdNotaFiscal, cdItem, connect);
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_nota_fiscal", "" + cdNotaFiscal, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmNotaFiscalDocVinculado = NotaFiscalDocVinculadoDAO.find(criterios, connect);
			float vlAfrmmItem = 0;
			if(rsmNotaFiscalDocVinculado.next()){
				int cdDocumentoEntrada = rsmNotaFiscalDocVinculado.getInt("cd_documento_entrada");
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_documento_entrada", "" + cdDocumentoEntrada, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmEntradaEventoFinanceiro = EntradaEventoFinanceiroDAO.find(criterios, connect);
				while(rsmEntradaEventoFinanceiro.next()){
					EntradaEventoFinanceiro entradaEventoFinanceiro = EntradaEventoFinanceiroDAO.get(rsmEntradaEventoFinanceiro.getInt("cd_documento_entrada"), rsmEntradaEventoFinanceiro.getInt("cd_evento_financeiro"), connect);
					if(entradaEventoFinanceiro.getCdEventoFinanceiro() == ParametroServices.getValorOfParametroAsInteger("CD_EVENTO_FINANCEIRO_AFRMM", 0)){
						float vlAfrmmTotal = entradaEventoFinanceiro.getVlEventoFinanceiro();
						float qtTributarioTotal = NotaFiscalServices.getQuantidadeAllItens(cdNotaFiscal, connect);
						float qtTributarioItem = notaItem.getQtTributario();
						vlAfrmmItem = (vlAfrmmTotal * qtTributarioItem) / qtTributarioTotal;
						break;
					}
				}
			}
		
			return Util.formatNumberSemSimbolos(vlAfrmmItem, 2);
		
			
		}
		catch (Exception e){
			com.tivic.manager.util.Util.registerLog(e);				
			e.printStackTrace(System.out);
			return null;					
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static void main(String args[]){		
		retornoProcessamento(262, 3);		
	}
	
	
	/**
	 * Imprime o DANFE simplificado em uma impressora portátil bluetooth
	 * 
	 * @param cdNotaFiscal
	 * @return
	 * @author Luiz Romario Filho
	 * @since 20/05/2015
	 */
	public static Result imprimirDanfeSimplificado(int cdEmpresa, int cdNotaFiscal){
		return imprimirDanfeSimplificado(cdEmpresa, cdNotaFiscal, null);
	}
	
	/**
	 * @see #imprimirDanfeSimplificado(int)
	 * @param cdNotaFiscal
	 * @param connect
	 * @return
	 */
	public static Result imprimirDanfeSimplificado(int cdEmpresa, int cdNotaFiscal, Connection connect){
		boolean isConnectionNull = connect == null;
		if(isConnectionNull)
			connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			
			NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal, connect);
			
			ArrayList<NotaFiscalItem> itens = NotaFiscalItemServices.getAllByCdNfe(cdNotaFiscal);
			
			// a) Dados do emitente: Nome/Razão Social, Sigla da UF, CNPJ, Inscrição Estadual; 
			// b) Dados gerais da NF-e: Tipo de operação (entrada ou saída), Série e número da NF-e, Data de emissão; 
			// c) Dados do destinatário/remetente: Nome/Razão Social, Sigla da UF, CNPJ/CPF; 
			// d) Dados dos itens: Descrição dos Produtos/Serviços, Unidade Comercial, Quantidade, Valor unitário, Valor total do item; 
			// e) Dados dos totais da NF-e: Valor total da Nota Fiscal
			
			String destinatarioNome, destinatarioSiglaUF, destinatarioCNPJCPF, unidade, emitenteUF;
			
			Result enderecoPrincipalCompleto;
			HashMap<String, Object> hashMap; 
		
			PessoaJuridica emitente = PessoaJuridicaDAO.get(nota.getCdEmpresa());
			enderecoPrincipalCompleto = PessoaEnderecoServices.getEnderecoPrincipalCompleto(emitente.getCdPessoa());
			hashMap = (HashMap<String, Object>) enderecoPrincipalCompleto.getObjects().get("PESSOAENDERECO");
			emitenteUF = (String) hashMap.get("SG_ESTADO");
			
			PessoaFisica pessoaFisica = PessoaFisicaDAO.get(nota.getCdDestinatario());
			if(pessoaFisica == null){
				PessoaJuridica pessoaJuridica = PessoaJuridicaDAO.get(nota.getCdDestinatario());
				destinatarioNome = pessoaJuridica.getNmRazaoSocial();
				destinatarioCNPJCPF = pessoaJuridica.getNrCnpj();
				enderecoPrincipalCompleto = PessoaEnderecoServices.getEnderecoPrincipalCompleto(pessoaJuridica.getCdPessoa());
				hashMap = (HashMap<String, Object>) enderecoPrincipalCompleto.getObjects().get("PESSOAENDERECO");
			} else {
				destinatarioNome = pessoaFisica.getNmPessoa();
				destinatarioCNPJCPF = pessoaFisica.getNrCpf();
				enderecoPrincipalCompleto = PessoaEnderecoServices.getEnderecoPrincipalCompleto(pessoaFisica.getCdPessoa());
				hashMap = (HashMap<String, Object>) enderecoPrincipalCompleto.getObjects().get("PESSOAENDERECO");
			}
			destinatarioSiglaUF = (String) hashMap.get("SG_ESTADO");
			
			System.out.println("*****************************************");
			System.out.println("DANFE SIMPLIFICADO");
			System.out.println(nota.getNrChaveAcesso());
			System.out.println(nota.getNrProtocoloAutorizacao());
			System.out.println();
			System.out.println(emitenteUF);
			System.out.println(emitente.getNrCnpj());
			System.out.println(emitente.getNrInscricaoEstadual());
			System.out.println();
			System.out.println(NotaFiscalServices.SAIDA + " - Saída\t" + nota.getTpMovimento() + "\n" +  NotaFiscalServices.ENTRADA + " - Entrada");
			System.out.println(nota.getNrSerie());
			System.out.println(nota.getNrNotaFiscal());
			System.out.println(new SimpleDateFormat("dd/MM/yyyy").format(nota.getDtEmissao().getTime()));
			System.out.println();
			System.out.println(destinatarioNome);
			System.out.println(destinatarioSiglaUF);
			System.out.println(destinatarioCNPJCPF);
			System.out.println("*****************************************");
			System.out.println("CODIGO \t\t PRODUTO\t\t\t\t\t  UN.\t\t\t\t QTD. \t VL UNITARIO \t VL TOTAL");
			for (NotaFiscalItem notaFiscalItem : itens) {
				ProdutoServico produtoServico = ProdutoServicoDAO.get(notaFiscalItem.getCdProdutoServico());
				System.out.print(produtoServico.getIdProdutoServico() + "\t");
				System.out.print(produtoServico.getNmProdutoServico() + "\t\t\t\t");
				
				if(nota.getTpMovimento() == NotaFiscalServices.SAIDA){
					DocumentoSaidaItem documentoSaidaItem = DocumentoSaidaItemDAO.get(notaFiscalItem.getCdDocumentoSaida(), produtoServico.getCdProdutoServico(), cdEmpresa, notaFiscalItem.getCdItemDocumento());
					UnidadeMedida unidadeMedida = UnidadeMedidaDAO.get(documentoSaidaItem.getCdUnidadeMedida());
					unidade = unidadeMedida.getNmUnidadeMedida();
				} else {
					DocumentoEntradaItem documentoEntradaItem = DocumentoEntradaItemDAO.get(notaFiscalItem.getCdDocumentoEntrada(), produtoServico.getCdProdutoServico(), cdEmpresa, notaFiscalItem.getCdItemDocumento());
					UnidadeMedida unidadeMedida = UnidadeMedidaDAO.get(documentoEntradaItem.getCdUnidadeMedida());
					unidade = unidadeMedida.getNmUnidadeMedida();
				}
				
				System.out.print(unidade + "\t\t\t");
				System.out.print(notaFiscalItem.getQtTributario() + "\t");
				System.out.print(notaFiscalItem.getVlUnitario() + "\t");
				float vlTotal = notaFiscalItem.getQtTributario() * notaFiscalItem.getVlUnitario() + notaFiscalItem.getVlAcrescimo() - notaFiscalItem.getVlDesconto();
				System.out.println(vlTotal + "\t"); 
			}
			System.out.println("TOTAL " + nota.getVlTotalNota());
			
//			PrinterMobile.printReset();
//			PrinterMobile.alignType(PrinterMobile.AT_CENTER);
//	
//			PrinterMobile.printCRLF(1);
//			PrinterMobile.formatString(false, true, true, false, false);
//			PrinterMobile.sendString("DANFE SIMPLIFICADO", PrinterMobile.TC_UTF8);
//	
//			PrinterMobile.printCRLF(1);
//			PrinterMobile.formatString(false, false, true, false, false);
//			PrinterMobile.sendString(nota.getNrChaveAcesso(), PrinterMobile.TC_UTF8);
//	
//			PrinterMobile.printCRLF(1);
//			PrinterMobile.formatString(false, false, true, false, false);
//			PrinterMobile.sendString(nota.getNrProtocoloAutorizacao(), PrinterMobile.TC_UTF8);
//			
//			PrinterMobile.printCRLF(1);
//			PrinterMobile.formatString(false, false, true, false, false);
//			PrinterMobile.sendString(emitente.getNmRazaoSocial(), PrinterMobile.TC_UTF8);
////			PrinterMobile.formatString(false, false, true, false, false);
////			PrinterMobile.sendString(, PrinterMobile.TC_UTF8);
//	
//			PrinterMobile.printCRLF(1);
//			PrinterMobile.formatString(false, false, true, false, false);
//	
//	//		PrinterMobile.printBarcode(4, 2, 40, 2, "500909");
//	
//			PrinterMobile.printCRLF(1);
//			PrinterMobile.printReset();
//			PrinterMobile.printPageEnd();
		}
	
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace();
		}
		
		finally{
			Conexao.desconectar(connect);
		}
	
		return null;
	}
}
