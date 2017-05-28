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
package org.biojava.nbio.core.alignment.matrices;

import android.util.Log;

import org.biojava.MyApplication;
import org.biojava.nbio.core.alignment.template.SubstitutionMatrix;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/** The default provider for AAINDEX loads substitution matrices from the AAINDEX file in the resources directory
 *
 * @author Andreas Prlic
 *
 */
public class DefaultAAIndexProvider implements AAIndexProvider {

    public static final String LOG = DefaultAAIndexProvider.class.getSimpleName();

	Map<String,SubstitutionMatrix<AminoAcidCompound>> matrices;

	public DefaultAAIndexProvider(){


		InputStream inStream = getInputStreamToAAindexFile();

		AAIndexFileParser parser = new AAIndexFileParser();

		try {
			parser.parse(inStream);
		} catch (IOException e){
		}

		matrices = parser.getMatrices();

	}

	@Override
	public SubstitutionMatrix<AminoAcidCompound> getMatrix(String matrixName) {

		return matrices.get(matrixName);

	}

	public InputStream getInputStreamToAAindexFile(){

		InputStream isReader = null;
		try {
			InputStream is = MyApplication.getAppContext().getAssets().open("matrices/AAINDEX.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return isReader;

	}

}
