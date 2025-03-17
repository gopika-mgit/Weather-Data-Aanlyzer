import java.nio.file.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.*;

/**
 * # Weather Data Analyzer
 *
 * A Java application that processes weather data from a CSV file.
 */
public class WeatherDataAnalyzer {

    /**
     * Represents a day's weather data.
     */
    record WeatherEntry(String date, double temperature, double humidity, double precipitation) {}

    /**
     * Main method to execute the weather data analysis.
     */
    public static void main(String[] args) {
        try {
            List<WeatherEntry> weatherData = readWeatherData("src/weatherdata.csv");
            System.out.printf("Average Temperature in August: %.2f°C%n", averageTemperature(weatherData, "08"));
            System.out.println("Days above 30°C: " + hotDays(weatherData, 30));
            System.out.println("Number of rainy days: " + countRainyDays(weatherData));
        } catch (IOException e) {
            System.err.println("Error reading weather data: " + e.getMessage());
        }
    }

    /**
     * Reads weather data from a CSV file.
     *
     * @param fileName The CSV file name.
     * @return A list of weather records.
     * @throws IOException If an error occurs while reading the file.
     */
    public static List<WeatherEntry> readWeatherData(String fileName) throws IOException {
        return Files.lines(Path.of(fileName))
                .skip(1) // Skip header row
                .map(line -> line.split(","))
                .map(parts -> new WeatherEntry(parts[0], Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3])))
                .toList();
    }

    /**
     * Computes the average temperature for a given month.
     *
     * @param data List of weather records.
     * @param month Month in "MM" format.
     * @return Average temperature.
     */
    public static double averageTemperature(List<WeatherEntry> data, String month) {
        return data.stream()
                .filter(entry -> entry.date.substring(5, 7).equals(month))
                .mapToDouble(WeatherEntry::temperature)
                .average()
                .orElse(0.0);
    }

    /**
     * Finds dates where the temperature exceeded a threshold.
     *
     * @param data List of weather records.
     * @param threshold Temperature threshold.
     * @return List of dates exceeding the threshold.
     */
    public static List<String> hotDays(List<WeatherEntry> data, double threshold) {
        return data.stream()
                .filter(entry -> entry.temperature > threshold)
                .map(WeatherEntry::date)
                .toList();
    }

    /**
     * Counts the number of rainy days.
     *
     * @param data List of weather records.
     * @return Count of days with precipitation.
     */
    public static long countRainyDays(List<WeatherEntry> data) {
        return data.stream()
                .filter(entry -> entry.precipitation > 0.0)
                .count();
    }
}
