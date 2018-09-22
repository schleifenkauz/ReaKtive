package org.nikok.reaktive.value

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.nikok.reaktive.help.description
import org.nikok.reaktive.value.help.value

internal object CreateSpec : Spek({
                                      fun testInitialValueAndDescription(createValue: (String, Int) -> Value<Int>) {
                                          val v = createValue("testDescription", 1)
                                          v shouldMatch value(equalTo(1))
                                          v shouldMatch description("testDescription")
                                      }

                                      describe("value(name, value)") {
                                          it("should return a Value of value described by name") {
                                              testInitialValueAndDescription(::value)
                                          }
                                      }
                                      describe("variable(name, value)") {
                                          it("should return a Variable of value described by name") {
                                              testInitialValueAndDescription(::value)
                                          }
                                      }
                                      describe("reactiveValue(name, value)") {
                                          it("should return a ReactiveValue of value described by name") {
                                              testInitialValueAndDescription(::value)
                                          }
                                      }
                                      describe("reactiveVariable(name, value)") {
                                          it("should return a ReactiveVariable of value described by name") {
                                              testInitialValueAndDescription(::value)
                                          }
                                      }
                                  })