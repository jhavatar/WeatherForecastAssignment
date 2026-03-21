package io.chthonic.weather.presentation.utils

import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems

inline fun <T : Any> LazyGridScope.items(
    items: LazyPagingItems<T>,
    crossinline itemKey: ((item: T) -> Any?) = { null },
    crossinline itemContent: @Composable LazyGridItemScope.(item: T?) -> Unit
) {
    items(
        count = items.itemCount,
        key = { index ->
            items.peek(index)?.let { item ->
                itemKey(item)
            } ?: index
        },
    ) { index ->
        itemContent(items[index])
    }
}