import edu.princeton.cs.algs4.Picture;


public class SeamCarver {

    private Picture picture;
    private double[][] energyMap;
    private int height;
    private int width;

    public SeamCarver(Picture picture) {
        // create a seam carver object based on the given picture
        if (picture==null){
            throw new IllegalArgumentException();
        }

        this.picture = new Picture(picture);
        this.width = picture.width();
        this.height = picture.height();
        this.energyMap = new double[height][width];
        for (int i=0;i<width;i++){
            energyMap[0][i] = 1000;
            energyMap[height-1][i] = 1000;
        }
        for (int i=0;i<height;i++){
            energyMap[i][0] = 1000;
            energyMap[i][width-1] = 1000;
        }
        for (int col = 1; col < width-1; col++) {
            for (int row = 1; row < height-1; row++) {
                updateEnergy(row,col);
            }
        }
    }

    private void updateEnergy(int row,int col){
        double deltaX = rgbDelta(row-1,col,row+1,col);
        double deltaY = rgbDelta(row,col-1,row,col+1);
        double energy = Math.sqrt(deltaX+deltaY);
        energyMap[row][col]= energy;
    }

    private double rgbDelta(int x1,int y1,int x2,int y2){
        int rgb1 = picture.getRGB(y1,x1);
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >>  8) & 0xFF;
        int b1 = (rgb1 >>  0) & 0xFF;

        int rgb2 = picture.getRGB(y2,x2);
        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >>  8) & 0xFF;
        int b2 = (rgb2 >>  0) & 0xFF;

        return delta(r1,r2) + delta(g1,g2) + delta(b1,b2);
    }

    private double delta(double a,double b){
        return Math.pow(a-b,2);
    }

    public Picture picture() {
        // current picture
        return new Picture(picture);
    }

    public int width() {
        // width of current picture
        return width;
    }

    public int height() {
        // height of current picture
        return height;
    }

    public double energy(int x, int y) {
        // energy of pixel at column x and row y
        validateInput(y,x);
        return energyMap[y][x];
    }

    public int[] findHorizontalSeam() {
        // sequence of indices for horizontal seam

        if (height==1){
            //return 0,0,0,0,0,...
            return new int[width];
        }

        double[][] distTo = new double[height][width];
        int[][] pathTo = new int[height][width];

        for (int row = 0; row < height; row++) {
            distTo[row][0] = energyMap[row][0];
            pathTo[row][0] = row;
        }

        final int[] d = {-1,1};
        for(int col=1;col<width;col++){

            //最上面和最底下的点只有两个路径到达，所以拉出来特殊处理
            int init0 = distTo[0][col-1]<distTo[1][col-1]?0:1;
            distTo[0][col] = distTo[init0][col-1] + energyMap[0][col];
            pathTo[0][col] = init0;
            int initn = distTo[height-1][col-1]<distTo[height-2][col-1]?height-1:height-2;
            distTo[height-1][col] = distTo[initn][col-1] + energyMap[height-1][col];
            pathTo[height-1][col] = initn;

            for (int row = 1; row < height-1; row++) {
                distTo[row][col] = distTo[row][col-1] + energyMap[row][col];
                pathTo[row][col] = row;
                for (int p :d) {
                    double newDis = distTo[row+p][col-1] + energyMap[row][col];
                    if (distTo[row][col]>newDis){
                        distTo[row][col] = newDis;
                        pathTo[row][col] = row+p;
                    }
                }
            }

        }


        int end = 0;
        double endDis = distTo[0][width-1];
        for (int row = 1; row < height; row++) {
            double newDis = distTo[row][width-1];
            if (newDis<endDis){
                end = row;
                endDis = newDis;
            }
        }

        int[] path = new int[width];

        path[width-1] = end;
        for (int col=width-2;col>-1;col--){
            path[col] = pathTo[path[col+1]][col+1];
        }

        return path;
    }

    public int[] findVerticalSeam() {
        // sequence of indices for vertical seam

        if (width==1){
            //return 0,0,0,0,0,...
            return new int[height];
        }

        double[][] distTo = new double[height][width];
        int[][] pathTo = new int[height][width];

        for (int col = 0; col < width; col++) {
            distTo[0][col] = energyMap[0][col];
            pathTo[0][col] = col;
        }

        final int[] d = {-1,1};
        for(int row=1;row<height;row++){

            //最左边和最右边的点只有两个路径到达，所以拉出来特殊处理
            int init0 = distTo[row-1][0]<distTo[row-1][1]?0:1;
            distTo[row][0] = distTo[row-1][init0] + energyMap[row][0];
            pathTo[row][0] = init0;
            int initn = distTo[row-1][width-1]<distTo[row-1][width-2]?width-1:width-2;
            distTo[row][width-1] = distTo[row-1][initn] + energyMap[row][width-1];
            pathTo[row][width-1] = initn;

            for (int col = 1; col < width-1; col++) {
                distTo[row][col] = distTo[row-1][col] + energyMap[row][col];
                pathTo[row][col] = col;
                for (int p :d) {
                    double newDis = distTo[row-1][col+p] + energyMap[row][col];
                    if (distTo[row][col]>newDis){
                        distTo[row][col] = newDis;
                        pathTo[row][col] = col+p;
                    }
                }
            }

        }


        int end = 0;
        double endDis = distTo[height-1][0];
        for (int col = 1; col < width; col++) {
            double newDis = distTo[height-1][col];
            if (newDis<endDis){
                end = col;
                endDis = newDis;
            }
        }

        int[] path = new int[height];

        path[height-1] = end;
        for (int row=height-2;row>-1;row--){
            path[row] = pathTo[row+1][path[row+1]];
        }

        return path;
    }

    public void removeHorizontalSeam(int[] seam) {
        // remove horizontal seam from current picture
        if (seam==null||seam.length!=width||height<=1){
            throw new IllegalArgumentException();
        }
        int last=-1;
        for (int i: seam) {
            if (last!=-1&&Math.abs(last-i)>1){
                throw new IllegalArgumentException();
            }
            last=i;
            validateInput(i,0);
        }
        int[][] rgb = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                rgb[row][col] = picture.getRGB(col,row);
            }
        }

        Picture newPicture = new Picture(width,height-1);

        for (int col = 0; col < width; col++) {
            //a little trick to skip the deleted pixel
            int d = 0;
            for (int row = 0; row < height; row++) {
                if (row== seam[col]){
                    d = -1;
                    continue;
                }
                newPicture.setRGB(col,row+d,rgb[row][col]);
            }
        }
        this.picture = newPicture;
        this.height -= 1;

        int[] d = {0,-1};
        for (int col = 0; col < width; col++) {
            for(int i:d){
                try{
                    updateEnergy(seam[col]+i,col);
                }catch (ArrayIndexOutOfBoundsException|IllegalArgumentException ignore){
                }
            }
        }
    }

    public void removeVerticalSeam(int[] seam) {
        // remove vertical seam from current picture
        if (seam==null||seam.length!=height||width<=1){
            throw new IllegalArgumentException();
        }
        int last=-1;
        for (int i: seam) {
            if (last!=-1&&Math.abs(last-i)>1){
                throw new IllegalArgumentException();
            }
            last=i;
            validateInput(0,i);
        }
        int[][] rgb = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                rgb[row][col] = picture.getRGB(col,row);
            }
        }

        Picture newPicture = new Picture(width-1,height);

        for (int row = 0; row < height; row++) {
            int d = 0;
            for (int col = 0; col < width; col++) {
                if (col== seam[row]){
                    d = -1;
                    continue;
                }
                newPicture.setRGB(col+d,row,rgb[row][col]);
            }
        }
        this.picture = newPicture;
        this.width -= 1;

        int[] d = {0,-1};
        for (int row = 0; row < height; row++) {
            for(int i:d){
                try{
                    updateEnergy(row, seam[row]+i);
                }catch (ArrayIndexOutOfBoundsException|IllegalArgumentException ignore){
                }
            }
        }
    }

    private void validateInput(int row, int col){
        if (row<0||row>height-1||col<0||col>width-1){
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args){
        String fileName = "chameleon.png";
        Picture p = new Picture("./testFile/seam/"+fileName);
        SeamCarver seamCarver = new SeamCarver(p);
        seamCarver.findHorizontalSeam();
        int removeVertical = 200;
        for (int i = 0; i < removeVertical; i++) {
            seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
        }
        int removeHizontal = 000;
        for (int i = 0; i < removeHizontal; i++) {
            seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());
        }
        Picture p2 = seamCarver.picture;
        p2.show();
    }
}

/*
Corner cases. Your code should throw a java.lang.IllegalArgumentException when a constructor or method is
called with an invalid argument, as documented below:

By convention, the indices x and y are integers between 0 and width − 1 and between 0 and height − 1
respectively, where width is the width of the current image and height is the height. Throw a
java.lang.IllegalArgumentException if either x or y is outside its prescribed range.
Throw a java.lang.IllegalArgumentException if the constructor, removeVerticalSeam(), or removeHorizontalSeam()
is called with a null argument.
Throw a java.lang.IllegalArgumentException if removeVerticalSeam() or removeHorizontalSeam() is called with
an array of the wrong length or if the array is not a valid seam (i.e., either an entry is outside its
prescribed range or two adjacent entries differ by more than 1).
Throw a java.lang.IllegalArgumentException if removeVerticalSeam() is called when the width of the picture
is less than or equal to 1 or if removeHorizontalSeam() is called when the height of the picture is less
than or equal to 1.
 */