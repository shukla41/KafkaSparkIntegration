package ScalaFrameWorkForSpark.Project


import ScalaFrameWorkForSpark.SparkUtils.SparkConfig._
import org.apache.spark.sql.Row


object HigherOrder {

  def main(args: Array[String]): Unit = {

    def getType[T: Manifest](t: T): Manifest[T] = manifest[T]


    //Anonymous function
    val conv= (y: Int,z:Int) => math.sqrt(y+z)

    // hogher order functions
    def conv1(p: Int) ={
      (y: Int) => math.sqrt(p+y)
    }


    val spark= SparkSn
    val data= spark.read.textFile("/Users/shuvamoymondal/Desktop/test2.txt")

    import spark.implicits._
    data.map(_.split(",")).map(p=> conv1(p(0).toInt)(p(1).toInt)).toDF("Compute").show()
    //data.foreach(println)


//println(getType(data1))



    }

}
