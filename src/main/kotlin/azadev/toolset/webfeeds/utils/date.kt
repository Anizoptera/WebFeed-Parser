package azadev.toolset.webfeeds.utils

import java.text.SimpleDateFormat
import java.util.*


/*
TODO: Support for ISO8601 (recommended for RDF RSS)
https://www.mnot.net/rss/tutorial/#rss-10
http://dublincore.org/documents/2012/06/14/dcmi-terms/?v=elements#date
https://www.w3.org/TR/NOTE-datetime
https://ru.wikipedia.org/wiki/ISO_8601
 */



/**
 * Tries to parse the date in any known format.
 */
internal fun String.parseDate(): Date {
	return try { parseRfc3339Date() }
	catch(e: Throwable) { parseRfc822Date() }
}



/*
2003-12-13T18:30:02Z
2003-12-13T18:30:02.25Z
2003-12-13T18:30:02+01:00
2003-12-13T18:30:02.25+01:00

https://www.ietf.org/rfc/rfc3339.txt
 */
private val REX_DATE_RFC822 = (
		"(\\d{4})-(\\d{2})-(\\d{2})" +      // 1-3: date
		"T(\\d{2}):(\\d{2}):(\\d{2})" +     // 4-6: time
		"(?:\\.(\\d+))?" +                  // 7: secfrac
		"(Z|([+-])(\\d{2}):(\\d{2}))"       // 8: tz, 9: sign, 10: hrs, 11: mins
).toRegex()

internal fun String.parseRfc3339Date(): Date {
	val m = REX_DATE_RFC822.matchEntire(this)
			?: throw IllegalArgumentException("Malformed date: $this")

	val cal = GregorianCalendar(
			m.groups[1]!!.value.toInt(),
			m.groups[2]!!.value.toInt() - 1,
			m.groups[3]!!.value.toInt(),
			m.groups[4]!!.value.toInt(),
			m.groups[5]!!.value.toInt(),
			m.groups[6]!!.value.toInt()
	)

	cal.timeZone = TimeZone.getTimeZone("UTC")

	val secfrac = m.groups[7]?.value
	if (secfrac != null)
		cal[Calendar.MILLISECOND] = secfrac.padEnd(3, '0').take(3).toInt()

	val tz = m.groups[8]!!.value
	if (tz != "Z") {
		val mult = if (m.groups[9]!!.value == "-") 1 else -1
		cal.add(Calendar.HOUR_OF_DAY, mult * m.groups[10]!!.value.toInt())
		cal.add(Calendar.MINUTE, mult * m.groups[11]!!.value.toInt())
	}

	return cal.time
}



private val DATE_FORMAT_RFC822 = SimpleDateFormat("EEE, dd MMM yy HH:mm:ss Z", Locale.US)

internal fun String.parseRfc822Date(): Date {
	try { return DATE_FORMAT_RFC822.parse(this) }
	catch(e: Throwable) {}

	throw IllegalArgumentException("Malformed date: $this")
}
