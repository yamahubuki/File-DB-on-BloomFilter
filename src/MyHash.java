/********************************************
	Java�W���̃n�b�V���֐���p����byte�z�񂩂�
	0�`63�̃n�b�V���l��Ԃ��N���X
********************************************/

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyHash{
	//solt�Ȃ��Ńn�b�V���l���v�Z����
	public static int calcHash(byte[] in){
		return calcHash(in,null);
	}

	//in��solt���̂��Ă���n�b�V���l���v�Z����
	public static int calcHash(byte[] in,Byte solt){
		MessageDigest md=null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e){
			System.out.println("SHA-256���V�X�e���ŗ��p�ł��Ȃ����߁A�����𒆎~���܂����B");
			System.exit(1);
		}
		byte[] tmp=in.clone();
		if (solt != null){
			tmp=byteJoin(in,solt);
		}
		md.update(tmp);
		return (md.digest()[0]+128)%64;
	}

	//�o�C�g�z���V���ɐ������A���̔z��ɂP�v�f�ǉ����ĕԂ�
	private static byte[] byteJoin(byte[] a,byte b){
		byte[] r=new byte[a.length+1];
		for(int i=0;i<a.length;i++){
			r[i]=a[i];
		}
		r[a.length]=b;
		return r;
	}
}
