package actiknow.com.atmncr.utils;

public class AppConfigURL {

    //    static String host = "actiknow-demo.com";
    static String host = "10.0.3.2";
//    static String host = "callsikandar.com";
//    static String host = "indiasupply.com";

    static String folder_name = "atm_ncr";

    public static String URL_GETALLATMS = "http://" + host + "/" + folder_name + "/getallatms.php";
    public static String URL_GETATMDETAILS = "http://" + host + "/" + folder_name + "/getatmdetails.php";
    public static String URL_SUBMITRESPONSE = "http://" + host + "/" + folder_name + "/submitresponse.php";
    public static String URL_REFRESHRESPONSE = "http://" + host + "/" + folder_name + "/refreshresponse.php";
}
