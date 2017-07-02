package serviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import runner.ServerRunner;
import service.IOService;



public class IOServiceImpl implements IOService{
	
	public static final String PATH = "D:/BF/";
	
	// account_name_date.BF
	
	@Override
	public boolean writeFile(String file, String userId, String fileName,String postfix) {
		Date now = new Date(); 
		String currentTime = now.toString().replaceAll(":", " ");
		
		File f = new File(PATH + userId + "_" + fileName + "_" + currentTime + "." + postfix);
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
	public String readFile(String userId, String fileName,String postfix) {
		// TODO Auto-generated method stub
		fileName = PATH + userId + "_" + fileName + "." + postfix;
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
		String fileName;
		File dir = new File(PATH);
		File[] files = dir.listFiles();
		for(File f : files){
			fileName=f.getName();
			if(fileName.contains(userId + "_")){
				if(!list.equals(""))
					list=list + ",";
				list = list + fileName;
			}
			
		}
		return list;
	}
	
	
	
}
