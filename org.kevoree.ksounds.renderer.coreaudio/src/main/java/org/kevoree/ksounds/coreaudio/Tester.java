package org.kevoree.ksounds.coreaudio;

import org.kevoree.kcl.KevoreeJarClassLoader;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: duke
 * Date: 13/03/12
 * Time: 20:38
 */
public class Tester {


    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException {

        KevoreeJarClassLoader kcl = new KevoreeJarClassLoader();
        kcl.addNativeMapping("FrogDisco", "/Users/duke/Documents/dev/dukeboard/kevoree-sounds/org.kevoree.ksounds.core.audio/lib/libFrogDisco.dylib");
        kcl.add("/Users/duke/Documents/dev/dukeboard/kevoree-sounds/org.kevoree.ksounds.core.audio/target/org.kevoree.ksounds-1.0.0-SNAPSHOT.jar");
        kcl.add("/Users/duke/Documents/dev/dukeboard/kevoree-sounds/org.kevoree.ksounds.core.audio/lib/FrogDisco.jar");
        Class clazz = kcl.loadClass("com.synthbot.frogdisco.ExampleFrogDisco");
        clazz.getMethod("play").invoke(clazz.newInstance());

        Thread.sleep(3000);
        System.out.println(clazz);

    }


}
