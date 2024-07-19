package javaapplication1;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;


public class GaussMethod {
    public static final double EPS = 1E-5;
    private Double[][] A; // n+1 для свободного коэффициента
    private Double[][] B;
    int m, n;
    private final Double[] answer;
    private final StringBuilder result;
    private final Boolean[] mark;

    GaussMethod() {
        m = 0; n = 0;
        A = new Double[m][n];
        answer = new Double[n];
        mark = new Boolean[m];
        result = new StringBuilder();

    }
    GaussMethod(int m, int n) {
        A = new Double[m][n+1]; // n+1 для свободного коэффициента
        B = new Double[m][n+1];
        answer = new Double[n];
        mark = new Boolean[m];
        result = new StringBuilder();
        this.m = m;
        this.n = n;
    }

    public void fillMatrix() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter matrix:");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n+1; j++) {
                double rand = ThreadLocalRandom.current().nextInt(1,50);
                A[i][j] = rand;
            }
        }
    }

    public void set(int row, int col, double val) {
        if(row > m) return;
        else if(col > n+1) return;
        else {
            A[row][col] = val;
            B[row][col] = val;
        }
    }

    public double get(int row, int col) {
        if(row > m) return 0;
        else if(col > n+1) return 0;
        else {
            return A[row][col];
        }
    }

    public void saveToFile(File name)  {
        try(FileWriter writer = new FileWriter(name, false))
        {
            StringBuilder inM = new StringBuilder();
            writer.write("\nИсходная матрица: \n");
            for (int i = 0; i < m; i++) {
                inM.append("[ ");
                for (int j = 0; j < n + 1; j++) {
                    inM.append(B[i][j]);
                    inM.append(" ");
                }
                inM.append("]\n");
                System.out.println("ALL OK!");
            }
            writer.write(inM.toString());
            writer.write("\nПреобразованная матрица: \n");
            StringBuilder outM = new StringBuilder();
            for (int i = 0; i < m; i++) {
                outM.append("[ ");
                for (int j = 0; j < n + 1; j++) {
                    outM.append(A[i][j]);
                    outM.append(" ");
                }
                outM.append("]\n");

            }
            writer.write(outM.toString());

            writer.write("\nРешение: \n");
            writer.write(result.toString());
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
    public void rightGaussianStroke() {
        int min_size = Math.min(m, n); // минимальное из чисел

        for (int k = 0; k < min_size; k++) {
            double maxv = 0;
            int position_of_line_with_maxv = k;
            for (int i = k; i < m; i++) {
                if (Math.abs(A[i][k]) > maxv) { // search max elements at the string
                    maxv = Math.abs(A[i][k]);
                    position_of_line_with_maxv = i;
                }
            }
            for (int j = 0; j < n + 1; j++) { // swap strings
                double tmp = A[k][j];
                A[k][j] = A[position_of_line_with_maxv][j];
                A[position_of_line_with_maxv][j] = tmp;
            }

            if (Math.abs(maxv) < EPS) {
                continue;
            }

            for (int i = 0; i < m; i++) {
                if (i == k) continue;

                double multiplier = A[i][k] / A[k][k];
                for (int j = k; j < n + 1; j++) {
                    A[i][j] -= multiplier * A[k][j];
                }
            }
        }

        for (int k = 0; k < min_size; k++) { // каждый коэффициент i-го уравнения делим на первый ненулевой коэффициент этого уравнения
            if (Math.abs(A[k][k]) > EPS) {
                double multiplier = A[k][k];
                if (Math.abs(multiplier) < EPS) continue;
                for (int j = k; j < n + 1; j++) {
                    A[k][j] /= multiplier;
                }
            }
        }

        //Boolean[] mark = new Boolean[m]; // отмечаем одинаковые строки в расширенной матрице ~A СЛАУ
        for (int i = 0; i < m; i++) {
            mark[i] = Boolean.FALSE;
        }

        for (int k1 = 0; k1 < m; k1++) {
            if (mark[k1] == Boolean.TRUE) continue;
            for (int k2 = k1 + 1; k2 < m; k2++) {
                boolean is_equal = true;
                for (int j = 0; j < n + 1; j++) {
                    if (Math.abs(A[k1][j] - A[k2][j]) > EPS) {
                        is_equal = false;
                        break;
                    }
                }
                if (is_equal) {
                    mark[k2] = true;
                }
            }
        }

        for (int i = 0; i < m; i++) {   // проверяем СЛАУ на совместность
            int cnt_of_zeroes = 0;
            for (int j = 0; j < n + 1; j++) {
                if (Math.abs(A[i][j]) < EPS) {
                    cnt_of_zeroes++;
                    A[i][j] = 0.0;
                }
            }
            if (cnt_of_zeroes == n + 1) {
                mark[i] = Boolean.TRUE;
            }
            if (cnt_of_zeroes == n && Math.abs(A[i][n]) > EPS) {
                System.out.println("Система не имеет решений"); // система не имеет решений
                return;
            }
        }
    }

    public void backGaussianStroke() {
        result.delete(0, result.length());                  // очищаем строку результат

        for (int i = 0; i < m; i++) {                       // все ненулевые строки "переносим вперёд":
            for (int j = i+1; j < m; j++) {
                if (mark[i] == Boolean.TRUE && mark[j] == Boolean.FALSE) {
                    Swap_Lines(i, j, n, A, mark);
                }
            }
        }
        int cnt_of_marks = 0;                               // если количество ненулевых строк совпадает с количеством
                                                            // уравнений, то система имеет единственное решение:
        for (int i = 0; i < m; i++) {
            if (mark[i] == Boolean.TRUE) cnt_of_marks++;
        }
        int bottom_border = m - 1 - cnt_of_marks;

        if (bottom_border == n - 1) {
            for (int k = n-1; k >= 0; k--) {
                answer[k] = A[k][n] / A[k][k];
            }

            result.append("[ ");
            for(int k = 0; k < n - 1; k++) {
                result.append(answer[k]);
                result.append(" ");
            }
            result.append(answer[n-1]);
            result.append(" ]");
        }
        else {                                              // иначе отмечаем свободные переменные
            int cnt_of_free_variables = n - (bottom_border + 1);

            Boolean[] marked_variables = new Boolean[n];
            for (int i = 0; i < n; i++) {
                marked_variables[i] = Boolean.FALSE;
            }

            for (int j = 0; j < n; j++) {
                int cnt_of_zeroes = 0;
                for (int i = 0; i < bottom_border; i++) {
                    if (Math.abs(A[i][j]) < EPS) {
                        cnt_of_zeroes++;
                    }
                }
                if (cnt_of_zeroes == bottom_border + 1) {
                    if (cnt_of_free_variables > 0) {
                        marked_variables[j] = Boolean.TRUE;
                        cnt_of_free_variables--;
                    }
                }
            }
            for (int i = n-1; i >= 0; i--) {            // инициализируем свободные переменные:
                if (cnt_of_free_variables == 0) break;
                marked_variables[i] = Boolean.TRUE;
                cnt_of_free_variables--;
            }

            result.append("Инициализация свободных переменных: \n");  // вывод возможного варианта ответа
                                                                    // полагаем, что значение переменной равно 1,
                                                                    // чтобы найти значения зависимых переменных
            //System.out.println("Initialization of free variables:");
            for (int i = 0; i < n; i++) {
                if (marked_variables[i] == Boolean.TRUE) {
                    answer[i] = 1.0;
                    result.append("Положим, что: ");
                    result.append(i);
                    result.append("-я переменная зависимая: ");
                    result.append(answer[i]);
                    result.append("\n");
                    //System.out.println("Let: " + i + "-th variable assigned:" + answer[i]);
                }
            }

            result.append("\nРезультат:\n");
            //System.out.println("Answer:");      // выводим в консоль результирующую матрицу ~A
            for (int i = 0; i < n; i++) {
                if (marked_variables[i] == Boolean.TRUE) {
                    result.append(i);
                    result.append("-я переменная зависимая\n");
                    //System.out.println(i + "-ья переменная зависимая");
                }
            }

            for (int i = bottom_border; i >= 0; i--) {
                double cur_sum = 0;

                int cur_variable = 0;
                for (int j = 0; j < n; j++) {
                    if (marked_variables[j] == Boolean.FALSE && Math.abs(A[i][j]) > EPS) {
                        cur_variable = j;
                        break;
                    }
                }

                result.append("X[");
                result.append(cur_variable);
                result.append("] = ");

                //System.out.print("X[" + cur_variable + "] = ");
                for (int j = 0; j < n; j++) {
                    if (marked_variables[j] == Boolean.TRUE) {
                        cur_sum += answer[j] * A[i][j];
                        result.append("(");
                        result.append(-A[i][j]);
                        result.append(" / ");
                        result.append(A[i][cur_variable]);
                        result.append(") * X[");
                        result.append(j);
                        result.append("] + ");
                        //System.out.print("(" + -A[i][j] + "/" + A[i][cur_variable] + ")" + "*X[" + j + "] + ");
                    }
                }
                result.append(A[i][n]);
                result.append("\n");
                //System.out.println(A[i][n]);
                //System.out.println();

                cur_sum *= -1;
                cur_sum += A[i][n];
                
                for (int j = 0; j < n; j++) {
                    if (marked_variables[j] == Boolean.FALSE && Math.abs(A[i][j]) > EPS) {
                        answer[j] = cur_sum / A[i][j];
                        marked_variables[j] = Boolean.TRUE;
                        break;
                    }
                }

            }

/*
            for (int i = 0; i < n; i++) {
                if (Math.abs(answer[i]) < EPS) answer[i] = 0.0;
            }
*/

            result.append("\nОдно из возможных решений, например: ");
            //System.out.println("One of the solutions:");
            for (int k = 0; k < n - 1; k++) {
                result.append(answer[k]);
                result.append(" ");
                //System.out.print(answer[k] + " ");
            }
            result.append(answer[n-1]);
            //System.out.println(answer[n - 1]);
        }
    }
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < m; i++) {
            res.append("[ ");
            for (int j = 0; j < n + 1; j++) {
                res.append(A[i][j]);
                res.append(" ");
            }
            res.append("]\n");
        }
        return res.toString();
    }

    public String answer() {
        return result.toString();
    }
    public static void Swap_Lines(int k1, int k2, int n, Double[][] A, Boolean[] mark) { // перестановка строк
        for (int j = 0; j < n; j++) {
            Double tmp;
            tmp = A[k1][j];
            A[k1][j] = A[k2][j];
            A[k2][j] = tmp;
        }
        Boolean tmp; // для проверки строк на одинаковость
        tmp = mark[k1];
        mark[k1] = mark[k2];
        mark[k2] = tmp;
    }

}
