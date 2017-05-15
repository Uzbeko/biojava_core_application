package org.biojava;

import android.support.test.runner.AndroidJUnit4;

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
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by edvinas on 17.5.4.
 */

@RunWith(AndroidJUnit4.class)
public class ParseFileFastaDemo {


//    @Test
//    @Ignore
//    public void readFastaFromFile(){
//        int mb = 1024*1024;
//
//        //Getting the runtime reference from system
//        Runtime runtime = Runtime.getRuntime();
//
//        System.out.println("##### Heap utilization statistics [MB] #####");
//
//        //Print used memory
//        System.out.println("Used Memory:"
//                + (runtime.totalMemory() - runtime.freeMemory()) / mb);
//
//        //Print free memory
//        System.out.println("Free Memory:"
//                + runtime.freeMemory() / mb);
//
//        //Print total available memory
//        System.out.println("Total Memory:" + runtime.totalMemory() / mb);
//
//        //Print Maximum available memory
//        System.out.println("Max Memory:" + runtime.maxMemory() / mb);
//
//
//        File f = new File(args[0]);
//
//        if ( ! f.exists()) {
//            System.err.println("File does not exist " + args[0]);
//            return;
//        }
//
//        long timeS = System.currentTimeMillis();
//
//        try {
//
//            // automatically uncompress files using InputStreamProvider
//            InputStreamProvider_Edvino isp = new InputStreamProvider_Edvino();
//
//            InputStream inStream = isp.getInputStream(f);
//
//
//            FastaReader<ProteinSequence, AminoAcidCompound> fastaReader = new FastaReader<ProteinSequence, AminoAcidCompound>(
//                    inStream,
//                    new GenericFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
//                    new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet()));
//
//            LinkedHashMap<String, ProteinSequence> b;
//
//            int nrSeq = 0;
//
//            while ((b = fastaReader.process(100)) != null) {
//                for (String key : b.keySet()) {
//                    nrSeq++;
//                    System.out.println(nrSeq + " : " + key + " " + b.get(key));
//                    if ( nrSeq % 100000 == 0)
//                        System.out.println(nrSeq );
//                }
//
//            }
//            long timeE = System.currentTimeMillis();
//            System.out.println("parsed a total of " + nrSeq + " TREMBL sequences! in " + (timeE - timeS));
//        } catch (Exception ex) {
//            Logger.getLogger(ParseFastaFileDemo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

//    @Test
//    public void readFastaInputstream(){
//
//        String resource = "org/biojava/nbio/core/sequence/kelios_sekos.fasta";
//        long timeS = System.currentTimeMillis();
//
//
//        Log.d("TEST","gaidys!!!!!!!!!!!!!!!!!!!!!");
//        System.err.println("EEEEEEEEEEEEEE TEstuoju");
//
//
//        try {
//
//            InputStream inStream = null;
//            try {
//                inStream = MyApplication.getAppContext().getAssets().open(resource);
//            } catch (IOException e) {                                                //Todo reiktu apdoroti exeptionus
//                System.err.println("An IOException was caught :"+e.getMessage());
//                e.printStackTrace();
//            }
//
//            FastaReader<ProteinSequence, AminoAcidCompound> fastaReader = new FastaReader<ProteinSequence, AminoAcidCompound>(
//                    inStream,
//                    new GenericFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
//                    new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet()));
//
//            LinkedHashMap<String, ProteinSequence> b;
//
//            int nrSeq = 0;
//
//            while ((b = fastaReader.process(100)) != null) {
//                for (String key : b.keySet()) {
//                    nrSeq++;
//                    System.out.println(nrSeq + " : " + key + " " + b.get(key));
//                    if ( nrSeq % 100000 == 0)
//                        System.out.println(nrSeq );
//                }
//
//            }
//            long timeE = System.currentTimeMillis();
//            System.out.println("parsed a total of " + nrSeq + " TREMBL sequences! in " + (timeE - timeS));
//        } catch (Exception ex) {
//            Logger.getLogger(ParseFastaFileDemo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    @Test
    public void demoTest() {
        String dnaFastaS = ">gb:GQ903697|Organism:Arenavirus H0030026 H0030026|Segment:S|Host:Rat\n" +
                "CGCACAGAGGATCCTAGGCGTTACTGACTTGCGCTAATAACAGATACTGTTTCATATTTAGATAAAGACC\n" +
                "CAGCCAACTGATTGGTCAGCATGGGACAACTTGTGTCCCTCTTCAGTGAAATTCCATCAATCATACACGA\n" +
//				"AGCTCTCAATGTTGCTCTCGTAGCTGTTAGCATCATTGCAATATTGAAAGGGGTTGTGAATGTTTGGAAG\n" +
//				"AGTGGAGTTTTGCAGCTTTTGGCCTTCTTGCTCCTGGCGGGAAGATCCTGCTCAGTCATAATTGGTCATC\n" +
//				"ATCTCGAACTGCAGCATGTGATCTTCAATGGGTCATCAATCACACCCTTTTTACCAGTTACATGTAAGAT\n" +
//				"CAATGATACCTACTTCCTACTAAGAGGCCCCTATGAAGCTGATTGGGCAGTTGAATTGAGTGTAACTGAA\n" +
//				"ACCACAGTCTTGGTTGATCTTGAAGGTGGCAGCTCAATGAAGCTGAAAGCCGGAAACATCTCAGGTTGTC\n" +
//				"TTGGAGACAACCCCCATCTGAGATCAGTGGTCTTCACATTGAATTGGTTGCTAACAGGATTAGATCATGT\n" +
//				"TATTGATTCTGACCCGAAAATTCTCTGTGATCTTAAAGACAGTGGGCACTTTCGTCTCCAGATGAACTTA\n" +
//				"ACAGAAAAGCACTATTGTGACAAGTTTCACATCAAAATGGGCAAGGTCTTTGGCGTATTCAAAGATCCGT\n" +
//				"GCATGGCTGGTGGTAAAATGTTTGCCATACTAAAAAATACCTCTTGGTCGAACCAGTGCCAAGGAAACCA\n" +
//				"TGTCAGCACCATTCATCTTGTCCTTCAGAGTAATTTCAAACAGGTCCTCAGTAGCAGGAAACTGTTGAAC\n" +
//				"TTTTTCAGCTGGTCATTGTCTGATGCCACAGGGGCTGATATGCCTGGTGGTTTTTGTCTGGAAAAATGGA\n" +
//				"TGTTGATTTCAAGTGAACTGAAATGCTTTGGAAACACAGCTGTGGCAAAGTGCAACTTAAATCATGACTC\n" +
//				"AGAGTTCTGTGACATGCTTAGGCTTTTTGATTTCAACAAAAAGGCAATAGTCACTCTTCAGAACAAAACA\n" +
//				"AAGCATCGGCTGGACACAGTAATTACTGCTATCAATTCATTGATCTCTGATAATATTCTTATGAAGAACA\n" +
//				"GGATTAAAGAATTGATAGATGTTCCTTACTGTAATTACACCAAATTTTGGTATGTCAATCACACAGGTCT\n" +
//				"AAATCTGCACACCCTTCCAAGATGTTGGCTTGTTAAAAATGGTAGCTACTTGAATGTGTCTGACTTCAGG\n" +
//				"AATGAGTGGATATTGGAGAGTGATCATCTTGTTTCGGAGATCCTTTCAAAGGAGTATGAGGAAAGGCAAA\n" +
//				"ATCGTACACCACTCTCACTGGTTGACATCTGTTTCTGGAGTACATTGTTTTACACAGCATCAATTTTCCT\n" +
//				"ACACCTCTTGAGAATTCCAACCCACAGACACATTGTTGGTGAGGGCTGCCCGAAGCCTCATAGGCTAAAC\n" +
//				"AGGCACTCAATATGTGCTTGTGGCCTTTTCAAACAAGAAGGCAGACCCTTGAGATGGGTAAGAAAGGTGT\n" +
//				"GAACAATGGTTGCTTGGTGGCCTCCATTGCTGCACCCCCCTAGGGGGGTGCAGCAATGGAGGTTCTCGYT\n" +
//				"GAGCCTAGAGAACAACTGTTGAATCGGGTTCTCTAAAGAGAACATCGATTGGTAGTACCCTTTTTGGTTT\n" +
//				"TTCATTGGTCACTGACCCTGAAAGCACAGCACTGAACATCAAACAGTCCAAAAGTGCACAGTGTGCATTT\n" +
//				"GTTGTGGCTGGTGCTGATCCTTTCTTCTTACTTTTAATGACTATTCCCTTATGTCTGTCACACAGATGTT\n" +
//				"CAAATCTCTTCCAAACAAGATCTTCAAAGAGCCGTGACTGTTCTGCGGTCAGTTTGACATCAACAATCTT\n" +
//				"CAAATCCTGTCTTCCATGCATATCAAAGAGCCTCCTAATATCATCAGCACCTTGCGCAGTGAAAACCATG\n" +
//				"GATTTAGGCAGACTCCTTATTATGCTTGTGATGAGGCCAGGTCGTGCATGTTCAACATCCTTCAGCAATA\n" +
//				"TCCCATGACAATATTTACTTTGGTCCTTAAAAGATTTTATGTCATTGGGTTTTCTGTAGCAGTGGATGAA\n" +
//				"TTTTTGTGATTCAGGCTGGTAAATTGCAAACTCAACAGGGTCATGTGGCGGGCCTTCAATGTCAATCCAT\n" +
//				"GTTGTGTCACTGACCATCAACGACTCTACACTTCTCTTCACCTGAGCCTCCACCTCAGGCTTGAGCGTGG\n" +
//				"ACAAGAGTGGGGCACCACCGTTCCGGATGGGGACTGGTGTTTTGCTTGGTAAACTCTCAAATTCCACAAC\n" +
//				"TGTATTGTCCCATGCTCTCCCTTTGATCTGTGATCTTGATGAAATGTAAGGCCAGCCCTCACCAGAGAGA\n" +
//				"CACACCTTATAAAGTATGTTTTCATAAGGATTCCTCTGTCCTGGTATGGCACTGATGAACATGTTTTCCC\n" +
//				"TCTTTTTGATCTCCAAGAGGGTTTTTATAATGGTTGTGAATGTGGACTCCTCAATCTTTATTGTTTCCAG\n" +
//				"CATGTTGCCACCATCAATCAGGCAAGCACCGGCTTTCACAGCAGCTGATAAACTAAGGTTGTAGCCTGAT\n" +
//				"ATGTTAATTTGAGAATCCTCCTGAGTGATTACCTTTAGAGAAGGATGCTTCTCCATCAAAGCATCTAAGT\n" +
//				"CACTTAAATTAGGGTATTTTGCTGTGTATAGCAACCCCAGATCTGTGAGGGCCTGAACCACATCATTTAG\n" +
//				"AGTTTCCCCTCCCTGTTCAGTCATACAGGAAATTGTGAGTGCTGGCATCGATCCAAATTGGTTGATCATA\n" +
//				"AGTGATGAGTCTTTAACGTCCCAGACTTTGACCACCCCTCCAGTTCTAGCCAACCCAGGTCTCTGAATAC\n" +
//				"CAACAAGTTGCAGAATTTCGGACCTCCTGGTGAGCTGTGTTGTAGAGAGGTTCCCTAGATACTGGCCACC\n" +
//				"TGTGGCTGTCAACCTCTCTGTTCTTTGAACTTTTTGCCTTAATTTGTCCAAGTCACTGGAGAGTTCCATT\n" +
//				"AGCTCTTCCTTTGACAATGATCCTATCTTAAGGAACATGTTCTTTTGGGTTGACTTCATGACCATCAATG\n" +
//				"AGTCAACTTCCTTATTCAAGTCCCTCAAACTAACAAGATCACTGTCATCTCTTTTAGACCTCCTCATCAT\n" +
//				"GCGTTGCACACTTGCAACCTTTGAAAAATCTAAGCCGGACAGAAGAGCCCTCGCGTCAGTTAGGACATCT\n" +
//				"GCCTTAACAGCAGTTGTCCAGTTCGAGAGTCCTCTCCTGAGAGACTGTGTCCATCTGAATGATGGGATTG\n" +
                "GTTGTTCGCTCATAGTGATGAAATTGCGCAGAGTTATCCAAAAGCCTAGGATCCTCTGTGCG";


        try {

            // parse the raw sequence from the string
            InputStream stream = new ByteArrayInputStream(dnaFastaS.getBytes());

            // define the Ambiguity Compound Sets
            AmbiguityDNACompoundSet ambiguityDNACompoundSet = AmbiguityDNACompoundSet.getDNACompoundSet();
            CompoundSet<NucleotideCompound> nucleotideCompoundSet = AmbiguityRNACompoundSet.getRNACompoundSet();

            FastaReader<DNASequence, NucleotideCompound> proxy =
                    new FastaReader<DNASequence, NucleotideCompound>(
                            stream,
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
                    System.out.println("Translated Frame:" + frame +" : " + results.get(frame));
                    //System.out.println(dna.getRNASequence(frame).getProteinSequence(engine));

                    ProteinSequence ps = new ProteinSequence(results.get(frame).getSequenceAsString());
                    System.out.println(ps);
                    try {

                    } catch (Exception e){
                        System.err.println(e.getMessage() + " when trying to translate frame " + frame);
                    }
                }

            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
