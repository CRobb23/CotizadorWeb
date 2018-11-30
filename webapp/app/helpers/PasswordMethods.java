package helpers;

import java.util.Random;

public final class PasswordMethods {
	
	private static char[] cs = new char[45]; 
	private static char[] ncs = new char[15]; 
	private static final int csLen = 45;
	private static final int ncsLen = 15;
	
	static{
    	cs[0] = 'A';
		cs[1] = 'B';
		cs[2] = 'C';
		cs[3] = 'D';
		cs[4] = 'E';
		cs[5] = 'F';
		cs[6] = 'H';
		cs[7] = 'J';
		cs[8] = 'K';
		cs[9] = 'M';
		cs[10] = 'N';
		cs[11] = 'P';
		cs[12] = 'Q';
		cs[13] = 'R';
		cs[14] = 'T';
		cs[15] = 'U';
		cs[16] = 'V';
		cs[17] = 'W';
		cs[18] = 'X';
		cs[19] = 'Y';
		cs[20] = 'a';
    	cs[21] = 'b';
    	cs[22] = 'c';
    	cs[23] = 'd';
    	cs[24] = 'e';
    	cs[25] = 'f';
    	cs[26] = 'g';
    	cs[27] = 'j';
    	cs[28] = 'k';
    	cs[29] = 'm';
    	cs[30] = 'n';
    	cs[31] = 'p';
    	cs[32] = 'q';
    	cs[33] = 'r';
    	cs[34] = 't';
    	cs[35] = 'u';
    	cs[36] = 'v';
    	cs[37] = 'w';
    	cs[38] = 'x';
    	cs[39] = 'y';
    	cs[40] = '$';
    	cs[41] = '%';
    	cs[42] = '&';
    	cs[43] = '/';
    	cs[44] = '?';
		
		ncs[0] = '3';
		ncs[1] = '4';
		ncs[2] = '7';
		ncs[3] = '8';
		ncs[4] = '9';
		ncs[5] = '1';
		ncs[6] = '2';
		ncs[7] = '5';
		ncs[8] = '6';
		ncs[9] = '0';
		ncs[10] = '#';
		ncs[11] = '@';
		ncs[12] = '*';
		ncs[13] = '+';
		ncs[14] = '|';
	}
	
	public static String getCode() {
		StringBuilder retStr = new StringBuilder(8);
		Random r = new Random();
		for (int i = 0; i < 2; i++) {
			char c = ncs[r.nextInt(ncsLen)];
			retStr.append(c);
		}
		
		int ran = r.nextInt(csLen+ncsLen);
		if (ran < csLen) {
			retStr.append(cs[ran]);
		}else{
			retStr.append(ncs[ran-csLen]);
		}
		
		for (int i = 0; i < 4; i++) {
			char c = cs[r.nextInt(csLen)];
			retStr.append(c);
		}
		
		for (int i = 0; i < 2; i++) {
			char c = ncs[r.nextInt(ncsLen)];
			retStr.append(c);
		}
		
		return retStr.toString();
	}
	
}
