package com.csv.utility;

import com.csv.entity.Car;
import com.csv.exceptions.CSVException;
import com.csv.modal.CarDto;
import org.apache.commons.csv.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVUtility {
    private static final List<String> HEADINGFORFILE = Arrays.asList("Registration Number", "Rto", "Registration " +
            "State", "Registration Year", "mileage", "make", "model", "BodyType", "Car score", "variant", "chassis Number", "colour", "Year of Manufacture");
    private static final String FILETYPE = "text/csv";

    private CSVUtility() {
    }

    public static boolean hasCSVFormat(MultipartFile file) {
        try {
            return FILETYPE.equals(file.getContentType());
        } catch (Exception e) {
            throw new CSVException(e.getMessage());
        }
    }

    public static List<CarDto> csvToDatabase(InputStream inputStream) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)); CSVParser csvParser = new CSVParser(bufferedReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            List<CarDto> carList = new ArrayList<>();
            for (CSVRecord i : csvRecords) {
                CarDto carModal = new CarDto((i.get("regNumber")), (i.get("rto")), i.get("registrationState"), Integer.parseInt(i.get("registrationYear")), i.get("make"), i.get("model"), Integer.parseInt(i.get("mileage")), i.get("bodyType"), Integer.parseInt(i.get("carScore")), i.get("variant"), i.get("chassisNumber"), i.get("colour"), Integer.parseInt(i.get("yearOfManufacture")));
                carList.add(carModal);
            }
            return carList;
        } catch (IOException e) {
            throw new CSVException("failed to parse CSV file");
        }
    }

    public static ByteArrayInputStream databaseToCSV(List<Car> carList) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            csvPrinter.printRecord(HEADINGFORFILE);
            for (Car car : carList) {
                List<String> data = Arrays.asList(car.getRegNumber(), car.getRto(), car.getRegistrationState(), String.valueOf(car.getRegistrationYear()), String.valueOf(car.getMileage()), car.getBodyType(), String.valueOf(car.getCarScore()), car.getMake(), car.getModel(), car.getVariant(), car.getChassisNumber(), car.getColour(), String.valueOf(car.getYearOfManufacture()));
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new CSVException("Unable to write data to a CSV file");
        }
    }
}

