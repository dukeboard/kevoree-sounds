package org.kevoree.ksounds.coreaudio;

/**
 * Created by IntelliJ IDEA.
 * User: duke
 * Date: 13/03/12
 * Time: 20:39
 */
import com.synthbot.frogdisco.CoreAudioRenderAdapter;
import com.synthbot.frogdisco.FrogDisco;
import com.synthbot.frogdisco.SampleFormat;

import java.nio.FloatBuffer;

public class ExampleFrogDisco extends CoreAudioRenderAdapter {

  private long sampleIndex = 0;
  private FrogDisco frogDisco;

  public ExampleFrogDisco() {
    System.out.println(this.getClass().getClassLoader().getClass().getName());
    frogDisco = new FrogDisco(1, 128, 44100.0, SampleFormat.UNINTERLEAVED_FLOAT, 4, this);
  }

  public void play() {
    frogDisco.play();
  }
    
    public void loadLib(){
        System.loadLibrary("FrogDisco");
    }

  @Override
  public void onCoreAudioFloatRenderCallback(FloatBuffer buffer) {
    /*
     * Put your audio code here.
     * This example produces a 440Hz sine wave.
     */
    int length = buffer.capacity();
    for (int i = 0; i < length; i++, sampleIndex++) {
      buffer.put((float) Math.sin(2.0 * Math.PI * 240.0 * sampleIndex / 44100.0));
    }
  }

  public static void main(String[] args) {
    ExampleFrogDisco example = new ExampleFrogDisco();
    example.play();
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("ExampleFrogDisco exiting.");
  }
}