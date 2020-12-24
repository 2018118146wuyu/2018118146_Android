package tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Getbihua {

    public int getbihua(String keywords) throws IOException {
        //搜索的url
        String keyUrl = "https://dict.baidu.com/s?wd=" + keywords + "&from=zici";
        System.out.println(keyUrl);
        String startNode = "<label>笔 画</label>";

        String rLine;

        LinkedHashMap<String, String> keyMap = new LinkedHashMap<String, String>();

        //开始网络请求
        URL url = new URL(keyUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream(), "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        int flag = 0;
        String b = "";
        while ((rLine = bufferedReader.readLine()) != null) {
            if (flag == 1) {
                System.out.println(rLine);
                String a = rLine.split(">")[1];
                System.out.println(a);
                b = a.split("<")[0];
                System.out.println(b);
                break;
            }
            if (rLine.contains(startNode)) {
                flag = 1;
            }
        }
        return Integer.parseInt(b);
    }

}
