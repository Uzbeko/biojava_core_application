package org.biojava;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.biojava.demo.ParseFastaFileDemo;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.io.FastaReader;
import org.biojava.nbio.core.sequence.io.GenericFastaHeaderParser;
import org.biojava.nbio.core.sequence.io.ProteinSequenceCreator;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);


//---------------------test
        String resource = "org/biojava/nbio/core/sequence/kelios_sekos.fasta";
        long timeS = System.currentTimeMillis();


        Log.d("TEST", "gaidys!!!!!!!!!!!!!!!!!!!!!");
        System.err.println("EEEEEEEEEEEEEE TEstuoju");


        try {

            InputStream inStream = null;
            try {
                inStream = MyApplication.getAppContext().getAssets().open(resource);
            } catch (IOException e) {                                                //Todo reiktu apdoroti exeptionus
                System.err.println("An IOException was caught :" + e.getMessage());
                e.printStackTrace();
            }

            FastaReader<ProteinSequence, AminoAcidCompound> fastaReader = new FastaReader<ProteinSequence, AminoAcidCompound>(
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
            Logger.getLogger(ParseFastaFileDemo.class.getName()).log(Level.SEVERE, null, ex);
        }

//----------Button-------------------------------------------------------

        Button buton = (Button) findViewById(R.id.button);
        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        InputStream classpathIs = getApplicationContext().getResources().openRawResource(R.raw.iupac2);

                try {
                    String s = getSeq("ATGGCGGCGCTGAGCGGT").getRNASequence().getSequenceAsString();
                } catch (CompoundNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData(); //The uri with the location of the file
        }
    }

    private DNASequence getSeq() throws CompoundNotFoundException {
        return getSeq(null);
    }

    private DNASequence getSeq(final String seq) throws CompoundNotFoundException {
        String target = (seq == null) ? "ATGC" : seq;
        return new DNASequence(target);
    }


}
