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
  
  private boolean liveStream;

  public MFCCBlockStream(String filename, int blockSize) {
    this.blockSize = blockSize;
    this.liveStream = false;
    this.mfccs = new LinkedBlockingQueue<MFCCVector>();
    loadMFCCsFromFile(filename);
  }
  
  public MFCCBlockStream(String filename) {
    this.blockSize = TMMConstants.MFCC_BLOCK_SIZE.getValue();
    this.liveStream = false;
    this.mfccs = new LinkedBlockingQueue<MFCCVector>();
    loadMFCCsFromFile(filename);
  }
  
  public MFCCBlockStream(int blockSize) {
    this.blockSize = blockSize;
    this.liveStream = true;
    this.mfccs = new LinkedBlockingQueue<MFCCVector>();
  }

  public MFCCBlockStream() {
    this.blockSize = TMMConstants.MFCC_BLOCK_SIZE.getValue();
    this.liveStream = true;
    this.mfccs = new LinkedBlockingQueue<MFCCVector>();
  }

  private void loadMFCCsFromFile(String filename) {
    if (filename.endsWith(mfccExt)) {
      File file = new File(filename);
      Scanner scn;
      try {
        scn = new Scanner(file);
        while (scn.hasNextLine()) {
          String[] line = scn.nextLine().trim().split("\\s+");
          MFCCVector vec = new MFCCVector(line.length);
          for (int i = 0; i < line.length; i++) {
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
    if(liveStream)
      return true;
    else
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
    MFCCBlockStream factory = new MFCCBlockStream("./features/expanded_mfccs_38_dim.mfc");
    //factory.setPulseTime(100);
    for (MFCCVector[] block : factory) {
      for (MFCCVector vec : block) {
        System.out.print("MFCC:\t");
        vec.printVector();
      }
    }
  }

}
