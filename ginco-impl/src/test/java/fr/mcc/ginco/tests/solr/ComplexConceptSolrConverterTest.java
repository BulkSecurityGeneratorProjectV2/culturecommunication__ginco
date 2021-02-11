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

package fr.mcc.ginco.tests.solr;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.SplitNonPreferredTerm;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.enums.TermStatusEnum;
import fr.mcc.ginco.solr.ComplexConceptSolrConverter;
import fr.mcc.ginco.solr.ExtEntityType;
import fr.mcc.ginco.solr.SolrField;
import fr.mcc.ginco.utils.DateUtil;

public class ComplexConceptSolrConverterTest {

	@InjectMocks
	private ComplexConceptSolrConverter complexConceptSolrConverter;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testConvertSolrComplexConcept() throws SolrServerException, IOException {

		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("http://th1");
		fakeThesaurus.setTitle("Fake thesaurus");

		Language langFR = new Language();
		langFR.setId("fr-FR");

		SplitNonPreferredTerm fakeComplexConcept = new SplitNonPreferredTerm();
		fakeComplexConcept.setIdentifier("http://cc1");
		fakeComplexConcept.setThesaurus(fakeThesaurus);
		fakeComplexConcept.setCreated(DateUtil.nowDate());
		fakeComplexConcept.setModified(DateUtil.nowDate());
		fakeComplexConcept.setStatus(TermStatusEnum.VALIDATED.getStatus());
		fakeComplexConcept.setLanguage(langFR);
		fakeComplexConcept.setLexicalValue("fakeComplexConceptLexicalValue");


		SolrInputDocument doc = complexConceptSolrConverter.convertSolrComplexConcept(fakeComplexConcept);

		Assert.assertEquals(fakeThesaurus.getIdentifier(), doc
				.getFieldValue(SolrField.THESAURUSID));

		Assert.assertEquals(fakeThesaurus.getTitle(), doc
				.getFieldValue(SolrField.THESAURUSTITLE));

		Assert.assertEquals(fakeComplexConcept.getIdentifier(), doc
				.getFieldValue(SolrField.IDENTIFIER));

		Assert.assertEquals(fakeComplexConcept.getLexicalValue(), doc
				.getFieldValue(SolrField.LEXICALVALUE));

		Assert.assertEquals(SplitNonPreferredTerm.class.getSimpleName(), doc
				.getFieldValue(SolrField.TYPE));

		Assert.assertEquals(ExtEntityType.COMPLEX_CONCEPT, doc
				.getFieldValue(SolrField.EXT_TYPE));

		Assert.assertEquals(fakeComplexConcept.getModified(), doc
				.getFieldValue(SolrField.MODIFIED));

		Assert.assertEquals(fakeComplexConcept.getCreated(), doc
				.getFieldValue(SolrField.CREATED));

		Assert.assertEquals(fakeComplexConcept.getStatus(), doc
				.getFieldValue(SolrField.STATUS));
	}
}
