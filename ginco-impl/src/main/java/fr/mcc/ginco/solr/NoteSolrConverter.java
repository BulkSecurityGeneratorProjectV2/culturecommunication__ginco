/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture
 * et de la Communication (2013)
 * <p/>
 * contact.gincoculture_at_gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to provide a thesaurus
 * management solution.
 * <p/>
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated
 * with loading, using, modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systemsand/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.mcc.ginco.solr;

import java.sql.Timestamp;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;

@Service
public class NoteSolrConverter {

	/**
	 * Convert a Note into a SolrDocument
	 *
	 * @param thesaurusNote
	 * @return SolrInputDocument
	 */

	public SolrInputDocument convertSolrNote(Note thesaurusNote) {
		SolrInputDocument doc = new SolrInputDocument();

		ThesaurusTerm term = thesaurusNote.getTerm();
		if (term != null) {
			doc.addField(SolrField.THESAURUSID, term.getThesaurusId());
			doc.addField(SolrField.THESAURUSTITLE, term.getThesaurus()
					.getTitle());
		}

		ThesaurusConcept concept = thesaurusNote.getConcept();
		if (concept != null) {
			doc.addField(SolrField.THESAURUSID, concept.getThesaurusId());
			doc.addField(SolrField.THESAURUSTITLE, concept.getThesaurus()
					.getTitle());
		}

		doc.addField(SolrField.IDENTIFIER, thesaurusNote.getIdentifier());
		if (thesaurusNote.getLexicalValue().getBytes().length <= SolrField.MAX_SIZE) {
			doc.addField(SolrField.LEXICALVALUE, thesaurusNote.getLexicalValue());
		} else {
			doc.addField(SolrField.LEXICALVALUE, thesaurusNote.getLexicalValue().substring(0, SolrField.CUTOFF_SIZE -1));
		}
		doc.addField(SolrField.TYPE, Note.class.getSimpleName());

		String noteTypeCode = thesaurusNote.getNoteType().getCode();
		if ("definition".equals(noteTypeCode)) {
			doc.addField(SolrField.EXT_TYPE, ExtEntityType.NOTE_DEFINITION);
		} else if ("editorialNote".equals(noteTypeCode)) {
			doc.addField(SolrField.EXT_TYPE, ExtEntityType.NOTE_EDITORIAL);
		} else if ("example".equals(noteTypeCode)) {
			doc.addField(SolrField.EXT_TYPE, ExtEntityType.NOTE_EXAMPLE);
		} else if ("historyNote".equals(noteTypeCode)) {
			doc.addField(SolrField.EXT_TYPE, ExtEntityType.NOTE_HISTORY);
		} else if ("scopeNote".equals(noteTypeCode)) {
			doc.addField(SolrField.EXT_TYPE, ExtEntityType.NOTE_SCOPE);
		} else {
			doc.addField(SolrField.EXT_TYPE, ExtEntityType.NOTE);
		}

		doc.addField(SolrField.LANGUAGE, thesaurusNote.getLanguage().getId());
		Timestamp modifiedDate = new Timestamp(thesaurusNote.getModified()
				.getTime());
		doc.addField(SolrField.MODIFIED, modifiedDate);

		Timestamp createdDate = new Timestamp(thesaurusNote.getCreated()
				.getTime());
		doc.addField(SolrField.CREATED, createdDate);

		return doc;
	}

}
