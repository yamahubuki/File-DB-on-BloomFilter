/********************************************
	Java標準のハッシュ関数を用いてbyte配列から
	0～63のハッシュ値を返すクラス
********************************************/

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyHash{
	//soltなしでハッシュ値を計算する
	public static int calcHash(byte[] in){
		return calcHash(in,null);
	}

	//inにsoltをのせてからハッシュ値を計算する
	public static int calcHash(byte[] in,Byte solt){
		MessageDigest md=null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e){
			System.out.println("SHA-256がシステムで利用できないため、処理を中止しました。");
			System.exit(1);
		}
		byte[] tmp=in.clone();
		if (solt != null){
			tmp=byteJoin(in,solt);
		}
		md.update(tmp);
		return (md.digest()[0]+128)%64;
	}

	//バイト配列を新たに生成し、元の配列に１要素追加して返す
	private static byte[] byteJoin(byte[] a,byte b){
		byte[] r=new byte[a.length+1];
		for(int i=0;i<a.length;i++){
			r[i]=a[i];
		}
		r[a.length]=b;
		return r;
	}
}
