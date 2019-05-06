public class ByteDump {

	//可変長のByte型の値を16進数表現にする
	public static String byteDump(byte[] bytes){
		String result="0x";
		for(byte n:bytes){
			result+=String.format("%02X",n);
		}
		return result;
	}

	//long型(64bit)のtargetを16桁固定の16進数表記文字列にする
	public static String byteDump64(long target){
		String tmp=Long.toHexString(target);
		while(tmp.length()<16){
			tmp="0"+tmp;
		}
		return "0x"+tmp;
	}
}
