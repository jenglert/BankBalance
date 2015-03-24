import java.util.Date

object Main extends App {

  // Example:
/*   * - Pay Day        $1000 March 1
   * - Rent           ($500) March 1
   * - Cable          ($100) March 3
   * - Spending Money ($200) March 5
   * - Pay Day        $1000 March 15
   * - Spending Money ($200) March 20
   * - Mom's Bday     ($100) March 28th
*/  
  val trxs = List(
    Transaction(new Date("03/1/2014"), 1000),
    Transaction(new Date("03/1/2014"), -500),
    Transaction(new Date("03/3/2014"), -100),
    Transaction(new Date("03/5/2014"), -200),
    Transaction(new Date("03/15/2014"), 1000),
    Transaction(new Date("03/20/2014"), -200),
    Transaction(new Date("03/28/2014"), -100),
    Transaction(new Date("03/31/2014"), 0)
  )
  
  BalancePeriodCalc.calculate(trxs).foreach(println)
}