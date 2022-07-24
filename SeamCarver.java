import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;


public class SeamCarver{
        private Picture pic;
        private double[][] energy1;
        private double[][] color;
        private double[][] energyto;
        private int[][] pathto;
        private int finalw;
        private int[] current;
        private boolean transposed;
        private boolean t;


        // create a seam carver object based on the given picture
        public SeamCarver(Picture picture){
                pic = picture;
                int w = picture.width();
                int h = picture.height();
                energy1 = new double[h][w];
                color = new double[h][w];
                transposed = false;

                for(int j = 0; j<h; j++) {
                        for (int x = 0; x < w; x++) {
                                energy1[j][x] = energy(x,j);

                        }}
                for(int x = 0; x<w; x++){
                        for(int l= 0; l<h; l++){
                                color[l][x] = pic.getRGB(x, l);
                        }
                }

        }

        // current picture
        public Picture picture(){

                t=true;
                if(energy1.length != color.length){
                        t =false;}

                if(transposed && t){
                        energy1 = transpose(energy1);
                }
                if(!transposed&& !t){
                        energy1 = transpose(energy1);
                }
                Picture newa = new Picture(color[0].length, color.length);
                for(int l = 0; l<color.length; l++){
                        for(int x= 0; x<color[0].length; x++){
                                newa.setRGB(x,l, (int) color[l][x]);
                        }}
                pic = newa;





                return pic;}


        // width of current picture
        public int width(){
                return pic.width();
        }

        // height of current picture
        public int height(){
                return pic.height();
        }


        // energy of pixel at column x and row y
        public double energy(int x, int y){
                double energyreturn;
                if(x==0 || (y==0)||x==pic.width()-1|| y== pic.height()-1){
                        energyreturn = 1000;
                }
                else{energyreturn = Math.sqrt((xenergy(x,y)+yenergy(x,y)));}
                return energyreturn;
        }


        private double xenergy(int x, int y){
                int color1 =   pic.getRGB((x+1),y);
                double blue1 = (color1) & 0xFF;
                double green1 = (color1 >> 8) & 0xFF;
                double red1 = (color1 >>16) & 0xFF;

                int color2 = pic.getRGB(x-1, y);
                double blue2 = color2 & 0xFF;
                double green2 = (color2 >> 8) & 0xFF;
                double red2 = (color2 >> 16) & 0xFF;

                double blue = Math.pow(blue1 - blue2, 2);
                double green = Math.pow(green1 - green2, 2);
                double red = Math.pow(red1 - red2, 2);
                double total = blue+green+red;

                return total;
        }

        private double yenergy(int x, int y){
                int color1 =   pic.getRGB(x,y+1);
                double blue1 = color1 & 0xff;
                double green1 = (color1 & 0xff00) >> 8;
                double red1 = (color1 & 0xff0000) >> 16;

                int color2 = pic.getRGB(x, y-1);
                double blue2 = color2 & 0xff;
                double green2 = (color2 & 0xff00) >> 8;
                double red2 = (color2 & 0xff0000) >> 16;

                double blue = Math.pow((blue1-blue2), 2);
                double green = Math.pow((green1 - green2), 2);
                double red = Math.pow((red1 - red2), 2);
                double total = blue+green+red;
                return total;

        }

        private double[][] transpose(double[][] arr){
                double [][] pixels = new double[arr[0].length][arr.length];
                for (int r=0;r<arr.length;r++ ) {
                        for(int j =0; j<arr[0].length; j++){
                                pixels[j][r] = arr[r][j];
                        } }                return pixels;
        }

        // sequence of indices for horizontal seam
        public int[] findHorizontalSeam(){

                if(!transposed){
                        energy1 = transpose(energy1);
                        transposed = true;}



                int h = energy1.length;
                int w = energy1[0].length;
                if(h==1){
                        current =  new int[1] ;
                        current[0] = 1;
                }
                if(w==1){
                        current =  new int[h] ;
                        for(int x =0; x<h; x++){
                                current[x] = 1;
                        }
                }
                else{

                                current = verticalsort(energy1);
                                int[] result = new int[current.length];
                                for(int i =0; i<current.length; i++){

                                }
                                current = result;


                }

         return current;}




        // sequence of indices for vertical seam
        public int[] findVerticalSeam(){
                if(transposed){
                        energy1 = transpose(energy1);
                        transposed = false;
                }
                if(height()==1){
                        current =  new int[1] ;
                        current[0] = 1;
                }
                if(width()==1){
                        current =  new int[height()] ;
                        for(int x =0; x<height(); x++){
                                current[x] = 1;
                        }
                }
                else{

                        current = verticalsort(energy1);


                }
                return current;
        }

        private int[] verticalsort(double[][] arr) {
                pathto = new int[arr.length][arr[0].length];
                energyto = new double[arr.length][arr[0].length];
                int width = arr[0].length;
                int length = arr.length;

                for(int r= 0; r< length; r++ ){
                        for (int w=0;w<width; w++ ) {
                                if (r==0) {
                                        energyto[r][w] = 0;
                                }
                                else{
                                        energyto[r][w] = Double.POSITIVE_INFINITY;
                                } } }


                for(int w=0;w<width;w++){
                        for (int r=0; r<length-1; r++) {
                                if(w-1>= 0){
                                        relax(r,w,r+1,w-1);
                                }
                                relax(r,w,r+1,w);
                                if(w+1< width){
                                        relax(r,w,r+1,w+1);
                                } } }
                double finalenergy = Double.POSITIVE_INFINITY;
                for (int w =0;w<width;w++ ) {
                        if (energyto[length-1][w]<=finalenergy) {
                                finalenergy = energyto[length-1][w];
                                finalw = w;

                        }}

                int[] path = new int[length];
                path[length - 1] = finalw;


                for (int row = length - 2; row >= 0; row--) {
                        MinPQ<Double> queue = new MinPQ<>();


                        if(finalw>0){
                                double en1 = energyto[row][finalw-1];
                                queue.insert(en1);
                        }
                        queue.insert(energyto[row][finalw]);

                        if(finalw<width-1){
                                queue.insert(energyto[row][finalw+1]);
                        }

                        double result = queue.delMin();
                        if (finalw-1>=0 &&result == energyto[row][finalw-1]) {
                                path[row] = finalw-1;
                                finalw = finalw-1;

                        }
                        else if(finalw+1 <= length-1&& result == energyto[row][finalw+1]){
                                path[row] = finalw+1;
                                finalw = finalw+1;
                        }
                        else{
                                path[row] = finalw;
                        }


                        }
                return path;
        }

        private void relax(int x, int y, int x2, int y2){
                if (energyto[x2][y2] > energyto[x][y] + energy1[x2][y2]) {
                        energyto[x2][y2] = energyto[x][y] + energy1[x2][y2];
                        pathto[x2][y2] = y;

                }
        }

        private void editenergy(int[] seam){

                for(int i = 0; i<seam.length;  i++){



                        int ycoord = i;
                        int xcoord = seam[i];
                        if(ycoord == 0|| ycoord==energy1.length-1||xcoord==0){
                                energy1[ycoord][xcoord] = 1000;

                        }

                        else if(xcoord == energy1[0].length-1){


                                energy1[ycoord][xcoord] = 1000;
                        }
                        else{

                                energy1[ycoord][xcoord] = energy(xcoord,ycoord);
                                if(xcoord+1 == energy1[0].length-1){

                                        energy1[ycoord][xcoord+1] = 1000;
                                }
                                if (xcoord-1 == 0) {

                                        energy1[ycoord][xcoord] = 1000;

                                }

                                if(xcoord+1<energy1[0].length-1){
                                        energy1[ycoord][xcoord+1] = energy(xcoord+1, ycoord);
                                }
                                if(xcoord-1>0){

                                        energy1[ycoord][xcoord-1] = energy(xcoord-1,ycoord);

                                }

                        }}}






        // remove horizontal seam from current picture
        public void removeHorizontalSeam(int[] seam){
                if (!transposed) {
                        energy1 = transpose(energy1);
                        transposed = true;
                }
                if(energy1.length!=color.length){
                        color =transpose(color);
                }
                double[][] energy2 = new double[seam.length][energy1[0].length-1];
                double[][] color2 = new double[seam.length][color[0].length-1];

                for(int s =0; s<seam.length; s++){

                        System.arraycopy(energy1[s], 0 , energy2[s], 0, seam[s] );
                        System.arraycopy(energy1[s], seam[s]+1 , energy2[s], seam[s], energy2[0].length-seam[s] );

                        System.arraycopy(color[s], 0 , color2[s], 0, seam[s] );
                        System.arraycopy(color[s], seam[s]+1 , color2[s], seam[s], color2[0].length-seam[s] );
                }
                energy1=energy2;
                color=color2;
                editenergy(seam);
        }

        // remove vertical seam from current picture
        public void removeVerticalSeam(int[] seam){
                if (transposed) {
                        energy1 = transpose(energy1);
                        transposed=false;

                }
                if(energy1.length!= color.length){
                        color =transpose(color);
                }
                double[][] energy2 = new double[height()][width()-1];
                double[][] color2 = new double[height()][width()-1];

                for(int s =0; s<seam.length; s++){

                        System.arraycopy(energy1[s], 0 , energy2[s], 0, seam[s] );
                        System.arraycopy(energy1[s], seam[s]+1 , energy2[s], seam[s], energy2[0].length-seam[s] );

                        System.arraycopy(color[s], 0 , color2[s], 0, seam[s] );
                        System.arraycopy(color[s], seam[s]+1 , color2[s], seam[s], energy2[0].length-seam[s] );
                }
                energy1=energy2;
                color=color2;
                editenergy(seam);
        }


        //  unit testing (optional)

        public static void main(String[] args){


                Picture picture = new Picture(args[0]);
                StdOut.printf("%s (%d-by-%d image)\n", args[0], picture.width(), picture.height());
                StdOut.println();
                StdOut.println("The table gives the dual-gradient energies of each pixel.");
                StdOut.println("The asterisks denote a minimum energy vertical or horizontal seam.");
                StdOut.println();

                SeamCarver carver = new SeamCarver(picture);

                StdOut.printf("Vertical seam: { ");
                int[] verticalSeam = carver.findVerticalSeam();
                for (int y : verticalSeam)
                        StdOut.print(y + " ");
                StdOut.println("}");


                StdOut.printf("Horizontal seam: { ");
                int[] horizontalSeam = carver.findHorizontalSeam();
                for (int y : horizontalSeam)
                        StdOut.print(y + " ");
                StdOut.println("}");


        }

}








