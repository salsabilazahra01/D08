package propensi.project.water.setting;

public class Settings {
    final public static String CLIENT_BASE_URL = "http://localhost:8080";
    final public static String CLIENT_LOGIN = CLIENT_BASE_URL + "/validate-ticket";
    final public static String CLIENT_LOGOUT = CLIENT_BASE_URL + "/logout";
    final public static String SERVER_BASE_URL = "https://sso.ui.ac.id/cas2";
    final public static String SERVER_LOGIN = SERVER_BASE_URL + "/login?service=";
    final public static String SERVER_LOGOUT = SERVER_BASE_URL + "/logout?url=";
    final public static String SERVER_VALIDATE_TICKET = SERVER_BASE_URL + "/serviceValidate?ticket=%s&service=%s";
}
