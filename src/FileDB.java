/********************************************
	bloomフィルタを利用したファイルDB
********************************************/

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.File;

class FileDB {

	Map<String,Long> files=new HashMap<>();
	Set<byte[]> keywords=new HashSet<>();
	BloomFilter filter;

	public FileDB(){
		byte[] solts={0,1,2};
		filter=new BloomFilter(solts);
	}

	public void addKeyword(byte[] newWord){
		keywords.add(newWord);
	}

	public void addKeyword(String newWord){
		keywords.add(newWord.getBytes());
	}

	public boolean addFile(String fileName){
		byte[] fileBytes=null;

		System.out.println("\tadd file:"+getFileName(fileName));
		Path filePath=Paths.get(fileName);
		if (Files.notExists(filePath)
			|| Files.isDirectory(filePath)){
				System.out.println("\tfile not found.");
				return false;
		}
		try {
			fileBytes=java.nio.file.Files.readAllBytes(filePath);
		} catch (IOException e){
			System.out.println("\tI/O Error!");
			System.exit(1);
		}
		Set<byte[]> words=new HashSet<>();

		for(byte[] word : keywords){
			if (contain(fileBytes,word)){
				System.out.println("\t\t"+new String(word)+" ("+ByteDump.byteDump(word)+") :find");
				words.add(word);
			} else {
				System.out.println("\t\t"+new String(word)+" ("+ByteDump.byteDump(word)+") :not found");
			}
		}
		System.out.println("\t\t"+words.size()+" word found.");
		Long index=filter.getBfindex(words);
		System.out.println("\t\tindex="+ByteDump.byteDump64(index));
		files.put(fileName,index);
		return true;
	}


	//base[]にsearch[]が含まれるならtrueを返す
	private static boolean contain(byte[] base,byte[] searchWord){
		if ((base == null) || (searchWord == null) ||
			(base.length == 0) || (searchWord.length == 0)
			|| (base.length < searchWord.length)){
			return false;
		}

		for (int i=0;i<=base.length-searchWord.length;i++){
			for (int j=0;j<searchWord.length;j++){
				if (base[i+j]==searchWord[j]){
					if (j==searchWord.length-1){
						return true;
					}
				} else {
					break;
				}
			}
		}
		return false;
	}

	public Set<String> searchFile(String keyword){
		Long index=filter.getBfindex(keyword.getBytes());
		System.out.println("Searchfile:word="+keyword+" ("+ByteDump.byteDump(keyword.getBytes())+") index="+ByteDump.byteDump64(index));
		Set<String> results=new HashSet<>();
		for(Map.Entry<String,Long> entry : files.entrySet()) {
			if ((entry.getValue()&index)==index){
				results.add(entry.getKey());
				System.out.println("file:"+getFileName(entry.getKey())+"\tis positive. (file index "+ByteDump.byteDump64(entry.getValue())+" & word index = "+ByteDump.byteDump64(entry.getValue()&index)+")");
			} else {
				System.out.println("file:"+getFileName(entry.getKey())+"\tis negative. (file index "+ByteDump.byteDump64(entry.getValue())+" & word index = "+ByteDump.byteDump64(entry.getValue()&index)+")");
			}
		}
		return results;
	}

	private static String getFileName(String fullName){
		return new File(fullName).getName();
    }

}
