package com.expensetracker.utils;
import java.security.MessageDigest;

public class HashUtils {
    public static String sha256(String input){
        try{
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            byte[] hashBytes=md.digest(input.getBytes());
            StringBuilder sb= new StringBuilder();
            for(byte b:hashBytes){
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e){
            throw new RuntimeException("Error hashing password");
        }
    }
}
