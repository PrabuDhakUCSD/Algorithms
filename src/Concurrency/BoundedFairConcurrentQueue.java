package Concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.Random;
import java.util.Deque;
import java.util.ArrayDeque;

public class BoundedFairConcurrentQueue {
    int[] buffer;
    int capacity;
    int size;
    int readPos;
    int writePos;
    boolean isEmpty;
    
    Lock exclusiveLock;
    Condition canProduce;
    Condition canConsume;
    Random random;
    Deque<Long> producerQueue;
    Deque<Long> consumerQueue;
    
    public BoundedFairConcurrentQueue(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.readPos = 0;
        this.writePos = 0;
        buffer = new int[this.capacity];
        isEmpty = true;
        exclusiveLock = new ReentrantLock();
        canProduce = exclusiveLock.newCondition();
        canConsume = exclusiveLock.newCondition();
        random = new Random();
        producerQueue = new ArrayDeque<Long>();
        consumerQueue = new ArrayDeque<Long>();
    }
    
    public void produce() throws InterruptedException {
        exclusiveLock.lock();
        int item = generateItem();
        long threadId = Thread.currentThread().getId();
        producerQueue.addLast(threadId);
        
        while(size == capacity || producerQueue.getFirst() != threadId) {
            canProduce.await();
        }
        
        producerQueue.removeFirst();
        buffer[writePos] = item;
        size++;
        writePos = (writePos+1)%capacity;
        canConsume.signalAll();
    }
    
    public int consume() throws InterruptedException {
        exclusiveLock.lock();
        long threadId = Thread.currentThread().getId();
        consumerQueue.addLast(threadId);
        
        while(size == 0 || consumerQueue.getFirst() != threadId) {
            canConsume.await();
        }
        
        consumerQueue.removeFirst();
        int item = buffer[readPos];
        size--;
        readPos = (readPos+1)%capacity;
        canProduce.signalAll();
        return item;
    }

    public int generateItem() {
        return random.nextInt(100);
    }
    
    public static void main(String[] args) {
    }
}