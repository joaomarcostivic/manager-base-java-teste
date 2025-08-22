package com.tivic.manager.print;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
//import javax.comm.CommPortIdentifier;
//import javax.comm.NoSuchPortException;
//import javax.comm.PortInUseException;
//import javax.comm.SerialPort;
//import javax.comm.UnsupportedCommOperationException;
import com.tivic.manager.grl.ParametroServices;

public class PrinterMobile {
	public static final int RETIRADA = 0;
	public static final int DEVOLUCAO = 1;
	public static final int AIT = 2;
	public static final int AIT_TRANSPORTE = 3;
	public static final int DESIGN = 4;

	public static final int AT_LEFT = 0;
	public static final int AT_CENTER = 1;
	public static final int AT_RIGHT = 2;

	public static final int CON_PAGESTART = 100;
	public static final int CON_PAGEEND = 101;

	public static final int DRAW_LINE = 200;
	public static final int DRAW_RECTANGLE = 201;
	public static final int DRAW_TEXT = 202;
	public static final int DRAW_CODE128 = 203;
	public static final int DRAW_CREATEROTALBLOCK = 204;
	public static final int DRAW_CIRCLE = 205;
	public static final int DRAW_OVAL = 206;

	public static final int ASCII_RESET = 301;
	public static final int ASCII_PRINTCRLF = 302;
	public static final int ASCII_FORMATSTRING = 303;
	public static final int ASCII_SENDSTRING = 304;
	public static final int ASCII_FEEDLINES = 305;
	public static final int ASCII_SENDBUFFER = 306;
	public static final int ASCII_PRINTQRCODE = 307;
	public static final int ASCII_SETPRINTPOSITION = 308;
	public static final int ASCII_OPPOSITECOLOR = 309;
	public static final int ASCII_SETLINESPACE = 310;
	public static final int ASCII_ALIGNTYPE = 311;
	public static final int ASCII_DUPLEPRINT = 312;
	public static final int ASCII_PRINTBARCODE = 313;
    
	public static final int BC_UPCA = 65;
	public static final int BC_UPCB = 66;
	public static final int BC_EAN13 = 67;
	public static final int BC_EAN8 = 68;
	public static final int BC_CODE39 = 69;
	public static final int BC_ITF = 70;
	public static final int BC_CODEBAR = 71;
	public static final int BC_CODE93 = 72;
	public static final int BC_CODE128 = 73;
	public static final int BC_DEFAULT = 0;
	public static final int BC_HRINONE = 0;
	public static final int BC_HRIUNDER = 1;
	public static final int BC_HRIBELOW = 2;
	public static final String TC_GB2312 = "gb2312";
	public static final String TC_UTF8 = "utf-8";
	public static final String TC_LATIN1 = "iso-8859-1";
	public static final String TC_SHIFTJIS = "shift-jis";
	public static final int RT_90 = 1;
	public static final int RT_270 = 2;
	public static final int PS_NORMAL = 0;
	public static final int PS_PRAPEROUT = 3;
	public static final int PS_ERR = 5;
	
	public static boolean sendString(String strSend, String strCode) {
		System.out.println("REGOLIB, ASCII_SendString...");
		byte[] btSend = makeString(strSend, strCode);
		sendbuffer(btSend);
		return true;
	}

	public static byte[] makeString(String strSend, String codeType) {
		System.out.println("REGOLIB, asciiPrint:makeString...");
		try {
			return strSend.getBytes(codeType);
		} catch (UnsupportedEncodingException e) {
			System.out.println("REGOLIB, asciiPrint:makeString error " + e.getMessage());
		}
		return null;
	}

	public static void sendbuffer(byte[] btSend) {
		sendbuffer(btSend, null);
	}

	public static void sendbuffer(byte[] szBuffer, OutputStream outputStream) {
		SerialPort serialPort = null;
		System.out.println("btOper, sendbuffer...");
		String portName = ParametroServices.getValorOfParametro("NM_PORTA_SERIAL", "COM4", null);
		try {
			if (outputStream == null) {
				Enumeration<?> ports = CommPortIdentifier.getPortIdentifiers();  

			    while(ports.hasMoreElements()){  
			        CommPortIdentifier port = (CommPortIdentifier) ports.nextElement();
			        System.out.println(port.getName());
			    }
				CommPortIdentifier portId = (CommPortIdentifier) CommPortIdentifier.getPortIdentifier(portName);
				serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				outputStream = serialPort.getOutputStream();
			}
			outputStream.write(szBuffer);
		} catch (PortInUseException e) {
			System.out.println("PortInUseException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			System.out.println("UnsupportedCommOperationException");
			e.printStackTrace();
		} catch (NoSuchPortException e) {
			System.out.println("NoSuchPortException");
			e.printStackTrace();
		} finally {
			serialPort.close();
			System.out.println("concluded.");
		}
	}

	public static boolean printBarcode(int iType, int iWidth, int iHeight, int hri, String strData) {
		System.out.println("MobilePrinter, ASCII_PrintBarcode...");
		ArrayList<?> btSendlist = makePrintBarcode(iType, iWidth, iHeight, hri, strData);
		byte btSend[];
		Iterator<?> iterator = btSendlist.iterator();
		do {
			btSend = (byte[]) iterator.next();
			sendbuffer(btSend);
		} while (iterator.hasNext());
		return true;
	}

	public static ArrayList<byte[]> makePrintBarcode(int iType, int iWidth, int iHeight, int hri, String strData) {
		System.out.println("REGOLIB, asciiPrint:PrintBarcode...");

		ArrayList<byte[]> cmdlist = new ArrayList<byte[]>();
		if ((iWidth <= 0) || (iWidth > 4)) {
			iWidth = 2;
		}
		byte[] data1 = makeCommCmd(10, iWidth, 0);
		cmdlist.add(data1);
		if (iHeight <= 0) {
			iHeight = 36;
		}
		byte[] data2 = makeCommCmd(11, iHeight, 0);
		cmdlist.add(data2);
		if ((hri < 0) || (hri > 2)) {
			hri = 2;
		}
		byte[] data3 = makeCommCmd(12, hri, 0);
		cmdlist.add(data3);

		System.out.println("BTP_barHRI " + data3.length);
		for (int i = 0; i < 3; i++) {
			int hex = data3[i] & 0xFF;
			System.out.print(Integer.toHexString(hex) + " ");
		}
		System.out.println();

		byte[] data4 = makeCommCmd(8, iType, strData.length());
		cmdlist.add(data4);

		byte[] data5 = (byte[]) null;
		data5 = strData.getBytes();
		cmdlist.add(data5);

		return cmdlist;
	}

	public static boolean printReset() {
		System.out.println("MobilePrinter, ASCII_Reset...");
		byte btSend[] = makeReset();
		sendbuffer(btSend);
		return true;
	}

	public static byte[] makeReset() {
		System.out.println("REGOLIB, asciiPrint:Reset...");

		byte[] data = makeCommCmd(0, 0, 0);

		return data;
	}

	public static byte[] makeFeedLines(int iNum) {
		System.out.println("REGOLIB, asciiPrint:FeedLines...");

		byte[] data = makeCommCmd(1, iNum, 0);
		return data;
	}

	public static byte[] makeCommCmd(int iType, int para1, int para2) {
		byte[] mBuff = (byte[]) null;
		switch (iType) {
		case 0:
			mBuff = new byte[2];
			mBuff[0] = 27;
			mBuff[1] = 64;
			break;
		case 1:
			mBuff = new byte[3];
			mBuff[0] = 27;
			mBuff[1] = 74;
			mBuff[2] = ((byte) para1);
			break;
		case 2:
			mBuff = new byte[4];
			mBuff[0] = 29;
			mBuff[1] = 76;
			mBuff[2] = ((byte) (para1 % 256));
			mBuff[3] = ((byte) (para1 / 256));
			break;
		case 3:
			mBuff = new byte[4];
			mBuff[0] = 29;
			mBuff[1] = 87;
			mBuff[2] = ((byte) (para1 % 256));
			mBuff[3] = ((byte) (para1 / 256));
			break;
		case 4:
			mBuff = new byte[3];
			mBuff[0] = 29;
			mBuff[1] = 66;
			mBuff[2] = ((byte) para1);
			break;
		case 5:
			if (1 == para2) {
				mBuff = new byte[2];
				mBuff[0] = 27;
				mBuff[1] = 50;
			} else {
				mBuff = new byte[3];
				mBuff[0] = 27;
				mBuff[1] = 51;
				mBuff[2] = ((byte) para1);
			}
			break;
		case 6:
			mBuff = new byte[3];
			mBuff[0] = 27;
			mBuff[1] = 97;
			mBuff[2] = ((byte) para1);
			break;
		case 7:
			mBuff = new byte[3];
			mBuff[0] = 27;
			mBuff[1] = 71;
			mBuff[2] = ((byte) para1);
			break;
		case 8:
			mBuff = new byte[4];
			mBuff[0] = 29;
			mBuff[1] = 107;
			mBuff[2] = ((byte) para1);
			mBuff[3] = ((byte) para2);
			break;
		case 10:
			mBuff = new byte[3];
			mBuff[0] = 29;
			mBuff[1] = 119;
			mBuff[2] = ((byte) para1);
			break;
		case 11:
			mBuff = new byte[3];
			mBuff[0] = 29;
			mBuff[1] = 104;
			mBuff[2] = ((byte) para1);
			break;
		case 12:
			mBuff = new byte[3];
			mBuff[0] = 29;
			mBuff[1] = 72;
			mBuff[2] = ((byte) para1);
			break;
		case 9:
			mBuff = new byte[2];
			mBuff[0] = 27;
			mBuff[1] = 118;
			break;
		case 13:
			mBuff = new byte[3];
			mBuff[0] = 27;
			mBuff[1] = 33;
			mBuff[2] = ((byte) para1);
			break;
		case 14:
			mBuff = new byte[2];
			mBuff[0] = 27;
			mBuff[1] = 68;
			break;
		case 15:
			mBuff = new byte[4];
			mBuff[0] = 28;
			mBuff[1] = 112;
			mBuff[2] = ((byte) para1);
			mBuff[3] = ((byte) para2);
			break;
		case 16:
			mBuff = new byte[8];
			mBuff[0] = 29;
			mBuff[1] = 40;
			mBuff[2] = 107;
			mBuff[3] = 3;
			mBuff[4] = 0;
			mBuff[5] = 49;
			mBuff[6] = 67;
			mBuff[7] = ((byte) para1);
			break;
		case 17:
			mBuff = new byte[5];
			mBuff[0] = 29;
			mBuff[1] = 107;
			mBuff[2] = 32;
			mBuff[3] = 16;
			mBuff[4] = ((byte) para1);
			break;
		case 18:
			mBuff = new byte[1];
			mBuff[0] = 10;
			break;
		case 19:
			mBuff = new byte[4];
			mBuff[0] = 31;
			mBuff[1] = 0;
			mBuff[2] = 20;
			mBuff[3] = 0;
			break;
		case 20:
			mBuff = new byte[4];
			mBuff[0] = 31;
			mBuff[1] = 0;
			mBuff[2] = 20;
			mBuff[3] = 1;
			break;
		case 21:
			mBuff = new byte[4];
			mBuff[0] = 31;
			mBuff[1] = 0;
			mBuff[2] = 20;
			mBuff[3] = 2;
			break;
		case 22:
			mBuff = new byte[3];
			mBuff[0] = 31;
			mBuff[1] = 0;
			mBuff[2] = 18;
			break;
		case 23:
			mBuff = new byte[7];
			mBuff[0] = 31;
			mBuff[1] = 0;
			mBuff[2] = 17;
			mBuff[3] = ((byte) (para1 % 256));
			mBuff[4] = ((byte) (para1 / 256));
			mBuff[5] = ((byte) (para2 % 256));
			mBuff[6] = ((byte) (para2 / 256));
			break;
		case 24:
			mBuff = new byte[5];
			mBuff[0] = 31;
			mBuff[1] = 0;
			mBuff[2] = 19;
			mBuff[3] = ((byte) (para1 % 256));
			mBuff[4] = ((byte) (para1 / 256));
			break;
		}
		return mBuff;
	}

	public static boolean printCRLF(int iTimes) {
		System.out.println("MobilePrinter, ASCII_PrintCRLF...");
		byte btSend[] = makeCRLF(iTimes);
		for (int i = 0; i < iTimes; i++)
			sendbuffer(btSend);
		return true;
	}

	public static byte[] makeCRLF(int iTimes) {
		System.out.println("REGOLIB, asciiPrint:PrintCRLF...");

		byte[] data = makeCommCmd(18, 0, 0);
		byte[] retBuf = new byte[data.length * iTimes];
		for (int i = 0; i < iTimes; i++) {
			for (int j = 0; j < data.length; j++) {
				retBuf[(i * data.length + j)] = data[j];
			}
		}
		return retBuf;
	}

	public static byte[] makeSetLineSpace(int iLines) {
		System.out.println("REGOLIB, asciiPrint:SetLineSpace...");

		byte[] data = (byte[]) null;
		if (iLines == 0) {
			data = makeCommCmd(5, 0, 1);
		} else {
			data = makeCommCmd(5, iLines, 0);
		}
		return data;
	}

	public static boolean alignType(int alignType) {
		System.out.println("MobilePrinter, ASCII_AlignType...");
		byte btSend[] = makeSetLineSpace(alignType);
		sendbuffer(btSend);
		return true;
	}

	public static boolean formatString(boolean width, boolean height, boolean bold, boolean underline, boolean minifont) {
		System.out.println("MobilePrinter, ASCII_AlignType...");
		byte btSend[] = makeFormatString(width, height, bold, underline, minifont);
		sendbuffer(btSend);
		return true;
	}

	public static byte[] makeFormatString(boolean width, boolean height, boolean bold, boolean underline, boolean minifont) {
		System.out.println("REGOLIB, asciiPrint:FormatString...");

		byte[] datacom = makeFontCmd(width, height, bold, underline, minifont);
		return datacom;
	}

	public static byte[] makeFontCmd(boolean width, boolean height, boolean bold, boolean underline, boolean minifont) {
		byte[] buffCmd = new byte[3];
		buffCmd[0] = 27;
		buffCmd[1] = 33;
		buffCmd[2] = 0;
		if (width) {
			int tmp29_28 = 2;
			byte[] tmp29_26 = buffCmd;
			tmp29_26[tmp29_28] = ((byte) (tmp29_26[tmp29_28] | 0x20));
		}
		if (height) {
			int tmp43_42 = 2;
			byte[] tmp43_40 = buffCmd;
			tmp43_40[tmp43_42] = ((byte) (tmp43_40[tmp43_42] | 0x10));
		}
		if (bold) {
			int tmp57_56 = 2;
			byte[] tmp57_54 = buffCmd;
			tmp57_54[tmp57_56] = ((byte) (tmp57_54[tmp57_56] | 0x8));
		}
		if (underline) {
			int tmp71_70 = 2;
			byte[] tmp71_68 = buffCmd;
			tmp71_68[tmp71_70] = ((byte) (tmp71_68[tmp71_70] | 0x80));
		}
		if (minifont) {
			int tmp87_86 = 2;
			byte[] tmp87_84 = buffCmd;
			tmp87_84[tmp87_86] = ((byte) (tmp87_84[tmp87_86] | 0x1));
		}
		return buffCmd;
	}

	public static boolean printPageEnd() {
		System.out.println("MobilePrinter, CON_PageEnd...");
		return true;
	}

}
