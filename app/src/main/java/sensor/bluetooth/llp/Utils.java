package sensor.bluetooth.llp;

public class Utils {
	public static String bytes2HexString(byte[] b) {
		String ret = "";
		if (b.length != 0) {
			for (int i = 0; i < b.length; i++) {
				String hex = Integer.toHexString(b[i] & 0xFF);
				if (hex.length() == 1) {
					hex = '0' + hex;
				}
				ret += hex.toUpperCase();
			}
		}
		return ret;
	}

	public static byte[] convert2HexArray(String apdu) {
		int len = apdu.length() / 2;
		char[] chars = apdu.toCharArray();
		String[] hexes = new String[len];
		byte[] bytes = new byte[len];
		for (int i = 0, j = 0; j < len; i = i + 2, j++) {
			hexes[j] = "" + chars[i] + chars[i + 1];
			bytes[j] = (byte) Integer.parseInt(hexes[j], 16);
		}
		return bytes;
	}

	public static Integer convert2Integer(String s, int beginIndex) {

		return Integer.parseInt(s.substring(beginIndex, beginIndex + 4), 16);
	}
	
	public static Short convert2Short(String s, int beginIndex) {
		int result = Integer.parseInt(s.substring(beginIndex, beginIndex + 4), 16);
		return (short)result;
	}

	public static short byteToShort(byte[] b, int beginindex) {
		short s = 0;
		short s0 =(short)(b[beginindex]&0xff); 
		short s1 = (short) (b[beginindex+1] & 0xff);
		s0<<=8;
		s =(short)(s0 | s1); 
		return s;
	}
}
