package org.biojava.coreapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.biojava.bl.Controller;
import org.biojava.bl.GenbankAsyncTask;
import org.biojava.bl.ListAdapter;
import org.biojava.bl.ParseFastaFile;
import org.biojava.bl.Translation;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.search.io.Hit;
import org.biojava.nbio.core.search.io.Result;
import org.biojava.nbio.core.search.io.blast.BlastXMLParser;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.compound.DNACompoundSet;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.io.DNASequenceCreator;
import org.biojava.nbio.core.sequence.io.FastaReader;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.io.GenericFastaHeaderParser;
import org.biojava.nbio.core.sequence.io.ProteinSequenceCreator;
import org.biojava.nbio.core.sequence.template.CompoundSet;
import org.biojava.nbio.core.sequence.template.SequenceMixin;
import org.biojava.nbio.core.sequence.template.SequenceView;
import org.biojava.nbio.core.sequence.views.ComplementSequenceView;
import org.biojava.nbio.core.sequence.views.ReversedSequenceView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.biojava.coreapp.R.id.outputView2;


public class MainActivity extends AppCompatActivity {

    public static final String LOG = MainActivity.class.getSimpleName();


    private ListView buttonList;
    private ArrayList list = new ArrayList();
    private ArrayList<String> myData=new ArrayList<String>();
    private ArrayAdapter adapter;

    private TextView outputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//---------------------------------------------------------------------
//        ActivityManager.RunningAppProcessInfo info = new ActivityManager.RunningAppProcessInfo();
//        ActivityManager.getMyMemoryState(info);
////        final InputStream classpathIs = getClass().getClassLoader().getResourceAsStream("org/biojava/nbio/core/sequence/iupac.txt");//senoji direktorij
//        XmlPullParserFactory factory = null;
//        try {
//            factory = XmlPullParserFactory.newInstance();
//            XmlPullParser parser = factory.newPullParser();
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        }


        Log.e(LOG, System.getProperty("os.name"));
        outputView = (TextView) findViewById(outputView2);
//---------button list---------------------------------------------------------
        buttonList = (ListView) findViewById(R.id.Buttons);

        myData.add("Translate");
        myData.add("to_RNA");
        myData.add("ParseFasta");
        myData.add("inversija");
        myData.add("komplementari");
        myData.add("reversija");
        myData.add("posekis");
        myData.add("GC count");
        myData.add("AT count");
        myData.add("composition");
        myData.add("kmerNonOverlap");
        myData.add("kmerOverlap");
        myData.add("blast_report_paerser");
        myData.add("genebank");
        myData.add("genebank fom internet (NP_000257)");
        myData.add("other");

        ListAdapter adapter = new ListAdapter(this,myData);
        buttonList.setAdapter(adapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

    //-----------------------------------------------------------------------------------allCases
        if (resultCode == RESULT_OK) {
            Uri fileUri = data.getData(); //The uri with the location of the file
            Controller control = new Controller();
            String result = control.swichCase(requestCode, fileUri);
            TextView outputView = (TextView) findViewById(outputView2);
            outputView.setText(result);
        }

//    //---------------------------------------------------------------------------------- TRANSLATE_6
//            if (requestCode == 120 && resultCode == RESULT_OK) {
//                Uri fileUri = data.getData(); //The uri with the location of the file
//
//
//                try (InputStream fileInputsteam = getContentResolver().openInputStream(fileUri)) {
//
//
////                    Controller control = new Controller(fileInputsteam);
////                    List<String> translatedSeq = control.getResult(requestCode);
//
//                    Translation translate = new Translation();
//                    List<String> translatedSeq = translate.doTranslation(fileInputsteam);
////                    outputView.
//                    TextView outputView = (TextView) findViewById(outputView2);
//                    outputView.setText(TextUtils.join(" \n\n ", translatedSeq));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//    //----------------------------------------------------------------------translateToRna
//            if (requestCode == 121 && resultCode == RESULT_OK) {
//                Uri fileUri = data.getData(); //The uri with the location of the file
//
//                try (InputStream fileInputsteam = getContentResolver().openInputStream(fileUri)) {
//
//                    FastaReader<ProteinSequence, AminoAcidCompound> fastaReader = new FastaReader<>(
//                            fileInputsteam,
//                            new GenericFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
//                            new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet()));
//
//                    LinkedHashMap<String, ProteinSequence> sequences = fastaReader.process();
//
//                    TextView outputView = (TextView) findViewById(outputView2);
//
//                    int nrSeq = 0;
//                    for (String key : sequences.keySet()) {
//                        nrSeq++;
//                        String dnaToRNA = new DNASequence(sequences.get(key).toString()).getRNASequence().getSequenceAsString();
//                        Log.d(LOG, nrSeq + " : " + key + " " + dnaToRNA );
//                        outputView.append(nrSeq + " : " + key + " " + dnaToRNA + "\n");
//                    }
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (CompoundNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//
//    //--------------------------------------------------------------------------------------------parseFasta
//            if (requestCode == 122 && resultCode == RESULT_OK) {
//
//                Log.d(LOG, "suveike failo ivedimas is parse fasta ");
//                Uri fileUri = data.getData(); //The uri with the location of the file
//
//                ParseFastaFile parseF = new ParseFastaFile();
//                LinkedHashMap<String, ProteinSequence> sequences = parseF.parseFasta(fileUri);
//
//                TextView outputView = (TextView) findViewById(outputView2);
//                outputView.setText("");
//
//                int nrSeq = 0;
//                for (String key : sequences.keySet()) {
//                    nrSeq++;
//                    Log.d(LOG, nrSeq + " : " + key + " " + sequences.get(key));
//                    outputView.append(nrSeq + " : " + key + " " + sequences.get(key) + "\n\n");
//                }
//            }
//    //-------------------------------------------------------------------------------------------inversija
//        if (requestCode == 123 && resultCode == RESULT_OK) {
//            Uri fileUri = data.getData(); //The uri with the location of the file
//
//            try (InputStream fileInputsteam = getContentResolver().openInputStream(fileUri)) {
//
//                FastaReader<ProteinSequence, AminoAcidCompound> fastaReader = new FastaReader<>(
//                        fileInputsteam,
//                        new GenericFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
//                        new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet()));
//
//                LinkedHashMap<String, ProteinSequence> sequences = fastaReader.process();
//
//                TextView outputView = (TextView) findViewById(outputView2);
//
//                int nrSeq = 0;
//                for (String key : sequences.keySet()) {
//                    nrSeq++;
//                    String invertSquence = new DNASequence(sequences.get(key).toString()).getInverse().getSequenceAsString();
//                    Log.d(LOG, nrSeq + " : " + key + " " +  invertSquence);
//                    outputView.append(nrSeq + " : " + key + " " + invertSquence + "\n\n");
//                }
//
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (CompoundNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
////-------------------------------------------------------------------------------------------komplementari
//        if (requestCode == 124 && resultCode == RESULT_OK) {
//            Uri fileUri = data.getData(); //The uri with the location of the file
//
//            try (InputStream fileInputsteam = getContentResolver().openInputStream(fileUri)) {
//
//                FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
//                        fileInputsteam,
//                        new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
//                        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));
//
//                LinkedHashMap<String, DNASequence> sequences = fastaReader.process();
//
//                TextView outputView = (TextView) findViewById(outputView2);
//
//                int nrSeq = 0;
//                for (String key : sequences.keySet()) {
//                    nrSeq++;
//                    String komplementSquence = new ComplementSequenceView<NucleotideCompound>(sequences.get(key)).getSequenceAsString();
//                    Log.d(LOG, nrSeq + " : " + key + " " +  komplementSquence);
//                    outputView.append(nrSeq + " : " + key + " " + komplementSquence + "\n\n");
//                }
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
////-------------------------------------------------------------------------------------------reversija
//        if (requestCode == 125 && resultCode == RESULT_OK) {
//            Uri fileUri = data.getData(); //The uri with the location of the file
//
//            try (InputStream fileInputsteam = getContentResolver().openInputStream(fileUri)) {
//
//                FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
//                        fileInputsteam,
//                        new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
//                        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));
//
//                LinkedHashMap<String, DNASequence> sequences = fastaReader.process();
//
//                TextView outputView = (TextView) findViewById(outputView2);
//
//                int nrSeq = 0;
//                for (String key : sequences.keySet()) {
//                    nrSeq++;
//                    String reverttSquence = new ReversedSequenceView<NucleotideCompound>(sequences.get(key)).getSequenceAsString();
//                    Log.d(LOG, nrSeq + " : " + key + " " +  reverttSquence);
//                    outputView.append(nrSeq + " : " + key + " " + reverttSquence + "\n\n");
//                }
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
////-------------------------------------------------------------------------------------------subSequence
//
//        if (requestCode == 126 && resultCode == RESULT_OK) {
//            Uri fileUri = data.getData(); //The uri with the location of the file
//
//            try (InputStream fileInputsteam = getContentResolver().openInputStream(fileUri)) {
//
//                FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
//                        fileInputsteam,
//                        new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
//                        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));
//
//                LinkedHashMap<String, DNASequence> sequences = fastaReader.process();
//
//                TextView outputView = (TextView) findViewById(outputView2);
//
//                int nrSeq = 0;
//                for (String key : sequences.keySet()) {
//                    nrSeq++;
//                    String subSequence = sequences.get(key).getSubSequence(7, 17).getSequenceAsString();
//                    Log.d(LOG, nrSeq + " : " + key + " " +  subSequence);
//                    outputView.append(nrSeq + " : " + key + " " + subSequence + "\n\n");
//                }
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
////-------------------------------------------------------------------------------------------GC count
//
//        if (requestCode == 127 && resultCode == RESULT_OK) {
//            Uri fileUri = data.getData(); //The uri with the location of the file
//
//            try (InputStream fileInputsteam = getContentResolver().openInputStream(fileUri)) {
//
//                FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
//                        fileInputsteam,
//                        new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
//                        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));
//
//                LinkedHashMap<String, DNASequence> sequences = fastaReader.process();
//
//                TextView outputView = (TextView) findViewById(outputView2);
//
//                int nrSeq = 0;
//                for (String key : sequences.keySet()) {
//                    nrSeq++;
//                    int gcCount = sequences.get(key).getGCCount();
//                    Log.d(LOG, nrSeq + " : " + key + "; GC count: " + gcCount);
//                    outputView.append(nrSeq + " : " + key + "; GC count: " + gcCount+ "\n\n");
//                }
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
////-------------------------------------------------------------------------------------------AT count
//        if (requestCode == 128 && resultCode == RESULT_OK) {
//            Uri fileUri = data.getData(); //The uri with the location of the file
//
//            try (InputStream fileInputsteam = getContentResolver().openInputStream(fileUri)) {
//
//                FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
//                        fileInputsteam,
//                        new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
//                        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));
//
//                LinkedHashMap<String, DNASequence> sequences = fastaReader.process();
//
//                TextView outputView = (TextView) findViewById(outputView2);
//
//                int nrSeq = 0;
//                for (String key : sequences.keySet()) {
//                    nrSeq++;
//                    int atCount = SequenceMixin.countAT(sequences.get(key));
//                    Log.d(LOG, nrSeq + " : " + key + "; AT count: " + atCount);
//                    outputView.append(nrSeq + " : " + key + "; AT count: " + atCount+ "\n\n");
//                }
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
////-------------------------------------------------------------------------------------------composition
//        if (requestCode == 129 && resultCode == RESULT_OK) {
//            Uri fileUri = data.getData(); //The uri with the location of the file
//
//            try (InputStream fileInputsteam = getContentResolver().openInputStream(fileUri)) {
//
//                FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
//                        fileInputsteam,
//                        new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
//                        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));
//
//                LinkedHashMap<String, DNASequence> sequences = fastaReader.process();
//
//                TextView outputView = (TextView) findViewById(outputView2);
//
//                int nrSeq = 0;
//                for (String key : sequences.keySet()) {
//
//
//                    DNASequence seq = sequences.get(key);
//                    CompoundSet<NucleotideCompound> set = seq.getCompoundSet();
//                    Map<NucleotideCompound, Double> distribution = SequenceMixin.getDistribution(seq);
//
//                    nrSeq++;
//                    outputView.append(nrSeq + " : " + key + "; copmosition: \n");
//                    outputView.append("A distribution: "+ distribution.get(set.getCompoundForString("A"))+"\n");
//                    outputView.append("T distribution: "+ distribution.get(set.getCompoundForString("T"))+"\n");
//                    outputView.append("G distribution: "+ distribution.get(set.getCompoundForString("G"))+"\n");
//                    outputView.append("C distribution: "+ distribution.get(set.getCompoundForString("C"))+"\n\n");
//
//                }
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        //-------------------------------------------------------------------------------------------kmerNonOverlap
//        if (requestCode == 130 && resultCode == RESULT_OK) {
//            Uri fileUri = data.getData(); //The uri with the location of the file
//
//            try (InputStream fileInputsteam = getContentResolver().openInputStream(fileUri)) {
//
//                FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
//                        fileInputsteam,
//                        new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
//                        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));
//
//                LinkedHashMap<String, DNASequence> sequences = fastaReader.process();
//
//                TextView outputView = (TextView) findViewById(outputView2);
//
//                int nrSeq = 0;
//                for (String key : sequences.keySet()) {
//
//
//                    DNASequence seq = sequences.get(key);
//                    List<SequenceView<NucleotideCompound>> list = SequenceMixin.nonOverlappingKmers(seq, 7);
//                    CompoundSet<NucleotideCompound> set = seq.getCompoundSet();
//
//                    outputView.append(nrSeq + " : " + key + "; kmerNonOverlap count: "+list.size()+"\n");
//                    for(int i=0; i<list.size(); i++){
//                        outputView.append(i+": k-mer: "+ list.get(i).getSequenceAsString()+"\n");
//                    }
//                    outputView.append("\n");
//
//                }
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        //-------------------------------------------------------------------------------------------kmerOverlap
//        if (requestCode == 131 && resultCode == RESULT_OK) {
//            Uri fileUri = data.getData(); //The uri with the location of the file
//
//            try (InputStream fileInputsteam = getContentResolver().openInputStream(fileUri)) {
//
//                FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<>(
//                        fileInputsteam,
//                        new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
//                        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet()));
//
//                LinkedHashMap<String, DNASequence> sequences = fastaReader.process();
//
//                TextView outputView = (TextView) findViewById(outputView2);
//
//                int nrSeq = 0;
//                for (String key : sequences.keySet()) {
//
//
//                    DNASequence seq = sequences.get(key);
//                    List<SequenceView<NucleotideCompound>> list = SequenceMixin.overlappingKmers(seq, 207);
//                    CompoundSet<NucleotideCompound> set = seq.getCompoundSet();
//
//                    outputView.append(nrSeq + " : " + key + "; kmerNonOverlap count: "+list.size()+"\n");
//                    for(int i=0; i<list.size(); i++){
//                        outputView.append(i+": k-mer: "+ list.get(i).getSequenceAsString()+"\n");
//                    }
//                    outputView.append("\n");
//
//                }
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        //----------------------------------------------------------------------blast_report_parser
//        if (requestCode == 132 && resultCode == RESULT_OK) {
//            Uri fileUri = data.getData(); //The uri with the location of the file
//
//            try (InputStream fileInputsteam = getContentResolver().openInputStream(fileUri)) {
//
//                BlastXMLParser instance = new BlastXMLParser();
//                instance.setStream(fileInputsteam);
//
//                List<Result> result = instance.createObjects(1e-10);
//
//                TextView outputView = (TextView) findViewById(outputView2);
//
//
//                for(Hit hit : result.iterator().next()){
//                    outputView.append(hit.getHitNum()+" : "+hit.getHitId()+" : "+hit.getHitDef()+" : "+hit.getHitAccession()+" : "+hit.getHitLen()+"\n");
//                    Log.d(LOG, hit.getHitNum()+" : "+hit.getHitId()+" : "+hit.getHitDef()+" : "+hit.getHitAccession()+" : "+hit.getHitLen()+" : evalue = "+hit.iterator().next().getHspEvalue()+"\n");
//                }
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        //----------------------------------------------------------------------genebank
//        if (requestCode == 133 && resultCode == RESULT_OK) {
//            Uri fileUri = data.getData(); //The uri with the location of the file
//
//            try (InputStream fileInputsteam = getContentResolver().openInputStream(fileUri)) {
//
//                LinkedHashMap<String, DNASequence> dnaSequences = GenbankReaderHelper.readGenbankDNASequence(fileInputsteam);
//
//                for (DNASequence sequence : dnaSequences.values()) {
//                    outputView.append("DNA Sequence: "+ sequence.getSequenceAsString()+"\n");
//                    Log.d(LOG,"DNA Sequence: "+ sequence.getSequenceAsString());
//                }
//
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        //----------------------------------------------------------------------genebank fom internet (NP_000257)
//        if (requestCode == 134 && resultCode == RESULT_OK) {
//            Uri fileUri = data.getData(); //The uri with the location of the file
//
//            try (InputStream fileInputsteam = getContentResolver().openInputStream(fileUri)) {
//
//                //Try with the GenbankProxySequenceReader
////                GenbankProxySequenceReader<AminoAcidCompound> genbankProteinReader
////                        = new GenbankProxySequenceReader<AminoAcidCompound>(System.getProperty("java.io.tmpdir"), "NP_000257", AminoAcidCompoundSet.getAminoAcidCompoundSet());
////                ProteinSequence proteinSequence = new ProteinSequence(genbankProteinReader);
////                genbankProteinReader.getHeaderParser().parseHeader(genbankProteinReader.getHeader(), proteinSequence);
////                Log.i(LOG,"Sequence("+proteinSequence.getAccession()+","+proteinSequence.getLength()+") = "+proteinSequence.getSequenceAsString().substring(0, 10)+"...");
////                outputView.append("Sequence("+proteinSequence.getAccession()+","+proteinSequence.getLength()+") = "+proteinSequence.getSequenceAsString().substring(0, 10)+"..."+"\n");
//
//                //test thread----------------------------------------------------------------
////                Thread thread = new Thread(new Runnable(){
////                    public void run() {
////                        try {
////                            GenbankProxySequenceReader<AminoAcidCompound> genbankProteinReader
////                                    = new GenbankProxySequenceReader<AminoAcidCompound>(System.getProperty("java.io.tmpdir"), "NP_000257", AminoAcidCompoundSet.getAminoAcidCompoundSet());
////                            ProteinSequence proteinSequence = new ProteinSequence(genbankProteinReader);
////                            genbankProteinReader.getHeaderParser().parseHeader(genbankProteinReader.getHeader(), proteinSequence);
////                            Log.i(LOG,"Sequence("+proteinSequence.getAccession()+","+proteinSequence.getLength()+") = "+proteinSequence.getSequenceAsString().substring(0, 10)+"...");
//////                            outputView.append("Sequence("+proteinSequence.getAccession()+","+proteinSequence.getLength()+") = "+proteinSequence.getSequenceAsString().substring(0, 10)+"..."+"\n");
////
////                        } catch (Exception e) {
////                            e.printStackTrace();
////                        }
////                    }
////                });
////
////                thread.start();
//                GenbankAsyncTask genbAsyncTask = new GenbankAsyncTask();
//                genbAsyncTask.execute("");
//                outputView.append("sequence: "+genbAsyncTask.getSequence());
//                //-----------------------------------------------------------------------------
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
    }

}
