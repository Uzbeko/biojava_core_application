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
package org.biojava.nbio.core.search.io.blast;


import android.util.Log;

import org.biojava.nbio.core.search.io.Hit;
import org.biojava.nbio.core.search.io.Hsp;
import org.biojava.nbio.core.search.io.Result;
import org.biojava.nbio.core.search.io.ResultFactory_V2;
import org.biojava.nbio.core.sequence.template.Sequence;
import org.biojava.nbio.core.util.XMLHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathException;


/**
 * Re-designed by Paolo Pavan on the footprint of:
 * org.biojava.nbio.genome.query.BlastXMLQuery by Scooter Willis <willishf at gmail dot com>
 *
 * You may want to find my contacts on Github and LinkedIn for code info
 * or discuss major changes.
 * https://github.com/paolopavan
 *
 *
 * @author Paolo Pavan
 */
public class BlastXMLParser implements ResultFactory_V2 {
public static final String LOG = BlastXMLParser.class.getSimpleName();

	Document blastDoc = null;
	private File targetFile;
	private InputStream fileInputStream;
	private List<Sequence> queryReferences, databaseReferences;
	private Map<String,Sequence> queryReferencesMap, databaseReferencesMap;

	public BlastXMLParser() {

	}
	@Override
	public void setFile(File f){
		targetFile = f;
	}

	@Override
	public void setStream(InputStream is) {
		fileInputStream = is;
	}


	private void readFile(String blastFile) throws IOException, ParseException{
		Log.i(LOG,"Start reading " + blastFile);
		try {
			blastDoc = XMLHelper.loadXML(blastFile);
		} catch (SAXException ex) {
			Log.e(LOG,"A parsing error has occurred while reading XML blast file");
			throw new ParseException(ex.getMessage(),0);
		} catch (ParserConfigurationException ex) {
			Log.e(LOG,"Internal XML parser non properly configured");
			throw new ParseException(ex.getMessage(),0);
		}
		Log.i(LOG,"Read finished");
	}

	private void readStream() throws IOException, ParseException{
		Log.i(LOG,"Start reading fileInputStream");
		try {
			blastDoc = XMLHelper.inputStreamToDocument(fileInputStream);
		} catch (SAXException ex) {
			Log.e(LOG,"A parsing error has occurred while reading XML blast file");
			throw new ParseException(ex.getMessage(),0);
		} catch (ParserConfigurationException ex) {
			Log.e(LOG,"Internal XML parser non properly configured");
			throw new ParseException(ex.getMessage(),0);
		}
		Log.i(LOG,"Read finished");
	}

	@Override
	public List<Result> createObjects(double maxEScore) throws IOException, ParseException {

		if (targetFile == null && fileInputStream == null) throw new IllegalStateException("File to be parsed not specified.");

		if(targetFile == null){
			readStream();
		}else {
		// getAbsolutePath throws SecurityException
			readFile(targetFile.getAbsolutePath());
		}

		// create mappings between sequences and blast id
		mapIds();

		ArrayList<Result> resultsCollection;
		ArrayList<Hit> hitsCollection;
		ArrayList<Hsp> hspsCollection;

		try {
			// select top level elements
			String program = XMLHelper.selectSingleElement(blastDoc.getDocumentElement(),"BlastOutput_program").getTextContent();
			String version = XMLHelper.selectSingleElement(blastDoc.getDocumentElement(),"BlastOutput_version").getTextContent();
			String reference = XMLHelper.selectSingleElement(blastDoc.getDocumentElement(),"BlastOutput_reference").getTextContent();
			String dbFile = XMLHelper.selectSingleElement(blastDoc.getDocumentElement(),"BlastOutput_db").getTextContent();

			Log.i(LOG,"Query for hits in "+ targetFile);
			ArrayList<Element> IterationsList = XMLHelper.selectElements(blastDoc.getDocumentElement(), "BlastOutput_iterations/Iteration[Iteration_hits]");
			Log.i(LOG,IterationsList.size() + " results");

			resultsCollection = new ArrayList<Result>();


			BlastResultBuilder resultBuilder;
			Element iterationHitsElement;
			ArrayList<Element> hitList;
			BlastHitBuilder blastHitBuilder;
			Element hithspsElement;
			ArrayList<Element> hspList;
			Double evalue;
			BlastHspBuilder blastHspBuilder;

			for (Element element : IterationsList) {
				resultBuilder = new BlastResultBuilder();
				// will add BlastOutput* key sections in the result object
				resultBuilder
					.setProgram(program)
					.setVersion(version)
					.setReference(reference)
					.setDbFile(dbFile);

				// Iteration* section keys:
				resultBuilder
					.setIterationNumber(new Integer(XMLHelper.selectSingleElement(element,"Iteration_iter-num").getTextContent()))
					.setQueryID(XMLHelper.selectSingleElement(element,"Iteration_query-ID").getTextContent())
					.setQueryDef(XMLHelper.selectSingleElement(element, "Iteration_query-def").getTextContent())
					.setQueryLength(new Integer(XMLHelper.selectSingleElement(element,"Iteration_query-len").getTextContent()));

				if (queryReferences != null) resultBuilder.setQuerySequence(queryReferencesMap.get(
						XMLHelper.selectSingleElement(element,"Iteration_query-ID").getTextContent()
				));



				iterationHitsElement = XMLHelper.selectSingleElement(element, "Iteration_hits");
				hitList = XMLHelper.selectElements(iterationHitsElement, "Hit");

				hitsCollection = new ArrayList<Hit>();
				for (Element hitElement : hitList) {
					blastHitBuilder = new BlastHitBuilder();
					blastHitBuilder
						.setHitNum(new Integer(XMLHelper.selectSingleElement(hitElement, "Hit_num").getTextContent()))
						.setHitId(XMLHelper.selectSingleElement(hitElement, "Hit_id").getTextContent())
						.setHitDef(XMLHelper.selectSingleElement(hitElement, "Hit_def").getTextContent())
						.setHitAccession(XMLHelper.selectSingleElement(hitElement, "Hit_accession").getTextContent())
						.setHitLen(new Integer(XMLHelper.selectSingleElement(hitElement, "Hit_len").getTextContent()));

					if (databaseReferences != null) blastHitBuilder.setHitSequence(databaseReferencesMap.get(
						XMLHelper.selectSingleElement(hitElement, "Hit_id").getTextContent()
					));

					hithspsElement = XMLHelper.selectSingleElement(hitElement, "Hit_hsps");
					hspList = XMLHelper.selectElements(hithspsElement, "Hsp");

					hspsCollection = new ArrayList<Hsp>();
					for (Element hspElement : hspList) {
						evalue = new Double(XMLHelper.selectSingleElement(hspElement, "Hsp_evalue").getTextContent());

						// add the new hsp only if it pass the specified threshold. It can save lot of memory and some parsing time
						if (evalue <= maxEScore) {
							blastHspBuilder = new BlastHspBuilder();
							blastHspBuilder
								.setHspNum(new Integer(XMLHelper.selectSingleElement(hspElement, "Hsp_num").getTextContent()))
								.setHspBitScore(new Double(XMLHelper.selectSingleElement(hspElement, "Hsp_bit-score").getTextContent()))
								.setHspScore(new Integer(XMLHelper.selectSingleElement(hspElement, "Hsp_score").getTextContent()))
								.setHspEvalue(evalue)
								.setHspQueryFrom(new Integer(XMLHelper.selectSingleElement(hspElement, "Hsp_query-from").getTextContent()))
								.setHspQueryTo(new Integer(XMLHelper.selectSingleElement(hspElement, "Hsp_query-to").getTextContent()))
								.setHspHitFrom(new Integer(XMLHelper.selectSingleElement(hspElement, "Hsp_hit-from").getTextContent()))
								.setHspHitTo(new Integer(XMLHelper.selectSingleElement(hspElement, "Hsp_hit-to").getTextContent()))
								.setHspQueryFrame(new Integer(XMLHelper.selectSingleElement(hspElement, "Hsp_query-frame").getTextContent()))
								.setHspHitFrame(new Integer(XMLHelper.selectSingleElement(hspElement, "Hsp_hit-frame").getTextContent()))
								.setHspIdentity(new Integer(XMLHelper.selectSingleElement(hspElement, "Hsp_identity").getTextContent()))
								.setHspPositive(new Integer(XMLHelper.selectSingleElement(hspElement, "Hsp_positive").getTextContent()))
								.setHspGaps(new Integer(XMLHelper.selectSingleElement(hspElement, "Hsp_gaps").getTextContent()))
								.setHspAlignLen(new Integer(XMLHelper.selectSingleElement(hspElement, "Hsp_align-len").getTextContent()))
								.setHspQseq(XMLHelper.selectSingleElement(hspElement, "Hsp_qseq").getTextContent())
								.setHspHseq(XMLHelper.selectSingleElement(hspElement, "Hsp_hseq").getTextContent())
								.setHspIdentityString(XMLHelper.selectSingleElement(hspElement, "Hsp_midline").getTextContent());

							hspsCollection.add(blastHspBuilder.createBlastHsp());
						}
					}
					// finally set the computed hsp collection and create Hit object
					blastHitBuilder.setHsps(hspsCollection);
					hitsCollection.add(blastHitBuilder.createBlastHit());
				}
				// finally set the computed Hit collection to the result
				resultBuilder.setHits(hitsCollection);
				resultsCollection.add(resultBuilder.createBlastResult());
			}
		} catch (XPathException e) {
			throw new ParseException(e.getMessage(),0);
		}
		Log.i(LOG,"Parsing of "+targetFile+" finished.");

		return resultsCollection;
	}

	@Override
	public List<String> getFileExtensions(){
		ArrayList<String> extensions = new ArrayList<String>(1);
		extensions.add("blastxml");
		return extensions;
	}

	@Override
	public void setQueryReferences(List<Sequence> sequences) {
		queryReferences = sequences;
	}

	@Override
	public void setDatabaseReferences(List<Sequence> sequences) {
		databaseReferences = sequences;
	}

	/**
	 * fill the map association between sequences an a unique id
	 */
	private void mapIds() {
		if (queryReferences != null) {
			queryReferencesMap = new HashMap<String,Sequence>(queryReferences.size());
			for (int counter=0; counter < queryReferences.size() ; counter ++){
				String id = "Query_"+(counter+1);
				queryReferencesMap.put(id, queryReferences.get(counter));
			}
		}

		if (databaseReferences != null) {
			databaseReferencesMap = new HashMap<String,Sequence>(databaseReferences.size());
			for (int counter=0; counter < databaseReferences.size() ; counter ++){
				// this is strange: while Query_id are 1 based, Hit (database) id are 0 based
				String id = "gnl|BL_ORD_ID|"+(counter);
				databaseReferencesMap.put(id, databaseReferences.get(counter));
			}
		}
	}

	@Override
	public void storeObjects(List<Result> results) throws IOException, ParseException {
		throw new UnsupportedOperationException("This parser does not support writing yet.");
	}
}
