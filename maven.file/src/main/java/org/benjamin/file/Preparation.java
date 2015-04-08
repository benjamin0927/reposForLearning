package org.benjamin.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Preparation {
	
	private static void doPreparation(File root, File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
	 
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		while ((line = br.readLine()) != null) {
			
			System.out.println(line);
			prepareFile(root, line);
		}

		br.close();
	}
	
	private static void prepareFile(File root, String path) throws IOException {
		String[] paths = path.split("/");
		if(paths[paths.length -1].endsWith(".text") || paths[paths.length -1].endsWith(".txt")){
			File directory = new File(root.getAbsolutePath() + File.separator + path.replace(paths[paths.length - 1], ""));
			mkdirsIfDirsNotExisted(directory);
			System.out.println("directory - A - " + directory);
			File file = new File(root.getAbsolutePath() + File.separator + path);
			file.createNewFile();
			System.out.println("file - " + file);
		} else {
			File directory = new File(root.getAbsolutePath() + File.separator + path);
			mkdirsIfDirsNotExisted(directory);
			System.out.println("directory - B - " + directory);
		}		
	}
	
	private static void mkdirsIfDirsNotExisted(File directory){
		if(!directory.exists()) {
			directory.mkdirs();
		}
	}

	public static void main(String[] args) throws IOException {
		Preparation p = new Preparation();
		File root = new File("/mnt/tmp/");
		File from = new File(Preparation.class.getClassLoader().getResource("F.txt").getFile());
		File to = new File(Preparation.class.getClassLoader().getResource("T.txt").getFile());
		p.doPreparation(root, from);
		p.doPreparation(root, to);
	}

}
