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
package org.biojava.nbio.core.sequence.loader;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import java.io.IOException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Testing example for issue #834
 *
 * @author Jacek Grzebyta
 * @author Paolo Pavan
// * @see InfoTask
 */
@RunWith(AndroidJUnit4.class)
public class SimpleGenbankProxySequenceReaderTest {


	public static final String LOG = SimpleGenbankProxySequenceReaderTest.class.getSimpleName();

	@Test(expected = IOException.class)
	public void testWrongSequence() throws Exception {
		Log.i(LOG, "test wrong sequence");

		String wrongGi = "34567";

		GenbankProxySequenceReader<AminoAcidCompound> genbankReader
				= new GenbankProxySequenceReader<AminoAcidCompound>(System.getProperty("java.io.tmpdir"),
						wrongGi,
						AminoAcidCompoundSet.getAminoAcidCompoundSet());

		ProteinSequence seq = new ProteinSequence(genbankReader);
	}
}
