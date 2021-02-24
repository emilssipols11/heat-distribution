package app;

import java.io.File;
import java.io.FileWriter;
import org.jsfml.system.Clock;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.LUDecomposition;

public class App {
    public static void main(String[] args) throws Exception {
        int num = 50;
        double top = 300;
        double right = 400;
        double bottom = 1000;
        double left = 550;
        int time = 1000;

        double[][][] field = new double[num][num][time];
        //initial boundary conditions
        field[0][0][0] = (top+left)/2;
        field[num-1][0][0] = (top+right)/2;
        field[0][num-1][0] = (bottom+left)/2;
        field[num-1][num-1][0] = (bottom+right)/2;

        for(int x = 1; x<num-1; x++){
            field[x][0][0] = top;
            field[x][num-1][0] = bottom;
        }

        for(int y = 1; y<num-1; y++){
            field[0][y][0] = left;
            field[num-1][y][0] = right;
        }
        
        DoubleMatrix2D coef = new DenseDoubleMatrix2D((int)Math.pow(num-2, 2),(int)Math.pow(num-2, 2)); // equation matrix with coefficents
        int count = 0;
        for(int x = 0; x<num-2; x++){
            for(int y = 0; y<num-2; y++){

                coef.set(count, count, -4);  //current cell

                if(x-1 != -1){ // neighbours
                    coef.set(count, (x-1)*(num-2) + y, 1);
                }
                if(x+1 != num-2){
                    coef.set(count, (x+1)*(num-2) + y, 1);
                }
                if(y-1 != -1){
                    coef.set(count, (x)*(num-2) + y-1, 1);
                }
                if(y+1 != num-2){
                    coef.set(count, (x)*(num-2) + y+1, 1);
                }
                count++;
            }
        }

        DoubleMatrix2D known = new DenseDoubleMatrix2D((int)Math.pow(num-2, 2), 1); // result vector matrix
        count = 0;
        
        for(int x = 1; x<num-1; x++){
            for(int y = 1; y<num-1; y++){
                double value = 0;
                value -= field[x-1][y][0];
                value -= field[x+1][y][0];
                value -= field[x][y-1][0];
                value -= field[x][y+1][0];
                known.set(count, 0, value);
                count++;
            }
        }
        new DenseDoubleMatrix2D((int) Math.pow(num - 2, 2), 1);
        DoubleMatrix2D unknown;
        LUDecomposition equation = new LUDecomposition(coef);
        unknown = equation.solve(known);

        count = 0;
        for(int x = 1; x<num-1; x++){
            for(int y = 1; y<num-1; y++){
                field[x][y][0] = unknown.get(count, 0);
                count++;
            }
        }

        double beta = 0.24;
        DoubleMatrix2D timeCoef = new DenseDoubleMatrix2D((int)Math.pow(num, 2),(int)Math.pow(num, 2));

        count = 0;
        for(int x = 0; x<num; x++){
            for(int y = 0; y<num; y++){
                timeCoef.set(count, count, 1-(4*beta));  //current cell

                if(x-1 != -1){ // neighbours
                    timeCoef.set(count, (x-1)*num + y, beta);
                }
                if(x+1 != num){
                    timeCoef.set(count, (x+1)*num + y, beta);
                }
                if(y-1 != -1){
                    timeCoef.set(count, (x)*num + y-1, beta);
                }
                if(y+1 != num){
                    timeCoef.set(count, (x)*num + y+1, beta);
                }
                count++;
            }
        }

        for(count = 0; count<time-1; count++){

            DoubleMatrix2D nextKnown = new DenseDoubleMatrix2D((int)Math.pow(num, 2),1);
            
            for(int x = 0; x<num; x++){
                for(int y = 0; y<num; y++){
                    nextKnown.set(num*x+y, 0, field[x][y][count]);
                }
            }

            DoubleMatrix2D rez = new DenseDoubleMatrix2D((int)Math.pow(num, 2),1);
            timeCoef.zMult(nextKnown, rez);

            int timeCount = 0;
            for(int x = 0; x<num; x++){
                for(int y = 0; y<num; y++){
                    field[x][y][count+1] = rez.get(timeCount, 0);
                    timeCount++;
                }
            }
    
        }

        Clock BBC = new Clock();
        RenderWindow main_wind = new RenderWindow(new VideoMode(800,700), "window");
        RectangleShape[] poop = new RectangleShape[num*num];
        int t = 0;
        while(main_wind.isOpen()){
            for ( Event myevent : main_wind.pollEvents()) {
                if(myevent.type == Type.CLOSED){
                    main_wind.close();
                }
            }

            if(BBC.getElapsedTime().asSeconds() > 0.1f){
                if(t<time){
                    t++;
                }
                BBC.restart();
            }
            count = 0;
            
            for(int x = 0; x<num; x++){
                for(int y = 0; y<num; y++){
                    poop[count] = new RectangleShape();
                    poop[count].setSize(new Vector2f(10,10));
                    poop[count].setPosition(new Vector2f(10+10*x,10+10*y));
                    if(field[x][y][t] >= 0 && field[x][y][t] < 29){
                        poop[count].setFillColor(Color.WHITE);
                    }
                    if(field[x][y][t] >= 29 && field[x][y][t] < 58){
                        poop[count].setFillColor(Color.CYAN);
                    }
                    if(field[x][y][t] >= 58 && field[x][y][t] < 87){
                        poop[count].setFillColor(Color.BLUE);
                    }
                    if(field[x][y][t] >= 87 && field[x][y][t] < 116){
                        poop[count].setFillColor(Color.GREEN);
                    }
                    if(field[x][y][t] >= 116 && field[x][y][t] < 145){
                        poop[count].setFillColor(Color.YELLOW);
                    }
                    if(field[x][y][t] >= 145 && field[x][y][t] < 174){
                        poop[count].setFillColor(Color.MAGENTA);
                    }
                    if(field[x][y][t] >= 174){
                        poop[count].setFillColor(Color.RED);
                    }
                    count++;
                }
            }
            count = 0;
            for(int x = 0; x<num; x++){
                for(int y = 0; y<num; y++){
                    main_wind.draw(poop[count]);
                    count++;
                }
            }

            main_wind.display();
            main_wind.clear();

        }

//        File file = new File("C:\\Users\\Home\\Desktop\\data.txt");
//        FileWriter myWriter = new FileWriter(file);
//        for(int ti = 0; ti<time; ti++){
//            for(int x = 0; x<num; x++){
//                for(int y = 0; y<num; y++){
//                    myWriter.write((float)x*0.1+"\t"+(float)y*0.1+"\t"+(float)ti*0.02+"\t"+field[x][y][t]+"\n");
//                }
//            }
//        }
//        myWriter.close();

    }
}