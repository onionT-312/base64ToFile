import java.io.*;
import java.nio.file.*;
import java.util.Base64;

public class Main {

    public static void main(String[] args) {
        //input path
        String inputBase64 = "input.txt";

        //output path
        String outputBase64 = "output";

        try {
            String base64Data = readFile(inputBase64);

            //convert Base64 to file
//            decodeBase64(base64Data, outputBase64 + ".png");              //image
//            decodeBase64(base64Data, outputBase64 + ".xlsx");             //excel
            decodeBase64(base64Data, outputBase64 + ".pdf");        //pdf

            System.out.println("File has decoded successfully!");

        } catch (IOException e) {
            System.out.println("File has decoded failed!" + e.getMessage());
        }

    }

    public static String sanitizeBase64(String data) {
        return data.replaceAll("\\s", "");
    }

    public static void decodeBase64(String data, String output) throws IOException {
        data = sanitizeBase64(data);

        if(data.contains(",")) {
            data = data.split(",")[1];
        }

        //decode string base64
        byte[] decodedBytes = Base64.getDecoder().decode(data);

        //write data to file
        try (FileOutputStream fos = new FileOutputStream(output)) {
            fos.write(decodedBytes);
        }
    }

    //read file .txt
    public static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

}