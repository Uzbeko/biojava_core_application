package org.biojava.bl;

import android.os.AsyncTask;
import android.util.Log;

import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;

import java.io.IOException;

/**
 * Created by edvinas on 17.5.25.
 */

public class GenbankAsyncTask extends AsyncTask {

    private GenbankResponse observer;


    public GenbankAsyncTask(GenbankResponse bserver){
        this.observer = bserver;
    }

    @Override
    protected String doInBackground(Object[] params) {

        ProteinSequence proteinSequence = null;

        try {
            GenbankProxySequenceReader<AminoAcidCompound> genbankProteinReader = null;
            genbankProteinReader = new GenbankProxySequenceReader<AminoAcidCompound>(System.getProperty("java.io.tmpdir"), "NP_000257", AminoAcidCompoundSet.getAminoAcidCompoundSet());
            proteinSequence = new ProteinSequence(genbankProteinReader);
            genbankProteinReader.getHeaderParser().parseHeader(genbankProteinReader.getHeader(), proteinSequence);
//        Log.i(LOG,"Sequence("+proteinSequence.getAccession()+","+proteinSequence.getLength()+") = "+proteinSequence.getSequenceAsString().substring(0, 10)+"...");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (CompoundNotFoundException e) {
            e.printStackTrace();
        }


        return "Sequence("+proteinSequence.getAccession()+","+proteinSequence.getLength()+") = "+proteinSequence.getSequenceAsString().substring(0, 10)+"...";
    }

    @Override
    protected void onPostExecute(Object o) {
        observer.onGenbankResponse((String) o);
    }
}
