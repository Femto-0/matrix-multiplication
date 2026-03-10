public class AppConfig {
    private int m;
    private int n;
    private int p;
    private int maxBuffSize;
    private int splitSize;
    private int numConsumer;
    private int maxProducerSleepTime;
    private int maxConsumerSleepTime;


    public int getSplitSize() {
        return splitSize;
    }

    public void setSplitSize(int splitSize) {
        this.splitSize = splitSize;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getMaxBuffSize() {
        return maxBuffSize;
    }

    public void setMaxBuffSize(int maxBuffSize) {
        this.maxBuffSize = maxBuffSize;
    }

    public int getNumConsumer() {
        return numConsumer;
    }

    public void setNumConsumer(int numConsumer) {
        this.numConsumer = numConsumer;
    }

    public int getMaxProducerSleepTime() {
        return maxProducerSleepTime;
    }

    public void setMaxProducerSleepTime(int maxProducerSleepTime) {
        this.maxProducerSleepTime = maxProducerSleepTime;
    }

    public int getMaxConsumerSleepTime() {
        return maxConsumerSleepTime;
    }

    public void setMaxConsumerSleepTime(int maxConsumerSleepTime) {
        this.maxConsumerSleepTime = maxConsumerSleepTime;
    }

}
