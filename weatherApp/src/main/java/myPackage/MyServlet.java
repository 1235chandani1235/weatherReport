package myPackage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;
import java.io.InputStreamReader;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	
		String apikey = "03992f73c58618ae9ab3ee31a6c1ffc3";
		String city = request.getParameter("city");
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apikey + "";
		URL url = new URL(apiUrl);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		InputStream inputStream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputStream);
		
	//	Scanner scanner = new Scanner(reader);
		StringBuilder responseContent = new StringBuilder();
        
		
		Scanner scanner = new Scanner(reader);
		
		while(scanner.hasNext())
		{
			responseContent.append(scanner.nextLine());
		}
		scanner.close();
		
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(),JsonObject.class);
		System.out.println(jsonObject);
		
		
		long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000 ;
		String date = new Date(dateTimestamp).toString();
		
		double tempratureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int tempratureCelsius = (int)(tempratureKelvin - 273.15);
		
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
	
		
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperature", tempratureCelsius);
		request.setAttribute("weatherCondition", weatherCondition);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("weatherData", responseContent.toString());
		
		connection.disconnect();
		
		request.getRequestDispatcher("index.jsp").forward(request,response);
	}


	

}
