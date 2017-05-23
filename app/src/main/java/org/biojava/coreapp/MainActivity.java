package org.biojava.coreapp;

import android.app.ActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.biojava.MyApplication;
import org.biojava.bl.ParseFastaFile;
import org.biojava.bl.Translation;
import org.biojava.demo.ParseFastaFileDemo;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.io.FastaReader;
import org.biojava.nbio.core.sequence.io.GenericFastaHeaderParser;
import org.biojava.nbio.core.sequence.io.ProteinSequenceCreator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String LOG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//---------------------------------------------------------------------
        ActivityManager.RunningAppProcessInfo info = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(info);
//        final InputStream classpathIs = getClass().getClassLoader().getResourceAsStream("org/biojava/nbio/core/sequence/iupac.txt");//senoji direktorij
//        XmlPullParserFactory factory = null;
//        try {
//            factory = XmlPullParserFactory.newInstance();
//            XmlPullParser parser = factory.newPullParser();
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        }


        Log.e(LOG, System.getProperty("os.name"));
//---------------------test
        String resource = "org/biojava/nbio/core/sequence/kelios_sekos.fasta";
        long timeS = System.currentTimeMillis();


        Log.d(LOG, "gaidys!!!!!!!!!!!!!!!!!!!!!");
        System.err.println("EEEEEEEEEEEEEE TEstuoju1");


        try {

            InputStream inStream = null;
            try {
                inStream = MyApplication.getAppContext().getAssets().open(resource);
            } catch (IOException e) {                                                //Todo reiktu apdoroti exeptionus
                System.err.println("An IOException was caught :" + e.getMessage());
                e.printStackTrace();
            }

            FastaReader<ProteinSequence, AminoAcidCompound> fastaReader = new FastaReader<>(
                    inStream,
                    new GenericFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
                    new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet()));

            LinkedHashMap<String, ProteinSequence> b;

            int nrSeq = 0;

            while ((b = fastaReader.process(100)) != null) {
                for (String key : b.keySet()) {
                    nrSeq++;
                    System.out.println(nrSeq + " : " + key + " " + b.get(key));
                    if (nrSeq % 100000 == 0)
                        System.out.println(nrSeq);
                }

            }
            long timeE = System.currentTimeMillis();
            System.out.println("parsed a total of " + nrSeq + " TREMBL sequences! in " + (timeE - timeS));
        } catch (Exception ex) {
            Log.e(LOG, "exception", ex);
        }

//----------Buttons-------------------------------------------------------

        Button buton = (Button) findViewById(R.id.translate);
        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);


            }
        });

        Button buton1 = (Button) findViewById(R.id.toRNA);
        buton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        InputStream classpathIs = getApplicationContext().getResources().openRawResource(R.raw.iupac2);

                try {
                    String rnaString = getSeq("ATGGCGGCGCTGAGCGGT").getRNASequence().getSequenceAsString();
                    TextView outputView = (TextView) findViewById(R.id.outputView);
                    outputView.setText(rnaString);
                } catch (CompoundNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        Button buton2 = (Button) findViewById(R.id.parseFasta);
        buton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a file"), 124);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK) {
            Uri fileUri = data.getData(); //The uri with the location of the file

            try (InputStream fileInputsteam = getContentResolver().openInputStream(fileUri)) {

                Translation translate = new Translation();
                List<String> translatedSeq = translate.doTranslation(fileInputsteam);

                TextView outputView = (TextView) findViewById(R.id.outputView);
                outputView.setText(TextUtils.join(" \n\n ", translatedSeq));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 124 && resultCode == RESULT_OK) {

            Log.d(LOG, "suveike failo ivedimas is parse fasta ");
            Uri fileUri = data.getData(); //The uri with the location of the file

            ParseFastaFile parseF = new ParseFastaFile();
            LinkedHashMap<String, ProteinSequence> sequences = parseF.parseFasta(fileUri);

            TextView outputView = (TextView) findViewById(R.id.outputView);
            outputView.setText("");

            int nrSeq = 0;
            for (String key : sequences.keySet()) {
                nrSeq++;
                Log.d(LOG, nrSeq + " : " + key + " " + sequences.get(key));
                outputView.append(nrSeq + " : " + key + " " + sequences.get(key) + "\n");
            }
        }

    }



    private DNASequence getSeq(final String seq) throws CompoundNotFoundException {
        String target = (seq == null) ? "ATGC" : seq;
        return new DNASequence(target);
    }
}
