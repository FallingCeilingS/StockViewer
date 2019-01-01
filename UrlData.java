import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlData {
    private static String url1 = "https://quotes.wsj.com/";
    private static String url2 = "/historical-prices/download?MOD_VIEW=page&num_rows=300&startDate=";
    private static String url3 = "&endDate=";
    public static String ticker;
    public static String startDate;
    public static String endDate;
    public static boolean validation = false;
    public static String url;
    private static String errMsgTicker = "Invalid Input of Ticker. Please Select a Ticker!";
    private static String errMsgStartDate = "Invalid Input of Start Date. Please Select a Date!";
    private static String errMsgEndDate = "Invalid Input of End Date. Please Select a Date!";
    public static String errMsg = "";

    public static File file;
    public static String filename = "HistoricalPrices.csv";

    private static String parseTicker(String val) {
        String ticker = "";
        for (int i = 0; i < val.length(); i++) {
            if (val.substring(i, i + 1).equals(" ")) {
                ticker = val.substring(0, i).trim();
                break;
            }
        }
        return ticker;
    }

    public static void setUrl() {
       if (ticker != null && startDate != null && endDate != null) {
           validation = true;
           url = url1 + parseTicker(ticker) + url2 + startDate + url3 + endDate;
           System.out.println(url);
       }
    }

    public static void setValidation() {
        errMsg = "";
        if (ticker == null) {
            validation = false;
            errMsg = errMsg + "\n" + errMsgTicker;
        }
        if (startDate == null) {
            validation = false;
            errMsg = errMsg + "\n" + errMsgStartDate;
        }
        if (endDate == null) {
            validation = false;
            errMsg = errMsg + "\n" + errMsgEndDate;
        }
    }

    public static void saveUrlAs() {
        file = new File(System.getProperty("user.dir"));
        if (validation) {
            if (!file.exists()) {
                file.mkdirs();
                System.out.println("not exist path");
            } else {
                System.out.println(file.getPath());
                System.out.println(file.getAbsolutePath());
            }

            FileOutputStream fileOutputStream;
            HttpURLConnection httpURLConnection;
            InputStream inputStream;

            try {
                URL httpUrl = new URL(url);

                httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(true);
                httpURLConnection.connect();

                inputStream = httpURLConnection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                fileOutputStream = new FileOutputStream(System.getProperty("user.dir") + "/" + filename);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

                byte[] bytes = new byte[4096];
                int length = bufferedInputStream.read(bytes);

                while (length != -1) {
                    bufferedOutputStream.write(bytes, 0, length);
                    length = bufferedInputStream.read(bytes);
                }

                bufferedOutputStream.close();
                bufferedInputStream.close();
                httpURLConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}