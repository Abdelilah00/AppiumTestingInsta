import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileService {
    private final static String fileLike = "followers.txt";

    public static List<String> getFollowers() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(fileLike);
        Scanner scanner = new Scanner(fis);

        List<String> proxies = new ArrayList<>();

        while (scanner.hasNextLine()) {
            proxies.add(scanner.nextLine());
        }
        return proxies;
    }

    public static void saveFollowers(List<String> proxies) throws IOException {
        File file = new File(fileLike);
        if (file.exists())
            file.delete();

        file.createNewFile();


        FileWriter fileWriter = new FileWriter(file);
        for (var proxy : proxies) {
            fileWriter.write(proxy+ '\n');
        }
        fileWriter.close();
    }
}
