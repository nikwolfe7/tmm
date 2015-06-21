package mlsp.cs.cmu.edu.tmm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class MFCCBlockStream implements Iterable<MFCCVector[]>, Iterator<MFCCVector[]> {

  private int blockSize;
  private String mfccExt = ".mfc";
  private LinkedBlockingQueue<MFCCVector> mfccs;
  private int pulseTimeInMilliseconds;
  
  public MFCCBlockStream(String filename, int blockSize) {
    this.blockSize = blockSize;
    this.mfccs = new LinkedBlockingQueue<MFCCVector>();
    if(filename.endsWith(mfccExt)) {
      File file = new File(filename);
      Scanner scn;
      try {
        scn = new Scanner(file);
        while(scn.hasNextLine()) {
          String[] line = scn.nextLine().trim().split("\\s+");
          MFCCVector vec = new MFCCVector(line.length);
          for(int i = 0; i < line.length; i++) {
            double val = Double.parseDouble(line[i]);
            vec.setCoefficient(i, val);
          }
         put(vec);
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
  
  public MFCCBlockStream(int blockSize) {
    this.blockSize = blockSize;
    this.mfccs = new LinkedBlockingQueue<MFCCVector>();
  }
  
  public void setPulseTime(int pulseTime) {
    this.pulseTimeInMilliseconds = pulseTime;
  }
  
  public void put(MFCCVector vector) {
    try {
      mfccs.put(vector);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
  
  @Override
  public Iterator<MFCCVector[]> iterator() {
    return this;
  }

  @Override
  public boolean hasNext() {
    return !mfccs.isEmpty();
  }

  @Override
  public MFCCVector[] next() {
    if (pulseTimeInMilliseconds > 0) {
      new Runnable() {
        @Override
        public void run() {
          try {
            Thread.currentThread();
            Thread.sleep(pulseTimeInMilliseconds);
          } catch (InterruptedException e1) {
            Thread.currentThread().interrupt();
          }
        }
      }.run();
    }
    int size = Math.min(blockSize, mfccs.size());
    MFCCVector[] block = new MFCCVector[size];
    for (int i = 0; i < size; i++) {
      try {
        block[i] = mfccs.take();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
    return block;
  }
  
  public static void main(String[] args) {
    MFCCBlockStream factory = new MFCCBlockStream("./features/My.mfc", 10);
    factory.setPulseTime(100);
    for(MFCCVector[] block : factory) {
      for(MFCCVector vec : block) {
        System.out.print("MFCC:\t");
        vec.printVector();
      }
    }
  }


  

}
