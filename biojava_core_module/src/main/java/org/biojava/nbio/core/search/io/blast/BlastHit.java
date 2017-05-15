package org.biojava.nbio.core.search.io.blast;

import org.biojava.nbio.core.search.io.Hsp;
import org.biojava.nbio.core.sequence.template.Sequence;

import java.util.List;

public class BlastHit extends org.biojava.nbio.core.search.io.Hit {
	public BlastHit(int hitNum, String hitId, String hitDef, String hitAccession, int hitLen, List<Hsp> hitHsps, Sequence hitSequence) {
		super(hitNum, hitId, hitDef, hitAccession, hitLen, hitHsps, hitSequence);
	}

}
