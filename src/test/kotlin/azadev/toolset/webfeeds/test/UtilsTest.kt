package azadev.toolset.webfeeds.test

import azadev.toolset.webfeeds.utils.*
import org.junit.*
import org.junit.Assert.assertEquals as eq
import org.junit.Assert.assertTrue as yes


class UtilsTest
{
	@Test fun dateParserTest() {
		eq(1071340202000, "2003-12-13T18:30:02Z".parseRfc3339Date().time)
		eq(1071340202250, "2003-12-13T18:30:02.25Z".parseRfc3339Date().time)
		eq(1071336602000, "2003-12-13T18:30:02+01:00".parseRfc3339Date().time)
		eq(1071336602250, "2003-12-13T18:30:02.25+01:00".parseRfc3339Date().time)

		eq(1492249351000, "Sat, 15 Apr 2017 09:42:31 GMT".parseRfc822Date().time)
		eq(1472119377000, "Thu, 25 Aug 16 15:02:57 YEKT".parseRfc822Date().time)


		eq(1071340202000, "2003-12-13T18:30:02Z".parseDate().time)
		eq(1472119377000, "Thu, 25 Aug 16 15:02:57 YEKT".parseDate().time)
	}
}
