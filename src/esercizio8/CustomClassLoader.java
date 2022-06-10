package esercizio8;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CustomClassLoader extends ClassLoader {

    private int counter;
    public CustomClassLoader(ClassLoader parent) {
        super(parent);
        counter = 0;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if(name.startsWith("java.")) {
            System.out.println(name);
            return getClass(name);
        } 
        return super.loadClass(name);
    }
    
    private Class<?> getClass(String name) {
        String path = name.replace('.', File.separatorChar) + ".class";
        try {
            byte[] b = loadClassFileData(path);
            Class<?> cl = defineClass(name, b, 0, b.length);
            resolveClass(cl);
            return cl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] loadClassFileData(String name) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(name);
        byte buff[] = new byte[stream.available()];
        DataInputStream in = new DataInputStream(stream);
        in.readFully(buff);
        in.close();
        return buff;

    }

    public static void main(String[] args) {
        try {
            CustomClassLoader ccl = new CustomClassLoader(CustomClassLoader.class.getClassLoader());
            ccl.loadClass("java.lang.String");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
