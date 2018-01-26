import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * Version: 2 (JSoup implementation)
 * */
public class HttpsClient{
	//constants 
	private static final int CAPTCHA_CODE = 302;
	// fields
	private String http_url;
	private String cookie;
	private Connection.Method method;
	
	// constructors
	public HttpsClient(String URL,String sessCookie,Connection.Method method){
		http_url = URL;
		cookie = sessCookie;
		this.method = method;
	}
	public HttpsClient(String URL,String sessCookie){
		this(URL,sessCookie,Method.GET);
	}
	public HttpsClient(String URL){
		this(URL,"",Method.GET);
	}

	private String returnContent(){
		Connection.Response resp;
		try {
			resp = Jsoup.connect(http_url)
					.cookie("PHPSESSID",cookie)
					.method(method)
					.execute();
			// TODO fix this to return here!
			if (resp.statusCode() == CAPTCHA_CODE) {
				askForCookie();
				resp = Jsoup.connect(http_url)
						.cookie("PHPSESSID",cookie)
						.method(method)
						.execute();
			}
		} catch (IOException e) {return "";}
		return resp.body();
	}
	// TODO fix this to work
	private void askForCookie() {
		Connection.Response resp;
		try {
			resp = Jsoup.connect("https://stars.bilkent.edu.tr/homepage/captcha.php").cookie("PHPSESSID",cookie).method(Method.GET).execute();
			cookie = resp.cookie("PHPSESSID");
			Document d = resp.parse();
			Elements e = d.getElementsByTag("img");
			Element image = e.get(0);
			String source = image.attr("src");
			resp = Jsoup.connect("https://stars.bilkent.edu.tr"+source).cookie("PHPSESSID",cookie).method(Method.GET).execute();
			BufferedInputStream bis = resp.bodyStream();
			byte [] buffer = new byte[1024];
			File f = new File("image.png");
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			int read;
			while( (read = bis.read(buffer))!=-1)
				fos.write(buffer, 0, read);
			fos.close();
			bis.close();
			Scanner s = new Scanner(System.in);
			System.out.print("Please write the captcha in the image: ");
			String captcha = s.nextLine();
			Jsoup.connect("https://stars.bilkent.edu.tr/homepage/captcha.php").cookie("PHPSESSID",cookie).data("user_code",captcha.trim()).method(Method.POST).execute();
		} catch (IOException e) {}
	}
	public String getOfferings(){
		String websiteContents = returnContent();
		if(websiteContents.contains("no course"))
			return "";
		return websiteContents;
	}
}