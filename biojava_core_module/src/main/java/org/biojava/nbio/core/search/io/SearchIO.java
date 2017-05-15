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
package org.biojava.nbio.core.search.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;

/**
 * Designed by Paolo Pavan.
 * You may want to find my contacts on Github and LinkedIn for code info
 * or discuss major changes.
 * https://github.com/paolopavan
 *
 * @author Paolo Pavan
 */

public class SearchIO implements Iterable<Result>{
	static private HashMap<String,ResultFactory_V2> extensionFactoryAssociation;

	final private ResultFactory_V2 factory;
	private File file;
	private InputStream fileInputStream;

	/**
	 * this threshold applies in retrieving hsp. Those having e-value below this
	 * will not be loaded.
	 */
	private double evalueThreshold = Double.MAX_VALUE;
	/**
	 * contains one object per query sequence describing search results.
	 * Sometime also referred as Iterations.
	 */
	private List<Result> results;

	private final String NOT_SUPPORTED_FILE_EXCEPTION =
			"This extension is not associated with any parser. You can try to specify a ResultFactory object.";

	/**
	 * Build a SearchIO reader and tries to select the appropriate parser inspecting
	 * file extension.
	 *
	 * @param f
	 * @throws Exception
	 */
	public SearchIO(File f)  throws IOException, ParseException{
		factory = guessFactory(f);
		file = f;
		if (file.exists()) readResults();
	}

	/**
	 * Build a SearchIO reader and specify a ResultFactory object to be used
	 * for parsing
	 *
	 * @param f
	 * @param factory
	 *
	 * @throws IOException for file access related issues
	 * @throws ParseException for file format related issues
	 */
	public SearchIO(File f, ResultFactory_V2 factory) throws IOException, ParseException{
		file = f;
		this.factory = factory;
		if (file.exists()) readResults();
	}
	/**
	 * Build a SearchIO reader, specify a ResultFactory object to be used for parsing
	 * and filter hsp retrieved by a e-value threshold.
	 * This usually increase parsing speed
	 * @param f
	 * @param factory
	 * @param maxEvalue
	 *
	 * @throws IOException for file access related issues
	 * @throws ParseException for file format related issues
	 */
	public SearchIO(File f, ResultFactory_V2 factory, double maxEvalue) throws IOException, ParseException{
		file = f;
		this.factory = factory;
		this.evalueThreshold = maxEvalue;
		if (file.exists()) readResults();
	}

	//Todo mano pakeistos eilutes
	public SearchIO(InputStream is, ResultFactory_V2 factory, double maxEvalue) throws IOException, ParseException{
		this.fileInputStream = is;
		this.factory = factory;
		this.evalueThreshold = maxEvalue;
		readResultsStream();
	}

	//Todo mano pakeistos eilutes
	public SearchIO(InputStream is, ResultFactory_V2 factory) throws IOException, ParseException{
		this.fileInputStream = is;
		this.factory = factory;
		readResultsStream();
	}


	/**
	 * This method is declared private because it is the default action of constructor
	 * when file exists
	 *
	 * @throws IOException for file access related issues
	 * @throws ParseException for file format related issues
	 */
	private void readResults() throws IOException, ParseException {
		factory.setFile(file);
		results = factory.createObjects(evalueThreshold);
	}

	//Todo mano pakeistos eilutes
	private void readResultsStream() throws IOException, ParseException {
		factory.setStream(fileInputStream);
		results = factory.createObjects(evalueThreshold);
	}

	/**
	 * used to write a search report using the guessed or specified factory
	 *
	 * @throws IOException for file access related issues
	 * @throws ParseException for file format related issues
	 */
	public void writeResults() throws IOException, ParseException {
		factory.setFile(file);
		factory.createObjects(evalueThreshold);
	}

	/**
	 * Guess factory class to be used using file extension.
	 * It can be used both for read and for in write.
	 * To be ResultFactory classes automatically available to this subsystem
	 * they must be listed in the file org.biojava.nbio.core.search.io.ResultFactory
	 * located in src/main/resources
	 *
	 * @param f: file. Its last extension (text after last dot) will be compared
	 * to default extensions of known ResultFactory_V2 implementing classes
	 * @return the guessed factory
	 */
	private ResultFactory_V2 guessFactory(File f){
		if (extensionFactoryAssociation == null){
			extensionFactoryAssociation = new HashMap<String, ResultFactory_V2>();
			ServiceLoader<ResultFactory_V2> impl = ServiceLoader.load(ResultFactory_V2.class);
			for (ResultFactory_V2 loadedImpl : impl) {
				List<String> fileExtensions = loadedImpl.getFileExtensions();
				for (String ext: fileExtensions) extensionFactoryAssociation.put(ext, loadedImpl);
			}
		}

		String filename = f.getAbsolutePath();
		int extensionPos = filename.lastIndexOf(".");
		String extension = filename.substring(extensionPos + 1);
		if (extensionFactoryAssociation.get(extension) == null)
			throw new UnsupportedOperationException(NOT_SUPPORTED_FILE_EXCEPTION
					+ "\nExtension:"+ extension);

		return extensionFactoryAssociation.get(extension);
	}

	public double getEvalueThreshold() {
		return evalueThreshold;
	}

	@Override
	public Iterator<Result> iterator() {
		return new Iterator<Result>() {
			int currentResult = 0;
			@Override
			public boolean hasNext() {
				return currentResult < results.size();
			}

			@Override
			public Result next() {
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                return results.get(currentResult++);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("The remove operation is not supported by this iterator");
			}
		};
	}
}
