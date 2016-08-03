package com.markodevcic.kvalidation

import com.markodevcic.kvalidation.errors.ValidationError
import java.util.*

@Suppress("UNCHECKED_CAST")
abstract class BaseValidator<T>(private val consumer: T) where T : Any {
    private val propertyContexts: MutableList<PropertyContext<T, *>> = ArrayList()
    private val consumerClass = consumer.javaClass
    private val valueMap = HashMap<Class<*>, PropertyContext<T, *>>()

    var strategy = ValidationStrategy.FULL

    fun <TFor> newRule(valueFactory: (T) -> TFor): RuleBuilder<T, TFor> {
        val propertyContext = PropertyContext(valueFactory)
        val method = valueFactory.javaClass
                .declaredMethods
                .filter { m ->
                    m.parameterTypes.count() == 1
                            && m.parameterTypes[0] == consumerClass
                            && m.name == "invoke"
                }.single()
        val valueClass = method.returnType as Class<TFor>
        propertyContext.clazz = valueClass
        propertyContexts.add(propertyContext)
        return RuleBuilder(propertyContext)
    }

    fun validate(): ValidationResult {
        val strategyCopy = strategy
        val result = ValidationResult()
        propertyContexts.forEach { context ->
            val validators = context.validators
            val value = context.valueFactory(consumer)
            validators.forEach { validator ->
                if (validator.precondition.invoke(consumer) && !validator.isValid(value)) {
                    result.validationErrors.add(ValidationError("Fail", validator.errorLevel))
                    if (strategyCopy == ValidationStrategy.STOP_ON_FIRST) {
                        return result
                    }
                }
            }
        }
        return result
    }
}