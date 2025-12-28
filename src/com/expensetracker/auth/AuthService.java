package com.expensetracker.auth;
import com.expensetracker.utils.HashUtils;
import java.io.*;
import java.util.Scanner;

public class AuthService {
    private static final String USER_FILE="data/users.txt";

    public boolean register(String username, String password){
        if(userExists(username)){
            return false;
        }
        String hash=HashUtils.sha256(password);

        try(BufferedWriter bw=new BufferedWriter(new FileWriter(USER_FILE, true))){
            bw.write(username+","+hash);
            bw.newLine();
            return true;
        } catch (IOException e){
            throw new RuntimeException("Error writing user file");
        }
    }

    private boolean userExists(String username){
        try(Scanner sc=new Scanner(new File(USER_FILE))){
            while (sc.hasNextLine()){
                if(sc.nextLine().startsWith(username+",")){
                    return true;
                }
            }
        } catch (IOException e){
            return false;
        }
        return false;
    }

    public boolean login(String username, String pass){
        String hash=HashUtils.sha256(pass);

        try(Scanner sc=new Scanner(new File(USER_FILE))){
            while (sc.hasNextLine()){
                String[] pts= sc.nextLine().split(",");
                if(pts[0].equals(username) && pts[1].equals(hash)){
                    return true;
                }
            }
        } catch (IOException e){
            throw new RuntimeException("Error reading user file");
        }
        return false;
    }
}
