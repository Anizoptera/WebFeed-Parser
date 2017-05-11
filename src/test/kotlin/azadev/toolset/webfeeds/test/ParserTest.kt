package azadev.toolset.webfeeds.test

import azadev.toolset.webfeeds.WebFeed
import azadev.toolset.webfeeds.WebFeedParser
import azadev.toolset.webfeeds.utils.ellipsize
import org.junit.*
import org.junit.Assert.assertEquals as eq
import org.junit.Assert.assertTrue as yes


class ParserTest
{
	@Test fun rss2_01() {
		parseTestDoc("rss2_01.xml") {
			eq("Liftoff News", title)
			eq("Liftoff to Space Exploration.", description)
			eq("http://liftoff.msfc.nasa.gov/", link)
			eq(1055217600000, pubished?.time)
			eq(1055238061000, updated?.time)

			checkItems(items, listOf(
					"""Item(t:Star City|d:How do-ty</a>.|l:http:/-ity.asp|pd:1054633161000|ud:1054633161000)""",
					"""Item(d:Sky wa-y 31st.|pd:1054292802000|ud:1054292802000)""",
					"""Item(t:The En-es More|d:Before-o that.|l:http:/-IMR.asp|pd:1054024652000|ud:1054024652000)""",
					"""Item(t:Astron-Laundry|d:Compar-ptions.|l:http:/-dry.asp|pd:1053420962000|ud:1053420962000)"""
			))
		}
	}

	@Test fun rss2_02() {
		parseTestDoc("rss2_02.xml") {
			eq("Анна Быкова", title)
			eq("Психолог, педагог, арт-терапевт", description)
			eq("http://annabykova.ru", link)
			eq(1493061350000, pubished?.time)
			eq(1493061350000, updated?.time)

			checkItems(items, listOf(
					"""Item(t:Тренин-стности|d:<p>Нов-a>.</p>|l:http:/-ti.html|pd:1492843916000|ud:1492843916000|ct:<p sty-a>.</p>)""",
					"""Item(t:Семейн-русель»|d:<p>Те,-a>.</p>|l:http:/-el.html|pd:1492618638000|ud:1492618638000|ct:<p><a -a>.</p>)""",
					"""Item(t:Секрет-ойствия|d:<p>Дав-a>.</p>|l:http:/-ya.html|pd:1490120736000|ud:1490120736000|ct:<p><a -a>.</p>)""",
					"""Item(t:Зачем -отовить|d:<p>Зач-a>.</p>|l:http:/-19.html|pd:1481556050000|ud:1481556050000|ct:<p><sp-a>.</p>)""",
					"""Item(t:Мы все-исовать|d:<p>Я н-a>.</p>|l:http:/-at.html|pd:1478696397000|ud:1478696397000|ct:<p sty-a>.</p>)"""
			))
		}
	}

	@Test fun rss2_03() {
		parseTestDoc("rss2_03.xml") {
			eq("AzaGroup – команда разработчиков", title)
			eq("Latest news from AzaGroup – команда разработчиков", description)
			eq("http://azagroup.ru/", link)
			eq(1477135481000, pubished?.time)
			eq(1477135481000, updated?.time)

			checkItems(items, listOf(
					"""Item(t:Java и- и чар?|d:<p>Соб-е »</a>|l:http:/-int-fun|pd:1477080000000|ud:1477080000000)""",
					"""Item(t:Androi- экрана|d:<p>Одн-е »</a>|l:http:/-ntation|pd:1443902400000|ud:1443902400000)""",
					"""Item(t:OnScro-oid 2.x|d:<p>Зам-е »</a>|l:http:/--called|pd:1442520000000|ud:1442520000000)""",
					"""Item(t:Автодо-лайфхак|d:<p>У&n-9"></p>|l:http:/-pletion|pd:1440532800000|ud:1440532800000)""",
					"""Item(t:Kotlin- Syntax|d:<p>Пор-е »</a>|l:http:/--syntax|pd:1437076800000|ud:1437076800000)""",
					"""Item(t:О скор-ормации|d:<p>Поп-е »</a>|l:http:/-nshuman|pd:1436299200000|ud:1436299200000)""",
					"""Item(t:Упаков-ка Kryo|d:<p>Для-е »</a>|l:http:/-on-kryo|pd:1435435200000|ud:1435435200000)""",
					"""Item(t:Немног-ования)|d:<p>Мы&-g"></p>|l:http:/--parser|pd:1433620800000|ud:1433620800000)""",
					"""Item(t:Создан- Gradle|d:<p>Соз-е »</a>|l:http:/--module|pd:1433275200000|ud:1433275200000)""",
					"""Item(t:Органи-and Use|d:<p>При-е »</a>|l:http:/-project|pd:1432929600000|ud:1432929600000)"""
			))
		}
	}

	@Test fun rss2_06() {
		parseTestDoc("rss2_06.xml") {
			eq(null, title)
			eq(null, description)
			eq("https://www.visitportugal.com/en/feedRss", link)
			eq(null, pubished?.time)
			eq(null, updated?.time)

			eq(100, items.size)
		}
	}


	private fun parseTestDoc(fileName: String, callback: WebFeed.()->Unit) {
		val stream = javaClass.classLoader.getResourceAsStream("test-docs/$fileName")
		WebFeedParser().parse(stream).normalize().callback()
	}

	private fun checkItems(items: List<WebFeed.Item>, expected: List<String>) {
		val GLUE = " | "
		eq(expected.joinToString(GLUE), items.map { itemToString(it) }.joinToString(GLUE))
	}

	private fun itemToString(item: WebFeed.Item): String = with(item) {
		val fields = linkedMapOf(
				"t" to title,
				"d" to description,
				"l" to link,
				"pd" to pubished?.time,
				"ud" to updated?.time,
				"ct" to content
		)

		"Item(${fields
				.map { if (it.value == null) null else "${it.key}:${it.value.toString().ellipsize(14, "-")}" }
				.filterNotNull()
				.joinToString("|")})"
	}
}
