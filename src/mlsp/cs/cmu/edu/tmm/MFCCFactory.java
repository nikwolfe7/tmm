package mlsp.cs.cmu.edu.tmm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class MFCCFactory implements Iterable<MFCCVector[]>, Iterator<MFCCVector[]> {

  private int blockSize;
  private String mfccExt = ".mfc";
  private LinkedBlockingQueue<MFCCVector> mfccs;
  private int pulseTimeInMilliseconds;
  
  public MFCCFactory(String filename, int blockSize) {
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
  
  public MFCCFactory(int blockSize) {
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
    if(pulseTimeInMilliseconds > 0) {
      try {
        Thread.currentThread();
        Thread.sleep(pulseTimeInMilliseconds);
      } catch (InterruptedException e1) {
        Thread.currentThread().interrupt();
      }
    }
    int size = Math.min(blockSize, mfccs.size());
    MFCCVector[] block = new MFCCVector[size];
    for(int i = 0; i < size; i++) {
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
    MFCCFactory factory = new MFCCFactory("./features/My.mfc", 3);
    factory.setPulseTime(500);
    for(MFCCVector[] block : factory) {
      for(MFCCVector vec : block)
        vec.printVector();
    }
  }


  

}
