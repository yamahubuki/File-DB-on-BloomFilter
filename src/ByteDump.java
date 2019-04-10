public class ByteDump {

	public static String byteDump(byte[] bytes){
		String result="0x";
		for(byte n:bytes){
			result+=String.format("%02X",n);
		}
		return result;
	}
}
