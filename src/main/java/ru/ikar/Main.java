package ru.ikar;

import java.io.*;
import java.io.IOException;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {
    public static void main(String[] args) throws IOException {
        String[] products = {"Хлеб", "Яблоки", "Молоко"};
        int[] prices = {100, 200, 300};
        ClientLog cl = new ClientLog();

        System.out.println("Список возможных товаров для покупки");
        for (int i = 0; i < products.length; i++) {
            System.out.printf("%d. %s %d руб/шт\n", i + 1, products[i], prices[i]);
        }

        //File f = new File("basket.txt");
        File f = new File("basket.json");
        Basket basket;
        if(f.exists()) {
            String s = "";
            String n = "";
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                while ((n = br.readLine()) != null) {
                    s += n;
                }
            }catch(IOException ex){
                System.out.println(ex.getMessage());
            }
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            basket = gson.fromJson(s, Basket.class);
            //basket = Basket.loadFromTxtFile(f);
            basket.printCart();
        }else{
            basket = new Basket(products, prices);
        }

        while (true) {
            System.out.println("Выберите товар и количество или введите `end` ");
            Scanner sc = new Scanner(System.in);
            String answer = sc.nextLine();
            if(answer.equals("end")) break;
            String[] st = answer.split(" ");
            basket.addToCart(Integer.parseInt(st[0])-1, Integer.parseInt(st[1]));
            cl.log(Integer.parseInt(st[0])-1, Integer.parseInt(st[1]));
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            String json = gson.toJson(basket);
            try(FileWriter writer = new FileWriter("basket.json")) {
                writer.write(json);
            }
            catch(IOException e){
                // Handle the exception
            }
            basket.saveTxt(f);
        }
        File fcsv = new File("log.csv");
        cl.exportAsCSV(fcsv);
        basket.printCart();

    }
}