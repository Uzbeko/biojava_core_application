package org.biojava.bl;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AmbiguityDNACompoundSet;
import org.biojava.nbio.core.sequence.compound.AmbiguityRNACompoundSet;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.io.DNASequenceCreator;
import org.biojava.nbio.core.sequence.io.FastaReader;
import org.biojava.nbio.core.sequence.io.GenericFastaHeaderParser;
import org.biojava.nbio.core.sequence.template.CompoundSet;
import org.biojava.nbio.core.sequence.template.Sequence;
import org.biojava.nbio.core.sequence.transcription.Frame;
import org.biojava.nbio.core.sequence.transcription.TranscriptionEngine;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by edvinas on 17.5.11.
 */

public class Translation {

    public static final String LOG = Translation.class.getSimpleName();


    public List<String> doTranslation(InputStream inputStream) {

        List<String> stringList = new ArrayList<>();

        try {

            // define the Ambiguity Compound Sets
            AmbiguityDNACompoundSet ambiguityDNACompoundSet = AmbiguityDNACompoundSet.getDNACompoundSet();
            CompoundSet<NucleotideCompound> nucleotideCompoundSet = AmbiguityRNACompoundSet.getRNACompoundSet();

            FastaReader<DNASequence, NucleotideCompound> proxy = new FastaReader<>(
                            inputStream,
                            new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
                            new DNASequenceCreator(ambiguityDNACompoundSet));

            // has only one entry in this example, but could be easily extended to parse a FASTA file with multiple sequences
            LinkedHashMap<String, DNASequence> dnaSequences = proxy.process();

            // Initialize the Transcription Engine
            TranscriptionEngine engine = new
                    TranscriptionEngine.Builder().dnaCompounds(ambiguityDNACompoundSet).rnaCompounds(nucleotideCompoundSet).build();

            Frame[] sixFrames = Frame.getAllFrames();

            for (DNASequence dna : dnaSequences.values()) {

                Map<Frame, Sequence<AminoAcidCompound>> results = engine.multipleFrameTranslation(dna, sixFrames);

                for (Frame frame : sixFrames){

                    stringList.add("Translated Frame:" + frame + " : " + results.get(frame));
                    //System.out.println(dna.getRNASequence(frame).getProteinSequence(engine));

                    ProteinSequence ps = new ProteinSequence(results.get(frame).getSequenceAsString());
                    System.out.println(ps);
                }

            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return stringList;
    }

}
