/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 */
/**
 *
 */
package org.biojava.nbio.core.sequence.io;


import android.support.test.runner.AndroidJUnit4;

import junit.framework.TestCase;

import org.biojava.MyApplication;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.io.GenbankWriterHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;


/**
 * @author mckeee1
 *
 */
@RunWith(AndroidJUnit4.class)
public class GenbankWriterTest extends TestCase{


	@Test
	public void testProcess() throws Exception {

//		InputStream inStream = GenbankWriterTest.class.getResourceAsStream("/NM_000266.gb"); originalus varintas

		//-----------------------------Edvino pakeista
		InputStream inStream = null;
		try {
			inStream = MyApplication.getAppContext().getAssets().open("NM_000266.gb");
		} catch (IOException e) { //Todo reiktu padoroti exeptionus
			e.printStackTrace();
		}
		//-----------------------End


		//File dnaFile = new File("src/test/resources/NM_000266.gb");
		LinkedHashMap<String, DNASequence> dnaSequences = GenbankReaderHelper.readGenbankDNASequence( inStream );
		ByteArrayOutputStream fragwriter = new ByteArrayOutputStream();
		ArrayList<DNASequence> seqs = new ArrayList<DNASequence>();
		for(DNASequence seq : dnaSequences.values()) {
			seqs.add(seq);
		}
		GenbankWriterHelper.writeNucleotideSequence(fragwriter, seqs,
				GenbankWriterHelper.LINEAR_DNA);
		//System.out.println(fragwriter.toString());
		ByteArrayInputStream fragreader = new ByteArrayInputStream(fragwriter.toByteArray());
		/**
		 * Hello Jacek
		 * can you please investigate why this test fails? it seems that
		 * fragreader at the line below is read with the last feature
		 * in an invalid state: location = 2005..2004
		 */
		//dnaSequences = GenbankReaderHelper.readGenbankDNASequence( fragreader );
		fragwriter.close();
		assertEquals(seqs.get(0).getSequenceAsString(),dnaSequences.values().iterator().next().getSequenceAsString());
	}
}
