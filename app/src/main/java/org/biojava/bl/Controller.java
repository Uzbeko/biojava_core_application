package org.biojava.bl;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import org.biojava.MyApplication;
import org.biojava.coreapp.MainActivity;
import org.biojava.nbio.alignment.Alignments;
import org.biojava.nbio.alignment.SimpleGapPenalty;
import org.biojava.nbio.alignment.template.GapPenalty;
import org.biojava.nbio.alignment.template.PairwiseSequenceAligner;
import org.biojava.nbio.core.alignment.matrices.SubstitutionMatrixHelper;
import org.biojava.nbio.core.alignment.template.SequencePair;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.search.io.Hit;
import org.biojava.nbio.core.search.io.Result;
import org.biojava.nbio.core.search.io.blast.BlastXMLParser;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AmbiguityDNACompoundSet;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.compound.DNACompoundSet;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.io.DNASequenceCreator;
import org.biojava.nbio.core.sequence.io.FastaReader;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.io.GenericFastaHeaderParser;
import org.biojava.nbio.core.sequence.io.ProteinSequenceCreator;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;
import org.biojava.nbio.core.sequence.template.CompoundSet;
import org.biojava.nbio.core.sequence.template.SequenceMixin;
import org.biojava.nbio.core.sequence.template.SequenceView;
import org.biojava.nbio.core.sequence.views.ComplementSequenceView;
import org.biojava.nbio.core.sequence.views.ReversedSequenceView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.biojava.coreapp.R.id.outputView;
import static org.biojava.coreapp.R.id.outputView2;

/**
 * Created by edvinas on 17.5.24.
 */

public class Controller  implements GenbankResponse {

    public static final String LOG = Controller.class.getSimpleName();


    private Uri fileUri;
    private int caseIndex;
    private Context activityContext;

    public Controller (Context context){
        this.activityContext = context;
    }

//-------------------methods------------------------------------

    public String swichCase(int caseIndex, Uri fileUri){
        this.fileUri = fileUri;
        this.caseIndex = caseIndex;

        switch (caseIndex){
            case 120:
                return allTranslationFrames(fileUri);
            case 121:
                return translateToRna(fileUri);
            case 122:
                return parseFasta (fileUri);
            case 123:
                return inverse (fileUri);
            case 124:
                return complementSequence (fileUri);
            case 125:
                return reversedSequence (fileUri);
            case 126:
                return subSequence (fileUri);
            case 127:
                return gcCount (fileUri);
            case 128:
                return atCount (fileUri);
            case 129:
                return composition (fileUri);
            case 130:
                return kmerNonOverlap (fileUri);
            case 131:
                return kmerOverlap (fileUri);
            case 132:
                return blastXmpreportParse (fileUri);
            case 133:
                return genebankParse (fileUri);
            case 134:
                return genebankProxyReader (fileUri);
            case 135:
                return needlemanWunsch (fileUri);
        }

        return "";
    }

    //-------------------------------------------------------------------------allTranslationFrames
    public String allTranslationFrames(Uri fileUri){

        String result = "";

        try (InputStream fileInputsteam = MyApplication.getAppContext().getContentResolver().openInputStream(fileUri)) {
            Translation translate = new Translation();
            List<String> translatedSeq = translate.doTranslation(fileInputsteam);
            result = TextUtils.join(" \n\n ", translatedSeq);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //------------------------------------------------------------------------translateToRna
    public String translateToRna(Uri fileUri){

        LinkedHashMap<String, ProteinSequence> sequences = null;
        StringBuilder result = new StringBuilder();

        try (InputStream fileInputsteam = MyApplication.getAppContext().getContentResolver().openInputStream(fileUri)) {

            FastaReader<ProteinSequence, AminoAcidCompound> fastaReader = new FastaReader<>(
                    fileInputsteam,
                    new GenericFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
                    new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet()));

            sequences = fastaReader.process();

            int nrSeq = 0;

            for (String key : sequences.keySet()) {
                nrSeq++;
                result.append(nrSeq + " : " + key + " " + new DNASequence(sequences.get(key).toString()).getRNASequence().getSequenceAsString()+"\n\n");
            }
            Log.d(LOG, result.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CompoundNotFoundException e) {
            e.printStackTrace();
        }

        return result.toString();

    }
    //--------------------------------------------------------------------------parseFasta

    public String parseFasta (Uri fileUri){

        ParseFastaFile parseF = new ParseFastaFile();
        LinkedHashMap<String, ProteinSequence> sequences = parseF.parseFasta(fileUri);
        StringBuilder result = new StringBuilder();

            int nrSeq = 0;
            for (String key : sequences.keySet()) {
                nrSeq++;
                result.append(nrSeq + " : " + key + " " + sequences.get(key) + "\n\n");
            }
            Log.d(LOG, result.toString());

        return result.toString();
    }
    //--------------------------------------------------------------------------------inverse
    public String inverse (Uri fileUri){

        StringBuilder result = new StringBuilder();

        try (InputStream fileInputsteam = MyApplication.getAppContext().getContentResolver().openInputStream(fileUri)) {

                FastaReader<ProteinSequence, AminoAcidCompound> fastaReader = new FastaReader<>(
                        fileInputsteam,
                        new GenericFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
                        new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet()));

                LinkedHashMap<String, ProteinSequence> sequences = fastaReader.process();

                int nrSeq = 0;
                for (String key : sequences.keySet()) {
                    nrSeq++;
                    result.append(nrSeq + " : " + key + " " + new DNASequence(sequences.get(key).toString()).getInverse().getSequenceAsString() + "\n\n");
                }
                Log.d(LOG, result.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CompoundNotFoundException e) {
                e.printStackTrace();
            }

        return result.toString();

    }
//-------------------------------------------------------------------------------------------complementSequence
    public String complementSequence (Uri fileUri){

        StringBuilder result = new StringBuilder();

            try (InputStream fileInputsteam = MyApplication.getAppContext().getContentResolver().openInputStream(fileUri)) {

                FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
                        fileInputsteam,
                        new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
                        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));

                LinkedHashMap<String, DNASequence> sequences = fastaReader.process();

                int nrSeq = 0;
                for (String key : sequences.keySet()) {
                    nrSeq++;
                    result.append(nrSeq + " : " + key + " " + new ComplementSequenceView<NucleotideCompound>(sequences.get(key)).getSequenceAsString() + "\n\n");
                }
                Log.d(LOG, result.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return result.toString();
    }

//-------------------------------------------------------------------------------------------ReversedSequence
    public String reversedSequence (Uri fileUri){

        StringBuilder result = new StringBuilder();

        try (InputStream fileInputsteam = MyApplication.getAppContext().getContentResolver().openInputStream(fileUri)) {

                FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
                        fileInputsteam,
                        new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
                        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));

                LinkedHashMap<String, DNASequence> sequences = fastaReader.process();

                int nrSeq = 0;
                for (String key : sequences.keySet()) {
                    nrSeq++;
                    result.append(nrSeq + " : " + key + " " + new ReversedSequenceView<NucleotideCompound>(sequences.get(key)).getSequenceAsString() + "\n\n");
                }
                Log.d(LOG, result.toString());


        } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return result.toString();
    }
    //-------------------------------------------------------------------------------------------subSequence
    public String subSequence (Uri fileUri){
        StringBuilder result = new StringBuilder();

        try (InputStream fileInputsteam = MyApplication.getAppContext().getContentResolver().openInputStream(fileUri)) {
            FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
                    fileInputsteam,
                    new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
                    new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));

            LinkedHashMap<String, DNASequence> sequences = fastaReader.process();

            int nrSeq = 0;
            for (String key : sequences.keySet()) {
                nrSeq++;
                result.append(nrSeq + " : " + key + " " + sequences.get(key).getSubSequence(7, 17).getSequenceAsString() + "\n\n");
            }
            Log.d(LOG, result.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();

    }
//-------------------------------------------------------------------------------------------GC count
    public String gcCount (Uri fileUri){
        StringBuilder result = new StringBuilder();
            try (InputStream fileInputsteam = MyApplication.getAppContext().getContentResolver().openInputStream(fileUri)) {

                FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
                        fileInputsteam,
                        new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
                        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));

                LinkedHashMap<String, DNASequence> sequences = fastaReader.process();

                int nrSeq = 0;
                for (String key : sequences.keySet()) {
                    nrSeq++;
                    result.append(nrSeq + " : " + key + "; GC count: " + sequences.get(key).getGCCount()+ "\n\n");
                }
                Log.d(LOG, result.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return result.toString();
    }
//-------------------------------------------------------------------------------------------AT count
    public String atCount (Uri fileUri){
        StringBuilder result = new StringBuilder();
            try (InputStream fileInputsteam = MyApplication.getAppContext().getContentResolver().openInputStream(fileUri)) {

                FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
                        fileInputsteam,
                        new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
                        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));

                LinkedHashMap<String, DNASequence> sequences = fastaReader.process();

                int nrSeq = 0;
                for (String key : sequences.keySet()) {
                    nrSeq++;
                    result.append(nrSeq + " : " + key + "; AT count: " + SequenceMixin.countAT(sequences.get(key))+ "\n\n");
                }
                Log.d(LOG, result.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return result.toString();
    }
    //-------------------------------------------------------------------------------------------composition
    public String composition (Uri fileUri){

        StringBuilder result = new StringBuilder();

            try (InputStream fileInputsteam = MyApplication.getAppContext().getContentResolver().openInputStream(fileUri)) {
                FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
                        fileInputsteam,
                        new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
                        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));

                LinkedHashMap<String, DNASequence> sequences = fastaReader.process();

                int nrSeq = 0;
                DNASequence seq;
                CompoundSet<NucleotideCompound> set;
                Map<NucleotideCompound, Double> distribution;

                for (String key : sequences.keySet()) {

                    seq= sequences.get(key);
                    set= seq.getCompoundSet();
                    distribution = SequenceMixin.getDistribution(seq);

                    nrSeq++;
                    result.append(nrSeq + " : " + key + "; copmosition: \n");
                    result.append("A distribution: "+ distribution.get(set.getCompoundForString("A"))+"\n");
                    result.append("T distribution: "+ distribution.get(set.getCompoundForString("T"))+"\n");
                    result.append("G distribution: "+ distribution.get(set.getCompoundForString("G"))+"\n");
                    result.append("C distribution: "+ distribution.get(set.getCompoundForString("C"))+"\n\n");

                }
                Log.d(LOG, result.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return result.toString();
    }
    //-------------------------------------------------------------------------------------------kmerNonOverlap
    public String kmerNonOverlap (Uri fileUri){
        StringBuilder result = new StringBuilder();

            try (InputStream fileInputsteam = MyApplication.getAppContext().getContentResolver().openInputStream(fileUri)) {
                FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
                        fileInputsteam,
                        new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
                        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));

                LinkedHashMap<String, DNASequence> sequences = fastaReader.process();

                int nrSeq = 0;
                DNASequence seq;
                List<SequenceView<NucleotideCompound>> list;

                for (String key : sequences.keySet()) {

                    seq = sequences.get(key);
                    list = SequenceMixin.nonOverlappingKmers(seq, 7);

                    result.append(nrSeq + " : " + key + "; kmerNonOverlap count: "+list.size()+"\n");
                    for(int i=0; i<list.size(); i++){
                        result.append(i+": k-mer: "+ list.get(i).getSequenceAsString()+"\n");
                    }
                    result.append("\n");
                }
                Log.d(LOG, result.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return result.toString();
    }
    //-------------------------------------------------------------------------------------------kmerOverlap
    public String kmerOverlap (Uri fileUri){
        StringBuilder result = new StringBuilder();

            try (InputStream fileInputsteam = MyApplication.getAppContext().getContentResolver().openInputStream(fileUri)) {
                FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
                        fileInputsteam,
                        new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
                        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));

                LinkedHashMap<String, DNASequence> sequences = fastaReader.process();

                int nrSeq = 0;
                DNASequence seq;
                List<SequenceView<NucleotideCompound>> list;

                for (String key : sequences.keySet()) {

                    seq = sequences.get(key);
                    list = SequenceMixin.overlappingKmers(seq, 207);

                    result.append(nrSeq + " : " + key + "; kmerNonOverlap count: "+list.size()+"\n");
                    for(int i=0; i<list.size(); i++){
                        result.append(i+": k-mer: "+ list.get(i).getSequenceAsString()+"\n");
                    }
                    result.append("\n");

                }
                Log.d(LOG, result.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return result.toString();
    }
    //----------------------------------------------------------------------blast_report_parser
    public String blastXmpreportParse (Uri fileUri){

        StringBuilder resultSq = new StringBuilder();

            try (InputStream fileInputsteam = MyApplication.getAppContext().getContentResolver().openInputStream(fileUri)) {
                BlastXMLParser instance = new BlastXMLParser();
                instance.setStream(fileInputsteam);

                List<Result> result = instance.createObjects(1e-10);

                for(Hit hit : result.iterator().next()){
                    resultSq.append(hit.getHitNum()+" : "+hit.getHitId()+" : "+hit.getHitDef()+" : "+hit.getHitAccession()+" : "+hit.getHitLen()+"\n");
                }
                Log.d(LOG, result.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        return resultSq.toString();
    }
    //---------------------------------------------------------------------genebank
    public String genebankParse (Uri fileUri){

        StringBuilder result = new StringBuilder();

            try (InputStream fileInputsteam = MyApplication.getAppContext().getContentResolver().openInputStream(fileUri)) {
                LinkedHashMap<String, DNASequence> dnaSequences = GenbankReaderHelper.readGenbankDNASequence(fileInputsteam);

                for (DNASequence sequence : dnaSequences.values()) {
                    result.append("DNA Sequence: "+ sequence.getSequenceAsString()+"\n");
                }
                Log.d(LOG, result.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        return result.toString();
    }
    //----------------------------------------------------------------------genebank from internet (NP_000257)
    public String genebankProxyReader (Uri fileUri){
        String result="NP_000257";

        try (InputStream fileInputsteam = MyApplication.getAppContext().getContentResolver().openInputStream(fileUri)) {

            BufferedReader in = new BufferedReader(new InputStreamReader(fileInputsteam));
            result = in.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GenbankAsyncTask genbAsyncTask = new GenbankAsyncTask(this,result);
            genbAsyncTask.execute("");

        return "wait for result";
    }

    //----------------------------------------------------------------------needlemanWunsch išlyginimas
    public String needlemanWunsch (Uri fileUri) {

        StringBuilder result = new StringBuilder();

        try (InputStream fileInputsteam = MyApplication.getAppContext().getContentResolver().openInputStream(fileUri)) {

            FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
                    fileInputsteam,
                    new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
                    new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));

            LinkedHashMap<String, DNASequence> sequences = fastaReader.process();

            Iterator iterator = sequences.keySet().iterator();

            String target = sequences.get(iterator.next()).toString();
            String query = sequences.get(iterator.next()).toString();
//            GapPenalty penalty = new SimpleGapPenalty(-14, -4);
//            GapPenalty penalty = new SimpleGapPenalty(5,2);
            GapPenalty penalty = new SimpleGapPenalty();
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


            result.append("Alignment: \n"+ alignment);
            result.append("Number of identical residues: "+ identical+"\n");
            result.append("% identical query: "+ identical / (float) query.length()+"\n");
            result.append("% identical target: "+ identical / (float) target.length());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CompoundNotFoundException e) {
            e.printStackTrace();
        }

        return result.toString();

    }
    //-------------------------------------------------------
    @Override
    public void onGenbankResponse(String result) {

        MainActivity mainActivity = (MainActivity) activityContext;
        mainActivity.setTextView(result);
    }

}
