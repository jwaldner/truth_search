package com.wfs.truthsearch.utils

object VerseUtils {
    private val bookAbbreviations = mapOf(
        "01" to "Gen", "02" to "Exo", "03" to "Lev", "04" to "Num", "05" to "Deu",
        "06" to "Jos", "07" to "Jud", "08" to "Ruth", "09" to "1Sam", "10" to "2Sam",
        "11" to "1Kgs", "12" to "2Kgs", "13" to "1Chr", "14" to "2Chr", "15" to "Ezr",
        "16" to "Neh", "17" to "Est", "18" to "Job", "19" to "Psa", "20" to "Pro",
        "21" to "Ecc", "22" to "Sng", "23" to "Isa", "24" to "Jer", "25" to "Lam",
        "26" to "Eze", "27" to "Dan", "28" to "Hos", "29" to "Joe", "30" to "Amo",
        "31" to "Oba", "32" to "Jon", "33" to "Mic", "34" to "Nah", "35" to "Hab",
        "36" to "Zep", "37" to "Hag", "38" to "Zec", "39" to "Mal",
        "40" to "Mat", "41" to "Mar", "42" to "Luk", "43" to "Joh", "44" to "Act",
        "45" to "Rom", "46" to "1Cor", "47" to "2Cor", "48" to "Gal", "49" to "Eph",
        "50" to "Phi", "51" to "Col", "52" to "1Th", "53" to "2Th", "54" to "1Tim",
        "55" to "2Tim", "56" to "Tit", "57" to "Phm", "58" to "Heb", "59" to "Jam",
        "60" to "1Pet", "61" to "2Pet", "62" to "1Joh", "63" to "2Joh", "64" to "3Joh",
        "65" to "Jud", "66" to "Rev"
    )

    fun formatFriendlyVerseId(verseId: String): String {
        val parts = verseId.split("_", ":")
        if (parts.size < 3) return verseId // Return as-is if invalid format

        val bookId = parts[0]
        val chapter = parts[1].toIntOrNull() ?: return verseId
        val verse = parts[2].toIntOrNull() ?: return verseId

        val bookAbbreviation = bookAbbreviations[bookId] ?: "Unknown"

        return "$bookAbbreviation $chapter:$verse"
    }

    /**
     * Converts a list of verse IDs to their friendly formats.
     * @param verseIds List of verse IDs in the format `01_01:001`.
     * @return List of friendly verse IDs in the format `BookAbbreviation Chapter:Verse`.
     */
    fun formatFriendlyVerseIds(verseIds: List<String>): List<String> {
        return verseIds.map { formatFriendlyVerseId(it) }
    }
}
