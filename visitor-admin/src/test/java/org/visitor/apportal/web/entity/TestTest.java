package org.visitor.apportal.web.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 测试Test的validator
 * 
 * @author yong.cao
 * @create-time 2013-12-2
 * @revision 1.0.0
 * @E_mail yong.cao@renren-inc.com
 */
public class TestTest {

    static Validator validator;

    Set<ConstraintViolation<org.visitor.apportal.web.entity.Test>> constraintViolations;

    org.visitor.apportal.web.entity.Test test;

    @BeforeClass
    public static void before() throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        ValidatorFactory config = Validation.buildDefaultValidatorFactory();
        validator = config.getValidator();
    }

    @Before
    public void setUp() throws Exception {
        test = new org.visitor.apportal.web.entity.Test();
        test.setMsg("msg");
        test.setDetail("detail");
    }

    @Test
    public void shouldFailValidationWithNullInputValuesForMsgField() {
        constraintViolations = validator.validateValue(org.visitor.apportal.web.entity.Test.class,
                "msg", null);

        assertEquals(1, constraintViolations.size());
        assertEquals("别乱写，不支持三个字以下的", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void shouldFailValidationWithLengthLessThan3ForDetailField() {
        constraintViolations = validator.validateValue(org.visitor.apportal.web.entity.Test.class,
                "detail", "aa");

        assertEquals(1, constraintViolations.size());
        assertTrue(constraintViolations.iterator().next().getMessage().contains("3"));
    }

    @Test
    public void shouldFailValidationWithLengthMoreThan10ForDetailField() {
        constraintViolations = validator.validateValue(org.visitor.apportal.web.entity.Test.class,
                "detail", "12345678900");

        assertEquals(1, constraintViolations.size());
        assertTrue(constraintViolations.iterator().next().getMessage().contains("10"));
    }

    @Test
    public void testHappyPathValidationForWholeObject() {
        constraintViolations = validator.validate(test);
        assertEquals(0, constraintViolations.size());
    }

}
