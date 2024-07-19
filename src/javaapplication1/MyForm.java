package javaapplication1;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

public class MyForm extends JFrame{
    GaussMethod sample;
    static JButton button, button2, button3, button4, button5, button6;
    static JLabel lb1, lb2, imageLabel, lb3;
    static JTextField mSize, nSize;
    static JTable table;
    static DefaultTableModel tableModel;
    static JTextPane solution;
    static JProgressBar bar = new JProgressBar();


    Checked check = new Checked();


    public MyForm() {
        super("Решение СЛАУ методом Гаусса");

        button = new JButton("Заполнить матрицу");
        button2 = new JButton("Решить систему");
        button3 = new JButton("Сохранить в файл");
        mSize= new JTextField("0");
        lb1 = new JLabel("Введите количество уравнений:");
        nSize = new JTextField("0");
        lb2 = new JLabel("Введите количество переменных:");
        lb3 = new JLabel("Решение СЛАУ:");
        button4 = new JButton("Показать график");
        button5 = new JButton("Заполнить случайно");
        button6 = new JButton("Открыть из файла");


        solution = new JTextPane();
        imageLabel = new JLabel(new ImageIcon("/Users/polinamorozova/IdeaProjects/Gauss_method/calc.png"));
        imageLabel.setVisible(true);
        imageLabel.setBounds(560,120,150,150);

        setSize(800,760);
        button.setBounds( 400, 20,160,30);
        button2.setBounds( 400, 50,160,30);
        button4.setBounds(400,80, 160, 30);

        button5.setBounds(560,20, 160, 30);
        button6.setBounds(560,50, 160, 30);
        button3.setBounds( 560, 80,160,30);

        mSize.setBounds(10,20,200,30);
        nSize.setBounds(10,70,200,30);

        lb1.setBounds(mSize.getX(), mSize.getY() - 20, 300, 20 );
        lb2.setBounds(nSize.getX(), nSize.getY() - 20, 300, 20 );
        lb3.setBounds(10, 310, 300, 20 );

        solution.setBounds(10, 330, 780, 390);

        bar.setValue(0);
        bar.setBounds(10, 100, 780, 50);
        bar.setStringPainted(true);
        add(bar);


        add(button);
        add(button2);
        add(button3);
        add(button4);
        add(button5);
        add(button6);
        add(mSize);
        add(lb1);
        add(nSize);
        add(lb2);
        add(lb3);
        add(solution);
        add(imageLabel);


        setLayout(null);
        setVisible(true);
        setResizable(false);

        button.addActionListener(check);
        button2.addActionListener(check);
        button3.addActionListener(check);
        button4.addActionListener(check);
        button5.addActionListener(check);
        button6.addActionListener(check);

    }


    private static SwingWorker<Void, Void> startWorker(JProgressBar progress, JButton button) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                for (int i = 0; i < 100 && !isCancelled(); i++) {
                    doWork();
                    setProgress(i);
                }
                return null;
            }

            @Override
            protected void done() {
                button.setEnabled(true);
                button.setText("Заполнить матрицу");
               bar.setValue(100);
            }
        };
        worker.addPropertyChangeListener(e -> {
            if ("progress".equals(e.getPropertyName())) {
                progress.setValue((Integer)e.getNewValue());
            }
        });
        return worker;
    }

    private static void doWork() {
        try {
            Thread.sleep((long) Integer.parseInt(mSize.getText()) * Integer.parseInt(nSize.getText()) /7000);

        } catch (InterruptedException ignored) {}
    }

    public class MyThread extends Thread {
        public void run() {
            System.out.println("поток1 запущен");
            startWorker(bar, button5).execute();
            button5.setEnabled(false);
        }
    }

    public class MyThread2 extends Thread {
        public void run() {
            System.out.println("поток2 запущен");
            for(int i = 0; i < Integer.parseInt(mSize.getText()); i++) {
                for(int j = 0; j < Integer.parseInt(nSize.getText())+1; j++) {
                    try {
                        double rand = (int) (Math.random() * 50);
                        table.getModel().setValueAt(rand, i, j);
                        // val = Double.valueOf(str);
                        sample.set(i, j, rand);
                        //bar.setValue((i+1)*(j+1));

                        revalidate();
                    }
                    catch (NumberFormatException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null, "Таблица заполнена некорректно");
                        return;
                    }
                    //System.out.print(table.getModel().getValueAt(i,j) + " ");
                }
                //System.out.println();
            }
            System.out.println("поток2 завершен");
        }
    }
    private class Checked implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event) {
            if(event.getSource() == button) {
                try {
                    int n = Integer.parseInt(nSize.getText());
                    int m = Integer.parseInt(mSize.getText());
                    if(n > 5000 || m > 5000) {
                        JOptionPane.showMessageDialog(null, "Размер матрицы должен быть меньше 5000");
                    }
                    sample = new GaussMethod(m,n);
                    tableModel = new DefaultTableModel(m,n + 1);
                    //System.out.println(m+"\n");
                    table = new JTable(tableModel);
                    table.setBorder(new LineBorder(new Color(41, 101, 222)));
                    table.setSelectionBackground(Color.LIGHT_GRAY);
                    table.setVisible(true);
                    table.setBounds(10, 140, m * 125, n * 20);
                    add(table);
                    revalidate();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Пожалуйста, введите число");
                }


            }
            if(event.getSource() == button2) {
                if(Integer.parseInt(mSize.getText()) > 30 || Integer.parseInt(nSize.getText()) > 30) {
                    JOptionPane.showMessageDialog(null, "Для удобного просмотра после решения системы," +
                            "\n сохраните результат в файл\n Или используйте консольную версию :)");
                }
                Double val = null;
                String str;
                for(int i = 0; i < Integer.parseInt(mSize.getText()); i++) {
                    for(int j = 0; j < Integer.parseInt(nSize.getText())+1; j++) {
                        try {
                            str = table.getModel().getValueAt(i, j).toString();
                            //System.out.print("STR:" + str + "  ");
                            val = Double.valueOf(str);
                            //System.out.println("DOUBLE:" + val + "  ");
                            sample.set(i, j, val);
                        }
                        catch (Exception ex) {
                            System.out.println(ex.getMessage());
                            JOptionPane.showMessageDialog(null, "Таблица заполнена некорректно");
                            return;
                        }
                        //System.out.print(table.getModel().getValueAt(i,j) + " ");
                    }
                    //System.out.println();
                }
                System.out.println(sample);
                sample.rightGaussianStroke();
                sample.backGaussianStroke();

                StyledDocument doc = solution.getStyledDocument();
                SimpleAttributeSet keyWord = new SimpleAttributeSet();

                try
                {
                    doc.remove(0, doc.getLength());
                    doc.insertString(0, sample.answer(), null );
                }
                catch(Exception e) { System.out.println(e); }
//                solution.setText(sample.answer());
                System.out.println(sample.answer());
            }

            if(event.getSource() == button3) {

                JFileChooser fc = new JFileChooser();
                if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try (FileWriter fw = new FileWriter(fc.getSelectedFile())) {
                        fw.write("");
                        sample.saveToFile(fc.getSelectedFile());
                    }
                    catch (IOException e ) {
                        System.out.println("Сохранение не выполнено!");
                    }
                }
                // sample.saveToFile();
            }
            if(event.getSource() == button4) {
                Icon gr = new ImageIcon("/Users/polinamorozova/IdeaProjects/Gauss_method/graph.png");
                JOptionPane.showMessageDialog(null, "","График зависимости времени решения системы от количества уравнений", JOptionPane.WARNING_MESSAGE, gr);
            }

            if(event.getSource() == button5) {
                MyThread myThread = new MyThread();
                MyThread2 myThread2 = new MyThread2();
                myThread.start();
                myThread2.start();

//
//                for(int i = 0; i < Integer.parseInt(mSize.getText()); i++) {
//                    for(int j = 0; j < Integer.parseInt(nSize.getText())+1; j++) {
//                        try {
//                            double rand = (int) (Math.random() * 50);
//                            table.getModel().setValueAt(rand, i, j);
//                           // val = Double.valueOf(str);
//                            sample.set(i, j, rand);
//                            //bar.setValue((i+1)*(j+1));
//
//                            revalidate();
//                        }
//                        catch (NumberFormatException ex) {
//                            System.out.println(ex.getMessage());
//                            JOptionPane.showMessageDialog(null, "Таблица заполнена некорректно");
//                            return;
//                        }
//                        //System.out.print(table.getModel().getValueAt(i,j) + " ");
//                    }
//                    //System.out.println();
//                }
            }

            if(event.getSource() == button6) {
                Scanner sc = null;
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                int returnValue = jfc.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    //System.out.println(selectedFile.getAbsolutePath());
                    try {
                        sc = new Scanner(new BufferedReader(new FileReader(selectedFile.getAbsolutePath())));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }

                int rows;
                int columns;
                assert sc != null;
                String[] lineIn = sc.nextLine().trim().split(" ");
                rows = Integer.parseInt(lineIn[0]);
                columns = Integer.parseInt(lineIn[1]) + 1;
                sample = new GaussMethod(rows,columns-1);
                //int [][] myArray = new int[rows][columns];
                while(sc.hasNextLine()) {
                    for (int i = 0; i < rows; i++) {
                        String[] line = sc.nextLine().trim().split(" ");
                        for (int j = 0; j < line.length; j++) {
                            sample.set(i, j,Integer.parseInt(line[j]));
                            //myArray[i][j] = Integer.parseInt(line[j]);
                        }
                    }
                }

                tableModel = new DefaultTableModel(rows, columns);
                //System.out.println(m+"\n");
                table = new JTable(tableModel);
                table.setBorder(new LineBorder(new Color(41, 101, 222)));
                table.setSelectionBackground(Color.LIGHT_GRAY);
                table.setVisible(true);
                table.setBounds(10, 140, rows * 125, columns * 20);
                add(table);
                revalidate();
                //System.out.println(Arrays.deepToString(myArray));

                for(int i = 0; i < rows; i++) {
                    for(int j = 0; j < columns; j++) {
                        try {
                            table.getModel().setValueAt(sample.get(i,j), i, j);
                        }
                        catch (NumberFormatException ex) {
                            System.out.println(ex.getMessage());
                            JOptionPane.showMessageDialog(null, "Таблица заполнена некорректно");
                            return;
                        }
                        //System.out.print(table.getModel().getValueAt(i,j) + " ");
                    }
                    //System.out.println();
                }
            }
        }
    }

}
