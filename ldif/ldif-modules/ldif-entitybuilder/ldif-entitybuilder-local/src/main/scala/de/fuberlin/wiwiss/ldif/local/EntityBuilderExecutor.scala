package de.fuberlin.wiwiss.ldif.local

import ldif.module.Executor
import de.fuberlin.wiwiss.ldif.EntityBuilderTask
import ldif.local.runtime._
import java.util.Properties
import ldif.util.{MemoryUsage, FatalErrorListener}

class EntityBuilderExecutor(configParameters: ConfigParameters = ConfigParameters(new Properties, null)) extends Executor {

  type TaskType = EntityBuilderTask
  type InputFormat = GraphFormat
  type OutputFormat = DynamicEntityFormat

  /**
   * Determines the accepted input format of a specific task.
   */
  override def input(task : EntityBuilderTask) : GraphFormat = GraphFormat()

  /**
   * Determines the output format of a specific task.
   */
  override def output(task : EntityBuilderTask) : DynamicEntityFormat = DynamicEntityFormat()

  /**
   * Executes a specific task.
   *
   * @param task The task to be executed
   * @param reader The reader of the input data
   * @param writer The writer of the output data
   */
  override def execute(task : EntityBuilderTask, reader : Seq[QuadReader], writer : Seq[EntityWriter])
  {
    val eb = EntityBuilderFactory.getEntityBuilder(configParameters, task.entityDescriptions, reader)

//    println("Memory used (before build entities): " + MemoryUsage.getMemoryUsage())   //TODO: remove
    for ((ed, i) <- task.entityDescriptions.zipWithIndex )
      runInBackground {
        eb.buildEntities(ed, writer(i))
      }
  }

  /**
   * Evaluates an expression in the background.
   */
  private def runInBackground(function : => Unit) {
    val thread = new Thread {
      private val listener: FatalErrorListener = FatalErrorListener

      override def run {
        try {
          function
        } catch {
          case e: Exception => listener.reportError(e)
        }
      }
    }
    thread.start
  }
}
