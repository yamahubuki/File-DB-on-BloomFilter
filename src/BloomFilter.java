/********************************************
	bloom�t�B���^���������邽�߂�
	�C���f�b�N�X�v�Z�N���X
********************************************/
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

class BloomFilter {
	byte[] solts;	//�n�b�V���֐��ɗp����solt

	//�n�b�V���ɗp����solt���w��
	public BloomFilter(byte[] solts){
		this.solts=solts;
	}

	//�n�b�V���ɗp����solt�̌��̂ݎw�肵�����_���Őݒ�
	public BloomFilter(int k){
		Random random=new Random();
		this.solts=new byte[k];
		random.nextBytes(this.solts);
	}

	public long getBfindex(byte[] word){
		Set<byte[]> tmp=new HashSet<>();
		tmp.add(word);
		return getBfindex(tmp);
	}

	public long getBfindex(Set<byte[]> words){
		long index=0;
		for (byte[] word : words){
			for (byte solt : solts){
				index=index|getFlg(MyHash.calcHash(word,solt));
			}
		}
		return index;
	}

	//index�r�b�g�𗧂Ă�long�^�̒l��Ԃ�
	//index=0�`63�Ŏw��
	private static long getFlg(int index){
		long ret=1;
		for(int i=0;i<index;i++){
			ret*=2;
		}
		return ret;
	}
}
