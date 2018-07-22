package ScalaFrameWorkForSpark.Common

import sys.process._

/**
  * Created by shuvamoymondal on 7/23/18.
  */
object FileDirectoryHandling {

  def cmd(cmd: String) = cmd.!!

}
