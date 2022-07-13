package com.csv.utility;

import com.csv.entity.Car;
import com.csv.exceptions.CSVException;
import com.csv.modal.CarDto;
import com.csv.modal.UserDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class CSVUtility {

    private static final String[] FILE_HEADING = {"Registration Number", "Rto", "Registration " + "State", "Registration Year", "Mileage", "Make", "Model", "Body Type", "Car Score", "Variant", "Chassis " + "Number", "Colour", "Year Of Manufacture"};

    private CSVUtility() {
    }

    public static boolean hasCSVFormat(MultipartFile file, String fileType) {
        return fileType.equals(file.getContentType());

    }

    public static List<CarDto> csvToCarDto(MultipartFile file) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)); CSVParser csvParser = new CSVParser(bufferedReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            return csvParser.getRecords().stream().map(i -> CarDto.builder().make(i.get(FILE_HEADING[5])).carScore(Integer.parseInt(i.get(FILE_HEADING[8]))).bodyType(i.get(FILE_HEADING[7])).chassisNumber(i.get(FILE_HEADING[10])).regNumber(i.get(FILE_HEADING[0])).colour(i.get(FILE_HEADING[11])).model(i.get(FILE_HEADING[6])).registrationState(i.get(FILE_HEADING[2])).registrationYear(Integer.parseInt(i.get(FILE_HEADING[3]))).mileage(Integer.parseInt(i.get(FILE_HEADING[4]))).yearOfManufacture(Integer.parseInt(i.get(FILE_HEADING[12]))).rto(i.get(FILE_HEADING[1])).variant(i.get(FILE_HEADING[9])).build()).toList();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new CSVException("Invalid CSV file: please match all the headings in the file with," + Arrays.stream(FILE_HEADING).toList());
        } catch (IOException e) {
            e.printStackTrace();
            throw new CSVException("failed to parse CSV file");
        }
    }

    public static ByteArrayInputStream byteArrayInputStreamToCSV(List<Car> carList) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            csvPrinter.printRecord((Object[]) FILE_HEADING);
            carList.stream().map(car -> Arrays.asList(car.getInventoryId(), car.getRegNumber(), car.getRto(),
                    car.getRegistrationState(),
                    String.valueOf(car.getRegistrationYear()), String.valueOf(car.getMileage()), car.getBodyType(), String.valueOf(car.getCarScore()), car.getMake(), car.getModel(), car.getVariant(), car.getChassisNumber(), car.getColour(), String.valueOf(car.getYearOfManufacture()))).forEach(data -> {
                try {
                    csvPrinter.printRecord(data);
                } catch (IOException e) {
                    throw new CSVException("Unable to write data to a CSV file");
                }
            });

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new CSVException("Unable to write data to a CSV file");
        }
    }

    public static List<UserDto> csvToUserDto(MultipartFile file) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)); CSVParser csvParser = new CSVParser(bufferedReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            return csvParser.getRecords().stream().map(i -> UserDto.builder().firstName(i.get("firstName")).lastName(i.get("LastName")).address(i.get("address")).email(i.get("email")).mobileNumber(i.get("mobileNumber")).role(i.get("role")).build()).toList();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new CSVException("Invalid CSV file");
        } catch (IOException e) {
            e.printStackTrace();
            throw new CSVException("failed to parse CSV file");
        }
    }
}

