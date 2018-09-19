public class SeamCarver {
    public SeamCarver(Picture picture) {                // create a seam carver object based on the given picture
    }

    public Picture picture(){
        // current picture
        return null;
    }

    public     int width(){
        // width of current picture
        return 0;
    }

    public     int height(){
        // height of current picture
        return 0;
    }

    public  double energy(int x, int y){
        // energy of pixel at column x and row y
        return 0;
    }

    public   int[] findHorizontalSeam(){
        // sequence of indices for horizontal seam
        return null;
    }

    public   int[] findVerticalSeam(){
        // sequence of indices for vertical seam
        return null;
    }

    public    void removeHorizontalSeam(int[] seam) {
        // remove horizontal seam from current picture
    }

    public    void removeVerticalSeam(int[] seam){
        // remove vertical seam from current picture
    }
}

/*
Corner cases. Your code should throw a java.lang.IllegalArgumentException when a constructor or method is called with an invalid argument, as documented below:

By convention, the indices x and y are integers between 0 and width − 1 and between 0 and height − 1 respectively, where width is the width of the current image and height is the height. Throw a java.lang.IllegalArgumentException if either x or y is outside its prescribed range.
Throw a java.lang.IllegalArgumentException if the constructor, removeVerticalSeam(), or removeHorizontalSeam() is called with a null argument.
Throw a java.lang.IllegalArgumentException if removeVerticalSeam() or removeHorizontalSeam() is called with an array of the wrong length or if the array is not a valid seam (i.e., either an entry is outside its prescribed range or two adjacent entries differ by more than 1).
Throw a java.lang.IllegalArgumentException if removeVerticalSeam() is called when the width of the picture is less than or equal to 1 or if removeHorizontalSeam() is called when the height of the picture is less than or equal to 1.
 */