package org.biojava.coreapp;

import android.content.Intent;
import android.graphics.Typeface;
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
import org.biojava.bl.GenbankResponse;
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
    private ArrayList<String> myData=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//---------button list---------------------------------------------------------
        buttonList = (ListView) findViewById(R.id.Buttons);

        myData.add("1. Translate");
        myData.add("2. to_RNA");
        myData.add("3. ParseFasta");
        myData.add("4. inversija");
        myData.add("5. komplementari");
        myData.add("6. reversija");
        myData.add("7. posekis");
        myData.add("8. GC count");
        myData.add("9. AT count");
        myData.add("10. composition");
        myData.add("11. kmerNonOverlap");
        myData.add("12. kmerOverlap");
        myData.add("13. blast_report_paerser");
        myData.add("14. genebank");
        myData.add("15. genebank fom internet");
        myData.add("16. needlemanWunsch(global)");

        ListAdapter adapter = new ListAdapter(this,myData);
        buttonList.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

    //-----------------------------------------------------------------------------------allCases
        if (resultCode == RESULT_OK) {
            Uri fileUri = data.getData(); //The uri with the location of the file
            Controller control = new Controller(this);
            String result = control.swichCase(requestCode, fileUri);
            TextView outputView = (TextView) findViewById(outputView2);
            outputView.setTypeface(Typeface.MONOSPACE);                 //visos raides vienodo ilgio
            outputView.setText(result);
        }

    }

    public void setTextView(String result){
        TextView outputView = (TextView) findViewById(outputView2);
        outputView.setText(result);
    }


}
