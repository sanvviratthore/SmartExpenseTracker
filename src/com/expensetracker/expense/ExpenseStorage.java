package com.expensetracker.expense;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseStorage {
    private  static final String FILE_PATH = "data/expenses.txt";

    public static void saveExpenses(List<Expense> expenses){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))){
            for(Expense e : expenses){
                writer.write(e.toFileString());
                writer.newLine();
            }
        } catch (IOException e){
            System.out.println("Error saving expenses.");
        }
    }

    public static List<Expense> loadExpenses(){
        List<Expense> expenses = new ArrayList<>();

        File file = new File(FILE_PATH);
        if (!file.exists()) return expenses;

        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))){
            String line;
            while ((line=reader.readLine()) !=null){
                Expense e = Expense.fromFileString(line);
                if (e != null) {
                    expenses.add(e);
                }
            }
        } catch (IOException e){
            System.out.println("Error loading expenses.");
        }
        return expenses;
    }
}
