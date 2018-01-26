import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.sql.Connection;
//import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


//      ************** My DB dumping masterpiece***************
public class DatabaseDumper {

	public static void main(String []args) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm");
		Date date = new Date();
		String dateForFileTitle = dateFormat.format(date);
		
		String theTable = "CREATE TABLE IF NOT EXISTS `offerings` (\n"
				+ "`dep` varchar(5) CHARACTER SET utf8 COLLATE utf8_turkish_ci DEFAULT NULL,\n"
				+ "`grade` varchar(4) CHARACTER SET utf8 COLLATE utf8_turkish_ci DEFAULT NULL,\n"
				+ "`section` varchar(3) CHARACTER SET utf8 COLLATE utf8_turkish_ci DEFAULT NULL,\n"
				+ "`name` varchar(100) CHARACTER SET utf8 COLLATE utf8_turkish_ci DEFAULT NULL,\n"
				+ "`teacher` varchar(50) CHARACTER SET utf8 COLLATE utf8_turkish_ci DEFAULT NULL,\n"
				+ "`quota` varchar(2) CHARACTER SET utf8 COLLATE utf8_turkish_ci DEFAULT NULL,\n"
				+ "`lec1` varchar(35) CHARACTER SET utf8 COLLATE utf8_turkish_ci DEFAULT NULL,\n"
				+ "`lec2` varchar(35) CHARACTER SET utf8 COLLATE utf8_turkish_ci DEFAULT NULL,\n"
				+ "`lec3` varchar(35) DEFAULT NULL,\n"
				+ "`lec4` varchar(35) DEFAULT NULL,lec5` varchar(35) DEFAULT NULL\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n";
		
		//Connection con = null;
		//Statement st = null;
		String[] dep = new String[]{
				"ACC","ADA","AMER","ARCH","BF","BIM","BTE","CAA","CAD",
				"CHEM","CI","CINT","CITE","COMD","CS","CTE","CTIS","CTP",
				"DIR","ECON","EDEB","EE","EEE","ELIT","ELS","EM","EMBA",
				"ENG","ETE","ETS","FA","FRE","FRL","FRP","GE","GER","GIA",
				"GRA","HART","HCIV","HIST","HISTR","HUM","IAED","IE","IR",
				"ITA","JAP","LAUD","LAW","MAN","MATH","MBA","MBG","ME","MIAPP",
				"MSC","MSG","MSN","MTE","MUS","MUSS","NSC","PE","PHIL","PHYS",
				"PNT","POLS","PREP","PSYC","RUS","SFL","SOC","SPA",
				"TE","TEFL","THEA","THM","THR","THS","TRIN","TTP","TURK"};
		FileOutputStream fis = null;
		try {
			File f = new File(dateForFileTitle+"_dump.sql");
			f.createNewFile();
			fis = new FileOutputStream(f);
			fis.write(theTable.getBytes());
			fis.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for (int d = 0; d < dep.length; d++){
			DataSearcher x = new DataSearcher(dep[d],0,0);
			ArrayList<InfoHolder> myStuff;
			try{
				myStuff = x.findAll();
			}
			catch(NullPointerException e){
				e.printStackTrace();
				continue;
			}
			try {
				//Class.forName("com.mysql.jdbc.Driver");
				//LOCAL DB
				//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/myDBs?autoReconnect=true&amp;useUnicode=true&amp;characterEncoding=UTF-8&amp;characterSetResults=utf8&amp;connectionCollation=utf8_turkish_ci", "root", "Asdf!234");
				
				//ONLINE DB
				//con = DriverManager.getConnection("jdbc:mysql://sql2.freemysqlhosting.net:3306/sql287738", "sql287738", "qE5*eR5%");
				
				//st = con.createStatement();
				InfoHolder temp;
				String tempDep;
				String tempGra;
				String tempSec;
				String tempTea;
				String tempNam;
				String tempQuo;
				String[] tempHou;
				Iterator<InfoHolder> it = myStuff.iterator();
				int tempLen;
				int exec=0;
				String lessonQuery = "";
				while(it.hasNext()){
				
					temp = ((InfoHolder)it.next());
					tempDep = temp.getId().substring(0, temp.getId().indexOf(" "));
					tempGra = temp.getId().substring(temp.getId().indexOf(" ")+1, temp.getId().indexOf("-"));
					tempSec = temp.getId().substring(temp.getId().indexOf("-")+1, temp.getId().length());
					tempTea = temp.getCourseTeacher();
					tempNam = temp.getCourseName();
					if(tempNam.indexOf("\'")>=0)
						tempNam= tempNam.substring(0, tempNam.indexOf("'"))+"`"+tempNam.substring(tempNam.indexOf("'")+1,tempNam.length());
					if(tempTea.indexOf("\'")>=0)
						tempTea= tempTea.substring(0, tempTea.indexOf("'"))+"`"+tempTea.substring(tempTea.indexOf("'")+1,tempTea.length());
					
					tempQuo = ""+temp.getTotalQuota();
					tempHou = temp.getLessonAndBuilding();
					tempLen = tempHou.length;
					for(int c = 1; c<=tempLen; c++){
						if (tempHou[c-1].equals(" ")||tempHou[c-1].equals("")||tempHou[c-1].equals(null))
							continue;
						else{
							lessonQuery += ",'"+tempHou[c-1]+"'";
							exec++;
						}
					}
					
					for(int c = 0; c < 5-exec; c++){
						lessonQuery += ",NULL";
					}
					String toWrite = "INSERT INTO offerings(dep,grade,section,name,teacher,quota,lec1,lec2,lec3,lec4,lec5) VALUES('"
							+tempDep+"', '"+tempGra+"', '"+tempSec+"', '"+tempNam+"', '"+tempTea+"', '"+tempQuo+"'"+ lessonQuery + ");\n";
					fis.write(toWrite.getBytes());
					fis.flush();
					
					//the real deal
					//st.executeUpdate("INSERT INTO offerings(dep,grade,section,name,teacher,quota,lec1,lec2,lec3,lec4,lec5) VALUES('"
					//		+tempDep+"', '"+tempGra+"', '"+tempSec+"', '"+tempNam+"', '"+tempTea+"', '"+tempQuo+"'"+ lessonQuery + ");");

					//empty up variables for reuse
					lessonQuery = "";
					exec = 0;
				}
				System.out.println(dep[d]+" dumped successfully!");
			} catch (Exception e) {
				System.out.println("Error: "+e);
			}
		}
		
		//closes the file stream
		try {
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
