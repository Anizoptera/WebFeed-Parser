package azadev.toolset.webfeeds

import azadev.toolset.webfeeds.parser.RssParser
import azadev.toolset.webfeeds.utils.eqIc
import org.jsoup.Jsoup
import org.jsoup.helper.StringUtil
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import java.io.InputStream
import java.nio.charset.Charset


class WebFeedParser
{
	fun parse(document: Document): WebFeed {
		val childNodes = document.childNodes()

		if (childNodes.find { it.nodeName() eqIc "rss" } != null)
			return RssParser().parse(document)

		if (childNodes.find { it.nodeName() eqIc "feed" } != null)
			TODO("not implemented")

		if (childNodes.find { it.nodeName() eqIc "rdf:RDF" } != null)
			TODO("not implemented")

		throw IllegalArgumentException("Unknown kind of web feed: ${StringUtil.normaliseWhitespace(document.outerHtml()).take(100)}")
	}

	fun parse(string: String)
			= parse(Jsoup.parse(string, "", Parser.xmlParser()))

	fun parse(stream: InputStream, charset: Charset = Charsets.UTF_8)
			= parse(Jsoup.parse(stream, charset.toString(), "", Parser.xmlParser()))
}
