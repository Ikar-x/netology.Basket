import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        String[] products = {"Хлеб", "Яблоки", "Молоко"};
        int[] prices = {100, 200, 300};

        System.out.println("Список возможных товаров для покупки");
        for (int i = 0; i < products.length; i++) {
            System.out.printf("%d. %s %d руб/шт\n", i + 1, products[i], prices[i]);
        }

        File f = new File("basket.bin");
        Basket basket;
        if(f.exists()) {
            basket = Basket.loadFromBinFile(f);
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
            basket.saveBin(f);
        }

        basket.printCart();

    }
}