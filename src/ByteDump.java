public class ByteDump {

	//�ϒ���Byte�^�̒l��16�i���\���ɂ���
	public static String byteDump(byte[] bytes){
		String result="0x";
		for(byte n:bytes){
			result+=String.format("%02X",n);
		}
		return result;
	}

	//long�^(64bit)��target��16���Œ��16�i���\�L������ɂ���
	public static String byteDump64(long target){
		String tmp=Long.toHexString(target);
		while(tmp.length()<16){
			tmp="0"+tmp;
		}
		return "0x"+tmp;
	}
}
