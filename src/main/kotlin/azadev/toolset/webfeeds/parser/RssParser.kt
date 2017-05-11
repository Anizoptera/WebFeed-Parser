package azadev.toolset.webfeeds.parser

import azadev.toolset.webfeeds.WebFeed
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


/*
http://www.rssboard.org/rss-specification
 */
class RssParser : Parser()
{
	val feed = WebFeed()


	override fun parse(document: Document): WebFeed {
		val channel = document.select("rss > channel").firstOrNull() ?: throw IllegalArgumentException("Malformed RSS")

		channel.childNodes().forEach {
			val node = it as? Element ?: return@forEach
			when(node.nodeName().toLowerCase()) {
				"item" -> {
					val item = WebFeed.Item()
					node.childNodes().forEach {
						if (it is Element)
							item.setField(it.nodeName(), it.text())
					}
					feed.addItem(item.normalize())
				}
				else -> feed.setField(node.nodeName(), node.text())
			}
		}

		return feed
	}
}
