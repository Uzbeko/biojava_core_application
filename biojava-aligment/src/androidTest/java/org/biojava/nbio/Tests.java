package org.biojava.nbio;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.biojava.nbio.alignment.Alignments;
import org.biojava.nbio.alignment.SimpleGapPenalty;
import org.biojava.nbio.alignment.template.GapPenalty;
import org.biojava.nbio.alignment.template.PairwiseSequenceAligner;
import org.biojava.nbio.core.alignment.matrices.SubstitutionMatrixHelper;
import org.biojava.nbio.core.alignment.template.SequencePair;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.AmbiguityDNACompoundSet;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by edvinas on 17.5.28.
 */

@RunWith(AndroidJUnit4.class)
public class Tests {

    public static final String LOG = Tests.class.getSimpleName();


    @Test
    public void test() throws CompoundNotFoundException {
        String query = "CATGGA";

        String target = "CTGGC";

        GapPenalty penalty = new SimpleGapPenalty(-14, -4);
        PairwiseSequenceAligner<DNASequence, NucleotideCompound> aligner = Alignments.getPairwiseAligner(
                new DNASequence(query, AmbiguityDNACompoundSet.getDNACompoundSet()),
                new DNASequence(target, AmbiguityDNACompoundSet.getDNACompoundSet()),
                Alignments.PairwiseSequenceAlignerType.GLOBAL,
                penalty, SubstitutionMatrixHelper.getNuc4_4());
        SequencePair<DNASequence, NucleotideCompound>
                alignment = aligner.getPair();

        Log.i(LOG,"Alignment: \n"+ alignment);

        int identical = alignment.getNumIdenticals();
        Log.i(LOG,"Number of identical residues: "+ identical);
        Log.i(LOG, "% identical query: "+ identical / (float) query.length());
        Log.i(LOG, "% identical target: "+ identical / (float) target.length());
    }

}
