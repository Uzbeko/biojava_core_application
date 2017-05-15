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
package org.biojava.nbio.core;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.TestCase;

import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.RNASequence;
import org.biojava.nbio.core.sequence.compound.AmbiguityDNACompoundSet;
import org.biojava.nbio.core.sequence.compound.AmbiguityRNACompoundSet;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.io.RNASequenceCreator;
import org.biojava.nbio.core.sequence.template.CompoundSet;
import org.biojava.nbio.core.sequence.template.Sequence;
import org.biojava.nbio.core.sequence.transcription.DNAToRNATranslator;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * A Test case for https://github.com/biojava/biojava/issues/344
 *
 * Created by andreas on 12/4/15.
 */
@RunWith(AndroidJUnit4.class)
public class TestAmbiguityCompoundSet extends TestCase{

	@Test
	public void testCompountSet(){
		try {

			CompoundSet<NucleotideCompound> dnaSet = AmbiguityDNACompoundSet.getDNACompoundSet();
			CompoundSet<NucleotideCompound> rnaSet = AmbiguityRNACompoundSet.getRNACompoundSet();

			DNASequence dna=new DNASequence("AGTCS", dnaSet);

			assertEquals("AGTCS",dna.toString());

			RNASequence rna = dna.getRNASequence();

			rna = new RNASequence(dna.getSequenceAsString().replaceAll("T", "U"), AmbiguityRNACompoundSet.getRNACompoundSet()); //fails with missing compound S

			assertEquals("AGUCS",rna.toString());

			/** now, do the translation also using the underlying API (should not be needed for a user)
			 *
			 */
			DNAToRNATranslator translator = new DNAToRNATranslator(new RNASequenceCreator(rnaSet
					),dnaSet,rnaSet,false);

			Sequence<NucleotideCompound> translated = translator.createSequence(dna);

			assertEquals("AGUCS", translated.toString());

		} catch (CompoundNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
