import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Config {
	// static fields for singleton access mode
	private String cookie;
	private ArrayList<DataSearcher> targets;
	
	// constructor
	public Config(String filename) throws IOException {
		targets = new ArrayList<DataSearcher>();
		Document doc = Jsoup.parse(new File(filename),"UTF-8");
		Elements cookies = doc.getElementsByTag("cookie");
		Elements targetCourses = doc.getElementsByTag("target");
		Element c = cookies.get(0);
		cookie = c.text();
		for(Element t: targetCourses) {
			Elements courseSpecs = t.children();
			String dept = courseSpecs.get(0).text();
			int code = Integer.parseInt(courseSpecs.get(1).text());
			int sect = Integer.parseInt(courseSpecs.get(2).text());
			targets.add(new DataSearcher(dept,cookie,code,sect));
		}
	}
	public Config() throws IOException { this("config.xml"); }
	
	// getters
	public ArrayList<DataSearcher> getTargets(){ return targets; }
	public String getCookie() { return cookie; }
}