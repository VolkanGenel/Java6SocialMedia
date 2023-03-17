package com.volkan.utility;

import java.util.UUID;

public class CodeGenerator {
    public static String generateCode() {
        String code = UUID.randomUUID().toString();
       String [] data = code.split("-");
       String newCode ="";
        for (String string: data) {
            newCode += string.charAt(0);
        }
        return newCode;
    }

    public static void main(String[] args) {
        System.out.println(generateCode());
    }
}
