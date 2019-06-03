import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner; 
import java.io.IOException;

class Main {

	public static void main(String[] args){
		Set<String> files=new HashSet<>();
		byte separator=0;
		FileDB db=new FileDB();
		byte[] fileBytes=null;

		if (args.length<2){
			System.out.println("実行時には、検索対象ディレクトリとキーワードファイルを指定してください。");
			System.exit(1);
		}

		if (args.length>3){
			System.out.println("パラメータが多すぎます。");
			System.exit(1);
		}

		Path dirPath=Paths.get(args[0]);
		if (Files.notExists(dirPath)
			|| Files.isDirectory(dirPath)==false){
			System.out.println(args[0]+":directory not found.");
			System.exit(1);
		}

		System.out.println("STEP1 Search file in \""+args[0]+"\"");
		File dir = new File(args[0]);
		File[] fileList = dir.listFiles();
		for(int i=0; i<fileList.length; i++) {
			if(fileList[i].isFile()){
				files.add(fileList[i].getAbsolutePath());
				System.out.println("\tFound:"+fileList[i].getName());
			}
		}
		if (files.isEmpty()){
			System.out.println(args[0]+": is a directory. but no file here.");
			System.exit(1);
		}


		System.out.println("STEP2:Get separator");
		if (args.length==2){		//区切りはLF
			separator=10;
			System.out.println("\tSeparator = 0x0A");
		} else {
			try {
				separator=Byte.parseByte(args[2]);
				if (separator<0){
					throw new NumberFormatException();
				}
				System.out.println("\tSeparator=0x"+Integer.toHexString(separator));
			} catch (NumberFormatException e){
				System.out.println("セパレータが不正です。0～127で入力してください。");
				System.exit(1);
			}
		}

		System.out.println("STEP3:Create keyword dictionary");
		Path filePath=Paths.get(args[1]);
		if (Files.notExists(filePath)
			|| Files.isDirectory(filePath)){
				System.out.println("キーワードファイルを読み込めませんでした。");;
		}
		try {
			fileBytes=java.nio.file.Files.readAllBytes(filePath);
		} catch (IOException e){
			System.out.println("I/O Error!");
			System.exit(1);
		}

		for(int i=0;i<fileBytes.length;i++){
			boolean findFlg=false;
			for(int j=i;j<fileBytes.length;j++){
				if(fileBytes[j]==separator){
				byte[] tmp=new byte[j-i];
					System.arraycopy(fileBytes,i,tmp,0,j-i);
					db.addKeyword(tmp);
					System.out.printf("\tadd keyword \""+new String(tmp)+"\"("+ByteDump.byteDump(tmp)+")\n");
					i=j;
					findFlg=true;
					break;
				}
			}
			if(findFlg==false){
				break;
			}
		}

		System.out.println("STEP4: add file database");
		for(String file:files){
			db.addFile(file);
		}

		System.out.println("\nPlease Input search word and press return key.");
		System.out.println("to exit press Ctrl+C.");
		while(true){
			System.out.println();
		    Scanner scanner = new Scanner(System.in); 
		    String searchWord = scanner.next();
			System.out.println();
			System.out.println("Search file as keyword \""+searchWord+"\"");
			Set<String> results=db.searchFile(searchWord);
			System.out.println(results.size()+" file found.");
			if(results.size()>0){
				System.out.println("However, there is a possibility of false positive.");
			}
		}
	}
}
