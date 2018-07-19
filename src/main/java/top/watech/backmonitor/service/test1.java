package top.watech.backmonitor.service;

import java.net.URL;
import java.net.URLConnection;

/**
 * Created by wuao.tp on 2018/7/12.
 */
public class test1 {
    public static void main(String[] args) {
        try {
//            URL url=new URL("http://www.wetach.com/");
            URL url=new URL("http://www.baidu.com/");

            URLConnection conn=url.openConnection();
            String str=conn.getHeaderField(0);
            System.out.println(conn.getConnectTimeout());
            for (int i = 0; i < 100; i++) {
                System.out.println( conn.getHeaderField(i));

            }


            if (str.indexOf("OK")> 0){
                System.out.println("网址正常");
                //网址正常
            }else{
                System.out.println("网址不正常");
                //网址不正常
            }
        } catch (Exception ex) {
            System.out.println("网址不正常");
        }
    }
}
