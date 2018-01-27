import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * ==========================================================
 * @Author {Erin Avllazagaj}
 * @Version 2.7
 * ==========================================================
 * This will search for the data in the offering URL.
 * And then will return data accordingly in an ArrayList
 * of InfoHolders. 
 * ==========================================================
 * Date: 23/3/2015
 * Version: 2.0
 * 
 * Date: 2/4/2015
 * Version: 2.4
 * 
 * Date: 9/9/2015
 * Version: 2.6
 * 
 * Date: 25/1/2017
 * Version: 2.7 (Using JSoup and improving efficiency drammatically)
 * */
public class DataSearcher extends OfferingsReader {
	private String websiteContents;
	private int section, code;
	private int rownumbersinglesearch;

	// modified for backwards compatibility
	public DataSearcher(String id) { this( id,"", 0,0); }
	//by default can select all courses
	public DataSearcher(String id,String cookie) { this( id, cookie, 0,0); }
	public DataSearcher(String id, int c, int sec) { this(id,"",c,sec); }
	public DataSearcher(String id,String cookie, int c, int sec) {
		super(id);
		id = id.toUpperCase();
		section = sec;
		code = c;
		websiteContents = super.getExpected(cookie);
		if(websiteContents.equals(""))
			throw new RuntimeException("Website has no content or error on the response!");
		rownumbersinglesearch = -1;
	}

	// this is used to find all courses of that department
	public ArrayList<InfoHolder> findAll() throws NullPointerException{
		Document parsed = Jsoup.parse(websiteContents);
		Element table = parsed.getElementById("poTable");
		Element tableBody = table.getElementsByTag("tbody").get(0);
		Elements tableRows = tableBody.getElementsByTag("tr");
		String regex = "((Mon)?(Tue)?(Wed)?(Thu)?(Fri)?(Sat)?(Sun)?) [0,1,2]?[0-9]:[0-9][0-9]-([0,1,2])?[0-9]:[0-9][0-9] ?([a-zA-Z0-9]+-[a-zA-Z0-9]+)?(\\[[a-zA-Z0-9]+\\])?";

		ArrayList<InfoHolder> toReturn = new ArrayList<>();	
		for(Element row : tableRows) {
			// get the row
			Elements data = row.children();

			// parse course code
			String courseCode = data.get(0).text();
			String dept = super.getID().toUpperCase();//courseCode.substring(0,courseCode.indexOf(" "));
			int c = Integer.parseInt(courseCode.substring(courseCode.indexOf(" ")+1,courseCode.indexOf("-")));
			int s = Integer.parseInt(courseCode.substring(courseCode.indexOf("-")+1));

			// parse other info
			String courseName = data.get(1).text();
			String courseInstructor = data.get(2).text();
			String courseCredits = data.get(3).text();
			String courseEcts = data.get(4).text();
			String courseHours = data.get(5).text();
			String courseTotalQuota = data.get(7).text();
			String courseAvailQuota = data.get(12).text();

			List<String> matches = new ArrayList<String>(5);
			Matcher m = Pattern.compile(regex).matcher(courseHours);
			while(m.find())
				matches.add(m.group(0));

			String [] info = new String[9+matches.size()];
			info[0] = dept;
			info[1] = ""+c;
			info[2] = ""+s;
			info[3] = courseName;
			info[4] = courseInstructor;
			info[5] = courseCredits;
			info[6] = courseEcts;
			for(int i = 0; i < matches.size(); i++)
				info[7+i] = matches.get(i);
			info[7+matches.size()] = courseTotalQuota;
			info[7+matches.size()+1] = courseAvailQuota;
			toReturn.add(new InfoHolder(info));
		}
		return toReturn;
	}
	// this is used to find a single course and is very efficient for many requests
	public InfoHolder singleSearch() {
		Document parsed = Jsoup.parse(websiteContents);
		Element table = parsed.getElementById("poTable");
		Element tableBody = table.getElementsByTag("tbody").get(0);
		Elements tableRows = tableBody.getElementsByTag("tr");
		String regex = "((Mon)?(Tue)?(Wed)?(Thu)?(Fri)?(Sat)?(Sun)?) [0,1,2]?[0-9]:[0-9][0-9]-([0,1,2])?[0-9]:[0-9][0-9] ?([a-zA-Z0-9]+-[a-zA-Z0-9]+)?(\\[[a-zA-Z0-9]+\\])?";

		if(rownumbersinglesearch != -1) {
			Elements data = tableRows.get(rownumbersinglesearch).children();
//			String courseCode = data.get(0).text();
			String dept = super.getID().toUpperCase();//courseCode.substring(0,courseCode.indexOf(" "));
			String courseName = data.get(1).text();
			String courseInstructor = data.get(2).text();
			String courseCredits = data.get(3).text();
			String courseEcts = data.get(4).text();
			String courseHours = data.get(5).text();
			String courseTotalQuota = data.get(7).text();
			String courseAvailQuota = data.get(12).text();

			List<String> matches = new ArrayList<String>(5);
			Matcher m = Pattern.compile(regex).matcher(courseHours);
			while(m.find())
				matches.add(m.group(0));

			String [] info = new String[9+matches.size()];
			info[0] = dept;
			info[1] = ""+code;
			info[2] = ""+section;
			info[3] = courseName;
			info[4] = courseInstructor;
			info[5] = courseCredits;
			info[6] = courseEcts;
			for(int i = 0; i < matches.size(); i++)
				info[7+i] = matches.get(i);
			info[7+matches.size()] = courseTotalQuota;
			info[7+matches.size()+1] = courseAvailQuota;
			return new InfoHolder(info);
		}
		for(int index = 0; index < tableRows.size();index++) {
			// get the row
			Elements data = tableRows.get(index).children();

			// parse course code
			String courseCode = data.get(0).text();
			String dept = courseCode.substring(0,courseCode.indexOf(" "));
			int c = Integer.parseInt(courseCode.substring(courseCode.indexOf(" ")+1,courseCode.indexOf("-")));
			int s = Integer.parseInt(courseCode.substring(courseCode.indexOf("-")+1));

			// check exit conditions
			if (c < code)
				continue;
			else if (code == c && section != s)
				continue;
			else if (c > code)
				return null;

			// this happens only when code == c and section == s
			String courseName = data.get(1).text();
			String courseInstructor = data.get(2).text();
			String courseCredits = data.get(3).text();
			String courseEcts = data.get(4).text();
			String courseHours = data.get(5).text();
			String courseTotalQuota = data.get(7).text();
			String courseAvailQuota = data.get(12).text();

			List<String> matches = new ArrayList<String>(5);
			Matcher m = Pattern.compile(regex).matcher(courseHours);
			while(m.find())
				matches.add(m.group(0));

			String [] info = new String[9+matches.size()];
			info[0] = dept;
			info[1] = ""+c;
			info[2] = ""+s;
			info[3] = courseName;
			info[4] = courseInstructor;
			info[5] = courseCredits;
			info[6] = courseEcts;
			for(int i = 0; i < matches.size(); i++)
				info[7+i] = matches.get(i);
			info[7+matches.size()] = courseTotalQuota;
			info[7+matches.size()+1] = courseAvailQuota;
			rownumbersinglesearch = index;
			return new InfoHolder(info);
		}
		return null;
	}
	// this is used to get quota of a course and is very efficient to changes
	public int singleSearchAvailQuota() {
		Document parsed = Jsoup.parse(websiteContents);
		Element table = parsed.getElementById("poTable");
		Element tableBody = table.getElementsByTag("tbody").get(0);
		Elements tableRows = tableBody.getElementsByTag("tr");
		if(rownumbersinglesearch != -1) {
			Elements data = tableRows.get(rownumbersinglesearch).children();
			return Integer.parseInt(data.get(12).text());
		}
		for(int index = 0; index < tableRows.size();index++) {
			// get the row
			Elements data = tableRows.get(index).children();

			// parse course code
			String courseCode = data.get(0).text();
			int c = Integer.parseInt(courseCode.substring(courseCode.indexOf(" ")+1,courseCode.indexOf("-")));
			int s = Integer.parseInt(courseCode.substring(courseCode.indexOf("-")+1));

			// check exit conditions
			if (c < code)
				continue;
			else if (code == c && section != s)
				continue;
			else if (c > code)
				return -1;

			// this happens only when code == c and section == s
			rownumbersinglesearch = index;
			return Integer.parseInt(data.get(12).text());
		}
		return -1;

	}

	// getters
	public int getCode() { return code; }
	public int getSection() { return section; }
	public String getWebsite() { return websiteContents; }
	@Override
	public String getID() { return super.getID()+" "+code+"-"+section; }
}