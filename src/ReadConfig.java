import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ReadConfig {
    private int m=10;
    private int n=10;
    private int p=10;
    private int maxBuffSize=5;
    private int splitSize=3;
    private int numConsumer=2;
    private int maxProducerSleepTime=20;
    private int maxConsumerSleepTime=80;

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
