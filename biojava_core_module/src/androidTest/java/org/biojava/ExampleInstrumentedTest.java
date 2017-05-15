//package org.biojava;
//
//import android.content.Context;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.runner.AndroidJUnit4;
//
//import org.biojava.nbio.core.search.io.Hit;
//import org.biojava.nbio.core.search.io.Hsp;
//import org.biojava.nbio.core.search.io.Result;
//import org.biojava.nbio.core.search.io.ResultFactory;
//import org.biojava.nbio.core.search.io.ResultFactory_V2;
//import org.biojava.nbio.core.search.io.SearchIO;
//import org.biojava.nbio.core.search.io.SearchIO;
//import org.biojava.nbio.core.search.io.blast.BlastHit;
//import org.biojava.nbio.core.search.io.blast.BlastHitBuilder;
//import org.biojava.nbio.core.search.io.blast.BlastHsp;
//import org.biojava.nbio.core.search.io.blast.BlastHspBuilder;
//import org.biojava.nbio.core.search.io.blast.BlastResult;
//import org.biojava.nbio.core.search.io.blast.BlastResultBuilder;
//import org.biojava.nbio.core.search.io.blast.BlastTabularParser;
//import org.biojava.nbio.core.search.io.blast.BlastXMLParser;
//import org.biojava.nbio.core.util.XMLHelper;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.w3c.dom.Document;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.*;
//
///**
// * Instrumentation test, which will execute on an Android device.
// *
// * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
// */
//@RunWith(AndroidJUnit4.class)
//public class ExampleInstrumentedTest {
//
//    @Test
//    public void useAppContext() throws Exception {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//
//        assertEquals("org.biojava", appContext.getPackageName());
//    }
//
//    @Test
//    public void parseTest() throws Exception{
//
//        InputStream classpathIs = null;
//        try {
//            classpathIs = MyApplication.getAppContext().getAssets().open("org/biojava/nbio/core/search/io/blast/small-blastreport.blastxml");
//        } catch (IOException e) { //Todo reiktu padoroti exeptionus
//            e.printStackTrace();
//        }
//
//        BlastXMLParser instance = new BlastXMLParser();
//        instance.setStraem(classpathIs);
//
//        //instance.setQueryReferences(null);
//        //instance.setDatabaseReferences(null);
//        List<Result> result = instance.createObjects(1e-10);
//
//        // test with random manual selected results
//        BlastHsp hsp1hit1res1 = new BlastHspBuilder()
//                .setHspNum(1)
//                .setHspBitScore(2894.82)
//                .setHspScore(1567)
//                .setHspEvalue(0)
//                .setHspQueryFrom(1)
//                .setHspQueryTo(1567)
//                .setHspHitFrom(616309)
//                .setHspHitTo(617875)
//                .setHspQueryFrame(1)
//                .setHspHitFrame(1)
//                .setHspIdentity(1567)
//                .setHspPositive(1567)
//                .setHspGaps(0)
//                .setHspAlignLen(1567)
//                .setHspQseq("TTAAATTGAGAGTTTGATCCTGGCTCAGGATGAACGCTGGTGGCGTGCCTAATACATGCAAGTCGTACGCTAGCCGCTGAATTGATCCTTCGGGTGAAGTGAGGCAATGACTAGAGTGGCGAACTGGTGAGTAACACGTAAGAAACCTGCCCTTTAGTGGGGGATAACATTTGGAAACAGATGCTAATACCGCGTAACAACAAATCACACATGTGATCTGTTTGAAAGGTCCTTTTGGATCGCTAGAGGATGGTCTTGCGGCGTATTAGCTTGTTGGTAGGGTAGAAGCCTACCAAGGCAATGATGCGTAGCCGAGTTGAGAGACTGGCCGGCCACATTGGGACTGAGACACTGCCCAAACTCCTACGGGAGGCTGCAGTAGGGAATTTTCCGCAATGCACGAAAGTGTGACGGAGCGACGCCGCGTGTGTGATGAAGGCTTTCGGGTCGTAAAGCACTGTTGTAAGGGAAGAATAACTGAATTCAGAGAAAGTTTTCAGCTTGACGGTACCTTACCAGAAAGGGATGGCTAAATACGTGCCAGCAGCCGCGGTAATACGTATGTCCCGAGCGTTATCCGGATTTATTGGGCGTAAAGCGAGCGCAGACGGTTTATTAAGTCTGATGTGAAATCCCGAGGCCCAACCTCGGAACTGCATTGGAAACTGATTTACTTGAGTGCGATAGAGGCAAGTGGAACTCCATGTGTAGCGGTGAAATGCGTAGATATGTGGAAGAACACCAGTGGCGAAAGCGGCTTGCTAGATCGTAACTGACGTTGAGGCTCGAAAGTATGGGTAGCAAACGGGATTAGATACCCCGGTAGTCCATACCGTAAACGATGGGTGCTAGTTGTTAAGAGGTTTCCGCCTCCTAGTGACGTAGCAAACGCATTAAGCACCCCGCCTGAGGAGTACGGCCGCAAGGCTAAAACTTAAAGGAATTGACGGGGACCCGCACAAGCGGTGGAGCATGTGGTTTAATTCGAAGATACGCGAAAAACCTTACCAGGTCTTGACATACCAATGATCGCTTTTGTAATGAAAGCTTTTCTTCGGAACATTGGATACAGGTGGTGCATGGTCGTCGTCAGCTCGTGTCGTGAGATGTTGGGTTAAGTCCCGCAACGAGCGCAACCCTTGTTATTAGTTGCCAGCATTTAGTTGGGCACTCTAATGAGACTGCCGGTGATAAACCGGAGGAAGGTGGGGACGACGTCAGATCATCATGCCCCTTATGACCTGGGCAACACACGTGCTACAATGGGAAGTACAACGAGTCGCAAACCGGCGACGGTAAGCTAATCTCTTAAAACTTCTCTCAGTTCGGACTGGAGTCTGCAACTCGACTCCACGAAGGCGGAATCGCTAGTAATCGCGAATCAGCATGTCGCGGTGAATACGTTCCCGGGTCTTGTACACACCGCCCGTCAAATCATGGGAGTCGGAAGTACCCAAAGTCGCTTGGCTAACTTTTAGAGGCCGGTGCCTAAGGTAAAATCGATGACTGGGATTAAGTCGTAACAAGGTAGCCGTAGGAGAACCTGCGGCTGGATCACCTCCTTTCT")
//                .setHspHseq("TTAAATTGAGAGTTTGATCCTGGCTCAGGATGAACGCTGGTGGCGTGCCTAATACATGCAAGTCGTACGCTAGCCGCTGAATTGATCCTTCGGGTGAAGTGAGGCAATGACTAGAGTGGCGAACTGGTGAGTAACACGTAAGAAACCTGCCCTTTAGTGGGGGATAACATTTGGAAACAGATGCTAATACCGCGTAACAACAAATCACACATGTGATCTGTTTGAAAGGTCCTTTTGGATCGCTAGAGGATGGTCTTGCGGCGTATTAGCTTGTTGGTAGGGTAGAAGCCTACCAAGGCAATGATGCGTAGCCGAGTTGAGAGACTGGCCGGCCACATTGGGACTGAGACACTGCCCAAACTCCTACGGGAGGCTGCAGTAGGGAATTTTCCGCAATGCACGAAAGTGTGACGGAGCGACGCCGCGTGTGTGATGAAGGCTTTCGGGTCGTAAAGCACTGTTGTAAGGGAAGAATAACTGAATTCAGAGAAAGTTTTCAGCTTGACGGTACCTTACCAGAAAGGGATGGCTAAATACGTGCCAGCAGCCGCGGTAATACGTATGTCCCGAGCGTTATCCGGATTTATTGGGCGTAAAGCGAGCGCAGACGGTTTATTAAGTCTGATGTGAAATCCCGAGGCCCAACCTCGGAACTGCATTGGAAACTGATTTACTTGAGTGCGATAGAGGCAAGTGGAACTCCATGTGTAGCGGTGAAATGCGTAGATATGTGGAAGAACACCAGTGGCGAAAGCGGCTTGCTAGATCGTAACTGACGTTGAGGCTCGAAAGTATGGGTAGCAAACGGGATTAGATACCCCGGTAGTCCATACCGTAAACGATGGGTGCTAGTTGTTAAGAGGTTTCCGCCTCCTAGTGACGTAGCAAACGCATTAAGCACCCCGCCTGAGGAGTACGGCCGCAAGGCTAAAACTTAAAGGAATTGACGGGGACCCGCACAAGCGGTGGAGCATGTGGTTTAATTCGAAGATACGCGAAAAACCTTACCAGGTCTTGACATACCAATGATCGCTTTTGTAATGAAAGCTTTTCTTCGGAACATTGGATACAGGTGGTGCATGGTCGTCGTCAGCTCGTGTCGTGAGATGTTGGGTTAAGTCCCGCAACGAGCGCAACCCTTGTTATTAGTTGCCAGCATTTAGTTGGGCACTCTAATGAGACTGCCGGTGATAAACCGGAGGAAGGTGGGGACGACGTCAGATCATCATGCCCCTTATGACCTGGGCAACACACGTGCTACAATGGGAAGTACAACGAGTCGCAAACCGGCGACGGTAAGCTAATCTCTTAAAACTTCTCTCAGTTCGGACTGGAGTCTGCAACTCGACTCCACGAAGGCGGAATCGCTAGTAATCGCGAATCAGCATGTCGCGGTGAATACGTTCCCGGGTCTTGTACACACCGCCCGTCAAATCATGGGAGTCGGAAGTACCCAAAGTCGCTTGGCTAACTTTTAGAGGCCGGTGCCTAAGGTAAAATCGATGACTGGGATTAAGTCGTAACAAGGTAGCCGTAGGAGAACCTGCGGCTGGATCACCTCCTTTCT")
//                .setHspIdentityString("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||")
//                .createBlastHsp();
//        List<Hsp> hsplist = new ArrayList<Hsp>();
//        hsplist.add(hsp1hit1res1);
//        hsplist.add(hsp1hit1res1);
//
//        BlastHit hit1res1 = new BlastHitBuilder()
//                .setHitNum(1)
//                .setHitId("gnl|BL_ORD_ID|2006")
//                .setHitDef("CP000411 Oenococcus oeni PSU-1, complete genome")
//                .setHitAccession("0")
//                .setHitLen(1780517)
//                .setHsps(hsplist)
//                .createBlastHit();
//
//        List<Hit> hitlist = new ArrayList<Hit>();
//        hitlist.add(hit1res1);
//
//        BlastResult res1 = new BlastResultBuilder()
//                .setProgram("blastn")
//                .setVersion("BLASTN 2.2.29+")
//                .setReference("Zheng Zhang, Scott Schwartz, Lukas Wagner, and Webb Miller (2000), &quot;A greedy algorithm for aligning DNA sequences&quot;, J Comput Biol 2000; 7(1-2):203-14.")
//                .setQueryID("Query_1")
//                .setQueryDef("CP000411_-_16S_rRNA Oenococcus oeni PSU-1, complete genome")
//                .setQueryLength(1567)
//                .createBlastResult();
//
//        Result expRes1 = result.get(0);
//        Hit expHit1res1 = expRes1.iterator().next();
//        Hsp expHsp1hit1res1 = expHit1res1.iterator().next();
//
//        // result not testable without all hits and hsp
//        //assertEquals(expRes1, res1);
//
//        // hit test
//        assertEquals(expHit1res1, hit1res1);
//
//        // hsp test
//        assertEquals(expHsp1hit1res1, hsp1hit1res1);
//    }
//
//    @Test
//    public void DOMtest() throws Exception{
//
//        InputStream classpathIs = null;
//        try {
//            classpathIs = MyApplication.getAppContext().getAssets().open("org/biojava/nbio/core/search/io/blast/small-blastreport.blastxml");
//        } catch (IOException e) { //Todo reiktu padoroti exeptionus
//            e.printStackTrace();
//        }
//
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        DocumentBuilder db = dbf.newDocumentBuilder();
//        Document doc = db.parse(classpathIs);
//        doc.getDocumentElement().normalize();
//
//        Document blastDoc = doc;
//
//
//        String program = XMLHelper.selectSingleElement(blastDoc.getDocumentElement(),"BlastOutput_program").getTextContent();
//        String version = XMLHelper.selectSingleElement(blastDoc.getDocumentElement(),"BlastOutput_version").getTextContent();
//        String reference = XMLHelper.selectSingleElement(blastDoc.getDocumentElement(),"BlastOutput_reference").getTextContent();
//        String dbFile = XMLHelper.selectSingleElement(blastDoc.getDocumentElement(),"BlastOutput_db").getTextContent();
//
//
//    }
//
//    @Test
//    public void BlastTabularParser(){
//        System.out.println("createObjects");
//        Result expRes1;
//        Hit expHit1res1;
//        Hsp expHsp1hit1res1;
//
//        BlastTabularParser instance = new BlastTabularParser();
//        instance.setFileUrl("org/biojava/nbio/core/search/io/blast/small-blastreport.blasttxt");
//
//
//        List<Result> results = null;
//        try {
//            results = instance.createObjects(1e-10);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        BlastHsp hsp1Hit1Res1 = new BlastHspBuilder()
//                .setHspNum(1)
//                .setPercentageIdentity(100.0/100)
//                .setHspAlignLen(1567)
//                .setMismatchCount(0)
//                .setHspGaps(0)
//                .setHspQueryFrom(1)
//                .setHspQueryTo(1567)
//                .setHspHitFrom(616309)
//                .setHspQueryTo(617875)
//                .setHspEvalue(0)
//                .setHspBitScore(2894)
//                .createBlastHsp();
//
//        BlastHsp hsp1Hit1Res2 = new BlastHspBuilder()
//                .setHspNum(1)
//                .setPercentageIdentity(100.0/100)
//                .setHspAlignLen(1567)
//                .setMismatchCount(0)
//                .setHspGaps(0)
//                .setHspQueryFrom(1)
//                .setHspQueryTo(1567)
//                .setHspHitFrom(1278699)
//                .setHspQueryTo(1277133)
//                .setHspEvalue(0)
//                .setHspBitScore(2894)
//                .createBlastHsp();
//
//        List<Hsp> hsplist = new ArrayList<Hsp>();
//        hsplist.add(hsp1Hit1Res1);
//        hsplist.add(hsp1Hit1Res2);
//
//        BlastHit hit1Res1 = new BlastHitBuilder()
//                .setHitDef("CP000411")
//                .setHsps(hsplist)
//                .createBlastHit();
//        List<Hit> hitlist = new ArrayList<Hit>();
//        hitlist.add(hit1Res1);
//
//        BlastResult res1 = new BlastResultBuilder()
//                .setQueryID("CP000411_-_16S_rRNA")
//                .setQueryDef("CP000411_-_16S_rRNA")
//                .setHits(hitlist)
//                .createBlastResult();
//
//        expRes1 = results.get(0);
//        expHit1res1 = expRes1.iterator().next();
//        expHsp1hit1res1 = expHit1res1.iterator().next();
//
//        // results test
//        assertEquals(expRes1, res1);
//        // hit test
//        assertEquals(expHit1res1, hit1Res1);
//        // hsp test
//        assertEquals(expHsp1hit1res1, hsp1Hit1Res1);
//
//
////        String resource2 = "/org/biojava/nbio/core/search/io/blast/testBlastTabularReport.txt";
////        File file2 = getFileForResource(resource2);
////

//        BlastTabularParser instance2 = new BlastTabularParser();
//        instance2.setFileUrl("org/biojava/nbio/core/search/io/blast/testBlastTabularReport.txt");
//
//        List<Result> results2 = null;
//        try {
//            results2 = instance2.createObjects(1e-10);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        expRes1 = results2.get(0);
//        expHit1res1 = expRes1.iterator().next();
//        expHsp1hit1res1 = expHit1res1.iterator().next();
//
//        hsp1Hit1Res1 = new BlastHspBuilder()
//                .setPercentageIdentity(100.00/100)
//                .setHspAlignLen(48)
//                .setMismatchCount(0)
//                .setHspGaps(0)
//                .setHspQueryFrom(1)
//                .setHspQueryTo(48)
//                .setHspHitFrom(344)
//                .setHspHitTo(391)
//                .setHspEvalue(4e-19)
//                .setHspBitScore(95.6)
//                .createBlastHsp();
//
//        // results test
//        assertEquals(expRes1.getQueryID(), "1_759_906_F3");
//        assertEquals(results2.size(), 298);
//        // only one hsp test
//        assertEquals(expHsp1hit1res1, hsp1Hit1Res1);
//    }
//
//    @Test
//    public void SearchIoTest(){
//
//        String resource = "org/biojava/nbio/core/search/io/blast/testBlastReport.blastxml";
//
//        InputStream is = null;
//        try {
//            is = MyApplication.getAppContext().getAssets().open(resource);
//        } catch (IOException e) {                                                //Todo reiktu apdoroti exeptionus
//            System.err.println("An IOException was caught :"+e.getMessage());
//            e.printStackTrace();
//        }
//
//        ResultFactory_V2 blastResultFactory = new BlastXMLParser();
//        final SearchIO instance;
//        try {
//            instance = new SearchIO(is, blastResultFactory);
//        } catch (Exception e) {
//            fail("test failed:\n"+e.getMessage());
//        }
//
//    }
//}
//
