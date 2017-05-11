package azadev.toolset.webfeeds.parser

import azadev.toolset.webfeeds.WebFeed
import org.jsoup.nodes.Document


abstract class Parser
{
	abstract fun parse(document: Document): WebFeed
}
