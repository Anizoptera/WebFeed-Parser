package azadev.toolset.webfeeds

import azadev.toolset.webfeeds.utils.notNullOrEmpty
import azadev.toolset.webfeeds.utils.parseDate
import java.util.*


/**
 * Respresents an RSS/Atom feed.
 *
 * All the fields of a WebFeed-object should be set via [setField] method.
 * [setField] has some logics, and recognizes the tag-names of all the feed formats.
 * This makes it possible to parse mixed feed files
 * (for example, when RDF's "content:encoded" tag is used inside a RSS2.0 file).
 *
 * After the fields are set, you can invoke [normalize] method to do some cleaning.
 */
class WebFeed
{
	var title: String? = null
	var description: String? = null
	var link: String? = null

	/**
	 * In RSS:
	 *  The last date when this feed was published/re-published.
	 *  For example, the New York Times publishes on a daily basis,
	 *  the publication date flips once every 24 hours.
	 *
	 * In Atom:
	 *  Typically, will be associated with the initial
	 *  creation or first availability of the feed.
	 *  https://tools.ietf.org/html/rfc4287#section-4.2.9
	 */
	var pubished: Date? = null

	/**
	 * Date when the content of the feed was changed.
	 */
	var updated: Date? = null

	var items: ArrayList<Item> = ArrayList()


	fun addItem(item: Item): WebFeed {
		items.add(item)
		return this
	}

	fun setField(name: String, value: String) {
		when (name.toLowerCase()) {
			"title"             -> title = value
			"description"       -> description = value
			"link"              -> link = value

			"pubdate"           -> pubished = value.parseDate()
			"published"         -> pubished = value.parseDate()
			"lastbuilddate"     -> updated = value.parseDate()
			"updated"           -> updated = value.parseDate()
			"dc:date"           -> {
				val date = value.parseDate()
				pubished = date
				updated = date
			}
		}
	}

	fun normalize(): WebFeed {
		if (pubished == null)
			pubished = updated

		if (updated == null)
			updated = pubished

		title = title.notNullOrEmpty()
		description = description.notNullOrEmpty()
		link = link.notNullOrEmpty()

		return this
	}


	/**
	 * Represents an item/entry of a feed.
	 */
	class Item
	{
		var title: String? = null
		var description: String? = null
		var link: String? = null

		var pubished: Date? = null
		var updated: Date? = null

		/**
		 * RdfSS has a module "Content" for the actual content of websites, in multiple formats.
		 * http://web.resource.org/rss/1.0/modules/content/
		 */
		var content: String? = null


		fun setField(name: String, value: String) {
			when (name.toLowerCase()) {
				"title"             -> title = value
				"description"       -> description = value
				"link"              -> link = value

				"pubdate"           -> pubished = value.parseDate()
				"published"         -> pubished = value.parseDate()
				"updated"           -> updated = value.parseDate()
				"dc:date"           -> {
					val date = value.parseDate()
					pubished = date
					updated = date
				}

				"content:encoded"   -> content = value
			}
		}

		fun normalize(): Item {
			if (pubished == null)
				pubished = updated

			if (updated == null)
				updated = pubished

			title = title.notNullOrEmpty()
			description = description.notNullOrEmpty()
			link = link.notNullOrEmpty()

			content = content.notNullOrEmpty()

			return this
		}
	}
}
