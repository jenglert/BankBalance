import java.util.Date

case class BalancePeriod(
  start: Date,
  end: Date,
  balance: BigDecimal
)