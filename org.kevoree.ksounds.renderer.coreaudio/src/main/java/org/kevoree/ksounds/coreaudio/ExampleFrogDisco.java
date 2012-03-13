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
import org.kevoree.ksounds.sample.LaProducer;

import java.nio.FloatBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ExampleFrogDisco extends CoreAudioRenderAdapter {

    private long sampleIndex = 0;
    private FrogDisco frogDisco;

    private BlockingQueue<float[]> input = new ArrayBlockingQueue<float[]>(100);

    public void put(float[] b) {
        try {
            input.put(b);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public ExampleFrogDisco() {
        System.out.println(this.getClass().getClassLoader().getClass().getName());
    }

    public void play() {
        frogDisco = new FrogDisco(1, 128, 44100.0, SampleFormat.UNINTERLEAVED_FLOAT, 4, this);
        frogDisco.play();
    }

    public void loadLib() {
        System.loadLibrary("FrogDisco");
    }

    LaProducer la = new LaProducer();
    
    @Override
    public void onCoreAudioFloatRenderCallback(final FloatBuffer buffer) {
/*
        int length = buffer.capacity();
            for (int i = 0; i < length; i++, sampleIndex++) {
              buffer.put((float) Math.sin(2.0 * Math.PI * 440.0 * sampleIndex / 44100.0));
            }*/

       try {
            
           // System.out.println("La");
            //buffer.put(la.generateLaBuffer(440));
          //  buffer.put(la.generateLaBuffer(440));
            buffer.put(input.take());
       } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}