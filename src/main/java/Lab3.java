import org.jblas.DoubleMatrix;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static java.lang.Math.round;
import static java.lang.Math.sqrt;


//////////////////////////////////////////////////////////////////////////////////////
// Лабораторная работа 3 по дисциплине МРЗвИС
// Выполнена студентом группы 821701
// БГУИР Поживилко Петром Сергеевичем
// Файл main с функциями для восстановления образа
// 17.12.2020 ver. 0.1
//
// библиотека для работы с матрицами http://jblas.org/
// Авторство Поживилко Петр Сергеевич





//
public class Lab3 {


    public static void main(String[] args) throws IOException {
        int X=18;


        DoubleMatrix input1 = getData("D://000/mrzvis3/test/test1.txt");
        DoubleMatrix out1 = getData("D://000/mrzvis3/test/test1_association.txt");
        DoubleMatrix broken1 = getData("D://000/mrzvis3/test/test1_broken.txt");
        DoubleMatrix broken2 = getData("D://000/mrzvis3/test/test2_broken.txt");


        DoubleMatrix input2 = getData("D://000/mrzvis3/test/test2.txt");
        DoubleMatrix out2 = getData("D://000/mrzvis3/test/test2_association.txt");




        DoubleMatrix weights1=input1.transpose().mmul(out1).add(input2.transpose().mmul(out2));
        DoubleMatrix weights2 = weights1.transpose();
        



        learn(broken1,weights1,weights2,input1);

        learn(broken2,weights2,weights1,out2);











    }

    public static void learn(DoubleMatrix input, DoubleMatrix weights1, DoubleMatrix weights2, DoubleMatrix etalon) {
        double energy=0;
        double prevEnergy = energy;
        DoubleMatrix tmp = input;
        printMatrix(tmp,18);
        DoubleMatrix buff=new DoubleMatrix(1,input.columns);
        do {
            DoubleMatrix S1 = tmp.mmul(weights1);
            DoubleMatrix y1 = activate(S1);
            for(int i=0;i<S1.length;i++){
                energy += S1.get(i)*y1.get(i)*-0.5;
            }
            System.out.println(energy);
            if(energy==prevEnergy){
                printMatrix(activate(tmp.mmul(weights1)),18);
                break;
            }else{
                prevEnergy=energy;
                energy=0;
            }
            DoubleMatrix S2 = y1.mmul(weights2);
            tmp = activate(S2);






        } while (true);
        printMatrix(tmp,18);

    }
    public static DoubleMatrix activate(DoubleMatrix input){
        DoubleMatrix out = new DoubleMatrix(1, input.length);
        for(int i=0;i<out.length;i++){
            if(input.get(i)<0)
                out.put(0,i,-1);
            else
                out.put(0,i,1);
        }
        return out;
    }

    public static void printMatrix(DoubleMatrix matrix,int x){
        System.out.println();
        ArrayList<DoubleMatrix> lines=new ArrayList<DoubleMatrix>();
        ArrayList<Double> tmp = new ArrayList<Double>();
        int it=0;
        for (int i = 0; i < matrix.length; i ++) {
            tmp.add(matrix.get(0, i));
            it++;
            if(it==x){
                lines.add(new DoubleMatrix(tmp).transpose());
                tmp.clear();
                it=0;
            }


        }
        for (DoubleMatrix line : lines) {
            for (int j = 0; j < line.length; j++) {
                if (line.get(0, j) == -1)
                    System.out.print("-");
                else
                    System.out.print("#");
            }
            System.out.println();
        }
    }
    public static DoubleMatrix getData(String dataPath) throws FileNotFoundException {
        ArrayList<Double> list = new ArrayList<Double>();
        Scanner scanner=new Scanner(new File(dataPath));
        while (scanner.hasNext()){
            String line=scanner.nextLine();
            for(int i=0;i<line.length();i++){
                if(line.charAt(i)=='-')
                    list.add(-1.);
                else
                    list.add(1.);
            }

        }
        DoubleMatrix result = new DoubleMatrix(list);
        result.rows=1;
        result.columns = list.size();
        return result;
    }

}