package com.markodevcic.kvalidation.validators


import com.markodevcic.kvalidation.TestObject
import com.markodevcic.kvalidation.TestObjectValidator
import org.junit.Assert
import org.junit.Test

class NonNullValidatorTests {

    @Test
    fun testNonNull() {
        val testObject = TestObject()
        val validator = TestObjectValidator(testObject)
        validator.newRule { t -> t.name }
        .nonNull()
        testObject.name = null
        val validationResult = validator.validate()
        Assert.assertFalse(validationResult.isValid)
        Assert.assertTrue(validationResult.validationErrors.size == 1)
    }
}