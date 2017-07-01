package serviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import runner.ServerRunner;
import service.IOService;



public class IOServiceImpl implements IOService{
	
	// test 
	// past
	/*
	public static void main(String[] args) {
		IOServiceImpl i = new IOServiceImpl();
		i.writeFile("test write\r\n15165516","147258369","Hello");
		i.writeFile("test write 2","147258369","abcd");
		System.out.println(i.readFile("147258369", "147258369_Hello"));
		System.out.println(i.readFileList("147258369"));
	}
*/	
	public static final String PATH = "D:/BF/";
	
	@Override
	public boolean writeFile(String file, String userId, String fileName) {
		File f = new File(PATH + userId + "_" + fileName + ".txt");
		try {
			FileWriter fw = new FileWriter(f, false);
			fw.write(file);
			fw.flush();
			fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String readFile(String userId, String fileName) {
		// TODO Auto-generated method stub
		fileName = PATH + fileName + ".txt";
		String line,text="";
		try {
			File file = new File(fileName);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			try {
				while( ( line=br.readLine() )!=null ){
					text=text.concat(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}

	@Override
	public String readFileList(String userId) {
		// TODO Auto-generated method stub
		String list="";
		File dir = new File(PATH);
		File[] files = dir.listFiles();
		for(File f : files){
			if(list.equals(""))
				list=list + f.getName();
			else
				list=list + "," + f.getName();
		}
		return list;
	}
	
}
