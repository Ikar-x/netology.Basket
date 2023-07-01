package ru.ikar;

import java.io.*;
import java.io.IOException;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Main {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        boolean loadBasket = false;
        boolean saveBasket = false;
        boolean saveLog = false;
        String nameFileBasketWrite = "basket.json";
        String nameFileBasketRead = "basket.json";
        String nameFileLog = "log.csv";
        String formatFileBasketWrite = "json";
        String formatFileBasketRead = "json";
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = docBuilder.parse(new File("shop.xml"));
        Node root = doc.getDocumentElement();
        NodeList blocks = root.getChildNodes();
        for (int i = 0; i < blocks.getLength(); i++) {
            Node block = blocks.item(i);
            NodeList elements = block.getChildNodes();
            if (block.getNodeName().equals("load")){
                Element eElement = (Element) block;
                loadBasket = eElement.getElementsByTagName("enabled").equals(true);
                nameFileBasketRead = eElement.getElementsByTagName("fileName").item(0).getTextContent();
                formatFileBasketRead = eElement.getElementsByTagName("format").item(0).getTextContent();
            }
            if (block.getNodeName().equals("save")){
                Element eElement = (Element) block;
                saveBasket = eElement.getElementsByTagName("enabled").equals(true);
                nameFileBasketWrite = eElement.getElementsByTagName("fileName").item(0).getTextContent();
                formatFileBasketWrite = eElement.getElementsByTagName("format").item(0).getTextContent();
            }
            if (block.getNodeName().equals("log")){
                Element eElement = (Element) block;
                saveLog = eElement.getElementsByTagName("enabled").equals(true);
                nameFileLog = eElement.getElementsByTagName("fileName").item(0).getTextContent();
            }
        }

        String[] products = {"Хлеб", "Яблоки", "Молоко"};
        int[] prices = {100, 200, 300};
        ClientLog cl = new ClientLog();

        System.out.println("Список возможных товаров для покупки");
        for (int i = 0; i < products.length; i++) {
            System.out.printf("%d. %s %d руб/шт\n", i + 1, products[i], prices[i]);
        }

        Basket basket;
        if(loadBasket){
            File f = new File(nameFileBasketRead);
            if(f.exists()) {
                if(formatFileBasketRead.equals("json")){
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
                }else{
                    basket = Basket.loadFromTxtFile(f);
                }
                basket.printCart();
            }else{
                basket = new Basket(products, prices);
            }
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
            if(saveBasket){
                if(formatFileBasketWrite.equals("json")){
                    Gson gson = new GsonBuilder()
                            .setPrettyPrinting()
                            .create();
                    String json = gson.toJson(basket);
                    try(FileWriter writer = new FileWriter(nameFileBasketWrite)) {
                        writer.write(json);
                    }
                    catch(IOException e){

                    }
                }else{
                    basket.saveTxt(new File(nameFileBasketWrite));
                }
            }
            cl.log(Integer.parseInt(st[0])-1, Integer.parseInt(st[1]));
        }
        if(saveLog){
            cl.exportAsCSV(new File(nameFileLog));
            basket.printCart();
        }
    }
}