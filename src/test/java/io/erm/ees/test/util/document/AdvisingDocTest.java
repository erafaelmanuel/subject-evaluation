//package io.erm.ees.test.util.document;
//
//import io.erm.ees.helper.DbFactory;
//import io.erm.ees.model.Student;
//import io.erm.ees.model.Subject;
//import io.erm.ees.util.document.AdvisingDoc;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.List;
//import java.util.logging.Logger;
//
//public class AdvisingDocTest implements AdvisingDoc.CreationListener {
//
//    private AdvisingDoc advisingDoc;
//    private final Student STUDENT = DbFactory.studentFactory().getStudentById(20178925375L);
//    private final List<Subject> SUBJECT_LIST = DbFactory.subjectFactory().getSubjectListBySearch("3");
//
//    static final Logger LOGGER = Logger.getLogger(AdvisingDocTest.class.getSimpleName());
//
//    @Before
//    public void setup() {
//        advisingDoc = new AdvisingDoc();
//        advisingDoc.setStudent(STUDENT);
//        advisingDoc.addRawSubject(SUBJECT_LIST);
//        advisingDoc.setCreationListener(this);
//    }
//
//    @Test
//    public void testCreate() {
//        advisingDoc.create();
//    }
//
//    @Override
//    public void onError() {
//        LOGGER.warning("File failed to create");
//    }
//}
