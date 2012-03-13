package org.kevoree.ksounds.sample;

import java.nio.FloatBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: duke
 * Date: 13/03/12
 * Time: 22:34
 */

public class LaProducer {

    int sampleIndex = 0;

    public float[] generateLaBuffer(int note) {

        FloatBuffer buf = FloatBuffer.allocate(128);
        for (int i = 0; i < buf.capacity(); i++, sampleIndex++) {
            buf.put((float) Math.sin(2.0 * Math.PI * note * sampleIndex / 44100.0));
        }

        return buf.array();
    }

}
