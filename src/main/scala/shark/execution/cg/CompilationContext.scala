/*
 * Copyright (C) 2012 The Regents of The University California.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package shark.execution.cg

import scala.collection.mutable.ArrayBuffer
import shark.execution.HiveDesc
import shark.execution.CGObjectOperator


/**
 * Compilation Context (CC) is the core concept of the CG class compiling. Its life cycle exists
 * the SparkTask.execute(), which means it compiles the source code in the Master Process, the
 * generated bytecode will be distributed via OperatorClassLoader. 
 */
class CompilationContext() {
  
  private[this] val entries = ArrayBuffer[(String, String)]()
  private[this] val compiler = new JavaCompilerHelper
  val preCompiledClassLoader: OperatorClassLoader = compiler.getOperatorClassLoader

  def add(units: List[(String, String)]) {
    entries ++= units
  }
  
  def remove(units: List[(String, String)]) {
    entries --= units
  }

  def compile(units: List[(String, String)]) {
    entries ++= units
    compiler.compile(units)
  }
  // to compile all of the source code entries
  def compile() {
    if(entries.length > 0) compiler.compile(entries.toList)
  }
  
  def count() = entries.length
}
