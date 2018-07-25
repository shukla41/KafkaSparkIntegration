package ScalaFrameWorkForSpark.SchemaGenerate

import org.apache.spark.sql.{DataFrame, Dataset, Row}
import org.apache.spark.sql.functions._


/**
  * Created by shuvamoymondal on 7/25/18.
  */
object XMLToDataFrameParse {

  def AddressSchema(DF: Dataset[Row]): Dataset[Row] = {

  val p= DF.select(DF("RetailStoreID"),DF("RetailTransaction.LineItem.Sale").as("Sale"))
    val q=p.select(posexplode(p("Sale.DiscountAmount")),p("RetailStoreID"))
    q

  }

}
