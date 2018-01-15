package com.carterhudson

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.sameInstance
import org.junit.After
import org.junit.Assert
import org.junit.Test

class KotlinExtensionTest {
    val aTrue = true
    val aFalse = false
    val aThing: Any? = Any()
    val emptyString: String? = ""
    val valueString: String? = "a"
    val SHOULD_NOT_EXECUTE = "should not execute"

    private fun shouldNotExecute() {
        Assert.fail(SHOULD_NOT_EXECUTE)
    }

    @Test
    fun letAllNotNull_pair() {
        (emptyString to valueString).letAllNotNull { e, v ->
            assert.that(e, sameInstance(emptyString))
            assert.that(v, sameInstance(valueString))
        } ?: shouldNotExecute()
    }

    @Test
    fun letAllNot_value_pair() {
        (emptyString to valueString).letAllNot(emptyString) { _, _ -> shouldNotExecute() }
        (valueString to valueString).letAllNot(emptyString) { a, b ->
            assert.that(a, sameInstance(valueString))
            assert.that(b, sameInstance(valueString))
        }
    }

    @Test
    fun letAll_value_pair() {
        Assert.assertNull((emptyString to valueString).letAll(emptyString) { _, _ -> shouldNotExecute() })
        (emptyString to emptyString).letAll(emptyString) { a, b ->
            assert.that(a, sameInstance(emptyString))
            assert.that(b, sameInstance(emptyString))
        }
    }

    @Test
    fun letAllNotNull_triple() {
        Assert.assertNull(Triple(emptyString, valueString, null).letAllNotNull { _, _, _ -> shouldNotExecute() })
        Triple(emptyString, valueString, aThing).letAllNotNull { a, b, c ->
            Assert.assertNotNull(a)
            Assert.assertNotNull(b)
            Assert.assertNotNull(c)
        }
    }

    @Test
    fun letAllNotNull_quad() {
        Assert.assertNull(Quad(valueString, valueString, valueString, null).letAllNotNull { _, _, _, _ -> shouldNotExecute() })
        Quad(valueString, valueString, valueString, valueString).letAllNotNull { a, b, c, d ->
            Assert.assertNotNull(a)
            Assert.assertNotNull(b)
            Assert.assertNotNull(c)
            Assert.assertNotNull(d)
        }
    }

    @Test
    fun boolean_ifTrue() {
        aTrue.ifTrue { valueString }.let { assert.that(it, sameInstance(Unit)) }
        aFalse.ifTrue { shouldNotExecute() }.let { assert.that(it, sameInstance(Unit)) }
    }

    @Test
    fun boolean_ifTrueMaybe() {
        aTrue.ifTrueMaybe { valueString }.let { assert.that(it, sameInstance(valueString)) }
        aFalse.ifTrueMaybe { shouldNotExecute() }.let { Assert.assertNull(it) }
    }

    @Test
    fun boolean_ifTrueAlso() {
        aTrue.ifTrueAlso { valueString }.let { assert.that(it, sameInstance(true)) }
        aFalse.ifTrueAlso { shouldNotExecute() }.let { assert.that(it, sameInstance(false)) }
    }

    @Test
    fun boolean_ifFalse() {
        aTrue.ifFalse { shouldNotExecute() }.let { assert.that(it, sameInstance(Unit)) }
        aFalse.ifFalse { valueString }.let { assert.that(it, sameInstance(Unit)) }
    }

    @Test
    fun boolean_ifFalseMaybe() {
        aFalse.ifFalseMaybe { valueString }.let { assert.that(it, sameInstance(valueString)) }
        aTrue.ifFalseMaybe { shouldNotExecute() }.let { Assert.assertNull(it) }
    }

    @Test
    fun boolean_ifFalseAlso() {
        aTrue.ifFalseAlso { shouldNotExecute() }.let { assert.that(it, sameInstance(true)) }
        aFalse.ifFalseAlso { valueString }.let { assert.that(it, sameInstance(false)) }
    }

    @Test
    fun ifTrue() {
        //true predicate
        emptyString?.ifTrue({ it.also { assert.that(it, sameInstance(emptyString)) }.isEmpty() }) {
            assert.that(it, sameInstance(emptyString))
        }?.let { assert.that(it, sameInstance(Unit)) }

        //false predicate
        emptyString?.ifTrue({ it.also { assert.that(it, sameInstance(emptyString)) }.isNotEmpty() }) {
            shouldNotExecute()
        }?.let { assert.that(it, sameInstance(Unit)) }

        //expression
        emptyString?.ifTrue(true) {
            assert.that(it, sameInstance(emptyString))
        }?.let { assert.that(it, sameInstance(Unit)) }
    }

    @Test
    fun ifMaybe() {
        //true predicate
        emptyString?.ifMaybe({ it.also { assert.that(it, sameInstance(emptyString)) }.isEmpty() }) {
            valueString?.apply { assert.that(it, sameInstance(emptyString)) }
        }?.let { assert.that(it, sameInstance(valueString)) }

        //false predicate
        emptyString?.ifMaybe({ it.also { assert.that(it, sameInstance(emptyString)) }.isNotEmpty() }) {
            shouldNotExecute()
        }?.let { Assert.assertNull(it) }

        //expression
        emptyString?.ifMaybe(true) {
            assert.that(it, sameInstance(emptyString)); valueString
        }?.let { assert.that(it, sameInstance(valueString)) }
    }

    @Test
    fun ifAlso() {
        //true predicate
        emptyString?.ifAlso({ it.also { assert.that(it, sameInstance(emptyString)) }.isEmpty() }) {
            assert.that(it, sameInstance(emptyString))
        }?.let { assert.that(it, sameInstance(emptyString)) }

        //false predicate
        emptyString?.ifAlso({ it.also { assert.that(it, sameInstance(emptyString)) }.isNotEmpty() }) {
            shouldNotExecute()
        }?.let { assert.that(it, sameInstance(emptyString)) }

        //expression
        emptyString?.ifAlso(true) {
            assert.that(it, sameInstance(emptyString))
        }?.let { assert.that(it, sameInstance(emptyString)) }
    }

    @Test
    fun ifApply() {
        //true predicate
        emptyString?.ifApply({ it.also { assert.that(it, sameInstance(emptyString)) }.isEmpty() }) {
            assert.that(this, sameInstance(emptyString))
        }?.let { assert.that(it, sameInstance(emptyString)) }

        //false predicate
        emptyString?.ifApply({ it.also { assert.that(it, sameInstance(emptyString)) }.isNotEmpty() }) {
            assert.that(this, sameInstance(emptyString))
        }?.let { assert.that(it, sameInstance(emptyString)) }

        //expression
        emptyString?.ifApply(true) {
            assert.that(this, sameInstance(emptyString))
        }?.let { assert.that(it, sameInstance(emptyString)) }
    }

    @After
    fun cleanup() {

    }
}