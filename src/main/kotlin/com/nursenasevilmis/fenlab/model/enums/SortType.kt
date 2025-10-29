package com.nursenasevilmis.fenlab.model.enums

//database sorgusunda kullanılacak
enum class SortType {
    MOST_RECENT,
    MOST_FAVORITED,
    HIGHEST_RATED,
    OLDEST  // AVG(ratings.rating) DESC
}