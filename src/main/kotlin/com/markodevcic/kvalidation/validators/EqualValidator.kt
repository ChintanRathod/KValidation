package com.markodevcic.kvalidation.validators

class EqualValidator(private val other: Any) : ValidatorBase (){
    override fun isValid(result: Any?): Boolean {
        return result == other
    }

    override fun toString(): String {
        return "Equality validator, expected equal to: $other"
    }
}