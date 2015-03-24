import java.util.Date

object BalancePeriodCalc {

  /**
   * Based on a set of transactions, calculates the periods that balances will be available 
   * such that any amount of money is represented in the longest possible period.
   * 
   * For example, given the following transactions:
   * 
   * - Pay Day        $1000 March 1
   * - Rent           ($500) March 1
   * - Cable          ($100) March 3
   * - Spending Money ($200) March 5
   * - Pay Day        $1000 March 15
   * - Spending Money ($200) March 20
   * - Mom's Bday     ($100) March 28th
   * (END MARCH 31)
   * 
   * The system would return the following periods:
   * 
   * March 1->31 : $200
   *       1->3  : $100
   *       3->5  : $200
   *       15->31: $700
   *       20->28: $100
   *       15->20: $200
   *       
   *  Why is this useful/interesting?  Imagine we lived in a world where short term
   *  secure investments (money markets, CDs) returned a useful amount in interest.
   *  This would help you optimize the investments you should make given some info
   *  about the future.
   *  
   */
  def calculate(trxs: List[Transaction]): List[BalancePeriod] = {
    
    val withoutDuplicateDates = trxs.groupBy(_.date).map{ case (date, gTrxs) => Transaction(date, gTrxs.map(_.amt).sum) }.toList.sortBy(_.date)
    
    // This bit of magic creates a new list of size n - 1 containing pairs of
    // adjacent elements.
    val pairs = (0 to (withoutDuplicateDates.size - 2)).map { i =>
      (withoutDuplicateDates(i), withoutDuplicateDates(i + 1))  
    }
    
    var balancePeriods = List.empty[BalancePeriod]
    pairs.foldLeft(BigDecimal(0.0)) { case (currBal, pair) =>
      val newBalance = currBal + pair._1.amt
      balancePeriods = balancePeriods :+ BalancePeriod(pair._1.date, pair._2.date, newBalance)
      newBalance
    }
    
    findMaxBalancePeriods(balancePeriods)
  }
  
  private def findMaxBalancePeriods(balancePeriods: List[BalancePeriod]): List[BalancePeriod] = {
    if (balancePeriods.size <= 1) {
      return balancePeriods
    }
    
    val minBalancePerdiod = balancePeriods.map(_.balance).min
    
    if (minBalancePerdiod > 0) {
      BalancePeriod(balancePeriods.head.start, balancePeriods.last.end, minBalancePerdiod) +: 
        findMaxBalancePeriods(balancePeriods.map( bp => bp.copy(balance= bp.balance - minBalancePerdiod)))
    } else { 
      // Find all elements with a positive balance, stops at an element with balance == 0.  Skip that element and recurse.
      val pair = balancePeriods.span(_.balance > 0)
      findMaxBalancePeriods(pair._1) ++ findMaxBalancePeriods(pair._2.tail)
    }
  }
}