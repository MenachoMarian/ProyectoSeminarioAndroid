package com.example.proyecto;

public class Utils {


    public static String HOST = "http://192.168.100.8:8000";


    public static String REGISTER_USER = HOST + "/v1.0/api/user";
    public static String LOGIN_USER = HOST + "/v1.0/api/login";
    public static String GET_USER = HOST + "/v1.0/api/user?email=";
    public static String GET_USER_ID = HOST + "/v1.0/api/user?id=";

    public static String GET_PRODUCT = HOST + "/v1.0/api/product";
    public static String REGISTER_PRODUCT = HOST + "/v1.0/api/product";
    public static String REGISTER_IMAGE_PRODUCT = HOST + "/v1.0/api/product/uploadImg?id=";
    public static String SEARCH_PRODUCT = HOST + "/v1.0/api/product?nombre=cadena";
    public static String GET_PRODUCT_ID = HOST + "/v1.0/api/product?id=";
   // public static String GET_IMAGEN = HOST + "/v1.0/api/imagen";

    public static String REGISTER_CITA = HOST + "/v1.0/api/cita";
    public static String GET_CITA = HOST + "/v1.0/api/cita";

    public static String TOKEN = "";
    public static String ID_USER = "";
    public static String EMAIL_USER = ""; //A la hora del login se guardarà el email para listar solo sus productos en su perfil

}
