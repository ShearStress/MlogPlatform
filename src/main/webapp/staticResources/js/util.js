/**
 * tell if a string is empty
 * 	return true if empty
 * @param str
 * @returns {Boolean}
 */
function isEmpty(str) {
	if (str == null || str.trim() == "") {
		return true;
	}
	return false;
}