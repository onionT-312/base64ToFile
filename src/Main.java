import java.io.*;
import java.nio.file.*;
import java.util.Base64;

public class Main {

    public static void main(String[] args) {
        String inputBase64 = "src/resource/input.txt";
        String outputBase64 = "src/resource/output";

        try {
            String base64Data = readFile(inputBase64);
            String fileExtension = detectFileType(base64Data);
            if (fileExtension.equals("unknown")) {
                System.out.println("Không xác định được loại file!");
                return;
            }

            // Tạo output với phần mở rộng file phù hợp
            String outputFilePath = outputBase64 + "." + fileExtension;

            // Decode và lưu file
            decodeBase64(base64Data, outputFilePath);

            System.out.println("File đã giải mã thành công: " + outputFilePath);
        } catch (IOException e) {
            System.out.println("File giải mã thất bại: " + e.getMessage());
        }
    }

    public static String sanitizeBase64(String data) {
        return data.replaceAll("\\s", "");
    }

    public static void decodeBase64(String data, String output) throws IOException {
        data = sanitizeBase64(data);

        if (data.contains(",")) {
            data = data.split(",")[1];
        }

        // Decode Base64 thành byte[]
        byte[] decodedBytes = Base64.getDecoder().decode(data);

        // Ghi file
        try (FileOutputStream fos = new FileOutputStream(output)) {
            fos.write(decodedBytes);
        }
    }

    public static String detectFileType(String base64) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64);

        if (decodedBytes.length < 4) return "unknown"; // Dữ liệu quá ngắn

        String hex = String.format("%02X%02X%02X%02X", decodedBytes[0], decodedBytes[1], decodedBytes[2], decodedBytes[3]);

        // Kiểm tra Magic Number
        switch (hex) {
            case "89504E47": return "png";  // PNG
            case "FFD8FFE0":
            case "FFD8FFE1":
            case "FFD8FFE2":
            case "FFD8FFE3": return "jpg";  // JPG
            case "25504446": return "pdf";  // PDF
            case "504B0304": return "zip";  // ZIP hoặc DOCX/XLSX/PPTX
            case "D0CF11E0": return "doc";  // DOC (Office cũ)
            default: return "unknown"; // Không xác định
        }
    }

    public static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
