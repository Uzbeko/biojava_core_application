package org.biojava.nbio.core.search.io.blast;

public class BlastHsp extends org.biojava.nbio.core.search.io.Hsp {
	public BlastHsp(int hspNum, double hspBitScore, int hspScore, double hspEvalue, int hspQueryFrom, int hspQueryTo, int hspHitFrom, int hspHitTo, int hspQueryFrame, int hspHitFrame, int hspIdentity, int hspPositive, int hspGaps, int hspAlignLen, String hspQseq, String hspHseq, String hspIdentityString, Double percentageIdentity, Integer mismatchCount) {
		super(hspNum, hspBitScore, hspScore, hspEvalue, hspQueryFrom, hspQueryTo, hspHitFrom, hspHitTo, hspQueryFrame, hspHitFrame, hspIdentity, hspPositive, hspGaps, hspAlignLen, hspQseq, hspHseq, hspIdentityString, percentageIdentity, mismatchCount);
	}

}
