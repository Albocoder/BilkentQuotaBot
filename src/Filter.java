import java.util.ArrayList;

/*
 * ==========================================================
 * @Author {Erin Avllazagaj}
 * @Version 2.0
 * ==========================================================
 * This class will handle filtering of the data according
 * to different parameters
 * ==========================================================
 * Date: 23/3/2015
 * Date: 26/1/2018 (remake)
 * */

//TODO: Add better and more useful filters
public class Filter {
	// field
	private ArrayList<InfoHolder> toFilter;
	
	// constructor
	public Filter( ArrayList<InfoHolder> param) { toFilter = param; }
	
	//	Attention! Policy name is only the first thing
	public void teacherNameFilter(String teacherName) {
		ArrayList<InfoHolder> toReturn = new ArrayList<InfoHolder>();
		for(int x = 0; x < toFilter.size(); x++) {
			String tmp = toFilter.get(x).getCourseTeacher();
			if( !tmp.substring(0,tmp.indexOf(" ")).equalsIgnoreCase(teacherName) )
				toReturn.add(toFilter.get(x));
		}
	}
	
	public void teacherSNameFilter(String teacherSName) {
		ArrayList<InfoHolder> toReturn = new ArrayList<InfoHolder>();
		for(int x = 0; x < toFilter.size(); x++) {
			String tmp = toFilter.get(x).getCourseTeacher();
			if( !tmp.substring(tmp.lastIndexOf(" ")+1).equalsIgnoreCase(teacherSName) )
				toReturn.add(toFilter.get(x));
		}
	}
	
	public ArrayList<InfoHolder> idFilter(String idFilter) {
		ArrayList<InfoHolder> toReturn = new ArrayList<InfoHolder>();
		for(int x = 0; x< toFilter.size(); x++){
			if( toFilter.get(x).getId().equals(idFilter) )
				toReturn.add(toFilter.get(x));
		}
		return toReturn;
	}
	
	public ArrayList<InfoHolder> getArray(){ return toFilter; }
	public String toString(){
		String toReturn = "Initial array: \n"; 
		for(int c = 0; c < toFilter.size(); c++)
			toReturn += toFilter.get(c).toString();
		return toReturn;
	}
}