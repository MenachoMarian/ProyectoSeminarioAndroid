package com.example.proyecto;

public class Utils {
    public static String HOST = "http://192.168.1.13:8000";
    public static String REGISTER_USER = HOST + "/v1.0/api/user";
    public static String LOGIN_USER = HOST + "/v1.0/api/login";

    public static String GET_PRODUCT = HOST + "/v1.0/api/product";
    public static String REGISTER_PRODUCT = HOST + "/v1.0/api/product";

    public static String TOKEN = "";
    public static String ID_USER = "";
    public static String EMAIL_USER = null; //A la hora del login se guardarà la contraseña para listar solo sus productos en su perfil

}
