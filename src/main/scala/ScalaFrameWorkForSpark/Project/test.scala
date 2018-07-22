package ScalaFrameWorkForSpark.Project

import java.io.File
import sys.process._


/**
  * Created by shuvamoymondal on 7/23/18.
  */
object test {

  def cmd(cmd: String) = cmd.!!

  def main(args: Array[String]): Unit = {

    val p ="rm -rf /usr/local/src/file2/f.xml"

    p !!
  }
}
