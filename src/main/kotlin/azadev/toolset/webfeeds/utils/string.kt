package azadev.toolset.webfeeds.utils


internal fun <T : CharSequence> T?.notNullOrEmpty(): T? = if (!isNullOrEmpty()) this else null


internal infix fun String.eqIc(other: String) = equals(other, ignoreCase = true)


internal fun String.ellipsize(maxLen: Int, ellipsis: String = ".."): String {
	val len = length

	if (len <= maxLen)
		return this

	val partLen = (maxLen - ellipsis.length) / 2.0
	val lenLeft = Math.floor(partLen).toInt()
	val lenRight = Math.ceil(partLen).toInt()

	if (lenLeft <= 0 || lenRight <= 0)
		return this

	return take(lenLeft) + ellipsis + takeLast(lenRight)
}

