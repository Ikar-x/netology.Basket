import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Basket{
    private String[] products;
    private int[] prices;
    private int[] count;
    private int allCount;

    public Basket(){

    }

    public Basket(String[] products, int[] prices){
        this.products = products;
        this.prices = prices;
        this.allCount = products.length;
        this.count = new int[allCount];
    }

    public Basket(String[] products, int[] prices, int[] count){
        this.products = products;
        this.prices = prices;
        this.allCount = products.length;
        this.count = count;
    }

    public boolean addToCart(int productNum, int amount){
        if(productNum > count.length - 1) return false;
        count[productNum] = amount;
        return true;
    }

    public void printCart(){
        System.out.println("\nВаша корзина:");
        for (int i = 0; i < allCount; i++) {
            if(count[i] != 0)
                System.out.printf("%s %d шт %d руб/шт %d руб в сумме\n",
                        products[i], count[i], prices[i], (count[i] * prices[i]));
        }
        int sum = 0;
        for (int i = 0; i < products.length; i++) {
            sum += count[i] * prices[i];
        }
        System.out.printf("Итого %d руб\n\n", sum);
    }

    public void saveTxt(File textFile) throws IOException {
        String[] produstInLines = new String[allCount];
        for(int i = 0; i < allCount; i++){
            produstInLines[i] = products[i] + " " + prices[i] + " " + count[i];
        }
        try (PrintWriter out = new PrintWriter(textFile)){
            for(String s : produstInLines){
                out.println(s);
            }
        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    static Basket loadFromTxtFile(File textFile){
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(textFile))) {
            String s;
            while ((s = br.readLine()) != null) {
                list.add(s);
            }
            int allCount = list.size();
            String[] products = new String[allCount];
            int[] prices = new int[allCount];
            int[] count = new int[allCount];
            for(int i = 0; i < allCount; i++){
                String[] elements = list.get(i).split(" ");
                products[i] = elements[0];
                prices[i] = Integer.parseInt(elements[1]);
                count[i] = Integer.parseInt(elements[2]);
            }
            return new Basket(products, prices, count);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return new Basket();
    }
}
