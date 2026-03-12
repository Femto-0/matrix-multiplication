public class WorkItem {
    int[][] subA; //holds the sub-matrix for matrix A that's going to be put in the queue by producer
    int[][] subB; //sub-matrix B
    int[][] subC; //sub matrix of result matrix C
    int lowA;
    int highA;
    int lowB;
    int highB; //the low and high indices of matrix A and B. Going to be determined by "SplitSize"
    boolean done; //false if the producer created this item, true once consumer processes it

    public int[][] getSubC() {
        return subC;
    }

    public void setSubC(int[][] subC) {
        this.subC = subC;
    }

    public int[][] getSubA() {
        return subA;
    }

    public void setSubA(int[][] subA) {
        this.subA = subA;
    }

    public int[][] getSubB() {
        return subB;
    }

    public void setSubB(int[][] subB) {
        this.subB = subB;
    }

    public int getLowA() {
        return lowA;
    }

    public void setLowA(int lowA) {
        this.lowA = lowA;
    }

    public int getHighA() {
        return highA;
    }

    public void setHighA(int highA) {
        this.highA = highA;
    }

    public int getLowB() {
        return lowB;
    }

    public void setLowB(int lowB) {
        this.lowB = lowB;
    }

    public int getHighB() {
        return highB;
    }

    public void setHighB(int highB) {
        this.highB = highB;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {this.done = done;}
}
