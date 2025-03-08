// Import necessary Java libraries
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;  // Import JSON library to parse API response

public class SimpleWeatherApp {
    // OpenWeatherMap API key 
    private static final String API_KEY = "85538d49d8f7b9b98e22877282d29b0d";  
    // City name for which we want to fetch weather data
    private static final String CITY = "Chennai";  

    public static void main(String[] args) {
        try {
            // Construct the API URL using the city name and API key
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=Chennai&appid=85538d49d8f7b9b98e22877282d29b0d&units=metric";

            // Debugging: Print the API URL
            System.out.println("Requesting URL: " + apiUrl);

            // Create a URL object with the API endpoint
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                
            // Set the request method to GET (because we are retrieving data)
            connection.setRequestMethod("GET");

            // Get the HTTP response code from the API (200 means success)
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {  // If response is successful
                // Read the API response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                // Read each line of the API response and append it to response
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();  // Close the BufferedReader after reading data
                
                // Call method to parse and display weather data
                displayWeatherData(response.toString());

            } else {
                // Print error message if response code is not 200
                System.out.println("Error: Unable to fetch data. Response Code: " + responseCode);
            }

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());  // Print the error message
        }
    }

    // Method to parse JSON response and display weather data
    private static void displayWeatherData(String jsonResponse) {
        try {
            // Convert the JSON response string into a JSON object
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Check if the API returned an error message
            if (jsonObject.has("cod") && jsonObject.getInt("cod") != 200) {
                System.out.println("API Error: " + jsonObject.getString("message"));
                return;
            }

            // Extract the city name from the JSON data
            String city = jsonObject.getString("name");

            // Extract the main weather details (temperature, humidity)
            JSONObject main = jsonObject.getJSONObject("main");
            double temperature = main.getDouble("temp");  // Get temperature in Celsius
            int humidity = main.getInt("humidity");  // Get humidity percentage

            // Extract weather description (e.g., "clear sky", "rainy")
            JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
            String description = weather.getString("description");

            // Print the weather data in a structured format
            System.out.println("\n====== Weather Report ======");
            System.out.println("City: " + city);
            System.out.println("Temperature: " + temperature + "°C");
            System.out.println("Humidity: " + humidity + "%");
            System.out.println("Condition: " + description);
            System.out.println("==============================\n");

        } catch (Exception e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
        }
    }
}
