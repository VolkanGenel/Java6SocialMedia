package com.volkan.constants;

public class ApiUrls {
    /**
     * Uygulamanız içinde kullanılan tüm erişim noktalarının listesi burada tutulur,
     * böylece farklı ortamlar i.in kullanılacak end pointler tek bir noktadan değiştirilebilir.
     */
    public static final String VERSION = "api/v1";
    public static final String AUTH = VERSION+"/auth";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String UPDATE = "/update";
    public static final String CREATE = "/create";
    public static final String DELETEBYID = "/deletebyid";
    public static final String FINDBYID = "/findbyid";
    public static final String FINDBYROLE = "/findbyrole";
    public static final String FINDALL = "/findall";
    public static final String ACTIVATESTATUS= "/activatestatus";
    public static final String DEACTIVATESTATUS= "/deactivatestatus";

}
