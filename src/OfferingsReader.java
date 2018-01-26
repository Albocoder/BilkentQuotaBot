
/*
 * ==========================================================
 * @Author {Erin Avllazagaj}
 * @Version 1.8
 * ==========================================================
 * This reads the whole offerings page and makes a new file 
 * caller myGrabbedWebsite.html almost the same as original
 * ==========================================================
 * Date: 23/3/2015
 * Version 2.1
 * 
 * Date: 25/1/2018
 * Version 2.2 (jsoup implementation)
 * 
 * */
public class OfferingsReader {
	//properties
	private String courseID;
	private HttpsClient scanner;
	
	public OfferingsReader( String id ){
		courseID = id;
	}
	
	//Gets the current date and month and predicts what user wants to see...
	private static String timeSpecifier(){
		String toReturn = "";
		String monthGetter, yearGetter;
		int month, year;
		
		//gets the epoch time and converts it to readable string
		long epoch = System.currentTimeMillis()/1000;
		String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
		.format(new java.util.Date (epoch*1000));

		//Getting String for year and month
		yearGetter = date.substring(6, 10);
		monthGetter = date.substring(0, 2);
			
		//Parsing integers from Strings
		year = Integer.parseInt(yearGetter);
		month = Integer.parseInt(monthGetter);
			
			//filling toReturn String according to dates(must revise as well)
		if (month >= 1 && month <= 5){
			year--;
			toReturn += year;
			toReturn += "2";
		}
		else if (month >= 6 && month <= 7){
			year--;
			toReturn += year;
		   	toReturn += "3";
		}
		else {
			toReturn += year;
		   	toReturn += "1";
		}
		return toReturn;
	}
	
	public String getExpected(String cookie){
		courseID = courseID.toUpperCase();
		//makes an HttpsClient for a specific url
		scanner = new HttpsClient("https://stars.bilkent.edu.tr/homepage/print/plainOfferings.php?COURSE_CODE="
				+ courseID +"&SEMESTER=" + timeSpecifier(),cookie);
		return scanner.getOfferings();
	}
	// Overloaded (newest version adaptation to backwards compatability)
	public String getExpected(){ return getExpected("");}
	
	
	//Getter
	protected String getID(){
		return courseID;
	}
	
	//Setter
	protected void setID( String id ){
		courseID = id;
	}
	
}