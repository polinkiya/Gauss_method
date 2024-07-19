package javaapplication1;
import javax.swing.*;


public class JavaApplication1 {

    public static void main(String[] args)  {


        MyForm pr = new MyForm();

        pr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* для запуска приложения из консоли

        long start = System.nanoTime();

        Scanner sc = new Scanner(System.in);
        int n, m;
        System.out.print("Введите количество уравнений: ");
        m = sc.nextInt(); // количество уравнений

        System.out.print("Введите количество переменных: ");
        n = sc.nextInt(); // количество переменных

        GaussMethod sample = new GaussMethod(m, n);

        sample.fillMatrix();
        sample.rightGaussianStroke();

        System.out.println(sample);
        sample.backGaussianStroke();

        System.out.println("Решение: ");
        System.out.println(sample.answer());

        long finish = System.nanoTime();
        long elapsed = finish - start;
        System.out.println("Прошло времени, нс: " + elapsed/1000000 +"\n");
        long usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.out.println(usedBytes/1048576 + "Mb");
        */
    }

}
