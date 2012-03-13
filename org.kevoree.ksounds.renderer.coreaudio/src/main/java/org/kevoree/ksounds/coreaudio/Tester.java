package org.kevoree.ksounds.coreaudio;

import org.kevoree.ksounds.sample.LaProducer;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: duke
 * Date: 13/03/12
 * Time: 20:38
 */
public class Tester {


    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {
        final ExampleFrogDisco[] hi = {null};

        new Thread() {
            @Override
            public void run() {
                hi[0] = new ExampleFrogDisco();
                hi[0].play();
            }
        }.start();

        final LaProducer p = new LaProducer();


        Thread t = new Thread() {
            @Override
            public void run() {
                int note = 220;
                while (true) {
                    note = note + 1 % 440;
                    if (hi[0] != null) {
                        hi[0].put(p.generateLaBuffer(440));
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }

            }
        };
        t.start();

        Thread.sleep(6000);
        /*
                KevoreeJarClassLoader kcl = new KevoreeJarClassLoader();
                kcl.addNativeMapping("FrogDisco", "/Users/duke/Documents/dev/dukeboard/kevoree-sounds/org.kevoree.ksounds.core.audio/lib/libFrogDisco.dylib");
                kcl.add("/Users/duke/Documents/dev/dukeboard/kevoree-sounds/org.kevoree.ksounds.core.audio/target/org.kevoree.ksounds-1.0.0-SNAPSHOT.jar");
                kcl.add("/Users/duke/Documents/dev/dukeboard/kevoree-sounds/org.kevoree.ksounds.core.audio/lib/FrogDisco.jar");
                Class clazz = kcl.loadClass("com.synthbot.frogdisco.ExampleFrogDisco");
                clazz.getMethod("play").invoke(clazz.newInstance());

                Thread.sleep(3000);
                System.out.println(clazz);
        */
    }


}
