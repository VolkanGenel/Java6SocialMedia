package com.volkan.constants;

public class ApiUrls {
    /**
     * Uygulamanız içinde kullanılan tüm erişim noktalarının listesi burada tutulur,
     * böylece farklı ortamlar i.in kullanılacak end pointler tek bir noktadan değiştirilebilir.
     */
    public static final String VERSION = "api/v1";
    public static final String POST= VERSION+"/post";
    public static final String LIKE= VERSION+"/like";
    public static final String COMMENT= VERSION+"/comment";
    public static final String DISLIKE= VERSION+"/dislike";


    //PostController
    public static final String CREATE = "/create";
    public static final String LOGIN = "/login";
    public static final String UPDATE = "/update";
    public static final String DELETEBYID = "/deletebyid";
    public static final String FINDBYID = "/findbyid";
    public static final String FINDBYROLE = "/findbyrole";
    public static final String FINDBYUSERNAME = "/findbyusername";
    public static final String FINDALL = "/findall";

}
