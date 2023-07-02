package ru.kbs41.retrofit_1c

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Credentials
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kbs41.retrofit_1c.datamodels.NewProduct
import ru.kbs41.retrofit_1c.datamodels.Product
import ru.kbs41.retrofit_1c.datamodels.ProductResponseModel
import ru.kbs41.retrofit_1c.ui.ListScreen
import ru.kbs41.retrofit_1c.ui.theme.Retrofit_1cTheme

class MainActivity : ComponentActivity() {

    val credential = Credentials.basic("Admin", "123456")

    val api: Api = Retrofit.Builder()
        .baseUrl("http://192.168.1.135:80/UT11/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {


            Retrofit_1cTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Column {

                        TestConnectionButton(api, credential)
                        Spacer(modifier = Modifier.height(6.dp))

                        AddNewProductScreen(api, credential)
                        Spacer(modifier = Modifier.height(6.dp))

                        GetByNameButton(api, credential)
                    }


                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewProductScreen(
    api: Api,
    credential: String
) {

    val coroutineScope = rememberCoroutineScope()
    var productName by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }

    Column {

        TextField(
            value = productName,
            onValueChange = { productName = it },
            placeholder = {
                Text(
                    text = "Имя нового товара"
                )
            }
        )

        TextField(
            value = code,
            onValueChange = { code = it },
            placeholder = {
                Text(
                    text = "Артикул"
                )
            }
        )

        Button(onClick = {
            coroutineScope.launch(Dispatchers.IO) {
                val data = NewProduct(productName, code)
                val apiInterface = api.addNewProduct(credential, data)
                try {
                    val response = apiInterface.execute()
                    Log.d("LEA", response.isSuccessful.toString())
                } catch (e: Exception) {
                    Log.e("LEA", e.localizedMessage ?: "")
                }
            }
        }) {
            Text(text = "Создать товар")
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetByNameButton(
    api: Api,
    credential: String
) {

    val coroutineScope = rememberCoroutineScope()
    var productName by remember { mutableStateOf("") }
    var productList: List<Product> by remember { mutableStateOf(emptyList()) }

    Column {

        TextField(value = productName, onValueChange = { productName = it })

        Button(onClick = {
            coroutineScope.launch(Dispatchers.IO) {
                val response: Response<ProductResponseModel>
                val apiInterface = api.getProductBuName(credential, productName)
                try {
                    response = apiInterface.execute()

                    if (response.isSuccessful) {
                        productList = response.body()?.products ?: emptyList()
                    }

                } catch (e: Exception) {
                    Log.e("LEA", e.localizedMessage ?: "")
                }
            }

        }) {
            Text(text = "Получить по имени")
        }

        ListScreen(list = productList)

    }


}

@Composable
fun TestConnectionButton(api: Api, credential: String) {

    val coroutineScope = rememberCoroutineScope()

    Button(onClick = {
        coroutineScope.launch(Dispatchers.IO) {
            val apiInterface = api.testConnection(credential)
            try {
                val response = apiInterface.execute()
                Log.d("LEA", response.isSuccessful.toString())
            } catch (e: Exception) {
                Log.e("LEA", e.localizedMessage ?: "")
            }
        }
    }) {
        Text(text = "Проверить соединение")
    }
}

