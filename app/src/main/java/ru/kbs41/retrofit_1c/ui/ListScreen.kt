package ru.kbs41.retrofit_1c.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kbs41.retrofit_1c.datamodels.Product


@Preview
@Composable
fun PreviewListScreen() {

    val list = listOf<Product>(
        Product("Ботинки", 2.0),
        Product("Штаны", 6.0),
        Product("Чебуреки", 16.0)
    )

    ListScreen(list = list)

}

@Composable
fun ListScreen(list: List<Product>?) {

    if (list == null) return

    LazyColumn {

        itemsIndexed(list) { _, product ->

            Row {

                Text(text = product.name)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = product.qty.toString())

            }

        }

    }

}
