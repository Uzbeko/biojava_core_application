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
package org.biojava.nbio.core.sequence.io;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.compound.DNACompoundSet;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.io.DNASequenceCreator;
import org.biojava.nbio.core.sequence.io.GenbankReader;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.io.GenericGenbankHeaderParser;
import org.biojava.nbio.core.sequence.io.ProteinSequenceCreator;
import org.biojava.nbio.core.sequence.io.util.ClasspathResource;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;
import org.junit.*;
import org.junit.runner.RunWith;

import java.util.LinkedHashMap;

/**
 *
 * @author Scooter Willis <willishf at gmail dot com>
 */
@RunWith(AndroidJUnit4.class)
public class GenbankCookbookTest {

	public static final String LOG = GenbankCookbookTest.class.getSimpleName();

	public GenbankCookbookTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of process method, of class GenbankReader.
	 */
	@Test
	public void testProcess() throws Throwable {
		/*
		 * Method 1: With the GenbankProxySequenceReader
		 */
		//Try with the GenbankProxySequenceReader
		GenbankProxySequenceReader<AminoAcidCompound> genbankProteinReader
				= new GenbankProxySequenceReader<AminoAcidCompound>(System.getProperty("java.io.tmpdir"), "NP_000257", AminoAcidCompoundSet.getAminoAcidCompoundSet());
		ProteinSequence proteinSequence = new ProteinSequence(genbankProteinReader);
		genbankProteinReader.getHeaderParser().parseHeader(genbankProteinReader.getHeader(), proteinSequence);
		Log.i(LOG,"Sequence("+proteinSequence.getAccession()+","+proteinSequence.getLength()+") = "+proteinSequence.getSequenceAsString().substring(0, 10)+"...");

	GenbankProxySequenceReader<NucleotideCompound> genbankDNAReader
	= new GenbankProxySequenceReader<NucleotideCompound>(System.getProperty("java.io.tmpdir"), "NM_001126", DNACompoundSet.getDNACompoundSet());
	DNASequence dnaSequence = new DNASequence(genbankDNAReader);
	genbankDNAReader.getHeaderParser().parseHeader(genbankDNAReader.getHeader(), dnaSequence);
	Log.i(LOG,"Sequence("+dnaSequence.getAccession()+","+dnaSequence.getLength()+") = "+dnaSequence.getSequenceAsString().substring(0, 10)+"...");
		/*
		 * Method 2: With the GenbankReaderHelper
		 */
		//Try with the GenbankReaderHelper
		ClasspathResource dnaResource = new ClasspathResource("NM_000266.gb", true);
		//File dnaFile = new File("src/test/resources/NM_000266.gb");
		//File protFile = new File("src/test/resources/BondFeature.gb");
		ClasspathResource protResource = new ClasspathResource("BondFeature.gb");

		LinkedHashMap<String, DNASequence> dnaSequences = GenbankReaderHelper.readGenbankDNASequence(dnaResource.getInputStream());
		for (DNASequence sequence : dnaSequences.values()) {
			Log.d(LOG,"DNA Sequence: "+ sequence.getSequenceAsString());
		}

		LinkedHashMap<String, ProteinSequence> protSequences = GenbankReaderHelper.readGenbankProteinSequence(protResource.getInputStream());
		for (ProteinSequence sequence : protSequences.values()) {
			Log.d(LOG,"Protein Sequence: "+ sequence.getSequenceAsString());
		}
		/*
		 * Method 3: With the GenbankReader Object
		 */
		//Try reading with the GanbankReader

		GenbankReader<DNASequence, NucleotideCompound> dnaReader = new GenbankReader<DNASequence, NucleotideCompound>(
				dnaResource.getInputStream(),
				new GenericGenbankHeaderParser<DNASequence, NucleotideCompound>(),
				new DNASequenceCreator(DNACompoundSet.getDNACompoundSet())
		);
		dnaSequences = dnaReader.process();

		Log.d(LOG,"DNA Sequence: "+ dnaSequences);


		GenbankReader<ProteinSequence, AminoAcidCompound> protReader = new GenbankReader<ProteinSequence, AminoAcidCompound>(
				protResource.getInputStream(),
				new GenericGenbankHeaderParser<ProteinSequence, AminoAcidCompound>(),
				new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet())
		);
		protSequences = protReader.process();

		Log.d(LOG,"Protein Sequence: "+ protSequences);

	}

}
