package org.biojava.demo;

import org.biojava.MyApplication;
import org.biojava.R;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by edvinas on 17.2.1.
 */

public class Temp {

    public static void main(String [] args){

        System.out.println("=============Suveike=================");


        Temp tmp = new Temp();
        tmp.testFile();

    }

    public void testFile(){

//        InputStream classpathIs = MyApplication.getAppContext().getResources().openRawResource(R.raw.iupac2);
//        System.out.print("asdf");
//        Resources.getSystem().getString(android.R.string.someuniversalstuff);
//        InputStream iS = Resources.getSystem().openRawResource();

        InputStream classpathIs = MyApplication.getAppContext().getResources().openRawResource(R.raw.iupac2);

        InputStream inStream = Temp.class.getResourceAsStream("/iupac.txt");

        String resource = "/home/edvinas/AndroidStudioProjects/Biojava/app/build/intermediates/sourceFolderJavaResources/test/debug/core/search/io/blast/small-blastreport.blastxml";
        URL resourceURL = getClass().getResource(resource);
        File file = new File(resourceURL.getFile());
        System.out.print("asdf");
    }

}
