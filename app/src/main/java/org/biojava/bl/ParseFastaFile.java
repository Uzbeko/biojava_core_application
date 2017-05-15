package org.biojava.bl;

import android.net.Uri;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.io.FastaReader;
import org.biojava.nbio.core.sequence.io.GenericFastaHeaderParser;
import org.biojava.nbio.core.sequence.io.ProteinSequenceCreator;
import org.biojava.nbio.core.util.InputStreamProvider;

import java.io.InputStream;
import java.util.LinkedHashMap;

/**
 * Created by edvinas on 17.5.12.
 */

public class ParseFastaFile {

    public static final String LOG = ParseFastaFile.class.getSimpleName();

    public LinkedHashMap<String, ProteinSequence> parseFasta(Uri is) {

        LinkedHashMap<String, ProteinSequence> sequences = new LinkedHashMap<>();


        // automatically uncompress files using InputStreamProvider
        InputStreamProvider isp = new InputStreamProvider();

        try (InputStream inStream = isp.getInputStream(is)) {

            FastaReader<ProteinSequence, AminoAcidCompound> fastaReader = new FastaReader<>(
                    inStream,
                    new GenericFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
                    new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet()));

            sequences = fastaReader.process();
            inStream.close();
//-----------------------------------------------------------------------------start


//            int nrSeq = 0;
//
//            while ((b = fastaReader.process(100)) != null) {
//                for (String key : b.keySet()) {
//                    nrSeq++;
//                    Log.d(LOG, nrSeq + " : " + key + " " + b.get(key));
//                    if ( nrSeq % 100000 == 0)
//                        System.out.println(nrSeq );
//                }
//
//            }
//            long timeE = System.currentTimeMillis();
//
//            Log.d(LOG,"parsed a total of " + nrSeq + " TREMBL sequences! in " + (timeE - timeS));
        } catch (Exception ex) {
            ex.printStackTrace();
//        Logger.getLogger(ParseFastaFileDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
//-----------------------------------------------------------------------------end
        return sequences;
    }


}
