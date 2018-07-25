package ScalaFrameWorkForSpark.Project

import ScalaFrameWorkForSpark.KafkaUtils.KafkaConsumer._
import ScalaFrameWorkForSpark.SparkUtils.SparkConfig.SparkSn

/**
  * Created by shuvamoymondal on 7/23/18.
  */
object App3 {
  def main(args: Array[String]): Unit = {


    val df = ScalaFrameWorkForSpark.SparkUtils.SparkConfig.SparkSn.sqlContext.read.format("com.databricks.spark.xml")
      .option("rowTag", "Transaction").load("/usr/local/src/xml.xml")

    df.printSchema()
    //df.show()

    val selectedData = df.select("RetailStoreID","WorkstationID","OperatorID._OperatorName","OperatorID._VALUE","RetailTransaction.ReceiptDateTime")
    selectedData.show()
    }
  }
