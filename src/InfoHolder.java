/*
 * ==========================================================
 * @Author {Erin Avllazagaj}
 * @Version 3.0
 * ==========================================================
 * This class will hold info for the course like:
 * course ID, course name, course teacher, course hours...
 * ==========================================================
 * Date: 23/3/2015
 * Date: 8/9/2015 (remake)
 * Date: 25/1/2018 (remake to support better atomization)
 * 
 * */
public class InfoHolder {
	// fields
	
	// identifier
	private String dept;
	private int code,section;
	// name
	private String courseName;
	private String courseTeacher;
	// credits
	private String credit,ects;
	// lecture time and place
	private String[] lessonAndBuilding;
	// quota
	private int totalQuota, availQuota;
	
	// constructor
	public InfoHolder(String [] info) {
		dept = info[0];
		code = Integer.parseInt(info[1]);
		section = Integer.parseInt(info[2]);
		courseName = info[3];
		courseTeacher = info[4];
		credit = info[5];
		ects = info[6];
		int numberOfLectures = info.length-9;
		
		if(numberOfLectures == 0)
			lessonAndBuilding = null;
		else {
			lessonAndBuilding = new String[numberOfLectures];
			for(int i = 0; i < numberOfLectures; i++)
				lessonAndBuilding[i] = info[7+i];
		}
		
		if (info[7+numberOfLectures].equals("-"))
			totalQuota = 999;
		else
			totalQuota = Integer.parseInt(info[7+numberOfLectures]);
		
		if (info[7+numberOfLectures+1].equals("-"))
			availQuota = Integer.parseInt(info[7+numberOfLectures+1]);
		else
			availQuota = 999;
	}
	
	public String toString(){
		String toReturn = "\n";
		toReturn += dept+" "+code+"-"+section+"\n"+courseName+"\n"+courseTeacher+"\n";
		for(int c = 0; c < lessonAndBuilding.length; c++){
			toReturn += lessonAndBuilding[c] + "\n";
		}
		toReturn += "Quota: " + totalQuota;
		toReturn += "\n\n";
		return toReturn;
	}
	public String getDep() { return dept; }
	public int getCode() { return code; }
	public int getSection() { return section; }
	public String getId() { return dept+" "+code+"-"+section; }
	public String getCourseName() { return courseName; }
	public String getCourseTeacher() { return courseTeacher; }
	public String[] getLessonAndBuilding() { return lessonAndBuilding; }
	public int getTotalQuota(){ return totalQuota; }
	public int getAvailQuota(){ return availQuota; }
	public String getCredit() { return credit; }
	public String getEcts() { return ects; }
}