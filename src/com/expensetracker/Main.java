package com.expensetracker;
import com.expensetracker.auth.AuthService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        AuthService auth = new AuthService();

        System.out.println("1. Register");
        System.out.println("2. Login");
        int choice = sc.nextInt();
        sc.nextLine();
        System.out.println("Username: ");
        String username=sc.nextLine();
        System.out.println("Password: ");
        String pass=sc.nextLine();

        if(choice==1){
            System.out.println(auth.register(username, pass)
            ? "Registration successful"
            : "User already exists");
        } else{
            System.out.println(auth.login(username, pass)
            ? "Login successful"
            : "Invalid credentials");
        }
    }
}
