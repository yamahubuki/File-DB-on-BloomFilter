/********************************************
	bloomフィルタを実装するための
	インデックス計算クラス
********************************************/
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

class BloomFilter {
	byte[] solts;	//ハッシュ関数に用いるsolt

	//ハッシュに用いるsoltも指定
	public BloomFilter(byte[] solts){
		this.solts=solts;
	}

	//ハッシュに用いるsoltの個数のみ指定しランダムで設定
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

	//indexビットを立てたlong型の値を返す
	//index=0～63で指定
	private static long getFlg(int index){
		long ret=1;
		for(int i=0;i<index;i++){
			ret*=2;
		}
		return ret;
	}
}
